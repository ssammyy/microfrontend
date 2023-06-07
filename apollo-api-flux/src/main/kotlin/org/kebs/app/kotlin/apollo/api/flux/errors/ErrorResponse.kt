package org.kebs.app.kotlin.apollo.api.flux.errors

data class ErrorResponse(val message: String, val details: MutableList<String?>)