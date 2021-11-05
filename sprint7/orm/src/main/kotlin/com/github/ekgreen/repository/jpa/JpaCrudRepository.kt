package com.github.ekgreen.repository.jpa

import com.github.ekgreen.repository.Attribute
import com.github.ekgreen.repository.CrudRepository
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.Transaction
import org.hibernate.query.Query
import java.io.Serializable
import java.lang.Exception


class JpaCrudRepository<T, Id : Serializable>(
    private val sessionFactory: SessionFactory,
    private val entityType: Class<T>
): CrudRepository<T, Id> {

    override fun create(entity: T): Id {
        return sessionFactory.openSession().transactional { session ->
            return@transactional session.save(entity) as Id
        }
    }

    override fun update(entity: T) {
        sessionFactory.openSession().transactional { session ->
            session.update(entity)
        }
    }

    override fun findById(id: Id): T {
        return sessionFactory.openSession().use { session ->
            session.get(entityType, id) as T
        }
    }

    override fun findAllBy(attribute: Attribute, vararg attributes: Attribute): List<T> {
        sessionFactory.openSession().use { session ->
            val query =
                buildQlByAttributes(session, attribute, *attributes)

            return query.list() as List<T>
        }
    }

    override fun findBy(attribute: Attribute, vararg attributes: Attribute): T {
        sessionFactory.openSession().use { session ->
            val query =
                buildQlByAttributes(session, attribute, *attributes)

            return query.uniqueResult() as T
        }
    }

    private fun buildQlByAttributes(session: Session, attribute: Attribute, vararg attributes: Attribute): Query<T> {
        val qlString: String = "from ${entityType.simpleName} t where t.${attribute.name}=:${attribute.name}" +
                attributes.joinToString { attr -> " and t.${attr.name}=:${attr.name}" }

        val query = session.createQuery(qlString, entityType)

        query.setParameter(attribute.name, attribute.value)
        attributes.forEach { query.setParameter(it.name, it.value) }

        return query
    }

    override fun delete(entity: T) {
        return sessionFactory.openSession().transactional { session ->
            session.delete(entity)
        }
    }

    override fun deleteById(id: Id) {
        return sessionFactory.openSession().transactional { session ->
            val query = session.createQuery("delete ${entityType.simpleName} t where t.id=:id")
                .setParameter("id", id)

            query.executeUpdate()
        }
    }

    private fun <R> Session.transactional(block: (Session) -> R): R {
        this.use { session ->
            var transaction: Transaction? = null
            try {
                transaction = this.beginTransaction()
                val result = block(session)
                transaction.commit()
                return result
            } catch (exception: Exception) {
                transaction?.rollback()
                throw exception
            }
        }
    }
}

