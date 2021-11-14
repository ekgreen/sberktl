package com.github.ekgreen.springmvc.book.model.converter

import com.github.ekgreen.springmvc.book.model.da.User
import com.github.ekgreen.springmvc.book.model.dto.UserDto
import org.mapstruct.Mapper
import org.springframework.security.core.authority.SimpleGrantedAuthority

@Mapper(componentModel = "spring")
interface UserTransformation {

    fun transformToDto(user: User): UserDto

    fun transformToModel(userDto: UserDto): User

}

fun transformToAuthentication(user: User): org.springframework.security.core.userdetails.User{
    return org.springframework.security.core.userdetails.User(
        user.login,
        user.password,
        user.authorities.map { SimpleGrantedAuthority(it.name) }
    )
}