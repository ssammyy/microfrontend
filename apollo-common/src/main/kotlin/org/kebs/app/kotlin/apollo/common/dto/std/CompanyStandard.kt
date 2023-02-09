package org.kebs.app.kotlin.apollo.common.dto.std

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp


class ComJustificationDecision(
    @JsonProperty("taskId") val taskId: String,
    @JsonProperty("accentTo") val accentTo: Boolean,
    @JsonProperty("approvalID")  val approvalID: Long,
    @JsonProperty("comments") val comments: String
    ) {
}
class ComDraftDecision(
    @JsonProperty("taskId") val taskId: String,
    @JsonProperty("accentTo") val accentTo: Boolean,
    @JsonProperty("approvalID")  val approvalID: Long,
    @JsonProperty("comments") val comments: String,
    @JsonProperty("processId") val processId: String,
    @JsonProperty("assignedTo") var assignedTo: Long?
) {
}

class ComStdDraftDto(
    var title: String?=null,
    var scope: String?=null,
    var normativeReference: String?=null,
    var symbolsAbbreviatedTerms: String?=null,
    var clause: String?=null,
    var special: String?=null,
    var uploadedBy: String?=null,
    var requestNumber: String?=null,
    var requestId: Long?=null,
    var departmentId: Long?=null,
    var subject: String?=null,
    var description: String?=null,
    var contactOneFullName: String?=null,
    var contactOneTelephone: String?=null,
    var contactOneEmail: String?=null,
    var contactTwoFullName: String?=null,
    var contactTwoTelephone: String?=null,
    var contactTwoEmail: String?=null,
    var contactThreeFullName: String?=null,
    var contactThreeTelephone: String?=null,
    var contactThreeEmail: String?=null,
    var companyName: String?=null,
    var companyPhone: String?=null
){

}

class ResponseMsg (val responseMessage: String) {
}
class IntStdDraftDecisionDto(
    var requestId: Long?=null,
    var comments: String?=null,
    var accentTo: String?=null,
    var proposalId: Long?=null,
    var draftId: Long?=null
){

}

class ComStdDraftDecisionDto(
    var title: String?=null,
    var scope: String?=null,
    var normativeReference: String?=null,
    var symbolsAbbreviatedTerms: String?=null,
    var clause: String?=null,
    var special: String?=null,
    var uploadedBy: String?=null,
    var requestNumber: String?=null,
    var accentTo: String?=null,
    var response: String?=null,
    var id: Long?=null,
    var requestId: Long?=null,
    var comments: String?=null,
    var companyName: String?=null,
    var companyPhone: String?=null,
    var contactOneFullName: String?=null,
    var contactOneEmail: String?=null,
    var contactOneTelephone: String?=null,
    var departmentId: Long?=null,
    var subject: String?=null,
    var description: String?=null,
    var contactTwoFullName: String?=null,
    var contactTwoTelephone: String?=null,
    var contactTwoEmail: String?=null,
    var contactThreeFullName: String?=null,
    var contactThreeTelephone: String?=null,
    var contactThreeEmail: String?=null,
){

}


class ComDraftCommentDto(
    var commentTitle: String?=null,
    var commentDocumentType: String?=null,
    var uploadDate: Timestamp?=null,
    var nameOfRespondent: String?=null,
    var emailOfRespondent: String?=null,
    var phoneOfRespondent: String?=null,
    var nameOfOrganization: String?=null,
    var clause: String?=null,
    var paragraph: String?=null,
    var typeOfComment: String?=null,
    var comment: String?=null,
    var proposedChange: String?=null,
    var observation: String?=null,
    var requestID: Long?=null,
    var draftID: Long?=null,

    var draftComment: String?=null,
    var comNameOfOrganization: String?=null,
    var comClause: String?=null,
    var comParagraph: String?=null,
    var recommendations: String?=null,
    var positionOfRespondent: String?=null,
    var adoptStandard: String?=null,
    var adoptDraft: String?=null,
    var reason: String?=null,
){

}
