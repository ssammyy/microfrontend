package org.kebs.app.kotlin.apollo.api.ports.provided.bpmn

import mu.KotlinLogging
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.task.api.Task
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.di.BpmnTaskDetails
import org.kebs.app.kotlin.apollo.store.model.StandardLevyPaymentsEntity
import org.kebs.app.kotlin.apollo.store.repo.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.Instant

@Service
class StandardsLevyBpmn(private val taskService: TaskService,
                        private val runtimeService: RuntimeService,
                        private val userRepo: IUserRepository,
                        private val slPaymentsRepo: IStandardLevyPaymentsRepository,
                        private val companyProfileRepo: ICompanyProfileRepository,
                        private val manufacturerRepo: IManufacturerRepository,
                        private val slFactoryVisitReportRepo:IStandardLevyFactoryVisitReportRepository,
                        private val bpmnCommonFunctions: BpmnCommonFunctions) {
    @Value("\${bpmn.sl.registration.process.definition.key}")
    lateinit var slRegistrationProcessDefinitionKey: String

    @Value("\${bpmn.sl.registration..business.key}")
    lateinit var slRegistrationBusinessKey: String

    @Value("\${bpmn.sl.site.visit.process.definition.key}")
    lateinit var slSiteVisitProcessDefinitionKey: String

    @Value("\${bpmn.sl.site.visit..business.key}")
    lateinit var slSiteVisitBusinessKey: String

    val successMessage: String = "success"
    val processStarted:Int = 1
    val processCompleted:Int = 2

    fun fetchTaskByObjectId(objectId: Long,process:String): List<BpmnTaskDetails>? {
        try {
            getPIdByObjectAndProcess(objectId,process)?.let{
                val processInstanceId =  it["processInstanceId"].toString()
                bpmnCommonFunctions.getTasks("processInstanceId", processInstanceId)?.let { tasks ->
                    return bpmnCommonFunctions.generateTaskDetails(tasks)
                }?: run{ KotlinLogging.logger { }.info("ComplaintId : $objectId : No task found");return null}
            }
            KotlinLogging.logger { }.info("ComplaintId : $objectId : No complaint found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }

    //Get the processInstanceId from the slPayment and process
    fun getPIdByObjectAndProcess(objectId:Long,process:String): HashMap<String, Any>?{
        val variables: HashMap<String, Any> = HashMap()
        try{
            var processInstanceId = ""

                KotlinLogging.logger { }.trace("ObjectId : $objectId : Valid slPayment found")
                if (process == slRegistrationProcessDefinitionKey){
                    slPaymentsRepo.findByIdOrNull(objectId)?.let { slPayment ->//Check that the slPayment is valid
                        processInstanceId = slPayment.slRegistrationProcessInstanceId.toString()
                        variables["slPayment"] = slPayment
                    }
                }
                if (process == slSiteVisitProcessDefinitionKey){
                    slFactoryVisitReportRepo.findByIdOrNull(objectId)?.let { slFactoryVisitReport ->//Check that the sl factory visit report is valid
                        processInstanceId = slFactoryVisitReport.slProcessInstanceId.toString()
                        variables["slFactoryVisitReport"] = slFactoryVisitReport
                    }

                }
                variables["processInstanceId"] = processInstanceId

                return variables
            //}
            KotlinLogging.logger { }.info("slPayment : $objectId : No slPayment found")
        } catch (e:Exception){
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }

    fun fetchAllTasksByAssignee(assigneeId: Long): List<BpmnTaskDetails>? {
        try {
            taskService.createTaskQuery().taskAssignee(assigneeId.toString()).processDefinitionKeyLikeIgnoreCase("sl%").list()?.let{ tasks->
                return bpmnCommonFunctions.generateTaskDetails(tasks)
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }

    fun fetchAllTasks(): List<BpmnTaskDetails>? {
        try {
            taskService.createTaskQuery().processDefinitionKeyLikeIgnoreCase("sl%").list()?.let{ tasks->
                return bpmnCommonFunctions.generateTaskDetails(tasks)
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }

    //Update task variable by object Id and task Key
    fun updateTaskVariableByObjectIdAndKey(objectId: Long, taskDefinitionKey: String, process:String, parameterName:String, parameterValue: String): Boolean {
        try {
            getPIdByObjectAndProcess(objectId,process)?.let{
                bpmnCommonFunctions.getTaskByTaskDefinitionKey(it["processInstanceId"].toString(),taskDefinitionKey)?.let {task ->
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

    fun slCompleteTask(objectId: Long,taskDefinitionKey:String, process: String): HashMap<String, Any>? {
        KotlinLogging.logger { }.info("objectId : $objectId :  Completing task $taskDefinitionKey")
        try {
            getPIdByObjectAndProcess(objectId,process)?.let{
                bpmnCommonFunctions.getTaskByTaskDefinitionKey(it["processInstanceId"].toString(),taskDefinitionKey)?.let {task ->
                    bpmnCommonFunctions.completeTask(task.id)
                    it["assigneeId"] = task.assignee
                    return it
                }
            }
            KotlinLogging.logger { }.info("objectId : $objectId :  No complaint found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }

    fun slAssignTask(processInstanceId: String, taskDefinitionKey:String?, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("Assign next task begin")
        try {
            var localAssigneeId: String = assigneeId.toString()
            var task: Task? = null

            //complaint.let {
            taskDefinitionKey?.let{
                task = bpmnCommonFunctions.getTaskByTaskDefinitionKey(processInstanceId,taskDefinitionKey)
            }?: run {
                task = bpmnCommonFunctions.getTasks("processInstanceId",processInstanceId)?.get(0)
            }

            task?.let {task->
                userRepo.findByIdOrNull(localAssigneeId.toLong())?.let { usersEntity ->
                    bpmnCommonFunctions.updateVariable(task.id, "email", usersEntity.email.toString())
                }
                //Refetch the task because we have updated the email variable
                bpmnCommonFunctions.getTaskById(task.id)?.let { updatedTask->
                    updatedTask.assignee = localAssigneeId
                    taskService.saveTask(updatedTask)
                    KotlinLogging.logger { }.info("Task ${updatedTask.name} assigned to $localAssigneeId")
                    return true
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return false
    }

    fun checkStartProcessInputs(objectId: Long, assigneeId: Long, processKey:String): HashMap<String, Any>? {
        val variables: HashMap<String, Any> = HashMap()
        KotlinLogging.logger { }.info("objectId : $objectId : Checking start process values")
        try {
            //Check that the object is valid

                slPaymentsRepo.findByIdOrNull(objectId)?.let { //Check that the object is valid
                    slPayment ->
                    variables["slPayment"] = slPayment
                    if (processKey == slRegistrationProcessDefinitionKey) {
                        slPayment.slRegistrationStatus?.let { status ->
                            if (status != 0) {
                                KotlinLogging.logger { }.info("objectId : $objectId : object already has a sl registration task assigned"); return null
                            }
                        }
                    }

                    if (processKey == slSiteVisitProcessDefinitionKey) {
                        slPayment.slSiteVisitStatus?.let { status ->
                            if (status != 0) {
                                KotlinLogging.logger { }.info("objectId : $objectId : object already has a sl site visit task assigned"); return null
                            }
                        }
                    }

                } ?: run {KotlinLogging.logger { }.info("objectId : $objectId : No object found for id $objectId"); return null}

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
    * STANDARDS LEVY REGISTRATION PROCESS
    ***********************************************************************************
     */
    //Start the MS Complaint process
    fun startSlRegistrationProcess(objectId: Long,assigneeId:Long): HashMap<String, String>? {
        var variables: HashMap<String, Any> = HashMap()
        KotlinLogging.logger { }.info("objectId : $objectId : Starting SL Registration process")
        try {
            //Remember to start by setting email and assignee to manufacturer
            checkStartProcessInputs(objectId,assigneeId,slRegistrationProcessDefinitionKey)?.let{checkVariables->
                val slPayment: StandardLevyPaymentsEntity = checkVariables["slPayment"] as StandardLevyPaymentsEntity
                variables["objectId"] = slPayment.id.toString()
                variables["manufacturerId"] = assigneeId
                variables["isContractor"] = 0

                bpmnCommonFunctions.startBpmnProcess(slRegistrationProcessDefinitionKey, slRegistrationBusinessKey, variables, assigneeId)?.let {
                    slPayment.slRegistrationProcessInstanceId = it["processInstanceId"]
                    slPayment.slRegistrationStartedOn = Timestamp.from(Instant.now())
                    slPayment.slRegistrationStatus = processStarted
                    slPaymentsRepo.save(slPayment)
                    KotlinLogging.logger { }.info("objectId : $objectId : Successfully started SL Registration process")
                    return it
                }?: run{
                    KotlinLogging.logger { }.info("$objectId : Unable to start SL Registration process")
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }

    fun slManufacturerRegistrationComplete(objectId: Long, isContractor:Boolean): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Manufacturer registration complete")
        var currAssigneeId:Long = 0
        fetchTaskByObjectId(objectId, slRegistrationProcessDefinitionKey)?.let { taskDetails ->
            currAssigneeId = taskDetails[0].task.assignee.toLong()
        }
        updateTaskVariableByObjectIdAndKey(objectId,"slrRegister",slRegistrationProcessDefinitionKey,"isContractor",bpmnCommonFunctions.booleanToInt(isContractor).toString())
        slCompleteTask(objectId,"slrRegister",slRegistrationProcessDefinitionKey)?.let {
            return if (isContractor){   //Is a contractor
                slAssignTask(it["processInstanceId"].toString(),"slrFillSl1CForm",currAssigneeId)
            } else {
                slAssignTask(it["processInstanceId"].toString(),"slrFillSl1Form",currAssigneeId)
            }
        }
        return false
    }

    fun slFillSl1FormComplete(objectId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  FIll SL 1 Form complete")
        var currAssigneeId:Long = 0
        fetchTaskByObjectId(objectId, slRegistrationProcessDefinitionKey)?.let { taskDetails ->
            currAssigneeId = taskDetails[0].task.assignee.toLong()
        }
        slCompleteTask(objectId,"slrFillSl1Form",slRegistrationProcessDefinitionKey)?.let {
            return slAssignTask(it["processInstanceId"].toString(),"slrSubmitDetails",currAssigneeId)
        }
        return false
    }

    fun slFillSl1CFormComplete(objectId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  FIll SL 1 C Form complete")
        var currAssigneeId:Long = 0
        fetchTaskByObjectId(objectId, slRegistrationProcessDefinitionKey)?.let { taskDetails ->
            currAssigneeId = taskDetails[0].task.assignee.toLong()
        }
        slCompleteTask(objectId,"slrFillSl1CForm",slRegistrationProcessDefinitionKey)?.let {
            return slAssignTask(it["processInstanceId"].toString(),"slrSubmitDetails",currAssigneeId)
        }
        return false
    }

    fun slSubmitDetailsComplete(objectId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Submit details complete")
        var currAssigneeId:Long = 0
        fetchTaskByObjectId(objectId, slRegistrationProcessDefinitionKey)?.let { taskDetails ->
            currAssigneeId = taskDetails[0].task.assignee.toLong()
        }
        slCompleteTask(objectId,"slrSubmitDetails",slRegistrationProcessDefinitionKey)?.let {
            return true
        }
        return false
    }

    fun endSlRegistrationProcess(objectId:String) {
        KotlinLogging.logger { }.info("End SL Registration for object $objectId............")
        try{
            slPaymentsRepo.findByIdOrNull(objectId.toLong())?.let { slPayment ->
                slPayment.slRegistrationCompletedOn = Timestamp.from(Instant.now())
                slPayment.slRegistrationStatus = processCompleted
                slPaymentsRepo.save(slPayment)
            }
        } catch(e:Exception){
            KotlinLogging.logger { }.error("$objectId : Unable to complete SL Site Visit process")
        }
    }

    /*
    ***********************************************************************************
    * STANDARDS LEVY SITE VISIT PROCESS
    ***********************************************************************************
     */
    //Start the MS Complaint process
    fun startSlSiteVisitProcess(objectId: Long,assigneeId:Long): HashMap<String, String>? {
        var variables: HashMap<String, Any> = HashMap()
        KotlinLogging.logger { }.info("objectId : $objectId : Starting SL Site Visit process")
        try {
            //Remember to start by setting email and assignee to manufacturer
            checkStartProcessInputs(objectId,assigneeId,slSiteVisitProcessDefinitionKey)?.let{checkVariables->
                val slPayment: StandardLevyPaymentsEntity = checkVariables["slPayment"] as StandardLevyPaymentsEntity
                variables["objectId"] = slPayment.id.toString()
                variables["manufacturerId"] = assigneeId
                variables["approvedAsstMgr"] = 0
                variables["approvedMgr"] = 0

                bpmnCommonFunctions.startBpmnProcess(slSiteVisitProcessDefinitionKey, slSiteVisitBusinessKey, variables, assigneeId)?.let {
                    slPayment.slSiteVisitProcessInstanceId = it["processInstanceId"]
                    slPayment.slSiteVisitStartedOn = Timestamp.from(Instant.now())
                    slPayment.slSiteVisitStatus = processStarted
                    slPaymentsRepo.save(slPayment)
                    KotlinLogging.logger { }.info("objectId : $objectId : Successfully started SL Site Visit process")
                    return it
                }?: run{
                    KotlinLogging.logger { }.info("$objectId : Unable to start SL Site Visit process")
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }

    fun getSlsvProcessInstanceId(objectId: Long):String{
        slPaymentsRepo.findByIdOrNull(objectId)?.let {slpEntity->
            return slpEntity.slSiteVisitProcessInstanceId.toString()
        }
        return ""
    }

    fun slsvQueryManufacturerDetailsComplete(objectId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Query Manufacturer details complete")
        var currAssigneeId:Long = 0
        fetchTaskByObjectId(objectId, slSiteVisitProcessDefinitionKey)?.let { taskDetails ->
            currAssigneeId = taskDetails[0].task.assignee.toLong()
        }

        slCompleteTask(objectId,"sLSvQueryManufacturerDetails",slSiteVisitProcessDefinitionKey)?.let {
            Thread.sleep(3000)
            return slAssignTask(it["processInstanceId"].toString(),"sLsVScheduleVisit",currAssigneeId)
        }
        return false
    }

    fun slsvScheduleVisitComplete(objectId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Schedule Visit complete")
        var currAssigneeId:Long = 0
        fetchTaskByObjectId(objectId, slSiteVisitProcessDefinitionKey)?.let { taskDetails ->
            currAssigneeId = taskDetails[0].task.assignee.toLong()
        }

        slCompleteTask(objectId,"sLsVScheduleVisit",slSiteVisitProcessDefinitionKey)?.let {
            return slAssignTask(it["processInstanceId"].toString(),"sLsVPrepareVisitReport",currAssigneeId)
        }
        return false
    }

    fun slsvPrepareVisitReportComplete(objectId: Long, assistantManagerId:Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Prepare visit report complete")
        slCompleteTask(objectId,"sLsVPrepareVisitReport",slSiteVisitProcessDefinitionKey)?.let {
            return slAssignTask(it["processInstanceId"].toString(),"sLsVApproveReportAssMgr",assistantManagerId)
        }
        return false
    }

    fun slsvApproveReportAsstManagerComplete(objectId: Long, managerId:Long,approvedAsstMgr:Boolean): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Assistant manager approve report complete")
        var currAssigneeId:Long = 0
        fetchTaskByObjectId(objectId, slSiteVisitProcessDefinitionKey)?.let { taskDetails ->
            currAssigneeId = taskDetails[0].task.assignee.toLong()
        }
        updateTaskVariableByObjectIdAndKey(objectId,"sLsVApproveReportAssMgr",slSiteVisitProcessDefinitionKey,"approvedAsstMgr",bpmnCommonFunctions.booleanToInt(approvedAsstMgr).toString())
        slCompleteTask(objectId,"sLsVApproveReportAssMgr",slSiteVisitProcessDefinitionKey)?.let {
            return if (approvedAsstMgr){   //Approved by assistant manager
                slAssignTask(it["processInstanceId"].toString(),"sLsVApproveReportMgr",managerId)
            } else {
                slAssignTask(it["processInstanceId"].toString(),"sLsVPrepareVisitReport",currAssigneeId)
            }
        }
        return false
    }

    fun slsvApproveReportManagerComplete(objectId: Long, principalOfficerId:Long,approvedMgr:Boolean): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Manager approve report complete")
        var currAssigneeId:Long = 0
        fetchTaskByObjectId(objectId, slSiteVisitProcessDefinitionKey)?.let { taskDetails ->
            currAssigneeId = taskDetails[0].task.assignee.toLong()
        }
        updateTaskVariableByObjectIdAndKey(objectId,"sLsVApproveReportMgr",slSiteVisitProcessDefinitionKey,"approvedMgr",bpmnCommonFunctions.booleanToInt(approvedMgr).toString())
        slCompleteTask(objectId,"sLsVApproveReportMgr",slSiteVisitProcessDefinitionKey)?.let {
            return if (approvedMgr){   //Approved by manager
                slAssignTask(it["processInstanceId"].toString(),"sLsVDraftFeedbackAndShare",principalOfficerId)
            } else {
                slAssignTask(it["processInstanceId"].toString(),"sLsVApproveReportAssMgr",currAssigneeId)
            }
        }
        return false
    }

    fun slsvDraftFeedbackComplete(objectId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Draft feedback complete")
        slCompleteTask(objectId,"sLsVDraftFeedbackAndShare",slSiteVisitProcessDefinitionKey)?.let {
            return true
        }
        return false
    }

    fun endSlSiteVisitProcess(objectId:String) {
        KotlinLogging.logger { }.info("End SL Site Visit for object $objectId............")
        try{
            slPaymentsRepo.findByIdOrNull(objectId.toLong())?.let { slPayment ->
                slPayment.slSiteVisitCompletedOn = Timestamp.from(Instant.now())
                slPayment.slSiteVisitStatus = processCompleted
                slPaymentsRepo.save(slPayment)
            }
        } catch(e:Exception){
            KotlinLogging.logger { }.error("$objectId : Unable to complete SL Site Visit process")
        }
    }
}