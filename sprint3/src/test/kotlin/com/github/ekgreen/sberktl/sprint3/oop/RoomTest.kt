package com.github.ekgreen.sberktl.sprint3.oop

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class RoomTest{


    @Test
    fun `newRoom default size`(){
        val room = Room("#1")

        Assertions.assertEquals("#1", room.name)
        Assertions.assertEquals(100, room.size)
    }

    @Test
    fun `newRoom new size 10`(){
        val room = Room("#1", 10)

        Assertions.assertEquals("#1", room.name)
        Assertions.assertEquals(10, room.size)
    }

    @Test
    fun createTownSquare(){
        val room = TownSquare()

        Assertions.assertEquals("Town Square", room.name)
        Assertions.assertEquals(1000, room.size)
        Assertions.assertEquals("There is only noise", room.load())
    }
}