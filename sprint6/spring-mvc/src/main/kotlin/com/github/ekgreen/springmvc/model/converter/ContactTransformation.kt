package com.github.ekgreen.springmvc.model.converter

import com.github.ekgreen.springmvc.model.Contact
import com.github.ekgreen.springmvc.model.dto.UserDto
import com.github.ekgreen.springmvc.model.User
import com.github.ekgreen.springmvc.model.dto.ContactDto
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface ContactTransformation {

    fun transformToDto(contact: Contact): ContactDto

    fun transformToModel(contactDto: ContactDto): Contact

    fun transformToDto(contacts: List<Contact>): List<ContactDto> {
        return contacts.map { transformToDto(it) }
    }

    fun transformToModel(contactsDto: List<ContactDto>): List<Contact> {
        return contactsDto.map { transformToModel(it) }
    }
}