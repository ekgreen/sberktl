package com.github.ekgreen.entity

import org.hibernate.Hibernate
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "gamekeeper", schema = "reserve")
open class Gamekeeper(
    @Id
    @Column(name = "id", nullable = false)
    open var id: UUID? = null,

    @Column(name = "name", nullable = false, length = 128)
    open var name: String? = null,

    @Enumerated(value = EnumType.STRING)
    @Column(name = "friendly", nullable = false, length = 32)
    open var friendly: FriendlyType? = null,
){
    override fun toString(): String {
        return this::class.simpleName + "(name = $name , friendly = $friendly )"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Gamekeeper

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}