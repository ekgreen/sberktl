package com.github.ekgreen.springmvc.auth

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "auth")
data class AuthConfiguration constructor(
    val security: Set<String>
)
