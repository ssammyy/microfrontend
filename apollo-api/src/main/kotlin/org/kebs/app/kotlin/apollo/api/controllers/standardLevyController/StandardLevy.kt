package org.kebs.app.kotlin.apollo.api.controllers.standardLevyController

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.StandardsLevyBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.exceptions.InvalidInputException
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.store.model.SlVisitUploadsEntity
import org.kebs.app.kotlin.apollo.store.model.StandardLevyFactoryVisitReportEntity
import org.kebs.app.kotlin.apollo.store.model.registration.CompanyProfileEntity
import org.kebs.app.kotlin.apollo.store.repo.ICompanyProfileRepository
import org.kebs.app.kotlin.apollo.store.repo.ISlVisitUploadsRepository
import org.kebs.app.kotlin.apollo.store.repo.IStandardLevyFactoryVisitReportRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import javax.servlet.http.HttpServletResponse


@Controller
@RequestMapping("/api/sl/")
class StandardLevy(
    private val standardLevyFactoryVisitReportRepository: IStandardLevyFactoryVisitReportRepository,
    private val commonDaoServices: CommonDaoServices,
    private val standardsLevyBpmn: StandardsLevyBpmn,
    private val companyProfileRepo: ICompanyProfileRepository,
    private val slUploadsRepo: ISlVisitUploadsRepository,
) {

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

    @PostMapping("/save-visit-report-data")
    fun saveFactoryVisitReport(
        @RequestParam("manufacturerId") manufacturerId: Long, reportData: StandardLevyFactoryVisitReportEntity,
        @RequestParam("docFileName", required = false) docFileName: String?,
        @RequestParam("docFile", required = false) docFile: MultipartFile?
    ): String {
        standardLevyFactoryVisitReportRepository.findByManufacturerEntity(manufacturerId)?.let { report ->
            report.remarks = reportData.remarks
            report.purpose = reportData.purpose
            report.actionTaken = reportData.actionTaken
            report.personMet = reportData.personMet
            report.visitDate = reportData.visitDate
            report.status = 0
            report.reportDate = LocalDate.now()
            report.createdBy = commonDaoServices.checkLoggedInUser()
            report.createdOn = Timestamp.from(Instant.now())
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


            standardsLevyBpmn.slsvPrepareVisitReportComplete(report.id ?: throw NullValueNotAllowedException("Invalid Report Id"), commonDaoServices.loggedInUserDetails().id ?: throw NullValueNotAllowedException("Invalid Session"))

            return "redirect:/sl/manufacturer?manufacturerId=${manufacturerId}"
        } ?: throw InvalidInputException("Please enter a valid id")
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
            report.assistanceManagerRemarks = reportData.assistanceManagerRemarks
            standardLevyFactoryVisitReportRepository.save(report)
            report.id?.let { standardsLevyBpmn.slsvApproveReportAsstManagerComplete(it, 33, true) }
            return "redirect:/sl/manufacturer?manufacturerId=${manufacturerId}"
        } ?: throw InvalidInputException("No Report found")
    }

    @PostMapping("manager-approval")
    fun managerApproval(@RequestParam("manufacturerId") manufacturerId: Long, reportData: StandardLevyFactoryVisitReportEntity): String {
        standardLevyFactoryVisitReportRepository.findByManufacturerEntity(manufacturerId)?.let { report ->
            report.managersApproval = 1
            report.assistanceManagerRemarks = reportData.assistanceManagerRemarks
            standardLevyFactoryVisitReportRepository.save(report)
            report.id?.let { standardsLevyBpmn.slsvApproveReportManagerComplete(it, 33, true) }
            return "redirect:/sl/manufacturer?manufacturerId=${manufacturerId}"
        } ?: throw InvalidInputException("No Report found")
    }
}
