package com.github.ekgreen.serialization

import java.time.LocalDate

data class Client6(
    val firstName: String? = null,
    val lastName: String? = null,
    val middleName: String? = null,
    val passportNumber: String? = null,
    val passportSerial: String? = null,
    val birthDate: LocalDate? = null
)
