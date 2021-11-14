package com.github.ekgreen.springmvc.security.service

import com.github.ekgreen.springmvc.book.da.UserRepository
import com.github.ekgreen.springmvc.book.model.converter.transformToAuthentication
import com.github.ekgreen.springmvc.book.model.da.User
import mu.KLogging
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

class UserDetailsServiceImpl(private val repository: UserRepository) : UserDetailsService{

    override fun loadUserByUsername(username: String): UserDetails {
        // получим пользователя
        val user: User? = repository.findByLogin(username)

        if(user == null) {
            logger.debug("Query returned no results for user '$username'")
            throw UsernameNotFoundException("username '$username' not found")
        }

        // сконвертируем его в пользователя для атентификации
        return transformToAuthentication(user)
    }

    companion object: KLogging()
}