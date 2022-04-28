package org.kebs.app.kotlin.apollo.api.service

import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocAgentMonitoringStatusEntity
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocPartnersEntity
import org.kebs.app.kotlin.apollo.store.repo.IPvocAgentMonitoringStatusEntityRepo
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.Instant

enum class MonitoringStatus(val status: Int) {
    OPEN(1), NEW(2), REVIEW(3), APPROVE(4), REJECTED(5)
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

    fun listAgentMonitoring(status: String, page: PageRequest): ApiResponseModel {
        val response = ApiResponseModel()
        when (status) {
            "OPEN" -> iPvocAgentMonitoringStatusEntityRepo.findAllByStatus(MonitoringStatus.OPEN.status, page)
            "NEW" -> iPvocAgentMonitoringStatusEntityRepo.findAllByStatus(MonitoringStatus.NEW.status, page)
            "REVIEW" -> iPvocAgentMonitoringStatusEntityRepo.findAllByStatus(MonitoringStatus.REVIEW.status, page)
            "APPROVE" -> iPvocAgentMonitoringStatusEntityRepo.findAllByStatus(MonitoringStatus.APPROVE.status, page)
            "REJECTED" -> iPvocAgentMonitoringStatusEntityRepo.findAllByStatus(MonitoringStatus.REJECTED.status, page)
        }
        return response
    }

}