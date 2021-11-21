package com.github.ekgreen.retailer.service

import com.github.ekgreen.retailer.adapter.DistributorPublisher
import com.github.ekgreen.retailer.api.distributor.Order
import com.github.ekgreen.retailer.api.distributor.OrderInfo
import com.github.ekgreen.retailer.api.distributor.OrderStatus
import com.github.ekgreen.retailer.storage.OrderStorage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.lang.IllegalStateException

/**
 * Сервис с бизнес-логикой обработки заказов
 */
@Service
class OrderService {

    @Autowired
    lateinit var distributorPublisher: DistributorPublisher

    @Autowired
    lateinit var orderStorage: OrderStorage

    /**
     * Размещение заказа
     * 1) Сохранение в БД и получение orderId
     * 2) Отправка заказа дистрибьютору
     * Возврат сохраненного заказа вместе с id
     */
    fun placeOrder(orderDraft: Order): OrderInfo {
        val data = orderStorage.createOrder(orderDraft)
        if (distributorPublisher.placeOrder(data.order)) {
            val info = data.info

            info.status = OrderStatus.SENT
            orderStorage.updateOrder(info)
        } else {
            throw IllegalStateException("Publishing failed")
        }

        return data.info
    }

    /**
     * Поиск заказа в БД
     */
    fun getOrderInfo(id: String): OrderInfo? = orderStorage.getOrderInfo(id)

    /**
     * Обновление заказа
     * Должно быть вызвано после получения каждого уведомления от дистрибьютора
     */
    fun updateOrderInfo(orderInfo: OrderInfo): Boolean {
        return orderStorage.updateOrder(orderInfo)
    }
}