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
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.StandardsLevyBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.dto.std.*
import org.kebs.app.kotlin.apollo.common.dto.stdLevy.ProcessInstanceResponseSite
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
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
                 private val notifications: Notifications,
                 private val bpmnService: StandardsLevyBpmn,
                 private val userListRepository: UserListRepository,
                 private val standardNwaRemarksRepository: StandardNwaRemarksRepository,
                 private val companyStandardRepository: CompanyStandardRepository,
                 private val standardRepository: StandardRepository


                 ) {

    val PROCESS_DEFINITION_KEY = "sd_KenyaNationalWorkshopAgreementModule"

    //deploy bpmn file
    fun deployProcessDefinition(): Deployment =repositoryService
        .createDeployment()
        .addClasspathResource("processes/std/Kenya_National_Workshop_Agreement_Process.bpmn20.xml")
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
        nwaJustification.requestedBy?.let{ variables.put("requestedBy", it)}
        nwaJustification.issuesAddressed?.let{ variables.put("issuesAddressed", it)}
        nwaJustification.knwAcceptanceDate?.let{ variables.put("knwAcceptanceDate", it)}
        nwaJustification.referenceMaterial?.let{ variables.put("referenceMaterial", it)}
        nwaJustification.department?.let{ variables.put("department", it)}
        nwaJustification.status?.let{ variables.put("status", it)}
        nwaJustification.remarks?.let{ variables.put("remarks", it)}
        nwaJustification.assignedTo= companyStandardRepository.getKnwSecId()

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
        taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
            ?.let { t ->
                t.list()[0]
                    ?.let { task ->
                        task.assignee =
                            "${nwaJustification.assignedTo ?: throw NullValueNotAllowedException(" invalid user id provided")}"  //set the assignee}"

                        taskService.saveTask(task)
                    }
                    ?: KotlinLogging.logger { }.error("Task list empty for $PROCESS_DEFINITION_KEY ")


            }
            ?: KotlinLogging.logger { }.error("No task found for $PROCESS_DEFINITION_KEY ")
        bpmnService.slAssignTask(
            processInstance.processInstanceId,
            "Review Justification",
            nwaJustification?.assignedTo
                ?: throw NullValueNotAllowedException("invalid user id provided")
        )

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
    private fun getTaskDetails(tasks: List<Task>): List<WorkShopAgreementTasks> {
        val taskDetails: MutableList<WorkShopAgreementTasks> = ArrayList()
        for (task in tasks) {
            val processVariables = taskService.getVariables(task.id)
            taskDetails.add(WorkShopAgreementTasks(task.id, task.name,task.processInstanceId, processVariables))

        }
        return taskDetails
    }

    //Return task details for KNW_SEC
//    fun getKNWTasks():List<TaskDetails>
//    {
//
//        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_KNW_SEC).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
//        return getTaskDetails(tasks)
//    }

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
    fun decisionOnJustificationKNW(
        nwaJustificationDecision: NWAJustificationDecision,
        standardNwaRemarks: StandardNwaRemarks
    ) : List<WorkShopAgreementTasks> {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        variables["Yes"] = nwaJustificationDecision.accentTo
        variables["No"] = nwaJustificationDecision.accentTo
        loggedInUser.id?.let { variables["originator"] = it }
        nwaJustificationDecision.comments.let { variables.put("comments", it) }
        nwaJustificationDecision.taskId?.let { variables.put("taskId", it) }
        nwaJustificationDecision.assignedTo= companyStandardRepository.getSpcSecId()

        val fname=loggedInUser.firstName
        val sname=loggedInUser.lastName
        val usersName= "$fname  $sname"
        standardNwaRemarks.processId= nwaJustificationDecision.processId
        standardNwaRemarks.remarks= nwaJustificationDecision.comments
        standardNwaRemarks.status = 1.toString()
        standardNwaRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
        standardNwaRemarks.remarkBy = usersName
        if(variables["Yes"]==true){
            nwaJustificationRepository.findByIdOrNull(nwaJustificationDecision.approvalID)?.let { nwaJustification->

                with(nwaJustification){
                    remarks=nwaJustificationDecision.comments
                    //accentTo = true
                }
                nwaJustificationRepository.save(nwaJustification)
                standardNwaRemarksRepository.save(standardNwaRemarks)
                runtimeService.createProcessInstanceQuery()
                    .processInstanceId(nwaJustificationDecision.processId).list()
                    ?.let { l ->
                        val processInstance = l[0]
                        taskService.complete(nwaJustificationDecision.taskId, variables)

                        taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
                            ?.let { t ->
                                t.list()[0]
                                    ?.let { task ->
                                        task.assignee = "${
                                            nwaJustificationDecision.assignedTo ?: throw NullValueNotAllowedException(
                                                " invalid user id provided"
                                            )
                                        }"  //set the assignee}"
                                        //task.dueDate = standardLevyFactoryVisitReportEntity.scheduledVisitDate  //set the due date
                                        taskService.saveTask(task)
                                    }
                                    ?: KotlinLogging.logger { }
                                        .error("Task list empty for $PROCESS_DEFINITION_KEY ")


                            }
                            ?: KotlinLogging.logger { }.error("No task found for $PROCESS_DEFINITION_KEY ")
                        bpmnService.slAssignTask(
                            processInstance.processInstanceId,
                            "View Justification",
                            nwaJustificationDecision.assignedTo
                                ?: throw NullValueNotAllowedException("invalid user id provided")
                        )

                    }
                    ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${nwaJustificationDecision.processId} ")

            }?: throw Exception("TASK NOT FOUND")

        }else if(variables["No"]==false) {
            nwaJustificationRepository.findByIdOrNull(nwaJustificationDecision.approvalID)?.let { nwaJustification->

                with(nwaJustification){
                    remarks=nwaJustificationDecision.comments
                    //accentTo = false
                }
                nwaJustificationRepository.save(nwaJustification)
                standardNwaRemarksRepository.save(standardNwaRemarks)
            }?: throw Exception("TASK NOT FOUND")

        }
        taskService.complete(nwaJustificationDecision.taskId, variables)
        return  getUserTasks()
    }

    //Return task details for SPC_SEC
//    fun getSPCSECTasks():List<TaskDetails>
//    {
//        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_SPC_SEC).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
//        return getTaskDetails(tasks)
//    }



    // SPC Decision on Justification

    fun decisionOnJustification(nwaJustificationDecision: NWAJustificationDecision,
                                standardNwaRemarks: StandardNwaRemarks) : List<WorkShopAgreementTasks> {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        variables["Yes"] = nwaJustificationDecision.accentTo
        variables["No"] = nwaJustificationDecision.accentTo
        nwaJustificationDecision.comments.let { variables.put("comments", it) }
        nwaJustificationDecision.taskId.let { variables.put("taskId", it) }
        nwaJustificationDecision.assignedTo= companyStandardRepository.getKnwSecId()
        val fname=loggedInUser.firstName
        val sname=loggedInUser.lastName
        val usersName= "$fname  $sname"
        standardNwaRemarks.processId= nwaJustificationDecision.processId
        standardNwaRemarks.remarks= nwaJustificationDecision.comments
        standardNwaRemarks.status = 1.toString()
        standardNwaRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
        standardNwaRemarks.remarkBy = usersName
        if(variables["Yes"]==true){
            nwaJustificationRepository.findByIdOrNull(nwaJustificationDecision.approvalID)?.let { nwaJustification->

                with(nwaJustification){
                    remarks=nwaJustificationDecision.comments
                    //accentTo = true
                }
                nwaJustificationRepository.save(nwaJustification)
                standardNwaRemarksRepository.save(standardNwaRemarks)
                runtimeService.createProcessInstanceQuery()
                    .processInstanceId(nwaJustificationDecision.processId).list()
                    ?.let { l ->
                        val processInstance = l[0]
                        taskService.complete(nwaJustificationDecision.taskId, variables)

                        taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
                            ?.let { t ->
                                t.list()[0]
                                    ?.let { task ->
                                        task.assignee = "${
                                            nwaJustificationDecision.assignedTo ?: throw NullValueNotAllowedException(
                                                " invalid user id provided"
                                            )
                                        }"  //set the assignee}"
                                        //task.dueDate = standardLevyFactoryVisitReportEntity.scheduledVisitDate  //set the due date
                                        taskService.saveTask(task)
                                    }
                                    ?: KotlinLogging.logger { }
                                        .error("Task list empty for $PROCESS_DEFINITION_KEY ")


                            }
                            ?: KotlinLogging.logger { }.error("No task found for $PROCESS_DEFINITION_KEY ")
                        bpmnService.slAssignTask(
                            processInstance.processInstanceId,
                            "Prepare Justification for DI-SDT Approval",
                            nwaJustificationDecision.assignedTo
                                ?: throw NullValueNotAllowedException("invalid user id provided")
                        )

                    }
                    ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${nwaJustificationDecision.processId} ")
            }?: throw Exception("TASK NOT FOUND")

        }else if(variables["No"]==false) {
            nwaJustificationRepository.findByIdOrNull(nwaJustificationDecision.approvalID)?.let { nwaJustification->

                with(nwaJustification){
                    remarks=nwaJustificationDecision.comments
                    //accentTo = false
                }
                nwaJustificationRepository.save(nwaJustification)
                standardNwaRemarksRepository.save(standardNwaRemarks)
                runtimeService.createProcessInstanceQuery()
                    .processInstanceId(nwaJustificationDecision.processId).list()
                    ?.let { l ->
                        val processInstance = l[0]
                        taskService.complete(nwaJustificationDecision.taskId, variables)

                        taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
                            ?.let { t ->
                                t.list()[0]
                                    ?.let { task ->
                                        task.assignee = "${
                                            nwaJustificationDecision.assignedTo ?: throw NullValueNotAllowedException(
                                                " invalid user id provided"
                                            )
                                        }"  //set the assignee}"
                                        //task.dueDate = standardLevyFactoryVisitReportEntity.scheduledVisitDate  //set the due date
                                        taskService.saveTask(task)
                                    }
                                    ?: KotlinLogging.logger { }
                                        .error("Task list empty for $PROCESS_DEFINITION_KEY ")


                            }
                            ?: KotlinLogging.logger { }.error("No task found for $PROCESS_DEFINITION_KEY ")
                        bpmnService.slAssignTask(
                            processInstance.processInstanceId,
                            "Review Justification",
                            nwaJustificationDecision.assignedTo
                                ?: throw NullValueNotAllowedException("invalid user id provided")
                        )

                    }
                    ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${nwaJustificationDecision.processId} ")
            }?: throw Exception("TASK NOT FOUND")

        }
        taskService.complete(nwaJustificationDecision.taskId, variables)
        return  getUserTasks()
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
        nwaDiSdtJustification.assignedTo= companyStandardRepository.getDiDirectorId()
        nwaDiSdtJustification.datePrepared = commonDaoServices.getTimestamp()
        nwaDiSdtJustification.datePrepared?.let{variable.put("datePrepared", it)}
        nwaDiSdtJustification.taskId?.let{variable.put("taskId", it)}
        nwaDiSdtJustification.processId?.let{variable.put("processId", it)}
        loggedInUser.id?.let { variable["diOriginator"] = it }

        //print(nwaDiSdtJustification.toString())


        val nwaDetails = nwaDisDtJustificationRepository.save(nwaDiSdtJustification)
        variable["ID"] = nwaDetails.id
        runtimeService.createProcessInstanceQuery()
            .processInstanceId(nwaDiSdtJustification.processId).list()
            ?.let { l ->
                val processInstance = l[0]

                taskService.complete(nwaDiSdtJustification.taskId, variable)

                taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
                    ?.let { t ->
                        t.list()[0]
                            ?.let { task ->
                                task.assignee =
                                    "${nwaDiSdtJustification.assignedTo ?: throw NullValueNotAllowedException(" invalid user id provided")}"  //set the assignee}"
                                taskService.saveTask(task)
                            }
                            ?: KotlinLogging.logger { }.error("Task list empty for $PROCESS_DEFINITION_KEY ")


                    }
                    ?: KotlinLogging.logger { }.error("No task found for $PROCESS_DEFINITION_KEY ")
                bpmnService.slAssignTask(
                    processInstance.processInstanceId,
                    "View Justification For DiSDT Approval",
                    nwaDiSdtJustification.assignedTo ?: throw NullValueNotAllowedException("invalid user id provided")
                )
                return ProcessInstanceDISDT(
                    nwaDetails.id,
                    processInstance.id,
                    processInstance.isEnded,nwaDiSdtJustification.datePrepared?: throw NullValueNotAllowedException("Date is required")
                )
            }
            ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${nwaDiSdtJustification.processId} ")
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
//    fun getDISDTTasks():List<TaskDetails>
//    {
//        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_DI_SDT).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
//        return getTaskDetails(tasks)
//    }

    // View DI SDT Document upload
    fun findUploadedDIFileBYId(diDocumentId: Long): SDDIJustificationUploads {
        return sdDiJustificationUploadsRepository.findByDiDocumentId(diDocumentId) ?: throw ExpectedDataNotFound("No File found with the following [ id=$diDocumentId]")
    }
    //Return task details for TC_SEC
//    fun getTCSeCTasks():List<TaskDetails>
//    {
//
//        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_TC_SEC).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
//        return getTaskDetails(tasks)
//    }

    //Decision on DI-SDT
    fun decisionOnDiSdtJustification(workshopAgreement: WorkshopAgreement,
                                     standardNwaRemarks: StandardNwaRemarks) : List<WorkShopAgreementTasks> {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        variables["Yes"] = workshopAgreement.accentTo
        variables["No"] = workshopAgreement.accentTo
        workshopAgreement.comments.let { variables.put("comments", it) }
        workshopAgreement.taskId.let { variables.put("taskId", it) }
        val fname=loggedInUser.firstName
        val sname=loggedInUser.lastName
        val usersName= "$fname  $sname"
        standardNwaRemarks.processId= workshopAgreement.processId
        standardNwaRemarks.remarks= workshopAgreement.comments
        standardNwaRemarks.status = 1.toString()
        standardNwaRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
        standardNwaRemarks.remarkBy = usersName
        if(variables["Yes"]==true){
            workshopAgreement.assignedTo= companyStandardRepository.getTcSecId()
            nwaDisDtJustificationRepository.findByIdOrNull(workshopAgreement.approvalID)?.let { nwaDiSdtJustification->
                val valueFound =getCDNumber()
                with(nwaDiSdtJustification){
                    cdAppNumber = valueFound.first
                    cdn = valueFound.second
                    remarks=workshopAgreement.comments
                    accentTo = true
                }
                nwaDisDtJustificationRepository.save(nwaDiSdtJustification)
                standardNwaRemarksRepository.save(standardNwaRemarks)
                runtimeService.createProcessInstanceQuery()
                    .processInstanceId(workshopAgreement.processId).list()
                    ?.let { l ->
                        val processInstance = l[0]
                        taskService.complete(workshopAgreement.taskId, variables)

                        taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
                            ?.let { t ->
                                t.list()[0]
                                    ?.let { task ->
                                        task.assignee = "${
                                            workshopAgreement.assignedTo ?: throw NullValueNotAllowedException(
                                                " invalid user id provided"
                                            )
                                        }"  //set the assignee}"
                                        //task.dueDate = standardLevyFactoryVisitReportEntity.scheduledVisitDate  //set the due date
                                        taskService.saveTask(task)
                                    }
                                    ?: KotlinLogging.logger { }
                                        .error("Task list empty for $PROCESS_DEFINITION_KEY ")


                            }
                            ?: KotlinLogging.logger { }.error("No task found for $PROCESS_DEFINITION_KEY ")
                        bpmnService.slAssignTask(
                            processInstance.processInstanceId,
                            "Prepare Preliminary Draft",
                            workshopAgreement.assignedTo
                                ?: throw NullValueNotAllowedException("invalid user id provided")
                        )

                    }
                    ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${workshopAgreement.processId} ")
            }?: throw Exception("TASK NOT FOUND")

        }else if(variables["No"]==false) {
            workshopAgreement.assignedTo= companyStandardRepository.getKnwSecId()
            nwaJustificationRepository.findByIdOrNull(workshopAgreement.jstNumber)?.let { nwaJustification->

                with(nwaJustification){
                    remarks=workshopAgreement.comments
                   // accentTo = false
                }
                nwaJustificationRepository.save(nwaJustification)
                standardNwaRemarksRepository.save(standardNwaRemarks)
                runtimeService.createProcessInstanceQuery()
                    .processInstanceId(workshopAgreement.processId).list()
                    ?.let { l ->
                        val processInstance = l[0]
                        taskService.complete(workshopAgreement.taskId, variables)

                        taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
                            ?.let { t ->
                                t.list()[0]
                                    ?.let { task ->
                                        task.assignee = "${
                                            workshopAgreement.assignedTo ?: throw NullValueNotAllowedException(
                                                " invalid user id provided"
                                            )
                                        }"  //set the assignee}"
                                        //task.dueDate = standardLevyFactoryVisitReportEntity.scheduledVisitDate  //set the due date
                                        taskService.saveTask(task)
                                    }
                                    ?: KotlinLogging.logger { }
                                        .error("Task list empty for $PROCESS_DEFINITION_KEY ")


                            }
                            ?: KotlinLogging.logger { }.error("No task found for $PROCESS_DEFINITION_KEY ")
                        bpmnService.slAssignTask(
                            processInstance.processInstanceId,
                            "Prepare Justification for DI-SDT Approval",
                            workshopAgreement.assignedTo
                                ?: throw NullValueNotAllowedException("invalid user id provided")
                        )

                    }
                    ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${workshopAgreement.processId} ")
            }?: throw Exception("TASK NOT FOUND")

        }

        return  getUserTasks()
    }



    // Prepare Preliminary Draft
    fun preparePreliminaryDraft(
        nwaPreliminaryDraft: NWAPreliminaryDraft,
        standardNwaRemarks: StandardNwaRemarks
       ) : ProcessInstancePD
    {
        val variable:MutableMap<String, Any> = HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        nwaPreliminaryDraft.title?.let{variable.put("title", it)}
        nwaPreliminaryDraft.scope?.let{variable.put("scope", it)}
        nwaPreliminaryDraft.normativeReference?.let{variable.put("normativeReference", it)}
        nwaPreliminaryDraft.symbolsAbbreviatedTerms?.let{variable.put("symbolsAbbreviatedTerms", it)}
        nwaPreliminaryDraft.clause?.let{variable.put("clause", it)}
        nwaPreliminaryDraft.special?.let{variable.put("special", it)}
        nwaPreliminaryDraft.taskId?.let{variable.put("taskId", it)}
        nwaPreliminaryDraft.diJNumber?.let{variable.put("diJNumber", it)}
        nwaPreliminaryDraft.assignedTo= companyStandardRepository.getKnwSecId()
        nwaPreliminaryDraft.datePdPrepared = commonDaoServices.getTimestamp()
        variable["datePdPrepared"] = nwaPreliminaryDraft.datePdPrepared!!
        loggedInUser.id?.let { variable["pdOriginator"] = it }
        standardNwaRemarks.processId= nwaPreliminaryDraft.processId


        val nwaDetails = nwaPreliminaryDraftRepository.save(nwaPreliminaryDraft)
        variable["ID"] = nwaDetails.id
        runtimeService.createProcessInstanceQuery()
            .processInstanceId(nwaPreliminaryDraft.processId).list()
            ?.let { l ->
                val processInstance = l[0]

                taskService.complete(nwaPreliminaryDraft.taskId, variable)

                taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
                    ?.let { t ->
                        t.list()[0]
                            ?.let { task ->
                                task.assignee =
                                    "${nwaPreliminaryDraft.assignedTo ?: throw NullValueNotAllowedException(" invalid user id provided")}"  //set the assignee}"
                                taskService.saveTask(task)
                            }
                            ?: KotlinLogging.logger { }.error("Task list empty for $PROCESS_DEFINITION_KEY ")


                    }
                    ?: KotlinLogging.logger { }.error("No task found for $PROCESS_DEFINITION_KEY ")
                bpmnService.slAssignTask(
                    processInstance.processInstanceId,
                    "View and Edit Preliminary Draft",
                    nwaPreliminaryDraft.assignedTo ?: throw NullValueNotAllowedException("invalid user id provided")
                )
                return ProcessInstancePD(
                    nwaDetails.id,
                    processInstance.id,
                    processInstance.isEnded,nwaPreliminaryDraft.datePdPrepared?: throw NullValueNotAllowedException("Date is required")
                )
            }
            ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${nwaPreliminaryDraft.processId} ")

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

    fun decisionOnPD(nwaPreliminaryDraftDecision: NWAPreliminaryDraftDecision,
                     standardNwaRemarks: StandardNwaRemarks) : List<WorkShopAgreementTasks> {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        variables["Yes"] = nwaPreliminaryDraftDecision.accentTo
        nwaPreliminaryDraftDecision.comments.let { variables.put("comments", it) }
        nwaPreliminaryDraftDecision.taskId.let { variables.put("taskId", it) }
        val fname=loggedInUser.firstName
        val sname=loggedInUser.lastName
        val usersName= "$fname  $sname"
        standardNwaRemarks.processId= nwaPreliminaryDraftDecision.processId
        standardNwaRemarks.remarks= nwaPreliminaryDraftDecision.comments
        standardNwaRemarks.status = 1.toString()
        standardNwaRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
        standardNwaRemarks.remarkBy = usersName
        loggedInUser.id?.let { variables["vpdOriginator"] = it }
        if(variables["Yes"]==true){
            nwaPreliminaryDraftDecision.assignedTo= companyStandardRepository.getHopId()
            nwaPreliminaryDraftRepository.findByIdOrNull(nwaPreliminaryDraftDecision.approvalID)?.let { nwaPreliminaryDraft->

                with(nwaPreliminaryDraft){
                    remarks=nwaPreliminaryDraftDecision.comments
                   // accentTo = "true"
                }
                nwaPreliminaryDraftRepository.save(nwaPreliminaryDraft)
                standardNwaRemarksRepository.save(standardNwaRemarks)
                runtimeService.createProcessInstanceQuery()
                    .processInstanceId(nwaPreliminaryDraftDecision.processId).list()
                    ?.let { l ->
                        val processInstance = l[0]
                        taskService.complete(nwaPreliminaryDraftDecision.taskId, variables)

                        taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
                            ?.let { t ->
                                t.list()[0]
                                    ?.let { task ->
                                        task.assignee = "${
                                            nwaPreliminaryDraftDecision.assignedTo ?: throw NullValueNotAllowedException(
                                                " invalid user id provided"
                                            )
                                        }"  //set the assignee}"
                                        //task.dueDate = standardLevyFactoryVisitReportEntity.scheduledVisitDate  //set the due date
                                        taskService.saveTask(task)
                                    }
                                    ?: KotlinLogging.logger { }
                                        .error("Task list empty for $PROCESS_DEFINITION_KEY ")


                            }
                            ?: KotlinLogging.logger { }.error("No task found for $PROCESS_DEFINITION_KEY ")
                        bpmnService.slAssignTask(
                            processInstance.processInstanceId,
                            "Prepare Preliminary Draft",
                            nwaPreliminaryDraftDecision.assignedTo
                                ?: throw NullValueNotAllowedException("invalid user id provided")
                        )

                    }
                    ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${nwaPreliminaryDraftDecision.processId} ")
            }?: throw Exception("TASK NOT FOUND")

        }else if(variables["No"]==false) {
            nwaPreliminaryDraftDecision.assignedTo= companyStandardRepository.getTcSecId()
            nwaDisDtJustificationRepository.findByIdOrNull(nwaPreliminaryDraftDecision.diJNumber)?.let { nwaDiSdtJustification->

                with(nwaDiSdtJustification){
                    remarks=nwaPreliminaryDraftDecision.comments
                    //accentTo = false
                }
                nwaDisDtJustificationRepository.save(nwaDiSdtJustification)
                standardNwaRemarksRepository.save(standardNwaRemarks)
                runtimeService.createProcessInstanceQuery()
                    .processInstanceId(nwaPreliminaryDraftDecision.processId).list()
                    ?.let { l ->
                        val processInstance = l[0]
                        taskService.complete(nwaPreliminaryDraftDecision.taskId, variables)

                        taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
                            ?.let { t ->
                                t.list()[0]
                                    ?.let { task ->
                                        task.assignee = "${
                                            nwaPreliminaryDraftDecision.assignedTo ?: throw NullValueNotAllowedException(
                                                " invalid user id provided"
                                            )
                                        }"  //set the assignee}"
                                        //task.dueDate = standardLevyFactoryVisitReportEntity.scheduledVisitDate  //set the due date
                                        taskService.saveTask(task)
                                    }
                                    ?: KotlinLogging.logger { }
                                        .error("Task list empty for $PROCESS_DEFINITION_KEY ")
                            }
                            ?: KotlinLogging.logger { }.error("No task found for $PROCESS_DEFINITION_KEY ")
                        bpmnService.slAssignTask(
                            processInstance.processInstanceId,
                            "Editing of FDKNWA (Workshop Draft)",
                            nwaPreliminaryDraftDecision.assignedTo
                                ?: throw NullValueNotAllowedException("invalid user id provided")
                        )

                    }
                    ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${nwaPreliminaryDraftDecision.processId} ")
            }?: throw Exception("TASK NOT FOUND")

        }

        return  getUserTasks()
    }

    //Return task details for Head of Publishing
//    fun getHOPTasks():List<TaskDetails>
//    {
//        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_HOP).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
//        return getTaskDetails(tasks)
//    }

    // Edit Workshop  Draft
    fun editWorkshopDraft(nwaWorkShopDraft: NWAWorkShopDraft,
                          standardNwaRemarks: StandardNwaRemarks) : ProcessInstanceWD
    {
        val variable:MutableMap<String, Any> = HashMap()
        nwaWorkShopDraft.title?.let{variable.put("title", it)}
        nwaWorkShopDraft.scope?.let{variable.put("scope", it)}
        nwaWorkShopDraft.normativeReference?.let{variable.put("normativeReference", it)}
        nwaWorkShopDraft.symbolsAbbreviatedTerms?.let{variable.put("symbolsAbbreviatedTerms", it)}
        nwaWorkShopDraft.clause?.let{variable.put("clause", it)}
        nwaWorkShopDraft.special?.let{variable.put("special", it)}
        nwaWorkShopDraft.taskId?.let{variable.put("taskId", it)}
        nwaWorkShopDraft.processId?.let{variable.put("processId", it)}
        nwaWorkShopDraft.dateWdPrepared = commonDaoServices.getTimestamp()
        variable["dateWdPrepared"] = nwaWorkShopDraft.dateWdPrepared!!
        nwaWorkShopDraft.assignedTo= companyStandardRepository.getSaSecId()

        //print(nwaWorkShopDraft.toString())

        val nwaDetails = nwaWorkshopDraftRepository.save(nwaWorkShopDraft)
        variable["ID"] = nwaDetails.id
        runtimeService.createProcessInstanceQuery()
            .processInstanceId(nwaWorkShopDraft.processId).list()
            ?.let { l ->
                val processInstance = l[0]

                taskService.complete(nwaWorkShopDraft.taskId, variable)

                taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
                    ?.let { t ->
                        t.list()[0]
                            ?.let { task ->
                                task.assignee =
                                    "${nwaWorkShopDraft.assignedTo ?: throw NullValueNotAllowedException(" invalid user id provided")}"  //set the assignee}"
                                taskService.saveTask(task)
                            }
                            ?: KotlinLogging.logger { }.error("Task list empty for $PROCESS_DEFINITION_KEY ")


                    }
                    ?: KotlinLogging.logger { }.error("No task found for $PROCESS_DEFINITION_KEY ")
                bpmnService.slAssignTask(
                    processInstance.processInstanceId,
                    "View Workshop Draft",
                    nwaWorkShopDraft.assignedTo ?: throw NullValueNotAllowedException("invalid user id provided")
                )
                return ProcessInstanceWD(
                    nwaDetails.id,
                    processInstance.id,
                    processInstance.isEnded,nwaWorkShopDraft.dateWdPrepared?: throw NullValueNotAllowedException("Date is required")
                )
            }
            ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${nwaWorkShopDraft.processId} ")



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
//    fun getSacSecTasks():List<TaskDetails>
//    {
//        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_SAC_SEC).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
//        return getTaskDetails(tasks)
//    }

    // View WD Document upload
    fun findUploadedWDFileBYId(nwaWDDocumentId: Long): NWAWorkShopDraftUploads {
        return nwaWorkShopDraftUploadsRepository.findByNwaWDDocumentId(nwaWDDocumentId) ?: throw ExpectedDataNotFound("No File found with the following [ id=$nwaWDDocumentId]")
    }

    //Decision on WorkShop Draft
    fun decisionOnWD(nwaWorkshopDraftDecision: NWAWorkshopDraftDecision,
                     standardNwaRemarks: StandardNwaRemarks) : List<WorkShopAgreementTasks> {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        variables["Yes"] = nwaWorkshopDraftDecision.accentTo
        nwaWorkshopDraftDecision.comments.let { variables.put("comments", it) }
        nwaWorkshopDraftDecision.taskId.let { variables.put("taskId", it) }
        val fname=loggedInUser.firstName
        val sname=loggedInUser.lastName
        val usersName= "$fname  $sname"
        standardNwaRemarks.processId= nwaWorkshopDraftDecision.processId
        standardNwaRemarks.remarks= nwaWorkshopDraftDecision.comments
        standardNwaRemarks.status = 1.toString()
        standardNwaRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
        standardNwaRemarks.remarkBy = usersName
        if(variables["Yes"]==true){
            nwaWorkshopDraftDecision.assignedTo= companyStandardRepository.getHopId()
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
                standardNwaRemarksRepository.save(standardNwaRemarks)
                runtimeService.createProcessInstanceQuery()
                    .processInstanceId(nwaWorkshopDraftDecision.processId).list()
                    ?.let { l ->
                        val processInstance = l[0]
                        taskService.complete(nwaWorkshopDraftDecision.taskId, variables)

                        taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
                            ?.let { t ->
                                t.list()[0]
                                    ?.let { task ->
                                        task.assignee = "${
                                            nwaWorkshopDraftDecision.assignedTo ?: throw NullValueNotAllowedException(
                                                " invalid user id provided"
                                            )
                                        }"  //set the assignee}"
                                        //task.dueDate = standardLevyFactoryVisitReportEntity.scheduledVisitDate  //set the due date
                                        taskService.saveTask(task)
                                    }
                                    ?: KotlinLogging.logger { }
                                        .error("Task list empty for $PROCESS_DEFINITION_KEY ")


                            }
                            ?: KotlinLogging.logger { }.error("No task found for $PROCESS_DEFINITION_KEY ")
                        bpmnService.slAssignTask(
                            processInstance.processInstanceId,
                            "Upload on KNWA Standards Database",
                            nwaWorkshopDraftDecision.assignedTo
                                ?: throw NullValueNotAllowedException("invalid user id provided")
                        )

                    }
                    ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${nwaWorkshopDraftDecision.processId} ")
            }?: throw Exception("TASK NOT FOUND")

        }else if(variables["No"]==false) {
            nwaWorkshopDraftDecision.assignedTo= companyStandardRepository.getKnwSecId()
            nwaWorkshopDraftRepository.findByIdOrNull(nwaWorkshopDraftDecision.approvalID)?.let { nwaWorkShopDraft->

                with(nwaWorkShopDraft){
                    remarks=nwaWorkshopDraftDecision.comments
                   // accentTo = false
                }
                nwaWorkshopDraftRepository.save(nwaWorkShopDraft)
                standardNwaRemarksRepository.save(standardNwaRemarks)
                runtimeService.createProcessInstanceQuery()
                    .processInstanceId(nwaWorkshopDraftDecision.processId).list()
                    ?.let { l ->
                        val processInstance = l[0]
                        taskService.complete(nwaWorkshopDraftDecision.taskId, variables)

                        taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
                            ?.let { t ->
                                t.list()[0]
                                    ?.let { task ->
                                        task.assignee = "${
                                            nwaWorkshopDraftDecision.assignedTo ?: throw NullValueNotAllowedException(
                                                " invalid user id provided"
                                            )
                                        }"  //set the assignee}"
                                        //task.dueDate = standardLevyFactoryVisitReportEntity.scheduledVisitDate  //set the due date
                                        taskService.saveTask(task)
                                    }
                                    ?: KotlinLogging.logger { }
                                        .error("Task list empty for $PROCESS_DEFINITION_KEY ")


                            }
                            ?: KotlinLogging.logger { }.error("No task found for $PROCESS_DEFINITION_KEY ")
                        bpmnService.slAssignTask(
                            processInstance.processInstanceId,
                            "View and Edit Preliminary Draft",
                            nwaWorkshopDraftDecision.assignedTo
                                ?: throw NullValueNotAllowedException("invalid user id provided")
                        )

                    }
                    ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${nwaWorkshopDraftDecision.processId} ")
            }?: throw Exception("TASK NOT FOUND")
        }

        return  getUserTasks()
    }

    // Upload NWA Standard
    fun uploadNwaStandard(
        nWAStandard: NWAStandard,
        standardNwaRemarks: StandardNwaRemarks,
        standard: Standard
        ) : ProcessInstanceUS
    {
        val variable:MutableMap<String, Any> = HashMap()
        nWAStandard.title?.let{variable.put("title", it)}
        nWAStandard.scope?.let{variable.put("scope", it)}
        nWAStandard.normativeReference?.let{variable.put("normativeReference", it)}
        nWAStandard.symbolsAbbreviatedTerms?.let{variable.put("symbolsAbbreviatedTerms", it)}
        nWAStandard.clause?.let{variable.put("clause", it)}
        nWAStandard.special?.let{variable.put("special", it)}
        nWAStandard.ksNumber?.let{variable.put("ksNumber", it)}
        nWAStandard.taskId?.let{variable.put("taskId", it)}
        nWAStandard.dateSdUploaded = commonDaoServices.getTimestamp()
        variable["dateSdUploaded"] = nWAStandard.dateSdUploaded!!
        standardNwaRemarks.processId= nWAStandard.processId
        nWAStandard.assignedTo= companyStandardRepository.getHoSicId()

        standard.title=nWAStandard.title
        standard.scope= nWAStandard.scope
        standard.normativeReference= nWAStandard.normativeReference
        standard.symbolsAbbreviatedTerms= nWAStandard.symbolsAbbreviatedTerms
        standard.clause= nWAStandard.clause
        standard.special=nWAStandard.special
        standard.standardNumber= nWAStandard.ksNumber
        standard.status=1
        standard.standardType="KNWA"
        standard.dateFormed=nWAStandard.dateSdUploaded


        var userList= companyStandardRepository.getSacSecEmailList()
        val targetUrl = "https://kimsint.kebs.org/";
        userList.forEach { item->
            //val recipient="stephenmuganda@gmail.com"
            val recipient= item.getUserEmail()
            val subject = "New Company Standard"+  nWAStandard.ksNumber
            val messageBody= "Dear ${item.getFirstName()} ${item.getLastName()},A New standard has been approved and uploaded.Click on the Link below to view. ${targetUrl} "
            if (recipient != null) {
                notifications.sendEmail(recipient, subject, messageBody)
            }
        }

        //print(nWAStandard.toString())
        val nwaDetails = nwaStandardRepository.save(nWAStandard)
        standardRepository.save(standard)
        // Send email to Legal
        variable["ID"] = nwaDetails.id
        runtimeService.createProcessInstanceQuery()
            .processInstanceId(nWAStandard.processId).list()
            ?.let { l ->
                val processInstance = l[0]

                taskService.complete(nWAStandard.taskId, variable)

                taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
                    ?.let { t ->
                        t.list()[0]
                            ?.let { task ->
                                task.assignee =
                                    "${nWAStandard.assignedTo ?: throw NullValueNotAllowedException(" invalid user id provided")}"  //set the assignee}"
                                taskService.saveTask(task)
                            }
                            ?: KotlinLogging.logger { }.error("Task list empty for $PROCESS_DEFINITION_KEY ")


                    }
                    ?: KotlinLogging.logger { }.error("No task found for $PROCESS_DEFINITION_KEY ")
                bpmnService.slAssignTask(
                    processInstance.processInstanceId,
                    "Update Gazzzettement Date",
                    nWAStandard.assignedTo ?: throw NullValueNotAllowedException("invalid user id provided")
                )
                return ProcessInstanceUS(
                    nwaDetails.id,
                    processInstance.id,
                    processInstance.isEnded,nWAStandard.ksNumber?: throw NullValueNotAllowedException("Standard Number is required")
                )
            }
            ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${nWAStandard.processId} ")


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
//    fun getHoSiCTasks():List<TaskDetails>
//    {
//        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_HO_SIC).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
//        return getTaskDetails(tasks)
//    }

    // View STD Document upload
    fun findUploadedSTDFileBYId(nwaSDocumentId: Long): NWAStandardUploads {
        return nwaStandardUploadsRepository.findByNwaSDocumentId(nwaSDocumentId) ?: throw ExpectedDataNotFound("No File found with the following [ id=$nwaSDocumentId]")
    }

    // Upload NWA Gazette notice on Website
    fun uploadGazetteNotice(
        nWAGazetteNotice: NWAGazetteNotice,
        standardNwaRemarks: StandardNwaRemarks
    )
    {
        val variable:MutableMap<String, Any> = HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        nWAGazetteNotice.ksNumber?.let{variable.put("ksNumber", it)}
        //nWAGazetteNotice.dateUploaded?.let{variable.put("dateUploaded", it)}
        nWAGazetteNotice.description?.let{variable.put("description", it)}
        nWAGazetteNotice.taskId?.let{variable.put("taskId", it)}
        nWAGazetteNotice.dateUploaded = commonDaoServices.getTimestamp()
        nWAGazetteNotice.assignedTo= companyStandardRepository.getHoSicId()
        variable["dateUploaded"] = nWAGazetteNotice.dateUploaded!!
        standardNwaRemarks.processId= nWAGazetteNotice.processId
        standardNwaRemarks.status = 1.toString()
        standardNwaRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
        val fname=loggedInUser.firstName
        val sname=loggedInUser.lastName
        val usersName= "$fname  $sname"
        standardNwaRemarks.remarkBy = usersName

        print(nWAGazetteNotice.toString())
        standardNwaRemarksRepository.save(standardNwaRemarks)
        val nwaDetails = nwaGazetteNoticeRepository.save(nWAGazetteNotice)
        variable["ID"] = nwaDetails.id
        runtimeService.createProcessInstanceQuery()
            .processInstanceId(nWAGazetteNotice.processId).list()
            ?.let { l ->
                val processInstance = l[0]

                taskService.complete(nWAGazetteNotice.taskId, variable)

                taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
                    ?.let { t ->
                        t.list()[0]
                            ?.let { task ->
                                task.assignee =
                                    "${nWAGazetteNotice.assignedTo ?: throw NullValueNotAllowedException(" invalid user id provided")}"  //set the assignee}"
                                taskService.saveTask(task)
                            }
                            ?: KotlinLogging.logger { }.error("Task list empty for $PROCESS_DEFINITION_KEY ")


                    }
                    ?: KotlinLogging.logger { }.error("No task found for $PROCESS_DEFINITION_KEY ")
                bpmnService.slAssignTask(
                    processInstance.processInstanceId,
                    "Upload Gazette notice on the Website",
                    nWAGazetteNotice.assignedTo ?: throw NullValueNotAllowedException("invalid user id provided")
                )

            }
            ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${nWAGazetteNotice.processId} ")

        println("NWA Gazette Notice has been uploaded")

    }

    // Upload NWA Gazette date
    fun updateGazettementDate(nWAGazettement: NWAGazettement,
                              standardNwaRemarks: StandardNwaRemarks )
    {
        val variable:MutableMap<String, Any> = HashMap()
        nWAGazettement.ksNumber?.let{variable.put("ksNumber", it)}
        //nWAGazettement.dateOfGazettement?.let{variable.put("dateOfGazettement", it)}
        nWAGazettement.description?.let{variable.put("description", it)}
        nWAGazettement.taskId?.let{variable.put("taskId", it)}
        standardNwaRemarks.processId= nWAGazettement.processId

        nWAGazettement.dateOfGazettement = Timestamp(System.currentTimeMillis())
        variable["dateOfGazettement"] = nWAGazettement.dateOfGazettement!!
        print(nWAGazettement.toString())

        nwaGazettementRepository.save(nWAGazettement)
        taskService.complete(nWAGazettement.taskId, variable)
        println("NWA Gazettement date has been updated")

    }

    fun getUserTasks(): List<WorkShopAgreementTasks> {
        val tasks = taskService.createTaskQuery()
            .taskAssignee("${commonDaoServices.loggedInUserDetails().id ?: throw NullValueNotAllowedException(" invalid user id provided")}")
            .list()
        return getTaskDetails(tasks)
    }

    fun getKnwSecretary(): List<UserDetailHolder> {
        return userListRepository.getKnwSecretary()
    }

    fun getSpcSecretary(): List<UserDetailHolder> {
        return userListRepository.getSpcSecretary()
    }

    fun getDirector(): List<UserDetailHolder> {
        return userListRepository.getDirector()
    }
    fun getHeadOfPublishing(): List<UserDetailHolder> {
        return userListRepository.getHeadOfPublishing()
    }
    fun getSacSecretary(): List<UserDetailHolder> {
        return userListRepository.getSacSecretary()
    }
    fun getHeadOfSic(): List<UserDetailHolder> {
        return userListRepository.getHeadOfSic()
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
