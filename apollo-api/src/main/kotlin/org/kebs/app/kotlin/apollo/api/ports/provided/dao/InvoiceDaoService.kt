package org.kebs.app.kotlin.apollo.api.ports.provided.dao


import org.kebs.app.kotlin.apollo.api.ports.provided.sage.PostInvoiceToSageServices
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.di.CdDemandNoteItemsDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.invoice.BillPayments
import org.kebs.app.kotlin.apollo.store.model.invoice.CorporateCustomerAccounts
import org.kebs.app.kotlin.apollo.store.model.invoice.InvoiceBatchDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.qa.QaBatchInvoiceEntity
import org.kebs.app.kotlin.apollo.store.repo.*
import org.kebs.app.kotlin.apollo.store.repo.di.IDemandNoteItemsDetailsRepository
import org.kebs.app.kotlin.apollo.store.repo.di.IDemandNoteRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
class InvoiceDaoService(
        private val invoiceBatchDetailsRepo: InvoiceBatchDetailsRepo,
        private val billTransactionRepo: IBillTransactionsEntityRepository,
        private val billsRepo: IBillPaymentsRepository,
        private val invoicePaymentRepo: IStagingPaymentReconciliationRepo,
        private val iPaymentMethodsRepo: IPaymentMethodsRepository,
        private val applicationMapProperties: ApplicationMapProperties,
        private val iDemandNoteRepository: IDemandNoteRepository,
        private val iDemandNoteItemsRepository: IDemandNoteItemsDetailsRepository,
        private val commonDaoServices: CommonDaoServices
) {

    @Lazy
    @Autowired
    lateinit var diDaoServices: DestinationInspectionDaoServices

    @Lazy
    @Autowired
    lateinit var qaDaoServices: QADaoServices

    @Lazy
    @Autowired
    lateinit var postInvoiceToSageServices: PostInvoiceToSageServices

    final val appId = applicationMapProperties.mapInvoiceTransactions
    val map = commonDaoServices.serviceMapDetails(appId)

    fun createBatchInvoiceDetails(user: String, invoiceNumber: String): InvoiceBatchDetailsEntity {
        val map = commonDaoServices.serviceMapDetails(appId)
        var batchInvoice = invoiceBatchDetailsRepo.findByBatchNumber(invoiceNumber) ?: InvoiceBatchDetailsEntity()
        with(batchInvoice) {
//            batchNumber = "KEBS/INVOICE-${generateRandomText(5, map.secureRandom, map.messageDigestAlgorithm, true)}".toUpperCase()
            batchNumber = invoiceNumber
            status = map.inactiveStatus
            createdBy = user
            createdOn = commonDaoServices.getTimestamp()
        }
        batchInvoice = invoiceBatchDetailsRepo.save(batchInvoice)
        return batchInvoice
    }

    fun findInvoiceBatchDetails(batchInvoiceID: Long): InvoiceBatchDetailsEntity {
        invoiceBatchDetailsRepo.findByIdOrNull(batchInvoiceID)
                ?.let {
                    return it
                }
                ?: throw ExpectedDataNotFound("BATCH INVOICE WITH [ID = ${batchInvoiceID}], DOES NOT EXIST")
    }

    fun findInvoiceStgReconciliationDetails(referenceCode: String): StagingPaymentReconciliation {
        invoicePaymentRepo.findByReferenceCode(referenceCode)
                ?.let {
                    return it
                }
                ?: throw ExpectedDataNotFound(" INVOICE WITH [REFERENCE CODE = ${referenceCode}], DOES NOT EXIST")
    }

    fun findInvoiceStgReconciliationDetailsByID(stgID: Long): StagingPaymentReconciliation {
        invoicePaymentRepo.findByIdOrNull(stgID)
                ?.let {
                    return it
                }
                ?: throw ExpectedDataNotFound(" INVOICE WITH ID ON STAGING WITH ID = ${stgID}, DOES NOT EXIST")
    }

    fun updateInvoiceBatchDetails(
            invoiceBatchDetails: InvoiceBatchDetailsEntity,
            tableSourcePrefix: String,
            detailsDescription: String,
            user: UsersEntity,
            amount: BigDecimal,
            taxAmount: BigDecimal
    ): InvoiceBatchDetailsEntity {
        val map = commonDaoServices.serviceMapDetails(appId)
        with(invoiceBatchDetails) {
            totalAmount = amount
            totalTaxAmount = taxAmount
            description = "{DETAILS :[$detailsDescription]}"
            tableSource = tableSourcePrefix
            status = map.inactiveStatus
            modifiedBy = commonDaoServices.concatenateName(user)
            modifiedOn = commonDaoServices.getTimestamp()
        }
        return invoiceBatchDetailsRepo.save(invoiceBatchDetails)
    }

    fun updateInvoiceBatchDetails(
            invoiceBatchDetails: InvoiceBatchDetailsEntity,
            user: String
    ): InvoiceBatchDetailsEntity {
        with(invoiceBatchDetails) {
            modifiedBy = user
            modifiedOn = commonDaoServices.getTimestamp()
        }
        return invoiceBatchDetailsRepo.save(invoiceBatchDetails)
    }

    fun updateStgReconciliationTableDetails(
        invoiceStgRecoDetails: StagingPaymentReconciliation,
        user: String
    ): StagingPaymentReconciliation {
        with(invoiceStgRecoDetails) {
            modifiedBy = user
            modifiedOn = commonDaoServices.getTimestamp()
        }
        return invoicePaymentRepo.save(invoiceStgRecoDetails)
    }

    fun addInvoiceDetailsToBatchInvoice(addDetails: Any, tableSourcePrefix: String, user: UsersEntity, invoiceBatchDetails: InvoiceBatchDetailsEntity): InvoiceBatchDetailsEntity {
        val map = commonDaoServices.serviceMapDetails(appId)
        var totalAmount: BigDecimal = 0.toBigDecimal()
        var totalTaxAmount: BigDecimal = 0.toBigDecimal()
        var detailsDescription = ""

        when (tableSourcePrefix) {
            applicationMapProperties.mapInvoiceTransactionsForDemandNote -> {
                var dNote = addDetails as CdDemandNoteEntity
                with(dNote) {
                    invoiceBatchNumberId = invoiceBatchDetails.id
                    paymentStatus = map.inactiveStatus
                }
                dNote = diDaoServices.upDateDemandNoteWithUser(dNote, user)

                totalAmount = dNote.totalAmount?.let { totalAmount.plus(it) }!!
                detailsDescription = "Demand Note Number:${dNote.demandNoteNumber}"
            }
            applicationMapProperties.mapInvoiceTransactionsForMSFuelReconciliation -> {
                //Todo: Adding Function for Ms Fuel
            }
            applicationMapProperties.mapInvoiceTransactionsForPermit -> {
                var invoiceNote = addDetails as QaBatchInvoiceEntity
                with(invoiceNote) {
                    invoiceBatchNumberId = invoiceBatchDetails.id
//                    invoiceNumber = invoiceBatchDetails.batchNumber
                    paidStatus = map.inactiveStatus
                }
                invoiceNote = qaDaoServices.qaInvoiceBatchUpdateDetails(invoiceNote, user)

                totalAmount = invoiceNote.totalAmount?.let { totalAmount.plus(it) }!!
                totalTaxAmount = invoiceNote.totalTaxAmount?.let { totalTaxAmount.plus(it) }!!
                detailsDescription = "PERMIT INVOICE NUMBER:${invoiceNote.invoiceNumber}"
            }
        }

        return updateInvoiceBatchDetails(invoiceBatchDetails, tableSourcePrefix, detailsDescription, user, totalAmount,totalTaxAmount)
    }

    fun findPaymentMethodtype(paymentMethodID: Long): PaymentMethodsEntity {
        iPaymentMethodsRepo.findByIdOrNull(paymentMethodID)
                ?.let {
                    return it
                } ?: throw ExpectedDataNotFound("PAYMENT METHOD WITH [ID = $paymentMethodID], DOES NOT EXIST")
    }

    fun postRequestToSage(user: String, demandNote: CdDemandNoteEntity) {
        val map = commonDaoServices.serviceMapDetails(appId)
        postInvoiceToSageServices.postInvoiceTransactionToSage(demandNote, user, map)
        iDemandNoteRepository.save(demandNote)
        // Check if posting to sage was successful and raise error to allow retry
        if (demandNote.postingStatus != map.activeStatus) {
            throw ExpectedDataNotFound(demandNote.varField7)
        }
    }

    fun createPaymentDetailsOnStgReconciliationTable(user: String, invoiceBatchDetails: InvoiceBatchDetailsEntity, invoiceAccountDetails: InvoiceAccountDetails): StagingPaymentReconciliation {
        val map = commonDaoServices.serviceMapDetails(appId)
        var invoiceDetails = StagingPaymentReconciliation()
        with(invoiceDetails) {
            customerName = user
            statusCode = map.initStage
            statusDescription = "Initial Stage"
            additionalInformation = invoiceBatchDetails.description
            accountName = invoiceAccountDetails.accountName
            accountNumber = invoiceAccountDetails.accountNumber
            currency = invoiceAccountDetails.currency
            invoiceId = invoiceBatchDetails.id
            referenceCode = invoiceBatchDetails.batchNumber
            invoiceAmount = invoiceBatchDetails.totalAmount
            invoiceTaxAmount = invoiceBatchDetails.totalTaxAmount
            actualAmount = invoiceBatchDetails.totalAmount
            transactionDate = commonDaoServices.getCurrentDate()
            invoiceDate = commonDaoServices.getCurrentDate()
            description = "{BATCH DETAILS :${additionalInformation},BATCH NUMBER:${referenceCode},BATCH ID:${invoiceId},TOTAL AMOUNT:${invoiceAmount},TRANSACTION DATE:${transactionDate}}"
            paidAmount = BigDecimal.ZERO
            transactionId = null
            paymentTablesUpdatedStatus = null
            outstandingAmount = map.inactiveStatus.toBigDecimal()
            extras = " "
            status = map.inactiveStatus
            createdOn = commonDaoServices.getTimestamp()
            createdBy = user
        }
        invoiceDetails = invoicePaymentRepo.save(invoiceDetails)

        postInvoiceToSageServices.postInvoiceTransactionToSageQa(invoiceDetails.id ?: throw Exception("STG INVOICE CAN'T BE NULL"),invoiceAccountDetails, user, map)

        return invoiceDetails
    }

    fun updateInvoiceBatchDetailsAfterPaymentDone(invoiceBatchDetails: InvoiceBatchDetailsEntity): InvoiceBatchDetailsEntity {
        val map = commonDaoServices.serviceMapDetails(appId)
        with(invoiceBatchDetails) {
            status = map.activeStatus
            modifiedBy = "SYSTEM"
            modifiedOn = commonDaoServices.getTimestamp()
        }
        return invoiceBatchDetailsRepo.save(invoiceBatchDetails)
    }

    fun updateDemandNoteDetailsAfterPaymentDone(invoiceDetails: StagingPaymentReconciliation): Boolean {
        val map = commonDaoServices.serviceMapDetails(appId)
        invoiceDetails.invoiceId?.let {
            diDaoServices.findDemandNoteByBatchID(it).forEach { demandNote ->
                diDaoServices.demandNotePayment(demandNote, invoiceDetails, map)
            }
        }
        return true
    }


    fun updateStagingPaymentReconciliation(stagingPaymentReconciliation: StagingPaymentReconciliation): StagingPaymentReconciliation {
        val map = commonDaoServices.serviceMapDetails(appId)
        with(stagingPaymentReconciliation) {
            paymentTablesUpdatedStatus = map.inactiveStatus
            modifiedBy = "SYSTEM"
            modifiedOn = commonDaoServices.getTimestamp()
        }
        return invoicePaymentRepo.save(stagingPaymentReconciliation)
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updatePaymentDetailsToSpecificTable(updatedBatchInvoiceDetail: InvoiceBatchDetailsEntity, invoiceDetail: StagingPaymentReconciliation) {
        when (updatedBatchInvoiceDetail.tableSource) {
            applicationMapProperties.mapInvoiceTransactionsForDemandNote -> {
                updateDemandNoteDetailsAfterPaymentDone(invoiceDetail)
                updateStagingPaymentReconciliation(invoiceDetail)
            }
            applicationMapProperties.mapInvoiceTransactionsForMSFuelReconciliation -> {
                // Todo: add for fuel update details function
                updateStagingPaymentReconciliation(invoiceDetail)
            }
            applicationMapProperties.mapInvoiceTransactionsForPermit -> {
                // Todo: add for Permit update details function
                updateStagingPaymentReconciliation(invoiceDetail)
            }
        }
    }


    fun findAllInvoicesPaid(): List<StagingPaymentReconciliation>? {
        val map = commonDaoServices.serviceMapDetails(appId)
        return invoicePaymentRepo.findByPaymentTablesUpdatedStatus(map.activeStatus)
    }

    fun findDemandNoteCdId(cdId: Long): CdDemandNoteEntity? {
        return iDemandNoteRepository.findFirstByCdIdAndStatusIn(cdId, listOf(10, 1))
    }

    fun findDemandNoteItemsCdId(dnId: Long): List<CdDemandNoteItemsDetailsEntity> {
        return iDemandNoteItemsRepository.findByDemandNoteId(dnId)
    }

    fun updateOfInvoiceTables() {
        findAllInvoicesPaid()
                ?.forEach { invoice ->
                    val batchInvoiceDetail = invoice.invoiceId?.let { findInvoiceBatchDetails(it) }
                    val updatedBatchInvoiceDetail = batchInvoiceDetail?.let { updateInvoiceBatchDetailsAfterPaymentDone(it) }
                    updatedBatchInvoiceDetail?.let { updatePaymentDetailsToSpecificTable(it, invoice) }
                }
    }

    fun findBillTransactions(billId: Long): List<BillSummary> {
        return this.billTransactionRepo.sumTotalAmountByRevenueLineAndBillId(billId, 0)
    }

    fun findBillDetails(billId: Long): BillPayments? {
        return this.billsRepo.findByIdOrNull(billId)
    }

    fun postBillToSage(bill: BillPayments, user: String, map: ServiceMapsEntity, corporate: CorporateCustomerAccounts) {
        postInvoiceToSageServices.postInvoiceTransactionToSage(bill, user, corporate, map)
        billsRepo.save(bill)
        // Check if posting to sage was successful and raise error to allow retry
        if (bill.postingStatus != map.activeStatus) {
            throw ExpectedDataNotFound(bill.varField3)
        }
    }

    class InvoiceAccountDetails {

        var accountName: String? = null

        var accountNumber: String? = null

        var currency: String? = null

        var reveneCode: String? = null

        var revenueDesc: String? = null
    }

}

