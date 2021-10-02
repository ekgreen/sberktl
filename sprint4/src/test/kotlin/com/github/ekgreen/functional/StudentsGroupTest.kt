package com.github.ekgreen.functional

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class StudentsGroupTest {

    @Test
    fun `filterByPredicate should filter student by predicate=from Moscow`() {
        val filteredByCityStudents: List<Student> = StudentsGroup().filterByPredicate { it.city == "Москва" }

        assertEquals(2, filteredByCityStudents.size)
    }
}
