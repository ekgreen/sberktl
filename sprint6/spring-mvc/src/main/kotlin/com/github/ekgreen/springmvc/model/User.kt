package com.github.ekgreen.springmvc.model

data class User(
    val login       : String,
    val firstName   : String?,
    val lastName    : String?,
    val email       : String,
    var password    : String
)
