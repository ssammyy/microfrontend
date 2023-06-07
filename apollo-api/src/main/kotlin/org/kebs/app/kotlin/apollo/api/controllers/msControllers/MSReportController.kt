/*
 *
 *  *
 *  *
 *  *  *    Copyright (c) ${YEAR}.   BSK Global Technologies
 *  *  *
 *  *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *    you may not use this file except in compliance with the License.
 *  *  *    You may obtain a copy of the License at
 *  *  *
 *  *  *       http://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  *    Unless required by applicable law or agreed to in writing, software
 *  *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *   See the License for the specific language governing permissions and
 *  *  *   limitations under the License.
 *  *
 *
 */

package org.kebs.app.kotlin.apollo.api.controllers.msControllers//package org.kebs.app.kotlin.apollo.api.controllers
//
//
//import mu.KotlinLogging
//import org.kebs.app.kotlin.apollo.common.dto.ServiceMaps
//import org.kebs.app.kotlin.apollo.ipc.adaptor.kafka.SendToKafkaQueue
//import org.kebs.app.kotlin.apollo.ipc.adaptor.kafka.config.ProducerConfigure
//import org.kebs.app.kotlin.apollo.ms.ports.dto.*
//import org.springframework.http.ResponseEntity
//import org.springframework.web.bind.annotation.PostMapping
//import org.springframework.web.bind.annotation.RequestBody
//import org.springframework.web.bind.annotation.RequestMapping
//import org.springframework.web.bind.annotation.RestController
//
//@RestController
//@RequestMapping("/api/ms/report")
//class MSReportController {
//    private final var config = ProducerConfigure()
//    var submitToKafkaQueue = SendToKafkaQueue(config)
//    var serviceMaps = ServiceMaps()
//
//    @PostMapping("/create-preliminary")
//    fun createPreliminaryReport (@RequestBody preliminaryReport: PreliminaryReport): ResponseEntity<Any> {
//        // This happens after complaint has been received validated.
//        // The MSIO inputs the preliminary report which is saved to the db.
//
//        preliminaryReport.status = serviceMaps.preliminaryReportCreated
//
//        val topic = "create-preliminary-report-request-topic"
//        try {
//            val response = submitToKafkaQueue.submitRequestToBus(preliminaryReport, topic)
//            KotlinLogging.logger { }.info { "$response" }
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.error { e }
//        }
//
//        return ResponseEntity.ok<Any>("")
//    }
//
//    @PostMapping("/preliminary-hof-review")
//    fun hofReviewStagePreliminary (@RequestBody approvals: ApprovalsPreliminaryReport): ResponseEntity<Any> {
//        // This endpoint initial status is `preliminaryReportCreated`.
//        // If approved the status changes to `hodApprovalPreliminaryPending`.
//        // If fails the status changes to `failedHofApprovalPreliminary` and reverts to MSIO.
//
//        when (approvals.approval) {
//            true -> {approvals.application.status = serviceMaps.hodApprovalPreliminaryPending}
//            false -> {approvals.application.status = serviceMaps.failedHofApprovalPreliminary}
//        }
//
//        val topic = "preliminary-report-approval-request-topic"
//        try {
//            val response = submitToKafkaQueue.submitRequestToBus(approvals, topic)
//            KotlinLogging.logger { }.info { "$response" }
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.error { e }
//        }
//
//        return ResponseEntity.ok<Any>(approvals.application)
//    }
//
//    @PostMapping("/preliminary-hod-review")
//    fun hodReviewStagePreliminary (@RequestBody approvals: ApprovalsPreliminaryReport): ResponseEntity<Any> {
//        // This endpoint initial status is `hodApprovalPending`.
//        // If approved the status changes to `preliminaryReportApproved`.
//        // If fails the status changes to `failedHodApprovalPreliminary` and reverts to MSIO.
//
//        when (approvals.approval) {
//            true -> {approvals.application.status = serviceMaps.preliminaryReportApproved}
//            false -> {approvals.application.status = serviceMaps.failedHodApprovalPreliminary}
//        }
//
//        val topic = "preliminary-report-approval-request-topic"
//        try {
//            val response = submitToKafkaQueue.submitRequestToBus(approvals, topic)
//            KotlinLogging.logger { }.info { "$response" }
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.error { e }
//        }
//
//        return ResponseEntity.ok<Any>(approvals.application)
//    }
//
//    @PostMapping("/create-final")
//    fun createFinalReport (@RequestBody finalReport: FinalReport): ResponseEntity<Any> {
//        // This happens after complaint has been received validated.
//        // The MSIO inputs the preliminary report which is saved to the db.
//
//        finalReport.status = serviceMaps.finalReportApproved
//
//        val topic = "create-final-report-request-topic"
//        try {
//            val response = submitToKafkaQueue.submitRequestToBus(finalReport, topic)
//            KotlinLogging.logger { }.info { "$response" }
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.error { e }
//        }
//
//        return ResponseEntity.ok<Any>("")
//    }
//
//    @PostMapping("/final-hof-review")
//    fun hofReviewStageFinal (@RequestBody approvals: ApprovalsFinalReport): ResponseEntity<Any> {
//        // This endpoint initial status is `finalReportApproved`.
//        // If approved the status changes to `hodApprovalFinalPending`.
//        // If fails the status changes to `failedHofApprovalFinal` and reverts to MSIO.
//
//        when (approvals.approval) {
//            true -> {approvals.application.status = serviceMaps.hodApprovalFinalPending}
//            false -> {approvals.application.status = serviceMaps.failedHofApprovalFinal}
//        }
//
//        val topic = "final-report-approval-request-topic"
//        try {
//            val response = submitToKafkaQueue.submitRequestToBus(approvals, topic)
//            KotlinLogging.logger { }.info { "$response" }
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.error { e }
//        }
//
//        return ResponseEntity.ok<Any>(approvals.application)
//    }
//
//    @PostMapping("/final-hod-review")
//    fun hodReviewStageFinal (@RequestBody approvals: ApprovalsFinalReport): ResponseEntity<Any> {
//        // This endpoint initial status is `hodApprovalFinalPending`.
//        // If approved the status changes to `finalReportApproved`.
//        // If fails the status changes to `failedHodApprovalFinal` and reverts to MSIO.
//
//        when (approvals.approval) {
//            true -> {approvals.application.status = serviceMaps.finalReportApproved}
//            false -> {approvals.application.status = serviceMaps.failedHodApprovalFinal}
//        }
//
//        val topic = "final-report-approval-request-topic"
//        try {
//            val response = submitToKafkaQueue.submitRequestToBus(approvals, topic)
//            KotlinLogging.logger { }.info { "$response" }
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.error { e }
//        }
//
//        return ResponseEntity.ok<Any>(approvals.application)
//    }
//
//}
