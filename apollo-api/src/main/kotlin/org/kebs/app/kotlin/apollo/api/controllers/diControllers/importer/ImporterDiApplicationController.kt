package org.kebs.app.kotlin.apollo.api.controllers.diControllers.importer

import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.ImporterDaoServices
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.importer.*
import org.kebs.app.kotlin.apollo.store.repo.IServiceRequestsRepository
import org.kebs.app.kotlin.apollo.store.repo.importer.ICsApprovalApplicationsUploadsRepository
import org.kebs.app.kotlin.apollo.store.repo.importer.ITemporaryImportApplicationsUploadsRepository
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid


@Controller
@RequestMapping("/api/importer/")
@SessionAttributes("userRole", "schedule", "userNumber", "CoCs", "destinationFees", "demandNote", "actionType", "docView", "CoCsBlacklist", "myCoc", "coc", "itemCoCs", "status", "item", "itemCerts", "itemIdf", "certBack", "officers", "inspectionType", "remarks", "filledChecklist")
class ImporterDiApplicationController(
    private val applicationMapProperties: ApplicationMapProperties,
    private val daoServices: ImporterDaoServices,
    private val serviceRequestsRepository: IServiceRequestsRepository,
    private val iCsApprovalApplicationsUploadsRepo: ICsApprovalApplicationsUploadsRepository,
    private val iTemporaryImportApplicationsUploadsRepo: ITemporaryImportApplicationsUploadsRepository,

    private val commonDaoServices: CommonDaoServices
) {


    final var appId = applicationMapProperties.mapImporterDetails

    final var redirectRFCDetailPage = "redirect:/api/importer/rfc-detail?rfcID"
    final var redirectErrorPage = "redirect:api/error/occurred"
    private final var redirectDiApplicationListPage = "redirect:/api/importer/di-application-list?diApplicationTypeUuid"

    @GetMapping("view/attached")
    fun downloadFileDocument(
            response: HttpServletResponse,
            @RequestParam("diApplicationTypeUuid") diApplicationTypeUuid: String,
            @RequestParam("fileID") fileID: Long
    ) {
//        try {
            val diApplicationTypeEntity = daoServices.findDiApplicationTypeWithUuid(diApplicationTypeUuid)
            val fileUploaded: Any
            val mappedFileClass: Any
            when (diApplicationTypeEntity.uuid) {
                applicationMapProperties.mapDIApplicationCSApprovalTypeUuid -> {
                    fileUploaded = daoServices.findCSApprovalUploadWithID(fileID)
                    mappedFileClass = commonDaoServices.mapClass(fileUploaded)
                    commonDaoServices.downloadFile(response, mappedFileClass)
                }
                applicationMapProperties.mapDIApplicationTemporaryImportTypeUuid -> {
                    fileUploaded = daoServices.findTemporaryImportsUploadWithID(fileID)
                    mappedFileClass = commonDaoServices.mapClass(fileUploaded)
                    commonDaoServices.downloadFile(response, mappedFileClass)
                }
            }
//        } catch (e: Exception) {
//            createUserAlert(req, e)
//        }
    }

    @PreAuthorize("hasAuthority('IMPORTER')")
    @PostMapping("di-application/cs_approval/new/save")
    fun applicationForDiApplicationCsApproval(
            model: Model,
            @ModelAttribute("csApprovalEntity") @Valid csApprovalEntity: CsApprovalApplicationsEntity,
            @ModelAttribute("csApprovalUploadEntity") csApprovalUploadEntity: CsApprovalApplicationsUploadsEntity,
            @RequestParam("diApplicationTypeUuid") diApplicationTypeUuid: String,
            @RequestParam("bill_of_landing_file") bill_of_landing_file: MultipartFile,
            @RequestParam("custom_entry_file") custom_entry_file: MultipartFile,
            @RequestParam("IDF_file") IDF_file: MultipartFile,
            @RequestParam("commercial_invoice_file") commercial_invoice_file: MultipartFile,
            results: BindingResult,
            redirectAttributes: RedirectAttributes
    ): String {

//        return try {
            val map = commonDaoServices.serviceMapDetails(appId)
            var sr = commonDaoServices.createServiceRequest(map)
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val diApplicationTypeEntity = daoServices.findDiApplicationTypeWithUuid(diApplicationTypeUuid)
            when (diApplicationTypeEntity.uuid) {
                applicationMapProperties.mapDIApplicationCSApprovalTypeUuid -> {
                    daoServices.csApprovalSave(csApprovalEntity, loggedInUser, map, diApplicationTypeEntity)
                            .let { savedCsApprovalEntity ->
                                //Create payload for Logging the transactions done before sending an Email
                                var payload = "CS approval[id= ${savedCsApprovalEntity.id}]"
                                // Create an ArrayList for lopping through the files to be saved
                                val fileList = arrayListOf(bill_of_landing_file, custom_entry_file, IDF_file, commercial_invoice_file)
                                val fileListName = arrayListOf("Bill of landing file", "custom entry file", "IDF file", "commercial invoice file")
                                for (file in fileList.indices) {
                                    //                                                                Executors.newSingleThreadScheduledExecutor().schedule({
                                    val tempDocFiles = daoServices.saveCsApprovalUploads(fileList[file], fileListName[file], loggedInUser, map, savedCsApprovalEntity)
                                    payload = "${payload}: File Saved [id=${tempDocFiles.id}]"
                                }
                                //Generate A Log Service Request to store the transactions done after all payloads created
                                sr = commonDaoServices.mapServiceRequestForSuccess(map, payload, loggedInUser)
                                //Send user and email stating the application has been submitted successful

                                commonDaoServices.sendEmailWithUserEntity(loggedInUser, daoServices.diApplicationSubmittedUuid, savedCsApprovalEntity, map, sr)
                                // Todo send email to management inspection officer clarification on who to choose needed
                                savedCsApprovalEntity.entryPointId?.let { entryId -> commonDaoServices.findUserProfileWithSectionIdAndDesignationId(entryId, commonDaoServices.findDesignationByID(daoServices.diDesignationManagerInspectionId.toLong()), map.activeStatus).userId?.let { commonDaoServices.sendEmailWithUserEntity(it, daoServices.diApplicationAwaitingApprovalUuid, savedCsApprovalEntity, map, sr) } }
                                redirectAttributes.addFlashAttribute("message", "You have successfully Submitted Your application")
                                return "$redirectDiApplicationListPage=${diApplicationTypeUuid}"
                            }
                }
                else -> throw ExpectedDataNotFound("No marching di Application Type with ID  = ${diApplicationTypeEntity.id}, used for saving Cs Approval, does not Exist")
            }

//        } catch (e: Exception) {
////            KotlinLogging.logger { }.error(e.message)
////            KotlinLogging.logger { }.debug(e.message, e)
//////            KotlinLogging.logger { }.trace(e.message, e)
////            sr.status = sr.serviceMapsId?.exceptionStatus
////            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
////            sr.responseMessage = e.message
////            sr = serviceRequestsRepository.save(sr)
//        }

    }

    @PreAuthorize("hasAuthority('DI_MANAGER_INSPECTION_READ')")
    @PostMapping("di-application/cs_approval/update/save")
    fun updateForDiApplicationCsApproval(
            model: Model,
            @ModelAttribute("csApprovalEntity") @Valid csApprovalEntity: CsApprovalApplicationsEntity,
            @RequestParam("diApplicationUuid") diApplicationUuid: String,
            results: BindingResult,
            redirectAttributes: RedirectAttributes
    ): String {
        commonDaoServices.serviceMapDetails(appId)
                .let { map ->
                    commonDaoServices.loggedInUserDetails()
                            .let { loggedInUser ->
                                daoServices.findCSApprovalWithUuid(diApplicationUuid)
                                        .let { diApplicationEntity ->
                                            val valueID: Long = diApplicationEntity.id.let { commonDaoServices.makeAnyNotBeNull(it) } as Long
                                            csApprovalEntity.id = valueID

                                            // Before update of Details in DB
                                            if (csApprovalEntity.confirmAssignedUserId != null) {
                                                with(csApprovalEntity) {
                                                    assignedUser = confirmAssignedUserId?.let { commonDaoServices.findUserByID(it) }
                                                }
                                            }
                                            //updating of Details in DB
                                            val updatedCSApproval = daoServices.updateCsApproval(commonDaoServices.updateDetails(csApprovalEntity, diApplicationEntity) as CsApprovalApplicationsEntity, loggedInUser, map)

                                            // After update of Details in DB sending mail or any other actions depending with the transaction status
                                            when {
                                                csApprovalEntity.approvalStatus == map.activeStatus -> {
                                                    val payload = "CS APPROVAL APPROVED [approvalStatus= ${csApprovalEntity.approvalStatus}, approvalRemarks= ${csApprovalEntity.approvalRemarks}]"
                                                    val sr = commonDaoServices.mapServiceRequestForSuccess(map, payload, loggedInUser)
                                                    updatedCSApproval.importerId?.let { commonDaoServices.sendEmailWithUserEntity(it, daoServices.diApplicationApprovedUuid, updatedCSApproval, map, sr) }
                                                }
                                                csApprovalEntity.rejectedStatus == map.activeStatus -> {
                                                    val payload = "CS APPROVAL REJECTED [rejectedStatus= ${csApprovalEntity.rejectedStatus}, rejectedRemarks= ${csApprovalEntity.rejectedRemarks}]"
                                                    val sr = commonDaoServices.mapServiceRequestForSuccess(map, payload, loggedInUser)
                                                    updatedCSApproval.importerId?.let { commonDaoServices.sendEmailWithUserEntity(it, daoServices.diApplicationRejectedUuid, updatedCSApproval, map, sr) }
                                                }
                                                csApprovalEntity.assignedUserStatus == map.activeStatus -> {
                                                    val payload = "CS APPROVAL ASSIGNED USER [assignedUserStatus= ${csApprovalEntity.assignedUserStatus}, assignedUserRemarks= ${csApprovalEntity.assignedUserRemarks}]"
                                                    val sr = commonDaoServices.mapServiceRequestForSuccess(map, payload, loggedInUser)
                                                    updatedCSApproval.assignedUser?.let { commonDaoServices.sendEmailWithUserEntity(it, daoServices.diApplicationAssignedUuid, updatedCSApproval, map, sr) }
                                                }
                                            }

                                            return "$redirectDiApplicationListPage=${diApplicationEntity.applicationType?.uuid}"

                                        }
                            }
                }
    }

    @PreAuthorize("hasAuthority('IMPORTER')")
    @PostMapping("di-application/temporary-imports/new/save")
    fun applicationForDiApplicationTemporaryImports(
            model: Model,
            @ModelAttribute("temporaryImportEntity") @Valid temporaryImportEntity: TemporaryImportApplicationsEntity,
            @ModelAttribute("temporaryImportsUploadEntity") temporaryImportsUploadEntity: TemporaryImportApplicationsUploadsEntity,
            @RequestParam("diApplicationTypeUuid") diApplicationTypeUuid: String,
            @RequestParam("bill_of_landing_file") bill_of_landing_file: MultipartFile,
            @RequestParam("custom_entry_file") custom_entry_file: MultipartFile,
            @RequestParam("IDF_file") IDF_file: MultipartFile,
            @RequestParam("commercial_invoice_file") commercial_invoice_file: MultipartFile,
            @RequestParam("kra_bond_file") kra_bond_file: MultipartFile,
            @RequestParam("export_certificate_file") export_certificate_file: MultipartFile,
            results: BindingResult,
            redirectAttributes: RedirectAttributes
    ): String {

        commonDaoServices.serviceMapDetails(appId)
                .let { map ->
                    commonDaoServices.loggedInUserDetails()
                            .let { loggedInUser ->
                                daoServices.findDiApplicationTypeWithUuid(diApplicationTypeUuid)
                                        .let { diApplicationTypeEntity ->
                                            when (diApplicationTypeEntity.uuid) {
                                                applicationMapProperties.mapDIApplicationTemporaryImportTypeUuid -> {
                                                    daoServices.temporaryImportSave(temporaryImportEntity, loggedInUser, map, diApplicationTypeEntity)
                                                            .let { savedTemporaryImportEntity ->
                                                                //Create payload for Logging the transactions done before sending an Email
                                                                var payload = "Temporary Import[id= ${savedTemporaryImportEntity.id}]"
                                                                // Create an ArrayList for lopping through the files to be saved
                                                                val fileList = arrayListOf(bill_of_landing_file, custom_entry_file, IDF_file, commercial_invoice_file, kra_bond_file, export_certificate_file)
                                                                val fileListName = arrayListOf("Bill of landing file", "custom entry file", "IDF file", "commercial invoice file", "KRA bond file", "export certificate file")
                                                                for (file in fileList.indices) {
                                                                    val tempDocFiles = daoServices.saveTemporaryImportUploads(fileList[file], fileListName[file], loggedInUser, map, savedTemporaryImportEntity)
                                                                    payload = "${payload}: File Saved [id=${tempDocFiles.id}]"
                                                                }
                                                                //Generate A Log Service Request to store the transactions done after all payloads created
                                                                val sr = commonDaoServices.mapServiceRequestForSuccess(map, payload, loggedInUser)
                                                                //Send user and email stating the application has been submitted successful
                                                                commonDaoServices.sendEmailWithUserEntity(loggedInUser, daoServices.diApplicationSubmittedUuid, savedTemporaryImportEntity, map, sr)
                                                                // Todo send email to management inspection officer clarification on who to choose needed
                                                                savedTemporaryImportEntity.entryPointId?.let { entry -> commonDaoServices.findUserProfileWithSectionIdAndDesignationId(entry, commonDaoServices.findDesignationByID(daoServices.diDesignationManagerInspectionId.toLong()), map.activeStatus).userId?.let { commonDaoServices.sendEmailWithUserEntity(it, daoServices.diApplicationAwaitingApprovalUuid, savedTemporaryImportEntity, map, sr) } }
                                                                return "$redirectDiApplicationListPage=${diApplicationTypeUuid}"
                                                            }
                                                }
                                                else -> throw ExpectedDataNotFound("No marching di Application Type with UUID  = ${diApplicationTypeEntity.uuid}, used for saving Temporary imports , does not Exist")
                                            }
                                        }
                            }
                }
    }


    @PreAuthorize("hasAuthority('DI_MANAGER_INSPECTION_READ')")
    @PostMapping("di-application/temporary-imports/update/save")
    fun updateForDiApplicationTemporaryImports(
            model: Model,
            @ModelAttribute("temporaryImportEntity") @Valid temporaryImportEntity: TemporaryImportApplicationsEntity,
            @RequestParam("diApplicationUuid") diApplicationUuid: String,
            results: BindingResult,
            redirectAttributes: RedirectAttributes
    ): String {
        commonDaoServices.serviceMapDetails(appId)
                .let { map ->
                    commonDaoServices.loggedInUserDetails()
                            .let { loggedInUser ->
                                daoServices.findTemporaryImportsWithUuid(diApplicationUuid)
                                        .let { diApplicationEntity ->
                                            val valueID: Long = diApplicationEntity.id.let { commonDaoServices.makeAnyNotBeNull(it) } as Long
                                            temporaryImportEntity.id = valueID

                                            // Before update of Details in DB
                                            if (temporaryImportEntity.confirmAssignedUserId != null) {
                                                with(temporaryImportEntity) {
                                                    assignedUser = confirmAssignedUserId?.let { commonDaoServices.findUserByID(it) }
                                                }
                                            }
                                            //updating of Details in DB
                                            val updatedTemporaryImport = daoServices.updateTemporaryImport(commonDaoServices.updateDetails(temporaryImportEntity, diApplicationEntity) as TemporaryImportApplicationsEntity, loggedInUser, map)

                                            // After update of Details in DB sending mail or any other actions depending with the transaction status
                                            when {
                                                temporaryImportEntity.approvalStatus == map.activeStatus -> {
                                                    val payload = "TEMPORARY IMPORT APPROVED [approvalStatus= ${temporaryImportEntity.approvalStatus}, approvalRemarks= ${temporaryImportEntity.approvalRemarks}]"
                                                    val sr = commonDaoServices.mapServiceRequestForSuccess(map, payload, loggedInUser)
                                                    updatedTemporaryImport.importerId?.let { commonDaoServices.sendEmailWithUserEntity(it, daoServices.diApplicationApprovedUuid, updatedTemporaryImport, map, sr) }
                                                }
                                                temporaryImportEntity.rejectedStatus == map.activeStatus -> {
                                                    val payload = "TEMPORARY IMPORT REJECTED [rejectedStatus= ${temporaryImportEntity.rejectedStatus}, rejectedRemarks= ${temporaryImportEntity.rejectedRemarks}]"
                                                    val sr = commonDaoServices.mapServiceRequestForSuccess(map, payload, loggedInUser)
                                                    updatedTemporaryImport.importerId?.let { commonDaoServices.sendEmailWithUserEntity(it, daoServices.diApplicationRejectedUuid, updatedTemporaryImport, map, sr) }
                                                }
                                                temporaryImportEntity.assignedUserStatus == map.activeStatus -> {
                                                    val payload = "TEMPORARY IMPORT ASSIGNED USER [assignedUserStatus= ${temporaryImportEntity.assignedUserStatus}, assignedUserRemarks= ${temporaryImportEntity.assignedUserRemarks}]"
                                                    val sr = commonDaoServices.mapServiceRequestForSuccess(map, payload, loggedInUser)
                                                    updatedTemporaryImport.assignedUser?.let { commonDaoServices.sendEmailWithUserEntity(it, daoServices.diApplicationAssignedUuid, updatedTemporaryImport, map, sr) }
                                                }
                                                temporaryImportEntity.issuesStatus != null -> {
                                                    val payload = "TEMPORARY IMPORT ISSUES ON APPLICATION [issuesStatus= ${temporaryImportEntity.issuesStatus}, issuesRemarks= ${temporaryImportEntity.issuesRemarks} ]"
                                                    val sr = commonDaoServices.mapServiceRequestForSuccess(map, payload, loggedInUser)
                                                    updatedTemporaryImport.importerId?.let { commonDaoServices.sendEmailWithUserEntity(it, daoServices.diApplicationIssuesUuid, updatedTemporaryImport, map, sr) }
                                                }
                                            }
                                            return "$redirectDiApplicationListPage=${diApplicationEntity.applicationType?.uuid}"

                                        }
                            }
                }
    }
}
