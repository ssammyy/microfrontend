package org.kebs.app.kotlin.apollo.common.dto.stdLevy

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp
import java.time.LocalDate

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
    var nameOfBranch: String? = null,
    var manufacture_status: Int? = null

)

class NotificationForm (val responseMessage: String,val responseStatus: String,val responseButton: String,val responseMsg: String) {
}

class CommentForm (val responseMessage: String,val responseStatus: String,val responseButton: String,val responseMsg: String) {
}

class PaymentStatus (val responseStatus: Long) {
}
//class NotificationForm (val savedRowID: Long?, val entryNumber: String,val responseMessage: String) {
//}

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
    var taskId: String? = null,
    var processId: String? = null,
    var entryNumber: String? = null,
    var companyName: String? = null,
    var kraPin: String? = null,
    var registrationNumber: String? = null


)

data class EditCompanyTaskToDTO(
    var companyName: String? = null,
    var kraPin: String? = null,
    var otherBusinessNatureType: String? = null,
    var registrationNumber: String? = null,
    var entryNumber: String? = null,
    var companyId: Long? = null,
    var physicalAddressEdit: String? = null,
    var physicalAddress: String? = null,
    var postalAddress: String? = null,
    var postalAddressEdit: String? = null,
    var companyEmailEdit: String? = null,
    var companyEmail: String? = null,
    var companyTelephoneEdit: String? = null,
    var companyTelephone: String? = null,
    var yearlyTurnoverEdit: BigDecimal? = null,
    var yearlyTurnover: BigDecimal? = null,
    var ownership: String? = null,
    var ownershipEdit: String? = null,
    var taskType: Long? = null,
    var userType: Long? = null,
    var typeOfManufacture: Int? = null,
    var remarks: String? = null,
    var assignedTo: Long? = null,
    var processId: String?= null,
    var taskId: String? = null,
    var accentTo: Boolean? = null,

    var editID: Long? = null

)
data class AssignCompanyTaskToDTO(
    var manufacturerEntity: Long? = null,
    var assignedTo: Long? = null,
    var companyName: String? = null,
    var kraPin: String? = null,
    var status: Int? = null,
    var registrationNumber: String? = null,
    var postalAddress: String? = null,
    var physicalAddress: String? = null,
    var plotNumber: String? = null,
    var companyEmail: String? = null,
    var companyTelephone: String? = null,
    var yearlyTurnover: BigDecimal? = null,
    var businessLineName: String?=null,
    var businessLines: Long?=null,
    var businessNatureName: String?=null,
    var businessNatures: Long?=null,
    var regionName: String?=null,
    var region: Long?=null,
    var countyName: String?=null,
    var county: Long?=null,
    var townName: String?=null,
    var town: Long?=null,
    var buildingName: String? = null,
    var branchName: String? = null,
    var streetName: String? = null,
    var directorIdNumber: String? = null,
    var manufactureStatus: Int? = null,
    var entryNumber: String? = null,
    var contactId: Long? = null,
    var taskType: Long? = null,
    var typeOfManufacture: Int? = null,
    var otherBusinessNatureType: String? = null
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
    var makeRemarks: String? = null,
    var status: Int? = null,
    var assistantManager: Long? = null,
    var reportDate: LocalDate? = null,
    var assistantManagerApproval: Int? = null,
    var managersApproval: Int? = null,
    var assistantManagerRemarks: String? = null,
    var cheifManagerRemarks: String? = null,
    var officersFeedback: String? = null,
    var taskId: String? = null,
    var processId: String? = null,
    var visitID: Long? = null,
    var assigneeId: Long? = null,
    var manufacturerEntity: Long? = null,
    var userType: Long? = null,
    var complianceStatus: Long? = null
)

class ProcessInstanceResponseValueSite(val savedRowID: Long?,val processId: String, val isEnded: Boolean) {
}

class LevyClosureValue(val savedRowID: Long?) {
}

data class SuspendCompanyDto(
   var id: Long? = null,
   var name: String? = null,
   var reason: String? = null,
   var dateOfSuspension: String? = null,

){

}

data class CompanySuspendDto(
    var id: Long? = null,
    var companyId: Long? = null,
){}

data class ExemptionStatusDto(
    var companyId: Long? = null,
    var exemptionStatus: Long? = null

){}

data class CompanyCloseDto(
    var id: Long? = null,
    var companyId: Long? = null,
){}

data class CloseCompanyDto(
    var id: Long? = null,
    var name: String? = null,
    var reason: String? = null,
    var dateOfClosure: Timestamp? = null,

    ){

}

data class SiteVisitReportDecisionDTO(
    var taskId: String? = null,
    var processId: String? = null,
    var accentTo: Boolean? = null,
    var visitID: Long? = null,
    var assigneeId: Long? = null,
    var manufacturerEntity: Long? = null,
    var contactId: Long? = null,
    var comments: String? = null,
    var companyName: String? = null,
    var userType: Long? = null,
    var approvalStatus: String? = null,
    var approvalStatusId: Long? = null,
    var status: String? = null,
    var remarkBy: String? = null,
    var role: String? = null,
    var description: String? =null,
    var scheduledVisitDate: java.util.Date? = null


){

}


class SiteVisitReportDecision(

    @JsonProperty("taskId") val taskId: String,
    @JsonProperty("accentTo") val accentTo: Boolean,
    @JsonProperty("visitID")  val visitID: Long,
    @JsonProperty("comments") val comments: String,
    @JsonProperty("assigneeId")  val assigneeId: Long,
    @JsonProperty("contactId")  val contactId: Long,
    @JsonProperty("manufacturerEntity")  val manufacturerEntity: Long



) {
}

data class SiteVisitDocDTO(
    var siteVisitDocList: List<SLFilesListDto>? = null
)

data class SLFilesListDto(
    var id: Long? = null,
    var name: String? = null,
    var fileType: String? = null,
    var documentType: String? = null,
    var document: ByteArray? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SLFilesListDto

        if (id != other.id) return false
        if (name != other.name) return false
        if (fileType != other.fileType) return false
        if (documentType != other.documentType) return false
        if (document != null) {
            if (other.document == null) return false
            if (!document.contentEquals(other.document)) return false
        } else if (other.document != null) return false

        return true
    }
    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (fileType?.hashCode() ?: 0)
        result = 31 * result + (documentType?.hashCode() ?: 0)
        result = 31 * result + (document?.contentHashCode() ?: 0)
        return result
    }
}

data class SendEmailDto(
    var userId: Long? = null,
    var email: String? = null,
){}

data class VerifyEmailDto(
    var userId: Long? = null,
    var email: String? = null,
    var verificationToken: String? = null,
){}

class ResponseMessage (val responseMessage: String) {
}
class ResponseNotification (val responseMessage: Int) {
}
data class LevyFiltersDTOs(
    var startDate: Date? = null,
    var endDate: Date? =null,
    var region : String? = null
){}

data class LevyFilterDTO(
    var startDate: Date? = null,
    var endDate: Date? =null,
    var businessLines: Long?= null,
    var region : Long? = null,
    var company : String? = null,
    var kraPin : String? = null,
){}

data class LevyFiltersDTO(
    var periodFrom: Date? = null,
    var periodTo: Date? =null,
    var businessLines: Long?= null,
    var region : Long? = null,
){}



