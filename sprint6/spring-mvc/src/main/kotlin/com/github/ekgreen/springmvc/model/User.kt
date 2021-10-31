package com.github.ekgreen.springmvc.model

data class User(
    val login       : String,
    val email       : String,
    var password    : String
)
