package com.github.ekgreen.sberktl.sprint3.oop

data class User(val name: String, val age: Long) {
    lateinit var city: String

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (name != other.name) return false
        if (age != other.age) return false
        if (city != other.city) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + age.hashCode()
        result = 31 * result + city.hashCode()
        return result
    }

    // и где-то тут toString
}

fun main() {
    val user1 = User("Alex", 13)
    // Создайте user2, изменив имя и используя функцию copy()
    val user2 = user1.copy(name = "Wall-E")
    println(user2)
    // Измените город user1 на 'Omsk'
    user1.city = "Omsk"
    // Создайте копию user1 - user3, только с городом 'Tomsk'.
    val user3 = user1.copy()
    user3.city = "Tomsk"
    // Сравните user1 и user3, используя функцию equals()
    println(user1.equals(user3))
}