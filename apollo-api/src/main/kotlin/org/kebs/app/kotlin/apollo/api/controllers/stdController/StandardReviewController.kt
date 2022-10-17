package org.kebs.app.kotlin.apollo.api.controllers.stdController

import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.StandardReviewFormService
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.StandardReviewService
import org.kebs.app.kotlin.apollo.common.dto.std.*
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.util.stream.Collectors


@RestController
@RequestMapping("api/v1/migration/standard_review")
class StandardReviewController(
    val standardReviewService: StandardReviewService,
    val standardReviewFormService: StandardReviewFormService
    ) {

    //********************************************************** deployment endpoints **********************************************************
    @PostMapping("/deploy")
    fun deployWorkflow(): ServerResponse {
        standardReviewService.deployProcessDefinition()
        return ServerResponse(HttpStatus.OK,"Successfully deployed server", HttpStatus.OK)
    }

    @PreAuthorize("hasAuthority('TC_SEC_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/getStandardsForReview")
    @ResponseBody
    fun getStandardsForReview(): MutableList<ReviewStandards>
    {
        return standardReviewService.getStandardsForReview()
    }

    //********************************************************** Start Review Process **********************************************************
    @PreAuthorize("hasAuthority('TC_SEC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/standardReviewForm")
    @ResponseBody
    fun standardReviewForm(@RequestBody standardReview: StandardReview): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Successfully Uploaded Form",standardReviewService.standardReviewForm(standardReview))
    }

    @PreAuthorize("hasAuthority('TC_SEC_SD_READ') or hasAuthority('SPC_SEC_SD_READ')" +
            " or hasAuthority('STAKEHOLDERS_SD_READ') or hasAuthority('SAC_SEC_SD_READ')" +
            " or hasAuthority('TC_SEC_SD_READ') or hasAuthority('HOP_SD_READ') or" +
            " hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')  ")
    @GetMapping("/getUserTasks")
    fun getUserTasks():List<StandardReviewTasks>
    {
        return standardReviewService.getUserTasks()
    }

   // @PreAuthorize("hasAuthority('TC_SEC_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/getStandardsProposalForComment")
    @ResponseBody
    fun getStandardsProposalForComment(): MutableList<ReviewStandards>
    {
        return standardReviewService.getStandardsProposalForComment()
    }

    //@PreAuthorize("hasAuthority('STAKEHOLDERS_SD_MODIFY')")
    @PostMapping("/SubmitAPComments")
    @ResponseBody
    fun submitAPComments(@RequestBody standardReviewProposalComments: StandardReviewProposalComments): ServerResponse{
        return ServerResponse(HttpStatus.OK,"Comment Has been submitted",standardReviewService.submitAPComments(standardReviewProposalComments))
    }

    @GetMapping("/getStandardsProposalComments")
    fun getStandardsProposalComments(@RequestParam("proposalId") proposalId: Long):MutableIterable<ReviewStandards>?
    {
        return standardReviewService.getStandardsProposalComments(proposalId)
    }

    //@PreAuthorize("hasAuthority('STAKEHOLDERS_SD_MODIFY')")
    @PostMapping("/makeRecommendationsOnAdoptionProposal")
    @ResponseBody
    fun makeRecommendationsOnAdoptionProposal(@RequestBody standardReviewProposalRecommendations: StandardReviewProposalRecommendations): ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Comment Has been submitted",
            standardReviewService.makeRecommendationsOnAdoptionProposal(standardReviewProposalRecommendations)
        )
    }

    //decision on Adoption Recommendation
    // @PreAuthorize("hasAuthority('TC_SEC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
//    @PostMapping("/decisionOnRecommendation")
//    fun decisionOnRecommendation(@RequestBody iSDecision: ISDecision,
//                                 internationalStandardRemarks: InternationalStandardRemarks) : List<StandardReviewTasks>
//    {
//        return standardReviewService.decisionOnRecommendation(iSDecision,internationalStandardRemarks)
//    }

    //Level Two Decision
    @PostMapping("/levelUpDecisionOnRecommendations")
    fun levelUpDecisionOnRecommendations(
        @RequestBody iSDecision: ISDecision,
        internationalStandardRemarks: InternationalStandardRemarks,
        standard: Standard
    ): List<StandardReviewTasks> {
        return standardReviewService.levelUpDecisionOnRecommendations(
            iSDecision,
            internationalStandardRemarks,
            standard
        )
    }



    //********************************************************** Submit Review Comments **********************************************************
    //@PreAuthorize("hasAuthority('STAKEHOLDERS_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/updateGazette")
    @ResponseBody
    fun updateGazette(@RequestBody internationalStandardRemarks: InternationalStandardRemarks,
                      standard:Standard,iSDecision: ISDecision): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Successfully Submitted Comments",standardReviewService.updateGazette(internationalStandardRemarks,standard,iSDecision))
    }

    @PostMapping("/updateGazettementDate")
    @ResponseBody
    fun updateGazettementDate(@RequestBody nWAGazettement: NWAGazettement,iSDecision: ISDecision): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Successfully Submitted Comments",standardReviewService.updateGazettementDate(nWAGazettement,iSDecision))
    }



    //********************************************************** Make Recommendations **********************************************************
    @PreAuthorize("hasAuthority('TC_SEC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/reviewRecommendations")
    @ResponseBody
    fun reviewRecommendations(@RequestBody standardReviewRecommendations: StandardReviewRecommendations): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Successfully Submitted Recommendations",standardReviewService.reviewRecommendations(standardReviewRecommendations))
    }



    //decision on Adoption Recommendation
    @PreAuthorize("hasAuthority('SPC_SEC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/decisionOnRecommendation")
    fun decisionOnRecommendation(@RequestBody standardReviewRecommendations: StandardReviewRecommendations)
    {
        standardReviewService.decisionOnRecommendation(standardReviewRecommendations)
    }



    //decision on Adoption Recommendation
    @PreAuthorize("hasAuthority('SAC_SEC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/decisionOfSac")
    fun decisionOfSac(@RequestBody standardReviewRecommendations: StandardReviewRecommendations)
    {
        standardReviewService.decisionOfSac(standardReviewRecommendations)
    }



    @PostMapping("/upload")
    fun uploadFile(@RequestParam("file") file: MultipartFile,
                   @RequestParam("itemId") itemId: String,
                   @RequestParam("type") type: String): ResponseEntity<ResponseMessage>
    {
        var message: String? = null



        return try{
            if(type == "StandardReviewForm")
            {
                val newItemId = itemId.replace("/",".")
                standardReviewFormService.store(file,newItemId)
            }


            message ="Uploaded the file successfully: ${file.originalFilename}"
            ResponseEntity.status(HttpStatus.OK).body(ResponseMessage(message))
        }
        catch (e: Exception){
            message ="Could not upload the file: ${file.originalFilename}!"
            ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(ResponseMessage(message))
        }
    }


//    @PostMapping("/upload")
//    fun uploadFile(@RequestParam("file") file: MultipartFile): ResponseEntity<ResponseMessage?>? {
//        var message = ""
//        return try {
//            standardReviewFormService.store(file)
//            message = "Uploaded the file successfully: " + file.originalFilename
//            ResponseEntity.status(HttpStatus.OK).body(ResponseMessage(message))
//        } catch (e: java.lang.Exception) {
//            message = "Could not upload the file: " + file.originalFilename + "!"
//            ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(ResponseMessage(message))
//        }
//    }
    //********************************************************** Upload File **********************************************************

//    @GetMapping("/files")
//    fun getListFiles(@PathVariable type: String?,@PathVariable("itemId") itemId: String): ResponseEntity<List<ResponseFile>> {
//
//        var files: List<ResponseFile>? = null
//
//        if(type == "StandardReviewForm")
//        {
//
//            files = standardReviewFormService.getAllFiles(itemId).map { dbFile ->
//                val fileDownloadUri = ServletUriComponentsBuilder
//                    .fromCurrentContextPath()
//                    .path("/files/")
//                    .path(dbFile.id)
//                    .toUriString()
//
//                ResponseFile(
//                    dbFile.name,
//                    fileDownloadUri,
//                    dbFile.type,
//                    dbFile.data.size)
//
//            }.collect(Collectors.toList())
//        }
//
//
//        return ResponseEntity.status(HttpStatus.OK).body(files)
//    }
//
//    @GetMapping("/files/{id}")
//    fun getFile(@PathVariable id: String?): ResponseEntity<ByteArray?>? {
//        val fileDB = standardReviewFormService.getFile(id!!)
//        return ResponseEntity.ok()
//            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB!!.name + "\"")
//            .body(fileDB.data)
//    }
    @GetMapping("/files")
    fun getListFiles(): ResponseEntity<List<ResponseFile>> {
        val files = standardReviewFormService.getAllFiles()?.map { dbFile ->
            val fileDownloadUri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/files/")
                .path(dbFile!!.id)
                .toUriString()

            ResponseFile(
                dbFile.name,
                fileDownloadUri,
                dbFile.type,
                dbFile.data.size)

        }?.collect(Collectors.toList())
        return ResponseEntity.status(HttpStatus.OK).body(files)
    }




    @GetMapping("/files/{id}")
    fun getFile(@PathVariable id: String?): ResponseEntity<ByteArray?>? {
        val fileDB = standardReviewFormService.getFile(id!!)
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB!!.name + "\"")
            .body(fileDB.data)
    }





}
