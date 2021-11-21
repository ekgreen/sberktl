package com.github.ekgreen.retailer.api.distributor

import java.io.Serializable

/**
 * Описание заказа
 */
data class Order(
    /**
     * Уникальный идентификатор заказа на стороне ретейлера
     */
    var id: String?,

    /**
     * Произвольный адрес доставки
     */
    val address: String,

    /**
     * Произвольный получатель доставки
     */
    val recipient: String,

    /**
     * Список заказанных товаров
     */
    val items: List<Item>
): Serializable