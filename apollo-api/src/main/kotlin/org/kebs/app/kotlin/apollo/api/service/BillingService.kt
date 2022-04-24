package org.kebs.app.kotlin.apollo.api.service

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CdTypeCodes
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.InvoiceDaoService
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.CdDemandNoteEntity
import org.kebs.app.kotlin.apollo.store.model.ServiceMapsEntity
import org.kebs.app.kotlin.apollo.store.model.invoice.*
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocPartnersEntity
import org.kebs.app.kotlin.apollo.store.repo.*
import org.springframework.data.domain.PageRequest
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.security.auth.login.AccountNotFoundException

enum class BillStatus(val status: Int) {
    OPEN(1), CLOSED(2), PENDING_PAYMENT(3), PAID(4);
}

enum class BillingPurpose(val code: String) {
    DESTINATION_INSPECTION("DI"), QUALITY_ASSUARANCE("QA"), MARKET_SURVEILANCE("MS"), STANDARDS_DEVELOPMENT("SD");
}

@Component
class BillingService(
        private val corporateCustomerRepository: ICorporateCustomerRepository,
        private val billPaymentRepository: IBillPaymentsRepository,
        private val billingLimitsRepository: IBillingLimitsRepository,
        private val batchInvoiceRepository: InvoiceBatchDetailsRepo,
        private val invoiceDaoService: InvoiceDaoService,
        private val diServiceDao: DestinationInspectionDaoServices,
        private val billTransactionRepo: IBillTransactionsEntityRepository,
        private val commonDaoServices: CommonDaoServices,

        private val properties: ApplicationMapProperties
) {
    final val DATE_FORMATER = DateTimeFormatter.ofPattern("yyyy-MM")
    final val MONTH_FORMATER = DateTimeFormatter.ofPattern("MM")

    fun addBillTransaction(transaction: BillTransactionsEntity, corporate: CorporateCustomerAccounts): BillPayments {
        val billNumber = creteBillPrefix(corporate.accountLimits)
        val billOptional = this.billPaymentRepository.findFirstByCorporateIdAndBillNumberAndPaymentStatus(corporate.id, billNumber, BillStatus.OPEN.status)
        if (billOptional.isPresent) {
            val bill = billOptional.get()
            transaction.billId = bill.id
            this.billTransactionRepo.save(transaction)
            bill.billAmount = this.billPaymentRepository.sumTotalAmountByCorporateIdAndBillId(corporate.id, bill.id)
            // Check limits and close bill if limit is exceeded
            corporate.accountLimits?.let {
                if (BigDecimal.ZERO <= it.maxBillAmount) {
                    if (bill.billAmount?.compareTo(it.maxBillAmount) ?: 0 >= 0) {
                        bill.billStatus = BillStatus.CLOSED.status
                        bill.billDate = commonDaoServices.getTimestamp()
                    }
                }
            }
            return this.billPaymentRepository.save(bill)
        } else {
            val bill = BillPayments()
            bill.corporateId = corporate.id
            bill.billNumberSequence = this.billPaymentRepository.countByCorporateIdAndBillNumber(corporate.id, billNumber) + 1
            bill.billNumber = billNumber
            bill.paymentStatus = BillStatus.OPEN.status
            bill.customerName = corporate.corporateName
            bill.customerCode = corporate.corporateCode
            bill.currencyCode = "KES"
            bill.billServiceType = BillingPurpose.DESTINATION_INSPECTION.code
            bill.billType = 1
            bill.billStatusDesc = BillStatus.OPEN.name
            bill.billStatus = BillStatus.OPEN.status
            bill.penaltyAmount = BigDecimal.ZERO
            bill.totalAmount = transaction.amount
            bill.status = 1
            val paymentDate = when {
                corporate.paymentDays?.compareTo(0) ?: 0 > 0 -> LocalDate.now().withDayOfMonth(corporate.paymentDays
                        ?: 15)
                else -> corporate.accountLimits?.let { LocalDate.now().withDayOfMonth(it.billPaymentDay ?: 15) }
                        ?: LocalDate.now().withDayOfMonth(15)
            }
            bill.paymentDate = Date.valueOf(paymentDate)
            val dt = Timestamp.from(Instant.now())
            bill.createOn = dt
            bill.billNumberPrefix = "BN${commonDaoServices.convertDateToString(dt.toLocalDateTime(), "yyMM")}${this.billPaymentRepository.countByCorporateId(corporate.id) + 1}"
            bill.billRefNumber = "${bill.billNumberPrefix}${bill.billNumber}"
            bill.billDescription = "Bill payment for the month of " + commonDaoServices.convertDateToString(bill.paymentDate, "MMMM")
            val saved = this.billPaymentRepository.save(bill)
            transaction.billId = saved.id
            this.billTransactionRepo.save(transaction)
            bill.billAmount = this.billPaymentRepository.sumTotalAmountByCorporateIdAndBillId(corporate.id, saved.id)
            this.billPaymentRepository.save(bill)
            return saved
        }

    }

    /**
     * Registers transaction for billing and send assigns a temporally transaction reference/receipt
     *
     * @param demandNote demand node to add for billing
     * @param map application properties
     */
    fun registerBillTransaction(demandNote: CdDemandNoteEntity, identifier: String?, cdType: String?, map: ServiceMapsEntity): BillTransactionsEntity? {
        // Find corporate by supplied indentifier(Courier Good) or importer Pin
        KotlinLogging.logger { }.info("Importer: ${demandNote.importerPin}, Courier: $identifier, CdType: $cdType")
        if (!CdTypeCodes.COURIER_GOODS.code.equals(cdType, true)) { // Only courier documents allowed
            KotlinLogging.logger { }.info("Found document of type, bill not applicable: $cdType")
            return null
        }

        val corporate = identifier?.let {
            corporateCustomerRepository.findAllByCorporateIdentifier(it)
        } ?: corporateCustomerRepository.findAllByCorporateIdentifier(demandNote.importerPin)
        // Add transaction to billing if corporate billing exists
        if (corporate.isPresent) {
            // Check if account is suspended or blocked
            if (!(corporate.get().accountSuspendend == 1 || corporate.get().accountBlocked == 1)) {

                val transactionEntity = BillTransactionsEntity()
                transactionEntity.amount = demandNote.totalAmount
                transactionEntity.taxAmount = demandNote.totalAmount?.times(BigDecimal(0.16))
                transactionEntity.corporateId = corporate.get().id
                transactionEntity.description = demandNote.descriptionGoods
                transactionEntity.invoiceNumber = demandNote.demandNoteNumber
                transactionEntity.transactionType = "DEMAND_NOTE"
                transactionEntity.paidStatus = 0
                transactionEntity.tempReceiptNumber = generateRefNoteNumber(corporate.get().accountLimits, map)
                transactionEntity.revenueLine = demandNote.revenueLine
                transactionEntity.status = 1
                transactionEntity.transactionDate = Timestamp.from(Instant.now())
                transactionEntity.transactionId = demandNote.id.toString()
                transactionEntity.createdBy = demandNote.createdBy
                transactionEntity.createdOn = Timestamp.from(Instant.now())
                val saved = billTransactionRepo.save(transactionEntity)
                this.addBillTransaction(transactionEntity, corporate.get())
                return saved
            }
        }
        return null

    }

    fun demandNoteTransactionPaid(demandNoteNumber: String, receiptNumber: String, billId: Long) {
        this.billTransactionRepo.findByInvoiceNumberAndBillId(demandNoteNumber, billId)?.let { transactionEntity ->
            transactionEntity.receiptNumber = receiptNumber
            transactionEntity.paidStatus = 1
            transactionEntity.receiptDate = Timestamp.from(Instant.now())
            this.billTransactionRepo.save(transactionEntity)
        }

    }

    /**
     * Registers transaction for billing and send assigns a temporally transaction reference/receipt
     *
     * @param transactionEntity demand node to add for billing
     * @param partner application properties
     */
    fun registerPvocTransaction(transactionEntity: BillTransactionsEntity, partner: PvocPartnersEntity): BillTransactionsEntity? {
        // Find corporate by supplied indentifier(Courier Good) or importer Pin
        val corporate = corporateCustomerRepository.findById(partner.billingId!!)
        val map = commonDaoServices.serviceMapDetails(properties.mapImportInspection)
        // Add transaction to billing if corporate billing exists
        if (corporate.isPresent) {
            transactionEntity.fobAmount = this.diServiceDao.convertAmount(transactionEntity.fobAmount, transactionEntity.currency
                    ?: "USD")
            transactionEntity.amount = this.diServiceDao.convertAmount(transactionEntity.amount, transactionEntity.currency
                    ?: "USD")
            transactionEntity.currency = "KES"
            transactionEntity.corporateId = corporate.get().id
            transactionEntity.transactionType = "PVOC_CHARGE"
            transactionEntity.paidStatus = 0
            transactionEntity.tempReceiptNumber = generateRefNoteNumber(corporate.get().accountLimits, map)
            transactionEntity.status = 1
            transactionEntity.transactionDate = Timestamp.from(Instant.now())
            transactionEntity.createdBy = transactionEntity.createdBy
            transactionEntity.createdOn = Timestamp.from(Instant.now())
            val saved = billTransactionRepo.save(transactionEntity)
            this.addBillTransaction(transactionEntity, corporate.get())
            return saved
        } else {
            throw ExpectedDataNotFound("Could not find associated billing account")
        }
    }

    fun billTransactions(billId: Long, corporateId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        val corporate = corporateCustomerRepository.findById(corporateId)
        if (corporate.isPresent) {
            val bill = this.billPaymentRepository.findById(billId)
            if (bill.isPresent) {
                val map = mutableMapOf<String, Any>()
                map["transactions"] = billTransactionRepo.findAllByCorporateIdAndBillId(corporateId, billId)
                map["bill"] = bill.get()
                response.data = map
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "Success"
            } else {
                response.responseCode = ResponseCodes.NOT_FOUND
                response.message = "Bill not found"
            }
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
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to load bills", ex)
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

    fun creteBillPrefix(limit: BillingLimits?, current: Boolean = false): String {
        val today = LocalDate.now()
        var billNumber = DATE_FORMATER.format(today) // This month
        if (!current) {
            billNumber = DATE_FORMATER.format(today.minusMonths(1)) // Last Month
        }
        limit?.let {
            when (limit.billDateType) {
                "DATE_RANGE" -> {
                    val startDate: LocalDate
                    val endDate: LocalDate
                    if (current) {
                        startDate = today.minusMonths(1).withDayOfMonth(limit.billStartDate!!)
                        endDate = today.withDayOfMonth(limit.billEndDay!!)
                    } else {
                        startDate = today.minusMonths(2).withDayOfMonth(limit.billStartDate!!)
                        endDate = today.minusMonths(1).withDayOfMonth(limit.billEndDay!!)
                    }
                    billNumber = DATE_FORMATER.format(startDate) + MONTH_FORMATER.format(endDate)
                }
            }
        }
        return billNumber
    }

    /**
     * Send demand note payment notices every 3 minutes
     *
     */
    @Scheduled(fixedDelay = 180_000)
    fun sendPaymentNotices() {
        val date = LocalDateTime.now()
        val startOfDay = date.withHour(6) // Start of day
        val endOfDay = date.withHour(19) // End of day
        if (date.isAfter(startOfDay) && date.isBefore(endOfDay)) {
            // don't send emails at night
            return
        }
        val bills = this.billPaymentRepository.findAllByPaymentStatus(BillStatus.PENDING_PAYMENT.status)
        val map = commonDaoServices.serviceMapDetails(properties.mapImportInspection)
        bills.forEach { bill ->
            // TODO: send payment notice
            // If late
            this.applyPenaltyToBill(bill, map)
            // Increment notice
            bill.noticeCount = bill.noticeCount?.plus(2) ?: 2
            bill.nextNoticeDate = Date.valueOf(LocalDate.now().plusDays(2))
            this.billPaymentRepository.save(bill)
        }
    }

    /**
     * Close all bill that are due and request payment
     * run once a minute
     */
    @Scheduled(fixedDelay = 60_000)
    fun closeAllDueBills() {
        val limits = this.billingLimitsRepository.findAllByStatus(1)
        val date = LocalDate.now()
        limits.forEach {
            val billPrefix = creteBillPrefix(it, false)
            val bills = this.billPaymentRepository.findAllByBillNumberPrefixAndPaymentStatus(billPrefix, BillStatus.CLOSED.status)
            bills.forEach { bill ->
                bill.billStatus = BillStatus.CLOSED.status
                bill.billDate = commonDaoServices.getTimestamp()
                bill.billStatusDesc = BillStatus.CLOSED.name
                bill.paymentRequestDate = Timestamp.from(Instant.now())
                bill.nextNoticeDate = Date.valueOf(date.plusDays(2))
                bill.noticeCount = 1
                this.billPaymentRepository.save(bill)
            }
        }
    }

    fun closeAndGenerateBill(billId: Long, remarks: String): ApiResponseModel {
        val response = ApiResponseModel()
        val optionalBill = this.billPaymentRepository.findById(billId)
        if (optionalBill.isPresent) {
            val bill = optionalBill.get()
            bill.remarks = remarks
            if (this.generateBatchInvoice(bill)) {
                response.message = "Bill sent"
                response.responseCode = ResponseCodes.SUCCESS_CODE
            } else {
                response.message = "Bill invoice not generated"
                response.responseCode = ResponseCodes.FAILED_CODE
            }
        } else {
            response.message = "Bill not found"
            response.responseCode = ResponseCodes.NOT_FOUND
        }

        return response
    }

    /**
     * Run once every 3 minutes
     */
    @Scheduled(fixedDelay = 180_000)
    fun generateBills() {
        val today = Date.from(Instant.now())
        val bills = this.billPaymentRepository.findAllByPaymentStatusAndNextNoticeDateGreaterThan(BillStatus.CLOSED.status, today)
        bills.forEach { bill ->
            if (this.generateBatchInvoice(bill)) {
                return
            }
        }
    }

    @Transactional
    fun generateBatchInvoice(bill: BillPayments): Boolean {
        val map = commonDaoServices.serviceMapDetails(properties.mapImportInspection)
        bill.billStatus = BillStatus.PENDING_PAYMENT.status
        bill.paymentRequestDate = Timestamp.from(Instant.now())

        val batch = InvoiceBatchDetailsEntity()
        batch.tableSource = "BILLING"
        batch.totalAmount = bill.totalAmount
        batch.description = bill.billNumber + "-" + bill.billNumberSequence
        batch.batchNumber = "KIMSAR${bill.billNumber}${
            generateRandomText(
                    5,
                    map.secureRandom,
                    map.messageDigestAlgorithm,
                    true
            )
        }".toUpperCase()
        batch.status = map.inactiveStatus
        batch.paymentStarted = map.inactiveStatus
        batch.createdBy = "Billing"
        batch.createdOn = commonDaoServices.getTimestamp()
        batch.modifiedBy = "Billing"
        batch.modifiedOn = commonDaoServices.getTimestamp()
        val bsaved = this.batchInvoiceRepository.save(batch)
        bill.paymentRequestReference = bsaved.id.toString()
        val myAccountDetails = InvoiceDaoService.InvoiceAccountDetails()
        val corporate = corporateCustomerRepository.findById(bill.corporateId)
        if (corporate.isPresent) {
            with(myAccountDetails) {
                accountName = corporate.get().corporateName
                accountNumber = corporate.get().corporateIdentifier
                currency = properties.mapInvoiceTransactionsLocalCurrencyPrefix
            }
            this.billPaymentRepository.save(bill)
            this.invoiceDaoService.postBillToSage(bill, "system", map, corporate.get())
        }
        return true
    }

    fun generateRefNoteNumber(limit: BillingLimits?, map: ServiceMapsEntity): String {
        val prefix = limit?.billReceiptPrefix ?: "PNT-INV"
        return "${prefix}${generateRandomText(5, map.secureRandom, map.messageDigestAlgorithm, true)}".toUpperCase()
    }

    @Transactional
    fun applyPenaltyToBill(bill: BillPayments, map: ServiceMapsEntity) {
        val penaltyBill = BillPayments()
        val optionAccoint = this.corporateCustomerRepository.findById(penaltyBill.corporateId)
        if (optionAccoint.isPresent) {
            val corporate = optionAccoint.get()
            penaltyBill.corporateId = bill.corporateId
            penaltyBill.billNumberSequence = this.billPaymentRepository.countByCorporateIdAndBillNumber(corporate.id, bill.billNumber
                    ?: "") + 1
            penaltyBill.billNumber = bill.billNumber
            penaltyBill.paymentStatus = 0
            penaltyBill.status = 1
            penaltyBill.penaltyAmount = BigDecimal.ZERO
            penaltyBill.billStatus = BillStatus.CLOSED.status
            penaltyBill.billStatusDesc = BillStatus.CLOSED.name

            val paymentDate = when {
                corporate.paymentDays?.compareTo(0) ?: 0 > 0 -> LocalDate.now().withDayOfMonth(corporate.paymentDays
                        ?: 15)
                else -> corporate.accountLimits?.let { LocalDate.now().withDayOfMonth(it.billPaymentDay ?: 15) }
                        ?: LocalDate.now().withDayOfMonth(15)
            }
            penaltyBill.paymentDate = Date.valueOf(paymentDate)
            penaltyBill.createOn = Timestamp.from(Instant.now())
            val savedBill = this.billPaymentRepository.save(penaltyBill)
            corporate.accountLimits?.let { limit ->
                limit.penaltyAmount?.let { penalty ->
                    if (BigDecimal.ZERO.compareTo(penalty) >= 0) {
                        val transactionEntity = BillTransactionsEntity()
                        transactionEntity.amount = when (limit.penaltyType) {
                            "PERCENTAGE" -> penalty.multiply(bill.totalAmount).divide(BigDecimal.valueOf(100))
                            "FIXED" -> penalty
                            else -> penalty
                        }
                        transactionEntity.corporateId = corporate.id
                        transactionEntity.description = "Penalty for late payment on bill " + bill.billNumber + " " + bill.billNumberSequence
                        transactionEntity.invoiceNumber = bill.billNumber
                        transactionEntity.transactionType = "BILL_PENALTY"
                        transactionEntity.paidStatus = 0
                        transactionEntity.tempReceiptNumber = generateRefNoteNumber(limit, map)
                        transactionEntity.status = 1
                        transactionEntity.transactionDate = Timestamp.from(Instant.now())
                        transactionEntity.createdBy = "system"
                        transactionEntity.createdOn = Timestamp.from(Instant.now())
                        transactionEntity.billId = savedBill.id
                        val saved = billTransactionRepo.save(transactionEntity)
                        penaltyBill.totalAmount = transactionEntity.amount
                        this.billTransactionRepo.save(transactionEntity)
                        penaltyBill.billAmount = this.billPaymentRepository.sumTotalAmountByCorporateIdAndBillId(corporate.id, saved.id)
                        this.billPaymentRepository.save(penaltyBill)
                    }
                }
            } ?: throw AccountNotFoundException("Account limits and penalty not set")
        }
    }


}