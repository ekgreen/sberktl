package com.github.ekgreen.serialization

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.addDeserializer
import com.fasterxml.jackson.module.kotlin.readValue
import io.mockk.InternalPlatformDsl.toStr
import org.junit.jupiter.api.Test
import java.util.regex.Pattern
import kotlin.test.assertEquals

class JsonCustomClassDeserializer {

    @Test
    fun `Data deserialization`() {
        // given
        val data = """{"client": "Иванов Иван Иванович"}"""
        val objectMapper = ObjectMapper()
            .registerModule(clientCustomDeserializer())

        // when
        val client = objectMapper.readValue<Client7>(data)

        // then
        assertEquals("Иван", client.firstName)
        assertEquals("Иванов", client.lastName)
        assertEquals("Иванович", client.middleName)
    }

    private fun clientCustomDeserializer(): Module{
        val module: SimpleModule = SimpleModule()
        module.addDeserializer(Client7::class, object : JsonDeserializer<Client7>() {

            val DELIMITER: String = " "
            val pattern: Pattern = Pattern.compile("^[а-яА-Я]+\\s[а-яА-Я]+(\\s[а-яА-Я]+)?$")

            override fun deserialize(jp: JsonParser, context: DeserializationContext): Client7 {
                val node: JsonNode = jp.codec.readTree(jp)

                val fio = node.get("client").asText()

                if(!pattern.matcher(fio).matches())
                    throw JsonParseException(jp, "content not matches pattern = $pattern")

                val dividedFio = fio.split(DELIMITER)

                return Client7(dividedFio[1],dividedFio[0], middleName = if(dividedFio.size == 3) dividedFio[2] else null)
            }
        })

        return module
    }

}
