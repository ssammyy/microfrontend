package org.kebs.app.kotlin.apollo.api.controllers.qaControllers


import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.QADaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.QualityAssuranceDaoServices
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
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
import org.springframework.web.servlet.function.paramOrNull
import org.springframework.web.servlet.mvc.support.RedirectAttributes


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
        model: Model,
        result: BindingResult)
    : String? {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val permitType = qaDaoServices.findPermitType(permitTypeID)
        val permitApplied = qaDaoServices.permitSave(permit, permitType, loggedInUser,map)

        return "${qaDaoServices.permitDetails}=${permitApplied.id}&userID=${loggedInUser.id}"
    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @PostMapping("/apply/update-permit")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updatePermitDetails(
        @ModelAttribute("permit") permit: PermitApplicationsEntity,
        @RequestParam( "permitID") permitID: Long,
        model: Model,
        result: BindingResult)
    : String? {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        val permitDetails = loggedInUser.id?.let { qaDaoServices.findPermitBYUserIDAndId(permitID, it) }?: throw ExpectedDataNotFound("User Id required")

        //updating of Details in DB
        val updatedCDDetails = qaDaoServices.permitUpdateDetails(commonDaoServices.updateDetails(permit, permitDetails) as PermitApplicationsEntity, loggedInUser)

        return "${qaDaoServices.permitDetails}=${permit.id}"
    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @PostMapping("/apply/new-sta3")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun saveNewSta3(
        @RequestParam( "permitID") permitID: Long,
        @ModelAttribute("QaSta3Entity") QaSta3Entity: QaSta3Entity,
        model: Model,
        result: BindingResult)
    : String? {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val permit = loggedInUser.id?.let { qaDaoServices.findPermitBYUserIDAndId(permitID, it) }?: throw ExpectedDataNotFound("User Id required")
        permit.id?.let { qaDaoServices.sta3NewSave(it, QaSta3Entity, loggedInUser,map) }

        val updatePermit  = PermitApplicationsEntity()
        with(updatePermit){
           sta3FilledStatus = map.activeStatus
        }
        //updating of Details in DB
        val updatedCDDetails = qaDaoServices.permitUpdateDetails(commonDaoServices.updateDetails(permit, updatePermit) as PermitApplicationsEntity, loggedInUser)

        return "${qaDaoServices.permitDetails}=${permit.id}&userID=${loggedInUser.id}"
    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @PostMapping("/apply/new-sta10")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun saveNewSta10(
        @RequestParam( "permitID") permitID: Long,
        @ModelAttribute("QaSta10Entity") QaSta10Entity: QaSta10Entity,
        model: Model,
        result: BindingResult)
    : String? {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val permit = loggedInUser.id?.let { qaDaoServices.findPermitBYUserIDAndId(permitID, it) }?: throw ExpectedDataNotFound("User Id required")
        permit.id?.let { qaDaoServices.sta10NewSave(it, QaSta10Entity, loggedInUser,map) }

        val updatePermit  = PermitApplicationsEntity()
        with(updatePermit){
           sta10FilledStatus = map.activeStatus
        }
        //updating of Details in DB
        val updatedCDDetails = qaDaoServices.permitUpdateDetails(commonDaoServices.updateDetails(permit, updatePermit) as PermitApplicationsEntity, loggedInUser)

        return "${qaDaoServices.permitDetails}=${permit.id}&userID=${loggedInUser.id}"
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
    @GetMapping("/new-permit-submit")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun submitPermit(
        @RequestParam( "permitID") permitID: Long,
        model: Model)
    : String? {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        val result: ServiceRequestsEntity?

        var permit = loggedInUser.id?.let { qaDaoServices.findPermitBYUserIDAndId(permitID, it) }?: throw ExpectedDataNotFound("Required User ID, check config")
        val permitType = permit.permitType?.let { qaDaoServices.findPermitType(it) }?: throw ExpectedDataNotFound("PermitType Id Not found")
        result = qaDaoServices.permitInvoiceCalculation(map, loggedInUser, permit, permitType)
        with(permit){
            sendApplication = map.activeStatus
            invoiceGenerated = map.activeStatus

        }
        permit = qaDaoServices.permitUpdateDetails(permit, loggedInUser)

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permit.id}%26userID=${loggedInUser.id}"
        sm.message = "You have successful Submitted Your Application, an invoice has been generated, check Your permit detail"

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