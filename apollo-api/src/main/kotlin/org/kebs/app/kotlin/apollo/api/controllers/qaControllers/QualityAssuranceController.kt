package org.kebs.app.kotlin.apollo.api.controllers.qaControllers


import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.QADaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.QualityAssuranceDaoServices
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.ServiceMapNotFoundException
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.di.ConsignmentDocumentDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.qa.*
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.function.paramOrNull
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.servlet.http.HttpServletResponse


@Controller
@RequestMapping("/api/qa")
class QualityAssuranceController(
    private val applicationMapProperties: ApplicationMapProperties,
    private val qaDaoServices: QADaoServices,
    private val commonDaoServices: CommonDaoServices,
) {

    final val appId: Int = applicationMapProperties.mapQualityAssurance

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @PostMapping("/apply/new-permit")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun saveNewPermit(
        @ModelAttribute("permit") permit: PermitApplicationsEntity,
        @RequestParam( "permitTypeID") permitTypeID: Long,
        model: Model)
    : String? {
        val result: ServiceRequestsEntity?
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val permitType = qaDaoServices.findPermitType(permitTypeID)

        result = qaDaoServices.permitSave(permit, permitType, loggedInUser,map)

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${result.varField1}%26userID=${loggedInUser.id}"
        sm.message = "You have Successful Filled STA 1 , Complete your application"

        return commonDaoServices.returnValues(result, map, sm)
    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @PostMapping("/apply/new-scheme-of-supervision")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun saveNewSchemeOfSupervision(
        @ModelAttribute("QaSchemeForSupervisionEntity") QaSchemeForSupervisionEntity: QaSchemeForSupervisionEntity,
        @RequestParam( "permitID") permitID: Long,
        model: Model)
    : String? {
        val result: ServiceRequestsEntity?
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        //Find Permit with permit ID
        val permitDetails = qaDaoServices.findPermitBYID(permitID)

        result = qaDaoServices.newSchemeSupervisionSave(permitDetails, QaSchemeForSupervisionEntity, loggedInUser,map)
        //Set scheme as generated and update results
        permitDetails.generateSchemeStatus = map.activeStatus
        qaDaoServices.permitUpdateDetails(permitDetails,map, loggedInUser)


        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/scheme-of-supervision?permitID=${result.varField1}%26userID=${loggedInUser.id}"
        sm.message = "You have Successful Filled SSC"

        return commonDaoServices.returnValues(result, map, sm)
    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @PostMapping("/apply/update-scheme-of-supervision")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updateSchemeOfSupervision(
        @ModelAttribute("schemeFound") schemeFound: QaSchemeForSupervisionEntity,
        @RequestParam( "schemeID") schemeID: Long,
        model: Model)
    : String? {
        val result: ServiceRequestsEntity?
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        result = qaDaoServices.schemeSupervisionUpdateSave(schemeID, schemeFound, loggedInUser,map)

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/scheme-of-supervision?permitID=${result.varField1}%26userID=${loggedInUser.id}"
        sm.message = "You have Successful UPDATED SSC"

        return commonDaoServices.returnValues(result, map, sm)
    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_MANAGER_ASSESSORS_READ') or hasAuthority('QA_HOF_READ') or hasAuthority('QA_HOD_READ') or hasAuthority('QA_OFFICER_MODIFY')")
    @PostMapping("/apply/update-permit")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updatePermitDetails(
        @ModelAttribute("permit") permit: PermitApplicationsEntity,
        @RequestParam( "permitID") permitID: Long,
        model: Model)
    : String? {


        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        val result: ServiceRequestsEntity?

        //Find Permit with permit ID
        val permitDetails = qaDaoServices.findPermitBYID(permitID)

        //Add Permit ID THAT was Fetched so That it wont create a new record while updating with the methode
        permit.id = permitDetails.id

        //Check If the attached plant details is added
        when (permit.attachedPlantId) {
            0L -> {
                throw ServiceMapNotFoundException("Please select A Plant Details")
            }
            //updating of Details in DB
            else -> {
                result = qaDaoServices.permitUpdateDetails(commonDaoServices.updateDetails(permit, permitDetails) as PermitApplicationsEntity, map, loggedInUser)

                val sm = CommonDaoServices.MessageSuccessFailDTO()
                sm.closeLink =
                    "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitDetails.id}%26userID=${loggedInUser.id}"
                sm.message = "${permit.description}"

                return commonDaoServices.returnValues(result, map, sm)
            }
        }

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @PostMapping("kebs/add/plant-details/save")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun addManufacturePlantDetails(
        model: Model,
        @ModelAttribute("manufacturePlantDetails") manufacturePlantDetails: ManufacturePlantDetailsEntity,
        results: BindingResult,
        redirectAttributes: RedirectAttributes
    ): String? {

        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        val result: ServiceRequestsEntity?

        result = qaDaoServices.addPlantDetailsManufacture(manufacturePlantDetails,map, loggedInUser)

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/user/user-profile"
        sm.message = "Plant with the following building [Name = ${manufacturePlantDetails.buildingName}] was added sucessfull"

        return commonDaoServices.returnValues(result, map, sm)
    }


    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @PostMapping("/apply/new-sta3")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun saveNewSta3(
        @RequestParam( "permitID") permitID: Long,
        @ModelAttribute("QaSta3Entity") QaSta3Entity: QaSta3Entity,
        model: Model)
    : String? {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val permit = loggedInUser.id?.let { qaDaoServices.findPermitBYUserIDAndId(permitID, it) }?: throw ExpectedDataNotFound("User Id required")
        permit.id?.let { qaDaoServices.sta3NewSave(it, QaSta3Entity, loggedInUser,map) }

        val result: ServiceRequestsEntity?

        val updatePermit  = PermitApplicationsEntity()
        with(updatePermit){
            id = permit.id
           sta3FilledStatus = map.activeStatus
        }
        //updating of Details in DB
        result = qaDaoServices.permitUpdateDetails(commonDaoServices.updateDetails(permit, updatePermit) as PermitApplicationsEntity,map, loggedInUser)

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitID}%26userID=${loggedInUser.id}"
        sm.message = "You have Successful Filled STA 3 and has been submitted sucessful , Submit your application"

        return  commonDaoServices.returnValues(result, map, sm)
    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @PostMapping("/apply/new-sta10")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun saveNewSta10(
        @RequestParam( "permitID") permitID: Long,
        @ModelAttribute("QaSta10Entity") QaSta10Entity: QaSta10Entity,
        model: Model)
    : String? {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val permit = loggedInUser.id?.let { qaDaoServices.findPermitBYUserIDAndId(permitID, it) }?: throw ExpectedDataNotFound("User Id required")
        permit.id?.let { qaDaoServices.sta10NewSave(it, QaSta10Entity, loggedInUser,map) }

        val result: ServiceRequestsEntity?

        val updatePermit  = PermitApplicationsEntity()
        with(updatePermit){
            id = permit.id
           sta10FilledStatus = map.activeStatus
        }
        //updating of Details in DB
        result = qaDaoServices.permitUpdateDetails(commonDaoServices.updateDetails(permit, updatePermit) as PermitApplicationsEntity, map,loggedInUser)

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/view-sta10?permitID=${permitID}%26userID=${loggedInUser.id}"
        sm.message = "You have Successful Filled Some part of STA 10, Processed To finish the Rest and submit"

        return  commonDaoServices.returnValues(result, map, sm)
    }


    @PreAuthorize("hasAuthority('QA_MANAGER_ASSESSORS_MODIFY') or hasAuthority('QA_OFFICER_MODIFY')")
    @PostMapping("/apply/new-sta10-officer")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun saveNewSta10Officer(
        @RequestParam( "sta10ID")  sta10ID: Long,
        @ModelAttribute("QaSta10Entity") QaSta10Entity: QaSta10Entity,
        model: Model)
    : String? {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        var foundSta10Entity = qaDaoServices.findSta10BYID(sta10ID)
        foundSta10Entity = qaDaoServices.sta10OfficerNewSave(commonDaoServices.updateDetails(foundSta10Entity, QaSta10Entity) as QaSta10Entity, map,loggedInUser)
        val permit = foundSta10Entity.permitId?.let { qaDaoServices.findPermitBYID(it) } ?: throw ExpectedDataNotFound("PERMIT ID ON STA10  with [id=${sta10ID}] is NULL")

        val result: ServiceRequestsEntity?

        val updatePermit  = PermitApplicationsEntity()
        with(updatePermit){
            id = permit.id
            sta10FilledOfficerStatus = map.activeStatus
        }
        //updating of Details in DB
        result = qaDaoServices.permitUpdateDetails(commonDaoServices.updateDetails(permit, updatePermit) as PermitApplicationsEntity, map,loggedInUser)

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/view-sta10?permitID=${permit.id}%26userID=${loggedInUser.id}"
        sm.message = "You have Successful Filled STA 10 Official Part"

        return  commonDaoServices.returnValues(result, map, sm)
    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @PostMapping("/add/new-sta10-product-manufactured")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun saveNewSta10ProductManufactured(
        @RequestParam( "qaSta10ID") qaSta10ID: Long,
        @ModelAttribute("QaProductManufacturedEntity") QaProductManufacturedEntity: QaProductManufacturedEntity,
        model: Model,
        result: BindingResult)
    : String? {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val qaSta10 = qaDaoServices.findSta10BYID(qaSta10ID)
        qaSta10.id?.let { qaDaoServices.sta10ManufactureProductNewSave(it, QaProductManufacturedEntity, loggedInUser,map) }

        return "${qaDaoServices.sta10Details}=${qaSta10.permitId}&userID=${loggedInUser.id}"
    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @PostMapping("/add/new-sta10-raw-materials")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun saveNewSta10RawMaterials(
        @RequestParam( "qaSta10ID") qaSta10ID: Long,
        @ModelAttribute("QaRawMaterialEntity") QaRawMaterialEntity: QaRawMaterialEntity,
        model: Model,
        result: BindingResult)
    : String? {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val qaSta10 = qaDaoServices.findSta10BYID(qaSta10ID)
        qaSta10.id?.let { qaDaoServices.sta10RawMaterialsNewSave(it, QaRawMaterialEntity, loggedInUser,map) }

        return "${qaDaoServices.sta10Details}=${qaSta10.permitId}&userID=${loggedInUser.id}"
    }


    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @PostMapping("/add/new-sta10-machine-plant")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun saveNewSta10MachinePlant(
        @RequestParam( "qaSta10ID") qaSta10ID: Long,
        @ModelAttribute("QaMachineryEntity") QaMachineryEntity: QaMachineryEntity,
        model: Model,
        result: BindingResult)
    : String? {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val qaSta10 = qaDaoServices.findSta10BYID(qaSta10ID)
        qaSta10.id?.let { qaDaoServices.sta10MachinePlantNewSave(it, QaMachineryEntity, loggedInUser,map) }

        return "${qaDaoServices.sta10Details}=${qaSta10.permitId}&userID=${loggedInUser.id}"
    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @PostMapping("/add/new-sta10-manufacturing-process")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun saveNewSta10MachinePlant(
        @RequestParam( "qaSta10ID") qaSta10ID: Long,
        @ModelAttribute("QaManufacturingProcessEntity") QaManufacturingProcessEntity: QaManufacturingProcessEntity,
        model: Model,
        result: BindingResult)
    : String? {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val qaSta10 = qaDaoServices.findSta10BYID(qaSta10ID)
        qaSta10.id?.let { qaDaoServices.sta10ManufacturingProcessNewSave(it, QaManufacturingProcessEntity, loggedInUser,map) }

        return "${qaDaoServices.sta10Details}=${qaSta10.permitId}&userID=${loggedInUser.id}"
    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @PostMapping("kebs/add/new-upload")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadFilesQA(
        @RequestParam( "permitID") permitID: Long,
        @RequestParam( "docFileName") docFileName: String,
        @RequestParam("doc_file") docFile: MultipartFile,
        model: Model)
    : String? {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val permitDetails = loggedInUser.id?.let { qaDaoServices.findPermitBYUserIDAndId(permitID, it) }?: throw ExpectedDataNotFound("User Id required")

        val result: ServiceRequestsEntity?

        result = qaDaoServices.saveQaFileUploads(docFile,docFileName, loggedInUser, map, permitID)

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitDetails.id}%26userID=${loggedInUser.id}"
        sm.message = "You have successful Uploaded the Document with the following [Name = ${docFileName}]"

        return commonDaoServices.returnValues(result, map, sm)
    }

    @GetMapping("/kebs/view/attached")
    fun downloadFileDocument(
        response: HttpServletResponse,
        @RequestParam("fileID") fileID: Long
    ) {
        val fileUploaded = qaDaoServices.findUploadedFileBYId(fileID)
        val mappedFileClass = commonDaoServices.mapClass(fileUploaded)
        commonDaoServices.downloadFile(response, mappedFileClass)
    }


    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @GetMapping("/new-permit-submit")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun submitPermit(
        @RequestParam( "permitID") permitID: Long,
        model: Model)
    : String? {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        var result: ServiceRequestsEntity?

        val permit = loggedInUser.id?.let { qaDaoServices.findPermitBYUserIDAndId(permitID, it) }?: throw ExpectedDataNotFound("Required User ID, check config")
        val permitType = permit.permitType?.let { qaDaoServices.findPermitType(it) }?: throw ExpectedDataNotFound("PermitType Id Not found")
//       val fmarkGenerated =
        result = qaDaoServices.permitInvoiceCalculation(map, loggedInUser, permit, permitType)
        with(permit){
            sendApplication = map.activeStatus
            invoiceGenerated = map.activeStatus
            when {
                permit.permitType!! == applicationMapProperties.mapQAPermitTypeIDDmark -> {
                    hodId = qaDaoServices.assignNextOfficerAfterPayment(permit, map,applicationMapProperties.mapQADesignationIDForHODId)?.id
                }
                permit.permitType!! == applicationMapProperties.mapQAPermitTypeIdSmark -> {
                    qamId = qaDaoServices.assignNextOfficerAfterPayment(permit, map,applicationMapProperties.mapQADesignationIDForQAMId)?.id
                }
            }

        }
        result = qaDaoServices.permitUpdateDetails(permit, map, loggedInUser)

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permit.id}%26userID=${loggedInUser.id}"
        sm.message = "You have successful Submitted Your Application, an invoice has been generated, check Your permit detail and pay for the Invoice"

        return commonDaoServices.returnValues(result, map, sm)
    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @GetMapping("/kebs/mpesa-stk-push")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun payPermitWithMpesa(
        @RequestParam( "permitID") permitID: Long,
        @RequestParam( "phoneNumber") phoneNumber: String,
        model: Model)
    : String? {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        val result: ServiceRequestsEntity?

        val permit = loggedInUser.id?.let { qaDaoServices.findPermitBYUserIDAndId(permitID, it) }?: throw ExpectedDataNotFound("Required User ID, check config")
        val invoiceEntity = qaDaoServices.findPermitInvoiceByPermitID(permitID, loggedInUser.id!!)
        result = qaDaoServices.permitInvoiceSTKPush(map, loggedInUser, phoneNumber, invoiceEntity)

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permit.id}%26userID=${loggedInUser.id}"
        sm.message = "Check You phone for an STK Push,If You can't see the push either pay with Bank or Normal Mpesa service"

        return commonDaoServices.returnValues(result, map, sm)
    }

}