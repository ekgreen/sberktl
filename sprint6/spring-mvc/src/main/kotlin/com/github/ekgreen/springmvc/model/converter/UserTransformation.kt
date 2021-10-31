package com.github.ekgreen.springmvc.model.converter

import com.github.ekgreen.springmvc.model.dto.UserDto
import com.github.ekgreen.springmvc.model.User
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface UserTransformation {

    fun transformToDto(user: User): UserDto

    fun transformToModel(userDto: UserDto): User
}