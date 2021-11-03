package com.example.demo.mapper

import com.example.demo.controller.EntityDto
import com.example.demo.persistance.Entity
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface EntityMapper {
    fun transformToDto(entity: Entity): EntityDto
}
