package org.kebs.app.kotlin.apollo.api.controllers.stdController


import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.*
import org.kebs.app.kotlin.apollo.common.dto.std.*
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.util.stream.Collectors

@RestController
@RequestMapping("api/v1/migration")
class StandardRequestController(
    val standardRequestService: StandardRequestService,
    val purposeAnnexService: FilePurposeAnnexService,
    val relevantDocumentsNWIService: RelevantDocumentsNWIService,
    val referenceMaterialJustificationService: ReferenceMaterialJustificationService
) {

    //********************************************************** deployment endpoints **********************************************************
    @PostMapping("/anonymous/standard/deploy")
    fun deployWorkflow(): ServerResponse {
        standardRequestService.deployProcessDefinition()
        return ServerResponse(HttpStatus.OK, "Successfully deployed server", HttpStatus.OK)
    }

    //********************************************************** process upload standard request **********************************************************
    @PostMapping("/anonymous/standard/dropdown/request")
    @ResponseBody
    fun requestForStandard(@RequestBody standardRequest: StandardRequest): ServerResponse {
        //return ResponseEntity(standardRequestService.requestForStandard(standardRequest), HttpStatus.OK)
        return ServerResponse(
            HttpStatus.OK,
            "Successfully uploaded standard request",
            standardRequestService.requestForStandard(standardRequest)
        )
    }

    @GetMapping("/anonymous/standard/dropdown/getProducts/{technicalCommitteeId}")
    @ResponseBody
    fun getProducts(@PathVariable("technicalCommitteeId") technicalCommitteeId: String?): MutableList<Product> {
        return standardRequestService.getProducts(technicalCommitteeId?.toLong());
    }

    @GetMapping("/anonymous/standard/getStandardRequest")
    @ResponseBody
    fun getStandardRequests(): String {
        return standardRequestService.generateSRNumber("ENG");
    }

    /* @GetMapping("/getProducts")
     @ResponseBody
     fun getProducts(): ServerResponse
     {
          return ServerResponse(HttpStatus.OK,"Successfully returned products",standardRequestService.getProducts());
     }*/

    @GetMapping("/anonymous/standard/dropdown/getProductCategories/{productId}")
    @ResponseBody
    fun getProductCategories(@PathVariable("productId") productId: String?): MutableList<ProductSubCategory> {
        return standardRequestService.getProductCategories(productId?.toLong());
    }

    /*@PostMapping("/getProductCategories")
    @ResponseBody
    fun getProductCategories(@RequestBody id: ID): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Successfully returned product categories",standardRequestService.getProductCategories(id));
    }*/


    @GetMapping("/anonymous/standard/dropdown/getDepartments")
    @ResponseBody
    fun getDepartments(): MutableList<Department> {
        return standardRequestService.getDepartments();
    }

    @GetMapping("/anonymous/standard/getLiaisonOrganizations")
    @ResponseBody
    fun getLiaisonOrganization(): MutableList<LiaisonOrganization> {
        return standardRequestService.getLiaisonOrganization();
    }

    /*@GetMapping("/getDepartments")
    @ResponseBody
    fun getDepartments(): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Successfully returned departments",standardRequestService.getDepartments());
    }*/

    @GetMapping("/anonymous/standard/dropdown/getTechnicalCommittee/{departmentId}")
    @ResponseBody
    fun getTechnicalCommittee(@PathVariable("departmentId") departmentId: String?): MutableList<TechnicalCommittee> {
        return standardRequestService.getTechnicalCommittee(departmentId?.toLong());
    }


    @GetMapping("/anonymous/standard/getTechnicalCommitteeName/{tcId}")
    @ResponseBody
    fun getTechnicalCommitteeName(@PathVariable("tcId") tcId: String?): String {
        return standardRequestService.getTechnicalCommitteeName(tcId?.toLong());
    }

    /*@GetMapping("/getTechnicalCommittee")
    @ResponseBody
    fun getTechnicalCommittee(): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Successfully returned technical committee",standardRequestService.getTechnicalCommittee());
    }*/

    @GetMapping("standard/getHOFTasks")
    fun getHOFTasks(): List<TaskDetails> {
        return standardRequestService.getHOFTasks()
    }

    @PostMapping("standard/process")
    @ResponseBody
    fun checkState(@RequestBody id: ID): ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Successfully returned process history",
            standardRequestService.checkProcessHistory(id)
        )
    }

    @PostMapping("standard/hof/review")
    @ResponseBody
    fun hofReview(@RequestBody hofFeedback: HOFFeedback): ServerResponse {
        println(hofFeedback)
        return ServerResponse(
            HttpStatus.OK,
            "Successfully completed HOF review",
            standardRequestService.hofReview(hofFeedback)
        )
    }

    @GetMapping("standard/getTCSECTasks")
    fun getTCSECTasks(): List<TaskDetails> {
        return standardRequestService.getTCSECTasks()
    }


    @PostMapping("standard/uploadNWI")
    @ResponseBody
    fun uploadNWI(@RequestBody standardNWI: StandardNWI): ServerResponse {
        println("hereeeeee")
        print(standardNWI)
        return ServerResponse(HttpStatus.OK, "Upload new work item", standardRequestService.uploadNWI(standardNWI))
    }

    @GetMapping("standard/getTCTasks")
    fun getTCTasks(): List<TaskDetails> {
        return standardRequestService.getTCTasks()
    }


    @PostMapping("standard/decisionOnNWI")
    @ResponseBody
    fun decisionOnNWI(@RequestBody voteOnNWI: VoteOnNWI): ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Decision on New Work Item by the TC",
            standardRequestService.decisionOnNWI(voteOnNWI)
        )
    }


    @GetMapping("standard/tc-sec/tasks")
    fun getTCSecTasks(): List<TaskDetails> {
        return standardRequestService.getTCSecTasks()
    }

    @PostMapping("standard/uploadJustification")
    @ResponseBody
    fun uploadJustification(@RequestBody standardJustification: StandardJustification): ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Successfully uploaded justification by the TC",
            standardRequestService.uploadJustification(standardJustification)
        )
    }

    @GetMapping("standard/spc-sec/tasks")
    fun getSPCSecTasks(): List<TaskDetails> {
        return standardRequestService.getSPCSecTasks()
    }

    @PostMapping("standard/decisionOnJustification")
    @ResponseBody
    fun decisionOnJustification(@RequestBody decisionJustification: DecisionJustification): ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Submitted decision on justification",
            standardRequestService.decisionOnJustification(decisionJustification)
        )
    }

    @GetMapping("standard/tc-sec/tasks/workplan")
    fun getTCSecTasksWorkPlan(): List<TaskDetails> {
        return standardRequestService.getTCSecTasksWorkPlan()
    }

    @PostMapping("standard/uploadWorkPlan")
    @ResponseBody
    fun uploadWorkPlan(@RequestBody standardWorkPlan: StandardWorkPlan): ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Successfully uploaded workplan",
            standardRequestService.uploadWorkPlan(standardWorkPlan)
        )
    }

    @PostMapping("standard/uploadFiles")
    fun uploadFiles(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("itemId") itemId: String,
        @RequestParam("type") type: String
    ): ResponseEntity<ResponseMessage> {
        var message: String? = null



        return try {
            if (type == "FilePurposeAnnex") {
                val newItemId = itemId.replace("/", ".")
                purposeAnnexService.store(file, newItemId)
            } else if (type == "RelevantDocumentsNWI") {
                val newItemId = itemId.replace("/", ".")
                relevantDocumentsNWIService.store(file, newItemId)
            } else if (type == "ReferenceMaterialJustification") {
                val newItemId = itemId.replace("/", ".")
                referenceMaterialJustificationService.store(file, newItemId)
            }

            message = "Uploaded the file successfully: ${file.originalFilename}"
            ResponseEntity.status(HttpStatus.OK).body(ResponseMessage(message))
        } catch (e: Exception) {
            message = "Could not upload the file: ${file.originalFilename}!"
            ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(ResponseMessage(message))
        }
    }


    @GetMapping("standard/list/files/{type}/{itemId}")
    fun getListFiles(
        @PathVariable type: String?,
        @PathVariable("itemId") itemId: String
    ): ResponseEntity<List<ResponseFile>> {

        var files: List<ResponseFile>? = null
        if (type == "FilePurposeAnnex") {

            files = purposeAnnexService.getAllFiles(itemId).map { dbFile ->
                val fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/standard/files/")
                    .path(dbFile.id)
                    .toUriString()

                ResponseFile(
                    dbFile.name,
                    fileDownloadUri,
                    dbFile.type,
                    dbFile.data.size
                )

            }.collect(Collectors.toList())
        } else if (type == "RelevantDocumentsNWI") {

            files = relevantDocumentsNWIService.getAllFiles(itemId).map { dbFile ->
                val fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/standard/files/")
                    .path(dbFile.id)
                    .toUriString()

                ResponseFile(
                    dbFile.name,
                    fileDownloadUri,
                    dbFile.type,
                    dbFile.data.size
                )

            }.collect(Collectors.toList())
        } else if (type == "ReferenceMaterialJustification") {

            files = referenceMaterialJustificationService.getAllFiles(itemId).map { dbFile ->
                val fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/standard/files/")
                    .path(dbFile.id)
                    .toUriString()

                ResponseFile(
                    dbFile.name,
                    fileDownloadUri,
                    dbFile.type,
                    dbFile.data.size
                )

            }.collect(Collectors.toList())
        }


        return ResponseEntity.status(HttpStatus.OK).body(files)
    }

    @GetMapping("standard/files/{id}")
    fun getFile(@PathVariable id: String?): ResponseEntity<ByteArray?>? {
        val fileDB = purposeAnnexService.getFile(id!!)
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB!!.name + "\"")
            .body(fileDB.data)
    }

}
