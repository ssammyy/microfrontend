package org.kebs.app.kotlin.apollo.api.controllers.stdController


import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.FilePurposeAnnexService
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.ReferenceMaterialJustificationService
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.RelevantDocumentsNWIService
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.StandardRequestService
import org.kebs.app.kotlin.apollo.common.dto.std.*
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.std.StandardJustificationRepository
import org.kebs.app.kotlin.apollo.store.repo.std.StandardNWIRepository
import org.kebs.app.kotlin.apollo.store.repo.std.StandardRequestRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.util.*
import java.util.stream.Collectors

@RestController
@RequestMapping("api/v1/migration")
class StandardRequestController(
    val standardRequestService: StandardRequestService,
    val purposeAnnexService: FilePurposeAnnexService,
    val relevantDocumentsNWIService: RelevantDocumentsNWIService,
    val referenceMaterialJustificationService: ReferenceMaterialJustificationService,
    private val standardRequestRepository: StandardRequestRepository,
    private val standardNWIRepository: StandardNWIRepository,
    private val standardJustificationRepository: StandardJustificationRepository,


    ) {

    //********************************************************** deployment endpoints **********************************************************
    @PostMapping("/anonymous/standard/deploy")
    fun deployWorkflow(): ServerResponse {
        standardRequestService.deployProcessDefinition()
        return ServerResponse(HttpStatus.OK, "Successfully deployed server", HttpStatus.OK)
    }

    //********************************************************** deployment endpoints **********************************************************
//    @PostMapping("/anonymous/standard/delete")
//    @ResponseBody
//    fun delete(@RequestBody id: ID): ServerResponse {
//        println("ID passed"+id)
//        standardRequestService.deleteTask(id)
//        return ServerResponse(
//            HttpStatus.OK,
//            "Successfully returned process history",
//            HttpStatus.OK
//
//        )
//    }


    //Delete A process

    @PostMapping("/anonymous/standard/close")
    fun clos(@RequestBody responseMessage: ResponseMessage) {
        return standardRequestService.closeProcess(responseMessage.message)
    }

    //Delete A Task
    @PostMapping("/anonymous/standard/closetask")
    fun closeTask(@RequestBody responseMessage: ResponseMessage) {
        return standardRequestService.closeTask(responseMessage.message)
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
        return standardRequestService.getProducts(technicalCommitteeId?.toLong())
    }

    @GetMapping("/anonymous/standard/getStandardRequest")
    @ResponseBody
    fun getStandardRequests(): String {
        return standardRequestService.generateSRNumber("ENG")
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
        return standardRequestService.getProductCategories(productId?.toLong())
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
        return standardRequestService.getDepartments()
    }

    @GetMapping("/standard/getLiaisonOrganizations")
    @ResponseBody
    fun getLiaisonOrganization(): MutableList<LiaisonOrganization> {
        return standardRequestService.getLiaisonOrganization()
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
        return standardRequestService.getTechnicalCommittee(departmentId?.toLong())
    }


    @GetMapping("/anonymous/standard/getTechnicalCommitteeName/{tcId}")
    @ResponseBody
    fun getTechnicalCommitteeName(@PathVariable("tcId") tcId: String?): String {
        return standardRequestService.getTechnicalCommitteeName(tcId?.toLong())
    }

    @GetMapping("/anonymous/standard/getAllTcsForApplication")
    fun getAllTcsForApplication(): List<DataHolder> {
        return standardRequestService.getAllTcs()
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

        return ServerResponse(HttpStatus.OK, "Upload new work item", standardRequestService.uploadNWI(standardNWI))
    }

    @GetMapping("standard/getTCTasks")
    fun getTCTasks(): List<TaskDetails> {
        return standardRequestService.getTCTasks()
    }

    @GetMapping("standard/getAllNwis")
    fun getAllNwiS(): List<StandardNWI> {
        return standardRequestService.getAllNwiSUnderVote()
    }

    @GetMapping("standard/getAllNwisLoggedInUserToVoteFor")
    fun getAllNwisLoggedInUserToVoteFor(): List<StandardNWI> {
        return standardRequestService.getAllNwisLoggedInUserToVoteFor()
    }

    @GetMapping("standard/getAllApprovedNwiS")
    fun getAllApprovedNwiS(): List<StandardNWI> {
        return standardRequestService.getAllNwiSApproved()
    }

    @GetMapping("standard/getAllNwiSApprovedForJustification")
    fun getAllNwiSApprovedForJustification(): List<StandardNWI> {
        return standardRequestService.getAllNwiSApprovedForJustification()
    }


    @GetMapping("standard/getAllRejectedNwiS")
    fun getAllRejectedNwiS(): List<StandardNWI> {
        return standardRequestService.getAllNwiSRejected()
    }


    @PostMapping("standard/decisionOnNWI")
    @ResponseBody
    fun decisionOnNWI(@RequestBody voteOnNWI: VoteOnNWI): ServerResponse {
        return (standardRequestService.decisionOnNWI(voteOnNWI))
    }

    @PostMapping("standard/reVoteOnNWI")
    @ResponseBody
    fun reVoteOnNWI(@RequestBody voteOnNWI: VoteOnNWI): ServerResponse {
        return (standardRequestService.reVoteOnNWI(voteOnNWI))
    }

    @GetMapping("standard/getUserLoggedInBallots")
    fun getUserLoggedInBallots(): List<VotesDto> {
        return standardRequestService.getUserLoggedInBallots()
    }

    @GetMapping("standard/getAllVotesTally")
    fun getAllVotesTally(): List<NwiVotesTally> {
        return standardRequestService.getAllVotesTally()
    }

    @GetMapping("standard/getAllVotesOnNwi")
    fun getAllVotesOnNwi(@RequestParam("nwiId") nwiId: Long): List<VotesWithNWIId> {
        return standardRequestService.getAllVotesOnNwi(nwiId)
    }

    @PostMapping("standard/approveNwi")
    fun approveNwi(@RequestParam("nwiId") nwiId: Long)
            : ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "New Work Item Approved.",
            standardRequestService.approveNWI(nwiId)
        )
    }

    @PostMapping("standard/rejectNwi")
    fun rejectNwi(@RequestParam("nwiId") nwiId: Long)
            : ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "New Work Item Rejected.",
            standardRequestService.rejectNWI(nwiId)
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

    @GetMapping("standard/getJustificationsPendingDecision")
    fun getJustificationsPendingDecision(): List<StandardJustification> {
        return standardRequestService.getJustificationsPendingDecision()
    }

    @GetMapping("standard/getApprovedJustifications")
    fun getApprovedJustifications(): List<StandardJustification> {
        return standardRequestService.getApprovedJustifications()
    }

    @GetMapping("standard/getAllMyJustifications")
    fun getAllMyJustifications(): List<StandardJustification> {
        return standardRequestService.getAllMyJustifications()
    }


    @GetMapping("standard/getRejectedJustifications")
    fun getRejectedJustifications(): List<StandardJustification> {
        return standardRequestService.getRejectedJustifications()
    }

    @GetMapping("standard/getRejectedAmendmentJustifications")
    fun getRejectedAmendmentJustifications(): List<StandardJustification> {
        return standardRequestService.getRejectedAmendmentJustifications()
    }

    @GetMapping("standard/getJustificationByNwiId")
    fun getJustificationByNwiId(
        @RequestParam("nwiId") nwiId: Long,

        ): List<StandardJustification> {
        return standardRequestService.getJustificationByNwiId(nwiId)
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
        var message: String?



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

    @PostMapping("standard/createDepartment")
    @ResponseBody
    fun createDepartment(@RequestBody department: Department): ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Upload Department",
            standardRequestService.createStandardsDepartment(department)
        )
    }

    @PostMapping("standard/createTechnicalCommittee")
    @ResponseBody
    fun createTechnicalCommittee(@RequestBody technicalCommittee: TechnicalCommittee): ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Upload Technical Committee",
            standardRequestService.createTechnicalCommittee(technicalCommittee)
        )
    }

    @PostMapping("standard/createProductCategory")
    @ResponseBody
    fun createProductCategory(@RequestBody product: Product): ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Upload Product Category",
            standardRequestService.createProductCategory(product)
        )
    }

    @PostMapping("standard/createProductSubCategory")
    @ResponseBody
    fun createProductSubCategory(@RequestBody productSubCategory: ProductSubCategory): ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Upload Department",
            standardRequestService.createProductSubCategory(productSubCategory)
        )
    }

    @GetMapping("standard/getAllDepartments")
    fun getAllDepartments(): MutableList<Department> {
        return standardRequestService.getAllDepartments()
    }

    @GetMapping("standard/getAllTcs")
    fun getAllTechnicalCommittees(): List<DataHolder> {
        return standardRequestService.getAllTcs()
    }


    @GetMapping("standard/getAllProductCategories")
    fun getAllProductCategories(): List<DataHolder> {
        return standardRequestService.getAllProductCategories()
    }

    @GetMapping("standard/getAllProductSubCategories")
    fun getAllProductSubCategories(): List<DataHolder> {
        return standardRequestService.getAllProductSubCategories()
    }


    @PostMapping("standard/updateDepartment")
    @ResponseBody
    fun updateDepartment(@RequestBody department: Department): ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Updated Department",
            standardRequestService.createStandardsDepartment(department)
        )
    }

    @PostMapping("standard/updateDepartmentStandardRequest")
    @ResponseBody
    fun updateDepartmentStandardRequest(@RequestBody standardRequest: StandardRequest): ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Updated Department For Request",
            standardRequestService.updateDepartmentStandardRequest(standardRequest)
        )
    }

    @GetMapping("standard/getAllTcSec")
    fun getAllTcSec(): List<UsersEntity> {
        return standardRequestService.getAllTcSec()
    }

    @GetMapping("standard/getAllStds")
    fun getAllStandards(): List<StandardsDto> {
        return standardRequestService.getAllStandardRequests()
    }

    @GetMapping("standard/getAllStdsForNwi")
    fun getAllStdsForNwi(): List<StandardsDto> {
        return standardRequestService.getAllStandardRequestsToPrepareNWI()
    }

    @GetMapping("standard/getAllRejectedStdsForNwi")
    fun getAllRejectedStdsForNwi(): List<StandardsDto> {
        return standardRequestService.getAllRejectedStandardRequestsToPrepareNWI()
    }

    @GetMapping("standard/getAllOnHoldStdsForNwi")
    fun getAllOnHoldStdsForNwi(): List<StandardsDto> {
        return standardRequestService.getAllOnHoldStandardRequestsToPrepareNWI()
    }

    @PostMapping("standard/deleteDepartment")
    @ResponseBody
    fun deleteDepartment(@RequestBody department: Department): ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Deleted Department",
            standardRequestService.deleteDepartment(department.id)
        )
    }


    @PostMapping("/anonymous/standard/file-upload")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadFilesDraftStandard(
        @RequestParam("requestId") requestId: Long,
        @RequestParam("requesterName") requesterName: String,
        @RequestParam("docFile") docFile: List<MultipartFile>,
        @RequestParam("type") type: String,

        model: Model
    ): CommonDaoServices.MessageSuccessFailDTO {

        var docDescription: String;

        val application =
            standardRequestRepository.findByIdOrNull(requestId) ?: throw Exception("APPLICATION DOES NOT EXIST")

        docFile.forEach { u ->
            val upload = DatKebsSdStandardsEntity()
            with(upload) {
                sdDocumentId = application.id
                documentTypeDef = type

            }
            docDescription = "$requesterName AdditionalDetails"

            standardRequestService.uploadSDFileNotLoggedIn(
                upload,
                u,
                "UPLOADS",
                requesterName,
                docDescription

            )
        }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.message = "Document Uploaded successfully"

        return sm
    }

    @GetMapping("standard/getAdditionalDocuments")
    fun getAdditionalDocuments(
        @RequestParam("standardId") standardId: Long,
    ): Collection<DatKebsSdStandardsEntity?>? {
        return standardRequestService.getDocuments(standardId)
    }


    @GetMapping("standard/getAdditionalDocumentsByProcess")
    fun getAdditionalDocumentsByProcess(
        @RequestParam("standardId") standardId: Long,
        @RequestParam("processName") processName: String,

        ): Collection<DatKebsSdStandardsEntity?>? {
        return standardRequestService.getDocumentsByProcessName(standardId, processName)
    }


    @GetMapping("standard/getANwiById")
    fun getANwiById(
        @RequestParam("nwiId") nwiId: Long,

        ): List<StandardNWI> {
        return standardRequestService.getANwiById(nwiId)
    }

    @GetMapping("standard/getDepartmentById")
    @ResponseBody
    fun getDepartment(@RequestParam("departmentId") departmentId: String): MutableList<Department> {
        return standardRequestService.getDepartment(departmentId.toLong())
    }

    @GetMapping("standard/getJustificationDecisionById")
    fun getJustificationDecisionById(
        @RequestParam("justificationId") justificationId: Long,

        ): List<DecisionJustification> {
        return standardRequestService.getJustificationDecisionById(justificationId)
    }

    @GetMapping("standard/getRequestById")
    fun getRequestById(
        @RequestParam("requestId") requestId: Long,

        ): List<StandardsDto> {
        return standardRequestService.getRequestById(requestId)
    }


    //NWI Upload Documents
    @PostMapping("/standard/uploadNWIDocs")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadDocs(
        @RequestParam("nwiId") nwiId: String,
        @RequestParam("docFile") docFile: List<MultipartFile>,
        @RequestParam("type") type: String,
        @RequestParam("docDescription") docDescription: String,

        model: Model
    ): CommonDaoServices.MessageSuccessFailDTO {


        val nwi = standardNWIRepository.findByIdOrNull(nwiId.toLong())
            ?: throw Exception("APPLICATION DOES NOT EXIST")
        docFile.forEach { u ->
            val upload = DatKebsSdStandardsEntity()
            with(upload) {
                sdDocumentId = nwi.id
                documentTypeDef = type

            }

            standardRequestService.uploadSDFileCommittee(
                upload,
                u,
                "NWI Documents",
                nwi.id,
                docDescription

            )
        }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.message = "Documents Uploaded successfully"

        return sm
    }


    //Justification Upload Documents
    @PostMapping("/standard/uploadJustificationDocs")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadJustificationDocs(
        @RequestParam("justificationId") nwiId: String,
        @RequestParam("docFile") docFile: List<MultipartFile>,
        @RequestParam("type") type: String,
        @RequestParam("docDescription") docDescription: String,

        model: Model
    ): CommonDaoServices.MessageSuccessFailDTO {


        val justification = standardJustificationRepository.findByIdOrNull(nwiId.toLong())
            ?: throw Exception("APPLICATION DOES NOT EXIST")
        docFile.forEach { u ->
            val upload = DatKebsSdStandardsEntity()
            with(upload) {
                sdDocumentId = justification.id
                documentTypeDef = type

            }

            standardRequestService.uploadSDFileCommittee(
                upload,
                u,
                "Justification Documents",
                justification.id,
                docDescription

            )
        }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.message = "Documents Uploaded successfully"

        return sm
    }


}
