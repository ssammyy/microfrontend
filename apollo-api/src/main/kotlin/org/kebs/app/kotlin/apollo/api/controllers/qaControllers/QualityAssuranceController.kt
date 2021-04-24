package org.kebs.app.kotlin.apollo.api.controllers.qaControllers


import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.QADaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.QualityAssuranceDaoServices
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.di.ConsignmentDocumentDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.qa.PermitApplicationsEntity
import org.kebs.app.kotlin.apollo.store.model.qa.QaSta3Entity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*


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
    fun saveNewPermit(
        @ModelAttribute("permit") permit: PermitApplicationsEntity,
        @RequestParam( "permitTypeID") permitTypeID: Long,
        model: Model,
        result: BindingResult)
    : String? {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val permitType = qaDaoServices.findPermitType(permitTypeID)
            qaDaoServices.permitSave(permit, permitType, loggedInUser,map)
        return "${qaDaoServices.permitList}=$permitTypeID"
    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @PostMapping("/apply/update-permit")
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

        return "${qaDaoServices.permitDetails}=${permit.id}"
    }

}