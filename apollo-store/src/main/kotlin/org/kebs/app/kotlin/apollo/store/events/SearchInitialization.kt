package org.kebs.app.kotlin.apollo.store.events

import mu.KotlinLogging
import org.hibernate.search.jpa.FullTextEntityManager
import org.hibernate.search.jpa.FullTextQuery
import org.hibernate.search.jpa.Search
import org.hibernate.search.query.dsl.QueryBuilder
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.di.ConsignmentDocumentDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.di.ConsignmentDocumentTypesEntity
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils
import javax.persistence.EntityManager

/**
 * Search initialization
 *
 * @author jmungai
 */
@Service
class SearchInitialization(
        var entityManager: EntityManager,
        val fullTextEntityManager: FullTextEntityManager
) {
    @Transactional
    @EventListener(ContextRefreshedEvent::class)
    fun onApplicationEvent(event: ContextRefreshedEvent) {
        try {
            fullTextEntityManager.createIndexer().startAndWait()
        } catch (e: InterruptedException) {
            KotlinLogging.logger { }.error("Error occurred trying to build Hibernate Search indexes ", e)
        }
    }

    fun searchConsignmentDocuments(keywords: String?, category: String?, cdType: ConsignmentDocumentTypesEntity?, inspectionOfficer: Boolean, loggedInUser: UsersEntity?,page: Int=20): List<ConsignmentDocumentDetailsEntity> {
//        val loggedInUser = commonDaoServices.getLoggedInUser()
        val searchManager = Search.getFullTextEntityManager(entityManager)
        val builder: QueryBuilder = searchManager.getSearchFactory()
                .buildQueryBuilder()
                .forEntity(ConsignmentDocumentDetailsEntity::class.java)
                .get()
        val query = builder.bool()
        if (StringUtils.hasLength(keywords)) {
            query.should(builder.keyword().fuzzy()
                    .withEditDistanceUpTo(2)
                    .onFields("ucrNumber","cdRefNumber", "cocNumber", "idfNumber", "varField10", "description")
                    .matching(keywords).createQuery())
        }
        // Remove Old CD from result
        query.must(builder.keyword().onField("oldCdStatus").matching(-1).createQuery())
        // Filter by Assigner
        if (StringUtils.hasLength(category)) {
            when (category) {
                "my-tasks" -> {
                    try {
                        if (inspectionOfficer) {
                            query.must(builder.keyword().onField("assignedInspectionOfficer").matching(loggedInUser).createQuery())
                        } else {
                            query.must(builder.keyword().onField("assigner").matching(loggedInUser).createQuery())
                        }
                    } catch (ex: java.lang.Exception) {
                        KotlinLogging.logger { }.debug("Invalid user type:", ex)
                    }
                }
                "completed" -> {
                    query.must(builder.keyword().onField("approveRejectCdStatus").matching("NULL").createQuery()).not()
                }
            }
        }
        // Filter by area of consignment document type
        cdType?.let {
            query.must(builder.keyword().onField("cdType").matching(it).createQuery())
        }
        // Retrieve search result
        val queryResult: FullTextQuery = searchManager.createFullTextQuery(query.createQuery(), ConsignmentDocumentDetailsEntity::class.java)
                .setMaxResults(30)
        return queryResult.getResultList() as List<ConsignmentDocumentDetailsEntity>
    }


}