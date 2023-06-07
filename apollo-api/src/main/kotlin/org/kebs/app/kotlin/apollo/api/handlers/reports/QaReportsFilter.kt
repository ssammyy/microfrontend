package org.kebs.app.kotlin.apollo.api.handlers.reports;

import liquibase.pro.packaged.au.*
import org.kebs.app.kotlin.apollo.store.model.qa.PermitApplicationsEntity
import org.springframework.data.jpa.domain.Specification
import java.math.BigDecimal
import java.util.*
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root


class QaReportsFilter {
    private fun rentalRateBetween(
        minRate: Optional<BigDecimal?>,
        maxRate: Optional<BigDecimal?>
    ): Specification<PermitApplicationsEntity> {
        return Specification<PermitApplicationsEntity> { root: Root<PermitApplicationsEntity>, query: CriteriaQuery<*>?, builder: CriteriaBuilder ->
            minRate.map { min ->
                maxRate.map { max ->
                    builder.between(
                        root.get("createdAt"),
                        min,
                        max
                    )
                }.orElse(null)
            }.orElse(null)
        }
    }

    fun releaseYearEqualTo(releaseYear: Optional<Long?>): Specification<PermitApplicationsEntity> {
        return Specification<PermitApplicationsEntity> { root: Root<PermitApplicationsEntity>, query: CriteriaQuery<*>?, builder: CriteriaBuilder ->
            releaseYear.map { relYear ->
                builder.equal(
                    root.get<String>("releaseYear"),
                    java.lang.String.valueOf(relYear)
                )
            }.orElse(null)
        }
    }


}
