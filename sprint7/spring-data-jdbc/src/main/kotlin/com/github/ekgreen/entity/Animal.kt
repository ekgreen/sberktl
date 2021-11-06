package com.github.ekgreen.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table("reserve\".\"animal")
open class Animal(

    @Id
    @Column("id")
    open var id: UUID? = null,

    @Column("type")
    open var type: String? = null,

    @Column("name")
    open var name: String? = null,

    @Column("chip_id")
    open var chip: UUID? = null,

    @Column("zone_id")
    open var areal: UUID? = null
){
    override fun toString(): String {
        return "Animal(id=$id, type=$type, name=$name, chip=$chip, areal=$areal)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Animal

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}