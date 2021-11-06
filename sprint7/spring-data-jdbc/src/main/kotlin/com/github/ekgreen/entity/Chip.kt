package com.github.ekgreen.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table("reserve\".\"chip")
open class Chip(

    @Id
    @Column("id")
    open var id: UUID? = null,

    @Column("brand")
    open var brand: String? = null,

    @Column("model")
    open var model: String? = null,

    @Column("description")
    open var description: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Chip

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}