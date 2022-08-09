package org.kebs.app.kotlin.apollo.api.controllers.stdController

import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.*
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

    //********************************************************** Save Standard **********************************************************
    @PostMapping("/saveStandard")
    @ResponseBody
    fun saveStandard(@RequestBody standard: Standard): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Successfully saved Standard",standardReviewService.saveStandard(standard))
    }

    //**********************************************************Get Standards**********************************************************
    @PreAuthorize("hasAuthority('TC_SEC_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/reviewedStandards")
    @ResponseBody
    fun reviewedStandards(): MutableList<Standard>
    {
        return standardReviewService.reviewedStandards();
    }

    //********************************************************** Start Review Process **********************************************************
    @PreAuthorize("hasAuthority('TC_SEC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/standardReviewForm")
    @ResponseBody
    fun standardReviewForm(@RequestBody standardReview: StandardReview): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Successfully Uploaded Form",standardReviewService.standardReviewForm(standardReview))
    }

    //********************************************************** get Stakeholders Tasks **********************************************************
    @PreAuthorize("hasAuthority('STAKEHOLDERS_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/getReviewForms")
    fun getReviewForms():List<TaskDetails>
    {
        return standardReviewService.getReviewForms()
    }

    //********************************************************** Submit Review Comments **********************************************************
    @PreAuthorize("hasAuthority('STAKEHOLDERS_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/commentsOnReview")
    @ResponseBody
    fun commentsOnReview(@RequestBody standardReviewComments: StandardReviewComments): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Successfully Submitted Comments",standardReviewService.commentsOnReview(standardReviewComments))
    }

    //********************************************************** get TSC SEC Tasks **********************************************************
    @PreAuthorize("hasAuthority('TC_SEC_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/getReviewTasks")
    fun getReviewTasks():List<TaskDetails>
    {
        return standardReviewService.getReviewTasks()
    }

    //********************************************************** Make Recommendations **********************************************************
    @PreAuthorize("hasAuthority('TC_SEC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/reviewRecommendations")
    @ResponseBody
    fun reviewRecommendations(@RequestBody standardReviewRecommendations: StandardReviewRecommendations): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Successfully Submitted Recommendations",standardReviewService.reviewRecommendations(standardReviewRecommendations))
    }

    //********************************************************** get SPC SEC Tasks **********************************************************
    @PreAuthorize("hasAuthority('SPC_SEC_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/getRecommendations")
    fun getRecommendations():List<TaskDetails>
    {
        return standardReviewService.getRecommendations()
    }

    //decision on Adoption Recommendation
    @PreAuthorize("hasAuthority('SPC_SEC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/decisionOnRecommendation")
    fun decisionOnRecommendation(@RequestBody standardReviewRecommendations: StandardReviewRecommendations)
    {
        standardReviewService.decisionOnRecommendation(standardReviewRecommendations)
    }

    //********************************************************** get SAC SEC Tasks **********************************************************
    @PreAuthorize("hasAuthority('SAC_SEC_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/getSacList")
    fun getSacList():List<TaskDetails>
    {
        return standardReviewService.getSacList()
    }

    //decision on Adoption Recommendation
    @PreAuthorize("hasAuthority('SAC_SEC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/decisionOfSac")
    fun decisionOfSac(@RequestBody standardReviewRecommendations: StandardReviewRecommendations)
    {
        standardReviewService.decisionOfSac(standardReviewRecommendations)
    }

    //********************************************************** get HOP Tasks **********************************************************
    @PreAuthorize("hasAuthority('HOP_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/getPublishingTasks")
    fun getPublishingTasks():List<TaskDetails>
    {
        return standardReviewService.getPublishingTasks()
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
