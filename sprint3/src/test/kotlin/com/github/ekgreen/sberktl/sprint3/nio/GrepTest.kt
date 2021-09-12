package com.github.ekgreen.sberktl.sprint3.nio

import org.junit.jupiter.api.Test
import ru.sber.nio.Grep

internal class GrepTest{

    @Test
    fun `find() check method`(){
        Grep.find(" 200 ")
    }
}