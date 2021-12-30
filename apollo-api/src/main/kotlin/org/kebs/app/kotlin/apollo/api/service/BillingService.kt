package org.kebs.app.kotlin.apollo.api.service

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
import org.kebs.app.kotlin.apollo.store.model.CdDemandNoteEntity
import org.kebs.app.kotlin.apollo.store.model.ServiceMapsEntity
import org.kebs.app.kotlin.apollo.store.model.invoice.BillPayments
import org.kebs.app.kotlin.apollo.store.model.invoice.BillTransactionsEntity
import org.kebs.app.kotlin.apollo.store.model.invoice.CorporateCustomerAccounts
import org.kebs.app.kotlin.apollo.store.repo.IBillPaymentsRepository
import org.kebs.app.kotlin.apollo.store.repo.IBillTransactionsEntityRepository
import org.kebs.app.kotlin.apollo.store.repo.ICorporateCustomerRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Component
class BillingService(
        private val corporateCustomerRepository: ICorporateCustomerRepository,
        private val billPaymentRepository: IBillPaymentsRepository,
        private val billTransactionRepo: IBillTransactionsEntityRepository,
        private val commonDaoServices: CommonDaoServices
) {
    final val DATE_FORMATER = DateTimeFormatter.ofPattern("yyyy-MM")
    fun addBillTransaction(transaction: BillTransactionsEntity, corporate: CorporateCustomerAccounts): BillPayments {
        val billNumber = DATE_FORMATER.format(LocalDate.now())
        val billOptional = this.billPaymentRepository.findFirstByCorporateIdAndBillNumber(corporate.id, billNumber)
        if (billOptional.isPresent) {
            val bill = billOptional.get()
            transaction.billId = bill.id
            this.billTransactionRepo.save(transaction)
            bill.billAmount = this.billPaymentRepository.sumTotalAmountByCorporateIdAndBillId(corporate.id, bill.id)
            return this.billPaymentRepository.save(bill)
        } else {
            val bill = BillPayments()
            bill.corporateId = corporate.id
            bill.billNumber = billNumber
            bill.paymentStatus = 0
            bill.totalAmount = transaction.amount
            bill.penaltyAmount = BigDecimal.ZERO
            bill.totalAmount = BigDecimal.ZERO
            bill.status = 1
            bill.createOn = Timestamp.from(Instant.now())
            val saved = this.billPaymentRepository.save(bill)
            transaction.billId = saved.id
            this.billTransactionRepo.save(transaction)
            return saved
        }
    }

    /**
     * Registers transaction for billing and send assigns a temporally transaction reference/receipt
     *
     * @param demandNote demand node to add for billing
     * @param map application properties
     */
    fun registerBillTransaction(demandNote: CdDemandNoteEntity, map: ServiceMapsEntity): BillTransactionsEntity? {
        val corporate = corporateCustomerRepository.findAllByCorporateIdentifier(demandNote.importerPin)
        if (corporate.isPresent) {
            val transactionEntity = BillTransactionsEntity()
            transactionEntity.amount = demandNote.totalAmount
            transactionEntity.corporateId = corporate.get().id
            transactionEntity.description = demandNote.descriptionGoods
            transactionEntity.invoiceNumber = demandNote.demandNoteNumber
            transactionEntity.paidStatus = 0
            transactionEntity.tempReceiptNumber = "TMP-INV${generateRandomText(5, map.secureRandom, map.messageDigestAlgorithm, true)}".toUpperCase()
            transactionEntity.status = 1
            transactionEntity.transactionDate = Timestamp.from(Instant.now())
            transactionEntity.transactionId = demandNote.id.toString()
            transactionEntity.createdBy = commonDaoServices.loggedInUserAuthentication().name
            transactionEntity.createdOn = Timestamp.from(Instant.now())
            val saved = billTransactionRepo.save(transactionEntity)
            this.addBillTransaction(transactionEntity, corporate.get())
            return saved
        }
        return null

    }

    fun billTransactions(billId: Long, corporateId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        val corporate = corporateCustomerRepository.findById(corporateId)
        if (corporate.isPresent) {
            response.data = billTransactionRepo.findAllByCorporateIdAndBillId(billId, corporateId)
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Success"
        } else {
            response.responseCode = ResponseCodes.NOT_FOUND
            response.message = "Corporate not found"
        }
        return response
    }

    fun corporateBills(corporateId: Long, page: PageRequest): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val corporate = corporateCustomerRepository.findById(corporateId)
            if (corporate.isPresent) {
                val pg = billPaymentRepository.findByCorporateId(corporateId, page)
                response.data = pg.toList()
                response.totalPages = pg.totalPages
                response.totalCount = pg.totalElements
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "Success"
            } else {
                response.responseCode = ResponseCodes.NOT_FOUND
                response.message = "Corporate not found"
            }
        }catch (ex: Exception) {
            KotlinLogging.logger {  }.error("Failed to load bills", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Corporate bills not found"
        }
        return response
    }

    fun corporateBillByPaymentStatus(corporateId: Long, paymentStatus: List<Int>): ApiResponseModel {
        val response = ApiResponseModel()
        val corporate = corporateCustomerRepository.findById(corporateId)
        if (corporate.isPresent) {
            val map = mutableMapOf<String, Any>()
            map["corporate"] = corporate
            map["bills"] = billPaymentRepository.findAllByCorporateIdAndPaymentStatusIn(corporateId, paymentStatus)
            response.data = map
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Success"
        } else {
            response.responseCode = ResponseCodes.NOT_FOUND
            response.message = "Corporate not found"
        }
        return response
    }

}