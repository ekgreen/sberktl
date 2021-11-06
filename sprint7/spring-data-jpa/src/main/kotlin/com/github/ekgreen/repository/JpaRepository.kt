package com.github.ekgreen.repository

import com.github.ekgreen.entity.*
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface JpaAnimalRepository: JpaRepository<Animal, UUID>
interface JpaChipRepository: JpaRepository<Chip, UUID>{
    fun findChipByBrandAndModel(brand: String, model: String): Chip
}
interface JpaGamekeeperRepository: JpaRepository<Gamekeeper, UUID>
interface JpaParkRepository: JpaRepository<Park, UUID>
interface JpaZoneRepository: JpaRepository<Zone, UUID>
