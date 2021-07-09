package org.kebs.app.kotlin.apollo.api.ports.provided.bpmn

import mu.KotlinLogging
import org.flowable.engine.TaskService
import org.flowable.task.api.Task
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.di.BpmnTaskDetails
import org.kebs.app.kotlin.apollo.common.exceptions.InvalidValueException
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.store.model.SlUpdatecompanyDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.StandardLevyFactoryVisitReportEntity
import org.kebs.app.kotlin.apollo.store.model.StandardLevyPaymentsEntity
import org.kebs.app.kotlin.apollo.store.repo.ISlUpdatecompanyDetailsEntityRepository
import org.kebs.app.kotlin.apollo.store.repo.IStandardLevyFactoryVisitReportRepository
import org.kebs.app.kotlin.apollo.store.repo.IStandardLevyPaymentsRepository
import org.kebs.app.kotlin.apollo.store.repo.IUserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.Instant

@Service
class StandardsLevyBpmn(
    private val taskService: TaskService,
    private val userRepo: IUserRepository,
    private val slPaymentsRepo: IStandardLevyPaymentsRepository,
    private val slFactoryVisitReportRepo: IStandardLevyFactoryVisitReportRepository,
    private val slUpdatecompanyDetailsRepo: ISlUpdatecompanyDetailsEntityRepository,
    private val bpmnCommonFunctions: BpmnCommonFunctions
) {
    @Value("\${bpmn.sl.registration.process.definition.key}")
    lateinit var slRegistrationProcessDefinitionKey: String

    @Value("\${bpmn.sl.registration..business.key}")
    lateinit var slRegistrationBusinessKey: String

    @Value("\${bpmn.sl.site.visit.process.definition.key}")
    lateinit var slSiteVisitProcessDefinitionKey: String

    @Value("\${bpmn.sl.site.visit..business.key}")
    lateinit var slSiteVisitBusinessKey: String

    @Value("\${bpmn.sl.update.company.details.process.definition.key}")
    lateinit var slUpdateCompanyDetailsProcessDefinitionKey: String

    @Value("\${bpmn.sl.update.company.details.business.key}")
    lateinit var slUpdateCompanyDetailsBusinessKey: String

    val successMessage: String = "success"
    val processStarted: Int = 1
    val processCompleted: Int = 2

    fun fetchTaskByObjectId(objectId: Long, process: String): List<BpmnTaskDetails>? {
        try {
            getPIdByObjectAndProcess(objectId, process)?.let {
                val processInstanceId = it["processInstanceId"].toString()
                bpmnCommonFunctions.getTasks("processInstanceId", processInstanceId)?.let { tasks ->
                    return bpmnCommonFunctions.generateTaskDetails(tasks)
                } ?: run { KotlinLogging.logger { }.info("ObjectId : $objectId : No task found");return null }
            }
            KotlinLogging.logger { }.info("ObjectId : $objectId : No object found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }

    //Get the processInstanceId from the slPayment and process
    fun getPIdByObjectAndProcess(objectId: Long, process: String): HashMap<String, Any>? {
        val variables: HashMap<String, Any> = HashMap()
        try {
            var processInstanceId = ""

            KotlinLogging.logger { }.trace("ObjectId : $objectId : Valid slPayment found")
            if (process == slRegistrationProcessDefinitionKey) {
                slPaymentsRepo.findByIdOrNull(objectId)?.let { slPayment ->//Check that the slPayment is valid
                    processInstanceId = "${slPayment.slRegistrationProcessInstanceId}"
                    variables["slPayment"] = slPayment
                }
            }
            if (process == slSiteVisitProcessDefinitionKey) {
                slFactoryVisitReportRepo.findByIdOrNull(objectId)
                    ?.let { slFactoryVisitReport ->//Check that the sl factory visit report is valid
                        processInstanceId = slFactoryVisitReport.slProcessInstanceId ?: throw NullValueNotAllowedException("No process id on the site visit")
                        variables["slFactoryVisitReport"] = slFactoryVisitReport
                    }

            }
            variables["processInstanceId"] = processInstanceId

            return variables
            //}
            KotlinLogging.logger { }.info("slPayment : $objectId : No slPayment found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }

    fun fetchAllTasksByAssignee(assigneeId: Long): List<BpmnTaskDetails>? {
        try {
            taskService.createTaskQuery().taskAssignee("$assigneeId").processDefinitionKeyLikeIgnoreCase("sl%")
                .list()?.let { tasks ->
                    return bpmnCommonFunctions.generateTaskDetails(tasks)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }

    fun fetchAllTasks(): List<BpmnTaskDetails>? {
        try {
            taskService.createTaskQuery().processDefinitionKeyLikeIgnoreCase("sl%").list()?.let { tasks ->
                return bpmnCommonFunctions.generateTaskDetails(tasks)
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
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

    fun slCompleteTask(objectId: Long, taskDefinitionKey: String, process: String): HashMap<String, Any>? {
        KotlinLogging.logger { }.info("objectId : $objectId :  Completing task $taskDefinitionKey")
        try {
            getPIdByObjectAndProcess(objectId, process)?.let {
                bpmnCommonFunctions.getTaskByTaskDefinitionKey(it["processInstanceId"].toString(), taskDefinitionKey)
                    ?.let { task ->
                        bpmnCommonFunctions.completeTask(task.id)
                        //task.processVariables["principalOfficerId"]
                        //return hashMapOf(Pair("assigneeId", task.assignee))
                        it["assigneeId"] = task.assignee
                        return it
                    }
            }
            KotlinLogging.logger { }.info("objectId : $objectId :  No object found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }

    fun slAssignTask(processInstanceId: String, taskDefinitionKey: String?, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("Assign next task begin")
        try {
            var task: Task? = null
            taskDefinitionKey?.let {
                task = bpmnCommonFunctions.getTaskByTaskDefinitionKey(processInstanceId, taskDefinitionKey)
            } ?: run {
                task = bpmnCommonFunctions.getTasks("processInstanceId", processInstanceId)?.get(0)
            }

            task?.let {
                userRepo.findByIdOrNull(assigneeId)?.let { usersEntity ->
                    bpmnCommonFunctions.updateVariable(it.id, "email", usersEntity.email ?: throw NullValueNotAllowedException("No email address defined for user id =${usersEntity.id}"))
                }
                //Re-fetch the task because we have updated the email variable
                bpmnCommonFunctions.getTaskById(it.id)?.let { updatedTask ->
                    updatedTask.assignee = "$assigneeId"
                    taskService.saveTask(updatedTask)
                    KotlinLogging.logger { }.info("Task ${updatedTask.name} assigned to $assigneeId")
                    return true
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return false
    }

    fun checkStartProcessInputs(objectId: Long, assigneeId: Long, processKey: String): HashMap<String, Any>? {
        val variables: HashMap<String, Any> = HashMap()
        KotlinLogging.logger { }.info("objectId : $objectId : Checking start process values")
        try {
            when (processKey) {
                slRegistrationProcessDefinitionKey -> {
                    slPaymentsRepo.findByIdOrNull(objectId)
                        ?.let { slPayment ->
                            if (slPayment.slSiteVisitStatus ?: 0 != 0) {
                                throw InvalidValueException("The visit is already scheduled and in process")
                            }
                            variables["slPayment"] = slPayment
                        }
                        ?: throw InvalidValueException("objectId : $objectId : No object found for id $objectId")
                }
                else -> {
                    slFactoryVisitReportRepo.findByIdOrNull(objectId)
                        ?.let { slFactoryVisit ->
                            variables["slFactoryVisit"] = slFactoryVisit
                            if (slFactoryVisit.slStatus ?: 0 != 0) {
                                throw InvalidValueException("The visit is already scheduled and in process")
                            }
                        }
                        ?: throw InvalidValueException("objectId : $objectId : No object found for id $objectId")
                }

            }
            userRepo.findByIdOrNull(assigneeId)
                ?.let { variables["assigneeEmail"] = it.email ?: throw InvalidValueException("User id=${it.id} does not have an email address") }
                ?: throw InvalidValueException("objectId : $objectId : No user found for id $assigneeId")
            return variables

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            return null

        }

    }

    /*
    ***********************************************************************************
    * STANDARDS LEVY REGISTRATION PROCESS
    ***********************************************************************************
     */
    //Start the Standards Levy registration process
    fun startSlRegistrationProcess(objectId: Long, assigneeId: Long): HashMap<String, String>? {
        var variables: HashMap<String, Any> = HashMap()
        KotlinLogging.logger { }.info("objectId : $objectId : Starting SL Registration process")
        try {
            //Remember to start by setting email and assignee to manufacturer
            checkStartProcessInputs(objectId, assigneeId, slRegistrationProcessDefinitionKey)?.let { checkVariables ->
                val slPayment: StandardLevyPaymentsEntity = checkVariables["slPayment"] as StandardLevyPaymentsEntity
                variables["objectId"] = "${slPayment.id}"
                variables["manufacturerId"] = assigneeId
                variables["isContractor"] = 0

                bpmnCommonFunctions.startBpmnProcess(
                    slRegistrationProcessDefinitionKey,
                    slRegistrationBusinessKey,
                    variables,
                    assigneeId
                )?.let {
                    slPayment.slRegistrationProcessInstanceId = it["processInstanceId"]
                    slPayment.slRegistrationStartedOn = Timestamp.from(Instant.now())
                    slPayment.slRegistrationStatus = processStarted
                    slPaymentsRepo.save(slPayment)
                    KotlinLogging.logger { }.info("objectId : $objectId : Successfully started SL Registration process")
                    return it
                } ?: run {
                    KotlinLogging.logger { }.info("$objectId : Unable to start SL Registration process")
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }

    fun slManufacturerRegistrationComplete(objectId: Long, isContractor: Boolean): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Manufacturer registration complete")
        var currAssigneeId: Long = 0
        fetchTaskByObjectId(objectId, slRegistrationProcessDefinitionKey)?.let { taskDetails ->
            currAssigneeId = taskDetails[0].task.assignee.toLongOrNull() ?: 54
        }
        updateTaskVariableByObjectIdAndKey(
            objectId,
            "slrRegister",
            slRegistrationProcessDefinitionKey,
            "isContractor",
            "${bpmnCommonFunctions.booleanToInt(isContractor)}"
        )
        slCompleteTask(objectId, "slrRegister", slRegistrationProcessDefinitionKey)?.let {
            return if (isContractor) {   //Is a contractor
                slAssignTask(it["processInstanceId"].toString(), "slrFillSl1CForm", currAssigneeId)
            } else {
                slAssignTask(it["processInstanceId"].toString(), "slrFillSl1Form", currAssigneeId)
            }
        }
        return false
    }

    fun slFillSl1FormComplete(objectId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  FIll SL 1 Form complete")
        var currAssigneeId: Long = 0
        fetchTaskByObjectId(objectId, slRegistrationProcessDefinitionKey)?.let { taskDetails ->
            currAssigneeId = taskDetails[0].task.assignee.toLongOrNull() ?: 54
        }
        slCompleteTask(objectId, "slrFillSl1Form", slRegistrationProcessDefinitionKey)?.let {
            return slAssignTask(it["processInstanceId"].toString(), "slrSubmitDetails", currAssigneeId)
        }
        return false
    }

    fun slFillSl1CFormComplete(objectId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  FIll SL 1 C Form complete")
        var currAssigneeId: Long = 0
        fetchTaskByObjectId(objectId, slRegistrationProcessDefinitionKey)?.let { taskDetails ->
            currAssigneeId = taskDetails[0].task.assignee.toLongOrNull() ?: 54
        }
        slCompleteTask(objectId, "slrFillSl1CForm", slRegistrationProcessDefinitionKey)?.let {
            return slAssignTask(it["processInstanceId"].toString(), "slrSubmitDetails", currAssigneeId)
        }
        return false
    }

    fun slSubmitDetailsComplete(objectId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Submit details complete")
        var currAssigneeId: Long = 0
        fetchTaskByObjectId(objectId, slRegistrationProcessDefinitionKey)?.let { taskDetails ->
            currAssigneeId = taskDetails[0].task.assignee.toLongOrNull() ?: 54
        }
        slCompleteTask(objectId, "slrSubmitDetails", slRegistrationProcessDefinitionKey)?.let {
            return true
        }
        return false
    }

    fun endSlRegistrationProcess(objectId: String) {
        KotlinLogging.logger { }.info("End SL Registration for object $objectId............")
        try {
            slPaymentsRepo.findByIdOrNull(objectId.toLong())?.let { slPayment ->
                slPayment.slRegistrationCompletedOn = Timestamp.from(Instant.now())
                slPayment.slRegistrationStatus = processCompleted
                slPaymentsRepo.save(slPayment)
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("$objectId : Unable to complete SL Site Visit process")
        }
    }

    /*
    ***********************************************************************************
    * STANDARDS LEVY SITE VISIT PROCESS
    ***********************************************************************************
     */
    //Start the SL Site visit process
    fun startSlSiteVisitProcess(objectId: Long, assigneeId: Long): HashMap<String, String>? {
        var variables: HashMap<String, Any> = HashMap()
        KotlinLogging.logger { }.info("objectId : $objectId : Starting SL Site Visit process")
        try {
            //Remember to start by setting email and assignee to manufacturer
            checkStartProcessInputs(objectId, assigneeId, slSiteVisitProcessDefinitionKey)?.let { checkVariables ->
                val slFactoryVisit: StandardLevyFactoryVisitReportEntity =
                    checkVariables["slFactoryVisit"] as StandardLevyFactoryVisitReportEntity
                variables["objectId"] = slFactoryVisit.id ?: throw NullValueNotAllowedException("Id should not be null")
                variables["manufacturerId"] = assigneeId
                variables["approvedAsstMgr"] = 0
                variables["approvedMgr"] = 0
                variables["principalOfficerId"] = 0
                variables["manufacturerId"] = 0

                bpmnCommonFunctions.startBpmnProcess(
                    slSiteVisitProcessDefinitionKey,
                    slSiteVisitBusinessKey,
                    variables,
                    assigneeId
                )?.let {
                    slFactoryVisit.slProcessInstanceId = it["processInstanceId"]
                    slFactoryVisit.slStartedOn = Timestamp.from(Instant.now())
                    slFactoryVisit.slStatus = processStarted
                    slFactoryVisitReportRepo.save(slFactoryVisit)
                    KotlinLogging.logger { }.info("objectId : $objectId : Successfully started SL Site Visit process")
                    return it
                } ?: run {
                    KotlinLogging.logger { }.info("$objectId : Unable to start SL Site Visit process")
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }

    fun slSvQueryManufacturerDetailsComplete(objectId: Long, manufacturerId:String): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Query Manufacturer details complete")
        var currAssigneeId: Long = 0
        updateTaskVariableByObjectIdAndKey(objectId, "sLSvQueryManufacturerDetails", slSiteVisitProcessDefinitionKey, "manufacturerId", manufacturerId)
        fetchTaskByObjectId(objectId, slSiteVisitProcessDefinitionKey)
            ?.let { taskDetails ->
                if (taskDetails.isEmpty()) {
                    throw NullValueNotAllowedException("No task found")
                } else {
                    currAssigneeId = taskDetails[0].task.assignee.toLong()
                }
            }

        slCompleteTask(objectId, "sLSvQueryManufacturerDetails", slSiteVisitProcessDefinitionKey)?.let {
            Thread.sleep(3000)
            return slAssignTask(it["processInstanceId"].toString(), "sLsVScheduleVisit", currAssigneeId)
        }
        return false
    }

    fun slSvScheduleVisitComplete(objectId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Schedule Visit complete")
        var currAssigneeId: Long = 0
        fetchTaskByObjectId(objectId, slSiteVisitProcessDefinitionKey)?.let { taskDetails ->
            currAssigneeId = taskDetails[0].task.assignee?.toLongOrNull() ?: 54
            updateTaskVariableByObjectIdAndKey(objectId, "sLsVScheduleVisit", slSiteVisitProcessDefinitionKey, "principalOfficerId", currAssigneeId.toString())
//            currAssigneeId = taskDetails[0].task.assignee?.toLongOrNull() ?: throw InvalidValueException("Assignee Id is not valid")
        }

        slCompleteTask(objectId, "sLsVScheduleVisit", slSiteVisitProcessDefinitionKey)?.let {
            return slAssignTask(it["processInstanceId"].toString(), "sLsVPrepareVisitReport", currAssigneeId)
        }
        return false
    }

    fun slsvPrepareVisitReportComplete(objectId: Long, assistantManagerId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Prepare visit report complete")
        slCompleteTask(objectId, "sLsVPrepareVisitReport", slSiteVisitProcessDefinitionKey)?.let {
            return slAssignTask(it["processInstanceId"].toString(), "sLsVApproveReportAssMgr", assistantManagerId)
        }
        return false
    }

    fun slsvApproveReportAsstManagerComplete(objectId: Long, managerId: Long, approvedAsstMgr: Boolean): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Assistant manager approve report complete")
        updateTaskVariableByObjectIdAndKey(
            objectId,
            "sLsVApproveReportAssMgr",
            slSiteVisitProcessDefinitionKey,
            "approvedAsstMgr",
            bpmnCommonFunctions.booleanToInt(approvedAsstMgr).toString()
        )

        slCompleteTask(objectId, "sLsVApproveReportAssMgr", slSiteVisitProcessDefinitionKey)?.let {
            return if (approvedAsstMgr) {   //Approved by assistant manager
                slAssignTask(it["processInstanceId"].toString(), "sLsVApproveReportMgr", managerId)
            } else {
                val principalOfficerId = bpmnCommonFunctions.getProcessVariables(it["processInstanceId"].toString())?.get("principalOfficerId").toString().toLong()
                slAssignTask(it["processInstanceId"].toString(), "sLsVPrepareVisitReport", principalOfficerId)
            }
        }
        return false
    }

    fun slsvApproveReportManagerComplete(objectId: Long, principalOfficerId: Long, approvedMgr: Boolean): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Manager approve report complete")
        var currAssigneeId: Long = 0
        fetchTaskByObjectId(objectId, slSiteVisitProcessDefinitionKey)?.let { taskDetails ->
            currAssigneeId = taskDetails[0].task.assignee.toLongOrNull() ?: 54
        }
        updateTaskVariableByObjectIdAndKey(
            objectId,
            "sLsVApproveReportMgr",
            slSiteVisitProcessDefinitionKey,
            "approvedMgr",
            "${bpmnCommonFunctions.booleanToInt(approvedMgr)}"
        )
        slCompleteTask(objectId, "sLsVApproveReportMgr", slSiteVisitProcessDefinitionKey)?.let {
            return if (approvedMgr) {   //Approved by manager
                slAssignTask(it["processInstanceId"].toString(), "sLsVDraftFeedbackAndShare", principalOfficerId)
            } else {
                slAssignTask(it["processInstanceId"].toString(), "sLsVApproveReportAssMgr", currAssigneeId)
            }
        }
        return false
    }

    fun slsvDraftFeedbackComplete(objectId: Long, manufactureUserId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Draft feedback complete")
        slCompleteTask(objectId, "sLsVDraftFeedbackAndShare", slSiteVisitProcessDefinitionKey)?.let {
            return true
        }
        return false
    }

    fun endSlSiteVisitProcess(objectId: String) {
        KotlinLogging.logger { }.info("End SL Site Visit for object $objectId............")
        try {
            slFactoryVisitReportRepo.findByIdOrNull(objectId.toLong())?.let { slFactoryVisit ->
                slFactoryVisit.slCompletedOn = Timestamp.from(Instant.now())
                slFactoryVisit.slStatus = processCompleted
                slFactoryVisitReportRepo.save(slFactoryVisit)
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("$objectId : Unable to complete SL Site Visit process")
        }
    }

    /*
    ***********************************************************************************
    * STANDARDS UPDATE MANUFACTURER DETAILS PROCESS
    ***********************************************************************************
     */
    //Start the SL Site visit process
    fun startSlUmdProcess(objectId: Long, assigneeId: Long): HashMap<String, String>? {
        var variables: HashMap<String, Any> = HashMap()
        KotlinLogging.logger { }.info("objectId : $objectId : Starting SL Update Company Details process")
        try {
            //Remember to start by setting email and assignee to manufacturer
            checkStartProcessInputs(objectId, assigneeId, slUpdateCompanyDetailsProcessDefinitionKey)?.let { checkVariables ->
                val slUpdatecompanyDetails: SlUpdatecompanyDetailsEntity =
                    checkVariables["slUpdateCompanyDetails"] as SlUpdatecompanyDetailsEntity
                variables["objectId"] = slUpdatecompanyDetails.id ?: throw NullValueNotAllowedException("Id should not be null")
                variables["approvedAsstMgr"] = 0
                variables["manufacturerId"] = 0

                bpmnCommonFunctions.startBpmnProcess(
                    slUpdateCompanyDetailsProcessDefinitionKey,
                    slUpdateCompanyDetailsBusinessKey,
                    variables,
                    assigneeId
                )?.let {
                    slUpdatecompanyDetails.slUpdateProcessInstanceId = it["processInstanceId"]
                    slUpdatecompanyDetails.slUpdateStartedOn = Timestamp.from(Instant.now())
                    slUpdatecompanyDetails.slUpdateStatus = processStarted
                    slUpdatecompanyDetailsRepo.save(slUpdatecompanyDetails)
                    KotlinLogging.logger { }.info("objectId : $objectId : Successfully started SL Update Company process")
                    return it
                } ?: run {
                    KotlinLogging.logger { }.info("$objectId : Unable to start SL Update Company Details process")
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }

    fun slUmdOfficerUpdateDetailsComplete(objectId: Long, assistantManagerId: Long): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Officer update manufacturer details complete")
        slCompleteTask(objectId, "slUmdOfficerUpdate", slUpdateCompanyDetailsProcessDefinitionKey)?.let {
            return slAssignTask(it["processInstanceId"].toString(), "slUmdAssistantManagerApprove", assistantManagerId)
        }
        return false
    }

    fun slUmdApproveAsstManagerComplete(objectId: Long, managerId: Long, approvedAsstMgr: Boolean): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Assistant manager approve complete")
        updateTaskVariableByObjectIdAndKey(
            objectId,
            "slUmdAssistantManagerApprove",
            slUpdateCompanyDetailsProcessDefinitionKey,
            "approvedAsstMgr",
            bpmnCommonFunctions.booleanToInt(approvedAsstMgr).toString()
        )

        slCompleteTask(objectId, "slUmdAssistantManagerApprove", slUpdateCompanyDetailsProcessDefinitionKey)?.let {
            return if (approvedAsstMgr) {   //Approved by assistant manager
                slAssignTask(it["processInstanceId"].toString(), "slUmdManagerApprove", managerId)
            } else {
                return true
            }
        }
        return false
    }

    fun endSlUpdateCompanyDetailsProcess(objectId: String) {
        KotlinLogging.logger { }.info("End SL Update Company Details for object $objectId............")
        try {
            slUpdatecompanyDetailsRepo.findByIdOrNull(objectId.toLong())?.let { slUpdateCompanyDetails ->
                slUpdateCompanyDetails.slUpdateCompletedOn = Timestamp.from(Instant.now())
                slUpdateCompanyDetails.slUpdateStatus = processCompleted
                slUpdatecompanyDetailsRepo.save(slUpdateCompanyDetails)
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("$objectId : Unable to complete SL Update Company Details process")
        }
    }
}
