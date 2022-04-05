package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std

import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.engine.history.HistoricActivityInstance
import org.flowable.engine.repository.Deployment
import org.flowable.task.api.Task
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.dto.std.ID
import org.kebs.app.kotlin.apollo.common.dto.std.ProcessInstanceResponseValue
import org.kebs.app.kotlin.apollo.common.dto.std.TaskDetails
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.std.DatKebsSdStandardsEntity
import org.kebs.app.kotlin.apollo.store.model.std.DecisionFeedback
import org.kebs.app.kotlin.apollo.store.model.std.SACSummary
import org.kebs.app.kotlin.apollo.store.repo.std.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.sql.Timestamp
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.set

@Service
class AdoptionOfEastAfricaStandardService(
    private val runtimeService: RuntimeService,
    private val taskService: TaskService,
    @Qualifier("processEngine") private val processEngine: ProcessEngine,
    private val repositoryService: RepositoryService,
    private val sacSummaryRepository: SACSummaryRepository,
    val commonDaoServices: CommonDaoServices,
    private val sdDocumentsRepository: StandardsDocumentsRepository,
    private val departmentRepository: DepartmentRepository,
    private val technicalCommitteeRepository: TechnicalCommitteeRepository,
    private val decisionFeedbackRepository: DecisionFeedbackRepository
) {

    val PROCESS_DEFINITION_KEY = "adoption_of_east_africa_standard"
    val SPC_SEC = "SPC-SEC"
    val SAC_SEC = "SAC-sec"
    val TC_sec = "tc-secretary"

    fun deployProcessDefinition(): Deployment = repositoryService
        .createDeployment()
        .addClasspathResource("processes/std/adoption_of_east_africa_standard.bpmn20.xml")
        .deploy()

    fun submitSACSummary(sacSummary: SACSummary): ProcessInstanceResponseValue {
        val variable: MutableMap<String, Any> = HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        sacSummary.requestedBy = loggedInUser.id.toString()
        sacSummary.sl?.let { variable.put("sl", it) }
        sacSummary.ks?.let { variable.put("ks", it) }
        sacSummary.requestedBy?.let { variable.put("requestedBy", it) }
        sacSummary.issuesAddressed?.let { variable.put("issuesAddressed", it) }
        sacSummary.backgroundInformation?.let { variable.put("backgroundInformation", it) }
        sacSummary.approvalStatus?.let { variable.put("approvalStatus", it) }
        sacSummary.feedback?.let { variable.put("feedback", it) }
        sacSummary.dateOfApproval = Timestamp(System.currentTimeMillis())
        sacSummary.dateOfApproval?.let { variable.put("dateOfApproval", it) }
        sacSummary.eacGazette?.let { variable.put("eacGazette", it) }
        sacSummary.authenticText?.let { variable.put("authenticText", it) }
        sacSummary.varField1 = "CREATED"
        sacSummary.varField1?.let { variable.put("varField1", it) }
        sacSummary.edition?.let { variable.put("edition", it) }
        sacSummary.title?.let { variable.put("title", it) }
        sacSummary.referenceMaterial?.let { variable.put("referenceMaterial", it) }
        sacSummary.technicalCommittee?.let { variable.put("technicalCommittee", it) }
        sacSummary.department?.let { variable.put("department", it) }

        val department = departmentRepository.findNameById(sacSummary.department?.toLong())
        department.let { variable.put("departmentName", it) }
        val technicalCommittee = technicalCommitteeRepository.findNameById(sacSummary.technicalCommittee?.toLong())
        technicalCommittee.let { variable.put("technicalCommitteeName", it) }

        val requestedByName = loggedInUser.firstName +loggedInUser.lastName;
        requestedByName.let { variable.put("requestedByName", it) }


//
//        variable["technicalCommitteeId"] =
//            technicalCommitteeRepository.findNameById(sacSummary.technicalCommittee?.toLong())
//        variable["departmentId"] = departmentRepository.findNameById(sacSummary.department?.toLong())

        sacSummaryRepository.save(sacSummary)
        sacSummary.id.let { variable.put("id", it) }


        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variable)
        return ProcessInstanceResponseValue(
            sacSummary.id, processInstance.id, processInstance.isEnded,
            sacSummary.sl ?: throw NullValueNotAllowedException("ID is required")
        )
    }
    fun getTcSecSummaryTask(): List<TaskDetails> {
        val tasks = taskService.createTaskQuery().taskAssignee(TC_sec).list()
        return getTaskDetails(tasks)
    }

    fun approveSACSummary(decisionFeedback: DecisionFeedback, applicationID: Long) {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val u: SACSummary = sacSummaryRepository.findById(applicationID).orElse(null);
        u.varField1 = "Approved"
        sacSummaryRepository.save(u)
        decisionFeedback.user_id = loggedInUser.id!!
        decisionFeedback.user_id.let { variables.put("user_id", it) }

        variables["approved"] = "approved"
        taskService.complete(decisionFeedback.taskId, variables)

        decisionFeedbackRepository.save(decisionFeedback)
    }


    fun getSACSummaryTask(): List<TaskDetails> {
        val tasks = taskService.createTaskQuery().taskAssignee(SPC_SEC).list()
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

    fun decisionOnSACSummary(decisionFeedback: DecisionFeedback, applicationID: Long) {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val u: SACSummary = sacSummaryRepository.findById(applicationID).orElse(null);
        u.varField1 = decisionFeedback.status.toString()
        sacSummaryRepository.save(u)
        decisionFeedback.user_id = loggedInUser.id!!
        decisionFeedback.user_id.let { variables.put("user_id", it) }
        decisionFeedback.item_id?.let { variables.put("item_id", it) }
        decisionFeedback.comment?.let { variables.put("comment", it) }
        decisionFeedback.taskId?.let { variables.put("taskId", it) }

        variables["approved"] = decisionFeedback.status!!
        taskService.complete(decisionFeedback.taskId, variables)

        decisionFeedbackRepository.save(decisionFeedback)
    }

    fun getSACSECTask():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(SAC_SEC).list()
        return getTaskDetails(tasks)
    }

    fun decisionOnSACSEC(decisionFeedback: DecisionFeedback, applicationID: Long) {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val u: SACSummary = sacSummaryRepository.findById(applicationID).orElse(null);
        u.varField2 = decisionFeedback.status.toString()
        sacSummaryRepository.save(u)
        decisionFeedback.user_id = loggedInUser.id!!
        decisionFeedback.user_id?.let { variables.put("user_id", it) }
        decisionFeedback.item_id?.let { variables.put("item_id", it) }
        decisionFeedback.comment?.let { variables.put("comment", it) }
        decisionFeedback.taskId?.let { variables.put("taskId", it) }

        variables["SACApproved"] = decisionFeedback.status!!
        taskService.complete(decisionFeedback.taskId, variables)

        decisionFeedbackRepository.save(decisionFeedback)
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


    fun uploadSDFile(
        uploads: DatKebsSdStandardsEntity,
        docFile: MultipartFile,
        doc: String,
        user: UsersEntity,
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
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }

        return sdDocumentsRepository.save(uploads)
    }

}
