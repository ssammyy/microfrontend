package org.kebs.app.kotlin.apollo.store.config

import org.hibernate.search.jpa.FullTextEntityManager
import org.hibernate.search.jpa.Search
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Configuration
class SearchManager(@PersistenceContext
                    private val entityManager: EntityManager) {

    @Bean(name = ["search-manager"])
    @Transactional
    fun fullTextEntityManager(): FullTextEntityManager {
        return Search.getFullTextEntityManager(entityManager)
    }

    @Bean
    @Primary
    fun entityManager(): EntityManager? {
        return entityManager
    }
}