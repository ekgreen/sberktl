package com.github.ekgreen.springmvc.security.auth

import com.github.ekgreen.springmvc.book.da.UserRepository
import com.github.ekgreen.springmvc.book.model.converter.UserTransformation
import com.github.ekgreen.springmvc.book.model.converter.transformToAuthentication
import com.github.ekgreen.springmvc.book.model.da.User
import com.github.ekgreen.springmvc.book.model.dto.UserDto
import mu.KLogging
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.regex.Pattern

class AuthService(
    private val repository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val userMapper: UserTransformation,
    private val tokenizer: AuthTokenizer
    ) {

    // Minimum eight characters, at least one letter and one number
    private val PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}\$")

    /**
     * Метод регистрации нового пользователя в системе
     *
     * @return аутентификационный-токен или пусто,
     * если аутентификация завершилась неуспешно
     */
    fun signUp(userDto: UserDto): String? {
        val user: User = userMapper.transformToModel(userDto)

        // возможно эти данные надо отдавать на PL, чтобы пользователю было понятно почему он не зарегестрирован
        // но так как у нас тестовый пример, не будем усложнять
        if (repository.findByLogin(user.login!!) != null) {
            logger.warn { "Аутентификация. Пользователь с указанным логин уже существует { login = ${user.login} }" }
            return null
        }

        if(!PASSWORD_PATTERN.matcher(user.password!!).matches()) {
            logger.warn { "Аутентификация. Пользователь ввел слишком простой пароль или пароль с недопустимыми символами { login = ${user.login} }" }
            return null
        }

        user.password = passwordEncoder.encode(user.password)
        repository.save(user)

        return tokenizer.generateAuthToken(transformToAuthentication(user))
    }

    companion object : KLogging()
}