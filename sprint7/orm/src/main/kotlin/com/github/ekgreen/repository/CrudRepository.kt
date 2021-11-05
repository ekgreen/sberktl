package com.github.ekgreen.repository

import java.io.Serializable

interface CrudRepository<T, Id : Serializable> {

    fun create(entity: T): Id

    fun update(entity: T)

    fun findById(id: Id): T

    fun findAllBy(attribute: Attribute, vararg attributes: Attribute): List<T>

    fun findBy(attribute: Attribute, vararg attributes: Attribute): T

    fun delete(entity: T)

    fun deleteById(id: Id)
}