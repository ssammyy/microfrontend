package org.kebs.app.kotlin.apollo.api.ports.provided.bpmn

import mu.KotlinLogging
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.di.BpmnTaskDetails
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.di.DiTaskDetails
import org.kebs.app.kotlin.apollo.api.ports.provided.scheduler.SchedulerImpl
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocApplicationEntity
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocComplaintEntity
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocTimelinesDataEntity
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocWaiversApplicationEntity
import org.kebs.app.kotlin.apollo.store.repo.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.Instant
import java.util.ArrayList
import kotlin.collections.HashMap

@Service
class PvocBpmn(
        private val taskService: TaskService,
        private val runtimeService: RuntimeService,
        private val userRepo: IUserRepository,
        private val pvocApplicationRepo: IPvocApplicationRepo,
        private val pvocTimelinesDataRepo: IPvocTimelinesDataRepository,
        private val schedulerRepo: ISchedulerRepository,
        private val schedulerImpl: SchedulerImpl,
        private val bpmnCommonFunctions: BpmnCommonFunctions,
        private val notifications: Notifications,
        private val iwaiversApplicationRepo: IwaiversApplicationRepo
) {
    @Value("\${bpmn.pvoc.ea.process.definition.key}")
    lateinit var pvocEaProcessDefinitionKey: String

    @Value("\${bpmn.pvoc.ea.business.key}")
    lateinit var pvocEaBusinessKey: String

    @Value("\${bpmn.pvoc.wa.process.definition.key}")
    lateinit var pvocWaProcessDefinitionKey: String

    @Value("\${bpmn.pvoc.wa.business.key}")
    lateinit var pvocWaBusinessKey: String

    @Value("\${bpmn.pvoc.monit.process.definition.key}")
    lateinit var pvocMoProcessDefinitionKey: String

    @Value("\${bpmn.pvoc.monit.business.key}")
    lateinit var pvocMoBusinessKey: String

    @Value("\${bpmn.task.default.duration}")
    lateinit var defaultDuration: Integer

    val successMessage: String = "success"
    val processStarted: Int = 1
    val processCompleted: Int = 2
    val pidPrefix = "pvoc"

    fun fetchTaskByObjectId(objectId: Long, process: String): List<BpmnTaskDetails>? {
        try {
            getPIdByObjectAndProcess(objectId, process)?.let {
                val processInstanceId = it["processInstanceId"].toString()
                bpmnCommonFunctions.getTasks("processInstanceId", processInstanceId)?.let { tasks ->
                    return bpmnCommonFunctions.generateTaskDetails(tasks)
                } ?: run { KotlinLogging.logger { }.info("ObjectId : $objectId : No task found");return null }
            }
            KotlinLogging.logger { }.info("ObjectId : $objectId : No complaint found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }

    //Get the processInstanceId from the object and process
    fun getPIdByObjectAndProcess(objectId: Long, process: String): HashMap<String, Any>? {
        val variables: HashMap<String, Any> = HashMap()
        try {
            var processInstanceId = ""
            if (process == pvocEaProcessDefinitionKey) {
                pvocApplicationRepo.findByIdOrNull(objectId)?.let { pvocApplicationEntity ->
                    KotlinLogging.logger { }.trace("CD : $objectId : Valid Pvoc Application found")
                    when (process) {
                        pvocEaProcessDefinitionKey -> {
                            processInstanceId = pvocApplicationEntity.pvocEaProcessInstanceId.toString()
                        }
                        pvocWaProcessDefinitionKey -> {
                            processInstanceId = pvocApplicationEntity.pvocWaProcessInstanceId.toString()
                        }
                        else -> {
                            //do nothing
                        }
                    }
                    variables["processInstanceId"] = processInstanceId
                    variables["pvocApplication"] = pvocApplicationEntity
                    return variables

                } ?: KotlinLogging.logger { }.info("CD : $objectId : No Pvoc Timeline found")
            } else if (process == pvocWaProcessDefinitionKey) {
                iwaiversApplicationRepo.findByIdOrNull(objectId)?.let { pvocWaiversApplicationEntity ->
                    KotlinLogging.logger { }.trace("CD : $objectId : Valid Pvoc Timeline Data found")
                    when (process) {
                        pvocWaProcessDefinitionKey -> {
                            processInstanceId = pvocWaiversApplicationEntity.pvocWaProcessInstanceId.toString()
                        }
                        else -> {
                            //do nothing
                        }
                    }
                    variables["processInstanceId"] = processInstanceId
                    variables["pvocWaiversApplicationEntity"] = pvocWaiversApplicationEntity
                    return variables

                } ?: KotlinLogging.logger { }.info("CD : $objectId : No Pvoc Timeline Data found")
            } else if (process == pvocMoProcessDefinitionKey) {
                pvocTimelinesDataRepo.findByIdOrNull(objectId)?.let { pvocTimelinesDataEntity ->
                    KotlinLogging.logger { }.trace("CD : $objectId : Valid Pvoc Timeline Data found")
                    when (process) {
                        pvocMoProcessDefinitionKey -> {
                            processInstanceId = pvocTimelinesDataEntity.pvocMonitProcessInstanceId.toString()
                        }
                        else -> {
                            //do nothing
                        }
                    }
                    variables["processInstanceId"] = processInstanceId
                    variables["pvocTimelinesData"] = pvocTimelinesDataEntity
                    return variables

                } ?: KotlinLogging.logger { }.info("CD : $objectId : No Pvoc Timeline Data found")
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }

    fun fetchAllTasksByAssignee(assigneeId: Long): List<BpmnTaskDetails>? {
        return bpmnCommonFunctions.fetchAllTasksByAssignee(assigneeId, pidPrefix)
    }

    fun fetchAllTasks(): List<BpmnTaskDetails>? {
        return bpmnCommonFunctions.fetchAllTasks(pidPrefix)
    }

    //Update task variable by object Id and task Key
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

    fun pvocCompleteTask(objectId: String, data: Map<String, Any>): HashMap<String, Any>? {
        KotlinLogging.logger { }.info("ObjectId : $objectId :  Completing task $data")
        try {
            this.taskService.complete(objectId, data)
            KotlinLogging.logger { }.info("objectId : $objectId :  Completed")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }

    fun pvocCompleteTask(objectId: Long, taskDefinitionKey: String, process: String): HashMap<String, Any>? {
        KotlinLogging.logger { }.info("ObjectId : $objectId :  Completing task $taskDefinitionKey")
        try {
            getPIdByObjectAndProcess(objectId, process)?.let {
                bpmnCommonFunctions.completeTask(it, taskDefinitionKey).let { variables ->
                    return variables
                }
            }
            KotlinLogging.logger { }.info("objectId : $objectId :  No objectId found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }

    fun checkStartProcessInputs(objectId: Long, assigneeId: Long, processKey: String): HashMap<String, Any>? {
        val variables: HashMap<String, Any> = HashMap()
        KotlinLogging.logger { }.info("objectId : $objectId : Checking start process values")
        try {
            if (processKey == pvocEaProcessDefinitionKey) {
                pvocApplicationRepo.findByIdOrNull(objectId)?.let { pvocApplicationEntity ->
                    variables["pvocApplication"] = pvocApplicationEntity
                    when (processKey) {
                        pvocEaProcessDefinitionKey -> {
                            pvocApplicationEntity.pvocEaStatus?.let { status ->
                                /*
                                if (status != 0) {
                                    KotlinLogging.logger { }.info("objectId : $objectId : Pvoc Application already has a application task assigned"); return null
                                }
                                */
                            }
                        }
                        else -> {
                            //do nothing
                        }
                    }

                }
            } else if (processKey == pvocWaProcessDefinitionKey) {
                iwaiversApplicationRepo.findByIdOrNull(objectId)?.let { pvocWaiversApplicationEntity ->
                    variables["pvocWaiversApplicationEntity"] = pvocWaiversApplicationEntity
                    when (processKey) {
                        pvocWaProcessDefinitionKey -> {
                            pvocWaiversApplicationEntity.pvocWaStatus?.let { status ->
                                /*
                                if (status != 0) {
                                    KotlinLogging.logger { }.info("objectId : $objectId : Pvoc Application already has a application task assigned"); return null
                                }
                                */
                            }
                        }
                        else -> {
                            //do nothing
                        }
                    }
                }

            } else if (processKey == pvocMoProcessDefinitionKey) {
                pvocTimelinesDataRepo.findByIdOrNull(objectId)?.let { pvocTimelinesDataEntity ->
                    variables["pvocTimelinesData"] = pvocTimelinesDataEntity
                    when (processKey) {
                        pvocEaProcessDefinitionKey -> {
                            pvocTimelinesDataEntity.pvocMonitStatus?.let { status ->
                                /*
                                if (status != 0) {
                                    KotlinLogging.logger { }.info("objectId : $objectId : Pvoc Application already has a application task assigned"); return null
                                }
                                */
                            }
                        }
                        else -> {
                            //do nothing
                        }
                    }
                }
            }

            //Check that the assignee is valid
            userRepo.findByIdOrNull(assigneeId)?.let { usersEntity ->
                variables["assigneeEmail"] = usersEntity.email.toString()
            }
                    ?: run { KotlinLogging.logger { }.info("objectId : $objectId : No user found for id $assigneeId"); return null }

            return variables

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
            return null
        }
    }

    /*
    ***********************************************************************************
    * PVOC APPLICATIONS EXEMPTIONS PROCESS
    ***********************************************************************************
     */
    //Start the PVOC exemptions applications process
    fun startPvocApplicationExemptionsProcess(application: PvocApplicationEntity): HashMap<String, String>? {
        val variables: HashMap<String, Any> = HashMap()
        KotlinLogging.logger { }.info("Exemption : ${application.id} : Starting PVOC exemptions applications process")
        try {
            variables.put("exemptionId", application.id!!)
            variables.put("appliedBy", application.createdBy!!)
            val process = this.runtimeService.startProcessInstanceById("exemptionApplicationProcess", variables)
            application.pvocWaProcessInstanceId = process.processInstanceId
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }

    private fun getTaskDetails(tasks: MutableList<org.flowable.task.api.Task>?): List<DiTaskDetails> {
        val taskDetails: MutableList<DiTaskDetails> = ArrayList()
        if (tasks != null) {
            for (task in tasks) {
                val properties = this.taskService.getVariables(task.id)
                taskDetails.add(DiTaskDetails(task.id, task.name, task.createTime, task.description, properties, task.category))
            }
        }
        return taskDetails
    }

    fun getExemptionTasks(category: String, exemptionId: Long): List<DiTaskDetails> {
        val tasks = taskService.createTaskQuery().taskCategory(category)
                .processVariableValueEquals("exemptionId", exemptionId)
                .listPage(0, 30)
        return getTaskDetails(tasks)
    }

    fun getWaiverTasks(category: String, waiverId: Long): List<DiTaskDetails> {
        val tasks = taskService.createTaskQuery().taskCategory(category)
                .processVariableValueEquals("waiverId", waiverId)
                .listPage(0, 30)
        return getTaskDetails(tasks)
    }

    fun getComplaintTasks(category: List<String>, exemptionId: Long): List<DiTaskDetails> {
        val tasks = mutableListOf<org.flowable.task.api.Task>()
        for (cat in category) {
            tasks.addAll(taskService.createTaskQuery().taskCategory(cat)
                    .processVariableValueEquals("complaintId", exemptionId)
                    .listPage(0, 30))
        }
        return getTaskDetails(tasks)
    }

    //pass to section officer as assigneeId
    fun pvocEaSubmitApplicationComplete(objectId: Long, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Submit application form complete")
        pvocCompleteTask(objectId, "pvocEaSubmitApplication", pvocEaProcessDefinitionKey)?.let {
            return bpmnCommonFunctions.assignTask(it["processInstanceId"].toString(), "pvocEaCheckApplicationComplete", assigneeId)
        }
        return false
    }

    fun pvocEaCheckApplicationComplete(objectId: Long, assigneeId: Long, applicationComplete: Boolean): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Check application complete")
        updateTaskVariableByObjectIdAndKey(objectId, "pvocEaCheckApplicationComplete", pvocEaProcessDefinitionKey, "applicationComplete", bpmnCommonFunctions.booleanToInt(applicationComplete).toString())
        fetchTaskByObjectId(objectId, pvocEaProcessDefinitionKey)?.let { taskDetails ->
            pvocCompleteTask(objectId, "pvocEaCheckApplicationComplete", pvocEaProcessDefinitionKey)?.let {
                //return bpmnCommonFunctions.assignTask(it["processInstanceId"].toString(), "pvocEaAcceptTerms", assigneeId)
                return if (applicationComplete) {   //lab report compliant
                    bpmnCommonFunctions.assignTask(it["processInstanceId"].toString(), "pvocEaApproveApplication", assigneeId)
                } else {
                    bpmnCommonFunctions.assignTask(it["processInstanceId"].toString(), "pvocEaDeferApplication", taskDetails[0].task.assignee.toLong())
                }
            }
        }
        return false
    }

    fun pvocAeDeferApplicationComplete(objectId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Defer application complete")
        pvocCompleteTask(objectId, "pvocEaDeferApplication", pvocEaProcessDefinitionKey)?.let {
            return true
        }
        return false
    }

    fun pvocAeRejectApplicationComplete(objectId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Reject application complete")
        pvocCompleteTask(objectId, "pvocEaDeferApplication", pvocEaProcessDefinitionKey)?.let {
            return true
        }
        return false
    }

    fun pvocEaApproveApplicationComplete(objectId: Long, applicationApproved: Boolean): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Approve application complete")
        updateTaskVariableByObjectIdAndKey(objectId, "pvocEaApproveApplication", pvocEaProcessDefinitionKey, "applicationApproved", bpmnCommonFunctions.booleanToInt(applicationApproved).toString())
        pvocCompleteTask(objectId, "pvocEaApproveApplication", pvocEaProcessDefinitionKey)?.let {
            return true
        }
        return false
    }

    fun endPvocEaProcess(objectId: String) {
        KotlinLogging.logger { }.info("End PVOC Exemptions Applications process for objectId $objectId")
        try {
            pvocApplicationRepo.findByIdOrNull(objectId.toLong())?.let { pvocApplicationEntity ->
                pvocApplicationEntity.pvocEaCompletedOn = Timestamp.from(Instant.now())
                pvocApplicationEntity.pvocEaStatus = processCompleted
                pvocApplicationRepo.save(pvocApplicationEntity)
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("$objectId : Unable to complete PVOC Exemptions Applications process for $objectId")
        }
    }

    /*
    ***********************************************************************************
    * PVOC WAIVERS APPLICATIONS PROCESS
    ***********************************************************************************
     */
    //Start the PVOC waivers applications process
    fun startPvocWaiversApplicationsProcess(waiver: PvocWaiversApplicationEntity, username: String) {
        val variables: HashMap<String, Any> = HashMap()
        KotlinLogging.logger { }.info("Waiver ID : ${waiver.id} : Starting PVOC waivers applications process")
        variables["waiverId"] = waiver.id
        variables["startedBy"] = username
        val process = this.runtimeService.startProcessInstanceById("waiverApplicationProcess", variables)
        waiver.pvocWaProcessInstanceId = process.processInstanceId
    }

    //submit the application
    fun pvocWaSubmitApplicationComplete(objectId: Long, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Submit application form complete")
        pvocCompleteTask(objectId, "pvocWaSubmitApplication", pvocWaProcessDefinitionKey)?.let {
            return bpmnCommonFunctions.assignTask(it["processInstanceId"].toString(), "pvocWaReviewApplication", assigneeId)
        }
        return false
    }

    //submit the application
    fun pvocWaReviewApplicationComplete(objectId: Long, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Review application form complete")
        pvocCompleteTask(objectId, "pvocWaReviewApplication", pvocWaProcessDefinitionKey)?.let {
            return bpmnCommonFunctions.assignTask(it["processInstanceId"].toString(), "pvocWaGenerateReport", assigneeId)
        }
        return false
    }


    //secretary generates report here
    fun pvocWaGenerateReportComplete(objectId: Long, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Generate report complete")
        pvocCompleteTask(objectId, "pvocWaGenerateReport", pvocWaProcessDefinitionKey)?.let {
            return bpmnCommonFunctions.assignTask(it["processInstanceId"].toString(), "pvocWaApproveWaiver", assigneeId)
        }
        return false
    }

    //secretary approves the report and forwards it to
    fun pvocWaApproveWaiverComplete(objectId: Long, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Approve waiver complete")
        pvocCompleteTask(objectId, "pvocWaApproveWaiver", pvocWaProcessDefinitionKey)?.let {
            return bpmnCommonFunctions.assignTask(it["processInstanceId"].toString(), "pvocWaGenerateMinutes", assigneeId)
        }
        return false
    }

    //adding method for defarrerals for reports by chairman
    //pass the rejected waiver back to sec(wetc)
    fun pvocWaDefferalWaiverComplete(objectId: Long, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Deferral waiver complete")
        pvocCompleteTask(objectId, "pvocWaApproveWaiver", pvocWaProcessDefinitionKey)?.let {
            return bpmnCommonFunctions.assignTask(it["processInstanceId"].toString(), "pvocWaGenerateMinutes", assigneeId)
        }
        return false
    }

    //adding method for rejection of report by chairman
    //pass the rejected waiver back to sec(wetc)
    fun pvocWaRejectionWaiverComplete(objectId: Long, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Rejection waiver complete")
        pvocCompleteTask(objectId, "pvocWaApproveWaiver", pvocWaProcessDefinitionKey)?.let {
            return bpmnCommonFunctions.assignTask(it["processInstanceId"].toString(), "pvocWaGenerateMinutes", assigneeId)
        }
        return false
    }

    fun pvocWaGenerateMinutesComplete(objectId: Long, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Generate minutes complete")
        fetchTaskByObjectId(objectId, pvocWaProcessDefinitionKey)?.let { taskDetails ->
            pvocCompleteTask(objectId, "pvocWaGenerateMinutes", pvocWaProcessDefinitionKey)?.let {
                bpmnCommonFunctions.assignTask(it["processInstanceId"].toString(), "pvocWaGenerateDeferral", taskDetails[0].task.assignee.toLong())
                return bpmnCommonFunctions.assignTask(it["processInstanceId"].toString(), "pvocWaNscApproval", assigneeId)
            }
        }
        return false
    }

    //call this after sending defferal letter to importer
    fun pvocWaGenerateDeferralComplete(objectId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Generate deferral complete")
        pvocCompleteTask(objectId, "pvocWaGenerateDeferral", pvocWaProcessDefinitionKey)?.let {
            return true
        }
        return false
    }

    //call this when ncs approves minutes
    fun pvocWaNscApproveComplete(objectId: Long, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Nsc Approve complete")
        pvocCompleteTask(objectId, "pvocWaNscApproval", pvocWaProcessDefinitionKey)?.let {
            return bpmnCommonFunctions.assignTask(it["processInstanceId"].toString(), "pvocWaGenerateWaiverRequestLetter", assigneeId)
        }
        return false
    }

    fun pvocWaGenerateWaiverReqLetterComplete(objectId: Long, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Generate waiver request letter complete")
        pvocCompleteTask(objectId, "pvocWaGenerateWaiverRequestLetter", pvocWaProcessDefinitionKey)?.let {
            return bpmnCommonFunctions.assignTask(it["processInstanceId"].toString(), "pvocWaSubmitApprovalLetter", assigneeId)
        }
        return false
    }

    fun pvocWaSubmitApprovalLetterComplete(objectId: Long, assigneeId: Long, waiverApproved: Boolean): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Submit approval letter complete")
        updateTaskVariableByObjectIdAndKey(objectId, "pvocWaSubmitApprovalLetter", pvocWaProcessDefinitionKey, "waiverApproved", bpmnCommonFunctions.booleanToInt(waiverApproved).toString())
        pvocCompleteTask(objectId, "pvocWaSubmitApprovalLetter", pvocWaProcessDefinitionKey)?.let {
            //return bpmnCommonFunctions.assignTask(it["processInstanceId"].toString(), "pvocEaAcceptTerms", assigneeId)
            return if (waiverApproved) {   //waiver approved
                bpmnCommonFunctions.assignTask(it["processInstanceId"].toString(), "pvocWaGenerateApprovalLetter", assigneeId)
            } else {
                bpmnCommonFunctions.assignTask(it["processInstanceId"].toString(), "pvocWaGenerateRejectionLetter", assigneeId)
            }
        }
        return false
    }

    fun pvocWaGenerateWaiverApprovalLetterComplete(objectId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Generate waiver approval letter complete")
        pvocCompleteTask(objectId, "pvocWaGenerateApprovalLetter", pvocWaProcessDefinitionKey)?.let {
            return true
        }
        return false
    }

    fun pvocWaGenerateWaiverRejectionLetterComplete(objectId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Generate waiver rejection letter complete")
        pvocCompleteTask(objectId, "pvocWaGenerateRejectionLetter", pvocWaProcessDefinitionKey)?.let {
            return true
        }
        return false
    }

    fun endPvocWaProcess(objectId: String) {
        KotlinLogging.logger { }.info("End PVOC Waivers Applications process for objectId $objectId")
        try {
            pvocApplicationRepo.findByIdOrNull(objectId.toLong())?.let { pvocApplicationEntity ->
                pvocApplicationEntity.pvocWaCompletedOn = Timestamp.from(Instant.now())
                pvocApplicationEntity.pvocWaStatus = processCompleted
                pvocApplicationRepo.save(pvocApplicationEntity)
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("$objectId : Unable to complete PVOC Waivers Applications process for $objectId")
        }
    }

    /*
    ***********************************************************************************
    * PVOC MONITORING PROCESS
    ***********************************************************************************
     */
    //Start the PVOC exemptions applications process
    fun startPvocMonitoringProcess(objectId: Long, assigneeId: Long): HashMap<String, String>? {
        var variables: HashMap<String, Any> = HashMap()
        KotlinLogging.logger { }.info("objectid : $objectId : Starting PVOC monitoring process")
        try {
            checkStartProcessInputs(objectId, assigneeId, pvocMoProcessDefinitionKey)?.let { checkVariables ->
                val pvocTimelinesData: PvocTimelinesDataEntity = checkVariables["pvocTimelinesData"] as PvocTimelinesDataEntity
                variables["objectId"] = pvocTimelinesData.id.toString()
                variables["analysisReportOk"] = 0
                variables["analysisReportAction"] = 0
                variables["pvocAgentId"] = 0

                bpmnCommonFunctions.startBpmnProcess(pvocMoProcessDefinitionKey, pvocMoBusinessKey, variables, assigneeId)?.let {
                    pvocTimelinesData.pvocMonitProcessInstanceId = it["processInstanceId"]
                    pvocTimelinesData.pvocMonitStartedOn = Timestamp.from(Instant.now())
                    pvocTimelinesData.pvocMonitStatus = processStarted
                    pvocTimelinesDataRepo.save(pvocTimelinesData)
                    KotlinLogging.logger { }.info("objectId : $objectId : Successfully started PVOC monitoring process")
                    return it
                } ?: run {
                    KotlinLogging.logger { }.info("$objectId : Unable to start PVOC monitoring process")
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }


    fun pvocMoGenerateAnalysisReportComplete(objectId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Generate analysis report complete")
        fetchTaskByObjectId(objectId, pvocMoProcessDefinitionKey)?.let { taskDetails ->
            pvocCompleteTask(objectId, "pvocMoGenerateAnalysisReport", pvocMoProcessDefinitionKey)?.let {
                return bpmnCommonFunctions.assignTask(it["processInstanceId"].toString(), "pvocMoCheckAnalysisReport", taskDetails[0].task.assignee.toLong())
            }
        }
        return false
    }

    fun pvocMoCheckAnalysisReportComplete(objectId: Long, analysisReportOk: Boolean): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Check analysis report complete")
        updateTaskVariableByObjectIdAndKey(objectId, "pvocMoCheckAnalysisReport", pvocMoProcessDefinitionKey, "analysisReportOk", bpmnCommonFunctions.booleanToInt(analysisReportOk).toString())
        fetchTaskByObjectId(objectId, pvocMoProcessDefinitionKey)?.let { taskDetails ->
            pvocCompleteTask(objectId, "pvocMoCheckAnalysisReport", pvocMoProcessDefinitionKey)?.let {
                if (analysisReportOk) {
                    return true //process ends here
                } else {
                    return bpmnCommonFunctions.assignTask(it["processInstanceId"].toString(), "pvocMoRequestForInfo", taskDetails[0].task.assignee.toLong())
                }
            }
        }
        return false
    }

    fun pvocMoRequestForInfoComplete(objectId: Long, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Request for info complete")
        pvocCompleteTask(objectId, "pvocMoRequestForInfo", pvocMoProcessDefinitionKey)?.let {
            return bpmnCommonFunctions.assignTask(it["processInstanceId"].toString(), "pvocMoRequestForInfoFeedback", assigneeId)
        }
        return false
    }

    fun pvocMoRequestForInfoFeedbackComplete(objectId: Long, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Request for info feedback complete")
        fetchTaskByObjectId(objectId, pvocMoProcessDefinitionKey)?.let { taskDetails ->
            updateTaskVariableByObjectIdAndKey(objectId, "pvocMoRequestForInfoFeedback", pvocMoProcessDefinitionKey, "pvocAgentId", taskDetails[0].task.assignee)
        }
        pvocCompleteTask(objectId, "pvocMoRequestForInfoFeedback", pvocMoProcessDefinitionKey)?.let {
            return bpmnCommonFunctions.assignTask(it["processInstanceId"].toString(), "pvocMoGenMonitoredCasesAnalysisRpt", assigneeId)
        }
        return false
    }

    fun pvocMoGenerateMonitoredCasesRptComplete(objectId: Long, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Generate monitored cases report complete")
        pvocCompleteTask(objectId, "pvocMoGenMonitoredCasesAnalysisRpt", pvocMoProcessDefinitionKey)?.let {
            return bpmnCommonFunctions.assignTask(it["processInstanceId"].toString(), "pvocMoMpvocAnalysisRptReview", assigneeId)
        }
        return false
    }

    fun pvocMoMpvocAnalysisComplete(objectId: Long, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  MPVOC analysis complete")
        pvocCompleteTask(objectId, "pvocMoMpvocAnalysisRptReview", pvocMoProcessDefinitionKey)?.let {
            return bpmnCommonFunctions.assignTask(it["processInstanceId"].toString(), "pvocMoHodAnalysisRptReview", assigneeId)
        }
        return false
    }

    fun pvocMoHodAnalysisComplete(objectId: Long, assigneeId: Long, analysisReportAction: String): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Check HOD analysis complete")
        updateTaskVariableByObjectIdAndKey(objectId, "pvocMoHodAnalysisRptReview", pvocMoProcessDefinitionKey, "analysisReportAction", analysisReportAction.toLowerCase())
        fetchTaskByObjectId(objectId, pvocMoProcessDefinitionKey)?.let { taskDetails ->
            pvocCompleteTask(objectId, "pvocMoHodAnalysisRptReview", pvocMoProcessDefinitionKey)?.let {
                return when (analysisReportAction) {
                    "penalty" -> bpmnCommonFunctions.assignTask(it["processInstanceId"].toString(), "pvocMoGeneratePenaltyLetter", assigneeId)
                    "warning" -> bpmnCommonFunctions.assignTask(it["processInstanceId"].toString(), "pvocMoGenerateWarningLetter", assigneeId)
                    else -> bpmnCommonFunctions.assignTask(it["processInstanceId"].toString(), "pvocMoCloseCase", taskDetails[0].task.assignee.toLong())
                }
            }
        }
        return false
    }

    fun pvocMoGeneratePenaltyLetterComplete(objectId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Generate penalty letter and invoice complete")
        fetchTaskByObjectId(objectId, pvocMoProcessDefinitionKey)?.let { taskDetails ->
            pvocCompleteTask(objectId, "pvocMoGeneratePenaltyLetter", pvocMoProcessDefinitionKey)?.let {
                return bpmnCommonFunctions.assignTask(it["processInstanceId"].toString(), "pvocMoSubmitPenaltyLetter", taskDetails[0].task.assignee.toLong())
            }
        }
        return false
    }

    fun pvocMoSubmitPenaltyLetterComplete(objectId: Long, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Generate submit  complete")
        pvocCompleteTask(objectId, "pvocMoGeneratePenaltyLetter", pvocMoProcessDefinitionKey)?.let {
            return bpmnCommonFunctions.assignTask(it["processInstanceId"].toString(), "pvocMoHodAnalysisRptReview", assigneeId)
        }
        return false
    }

    fun pvocMoSubmitPaymentComplete(objectId: Long, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Payment complete")
        pvocCompleteTask(objectId, "pvocMoPenaltyPayment", pvocMoProcessDefinitionKey)?.let {
            return bpmnCommonFunctions.assignTask(it["processInstanceId"].toString(), "pvocMoCloseCase", assigneeId)
        }
        return false
    }

    fun pvocMoGenerateWarningLetterComplete(objectId: Long, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Generate warning letter complete")
        pvocCompleteTask(objectId, "pvocMoGenerateWarningLetter", pvocMoProcessDefinitionKey)?.let {
            return bpmnCommonFunctions.assignTask(it["processInstanceId"].toString(), "pvocMoCloseCase", assigneeId)
        }
        return false
    }

    fun pvocMoCloseCaseComplete(objectId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Close case complete")
        pvocCompleteTask(objectId, "pvocMoCloseCase", pvocMoProcessDefinitionKey)?.let {
            return true
        }
        return false
    }

    fun endPvocMoProcess(objectId: String) {
        KotlinLogging.logger { }.info("End PVOC Monitoring process for objectId $objectId")
        try {
            pvocTimelinesDataRepo.findByIdOrNull(objectId.toLong())?.let { pvocTimelinesData ->
                pvocTimelinesData.pvocMonitCompletedOn = Timestamp.from(Instant.now())
                pvocTimelinesData.pvocMonitStatus = processCompleted
                pvocTimelinesDataRepo.save(pvocTimelinesData)
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("$objectId : Unable to complete PVOC Monitoring process for $objectId")
        }
    }

    fun startPvocComplaintApplicationsProcess(application: PvocComplaintEntity) {
        val variables: HashMap<String, Any> = HashMap()
        KotlinLogging.logger { }.info("Complaint : ${application.id} : Starting PVOC complaint applications process")
        try {
            variables.put("complaintId", application.id!!)
            variables.put("appliedBy", application.email!!)
            val process = this.runtimeService.startProcessInstanceById("complaintApplicationProcess", variables)
            application.processId = process.processInstanceId
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
    }
}