package org.kebs.app.kotlin.apollo.api.ports.provided.spec

import org.kebs.app.kotlin.apollo.api.ports.provided.criteria.SearchCriteria
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.ms.ComplaintEntity
import org.kebs.app.kotlin.apollo.store.model.ms.MsComplaintFeedbackViewEntity
import org.springframework.data.jpa.domain.Specification
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

class ComplaintViewSpecification(private val criteria: SearchCriteria) : Specification<MsComplaintFeedbackViewEntity?> {


    override fun toPredicate(
        root: Root<MsComplaintFeedbackViewEntity?>,
        query: CriteriaQuery<*>,
        builder: CriteriaBuilder
    ): Predicate? {
        when {
            criteria.operation.equals(">", ignoreCase = true) -> {
                return builder.greaterThanOrEqualTo(
                    root.get(criteria.key), criteria.value.toString()
                )
            }
            criteria.operation.equals("<", ignoreCase = true) -> {
                return builder.lessThanOrEqualTo(
                    root.get(criteria.key), criteria.value.toString()
                )
            }
            criteria.operation.equals(":", ignoreCase = true) -> {
                return criteria.value
                    ?.let {
                        when (it) {
                            is String -> {
                                when {
                                    it.isEmpty() -> {
                                        null
                                    }
                                    else -> {
                                        when (root.get<Any>(criteria.key).javaType) {
                                            String::class.java -> {
                                                builder.like(
                                                    root.get(criteria.key), "%$it%"
                                                )
                                            }
                                            else -> {
                                                builder.equal(root.get<Any>(criteria.key), criteria.value)
                                            }
                                        }

                                    }
                                }
                            }
                            else -> {
                                builder.equal(root.get<Any>(criteria.key), criteria.value)

                            }
                        }

                    }


            }
            else -> return null
        }
    }
}