package com.github.ekgreen.functional

class StudentsGroup {

    lateinit var students: List<Student>

    init {
        students = listOf(
            Student(firstName = "Иван", lastName = "Иванов",  averageRate = 4.0),
            Student(firstName = "Петр", lastName = "Петров",  averageRate = 4.5),
            Student(firstName = "Альбус", lastName = "Дамблдор",  averageRate = 100.0, city = "Хогвартс", prevEducation = "Грифиндор"),
        )
    }

    fun filterByPredicate(predicate: (student: Student) -> Boolean): List<Student> {
        return ArrayList(students.filter(predicate))
    }
}