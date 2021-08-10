package org.kebs.app.kotlin.apollo.common.dto.std

import com.fasterxml.jackson.annotation.JsonProperty

class Decision(@JsonProperty("taskId") val taskId: String,@JsonProperty("decision")  val decision: Boolean) {
}
