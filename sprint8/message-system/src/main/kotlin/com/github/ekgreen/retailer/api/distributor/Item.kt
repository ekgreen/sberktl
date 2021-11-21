package com.github.ekgreen.retailer.api.distributor

import java.io.Serializable

/**
 * Описание товара
 */
data class Item(
    /**
     * Произвольный идентификатор
     */
    val id: Long,

    /**
     * Произвольное название
     */
    val name: String
): Serializable