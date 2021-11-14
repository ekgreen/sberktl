package com.github.ekgreen.springmvc.book.da

import com.github.ekgreen.springmvc.book.model.da.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, String> {

    fun findByLogin(username: String): User?
}