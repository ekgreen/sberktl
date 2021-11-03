package com.example.demo.controller

import com.example.demo.mapper.EntityMapper
import com.example.demo.persistance.Entity
import com.example.demo.persistance.EntityRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/entity")
class Controller(private val repository: EntityRepository,private val mapper: EntityMapper) {

    @GetMapping("/get/{id}")
    fun getEntity(@PathVariable("id") id: Long): ResponseEntity<EntityDto> {
         val entity: Entity = repository.findById(id).orElseThrow{ IllegalArgumentException("entity ($id) not found")  }

        return ResponseEntity(mapper.transformToDto(entity), HttpStatus.OK)
    }

    @ExceptionHandler(value = [IllegalArgumentException::class])
    fun handleException(): ResponseEntity<EntityDto> {
        return ResponseEntity(HttpStatus.NOT_FOUND)
    }
}