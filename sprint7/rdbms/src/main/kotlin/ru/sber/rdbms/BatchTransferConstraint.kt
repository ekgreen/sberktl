package ru.sber.rdbms

import mu.KLogging
import ru.sber.rdbms.api.AccountTransfer
import ru.sber.rdbms.factory.ConnectionFactory
import java.sql.SQLException

class BatchTransferConstraint(private val connectionFactory: ConnectionFactory) : AccountTransfer {

    override fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        connectionFactory.openTransactionalConnection().use { connection ->
            try {
                val statement =
                    connection.createStatement()

                statement.addBatch("update rdbms.account set amount = amount - $amount where id = $accountId1")
                statement.addBatch("update rdbms.account set amount = amount + $amount where id = $accountId2")

                statement.use { it.executeBatch() }
                connection.commit()
            } catch (exception: SQLException) {
                logger.warn(exception) { "transfer [$amount] $accountId1 -> $accountId2 was canceled" }
                connection.rollback()
            }
        }
    }

    companion object : KLogging()
}
