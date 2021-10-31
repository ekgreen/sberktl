package com.github.ekgreen.springmvc.da

import com.github.ekgreen.springmvc.model.User
import java.util.concurrent.ConcurrentHashMap

class UserRepository(private val users: MutableMap<String, User> = ConcurrentHashMap()) {

    init {
        users["Wall-E"] = User(
            login     = "Wall-E",
            password  = "\$2a\$10\$KrCvQStIcK4RlouGpyi86utKl0aKgrwrWk9Y7q3xYhzw.3RuvJj8W",
            email     = "walle@space.in"
        )
    }

    fun findByLogin(login: String): User? {
        return users[login]
    }

    fun findAll(): List<User> {
        return ArrayList(users.values)
    }

    fun save(user: User): User {
        users[user.login] = user
        return user
    }
}