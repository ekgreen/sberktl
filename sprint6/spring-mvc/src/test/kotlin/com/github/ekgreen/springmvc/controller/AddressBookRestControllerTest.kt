package com.github.ekgreen.springmvc.controller

import com.github.ekgreen.springmvc.book.BookingService
import com.github.ekgreen.springmvc.model.Contact
import com.github.ekgreen.springmvc.model.converter.ContactTransformation
import com.github.ekgreen.springmvc.model.dto.ContactDto
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.*
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.test.context.TestPropertySource
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import java.time.LocalDate


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(properties = ["auth.secret=elephant_on_the_street_playing_with_boy_321"])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class AddressBookRestControllerTest {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var testRestTemplate: TestRestTemplate

    @Autowired
    private lateinit var bookingService: BookingService

    @Autowired
    private lateinit var mapper: ContactTransformation

    // default contact in service
    private lateinit var contact: Contact

    @BeforeAll
    fun beforeAll() {
        val requestFactory = HttpComponentsClientHttpRequestFactory()
        requestFactory.setConnectTimeout(Companion.TIMEOUT)
        requestFactory.setReadTimeout(Companion.TIMEOUT)

        testRestTemplate.restTemplate.setRequestFactory(requestFactory)
    }

    @BeforeEach
    fun setUp(){
        contact = bookingService.add(Contact(
            nickname = "walle",
            firstName = "Валли",
            lastName = "Космический",
            birthDate = LocalDate.of(1990, 10, 10),
            location = "Space",
            email = "walle@gmail.com",
            mobileNumber = "+79999999999"
        ))
    }

    @AfterEach
    fun tearDown() {
        bookingService.delete(contact.id!!)
    }

    fun authorizationHeaders(): HttpHeaders {
        // create sign-in request
        val request: MultiValueMap<String, String> = LinkedMultiValueMap()
        request.set("nickname", "Wall-E")
        request.set("password", "eva")

        // send
        val response = testRestTemplate.postForEntity("http://localhost:${port}/auth/login", HttpEntity(request, HttpHeaders()), String::class.java)

        // create request headers
        val headers: HttpHeaders = HttpHeaders()
        headers.add("Cookie", response!!.headers["Set-Cookie"]!![0])

        return headers
    }

    @Test
    fun addContact() {
        // given
        val expected = ContactDto(
            id = null,
            nickname = "walle2",
            firstName = "Валли",
            middleName = null,
            lastName = "Космический",
            birthDate = LocalDate.of(1990, 11, 11),
            location = "Space",
            email = "walle@gmail.com",
            mobileNumber = "+79999999999"
        )


        // when
        val actual = testRestTemplate.exchange(
            "http://localhost:${port}/api/v1/book/add",
            HttpMethod.POST,
            HttpEntity(expected, authorizationHeaders()),
            ContactDto::class.java
        )

        // then
        assertEquals(HttpStatus.CREATED, actual.statusCode)

        val id = actual.body!!.id!!
        assertNotNull(bookingService.get(id))

        // after
        bookingService.delete(id)
    }

    @Test
    fun contextSearch() {
        // when
        val actual = testRestTemplate.exchange(
            "http://localhost:${port}/api/v1/book/list?search=${contact.lastName}",
            HttpMethod.GET,
            HttpEntity(null, authorizationHeaders()),
            Array<out ContactDto>::class.java
        )

        // then
        assertEquals(HttpStatus.OK, actual.statusCode)
        assertNotNull(actual.body)
        assertEquals(contact, mapper.transformToModel(actual.body!![0]))
    }

    @Test
    fun viewContact() {
        // when
        val actual = testRestTemplate.exchange(
            "http://localhost:${port}/api/v1/book/view/${contact.id}",
            HttpMethod.GET,
            HttpEntity(null, authorizationHeaders()),
            ContactDto::class.java
        )

        // then
        assertEquals(HttpStatus.OK, actual.statusCode)
        assertNotNull(actual.body)
        assertEquals(contact, mapper.transformToModel(actual.body!!))
    }

    @Test
    fun editContact() {
        // given
        val expected = contact.copy(mobileNumber = "+79999999998")

        // when
        val actual = testRestTemplate.exchange(
            "http://localhost:${port}/api/v1/book/edit/${contact.id}",
            HttpMethod.PATCH,
            HttpEntity(expected, authorizationHeaders()),
            ContactDto::class.java
        )

        // then
        assertEquals(HttpStatus.OK, actual.statusCode)
        assertNotNull(actual.body)
        assertEquals(expected, mapper.transformToModel(actual.body!!))
    }

    @Test
    fun deleteContact() {
        // when
        val actual = testRestTemplate.exchange(
            "http://localhost:${port}/api/v1/book/delete/${contact.id}",
            HttpMethod.DELETE,
            HttpEntity(null, authorizationHeaders()),
            ContactDto::class.java
        )

        // then
        assertEquals(HttpStatus.OK, actual.statusCode)
        assertNotNull(actual.body)
        assertEquals(contact, mapper.transformToModel(actual.body!!))
        assertNull(bookingService.get(contact.id!!))
    }

    companion object {
        const val TIMEOUT: Int = 1000
    }
}