package org.kebs.app.kotlin.apollo.api.controllers.qaControllers

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.QADaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.ReportsDaoService
import org.kebs.app.kotlin.apollo.api.ports.provided.lims.LimsServices
import org.kebs.app.kotlin.apollo.api.ports.provided.makeAnyNotBeNull
import org.kebs.app.kotlin.apollo.common.dto.ApiResponseModel
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.ServiceRequestsEntity
import org.kebs.app.kotlin.apollo.store.model.qa.QaUploadsEntity
import org.kebs.app.kotlin.apollo.store.repo.ICompanyProfileRepository
import org.kebs.app.kotlin.apollo.store.repo.IManufacturePlantDetailsRepository
import org.kebs.app.kotlin.apollo.store.repo.UserSignatureRepository
import org.kebs.app.kotlin.apollo.store.repo.qa.IPermitRatingRepository
import org.springframework.core.io.ResourceLoader
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.math.BigDecimal
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.sql.Date
import javax.servlet.http.HttpServletResponse


@RestController
@RequestMapping("api/v1/migration/qa")
class QualityAssuranceJSONControllers(
    private val applicationMapProperties: ApplicationMapProperties,
    private val qaDaoServices: QADaoServices,
    private val iPermitRatingRepo: IPermitRatingRepository,
    private val companyProfileRepo: ICompanyProfileRepository,
    private val manufacturePlantRepository: IManufacturePlantDetailsRepository,
    private val reportsDaoService: ReportsDaoService,
    private val resourceLoader: ResourceLoader,
    private val notifications: Notifications,
    private val commonDaoServices: CommonDaoServices,
    private val limsServices: LimsServices,
    private val usersSignatureRepository: UserSignatureRepository

) {

    final val appId: Int = applicationMapProperties.mapQualityAssurance

    final val dMarkImageResource = resourceLoader.getResource(applicationMapProperties.mapDmarkImagePath)
    val dMarkImageFile = dMarkImageResource.file.toString()

    private val sMarkImageResource = resourceLoader.getResource(applicationMapProperties.mapSmarkImagePath)
    val sMarkImageFile = sMarkImageResource.file.toString()

    private val fMarkImageResource = resourceLoader.getResource(applicationMapProperties.mapFmarkImagePath)
    val fMarkImageFile = fMarkImageResource.file.toString()


    /*:::::::::::::::::::::::::::::::::::::::::::::START INTERNAL USER FUNCTIONALITY ANGULAR:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

    @PostMapping("/upload/inspection-invoice")
    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updateWorkPlanDestructionReportUpload(
        @RequestParam("branchID") branchID: Long,
        @RequestParam("userPaidDate") userPaidDate: Date,
        @RequestParam("docFile") docFile: MultipartFile,
        model: Model
    ) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        val branchDetails = qaDaoServices.findPlantDetails(branchID)
        val upload = QaUploadsEntity()
        with(upload) {
            versionNumber = 1
            ordinaryStatus = 0
        }

        val fileDoc =
            qaDaoServices.uploadQaFile(upload, docFile, "INSPECTION_INVOICE_PAID", "NOT_PERMIT_DETAILS", loggedInUser)

        companyProfileRepo.findByIdOrNull(branchDetails.companyProfileId)
            ?.let { manufacture ->
                val ratesMap = iPermitRatingRepo.findAllByStatus(map.activeStatus)
                    ?: throw Exception("SMARK RATE SHOULD NOT BE NULL")
                val selectedRate = ratesMap.firstOrNull {
                    manufacture.yearlyTurnover!! > (it.min
                        ?: BigDecimal.ZERO) && manufacture.yearlyTurnover!! <= (it.max
                        ?: throw NullValueNotAllowedException("Max needs to be defined"))
                } ?: throw NullValueNotAllowedException("Rate not found")
                with(branchDetails) {
                    varField9 = fileDoc.id.toString()
                    inspectionFeeStatus = 1
                    invoiceInspectionGenerated = 1
                    paidDate = userPaidDate
                    val validityInYears = selectedRate.validity ?: throw Exception("INVALID NUMBER OF YEARS")
                    val yearsGotten = commonDaoServices.getCalculatedDateInLong(userPaidDate)
                    endingDate = when {
                        validityInYears == yearsGotten -> {
                            throw NullValueNotAllowedException("Inspection Fee has already reach The validity date for $validityInYears years after payment date")
                        }

                        yearsGotten > validityInYears -> {
                            throw NullValueNotAllowedException("Inspection Fee has already passed, The validity date for $validityInYears years after payment date")
                        }

                        else -> {
                            commonDaoServices.addYearsToCurrentDate(yearsGotten)
                        }
                    }

                }
                manufacturePlantRepository.save(branchDetails)
//                return  ServerResponse.ok().body("INSPECTION INVOICE UPLOADED SUCCESSFUL")
            } ?: throw NullValueNotAllowedException("No Company Record not found")
    }

    @PostMapping("/internal-users/apply/permit/upload-scheme-supervision")
    @PreAuthorize("hasAuthority('QA_OFFICER_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadSchemeOfSupervisionDataReport(
        @RequestParam("permitID") permitID: Long,
        @RequestParam("data") data: String,
        @RequestParam("docFile") docFile: List<MultipartFile>?,
        model: Model
    ): ApiResponseModel {

        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)
            var permit = qaDaoServices.findPermitBYID(permitID)
            val permitType =
                qaDaoServices.findPermitType(permit.permitType ?: throw Exception("MISSING PERMIT TYPE ID"))

            var versionNumber: Long = 1
            versionNumber = qaDaoServices.findAllUploadedFileBYPermitRefNumberAndPerMitIDAndSscStatus(
                permit.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER"),
                permit.id ?: throw Exception("MISSING PERMIT ID"),
                map.activeStatus
            ).size.toLong().plus(versionNumber)
            val fileDocList = mutableListOf<Long>()
            docFile?.forEach { fileDoc ->
                val uploads = QaUploadsEntity()
                with(uploads) {
                    ordinaryStatus = 0
                    sscStatus = 1
                }
                val fileDocSaved = qaDaoServices.saveQaFileUploads(
                    fileDoc,
                    "SCHEME_OF_SUPERVISION",
                    loggedInUser,
                    map,
                    uploads,
                    permit.permitRefNumber ?: throw Exception("MISSING PERMIT REF NUMBER"),
                    permit.id ?: throw Exception("MISSING PERMIT ID"),
                    versionNumber,
                    0
                )
                fileDocSaved.second.id?.let { fileDocList.add(it) }
            }

            with(permit) {
                generateSchemeStatus = 1
                approvedRejectedScheme = 1
                sscId = fileDocList[0]
                permitStatus = applicationMapProperties.mapQaStatusPSSF
                userTaskId = applicationMapProperties.mapUserTaskNameQAO
            }
            //updating of Details in DB
            val updateResults = qaDaoServices.permitUpdateDetails(permit, map, loggedInUser)

            return when (updateResults.first.status) {
                map.successStatus -> {
                    permit = updateResults.second
                    val batchID: Long? = qaDaoServices.getBatchID(permit, map, permitID)
                    val batchIDDifference: Long? = qaDaoServices.getBatchIDDifference(permit, map, permitID)
                    val permitAllDetails = qaDaoServices.mapAllPermitDetailsTogetherForInternalUsers(
                        permit,
                        batchID,
                        batchIDDifference,
                        map
                    )
                    commonDaoServices.setSuccessResponse(permitAllDetails, null, null, null)
                }

                else -> {
                    commonDaoServices.setErrorResponse(updateResults.first.responseMessage ?: "UNKNOWN_ERROR")
                }
            }
        } catch (error: Exception) {
            return commonDaoServices.setErrorResponse(error.message ?: "UNKNOWN_ERROR")
        }

    }

    @PostMapping("/internal-users/apply/permit/uploadInspectionReport")
    @PreAuthorize("hasAuthority('QA_OFFICER_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadInspectionReport(
        @RequestParam("permitID") permitID: Long,
        @RequestParam("inspectionReportId") inspectionReportId: String,
        @RequestParam("data") data: String,
        @RequestParam("docFile") docFile: List<MultipartFile>?,
        model: Model
    ): ApiResponseModel {

        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)
            var permit = qaDaoServices.findPermitBYID(permitID)

            var versionNumber: Long = 1
            versionNumber = qaDaoServices.findAllUploadedFileBYPermitRefNumberAndPerMitIDAndSscStatus(
                permit.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER"),
                permit.id ?: throw Exception("MISSING PERMIT ID"),
                map.activeStatus
            ).size.toLong().plus(versionNumber)
            val fileDocList = mutableListOf<Long>()
            docFile?.forEach { fileDoc ->
                val uploads = QaUploadsEntity()
                with(uploads) {
                    ordinaryStatus = 0
                    inspectionReportStatus = 1
                    varField1 = inspectionReportId
                }
                val fileDocSaved = qaDaoServices.saveQaFileUploads(
                    fileDoc,
                    "INSPECTION_REPORT",
                    loggedInUser,
                    map,
                    uploads,
                    permit.permitRefNumber ?: throw Exception("MISSING PERMIT REF NUMBER"),
                    permit.id ?: throw Exception("MISSING PERMIT ID"),
                    versionNumber,
                    0
                )
                fileDocSaved.second.id?.let { fileDocList.add(it) }
            }
            //updating of Details in DB
            val updateResults = qaDaoServices.permitUpdateDetails(permit, map, loggedInUser)
            return when (updateResults.first.status) {
                map.successStatus -> {
                    permit = updateResults.second
                    val batchID: Long? = qaDaoServices.getBatchID(permit, map, permitID)
                    val batchIDDifference: Long? = qaDaoServices.getBatchIDDifference(permit, map, permitID)
                    val permitAllDetails = qaDaoServices.mapAllPermitDetailsTogetherForInternalUsers(
                        permit,
                        batchID,
                        batchIDDifference,
                        map
                    )
                    commonDaoServices.setSuccessResponse(permitAllDetails, null, null, null)
                }

                else -> {
                    commonDaoServices.setErrorResponse(updateResults.first.responseMessage ?: "UNKNOWN_ERROR")
                }
            }
        } catch (error: Exception) {
            return commonDaoServices.setErrorResponse(error.message ?: "UNKNOWN_ERROR")
        }

    }


    @PostMapping("/internal-users/apply/permit/upload-docs")
    @PreAuthorize("hasAuthority('QA_OFFICER_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadDocuments(
        @RequestParam("permitID") permitID: Long,
        @RequestParam("data") data: String,
        @RequestParam("docFileName") docFileName: String,
        @RequestParam("docFile") docFile: List<MultipartFile>?,
        model: Model
    ): ApiResponseModel {

        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)
            var permit = qaDaoServices.findPermitBYID(permitID)
            val permitType =
                qaDaoServices.findPermitType(permit.permitType ?: throw Exception("MISSING PERMIT TYPE ID"))

            val versionNumber: Long = 1
            val fileDocList = mutableListOf<Long>()
            docFile?.forEach { fileDoc ->
                val uploads = QaUploadsEntity()
                with(uploads) {
                    ordinaryStatus = 1
                }
                val fileDocSaved = qaDaoServices.saveQaFileUploads(
                    fileDoc,
                    docFileName,
                    loggedInUser,
                    map,
                    uploads,
                    permit.permitRefNumber ?: throw Exception("MISSING PERMIT REF NUMBER"),
                    permit.id ?: throw Exception("MISSING PERMIT ID"),
                    versionNumber,
                    0
                )
                fileDocSaved.second.id?.let { fileDocList.add(it) }
            }

            with(permit) {
                userTaskId = applicationMapProperties.mapUserTaskNameQAO
            }
            //updating of Details in DB
            val updateResults = qaDaoServices.permitUpdateDetails(permit, map, loggedInUser)

            return when (updateResults.first.status) {
                map.successStatus -> {
                    permit = updateResults.second
                    val batchID: Long? = qaDaoServices.getBatchID(permit, map, permitID)
                    val batchIDDifference: Long? = qaDaoServices.getBatchIDDifference(permit, map, permitID)
                    val permitAllDetails = qaDaoServices.mapAllPermitDetailsTogetherForInternalUsers(
                        permit,
                        batchID,
                        batchIDDifference,
                        map
                    )
                    commonDaoServices.setSuccessResponse(permitAllDetails, null, null, null)
                }

                else -> {
                    commonDaoServices.setErrorResponse(updateResults.first.responseMessage ?: "UNKNOWN_ERROR")
                }
            }
        } catch (error: Exception) {
            return commonDaoServices.setErrorResponse(error.message ?: "UNKNOWN_ERROR")
        }

    }

    @PostMapping("/internal-users/apply/permit/upload-justification-report")
    @PreAuthorize("hasAuthority('QA_OFFICER_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadJustificationReport(
        @RequestParam("permitID") permitID: Long,
        @RequestParam("data") data: String,
        @RequestParam("docFile") docFile: List<MultipartFile>?,
        model: Model
    ): ApiResponseModel {

        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)
            var permit = qaDaoServices.findPermitBYID(permitID)
            val permitType =
                qaDaoServices.findPermitType(permit.permitType ?: throw Exception("MISSING PERMIT TYPE ID"))

            val versionNumber: Long = 1
            val fileDocList = mutableListOf<Long>()
            docFile?.forEach { fileDoc ->
                val uploads = QaUploadsEntity()
                with(uploads) {
                    ordinaryStatus = 0
                    justificationReportStatus = 1
                }
                val fileDocSaved = qaDaoServices.saveQaFileUploads(
                    fileDoc,
                    "JUSTIFICATION_REPORT_DMARK_ASSESSMENT",
                    loggedInUser,
                    map,
                    uploads,
                    permit.permitRefNumber ?: throw Exception("MISSING PERMIT REF NUMBER"),
                    permit.id ?: throw Exception("MISSING PERMIT ID"),
                    versionNumber,
                    0
                )
                fileDocSaved.second.id?.let { fileDocList.add(it) }
            }

            with(permit) {
                justificationReportStatus = 1
                approvedRejectedScheme = 1
//                jus = fileDocList[0]
                permitStatus = applicationMapProperties.mapQaStatusPInspectionReportApproval
                userTaskId = applicationMapProperties.mapUserTaskNameHOD
            }
            //updating of Details in DB
            val updateResults = qaDaoServices.permitUpdateDetails(permit, map, loggedInUser)

            return when (updateResults.first.status) {
                map.successStatus -> {
                    permit = updateResults.second
                    val batchID: Long? = qaDaoServices.getBatchID(permit, map, permitID)
                    val batchIDDifference: Long? = qaDaoServices.getBatchIDDifference(permit, map, permitID)
                    val permitAllDetails = qaDaoServices.mapAllPermitDetailsTogetherForInternalUsers(
                        permit,
                        batchID,
                        batchIDDifference,
                        map
                    )
                    commonDaoServices.setSuccessResponse(permitAllDetails, null, null, null)
                }

                else -> {
                    commonDaoServices.setErrorResponse(updateResults.first.responseMessage ?: "UNKNOWN_ERROR")
                }
            }
        } catch (error: Exception) {
            return commonDaoServices.setErrorResponse(error.message ?: "UNKNOWN_ERROR")
        }

    }

    @PostMapping("/internal-users/apply/permit/upload-assessment-report")
    @PreAuthorize("hasAuthority('QA_OFFICER_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadAssessmentReport(
        @RequestParam("permitID") permitID: Long,
        @RequestParam("data") data: String,
        @RequestParam("docFile") docFile: List<MultipartFile>?,
        model: Model
    ): ApiResponseModel {

        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val map = commonDaoServices.serviceMapDetails(appId)
            var permit = qaDaoServices.findPermitBYID(permitID)
            val permitType =
                qaDaoServices.findPermitType(permit.permitType ?: throw Exception("MISSING PERMIT TYPE ID"))

            val versionNumber: Long = 1
            val fileDocList = mutableListOf<Long>()
            docFile?.forEach { fileDoc ->
                val uploads = QaUploadsEntity()
                with(uploads) {
                    ordinaryStatus = 0
                    assessmentReportStatus = 1
                }
                val fileDocSaved = qaDaoServices.saveQaFileUploads(
                    fileDoc,
                    "FACTORY_ASSESSMENT_REPORT",
                    loggedInUser,
                    map,
                    uploads,
                    permit.permitRefNumber ?: throw Exception("MISSING PERMIT REF NUMBER"),
                    permit.id ?: throw Exception("MISSING PERMIT ID"),
                    versionNumber,
                    0
                )
                fileDocSaved.second.id?.let { fileDocList.add(it) }
            }

//            val hodDetails = qaDaoServices.assignNextOfficerAfterPayment(permit, map, applicationMapProperties.mapQADesignationIDForHODId)

            with(permit) {
                assessmentScheduledStatus = 1
//                assessmentReportRemarks = 1
//                jus = fileDocList[0]
//                hodId = hodDetails?.id
                permitStatus = applicationMapProperties.mapQaStatusPApprovalAssesmentReport
                userTaskId = applicationMapProperties.mapUserTaskNameHOD
            }
            //updating of Details in DB
            val updateResults = qaDaoServices.permitUpdateDetails(permit, map, loggedInUser)

            return when (updateResults.first.status) {
                map.successStatus -> {
                    permit = updateResults.second
                    val batchID: Long? = qaDaoServices.getBatchID(permit, map, permitID)
                    val batchIDDifference: Long? = qaDaoServices.getBatchIDDifference(permit, map, permitID)
                    val permitAllDetails = qaDaoServices.mapAllPermitDetailsTogetherForInternalUsers(
                        permit,
                        batchID,
                        batchIDDifference,
                        map
                    )
                    commonDaoServices.setSuccessResponse(permitAllDetails, null, null, null)
                }

                else -> {
                    commonDaoServices.setErrorResponse(updateResults.first.responseMessage ?: "UNKNOWN_ERROR")
                }
            }
        } catch (error: Exception) {
            return commonDaoServices.setErrorResponse(error.message ?: "UNKNOWN_ERROR")
        }

    }

    @GetMapping("/view/attached-lab-pdf")
    fun downloadFileLabResultsDocument(
        response: HttpServletResponse,
        @RequestParam("fileName") fileName: String,
        @RequestParam("bsNumber") bsNumber: String
    ) {
        val file = limsServices.mainFunctionLimsGetPDF(bsNumber, fileName)
        //            val targetFile = File(Files.createTempDir(), fileName)
//            targetFile.deleteOnExit()
        response.contentType = commonDaoServices.getFileTypeByMimetypesFileTypeMap(file.name)
//                    response.setHeader("Content-Length", pdfReportStream.size().toString())
        response.addHeader("Content-Disposition", "inline; filename=${file.name}")
        response.outputStream
            .let { responseOutputStream ->
                responseOutputStream.write(file.readBytes())
                responseOutputStream.close()
            }

        KotlinLogging.logger { }.info("VIEW FILE SUCCESSFUL")

    }

    /*:::::::::::::::::::::::::::::::::::::::::::::END INTERNAL USER FUNCTIONALITY ANGULAR:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/


    @GetMapping("/view/inspection-invoice")
    fun viewPDFFileLabResultsDocument(
        response: HttpServletResponse,
        @RequestParam("fileID") fileID: Long
    ) {
        val fileUploaded = qaDaoServices.findUploadedFileBYId(fileID)
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

    @GetMapping("/view/attached")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun downloadFileDocument(
        response: HttpServletResponse,
        @RequestParam("fileID") fileID: Long
    ) {
        val fileUploaded = qaDaoServices.findUploadedFileBYId(fileID)
        val mappedFileClass = commonDaoServices.mapClass(fileUploaded)
        commonDaoServices.downloadFile(response, mappedFileClass)
    }

    @PostMapping("/permit/apply/sta3-update-upload")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadFilesQASTA3Permit(
        @RequestParam("permitID") permitID: Long,
        @RequestParam("docFile") docFile: List<MultipartFile>,
        model: Model
    ): CommonDaoServices.MessageSuccessFailDTO {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        var permitDetails = qaDaoServices.findPermitBYID(permitID)

        docFile.forEach { u ->
            val upload = QaUploadsEntity()
            with(upload) {
                permitId = permitDetails.id
                versionNumber = 1
                sta3Status = 1
                ordinaryStatus = 0
            }
            qaDaoServices.uploadQaFile(
                upload,
                u,
                "STA3-UPLOADS",
                permitDetails.permitRefNumber ?: throw Exception("MISSING PERMIT REF NUMBER"),
                loggedInUser
            )
        }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
//        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitDetails.id}"
        sm.message = "Document Uploaded successful"

        return sm
//        return commonDaoServices.returnValues(result ?: throw Exception("invalid results"), map, sm)
    }

    @PostMapping("/permit/apply/sta10-update-upload")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadFilesQASTA10Permit(
        @RequestParam("permitID") permitID: Long,
        @RequestParam("docFile") docFile: List<MultipartFile>,
        model: Model
    ): CommonDaoServices.MessageSuccessFailDTO {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        var permitDetails = qaDaoServices.findPermitBYID(permitID)

        docFile.forEach { u ->
            val upload = QaUploadsEntity()
            with(upload) {
                permitId = permitDetails.id
                versionNumber = 1
                sta10Status = 1
                ordinaryStatus = 0
            }
            qaDaoServices.uploadQaFile(
                upload,
                u,
                "STA10-UPLOADS",
                permitDetails.permitRefNumber ?: throw Exception("MISSING PERMIT REF NUMBER"),
                loggedInUser
            )
        }

        with(permitDetails) {
            sta10FilledStatus = map.activeStatus
            permitStatus = applicationMapProperties.mapQaStatusPSubmission
        }

        permitDetails = qaDaoServices.permitUpdateDetails(permitDetails, map, loggedInUser).second
        val sm = CommonDaoServices.MessageSuccessFailDTO()
//        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitDetails.id}"
        sm.message = "Document Uploaded successful"

        return sm
//        return commonDaoServices.returnValues(result ?: throw Exception("invalid results"), map, sm)
    }


    @PostMapping("/permit/apply/ordinary-upload")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadFilesOrdinaryPermit(
        @RequestParam("permitID") permitID: Long,
        @RequestParam("docDesc") docDesc: String,
        @RequestParam("docFile") docFile: List<MultipartFile>,
        model: Model
    ): CommonDaoServices.MessageSuccessFailDTO {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        var permitDetails = qaDaoServices.findPermitBYID(permitID)

        docFile.forEach { u ->
            val upload = QaUploadsEntity()
            with(upload) {
                permitId = permitDetails.id
                versionNumber = 1
                ordinaryStatus = 1
            }
            qaDaoServices.uploadQaFile(
                upload,
                u,
                docDesc,
                permitDetails.permitRefNumber ?: throw Exception("MISSING PERMIT REF NUMBER"),
                loggedInUser
            )
        }

        with(permitDetails) {
            sta10FilledStatus = map.inactiveStatus
            permitStatus = applicationMapProperties.mapQaStatusPSubmission
        }

        permitDetails = qaDaoServices.permitUpdateDetails(permitDetails, map, loggedInUser).second
        val sm = CommonDaoServices.MessageSuccessFailDTO()
//        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitDetails.id}"
        sm.message = "Document Uploaded successful"

        return sm
//        return commonDaoServices.returnValues(result ?: throw Exception("invalid results"), map, sm)
    }


    @RequestMapping(value = ["/report/proforma-invoice-with-Item"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun proformaInvoiceWithMoreItems(
        response: HttpServletResponse,
        @RequestParam(value = "ID") ID: Long
    ) {
        var map = hashMapOf<String, Any>()
//        val cdItemDetailsEntity = daoServices.findItemWithItemID(id)
        val batchInvoice = qaDaoServices.findBatchInvoicesWithID(ID)
        val batchInvoiceList = qaDaoServices.findALlInvoicesPermitWithBatchID(
            batchInvoice.id ?: throw ExpectedDataNotFound("MISSING BATCH INVOICE ID")
        )
        val companyProfile = commonDaoServices.findCompanyProfileWithID(
            commonDaoServices.findUserByID(
                batchInvoice.userId ?: throw ExpectedDataNotFound("MISSING USER ID")
            ).companyId ?: throw ExpectedDataNotFound("MISSING USER ID")
        )

        map["preparedBy"] = batchInvoice.createdBy.toString()
        map["datePrepared"] = commonDaoServices.convertTimestampToKeswsValidDate(
            batchInvoice.createdOn ?: throw ExpectedDataNotFound("MISSING CREATION DATE")
        )
        map["demandNoteNo"] = batchInvoice.sageInvoiceNumber.toString()
        map["companyName"] = companyProfile.name.toString()
        map["companyAddress"] = companyProfile.postalAddress.toString()
        map["companyTelephone"] = companyProfile.companyTelephone.toString()
//        map["productName"] = demandNote?.product.toString()
//        map["cfValue"] = demandNote?.cfvalue.toString()
//        map["rate"] = demandNote?.rate.toString()
//        map["amountPayable"] = demandNote?.amountPayable.toString()
        map["customerNo"] = companyProfile.entryNumber.toString()
        map["totalAmount"] = batchInvoice.totalAmount.toString()
        //Todo: config for amount in words

//                    map["amountInWords"] = demandNote?.
        map["receiptNo"] = batchInvoice.receiptNo.toString()

        map = reportsDaoService.addBankAndMPESADetails(map, batchInvoice.sageInvoiceNumber.toString())

        reportsDaoService.extractReport(
            map,
            response,
            applicationMapProperties.mapReportProfomaInvoiceWithItemsPath,
            batchInvoiceList
        )
    }

    @RequestMapping(value = ["/report/braked-down-invoice-with-Item"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun brakedDownInvoiceWithMoreItems(
        response: HttpServletResponse,
        @RequestParam(value = "ID") ID: Long
    ) {
        var map = hashMapOf<String, Any>()
//        val cdItemDetailsEntity = daoServices.findItemWithItemID(id)
        val masterInvoice = qaDaoServices.findPermitInvoiceByPermitID(ID)
        val invoiceDetailsList = qaDaoServices.findALlInvoicesPermitWithMasterInvoiceID(
            masterInvoice.id, 1
        )
        val companyProfile = commonDaoServices.findCompanyProfileWithID(
            commonDaoServices.findUserByID(
                masterInvoice.userId ?: throw ExpectedDataNotFound("MISSING USER ID")
            ).companyId ?: throw ExpectedDataNotFound("MISSING USER ID")
        )

        map["preparedBy"] = masterInvoice.createdBy.toString()
        map["datePrepared"] = commonDaoServices.convertTimestampToKeswsValidDate(
            masterInvoice.createdOn ?: throw ExpectedDataNotFound("MISSING CREATION DATE")
        )
        map["demandNoteNo"] = masterInvoice.invoiceRef.toString()
        map["companyName"] = companyProfile.name.toString()
        map["companyAddress"] = companyProfile.postalAddress.toString()
        map["companyTelephone"] = companyProfile.companyTelephone.toString()
//        map["productName"] = demandNote?.product.toString()
//        map["cfValue"] = demandNote?.cfvalue.toString()
//        map["rate"] = demandNote?.rate.toString()
//        map["amountPayable"] = demandNote?.amountPayable.toString()
        map["customerNo"] = companyProfile.entryNumber.toString()
        map["taxAmount"] = masterInvoice.taxAmount.toString()
        map["subTotalAmount"] = masterInvoice.subTotalBeforeTax.toString()
        map["totalAmount"] = masterInvoice.totalAmount.toString()
        //Todo: config for amount in words

//                    map["amountInWords"] = demandNote?.
        map["receiptNo"] = masterInvoice.receiptNo.toString()

        map = reportsDaoService.addBankAndMPESADetails(map, masterInvoice.invoiceRef.toString())

        reportsDaoService.extractReport(
            map,
            response,
            applicationMapProperties.mapReportBreakDownInvoiceWithItemsPath,
            invoiceDetailsList
        )
    }

    /*
 DMARK Permit Report
  */
    @RequestMapping(value = ["/report/permit-certificate"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun certificatePermit(
        response: HttpServletResponse,
        @RequestParam(value = "permitID") permitID: Long
    ) {
        var map = hashMapOf<String, Any>()
        val appId: Int = applicationMapProperties.mapQualityAssurance
        val s = commonDaoServices.serviceMapDetails(appId)
        val permit = qaDaoServices.findPermitBYID(permitID)


        val foundPermitDetails = qaDaoServices.permitDetails(permit, s)

        val q = foundPermitDetails.permitNumber
        val url =
            "${applicationMapProperties.baseEndPointValue}/getAllAwardedPermits?permitNumber=" + URLEncoder.encode(
                q,
                StandardCharsets.UTF_8
            )


        map["FirmName"] = foundPermitDetails.firmName.toString()
        map["PermitNo"] = foundPermitDetails.permitNumber.toString()
        map["PostalAddress"] = foundPermitDetails.postalAddress.toString()
        map["PhysicalAddress"] = foundPermitDetails.physicalAddress.toString()
        map["DateOfIssue"] =
            foundPermitDetails.dateOfIssue?.let { commonDaoServices.convertDateToString(it, "dd-MM-YYYY") }!!
        map["ExpiryDate"] =
            foundPermitDetails.dateOfExpiry?.let { commonDaoServices.convertDateToString(it, "dd-MM-YYYY") }!!
        map["EffectiveDate"] =
            foundPermitDetails.effectiveDate?.let { commonDaoServices.convertDateToString(it, "dd-MM-YYYY") }!!

//        map["FirmName"] = foundPermitDetails.firmName.toString()
        map["CommodityDesc"] = foundPermitDetails.commodityDescription.toString()
        map["TradeMark"] = foundPermitDetails.brandName.toString()
        map["StandardTitle"] = "${foundPermitDetails.standardNumber}".plus(" ${foundPermitDetails.standardTitle}")

        map["faxNumber"] = foundPermitDetails.faxNo.toString()
        map["EmailAddress"] = foundPermitDetails.email.toString()
        map["phoneNumber"] = foundPermitDetails.telephoneNo.toString()
//        map["QrCode"] = url
        map["QrCode"] = "${applicationMapProperties.baseUrlQRValue}qr-code-qa-permit-scan#${foundPermitDetails.permitNumber}"


        val user = permit.varField6?.toLong().let { it?.let { it1 -> commonDaoServices.findUserByID(it1) } }


        if (user != null) {
            val mySignature: ByteArray?
            val image: ByteArrayInputStream?
            val signatureFromDb = user.id?.let { usersSignatureRepository.findByUserId(it) }
            if (signatureFromDb != null) {
                mySignature = signatureFromDb.signature
                image = ByteArrayInputStream(mySignature)
                map["Signature"] = image

            }
        }


        when (foundPermitDetails.permitTypeID) {
            applicationMapProperties.mapQAPermitTypeIDDmark -> {
                map["DmarkLogo"] = dMarkImageFile
                reportsDaoService.extractReportEmptyDataSource(
                    map,
                    response,
                    applicationMapProperties.mapReportDmarkPermitReportPath
                )
            }

            applicationMapProperties.mapQAPermitTypeIdSmark -> {
                map["SmarkLogo"] = sMarkImageFile
                reportsDaoService.extractReportEmptyDataSource(
                    map,
                    response,
                    applicationMapProperties.mapReportSmarkPermitReportPath
                )
            }

            applicationMapProperties.mapQAPermitTypeIdFmark -> {
                map["FmarkLogo"] = fMarkImageFile
                reportsDaoService.extractReportEmptyDataSource(
                    map,
                    response,
                    applicationMapProperties.mapReportFmarkPermitReportPath
                )
            }
        }
    }


//    @PostMapping("/permit/apply/updateMigratedPermit")
////    https://localhost:8006/api/v1/migration/qa/permit/apply/updateMigratedPermit
//    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
//    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
//    fun updatePermitMigrated(
//        @RequestParam("permitId") permitId: String,
//        @RequestParam("permitIdBeingMigrated") permitIdBeingMigrated: String
//    ): CommonDaoServices.MessageSuccessFailDTO {
//
//        val map = commonDaoServices.serviceMapDetails(appId)
//        val loggedInUser = commonDaoServices.loggedInUserDetails()
//        println(permitId)
//       // var permitDetails = qaDaoServices.findPermitBYID(permitId)
////        return try {
////            val map = commonDaoServices.serviceMapDetails(appId)
////            val loggedInUser = commonDaoServices.loggedInUserDetails()
////
////
////            println(permitId)
////            println(permitIdBeingMigrated)
////
//////            qaDaoServices.permitInvoiceSTKPush(map, loggedInUser, dto.phoneNumber, invoiceEntity)
////
//////            val messageDto = MigratedPermitDto(
//////                invoiceEntity
//////            )
////            ServerResponse.ok().body(permitId)
////
////        } catch (e: Exception) {
////            KotlinLogging.logger { }.error(e.message)
////            KotlinLogging.logger { }.debug(e.message, e)
////            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
////        }
//
//        val sm = CommonDaoServices.MessageSuccessFailDTO()
////        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitDetails.id}"
//        sm.message = "Document Uploaded successful"
//
//        return sm
////        return commonDaoServices.returnValues(result
//    }


    @PostMapping("/kebs/add/new-upload")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadFilesQA(
        @RequestParam("permitID") permitID: Long,
        @RequestParam("manufactureNonStatus") manufactureNonStatus: Int,
        @RequestParam("ordinaryStatus") ordinaryStatus: Int,
        @RequestParam("inspectionReportStatus") inspectionReportStatus: Int?,
        @RequestParam("sta10Status") sta10Status: Int?,
        @RequestParam("sscUploadStatus") sscUploadStatus: Int?,
        @RequestParam("scfStatus") scfStatus: Int?,
        @RequestParam("ssfStatus") ssfStatus: Int?,
        @RequestParam("cocStatus") cocStatus: Int?,
        @RequestParam("assessmentReportStatus") assessmentReportStatus: Int?,
        @RequestParam("labResultsStatus") labResultsStatus: Int?,
        @RequestParam("docFileName") docFileName: String,
        @RequestParam("doc_file") docFile: MultipartFile,
        @RequestParam("assessment_recommendations") assessmentRecommendations: String?,
        model: Model
    ): CommonDaoServices.MessageSuccessFailDTO {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        var permitDetails = qaDaoServices.findPermitBYID(permitID)

//        val result: ServiceRequestsEntity?
        val uploads = QaUploadsEntity()
        var versionNumber: Long = 1
        var uploadResults: Pair<ServiceRequestsEntity, QaUploadsEntity>? = null

        when (ordinaryStatus) {
            map.activeStatus -> {
                uploads.ordinaryStatus = ordinaryStatus
                uploadResults = qaDaoServices.saveQaFileUploads(
                    docFile,
                    docFileName,
                    loggedInUser,
                    map,
                    uploads,
                    permitDetails.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER"),
                    permitDetails.id ?: throw Exception("INVALID PERMIT ID"),
                    versionNumber,
                    manufactureNonStatus
                )
            }

            map.inactiveStatus -> {
                uploads.ordinaryStatus = ordinaryStatus
                when {
                    cocStatus != null -> {
                        uploads.cocStatus = cocStatus
                        versionNumber = qaDaoServices.findAllUploadedFileBYPermitRefNumberAndCocStatus(
                            permitDetails.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER"),
                            map.activeStatus
                        ).size.toLong().plus(versionNumber)
                        uploadResults = qaDaoServices.saveQaFileUploads(
                            docFile,
                            docFileName,
                            loggedInUser,
                            map,
                            uploads,
                            permitDetails.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER"),
                            permitDetails.id ?: throw Exception("INVALID PERMIT ID"),
                            versionNumber,
                            manufactureNonStatus
                        )
                        permitDetails.cocId = uploadResults.second.id
                        permitDetails = qaDaoServices.permitUpdateDetails(permitDetails, map, loggedInUser).second
                        qaDaoServices.permitInsertStatus(
                            permitDetails,
                            applicationMapProperties.mapQaStatusCocUploaded,
                            loggedInUser
                        )

                    }

                    sscUploadStatus != null -> {
                        uploads.sscStatus = sscUploadStatus
                        versionNumber = qaDaoServices.findAllUploadedFileBYPermitRefNumberAndSscStatus(
                            permitDetails.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER"),
                            map.activeStatus
                        ).size.toLong().plus(versionNumber)
                        uploadResults = qaDaoServices.saveQaFileUploads(
                            docFile,
                            docFileName,
                            loggedInUser,
                            map,
                            uploads,
                            permitDetails.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER"),
                            permitDetails.id ?: throw Exception("INVALID PERMIT ID"),
                            versionNumber,
                            manufactureNonStatus
                        )
                        permitDetails.generateSchemeStatus = map.activeStatus
                        permitDetails.sscId = uploadResults.second.id
                        permitDetails = qaDaoServices.permitUpdateDetails(permitDetails, map, loggedInUser).second
                        qaDaoServices.permitInsertStatus(
                            permitDetails,
                            applicationMapProperties.mapQaStatusPApprSSC,
                            loggedInUser
                        )

                    }

                    assessmentReportStatus != null -> {
                        uploads.assessmentReportStatus = assessmentReportStatus
                        versionNumber = qaDaoServices.findAllUploadedFileBYPermitRefNumberAndAssessmentReportStatus(
                            permitDetails.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER"),
                            map.activeStatus
                        ).size.toLong().plus(versionNumber)
                        uploadResults = qaDaoServices.saveQaFileUploads(
                            docFile,
                            docFileName,
                            loggedInUser,
                            map,
                            uploads,
                            permitDetails.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER"),
                            permitDetails.id ?: throw Exception("INVALID PERMIT ID"),
                            versionNumber,
                            manufactureNonStatus
                        )

                        val hodDetails = qaDaoServices.assignNextOfficerAfterPayment(
                            permitDetails,
                            map,
                            applicationMapProperties.mapQADesignationIDForHODId
                        )


                        with(permitDetails) {
                            assessmentScheduledStatus = map.successStatus
                            assessmentReportRemarks = assessmentRecommendations
                            hodId = hodDetails?.id
                            permitStatus = applicationMapProperties.mapQaStatusPApprovalAssesmentReport
                            userTaskId = applicationMapProperties.mapUserTaskNameHOD
                        }
                        qaDaoServices.permitUpdateDetails(permitDetails, map, loggedInUser)

                        //Send notification to PAC secretary
                        val hodSec = hodDetails?.id?.let { commonDaoServices.findUserByID(it) }
                        hodSec?.email?.let {
                            qaDaoServices.sendPacDmarkAssessmentNotificationEmail(
                                it,
                                permitDetails
                            )
                        }

//                        permitDetails.generateSchemeStatus = map.activeStatus
//                        permitDetails.sscId = uploadResults.second.id
//                        permitDetails = qaDaoServices.permitUpdateDetails(permitDetails, map, loggedInUser).second
//                        qaDaoServices.permitInsertStatus(
//                            permitDetails,
//                            applicationMapProperties.mapQaStatusPApprSSC,
//                            loggedInUser
//                        )

                    }

                    inspectionReportStatus != null -> {
                        uploads.inspectionReportStatus = inspectionReportStatus
//                        versionNumber = qaDaoServices.findAllUploadedFileBYPermitIDAndSscStatus(permitID, map.activeStatus).size.toLong().plus(versionNumber)
                        uploadResults = qaDaoServices.saveQaFileUploads(
                            docFile,
                            docFileName,
                            loggedInUser,
                            map,
                            uploads,
                            permitDetails.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER"),
                            permitDetails.id ?: throw Exception("INVALID PERMIT ID"),
                            versionNumber,
                            manufactureNonStatus
                        )
//                        permitDetails.generateSchemeStatus = map.activeStatus
//                        permitDetails.sscId = uploadResults.second.id
                        permitDetails = qaDaoServices.permitUpdateDetails(permitDetails, map, loggedInUser).second
//                        qaDaoServices.permitInsertStatus(permitDetails, applicationMapProperties.mapQaStatusPApprSSC, loggedInUser)

                    }

                    sta10Status != null -> {
                        uploads.sta10Status = sta10Status
//                        versionNumber = qaDaoServices.findAllUploadedFileBYPermitIDAndSscStatus(permitID, map.activeStatus).size.toLong().plus(versionNumber)
                        uploadResults = qaDaoServices.saveQaFileUploads(
                            docFile,
                            docFileName,
                            loggedInUser,
                            map,
                            uploads,
                            permitDetails.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER"),
                            permitDetails.id ?: throw Exception("INVALID PERMIT ID"),
                            versionNumber,
                            manufactureNonStatus
                        )
//                        permitDetails.generateSchemeStatus = map.activeStatus
//                        permitDetails.sscId = uploadResults.second.id
                        permitDetails = qaDaoServices.permitUpdateDetails(permitDetails, map, loggedInUser).second
//                        qaDaoServices.permitInsertStatus(permitDetails, applicationMapProperties.mapQaStatusPApprSSC, loggedInUser)

                    }
                }

            }
        }


//        result = uploadResults?.first

        val sm = CommonDaoServices.MessageSuccessFailDTO()
//        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitDetails.id}"
        sm.message = "Document Uploaded successful"

        return sm
//        return commonDaoServices.returnValues(result ?: throw Exception("invalid results"), map, sm)

    }
}
