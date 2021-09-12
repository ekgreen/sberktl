package com.github.ekgreen.sberktl.sprint2

import com.github.ekgreen.sberktl.toolbox.readInt
import java.util.function.IntFunction
import java.util.stream.IntStream

/**
 *  Равномерная строка
 *
 * Дано: от i до k латинских букв от a до z
 * Условие: максимизировать минимальную частоту
 * Найти: строка длины n
 *
 * @see <a href="https://codeforces.com/problemset/problem/1092/A?locale=ru"> Равномерная строка</a>
 */
class ProblemB {
    // начальная позиция (одна до 'a')
    val position: Int = 97

    fun main() {
        val t = readInt()
        (1..t).forEach { i ->
            val (n, k) = readLine()!!.split(' ').map(String::toInt)

            var s = "";
            for (l in 1..n)
                s += (position + (l % k)).toChar()
            println(s)
        }
    }
}

