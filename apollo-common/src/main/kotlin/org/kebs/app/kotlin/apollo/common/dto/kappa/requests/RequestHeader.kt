package org.kebs.app.kotlin.apollo.common.dto.kappa.requests

data class RequestHeader(
    val serviceName: String,
    val messageID: String,
    val connectionID: String,
    val connectionPassword: String,
)