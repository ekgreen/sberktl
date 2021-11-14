package com.github.ekgreen.springmvc.book.model.dto

import com.fasterxml.jackson.annotation.JsonCreator

data class UserDto @JsonCreator constructor(
    val login       : String?,
    val email       : String?,
    val password    : String?
)
