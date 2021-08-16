package org.kebs.app.kotlin.apollo.api.payload

class ApiResponseModel {
    var extras: Any? = null
    lateinit var message: String
    lateinit var responseCode: String
    var data: Any? = null
    var errors: Any? = null
    var totalPages: Int? = null
}