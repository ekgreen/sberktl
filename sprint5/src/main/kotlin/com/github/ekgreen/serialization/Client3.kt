package com.github.ekgreen.serialization

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class Client3 @JsonCreator constructor(
    @JsonProperty("firstName")      val firstName: String,
    @JsonProperty("lastName")       val lastName: String,
    @JsonProperty("middleName")     val middleName: String?,
    @JsonProperty("passportNumber") val passportNumber: String,
    @JsonProperty("passportSerial") val passportSerial: String,
    @JsonProperty("birthDate") @JsonFormat(pattern = "dd-MM-yyyy") val birthDate: LocalDate
)
