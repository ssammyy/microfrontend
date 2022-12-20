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
    var requestId: Long?=null
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
    var id: Long?=null,
    var requestId: Long?=null,
    var comments: String?=null
){

}
