package org.kebs.app.kotlin.apollo.common.dto.std

import com.fasterxml.jackson.annotation.JsonProperty

class WorkshopAgreement(@JsonProperty("taskId") val taskId: String, @JsonProperty("accentTo") val accentTo: Boolean, @JsonProperty("approvalID")  val approvalID: Long,@JsonProperty("comments") val comments: String,@JsonProperty("kID")  val kID: Long) {
}

class NWAJustificationDecision(@JsonProperty("taskId") val taskId: String, @JsonProperty("accentTo") val accentTo: Boolean, @JsonProperty("approvalID")  val approvalID: Long,@JsonProperty("comments") val comments: String) {
}

class NWAPreliminaryDraftDecision(@JsonProperty("taskId") val taskId: String, @JsonProperty("accentTo") val accentTo: Boolean, @JsonProperty("approvalID")  val approvalID: Long,@JsonProperty("comments") val comments: String) {
}
class NWAWorkshopDraftDecision(@JsonProperty("taskId") val taskId: String, @JsonProperty("accentTo") val accentTo: Boolean, @JsonProperty("approvalID")  val approvalID: Long,@JsonProperty("comments") val comments: String) {
}
