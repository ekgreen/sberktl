package ru.sber.rdbms

import mu.KLogging
import ru.sber.rdbms.api.AccountTransfer
import ru.sber.rdbms.api.ReUsableTransferRelease
import ru.sber.rdbms.api.TransferRelease
import ru.sber.rdbms.factory.ConnectionFactory
import java.sql.SQLException

class TransferBatchConstraint(private val connectionFactory: ConnectionFactory) : AccountTransfer {

    override fun transfer(decrementAccountId: Long, incrementAccountId: Long, amount: Long) {
        connectionFactory.openTransactionalConnection().use { connection ->
            try {
                val transferRelease: TransferRelease = ReUsableTransferRelease(decrementAccountId, incrementAccountId)

                val statement =
                    connection.createStatement()

                transferRelease.release(
                    decrementCallback = { statement.addBatch("update rdbms.account set amount = amount - $amount where id = $it") },
                    incrementCallback = { statement.addBatch("update rdbms.account set amount = amount + $amount where id = $it") }
                )

                statement.use { it.executeBatch() }

                connection.commit()
            } catch (exception: SQLException) {
                logger.warn(exception) { "transfer [$amount] $decrementAccountId -> $incrementAccountId was canceled" }
                connection.rollback()
            }
        }
    }

    companion object : KLogging()
}
