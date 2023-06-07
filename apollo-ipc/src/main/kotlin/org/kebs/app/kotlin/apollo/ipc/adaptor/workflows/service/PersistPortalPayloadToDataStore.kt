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
import org.kebs.app.kotlin.apollo.ipc.ports.provided.TransactionReferenceGenerator
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.repo.*
import org.springframework.stereotype.Service
import java.sql.Date
import java.sql.Timestamp
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@Service
class PersistPortalPayloadToDataStore(
        private val userRepository: IUserRepository,
        private val contactDetailsRepository: IContactDetailsRepository,
        private val manufacturersRepo: IManufacturerRepository,
        private val manufacturerAddressesEntityRepo: IManufacturerAddressRepository,
        private val manufacturerContactsRepository: IManufacturerContactsRepository,
        private val workflowTransactionsRepository: IWorkflowTransactionsRepository,
        private val serviceRequestsRepository: IServiceRequestsRepository,
        private val workflowRepo: IServiceMapsWorkflowsRepository,
        private val brsLookupManufacturerDataRepo: IBrsLookupManufacturerDataRepository,
        private val brsLookupManufacturerPartnerRepo: IBrsLookupManufacturerPartnersRepository,
        private val sdlslSl1FormsRepository: ISdlSl1FormsRepository,
        private val verifcationTokensRepo: IUserVerificationTokensRepository

) {

    private fun tokenGenerator(s: ServiceMapsEntity): String {
        val result: String
        val generateTransactionReference = TransactionReferenceGenerator(s.transactionRefLength, s.secureRandom, s.messageDigestAlgorithm)
        result = generateTransactionReference.generateTransactionReference()
        return result

    }

    fun generateVerificationToken(sr: ServiceRequestsEntity, user: UsersEntity, workFlowId: Long): Int {
        var log = WorkflowTransactionsEntity()
        try {
            sr.serviceMapsId
                    ?.let { map ->
                        sr.payload = user.toString()

                        with(log) {
                            transactionDate = Date(Date().time)
                            transactionStartDate = Timestamp.from(Instant.now())
                            transactionReference = "${sr.transactionReference}_${sr.currentStage}"
                            serviceRequests = sr
                            retried = 0
                            transactionStatus = map.initStatus
                            createdBy = "${sr.id}_${sr.transactionReference}"
                            createdOn = Timestamp.from(Instant.now())

                        }


                        val token = sr.transactionReference ?: tokenGenerator(map)
                        sr.transactionReference = token
                        when {
                            sr.transactionReference.isNullOrBlank() -> throw Exception("Unable to generate UserVerificationToken")
                        }

                        sr.requestId = user.id

                        log.integrationResponse = token
//                    KotlinLogging.logger { }.info { "Token generation successful" }
                        log.responseMessage = "Token generation successful"
                        log.responseStatus = map.successStatusCode
                        log.transactionStatus = map.successStatus

                        sr.responseStatus = sr.serviceMapsId?.successStatusCode
                        sr.responseMessage = "Success ${sr.payload}"
                        sr.status = map.successStatus
                        sr.processingEndDate = Timestamp.from(Instant.now())

                    }


        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
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

    fun validateVerificationToken(sr: ServiceRequestsEntity, token: String, workFlowId: Long): Int {
        var log = WorkflowTransactionsEntity()
        try {
            sr.serviceMapsId
                    ?.let { map ->
                        sr.payload = token

                        with(log) {
                            transactionDate = Date(Date().time)
                            transactionStartDate = Timestamp.from(Instant.now())
                            serviceRequests = sr
                            retried = 0
                            transactionStatus = map.initStatus
                            createdBy = "${sr.id}_${sr.transactionReference}"
                            transactionReference = "${sr.transactionReference}_${sr.currentStage}"
                            createdOn = Timestamp.from(Instant.now())

                        }


                        verifcationTokensRepo.findByTokenAndStatus(token, map.initStatus)
                                ?.let { verificationToken ->
                                    log.integrationResponse = "${verificationToken.id}"
                                    verificationToken.tokenExpiryDate
                                            ?.let { expiry ->
                                                when {

                                                    expiry.after(Timestamp.from(Instant.now())) -> {
                                                        /**
                                                         * Send notification to log in and set up a password
                                                         * TODO: Confirm for email notifications
                                                         */
                                                        sr.messageParamList = verificationToken.userId?.email
                                                        sr.notificationType = 30

                                                        /**
                                                         * If user exists activate and enable
                                                         */
                                                        verificationToken.userId
                                                                ?.let { user ->
                                                                    with(user) {
                                                                        enabled = map.activeStatus
                                                                        status = map.activeStatus
                                                                        accountLocked = map.activeStatus
                                                                        approvedDate = Timestamp.from(Instant.now())
                                                                        userRepository.save(user)

                                                                    }

                                                                }


                                                    }
                                                    else -> {
                                                        verificationToken.status = map.failedStatus
                                                        verificationToken.lastModifiedOn = Timestamp.from(Instant.now())
                                                        verificationToken.lastModifiedBy = "Expired Verification Token Received"
                                                        verifcationTokensRepo.save(verificationToken)
                                                        throw Exception("Expired Verification Token Received")
                                                    }
                                                }

                                            } ?: throw Exception("Verification Token without a valid expiry found")


                                } ?: throw Exception("Verification Token not found")




                        sr.responseStatus = sr.serviceMapsId?.successStatusCode
                        sr.responseMessage = "Success ${sr.payload}"
                        sr.status = map.successStatus
                        sr.processingEndDate = Timestamp.from(Instant.now())

                        log.responseMessage = "Token generation successful"
                        log.responseStatus = map.successStatusCode
                        log.transactionStatus = map.successStatus

                    }


        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
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

    fun emailVerificationToken(sr: ServiceRequestsEntity, user: UsersEntity, workFlowId: Long): Int {
        var log = WorkflowTransactionsEntity()
        try {
            sr.serviceMapsId
                    ?.let { map ->
                        sr.payload = user.toString()

                        with(log) {
                            transactionDate = Date(Date().time)
                            transactionStartDate = Timestamp.from(Instant.now())
                            serviceRequests = sr
                            retried = 0
                            transactionStatus = map.initStatus
                            transactionReference = "${sr.transactionReference}_${sr.currentStage}"
                            createdBy = "${sr.id}_${sr.transactionReference}"
                            createdOn = Timestamp.from(Instant.now())

                        }


                        /**
                         * Save the Token and Send an email
                         */
                        var tokensEntity = UserVerificationTokensEntity()
                        with(tokensEntity) {
                            token = sr.transactionReference
                            userId = user
                            status = map.initStatus
                            createdBy = sr.transactionReference
                            createdOn = Timestamp.from(Instant.now())
                            map.tokenExpiryHours?.let { h -> tokenExpiryDate = Timestamp.from(Instant.now().plus(h, ChronoUnit.HOURS)) }
                                    ?: throw Exception("Missing Configuration: Hours to Token Expiry")
                            transactionDate = Date(Date().time)
                        }

                        tokensEntity = verifcationTokensRepo.save(tokensEntity)

                        /**
                         * TODO What should it be for email? also validate that the email has been sent out
                         */
                        sr.notificationType = 30
                        sr.messageParamList = user.email

                        sr.responseStatus = sr.serviceMapsId?.successStatusCode
                        sr.responseMessage = "Success ${sr.payload}"
                        sr.status = map.successStatus
                        sr.processingEndDate = Timestamp.from(Instant.now())
                        log.integrationResponse = tokensEntity.token
                        log.responseMessage = "Token generation successful"
                        log.responseStatus = map.successStatusCode
                        log.transactionStatus = map.successStatus

                    }


        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
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

    fun loadSl1Form(sr: ServiceRequestsEntity, user: UsersEntity, workFlowId: Long): Int {
        sr.serviceMapsId?.successStatus?.let { successStatus ->
            manufacturersRepo.findByUserIdAndStatus(user, successStatus)
                    ?.let { manufacturer ->
                        manufacturerAddressesEntityRepo.findByManufacturerIdAndStatusOrderByVersions(manufacturer, successStatus)
                                ?.let { addresses ->
                                    manufacturer.registrationNumber?.let { registrationNumber ->
                                        brsLookupManufacturerDataRepo.findFirstByRegistrationNumberAndStatusOrderById(registrationNumber, successStatus)
                                                ?.let { brsLookupData ->
                                                    brsLookupManufacturerPartnerRepo.findBrsLookupManufacturerPartnersEntitiesByManufacturerIdAndStatus(
                                                        brsLookupData.id,
                                                        successStatus
                                                    )
                                                            ?.let { directors ->
                                                                var partnerList = ""
                                                                directors.forEach {
                                                                    it.names?.let { names ->
                                                                        partnerList = when {
                                                                            partnerList.isBlank() -> names
                                                                            else -> partnerList + "\n" + names
                                                                        }
                                                                    }
                                                                }

                                                                var branches = ""
                                                                var hqAddress = ManufacturerAddressesEntity()
                                                                addresses.forEach {
                                                                    when (it.versions) {
                                                                        sr.serviceMapsId?.mainVersionId -> hqAddress = it
                                                                    }
                                                                    it.name?.let { address ->
                                                                        branches = when {
                                                                            branches.isBlank() -> address
                                                                            else -> branches + "\n" + address
                                                                        }

                                                                    }
                                                                }

                                                                var sdlSl1FormsEntity = SdlSl1FormsEntity()
                                                                with(sdlSl1FormsEntity) {
                                                                    manufacturerId = manufacturer
                                                                    businessRegistrationNumber = brsLookupData.registrationNumber
                                                                    businessName = brsLookupData.businessName
                                                                    plotNumber = hqAddress.plotNumber
                                                                    postalAddress = brsLookupData.postalAddress
                                                                    telephone = brsLookupData.phoneNumber
                                                                    adminLocation = hqAddress.streetName
                                                                    partners = partnerList
                                                                    locations = branches
                                                                    transactionDate = Date(Date().time)
                                                                    status = successStatus


                                                                }

                                                                sdlSl1FormsEntity = sdlslSl1FormsRepository.save(sdlSl1FormsEntity)
                                                                KotlinLogging.logger { }.trace("${sdlSl1FormsEntity.businessName}")

                                                            }
                                                }
                                    }

                                }


                    }
        }

        return 1
    }


//    fun saveManufacturerUser(sr: ServiceRequestsEntity, user: UsersEntity, workFlowId: Long): Int {
//
//        var log = WorkflowTransactionsEntity()
//        try {
//            sr.payload = user.toString()
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
//                    ?.let { workflowsEntity -> log.methodId = workflowsEntity }
//
//
//            /**
//             * Save the userID
//             */
//            var myUser = user
//            with(myUser) {
//                enabled = sr.serviceMapsId?.inactiveStatus!!
//                accountExpired = sr.serviceMapsId?.activeStatus!!
//                accountLocked = sr.serviceMapsId?.inactiveStatus!!
//                credentialsExpired = sr.serviceMapsId?.activeStatus!!
//                status = sr.serviceMapsId?.inactiveStatus!!
//                registrationDate = Date(Date().time)
//            }
//            myUser = userRepository.save(user)
//
//            /**
//             * Save the ContactDetailsEntity
//             */
//            user.userContactDetails.forEach { contact ->
//                with(contact) {
//                    status = sr.serviceMapsId?.inactiveStatus!!
//                    registrationDate = Date(Date().time)
//                    userId = myUser
//
//                }
//                contactDetailsRepository.save(contact)
//            }
//            var myManufacturer: ManufacturersEntity
//
//            user.manufacturers.forEach { manufacturer ->
//                with(manufacturer) {
//                    registrationDate = Date(Date().time)
//                    status = sr.serviceMapsId?.inactiveStatus!!
//                    userId = myUser
//                }
//                myManufacturer = manufacturersRepo.save(manufacturer)
//
//                myManufacturer.manufacturerAddresses.forEach { add ->
//                    with(add) {
//                        registrationDate = Date(Date().time)
//                        manufacturerId = myManufacturer
//
//                    }
//                    manufacturerAddressesEntityRepo.save(add)
//
//                }
//
//                myManufacturer.manufacturerContacts.forEach { cont ->
//                    with(cont) {
//                        registrationDate = Date(Date().time)
//                        manufacturerId = myManufacturer
//
//                    }
//                    manufacturerContactsRepository.save(cont)
//                }
//                sr.requestId = myManufacturer.id
//                sr.requestIdentifier = sr.serviceMapsId?.manufacturerUserTable
//                log.integrationResponse = user.toString()
//                KotlinLogging.logger { }.info { "User registration successfully saved" }
//                log.responseMessage = "User registration successfully saved"
//                log.responseStatus = sr.serviceMapsId?.successStatusCode
//                log.transactionStatus = sr.serviceMapsId?.successStatus!!
//
//
//            }
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
}