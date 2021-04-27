package com.apollo.standardsdevelopment.services

import com.apollo.standardsdevelopment.dto.Decision
import com.apollo.standardsdevelopment.dto.ID
import com.apollo.standardsdevelopment.dto.ProcessInstanceResponse
import com.apollo.standardsdevelopment.dto.TaskDetails
import com.apollo.standardsdevelopment.models.DecisionFeedback
import com.apollo.standardsdevelopment.models.StandardDraft
import com.apollo.standardsdevelopment.models.StandardJustification
import com.apollo.standardsdevelopment.models.StandardRequest
import com.apollo.standardsdevelopment.repositories.DecisionFeedbackRepository
import com.apollo.standardsdevelopment.repositories.StandardDraftRepository
import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.engine.repository.Deployment
import org.flowable.task.api.Task
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.util.ArrayList

@Service
class PublishingService(private val runtimeService: RuntimeService,
                        private val taskService: TaskService,
                        @Qualifier("processEngine") private val processEngine: ProcessEngine,
                        private val repositoryService: RepositoryService,
                        private val standardDraftRepository: StandardDraftRepository,
                        private val decisionFeedbackRepository: DecisionFeedbackRepository
)
{
    val PROCESS_DEFINITION_KEY = "sd_publishing"
    val TASK_HEAD_OF_PUBLISHING = "head_of_publishing"
    val TASK_EDITOR = "editor"
    val TASK_PROOFREADER = "proofreader"
    val TASK_DRAUGHTSMAN = "draughtsman"

    fun deployProcessDefinition(): Deployment =repositoryService
            .createDeployment()
            .addClasspathResource("processes/publishing_module.bpmn20.xml")
            .deploy()

    fun sumbitDraftStandard(standardDraft: StandardDraft): ProcessInstanceResponse
    {
        val variables: MutableMap<String, Any> = HashMap()

        standardDraft.standardOfficerId?.let { variables.put("standard_officer_id",it) }
        standardDraft.requestorId?.let { variables.put("requestor_id", it) }
        standardDraft.title?.let { variables.put("title", it) }


        standardDraftRepository.save(standardDraft)

        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables)
        return ProcessInstanceResponse(processInstance.id, processInstance.isEnded)
    }

    fun getHOPTasks():List<TaskDetails>
    {
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

    fun uploadFeedbackOnDraft(decisionFeedback: DecisionFeedback)
    {
        val variable:MutableMap<String, Any> = HashMap()

        decisionFeedback.user_id?.let{variable.put("user_id", it)}
        decisionFeedback.item_id?.let{variable.put("item_id", it)}
        decisionFeedback.status?.let{variable.put("status", it)}
        decisionFeedback.comment?.let{variable.put("comment", it)}
        decisionFeedback.taskId?.let{variable.put("taskId", it)}


        print(decisionFeedback.toString())

        decisionFeedbackRepository.save(decisionFeedback)

        taskService.complete(decisionFeedback.taskId)
    }

    fun getEditorTasks():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_EDITOR).list()
        return getTaskDetails(tasks)
    }

    fun editDraftStandard(standardDraft: StandardDraft)
    {
        val variables: MutableMap<String, Any> = HashMap()

        standardDraft.standardOfficerId?.let { variables.put("standard_officer_id",it) }
        standardDraft.requestorId?.let { variables.put("requestor_id", it) }
        standardDraft.title?.let { variables.put("title", it) }
        standardDraft.versionNumber?.let { variables.put("versionNumber", it) }
        standardDraft.taskId?.let{variables.put("taskId", it)}

        standardDraftRepository.save(standardDraft)

        taskService.complete(standardDraft.taskId)
    }

    fun getProofreaderTasks():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_PROOFREADER).list()
        return getTaskDetails(tasks)
    }

    fun decisionOnProofReading(decision: Decision)
    {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        variables["draught"] = decision.decision
        taskService.complete(decision.taskId, variables)
    }

    fun approveDraughtChange(standardDraft: StandardDraft)
    {
        val variables: MutableMap<String, Any> = HashMap()

        standardDraft.standardOfficerId?.let { variables.put("standard_officer_id",it) }
        standardDraft.requestorId?.let { variables.put("requestor_id", it) }
        standardDraft.title?.let { variables.put("title", it) }
        standardDraft.taskId?.let{variables.put("taskId", it)}

        standardDraftRepository.save(standardDraft)
        taskService.complete(standardDraft.taskId)
    }

    fun getDraughtsmanTasks():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_DRAUGHTSMAN).list()
        return getTaskDetails(tasks)
    }

    fun uploadDraftStandard(standardDraft: StandardDraft)
    {
        val variables: MutableMap<String, Any> = HashMap()

        standardDraft.standardOfficerId?.let { variables.put("standard_officer_id",it) }
        standardDraft.requestorId?.let { variables.put("requestor_id", it) }
        standardDraft.title?.let { variables.put("title", it) }
        standardDraft.versionNumber?.let { variables.put("versionNumber", it) }
        standardDraft.taskId?.let{variables.put("taskId", it)}

        standardDraftRepository.save(standardDraft)
        taskService.complete(standardDraft.taskId)
    }

}