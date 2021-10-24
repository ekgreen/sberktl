package com.github.ekgreen.springmvc.da

import com.github.ekgreen.springmvc.model.User
import java.util.concurrent.ConcurrentHashMap

class UserRepository(private val users: MutableMap<String, User> = ConcurrentHashMap()) {

    init {
        users["Wall-E"] = User(
            login     = "Wall-E",
            password  = "\$2a\$10\$AS8VMex2OWKeH/.PMnZRv.G8rnM/TZS4Q1pW6Sp4R0e9OUDB6Zq8m",
            firstName = "Wall",
            lastName  = "E",
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