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

package org.kebs.app.kotlin.apollo.ipc.adaptor.workflows.service.ms

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.repo.*
import org.kebs.app.kotlin.apollo.store.repo.ms.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.sql.Date
import java.sql.Timestamp
import java.time.Instant
import java.util.*

@Service
class MarketSurveillanceWorkPlansWorkFlowService(
    private val workflowRepo: IServiceMapsWorkflowsRepository,
    val serviceRequestsRepository: IServiceRequestsRepository,
    val workflowTransactionsRepository: IWorkflowTransactionsRepository,
    val workPlanRepo: IWorkplanRepository,
    val workPlanUserRepo: IWorkPlanUsersRepository,
    val workPlanResourcesRepo: IWorkPlanResourcesRepository,
    val workPlanItemsRepo: IWorkPlanItemsRepository,
    val workPlanBudgetItemsRepo: IWorkPlanBudgetLinesRepository
) {
    fun addBudgetItemsToWorkPlan(sr: ServiceRequestsEntity, planBudgetLinesEntity: WorkPlanBudgetLinesEntity, workFlowId: Long): Int {
        var log = WorkflowTransactionsEntity()
        try {
            sr.payload = planBudgetLinesEntity.toString()
            var budgetLinesEntity = planBudgetLinesEntity

            with(log) {
                transactionDate = Date(Date().time)
                transactionStartDate = Timestamp.from(Instant.now())
                serviceRequests = sr
                retried = 0
                transactionStatus = sr.serviceMapsId?.initStatus!!
                createdBy = "${sr.id}_${sr.transactionReference}"
                createdOn = Timestamp.from(Instant.now())
                workflowRepo.findByIdOrNull(workFlowId)
                    ?.let { workflowsEntity -> methodId = workflowsEntity }
                transactionReference = "${sr.transactionReference}_${methodId?.methodName}"

            }

            with(budgetLinesEntity) {
                transactionDate = Date(Date().time)
                status = sr.serviceMapsId?.activeStatus
                createdBy = log.transactionReference
                createdOn = Timestamp.from(Instant.now())
                version = sr.serviceMapsId?.initStatus

            }
            budgetLinesEntity = workPlanBudgetItemsRepo.save(budgetLinesEntity)
            sr.requestId = budgetLinesEntity.id

        } catch (e: Exception) {
            KotlinLogging.logger { }.trace(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            log.responseMessage = e.message
            log.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            log.transactionStatus = sr.serviceMapsId?.exceptionStatus!!
        }
        log.transactionCompletedDate = Timestamp.from(Instant.now())

        try {
            serviceRequestsRepository.save(sr)
            log = workflowTransactionsRepository.save(log)
        } catch (e: Exception) {
            KotlinLogging.logger { }.trace(e.message, e)
            KotlinLogging.logger { }.error(e.message)

        }

        return log.transactionStatus
    }

    fun addItemsToWorkPlan(sr: ServiceRequestsEntity, planItemsEntity: WorkPlanItemsEntity, workFlowId: Long): Int {
        var log = WorkflowTransactionsEntity()
        try {
            sr.payload = planItemsEntity.toString()
            var itemsEntity = planItemsEntity

            with(log) {
                transactionDate = Date(Date().time)
                transactionStartDate = Timestamp.from(Instant.now())
                serviceRequests = sr
                retried = 0
                transactionStatus = sr.serviceMapsId?.initStatus!!
                createdBy = "${sr.id}_${sr.transactionReference}"
                createdOn = Timestamp.from(Instant.now())
                workflowRepo.findByIdOrNull(workFlowId)
                    ?.let { workflowsEntity -> methodId = workflowsEntity }
                transactionReference = "${sr.transactionReference}_${methodId?.methodName}"

            }

            with(itemsEntity) {
                transactionDate = Date(Date().time)
                status = sr.serviceMapsId?.activeStatus
                createdBy = log.transactionReference
                createdOn = Timestamp.from(Instant.now())
                version = sr.serviceMapsId?.initStatus

            }
            itemsEntity = workPlanItemsRepo.save(itemsEntity)
            sr.requestId = itemsEntity.id

        } catch (e: Exception) {
            KotlinLogging.logger { }.trace(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            log.responseMessage = e.message
            log.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            log.transactionStatus = sr.serviceMapsId?.exceptionStatus!!
        }
        log.transactionCompletedDate = Timestamp.from(Instant.now())

        try {
            serviceRequestsRepository.save(sr)
            log = workflowTransactionsRepository.save(log)
        } catch (e: Exception) {
            KotlinLogging.logger { }.trace(e.message, e)
            KotlinLogging.logger { }.error(e.message)

        }

        return log.transactionStatus
    }

    fun addResourcesToWorkPlan(sr: ServiceRequestsEntity, planUsersEntity: WorkPlanResourcesEntity, workFlowId: Long): Int {
        var log = WorkflowTransactionsEntity()
        try {
            sr.payload = planUsersEntity.toString()
            var resourcesEntity = planUsersEntity

            with(log) {
                transactionDate = Date(Date().time)
                transactionStartDate = Timestamp.from(Instant.now())
                serviceRequests = sr
                retried = 0
                transactionStatus = sr.serviceMapsId?.initStatus!!
                createdBy = "${sr.id}_${sr.transactionReference}"
                createdOn = Timestamp.from(Instant.now())
                workflowRepo.findByIdOrNull(workFlowId)
                    ?.let { workflowsEntity -> methodId = workflowsEntity }
                transactionReference = "${sr.transactionReference}_${methodId?.methodName}"

            }

            with(resourcesEntity) {
                transactionDate = Date(Date().time)
                status = sr.serviceMapsId?.activeStatus
                createdBy = log.transactionReference
                createdOn = Timestamp.from(Instant.now())
                version = sr.serviceMapsId?.initStatus

            }
            resourcesEntity = workPlanResourcesRepo.save(resourcesEntity)
            sr.requestId = resourcesEntity.id

        } catch (e: Exception) {
            KotlinLogging.logger { }.trace(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            log.responseMessage = e.message
            log.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            log.transactionStatus = sr.serviceMapsId?.exceptionStatus!!
        }
        log.transactionCompletedDate = Timestamp.from(Instant.now())

        try {
            serviceRequestsRepository.save(sr)
            log = workflowTransactionsRepository.save(log)
        } catch (e: Exception) {
            KotlinLogging.logger { }.trace(e.message, e)
            KotlinLogging.logger { }.error(e.message)

        }

        return log.transactionStatus
    }

    fun addUserToWorkPlan(sr: ServiceRequestsEntity, workPlanUsersEntity: WorkPlanUsersEntity, workFlowId: Long): Int {
        var log = WorkflowTransactionsEntity()
        try {
            sr.payload = workPlanUsersEntity.toString()
            var usersEntity = workPlanUsersEntity

            with(log) {
                transactionDate = Date(Date().time)
                transactionStartDate = Timestamp.from(Instant.now())
                serviceRequests = sr
                retried = 0
                transactionStatus = sr.serviceMapsId?.initStatus!!
                createdBy = "${sr.id}_${sr.transactionReference}"
                createdOn = Timestamp.from(Instant.now())
                workflowRepo.findByIdOrNull(workFlowId)
                    ?.let { workflowsEntity -> methodId = workflowsEntity }
                transactionReference = "${sr.transactionReference}_${methodId?.methodName}"

            }

            with(usersEntity) {
                transactionDate = Date(Date().time)
                status = sr.serviceMapsId?.activeStatus
                createdBy = log.transactionReference
                createdOn = Timestamp.from(Instant.now())
                version = sr.serviceMapsId?.initStatus

            }
            usersEntity = workPlanUserRepo.save(usersEntity)
            sr.requestId = usersEntity.id

        } catch (e: Exception) {
            KotlinLogging.logger { }.trace(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            log.responseMessage = e.message
            log.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            log.transactionStatus = sr.serviceMapsId?.exceptionStatus!!
        }
        log.transactionCompletedDate = Timestamp.from(Instant.now())

        try {
            serviceRequestsRepository.save(sr)
            log = workflowTransactionsRepository.save(log)
        } catch (e: Exception) {
            KotlinLogging.logger { }.trace(e.message, e)
            KotlinLogging.logger { }.error(e.message)

        }

        return log.transactionStatus
    }

    fun saveWorkPlan(sr: ServiceRequestsEntity, workplanEntity: WorkplanEntity, workFlowId: Long): Int {
        var log = WorkflowTransactionsEntity()

        try {
            sr.payload = workplanEntity.toString()
            var workPlan = workplanEntity

            with(log) {
                transactionDate = Date(Date().time)
                transactionStartDate = Timestamp.from(Instant.now())
                serviceRequests = sr
                retried = 0
                transactionStatus = sr.serviceMapsId?.initStatus!!
                createdBy = "${sr.id}_${sr.transactionReference}"
                createdOn = Timestamp.from(Instant.now())
                workflowRepo.findByIdOrNull(workFlowId)
                    ?.let { workflowsEntity -> methodId = workflowsEntity }
                transactionReference = "${sr.transactionReference}_${methodId?.methodName}"

            }



            with(workPlan) {
                transactionDate = Date(Date().time)
                status = sr.serviceMapsId?.activeStatus
                createdBy = log.transactionReference
                createdOn = Timestamp.from(Instant.now())
                version = sr.serviceMapsId?.initStatus

            }
            workPlan = workPlanRepo.save(workPlan)

            workPlan.userId?.let { user ->
                var workPlanUsersEntity = WorkPlanUsersEntity()
                with(workPlanUsersEntity) {
                    transactionDate = Date(Date().time)
                    userId = user
                    workPlanId = workPlan
                    status = sr.serviceMapsId?.activeStatus
                    createdBy = log.transactionReference
                    createdOn = Timestamp.from(Instant.now())
                    version = sr.serviceMapsId?.initStatus
                    remarks = "Automatically added as the user that created the workplan"

                }
                workPlanUsersEntity = workPlanUserRepo.save(workPlanUsersEntity)
                KotlinLogging.logger { }.trace("Automatically added as the user[${workPlanUsersEntity.id}, ${workPlanUsersEntity.createdBy}] that created the workplan")

            }
            sr.requestId = workPlan.id

        } catch (e: Exception) {
            KotlinLogging.logger { }.trace(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            log.responseMessage = e.message
            log.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            log.transactionStatus = sr.serviceMapsId?.exceptionStatus!!
        }
        log.transactionCompletedDate = Timestamp.from(Instant.now())

        try {
            serviceRequestsRepository.save(sr)
            log = workflowTransactionsRepository.save(log)
        } catch (e: Exception) {
            KotlinLogging.logger { }.trace(e.message, e)
            KotlinLogging.logger { }.error(e.message)

        }


        return log.transactionStatus
    }
}

