package com.github.ekgreen.retailer.endpoint

import com.github.ekgreen.retailer.api.distributor.Order
import com.github.ekgreen.retailer.api.distributor.OrderInfo
import com.github.ekgreen.retailer.service.OrderService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class RetailEndpoint {

    @Autowired
    lateinit var orderService: OrderService

    @PostMapping("/placeOrder")
    fun placeOrder(@RequestBody orderDraft: Order): OrderInfo = orderService.placeOrder(orderDraft)

    @GetMapping("/view/{id}")
    fun viewOrder(@PathVariable("id") id: String): OrderInfo? = orderService.getOrderInfo(id)

}

