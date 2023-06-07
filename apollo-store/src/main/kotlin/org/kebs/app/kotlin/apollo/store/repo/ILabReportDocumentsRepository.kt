package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.LabReportDocumentsEntity
import org.kebs.app.kotlin.apollo.store.model.LabReportsEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository

interface ILabReportDocumentsRepository: HazelcastRepository<LabReportDocumentsEntity, Long> {
    fun findByLabReport(labReport: LabReportsEntity): LabReportDocumentsEntity?
}