package com.github.ekgreen.retailer

import org.springframework.amqp.core.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RetailerApplicationConfiguration {

    companion object {
        const val RETAILER_NAME: String = "ekgreen"
    }

}