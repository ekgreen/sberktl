package com.github.ekgreen.sberktl.sprint2

import com.github.ekgreen.sberktl.toolbox.readInt
import kotlin.math.max

/**
 * Гамбургеры
 *
 * Дано: n - кол-во клиентов, ai - кол-во монет у i-го клиента
 * Найти: наибольшую прибыть кафе
 *
 * @see <a href="https://codeforces.com/contest/1431/problem/A">Гамбургеры</a>
 */
class ProblemD {

    fun main() {
        val t = readInt()
        // O(f) = t * O(n * logn) + O(n) = C * O(n * logn)
        (1..t).forEach { l ->
            val n = readInt()
            val a = readLine()!!.split(' ').map(String::toLong).sorted()

            var max = -1L
            for (i in a.indices)
                max = max(max, a[i] * (a.size - i))
            println(max)
        }
    }

}