package ru.sberschool.hystrix.example

class FallbackSlowlyApi : SlowlyApi {
    override fun getSomething() = SimpleResponse("predefined data")
}


