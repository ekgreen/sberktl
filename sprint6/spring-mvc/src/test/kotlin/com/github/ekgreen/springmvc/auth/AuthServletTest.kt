package com.github.ekgreen.springmvc.auth

import com.github.ekgreen.springmvc.model.dto.UserDto
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.servlet.ServletRegistrationBean
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.context.TestPropertySource
import org.springframework.web.client.HttpClientErrorException
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.WebContext
import java.io.PrintWriter
import java.io.StringWriter
import java.time.DayOfWeek
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.test.assertEquals

internal class AuthServletTest {

    private lateinit var authServlet: AuthServlet;

    private lateinit var authService: AuthService

    private lateinit var engine: TemplateEngine

    private lateinit var request: HttpServletRequest

    private lateinit var response: HttpServletResponse

    @BeforeEach
    fun setUp() {
        authService = mockk<AuthService>()

        engine      = mockk<TemplateEngine>()
        every { engine.process(any<String>(),any<WebContext>(),any()) } returns Unit

        request     = spyk<HttpServletRequest>()
        response    = spyk<HttpServletResponse>()

        authServlet = AuthServlet(authService, engine)
    }

    @ParameterizedTest
    @ValueSource(strings = [AuthServlet.LOG_IN_PATH, AuthServlet.SIGN_UP_PATH])
    fun `redirect on any get request`(path: String) {
        // given
        every { request.pathInfo } returns path

        // when
        authServlet.doGet(request, response)

        // then
        verify(exactly = 1) { response.writer }
        verify(exactly = 1) { engine.process("sign.html", any<WebContext>(), any()) }
    }

    @Test
    fun `empty request and response not supported`() {
        // then
        Assertions.assertThrows(HttpClientErrorException::class.java) {
            // when
            authServlet.doGet(null, null)
        }
    }

    @Test
    fun `path not supported`() {
        // given
        every { request.pathInfo } returns "/any"

        // when
        authServlet.doGet(request, response)

        // then
        verify(exactly = 1) { response.status = HttpStatus.NOT_FOUND.value() }
        verify(exactly = 1) { response.writer }
        verify(exactly = 1) { engine.process("sign.html", any<WebContext>(), any()) }
    }

    @Test
    fun `login request`() {
        // given
        val token: String = UUID.randomUUID().toString() // типо токен
        val nickname: String = "Wall-E"
        val password: String = "eva"

        every { authService.signIn(nickname,password) } returns token

        every { request.pathInfo } returns AuthServlet.LOG_IN_PATH
        every { request.contextPath } returns ""

        every { request.getParameter("nickname") } returns nickname
        every { request.getParameter("password") } returns password

        // when
        authServlet.doPost(request, response)

        // then
        verify(exactly = 1) { response.addHeader(AuthServlet.AUTHORIZATION, token) }
        verify(exactly = 1) { response.addHeader("Set-Cookie", any()) }
        verify(exactly = 1) { response.sendRedirect("/app/v1/book/main") }
    }

    @Test
    fun `unauthorized login request`() {
        // given
        val nickname: String = "Wall-E"
        val password: String = "eva"

        every { authService.signIn(nickname,password) } returns null

        every { request.pathInfo } returns AuthServlet.LOG_IN_PATH

        every { request.getParameter("nickname") } returns nickname
        every { request.getParameter("password") } returns password

        // when
        authServlet.doPost(request, response)

        // then
        verify(exactly = 1) { response.status = HttpStatus.UNAUTHORIZED.value() }
        verify(exactly = 1) { response.writer }
        verify(exactly = 1) { engine.process("sign.html", any<WebContext>(), any()) }
    }

    @Test
    fun `signup request`() {
        // given
        val token: String = UUID.randomUUID().toString() // типо токен
        val nickname: String = "Wall-E"
        val password: String = "eva"
        val email: String    = "walle@gmail.com"

        every { authService.signUp(UserDto(nickname,email,password)) } returns token

        every { request.pathInfo } returns AuthServlet.SIGN_UP_PATH
        every { request.contextPath } returns ""

        every { request.getParameter("nickname") } returns nickname
        every { request.getParameter("password") } returns password
        every { request.getParameter("email") }    returns email

        // when
        authServlet.doPost(request, response)

        // then
        verify(exactly = 1) { response.addHeader(AuthServlet.AUTHORIZATION, token) }
        verify(exactly = 1) { response.addHeader("Set-Cookie", any()) }
        verify(exactly = 1) { response.sendRedirect("/app/v1/book/main") }
    }

    @Test
    fun `not valid login request`() {
        // given
        val nickname: String = "Wall-E"
        val password: String = "eva"
        val email: String    = "walle@gmail.com"

        every { authService.signUp(UserDto(nickname,email,password)) } returns null

        every { request.pathInfo } returns AuthServlet.SIGN_UP_PATH

        every { request.getParameter("nickname") } returns nickname
        every { request.getParameter("password") } returns password
        every { request.getParameter("email") }    returns email

        // when
        authServlet.doPost(request, response)

        // then
        verify(exactly = 1) { response.status = HttpStatus.BAD_REQUEST.value() }
        verify(exactly = 1) { response.writer }
        verify(exactly = 1) { engine.process("sign.html", any<WebContext>(), any()) }
    }


    @ParameterizedTest
    @ValueSource(strings = [AuthServlet.LOG_IN_PATH, AuthServlet.SIGN_UP_PATH])
    fun `corrupted login on login or signup request`(path: String) {
        // given
        every { request.pathInfo } returns path

        val nickname: String = "Wall-E?id<>0"
        val password: String = "eva"
        val email: String    = "walle@gmail.com"

        every { request.getParameter("nickname") } returns nickname
        every { request.getParameter("password") } returns password
        every { request.getParameter("email") }    returns email

        // when
        authServlet.doPost(request, response)

        // then
        verify(exactly = 1) { response.status = HttpStatus.BAD_REQUEST.value() }
        verify(exactly = 1) { response.writer }
        verify(exactly = 1) { engine.process("sign.html", any<WebContext>(), any()) }
    }

    @ParameterizedTest
    @ValueSource(strings = [AuthServlet.LOG_IN_PATH, AuthServlet.SIGN_UP_PATH])
    fun `corrupted email on login or signup request`(path: String) {
        // given
        every { request.pathInfo } returns path

        val nickname: String = "Wall-E?id<>0"
        val password: String = "eva"
        val email: String    = "walle=_@_@_=gmail.com"

        every { request.getParameter("nickname") } returns nickname
        every { request.getParameter("password") } returns password
        every { request.getParameter("email") }    returns email

        // when
        authServlet.doPost(request, response)

        // then
        verify(exactly = 1) { response.status = HttpStatus.BAD_REQUEST.value() }
        verify(exactly = 1) { response.writer }
        verify(exactly = 1) { engine.process("sign.html", any<WebContext>(), any()) }
    }

}