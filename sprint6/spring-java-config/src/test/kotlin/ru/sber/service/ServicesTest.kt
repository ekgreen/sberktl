package ru.sber.service

import org.junit.jupiter.api.Test
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import ru.sber.config.AnotherServicesConfig
import ru.sber.config.ServicesConfig
import kotlin.test.assertEquals

class ServicesTest {

    @Test
    fun `getBean should return firstService successfully`() {
        // given
        val context = AnnotationConfigApplicationContext(ServicesConfig::class.java)

        // when
        val firstService = context.getBean("firstService")

        // then
        assertEquals("I am firstService", firstService.toString())
    }

    @Test
    fun `getBean should return secondService successfully`() {
        // given
        val context = AnnotationConfigApplicationContext(ServicesConfig::class.java)

        // when
        val secondService = context.getBean("secondService")

        // then
        assertEquals("I am secondService", secondService.toString())
    }

    @Test
    fun `getBean should return thirdService successfully`() {
        // given
        val context = AnnotationConfigApplicationContext(ServicesConfig::class.java)

        // when
        val thirdService = context.getBean("thirdService")

        // then
        assertEquals("I am thirdService", thirdService.toString())
    }

    @Test
    fun `getBean should return fourthService successfully`() {
        // given
        val context = AnnotationConfigApplicationContext(AnotherServicesConfig::class.java)

        // when
        val fourthService = context.getBean("fourthService")

        // then
        assertEquals("I am fourthService", fourthService.toString())
    }
}