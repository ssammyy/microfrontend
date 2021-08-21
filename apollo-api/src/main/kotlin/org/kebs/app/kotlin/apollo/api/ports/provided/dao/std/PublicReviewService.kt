package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std

import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.task.api.Task
import org.kebs.app.kotlin.apollo.api.errors.std.*
import org.kebs.app.kotlin.apollo.common.dto.std.*
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.std.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable

@Service
class PublicReviewService(
    private val runtimeService: RuntimeService,
    private val taskService: TaskService,
    @Qualifier("processEngine") private val processEngine: ProcessEngine,
    private val repositoryService: RepositoryService,
    private val publicReviewDraftRepository: PublicReviewDraftRepository,
    private val publicReviewDraftCommentsRepository: PublicReviewDraftCommentsRepository,

    ) {
    val PROCESS_DEFINITION_KEY = "publicreview"
    val TASK_CANDIDATE_GROUP_TC_SEC = "TC-sec"
    val TASK_CANDIDATE_GROUP_TC = "TC"
    val variable: MutableMap<String, Any> = HashMap()


    fun deployProcessDefinition() {
        repositoryService
            .createDeployment()
            .addClasspathResource("processes/std/public_review.bpmn20.xml")
            .deploy()
    }

    fun preparePublicReview(publicReviewDraft: PublicReviewDraft): ProcessInstanceResponse {
        publicReviewDraft.prdName?.let { variable.put("prdName", it) }
        publicReviewDraft.prdraftBy?.let { variable.put("prdraftBy", it) }
        publicReviewDraft.prdpath?.let { variable.put("prdpath", it) }

        print(publicReviewDraft.toString())
        publicReviewDraftRepository.save(publicReviewDraft)
        sendNotification(publicReviewDraft.id)
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variable)
        return ProcessInstanceResponse(processInstance.id, processInstance.isEnded)

    }

    fun sendNotification(id: Long) {
        print("Notification sent To Head of ICT,Departments/ Organizations,NEP officer,Head of Publishing")

    }

    fun makeCommentOnDraft(publicReviewDraftComments: PublicReviewDraftComments): ProcessInstanceResponse {
        publicReviewDraftComments.prdId.let { variable.put("prdId", it) }
        publicReviewDraftComments.roleId.let { variable.put("roleId", it) }
        publicReviewDraftComments.roleName?.let { variable.put("roleName", it) }
        publicReviewDraftComments.userId.let { variable.put("userId", it) }
        publicReviewDraftComments.title?.let { variable.put("title", it) }
        publicReviewDraftComments.comment?.let { variable.put("comment", it) }
        publicReviewDraftComments.documentType?.let { variable.put("documentType", it) }
        publicReviewDraftComments.circulationDate?.let { variable.put("circulationDate", it) }
        publicReviewDraftComments.closingDate?.let { variable.put("closingDate", it) }
        publicReviewDraftComments.organization?.let { variable.put("organization", it) }
        publicReviewDraftComments.clause?.let { variable.put("clause", it) }
        publicReviewDraftComments.commentType?.let { variable.put("commentType", it) }
        publicReviewDraftComments.comment?.let { variable.put("comment", it) }
        publicReviewDraftComments.proposedChange?.let { variable.put("proposedChange", it) }
        publicReviewDraftComments.observations?.let { variable.put("observations", it) }
        print(publicReviewDraftComments.toString())
        publicReviewDraftCommentsRepository.save(publicReviewDraftComments)
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variable)
        return ProcessInstanceResponse(processInstance.id, processInstance.isEnded)

    }

    fun getPrs(): MutableList<PublicReviewDraft> {
        return publicReviewDraftRepository.findAll()
    }

    fun getPrComments(): MutableList<PublicReviewDraftComments> {
        return publicReviewDraftCommentsRepository.findAll()
    }

    private fun getTaskDetails(tasks: List<Task>): List<TaskDetails> {
        val taskDetails: MutableList<TaskDetails> = ArrayList()
        for (task in tasks) {
            val processVariables = taskService.getVariables(task.id)
            taskDetails.add(TaskDetails(task.id, task.name, processVariables))
        }
        return taskDetails
    }

    fun approvePublicReview(taskId: String?, approved: Boolean) {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        variables["approved"] = approved
        taskService.complete(taskId, variables)
    }


    fun checkProcessHistory(processId: String?) {
        val historyService = processEngine.historyService
        val activities = historyService
            .createHistoricActivityInstanceQuery()
            .processInstanceId(processId)
            .finished()
            .orderByHistoricActivityInstanceEndTime()
            .asc()
            .list()
        for (activity in activities) {
            println(
                activity.activityId + " took " + activity.durationInMillis + " milliseconds"
            )
        }

    }

    //Return task details for TC
    fun getTCTasks(): List<TaskDetails> {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_TC)
            .processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    //    fun getPublicReviewDrafts(id: Long): MutableList<PublicReviewDraft> {
//        return publicReviewDraftRepository.findById(id)
//    }
    fun getPublicReviewDraftById(@PathVariable(value = "id") publicReviewId: Long): ResponseEntity<PublicReviewDraft?>? {
        val employee: PublicReviewDraft = publicReviewDraftRepository.findById(publicReviewId)
            .orElseThrow { ResourceNotFoundException("Employee not found for this id :: $publicReviewId") }
        return ResponseEntity.ok().body<PublicReviewDraft?>(employee)
    }
}
