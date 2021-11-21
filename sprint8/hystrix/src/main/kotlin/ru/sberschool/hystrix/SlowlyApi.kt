package ru.sberschool.hystrix

import feign.RequestLine

interface SlowlyApi {
    @RequestLine("GET /")
    fun getSomething(): SimpleResponse
}


