package ru.sberschool.hystrix.pokemonapi

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class Pokemon @JsonCreator constructor(
    // уникальный идентификатор покемона
    @JsonProperty("id")
    val id: Long,
    // имя покемона, например slowpoke
    @JsonProperty("name")
    val name: String,
    // базовый опыт
    @JsonProperty("base_experience")
    val baseExperience: Long,
    // высота
    @JsonProperty("height")
    val height: Long,
    // вес
    @JsonProperty("weight")
    val weight: Long,
    // Order for sorting. Almost national order, except families are grouped together.
    @JsonProperty("order")
    val order: Long,
    // способности
    @JsonProperty("abilities")
    val abilities: List<Ability>? = ArrayList(),
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class Ability @JsonCreator constructor(
    @JsonProperty("is_hidden") val isHidden: Boolean,
    @JsonProperty("slot")      val slot: Long,
    @JsonProperty("ability")   val ability: Species
)

data class Species @JsonCreator constructor(
    @JsonProperty("name") val name: String,
    @JsonProperty("url")  val url: String
)

