package com.github.ekgreen.springmvc.controller

import com.github.ekgreen.springmvc.book.BookingContextSearch
import com.github.ekgreen.springmvc.book.BookingService
import com.github.ekgreen.springmvc.model.converter.ContactTransformation
import com.github.ekgreen.springmvc.model.dto.ContactDto
import org.mapstruct.factory.Mappers
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.annotation.PostConstruct
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/book")
class AddressBookRestController(private val service: BookingService, private val mapper: ContactTransformation) {

    @PostMapping("/add")
    fun addContact(@Valid @RequestBody contact: ContactDto): ResponseEntity<ContactDto> {
        val model = service.add(mapper.transformToModel(contact))

        return ResponseEntity(mapper.transformToDto(model), HttpStatus.CREATED)
    }

    @GetMapping("/list")
    fun contextSearch(@RequestParam("search") context: String?
    ): ResponseEntity<List<ContactDto>> {
        if(context == null)
            return ResponseEntity(HttpStatus.NO_CONTENT)

        val list = service.list(context)

        if(list.isEmpty())
            return ResponseEntity(HttpStatus.NO_CONTENT)

        return ResponseEntity(mapper.transformToDto(list), HttpStatus.OK)
    }

    @GetMapping("/view/{id}")
    fun viewContact(@PathVariable("id") id: String): ResponseEntity<ContactDto>{
        val model = service.get(id)
            ?: return ResponseEntity(HttpStatus.NO_CONTENT)

        return ResponseEntity(mapper.transformToDto(model), HttpStatus.OK)
    }

    @PatchMapping("/edit/{id}")
    fun editContact(@PathVariable("id") id: String, @RequestBody contact: ContactDto): ResponseEntity<ContactDto>{
        val model = service.edit(id, mapper.transformToModel(contact))
            ?: return ResponseEntity(HttpStatus.NO_CONTENT)

        return ResponseEntity(mapper.transformToDto(model), HttpStatus.OK)
    }

    @DeleteMapping("/delete/{id}")
    fun deleteContact(@PathVariable("id") id: String): ResponseEntity<ContactDto>{
        val model = service.delete(id)
            ?: return ResponseEntity(HttpStatus.NO_CONTENT)

        return ResponseEntity(mapper.transformToDto(model), HttpStatus.OK)
    }
}