package org.kebs.app.kotlin.apollo.api.ports.provided.mpesa.requests

data class RequestHeader(
    val serviceName: String,
    val messageID: String,
    val connectionID: String,
    val connectionPassword: String,
)