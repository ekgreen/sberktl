package com.github.ekgreen.serialization

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class JsonSerializationTest {

    @Test
    fun `Json include only non-null fields - fix it by annotation`() {
        // given
        val client = Client5()
        val objectMapper = ObjectMapper()

        // when
        val data = objectMapper.writeValueAsString(client)

        // then
        assertEquals("{}", data)
    }

    @Test
    fun `Json include only non-null fields - fix it by ObjectMapper`() {
        // given
        val client = Client6()
        val objectMapper = ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)

        // when
        val data = objectMapper.writeValueAsString(client)

        // then
        assertEquals("{}", data)
    }
}
