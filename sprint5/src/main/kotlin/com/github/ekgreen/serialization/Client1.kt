package com.github.ekgreen.serialization

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

@JsonIgnoreProperties(ignoreUnknown = true)
data class Client1 @JsonCreator constructor(
    @JsonProperty("FIRSTNAME")      @JsonAlias("firstName")      val firstName: String,
    @JsonProperty("LASTNAME")       @JsonAlias("lastName")       val lastName: String,
    @JsonProperty("MIDDLENAME")     @JsonAlias("middleName")     val middleName: String,
    @JsonProperty("PASSPORTNUMBER") @JsonAlias("passportNumber") val passportNumber: String,
    @JsonProperty("PASSPORTSERIAL") @JsonAlias("passportSerial") val passportSerial: String,
    @JsonProperty("BIRTHDATE")      @JsonAlias("birthDate")      val birthDate: LocalDate
)
