package ru.sber.service

import org.junit.jupiter.api.Test
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import ru.sber.config.ServicesConfig
import ru.sber.services.ConditionalBeanInjectionService
import ru.sber.services.ConstructorInjectionService
import ru.sber.services.FieldInjectionService
import ru.sber.services.PrimaryBeanInjectionService
import ru.sber.services.QualifierBeanInjectionService
import ru.sber.services.SetterInjectionService
import ru.sber.services.SeveralBeanInjectionService
import kotlin.test.assertEquals

class ServicesTest {

    @Test
    fun `getBean should return constructorInjectionService successfully`() {
        // given
        val context = AnnotationConfigApplicationContext(ServicesConfig::class.java)

        // when
        val constructorInjectionService = context.getBean("constructorInjectionService") as ConstructorInjectionService

        // then
        assertEquals("Service was injected into ConstructorInjectionService", constructorInjectionService.toString())
    }

    @Test
    fun `getBean should return fieldInjectionService successfully`() {
        // given
        val context = AnnotationConfigApplicationContext(ServicesConfig::class.java)

        // when
        val fieldInjectionService = context.getBean("fieldInjectionService") as FieldInjectionService

        // then
        assertEquals("Service was injected into FieldInjectionService", fieldInjectionService.toString())
    }

    @Test
    fun `getBean should return setterInjectionService successfully`() {
        // given
        val context = AnnotationConfigApplicationContext(ServicesConfig::class.java)

        // when
        val setterInjectionService = context.getBean("setterInjectionService") as SetterInjectionService

        // then
        assertEquals("Service was injected into SetterInjectionService", setterInjectionService.toString())
    }

    @Test
    fun `severalBeanInjectionService should have two dependencies`() {
        // given
        val context = AnnotationConfigApplicationContext(ServicesConfig::class.java)

        // when
        val severalBeanInjectionService = context.getBean("severalBeanInjectionService") as SeveralBeanInjectionService

        // then
        assertEquals(
            "SeveralBeanInjectionService(services=[FirstServiceImpl, SecondServiceImpl])",
            severalBeanInjectionService.toString()
        )
    }

    @Test
    fun `primaryBeanInjectionService should return SecondPrimaryServiceImpl dependency`() {
        // given
        val context = AnnotationConfigApplicationContext(ServicesConfig::class.java)

        // when
        val primaryBeanInjectionService = context.getBean("primaryBeanInjectionService") as PrimaryBeanInjectionService

        // then
        assertEquals(
            "PrimaryBeanInjectionService(primaryService=SecondPrimaryServiceImpl)",
            primaryBeanInjectionService.toString()
        )
    }

    @Test
    fun `qualifierBeanInjectionService should return only SecondQualifierServiceImpl dependency`() {
        // given
        val context = AnnotationConfigApplicationContext(ServicesConfig::class.java)

        // when
        val qualifierBeanInjectionService =
            context.getBean("qualifierBeanInjectionService") as QualifierBeanInjectionService

        // then
        assertEquals(
            "QualifierBeanInjectionService(qualifierServices=[SecondQualifierServiceImpl])",
            qualifierBeanInjectionService.toString()
        )
    }

    @Test
    fun `conditionalBeanInjectionService should return ConditionalService dependency`() {
        // given
        val context = AnnotationConfigApplicationContext().apply {
            environment.setActiveProfiles("prod")
            register(ServicesConfig::class.java)
            refresh()
        }

        // when
        val conditionalBeanInjectionService =
            context.getBean("conditionalBeanInjectionService") as ConditionalBeanInjectionService

        // then
        assertEquals(
            "ConditionalBeanInjectionService(conditionalService=ConditionalService)",
            conditionalBeanInjectionService.toString()
        )
    }
}