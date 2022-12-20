package org.kebs.app.kotlin.apollo.api.controllers.qaControllers

import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.QADaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.ReportsDaoService
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.ServiceRequestsEntity
import org.kebs.app.kotlin.apollo.store.model.qa.QaUploadsEntity
import org.kebs.app.kotlin.apollo.store.repo.UserSignatureRepository
import org.springframework.core.io.ResourceLoader
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import javax.servlet.http.HttpServletResponse


@RestController
@RequestMapping("api/v1/migration/qa")
class QualityAssuranceJSONControllers(
    private val applicationMapProperties: ApplicationMapProperties,
    private val qaDaoServices: QADaoServices,
    private val reportsDaoService: ReportsDaoService,
    private val resourceLoader: ResourceLoader,
    private val notifications: Notifications,
    private val commonDaoServices: CommonDaoServices,
    private val usersSignatureRepository: UserSignatureRepository

) {

    final val appId: Int = applicationMapProperties.mapQualityAssurance

    final val dMarkImageResource = resourceLoader.getResource(applicationMapProperties.mapDmarkImagePath)
    val dMarkImageFile = dMarkImageResource.file.toString()

    private val sMarkImageResource = resourceLoader.getResource(applicationMapProperties.mapSmarkImagePath)
    val sMarkImageFile = sMarkImageResource.file.toString()

    private val fMarkImageResource = resourceLoader.getResource(applicationMapProperties.mapFmarkImagePath)
    val fMarkImageFile = fMarkImageResource.file.toString()


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
        map["QrCode"] = foundPermitDetails.permitNumber.toString()
        val user = permit.varField6?.toLong().let { it?.let { it1 -> commonDaoServices.findUserByID(it1) } }


        if (user != null) {
            val mySignature: ByteArray?
            val image: ByteArrayInputStream?
            val signatureFromDb = user.id?.let { usersSignatureRepository.findByUserId(it) }
            if (signatureFromDb != null) {
                mySignature= signatureFromDb.signature
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
