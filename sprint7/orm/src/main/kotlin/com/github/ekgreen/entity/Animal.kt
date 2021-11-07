package com.github.ekgreen.entity

import org.hibernate.Hibernate
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "animal", schema = "reserve")
open class Animal(
    @Id
    @Column(name = "id", nullable = false)
    open var id: UUID? = null,

    @Column(name = "type", nullable = false, length = 128)
    open var type: String? = null,

    @Column(name = "name", nullable = false, length = 128)
    open var name: String? = null,

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "chip_instance_id", nullable = false)
    open var chip: ChipInstance? = null,

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id")
    open var areal: Zone? = null
){
    override fun toString(): String {
        return this::class.simpleName + "(type = $type , name = $name)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Animal

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}