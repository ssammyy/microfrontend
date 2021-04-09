package org.kebs.app.kotlin.apollo.api.controllers

import mu.KotlinLogging
import org.json.JSONObject
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/")
class MpesaCallBack {

    @PostMapping(path = ["/auth/mpesa-callback"], consumes = ["application/json"], produces = ["application/json"])
    fun addMember(@RequestBody payload: JSONObject?) : ResponseEntity<String> {
        KotlinLogging.logger { }.info { "My mpesa response from safaricom details:  = ${payload.toString()}" }
        return ResponseEntity(HttpStatus.OK)
//        println(payload)
    }
}