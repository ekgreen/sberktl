package com.github.ekgreen.sberktl.sprint3.oop

open class Room(val name: String, val size: Int, val monster: Monster = Goblin("Мистер Гоблин")) {

    constructor(name: String) : this(name, 100)

    protected open val dangerLevel = 5

    fun description() = "Room: $name with size $size"

    open fun load() = monster.getSalutation()
}

// Создайте подкласс класса ru.sber.oop.Room - TownSquare c именем "Town Square" и размером 1000.
// Переопределите в новом классе функцию load() (придумайте строку для загрузки)

class TownSquare : Room("Town Square", 1000){

    //Переопределите dangerLevel в TownSquare, так чтобы сделать уровень угрозы на 3 пункта меньше среднего.
    // В классе ru.sber.oop.Room предоставить доступ к этой переменной только для наследников.
    // PS не очень понял, откуда взять среднее ( наверное речь про родительский класс )
    override val dangerLevel = super.dangerLevel - 3

    final override fun load() = "There is only noise"
}

fun main() {
    println(Room("#1", 10).description())
    println(Room("#2").description())
    println(Room("#3").load())
    println(TownSquare().load())
}