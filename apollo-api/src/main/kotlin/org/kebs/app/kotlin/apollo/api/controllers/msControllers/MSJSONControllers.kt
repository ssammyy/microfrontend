package org.kebs.app.kotlin.apollo.api.controllers.msControllers

import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.*
import org.kebs.app.kotlin.apollo.api.ports.provided.emailDTO.WorkPlanScheduledDTO
import org.kebs.app.kotlin.apollo.api.ports.provided.lims.LimsServices
import org.kebs.app.kotlin.apollo.common.dto.ms.*
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.MsSampleSubmissionEntity
import org.kebs.app.kotlin.apollo.store.model.ms.MsUploadsEntity
import org.kebs.app.kotlin.apollo.store.repo.ICompanyProfileRepository
import org.kebs.app.kotlin.apollo.store.repo.UserSignatureRepository
import org.kebs.app.kotlin.apollo.store.repo.ms.*
import org.springframework.core.io.ResourceLoader
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import javax.servlet.http.HttpServletResponse


@RestController
@RequestMapping("/api/v1/migration/ms")
class MSJSONControllers(
    private val applicationMapProperties: ApplicationMapProperties,
    private val iSampleCollectViewRepo: ISampleCollectionViewRepository,
    private val iSampleSubmissionViewRepo: IMsSampleSubmissionViewRepository,
    private val iFieldReportViewRepo: IMsFieldReportViewRepository,
    private val sampleSubmitRepo: IMSSampleSubmissionRepository,
    private val fuelRemediationInvoiceRepo: IFuelRemediationInvoiceRepository,
    private val commonDaoServices: CommonDaoServices,
    private val reportsDaoService: ReportsDaoService,
    private val investInspectReportRepo: IMSInvestInspectReportRepository,
    private val marketSurveillanceDaoComplaintServices: MarketSurveillanceComplaintProcessDaoServices,
    private val msWorkPlanDaoService: MarketSurveillanceWorkPlanDaoServices,
    private val msFuelDaoService: MarketSurveillanceFuelDaoServices,
    private val usersSignatureRepository: UserSignatureRepository,
    private val limsServices: LimsServices,
    private val resourceLoader: ResourceLoader,
    private val companyProfileRepo: ICompanyProfileRepository
){
    private val appId: Int = applicationMapProperties.mapMarketSurveillance

    @PostMapping("/work-plan/file/save")
    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadFiles(
        @RequestParam("referenceNo") referenceNo: String,
        @RequestParam("batchReferenceNo") batchReferenceNo: String,
        @RequestParam("docTypeName") docTypeName: String,
        @RequestParam("docFile") docFile: List<MultipartFile>,
        model: Model
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var workPlanScheduled = msWorkPlanDaoService.findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = msWorkPlanDaoService.findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        var fileDocSaved: MsUploadsEntity? = null
        docFile.forEach { fileDoc ->
            with(workPlanScheduled){
                when (docTypeName) {
                    "ONSITE_FILE" -> {
                        fileDocSaved = msWorkPlanDaoService.saveOnsiteUploadFiles(fileDoc,map,loggedInUser,docTypeName,workPlanScheduled).second
                    }
                    "SCF_FILE" -> {
                        fileDocSaved = msWorkPlanDaoService.saveOnsiteUploadFiles(fileDoc,map,loggedInUser,docTypeName,workPlanScheduled).second
                        scfDocId = fileDocSaved!!.id
                        userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
                        sampleCollectionStatus = map.activeStatus
                    }
                    "SSF_FILE" -> {
                        fileDocSaved = msWorkPlanDaoService.saveOnsiteUploadFiles(fileDoc,map,loggedInUser,docTypeName,workPlanScheduled).second
                        ssfDocId = fileDocSaved!!.id
                    }
                    "SEIZURE_FILE" -> {
                        fileDocSaved = msWorkPlanDaoService.saveOnsiteUploadFiles(fileDoc,map,loggedInUser,docTypeName,workPlanScheduled).second
                        seizureDocId = fileDocSaved!!.id
                        userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
                    }
                    "DECLARATION_FILE" -> {
                        fileDocSaved = msWorkPlanDaoService.saveOnsiteUploadFiles(fileDoc,map,loggedInUser,docTypeName,workPlanScheduled).second
                        declarationDocId = fileDocSaved!!.id
                    }
                    "CHARGE_SHEET_FILE" -> {
                        fileDocSaved = msWorkPlanDaoService.saveOnsiteUploadFiles(fileDoc,map,loggedInUser,docTypeName,workPlanScheduled).second
                        chargeSheetDocId = fileDocSaved!!.id
                        userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
                        chargeSheetStatus = map.activeStatus
                    }
                    "DATA_REPORT_FILE" -> {
                        fileDocSaved = msWorkPlanDaoService.saveOnsiteUploadFiles(fileDoc,map,loggedInUser,docTypeName,workPlanScheduled).second
                        dataReportDocId = fileDocSaved!!.id
                    }
                }
            }
            workPlanScheduled = msWorkPlanDaoService.updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser).second
        }

        return msWorkPlanDaoService.workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
    }


    @PostMapping("/work-plan/additional-info-field-report/save")
    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadFilesForFieldReport(
        @RequestParam("referenceNo") referenceNo: String,
        @RequestParam("batchReferenceNo") batchReferenceNo: String,
        @RequestParam("docTypeName") docTypeName: String,
        @RequestParam("data") data: String,
        @RequestParam("docFile") docFile: List<MultipartFile>,
        model: Model
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var workPlanScheduled = msWorkPlanDaoService.findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = msWorkPlanDaoService.findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        val fieldReport =  msWorkPlanDaoService.findInspectionInvestigationByWorkPlanInspectionID(workPlanScheduled.id)?: throw ExpectedDataNotFound("Missing Filed report Not filled")
        val gson = Gson()
        val body = gson.fromJson(data, FieldReportAdditionalInfo::class.java)
        val stringData = commonDaoServices.convertClassToJson(body)
        with(fieldReport){
            additionalInformation = stringData
            additionalInformationStatus = map.activeStatus
            modifiedBy = commonDaoServices.concatenateName(loggedInUser)
            modifiedOn = commonDaoServices.getTimestamp()
        }
        investInspectReportRepo.save(fieldReport)

        var fileDocSaved: MsUploadsEntity? = null
        docFile.forEach { fileDoc ->
            with(workPlanScheduled){
                when (docTypeName) {
                    "FIELD_REPORT_UPLOAD" -> {
                        fileDocSaved = msWorkPlanDaoService.saveOnsiteUploadFiles(fileDoc,map,loggedInUser,docTypeName,workPlanScheduled).second
                        fieldReportStatus = map.activeStatus
                    }
                }
            }
            workPlanScheduled = msWorkPlanDaoService.updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser).second
        }

        return msWorkPlanDaoService.workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
    }


    @PostMapping("/work-plan/final-feed-back/save")
    @PreAuthorize("hasAuthority('MS_HOD_MODIFY') or hasAuthority('MS_RM_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadFilesFinalFeedbackReport(
        @RequestParam("referenceNo") referenceNo: String,
        @RequestParam("batchReferenceNo") batchReferenceNo: String,
        @RequestParam("docTypeName") docTypeName: String,
        @RequestParam("data") data: String,
        @RequestParam("docFile") docFile: MultipartFile,
        model: Model
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        val workPlanScheduled = msWorkPlanDaoService.findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = msWorkPlanDaoService.findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        val gson = Gson()
        val body = gson.fromJson(data, WorkPlanFeedBackDto::class.java)


        var fileDocSaved: MsUploadsEntity? = null
        when (docTypeName) {
            "COMPLAINT_FEED_BACK_UPLOAD" -> {
                fileDocSaved = msWorkPlanDaoService.saveOnsiteUploadFiles(docFile,map,loggedInUser,docTypeName,workPlanScheduled).second
            }
        }
        return msWorkPlanDaoService.addWorkPlanScheduleFeedBackByHOD(referenceNo,batchReferenceNo,body,docFile)
    }

    @PostMapping("/fuel/file/save")
    @PreAuthorize("hasAuthority('MS_IOP_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadFuelFiles(
        @RequestParam("referenceNo") referenceNo: String,
        @RequestParam("teamsReferenceNo") teamsReferenceNo: String,
        @RequestParam("countyReferenceNo") countyReferenceNo: String,
        @RequestParam("batchReferenceNo") batchReferenceNo: String,
        @RequestParam("docTypeName") docTypeName: String,
        @RequestParam("docFile") docFile: List<MultipartFile>,
        model: Model
    ): FuelInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        val batchDetail = msFuelDaoService.findFuelBatchDetailByReferenceNumber(batchReferenceNo)
        val teamsDetail = msFuelDaoService.findFuelTeamsDetailByReferenceNumber(teamsReferenceNo)
        val countyDetail = msFuelDaoService.findFuelCountyDetailByReferenceNumber(countyReferenceNo)
        var fuelInspectionDetail = msFuelDaoService.findFuelInspectionDetailByReferenceNumber(referenceNo)
//        val batchDetails = msWorkPlanDaoService.findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        var fileDocSaved: MsUploadsEntity? = null
        docFile.forEach { fileDoc ->
            with(fuelInspectionDetail){
                when (docTypeName) {
                    "FUEL_REPORT_FILE" -> {
                        fileDocSaved = msFuelDaoService.saveOnsiteUploadFiles(fileDoc,map,loggedInUser,docTypeName,fuelInspectionDetail).second
                        fuelInspectionDetail.fuelReportId = fileDocSaved!!.id
                    }
                    "SCF_FILE" -> {
                        fileDocSaved = msFuelDaoService.saveOnsiteUploadFiles(fileDoc,map,loggedInUser,docTypeName,fuelInspectionDetail).second
                        fuelInspectionDetail.scfUploadId = fileDocSaved!!.id
                    }
                    "INVOICE_DOC_FILE" -> {
                        fileDocSaved = msFuelDaoService.saveOnsiteUploadFiles(fileDoc,map,loggedInUser,docTypeName,fuelInspectionDetail).second
                        fuelInspectionDetail.invoiceDocFile = fileDocSaved!!.id
                        msFuelDaoService.postFuelInspectionDetailsSendRemediationInvoice(fuelInspectionDetail,batchDetail,teamsDetail,countyDetail,fileDoc)
                    }
                }
            }
            fuelInspectionDetail = msFuelDaoService.updateFuelInspectionDetails(fuelInspectionDetail, map, loggedInUser).second
        }

        return msFuelDaoService.fuelInspectionMappingCommonDetails(fuelInspectionDetail, map, batchDetail,teamsDetail,countyDetail)
    }

    @PostMapping("/update/destruction-notice-upload")
    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updateWorkPlanDestructionNoticeUpload(
        @RequestParam("referenceNo") referenceNo: String,
        @RequestParam("productReferenceNo") productReferenceNo: String,
        @RequestParam("batchReferenceNo") batchReferenceNo: String,
        @RequestParam("data") data: String,
        @RequestParam("docFile") docFile: MultipartFile,
        model: Model
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var workPlanScheduled = msWorkPlanDaoService.findWorkPlanActivityByReferenceNumber(referenceNo)
        var workPlanProduct = msWorkPlanDaoService.findWorkPlanProductByReferenceNumber(productReferenceNo)
        val batchDetails = msWorkPlanDaoService.findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        val gson = Gson()
        val body = gson.fromJson(data, DestructionNotificationDto::class.java)

        val fileDoc = msWorkPlanDaoService.saveOnsiteUploadFiles(docFile,map,loggedInUser,"DESTRUCTION NOTICE",workPlanScheduled)

        with(workPlanProduct){
            destructionNotificationDocId = fileDoc.second.id
            destructionClientEmail = body.clientEmail
            destructionClientFullName = body.clientFullName
            destructionNotificationDate = commonDaoServices.getCurrentDate()
            destructionNotificationStatus = map.activeStatus
//            msProcessId = applicationMapProperties.mapMSWorkPlanInspectionDestructionAddedPendingAppeal
//            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
        }

        workPlanProduct = msWorkPlanDaoService.updateWorkPlanProductDetails(workPlanProduct, map, loggedInUser).second
        val remarksDto = RemarksToAddDto()
        with(remarksDto){
            remarksDescription= body.remarks
            remarksStatus= "N/A"
            processID = workPlanScheduled.msProcessId
            userId= loggedInUser.id
        }
        val remarksSaved = msWorkPlanDaoService.workPlanAddRemarksDetails(workPlanScheduled.id,remarksDto, map, loggedInUser)
        runBlocking {
            val scheduleEmailDetails =  WorkPlanScheduledDTO()
            with(scheduleEmailDetails){
                baseUrl= applicationMapProperties.baseUrlValue
                fullName = workPlanProduct.destructionClientFullName
                refNumber = referenceNo
                batchRefNumber = batchReferenceNo
                yearCodeName = batchDetails.yearNameId?.yearName
                dateSubmitted = commonDaoServices.getCurrentDate()

            }
            workPlanProduct.destructionClientEmail?.let {
                commonDaoServices.sendEmailWithUserEmail(it,
                    applicationMapProperties.mapMsOfficerSendDestructionNotificationEmail,
                scheduleEmailDetails, map, remarksSaved.first,commonDaoServices.convertMultipartFileToFile(docFile).absolutePath)
            }
        }
        return msWorkPlanDaoService.workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
    }

    @PostMapping("/workPlan/inspection/add/seizure-declaration")
    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun addWorkPlanInspectionDetailsSeizure(
        @RequestParam("referenceNo") referenceNo: String,
        @RequestParam("batchReferenceNo") batchReferenceNo: String,
        @RequestParam("data") data: String,
        @RequestParam("docFile") docFile: MultipartFile,
        model: Model
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        val workPlanScheduled = msWorkPlanDaoService.findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = msWorkPlanDaoService.findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        val gson = Gson()
        val body = gson.fromJson(data, SeizureDto::class.java)

        val fileDoc = msWorkPlanDaoService.saveOnsiteUploadFiles(docFile,map,loggedInUser,"DESTRUCTION NOTICE",workPlanScheduled)

        with(body){
            docID = fileDoc.second.id
        }

        val productSized = msWorkPlanDaoService.workPlanInspectionDetailsAddSeizureDeclaration(body, workPlanScheduled, map, loggedInUser)
        when (productSized.first.status) {
            map.successStatus -> {
                return msWorkPlanDaoService.workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(productSized.first))
            }
        }
    }

    @PostMapping("/update/destruction-report-upload")
    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updateWorkPlanDestructionReportUpload(
        @RequestParam("referenceNo") referenceNo: String,
        @RequestParam("productReferenceNo") productReferenceNo: String,
        @RequestParam("batchReferenceNo") batchReferenceNo: String,
        @RequestParam("docFile") docFile: MultipartFile,
        model: Model
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var workPlanScheduled = msWorkPlanDaoService.findWorkPlanActivityByReferenceNumber(referenceNo)
        var workPlanProduct = msWorkPlanDaoService.findWorkPlanProductByReferenceNumber(productReferenceNo)
        val batchDetails = msWorkPlanDaoService.findCreatedWorkPlanWIthRefNumber(batchReferenceNo)

        val fileDoc = msWorkPlanDaoService.saveOnsiteUploadFiles(docFile,map,loggedInUser,"DESTRUCTION REPORT",workPlanScheduled)

        with(workPlanProduct){
            destructionDocId = fileDoc.second.id
            destructedStatus = map.activeStatus
//            msProcessId = applicationMapProperties.mapMSWorkPlanInspectionDestructionSuccessfullPendingFinalRemarks
//            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameHodRm
        }

        workPlanProduct = msWorkPlanDaoService.updateWorkPlanProductDetails(workPlanProduct, map, loggedInUser).second
        runBlocking {
            val hodRmDetails = workPlanScheduled.hodRmAssigned?.let { commonDaoServices.findUserByID(it) }
            val scheduleEmailDetails =  WorkPlanScheduledDTO()
            with(scheduleEmailDetails){
                baseUrl= applicationMapProperties.baseUrlValue
                fullName = hodRmDetails?.let { commonDaoServices.concatenateName(it) }
                refNumber = referenceNo
                batchRefNumber = batchReferenceNo
                yearCodeName = batchDetails.yearNameId?.yearName
                dateSubmitted = commonDaoServices.getCurrentDate()

            }

            hodRmDetails?.email?.let { commonDaoServices.sendEmailWithUserEmail(it, applicationMapProperties.mapMsOfficerSendDestructionNotificationHODEmail, scheduleEmailDetails, map, fileDoc.first) }
        }

        return msWorkPlanDaoService.workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
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

    @GetMapping("/view/attached")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun downloadFileDocument(
        response: HttpServletResponse,
        @RequestParam("fileID") fileID: String
    ) {
        val fileUploaded = msFuelDaoService.findUploadedFileBYId(fileID.toLong())
        val mappedFileClass = commonDaoServices.mapClass(fileUploaded)
        commonDaoServices.downloadFile(response, mappedFileClass)
    }

    @RequestMapping(value = ["/report/sample-collection"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun msSampleCollectionPDF(
        response: HttpServletResponse,
        @RequestParam(value = "sampleCollectionID") sampleCollectionID: Long
    ) {
        val map = hashMapOf<String, Any>()
        map["imagePath"] = commonDaoServices.resolveAbsoluteFilePath(applicationMapProperties.mapKebsLogoPath)

        val sampleCollection = iSampleCollectViewRepo.findBySampleCollectionId(sampleCollectionID)

        val pdfReportStream = reportsDaoService.extractReport(
            map,
            applicationMapProperties.mapMSSampleCollectionPath,
            sampleCollection
        )
        response.contentType = "text/html"
        response.contentType = "application/pdf"
        response.setHeader("Content-Length", pdfReportStream.size().toString())
        response.addHeader("Content-Dispostion", "inline; Sample-Collection-${sampleCollection[0].sampleCollectionId}.pdf;")
        response.outputStream.let { responseOutputStream ->
            responseOutputStream.write(pdfReportStream.toByteArray())
            responseOutputStream.close()
            pdfReportStream.close()
        }
    }

    @RequestMapping(value = ["/report/sample-submission"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun msSampleSubmissionPDF(
        response: HttpServletResponse,
        @RequestParam(value = "ssfID") ssfID: Long
    ) {
        val map = hashMapOf<String, Any>()
        map["imagePath"] = commonDaoServices.resolveAbsoluteFilePath(applicationMapProperties.mapKebsLogoPath)

        val ssfFile = iSampleSubmissionViewRepo.findAllById(ssfID.toString())

        val user = ssfFile[0].createdUserId?.let { commonDaoServices.findUserByID(it.toLong()) }

        if (user != null) {
            val mySignature: ByteArray?
            val image: ByteArrayInputStream?
            println("UserID is" + user.id)
            val signatureFromDb = user.id?.let { usersSignatureRepository.findByUserId(it) }
            if (signatureFromDb != null) {
                mySignature= signatureFromDb.signature
                image = ByteArrayInputStream(mySignature)
                map["signaturePath"] = image

            }
        }
//        map["recieversSignaturePath"] = commonDaoServices.resolveAbsoluteFilePath(applicationMapProperties.mapKebsTestSignaturePath)

        val pdfReportStream = reportsDaoService.extractReport(
            map,
            applicationMapProperties.mapMSSampleSubmissionPath,
            ssfFile
        )

        response.contentType = "text/html"
        response.contentType = "application/pdf"
        response.setHeader("Content-Length", pdfReportStream.size().toString())
        response.addHeader("Content-Dispostion", "inline; Sample-Submission-${ssfFile[0].sampleReferences}.pdf;")
        response.outputStream.let { responseOutputStream ->
            responseOutputStream.write(pdfReportStream.toByteArray())
            responseOutputStream.close()
            pdfReportStream.close()
        }
    }

    @RequestMapping(value = ["/report/ms-field-report"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun msFieldReportPDF(
        response: HttpServletResponse,
        @RequestParam(value = "workPlanGeneratedID") workPlanGeneratedID: String
    ) {
        val map = hashMapOf<String, Any>()
        map["imagePath"] = commonDaoServices.resolveAbsoluteFilePath(applicationMapProperties.mapKebsMSLogoPath)
        map["imageFooterPath"] = commonDaoServices.resolveAbsoluteFilePath(applicationMapProperties.mapKebsMSFooterPath)
//        map["imagePath"] = commonDaoServices.resolveAbsoluteFilePath(applicationMapProperties.mapKebsLogoPath)

        var fieldReport = iFieldReportViewRepo.findByMsWorkplanGeneratedId(workPlanGeneratedID)

        val user = fieldReport[0].createdUserId?.let { commonDaoServices.findUserByID(it.toLong()) }

        var officersList = fieldReport[0].kebsInspectors?.let { msWorkPlanDaoService.mapKEBSOfficersNameListDto(it) }
        var officersNames:String? = null
        var numberTest = 1
        officersList?.forEach { of->
            officersNames = " $numberTest. ${of.inspectorName}, ${of.designation}; "
            numberTest++
        }

        fieldReport[0].kebsInspectors = officersNames

//        if (user != null) {
//            val mySignature: ByteArray?
//            val image: ByteArrayInputStream?
//            println("UserID is" + user.id)
//            val signatureFromDb = user.id?.let { usersSignatureRepository.findByUserId(it) }
//            if (signatureFromDb != null) {
//                mySignature= signatureFromDb.signature
//                image = ByteArrayInputStream(mySignature)
//                map["signaturePath"] = image
//
//            }
//        }
//        map["recieversSignaturePath"] = commonDaoServices.resolveAbsoluteFilePath(applicationMapProperties.mapKebsTestSignaturePath)

        val pdfReportStream = reportsDaoService.extractReport(
            map,
            applicationMapProperties.mapMSFieldReportPath,
            fieldReport
        )

        response.contentType = "text/html"
        response.contentType = "application/pdf"
        response.setHeader("Content-Length", pdfReportStream.size().toString())
        response.addHeader("Content-Dispostion", "inline; Field-Report-${fieldReport[0].reportReference}.pdf;")
        response.outputStream.let { responseOutputStream ->
            responseOutputStream.write(pdfReportStream.toByteArray())
            responseOutputStream.close()
            pdfReportStream.close()
        }
    }



    @RequestMapping(value = ["/report/remediation-invoice"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun msRemediationInvoicePDF(
        response: HttpServletResponse,
        @RequestParam(value = "fuelInspectionId") fuelInspectionId: Long
    ) {
        val map = hashMapOf<String, Any>()
        map["imagePath"] = commonDaoServices.resolveAbsoluteFilePath(applicationMapProperties.mapKebsLogoPath)

        val invoiceRemediationDetails = fuelRemediationInvoiceRepo.findFirstByFuelInspectionId(fuelInspectionId)
        val fuelRemediationDetailsDto = msFuelDaoService.mapFuelRemediationDetails(invoiceRemediationDetails)
        val pdfReportStream = reportsDaoService.extractReport(
            map,
            applicationMapProperties.mapMSFuelInvoiceRemediationPath,
            fuelRemediationDetailsDto
        )
        response.contentType = "text/html"
        response.contentType = "application/pdf"
        response.setHeader("Content-Length", pdfReportStream.size().toString())
        response.addHeader("Content-Dispostion", "inline; Remediation-Invoice-${invoiceRemediationDetails[0].fuelInspectionId}.pdf;")
        response.outputStream.let { responseOutputStream ->
            responseOutputStream.write(pdfReportStream.toByteArray())
            responseOutputStream.close()
            pdfReportStream.close()
        }
    }

    fun mapSampleSubmissionDto(data: MsSampleSubmissionEntity, data2:List<SampleSubmissionItemsDto>): SampleSubmissionDtoPDF {
        return SampleSubmissionDtoPDF(
            data.id.toString(),
            data.nameProduct,
            data.packaging,
            data.labellingIdentification,
            data.fileRefNumber,
            data.referencesStandards,
            data.sizeTestSample,
            data.sizeRefSample,
            data.condition,
            data.sampleReferences,
            data.sendersName,
            data.designation,
            data.address,
            data.sendersDate.toString(),
            data.receiversName,
            data.testChargesKsh.toString(),
            data.receiptLpoNumber,
            data.invoiceNumber,
            data.disposal,
            data.remarks,
            data.sampleCollectionNumber.toString(),
            null,
            data.bsNumber,
            data2
        )
    }



}
