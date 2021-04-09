package org.kebs.app.kotlin.apollo.api.errors

data class ErrorResponse(val message: String, val details: MutableList<String?>)