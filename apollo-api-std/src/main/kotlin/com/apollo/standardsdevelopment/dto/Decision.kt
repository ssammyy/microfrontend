package com.apollo.standardsdevelopment.dto

import com.fasterxml.jackson.annotation.JsonProperty

class Decision(@JsonProperty("taskId") val taskId: String,@JsonProperty("decision")  val decision: Boolean) {
}