package com.github.ekgreen.retailer.storage

import com.github.ekgreen.retailer.api.distributor.Order
import com.github.ekgreen.retailer.api.distributor.OrderInfo

/**
 * Контейнер для сущности заказа и сущности информации о заказе
 */
data class PlaceOrderData(val order: Order, val info: OrderInfo)