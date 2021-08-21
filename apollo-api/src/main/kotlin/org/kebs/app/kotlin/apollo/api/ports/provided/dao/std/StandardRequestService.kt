package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std

import com.beust.klaxon.Klaxon
import org.kebs.app.kotlin.apollo.common.dto.std.ID
import org.kebs.app.kotlin.apollo.common.dto.std.ProcessInstanceResponse
import org.kebs.app.kotlin.apollo.common.dto.std.TaskDetails
//import com.apollo.standardsdevelopment.models.*
//import com.apollo.standardsdevelopment.repositories.*
import com.beust.klaxon.JsonReader
//import com.beust.klaxon.Klaxon

//import com.google.gson.stream.JsonReader
import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.engine.history.HistoricActivityInstance
import org.flowable.engine.repository.Deployment
import org.flowable.task.api.Task
import org.kebs.app.kotlin.apollo.api.web.config.EmailConfig
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.std.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.repository.findByIdOrNull
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.stereotype.Service
import java.io.StringReader
import java.sql.Timestamp
import java.util.*
import kotlin.collections.HashMap

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
    private val decisionJustificationRepository: DecisionJustificationRepository
) {

    val PROCESS_DEFINITION_KEY = "requestModule"
    val TASK_CANDIDATE_GROUP_HOF = "HOF"
    val TASK_CANDIDATE_GROUP_TC_SEC = "TC-sec"
    val TASK_CANDIDATE_GROUP_TC = "TC"
    val TASK_CANDIDATE_GROUP_SPC_SEC = "SPC-sec"

    fun deployProcessDefinition(): Deployment = repositoryService
        .createDeployment()
        .addClasspathResource("processes/std/request_module.bpmn20.xml")
        .deploy()


    fun requestForStandard(standardRequest: StandardRequest): ProcessInstanceResponse {

        val variables: MutableMap<String, Any> = HashMap()


        standardRequest.name?.let { variables.put("name", it) }
        standardRequest.email?.let { variables.put("email", it) }
        standardRequest.phone?.let { variables.put("phone", it) }
        standardRequest.tcId?.let { variables.put("tcId", it) }
        //standardRequest.tcName?.let { variables.put("tcName", technicalCommitteeRepository.findNameById(standardRequest.tcId?.toLong())) }
        standardRequest.departmentId?.let { variables.put("departmentId", it) }
        standardRequest.productSubCategoryId?.let { variables.put("productSubCategoryId", it) }
        standardRequest.productId?.let { variables.put("productId", it) }

        standardRequest.submissionDate = Timestamp(System.currentTimeMillis())



        variables["tcName"] = technicalCommitteeRepository.findNameById(standardRequest.tcId?.toLong())
        standardRequest.tcName = technicalCommitteeRepository.findNameById(standardRequest.tcId?.toLong())

        variables["departmentName"] = departmentRepository.findNameById(standardRequest.departmentId?.toLong())
        standardRequest.departmentName = departmentRepository.findNameById(standardRequest.departmentId?.toLong())

        variables["productName"] = productRepository.findNameById(standardRequest.productId?.toLong())
        standardRequest.productName = productRepository.findNameById(standardRequest.productId?.toLong())

        variables["productSubCategoryName"] =
            productSubCategoryRepository.findNameById(standardRequest.productSubCategoryId?.toLong())
        standardRequest.productSubCategoryName =
            productSubCategoryRepository.findNameById(standardRequest.productSubCategoryId?.toLong())


        //println(standardRequest.tcId?.toLong())

        //println(technicalCommitteeRepository.findNameById(standardRequest.tcId?.toLong()))

        variables["submissionDate"] = standardRequest.submissionDate!!

        //print(standardRequest.toString())

        var department = departmentRepository.findByIdOrNull(standardRequest.departmentId?.toLong())

        standardRequest.requestNumber = generateSRNumber(department?.abbreviations)

        variables["requestNumber"] = standardRequest.requestNumber!!

        standardRequest.rank = evaluateStandardRequest(standardRequest.tcId!!, standardRequest.departmentId!!)

        variables["rank"] = standardRequest.rank!!

        standardRequestRepository.save(standardRequest)

        sendFeedback(
            standardRequest.email!!,
            "We have received your request for standard. This is your request number: " + standardRequest.requestNumber!!,
            standardRequest.name!!,
            "Standard Request Submission"
        )

        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables)
        //print(variables)

        val processIntance = ProcessInstanceResponse(processInstance.id, processInstance.isEnded)

        //val getProcessInstance = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(PROCESS_DEFINITION_KEY).singleResult()

//        val gottenVariables = processIntance.body
//
//        println(gottenVariables)

        return processIntance
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
      //  mailSender.send(mailMessage)
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
        println("HOF has finished the review and given suggestion")
        val variable: MutableMap<String, Any> = HashMap()
        hofFeedback.isTc?.let { variable.put("tc", it) }
        hofFeedback.isTcSec?.let { variable.put("tcSec", it) }
        hofFeedback.sdOutput?.let { variable.put("output", it) }
        hofFeedback.sdRequestID?.let { variable.put("sdRequest", it) }
        hofFeedback.taskId?.let { variable.put("taskId", it) }


        hofFeedbackRepository.save(hofFeedback)
        taskService.complete(hofFeedback.taskId)

        return hofFeedback
    }

    fun getTCSECTasks(): List<TaskDetails> {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_TC_SEC).list()
        return getTaskDetails(tasks)
    }

    fun uploadNWI(standardNWI: StandardNWI) {

        println("TC-SEC has uploaded NWI")

        val variable: MutableMap<String, Any> = HashMap()
        standardNWI.proposalTitle?.let { variable.put("proposalTitle", it) }
        standardNWI.nameOfProposer?.let { variable.put("nameOfProposer", it) }
        standardNWI.scope?.let { variable.put("scope", it) }
        standardNWI.purpose?.let { variable.put("purpose", it) }
        standardNWI.targetDate?.let { variable.put("targetDate", it) }
        standardNWI.similarStandards?.let { variable.put("similarStandards", it) }
        //standardNWI.liaisonOrganisation?.let{variable.put("liaisonOrganisation", it)}
        standardNWI.outlineAttached?.let { variable.put("outlineAttached", it) }
        standardNWI.draftAttached?.let { variable.put("draftAttached", it) }
        standardNWI.draftOutlineImpossible?.let { variable.put("draftOutlineImpossible", it) }
        standardNWI.outlineSentLater?.let { variable.put("outlineSentLater", it) }
        standardNWI.organization?.let { variable.put("organization", it) }
        standardNWI.circulationDate?.let { variable.put("circulationDate", it) }
        standardNWI.closingDate?.let { variable.put("closingDate", it) }
        standardNWI.dateOfPresentation?.let { variable.put("dateOfPresentation", it) }
        standardNWI.nameOfTC?.let { variable.put("nameOfTC", it) }
        standardNWI.referenceNumber?.let { variable.put("referenceNumber", it) }
        standardNWI.taskId?.let { variable.put("taskId", it) }

        println(standardNWI)


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

        println(allOrganization)

        standardNWI.liaisonOrganisation = allOrganization
        standardNWI.liaisonOrganisation?.let { variable.put("liaisonOrganisation", it) }

        standardNWIRepository.save(standardNWI)

        taskService.complete(standardNWI.taskId, variable)

    }


    fun getTCTasks(): List<TaskDetails> {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_TC).list()
        return getTaskDetails(tasks)
    }

    fun decisionOnNWI(voteOnNWI: VoteOnNWI) {
        voteOnNWIRepository.save(voteOnNWI)

        val variables: MutableMap<String, Any> = java.util.HashMap()
        variables["approved"] = voteOnNWI.decision.toBoolean()
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

        var standardJustification = standardJustificationRepository.findByRequestNo(decisionJustification.referenceNo)

        standardJustification[0].cdNumber = cdNumber

        standardJustificationRepository.saveAll(standardJustification)

        taskService.complete(decisionJustification.taskId, variables)
    }

    fun getTCSecTasksWorkPlan(): List<TaskDetails> {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_TC_SEC).list()
        return getTaskDetails(tasks)
    }

    fun uploadWorkPlan(standardWorkPlan: StandardWorkPlan) {
        val variable: MutableMap<String, Any> = HashMap()
        standardWorkPlan.targetDate?.let { variable.put("targetDate", it) }

        standardWorkPlanRepository.save(standardWorkPlan)
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
                activity.activityId + " took " + activity.durationInMillis + " milliseconds"
            )
        }

        return activities

    }


}
