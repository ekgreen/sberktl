package com.github.ekgreen.retailer.storage

import com.github.ekgreen.retailer.api.distributor.Order
import com.github.ekgreen.retailer.api.distributor.OrderInfo
import com.github.ekgreen.retailer.api.distributor.OrderStatus
import org.springframework.stereotype.Repository
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.security.MessageDigest
import java.util.*
import javax.xml.bind.DatatypeConverter


@Repository
class OrderStorageImpl(private val delegate: OrderRepository) : OrderStorage {

    override fun createOrder(draftOrder: Order): PlaceOrderData {
        val orderInfo: OrderInfo = delegate.save(convertOrderToInfo(order = draftOrder))

        draftOrder.id = orderInfo.orderId // надо бы создать новый объект, но мне лень +_+

        return PlaceOrderData(draftOrder, orderInfo)
    }

    override fun updateOrder(order: OrderInfo): Boolean {
        delegate.save(order)
        return true
    }

    override fun getOrderInfo(id: String): OrderInfo? {
        return delegate.findById(id).orElse(null)
    }

    private fun convertOrderToInfo(order: Order): OrderInfo {
        return OrderInfo(UUID.randomUUID().toString(), OrderStatus.CREATED)
    }

}