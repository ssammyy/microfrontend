package org.kebs.app.kotlin.apollo.api.controllers.stdController

import com.google.gson.Gson
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.*
import org.kebs.app.kotlin.apollo.api.ports.provided.makeAnyNotBeNull
import org.kebs.app.kotlin.apollo.common.dto.std.*
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.std.NationalEnquiryEntityRepository
import org.kebs.app.kotlin.apollo.store.repo.std.NationalEnquiryPointRepository
import org.kebs.app.kotlin.apollo.store.repo.std.SdNepDocUploadsEntityRepository
import org.kebs.app.kotlin.apollo.store.repo.std.SdNepDraftRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletResponse


@RestController
@RequestMapping("api/v1/migration")
class NationalEnquiryPointController(
    val nationalEnquiryPointService: NationalEnquiryPointService,
    private val nationalEnquiryPointRepository: NationalEnquiryPointRepository,
    private val sdNepDocUploads: SdNepDocUploadsEntityRepository,
    private val nationalEnquiryEntityRepository: NationalEnquiryEntityRepository,
    private val commonDaoServices: CommonDaoServices,
    private val nepDraftRepo: SdNepDraftRepository,
) {

    //********************************************************** deployment endpoints **********************************************************

    @PostMapping("/anonymous/National_enquiry_point/deploy")
    fun deployWorkflow() {
        nationalEnquiryPointService.deployProcessDefinition()
    }

    //********************************************************** process endpoints **********************************************************

    @PostMapping("/anonymous/National_enquiry_point/notification_request")
    fun notificationRequest(@RequestBody nep: NepRequestsDto): ServerResponse? {
        return ServerResponse(
            HttpStatus.OK,"Successfully uploaded Justification",nationalEnquiryPointService.
            notificationRequest(nep))

    }

    @PostMapping("/anonymous/National_enquiry_point/nepDocUpload")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadNepDocument(
        @RequestParam("enquiryId") enquiryId: Long,
        @RequestParam("docFile") docFile: List<MultipartFile>,
        model: Model
    ): CommonDaoServices.MessageSuccessFailDTO {


       // val nepRequest = nationalEnquiryPointRepository.findByIdOrNull(enquiryId)?: throw Exception("ENQUIRY ID DOES NOT EXIST")

        docFile.forEach { u ->
            val upload = SdNepDocumentUploadsEntity()
            with(upload) {
                nepDocumentId = enquiryId

            }
            nationalEnquiryPointService.uploadNepDocument(
                upload,
                u,
                "UPLOADS",
                "NEP Request",
                "Request Document"
            )
        }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.message = "Document Uploaded successfully"

        return sm
    }

    @GetMapping("/National_enquiry_point/getNepRequests")
    @ResponseBody
    fun getNepRequests(): MutableList<NationalEnquiryPointEntity>
    {
        return nationalEnquiryPointService.getNepRequests()
    }

    @PostMapping("/anonymous/National_enquiry_point/uploadNepDoc")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadNepDoc(
        @RequestParam("enquiryId") enquiryId: Long,
        @RequestParam("docFile") docFile: List<MultipartFile>,
        model: Model
    ): CommonDaoServices.MessageSuccessFailDTO {


        val nepRequest = nationalEnquiryEntityRepository.findByIdOrNull(enquiryId)?: throw Exception("REQUEST ID DOES NOT EXIST")

        docFile.forEach { u ->
            val upload = SdNepDocUploadsEntity()
            with(upload) {
                nepDocumentId = nepRequest.id

            }
            nationalEnquiryPointService.uploadNepDoc(
                upload,
                u,
                "UPLOADS",
                "NEP Request",
                "Request Document"
            )
        }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.message = "Document Uploaded successfully"

        return sm
    }

    //View NEP Enquiry Document
    @GetMapping("/National_enquiry_point/viewRequestUpload")
    fun viewPDFile(
        response: HttpServletResponse,
        @RequestParam("enquiryId") enquiryId: Long
    ) {

        val fileUploaded = nationalEnquiryPointService.findUploadedReportFileBYId(enquiryId)
        val fileDoc = commonDaoServices.mapClass(fileUploaded)
        response.contentType = "application/pdf"
//                    response.setHeader("Content-Length", pdfReportStream.size().toString())
        response.addHeader("Content-Disposition", "inline; filename=${fileDoc.name}")
        response.outputStream
            .let { responseOutputStream ->
                responseOutputStream.write(fileDoc.document?.let { makeAnyNotBeNull(it) } as ByteArray)
                responseOutputStream.close()
            }

        KotlinLogging.logger { }.info("VIEW FILE SUCCESSFUL")

    }

    @GetMapping("/anonymous/National_enquiry_point/getNepDivisionRequests")
    @ResponseBody
    fun getNepDivisionRequests(@RequestParam("enquiryId") enquiryId: Long): MutableList<NationalEnquiryEntity>
    {
        return nationalEnquiryPointService.getNepDivisionRequests(enquiryId)
    }


    @PostMapping("/anonymous/National_enquiry_point/responseOnEnquiryInfo")
    fun responseOnEnquiryInfo(@RequestBody nep: DivResponseDto): ServerResponse? {
        return ServerResponse(
            HttpStatus.OK,"Successfully uploaded Justification",nationalEnquiryPointService.
            responseOnEnquiryInfo(nep))

    }

    @GetMapping("/National_enquiry_point/getNepDivisionResponse")
    @ResponseBody
    fun getNepDivisionResponse(): MutableList<NationalEnquiryEntity>
    {
        return nationalEnquiryPointService.getNepDivisionResponse()
    }


    @PostMapping("/National_enquiry_point/decisionOnEnquiryInfo")
    fun decisionOnEnquiryInfo(@RequestBody nepInfoCheckDto: NepInfoCheckDto
    ) : ServerResponse
    {

        return ServerResponse(
            HttpStatus.OK,"Saved",nationalEnquiryPointService.
            decisionOnEnquiryInfo(nepInfoCheckDto))
    }

    @PostMapping("/National_enquiry_point/sendFeedBack")
    fun sendFeedBack(@RequestBody nepInfoCheckDto: NepInfoCheckDto
    ) : ServerResponse
    {

        return ServerResponse(
            HttpStatus.OK,"Saved",nationalEnquiryPointService.
            sendFeedBack(nepInfoCheckDto))
    }

    //Retrieves requests made by users on the front end
    @GetMapping("/National_enquiry_point/nep_officer/tasks")
    fun getTasks(): List<TaskDetails> {
        return nationalEnquiryPointService.getManagerTasks() as List<TaskDetails>
    }

    @PostMapping("/National_enquiry_point/nep_officer/is_available")
    fun approveTask(@RequestBody infoCheckHandler: InfoCheckHandler) {
        nationalEnquiryPointService.informationAvailable(infoCheckHandler.taskId, infoCheckHandler.isAvailable)
    }

    @PostMapping("/National_enquiry_point/user/submit_enquiry")
    fun submitEnquiry(@RequestBody taskIdManager: TaskIdManager) {
        nationalEnquiryPointService.closeTask(taskIdManager.taskId)
    }

    @GetMapping("/National_enquiry_point/process/{processId}")
    fun checkState(@PathVariable("processId") processId: String?) {
        nationalEnquiryPointService.checkProcessHistory(processId)
    }

    @PostMapping("/National_enquiry_point/information_available/send_email")
    fun sendEmailInfoAvailable(@RequestBody informationTracker: InformationTracker) {
        nationalEnquiryPointService.sendEmailInfoAvailable(informationTracker, informationTracker.taskId)
    }

    @GetMapping("/National_enquiry_point/division/tasks")
    fun getDivisionTasks(): List<TaskDetails> {
        return nationalEnquiryPointService.getDepartmentTasks() as List<TaskDetails>
    }

    @PostMapping("/National_enquiry_point/division_response/send_response")
    fun divisionResponse(@RequestBody departmentResponse: DepartmentResponse) {
        nationalEnquiryPointService.departmentOrganizationResponse(departmentResponse, departmentResponse.taskId)
    }

    @PostMapping("/National_enquiry_point/notificationOfReview")
    fun notificationOfReview(@RequestBody nep: NepNotificationDto): ServerResponse? {
        val gson = Gson()
        KotlinLogging.logger { }.info { "INVOICE CALCULATED" + gson.toJson(nep) }
        return ServerResponse(
            HttpStatus.OK,"Successfully uploaded Review",nationalEnquiryPointService.
            notificationOfReview(nep))

    }

    @GetMapping("/National_enquiry_point/getDraftNotification")
    @ResponseBody
    fun getDraftNotification(): MutableList<NepNotificationFormEntity>
    {
        return nationalEnquiryPointService.getDraftNotification()
    }

    @PostMapping("/National_enquiry_point/uploadNepDraftDoc")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadNepDraftDoc(
        @RequestParam("draftId") draftId: Long,
        @RequestParam("docFile") docFile: List<MultipartFile>,
        model: Model
    ): CommonDaoServices.MessageSuccessFailDTO {


        val nepRequest = nepDraftRepo.findByIdOrNull(draftId)?: throw Exception("DRAFT ID DOES NOT EXIST")

        docFile.forEach { u ->
            val upload = SdNepDraftUploadsEntity()
            with(upload) {
                nepDraftId = nepRequest.id

            }
            nationalEnquiryPointService.uploadNepDraftDoc(
                upload,
                u,
                "UPLOADS",
                "NEP Request",
                "Request Document"
            )
        }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.message = "Document Uploaded successfully"

        return sm
    }

    @GetMapping("/National_enquiry_point/viewDraftUpload")
    fun viewDraftUpload(
        response: HttpServletResponse,
        @RequestParam("draftId") draftId: Long
    ) {

        val fileUploaded = nationalEnquiryPointService.findUploadedDraftBYId(draftId)
        val fileDoc = commonDaoServices.mapClass(fileUploaded)
        response.contentType = "application/pdf"
        response.addHeader("Content-Disposition", "inline; filename=${fileDoc.name}")
        response.outputStream
            .let { responseOutputStream ->
                responseOutputStream.write(fileDoc.document?.let { makeAnyNotBeNull(it) } as ByteArray)
                responseOutputStream.close()
            }

        KotlinLogging.logger { }.info("VIEW FILE SUCCESSFUL")

    }

    @PostMapping("/National_enquiry_point/decisionOnReviewDraft")
    fun decisionOnReviewDraft(@RequestBody nep: NepDraftDecisionDto
    ) : ServerResponse
    {

        return ServerResponse(
            HttpStatus.OK,"Saved",nationalEnquiryPointService.
            decisionOnReviewDraft(nep))
    }

    @GetMapping("/National_enquiry_point/getNotificationForApproval")
    @ResponseBody
    fun getNotificationForApproval(): MutableList<NepNotificationFormEntity>
    {
        return nationalEnquiryPointService.getNotificationForApproval()
    }

    @PostMapping("/National_enquiry_point/decisionOnNotification")
    fun decisionOnNotification(@RequestBody nep: NepDraftDecisionDto
    ) : ServerResponse
    {

        return ServerResponse(
            HttpStatus.OK,"Saved",nationalEnquiryPointService.
            decisionOnNotification(nep))
    }

    @GetMapping("/National_enquiry_point/getDraftNotificationForUpload")
    @ResponseBody
    fun getDraftNotificationForUpload(): MutableList<NepNotificationFormEntity>
    {
        return nationalEnquiryPointService.getDraftNotificationForUpload()
    }

    @PostMapping("/National_enquiry_point/uploadNotification")
    fun uploadNotification(@RequestBody nep: NepDraftWtoDto): ServerResponse? {
        return ServerResponse(
            HttpStatus.OK,"Successfully uploaded",nationalEnquiryPointService.
            uploadNotification(nep))

    }




}
