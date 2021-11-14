package com.github.ekgreen.springmvc.book.controller

import com.github.ekgreen.springmvc.security.auth.AuthTokenizer
import com.github.ekgreen.springmvc.book.BookingService
import com.github.ekgreen.springmvc.book.model.da.Contact
import com.github.ekgreen.springmvc.security.BookSecurityConfiguration
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import java.time.LocalDate
import javax.servlet.http.Cookie

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username = "Wall-E", password = "eva", authorities = ["ROLE_ADMIN"])
@TestPropertySource(properties = ["AUTH_SERVICE_SECRET=elephant_on_the_street_playing_with_boy"])
internal class AddressBookControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var bookingService: BookingService

    @Autowired
    private lateinit var tokenizer: AuthTokenizer

    // default contact in service
    private lateinit var contact: Contact

    @BeforeEach
    fun setUp() {
        contact = bookingService.add(
            Contact(
                nickname = "walle",
                firstName = "Валли",
                lastName = "Космический",
                birthDate = LocalDate.of(1990, 10, 10),
                location = "Space",
                email = "walle@gmail.com",
                mobileNumber = "+79999999999"
            )
        )
    }

    @AfterEach
    fun tearDown() {
        bookingService.clear()
    }

    fun authorizationCookie(): Cookie {
        return Cookie(
            BookSecurityConfiguration.AUTHORIZATION,
            tokenizer.generateAuthToken(User("Wall-E", "eva", listOf(SimpleGrantedAuthority("ROLE_ADMIN"))))
        )
    }

    @Test
    fun mainPage() {
        mockMvc.perform(
            post("/app/v1/book/main")
                .cookie(authorizationCookie())
                .with(csrf())
        )
            .andExpect(status().isOk)
            .andExpect(view().name("main"))
            .andExpect(content().contentType("text/html;charset=UTF-8"))
    }

    @Test
    fun getAddContact() {
        mockMvc.perform(
            get("/app/v1/book/add")
                .cookie(authorizationCookie())
                .with(csrf())
        )
            .andExpect(status().isOk)
            .andExpect(view().name("add"))
            .andExpect(content().contentType("text/html;charset=UTF-8"))
    }

    @Test
    fun postAddContact() {
        // given
        val model: MultiValueMap<String, String> = LinkedMultiValueMap()
        model.add("firstName", "Валли")
        model.add("lastName", "Галактический")
        model.add("nickname", "walle")
        model.add("location", "Space")

        mockMvc.perform(
            post("/app/v1/book/add")
                .cookie(authorizationCookie())
                .params(model)
                .with(csrf())
        )
            .andExpect(status().isOk)
            .andExpect(view().name("main"))
            .andExpect(content().contentType("text/html;charset=UTF-8"))

        // пока что так, но нужно конечно все подмокать
        assertEquals(1, bookingService.list(context = "Галактический").size)
    }

    @Test
    fun `post contact by API`() {
        // given
        val model: MultiValueMap<String, String> = LinkedMultiValueMap()
        model.add("firstName", "Валли")
        model.add("lastName", "Галактический")
        model.add("nickname", "walle")
        model.add("location", "Space")

        mockMvc.perform(
            post("/api/v1/book/add")
                .cookie(authorizationCookie())
                .content("""{"firstName": "Валли", "lastName": "Галактический", "nickname": "walle", "location": "Space"}""")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
        )
            .andExpect(status().isCreated)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))

        // пока что так, но нужно конечно все подмокать
        assertEquals(1, bookingService.list(context = "Галактический").size)
    }

    @Test
    fun contextSearch() {
        mockMvc.perform(
            get("/app/v1/book/list?search=${contact.lastName}")
                .cookie(authorizationCookie())
                .with(csrf())
        )
            .andExpect(status().isOk)
            .andExpect(view().name("main"))
            .andExpect(content().contentType("text/html;charset=UTF-8"))
            .andExpect(content().string(containsString(contact.nickname)))
    }

    @Test
    fun `context search by API`() {
        mockMvc.perform(
            get("/api/v1/book/list?search=${contact.lastName}")
                .cookie(authorizationCookie())
                .with(csrf())
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string(containsString(contact.nickname)))
    }

    @Test
    fun viewContact() {
        mockMvc.perform(
            get("/app/v1/book/view/${contact.id}")
                .cookie(authorizationCookie())
                .with(csrf())
        )
            .andExpect(status().isOk)
            .andExpect(view().name("view"))
            .andExpect(content().contentType("text/html;charset=UTF-8"))
            .andExpect(content().string(containsString(contact.nickname)))
    }

    @Test
    fun `view contact by API`() {
        mockMvc.perform(
            get("/api/v1/book/list?search=${contact.lastName}")
                .cookie(authorizationCookie())
                .with(csrf())
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string(containsString(contact.nickname)))
    }

    @Test
    fun getEditContact() {
        mockMvc.perform(
            get("/app/v1/book/edit/${contact.id}")
                .cookie(authorizationCookie())
                .with(csrf())
        )
            .andExpect(status().isOk)
            .andExpect(view().name("edit"))
            .andExpect(content().contentType("text/html;charset=UTF-8"))
            .andExpect(content().string(containsString(contact.nickname)))
    }

    @Test
    fun postEditContact() {
        // given
        val model: MultiValueMap<String, String> = LinkedMultiValueMap()
        model.add("firstName", contact.firstName)
        model.add("lastName", contact.lastName)
        model.add("nickname", contact.nickname)
        model.add("location", contact.location)
        model.add("mobileNumber", "+79999999998")

        // then
        mockMvc.perform(
            post("/app/v1/book/edit/${contact.id}")
                .cookie(authorizationCookie())
                .params(model)
                .with(csrf())
        )
            .andExpect(status().isOk)
            .andExpect(view().name("main"))
            .andExpect(content().contentType("text/html;charset=UTF-8"))

        assertEquals("+79999999998", contact.mobileNumber)
    }

    @Test
    fun `edit contact by API`() {
        // given

        // then
        mockMvc.perform(
            patch("/api/v1/book/edit/${contact.id}")
                .cookie(authorizationCookie())
                .content("""{
                    |"firstName": "${contact.firstName}", 
                    |"lastName": "${contact.lastName}", 
                    |"nickname": "${contact.nickname}", 
                    |"location": "${contact.location}", 
                    |"mobileNumber": "+79999999998"
                    |}""".trimMargin())
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))

        assertEquals("+79999999998", contact.mobileNumber)
    }

    @Test
    fun deleteContact() {
        mockMvc.perform(
            post("/app/v1/book/delete/${contact.id}")
                .cookie(authorizationCookie())
                .with(csrf())
        )
            .andExpect(status().isOk)
            .andExpect(view().name("view"))
            .andExpect(content().contentType("text/html;charset=UTF-8"))
            .andExpect(content().string(containsString(contact.nickname)))

        // пока что так, но нужно конечно все подмокать
        assertEquals(0, bookingService.list(context = contact.lastName).size)
    }

    @Test
    fun `delete contact by API`() {
        mockMvc.perform(
            delete("/api/v1/book/delete/${contact.id}")
                .cookie(authorizationCookie())
                .with(csrf())
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string(containsString(contact.nickname)))

        // пока что так, но нужно конечно все подмокать
        assertEquals(0, bookingService.list(context = contact.lastName).size)
    }
}