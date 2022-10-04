package org.kebs.app.kotlin.apollo.api.ports.provided.dao.kra

import akka.actor.ActorSystem
import com.google.gson.Gson
import mu.KotlinLogging
import org.jasypt.encryption.StringEncryptor
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DaoFluxService
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DaoService
import org.kebs.app.kotlin.apollo.api.security.service.CustomAuthenticationProvider
import org.kebs.app.kotlin.apollo.common.dto.kra.request.*
import org.kebs.app.kotlin.apollo.common.dto.kra.response.PinValidationResponse
import org.kebs.app.kotlin.apollo.common.dto.kra.response.PinValidationResponseResult
import org.kebs.app.kotlin.apollo.common.dto.kra.response.RequestFinalResults
import org.kebs.app.kotlin.apollo.common.dto.kra.response.RequestResult
import org.kebs.app.kotlin.apollo.common.exceptions.InvalidInputException
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.config.adaptor.akka.config.ActorSpringExtension
import org.kebs.app.kotlin.apollo.store.model.KraPinValidations
import org.kebs.app.kotlin.apollo.store.model.Sl2PaymentsDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.Sl2PaymentsHeaderEntity
import org.kebs.app.kotlin.apollo.store.model.WorkflowTransactionsEntity
import org.kebs.app.kotlin.apollo.store.repo.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate

/**
 *
 * <H1>
 * @link StandardsLevyDaoService
 * </H1>
 *
 * a Data Access class For sending API data to the backend and/or invoking subsequent calls as required
 *  @author Kenneth KZM. Muhia
 *  @since 1.0.0
 * Constructor values are to inject spring beans
 * <br>
 *  @see DaoService
 *  @see StringEncryptor
 *  @see ReactiveAuthenticationManager
 *  @see IWorkflowTransactionsRepository
 *  @see IKraPinValidationsRepository
 *  @see IIntegrationConfigurationRepository
 *  @see ActorSpringExtension
 *  @see ActorSystem
 *  @see ISl2PaymentsHeaderRepository
 *  @see ISl2PaymentsDetailsRepository
 *
 *  The class implements @see AbstractValidationHandler
 */
@Service
class StandardsLevyDaoService(
    private val daoService: DaoFluxService,
    private val jasyptStringEncryptor: StringEncryptor,
    private val reactiveAuthenticationManager: CustomAuthenticationProvider,
    private val logsRepo: IWorkflowTransactionsRepository,
    private val kraPinValidationsRepo: IKraPinValidationsRepository,
    private val jobsRepo: IBatchJobDetailsRepository,
    private val integRepo: IIntegrationConfigurationRepository,
    private val commonDaoServices: CommonDaoServices,

    private val extension: ActorSpringExtension,
//    private val actorSystem: ActorSystem,
    private val headerRepository: ISl2PaymentsHeaderRepository,
    private val detailsRepository: ISl2PaymentsDetailsRepository
) {

    /**
     * Function which saves SL2 payments to the KEBS DB
     *
     * @param paymentRequest
     * <pre>
     * Needs to be transactional as we save data to multiple tables
     * </pre>
     * @return RequestResult
     */
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun processSl2Payments(paymentRequest: Request): RequestFinalResults {
        val result = RequestFinalResults()
        var requestResult = RequestResult()
        val log = daoService.createTransactionLog(0, daoService.generateTransactionReference())
        try {
            log.integrationRequest = daoService.mapper().writeValueAsString(paymentRequest)
            /**
             * Attempt to log in
             */
            try {

//                val token: usernamePasswordToken = checkVariousLoginOptions(request) // (1)


                reactiveAuthenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken(
                        paymentRequest.loginId,
                        paymentRequest.password
                    )
                ).isAuthenticated

                requestResult = validateCredentialsAndLogToDataStore(paymentRequest, log)


            } catch (e: DisabledException) {
                throw InvalidInputException("90004,NOK, Invalid account status")
            } catch (e: BadCredentialsException) {
                throw InvalidInputException("90004,NOK, Invalid credentials")
            } catch (e: Exception) {
                throw InvalidInputException("90004,NOK, ${e.message}")

            }


        } catch (e: Exception) {
            try {
                e.message
                    ?.split(",")
                    ?.forEach { part ->
                        when {
                            part.startsWith("9") -> requestResult.responseCode = part
                            part.startsWith("NOK") -> requestResult.status = part

                        }
                    }
                requestResult.message = e.message
            } catch (e: Exception) {
                requestResult.responseCode = "90001"
                requestResult.message = e.message
                requestResult.status = "NOK"
                KotlinLogging.logger { }.error(e.message, e)
                KotlinLogging.logger { }.debug(e.message, e)

            }

            log.transactionStatus = 20
            log.responseStatus = requestResult.responseCode
            log.responseMessage = e.message
        }
        result.requestResult = requestResult
        log.integrationResponse = daoService.mapper().writeValueAsString(result)
        logsRepo.save(log)


        return result
    }

    /**
     * Function to validate ingress hash and validate
     *
     * @param paymentRequest as received on the API
     * @param log to track processing and persist final status as well as log intermidiary results
     * @param result eventaul response to be supplied as API response
     * <br>
     * Needs to be transactional as we save data to multiple tables
     *
     * @return RequestResult
     */
    private fun validateCredentialsAndLogToDataStore(paymentRequest: Request, log: WorkflowTransactionsEntity): RequestResult {
        when (daoService.validateHash(paymentRequest)) {
            false -> throw InvalidInputException("90003,NOK, Hash code validation provided")
            true -> {
                /**
                 * DONE: Pick up the data validate and pass it on to KIMS for processing
                 */

                var header = Sl2PaymentsHeaderEntity()

                header.requestHeaderEntryNo = paymentRequest.header?.entryNo
                header.requestHeaderKraPin = paymentRequest.header?.kraPin
                header.requestHeaderManufacturerName = paymentRequest.header?.manufacturerName
                header.requestHeaderPaymentSlipNo = paymentRequest.header?.paymentSlipNo
                header.requestHeaderPaymentSlipDate = paymentRequest.header?.paymentSlipDate
                header.requestHeaderPaymentType = paymentRequest.header?.paymentType
                header.requestHeaderTotalDeclAmt = paymentRequest.header?.totalDeclAmt?.toBigDecimal()
                header.requestHeaderTotalPenaltyAmt = paymentRequest.header?.totalPenaltyAmt?.toBigDecimal()
                header.requestHeaderTotalPaymentAmt = paymentRequest.header?.totalPaymentAmt?.toBigDecimal()
                header.requestHeaderBank = paymentRequest.header?.bank
                header.requestBankRefNo = paymentRequest.header?.bankRefNo
                header.requestHeaderTransmissionDate = paymentRequest.transmissionDate
//                header.headerTransmissionDate = paymentRequest.header?.paymentDate
                header.transactionDate = paymentRequest.header?.paymentDate
                header.status = 0
                header.createdBy = paymentRequest.loginId
                header.createdOn = Timestamp.from(Instant.now())
                header.varField1 = log.transactionReference
                header.version = 1
                header = headerRepository.save(header)


                paymentRequest.details?.declaration?.forEach { d ->
                    val detail = Sl2PaymentsDetailsEntity()
                    detail.headerId = header.id
                    detail.transactionType = "DECLARATION"
                    detail.commodityType = d.commodityType
                    detail.periodFrom = d.periodFrom
                    detail.periodTo = d.periodTo
                    detail.qtyManf = d.qtyManf?.toBigDecimal()
                    detail.exFactVal = d.exFactVal?.toBigDecimal()
                    detail.levyPaid = d.levyPaid?.toBigDecimal()
                    detail.transactionDate = commonDaoServices.getCurrentDate()
                    detail.status = 0
                    detail.createdBy = paymentRequest.loginId
                    detail.createdOn = Timestamp.from(Instant.now())
                    detail.version = 1
                    detailsRepository.save(detail)
                }
                paymentRequest.details?.penalty?.forEach { p ->
                    val detail = Sl2PaymentsDetailsEntity()
                    detail.headerId = header.id
                    detail.transactionType = "PENALTY"
                    detail.penaltyOrderNo = p.penaltyOrderNo
                    detail.periodFrom = p.periodFrom
                    detail.periodTo = p.periodTo
                    detail.penaltyPaid = p.penaltyPaid?.toBigDecimal()
                    detail.transactionDate = commonDaoServices.getCurrentDate()
                    detail.status = 0
                    detail.createdBy = paymentRequest.loginId
                    detail.createdOn = Timestamp.from(Instant.now())
                    detail.version = 1
                    detailsRepository.save(detail)
                }

                val requestResult = RequestResult()
                with(requestResult){
                    responseCode = "90000"
                    message = "Successful Submission of Payment Details"
                    status = "OK"
                }

                log.transactionStatus = 30

                log.responseStatus = requestResult.responseCode
                log.responseMessage = requestResult.message

                return requestResult

            }
        }
    }

    /**
     * Function to initialize the job which processes the SL entries to be submitted to KRA using an akka
     * @see ActorSystem
     *
     * @param jobId jobId to single out this job
     *
     * @return RequestResult
     */
//    fun startJob(jobId: String): String {
//        var result = ""
//        try {
//
//
//            jobId.toLongOrNull()
//                ?.let { id ->
//                    jobsRepo.findByIdOrNull(id)
//                        ?.let { job ->
//                            job.processingActorBean
//                                ?.let { actorBean ->
//                                    val actorRef = actorSystem.actorOf(extension.get(actorSystem).props(actorBean), actorBean)
//                                    actorRef.tell(job, actorRef)
//                                    result = "Job successfully submitted for processing"
//
//                                }
//                                ?: throw NullValueNotAllowedException("Processing Actor not found for job {id=${job.id}")
//
//
//                        }
//
//                        ?: throw InvalidInputException("Invalid jobId=$jobId no BatchJob found, aborting")
//                }
//                ?: throw InvalidInputException("Invalid jobId=$jobId, aborting")
//        } catch (e: Exception) {
//            throw InvalidInputException(e.message)
//        }
//
//
//
//        return result
//
//    }

    /**
     * Function to initialize the job which processes the SL penalties entries to be submitted to KRA using an akka
     * @see ActorSystem
     *
     * @param jobId jobId to single out this job
     *
     * @return RequestResult
     */
//    fun startPenaltyJob(jobId: String): String {
//        var result = ""
//        try {
//
//
//            jobId.toLongOrNull()
//                ?.let { id ->
//                    jobsRepo.findByIdOrNull(id)
//                        ?.let { job ->
//                            job.processingActorBean
//                                ?.let { actorBean ->
//                                    val detail = PenaltyJobDetails(job.id, actorBean)
//                                    val actorRef = actorSystem.actorOf(extension.get(actorSystem).props(actorBean), actorBean)
//                                    actorRef.tell(detail, actorRef)
//                                    result = "Penalty Job successfully submitted for processing"
//
//                                }
//                                ?: throw NullValueNotAllowedException("Processing Actor not found for job {id=${job.id}")
//
//
//                        }
//
//                        ?: throw InvalidInputException("Invalid jobId=$jobId no BatchJob found, aborting")
//                }
//                ?: throw InvalidInputException("Invalid jobId=$jobId, aborting")
//        } catch (e: Exception) {
//            throw InvalidInputException(e.message)
//        }
//
//
//
//        return result
//
//    }

    /**
     * Function to validate the Pin request
     *
     *
     * @param body as submitted on the API
     *
     * @return RequestResult
     */
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    suspend fun processPinValidation(body: PinValidationWebRequest): PinValidationResponse {
        val transactionRef = daoService.generateTransactionReference()
        var log = daoService.createTransactionLog(0, transactionRef)
        val result = PinValidationResponseResult()
        var response = PinValidationResponse()
        try {
            jobsRepo.findByIdOrNull(body.integration)
                ?.let { job ->
                    integRepo.findByIdOrNull(job.integrationId)
                        ?.let { config ->
                            kraPinValidationsRepo.findFirstByKraPinAndStatusOrderByIdDesc(body.kraPin, job.endSuccessStatus)
                                ?.let {
                                    /**
                                     * Use existing valid Database record
                                     */
                                    try {

                                        response = daoService.xmlMapper().readValue(it.responsePayload, PinValidationResponse::class.java)
                                        log.integrationResponse = it.responsePayload
                                        log.integrationRequest = "reusing correct record"
                                        log.responseStatus = response.pinValidationResponseResult?.status
                                        log.responseMessage = "${response.pinValidationResponseResult?.message}-${response.pinValidationResponseResult?.message}"
                                        log.transactionStatus = job.endSuccessStatus ?: 30


                                    } catch (e: Exception) {
                                        result.message = e.message
                                        result.responseCode = "99"
                                        result.status = "500"
                                        log.responseMessage = result.message
                                        log.responseStatus = result.status
                                        log.transactionStatus = job.endFailureStatus ?: 25

                                    }

                                }
                                ?: run {
                                    val req = PinValidationRequest()
                                    req.kraPin = body.kraPin
                                    req.loginId = jasyptStringEncryptor.decrypt(config.username)
                                    req.password = jasyptStringEncryptor.decrypt(config.password)
                                    val finalUrl = "${config.url}${job.jobUri}"
                                    log.integrationRequest = daoService.xmlMapper().writeValueAsString(req)
                                    val resp = daoService.getHttpResponseFromPostCall(finalUrl, null, req, config, null, null, log)
                                    val data = daoService.processResponses<PinValidationResponse>(resp, log, finalUrl, config)
                                    log = data.first
                                    log.responseStatus = data.second?.pinValidationResponseResult?.status
                                    log.responseMessage = "${data.second?.pinValidationResponseResult?.message}-${data.second?.pinValidationResponseResult?.message}"

                                    log = logsRepo.save(log)
                                    when (data.second?.pinValidationResponseResult?.responseCode) {
                                        config.successCode -> {
                                            /**
                                             * Save the record to the DB to be used for subsequent results
                                             */

                                            val kraPinValidations = KraPinValidations()
                                            kraPinValidations.requestReference = log.transactionReference
                                            kraPinValidations.responseCode = data.second?.pinValidationResponseResult?.responseCode
                                            kraPinValidations.responseMessage = data.second?.pinValidationResponseResult?.message
                                            kraPinValidations.responseStatus = data.second?.pinValidationResponseResult?.status
                                            data.second?.let { kraPinValidations.responsePayload = daoService.mapper().writeValueAsString(it) }
                                            kraPinValidations.status = job.endSuccessStatus
                                            kraPinValidations.transactionDate = LocalDate.now()
                                            kraPinValidations.transmissionDate = log.transactionStartDate
                                            kraPinValidations.createdBy = log.transactionReference
                                            kraPinValidations.createdOn = Timestamp.from(Instant.now())
                                            kraPinValidationsRepo.save(kraPinValidations)
                                            log.transactionStatus = job.endSuccessStatus ?: 30
                                            log = logsRepo.save(log)

                                        }
                                        else -> {
                                            log.transactionStatus = job.endFailureStatus ?: 20
                                            log = logsRepo.save(log)
                                        }
                                    }
                                }


                        }
                        ?: throw NullValueNotAllowedException("Invalid Integration with id= ${job.integrationId} for for job {id=${body.integration}")


                }
                ?: throw NullValueNotAllowedException("Processing Job not found for job {id=${body.integration}")

        } catch (e: Exception) {
            result.message = e.message
            result.responseCode = "99"
            result.status = "500"


        }

        log = logsRepo.save(log)
        response.pinValidationResponseResult = result




        return response

    }
}
