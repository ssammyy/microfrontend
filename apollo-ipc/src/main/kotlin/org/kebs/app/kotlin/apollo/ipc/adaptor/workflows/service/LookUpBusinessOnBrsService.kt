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

import com.google.gson.GsonBuilder
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.ipc.adaptor.workflows.integ.KtorHttpClient
import org.kebs.app.kotlin.apollo.ipc.dto.BrsLookUpResponse
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.repo.*
import org.springframework.context.ApplicationContext
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.sql.Date
import java.sql.Timestamp
import java.time.Instant

@Service
class LookUpBusinessOnBrsService(
    private val ktorHttpClient: KtorHttpClient,
    private val serviceMapsWorkflowsRepository: IServiceMapsWorkflowsRepository,
    private val integrationConfigurationRepository: IIntegrationConfigurationRepository,
    private val serviceRequestsRepository: IServiceRequestsRepository,
    private val workflowTransactionsRepository: IWorkflowTransactionsRepository,
    private val brsLookupManufacturerDataRepo: IBrsLookupManufacturerDataRepository,
    private val brsLookupManufacturerPartnerRepo: IBrsLookupManufacturerPartnersRepository,
    private val manufacturerRepository: IManufacturerRepository,
    val applicationContext: ApplicationContext

) {

    fun convertIntegrationResponseToJson(log: WorkflowTransactionsEntity, integ: IntegrationConfigurationEntity): Any? {

        var testModel: Any? = null

        if (!integ.gsonOutputBean.isNullOrBlank()) {
            val bean = applicationContext.getBean(integ.gsonOutputBean!!)
            val beanClazz = bean::class

            val gson = when {
                integ.gsonDateFormt.isNullOrBlank() -> GsonBuilder().create()
                else -> GsonBuilder().setDateFormat(integ.gsonDateFormt).create()
            }

            testModel = gson.fromJson(log.integrationResponse, beanClazz.java)
        }



        return testModel
    }

    fun lookUpRequest(sr: ServiceRequestsEntity, user: UsersEntity, workFlowId: Long): Int {
        var log = WorkflowTransactionsEntity()
        try {
            with(log) {
                transactionDate = Date(java.util.Date().time)
                transactionStartDate = Timestamp.from(Instant.now())
                serviceRequests = sr
                retried = 0
                transactionStatus = sr.serviceMapsId?.initStatus!!
                createdBy = "${sr.id}_${sr.transactionReference}"
                createdOn = Timestamp.from(Instant.now())
            }

            serviceMapsWorkflowsRepository.findByIdOrNull(workFlowId)
                ?.let { workFlow ->
                    log.methodId = workFlow
                    integrationConfigurationRepository.findByWorkflowId(workFlow.id)
                        ?.let { config ->
                            log.currentSequence = sr.currentStage
                            log.executionOrder = sr.currentSequence
                            log.transactionReference = "${sr.transactionReference}_${log.currentSequence}_${log.executionOrder}"
                            log.integrationId = config
                            val requestParams: MutableMap<String, String> = mutableMapOf()
                            manufacturerRepository.findByUserId(user)
                                    ?.let { manufacturer->
                                        manufacturer.registrationNumber?.let { requestParams.putIfAbsent(config.requestParams.split(config.requestParamsSeparator)[0], it) }
                                        log = runBlocking { ktorHttpClient.sendRequests(config, user, log, sr.serviceMapsId!!, requestParams, null)!! }

                                    }




                            when (log.transactionStatus) {
                                sr.serviceMapsId?.successStatus -> {
                                    try {

                                        val outputModel = convertIntegrationResponseToJson(log, config)
                                        val brsLookUpResponse: BrsLookUpResponse = outputModel as BrsLookUpResponse
                                        val m = manufacturerRepository.findByIdOrNull(sr.requestId)


                                        var brsLookupManufacturerDataEntity: BrsLookupManufacturerDataEntity
                                        brsLookUpResponse.records?.forEach { record ->
                                            brsLookupManufacturerDataEntity = BrsLookupManufacturerDataEntity()
                                            with(brsLookupManufacturerDataEntity) {
                                                manufacturerId = m
                                                transactionDate = Date(java.util.Date().time)
                                                registrationNumber = record.registrationNumber
                                                registrationDate = record.registrationDate?.time?.let { Date(it) }
                                                postalAddress = record.postalAddress
                                                physicalAddress = record.physicalAddress
                                                phoneNumber = record.phoneNumber
                                                brsId = record.id
                                                email = record.email
                                                kraPin = record.kraPin
                                                businessName = record.businessName
                                                brsStatus = record.status
                                                status = sr.serviceMapsId?.successStatus
                                                createdBy = log.transactionReference
                                                createdOn = Timestamp.from(Instant.now())
                                            }
                                            var brsLookupManufacturerPartnersEntity: BrsLookupManufacturerPartnersEntity
                                            brsLookupManufacturerDataEntity = brsLookupManufacturerDataRepo.save(brsLookupManufacturerDataEntity)
                                            record.partners.forEach { partner ->
                                                KotlinLogging.logger { }.trace { "working on ${partner.name}" }
                                                brsLookupManufacturerPartnersEntity = BrsLookupManufacturerPartnersEntity()
                                                with(brsLookupManufacturerPartnersEntity) {
                                                    manufacturerId = brsLookupManufacturerDataEntity
                                                    transactionDate = Date(java.util.Date().time)
                                                    names = partner.name
                                                    idType = partner.idType
                                                    idNumber = partner.idNumber
                                                    status = sr.serviceMapsId?.successStatus
                                                    createdBy = log.transactionReference
                                                    createdOn = Timestamp.from(Instant.now())
                                                }
                                                brsLookupManufacturerPartnersEntity = brsLookupManufacturerPartnerRepo.save(brsLookupManufacturerPartnersEntity)

                                            }


                                        }


                                    } catch (e: Exception) {
                                        KotlinLogging.logger { }.error { e }
                                        log.responseMessage = "${log.responseMessage}: unable to save record ${e.message}"
                                        //                                        log.responseStatus = sr.serviceMapsId?.exceptionStatusCode
                                        //                                        log.transactionStatus = sr.serviceMapsId?.exceptionStatus!!

                                    }
                                }
                                else -> {
                                    KotlinLogging.logger { }.error { "BrsLookup records do not exist for failed records" }

                                }
                            }
                        }

                }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error { e }
            log.responseMessage = e.message
            log.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            log.transactionStatus = sr.serviceMapsId?.exceptionStatus!!
        }
        log.transactionCompletedDate = Timestamp.from(Instant.now())
        try {
            serviceRequestsRepository.save(sr)
            workflowTransactionsRepository.save(log)
        } catch (e: Exception) {
            KotlinLogging.logger { }.error { e }

        }




        return log.transactionStatus
    }
}