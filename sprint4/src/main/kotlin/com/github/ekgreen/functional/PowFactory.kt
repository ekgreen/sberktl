package com.github.ekgreen.functional

object PowFactory {
    /**
     * Возвращает функцию, которая всегда возводит аргумент в нужную степень, указанную при создании функции.
     */
    fun buildPowFunction(pow: Int): (base: Double) -> Double {
        return {base: Double -> Math.pow(base, pow.toDouble())}
    }
}
