package org.kebs.app.kotlin.apollo.api.controllers.standardLevyController

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.StandardsLevyBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.service.FileStorageService
import org.kebs.app.kotlin.apollo.api.service.UserRolesService
import org.kebs.app.kotlin.apollo.common.exceptions.InvalidInputException
import org.kebs.app.kotlin.apollo.store.model.StandardLevyFactoryVisitReportEntity
import org.kebs.app.kotlin.apollo.store.model.registration.CompanyProfileEntity
import org.kebs.app.kotlin.apollo.store.repo.ICompanyProfileRepository
import org.kebs.app.kotlin.apollo.store.repo.IStandardLevyFactoryVisitReportRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import java.lang.Exception
import java.sql.Date
import java.time.LocalDate



@Controller
@RequestMapping("/api/sl/")
class StandardLevy(
    private val standardLevyFactoryVisitReportRepository: IStandardLevyFactoryVisitReportRepository,
    private val commonDaoServices: CommonDaoServices,
    private val standardsLevyBpmn: StandardsLevyBpmn,
    private val userRolesService: UserRolesService,
    private val companyProfileRepo: ICompanyProfileRepository,
    private val storageService: FileStorageService
) {
    @GetMapping("filter-by-date-created")
    fun filterByDateCreated(
        @RequestParam("fromDate") fromDate: String,
        @RequestParam("toDate") toDate: String,
        @RequestParam("whereTo") whereTo: String,
        model: Model
    ): String {
        when (whereTo) {
            "filter" -> {
                companyProfileRepo.findAllByCreatedOnBetween(Date.valueOf(fromDate), Date.valueOf(toDate))
                    ?.let { manufacturers ->
                        model.addAttribute("manufacturers", manufacturers)
                    } ?: throw InvalidInputException("No Data found")
            }
        }
        return "standard-levy/manufacturers"
    }

    @PostMapping("/save-visit-report-data")
//    @Transactional(readOnly = true)
    fun saveFactoryVisitReport(
        @RequestParam("manufacturerId") manufacturerId: Long,
        @RequestParam("factoryReport") factoryReport: MultipartFile,
        reportData: StandardLevyFactoryVisitReportEntity
    ): String {
        standardLevyFactoryVisitReportRepository.findByManufacturerEntity(manufacturerId)?.let { report ->
            report.remarks = reportData.remarks
            report.purpose = reportData.purpose
            report.actionTaken = reportData.actionTaken
            report.personMet = reportData.personMet
            report.assistantManager = reportData.assistantManager
            report.principalLevyOfficer = reportData.principalLevyOfficer ?: report.principalLevyOfficer
            report.reportDate = LocalDate.now()
            report.id?.let { report.createdBy?.let { it1 ->
                report.createdOn?.let { it2 ->
                    storageService.store(factoryReport, it,
                        it1, it2
                    )
                }
            } }
            standardLevyFactoryVisitReportRepository.save(report)
            report.id?.let {
                reportData.assistantManager?.let { it1 ->
                    standardsLevyBpmn.slsvPrepareVisitReportComplete(
                        it, it1
                    )
                }
            }
            return "redirect:/sl/manufacturer?manufacturerId=${manufacturerId}"
        } ?: throw InvalidInputException("Please enter a valid id")
    }

    @PostMapping("update-manufacturer")
    fun updateManufacturerDetails(
        @RequestParam("manufacturerId") manufacturerId: Long,
        companyProfile: CompanyProfileEntity
    ): String {
        KotlinLogging.logger { }.info { "Company Details ===> " + manufacturerId }
        commonDaoServices.findCompanyProfileWithID(manufacturerId).let { manufacturerDetails ->
            manufacturerDetails.ownership = companyProfile.ownership
            manufacturerDetails.closureOfOperations = companyProfile.closureOfOperations
            manufacturerDetails.postalAddress = companyProfile.postalAddress
            companyProfileRepo.save(manufacturerDetails)
            return "redirect:/sl/manufacturer?manufacturerId=${manufacturerId}"
        }
    }

    @PostMapping("assistance-manager-approval")
    fun assistanceManagerApproval(
        @RequestParam("manufacturerId") manufacturerId: Long,
        reportData: StandardLevyFactoryVisitReportEntity
    ): String {
        standardLevyFactoryVisitReportRepository.findByManufacturerEntity(manufacturerId)?.let { report ->
            report.assistantManagerApproval = 1
            report.assistanceManagerRemarks = reportData.assistanceManagerRemarks
            report.slManager = reportData.slManager
            standardLevyFactoryVisitReportRepository.save(report)
            report.id?.let {
                reportData.slManager?.let { it1 ->
                    standardsLevyBpmn.slsvApproveReportAsstManagerComplete(
                        it,
                        it1, true
                    )
                }
            }
            return "redirect:/sl/manufacturer?manufacturerId=${manufacturerId}"
        } ?: throw InvalidInputException("No Report found")
    }


    @PostMapping("manager-approval")
    fun managerApproval(
        @RequestParam("manufacturerId") manufacturerId: Long,
        reportData: StandardLevyFactoryVisitReportEntity
    ): String {
        standardLevyFactoryVisitReportRepository.findByManufacturerEntity(manufacturerId)?.let { report ->
            report.managersApproval = 1
            report.cheifManagerRemarks = reportData.cheifManagerRemarks
            report.principalLevyOfficer = reportData.principalLevyOfficer ?: report.principalLevyOfficer
            standardLevyFactoryVisitReportRepository.save(report)
            report.id?.let {
                reportData.principalLevyOfficer?.let { it1 ->
                    standardsLevyBpmn.slsvApproveReportManagerComplete(
                        it,
                        it1, true
                    )
                }
            }
            return "redirect:/sl/manufacturer?manufacturerId=${manufacturerId}"
        } ?: throw InvalidInputException("No Report found")
    }

    @PostMapping("draft-feedback")
    fun sendDraftFeedback(
        @RequestParam("manufacturerId") manufacturerId: Long,
        reportData: StandardLevyFactoryVisitReportEntity
    ): String {
        standardLevyFactoryVisitReportRepository.findByManufacturerEntity(manufacturerId)?.let { report ->
            report.managersApproval = 1
            report.feedBackRemarks = reportData.feedBackRemarks
            standardLevyFactoryVisitReportRepository.save(report)
            report.id?.let {
                standardsLevyBpmn.slsvDraftFeedbackComplete(it)
                standardsLevyBpmn.endSlSiteVisitProcess(it)
            }
            return "redirect:/sl/manufacturer?manufacturerId=${manufacturerId}"
        } ?: throw InvalidInputException("No Report found")
    }

}