package com.github.ekgreen.springmvc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringMvcApplication

fun main(args: Array<String>) {
    runApplication<SpringMvcApplication>(*args)
}
