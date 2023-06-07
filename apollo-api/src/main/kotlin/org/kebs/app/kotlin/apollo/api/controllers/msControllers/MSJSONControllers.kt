package org.kebs.app.kotlin.apollo.api.controllers.msControllers

import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.*
import org.kebs.app.kotlin.apollo.api.ports.provided.emailDTO.WorkPlanScheduledDTO
import org.kebs.app.kotlin.apollo.api.ports.provided.lims.LimsServices
import org.kebs.app.kotlin.apollo.common.dto.ms.*
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.MsDataReportEntity
import org.kebs.app.kotlin.apollo.store.model.MsInspectionInvestigationReportEntity
import org.kebs.app.kotlin.apollo.store.model.MsSampleSubmissionEntity
import org.kebs.app.kotlin.apollo.store.model.MsSeizureDeclarationEntity
import org.kebs.app.kotlin.apollo.store.model.ms.MsUploadsEntity
import org.kebs.app.kotlin.apollo.store.repo.ICompanyProfileRepository
import org.kebs.app.kotlin.apollo.store.repo.UserSignatureRepository
import org.kebs.app.kotlin.apollo.store.repo.ms.*
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
import javax.servlet.http.HttpServletResponse


@RestController
@RequestMapping("/api/v1/migration/ms")
class MSJSONControllers(
    private val applicationMapProperties: ApplicationMapProperties,
    private val iSampleCollectViewRepo: ISampleCollectionViewRepository,
    private val iSampleSubmissionViewRepo: IMsSampleSubmissionViewRepository,
    private val iComplaintPdfViewRepo: IMsComplaintPdfGenerationViewRepository,
    private val iFieldReportViewRepo: IMsFieldReportViewRepository,
    private val dataReportRepo: IDataReportRepository,
    private val iSeizedGoodsReportViewRepo: IMsSeizedGoodsReportViewRepository,
    private val iOutletVisitedAndSummaryOfFindingsViewRepo: IOutletVisitedAndSummaryOfFindingsViewRepository,
    private val iSummaryOfSamplesDrawnViewRepo: ISummaryOfSamplesDrawnViewRepository,
    private val sampleSubmitRepo: IMSSampleSubmissionRepository,
    private val fuelRemediationInvoiceRepo: IFuelRemediationInvoiceRepository,
    private val commonDaoServices: CommonDaoServices,
    private val reportsDaoService: ReportsDaoService,
    private val investInspectReportRepo: IMSInvestInspectReportRepository,
    private val msUploadRepo: IMsUploadsRepository,
    private val marketSurveillanceDaoComplaintServices: MarketSurveillanceComplaintProcessDaoServices,
    private val msWorkPlanDaoService: MarketSurveillanceWorkPlanDaoServices,
    private val seizureDeclarationRepo: IMsSeizureRepository,
    private val dataReportParameterRepo: IDataReportParameterRepository,
    private val ssfLabParametersRepo: ISSFLabParameterRepository,
    private val msFuelDaoService: MarketSurveillanceFuelDaoServices,
    private val usersSignatureRepository: UserSignatureRepository,
    private val limsServices: LimsServices,
    private val resourceLoader: ResourceLoader,
    private val companyProfileRepo: ICompanyProfileRepository,
    private val msFuelDaoServices: MarketSurveillanceFuelDaoServices,
    private val sampleSubmitParameterRepo: ISampleSubmitParameterRepository,
){
    private val appId: Int = applicationMapProperties.mapMarketSurveillance

    @PostMapping("/work-plan/file/save")
    @PreAuthorize("hasAuthority('MS_IO_MODIFY') or hasAuthority('MS_HOD_MODIFY') or hasAuthority('MS_RM_MODIFY')")
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
                    "CLIENT_APPEAL_DOCUMENT" -> {
                        fileDocSaved = msWorkPlanDaoService.saveOnsiteUploadFiles(fileDoc,map,loggedInUser,docTypeName,workPlanScheduled).second
                        clientAppealDocID = fileDocSaved!!.id
                    }
                    "CLIENT_DID_NOT_APPEAL_DOCUMENT" -> {
                        fileDocSaved = msWorkPlanDaoService.saveOnsiteUploadFiles(fileDoc,map,loggedInUser,docTypeName,workPlanScheduled).second
                        clientAppealDocID = fileDocSaved!!.id
                    }
                    "SUCCESSFUL/UNSUCCESSFUL_APPEAL_DOCUMENT" -> {
                        fileDocSaved = msWorkPlanDaoService.saveOnsiteUploadFiles(fileDoc,map,loggedInUser,docTypeName,workPlanScheduled).second
                        successfulOrUnsuccessfulAppealDocID = fileDocSaved!!.id
                    }
                    "CHAIN_OF_CUSTODY" -> {
                        fileDocSaved = msWorkPlanDaoService.saveOnsiteUploadFiles(fileDoc,map,loggedInUser,docTypeName,workPlanScheduled).second
                        chainOfCustodyDocID = fileDocSaved!!.id
                    }
                    "FOLLOW_UP_ACTION_DOC" -> {
                        fileDocSaved = msWorkPlanDaoService.saveOnsiteUploadFiles(fileDoc,map,loggedInUser,docTypeName,workPlanScheduled).second
                        followUpActionDocID = fileDocSaved!!.id
                    }
                    "WORKPLAN_END_FILE" -> {
                        fileDocSaved = msWorkPlanDaoService.saveOnsiteUploadFiles(fileDoc,map,loggedInUser,docTypeName,workPlanScheduled).second
                        workplanEndFileDocID = fileDocSaved!!.id
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
        val fieldReport =  msWorkPlanDaoService.findInspectionInvestigationByWorkPlanInspectionID(workPlanScheduled.id, map.inactiveStatus)?: throw ExpectedDataNotFound("Missing Filed report Not filled")
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
                productName = workPlanProduct.productName
                productBrand = workPlanProduct.productBrand
            }
            workPlanProduct.destructionClientEmail?.let {
                commonDaoServices.sendEmailWithUserEmail(it,
                    applicationMapProperties.mapMsOfficerSendDestructionNotificationEmail,
                scheduleEmailDetails, map, remarksSaved.first, commonDaoServices.convertMultipartFileToFile(docFile)?.absolutePath
                )
            }
        }
        return msWorkPlanDaoService.workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
    }

    @PostMapping("/update/upload-final-report")
    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updateWorkPlanUploadFinalReport(
        @RequestParam("referenceNo") referenceNo: String,
        @RequestParam("batchReferenceNo") batchReferenceNo: String,
        @RequestParam("docFile") docFile: MultipartFile,
        model: Model
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var workPlanScheduled = msWorkPlanDaoService.findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = msWorkPlanDaoService.findCreatedWorkPlanWIthRefNumber(batchReferenceNo)

        var versionNumber =1L
        val uploadFound = msUploadRepo.findTopByMsWorkplanGeneratedIdAndWorkPlanUploadsAndIsUploadFinalReportOrderByIdDesc(workPlanScheduled.id,1,1)

        if (uploadFound!=null){
            versionNumber= uploadFound.versionNumber?.plus(1L)!!
        }

        val fileDoc = msWorkPlanDaoService.saveOnsiteUploadFiles(docFile,map,loggedInUser,"FINAL_REPORT",workPlanScheduled, versionNumber, 1)

        return msWorkPlanDaoService.updateWorkPlanScheduleInspectionDetailsFinalPreliminaryReport(referenceNo,batchReferenceNo,fileDoc.second.id?:throw ExpectedDataNotFound("MISSING DOC ID"),true)
    }

    @PostMapping("/update/upload-final-report-hod-hof-director")
    @PreAuthorize("hasAuthority('MS_HOD_MODIFY') or hasAuthority('MS_RM_MODIFY') or hasAuthority('MS_HOF_MODIFY') or hasAuthority('MS_DIRECTOR_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updateWorkPlanUploadFinalReportHODHOF(
        @RequestParam("referenceNo") referenceNo: String,
        @RequestParam("batchReferenceNo") batchReferenceNo: String,
        @RequestParam("docFile") docFile: MultipartFile,
        model: Model
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var workPlanScheduled = msWorkPlanDaoService.findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = msWorkPlanDaoService.findCreatedWorkPlanWIthRefNumber(batchReferenceNo)

        var versionNumber =1L
        val uploadFound = msUploadRepo.findTopByMsWorkplanGeneratedIdAndWorkPlanUploadsAndIsUploadFinalReportOrderByIdDesc(workPlanScheduled.id,1,1)

        if (uploadFound!=null){
            versionNumber= uploadFound.versionNumber?.plus(1L)!!
        }

        val fileDoc = msWorkPlanDaoService.saveOnsiteUploadFiles(docFile,map,loggedInUser,"FINAL_REPORT",workPlanScheduled, versionNumber, 1)

        return msWorkPlanDaoService.updateWorkPlanScheduleInspectionDetailsFinalPreliminaryReport(referenceNo,batchReferenceNo,fileDoc.second.id?:throw ExpectedDataNotFound("MISSING DOC ID"),false)
    }

    @PostMapping("/workPlan/inspection/add/seizure-declaration")
    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun addWorkPlanInspectionDetailsSeizure(
        @RequestParam("referenceNo") referenceNo: String,
        @RequestParam("batchReferenceNo") batchReferenceNo: String,
        @RequestParam("data") data: String,
        @RequestParam("docFile") docFile: MultipartFile?,
        model: Model
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        val workPlanScheduled = msWorkPlanDaoService.findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = msWorkPlanDaoService.findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        val gson = Gson()
        val body = gson.fromJson(data, SeizureListDto::class.java)

        if (docFile!=null) {
            body.docID?.let { msUploadRepo.deleteById(it) }
        }

        val fileDoc = docFile?.let { msWorkPlanDaoService.saveOnsiteUploadFiles(it,map,loggedInUser,"SEIZURE AND DECLARATION",workPlanScheduled) }

        with(body){
            docID = fileDoc?.second?.id
        }

        val mainSized = msWorkPlanDaoService.workPlanInspectionDetailsAddMainSeizure(body, workPlanScheduled, map, loggedInUser)
        when (mainSized.first.status) {
            map.successStatus -> {
                val paramList = msWorkPlanDaoService.findSeizureDeclarationByWorkPlanInspectionID(workPlanScheduled.id,mainSized.second.id)
                paramList?.forEach { paramRemove->
                    val result: SeizureDto? = body.seizureList?.find { actor -> actor.id==paramRemove.id }
                    if (result == null) {
                        seizureDeclarationRepo.deleteById(paramRemove.id)
                    }
                }

                body.seizureList?.forEach { body2->
                    body2.mainSeizureID = mainSized.second.id
                    msWorkPlanDaoService.workPlanInspectionDetailsAddSeizureDeclaration(body2, workPlanScheduled, map, loggedInUser)
                }?:throw ExpectedDataNotFound("MISSING PRODUCTS TO SEIZURE")
                return msWorkPlanDaoService.workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(mainSized.first))
            }
        }

    }

    @PostMapping("/workPlan/inspection/add/data-report")
    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun addWorkPlanInspectionDataReport(
        @RequestParam("referenceNo") referenceNo: String,
        @RequestParam("batchReferenceNo") batchReferenceNo: String,
        @RequestParam("data") data: String,
        @RequestParam("docFile") docFile: List<MultipartFile>?,
        model: Model
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        val workPlanScheduled = msWorkPlanDaoService.findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = msWorkPlanDaoService.findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        val gson = Gson()
        val body = gson.fromJson(data, DataReportDto::class.java)

        if (docFile!=null) {
            body.docList?.forEach { fileDel->
                msUploadRepo.deleteById(fileDel)
            }
        }

        val fileDocList = mutableListOf<Long>()
        docFile?.forEach { fileDoc ->
            val fileDocSaved =   msWorkPlanDaoService.saveOnsiteUploadFiles(fileDoc,map,loggedInUser,"DATA REPORT",workPlanScheduled)
            fileDocSaved.second.id?.let { fileDocList.add(it) }
        }


        with(body){
            docList = fileDocList
        }

        val dataReportFileSaved = msWorkPlanDaoService.workPlanInspectionDetailsAddDataReport(body, workPlanScheduled, map, loggedInUser)

        when (dataReportFileSaved.first.status) {
            map.successStatus -> {
                val dataReportParamList = dataReportFileSaved.second.id.let { msWorkPlanDaoService.findDataReportParamsByDataReportID(it) }
                dataReportParamList?.forEach { paramRemove ->
                    val result: DataReportParamsDto? = body.productsList?.find { actor -> actor.id == paramRemove.id }
                    if (result == null) {
                        dataReportParameterRepo.deleteById(paramRemove.id)
                    }
                }

                body.productsList?.forEach { param ->
                    msWorkPlanDaoService.workPlanInspectionDetailsAddDataReportParams(param, dataReportFileSaved.second, map, loggedInUser)
                }
                return msWorkPlanDaoService.workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(dataReportFileSaved.first))
            }
        }

    }

    @PostMapping("/workPlan/inspection/add/sample-submission")
    @PreAuthorize("hasAuthority('MS_IO_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun addWorkPlanInspectionSSF(
        @RequestParam("referenceNo") referenceNo: String,
        @RequestParam("batchReferenceNo") batchReferenceNo: String,
        @RequestParam("data") data: String,
        @RequestParam("docFile") docFile: List<MultipartFile>?,
        model: Model
    ): WorkPlanInspectionDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        val workPlanScheduled = msWorkPlanDaoService.findWorkPlanActivityByReferenceNumber(referenceNo)
        val batchDetails = msWorkPlanDaoService.findCreatedWorkPlanWIthRefNumber(batchReferenceNo)
        val gson = Gson()
        val body = gson.fromJson(data, SampleSubmissionDto::class.java)

        if (docFile!=null) {
            body.docList?.forEach { fileDel->
                msUploadRepo.deleteById(fileDel)
            }
        }

        val fileDocList = mutableListOf<Long>()
        docFile?.forEach { fileDoc ->
            val fileDocSaved =   msWorkPlanDaoService.saveOnsiteUploadFiles(fileDoc,map,loggedInUser,"SAMPLE SUBMISSION FORM",workPlanScheduled)
            fileDocSaved.second.id?.let { fileDocList.add(it) }
        }


        with(body){
            docList = fileDocList
        }

        val SSFFileSaved = msWorkPlanDaoService.workPlanInspectionDetailsAddSSF(body, workPlanScheduled, map, loggedInUser)

        when (SSFFileSaved.first.status) {
            map.successStatus -> {
                val SSFLabParamList = SSFFileSaved.second.id.let { msFuelDaoServices.findAllSampleSubmissionParametersBasedOnSampleSubmissionID(it) }
                SSFLabParamList?.forEach { paramRemove ->
                    val result: SampleSubmissionItemsDto? = body.parametersList?.find { actor -> actor.id == paramRemove.id }
                    if (result == null) {
                        sampleSubmitParameterRepo.deleteById(paramRemove.id)
                    }
                }

                body.parametersList?.forEach { param ->
                    msFuelDaoServices.addSampleSubmissionParamAdd(param, SSFFileSaved.second, map, loggedInUser)
                }

                return msWorkPlanDaoService.workPlanInspectionMappingCommonDetails(workPlanScheduled, map, batchDetails)
            }
            else -> {
                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(SSFFileSaved.first))
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
        map["imagePath"] = commonDaoServices.resolveAbsoluteFilePath(applicationMapProperties.mapKebsMSLogoPath)

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

    @RequestMapping(value = ["/report/complaint"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun msComplaintPDF(
        response: HttpServletResponse,
        @RequestParam(value = "refNumber") refNumber: String
    ) {
        val map = hashMapOf<String, Any>()
        map["imagePath"] = commonDaoServices.resolveAbsoluteFilePath(applicationMapProperties.mapKebsMSLogoPath)
        map["imageFooterPath"] = commonDaoServices.resolveAbsoluteFilePath(applicationMapProperties.mapKebsMSFooterPath)

        val complaintFile = iComplaintPdfViewRepo.findAllByReferenceNumber(refNumber)

//        val user = ssfFile[0].createdUserId?.let { commonDaoServices.findUserByID(it.toLong()) }

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
            applicationMapProperties.mapMSComplaintPath,
            complaintFile
        )

        response.contentType = "text/html"
        response.contentType = "application/pdf"
        response.setHeader("Content-Length", pdfReportStream.size().toString())
        response.addHeader("Content-Dispostion", "inline; Complaint-${complaintFile[0].referenceNumber}.pdf;")
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
        val mapValue = commonDaoServices.serviceMapDetails(appId)
        val map = hashMapOf<String, Any>()
        map["imagePath"] = commonDaoServices.resolveAbsoluteFilePath(applicationMapProperties.mapKebsMSLogoPath)
        map["imageFooterPath"] = commonDaoServices.resolveAbsoluteFilePath(applicationMapProperties.mapKebsMSFooterPath)
//        map["imagePath"] = commonDaoServices.resolveAbsoluteFilePath(applicationMapProperties.mapKebsLogoPath)

        val inspectionInvestigation =investInspectReportRepo.findByIsPreliminaryReportAndWorkPlanGeneratedID( mapValue.inactiveStatus, workPlanGeneratedID.toLong(),)
        val fieldReport = mutableListOf<InspectionInvestigationReportJSPDto>()
        val fieldReportList = inspectionInvestigation?.let { mapInspectionInvestigationDetailsDto(it) }
        if (fieldReportList != null) {
            fieldReport.add(fieldReportList)
        }

//        val fieldReport = iFieldReportViewRepo.findMsWorkplanGeneratedId(workPlanGeneratedID)

        val dataReportDtoList = mutableListOf<DataReportJSPDto>()
        msWorkPlanDaoService.findDataReportListByWorkPlanInspectionID(workPlanGeneratedID.toLong())
            ?.forEach { dataReport ->
                val dataReportParameters = dataReport.id.let { msWorkPlanDaoService.findDataReportParamsByDataReportID(it) }
                val dataReportParametersDto = dataReportParameters?.let { msWorkPlanDaoService.mapDataReportParamListDto(it) }
                val dataReportDto = dataReport.let { dataReportParametersDto?.let { it1 -> mapDataReportDetailsDto(it, it1) } }
                if (dataReportDto != null) {
                    dataReportDtoList.add(dataReportDto)
                }
            }

        val outletsVisitedSummaryfindings = JRBeanCollectionDataSource(dataReportDtoList)
        map["OutletsVisitedSummaryfindingsParam"] =outletsVisitedSummaryfindings

        val sampleSubmittedDtoList = mutableListOf<SampleSubmissionDtoPDF>()
        msWorkPlanDaoService.findSampleSubmissionDetailByWorkPlanGeneratedID(workPlanGeneratedID.toLong())
            ?.forEach { sampleSubmitted ->
                val sampleSubmittedParamList = sampleSubmitted.id.let {
                    msFuelDaoServices.findAllSampleSubmissionParametersBasedOnSampleSubmissionID(it)
                }
                val sampleSubmittedDtoValues = sampleSubmittedParamList?.let { msFuelDaoServices.mapSampleSubmissionParamListDto(it) }
                    ?.let { mapSampleSubmissionDto(sampleSubmitted, it) }
                if (sampleSubmittedDtoValues != null) {
                    sampleSubmittedDtoList.add(sampleSubmittedDtoValues)
                }
            }

        val summarySamplesDrawnParam = JRBeanCollectionDataSource(sampleSubmittedDtoList)
        map["SummarySamplesDrawnParam"] =summarySamplesDrawnParam

        val seizureDtoList = mutableListOf<SeizureDto>()
        msWorkPlanDaoService.findSeizureByWorkPlanInspectionID(workPlanGeneratedID.toLong())
            ?.forEach { seizure ->
                val seizureDeclarationList = msWorkPlanDaoService.findSeizureDeclarationByWorkPlanInspectionID(workPlanGeneratedID.toLong(), seizure.id)
                val seizureDeclarationDtoList = seizureDeclarationList?.let { msWorkPlanDaoService.mapSeizureDeclarationDetailsDto(it) }
                if (seizureDeclarationDtoList != null) {
                    seizureDtoList.addAll(seizureDeclarationDtoList)
                }
//                val seizureDto = seizureDeclarationDtoList?.let { mapSeizureDetailsDto(seizure, it) }
//                if (seizureDto != null) {
//                    seizureDtoList.add(seizureDto)
//                }
            }

        val summarySiezedGoods = JRBeanCollectionDataSource(seizureDtoList)
        map["SummarySiezedGoods"] =summarySiezedGoods

        val user = fieldReport[0].createdUserId?.let { commonDaoServices.findUserByID(it.toLong()) }

        val officersList = fieldReport[0].kebsInspectors?.let { msWorkPlanDaoService.mapKEBSOfficersNameListDto(it) }
        var officersNames:String? = null
        var numberTest = 1
        officersList?.forEach { of->
            officersNames = " $numberTest. ${of.inspectorName}, ${of.designation}; "
            numberTest++
        }

        map["officersList"] =JRBeanCollectionDataSource(officersList)

        fieldReport[0].kebsInspectors = officersNames
        fieldReport[0].reportClassification?.uppercase()

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
        var pathFileToSelect = applicationMapProperties.mapMSFieldReportPathTopSecret
        when (fieldReport[0].reportClassification) {
            "TOP SECRET" -> {
                pathFileToSelect = applicationMapProperties.mapMSFieldReportPathTopSecret
            }
            "SECRET" -> {
                pathFileToSelect = applicationMapProperties.mapMSFieldReportPathSecret
            }
            "CONFIDENTIAL" -> {
                pathFileToSelect = applicationMapProperties.mapMSFieldReportPathConfidential
            }
            "RESTRICTED" -> {
                pathFileToSelect = applicationMapProperties.mapMSFieldReportPathRestricted
            }
        }

        val pdfReportStream = reportsDaoService.extractReport(
            map,
            pathFileToSelect,
            fieldReport
        )

        response.contentType = "text/html"
        response.contentType = "application/pdf"
        response.setHeader("Content-Length", pdfReportStream.size().toString())
        response.addHeader("Content-Dispostion", "inline; Initial-Report-${fieldReport[0].reportReference}.pdf;")
        response.outputStream.let { responseOutputStream ->
            responseOutputStream.write(pdfReportStream.toByteArray())
            responseOutputStream.close()
            pdfReportStream.close()
        }
    }

    @RequestMapping(value = ["/report/ms-progress-report"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun msProgressReportPDF(
        response: HttpServletResponse,
        @RequestParam(value = "workPlanGeneratedID") workPlanGeneratedID: String
    ) {
        val mapValue = commonDaoServices.serviceMapDetails(appId)
        val map = hashMapOf<String, Any>()
        map["imagePath"] = commonDaoServices.resolveAbsoluteFilePath(applicationMapProperties.mapKebsMSLogoPath)
        map["imageFooterPath"] = commonDaoServices.resolveAbsoluteFilePath(applicationMapProperties.mapKebsMSFooterPath)
//        map["imagePath"] = commonDaoServices.resolveAbsoluteFilePath(applicationMapProperties.mapKebsLogoPath)

        val inspectionInvestigation =investInspectReportRepo.findByIsPreliminaryReportAndWorkPlanGeneratedID( mapValue.inactiveStatus, workPlanGeneratedID.toLong(),)
        val progressReport = mutableListOf<InspectionInvestigationReportJSPDto>()
        val fieldReportList = inspectionInvestigation?.let { mapInspectionInvestigationDetailsDto(it) }
        if (fieldReportList != null) {
            progressReport.add(fieldReportList)
        }

//        val fieldReport = iFieldReportViewRepo.findMsWorkplanGeneratedId(workPlanGeneratedID)

        val dataReportDtoList = mutableListOf<DataReportJSPDto>()
        msWorkPlanDaoService.findDataReportListByWorkPlanInspectionID(workPlanGeneratedID.toLong())
            ?.forEach { dataReport ->
                val dataReportParameters = dataReport.id.let { msWorkPlanDaoService.findDataReportParamsByDataReportID(it) }
                val dataReportParametersDto = dataReportParameters?.let { msWorkPlanDaoService.mapDataReportParamListDto(it) }
                val dataReportDto = dataReport.let { dataReportParametersDto?.let { it1 -> mapDataReportDetailsDto(it, it1) } }
                if (dataReportDto != null) {
                    dataReportDtoList.add(dataReportDto)
                }
            }

        val outletsVisitedSummaryfindings = JRBeanCollectionDataSource(dataReportDtoList)
        map["OutletsVisitedSummaryfindingsParam"] =outletsVisitedSummaryfindings

        val sampleSubmittedDtoList = mutableListOf<SampleSubmissionDtoPDF>()
        msWorkPlanDaoService.findSampleSubmissionDetailByWorkPlanGeneratedID(workPlanGeneratedID.toLong())
            ?.forEach { sampleSubmitted ->
                val sampleSubmittedParamList = sampleSubmitted.id.let {
                    msFuelDaoServices.findAllSampleSubmissionParametersBasedOnSampleSubmissionID(it)
                }
                val sampleSubmittedDtoValues = sampleSubmittedParamList?.let { msFuelDaoServices.mapSampleSubmissionParamListDto(it) }
                    ?.let { mapSampleSubmissionDto(sampleSubmitted, it) }
                if (sampleSubmittedDtoValues != null) {
                    sampleSubmittedDtoList.add(sampleSubmittedDtoValues)
                }
            }

        val summarySamplesDrawnParam = JRBeanCollectionDataSource(sampleSubmittedDtoList)
        map["SummarySamplesDrawnParam"] =summarySamplesDrawnParam

        val seizureDtoList = mutableListOf<SeizureDto>()
        msWorkPlanDaoService.findSeizureByWorkPlanInspectionID(workPlanGeneratedID.toLong())
            ?.forEach { seizure ->
                val seizureDeclarationList = msWorkPlanDaoService.findSeizureDeclarationByWorkPlanInspectionID(workPlanGeneratedID.toLong(), seizure.id)
                val seizureDeclarationDtoList = seizureDeclarationList?.let { msWorkPlanDaoService.mapSeizureDeclarationDetailsDto(it) }
                if (seizureDeclarationDtoList != null) {
                    seizureDtoList.addAll(seizureDeclarationDtoList)
                }
//                val seizureDto = seizureDeclarationDtoList?.let { mapSeizureDetailsDto(seizure, it) }
//                if (seizureDto != null) {
//                    seizureDtoList.add(seizureDto)
//                }
            }

        val summarySiezedGoods = JRBeanCollectionDataSource(seizureDtoList)
        map["SummarySiezedGoods"] =summarySiezedGoods

        val user = progressReport[0].createdUserId?.let { commonDaoServices.findUserByID(it.toLong()) }

        var officersList = progressReport[0].kebsInspectors?.let { msWorkPlanDaoService.mapKEBSOfficersNameListDto(it) }
        var officersNames:String? = null
        var numberTest = 1
        officersList?.forEach { of->
            officersNames = " $numberTest. ${of.inspectorName}, ${of.designation}; "
            numberTest++
        }

        map["officersList"] =JRBeanCollectionDataSource(officersList)

        progressReport[0].kebsInspectors = officersNames
        progressReport[0].reportClassification?.uppercase()

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
        var pathFileToSelect = applicationMapProperties.mapMSFieldReportPathTopSecret
        when (progressReport[0].reportClassification) {
            "TOP SECRET" -> {
                pathFileToSelect = applicationMapProperties.mapMSFieldReportPathTopSecret
            }
            "SECRET" -> {
                pathFileToSelect = applicationMapProperties.mapMSFieldReportPathSecret
            }
            "CONFIDENTIAL" -> {
                pathFileToSelect = applicationMapProperties.mapMSFieldReportPathConfidential
            }
            "RESTRICTED" -> {
                pathFileToSelect = applicationMapProperties.mapMSFieldReportPathRestricted
            }
        }

        val pdfReportStream = reportsDaoService.extractReport(
            map,
            pathFileToSelect,
            progressReport
        )

        response.contentType = "text/html"
        response.contentType = "application/pdf"
        response.setHeader("Content-Length", pdfReportStream.size().toString())
        response.addHeader("Content-Dispostion", "inline; Progress-Report-${progressReport[0].reportReference}.pdf;")
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
            data.sendersDate?.let { commonDaoServices.convertDateToKraSqlDate(it) },
            data.receiversName,
            data.testChargesKsh.toString(),
            data.receiptLpoNumber,
            data.invoiceNumber,
            data.disposal,
            data.remarks,
            data.countryOfOrigin,
            data.sampleCollectionNumber.toString(),
            null,
            data.bsNumber,
            dataReportRepo.findByIdOrNull(data.dataReportID?: -1L)?.outletName,
            data.lbIdTradeMark,
            data.lbIdExpiryDate?.let { commonDaoServices.convertDateToKraSqlDate(it) },
            data.sampleCollectionDate?.let { commonDaoServices.convertDateToKraSqlDate(it) },
            data.lbIdBatchNo,
            data2
        )
    }


    fun mapSeizureDetailsDto(
        data: MsSeizureDeclarationEntity,
        data2: List<SeizureDto>
    ): SeizureListJSPDto {
        return SeizureListJSPDto(
            data.id.toString(),
            data.docId.toString(),
            data.marketTownCenter,
            data.productField,
            data.serialNumber,
            data.nameOfOutlet,
            data.nameSeizingOfficer,
            data.additionalOutletDetails,
            data2
        )

    }

    fun mapDataReportDetailsDto(
        dataReport: MsDataReportEntity,
        dataReportParam: List<DataReportParamsDto>
    ): DataReportJSPDto {
        return DataReportJSPDto(
            dataReport.id.toString(),
            dataReport.referenceNumber,
            dataReport.inspectionDate?.let { commonDaoServices.convertDateToKraSqlDate(it) },
            dataReport.inspectorName,
            dataReport.function,
            dataReport.department,
            dataReport.regionName,
            dataReport.town,
            dataReport.marketCenter,
            dataReport.outletDetails,
            dataReport.physicalLocation,
            dataReport.outletName,
            dataReport.phoneNumber,
            dataReport.emailAddress,
            dataReport.mostRecurringNonCompliant,
            dataReport.additionalNonComplianceDetails,
            dataReport.personMet,
            dataReport.summaryFindingsActionsTaken,
            dataReport.samplesDrawnAndSubmitted,
            dataReport.sourceOfProductAndEvidence,
            dataReport.finalActionSeizedGoods,
            dataReport.totalComplianceScore,
            dataReport.numberOfProducts,
            dataReport.remarks,
            dataReportParam,
            dataReport.docList?.let { msWorkPlanDaoService.mapUploadListListDto(it) }
        )
    }


    fun mapInspectionInvestigationDetailsDto(
        inspectionInvestigation: List<MsInspectionInvestigationReportEntity>
    ): InspectionInvestigationReportJSPDto {
        return InspectionInvestigationReportJSPDto(
          inspectionInvestigation[0].workPlanGeneratedID.toString(),
          inspectionInvestigation[0].createdUserId.toString(),
          inspectionInvestigation[0].id.toString(),
          inspectionInvestigation[0].reportReference,
          inspectionInvestigation[0].reportTo,
          inspectionInvestigation[0].reportThrough,
          inspectionInvestigation[0].reportFrom,
          inspectionInvestigation[0].reportSubject,
          inspectionInvestigation[0].reportTitle,
          inspectionInvestigation[0].reportDate?.let { commonDaoServices.convertDateToKraSqlDate(it) },
          inspectionInvestigation[0].reportRegion,
          inspectionInvestigation[0].reportDepartment,
          inspectionInvestigation[0].reportFunction,
          inspectionInvestigation[0].backgroundInformation,
          inspectionInvestigation[0].objectiveInvestigation,
          inspectionInvestigation[0].startDateInvestigationInspection?.let { commonDaoServices.convertDateToKraSqlDate(it) },
          inspectionInvestigation[0].endDateInvestigationInspection?.let { commonDaoServices.convertDateToKraSqlDate(it) },
          inspectionInvestigation[0].methodologyEmployed,
          inspectionInvestigation[0].summaryOfFindings,
          inspectionInvestigation[0].additionalInformation,
          inspectionInvestigation[0].conclusion,
          inspectionInvestigation[0].recommendations,
          inspectionInvestigation[0].statusActivity,
          inspectionInvestigation[0].finalRemarkHod,
          inspectionInvestigation[0].kebsInspectors,
          inspectionInvestigation[0].varField1,
          inspectionInvestigation[0].createdOn?.let { commonDaoServices.convertDateToKraSqlTimeStamp(it) },
          inspectionInvestigation[0].createdBy,
          inspectionInvestigation[0].modifiedBy,
          inspectionInvestigation[0].modifiedOn?.let { commonDaoServices.convertDateToKraSqlTimeStamp(it) },
          inspectionInvestigation[0].reportClassification,
          inspectionInvestigation[0].changesMade,
        )
    }


}
