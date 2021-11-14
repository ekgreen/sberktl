package com.github.ekgreen.springmvc.book.model.da

import org.hibernate.Hibernate
import javax.persistence.*

@Entity
@Table(name = "users", schema = "auth")
open class User(
    @Id
    @Column(name = "username", nullable = false)
    open var login       : String? = null,

    @Column(name = "email", nullable = false)
    open var email       : String? = null,

    @Column(name = "password", nullable = false)
    open var password    : String? = null,

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    open var authorities: MutableList<Authority> = mutableListOf()
){
    override fun toString(): String {
        return this::class.simpleName + "(login = $login , email = $email , password = $password )"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as User

        return login != null && login == other.login
    }

    override fun hashCode(): Int = javaClass.hashCode()
}
