package com.github.ekgreen.retailer

import com.github.ekgreen.retailer.RetailerApplicationConfiguration.Companion.RETAILER_NAME
import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class RetailerAmqpConfiguration {

    companion object{
        const val DISTRIBUTOR_EXCHANGE  : String = "distributor_exchange"
        const val RETAILER_QUEUE        : String = "retailer_queue"
    }

    @Bean
    fun distributorExchanger(): Exchange {
        return TopicExchange(DISTRIBUTOR_EXCHANGE, true, false)
    }

    @Bean
    fun retailerQueue(): Queue {
        return Queue(RETAILER_QUEUE, true, true, false)
    }

    @Bean
    fun retailerQueueBinding(): Binding {
        return BindingBuilder
            .bind(retailerQueue())
            .to(distributorExchanger())
            .with("retailer.${RETAILER_NAME}.#")
            .noargs()
    }

    @Bean
    fun jsonMessageConverter(): MessageConverter{
        return Jackson2JsonMessageConverter()
    }

    @Bean
    fun rabbitListenerContainerFactory(connectionFactory: ConnectionFactory): SimpleRabbitListenerContainerFactory {
        val factory = SimpleRabbitListenerContainerFactory()
        factory.setConnectionFactory(connectionFactory)
        factory.setMessageConverter(jsonMessageConverter())
        return factory
    }
}