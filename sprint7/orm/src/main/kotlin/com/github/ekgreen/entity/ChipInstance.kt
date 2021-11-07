package com.github.ekgreen.entity

import org.hibernate.Hibernate
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "chip_instance", schema = "reserve")
open class ChipInstance(
    @Id
    @Column(name = "id", nullable = false)
    open var id: UUID? = null,

    @CreationTimestamp
    @Column(name = "start_exploitation_date", nullable = false)
    open var startExploitationDate: LocalDateTime? = null,

    @Column(name = "is_active", nullable = false)
    open var isActive: Boolean? = null,

    @ManyToOne(optional = false)
    @JoinColumn(name = "chip_id")
    open var chip: Chip? = null
) {
    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , startExploitationDate = $startExploitationDate )"
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as ChipInstance

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}