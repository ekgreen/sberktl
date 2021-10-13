package com.github.ekgreen.serialization

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue

data class JacksonExample(
    val first: String,
    val second: String?,
)

fun main() {
    val objectMapper = ObjectMapper()
        .registerModule(KotlinModule())
        .enable(SerializationFeature.INDENT_OUTPUT)
    val initialObject = JacksonExample("firstValue", "secondValue")
    val serializedData = objectMapper.writeValueAsString(initialObject)
    println("Сериализованные данные: $serializedData")
    val deserializedObject = objectMapper.readValue<JacksonExample>(serializedData)
    println("Исходный и десериализованный объекты равны: ${initialObject == deserializedObject}")
}

