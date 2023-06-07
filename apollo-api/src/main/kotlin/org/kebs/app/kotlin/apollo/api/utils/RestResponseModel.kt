package org.kebs.app.kotlin.apollo.api.utils

import com.fasterxml.jackson.annotation.JsonInclude

class RestResponseModel {
    val status: Int
    val message: String
    @JsonInclude(JsonInclude.Include.NON_NULL)
    var data: Any? = null

    constructor(status: Int, message: String) {
        this.status = status
        this.message = message
    }

    constructor(status: Int, message: String, data: Any) {
        this.status = status
        this.message = message
        this.data = data
    }
}
