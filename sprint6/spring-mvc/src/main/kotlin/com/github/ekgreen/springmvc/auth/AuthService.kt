package com.github.ekgreen.springmvc.auth

import com.github.ekgreen.springmvc.da.UserRepository
import com.github.ekgreen.springmvc.model.User
import com.github.ekgreen.springmvc.model.converter.UserTransformation
import com.github.ekgreen.springmvc.model.dto.UserDto
import mu.KLogging
import org.mapstruct.factory.Mappers
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Repository
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
     * Метод аутентификации клиента по связке логин-пароль
     *
     * @param login     логин клиента - валидный (проверенный на корректность)
     * @param password  пароль
     *
     * @return аутентификационный-токен или пусто,
     * если аутентификация завершилась неуспешно
     */
    fun signIn(login: String, password: String): String? {
        val user: User? = repository.findByLogin(login)

        if(user == null){
            logger.warn { "Аутентификация. Пользователь не найден { login = $login }" }
            return null
        }

        if(!passwordEncoder.matches(password, user.password)) {
            logger.warn { "Аутентификация. Пользователь ввел неверную связку логин-пароль { login = $login }" }
            return null
        }

        // по-простому, без рефреш токенов
        return tokenizer.generateToken(user)
    }

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
        if (repository.findByLogin(user.login) != null) {
            logger.warn { "Аутентификация. Пользователь с указанным логин уже существует { login = ${user.login} }" }
            return null
        }

        if(!PASSWORD_PATTERN.matcher(user.password).matches()) {
            logger.warn { "Аутентификация. Пользователь ввел слишком простой пароль или пароль с недопустимыми символами { login = ${user.login} }" }
            return null
        }

        user.password = passwordEncoder.encode(user.password)
        repository.save(user)

        return tokenizer.generateToken(user)
    }

    companion object : KLogging()
}