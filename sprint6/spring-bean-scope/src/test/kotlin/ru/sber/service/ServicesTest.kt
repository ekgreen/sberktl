package ru.sber.service

import org.junit.jupiter.api.Test
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import ru.sber.config.ServicesConfig
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ServicesTest {

    @Test
    fun `assertEquals should return true if service bean scope is singleton`() {
        // given
        val context = AnnotationConfigApplicationContext(ServicesConfig::class.java)

        // when
        val service = context.getBean("singletonService")
        val theSameService = context.getBean("singletonService")

        // then
        assertEquals(service, theSameService)
    }

    @Test
    fun `assertNotEquals should return true if service bean scope is prototype`() {
        // given
        val context = AnnotationConfigApplicationContext(ServicesConfig::class.java)

        // when
        val service = context.getBean("prototypeService")
        val anotherService = context.getBean("prototypeService")

        // then
        assertNotEquals(service, anotherService)
    }
}