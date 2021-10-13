package com.github.ekgreen.serialization

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class Client2 @JsonCreator constructor(
    @JsonProperty("name")           val firstName: String,
    @JsonProperty("lastName")       val lastName: String,
    @JsonProperty("middleName")     val middleName: String?,
    @JsonProperty("passportNumber") val passportNumber: String,
    @JsonProperty("passportSerial") val passportSerial: String,
    @JsonProperty("birthDate")      val birthDate: LocalDate
)
