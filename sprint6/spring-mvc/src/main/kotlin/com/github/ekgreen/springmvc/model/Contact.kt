package com.github.ekgreen.springmvc.model

import java.time.LocalDate

data class Contact(
    var id          : String? = null,
    var nickname    : String,
    var firstName   : String,
    var lastName    : String,
    var middleName  : String? = null,
    var birthDate   : LocalDate? = null,
    var location    : String? = null,
    var email       : String? = null,
    var mobileNumber: String? = null
)
