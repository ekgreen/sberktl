package com.github.ekgreen.retailer.api.distributor

/**
 * Возможные статусы заказа
 */
enum class OrderStatus {
    SENT,
    CREATED,
    DELIVERED,
    ERROR
}