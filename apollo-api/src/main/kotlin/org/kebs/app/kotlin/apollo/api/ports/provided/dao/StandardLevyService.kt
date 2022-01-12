package org.kebs.app.kotlin.apollo.api.ports.provided.dao

import mu.KotlinLogging
import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.engine.repository.Deployment
import org.flowable.task.api.Task
import org.joda.time.DateTime
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.StandardsLevyBpmn
import org.kebs.app.kotlin.apollo.common.dto.std.NWAPreliminaryDraftDecision
import org.kebs.app.kotlin.apollo.common.dto.std.TaskDetails
import org.kebs.app.kotlin.apollo.common.dto.stdLevy.*
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.registration.CompanyProfileEntity
import org.kebs.app.kotlin.apollo.store.model.std.DatKebsSdNwaUploadsEntity
import org.kebs.app.kotlin.apollo.store.model.std.NWAJustification
import org.kebs.app.kotlin.apollo.store.model.std.NWAPreliminaryDraftUploads
import org.kebs.app.kotlin.apollo.store.model.std.TechnicalCommittee
import org.kebs.app.kotlin.apollo.store.repo.*
import org.kebs.app.kotlin.apollo.store.repo.stdLevy.ManufacturePenaltyDetailsDTO
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Service
class StandardLevyService(
    private val runtimeService: RuntimeService,
    private val taskService: TaskService,
    @Qualifier("processEngine") private val processEngine: ProcessEngine,
    private val repositoryService: RepositoryService,
    private val iStagingStandardsLevyManufacturerPenaltyRepository: IStagingStandardsLevyManufacturerPenaltyRepository,
    private val iStandardLevyPaymentsRepository: IStandardLevyPaymentsRepository,
    private val companyProfileRepo: ICompanyProfileRepository,
    private val commonDaoServices: CommonDaoServices,
    private val standardLevyFactoryVisitReportRepo: IStandardLevyFactoryVisitReportRepository,
    private val slUploadsRepo: ISlVisitUploadsRepository,
    private val bpmnService: StandardsLevyBpmn
) {
    val PROCESS_DEFINITION_KEY = "sl_SiteVisitProcessFlow"
    val TASK_CANDIDATE_SL_PRINCIPAL_LEVY_OFFICER ="SL_PRINCIPAL_LEVY_OFFICER"
    val TASK_CANDIDATE_SL_Assistant_Manager ="SL_Assistant_Manager"
    val TASK_CANDIDATE_SL_Manager ="SL_Manager"
    val TASK_CANDIDATE_Manufacturer ="Manufacturer"

    //deploy bpmn file
    fun deployProcessDefinition(): Deployment =repositoryService
        .createDeployment()
        .addClasspathResource("processes/stdLevy/Sl_Site_Visit.bpmn20.xml")
        .deploy()

    //start the process by process Key
    fun startProcessByKey() : ProcessInstanceSiteResponse
    {

        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY)
        return ProcessInstanceSiteResponse( processInstance.id, processInstance.isEnded)
    }

    //List Manufactures
    fun getManufactureList(): MutableIterable<CompanyProfileEntity>
    {
        return companyProfileRepo.findAll()
    }


    fun getManufacturerPenaltyHistory(): MutableIterable<StagingStandardsLevyManufacturerPenalty>
    {
        return iStagingStandardsLevyManufacturerPenaltyRepository.findAll()
    }
    fun getManufacturerPenalty(): MutableIterable<StagingStandardsLevyManufacturerPenalty>
    {
        return iStagingStandardsLevyManufacturerPenaltyRepository.findAll()
    }

    fun getPaidLevies(): MutableIterable<StandardLevyPaymentsEntity>
    {
        return iStandardLevyPaymentsRepository.findAll()
    }

    fun assignCompany(
        companyProfileEntity : CompanyProfileEntity
    ) : ProcessInstanceSiteResponse
    {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val variables: MutableMap<String, Any> = java.util.HashMap()
        companyProfileEntity.assignedTo?.let { variables["assignedTo"] =it }
        companyProfileEntity.name?.let { variables["companyName"] =it }
        companyProfileEntity.kraPin?.let { variables["kraPin"] =it }
        companyProfileEntity.status?.let { variables["status"] =it }
        companyProfileEntity.registrationNumber?.let { variables["registrationNumber"] =it }
        companyProfileEntity.postalAddress?.let { variables["postalAddress"] =it }
        companyProfileEntity.physicalAddress?.let { variables["physicalAddress"] =it }
        companyProfileEntity.plotNumber?.let { variables["plotNumber"] =it }
        companyProfileEntity.companyEmail?.let { variables["companyEmail"] =it }
        companyProfileEntity.companyTelephone?.let { variables["companyTelephone"] =it }
        companyProfileEntity.yearlyTurnover?.let { variables["yearlyTurnover"] =it }
        companyProfileEntity.businessLines?.let { variables["businessLines"] =it }
        companyProfileEntity.businessNatures?.let { variables["businessNatures"] =it }
        companyProfileEntity.buildingName?.let { variables["buildingName"] =it }
        companyProfileEntity.directorIdNumber?.let { variables["directorIdNumber"] =it }
        companyProfileEntity.region?.let { variables["region"] =it }
        companyProfileEntity.county?.let { variables["county"] =it }
        companyProfileEntity.town?.let { variables["town"] =it }
        companyProfileEntity.manufactureStatus?.let { variables["manufactureStatus"] =it }
        companyProfileEntity.entryNumber?.let { variables["entryNumber"] =it }
        companyProfileEntity.id?.let { variables["manufacturerEntity"] =it }
        companyProfileEntity.userId?.let { variables["contactId"] =it }


        companyProfileRepo.findByIdOrNull(companyProfileEntity.id)?.let { companyProfileEntity->

            with(companyProfileEntity){
                assignedTo=companyProfileEntity.assignedTo
                assignStatus= 1

            }
            companyProfileRepo.save(companyProfileEntity)
        }?: throw Exception("COMPANY NOT FOUND")
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables)
        taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
            ?.let { t->
                t.list()[0]
                    ?.let {task ->
                        task.assignee = "${companyProfileEntity.assignedTo?:throw NullValueNotAllowedException(" invalid user id provided")}"  //set the assignee}"

                        taskService.saveTask(task)
                    }
                    ?: KotlinLogging.logger {  }.error("Task list empty for $PROCESS_DEFINITION_KEY ")


            }
            ?: KotlinLogging.logger {  }.error("No task found for $PROCESS_DEFINITION_KEY ")
        bpmnService.slAssignTask(processInstance.processInstanceId, "Schedule Site Visit", companyProfileEntity?.assignedTo?:throw NullValueNotAllowedException("invalid user id provided"))
        return ProcessInstanceSiteResponse(processInstance.id, processInstance.isEnded)

    }

    fun scheduleSiteVisit(
        standardLevyFactoryVisitReportEntity: StandardLevyFactoryVisitReportEntity
    ) : ProcessInstanceResponseSite
    {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val variables: MutableMap<String, Any> = java.util.HashMap()
        standardLevyFactoryVisitReportEntity.manufacturerEntity?.let { variables["manufacturerEntity"] =it }
        standardLevyFactoryVisitReportEntity.scheduledVisitDate?.let { variables["scheduledVisitDate"] =it  }
        standardLevyFactoryVisitReportEntity.createdBy?.let { variables["createdBy"] =it  }
        standardLevyFactoryVisitReportEntity.status?.let { variables["status"] =it  }
        standardLevyFactoryVisitReportEntity.assigneeId?.let{variables["originator"] =it}

        val visitDetails = standardLevyFactoryVisitReportRepo.save(standardLevyFactoryVisitReportEntity)
        visitDetails.id?.let { variables["visitID"] =it }

        taskService.complete(standardLevyFactoryVisitReportEntity.taskId, variables)
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables)

        taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
            ?.let { t->
                t.list()[0]
                    ?.let {task ->
                        task.assignee = "${loggedInUser.id?:throw NullValueNotAllowedException(" invalid user id provided")}"  //set the assignee}"
                        task.dueDate = standardLevyFactoryVisitReportEntity.scheduledVisitDate  //set the due date
                        taskService.saveTask(task)
                    }
                    ?: KotlinLogging.logger {  }.error("Task list empty for $PROCESS_DEFINITION_KEY ")


            }
            ?: KotlinLogging.logger {  }.error("No task found for $PROCESS_DEFINITION_KEY ")
        bpmnService.slAssignTask(processInstance.processInstanceId, "Prepare Report on Visit", loggedInUser?.id?:throw NullValueNotAllowedException("invalid user id provided"))
        return ProcessInstanceResponseSite(visitDetails.id.toString(),processInstance.id, processInstance.isEnded)


    }
    private fun getTaskDetails(tasks: List<Task>): List<TaskDetails> {
        val taskDetails: MutableList<TaskDetails> = ArrayList()
        for (task in tasks) {
            val processVariables = taskService.getVariables(task.id)
            taskDetails.add(TaskDetails(task.id, task.name, processVariables))

        }
        return taskDetails
    }

    //Return task details for PL OFFICER
    fun getUserTasks():List<TaskDetails>
    {

//        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_SL_PRINCIPAL_LEVY_OFFICER).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        val tasks = taskService.createTaskQuery().taskAssignee("${commonDaoServices.loggedInUserDetails().id?:throw NullValueNotAllowedException(" invalid user id provided")}").list()
        return getTaskDetails(tasks)
    }

    fun reportOnSiteVisit(standardLevyFactoryVisitReportEntity: StandardLevyFactoryVisitReportEntity) : ProcessInstanceResponseValueSite
    {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val variables: MutableMap<String, Any> = java.util.HashMap()
        standardLevyFactoryVisitReportEntity.visitDate?.let { variables["visitDate"] =it }
        standardLevyFactoryVisitReportEntity.purpose?.let { variables["purpose"] =it }
        standardLevyFactoryVisitReportEntity.personMet?.let { variables["personMet"] =it }
        standardLevyFactoryVisitReportEntity.actionTaken?.let { variables["actionTaken"] =it }
        standardLevyFactoryVisitReportEntity.id?.let { variables["visitID"] =it }
        standardLevyFactoryVisitReportEntity.status?.let { variables["status"] =it }
        standardLevyFactoryVisitReportEntity.assigneeId?.let { variables["assigneeId"] =it }

        standardLevyFactoryVisitReportRepo.findByIdOrNull(standardLevyFactoryVisitReportEntity.id)?.let { standardLevyFactoryVisitReportEntity->

            with(standardLevyFactoryVisitReportEntity){
                visitDate=standardLevyFactoryVisitReportEntity.visitDate
                purpose=standardLevyFactoryVisitReportEntity.purpose
                personMet=standardLevyFactoryVisitReportEntity.personMet
                actionTaken=standardLevyFactoryVisitReportEntity.actionTaken
                status=1
                assigneeId=standardLevyFactoryVisitReportEntity.assigneeId

            }
            standardLevyFactoryVisitReportRepo.save(standardLevyFactoryVisitReportEntity)
        }?: throw Exception("SCHEDULED VISIT NOT FOUND")
        taskService.complete(standardLevyFactoryVisitReportEntity.taskId, variables)
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables)

        taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
            ?.let { t->
                t.list()[0]
                    ?.let {task ->
                        task.assignee = "${standardLevyFactoryVisitReportEntity.assigneeId?:throw NullValueNotAllowedException(" invalid user id provided")}"  //set the assignee}"
                        //task.dueDate = standardLevyFactoryVisitReportEntity.scheduledVisitDate  //set the due date
                        taskService.saveTask(task)
                    }
                    ?: KotlinLogging.logger {  }.error("Task list empty for $PROCESS_DEFINITION_KEY ")


            }
            ?: KotlinLogging.logger {  }.error("No task found for $PROCESS_DEFINITION_KEY ")
        bpmnService.slAssignTask(processInstance.processInstanceId, "Notification of Report", standardLevyFactoryVisitReportEntity?.assigneeId?:throw NullValueNotAllowedException("invalid user id provided"))
        return ProcessInstanceResponseValueSite(standardLevyFactoryVisitReportEntity.id,processInstance.id, processInstance.isEnded)



    }

    // Upload PD document
    fun uploadSiteReport(
        uploads: SlVisitUploadsEntity,
        docFile: MultipartFile,
        doc: String,
        user: UsersEntity,
        DocDescription: String
    ): SlVisitUploadsEntity {

        with(uploads) {
//            filepath = docFile.path
            name = commonDaoServices.saveDocuments(docFile)
//            fileType = docFile.contentType
            fileType = docFile.contentType
            documentType = doc
            description=DocDescription
            document = docFile.bytes
            transactionDate = LocalDate.now()
            status = 1
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = LocalDateTime.now()
        }

        return slUploadsRepo.save(uploads)
    }

    //Return task details for Ast Manager
    fun getSiteReport():List<TaskDetails>
    {

        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_SL_Assistant_Manager).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    // View Report Document
    fun findUploadedReportFileBYId(visitId: Long): SlVisitUploadsEntity {
        return slUploadsRepo.findByVisitId(visitId) ?: throw ExpectedDataNotFound("No File found with the following [ id=$visitId]")
    }

    fun decisionOnSiteReport(siteVisitReportDecision: SiteVisitReportDecision) : List<TaskDetails> {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        variables["Yes"] = siteVisitReportDecision.accentTo
        variables["No"] = siteVisitReportDecision.accentTo
        variables["assigneeId"] = siteVisitReportDecision.assigneeId
        variables["status"] = 1
        siteVisitReportDecision.comments.let { variables.put("comments", it) }

        standardLevyFactoryVisitReportRepo.findByIdOrNull(siteVisitReportDecision.visitID)?.let { standardLevyFactoryVisitReportEntity->

                with(standardLevyFactoryVisitReportEntity){
                    assistantManagerRemarks=siteVisitReportDecision.comments
                    status = 1
                    assigneeId= siteVisitReportDecision.assigneeId
                }
            standardLevyFactoryVisitReportRepo.save(standardLevyFactoryVisitReportEntity)
            }?: throw Exception("TASK NOT FOUND")

        taskService.complete(siteVisitReportDecision.taskId, variables)
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables)

        taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
            ?.let { t->
                t.list()[0]
                    ?.let {task ->
                        task.assignee = "${siteVisitReportDecision.assigneeId?:throw NullValueNotAllowedException(" invalid user id provided")}"  //set the assignee}"
                        //task.dueDate = standardLevyFactoryVisitReportEntity.scheduledVisitDate  //set the due date
                        taskService.saveTask(task)
                    }
                    ?: KotlinLogging.logger {  }.error("Task list empty for $PROCESS_DEFINITION_KEY ")


            }
            ?: KotlinLogging.logger {  }.error("No task found for $PROCESS_DEFINITION_KEY ")
        bpmnService.slAssignTask(processInstance.processInstanceId, "View Report", siteVisitReportDecision?.assigneeId?:throw NullValueNotAllowedException("invalid user id provided"))

        return  getUserTasks()
    }

    //Return task details for  Manager
    fun getSiteReportLevelTwo():List<TaskDetails>
    {

        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_SL_Manager).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    fun decisionOnSiteReportLevelTwo(siteVisitReportDecision: SiteVisitReportDecision) : List<TaskDetails> {
        val variables: MutableMap<String, Any> = mutableMapOf()
        variables["Yes"] = siteVisitReportDecision.accentTo
        variables["No"] = siteVisitReportDecision.accentTo
        variables["status"] = 1
        siteVisitReportDecision.comments.let { variables.put("comments", it) }
        variables["contactId"] = siteVisitReportDecision.contactId

        standardLevyFactoryVisitReportRepo.findByIdOrNull(siteVisitReportDecision.visitID)?.let { standardLevyFactoryVisitReportEntity->

            with(standardLevyFactoryVisitReportEntity){
                cheifManagerRemarks=siteVisitReportDecision.comments
                status = 2
            }
            standardLevyFactoryVisitReportRepo.save(standardLevyFactoryVisitReportEntity)
        }?: throw Exception("TASK NOT FOUND")

        taskService.complete(siteVisitReportDecision.taskId, variables)
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables)

        taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
            ?.let { t->
                t.list()[0]
                    ?.let {task ->
                        task.assignee = "${siteVisitReportDecision.contactId?:throw NullValueNotAllowedException(" invalid user id provided")}"  //set the assignee}"
                        //task.dueDate = standardLevyFactoryVisitReportEntity.scheduledVisitDate  //set the due date
                        taskService.saveTask(task)
                    }
                    ?: KotlinLogging.logger {  }.error("Task list empty for $PROCESS_DEFINITION_KEY ")


            }
            ?: KotlinLogging.logger {  }.error("No task found for $PROCESS_DEFINITION_KEY ")
        bpmnService.slAssignTask(processInstance.processInstanceId, "Draft Feedback and share with manufacturer", siteVisitReportDecision?.contactId?:throw NullValueNotAllowedException("invalid user id provided"))

        return  getUserTasks()
    }

    fun siteVisitReportFeedback(
        standardLevyFactoryVisitReportEntity: StandardLevyFactoryVisitReportEntity
    ) : ProcessInstanceResponseValueSite
    {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val variables: MutableMap<String, Any> = java.util.HashMap()
        standardLevyFactoryVisitReportEntity.officersFeedback?.let { variables["officersFeedback"] =it }
        standardLevyFactoryVisitReportEntity.id?.let { variables["visitID"] =it }
        standardLevyFactoryVisitReportEntity.status?.let { variables["status"] =it }
        standardLevyFactoryVisitReportEntity.assigneeId?.let { variables["assigneeId"] =it }

        standardLevyFactoryVisitReportRepo.findByIdOrNull(standardLevyFactoryVisitReportEntity.id)?.let { standardLevyFactoryVisitReportEntity->

            with(standardLevyFactoryVisitReportEntity){
                officersFeedback=standardLevyFactoryVisitReportEntity.officersFeedback
                assigneeId=standardLevyFactoryVisitReportEntity.assigneeId
                status=3

            }
            standardLevyFactoryVisitReportRepo.save(standardLevyFactoryVisitReportEntity)
        }?: throw Exception("SCHEDULED VISIT NOT FOUND")

        taskService.complete(standardLevyFactoryVisitReportEntity.taskId, variables)
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables)

        taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
            ?.let { t->
                t.list()[0]
                    ?.let {task ->
                        task.assignee = "${standardLevyFactoryVisitReportEntity.assigneeId?:throw NullValueNotAllowedException(" invalid user id provided")}"  //set the assignee}"
                        //task.dueDate = standardLevyFactoryVisitReportEntity.scheduledVisitDate  //set the due date
                        taskService.saveTask(task)
                    }
                    ?: KotlinLogging.logger {  }.error("Task list empty for $PROCESS_DEFINITION_KEY ")


            }
            ?: KotlinLogging.logger {  }.error("No task found for $PROCESS_DEFINITION_KEY ")
        bpmnService.slAssignTask(processInstance.processInstanceId, "Receive Feedback", standardLevyFactoryVisitReportEntity?.assigneeId?:throw NullValueNotAllowedException("invalid user id provided"))
        return ProcessInstanceResponseValueSite(standardLevyFactoryVisitReportEntity.id,processInstance.id, processInstance.isEnded)



    }

    //Return task details for  Manufacturer
    fun getSiteFeedback():List<TaskDetails>
    {

        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_Manufacturer).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    fun getManufacturerList(): MutableList<CompanyProfileEntity> {
        return companyProfileRepo.getManufacturerList()
    }

    fun getMnCompleteTask(assignedTo: Long): MutableList<CompanyProfileEntity> {
        return companyProfileRepo.getMnCompleteTask(assignedTo)
    }

    fun getMnPendingTask(assignedTo: Long): MutableList<CompanyProfileEntity> {
        return companyProfileRepo.getMnPendingTask(assignedTo)
    }

    fun closeTask(taskId: String) {
        taskService.complete(taskId)
        taskService.deleteTask(taskId, true)

    }

    fun closeProcess(taskId: String) {
        runtimeService.deleteProcessInstance(taskId, "cleaning")
    }


}