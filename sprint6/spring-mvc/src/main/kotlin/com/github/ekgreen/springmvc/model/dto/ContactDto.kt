package com.github.ekgreen.springmvc.model.dto

import com.fasterxml.jackson.annotation.JsonCreator
import java.time.LocalDate
import javax.validation.constraints.*

data class ContactDto @JsonCreator constructor(
    @get:Null
    val id          : String?,
    @get:NotBlank(message = "Nickname can not be null or empty")
    val nickname    : String?,
    @get:NotBlank(message = "First name can not be null or empty")
    val firstName   : String?,
    @get:NotBlank(message = "Last name can not be null or empty")
    val lastName    : String?,
    val middleName  : String?,
    val birthDate   : LocalDate?,
    @get:NotBlank(message = "Location can not be null or empty")
    val location    : String?,
    val email       : String?,
    val mobileNumber: String?
){
    fun fullName(): String{
        return "$lastName $firstName ${middleName?:""}"
    }
}
