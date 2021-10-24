package com.github.ekgreen.springmvc.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.ekgreen.springmvc.model.User
import com.github.ekgreen.springmvc.model.dto.UserDto
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException
import java.util.regex.Pattern
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class AuthServlet(private val authService: AuthService, private val objectMapper: ObjectMapper) : HttpServlet() {

    companion object {
        const val AUTHORIZATION: String = "Authorization"

        const val LOG_IN_PATH : String = "/login"
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
        // Регулярное выражение для имени и фамилии клиента
        private val namePattern: Pattern = Pattern.compile("^[a-zA-Z0-9-]{2,64}$")
    }

    override fun doPost(request: HttpServletRequest?, response: HttpServletResponse?) {
        // 1. проверим что объекты запроса и ответа не пустые
        if(request == null || response == null)
            throw HttpClientErrorException(HttpStatus.BAD_REQUEST, "missed request and/or response")

        // 2. проверим что путь нам подходит
        val pathInfo: String = request.pathInfo

        if(!pathPattern.matcher(pathInfo).matches())
            throw HttpClientErrorException(HttpStatus.NOT_FOUND, "path not supported")

        // 3. проверим что в теле JSON (хотя бы по заголовкам)
        val contentType: String? = request.getHeader("Content-Type")

        if(contentType == null || contentType.isEmpty() || !contentType.startsWith("application/json"))
            throw HttpClientErrorException(HttpStatus.BAD_REQUEST, "content not JSON")

        when(pathInfo){
            SIGN_UP_PATH -> handleSignUpRequest(request, response)
            LOG_IN_PATH -> handleLogInRequest(request, response)
        }
    }

    private fun handleSignUpRequest(request: HttpServletRequest, response: HttpServletResponse) {
        val dto: UserDto = validateForm(objectMapper.readValue<UserDto>(request.inputStream))

        val token: String = authService.signUp(userDto = dto)
            ?: throw HttpClientErrorException(HttpStatus.BAD_REQUEST, "not valid login or password")

        addTokenHeader(token, response)
    }

    private fun handleLogInRequest(request: HttpServletRequest, response: HttpServletResponse) {
        val dto: UserDto = validateForm(objectMapper.readValue<UserDto>(request.inputStream))

        val token: String = authService.signIn(login = dto.login, password = dto.password)
            ?: throw HttpClientErrorException(HttpStatus.UNAUTHORIZED, "not valid login or password")

        addTokenHeader(token, response)
    }

    private fun addTokenHeader(token: String, response: HttpServletResponse) {
        response.addHeader(AUTHORIZATION, token)
    }

    private fun validateForm(dto: UserDto?): UserDto {
        if(dto == null)
            throw HttpClientErrorException(HttpStatus.BAD_REQUEST, "empty object")

        if(!loginPattern.matcher(dto.login).matches())
            throw HttpClientErrorException(HttpStatus.BAD_REQUEST, "login not faced with pattern")

        if(dto.firstName != null && !namePattern.matcher(dto.firstName).matches())
            throw HttpClientErrorException(HttpStatus.BAD_REQUEST, "first name not faced with pattern")

        if(dto.lastName != null && !namePattern.matcher(dto.lastName).matches())
            throw HttpClientErrorException(HttpStatus.BAD_REQUEST, "last name not faced with pattern")

        if(dto.email != null && !emailPattern.matcher(dto.email).matches())
            throw HttpClientErrorException(HttpStatus.BAD_REQUEST, "email not faced with pattern")

        return dto
    }

}