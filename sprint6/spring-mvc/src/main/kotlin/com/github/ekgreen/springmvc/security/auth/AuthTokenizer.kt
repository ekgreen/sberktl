package com.github.ekgreen.springmvc.security.auth

import org.springframework.security.core.userdetails.UserDetails

interface AuthTokenizer {

    /**
     * Метод генерирует аутентификационный токен, могут быть различные методы, например JWT
     *
     * @return аутентификационный токен
     */
    fun generateAuthToken(user: UserDetails): String

    /**
     * Метод валидирует токен на консистентность и актуальность
     *
     * @return валидный или не валидный токен
     */
    fun validateToken(token: String): Boolean

    /**
     * Метод возвращает username [subject] пользователя, который совершает запрос
     *
     * @return уникальное имя пользователя
     */
    fun getSubject(token: String): String?
}