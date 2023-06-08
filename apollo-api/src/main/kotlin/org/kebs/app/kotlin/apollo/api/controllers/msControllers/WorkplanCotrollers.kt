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

package org.kebs.app.kotlin.apollo.api.controllers.msControllers
//
//import mu.KotlinLogging
//import org.kebs.app.kotlin.apollo.common.dto.ServiceMaps
//import org.kebs.app.kotlin.apollo.ipc.adaptor.kafka.SendToKafkaQueue
//import org.kebs.app.kotlin.apollo.ipc.adaptor.kafka.config.ProducerConfigure
//import org.kebs.app.kotlin.apollo.ms.ports.dto.*
//import org.springframework.http.ResponseEntity
//import org.springframework.web.bind.annotation.PostMapping
//import org.springframework.web.bind.annotation.RequestBody
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

//
@RestController
@RequestMapping("/api/ms/work-plan")
class WorkplanControllers {
//    private final var config = ProducerConfigure()
//    var submitToKafkaQueue = SendToKafkaQueue(config)
//    var serviceMaps = ServiceMaps()
//
//    @PostMapping("/create")
//    fun createWorkPlan (@RequestBody workPlan: WorkPlan): ResponseEntity<Any> {
//        // This happens after complaint has been received validated.
//        // The QAO inputs the work plan which is saved to the db.
//
//        val topic = "create-work-plan-request-topic"
//        try {
//            val response = submitToKafkaQueue.submitRequestToBus(workPlan, topic)
//            KotlinLogging.logger { }.info { "$response" }
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.error { e }
//        }
//
//        return ResponseEntity.ok<Any>("")
//    }
//
//    @PostMapping("/hod-review")
//    fun hodReviewStage (@RequestBody approvals: ApprovalsWorkPlan): ResponseEntity<Any> {
//        // This endpoint initial status is `hodApprovalBeforeAssessmentRequired`.
//        // If approved the status changes to `assessorAssignmentPending`.
//        // If fails the status changes to `failedHodApprovalBeforeAssessment` and reverts to QAO.
//
//        when (approvals.approval) {
//            true -> {approvals.application.status = serviceMaps.workPlanApproved}
//            false -> {approvals.application.status = serviceMaps.failedHodApprovalWorkPlan}
//        }
//
//        val topic = "work-plan-approval-request-topic"
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

    @GetMapping("/test")
    @Secured("ROLE_BASIC_ACCESS")
    fun testAuthorities(): ResponseEntity<Any> {
        return ResponseEntity.ok<Any>("I am authorized")
    }
}