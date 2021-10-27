package org.kebs.app.kotlin.apollo.store.events

import mu.KotlinLogging
import org.hibernate.CacheMode
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
            fullTextEntityManager.createIndexer()
                    .cacheMode(CacheMode.REFRESH)
                    .purgeAllOnStart(false)
                    .optimizeOnFinish(true)
                    .startAndWait()
        } catch (e: InterruptedException) {
            KotlinLogging.logger { }.error("Error occurred trying to build Hibernate Search indexes ", e)
        }
    }

    fun searchConsignmentDocuments(keywords: String?, category: String?, cdType: ConsignmentDocumentTypesEntity?, inspectionOfficer: Boolean, loggedInUser: UsersEntity?, page: Int = 0): List<ConsignmentDocumentDetailsEntity> {
//        val loggedInUser = commonDaoServices.getLoggedInUser()
        val searchManager = Search.getFullTextEntityManager(entityManager)
        val builder: QueryBuilder = searchManager.searchFactory
                .buildQueryBuilder()
                .forEntity(ConsignmentDocumentDetailsEntity::class.java)
                .get()
// Remove Old CD from result
        val query = builder.bool()
                .must(builder.range().onField("oldCdStatus").above(0).createQuery())
        if (StringUtils.hasLength(keywords)) {
            // Key words on application status
            query.should(builder.phrase().withSlop(2)
//                    .boostedTo(6.5f).withConstantScore()
                    .onField("varField10").sentence(keywords).createQuery())
            query.should(builder.phrase().withSlop(2)
//                    .boostedTo(4.5f).withConstantScore()
                    .onField("description").sentence(keywords).createQuery())
            // Others
            query.should(builder.keyword().wildcard()
//                    .boostedTo(1.5f).withConstantScore()
                    .onFields("ucrNumber", "cdRefNumber", "cocNumber", "idfNumber")
                    .matching(keywords).createQuery())
        }
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
                    query.must(builder.keyword().onField("approveRejectCdStatus").matching("NULL").createQuery())
                }
            }
        }

        // Filter by area of consignment document type
        cdType?.let {
            query.must(builder.keyword()
//                    .boostedTo(9.5f).withConstantScore()
                    .onField("cdType").matching(it).createQuery())
        }
        val q=query.createQuery()
        KotlinLogging.logger {  }.info("Query: ${q}")
        // Retrieve search result
        val queryResult: FullTextQuery = searchManager.createFullTextQuery(q, ConsignmentDocumentDetailsEntity::class.java)
                .setFirstResult(page * 30)
                .setMaxResults(30)
        return queryResult.getResultList() as List<ConsignmentDocumentDetailsEntity>
    }


}