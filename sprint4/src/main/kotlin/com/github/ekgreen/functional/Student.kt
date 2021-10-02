package com.github.ekgreen.functional

data class Student(
    val firstName: String,
    val lastName: String,
    val middleName: String? = "",
    val age: Int? = -1,
    val averageRate: Double,
    val city: String = "Москва",
    val specialization: String = "Специализация отсутствует",
    val prevEducation: String? = "",
)
