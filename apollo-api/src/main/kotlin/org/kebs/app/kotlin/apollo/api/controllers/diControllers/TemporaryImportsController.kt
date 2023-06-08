//package org.kebs.app.kotlin.apollo.api.controllers.diControllers
//
//import com.fasterxml.jackson.databind.ObjectMapper
//import mu.KotlinLogging
//import org.json.JSONObject
//import org.kebs.app.kotlin.apollo.common.exceptions.SupervisorNotFoundException
//import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
//import org.kebs.app.kotlin.apollo.store.model.ConsignmentDocumentEntity
//import org.kebs.app.kotlin.apollo.store.model.RemarksEntity
//import org.kebs.app.kotlin.apollo.store.model.TemporaryImportsEntity
//import org.kebs.app.kotlin.apollo.store.model.UsersEntity
//import org.kebs.app.kotlin.apollo.store.repo.*
//import org.springframework.data.domain.PageRequest
//import org.springframework.data.repository.findByIdOrNull
//import org.springframework.security.access.prepost.PreAuthorize
//import org.springframework.security.core.context.SecurityContextHolder
//import org.springframework.stereotype.Controller
//import org.springframework.ui.Model
//import org.springframework.web.bind.annotation.*
//import java.sql.Timestamp
//import java.time.Instant
//import java.util.*
//import javax.servlet.http.HttpSession
//
//@Controller
//@RequestMapping("/api/di/")
//@SessionAttributes("temp_imports", "status", "coc")
//class TemporaryImportsController(
//        private val iUsersRepo: IUserRepository,
//        private val applicationMapProperties: ApplicationMapProperties,
//        private val iCdStatusesValuesRepository: ICdStatusesValuesRepository,
//        private val iTemporaryImportsRepository: ITemporaryImportsRepository,
//        private val iArrivalPointRepo: IArrivalPointRepository,
//        private val iConsignmentDocumentRepository: IConsignmentDocumentRepository,
//        private val iCocItemRepository: ICocItemRepository,
//        private val idfItemRepo: IdfItemsEntityRepository,
//        private val iConsignmentItemsRepo: IConsignmentItemsRepository,
//        private val iRemarksRepo: IRemarksRepository,
//        private val iTargetedCdInspectionRepo: ITargetedCdInspectionRepository
//) {
//
//
//    private final val importInspection: Int = applicationMapProperties.mapImportInspection
//    val statusEntity = iCdStatusesValuesRepository.findByServiceMapId(importInspection)
//
//    @PreAuthorize("hasAuthority('IMPORTER')")
//    @GetMapping("temporary/imports")
//    fun importer(
//            model: Model
//    ): String{
//        model.addAttribute("temporaryImportsEntity", TemporaryImportsEntity())
//        return "destination-inspection/temporary-imports/importer-application-form"
//    }
//
//
//    @GetMapping("temporary-imports")
//    fun temporaryImports(
//            @RequestParam(value = "id", required = false) id: Long,
//            @RequestParam(value = "section", required = false) section: String,
//            @RequestParam(value = "userRole", required = false) userRole: Int,
//            @RequestParam(value = "currentPage", required = false) currentPage: Int,
//            @RequestParam(value = "pageSize", required = false) pageSize: Int,
//            model: Model): String {
//
//
//        PageRequest.of(currentPage, pageSize)
//                .let { page ->
//                    var myRole = 0
//                    SecurityContextHolder.getContext().authentication
//                            ?.let { auth ->
//                                when (userRole) {
//                                    statusEntity?.diSupervisorRoleId -> {
//                                        model.addAttribute("temp_imports", iTemporaryImportsRepository.findAll(page));
//                                    }
//                                    statusEntity?.diOfficerRoleId -> {
//                                        myRole = userRole
//                                        model.addAttribute("temp_imports", iTemporaryImportsRepository.findByAssignIoId(id, page))
//                                    }
//                                    else -> throw SupervisorNotFoundException("The user with the following [userRole=$userRole], can't access this page")
//                                }
//                            }
//
//                    model.addAttribute("userRole", myRole)
//                    model.addAttribute("status", statusEntity)
//                    return "destination-inspection/temporary-imports/temp-imports"
//                }
//    }
//
//    @GetMapping("temporary-imports/cd-details")
//    fun cocDetails(
//            @RequestParam(value = "id", required = false) id: Long,
//            model: Model): String {
//        iTemporaryImportsRepository.findByIdOrNull(id)?.let { temp_imports ->
//            temp_imports.ucrNumber?.let {
//                iConsignmentDocumentRepository.findByUcrNumber(it)
//                        ?.let { cocDoc ->
//
//                            cocDoc.cocId?.id
//                                    ?.let { cocId ->
//                                        iCocItemRepository.findByCocId(cocId)
//                                                .let { itemCert ->
//                                                    cocDoc.idfId?.id
//                                                            ?.let { idfId ->
//                                                                idfItemRepo.findByIdfId(idfId)
//                                                                        .let { itemIdf ->
//                                                                            cocDoc.id
//                                                                                    ?.let { cocId ->
//                                                                                        iConsignmentItemsRepo.findByConsigmentId(cocId)
//                                                                                                .let { itemCoCs ->
//                                                                                                    statusEntity?.diOfficerUserTypeId
//                                                                                                            ?.let { userType ->
//                                                                                                                iUsersRepo.findByUserTypes(userType)
//                                                                                                                        .let { diOfficers ->
//                                                                                                                            model.addAttribute("itemCerts", itemCert)
//                                                                                                                            model.addAttribute("itemIdf", itemIdf)
//                                                                                                                            model.addAttribute("coc", cocDoc)
//                                                                                                                            model.addAttribute("myCoc", ObjectMapper().writeValueAsString(cocDoc))
//                                                                                                                            model.addAttribute("itemCoCs", itemCoCs)
//                                                                                                                            model.addAttribute("targets", iTargetedCdInspectionRepo.findAll())
//                                                                                                                            model.addAttribute("remarks", cocDoc.remarksEntity)
//                                                                                                                            model.addAttribute("remarkData", RemarksEntity())
//                                                                                                                            model.addAttribute("status", statusEntity)
//                                                                                                                            model.addAttribute("officers", diOfficers)
//                                                                                                                            model.addAttribute("temp_imports", temp_imports)
//                                                                                                                            return "destination-inspection/temporary-imports/consignment-document"
//                                                                                                                        }
//                                                                                                            }
//                                                                                                }
//                                                                                    }
//
//                                                                        }
//                                                            }
//                                                }
//                                    }
//                        }
//            }
//        }
//                ?: throw SupervisorNotFoundException("The consignment Details with the following [id=$id], does not exist")
//    }
//
//
//    @PostMapping("temporary-imports/assign/officer")
//    fun saveAssignedOfficer(
//            @ModelAttribute coc: ConsignmentDocumentEntity,
//            session: HttpSession
//    ): String {
//        return try {
//            iUsersRepo.findByIdOrNull(coc.assignedIo)?.let { userEntity ->
//                iConsignmentDocumentRepository.findByIdOrNull(coc.id)?.let { consignmentDocumentEntity ->
//                    consignmentDocumentEntity.assignIoId = userEntity
//                    iConsignmentDocumentRepository.save(consignmentDocumentEntity).let {
//                        iTemporaryImportsRepository.findByIdOrNull(coc.temporaryImportsID)?.let { temporaryImportsEntity ->
//
//
//                            temporaryImportsEntity.assignIoId = userEntity
//                            temporaryImportsEntity.assignedStatus = 1
//
//                            iTemporaryImportsRepository.save(temporaryImportsEntity)
//                            //Todo send notification assigned IO
//                        }
//
//                    }
//                }
//            }
//
//            "redirect:/api/di/temporary-imports/cd-details/?id=${coc.temporaryImportsID}"
//
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.info { e }
////            "redirect:/api/di/temporary-imports?id=${session.currentUser.id},userRole = ${session.currentUser.userTypes.defaultRole.id},section = ${session.section.section},currentPage=0,pageSize=10"
//            "redirect:api/di/home"
//        }
//    }
//
//    @PostMapping("temporary-imports/cd-details/add-remarks")
//    fun remarks(
//            @ModelAttribute coc: ConsignmentDocumentEntity,
//            @ModelAttribute remarkData: RemarksEntity
//    ): String {
//        return try {
//            iConsignmentDocumentRepository.findByIdOrNull(coc.id)?.let { cd ->
//                    var remarksEntity = RemarksEntity()
//                    remarksEntity.userId = coc.assignedIo
//                    remarksEntity.remarks = coc.remarks
//                    remarksEntity.createdBy = coc.createdBy
//                    remarksEntity.consignmentDocumentId = coc
//                    remarksEntity.remarkStatus = statusEntity?.statusIsOne
//                    remarksEntity.createdOn = Timestamp.from(Instant.now())
//                    iRemarksRepo.save(remarksEntity)
//                    cd.remarks = coc.remarks
//                    iConsignmentDocumentRepository.save(cd)
//            }
//            "redirect:/api/di/temporary-imports/cd-details/?id=${coc.temporaryImportsID}"
//
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.info { e }
//            "redirect:api/di/home"
//        }
//    }
//
//}