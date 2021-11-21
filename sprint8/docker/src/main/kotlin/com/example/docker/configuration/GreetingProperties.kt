package com.example.docker.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("props")
class GreetingProperties {
    lateinit var greeting: String
}