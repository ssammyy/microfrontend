package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.InvoiceEntity
import org.kebs.app.kotlin.apollo.store.model.PetroleumInstallationInspectionEntity
import org.kebs.app.kotlin.apollo.store.model.invoice.BillPayments
import org.kebs.app.kotlin.apollo.store.model.invoice.BillTransactionsEntity
import org.kebs.app.kotlin.apollo.store.model.invoice.BillingLimits
import org.kebs.app.kotlin.apollo.store.model.invoice.CorporateCustomerAccounts
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.sql.Timestamp
import java.util.*


@Repository
interface IInvoiceRepository : HazelcastRepository<InvoiceEntity, Long> {
    fun findByPermitId(permitId: Long): InvoiceEntity?
    fun findByPermitRefNumber(permitRefNumber: String): InvoiceEntity?
    fun findByPermitIdAndUserId(permitId: Long, userId: Long): InvoiceEntity?
    fun findByPermitRefNumberAndUserId(permitRefNumber: String, userId: Long): InvoiceEntity?
    fun findByPermitRefNumberAndUserIdAndPermitId(permitRefNumber: String, userId: Long, permitId: Long): InvoiceEntity?
    fun findAllByStatus(status: Long): List<InvoiceEntity>?
    fun findAllByUserIdAndStatus(userId: Long, status: Int): List<InvoiceEntity>?
    fun findByStatus(status: Long, pages: Pageable): Page<InvoiceEntity>?

    /*
    fun findAllByManufacturer(manufacturer: ManufacturersEntity, page: Pageable): Page<InvoiceEntity>?
    fun findAllByManufacturer(manufacturer: ManufacturersEntity): List<InvoiceEntity>?
    */
    fun findAllByManufacturer(manufacturer: Long, page: Pageable): Page<InvoiceEntity>?
    fun findAllByManufacturer(manufacturer: Long): List<InvoiceEntity>?
    fun findAllByUserIdAndPaymentStatus(userId: Long, paymentStatus: Int): List<InvoiceEntity>?
    fun findAllByUserIdAndPaymentStatusAndBatchInvoiceNoIsNull(userId: Long, paymentStatus: Int): List<InvoiceEntity>?
    fun findAllByBatchInvoiceNo(
            batchInvoiceNo: Long
    ): List<InvoiceEntity>?

    fun findByInstallationInspectionId(installationInspectionId: PetroleumInstallationInspectionEntity): InvoiceEntity?
}

@Repository
interface IBillingLimitsRepository : HazelcastRepository<BillingLimits, Long> {
    fun findAllByStatus(status: Int): List<BillingLimits>
}

@Repository
interface ICorporateCustomerRepository : HazelcastRepository<CorporateCustomerAccounts, Long> {
    fun findAllByCorporateIdentifier(corporateId: String?): Optional<CorporateCustomerAccounts>
    fun findAllByCorporateNameContains(corporateName: String, page: Pageable): Page<CorporateCustomerAccounts>
    fun countByCreatedOnBetween(startDate: Timestamp, endDate: Timestamp): Long
}

interface BillSummary {
    fun getTotalAmount(): BigDecimal
    fun getTotalTax(): BigDecimal
    fun getRevenueLine(): String
}

@Repository
interface IBillTransactionsEntityRepository : HazelcastRepository<BillTransactionsEntity, Long> {
    fun findAllByCorporateIdAndBillId(corporateId: Long, billId: Long): List<BillTransactionsEntity>
    fun findByBillId(billId: Long): List<BillTransactionsEntity>

    @Query(value = "select sum(CASE WHEN bt.AMOUNT>0 then bt.AMOUNT else 0.0 END) TOTAL_AMOUNT,sum(CASE WHEN bt.TAX_AMOUNT>0 then bt.TAX_AMOUNT else 0.0 END) TOTAL_TAX, bt.REVENUE_LINE from DAT_KEBS_BILL_TRANSACTIONS bt  where BILL_ID=:billId and bt.PAID_STATUS=:billStatus group by bt.REVENUE_LINE", nativeQuery = true)
    fun sumTotalAmountByRevenueLineAndBillId(@Param("billId") billId: Long?, @Param("billStatus") status: Int): List<BillSummary>
    fun findByInvoiceNumberAndBillId(demandNoteNumber: String, billId: Long): BillTransactionsEntity?
}


@Repository
interface IBillPaymentsRepository : HazelcastRepository<BillPayments, Long> {
    fun findByCorporateId(corporateId: Long, page: Pageable): Page<BillPayments>
    fun findAllByPaymentStatus(status: Int): List<BillPayments>
    fun findAllByPaymentStatusAndNextNoticeDateGreaterThan(status: Int, date: Date): List<BillPayments>
    fun findAllByBillNumberPrefixAndPaymentStatus(billNumberPrefix: String, status: Int): List<BillPayments>
    fun countByCorporateIdAndBillNumber(corporateId: Long?, billNumber: String): Long
    fun countByCorporateId(corporateId: Long?): Long
    fun findFirstByCorporateIdAndBillNumberAndPaymentStatus(corporateId: Long?, billNumber: String, status: Int): Optional<BillPayments>
    @Query(value = "select sum(CASE WHEN bt.AMOUNT>0 then bt.AMOUNT else 0.0 END) TOTAL_AMOUNT from DAT_KEBS_BILL_TRANSACTIONS bt  where CORPORATE_ID=:corporateId and BILL_ID=:billId", nativeQuery = true)
    fun sumTotalAmountByCorporateIdAndBillId(@Param("corporateId") corporateId: Long?, @Param("billId") billId: Long): BigDecimal?
    fun findAllByCorporateIdAndPaymentStatusIn(corporateId: Long?, status: List<Int>): List<BillPayments>
    fun findFirstByBillStatusAndPaymentStatusAndPostingStatusOrderByCreateOnDesc(billStatus: Int, paymentStatus: Int, postingStatus: Int): BillPayments?
}