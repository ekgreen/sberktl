package com.example.demo.persistance

import org.springframework.data.repository.CrudRepository


interface EntityRepository: CrudRepository<Entity, Long>{}