package ru.sber.rdbms.factory

import java.sql.Connection

interface ConnectionFactory {

    fun openConnection(): Connection

    fun openTransactionalConnection(): Connection {
        val connection: Connection = openConnection()
        connection.autoCommit = false
        return connection
    }
}