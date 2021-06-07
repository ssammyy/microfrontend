package org.kebs.app.kotlin.apollo.api.controllers.diControllers

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.DestinationInspectionBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.*
import org.kebs.app.kotlin.apollo.api.ports.provided.emailDTO.MvInspectionNotificationDTO
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.CdSampleCollectionEntity
import org.kebs.app.kotlin.apollo.store.model.CdSampleSubmissionItemsEntity
import org.kebs.app.kotlin.apollo.store.model.ServiceRequestsEntity
import org.kebs.app.kotlin.apollo.store.model.di.*
import org.kebs.app.kotlin.apollo.store.model.qa.QaSampleSubmissionEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.support.SessionStatus
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.io.BufferedInputStream
import java.io.ByteArrayInputStream
import java.io.File
import java.io.InputStream
import java.lang.Exception
import java.net.URLConnection
import javax.servlet.http.HttpServletResponse


@Controller
@RequestMapping("/api/di/")
class DestinationInspectionController(
    private val applicationMapProperties: ApplicationMapProperties,
    private val daoServices: DestinationInspectionDaoServices,
    private val invoiceDaoService: InvoiceDaoService,
    private val riskProfileDaoService: RiskProfileDaoService,
    private val commonDaoServices: CommonDaoServices,
    private val diBpmn: DestinationInspectionBpmn,
    private val reportsDaoService: ReportsDaoService
) {

    final val appId = applicationMapProperties.mapImportInspection

    //    private val motorVehicleInspectionDetailsPage = "redirect:/api/di/mv-inspection-checklist-detail?cdItemUuid"
//    private val motorVehicleInspectionDetailsPage = "redirect:/api/di/inspection/motor-vehicle-inspection-details?itemId"
    private val motorVehicleInspectionDetailsPage = "redirect:/api/di/motor-vehicle-inspection-details?itemId"
    private val cdItemViewPageDetails = "redirect:/api/di/cd-item-details?cdItemUuid"


    @PreAuthorize("hasAuthority('DI_OFFICER_CHARGE_MODIFY') or hasAuthority('DI_INSPECTION_OFFICER_MODIFY')")
    @PostMapping("cd-save/details")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun cdDetailsSave(
        @ModelAttribute cdDetails: ConsignmentDocumentDetailsEntity,
        @RequestParam("cdUuid") cdUuid: String,
        model: Model,
        result: BindingResult,
        redirectAttributes: RedirectAttributes
    ): String {
        val processStages = commonDaoServices.findProcesses(appId)
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        daoServices.findCDWithUuid(cdUuid)
            .let { consignmentDocument ->

                val cdID: Long = consignmentDocument.id?.let { commonDaoServices.makeAnyNotBeNull(it) } as Long
                cdDetails.id = cdID
                var cdStatusType: CdStatusTypesEntity? = null


                when {
                    // Assign Officer Function
                    cdDetails.confirmAssignedUserId != null -> {
                        with(cdDetails) {
                            assignedInspectionOfficer =
                                confirmAssignedUserId?.let { commonDaoServices.findUserByID(it) }
                            val userProfile = assignedInspectionOfficer?.let {
                                commonDaoServices.findUserProfileByUserID(it, map.activeStatus)
                            }
                            assigner = userProfile?.sectionId?.let {
                                commonDaoServices.findUserProfileWithSectionIdAndDesignationId(
                                    it,
                                    commonDaoServices.findDesignationByID(applicationMapProperties.mapDIOfficerInChargeID),
                                    map.activeStatus
                                ).userId
                            }
                        }
                    }
                    // Approve or Reject Function
                    cdDetails.approveRejectCdStatus == map.activeStatus -> {
                        KotlinLogging.logger { }.info { "approveRejectCdStatusType = $cdDetails.confirmCdStatusTypeId" }
                        cdStatusType = cdDetails.confirmCdStatusTypeId?.let { daoServices.findCdStatusValue(it) }
                        cdDetails.confirmCdStatusTypeId?.let {
                            with(cdDetails) {
                                approveRejectCdStatusType = cdStatusType
                            }
                        }
                    }
                    // Local coi Function
                    cdDetails.localCoi == map.activeStatus -> {
                        with(cdDetails) {
                            localCoiRemarks = localCocOrCorRemarks
                        }
                    }
                }
                //updating of Details in DB
                val updatedCDDetails = daoServices.updateCdDetailsInDB(
                    commonDaoServices.updateDetails(cdDetails, consignmentDocument) as ConsignmentDocumentDetailsEntity,
                    loggedInUser
                )
//                                            val updatedCDDetails = com.updateCDDetails(cdDetails, cdID, loggedInUser, map)

                //If CD without COR, auto-target
                //Todo: Use the method for saving details
                if (updatedCDDetails.cdType?.let { daoServices.findCdTypeDetails(it).uuid } == daoServices.noCorCdType) {
                    updatedCDDetails.id?.let {
//                        daoServices.loopAllItemsInCDToBeTargeted(it, updatedCDDetails, map, loggedInUser)
                    }
                }

                when {
                    // CD transaction log Details Function
                    cdDetails.freightStation != null && cdDetails.portOfArrival != null && cdDetails.clusterId != null -> {
                        updatedCDDetails.assignPortRemarks?.let {
                            processStages.process1?.let { it1 ->
                                daoServices.createCDTransactionLog(map, loggedInUser, cdID, it, it1)
                            }
                        }
                    }
                    //Send Approval/Rejection message To Single Window
                    cdDetails.approveRejectCdStatus == map.activeStatus -> {
                        if (cdStatusType != null) {
                            cdStatusType.statusCode?.let {
                                cdDetails.approveRejectCdRemarks?.let { it1 -> daoServices.submitCDStatusToKesWS(it1, it, consignmentDocument.version.toString(), consignmentDocument)

                                    updatedCDDetails.cdStandard?.let { cdStd -> cdDetails.approveRejectCdStatusType?.id?.let { it2 -> daoServices.updateCDStatus(cdStd, it2) } }
                                }
                            }

                        }
                        //Update Check CD task in Bpm
                        cdDetails.id?.let { it1 ->
                            cdDetails.assigner?.id?.let { it2 ->
                                cdDetails.approveRejectCdStatusType?.category?.let { cdDecision ->
                                    diBpmn.diCheckCdComplete(it1, it2, cdDecision)
                                }
                            }
                        }
                    }
                    //Function for assigning IO
                    cdDetails.assignedStatus == map.activeStatus -> {
//                            val payload = "Assigned Inspection Officer [assignedStatus= ${updatedCDDetails.assignedStatus}, assignedRemarks= ${updatedCDDetails.assignedRemarks}]"
//                            val sr = commonDaoServices.mapServiceRequestForSuccess(map, payload, loggedInUser)
//                            updatedCDDetails.assignedInspectionOfficer?.let { commonDaoServices.sendEmailWithUserEntity(it, daoServices.diCdAssignedUuid, updatedCDDetails, map, sr) }
                        updatedCDDetails.assignedRemarks?.let {
                            processStages.process2?.let { it1 ->
                                daoServices.createCDTransactionLog(map, loggedInUser, cdID, it, it1)
                            }
                        }
                        updatedCDDetails.cdStandard?.let { cdStd ->
                            daoServices.updateCDStatus(cdStd, applicationMapProperties.mapDIStatusTypeAssignIoId)
                        }
                        //Start the relevant BPM
                        daoServices.startDiBpmProcessByCdType(updatedCDDetails)
                        daoServices.assignIOBpmTask(updatedCDDetails)
                    }
                    //Function for reassigning IO
                    cdDetails.reassignedStatus == map.activeStatus -> {
//                            val payload = "Assigned Inspection Officer [assignedStatus= ${updatedCDDetails.assignedStatus}, assignedRemarks= ${updatedCDDetails.assignedRemarks}]"
//                            val sr = commonDaoServices.mapServiceRequestForSuccess(map, payload, loggedInUser)
//                            updatedCDDetails.assignedInspectionOfficer?.let { commonDaoServices.sendEmailWithUserEntity(it, daoServices.diCdAssignedUuid, updatedCDDetails, map, sr) }
                        updatedCDDetails.reassignedRemarks?.let {
                            processStages.process2?.let { it1 ->
                                daoServices.createCDTransactionLog(map, loggedInUser, cdID, it, it1)
                            }
                        }
                        updatedCDDetails.cdStandard?.let { cdStd ->
                            daoServices.updateCDStatus(cdStd, applicationMapProperties.mapDIStatusTypeReassignIoId)
                        }
                        //Start the relevant BPM
                        daoServices.startDiBpmProcessByCdType(updatedCDDetails)
                        daoServices.assignIOBpmTask(updatedCDDetails)
                    }
                    //Function for blackList awaiting approval
                    cdDetails.blacklistStatus == map.activeStatus -> {
                        val payload =
                            "BlackList Consignment Document [blacklistStatus= ${updatedCDDetails.blacklistStatus}, blacklistRemarks= ${updatedCDDetails.blacklistRemarks}]"
                        val sr = commonDaoServices.mapServiceRequestForSuccess(map, payload, loggedInUser)
                        updatedCDDetails.assigner?.let {
                            commonDaoServices.sendEmailWithUserEntity(
                                it,
                                daoServices.diCdAssignedUuid,
                                updatedCDDetails,
                                map,
                                sr
                            )
                        }
                        updatedCDDetails.assignedRemarks?.let {
                            processStages.process2?.let { it1 ->
                                daoServices.createCDTransactionLog(
                                    map,
                                    loggedInUser,
                                    cdID,
                                    it,
                                    it1
                                )
                            }
                        }
                    }
                    //Send Demand Note
                    cdDetails.sendDemandNote == map.activeStatus -> {
                        //Send Demand Note
                        val demandNote = updatedCDDetails.id?.let {
                            daoServices.findDemandNoteWithCDID(
                                it
                            )
                        }
                        //TODO: DemandNote Simulate payment Status
                        demandNote?.demandNoteNumber?.let {
                            invoiceDaoService.createBatchInvoiceDetails(loggedInUser, it)
                                .let { batchInvoiceDetail ->
                                    invoiceDaoService.addInvoiceDetailsToBatchInvoice(
                                        demandNote,
                                        applicationMapProperties.mapInvoiceTransactionsForDemandNote,
                                        loggedInUser,
                                        batchInvoiceDetail
                                    )
                                        .let { updateBatchInvoiceDetail ->
                                            //Todo: Payment selection
                                            val importerDetails =
                                                updatedCDDetails.cdImporter?.let {
                                                    daoServices.findCDImporterDetails(it)
                                                }
                                            val myAccountDetails =
                                                InvoiceDaoService.InvoiceAccountDetails()
                                            with(myAccountDetails) {
                                                accountName = importerDetails?.name
                                                accountNumber = importerDetails?.pin
                                                currency =
                                                    applicationMapProperties.mapInvoiceTransactionsLocalCurrencyPrefix
                                            }
                                            invoiceDaoService.createPaymentDetailsOnStgReconciliationTable(
                                                loggedInUser,
                                                updateBatchInvoiceDetail,
                                                myAccountDetails
                                            )
                                            demandNote.id?.let { it1 ->
                                                daoServices.sendDemandNotGeneratedToKWIS(it1)
                                                updatedCDDetails.cdStandard?.let { cdStd ->
                                                    daoServices.updateCDStatus(
                                                        cdStd,
                                                        daoServices.awaitPaymentStatus.toLong()
                                                    )

                                                }
                                            }
                                        }

                                }
                        }
//                                                daoServices.demandNotePayment(demandNote, map, loggedInUser)
                        //Update BPM payment required task
//                            val cdDetails = updatedItemDetails.cdDocId
//                            cdDetails?.id?.let { it1 ->
//                                cdDetails.assignedInspectionOfficer?.id?.let { it2 ->
//                                    diBpmn.diPaymentRequiredComplete(it1, it2, true)
//                                }
//                            }
                    }
                    //Function for blackList approved
                    cdDetails.blacklistApprovedStatus == map.activeStatus -> {
                        when (cdDetails.blacklistId) {
                            applicationMapProperties.mapRiskProfileImporter -> {
                                val importerDetailsEntity =
                                    cdDetails.cdImporter?.let { daoServices.findCDImporterDetails(it) }
                                val allRemarksAdded =
                                    """[${cdDetails.blacklistApprovedRemarks}] and [${cdDetails.blacklistRemarks}] and [The UCR Number = ${cdDetails.ucrNumber}]"""
                                importerDetailsEntity?.let {
                                    riskProfileDaoService.addImporterToRiskProfile(
                                        it,
                                        allRemarksAdded,
                                        loggedInUser
                                    )
                                }
                            }
                            applicationMapProperties.mapRiskProfileExporter -> {
                                val exporterDetailsEntity =
                                    cdDetails.cdExporter?.let { daoServices.findCdExporterDetails(it) }
                                val allRemarksAdded =
                                    "[${cdDetails.blacklistApprovedRemarks}] and [${cdDetails.blacklistRemarks}] and [The UCR Number = ${cdDetails.ucrNumber}]"
                                exporterDetailsEntity?.let {
                                    riskProfileDaoService.addExporterToRiskProfile(
                                        it,
                                        allRemarksAdded,
                                        loggedInUser
                                    )
                                }
                            }
                            applicationMapProperties.mapRiskProfileConsignee -> {
                                val consigneeDetailsEntity =
                                    cdDetails.cdConsignee?.let { daoServices.findCdConsigneeDetails(it) }
                                val allRemarksAdded =
                                    "[${cdDetails.blacklistApprovedRemarks}] and [${cdDetails.blacklistRemarks}] and [The UCR Number = ${cdDetails.ucrNumber}]"
                                consigneeDetailsEntity?.let {
                                    riskProfileDaoService.addConsigneeToRiskProfile(
                                        it,
                                        allRemarksAdded,
                                        loggedInUser
                                    )
                                }
                            }
                            applicationMapProperties.mapRiskProfileConsignor -> {
                                val consignorDetailsEntity =
                                    cdDetails.cdConsignor?.let { daoServices.findCdConsignorDetails(it) }
                                val allRemarksAdded =
                                    "[${cdDetails.blacklistApprovedRemarks}] and [${cdDetails.blacklistRemarks}] and [The UCR Number = ${cdDetails.ucrNumber}]"
                                consignorDetailsEntity?.let {
                                    riskProfileDaoService.addConsignorToRiskProfile(
                                        it,
                                        allRemarksAdded,
                                        loggedInUser
                                    )
                                }
                            }
                        }
//                        daoServices.loopAllItemsInCDToBeTargeted(cdID, updatedCDDetails, map, loggedInUser)
                        updatedCDDetails.blacklistApprovedRemarks?.let {
                            processStages.process1?.let { it1 ->
                                daoServices.createCDTransactionLog(
                                    map,
                                    loggedInUser,
                                    cdID,
                                    it,
                                    it1
                                )
                            }
                        }
                    }
                    //Send Coi Data to Single Window
                    cdDetails.sendCoiStatus == map.activeStatus -> {
                        val localCoi = updatedCDDetails.ucrNumber?.let { daoServices.findCOC(it) }
                        if (localCoi != null) {
                            daoServices.localCoiItems(updatedCDDetails, localCoi, loggedInUser, map)
                            daoServices.sendLocalCoi(localCoi.id)
                            updatedCDDetails.cdStandard?.let { cdStd ->
                                daoServices.updateCDStatus(
                                    cdStd,
                                    applicationMapProperties.mapDICdStatusTypeCOIGeneratedAndSendID
                                )
                            }
                        }

                    }
                    //Send LOCAL COI/COC/COR Data to Single Window
                    cdDetails.localCocOrCorStatus == map.activeStatus -> {
                        if (updatedCDDetails.cdType?.let { daoServices.findCdTypeDetails(it).localCocStatus } == map.activeStatus) {

                            when (cdDetails.localCoi) {
                                //Todo : Ask Fred on where to get the routValue
                                map.activeStatus -> {
                                    val localCoi = daoServices.createLocalCoi(loggedInUser, updatedCDDetails, map, "D")
                                }
                                else -> {
                                    val localCoc = daoServices.createLocalCoc(loggedInUser, updatedCDDetails, map, "A")
                                    updatedCDDetails.cdStandard?.let { cdStd ->
                                        daoServices.updateCDStatus(
                                            cdStd,
                                            applicationMapProperties.mapDICdStatusTypeCOCGeneratedAndSendID
                                        )
                                    }
                                    KotlinLogging.logger { }.info { "localCoc = ${localCoc.id}" }
                                    //Generate PDF File & send to manufacturer
                                    reportsDaoService.generateLocalCoCReportWithDataSource(updatedCDDetails, applicationMapProperties.mapReportLocalCocPath)?.let { file ->
                                        updatedCDDetails.cdImporter?.let { daoServices.findCDImporterDetails(it)
                                        }?.let { importer ->
                                            importer.email?.let { daoServices.sendLocalCocReportEmail(it, file.path) }
                                        }
                                    }
                                }
                            }
                        } else if (updatedCDDetails.cdType?.let { daoServices.findCdTypeDetails(it).localCorStatus } == map.activeStatus) {
                            daoServices.generateCor(updatedCDDetails, map, loggedInUser).let { corDetails ->
                                daoServices.submitCoRToKesWS(corDetails)
                                updatedCDDetails.cdStandard?.let { cdStd ->
                                    daoServices.updateCDStatus(
                                        cdStd,
                                        applicationMapProperties.mapDICdStatusTypeCORGeneratedAndSendID
                                    )
                                }
                                //Send Cor to manufacturer
                                reportsDaoService.generateLocalCoRReport(updatedCDDetails, applicationMapProperties.mapReportLocalCorPath)?.let { file ->
                                    with(corDetails) {
                                        localCorFile = file.readBytes()
                                        localCorFileName = file.name
                                    }
                                    daoServices.saveCorDetails(corDetails)
                                    //Send email
                                    updatedCDDetails.cdImporter?.let { daoServices.findCDImporterDetails(it)
                                    }?.let { importer ->
                                        importer.email?.let { daoServices.sendLocalCorReportEmail(it, file.path) }
                                    }
                                }
                            }
                        }
                    }

                    //Targeting & Approval
                    //If item targeted by IO, submit task to Supervisor for review
//                    cdDetails.targetStatus == map.activeStatus && cdDetails.targetApproveStatus == null -> {
//                        val cdDecision = "TARGET"
//                        cdDetails?.id?.let { it1 ->
//                            cdDetails.assigner?.id?.let { it2 ->
//                                diBpmn.diCheckCdComplete(it1, it2, cdDecision)
//                            }
//                        }
//                    }

                    //If item target has been approved by Supervisor, update review target approval
                    cdDetails.targetApproveStatus == map.activeStatus && cdDetails.inspectionNotificationStatus == map.activeStatus -> {
                        KotlinLogging.logger { }.info { "::::::::::: Target approved ::::::::::" }
                        /*
                        Send verification request
                         */
                        //Get the declaration ref no
                        val declaration: DeclarationDetailsEntity? =   updatedCDDetails.ucrNumber?.let { daoServices.findDeclaration(it) }
                        if (declaration != null) {
                            daoServices.submitCDStatusToKesWS("OH", "OH", updatedCDDetails.version.toString(), updatedCDDetails)
                            //Update cd details
                            updatedCDDetails.cdStandard?.let { cdStd ->
                                daoServices.updateCDStatus(cdStd, applicationMapProperties.mapDIStatusTypeKraVerificationSendId)
                            }
                        } else {
                            redirectAttributes.addFlashAttribute("error", "Could not send verification request. Declaration unavailable")
                        }
//                        cdDetails?.id?.let { it1 ->
//                            cdDetails.assigner?.id?.let { it2 ->
//                                updatedItemDetails.id?.let {
//                                    diBpmn.diReviewTargetRequestComplete(it1, it2, true, it)
//                                }
//                            }
//                        }
                    }
                }
                daoServices.updateCdDetailsInDB(updatedCDDetails, loggedInUser)
                return daoServices.viewCdPageDetails(cdUuid)
            }


    }


    @PreAuthorize("hasAuthority('DI_OFFICER_CHARGE_MODIFY') or hasAuthority('DI_INSPECTION_OFFICER_MODIFY')")
    @PostMapping("cd-item-save/details")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun cdItemDetailsSave(
        @ModelAttribute item: CdItemDetailsEntity,
        @RequestParam("cdItemUuid") cdItemUuid: String,
        model: Model,
        result: BindingResult
    ): String {
        commonDaoServices.serviceMapDetails(appId)
            .let { map ->
                commonDaoServices.loggedInUserDetails()
                    .let { loggedInUser ->
                        daoServices.findItemWithUuid(cdItemUuid)
                            .let { itemDetails ->
                                val cdItemID: Long =
                                    itemDetails.id?.let { commonDaoServices.makeAnyNotBeNull(it) } as Long
                                item.id = cdItemID

                                //Values before update of item Details
                                if (item.confirmFeeIdSelected != null) {
                                    with(item) {
                                        paymentFeeIdSelected =
                                            confirmFeeIdSelected?.let { daoServices.findDIFee(it) }
                                    }
                                }
                                //Update Item details
                                val updatedItemDetails = daoServices.updateCdItemDetailsInDB(
                                    commonDaoServices.updateDetails(
                                        item,
                                        itemDetails
                                    ) as CdItemDetailsEntity, loggedInUser
                                )

                                //Values after update of item Details
                                when {

                                    //If the item needs To be Paid For (Demand Note Generation)
                                    item.confirmFeeIdSelected != null -> {
                                        val demandNote = daoServices.generateDemandNoteWithItemList(
                                            updatedItemDetails,
                                            map,
                                            loggedInUser
                                        )
                                    }
                                }

                                return daoServices.viewCdItemPage(cdItemUuid)
                            }
                    }
            }

    }

    @PreAuthorize("hasAuthority('DI_OFFICER_CHARGE_MODIFY') or hasAuthority('DI_INSPECTION_OFFICER_MODIFY')")
    @PostMapping("kebs/ssf-details-uploads")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun saveNewSSF(
        @RequestParam("cdItemID") cdItemID: Long,
        @ModelAttribute("SampleSubmissionDetails") sampleSubmissionDetails: QaSampleSubmissionEntity,
        model: Model
    ): String? {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        var cdItem = daoServices.findItemWithItemID(cdItemID)

        val result: ServiceRequestsEntity?


        //updating of Details in DB
        result = daoServices.ssfSave(cdItem,sampleSubmissionDetails,loggedInUser,map).first
        with(cdItem){
            sampleBsNumberStatus = map.activeStatus
        }
        cdItem = daoServices.updateCdItemDetailsInDB(cdItem,loggedInUser)

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/di/inspection/ssf-details?cdItemID=${cdItem.id}"
        sm.message = "You have Successful Filled Sample Submission Details"

        return commonDaoServices.returnValues(result, map, sm)
    }

    @PreAuthorize("hasAuthority('DI_OFFICER_CHARGE_MODIFY') or hasAuthority('DI_INSPECTION_OFFICER_MODIFY')")
    @PostMapping("kebs/lab-results-compliance-status/save")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun complianceStatusSSF(
        @RequestParam("cdItemID") cdItemID: Long,
        @ModelAttribute("SampleSubmissionDetails") sampleSubmissionDetails: QaSampleSubmissionEntity,
        model: Model
    ): String? {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        var cdItem = daoServices.findItemWithItemID(cdItemID)

        val result: ServiceRequestsEntity?
//        sampleSubmissionDetails.id = cdItem.id

//        val ssf =commonDaoServices.updateDetails(sampleSubmissionDetails,daoServices.findSampleSubmittedBYCdItemID(cdItem.id?: throw Exception("MISSING ITEM ID"))) as QaSampleSubmissionEntity


        //updating of Details in DB
        result = daoServices.ssfUpdateDetails(cdItem,sampleSubmissionDetails,loggedInUser,map).first

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/di/inspection/ssf-details?cdItemID=${cdItem.id}"
        sm.message = "You have Successful Filled Sample Submission Details"

        return commonDaoServices.returnValues(result, map, sm)
    }


    @PreAuthorize("hasAuthority('DI_OFFICER_CHARGE_MODIFY') or hasAuthority('DI_INSPECTION_OFFICER_MODIFY')")
    @PostMapping("uploads")
    fun uploadDetailsSave(
        @ModelAttribute upLoads: DiUploadsEntity,
        @RequestParam("cdItemUuid") cdItemUuid: String,
        @RequestParam("doc_file") docFile: MultipartFile,
        model: Model,
        redirectAttributes: RedirectAttributes,
        result: BindingResult
    ): String {

        commonDaoServices.serviceMapDetails(appId)
            .let { map ->
                commonDaoServices.loggedInUserDetails()
                    .let { loggedInUser ->
                        if (docFile.isEmpty) {
                            redirectAttributes.addFlashAttribute("message", "Please select a file to upload.")
                            return daoServices.viewCdItemPage(cdItemUuid)
                        }

                        daoServices.findItemWithUuid(cdItemUuid)
                            .let { itemDetails ->
                                daoServices.saveUploads(
                                    upLoads,
                                    docFile,
                                    "inspection_doc",
                                    loggedInUser,
                                    map,
                                    itemDetails,
                                    null
                                )
                                return daoServices.viewCdItemPage(cdItemUuid)
                            }

                    }
            }

    }


    @PreAuthorize("hasAuthority('DI_INSPECTION_OFFICER_MODIFY')")
    @PostMapping("item/inspection/checklist/save")
    fun saveCheckListDetails(
//            @ModelAttribute checkList: CdInspectionChecklistEntity,
        @ModelAttribute generalCheckList: CdInspectionGeneralEntity,
        @ModelAttribute agrochemItemInspectionChecklist: CdInspectionAgrochemItemChecklistEntity,
        @ModelAttribute engineeringItemInspectionChecklist: CdInspectionEngineeringItemChecklistEntity,
        @ModelAttribute otherItemInspectionChecklist: CdInspectionOtherItemChecklistEntity,
        @ModelAttribute motorVehicleItemInspectionChecklist: CdInspectionMotorVehicleItemChecklistEntity?,
        @ModelAttribute item: CdItemDetailsEntity,
        @RequestParam("cdItemUuid") cdItemUuid: String,
        model: Model,
        result: BindingResult
    ): String {
        commonDaoServices.serviceMapDetails(appId)
            .let { map ->
                commonDaoServices.loggedInUserDetails()
                    .let { loggedInUser ->
                        //Get CD item
                        val cdItem = daoServices.findItemWithUuid(cdItemUuid)
                        var cdUpdateItem: CdItemDetailsEntity? = item
                        //Save the general checklist
                        val inspectionGeneral =
                            daoServices.saveInspectionGeneralDetails(generalCheckList, cdItem, loggedInUser, map)
                        //Save the respective checklist
                        when (inspectionGeneral.checkListType?.uuid) {
                            daoServices.agrochemItemChecklistType -> {
                                daoServices.saveInspectionAgrochemItemChecklist(
                                    agrochemItemInspectionChecklist,
                                    inspectionGeneral,
                                    loggedInUser,
                                    map
                                )
                                cdUpdateItem = agrochemItemInspectionChecklist.sampled?.let {
                                    daoServices.checkIfChecklistUndergoesSampling(
                                        it,
                                        item,
                                        map
                                    )
                                }
                            }
                            daoServices.engineeringItemChecklistType -> {
                                daoServices.saveInspectionEngineeringItemChecklist(
                                    engineeringItemInspectionChecklist,
                                    inspectionGeneral,
                                    loggedInUser,
                                    map
                                )
                                cdUpdateItem = engineeringItemInspectionChecklist.sampled?.let {
                                    daoServices.checkIfChecklistUndergoesSampling(
                                        it,
                                        item,
                                        map
                                    )
                                }
                            }
                            daoServices.otherItemChecklistType -> {
                                daoServices.saveInspectionOtherItemChecklist(
                                    otherItemInspectionChecklist,
                                    inspectionGeneral,
                                    loggedInUser,
                                    map
                                )
                                cdUpdateItem = otherItemInspectionChecklist.sampled?.let {
                                    daoServices.checkIfChecklistUndergoesSampling(
                                        it,
                                        item,
                                        map
                                    )
                                }
                            }
                            daoServices.motorVehicleItemChecklistType -> {
                                if (motorVehicleItemInspectionChecklist != null) {
                                    daoServices.saveInspectionMotorVehicleItemChecklist(
                                        motorVehicleItemInspectionChecklist,
                                        inspectionGeneral,
                                        loggedInUser,
                                        map
                                    )
                                    cdUpdateItem = daoServices.updateItemNoSampling(item, map)
                                }
                            }
                        }
                        //Save CD item details
                        val cdItemID: Long = cdItem.id?.let { commonDaoServices.makeAnyNotBeNull(it) } as Long
                        cdUpdateItem?.id = cdItemID
                        cdUpdateItem?.checkListTypeId = generalCheckList.checkListType
                        when {
                            cdUpdateItem != null -> {
                                daoServices.updateCDItemDetails(cdUpdateItem, cdItemID, loggedInUser, map)
                            }
                        }
                        //BPM: Update fill inspection details workflow
                        val cdDetails = cdItem.cdDocId
//                        cdDetails?.id?.let { it1 ->
//                            cdDetails.assignedInspectionOfficer?.id?.let { it2 ->
//                                diBpmn.diFillInspectionForms(it1, it2)
//                            }
//                        }
                        cdDetails?.cdStandard?.let { cdStd ->
                            daoServices.updateCDStatus(cdStd, applicationMapProperties.mapDIStatusTypeInspectionChecklistId)
                        }

                        return daoServices.viewCdItemPage(cdItemUuid)
                    }
            }

    }


    @PreAuthorize("hasAuthority('MINISTRY_OF_TRANSPORT_MODIFY')")
    @PostMapping("ministry/upload-inspection-report")
    fun uploadMinistryInspectionReport(
        @RequestParam("mvInspectionChecklistId") inspectionChecklistId: Long,
        @RequestParam("doc_file") docFile: MultipartFile,
        redirectAttributes: RedirectAttributes
//            result: BindingResult
    ): String {
        commonDaoServices.serviceMapDetails(appId)
            .let { map ->
                commonDaoServices.loggedInUserDetails()
                    .let { loggedInUser ->
                        if (docFile.isEmpty) {
                            throw ExpectedDataNotFound("No File found Found")
                        }
                        KotlinLogging.logger { }.info { "mvInspectionChecklistId = $inspectionChecklistId" }
                        daoServices.findInspectionMotorVehicleById(inspectionChecklistId)
                            ?.let { inspectionMotorVehicle ->
                                inspectionMotorVehicle.ministryReportFile = docFile.bytes
                                inspectionMotorVehicle.ministryReportSubmitStatus = map.activeStatus
                                daoServices.updateCdInspectionMotorVehicleItemChecklistInDB(
                                    inspectionMotorVehicle,
                                    loggedInUser
                                ).let { cdInspectionMVChecklist ->
                                    cdInspectionMVChecklist.inspectionGeneral?.cdItemDetails?.let { cdItemDetails ->
                                        cdItemDetails.cdDocId?.let { cdEntity ->
                                            //daoServices.sendMinistryInspectionReportSubmittedEmail(it, cdItemDetails)
                                            //Complete Generate Ministry Inspection Report & Assign Review Ministry Inspection Report
                                            cdEntity.id?.let {
                                                cdEntity.assignedInspectionOfficer?.id?.let { it1 ->
                                                    diBpmn.diGenerateMinistryInspectionReportComplete(
                                                        it, it1
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                                redirectAttributes.addFlashAttribute("success", "Report Submitted Successfully")
                                return "$motorVehicleInspectionDetailsPage=${inspectionMotorVehicle.inspectionGeneral?.cdItemDetails?.id}&docType=${daoServices.motorVehicleMinistryInspectionChecklistName}"
                            } ?: throw ExpectedDataNotFound("No Motor Vehicle Inspection Checklist Found")
                    }
            }
    }

    @PreAuthorize("hasAuthority('DI_INSPECTION_OFFICER_MODIFY')")
    @PostMapping("upload/motor-inspection-report")
    fun uploadMotorInspectionReport(
        @RequestParam("generalInspectionChecklistId") generalInspectionChecklistId: Long,
        @RequestParam("doc_file") docFile: MultipartFile,
        @RequestParam("compliance_status") complianceStatus: Int,
        @RequestParam("compliance_recommendations") complianceRecommendations: String,
        redirectAttributes: RedirectAttributes
    ): String {
        KotlinLogging.logger { }.info {
            "Params received \n generalInspectionChecklistId: ${generalInspectionChecklistId}, " +
                    "compliance_status: $complianceStatus, compliance_recommendations: $complianceRecommendations"
        }
        commonDaoServices.serviceMapDetails(appId)
            .let {
                commonDaoServices.loggedInUserDetails()
                    .let { loggedInUser ->
                        if (docFile.isEmpty) {
                            throw ExpectedDataNotFound("No File found Found")
                        }
                        KotlinLogging.logger { }.info { "generalInspectionChecklistId = $generalInspectionChecklistId" }

                        daoServices.findInspectionGeneralById(generalInspectionChecklistId)
                            ?.let { cdInspectionGeneralEntity ->
                                cdInspectionGeneralEntity.inspectionReportFile = docFile.bytes
                                cdInspectionGeneralEntity.complianceRecommendations = complianceRecommendations
                                cdInspectionGeneralEntity.complianceStatus = complianceStatus
                                daoServices.updateCdInspectionGeneralChecklistInDB(
                                    cdInspectionGeneralEntity,
                                    loggedInUser
                                ).let {
                                    //TODO: Send notification to the supervisor
//                                        diBpmn.diGenerateMotorInspectionReportComplete(it, it1)
                                }
                                return "$motorVehicleInspectionDetailsPage=${cdInspectionGeneralEntity.cdItemDetails?.id}&docType=${daoServices.motorVehicleMinistryInspectionChecklistName}"
                            } ?: throw ExpectedDataNotFound("No General Inspection Checklist Found")
                    }
            }
    }

    @PreAuthorize("hasAuthority('DI_INSPECTION_OFFICER_MODIFY')")
    @PostMapping("item/inspection/sample-Collect/save")
    fun saveSampleCollectDetails(
        @ModelAttribute sampleCollect: CdSampleCollectionEntity,
        @ModelAttribute item: CdItemDetailsEntity,
        @RequestParam("cdItemUuid") cdItemUuid: String,
        model: Model,
        result: BindingResult
    ): String {
        commonDaoServices.serviceMapDetails(appId)
            .let { map ->
                commonDaoServices.loggedInUserDetails()
                    .let { loggedInUser ->
                        daoServices.saveSampleCollectDetails(sampleCollect, loggedInUser, map)
                            .let {
                                daoServices.findItemWithUuid(cdItemUuid)
                                    .let { itemDetails ->
                                        val cdItemID: Long =
                                            itemDetails.id?.let { commonDaoServices.makeAnyNotBeNull(it) } as Long
                                        // Update status of checklist in the item Details
                                        item.id = cdItemID
                                        daoServices.updateCdItemDetailsInDB(
                                            commonDaoServices.updateDetails(
                                                item,
                                                itemDetails
                                            ) as CdItemDetailsEntity, loggedInUser
                                        )
                                        return daoServices.viewCdItemPage(cdItemUuid)
                                    }
                            }


                    }
            }

    }

    @PreAuthorize("hasAuthority('DI_INSPECTION_OFFICER_MODIFY')")
    @PostMapping("item/inspection/sample-Submit/save")
    fun saveSampleSubmitDetails(
        @ModelAttribute sampleSubmit: CdSampleSubmissionItemsEntity,
        @ModelAttribute item: CdItemDetailsEntity,
        @RequestParam("cdItemUuid") cdItemUuid: String,
        model: Model,
        result: BindingResult
    ): String {
        commonDaoServices.serviceMapDetails(appId)
            .let { map ->
                commonDaoServices.loggedInUserDetails()
                    .let { loggedInUser ->
                        daoServices.saveSampleSubmitDetails(sampleSubmit, loggedInUser, map)
                            .let {
                                daoServices.findItemWithUuid(cdItemUuid)
                                    .let { itemDetails ->
                                        val cdItemID: Long =
                                            itemDetails.id?.let { commonDaoServices.makeAnyNotBeNull(it) } as Long
                                        // Update status of checklist in the item Details
                                        item.id = cdItemID
                                        daoServices.updateCdItemDetailsInDB(
                                            commonDaoServices.updateDetails(
                                                item,
                                                itemDetails
                                            ) as CdItemDetailsEntity, loggedInUser
                                        )
                                        return daoServices.viewCdItemPage(cdItemUuid)
                                    }
                            }
                    }
            }

    }


    @PreAuthorize("hasAuthority('DI_INSPECTION_OFFICER_MODIFY')")
    @PostMapping("item/inspection/sample-submit/param/save")
    fun saveSampleSubmitParamDetails(
        @ModelAttribute sampleParam: CdSampleSubmissionParamatersEntity,
        @ModelAttribute sampleSubmit: CdSampleSubmissionItemsEntity?,
        @ModelAttribute item: CdItemDetailsEntity?,
        @RequestParam("cdItemUuid") cdItemUuid: String,
        @RequestParam("sampleId") sampleId: Long,
        @RequestParam("message") message: String,
        model: Model,
        result: BindingResult
    ): String? {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        var payload: String
        val itemDetails = daoServices.findItemWithUuid(cdItemUuid)
        val cdItemID: Long = itemDetails.id?.let { commonDaoServices.makeAnyNotBeNull(it) } as Long
        var returnPage: String? = null
//        sampleParam.confirmSampleId
//                ?.let {
        val sampleSubmitted = daoServices.findSavedSampleSubmissionById(sampleId)
        when (message) {
            daoServices.sampSubmitAddParamDetails -> {
                sampleParam.confirmLabId
                    ?.let { it1 ->
                        val laboratory = commonDaoServices.findLaboratoryWIthId(it1)
                        daoServices.saveSampleSubmitParamDetails(
                            sampleSubmitted,
                            laboratory,
                            sampleParam,
                            loggedInUser,
                            map
                        )
//                            val paramAddedDetails = daoServices.saveSampleSubmitParamDetails(sampleSubmitted, laboratory, sampleParam, loggedInUser, map)
                        returnPage = daoServices.viewSampleSubmitPage(itemDetails, message)

                    }
                    ?: throw ExpectedDataNotFound("check config for lab ID")
            }
            daoServices.sampSubmitParamName -> {
                sampleParam.remarks?.let { it1 ->
                    val sampleParamList = daoServices.findListSampleSubmissionParameter(sampleSubmitted)
                    daoServices.sendSamplesToRespectiveLabs(sampleSubmitted, it1, loggedInUser, map, sampleParamList)
                        .let { sampleSubmittedToLab ->
                            payload =
                                "${this::saveSampleSubmitParamDetails.name} updated with id =[${sampleSubmittedToLab.id}]"
                            when {
                                item != null -> {
                                    with(item) {
                                        id = cdItemID
                                        sampleBsNumberStatus = map.activeStatus
                                    }
                                    //Update Item Details By Making the sample with Bs Number Updated
                                    daoServices.updateCdItemDetailsInDB(
                                        commonDaoServices.updateDetails(
                                            item,
                                            itemDetails
                                        ) as CdItemDetailsEntity, loggedInUser
                                    )
                                        .let { updatedItemDetails ->
                                            payload =
                                                "${payload}: Updated details [${this::saveSampleSubmitParamDetails.name} with Item ID =[${updatedItemDetails.id}]]"
                                            //Adding properties For sending Email With Bs Number to IO
                                            val bsNumberLabDto = daoServices.bsNumberLabDTOResultsEmailCompose(
                                                sampleSubmittedToLab,
                                                updatedItemDetails,
                                                daoServices.bsNumber
                                            )
                                            val sr = commonDaoServices.mapServiceRequestForSuccess(
                                                map,
                                                payload,
                                                loggedInUser
                                            )
                                            updatedItemDetails.cdDocId?.assignedInspectionOfficer?.let {
                                                commonDaoServices.sendEmailWithUserEntity(
                                                    it,
                                                    applicationMapProperties.mapDIBsNumberNotification,
                                                    bsNumberLabDto,
                                                    map,
                                                    sr
                                                )
                                            }

                                            //Simulation of Lab Results details
                                            daoServices.simulateLabReportSendingResults(
                                                sampleParamList,
                                                map,
                                                loggedInUser,
                                                sampleSubmittedToLab
                                            )
                                                .let {
                                                    returnPage =
                                                        daoServices.viewSampleSubmitPage(updatedItemDetails, message)
                                                }


                                        }
                                }
                            }

                        }
                } ?: throw ExpectedDataNotFound("Sample Remarks messing")
            }
            daoServices.labResults -> {
                sampleParam.confirmParamId?.let { it1 ->
                    sampleParam.id = it1
                    var updatedParam = daoServices.updateCdItemSampleParamDetailsInDB(
                        commonDaoServices.updateDetails(
                            sampleParam,
                            daoServices.findSavedSampleSubmissionParameter(it1)
                        ) as CdSampleSubmissionParamatersEntity, loggedInUser
                    )
//                    var updatedParam = daoServices.updateCDSampleParamsDetails(sampleParam, it1, loggedInUser, map)
//                                val param = daoServices.findSavedSampleSubmissionParameter(it1)
//                                param.labResultCompleteStatus = sampleParam.labResultCompleteStatus
                    when (updatedParam.labResultCompleteStatus) {
                        map.activeStatus -> {
                        }
                        map.inactiveStatus -> {
                            val simulateParam = daoServices.simulateLabReportResults(updatedParam, map)
                            updatedParam = daoServices.updateCdItemSampleParamDetailsInDB(simulateParam, loggedInUser)
                            //Simulate Email Results Sending Back Again
                            daoServices.simulateEmailLabResults(updatedParam, map, loggedInUser)
                        }
                    }
                    returnPage = daoServices.viewSampleSubmitPage(itemDetails, message)
//                    returnPage = "$cdItemSampleSubmittedViewPageDetails=${sampleSubmitted.itemId}&docType=${daoServices.sampSubmitName}&message=$message"
                } ?: throw ExpectedDataNotFound("check config for param id")
            }
            daoServices.labResultsAllComplete -> {
                with(sampleSubmit) {
                    this?.id = sampleSubmitted.id
                }
                daoServices.updateSampleSubmitDetails(sampleSubmit?.let {
                    commonDaoServices.updateDetails(
                        it,
                        sampleSubmitted
                    )
                } as CdSampleSubmissionItemsEntity, loggedInUser, map)
                with(item) {
                    this?.id = cdItemID
                }
                daoServices.updateCdItemDetailsInDB(item?.let {
                    commonDaoServices.updateDetails(
                        it,
                        itemDetails
                    )
                } as CdItemDetailsEntity, loggedInUser)

                //Todo: Push ALl Results To the risk module
                returnPage = itemDetails.uuid?.let { daoServices.viewCdItemPage(it) }
            }
            else -> {
                throw ExpectedDataNotFound("INVALID MESSAGE VALUE!!!!!!")
            }
        }
        return returnPage
    }

    @PreAuthorize("hasAuthority('DI_OFFICER_CHARGE_MODIFY') or hasAuthority('DI_INSPECTION_OFFICER_MODIFY')")
    @PostMapping("inspection/item-report/save")
    fun saveInspectionReportRecommendationDetails(
        @ModelAttribute generalInspectionReport: CdInspectionGeneralEntity,
        @RequestParam("cdItemUuid") cdItemUuid: String,
        model: Model,
        result: BindingResult
    ): String {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val itemDetails = daoServices.findItemWithUuid(cdItemUuid)
        val filledGeneralInspection = daoServices.findSavedGeneralInspection(itemDetails)

        //Update The entity from frontEnd With id of fetched Inspection General entity
        with(generalInspectionReport) {
            id = filledGeneralInspection.id
        }
        if (generalInspectionReport.complianceStatus != null) {
            generalInspectionReport.inspectionReportRefNumber =
                "IRREF#${generateRandomText(5, map.secureRandom, map.messageDigestAlgorithm, true)}".toUpperCase()
        }

        var payload =
            "${this::saveInspectionReportRecommendationDetails.name} updated with id =[${generalInspectionReport.id}]"
        daoServices.updateCdInspectionGeneralChecklistInDB(
            commonDaoServices.updateDetails(
                generalInspectionReport,
                filledGeneralInspection
            ) as CdInspectionGeneralEntity, loggedInUser
        )
            .let { updatedGeneralInspectionReport ->
                payload =
                    "${payload}: Updated details [${this::saveInspectionReportRecommendationDetails.name} with Item ID =[${itemDetails.id}]]"

                when {
                    generalInspectionReport.complianceStatus != null -> {
                        with(itemDetails) {
                            inspectionReportStatus = map.activeStatus
                        }
                        val updatedItemDetails = daoServices.updateCdItemDetailsInDB(itemDetails, loggedInUser)
                        val inspectionReportEmailDto = daoServices.inspectionReportApprovalDTOEmailCompose(
                            updatedItemDetails,
                            updatedGeneralInspectionReport
                        )
                        val sr = commonDaoServices.mapServiceRequestForSuccess(map, payload, loggedInUser)
//                             itemDetails.cdDocId?.assigner?.let { commonDaoServices.sendEmailWithUserEntity(it, applicationMapProperties.mapDIReportApprovalNotification, inspectionReportEmailDto, map, sr) }

                    }
                    generalInspectionReport.inspectionReportApprovalStatus != null -> {

                        when (generalInspectionReport.inspectionReportApprovalStatus) {
                            map.activeStatus -> {

                                val inspectionReportApprovedEmailDto =
                                    daoServices.inspectionReportApprovedDTOEmailCompose(
                                        itemDetails,
                                        updatedGeneralInspectionReport
                                    )
                                val sr = commonDaoServices.mapServiceRequestForSuccess(map, payload, loggedInUser)
//                                    itemDetails.cdDocId?.assignedInspectionOfficer?.let { commonDaoServices.sendEmailWithUserEntity(it, applicationMapProperties.mapDIReportApprovedNotification, inspectionReportApprovedEmailDto, map, sr) }

                            }
                            map.inactiveStatus -> {
                                updatedGeneralInspectionReport.complianceStatus = null
                                updatedGeneralInspectionReport.inspectionReportApprovalStatus = null
                                daoServices.updateCdInspectionGeneralChecklistInDB(
                                    updatedGeneralInspectionReport,
                                    loggedInUser
                                )
                                val inspectionReportDisApprovedEmailDto =
                                    daoServices.inspectionReportDisApprovedDTOEmailCompose(
                                        itemDetails,
                                        updatedGeneralInspectionReport
                                    )
                                val sr = commonDaoServices.mapServiceRequestForSuccess(map, payload, loggedInUser)
                                // itemDetails.cdDocId?.assignedInspectionOfficer?.let { commonDaoServices.sendEmailWithUserEntity(it, applicationMapProperties.mapDIReportDisApprovedNotification, inspectionReportDisApprovedEmailDto, map, sr) }

                            }
                        }

                    }
                }

                return daoServices.viewCdItemPage(cdItemUuid)
            }

    }

    @PreAuthorize("hasAuthority('DI_INSPECTION_OFFICER_MODIFY')")
    @GetMapping("ministry-submission")
    fun submitMVInspectionRequestToMinistry(
//        response: HttpServletResponse,
        @RequestParam("cdItemUuid") cdItemUuid: String,
        redirectAttributes: RedirectAttributes
    ): String {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        val cdItemDetails = daoServices.findItemWithUuid(cdItemUuid)
        val cdItemID: Long = cdItemDetails.id?.let { commonDaoServices.makeAnyNotBeNull(it) } as Long

        cdItemDetails.ministrySubmissionStatus = map.activeStatus
        daoServices.updateCDItemDetails(cdItemDetails, cdItemID, loggedInUser, map)

        val cdDetails = cdItemDetails.cdDocId
        commonDaoServices.findAllUsersWithMinistryUserType()?.let { ministryUsers ->
            cdDetails?.id?.let { cdDetailsId ->
                ministryUsers.get(0).id?.let {
                    diBpmn.diMinistryInspectionRequiredComplete(
                        cdDetailsId,
                        it, true
                    )
                }
            }
        }

        redirectAttributes.addFlashAttribute("success", "Data Submitted Successfully")
        return "$motorVehicleInspectionDetailsPage=${cdItemDetails.id}&docType=${daoServices.motorVehicleInspectionDetailsName}"
    }


    @PreAuthorize("hasAuthority('DI_INSPECTION_OFFICER_MODIFY')")
    @GetMapping("ministry-inspection-report")
    fun downloadFileDocument(
        response: HttpServletResponse,
        @RequestParam("motorVehicleInspectionChecklistId") motorVehicleInspectionChecklistId: Long
    ) {
        daoServices.findInspectionMotorVehicleById(motorVehicleInspectionChecklistId)
            ?.let { cdInspectionMotorVehicleItemChecklistEntity ->
                cdInspectionMotorVehicleItemChecklistEntity.ministryReportFile?.let {
                    response.contentType = "application/pdf"
                    response.addHeader(
                        "Content-Dispostion", "inline; filename=${
                            cdInspectionMotorVehicleItemChecklistEntity.chassisNo
                        }_inspection_report;"
                    )
                    response.outputStream
                        .let { responseOutputStream ->
                            responseOutputStream.write(it)
                            responseOutputStream.close()
                        }
                } ?: throw ExpectedDataNotFound("Inspection Report file not found")
            }
            ?: throw ExpectedDataNotFound("Motor Vehicle Inspection checklist with ID: $motorVehicleInspectionChecklistId not found")
    }

    @PreAuthorize("hasAuthority('DI_OFFICER_CHARGE_MODIFY') or hasAuthority('DI_INSPECTION_OFFICER_MODIFY')")
    @GetMapping("final-motor-inspection-report")
    fun finalMotorVehicleInspectionReport(
        response: HttpServletResponse,
        @RequestParam("generalInspectionChecklist") generalInspectionChecklistId: Long
    ) {
        daoServices.findInspectionGeneralById(generalInspectionChecklistId)
            ?.let { cdInspectionGeneralEntity ->
                cdInspectionGeneralEntity.inspectionReportFile?.let {
                    response.contentType = "application/pdf"
                    response.addHeader("Content-Dispostion", "inline; filename=final_inspection_report;")
                    response.outputStream
                        .let { responseOutputStream ->
                            responseOutputStream.write(it)
                            responseOutputStream.close()
                        }
                } ?: throw ExpectedDataNotFound("Motor Inspection Report file not found")
            }
            ?: throw ExpectedDataNotFound("Motor Inspection report with ID: $generalInspectionChecklistId not found")
    }

    @PreAuthorize("hasAuthority('DI_OFFICER_CHARGE_MODIFY') or hasAuthority('DI_INSPECTION_OFFICER_MODIFY')")
    @PostMapping("mv-inspection-action")
    fun cdItemDetailsSave(
        @ModelAttribute mvInspectionChecklist: CdInspectionMotorVehicleItemChecklistEntity,
        @RequestParam("mvInspectionChecklistId") mvInspectionChecklistId: Long,
        model: Model,
        result: BindingResult,
        status: SessionStatus
    ): String {
        commonDaoServices.serviceMapDetails(appId)
            .let { map ->

                commonDaoServices.loggedInUserDetails()
                    .let { loggedInUser ->
                        KotlinLogging.logger { }.info { "mvInspectionChecklistId = $mvInspectionChecklistId" }
                        daoServices.findInspectionMotorVehicleById(mvInspectionChecklistId)
                            ?.let { inspectionMotorVehicle ->
                                when {
                                    //Supervisor report disapproval
                                    mvInspectionChecklist.inspectionReportApprovalStatus != null -> {
                                        inspectionMotorVehicle.inspectionGeneral?.let { cdInspectionGeneralEntity ->
                                            cdInspectionGeneralEntity.inspectionReportApprovalStatus =
                                                mvInspectionChecklist.inspectionReportApprovalStatus
                                            if (mvInspectionChecklist.inspectionReportApprovalComments != null) {
                                                cdInspectionGeneralEntity.inspectionReportApprovalComments =
                                                    mvInspectionChecklist.inspectionReportApprovalComments
                                                cdInspectionGeneralEntity.inspectionReportApprovalDate =
                                                    commonDaoServices.getCurrentDate()
                                                daoServices.updateCdInspectionGeneralChecklistInDB(
                                                    cdInspectionGeneralEntity,
                                                    loggedInUser
                                                ).let {
                                                    //Generate Local Cor & Submit to KeSWS

                                                    val payload =
                                                        "Motor Inspection Report Approved [inspectionReportApprovalStatus= ${cdInspectionGeneralEntity.inspectionReportApprovalStatus}, inspectionReportApprovalComments= ${cdInspectionGeneralEntity.inspectionReportApprovalComments}]"
                                                    val sr = commonDaoServices.mapServiceRequestForSuccess(
                                                        map,
                                                        payload,
                                                        loggedInUser
                                                    )
                                                    //Create DTO & Send email
                                                    createMvInspectionReportNotificationDTO(
                                                        cdInspectionGeneralEntity,
                                                        inspectionMotorVehicle
                                                    ).let { mvInspectionReportDTO ->
                                                        KotlinLogging.logger { }
                                                            .info { "mvInspectionReportDTO = $mvInspectionReportDTO" }
//                                                                    cdInspectionGeneralEntity.cdItemDetails?.cdDocId?.assignedInspectionOfficer?.let { it1 -> commonDaoServices.sendEmailWithUserEntity(it1, applicationMapProperties.mapMVReportApprovedNotification, mvInspectionReportDTO, map, sr) }
                                                    }
                                                }
                                            } else {
                                                cdInspectionGeneralEntity.inspectionReportDisapprovalComments =
                                                    mvInspectionChecklist.inspectionReportDisapprovalComments
                                                cdInspectionGeneralEntity.inspectionReportDisapprovalDate =
                                                    commonDaoServices.getCurrentDate()
                                                daoServices.updateCdInspectionGeneralChecklistInDB(
                                                    cdInspectionGeneralEntity,
                                                    loggedInUser
                                                ).let {
                                                    val payload =
                                                        "Motor Inspection Report Disapproved [inspectionReportApprovalStatus= ${cdInspectionGeneralEntity.inspectionReportApprovalStatus}, inspectionReportDisapprovalComments= ${cdInspectionGeneralEntity.inspectionReportDisapprovalComments}]"
                                                    val sr = commonDaoServices.mapServiceRequestForSuccess(
                                                        map,
                                                        payload,
                                                        loggedInUser
                                                    )
                                                    //Create DTO & Send email
                                                    val mvInspectionReportDTO = createMvInspectionReportNotificationDTO(
                                                        cdInspectionGeneralEntity,
                                                        inspectionMotorVehicle
                                                    )
//                                                                cdInspectionGeneralEntity.cdItemDetails?.cdDocId?.assignedInspectionOfficer?.let { it1 -> commonDaoServices.sendEmailWithUserEntity(it1, applicationMapProperties.mapMVReportDissaprovedNotification, mvInspectionReportDTO, map, sr) }
                                                }
                                            }
                                        }
                                    }
                                }
                                return "$motorVehicleInspectionDetailsPage=${inspectionMotorVehicle.inspectionGeneral?.cdItemDetails?.id}&docType=${daoServices.motorVehicleInspectionDetailsName}"
                            } ?: throw ExpectedDataNotFound("No Motor Vehicle Inspection Checklist Found")
                    }
            }
    }

    fun createMvInspectionReportNotificationDTO(
        cdInspectionGeneralEntity: CdInspectionGeneralEntity,
        inspectionMotorVehicle: CdInspectionMotorVehicleItemChecklistEntity
    ):
            MvInspectionNotificationDTO {
        val mvInspectionNotificationDTO = MvInspectionNotificationDTO()
        with(mvInspectionNotificationDTO) {
            baseUrl = applicationMapProperties.baseUrlValue
            id = cdInspectionGeneralEntity.cdItemDetails?.id
            fullName = cdInspectionGeneralEntity.cdItemDetails?.cdDocId?.assignedInspectionOfficer?.let { it1 ->
                commonDaoServices.concatenateName(it1)
            }
            date =
                if (cdInspectionGeneralEntity.inspectionReportApprovalDate != null) cdInspectionGeneralEntity.inspectionReportApprovalDate else cdInspectionGeneralEntity.inspectionReportDisapprovalDate
            chassisNumber = inspectionMotorVehicle.chassisNo
            comments =
                if (cdInspectionGeneralEntity.inspectionReportApprovalComments != null) cdInspectionGeneralEntity.inspectionReportApprovalComments else cdInspectionGeneralEntity.inspectionReportDisapprovalComments
        }
        return mvInspectionNotificationDTO
    }

    @GetMapping("/coc-certificate/view")
    fun downloadCocCertificateFile(response: HttpServletResponse, @RequestParam("cocId") cocId: Long) {
        daoServices.findCOCById(cocId)?.let { coc ->
            coc.localCocFile?.let { file ->
                //Create FileDTO Object
                val fileDto: CommonDaoServices.FileDTO = CommonDaoServices.FileDTO()
                fileDto.document = file
                fileDto.fileType = "application/x-pdf"
                fileDto.name = coc.localCocFileName

                commonDaoServices.downloadFile(response, fileDto)
            }
        }
    }

    @GetMapping("/cor-certificate/view")
    fun downloadCorCertificateFile(response: HttpServletResponse, @RequestParam("corId") corId: Long) {
        daoServices.findCorById(corId)?.let { cor ->
            cor.localCorFile?.let { file ->
                //Create FileDTO Object
                val fileDto: CommonDaoServices.FileDTO = CommonDaoServices.FileDTO()
                fileDto.document = file
                fileDto.fileType = "application/x-pdf"
                fileDto.name = cor.localCorFileName

                commonDaoServices.downloadFile(response, fileDto)
            }
        }
    }
}
