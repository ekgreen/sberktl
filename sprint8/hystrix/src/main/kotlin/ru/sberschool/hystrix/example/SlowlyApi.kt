package ru.sberschool.hystrix.example

import feign.RequestLine

interface SlowlyApi {
    @RequestLine("GET /")
    fun getSomething(): SimpleResponse
}


