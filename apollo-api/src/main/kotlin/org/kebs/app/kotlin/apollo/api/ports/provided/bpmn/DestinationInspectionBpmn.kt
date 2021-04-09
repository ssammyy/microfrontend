package org.kebs.app.kotlin.apollo.api.ports.provided.bpmn

import mu.KotlinLogging
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.di.BpmnTaskDetails
import org.kebs.app.kotlin.apollo.api.ports.provided.scheduler.SchedulerImpl
import org.kebs.app.kotlin.apollo.store.model.di.ConsignmentDocumentDetailsEntity
import org.kebs.app.kotlin.apollo.store.repo.*
import org.kebs.app.kotlin.apollo.store.repo.di.IConsignmentDocumentDetailsRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.Instant
import kotlin.collections.HashMap

@Service
class DestinationInspectionBpmn(
    private val taskService: TaskService,
    private val runtimeService: RuntimeService,
    private val userRepo: IUserRepository,
    private val diRepo: IDestinationInspectionRepository,
    private val schedulerRepo: ISchedulerRepository,
    private val schedulerImpl: SchedulerImpl,
    private val bpmnCommonFunctions: BpmnCommonFunctions,
    private val notifications: Notifications,
    private val iConsignmentDocumentDetailsRepo: IConsignmentDocumentDetailsRepository,
    private val iCdItemDetailsRepo: ICdItemDetailsRepo
) {

    @Value("\${bpmn.di.coc.process.definition.key}")
    lateinit var diCocProcessDefinitionKey: String

    @Value("\${bpmn.di.coc.business.key}")
    lateinit var diCocBusinessKey: String

    @Value("\${bpmn.di.mv.cor.process.definition.key}")
    lateinit var diMvWithCorProcessDefinitionKey: String

    @Value("\${bpmn.di.mv.cor.business.key}")
    lateinit var diMvWithCorBusinessKey: String

    @Value("\${bpmn.di.mv.inspection.process.definition.key}")
    lateinit var diMvInspectionProcessDefinitionKey: String

    @Value("\${bpmn.di.mv.inspection.business.key}")
    lateinit var diMvInspectionBusinessKey: String

    @Value("\${bpmn.task.default.duration}")
    lateinit var defaultDuration: Integer

    val successMessage: String = "success"
    val processStarted: Int = 1
    val processCompleted: Int = 2
    val pidPrefix = "di"

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
            if (process == diMvWithCorProcessDefinitionKey) {
                iConsignmentDocumentDetailsRepo.findByIdOrNull(objectId)?.let { cdEntity ->
                    KotlinLogging.logger { }.info("CD : $objectId : Valid CD found")
                    cdEntity.diProcessInstanceId?.let { it ->
                        processInstanceId = it
                    }
                    variables["processInstanceId"] = processInstanceId
                    variables["cd"] = cdEntity
                    return variables
                }
                KotlinLogging.logger { }.info("CD : $objectId : No CD found")
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
    fun updateTaskVariableByObjectIdAndKey(
        objectId: Long,
        taskDefinitionKey: String,
        process: String,
        parameterName: String,
        parameterValue: String
    ): Boolean {
        try {
            getPIdByObjectAndProcess(objectId, process)?.let {
                bpmnCommonFunctions.getTaskByTaskDefinitionKey(it["processInstanceId"].toString(), taskDefinitionKey)
                    ?.let { task ->
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

    fun dICompleteTask(objectId: Long, taskDefinitionKey: String, process: String): HashMap<String, Any>? {
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
            if (processKey == diMvWithCorProcessDefinitionKey) {
                iConsignmentDocumentDetailsRepo.findByIdOrNull(objectId)?.let { cdEntity ->
                    variables["cd"] = cdEntity
                    when (processKey) {
                        diMvWithCorProcessDefinitionKey -> {
                            cdEntity.diProcessStatus?.let { status ->
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
            else if (processKey == diMvInspectionProcessDefinitionKey) {
                iCdItemDetailsRepo.findByIdOrNull(objectId)?.let{ cdItemDetailsEntity ->
                    variables["cdItemDetailsEntity"] = cdItemDetailsEntity
                    when (processKey) {
                        diMvInspectionProcessDefinitionKey-> {
                            cdItemDetailsEntity.inspectionProcessStatus?.let { status ->
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
            } ?: run {
                KotlinLogging.logger { }.info("objectId : $objectId : No user found for id $assigneeId"); return null
            }

            return variables

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
            return null
        }
    }

//    fun msAssignTask(processInstanceId: String, taskDefinitionKey: String?, assigneeId: Long): Boolean {
//        KotlinLogging.logger { }.info("Assign next task begin")
//        try {
//            var localAssigneeId: String = assigneeId.toString()
//            var task: Task? = null
//
//            taskDefinitionKey?.let{
//                task = bpmnCommonFunctions.getTaskByTaskDefinitionKey(processInstanceId, taskDefinitionKey)
//            }?: run {
//                task = bpmnCommonFunctions.getTasks("processInstanceId", processInstanceId)?.get(0)
//            }
//
//            task?.let { task->
//                userRepo.findByIdOrNull(localAssigneeId.toLong())?.let { usersEntity ->
//                    bpmnCommonFunctions.updateVariable(task.id, "email", usersEntity.email.toString())
//                }
//                //Refetch the task because we have updated the email variable
//                bpmnCommonFunctions.getTaskById(task.id)?.let { updatedTask->
//                    updatedTask.assignee = localAssigneeId
//                    //Adds 2 days (default duration) and sets this as the due date
//                    updatedTask.dueDate = DateTime(Date()).plusDays(defaultDuration.toInt()).toDate()
//                    taskService.saveTask(updatedTask)
//                    KotlinLogging.logger { }.info("Task ${updatedTask.name} assigned to $localAssigneeId")
//                    return true
//                }
//            }
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.error(e.message, e)
//        }
//        return false
//    }

    /*
    ***********************************************************************************
    * IMPORTED VEHICLES WITH COR PROCESS
    ***********************************************************************************
     */
    //Start the process
    fun startImportedVehiclesWithCorProcess(objectId: Long, assigneeId: Long): HashMap<String, String>? {
        var variables: HashMap<String, Any> = HashMap()
        KotlinLogging.logger { }.info("objectid : $objectId : Starting Imported Vehicles with cor process")
        try {
            checkStartProcessInputs(objectId, assigneeId, diMvWithCorProcessDefinitionKey)?.let { checkVariables ->

                val consignmentDocumentDetailsEntity: ConsignmentDocumentDetailsEntity =
                    checkVariables["cd"] as ConsignmentDocumentDetailsEntity
                variables["objectId"] = consignmentDocumentDetailsEntity.id.toString()
                variables["inspectionOfficerUserId"] = assigneeId
                variables["cdDecision"] = ""
                variables["supervisorTarget"] = 0
                variables["supervisorTargetApproved"] = '0'
                variables["itemId"] = 0
                variables["paymentRequired"] = '0'


                bpmnCommonFunctions.startBpmnProcess(
                    diMvWithCorProcessDefinitionKey,
                    diMvWithCorBusinessKey,
                    variables,
                    assigneeId
                )?.let {
                    consignmentDocumentDetailsEntity.diProcessInstanceId = it["processInstanceId"]
                    consignmentDocumentDetailsEntity.diProcessStartedOn = Timestamp.from(Instant.now())
                    consignmentDocumentDetailsEntity.diProcessStatus = processStarted
                    iConsignmentDocumentDetailsRepo.save(consignmentDocumentDetailsEntity)
                    KotlinLogging.logger { }.info("objectId : $objectId : Successfully started DI Imported MV with CoR process")
                    return it
                } ?: run {
                    KotlinLogging.logger { }.info("$objectId : Unable to start DI Imported MV with CoR process")
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }

    //Assign the CD document to Inspection Officer
    fun diAssignIOComplete(objectId: Long, assigneeId: Long, supervisorTarget: Int, cdItemId: Long = 0): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  CD assigned to Inspection Officer")
        updateTaskVariableByObjectIdAndKey(objectId, "ImportedVehiclesWithCoRAssignIo", diMvWithCorProcessDefinitionKey,
            "supervisorTarget", supervisorTarget.toString())
        updateTaskVariableByObjectIdAndKey(objectId, "ImportedVehiclesWithCoRAssignIo", diMvWithCorProcessDefinitionKey,
            "itemId", cdItemId.toString())
        fetchTaskByObjectId(objectId, diMvWithCorProcessDefinitionKey)?.let { taskDetails ->
            dICompleteTask(objectId, "ImportedVehiclesWithCoRAssignIo", diMvWithCorProcessDefinitionKey)?.let {
                return if (supervisorTarget == 1){
                    true
                } else {
                    bpmnCommonFunctions.assignTask(
                        it["processInstanceId"].toString(),
                        "ImportedVehiclesWithCoRCheckCdComplete",
                        assigneeId
                    )
                }
            }
        }
        return false
    }

    //Check if CD is complete
    fun diCheckCdComplete(objectId: Long, assigneeId: Long, cdDecision: String): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Check CD complete")
        updateTaskVariableByObjectIdAndKey(
            objectId,
            "ImportedVehiclesWithCoRCheckCdComplete",
            diMvWithCorProcessDefinitionKey,
            "cdDecision",
            cdDecision
        )
        fetchTaskByObjectId(objectId, diMvWithCorProcessDefinitionKey)?.let { taskDetails ->
            dICompleteTask(objectId, "ImportedVehiclesWithCoRCheckCdComplete", diMvWithCorProcessDefinitionKey)?.let {
                return if (cdDecision == "APPROVE" || cdDecision == "REJECT"){
                    true
                } else {
                    bpmnCommonFunctions.assignTask(
                        it["processInstanceId"].toString(),
                        "ImportedVehiclesWithCoRReviewTargetApprovalRequest",
                        assigneeId
                    )
                }
            }
        }
        return false
    }

    //Check if target review is complete
    fun diReviewTargetRequestComplete(objectId: Long, assigneeId: Long, targetApprovalStatus: Boolean, cdItemId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Target Request Complete")
        updateTaskVariableByObjectIdAndKey(objectId,
            "ImportedVehiclesWithCoRReviewTargetApprovalRequest", diMvWithCorProcessDefinitionKey,
            "supervisorTargetApproved", bpmnCommonFunctions.booleanToInt(targetApprovalStatus).toString()).let {
            updateTaskVariableByObjectIdAndKey(objectId,
                "ImportedVehiclesWithCoRReviewTargetApprovalRequest", diMvWithCorProcessDefinitionKey,
                "itemId", cdItemId.toString())
        }
        fetchTaskByObjectId(objectId, diMvWithCorProcessDefinitionKey)?.let { taskDetails ->
            dICompleteTask(objectId, "ImportedVehiclesWithCoRReviewTargetApprovalRequest", diMvWithCorProcessDefinitionKey)?.let {
                return true
            }
        }
        return false
    }

    fun endImportedVehiclesWithCorProcess(objectId: String) {
        KotlinLogging.logger { }.info("End Motor Vehicle With CoR process for objectId $objectId")
        try{
            iConsignmentDocumentDetailsRepo.findByIdOrNull(objectId.toLong())?.let { consignmentDocumentDetailsEntity ->
                consignmentDocumentDetailsEntity.diProcessCompletedOn = Timestamp.from(Instant.now())
                consignmentDocumentDetailsEntity.diProcessStatus = processCompleted
                iConsignmentDocumentDetailsRepo.save(consignmentDocumentDetailsEntity)
            }
        } catch (e: Exception){
            KotlinLogging.logger { }.error("$objectId : Unable to complete Motor Vehicle With CoR process for $objectId")
        }
    }

    /*
    ***********************************************************************************
    * MOTOR VEHICLE INSPECTION PROCESS
    ***********************************************************************************
     */

    //Receive KESWS inspection schedule
    fun diReceiveInspectionScheduleComplete(objectId: Long, assigneeId: Long) {
        KotlinLogging.logger { }.info("Trigger Inspection schedule request task to complete")
        Thread.sleep(3000)
        //Get the process instance ID
        var processInstanceId: String? = null
        iConsignmentDocumentDetailsRepo.findByIdOrNull(objectId)?.let { cdEntity ->
            KotlinLogging.logger { }.info (  "CD : $objectId : Valid CD found" )
            cdEntity.diProcessInstanceId?.let { it ->
                processInstanceId = it
            }
        }
        KotlinLogging.logger { }.info ("Process Instance ID: $processInstanceId")

        processInstanceId?.let {
            try {
                val ex = runtimeService.createExecutionQuery().processInstanceId(it)
                    .activityId("MotorInspectionReceiveSchedule")
                    .singleResult()
                KotlinLogging.logger { }.info("Execution ID: ${ex.id} & activityID: ${ex.activityId}")
                runtimeService.trigger(ex.getId())
                bpmnCommonFunctions.assignTask(
                    it,
                    "MotorInspectionPaymentRequired",
                    assigneeId
                )
            } catch (e: Exception){
                KotlinLogging.logger { }.error("An error occurred on Trigger Inspection schedule request task to complete ", e)
            }
        }
    }

    //Check payment required
    fun diPaymentRequiredComplete(objectId: Long, assigneeId: Long, paymentRequired:Boolean): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Payment Required Complete")
        updateTaskVariableByObjectIdAndKey(
            objectId,
            "MotorInspectionPaymentRequired", diMvWithCorProcessDefinitionKey,
            "paymentRequired", bpmnCommonFunctions.booleanToInt(paymentRequired).toString()
        )
        fetchTaskByObjectId(objectId, diMvWithCorProcessDefinitionKey)?.let { taskDetails ->
            dICompleteTask(objectId, "MotorInspectionPaymentRequired", diMvWithCorProcessDefinitionKey)?.let {
                return if (paymentRequired){
//                    bpmnCommonFunctions.assignTask(it["processInstanceId"].toString(), "MotorInspectionGenerateDemandNote", assigneeId)
                    return true
                } else {
                    bpmnCommonFunctions.assignTask(it["processInstanceId"].toString(), "MotorInspectionFillInspectionForms", assigneeId)
                }
            }
        }
        return false
    }

    //Receive Payment Confirmation
    fun diReceivePaymentConfirmation(objectId: Long, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("::::::::::: Receive payment Confirmation from Payment Integration :::::::::::::::::::::")
        Thread.sleep(3000)
        //Get the process instance ID
        var processInstanceId: String? = null
        iConsignmentDocumentDetailsRepo.findByIdOrNull(objectId)?.let { cdEntity ->
            KotlinLogging.logger { }.info (  "CD : $objectId : Valid CD found" )
            cdEntity.diProcessInstanceId?.let { it ->
                processInstanceId = it
            }
        }
        KotlinLogging.logger { }.info ("Process Instance ID: $processInstanceId")
        processInstanceId?.let {
            //Notify Single Window of payment
            bpmnCommonFunctions.sendSwPaymentComplete(objectId.toString())
            try {
                val ex = runtimeService.createExecutionQuery().processInstanceId(it)
                    .activityId("MotorInspectionReceivePaymentConfirmation")
                    .singleResult()
                KotlinLogging.logger { }.info("Execution ID: ${ex.id} & activityID: ${ex.activityId}")
                runtimeService.trigger(ex.getId())
                bpmnCommonFunctions.assignTask(
                    it,
                    "MotorInspectionFillInspectionForms",
                    assigneeId
                )
            } catch (e: Exception){
                KotlinLogging.logger { }.error("An error occurred on Trigger receive payment confirmation task to complete ", e)
            }
        }
        return false
    }

    //After payment complete notification sent to SW
//    fun diSendPaymentNotificationToSWComplete(objectId: Long, inspectionOfficerUserId: Long): Boolean {
//        KotlinLogging.logger { }.info("objectId : $objectId : Send Payment to SW Complete")
//        KotlinLogging.logger { }.info(":::::::::::::: Assignee ID: $inspectionOfficerUserId")
//        iConsignmentDocumentDetailsRepo.findByIdOrNull(objectId)?.let { cdEntity ->
//            KotlinLogging.logger { }.info (  "CD : $objectId : Valid CD found" )
//            cdEntity.diProcessInstanceId?.let { it ->
//                return bpmnCommonFunctions.assignTask(it, "MotorInspectionFillInspectionForms", inspectionOfficerUserId)
//            }
//        }
//        return false
//    }

    //Fill Inspection Forms
    fun diFillInspectionForms(objectId: Long, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Fill Inspection Forms Complete")
        fetchTaskByObjectId(objectId, diMvWithCorProcessDefinitionKey)?.let { taskDetails ->
            dICompleteTask(objectId, "MotorInspectionFillInspectionForms", diMvWithCorProcessDefinitionKey)?.let {
                return bpmnCommonFunctions.assignTask(it["processInstanceId"].toString(), "MotorInspectionMinistryInspectionRequired", assigneeId)
            }
        }
        return false
    }

    //Check Ministry Inspection Required
    fun diMinistryInspectionRequiredComplete(objectId: Long, assigneeId: Long, forwardToMinistry:Boolean): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Check Ministry Inspection Required Complete")
        updateTaskVariableByObjectIdAndKey(
            objectId,
            "MotorInspectionMinistryInspectionRequired", diMvWithCorProcessDefinitionKey,
            "forwardToMinistry", bpmnCommonFunctions.booleanToInt(forwardToMinistry).toString()
        )
        fetchTaskByObjectId(objectId, diMvWithCorProcessDefinitionKey)?.let { taskDetails ->
            dICompleteTask(objectId, "MotorInspectionMinistryInspectionRequired", diMvWithCorProcessDefinitionKey)?.let {
                return if (forwardToMinistry){
                    bpmnCommonFunctions.assignTask(it["processInstanceId"].toString(), "MotorInspectionMinistryGenerateReport", assigneeId)
                } else {
                    bpmnCommonFunctions.assignTask(it["processInstanceId"].toString(), "MotorInspectionGenerateMotorInspectionReport", assigneeId)
                }
            }
        }
        return false
    }

    //Complete Generate Ministry Motor Vehicle Inspection Report
    fun diGenerateMinistryInspectionReportComplete(objectId: Long, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Generate Ministry Inspection Report Complete complete")
        dICompleteTask(objectId, "MotorInspectionMinistryGenerateReport", diMvWithCorProcessDefinitionKey)?.let {
            return bpmnCommonFunctions.assignTask(it["processInstanceId"].toString(), "MotorInspectionGenerateMotorInspectionReport", assigneeId)
        }
        return false
    }

//    //Complete Ministry Inspection Report Review
//    fun diReviewMinistryInspectionReportComplete(objectId: Long, assigneeId: Long): Boolean {
//        KotlinLogging.logger { }.info("objectId : $objectId :  Review Ministry Inspection Report Complete complete")
//        dICompleteTask(objectId, "MotorInspectionReviewMinistryInspectionReport", diMvWithCorProcessDefinitionKey)?.let {
//            return bpmnCommonFunctions.assignTask(it["processInstanceId"].toString(), "MotorInspectionGenerateMotorInspectionReport", assigneeId)
//        }
//        return false
//    }

    //Complete Generate Motor Inspection Report
    fun diGenerateMotorInspectionReportComplete(objectId: Long, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Generate Motor Inspection Report Complete complete")
        dICompleteTask(objectId, "MotorInspectionGenerateMotorInspectionReport", diMvWithCorProcessDefinitionKey)?.let {
            return bpmnCommonFunctions.assignTask(it["processInstanceId"].toString(), "MotorInspectionReviewMotorInspectionReport", assigneeId)
        }
        return false
    }

    //Check Ministry Inspection Required
    fun diReviewMotorInspectionReportComplete(objectId: Long, assigneeId: Long, motorInspectionReportApproved:Boolean): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId : Review Motor Inspection Report Complete")
        updateTaskVariableByObjectIdAndKey(
            objectId,
            "MotorInspectionReviewMotorInspectionReport", diMvWithCorProcessDefinitionKey,
            "motorInspectionReportApproved", bpmnCommonFunctions.booleanToInt(motorInspectionReportApproved).toString()
        )
        fetchTaskByObjectId(objectId, diMvWithCorProcessDefinitionKey)?.let { taskDetails ->
            dICompleteTask(objectId, "MotorInspectionReviewMotorInspectionReport", diMvWithCorProcessDefinitionKey)?.let {
                return if (motorInspectionReportApproved){
                    return true
                } else {
                    bpmnCommonFunctions.assignTask(it["processInstanceId"].toString(), "MotorInspectionGenerateMotorInspectionReport", assigneeId)
                }
            }
        }
        return false
    }

}
