package org.kebs.app.kotlin.apollo.common.dto.stdLevy

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp
import java.time.LocalDate
import java.util.*

data class StdLevyNotificationFormDTO(
    var NameAndBusinessOfProprietors: String? = null,
    var AllCommoditiesManufuctured: String? = null,
    var chiefExecutiveDirectors: String? = null,
    var chiefExecutiveDirectorsStatus: Int? = 0,
    var DateOfManufacture: Date? = null,
    var totalValueOfManufacture: BigDecimal? = null,
    var description: String? = null,
    var status: Long? = null,
    var entryNumber: String? = null,
    var companyProfileID: Long? = null,

)

class NotificationForm (val savedRowID: Long?, val entryNumber: String) {
}

data class StdLevyScheduleSiteVisitDTO(
    var status: Int? = null,
    var assistantManagerApproval: Int? = null,
    var managersApproval: Int? = null,
    var assistantManager: Long? = null,
    var principalLevyOfficer: Long? = null,
    var slManager: Long? = null,
    var purpose: String? = null,
    var personMet: String? = null,
    var actionTaken: String? = null,
    var remarks: String? = null,
    var officersFeedback: String? = null,
    var manufacturerEntity: Long? = null,
    var scheduledVisitDate: java.util.Date? = null,
    var visitDate: java.util.Date? = null,
    var reportDate: LocalDate? = null,
    var slStatus: Int? = null,
    var slStartedOn: Timestamp? = null,
    var slCompletedOn: Timestamp? = null,
    var slProcessInstanceId: String? = null,
    var cheifManagerRemarks: String? = null,
    var assistantManagerRemarks: String? = null,
    var createdBy: Long? = null,
    var createdOn:  Timestamp? = null,


)
class ProcessInstanceSiteResponse(val processId: String, val isEnded: Boolean) {
}
class ProcessInstanceResponseSite(val savedRowID: String,val processId: String, val isEnded: Boolean) {
}
data class ReportOnSiteVisitDTO(
    var visitDate: java.util.Date? = null,
    var purpose: String? = null,
    var personMet: String? = null,
    var actionTaken: String? = null,
    var status: Int? = null,
    var assistantManager: Long? = null,
    var reportDate: LocalDate? = null,
    var assistantManagerApproval: Int? = null,
    var managersApproval: Int? = null,
    var assistantManagerRemarks: String? = null,
    var cheifManagerRemarks: String? = null,
    var officersFeedback: String? = null,
    var taskId: String? = null,
    var visitID: Long? = null,
)

class ProcessInstanceResponseValueSite(val savedRowID: Long?,val processId: String, val isEnded: Boolean) {
}
class SiteVisitReportDecision(
    @JsonProperty("taskId") val taskId: String,
    @JsonProperty("accentTo") val accentTo: Boolean,
    @JsonProperty("visitID")  val visitID: Long,
    @JsonProperty("comments") val comments: String
) {
}