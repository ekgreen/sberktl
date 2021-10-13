package com.github.ekgreen.serialization

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate
import java.util.Optional

data class Client4 @JsonCreator constructor(
    @JsonProperty("firstName")      val firstName: String,
    @JsonProperty("lastName")       val lastName: String,
    @JsonProperty("middleName")     val middleName: Optional<String>,
    @JsonProperty("passportNumber") val passportNumber: String,
    @JsonProperty("passportSerial") val passportSerial: String,
    @JsonProperty("birthDate")      val birthDate: LocalDate
)
