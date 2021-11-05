package ru.sber.rdbms

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import liquibase.Contexts
import liquibase.Liquibase
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import mu.KLogging
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import ru.sber.rdbms.api.AccountTransfer
import ru.sber.rdbms.api.Lock
import ru.sber.rdbms.factory.ConnectionFactory
import ru.sber.rdbms.factory.EasyConnectionFactory
import java.sql.ResultSet
import java.util.stream.Stream


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
            DatabaseFactory.getInstance()
                .findCorrectDatabaseImplementation(JdbcConnection(connectionFactory.openConnection()))
        ).use { liquibase -> liquibase.update(Contexts()) }
    }

    @ParameterizedTest
    @MethodSource("accountTransferMethod")
    fun `transfer constraint test`(factoryType: String, block: (ConnectionFactory) -> AccountTransfer) {
        // given
        val accountTransfer: AccountTransfer = block(connectionFactory)

        // when
        accountTransfer.transfer(1, 2, 1000)

        // then
        testTransfer(1, 2)
    }

    @ParameterizedTest
    @MethodSource("accountTransferMethod")
    fun `transfer concurrency test`(factoryType: String, block: (ConnectionFactory) -> AccountTransfer) {
        // given
        val accountTransfer: AccountTransfer = block(connectionFactory)

        // when
        runBlocking(context = Dispatchers.Default) {
            repeat(1000) {
                launch { accountTransfer.transfer(1, 2, 1) }
            }
        }

        // then
        testTransfer(1, 2)
    }

    @ParameterizedTest
    @MethodSource("accountTransferMethod")
    fun `cross-transfer (deadlock) concurrency test`(factoryType: String, block: (ConnectionFactory) -> AccountTransfer) {
        // given
        val accountTransfer: AccountTransfer = block(connectionFactory)

        // when
        runBlocking(context = Dispatchers.Default) {
            launch {
                repeat(500) {
                    launch { accountTransfer.transfer(1, 2, 1) }
                }
            }
            launch {
                repeat(500) {
                    launch { accountTransfer.transfer(2, 1, 1) }
                }
            }
        }

        // then
        testTransfer(1, 2)
    }

    // region evaluations

    private fun testTransfer(accountId1: Int, accountId2: Int) {
        connectionFactory.openConnection().use { connection ->
            val decrementResultSet: ResultSet = connection.prepareStatement("select amount, version from rdbms.account where id = $accountId1")
                .executeQuery()
            val incrementResultSet: ResultSet = connection.prepareStatement("select amount, version from rdbms.account where id = $accountId2")
                .executeQuery()

            decrementResultSet.use { decrementAccount -> incrementResultSet.use { incrementAccount ->
                decrementAccount.next()
                incrementAccount.next()

                // определяем, были-ли потеряны денежные средства
                assertEquals(4000, decrementAccount.getLong(1) + incrementAccount.getLong(1))
                // определяем, были-ли параллельные изменения версии (по сути первое исключает второе, но ради интереса)
                assertEquals(decrementAccount.getLong(2) , incrementAccount.getLong(2))
            } }
        }
    }

    // endregion

    class TestPostgreSQLContainer(imageName: String) : PostgreSQLContainer<TestPostgreSQLContainer>(imageName)

    companion object : KLogging() {
        const val name: String = "db"
        const val user: String = "postgres"
        const val password: String = "postgres"

        @JvmStatic
        fun accountTransferMethod(): Stream<out Arguments> {
            return Stream.of(
                Arguments.of(
                    "Optimistic lock", { connectionFactory: ConnectionFactory -> TransferWithLock(connectionFactory, lock = Lock.OPTIMISTIC) }
                ),
                Arguments.of(
                    "Pessimistic lock", { connectionFactory: ConnectionFactory -> TransferWithLock(connectionFactory, lock = Lock.PESSIMISTIC) }
                ),
                Arguments.of(
                    "No lock", { connectionFactory: ConnectionFactory -> TransferConstraint(connectionFactory) }
                ),
                Arguments.of(
                    "No lock (batch mode)", { connectionFactory: ConnectionFactory -> TransferBatchConstraint(connectionFactory) }
                )
            )
        }
    }
}