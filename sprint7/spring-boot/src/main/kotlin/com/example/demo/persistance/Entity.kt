package com.example.demo.persistance

import org.hibernate.Hibernate
import javax.persistence.*
import javax.persistence.Entity

@Entity
@Table(name = "account", schema = "rdbms")
class Entity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    var id: Long? = null,

    @Column(name = "amount", nullable = false)
    var amount: Long? = null,

    @Version
    @Column(name = "version", nullable = false)
    var version: Long? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as com.example.demo.persistance.Entity

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}