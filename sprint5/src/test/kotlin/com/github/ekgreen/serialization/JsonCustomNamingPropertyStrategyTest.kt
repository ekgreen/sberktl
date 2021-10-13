package com.github.ekgreen.serialization

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JSR310Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.JSR310DateTimeDeserializerBase
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Test
import java.text.SimpleDateFormat
import java.time.LocalDate
import kotlin.test.assertEquals

class JsonCustomNamingPropertyStrategyTest {

    @Test
    fun `Custom deserialization strategy`() {
        // given
        val data =
            """{"FIRSTNAME": "Иван", "LASTNAME": "Иванов", "MIDDLENAME": "Иванович", "PASSPORTNUMBER": "123456", "PASSPORTSERIAL": "1234", "BIRTHDATE": "1990-01-01"}"""
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
}
