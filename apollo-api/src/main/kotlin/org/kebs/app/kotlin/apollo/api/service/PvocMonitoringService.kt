package org.kebs.app.kotlin.apollo.api.service

import org.kebs.app.kotlin.apollo.store.model.pvc.PvocAgentMonitoringStatusEntity
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocPartnersEntity
import org.kebs.app.kotlin.apollo.store.repo.IPvocAgentMonitoringStatusEntityRepo
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.Instant

enum class MonitoringStatus(val status: Int) {
    OPEN(1), REVIEW(2), CLOSED(4)
}

@Service
class PvocMonitoringService(
        private val iPvocAgentMonitoringStatusEntityRepo: IPvocAgentMonitoringStatusEntityRepo,
) {
    fun findMonitoringRecord(yearMonth: String, agent: PvocPartnersEntity): PvocAgentMonitoringStatusEntity {
        val monitoringOptional = this.iPvocAgentMonitoringStatusEntityRepo.findFirstByPartnerIdAndYearMonthAndStatus(agent, yearMonth, MonitoringStatus.OPEN.status)
        if (monitoringOptional.isPresent) {
            return monitoringOptional.get()
        } else {
            val monitoring = PvocAgentMonitoringStatusEntity()
            monitoring.partnerId = agent
            monitoring.recordNumber = yearMonth + (this.iPvocAgentMonitoringStatusEntityRepo.countByPartnerIdAndYearMonth(agent, yearMonth) + 1)
            monitoring.yearMonth = yearMonth
            monitoring.description = "Agent Monitoring record for $yearMonth"
            monitoring.monitoringStatus = MonitoringStatus.OPEN.status
            monitoring.monitoringStatusDesc = MonitoringStatus.OPEN.name
            monitoring.status = 1
            monitoring.createdOn = Timestamp.from(Instant.now())
            monitoring.createdBy = "system"
            return this.iPvocAgentMonitoringStatusEntityRepo.save(monitoring)
        }
    }
}