package com.github.ekgreen.serialization

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import java.time.LocalDate
import java.util.Optional

data class UserJdk8(
    val firstName: String,
    val lastName: Optional<String>,
    @JsonFormat(pattern = "dd-MM-yyyy")
    val birthDate: LocalDate,
)

fun main() {
    val objectMapper = ObjectMapper()
        .registerModules(KotlinModule(), JavaTimeModule(), Jdk8Module())
        .enable(SerializationFeature.INDENT_OUTPUT)
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    val initialObject = UserJdk8("Василий", Optional.of("Васильев"), LocalDate.of(1990, 1, 1))
    val serializedData = objectMapper.writeValueAsString(initialObject)
    println("Сериализованные данные:")
    println(serializedData)
    val deserializedObject = objectMapper.readValue<UserJdk8>(serializedData)
    println("Исходный и десериализованный объекты равны: ${initialObject == deserializedObject}")
}
