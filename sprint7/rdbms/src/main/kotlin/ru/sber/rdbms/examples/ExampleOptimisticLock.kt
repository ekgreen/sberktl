package ru.sber.rdbms

import java.sql.DriverManager
import java.sql.SQLException

fun main() {
    val connection = DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/db",
        "postgres",
        "postgres"
    )
    connection.use { conn ->
        val autoCommit = conn.autoCommit
        try {
            conn.autoCommit = false
            val prepareStatement1 = conn.prepareStatement("select * from rdbms.account where id = 1 for update")
            prepareStatement1.use { statement ->
                statement.executeQuery()
            }
            val prepareStatement2 = conn.prepareStatement("update rdbms.account set amount = amount - 100 where id = 1")
            prepareStatement2.use { statement ->
                statement.executeUpdate()
            }
            conn.commit()
        } catch (exception: SQLException) {
            exception.printStackTrace()
            conn.rollback()
        } finally {
            conn.autoCommit = autoCommit
        }
    }
}


