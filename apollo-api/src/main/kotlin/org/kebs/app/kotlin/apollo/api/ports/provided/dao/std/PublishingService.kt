package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std

import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.engine.repository.Deployment
import org.flowable.task.api.Task
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.dto.std.Decision
import org.kebs.app.kotlin.apollo.common.dto.std.ProcessInstanceResponseValue
import org.kebs.app.kotlin.apollo.common.dto.std.TaskDetails
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.std.DatKebsSdStandardsEntity
import org.kebs.app.kotlin.apollo.store.model.std.DecisionFeedback
import org.kebs.app.kotlin.apollo.store.model.std.StandardDraft
import org.kebs.app.kotlin.apollo.store.repo.std.DecisionFeedbackRepository
import org.kebs.app.kotlin.apollo.store.repo.std.StandardDraftRepository
import org.kebs.app.kotlin.apollo.store.repo.std.StandardsDocumentsRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.sql.Timestamp

@Service
class PublishingService(
    private val runtimeService: RuntimeService,
    private val taskService: TaskService,
    @Qualifier("processEngine") private val processEngine: ProcessEngine,
    private val repositoryService: RepositoryService,
    private val standardDraftRepository: StandardDraftRepository,
    private val decisionFeedbackRepository: DecisionFeedbackRepository,
    val commonDaoServices: CommonDaoServices,
    private val sdNwaUploadsEntityRepository: StandardsDocumentsRepository,


    ) {
    val PROCESS_DEFINITION_KEY = "sd_publishing"
    val TASK_HEAD_OF_PUBLISHING = "head_of_publishing"
    val TASK_EDITOR = "editor"
    val TASK_PROOFREADER = "proofreader"
    val TASK_DRAUGHTSMAN = "draughtsman"

    fun deployProcessDefinition(): Deployment = repositoryService
        .createDeployment()
        .addClasspathResource("processes/std/publishing_module.bpmn20.xml")
        .deploy()

    fun sumbitDraftStandard(standardDraft: StandardDraft): ProcessInstanceResponseValue {
        val variables: MutableMap<String, Any> = HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        standardDraft.standardOfficerId?.let {
            variables.put(
                (loggedInUser.id ?: throw ExpectedDataNotFound("No USER ID Found")).toString(), it
            )
        }
        standardDraft.standardOfficerName = loggedInUser.firstName + " " + loggedInUser.lastName
        variables["standardOfficerName"] = standardDraft.standardOfficerName!!
        standardDraft.standardOfficerId = loggedInUser.id.toString()
        variables["standardOfficerId"] = standardDraft.standardOfficerId!!
        standardDraft.requestorId?.let { variables.put("requestorId", it) }
        standardDraft.title?.let { variables.put("title", it) }
        standardDraft.submission_date = Timestamp(System.currentTimeMillis())
        variables["submission_date"] = standardDraft.submission_date!!
        standardDraft.versionNumber?.let { variables.put("version", it) }
        standardDraftRepository.save(standardDraft)
        variables["ID"] = standardDraft.id
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables)
        return ProcessInstanceResponseValue(
            standardDraft.id, processInstance.id, processInstance.isEnded,
            standardDraft.standardOfficerId ?: throw NullValueNotAllowedException("ID is required")
        )
    }

    fun getHOPTasks(): List<TaskDetails> {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_HEAD_OF_PUBLISHING).list()
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

    fun decisionOnKSDraft(decision: Decision)
    {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        variables["approved"] = decision.decision
        taskService.complete(decision.taskId, variables)
    }

    fun uploadFeedbackOnDraft(decisionFeedback: DecisionFeedback) {
        val variable: MutableMap<String, Any> = HashMap()

        decisionFeedback.user_id.let { variable.put("user_id", it) }
        decisionFeedback.item_id?.let { variable.put("item_id", it) }
        decisionFeedback.status?.let { variable.put("status", it) }
        decisionFeedback.comment?.let { variable.put("comment", it) }
        decisionFeedback.taskId?.let { variable.put("taskId", it) }


        print(decisionFeedback.toString())

        decisionFeedbackRepository.save(decisionFeedback)

        taskService.complete(decisionFeedback.taskId)
    }

    fun getEditorTasks(): List<TaskDetails> {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_EDITOR).list()
        return getTaskDetails(tasks)
    }

    fun editDraftStandard(standardDraft: StandardDraft) {
        val variables: MutableMap<String, Any> = HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        standardDraft.standardOfficerId?.let {
            variables.put(
                (loggedInUser.id ?: throw ExpectedDataNotFound("No USER ID Found")).toString(), it
            )
        }
        standardDraft.standardOfficerId = loggedInUser.id.toString();
        variables["standardOfficerId"] = standardDraft.standardOfficerId!!
        standardDraft.requestorId?.let { variables.put("requestor_id", it) }
        standardDraft.title?.let { variables.put("title", it) }
        standardDraft.versionNumber?.let { variables.put("versionNumber", it) }
        standardDraft.taskId?.let { variables.put("taskId", it) }

        standardDraftRepository.save(standardDraft)
        taskService.complete(standardDraft.taskId)
    }

    fun getProofreaderTasks(): List<TaskDetails> {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_PROOFREADER).list()
        return getTaskDetails(tasks)
    }

    fun decisionOnProofReading(decision: Decision) {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        variables["draught"] = decision.decision
        taskService.complete(decision.taskId, variables)
    }

    fun approveDraughtChange(standardDraft: StandardDraft) {
        val variables: MutableMap<String, Any> = HashMap()

        val loggedInUser = commonDaoServices.loggedInUserDetails()

        standardDraft.standardOfficerId?.let {
            variables.put(
                (loggedInUser.id ?: throw ExpectedDataNotFound("No USER ID Found")).toString(), it
            )
        }
        standardDraft.standardOfficerId = loggedInUser.id.toString();
        variables["standardOfficerId"] = standardDraft.standardOfficerId!!
        standardDraft.requestorId?.let { variables.put("requestor_id", it) }
        standardDraft.title?.let { variables.put("title", it) }
        standardDraft.taskId?.let { variables.put("taskId", it) }

        standardDraftRepository.save(standardDraft)
        taskService.complete(standardDraft.taskId)
    }

    fun getDraughtsmanTasks(): List<TaskDetails> {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_DRAUGHTSMAN).list()
        return getTaskDetails(tasks)
    }

    fun uploadDraftStandard(standardDraft: StandardDraft) {
        val variables: MutableMap<String, Any> = HashMap()

        val loggedInUser = commonDaoServices.loggedInUserDetails()

        standardDraft.standardOfficerId?.let {
            variables.put(
                (loggedInUser.id ?: throw ExpectedDataNotFound("No USER ID Found")).toString(), it
            )
        }
        standardDraft.standardOfficerId = loggedInUser.id.toString();
        variables["standardOfficerId"] = standardDraft.standardOfficerId!!
        standardDraft.requestorId?.let { variables.put("requestor_id", it) }
        standardDraft.title?.let { variables.put("title", it) }
        standardDraft.versionNumber?.let { variables.put("versionNumber", it) }
        standardDraft.taskId?.let { variables.put("taskId", it) }

        standardDraftRepository.save(standardDraft)
        taskService.complete(standardDraft.taskId)
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

        return sdNwaUploadsEntityRepository.save(uploads)
    }

}
