package org.kebs.app.kotlin.apollo.api.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.payload.request.DemandNoteRequestForm
import org.kebs.app.kotlin.apollo.api.payload.request.DemandNoteRequestItem
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.*
import org.kebs.app.kotlin.apollo.api.ports.provided.sage.response.PaymentStatusResult
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.CdDemandNoteEntity
import org.kebs.app.kotlin.apollo.store.model.ServiceMapsEntity
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.di.CdDemandNoteItemsDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.di.CurrencyExchangeRates
import org.kebs.app.kotlin.apollo.store.model.di.DestinationInspectionFeeEntity
import org.kebs.app.kotlin.apollo.store.model.di.InspectionFeeRanges
import org.kebs.app.kotlin.apollo.store.repo.InvoiceBatchDetailsRepo
import org.kebs.app.kotlin.apollo.store.repo.di.ICfgCurrencyExchangeRateRepository
import org.kebs.app.kotlin.apollo.store.repo.di.IDemandNoteItemsDetailsRepository
import org.kebs.app.kotlin.apollo.store.repo.di.IDemandNoteRepository
import org.kebs.app.kotlin.apollo.store.repo.di.InspectionFeeRangesRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.io.InputStreamReader
import java.io.Reader
import java.math.BigDecimal
import java.math.RoundingMode
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


@Service("invoiceService")
class InvoicePaymentService(
        private val iDemandNoteRepo: IDemandNoteRepository,
        private val iDemandNoteItemRepo: IDemandNoteItemsDetailsRepository,
        private val auditService: ConsignmentDocumentAuditService,
        private val reportsDaoService: ReportsDaoService,
        private val exchangeRateRepository: ICfgCurrencyExchangeRateRepository,
        private val service: DaoService,
        private val invoiceBatchDetailsRepo: InvoiceBatchDetailsRepo,
        // Demand notes
        private val currencyExchangeRateRepository: ICfgCurrencyExchangeRateRepository,
        private val feeRangesRepository: InspectionFeeRangesRepository,
        private val daoServices: DestinationInspectionDaoServices,
        private val invoiceDaoService: InvoiceDaoService,
        private val commonDaoServices: CommonDaoServices,
        private val applicationMapProperties: ApplicationMapProperties,
        private val billingService: BillingService
) {
    final val DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    var dateTimeFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    fun invoiceDetails(demandNoteId: Long): HashMap<String, Any> {
        var map = hashMapOf<String, Any>()
        daoServices.findDemandNoteWithID(demandNoteId)?.let { demandNote ->
            map["preparedBy"] = demandNote.generatedBy.toString()
            map["datePrepared"] = demandNote.dateGenerated.toString()
            map["demandNoteNo"] = demandNote.demandNoteNumber.toString()
            map["importerName"] = demandNote.nameImporter.toString()
            map["importerAddress"] = demandNote.address.toString()
            map["importerTelephone"] = demandNote.telephone.toString()
            map["ablNo"] = demandNote.entryAblNumber.toString()
            map["totalAmount"] = demandNote.totalAmount.toString()
            map["receiptNo"] = demandNote.receiptNo.toString()
            map = reportsDaoService.addBankAndMPESADetails(map, demandNote.demandNoteNumber ?: "")
        }
        return map
    }

    fun rejectDemandNoteGeneration(cdUuid: String, demandNoteId: Long, remarks: String): Boolean {
        try {
            var consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
            val demandNote = iDemandNoteRepo.findById(demandNoteId)
            if (demandNote.isPresent) {
                val demand = demandNote.get()
                val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
                demand.status = map.invalidStatus
                demand.varField3 = "REJECTED"
                demand.varField10 = remarks
                consignmentDocument.varField10 = "Demand note rejected"
                consignmentDocument.sendDemandNote = map.invalidStatus
                consignmentDocument.status = ConsignmentApprovalStatus.UNDER_INSPECTION.code
                consignmentDocument.diProcessStatus = map.inactiveStatus
                consignmentDocument.diProcessInstanceId = null
                consignmentDocument = daoServices.updateCDStatus(consignmentDocument, ConsignmentDocumentStatus.PAYMENT_REJECTED)
                this.daoServices.updateCdDetailsInDB(consignmentDocument, null)
                this.iDemandNoteRepo.save(demand)
                this.auditService.addHistoryRecord(consignmentDocument.id!!, consignmentDocument.ucrNumber, remarks, "REJECT DEMAND NOTE", "Demand note ${demandNoteId} rejected")
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("DEMAND NOTE ERROR", ex)
        }
        return true
    }

    fun approveDemandNoteGeneration(cdUuid: String, demandNoteId: Long, supervisor: String, remarks: String): Boolean {
        try {
            var consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
            val demandNote = iDemandNoteRepo.findById(demandNoteId)
            if (demandNote.isPresent) {
                val demand = demandNote.get()
                val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
                demand.status = map.initStatus
                demand.varField3 = "APPROVED"
                demand.varField10 = remarks

                // Update CD status
                consignmentDocument = daoServices.updateCDStatus(consignmentDocument, ConsignmentDocumentStatus.PAYMENT_APPROVED)
                consignmentDocument.sendDemandNote = map.activeStatus
                this.daoServices.updateCdDetailsInDB(consignmentDocument, null)
                // Update demand note
                this.iDemandNoteRepo.save(demand)
                this.auditService.addHistoryRecord(consignmentDocument.id!!, consignmentDocument.ucrNumber, remarks, "APPROVED DEMAND NOTE", "Demand note ${demandNoteId} approved")
            }

        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("DEMAND NOTE ERROR", ex)
        }
        return true
    }

    fun updateDemandNoteCdStatus(cdUuid: String, demandNoteId: Long): Boolean {
        // Send demand note to user
        val demandNote = iDemandNoteRepo.findById(demandNoteId)
        try {
            // Update submission status
            if (demandNote.isPresent) {
                var consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
                val demand = demandNote.get()
                // Update CD status
                consignmentDocument = daoServices.updateCDStatus(
                        consignmentDocument,
                        ConsignmentDocumentStatus.PAYMENT_APPROVED
                )
                demand.varField3 = "UPDATED DEMAND NOTE STATUS"
                consignmentDocument.status = ConsignmentApprovalStatus.WAITING.code
                consignmentDocument.varField10 = "Demand Approved, awaiting payment"
                this.daoServices.updateCdDetailsInDB(consignmentDocument, null)
                // Update Demand note status
                this.iDemandNoteRepo.save(demand)
            }
        } catch (ex: Exception) {
            if (demandNote.isPresent) {
                val demand = demandNote.get()
                demand.status = 0
                demand.varField3 = "SUBMIT TO KENTRADE FAILED"
                demand.varField5 = ex.localizedMessage
                this.iDemandNoteRepo.save(demand)
            }
        }
        return true
    }

    fun generateInvoiceBatch(cdUuid: String, demandNoteId: Long): Boolean {
        KotlinLogging.logger { }.info("INVOICE GENERATION: $demandNoteId")
        try {
            val consignmentDocument = daoServices.findCDWithUuid(cdUuid)
            //Send Demand Note
            val demandNote = daoServices.findDemandNoteWithID(demandNoteId)!!
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            // Try to add transaction to current bill or generate batch payment
            val transportDetails = daoServices.findCdTransportDetails(consignmentDocument.cdTransport ?: 0)
            this.billingService.registerBillTransaction(demandNote, transportDetails.blawb, map)?.let { billTrx ->
                demandNote.paymentStatus = 1
                demandNote.receiptNo = billTrx.tempReceiptNumber
                demandNote.billId = billTrx.id
                this.iDemandNoteRepo.save(demandNote)
                KotlinLogging.logger { }.info("Added demand note to bill transaction:${demandNote.demandNoteNumber} <=> ${billTrx.tempReceiptNumber}")
            } ?: run {
                demandNote.demandNoteNumber?.let {
                    KotlinLogging.logger { }.info("Generate demand note Sage:${demandNote.demandNoteNumber}")
                    val loggedInUser = commonDaoServices.findUserByUserName(demandNote.createdBy ?: "NA")
                    // Create payment batch
                    val batchInvoiceDetail = invoiceDaoService.createBatchInvoiceDetails(loggedInUser.userName!!, it)
                    // Add demand note details to batch
                    val updateBatchInvoiceDetail = invoiceDaoService.addInvoiceDetailsToBatchInvoice(
                            demandNote,
                            applicationMapProperties.mapInvoiceTransactionsForDemandNote,
                            loggedInUser,
                            batchInvoiceDetail
                    )
                    // Find recipient of the invoice from importer details
                    val importerDetails = consignmentDocument.cdImporter?.let {
                        daoServices.findCDImporterDetails(it)
                    }
                    val myAccountDetails = InvoiceDaoService.InvoiceAccountDetails()
                    with(myAccountDetails) {
                        accountName = importerDetails?.name
                        accountNumber = importerDetails?.pin
                        currency = applicationMapProperties.mapInvoiceTransactionsLocalCurrencyPrefix
                    }
                    KotlinLogging.logger { }.info("ADD STAGING TO TABLE: $demandNoteId")
                    // Create payment on staging table
                    invoiceDaoService.createPaymentDetailsOnStgReconciliationTable(
                            loggedInUser.userName!!,
                            updateBatchInvoiceDetail,
                            myAccountDetails
                    )

                } ?: ExpectedDataNotFound("Demand note number not set")
            }
            return true
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to generate batch number", ex)
        }
        return false
    }

    fun updateDemandNoteSw(cdUuid: String, demandNoteId: Long): Boolean {
        KotlinLogging.logger { }.info("UPDATE DEMAND NOTE ON SW")
        try {
            val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
            //2. Update payment status on KenTrade
            daoServices.findDemandNoteWithID(demandNoteId)?.let {
                daoServices.sendDemandNotGeneratedToKWIS(it)
                consignmentDocument.varField10 = "DEMAND NOTE SENT TO KENTRADE, AWAITING PAYMENT"
                this.daoServices.updateCdDetailsInDB(consignmentDocument, null)
            }
            return true
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to update status", ex)
            throw ex
        }
    }

    fun sendDemandNoteStatus(demandNoteId: Long): Boolean {
        daoServices.findDemandNoteWithID(demandNoteId)?.let { demandNote ->
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            //1. Update Demand Note Status
            demandNote.swStatus = map.activeStatus
            demandNote.varField10 = "PAYMENT COMPLETED"
            val demandNoteDetails = daoServices.upDateDemandNote(demandNote)
            val consignmentDocument = daoServices.findCD(demandNoteDetails.cdId!!)
            // 2. Update CD status
            daoServices.updateCDStatus(
                    consignmentDocument,
                    ConsignmentDocumentStatus.PAYMENT_MADE
            )
        }
        return true
    }

    fun invoicePaymentCompleted(cdId: Long, demandNoteId: Long): Boolean {
        try {
            val consignmentDocument = this.daoServices.findCD(cdId)
            // 1. Send demand payment status to SW
            daoServices.findDemandNoteWithID(demandNoteId)?.let { demandNote ->
                daoServices.sendDemandNotePayedStatusToKWIS(demandNote)
            } ?: throw ExpectedDataNotFound("Demand note with $demandNoteId was not found")
            // 2. Update application status
            consignmentDocument.varField10 = "DEMAND NOTE PAID,AWAITING INSPECTION"
            // 3. Clear Payment process completed
            consignmentDocument.diProcessStatus = 0
            consignmentDocument.diProcessInstanceId = null
            consignmentDocument.status = ConsignmentApprovalStatus.UNDER_INSPECTION.code
            this.daoServices.updateCdDetailsInDB(consignmentDocument, null)
            return true
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("INVOICE UPDATE FAILED", ex)
        }
        return false
    }

    fun uploadExchangeRates(multipartFile: MultipartFile, fileType: String) {
        var separator = ','
        if ("TSV".equals(fileType)) {
            KotlinLogging.logger { }.info("TAB SEPARATED DATA")
            separator = '\t'
        } else {
            KotlinLogging.logger { }.info("COMMA SEPARATED DATA")
        }
        val loggedInUser = commonDaoServices.getLoggedInUser()
        val targetReader: Reader = InputStreamReader(ByteArrayInputStream(multipartFile.bytes))
        val exchangeRates = service.readExchangeRatesFromController(separator, targetReader)
        for (rate in exchangeRates) {
            val exchangeRateEntity = CurrencyExchangeRates()
            // Clear the old rate
            this.exchangeRateRepository.findAllByCurrencyCodeAndCurrentRate(rate.currencyCode!!, 1).forEach { oldRate ->
                oldRate.currentRate = 0
                oldRate.changeDate = Timestamp.from(Instant.now())
                oldRate.modifiedOn = Timestamp.from(Instant.now())
                oldRate.modifiedBy = loggedInUser?.userName
                this.exchangeRateRepository.save(oldRate)
            }
            // Add new rate
            with(exchangeRateEntity) {
                applicableDate = Timestamp.from(Instant.now())
                currencyCode = rate.currencyCode
                exchangeRate = rate.exchangeRate
                description = rate.description
                currentRate = 1
                status = 1
                createdBy = loggedInUser?.userName
                createdOn = Timestamp.from(Instant.now())
                modifiedOn = Timestamp.from(Instant.now())
                modifiedBy = loggedInUser?.userName
            }
            this.exchangeRateRepository.save(exchangeRateEntity)
        }
    }

    /**
     * List any active demand note transaction(approved by supervisor)
     */
    fun listTransactions(status: Int?, date: Optional<String>, transactionNo: Optional<String>, page: PageRequest): Page<CdDemandNoteEntity> {
        val demandNotes: Page<CdDemandNoteEntity>
        val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
        when {
            transactionNo.isPresent -> {
                KotlinLogging.logger { }.info("Search by transaction NO")
                demandNotes = iDemandNoteRepo.findByDemandNoteNumberContainingAndStatusOrderByIdAsc(transactionNo.get(), map.activeStatus, page)
            }
            date.isPresent -> {
                val searchDate = LocalDate.parse(date.get(), dateTimeFormat)
                val startDate = searchDate.atStartOfDay()
                val endDate = searchDate.plusDays(1).atStartOfDay()
                KotlinLogging.logger { }.info("Start Date: ${startDate}, End Date: $endDate")
                demandNotes = when (status) {
                    null -> iDemandNoteRepo.findByModifiedOnAndModifiedOnLessThanAndStatusOrderByIdAsc(dateTimeFormat.format(startDate), listOf(map.activeStatus, map.initStatus, map.workingStatus), page)
                    else -> iDemandNoteRepo.findByModifiedOnAndModifiedOnLessThanAndPaymentStatusAndStatusOrderByIdAsc(dateTimeFormat.format(startDate), status, listOf(map.activeStatus, map.initStatus, map.workingStatus), page)
                }
            }
            status != null -> {
                KotlinLogging.logger { }.info("Search by transaction status")
                demandNotes = iDemandNoteRepo.findByPaymentStatusAndStatusOrderByIdAsc(status, map.activeStatus, page)
            }
            else -> {
                KotlinLogging.logger { }.info("Search by all transactions")
                demandNotes = iDemandNoteRepo.findByStatusOrderByIdAsc(map.activeStatus, page)
            }
        }
        return demandNotes
    }

    fun getTransactionStatsOnDate(date: Optional<String>): Any? {
        val currentDate = date.orElse(dateTimeFormat.format(LocalDate.now()))
        return iDemandNoteRepo.transactionStats(currentDate)
    }

    fun paymentReceived(responseStatus: PaymentStatusResult): Boolean {
        iDemandNoteRepo.findByDemandNoteNumber(responseStatus.response?.demandNoteNo ?: "")?.let { demandNode ->
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            demandNode.paymentSource = responseStatus.response?.paymentCode
            demandNode.receiptDate = responseStatus.response?.paymentDate
            demandNode.varField5 = responseStatus.response?.additionalInfo
            demandNode.varField6 = responseStatus.response?.currencyCode
            demandNode.varField7 = responseStatus.response?.paymentDescription
            if ("00".equals(responseStatus.header?.statusCode)) {
                demandNode.paymentStatus = map.activeStatus
                demandNode.paymentDate = Timestamp.from(Instant.now())
                demandNode.receiptDate = responseStatus.response?.paymentDate
                invoiceBatchDetailsRepo.findByBatchNumber(demandNode.demandNoteNumber!!)?.let { batch ->
                    batch.receiptNumber = responseStatus.response?.paymentReferenceNo
                    batch.paymentStarted = map.activeStatus
                    batch.receiptDate = responseStatus.response?.paymentDate?.time?.let { java.sql.Date(it) }
                    invoiceBatchDetailsRepo.save(batch)
                }
            } else {
                demandNode.paymentStatus = map.invalidStatus
                invoiceBatchDetailsRepo.findByBatchNumber(demandNode.demandNoteNumber!!)?.let { batch ->
                    batch.receiptNumber = responseStatus.response?.paymentReferenceNo
                    batch.paymentStarted = map.invalidStatus
                    batch.receiptDate = responseStatus.response?.paymentDate?.time?.let { java.sql.Date(it) }
                    invoiceBatchDetailsRepo.save(batch)
                }
            }
            // Completion event
            GlobalScope.launch(Dispatchers.IO) {
                delay(100L)
                when (demandNode.paymentPurpose) {
                    PaymentPurpose.CONSIGNMENT.code -> {
                        invoicePaymentCompleted(demandNode.cdId!!, demandNode.id!!)
                    }
                    else -> {
                        KotlinLogging.logger { }.info("Unhandled payment completion ${demandNode.id}-${demandNode.paymentPurpose}")
                    }
                }
            }
        }
                ?: throw ExpectedDataNotFound("Invalid transaction reference number: ${responseStatus.response?.demandNoteNo}")

        return true
    }


    @Transactional
    fun generateDemandNoteWithItemList(
            form: DemandNoteRequestForm,
            map: ServiceMapsEntity,
            purpose: PaymentPurpose,
            user: UsersEntity
    ): CdDemandNoteEntity {
        return iDemandNoteRepo.findFirstByCdIdAndStatusIn(form.referenceId!!, listOf(map.workingStatus))
                ?.let { demandNote ->
                    var demandNoteDetails = demandNote
                    // Remove all items for update
                    val demandNoteItems = iDemandNoteItemRepo.findByDemandNoteId(demandNote.id!!)
                    for (itm in demandNoteItems) {
                        iDemandNoteItemRepo.delete(itm)
                    }
                    //Call Function to add Item Details To be attached To The Demand note
                    demandNote.totalAmount = BigDecimal.ZERO
                    demandNote.amountPayable = BigDecimal.ZERO
                    form.items?.forEach {
                        addItemDetailsToDemandNote(it, demandNoteDetails, map, form.presentment, user)
                    }
                    // Foreign CoR without Items
                    if (form.items?.isEmpty() == true) {
                        demandNoteDetails.totalAmount = form.amount.toBigDecimal()
                        demandNoteDetails.amountPayable = form.amount.toBigDecimal()
                    }
                    //Calculate the total Amount for Items In one Cd To be paid For
                    if (!form.presentment) {
                        demandNoteDetails = calculateTotalAmountDemandNote(demandNoteDetails, user, form.presentment)
                    }
                    demandNoteDetails
                }
                ?: kotlin.run {
                    var demandNote = CdDemandNoteEntity()
                    with(demandNote) {
                        cdId = form.referenceId
                        nameImporter = form.name
                        address = form.address
                        telephone = form.address
                        cdRefNo = form.referenceNumber
                        //todo: Entry Number
                        entryAblNumber = form.ablNumber
                        totalAmount = BigDecimal.ZERO
                        amountPayable = BigDecimal.ZERO
                        receiptNo = "NOT PAID"
                        product = form.product ?: "UNKNOWN"
                        rate = "UNKNOWN"
                        ucrNumber = form.ucrNumber
                        cfvalue = BigDecimal.ZERO
                        //Generate Demand note number
                        demandNoteNumber =
                                "KIMS${form.invoicePrefix}${
                                    generateRandomText(
                                            5,
                                            map.secureRandom,
                                            map.messageDigestAlgorithm,
                                            true
                                    )
                                }".toUpperCase()
                        paymentStatus = map.inactiveStatus
                        paymentPurpose = PaymentPurpose.CONSIGNMENT.code
                        dateGenerated = commonDaoServices.getCurrentDate()
                        generatedBy = commonDaoServices.concatenateName(user)
                        status = map.workingStatus
                        createdOn = commonDaoServices.getTimestamp()
                        createdBy = commonDaoServices.getUserName(user)
                    }
                    if (!form.presentment) {
                        demandNote = iDemandNoteRepo.save(demandNote)
                    }
                    //Call Function to add Item Details To be attached To The Demand note
                    form.items?.forEach {
                        addItemDetailsToDemandNote(it, demandNote, map, form.presentment, user)
                        //Calculate the total Amount for Items In one Cd Tobe paid For
//                        if (!presentment) {
//                            demandNoteUpDatingCDAndItem(it, user, demandNote)
//                        }
                    }
                    if (!form.presentment) {
                        demandNote = calculateTotalAmountDemandNote(demandNote, user, form.presentment)
                    }
                    // Foreign CoR/CoC without Items
                    if (form.items?.isEmpty() == true) {
                        demandNote.totalAmount = form.amount.toBigDecimal()
                        demandNote.amountPayable = form.amount.toBigDecimal()
                    }
                    return demandNote

                }
    }

    private fun demandNoteCalculation(
            demandNoteItem: CdDemandNoteItemsDetailsEntity,
            diInspectionFeeId: DestinationInspectionFeeEntity,
            currencyCode: String,
            documentType: String,
            itemId: Long?
    ) {
        val cfiValue = demandNoteItem.cfvalue ?: BigDecimal.ZERO
        var minimumUsd: BigDecimal? = null
        var maximumUsd: BigDecimal? = null
        var minimumKes: BigDecimal = BigDecimal.ZERO
        var maximumKes: BigDecimal = BigDecimal.ZERO
        var fixedAmount: BigDecimal = BigDecimal.ZERO
        var rate: BigDecimal? = null
        var fee: InspectionFeeRanges? = null
        //1. Handle ranges in the fee depending on amounts
        var feeType = diInspectionFeeId.rateType
        if ("RANGE".equals(diInspectionFeeId.rateType)) {
            var documentOrigin = "LOCAL"
            when (documentType) {
                "L" -> {
                    documentOrigin = "LOCAL"
                }
                "F" -> {
                    documentOrigin = "PVOC"
                }
                "A" -> {
                    documentOrigin = "AUCTION"
                }
            }
            KotlinLogging.logger { }.info("$documentOrigin CFI DOCUMENT TYPE = $currencyCode-$cfiValue")
            val feeRange = this.feeRangesRepository.findByInspectionFeeAndMinimumKshGreaterThanEqualAndMaximumKshLessThanEqual(diInspectionFeeId.id, cfiValue, documentOrigin)
            if (feeRange.isEmpty()) {
                throw Exception("Item details with Id = ${itemId}, does not Have any range For payment Fee Id Selected $cfiValue")
            } else {
                fee = feeRange.get(0)
            }
            minimumUsd = fee.minimumUsd?.let { convertAmount(it, "USD") }
            maximumUsd = fee.maximumUsd?.let { convertAmount(it, "USD") }
            minimumKes = fee.minimumKsh ?: BigDecimal.ZERO
            maximumKes = fee.maximumKsh ?: BigDecimal.ZERO
            fixedAmount = fee.fixedAmount ?: BigDecimal.ZERO
            feeType = fee.rateType
            rate = fee.rate
        } else {
            minimumUsd = diInspectionFeeId.minimumUsd?.let { convertAmount(it.toBigDecimal(), "USD") }
            maximumUsd = diInspectionFeeId.higher?.let { convertAmount(it.toBigDecimal(), "USD") }
            minimumKes = diInspectionFeeId.minimumKsh?.toBigDecimal() ?: BigDecimal.ZERO
            maximumKes = diInspectionFeeId.maximumKsh?.toBigDecimal() ?: BigDecimal.ZERO
            fixedAmount = diInspectionFeeId.amountKsh?.toBigDecimal() ?: BigDecimal.ZERO
            rate = diInspectionFeeId.rate
            feeType = diInspectionFeeId.rateType
        }
        val percentage = 100
        var amount: BigDecimal? = null
        KotlinLogging.logger { }.info("$feeType CFI AMOUNT BEFORE CALCULATION = $currencyCode-$cfiValue")
        //2. Calculate based on the ranges provided
        when (feeType) {
            "PERCENTAGE" -> {
                amount = cfiValue.multiply(rate).divide(percentage.toBigDecimal())
                demandNoteItem.amountPayable = BigDecimal(amount?.toDouble() ?: 0.0)
                KotlinLogging.logger { }.info("$feeType MY AMOUNT BEFORE CALCULATION = $currencyCode-$amount")
                //3.  APPLY MAX AND MIN VALUES Prefer USD setting to KES setting
                amount?.let {
                    when (minimumUsd) {
                        null -> {
                            if (it < minimumKes && minimumKes > BigDecimal.ZERO) {
                                amount = minimumKes
                            } else if (it > maximumKes && maximumKes < BigDecimal.ZERO) {
                                amount = maximumKes
                            }
                        }
                        else -> {
                            if (it < minimumUsd && minimumUsd > BigDecimal.ZERO) {
                                amount = minimumUsd
                            } else if (maximumUsd != null && it > maximumUsd && maximumUsd > BigDecimal.ZERO) {
                                amount = maximumUsd
                            }
                        }
                    }
                }
                demandNoteItem.adjustedAmount = amount
                KotlinLogging.logger { }.info("$feeType MY AMOUNT AFTER CALCULATION = $amount")
            }
            "FIXED" -> {
                amount = fixedAmount
                KotlinLogging.logger { }.info("FIXED AMOUNT BEFORE CALCULATION = $fixedAmount")
                demandNoteItem.adjustedAmount = fixedAmount
                demandNoteItem.amountPayable = fixedAmount
            }
            "MANUAL" -> {
                // Not-Applicable to items
                amount = BigDecimal.ZERO
                demandNoteItem.adjustedAmount = amount
                demandNoteItem.amountPayable = amount
                KotlinLogging.logger { }.info("MANUAL AMOUNT BEFORE CALCULATION = $amount")
            }
            else -> {
                amount = BigDecimal.ZERO
                demandNoteItem.adjustedAmount = amount
                demandNoteItem.amountPayable = amount
            }
        }
    }

    fun convertAmount(amount: BigDecimal?, currencyCode: String): BigDecimal {
        return currencyExchangeRateRepository.findFirstByCurrencyCodeAndApplicableDateOrderByApplicableDateDesc(currencyCode, DATE_FORMAT.format(LocalDate.now()))?.let { exchangeRateEntity ->
            return amount?.times(exchangeRateEntity.exchangeRate
                    ?: BigDecimal.ZERO) ?: BigDecimal.ZERO
        } ?: run {
            currencyExchangeRateRepository.findFirstByCurrencyCodeAndCurrentRateAndStatus(currencyCode, 1, 1)?.let { exchangeRateEntity ->
                return amount?.times(exchangeRateEntity.exchangeRate
                        ?: BigDecimal.ZERO) ?: BigDecimal.ZERO
            } ?: run {
                throw ExpectedDataNotFound("Conversion rate for currency $currencyCode not found")
            }
        }
    }

    fun convertAmount(amount: BigDecimal?, currencyCode: String, demandNoteItem: CdDemandNoteItemsDetailsEntity?) {
        currencyExchangeRateRepository.findFirstByCurrencyCodeAndApplicableDateOrderByApplicableDateDesc(currencyCode, DATE_FORMAT.format(LocalDate.now()))?.let { exchangeRateEntity ->
            demandNoteItem?.exchangeRateId = exchangeRateEntity.id
            demandNoteItem?.cfvalue = amount?.times(exchangeRateEntity.exchangeRate
                    ?: BigDecimal.ZERO) ?: BigDecimal.ZERO
        } ?: run {
            KotlinLogging.logger { }.warn("Exchange rate for today not found, using the last known exchange rate")
            currencyExchangeRateRepository.findFirstByCurrencyCodeAndCurrentRateAndStatus(currencyCode, 1, 1)?.let { exchangeRateEntity ->
                demandNoteItem?.cfvalue = amount?.times(exchangeRateEntity.exchangeRate
                        ?: BigDecimal.ZERO) ?: BigDecimal.ZERO
                demandNoteItem?.exchangeRateId = exchangeRateEntity.id
            } ?: run {
                demandNoteItem?.cfvalue = amount ?: BigDecimal.ZERO
                throw ExpectedDataNotFound("Conversion rate for currency $currencyCode not found")
            }
        }
    }

    private fun addItemDetailsToDemandNote(
            itemDetails: DemandNoteRequestItem, demandNote: CdDemandNoteEntity, map: ServiceMapsEntity,
            presentment: Boolean,
            user: UsersEntity
    ) {
        val fee = itemDetails.fee
                ?: throw Exception("Item details with Id = ${itemDetails.itemId}, does not Have any Details For payment Fee Id Selected ")
        var demandNoteItem = iDemandNoteItemRepo.findByItemIdAndDemandNoteId(itemDetails.itemId, demandNote.id)
        if (demandNoteItem == null) {
            demandNoteItem = CdDemandNoteItemsDetailsEntity()
        }
        // Apply currency conversion rates for today
        when (itemDetails.currency?.toUpperCase()) {
            "KES" -> {
                demandNoteItem.cfvalue = itemDetails.itemValue
            }
            else -> {
                convertAmount(itemDetails.itemValue, "${itemDetails.currency}", demandNoteItem)
                KotlinLogging.logger { }.warn("Exchange Rate for ${itemDetails.currency}:${itemDetails.itemValue} => ${demandNoteItem.cfvalue}")
            }
        }
        // Add extra data
        with(demandNoteItem) {
            itemId = itemDetails.itemId
            varField1 = itemDetails.quantity.toString()
            demandNoteId = demandNote.id
            product = itemDetails.productName
            rate = fee.rate?.toString()
            rateType = fee.rateType
            feeName = fee.name ?: fee.description
            // Demand note Calculation Details
            status = map.activeStatus
            createdOn = commonDaoServices.getTimestamp()
            createdBy = commonDaoServices.getUserName(user)
        }
        // Apply fee type and adjustments
        demandNoteCalculation(demandNoteItem, fee, "KES", itemDetails.route, itemDetails.itemId)
        // Skip saving for presentment
        if (!presentment) {
            iDemandNoteItemRepo.save(demandNoteItem)
            return
        }
        // Add this for presentment purposes
        demandNote.totalAmount = demandNote.totalAmount?.plus(demandNoteItem.adjustedAmount ?: BigDecimal.ZERO)
        demandNote.amountPayable = demandNote.amountPayable?.plus(demandNoteItem.amountPayable ?: BigDecimal.ZERO)
    }


    private fun calculateTotalAmountDemandNote(
            demandNote: CdDemandNoteEntity,
            user: UsersEntity,
            presentment: Boolean,
    ): CdDemandNoteEntity {
        demandNote.amountPayable = BigDecimal.ZERO
        demandNote.totalAmount = BigDecimal.ZERO
        demandNote.cfvalue = BigDecimal.ZERO
        iDemandNoteItemRepo.findByDemandNoteId(demandNote.id!!).forEach { demandNoteItem ->
            demandNote.amountPayable = demandNote.amountPayable?.plus(demandNoteItem.adjustedAmount
                    ?: BigDecimal.ZERO)
            demandNote.totalAmount = demandNote.totalAmount?.plus(demandNoteItem.amountPayable
                    ?: BigDecimal.ZERO)
            demandNote.cfvalue = demandNote.cfvalue?.plus(demandNoteItem.cfvalue
                    ?: BigDecimal.ZERO)

        }

        demandNote.cfvalue = demandNote.cfvalue?.setScale(2, RoundingMode.HALF_UP)
        demandNote.totalAmount = demandNote.totalAmount?.setScale(2, RoundingMode.HALF_UP)
        demandNote.amountPayable = demandNote.amountPayable?.setScale(2, RoundingMode.HALF_UP)

        if (presentment) {
            return demandNote
        }
        return daoServices.upDateDemandNoteWithUser(demandNote, user)
    }

}