package org.kebs.app.kotlin.apollo.api.flux.ports.provided.dao.kappa

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.flux.ports.provided.dao.DaoService
import org.kebs.app.kotlin.apollo.common.dto.kappa.requests.NotificationRequestValue
import org.kebs.app.kotlin.apollo.common.dto.kappa.requests.ValidationRequestValue
import org.kebs.app.kotlin.apollo.common.dto.kappa.response.NotificationResponseValue
import org.kebs.app.kotlin.apollo.common.dto.kappa.response.ValidationResponseValues
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.store.repo.IInvoiceResponceStatusEntityRepo
import org.kebs.app.kotlin.apollo.store.repo.IStagingPaymentReconciliationRepo
import org.kebs.app.kotlin.apollo.store.repo.IWorkflowTransactionsRepository
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.Instant

@Service
class KappaPaymentsDaoService(
        private val stagingRepo: IStagingPaymentReconciliationRepo,
        private val logsRepo: IWorkflowTransactionsRepository,
        private val daoService: DaoService,
        private val invoiceResponceStatusEntityRepo: IInvoiceResponceStatusEntityRepo
) {
    val invoiceStatus = invoiceResponceStatusEntityRepo.findByStatus(1)
    suspend fun processValidateInvoice(value: ValidationRequestValue): ValidationResponseValues {
        val result = ValidationResponseValues()

        val log = daoService.createTransactionLog(0, daoService.generateTransactionReference())
        try {
            stagingRepo.findByReferenceCode(value.billReferenceCode)
                    ?.let { record ->
                        log.integrationRequest = daoService.mapper().writeValueAsString(value)
                        result.accountName = record.customerName
                        result.accountNumber = record.accountNumber
                        result.additionalInfo = record.additionalInformation
                        result.billReferenceCode = record.referenceCode
                        result.currency = record.currency
                        result.responseDate = Timestamp.from(Instant.now())
                        result.statusCode = invoiceStatus.success
                        result.statusDescription = record.statusDescription
                        result.totalAmount = record.invoiceAmount
                        log.integrationResponse = daoService.mapper().writeValueAsString(result)
                        log.responseStatus = invoiceStatus.success
                        log.responseMessage = result.billReferenceCode
                        log.transactionCompletedDate = Timestamp.from(Instant.now())
                        logsRepo.save(log)
                        return result

                    }
                    ?: run {
                        result.statusCode = invoiceStatus.notFound
                        result.responseDate = Timestamp.from(Instant.now())
                        result.accountName = ""
                        result.accountNumber = ""
                        result.additionalInfo = ""
                        result.billReferenceCode = ""
                        result.currency = ""
                        result.statusDescription = ""
                        result.totalAmount = (0).toBigDecimal()
                        return result
                    }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            log.responseStatus = invoiceStatus.error
            log.responseMessage = e.message


            result.responseDate = Timestamp.from(Instant.now())
            result.statusCode = invoiceStatus.error
            result.statusDescription = log.responseMessage
            log.integrationResponse = daoService.mapper().writeValueAsString(result)
            log.transactionCompletedDate = result.responseDate
            logsRepo.save(log)
            throw e
        }
    }

    suspend fun processPaymentNotification(value: NotificationRequestValue): NotificationResponseValue {
        val result = NotificationResponseValue()
        val log = daoService.createTransactionLog(0, daoService.generateTransactionReference())
        try {
            value.billReferenceCode
                    ?.let { code ->
                        stagingRepo.findByReferenceCode(code)
                                ?.let { record ->
                                    if (record.transactionId != null) {
                                        result.responseDate = Timestamp.from(Instant.now())
                                        result.statusCode = invoiceStatus.duplicateTransaction
                                        result.statusDescription = record.statusDescription
                                        return result

                                    } else {
                                        log.integrationRequest = daoService.mapper().writeValueAsString(value)
                                        result.billReferenceCode = record.referenceCode
                                        record.paidAmount = value.totalAmount
                                        record.paymentSource = value.paymentSource
                                        record.paymentTransactionDate = value.transactionDate
                                        record.transactionId = value.transactionID
                                        record.customerName = record.customerName?.let { value.customerName }
                                                ?: "${record.customerName}|${value.customerName}"
                                        record.extras = record.extras?.let { "${value.extras}" }
                                                ?: "${record.extras}|${value.extras}"
                                        record.paymentTablesUpdatedStatus = 1
                                        stagingRepo.save(record)

                                        /**
                                         * TODO: Transverse to a different status as a payment has now been received
                                         */

                                        result.responseDate = Timestamp.from(Instant.now())
                                        result.statusCode = invoiceStatus.success
                                        result.statusDescription = record.statusDescription



                                        log.integrationResponse = daoService.mapper().writeValueAsString(result)
                                        log.responseStatus = invoiceStatus.success
                                        log.responseMessage = result.billReferenceCode
                                        log.transactionCompletedDate = Timestamp.from(Instant.now())
                                        logsRepo.save(log)
                                        return result

                                    }

                                }
                                ?: run {
                                    result.responseDate = Timestamp.from(Instant.now())
                                    result.statusCode = invoiceStatus.notFound
                                    result.statusDescription = ""
                                    return result
                                }
                    }
                    ?: throw NullValueNotAllowedException("Invalid Reference code")


        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            log.responseStatus = invoiceStatus.error
            log.responseMessage = e.message


            result.responseDate = Timestamp.from(Instant.now())
            result.statusCode = log.responseStatus
            result.statusDescription = log.responseMessage

            log.integrationResponse = daoService.mapper().writeValueAsString(result)

            log.transactionCompletedDate = Timestamp.from(Instant.now())
            logsRepo.save(log)
            throw e

        }
    }
}