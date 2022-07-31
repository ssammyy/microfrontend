package org.kebs.app.kotlin.apollo.api.controllers.msControllers

import com.google.gson.Gson
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.*
import org.kebs.app.kotlin.apollo.api.ports.provided.lims.LimsServices
import org.kebs.app.kotlin.apollo.common.dto.ms.*
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.ms.MsUploadsEntity
import org.kebs.app.kotlin.apollo.store.repo.ICompanyProfileRepository
import org.kebs.app.kotlin.apollo.store.repo.ms.IFuelRemediationInvoiceRepository
import org.kebs.app.kotlin.apollo.store.repo.ms.ISampleCollectionViewRepository
import org.springframework.core.io.ResourceLoader
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletResponse


@RestController
@RequestMapping("/api/v1/migration/ms")
class MSJSONControllers(
    private val applicationMapProperties: ApplicationMapProperties,
    private val marketSurveillanceDaoServices: MarketSurveillanceFuelDaoServices,
    private val iSampleCollectViewRepo: ISampleCollectionViewRepository,
    private val fuelRemediationInvoiceRepo: IFuelRemediationInvoiceRepository,
    private val commonDaoServices: CommonDaoServices,
    private val msDaoService: MarketSurveillanceFuelDaoServices,
    private val reportsDaoService: ReportsDaoService,
    private val marketSurveillanceDaoComplaintServices: MarketSurveillanceComplaintProcessDaoServices,
    private val msWorkPlanDaoService: MarketSurveillanceWorkPlanDaoServices,
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
                    }
                    "SSF_FILE" -> {
                        fileDocSaved = msWorkPlanDaoService.saveOnsiteUploadFiles(fileDoc,map,loggedInUser,docTypeName,workPlanScheduled).second
                        ssfDocId = fileDocSaved!!.id
                    }
                    "SEIZURE_FILE" -> {
                        fileDocSaved = msWorkPlanDaoService.saveOnsiteUploadFiles(fileDoc,map,loggedInUser,docTypeName,workPlanScheduled).second
                        seizureDocId = fileDocSaved!!.id
                    }
                    "DECLARATION_FILE" -> {
                        fileDocSaved = msWorkPlanDaoService.saveOnsiteUploadFiles(fileDoc,map,loggedInUser,docTypeName,workPlanScheduled).second
                        declarationDocId = fileDocSaved!!.id
                    }
                    "CHARGE_SHEET_FILE" -> {
                        fileDocSaved = msWorkPlanDaoService.saveOnsiteUploadFiles(fileDoc,map,loggedInUser,docTypeName,workPlanScheduled).second
                        chargeSheetDocId = fileDocSaved!!.id
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

    @PostMapping("/update/destruction-notice-upload")
    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updateWorkPlanDestructionNoticeUpload(
        @RequestParam("referenceNo") referenceNo: String,
        @RequestParam("batchReferenceNo") batchReferenceNo: String,
        @RequestParam("data") data: String,
        @RequestParam("docFile") docFile: MultipartFile,
        model: Model
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var workPlanScheduled = msWorkPlanDaoService.findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = msWorkPlanDaoService.findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        val gson = Gson()
        val body = gson.fromJson(data, DestructionNotificationDto::class.java)
        val remarksDto = RemarksToAddDto()
        with(remarksDto){
            remarksDescription= body.remarks
            remarksStatus= "N/A"
            processID = workPlanScheduled.msProcessId
            userId= loggedInUser.id
        }

        val fileDoc = msWorkPlanDaoService.saveOnsiteUploadFiles(docFile,map,loggedInUser,"DESTRUCTION NOTICE",workPlanScheduled)

        with(workPlanScheduled){
            destructionNotificationDocId = fileDoc.second.id
            destructionClientEmail = body.clientEmail
            destructionNotificationDate = commonDaoServices.getCurrentDate()
            destructionNotificationStatus = map.activeStatus
            msProcessId = applicationMapProperties.mapMSWorkPlanInspectionDestructionAddedPendingAppeal
            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameIO
        }

        workPlanScheduled = msWorkPlanDaoService.updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser).second
        val remarksSaved = msWorkPlanDaoService.workPlanAddRemarksDetails(workPlanScheduled.id,remarksDto, map, loggedInUser)
        return msWorkPlanDaoService.workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
    }

    @PostMapping("/update/destruction-report-upload")
    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updateWorkPlanDestructionReportUpload(
        @RequestParam("referenceNo") referenceNo: String,
        @RequestParam("batchReferenceNo") batchReferenceNo: String,
        @RequestParam("docFile") docFile: MultipartFile,
        model: Model
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var workPlanScheduled = msWorkPlanDaoService.findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = msWorkPlanDaoService.findCreatedWorkPlanWIthRefNumber(batchReferenceNo)

        val fileDoc = msWorkPlanDaoService.saveOnsiteUploadFiles(docFile,map,loggedInUser,"DESTRUCTION REPORT",workPlanScheduled)

        with(workPlanScheduled){
            destructionDocId = fileDoc.second.id
            msProcessId = applicationMapProperties.mapMSWorkPlanInspectionDestructionSuccessfullPendingFinalRemarks
            userTaskId = applicationMapProperties.mapMSCPWorkPlanUserTaskNameHodRm
        }

        workPlanScheduled = msWorkPlanDaoService.updateWorkPlanInspectionDetails(workPlanScheduled, map, loggedInUser).second

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
        val fileUploaded = marketSurveillanceDaoServices.findUploadedFileBYId(fileID.toLong())
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





//    @GetMapping("populate-all-labs",  produces = [MediaType.APPLICATION_JSON_VALUE])
//    fun populateBroadCategory(): String? {
//        val gson = Gson()
//        return gson.toJson(iLaboratoryRepo.findAll())
//    }

//    @GetMapping("email-check/{email}",  produces = [MediaType.APPLICATION_JSON_VALUE])
//    fun myEmail(
//            @PathVariable(value = "email", required = false) email: String
//    ): String? {
//        var result = "notExist"
//        usersRepo.findByEmail(email)
//                .let {usersEntity ->
//                    if (usersEntity?.email == email){
//                        result = email
//                    }else{
//                        result
//                    }
//                }
//        return result
//    }


}
