package com.github.ekgreen.retailer.adapter

import com.github.ekgreen.retailer.api.distributor.OrderInfo

/**
 * Интерфейс для отправки заказа дистрибьютору
 */
interface DistributorReceiver {

    /**
     * Метод для получения обработанного заказа
     *
     * @param message статус заказа
     */
    fun handleDistributedOrder(orderInfo: OrderInfo)
}