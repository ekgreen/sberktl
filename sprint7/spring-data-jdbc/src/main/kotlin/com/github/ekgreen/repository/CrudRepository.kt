package com.github.ekgreen.repository

import com.github.ekgreen.entity.*
import org.springframework.data.repository.CrudRepository
import java.util.*

interface CrudAnimalRepository: CrudRepository<Animal, UUID>
interface CrudChipRepository: CrudRepository<Chip, UUID>{
    fun findChipByBrandAndModel(brand: String, model: String): Chip
}
interface CrudZoneRepository: CrudRepository<Zone, UUID>
