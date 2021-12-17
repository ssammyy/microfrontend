package org.kebs.app.kotlin.apollo.api.controllers.stdController

import com.google.gson.Gson
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.payload.request.JustificationTaskDataDto
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.*
import org.kebs.app.kotlin.apollo.api.ports.provided.makeAnyNotBeNull
import org.kebs.app.kotlin.apollo.common.dto.std.*
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.std.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.util.stream.Collectors
import org.springframework.http.HttpHeaders
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import javax.servlet.http.HttpServletResponse


@RestController
//@CrossOrigin(origins = ["http://localhost:4200"])
@RequestMapping("api/v1/migration/nwa")
class NWAController(val nwaService: NWAService,
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

    //********************************************************** deployment endpoints **********************************************************
    @PostMapping("/deploy")
    fun deployWorkflow(): ServerResponse {
        nwaService.deployProcessDefinition()
        return ServerResponse(HttpStatus.OK,"Successfully deployed server", HttpStatus.OK)
    }

//    //********************************************************** deployment endpoints **********************************************************
//    @PostMapping("/startProcessInstance")
//    fun startProcess(): ServerResponse {
//        nwaService.startProcessInstance()
//        return ServerResponse(HttpStatus.OK,"Successfully Started server", HttpStatus.OK)
//    }

    //Get KNW Departments
    @GetMapping("/getKNWDepartments")
    @ResponseBody
    fun getKNWDepartments(): MutableList<Department>
    {
        return nwaService.getKNWDepartments()
    }

    //Get KNW Committee
    @GetMapping("/getKNWCommittee")
    @ResponseBody
    fun getKNWCommittee(): MutableList<TechnicalCommittee>
    {
        return nwaService.getKNWCommittee()
    }
    //********************************************************** process upload Justification **********************************************************
    @PreAuthorize("hasAuthority('TC_SEC_SD_MODIFY')")
    @PostMapping("/prepareJustification")
    @ResponseBody
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun prepareJustification(@RequestBody nwaJustification: NWAJustification): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Successfully uploaded Justification",nwaService.prepareJustification(nwaJustification))
        //return ServerResponse(HttpStatus.OK,"Successfully uploaded Justification",response)
    }
//    @PostMapping("/prepareJustification")
//    @ResponseBody
//    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
//    fun prepareJustification(
//        @ModelAttribute nwaJustification: NWAJustification,
//        //@RequestParam("nwaJustificationID") nwaJustificationID: Long,
//        @ModelAttribute("docFile") docFile: List<MultipartFile>,
//        model: Model
//    ): ServerResponse{
//        val response = nwaService.prepareJustification(nwaJustification, docFile)
//        return ServerResponse(HttpStatus.OK,"Successfully uploaded Justification",response)
//    }


//    @PreAuthorize("hasAuthority('KNW_SEC_MODIFY')")
    @PostMapping("/file-upload")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadFiles(
        @RequestParam("nwaJustificationID") nwaJustificationID: Long,
        @RequestParam("docFile") docFile: List<MultipartFile>,
        model: Model
    ): CommonDaoServices.MessageSuccessFailDTO {

        val loggedInUser = commonDaoServices.loggedInUserDetails()
          val nwaJustification = nwaJustificationRepository.findByIdOrNull(nwaJustificationID)?: throw Exception("NWA DOCUMENT ID DOES NOT EXIST")

        docFile.forEach { u ->
            val upload = DatKebsSdNwaUploadsEntity()
            with(upload) {
                nwaDocumentId = nwaJustification.id

            }
            nwaService.uploadSDFile(
                upload,
                u,
                "UPLOADS",
                loggedInUser,
                "NWA Justification"
            )
        }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.message = "Document Uploaded successfully"

        return sm
    }

//    ********************************************************** get Justification document **********************************************************
//    @GetMapping("/view/justification")
//    fun downloadJustification(
//        response: HttpServletResponse,
//        @RequestParam("nwaDocumentId") nwaDocumentId: Long
//    ) {
//        val fileUploaded = nwaService.findUploadedFileBYId(nwaDocumentId)
//        val mappedFileClass = commonDaoServices.mapClass(fileUploaded)
//        commonDaoServices.downloadFile(response, mappedFileClass)
//    }

    // ********************************************************** get Justification document **********************************************************
    @GetMapping("/get/justification/{nwaDocumentId}")
    fun getJustification(@PathVariable nwaDocumentId: Long): ResponseEntity<ByteArray> {
        val fileDB: DatKebsSdNwaUploadsEntity? = nwaService.getJustification(nwaDocumentId)

            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB?.name + "\"")
                .body(fileDB?.document)


    }

//    @GetMapping("/view/justification")
//    fun downloadJustification(
//        response: HttpServletResponse,
//        @RequestParam("nwaDocumentId") nwaDocumentId: Long
//    ) {
//        val fileUploaded = nwaService.findUploadedFileBYId(nwaDocumentId)
//        val fileDoc = fileUploaded?.let { commonDaoServices.mapClass(it) }
//        response.contentType = fileDoc?.fileType
////                    response.setHeader("Content-Length", pdfReportStream.size().toString())
//        response.addHeader("Content-Disposition", "inline; filename=${fileDoc?.name}")
//        response.outputStream
//            .let { responseOutputStream ->
//                responseOutputStream.write(fileDoc?.document as ByteArray)
//                responseOutputStream.close()
//            }
//
//        KotlinLogging.logger { }.info("VIEW FILE SUCCESSFUL")
//
//    }
//@GetMapping("/view/justification")
//@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
//fun downloadJustification(
//    response: HttpServletResponse,
//    @RequestParam("nwaDocumentId") nwaDocumentId: Long
//) {
//    val fileUploaded = nwaService.findUploadedFileBYId(nwaDocumentId)
//    val mappedFileClass = commonDaoServices.mapClass(fileUploaded)
//    commonDaoServices.downloadFile(response, mappedFileClass)
//}
//********************************************************** get KNW Tasks **********************************************************
    @PreAuthorize("hasAuthority('KNW_SEC_READ')")
    @GetMapping("/knwtasks")
    fun getKNWTask():List<TaskDetails>
    {
        return nwaService.getKNWTasks()
    }

    @GetMapping("/view/justification")
    fun viewJustificationFile(
        response: HttpServletResponse,
        @RequestParam("nwaDocumentId") nwaDocumentId: Long
    ) {
        val fileUploaded = nwaService.findUploadedFileBYId(nwaDocumentId)
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

    //KNW decision on Justification
    @PreAuthorize("hasAuthority('KNW_SEC_MODIFY')")
    @PostMapping("/decisionOnJustificationKNW")
    fun decisionOnJustificationKNW(@RequestBody nwaJustificationDecision: NWAJustificationDecision) : List<TaskDetails>
    {
        return nwaService.decisionOnJustificationKNW(nwaJustificationDecision)
    }

    //********************************************************** get spc_sec Tasks **********************************************************
    @PreAuthorize("hasAuthority('SPC_SEC_SD_READ')")
    @GetMapping("/getSpcSecTasks")
    fun getSPCSECTasks():List<TaskDetails>
    {
        return nwaService.getSPCSECTasks()
    }


    //SPC decision on Justification
    @PreAuthorize("hasAuthority('SPC_SEC_SD_MODIFY')")
    @PostMapping("/decisionOnJustification")
    fun decisionOnJustification(@RequestBody nwaJustificationDecision: NWAJustificationDecision) : List<TaskDetails>
    {
        return nwaService.decisionOnJustification(nwaJustificationDecision)
    }


    //********************************************************** process prepare justification for DI-SDT Approval **********************************************************
    @PreAuthorize("hasAuthority('KNW_SEC_MODIFY')")
    @PostMapping("/prepareDiSdtJustification")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    @ResponseBody
    fun prepareDisDtJustification(@RequestBody nwaDiSdtJustification: NWADiSdtJustification): ServerResponse
    {
//        val gson = Gson()
//        KotlinLogging.logger { }.info { "INVOICE CALCULATED" + gson.toJson(nwaDiSdtJustification) }
        return ServerResponse(HttpStatus.OK,"Successfully uploaded DI-SDT Justification",nwaService.prepareDisDtJustification(nwaDiSdtJustification))
    }

    //Upload document for DI SDT
    @PostMapping("/di-file-upload")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadDIFiles(
        @RequestParam("nwaDiSdtJustificationID") nwaDiSdtJustificationID: Long,
        @RequestParam("docFile") docFile: List<MultipartFile>,
        model: Model
    ): CommonDaoServices.MessageSuccessFailDTO {

        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val nwaDiSdtJustification = nwadisdtJustificationRepository.findByIdOrNull(nwaDiSdtJustificationID)?: throw Exception("NWA DI SDT DOCUMENT ID DOES NOT EXIST")

        docFile.forEach { u ->
            val upload = SDDIJustificationUploads()
            with(upload) {
                diDocumentId = nwaDiSdtJustification.id

            }
            nwaService.uploadSDIFile(
                upload,
                u,
                "UPLOADS",
                loggedInUser,
                "NWA DI-SDT Justification"
            )
        }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.message = "Document Uploaded successfully"

        return sm
    }

    //********************************************************** get di-sdt Tasks **********************************************************
    //@PreAuthorize("hasAuthority('DI_SDT_SD_READ')")
    @GetMapping("/getDiSdtTasks")
    fun getDISDTTasks():List<TaskDetails>
    {
        return nwaService.getDISDTTasks()
    }

    // View DI SDT Uploaded document
    @GetMapping("/view/di-justification")
    fun viewDiJustificationFile(
        response: HttpServletResponse,
        @RequestParam("diDocumentId") diDocumentId: Long
    ) {
        val fileUploaded = nwaService.findUploadedDIFileBYId(diDocumentId)
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

    //********************************************************** get Tc Sec Tasks **********************************************************
    @PreAuthorize("hasAuthority('TC_SEC_SD_READ')")
    @GetMapping("/getTCSeCTasks")
    fun getTCSeCTasks():List<TaskDetails>
    {
        return nwaService.getTCSeCTasks()
    }


    //********************************************************** Decision  on DI-SDT Approval **********************************************************
    //@PreAuthorize("hasAuthority('DI_SDT_SD_MODIFY')")
    @PostMapping("/decisionOnDiSdtJustification")
    fun decisionOnDiSdtJustification(@RequestBody workshopAgreement: WorkshopAgreement): List<TaskDetails> {
        return nwaService.decisionOnDiSdtJustification(workshopAgreement)
    }

    //********************************************************** process prepare Preliminary Draft **********************************************************
    @PreAuthorize("hasAuthority('TC_SEC_SD_MODIFY')")
    @PostMapping("/preparePreliminaryDraft")
    @ResponseBody
    fun preparePreliminaryDraft(@RequestBody nwaPreliminaryDraft: NWAPreliminaryDraft): ServerResponse
    {

        return ServerResponse(HttpStatus.OK,"Successfully uploaded Preliminary Draft",nwaService.preparePreliminaryDraft(nwaPreliminaryDraft))
    }

    // upload preliminary draft
    @PostMapping("/pd-file-upload")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadPDFiles(
        @RequestParam("nwaPDid") nwaPDid: Long,
        @RequestParam("docFile") docFile: List<MultipartFile>,
        model: Model
    ): CommonDaoServices.MessageSuccessFailDTO {

        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val nwaPreliminaryDraft = nwaPreliminaryDraftRepository.findByIdOrNull(nwaPDid)?: throw Exception("NWA PRELIMINARY DRAFT  ID DOES NOT EXIST")

        docFile.forEach { u ->
            val upload = NWAPreliminaryDraftUploads()
            with(upload) {
                nwaPDDocumentId = nwaPreliminaryDraft.id

            }
            nwaService.uploadPDFile(
                upload,
                u,
                "UPLOADS",
                loggedInUser,
                "NWA PRELIMINARY DRAFT"
            )
        }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.message = "Document Uploaded successfully"

        return sm
    }

    // View PD Uploaded document
    @GetMapping("/view/preliminaryDraft")
    fun viewPDFile(
        response: HttpServletResponse,
        @RequestParam("nwaPDDocumentId") nwaPDDocumentId: Long
    ) {
        val fileUploaded = nwaService.findUploadedPDFileBYId(nwaPDDocumentId)
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

    //********************************************************** Decision  on Preliminary Draft **********************************************************
    @PreAuthorize("hasAuthority('KNW_SEC_MODIFY')")
    @PostMapping("/decisionOnPd")
    fun decisionOnPD(@RequestBody nwaPreliminaryDraftDecision: NWAPreliminaryDraftDecision) : List<TaskDetails>
    {
        return nwaService.decisionOnPD(nwaPreliminaryDraftDecision)
    }

    //********************************************************** get Head of Publishing Tasks **********************************************************
    @PreAuthorize("hasAuthority('HOP_SD_READ')")
    @GetMapping("/getHOPTasks")
    fun getHOPTasks():List<TaskDetails>
    {
        return nwaService.getHOPTasks()
    }

    //********************************************************** process prepare Workshop Draft **********************************************************
    @PreAuthorize("hasAuthority('HOP_SD_MODIFY')")
    @PostMapping("/editWorkshopDraft")
    @ResponseBody
    fun editWorkshopDraft(@RequestBody nwaWorkShopDraft: NWAWorkShopDraft): ServerResponse
    {

        return ServerResponse(HttpStatus.OK,"Successfully uploaded Workshop Draft",nwaService.editWorkshopDraft(nwaWorkShopDraft))
    }

    // upload Workshop draft
    @PostMapping("/wd-file-upload")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadWDFiles(
        @RequestParam("nwaWDid") nwaWDid: Long,
        @RequestParam("docFile") docFile: List<MultipartFile>,
        model: Model
    ): CommonDaoServices.MessageSuccessFailDTO {

        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val nwaWorkShopDraft = nwaWorkshopDraftRepository.findByIdOrNull(nwaWDid)?: throw Exception("NWA WORK SHOP DRAFT ID DOES NOT EXIST")

        docFile.forEach { u ->
            val upload = NWAWorkShopDraftUploads()
            with(upload) {
                nwaWDDocumentId = nwaWorkShopDraft.id

            }
            nwaService.uploadWDFile(
                upload,
                u,
                "UPLOADS",
                loggedInUser,
                "NWA WORK SHOP  DRAFT"
            )
        }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.message = "Document Uploaded successfully"

        return sm
    }


    //********************************************************** get Head of SAC SEC Tasks **********************************************************
    @PreAuthorize("hasAuthority('SAC_SEC_SD_READ')")
    @GetMapping("/getSacSecTasks")
    fun getSacSecTasks():List<TaskDetails>
    {
        return nwaService.getSacSecTasks()
    }

    // View WD Uploaded document
    @GetMapping("/view/workShopDraft")
    fun viewWDFile(
        response: HttpServletResponse,
        @RequestParam("nwaWDDocumentId") nwaWDDocumentId: Long
    ) {
        val fileUploaded = nwaService.findUploadedWDFileBYId(nwaWDDocumentId)
        val fileDoc = commonDaoServices.mapClass(fileUploaded)
        response.contentType = "application/pdf"
       // response.setHeader("Content-Length", pdfReportStream.size().toString())
        response.addHeader("Content-Disposition", "inline; filename=${fileDoc.name}")
        response.outputStream
            .let { responseOutputStream ->
                responseOutputStream.write(fileDoc.document?.let { makeAnyNotBeNull(it) } as ByteArray)
                responseOutputStream.close()
            }

        KotlinLogging.logger { }.info("VIEW FILE SUCCESSFUL")

    }

    //********************************************************** Decision  on Workshop Draft Approval **********************************************************
    @PreAuthorize("hasAuthority('SAC_SEC_SD_MODIFY')")
    @PostMapping("/decisionOnWd")
    fun decisionOnWd(@RequestBody nwaWorkshopDraftDecision: NWAWorkshopDraftDecision) : List<TaskDetails>
    {
        val gson = Gson()
        KotlinLogging.logger { }.info { "WORKSHOP DRAFT DECISION" + gson.toJson(nwaWorkshopDraftDecision) }
        return nwaService.decisionOnWD(nwaWorkshopDraftDecision)
    }


    //********************************************************** process upload Standard **********************************************************
    @PreAuthorize("hasAuthority('HOP_SD_MODIFY')")
    @PostMapping("/uploadNwaStandard")
    @ResponseBody
    fun uploadNwaStandard(@RequestBody nWAStandard: NWAStandard): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Successfully uploaded Standard",nwaService.uploadNwaStandard(nWAStandard))
    }

    // upload NWA Standard
    @PostMapping("/std-file-upload")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadSTDFiles(
        @RequestParam("nwaSTDid") nwaSTDid: Long,
        @RequestParam("docFile") docFile: List<MultipartFile>,
        model: Model
    ): CommonDaoServices.MessageSuccessFailDTO {

        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val nWAStandard = nwaStandardRepository.findByIdOrNull(nwaSTDid)?: throw Exception("NWA STANDARD  ID DOES NOT EXIST")

        docFile.forEach { u ->
            val upload = NWAStandardUploads()
            with(upload) {
                nwaSDocumentId = nWAStandard.id

            }
            nwaService.uploadSTDFile(
                upload,
                u,
                "UPLOADS",
                loggedInUser,
                "NWA STANDARD"
            )
        }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.message = "Document Uploaded successfully"

        return sm
    }


    //********************************************************** get Head of HO SIC Tasks **********************************************************
    @PreAuthorize("hasAuthority('HO_SIC_SD_READ')")
    @GetMapping("/getHoSiCTasks")
    fun getHoSiCTasks():List<TaskDetails>
    {
        return nwaService.getHoSiCTasks()
    }

    // View STD Uploaded document
    @GetMapping("/view/knwStandard")
    fun viewKnwStandardFile(
        response: HttpServletResponse,
        @RequestParam("nwaSDocumentId") nwaSDocumentId: Long
    ) {
        val fileUploaded = nwaService.findUploadedSTDFileBYId(nwaSDocumentId)
        val fileDoc = commonDaoServices.mapClass(fileUploaded)
        response.contentType = "application/pdf"
        // response.setHeader("Content-Length", pdfReportStream.size().toString())
        response.addHeader("Content-Disposition", "inline; filename=${fileDoc.name}")
        response.outputStream
            .let { responseOutputStream ->
                responseOutputStream.write(fileDoc.document?.let { makeAnyNotBeNull(it) } as ByteArray)
                responseOutputStream.close()
            }

        KotlinLogging.logger { }.info("VIEW FILE SUCCESSFUL")

    }

    //********************************************************** process upload Gazette Notice **********************************************************
    @PreAuthorize("hasAuthority('HO_SIC_SD_MODIFY')")
    @PostMapping("/uploadGazetteNotice")
    @ResponseBody
    fun uploadGazetteNotice(@RequestBody nWAGazetteNotice: NWAGazetteNotice): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Successfully uploaded Gazette Notice",nwaService.uploadGazetteNotice(nWAGazetteNotice))
    }

    //********************************************************** process upload Gazettement Date **********************************************************
    @PreAuthorize("hasAuthority('HO_SIC_SD_MODIFY')")
    @PostMapping("/updateGazettementDate")
    @ResponseBody
    fun updateGazettementDate(@RequestBody nWAGazettement: NWAGazettement): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Successfully uploaded Gazette Notice",nwaService.updateGazettementDate(nWAGazettement))
    }

    @PostMapping("/upload")
    fun uploadFile(@RequestParam("file") file: MultipartFile,
                    @RequestParam("itemId") itemId: String,
                    @RequestParam("type") type: String): ResponseEntity<ResponseMessage>
    {
        var message: String? = null

        return try{
            if(type == "Justification")
            {
                val newItemId = itemId.replace("/",".")
                nwaJustificationFileService.store(file,newItemId)
            }
            else if(type == "DISDTJustification")
            {
                val newItemId = itemId.replace("/",".")
                nwaJustificationFileService.store(file,newItemId)
            }

            message ="Uploaded the file successfully: ${file.originalFilename}"
            ResponseEntity.status(HttpStatus.OK).body(ResponseMessage(message))
        }
        catch (e: Exception){
            message ="Could not upload the file: ${file.originalFilename}!"
            ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(ResponseMessage(message))
        }
    }



    @GetMapping("list/files/{type}/{itemId}")
    fun getListFiles(@PathVariable type: String?,@PathVariable("itemId") itemId: String): ResponseEntity<List<ResponseFile>> {

        var files: List<ResponseFile>? = null

        if(type == "Justification")
        {

            files = nwaJustificationFileService.getAllFiles(itemId).map { dbFile ->
                val fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/v1/nwa/files/")
                    .path(dbFile.id)
                    .toUriString()

                ResponseFile(
                    dbFile.name,
                    fileDownloadUri,
                    dbFile.type,
                    dbFile.data.size)

            }.collect(Collectors.toList())
        }
        else if(type == "DISDTJustification")
        {

            files = nwaJustificationFileService.getAllFiles(itemId).map { dbFile ->
                val fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/v1/nwa/files/")
                    .path(dbFile.id)
                    .toUriString()

                ResponseFile(
                    dbFile.name,
                    fileDownloadUri,
                    dbFile.type,
                    dbFile.data.size)

            }.collect(Collectors.toList())
        }


        return ResponseEntity.status(HttpStatus.OK).body(files)
    }

    @GetMapping("/files/{id}")
    fun getFile(@PathVariable id: String?): ResponseEntity<ByteArray?>? {
        val fileDB = nwaJustificationFileService.getFile(id!!)
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB!!.name + "\"")
            .body(fileDB.data)
    }



//    @GetMapping("/process/{processId}")
//    fun checkState(@PathVariable("processId") processId: String?) {
//        nwaService.checkProcessHistory(processId)
//    }

    @GetMapping("/getRQNumber")
    @ResponseBody
    fun getRQNumber(): String
    {
        return nwaService.getRQNumber();
    }

    @GetMapping("/getCDNumber")
    @ResponseBody
    fun getCDNumber(): String
    {
        return nwaService.getCDNumber().first;
    }

    @GetMapping("/getKSNumber")
    @ResponseBody
    fun getKSNumber(): String
    {
        return nwaService.getKSNumber();
    }

    @PostMapping("/anonymous/standard/close")
    fun clos(@RequestBody responseMessage: ResponseMessage) {
        return nwaService.closeProcess(responseMessage.message)
    }

    //Delete A Task
    @PostMapping("/anonymous/standard/closetask")
    fun closeTask(@RequestBody responseMessage: ResponseMessage) {
        return nwaService.closeTask(responseMessage.message)
    }

    @PostMapping("/workshop/process")
    @ResponseBody
    fun checkState(@RequestBody id: ID): ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Successfully returned process history",
            nwaService.checkProcessHistory(id)
        )
    }
}
