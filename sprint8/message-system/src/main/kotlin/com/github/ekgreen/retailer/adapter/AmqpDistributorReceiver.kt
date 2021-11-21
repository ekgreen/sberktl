package com.github.ekgreen.retailer.adapter

import com.github.ekgreen.retailer.RetailerAmqpConfiguration.Companion.RETAILER_QUEUE
import com.github.ekgreen.retailer.api.distributor.OrderInfo
import com.github.ekgreen.retailer.storage.OrderStorage
import mu.KLogging
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service

@Service
class AmqpDistributorReceiver(private val storage: OrderStorage): DistributorReceiver {

    @RabbitListener(queues = [RETAILER_QUEUE])
    override fun handleDistributedOrder(orderInfo: OrderInfo) {
        logger.info { "заказ#${orderInfo.orderId} успешно обработан со статусом ${orderInfo.status}" }
        storage.updateOrder(orderInfo)
    }

    companion object: KLogging()
}