package org.kebs.app.kotlin.apollo.api.ports.provided.bpmn

import mu.KotlinLogging
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.task.api.Task
import org.joda.time.DateTime
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.di.BpmnTaskDetails
import org.kebs.app.kotlin.apollo.api.ports.provided.scheduler.SchedulerImpl
import org.kebs.app.kotlin.apollo.store.model.ms.ComplaintEntity
import org.kebs.app.kotlin.apollo.store.model.ms.MsFuelInspectionEntity
import org.kebs.app.kotlin.apollo.store.model.MsWorkPlanGeneratedEntity
import org.kebs.app.kotlin.apollo.store.repo.*
import org.kebs.app.kotlin.apollo.store.repo.ms.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.Instant
import java.util.*
import kotlin.collections.HashMap

@Service
class MarketSurveillanceBpmn(
        private val taskService: TaskService,
        private val runtimeService: RuntimeService,
        private val userRepo: IUserRepository,
        private val complaintRepo: IComplaintRepository,
        private val workPlanGenerateRepo: IWorkPlanGenerateRepository,
        private val fuelInspectionRepo: IFuelInspectionRepository,
        private val schedulerRepo: ISchedulerRepository,
        private val schedulerImpl: SchedulerImpl,
        private val bpmnCommonFunctions: BpmnCommonFunctions,
        private val notifications:Notifications) {

    @Value("\${bpmn.ms.complaints.process.definition.key}")
    lateinit var msComplaintsProcessDefinitionKey: String

    @Value("\${bpmn.ms.complaints.business.key}")
    lateinit var msComplaintsBusinessKey: String

    @Value("\${bpmn.ms.fuel.monitoring.process.definition.key}")
    lateinit var msFuelMonitoringProcessDefinitionKey: String

    @Value("\${bpmn.ms.fuel.monitoring.business.key}")
    lateinit var msFuelMonitoringBusinessKey: String

    @Value("\${bpmn.ms.market.surveillance.process.definition.key}")
    lateinit var msMarketSurveillanceProcessDefinitionKey: String

    @Value("\${bpmn.ms.market.surveillance.business.key}")
    lateinit var msMarketSurveillanceBusinessKey: String

    @Value("\${bpmn.task.default.duration}")
    lateinit var defaultDuration: Integer

    @Value("\${bpmn.timer.boundary.task.delay}")
    var timerBoundaryTaskDelay: Long = 0

    @Value("\${bpmn.ms.hod.action.director.updates.id}")
    lateinit var msDirectorUpdatesNotifId: String

    @Value("\${bpmn.notification.lab.results.delayed}")
    lateinit var msLabResultsDelayedNotifId: String

    val successMessage: String = "success"
    val processStarted:Int = 1
    val processCompleted:Int = 2
    val pidPrefix = "ms"

    fun fetchTaskByComplaintId(complaintId: Long, process: String): List<BpmnTaskDetails>? {
        try {
            getPIdByComplaintAndProcess(complaintId, process)?.let{
                val processInstanceId =  it["processInstanceId"].toString()
                bpmnCommonFunctions.getTasks("processInstanceId", processInstanceId)?.let { tasks ->
                    return bpmnCommonFunctions.generateTaskDetails(tasks)
                }?: run{ KotlinLogging.logger { }.info("ComplaintId : $complaintId : No task found");return null}
            }
            KotlinLogging.logger { }.info("ComplaintId : $complaintId : No complaint found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }

    //Get the processInstanceId from the complaint and process
    fun getPIdByComplaintAndProcess(objectId: Long, process: String): HashMap<String, Any>?{
        val variables: HashMap<String, Any> = HashMap()
        try{
            var processInstanceId = ""
            if (process == msMarketSurveillanceProcessDefinitionKey){
                workPlanGenerateRepo.findByIdOrNull(objectId)?.let { workplan ->//Check that the complaint is valid
                    KotlinLogging.logger { }.trace("ObjectId : $objectId : Valid workplan found")

                    if (process == msMarketSurveillanceProcessDefinitionKey){
                        processInstanceId = workplan.msProcessInstanceId.toString()
                    }
                    variables["processInstanceId"] = processInstanceId
                    variables["workplan"] = workplan
                    return variables
                }
                KotlinLogging.logger { }.info("Workplan : $objectId : No workplan found")
            }
            if (process == msComplaintsProcessDefinitionKey) {
                complaintRepo.findByIdOrNull(objectId)?.let { complaint ->//Check that the complaint is valid
                    KotlinLogging.logger { }.trace("ObjectId : $objectId : Valid complaint found")

                    if (process == msComplaintsProcessDefinitionKey){
                        processInstanceId = complaint.msComplaintProcessInstanceId.toString()
                    }
                    variables["processInstanceId"] = processInstanceId
                    variables["complaint"] = complaint
                    return variables
                }
                KotlinLogging.logger { }.info("ComplaintId : $objectId : No complaint found")
            }
            if (process == msFuelMonitoringProcessDefinitionKey) {
                fuelInspectionRepo.findByIdOrNull(objectId)?.let { fir ->//Check that the fuel inspection is valid
                    KotlinLogging.logger { }.trace("ObjectId : $objectId : Valid fuel inspection found")
                    if (process == msFuelMonitoringProcessDefinitionKey){
                        processInstanceId = fir.msFuelProcessInstanceId.toString()
                    }
                    variables["processInstanceId"] = processInstanceId
                    variables["fuelInspection"] = fir
                    return variables
                }
                KotlinLogging.logger { }.info("ComplaintId : $objectId : No complaint found")
            }
        } catch (e: Exception){
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }

    fun fetchAllTasksByAssignee(assigneeId: Long): List<BpmnTaskDetails>? {
        return bpmnCommonFunctions.fetchAllTasksByAssignee(assigneeId,pidPrefix)
    }

    fun fetchAllTasks(): List<BpmnTaskDetails>? {
        return bpmnCommonFunctions.fetchAllTasks(pidPrefix)
    }

    //Update task variable by complaint Id and task Key
    fun updateTaskVariableByComplaintIdAndKey(complaintId: Long, taskDefinitionKey: String, process: String, parameterName: String, parameterValue: String): Boolean {
        try {
            getPIdByComplaintAndProcess(complaintId, process)?.let{
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

    fun msCompleteTask(complaintId: Long, taskDefinitionKey: String, process: String, notifyDirector:Boolean): HashMap<String, Any>? {
        KotlinLogging.logger { }.info("ComplaintId : $complaintId :  Completing task $taskDefinitionKey")
        try {
            getPIdByComplaintAndProcess(complaintId, process)?.let{
                bpmnCommonFunctions.getTaskByTaskDefinitionKey(it["processInstanceId"].toString(), taskDefinitionKey)?.let { task ->
                    //val currentAssignee = task.assignee.toLong()
                    if (notifyDirector){
                        //Alert the DI
                        bpmnCommonFunctions.getTaskVariables(task.id)?.let{ taskVariable->
                            notifications.sendEmailServiceTask(taskVariable["directorId"].toString().toLong(),task.assignee.toLong(),msDirectorUpdatesNotifId.toInt(),task,null)
                        }
                    }
                    bpmnCommonFunctions.completeTask(task.id)
                    //Thread.sleep(timerBoundaryTaskDelay)
                    it["assigneeId"] = task.assignee
                    it["taskId"] = task.id
                    it["task"] = task
                    return it
                }
            }
            KotlinLogging.logger { }.info("ComplaintId : $complaintId :  No complaint found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }

    fun msAssignTask(processInstanceId: String, taskDefinitionKey: String?, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("Assign next task begin")
        try {
            var localAssigneeId: String = assigneeId.toString()
            var task: Task? = null

            //complaint.let {
                taskDefinitionKey?.let{
                    task = bpmnCommonFunctions.getTaskByTaskDefinitionKey(processInstanceId, taskDefinitionKey)
                }?: run {
                    task = bpmnCommonFunctions.getTasks("processInstanceId", processInstanceId)?.get(0)
                }

                task?.let { task->
                    userRepo.findByIdOrNull(localAssigneeId.toLong())?.let { usersEntity ->
                        bpmnCommonFunctions.updateVariable(task.id, "email", usersEntity.email.toString())
                    }
                    //Refetch the task because we have updated the email variable
                    bpmnCommonFunctions.getTaskById(task.id)?.let { updatedTask->
                        updatedTask.assignee = localAssigneeId
                        //Adds 2 days (default duration) and sets this as the due date
                        updatedTask.dueDate = DateTime(Date()).plusDays(defaultDuration.toInt()).toDate()
                        taskService.saveTask(updatedTask)
                        KotlinLogging.logger { }.info("Task ${updatedTask.name} assigned to $localAssigneeId")
                        return true
                    }
                }
                //KotlinLogging.logger { }.info("ComplaintId : ${complaint.id} :  No task found"); return false
            //}
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return false
    }

    fun checkStartProcessInputs(objectId: Long, assigneeId: Long, processKey: String): HashMap<String, Any>? {
        val variables: HashMap<String, Any> = HashMap()
        KotlinLogging.logger { }.info("objectId : $objectId : Checking start process values")
        try {
            //Check that the object is valid
            if (processKey == msMarketSurveillanceProcessDefinitionKey ){
                //val workplan = workPlanGenerateRepo.findByIdOrNull(objectId)
                workPlanGenerateRepo.findById(objectId).let{ workPlanEntity->
                    if (workPlanEntity.isPresent){
                        workPlanEntity.get().let{
                            variables["workplan"] = it
                            it.msStatus?.let { status ->
                                /*
                                if (status != 0) {
                                    KotlinLogging.logger { }.info("workplanId : $objectId : workplan already has a ms task assigned"); return null
                                }
                                */
                            }
                        }
                    } else {
                        KotlinLogging.logger { }.info("objectId : $objectId : No market surveillance process found for id $objectId")
                        return null
                    }
                }
            }

            if (processKey == msComplaintsProcessDefinitionKey ) {
                //val complaint = complaintRepo.findByIdOrNull(objectId)
                complaintRepo.findById(objectId).let{ complaintEntity->
                    if (complaintEntity.isPresent){
                        complaintEntity.get().let {
                            variables["complaint"] = it
                            it.msComplaintStatus?.let { status ->
                                /*
                                if (status != 0) {
                                    KotlinLogging.logger { }.info("complaintId : $objectId : Complaint already has a ms complaint task assigned"); return null
                                }
                                 */
                            }
                        }
                    } else {
                        KotlinLogging.logger { }.info("complaintid : $objectId : No complaint found for id $objectId")
                        return null
                    }
                }
            }

            if (processKey == msFuelMonitoringProcessDefinitionKey ) {
                fuelInspectionRepo.findById(objectId).let{ fuelInspectionEntity->
                    if (fuelInspectionEntity.isPresent){
                        fuelInspectionEntity.get().let {
                            variables["fuelInspection"] = it
                            it.msFuelStatus?.let { status ->
                                /*
                                    if (status != 0) {
                                        KotlinLogging.logger { }.info("complaintId : $objectId : Fuel inspection already has a ms fuel monitoring task assigned"); return null
                                    }
                                 */
                            }
                        }

                    } else {
                        KotlinLogging.logger { }.info("fuelInspectionId : $objectId : No fuel inspection found for id $objectId")
                        return null
                    }
                }

            }
            //Check that the assignee is valid
            userRepo.findByIdOrNull(assigneeId)?.let { usersEntity ->
                variables["assigneeEmail"] = usersEntity.email.toString()
            }?: run{KotlinLogging.logger { }.info("objectId : $objectId : No user found for id $assigneeId"); return null}

            return variables

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
            return null
        }

    }

    /*
    ***********************************************************************************
    * MS COMPLAINT PROCESS
    ***********************************************************************************
     */
    //Start the MS Complaint process
    fun startMSComplaintProcess(complaintId: Long, assigneeId: Long, customerEmail: String): HashMap<String, String>? {
        var variables: HashMap<String, Any> = HashMap()
        KotlinLogging.logger { }.info("complaintId : $complaintId : Starting MS Complaint process")
        try {
            //Remember to start by setting email and assignee to manufacturer
            checkStartProcessInputs(complaintId, assigneeId, msComplaintsProcessDefinitionKey)?.let{ checkVariables->
                val complaint: ComplaintEntity = checkVariables["complaint"] as ComplaintEntity
                variables["objectId"] = complaint.id.toString()
                variables["complaintAccepted"] = 0
                variables["customerEmail"] = customerEmail

                bpmnCommonFunctions.startBpmnProcess(msComplaintsProcessDefinitionKey, msComplaintsBusinessKey, variables, assigneeId)?.let {
                    complaint.msComplaintProcessInstanceId = it["processInstanceId"]
                    complaint.msComplaintStartedOn = Timestamp.from(Instant.now())
                    complaint.msComplaintStatus = processStarted
                    complaintRepo.save(complaint)
                    KotlinLogging.logger { }.info("complaintId : $complaintId : Successfully started MS Complaint process")
                    return it
                }?: run{
                    KotlinLogging.logger { }.info("$complaintId : Unable to start MS Complaint process")
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }


    fun msSubmitComplaintComplete(complaintId: Long, hodAssigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("complaintId : $complaintId :  Submit complaint complete")
        msCompleteTask(complaintId, "msCoSubmitComplaint", msComplaintsProcessDefinitionKey,false)?.let {
            return msAssignTask(it["processInstanceId"].toString(), "msCoAcceptComplaint", hodAssigneeId)
        }
        return false
    }

    fun msAcceptComplaintComplete(complaintId: Long, hofAssigneeId: Long?, complaintAccepted: Boolean): Boolean {
        KotlinLogging.logger { }.info("complaintId : $complaintId :  Accept complaint complete")
        updateTaskVariableByComplaintIdAndKey(complaintId, "msCoAcceptComplaint", msComplaintsProcessDefinitionKey, "complaintAccepted", bpmnCommonFunctions.booleanToInt(complaintAccepted).toString())
        msCompleteTask(complaintId, "msCoAcceptComplaint", msComplaintsProcessDefinitionKey,false)?.let {
             if (complaintAccepted){   //complaint accepted
                if (hofAssigneeId != null) {
                    return  msAssignTask(it["processInstanceId"].toString(), "msCoAssignMsIO", hofAssigneeId)
                }
            } else {
                 return true
                //msAssignTask(it["permit"] as PermitApplicationEntity,it["processInstanceId"].toString(),"qaSfmiAnalyzeSamples",labAssigneeId)
            }
        }
        return false
    }

    fun msAssignMsioComplete(complaintId: Long, msioAssigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("complaintId : $complaintId :  Assign msio complete")
        msCompleteTask(complaintId, "msCoAssignMsIO", msComplaintsProcessDefinitionKey,false)?.let {
            Thread.sleep(3000)
            return msAssignTask(it["processInstanceId"].toString(), "msCoMSProcess", msioAssigneeId)
        }
        return false
    }

    fun msProcessComplete(complaintId: Long): Boolean {
        KotlinLogging.logger { }.info("complaintId : $complaintId :  MS Process complete")
        msCompleteTask(complaintId, "msCoMSProcess", msComplaintsProcessDefinitionKey,false)?.let {
            return true
        }
        return false
    }

    /*
    ***********************************************************************************
    * MS FUEL MONITORING PROCESS
    ***********************************************************************************
     */
    //Start the MS Fuel Inspection process
    fun startMSFuelMonitoringProcess(objectId: Long, assigneeId: Long, customerEmail: String, epraEmail: String, directorId: Long): HashMap<String, String>? {
        var variables: HashMap<String, Any> = HashMap()
        KotlinLogging.logger { }.info("FuelInspectionId : $objectId : Starting MS Fuel Monitoring process")
        try {
            //Remember to start by setting email and assignee to manufacturer
            checkStartProcessInputs(objectId, assigneeId, msFuelMonitoringProcessDefinitionKey)?.let{ checkVariables->
                val fuelInspection: MsFuelInspectionEntity = checkVariables["fuelInspection"] as MsFuelInspectionEntity
                variables["objectId"] = fuelInspection.id.toString()
                variables["labReportCompliant"] = 0
                variables["customerEmail"] = customerEmail
                variables["epraEmail"] = epraEmail
                variables["expectedTaskDuration"] = "PT7S"
                variables["directorId"] = directorId

                bpmnCommonFunctions.startBpmnProcess(msFuelMonitoringProcessDefinitionKey, msFuelMonitoringBusinessKey, variables, assigneeId)?.let {
                    fuelInspection.msFuelProcessInstanceId = it["processInstanceId"]
                    fuelInspection.msFuelStartedOn = Timestamp.from(Instant.now())
                    fuelInspection.msFuelStatus = processStarted
                    fuelInspectionRepo.save(fuelInspection)
                    KotlinLogging.logger { }.info("fuelInspectionId : $objectId : Successfully started MS Fuel Monitoring process")
                    //Schedule the notification
                    //msCreateScheduledAlert(it["processInstanceId"].toString(),it["taskId"].toString(), directorId)
                    return it
                }?: run{
                    KotlinLogging.logger { }.info("$objectId : Unable to start MS Fuel Monitoring process")
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }

    fun msFmHodAssignOfficerComplete(objectId: Long, officerAssigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  HOD assign officer complete")
        msCompleteTask(objectId, "msFmHodAssignOfficer", msFuelMonitoringProcessDefinitionKey,false)?.let {
            return msAssignTask(it["processInstanceId"].toString(), "msFmFIllSampleCollectionForm", officerAssigneeId)
        }
        return false
    }

    fun msFmFillSampleCollectionFormComplete(objectId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Fill sample collection forms complete")
        var currAssigneeId:Long = 0
        fetchTaskByComplaintId(objectId, msFuelMonitoringProcessDefinitionKey)?.let { taskDetails ->
            currAssigneeId = taskDetails[0].task.assignee.toLong()
        }
        msCompleteTask(objectId, "msFmFIllSampleCollectionForm", msFuelMonitoringProcessDefinitionKey,false)?.let {
            return msAssignTask(it["processInstanceId"].toString(), "msFmGenSSFAndLabSubmit", currAssigneeId)
        }
        return false
    }

    fun msFmGenSSFAndSubmitComplete(objectId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Generate SSF and submit complete")
        var currAssigneeId:Long = 0
        fetchTaskByComplaintId(objectId, msFuelMonitoringProcessDefinitionKey)?.let { taskDetails ->
            currAssigneeId = taskDetails[0].task.assignee.toLong()
        }
        msCompleteTask(objectId, "msFmGenSSFAndLabSubmit", msFuelMonitoringProcessDefinitionKey,false)?.let {
            return msAssignTask(it["processInstanceId"].toString(), "msFmUpdateBSNumberAndSubmit", currAssigneeId)
        }
        return false
    }

    fun msFmUpdateBSNumberAndSubmitComplete(objectId: Long, labAssigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Update BS Number and submit complete")
        msCompleteTask(objectId, "msFmUpdateBSNumberAndSubmit", msFuelMonitoringProcessDefinitionKey,false)?.let {
            return msAssignTask(it["processInstanceId"].toString(), "msFmLabProcessSamples", labAssigneeId)
        }
        return false
    }

    fun msFmLabProcessSamplesComplete(objectId: Long, officerAssigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Lab process samples complete")
        msCompleteTask(objectId, "msFmLabProcessSamples", msFuelMonitoringProcessDefinitionKey,false)?.let {
            return msAssignTask(it["processInstanceId"].toString(), "msFmLabReportCompliant", officerAssigneeId)
        }
        return false
    }

    fun msFmCheckLabReportComplete(objectId: Long, labReportCompliant: Boolean): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Check lab report complete")
        var currAssigneeId:Long = 0
        fetchTaskByComplaintId(objectId, msFuelMonitoringProcessDefinitionKey)?.let { taskDetails ->
            currAssigneeId = taskDetails[0].task.assignee.toLong()
        }
        updateTaskVariableByComplaintIdAndKey(objectId, "msFmLabReportCompliant", msFuelMonitoringProcessDefinitionKey, "labReportCompliant", bpmnCommonFunctions.booleanToInt(labReportCompliant).toString())
        msCompleteTask(objectId, "msFmLabReportCompliant", msFuelMonitoringProcessDefinitionKey,false)?.let {
            return if (labReportCompliant){   //lab report compliant
                msAssignTask(it["processInstanceId"].toString(), "msFmRemediate", currAssigneeId)
            } else {
                msAssignTask(it["processInstanceId"].toString(), "msFmSendProformaInvoice", currAssigneeId)
            }
        }
        return false
    }

    fun msFmSendProformaInvoiceComplete(objectId: Long, customerAssigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Send proforma invoice complete")
        msCompleteTask(objectId, "msFmSendProformaInvoice", msFuelMonitoringProcessDefinitionKey,false)?.let {
            return msAssignTask(it["processInstanceId"].toString(), "msFmPayment", customerAssigneeId)
        }
        return false
    }

    fun msFmPaymentComplete(objectId: Long, officerAssigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Fuel monitoring payment complete")
        msCompleteTask(objectId, "msFmPayment", msFuelMonitoringProcessDefinitionKey,false)?.let {
            return msAssignTask(it["processInstanceId"].toString(), "msFmRemediate", officerAssigneeId)
        }
        return false
    }

    fun msFmRemediationComplete(objectId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Fuel monitoring remediation complete")
        msCompleteTask(objectId, "msFmRemediate", msFuelMonitoringProcessDefinitionKey,false)?.let {
            return true
        }
        return false
    }

    fun endMsFuelMonitoringProcess(objectId: String) {
        KotlinLogging.logger { }.info("End MS Fuel monitoring for fuel inspection $objectId............")
        try{
            fuelInspectionRepo.findByIdOrNull(objectId.toLong())?.let { fir ->
                fir.msFuelCompletedOn = Timestamp.from(Instant.now())
                fir.msFuelStatus = processCompleted
                fuelInspectionRepo.save(fir)
            }
        } catch (e: Exception){
            KotlinLogging.logger { }.error("$objectId : Unable to complete MS Fuel monitoring process")
        }
    }

    /*
    ***********************************************************************************
    * MS MARKET SURVEILLANCE PROCESS
    ***********************************************************************************
     */
    //Start the MS market surveillance process

    fun startMSMarketSurveillanceProcess(objectId: Long, assigneeId: Long, complainantEmail: String, directorId:Long): HashMap<String, String>? {
        var variables: HashMap<String, Any> = HashMap()
        KotlinLogging.logger { }.info("objectId : $objectId : Starting MS Market Surveillance process")
        try {
            //Remember to start by setting email and assignee to manufacturer
            checkStartProcessInputs(objectId, assigneeId, msMarketSurveillanceProcessDefinitionKey)?.let{ checkVariables->
                val workPlan: MsWorkPlanGeneratedEntity = checkVariables["workplan"] as MsWorkPlanGeneratedEntity
                variables["objectId"] = workPlan.id.toString()
                variables["workPlanApproved"] = 0
                variables["hofPreliminaryReportApproved"] = 0
                variables["hodPreliminaryReportApproved"] = 0
                variables["hofFinalReportApproved"] = 0
                variables["hodFinalReportApproved"] = 0
                variables["complainantEmail"] = complainantEmail
                variables["directorId"] = directorId
                variables["msioAssigneeId"] = ""

                bpmnCommonFunctions.startBpmnProcess(msMarketSurveillanceProcessDefinitionKey, msMarketSurveillanceBusinessKey, variables, assigneeId)?.let {
                    workPlan.msProcessInstanceId = it["processInstanceId"]
                    workPlan.msStartedOn = Timestamp.from(Instant.now())
                    workPlan.msStatus = processStarted
                    workPlanGenerateRepo.save(workPlan)
                    KotlinLogging.logger { }.info("workplan : $objectId : Successfully started MS Market Surveillance process")
                    return it
                }?: run{
                    KotlinLogging.logger { }.info("$objectId : Unable to start MS Market Surveillance process")
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }

    fun msMsGenerateWorkplanComplete(objectId: Long, hodAssigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Generate workplan complete")
        msCompleteTask(objectId, "msGenerateWorkPlan", msMarketSurveillanceProcessDefinitionKey,true)?.let {
            return msAssignTask(it["processInstanceId"].toString(), "msApproveWorkPlan", hodAssigneeId)
        }
        return false
    }

    fun msMsApproveWorkplanComplete(objectId: Long, workPlanApproved: Boolean, msioAssigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Approve workplan complete")
        updateTaskVariableByComplaintIdAndKey(objectId, "msApproveWorkPlan", msMarketSurveillanceProcessDefinitionKey, "workPlanApproved", bpmnCommonFunctions.booleanToInt(workPlanApproved).toString())
        msCompleteTask(objectId, "msApproveWorkPlan", msMarketSurveillanceProcessDefinitionKey,false)?.let {
            return if (workPlanApproved){   //work plan approved
                msAssignTask(it["processInstanceId"].toString(), "msOnsiteMSActivities", msioAssigneeId)
            } else {
                msAssignTask(it["processInstanceId"].toString(), "msGenerateWorkPlan", msioAssigneeId)
            }
        }
        return false
    }

    fun msMsOnsiteActivitiesComplete(objectId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Onsite activities complete")
        var currAssigneeId:Long = 0
        fetchTaskByComplaintId(objectId, msMarketSurveillanceProcessDefinitionKey)?.let { taskDetails ->
            currAssigneeId = taskDetails[0].task.assignee.toLong()
        }
        msCompleteTask(objectId, "msOnsiteMSActivities", msMarketSurveillanceProcessDefinitionKey,false)?.let {
            msAssignTask(it["processInstanceId"].toString(), "msGenerateAndSubmitPreliminaryRpt", currAssigneeId)
            return msAssignTask(it["processInstanceId"].toString(), "msSubmitLabSamples", currAssigneeId)
        }
        return false
    }

    fun msMsSubmitLabSamplesComplete(objectId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Submit lab samples complete")
        var currAssigneeId:Long = 0
        fetchTaskByComplaintId(objectId, msMarketSurveillanceProcessDefinitionKey)?.let { taskDetails ->
            currAssigneeId = taskDetails[0].task.assignee.toLong()
        }
        msCompleteTask(objectId, "msSubmitLabSamples", msMarketSurveillanceProcessDefinitionKey,false)?.let {
            return msAssignTask(it["processInstanceId"].toString(), "msUpdateBSNumberAndSubmit", currAssigneeId)
        }
        return false
    }

    fun msMsUpdateBsNumberAndSubmitComplete(objectId: Long, labAssigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  MS Update BS number and submit complete")
        var currAssigneeId:Long = 0
        fetchTaskByComplaintId(objectId, msMarketSurveillanceProcessDefinitionKey)?.let { taskDetails ->
            currAssigneeId = taskDetails[0].task.assignee.toLong()
        }
        updateTaskVariableByComplaintIdAndKey(objectId, "msUpdateBSNumberAndSubmit", msMarketSurveillanceProcessDefinitionKey, "msioAssigneeId", currAssigneeId.toString())
        msCompleteTask(objectId, "msUpdateBSNumberAndSubmit", msMarketSurveillanceProcessDefinitionKey,false)?.let {
            //Create a scheduled notification here after 14 days
            val task = it["task"] as Task
            bpmnCommonFunctions.getTaskByTaskDefinitionKey(task.processInstanceId,"msLabProcessSamples")?.let { nextTask->
                schedulerImpl.createScheduledAlert(nextTask.processInstanceId,null,
                        nextTask.id,currAssigneeId,msMarketSurveillanceBusinessKey,
                        14,1,msLabResultsDelayedNotifId.toInt(),nextTask.taskDefinitionKey
                )
            }

            return msAssignTask(it["processInstanceId"].toString(), "msLabProcessSamples", labAssigneeId)
        }
        return false
    }

    fun msMsLabProcessSamplesComplete(objectId: Long, msioAssigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  MS lab process samples complete")
        var result:Boolean = false
        msCompleteTask(objectId, "msLabProcessSamples", msMarketSurveillanceProcessDefinitionKey,false)?.let {
            val task = it["task"] as Task
            //Cancel the notification
            schedulerImpl.cancelScheduledAlert(task.processInstanceId,task.taskDefinitionKey)
            //Because of the parallel gateway an index out of bounds exception is ok as the task may be waiting for another path
            try{
                result = msAssignTask(it["processInstanceId"].toString(), "msGenerateFinalReport", msioAssigneeId)
            } catch (e: IndexOutOfBoundsException) {
                //KotlinLogging.logger { }.error(e.message)
                KotlinLogging.logger { }.error("Workplan $objectId is waiting for inputs before it can proceed")
                return true
            }
            return result
        }
        return false
    }

    fun msGeneratePreliminaryReportComplete(objectId: Long, hofAssigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Generate preliminary report complete")
        msCompleteTask(objectId, "msGenerateAndSubmitPreliminaryRpt", msMarketSurveillanceProcessDefinitionKey,true)?.let {
            return msAssignTask(it["processInstanceId"].toString(), "msHofApprovePreliminaryReport", hofAssigneeId)
        }
        return false
    }

    fun msHOFApprovePreliminaryReportComplete(objectId: Long, msioAssigneeId: Long, hodAssigneeId: Long, hofPreliminaryReportApproved: Boolean): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  HOF approve preliminary report complete")
        updateTaskVariableByComplaintIdAndKey(objectId, "msHofApprovePreliminaryReport", msMarketSurveillanceProcessDefinitionKey, "hofPreliminaryReportApproved", bpmnCommonFunctions.booleanToInt(hofPreliminaryReportApproved).toString())
        msCompleteTask(objectId, "msHofApprovePreliminaryReport", msMarketSurveillanceProcessDefinitionKey,true)?.let {
            return if (hofPreliminaryReportApproved){   //preliminary report approved
                msAssignTask(it["processInstanceId"].toString(), "msHodApprovePreliminaryReport", hodAssigneeId)
            } else {
                msAssignTask(it["processInstanceId"].toString(), "msGenerateAndSubmitPreliminaryRpt", msioAssigneeId)
            }
        }
        return false
    }

    fun msHODApprovePreliminaryReportComplete(objectId: Long, msioAssigneeId: Long, hofAssigneeId: Long, hodPreliminaryReportApproved: Boolean): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  HOD approve preliminary report complete")
        var result:Boolean = false
        updateTaskVariableByComplaintIdAndKey(objectId, "msHodApprovePreliminaryReport", msMarketSurveillanceProcessDefinitionKey, "hodPreliminaryReportApproved", bpmnCommonFunctions.booleanToInt(hodPreliminaryReportApproved).toString())
        msCompleteTask(objectId, "msHodApprovePreliminaryReport", msMarketSurveillanceProcessDefinitionKey,true)?.let {
            return if (hodPreliminaryReportApproved){   //preliminary report approved
                //Because of the parallel gateway an index out of bounds exception is ok as the task may be waiting for another path
                result = try{
                    msAssignTask(it["processInstanceId"].toString(), "msGenerateFinalReport", msioAssigneeId)
                } catch (e: IndexOutOfBoundsException) {
                    //KotlinLogging.logger { }.error(e.message)
                    KotlinLogging.logger { }.error("Workplan $objectId is waiting for inputs before it can proceed")
                    true
                }
                result
            } else {
                msAssignTask(it["processInstanceId"].toString(), "msHofApprovePreliminaryReport", hofAssigneeId)
            }
        }
        return false
    }

    fun msGenerateFinalReportComplete(objectId: Long, hofAssigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Generate final report complete")
        msCompleteTask(objectId, "msGenerateFinalReport", msMarketSurveillanceProcessDefinitionKey,true)?.let {
            return msAssignTask(it["processInstanceId"].toString(), "msHofApproveFinalReport", hofAssigneeId)
        }
        return false
    }

    fun msHOFApproveFinalReportComplete(objectId: Long, msioAssigneeId: Long, hodAssigneeId: Long, hofFinalReportApproved: Boolean): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  HOF approve final report complete")
        updateTaskVariableByComplaintIdAndKey(objectId, "msHofApproveFinalReport", msMarketSurveillanceProcessDefinitionKey, "hofFinalReportApproved", bpmnCommonFunctions.booleanToInt(hofFinalReportApproved).toString())
        msCompleteTask(objectId, "msHofApproveFinalReport", msMarketSurveillanceProcessDefinitionKey,true)?.let {
            return if (hofFinalReportApproved){   //final report approved
                msAssignTask(it["processInstanceId"].toString(), "msHodApproveFinalReport", hodAssigneeId)
            } else {
                msAssignTask(it["processInstanceId"].toString(), "msGenerateFinalReport", msioAssigneeId)
            }
        }
        return false
    }

    fun msHODApproveFinalReportComplete(objectId: Long, msioAssigneeId: Long, hodFinalReportApproved: Boolean): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  HOD approve final report complete")
        var currAssigneeId:Long = 0
        fetchTaskByComplaintId(objectId, msMarketSurveillanceProcessDefinitionKey)?.let { taskDetails ->
            currAssigneeId = taskDetails[0].task.assignee.toLong()
        }
        updateTaskVariableByComplaintIdAndKey(objectId, "msHodApproveFinalReport", msMarketSurveillanceProcessDefinitionKey, "hodFinalReportApproved", bpmnCommonFunctions.booleanToInt(hodFinalReportApproved).toString())
        msCompleteTask(objectId, "msHodApproveFinalReport", msMarketSurveillanceProcessDefinitionKey,true)?.let {
            return if (hodFinalReportApproved){   //final report approved
                msAssignTask(it["processInstanceId"].toString(), "msActOnRecommendations", currAssigneeId)
            } else {
                msAssignTask(it["processInstanceId"].toString(), "msGenerateFinalReport", msioAssigneeId)
            }
        }
        return false
    }

    fun endMsMarketSurveillanceProcess(objectId: String) {
        KotlinLogging.logger { }.info("End MS Market surveillance for objectId $objectId............")
        try{
            workPlanGenerateRepo.findByIdOrNull(objectId.toLong())?.let { workplan ->
                workplan.msCompletedOn = Timestamp.from(Instant.now())
                workplan.msStatus = processCompleted
                workPlanGenerateRepo.save(workplan)
            }
        } catch (e: Exception){
            KotlinLogging.logger { }.error("$objectId : Unable to complete MS Market surveillance process")
        }
    }

}
