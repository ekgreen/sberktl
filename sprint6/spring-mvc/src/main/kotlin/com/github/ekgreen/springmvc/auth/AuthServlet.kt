package com.github.ekgreen.springmvc.auth

import com.github.ekgreen.springmvc.model.dto.UserDto
import mu.KLogging
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.WebContext
import java.util.regex.Pattern
import javax.servlet.RequestDispatcher
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class AuthServlet(private val authService: AuthService, private val engine: TemplateEngine) : HttpServlet() {

    companion object : KLogging() {
        const val AUTHORIZATION: String = "Authorization"

        const val LOG_IN_PATH: String = "/login"
        const val SIGN_UP_PATH: String = "/signup"

        /**
         * Регулярное выражение для e-mail [RFC 5322, немного урезанный - для латиницы]
         * PS не очень понял, что имеется ввиду под валидацией домена (если на какие-то конкретные, то можно переделать)
         *
         * @see <a href="https://i.stack.imgur.com/YI6KR.png">RFC 5322</a>
         */
        private val emailPattern: Pattern =
            Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?")

        // Регулярное выражение для доступных путей
        private val pathPattern: Pattern = Pattern.compile("^(${LOG_IN_PATH}|${SIGN_UP_PATH})$")

        // Регулярное выражение для логина клиента
        private val loginPattern: Pattern = Pattern.compile("^[a-zA-Z0-9-_]{4,32}$")
    }

    public override fun doGet(request: HttpServletRequest?, response: HttpServletResponse?) {
        doRequest(request, response)
    }

    public override fun doPost(request: HttpServletRequest?, response: HttpServletResponse?) {
        doRequest(
            request,
            response,
            doSignUp = this::handleSignUpRequest,
            doSignIn = this::handleLogInRequest,
            redirect = this::forwardToMainPage
        )
    }

    private fun doRequest(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        doSignIn: (HttpServletRequest, HttpServletResponse, WebContext) -> Unit = { _, _, _ -> },
        doSignUp: (HttpServletRequest, HttpServletResponse, WebContext) -> Unit = { _, _, _ -> },
        redirect: (HttpServletRequest, HttpServletResponse, WebContext) -> Unit = this::showAuthPage,
    ) {
        // 1. проверим что объекты запроса и ответа не пустые
        if (request == null || response == null)
            throw HttpClientErrorException(HttpStatus.BAD_REQUEST, "missed request and/or response")

        // 1.1 Контекст для шаблонизатора
        val context: WebContext = WebContext(request, response, request.servletContext)

        try {
            // 2. проверим что путь нам подходит
            val pathInfo: String = request.pathInfo

            if (!pathPattern.matcher(pathInfo).matches())
                throw HttpClientErrorException(HttpStatus.NOT_FOUND, "path not supported")

            when (pathInfo) {
                SIGN_UP_PATH -> {
                    context.setVariable("signup", true)
                    doSignUp(request, response, context)
                }
                LOG_IN_PATH -> {
                    context.setVariable("signup", false)
                    doSignIn(request, response, context)
                }
            }

            redirect(request,response,context)
        } catch (clientException: HttpClientErrorException) {
            logger.warn { "user request failed { status=${clientException.rawStatusCode}, message=${clientException.message} }" }

            response.status = clientException.rawStatusCode
            // можно добавить какой-нибудь текст ошибки в контекст и отобразить его пользователю
            response.writer.use { engine.process("sign.html", context, it) }
        }
    }

    private fun showAuthPage(request: HttpServletRequest, response: HttpServletResponse, context: WebContext) {
        response.writer.use { engine.process("sign.html", context, it) }
    }

    private fun handleSignUpRequest(request: HttpServletRequest, response: HttpServletResponse, context: WebContext) {
        val dto: UserDto = validateForm(getSignRequest(request))

        val token: String = authService.signUp(userDto = dto)
            ?: throw HttpClientErrorException(HttpStatus.BAD_REQUEST, "not valid login or password")

        addTokenHeader(token, response)
    }

    private fun handleLogInRequest(request: HttpServletRequest, response: HttpServletResponse, context: WebContext) {
        val dto: UserDto = validateForm(getSignRequest(request))

        val token: String = authService.signIn(login = dto.login!!, password = dto.password!!)
            ?: throw HttpClientErrorException(HttpStatus.UNAUTHORIZED, "not valid login or password")

        addTokenHeader(token, response)
    }

    private fun forwardToMainPage(request: HttpServletRequest, response: HttpServletResponse, context: WebContext) {
        response.sendRedirect(request.contextPath + "/app/v1/book/main")
    }

    private fun getSignRequest(request: HttpServletRequest): UserDto {
        return UserDto(
            login    = request.getParameter("nickname"),
            password = request.getParameter("password"),
            email    = request.getParameter("email")
        )
    }

    private fun addTokenHeader(token: String, response: HttpServletResponse) {
        response.addHeader(AUTHORIZATION, token)
        response.addHeader("Set-Cookie", "$AUTHORIZATION=$token; path=/")
    }

    private fun validateForm(dto: UserDto?): UserDto {
        if (dto == null)
            throw HttpClientErrorException(HttpStatus.BAD_REQUEST, "empty object")

        if (dto.login == null || !loginPattern.matcher(dto.login).matches())
            throw HttpClientErrorException(HttpStatus.BAD_REQUEST, "login not faced with pattern")

        if (dto.email != null && !emailPattern.matcher(dto.email).matches())
            throw HttpClientErrorException(HttpStatus.BAD_REQUEST, "email not faced with pattern")

        return dto
    }

}