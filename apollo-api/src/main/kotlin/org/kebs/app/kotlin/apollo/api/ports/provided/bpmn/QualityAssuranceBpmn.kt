package org.kebs.app.kotlin.apollo.api.ports.provided.bpmn

import mu.KotlinLogging
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.task.api.Task
import org.joda.time.DateTime
import org.kebs.app.kotlin.apollo.api.ports.provided.scheduler.SchedulerImpl
import org.kebs.app.kotlin.apollo.store.model.PermitApplicationEntity
import org.kebs.app.kotlin.apollo.store.model.PetroleumInstallationInspectionEntity
import org.kebs.app.kotlin.apollo.store.model.qa.PermitApplicationsEntity
import org.kebs.app.kotlin.apollo.store.repo.*
import org.kebs.app.kotlin.apollo.store.repo.qa.IPermitApplicationsRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.Instant
import java.util.*
import kotlin.collections.HashMap


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
    private val schedulerImpl: SchedulerImpl
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

    @Value("\${bpmn.qa.dm.app.payment.process.definition.key}")
    lateinit var qaDmAppPaymentProcessDefinitionKey: String

    @Value("\${bpmn.qa.dm.app.payment.business.key}")
    lateinit var qaDmAppPaymentBusinessKey: String

    @Value("\${bpmn.notification.permit.expiry.id}")
    lateinit var permitExpiryNotificationId: String

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
                    if (process == qaDmAppPaymentProcessDefinitionKey) {
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

    fun qaAssignTask(permit: PermitApplicationsEntity?, processInstanceId: String, taskDefinitionKey: String?, assigneeId: Long, useManufacturer: Boolean): Boolean {
        KotlinLogging.logger { }.info("Assign next task begin")
        try {
            var localAssigneeId: String = assigneeId.toString()
            var task: Task? = null

            taskDefinitionKey?.let {
                task = bpmnCommonFunctions.getTaskByTaskDefinitionKey(processInstanceId, taskDefinitionKey)
            } ?: run {
                task = bpmnCommonFunctions.getTasks("processInstanceId", processInstanceId)?.get(0)
            }

            permit?.let {
                if (useManufacturer) {
                    permit.userId?.let { userId->
                        localAssigneeId = userId.toString()
                    }

                }
            }

            task?.let { task ->
                if (!useManufacturer) {
                    userRepo.findByIdOrNull(localAssigneeId.toLong())?.let { usersEntity ->
                        bpmnCommonFunctions.updateVariable(task.id, "email", usersEntity.email.toString())
                    }
                }
                //Refetch the task because we have updated the email variable
                bpmnCommonFunctions.getTaskById(task.id)?.let { updatedTask ->
                    updatedTask.assignee = localAssigneeId
                    updatedTask.dueDate = DateTime(Date()).plusDays(defaultDuration.toInt()).toDate()
                    taskService.saveTask(updatedTask)
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

    fun checkStartProcessInputs(objectId: Long, assigneeId: Long, useManufacturer: Boolean, processKey: String): HashMap<String, Any>? {
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
                    if (processKey == qaDmAppPaymentProcessDefinitionKey){
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
            if (!useManufacturer) {
                userRepo.findByIdOrNull(assigneeId)?.let { usersEntity ->
                    variables["assigneeEmail"] = usersEntity.email.toString()
                } ?: run { KotlinLogging.logger { }.info("objectId : $objectId : No user found for id $assigneeId"); return null }
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
                taskList.add(TaskDetails(task.processVariables["permitId"].toString().toLong(), 0,task))
            } catch (e:Exception){
                taskList.add(TaskDetails(0,task.processVariables["objectId"].toString().toLong(), task))
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
            checkStartProcessInputs(permitId, assigneeId, false, qaAppReviewProcessDefinitionKey)?.let { checkVariables ->
                val permit: PermitApplicationsEntity = checkVariables["permit"] as PermitApplicationsEntity
                variables["permitId"] = permit.id.toString()
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
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaAllocateFileToQAO", currAssigneeId, false)
            } else {
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaCorrectApplication", currAssigneeId, true)
            }
        }
        return false
    }

    fun qaAppReviewAllocateToQAO(permitId: Long, qaoAssigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Allocate to QAO begin")
        qaCompleteTask(permitId, "qaAllocateFileToQAO", qaAppReviewProcessDefinitionKey)?.let {
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaPlanFactoryVisit", qaoAssigneeId, false)
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

    fun qaAppReviewCorrectApplication(permitId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Correct application begin")
        var currAssigneeId: Long = 0
        fetchTaskByPermitId(permitId, qaAppReviewProcessDefinitionKey)?.let { taskDetails ->
            currAssigneeId = taskDetails[0].task.assignee.toLong()
        }
        qaCompleteTask(permitId, "qaCorrectApplication", qaAppReviewProcessDefinitionKey)?.let {
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaResubmitApplication", currAssigneeId, true)
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
            checkStartProcessInputs(permitId, assigneeId, false, qaSfMarkInspectionProcessDefinitionKey)?.let { checkVariables ->
                val permit: PermitApplicationsEntity = checkVariables["permit"] as PermitApplicationsEntity
                variables["permitId"] = permit.id.toString()
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

    fun qaSfMIGenSupervisionSchemeComplete(permitId: Long, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Generate supervision scheme")
        qaCompleteTask(permitId, "qaSfmiGenSupervisionScheme", qaSfMarkInspectionProcessDefinitionKey)?.let {
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaSfmiAcceptSupervisionScheme", assigneeId, true)
        }
        return false
    }

    fun qaSfMIAcceptSupervisionScheme(permitId: Long, assigneeId: Long, supervisionSchemeAccepted: Boolean): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Accept supervision scheme")
        updateTaskVariableByObjectIdAndKey(permitId, "qaSfmiAcceptSupervisionScheme", qaSfMarkInspectionProcessDefinitionKey, "supervisionSchemeAccepted", bpmnCommonFunctions.booleanToInt(supervisionSchemeAccepted).toString())
        qaCompleteTask(permitId, "qaSfmiAcceptSupervisionScheme", qaSfMarkInspectionProcessDefinitionKey)?.let {
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), null, assigneeId, false)
        }
        return false
    }

    fun qaSfMIFillFactoryInspectionForms(permitId: Long, qaoAssigneeId: Long, labAssigneeId: Long): Boolean {
        var result = false
        KotlinLogging.logger { }.info("PermitId : $permitId :  Fill factory inspection forms")
        qaCompleteTask(permitId, "qaSfmiFillFactoryInspectionForms", qaSfMarkInspectionProcessDefinitionKey)?.let {
            //We then have 2 assignments to do - Generate inspection report and lab
            qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaSfmiGenerateInspectionReport", qaoAssigneeId, false).let { assignStatus ->
                result = assignStatus
            }
            qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaSfmiReceiveSSF", labAssigneeId, false).let { assignStatus ->
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

    fun qaSfMIReceiveSSFComplete(permitId: Long, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Receive the SSF")
        qaCompleteTask(permitId, "qaSfmiReceiveSSF", qaSfMarkInspectionProcessDefinitionKey)?.let {
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaSfmiSubmitSamples", assigneeId, false)
        }
        return false
    }

    fun qaSfMISubmitSamplesComplete(permitId: Long, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Submit the samples")
        qaCompleteTask(permitId, "qaSfmiSubmitSamples", qaSfMarkInspectionProcessDefinitionKey)?.let {
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaSfmiAnalyzeSamples", assigneeId, false)
        }
        return false
    }

    fun qaSfMIAnalyzeSamplesComplete(permitId: Long, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Analyze the samples")
        qaCompleteTask(permitId, "qaSfmiAnalyzeSamples", qaSfMarkInspectionProcessDefinitionKey)?.let {
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaSfmiCheckLabReport", assigneeId, false)
        }
        return false
    }

    fun qaSfMICheckLabReportComplete(permitId: Long, qaoAssigneeId: Long, labAssigneeId: Long, labReportStatus: Boolean): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Check the lab report")
        updateTaskVariableByObjectIdAndKey(permitId, "qaSfmiCheckLabReport", qaSfMarkInspectionProcessDefinitionKey, "labReportStatus", bpmnCommonFunctions.booleanToInt(labReportStatus).toString())
        qaCompleteTask(permitId, "qaSfmiCheckLabReport", qaSfMarkInspectionProcessDefinitionKey)?.let {
            return if (labReportStatus) {   //lab report ok
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaSfmiAssignComplianceStatus", qaoAssigneeId, false)
            } else {
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaSfmiAnalyzeSamples", labAssigneeId, false)
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
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaSfmiApprovePermitRecommendation", hofAssigneeId, false)
            } else {
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaSfmiPerformCorrectiveAction", hofAssigneeId, true)
            }
        }
        return false
    }

    fun qaSfMIPerformCorrectiveActionComplete(permitId: Long, qaoAssigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Perform Corrective action complete")
        qaCompleteTask(permitId, "qaSfmiPerformCorrectiveAction", qaSfMarkInspectionProcessDefinitionKey)?.let {
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaSfmiFillFactoryInspectionForms", qaoAssigneeId, false)
        }
        return false
    }

    fun qaSfMIApprovePermitRecommendationComplete(permitId: Long, qaoAssigneeId: Long, permitRecommendationApproval: Boolean): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Approve the permit recommendation")
        qaCompleteTask(permitId, "qaSfmiApprovePermitRecommendation", qaSfMarkInspectionProcessDefinitionKey)?.let {
            return if (permitRecommendationApproval) {   //permit approval ok
                true
            } else {
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaSfmiPermitSurveillance", qaoAssigneeId, false)
            }
        }
        return false
    }

    fun qaSfMICheckIfPermitSurveillanceComplete(permitId: Long, qaoAssigneeId: Long, permitSurveillance: Boolean): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Check if permit surveillance will be done")
        qaCompleteTask(permitId, "qaSfmiPermitSurveillance", qaSfMarkInspectionProcessDefinitionKey)?.let {
            return if (permitSurveillance) {   //There will be factory surveillance
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaSfmiGenerateReportDuePermits", qaoAssigneeId, false)
            } else {
                true
            }
        }
        return false
    }

    fun qaSfMIFactorySurveillancePlanComplete(permitId: Long, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Factory Surveillance Generate Report of Due Permits and Workplan")
        qaCompleteTask(permitId, "qaSfmiGenerateReportDuePermits", qaSfMarkInspectionProcessDefinitionKey)?.let {
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaSfmiGenSupervisionScheme", assigneeId, false)
        }
        return false
    }

    /*
    ***********************************************************************************
    * DM APPLICATION REVIEW
    ***********************************************************************************
     */
    //Start the DM Application Review process
    fun startQADMApplicationReviewProcess(permitId: Long, assigneeId: Long): HashMap<String, String>? {
        var variables: HashMap<String, Any> = HashMap()
        KotlinLogging.logger { }.info("permitId : $permitId : Starting QA DM application review process")
        try {
            //Remember to start by setting email and assignee to manufacturer
            checkStartProcessInputs(permitId, assigneeId, false, qaDmApplicationReviewProcessDefinitionKey)?.let { checkVariables ->
                val permit: PermitApplicationsEntity = checkVariables["permit"] as PermitApplicationsEntity
                variables["permitId"] = permit.id.toString()
                variables["email"] = checkVariables["assigneeEmail"].toString()
                variables["manufacturerEmail"] = checkVariables["manufacturerEmail"].toString()
                variables["applicationComplete"] = 0
                variables["inspectionReportApproved"] = 0
                variables["testReportsAvailable"] = 0
                variables["testReportsCompliant"] = 0
                variables["labReportStatus"] = 0
                variables["complianceStatus"] = 0

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

    fun qaDmARCheckApplicationComplete(permitId: Long, hofAssigneeId: Long, applicationComplete: Boolean): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Check if application is complete")
        updateTaskVariableByObjectIdAndKey(permitId, "qaDmarCheckApplication", qaDmApplicationReviewProcessDefinitionKey, "applicationComplete", bpmnCommonFunctions.booleanToInt(applicationComplete).toString())
        qaCompleteTask(permitId, "qaDmarCheckApplication", qaDmApplicationReviewProcessDefinitionKey)?.let {
            return if (applicationComplete) {   //application is complete
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmarAllocatetoQAO", hofAssigneeId, false)
            } else {
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaSfmiAnalyzeSamples", hofAssigneeId, true)
            }
        }
        return false
    }

    fun qaDmARManufacturerCorrectionComplete(permitId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  manufacturer correction complete")
        qaCompleteTask(permitId, "qaDmarManufacturerCorrection", qaDmApplicationReviewProcessDefinitionKey)?.let {
            return true //process ends here
        }
        return false
    }

    fun qaDmARAllocateToQAOComplete(permitId: Long, qaoAssigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Allocate to QAO complete")
        //Update email
        userRepo.findByIdOrNull(qaoAssigneeId)?.let { usersEntity ->
            updateTaskVariableByObjectIdAndKey(permitId, "qaDmarAllocatetoQAO", qaDmApplicationReviewProcessDefinitionKey, "email", usersEntity.email.toString())
        }
        qaCompleteTask(permitId, "qaDmarAllocatetoQAO", qaDmApplicationReviewProcessDefinitionKey)?.let {
            qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmarFactoryInspection", qaoAssigneeId, false)
        }
        return false
    }

    fun qaDmARFactoryInspectionComplete(permitId: Long, qaoAssigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Factory inspection complete")
        qaCompleteTask(permitId, "qaDmarFactoryInspection", qaDmApplicationReviewProcessDefinitionKey)?.let {
            qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmarGenerateInspectionReport", qaoAssigneeId, false)
        }
        return false
    }

    fun qaDmARGenerateInspectionReportComplete(permitId: Long, hofAssigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Generate inspection report complete")
        //Update email
        userRepo.findByIdOrNull(hofAssigneeId)?.let { usersEntity ->
            updateTaskVariableByObjectIdAndKey(permitId, "qaDmarGenerateInspectionReport", qaDmApplicationReviewProcessDefinitionKey, "email", usersEntity.email.toString())
        }
        qaCompleteTask(permitId, "qaDmarGenerateInspectionReport", qaDmApplicationReviewProcessDefinitionKey)?.let {
            qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmarApproveInspectionReport", hofAssigneeId, false)
        }
        return false
    }

    fun qaDmARApproveInspectionReportComplete(permitId: Long, qaoAssigneeId: Long, inspectionReportApproved: Boolean): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Approve the inspection report complete")
        updateTaskVariableByObjectIdAndKey(permitId, "qaDmarApproveInspectionReport", qaDmApplicationReviewProcessDefinitionKey, "inspectionReportApproved", bpmnCommonFunctions.booleanToInt(inspectionReportApproved).toString())
        qaCompleteTask(permitId, "qaDmarApproveInspectionReport", qaDmApplicationReviewProcessDefinitionKey)?.let {
            return if (inspectionReportApproved) {   //inspection report approved
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmarTestReportsAvailable", qaoAssigneeId, false)
            } else {
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmarGenerateInspectionReport", qaoAssigneeId, false)
            }
        }
        return false
    }

    fun qaDmARCheckTestReportsComplete(permitId: Long, qaoAssigneeId: Long, testReportsAvailable: Boolean): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Check if test reports available complete")
        updateTaskVariableByObjectIdAndKey(permitId, "qaDmarTestReportsAvailable", qaDmApplicationReviewProcessDefinitionKey, "testReportsAvailable", bpmnCommonFunctions.booleanToInt(testReportsAvailable).toString())
        qaCompleteTask(permitId, "qaDmarTestReportsAvailable", qaDmApplicationReviewProcessDefinitionKey)?.let {
            return if (testReportsAvailable) {   //test reports available
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmarTestReportsCompliant", qaoAssigneeId, false)
            } else {
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmarFillSSF", qaoAssigneeId, false)
            }
        }
        return false
    }

    fun qaDmARTestReportsCompliantComplete(permitId: Long, qaoAssigneeId: Long, testReportsCompliant: Boolean): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Check if test reports are compliant complete")
        updateTaskVariableByObjectIdAndKey(permitId, "qaDmarTestReportsCompliant", qaDmApplicationReviewProcessDefinitionKey, "testReportsCompliant", bpmnCommonFunctions.booleanToInt(testReportsCompliant).toString())
        qaCompleteTask(permitId, "qaDmarTestReportsCompliant", qaDmApplicationReviewProcessDefinitionKey)?.let {
            return if (testReportsCompliant) {   //test reports are compliant
                //update the task completion time and status
                permitRepo.findByIdOrNull(permitId)?.let { permit ->//Check that the permit is valid
                    permit.dmAppReviewStatus = 2
                    permit.dmAppReviewCompletedOn = Timestamp.from(Instant.now())
                    permitRepo.save(permit)
                }
                true
            } else {
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmarFillSSF", qaoAssigneeId, false)
            }
        }
        return false
    }

    fun qaDmARFillSSFComplete(permitId: Long, labAssigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Fill the SSF complete")
        qaCompleteTask(permitId, "qaDmarFillSSF", qaDmApplicationReviewProcessDefinitionKey)?.let {
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmarReceiveSSF", labAssigneeId, false)
        }
        return false
    }

    fun qaDmARReceiveSSFComplete(permitId: Long, qaoAssigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Receive the SSF complete")
        qaCompleteTask(permitId, "qaDmarReceiveSSF", qaDmApplicationReviewProcessDefinitionKey)?.let {
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmarSubmitSamples", qaoAssigneeId, false)
        }
        return false
    }

    fun qaDmARSubmitSamplesComplete(permitId: Long, labAssigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Submit the samples complete")
        qaCompleteTask(permitId, "qaDmarSubmitSamples", qaDmApplicationReviewProcessDefinitionKey)?.let {
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmarAnalyzeSamples", labAssigneeId, false)
        }
        return false
    }

    fun qaDmARLabAnalysisComplete(permitId: Long, qaoAssigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Lab analysis complete")
        userRepo.findByIdOrNull(qaoAssigneeId)?.let { usersEntity ->
            updateTaskVariableByObjectIdAndKey(permitId, "qaDmarAnalyzeSamples", qaDmApplicationReviewProcessDefinitionKey, "email", usersEntity.email.toString())
        }
        qaCompleteTask(permitId, "qaDmarAnalyzeSamples", qaDmApplicationReviewProcessDefinitionKey)?.let {
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmarLabReportCorrect", qaoAssigneeId, false)
        }
        return false
    }

    fun qaDmARCheckLabReportsComplete(permitId: Long, qaoAssigneeId: Long, labAssigneeId: Long, labReportStatus: Boolean): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Check if lab report correct complete")
        updateTaskVariableByObjectIdAndKey(permitId, "qaDmarLabReportCorrect", qaDmApplicationReviewProcessDefinitionKey, "labReportStatus", bpmnCommonFunctions.booleanToInt(labReportStatus).toString())
        qaCompleteTask(permitId, "qaDmarLabReportCorrect", qaDmApplicationReviewProcessDefinitionKey)?.let {
            return if (labReportStatus) {   //test reports available
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmarAssignComplianceStatus", qaoAssigneeId, false)
            } else {
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmarAnalyzeSamples", labAssigneeId, false)
            }
        }
        return false
    }

    fun qaDmARAssignComplianceStatusComplete(permitId: Long, qaoAssigneeId: Long, complianceStatus: Boolean): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Assign the compliance status complete")
        updateTaskVariableByObjectIdAndKey(permitId, "qaDmarAssignComplianceStatus", qaDmApplicationReviewProcessDefinitionKey, "complianceStatus", bpmnCommonFunctions.booleanToInt(complianceStatus).toString())
        fetchTaskByPermitId(permitId, qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
            bpmnCommonFunctions.getTaskVariables(taskDetails[0].task.id)?.let {
                updateTaskVariableByObjectIdAndKey(permitId, "qaDmarAssignComplianceStatus", qaDmApplicationReviewProcessDefinitionKey, "email", it["manufacturerEmail"].toString())
            }
        }
        qaCompleteTask(permitId, "qaDmarAssignComplianceStatus", qaDmApplicationReviewProcessDefinitionKey)?.let {
            return if (complianceStatus) {   //compliance Status ok
                //update the task completion time and status
                permitRepo.findByIdOrNull(permitId)?.let { permit ->//Check that the permit is valid
                    permit.dmAppReviewStatus = 2
                    permit.dmAppReviewCompletedOn = Timestamp.from(Instant.now())
                    permitRepo.save(permit)
                }
                true //process ends here
            } else {
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaoAssigneeId", qaoAssigneeId, false)
            }
        }
        return false
    }

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
            checkStartProcessInputs(permitId, assigneeId, true, qaSfApplicationPaymentProcessDefinitionKey)?.let { checkVariables ->
                val permit: PermitApplicationsEntity = checkVariables["permit"] as PermitApplicationsEntity
                variables["permitId"] = permit.id.toString()
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
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), null, 0, true)
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
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaSfapReviewApplication", hofAssigneeId, false)
        }
        return false
    }

    fun qaSfAPReviewApplicationComplete(permitId: Long, approvalStatus: Boolean): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  Review application complete")
        updateTaskVariableByObjectIdAndKey(permitId, "qaSfapReviewApplication", qaSfApplicationPaymentProcessDefinitionKey, "approvalStatus", bpmnCommonFunctions.booleanToInt(approvalStatus).toString())
        qaCompleteTask(permitId, "qaSfapReviewApplication", qaSfApplicationPaymentProcessDefinitionKey)?.let {
            return if (approvalStatus) {
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaSfapMakePayment", 0, true)
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
            checkStartProcessInputs(permitId, assigneeId, false, qaSfPermitAwardProcessDefinitionKey)?.let { checkVariables ->
                val permit: PermitApplicationsEntity = checkVariables["permit"] as PermitApplicationsEntity
                variables["permitId"] = permit.id.toString()
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
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaSfpaPCMPermitAward", pcmAssigneeId, false)
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
            checkStartProcessInputs(objectId, assigneeId, false, qaIiScheduleProcessDefinitionKey)?.let { checkVariables ->
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
            return qaAssignTask(null, it["processInstanceId"].toString(), "qaIisPMReview", pmAssigneeId, false)
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
            return qaAssignTask(null, it["processInstanceId"].toString(), "qaIisAllocateInspectionOfficer", currAssigneeId, false)
        }
        return false
    }

    fun qaIISAllocateIOComplete(objectId: Long, qaoAssigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Inspection officer assign complete")
        qaCompleteTask(objectId, "qaIisAllocateInspectionOfficer", qaIiScheduleProcessDefinitionKey)?.let {
            return qaAssignTask(null, it["processInstanceId"].toString(), "qaIisGenerateProformaInvoice", qaoAssigneeId, false)
        }
        return false
    }

    fun qaIISGenerateProformaInvComplete(objectId: Long,qaoAssigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Generate and send proforma invoice complete")
        qaCompleteTask(objectId, "qaIisGenerateProformaInvoice", qaIiScheduleProcessDefinitionKey)?.let {
            return qaAssignTask(null, it["processInstanceId"].toString(), "qaIisCustomerPayment", qaoAssigneeId, false)
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
            return qaAssignTask(null, it["processInstanceId"].toString(), "qaIisSchedCalendarVisit", qaoAssigneeId, false)
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
            checkStartProcessInputs(objectId, assigneeId, false, qaIiReportingProcessDefinitionKey)?.let { checkVariables ->
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
            return qaAssignTask(null, it["processInstanceId"].toString(), "qaIirHofApplicationReview", hofAssigneeId, false)
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
                qaAssignTask(null, it["processInstanceId"].toString(), "qaIirInspection", qaoAssigneeId, false)
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
            checkStartProcessInputs(permitId, assigneeId, false, qaDmAssessmentProcessDefinitionKey)?.let { checkVariables ->
                val permit: PermitApplicationsEntity = checkVariables["permit"] as PermitApplicationsEntity
                variables["permitId"] = permit.id.toString()
                variables["email"] = checkVariables["assigneeEmail"].toString()
                variables["manufacturerEmail"] = checkVariables["manufacturerEmail"].toString()
                variables["assessorEmail"] = ""
                variables["qaoEmail"] = ""
                variables["justificationReportApproved"] = 0
                variables["assessmentReportApproved"] = 0
                variables["reportCompliant"] = 0
                variables["pacApproval"] = 0

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
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmasApproveJustificationRpt", hodAssigneeId, false)
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
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmasAppointAssessor", currAssigneeId, false)
            } else {
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmasGenJustificationRpt", qaoAssigneeId, false)
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
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmasScheduleFactoryVisit", assessorAssigneeId, false)
        }
        return false
    }

    fun qaScheduleFactoryVisitComplete(permitId: Long, assessorAssigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId : QA DM Appoint Assessor complete")
        qaCompleteTask(permitId, "qaDmasScheduleFactoryVisit", qaDmAssessmentProcessDefinitionKey)?.let {
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmasGenAssessmentRpt", assessorAssigneeId, false)
        }
        return false
    }

    fun qaDmasGenerateAssessmentRptComplete(permitId: Long, hodAssigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId : QA DM Generate assessment report complete")
        qaCompleteTask(permitId, "qaDmasGenAssessmentRpt", qaDmAssessmentProcessDefinitionKey)?.let {
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmasApproveAssessmentReport", hodAssigneeId, false)
        }
        return false
    }

    fun qaDmasApproveAssessmentRptComplete(permitId: Long, assessorAssigneeId: Long, assessmentReportApproved: Boolean): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  QA DM Assessment report approval complete")
        var currAssigneeId: Long = 0
        fetchTaskByPermitId(permitId, qaDmAssessmentProcessDefinitionKey)?.let { taskDetails ->
            currAssigneeId = taskDetails[0].task.assignee.toLong()
        }
        /*
        if (!assessmentReportApproved){
            userRepo.findByIdOrNull(assessorAssigneeId)?.let { usersEntity ->
                updateTaskVariableByPermitIdAndKey(permitId,"qaDmasApproveAssessmentReport",qaSfPermitAwardProcessDefinitionKey,"email",usersEntity.email.toString())
            }
        }
         */
        updateTaskVariableByObjectIdAndKey(permitId, "qaDmasApproveAssessmentReport", qaDmAssessmentProcessDefinitionKey, "assessmentReportApproved", bpmnCommonFunctions.booleanToInt(assessmentReportApproved).toString())

        qaCompleteTask(permitId, "qaDmasApproveAssessmentReport", qaDmAssessmentProcessDefinitionKey)?.let {
            return if (assessmentReportApproved) {
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmasCheckReportCompliant", currAssigneeId, false)
            } else {
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmasGenAssessmentRpt", assessorAssigneeId, false)
            }
        }
        return false
    }

    fun qaDmasAssessmentRptCompliantComplete(permitId: Long, pacAssigneeId: Long, reportCompliant: Boolean): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  QA DM Assessment report compliant complete")
        updateTaskVariableByObjectIdAndKey(permitId, "qaDmasCheckReportCompliant", qaDmAssessmentProcessDefinitionKey, "reportCompliant", bpmnCommonFunctions.booleanToInt(reportCompliant).toString())
        qaCompleteTask(permitId, "qaDmasCheckReportCompliant", qaDmAssessmentProcessDefinitionKey)?.let {
            return if (reportCompliant) {
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmasPacApproval", pacAssigneeId, false)
            } else {
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmasCorrectiveAction", 0, true)
            }
        }
        return false
    }

    fun qaDmasCorrectiveActionComplete(permitId: Long, assessorAssigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId : QA DM Corrective action complete")
        qaCompleteTask(permitId, "qaDmasCorrectiveAction", qaDmAssessmentProcessDefinitionKey)?.let {
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmasScheduleFactoryVisit", assessorAssigneeId, false)
        }
        return false
    }

    fun qaDmasPacApprovalComplete(permitId: Long, assessorAssigneeId: Long, qaoAssigneeId: Long, pacApproval: Boolean): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId :  QA DM Assessment PAC Approval complete")
        if (!pacApproval) {
            userRepo.findByIdOrNull(assessorAssigneeId)?.let { usersEntity ->
                updateTaskVariableByObjectIdAndKey(permitId, "qaDmasPacApproval", qaDmAssessmentProcessDefinitionKey, "assessorEmail", usersEntity.email.toString())
            }
            userRepo.findByIdOrNull(qaoAssigneeId)?.let { usersEntity ->
                updateTaskVariableByObjectIdAndKey(permitId, "qaDmasPacApproval", qaDmAssessmentProcessDefinitionKey, "qaoEmail", usersEntity.email.toString())
            }
        }
        updateTaskVariableByObjectIdAndKey(permitId, "qaDmasPacApproval", qaDmAssessmentProcessDefinitionKey, "pacApproval", bpmnCommonFunctions.booleanToInt(pacApproval).toString())

        qaCompleteTask(permitId, "qaDmasPacApproval", qaDmAssessmentProcessDefinitionKey)?.let {
            return if (pacApproval) {
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmasSurveillanceWorkplan", 0, true)
            } else {
                return true
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
    //Start the DM App Payment process
    fun startQADmAppPaymentProcess(permitId: Long, assigneeId: Long): HashMap<String, String>? {
        var variables: HashMap<String, Any> = HashMap()
        KotlinLogging.logger { }.info("permitId : $permitId : Starting QA DM app payment process")
        try {
            checkStartProcessInputs(permitId, assigneeId, false, qaDmAppPaymentProcessDefinitionKey)?.let { checkVariables ->
                val permit: PermitApplicationsEntity = checkVariables["permit"] as PermitApplicationsEntity
                variables["permitId"] = permit.id.toString()
                variables["email"] = checkVariables["assigneeEmail"].toString()
                variables["manufacturerEmail"] = checkVariables["manufacturerEmail"].toString()
                variables["isRenewal"] = 0
                variables["isDomestic"] = 0
                variables["foreignApplicationComplete"] = 0
                variables["domesticApplicationComplete"] = 0
                variables["pcmId"] = 0
                //In this case the first assignee is the manufacturer
                permit.userId?.let { manufacturerId ->
                    bpmnCommonFunctions.startBpmnProcess(qaDmAppPaymentProcessDefinitionKey, qaDmAppPaymentBusinessKey, variables, manufacturerId)?.let {
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

    fun qaDmappSelectDmarkComplete(permitId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId : QA DM App Payment select dmark complete")
        qaCompleteTask(permitId, "qaDmappSelectDmark", qaDmAppPaymentProcessDefinitionKey)?.let {
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), null, 0, true)
        }
        return false
    }

    fun qaDmappCheckIfRenewal(permitId: String): String {
        KotlinLogging.logger { }.info("PermitId : $permitId : QA DM App Payment check if renewal")
        return "0"
    }

    fun qaDmappCheckDomesticComplete(permitId: Long, isDomestic: Boolean): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId : QA DM App Payment check domestic complete")
        updateTaskVariableByObjectIdAndKey(permitId, "qaDmappCheckDomestic", qaDmAppPaymentProcessDefinitionKey, "isDomestic", bpmnCommonFunctions.booleanToInt(isDomestic).toString())
        qaCompleteTask(permitId, "qaDmappCheckDomestic", qaDmAppPaymentProcessDefinitionKey)?.let {
            return if (isDomestic) {
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmappFillDomesticApplication", 0, true)
            } else {
                qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmappFillForeignApplication", 0, true)
            }
        }
        return false
    }

    fun qaDmappFillForeignAppComplete(permitId: Long, pcmAssigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId : QA DM App Payment fill foreign application complete")
        qaCompleteTask(permitId, "qaDmappFillForeignApplication", qaDmAppPaymentProcessDefinitionKey)?.let {
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmappPCMCheckApplicationCompleForeign", pcmAssigneeId, false)
        }
        return false
    }

    fun qaDmappCheckAppCompleteForeign(permitId: Long, foreignApplicationComplete: Boolean): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId : QA DM App Payment check if foreign application complete")
        updateTaskVariableByObjectIdAndKey(permitId, "qaDmappPCMCheckApplicationCompleForeign", qaDmAppPaymentProcessDefinitionKey, "foreignApplicationComplete", bpmnCommonFunctions.booleanToInt(foreignApplicationComplete).toString())
        qaCompleteTask(permitId, "qaDmappPCMCheckApplicationCompleForeign", qaDmAppPaymentProcessDefinitionKey)?.let {
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), null, 0, true)
        }
        return false
    }

    fun qaDmappManufacturerCorrectionCompleteForeign(permitId: Long, pcmAssigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId : QA DM App Payment manufacturer correction complete")
        qaCompleteTask(permitId, "qaDmappManufacturerCorrectionForeign", qaDmAppPaymentProcessDefinitionKey)?.let {
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), null, pcmAssigneeId, false)
        }
        return false
    }

    fun qaDmappFillDomesticAppComplete(permitId: Long, pcmAssigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId : QA DM App Payment fill domestic application complete")
        qaCompleteTask(permitId, "qaDmappFillDomesticApplication", qaDmAppPaymentProcessDefinitionKey)?.let {
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), "qaDmappPCMCheckApplicationCompleDomestic", pcmAssigneeId, false)
        }
        return false
    }

    fun qaDmappCheckAppCompleteDomestic(permitId: Long, domesticApplicationComplete: Boolean): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId : QA DM App Payment check if domestic application complete")
        updateTaskVariableByObjectIdAndKey(permitId, "qaDmappPCMCheckApplicationCompleDomestic", qaDmAppPaymentProcessDefinitionKey, "domesticApplicationComplete", bpmnCommonFunctions.booleanToInt(domesticApplicationComplete).toString())
        qaCompleteTask(permitId, "qaDmappPCMCheckApplicationCompleDomestic", qaDmAppPaymentProcessDefinitionKey)?.let {
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), null, 0, true)
        }
        return false
    }

    fun qaDmappManufacturerCorrectionCompleteDomestic(permitId: Long, pcmAssigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId : QA DM App Payment manufacturer correction domestic")
        qaCompleteTask(permitId, "qaDmappManufacturerCorrectionDomestic", qaDmAppPaymentProcessDefinitionKey)?.let {
            return qaAssignTask(it["permit"] as PermitApplicationsEntity, it["processInstanceId"].toString(), null, pcmAssigneeId, false)
        }
        return false
    }

    fun qaDmappPaymentComplete(permitId: Long): Boolean {
        KotlinLogging.logger { }.info("PermitId : $permitId : QA DM App Payment complete")
        qaCompleteTask(permitId, "qaDmappPayment", qaDmAppPaymentProcessDefinitionKey)?.let {
            return true
        }
        return false
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
}
