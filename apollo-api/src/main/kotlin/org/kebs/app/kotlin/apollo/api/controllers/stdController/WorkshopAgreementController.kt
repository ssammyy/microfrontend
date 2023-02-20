package org.kebs.app.kotlin.apollo.api.controllers.stdController

import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.NwaDiJustificationFileService
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.NwaJustificationFileService
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.StandardReviewFormService
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.WorkshopAgreementService
import org.kebs.app.kotlin.apollo.common.dto.std.*
import org.kebs.app.kotlin.apollo.store.model.std.*
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
    @PreAuthorize("hasAuthority('TC_SEC_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
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

    @GetMapping("/getJustification")
    @ResponseBody
    fun getJustification(@RequestParam("requestId") requestId: Long): MutableList<NWAJustification>
    {
        return waService.getJustification(requestId)
    }

    @PreAuthorize("hasAuthority('SPC_SEC_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/getWorkshopJustification")
    @ResponseBody
    fun getWorkshopJustification(): MutableList<StandardRequest>
    {
        return waService.getWorkshopJustification()
    }

    @PreAuthorize("hasAuthority('SPC_SEC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/decisionOnJustification")
    fun decisionOnJustification(@RequestBody nwaDecisionOnJustificationDto: NwaDecisionOnJustificationDto
    ) : ServerResponse
    {
        return ServerResponse(
            HttpStatus.OK,"Saved",waService.
            decisionOnJustification(nwaDecisionOnJustificationDto))
    }

    @PreAuthorize("hasAuthority('TC_SEC_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/getWorkshopForPDraft")
    @ResponseBody
    fun getWorkshopForPDraft(): MutableList<StandardRequest>
    {
        return waService.getWorkshopForPDraft()
    }

    //********************************************************** process prepare Preliminary Draft **********************************************************
    @PreAuthorize("hasAuthority('TC_SEC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/preparePreliminaryDraft")
    @ResponseBody
    fun preparePreliminaryDraft(@RequestBody workshopPreliminaryDraft: WorkshopPreliminaryDraft): ServerResponse
    {

        return ServerResponse(HttpStatus.OK,"Successfully uploaded Preliminary Draft",waService.preparePreliminaryDraft(workshopPreliminaryDraft))
    }

    @PreAuthorize("hasAuthority('TC_SEC_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/getWorkShopStdDraft")
    @ResponseBody
    fun getWorkShopStdDraft(): MutableList<ComStdDraft>
    {
        return waService.getWorkShopStdDraft()
    }

    @PreAuthorize("hasAuthority('TC_SEC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/decisionOnStdDraft")
    fun decisionOnStdDraft(@RequestBody workshopAgreementDecisionDto: WorkshopAgreementDecisionDto
    ) : ServerResponse
    {
        return ServerResponse(
            HttpStatus.OK,"Saved",waService.
            decisionOnStdDraft(workshopAgreementDecisionDto))
    }

    @PreAuthorize("hasAuthority('EDITOR_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/getWorkShopStdForEditing")
    @ResponseBody
    fun getWorkShopStdForEditing(): MutableList<ComStandard>
    {
        return waService.getWorkShopStdForEditing()
    }

    @PreAuthorize("hasAuthority('EDITOR_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/submitDraftForEditing")
    @ResponseBody
    fun submitDraftForEditing(@RequestBody isDraftDto: CSDraftDto): ServerResponse
    {
        return ServerResponse(
            HttpStatus.OK,"Saved",waService.
            submitDraftForEditing(isDraftDto))
    }

}