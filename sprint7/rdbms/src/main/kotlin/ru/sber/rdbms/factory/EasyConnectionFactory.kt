package ru.sber.rdbms.factory

import java.sql.Connection
import java.sql.DriverManager

class EasyConnectionFactory(
    private val url: String,
    private val user: String,
    private val password: String
): ConnectionFactory {
    override fun openConnection(): Connection {
        return DriverManager.getConnection(
            url,
            user,
            password
        )
    }
}