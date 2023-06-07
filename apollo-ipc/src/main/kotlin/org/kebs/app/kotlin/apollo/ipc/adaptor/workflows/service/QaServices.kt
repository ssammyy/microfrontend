///*
// *
// *  *
// *  *
// *  *  *    Copyright (c) ${YEAR}.   BSK Global Technologies
// *  *  *
// *  *  *    Licensed under the Apache License, Version 2.0 (the "License");
// *  *  *    you may not use this file except in compliance with the License.
// *  *  *    You may obtain a copy of the License at
// *  *  *
// *  *  *       http://www.apache.org/licenses/LICENSE-2.0
// *  *  *
// *  *  *    Unless required by applicable law or agreed to in writing, software
// *  *  *    distributed under the License is distributed on an "AS IS" BASIS,
// *  *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// *  *  *   See the License for the specific language governing permissions and
// *  *  *   limitations under the License.
// *  *
// *
// */
//
//package org.kebs.app.kotlin.apollo.ipc.adaptor.workflows.service
//
//import mu.KotlinLogging
//import org.kebs.app.kotlin.apollo.store.model.*
//import org.kebs.app.kotlin.apollo.store.repo.*
//import org.springframework.data.repository.findByIdOrNull
//import org.springframework.stereotype.Service
//import java.sql.Date
//import java.sql.Timestamp
//import java.time.Instant
//import java.util.*
//
//@Service
//class QaServices(
//    private val iPermitRepository: IPermitRepository,
////        private val ilevyRateRepository: ILevyRateRepository,
//    private val manufacturersRepo: IManufacturerRepository,
//    private val manufacturerAddressesEntityRepo: IManufacturerAddressRepository,
////        private val monthlyTurnOversRepo: IMonthlyTurnOversRepository,
////        private val manufacturerTurnoverRepository: IManufacturerTurnoverRepository,
//    private val manufacturerContactsRepository: IManufacturerContactsRepository,
//    private val workflowTransactionsRepository: IWorkflowTransactionsRepository,
//    private val serviceRequestsRepository: IServiceRequestsRepository,
//    private val workflowRepo: IServiceMapsWorkflowsRepository,
//    private val brsLookupManufacturerDataRepo: IBrsLookupManufacturerDataRepository,
//    private val brsLookupManufacturerPartnerRepo: IBrsLookupManufacturerPartnersRepository,
//    private val sdlslSl1FormsRepository: ISdlSl1FormsRepository
//
//) {
//
//    fun loadSl1Form(sr: ServiceRequestsEntity, user: UsersEntity, workFlowId: Long): Int {
//        sr.serviceMapsId?.successStatus?.let { successStatus ->
//            manufacturersRepo.findByUserIdAndStatus(user, successStatus)
//                ?.let { manufacturer ->
//                    manufacturerAddressesEntityRepo.findByManufacturerIdAndStatusOrderByVersions(manufacturer, successStatus)
//                        ?.let { addresses ->
//                            manufacturer.registrationNumber?.let { registrationNumber ->
//                                brsLookupManufacturerDataRepo.findFirstByRegistrationNumberAndStatusOrderById(registrationNumber, successStatus)
//                                    ?.let { brsLookupData ->
//                                        brsLookupManufacturerPartnerRepo.findBrsLookupManufacturerPartnersEntitiesByManufacturerIdAndStatus(brsLookupData, successStatus)
//                                            ?.let { directors ->
//                                                var partnerList = ""
//                                                directors.forEach {
//                                                    it.names?.let { names ->
//                                                        partnerList = when {
//                                                            partnerList.isBlank() -> names
//                                                            else -> partnerList + "\n" + names
//                                                        }
//                                                    }
//                                                }
//
//                                                var branches = ""
//                                                var hqAddress = ManufacturerAddressesEntity()
//                                                addresses.forEach {
//                                                    when (it.versions) {
//                                                        sr.serviceMapsId?.mainVersionId -> hqAddress = it
//                                                    }
//                                                    it.name?.let { address ->
//                                                        branches = when {
//                                                            branches.isBlank() -> address
//                                                            else -> branches + "\n" + address
//                                                        }
//
//                                                    }
//                                                }
//
//                                                var sdlSl1FormsEntity = SdlSl1FormsEntity()
//                                                with(sdlSl1FormsEntity) {
//                                                    manufacturerId = manufacturer
//                                                    businessRegistrationNumber = brsLookupData.registrationNumber
//                                                    businessName = brsLookupData.businessName
//                                                    plotNumber = hqAddress.plotNumber
//                                                    postalAddress = brsLookupData.postalAddress
//                                                    telephone = brsLookupData.phoneNumber
//                                                    adminLocation = hqAddress.streetName
//                                                    partners = partnerList
//                                                    locations = branches
//                                                    transactionDate = Date(Date().time)
//                                                    status = successStatus
//
//
//                                                }
//
//                                                sdlSl1FormsEntity = sdlslSl1FormsRepository.save(sdlSl1FormsEntity)
//                                                KotlinLogging.logger { }.trace("${this::loadSl1Form.name} saved as [id=${sdlSl1FormsEntity.id}]")
//
//                                            }
//                                    }
//                            }
//
//                        }
//
//
//                }
//        }
//
//        return 0
//    }
//
////    fun calculatePayment(sr: ServiceRequestsEntity, permit: PermitApplicationEntity, workFlowId: Long): Int{
////
////        val manufactureId = permit?.manufacturerId
//
////        manufactureId?.let { manufacturesId ->
//////            manufacturerTurnoverRepository.findFirstByManufactureId(manufacturesId)
//////                    ?.let {manufacturerTurnoverEntity ->
//////                        val turnover = manufacturerTurnoverEntity.monthlyTurnOverAmount
//////                        turnover?.let {
//////                            ilevyRateRepository.findBetweenLowerLimitAndUpperLimit(it)
//////                                    ?.let { rateCard->
////////                                        var amountToPay = rateCard.variableAmountToPay?.multiply(permit?.brand?.toDouble()?.let { it1 -> BigDecimal.valueOf(it1) })?.multiply(permit?.site)?.let { it2 -> rateCard.fixedAmountToPay?.plus(it2) }
////////                                        KotlinLogging.logger(){"Amount to pay is" + amountToPay}
//////
//////                                    }
//////
//////                        }
////
////
////                    }
////        }
//
//
//    fun savePermitApplicationDetails(sr: ServiceRequestsEntity, permit: PermitApplicationEntity, workFlowId: Long): Int {
//
//        var log = WorkflowTransactionsEntity()
//        try {
//            sr.payload = permit.toString()
//
//            with(log) {
//                transactionDate = Date(Date().time)
//                transactionStartDate = Timestamp.from(Instant.now())
//                serviceRequests = sr
//                retried = 0
//                transactionStatus = sr.serviceMapsId?.initStatus!!
//                createdBy = "${sr.id}_${sr.transactionReference}"
//                createdOn = Timestamp.from(Instant.now())
//
//            }
//            workflowRepo.findByIdOrNull(workFlowId)
//                ?.let { workflowsEntity -> log.methodId = workflowsEntity }
//
//
//            /**
//             * Save the permitID
//             */
//            var myPermit = permit
//            with(myPermit) {
//                status = sr.serviceMapsId?.inactiveStatus!!
//                dateCreated = Date(Date().time)
//            }
//            myPermit = iPermitRepository.save(permit)
//
//            sr.requestId = myPermit.id
//            sr.requestIdentifier = sr.serviceMapsId?.manufacturerUserTable
//            log.integrationResponse = myPermit.toString()
//            KotlinLogging.logger { }.info { "Permit application successfully saved" }
//            log.responseMessage = "Permit application successfully saved"
//            log.responseStatus = sr.serviceMapsId?.successStatusCode
//            log.transactionStatus = sr.serviceMapsId?.successStatus!!
//
//
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.error { e }
//            log.responseMessage = e.message
//            log.responseStatus = sr.serviceMapsId?.exceptionStatusCode
//            log.transactionStatus = sr.serviceMapsId?.exceptionStatus!!
//        }
//        log.transactionCompletedDate = Timestamp.from(Instant.now())
//
//        try {
//            serviceRequestsRepository.save(sr)
//            log = workflowTransactionsRepository.save(log)
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.error { e }
//
//        }
//
//
//
//
//
//        return log.transactionStatus
//
//    }
//}