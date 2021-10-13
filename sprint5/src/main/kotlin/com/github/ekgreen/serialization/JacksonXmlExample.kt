package com.github.ekgreen.serialization

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue

data class JacksonXmlExample(
    val first: String,
    val second: String?,
    val items: List<String>,
)

fun main() {
    val xmlMapper = XmlMapper.builder()
        .build()
        .also {
            it.registerModule(KotlinModule())
            it.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            it.propertyNamingStrategy = PropertyNamingStrategies.UPPER_CAMEL_CASE
        }
    val initialObject = JacksonXmlExample("firstValue", "secondValue", listOf("item1", "item2"))
    val serializedData = xmlMapper.writeValueAsString(initialObject)
    println("Сериализованные данные: $serializedData")
    val deserializedObject = xmlMapper.readValue<JacksonXmlExample>(serializedData)
    println("Исходный и десериализованный объекты равны: ${initialObject == deserializedObject}")
}

