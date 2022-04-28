package org.kebs.app.kotlin.apollo.api.payload.response

class CallbackResponses {
    lateinit var status: String
    lateinit var message: String
    var data: Any? = null
    var errors: Any? = null
}