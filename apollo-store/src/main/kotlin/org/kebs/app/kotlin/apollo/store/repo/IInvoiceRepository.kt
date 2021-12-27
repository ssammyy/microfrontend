package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.InvoiceEntity
import org.kebs.app.kotlin.apollo.store.model.ManufacturersEntity
import org.kebs.app.kotlin.apollo.store.model.PermitApplicationEntity
import org.kebs.app.kotlin.apollo.store.model.PetroleumInstallationInspectionEntity
import org.kebs.app.kotlin.apollo.store.model.invoice.BillPayments
import org.kebs.app.kotlin.apollo.store.model.invoice.BillTransactionsEntity
import org.kebs.app.kotlin.apollo.store.model.invoice.CorporateCustomerAccounts
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.math.BigDecimal
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
interface ICorporateCustomerRepository : HazelcastRepository<CorporateCustomerAccounts, Long> {
    fun findAllByCorporateIdentifier(corporateId: String?): Optional<CorporateCustomerAccounts>
    fun findAllByCorporateNameContains(corporateName: String, page: Pageable): Page<CorporateCustomerAccounts>
}

@Repository
interface IBillTransactionsEntityRepository : HazelcastRepository<BillTransactionsEntity, Long> {
    fun findAllByCorporateIdAndBillId(corporateId: Long, billId: Long): List<BillTransactionsEntity>
}


@Repository
interface IBillPaymentsRepository : HazelcastRepository<BillPayments, Long> {
    fun findAllByCorporateId(corporateId: Long?): List<BillPayments>
    fun findAllByCorporateIdAndBillNumber(corporateId: Long?,billNumber: String): Optional<BillPayments>
    @Query(value = "select sum(CASE WHEN bt.AMOUNT>0 then bt.AMOUNT else 0.0 END) TOTAL_AMOUNT from DAT_KEBS_BILL_TRANSACTIONS bt  where CORPORATE_ID=:corporateId and BILL_ID=:billId", nativeQuery = true)
    fun sumTotalAmountByCorporateIdAndBillId(@Param("corporateId")corporateId: Long?,@Param("billId")billId: Long): BigDecimal?
    fun findAllByCorporateIdAndPaymentStatusIn(corporateId: Long?, status: List<Int>): List<BillPayments>
}