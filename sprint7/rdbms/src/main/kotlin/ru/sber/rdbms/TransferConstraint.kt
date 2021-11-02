package ru.sber.rdbms

import mu.KLogging
import ru.sber.rdbms.api.AccountTransfer
import ru.sber.rdbms.factory.ConnectionFactory
import java.sql.SQLException

class TransferConstraint(private val connectionFactory: ConnectionFactory) : AccountTransfer {

    override fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        connectionFactory.openTransactionalConnection().use { connection ->
            try {
                val decrement =
                    connection.prepareStatement("update rdbms.account set amount = amount - $amount where id = $accountId1")
                decrement.use { it.executeUpdate() }

                val increment =
                    connection.prepareStatement("update rdbms.account set amount = amount + $amount where id = $accountId2")
                increment.use { it.executeUpdate() }

                connection.commit()
            } catch (exception: SQLException) {
                logger.warn(exception) { "transfer [$amount] $accountId1 -> $accountId2 was canceled" }
                connection.rollback()
            }
        }
    }

    companion object : KLogging()
}
