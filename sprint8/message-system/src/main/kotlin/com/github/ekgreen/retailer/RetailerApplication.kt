package com.github.ekgreen.retailer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RetailerApplication

fun main(args: Array<String>) {
	runApplication<RetailerApplication>(*args)
}
