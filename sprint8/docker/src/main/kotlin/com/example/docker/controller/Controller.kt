package com.example.docker.controller

import com.example.docker.configuration.GreetingProperties
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller(
    private val greetingProperties: GreetingProperties
) {
    @GetMapping("hello")
    fun hello() = greetingProperties.greeting
}