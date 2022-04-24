package org.kebs.app.kotlin.apollo.api.flux.adaptor.akka.actors.workers

import akka.actor.AbstractActor
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.jasypt.encryption.StringEncryptor
import org.kebs.app.kotlin.apollo.api.flux.ports.provided.dao.DaoService
import org.kebs.app.kotlin.apollo.api.flux.ports.provided.dao.base256Encode
import org.kebs.app.kotlin.apollo.common.dto.jobs.PenaltyJobDetails
import org.kebs.app.kotlin.apollo.common.dto.kra.request.*
import org.kebs.app.kotlin.apollo.common.dto.kra.response.RequestResult
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.config.adaptor.akka.interfaces.Actor
import org.kebs.app.kotlin.apollo.store.model.BatchJobDetails
import org.kebs.app.kotlin.apollo.store.model.IntegrationConfigurationEntity
import org.kebs.app.kotlin.apollo.store.model.StagingStandardsLevyManufacturerEntryNumber
import org.kebs.app.kotlin.apollo.store.model.StagingStandardsLevyManufacturerPenalty
import org.kebs.app.kotlin.apollo.store.repo.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant


@Actor
class SendManufacturerEntryNumbersToKraActor(
    private val entryNumberRepository: IStagingStandardsLevyManufacturerEntryNumberRepository,
    private val penaltyRepository: IStagingStandardsLevyManufacturerPenaltyRepository,
    private val configRepo: IIntegrationConfigurationRepository,
    private val daoService: DaoService,
    private val jasyptStringEncryptor: StringEncryptor,
    private val logsRepo: IWorkflowTransactionsRepository,
    private val jobsRepo: IBatchJobDetailsRepository,


    ) : AbstractActor() {

    fun retrievePendingManufacturerPenalties(details: PenaltyJobDetails) {
        try {
            jobsRepo.findByIdOrNull(details.id)
                ?.let { jobDetails ->
                    jobDetails.startStatus
                        ?.let { status ->
                            val totalRecords = penaltyRepository.findRecordCount(status)
                            jobDetails.integrationId
                                ?.let { integId ->
                                    configRepo.findByIdOrNull(integId)
                                        ?.let { config ->
                                            val pages = totalRecords.div(jobDetails.pageSize)
                                            repeat((jobDetails.initialPage..pages).count()) {
                                                val page = PageRequest.of(jobDetails.initialPage, jobDetails.pageSize)
                                                penaltyRepository.findAllByStatus(status, page)
                                                    ?.let { pendingEntries ->
                                                        processPendingPenalties(pendingEntries, jobDetails, config)

                                                    }
                                            }
                                        }
                                        ?: throw NullValueNotAllowedException("Integration Configuration id=$integId is not valid for job id=${jobDetails.id} is not set aborting")
                                }
                                ?: throw NullValueNotAllowedException("Integration Configuration not set up for job id=${jobDetails.id} is not set aborting")
                        }
                        ?: throw NullValueNotAllowedException("start status for job id=${jobDetails.id} is not set aborting")

                }
                ?: throw NullValueNotAllowedException("Invalid Job id=${details.id}")



            context().stop(self)

        } catch (e: Exception) {
            self().tell(e, self())
        }
    }

    fun retrievePendingManufacturers(jobDetails: BatchJobDetails) {
        try {
            jobDetails.startStatus
                ?.let { status ->
                    val totalRecords = entryNumberRepository.findRecordCount(status)
                    jobDetails.integrationId
                        ?.let { integId ->
                            configRepo.findByIdOrNull(integId)
                                ?.let { config ->
                                    val pages = totalRecords.div(jobDetails.pageSize)
                                    repeat((jobDetails.initialPage..pages).count()) {
                                        val page = PageRequest.of(jobDetails.initialPage, jobDetails.pageSize)
                                        entryNumberRepository.findAllByStatus(status, page)
                                            ?.let { pendingEntries ->
                                                processPendingEntries(pendingEntries, jobDetails, config)

                                            }
                                    }
                                }
                                ?: throw NullValueNotAllowedException("Integration Configuration id=$integId is not valid for job id=${jobDetails.id} is not set aborting")
                        }
                        ?: throw NullValueNotAllowedException("Integration Configuration not set up for job id=${jobDetails.id} is not set aborting")
                }
                ?: throw NullValueNotAllowedException("start status for job id=${jobDetails.id} is not set aborting")

            context().stop(self)

        } catch (e: Exception) {
            self().tell(e, self())
        }


    }

    private fun processPendingPenalties(
        pendingEntries: Page<StagingStandardsLevyManufacturerPenalty>,
        jobDetails: BatchJobDetails,
        config: IntegrationConfigurationEntity,
    ) {
        val transactionRef = daoService.generateTransactionReference()
        val log = daoService.createTransactionLog(0, transactionRef)
        try {

            val details = mutableListOf<PenaltyInfo>()
            val manufacturersProcessed = mutableListOf<Long?>()
            pendingEntries.forEach { entry ->

                val detail = PenaltyInfo()
                detail.entryNo = entry.manufacturerId.toString()
                detail.kraPin = entry.kraPin
                detail.manufacName = entry.manufacturerName
                detail.penaltyGenDate = entry.penaltyGenerationDate
                detail.penaltyOrderNo = entry.id.toString()
                detail.penaltyPayable = entry.penaltyPayable
                entry.periodFrom?.time?.let { detail.periodFrom = Date(it) }
                entry.periodTo?.time?.let { detail.periodTo = Date(it) }




                details.add(detail)
                manufacturersProcessed.add(entry.manufacturerId)
            }
            val recordCount = details.count()
            val timeNow = Timestamp.from(Instant.now())


            val transmissionDate = SimpleDateFormat(config.gsonDateFormt ?: "dd-MM-yyyy'T'HH:mm:ss").format(timeNow)
            /* DateTimeFormatter.ofPattern(config.gsonDateFormt).withLocale(Locale.getDefault()).withZone(ZoneId.systemDefault()).format(timeNow)*/
            val hash = hashingMechanisms(config, "$recordCount$transmissionDate")
            val header = RequestHeader(
                jasyptStringEncryptor.decrypt(config.username),
                jasyptStringEncryptor.decrypt(config.password),
                hash,
                recordCount.toString(),

                )
            header.transmissionDate = timeNow
            val entryRequest = PenaltyRequest()
            entryRequest.header = header
            entryRequest.details = details
            val finalUrl = "${config.url}${jobDetails.jobUri}"
            log.integrationRequest = daoService.mapper().writeValueAsString(entryRequest)
            when {
                details.count() > 0 -> {
                    runBlocking {

                        try {
                            val resp = daoService.getHttpResponseFromPostCall(finalUrl, null, entryRequest, config, null, null, log)
                            val data = daoService.processResponses<RequestResult>(resp, log, finalUrl, config)
                            log.responseStatus = data.second?.responseCode
                            log.responseMessage = "${data.second?.status}-${data.second?.message}"
                            KotlinLogging.logger { }.info(data.second?.message)
                            logsRepo.save(data.first)
                        } catch (e: Exception) {
                            log.responseMessage = e.message
                            log.responseStatus = config.exceptionCode
                            log.transactionCompletedDate = Timestamp.from(Instant.now())
                            logsRepo.save(log)
                            KotlinLogging.logger { }.error(e.message)
                            KotlinLogging.logger { }.debug(e.message, e)

                        }


                    }
                }
                else -> {
                    val result = RequestResult()
                    result.status = "NOK"
                    result.responseCode = "05"
                    result.message = "Count of manufacturers to be sent is empty aborting"
                    log.responseStatus = result.responseCode
                    log.integrationResponse = daoService.mapper().writeValueAsString(result)
                    log.transactionCompletedDate = Timestamp.from(Instant.now())
                    log.responseMessage = log.responseMessage?.let { "${result.message}|${log.responseMessage}" } ?: result.message
                    KotlinLogging.logger { }.debug("${result.message} vs ${log.responseMessage}")
                    logsRepo.save(log)
                }
            }
            daoService.postJobProcessingRecordsCleanUpPenalty(
                jobDetails,
                manufacturersProcessed,
                log,
                header.transmissionDate,
                config
            )


        } catch (e: Exception) {
            log.responseMessage = e.message
            log.responseStatus = config.exceptionCode
            log.transactionCompletedDate = Timestamp.from(Instant.now())
            logsRepo.save(log)
            throw  e

        }


    }

    private fun processPendingEntries(
        pendingEntries: Page<StagingStandardsLevyManufacturerEntryNumber>,
        jobDetails: BatchJobDetails,
        config: IntegrationConfigurationEntity,
    ) {
        val transactionRef = daoService.generateTransactionReference()
        val log = daoService.createTransactionLog(0, transactionRef)
        try {

            val details = mutableListOf<EntryDetails>()
            val manufacturersProcessed = mutableListOf<Long?>()
            pendingEntries.forEach { entry ->
                val strDate = SimpleDateFormat(jobDetails.standardDateFormat).format(entry.registrationDate)
                val detail = EntryDetails(entry.manufacturerId.toString(), entry.kraPin, entry.manufacturerName, strDate, entry.recordStatus)
                details.add(detail)
                manufacturersProcessed.add(entry.manufacturerId)
            }
            val recordCount = details.count()
            val timeNow = Timestamp.from(Instant.now())


            val transmissionDate = SimpleDateFormat(config.gsonDateFormt ?: "dd-MM-yyyy'T'HH:mm:ss").format(timeNow)
            /* DateTimeFormatter.ofPattern(config.gsonDateFormt).withLocale(Locale.getDefault()).withZone(ZoneId.systemDefault()).format(timeNow)*/
            //val hash = hashingMechanisms(config, "$recordCount$transmissionDate")
            val hash = hashingMechanisms(config, "$recordCount${jasyptStringEncryptor.decrypt(config.password)}${jasyptStringEncryptor.decrypt(config.username)}")
            val header = RequestHeader(
                jasyptStringEncryptor.decrypt(config.username),
                jasyptStringEncryptor.decrypt(config.password),
                hash,
                recordCount.toString(),

                )
            header.transmissionDate = timeNow
            val entryRequest = EntryRequest()
            entryRequest.header = header
            entryRequest.details = details
            val finalUrl = "${config.url}${jobDetails.jobUri}"
            log.integrationRequest = daoService.mapper().writeValueAsString(entryRequest)


            when {
                details.count() > 0 -> {
                    runBlocking {

                        try {
                            val resp = daoService.getHttpResponseFromPostCall(finalUrl, null, entryRequest, config, null, null, log)
                            val data = daoService.processResponses<RequestResult>(resp, log, finalUrl, config)
                            log.responseStatus = data.second?.responseCode
                            log.responseMessage = "${data.second?.status}-${data.second?.message}"
                            KotlinLogging.logger { }.info(data.second?.message)
                            logsRepo.save(data.first)
                        } catch (e: Exception) {
                            log.responseMessage = e.message
                            log.responseStatus = config.exceptionCode
                            log.transactionCompletedDate = Timestamp.from(Instant.now())
                            logsRepo.save(log)
                            KotlinLogging.logger { }.error(e.message)
                            KotlinLogging.logger { }.debug(e.message, e)

                        }


                    }
                }
                else -> {
                    val result = RequestResult()
                    result.status = "NOK"
                    result.responseCode = "05"
                    result.message = "Count of manufacturers to be sent is empty aborting"
                    log.responseStatus = result.responseCode
                    log.integrationResponse = daoService.mapper().writeValueAsString(result)
                    log.transactionCompletedDate = Timestamp.from(Instant.now())
                    log.responseMessage = log.responseMessage?.let { "${result.message}|${log.responseMessage}" } ?: result.message
                    KotlinLogging.logger { }.debug("${result.message} vs ${log.responseMessage}")
                    logsRepo.save(log)
                }
            }
            val cleanUp = daoService.postJobProcessingRecordsCleanUp(
                jobDetails,
                manufacturersProcessed,
                log,
                header.transmissionDate,
                config
            )

            KotlinLogging.logger { }.info("Post update result = $cleanUp")


        } catch (e: Exception) {
            log.responseMessage = e.message
            log.responseStatus = config.exceptionCode
            log.transactionCompletedDate = Timestamp.from(Instant.now())
            logsRepo.save(log)
            throw  e

        }
//        logsRepo.save(log)
    }

    private fun hashingMechanisms(config: IntegrationConfigurationEntity, records: String): String {
//        val str = "$records${jasyptStringEncryptor.decrypt(config.password)}${jasyptStringEncryptor.decrypt(config.username)}"
        return base256Encode(records, config)

    }


    override fun createReceive(): Receive {
        return receiveBuilder()
            .match(BatchJobDetails::class.java, this::retrievePendingManufacturers)
            .match(PenaltyJobDetails::class.java, this::retrievePendingManufacturerPenalties)
            .match(Exception::class.java) { e ->
                KotlinLogging.logger { }.error("Aborted reason=${e.message}")
                KotlinLogging.logger { }.debug(" Aborted reason=${e.message}", e)
                context().stop(self)
            }
            .build()


    }
}

private fun <E> MutableList<E>.add(element: String?) {

}
