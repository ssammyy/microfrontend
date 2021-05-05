package com.apollo.standardsdevelopment.services

import com.apollo.standardsdevelopment.dto.ProcessInstanceResponse
import com.apollo.standardsdevelopment.dto.TaskDetails
import com.apollo.standardsdevelopment.models.PublicReviewDraft
import com.apollo.standardsdevelopment.models.PublicReviewDraftComments
import com.apollo.standardsdevelopment.repositories.PublicReviewDraftCommentsRepository
import com.apollo.standardsdevelopment.repositories.PublicReviewDraftRepository
import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.task.api.Task
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.util.ArrayList

@Service
class PublicReviewService(
    private val runtimeService: RuntimeService,
    private val taskService: TaskService,
    @Qualifier("processEngine") private val processEngine: ProcessEngine,
    private val repositoryService: RepositoryService,
    private val publicReviewDraftRepository: PublicReviewDraftRepository,
    private val publicReviewDraftCommentsRepository: PublicReviewDraftCommentsRepository,

    ) {
    val PROCESS_DEFINITION_KEY = "public_review_stage"
    val TASK_CANDIDATE_GROUP_TC_SEC = "TC-sec"
    val TASK_CANDIDATE_GROUP_TC = "TC"
    val variable: MutableMap<String, Any> = HashMap()


    fun deployProcessDefinition() {
        repositoryService
            .createDeployment()
            .addClasspathResource("processes/public_review.bpmn20.xml")
            .deploy()
    }

    fun preparePublicReview(publicReviewDraft: PublicReviewDraft): ProcessInstanceResponse {
        publicReviewDraft.PrdName?.let { variable.put("PrdName", it) }
        publicReviewDraft.PrdraftBy?.let { variable.put("PrdraftBy", it) }
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
        publicReviewDraftComments.prdId?.let { variable.put("prdId", it) }
        publicReviewDraftComments.roleId?.let { variable.put("roleId", it) }
        publicReviewDraftComments.roleName?.let { variable.put("roleName", it) }
        publicReviewDraftComments.userId?.let { variable.put("userId", it) }
        publicReviewDraftComments.comment?.let { variable.put("comment", it) }

        print(publicReviewDraftComments.toString())
        publicReviewDraftCommentsRepository.save(publicReviewDraftComments)
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variable)
        return ProcessInstanceResponse(processInstance.id, processInstance.isEnded)

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
}