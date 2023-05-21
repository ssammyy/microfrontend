package org.kebs.app.kotlin.apollo.common.dto.std

import org.kebs.app.kotlin.apollo.common.dto.qa.FilesListDto
import java.sql.Timestamp


class TaskDetails(val taskId: String, val name: String, val taskData: Map<String, Any>) {
}

class TaskDetailsBody(val taskId: String, val name: String, val processId: String, val taskData: Map<String, Any>) {
}

class WorkShopAgreementTasks(
    val taskId: String,
    val name: String,
    val processId: String,
    val taskData: Map<String, Any>
) {
}

class InternationalStandardTasks(
    val taskId: String,
    val name: String,
    val processId: String,
    val taskData: Map<String, Any>
) {
}


class StdUserTasks(val taskId: String, val name: String, val processId: String, val taskData: Map<String, Any>) {
}

data class DepartmentDto(
    var departmentName: String? = null

)


class StandardReviewTasks(val taskId: String, val name: String, val processId: String, val taskData: Map<String, Any>) {
}

data class AllRequestDetailsApplyDto(
    var id: Long? = null,
    var requestDetailsDto: StandardsDto? = null,
    var nwiDetailsDto: NwiDetailsDto? = null,
    var justificationDetailsDto: JustificationDetailsDto? = null,


    )


data class StandardsDto(
    var id: Long? = null,
    var requestNumber: String? = null,
    var rank: String? = null,
    var name: String? = null,
    var phone: String? = null,
    var email: String? = null,
    var submissionDate: Timestamp? = null,
    var departmentId: String? = null,
    var tcId: String? = null,
    var organisationName: String? = null,
    var subject: String? = null,
    var description: String? = null,
    var economicEfficiency: String? = null,
    var healthSafety: String? = null,
    var environment: String? = null,
    var integration: String? = null,
    var exportMarkets: String? = null,
    var levelOfStandard: String? = null,
    var status: String? = null,
    var departmentName: String? = null,
    var standardCreationDate:Timestamp?=null,
    //HOF Feedback
    var tcSecAssigned: String? = null,
    var reviewedBy: String? = null,
    var reviewDate: Timestamp? = null,
    var desiredOutput: String? = null,
    var desiredResult: String? = null,
    var reason: String? = null,

    var createdBy: String? = null,
    var createdOn: Timestamp? = null,
    var modifiedOn: Timestamp? = null,
    var deletedOn: Timestamp? = null,
    var deleteBy: String? = null,


    )

data class VotesDto(
    var id: Long? = null,
    var decision: String? = null,
    var reason: String? = null,
    var dateVoteWasCast: Timestamp? = null,
    var nwiId: Long? = null,
    var proposalTitle: String? = null,
    var closingDate: String? = null,
    var standardId: Long? = null,
    var requestNumber: String? = null,


    )

data class NwiDetailsDto(
    var id: Long? = null,
    var proposalTitle: String? = null,
    var scope: String? = null,
    var purpose: String? = null,
    var targetDate: String? = null,
    var similarStandards: String? = null,
    var liaisonOrganisation: Timestamp? = null,
    var referenceNumber: String? = null,
    var pdStatus: String? = null,
    var minutesPdStatus: String? = null,
    var draftDocsPdStatus: String? = null,
    var prPdStatus: String? = null,
    var attachedDocuments: List<FilesListDto>? = null


)

data class JustificationDetailsDto(
    var id: Long? = null,
    var nwiId: Long? = null,
    var spcMeetingDate: Long? = null,
    var departmentId: String? = null,
    var tcId: String? = null,
    var tcSecretary: String? = null,
    var sl: String? = null,
    var title: String? = null,
    var edition: String? = null,
    var requestNo: String? = null,
    var issuesAddressedBy: String? = null,
    var tcAcceptanceDate: String? = null,
    var status: String? = null,
    var requestedBy: String? = null,
    var cdNumber: String? = null,
    var createdOn: Timestamp? = null,
    var createdBy: Long? = null,
    )


