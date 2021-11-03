package com.example.demo.controller

import com.example.demo.persistance.Entity
import com.example.demo.persistance.EntityRepository
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class ControllerTest {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var testRestTemplate: TestRestTemplate

    @Autowired
    private lateinit var entityRepository: EntityRepository

    private lateinit var entity: Entity;

    @BeforeEach
    fun setUp() {
        entity = entityRepository.save(Entity(amount = 3000))
    }

    @AfterEach
    fun tearDown(){
        entityRepository.delete(entity)
    }

    @Test
    fun getEntity() {
        val actual = testRestTemplate.exchange(
            "http://localhost:${port}/api/v1/entity/get/${entity.id}",
            HttpMethod.GET,
            null,
            EntityDto::class.java
        )

        // then
        assertEquals(HttpStatus.OK, actual.statusCode)
        assertEquals(entity.id , actual.body!!.id)
    }

    @Test
    fun handleException() {
        val actual = testRestTemplate.exchange(
            "http://localhost:${port}/api/v1/entity/get/-1",
            HttpMethod.GET,
            null,
            EntityDto::class.java
        )

        // then
        assertEquals(HttpStatus.NOT_FOUND, actual.statusCode)
    }
}