package ru.sber.rdbms

import mu.KLogging
import ru.sber.rdbms.api.*
import ru.sber.rdbms.factory.ConnectionFactory
import java.sql.*
import java.util.*

class TransferWithLock(private val connectionFactory: ConnectionFactory, private val lock: Lock = Lock.OPTIMISTIC) :
    AccountTransfer {

    override fun transfer(decrementAccountId: Long, incrementAccountId: Long, amount: Long) {
        connectionFactory.openTransactionalConnection().use { connection ->
            try {
                createChange(connection, decrementAccountId, incrementAccountId)
                    .change(amount, connection)

                connection.commit()
            } catch (exception: SQLException) {
                logger.warn(exception) { "transfer [$amount] $decrementAccountId -> $incrementAccountId was canceled" }
                connection.rollback()
            }
        }
    }

    private fun createChange(connection: Connection, decrementAccountId: Long, incrementAccountId: Long): Change {
        return when (lock) {
            Lock.OPTIMISTIC  -> createOptimisticChange(connection, decrementAccountId, incrementAccountId)
            Lock.PESSIMISTIC -> createPessimisticChange(connection, decrementAccountId, incrementAccountId)
        }
    }

    private fun createOptimisticChange(connection: Connection, decrementAccountId: Long, incrementAccountId: Long): Change {
        val statement: PreparedStatement =
            connection.prepareStatement("select id, amount, version from rdbms.account where id in ($decrementAccountId,$incrementAccountId)")

        statement.use { it.executeQuery().use { result ->
                result.next() // first account
                val account1: Account = mapAccountFromResultSet(result)
                result.next() // second account
                val account2: Account = mapAccountFromResultSet(result)

                return OptimisticChange(
                    if (account1.id == decrementAccountId) account1 else account2,
                    if (account1.id == incrementAccountId) account1 else account2,
                )
            }
        }
    }

    private fun createPessimisticChange(connection: Connection, decrementAccountId: Long, incrementAccountId: Long): Change {
        val transferRelease: TransferRelease = ReUsableTransferRelease(decrementAccountId, incrementAccountId)

        return transferRelease.release(
            decrementCallback = { lockAndGetRow(connection, it, lock = RowLock.ON_UPDATE) },
            incrementCallback = { lockAndGetRow(connection, it, lock = RowLock.ON_UPDATE) }
        ).use { decrementAccount, incrementAccount -> OptimisticChange(decrementAccount,incrementAccount, transferRelease)}
    }

    private fun lockAndGetRow(connection: Connection, accountId: Long, lock: RowLock = RowLock.ON_UPDATE): Account {
        val statement: PreparedStatement = when(lock){
            RowLock.ON_UPDATE ->  connection.prepareStatement("select id, amount, version from rdbms.account where id = $accountId for update")
        }

        statement.use { it.executeQuery().use { result ->
            result.next()
            return mapAccountFromResultSet(result)
        } }
    }

    // используем класс оптимистичной блокировки и для пессимистичной: переиспользуем код и некое наследование в иерархии блокировок
    // TransferRelease передается как параметр, для поддержания консистентности последовательности вызовов
    private inner class OptimisticChange(
        private val decrementAccount: Account,
        private val incrementAccount: Account,
        val transferRelease: TransferRelease = ReUsableTransferRelease(decrementAccount.id,incrementAccount.id)
    ) : Change {

        override fun change(amount: Long, connection: Connection) {
            if(logger.isDebugEnabled)
                logger.debug("change between ${decrementAccount.id}[${decrementAccount.amount}] and ${incrementAccount.id}[${incrementAccount.amount}] amount of $amount")

            if (decrementAccount.amount - amount < 0)
                throw SQLException("transfer rejected, decrement-account's \"amount\" might be equal or greater than 0")

            transferRelease.release(
                decrementCallback = { executeUpdate(connection, "update rdbms.account set amount = amount - $amount, version = version + 1 where id = ${decrementAccount.id} and version = ${decrementAccount.version}") },
                incrementCallback = { executeUpdate(connection, "update rdbms.account set amount = amount + $amount, version = version + 1 where id = ${incrementAccount.id} and version = ${incrementAccount.version}") }
            )
        }
    }

    private fun executeUpdate(connection: Connection, sql: String){
        connection.prepareStatement(sql).use {
            if(it.executeUpdate() != 1)
                throw SQLException("transfer rejected, decrement/increment-account's \"version\" were changed by other process during statement execution")
        }
    }

    private fun mapAccountFromResultSet(resultSet: ResultSet): Account {
        return Account(resultSet.getLong(1), resultSet.getLong(2), resultSet.getLong(3))
    }

    private interface Change {
        fun change(amount: Long, connection: Connection)
    }

    data class Account(val id: Long, val amount: Long, val version: Long)
    companion object : KLogging()
}
