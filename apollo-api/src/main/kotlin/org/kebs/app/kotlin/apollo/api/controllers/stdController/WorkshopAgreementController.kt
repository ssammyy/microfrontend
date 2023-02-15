package org.kebs.app.kotlin.apollo.api.controllers.stdController

import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.NwaDiJustificationFileService
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.NwaJustificationFileService
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.StandardReviewFormService
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.WorkshopAgreementService
import org.kebs.app.kotlin.apollo.common.dto.std.NwaJustificationDto
import org.kebs.app.kotlin.apollo.common.dto.std.ServerResponse
import org.kebs.app.kotlin.apollo.store.model.std.Department
import org.kebs.app.kotlin.apollo.store.model.std.NWAJustification
import org.kebs.app.kotlin.apollo.store.model.std.StandardRequest
import org.kebs.app.kotlin.apollo.store.model.std.TechnicalCommittee
import org.kebs.app.kotlin.apollo.store.repo.std.*
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/migration/wa")
class WorkshopAgreementController(
    private val waService: WorkshopAgreementService,
    private val commonDaoServices: CommonDaoServices,
    private val nwaJustificationRepository: NwaJustificationRepository,
    val standardReviewFormService: StandardReviewFormService,
    val nwaJustificationFileService: NwaJustificationFileService,
    val nwaDiJustificationFileService: NwaDiJustificationFileService,
    val nwadisdtJustificationRepository: NWADISDTJustificationRepository,
    val nwaPreliminaryDraftRepository: NwaPreliminaryDraftRepository,
    val nwaWorkshopDraftRepository: NwaWorkShopDraftRepository,
    val nwaStandardRepository: NwaStandardRepository
) {
    //Get KNW Departments
    @GetMapping("/getKNWDepartments")
    @ResponseBody
    fun getKNWDepartments(): MutableList<Department>
    {
        return waService.getKNWDepartments()
    }

    //Get KNW Committee
    @GetMapping("/getKNWCommittee")
    @ResponseBody
    fun getKNWCommittee(): MutableList<TechnicalCommittee>
    {
        return waService.getKNWCommittee()
    }

    //Get KNW Committee
    @GetMapping("/getWorkshopStandards")
    @ResponseBody
    fun getWorkshopStandards(): MutableList<StandardRequest>
    {
        return waService.getWorkshopStandards()
    }

    //********************************************************** process upload Justification **********************************************************
    @PreAuthorize("hasAuthority('TC_SEC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/prepareJustification")
    @ResponseBody
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun prepareJustification(@RequestBody nwaJustificationDto: NwaJustificationDto): ServerResponse
    {
        return ServerResponse(
            HttpStatus.OK,"Successfully uploaded Justification",waService.
        prepareJustification(nwaJustificationDto))
        //return ServerResponse(HttpStatus.OK,"Successfully uploaded Justification",response)
    }



}