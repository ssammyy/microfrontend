package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std

import mu.KotlinLogging
import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.engine.repository.Deployment
import org.flowable.task.api.Task
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.StandardsLevyBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.dto.std.*
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.store.model.StandardLevySiteVisitRemarks
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.registration.CompanyProfileEntity
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.std.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.sql.Timestamp
import java.util.*


@Service
class CompanyStandardService(
    private val runtimeService: RuntimeService,
    private val taskService: TaskService,
    @Qualifier("processEngine") private val processEngine: ProcessEngine,
    private val repositoryService: RepositoryService,
    private val comStandardRequestRepository: ComStandardRequestRepository,
    private val productRepository: ProductRepository,
    private val departmentRepository: DepartmentRepository,
    private val productSubCategoryRepository: ProductSubCategoryRepository,
    private val comStdActionRepository: ComStdActionRepository,
    private val comJcJustificationRepository: ComJcJustificationRepository,
    private val comStdDraftRepository: ComStdDraftRepository,
    private val companyStandardRepository: CompanyStandardRepository,
    private val technicalCommitteeRepository: TechnicalCommitteeRepository,
    private val userListRepository: UserListRepository,
    private val comJcJustificationUploadsRepository: ComJcJustificationUploadsRepository,
    private val notifications: Notifications,
    private val commonDaoServices: CommonDaoServices,
    private val comStandardDraftUploadsRepository: ComStandardDraftUploadsRepository,
    private val comStandardUploadsRepository: ComStandardUploadsRepository,
    private val comStandardJCRepository: ComStandardJCRepository,
    private val bpmnService: StandardsLevyBpmn,
    private val standardComRemarksRepository: StandardComRemarksRepository
) {
    val PROCESS_DEFINITION_KEY = "sd_CompanyStandardsProcess"

    fun deployProcessDefinition(): Deployment = repositoryService
        .createDeployment()
        .addClasspathResource("processes/std/com_Standard_Process.bpmn20.xml")
        .deploy()

    fun startProcessInstance(): ProcessInstanceResponse {
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY)
        return ProcessInstanceResponse(processInstance.id, processInstance.isEnded)
    }

    //request for company standard
    fun requestForStandard(companyStandardRequest: CompanyStandardRequest): ProcessInstanceResponse {

        val variables: MutableMap<String, Any> = HashMap()
        companyStandardRequest.companyName?.let { variables.put("companyName", it) }
        companyStandardRequest.departmentId?.let { variables.put("departmentId", it) }
        companyStandardRequest.tcId?.let { variables.put("tcId", it) }
        companyStandardRequest.productId?.let { variables.put("productId", it) }
        companyStandardRequest.productSubCategoryId?.let { variables.put("productSubCategoryId", it) }
        //companyStandardRequest.submissionDate?.let{ variables.put("submissionDate", it)}
        companyStandardRequest.companyPhone?.let { variables.put("companyPhone", it) }
        companyStandardRequest.companyEmail?.let { variables.put("companyEmail", it) }
        companyStandardRequest.submissionDate = Timestamp(System.currentTimeMillis())
        variables["submissionDate"] = companyStandardRequest.submissionDate!!
        companyStandardRequest.requestNumber = getRQNumber()
        companyStandardRequest.assignedTo= companyStandardRepository.getHodId()


        variables["requestNumber"] = companyStandardRequest.requestNumber!!

        variables["tcName"] = technicalCommitteeRepository.findNameById(companyStandardRequest.tcId?.toLong())
        companyStandardRequest.tcName = technicalCommitteeRepository.findNameById(companyStandardRequest.tcId?.toLong())

        variables["departmentName"] = departmentRepository.findNameById(companyStandardRequest.departmentId?.toLong())
        companyStandardRequest.departmentName =
            departmentRepository.findNameById(companyStandardRequest.departmentId?.toLong())

        variables["productName"] = productRepository.findNameById(companyStandardRequest.productId?.toLong())
        companyStandardRequest.productName = productRepository.findNameById(companyStandardRequest.productId?.toLong())

        variables["productSubCategoryName"] =
            productSubCategoryRepository.findNameById(companyStandardRequest.productSubCategoryId?.toLong())
        companyStandardRequest.productSubCategoryName =
            productSubCategoryRepository.findNameById(companyStandardRequest.productSubCategoryId?.toLong())

        comStandardRequestRepository.save(companyStandardRequest)


        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables)
        taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
            ?.let { t ->
                t.list()[0]
                    ?.let { task ->
                        task.assignee =
                            "${companyStandardRequest.assignedTo ?: throw NullValueNotAllowedException(" invalid user id provided")}"  //set the assignee}"

                        taskService.saveTask(task)
                    }
                    ?: KotlinLogging.logger { }.error("Task list empty for $PROCESS_DEFINITION_KEY ")


            }
            ?: KotlinLogging.logger { }.error("No task found for $PROCESS_DEFINITION_KEY ")
        bpmnService.slAssignTask(
            processInstance.processInstanceId,
            "assignProjectLeader",
            companyStandardRequest.assignedTo
                ?: throw NullValueNotAllowedException("invalid user id provided")
        )

        return ProcessInstanceResponse(processInstance.id, processInstance.isEnded)

    }

    fun getUserTasks(): List<StdUserTasks> {
        val tasks = taskService.createTaskQuery()
            .taskAssignee("${commonDaoServices.loggedInUserDetails().id ?: throw NullValueNotAllowedException(" invalid user id provided")}")
            .list()
        return getTaskDetails(tasks)
    }

    private fun getTaskDetails(tasks: List<Task>): List<StdUserTasks> {
        val taskDetails: MutableList<StdUserTasks> = ArrayList()
        for (task in tasks) {
            val processVariables = taskService.getVariables(task.id)
            taskDetails.add(StdUserTasks(task.id, task.name,task.processInstanceId, processVariables))

        }
        return taskDetails
    }

    fun assignRequest(comStdAction: ComStdAction): ProcessInstanceResponse {
        val variables: MutableMap<String, Any> = HashMap()
        comStdAction.requestNumber?.let { variables.put("requestNumber", it) }
        comStdAction.assignedTo= companyStandardRepository.getProjectLeaderId()
        comStdAction.taskId?.let { variables.put("taskId", it) }
        comStdAction.processId?.let { variables.put("processId", it) }
        comStdAction.dateAssigned = Timestamp(System.currentTimeMillis())
        variables["dateAssigned"] = comStdAction.dateAssigned!!
        variables["plAssigned"] = userListRepository.findNameById(comStdAction.assignedTo)
        comStdAction.plAssigned = userListRepository.findNameById(comStdAction.assignedTo)
        //print(comStdAction.toString())

        comStdActionRepository.save(comStdAction)
        runtimeService.createProcessInstanceQuery()
            .processInstanceId(comStdAction.processId).list()
            ?.let { l ->
                val processInstance = l[0]

                taskService.complete(comStdAction.taskId, variables)

                taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
                    ?.let { t ->
                        t.list()[0]
                            ?.let { task ->
                                task.assignee =
                                    "${comStdAction.assignedTo ?: throw NullValueNotAllowedException(" invalid user id provided")}"  //set the assignee}"
                                taskService.saveTask(task)
                            }
                            ?: KotlinLogging.logger { }.error("Task list empty for $PROCESS_DEFINITION_KEY ")


                    }
                    ?: KotlinLogging.logger { }.error("No task found for $PROCESS_DEFINITION_KEY ")
                bpmnService.slAssignTask(
                    processInstance.processInstanceId,
                    "formJointCommittee",
                    comStdAction.assignedTo ?: throw NullValueNotAllowedException("invalid user id provided")
                )
                return ProcessInstanceResponse(processInstance.id, processInstance.isEnded)

            }
            ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${comStdAction.processId} ")


    }

    fun formJointCommittee(comStandardJC: ComStandardJC, user: UsersEntity): ProcessInstanceResponse {
        val variables: MutableMap<String, Any> = HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        comStandardJC.requestNumber?.let { variables.put("requestNumber", it) }
        comStandardJC.idOfJc?.let { variables.put("idOfJc", it) }
        comStandardJC.taskId?.let { variables.put("taskId", it) }
        comStandardJC.processId?.let { variables.put("processId", it) }
        comStandardJC.dateOfFormation = Timestamp(System.currentTimeMillis())
        variables["dateOfFormation"] = comStandardJC.dateOfFormation!!
        variables["nameOfJc"] = userListRepository.findNameById(comStandardJC.idOfJc?.toLong())
        comStandardJC.nameOfJc = userListRepository.findNameById(comStandardJC.idOfJc?.toLong())
        comStandardJC.createdBy = commonDaoServices.concatenateName(user)
        comStandardJC.createdOn = commonDaoServices.getTimestamp()
        comStandardJC.assignedTo= loggedInUser.id
        print(comStandardJC.toString())

        comStandardJCRepository.save(comStandardJC)
        runtimeService.createProcessInstanceQuery()
            .processInstanceId(comStandardJC.processId).list()
            ?.let { l ->
                val processInstance = l[0]

                taskService.complete(comStandardJC.taskId, variables)

                taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
                    ?.let { t ->
                        t.list()[0]
                            ?.let { task ->
                                task.assignee =
                                    "${comStandardJC.assignedTo ?: throw NullValueNotAllowedException(" invalid user id provided")}"  //set the assignee}"
                                taskService.saveTask(task)
                            }
                            ?: KotlinLogging.logger { }.error("Task list empty for $PROCESS_DEFINITION_KEY ")


                    }
                    ?: KotlinLogging.logger { }.error("No task found for $PROCESS_DEFINITION_KEY ")
                bpmnService.slAssignTask(
                    processInstance.processInstanceId,
                    "uploadDraftCompanyStandard",
                    comStandardJC.assignedTo ?: throw NullValueNotAllowedException("invalid user id provided")
                )
                return ProcessInstanceResponse(processInstance.id, processInstance.isEnded)

            }
            ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${comStandardJC.processId} ")

    }

    //Upload Company Draft
    fun uploadDraft(comStdDraft: ComStdDraft): ProcessInstanceComDraft {
        val variables: MutableMap<String, Any> = HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        comStdDraft.title?.let { variables.put("title", it) }
        comStdDraft.scope?.let { variables.put("scope", it) }
        comStdDraft.normativeReference?.let { variables.put("normativeReference", it) }
        comStdDraft.symbolsAbbreviatedTerms?.let { variables.put("symbolsAbbreviatedTerms", it) }
        comStdDraft.clause?.let { variables.put("clause", it) }
        comStdDraft.special?.let { variables.put("special", it) }
        comStdDraft.uploadedBy= loggedInUser.id.toString()
        comStdDraft.taskId?.let { variables.put("taskId", it) }
        comStdDraft.processId?.let { variables.put("processId", it) }
        comStdDraft.requestNumber?.let { variables.put("requestNumber", it) }
        comStdDraft.uploadDate = Timestamp(System.currentTimeMillis())
        variables["uploadDate"] = comStdDraft.uploadDate!!
        variables["uploadedByName"] = userListRepository.findNameById(comStdDraft.uploadedBy?.toLong())
        comStdDraft.createdBy = userListRepository.findNameById(comStdDraft.uploadedBy?.toLong())

        comStdDraft.draftNumber = getDRNumber()
        comStdDraft.assignedTo= companyStandardRepository.getJcSecId()

        variables["draftNumber"] = comStdDraft.draftNumber!!

        //val gson = Gson()
        //KotlinLogging.logger { }.info { "DRAFT NUMBER" + gson.toJson(getDRNumber()) }
        //send email to JC

        val comDetails = comStdDraftRepository.save(comStdDraft)
        variables["ID"] = comDetails.id


        runtimeService.createProcessInstanceQuery()
            .processInstanceId(comStdDraft.processId).list()
            ?.let { l ->
                val processInstance = l[0]

                taskService.complete(comStdDraft.taskId, variables)

                taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
                    ?.let { t ->
                        t.list()[0]
                            ?.let { task ->
                                task.assignee =
                                    "${comStdDraft.assignedTo ?: throw NullValueNotAllowedException(" invalid user id provided")}"  //set the assignee}"
                                taskService.saveTask(task)
                            }
                            ?: KotlinLogging.logger { }.error("Task list empty for $PROCESS_DEFINITION_KEY ")


                    }
                    ?: KotlinLogging.logger { }.error("No task found for $PROCESS_DEFINITION_KEY ")
                bpmnService.slAssignTask(
                    processInstance.processInstanceId,
                    "viewDraft",
                    comStdDraft.assignedTo ?: throw NullValueNotAllowedException("invalid user id provided")
                )
                return ProcessInstanceComDraft(
                    comDetails.id,
                    processInstance.id,
                    processInstance.isEnded,comStdDraft.draftNumber?: throw NullValueNotAllowedException("Draft Number is required")
                )
            }
            ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${comStdDraft.processId} ")


    }

    fun uploadDrFile(
        uploads: ComStandardDraftUploads,
        docFile: MultipartFile,
        doc: String,
        user: UsersEntity,
        DocDescription: String
    ): ComStandardDraftUploads {

        with(uploads) {
//            filepath = docFile.path
            name = commonDaoServices.saveDocuments(docFile)
//            fileType = docFile.contentType
            fileType = docFile.contentType
            documentType = doc
            description = DocDescription
            document = docFile.bytes
            transactionDate = commonDaoServices.getCurrentDate()
            status = 1
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }

        return comStandardDraftUploadsRepository.save(uploads)
    }

    //View Company Draft
    fun findUploadedCDRFileBYId(comStdDraftID: Long): ComStandardDraftUploads {
        return comStandardDraftUploadsRepository.findByComDraftDocumentId(comStdDraftID)
            ?: throw ExpectedDataNotFound("No File found with the following [ id=$comStdDraftID]")
    }

    fun findUploadedFileBYId(comDraftDocumentId: Long): ComStandardDraftUploads {
        comStandardDraftUploadsRepository.findByComDraftDocumentId(comDraftDocumentId)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No File found with the following [ id=$comDraftDocumentId]")
    }

    // Decision on Company Draft
    fun decisionOnCompanyStdDraft(comDraftDecision: ComDraftDecision,standardComRemarks: StandardComRemarks): List<StdUserTasks> {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        var loggedInUser = commonDaoServices.loggedInUserDetails()
        variables["Yes"] = comDraftDecision.accentTo
        variables["No"] = comDraftDecision.accentTo
        comDraftDecision.comments.let { variables.put("comments", it) }
        comDraftDecision.taskId?.let { variables.put("taskId", it) }

        val fname=loggedInUser.firstName
        val sname=loggedInUser.lastName
        val usersName= "$fname  $sname"
        standardComRemarks.processId= comDraftDecision.processId
        standardComRemarks.remarks= comDraftDecision.comments
        standardComRemarks.approvalID= comDraftDecision.approvalID
        standardComRemarks.status = 1.toString()
        standardComRemarks.dateOfRemark = Timestamp(System.currentTimeMillis()).toString()
        standardComRemarks.remarkBy = usersName

        if(variables["Yes"]==true){
            comDraftDecision.assignedTo= companyStandardRepository.getComSecId()
            comStdDraftRepository.findByIdOrNull(comDraftDecision.approvalID)?.let { comStdDraft->
                with(comStdDraft){
                    remarks=comDraftDecision.comments
                    accentTo = true
                }
                comStdDraftRepository.save(comStdDraft)
                standardComRemarksRepository.save(standardComRemarks)
                runtimeService.createProcessInstanceQuery()
                    .processInstanceId(comDraftDecision.processId).list()
                    ?.let { l ->
                        val processInstance = l[0]
                        taskService.complete(comDraftDecision.taskId, variables)

                        taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
                            ?.let { t ->
                                t.list()[0]
                                    ?.let { task ->
                                        task.assignee = "${
                                            comDraftDecision.assignedTo ?: throw NullValueNotAllowedException(
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
                            "companyStandard",
                            comDraftDecision.assignedTo
                                ?: throw NullValueNotAllowedException("invalid user id provided")
                        )

                    }
                    ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${comDraftDecision.processId} ")
            }?: throw Exception("TASK NOT FOUND")

        }else if(variables["No"]==false) {
            comDraftDecision.assignedTo= companyStandardRepository.getProjectLeaderId()
            comStdDraftRepository.findByIdOrNull(comDraftDecision.approvalID)?.let { comStdDraft->

                with(comStdDraft){
                    remarks=comDraftDecision.comments
                    // accentTo = false
                }
                comStdDraftRepository.save(comStdDraft)
                standardComRemarksRepository.save(standardComRemarks)
                runtimeService.createProcessInstanceQuery()
                    .processInstanceId(comDraftDecision.processId).list()
                    ?.let { l ->
                        val processInstance = l[0]
                        taskService.complete(comDraftDecision.taskId, variables)

                        taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
                            ?.let { t ->
                                t.list()[0]
                                    ?.let { task ->
                                        task.assignee = "${
                                            comDraftDecision.assignedTo ?: throw NullValueNotAllowedException(
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
                            "uploadDraftCompanyStandard",
                            comDraftDecision.assignedTo
                                ?: throw NullValueNotAllowedException("invalid user id provided")
                        )

                    }
                    ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${comDraftDecision.processId} ")
            }?: throw Exception("TASK NOT FOUND")

        }
        return getUserTasks()
    }

    // Decision on Company Standard
    fun decisionOnCompanyStd(comDraftDecision: ComDraftDecision,standardComRemarks: StandardComRemarks): List<StdUserTasks> {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        variables["Yes"] = comDraftDecision.accentTo
        variables["No"] = comDraftDecision.accentTo
        comDraftDecision.comments.let { variables.put("comments", it) }
        comDraftDecision.taskId?.let { variables.put("taskId", it) }

        val fname=loggedInUser.firstName
        val sname=loggedInUser.lastName
        val usersName= "$fname  $sname"
        standardComRemarks.processId= comDraftDecision.processId
        standardComRemarks.remarks= comDraftDecision.comments
        standardComRemarks.approvalID= comDraftDecision.approvalID
        standardComRemarks.status = 1.toString()
        standardComRemarks.dateOfRemark = Timestamp(System.currentTimeMillis()).toString()
        standardComRemarks.remarkBy = usersName

        if(variables["Yes"]==true){
            comDraftDecision.assignedTo= companyStandardRepository.getHopId()
            comStdDraftRepository.findByIdOrNull(comDraftDecision.approvalID)?.let { comStdDraft->
                with(comStdDraft){
                    remarks=comDraftDecision.comments
                    accentTo = true
                }
                comStdDraftRepository.save(comStdDraft)
                standardComRemarksRepository.save(standardComRemarks)
                runtimeService.createProcessInstanceQuery()
                    .processInstanceId(comDraftDecision.processId).list()
                    ?.let { l ->
                        val processInstance = l[0]
                        taskService.complete(comDraftDecision.taskId, variables)

                        taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
                            ?.let { t ->
                                t.list()[0]
                                    ?.let { task ->
                                        task.assignee = "${
                                            comDraftDecision.assignedTo ?: throw NullValueNotAllowedException(
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
                            "publishing",
                            comDraftDecision.assignedTo
                                ?: throw NullValueNotAllowedException("invalid user id provided")
                        )

                    }
                    ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${comDraftDecision.processId} ")
            }?: throw Exception("TASK NOT FOUND")

        }else if(variables["No"]==false) {
            comDraftDecision.assignedTo= companyStandardRepository.getJcSecId()
            comStdDraftRepository.findByIdOrNull(comDraftDecision.approvalID)?.let { comStdDraft->

                with(comStdDraft){
                    remarks=comDraftDecision.comments
                    // accentTo = false
                }
                comStdDraftRepository.save(comStdDraft)
                standardComRemarksRepository.save(standardComRemarks)
                runtimeService.createProcessInstanceQuery()
                    .processInstanceId(comDraftDecision.processId).list()
                    ?.let { l ->
                        val processInstance = l[0]
                        taskService.complete(comDraftDecision.taskId, variables)

                        taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
                            ?.let { t ->
                                t.list()[0]
                                    ?.let { task ->
                                        task.assignee = "${
                                            comDraftDecision.assignedTo ?: throw NullValueNotAllowedException(
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
                            "viewDraft",
                            comDraftDecision.assignedTo
                                ?: throw NullValueNotAllowedException("invalid user id provided")
                        )

                    }
                    ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${comDraftDecision.processId} ")
            }?: throw Exception("TASK NOT FOUND")

        }
        return getUserTasks()
    }

    // Upload Company Standard
    fun uploadComStandard(companyStandard: CompanyStandard): ProcessInstanceComStandard {
        val variable: MutableMap<String, Any> = HashMap()
        companyStandard.title?.let { variable.put("title", it) }
        companyStandard.scope?.let { variable.put("scope", it) }
        companyStandard.normativeReference?.let { variable.put("normativeReference", it) }
        companyStandard.symbolsAbbreviatedTerms?.let { variable.put("symbolsAbbreviatedTerms", it) }
        companyStandard.clause?.let { variable.put("clause", it) }
        companyStandard.special?.let { variable.put("special", it) }
        companyStandard.taskId?.let { variable.put("taskId", it) }
        companyStandard.processId?.let { variable.put("processId", it) }

        companyStandard.comStdNumber = getCSNumber()

        var userList= companyStandardRepository.getSacSecEmailList()
        val targetUrl = "https://kimsint.kebs.org/";
        userList.forEach { item->
            val recipient="stephenmuganda@gmail.com"
            //val recipient= item.getUserEmail()
            val subject = "New Company Standard"+  companyStandard.comStdNumber
            val messageBody= "Dear ${item.getFirstName()} ${item.getLastName()},A New standard has been approved and uploaded.Click on the Link below to view. ${targetUrl} "
            if (recipient != null) {
                notifications.sendEmail(recipient, subject, messageBody)
            }
        }

        variable["comStdNumber"] = companyStandard.comStdNumber!!

        val comDetails = companyStandardRepository.save(companyStandard)
        variable["ID"] = comDetails.id
        taskService.complete(companyStandard.taskId, variable)
        println("Company Standard Uploaded")
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variable)
        return ProcessInstanceComStandard(
            comDetails.id,
            processInstance.id,
            processInstance.isEnded,
            companyStandard.comStdNumber ?: throw NullValueNotAllowedException("Standard Number is required")
        )

    }

    fun uploadSTDFile(
        uploads: ComStandardUploads,
        docFile: MultipartFile,
        doc: String,
        user: UsersEntity,
        DocDescription: String
    ): ComStandardUploads {

        with(uploads) {
//            filepath = docFile.path
            name = commonDaoServices.saveDocuments(docFile)
//            fileType = docFile.contentType
            fileType = docFile.contentType
            documentType = doc
            description = DocDescription
            document = docFile.bytes
            transactionDate = commonDaoServices.getCurrentDate()
            status = 1
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }

        return comStandardUploadsRepository.save(uploads)
    }

    // View STD Document upload
    fun findUploadedSTDFileBYId(comStdDocumentId: Long): ComStandardUploads {
        return comStandardUploadsRepository.findByComStdDocumentId(comStdDocumentId)
            ?: throw ExpectedDataNotFound("No File found with the following [ id=$comStdDocumentId]")
    }



    fun getRQNumber(): String {
        val allRequests = comStandardRequestRepository.findAllByOrderByIdDesc()

        var lastId: String? = "0"
        var finalValue = 1
        var startId = "RQ"


        for (item in allRequests) {
            println(item)
            lastId = item.requestNumber
            break
        }

        if (lastId != "0") {
            val strs = lastId?.split(":")?.toTypedArray()

            val firstPortion = strs?.get(0)

            val lastPortArray = firstPortion?.split("/")?.toTypedArray()

            val intToIncrement = lastPortArray?.get(1)

            finalValue = (intToIncrement?.toInt()!!)
            finalValue += 1
        }


        val year = Calendar.getInstance()[Calendar.YEAR]

        return "$startId/$finalValue:$year";
    }

    fun getDRNumber(): String {
        var allRequests = comStdDraftRepository.getMaxDraftId()

        var lastId: String? = "0"
//        var finalValue = 1
        var startId = "DRAFT"

        //allRequests = allRequests+1

        val c = allRequests
        val d = c.toInt()
        val x = 1
        val z = x  + d

        val finalValue = z.toString()

//        println("Sum of x+y = $finalValue")

        val year = Calendar.getInstance()[Calendar.YEAR]
        val month = Calendar.getInstance()[Calendar.MONTH]

        return "$startId/$finalValue/$month:$year"


    }

    fun getCSNumber(): String {
        var allRequests = companyStandardRepository.getMaxComStdId()

        var lastId: String? = "0"
//        var finalValue = 1
        var startId = "CS"

        //allRequests = allRequests+1

        val c = allRequests
        val d = c.toInt()
        val x = 1
        val z = x  + d

        val finalValue = z.toString()

//        println("Sum of x+y = $finalValue")

        val year = Calendar.getInstance()[Calendar.YEAR]
        val month = Calendar.getInstance()[Calendar.MONTH]

        return "$startId/$finalValue/$month:$year"

    }

    fun getUserList(): MutableList<UsersEntity> {
        return userListRepository.findAll()
    }

    fun getComStandardRemarks(approvalID: Long): List<StandardComRemarks> {
        standardComRemarksRepository.findAllByApprovalIDOrderByIdDesc(approvalID)?.let {
            return it
        }
            ?: throw ExpectedDataNotFound("No Data Found")
    }

    fun getAllStandards(): MutableList<CompanyStandard> {
        return companyStandardRepository.findAll()
    }

}