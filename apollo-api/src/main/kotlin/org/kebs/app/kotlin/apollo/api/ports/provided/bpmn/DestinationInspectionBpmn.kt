package org.kebs.app.kotlin.apollo.api.ports.provided.bpmn

import mu.KotlinLogging
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ConsignmentDocumentDao
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.request.ConsignmentUpdateRequest
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.di.BpmnTaskDetails
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.di.DiTaskDetails
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.api.service.ConsignmentApprovalStatus
import org.kebs.app.kotlin.apollo.api.service.ConsignmentDocumentAuditService
import org.kebs.app.kotlin.apollo.api.service.ConsignmentDocumentStatus
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.CdDemandNoteEntity
import org.kebs.app.kotlin.apollo.store.model.ServiceMapsEntity
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.di.CdInspectionMotorVehicleItemChecklistEntity
import org.kebs.app.kotlin.apollo.store.model.di.CdItemDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.di.ConsignmentDocumentDetailsEntity
import org.kebs.app.kotlin.apollo.store.repo.ICdItemDetailsRepo
import org.kebs.app.kotlin.apollo.store.repo.IUserRepository
import org.kebs.app.kotlin.apollo.store.repo.di.IConsignmentDocumentDetailsRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.Instant
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.set


@Service
class DestinationInspectionBpmn(
        private val taskService: TaskService,
        private val runtimeService: RuntimeService,
        private val userRepo: IUserRepository,
        private val bpmnCommonFunctions: BpmnCommonFunctions,
        private val iConsignmentDocumentDetailsRepo: IConsignmentDocumentDetailsRepository,
        private val iCdItemDetailsRepo: ICdItemDetailsRepo,
        private val daoServices: DestinationInspectionDaoServices,
        private val auditService: ConsignmentDocumentAuditService,
        private val applicationMapProperties: ApplicationMapProperties,
        private val commonDaoServices: CommonDaoServices,
) {

    @Value("\${bpmn.di.mv.cor.process.definition.key}")
    lateinit var diMvWithCorProcessDefinitionKey: String

    @Value("\${bpmn.di.mv.cor.business.key}")
    lateinit var diMvWithCorBusinessKey: String

    @Value("\${bpmn.di.mv.inspection.process.definition.key}")
    lateinit var diMvInspectionProcessDefinitionKey: String

    val processStarted: Int = 1
    val processCompleted: Int = 2
    val pidPrefix = "di"

    fun startMinistryInspection(saved: CdInspectionMotorVehicleItemChecklistEntity, detail: CdItemDetailsEntity) {
        val data = mutableMapOf<String, Any?>()
        data.put("mvInspectionId", saved.id)
        data.put("stationId", saved.ministryStationId?.id)
        data.put("stationName", saved.ministryStationId?.stationName)
        data.put("cdItemId", detail.id)
        data.put("ministry", saved.ministryStationId?.stationName ?: "ALL")

        val processInstance = runtimeService.startProcessInstanceByKey("ministryInspectionProcess", data)
        // Update CD Item details
        detail.varField7 = processInstance.processDefinitionId
        detail.varField8 = processStarted.toString()
        detail.varField9 = Timestamp.from(Instant.now()).toString()
        detail.varField10 = "Submitted to ministry"
        detail.varField1 = processInstance.processDefinitionId
        detail.status = ConsignmentApprovalStatus.WAITING.code
        // Update CD status
        detail.cdDocId?.let {
            it.varField7 = processInstance.processDefinitionId
            it.varField8 = processStarted.toString()
            it.varField9 = Timestamp.from(Instant.now()).toString()
            it.varField10 = "Submitted to ministry"
            daoServices.updateCDStatus(it, ConsignmentDocumentStatus.MINISTRY_REQUEST)
        } ?: this.daoServices.updateCdItemDetailsInDB(detail, null)
    }

    fun startAssignmentProcesses(data: MutableMap<String, Any?>, consignmentDocument: ConsignmentDocumentDetailsEntity) {
        KotlinLogging.logger { }.info("Assign inspection officer to consignment: ${Thread.currentThread().name}")
        data.put("cfs_code", consignmentDocument.freightStation?.cfsCode)
        val processInstance = runtimeService.startProcessInstanceByKey("assignInspectionOfficer", data)
        val updatedDocument = this.daoServices.findCD(consignmentDocument.id!!)
        updatedDocument.diProcessInstanceId = processInstance.processDefinitionId
        updatedDocument.diProcessStatus = 0
        updatedDocument.diProcessStartedOn = Timestamp.from(Instant.now())
        updatedDocument.varField10 = "Start Assign Inspection Officer"
        // Update document
        this.daoServices.updateCdDetailsInDB(updatedDocument, this.commonDaoServices.getLoggedInUser())
    }

    // Check for modification disabled status
    fun modifyDisabled(consignmentDocument: ConsignmentDocumentDetailsEntity, response: ApiResponseModel): Boolean {
        if (consignmentDocument.oldCdStatus != null) {
            return true
        }
        consignmentDocument.approveRejectCdStatusType?.let {
            if (it.finalStatus == 1) {
                response.responseCode = ResponseCodes.INVALID_CODE
                response.message = "Consignment with status '${it.typeName}' cannot be modified"
                return true
            }
        }
        return false
    }

    fun startApprovalConsignment(cdUuid: String, form: ConsignmentUpdateRequest): ApiResponseModel {
        val response = ApiResponseModel()
        var consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
        if (modifyDisabled(consignmentDocument, response)) {
            return response
        }
        // Check already approved
        val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
        KotlinLogging.logger { }.info { "approveRejectCdStatusType = ${form.cdStatusTypeId}" }
        val cdStatusType = form.cdStatusTypeId?.let { daoServices.findCdStatusValue(it) }
        if (cdStatusType != null) {
            val status: ConsignmentDocumentStatus
            when (cdStatusType.category) {
                "APPROVE" -> {
                    status = ConsignmentDocumentStatus.APPROVE_REQUEST
                }
                "REJECT" -> {
                    status = ConsignmentDocumentStatus.REJECT_REQUEST
                }
                "QUERY" -> {
                    status = ConsignmentDocumentStatus.QUERY_REQUEST
                }
                "AMENDMENT" -> {
                    status = ConsignmentDocumentStatus.REJ_AMEND_REQUEST
                }
                "ONHOLD" -> {
                    status = ConsignmentDocumentStatus.ON_HOLD
                }
                else -> throw ExpectedDataNotFound("Invalid transaction status: ${cdStatusType.category}")
            }
            // Update process status
            consignmentDocument = daoServices.updateCDStatus(consignmentDocument, status)
            // Add process variables
            KotlinLogging.logger { }.info("Updated the process status to: ${status.name}")
            val processProperties = mutableMapOf<String, Any?>()
            val loggedInUser = this.commonDaoServices.getLoggedInUser()
            processProperties.put("cdUuid", cdUuid)
            processProperties.put("supervisor", consignmentDocument.assigner?.userName
                    ?: loggedInUser?.userName)
            processProperties.put("cfs_code", consignmentDocument.freightStation?.cfsCode)
            processProperties.put("remarks", form.remarks)
            processProperties.put("activeStatus", map.activeStatus)
            processProperties.put("cdStatusTypeId", form.cdStatusTypeId ?: 0)
            processProperties.put("consignmentDocumentApproved", cdStatusType.category == "APPROVED")
            // Attach Process to consignment'
            KotlinLogging.logger { }.info("Starting bpm process: ${cdStatusType.category}")
            val processInstance = runtimeService.startProcessInstanceByKey("updateConsignmentStatus", processProperties)
            Thread.sleep(5000)
            consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
            consignmentDocument.varField9 = processInstance.processDefinitionId
            consignmentDocument.varField8 = processInstance.id
            consignmentDocument.diProcessInstanceId = processInstance.id
            consignmentDocument.diProcessStatus = map.activeStatus
            consignmentDocument.modifiedOn = commonDaoServices.getTimestamp()

            this.daoServices.updateCdDetailsInDB(consignmentDocument, this.commonDaoServices.getLoggedInUser())

            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Success"
        } else {
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Please select approval status"
        }
        return response
    }

    fun consignmentDocumentProcessUpdate(taskId: String, data: MutableMap<String, Any?>, consignmentDocument: ConsignmentDocumentDetailsEntity): ApiResponseModel {
        val response = ApiResponseModel()
        // Check already approved
        try {
            taskService.complete(taskId, data)
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Task updated successfully"
        } catch (ex: ExpectedDataNotFound) {
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = ex.localizedMessage
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("FAILED TO COMPLETE TASK", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Task update failed"
        }
        return response
    }

    fun startTargetConsignment(map: ServiceMapsEntity, data: MutableMap<String, Any?>, consignmentDocument: ConsignmentDocumentDetailsEntity) {
        data.put("cfs_code", consignmentDocument.freightStation?.cfsCode)
        val processInstance = runtimeService.startProcessInstanceByKey("targetConsignmentDocument", data)
        consignmentDocument.diProcessInstanceId = processInstance.processDefinitionId
        consignmentDocument.diProcessStatus = map.activeStatus
        consignmentDocument.diProcessStartedOn = Timestamp.from(Instant.now())
        consignmentDocument.varField10 = "Request Targeting"
        consignmentDocument.targetStatus = map.initStatus
        consignmentDocument.targetReason = data.get("remarks") as String?
        consignmentDocument.status = ConsignmentApprovalStatus.WAITING.code
        // Save process details
        this.auditService.addHistoryRecord(consignmentDocument.id!!, consignmentDocument.ucrNumber, data["remarks"] as String?, "REQUEST CONSIGNMENT TARGETING", "Consignment targeting request")
        this.commonDaoServices.getLoggedInUser()?.let { it1 -> this.daoServices.updateCdDetailsInDB(consignmentDocument, it1) }
        // Update status to wait targeting approval
        daoServices.updateCDStatus(consignmentDocument, ConsignmentDocumentStatus.TARGET_REQUEST)
    }

    fun startCompliantProcess(map: ServiceMapsEntity, data: MutableMap<String, Any?>, consignmentDocument: ConsignmentDocumentDetailsEntity) {
        data.put("cfs_code", consignmentDocument.freightStation?.cfsCode)
        val processInstance = runtimeService.startProcessInstanceByKey("consignmentCompliantApprovalProcess", data)
        consignmentDocument.diProcessInstanceId = processInstance.processDefinitionId
        consignmentDocument.diProcessStatus = map.activeStatus
        consignmentDocument.localCocOrCorStatus = map.initStatus
        consignmentDocument.compliantStatus = map.initStatus
        consignmentDocument.diProcessStartedOn = Timestamp.from(Instant.now())
        consignmentDocument.diProcessCompletedOn = null
        consignmentDocument.varField10 = "REQUEST COMPLIANCE APPROVAL"
        consignmentDocument.status = ConsignmentApprovalStatus.WAITING.code
        // Save process details
        val updateCd = daoServices.updateCDStatus(consignmentDocument, ConsignmentDocumentStatus.COMPLIANCE_REQUEST)
        this.auditService.addHistoryRecord(consignmentDocument.id!!, consignmentDocument.ucrNumber, data["remarks"] as String?, "REQUEST CONSIGNMENT COMPLIANCE UPDATE", "Request to mark Consignment as compliant")
        // Update current process
        this.commonDaoServices.getLoggedInUser()?.let { it1 -> this.daoServices.updateCdDetailsInDB(updateCd, it1) }
    }

    fun startBlacklist(data: MutableMap<String, Any?>, consignmentDocument: ConsignmentDocumentDetailsEntity) {
        data.put("cfs_code", consignmentDocument.freightStation?.cfsCode)
        val processInstance = runtimeService.startProcessInstanceByKey("blacklistUser", data)
        consignmentDocument.diProcessInstanceId = processInstance.processDefinitionId
        consignmentDocument.diProcessStatus = 1
        consignmentDocument.diProcessStartedOn = Timestamp.from(Instant.now())
        consignmentDocument.varField8 = processInstance.id
        // Save process details
        val updateCd = daoServices.updateCDStatus(consignmentDocument, ConsignmentDocumentStatus.BLACKLIST_REQUEST)
        this.auditService.addHistoryRecord(consignmentDocument.id!!, consignmentDocument.ucrNumber, data["remarks"] as String?, "REQUEST CONSIGNMENT BLACKLISTING", "Consignment blacklist request")
        // Update current process
        this.commonDaoServices.getLoggedInUser()?.let { it1 -> this.daoServices.updateCdDetailsInDB(updateCd, it1) }
    }

    fun startGenerateCoC(map: ServiceMapsEntity, data: MutableMap<String, Any?>, consignmentDocument: ConsignmentDocumentDetailsEntity) {
        data.put("cfs_code", consignmentDocument.freightStation?.cfsCode)
        val processInstance = runtimeService.startProcessInstanceByKey("cocApprovalProcess", data)
        consignmentDocument.diProcessInstanceId = processInstance.processDefinitionId
        consignmentDocument.diProcessStatus = map.activeStatus
        consignmentDocument.localCocOrCorStatus = map.initStatus
        consignmentDocument.diProcessStartedOn = Timestamp.from(Instant.now())
        consignmentDocument.targetReason = data.get("remarks") as String?
        // Save process details
        this.auditService.addHistoryRecord(consignmentDocument.id!!, consignmentDocument.ucrNumber, data["remarks"] as String?, "REQUEST CONSIGNMENT COC/COI", "Consignment CoC/CoI request")
        this.commonDaoServices.getLoggedInUser()?.let { it1 -> this.daoServices.updateCdDetailsInDB(consignmentDocument, it1) }
    }

    fun startGenerateCor(map: ServiceMapsEntity, data: MutableMap<String, Any?>, consignmentDocument: ConsignmentDocumentDetailsEntity) {
        data.put("cfs_code", consignmentDocument.freightStation?.cfsCode)
        val processInstance = runtimeService.startProcessInstanceByKey("corApprovalProcess", data)
        consignmentDocument.diProcessInstanceId = processInstance.processDefinitionId
        consignmentDocument.diProcessStatus = map.activeStatus
        consignmentDocument.localCocOrCorStatus = map.initStatus
        consignmentDocument.diProcessStartedOn = Timestamp.from(Instant.now())
        consignmentDocument.targetReason = data.get("remarks") as String?
        // Save process details
        this.auditService.addHistoryRecord(consignmentDocument.id!!, consignmentDocument.ucrNumber, data["remarks"] as String?, "REQUEST CONSIGNMENT COR", "Consignment CoR request")
        this.daoServices.updateCdDetailsInDB(consignmentDocument, null)
    }

    fun startGenerateDemandNote(map: ServiceMapsEntity, data: MutableMap<String, Any?>, consignmentDocument: ConsignmentDocumentDetailsEntity) {
        data.put("cfs_code", consignmentDocument.freightStation?.cfsCode)
        // Update CD
        val updateCd = daoServices.updateCDStatus(consignmentDocument, ConsignmentDocumentStatus.PAYMENT_REQUEST)
        // Start process
        val processInstance = runtimeService.startProcessInstanceByKey("demandNoteGenerationProcess", data)
        updateCd.diProcessInstanceId = processInstance.processDefinitionId
        updateCd.diProcessStatus = map.activeStatus
        updateCd.sendDemandNote = map.initStatus
        updateCd.status = ConsignmentApprovalStatus.WAITING.code
        updateCd.varField10 = "DEMAND NOTE SUBMITTED AWAITING APPROVAL"
        updateCd.diProcessStartedOn = Timestamp.from(Instant.now())
        updateCd.targetReason = data.get("remarks") as String?
        // Save process details
        this.auditService.addHistoryRecord(updateCd.id!!, updateCd.ucrNumber, data["remarks"] as String?, "DEMAND NOTE CONSIGNMENT", "Consignment demand note request")
        this.daoServices.updateCdDetailsInDB(updateCd, null)
    }

    private fun getTaskDetails(tasks: MutableList<org.flowable.task.api.Task>?): List<DiTaskDetails> {
        val taskDetails: MutableList<DiTaskDetails> = ArrayList()
        if (tasks != null) {
            for (task in tasks) {
                val properties = this.taskService.getVariables(task.id)
                taskDetails.add(DiTaskDetails(task.id, task.name, task.createTime, task.description, properties))
            }
        }
        return taskDetails
    }

    fun consignmentDocumentWithActions(loggedInUser: UsersEntity, category: String?, myTasks: Boolean, page: PageRequest): ApiResponseModel {
        val response = ApiResponseModel()
        // 1. Get tasks from BPM
        val actionsQuery = taskService.createTaskQuery()
                .active()
                .processVariableExists("cdUuid")
        if (myTasks) {
            actionsQuery.taskOwner(loggedInUser.userName)
        }
        if (!category.isNullOrEmpty()) {
            actionsQuery.taskCategory(category)
        }
        // 2. Get task details
//        val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
//        val userProfilesEntity =commonDaoServices.findUserProfileByUserID(loggedInUser, map.activeStatus)
//        val codes = daoServices.findAllCFSUserCodes(userProfilesEntity.id ?: 0L)
        val consignmentActions = actionsQuery
                .orderByTaskCreateTime().desc()
                .listPage(page.offset.toInt(), page.pageSize)
        val taskDetails = getTaskDetails(consignmentActions)
        // 3. add to list
        val resultSet = mutableSetOf<String>()
        for (task in taskDetails) {
            resultSet.add(task.map.get("cdUuid") as String)
        }
        // 4. Query for CD with UUIDs
        response.data = ConsignmentDocumentDao.fromList(daoServices.findCDWithUuids(resultSet))
        response.pageNo = page.pageNumber
        response.totalCount = consignmentActions.size.toLong()
        response.responseCode = ResponseCodes.SUCCESS_CODE
        response.message = "Success"
        return response
    }

    fun consignmentDocumentActions(cdUuid: String): List<DiTaskDetails> {
        val consignmentActions = taskService.createTaskQuery()
                .active() // Load active tasks only
                .orderByTaskCreateTime().desc()
                .processVariableValueEquals("cdUuid", cdUuid) // Tasks with parameter cdUuid
                .list()
        return getTaskDetails(consignmentActions)
    }

    fun listUserTasks(): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            val auth = commonDaoServices.loggedInUserAuthentication()
            val loggedInUser = this.commonDaoServices.getLoggedInUser()
            var tasks: MutableList<org.flowable.task.api.Task> = mutableListOf()
            when {
                auth.authorities.stream().anyMatch { authority -> authority.authority == "DI_OFFICER_CHARGE_READ" } -> {
                    val userProfilesEntity = loggedInUser?.let { commonDaoServices.findUserProfileByUserID(it, map.activeStatus) }
                    val codes = daoServices.findAllCFSUserCodes(userProfilesEntity?.id ?: 0L)
                    KotlinLogging.logger { }.info("CFS CODES: $codes")
//                    taskService.createTaskQuery().taskAssignee(loggedInUser?.userName)
                    // Load tasks in supervisors cfs id
                    for (c in codes) {
                        tasks.addAll(taskService.createTaskQuery().taskCategory(c).listPage(0, 30))
                        if (tasks.size > 30) {
                            break
                        }
                    }
                }
                else -> {
                    tasks = taskService.createTaskQuery().taskOwner(loggedInUser?.userName).list()
                }
            }
            response.data = getTaskDetails(tasks)
            response.message = "Success"
            response.responseCode = ResponseCodes.SUCCESS_CODE
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("TEST", ex)
            response.message = "Request failed, please try again"
            response.responseCode = ResponseCodes.FAILED_CODE
        }
        return response
    }

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

    //Trigger demand note paid bpm task
    fun triggerDemandNotePaidBpmTask(demandNote: CdDemandNoteEntity): Boolean {
        //Get Item from Demand Note
        val data = hashMapOf<String, Any?>()
        data["demandNoteId"] = demandNote.id
        data["cdId"] = demandNote.cdId
        data["receiptNumber"] = demandNote.receiptNo
        data["demandNoteNumber"] = demandNote.demandNoteNumber
        val processInstance = runtimeService.startProcessInstanceByKey("demandNotePaymentProcess", data)
        demandNote.varField1 = processInstance.processDefinitionId
        daoServices.upDateDemandNote(demandNote)
        return false
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
            } else if (processKey == diMvInspectionProcessDefinitionKey) {
                iCdItemDetailsRepo.findByIdOrNull(objectId)?.let { cdItemDetailsEntity ->
                    variables["cdItemDetailsEntity"] = cdItemDetailsEntity
                    when (processKey) {
                        diMvInspectionProcessDefinitionKey -> {
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
        val variables: HashMap<String, Any> = HashMap()
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
                return if (supervisorTarget == 1) {
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
                return if (cdDecision == "APPROVE" || cdDecision == "REJECT") {
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
        try {
            iConsignmentDocumentDetailsRepo.findByIdOrNull(objectId.toLong())?.let { consignmentDocumentDetailsEntity ->
                consignmentDocumentDetailsEntity.diProcessCompletedOn = Timestamp.from(Instant.now())
                consignmentDocumentDetailsEntity.diProcessStatus = processCompleted
                iConsignmentDocumentDetailsRepo.save(consignmentDocumentDetailsEntity)
            }
        } catch (e: Exception) {
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
            KotlinLogging.logger { }.info("CD : $objectId : Valid CD found")
            cdEntity.diProcessInstanceId?.let { it ->
                processInstanceId = it
            }
        }
        KotlinLogging.logger { }.info("Process Instance ID: $processInstanceId")

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
            } catch (e: Exception) {
                KotlinLogging.logger { }.error("An error occurred on Trigger Inspection schedule request task to complete ", e)
            }
        }
    }

    //Check payment required
    fun diPaymentRequiredComplete(objectId: Long, assigneeId: Long, paymentRequired: Boolean): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Payment Required Complete")
        updateTaskVariableByObjectIdAndKey(
                objectId,
                "MotorInspectionPaymentRequired", diMvWithCorProcessDefinitionKey,
                "paymentRequired", bpmnCommonFunctions.booleanToInt(paymentRequired).toString()
        )
        fetchTaskByObjectId(objectId, diMvWithCorProcessDefinitionKey)?.let { taskDetails ->
            dICompleteTask(objectId, "MotorInspectionPaymentRequired", diMvWithCorProcessDefinitionKey)?.let {
                return if (paymentRequired) {
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
            KotlinLogging.logger { }.info("CD : $objectId : Valid CD found")
            cdEntity.diProcessInstanceId?.let { it ->
                processInstanceId = it
            }
        }
        KotlinLogging.logger { }.info("Process Instance ID: $processInstanceId")
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
            } catch (e: Exception) {
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
    fun diMinistryInspectionRequiredComplete(objectId: Long, assigneeId: Long, forwardToMinistry: Boolean): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId :  Check Ministry Inspection Required Complete")
        updateTaskVariableByObjectIdAndKey(
                objectId,
                "MotorInspectionMinistryInspectionRequired", diMvWithCorProcessDefinitionKey,
                "forwardToMinistry", bpmnCommonFunctions.booleanToInt(forwardToMinistry).toString()
        )
        fetchTaskByObjectId(objectId, diMvWithCorProcessDefinitionKey)?.let { taskDetails ->
            dICompleteTask(objectId, "MotorInspectionMinistryInspectionRequired", diMvWithCorProcessDefinitionKey)?.let {
                return if (forwardToMinistry) {
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
    fun diReviewMotorInspectionReportComplete(objectId: Long, assigneeId: Long, motorInspectionReportApproved: Boolean): Boolean {
        KotlinLogging.logger { }.info("objectId : $objectId : Review Motor Inspection Report Complete")
        updateTaskVariableByObjectIdAndKey(
                objectId,
                "MotorInspectionReviewMotorInspectionReport", diMvWithCorProcessDefinitionKey,
                "motorInspectionReportApproved", bpmnCommonFunctions.booleanToInt(motorInspectionReportApproved).toString()
        )
        fetchTaskByObjectId(objectId, diMvWithCorProcessDefinitionKey)?.let { taskDetails ->
            dICompleteTask(objectId, "MotorInspectionReviewMotorInspectionReport", diMvWithCorProcessDefinitionKey)?.let {
                return if (motorInspectionReportApproved) {
                    return true
                } else {
                    bpmnCommonFunctions.assignTask(it["processInstanceId"].toString(), "MotorInspectionGenerateMotorInspectionReport", assigneeId)
                }
            }
        }
        return false
    }

    fun deleteTask(pathVariable: String): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val task = taskService.createTaskQuery().taskId(pathVariable)
                    .singleResult()
            runtimeService.deleteProcessInstance(task.processInstanceId, "Not necessary")
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Success"
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("FAILED to DELETE", ex)
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Failed to delete task"
        }
        return response
    }

}
