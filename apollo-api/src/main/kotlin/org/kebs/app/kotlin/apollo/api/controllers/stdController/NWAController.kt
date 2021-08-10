package org.kebs.app.kotlin.apollo.api.controllers.stdController

import com.nhaarman.mockitokotlin2.any
import org.apache.commons.io.input.ObservableInputStream
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.*
import org.kebs.app.kotlin.apollo.common.dto.std.*
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.std.NWADISDTJustificationRepository
import org.kebs.app.kotlin.apollo.store.repo.std.NwaJustificationRepository
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


@RestController
//@CrossOrigin(origins = ["http://localhost:4200"])
@RequestMapping("api/v1/migration/nwa")
class NWAController(val nwaService: NWAService,
                    private val commonDaoServices: CommonDaoServices,
                    private val nwaJustificationRepository: NwaJustificationRepository,
                    val standardReviewFormService: StandardReviewFormService,
                    val nwaJustificationFileService: NwaJustificationFileService,
                    val nwaDiJustificationFileService: NwaDiJustificationFileService,
                    val nwadisdtJustificationRepository: NWADISDTJustificationRepository
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
    @PreAuthorize("hasAuthority('KNW_SEC_MODIFY')")
    @PostMapping("/prepareJustification")
    @ResponseBody
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun prepareJustification(
        @RequestBody nwaJustification: NWAJustification
    ): ServerResponse{
        return ServerResponse(HttpStatus.OK,"Successfully uploaded Justification",nwaService.prepareJustification(nwaJustification))
    }

//    @PreAuthorize("hasAuthority('KNW_SEC_MODIFY')")
    @PostMapping("/file-upload")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadFiles(
        @RequestParam("nwaJustificationID") nwaJustificationID: Long,
        @RequestParam("docFile") docFile: List<MultipartFile>,
        @RequestParam("DocDescription") DocDescription: String,
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
                DocDescription
            )
        }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.message = "Document Uploaded successful"

        return sm
    }

    //********************************************************** get spc_sec Tasks **********************************************************
    @PreAuthorize("hasAuthority('SPC_SEC_SD_READ')")
    @GetMapping("/getSpcSecTasks")
    fun getSPCSECTasks():List<TaskDetails>
    {
        return nwaService.getSPCSECTasks()
    }


    //decision
    @PreAuthorize("hasAuthority('SPC_SEC_SD_MODIFY')")
    @PostMapping("/decisionOnJustification")
    fun decisionOnJustification(@RequestBody nwaJustification: NWAJustification)
    {
        nwaService.decisionOnJustification(nwaJustification)
    }


    //********************************************************** get KNW Tasks **********************************************************
    @PreAuthorize("hasAuthority('KNW_SEC_READ')")
    @GetMapping("/knwtasks")
    fun getKNWTask():List<TaskDetails>
    {
        return nwaService.getKNWTasks()
    }

    //********************************************************** process prepare justification for DI-SDT Approval **********************************************************
    @PreAuthorize("hasAuthority('KNW_SEC_MODIFY')")
    @PostMapping("/prepareDiSdtJustification")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    @ResponseBody
    fun prepareDisDtJustification(@RequestBody nwaDiSdtJustification: NWADiSdtJustification): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Successfully uploaded DI-SDT Justification",nwaService.prepareDisDtJustification(nwaDiSdtJustification))
    }

    @PostMapping("/di-file-upload")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadDIFiles(
        @RequestParam("nwaJustificationID") nwaJustificationID: Long,
        @RequestParam("docFile") docFile: List<MultipartFile>,
        @RequestParam("DocDescription") DocDescription: String,
        model: Model
    ): CommonDaoServices.MessageSuccessFailDTO {

        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val nwaJustification = nwadisdtJustificationRepository.findByIdOrNull(nwaJustificationID)?: throw Exception("NWA DISDT DOCUMENT ID DOES NOT EXIST")

        docFile.forEach { u ->
            val upload = DatKebsSdNwaUploadsEntity()
            with(upload) {
                nwaDocumentId = nwaJustification.id

            }
            nwaService.uploadSDIFile(
                upload,
                u,
                "UPLOADS",
                loggedInUser,
                DocDescription
            )
        }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.message = "Document Uploaded successful"

        return sm
    }
    //********************************************************** get di-sdt Tasks **********************************************************
    @PreAuthorize("hasAuthority('DI_SDT_SD_READ')")
    @GetMapping("/getDiSdtTasks")
    fun getDISDTTasks():List<TaskDetails>
    {
        return nwaService.getDISDTTasks()
    }

    //********************************************************** Decision  on DI-SDT Approval **********************************************************
    @PreAuthorize("hasAuthority('DI_SDT_SD_MODIFY')")
    @PostMapping("/decisionOnDiSdtJustification")
    fun decisionOnDiSdtJustification(@RequestBody nwaDiSdtJustification: NWADiSdtJustification)
    {
        nwaService.decisionOnDiSdtJustification(nwaDiSdtJustification)
    }

    //********************************************************** process prepare Preliminary Draft **********************************************************
    @PreAuthorize("hasAuthority('KNW_SEC_MODIFY')")
    @PostMapping("/preparePreliminaryDraft")
    @ResponseBody
    fun preparePreliminaryDraft(@RequestBody nwaPreliminaryDraft: NWAPreliminaryDraft): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Successfully uploaded Preliminary Draft",nwaService.preparePreliminaryDraft(nwaPreliminaryDraft))
    }

    //********************************************************** Decision  on Preliminary Draft **********************************************************
    @PreAuthorize("hasAuthority('KNW_SEC_MODIFY')")
    @PostMapping("/decisionOnPd")
    fun decisionOnPD(@RequestBody nwaPreliminaryDraft: NWAPreliminaryDraft)
    {
        nwaService.decisionOnPD(nwaPreliminaryDraft)
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

    //********************************************************** get Head of SAC SEC Tasks **********************************************************
    @PreAuthorize("hasAuthority('SAC_SEC_SD_READ')")
    @GetMapping("/getSacSecTasks")
    fun getSacSecTasks():List<TaskDetails>
    {
        return nwaService.getSacSecTasks()
    }

    //********************************************************** Decision  on Workshop Draft Approval **********************************************************
    @PreAuthorize("hasAuthority('SAC_SEC_SD_MODIFY')")
    @PostMapping("/decisionOnWd")
    fun decisionOnWd(@RequestBody nwaWorkShopDraft: NWAWorkShopDraft)
    {
        nwaService.decisionOnWD(nwaWorkShopDraft)
    }


    //********************************************************** process upload Standard **********************************************************
    @PreAuthorize("hasAuthority('HOP_SD_MODIFY')")
    @PostMapping("/uploadNwaStandard")
    @ResponseBody
    fun uploadNwaStandard(@RequestBody nWAStandard: NWAStandard): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Successfully uploaded Standard",nwaService.uploadNwaStandard(nWAStandard))
    }

    //********************************************************** get Head of HO SIC Tasks **********************************************************
    @PreAuthorize("hasAuthority('HO_SIC_SD_READ')")
    @GetMapping("/getHoSiCTasks")
    fun getHoSiCTasks():List<TaskDetails>
    {
        return nwaService.getHoSiCTasks()
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



    @GetMapping("/process/{processId}")
    fun checkState(@PathVariable("processId") processId: String?) {
        nwaService.checkProcessHistory(processId)
    }

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
        return nwaService.getCDNumber();
    }

    @GetMapping("/getKSNumber")
    @ResponseBody
    fun getKSNumber(): String
    {
        return nwaService.getKSNumber();
    }
}
