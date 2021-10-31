package com.github.ekgreen.springmvc.book

import com.github.ekgreen.springmvc.da.BookingRepository
import com.github.ekgreen.springmvc.model.Contact

class BookingService(private val repository: BookingRepository) {

    fun add(contact: Contact): Contact{
        // тут где-то должна быть бизнес логика
        // PS я умышлено не отделял модель для DA слоя, потому что все в мапке храним
        return repository.save(contact) // вернется с айдишником
    }

    fun list(context: String): List<Contact>{
        // тут где-то должна быть бизнес логика
        return repository.findByContext(context);
    }

    fun get(id: String): Contact?{
        // тут где-то должна быть бизнес логика
        return repository.findById(id)
    }

    fun edit(id: String, contact: Contact): Contact?{
        // тут где-то должна быть бизнес логика
        val editable = get(id) ?: return null

        // что-то страшное
        if(contact.location != null && contact.location!!.isNotEmpty())
            editable.location = contact.location
        if(contact.birthDate != null)
            editable.birthDate = contact.birthDate
        if(contact.email != null && contact.email!!.isNotEmpty())
            editable.email = contact.email
        if(contact.mobileNumber != null && contact.mobileNumber!!.isNotEmpty())
            editable.mobileNumber = contact.mobileNumber

        return repository.save(editable)
    }

    fun delete(id: String): Contact? {
        // тут где-то должна быть бизнес логика
        return repository.delete(id)
    }
}