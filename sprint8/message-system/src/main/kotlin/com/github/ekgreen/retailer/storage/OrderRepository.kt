package com.github.ekgreen.retailer.storage

import com.github.ekgreen.retailer.api.distributor.Order
import com.github.ekgreen.retailer.api.distributor.OrderInfo
import org.springframework.data.repository.CrudRepository
import java.util.*


/**
 * Интерфейс для организации хранилища заявок
 */
interface OrderRepository: CrudRepository<OrderInfo,String>