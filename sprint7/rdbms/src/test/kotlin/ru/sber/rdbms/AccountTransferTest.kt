package ru.sber.rdbms

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import liquibase.Contexts
import liquibase.Liquibase
import liquibase.database.Database
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import mu.KLogging
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import ru.sber.rdbms.api.AccountTransfer
import ru.sber.rdbms.api.Lock
import ru.sber.rdbms.factory.ConnectionFactory
import ru.sber.rdbms.factory.EasyConnectionFactory
import java.sql.Connection


@Testcontainers
internal class AccountTransferTest {

    @Container
    private val dataBaseContainer: TestPostgreSQLContainer =
        TestPostgreSQLContainer("postgres:14")
            .withDatabaseName(name)
            .withUsername(user)
            .withPassword(password)

    private lateinit var connectionFactory: ConnectionFactory

    @BeforeEach
    fun setUp() {
        connectionFactory = EasyConnectionFactory(dataBaseContainer.jdbcUrl, user, password)

        Liquibase(
            "db.changelog-master.yaml",
            ClassLoaderResourceAccessor(),
            DatabaseFactory.getInstance().findCorrectDatabaseImplementation(JdbcConnection(connectionFactory.openConnection()))
        ).use {  liquibase -> liquibase.update(Contexts()) }
    }

    @Test
    fun `transfer constraint test`() {
        // given
        val transfer: AccountTransfer = TransferConstraint(connectionFactory)

        // when
        transfer.transfer(1, 2, 1000)

        // then
        testTransfer(1000, 3000)
    }

    @Test
    fun `transfer constraint concurrency test`() {
        // given
        val transfer: AccountTransfer = TransferConstraint(connectionFactory)

        // when
        runBlocking(context = Dispatchers.Default) {
            repeat(1000) {
                launch { transfer.transfer(1, 2, 1) }
            }
        }

        // then
        testTransfer(1000, 3000)
    }


    @Test
    fun `batch transfer constraint test`() {
        // given
        val transfer: AccountTransfer = BatchTransferConstraint(connectionFactory)

        // when
        transfer.transfer(1, 2, 1000)

        // then
        testTransfer(1000, 3000)
    }

    @Test
    fun `batch transfer constraint concurrency test`() {
        // given
        val transfer: AccountTransfer = BatchTransferConstraint(connectionFactory)

        // when
        runBlocking(context = Dispatchers.Default) {
            repeat(1000) {
                launch { transfer.transfer(1, 2, 1) }
            }
        }

        // then
        testTransfer(1000, 3000)
    }

    @Test
    fun `optimistic transfer`() {
        // given
        val transfer: AccountTransfer = TransferWithLock(connectionFactory, lock = Lock.OPTIMISTIC)

        // when
        transfer.transfer(1, 2, 1000)

        // then
        testTransfer(1000, 3000)
    }

    @Test
    fun `optimistic transfer concurrency test`() {
        // given
        val transfer: AccountTransfer = TransferWithLock(connectionFactory, lock = Lock.OPTIMISTIC)

        // when
        runBlocking(context = Dispatchers.Default) {
            repeat(1000) {
                launch { transfer.transfer(1, 2, 1) }
            }
        }

        // then
        // версия аккаунта с которого списываем средства = версии аккаунта на который переводим средства
        connectionFactory.openConnection().use { connection ->
            val version1: Long = getAccountVersion(1, connection)
            val version2: Long = getAccountVersion(2, connection)
            assertEquals(version1, version2)
        }
    }

    @Test
    fun `pessimistic transfer`() {
        // given
        val transfer: AccountTransfer = TransferWithLock(connectionFactory, lock = Lock.PESSIMISTIC)

        // when
        transfer.transfer(1, 2, 1000)

        // then
        testTransfer(1000, 3000)
    }

    @Test
    fun `pessimistic transfer concurrency test`() {
        // given
        val transfer: AccountTransfer = TransferWithLock(connectionFactory, lock = Lock.PESSIMISTIC)

        // when
        runBlocking(context = Dispatchers.Default) {
            repeat(1000) {
                launch { transfer.transfer(1, 2, 1) }
            }
        }

        // then
        testTransfer(1000, 3000)
        // версия аккаунта с которого списываем средства = версии аккаунта на который переводим средства и все они равны 1000
        connectionFactory.openConnection().use { connection ->
            assertEquals(1000, getAccountVersion(1, connection))
            assertEquals(1000, getAccountVersion(2, connection))
        }
    }

    @Test
    fun `pessimistic transfer deadlock concurrency test`() {
        // given
        val transfer: AccountTransfer = TransferWithLock(connectionFactory, lock = Lock.PESSIMISTIC)

        // when
        runBlocking(context = Dispatchers.Default) {
            launch {
                repeat(500) {
                    launch { transfer.transfer(1, 2, 1) }
                }
            }
            launch {
                repeat(500) {
                    launch { transfer.transfer(2, 1, 1) }
                }
            }
        }

        // then
        testTransfer(2000, 2000)
        // версия аккаунта с которого списываем средства = версии аккаунта на который переводим средства и все они равны 1000
        connectionFactory.openConnection().use { connection ->
            assertEquals(1000, getAccountVersion(1, connection))
            assertEquals(1000, getAccountVersion(2, connection))
        }
    }


    // region evaluations

    private fun getAccountVersion(accountId: Long, connection: Connection): Long {
        connection.prepareStatement("select version from rdbms.account where id = 1").use { it.executeQuery().use {
            it.next()
            return it.getLong(1)
        } }
    }

    private fun testTransfer(expectedAmountOn1: Int, expectedAmountOn2: Int) {
        connectionFactory.openConnection().use { connection ->
            arrayOf(expectedAmountOn1, expectedAmountOn2).withIndex().forEach {
                connection.prepareStatement("select amount from rdbms.account where id = ${it.index + 1}")
                    .use { statement ->
                        statement.executeQuery().use { resultSet ->
                            assertTrue(resultSet.next())
                            assertEquals(it.value, resultSet.getInt(1))
                        }
                    }
            }
        }
    }

    // endregion

    class TestPostgreSQLContainer(imageName: String) : PostgreSQLContainer<TestPostgreSQLContainer>(imageName)

    companion object : KLogging() {
        const val name: String = "db"
        const val user: String = "postgres"
        const val password: String = "postgres"
    }
}