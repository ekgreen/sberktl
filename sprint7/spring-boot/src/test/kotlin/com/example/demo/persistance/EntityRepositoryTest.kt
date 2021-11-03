package com.example.demo.persistance

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import kotlin.test.assertEquals

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
internal class EntityRepositoryTest {

    @Autowired
    private lateinit var entityRepository: EntityRepository

    @Test
    fun `findById should find entity`(){
        // given
        val savedEntity: Entity = entityRepository.save(Entity(amount = 3000))

        // when
        val foundEntity: Entity = entityRepository.findById(savedEntity.id!!).orElse(null)

        //then
        assertEquals(savedEntity, foundEntity)
    }
}