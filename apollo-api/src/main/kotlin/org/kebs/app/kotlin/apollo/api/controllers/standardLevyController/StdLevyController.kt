package org.kebs.app.kotlin.apollo.api.controllers.standardLevyController

import com.google.gson.Gson
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.StandardsLevyBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.RegistrationDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.StandardLevyService
import org.kebs.app.kotlin.apollo.api.ports.provided.makeAnyNotBeNull
import org.kebs.app.kotlin.apollo.common.dto.CompanySl1DTO
import org.kebs.app.kotlin.apollo.common.dto.ManufactureSubmitEntityDto
import org.kebs.app.kotlin.apollo.common.dto.std.ResponseMessage
import org.kebs.app.kotlin.apollo.common.dto.std.ServerResponse
import org.kebs.app.kotlin.apollo.common.dto.std.TaskDetailsBody
import org.kebs.app.kotlin.apollo.common.dto.stdLevy.*
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.InvalidInputException
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.registration.CompanyProfileEditEntity
import org.kebs.app.kotlin.apollo.store.model.registration.CompanyProfileEntity
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/v1/migration/stdLevy")
class StdLevyController(
    private val applicationMapProperties: ApplicationMapProperties,
    private val standardLevyFactoryVisitReportRepository: IStandardLevyFactoryVisitReportRepository,
    private val commonDaoServices: CommonDaoServices,
    private val standardsLevyBpmn: StandardsLevyBpmn,
    private val companyProfileRepo: ICompanyProfileRepository,
    private val slUploadsRepo: ISlVisitUploadsRepository,
    private val userRolesRepo: IUserRoleAssignmentsRepository,
    @PersistenceContext
    private val entityManager: EntityManager,
    private val standardLevyService: StandardLevyService,
    private val daoServices: RegistrationDaoServices,
    private val standardLevyFactoryVisitReportRepo: IStandardLevyFactoryVisitReportRepository,
    private val standardLevyOperationsClosureRepository: StandardLevyOperationsClosureRepository

    ) {

    @PostMapping("/deploy")
    fun deployWorkflow(): ServerResponse {
        standardLevyService.deployProcessDefinition()
        return ServerResponse(HttpStatus.OK,"Successfully deployed server", HttpStatus.OK)
    }

//    @GetMapping("/getManufactureList")
//    @ResponseBody
//    fun getManufactureList(): MutableIterable<CompanyProfileEntity>
//    {
//        return standardLevyService.getManufactureList()
//    }



    final val appId = applicationMapProperties.mapPermitApplication

    //@PreAuthorize("hasAuthority('USER')")
    @PostMapping("kebs/add/manufacture-details/save")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun addManufactureDetails(
        @ModelAttribute("companyProfileEntity") companyProfileEntity: CompanyProfileEntity,
        model: Model,
        results: BindingResult,
        response: HttpServletResponse,
        redirectAttributes: RedirectAttributes
    ): String? {
        val result: ServiceRequestsEntity?
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val companyProfile = companyProfileEntity.registrationNumber?.let { commonDaoServices.findCompanyProfileWithRegistrationNumber(it) }

        if (companyProfile==null){
            val brsCheckUp = daoServices.checkBrs(companyProfileEntity)
            if (brsCheckUp.first){

                result = brsCheckUp.second?.let { daoServices.addUserManufactureProfile(map, loggedInUser, companyProfileEntity, it) }?: throw ExpectedDataNotFound("The Company Details Verification details could not be found")

                val sm = CommonDaoServices.MessageSuccessFailDTO()
                sm.closeLink = "${applicationMapProperties.baseUrlValue}/user/user-profile?userName=${loggedInUser.userName}"
                sm.message = "You have successful Added your Company details, Verify was success"
                return returnValues(result, map, sm)
            }else{
                throw ExpectedDataNotFound("The Company Details Verification failed Due to Invalid Registration Number or Director Id Failed")
            }
        }else{
            throw ExpectedDataNotFound("The Company with this [Registration Number : ${companyProfile.registrationNumber}] already exist")
        }
    }

    private fun returnValues(
        result: ServiceRequestsEntity,
        map: ServiceMapsEntity,
        sm: CommonDaoServices.MessageSuccessFailDTO
    ): String? {
        return when (result.status) {
            map.successStatus -> "${commonDaoServices.successLink}?message=${sm.message}&closeLink=${sm.closeLink}"
            else -> map.failureNotificationUrl
        }
    }



    @GetMapping("/angular/view/payment/detail")
    fun fetchPaymentDetailsByEntryNoForAngular(
        model: Model,
        @RequestParam("entryNo") entryNo: Long
    ): List<KraPaymentsEntityDto> {
        val userId = commonDaoServices.loggedInUserDetails().id ?: -3L
        val isEmployee = userRolesRepo.findByUserIdAndRoleIdAndStatus(userId, applicationMapProperties.slEmployeeRoleId ?: throw NullValueNotAllowedException("Role definition for employees not done"), 1)?.id != null
        if (isEmployee) {
            val details = entityManager
                .createNamedQuery(KraPaymentsEntityDto.FIND_ALL, KraPaymentsEntityDto::class.java)
                .setParameter(1, entryNo)
                .resultList
            return details

        } else {
            val manufacturerId = companyProfileRepo.findByUserId(userId)?.id ?: throw NullValueNotAllowedException("Invalid Request")
            val details = entityManager
                .createNamedQuery(KraPaymentsEntityDto.FIND_ALL, KraPaymentsEntityDto::class.java)
                .setParameter(1, manufacturerId)
                .resultList
            return details
        }



    }
    @GetMapping("/angular/view/uploaded")
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

    @PostMapping("/angular/approval/level/two")
    @PreAuthorize("hasAuthority('SL_SECOND_APPROVE_VISIT_REPORT')")
    fun factoryVisitLevelTwoApprovalAngular(
        redirectAttributes: RedirectAttributes,
        @RequestParam("levelTwoRemarks", required = true) remarks: String,
        @RequestParam("approval", required = true) approval: String,
        @RequestParam("visitId", required = true) visitId: Long
    ): StandardLevyFactoryVisitReportEntity {
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
                                        return report

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

                                        return report
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
    @PostMapping("/angular/approval/level/one")
    @PreAuthorize("hasAuthority('SL_APPROVE_VISIT_REPORT')")
    fun factoryVisitLevelOneApprovalAngular(
        redirectAttributes: RedirectAttributes,
        @RequestParam("levelOneRemarks", required = true) remarks: String,
        @RequestParam("approval", required = true) approval: String,
        @RequestParam("visitId", required = true) visitId: Long,
        @RequestParam("slLevelTwoApproval", required = false) slLevelTwoApproval: Long?,
    ):  StandardLevyFactoryVisitReportEntity {
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
                                        return report

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

                                        return report
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

    @PostMapping("/angular/submit/draft/feedback")
    @PreAuthorize("hasAuthority('SL_SEND_FEEDBACK')")
    fun factoryVisitSubmitFeedbackAngular(
        redirectAttributes: RedirectAttributes,
        @RequestParam("feedback", required = true) feedback: String,
        @RequestParam("visitId", required = true) visitId: Long
    ):  StandardLevyFactoryVisitReportEntity {
        try {
            standardLevyFactoryVisitReportRepository.findByIdOrNull(visitId)
                ?.let { report ->
                    report.officersFeedback = feedback
                    standardLevyFactoryVisitReportRepository.save(report)
                    val userId = companyProfileRepo.findByIdOrNull(report.manufacturerEntity ?: throw NullValueNotAllowedException("Report does not have a valid Manufacturer tied to it"))?.userId ?: throw NullValueNotAllowedException("No valid user assigned to this manufacturer")
                    standardsLevyBpmn.slsvDraftFeedbackComplete(report.id ?: throw NullValueNotAllowedException("Invalid Visit Id"), userId)
                    return report
                }
                ?: throw NullValueNotAllowedException("Invalid Visit Id")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            throw  e
        }

    }

    @PostMapping("/angular/save-visit-report-data")
    fun saveFactoryVisitReportAngular(
        @RequestParam("manufacturerId") manufacturerId: Long, visitReport: StandardLevyFactoryVisitReportEntity,
        @RequestParam("docFileName", required = false) docFileName: String?,
        @RequestParam("slLevelOneApproval", required = false) slLevelOneApproval: Long?,
        @RequestParam("docFile", required = false) docFile: MultipartFile?
    ):  StandardLevyFactoryVisitReportEntity {

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
                    return report
                }
                ?: throw InvalidInputException("Please enter a valid id")

        } else {
            throw NullValueNotAllowedException("Attempt to prepare report before visit is scheduled not allowed")
        }
    }

    @PostMapping("/angular-update-manufacturer")
    fun updateManufacturerDetailsAngular(@RequestParam("manufacturerId") manufacturerId: Long, manufacturer: CompanyProfileEntity): CompanyProfileEntity {
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
            return manufacturerDetails
        }
    }

    @PostMapping("/assistance-manager-approval-ang")
    fun assistanceManagerApprovalAngular(@RequestParam("manufacturerId") manufacturerId: Long, reportData: StandardLevyFactoryVisitReportEntity): StandardLevyFactoryVisitReportEntity {
        standardLevyFactoryVisitReportRepository.findByManufacturerEntity(manufacturerId)?.let { report ->
            report.assistantManagerApproval = 1
            report.assistantManagerRemarks = reportData.assistantManagerRemarks
            standardLevyFactoryVisitReportRepository.save(report)
            report.id?.let { standardsLevyBpmn.slsvApproveReportAsstManagerComplete(it, 33, true) }
            return report
        } ?: throw InvalidInputException("No Report found")
    }

    @PostMapping("/manager-approval-ang")
    fun managerApprovalAngular(@RequestParam("manufacturerId") manufacturerId: Long, reportData: StandardLevyFactoryVisitReportEntity): StandardLevyFactoryVisitReportEntity {
        standardLevyFactoryVisitReportRepository.findByManufacturerEntity(manufacturerId)?.let { report ->
            report.managersApproval = 1
            report.assistantManagerRemarks = reportData.assistantManagerRemarks
            standardLevyFactoryVisitReportRepository.save(report)
            report.id?.let { standardsLevyBpmn.slsvApproveReportManagerComplete(it, 33, true) }
            return report
        } ?: throw InvalidInputException("No Report found")
    }

    @GetMapping("/getManufacturerPenaltyHistory")
    fun getManufacturerPenaltyHistory():MutableIterable<StagingStandardsLevyManufacturerPenalty>
    {
        return standardLevyService.getManufacturerPenaltyHistory()
    }

    @GetMapping("/getManufacturerPenalty")
    fun getManufacturerPenalty():MutableIterable<StagingStandardsLevyManufacturerPenalty>
    {
        return standardLevyService.getManufacturerPenalty()
    }
    @GetMapping("/getPaidLevies")
    fun getPaidLevies():MutableIterable<StandardLevyPaymentsEntity>
    {
        return standardLevyService.getPaidLevies()
    }

    @GetMapping("/getCompanyProfile")
    fun getCompanyProfile(): MutableList<CompanyProfileEntity> {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        return commonDaoServices.findCompanyProfileDetail(loggedInUser.id?:throw  Exception("INVALID USER ID FOUND"))

    }

    class RegistrationDetails {
        var stdLevyNotificationFormEntity: StdLevyNotificationFormEntity? = null
        var manufacturer: ManufacturersEntity? = null
        var companySl1DTO: CompanySl1DTO? = null
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/save-sl-notification-form")
    @ResponseBody
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun saveNotificationFormSL(
        @RequestBody stdLevyNotificationFormDTO: StdLevyNotificationFormDTO,
        stdLevyNotificationForm: StdLevyNotificationForm,
        s: ServiceMapsEntity,
        sr: ServiceRequestsEntity,
        companyProfileEntity: CompanyProfileEntity,
    ): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Successfully uploaded Form",daoServices.saveNotificationFormSL(stdLevyNotificationFormDTO,stdLevyNotificationForm,s,sr,companyProfileEntity))
        //return ServerResponse(HttpStatus.OK,"Successfully uploaded Justification",response)
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/submit-registration-manufacture")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun submitRegistrationDetails(
       // @RequestParam( "companyProfileID") companyProfileID: Long,
         stdLevyNotificationFormEntity: StdLevyNotificationFormEntity,
        @ModelAttribute("companyProfileEntity") companyProfileEntity: CompanyProfileEntity,
         companySl1DTO: CompanySl1DTO,
         manufacturer: ManufacturersEntity,
        s: ServiceMapsEntity,
        sr: ServiceRequestsEntity,
        model: Model
    ): String? {

        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        val result: ServiceRequestsEntity?

        val myDetails = ManufactureSubmitEntityDto()
        with(myDetails) {
            submittedStatus = 1

        }
        //println(manufacturer)
        val gson = Gson()
       // KotlinLogging.logger { }.info { "Manufacturer" + gson.toJson(manufacturer) }
        KotlinLogging.logger { }.info { "stdLevyNotificationFormEntity" + gson.toJson(stdLevyNotificationFormEntity) }
        KotlinLogging.logger { }.info { "companyProfileEntity" + gson.toJson(companyProfileEntity) }
        KotlinLogging.logger { }.info { "companySl1DTO" + gson.toJson(companySl1DTO) }


        //daoServices.manufacturersInit(manufacturer, sr, loggedInUser, s)
        result = daoServices.closeManufactureRegistrationDetails(map, loggedInUser, myDetails)
        //Generation of Entry Number
        daoServices.generateEntryNumberDetails(map, loggedInUser)
       daoServices.manufacturerStdLevyInit(stdLevyNotificationFormEntity,manufacturer, companySl1DTO, s, sr)
        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/user/user-profile?userName=${loggedInUser.userName}"
       sm.message = "You have Successful Register, Email Has been sent with Entry Number "

       return commonDaoServices.returnValues(result, map, sm)
       // return "Executed"
    }

    @PreAuthorize("hasAuthority('SL_SCHEDULE_VISIT')")
  @PostMapping("/scheduleSiteVisit")
  @ResponseBody
  @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
  fun scheduleSiteVisit(
                         @RequestBody stdLevyScheduleSiteVisitDTO: StdLevyScheduleSiteVisitDTO,
  ): ServerResponse
  {
      val standardLevyFactoryVisitReportEntity= StandardLevyFactoryVisitReportEntity().apply {
          manufacturerEntity= stdLevyScheduleSiteVisitDTO.manufacturerEntity
          scheduledVisitDate= stdLevyScheduleSiteVisitDTO.scheduledVisitDate
          status= 0
          createdBy= commonDaoServices.loggedInUserDetails().userName
          createdOn= Timestamp(System.currentTimeMillis())
          taskId= stdLevyScheduleSiteVisitDTO.taskId
          assigneeId= commonDaoServices.loggedInUserDetails().id
          entryNumber= stdLevyScheduleSiteVisitDTO.entryNumber
          companyName= stdLevyScheduleSiteVisitDTO.companyName
          registrationNumber= stdLevyScheduleSiteVisitDTO.registrationNumber
          kraPin = stdLevyScheduleSiteVisitDTO.kraPin
      }
//             val gson = Gson()
//        KotlinLogging.logger { }.info { "INVOICE CALCULATED" + gson.toJson(standardLevyFactoryVisitReportEntity) }
      return ServerResponse(HttpStatus.OK,"Site Visit Scheduled",standardLevyService.scheduleSiteVisit(standardLevyFactoryVisitReportEntity))

  }

    @PreAuthorize("hasAuthority('MODIFY_COMPANY')")
    @PostMapping("/editCompanyDetails")
    @ResponseBody
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun editCompanyDetails(
        @RequestBody editCompanyDTO: EditCompanyTaskToDTO
    ): ServerResponse
    {
        val companyProfileEditEntity= CompanyProfileEditEntity().apply {
            manufactureId = editCompanyDTO.companyId
            postalAddress = editCompanyDTO.postalAddress
            physicalAddress = editCompanyDTO.physicalAddress
            ownership = editCompanyDTO.ownership
            taskType= editCompanyDTO.taskType
            assignedTo= editCompanyDTO.assignedTo
            userType= editCompanyDTO.userType
            name=editCompanyDTO.companyName
            kraPin=editCompanyDTO.kraPin
            registrationNumber=editCompanyDTO.registrationNumber
            entryNumber=editCompanyDTO.entryNumber
            typeOfManufacture=editCompanyDTO.typeOfManufacture



        }

        return ServerResponse(HttpStatus.OK,"Company Details Edited",standardLevyService.editCompanyDetails(companyProfileEditEntity))

    }

    @PreAuthorize("hasAuthority('MODIFY_COMPANY')")
    @PostMapping("/editCompanyDetailsConfirm")
    @ResponseBody
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun editCompanyDetailsConfirm(
        @RequestBody editCompanyDTO: EditCompanyTaskToDTO
    ): ServerResponse
    {
        val companyProfileEntity= CompanyProfileEntity().apply {
            id = editCompanyDTO.companyId
            postalAddress = editCompanyDTO.postalAddress
            physicalAddress = editCompanyDTO.physicalAddress
            ownership = editCompanyDTO.ownership
            taskId = editCompanyDTO.taskId
            slBpmnProcessInstance = editCompanyDTO.processId
            assignedTo = editCompanyDTO.assignedTo
            accentTo = editCompanyDTO.accentTo


        }

        return ServerResponse(HttpStatus.OK,"Company Details Edited",standardLevyService.editCompanyDetailsConfirm(companyProfileEntity))

    }

    @PreAuthorize("hasAuthority('MODIFY_COMPANY')")
    @PostMapping("/editCompanyDetailsConfirmLvlOne")
    @ResponseBody
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun editCompanyDetailsConfirmLvlOne(
        @RequestBody editCompanyDTO: EditCompanyTaskToDTO
    ): ServerResponse
    {
        val companyProfileEntity= CompanyProfileEntity().apply {
            id = editCompanyDTO.companyId
            postalAddress = editCompanyDTO.postalAddress
            physicalAddress = editCompanyDTO.physicalAddress
            ownership = editCompanyDTO.ownership
            taskId = editCompanyDTO.taskId
            slBpmnProcessInstance = editCompanyDTO.processId
            assignedTo = editCompanyDTO.assignedTo
            accentTo = editCompanyDTO.accentTo


        }

        return ServerResponse(HttpStatus.OK,"Company Details Edited",standardLevyService.editCompanyDetailsConfirmLvlOne(companyProfileEntity))

    }

    @PreAuthorize("hasAuthority('MODIFY_COMPANY')")
    @PostMapping("/editCompanyDetailsConfirmLvlTwo")
    @ResponseBody
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun editCompanyDetailsConfirmLvlTwo(
        @RequestBody editCompanyDTO: EditCompanyTaskToDTO
    ): ServerResponse
    {
        val companyProfileEntity= CompanyProfileEntity().apply {
            id = editCompanyDTO.companyId
            postalAddress = editCompanyDTO.postalAddress
            physicalAddress = editCompanyDTO.physicalAddress
            ownership = editCompanyDTO.ownership
            taskId = editCompanyDTO.taskId
            slBpmnProcessInstance = editCompanyDTO.processId
            assignedTo = editCompanyDTO.assignedTo
            accentTo = editCompanyDTO.accentTo


        }

        return ServerResponse(HttpStatus.OK,"Company Details Edited",standardLevyService.editCompanyDetailsConfirmLvlTwo(companyProfileEntity))

    }


    @PreAuthorize("hasAuthority('SL_ASSIGN_COMPANY')")
    @PostMapping("/assignCompany")
    @ResponseBody
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun assignCompany(
                           @RequestBody assignToDTO: AssignCompanyTaskToDTO
    ): ServerResponse
    {
        val companyProfileEntity= CompanyProfileEntity().apply {
            id = assignToDTO.manufacturerEntity
            assignedTo = assignToDTO.assignedTo
            name = assignToDTO.companyName
            kraPin = assignToDTO.kraPin
            status = assignToDTO.status
            registrationNumber = assignToDTO.registrationNumber
            postalAddress = assignToDTO.postalAddress
            physicalAddress = assignToDTO.physicalAddress
            plotNumber = assignToDTO.plotNumber
            companyEmail = assignToDTO.companyEmail
            companyTelephone = assignToDTO.companyTelephone
            yearlyTurnover = assignToDTO.yearlyTurnover
            businessLineName = assignToDTO.businessLineName
            businessLines = assignToDTO.businessLines
            businessNatureName = assignToDTO.businessNatureName
            businessNatures = assignToDTO.businessNatures
            buildingName = assignToDTO.buildingName
            directorIdNumber = assignToDTO.directorIdNumber
            regionName = assignToDTO.regionName
            region = assignToDTO.region
            countyName = assignToDTO.countyName
            county = assignToDTO.county
            townName = assignToDTO.townName
            town = assignToDTO.town
            branchName = assignToDTO.branchName
            streetName = assignToDTO.streetName
            manufactureStatus = assignToDTO.manufactureStatus
            entryNumber = assignToDTO.entryNumber
            userId= assignToDTO.contactId
            taskType= assignToDTO.taskType
            typeOfManufacture= assignToDTO.typeOfManufacture
        }
//        val gson = Gson()
//        KotlinLogging.logger { }.info { "Assigned Variables" + gson.toJson(assignToDTO) }

        return ServerResponse(HttpStatus.OK,"Company Task Assigned",standardLevyService.assignCompany(companyProfileEntity))

    }

    @PreAuthorize("hasAuthority('SL_MANUFACTURE_VIEW')")
    @GetMapping("/getUserTasks")
    fun getUserTasks():List<TaskDetailsBody>
    {
        return standardLevyService.getUserTasks()
    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @GetMapping("/viewFeedBack")
    fun viewFeedBack():List<TaskDetailsBody>
    {
        return standardLevyService.viewFeedBack()
    }

    @PreAuthorize("hasAuthority('SL_SITE_VISIT_REPORT_CREATE')")
    @PostMapping("/reportOnSiteVisit")
    @ResponseBody
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun reportOnSiteVisit(@RequestBody reportOnSiteVisitDTO: ReportOnSiteVisitDTO): ServerResponse
    {
        val standardLevyFactoryVisitReportEntity= StandardLevyFactoryVisitReportEntity().apply {
            visitDate=reportOnSiteVisitDTO.visitDate
            purpose = reportOnSiteVisitDTO.purpose
            personMet = reportOnSiteVisitDTO.personMet
            actionTaken = reportOnSiteVisitDTO.actionTaken
            id= reportOnSiteVisitDTO.visitID
            assigneeId=reportOnSiteVisitDTO.assigneeId
            taskId= reportOnSiteVisitDTO.taskId
            manufacturerEntity= reportOnSiteVisitDTO.manufacturerEntity
            makeRemarks = reportOnSiteVisitDTO.makeRemarks
            userType=reportOnSiteVisitDTO.userType
        }

        return ServerResponse(HttpStatus.OK,"Uploaded Report",standardLevyService.reportOnSiteVisit(standardLevyFactoryVisitReportEntity))
    }

    @PreAuthorize("hasAuthority('SL_SITE_VISIT_REPORT_CREATE')")
    @PostMapping("/reportOnSiteVisitTest")
    @ResponseBody
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun reportOnSiteVisitTest(@RequestBody reportOnSiteVisitDTO: ReportOnSiteVisitDTO): ServerResponse
    {
        val standardLevyFactoryVisitReportEntity= StandardLevyFactoryVisitReportEntity().apply {
            visitDate=reportOnSiteVisitDTO.visitDate
            purpose = reportOnSiteVisitDTO.purpose
            personMet = reportOnSiteVisitDTO.personMet
            actionTaken = reportOnSiteVisitDTO.actionTaken
            id= reportOnSiteVisitDTO.visitID
            assigneeId=reportOnSiteVisitDTO.assigneeId
            taskId= reportOnSiteVisitDTO.taskId
            manufacturerEntity= reportOnSiteVisitDTO.manufacturerEntity
            makeRemarks = reportOnSiteVisitDTO.makeRemarks
            userType=reportOnSiteVisitDTO.userType
        }
//        val gson = Gson()
//        KotlinLogging.logger { }.info { "INT TYPE" + gson.toJson(reportOnSiteVisitDTO) }
        return ServerResponse(HttpStatus.OK,"Uploaded Report",standardLevyService.reportOnSiteVisitTest(standardLevyFactoryVisitReportEntity))

    }



    @PostMapping("/site-report-upload")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadFiles(
        @RequestParam("reportFileID") reportFileID: Long,
        @RequestParam("docFile") docFile: List<MultipartFile>,
        model: Model
    ): CommonDaoServices.MessageSuccessFailDTO {

        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val siteVisit = standardLevyFactoryVisitReportRepo.findByIdOrNull(reportFileID)?: throw Exception("VISIT ID DOES NOT EXIST")

        docFile.forEach { u ->
            val upload = SlVisitUploadsEntity()
            with(upload) {
                visitId = siteVisit.id

            }
            standardLevyService.uploadSiteReport(
                upload,
                u,
                "UPLOADS",
                loggedInUser,
                "Report"
            )
        }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.message = "Document Uploaded successfully"

        return sm
    }

    @GetMapping("/getSiteReport")
    fun getSiteReport():List<TaskDetailsBody>
    {
        return standardLevyService.getSiteReport()
    }

    @GetMapping("/getVisitDocumentList")
    fun getVisitDocumentList(
        response: HttpServletResponse,
        @RequestParam("visitId") visitId: Long
    ): List<SiteVisitListHolder> {
        return standardLevyService.getVisitDocumentList(visitId)
    }



    //View Site Visit Report Document
    @GetMapping("/view/siteVisitReport")
    fun viewPDFile(
        response: HttpServletResponse,
        @RequestParam("visitID") visitID: Long
    ) {

        val fileUploaded = standardLevyService.findUploadedReportFileBYId(visitID)
        val fileDoc = commonDaoServices.mapClass(fileUploaded)
        response.contentType = "application/pdf"
//                    response.setHeader("Content-Length", pdfReportStream.size().toString())
        response.addHeader("Content-Disposition", "inline; filename=${fileDoc.name}")
        response.outputStream
            .let { responseOutputStream ->
                responseOutputStream.write(fileDoc.document?.let { makeAnyNotBeNull(it) } as ByteArray)
                responseOutputStream.close()
            }

        KotlinLogging.logger { }.info("VIEW FILE SUCCESSFUL")

    }

    @PreAuthorize("hasAuthority('SL_SITE_VISIT_REPORT_FEEDBACK_CREATE')")
    @PostMapping("/siteVisitReportFeedback")
    @ResponseBody
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun siteVisitReportFeedback(@RequestBody  reportOnSiteVisitDTO: ReportOnSiteVisitDTO


    ): ServerResponse
    {
        val standardLevyFactoryVisitReportEntity= StandardLevyFactoryVisitReportEntity().apply {
            officersFeedback = reportOnSiteVisitDTO.officersFeedback
            id = reportOnSiteVisitDTO.visitID
            taskId= reportOnSiteVisitDTO.taskId
            assigneeId = reportOnSiteVisitDTO.assigneeId
            manufacturerEntity= reportOnSiteVisitDTO.manufacturerEntity
            userType= reportOnSiteVisitDTO.userType

        }
        return ServerResponse(HttpStatus.OK,"Uploaded Feedback",standardLevyService.siteVisitReportFeedback(standardLevyFactoryVisitReportEntity))

    }

//    @PostMapping("/decisionOnSiteReportx")
//    fun decisionOnSiteReportx(@RequestBody siteVisitReportDecision: SiteVisitReportDecision) : List<TaskDetailsBody>
//    {
//        val standardLevyFactoryVisitReportEntity= StandardLevyFactoryVisitReportEntity().apply {
//            assistantManagerRemarks = siteVisitReportDecision.comments
//            assigneeId = siteVisitReportDecision.assigneeId
//            taskId= siteVisitReportDecision.taskId
//            id= siteVisitReportDecision.visitID
//            manufacturerEntity=siteVisitReportDecision.manufacturerEntity
//
//
//        }
//
//        return standardLevyService.decisionOnSiteReport(standardLevyFactoryVisitReportEntity)
//    }

    @PreAuthorize("hasAuthority('SL_MANUFACTURE_VIEW')")
    @PostMapping("/decisionOnSiteReport")
    @ResponseBody
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun decisionOnSiteReport(@RequestBody siteVisitReportDecisionDTO: SiteVisitReportDecisionDTO): ServerResponse
    {
        val standardLevyFactoryVisitReportEntity= StandardLevyFactoryVisitReportEntity().apply {
            taskId=siteVisitReportDecisionDTO.taskId
            accentTo = siteVisitReportDecisionDTO.accentTo
            id= siteVisitReportDecisionDTO.visitID
            assigneeId=siteVisitReportDecisionDTO.assigneeId
            manufacturerEntity= siteVisitReportDecisionDTO.manufacturerEntity
            assistantManagerRemarks =siteVisitReportDecisionDTO.comments
            approvalStatus =siteVisitReportDecisionDTO.approvalStatus
            approvalStatusId =siteVisitReportDecisionDTO.approvalStatusId

        }
        val standardLevySiteVisitRemarks= StandardLevySiteVisitRemarks().apply {
            siteVisitId=siteVisitReportDecisionDTO.visitID
            remarks=siteVisitReportDecisionDTO.comments
            status=siteVisitReportDecisionDTO.status
            role=siteVisitReportDecisionDTO.role

        }
//        val gson = Gson()
//        KotlinLogging.logger { }.info { "Report Decision" + gson.toJson(siteVisitReportDecisionDTO) }
        return ServerResponse(HttpStatus.OK,"Decision Saved",standardLevyService.decisionOnSiteReport(standardLevyFactoryVisitReportEntity,standardLevySiteVisitRemarks))

    }

    @GetMapping("/getSiteReportLevelTwo")
    fun getSiteReportLevelTwo():List<TaskDetailsBody>
    {
        return standardLevyService.getSiteReportLevelTwo()
    }

//    @PostMapping("/decisionOnSiteReportLevelTwox")
//    fun decisionOnSiteReportLevelTwox(@RequestBody siteVisitReportDecision: SiteVisitReportDecision) : List<TaskDetails>
//    {
//        return standardLevyService.decisionOnSiteReportLevelTwo(siteVisitReportDecision)
//    }

    @PreAuthorize("hasAuthority('SL_MANUFACTURE_VIEW')")
    @PostMapping("/decisionOnSiteReportLevelTwo")
    @ResponseBody
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun decisionOnSiteReportLevelTwo(@RequestBody siteVisitReportDecisionDTO: SiteVisitReportDecisionDTO): ServerResponse
    {
        val standardLevyFactoryVisitReportEntity= StandardLevyFactoryVisitReportEntity().apply {
            taskId=siteVisitReportDecisionDTO.taskId
            accentTo = siteVisitReportDecisionDTO.accentTo
            id= siteVisitReportDecisionDTO.visitID
            manufacturerEntity= siteVisitReportDecisionDTO.manufacturerEntity
            cheifManagerRemarks= siteVisitReportDecisionDTO.comments
            assigneeId = siteVisitReportDecisionDTO.assigneeId
            userType= siteVisitReportDecisionDTO.userType

        }
        val standardLevySiteVisitRemarks= StandardLevySiteVisitRemarks().apply {
            siteVisitId=siteVisitReportDecisionDTO.visitID
            remarks=siteVisitReportDecisionDTO.comments
            status=siteVisitReportDecisionDTO.status
            role=siteVisitReportDecisionDTO.role

        }
        return ServerResponse(HttpStatus.OK,"Decision Saved",standardLevyService.decisionOnSiteReportLevelTwo(standardLevyFactoryVisitReportEntity,standardLevySiteVisitRemarks))

    }



    @GetMapping("/getSiteFeedback")
    fun getSiteFeedback():List<TaskDetailsBody>
    {
        return standardLevyService.getSiteFeedback()
    }

    //Get List of Manufactures
    @PreAuthorize("hasAuthority('SL_MANUFACTURE_VIEW')")
    @GetMapping("/getManufacturerList")
    @ResponseBody
    fun getManufacturerList(): MutableList<ManufactureListHolder>
    {
        return standardLevyService.getManufacturerList()
    }

    //Get List of Manufactures Complete Tasks
    @PreAuthorize("hasAuthority('SL_MANUFACTURE_VIEW')")
    @GetMapping("/getMnCompleteTask")
    @ResponseBody
    fun getMnCompleteTask(): MutableList<CompanyProfileEntity>
    {

        commonDaoServices.loggedInUserDetails().id?.let { id ->
            return standardLevyService.getMnCompleteTask(id)

        }
            ?:return mutableListOf()


    }

    //Get List of Manufactures
    @PreAuthorize("hasAuthority('SL_MANUFACTURE_VIEW')")
    @GetMapping("/getMnPendingTask")
    @ResponseBody
    fun getMnPendingTask(): MutableList<CompanyProfileEntity>
    {
        commonDaoServices.loggedInUserDetails().id?.let { id ->
            return standardLevyService.getMnPendingTask(id)

        }
            ?:return mutableListOf()

    }

    @GetMapping("/getSlUsers")
    @ResponseBody
    fun getSlUsers(): List<UserDetailHolder> {
        return standardLevyService.getSlUsers()
    }

    @GetMapping("/getPlList")
    @ResponseBody
    fun getPlList(): List<UserDetailHolder> {
        return standardLevyService.getPlList()
    }

    @GetMapping("/getSlLvTwoList")
    @ResponseBody
    fun getSlLvTwoList(): List<UserDetailHolder> {
        return standardLevyService.getSlLvTwoList()
    }

    @GetMapping("/getSlLoggedIn")
    @ResponseBody
    fun getSlLoggedIn(): UserTypeHolder
    {

            return standardLevyService.getSlLoggedIn()

    }

    @GetMapping("/getRoleByUserId")
    @ResponseBody
    fun getRoleByUserId(): List<UserRoleHolder>?
    {

        return standardLevyService.getRoles()

    }

    @GetMapping("/getManufacturerStatus")
    @ResponseBody
    fun getManufacturerStatus(): BusinessTypeHolder {
        return standardLevyService.getManufacturerStatus()
    }

    @GetMapping("/getVerificationStatus")
    @ResponseBody
    fun getVerificationStatus(): Int {
        return standardLevyService.getVerificationStatus()
    }

    @PostMapping("/sendEmailVerificationToken")
    @ResponseBody
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun sendEmailVerificationToken(
        @RequestBody sendEmailDto: SendEmailDto
    ): ServerResponse {
        val emailVerificationTokenEntity= EmailVerificationTokenEntity().apply {
           email=sendEmailDto.email
           createdBy= sendEmailDto.userId.toString()
        }
        return ServerResponse(HttpStatus.OK,"Email Sent",standardLevyService.sendEmailVerificationToken(emailVerificationTokenEntity))

    }


    @PostMapping("/confirmEmailAddress")
    @ResponseBody
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun confirmEmailAddress(
        @RequestBody verifyEmailDto: VerifyEmailDto
    ): ServerResponse {
        val emailVerificationTokenEntity= EmailVerificationTokenEntity().apply {
            token=verifyEmailDto.verificationToken
        }
        val usersEntity= UsersEntity().apply {
            id=verifyEmailDto.userId
        }
        return ServerResponse(HttpStatus.OK,"Email Verified",standardLevyService.confirmEmailAddress(usersEntity,emailVerificationTokenEntity))

    }






    @GetMapping("/getCompanyEditedDetails")
    fun getCompanyEditedDetails(
        response: HttpServletResponse,
        @RequestParam("manufactureId") manufactureId: Long
    ): CompanyProfileEditEntity {
        return standardLevyService.getCompanyEditedDetails(manufactureId)
    }




    @GetMapping("/getSiteVisitRemarks")
    fun getSiteVisitRemarks(
        response: HttpServletResponse,
        @RequestParam("siteVisitId") siteVisitId: Long
    ): List<StandardLevySiteVisitRemarks> {
        return standardLevyService.getSiteVisitRemarks(siteVisitId)
    }

    @GetMapping("/getCompanyProcessId")
    fun getCompanyProcessId(
        response: HttpServletResponse,
        @RequestParam("manufactureId") manufactureId: Long
    ): CompanyProfileEditEntity {
        return standardLevyService.getCompanyProcessId(manufactureId)
    }


    @PreAuthorize("hasAuthority('SL_MANUFACTURE_VIEW')")
    @GetMapping("/getCompleteTasks")
    @ResponseBody
    fun getCompleteTasks(): List<CompleteTasksDetailHolder>
    {

            return standardLevyService.getCompleteTasks()


    }

    @GetMapping("/getApproveLevelOne")
    @ResponseBody
    fun getApproveLevelOne(): List<UserDetailHolder> {
        return standardLevyService.getApproveLevelOne()
    }

    @GetMapping("/getApproveLevelTwo")
    @ResponseBody
    fun getApproveLevelTwo(): List<UserDetailHolder> {
        return standardLevyService.getApproveLevelTwo()
    }

    @GetMapping("/getApproveLevelThree")
    @ResponseBody
    fun getApproveLevelThree(): List<UserDetailHolder> {
        return standardLevyService.getApproveLevelThree()
    }

    @GetMapping("/getAssignLevelOne")
    @ResponseBody
    fun getAssignLevelOne(): List<UserDetailHolder> {
        return standardLevyService.getAssignLevelOne()
    }

    @GetMapping("/getAssignLevelTwo")
    @ResponseBody
    fun getAssignLevelTwo(): List<UserDetailHolder> {
        return standardLevyService.getAssignLevelTwo()
    }

    @GetMapping("/getAssignLevelThree")
    @ResponseBody
    fun getAssignLevelThree(): List<UserDetailHolder> {
        return standardLevyService.getAssignLevelThree()
    }

    @GetMapping("/getSLNotificationStatus")
    @ResponseBody
    fun getSLNotificationStatus(): Boolean
    {

        return standardLevyService.getSLNotificationStatus()

    }

    @GetMapping("/getUserEmail")
    @ResponseBody
    fun getUserEmail(): String {
        return commonDaoServices.getUserEmail(3082)
    }

    @GetMapping("/getSlNotificationFormDetails")
    fun getSlNotificationFormDetails(
        response: HttpServletResponse,
        @RequestParam("manufactureId") manufactureId: Long
    ): NotificationFormDetailsHolder {
        return standardLevyService.getSlNotificationFormDetails(manufactureId)
    }

    @GetMapping("/getNotificationFormDetails")
    @ResponseBody
    fun getNotificationFormDetails(): NotificationFormDetailsHolder {
        return standardLevyService.getNotificationFormDetails()
    }

    @GetMapping("/getCompanyDirectors")
    @ResponseBody
    fun getCompanyDirectors():List<DirectorListHolder>? {
        return standardLevyService.getCompanyDirectors()
    }

    @GetMapping("/getBranchName")
    @ResponseBody
    fun getBranchName(): BranchNameHolder {
        return standardLevyService.getBranchName()
    }


   // @PreAuthorize("hasAuthority('MODIFY_COMPANY')")
    @PostMapping("/suspendCompanyOperations")
    @ResponseBody
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun suspendCompanyOperations(
        @RequestBody suspendCompanyDto: SuspendCompanyDto
    ): ServerResponse
    {
        val standardLevyOperationsSuspension= StandardLevyOperationsSuspension().apply {
            companyId = suspendCompanyDto.id
            reason = suspendCompanyDto.reason
            dateOfSuspension = suspendCompanyDto.dateOfSuspension.toString()
        }

        return ServerResponse(HttpStatus.OK,"Details Submitted for Verification",standardLevyService.suspendCompanyOperations(standardLevyOperationsSuspension))

    }

    @PostMapping("/confirmCompanySuspension")
    @ResponseBody
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun confirmCompanySuspension(
        @RequestBody companySuspendDto: CompanySuspendDto
    ): ServerResponse
    {
        val companyProfileEntity= CompanyProfileEntity().apply {
            id = companySuspendDto.companyId
        }
        val standardLevyOperationsSuspension= StandardLevyOperationsSuspension().apply {
            id = companySuspendDto.id
        }

        return ServerResponse(HttpStatus.OK,"Company Suspension Approved",standardLevyService.confirmCompanySuspension(companyProfileEntity,standardLevyOperationsSuspension))

    }

    @PostMapping("/rejectCompanySuspension")
    @ResponseBody
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun rejectCompanySuspension(
        @RequestBody companySuspendDto: CompanySuspendDto
    ): ServerResponse
    {

        val companyProfileEntity= CompanyProfileEntity().apply {
            id = companySuspendDto.companyId
        }
        val standardLevyOperationsSuspension= StandardLevyOperationsSuspension().apply {
            id = companySuspendDto.id
        }

        return ServerResponse(HttpStatus.OK,"Company Suspension Rejected",standardLevyService.rejectCompanySuspension(standardLevyOperationsSuspension,companyProfileEntity))

    }

    @GetMapping("/getCompanySuspensionRequest")
    @ResponseBody
    fun getCompanySuspensionRequest(): MutableList<CompanySuspensionList>
    {
        return standardLevyService.getCompanySuspensionRequest()
    }





    @PostMapping("/closeCompanyOperations")
    @ResponseBody
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun closeCompanyOperations(
        @RequestBody closeCompanyDto: CloseCompanyDto
    ): ServerResponse
    {
        val standardLevyOperationsClosure= StandardLevyOperationsClosure().apply {
            companyId = closeCompanyDto.id
            reason = closeCompanyDto.reason
            dateOfClosure = closeCompanyDto.dateOfClosure.toString()
        }

        return ServerResponse(HttpStatus.OK,"Details Submitted for Verification",standardLevyService.closeCompanyOperations(standardLevyOperationsClosure))

    }

    @PostMapping("/uploadWindingUpReport")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadWindingUpReport(
        @RequestParam("operationClosureId") operationClosureId: Long,
        @RequestParam("docFile") docFile: List<MultipartFile>,
        model: Model
    ): CommonDaoServices.MessageSuccessFailDTO {

        val loggedInUser = commonDaoServices.loggedInUserDetailsEmail()
        //val windingUpReport = standardLevyOperationsClosureRepository.findByIdOrNull(operationClosureId)?: throw Exception("Record Does Not Exist")

        docFile.forEach { u ->
            val upload = SlWindingUpReportUploadsEntity()
            upload.closureID = operationClosureId
            standardLevyService.uploadWindingUpReport(
                upload,
                u,
                "UPLOADS",
                loggedInUser,
                "Report"
            )
        }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.message = "Document Uploaded successfully"

        return sm
    }

    @PostMapping("/confirmCompanyClosure")
    @ResponseBody
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun confirmCompanyClosure(
        @RequestBody companyCloseDto: CompanyCloseDto
    ): ServerResponse
    {
        val companyProfileEntity= CompanyProfileEntity().apply {
            id = companyCloseDto.companyId
        }
        val standardLevyOperationsSuspension= StandardLevyOperationsSuspension().apply {
            id = companyCloseDto.id
        }

        return ServerResponse(HttpStatus.OK,"Company Closure Rejected",standardLevyService.confirmCompanyClosure(companyProfileEntity,standardLevyOperationsSuspension))

    }

    @PostMapping("/rejectCompanyClosure")
    @ResponseBody
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun rejectCompanyClosure(
        @RequestBody companySuspendDto: CompanySuspendDto
    ): ServerResponse
    {

        val companyProfileEntity= CompanyProfileEntity().apply {
            id = companySuspendDto.companyId
        }
        val standardLevyOperationsSuspension= StandardLevyOperationsSuspension().apply {
            id = companySuspendDto.id
        }

        return ServerResponse(HttpStatus.OK,"Company Closure Rejected",standardLevyService.rejectCompanyClosure(standardLevyOperationsSuspension,companyProfileEntity))

    }

    @GetMapping("/getWindingReportDocumentList")
    fun getWindingReportDocumentList(
        response: HttpServletResponse,
        @RequestParam("closureID") closureID: Long
    ): List<WindingUpReportListHolder> {
        return standardLevyService.getWindingReportDocumentList(closureID)
    }

    //View Winding Up Report Document
    @GetMapping("/view/windingUpReport")
    fun viewWindingUpReport(
        response: HttpServletResponse,
        @RequestParam("closureID") closureID: Long
    ) {

        val fileUploaded = standardLevyService.findWindingReportFileBYId(closureID)
        val fileDoc = commonDaoServices.mapClass(fileUploaded)
        response.contentType = "application/pdf"
//                    response.setHeader("Content-Length", pdfReportStream.size().toString())
        response.addHeader("Content-Disposition", "inline; filename=${fileDoc.name}")
        response.outputStream
            .let { responseOutputStream ->
                responseOutputStream.write(fileDoc.document?.let { makeAnyNotBeNull(it) } as ByteArray)
                responseOutputStream.close()
            }

        KotlinLogging.logger { }.info("VIEW FILE SUCCESSFUL")

    }



    @GetMapping("/getCompanyClosureRequest")
    @ResponseBody
    fun getCompanyClosureRequest(): MutableList<CompanyClosureList>
    {
        return standardLevyService.getCompanyClosureRequest()
    }




    @PostMapping("/anonymous/standard/close")
    fun clos(@RequestBody responseMessage: ResponseMessage) {
        return standardLevyService.closeProcess(responseMessage.message)
    }

    //Delete A Task
    @PostMapping("/anonymous/standard/closetask")
    fun closeTask(@RequestBody responseMessage: ResponseMessage) {
        return standardLevyService.closeTask(responseMessage.message)
    }

    @GetMapping("/sendLevyPaymentReminders")
    @ResponseBody
    fun sendLevyPaymentReminders(): String
    {
        return standardLevyService.sendLevyPaymentReminders()
    }

    @GetMapping("/getLevyPayments")
    @ResponseBody
    fun getLevyPayments(): MutableList<LevyPayments>
    {
        return standardLevyService.getLevyPayments()
    }

    @GetMapping("/getManufacturesLevyPayments")
    @ResponseBody
    fun getManufacturesLevyPayments(): MutableList<LevyPayments>
    {
        return standardLevyService.getManufacturesLevyPayments()
    }

    @GetMapping("/getManufacturesLevyPaymentsList")
    fun getManufacturesLevyPaymentsList(
        response: HttpServletResponse,
        @RequestParam("companyId") companyId: Long
    ): MutableList<LevyPayments> {
        return standardLevyService.getManufacturesLevyPaymentsList(companyId)
    }

    @GetMapping("/getLevyPenalty")
    @ResponseBody
    fun getLevyPenalty(): MutableList<LevyPenalty>
    {
        return standardLevyService.getLevyPenalty()
    }

    @GetMapping("/getManufacturesLevyPenalty")
    @ResponseBody
    fun getManufacturesLevyPenalty(): MutableList<LevyPenalty>
    {
        return standardLevyService.getManufacturesLevyPenalty()
    }

    @GetMapping("/getManufacturesLevyPenaltyList")
    fun getManufacturesLevyPenaltyList(
        response: HttpServletResponse,
        @RequestParam("companyId") companyId: Long
    ): MutableList<LevyPenalty> {
        return standardLevyService.getManufacturesLevyPenaltyList(companyId)
    }


}
