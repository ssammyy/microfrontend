package org.kebs.app.kotlin.apollo.common.dto.kappa.requests

data class ValidationRequest(
    val header: RequestHeader,
    val request: ValidationRequestBody,
)