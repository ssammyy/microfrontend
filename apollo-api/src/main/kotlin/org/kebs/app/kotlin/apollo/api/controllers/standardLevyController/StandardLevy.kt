//package org.kebs.app.kotlin.apollo.api.controllers.standardLevyController
//
//import mu.KotlinLogging
//import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.StandardsLevyBpmn
//import org.kebs.app.kotlin.apollo.api.ports.provided.dao.QualityAssuranceDaoServices
//import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
//import org.kebs.app.kotlin.apollo.store.model.ManufacturersEntity
//import org.kebs.app.kotlin.apollo.store.model.StandardLevyFactoryVisitReportEntity
//import org.kebs.app.kotlin.apollo.store.repo.IManufacturerRepository
//import org.kebs.app.kotlin.apollo.store.repo.IServiceMapsRepository
//import org.kebs.app.kotlin.apollo.store.repo.IStandardLevyFactoryVisitReportRepository
//import org.springframework.data.repository.findByIdOrNull
//import org.springframework.security.core.authority.SimpleGrantedAuthority
//import org.springframework.security.core.context.SecurityContextHolder
//import org.springframework.stereotype.Controller
//import org.springframework.ui.Model
//import org.springframework.web.bind.annotation.*
//import org.springframework.web.servlet.mvc.support.RedirectAttributes
//import java.net.ContentHandler
//import java.sql.Timestamp
//import java.time.Instant
//
//@Controller
//@RequestMapping("/sl")
//class StandardLevy(
//        private val standardLevyFactoryVisitReportRepo: IStandardLevyFactoryVisitReportRepository,
//        private val serviceMapsRepository: IServiceMapsRepository,
//        applicationMapProperties: ApplicationMapProperties,
//        private val manufacturerRepository: IManufacturerRepository,
//        private val qualityAssuranceDaoServices: QualityAssuranceDaoServices,
//        private val standardsLevyBpmn: StandardsLevyBpmn
//) {
//
//    private val slAssistantManager = applicationMapProperties.slAssistantManager
//
//
//    private final val appId = applicationMapProperties.mapPermitApplication
//
//    @PostMapping("/save-data")
//    fun saveData(
//            @ModelAttribute("standardLevyFactoryVisitReport") standardLevyFactoryVisitReport: StandardLevyFactoryVisitReportEntity,
//            @RequestParam("manufacturerId", required = false) manufacturerId: Long?,
//            @RequestParam("kraId", required = false) kraId: Long?,
//            redirectAttributes: RedirectAttributes
//    ): String {
//        serviceMapsRepository.findByIdOrNull(appId)
//                ?.let { map ->
//                    qualityAssuranceDaoServices.extractUserFromContext(SecurityContextHolder.getContext())
//                            ?.let {
//                                manufacturerRepository.findByIdOrNull(manufacturerId)
//                                        ?.let { manufacturer ->
//                                            with(standardLevyFactoryVisitReport){
//                                                status = map.inactiveStatus
//                                                createdOn = Timestamp.from(Instant.now())
//                                                createdBy = it.firstName
//                                                manufacturerEntity = manufacturer
//                                            }
//                                            standardLevyFactoryVisitReportRepo.save(standardLevyFactoryVisitReport)
//
//                                            //Prepare visit report complete
//                                            kraId?.let { it1 -> standardsLevyBpmn.slsvPrepareVisitReportComplete(it1, slAssistantManager) }
//                                            redirectAttributes.addFlashAttribute("alert", "You have submitted the factory inspection report for approval.")
//                                        }
//                            }
//                        }
//
//        return "redirect:/sl/manufacturer?manufacturerId=${manufacturerId}&appId=${appId}"
//    }
//
//    @PostMapping("/update-manufacturer")
//    fun updateManufacturerDetails(
//            model: Model,
//            redirectAttributes: RedirectAttributes,
//            @ModelAttribute("manufacturer") manufacturer: ManufacturersEntity,
//            @RequestParam("manufacturerId") manufacturerId: Long?
//            ): String {
//        var result = ""
//        serviceMapsRepository.findByIdOrNull(appId)
//                ?.let { map ->
//                    qualityAssuranceDaoServices.extractUserFromContext(SecurityContextHolder.getContext())
//                            ?.let {
//                                manufacturerRepository.findByIdOrNull(manufacturerId)
//                                        ?.let { extractedManufacturer ->
//                                            with(extractedManufacturer) {
//                                                manufacturer.postalAddress?.let { postalAddress = it }
//                                                manufacturer.ownership?.let { ownership = it }
//                                                manufacturer.closureOfOperations?.let { closureOfOperations = it }
//                                            }
//                                            manufacturerRepository.save(extractedManufacturer)
//
//                                            SecurityContextHolder.getContext().authentication.authorities
//                                                    ?.let { authorities ->
//                                                        if (authorities.contains(SimpleGrantedAuthority("PERMIT_APPLICATION"))) {
//                                                            result = "redirect:/api/v1/permit/customer"
//                                                        }
//                                                        else if(authorities.contains(SimpleGrantedAuthority("SL_MANUFACTURERS_VIEW")) ||
//                                                                authorities.contains(SimpleGrantedAuthority("SL_APPROVE_VISIT_REPORT")) ||
//                                                                authorities.contains(SimpleGrantedAuthority("SL_SECOND_APPROVE_VISIT_REPORT"))) {
//                                                            result = "redirect:/sl/manufacturer?manufacturerId=${extractedManufacturer.id}&appId=${map.id}"
//                                                        }
//                                                    }
//                                        }
//                            }
//                }
//
//        return result
//    }
//
//}