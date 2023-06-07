package org.kebs.app.kotlin.apollo.api.payload.response;

import org.kebs.app.kotlin.apollo.api.service.PaymentStatus
import org.kebs.app.kotlin.apollo.store.model.CdDemandNoteEntity
import org.kebs.app.kotlin.apollo.store.model.di.CdDemandNotePaymentEntity
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp

class DemandNotePaymentDto {
    var id: Long = 0
    var demandNoteId: Long? = null
    var referenceNumber: String? = null
    var receiptNumber: String? = null
    var paymentSource: String? = null
    var receiptDate: java.util.Date? = null
    var previousBalance: BigDecimal? = null
    var amount: BigDecimal? = null
    var balanceAfter: BigDecimal? = null
    var status: Int? = null

    companion object {
        fun fromEntity(payment: CdDemandNotePaymentEntity): DemandNotePaymentDto {
            return DemandNotePaymentDto().apply {
                id = payment.id
                demandNoteId = payment.demandNoteId?.id
                referenceNumber = payment.referenceNumber
                receiptNumber = payment.receiptNumber
                paymentSource = payment.paymentSource
                receiptDate = payment.receiptDate
                previousBalance = payment.previousBalance
                amount = payment.amount
                balanceAfter = payment.balanceAfter
                status = payment.status
            }
        }

        fun fromList(payments: List<CdDemandNotePaymentEntity>): List<DemandNotePaymentDto> {
            val dtos = mutableListOf<DemandNotePaymentDto>()
            payments.forEach { dtos.add(fromEntity(it)) }
            return dtos
        }
    }
}

class DemandNoteDto {
    var id: Long? = 0
    var nameImporter: String? = null
    var importerPin: String? = null
    var shippingAgent: String? = null
    var entryNo: String? = null
    var entryPoint: String? = null
    var courier: String? = null
    var swStatus: Int? = 0
    var address: String? = null
    var telephone: String? = null
    var product: String? = null
    var cfvalue: BigDecimal? = null
    var invoiceBatchNumberId: Long? = null
    var destinationFeeValue: Long? = 0
    var rate: String? = null
    var currency: String? = null
    var totalAmount: BigDecimal? = null
    var amountPayable: BigDecimal? = null
    var amountPaid: BigDecimal? = null
    var currentBalance: BigDecimal? = null
    var entryAblNumber: String? = null
    var paymentDate: Timestamp? = null
    var dateGenerated: Date? = null
    var ucrNumber: String? = null
    var cdRefNo: String? = null
    var demandNoteNumber: String? = null
    var receiptNo: String? = null
    var generatedBy: String? = null
    var paymentStatus: Int? = 0
    var paymentStatusDesc: String? = null
    var paymentPurpose: String? = null
    var billId: Long? = 0
    var cdId: Long? = 0
    var postingStatus: Int? = 0
    var postingStatusDesc: String? = null
    var postingReference: String? = null
    var postingMsgRef: String? = null
    var postingResponseMessage: String? = null
    var postingResponseCode: String? = null
    var postingResponseDate: String? = null
    var remarks: String? = null
    var status: Int? = null
    var payments: List<DemandNotePaymentDto>? = null

    companion object {
        fun fromEntity(note: CdDemandNoteEntity, details: Boolean = false): DemandNoteDto {
            val dto = DemandNoteDto().apply {
                id = note.id
                nameImporter = note.nameImporter
                importerPin = note.importerPin
                shippingAgent = note.shippingAgent
                entryNo = note.entryNo
                entryPoint = note.entryPoint
                courier = note.courier
                swStatus = note.swStatus
                address = note.address
                telephone = note.telephone
                product = note.product
                cfvalue = note.cfvalue
                invoiceBatchNumberId = note.invoiceBatchNumberId
                destinationFeeValue = note.destinationFeeValue
                rate = note.rate
                currency = note.currency
                totalAmount = note.totalAmount
                amountPayable = note.amountPayable
                amountPaid = note.amountPaid
                currentBalance = note.currentBalance
                entryAblNumber = note.entryAblNumber
                paymentDate = note.paymentDate
                dateGenerated = note.dateGenerated
                cdRefNo = note.cdRefNo
                demandNoteNumber = note.demandNoteNumber
                generatedBy = note.generatedBy
                paymentStatus = note.paymentStatus
                paymentStatusDesc = when (note.paymentStatus) {
                    PaymentStatus.REJECTED.code -> "DEMAND NOTE REJECTED"
                    PaymentStatus.NEW.code -> "PENDING PAYMENT"
                    PaymentStatus.DRAFT.code -> "PENDING APPROVAL"
                    PaymentStatus.PAID.code -> "PAYMENT COMPLETED"
                    PaymentStatus.BILLED.code -> "PAYMENT BILLED"
                    PaymentStatus.PARTIAL_PAYMENT.code -> "PARTIALLY PAID"
                    else -> "UNKNOWN"
                }
                paymentPurpose = note.paymentPurpose
                billId = note.billId
                cdId = note.cdId
                postingStatus = note.postingStatus
                postingStatusDesc = when (note.postingStatus) {
                    null, 0 -> "PENDING POSTING"
                    1 -> "PAYMENT REQUEST SENT"
                    else -> "POSTING FAILED"
                }
                postingReference = note.postingReference
                postingMsgRef = note.varField5
                postingResponseMessage = note.varField7
                postingResponseCode = note.varField6
                postingResponseDate = note.varField9
                remarks = note.varField10
                status = note.status
            }

            if (details) {
                dto.payments = note.payments?.let { DemandNotePaymentDto.fromList(it) }
            }
            return dto
        }

        fun fromList(notes: List<CdDemandNoteEntity>): List<DemandNoteDto> {
            val dtos = mutableListOf<DemandNoteDto>()
            notes.forEach { dtos.add(fromEntity(it)) }
            return dtos
        }
    }
}
