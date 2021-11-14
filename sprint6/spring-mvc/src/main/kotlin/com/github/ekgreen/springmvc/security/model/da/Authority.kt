package com.github.ekgreen.springmvc.book.model.da

import org.hibernate.Hibernate
import javax.persistence.*

@Entity
@Table(name = "authorities", schema = "auth")
open class Authority(
    @Id
    @Column(name = "id", nullable = false)
    open var id: Long? = null,

    @Column(name = "authority", nullable = false)
    open val name       : String? = null,

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "username", nullable = false)
    open var user: User? = null
){
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , name = $name )"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Authority

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}
