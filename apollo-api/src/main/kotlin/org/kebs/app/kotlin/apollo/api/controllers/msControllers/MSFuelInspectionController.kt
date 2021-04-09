package org.kebs.app.kotlin.apollo.api.controllers.msControllers

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.MarketSurveillanceBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.QualityAssuranceBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.RegistrationDaoServices
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.repo.*
import org.kebs.app.kotlin.apollo.store.repo.ms.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.ResourceUtils
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.sql.Date
import java.sql.Timestamp
import java.time.Instant
import java.util.*

@Controller
@RequestMapping("/api/ms")
class MSFuelInspectionController(
        private val installationInspectionRepository: IPetroleumInstallationInspectionRepository,
        private val userRepository: IUserRepository,
        private val serviceMapsRepo: IServiceMapsRepository,
        private val qualityAssuranceBpmn: QualityAssuranceBpmn,
        private val iFuelInspectionRepo: IFuelInspectionRepository,
        private val iFuelRemediationInvoiceRepo: IFuelRemediationInvoiceRepository,
        private val iFuelRemediationInvoiceChargesRepo: IFuelRemediationChargesRepository,
        private val iFuelRemediationRepo: IFuelRemediationRepository,
        private val iFuelInspectionOfficerRepo: IFuelInspectionOfficerRepository,
        private val invoiceRepo: IInvoiceRepository,
        private val marketSurveillanceBpmn: MarketSurveillanceBpmn,
        private val countiesRepo: ICountiesRepository,
        private val notifications: Notifications,
        private val townsRepo: ITownsRepository,
        private val divisionsRepo: IDivisionsRepository,
        private val departmentsRepo: IDepartmentsRepository,
        private val userProfilesRepo: IUserProfilesRepository,
        private val directorateRepo: IDirectoratesRepository,
        private val designationRepo: IDesignationsRepository,
        private val sectionsRepo: ISectionsRepository,
        private val subRegionsRepo: ISubRegionsRepository,
        private val regionsRepository: IRegionsRepository,
        private val installationInspectionDataRepository: IPetroleumFieldInspectionDataRepository,
        private val registrationDaoServices: RegistrationDaoServices,
        private val msReportsControllers: MSReportsControllers,
        applicationMapProperties: ApplicationMapProperties,
        private val serviceMapsRepository: IServiceMapsRepository
) {
    final var appId: Int? = null
    final var redirectSiteFuelList = "redirect:/api/ms/all-fuels?currentPage=0&pageSize=10&listView=allFuel"
    var redirectSiteFuelPage = "redirect:/api/ms/fuel-detail?fuelInspectId="
//    /api/ms/all-workPlans?createdWorkPlanID=26&listView=allWorkPlan
//    redirectSiteWorkPlan+"${workPlanEntity.workplanYearId}"+"&listView=allWorkPlan"
final var redirectSiteWorkPlan = "redirect:/api/ms/all-workPlans?createdWorkPlanID="

    private final val designationID: Long = 81
    private final val directorDermyValueID: Long = 0
    private final val activeStatus: Int = 1
    private final val remunerationChargesId: Long = 1
    private final val percentage: Long = 100
    private final val subsistenceChargesId: Long = 2
    private final val transportAirTicketsChargesId: Long = 3

    init {
        appId = applicationMapProperties.mapMarketSurveillance
    }

    @PreAuthorize("hasAuthority('EPRA')")
    @PostMapping("/fuel/new/save")
    fun saveFuel(
            model: Model,
            @ModelAttribute("fuelEntity") fuelEntity: MsFuelInspectionEntity,

//            @SessionAttribute("user") usersEntity: UsersEntity,
            results: BindingResult,
            redirectAttributes: RedirectAttributes
    ): String {
        SecurityContextHolder.getContext().authentication?.name
                ?.let { username ->
                    userRepository.findByUserName(username)
                            ?.let { loggedInUser ->
                                serviceMapsRepo.findByIdAndStatus(appId, activeStatus)
                                        ?.let { map ->

                                            var fuel = fuelEntity

                                            with(fuel) {
                                                referenceNumber = "FL-${generateRandomText(5, map.secureRandom, map.messageDigestAlgorithm, true)}".toUpperCase()
                                                countyId = registrationDaoServices.assignCounty(confirmCountyId)
                                                regionId = registrationDaoServices.assignRegion(countyId?.regionId)
                                                townsId = registrationDaoServices.assignTown(confirmTownsId)
                                                transactionDate = Date(Date().time)
                                                status = map.initStatus
                                                createdBy = loggedInUser.firstName + " " + loggedInUser.lastName
                                                createdOn = Timestamp.from(Instant.now())
                                            }
                                            fuel = iFuelInspectionRepo.save(fuel)
                                            KotlinLogging.logger { }.trace("MS FUEL saved with id =[${fuel.id}] ")


                                            /**
                                             * TODO: Lets discuss to understand better what how to assign HOD to a complaint is it based on Region or Randomly
                                             */

                                            KotlinLogging.logger { }.info { "Start MS PROCESS BY Finding Region" }
                                            fuel.regionId
                                                    ?.let { region ->
                                                        KotlinLogging.logger { }.info { "Start MS PROCESS region ID = $region " }
                                                        val userDETAILS = findRegionUserId(designationID, map.activeStatus, region)
//                                                            val recipient = userDETAILS?.email
//                                                            val subject = "Fuel Monitoring Schedule"
//                                                            val messageBody = "Check The System For Fuel Monitoring Schedule and Appoint an officer to be part of the joint operation"

                                                        userDETAILS?.id?.let {
                                                            userDETAILS.email?.let { userEmail ->
                                                                fuel.stationOwnerEmail?.let { stationOwnerEmail ->
                                                                    marketSurveillanceBpmn.startMSFuelMonitoringProcess(fuel.id, it, stationOwnerEmail, userEmail, directorDermyValueID)?.let {
                                                                        //                                                                        if (recipient != null) {
                                                                        //                                                                            notifications.sendEmail(recipient,subject,messageBody)
                                                                        //                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }


                                        }
                            }
                }




        return redirectSiteFuelList
    }

    //     Submit assign officer
    @PreAuthorize("hasAuthority('MS_MP_MODIFY')")
    @PostMapping("fuel/assign/officer")
    fun saveAssignedOfficer(
            @ModelAttribute("fuelOfficerInspectEntity") fuelOfficerInspectEntity: MsFuelInspectionOfficersEntity,
            @RequestParam("fuelInspectionID") fuelInspectionID: Long,
            results: BindingResult,
            redirectAttributes: RedirectAttributes
    ): String {
        return try {
            SecurityContextHolder.getContext().authentication?.name
                    ?.let { username ->
                        userRepository.findByUserName(username)
                                ?.let { loggedInUser ->
                                    serviceMapsRepo.findByIdAndStatus(appId, 1)
                                            ?.let { map ->
                                                iFuelInspectionRepo.findByIdOrNull(fuelInspectionID)
                                                        ?.let { fuelInspectionEntity ->

                                                            with(fuelOfficerInspectEntity) {
                                                                assignedUser
                                                                        ?.let { msioAssigneeId ->
                                                                            userRepository.findFirstByIdAndStatus(msioAssigneeId, 1)
                                                                                    .let { usersEntity ->
                                                                                        assignedIo = usersEntity
                                                                                        transactionDate = Date(Date().time)
                                                                                        status = map.activeStatus
                                                                                        createdBy = loggedInUser.firstName + " " + loggedInUser.lastName
                                                                                        createdOn = Timestamp.from(Instant.now())
                                                                                        msFuelInspectionId = fuelInspectionEntity
                                                                                    }


                                                                        }
                                                                val fuelOfficerInspect = iFuelInspectionOfficerRepo.save(fuelOfficerInspectEntity)

                                                                with(fuelInspectionEntity) {
                                                                    assignedOfficerStatus = map.activeStatus
                                                                    lastModifiedOn = Timestamp.from(Instant.now())
                                                                    lastModifiedBy = loggedInUser.firstName + " " + loggedInUser.lastName
                                                                }
                                                                iFuelInspectionRepo.save(fuelInspectionEntity)

                                                                KotlinLogging.logger { }.trace("saved with id =[${fuelOfficerInspect.id}] ")

                                                                fuelOfficerInspect.assignedIo?.id?.let {
                                                                    marketSurveillanceBpmn.msFmHodAssignOfficerComplete(fuelInspectionEntity.id, it).let {
                                                                        redirectSiteFuelList
                                                                    }

                                                                }
                                                            }


                                                        }


                                            }
                                }
                    }
        } catch (e: Exception) {
            KotlinLogging.logger { }.info { e }
            "redirect:/"
        } as String

    }

    @PreAuthorize("hasAuthority('MS_IOP_MODIFY')")
    @PostMapping("/fuel/add-remarks")
    fun saveComplaintsRemarks(
            model: Model,
//            @ModelAttribute("complaintRemarksEntity") complaintRemarksEntity: ComplaintRemarksEntity,
            @ModelAttribute("fuelEntity") fuelEntity: MsFuelInspectionEntity,
            @ModelAttribute("fuelRemediationEntity") fuelRemediationEntity: MsFuelRemediationEntity?,
            @ModelAttribute("fuelRemediationInvoiceEntity") fuelRemediationInvoiceEntity: MsFuelRemedyInvoicesEntity?,
            @RequestParam("fuelInspectID") fuelInspectID: Long,
            @RequestParam("doc_file") docFile: MultipartFile?,
            @RequestParam("ViewType") ViewType: String,
            results: BindingResult,
            redirectAttributes: RedirectAttributes
    ): String {
        SecurityContextHolder.getContext().authentication?.name
                ?.let { username ->
                    userRepository.findByUserName(username)
                            ?.let { loggedInUser ->
                                serviceMapsRepo.findByIdAndStatus(appId, 1)
                                        ?.let { map ->

                                            var fuelInspectEntity: MsFuelInspectionEntity? = null

                                            when (fuelEntity.processStage) {
                                                "COMPLIANT" -> {
                                                    fuelInspectEntity = fuelCompliantAndNotCompliant(fuelEntity, fuelInspectID, loggedInUser)
                                                    if (fuelInspectEntity.compliantStatus == activeStatus) {
                                                        fuelInspectEntity = fuelRemediationEntity?.let { fuelRemediation(it, fuelInspectID, loggedInUser) }
                                                        fuelInspectEntity?.id?.let { marketSurveillanceBpmn.msFmCheckLabReportComplete(it, true) }
                                                    } else if (fuelInspectEntity.notCompliantStatus == activeStatus) {
                                                        if (fuelRemediationInvoiceEntity != null) {
                                                            val fuelInvoiceID = fuelRemediationInvoice(fuelRemediationInvoiceEntity, fuelInspectID, loggedInUser)

                                                            val imagePath = ResourceUtils.getFile("classpath:static/images/KEBS_SMARK.png").toString()
                                                            val map = hashMapOf<String, Any>()
                                                            map["imagePath"] = imagePath
                                                            fuelInvoiceID.fuelInspectionId?.let {
                                                                msReportsControllers.extractAndSaveReport(map, "classpath:reports/remediationInvoice.jrxml", "Remediation-Invoice", iFuelRemediationInvoiceRepo.findFirstByFuelInspectionId(it) )
                                                            }
                                                            fuelInspectEntity.stationOwnerEmail?.let { sendEmailWithProforma(it, ResourceUtils.getFile("classpath:templates/TestPdf/Remediation-Invoice.pdf").toString()) }
                                                            fuelInspectEntity.id.let {
                                                                marketSurveillanceBpmn.msFmCheckLabReportComplete(it, false)
                                                                marketSurveillanceBpmn.msFmSendProformaInvoiceComplete(it, it)
                                                                sendPaymentUpdate(fuelInspectID)
                                                            }
                                                        }

                                                    }
                                                }
                                                "RAPIDTEST" -> {
                                                    fuelInspectEntity = fuelRapidTests(fuelEntity, fuelInspectID, loggedInUser)
                                                }
                                                "REMEDIATIONCOMPLETE" -> {
                                                    fuelInspectEntity = fuelRemediationComplete(fuelEntity, fuelInspectID, loggedInUser)
                                                    marketSurveillanceBpmn.msFmRemediationComplete(fuelInspectEntity.id)
                                                    sendEmailWithGeneratedReport(fuelInspectEntity)
                                                    marketSurveillanceBpmn.endMsFuelMonitoringProcess(fuelInspectEntity.id.toString())


                                                }
                                                "REMEDIATIONPAYMENT" -> {
                                                    fuelRemediationEntity?.let {
                                                        fuelRemediation(it, fuelInspectID, loggedInUser)
                                                                .let { SavedFuelEntity ->
                                                                    with(SavedFuelEntity) {
                                                                        remediationPaymentStatus = fuelEntity.remediationPaymentStatus
                                                                        lastModifiedBy = loggedInUser.firstName + " " + loggedInUser.lastName
                                                                        lastModifiedOn = Timestamp.from(Instant.now())
                                                                    }
                                                                    fuelInspectEntity = iFuelInspectionRepo.save(SavedFuelEntity)
                                                                }
                                                    }

                                                }
                                                "REMEDIATION" -> {
                                                    fuelRemediationEntity?.let {
                                                        fuelRemediationAfterPayment(it, fuelInspectID, loggedInUser)
                                                                .let { SavedFuelEntity ->
                                                                    SavedFuelEntity
                                                                            ?.let { msFuelInspectionEntity ->
                                                                                with(msFuelInspectionEntity) {
                                                                                    remendiationCompleteStatus = fuelEntity.remendiationCompleteStatus
                                                                                    lastModifiedBy = loggedInUser.firstName + " " + loggedInUser.lastName
                                                                                    lastModifiedOn = Timestamp.from(Instant.now())
                                                                                }
                                                                                fuelInspectEntity = iFuelInspectionRepo.save(msFuelInspectionEntity)
                                                                            }

                                                                }
                                                    }
                                                }
                                            }


                                            return redirectSiteFuelPage + "${fuelInspectEntity?.id}&ViewType=${ViewType}"

                                        }
                                        ?: throw ExpectedDataNotFound("Map with this [id=${appId}] does not exist")
                            }
                            ?: throw ExpectedDataNotFound("Logged in User with this [username=${username}] does not exist")
                }
                ?: throw ExpectedDataNotFound("User Is not Authenticated")
    }

    fun dateValue(valuePassed: Int): Any? {
        var valueDate: Any? = null
        if (valuePassed == 1) {
            valueDate = Date(Date().time)
        }

        return valueDate
    }

    fun fuelRapidTests(fuelEntity: MsFuelInspectionEntity, fuelInspectID: Long, loggedInUser: UsersEntity): MsFuelInspectionEntity {
        iFuelInspectionRepo.findByIdOrNull(fuelInspectID)
                ?.let { fetchedFuelInspectEntity ->
                    var fuelInspectEntity = fetchedFuelInspectEntity
                    with(fuelInspectEntity) {
                        rapidTestPassed = fuelEntity.rapidTestPassed
                        rapidTestFailed = fuelEntity.rapidTestFailed
                        rapidTestFailedOn = fuelEntity.rapidTestFailed?.let { dateValue(it) } as Date?
                        rapidTestPassedOn = fuelEntity.rapidTestPassed?.let { dateValue(it) } as Date?
                        rapidTestPassedBy = fuelEntity.rapidTestPassedBy
                        rapidTestFailedBy = fuelEntity.rapidTestFailedBy
                        rapidTestFailedRemarks = fuelEntity.rapidTestFailedRemarks
                        rapidTestPassedRemarks = fuelEntity.rapidTestPassedRemarks
                        lastModifiedBy = loggedInUser.firstName + " " + loggedInUser.lastName
                        lastModifiedOn = Timestamp.from(Instant.now())
                    }
                    fuelInspectEntity = iFuelInspectionRepo.save(fuelInspectEntity)
                    KotlinLogging.logger { }.trace("Fuel Inspect Entity saved with id =[${fuelInspectEntity.id}] ")
                    return fuelInspectEntity
                }
                ?: throw ExpectedDataNotFound("Fetched Fuel Inspect Entity with [id=${fuelInspectID}] does not exist")

    }

    fun fuelCompliantAndNotCompliant(fuelEntity: MsFuelInspectionEntity, fuelInspectID: Long, loggedInUser: UsersEntity): MsFuelInspectionEntity {
        iFuelInspectionRepo.findByIdOrNull(fuelInspectID)
                ?.let { fetchedFuelInspectEntity ->
                    var fuelInspectEntity = fetchedFuelInspectEntity
                    with(fuelInspectEntity) {
                        notCompliantStatus = fuelEntity.notCompliantStatus
                        compliantStatus = fuelEntity.compliantStatus
                        remediationStatus = fuelEntity.remediationStatus
                        remediationPaymentStatus = fuelEntity.remediationPaymentStatus
                        compliantStatusDate = fuelEntity.compliantStatus?.let { dateValue(it) } as Date?
                        notCompliantStatusDate = fuelEntity.notCompliantStatus?.let { dateValue(it) } as Date?
                        compliantStatusBy = fuelEntity.compliantStatusBy
                        notCompliantStatusBy = fuelEntity.notCompliantStatusBy
                        compliantStatusRemarks = fuelEntity.compliantStatusRemarks
                        notCompliantStatusRemarks = fuelEntity.notCompliantStatusRemarks
                        lastModifiedBy = loggedInUser.firstName + " " + loggedInUser.lastName
                        lastModifiedOn = Timestamp.from(Instant.now())
                    }
                    fuelInspectEntity = iFuelInspectionRepo.save(fuelInspectEntity)
                    KotlinLogging.logger { }.trace("Fuel Inspect Entity saved with id =[${fuelInspectEntity.id}] ")
                    return fuelInspectEntity
                }
                ?: throw ExpectedDataNotFound("Fetched Fuel Inspect Entity with [id=${fuelInspectID}] does not exist")

    }

    fun fuelRemediationComplete(fuelEntity: MsFuelInspectionEntity, fuelInspectID: Long, loggedInUser: UsersEntity): MsFuelInspectionEntity {
        iFuelInspectionRepo.findByIdOrNull(fuelInspectID)
                ?.let { fetchedFuelInspectEntity ->
                    var fuelInspectEntity = fetchedFuelInspectEntity
                    with(fuelInspectEntity) {
                        remendiationCompleteStatus = fuelEntity.remendiationCompleteStatus
                        remediationCompleteRemarks = fuelEntity.remediationCompleteRemarks
                        lastModifiedBy = loggedInUser.firstName + " " + loggedInUser.lastName
                        lastModifiedOn = Timestamp.from(Instant.now())
                    }
                    fuelInspectEntity = iFuelInspectionRepo.save(fuelInspectEntity)
                    KotlinLogging.logger { }.trace("Fuel Inspect Entity saved with id =[${fuelInspectEntity.id}] ")
                    return fuelInspectEntity
                }
                ?: throw ExpectedDataNotFound("Fetched Fuel Inspect Entity with [id=${fuelInspectID}] does not exist")

    }

    fun fuelRemediation(fuelRemediationEntity: MsFuelRemediationEntity, fuelInspectID: Long, loggedInUser: UsersEntity): MsFuelInspectionEntity {
        iFuelInspectionRepo.findByIdOrNull(fuelInspectID)
                ?.let { fetchedFuelInspectEntity ->
                    var fuelRemediation = fuelRemediationEntity
                    with(fuelRemediation) {
                        fuelInspectionId = fetchedFuelInspectEntity
                        status = activeStatus
                        createdBy = loggedInUser.firstName + " " + loggedInUser.lastName
                        createdOn = Timestamp.from(Instant.now())
                    }
                    fuelRemediation = iFuelRemediationRepo.save(fuelRemediation)
                    KotlinLogging.logger { }.trace("Fuel Remediation Entity saved with id =[${fuelRemediation.id}] ")
                    return fetchedFuelInspectEntity
                }
                ?: throw ExpectedDataNotFound("Fetched Fuel Inspect Entity with [id=${fuelInspectID}] does not exist")

    }

    fun fuelRemediationInvoice(fuelRemediationInvoiceEntity: MsFuelRemedyInvoicesEntity, fuelInspectID: Long, loggedInUser: UsersEntity): MsFuelRemedyInvoicesEntity {
        iFuelInspectionRepo.findByIdOrNull(fuelInspectID)
                ?.let { fetchedFuelInspectEntity ->
                    val remuneration = fuelRemediationInvoiceEntity.volumeFuelRemediated?.let { fuelRemunerationRateVatCalculation(it) }
                    val subsistence = fuelRemediationInvoiceEntity.subsistenceTotalNights?.let { fuelSubsistenceRateVatCalculation(it) }
                    val transportAirTickets = fuelTransportAirTicketsRateVatCalculation(fuelRemediationInvoiceEntity.transportAirTicket, fuelRemediationInvoiceEntity.transportInkm)

                    val rateValue = 0
                    val subTotal = 1
                    val vatTotal = 2
                    val totalValue = 3

                    var fuelRemediationInvoice = fuelRemediationInvoiceEntity
                    with(fuelRemediationInvoice) {
                        //Part of Remuneration calculation
                        volumeFuelRemediated = fuelRemediationInvoiceEntity.volumeFuelRemediated
                        remunerationRateLiter = remuneration?.get(rateValue)
                        remunerationSubTotal = remuneration?.get(subTotal)
                        remunerationVat = remuneration?.get(vatTotal)
                        remunerationTotal = remuneration?.get(totalValue)

                        //Part of Subsistence calculation
                        subsistenceTotalNights = fuelRemediationInvoiceEntity.subsistenceTotalNights
                        subsistenceRate = subsistence?.get(rateValue)
                        subsistenceRateNightTotal = subsistence?.get(subTotal)
                        subsistenceVat = subsistence?.get(vatTotal)
                        subsistenceTotal = subsistence?.get(totalValue)

                        //Part of Subsistence calculation
                        transportAirTicket = fuelRemediationInvoiceEntity.transportAirTicket
                        transportInkm = fuelRemediationInvoiceEntity.transportInkm
                        transportRate = transportAirTickets[rateValue]
                        transportTotalKmrate = transportAirTickets[subTotal]
                        transportVat = transportAirTickets[vatTotal]
                        transportTotal = transportAirTickets[totalValue]

                        //All calculated Grand total Value
                        transportGrandTotal = remunerationTotal?.let { subsistenceTotal?.let { it1 -> transportTotal?.let { it2 -> fuelGrandTotal(it, it1, it2) } } }

                        fuelInspectionId = fetchedFuelInspectEntity
                        transactionDate = Date(Date().time)
                        invoiceDate = Date(Date().time)
                        status = activeStatus
                        createdBy = loggedInUser.firstName + " " + loggedInUser.lastName
                        createdOn = Timestamp.from(Instant.now())
                    }
                    fuelRemediationInvoice = iFuelRemediationInvoiceRepo.save(fuelRemediationInvoice)
                    KotlinLogging.logger { }.trace("Fuel Remediation Entity saved with id =[${fuelRemediationInvoice.id}] ")
                    return fuelRemediationInvoice
                }
                ?: throw ExpectedDataNotFound("Fetched Fuel Inspect Entity with [id=${fuelInspectID}] does not exist")

    }

    fun fuelRemunerationRateVatCalculation(volume: Long): Array<Long?> {
        iFuelRemediationInvoiceChargesRepo.findByIdOrNull(remunerationChargesId)
                ?.let { chargesRateVat ->
                    val rateLitre = chargesRateVat.rate
                    val subTotal = (rateLitre?.times(volume))
                    val vatValue = (chargesRateVat.vatPercentage?.let { subTotal?.times(it) })?.div(percentage)
                    val totalValue1 = vatValue?.let { subTotal?.plus(it) }

                    return arrayOf(rateLitre, subTotal, vatValue, totalValue1)
                }
                ?: throw ExpectedDataNotFound("Fetched Fuel Remediation Charges Entity with [id=${remunerationChargesId}] does not exist")

    }

    fun fuelSubsistenceRateVatCalculation(totalNight: Long): Array<Long?> {
        iFuelRemediationInvoiceChargesRepo.findByIdOrNull(subsistenceChargesId)
                ?.let { chargesRateVat ->
                    val rateNight = chargesRateVat.rate
                    val subTotal = (rateNight?.times(totalNight))
                    val vatValue = (chargesRateVat.vatPercentage?.let { subTotal?.times(it) })?.div(percentage)
                    val totalValue2 = vatValue?.let { subTotal?.plus(it) }

                    return arrayOf(rateNight, subTotal, vatValue, totalValue2)
                }
                ?: throw ExpectedDataNotFound("Fetched Fuel Remediation Charges Entity with [id=${remunerationChargesId}] does not exist")

    }

    fun fuelTransportAirTicketsRateVatCalculation(airTicket: Long?, km: Long?): Array<Long?> {
        iFuelRemediationInvoiceChargesRepo.findByIdOrNull(transportAirTicketsChargesId)
                ?.let { chargesRateVat ->
                    val rateTransport = chargesRateVat.rate
                    val subTotal = airTicket?.let { (km?.let { rateTransport?.times(it) })?.plus(it) }
//                    val subTotal = (airTicketAndKm(airTicket, km)?.let { rateTransport?.times(it) })
                    val vatValue = (chargesRateVat.vatPercentage?.let { subTotal?.times(it) })?.div(percentage)
                    val totalValue3 = vatValue?.let { subTotal?.plus(it) }

                    return arrayOf(rateTransport, subTotal, vatValue, totalValue3)
                }
                ?: throw ExpectedDataNotFound("Fetched Fuel Remediation Charges Entity with [id=${remunerationChargesId}] does not exist")

    }

    fun airTicketAndKm(airTicket: Long?, km: Long?): Long? {
        val amountTotal: Long? = null

        if (airTicket != null && airTicket != 0L) {
            if (km != null && km != 0L) {
                if (airTicket != 0L && km != 0L) {
                    return km.times(airTicket)
                }
            }
            return airTicket
        } else if (km != null && km != 0L) {
            return km
        }

        return amountTotal
    }

    fun fuelGrandTotal(totalValue1: Long, totalValue2: Long, totalValue3: Long): Long {
        return (totalValue1.plus(totalValue2).plus(totalValue3))
    }


    fun fuelRemediationAfterPayment(fuelRemediationEntity: MsFuelRemediationEntity, fuelInspectID: Long, loggedInUser: UsersEntity): MsFuelInspectionEntity? {
        iFuelInspectionRepo.findByIdOrNull(fuelInspectID)
                ?.let { fuelInspectEntity ->
                    iFuelRemediationRepo.findByFuelInspectionId(fuelInspectEntity)
                            ?.let { fetchedFuelRemediationEntity ->
                                var fuelRemediation = fetchedFuelRemediationEntity
                                with(fuelRemediation) {
                                    productType = fuelRemediationEntity.productType
                                    quantityOfFuel = fuelRemediationEntity.quantityOfFuel
                                    contaminatedFuelType = fuelRemediationEntity.contaminatedFuelType
                                    applicableKenyaStandard = fuelRemediationEntity.applicableKenyaStandard
                                    remediationProcedure = fuelRemediationEntity.remediationProcedure
                                    volumeOfProductContaminated = fuelRemediationEntity.volumeOfProductContaminated
                                    volumeAdded = fuelRemediationEntity.volumeAdded
                                    totalVolume = fuelRemediationEntity.totalVolume
                                    modifiedBy = loggedInUser.firstName + " " + loggedInUser.lastName
                                    modifiedOn = Timestamp.from(Instant.now())
                                }
                                fuelRemediation = iFuelRemediationRepo.save(fuelRemediation)
                                KotlinLogging.logger { }.trace("Fuel Remediation Entity saved with id =[${fuelRemediation.id}] ")
                                return fuelRemediation.fuelInspectionId
                            }
                            ?: throw ExpectedDataNotFound("Fetched Fuel Remediation Entity with  fuel inspection [id=${fuelInspectID}] does not exist")
                }
                ?: throw ExpectedDataNotFound("Fetched Fuel Inspect Entity with [id=${fuelInspectID}] does not exist")

    }


    fun sendEmailWithProforma(recipient: String, attachment: String?): Boolean {
        val subject = "PRO FORMA INVOICE"
        val messageBody = "Check The attached Proforma Invoices for payment"

//        notifications.sendEmail(recipient, subject, messageBody)
        notifications.processEmail(recipient, subject, messageBody, attachment)

        return true
    }

    fun sendEmailWithGeneratedReport(fuelInspectEntity: MsFuelInspectionEntity): Boolean {
//        iFuelInspectionRepo.findByIdOrNull(fuelInspectID)
//                ?.let { fuelInspectEntity ->
//        iFuelInspectionOfficerRepo.findByMsFuelInspectionId(fuelInspectEntity)
//                ?.let { msFuelOffice ->

        val emailList: Array<String?> = arrayOf("epra@gmail.com", fuelInspectEntity.stationOwnerEmail )
        for (email in emailList) {
            val subject = "REMEDIATION REPORT"
            val messageBody = "Check The attached Remediation Report "

            if (email != null) {
                notifications.sendEmail(email, subject, messageBody)
            }
        }

//                }
////                }
//                ?: throw ExpectedDataNotFound("Fetched Fuel Inspect Entity with [id=${fuelInspectID}] does not exist")

//        notifications.processEmail(recipient, subject, messageBody, attachment)

        return true
    }

    fun sendPaymentUpdate(fuelInspectID: Long): Boolean {
        iFuelInspectionRepo.findByIdOrNull(fuelInspectID)
                ?.let { fuelInspectEntity ->
                    iFuelInspectionOfficerRepo.findByMsFuelInspectionId(fuelInspectEntity)
                            ?.let { msFuelOffice ->
                                msFuelOffice.assignedIo?.id?.let { marketSurveillanceBpmn.msFmPaymentComplete(fuelInspectEntity.id, it) }
                                val subject = "PRO FORMA INVOICE PAYMENT DETAILS"
                                val messageBody = "Check The attached Proforma Invoices for payment HAVE BEEN DONE "

                                msFuelOffice.assignedIo?.email?.let { notifications.sendEmail(it, subject, messageBody) }
                            }
                }
                ?: throw ExpectedDataNotFound("Fetched Fuel Inspect Entity with [id=${fuelInspectID}] does not exist")

//        notifications.processEmail(recipient, subject, messageBody, attachment)

        return true
    }

    fun findRegionUserId(designationID: Long, status: Int, regionEntity: RegionsEntity): UsersEntity? {
        var results: UsersEntity? = null
        designationRepo.findByIdOrNull(designationID)
                ?.let { designationsEntity ->
                    userProfilesRepo.findByRegionIdAndStatusAndDesignationId(regionEntity, status, designationsEntity)
                            ?.let { MSUSERREGION ->
                                KotlinLogging.logger { }.info { "User in region = ${MSUSERREGION.regionId?.region} user Name =[${MSUSERREGION.userId?.firstName}] " }
                                results = MSUSERREGION.userId!!
                            }
                }

        return results
    }


}