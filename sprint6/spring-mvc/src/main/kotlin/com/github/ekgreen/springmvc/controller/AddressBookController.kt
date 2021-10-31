package com.github.ekgreen.springmvc.controller

import com.github.ekgreen.springmvc.book.BookingService
import com.github.ekgreen.springmvc.model.converter.ContactTransformation
import com.github.ekgreen.springmvc.model.dto.ContactDto
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@Controller
@RequestMapping("/app/v1/book")
class AddressBookController(private val service: BookingService, private val mapper: ContactTransformation) {

    @RequestMapping(path = ["/main"], method = [ RequestMethod.GET, RequestMethod.POST ] )
    fun mainPage(): String {
        return "main"
    }

    @GetMapping("/add")
    fun addContact(): String {
        return "add"
    }

    @PostMapping("/add")
    fun addContact(@Valid @ModelAttribute contact: ContactDto): String {
        service.add(mapper.transformToModel(contact))

        return "main"
    }

    @GetMapping("/list")
    fun contextSearch(
        @RequestParam("search") context: String?,
        model: Model
    ): String {
        if(context != null) {
            val contacts: List<ContactDto> = service.list(context).map { mapper.transformToDto(it) }

            model.addAttribute("contacts", contacts)
        }

        return "main"
    }

    @GetMapping("/view/{id}")
    fun viewContact(@PathVariable("id") id: String?, model: Model): String {
        if (id == null || id.isEmpty())
            return "main"

        val contact = service.get(id) ?: return "main"

        model.addAttribute("contact", contact)

        return "view"
    }

    @GetMapping("/edit/{id}")
    fun editContact(@PathVariable("id") id: String?, model: Model): String {
        if (id == null || id.isEmpty())
            return "main"

        val contact = service.get(id) ?: return "main"

        model.addAttribute("contact", contact)

        return "edit"
    }

    @PostMapping("/edit/{id}")
    fun editContact(@PathVariable("id") id: String?, @ModelAttribute contact: ContactDto, model: Model): String {
        if (id == null || id.isEmpty())
            return "main"

        service.edit(id, mapper.transformToModel(contact)) ?: return "main"

        return "main"
    }

    @PostMapping("/delete/{id}")
    fun deleteContact(@PathVariable("id") id: String?, model: Model): String {
        if (id == null || id.isEmpty())
            return "main"

        val contact = service.delete(id)

        model.addAttribute("contact", contact)

        return "view"
    }
}