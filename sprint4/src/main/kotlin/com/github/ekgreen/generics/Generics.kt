package com.github.ekgreen.generics

import java.util.*
import kotlin.collections.ArrayList

//import com.sun.org.apache.xpath.internal.operations.Bool
//import java.util.*

/**
 * 1. Реализовать обобщенную функцию compare,
 * которая сравнивала бы два объекта класса Pair p1 и p2 и возвращала значение Boolean.
 */
fun <A,B> compare(p1: Pair<A,B>, p2: Pair<A,B>): Boolean {
    return p1 == p2
}

/**
 * 2. Реализовать обобщенную функцию, чтобы найти количество элементов в общем массиве,
 * которое больше, чем определенный элемент. int countGreaterThan принимает на вход массив и элемент,
 * с которым нужно сравнить все остальные элементы массива.
 */
fun <T : Comparable<T>> countGreaterThan(array: Array<T>, elem: T): Int {
    return array.count { it > elem }
}

/**
 * 3. Реализовать обобщенный класс Sorter с параметром Т и подходящим ограничением,
 * который имеет свойство list:MutableList и функцию fun add(value:T).
 * С каждым вызовом функции передаваемое значение должно добавляться в список
 * и список должен оставаться в отсортированном виде.
 */
class Sorter<T : Comparable<T>>(private val list: MutableList<T> = ArrayList()) {

    fun add(value: T) {
        list.add(value)
        // в целом, можно делать вставку в массив или еще что-то, но сильно зависит от задачи и данных, так что написал максимально просто
        list.sort()
    }
}

// 4. Написать обобщенный стек. Минимально - реализовать функции вставки, извлечения и проверки на пустоту
class Stack<T>(private val stack: Deque<T> = LinkedList()) {

    public fun push(value: T){
        stack.push(value)
    }

    public fun pop(): T{
        return stack.removeFirst()
    }

    public fun isEmpty(): Boolean{
        return stack.isEmpty()
    }
}