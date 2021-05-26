package org.kebs.app.kotlin.apollo.api.controllers.standardLevyController

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.StandardsLevyBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.exceptions.InvalidInputException
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.KraPaymentsEntityDto
import org.kebs.app.kotlin.apollo.store.model.SlVisitUploadsEntity
import org.kebs.app.kotlin.apollo.store.model.StandardLevyFactoryVisitReportEntity
import org.kebs.app.kotlin.apollo.store.model.registration.CompanyProfileEntity
import org.kebs.app.kotlin.apollo.store.repo.ICompanyProfileRepository
import org.kebs.app.kotlin.apollo.store.repo.ISlVisitUploadsRepository
import org.kebs.app.kotlin.apollo.store.repo.IStandardLevyFactoryVisitReportRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.servlet.http.HttpServletResponse


@Controller
@RequestMapping("/api/sl/")
class StandardLevy(
    applicationMapProperties: ApplicationMapProperties,
    private val standardLevyFactoryVisitReportRepository: IStandardLevyFactoryVisitReportRepository,
    private val commonDaoServices: CommonDaoServices,
    private val standardsLevyBpmn: StandardsLevyBpmn,
    private val companyProfileRepo: ICompanyProfileRepository,
    private val slUploadsRepo: ISlVisitUploadsRepository,
    @PersistenceContext
    private val entityManager: EntityManager,
) {
    final val appId = applicationMapProperties.mapPermitApplication

    @GetMapping("/view/payment/details")
    fun fetchPaymentDetailsByEntryNo(
        model: Model,
        @RequestParam("entryNo") entryNo: Long
    ): String {
        val details = entityManager
            .createNamedQuery(KraPaymentsEntityDto.FIND_ALL, KraPaymentsEntityDto::class.java)
            .setParameter(1, entryNo)
            .resultList
        model.addAttribute("payments", details)
        return "standard-levy/payments-details"
    }

    @GetMapping("/view/uploaded")
    fun downloadFileDocument(
        response: HttpServletResponse,
        @RequestParam("fileID") fileID: Long
    ) {
        slUploadsRepo.findByIdOrNull(fileID)
            ?.let { fileUploaded ->
                val dto = CommonDaoServices.FileDTO()
                dto.document = fileUploaded.document
                dto.fileType = fileUploaded.fileType
                dto.name = fileUploaded.name
                commonDaoServices.downloadFile(response, dto)

            }
            ?: KotlinLogging.logger { }.info("File not found")


    }

    @PostMapping("/approval/level/two")
    @PreAuthorize("hasAuthority('SL_SECOND_APPROVE_VISIT_REPORT')")
    fun factoryVisitLevelTwoApproval(
        redirectAttributes: RedirectAttributes,
        @RequestParam("levelTwoRemarks", required = true) remarks: String,
        @RequestParam("approval", required = true) approval: String,
        @RequestParam("visitId", required = true) visitId: Long
    ): String {
        try {
            standardLevyFactoryVisitReportRepository.findByIdOrNull(visitId)
                ?.let { report ->
                    when (report.managersApproval) {
                        1 -> {
                            throw InvalidInputException("Attempt to approve an already approved report, aborting")
                        }
                        else -> {
                            if (report.slManager != null && report.slManager != commonDaoServices.loggedInUserDetails().id) {
                                throw InvalidInputException("Attempt to approve report but not the assignee")
                            } else {
                                when (approval) {
                                    "approve" -> {
                                        //Assistant manager approve complete
                                        standardsLevyBpmn.slsvApproveReportManagerComplete(
                                            report.id ?: throw NullValueNotAllowedException("Invalid Report ID"),
                                            report.principalLevyOfficer ?: throw NullValueNotAllowedException("Invalid User for approval"),
                                            true
                                        )
                                        report.managersApproval = 1
                                        report.cheifManagerRemarks = remarks
                                        report.slManager = commonDaoServices.loggedInUserDetails().id
                                        report.modifiedBy = "${commonDaoServices.checkLoggedInUser()}|${report.modifiedBy}"
                                        report.varField1 = "${report.modifiedOn}"
                                        report.status = report.status ?: 0 + 1
                                        report.slStatus = report.status
                                        report.modifiedOn = Timestamp.from(Instant.now())

                                        standardLevyFactoryVisitReportRepository.save(report)
                                        redirectAttributes.addFlashAttribute(
                                            "success",
                                            "You have approved the report."
                                        )
                                        return "redirect:/sl/manufacturer?manufacturerId=${report.manufacturerEntity}&appId=${appId}"

                                    }
                                    else -> {
                                        standardsLevyBpmn.slsvApproveReportManagerComplete(
                                            report.id ?: throw NullValueNotAllowedException("Invalid Report ID"),
                                            report.principalLevyOfficer ?: throw NullValueNotAllowedException("Invalid User for approval"),
                                            false
                                        )
                                        report.cheifManagerRemarks = remarks
                                        report.slManager = commonDaoServices.loggedInUserDetails().id
                                        report.modifiedBy = "${commonDaoServices.checkLoggedInUser()}|${report.modifiedBy}"
                                        report.varField1 = "${report.modifiedOn}"
                                        report.status = report.status ?: 0 + 1
                                        report.slStatus = report.status
                                        report.modifiedOn = Timestamp.from(Instant.now())
                                        redirectAttributes.addFlashAttribute(
                                            "success",
                                            "You have rejected the report."
                                        )
                                        /**
                                         * DONE: send back the task to the creator
                                         */
                                        standardLevyFactoryVisitReportRepository.save(report)

                                        return "redirect:/sl/manufacturer?manufacturerId=${report.manufacturerEntity}&appId=${appId}"
                                    }
                                }

                            }

                        }
                    }
                }
                ?: throw NullValueNotAllowedException("Invalid Visit Id")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            throw  e
        }


    }

    @PostMapping("/approval/level/one")
    @PreAuthorize("hasAuthority('SL_APPROVE_VISIT_REPORT')")
    fun factoryVisitLevelOneApproval(
        redirectAttributes: RedirectAttributes,
        @RequestParam("levelOneRemarks", required = true) remarks: String,
        @RequestParam("approval", required = true) approval: String,
        @RequestParam("visitId", required = true) visitId: Long,
        @RequestParam("slLevelTwoApproval", required = false) slLevelTwoApproval: Long?,
    ): String {
        try {
            standardLevyFactoryVisitReportRepository.findByIdOrNull(visitId)
                ?.let { report ->
                    when (report.assistantManagerApproval) {
                        1 -> {
                            throw InvalidInputException("Attempt to approve an already approved report, aborting")
                        }
                        else -> {
                            /**
                             * Is approval sent by selected approver
                             */
                            if (report.assistantManager != null && report.assistantManager != commonDaoServices.loggedInUserDetails().id) {
                                throw InvalidInputException("Attempt to approve report but not the assignee")
                            } else {
                                when (approval) {
                                    "approve" -> {
                                        //Assistant manager approve complete
                                        standardsLevyBpmn.slsvApproveReportAsstManagerComplete(
                                            report.id ?: throw NullValueNotAllowedException("Invalid Report ID"),
                                            slLevelTwoApproval ?: throw NullValueNotAllowedException("Invalid User for approval"),
                                            true
                                        )
                                        report.assistantManagerApproval = 1
                                        report.assistantManagerRemarks = remarks
                                        report.assistantManager = commonDaoServices.loggedInUserDetails().id
                                        report.modifiedBy = commonDaoServices.checkLoggedInUser()
                                        report.status = report.status ?: 0 + 1
                                        report.slStatus = report.status
                                        report.modifiedOn = Timestamp.from(Instant.now())
                                        report.slManager = slLevelTwoApproval

                                        standardLevyFactoryVisitReportRepository.save(report)
                                        redirectAttributes.addFlashAttribute(
                                            "success",
                                            "You have approved the report."
                                        )
                                        return "redirect:/sl/manufacturer?manufacturerId=${report.manufacturerEntity}&appId=${appId}"

                                    }
                                    else -> {
                                        standardsLevyBpmn.slsvApproveReportAsstManagerComplete(
                                            report.id ?: throw NullValueNotAllowedException("Invalid Report ID"),
                                            commonDaoServices.loggedInUserDetails().id ?: throw NullValueNotAllowedException("Invalid User for approval"),
                                            false
                                        )
                                        report.assistantManagerRemarks = remarks
                                        report.assistantManager = commonDaoServices.loggedInUserDetails().id
                                        report.modifiedBy = commonDaoServices.checkLoggedInUser()
                                        report.status = report.status ?: 0 + 1
                                        report.slStatus = report.status
                                        report.modifiedOn = Timestamp.from(Instant.now())
                                        redirectAttributes.addFlashAttribute(
                                            "success",
                                            "You have rejected the report."
                                        )
                                        /**
                                         * DONE: send back the task to the creator
                                         */
                                        standardLevyFactoryVisitReportRepository.save(report)

                                        return "redirect:/sl/manufacturer?manufacturerId=${report.manufacturerEntity}&appId=${appId}"
                                    }
                                }

                            }

                        }
                    }
                }
                ?: throw NullValueNotAllowedException("Invalid Visit Id")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            throw  e
        }


    }


    @PostMapping("/submit/draft/feedback")
//    @PreAuthorize("hasAuthority('SL_APPROVE_VISIT_REPORT')")
    fun factoryVisitSubmitFeedback(
        redirectAttributes: RedirectAttributes,
        @RequestParam("feedback", required = true) feedback: String,
        @RequestParam("visitId", required = true) visitId: Long
    ): String {
        try {
            standardLevyFactoryVisitReportRepository.findByIdOrNull(visitId)
                ?.let { report ->
                    report.officersFeedback = feedback
                    standardLevyFactoryVisitReportRepository.save(report)
                    val userId = companyProfileRepo.findByIdOrNull(report.manufacturerEntity ?: throw NullValueNotAllowedException("Report does not have a valid Manufacturer tied to it"))?.userId ?: throw NullValueNotAllowedException("No valid user assigned to this manufacturer")
                    standardsLevyBpmn.slsvDraftFeedbackComplete(report.id ?: throw NullValueNotAllowedException("Invalid Visit Id"), userId)
                    return "redirect:/sl/manufacturer?manufacturerId=${report.manufacturerEntity}"
                }
                ?: throw NullValueNotAllowedException("Invalid Visit Id")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            throw  e
        }

    }

    @PostMapping("/save-visit-report-data")
    fun saveFactoryVisitReport(
        @RequestParam("manufacturerId") manufacturerId: Long, visitReport: StandardLevyFactoryVisitReportEntity,
        @RequestParam("docFileName", required = false) docFileName: String?,
        @RequestParam("slLevelOneApproval", required = false) slLevelOneApproval: Long?,
        @RequestParam("docFile", required = false) docFile: MultipartFile?
    ): String {

        if (visitReport.id != null) {
            standardLevyFactoryVisitReportRepository.findByIdOrNull(visitReport.id)
                ?.let { report ->
                    report.remarks = visitReport.remarks
                    report.purpose = visitReport.purpose
                    report.actionTaken = visitReport.actionTaken
                    report.personMet = visitReport.personMet
                    report.visitDate = visitReport.visitDate
                    report.status = 0
                    report.reportDate = LocalDate.now()
                    report.createdBy = commonDaoServices.checkLoggedInUser()
                    report.createdOn = Timestamp.from(Instant.now())
                    report.assistantManager = slLevelOneApproval
                    report.principalLevyOfficer = commonDaoServices.loggedInUserDetails().id

                    standardLevyFactoryVisitReportRepository.save(report)
                    docFile
                        ?.let { file ->
                            docFileName
                                ?.let { fileName ->
                                    var uploads = SlVisitUploadsEntity().apply {
                                        name = commonDaoServices.saveDocuments(file)
                                        fileType = file.contentType
                                        documentType = fileName
                                        document = file.bytes
                                        visitId = report.id
                                        status = 1
                                        transactionDate = LocalDate.now()
                                        createdBy = commonDaoServices.checkLoggedInUser()
                                        createdOn = LocalDateTime.now()

                                    }
                                    uploads = slUploadsRepo.save(uploads)
                                    KotlinLogging.logger { }.info("Saved file ${uploads.documentType} with id ${uploads.id}")
                                }
                                ?: throw  InvalidInputException("Empty file name not allowed")
                        }
                        ?: KotlinLogging.logger { }.info("No file to upload")
                    standardsLevyBpmn.slsvPrepareVisitReportComplete(report.id ?: throw NullValueNotAllowedException("Invalid Report Id"), slLevelOneApproval ?: throw NullValueNotAllowedException("Invalid Session"))
                    return "redirect:/sl/manufacturer?manufacturerId=${manufacturerId}"
                }
                ?: throw InvalidInputException("Please enter a valid id")

        } else {
            throw NullValueNotAllowedException("Attempt to prepare report before visit is scheduled not allowed")
        }
    }

    @PostMapping("update-manufacturer")
    fun updateManufacturerDetails(@RequestParam("manufacturerId") manufacturerId: Long, manufacturer: CompanyProfileEntity): String {
        KotlinLogging.logger { }.info { "Company Details ===> $manufacturerId" }
        commonDaoServices.findCompanyProfileWithID(manufacturerId).let { manufacturerDetails ->
            manufacturerDetails.ownership = manufacturer.ownership
            manufacturerDetails.closureOfOperations = manufacturer.closureOfOperations
            manufacturerDetails.postalAddress = manufacturer.postalAddress
            manufacturerDetails.county = manufacturer.county
            manufacturerDetails.town = manufacturer.town
            manufacturerDetails.streetName = manufacturer.streetName
            manufacturerDetails.buildingName = manufacturer.buildingName
            manufacturerDetails.modifiedBy = commonDaoServices.checkLoggedInUser()
            manufacturerDetails.modifiedOn = Timestamp.from(Instant.now())
            companyProfileRepo.save(manufacturerDetails)
            return "redirect:/sl/manufacturer?manufacturerId=${manufacturerId}"
        }
    }

    @PostMapping("assistance-manager-approval")
    fun assistanceManagerApproval(@RequestParam("manufacturerId") manufacturerId: Long, reportData: StandardLevyFactoryVisitReportEntity): String {
        standardLevyFactoryVisitReportRepository.findByManufacturerEntity(manufacturerId)?.let { report ->
            report.assistantManagerApproval = 1
            report.assistantManagerRemarks = reportData.assistantManagerRemarks
            standardLevyFactoryVisitReportRepository.save(report)
            report.id?.let { standardsLevyBpmn.slsvApproveReportAsstManagerComplete(it, 33, true) }
            return "redirect:/sl/manufacturer?manufacturerId=${manufacturerId}"
        } ?: throw InvalidInputException("No Report found")
    }


    @PostMapping("manager-approval")
    fun managerApproval(@RequestParam("manufacturerId") manufacturerId: Long, reportData: StandardLevyFactoryVisitReportEntity): String {
        standardLevyFactoryVisitReportRepository.findByManufacturerEntity(manufacturerId)?.let { report ->
            report.managersApproval = 1
            report.assistantManagerRemarks = reportData.assistantManagerRemarks
            standardLevyFactoryVisitReportRepository.save(report)
            report.id?.let { standardsLevyBpmn.slsvApproveReportManagerComplete(it, 33, true) }
            return "redirect:/sl/manufacturer?manufacturerId=${manufacturerId}"
        } ?: throw InvalidInputException("No Report found")
    }


}
