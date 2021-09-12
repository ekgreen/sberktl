package com.github.ekgreen.sberktl.sprint2

import com.github.ekgreen.sberktl.toolbox.readInt


/**
 * Прыгающая лягушка
 *
 * Дано: ось координат, лягушка
 * Действия:
 *  odd - x = x + a (на сколько лягушка прыгает вправо)
 *  even - x = x - b (на сколько лягушка прыгает влево)
 * Найти: позиция лягушки после k прыжков
 *
 * @see <a href="https://codeforces.com/problemset/problem/1077/A?locale=ru">Прыгающая лягушка</a>
 */
class ProblemA {

    fun main() {
        // кол-во лягушек
        val t = readInt()
        (1..t).forEach { frog ->
            val (a, b, k) = readLine()!!.split(' ').map(String::toLong)
            println(jumpingFrogOxPosition(a, b, k))
        }
    }

    private fun jumpingFrogOxPosition(a: Long, b: Long, k: Long): Long{
        val evenCount = k / 2
        return (k - evenCount) * a - evenCount * b
    }
}