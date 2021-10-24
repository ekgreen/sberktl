package com.github.ekgreen.springmvc.auth

import com.github.ekgreen.springmvc.model.User

interface AuthTokenizer {

    /**
     * Метод генерирует аутентификационный токен, могут быть различные методы, например JWT
     *
     * @return аутентификационный токен
     */
    fun generateToken(user: User): String

    /**
     * Метод валидирует токен на консистентность и актуальность
     *
     * @return валидный или не валидный токен
     */
    fun validateToken(token: String): Boolean
}