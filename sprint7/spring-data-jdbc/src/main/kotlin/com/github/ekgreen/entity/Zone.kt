package com.github.ekgreen.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table("reserve\".\"zone")
open class Zone(

    @Id
    @Column("id")
    open var id: UUID? = null,

    @Column("code")
    open var code: Long? = null,

    @Column("name")
    open var name: String? = null,

    @Column("description")
    open var description: String? = null,

    @Column("park_id")
    open var park: UUID? = null,

    @Column("gamekeeper_id")
    open var gamekeeper: UUID? = null
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Zone

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}