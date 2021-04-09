//package org.kebs.app.kotlin.apollo.api.controllers.diControllers.ncrs
//
//import mu.KotlinLogging
//import org.kebs.app.kotlin.apollo.common.exceptions.SupervisorNotFoundException
//import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
//import org.kebs.app.kotlin.apollo.store.model.ConsignmentDocumentEntity
//import org.kebs.app.kotlin.apollo.store.model.RemarksEntity
//import org.kebs.app.kotlin.apollo.store.repo.*
//import org.kebs.app.kotlin.apollo.store.repo.di.*
//import org.springframework.data.domain.PageRequest
//import org.springframework.data.repository.findByIdOrNull
//import org.springframework.stereotype.Controller
//import org.springframework.ui.Model
//import org.springframework.web.bind.annotation.*
//import java.sql.Timestamp
//import java.time.Instant
//
//
//@Controller
//@RequestMapping("/api/di/ncr/")
//@SessionAttributes("ncrs","ncr","itemCocs","status","item","itemCerts", "itemIdf", "certBack","remarks")
//class NCRController(
//        private val destinationInspectionRepo: IDestinationInspectionRepository,
//        private val iConsignmentItemsRepo: IConsignmentItemsRepository,
//        private val applicationMapProperties: ApplicationMapProperties,
//        private val iUsersRepo: IUserRepository,
//        private val iLaboratoryRepo: ILaboratoryRepository,
//        private val iLabParametersRepo: ILabParametersRepository,
//        private val idfItemRepo: IdfItemsEntityRepository,
//        private val iRemarksRepo: IRemarksRepository,
//        private  val iCheckListRepository: ICheckListRepository,
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
//        private val iCdStatusesValuesRepository: ICdStatusesValuesRepository
//
//) {
//    val importInspection: Int = applicationMapProperties.mapImportInspection
//    val statusEntity = iCdStatusesValuesRepository.findByServiceMapId(importInspection)
//    @GetMapping("supervisor")
//    fun supervisor(): String{
//        return "destination-inspection/ncr/import-inspection-supervisor"
//    }
//
//    /**
//     * Consignment with coc Home
//     */
//    @GetMapping("supervisor/{id}/all-ncrs")
//    fun SupervisorNcr(
//            @PathVariable("id") id: Long,
//            @RequestParam(value = "currentPage", required = false) currentPage: Int,
//            @RequestParam(value = "pageSize", required = false) pageSize: Int,
//            model: Model): String {
//        PageRequest.of(currentPage, pageSize)
//                .let { page ->
//                    destinationInspectionRepo.findBySupervisorIdAndNcrIdNotNull(id, page)
//                            ?.let { cocs ->
//                                KotlinLogging.logger {  }.error { cocs.count() }
//
//                                model.addAttribute("ncrs", cocs)
//                                KotlinLogging.logger {  }.error { cocs.count() }
//                                model.addAttribute("page", cocs);
//                                model.addAttribute("status", iCdStatusesValuesRepository.findByServiceMapId(importInspection))
//                                return "destination-inspection/ncr/consignment-with-ncrs"
//                            }
//                            ?:throw SupervisorNotFoundException("The supervisor with the following [id=$id], does not exist")
//                }
//    }
//
//
//    //Get coc selected details
//    @GetMapping("supervisor/ncr-details/{id}")
//    fun supervisorNcrDetails(@PathVariable("id") id: Long, model: Model): String {
//        return try {
//            val cocDoc = destinationInspectionRepo?.findByIdOrNull(id)
//            model.addAttribute("itemCerts", cocDoc?.cocId?.id?.let { iCocItemRepository.findByCocId(it) })
//            model.addAttribute("itemIdf", cocDoc?.idfId?.id?.let { idfItemRepo.findByIdfId(it) })
//            model.addAttribute("itemCocs", iConsignmentItemsRepo.findByConsigmentId(id))
//            model.addAttribute("ncr", cocDoc)
//            model.addAttribute("remarks", iRemarksRepo.findByConsignmentDocumentId(id))
//            model.addAttribute("remarkData", RemarksEntity())
//            model.addAttribute("status", statusEntity)
//            "destination-inspection/ncr/consignment-with-ncrs-details"
//        } catch (e: Exception) {
//            KotlinLogging.logger {  }.error { "Caught an exception $e" }
//            "redirect:supervisor/{id}/all-ncrs"
//        }
//    }
//
//    //Officer opening the item details for coc consignment details
//    @GetMapping("supervisor/ncr-item-details/{id}")
//    fun supervisorItemsDetails(@PathVariable("id") id: Long, model: Model): String {
//        return try {
//            model.addAttribute("item", iConsignmentItemsRepo.findByIdOrNull(id))
//            "destination-inspection/ncr/consignment-with-ncrs-item-details"
//        } catch (e: Exception) {
//            KotlinLogging.logger {  }.error { "Caught an exception $e" }
//            "redirect:officer/all-cocs"
//        }
//    }
//
//    // Supervisor add remarks
//    @PostMapping("supervisor/coc-add-remarks/{id}/{remarksType}")
//    fun supervisorRemarks(@PathVariable("id") id: Long, @PathVariable("remarksType") remarksType: String, @ModelAttribute coc: ConsignmentDocumentEntity, @ModelAttribute remarkData: RemarksEntity) : String {
//        return try {
//            destinationInspectionRepo.findByIdOrNull(id)
//                    ?.let {doc->
//                        iUsersRepo.findByIdOrNull(doc.supervisorId)
//                                ?.let {userDetails->
//                                    remarkData.firstName = userDetails?.firstName
//                                    remarkData.lastName = userDetails?.lastName
//                                    remarkData.userId = userDetails?.id
//                                    remarkData.consignmentDocumentId = doc.id
//                                    remarkData.remarkStatus = 1
//                                    remarkData.createdBy = "admin"
//                                    remarkData.createdOn = Timestamp.from(Instant.now())
//
//                                    when {
//                                        remarksType.equals("addRemarks") -> {
//                                            doc.supervisorRemarksStatus = 1
//                                            remarkData.remarksProcess = statusEntity?.addProcess
//                                        }
//
//                                    }
//                                    iRemarksAddRepo.save(remarkData)
//                                    destinationInspectionRepo.save(doc)
//                                }
//                        "redirect:/api/di/ncr/supervisor/ncr-details/{id}"
//                    }
//                    ?:throw Exception("Record not found")
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.info { e }
//            "redirect:/all-cocs"
//        }
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
//    @GetMapping("idf-view/{certBack}")
//    fun idfView(model: Model, @PathVariable("certBack") certBack: String) : String {
//        return try {
//            model.addAttribute("certBack",certBack)
//            "destination-inspection/ncr/idf-document"
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.info { e }
//            "redirect:/home"
//        }
//    }
//
//    @GetMapping("certificate-of-non-compliance/{certBack}")
//    fun certificateOfCompliance(model: Model, @PathVariable("certBack") certBack: String) : String {
//        return try {
//            model.addAttribute("certBack",certBack)
//            "destination-inspection/ncr/certificate-of-compliance"
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.info { e }
//            "redirect:/home"
//        }
//    }
//
//    @GetMapping("certificate/{id}/{close}")
//    fun certClose(model: Model, @PathVariable("id") id: Long, @PathVariable("close") close: String) : String {
//        return try {
//            var backValue = ""
//            when {
//                close.equals("actual") -> {
//                    backValue = "redirect:/api/di/officer/inspection-cd"
//                }
//                close.equals("physical") -> {
//                    backValue = "redirect:/api/di/officer/inspection-cd"
//                }
//                close.equals("cocDetailsOfficer") -> {
//                    backValue = "redirect:/api/di/officer/coc-details/{id}"
//                }
//                close.equals("cocDetailsSupervisor") -> {
//                    backValue = "redirect:/api/di/supervisor/coc-details/{id}"
//                }
//                close.equals("cocItemDetailsSupervisor") -> {
//                    backValue = "redirect:/api/di/supervisor/coc-item-details/{id}"
//                }
//                close.equals("cocDetailsSupervisor") -> {
//                    backValue = "redirect:/api/di/supervisor/coc-details/{id}"
//                }
//            }
//            backValue
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.info { e }
//            "redirect:/home"
//        }
//    }
//
//
//
//
//}