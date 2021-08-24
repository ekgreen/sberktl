package com.github.ekgreen.sberktl.sprint2

import com.github.ekgreen.sberktl.toolbox.readInt

/**
 * Формирование команд
 *
 * Дано: n - кол-во студентов (четное), ai умение i программиста
 * Условие: чтобы образовалась пара умения i-го и j-го программиста должны быть равны, каждая решенная задача
 * поднимает навык программиста на 1
 * Найти: минимальное кол-во решенных задач, чтобы могло образоваться n/2 пар
 *
 * @see <a href="https://codeforces.com/problemset/problem/1092/B?locale=ru">Формирование команд</a>
 */
class ProblemC {

    fun main() {
        val n = readInt()
        val a = readLine()!!.split(' ').map(String::toInt).sorted()

        // O(f) = O(n * logn) + O(n/2) = O(n * logn)
        var sum = 0
        for (i in 1 until n step 2)
            sum += a[i] - a[i-1]

        println(sum)
    }

}