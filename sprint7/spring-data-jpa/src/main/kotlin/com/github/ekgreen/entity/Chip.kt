package com.github.ekgreen.entity

import org.hibernate.Hibernate
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "chip", schema = "reserve")
open class Chip(
    @Id
    @Column(name = "id", nullable = false)
    open var id: UUID? = null,

    @Column(name = "brand", nullable = false, length = 32)
    open var brand: String? = null,

    @Column(name = "model", nullable = false, length = 32)
    open var model: String? = null,

    @Column(name = "description", length = 512)
    open var description: String? = null
) {
    override fun toString(): String {
        return this::class.simpleName + "(brand = $brand , model = $model , description = $description )"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Chip

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}