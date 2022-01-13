package org.kebs.app.kotlin.apollo.api.controllers.diControllers.pvoc

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.PvocDaoServices
import org.kebs.app.kotlin.apollo.store.model.invoice.PvocInvoicingEntity
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocReconciliationReportEntity
import org.kebs.app.kotlin.apollo.store.model.StagingPaymentReconciliation
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.repo.*
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.sql.Date
import java.sql.Timestamp
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.abs

class ReconciliationSummary{
    var totalVerificationFee : Long? = 0
    var totalInspectionFee : Long? = 0
    var totalRoyalty : Long? = 0
    var diff : Long? = 0
}


@Controller
@RequestMapping("/api/di/pvoc/")
class Reconciliation(
    private val pvocReconciliationReportEntityRepo: PvocReconciliationReportEntityRepo,
    private val pvocAgentContractEntityRepo: PvocAgentContractEntityRepo,
    private val iCocsRepository: ICocsRepository,
    private val iPvocPartnersRepository: IPvocPartnersRepository,
    private val pvocPartnersRepository: IPvocPartnersRepository,
    private val iPvocInvoicingRepository: IPvocInvoicingRepository,
    private val pvocDaoServices: PvocDaoServices,
    private val iUserRepository: IUserRepository,
    private val stgPaymentReconciliationEntityRepo: StgPaymentReconciliationEntityRepo
) {
    val map = hashMapOf<String, Any>()


    @GetMapping("all-partners")
    fun getAllThePartners(model: Model) : String{
        iPvocPartnersRepository.findAllByStatus(1).let { partners ->
            model.addAttribute("partners", partners)
            return "destination-inspection/pvoc/reconsiliation/Partners"
        }
    }


    @GetMapping("summary-report/{id}")
    fun getPartnersSummaryReportForMonth(model: Model, @PathVariable("id") id: Long
    ): String {
//        val today = Date()
//        val cal = Calendar.getInstance()
//        cal.time = today
//        cal.set(Calendar.MONTH, month - 1)
//        cal.set(Calendar.DAY_OF_MONTH, 1)
//        val cal2 = Calendar.getInstance()
//        cal2.time = today
//        cal2.set(Calendar.MONTH, month - 1)
//        cal2.set(Calendar.DAY_OF_MONTH, 1)
//        cal2.add(Calendar.MONTH, 1)
//        cal2.add(Calendar.DAY_OF_MONTH, -1)
//
//        val firstDayOfTheMonth = cal.time
//        val lastDayOfMonth = cal2.time
//
//        val sdf: DateFormat = SimpleDateFormat("yyyy-MM-dd")
//        val dateFrom = Date.valueOf(sdf.format(firstDayOfTheMonth))
//        val dateTo = Date.valueOf(sdf.format(lastDayOfMonth))
////            val dateFromstr = "2021-01-13"
////            val dateTostr = "2021-01-15"
//        val summaryReport = ReconciliationSummary()
//            pvocReconciliationReportEntityRepo.getPvocReconciliationReportDto( dateFrom.toString(), dateTo.toString())?.let { report ->
//                summaryReport.totalVerificationFee = report.verificationfeesum
//                summaryReport.totalInspectionFee = report.inspectionfeesum
//                summaryReport.diff = summaryReport.totalVerificationFee?.let { summaryReport.totalInspectionFee?.minus(it) }
//                summaryReport.totalRoyalty = report.royaltiestokebssum
//                model.addAttribute("summaryReport", summaryReport)
//                return "destination-inspection/pvoc/reconsiliation/PartnerSummary"
//            }?: throw Exception("No data found")
        pvocPartnersRepository.findByIdOrNull(id)?.let { partner ->

            pvocReconciliationReportEntityRepo.getSummaryByMonth( ).let { report ->
                model.addAttribute("reports", report)
                model.addAttribute("partner", partner)
                return "destination-inspection/pvoc/reconsiliation/PartnerSummary"
            }
        }?: throw Exception("Partner does not exist")
    }

    @GetMapping("summary-report-details/{id}")
    fun summaryReportDetails(@PathVariable("id") id: Long, model: Model) : String{
        pvocPartnersRepository.findByIdOrNull(id)?.let { partner ->
            val dateFromstr = "2021-01-13"
            val dateTostr = "2021-01-15"
            val summaryReport = ReconciliationSummary()
            pvocReconciliationReportEntityRepo.getPvocReconciliationReportDto(dateFromstr, dateTostr ).let { report ->
                model.addAttribute("reports", report)
                model.addAttribute("partner", partner)
                return "destination-inspection/pvoc/reconsiliation/PartnerSummaryDetails"
            }
        }?: throw Exception("Partner does not exist")
    }



    fun getCurrentUser(): UsersEntity? {
        SecurityContextHolder.getContext().authentication.name.let { uname ->
            iUserRepository.findByUserName(uname).let { user ->
                return user
            }
        }
    }


    fun reportsByMonth(month: Int, currentPage: Int, pageSize: Int, model: Model) {

        // filter by reports by months
        when (month) {
            in 1..12 -> {
                val today = Date()
                val cal = Calendar.getInstance()
                cal.time = today
                cal.set(Calendar.MONTH, month - 1)
                cal.set(Calendar.DAY_OF_MONTH, 1)

                val cal2 = Calendar.getInstance()
                cal2.time = today
                cal2.set(Calendar.MONTH, month - 1)
                cal2.set(Calendar.DAY_OF_MONTH, 1)
                cal2.add(Calendar.MONTH, 1)
                cal2.add(Calendar.DAY_OF_MONTH, -1)

                val firstDayOfTheMonth = cal.time
                val lastDayOfMonth = cal2.time

                val sdf: DateFormat = SimpleDateFormat("yyyy-MM-dd")
                val dateFrom = Date.valueOf(sdf.format(firstDayOfTheMonth))
                val dateTo = Date.valueOf(sdf.format(lastDayOfMonth))

                PageRequest.of(currentPage, pageSize).let { page ->
                    pvocReconciliationReportEntityRepo.findAllByStatusAndReviewedAndCreatedOnBetween(1, 0, dateFrom, dateTo, page).let { reports ->
                        model.addAttribute("reports", reports)
                    }
                }
            }
            else -> {
                PageRequest.of(currentPage, pageSize).let { page ->
                    pvocReconciliationReportEntityRepo.findAllByStatusAndReviewedOrReviewedIsNull(1, 0, page).let { reports ->
                        model.addAttribute("reports", reports)
                    }
                }
            }
        }
    }

    fun generateReportsMonthly() {

        val currentdate = LocalDate.now()
        val todaysDayOfMonth: Int = currentdate.dayOfMonth
//        val todaysDayaOfMonth : Int = currentdate.monthValue
        val reportGenerationDate = 14
        KotlinLogging.logger {}.info { "Day of month $todaysDayOfMonth" }
        when (todaysDayOfMonth) {
            reportGenerationDate -> {
                iCocsRepository.findAllByReportGenerationStatus(0).let { cocs ->
                    val cocsTotal: Int = cocs.count()
                    for (i in 0 until cocsTotal) {
                        val coc = cocs[i]
                        KotlinLogging.logger {}.info { "coc loop  " + coc.cocNumber + "partiner" + coc.pvocPartner }
                        val pvocReconciliationReportEntity = PvocReconciliationReportEntity()
                        with(pvocReconciliationReportEntity) {
                            route = coc.route
                            routeType = "Route Type " + coc.route
                            kecNo = "Not Provided"
                            idfNo = coc.idfNumber
                            exmpoterName = coc.exporterName
                            impoterName = coc.importerName
                            countryOfSupply = coc.countryOfSupply
                            insuanceDate = coc.cocIssueDate.toString()
                            sealNo = coc.shipmentSealNumbers
                            certificateNo = coc.cocNumber
                            createdOn = Timestamp.from(Instant.now())
                            createdBy = "Admin"
                            status = 1
                            reviewed = 0
                            certificateNo = coc.cocNumber
                            fob = coc.finalInvoiceCurrency
                            fobValue = coc.finalInvoiceFobValue
                            verificationFee = coc.route?.let { calculateVerificationFee(coc.finalInvoiceFobValue.toDouble(), it)?.toLong() }
                            inspectionFee = verificationFee?.let { calculateInspectionFee(it) }
                            royaltiesToKebs = try {
                                coc.cocNumber?.let { calculateRoyaltyToKebs(it, coc.finalInvoiceFobValue) }
                            } catch (e: Exception) {
                                KotlinLogging.logger { }.error { e.printStackTrace() }
                                -1
                            }

                            diff = verificationFee?.let { inspectionFee?.let { it1 -> calculateDifference(it, it1) } }
                            test = verificationFee?.let { inspectionFee?.let { it1 -> checkEquality(it, it1) } }
                        }
                        pvocReconciliationReportEntityRepo.save(pvocReconciliationReportEntity)
                        coc.reportGenerationStatus = 1
                        coc.createdBy = "Admin"
                        coc.modifiedOn = Timestamp.from(Instant.now())
                        iCocsRepository.save(coc)
                    }
//                    }
                }
            }
            else -> {
                print("Waiting for second day of the month to come")

            }
        }
    }

    //get coc reconciliation report and calculate the royalties
    @GetMapping("all-reports")
    fun allReportsMonthly(model: Model,
                          @RequestParam(value = "currentPage", required = false) currentPage: Int,//
                          @RequestParam(value = "pageSize", required = false) pageSize: Int,
                          @RequestParam(value = "month", required = false) month: Int
    ): String {
        generateReportsMonthly()
//        val dateFrom = Date.valueOf(LocalDate.parse(fromDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")))
//        val dateTo = Date.valueOf(LocalDate.parse(toDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")))
        reportsByMonth(month, currentPage, pageSize, model)
        return "destination-inspection/pvoc/reconsiliation/ReconcialiationReports"
    }

    @GetMapping("all-reports/{id}")
    fun reportById(@PathVariable("id") id: Long, model: Model): String {
        pvocReconciliationReportEntityRepo.findByIdOrNull(id).let { report ->
            report?.certificateNo?.let {
                iCocsRepository.findFirstByCocNumber(it).let { cocDetails ->
                    iPvocPartnersRepository.findByIdOrNull(cocDetails?.pvocPartner).let { partner ->
                        val discount = 0
                        model.addAttribute("discount", discount)
                        model.addAttribute("partner", partner)
                        model.addAttribute("invoiceNo", generateRandomNumbers("KEBS/PVOC"))
                        model.addAttribute("orderNumber", generateRandomNumbers("ORD"))
                        model.addAttribute("customerNumber", generateRandomNumbers("FOR"))
                        model.addAttribute("reconsiliationInvoice", PvocInvoicingEntity())
                    }
                }
            }
            model.addAttribute("coc", report)
            var saveReason = "invoice"
            when(report?.invoiced){
                1 -> {
                    report.id.let {
                        iPvocInvoicingRepository.findByReconcialitionId(it)?.let { invoice ->
                            report.certificateNo?.let { it1 -> invoice.partner?.let { it2 -> calculatePenalty(it2, it1,model ) } }
                            val balance = report.royaltiesToKebs?.minus(invoice.totalAmount)
                            if (balance != null) {
                                saveReason = if (balance > 0){
                                    "supplementary"
                                }else{
                                    "invoice"
                                }
                            }
                            model.addAttribute("paidAmoutnCheck", balance)
                            model.addAttribute("invoice", invoice)
                        }
                    }
            }


            }
            model.addAttribute("saveReason", saveReason)
            return "destination-inspection/pvoc/reconsiliation/ReconcialiationReportDetails"
        }
    }

    fun culculatingDaysBetweenDates(fromDate: Date): Int {
        val aDate = fromDate.toLocalDate()
        val todaysDate = LocalDate.now()
        val period: Period = Period.between(aDate, todaysDate)
        return abs(period.days)
    }

    fun generateRandomNumbers(symbol: String): String{
        val randomPIN = (Math.random() * 90000).toInt() + 100000
        return "${symbol}-" + randomPIN
    }

    fun getDateTime(s: Timestamp?): String? {
        return try {
            val sdf = SimpleDateFormat("MM/dd/yyyy")
            sdf.format(s)
        } catch (e: Exception) {
            e.toString()
        }
    }

    @PostMapping("save-generated-reconciliation-invoice/{id}/{saveReason}")
    fun saveGeneratedInvoice(@PathVariable("id") id: Long, @PathVariable("saveReason") saveReason : String, reconsiliationInvoice: PvocInvoicingEntity) : String{
        with(reconsiliationInvoice){
            currency = "USD"
            accountName = "Kenya Burea of Standards"
            bankCode = 12003
            bankName = "National Bank Of Kenya"
            kebsAccountNumber = "0100-302-830-604"
            usdAccountNumber =  "0200-302-830-600"
            branch = "Harambee Avenue"
            swiftCode = "NBKEKENXXXX"
            vatNumber = "0130253A"
            pinNumber ="P051092837Y"
            reason = saveReason
            createdBy = getCurrentUser()?.firstName +" "+getCurrentUser()?.lastName
            createdOn = Timestamp.from(Instant.now())
            val invoice = iPvocInvoicingRepository.save(reconsiliationInvoice)

            val stgPaymentReconciliationEntity = StagingPaymentReconciliation()
            with(stgPaymentReconciliationEntity){
//                get the customer name from the invoice
                iPvocPartnersRepository.findByIdOrNull(invoice.partner)?.let { partner ->
                    customerName = partner.partnerName
                }
                statusCode = "00"
                statusDescription = "Initial Stage"
                totalAmount = invoice.totalAmount
                additionalInformation= "This is can be paid using cheque or M-pesa"
                accountName = invoice.accountName
                accountNumber = invoice.kebsAccountNumber
                currency = invoice.currency
                invoiceId = invoice.id
                transactionDate = Date.valueOf(LocalDate.parse(invoice.invoiceDate.toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                invoiceAmount = invoice.totalAmount.toBigDecimal()
                invoiceDate = Date.valueOf(LocalDate.parse(invoice.invoiceDate.toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                invoiceNumber = invoice.invoiceNumber
                referenceCode = invoice.invoiceNumber
                createdOn = Timestamp.from(Instant.now())
                createdBy = invoice.createdBy
                paidAmount = 0.00000000.toBigDecimal()
                transactionId = 0.toString()
                outstandingAmount = 0.0000.toBigDecimal()
                extras = "Extras"
                description = invoice.description
                status = 0


//                reponseDate = ""
//                billReferenceCode = ""
                stgPaymentReconciliationEntityRepo.save(stgPaymentReconciliationEntity)
            }
            pvocReconciliationReportEntityRepo.findByIdOrNull(id)?.let { report ->
                report.invoiced = 1
                createdOn = report.createdOn
                createdBy = report.createdBy
                pvocReconciliationReportEntityRepo.save(report)
            }
            pvocDaoServices.sendInvoice(id)
            return "redirect:/api/di/pvoc/all-reports/${id}"
        }
    }


    @GetMapping("view-generated-invoice/{id}")
    fun viewGeneratedInvoice(@PathVariable("id") id: Long, model: Model): String{
        iPvocInvoicingRepository.findByReconcialitionId(id)?.let { invoice ->
            model.addAttribute("invoice", invoice)
            return "destination-inspection/pvoc/reconsiliation/ReconciliationInvoice"
        }?: throw Exception("Not found")
    }

    fun calculateVerificationFee(fobValue: Double, route: String): Double? {
        return when (route) {
            "A" -> {
                fobValue.times(0.6) / 100
            }
            "B" -> {
                fobValue.times(0.55) / 100
            }
            "C" -> {
                fobValue.times(0.35) / 100
            }
            else -> {
                fobValue.times(0.75) / 100
            }
        }
    }

    fun calculateInspectionFee(verificationFee: Long): Long {
        if (verificationFee > 2700) {
            return 2700
        } else
            if (verificationFee < 265) {
                return 265
            }
        return verificationFee
    }

    fun calculateDifference(chargeForVerification: Long, inspectionFee: Long): Long {
        return chargeForVerification - inspectionFee
    }

    fun checkEquality(chargeForVerification: Long, inspectionFee: Long): String? {
        if (chargeForVerification == inspectionFee) {
            return "True"
        }
        return "False"
    }

    fun calculateChargeForVerification(inspectionFee: Long): Long {
        return when {
            inspectionFee > 2700 -> {
                2700
            }
            inspectionFee < 265 -> {
                265
            }
            else -> {
                inspectionFee
            }
        }
    }

    fun calculateRoyaltyToKebs(certificateNo: String, fobValue: Double): Long? {
        //find coc document details
        iCocsRepository.findFirstByCocNumber(certificateNo)?.let { cocDetails ->
            pvocPartnersRepository.findByIdOrNull(cocDetails.pvocPartner).let { partinerDetails ->
                partinerDetails?.id?.let {
                    pvocAgentContractEntityRepo.findByServiceRenderedIdAndPvocPartner(2, it)?.let { contract ->
                        KotlinLogging.logger { }.info { "royalty applicable ==> " + contract.applicableRoyalty }
                        return contract.applicableRoyalty?.let { it1 -> fobValue.times(it1) }?.toLong()
                    } ?: throw Exception("recond not found")
                }
            }
        } ?: throw Exception("An error occurred please try again later")
    }

    fun calculatePenalty(partnerId: Long, certNo: String, model: Model?) {
        iPvocInvoicingRepository.findFirstByPartner(partnerId).let { invoice ->
            val date = LocalDate.now()
            val days: Int = Period.between(invoice?.invoiceDate?.toLocalDate(), date).days
            if (days > 1) {
                val penaltyMonths: Int = ((days - 14) / 30)
                iPvocPartnersRepository.findByIdOrNull(partnerId).let { partnerDetails ->
                    //calculate the loyalty amount for the services offered on general goods
                    partnerDetails?.partnerName?.let { it1 ->
                        pvocAgentContractEntityRepo.findByServiceRenderedIdAndName(2, it1.toUpperCase())
                    }?.let { pvocAgentContract ->
                        val applicableRoyalty = pvocAgentContract.applicableRoyalty
                        pvocReconciliationReportEntityRepo.findByCertificateNo(certNo)?.let { revenueReport ->
                            val inpectionFee = revenueReport.inspectionFee
                            val intialPenalty = inpectionFee?.times(10)
                            val totalPenalties: Double
                            val royaltyValue = applicableRoyalty?.let { it1 -> inpectionFee?.times(it1) }?: 0
                            if (intialPenalty != null) {
                                val latePenalties = when (partnerDetails.partnerName) {
                                    "QISJ" -> {
                                        royaltyValue.times(1.15).times(penaltyMonths)
                                    }
                                    "EAA" -> {
                                        royaltyValue.times(1.15).times(penaltyMonths)
                                    }
                                    "ATJ" -> {
                                        royaltyValue.times(1.15).times(penaltyMonths)
                                    }
                                    else -> {
                                        royaltyValue.times(0.10).times(penaltyMonths)
                                    }
                                }
                                totalPenalties = intialPenalty.plus(latePenalties)
                                model?.addAttribute("penalty", totalPenalties)
                            }

                        } ?: throw Exception("Report does not exist")
                    }
                } ?: throw Exception("Partner does not exist")

            }

        }

    }
}




