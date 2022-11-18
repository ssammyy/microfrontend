package org.kebs.app.kotlin.apollo.api.controllers.stdController

import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.NationalWorkshopAgreementService
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.NwaDiJustificationFileService
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.NwaJustificationFileService
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.StandardReviewFormService
import org.kebs.app.kotlin.apollo.common.dto.std.*
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.std.*
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("api/v1/migration/sd/wa")
class NationalWorkshopAgreementController(
    private val nwaService: NationalWorkshopAgreementService,
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

    @PreAuthorize("hasAuthority('TC_SEC_SD_MODIFY') or hasAuthority('KNW_SEC_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/prepareJustification")
    @ResponseBody
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun prepareJustification(@RequestBody nwaJustificationDto: NwaJustificationDto): ServerResponse {
        val nwaJustification= NWAJustification().apply {
            knw=nwaJustificationDto.knw
            dateOfMeeting=nwaJustificationDto.dateOfMeeting
            knwSecretary=nwaJustificationDto.knwSecretary
            sl=nwaJustificationDto.sl
            requestedBy=nwaJustificationDto.requestedBy
            issuesAddressed=nwaJustificationDto.issuesAddressed
            acceptanceDate=nwaJustificationDto.acceptanceDate
            referenceMaterial=nwaJustificationDto.referenceMaterial
            department=nwaJustificationDto.department
            remarks=nwaJustificationDto.remarks
            requestNumber=nwaJustificationDto.requestNumber
        }
        return ServerResponse(HttpStatus.OK,"Successfully uploaded Justification",nwaService.
        prepareJustification(nwaJustification))
    }

    @GetMapping("/getNwaJustification")
    @ResponseBody
    fun getNwaJustification(): MutableList<KnwaJustification>
    {
        return nwaService.getNwaJustification()
    }

    // SPC SEC decision on Justification
    @PreAuthorize("hasAuthority('SPC_SEC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/decisionOnJustification")
    fun decisionOnJustification(@RequestBody nwaJustificationAction: NwaJustificationAction
    ) : ServerResponse
    {
        val nwaJustification= NWAJustification().apply {
            accentTo=nwaJustificationAction.accentTo
            id=nwaJustificationAction.justificationID
        }
        val standardNwaRemarks= StandardNwaRemarks().apply {
            justificationID=nwaJustificationAction.justificationID
            remarks=nwaJustificationAction.comments
        }

        return ServerResponse(HttpStatus.OK,"Decision",nwaService.decisionOnJustification(nwaJustification,standardNwaRemarks))

    }

    @GetMapping("/getApprovedJustification")
    @ResponseBody
    fun getApprovedJustification(): MutableList<KnwaJustification>
    {
        return nwaService.getApprovedJustification()
    }

    @PreAuthorize("hasAuthority('TC_SEC_SD_MODIFY') or hasAuthority('KNW_SEC_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/preparePreliminaryDraft")
    @ResponseBody
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun preparePreliminaryDraft(@RequestBody preliminaryDraftDTO: PreliminaryDraftDTO): ServerResponse {
        val nwaPreliminaryDraft= NWAPreliminaryDraft().apply {
            title=preliminaryDraftDTO.title
            scope=preliminaryDraftDTO.scope
            normativeReference=preliminaryDraftDTO.normativeReference
            symbolsAbbreviatedTerms=preliminaryDraftDTO.symbolsAbbreviatedTerms
            clause=preliminaryDraftDTO.clause
            special=preliminaryDraftDTO.special
            justificationNumber=preliminaryDraftDTO.justificationNumber
            workShopDate=preliminaryDraftDTO.workShopDate
        }
        return ServerResponse(HttpStatus.OK,"Successfully uploaded Preliminary Draft",nwaService.
        preparePreliminaryDraft(nwaPreliminaryDraft))
    }

    @GetMapping("/getPreliminaryDraft")
    @ResponseBody
    fun getPreliminaryDraft(): MutableList<NwaPDraft>
    {
        return nwaService.getPreliminaryDraft()
    }

    // SPC SEC decision on Justification
    @PreAuthorize("hasAuthority('SPC_SEC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/decisionOnPreliminaryDraft")
    fun decisionOnPreliminaryDraft(@RequestBody nwaPDraftAction: NwaPDraftAction
    ) : ServerResponse
    {
        val nwaPreliminaryDraft= NWAPreliminaryDraft().apply {
            accentTo=nwaPDraftAction.accentTo
            id=nwaPDraftAction.preliminaryDraftID
            title=nwaPDraftAction.title
            scope=nwaPDraftAction.scope
            normativeReference=nwaPDraftAction.normativeReference
            symbolsAbbreviatedTerms=nwaPDraftAction.symbolsAbbreviatedTerms
            clause=nwaPDraftAction.clause
            special=nwaPDraftAction.special
        }
        val standardNwaRemarks= StandardNwaRemarks().apply {
            justificationID=nwaPDraftAction.justificationID
            remarks=nwaPDraftAction.comments
        }

        return ServerResponse(HttpStatus.OK,"Decision",nwaService.decisionOnPreliminaryDraft(nwaPreliminaryDraft,standardNwaRemarks))

    }

    @GetMapping("/getRejectedPreliminaryDraft")
    @ResponseBody
    fun getRejectedPreliminaryDraft(): MutableList<NwaPDraft>
    {
        return nwaService.getRejectedPreliminaryDraft()
    }



}