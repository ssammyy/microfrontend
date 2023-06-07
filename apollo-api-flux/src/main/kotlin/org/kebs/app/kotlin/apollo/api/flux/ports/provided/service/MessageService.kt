package org.kebs.app.kotlin.apollo.api.flux.ports.provided.service

import org.springframework.stereotype.Service

@Service
class MessageService {
    fun print(msg: String?) {
        println(msg)
    }
}