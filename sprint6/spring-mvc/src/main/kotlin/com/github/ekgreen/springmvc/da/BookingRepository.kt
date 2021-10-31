package com.github.ekgreen.springmvc.da

import com.github.ekgreen.springmvc.model.Contact
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.stream.Collectors.toList

class BookingRepository(private val book: MutableMap<String, Contact> = ConcurrentHashMap()) {

    /**
     * Сохранение/изменение сущности
     */
    fun save(contact: Contact): Contact {
        if(contact.id == null || !book.containsKey(contact.id)) {
            val uuid = UUID.randomUUID().toString()
            contact.id = uuid
        }

        val uuid = contact.id!!

        book[uuid] = contact
        return contact
    }

    /**
     * Некое подобие равно-взвешенного контекстного поиска
     */
    fun findByContext(context: String): List<Contact> {
        return book.values.stream()
            .filter {
                var sum = 0

                if(context.contains(it.firstName))
                    sum += 1

                if(context.contains(it.lastName))
                    sum += 1

                if(it.middleName != null && context.contains(it.middleName!!))
                    sum += 1

                return@filter sum > 0
            }
            .collect(toList())

    }

    /**
     * Поиск сущности по id
     */
    fun findById(id: String): Contact? {
        return book[id]
    }

    /**
     * Удаление сущности по id
     */
    fun delete(id: String): Contact? {
        if(book.containsKey(id)) return book.remove(id) else return null
    }
}