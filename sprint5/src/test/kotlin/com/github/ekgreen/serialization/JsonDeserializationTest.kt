package com.github.ekgreen.serialization

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Test
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull

class JsonDeserializationTest {
    @Test
    fun `The names of the properties match`() {
        // given
        val data =
            """{"firstName": "Иван", "lastName": "Иванов", "middleName": "Иванович", "passportNumber": "123456", "passportSerial": "1234", "birthDate": "1990-01-01"}"""
        val objectMapper = ObjectMapper()
            .registerModule(JavaTimeModule())

        // when
        val client = objectMapper.readValue<Client1>(data)


        // then
        assertEquals("Иван", client.firstName)
        assertEquals("Иванов", client.lastName)
        assertEquals("Иванович", client.middleName)
        assertEquals("123456", client.passportNumber)
        assertEquals("1234", client.passportSerial)
        assertEquals(LocalDate.of(1990, 1, 1), client.birthDate)
    }

    @Test
    fun  `Json contains excess properties - fix it in ObjectMapper`() {
        // given
        val data =
            """{"city": "Москва", "firstName": "Иван", "lastName": "Иванов", "middleName": "Иванович", "passportNumber": "123456", "passportSerial": "1234", "birthDate": "1990-01-01"}"""
        val objectMapper = ObjectMapper()
            .registerModule(JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        // when
        val client = objectMapper.readValue<Client1>(data)

        // then
        assertEquals("Иван", client.firstName)
        assertEquals("Иванов", client.lastName)
        assertEquals("Иванович", client.middleName)
        assertEquals("123456", client.passportNumber)
        assertEquals("1234", client.passportSerial)
        assertEquals(LocalDate.of(1990, 1, 1), client.birthDate)
    }

    @Test
    fun `Json contains excess properties - fix it through annotation`() {
        // given
        val data =
            """{"city": "Москва", "firstName": "Иван", "lastName": "Иванов", "middleName": "Иванович", "passportNumber": "123456", "passportSerial": "1234", "birthDate": "1990-01-01"}"""
        val objectMapper = ObjectMapper()
            .registerModule(JavaTimeModule())

        // when
        val client = objectMapper.readValue<Client1>(data)

        // then
        assertEquals("Иван", client.firstName)
        assertEquals("Иванов", client.lastName)
        assertEquals("Иванович", client.middleName)
        assertEquals("123456", client.passportNumber)
        assertEquals("1234", client.passportSerial)
        assertEquals(LocalDate.of(1990, 1, 1), client.birthDate)
    }

    @Test
    fun `The names of the properties mismatch`() {
        // given
        val data =
            """{"name": "Иван", "lastName": "Иванов", "middleName": "Иванович", "passportNumber": "123456", "passportSerial": "1234", "birthDate": "1990-01-01"}"""
        val objectMapper = ObjectMapper()
            .registerModule(JavaTimeModule())

        // when
        val client = objectMapper.readValue<Client2>(data)

        // then
        assertEquals("Иван", client.firstName)
        assertEquals("Иванов", client.lastName)
        assertEquals("Иванович", client.middleName)
        assertEquals("123456", client.passportNumber)
        assertEquals("1234", client.passportSerial)
        assertEquals(LocalDate.of(1990, 1, 1), client.birthDate)
    }

    @Test
    fun `Custom date format`() {
        // given
        val data =
            """{"firstName": "Иван", "lastName": "Иванов", "middleName": "Иванович", "passportNumber": "123456", "passportSerial": "1234", "birthDate": "01-01-1990"}"""
        val objectMapper = ObjectMapper()
            .registerModule(JavaTimeModule())

        // when
        val client = objectMapper.readValue<Client3>(data)

        // then
        assertEquals("Иван", client.firstName)
        assertEquals("Иванов", client.lastName)
        assertEquals("Иванович", client.middleName)
        assertEquals("123456", client.passportNumber)
        assertEquals("1234", client.passportSerial)
        assertEquals(LocalDate.of(1990, 1, 1), client.birthDate)
    }

    @Test
    fun `Optional type support`() {
        // given
        val data1 =
            """{"firstName": "Иван", "lastName": "Иванов", "middleName": "Иванович", "passportNumber": "123456", "passportSerial": "1234", "birthDate": "1990-01-01"}"""
        val objectMapper = ObjectMapper()
            .registerModule(Jdk8Module())
            .registerModule(JavaTimeModule())

        // when
        val client1 = objectMapper.readValue<Client4>(data1)

        // then
        assertEquals("Иванович", client1.middleName.get())

        // given
        val data2 =
            """{"firstName": "Иван", "lastName": "Иванов", "passportNumber": "123456", "passportSerial": "1234", "birthDate": "1990-01-01"}"""

        // when
        val client2 = objectMapper.readValue<Client4>(data2)

        // then
        assertNotNull(client2.middleName)
        assertFalse(client2.middleName.isPresent)
    }
}
