package org.kebs.app.kotlin.apollo.api.controllers.diControllers.pvoc


import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.exceptions.SupervisorNotFoundException
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.repo.*
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.time.Period
import java.util.concurrent.ThreadLocalRandom
import kotlin.streams.asSequence


@Controller
@RequestMapping("/api/di/pvoc/")
class PvocMonitoringAgents(
    private val iCocsRepository: ICocsRepository,
    private val iCdSampleCollectionEntityRepo: ICdSampleCollectionEntityRepo,
    private val iPvocCoiTimelinesDataEntityRepo: IPvocCoiTimelinesDataEntityRepo,
    private val iCdItemDetailsRepo: ICdItemDetailsRepo,
    private val iDestinationInspectionRepository: IDestinationInspectionRepository,
    private val iPvocTimelinesDataRepository: IPvocTimelinesDataRepository,
    private val iPvocWaiversRemarksRepo: IPvocWaiversRemarksRepo,
    private val iUserRepository: IUserRepository,
    private val iUserRoleAssignmentsRepository: IUserRoleAssignmentsRepository,
    private val iPvocAgentMonitoringStatusEntityRepo: IPvocAgentMonitoringStatusEntityRepo,
    private val iUserRolesRepository: IUserRolesRepository,
    private val iPvocTimelineDataPenaltyInvoiceEntityRepo: PvocTimelineDataPenaltyInvoiceEntityRepo,
    private val iPvocQuerriesRepository: IPvocQuerriesRepository,
    private val rfcCocEntityRepo: IRfcCocEntityRepo,
    private val commonDaoServices: CommonDaoServices,
    private val pvocInvoicingRepository: IPvocInvoicingRepository,
    private val pvocPartnersRepository: IPvocPartnersRepository,
    private val pvocAgentContractEntityRepo: PvocAgentContractEntityRepo,
    private val pvocRevenueReportEntityRepo: PvocRevenueReportEntityRepo,
    private val pvocPenaltyInvoicingEntityRepo: PvocPenaltyInvoicingEntityRepo,
    private val iPvocCorTimelinesDataEntityRepo: IPvocCorTimelinesDataEntityRepo
//        private val iUserRoleAssignmentsRepository: IUserRoleAssignmentsRepository

) {

    // Generating random invoice numbers
    private fun generatingRandomInvoice(stringLen: String): String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return ThreadLocalRandom.current()
            .ints(stringLen.toLong(), 0, charPool.size)
            .asSequence()
            .map(charPool::get)
            .joinToString("")

    }

    @GetMapping("pvoc-monitoring-home")
    fun home(): String {
        return "destination-inspection/pvoc/monitoring/index"
    }



    @GetMapping("coc-sampled-at-entry-point")
    fun cocSampledAtEntryPoint(model: Model): String {
        val cdIds = mutableListOf<Long>()
        val cocsDocs = mutableListOf<CocsEntity>()
        iCdItemDetailsRepo.findAllByStatusAndSampledStatus(1, 1)?.let { items ->
            items.forEach { item ->
                item.consigmentId?.let { cdIds.add(it) }
            }
        }
        // get coc using ucrno
        cdIds.forEach { consignmentId ->
            iDestinationInspectionRepository.findByIdOrNull(consignmentId).let { consignment ->
                consignment?.ucrNumber?.let { ucr -> iCocsRepository.findByUcrNumber(ucr)?.let { cocsDocs.add(it) } }
            }
        }
        model.addAttribute("sampled_goods", cocsDocs)
        return "destination-inspection/pvoc/monitoring/CocSampledAtPortOfEntry"
    }

    @GetMapping("coc-sampled-at-entry-point/{id}")
    fun cocSampledAtEntryPoint(model: Model, @PathVariable("id") id: Long): String {
        iCocsRepository.findByIdOrNull(id)?.let { coc ->
            model.addAttribute("coc", coc)
            return "destination-inspection/pvoc/monitoring/CocSampledAtPortOfEntry"
        } ?: throw Exception("Coc with {id} does not exist")
    }

    @GetMapping("goods-sampled-by-market-surveillance")
    fun goodsSampledByMarketSurveillance(
        model: Model,
        @RequestParam(value = "fromDate", required = false) fromDate: String,
        @RequestParam(value = "toDate", required = false) toDate: String,
        @RequestParam(value = "filter", required = false) filter: String,
        @RequestParam(value = "currentPage", required = false) currentPage: Int,
        @RequestParam(value = "pageSize", required = false) pageSize: Int
    ): String {

        PageRequest.of(currentPage, pageSize)
            .let { page ->
                SecurityContextHolder.getContext().authentication
                    ?.let { auth ->
                        when {
                            auth.authorities.stream().anyMatch { authority -> authority.authority == "PVOC_APPLICATION_READ" || authority.authority == "PVOC_APPLICATION_PROCESS" } -> {
                                when (filter) {
                                    "filter" -> {
                                        iCdSampleCollectionEntityRepo.findAllByCountryOfOriginNotAndCocNumberIsNullAndUcrNumberIsNull("Kenya",page).let { pvocSampling ->
                                            model.addAttribute("pvocTimelines", pvocSampling)
                                        }
                                    }
                                    else -> {
                                        iCdSampleCollectionEntityRepo.findAllByCountryOfOriginNotAndCocNumberIsNullAndUcrNumberIsNull("Kenya",page).let { pvocSampling ->
                                            model.addAttribute("pvocTimelines", pvocSampling)
                                        }
                                    }
                                }
                            }
                            else -> throw SupervisorNotFoundException("Only users with the following privilege PVOC Appliaction READ or PVOC APPLICATION PROCESS, can access this page")
                        }
                        return "destination-inspection/pvoc/monitoring/GoodsSampledByMarketSurveillance"
                    } ?: throw Exception("You must be loggedIn to access this page")

            }
    }

    @GetMapping("goods-sampled-by-market-surveillance/{id}")
    fun goodsSampledByMarketSurveillanceDetail(@PathVariable("id") id: Long, model: Model) : String {
        iCdSampleCollectionEntityRepo.findByIdOrNull(id)?.let { sampleDetails ->
            model.addAttribute("coc", sampleDetails)
            model.addAttribute("monitoring_status", iPvocAgentMonitoringStatusEntityRepo.findAllByStatus(1))
            model.addAttribute("remarkData", PvocWaiversRemarksEntity())
            model.addAttribute("pvocTimelineData", PvocTimelinesDataEntity())
            return "destination-inspection/pvoc/monitoring/GoodsSampledByMarketSurveillanceDetails"
        }?: throw Exception("item with id ${id} does not exist")

    }


    @GetMapping("coc-with-timeline-issue")
    fun cocWithTimelineIssue(
        model: Model,
        @RequestParam(value = "fromDate", required = false) fromDate: String,
        @RequestParam(value = "toDate", required = false) toDate: String,
        @RequestParam(value = "filter", required = false) filter: String,
        @RequestParam(value = "currentPage", required = false) currentPage: Int,
        @RequestParam(value = "pageSize", required = false) pageSize: Int
    ): String {
        PageRequest.of(currentPage, pageSize)
            .let { page ->
                SecurityContextHolder.getContext().authentication
                    ?.let { auth ->
                        when {
                            auth.authorities.stream().anyMatch { authority -> authority.authority == "PVOC_APPLICATION_READ" || authority.authority == "PVOC_APPLICATION_PROCESS" } -> {
                                when (filter) {
                                    "filter" -> {
                                        iPvocTimelinesDataRepository.findAllByCocNumberNotNull(page).let { pvocTimelines ->
                                            model.addAttribute("pvocTimelines", pvocTimelines)
                                        }
                                    }
                                    else -> {
                                        iPvocTimelinesDataRepository.findAllByCocNumberNotNull(page).let { pvocTimelines ->
                                            model.addAttribute("pvocTimelines", pvocTimelines)
                                        }
                                    }
                                }
                            }
                            else -> throw SupervisorNotFoundException("Only users with the following privilege PVOC Appliaction READ or PVOC APPLICATION PROCESS, can access this page")
                        }
                        return "destination-inspection/pvoc/monitoring/CocWithTimelineIssue"
                    } ?: throw Exception("You must be loggedIn to access this page")

            }
    }

    @GetMapping("coi-with-timeline-issue")
    fun coiWithTimelineIssue(
        model: Model,
        @RequestParam(value = "fromDate", required = false) fromDate: String,
        @RequestParam(value = "toDate", required = false) toDate: String,
        @RequestParam(value = "filter", required = false) filter: String,
        @RequestParam(value = "currentPage", required = false) currentPage: Int,
        @RequestParam(value = "pageSize", required = false) pageSize: Int
    ): String {
        PageRequest.of(currentPage, pageSize)
            .let { page ->
                SecurityContextHolder.getContext().authentication
                    ?.let { auth ->
                        when {
                            auth.authorities.stream().anyMatch { authority -> authority.authority == "PVOC_APPLICATION_READ" || authority.authority == "PVOC_APPLICATION_PROCESS" } -> {
                                when (filter) {
                                    "filter" -> {
                                        iPvocCoiTimelinesDataEntityRepo.findAllByCoiNumberNotNull(page).let { pvocTimelines ->
                                            model.addAttribute("pvocTimelines", pvocTimelines)
                                        }
                                    }
                                    else -> {
                                        iPvocCoiTimelinesDataEntityRepo.findAllByCoiNumberNotNull(page).let { pvocTimelines ->
                                            model.addAttribute("pvocTimelines", pvocTimelines)
                                        }
                                    }
                                }
                            }
                            else -> throw SupervisorNotFoundException("Only users with the following privilege PVOC Appliaction READ or PVOC APPLICATION PROCESS, can access this page")
                        }
                        return "destination-inspection/pvoc/monitoring/CoiWithTimelinesIssue"
                    } ?: throw Exception("You must be loggedIn to access this page")

            }
    }

    @GetMapping("cor-with-timeline-issue")
    fun corWithTimelineIssue(
        model: Model,
        @RequestParam(value = "fromDate", required = false) fromDate: String,
        @RequestParam(value = "toDate", required = false) toDate: String,
        @RequestParam(value = "filter", required = false) filter: String,
        @RequestParam(value = "currentPage", required = false) currentPage: Int,
        @RequestParam(value = "pageSize", required = false) pageSize: Int
    ): String {
        PageRequest.of(currentPage, pageSize)
            .let { page ->
                SecurityContextHolder.getContext().authentication
                    ?.let { auth ->
                        when {
                            auth.authorities.stream().anyMatch { authority -> authority.authority == "PVOC_APPLICATION_READ" || authority.authority == "PVOC_APPLICATION_PROCESS" } -> {
                                when (filter) {
                                    "filter" -> {
                                        iPvocCorTimelinesDataEntityRepo.findAllByUcrNumberNotNull(page)?.let { pvocTimelines ->
                                            model.addAttribute("pvocTimelines", pvocTimelines)
                                        }
                                    }
                                    else -> {
                                        iPvocCorTimelinesDataEntityRepo.findAllByUcrNumberNotNull(page).let { pvocTimelines ->
                                            model.addAttribute("pvocTimelines", pvocTimelines)
                                        }
                                    }
                                }
                            }
                            else -> throw SupervisorNotFoundException("Only users with the following privilege PVOC Appliaction READ or PVOC APPLICATION PROCESS, can access this page")
                        }
                        return "destination-inspection/pvoc/monitoring/CorWithTimelinesIssue"
                    } ?: throw Exception("You must be loggedIn to access this page")

            }
    }

    @GetMapping("coi-with-timeline-issue/{id}")
    fun coiWithTimelineIssueDetails(model: Model, @PathVariable("id") id: Long): String {
        iPvocCoiTimelinesDataEntityRepo.findByIdOrNull(id)?.let { coi ->
            iUserRolesRepository.findByRoleNameAndStatus("MPVOC", 1)?.let { role ->
                model.addAttribute("users", UsersEntity())
                model.addAttribute("invoiceNo", generatingRandomInvoice("8"))
                model.addAttribute("orderNumber", generatingRandomInvoice("5"))
                model.addAttribute("customerNumber", generatingRandomInvoice("5"))
                model.addAttribute("penaltyInvoice", PvocPenaltyInvoicingEntity())
                iUserRoleAssignmentsRepository.findByRoleIdAndStatus(role.id, 1)
                    ?.let { it ->
                        val userList = mutableListOf<Long?>()
                        it.map {userList.add(it.userId) }
                        model.addAttribute("pmvocs", iUserRepository.findAllByIdIn(userList))
                        model.addAttribute("users", UsersEntity())
                    }
                    ?: throw Exception("Role [id=${role.id}] not found, may not be active or assigned yet")
            } ?: throw Exception("User role name does not exist")
            model.addAttribute("monitoring_status", iPvocAgentMonitoringStatusEntityRepo.findAllByStatus(1))
            model.addAttribute("coc", coi)
            model.addAttribute("cocTimeline", PvocAgentMonitoringStatusEntity())
            model.addAttribute("remarkData", PvocWaiversRemarksEntity())
            model.addAttribute("pvocTimelineData", PvocTimelinesDataEntity())
            return "destination-inspection/pvoc/monitoring/CoiWithTimelineIssueDetails"
        } ?: throw Exception("Coc Timeline data with {id} id does not exist")

    }

    @GetMapping("coc-with-timeline-issue/{id}")
    fun cocWithTimelineIssueDetails(model: Model, @PathVariable("id") id: Long): String {
        iPvocTimelinesDataRepository.findByIdOrNull(id)?.let { coc ->
            iUserRolesRepository.findByRoleNameAndStatus("MPVOC", 1)?.let { role ->
                model.addAttribute("users", UsersEntity())
                model.addAttribute("invoiceNo", generatingRandomInvoice("8"))
                model.addAttribute("orderNumber", generatingRandomInvoice("5"))
                model.addAttribute("customerNumber", generatingRandomInvoice("5"))
                model.addAttribute("penaltyInvoice", PvocPenaltyInvoicingEntity())
                model.addAttribute("enquires" , coc.cocNumber?.let { iPvocQuerriesRepository.findAllByCocNumber(it) })
                iUserRoleAssignmentsRepository.findByRoleIdAndStatus(role.id, 1)
                        ?.let { it ->
                            val userList = mutableListOf<Long?>()
                            it.map {userList.add(it.userId) }
                            model.addAttribute("pmvocs", iUserRepository.findAllByIdIn(userList))
                            model.addAttribute("users", UsersEntity())
                        }
                        ?: throw Exception("Role [id=${role.id}] not found, may not be active or assigned yet")
            } ?: throw Exception("User role name does not exist")
            coc.ucrNumber?.let {
                model.addAttribute("shipmentMode", iCocsRepository.findFirstByCocNumber(it)?.shipmentMode)
                rfcCocEntityRepo.findByUcrNumber(it).let { rfcDoc ->
                    model.addAttribute("rfc", rfcDoc)
                    rfcDoc?.partner?.let { it2 ->
                        pvocPartnersRepository.findByIdOrNull(it2).let { partnerDetails ->
                            model.addAttribute("partner", partnerDetails)
                            pvocInvoicingRepository.findFirstByPartner(it2).let { invoice ->
                                model.addAttribute("invoice", invoice)
                                val date = LocalDate.now()
                                val days: Int = Period.between(invoice?.invoiceDate?.toLocalDate(), date).days
                                if (days > 14) {
                                    val penaltyMonths : Int = ((days -14)/30)
                                    //calculate the loyalty amount for the services offered on general goods
                                    partnerDetails?.partnerName?.let { it1 -> pvocAgentContractEntityRepo.findByServiceRenderedIdAndName(2, it1.toUpperCase())
                                    }?.let { pvocAgentContract ->
                                        val applicableRoyalty = pvocAgentContract.applicableRoyalty
                                        pvocRevenueReportEntityRepo.findByCocNo(it)?.let { revenueReport ->
                                            val inpectionFee = revenueReport.inspectionFee?.toLong()
                                            val intialPenalty = inpectionFee?.times(10)
                                            val latePenalties: Double
                                            var totalPenalties = 0.0
                                            val fobValue = revenueReport.fobValue?.toDouble()
                                            var fobToKebs = 0.0
                                            val royaltyValue = applicableRoyalty?.let { it1 -> inpectionFee?.times(it1) }?: 0
                                            if (intialPenalty != null) {
                                                latePenalties = when(partnerDetails.partnerName) {
                                                    "QISJ" -> {
                                                        royaltyValue.times(1.15).times(penaltyMonths)
                                                    }
                                                    "EAA" ->{
                                                        royaltyValue.times(1.15).times(penaltyMonths)
                                                    }
                                                    "ATJ" ->{
                                                        royaltyValue.times(1.15).times(penaltyMonths)
                                                    }
                                                    else -> {
                                                        royaltyValue.times(0.10).times(penaltyMonths)
                                                    }
                                                }
                                                totalPenalties = intialPenalty.plus(latePenalties)
                                            }

                                            model.addAttribute("totalPenalty",totalPenalties )


                                            when(coc.route){
                                                "A" ->{
                                                    fobValue?.let { fobVl ->
                                                        fobToKebs = fobVl.times(0.6)
                                                        if(fobToKebs > 2700){
                                                            fobToKebs = 2700.0
                                                        }
                                                        else
                                                            if(fobToKebs < 265) {
                                                                fobToKebs = 265.0
                                                            }
                                                    }?: throw Exception("This cannot be null")
                                                }
                                                "B" -> {
                                                    fobValue?.let { fobVl ->
                                                        fobToKebs = fobVl.times(0.55)
                                                        if(fobToKebs > 2700){
                                                            fobToKebs = 2700.0
                                                        }
                                                        else
                                                            if(fobToKebs < 265) {
                                                                fobToKebs = 265.0
                                                            }
                                                    }?: throw Exception("This cannot be null")
                                                }
                                                "C" -> {
                                                    fobValue?.let { fobVl ->
                                                        fobToKebs = fobVl.times(0.35)
                                                        if(fobToKebs > 2700){
                                                            fobToKebs = 2700.0
                                                        }
                                                        else
                                                            if(fobToKebs < 265) {
                                                                fobToKebs = 265.0
                                                            }
                                                    }?: throw Exception("This cannot be null")
                                                }
                                                else -> {
                                                    fobValue?.let { fobVl ->
                                                        fobToKebs = fobVl.times(0.75)
                                                        if(fobToKebs > 2700){
                                                            fobToKebs = 2700.0
                                                        }
                                                        else
                                                            if(fobToKebs < 265) {
                                                                fobToKebs = 265.0
                                                            }
                                                    }?: throw Exception("This cannot be null")
                                                }
                                            }
                                            model.addAttribute("rfcDoc",rfcDoc )
                                            model.addAttribute("fobValue", fobToKebs )

                                        }?: throw Exception("No report associated with $it coc number")

                                    }?: throw Exception("Agent with ${partnerDetails?.partnerName} name does not exist")

                                }
                           }

                        }
                    }
                }
            }
            model.addAttribute("monitoring_status", iPvocAgentMonitoringStatusEntityRepo.findAllByStatus(1))
            model.addAttribute("coc", coc)

            model.addAttribute("cocTimeline", PvocAgentMonitoringStatusEntity())
            model.addAttribute("remarkData", PvocWaiversRemarksEntity())
            model.addAttribute("pvocTimelineData", PvocTimelinesDataEntity())
            return "destination-inspection/pvoc/monitoring/CocWithTimelineIssueDetails"
        } ?: throw Exception("Coc Timeline data with {id} id does not exist")

    }

    @PostMapping("save-generated-invoice")
    fun saveGeneratedInvoice(@RequestParam("cocId") cocId: Long, penaltyInvoice: PvocPenaltyInvoicingEntity) : String {
        SecurityContextHolder.getContext().authentication.let { n->
            iUserRepository.findByUserName(n.name).let { userDetails ->
                with(penaltyInvoice){
                    createdOn = Timestamp.from(Instant.now())
                    createdBy = userDetails?.firstName +" "+userDetails?.lastName
                    pvocPenaltyInvoicingEntityRepo.save(penaltyInvoice)
                    return "redirect:/api/di/pvoc/generate-penalty-invoice?id=${cocId}"
                }
            }

        }
    }

    @GetMapping("generate-penalty-invoice")
    fun generatePenaltyInvoice(@RequestParam("id") id:Long, model: Model) : String {
        pvocPenaltyInvoicingEntityRepo.findByCocId(id).let { invoice ->
            model.addAttribute("invoice", invoice)
            return "destination-inspection/pvoc/monitoring/PenaltyInvoice"
        }

    }

    @PostMapping("coc-timelines-recommendation-remarks/{id}")
    fun waiverReviewRemarks(@PathVariable("id") id: Long, remarkData: PvocWaiversRemarksEntity): String {
        val user = commonDaoServices.loggedInUserDetails()
        remarkData.firstName = user.firstName
        remarkData.lastName = user.lastName
        remarkData.createdBy = user.firstName + " " + user.lastName
        remarkData.createdOn = Timestamp.from(Instant.now())
        remarkData.status = 1L
        remarkData.cocTimelineId = id
        user.id?.let {  remarkData.role = iUserRolesRepository.findByIdOrNull(iUserRoleAssignmentsRepository.findByUserIdAndStatus(it, 1)?.get(0)?.roleId)?.roleName}



        iPvocWaiversRemarksRepo.save(remarkData)
        return "redirect:/api/di/pvoc/coc-with-timeline-issue/${id}"
    }


    @PostMapping("coc-timelines-hod-status-update/{id}")
    fun cocTimelineHodStatusUpdate(@PathVariable("id") id: Long, pvocTimelineData: PvocTimelinesDataEntity): String {
        iPvocTimelinesDataRepository.findByIdOrNull(id)?.let { doc ->
            doc.hodStatus = pvocTimelineData.hodStatus
            iPvocTimelinesDataRepository.save(doc)
            return "redirect:/api/di/pvoc/coc-with-timeline-issue/${id}"
        } ?: throw Exception("Pvoc Timeline document with {id} id does not exist")
    }

    @PostMapping("coc-timelines-hod-assign-mpvoc/{id}")
    fun cocTimelineHodAssignMpvoc(@PathVariable("id") id: Long, pvocTimelineData: PvocTimelinesDataEntity): String {
        iPvocTimelinesDataRepository.findByIdOrNull(id)?.let { doc ->
            doc.mpvocAgent = pvocTimelineData.mpvocAgent
            iPvocTimelinesDataRepository.save(doc)
            return "redirect:/api/di/pvoc/coc-with-timeline-issue/${id}"
        } ?: throw Exception("Pvoc Timeline document with {id} id does not exist")
    }

    @GetMapping("coc-with-no-container-seal-details/{id}")
    fun cocWithNoContainerSealDetails(model: Model, @PathVariable("id") id: Long): String {
        iCocsRepository.findByIdOrNull(id)?.let { coc ->
            model.addAttribute("coc", coc)
        } ?: throw Exception("Coc with {id} id does not exist")
        return "destination-inspection/pvoc/monitoring/CocWithNoContainerSealDetails"
    }



    @GetMapping("coc-with-no-container-seal")
    fun cocWithNoContainerSeal(
        model: Model,
        @RequestParam(value = "fromDate", required = false) fromDate: String,
        @RequestParam(value = "toDate", required = false) toDate: String,
        @RequestParam(value = "filter", required = false) filter: String,
        @RequestParam(value = "currentPage", required = false) currentPage: Int,
        @RequestParam(value = "pageSize", required = false) pageSize: Int
    ): String {
        PageRequest.of(currentPage, pageSize)
            .let { page ->
                SecurityContextHolder.getContext().authentication
                    ?.let { auth ->
                        when {
                            auth.authorities.stream().anyMatch { authority -> authority.authority == "PVOC_APPLICATION_READ" || authority.authority == "PVOC_APPLICATION_PROCESS" } -> {
                                when (filter) {
                                    "filter" -> {
                                        iCocsRepository.findAllByRouteAndShipmentSealNumbersIsNull("A", page)
                                            .let { cocs ->
                                                model.addAttribute("seals", cocs)
                                            }
                                    }
                                    else -> {
                                        iCocsRepository.findAllByRouteAndShipmentSealNumbersIsNull("A", page)
                                            .let { cocs ->
                                                model.addAttribute("seals", cocs)
                                            }
                                    }
                                }

                            }
                            else -> throw SupervisorNotFoundException("Only users with the following privilege PVOC Appliaction READ or PVOC APPLICATION PROCESS, can access this page")
                        }
                        return "destination-inspection/pvoc/monitoring/CocWithNoContainerSeal"
                    } ?: throw Exception("You must be loggedIn to access this page")

            }
    }

    @GetMapping("coc-with-wrong-stardard")
    fun cocWithWrongStandard(): String {
        return "destination-inspection/pvoc/monitoring/CocWithWrongStarndard"
    }

    @GetMapping("generate-invoice")
    fun generateInvoice(model: Model, @RequestParam("id") id: Long): String {
        model.addAttribute("coc_id", id)
        model.addAttribute("pvoc_letter", PvocTimelineDataPenaltyInvoiceEntity())
        return "destination-inspection/pvoc/monitoring/InvoiceLetter"
    }

    @GetMapping("generate-penalty")
    fun generatePenalty(model: Model, @RequestParam("id") id: Long): String {
        model.addAttribute("coc_id", id)
        model.addAttribute("pvoc_letter", PvocTimelineDataPenaltyInvoiceEntity())
        return "destination-inspection/pvoc/monitoring/PenaltyLetter"
    }

    @GetMapping("generate-warning-letter")
    fun generateWarningLetter(model: Model, @RequestParam("id") id: Long): String {
        model.addAttribute("coc_id", id)
        model.addAttribute("pvoc_letter", PvocTimelineDataPenaltyInvoiceEntity())
        return "destination-inspection/pvoc/monitoring/PenaltyLetter"
    }

    @GetMapping("pvoc-query-details/{id}")
    fun pvocQueryDetails(@PathVariable("id") id: Long, model: Model): String {
        iPvocQuerriesRepository.findByIdOrNull(id)?.let { coc ->
            coc.cocNumber?.let {
                rfcCocEntityRepo.findByCocNumber(it).let { rfcDoc ->
                    model.addAttribute("rfc", rfcDoc)
                    rfcDoc?.partner?.let { it2 ->
                        pvocPartnersRepository.findByIdOrNull(it2).let { partnerDetails ->
                            model.addAttribute("partner", partnerDetails)
                            model.addAttribute("invoice", pvocInvoicingRepository.findFirstByPartner(it2))
                        }
                    }
                }
            }
            model.addAttribute("coc", coc.cocNumber?.let { iPvocTimelinesDataRepository.findByCocNumber(it) })
            model.addAttribute("queryDetails", coc)
            return "destination-inspection/pvoc/monitoring/PvocQueryDetails"
        } ?: throw Exception("Query with $id id does not exist")
    }

    @PostMapping("save-pvoc-monitoring-letter")
    fun savePvocMonitoringLetter(pvoc_letter: PvocTimelineDataPenaltyInvoiceEntity): String {
        SecurityContextHolder.getContext().authentication.name.let { username ->
            iUserRepository.findByUserName(username)?.let { user ->
                pvoc_letter.createdBy = user.firstName + ' ' + user.lastName
                pvoc_letter.createdOn = Timestamp.from(Instant.now())
                iPvocTimelineDataPenaltyInvoiceEntityRepo.save(pvoc_letter)
                return "redirect:/api/di/pvoc/coc-with-timeline-issue/${pvoc_letter.cocId}"
            } ?: throw Exception("You must be logged to make the request")
        }
    }

    @PostMapping("save-kebs-query")
    fun saveKebsQuery(@RequestParam("cocId") cocId: Long, pvocQuery: PvocQueriesEntity): String {
        SecurityContextHolder.getContext().authentication.name.let { username ->
            iUserRepository.findByUserName(username).let { userDetails ->
                pvocQuery.createdBy = userDetails?.firstName + ' ' + userDetails?.lastName
                pvocQuery.status = 1
                pvocQuery.partnerResponceAnalysisStatus = 0
                pvocQuery.kebsReplyReplyStatus = 0
                pvocQuery.pvocAgentReplyStatus = 0
                pvocQuery.createdOn = Timestamp.from(Instant.now())
                iPvocQuerriesRepository.save(pvocQuery)
                return "redirect:/api/di/pvoc/coc-with-timeline-issue/${cocId}"
            }
        }
    }

    @PostMapping("save-agent-query-reply")
    fun saveAgentQueryReply(@RequestParam("pvocQueryId") pvocQueryId: Long, pvocQuery: PvocQueriesEntity): String {
        iPvocQuerriesRepository.findByIdOrNull(pvocQueryId)?.let { kebsQuery ->
            kebsQuery.partnerResponse = pvocQuery.partnerResponse
            kebsQuery.pvocAgentReplyStatus = 1
            iPvocQuerriesRepository.save(kebsQuery)
            return "redirect:/api/di/pvoc/pvoc-query-details/${pvocQueryId}"
        } ?: throw Exception("Query with $pvocQueryId id does not exist")
    }

    @PostMapping("save-kebs-query-reply")
    fun saveKebsQueryReply(@RequestParam("pvocQueryId") pvocQueryId: Long, pvocQuery: PvocQueriesEntity): String {
        iPvocQuerriesRepository.findByIdOrNull(pvocQueryId).let { kebsQuery ->
            pvocQuery.createdBy = kebsQuery?.createdBy
            pvocQuery.kebsReplyReplyStatus = 1
            iPvocQuerriesRepository.save(pvocQuery)
            return "redirect:/api/di/pvoc/pvoc-query-details/${pvocQueryId}"
        }
    }

    @PostMapping("save-partner-response-analysis")
    fun savePartnerResponseAnalysis(@RequestParam("pvocQueryId") pvocQueryId: Long, pvocQuery: PvocQueriesEntity): String {
        iPvocQuerriesRepository.findByIdOrNull(pvocQueryId)?.let { kebsQuery ->
            kebsQuery.partnerResponseAnalysis = pvocQuery.partnerResponseAnalysis
            kebsQuery.pvocAgentReplyStatus = 1
            iPvocQuerriesRepository.save(kebsQuery)
            return "redirect:/api/di/pvoc/pvoc-query-details/${pvocQueryId}"
        } ?: throw Exception("An error occured")
    }

    @PostMapping("save-query-conclusion")
    fun saveQueryConclusion(@RequestParam("pvocQueryId") pvocQueryId: Long, pvocQuery: PvocQueriesEntity): String {
        iPvocQuerriesRepository.findByIdOrNull(pvocQueryId)?.let { kebsQuery ->
            kebsQuery.conclusion = pvocQuery.conclusion
            kebsQuery.conclusionStatus = 1
            iPvocQuerriesRepository.save(kebsQuery)
            return "redirect:/api/di/pvoc/pvoc-query-details/${pvocQueryId}"
        } ?: throw Exception("An error occured")
    }
}

