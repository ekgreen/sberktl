package com.github.ekgreen.entity

import org.hibernate.Hibernate
import org.hibernate.annotations.GenericGenerator
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "zone", schema = "reserve")
open class Zone(
    @Id
    @Column(name = "id", nullable = false)
    open var id: UUID? = null,

    @Column(name = "code", nullable = false)
    open var code: Long? = null,

    @Column(name = "name", nullable = false, length = 128)
    open var name: String? = null,

    @Column(name = "description", length = 512)
    open var description: String? = null,

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "park_id", nullable = false)
    open var park: Park? = null,

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "gamekeeper_id")
    open var gamekeeper: Gamekeeper? = null
){

    override fun toString(): String {
        return this::class.simpleName + "(code = $code , name = $name , description = $description )"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Zone

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}