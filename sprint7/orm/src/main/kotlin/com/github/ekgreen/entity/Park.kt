package com.github.ekgreen.entity

import org.hibernate.Hibernate
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "park", schema = "reserve")
open class Park(
    @Id
    @Column(name = "id", nullable = false)
    open var id: UUID? = null,

    @Column(name = "name", nullable = false, length = 128)
    open var name: String? = null,

    @Column(name = "planet", nullable = false, length = 32)
    open var planet: String? = null,

    @Column(name = "galaxy", nullable = false, length = 32)
    open var galaxy: String? = null,

    @Column(name = "description", length = 512)
    open var description: String? = null
){
    override fun toString(): String {
        return this::class.simpleName + "(name = $name , planet = $planet , galaxy = $galaxy , description = $description )"
    }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Park

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}