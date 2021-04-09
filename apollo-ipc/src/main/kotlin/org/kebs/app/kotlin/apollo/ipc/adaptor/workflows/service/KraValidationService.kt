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

package org.kebs.app.kotlin.apollo.ipc.adaptor.workflows.service

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.store.model.ServiceRequestsEntity
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.WorkflowTransactionsEntity
import org.kebs.app.kotlin.apollo.store.repo.IServiceRequestsRepository
import org.kebs.app.kotlin.apollo.store.repo.IWorkflowTransactionsRepository
import org.springframework.stereotype.Service
import java.sql.Date
import java.sql.Timestamp
import java.time.Instant
import java.util.*

@Service
class KraValidationService(
    private val serviceRequestsRepository: IServiceRequestsRepository,
    private val workflowTransactionsRepository: IWorkflowTransactionsRepository
) {
    fun validatePin(sr: ServiceRequestsEntity, user: UsersEntity, workFlowId: Long): Int? {

        var log = WorkflowTransactionsEntity()
        try {
            sr.payload = user.toString()

            with(log) {
                transactionDate = Date(Date().time)
                transactionStartDate = Timestamp.from(Instant.now())
                serviceRequests = sr
                retried = 0
                transactionStatus = sr.serviceMapsId?.initStatus!!
                createdBy = "${sr.id}_${sr.transactionReference}"
                createdOn = Timestamp.from(Instant.now())

            }
            /**
             * TODO: KRA Integration not yet implemented
             */
            throw Exception("Integration not yet implemented, contact Administrator")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error { e }
            log.responseMessage = e.message
            log.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            log.transactionStatus = sr.serviceMapsId?.exceptionStatus!!
        }
        log.transactionCompletedDate = Timestamp.from(Instant.now())

        try {
            serviceRequestsRepository.save(sr)
            log = workflowTransactionsRepository.save(log)
        } catch (e: Exception) {
            KotlinLogging.logger { }.error { e }

        }

        return log.transactionStatus
    }
}