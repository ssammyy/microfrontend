package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std

//import com.apollo.standardsdevelopment.models.*
//import com.apollo.standardsdevelopment.repositories.*
//import com.beust.klaxon.Klaxon

//import com.google.gson.stream.JsonReader

import com.beust.klaxon.JsonReader
import com.beust.klaxon.Klaxon
import mu.KotlinLogging
import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.engine.history.HistoricActivityInstance
import org.flowable.engine.repository.Deployment
import org.flowable.task.api.Task
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.web.config.EmailConfig
import org.kebs.app.kotlin.apollo.common.dto.std.ID
import org.kebs.app.kotlin.apollo.common.dto.std.ProcessInstanceResponseValue
import org.kebs.app.kotlin.apollo.common.dto.std.StandardsDto
import org.kebs.app.kotlin.apollo.common.dto.std.TaskDetails
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.IUserRepository
import org.kebs.app.kotlin.apollo.store.repo.IUserRoleAssignmentsRepository
import org.kebs.app.kotlin.apollo.store.repo.IUserRolesRepository
import org.kebs.app.kotlin.apollo.store.repo.std.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.repository.findByIdOrNull
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.StringReader
import java.sql.Timestamp
import java.util.*


@Service
class StandardRequestService(
    private val runtimeService: RuntimeService,
    private val taskService: TaskService,
    @Qualifier("processEngine") private val processEngine: ProcessEngine,
    private val repositoryService: RepositoryService,
    private val standardRequestRepository: StandardRequestRepository,
    private val standardNWIRepository: StandardNWIRepository,
    private val standardJustificationRepository: StandardJustificationRepository,
    private val standardWorkPlanRepository: StandardWorkPlanRepository,
    private val productRepository: ProductRepository,
    private val departmentRepository: DepartmentRepository,
    private val technicalCommitteeRepository: TechnicalCommitteeRepository,
    private val productSubCategoryRepository: ProductSubCategoryRepository,
    private val hofFeedbackRepository: HOFFeedbackRepository,
    private val rankingCriteriaRepository: RankingCriteriaRepository,
    private val liaisonOrganizationRepository: LiaisonOrganizationRepository,
    private val emailConfig: EmailConfig,
    private val voteOnNWIRepository: VoteOnNWIRepository,
    private val decisionJustificationRepository: DecisionJustificationRepository,
    private val commonDaoServices: CommonDaoServices,
    private val sdNwaUploadsEntityRepository: StandardsDocumentsRepository,
    private val notifications: Notifications,
    private val draftDocumentService: DraftDocumentService,
    private val iUserRoleAssignmentsRepository: IUserRoleAssignmentsRepository,
    private val userRolesAssignRepo: IUserRoleAssignmentsRepository,
    private val userRolesRepo: IUserRolesRepository,
    private val usersRepo: IUserRepository,
    private val sdDocumentsRepository: StandardsDocumentsRepository,

    ) {

    val PROCESS_DEFINITION_KEY = "requestModule"
    val TASK_CANDIDATE_GROUP_HOF = "HOF"
    val TASK_CANDIDATE_GROUP_TC_SEC = "TC-sec"
    val TASK_CANDIDATE_GROUP_TC = "TC"
    val TASK_CANDIDATE_GROUP_SPC_SEC = "SPC-sec"
    val variable: MutableMap<String, Any> = HashMap()

    fun deployProcessDefinition(): Deployment = repositoryService
        .createDeployment()
        .addClasspathResource("processes/std/request_module.bpmn20.xml")
        .deploy()


    fun requestForStandard(standardRequest: StandardRequest): ProcessInstanceResponseValue {

        val variables: MutableMap<String, Any> = HashMap()


        standardRequest.name?.let { variables.put("name", it) }
        standardRequest.createdBy?.let { variables.put("name", it) }

        standardRequest.email?.let { variables.put("email", it) }
        standardRequest.phone?.let { variables.put("phone", it) }
//        standardRequest.tcId?.let { variables.put("tcId", it) }
        //standardRequest.tcName?.let { variables.put("tcName", technicalCommitteeRepository.findNameById(standardRequest.tcId?.toLong())) }
        standardRequest.departmentId?.let { variables.put("departmentId", it) }
//        standardRequest.productSubCategoryId?.let { variables.put("productSubCategoryId", it) }
//        standardRequest.productId?.let { variables.put("productId", it) }

        standardRequest.submissionDate = Timestamp(System.currentTimeMillis())
        standardRequest.createdOn = Timestamp(System.currentTimeMillis())

        variables["departmentName"] = departmentRepository.findNameById(standardRequest.departmentId?.toLong())
        standardRequest.departmentName = departmentRepository.findNameById(standardRequest.departmentId?.toLong())

        standardRequest.organisationName?.let { variables.put("organisationName", it) }
        standardRequest.subject?.let { variables.put("subject", it) }
        standardRequest.description?.let { variables.put("description", it) }
        standardRequest.economicEfficiency?.let { variables.put("economicEfficiency", it) }
        standardRequest.healthSafety?.let { variables.put("healthSafety", it) }
        standardRequest.environment?.let { variables.put("environment", it) }
        standardRequest.integration?.let { variables.put("integration", it) }
        standardRequest.exportMarkets?.let { variables.put("exportMarkets", it) }
        standardRequest.levelOfStandard?.let { variables.put("levelOfStandard", it) }

        standardRequest.status = "Review By HOD"
        variables["status"] = standardRequest.status!!

        //println(standardRequest.tcId?.toLong())

        //println(technicalCommitteeRepository.findNameById(standardRequest.tcId?.toLong()))

        variables["submissionDate"] = standardRequest.submissionDate!!

        //print(standardRequest.toString())

        var department = departmentRepository.findByIdOrNull(standardRequest.departmentId?.toLong())

        standardRequest.requestNumber = generateSRNumber(department?.abbreviations)

        variables["requestNumber"] = standardRequest.requestNumber!!

        // standardRequest.rank = evaluateStandardRequest(standardRequest.tcId!!, standardRequest.departmentId!!)

        variables["rank"] = '3'
        standardRequest.rank = '3'.toString()
        variable["rank"] = standardRequest.rank!!

        standardRequestRepository.save(standardRequest)

//        sendFeedback(
//            standardRequest.email!!,
//            "We have received your request for standard. This is your request number: " + standardRequest.requestNumber!!,
//            standardRequest.name!!,
//            "Standard Request Submission"
//        )

        notifications.sendEmail(
            standardRequest.email!!,
            "Standard Request Submission",
            "Hello " + standardRequest.name!! + ",\n We have received your request for standard. This is your request number: " + standardRequest.requestNumber!!
        )
        standardRequest.id.let { variables.put("id", it) }


        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables)
        //print(variables)

//        val processIntance = ProcessInstanceResponse(processInstance.id, processInstance.isEnded)
        //  val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variable)
        return ProcessInstanceResponseValue(
            standardRequest.id, processInstance.id, processInstance.isEnded,
            standardRequest.requestNumber ?: throw NullValueNotAllowedException("ID is required")
        )
    }

    fun updateDepartmentStandardRequest(standardRequest: StandardRequest) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val standardRequestToUpdate = standardRequestRepository.findById(standardRequest.id)
            .orElseThrow { RuntimeException("No Standard Request found") }
        standardRequestToUpdate.departmentId = standardRequest.departmentId
        variable["departmentId"] = standardRequestToUpdate.departmentId!!
        standardRequestToUpdate.modifiedOn = Timestamp(System.currentTimeMillis())
        variable["modifiedOn"] = standardRequestToUpdate.modifiedOn!!
        standardRequestToUpdate.modifiedBy = loggedInUser.id.toString()
        variable["modifiedBy"] = standardRequestToUpdate.modifiedBy ?: throw ExpectedDataNotFound("No USER ID Found")

//
//        standardRequestToUpdate.departmentId?.let { variables.put("departmentId", it) }
//        standardRequestToUpdate.taskId?.let { variables.put("taskId", it) }
//        variables["departmentName"] = departmentRepository.findNameById(standardRequestToUpdate.departmentId?.toLong())
//        standardRequestToUpdate.departmentName =
//            departmentRepository.findNameById(standardRequestToUpdate.departmentId?.toLong())
//        runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables)

        standardRequestRepository.save(standardRequestToUpdate)
    }

    fun evaluateStandardRequest(tcId: String, departmentId: String): String {
        return rankingCriteriaRepository.findRankByAll(departmentId.toLong(), tcId.toLong()).toString()
    }


    fun sendFeedback(toMail: String, body: String, toName: String, subject: String) {


        // Create a mail sender
        val mailSender = JavaMailSenderImpl()
        mailSender.host = this.emailConfig.host
        mailSender.port = this.emailConfig.port
        mailSender.username = this.emailConfig.username
        mailSender.password = this.emailConfig.password


        // Create an email instance
        val mailMessage = SimpleMailMessage()
        mailMessage.setFrom("donotreply@kebs.org")
        mailMessage.setTo(toMail)
        mailMessage.setSubject(subject)
        mailMessage.setText("$toName, $body")

        // Send mail
        mailSender.send(mailMessage)
    }


    fun getAllStandardRequests(): List<StandardsDto> {
        val standardRequest: List<StandardRequest> = standardRequestRepository.findByStatus("Review By HOD")!!
        return standardRequest.map { p ->
            StandardsDto(
                p.id,
                p.requestNumber,
                p.rank,
                p.name,
                p.phone,
                p.email,
                p.submissionDate,
                p.departmentId,
                p.tcId,
                p.organisationName,
                p.subject,
                p.description,
                p.economicEfficiency,
                p.healthSafety,
                p.environment,
                p.integration,
                p.exportMarkets,
                p.levelOfStandard,
                p.status,
                departmentRepository.findNameById(p.departmentId?.toLong()),


                )
        }


    }


    fun getHOFTasks(): List<TaskDetails> {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_HOF).list()
        return getTaskDetails(tasks)
    }

    private fun getTaskDetails(tasks: List<Task>): List<TaskDetails> {
        val taskDetails: MutableList<TaskDetails> = ArrayList()
        for (task in tasks) {
            val processVariables = taskService.getVariables(task.id)
            taskDetails.add(TaskDetails(task.id, task.name, processVariables))
        }
        return taskDetails
    }

    fun hofReview(hofFeedback: HOFFeedback): HOFFeedback {
        val variable: MutableMap<String, Any> = HashMap()
        hofFeedback.isTc?.let { variable.put("isTc", it) }
        hofFeedback.isTcSec?.let { variable.put("isTcSec", it) }
        hofFeedback.sdOutput?.let { variable.put("sdOutput", it) }
        hofFeedback.sdRequestID?.let { variable.put("id", it) }
        hofFeedback.taskId?.let { variable.put("taskId", it) }
        hofFeedback.sdResult?.let { variable.put("resultOutput", it) }
        hofFeedback.reason?.let { variable.put("reason", it) }

        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val standardRequestToUpdate = hofFeedback.sdRequestID?.let {
            standardRequestRepository.findById(it.toLong())
                .orElseThrow { RuntimeException("No Standard Request found") }
        }
        if (standardRequestToUpdate != null) {
            if (hofFeedback.sdResult == "Approve For Review") {
                standardRequestToUpdate.status = "Assigned To TC Sec"
                standardRequestToUpdate.process = hofFeedback.sdOutput
                standardRequestToUpdate.tcSecAssigned = hofFeedback.isTc
                standardRequestToUpdate.modifiedOn = Timestamp(System.currentTimeMillis())
                variable["modifiedOn"] = standardRequestToUpdate.modifiedOn!!
                standardRequestToUpdate.modifiedBy = loggedInUser.id.toString()
                variable["modifiedBy"] =
                    standardRequestToUpdate.modifiedBy ?: throw ExpectedDataNotFound("No USER ID Found")
            } else if (hofFeedback.sdResult == "Reject For Review") {
                standardRequestToUpdate.status = "Rejected For Review"
                standardRequestToUpdate.modifiedOn = Timestamp(System.currentTimeMillis())
                variable["modifiedOn"] = standardRequestToUpdate.modifiedOn!!
                standardRequestToUpdate.modifiedBy = loggedInUser.id.toString()
                variable["modifiedBy"] =
                    standardRequestToUpdate.modifiedBy ?: throw ExpectedDataNotFound("No USER ID Found")
            }
            standardRequestRepository.save(standardRequestToUpdate)
        }


        hofFeedback.reviewedBy = loggedInUser.id.toString()
        hofFeedback.createdOn = Timestamp(System.currentTimeMillis())

        hofFeedbackRepository.save(hofFeedback)
//        taskService.complete(hofFeedback.taskId)

        return hofFeedback
    }

    fun getTCSECTasks(): List<TaskDetails> {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_TC_SEC).list()
        return getTaskDetails(tasks)
    }

    fun getAllStandardRequestsToPrepareNWI(): List<StandardsDto> {
        val standardRequest: List<StandardRequest> = standardRequestRepository.findByStatus("Assigned To TC Sec")!!

        return standardRequest.map { p ->
            StandardsDto(
                p.id,
                p.requestNumber,
                p.rank,
                p.name,
                p.phone,
                p.email,
                p.submissionDate,
                p.departmentId,
                p.tcId,
                p.organisationName,
                p.subject,
                p.description,
                p.economicEfficiency,
                p.healthSafety,
                p.environment,
                p.integration,
                p.exportMarkets,
                p.levelOfStandard,
                p.status,
                departmentRepository.findNameById(p.departmentId?.toLong()),
                //Feedback Segment From Review
                p.tcSecAssigned?.toLong()?.let { usersRepo.findById(it) }
                    ?.get()?.firstName + " " + p.tcSecAssigned?.toLong()?.let { usersRepo.findById(it) }
                    ?.get()?.lastName,

                returnUsername(p.id.toString()),
                p.id.let { findHofFeedbackDetails(it.toString())?.createdOn },
                p.id.let { findHofFeedbackDetails(it.toString())?.sdOutput },
                p.id.let { findHofFeedbackDetails(it.toString())?.sdResult },
                p.id.let { findHofFeedbackDetails(it.toString())?.reason },

                )
        }


    }

    fun getAllRejectedStandardRequestsToPrepareNWI(): List<StandardsDto> {
        val standardRequest: List<StandardRequest> = standardRequestRepository.findByStatus("Rejected For Review")!!
        return standardRequest.map { p ->
            StandardsDto(
                p.id,
                p.requestNumber,
                p.rank,
                p.name,
                p.phone,
                p.email,
                p.submissionDate,
                p.departmentId,
                p.tcId,
                p.organisationName,
                p.subject,
                p.description,
                p.economicEfficiency,
                p.healthSafety,
                p.environment,
                p.integration,
                p.exportMarkets,
                p.levelOfStandard,
                p.status,
                departmentRepository.findNameById(p.departmentId?.toLong()),
                //Feedback Segment From Review
                p.tcSecAssigned?.toLong()?.let { usersRepo.findById(it) }
                    ?.get()?.firstName + " " + p.tcSecAssigned?.toLong()?.let { usersRepo.findById(it) }
                    ?.get()?.lastName,

                returnUsername(p.id.toString()),
                p.id.let { findHofFeedbackDetails(it.toString())?.createdOn },
                p.id.let { findHofFeedbackDetails(it.toString())?.sdOutput },
                p.id.let { findHofFeedbackDetails(it.toString())?.sdResult },
                p.id.let { findHofFeedbackDetails(it.toString())?.reason },


                )
        }


    }


    fun uploadNWI(standardNWI: StandardNWI): ProcessInstanceResponseValue {
        println("TC-SEC has uploaded NWI")
        var allOrganization = ""
        val klaxon = Klaxon()
        JsonReader(StringReader(standardNWI.liaisonOrganisation!!)).use { reader ->
            reader.beginArray {
                while (reader.hasNext()) {
                    val liaisonOrganization = klaxon.parse<LiaisonOrganization>(reader)
                    println(liaisonOrganization?.name)
                    allOrganization += liaisonOrganization?.name + ","
                }
            }
        }
        standardNWI.liaisonOrganisation = allOrganization

        val standardRequestToUpdate = standardNWI.standardId?.let {
            standardRequestRepository.findById(it)
                .orElseThrow { RuntimeException("No Standard Request found") }
        }
        if (standardRequestToUpdate != null) {
            standardRequestToUpdate.nwiStatus = "New Work Item Created For Voting"
            standardRequestRepository.save(standardRequestToUpdate)

        }

        standardNWI.status = "Vote ON NWI"
        standardNWIRepository.save(standardNWI)
        return ProcessInstanceResponseValue(standardNWI.id, "Complete", true, "standardNWI.id")

    }


    fun getTCTasks(): List<TaskDetails> {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_TC).list()
        return getTaskDetails(tasks)
    }

    fun decisionOnNWI(voteOnNWI: VoteOnNWI) {
        voteOnNWIRepository.save(voteOnNWI)

        val variables: MutableMap<String, Any> = java.util.HashMap()
        variables["approved"] = voteOnNWI.decision.toBoolean()
        val u: StandardNWI = standardNWIRepository.findById(voteOnNWI.nwiId).orElse(null);
        u.status = "Upload Justification";
        standardNWIRepository.save(u)
        taskService.complete(voteOnNWI.taskId, variables)
    }

    fun getTCSecTasks(): List<TaskDetails> {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_TC_SEC).list()

        return getTaskDetails(tasks)
    }

    fun uploadJustification(standardJustification: StandardJustification) {
        val variable: MutableMap<String, Any> = HashMap()
        standardJustification.title?.let { variable.put("title", it) }
        standardJustification.spcMeetingDate?.let { variable.put("spcMeetingDate", it) }
        standardJustification.tcSecretary?.let { variable.put("tcSecretary", it) }
        standardJustification.sl?.let { variable.put("sl", it) }
        standardJustification.edition?.let { variable.put("edition", it) }
        standardJustification.requestedBy?.let { variable.put("requestedBy", it) }
        standardJustification.issuesAddressedBy?.let { variable.put("issuesAddressedBy", it) }
        standardJustification.tcAcceptanceDate?.let { variable.put("tcAcceptanceDate", it) }
        standardJustification.requestNo?.let { variable.put("requestNo", it) }
        standardJustification.tcId?.let { variable.put("tcId", it) }

        println(standardJustification.toString())
        standardJustificationRepository.save(standardJustification)
        val u: StandardNWI = standardNWIRepository.findById(standardJustification.nwiId!!.toLong()).orElse(null);
        u.status = "Approve/Reject Justification";
        standardNWIRepository.save(u)
        taskService.complete(standardJustification.taskId, variable)
        println("TC-SEC has uploaded Justification")

    }

    fun getSPCSecTasks(): List<TaskDetails> {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_SPC_SEC).list()
        return getTaskDetails(tasks)
    }

    fun decisionOnJustification(decisionJustification: DecisionJustification) {

        decisionJustificationRepository.save(decisionJustification)

        val variables: MutableMap<String, Any> = java.util.HashMap()
        decisionJustification.decision?.let { variables.put("spc_approved", it.toBoolean()) }
        decisionJustification.reason?.let { variables.put("reason", it) }


        val cdNumber = "CD/" + decisionJustification.referenceNo

        variables["cdNumber"] = cdNumber

        val standardJustification = standardJustificationRepository.findByRequestNo(decisionJustification.referenceNo)
        standardJustification.cdNumber = cdNumber

        standardJustificationRepository.save(standardJustification)
        val u: StandardNWI = standardNWIRepository.findById(standardJustification.nwiId!!.toLong()).orElse(null);
        u.status = "Prepare Workplan";
        standardNWIRepository.save(u)

        taskService.complete(decisionJustification.taskId, variables)
    }

    fun getTCSecTasksWorkPlan(): List<TaskDetails> {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_TC_SEC).list()
        return getTaskDetails(tasks)
    }

    fun uploadWorkPlan(standardWorkPlan: StandardWorkPlan) {
        val variable: MutableMap<String, Any> = HashMap()
        standardWorkPlan.targetDate?.let { variable.put("targetDate", it) }
        standardWorkPlan.status = "Prepare Minutes and Drafts For Preliminary Draft"
        variable["status"] = standardWorkPlan.status!!
        standardWorkPlanRepository.save(standardWorkPlan)
        val u: StandardNWI = standardNWIRepository.findById(standardWorkPlan.id).orElse(null);
        u.status = "Prepare Minutes and Drafts For Preliminary Draft";
        standardNWIRepository.save(u)
        taskService.complete(standardWorkPlan.taskId)
        println("TC-SEC has uploaded workplan")

    }


    fun getTechnicalCommitteeName(id: Long?): String {
        return technicalCommitteeRepository.findNameById(id)
    }

    fun getProducts(id: Long?): MutableList<Product> {
        return productRepository.findByTechnicalCommitteeId(id)
    }

    fun getDepartments(): MutableList<Department> {
        return departmentRepository.findAll()
    }

    fun getLiaisonOrganization(): MutableList<LiaisonOrganization> {
        return liaisonOrganizationRepository.findAll()
    }

    fun generateSRNumber(departmentAbbrv: String?): String {
        val allRequests = standardRequestRepository.findAllByOrderByIdDesc()

        var lastId: String? = "0"
        var finalValue = 1


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

        return "$departmentAbbrv/$finalValue:$year";
    }

    fun getTechnicalCommittee(id: Long?): MutableList<TechnicalCommittee> {
        return technicalCommitteeRepository.findByDepartmentId(id)
    }


    /*fun getProductCategories(id:ID): MutableList<ProductSubCategory>
    {
        return productSubCategoryRepository.findAll()
    }*/

    fun getProductCategories(id: Long?): MutableList<ProductSubCategory> {
        return productSubCategoryRepository.findByProductId(id)
    }

    fun getAllTcSec(): MutableList<UsersEntity> {
        val users: MutableList<UsersEntity> = ArrayList()

        userRolesRepo.findByRoleNameAndStatus("SD_TC_SEC", 1)
            ?.let { role ->
                userRolesAssignRepo.findByRoleIdAndStatus(role.id, 1)
                    ?.let { roleAssigns ->
                        roleAssigns.forEach { roleAssign ->
                            usersRepo.findByIdOrNull(roleAssign.userId)
                                ?.let { user ->
                                    users.add(user)
                                }
                        }
                    }
                    ?: throw Exception("Role [id=${role.id}] not found, may not be active or assigned yet")

            }
            ?: throw Exception("User role name does not exist")
        return users
    }

    fun checkProcessHistory(id: ID): List<HistoricActivityInstance> {
        val historyService = processEngine.historyService
        val activities = historyService
            .createHistoricActivityInstanceQuery()
            .processInstanceId(id.ID)
            .finished()
            .orderByHistoricActivityInstanceEndTime()
            .asc()
            .list()
        for (activity in activities) {
            println(
                activity.activityId + " took " + activity.durationInMillis + " milliseconds" + activity.processInstanceId
            )
        }

        return activities

    }


//    fun deleteTask(taskId: ID) {
//        val deleteReason = "test"
//        val cascade = true
//        println("ID passed to services is" + taskId)
//        val historyService = processEngine.historyService
//
//
//         val activities = historyService
//            .createHistoricActivityInstanceQuery()
//            .processInstanceId(taskId.ID)
////        val taskIdb = taskId.replace("\"", "");
////        val taskIdbc = taskIdb.replace("{", "").replace("}", "");
////        val taskIdc = taskIdbc.replace("id:", "");
//        println("Final ID passed to services is" + taskId.toString())
//        val tasks = CommandContextUtil.getTaskService().findTasksByProcessInstanceId("b88aa534-d97f-11eb-82cd-fe5c68490b49")
//        for (task in tasks) {
//            if (CommandContextUtil.getEventDispatcher().isEnabled && !task.isCanceled) {
//                task.isCanceled = true
//                val execution = CommandContextUtil.getExecutionEntityManager().findById(task.executionId)
//                CommandContextUtil.getEventDispatcher()
//                    .dispatchEvent(
//                        FlowableEventBuilder
//                            .createActivityCancelledEvent(
//                                execution.activityId, task.name,
//                                task.executionId, task.processInstanceId,
//                                task.processDefinitionId, "userTask", deleteReason
//                            )
//                    )
//            }
//            deleteTask(task, deleteReason, cascade, true, true)
//        }
//    }

    //    fun deleteTask(task: TaskEntity, deleteReason: String?, cascade: Boolean, b: Boolean, b1: Boolean) {
//        TaskHelper.deleteTask(task.toString(), deleteReason, cascade)
//
//    }
    fun closeTask(taskId: String) {
        taskService.complete(taskId)
        taskService.deleteTask(taskId, true)

    }

    fun closeProcess(taskId: String) {
        // taskService.complete(taskId)
        // taskService.deleteTask(taskId, true)

        runtimeService.deleteProcessInstance(taskId, "cleaning")
    }

    //create department
    fun createStandardsDepartment(department: Department) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()


        department.name?.let { variable.put("name", it) }
        department.abbreviations?.let { variable.put("abbreviations", it) }
        department.codes?.let { variable.put("codes", it) }
        department.createdBy?.let {
            variable.put(
                (loggedInUser.id ?: throw ExpectedDataNotFound("No USER ID Found")).toString(), it
            )
        }

        department.createdOn = Timestamp(System.currentTimeMillis())
        variable["createdOn"] = department.createdOn!!
        department.status = 1
        variable["status"] = department.status!!
        department.createdBy = loggedInUser.id.toString();
        variable["createdBy"] = department.createdBy!!

        departmentRepository.save(department)
    }

    //create technicalCommittee
    fun createTechnicalCommittee(technicalCommittee: TechnicalCommittee) {
//        val  department_id = departmentRepository.findByName(variable.put("name").toString())
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        technicalCommittee.departmentId.let { variable.put("departmentId", it) }
        technicalCommittee.parentCommitte?.let { variable.put("parentCommittee", it) }
        technicalCommittee.technical_committee_no?.let { variable.put("technical_committee_no", it) }
        technicalCommittee.title?.let { variable.put("title", it) }

        technicalCommittee.createdOn = Timestamp(System.currentTimeMillis())
        variable["createdOn"] = technicalCommittee.createdOn!!
        technicalCommittee.status = 1.toString()
        variable["status"] = technicalCommittee.status!!
        technicalCommittee.createdBy = loggedInUser.id.toString();
        variable["createdBy"] = technicalCommittee.createdBy!!

        technicalCommitteeRepository.save(technicalCommittee)


    }

    //create productCategory
    fun createProductCategory(product: Product) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        product.technicalCommitteeId.let { variable.put("technicalCommitteeId", it) }
        product.name?.let { variable.put("name", it) }
        product.description?.let { variable.put("description", it) }

        product.createdOn = Timestamp(System.currentTimeMillis())
        variable["createdOn"] = product.createdOn!!
        product.status = 1
        variable["status"] = product.status
        product.createdBy = loggedInUser.id.toString();
        variable["createdBy"] = product.createdBy!!

        productRepository.save(product)


    }

    //create productSubCategory
    fun createProductSubCategory(productSubCategory: ProductSubCategory) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        productSubCategory.productId.let { variable.put("productId", it) }
        productSubCategory.name?.let { variable.put("name", it) }
        productSubCategory.description?.let { variable.put("description", it) }


        productSubCategory.createdOn = Timestamp(System.currentTimeMillis())
        variable["createdOn"] = productSubCategory.createdOn!!
        productSubCategory.status = 1
        variable["status"] = productSubCategory.status!!
        productSubCategory.createdBy = loggedInUser.id.toString();
        variable["createdBy"] = productSubCategory.createdBy!!

        productSubCategoryRepository.save(productSubCategory)


    }

    //get all Departments
    fun getAllDepartments(): MutableList<Department> {
        return departmentRepository.findAll()
    }

    //get all TCs
    fun getAllTcs(): List<DataHolder> {
        return technicalCommitteeRepository.findAllWithDescriptionQuery()
    }

    //get all ProductCategory
    fun getAllProductCategories(): List<DataHolder> {
        return productRepository.findAllWithDescriptionQuery()
    }

    //get all productSubCategories
    fun getAllProductSubCategories(): List<DataHolder> {
        return productSubCategoryRepository.findAllWithDescriptionQuery()

    }

    //update Department
    fun updateDepartment(department: Department) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        department.id.let { variable.put("id", it) }
        department.name?.let { variable.put("name", it) }
        department.abbreviations?.let { variable.put("abbreviations", it) }
        department.codes?.let { variable.put("codes", it) }
        department.modifiedBy?.let {
            variable.put(
                (loggedInUser.id ?: throw ExpectedDataNotFound("No USER ID Found")).toString(), it
            )
        }
        department.modifiedOn = Timestamp(System.currentTimeMillis())
        variable["createdOn"] = department.createdOn!!
        departmentRepository.save(department)


    }

    //delete department
    fun findPermitBYPermitNumber(permitNumber: Long) {

        val exist: Boolean = technicalCommitteeRepository.existsTechnicalCommitteeByDepartmentId(permitNumber)


        if (!exist) {
            try {
                departmentRepository.deleteById(permitNumber)

            } catch (e: Exception) {
                KotlinLogging.logger { }.error(e.message)
                KotlinLogging.logger { }.debug(e.message, e)
                throw ExpectedDataNotFound("The Department ID Does not Exist")
            }
        } else {
            println("Exists")
        }
    }

    //delete Department
    fun deleteDepartment(department: Long) {

        findPermitBYPermitNumber(department)

    }

    //update technicalCommittee
    fun updateTechnicalCommittee(technicalCommittee: TechnicalCommittee) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        technicalCommittee.id.let { variable.put("id", it) }
        technicalCommittee.departmentId.let { variable.put("departmentId", it) }
        technicalCommittee.parentCommitte?.let { variable.put("parentCommittee", it) }
        technicalCommittee.technical_committee_no?.let { variable.put("technical_committee_no", it) }
        technicalCommittee.title?.let { variable.put("title", it) }
        technicalCommittee.createdOn = Timestamp(System.currentTimeMillis())
        variable["createdOn"] = technicalCommittee.createdOn!!
        technicalCommittee.status = 1.toString()
        variable["status"] = technicalCommittee.status!!
        technicalCommittee.createdBy = loggedInUser.id.toString();
        variable["createdBy"] = technicalCommittee.createdBy!!
        technicalCommitteeRepository.save(technicalCommittee)

    }

    //delete technicalCommittee
    fun deleteTechnicalCommitee(technicalCommitteeId: Long) {
        findTechnicalCommitteeById(technicalCommitteeId)
    }

    fun findTechnicalCommitteeById(technicalCommitteeId: Long) {

        val exist: Boolean = productRepository.existsProductByTechnicalCommitteeId(technicalCommitteeId)


        if (!exist) {
            try {
                technicalCommitteeRepository.deleteById(technicalCommitteeId)

            } catch (e: Exception) {
                KotlinLogging.logger { }.error(e.message)
                KotlinLogging.logger { }.debug(e.message, e)
                throw ExpectedDataNotFound("The Technical Committee ID Does not Exist")
            }
        } else {
            println("Exists")
        }
    }


    //update productCategory
    fun updateProductCategory(product: Product) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        product.id.let { variable.put("id", it) }
        product.technicalCommitteeId.let { variable.put("technicalCommitteeId", it) }
        product.name?.let { variable.put("name", it) }
        product.description?.let { variable.put("description", it) }

        product.createdOn = Timestamp(System.currentTimeMillis())
        variable["createdOn"] = product.createdOn!!
        product.status = 1
        variable["status"] = product.status
        product.createdBy = loggedInUser.id.toString();
        variable["createdBy"] = product.createdBy!!

        productRepository.save(product)


    }


    //update productSubCategory
    fun updateProductSubCategory(productSubCategory: ProductSubCategory) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        productSubCategory.id.let { variable.put("id", it) }
        productSubCategory.productId.let { variable.put("productId", it) }
        productSubCategory.name?.let { variable.put("name", it) }
        productSubCategory.description?.let { variable.put("description", it) }

        productSubCategory.createdOn = Timestamp(System.currentTimeMillis())
        variable["createdOn"] = productSubCategory.createdOn!!
        productSubCategory.status = 1
        variable["status"] = productSubCategory.status!!
        productSubCategory.createdBy = loggedInUser.id.toString();
        variable["createdBy"] = productSubCategory.createdBy!!

        productSubCategoryRepository.save(productSubCategory)


    }

    fun uploadSDFileNotLoggedIn(
        uploads: DatKebsSdStandardsEntity,
        docFile: MultipartFile,
        doc: String,
        nomineeName: String,
        DocDescription: String
    ): DatKebsSdStandardsEntity {

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
            createdBy = nomineeName
            createdOn = commonDaoServices.getTimestamp()
        }

        return sdNwaUploadsEntityRepository.save(uploads)
    }

    fun getDocuments(standardId: Long): Collection<DatKebsSdStandardsEntity?>? {

        return draftDocumentService.findUploadedDIFileBYId(standardId)
    }

    fun findHofFeedbackDetails(sdRequestNumber: String): HOFFeedback? {
        hofFeedbackRepository.findBySdRequestID(sdRequestNumber).let {
            return it
        }
    }

    fun returnUsername(sdRequestNumber: String): String {

        val hofFeedback = findHofFeedbackDetails(sdRequestNumber)
        return if (hofFeedback != null) {
            val user = usersRepo.findByIdOrNull(hofFeedback.reviewedBy!!.toLong())
            user?.firstName + " " + user?.lastName
        } else {
            "Not Assigned"
        }


    }

    fun uploadSDFileCommittee(
        uploads: DatKebsSdStandardsEntity,
        docFile: MultipartFile,
        doc: String,
        nwi: Long,
        DocDescription: String
    ): DatKebsSdStandardsEntity {

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
            sdDocumentId = nwi
            createdBy = commonDaoServices.concatenateName(commonDaoServices.loggedInUserDetails())
            createdOn = commonDaoServices.getTimestamp()
        }


        return sdDocumentsRepository.save(uploads)
    }


}





