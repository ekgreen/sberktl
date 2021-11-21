package com.github.ekgreen.retailer.api.distributor

import org.springframework.messaging.handler.annotation.Payload
import javax.persistence.*

/**
 * Уведомление об изменении заказа
 */
@Entity
@Table(name = "orders", schema = "shop")
open class OrderInfo(

    /**
     * Уникальный идентификатор заказа
     *
     * @see com.example.retailer.api.distributor.Item#id
     */
    @Id
    @Column(name = "order_id", nullable = false, unique = true)
    open var orderId: String? = null,

    /**
     * Статус заказа:
     *  Created
     *
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    open var status: OrderStatus? = null,

    /**
     * Контрольная сумма
     */
    @Column(name = "signature", nullable = false)
    open var signature: String? = "",
)