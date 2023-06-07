package org.kebs.app.kotlin.apollo.api.ports.provided.bpmn

import mu.KotlinLogging
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.task.api.Task
import org.joda.time.DateTime
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.di.BpmnTaskDetails
import org.kebs.app.kotlin.apollo.api.ports.provided.scheduler.SchedulerImpl
import org.kebs.app.kotlin.apollo.store.model.PetroleumInstallationInspectionEntity
import org.kebs.app.kotlin.apollo.store.model.qa.PermitApplicationsEntity
import org.kebs.app.kotlin.apollo.store.repo.IManufacturerRepository
import org.kebs.app.kotlin.apollo.store.repo.IPermitRepository
import org.kebs.app.kotlin.apollo.store.repo.IPetroleumInstallationInspectionRepository
import org.kebs.app.kotlin.apollo.store.repo.IUserRepository
import org.kebs.app.kotlin.apollo.store.repo.qa.IPermitApplicationsRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.Instant
import java.util.*


@Service
class QualityAssuranceBpmn(

    private val taskService: TaskService,
        //private val runtimeService: RuntimeService,
    private val permitRepo: IPermitRepository,
    private val permitsRepo: IPermitApplicationsRepository,
    private val userRepo: IUserRepository,
        //private val permitRemarksRepository: IPermitApplicationRemarksRepository,
    private val manufacturerRepo: IManufacturerRepository,
    private val petroleumIIRepo: IPetroleumInstallationInspectionRepository,
    private val bpmnCommonFunctions: BpmnCommonFunctions,
    private val schedulerImpl: SchedulerImpl,
    private val runtimeService: RuntimeService
) {

    @Value("\${bpmn.qa.app.review.process.definition.key}")
    lateinit var qaAppReviewProcessDefinitionKey: String

    @Value("\${bpmn.qa.app.review.business.key}")
    lateinit var qaAppReviewBusinessKey: String

    @Value("\${bpmn.qa.sf.mark.inspection.process.definition.key}")
    lateinit var qaSfMarkInspectionProcessDefinitionKey: String

    @Value("\${bpmn.qa.sf.mark.inspection.business.key}")
    lateinit var qaSfMarkInspectionBusinessKey: String

    @Value("\${bpmn.qa.dm.application.review.process.definition.key}")
    lateinit var qaDmApplicationReviewProcessDefinitionKey: String

    @Value("\${bpmn.qa.dm.application.review.business.key}")
    lateinit var qaDmApplicationReviewBusinessKey: String

    @Value("\${bpmn.qa.sf.application.payment.process.definition.key}")
    lateinit var qaSfApplicationPaymentProcessDefinitionKey: String

    @Value("\${bpmn.qa.sf.application.payment.business.key}")
    lateinit var qaSfApplicationPaymentBusinessKey: String

    @Value("\${bpmn.qa.sf.permit.award.process.definition.key}")
    lateinit var qaSfPermitAwardProcessDefinitionKey: String

    @Value("\${bpmn.qa.sf.permit.award.business.key}")
    lateinit var qaSfPermitAwardBusinessKey: String

    @Value("\${bpmn.qa.ii.schedule.process.definition.key}")
    lateinit var qaIiScheduleProcessDefinitionKey: String

    @Value("\${bpmn.qa.ii.schedule.business.key}")
    lateinit var qaIiScheduleBusinessKey: String

    @Value("\${bpmn.qa.ii.reporting.process.definition.key}")
    lateinit var qaIiReportingProcessDefinitionKey: String

    @Value("\${bpmn.qa.ii.reporting.business.key}")
    lateinit var qaIiReportingBusinessKey: String

    @Value("\${bpmn.qa.dm.assessment.process.definition.key}")
    lateinit var qaDmAssessmentProcessDefinitionKey: String

    @Value("\${bpmn.qa.dm.assessment.business.key}")
    lateinit var qaDmAssessmentBusinessKey: String

//    @Value("\${bpmn.qa.dm.app.payment.process.definition.key}")
//    lateinit var qaDmAppPaymentProcessDefinitionKey: String

    @Value("\${bpmn.qa.dm.application.payment.process.definition.key}")
    lateinit var qaApplicationAndPaymentProcessDefinitionKey: String

    @Value("\${bpmn.qa.dm.app.payment.business.key}")
    lateinit var qaDmAppPaymentBusinessKey: String

    @Value("\${bpmn.notification.permit.expiry.id}")
    lateinit var permitExpiryNotificationId: String

    @Value("\${bpmn.candidate.group.pcm}")
    lateinit var pcmCandidateGroup: String

    @Value("\${bpmn.task.default.duration}")
    lateinit var defaultDuration: Integer


    val successMessage: String = "success"
    val processStarted: Int = 1
    val processCompleted: Int = 2

    /*
    ***********************************************************************************
    * GENERAL
    ***********************************************************************************
     */

    //Get the processInstanceId from the permit and process
    fun getPIdByObjectAndProcess(objectId: Long, process: String): HashMap<String, Any>? {
        val variables: HashMap<String, Any> = HashMap()
        try {
            var processInstanceId = ""
            if (process == qaIiScheduleProcessDefinitionKey || process== qaIiReportingProcessDefinitionKey) {
                petroleumIIRepo.findByIdOrNull(objectId)?.let { piie ->//Check that the entity is valid
                    KotlinLogging.logger { }.trace("ObjectId : $objectId : Valid object found")
                    if (process == qaIiScheduleProcessDefinitionKey) {
                        processInstanceId = piie.iisProcessInstanceId.toString()
                    }

                    if (process == qaIiReportingProcessDefinitionKey) {
                        processInstanceId = piie.iirProcessInstanceId.toString()
                    }
                    variables["processInstanceId"] = processInstanceId
                    variables["piie"] = piie
                    return variables
                }
                KotlinLogging.logger { }.info("ObjectId : $objectId : No object found")
            } else {
                permitsRepo.findByIdOrNull(objectId)?.let { permit ->//Check that the permit is valid
                    KotlinLogging.logger { }.trace("PermitId : $objectId : Valid permit found")
                    if (process == qaAppReviewProcessDefinitionKey) {
                        processInstanceId = permit.appReviewProcessInstanceId.toString()
                    }
                    if (process == qaSfMarkInspectionProcessDefinitionKey) {
                        processInstanceId = permit.sfMarkInspectionProcessInstanceId.toString()
                    }
                    if (process == qaDmApplicationReviewProcessDefinitionKey) {
                        processInstanceId = permit.dmAppReviewProcessInstanceId.toString()
                    }
                    if (process == qaSfApplicationPaymentProcessDefinitionKey) {
                        processInstanceId = permit.sfAppPaymentProcessInstanceId.toString()
                    }
                    if (process == qaSfPermitAwardProcessDefinitionKey) {
                        processInstanceId = permit.sfPermitAwardProcessInstanceId.toString()
                    }
                    if (process == qaDmAssessmentProcessDefinitionKey) {
                        processInstanceId = permit.dmAssessmentProcessInstanceId.toString()
                    }
//                    if (process == qaDmAppPaymentProcessDefinitionKey) {
//                        processInstanceId = permit.dmAppPaymentProcessInstanceId.toString()
//                    }
                    if (process == qaApplicationAndPaymentProcessDefinitionKey) {
                        processInstanceId = permit.dmAppPaymentProcessInstanceId.toString()
                    }
                    variables["processInstanceId"] = processInstanceId
                    variables["permit"] = permit
                    return variables
                }
                KotlinLogging.logger { }.info("PermitId : $objectId : No permit found")
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }

    //Get all user tasks related to the app review process
    fun fetchTaskByPermitId(permitId: Long, process: String): List<TaskDetails>? {
        try {
            getPIdByObjectAndProcess(permitId, process)?.let {
                val processInstanceId = it["processInstanceId"].toString()
                bpmnCommonFunctions.getTasks("processInstanceId", processInstanceId)?.let { tasks ->
                    return generateTaskDetails(tasks)
                } ?: run { KotlinLogging.logger { }.info("PermitId : $permitId : No task found");return null }
            }
            KotlinLogging.logger { }.info("PermitId : $permitId : No permit found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }

    fun fetchTaskByInstallationId(objectId: Long, process: String): List<TaskDetails>? {
        try {
            getPIdByObjectAndProcess(objectId, process)?.let {
                val processInstanceId = it["processInstanceId"].toString()
                bpmnCommonFunctions.getTasks("processInstanceId", processInstanceId)?.let { tasks ->
                    return generateTaskDetails(tasks)
                } ?: run { KotlinLogging.logger { }.info("objectId : $objectId : No task found");return null }
            }
            KotlinLogging.logger { }.info("objectId : $objectId : No object found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }

    fun fetchAllTasksByAssignee(assigneeId: Long): List<TaskDetails>? {
        try {
            taskService.createTaskQuery().taskAssignee(assigneeId.toString()).processDefinitionKeyLikeIgnoreCase("%qa%").includeProcessVariables().list()?.let { tasks ->
                return generateTaskDetails(tasks)
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }

    //Update task variable by permit Id and task Key
    fun updateTaskVariableByObjectIdAndKey(objectId: Long, taskDefinitionKey: String, process: String, parameterName: String, parameterValue: String): Boolean {
        try {
            getPIdByObjectAndProcess(objectId, process)?.let {
                bpmnCommonFunctions.getTaskByTaskDefinitionKey(it["processInstanceId"].toString(), taskDefinitionKey)?.let { task ->
                    bpmnCommonFunctions.updateVariable(task.id, parameterName, parameterValue).let {
                        return true
                    }
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return false
    }

    fun qaCompleteTask(objectId: Long, taskDefinitionKey: String, process: String): HashMap<String, Any>? {
        KotlinLogging.logger { }.info("objectId : $objectId :  Completing task $taskDefinitionKey")
        try {
            getPIdByObjectAndProcess(objectId, process)?.let {
                bpmnCommonFunctions.getTaskByTaskDefinitionKey(it["processInstanceId"].toString(), taskDefinitionKey)?.let { task ->
                    bpmnCommonFunctions.completeTask(task.id)
                    it["assigneeId"] = task.assignee
                    return it
                }
            }
            KotlinLogging.logger { }.info("objectId : $objectId :  No objectId found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }

//    fun qaAssignTask(permit: PermitApplicationsEntity?, processInstanceId: String, taskDefinitionKey: String?, assigneeId: Long, useManufacturer: Boolean): Boolean {
//        KotlinLogging.logger { }.info("Assign next task begin")
//        try {
//            var localAssigneeId: String = assigneeId.toString()
//            var task: Task? = null
//
//            taskDefinitionKey?.let {
//                task = bpmnCommonFunctions.getTaskByTaskDefinitionKey(processInstanceId, taskDefinitionKey)
//            } ?: run {
//                task = bpmnCommonFunctions.getTasks("processInstanceId", processInstanceId)?.get(0)
//            }
//
//            permit?.let {
//                if (useManufacturer) {
//                    permit.userId?.let { userId->
//                        localAssigneeId = userId.toString()
//                    }
//
//                }
//            }
//
//            task?.let { task ->
//                if (!useManufacturer) {
//                    userRepo.findByIdOrNull(localAssigneeId.toLong())?.let { usersEntity ->
//                        bpmnCommonFunctions.updateVariable(task.id, "email", usersEntity.email.toString())
//                    }
//                }
//                //Refetch the task because we have updated the email variable
//                bpmnCommonFunctions.getTaskById(task.id)?.let { updatedTask ->
//                    updatedTask.assignee = localAssigneeId
//                    updatedTask.dueDate = DateTime(Date()).plusDays(defaultDuration.toInt()).toDate()
//                    taskService.saveTask(updatedTask)
//                    KotlinLogging.logger { }.info("Task ${updatedTask.name} assigned to $localAssigneeId")
//                    return true
//                }
//            }
//            KotlinLogging.logger { }.info("No task found"); return false
//
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.error(e.message, e)
//        }
//        return false
//    }

    fun qaAssignTask(permit: PermitApplicationsEntity?, processInstanceId: String, taskDefinitionKey: String?, assigneeId: Long, candidateGroup: String? = null): Boolean {
        KotlinLogging.logger { }.info("Assign next task begin")
        try {
            var localAssigneeId: String = assigneeId.toString()
            var task: Task? = null

            taskDefinitionKey?.let {
                task = bpmnCommonFunctions.getTaskByTaskDefinitionKey(processInstanceId, taskDefinitionKey)
            } ?: run {
                task = bpmnCommonFunctions.getTasks("processInstanceId", processInstanceId)?.get(0)
            }

            task?.let { task ->
                userRepo.findByIdOrNull(localAssigneeId.toLong())?.let { usersEntity ->
                    bpmnCommonFunctions.updateVariable(task.id, "email", usersEntity.email.toString())
                }
                //Refetch the task because we have updated the email variable
                bpmnCommonFunctions.getTaskById(task.id)?.let { updatedTask ->
                    updatedTask.assignee = localAssigneeId
                    updatedTask.dueDate = DateTime(Date()).plusDays(defaultDuration.toInt()).toDate()
                    taskService.saveTask(updatedTask)
                    candidateGroup?.let {
                        taskService.addCandidateGroup(updatedTask.id, it)
                    }
                    KotlinLogging.logger { }.info("Task ${updatedTask.name} assigned to $localAssigneeId")
                    return true
                }
            }
            KotlinLogging.logger { }.info("No task found"); return false

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return false
    }

    fun checkStartProcessInputs(objectId: Long, assigneeId: Long?, processKey: String): HashMap<String, Any>? {
        val variables: HashMap<String, Any> = HashMap()
        KotlinLogging.logger { }.info("objectId : $objectId : Checking start process values")
        try {
            if (processKey == qaIiScheduleProcessDefinitionKey || processKey == qaIiReportingProcessDefinitionKey ) {
                petroleumIIRepo.findByIdOrNull(objectId)?.let { piie->
                    variables["petroleumInstallationInspection"] = piie
                    if (processKey == qaIiScheduleProcessDefinitionKey){
                        piie.iisStatus?.let{ status->
                            if (status!=0){
                                KotlinLogging.logger { }.info("objectId : $objectId : Installation already has a II schedule task assigned"); return null
                            }
                        }
                    }

                    if (processKey == qaIiReportingProcessDefinitionKey){
                        piie.iirStatus?.let{ status->
                            if (status!=0){
                                KotlinLogging.logger { }.info("objectId : $objectId : Installation already has a II reporting task assigned"); return null
                            }
                        }
                    }
                }  ?: run { KotlinLogging.logger { }.info("objectId : $objectId : No petroleum inspection object found for id $objectId"); return null }
            } else {
                //Check that the permit is valid
                permitsRepo.findByIdOrNull(objectId)?.let { //Check that the permit is valid
                    pm ->
                    variables["permit"] = pm

                    if (processKey == qaAppReviewProcessDefinitionKey ){
                        pm.appReviewStatus?.let{ status->
                            if (status!=0){
                                KotlinLogging.logger { }.info("permitId : $objectId : Permit already has a app review task assigned"); return null
                            }
                        }
                    }

                    if (processKey == qaSfMarkInspectionProcessDefinitionKey ){
                        pm.sfMarkInspectionStatus?.let{ status->
                            if (status!=0){
                                //KotlinLogging.logger { }.info("permitId : $objectId : Permit already has an inspection task assigned"); return null
                            }
                        }
                    }

                    if (processKey == qaDmApplicationReviewProcessDefinitionKey ){
                        pm.dmAppReviewStatus?.let{ status->
                            if (status!=0){
                                KotlinLogging.logger { }.info("permitId : $objectId : Permit already has a DM application review task assigned"); return null
                            }
                        }
                    }

                    if (processKey == qaSfApplicationPaymentProcessDefinitionKey){
                        pm.sfAppPaymentStatus?.let{ status->
                            if (status!=0){
                                //KotlinLogging.logger { }.info("permitId : $objectId : Permit already has a SF application payment task assigned"); return null
                            }
                        }
                    }

                    if (processKey == qaSfPermitAwardProcessDefinitionKey){
                        pm.sfPermitAwardStatus?.let{ status->
                            if (status!=0){
                                KotlinLogging.logger { }.info("permitId : $objectId : Permit already has a SF permit award task assigned"); return null
                            }
                        }
                    }

                    if (processKey == qaDmAssessmentProcessDefinitionKey){
                        pm.dmAssessmentStatus?.let{ status->
                            if (status!=0){
                                KotlinLogging.logger { }.info("permitId : $objectId : Permit already has a DM assessment task assigned"); return null
                            }
                        }
                    }
//                    if (processKey == qaDmAppPaymentProcessDefinitionKey){
//                        pm.dmAppPaymentStatus?.let{ status->
//                            if (status!=0){
//                                KotlinLogging.logger { }.info("permitId : $objectId : Permit already has a DM app payment task assigned"); return null
//                            }
//                        }
//                    }
                    if (processKey == qaApplicationAndPaymentProcessDefinitionKey){
                        pm.dmAppPaymentStatus?.let{ status->
                            if (status!=0){
                                KotlinLogging.logger { }.info("permitId : $objectId : Permit already has a DM app payment task assigned"); return null
                            }
                        }
                    }
                    //Check that the manufacturer is valid
                    KotlinLogging.logger { }.info("--------------- ${pm.userId}")
                    pm.userId?.let { manufacturerId ->
                        userRepo.findByIdOrNull(manufacturerId)?.let { usersEntity ->
                            variables["manufacturerEmail"] = usersEntity.email.toString()
                            variables["assigneeEmail"] = usersEntity.email.toString()
                        }
                    } ?: run { KotlinLogging.logger { }.info("objectId : $objectId : Permit has no manufacturer Id"); return null; }

                    //Check that the user_id field is valid
                    pm.userId?.let { usersEntity ->
                        //do nothing
                    } ?: run { KotlinLogging.logger { }.info("objectId : $objectId : Permit has no manufacturer user Id"); return null; }

                } ?: run { KotlinLogging.logger { }.info("objectId : $objectId : No permit found for id $objectId"); return null }
            }

            //Check that the assignee is valid
            assigneeId?.let {
                userRepo.findByIdOrNull(assigneeId)?.let { usersEntity ->
                    variables["assigneeEmail"] = usersEntity.email.toString()
                } ?: run { KotlinLogging.logger { }.info("objectId : $objectId : No user found for id $assigneeId") }
            }

            return variables

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
            return null
        }

    }

    //Get task details
    fun fetchTaskDetails(parameterType: String, parameterValue: String): List<TaskDetails>? {
        try {
            var processInstanceId: String = parameterValue
            if (parameterType == "permitId") { //get the process instance Id
                try {
                    processInstanceId = permitsRepo.findById(parameterValue.toLong()).get().appReviewProcessInstanceId.toString()
                } catch (e: Exception) {
                    KotlinLogging.logger { }.info(e.message); return null
                }
            }
            bpmnCommonFunctions.getTasks("processInstanceId", processInstanceId)?.let { tasks ->
                return generateTaskDetails(tasks)
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }

    //Get all user tasks related to the app review process
    fun fetchQaAppReviewTasks(assigneeId: Long): List<TaskDetails>? {
        KotlinLogging.logger { }.info("Fetching tasks assigned to $assigneeId")
        try {
            bpmnCommonFunctions.getTasksByAssigneeAndBusinessKey(assigneeId, qaAppReviewBusinessKey)?.let { tasks ->
                KotlinLogging.logger { }.info("Found " + tasks.count() + " tasks for $assigneeId")
                return generateTaskDetails(tasks)
            }
            KotlinLogging.logger { }.info("No tasks found for assignee $assigneeId")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }


    fun generateTaskDetails(tasks: List<Task>): List<TaskDetails> {
        val taskList: MutableList<TaskDetails> = mutableListOf()
        for (task in tasks) {
            try{
                taskList.add(TaskDetails(task.processVariables["permitId"].toString().toLong(), 0,task,task.processVariables["permitRefNo"].toString()))
            } catch (e:Exception){
                taskList.add(TaskDetails(0,task.processVariables["objectId"].toString().toLong(), task,""))
            }
        }
        return taskList
    }


    /*
    ***********************************************************************************
    * APPLICATION REVIEW
    ***********************************************************************************
     */
    //Start the app review process
    //Return the pid and taskid
    fun startQAAppReviewProcess(permitId: Long, assigneeId: Long): HashMap<String, String>? {
        var variables: HashMap<String, Any> = HashMap()
        KotlinLogging.logger { }.info("permitId : $permitId : Starting QA app review process")
        try {
            checkStartProcessInputs(permitId, assigneeId, qaAppReviewProcessDefinitionKey)?.let { checkVariables ->
                val permit: PermitApplicationsEntity = checkVariables["permit"] as PermitApplicationsEntity
                variables["permitId"] = permit.id.toString()
                variables["permitRefNo"] = permit.permitRefNumber.toString()
                variables["applicationComplete"] = 0
                variables["email"] = checkVariables["assigneeEmail"].toString()
                variables["manufacturerEmail"] = checkVariables["manufacturerEmail"].toString()
                bpmnCommonFunctions.startBpmnProcess(qaAppReviewProcessDefinitionKey, qaAppReviewBusinessKey, variables, assigneeId)?.let {
                    permit.appReviewProcessInstanceId = it["processInstanceId"]
                    permit.appReviewStartedOn = Timestamp.from(Instant.now())
                    permit.appReviewStatus = processStarted
                    permitsRepo.save(permit)
                    KotlinLogging.logger { }.info("permitId : $permitId : Successfully started QA app review process for permit")
                    return it
                } ?: run {
                    KotlinLogging.logger { }.info("$permitId : Unable to start QA app review process")
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }

    fun qaAppReviewCheckIfApplicationComplete(permitId: Long, applicationComplete: Int) : Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Check if application complete begin")
        var currAssigneeId: Long = 0
        fetchTaskByPermitId(permitId, qaAppReviewProcessDefinitionKey)?.let { taskDetails ->
            currAssigneeId = taskDetails[0].task.assignee.toLong()
        }
        updateTaskVariableByObjectIdAndKey(permitId, "qaHOFCheckIfComplete", qaAppReviewProcessDefinitionKey, "applicationComplete", applicationComplete.toString())
        qaCompleteTask(permitId, "qaHOFCheckIfComplete", qaAppReviewProcessDefinitionKey)?.let {
            return if (applicationComplete==1) {   //application is complete
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaAllocateFileToQAO", currAssigneeId)
            } else {
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaCorrectApplication", currAssigneeId)
            }
        }
        return false
    }

    fun qaAppReviewAllocateToQAO(permitId: Long, qaoAssigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Allocate to QAO begin")
        qaCompleteTask(permitId, "qaAllocateFileToQAO", qaAppReviewProcessDefinitionKey)?.let {
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaPlanFactoryVisit", qaoAssigneeId)
        }
        return false
    }

    fun qaAppReviewPlanFactoryVisit(permitId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Plan factory visit")
        qaCompleteTask(permitId, "qaPlanFactoryVisit", qaAppReviewProcessDefinitionKey)?.let {
            return true
        }
        return false
    }

    fun qaAppReviewCorrectApplication(permitId: Long,hofAssigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Correct application begin")
        var currAssigneeId: Long = 0
        fetchTaskByPermitId(permitId, qaAppReviewProcessDefinitionKey)?.let { taskDetails ->
            currAssigneeId = taskDetails[0].task.assignee.toLong()
        }
        qaCompleteTask(permitId, "qaCorrectApplication", qaAppReviewProcessDefinitionKey)?.let {
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaHOFCheckIfComplete", currAssigneeId)
        }
        return false
        //return qaCompleteAndAssign(qaAppReviewProcessDefinitionKey,permitId, 0, true, false)
    }

    fun qaAppReviewResubmitApplication(permitId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Resubmit application begin")
        qaCompleteTask(permitId, "qaResubmitApplication", qaAppReviewProcessDefinitionKey)?.let {
            return true
        }
        return false
    }

    /*
    ***********************************************************************************
    * SF MARK INSPECTION
    ***********************************************************************************
     */
    //Start the SF Mark Inspection process
    fun startQASFMarkInspectionProcess(permitId: Long, assigneeId: Long): HashMap<String, String>? {
        var variables: HashMap<String, Any> = HashMap()
        KotlinLogging.logger { }.info("permitId : $permitId : Starting QA SF Mark inspection process")
        try {
            //Remember to start by setting email and assignee to manufacturer
            checkStartProcessInputs(permitId, assigneeId, qaSfMarkInspectionProcessDefinitionKey)?.let { checkVariables ->
                val permit: PermitApplicationsEntity = checkVariables["permit"] as PermitApplicationsEntity
                variables["permitId"] = permit.id.toString()
                variables["permitRefNo"] = permit.permitRefNumber.toString()
                variables["applicationComplete"] = 0
                variables["email"] = checkVariables["assigneeEmail"].toString()
                variables["manufacturerEmail"] = checkVariables["manufacturerEmail"].toString()
                variables["supervisionSchemeAccepted"] = 0
                variables["labReportStatus"] = 0
                variables["complianceStatus"] = 0
                variables["permitRecommendationApproval"] = 0
                variables["permitSurveillance"] = 0
                variables["factoryInspectionFormsComplete"] = 1
                variables["emailMessage"] = ""

                bpmnCommonFunctions.startBpmnProcess(qaSfMarkInspectionProcessDefinitionKey, qaSfMarkInspectionBusinessKey, variables, assigneeId)?.let {
                    permit.sfMarkInspectionProcessInstanceId = it["processInstanceId"]
                    //permit.sfMarkInspectionTaskId = it["taskId"]
                    permit.sfMarkInspectionStartedOn = Timestamp.from(Instant.now())
                    permit.sfMarkInspectionStatus = 1
                    permitsRepo.save(permit)
                    KotlinLogging.logger { }.info("permitId : $permitId : Successfully started QA Sf Mark inspection process for permit")
                    return it
                } ?: run {
                    KotlinLogging.logger { }.info("$permitId : Unable to start QA SF Mark inspection process")
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }

    fun qaSfMIFillFactoryInspectionForms(permitId: Long, qaoAssigneeId: Long, labAssigneeId: Long): Boolean {
        var result = false
        KotlinLogging.logger { }.info("PermitId : $permitId :  Fill factory inspection forms")
        qaCompleteTask(permitId, "qaSfmiFillFactoryInspectionForms", qaSfMarkInspectionProcessDefinitionKey)?.let {
            //We then have 2 assignments to do - Generate inspection report and lab
            qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaSfmiGenerateInspectionReport", qaoAssigneeId).let { assignStatus ->
                result = assignStatus
            }
            qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaSfmiReceiveSSF", labAssigneeId).let { assignStatus ->
                result = assignStatus
            }
        }
        return result
    }

    fun qaSfMIGenerateInspectionReport(permitId: Long, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Generate Inspection Report")
        qaCompleteTask(permitId, "qaSfmiGenerateInspectionReport", qaSfMarkInspectionProcessDefinitionKey)?.let {
            return true
        }
        return false
    }

    fun qaSfMIGenSupervisionSchemeComplete(permitId: Long, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Generate supervision scheme")
        qaCompleteTask(permitId, "qaSfmiGenSupervisionScheme", qaSfMarkInspectionProcessDefinitionKey)?.let {
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaSfmiAcceptSupervisionScheme", assigneeId)
        }
        return false
    }

    fun qaSfMIAcceptSupervisionScheme(permitId: Long, assigneeId: Long, supervisionSchemeAccepted: Boolean): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Accept supervision scheme")
        updateTaskVariableByObjectIdAndKey(permitId, "qaSfmiAcceptSupervisionScheme", qaSfMarkInspectionProcessDefinitionKey, "supervisionSchemeAccepted", bpmnCommonFunctions.booleanToInt(supervisionSchemeAccepted).toString())
        qaCompleteTask(permitId, "qaSfmiAcceptSupervisionScheme", qaSfMarkInspectionProcessDefinitionKey)?.let {
            if (!supervisionSchemeAccepted){
                //return to sender
                return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaSfmiGenSupervisionScheme", assigneeId)
            } else {
                return true //process ends
            }
        }
        return false
    }


    fun qaSfMIReceiveSSFComplete(permitId: Long, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Receive the SSF")
        qaCompleteTask(permitId, "qaSfmiReceiveSSF", qaSfMarkInspectionProcessDefinitionKey)?.let {
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaSfmiSubmitSamples", assigneeId)
        }
        return false
    }

    fun qaSfMISubmitSamplesComplete(permitId: Long, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Submit the samples")
        qaCompleteTask(permitId, "qaSfmiSubmitSamples", qaSfMarkInspectionProcessDefinitionKey)?.let {
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaSfmiAnalyzeSamples", assigneeId)
        }
        return false
    }

    fun qaSfMIAnalyzeSamplesComplete(permitId: Long, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Analyze the samples")
        qaCompleteTask(permitId, "qaSfmiAnalyzeSamples", qaSfMarkInspectionProcessDefinitionKey)?.let {
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaSfmiCheckLabReport", assigneeId)
        }
        return false
    }

    fun qaSfMICheckLabReportComplete(permitId: Long, qaoAssigneeId: Long, labAssigneeId: Long, labReportStatus: Boolean): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Check the lab report")
        updateTaskVariableByObjectIdAndKey(permitId, "qaSfmiCheckLabReport", qaSfMarkInspectionProcessDefinitionKey, "labReportStatus", bpmnCommonFunctions.booleanToInt(labReportStatus).toString())
        qaCompleteTask(permitId, "qaSfmiCheckLabReport", qaSfMarkInspectionProcessDefinitionKey)?.let {
            return if (labReportStatus) {   //lab report ok
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaSfmiAssignComplianceStatus", qaoAssigneeId)
            } else {
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaSfmiAnalyzeSamples", labAssigneeId)
            }
        }
        return false
    }

    fun qaSfMIAssignComplianceStatusComplete(permitId: Long, hofAssigneeId: Long, complianceStatus: Boolean): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Assign the compliance status")
        updateTaskVariableByObjectIdAndKey(permitId, "qaSfmiAssignComplianceStatus", qaSfMarkInspectionProcessDefinitionKey, "complianceStatus", bpmnCommonFunctions.booleanToInt(complianceStatus).toString())
        userRepo.findByIdOrNull(hofAssigneeId)?.let { usersEntity ->
            updateTaskVariableByObjectIdAndKey(permitId, "qaSfmiAssignComplianceStatus", qaSfMarkInspectionProcessDefinitionKey, "email", usersEntity.email.toString())
        }
        qaCompleteTask(permitId, "qaSfmiAssignComplianceStatus", qaSfMarkInspectionProcessDefinitionKey)?.let {
            return if (complianceStatus) {   //compliance status ok
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaSfmiApprovePermitRecommendation", hofAssigneeId)
            } else {
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaSfmiPerformCorrectiveAction", hofAssigneeId)
            }
        }
        return false
    }

    fun qaSfMIPerformCorrectiveActionComplete(permitId: Long, qaoAssigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Perform Corrective action complete")
        qaCompleteTask(permitId, "qaSfmiPerformCorrectiveAction", qaSfMarkInspectionProcessDefinitionKey)?.let {
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaSfmiFillFactoryInspectionForms", qaoAssigneeId)
        }
        return false
    }

    fun qaSfMIApprovePermitRecommendationComplete(permitId: Long, qaoAssigneeId: Long, permitRecommendationApproval: Boolean): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Approve the permit recommendation")
        qaCompleteTask(permitId, "qaSfmiApprovePermitRecommendation", qaSfMarkInspectionProcessDefinitionKey)?.let {
            return if (permitRecommendationApproval) {   //permit approval ok
                true
            } else {
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaSfmiPermitSurveillance", qaoAssigneeId)
            }
        }
        return false
    }

    fun qaSfMICheckIfPermitSurveillanceComplete(permitId: Long, qaoAssigneeId: Long, permitSurveillance: Boolean): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Check if permit surveillance will be done")
        qaCompleteTask(permitId, "qaSfmiPermitSurveillance", qaSfMarkInspectionProcessDefinitionKey)?.let {
            return if (permitSurveillance) {   //There will be factory surveillance
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaSfmiGenerateReportDuePermits", qaoAssigneeId)
            } else {
                true
            }
        }
        return false
    }

    fun qaSfMIFactorySurveillancePlanComplete(permitId: Long, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Factory Surveillance Generate Report of Due Permits and Workplan")
        qaCompleteTask(permitId, "qaSfmiGenerateReportDuePermits", qaSfMarkInspectionProcessDefinitionKey)?.let {
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaSfmiGenSupervisionScheme", assigneeId)
        }
        return false
    }

//    /*
//    ***********************************************************************************
//    * DM APPLICATION REVIEW
//    ***********************************************************************************
//     */
//    //Start the DM Application Review process
//    fun startQADMApplicationReviewProcess(permitId: Long, assigneeId: Long, foreignApplication: Boolean): HashMap<String, String>? {
//        var variables: HashMap<String, Any> = HashMap()
//        KotlinLogging.logger { }.info("permitId : $permitId : Starting QA DM application review process")
//        try {
//            //Remember to start by setting email and assignee to manufacturer
//            checkStartProcessInputs(permitId, assigneeId, qaDmApplicationReviewProcessDefinitionKey)?.let { checkVariables ->
//                val permit: PermitApplicationsEntity = checkVariables["permit"] as PermitApplicationsEntity
//                variables["permitId"] = permit.id.toString()
//                variables["permitRefNo"] = permit.permitRefNumber.toString()
//                variables["email"] = checkVariables["assigneeEmail"].toString()
//                variables["manufacturerEmail"] = checkVariables["manufacturerEmail"].toString()
//                variables["applicationComplete"] = 0
//                variables["inspectionReportApproved"] = 0
//                variables["testReportsAvailable"] = 0
//                variables["testReportsCompliant"] = 0
//                variables["labReportStatus"] = 0
//                variables["complianceStatus"] = 0
//                variables["supervisionSchemeAccepted"] = 0
//                variables["foreignDmark"] = bpmnCommonFunctions.booleanToInt(foreignApplication).toString()
//                variables["justificationReportApproved"] = 0
//
//                bpmnCommonFunctions.startBpmnProcess(qaDmApplicationReviewProcessDefinitionKey, qaDmApplicationReviewBusinessKey, variables, assigneeId)?.let {
//                    permit.dmAppReviewProcessInstanceId = it["processInstanceId"]
//                    //permit.sfMarkInspectionTaskId = it["taskId"]
//                    permit.dmAppReviewStartedOn = Timestamp.from(Instant.now())
//                    permit.dmAppReviewStatus = 1
//                    permitsRepo.save(permit)
//                    KotlinLogging.logger { }.info("permitId : $permitId : Successfully started QA DM application review process for permit")
//                    return it
//                } ?: run {
//                    KotlinLogging.logger { }.info("$permitId : Unable to start QA DM application review process")
//                }
//            }
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.error(e.message, e)
//        }
//        return null
//    }
//
//    fun qaDmARCheckApplicationComplete(permitId: Long, hofAssigneeId: Long, applicationComplete: Boolean): Boolean {
//        KotlinLogging.logger { }.info("PermitId : $permitId :  Check if application is complete")
//        updateTaskVariableByObjectIdAndKey(permitId, "qaDmarCheckApplication", qaDmApplicationReviewProcessDefinitionKey, "applicationComplete", bpmnCommonFunctions.booleanToInt(applicationComplete).toString())
//        qaCompleteTask(permitId, "qaDmarCheckApplication", qaDmApplicationReviewProcessDefinitionKey)?.let {
//            return if (applicationComplete) {   //application is complete
//                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmarAllocateQAO", hofAssigneeId)
//            } else {
//                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaSfmiAnalyzeSamples", hofAssigneeId)
//            }
//        }
//        return false
//    }
//
//    fun qaDmARManufacturerCorrectionComplete(permitId: Long, hofAssigneeId: Long): Boolean {
//        KotlinLogging.logger { }.info("PermitId : $permitId :  manufacturer correction complete")
//        qaCompleteTask(permitId, "qaDmarManufacturerCorrection", qaDmApplicationReviewProcessDefinitionKey)?.let {
//            qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmarCheckApplication", hofAssigneeId)
//        }
//        return false
//    }
//
//    fun qaDmARAllocateQAOComplete(permitId: Long, assigneeId: Long): Boolean {
//        KotlinLogging.logger { }.info("PermitId : $permitId :  Allocate QAO complete")
//        //Update email
//        userRepo.findByIdOrNull(assigneeId)?.let { usersEntity ->
//            updateTaskVariableByObjectIdAndKey(permitId, "qaDmarAllocateQAO", qaDmApplicationReviewProcessDefinitionKey, "email", usersEntity.email.toString())
//        }
//
//        qaCompleteTask(permitId, "qaDmarAllocateQAO", qaDmApplicationReviewProcessDefinitionKey)?.let {
//            val foreignDmark = bpmnCommonFunctions.getProcessVariables(it["processInstanceId"].toString())?.get("foreignDmark").toString()
//            if (foreignDmark == "1"){  //foreign dmark
//                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmarGenerateJustificationReport", assigneeId)
//            } else {
//                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmarFactoryInspection", assigneeId)
//            }
//        }
//        return false
//    }
//
//    //------------------------------------
//    //FOREIGN DMARK ADDITIONAL STEPS
//    //------------------------------------
//    fun qaDmARGenerateJustificationReportComplete(permitId: Long, assigneeId: Long): Boolean {
//        KotlinLogging.logger { }.info("PermitId : $permitId :  Generate Justification report complete")
//        qaCompleteTask(permitId, "qaDmarGenerateJustificationReport", qaDmApplicationReviewProcessDefinitionKey)?.let {
//            qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmarApproveJustificationReport", assigneeId)
//        }
//        return false
//    }
//
//    fun qaDmARApproveJustificationReportComplete(permitId: Long, assigneeId: Long,justificationReportApproved:Boolean): Boolean {
//        KotlinLogging.logger { }.info("PermitId : $permitId :  Approve the justification report complete")
//        updateTaskVariableByObjectIdAndKey(permitId, "qaDmarApproveJustificationReport", qaDmApplicationReviewProcessDefinitionKey, "justificationReportApproved", bpmnCommonFunctions.booleanToInt(justificationReportApproved).toString())
//        qaCompleteTask(permitId, "qaDmarApproveJustificationReport", qaDmApplicationReviewProcessDefinitionKey)?.let {
//            return if (justificationReportApproved) {   //justification report approved
//                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmarAssignAssessor", assigneeId)
//            } else {
//                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmarGenerateJustificationReport", assigneeId)
//            }
//        }
//        return false
//    }
//
//    fun qaDmARAssignAssessorComplete(permitId: Long, assigneeId: Long): Boolean {
//        KotlinLogging.logger { }.info("PermitId : $permitId :  Assign assessor complete")
//        qaCompleteTask(permitId, "qaDmarAssignAssessor", qaDmApplicationReviewProcessDefinitionKey)?.let {
//            qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmarFactoryInspection", assigneeId)
//        }
//        return false
//    }
//    //------------------------------------
//
//    fun qaDmARFactoryInspectionComplete(permitId: Long, qaoAssigneeId: Long): Boolean {
//        KotlinLogging.logger { }.info("PermitId : $permitId :  Factory inspection complete")
//        qaCompleteTask(permitId, "qaDmarFactoryInspection", qaDmApplicationReviewProcessDefinitionKey)?.let {
//            qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmarGenerateInspectionReport", qaoAssigneeId)
//        }
//        return false
//    }
//
//    fun qaDmARGenerateInspectionReportComplete(permitId: Long, hofAssigneeId: Long): Boolean {
//        KotlinLogging.logger { }.info("PermitId : $permitId :  Generate inspection report complete")
//        //Update email
//        userRepo.findByIdOrNull(hofAssigneeId)?.let { usersEntity ->
//            updateTaskVariableByObjectIdAndKey(permitId, "qaDmarGenerateInspectionReport", qaDmApplicationReviewProcessDefinitionKey, "email", usersEntity.email.toString())
//        }
//        qaCompleteTask(permitId, "qaDmarGenerateInspectionReport", qaDmApplicationReviewProcessDefinitionKey)?.let {
//            qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmarApproveInspectionReport", hofAssigneeId)
//        }
//        return false
//    }
//
//    fun qaDmARApproveInspectionReportComplete(permitId: Long, qaoAssigneeId: Long, inspectionReportApproved: Boolean): Boolean {
//        KotlinLogging.logger { }.info("PermitId : $permitId :  Approve the inspection report complete")
//        updateTaskVariableByObjectIdAndKey(permitId, "qaDmarApproveInspectionReport", qaDmApplicationReviewProcessDefinitionKey, "inspectionReportApproved", bpmnCommonFunctions.booleanToInt(inspectionReportApproved).toString())
//        qaCompleteTask(permitId, "qaDmarApproveInspectionReport", qaDmApplicationReviewProcessDefinitionKey)?.let {
//            qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmarGenerateSupervisionScheme", qaoAssigneeId)
//            return if (inspectionReportApproved) {   //inspection report approved
//                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmarTestReportsAvailable", qaoAssigneeId)
//            } else {
//                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmarGenerateInspectionReport", qaoAssigneeId)
//            }
//        }
//        return false
//    }
//
//    fun qaDmARGenSupervisionSchemeComplete(permitId: Long, assigneeId: Long): Boolean {
//        KotlinLogging.logger { }.info("PermitId : $permitId :  Generate supervision scheme")
//        qaCompleteTask(permitId, "qaDmarGenerateSupervisionScheme" , qaDmApplicationReviewProcessDefinitionKey)?.let {
//            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmarAcceptSupervisionScheme", assigneeId)
//        }
//        return false
//    }
//
//    fun qaDmARAcceptSupervisionScheme(permitId: Long, assigneeId: Long, supervisionSchemeAccepted: Boolean): Boolean {
//        KotlinLogging.logger { }.info("PermitId : $permitId :  Accept supervision scheme")
//        updateTaskVariableByObjectIdAndKey(permitId, "qaDmarAcceptSupervisionScheme", qaDmApplicationReviewProcessDefinitionKey, "supervisionSchemeAccepted", bpmnCommonFunctions.booleanToInt(supervisionSchemeAccepted).toString())
//        qaCompleteTask(permitId, "qaDmarAcceptSupervisionScheme", qaDmApplicationReviewProcessDefinitionKey)?.let {
//            if (!supervisionSchemeAccepted){
//                //return to sender
//                return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmarGenerateSupervisionScheme", assigneeId)
//            } else {
//                return true //process ends
//            }
//        }
//        return false
//    }
//
//    fun qaDmARCheckTestReportsComplete(permitId: Long, qaoAssigneeId: Long, testReportsAvailable: Boolean): Boolean {
//        KotlinLogging.logger { }.info("PermitId : $permitId :  Check if test reports available complete")
//        updateTaskVariableByObjectIdAndKey(permitId, "qaDmarTestReportsAvailable", qaDmApplicationReviewProcessDefinitionKey, "testReportsAvailable", bpmnCommonFunctions.booleanToInt(testReportsAvailable).toString())
//        qaCompleteTask(permitId, "qaDmarTestReportsAvailable", qaDmApplicationReviewProcessDefinitionKey)?.let {
//            return if (testReportsAvailable) {   //test reports available
//                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmarTestReportsCompliant", qaoAssigneeId)
//            } else {
//                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmarFillSSF", qaoAssigneeId)
//            }
//        }
//        return false
//    }
//
//    fun qaDmARTestReportsCompliantComplete(permitId: Long, qaoAssigneeId: Long, testReportsCompliant: Boolean): Boolean {
//        KotlinLogging.logger { }.info("PermitId : $permitId :  Check if test reports are compliant complete")
//        updateTaskVariableByObjectIdAndKey(permitId, "qaDmarTestReportsCompliant", qaDmApplicationReviewProcessDefinitionKey, "testReportsCompliant", bpmnCommonFunctions.booleanToInt(testReportsCompliant).toString())
//        qaCompleteTask(permitId, "qaDmarTestReportsCompliant", qaDmApplicationReviewProcessDefinitionKey)?.let {
//            return if (testReportsCompliant) {   //test reports are compliant
//                //update the task completion time and status
//                permitRepo.findByIdOrNull(permitId)?.let { permit ->//Check that the permit is valid
//                    permit.dmAppReviewStatus = 2
//                    permit.dmAppReviewCompletedOn = Timestamp.from(Instant.now())
//                    permitRepo.save(permit)
//                }
//                true
//            } else {
//                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmarFillSSF", qaoAssigneeId)
//            }
//        }
//        return false
//    }
//
//    fun qaDmARFillSSFComplete(permitId: Long, labAssigneeId: Long): Boolean {
//        KotlinLogging.logger { }.info("PermitId : $permitId :  Fill the SSF complete")
//        qaCompleteTask(permitId, "qaDmarFillSSF", qaDmApplicationReviewProcessDefinitionKey)?.let {
//            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmarReceiveSSF", labAssigneeId)
//        }
//        return false
//    }
//
//    fun qaDmARReceiveSSFComplete(permitId: Long, qaoAssigneeId: Long): Boolean {
//        KotlinLogging.logger { }.info("PermitId : $permitId :  Receive the SSF complete")
//        qaCompleteTask(permitId, "qaDmarReceiveSSF", qaDmApplicationReviewProcessDefinitionKey)?.let {
//            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmarSubmitSamples", qaoAssigneeId)
//        }
//        return false
//    }
//
//    fun qaDmARSubmitSamplesComplete(permitId: Long, labAssigneeId: Long): Boolean {
//        KotlinLogging.logger { }.info("PermitId : $permitId :  Submit the samples complete")
//        qaCompleteTask(permitId, "qaDmarSubmitSamples", qaDmApplicationReviewProcessDefinitionKey)?.let {
//            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmarAnalyzeSamples", labAssigneeId)
//        }
//        return false
//    }
//
//    fun qaDmARLabAnalysisComplete(permitId: Long, qaoAssigneeId: Long): Boolean {
//        KotlinLogging.logger { }.info("PermitId : $permitId :  Lab analysis complete")
//        userRepo.findByIdOrNull(qaoAssigneeId)?.let { usersEntity ->
//            updateTaskVariableByObjectIdAndKey(permitId, "qaDmarAnalyzeSamples", qaDmApplicationReviewProcessDefinitionKey, "email", usersEntity.email.toString())
//        }
//        qaCompleteTask(permitId, "qaDmarAnalyzeSamples", qaDmApplicationReviewProcessDefinitionKey)?.let {
//            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmarLabReportCorrect", qaoAssigneeId)
//        }
//        return false
//    }
//
//    fun qaDmARCheckLabReportsComplete(permitId: Long, qaoAssigneeId: Long, labAssigneeId: Long, labReportStatus: Boolean): Boolean {
//        KotlinLogging.logger { }.info("PermitId : $permitId :  Check if lab report correct complete")
//        updateTaskVariableByObjectIdAndKey(permitId, "qaDmarLabReportCorrect", qaDmApplicationReviewProcessDefinitionKey, "labReportStatus", bpmnCommonFunctions.booleanToInt(labReportStatus).toString())
//        qaCompleteTask(permitId, "qaDmarLabReportCorrect", qaDmApplicationReviewProcessDefinitionKey)?.let {
//            return if (labReportStatus) {   //test reports available
//                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmarAssignComplianceStatus", qaoAssigneeId)
//            } else {
//                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmarAnalyzeSamples", labAssigneeId)
//            }
//        }
//        return false
//    }
//
//    fun qaDmARAssignComplianceStatusComplete(permitId: Long, qaoAssigneeId: Long, complianceStatus: Boolean): Boolean {
//        KotlinLogging.logger { }.info("PermitId : $permitId :  Assign the compliance status complete")
//        updateTaskVariableByObjectIdAndKey(permitId, "qaDmarAssignComplianceStatus", qaDmApplicationReviewProcessDefinitionKey, "complianceStatus", bpmnCommonFunctions.booleanToInt(complianceStatus).toString())
//        fetchTaskByPermitId(permitId, qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
//            bpmnCommonFunctions.getTaskVariables(taskDetails[0].task.id)?.let {
//                updateTaskVariableByObjectIdAndKey(permitId, "qaDmarAssignComplianceStatus", qaDmApplicationReviewProcessDefinitionKey, "email", it["manufacturerEmail"].toString())
//            }
//        }
//        qaCompleteTask(permitId, "qaDmarAssignComplianceStatus", qaDmApplicationReviewProcessDefinitionKey)?.let {
//            return if (complianceStatus) {   //compliance Status ok
//                //update the task completion time and status
//                permitRepo.findByIdOrNull(permitId)?.let { permit ->//Check that the permit is valid
//                    permit.dmAppReviewStatus = 2
//                    permit.dmAppReviewCompletedOn = Timestamp.from(Instant.now())
//                    permitRepo.save(permit)
//                }
//                true //process ends here
//            } else {
//                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaoAssigneeId", qaoAssigneeId)
//            }
//        }
//        return false
//    }

    /*
     ***********************************************************************************
     * SF APPLICATION PAYMENT PROCESS
     ***********************************************************************************
      */
    //Start the SF Application Payment process
    fun startQASFApplicationPaymentProcess(permitId: Long, assigneeId: Long): HashMap<String, String>? {
        var variables: HashMap<String, Any> = HashMap()
        KotlinLogging.logger { }.info("permitId : $permitId : Starting QA SF application payment process")
        try {
            //Remember to start by setting email and assignee to manufacturer
            checkStartProcessInputs(permitId, assigneeId, qaSfApplicationPaymentProcessDefinitionKey)?.let { checkVariables ->
                val permit: PermitApplicationsEntity = checkVariables["permit"] as PermitApplicationsEntity
                variables["permitId"] = permit.id.toString()
                variables["permitRefNo"] = permit.permitRefNumber.toString()
                variables["email"] = checkVariables["assigneeEmail"].toString()
                variables["manufacturerEmail"] = checkVariables["manufacturerEmail"].toString()
                variables["renewal"] = 0
                variables["approvalStatus"] = 0
                permit.userId?.let { manufacturerId ->
                    bpmnCommonFunctions.startBpmnProcess(qaSfApplicationPaymentProcessDefinitionKey, qaSfApplicationPaymentBusinessKey, variables, manufacturerId)?.let {
                        permit.sfAppPaymentProcessInstanceId = it["processInstanceId"]
                        permit.sfAppPaymentStartedOn = Timestamp.from(Instant.now())
                        permit.sfAppPaymentStatus = 1
                        permitsRepo.save(permit)
                        KotlinLogging.logger { }.info("permitId : $permitId : Successfully started QA SF application payment process for permit")
                        return it
                    } ?: run {
                        KotlinLogging.logger { }.info("$permitId : Unable to start QA SF application payment process")
                    }
                }


            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }

    fun qaSfAPSelectMarkComplete(permitId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Select mark complete")
        qaCompleteTask(permitId, "qaSfapSelectMark", qaSfApplicationPaymentProcessDefinitionKey)?.let {
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), null, 0)
        }
        return false
    }

    fun qaSfApCheckIfRenewal(permitId: String): String {
        KotlinLogging.logger { }.info("PermitId : $permitId : QA DM App Payment check if renewal")
        return "0"
    }

    /*
    fun qaSfAPSelectIfRenewalComplete(permitId: Long,hofAssigneeId: Long,renewal:Boolean): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Select if renewal complete")
        updateTaskVariableByPermitIdAndKey(permitId,"qaSfapCheckIfRenewal",qaSfApplicationPaymentProcessDefinitionKey,"renewal",bpmnCommonFunctions.booleanToInt(renewal).toString())
        qaCompleteTask(permitId,"qaSfapCheckIfRenewal",qaSfApplicationPaymentProcessDefinitionKey)?.let {
            return if (renewal){
                qaAssignTask(it["permit"] as PermitApplicationEntity,it["processInstanceId"].toString(),"qaSfapMakePayment",0,true)
            } else {
                qaAssignTask(it["permit"] as PermitApplicationEntity,it["processInstanceId"].toString(),"qaSfapFillAndSubmit",hofAssigneeId,false)
            }
        }
        return false
    }
    */

    fun qaSfAPFillAndSubmitComplete(permitId: Long, hofAssigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Fill and submit complete")
        qaCompleteTask(permitId, "qaSfapFillAndSubmit", qaSfApplicationPaymentProcessDefinitionKey)?.let {
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaSfapReviewApplication", hofAssigneeId)
        }
        return false
    }

    fun qaSfAPReviewApplicationComplete(permitId: Long, approvalStatus: Boolean): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Review application complete")
        updateTaskVariableByObjectIdAndKey(permitId, "qaSfapReviewApplication", qaSfApplicationPaymentProcessDefinitionKey, "approvalStatus", bpmnCommonFunctions.booleanToInt(approvalStatus).toString())
        qaCompleteTask(permitId, "qaSfapReviewApplication", qaSfApplicationPaymentProcessDefinitionKey)?.let {
            return if (approvalStatus) {
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaSfapMakePayment", 0)
            } else {
                true
            }
        }
        return false
    }

    fun qaSfAPPaymentComplete(permitId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Payment complete")
        qaCompleteTask(permitId, "qaSfapMakePayment", qaSfApplicationPaymentProcessDefinitionKey)?.let {
            return true
        }
        return false
    }

    /*
    ***********************************************************************************
    * SF PERMIT AWARD PROCESS
    ***********************************************************************************
    */
    //Start the SF Permit Award process
    fun startQASFPermitAwardProcess(permitId: Long, assigneeId: Long): HashMap<String, String>? {
        var variables: HashMap<String, Any> = HashMap()
        KotlinLogging.logger { }.info("permitId : $permitId : Starting QA SF permit award process")
        try {
            //Remember to start by setting email and assignee to manufacturer
            checkStartProcessInputs(permitId, assigneeId, qaSfPermitAwardProcessDefinitionKey)?.let { checkVariables ->
                val permit: PermitApplicationsEntity = checkVariables["permit"] as PermitApplicationsEntity
                variables["permitId"] = permit.id.toString()
                variables["permitRefNo"] = permit.permitRefNumber.toString()
                variables["email"] = checkVariables["assigneeEmail"].toString()
                variables["manufacturerEmail"] = checkVariables["manufacturerEmail"].toString()
                variables["permitApproved"] = 0
                variables["permitAwarded"] = 0

                bpmnCommonFunctions.startBpmnProcess(qaSfPermitAwardProcessDefinitionKey, qaSfPermitAwardBusinessKey, variables, assigneeId)?.let {
                    permit.sfPermitAwardProcessInstanceId = it["processInstanceId"]
                    permit.sfPermitAwardStartedOn = Timestamp.from(Instant.now())
                    permit.sfPermitAwardStatus = processStarted
                    permitsRepo.save(permit)
                    KotlinLogging.logger { }.info("permitId : $permitId : Successfully started QA SF permit award process for permit")
                    return it
                } ?: run {
                    KotlinLogging.logger { }.info("$permitId : Unable to start QA SF permit award process")
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }

    fun qaSfPAPscApprovalComplete(permitId: Long, qaoAssigneeId: Long, pcmAssigneeId: Long, permitApproved: Boolean): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  QA SF Permit approval complete")
        if (permitApproved) {
            userRepo.findByIdOrNull(pcmAssigneeId)?.let { usersEntity ->
                updateTaskVariableByObjectIdAndKey(permitId, "qaSfpaPscApproval", qaSfPermitAwardProcessDefinitionKey, "email", usersEntity.email.toString())
            }
        } else {
            userRepo.findByIdOrNull(qaoAssigneeId)?.let { usersEntity ->
                updateTaskVariableByObjectIdAndKey(permitId, "qaSfpaPscApproval", qaSfPermitAwardProcessDefinitionKey, "email", usersEntity.email.toString())
            }
        }
        updateTaskVariableByObjectIdAndKey(permitId, "qaSfpaPscApproval", qaSfPermitAwardProcessDefinitionKey, "permitApproved", bpmnCommonFunctions.booleanToInt(permitApproved).toString())

        qaCompleteTask(permitId, "qaSfpaPscApproval", qaSfPermitAwardProcessDefinitionKey)?.let {
            return if (permitApproved) {
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaSfpaPCMPermitAward", pcmAssigneeId)
            } else {
                true
            }
        }
        return false
    }

    fun qaSfPAPermitAwardComplete(permitId: Long, qaoAssigneeId: Long, permitAwarded: Boolean): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  QA SF Permit award complete")
        userRepo.findByIdOrNull(qaoAssigneeId)?.let { usersEntity ->
            updateTaskVariableByObjectIdAndKey(permitId, "qaSfpaPCMPermitAward", qaSfPermitAwardProcessDefinitionKey, "email", usersEntity.email.toString())
        }
        updateTaskVariableByObjectIdAndKey(permitId, "qaSfpaPCMPermitAward", qaSfPermitAwardProcessDefinitionKey, "permitAwarded", bpmnCommonFunctions.booleanToInt(permitAwarded).toString())

        qaCompleteTask(permitId, "qaSfpaPCMPermitAward", qaSfPermitAwardProcessDefinitionKey)?.let {
            return true
        }
        return false
    }

    fun endQASFPermitAwardProcess(permitId: String) {
        KotlinLogging.logger { }.info("End QA SF Permit award for permit $permitId............")
        try {
            permitRepo.findByIdOrNull(permitId.toLong())?.let { permit ->
                permit.sfPermitAwardCompletedOn = Timestamp.from(Instant.now())
                permit.sfPermitAwardStatus = processCompleted
                permitRepo.save(permit)
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("$permitId : Unable to complete QA SF permit award process")
        }
    }

    /*
    ***********************************************************************************
    * II SCHEDULE PROCESS
    ***********************************************************************************
    */
    //Start the II Schedule process
    fun startQAIIScheduleProcess(objectId: Long, assigneeId: Long, customerEmail:String): HashMap<String, String>? {
        var variables: HashMap<String, Any> = HashMap()
        KotlinLogging.logger { }.info("objectId : $objectId : Starting QA II schedule process")
        try {
            checkStartProcessInputs(objectId, assigneeId, qaIiScheduleProcessDefinitionKey)?.let { checkVariables ->
                val piie: PetroleumInstallationInspectionEntity = checkVariables["petroleumInstallationInspection"] as PetroleumInstallationInspectionEntity
                variables["objectId"] = piie.id.toString()
                variables["email"] = checkVariables["assigneeEmail"].toString()
                variables["manufacturerEmail"] = checkVariables["manufacturerEmail"].toString()
                variables["customerEmail"] = customerEmail
                variables["petroleumManagerEmail"] = ""
                variables["permitApproved"] = 0
                variables["permitAwarded"] = 0
                variables["paymentAmount"] = 0
                variables["qaoUserId"] = 0
                variables["pmUserId"] = 0

                bpmnCommonFunctions.startBpmnProcess(qaIiScheduleProcessDefinitionKey, qaIiScheduleBusinessKey, variables, assigneeId)?.let {
                    piie.iisProcessInstanceId = it["processInstanceId"]
                    piie.iisStartedOn = Timestamp.from(Instant.now())
                    piie.iisStatus = processStarted
                    petroleumIIRepo.save(piie)
                    KotlinLogging.logger { }.info("objectId : $objectId : Successfully started QA II schedule process for permit")
                    return it
                } ?: run {
                    KotlinLogging.logger { }.info("$objectId : Unable to start QA II schedule process")
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }

    fun qaIISDirectorReviewComplete(objectId: Long, pmAssigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Director review complete")
        qaCompleteTask(objectId, "qaIisDirectorReviewAndSubmit", qaIiScheduleProcessDefinitionKey)?.let {
            return qaAssignTask(null, it["processInstanceId"].toString(), "qaIisPMReview", pmAssigneeId)
        }
        return false
    }

    fun qaIISPetroleumMangerReviewComplete(objectId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Petroleum manager review complete")
        var currAssigneeId: Long = 0
        fetchTaskByPermitId(objectId, qaIiScheduleProcessDefinitionKey)?.let { taskDetails ->
            currAssigneeId = taskDetails[0].task.assignee.toLong()
        }
        qaCompleteTask(objectId, "qaIisPMReview", qaIiScheduleProcessDefinitionKey)?.let {
            return qaAssignTask(null, it["processInstanceId"].toString(), "qaIisAllocateInspectionOfficer", currAssigneeId)
        }
        return false
    }

    fun qaIISAllocateIOComplete(objectId: Long, qaoAssigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Inspection officer assign complete")
        qaCompleteTask(objectId, "qaIisAllocateInspectionOfficer", qaIiScheduleProcessDefinitionKey)?.let {
            return qaAssignTask(null, it["processInstanceId"].toString(), "qaIisGenerateProformaInvoice", qaoAssigneeId)
        }
        return false
    }

    fun qaIISGenerateProformaInvComplete(objectId: Long,qaoAssigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Generate and send proforma invoice complete")
        qaCompleteTask(objectId, "qaIisGenerateProformaInvoice", qaIiScheduleProcessDefinitionKey)?.let {
            return qaAssignTask(null, it["processInstanceId"].toString(), "qaIisCustomerPayment", qaoAssigneeId)
        }
        return false
    }

    fun qaIISCustomerPaymentComplete(objectId: Long, qaoAssigneeId: Long, paymentAmount:Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Customer payment complete")
        userRepo.findByIdOrNull(qaoAssigneeId)?.let { usersEntity ->
            updateTaskVariableByObjectIdAndKey(objectId, "qaIisCustomerPayment", qaIiScheduleProcessDefinitionKey, "email", usersEntity.email.toString())
        }
        updateTaskVariableByObjectIdAndKey(objectId, "qaIisCustomerPayment", qaIiScheduleProcessDefinitionKey, "paymentAmount", paymentAmount.toString())
        qaCompleteTask(objectId, "qaIisCustomerPayment", qaIiScheduleProcessDefinitionKey)?.let {
            return qaAssignTask(null, it["processInstanceId"].toString(), "qaIisSchedCalendarVisit", qaoAssigneeId)
        }
        return false
    }

    fun qaIISScheduleVisitComplete(objectId: Long, qaoAssigneeId: Long, pmAssigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Schedule visit complete")
        /*
        userRepo.findByIdOrNull(pmAssigneeId)?.let { usersEntity ->
            updateTaskVariableByObjectIdAndKey(objectId, "qaIisSchedCalendarVisit", qaIiScheduleProcessDefinitionKey, "petroleumManagerEmail", usersEntity.email.toString())
        }
        userRepo.findByIdOrNull(custAssigneeId)?.let { usersEntity ->
            updateTaskVariableByObjectIdAndKey(objectId, "qaIisSchedCalendarVisit", qaIiScheduleProcessDefinitionKey, "customerEmail", usersEntity.email.toString())
        }
        userRepo.findByIdOrNull(qaoAssigneeId)?.let { usersEntity ->
            updateTaskVariableByObjectIdAndKey(objectId, "qaIisSchedCalendarVisit", qaIiScheduleProcessDefinitionKey, "email", usersEntity.email.toString())
        }
         */
        updateTaskVariableByObjectIdAndKey(objectId, "qaIisSchedCalendarVisit", qaIiScheduleProcessDefinitionKey, "qaoUserId", qaoAssigneeId.toString())
        updateTaskVariableByObjectIdAndKey(objectId, "qaIisSchedCalendarVisit", qaIiScheduleProcessDefinitionKey, "pmUserId", pmAssigneeId.toString())
        qaCompleteTask(objectId, "qaIisSchedCalendarVisit", qaIiScheduleProcessDefinitionKey)?.let {
            return true
        }
        return false
    }

    fun endQAIIScheduleProcess(objectId: String) {
        KotlinLogging.logger { }.info("End QA II Schedule for objectId $objectId............")
        try {
            petroleumIIRepo.findByIdOrNull(objectId.toLong())?.let { piie ->
                piie.iisCompletedOn = Timestamp.from(Instant.now())
                piie.iisStatus = processCompleted
                petroleumIIRepo.save(piie)
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("$objectId : Unable to complete QA II Schedule process")
        }
    }

    /*
    ***********************************************************************************
    * II REPORTING PROCESS
    ***********************************************************************************
    */
    //Start the II Reporting process
    fun startQAIIReportingProcess(objectId: Long, assigneeId: Long, customerEmail: String): HashMap<String, String>? {
        var variables: HashMap<String, Any> = HashMap()
        KotlinLogging.logger { }.info("objectId : $objectId : Starting QA II reporting process")
        try {
            checkStartProcessInputs(objectId, assigneeId, qaIiReportingProcessDefinitionKey)?.let { checkVariables ->
                val piie: PetroleumInstallationInspectionEntity = checkVariables["petroleumInstallationInspection"] as PetroleumInstallationInspectionEntity
                variables["objectId"] = piie.id.toString()
                variables["email"] = checkVariables["assigneeEmail"].toString()
                //variables["manufacturerEmail"] = checkVariables["manufacturerEmail"].toString()
                variables["customerEmail"] = customerEmail
                variables["approvalStatus"] = ""

                bpmnCommonFunctions.startBpmnProcess(qaIiReportingProcessDefinitionKey, qaIiReportingBusinessKey, variables, assigneeId)?.let {
                    piie.iirProcessInstanceId = it["processInstanceId"]
                    piie.iirStartedOn = Timestamp.from(Instant.now())
                    piie.iirStatus = processStarted
                    petroleumIIRepo.save(piie)
                    KotlinLogging.logger { }.info("objectId : $objectId : Successfully started QA II reporting process for object")
                    return it
                } ?: run {
                    KotlinLogging.logger { }.info("$objectId : Unable to start QA II reporting process")
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }

    fun qaIIRInspectionComplete(objectId: Long, hofAssigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId : Inspection complete")
        qaCompleteTask(objectId, "qaIirInspection", qaIiReportingProcessDefinitionKey)?.let {
            return qaAssignTask(null, it["processInstanceId"].toString(), "qaIirHofApplicationReview", hofAssigneeId)
        }
        return false
    }

    fun qaIIRApplicationReviewComplete(objectId: Long, qaoAssigneeId: Long, directorAssigneeId: Long, approvalStatus: Boolean): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId : Application review complete")
        if (approvalStatus) {
            userRepo.findByIdOrNull(directorAssigneeId)?.let { usersEntity ->
                updateTaskVariableByObjectIdAndKey(objectId, "qaIirHofApplicationReview", qaIiReportingProcessDefinitionKey, "email", usersEntity.email.toString())
            }
        }
        updateTaskVariableByObjectIdAndKey(objectId, "qaIirHofApplicationReview", qaIiReportingProcessDefinitionKey, "approvalStatus", bpmnCommonFunctions.booleanToInt(approvalStatus).toString())

        qaCompleteTask(objectId, "qaIirHofApplicationReview", qaIiReportingProcessDefinitionKey)?.let {
            return if (approvalStatus) {
                true
            } else {
                qaAssignTask(null, it["processInstanceId"].toString(), "qaIirInspection", qaoAssigneeId)
            }
        }
        return false
    }

    fun endQAIIReportingProcess(objectId: String) {
        KotlinLogging.logger { }.info("End QA II Reporting for objectId $objectId............")
        try {
            petroleumIIRepo.findByIdOrNull(objectId.toLong())?.let { piie ->
                piie.iirCompletedOn = Timestamp.from(Instant.now())
                piie.iirStatus = processCompleted
                petroleumIIRepo.save(piie)
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("$objectId : Unable to complete QA II Reporting process")
        }
    }

    /*
    ***********************************************************************************
    * DM ASSESSMENT PROCESS
    ***********************************************************************************
    */
    //Start the DM Assessment process
    fun startQADmAssessmentProcess(permitId: Long, assigneeId: Long): HashMap<String, String>? {
        var variables: HashMap<String, Any> = HashMap()
        KotlinLogging.logger { }.info("permitId : $permitId : Starting QA DM assessment process")
        try {
            checkStartProcessInputs(permitId, assigneeId, qaDmAssessmentProcessDefinitionKey)?.let { checkVariables ->
                val permit: PermitApplicationsEntity = checkVariables["permit"] as PermitApplicationsEntity
                variables["permitId"] = permit.id.toString()
                variables["permitRefNo"] = permit.permitRefNumber.toString()
                variables["email"] = checkVariables["assigneeEmail"].toString()
                variables["manufacturerEmail"] = checkVariables["manufacturerEmail"].toString()
                variables["assessorEmail"] = ""
                variables["qaoEmail"] = ""
                variables["justificationReportApproved"] = 0
                variables["assessmentReportApproved"] = 0
                variables["reportCompliant"] = 0
                variables["pacApproval"] = 0
                variables["pcmApproval"] = 0
                variables["manufacturerCorrectionRequired"] = 0
                variables["manufacturerAssessorCorrectionRequired"] = 0

                bpmnCommonFunctions.startBpmnProcess(qaDmAssessmentProcessDefinitionKey, qaDmAssessmentBusinessKey, variables, assigneeId)?.let {
                    permit.dmAssessmentProcessInstanceId = it["processInstanceId"]
                    permit.dmAssessmentStartedOn = Timestamp.from(Instant.now())
                    permit.dmAssessmentStatus = processStarted
                    permitsRepo.save(permit)
                    KotlinLogging.logger { }.info("permitId : $permitId : Successfully started QA DM assessment process for permit")
                    return it
                } ?: run {
                    KotlinLogging.logger { }.info("$permitId : Unable to start QA DM assessment process")
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }

    fun qaDmasGenJustificationRptComplete(permitId: Long, hodAssigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId : QA DM Assessment Generate justification report complete")
        qaCompleteTask(permitId, "qaDmasGenJustificationRpt", qaDmAssessmentProcessDefinitionKey)?.let {
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmasApproveJustificationRpt", hodAssigneeId)
        }
        return false
    }

    fun qaDmasApproveJustificationRptComplete(permitId: Long, qaoAssigneeId: Long, justificationReportApproved: Boolean): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  QA DM Assessment justification approval complete")
        var currAssigneeId: Long = 0
        fetchTaskByPermitId(permitId, qaDmAssessmentProcessDefinitionKey)?.let { taskDetails ->
            currAssigneeId = taskDetails[0].task.assignee.toLong()
        }
        if (!justificationReportApproved) {
            userRepo.findByIdOrNull(qaoAssigneeId)?.let { usersEntity ->
                updateTaskVariableByObjectIdAndKey(permitId, "qaDmasApproveJustificationRpt", qaDmAssessmentProcessDefinitionKey, "email", usersEntity.email.toString())
            }
        }
        updateTaskVariableByObjectIdAndKey(permitId, "qaDmasApproveJustificationRpt", qaDmAssessmentProcessDefinitionKey, "justificationReportApproved", bpmnCommonFunctions.booleanToInt(justificationReportApproved).toString())

        qaCompleteTask(permitId, "qaDmasApproveJustificationRpt", qaDmAssessmentProcessDefinitionKey)?.let {
            return if (justificationReportApproved) {
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmasAppointAssessor", currAssigneeId)
            } else {
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmasGenJustificationRpt", qaoAssigneeId)
            }
        }
        return false
    }

    fun qaDmasAppointAssessorComplete(permitId: Long, assessorAssigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId : QA DM Appoint Assessor complete")
        userRepo.findByIdOrNull(assessorAssigneeId)?.let { usersEntity ->
            updateTaskVariableByObjectIdAndKey(permitId, "qaDmasAppointAssessor", qaDmAssessmentProcessDefinitionKey, "email", usersEntity.email.toString())
        }
        qaCompleteTask(permitId, "qaDmasAppointAssessor", qaDmAssessmentProcessDefinitionKey)?.let {
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmasScheduleFactoryVisit", assessorAssigneeId)
        }
        return false
    }

    fun qaScheduleFactoryVisitComplete(permitId: Long, assessorAssigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId : QA DM Appoint Assessor complete")
        qaCompleteTask(permitId, "qaDmasScheduleFactoryVisit", qaDmAssessmentProcessDefinitionKey)?.let {
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmasGenAssessmentRpt", assessorAssigneeId)
        }
        return false
    }

    fun qaDmasGenerateAssessmentRptComplete(permitId: Long, hodAssigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId : QA DM Generate assessment report complete")
        qaCompleteTask(permitId, "qaDmasGenAssessmentRpt", qaDmAssessmentProcessDefinitionKey)?.let {
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmasApproveAssessmentReport", hodAssigneeId)
        }
        return false
    }

    fun qaDmasApproveAssessmentRptComplete(permitId: Long, assessorAssigneeId: Long, assessmentReportApproved: Boolean): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  QA DM Assessment report approval complete")
        var currAssigneeId: Long = 0
        fetchTaskByPermitId(permitId, qaDmAssessmentProcessDefinitionKey)?.let { taskDetails ->
            currAssigneeId = taskDetails[0].task.assignee.toLong()
        }
        updateTaskVariableByObjectIdAndKey(permitId, "qaDmasApproveAssessmentReport", qaDmAssessmentProcessDefinitionKey, "assessmentReportApproved", bpmnCommonFunctions.booleanToInt(assessmentReportApproved).toString())

        qaCompleteTask(permitId, "qaDmasApproveAssessmentReport", qaDmAssessmentProcessDefinitionKey)?.let {
            return if (assessmentReportApproved) {
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmasCheckReportCompliant", currAssigneeId)
            } else {
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmasGenAssessmentRpt", assessorAssigneeId)
            }
        }
        return false
    }

    fun qaDmasAssessmentRptCompliantComplete(permitId: Long, pacAssigneeId: Long, reportCompliant: Boolean): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  QA DM Assessment report compliant complete")
        updateTaskVariableByObjectIdAndKey(permitId, "qaDmasCheckReportCompliant", qaDmAssessmentProcessDefinitionKey, "reportCompliant", bpmnCommonFunctions.booleanToInt(reportCompliant).toString())
        qaCompleteTask(permitId, "qaDmasCheckReportCompliant", qaDmAssessmentProcessDefinitionKey)?.let {
            return if (reportCompliant) {
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmasPacApproval", pacAssigneeId)
            } else {
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmasCorrectiveAction", 0)
            }
        }
        return false
    }

    fun qaDmasCorrectiveActionComplete(permitId: Long, assigneeId: Long, manufacturerId:Long, manufacturerCorrectionRequired:Boolean): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  QA DM Assessment perform corrective action complete")
        updateTaskVariableByObjectIdAndKey(permitId, "qaDmasCorrectiveAction", qaDmAssessmentProcessDefinitionKey, "manufacturerCorrectionRequired", bpmnCommonFunctions.booleanToInt(manufacturerCorrectionRequired).toString())
        qaCompleteTask(permitId, "qaDmasCorrectiveAction", qaDmAssessmentProcessDefinitionKey)?.let {
            return if (manufacturerCorrectionRequired) {
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmasManufacturerCorrectiveAction", manufacturerId)
            } else {
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmasScheduleFactoryVisit", assigneeId)
            }
        }
        return false
    }

    fun qaDmasManufacturerCorrectionComplete(permitId: Long, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId : QA DM Manufacturer Corrective Action complete")
        qaCompleteTask(permitId, "qaDmasManufacturerCorrectiveAction", qaDmAssessmentProcessDefinitionKey)?.let {
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmasCorrectiveAction", assigneeId)
        }
        return false
    }

    fun qaDmasPacApprovalComplete(permitId: Long, assessorAssigneeId: Long, pcmAssigneeId: Long,pacApproval: Boolean): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  QA DM Assessment PAC Approval complete")
        /*
        if (!pacApproval) {
            userRepo.findByIdOrNull(assessorAssigneeId)?.let { usersEntity ->
                updateTaskVariableByObjectIdAndKey(permitId, "qaDmasPacApproval", qaDmAssessmentProcessDefinitionKey, "assessorEmail", usersEntity.email.toString())
            }
            userRepo.findByIdOrNull(qaoAssigneeId)?.let { usersEntity ->
                updateTaskVariableByObjectIdAndKey(permitId, "qaDmasPacApproval", qaDmAssessmentProcessDefinitionKey, "qaoEmail", usersEntity.email.toString())
            }
        }
         */

        updateTaskVariableByObjectIdAndKey(permitId, "qaDmasPacApproval", qaDmAssessmentProcessDefinitionKey, "pacApproval", bpmnCommonFunctions.booleanToInt(pacApproval).toString())

        qaCompleteTask(permitId, "qaDmasPacApproval", qaDmAssessmentProcessDefinitionKey)?.let {
            return if (pacApproval) {
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmasPcmApproval", pcmAssigneeId)
            } else {
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmasAssessorCorrection", assessorAssigneeId)
            }
        }
        return false
    }

    fun qaDmasAssessorCorrectionComplete(permitId: Long, pacAssigneeId: Long, manufacturerId:Long, manufacturerAssessorCorrectionRequired:Boolean): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  QA DM Assessor Correction complete")
        updateTaskVariableByObjectIdAndKey(permitId, "qaDmasAssessorCorrection", qaDmAssessmentProcessDefinitionKey, "manufacturerAssessorCorrectionRequired", bpmnCommonFunctions.booleanToInt(manufacturerAssessorCorrectionRequired).toString())
        qaCompleteTask(permitId, "qaDmasAssessorCorrection", qaDmAssessmentProcessDefinitionKey)?.let {
            return if (manufacturerAssessorCorrectionRequired) {
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmasManufacturerAssessorCorrection", manufacturerId)
            } else {
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmasPacApproval", pacAssigneeId)
            }
        }
        return false
    }

    fun qaDmasAssessorManufacturerCorrectionComplete(permitId: Long, assessorAssigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId : QA DM Manufacturer Assessor Correction complete")
        qaCompleteTask(permitId, "qaDmasManufacturerAssessorCorrection", qaDmAssessmentProcessDefinitionKey)?.let {
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmasCorrectiveAction", assessorAssigneeId)
        }
        return false
    }


    fun qaDmasPcmApprovalComplete(permitId: Long, assessorAssigneeId: Long, pcmApproval: Boolean): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  QA DM Assessment PAC Approval complete")
        /*
        if (!pcmApproval) {
            userRepo.findByIdOrNull(assessorAssigneeId)?.let { usersEntity ->
                updateTaskVariableByObjectIdAndKey(permitId, "qaDmasPcmApproval", qaDmAssessmentProcessDefinitionKey, "assessorEmail", usersEntity.email.toString())
            }
            userRepo.findByIdOrNull(qaoAssigneeId)?.let { usersEntity ->
                updateTaskVariableByObjectIdAndKey(permitId, "qaDmasPcmApproval", qaDmAssessmentProcessDefinitionKey, "qaoEmail", usersEntity.email.toString())
            }
        }
         */
        updateTaskVariableByObjectIdAndKey(permitId, "qaDmasPcmApproval", qaDmAssessmentProcessDefinitionKey, "pcmApproval", bpmnCommonFunctions.booleanToInt(pcmApproval).toString())

        qaCompleteTask(permitId, "qaDmasPcmApproval", qaDmAssessmentProcessDefinitionKey)?.let {
            return if (pcmApproval) {
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmasSurveillanceWorkplan", 0)
            } else {
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmasAssessorCorrection", assessorAssigneeId)
            }
        }
        return false
    }

    fun qaDmasSurveillanceWorkplanComplete(permitId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId : QA DM Create surveillance workplan complete")
        qaCompleteTask(permitId, "qaDmasSurveillanceWorkplan", qaDmAssessmentProcessDefinitionKey)?.let {
            return true
        }
        return false
    }

    /*
    ***********************************************************************************
    * DM APPLICATION PAYMENT PROCESS
    ***********************************************************************************
    */
    fun startQADmAppPaymentProcess(permitId: Long, assigneeId: Long?): HashMap<String, String>? {
        var variables: HashMap<String, Any> = HashMap()
        KotlinLogging.logger { }.info("permitId : $permitId : Starting QA DM app payment process")
        try {
            checkStartProcessInputs(permitId, assigneeId, qaApplicationAndPaymentProcessDefinitionKey)?.let { checkVariables ->
                val permit: PermitApplicationsEntity = checkVariables["permit"] as PermitApplicationsEntity
                variables["permitId"] = permit.id.toString()
                variables["permitRefNo"] = permit.permitRefNumber.toString()
                variables["email"] = checkVariables["assigneeEmail"].toString()
                variables["manufacturerEmail"] = checkVariables["manufacturerEmail"].toString()
                variables["isRenewal"] = 0
                variables["isDomestic"] = 0
                variables["foreignApplicationComplete"] = 0
                variables["domesticApplicationComplete"] = 0
                variables["pcmId"] = 0
                variables["candidateGroup"] = ""
                //In this case the first assignee is the manufacturer
                permit.userId?.let { manufacturerId ->
                    bpmnCommonFunctions.startBpmnProcess(qaApplicationAndPaymentProcessDefinitionKey, qaDmAppPaymentBusinessKey, variables, manufacturerId)?.let {
                        permit.dmAppPaymentProcessInstanceId = it["processInstanceId"]
                        permit.dmAppPaymentStartedOn = Timestamp.from(Instant.now())
                        permit.dmAppPaymentStatus = processStarted
                        permitsRepo.save(permit)
                        KotlinLogging.logger { }.info("permitId : $permitId : Successfully started QA DM app payment process for permit")
                        return it
                    } ?: run {
                        KotlinLogging.logger { }.info("$permitId : Unable to start QA DM app payment process")
                    }
                }

            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }

    fun qaDmSubmitApplicationComplete(permitId: Long, isRenewal: Boolean, isDomestic: Boolean): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId : QA DM App Payment submit application complete")
        updateTaskVariableByObjectIdAndKey(permitId, "QaDMAppSubmitApplication", qaDmAppPaymentBusinessKey, "isRenewal", bpmnCommonFunctions.booleanToInt(isRenewal).toString())
        updateTaskVariableByObjectIdAndKey(permitId, "QaDMAppSubmitApplication", qaDmAppPaymentBusinessKey, "isDomestic", bpmnCommonFunctions.booleanToInt(isDomestic).toString())
        updateTaskVariableByObjectIdAndKey(permitId, "QaDMAppSubmitApplication", qaDmAppPaymentBusinessKey, "candidateGroup", pcmCandidateGroup)
        qaCompleteTask(permitId, "QaDMAppSubmitApplication", qaApplicationAndPaymentProcessDefinitionKey)?.let {
            return true
//            return if (!isRenewal && !isDomestic) {
//                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "QaDMAppPCMCheckComplete", 0, pcmCandidateGroup)
//            } else {
//                true
//            }
        }
        return false
    }

    fun qaDmCheckApplicationComplete(permitId: Long, foreignApplicationComplete: Boolean): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId : QA DM App Payment check application complete")
        updateTaskVariableByObjectIdAndKey(permitId, "QaDMAppPCMCheckComplete", qaDmAppPaymentBusinessKey,
            "foreignApplicationComplete", bpmnCommonFunctions.booleanToInt(foreignApplicationComplete).toString())
        qaCompleteTask(permitId, "QaDMAppPCMCheckComplete", qaApplicationAndPaymentProcessDefinitionKey)?.let {
            if (foreignApplicationComplete) {
                return true
            } else {
                val permit: PermitApplicationsEntity? = permitsRepo.findByIdOrNull(permitId)
                val manufacturerId = permit?.userId
                manufacturerId?.let { manufacturerId ->
                    return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "QaDMAppManufacturerCorrection", manufacturerId)
                }
            }
        }
        return false
    }


    fun qaDmManufacturerCorrectionComplete(permitId: Long, pcmAssigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId : QA DM App Payment manufacturer correction complete")
        qaCompleteTask(permitId, "QaDMAppManufacturerCorrection", qaApplicationAndPaymentProcessDefinitionKey)?.let {
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "QaDMAppPCMCheckComplete", pcmAssigneeId)
        }
        return false
    }

    //End DM Application Process
    fun endDmApplicationProcess(permitId: String) {
        KotlinLogging.logger { }.info("End DM Application & Payment process for permitId $permitId")
        try{
            permitsRepo.findByIdOrNull(permitId.toLong())?.let { permit ->
                permit.dmAppPaymentCompletedOn = Timestamp.from(Instant.now())
                permit.dmAppPaymentStatus = processCompleted
                permitsRepo.save(permit)
            }
        } catch (e: Exception){
            KotlinLogging.logger { }.error("$permitId : Unable to complete DM Application process for $permitId")
        }
    }


    /*
    ***********************************************************************************
    * DM APPLICATION REVIEW
    ***********************************************************************************
     */

    //Start the DM Application Review process
    fun startQaDMApplicationReviewProcess(permitId: Long, assigneeId: Long): HashMap<String, String>? {
        var variables: HashMap<String, Any> = HashMap()
        KotlinLogging.logger { }.info("permitId : $permitId : Starting QA DM application review process")
        try {
            //Remember to start by setting email and assignee to manufacturer
            checkStartProcessInputs(permitId, assigneeId, qaDmApplicationReviewProcessDefinitionKey)?.let { checkVariables ->
                val permit: PermitApplicationsEntity = checkVariables["permit"] as PermitApplicationsEntity
                variables["permitId"] = permit.id.toString()
                variables["permitRefNo"] = permit.permitRefNumber.toString()
                variables["email"] = checkVariables["assigneeEmail"].toString()
                variables["manufacturerEmail"] = checkVariables["manufacturerEmail"].toString()
                variables["applicationComplete"] = 0
                variables["inspectionReportApproved"] = 0
                variables["testReportsAvailable"] = 0
                variables["testReportsCompliant"] = 0
                variables["labReportStatus"] = 0
                variables["complianceStatus"] = 0
                variables["labResultsCompliant"] = 0
                variables["supervisionSchemeAccepted"] = 0
                variables["foreignDmark"] = 0
                variables["justificationReportApproved"] = 0
                variables["generateInspectionReport"] = 0

                bpmnCommonFunctions.startBpmnProcess(qaDmApplicationReviewProcessDefinitionKey, qaDmApplicationReviewBusinessKey, variables, assigneeId)?.let {
                    permit.dmAppReviewProcessInstanceId = it["processInstanceId"]
                    //permit.sfMarkInspectionTaskId = it["taskId"]
                    permit.dmAppReviewStartedOn = Timestamp.from(Instant.now())
                    permit.dmAppReviewStatus = 1
                    permitsRepo.save(permit)
                    KotlinLogging.logger { }.info("permitId : $permitId : Successfully started QA DM application review process for permit")
                    return it
                } ?: run {
                    KotlinLogging.logger { }.info("$permitId : Unable to start QA DM application review process")
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }

    //HOD check application complete
    fun qaDMHodReviewComplete(permitId: Long, applicationComplete: Boolean, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId : QA DM App Payment check application complete")
        updateTaskVariableByObjectIdAndKey(permitId, "QaDmApplicationReview", qaDmApplicationReviewBusinessKey,
            "applicationComplete", bpmnCommonFunctions.booleanToInt(applicationComplete).toString())
        qaCompleteTask(permitId, "QaDmApplicationReview", qaDmApplicationReviewBusinessKey)?.let {
            if (applicationComplete) {
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "QaDmAssignQAO", assigneeId)
            } else {
                val permit: PermitApplicationsEntity? = permitsRepo.findByIdOrNull(permitId)
                val manufacturerId = permit?.userId
                manufacturerId?.let { manufacturerId ->
                    qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "QaDmReviewManufacturerCorrection", manufacturerId)
                }
            }
        }
        return false
    }

    //Assign QAO complete
    fun qaDMAssignQAOComplete(permitId: Long, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Allocate QAO complete")
        //Update email
        userRepo.findByIdOrNull(assigneeId)?.let { usersEntity -> updateTaskVariableByObjectIdAndKey(permitId, "QaDmAssignQAO",
                qaDmApplicationReviewProcessDefinitionKey, "email", usersEntity.email.toString())
        }
        //Complete Task
        qaCompleteTask(permitId, "QaDmAssignQAO", qaDmApplicationReviewProcessDefinitionKey)?.let {
            qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "QaDmReviewGenerateJustificationReport", assigneeId)
        }
        return false
    }

    //Generate justification report Complete
    fun qaDMGenerateJustificationReportComplete(permitId: Long, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId : QA generate justification complete")
        qaCompleteTask(permitId, "QaDmReviewGenerateJustificationReport", qaDmApplicationReviewProcessDefinitionKey)?.let {
            qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "QaDmReviewApproveJustificationReport", assigneeId)
        }
        return false
    }

    //Approve Justification Report Complete
    fun qaDMApproveJustificationReportComplete(permitId: Long, reportApproved: Boolean, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Approve Justification Report complete")
        updateTaskVariableByObjectIdAndKey(permitId, "QaDmReviewApproveJustificationReport", qaDmApplicationReviewBusinessKey,
            "justificationReportApproved", bpmnCommonFunctions.booleanToInt(reportApproved).toString())
        //Complete Task
        qaCompleteTask(permitId, "QaDmReviewApproveJustificationReport", qaDmApplicationReviewProcessDefinitionKey)?.let {
//            val foreignDmark = bpmnCommonFunctions.getProcessVariables(it["processInstanceId"].toString())?.get("foreignDmark").toString()
            if (reportApproved) {
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "QaDmReviewAssignAssessor", assigneeId)
            } else {
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "QaDmReviewGenerateJustificationReport", assigneeId)
            }
        }
        return false
    }

    //Assign Assessor Complete
    fun qaDMAssignAssessorComplete(permitId: Long, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId : QA assign assessor complete")
        qaCompleteTask(permitId, "QaDmReviewAssignAssessor", qaDmApplicationReviewProcessDefinitionKey)?.let {
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "QaDmReviewScheduleInspectionDate", assigneeId)
        }
        return false
    }

    //Schedule Inspection Date Complete
    fun qaDMScheduleInspectionComplete(permitId: Long, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId : QA schedule inspection complete")
        qaCompleteTask(permitId, "QaDmReviewScheduleInspectionDate", qaDmApplicationReviewProcessDefinitionKey)?.let {
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "QaDmReviewGenerateInspectionReport", assigneeId)
        }
        return false
    }

    //Generate Inspection Report Complete
    fun qaDMGenerateInspectionComplete(permitId: Long, assigneeId: Long, hodAssigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId : QA generate inspection complete")
        qaCompleteTask(permitId, "QaDmReviewGenerateInspectionReport", qaDmApplicationReviewProcessDefinitionKey)?.let {
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "QaDmReviewGenerateSchemeOfSupervision", assigneeId)
        }
        return false
    }

    //Generate Scheme of Supervision complete
    fun qaDMGenerateSchemeofSupervisionComplete(permitId: Long, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId : QA generate scheme of supervision complete")
        qaCompleteTask(permitId, "QaDmReviewGenerateSchemeOfSupervision", qaDmApplicationReviewProcessDefinitionKey)?.let {
            val permit: PermitApplicationsEntity? = permitsRepo.findByIdOrNull(permitId)
            val manufacturerId = permit?.userId
            manufacturerId?.let { manufacturerId ->
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "QaDmReviewAcceptSchemeOfSupervision", manufacturerId)
            }
            qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "QaDmReviewFillSSFandBSNumber", assigneeId)
            return true
        }
        return false
    }

    // ------------------------------------------------------------- START PARALLEL GATEWAY ---------------------------------------------------------------------------------------------

    /*
    ---------------------- Start Scheme of supervision Route ------------------------------------
     */
    //Accept Scheme of Supervision complete
    fun qaDMAcceptSchemeofSupervisionComplete(permitId: Long, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId : QA accept scheme of supervision complete")
        qaCompleteTask(permitId, "QaDmReviewAcceptSchemeOfSupervision", qaDmApplicationReviewProcessDefinitionKey)?.let {
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "QaDmReviewApproveInspectionReport", assigneeId)
        }
        return false
    }
    /*
    ---------------------- End Scheme of supervision Route ------------------------------------
     */

    /*
    ---------------------- Start Lab Route ------------------------------------
     */
    //Fill SSF & BS No. complete
    fun qaDMFillSSFAndBsNumberComplete(permitId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId : QA fill ssf and bs number complete")
        qaCompleteTask(permitId, "QaDmReviewFillSSFandBSNumber", qaDmApplicationReviewProcessDefinitionKey)?.let {
            return true
        }
        return false
    }

    //Assign Compliance status Complete
    fun qaDMAssignComplianceStatusComplete(permitId: Long, labResultsCompliant: Boolean, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Assign Compliance Report complete")
        updateTaskVariableByObjectIdAndKey(permitId, "QaDmReviewAssignComplianceStatus", qaDmApplicationReviewBusinessKey,
            "labResultsCompliant", bpmnCommonFunctions.booleanToInt(labResultsCompliant).toString())
        //Complete Task
        qaCompleteTask(permitId, "QaDmReviewAssignComplianceStatus", qaDmApplicationReviewProcessDefinitionKey)?.let {
//            val foreignDmark = bpmnCommonFunctions.getProcessVariables(it["processInstanceId"].toString())?.get("foreignDmark").toString()
            if (labResultsCompliant) {
                return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "QaDmReviewAddRecommendations", assigneeId)
            } else {
                val permit: PermitApplicationsEntity? = permitsRepo.findByIdOrNull(permitId)
                val manufacturerId = permit?.userId
                manufacturerId?.let { manufacturerId ->
                    qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "QaDmReviewPerformCorrectiveAction", manufacturerId)
                }
            }
        }
        return false
    }

    //Manufacturer Perform corrective action complete
    fun qaDMPerformCorrectiveActionComplete(permitId: Long, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId : QA perform corrective action complete")
        qaCompleteTask(permitId, "QaDmReviewPerformCorrectiveAction", qaDmApplicationReviewProcessDefinitionKey)?.let {
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "QaDmReviewCorrectiveAction", assigneeId)
        }
        return false
    }

    //Review Corrective Action Complete
    fun qaDMReviewCorrectiveActionComplete(permitId: Long, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Review Corrective Action complete")
        //Complete Task
        qaCompleteTask(permitId, "QaDmReviewCorrectiveAction", qaDmApplicationReviewProcessDefinitionKey)?.let {
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "QaDmReviewFillSSFandBSNumber", assigneeId)
        }
        return false
    }

    //Assessor Add Recommendations complete
    fun qaDMAddRecommendationsComplete(permitId: Long, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId : QA add recommendations complete")
        qaCompleteTask(permitId, "QaDmReviewAddRecommendations", qaDmApplicationReviewProcessDefinitionKey)?.let {
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "QaDmReviewApproveInspectionReport", assigneeId)
        }
        return false
    }
    /*
    ---------------------- End Lab Route ------------------------------------
     */

    // ------------------------------------------------------------- END PARALLEL GATEWAY ---------------------------------------------------------------------------------------------


    //Approve inspection report
    fun qaDMApproveInspectionComplete(permitId: Long, inspectionReportApproved: Boolean, generateInspectionReport: Boolean, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Approve Inspection Report complete")
        updateTaskVariableByObjectIdAndKey(permitId, "QaDmReviewApproveInspectionReport", qaDmApplicationReviewBusinessKey,
            "inspectionReportApproved", bpmnCommonFunctions.booleanToInt(inspectionReportApproved).toString())
        updateTaskVariableByObjectIdAndKey(permitId, "QaDmReviewApproveInspectionReport", qaDmAppPaymentBusinessKey,
            "generateInspectionReport", bpmnCommonFunctions.booleanToInt(generateInspectionReport).toString())
        //Complete Task
        qaCompleteTask(permitId, "QaDmReviewApproveInspectionReport", qaDmApplicationReviewProcessDefinitionKey)?.let {
            if (inspectionReportApproved) {
                return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "QaDmReviewHodAddRecommendations", assigneeId)
            } else {
                if (generateInspectionReport) {
                    return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "QaDmReviewGenerateInspectionReport", assigneeId)
                } else {
                    return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "QaDmReviewAssessorPerformCorrectiveAction", assigneeId)
                }
            }
        }
        return false
    }

    //Manufacturer Perform corrective action complete
    fun qaDMAssessorPerformCorrectiveActionComplete(permitId: Long, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId : QA Assessor perform corrective action complete")
        qaCompleteTask(permitId, "QaDmReviewAssessorPerformCorrectiveAction", qaDmApplicationReviewProcessDefinitionKey)?.let {
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "QaDmReviewApproveInspectionReport", assigneeId)
        }
        return false
    }

    //End DM Application Process
    fun endDmReviewProcess(permitId: String) {
        KotlinLogging.logger { }.info("End DM Review process for permitId $permitId")
        try{
            permitsRepo.findByIdOrNull(permitId.toLong())?.let { permit ->
                permit.dmAppReviewCompletedOn = Timestamp.from(Instant.now())
                permit.dmAppReviewStatus = processCompleted
                permitsRepo.save(permit)
            }
        } catch (e: Exception){
            KotlinLogging.logger { }.error("$permitId : Unable to complete DM Review process for $permitId")
        }
    }

    /*
    ***********************************************************************************
    * OTHER METHODS
    ***********************************************************************************
    */
    //Schedules a reminder
    fun scheduleReminder(notificationId:Int,reminderDate: Date, assigneeId: Long){
        schedulerImpl.createScheduledAlert(notificationId,reminderDate,assigneeId)
    }

    fun sendManufacturerPaymentRequestRequest(permitId: String) {
        KotlinLogging.logger { }.info("Sending Manufacturer Payment Request for Permit $permitId............")
    }

    //Receive Payment
    fun diReceivePaymentComplete(permitId: Long) {
        KotlinLogging.logger { }.info("Trigger payment received task to complete")
        //Get the process instance ID
        var processInstanceId: String? = null

        permitsRepo.findByIdOrNull(permitId)?.let { permit ->//Check that the permit is valid
            KotlinLogging.logger { }.trace("PermitId : $permitId : Valid permit found")
            processInstanceId = permit.dmAppPaymentProcessInstanceId.toString()
        }
        KotlinLogging.logger { }.info ("Process Instance ID: $processInstanceId")
        processInstanceId?.let {
            try {
                val ex = runtimeService.createExecutionQuery().processInstanceId(it)
                    .activityId("QaDMAppReceivePayment")
                    .singleResult()
                KotlinLogging.logger { }.info("Execution ID: ${ex.id} & activityID: ${ex.activityId}")
                runtimeService.trigger(ex.getId())
            } catch (e: Exception){
                KotlinLogging.logger { }.error("An error occurred on Trigger Inspection schedule request task to complete ", e)
            }
        }
    }

    //Send Manufacturer Payment Confirmation
    fun sendManufacturerPaymentConfirmation(permitId: String) {
        KotlinLogging.logger { }.info("Sending Manufacturer Payment Confirmation for Permit $permitId............")
    }

    //Get all PCM tasks
    fun getallPCMTasks(): List<BpmnTaskDetails?> {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(pcmCandidateGroup).processDefinitionKey(qaApplicationAndPaymentProcessDefinitionKey).list()
        return bpmnCommonFunctions.generateTaskDetails(tasks)
    }

    //Claim foreign permit review task
    fun claimPermitApplicationCheckTask(permitId: Long, assigneeId: Long) {
        var processInstanceId: String? = null

        permitsRepo.findByIdOrNull(permitId)?.let { permit ->//Check that the permit is valid
            KotlinLogging.logger { }.trace("PermitId : $permitId : Valid permit found")
            processInstanceId = permit.dmAppPaymentProcessInstanceId.toString()
        }

        processInstanceId?.let { bpmnCommonFunctions.claimTask(it, "QaDMAppPCMCheckComplete", assigneeId) }
    }

    //Send inspection schedule to manufacturer
    fun sendManufacturerInspectionScheduleEmail(permitId: String) {
        KotlinLogging.logger { }.info("Sending Inspection Schedule to Manufacturer for Permit $permitId............")
    }

    //Send inspection report to manufacturer
    fun sendManufacturerInspectionReportEmail(permitId: String) {
        KotlinLogging.logger { }.info("Sending Inspection Report to Manufacturer for Permit $permitId............")
    }

    //Receive Lab Results
    fun diReceiveLabResultsComplete(permitId: Long) {
        KotlinLogging.logger { }.info("Trigger lab results received task to complete")
        //Get the process instance ID
        var processInstanceId: String? = null

        permitsRepo.findByIdOrNull(permitId)?.let { permit ->//Check that the permit is valid
            KotlinLogging.logger { }.trace("PermitId : $permitId : Valid permit found")
            processInstanceId = permit.dmAppReviewProcessInstanceId.toString()
        }
        KotlinLogging.logger { }.info ("Process Instance ID: $processInstanceId")
        processInstanceId?.let {
            try {
                val ex = runtimeService.createExecutionQuery().processInstanceId(it)
                    .activityId("QaDmReviewReceiveLabResults")
                    .singleResult()
                KotlinLogging.logger { }.info("Execution ID: ${ex.id} & activityID: ${ex.activityId}")
                runtimeService.trigger(ex.getId())
            } catch (e: Exception){
                KotlinLogging.logger { }.error("An error occurred on Trigger receive lab results task to complete ", e)
            }
        }
    }
}
