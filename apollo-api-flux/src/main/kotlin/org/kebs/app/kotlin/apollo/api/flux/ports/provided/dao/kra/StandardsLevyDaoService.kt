package org.kebs.app.kotlin.apollo.api.flux.ports.provided.dao.kra

import akka.actor.ActorSystem
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.flux.ports.provided.dao.DaoService
import org.kebs.app.kotlin.apollo.common.dto.jobs.PenaltyJobDetails
import org.kebs.app.kotlin.apollo.common.dto.kra.request.ReceiveSL2PaymentRequest
import org.kebs.app.kotlin.apollo.common.dto.kra.response.RequestResult
import org.kebs.app.kotlin.apollo.common.exceptions.InvalidInputException
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.config.adaptor.akka.config.ActorSpringExtension
import org.kebs.app.kotlin.apollo.store.model.Sl2PaymentsDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.Sl2PaymentsHeaderEntity
import org.kebs.app.kotlin.apollo.store.model.WorkflowTransactionsEntity
import org.kebs.app.kotlin.apollo.store.repo.IBatchJobDetailsRepository
import org.kebs.app.kotlin.apollo.store.repo.ISl2PaymentsDetailsRepository
import org.kebs.app.kotlin.apollo.store.repo.ISl2PaymentsHeaderRepository
import org.kebs.app.kotlin.apollo.store.repo.IWorkflowTransactionsRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp
import java.time.Instant
import java.util.*


@Service
class StandardsLevyDaoService(
    private val daoService: DaoService,
    private val reactiveAuthenticationManager: ReactiveAuthenticationManager,
    private val logsRepo: IWorkflowTransactionsRepository,
    private val jobsRepo: IBatchJobDetailsRepository,
    private val extension: ActorSpringExtension,
    private val actorSystem: ActorSystem,
    private val headerRepository: ISl2PaymentsHeaderRepository,
    private val detailsRepository: ISl2PaymentsDetailsRepository
) {


    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun processSl2Payments(paymentRequest: ReceiveSL2PaymentRequest): RequestResult {
        val result = RequestResult()
        val log = daoService.createTransactionLog(0, daoService.generateTransactionReference())
        try {
            log.integrationRequest = daoService.mapper().writeValueAsString(paymentRequest)
            /**
             * Attempt to log in
             */
            try {
                reactiveAuthenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken(
                        paymentRequest.loginId,
                        paymentRequest.password
                    )
                ).block()

                validateCredentialsAndLogToDataStore(paymentRequest, log, result)


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
                            part.startsWith("9") -> result.responseCode = part
                            part.startsWith("NOK") -> result.status = part

                        }
                    }
                result.message = e.message
            } catch (e: Exception) {
                result.responseCode = "90001"
                result.message = e.message
                result.status = "NOK"
                KotlinLogging.logger { }.error(e.message, e)
                KotlinLogging.logger { }.debug(e.message, e)

            }

            log.transactionStatus = 20
            log.responseStatus = result.responseCode
            log.responseMessage = e.message
        }
        log.integrationResponse = daoService.mapper().writeValueAsString(result)
        logsRepo.save(log)


        return result
    }

    private fun validateCredentialsAndLogToDataStore(paymentRequest: ReceiveSL2PaymentRequest, log: WorkflowTransactionsEntity, result: RequestResult) {
        when (daoService.validateHash(paymentRequest)) {
            false -> throw InvalidInputException("90003,NOK, Hash code validation provided")
            true -> {
                /**
                 * DONE: Pick up the data validate and pass it on to KIMS for processing
                 */
                var header = Sl2PaymentsHeaderEntity()

                header.transactionDate = paymentRequest.transmissionDate
                header.requestHeaderEntryNo = paymentRequest.header?.entryNo
                header.requestHeaderKraPin = paymentRequest.header?.kraPin
                header.requestHeaderManufacturerName = paymentRequest.header?.manufacturerName
                header.requestHeaderPaymentSlipNo = paymentRequest.header?.paymentSlipNo
                header.requestHeaderPaymentSlipDate = paymentRequest.header?.paymentSlipDate
                header.requestHeaderPaymentType = paymentRequest.header?.paymentType
                header.requestHeaderTotalDeclAmt = paymentRequest.header?.totalDeclAmt
                header.requestHeaderTotalPenaltyAmt = paymentRequest.header?.totalPenaltyAmt
                header.requestHeaderTotalPaymentAmt = paymentRequest.header?.totalPaymentAmt
                header.requestHeaderBank = paymentRequest.header?.bank
                header.requestBankRefNo = paymentRequest.header?.bankRefNo
                header.requestHeaderTransmissionDate = paymentRequest.transmissionDate
                header.transactionDate = Date()
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
                    detail.periodTo = d.periodTo
                    detail.qtyManf = d.qtyManf
                    detail.exFactVal = d.exFactVal
                    detail.levyPaid = d.levyPaid
                    detail.transactionDate = Date()
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
                    detail.penaltyPaid = p.penaltyPaid
                    detail.transactionDate = Date()
                    detail.status = 0
                    detail.createdBy = paymentRequest.loginId
                    detail.createdOn = Timestamp.from(Instant.now())
                    detail.version = 1
                    detailsRepository.save(detail)
                }
                result.responseCode = "90000"
                result.message = "Successful Submission of Payment Details"
                result.status = "OK"

                log.transactionStatus = 30

                log.responseStatus = result.responseCode
                log.responseMessage = result.message


            }
        }
    }

    fun startJob(jobId: String): String {
        var result = ""
        try {


            jobId.toLongOrNull()
                ?.let { id ->
                    jobsRepo.findByIdOrNull(id)
                        ?.let { job ->
                            job.processingActorBean
                                ?.let { actorBean ->
                                    val actorRef = actorSystem.actorOf(extension.get(actorSystem).props(actorBean), actorBean)
                                    actorRef.tell(job, actorRef)
                                    result = "Job successfully submitted for processing"

                                }
                                ?: throw NullValueNotAllowedException("Processing Actor not found for job {id=${job.id}")


                        }

                        ?: throw InvalidInputException("Invalid jobId=$jobId no BatchJob found, aborting")
                }
                ?: throw InvalidInputException("Invalid jobId=$jobId, aborting")
        } catch (e: Exception) {
            throw InvalidInputException(e.message)
        }



        return result

    }

    fun startPenaltyJob(jobId: String): String {
        var result = ""
        try {


            jobId.toLongOrNull()
                ?.let { id ->
                    jobsRepo.findByIdOrNull(id)
                        ?.let { job ->
                            job.processingActorBean
                                ?.let { actorBean ->
                                    val detail = PenaltyJobDetails(job.id, actorBean)
                                    val actorRef = actorSystem.actorOf(extension.get(actorSystem).props(actorBean), actorBean)
                                    actorRef.tell(detail, actorRef)
                                    result = "Penalty Job successfully submitted for processing"

                                }
                                ?: throw NullValueNotAllowedException("Processing Actor not found for job {id=${job.id}")


                        }

                        ?: throw InvalidInputException("Invalid jobId=$jobId no BatchJob found, aborting")
                }
                ?: throw InvalidInputException("Invalid jobId=$jobId, aborting")
        } catch (e: Exception) {
            throw InvalidInputException(e.message)
        }



        return result

    }
}
