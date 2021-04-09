//package org.kebs.app.kotlin.apollo.api.controllers.diControllers
//
//import com.fasterxml.jackson.databind.ObjectMapper
//import mu.KotlinLogging
//import org.json.JSONObject
//import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
//
//import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
//import org.kebs.app.kotlin.apollo.api.ports.provided.mpesa.MPesaService
//import org.kebs.app.kotlin.apollo.api.ports.provided.mpesa.MpesaToken
////import org.kebs.app.kotlin.apollo.api.ports.provided.mpesa.MPesaHttpClient
//import org.kebs.app.kotlin.apollo.common.exceptions.SupervisorNotFoundException
//import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
//import org.kebs.app.kotlin.apollo.store.model.*
//import org.kebs.app.kotlin.apollo.store.repo.*
//import org.kebs.app.kotlin.apollo.store.repo.di.*
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.data.domain.PageRequest
//import org.springframework.data.repository.findByIdOrNull
//import org.springframework.security.access.prepost.PreAuthorize
//import org.springframework.security.core.context.SecurityContextHolder
//import org.springframework.stereotype.Controller
//import org.springframework.ui.Model
//import org.springframework.web.bind.annotation.*
//import java.sql.Timestamp
//import java.time.Instant
//import java.time.LocalDate
//import javax.servlet.http.HttpSession
//
//
//@Controller
//@RequestMapping("/api/di/")
//@SessionAttributes("userRole", "schedule", "userNumber", "CoCs", "destinationFees", "demandNote", "actionType", "docView", "CoCsBlacklist", "myCoc", "coc", "itemCoCs", "status", "item", "itemCerts", "itemIdf", "certBack", "officers", "inspectionType", "remarks", "filledChecklist")
//class DICOCController(
//        private val destinationInspectionRepo: IDestinationInspectionRepository,
//        private val iCdInspectionScheduledRepository: ICdInspectionScheduledRepository,
//        private val iConsignmentItemsRepo: IConsignmentItemsRepository,
//        private val applicationMapProperties: ApplicationMapProperties,
//        private val iUsersRepo: IUserRepository,
//        private val commonDaoServices: CommonDaoServices,
//        private val iImporterRepo: IImporterRepository,
//        private val iArrivalPointRepo: IArrivalPointRepository,
//        private val iLaboratoryRepo: ILaboratoryRepository,
//        private val iLabParametersRepo: ILabParametersRepository,
//        private val idfItemRepo: IdfItemsEntityRepository,
//        private val iRemarksRepo: IRemarksRepository,
//        private val iCheckListRepository: ICheckListRepository,
//        private val iChecklistCategory: IChecklistCategory,
//        private val iChecklistInspectionTypes: IChecklistInspectionTypes,
//        private val iCocItemRepository: ICocItemRepository,
//        private val iSampleCollectRepo: ISampleCollectRepository,
//        private val iSampleSubmitRepo: ISampleSubmitRepository,
//        private val iTargetedCdInspectionRepo: ITargetedCdInspectionRepository,
//        private val iDemandNoteRepo: IDemandNoteRepository,
//        private val iDestinationInspectionFeeRepo: IDestinationInspectionFeeRepository,
//        private val iPvocPatnersCountriesRepo: IPvocPatnersCountriesRepository,
//        private val iRemarksAddRepo: IRemarksAddRepository,
//        private val iCdStatusesValuesRepository: ICdStatusesValuesRepository,
//        private val mpesaService: MPesaService,
//        private val daoServices: DestinationInspectionDaoServices,
//        private val tokenResponse: MpesaToken,
//        private val demandNoteDaoServices: DemandNoteDaoServices
//) {
//    /**************************************************************************************************
//     * Kafka Details and common files
//     ***************************************************************************************************/
//    private final val importInspection: Int = applicationMapProperties.mapImportInspection
//    val statusEntity = iCdStatusesValuesRepository.findByServiceMapId(importInspection)
//
//    @Value("\${common.active.status}")
//    lateinit var activeStatus: String
//
//    @Value("\${common.inactive.status}")
//    lateinit var inActiveStatus: String
//
//    /***************************************************************************************************
//     * Thymeleaf Coc Mpesa test Controllers
//     ************************************************************************************************/
//    /**
//     * Coc Mpesa
//     */
//    @GetMapping("mpesa")
//    fun myPesa(model: Model): String {
//        model.addAttribute("mpesa", CdLaboratoryParametersEntity())
//
//        return "destination-inspection/coc-documents/mpesaPayment"
//    }
//
//    @GetMapping("mpesa/callBack")
//    fun myPesaCallBck(model: Model): String {
//
//        mpesaService.callBackValue()
//        return "destination-inspection/coc-documents/mpesaPayment"
//    }
//
//    //     Submit assign officer
//    @PostMapping("mpesa/test1")
//    fun saveMpesa(
//            @ModelAttribute mpesa: CdLaboratoryParametersEntity
//    ): String {
//
//        tokenResponse.token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJraW1zIiwiaXNzIjoiYXBwcy5tdWhpYS5vcmciLCJpYXQiOjE2MDMzOTc1OTgsImV4cCI6MTYwMzQwMzU5OCwiYWRkcmVzcyI6IjEwLjAuMC4yMiIsInVzZXJBZ2VudCI6IltQb3N0bWFuUnVudGltZS83LjI2LjVdIiwiaW50ZXJuYWxJcCI6WyI0MS44MS41Ni4xNDYsIDE2Mi4xNTguNDIuMTAiXSwicm9sZXMiOiJDMkJfUkVHSVNURVJfVVJMLENSRUFURV9FWFBSRVNTX1JFUVVFU1QsUVVFUllfRVhQUkVTU19SRVFVRVNUIn0.yY3M06ALWhv92Y909Y4uiqnoGsvz_0xG3cR98vggrxrLFT72sBn7Z416KoCL0qgSL3Bf7KY6Ibtb8koYDi3jmg"
//        mpesa.parameters?.let { mpesaService.sanitizePhoneNumber(it)?.let { mpesa.laboratoryId?.let { it1 -> mpesaService.pushRequest(it, it1.toBigDecimal(), null) } } }
//
//////        val authTester = Authe()
////        val jMpesa = JsonObject()
////        jMpesa.addProperty("ACCESS_TOKEN", mpesaService.authenticateTokens())
////        jMpesa.addProperty("Amount", mpesa.laboratoryId)
////        jMpesa.addProperty("PhoneNumber", mpesa.parameters?.let { mpesaService.sanitizePhoneNumber(it) })
////
//////        val mySTKPUSH = mpesaService.STKPUSH(jMpesa)
//////        KotlinLogging.logger { }.info { "My mpesa response details:  = $mySTKPUSH" }
//
//        return "destination-inspection/di-index"
//    }
//
//
////    @RequestMapping(value = ["mpesa-callback"], method = [RequestMethod.POST], consumes  = [MediaType.APPLICATION_JSON])
////    @Throws(java.lang.Exception::class)
////    fun process(@RequestBody payload: JSONObject?) {
////        KotlinLogging.logger { }.info { "My mpesa response from safaricom details:  = ${payload.toString()}" }
////        println(payload)
////    }
//
//
////    @PostMapping("mpesa/test1")
////    fun saveMpesaResponse(
////            @ModelAttribute mpesa: CdLaboratoryParametersEntity
////    ): String {
//////        val authTester = Authe()
////        val jMpesa = JsonObject()
////        jMpesa.addProperty("ACCESS_TOKEN", mpesaService.authenticateTokens())
////        jMpesa.addProperty("Amount", mpesa.laboratoryId)
////        jMpesa.addProperty("PhoneNumber", mpesa.parameters?.let { mpesaService.sanitizePhoneNumber(it) })
////
////        val mySTKPUSH = mpesaService.STKPUSH(jMpesa)
////        KotlinLogging.logger { }.info { "My mpesa response details:  = $mySTKPUSH" }
////
////        return "destination-inspection/di-index"
////    }
//
//
//    /***********************************************************************************************************************
//     * Thymeleaf Coc Controllers this part i am trying to minimize the controllers created for both Officer and supervisor
//     ***********************************************************************************************************************/
//    /**
//     * Coc home
//     */
//    @GetMapping("coc")
//    fun coc(model: Model): String {
//        return "destination-inspection/coc-details/import-inspection"
//    }
//
//    @GetMapping("blacklist")
//    fun blackListTarget(model: Model): String {
//        return "destination-inspection/coc-details/import-inspection-blacklist-target"
//    }
//
//
//    /**
//     * Consignment with coc
//     */
//    @PreAuthorize("hasAuthority('DI_INSPECTION_OFFICER_READ')  or hasAuthority('DI_DIRECTOR_READ') or hasAuthority('DI_WETC_CHAIR_READ')  or hasAuthority('DI_NSC_MEMBER_READ') or hasAuthority('DI_HOD_READ') or hasAuthority('DI_NSC_SECRETARY_READ') or hasAuthority('DI_CLUSTER_SUPERVISOR_READ') or hasAuthority('DI_WETC_MEMBER_READ') or hasAuthority('DI_OFFICER_CHARGE_READ') or hasAuthority('DI_MANAGER_INSPECTION_READ') or hasAuthority('DI_EXEMPTION_COMMITTEE_CHAIR_READ')  or hasAuthority('CD_SUPERVISOR_READ') or hasAuthority('IMPORTER') or hasAuthority('CD_OFFICER_READ') or hasAuthority('PVOC_APPLICATION_READ') or hasAuthority('PVOC_APPLICATION_PROCESS') or hasAuthority('MAKE_WAIVER_APPLICATION') or hasAuthority('WAIVERS_APPLICATION_REVIEW') or hasAuthority('WAIVER_APPLICATION_REPORT_PROCESS')")
//    @GetMapping("all-CoCs")
//    fun allCocsTable(
//            @RequestParam(value = "id", required = false) id: Long,
//            @ModelAttribute filterDetails: CdFilterDetailsEntity,
//            @RequestParam(value = "filter", required = false) filter: String?,
//            @RequestParam(value = "section", required = false) section: String,
//            @RequestParam(value = "userRole", required = false) userRole: Int,
//            @RequestParam(value = "currentPage", required = false) currentPage: Int,
//            @RequestParam(value = "pageSize", required = false) pageSize: Int,
//            model: Model): String {
//        PageRequest.of(currentPage, pageSize)
//                .let { page ->
//                    var myRole = 0
//                    SecurityContextHolder.getContext().authentication
//                            ?.let { auth ->
//                                when {
//                                    auth.authorities.stream().anyMatch { authority -> authority.authority == "DI_OFFICER_CHARGE_READ" } -> {
//                                        commonDaoServices.findUserByUserName(auth.name)
//                                                .let { usersEntity ->
//                                                    commonDaoServices.findUserProfileByUserID(usersEntity, activeStatus.toInt())
//                                                            .let { userProfilesEntity ->
//                                                                userProfilesEntity.sectionId
//                                                                        ?.let { sectionsEntity ->
//                                                                            model.addAttribute("CoCs", destinationInspectionRepo.findByPortOfArrivalOrderByIdDesc(sectionsEntity, page))
//                                                                        }
//
//                                                            }
//                                                }
//                                    }
//                                    auth.authorities.stream().anyMatch { authority -> authority.authority == "DI_INSPECTION_OFFICER_READ" } -> {
//                                        myRole = userRole
//                                        model.addAttribute("CoCs", destinationInspectionRepo.findByAssignedIoAndCocIdIsNotNullOrderByIdDesc(id, page))
//                                    }
//                                    else -> throw SupervisorNotFoundException("The user with the following [userRole=$userRole], can't access this page")
//                                }
//                            }
//                    model.addAttribute("filterDetails", CdFilterDetailsEntity())
//                    model.addAttribute("userNumber", 0)
//                    model.addAttribute("userRole", myRole)
//                    model.addAttribute("status", statusEntity)
//                    return "destination-inspection/coc-details/consignment-with-all-coc"
//                }
//    }
//
//    @PreAuthorize("hasAuthority('DI_INSPECTION_OFFICER_READ')  or hasAuthority('DI_DIRECTOR_READ') or hasAuthority('DI_WETC_CHAIR_READ')  or hasAuthority('DI_NSC_MEMBER_READ') or hasAuthority('DI_HOD_READ') or hasAuthority('DI_NSC_SECRETARY_READ') or hasAuthority('DI_CLUSTER_SUPERVISOR_READ') or hasAuthority('DI_WETC_MEMBER_READ') or hasAuthority('DI_OFFICER_CHARGE_READ') or hasAuthority('DI_MANAGER_INSPECTION_READ') or hasAuthority('DI_EXEMPTION_COMMITTEE_CHAIR_READ')  or hasAuthority('CD_SUPERVISOR_READ') or hasAuthority('IMPORTER') or hasAuthority('CD_OFFICER_READ') or hasAuthority('PVOC_APPLICATION_READ') or hasAuthority('PVOC_APPLICATION_PROCESS') or hasAuthority('MAKE_WAIVER_APPLICATION') or hasAuthority('WAIVERS_APPLICATION_REVIEW') or hasAuthority('WAIVER_APPLICATION_REPORT_PROCESS')")
//    @GetMapping("all-CoCs-user")
//    fun allCocsUserTable(
//            @RequestParam(value = "importerId", required = false) importerId: Long?,
//            @RequestParam(value = "currentPage", required = false) currentPage: Int,
//            @RequestParam(value = "pageSize", required = false) pageSize: Int,
//            model: Model): String {
//        PageRequest.of(currentPage, pageSize)
//                .let { page ->
//                    var myNumber: Long = 0
////                    SecurityContextHolder.getContext().authentication
////                            ?.let { auth ->
//                    when {
//
//                        importerId != null -> {
//                            iImporterRepo.findByIdOrNull(importerId)
//                                    ?.let { importerEntity ->
//                                        destinationInspectionRepo.findFirstByImporterIdAndCocIdIsNotNull(importerEntity, page)
//                                                .let { allImporterCoc ->
//                                                    model.addAttribute("CoCs", allImporterCoc)
//                                                    myNumber = importerId
//                                                }
//                                    }
//
//                        }
//                        else -> throw SupervisorNotFoundException("The user does not exist")
//                    }
////                            }
//                    model.addAttribute("filterDetails", CdFilterDetailsEntity())
//                    model.addAttribute("userNumber", myNumber)
//                    model.addAttribute("status", statusEntity)
//                    return "destination-inspection/coc-details/consignment-with-all-coc"
//                }
//    }
//
//    //Get coc selected details
//    @PreAuthorize("hasAuthority('DI_INSPECTION_OFFICER_READ')  or hasAuthority('DI_DIRECTOR_READ') or hasAuthority('DI_WETC_CHAIR_READ')  or hasAuthority('DI_NSC_MEMBER_READ') or hasAuthority('DI_HOD_READ') or hasAuthority('DI_NSC_SECRETARY_READ') or hasAuthority('DI_CLUSTER_SUPERVISOR_READ') or hasAuthority('DI_WETC_MEMBER_READ') or hasAuthority('DI_OFFICER_CHARGE_READ') or hasAuthority('DI_MANAGER_INSPECTION_READ') or hasAuthority('DI_EXEMPTION_COMMITTEE_CHAIR_READ')  or hasAuthority('CD_SUPERVISOR_READ') or hasAuthority('IMPORTER') or hasAuthority('CD_OFFICER_READ') or hasAuthority('PVOC_APPLICATION_READ') or hasAuthority('PVOC_APPLICATION_PROCESS') or hasAuthority('MAKE_WAIVER_APPLICATION') or hasAuthority('WAIVERS_APPLICATION_REVIEW') or hasAuthority('WAIVER_APPLICATION_REPORT_PROCESS')")
//    @GetMapping("coc-detail")
//    fun cocDetails(
//            @RequestParam(value = "id", required = false) id: Long,
//            @RequestParam(value = "docView", required = false) docView: String?,
////            @RequestParam(value = "coc", required = false) coc: ConsignmentDocumentEntity,
//            model: Model): String {
//        SecurityContextHolder.getContext().authentication.name
//                ?.let { username ->
//                    destinationInspectionRepo.findByIdOrNull(id)
//                            ?.let { cocDoc ->
//                                cocDoc.cocId?.id
//                                        ?.let { cocId ->
//                                            iCocItemRepository.findByCocId(cocId)
//                                                    .let { itemCert ->
//                                                        cocDoc.idfId?.id
//                                                                ?.let { idfId ->
//                                                                    idfItemRepo.findByIdfId(idfId)
//                                                                            .let { itemIdf ->
//                                                                                cocDoc.id
//                                                                                        ?.let { cocId ->
//                                                                                            iConsignmentItemsRepo.findByConsigmentId(cocId)
//                                                                                                    .let { itemCoCs ->
//                                                                                                        iRemarksRepo.findByConsignmentDocumentId(cocId)
//                                                                                                                .let { remarks ->
////                                                                                                        iUsersRepo.findByUserName(username)
////                                                                                                                ?.let { userEntity ->
////                                                                                                                    iuser.findByUserTypes(userType)
////                                                                                                                            .let { diOfficers ->
//                                                                                                                    model.addAttribute("itemCerts", itemCert)
//                                                                                                                    model.addAttribute("itemIdf", itemIdf)
//                                                                                                                    model.addAttribute("coc", cocDoc)
//                                                                                                                    model.addAttribute("myCoc", ObjectMapper().writeValueAsString(cocDoc))
//                                                                                                                    model.addAttribute("itemCoCs", itemCoCs)
//                                                                                                                    model.addAttribute("targets", iTargetedCdInspectionRepo.findAll())
//                                                                                                                    model.addAttribute("remarks", remarks)
//                                                                                                                    model.addAttribute("remarkData", RemarksEntity())
//                                                                                                                    model.addAttribute("demandNote", CdDemandNoteEntity())
//                                                                                                                    model.addAttribute("destinationFees", iDestinationInspectionFeeRepo.findAll())
//                                                                                                                    model.addAttribute("status", statusEntity)
////                                                                                                                    model.addAttribute("officers", daoServices.findOfficersList(cocDoc))
//                                                                                                                    model.addAttribute("schedule", CdInspectionScheduledDetailsEntity())
//                                                                                                                    model.addAttribute("docView", docView)
//                                                                                                                    return "destination-inspection/coc-details/consignment-with-coc-detail"
////                                                                                                                            }
////                                                                                                                }
//                                                                                                                }
//                                                                                                    }
//                                                                                        }
//
//                                                                            }
//                                                                }
//                                                    }
//                                        }
//                            }
//                            ?: throw SupervisorNotFoundException("The consignment Details with the following [id=$id], does not exist")
//                }
//                ?: throw SupervisorNotFoundException("INVALID USER")
//    }
//
//    //Get itemCoc selected details
//    @PreAuthorize("hasAuthority('DI_INSPECTION_OFFICER_READ')  or hasAuthority('DI_DIRECTOR_READ') or hasAuthority('DI_WETC_CHAIR_READ')  or hasAuthority('DI_NSC_MEMBER_READ') or hasAuthority('DI_HOD_READ') or hasAuthority('DI_NSC_SECRETARY_READ') or hasAuthority('DI_CLUSTER_SUPERVISOR_READ') or hasAuthority('DI_WETC_MEMBER_READ') or hasAuthority('DI_OFFICER_CHARGE_READ') or hasAuthority('DI_MANAGER_INSPECTION_READ') or hasAuthority('DI_EXEMPTION_COMMITTEE_CHAIR_READ')  or hasAuthority('CD_SUPERVISOR_READ') or hasAuthority('IMPORTER') or hasAuthority('CD_OFFICER_READ') or hasAuthority('PVOC_APPLICATION_READ') or hasAuthority('PVOC_APPLICATION_PROCESS') or hasAuthority('MAKE_WAIVER_APPLICATION') or hasAuthority('WAIVERS_APPLICATION_REVIEW') or hasAuthority('WAIVER_APPLICATION_REPORT_PROCESS')")
//    @GetMapping("coc-item-detail")
//    fun itemsDetails(
//            @RequestParam(value = "id", required = false) id: Long,
//            model: Model): String {
//        iConsignmentItemsRepo.findByIdOrNull(id)
//                ?.let { item ->
//                    model.addAttribute("item", item)
//                    return "destination-inspection/coc-details/consignment-with-coc-item-detail"
//                }
//                ?: throw SupervisorNotFoundException("The Item with the following [id=$id], does not exist")
//    }
//
//
//    //     Submit assign officer
//    @PreAuthorize("hasAuthority('DI_INSPECTION_OFFICER_READ')  or hasAuthority('DI_DIRECTOR_READ') or hasAuthority('DI_WETC_CHAIR_READ')  or hasAuthority('DI_NSC_MEMBER_READ') or hasAuthority('DI_HOD_READ') or hasAuthority('DI_NSC_SECRETARY_READ') or hasAuthority('DI_CLUSTER_SUPERVISOR_READ') or hasAuthority('DI_WETC_MEMBER_READ') or hasAuthority('DI_OFFICER_CHARGE_READ') or hasAuthority('DI_MANAGER_INSPECTION_READ') or hasAuthority('DI_EXEMPTION_COMMITTEE_CHAIR_READ')  or hasAuthority('CD_SUPERVISOR_READ') or hasAuthority('IMPORTER') or hasAuthority('CD_OFFICER_READ') or hasAuthority('PVOC_APPLICATION_READ') or hasAuthority('PVOC_APPLICATION_PROCESS') or hasAuthority('MAKE_WAIVER_APPLICATION') or hasAuthority('WAIVERS_APPLICATION_REVIEW') or hasAuthority('WAIVER_APPLICATION_REPORT_PROCESS')")
//    @PostMapping("assign/officer")
//    fun saveAssignedOfficer(
//            @ModelAttribute coc: ConsignmentDocumentEntity
//    ): String {
//        return try {
//            JSONObject(ObjectMapper().writeValueAsString(coc))
//                    .let { addValues ->
//                        JSONObject(coc.saveReason)
//                                .let { JObject ->
//                                    addValues.remove("saveReason")
//                                    for (key in addValues.keys()) {
//                                        key.let { keyStr ->
//                                            if (addValues.isNull(keyStr)) {
//                                                when {
//                                                    keyStr.equals("assignIoId") -> {
//                                                        KotlinLogging.logger { }.info { " my values key: $keyStr value: ${addValues.get(keyStr)}" }
//
//                                                    }
//                                                    else -> {
//                                                        KotlinLogging.logger { }.info { " my null values key: $keyStr value: ${addValues.get(keyStr)}" }
//                                                    }
//                                                }
//                                            } else {
//                                                JObject.remove(keyStr)
//                                                JObject.put(keyStr, addValues.get(keyStr))
//                                                KotlinLogging.logger { }.info { " my values key: $keyStr value: ${addValues.get(keyStr)}" }
//                                            }
//                                        }
//                                    }
//                                    ObjectMapper().readValue(JObject.toString(), ConsignmentDocumentEntity::class.java)
//                                            .let { updateCd ->
//                                                KotlinLogging.logger { }.info { "ucrNumber = ${updateCd.ucrNumber} myCD = ${updateCd.id} saveReason = ${updateCd.saveReason} assignedIo = ${updateCd.assignedIo}" }
//                                                updateCd.assignedIo?.let { userId ->
//                                                    iUsersRepo.findFirstByIdAndStatus(userId, 1)
//                                                            .let { usersEntity ->
//                                                                updateCd.assignIoId = usersEntity
//                                                            }
//                                                }
//                                                destinationInspectionRepo.save(updateCd)
//                                                "redirect:/api/di/coc-detail/?id=${updateCd.id}"
//                                            }
//                                }
//                    }
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.info { e }
//            "redirect:supervisor/all-cocs"
//        }
//    }
//
//    // add remarks and any button action details
//    @PreAuthorize("hasAuthority('DI_INSPECTION_OFFICER_READ')  or hasAuthority('DI_DIRECTOR_READ') or hasAuthority('DI_WETC_CHAIR_READ')  or hasAuthority('DI_NSC_MEMBER_READ') or hasAuthority('DI_HOD_READ') or hasAuthority('DI_NSC_SECRETARY_READ') or hasAuthority('DI_CLUSTER_SUPERVISOR_READ') or hasAuthority('DI_WETC_MEMBER_READ') or hasAuthority('DI_OFFICER_CHARGE_READ') or hasAuthority('DI_MANAGER_INSPECTION_READ') or hasAuthority('DI_EXEMPTION_COMMITTEE_CHAIR_READ')  or hasAuthority('CD_SUPERVISOR_READ') or hasAuthority('IMPORTER') or hasAuthority('CD_OFFICER_READ') or hasAuthority('PVOC_APPLICATION_READ') or hasAuthority('PVOC_APPLICATION_PROCESS') or hasAuthority('MAKE_WAIVER_APPLICATION') or hasAuthority('WAIVERS_APPLICATION_REVIEW') or hasAuthority('WAIVER_APPLICATION_REPORT_PROCESS')")
//    @PostMapping("coc-add-remarks")
//    fun remarks(
//            @ModelAttribute coc: ConsignmentDocumentEntity,
//            @RequestParam(value = "docView", required = false) docView: String?,
//            @ModelAttribute remarkData: RemarksEntity
//    ): String {
//        return try {
//            JSONObject(ObjectMapper().writeValueAsString(coc))
//                    .let { addValues ->
//                        JSONObject(coc.saveReason)
//                                .let { JObject ->
//                                    addValues.remove("saveReason")
//                                    for (key in addValues.keys()) {
//                                        key.let { keyStr ->
//                                            when {
//                                                addValues.isNull(keyStr) -> {
//                                                    KotlinLogging.logger { }.info { " my null values key: $keyStr value: ${addValues.get(keyStr)}" }
//                                                    if (keyStr.equals("blacklistRequestDate")) {
//                                                        JObject.remove(keyStr)
//                                                        JObject.put(keyStr, LocalDate.now())
//                                                    }
//                                                }
//                                                keyStr.equals("createdBy") -> {
//                                                    KotlinLogging.logger { }.info { " my do nothing values key: $keyStr value: ${addValues.get(keyStr)}" }
//                                                }
//                                                else -> {
//                                                    JObject.remove(keyStr)
//                                                    JObject.put(keyStr, addValues.get(keyStr))
//                                                    KotlinLogging.logger { }.info { " my values key: $keyStr value: ${addValues.get(keyStr)}" }
//                                                }
//                                            }
//                                        }
//                                    }
//                                    ObjectMapper().readValue(JObject.toString(), ConsignmentDocumentEntity::class.java)
//                                            .let { updateCd ->
//                                                KotlinLogging.logger { }.info { "supervisorRemarksStatus = ${coc.supervisorRemarksStatus} myCD = ${coc.id} assignedIo = ${coc.assignedIo}" }
//                                                remarkData.remarkStatus = statusEntity?.statusOne
//                                                remarkData.createdOn = Timestamp.from(Instant.now())
//                                                iRemarksAddRepo.save(remarkData)
//                                                destinationInspectionRepo.save(updateCd)
//                                                when {
//                                                    docView.equals("importerAgent") -> {
//                                                        "redirect:/api/di/coc-detail/?id=${updateCd.id}&docView=importerAgent"
//                                                    }
//                                                    docView.equals("targeting") -> {
//                                                        "redirect:/api/di/coc-detail/?id=${updateCd.id}&docView=targeting"
//                                                    }
//                                                    else -> {
//                                                        "redirect:/api/di/coc-detail/?id=${updateCd.id}"
//                                                    }
//                                                }
//                                            }
//                                }
//                    }
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.info { e }
//            "redirect:/all-cocs"
//        }
//    }
//
//    @PreAuthorize("hasAuthority('DI_INSPECTION_OFFICER_READ')  or hasAuthority('DI_DIRECTOR_READ') or hasAuthority('DI_WETC_CHAIR_READ')  or hasAuthority('DI_NSC_MEMBER_READ') or hasAuthority('DI_HOD_READ') or hasAuthority('DI_NSC_SECRETARY_READ') or hasAuthority('DI_CLUSTER_SUPERVISOR_READ') or hasAuthority('DI_WETC_MEMBER_READ') or hasAuthority('DI_OFFICER_CHARGE_READ') or hasAuthority('DI_MANAGER_INSPECTION_READ') or hasAuthority('DI_EXEMPTION_COMMITTEE_CHAIR_READ')  or hasAuthority('CD_SUPERVISOR_READ') or hasAuthority('IMPORTER') or hasAuthority('CD_OFFICER_READ') or hasAuthority('PVOC_APPLICATION_READ') or hasAuthority('PVOC_APPLICATION_PROCESS') or hasAuthority('MAKE_WAIVER_APPLICATION') or hasAuthority('WAIVERS_APPLICATION_REVIEW') or hasAuthority('WAIVER_APPLICATION_REPORT_PROCESS')")
//    @GetMapping("all-CoCs-target-blacklist")
//    fun allCocsBlacklistTargetTable(
//            @RequestParam(value = "section", required = false) section: String,
//            @RequestParam(value = "actionType", required = false) actionType: String,
//            @RequestParam(value = "currentPage", required = false) currentPage: Int,
//            @RequestParam(value = "pageSize", required = false) pageSize: Int,
//            model: Model): String {
//        PageRequest.of(currentPage, pageSize)
//                .let { page ->
//                    var myRole = ""
//                    SecurityContextHolder.getContext().authentication
//                            ?.let { auth ->
//                                when {
//                                    auth.authorities.stream().anyMatch { authority -> authority.authority == "CD_SUPERVISOR_READ" } -> {
//                                        iArrivalPointRepo.findByAcronymName(section)
//                                                ?.let { arrivalPoint ->
//                                                    myRole = actionType
//                                                    when {
//                                                        actionType.equals("Importer") -> {
//                                                            destinationInspectionRepo.findByArrivalPointAndBlacklistStatusAndBlacklistApprovedAndCocIdIsNotNull(arrivalPoint, 1, 0, page)
//                                                                    ?.let { blackList ->
//                                                                        model.addAttribute("CoCsBlacklist", blackList)
//                                                                        model.addAttribute("actionType", actionType)
//                                                                        model.addAttribute("myDockView", "importerAgent")
//                                                                        model.addAttribute("blacklistTarget", "blacklist")
//                                                                    }
//                                                        }
//                                                        actionType.equals("Clearing Agent") -> {
//                                                            destinationInspectionRepo.findByArrivalPointAndBlacklistStatusAndBlacklistApprovedAndCocIdIsNotNull(arrivalPoint, 1, 0, page)
//                                                                    ?.let { blackList ->
//                                                                        model.addAttribute("CoCsBlacklist", blackList)
//                                                                        model.addAttribute("actionType", actionType)
//                                                                        model.addAttribute("myDockView", "importerAgent")
//                                                                        model.addAttribute("blacklistTarget", "blacklist")
//                                                                    }
//                                                        }
//                                                        actionType.equals("Targeting") -> {
//                                                            destinationInspectionRepo.findByArrivalPointAndTargetedStatusAndTargetApprovedAndCocIdIsNotNull(arrivalPoint, 1, 0, page)
//                                                                    ?.let { target ->
//                                                                        model.addAttribute("CoCsBlacklist", target)
//                                                                        model.addAttribute("actionType", "Targeted")
//                                                                        model.addAttribute("myDockView", "targeting")
//                                                                        model.addAttribute("blacklistTarget", "target")
//                                                                    }
//                                                        }
//                                                        else -> throw SupervisorNotFoundException("The following [action type=$actionType] , does not exist")
//                                                    }
//
//
//                                                }
//                                    }
//                                    else -> throw SupervisorNotFoundException("Only users with the following privilege CD SUPERVISOR READ , can access this page")
//                                }
//                            }
//
//                    model.addAttribute("userRole", myRole)
//                    model.addAttribute("status", statusEntity)
//                    return "destination-inspection/coc-details/consignment-with-coc-target-blacklist"
//                }
//    }
//
//    /****************************************************************************************************
//     * Thymeleaf Certificate Of Compliance Module Controllers
//     ************************************************************************************************/
//    /**
//     * Coc Certificate Of Compliance and Remarks View
//     */
//    @PreAuthorize("hasAuthority('DI_INSPECTION_OFFICER_READ')  or hasAuthority('DI_DIRECTOR_READ') or hasAuthority('DI_WETC_CHAIR_READ')  or hasAuthority('DI_NSC_MEMBER_READ') or hasAuthority('DI_HOD_READ') or hasAuthority('DI_NSC_SECRETARY_READ') or hasAuthority('DI_CLUSTER_SUPERVISOR_READ') or hasAuthority('DI_WETC_MEMBER_READ') or hasAuthority('DI_OFFICER_CHARGE_READ') or hasAuthority('DI_MANAGER_INSPECTION_READ') or hasAuthority('DI_EXEMPTION_COMMITTEE_CHAIR_READ')  or hasAuthority('CD_SUPERVISOR_READ') or hasAuthority('IMPORTER') or hasAuthority('CD_OFFICER_READ') or hasAuthority('PVOC_APPLICATION_READ') or hasAuthority('PVOC_APPLICATION_PROCESS') or hasAuthority('MAKE_WAIVER_APPLICATION') or hasAuthority('WAIVERS_APPLICATION_REVIEW') or hasAuthority('WAIVER_APPLICATION_REPORT_PROCESS')")
//    @GetMapping("documents-view")
//    fun documentsView(model: Model,
//                      @RequestParam(value = "doc", required = false) doc: String
//                      , @RequestParam(value = "certBack", required = false) certBack: String
//    ): String {
//        return try {
//            model.addAttribute("certBack", certBack)
//            "destination-inspection/coc-documents/$doc-view"
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.info { e }
//            "redirect:/home"
//        }
//    }
//
//    @PreAuthorize("hasAuthority('DI_INSPECTION_OFFICER_READ')  or hasAuthority('DI_DIRECTOR_READ') or hasAuthority('DI_WETC_CHAIR_READ')  or hasAuthority('DI_NSC_MEMBER_READ') or hasAuthority('DI_HOD_READ') or hasAuthority('DI_NSC_SECRETARY_READ') or hasAuthority('DI_CLUSTER_SUPERVISOR_READ') or hasAuthority('DI_WETC_MEMBER_READ') or hasAuthority('DI_OFFICER_CHARGE_READ') or hasAuthority('DI_MANAGER_INSPECTION_READ') or hasAuthority('DI_EXEMPTION_COMMITTEE_CHAIR_READ')  or hasAuthority('CD_SUPERVISOR_READ') or hasAuthority('IMPORTER') or hasAuthority('CD_OFFICER_READ') or hasAuthority('PVOC_APPLICATION_READ') or hasAuthority('PVOC_APPLICATION_PROCESS') or hasAuthority('MAKE_WAIVER_APPLICATION') or hasAuthority('WAIVERS_APPLICATION_REVIEW') or hasAuthority('WAIVER_APPLICATION_REPORT_PROCESS')")
//    @GetMapping("back-view")
//    fun backView(model: Model,
//                 @RequestParam(value = "close", required = false) close: String?,
//                 @RequestParam(value = "section", required = false) section: String?,
//                 @RequestParam(value = "id", required = false) id: Long?,
//                 session: HttpSession
//    ): String {
//        return try {
//            var backValue = ""
//            when (close) {
//                "cocTable" -> {
//                    SecurityContextHolder.getContext().authentication?.name
//                            ?.let { username ->
//                                iUsersRepo.findByUserName(username)
//                                        ?.let { loggedInUser ->
//                                            backValue = "redirect:/api/di/all-CoCs/?id=$id?userRole=${loggedInUser.userTypes?.defaultRole}?section=$section?currentPage=0?pageSize=10"
//                                        }
//                            }
//                }
//                "actual" -> {
//                    backValue = "redirect:/api/di/inspection-cd/?inspectionType=$close"
//                }
//                "physical" -> {
//                    backValue = "redirect:/api/di/inspection-cd/?inspectionType=$close"
//                }
//                "cocDetailsOfficer" -> {
//                    backValue = "redirect:/api/di/coc-detail/?id=$id"
//                }
//                "cocDetails" -> {
//                    backValue = "redirect:/api/di/coc-detail/?id=$id"
//                }
//            }
//            backValue
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.info { e }
//            "redirect:/home"
//        }
//    }
//
//    /****************************************************************************************************
//     * Thymeleaf Inspection Module Controllers
//     ************************************************************************************************/
//    /**
//     * Inspection module  View
//     */
//    // Inspection officer
//    @PreAuthorize("hasAuthority('DI_INSPECTION_OFFICER_READ')  or hasAuthority('DI_DIRECTOR_READ') or hasAuthority('DI_WETC_CHAIR_READ')  or hasAuthority('DI_NSC_MEMBER_READ') or hasAuthority('DI_HOD_READ') or hasAuthority('DI_NSC_SECRETARY_READ') or hasAuthority('DI_CLUSTER_SUPERVISOR_READ') or hasAuthority('DI_WETC_MEMBER_READ') or hasAuthority('DI_OFFICER_CHARGE_READ') or hasAuthority('DI_MANAGER_INSPECTION_READ') or hasAuthority('DI_EXEMPTION_COMMITTEE_CHAIR_READ')  or hasAuthority('CD_SUPERVISOR_READ') or hasAuthority('IMPORTER') or hasAuthority('CD_OFFICER_READ') or hasAuthority('PVOC_APPLICATION_READ') or hasAuthority('PVOC_APPLICATION_PROCESS') or hasAuthority('MAKE_WAIVER_APPLICATION') or hasAuthority('WAIVERS_APPLICATION_REVIEW') or hasAuthority('WAIVER_APPLICATION_REPORT_PROCESS')")
//    @GetMapping("inspection-cd")
//    fun inspectionCd(
//            model: Model,
//            @RequestParam(value = "inspectionType", required = false) inspectionType: String
//    ): String {
//        return try {
//            model.addAttribute("inspectionType", inspectionType)
//            "destination-inspection/coc-Inspection-module/actual-inspection"
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.info { e }
//            "redirect:/all-cocs"
//        }
//    }
//
//    //Officer opening the inspection item details for coc being inspected consignment details
//    @PreAuthorize("hasAuthority('DI_INSPECTION_OFFICER_READ')  or hasAuthority('DI_DIRECTOR_READ') or hasAuthority('DI_WETC_CHAIR_READ')  or hasAuthority('DI_NSC_MEMBER_READ') or hasAuthority('DI_HOD_READ') or hasAuthority('DI_NSC_SECRETARY_READ') or hasAuthority('DI_CLUSTER_SUPERVISOR_READ') or hasAuthority('DI_WETC_MEMBER_READ') or hasAuthority('DI_OFFICER_CHARGE_READ') or hasAuthority('DI_MANAGER_INSPECTION_READ') or hasAuthority('DI_EXEMPTION_COMMITTEE_CHAIR_READ')  or hasAuthority('CD_SUPERVISOR_READ') or hasAuthority('IMPORTER') or hasAuthority('CD_OFFICER_READ') or hasAuthority('PVOC_APPLICATION_READ') or hasAuthority('PVOC_APPLICATION_PROCESS') or hasAuthority('MAKE_WAIVER_APPLICATION') or hasAuthority('WAIVERS_APPLICATION_REVIEW') or hasAuthority('WAIVER_APPLICATION_REPORT_PROCESS')")
//    @GetMapping("coc-inspection-item-details")
//    fun inspectionCdItemsDetails(
//            @RequestParam(value = "id", required = false) id: Long,
//            model: Model
//    ): String {
//        return try {
//            iConsignmentItemsRepo.findByIdOrNull(id)
//                    .let { itemDetails ->
//                        model.addAttribute("item", itemDetails)
//                        return "destination-inspection/coc-Inspection-module/inspection-item-details"
//                    }
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.info { e }
//            "redirect:/all-cocs"
//        }
//
//    }
//
//    @GetMapping("inspection-scheduled")
//    fun inspectionScheduled(model: Model): String {
//        return "destination-inspection/coc-Inspection-module/inspection-scheduling"
//    }
//
//    @PostMapping("save-schedule")
//    fun saveSchedule(model: Model,
//                     @RequestParam(value = "close", required = false) close: String?,
//                     @RequestParam(value = "id", required = false) id: Long,
//                     @ModelAttribute schedule: CdInspectionScheduledDetailsEntity
//    ): String {
//        return try {
//
//            schedule.cdId = id
//            schedule.createdOn = Timestamp.from(Instant.now())
//            iCdInspectionScheduledRepository.save(schedule)
//            "redirect:/api/di/inspection-cd/?inspectionType=$close"
//
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.info { e }
//            "redirect:supervisor/all-cocs"
//        }
//
//    }
//
//
//    //     Submit assign officer
//    @PreAuthorize("hasAuthority('DI_INSPECTION_OFFICER_READ')  or hasAuthority('DI_DIRECTOR_READ') or hasAuthority('DI_WETC_CHAIR_READ')  or hasAuthority('DI_NSC_MEMBER_READ') or hasAuthority('DI_HOD_READ') or hasAuthority('DI_NSC_SECRETARY_READ') or hasAuthority('DI_CLUSTER_SUPERVISOR_READ') or hasAuthority('DI_WETC_MEMBER_READ') or hasAuthority('DI_OFFICER_CHARGE_READ') or hasAuthority('DI_MANAGER_INSPECTION_READ') or hasAuthority('DI_EXEMPTION_COMMITTEE_CHAIR_READ')  or hasAuthority('CD_SUPERVISOR_READ') or hasAuthority('IMPORTER') or hasAuthority('CD_OFFICER_READ') or hasAuthority('PVOC_APPLICATION_READ') or hasAuthority('PVOC_APPLICATION_PROCESS') or hasAuthority('MAKE_WAIVER_APPLICATION') or hasAuthority('WAIVERS_APPLICATION_REVIEW') or hasAuthority('WAIVER_APPLICATION_REPORT_PROCESS')")
//    @PostMapping("generate/demand-note")
//    fun generateDemandNote(
//            @ModelAttribute demandNote: CdDemandNoteEntity
//    ): String {
//        var note: CdDemandNoteEntity? = null
//        KotlinLogging.logger { }.info { "Selected Fee" + demandNote.destinationFeeValue }
//        iDestinationInspectionFeeRepo.findByIdOrNull(demandNote.destinationFeeValue)
//                ?.let { fee ->
//                    iConsignmentItemsRepo.findByIdOrNull(demandNote.itemIdNo)
//                            ?.let { itemUpdate ->
//                                demandNote.destinationFeeId = fee
//                                demandNote.rate = fee.rate?.toLong()
//                                KotlinLogging.logger { }.info { "FEE RATE VALUE" + fee.rate?.toLong() }
//
//                                val percentage = 100
//                                demandNote.amountPayable = demandNote.cfvalue?.multiply(fee.rate)?.divide(percentage.toBigDecimal())
//
//                                demandNote.createdOn = Timestamp.from(Instant.now())
//
//                                note = iDemandNoteRepo.save(demandNote)
//                                KotlinLogging.logger { }.info { "FEE RATE VALUE${note?.amountPayable}" }
//                                itemUpdate.dnoteStatus = statusEntity?.statusOne
//                                iConsignmentItemsRepo.save(itemUpdate)
//                            }
//                }
////        var generatedDemandNote: CdDemandNoteEntity? = null
////        statusEntity?.let {
////            generatedDemandNote =  demandNoteDaoServices.generateDemandNote(demandNote, it)
////        }
//        return "redirect:/api/di/coc-inspection-item-details/?id=${note?.itemIdNo}"
//    }
//
//    //Officer opening the inspection item details for coc being inspected consignment details
//    @PreAuthorize("hasAuthority('DI_INSPECTION_OFFICER_READ')  or hasAuthority('DI_DIRECTOR_READ') or hasAuthority('DI_WETC_CHAIR_READ')  or hasAuthority('DI_NSC_MEMBER_READ') or hasAuthority('DI_HOD_READ') or hasAuthority('DI_NSC_SECRETARY_READ') or hasAuthority('DI_CLUSTER_SUPERVISOR_READ') or hasAuthority('DI_WETC_MEMBER_READ') or hasAuthority('DI_OFFICER_CHARGE_READ') or hasAuthority('DI_MANAGER_INSPECTION_READ') or hasAuthority('DI_EXEMPTION_COMMITTEE_CHAIR_READ')  or hasAuthority('CD_SUPERVISOR_READ') or hasAuthority('IMPORTER') or hasAuthority('CD_OFFICER_READ') or hasAuthority('PVOC_APPLICATION_READ') or hasAuthority('PVOC_APPLICATION_PROCESS') or hasAuthority('MAKE_WAIVER_APPLICATION') or hasAuthority('WAIVERS_APPLICATION_REVIEW') or hasAuthority('WAIVER_APPLICATION_REPORT_PROCESS')")
//    @GetMapping("inspection-check-list")
//    fun inspectionChecklistForm(
//            @RequestParam(value = "id", required = false) id: Long,
//            model: Model
//    ): String {
//        iCheckListRepository.findFirstByCdIdNumber(id)
//                ?.let { checkListFound ->
//                    model.addAttribute("checkListFound", checkListFound)
//                }
//        model.addAttribute("targets", iChecklistInspectionTypes.findAll())
//        model.addAttribute("categories", iChecklistCategory.findAll())
//        model.addAttribute("checkList", CdInspectionChecklistEntity())
//        return "destination-inspection/coc-Inspection-module/checklist-form"
//    }
//
//    //     Submit Checklist filled
//    @PreAuthorize("hasAuthority('DI_INSPECTION_OFFICER_READ')  or hasAuthority('DI_DIRECTOR_READ') or hasAuthority('DI_WETC_CHAIR_READ')  or hasAuthority('DI_NSC_MEMBER_READ') or hasAuthority('DI_HOD_READ') or hasAuthority('DI_NSC_SECRETARY_READ') or hasAuthority('DI_CLUSTER_SUPERVISOR_READ') or hasAuthority('DI_WETC_MEMBER_READ') or hasAuthority('DI_OFFICER_CHARGE_READ') or hasAuthority('DI_MANAGER_INSPECTION_READ') or hasAuthority('DI_EXEMPTION_COMMITTEE_CHAIR_READ')  or hasAuthority('CD_SUPERVISOR_READ') or hasAuthority('IMPORTER') or hasAuthority('CD_OFFICER_READ') or hasAuthority('PVOC_APPLICATION_READ') or hasAuthority('PVOC_APPLICATION_PROCESS') or hasAuthority('MAKE_WAIVER_APPLICATION') or hasAuthority('WAIVERS_APPLICATION_REVIEW') or hasAuthority('WAIVER_APPLICATION_REPORT_PROCESS')")
//    @PostMapping("inspection/check-list")
//    fun checkListSubmit(
//            @ModelAttribute checkList: CdInspectionChecklistEntity,
//            session: HttpSession,
//            model: Model
////            , @RequestParam(value = "id", required = false) id: Long
//    ): String {
//        return try {
//            var results = ""
//            checkList.createdOn = Timestamp.from(Instant.now())
//            iCheckListRepository.save(checkList)
//                    .let { savedChecklist ->
//                        iConsignmentItemsRepo.findByIdOrNull(savedChecklist.itemId)
//                                ?.let { item ->
//                                    destinationInspectionRepo.findByIdOrNull(item.consigmentId)
//                                            ?.let { coc ->
//                                                when (savedChecklist.itemNumber) {
//                                                    1L -> {
//                                                        /***
//                                                         ** AGROCHEM CHECKLIST TYPE
//                                                         ****/
//                                                        coc.checklistTypeAgrochem = iChecklistInspectionTypes.findByIdOrNull(1L)
//                                                    }
//                                                    2L -> {
//                                                        /***
//                                                         ** ENGINEERING CHECKLIST TYPE
//                                                         ****/
//                                                        coc.checklistTypeEngineering = iChecklistInspectionTypes.findByIdOrNull(2L)
//                                                    }
//                                                    3L -> {
//                                                        /***
//                                                         ** OTHERS CHECKLIST TYPE
//                                                         ****/
//                                                        coc.checklistTypeOthers = iChecklistInspectionTypes.findByIdOrNull(3L)
//                                                    }
//
//                                                    //                                                coc?.inspectionChecklistStatus = statusEntity?.statusOne
//                                                }
//                                                coc.inspectionChecklistStatus = statusEntity?.statusOne
//                                                destinationInspectionRepo.save(coc)
//                                                        .let { savedCocDoc ->
//                                                            item.checklistStatus = statusEntity?.statusOne
//                                                            iConsignmentItemsRepo.save(item)
//
//                                                            when {
//                                                                checkList.sampled.equals("YES") -> {
//                                                                    model.addAttribute("filledChecklist", savedChecklist)
//                                                                    results = "redirect:/api/di/inspection/sample-collect/?id=${savedChecklist.id}?cocId=${savedCocDoc.id}"
//
//                                                                }
//                                                                checkList.sampled.equals("NO") -> {
//                                                                    results = "redirect:/api/di/coc-inspection-item-details/?id=${item.id}"
//                                                                }
//                                                            }
//                                                        }
//
//                                            }
//                                }
//                                ?: throw SupervisorNotFoundException("The Item Details with the following [id=${checkList.id}], does not exist")
//                    }
//
//
//            results
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.info { e }
//            "redirect:supervisor/all-cocs"
//        }
//    }
//
//    // Collect sample
//    @GetMapping("inspection/sample-collect")
//    fun SampleCollect(
//            @RequestParam(value = "id", required = false) id: Long,
//            @RequestParam(value = "cocId", required = false) cocId: Long,
//            model: Model
//    ): String {
//
//        destinationInspectionRepo.findByIdOrNull(cocId)
//                ?.let { cocDoc ->
//                    model.addAttribute("coc", cocDoc)
//                    model.addAttribute("myCoc", ObjectMapper().writeValueAsString(cocDoc))
//                    model.addAttribute("sampleCollect", CdSampleCollectionEntity())
//                    return "destination-inspection/coc-Inspection-module/sample-collect-form"
//                }
//                ?: throw SupervisorNotFoundException("The document Details with the following [id=${cocId}], does not exist")
//
//    }
//
//    //
//    // Collect sample save
//    @PostMapping("inspection/sample-collected")
//    fun saveSampleCollectCoc(@ModelAttribute sampleCollect: CdSampleCollectionEntity,
//                             model: Model,
//                             @RequestParam(value = "id", required = false) id: Long,
//                             @RequestParam(value = "inspectionType", required = false) inspectionType: String
//    ): String {
//        return try {
//            iCheckListRepository.findByItemId(id)
//                    ?.let { checklist ->
////                    sampleCollect.manufacturingDate = checklist.dateMfgPackaging
////                    sampleCollect.expiryDate = checklist.dateExpiry
//                        sampleCollect.createdOn = Timestamp.from(Instant.now())
//                        iSampleCollectRepo.save(sampleCollect)
//                                .let { sampSaved ->
//                                    iConsignmentItemsRepo.findByIdOrNull(sampSaved.itemId)
//                                            ?.let { updateItem ->
//                                                updateItem.sampledStatus = statusEntity?.statusOne
//                                                updateItem.sampleSubmissionStatus = 0
//                                                iConsignmentItemsRepo.save(updateItem)
//                                                model.addAttribute("inspectionType", inspectionType)
//
//                                                "redirect:/api/di/coc-inspection-item-details/?id=${updateItem.id}"
//                                            }
//                                            ?: throw SupervisorNotFoundException("The Item Details with the following [id=${id}], does not exist")
//                                }
//                    }
//                    ?: throw SupervisorNotFoundException("The Checklist Details with the following [id=${id}], does not exist")
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.error { "Caught an exception $e" }
//            "redirect:officer/all-cocs"
//        }
//
//    }
//
//    //
//    // Collect sample
//    @GetMapping("inspection/sample-submit-item-details")
//    fun officerSampleSubmissionDetailsCoc(model: Model,
//                                          @RequestParam(value = "id", required = false) id: Long
//    ): String {
//        return try {
//            model.addAttribute("labParameter", DIListWrapper())
//            model.addAttribute("laboratories", iLaboratoryRepo.findAll())
//            model.addAttribute("filledChecklist", iCheckListRepository.findByItemId(id))
//            model.addAttribute("sampleCollected", iSampleCollectRepo.findByItemId(id))
//            model.addAttribute("sampleSubmit", CdSampleSubmissionItemsEntity())
//            "destination-inspection/coc-Inspection-module/sample-submission-form"
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.error { "Caught an exception $e" }
//            "redirect:officer/all-cocs"
//        }
//    }
//
//    //
////
//    // Submit sample save
//    @PostMapping("inspection/sample-submitted")
//    fun saveSampleSubmissionCoc(@ModelAttribute sampleSubmit: CdSampleSubmissionItemsEntity,
//                                @ModelAttribute("labParameter") labParameter: DIListWrapper,
//                                model: Model,
//                                @RequestParam(value = "id", required = false) id: Long,
//                                @RequestParam(value = "inspectionType", required = false) inspectionType: String
//    ): String {
//        iConsignmentItemsRepo.findByIdOrNull(id)
//                ?.let { updateItem ->
//                    sampleSubmit.createdOn = Timestamp.from(Instant.now())
//                    val sampleSubmitId = iSampleSubmitRepo.save(sampleSubmit).id
//
////                    for (labParam in labParameter.getFeatureArrayList()!!) {
////                        if (labParam.parameters != null && labParam.laboratoryName != null) {
////                            labParam.sampleSubmissionId = sampleSubmitId
////                            labParam.status = statusEntity?.statusOne
//////                            labParam.createdBy = "admin"
////                            labParam.createdOn = Timestamp.from(Instant.now())
////
////                            val idValue = iLabParametersRepo.save(labParam).id
////                            KotlinLogging.logger { }.info { "My id for lab save details:  = $idValue" }
////                            println(labParameter.getFeatureArrayList()!!.size)
////                        }
////
////                    }
//
//                    updateItem.sampledStatus = statusEntity?.statusOne
//                    updateItem.sampleSubmissionStatus = statusEntity?.statusOne
//                    updateItem.bothCollectedStatus = statusEntity?.statusOne
//                    iConsignmentItemsRepo.save(updateItem)
//
//                    model.addAttribute("inspectionType", inspectionType)
//
//                    return "redirect:/api/di/coc-inspection-item-details/?id=$id"
//                }
//                ?: throw SupervisorNotFoundException("The Item with the following [id=$id], does not exist")
//
//
//    }
//
////    // Demand NOTE GENERATION
////    @GetMapping("officer/{id}/demand-note")
////    fun officerDemandCd(model: Model,
////                        @PathVariable("id") itemDocId: Long
////    ) : String {
////        return try {
////            val item = iConsignmentItemsRepo.findByIdOrNull(itemDocId)
////            destinationInspectionRepo.findByIdOrNull(item?.consigmentId)
////                    ?.let {doc->
////                        var generatedDemandNoteId = statusEntity?.let { demandNoteDaoServices.generateDemandNote(itemDocId,"admin", it) }
////                        model.addAttribute("itemCocs", item)
////                        model.addAttribute("note", iDemandNoteRepo.findByIdOrNull(generatedDemandNoteId))
////                        model.addAttribute("coc", doc)
////                        "destination-inspection/coc-officer/demand-note-details"
////
////                    }
////                    ?:throw Exception("Record not found")
////
////        } catch (e: Exception) {
////            KotlinLogging.logger { }.info { e }
////            "redirect:/api/inspection/officer/all-cocs"
////        }
////    }
//
//    //Get remarks records for any process
//    @GetMapping("remarks-value")
//    @ResponseBody
//    fun processRemarks(model: Model,
//                       @RequestParam(value = "id", required = false) id: Long,
//                       @RequestParam(value = "remarksProcess", required = false) remarksProcess: String
//    ): RemarksEntity {
//        iRemarksAddRepo.findFirstByConsignmentDocumentIdAndRemarksProcess(id, remarksProcess)
//                ?.let { remarks ->
//                    return remarks
//                }
//                ?: throw Exception("Record not found")
//
//
//    }
//
////
////    // Demand NOTE GENERATION
////    @GetMapping("officer/{id}/demand-note")
////    fun officerDemandCd(model: Model, @PathVariable("id") itemDocId: Long ) : String {
////        return try {
////            val item = iConsignmentItemsRepo.findByIdOrNull(itemDocId)
////            destinationInspectionRepo.findByIdOrNull(item?.consigmentId)
////                    ?.let {doc->
////                        var generatedDemandNoteId = generateDemandNote(itemDocId)
////                        model.addAttribute("itemCocs", item)
////                        model.addAttribute("note", iDemandNoteRepo.findByIdOrNull(generatedDemandNoteId))
////                        model.addAttribute("coc", doc)
////                        "destination-inspection/coc-officer/demand-note-details"
////
////                    }
////                    ?:throw Exception("Record not found")
////
////        } catch (e: Exception) {
////            KotlinLogging.logger { }.info { e }
////            "redirect:/api/inspection/officer/all-cocs"
////        }
////    }
////
////    fun generateDemandNote(itemDocId: Long): Long {
////        var noteId: Long = 0
////        val item = iConsignmentItemsRepo.findByIdOrNull(itemDocId)
////        destinationInspectionRepo.findByIdOrNull(item?.consigmentId)
////                ?.let {doc->
////                    val demand = CdDemandNoteEntity()
////                    demand.nameImporter = doc.importerId?.name
////                    demand.address = doc.importerId?.physicalAddress
////                    demand.telephone = doc.importerId?.telephone
////                    demand.dateGenerated = java.util.Date.from(Instant.now())
////                    demand.descriptionGoods = item?.itemDescription
////                    demand.ucrNumber = doc.ucrNumber
////                    demand.itemHscode = item?.itemHsCode
////                    demand.itemId = item
////                    demand.itemIdNo = itemDocId
////
////                    when {
////                        item?.countryOfOrgin?.let { iPvocPatnersCountriesRepo.findByCountryNameContainingIgnoreCase(it) } != null -> {
////                            iDestinationInspectionFeeRepo.findByIdOrNull(1)
////                                    ?.let {fee->
////                                        demand.rate =  fee.rate?.toLong()
////                                        KotlinLogging.logger {  }.info { fee.rate?.toLong() }
////                                        demand.cFValue = item.totalPriceNcy
////                                        val percentage = 100
////                                        demand.amountPayable = item.totalPriceNcy?.multiply(fee.rate?.toBigDecimal())?.divide(percentage.toBigDecimal())
////                                    }
////                        }
////                        else -> {
////                            iDestinationInspectionFeeRepo.findByIdOrNull(2)
////                                    ?.let {fee->
////                                        demand.rate =  fee.rate?.toLong()
////                                        demand.cFValue = item?.totalPriceNcy
////                                        val percentage = 100
////                                        demand.amountPayable = item?.totalPriceNcy?.multiply(fee.rate?.toBigDecimal())?.divide(percentage.toBigDecimal())
////                                    }
////                        }
////                    }
////
////                    demand.product = item?.itemDescription
////                    demand.createdBy = createdBy
////                    demand.createdOn = timeCreatedBy
////
////                    noteId = iDemandNoteRepo.save(demand).id!!
////
////                    iConsignmentItemsRepo.findByIdOrNull(itemDocId)
////                            ?.let { itemUpdate ->
////                                itemUpdate.dnoteStatus = statusEntity?.statusOne
////                                iConsignmentItemsRepo.save(itemUpdate)
////                            }
////
////                }
////        return noteId
////    }
////
////
////    /***************************************************************************************************
////     * Thymeleaf Certificate Of Compliance Module Controllers
////     ************************************************************************************************/
////    /**
////     * Coc Certificate Of Compliance and Remarks View
////     */
////
////    @GetMapping("remarks-view/{certBack}")
////    fun remarksView(model: Model, @PathVariable("certBack") certBack: String) : String {
////        return try {
////            model.addAttribute("certBack",certBack)
////            "destination-inspection/coc-documents/remarks-view"
////        } catch (e: Exception) {
////            KotlinLogging.logger { }.info { e }
////            "redirect:/home"
////        }
////    }
////
////    @GetMapping("idf-view/{certBack}")
////    fun idfView(model: Model, @PathVariable("certBack") certBack: String) : String {
////        return try {
////            model.addAttribute("certBack",certBack)
////            "destination-inspection/coc-documents/idf-documents-view"
////        } catch (e: Exception) {
////            KotlinLogging.logger { }.info { e }
////            "redirect:/home"
////        }
////    }
////
////    @GetMapping("certificate-of-compliance/{certBack}")
////    fun certificateOfCompliance(model: Model, @PathVariable("certBack") certBack: String) : String {
////        return try {
////            model.addAttribute("certBack",certBack)
////            "destination-inspection/coc-documents/certificate-of-compliance"
////        } catch (e: Exception) {
////            KotlinLogging.logger { }.info { e }
////            "redirect:/home"
////        }
////    }
////
////
////    @GetMapping("certificate/{id}/{close}")
////    fun certClose(model: Model, @PathVariable("id") id: Long, @PathVariable("close") close: String) : String {
////        return try {
////            var backValue = ""
////            when (close) {
////                "actual" -> {
////                    backValue = "redirect:/api/di/officer/inspection-cd/{close}"
////                }
////                "actualItem" -> {
////                    backValue = "redirect:/api/di/officer/coc-inspection-item-details/{id}"
////                }
////                "physical" -> {
////                    backValue = "redirect:/api/di/officer/inspection-cd/{close}"
////                }
////                "cocDetailsOfficer" -> {
////                    backValue = "redirect:/api/di/officer/coc-details/{id}"
////                }
////                "cocDetailsSupervisor" -> {
////                    backValue = "redirect:/api/di/supervisor/coc-details/{id}"
////                }
////                "cocItemDetailsSupervisor" -> {
////                    backValue = "redirect:/api/di/supervisor/coc-item-details/{id}"
////                }
////                /**
////                 * TODO: Its duplicated
////                 */
////                "cocDetailsSupervisor" -> {
////                    backValue = "redirect:/api/di/supervisor/coc-details/{id}"
////                }
////            }
////            backValue
////        } catch (e: Exception) {
////            KotlinLogging.logger { }.info { e }
////            "redirect:/home"
////        }
////    }
////
////
//    /***************************************************************************************************
//     * Thymeleaf Inspection Module Controllers
//     ************************************************************************************************/
//    /**
//     * Coc Officer Inspection
//     */
//
////    // Inspection officer
////    @GetMapping("officer/inspection-cd/{inspectionType}")
////    fun officerInspectionCd(model: Model, @PathVariable("inspectionType") inspectionType: String) : String {
////        return try {
////            model.addAttribute("inspectionType", inspectionType)
////            "destination-inspection/coc-Inspection-module/actual-inspection"
////        } catch (e: Exception) {
////            KotlinLogging.logger { }.info { e }
////            "redirect:/all-cocs"
////        }
////    }
////
////    //Officer opening the inspection item details for coc being inspected consignment details
////    @GetMapping("officer/coc-inspection-item-details/{id}")
////    fun inspectionItemsDetails(@PathVariable("id") id: Long,model: Model): String {
////        model.addAttribute("item", iConsignmentItemsRepo.findByIdOrNull(id))
////        return "destination-inspection/coc-Inspection-module/inspection-item-details"
////    }
////
////
//////
////    /***************************************************************************************************
////     * Thymeleaf  Reports Controllers
////     ************************************************************************************************/
////    /**
////     * Coc reports View
////     */
//
//}
