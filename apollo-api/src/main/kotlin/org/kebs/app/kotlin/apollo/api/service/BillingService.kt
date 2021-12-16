package org.kebs.app.kotlin.apollo.api.service

import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
import org.kebs.app.kotlin.apollo.store.model.CdDemandNoteEntity
import org.kebs.app.kotlin.apollo.store.model.ServiceMapsEntity
import org.kebs.app.kotlin.apollo.store.model.invoice.BillTransactionsEntity
import org.kebs.app.kotlin.apollo.store.repo.IBillPaymentsRepository
import org.kebs.app.kotlin.apollo.store.repo.IBillTransactionsEntityRepository
import org.kebs.app.kotlin.apollo.store.repo.ICorporateCustomerRepository
import org.springframework.stereotype.Component
import java.sql.Timestamp
import java.time.Instant

@Component
class BillingService(
        private val corporateCustomerRepository: ICorporateCustomerRepository,
        private val billPaymentRepository: IBillPaymentsRepository,
        private val billTransactionRepo: IBillTransactionsEntityRepository,
        private val commonDaoServices: CommonDaoServices
) {
    /**
     * Registers transacton for billing and send assigns a temporaly transaction reference/receipt
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
            return billTransactionRepo.save(transactionEntity)
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

    fun corporateBills(corporateId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        val corporate = corporateCustomerRepository.findById(corporateId)
        if (corporate.isPresent) {
            val map = mutableMapOf<String, Any>()
            map["corporate"] = corporate
            map["bills"] = billPaymentRepository.findAllByCorporateId(corporateId)
            response.data = map
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Success"
        } else {
            response.responseCode = ResponseCodes.NOT_FOUND
            response.message = "Corporate not found"
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