package org.kebs.app.kotlin.apollo.common.dto.std

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp

class WorkshopAgreement(
    @JsonProperty("taskId") val taskId: String,
    @JsonProperty("accentTo") val accentTo: Boolean,
    @JsonProperty("approvalID")  val approvalID: Long,
    @JsonProperty("comments") val comments: String,
    @JsonProperty("jstNumber")  val jstNumber: Long
    ) {
}

class NWAJustificationDecision(
    @JsonProperty("taskId") val taskId: String,
    @JsonProperty("accentTo") val accentTo: Boolean,
    @JsonProperty("approvalID")  val approvalID: Long,
    @JsonProperty("comments") val comments: String
    ) {
}

class NWAPreliminaryDraftDecision(
    @JsonProperty("taskId") val taskId: String,
    @JsonProperty("accentTo") val accentTo: Boolean,
    @JsonProperty("approvalID")  val approvalID: Long,
    @JsonProperty("comments") val comments: String,
    @JsonProperty("diJNumber")  val diJNumber: Long
    ) {
}
class NWAWorkshopDraftDecision(
    @JsonProperty("taskId") val taskId: String,
    @JsonProperty("accentTo") val accentTo: Boolean,
    @JsonProperty("approvalID")  val approvalID: Long,
    @JsonProperty("comments") val comments: String
    ) {
}
class NWADiJustification(
    @JsonProperty("taskId") var taskId: String,
    @JsonProperty("identifiedNeed") var identifiedNeed: String,
    @JsonProperty("cost")  var cost: String,
    @JsonProperty("numberOfMeetings") var numberOfMeetings: String,
    @JsonProperty("dateOfApproval")  var dateOfApproval: String,
    @JsonProperty("kID")  var kID: Long,
    @JsonProperty("datePrepared")  var datePrepared: Timestamp,
){}
class EditCompanyStandard(
    @JsonProperty("taskId") var taskId: String,
    @JsonProperty("title") var title: String,
    @JsonProperty("scope")  var cost: String,
    @JsonProperty("normativeReference") var normativeReference: String,
    @JsonProperty("symbolsAbbreviatedTerms")  var symbolsAbbreviatedTerms: String,
    @JsonProperty("clause") var clause: String,
    @JsonProperty("special")  var special: String,
    @JsonProperty("comStdNumber")  var comStdNumber: String,
    @JsonProperty("savedRowID")  var id: Long,
){}
class ISDecision(
    @JsonProperty("taskId") val taskId: String,
    @JsonProperty("accentTo") val accentTo: Boolean,
    @JsonProperty("approvalID")  val approvalID: Long,
    @JsonProperty("comments") val comments: String
) {
}
class ISJustificationDecision(
    @JsonProperty("taskId") val taskId: String,
    @JsonProperty("accentTo") val accentTo: Boolean,
    @JsonProperty("approvalID")  val approvalID: Long,
    @JsonProperty("comments") val comments: String
) {
}
