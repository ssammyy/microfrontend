package org.kebs.app.kotlin.apollo.api.ports.provided.sms

interface ISmsService {

    fun sendSms(phone: String, message: String): Boolean
}