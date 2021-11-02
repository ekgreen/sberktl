package com.github.ekgreen.springmvc.controller

import com.github.ekgreen.springmvc.auth.AuthServlet
import com.github.ekgreen.springmvc.auth.AuthTokenizer
import com.github.ekgreen.springmvc.book.BookingService
import com.github.ekgreen.springmvc.model.Contact
import com.github.ekgreen.springmvc.model.User
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import java.time.LocalDate
import javax.servlet.http.Cookie

@SpringBootTest
@AutoConfigureMockMvc
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
        bookingService.delete(contact.id!!)
    }

    fun authorizationCookie(): Cookie {
        return Cookie(AuthServlet.AUTHORIZATION, tokenizer.generateToken(User("Wall-E", "", "eva")))
    }

    @Test
    fun mainPage() {
        mockMvc.perform(
            post("/app/v1/book/main")
                .cookie(authorizationCookie())
        )
            .andExpect(status().isOk)
            .andExpect(view().name("main"))
            .andExpect(content().contentType("text/html;charset=UTF-8"))
    }

    @Test
    fun authorizationCheck() {
        mockMvc.perform(get("/app/v1/book"))
            .andExpect(status().isForbidden)
    }

    @Test
    fun getAddContact() {
        mockMvc.perform(
            get("/app/v1/book/add")
                .cookie(authorizationCookie())
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
        )
            .andExpect(status().isOk)
            .andExpect(view().name("main"))
            .andExpect(content().contentType("text/html;charset=UTF-8"))

        // пока что так, но нужно конечно все подмокать
        assertEquals(1, bookingService.list(context = "Галактический").size)
    }

    @Test
    fun contextSearch() {
        mockMvc.perform(
            get("/app/v1/book/list?search=${contact.lastName}")
                .cookie(authorizationCookie())
        )
            .andExpect(status().isOk)
            .andExpect(view().name("main"))
            .andExpect(content().contentType("text/html;charset=UTF-8"))
            .andExpect(content().string(containsString(contact.nickname)))
    }

    @Test
    fun viewContact() {
        mockMvc.perform(
            get("/app/v1/book/view/${contact.id}")
                .cookie(authorizationCookie())
        )
            .andExpect(status().isOk)
            .andExpect(view().name("view"))
            .andExpect(content().contentType("text/html;charset=UTF-8"))
            .andExpect(content().string(containsString(contact.nickname)))
    }

    @Test
    fun getEditContact() {
        mockMvc.perform(
            get("/app/v1/book/edit/${contact.id}")
                .cookie(authorizationCookie())
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
        )
            .andExpect(status().isOk)
            .andExpect(view().name("main"))
            .andExpect(content().contentType("text/html;charset=UTF-8"))

        assertEquals("+79999999998", contact.mobileNumber)
    }

    @Test
    fun deleteContact() {
        mockMvc.perform(
            post("/app/v1/book/delete/${contact.id}")
                .cookie(authorizationCookie())
        )
            .andExpect(status().isOk)
            .andExpect(view().name("view"))
            .andExpect(content().contentType("text/html;charset=UTF-8"))
            .andExpect(content().string(containsString(contact.nickname)))

        // пока что так, но нужно конечно все подмокать
        assertEquals(0, bookingService.list(context = contact.lastName).size)
    }
}