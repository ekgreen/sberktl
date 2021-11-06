package com.github.ekgreen

import com.github.ekgreen.entity.Animal
import com.github.ekgreen.entity.Chip
import com.github.ekgreen.entity.Zone
import com.github.ekgreen.repository.JpaAnimalRepository
import com.github.ekgreen.repository.JpaChipRepository
import com.github.ekgreen.repository.JpaZoneRepository
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ConfigurableApplicationContext
import java.util.*

@SpringBootApplication
class SpringDataApplication

fun main(args: Array<String>) {
    val context: ConfigurableApplicationContext = runApplication<SpringDataApplication>(*args)

    // Получим, который подходит чип для Земли и Пандоры
    // index: brand & model
    val chipRepository = context.getBean(JpaChipRepository::class.java)
    val eva: Chip = chipRepository.findChipByBrandAndModel( "wall&eva", "we-213951")

    // Получим зону на Пандоре по id (например мы его откуда-то знаем)
    val zoneRepository = context.getBean(JpaZoneRepository::class.java)
    val waterfall = zoneRepository.findById(UUID.fromString("dd1d13d7-e1cd-4969-a74c-4fee05ab9e07")).orElse(null)

    // Добавим нового животного в зону водопада на пандоре, и не забудем ему добавить чип
    val animalRepository = context.getBean(JpaAnimalRepository::class.java)

    // создадим Стингбата
    val moth: Animal =  animalRepository.save(Animal(
        id = UUID.randomUUID(),
        type = "Стингбат",
        name = "Мотылек",
        chip = eva,
        areal = waterfall,
    ))

    // Заменим на более подходящий чип
    // index: brand & model
    val intergalactic: Chip =
        chipRepository.findChipByBrandAndModel("intergalactic", "inc-21e5")
    moth.chip = intergalactic
    animalRepository.save(moth)

    // Выпустим из заповедника Стингабата (но оставим в сердечке)
    animalRepository.deleteById(moth.id!!)

    println(moth)
}
