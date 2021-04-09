//package org.kebs.app.kotlin.apollo.api.controllers.diControllers.cors
//
//import mu.KotlinLogging
//import org.kebs.app.kotlin.apollo.common.exceptions.SupervisorNotFoundException
//import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
//import org.kebs.app.kotlin.apollo.store.model.*
//import org.kebs.app.kotlin.apollo.store.model.di.CdInspectionMinistryGeneralEntity
//import org.kebs.app.kotlin.apollo.store.repo.*
//import org.kebs.app.kotlin.apollo.store.repo.di.*
//import org.springframework.data.domain.PageRequest
//import org.springframework.data.repository.findByIdOrNull
//import org.springframework.stereotype.Controller
//import org.springframework.transaction.annotation.Propagation
//import org.springframework.transaction.annotation.Transactional
//import org.springframework.ui.Model
//import org.springframework.validation.BindingResult
//import org.springframework.web.bind.annotation.*
//import org.springframework.web.servlet.mvc.support.RedirectAttributes
//import java.sql.Timestamp
//import java.time.Instant
//import java.time.Instant.now
//import java.util.*
//import javax.servlet.http.HttpSession
//
//
//
//@Controller
//@RequestMapping("/api/di/cors/")
//@SessionAttributes("cors","coc","cocsM","cor","itemCocs","status","item","itemCerts", "itemIdf", "certBack", "officers", "inspectionType", "remarks", "filledChecklist")
//class DICORController(
//        private val serviceMapsRepo: IServiceMapsRepository,
//        private val destinationInspectionRepo: IDestinationInspectionRepository,
//        private val iConsignmentItemsRepo: IConsignmentItemsRepository,
//        private var applicationMapProperties: ApplicationMapProperties,
//        private val iUsersRepo: IUserRepository,
//        private val idfItemRepo: IdfItemsEntityRepository,
//        private val iRemarksRepo: IRemarksRepository,
//        private val iRemarksAddRepo: IRemarksAddRepository,
//        private val iCdStatusesValuesRepository: ICdStatusesValuesRepository,
//        private val iCorsItemsEntityRepository: ICorsItemsEntityRepository,
//        private val corsEntityRepository: CorsEntityRepository,
//        private val iMotorVehicleInspectionRepo: IMotorVehicleInspectionRepo,
//        private val iMinistryInspectionStatusRepo: IMinistryInspectionStatusRepo,
//        private val iCdInspectionMinistryGeneralEntityRepo: ICdInspectionMinistryGeneralEntityRepo,
//        private val iMinistryInspectionBodyWorkEntityRepo: IMinistryInspectionBodyWorkEntityRepo,
//        private val iMinistryInspectionEngineComponentsEntityRepo: IMinistryInspectionEngineComponentsEntityRepo,
//        private val iMinistryInspectionEngineFunctioningEntityRepo: IMinistryInspectionEngineFunctioningEntityRepo,
//        private val iMinistryInspectionUnderBodyInspectionEntityRepo: IMinistryInspectionUnderBodyInspectionEntityRepo,
//        private val iMinistryInspectionTestDriveEntityRepo: IMinistryInspectionTestDriveEntityRepo
//) {
//
//    val importInspection: Int = applicationMapProperties.mapImportInspection
//    val statusEntity = iCdStatusesValuesRepository.findByServiceMapId(importInspection)
//    final var appId: Int? = null
//
//    init {
//        appId = applicationMapProperties.mapMinistyInspectionSave
//    }
//
//
//    @GetMapping("supervisor/{id}/all-cors")
//    fun SupervisorCor(
//            @PathVariable("id") id: Long,
//            @RequestParam(value = "currentPage", required = false) currentPage: Int,
//            @RequestParam(value = "pageSize", required = false) pageSize: Int,
//            model: Model): String {
//        val assignedOfficers  = mutableListOf<UsersEntity>()
//        PageRequest.of(currentPage, pageSize)
//                .let { page ->
//
//                    destinationInspectionRepo.findBySupervisorIdAndCorIdNotNull(id, page)
//                            ?.let { cocs ->
////                                cocs.filter {
////       `                               it.assignIoId?.equals(assignedIO)!!
////                                }
//                                model.addAttribute("cors", cocs)
//                                cocs.forEach{coc ->
//                                    iUsersRepo.findByIdOrNull(coc.assignedIo)?.let { user ->
//                                        assignedOfficers.add(user)
//                                    }
//
//                                }
//
////                                KotlinLogging.logger { }.error {"ios" + cleanIOs?.count()}
//                                model.addAttribute("assignedOfficers",LinkedHashSet(assignedOfficers).toMutableList() )
//                                model.addAttribute("page", cocs)
//                                model.addAttribute("status", iCdStatusesValuesRepository.findByServiceMapId(importInspection))
//                                return "destination-inspection/cors/consignment-with-cor"
//                            }
//                            ?:throw SupervisorNotFoundException("The supervisor with the following [id=$id], does not exist")
//                }
//
//
//
//    }
//
//
//
////    fun uniqueArray(my_array: IntArray) {
////        println("Original Array : ")
////        for (i in my_array.indices) {
////            print(my_array[i].toString() + "\t")
////        }
////        //Assuming all elements in input array are unique
////        var noUniqueElements = my_array.size
////        //Comparing each element with all other elements
////        for (i in 0 until noUniqueElements) {
////            var j = i + 1
////            while (j < noUniqueElements) {
////                //If any two elements are found equal
////                if (my_array[i] == my_array[j]) { //Replace duplicate element with last unique element
////                    my_array[j] = my_array[noUniqueElements - 1]
////                    noUniqueElements--
////                    j--
////                }
////                j++
////            }
////        }
////        //Copying only unique elements of my_array into array1
////        val array1 = my_array.copyOf(noUniqueElements)
////        //Printing arrayWithoutDuplicates
////        println()
////        println("Array with unique values : ")
////        for (i in array1.indices) {
////            print(array1[i].toString() + "\t")
////        }
////        println()
////        println("---------------------------")
////    }
//
//
//    @GetMapping("officer/{id}/all-cors-ministry-inspection")
//    fun ministryInspectionCor(
//            @PathVariable("id") id: Long,
//            @RequestParam(value = "currentPage", required = false) currentPage: Int,
//            @RequestParam(value = "pageSize", required = false) pageSize: Int,
//            model: Model): String {
//        PageRequest.of(currentPage, pageSize)
//                .let { page ->
//                    destinationInspectionRepo.findByMinistryInspectionStatus(1, page)
//                            ?.let { cocs ->
//                                model.addAttribute("cocsM", cocs)
//                                model.addAttribute("page", cocs)
//                                KotlinLogging.logger { }.error { cocs.count() }
//                                model.addAttribute("status", iCdStatusesValuesRepository.findByServiceMapId(importInspection))
//                                return "destination-inspection/cors/ministry/consignment-with-cor"
//                            }
//                            ?:throw SupervisorNotFoundException("The supervisor with the following [id=$id], does not exist")
//                }
//    }
//
//    @GetMapping("ministry-inspection-cor-details/{id}")
//    fun ministryInspectionCorDetails(@PathVariable("id") id: Long, model: Model): String {
//        return try {
//            val cocDoc = destinationInspectionRepo.findByIdOrNull(id)
//            model.addAttribute("itemCerts", cocDoc?.cocId?.id?.let { iCorsItemsEntityRepository.findAllByCdId(it) })
//            model.addAttribute("itemIdf", cocDoc?.idfId?.id?.let { idfItemRepo.findByIdfId(it) })
//            model.addAttribute("itemCocs", iCorsItemsEntityRepository.findAllByCdId(id))
//            model.addAttribute("coc", cocDoc)
//            model.addAttribute("status", statusEntity)
//            model.addAttribute("remarkData", RemarksEntity())
//            "destination-inspection/cors/ministry/consignment-with-cor-ministry-details"
//        } catch (e: Exception) {
//            KotlinLogging.logger {  }.error { "Caught an exception $e" }
//            "redirect:supervisor/{id}/all-ncrs"
//        }
//    }
//
//    //Get coc selected details
//    @GetMapping("cor-details/{id}")
//    fun supervisorCorDetails(@PathVariable("id") id: Long, model: Model): String {
//        return try {
//            val cocDoc = destinationInspectionRepo.findByIdOrNull(id)
//            model.addAttribute("itemCerts", cocDoc?.cocId?.id?.let { iCorsItemsEntityRepository.findAllByCdId(it) })
//            model.addAttribute("itemIdf", cocDoc?.idfId?.id?.let { idfItemRepo.findByIdfId(it) })
//            model.addAttribute("itemCocs", iCorsItemsEntityRepository.findAllByCdId(id))
//            model.addAttribute("coc", cocDoc)
//            model.addAttribute("cor", cocDoc?.ucrNumber?.let { corsEntityRepository.findByUcrNumber(it) })
//            model.addAttribute("remarks", iRemarksRepo.findByConsignmentDocumentId(id))
//            model.addAttribute("remarkData", RemarksEntity())
//            model.addAttribute("status", statusEntity)
//            model.addAttribute("officers", iUsersRepo.findAll())
//            "destination-inspection/cors/consignment-with-cors-details"
//        } catch (e: Exception) {
//            KotlinLogging.logger {  }.error { "Caught an exception $e" }
//            "redirect:supervisor/{id}/all-ncrs"
//        }
//    }
//
//    //assigning an officer
//
//    //     Submit assign officer
//    @PostMapping("supervisor/assign/officer/{id}")
//    fun saveAssignOfficer(@ModelAttribute coc: ConsignmentDocumentEntity, @PathVariable("id") id: Long ): String {
//        return try{
//            KotlinLogging.logger { }.info { "assigned=${coc.assignedIo} id= ${coc.id}"}
//            destinationInspectionRepo.findByIdOrNull(coc.id)
//                    ?.let { doc->
//                        doc.assignedIo = coc.assignedIo
//                        doc.assignIoId = coc.assignedIo?.let { iUsersRepo.findFirstByIdAndStatus(it, 1) }
//                        doc.saveReason = statusEntity?.assignedStatus
//                        doc.cdStatus = statusEntity?.assignedStatus
//                        doc.approvalStatus = statusEntity?.initialStatus
//                        doc.assignedStatus = 1
//                        destinationInspectionRepo.save(doc)
//                    }
//            "redirect:/api/di/cors/cor-details/{id}"
//        } catch (e: Exception){
//            KotlinLogging.logger {  }.info { e }
//            "redirect:supervisor/all-cocs"
//        }
//    }
//
//    //Cor remarks
//    // Supervisor add remarks
//    @PostMapping("cor-action-add-remarks/{id}/{remarksType}")
//    fun supervisorRemarks(@PathVariable("id") id: Long,@PathVariable("remarksType") remarksType: String, @ModelAttribute coc: ConsignmentDocumentEntity) : String {
//        return try {
//            destinationInspectionRepo.findByIdOrNull(coc.id)
//                    ?.let {doc->
//                        val remarkData = RemarksEntity()
//                        remarkData.firstName = doc.assignIoId?.firstName
//                        remarkData.lastName = doc.assignIoId?.lastName
//                        remarkData.userId = doc.assignedIo
//                        remarkData.consignmentDocumentId = doc.id
//                        remarkData.remarkStatus = 1
//                        remarkData.createdBy = "admin"
//                        remarkData.createdOn = Timestamp.from(Instant.now())
//
//                        when {
//                            remarksType == "addRemarks" -> {
//                                doc.officerRemarksStatus = 1
//                                doc.officerRemarks = coc.officerRemarks
//                                remarkData.remarks = coc.officerRemarks
//                                remarkData.remarksProcess = statusEntity?.addProcess
//                            }
//                            remarksType == "reject" -> {
//                                doc.rejectedStatus = 1
//                                doc.saveReason = statusEntity?.rejectProcess
//                                doc.cdStatus = statusEntity?.rejectRemarksStatus
//                                doc.rejectedReason = coc.rejectedReason
//                                remarkData.remarks = coc.rejectedReason
//                                remarkData.remarksProcess = statusEntity?.rejectProcess
//                            }
//                            remarksType == "processRejection" -> {
//                                doc.processRejectionStatus = 1
//                                doc.saveReason = statusEntity?.processRejectionProcess
//                                doc.cdStatus = statusEntity?.processRejectionStatus
//                                doc.processRejectionRemarks = coc.processRejectionRemarks
//                                remarkData.remarks = coc.processRejectionRemarks
//                                remarkData.remarksProcess = statusEntity?.processRejectionProcess
//                            }
//                            remarksType == "defer" -> {
//                                remarkData.remarks = coc.deferReason
//                                doc.deferReason = coc.deferReason
//                                doc.deferStatus = 1
//                                doc.saveReason = statusEntity?.deferProcess
//                                doc.cdStatus = statusEntity?.deferStatus
//                                remarkData.remarksProcess = statusEntity?.deferProcess
//                            }
//                            remarksType == "targetInspection" -> {
//                                doc.targetedStatus = 1
//                                doc.targetApproved = 0
//                                doc.saveReason = statusEntity?.supervisorTargetProcess
//                                doc.cdStatus = statusEntity?.officerTargetProcess
//                                doc.targetedRemarks = coc.targetedRemarks
//                                remarkData.remarks = coc.targetedRemarks
//                                remarkData.remarksProcess = statusEntity?.supervisorTargetProcess
//                            }
//                            remarksType == "blackList" -> {
//                                doc.blacklistStatus = 1
//                                doc.blacklistApproved = 0
//                                doc.blacklistRequestDate = java.util.Date.from(Instant.now())
//                                doc.blacklistUser = coc.blacklistUser
//                                doc.saveReason = statusEntity?.officerBlacklistProcess
//                                doc.cdStatus = statusEntity?.officerBlacklistStatus
//                                doc.blacklistRemarks = coc.blacklistRemarks
//                                remarkData.remarks = coc.blacklistRemarks
//                                remarkData.remarksProcess = statusEntity?.officerBlacklistProcess
//                            }
//                            remarksType == "approve" -> {
//                                remarkData.remarks = coc.approveRemarks
//                                doc.approveRemarks = coc.approveRemarks
//                                doc.approvalStatus = statusEntity?.approveRemarksStatus
//                                doc.approvalDate = java.util.Date.from(Instant.now())
//                                doc.saveReason = statusEntity?.approveProcess
//                                doc.cdStatus = statusEntity?.approveRemarksStatus
//                                remarkData.remarksProcess = statusEntity?.approveProcess
//                            }
//                            remarksType == "assignMinistry" -> {
//                                doc.ministryInspectionAssignedDate = Date.from(now())
//                                doc.ministryInspectionStatus = 1
//                                doc.ministryInspectionRemarks = statusEntity?.ministryAssignment
//                            }
//                        }
//                        iRemarksAddRepo.save(remarkData)
//                        destinationInspectionRepo.save(doc)
//
//                        "redirect:/api/di/cors/cor-details/{id}"
//                    }
//                    ?:throw Exception("Record not found")
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.info { e }
//            "supervisor/{id}/all-cors"
//        }
//    }
//
//    // Supervisor add remarks
//    @PostMapping("cor-action-add-remarks-officer/{id}/{remarksType}")
//    fun officerRemarks(@PathVariable("id") id: Long,@PathVariable("remarksType") remarksType: String, @ModelAttribute coc: ConsignmentDocumentEntity) : String {
//        return try {
//            destinationInspectionRepo.findByIdOrNull(coc.id)
//                    ?.let {doc->
//                        val remarkData = RemarksEntity()
//                        remarkData.firstName = doc.assignIoId?.firstName
//                        remarkData.lastName = doc.assignIoId?.lastName
//                        remarkData.userId = doc.assignedIo
//                        remarkData.consignmentDocumentId = doc.id
//                        remarkData.remarkStatus = 1
//                        remarkData.createdBy = "admin"
//                        remarkData.createdOn = Timestamp.from(Instant.now())
//
//                        when {
//                            remarksType.equals("addRemarks") -> {
//                                doc.officerRemarksStatus = 1
//                                doc.officerRemarks = coc.officerRemarks
//                                remarkData.remarks = coc.officerRemarks
//                                remarkData.remarksProcess = statusEntity?.addProcess
//                            }
//                            remarksType.equals("reject") -> {
//                                doc.rejectedStatus = 1
//                                doc.saveReason = statusEntity?.rejectProcess
//                                doc.cdStatus = statusEntity?.rejectRemarksStatus
//                                doc.rejectedReason = coc.rejectedReason
//                                remarkData.remarks = coc.rejectedReason
//                                remarkData.remarksProcess = statusEntity?.rejectProcess
//                            }
//                            remarksType.equals("processRejection") -> {
//                                doc.processRejectionStatus = 1
//                                doc.saveReason = statusEntity?.processRejectionProcess
//                                doc.cdStatus = statusEntity?.processRejectionStatus
//                                doc.processRejectionRemarks = coc.processRejectionRemarks
//                                remarkData.remarks = coc.processRejectionRemarks
//                                remarkData.remarksProcess = statusEntity?.processRejectionProcess
//                            }
//                            remarksType.equals("defer") -> {
//                                remarkData.remarks = coc.deferReason
//                                doc.deferReason = coc.deferReason
//                                doc.deferStatus = 1
//                                doc.saveReason = statusEntity?.deferProcess
//                                doc.cdStatus = statusEntity?.deferStatus
//                                remarkData.remarksProcess = statusEntity?.deferProcess
//                            }
//                            remarksType.equals("targetInspection") -> {
//                                doc.targetedStatus = 1
//                                doc.targetApproved = 0
//                                doc.saveReason = statusEntity?.supervisorTargetProcess
//                                doc.cdStatus = statusEntity?.officerTargetProcess
//                                doc.targetedRemarks = coc.targetedRemarks
//                                remarkData.remarks = coc.targetedRemarks
//                                remarkData.remarksProcess = statusEntity?.supervisorTargetProcess
//                            }
//                            remarksType.equals("blackList") -> {
//                                doc.blacklistStatus = 1
//                                doc.blacklistApproved = 0
//                                doc.blacklistRequestDate = java.util.Date.from(Instant.now())
//                                doc.blacklistUser = coc.blacklistUser
//                                doc.saveReason = statusEntity?.officerBlacklistProcess
//                                doc.cdStatus = statusEntity?.officerBlacklistStatus
//                                doc.blacklistRemarks = coc.blacklistRemarks
//                                remarkData.remarks = coc.blacklistRemarks
//                                remarkData.remarksProcess = statusEntity?.officerBlacklistProcess
//                            }
//                            remarksType.equals("approve") -> {
//                                remarkData.remarks = coc.approveRemarks
//                                doc.approveRemarks = coc.approveRemarks
//                                doc.approvalStatus = statusEntity?.approveRemarksStatus
//                                doc.approvalDate = java.util.Date.from(Instant.now())
//                                doc.saveReason = statusEntity?.approveProcess
//                                doc.cdStatus = statusEntity?.approveRemarksStatus
//                                remarkData.remarksProcess = statusEntity?.approveProcess
//                            }
//                            remarksType.equals("assignMinistry") ->{
//                                remarkData.remarks = coc.ministryInspectionRemarks
//                                doc.ministryInspectionStatus = 1
//                                doc.ministryInspectionAssignedDate = Date.from(Instant.now())
//                                doc.saveReason = statusEntity?.ministryAssignment
//                                doc.cdStatus = statusEntity?.ministryAssignment
//                                remarkData.remarksProcess = statusEntity?.ministryAssignment
//                            }
//                        }
//                        iRemarksAddRepo.save(remarkData)
//                        destinationInspectionRepo.save(doc)
//
//                        "redirect:/api/di/cors/officer/cor-details/{id}"
//                    }
//                    ?:throw Exception("Record not found")
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.info { e }
//            "officer/{id}/all-cors"
//        }
//    }
//
//    //Cors officers sections
//    @GetMapping("officer/{id}/all-cors")
//    fun OfficerCor(
//            @PathVariable("id") id: Long,
//            @RequestParam(value = "currentPage", required = false) currentPage: Int,
//            @RequestParam(value = "pageSize", required = false) pageSize: Int,
//            model: Model): String {
//        PageRequest.of(currentPage, pageSize)
//                .let { page ->
//                    destinationInspectionRepo.findByAssignedIoAndCorIdIsNotNull(id, page)
//                            ?.let { cocs ->
//                                model.addAttribute("cos", cocs)
//                                KotlinLogging.logger { }.error { cocs.count() }
//                                model.addAttribute("page", cocs)
//                                model.addAttribute("status", iCdStatusesValuesRepository.findByServiceMapId(importInspection))
//                                return "destination-inspection/cors/officer/consignment-with-cor"
//                            }
//                            ?:throw SupervisorNotFoundException("The Officer with the following [id=$id], does not exist")
//                }
//    }
//
//    //Get coc selected details
//    @GetMapping("officer/cor-details/{id}")
//    fun officerCocDetails(@PathVariable("id") id: Long, session: HttpSession, model: Model): String {
//        return try {
//            val cocDoc = destinationInspectionRepo.findByIdOrNull(id)
//            model.addAttribute("itemCerts", cocDoc?.cocId?.id?.let { iCorsItemsEntityRepository.findAllByCdId(it) })
//            model.addAttribute("itemIdf", cocDoc?.idfId?.id?.let { idfItemRepo.findByIdfId(it) })
//            model.addAttribute("itemCocs", iCorsItemsEntityRepository.findAllByCdId(id))
//            model.addAttribute("coc", cocDoc)
//            model.addAttribute("remarks", iRemarksRepo.findByConsignmentDocumentId(id))
//            model.addAttribute("remarkData", RemarksEntity())
//            model.addAttribute("status", statusEntity)
//            model.addAttribute("officers", iUsersRepo.findAll())
//            "destination-inspection/cors/officer/consignment-with-cor-details"
//        } catch (e: Exception) {
//            KotlinLogging.logger {  }.error { "Caught an exception $e" }
//            "redirect:officer/all-cocs"
//        }
//    }
////
//
//    // Inspection officer certBack
//    @GetMapping("officer/inspection-cd/{certBack}")
//    fun officerInspectionCd(model: Model, @PathVariable("certBack") certBack: String) : String {
//        return try {
//            model.addAttribute("cor", certBack)
//            model.addAttribute("checklistForm", MotorVehicleInspectionEntity())
//            "destination-inspection/cors/officer/motor-vehicle-inspection"
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.info { e }
//            "redirect:/all-cocs"
//        }
//    }
//
//    @PostMapping("officer/checklist-post")
//    fun checklistPost(@ModelAttribute checklistForm: MotorVehicleInspectionEntity) : String{
//        return try {
//            checklistForm.createdBy = "Admin"
//            checklistForm.createdOn = Timestamp.from(Instant.now())
//            iMotorVehicleInspectionRepo.save(checklistForm)
//            "destination-inspection/cors/officer/motor-vehicle-inspection"
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.info { e }
//            "redirect:/api/di/cors/officer/checklist-post"
//        }
//    }
//
//
//    @GetMapping("motovehicle-checklist-report/{chassisNo}")
//    fun motovehicleChecklistReport(@PathVariable("chassisNo") chasisNumber: String, model: Model): String {
//        return try {
//            model.addAttribute("checklist", iMotorVehicleInspectionRepo.findFirstByChasisNumber(chasisNumber))
//            KotlinLogging.logger { }.info { iMotorVehicleInspectionRepo.findFirstByChasisNumber(chasisNumber)?.chasisNumber }
//            "destination-inspection/cors/officer/MotoVehicleInspectionReport"
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.info { e }
//            "redirect:/api/di/cors/officer/checklist-post"
//        }
//
//    }
//
//
//    @GetMapping("ministry-inspection/{chasisNumber}")
//    fun motoVehicleInspectionView(@PathVariable("chasisNumber") chasisNumber: String, model: Model): String {
//        model.addAttribute("corDetail", iMotorVehicleInspectionRepo.findFirstByChasisNumber(chasisNumber))
//        model.addAttribute("statuses", iMinistryInspectionStatusRepo.findAllByIdNotNull())
//        model.addAttribute("ministryInspectionGenerralEntity", CdInspectionMinistryGeneralEntity())
//        model.addAttribute("ministryInspectionBodyWorkEntity", MinistryInspectionBodyWorkEntity())
//        model.addAttribute("ministryInspectionEngineFunctioningEntity", MinistryInspectionEngineFunctioningEntity())
//        model.addAttribute("ministryInspectionTestDriveEntity", MinistryInspectionTestDriveEntity())
//        model.addAttribute("ministryInspectionUnderBodyInspectionEntity", MinistryInspectionUnderBodyInspectionEntity())
//        model.addAttribute("ministryInspectionEngineComponentsEntity", MinistryInspectionEngineComponentsEntity())
//        return "destination-inspection/cors/officer/MinistryInspectionMotoVehicle"
//    }
//
//    @PostMapping("ministry-inspection-form-save")
//    fun ministryInspectionFormSave(
//            model: Model,
//            @ModelAttribute("ministryInspectionGenerralEntity") cdInspectionMinistryGeneralEntity: CdInspectionMinistryGeneralEntity,
//            @ModelAttribute("ministryInspectionBodyWorkEntity") ministryInspectionBodyWorkEntity: MinistryInspectionBodyWorkEntity,
//            @ModelAttribute("ministryInspectionEngineComponentsEntity") ministryInspectionEngineComponentsEntity: MinistryInspectionEngineComponentsEntity,
//            @ModelAttribute("ministryInspectionEngineFunctioningEntity") ministryInspectionEngineFunctioningEntity: MinistryInspectionEngineFunctioningEntity,
//            @ModelAttribute("ministryInspectionUnderBodyInspectionEntity") ministryInspectionUnderBodyInspectionEntity: MinistryInspectionUnderBodyInspectionEntity,
//            @ModelAttribute("ministryInspectionTestDriveEntity") ministryInspectionTestDriveEntity: MinistryInspectionTestDriveEntity,
//            results: BindingResult,
//            redirectAttributes: RedirectAttributes
//    ): String {
//
//
//        KotlinLogging.logger { }.info { "something new " }
//
//        serviceMapsRepo.findByIdAndStatus(appId, 1)
//                ?.let { map ->
//                    saveMinistryInspectionChecklist(cdInspectionMinistryGeneralEntity, map, ministryInspectionBodyWorkEntity, ministryInspectionEngineComponentsEntity, ministryInspectionEngineFunctioningEntity, ministryInspectionUnderBodyInspectionEntity, ministryInspectionTestDriveEntity)
//                    ///updateMinistryInspectionStatus(ConsignmentDocumentEntity())
//                }
//
//
//        return "destination-inspection/cors/officer/MinistryInspectionMotoVehicle"
//
//
//    }
//
////    private fun updateMinistryInspectionStatus( consignmentDocumentEntity: ConsignmentDocumentEntity){
////
////        consignmentDocumentEntity.ministryInspectionInspectedDate = Date.from(Instant.now())
////        consignmentDocumentEntity.ministryInspectionInspectedStatus = 1
////        consignmentDocumentEntity.ministryInspectionInspectedRemarks = statusEntity?.ministryAssignment
////        return try {
////            destinationInspectionRepo.save(consignmentDocumentEntity)
////        }catch (e: Exception) {
////            KotlinLogging.logger { }.info { e }
////        }
////    }
//
//    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
//    fun saveMinistryInspectionChecklist(cdInspectionMinistryGeneralEntity: CdInspectionMinistryGeneralEntity, map: ServiceMapsEntity, ministryInspectionBodyWorkEntity: MinistryInspectionBodyWorkEntity, ministryInspectionEngineComponentsEntity: MinistryInspectionEngineComponentsEntity, ministryInspectionEngineFunctioningEntity: MinistryInspectionEngineFunctioningEntity, ministryInspectionUnderBodyInspectionEntity: MinistryInspectionUnderBodyInspectionEntity, ministryInspectionTestDriveEntity: MinistryInspectionTestDriveEntity) {
//        var generralEntity = cdInspectionMinistryGeneralEntity
//
//        with(generralEntity) {
//            status = map.initStatus
//            createdBy = "Admin"
//            createdOn = Timestamp.from(Instant.now())
//        }
//        generralEntity = iCdInspectionMinistryGeneralEntityRepo.save(generralEntity)
//        with(ministryInspectionBodyWorkEntity) {
//            status = map.activeStatus
//            createdBy = generralEntity.createdBy
//            createdOn = Timestamp.from(Instant.now())
//            generarInspection = generralEntity.id
//        }
//        iMinistryInspectionBodyWorkEntityRepo.save(ministryInspectionBodyWorkEntity)
//
//        with(ministryInspectionEngineComponentsEntity) {
//            status = map.activeStatus
//            createdBy = generralEntity.createdBy
//            createdOn = Timestamp.from(Instant.now())
//            generarInspection = generralEntity.id
//        }
//        iMinistryInspectionEngineComponentsEntityRepo.save(ministryInspectionEngineComponentsEntity)
//        KotlinLogging.logger { }.trace("${this::ministryInspectionFormSave.name} saved with id =[${ministryInspectionEngineComponentsEntity.id}] ")
//
//        with(ministryInspectionEngineFunctioningEntity) {
//            status = map.activeStatus
//            createdBy = generralEntity.createdBy
//            createdOn = Timestamp.from(Instant.now())
//            generarInspection = generralEntity.id
//        }
//        iMinistryInspectionEngineFunctioningEntityRepo.save(ministryInspectionEngineFunctioningEntity)
//        KotlinLogging.logger { }.trace("${this::ministryInspectionFormSave.name} saved with id =[${ministryInspectionEngineComponentsEntity.id}] ")
//
//        with(ministryInspectionUnderBodyInspectionEntity) {
//            status = map.activeStatus
//            createdBy = generralEntity.createdBy
//            createdOn = Timestamp.from(Instant.now())
//            generarInspection = generralEntity.id
//        }
//        iMinistryInspectionUnderBodyInspectionEntityRepo.save(ministryInspectionUnderBodyInspectionEntity)
//        KotlinLogging.logger { }.trace("${this::ministryInspectionFormSave.name} saved with id =[${ministryInspectionEngineComponentsEntity.id}] ")
//
//        with(ministryInspectionTestDriveEntity) {
//            status = map.activeStatus
//            createdBy = generralEntity.createdBy
//            createdOn = Timestamp.from(Instant.now())
//            generarInspection = generralEntity.id
//        }
//        iMinistryInspectionTestDriveEntityRepo.save(ministryInspectionTestDriveEntity)
//        KotlinLogging.logger { }.trace("${this::ministryInspectionFormSave.name} saved with id =[${ministryInspectionEngineComponentsEntity.id}] ")
//    }
//
//    @GetMapping("ministry-inspection-report/{chasisNumber}")
//    fun ministryInspectionReport(@PathVariable("chasisNumber") chasisNumber: String, model: Model): String {
//        iCdInspectionMinistryGeneralEntityRepo.findFirstByChassisNo(chasisNumber)
//                .let { generalInfo ->
//                    model.addAttribute("generalInfo", generalInfo)
//                    model.addAttribute("bodyInspection", generalInfo?.id?.let { iMinistryInspectionBodyWorkEntityRepo.findFirstByGenerarInspection(it) })
//                    model.addAttribute("engineComponents", generalInfo?.id?.let { iMinistryInspectionEngineComponentsEntityRepo.findFirstByGenerarInspection(it) })
//                    model.addAttribute("engineFunctioning", generalInfo?.id?.let { iMinistryInspectionEngineFunctioningEntityRepo.findFirstByGenerarInspection(it) })
//                    model.addAttribute("testDrive", generalInfo?.id?.let { iMinistryInspectionTestDriveEntityRepo.findFirstByGenerarInspection(it) })
//                    model.addAttribute("underBodyInspection", generalInfo?.id?.let { iMinistryInspectionUnderBodyInspectionEntityRepo.findFirstByGenerarInspection(it) })
//                }
//
//        return "destination-inspection/cors/officer/MinistryInspectionMotoVehicleReport"
//    }
//
//    @GetMapping("remarks-view/{id}")
//    fun remarksView(model: Model, @PathVariable("id") id: Long) : String {
//        return try {
//            model.addAttribute("remarks",iRemarksRepo.findByConsignmentDocumentId(id))
//            "destination-inspection/ncr/RemarksView"
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.info { e }
//            "redirect:/home"
//        }
//    }
//
//
//
//
//
//
//
//
//
//
//
//
//}