package com.github.ekgreen

import com.github.ekgreen.entity.*
import com.github.ekgreen.repository.Attribute
import com.github.ekgreen.repository.jpa.JpaCrudRepository
import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration
import java.util.*

fun main() {
    val sessionFactory: SessionFactory = Configuration().configure()
        .addAnnotatedClass(Animal::class.java)
        .addAnnotatedClass(Chip::class.java)
        .addAnnotatedClass(ChipInstance::class.java)
        .addAnnotatedClass(Gamekeeper::class.java)
        .addAnnotatedClass(Park::class.java)
        .addAnnotatedClass(Zone::class.java)
        .buildSessionFactory()

    sessionFactory.use { factory ->
        // Получим тип чипа, который подходит чип для Земли и Пандоры
        // index: brand & model
        val chipRepository = JpaCrudRepository<Chip, UUID>(factory, Chip::class.java)
        val eva: Chip = chipRepository.findBy(Attribute("brand", "wall&eva"), Attribute("model", "we-213951"))

        // Создадим новый чип
        val chipInstanceRepository = JpaCrudRepository<ChipInstance, UUID>(factory, ChipInstance::class.java)
        val newEvaChip = ChipInstance(UUID.randomUUID(), chip = eva)
        newEvaChip.id = chipInstanceRepository.create(newEvaChip)

        // Получим зону на Пандоре по id (например мы его откуда-то знаем)
        val zoneRepository = JpaCrudRepository<Zone, UUID>(factory, Zone::class.java)
        val waterfall = zoneRepository.findById(UUID.fromString("dd1d13d7-e1cd-4969-a74c-4fee05ab9e07"))

        // Добавим нового животного в зону водопада на пандоре, и не забудем ему добавить чип
        val animalRepository = JpaCrudRepository<Animal, UUID>(factory, Animal::class.java)

        val moth: Animal = Animal(
            id = UUID.randomUUID(),
            type = "Стингбат",
            name = "Мотылек",
            chip = newEvaChip,
            areal = waterfall,
        )

        // создадим Стингбата
        val uuid: UUID = animalRepository.create(moth)

        // Заменим на более подходящий чип
        // index: brand & model
        val intergalactic: Chip =
            chipRepository.findBy(Attribute("brand", "intergalactic"), Attribute("model", "inc-21e5"))
        val newIntergalacticChip = ChipInstance(UUID.randomUUID(), chip = intergalactic)
        newIntergalacticChip.id = chipInstanceRepository.create(newIntergalacticChip)

        moth.chip = newIntergalacticChip
        animalRepository.update(moth)

        // Выпустим из заповедника Стингабата (но оставим в сердечке)
        animalRepository.deleteById(uuid)

        println(moth)
    }
}