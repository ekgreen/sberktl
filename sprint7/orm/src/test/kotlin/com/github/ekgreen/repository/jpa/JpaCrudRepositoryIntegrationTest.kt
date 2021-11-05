package com.github.ekgreen.repository.jpa

import com.github.ekgreen.entity.*
import com.github.ekgreen.repository.Attribute
import mu.KLogging
import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Testcontainers
internal class JpaCrudRepositoryIntegrationTest {

    @Container
    private val db: TestPostgreSQLContainer =
        TestPostgreSQLContainer("postgres:14")
            .withDatabaseName(name)
            .withUsername(username)
            .withPassword(password)
            .withInitScript("changeset/init.sql")

    private val animal = Animal(
        id = UUID.randomUUID(),
        type = "Стингбат",
        name = "Мотылек",
        chip = Chip(id = UUID.fromString("33171e98-b2a3-4623-9e0c-105863444cf5")),
        areal = Zone(id = UUID.fromString("dd1d13d7-e1cd-4969-a74c-4fee05ab9e07")),
    )

    private lateinit var sessionFactory: SessionFactory;

    @BeforeEach
    fun setUp() {
        sessionFactory = Configuration().configure()
            .addAnnotatedClass(Animal::class.java)
            .addAnnotatedClass(Chip::class.java)
            .addAnnotatedClass(Gamekeeper::class.java)
            .addAnnotatedClass(Park::class.java)
            .addAnnotatedClass(Zone::class.java)
            .setProperty("hibernate.connection.url", db.jdbcUrl)
            .setProperty("connection.url", db.jdbcUrl)
            .buildSessionFactory()
    }

    @Test
    fun `create animal`() {
        sessionFactory.use { factory ->
            // given
            val repository = JpaCrudRepository<Animal, UUID>(factory, Animal::class.java)

            // when
            val uuid: UUID = repository.create(animal)

            // then
            assertExists("animal", uuid)
        }
    }

    @Test
    fun `select by id animal`() {
        sessionFactory.use { factory ->
            // given
            val repository = JpaCrudRepository<Animal, UUID>(factory, Animal::class.java)

            // when
            val giraffe: Animal = repository.findById(UUID.fromString("d07c376d-75f4-4984-a1ed-866569c74017"))

            // then
            assertEquals("Жираф", giraffe.name)
            assertEquals("Диплодок", giraffe.type)
            assertExists("animal", giraffe.id)
        }
    }

    @Test
    fun `select by attribute animal`() {
        sessionFactory.use { factory ->
            // given
            val repository = JpaCrudRepository<Animal, UUID>(factory, Animal::class.java)

            // when
            val giraffe: Animal = repository.findBy(Attribute("type", "Диплодок"))

            // then
            assertEquals("Жираф", giraffe.name)
            assertEquals("Диплодок", giraffe.type)
            assertExists("animal", giraffe.id)
        }
    }


    @Test
    fun `select all by attribute animal`() {
        sessionFactory.use { factory ->
            // given
            val repository = JpaCrudRepository<Animal, UUID>(factory, Animal::class.java)

            // when
            val unicorns = repository.findAllBy(Attribute("type", "Единорог"))

            // then
            assertEquals(3, unicorns.size)
            unicorns.forEach { assertExists("animal", it.id) }
        }
    }

    @Test
    fun `update animal attribute`() {
        sessionFactory.use { factory ->
            // given
            val repository = JpaCrudRepository<Animal, UUID>(factory, Animal::class.java)

            // when
            createAnimalByJdbc(animal)

            animal.name = "Супер Мотылек"
            repository.update(animal)

            // then
            assertAttribute("animal", "name", "Супер Мотылек", animal.id)
        }
    }

    @Test
    fun `delete animal`() {
        sessionFactory.use { factory ->
            // given
            val repository = JpaCrudRepository<Animal, UUID>(factory, Animal::class.java)

            // when
            createAnimalByJdbc(animal)
            repository.delete(animal)

            // then
            assertExists("animal", animal.id, expected = false)
        }
    }

    @Test
    fun `delete animal by id`() {
        sessionFactory.use { factory ->
            // given
            val repository = JpaCrudRepository<Animal, UUID>(factory, Animal::class.java)

            // when
            createAnimalByJdbc(animal)
            repository.deleteById(animal.id!!)

            // then
            assertExists("animal", animal.id, expected = false)
        }
    }

    class TestPostgreSQLContainer(imageName: String) : PostgreSQLContainer<TestPostgreSQLContainer>(imageName)

    private fun createAnimalByJdbc(animal: Animal) {
        session {
            val statement = it.prepareStatement(
                "insert into reserve.animal (id, type, name, chip_id, zone_id)\n" +
                        "    values ('${animal.id}', '${animal.type}', '${animal.name}', '${animal.chip!!.id}', '${animal.areal!!.id}');")

            assert(statement.executeUpdate() == 1)
        }
    }

    private fun assertExists(table: String, id: Any?, expected: Boolean = true) {
        assertTrue { id != null }

        val exists = session { connection ->
            connection.createStatement().use {
                it.executeQuery("select count(id) from reserve.${table} where id = '$id'").use { result ->
                    result.next()
                    result.getInt(1) == 1
                }
            }
        }

        assertEquals(expected, exists)
    }
    private fun assertAttribute(table: String, name: String, expected: String,  id: Any?) {
        assertEquals(expected, session { connection ->
            connection.createStatement().use {
                it.executeQuery("select $name from reserve.${table} where id = '$id'").use { result ->
                    result.next()
                    result.getString(1)
                }
            }
        })
    }


    private fun <R> session(block: (Connection) -> R): R {
        val connection: Connection = DriverManager.getConnection(db.jdbcUrl, username, password)
        connection.autoCommit = false

        try {
            val result: R = block(connection)
            connection.commit()
            return result
        } catch (exception: SQLException) {
            connection.rollback()
            throw exception
        }
    }

    companion object : KLogging() {
        const val name: String = "db"
        const val username: String = "postgres"
        const val password: String = "postgres"
    }
}