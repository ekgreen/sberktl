package com.github.ekgreen.sberktl.sprint3.oop

import kotlin.random.Random

interface Fightable {
    val powerType: String
    var healthPoints: Int
    val damageRoll: Int
        get() = Random.nextInt()

    fun attack(opponent: Fightable): Int
}

/**
 * Реализуйте класс Player, имплементирующий интерфейс ru.sber.oop.Fightable с дополнительным полем name (строка)
 * и isBlessed. attack уменьшает здоровье оппоненту на damageRoll, если isBlessed = false, и удвоенный damageRoll,
 * если isBlessed = true.
 *
 * Результат функции attack - количество урона, которое нанес объект класса Player.
 */
class Player(
    val name: String,
    val isBlessed: Boolean,
    override val powerType: String,
    override var healthPoints: Int
) : Fightable {

    override fun attack(opponent: Fightable): Int {
        val damage: Int = (if (isBlessed) 1 else 2) * damageRoll
        opponent.healthPoints = Math.min(0, opponent.healthPoints - damage)
        return damage
    }
}

/**
 * Реализуйте абстрактный класс Monster, имплементирующий интерфейс ru.sber.oop.Fightable со строковыми
 * полями name и description. Логика функции attack, такая же, как и в предыдущем пункте,
 * только без учета флага isBlessed (которого у нас нет).
 */
abstract class Monster(open val name: String,open val description: String) : Fightable {
    override fun attack(opponent: Fightable): Int {
        val damage: Int = damageRoll
        opponent.healthPoints = Math.min(0, opponent.healthPoints - damage)
        return damage
    }
}

/**
 * Реализуйте наследника класса Monster - класс Goblin. Переопределите в нем метод
 * чтения damageRoll (допустим, он в два раза меньше сгененрированного рандомного значения).
 */
class Goblin(
    override val name: String,
    override val description: String = "Иногда зеленый",
    override val powerType: String = "Иногда с ножом",
    override var healthPoints: Int = 10
) : Monster(name, description) {

    override val damageRoll: Int
        get() = super.damageRoll / 2
}

/**
 * Добавьте функцию-расширение к классу Monster, getSalutation() - которое выдает приветствтие монстра
 * и вызовите ее в функции load() класса Room.
 */
fun Monster.getSalutation() : String{
    return "Привет путник! Я страшный и ужасный $name"
}