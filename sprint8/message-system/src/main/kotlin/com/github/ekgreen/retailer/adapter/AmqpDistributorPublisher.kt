package com.github.ekgreen.retailer.adapter

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.ekgreen.retailer.RetailerAmqpConfiguration.Companion.DISTRIBUTOR_EXCHANGE
import com.github.ekgreen.retailer.RetailerApplicationConfiguration.Companion.RETAILER_NAME
import com.github.ekgreen.retailer.api.distributor.Order
import com.github.ekgreen.retailer.storage.OrderStorage
import mu.KLogging
import org.springframework.amqp.core.MessageProperties
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
class AmqpDistributorPublisher constructor(private val template: RabbitTemplate) : DistributorPublisher {

    /**
     * Метод для отправки заказа
     * должен попасть в "distributor_exchange" с ключом маршрутизации "distributor.placeOrder.githubName.orderId"
     * Для получения уведомления, заполняем заголовки:
     * Notify-Exchange = distributor_exchange
     * Notify-RoutingKey = ваш ключ маршрутизации по шаблону "retail.#github_username#"
     *
     * После некоторого времени уведомления будут поступать в distributor_exchange с ключом retail.#github_username#.#orderId#
     */
    override fun placeOrder(order: Order): Boolean {
        try {
            template.convertAndSend(DISTRIBUTOR_EXCHANGE, "distributor.placeOrder.$RETAILER_NAME.${order.id}", order) { message ->
                val headers = message.messageProperties.headers

                headers["Notify-Exchange"]   = "distributor_exchange"
                headers["Notify-RoutingKey"] = "retailer.$RETAILER_NAME"

                message.messageProperties.contentType     = MessageProperties.CONTENT_TYPE_JSON
                message.messageProperties.contentEncoding = "UTF-8"

                return@convertAndSend message
            }
            return true
        }catch (any: Exception){
            logger.warn(any) { "не удалось отправить заказ#${order.id} в $DISTRIBUTOR_EXCHANGE" }
            return false
        }
    }

    companion object: KLogging()
}