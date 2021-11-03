package ru.sber.rdbms.api

data class ReleaseResult<D,I>(val decrement: D, val increment: I){
    fun <T> use(block: (D,I) -> T): T {
        return block(decrement,increment)
    }
}
