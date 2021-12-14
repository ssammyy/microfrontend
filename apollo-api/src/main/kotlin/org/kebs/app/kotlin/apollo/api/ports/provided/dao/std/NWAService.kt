package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std

import mu.KotlinLogging
import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.engine.history.HistoricActivityInstance
import org.flowable.engine.repository.Deployment
import org.flowable.task.api.Task
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.payload.request.JustificationTaskDataDto
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.dto.std.*
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.qa.QaUploadsEntity
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.std.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.sql.Timestamp
import java.util.*
import javax.servlet.http.HttpServletResponse
import kotlin.collections.set


@Service
class NWAService(private val runtimeService: RuntimeService,
                 private val taskService: TaskService,
                 @Qualifier("processEngine") private val processEngine: ProcessEngine,
                 private val repositoryService: RepositoryService,
                 private val commonDaoServices: CommonDaoServices,
                 private val nwaJustificationRepository: NwaJustificationRepository,
                 private val nwaDisDtJustificationRepository: NWADISDTJustificationRepository,
                 private val nwaPreliminaryDraftRepository: NwaPreliminaryDraftRepository,
                 private val technicalCommitteeRepository: TechnicalCommitteeRepository,
                 private val technicalComListRepository: TechnicalComListRepository,
                 private val departmentRepository: DepartmentRepository,
                 private val departmentListRepository: DepartmentListRepository,
                 private val sdNwaUploadsEntityRepository: DatKebsSdNwaUploadsEntityRepository,
                 private val nwaWorkshopDraftRepository: NwaWorkShopDraftRepository,
                 private val nwaStandardRepository: NwaStandardRepository,
                 private val nwaGazettementRepository: NwaGazettementRepository,
                 private val nwaGazetteNoticeRepository: NwaGazetteNoticeRepository,
                 private val sdDiJustificationUploadsRepository: SDDIJustificationUploadsRepository,
                 private val nwaPreliminaryDraftUploadsRepository: NWAPreliminaryDraftUploadsRepository,
                 private val nwaStandardUploadsRepository: NWAStandardUploadsRepository,
                 private val nwaWorkShopDraftUploadsRepository: NWAWorkShopDraftUploadsRepository,
                 private val notifications: Notifications


) {

    val PROCESS_DEFINITION_KEY = "sd_KenyaNationalWorkshopAgreementModule"
    val TASK_CANDIDATE_TC_SEC ="TC_SEC"
    val TASK_CANDIDATE_KNW_SEC ="KNW_SEC"
    val TASK_CANDIDATE_SPC_SEC ="SPC_SEC"
    val TASK_CANDIDATE_DI_SDT ="DI_SDT"
    val TASK_CANDIDATE_HOP ="HOP"
    val TASK_CANDIDATE_SAC_SEC ="SAC_SEC"
    val TASK_CANDIDATE_HO_SIC ="HO_SIC"

    //deploy bpmn file
    fun deployProcessDefinition(): Deployment =repositoryService
        .createDeployment()
        .addClasspathResource("processes/std/Kenya_National_Workshop_Agreement_Module.bpmn20.xml")
        .deploy()

    //start the process by process Key
    fun startProcessByKey() :ProcessInstanceResponse
    {

        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY)
        return ProcessInstanceResponse( processInstance.id, processInstance.isEnded)
    }



    fun getKNWDepartments(): MutableList<Department>
    {
        return departmentRepository.findAll()
    }

    fun getKNWCommittee(): MutableList<TechnicalCommittee>
    {
        return technicalCommitteeRepository.findAll()
    }

//    fun startProcessInstance (): ProcessInstanceResponse{
//        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY)
//        return ProcessInstanceResponse(processInstance.id, processInstance.isEnded)
//    }

    //prepare justification
    fun prepareJustification(nwaJustification: NWAJustification) : ProcessInstanceResponseValue
    {
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        val variables: MutableMap<String, Any> = HashMap()
        nwaJustification.knw?.let{ variables.put("knw", it)}
        nwaJustification.meetingDate?.let{ variables.put("meetingDate", it)}
        nwaJustification.knwSecretary?.let{ variables.put("knwSecretary", it)}
        nwaJustification.sl?.let{ variables.put("sl", it)}
        //nwaJustification.requestNumber?.let{ variables.put("requestNumber", it)}
        nwaJustification.requestedBy?.let{ variables.put("requestedBy", it)}
        nwaJustification.issuesAddressed?.let{ variables.put("issuesAddressed", it)}
        nwaJustification.knwAcceptanceDate?.let{ variables.put("knwAcceptanceDate", it)}
        nwaJustification.referenceMaterial?.let{ variables.put("referenceMaterial", it)}
        nwaJustification.department?.let{ variables.put("department", it)}
        nwaJustification.status?.let{ variables.put("status", it)}
        nwaJustification.remarks?.let{ variables.put("remarks", it)}

        nwaJustification.submissionDate = Timestamp(System.currentTimeMillis())
        variables["submissionDate"] = nwaJustification.submissionDate!!

        nwaJustification.requestNumber = getRQNumber()

        variables["requestNumber"] = nwaJustification.requestNumber!!

        variables["knwCommittee"] = technicalComListRepository.findNameById(nwaJustification.knw?.toLong())
        nwaJustification.knwCommittee = technicalComListRepository.findNameById(nwaJustification.knw?.toLong())

        variables["departmentName"] = departmentListRepository.findNameById(nwaJustification.department?.toLong())
        nwaJustification.departmentName = departmentListRepository.findNameById(nwaJustification.department?.toLong())
        //var justificationUploadId =sdNwaUploadsEntityRepository.getMaxUploadedID()
        val nwaDetails = nwaJustificationRepository.save(nwaJustification)
        variables["ID"] = nwaDetails.id

        //taskService.complete(nwaJustification.taskId, variables)
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables)
        return ProcessInstanceResponseValue(nwaDetails.id, processInstance.id, processInstance.isEnded,
            nwaJustification.requestNumber?: throw NullValueNotAllowedException("Request Number is required")
        )




    }



//    fun prepareJustification(
//        nwaJustification: NWAJustification,
//        docFiles: List<MultipartFile>
//
//    ): ProcessInstanceResponseValue
//    {
//        val loggedInUser = commonDaoServices.loggedInUserDetails()
//
//        val variables: MutableMap<String, Any> = HashMap()
//        nwaJustification.knw?.let{ variables.put("knw", it)}
//        nwaJustification.meetingDate?.let{ variables.put("meetingDate", it)}
//        nwaJustification.knwSecretary?.let{ variables.put("knwSecretary", it)}
//        nwaJustification.sl?.let{ variables.put("sl", it)}
//        //nwaJustification.requestNumber?.let{ variables.put("requestNumber", it)}
//        nwaJustification.requestedBy?.let{ variables.put("requestedBy", it)}
//        nwaJustification.issuesAddressed?.let{ variables.put("issuesAddressed", it)}
//        nwaJustification.knwAcceptanceDate?.let{ variables.put("knwAcceptanceDate", it)}
//        nwaJustification.referenceMaterial?.let{ variables.put("referenceMaterial", it)}
//        nwaJustification.department?.let{ variables.put("department", it)}
//        nwaJustification.status?.let{ variables.put("status", it)}
//        nwaJustification.remarks?.let{ variables.put("remarks", it)}
//
//        nwaJustification.submissionDate = Timestamp(System.currentTimeMillis())
//        variables["submissionDate"] = nwaJustification.submissionDate!!
//
//        nwaJustification.requestNumber = getRQNumber()
//
//        variables["requestNumber"] = nwaJustification.requestNumber!!
//
//        variables["knwCommittee"] = technicalComListRepository.findNameById(nwaJustification.knw?.toLong())
//        nwaJustification.knwCommittee = technicalComListRepository.findNameById(nwaJustification.knw?.toLong())
//
//        variables["departmentName"] = departmentListRepository.findNameById(nwaJustification.department?.toLong())
//        nwaJustification.departmentName = departmentListRepository.findNameById(nwaJustification.department?.toLong())
//        //var justificationUploadId =sdNwaUploadsEntityRepository.getMaxUploadedID()
//        val nwaDetails = nwaJustificationRepository.save(nwaJustification)
//        variables["ID"] = nwaDetails.id
//        //variables["jsUploadDocId"] = justificationUploadId.plus(1)
//        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables)
//
//        docFiles.forEach { docFile ->
//            var uploads = DatKebsSdNwaUploadsEntity()
//                .apply {
////            filepath = docFile.path
//                    nwaDocumentId = nwaDetails.id
//                    name = commonDaoServices.saveDocuments(docFile)
////            fileType = docFile.contentType
//                    fileType = docFile.contentType
//                    document = docFile.bytes
//                    transactionDate = commonDaoServices.getCurrentDate()
//                    status = 1
// //                   createdBy = commonDaoServices.concatenateName(user)
//                    createdOn = commonDaoServices.getTimestamp()
//                }
//
//            uploads = sdNwaUploadsEntityRepository.save(uploads)
//            val docVariables: MutableMap<String, Any> = HashMap()
//            docVariables["jsNwaJustification"] = "${ uploads.nwaDocumentId }"
//            docVariables["jsUploadDocId"] = "${ uploads.id }"
//            docVariables["jsUploadDocName"] = "${ uploads.name }"
//            docVariables["jsUploadDocType"] = "${ uploads.fileType }"
////
// //          processInstance.processVariables.putAll(docVariables)
//
//        }
//
//
//
//
//        return ProcessInstanceResponseValue(nwaDetails.id, processInstance.id, processInstance.isEnded,
//            nwaJustification.requestNumber?: throw NullValueNotAllowedException("Request Number is required")
//        )
//
//    }


    fun uploadSDFile(
        uploads: DatKebsSdNwaUploadsEntity,
        docFile: MultipartFile,
        doc: String,
        user: UsersEntity,
        DocDescription: String
    ): DatKebsSdNwaUploadsEntity {

        with(uploads) {
//            filepath = docFile.path
            name = commonDaoServices.saveDocuments(docFile)
//            fileType = docFile.contentType
            fileType = docFile.contentType
            documentType = doc
            description=DocDescription
            document = docFile.bytes
            transactionDate = commonDaoServices.getCurrentDate()
            status = 1
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }

        return sdNwaUploadsEntityRepository.save(uploads)
    }

    //Function to retrieve task details for any candidate group
    private fun getTaskDetails(tasks: List<Task>): List<TaskDetails> {
        val taskDetails: MutableList<TaskDetails> = ArrayList()
        for (task in tasks) {
            val processVariables = taskService.getVariables(task.id)
            taskDetails.add(TaskDetails(task.id, task.name, processVariables))

        }
        return taskDetails
    }

    //Return task details for KNW_SEC
    fun getKNWTasks():List<TaskDetails>
    {

        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_KNW_SEC).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

//

//    fun getSPCSECTasks():List<JustificationTaskDataDto>
//    {
//        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_SPC_SEC).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
//        val justificationTasks = mutableListOf<JustificationTaskDataDto>()
//        tasks.forEach { task->
//            val processVariables = taskService.getVariables(task.id)
//            val justificationID = processVariables["ID"] as Long?
//            nwaJustificationRepository.findByIdOrNull(justificationID?:0L)
//            //throw NullValueNotAllowedException("Justification ID cannot be empty"))
//            ?.let { justification  ->
//
//                val uploads =sdNwaUploadsEntityRepository.findByNwaDocumentId(justification.id)
//
//                val justificationTask = JustificationTaskDataDto(TaskDetails(task.id, task.name,processVariables) ,justification, uploads )
//                justificationTasks.add(justificationTask)
//            }
//        }
//        return justificationTasks
//    }

    //Get justification Document
    fun findUploadedFileBYId(nwaDocumentId: Long): DatKebsSdNwaUploadsEntity {
        return sdNwaUploadsEntityRepository.findByNwaDocumentId(nwaDocumentId) ?: throw ExpectedDataNotFound("No File found with the following [ id=$nwaDocumentId]")
    }


    fun getJustification(nwaDocumentId: Long): DatKebsSdNwaUploadsEntity? {
        return sdNwaUploadsEntityRepository.findById(nwaDocumentId).get()
    }

    fun downloadFile(response: HttpServletResponse, doc: CommonDaoServices.FileDTO) {
        response.contentType = doc.fileType
//                    response.setHeader("Content-Length", pdfReportStream.size().toString())
        response.addHeader("Content-Disposition", "inline; filename=${doc.name};")
        response.outputStream
            .let { responseOutputStream ->
                responseOutputStream.write(doc.document?.let { makeAnyNotBeNull(it) } as ByteArray)
                responseOutputStream.close()
            }

        KotlinLogging.logger { }.info("VIEW FILE SUCCESSFUL")
    }

    fun makeAnyNotBeNull(anyValue: Any): Any {
        return anyValue
    }

    // KNW SEC Decision on Justification
    fun decisionOnJustificationKNW(nwaJustificationDecision: NWAJustificationDecision) : List<TaskDetails> {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        variables["Yes"] = nwaJustificationDecision.accentTo
        variables["No"] = nwaJustificationDecision.accentTo
        nwaJustificationDecision.comments.let { variables.put("comments", it) }
        if(variables["Yes"]==true){
            nwaJustificationRepository.findByIdOrNull(nwaJustificationDecision.approvalID)?.let { nwaJustification->

                with(nwaJustification){
                    remarks=nwaJustificationDecision.comments
                    accentTo = true
                }
                nwaJustificationRepository.save(nwaJustification)
            }?: throw Exception("TASK NOT FOUND")

        }else if(variables["No"]==false) {
            nwaJustificationRepository.findByIdOrNull(nwaJustificationDecision.approvalID)?.let { nwaJustification->

                with(nwaJustification){
                    remarks=nwaJustificationDecision.comments
                    accentTo = false
                }
                nwaJustificationRepository.save(nwaJustification)
            }?: throw Exception("TASK NOT FOUND")

        }
        taskService.complete(nwaJustificationDecision.taskId, variables)
        return  getKNWTasks()
    }

    //Return task details for SPC_SEC
    fun getSPCSECTasks():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_SPC_SEC).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }



    // SPC Decision on Justification

    fun decisionOnJustification(nwaJustificationDecision: NWAJustificationDecision) : List<TaskDetails> {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        variables["Yes"] = nwaJustificationDecision.accentTo
        variables["No"] = nwaJustificationDecision.accentTo
        nwaJustificationDecision.comments.let { variables.put("comments", it) }
        if(variables["Yes"]==true){
            nwaJustificationRepository.findByIdOrNull(nwaJustificationDecision.approvalID)?.let { nwaJustification->

                with(nwaJustification){
                    remarks=nwaJustificationDecision.comments
                    accentTo = true
                }
                nwaJustificationRepository.save(nwaJustification)
            }?: throw Exception("TASK NOT FOUND")

        }else if(variables["No"]==false) {
            nwaJustificationRepository.findByIdOrNull(nwaJustificationDecision.approvalID)?.let { nwaJustification->

                with(nwaJustification){
                    remarks=nwaJustificationDecision.comments
                    accentTo = false
                }
                nwaJustificationRepository.save(nwaJustification)
            }?: throw Exception("TASK NOT FOUND")

        }
        taskService.complete(nwaJustificationDecision.taskId, variables)
        return  getSPCSECTasks()
    }



    // Prepare Justification for DI-SDT approval
    fun prepareDisDtJustification(nwaDiSdtJustification: NWADiSdtJustification) : ProcessInstanceDISDT
    {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val variable:MutableMap<String, Any> = HashMap()
        nwaDiSdtJustification.cost?.let{variable.put("cost", it)}
        nwaDiSdtJustification.numberOfMeetings?.let{variable.put("numberOfMeetings", it)}
        nwaDiSdtJustification.identifiedNeed?.let{variable.put("identifiedNeed", it)}
        nwaDiSdtJustification.dateOfApproval?.let{variable.put("dateOfApproval", it)}
        nwaDiSdtJustification.jstNumber?.let{variable.put("jstNumber", it)}
        nwaDiSdtJustification.datePrepared = commonDaoServices.getTimestamp()
        variable["datePrepared"] = nwaDiSdtJustification.datePrepared!!

        //print(nwaDiSdtJustification.toString())


        val nwaDetails = nwaDisDtJustificationRepository.save(nwaDiSdtJustification)
        variable["ID"] = nwaDetails.id
        taskService.complete(nwaDiSdtJustification.taskId, variable)
        println("Justification for DI-SDT prepared")
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variable)
        return ProcessInstanceDISDT(nwaDetails.id, processInstance.id, processInstance.isEnded,
            nwaDiSdtJustification.datePrepared?: throw NullValueNotAllowedException("Date is required")
        )

    }
    // Upload document for DI SDT
    fun uploadSDIFile(
        uploads: SDDIJustificationUploads,
        docFile: MultipartFile,
        doc: String,
        user: UsersEntity,
        DocDescription: String
    ): SDDIJustificationUploads {

        with(uploads) {
//            filepath = docFile.path
            name = commonDaoServices.saveDocuments(docFile)
//            fileType = docFile.contentType
            fileType = docFile.contentType
            documentType = doc
            description=DocDescription
            document = docFile.bytes
            transactionDate = commonDaoServices.getCurrentDate()
            status = 1
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }

        return sdDiJustificationUploadsRepository.save(uploads)
    }

    //Return task details for SPC_SEC
    fun getDISDTTasks():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_DI_SDT).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    // View DI SDT Document upload
    fun findUploadedDIFileBYId(diDocumentId: Long): SDDIJustificationUploads {
        return sdDiJustificationUploadsRepository.findByDiDocumentId(diDocumentId) ?: throw ExpectedDataNotFound("No File found with the following [ id=$diDocumentId]")
    }
    //Return task details for TC_SEC
    fun getTCSeCTasks():List<TaskDetails>
    {

        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_TC_SEC).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    //Decision on DI-SDT
    fun decisionOnDiSdtJustification(workshopAgreement: WorkshopAgreement) : List<TaskDetails> {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        variables["Yes"] = workshopAgreement.accentTo
        variables["No"] = workshopAgreement.accentTo
        workshopAgreement.comments.let { variables.put("comments", it) }
        if(variables["Yes"]==true){
            nwaDisDtJustificationRepository.findByIdOrNull(workshopAgreement.approvalID)?.let { nwaDiSdtJustification->
                val valueFound =getCDNumber()
                with(nwaDiSdtJustification){
                    cdAppNumber = valueFound.first
                    cdn = valueFound.second
                    remarks=workshopAgreement.comments
                    accentTo = true
                }
                nwaDisDtJustificationRepository.save(nwaDiSdtJustification)
            }?: throw Exception("TASK NOT FOUND")

        }else if(variables["No"]==false) {
            nwaJustificationRepository.findByIdOrNull(workshopAgreement.jstNumber)?.let { nwaJustification->

                with(nwaJustification){
                    remarks=workshopAgreement.comments
                   // accentTo = false
                }
                nwaJustificationRepository.save(nwaJustification)
            }?: throw Exception("TASK NOT FOUND")

        }
        taskService.complete(workshopAgreement.taskId, variables)
        return  getDISDTTasks()
    }



    // Prepare Preliminary Draft
    fun preparePreliminaryDraft(nwaPreliminaryDraft: NWAPreliminaryDraft) : ProcessInstancePD
    {
        val variable:MutableMap<String, Any> = HashMap()
        nwaPreliminaryDraft.title?.let{variable.put("title", it)}
        nwaPreliminaryDraft.scope?.let{variable.put("scope", it)}
        nwaPreliminaryDraft.normativeReference?.let{variable.put("normativeReference", it)}
        nwaPreliminaryDraft.symbolsAbbreviatedTerms?.let{variable.put("symbolsAbbreviatedTerms", it)}
        nwaPreliminaryDraft.clause?.let{variable.put("clause", it)}
        nwaPreliminaryDraft.special?.let{variable.put("special", it)}
        nwaPreliminaryDraft.diJNumber?.let{variable.put("diJNumber", it)}
        nwaPreliminaryDraft.datePdPrepared = commonDaoServices.getTimestamp()
        variable["datePdPrepared"] = nwaPreliminaryDraft.datePdPrepared!!


        val nwaDetails = nwaPreliminaryDraftRepository.save(nwaPreliminaryDraft)
        variable["ID"] = nwaDetails.id
        taskService.complete(nwaPreliminaryDraft.taskId, variable)
        println("Preliminary Draft Prepared")
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variable)
        return ProcessInstancePD(nwaDetails.id, processInstance.id, processInstance.isEnded,nwaPreliminaryDraft.datePdPrepared!!)

    }

    // Upload PD document
    fun uploadPDFile(
        uploads: NWAPreliminaryDraftUploads,
        docFile: MultipartFile,
        doc: String,
        user: UsersEntity,
        DocDescription: String
    ): NWAPreliminaryDraftUploads {

        with(uploads) {
//            filepath = docFile.path
            name = commonDaoServices.saveDocuments(docFile)
//            fileType = docFile.contentType
            fileType = docFile.contentType
            documentType = doc
            description=DocDescription
            document = docFile.bytes
            transactionDate = commonDaoServices.getCurrentDate()
            status = 1
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }

        return nwaPreliminaryDraftUploadsRepository.save(uploads)
    }

    // View PD Document upload
    fun findUploadedPDFileBYId(nwaPDDocumentId: Long): NWAPreliminaryDraftUploads {
        return nwaPreliminaryDraftUploadsRepository.findByNwaPDDocumentId(nwaPDDocumentId) ?: throw ExpectedDataNotFound("No File found with the following [ id=$nwaPDDocumentId]")
    }

    //Decision on Preliminary Draft

    fun decisionOnPD(nwaPreliminaryDraftDecision: NWAPreliminaryDraftDecision) : List<TaskDetails> {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        variables["Yes"] = nwaPreliminaryDraftDecision.accentTo
        nwaPreliminaryDraftDecision.comments.let { variables.put("comments", it) }
        if(variables["Yes"]==true){
            nwaPreliminaryDraftRepository.findByIdOrNull(nwaPreliminaryDraftDecision.approvalID)?.let { nwaPreliminaryDraft->

                with(nwaPreliminaryDraft){
                    remarks=nwaPreliminaryDraftDecision.comments
                    accentTo = true
                }
                nwaPreliminaryDraftRepository.save(nwaPreliminaryDraft)
            }?: throw Exception("TASK NOT FOUND")

        }else if(variables["No"]==false) {
            nwaDisDtJustificationRepository.findByIdOrNull(nwaPreliminaryDraftDecision.diJNumber)?.let { nwaDiSdtJustification->

                with(nwaDiSdtJustification){
                    remarks=nwaPreliminaryDraftDecision.comments
                    //accentTo = false
                }
                nwaDisDtJustificationRepository.save(nwaDiSdtJustification)
            }?: throw Exception("TASK NOT FOUND")

        }
        taskService.complete(nwaPreliminaryDraftDecision.taskId, variables)
        return  getKNWTasks()
    }

    //Return task details for Head of Publishing
    fun getHOPTasks():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_HOP).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    // Edit Workshop  Draft
    fun editWorkshopDraft(nwaWorkShopDraft: NWAWorkShopDraft) : ProcessInstanceWD
    {
        val variable:MutableMap<String, Any> = HashMap()
        nwaWorkShopDraft.title?.let{variable.put("title", it)}
        nwaWorkShopDraft.scope?.let{variable.put("scope", it)}
        nwaWorkShopDraft.normativeReference?.let{variable.put("normativeReference", it)}
        nwaWorkShopDraft.symbolsAbbreviatedTerms?.let{variable.put("symbolsAbbreviatedTerms", it)}
        nwaWorkShopDraft.clause?.let{variable.put("clause", it)}
        nwaWorkShopDraft.special?.let{variable.put("special", it)}
        nwaWorkShopDraft.dateWdPrepared = commonDaoServices.getTimestamp()
        variable["dateWdPrepared"] = nwaWorkShopDraft.dateWdPrepared!!

        //print(nwaWorkShopDraft.toString())

        val nwaDetails = nwaWorkshopDraftRepository.save(nwaWorkShopDraft)
        variable["ID"] = nwaDetails.id
        taskService.complete(nwaWorkShopDraft.taskId, variable)
        println("Workshop Draft Edited")
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variable)
        return ProcessInstanceWD(nwaDetails.id, processInstance.id, processInstance.isEnded,nwaWorkShopDraft.dateWdPrepared!!)

    }

    //upload Workshop draft Document
    fun uploadWDFile(
        uploads: NWAWorkShopDraftUploads,
        docFile: MultipartFile,
        doc: String,
        user: UsersEntity,
        DocDescription: String
    ): NWAWorkShopDraftUploads {

        with(uploads) {
//            filepath = docFile.path
            name = commonDaoServices.saveDocuments(docFile)
//            fileType = docFile.contentType
            fileType = docFile.contentType
            documentType = doc
            description=DocDescription
            document = docFile.bytes
            transactionDate = commonDaoServices.getCurrentDate()
            status = 1
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }

        return nwaWorkShopDraftUploadsRepository.save(uploads)
    }


    //Return task details for SAC SEC
    fun getSacSecTasks():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_SAC_SEC).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    // View WD Document upload
    fun findUploadedWDFileBYId(nwaWDDocumentId: Long): NWAWorkShopDraftUploads {
        return nwaWorkShopDraftUploadsRepository.findByNwaWDDocumentId(nwaWDDocumentId) ?: throw ExpectedDataNotFound("No File found with the following [ id=$nwaWDDocumentId]")
    }

    //Decision on WorkShop Draft
    fun decisionOnWD(nwaWorkshopDraftDecision: NWAWorkshopDraftDecision) : List<TaskDetails> {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        variables["Yes"] = nwaWorkshopDraftDecision.accentTo
        nwaWorkshopDraftDecision.comments.let { variables.put("comments", it) }
        if(variables["Yes"]==true){
            val assignedKsNumber= getKSNumber()

            variables["ksNumber"] = assignedKsNumber
            nwaWorkshopDraftRepository.findByIdOrNull(nwaWorkshopDraftDecision.approvalID)?.let { nwaWorkShopDraft->

                with(nwaWorkShopDraft){
                    remarks=nwaWorkshopDraftDecision.comments
                    accentTo = true
                    ksNumber = assignedKsNumber

                    // auto assign KNWA  number
                }
                nwaWorkshopDraftRepository.save(nwaWorkShopDraft)
            }?: throw Exception("TASK NOT FOUND")

        }else if(variables["No"]==false) {
            nwaWorkshopDraftRepository.findByIdOrNull(nwaWorkshopDraftDecision.approvalID)?.let { nwaWorkShopDraft->

                with(nwaWorkShopDraft){
                    remarks=nwaWorkshopDraftDecision.comments
                   // accentTo = false
                }
                nwaWorkshopDraftRepository.save(nwaWorkShopDraft)
            }?: throw Exception("TASK NOT FOUND")
        }
        taskService.complete(nwaWorkshopDraftDecision.taskId, variables)
        return  getSacSecTasks()
    }

    // Upload NWA Standard
    fun uploadNwaStandard(nWAStandard: NWAStandard) : ProcessInstanceUS
    {
        val variable:MutableMap<String, Any> = HashMap()
        nWAStandard.title?.let{variable.put("title", it)}
        nWAStandard.scope?.let{variable.put("scope", it)}
        nWAStandard.normativeReference?.let{variable.put("normativeReference", it)}
        nWAStandard.symbolsAbbreviatedTerms?.let{variable.put("symbolsAbbreviatedTerms", it)}
        nWAStandard.clause?.let{variable.put("clause", it)}
        nWAStandard.special?.let{variable.put("special", it)}
        nWAStandard.ksNumber?.let{variable.put("ksNumber", it)}
        nWAStandard.dateSdUploaded = commonDaoServices.getTimestamp()
        variable["dateSdUploaded"] = nWAStandard.dateSdUploaded!!

        //print(nWAStandard.toString())
        val nwaDetails = nwaStandardRepository.save(nWAStandard)

        // Send email to Legal
        variable["ID"] = nwaDetails.id
        taskService.complete(nWAStandard.taskId, variable)
        println("NWA Standard Uploaded")
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variable)
        return ProcessInstanceUS(nwaDetails.id, processInstance.id, processInstance.isEnded,nWAStandard.ksNumber?: throw NullValueNotAllowedException("Standard Number is required"))

    }
    // Upload nwa Standard Document
    fun uploadSTDFile(
        uploads: NWAStandardUploads,
        docFile: MultipartFile,
        doc: String,
        user: UsersEntity,
        DocDescription: String
    ): NWAStandardUploads {

        with(uploads) {
//            filepath = docFile.path
            name = commonDaoServices.saveDocuments(docFile)
//            fileType = docFile.contentType
            fileType = docFile.contentType
            documentType = doc
            description=DocDescription
            document = docFile.bytes
            transactionDate = commonDaoServices.getCurrentDate()
            status = 1
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }

        return nwaStandardUploadsRepository.save(uploads)
    }


    //Return task details for HO SIC
    fun getHoSiCTasks():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_HO_SIC).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    // View STD Document upload
    fun findUploadedSTDFileBYId(nwaStdDocumentId: Long): NWAStandardUploads {
        return nwaStandardUploadsRepository.findByNwaStdDocumentId(nwaStdDocumentId) ?: throw ExpectedDataNotFound("No File found with the following [ id=$nwaStdDocumentId]")
    }

    // Upload NWA Gazette notice on Website
    fun uploadGazetteNotice(nWAGazetteNotice: NWAGazetteNotice)
    {
        val variable:MutableMap<String, Any> = HashMap()
        nWAGazetteNotice.ksNumber?.let{variable.put("ksNumber", it)}
        //nWAGazetteNotice.dateUploaded?.let{variable.put("dateUploaded", it)}
        nWAGazetteNotice.description?.let{variable.put("description", it)}
        nWAGazetteNotice.dateUploaded = commonDaoServices.getTimestamp()
        variable["dateUploaded"] = nWAGazetteNotice.dateUploaded!!

        print(nWAGazetteNotice.toString())
        val nwaDetails = nwaGazetteNoticeRepository.save(nWAGazetteNotice)
        variable["ID"] = nwaDetails.id

        taskService.complete(nWAGazetteNotice.taskId, variable)
        println("NWA Gazette Notice has been uploaded")

    }

    // Upload NWA Gazette date
    fun updateGazettementDate(nWAGazettement: NWAGazettement)
    {
        val variable:MutableMap<String, Any> = HashMap()
        nWAGazettement.ksNumber?.let{variable.put("ksNumber", it)}
        //nWAGazettement.dateOfGazettement?.let{variable.put("dateOfGazettement", it)}
        nWAGazettement.description?.let{variable.put("description", it)}

        nWAGazettement.dateOfGazettement = Timestamp(System.currentTimeMillis())
        variable["dateOfGazettement"] = nWAGazettement.dateOfGazettement!!
        print(nWAGazettement.toString())

        nwaGazettementRepository.save(nWAGazettement)
        taskService.complete(nWAGazettement.taskId, variable)
        println("NWA Gazettement date has been updated")

    }

    //Get Process History
//    fun checkProcessHistory(processId: String?) {
//        val historyService = processEngine.historyService
//        val activities = historyService
//            .createHistoricActivityInstanceQuery()
//            .processInstanceId(processId)
//            .finished()
//            .orderByHistoricActivityInstanceEndTime()
//            .asc()
//            .list()
//        for (activity in activities) {
//            println(
//                activity.activityId + " took " + activity.durationInMillis + " milliseconds")
//        }
//
//    }
    fun checkProcessHistory(id: ID): List<HistoricActivityInstance> {
        val historyService = processEngine.historyService
        val activities = historyService
            .createHistoricActivityInstanceQuery()
            .processInstanceId(id.ID)
            .finished()
            .orderByHistoricActivityInstanceEndTime()
            .asc()
            .list()
        for (activity in activities) {
            println(
                activity.activityId + " took " + activity.durationInMillis + " milliseconds" + activity.processInstanceId
            )
        }

        return activities

    }
    fun getRQNumber(): String
    {
        val allRequests =nwaJustificationRepository.findAllByOrderByIdDesc()

        var lastId:String?="0"
        var finalValue =1
        var startId="RQ"


        for(item in allRequests){
            println(item)
            lastId = item.requestNumber
            break
        }

        if(lastId != "0")
        {
            val strs = lastId?.split(":")?.toTypedArray()

            val firstPortion = strs?.get(0)

            val lastPortArray = firstPortion?.split("/")?.toTypedArray()

            val intToIncrement =lastPortArray?.get(1)

            finalValue = (intToIncrement?.toInt()!!)
            finalValue += 1
        }


        val year = Calendar.getInstance()[Calendar.YEAR]

        return "$startId/$finalValue:$year"
    }

    fun getCDNumber(): Pair<String, Long>
    {
        //val allRequests: Int
        var allRequests =nwaDisDtJustificationRepository.getMaxCDN()

       // var lastId:String?="0"
        var finalValue =1
        var startId="CD"

        allRequests = allRequests.plus(1)
//        for(item in allRequests){
//            println(item)
//            lastId = item.cdAppNumber
//            break
//        }




        val year = Calendar.getInstance()[Calendar.YEAR]

        return Pair("$startId/$allRequests:$year", allRequests)
    }

    fun getKSNumber(): String
    {
        val allRequests =nwaStandardRepository.findAllByOrderByIdDesc()

        var lastId:String?="0"
        var finalValue =1
        var startId="KS"


        for(item in allRequests){
            println(item)
            lastId = item.ksNumber
            break
        }

        if(lastId != "0")
        {
            val strs = lastId?.split(":")?.toTypedArray()

            val firstPortion = strs?.get(0)

            val lastPortArray = firstPortion?.split("/")?.toTypedArray()

            val intToIncrement =lastPortArray?.get(1)

            finalValue = (intToIncrement?.toInt()!!)
            finalValue += 1
        }


        val year = Calendar.getInstance()[Calendar.YEAR]

        return "$startId/$finalValue:$year"
    }

//    fun closeTask(taskId: String) {
//        taskService.complete(taskId)
//    }
    //*** Not used *** but closes any Task, linked to task close endpoint
    fun closeTask(taskId: String) {
        taskService.complete(taskId)
        taskService.deleteTask(taskId, true)

    }

    fun closeProcess(taskId: String) {
        // taskService.complete(taskId)
        // taskService.deleteTask(taskId, true)

        runtimeService.deleteProcessInstance(taskId, "cleaning")
    }
}
