package com.apollo.standardsdevelopment.services

import com.apollo.standardsdevelopment.dto.Decision
import com.apollo.standardsdevelopment.dto.ProcessInstanceResponse
import com.apollo.standardsdevelopment.dto.TaskDetails
import com.apollo.standardsdevelopment.models.DecisionFeedback
import com.apollo.standardsdevelopment.models.JustificationForTC
import com.apollo.standardsdevelopment.repositories.DecisionFeedbackRepository
import com.apollo.standardsdevelopment.repositories.JustificationForTCRepository
import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.engine.repository.Deployment
import org.flowable.engine.runtime.ProcessInstance
import org.flowable.task.api.Task
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.util.ArrayList

@Service
class FormationOfTCService(private val runtimeService: RuntimeService,
                           private val taskService: TaskService,
                           @Qualifier("processEngine") private val processEngine: ProcessEngine,
                           private val repositoryService: RepositoryService,
                           private val justificationForTCRepository: JustificationForTCRepository,
                           private val decisionFeedbackRepository: DecisionFeedbackRepository
) {

    val PROCESS_DEFINITION_KEY = "sd_formation_of_technical_committee"
    val TASK_SPC = "SPC"
    val TASK_SAC = "SAC"

    fun deployProcessDefinition(): Deployment =repositoryService
            .createDeployment()
            .addClasspathResource("processes/formation_of_technical_committee.bpmn20.xml")
            .deploy()

    fun submitJustificationForFormationOfTC(justificationForTC: JustificationForTC):ProcessInstanceResponse
    {
        val variable:MutableMap<String,Any> = HashMap()
        justificationForTC.proposer?.let { variable.put("proposer",it) }
        justificationForTC.purpose?.let { variable.put("purpose",it) }
        justificationForTC.subject?.let { variable.put("subject",it) }

        justificationForTCRepository.save(justificationForTC)

        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variable)
        return ProcessInstanceResponse(processInstance.id, processInstance.isEnded)

    }

    fun getSPCTasks():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_SPC).list()
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


    fun decisionOnJustificationForTC(decision: Decision)
    {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        variables["approved"] = decision.decision
        taskService.complete(decision.taskId, variables)
    }

    fun uploadFeedbackOnJustification(decisionFeedback: DecisionFeedback)
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

    fun getSACTasks():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_SAC).list()
        return getTaskDetails(tasks)
    }

    fun decisionOnSPCFeedback(decision: Decision)
    {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        variables["approved"] = decision.decision
        taskService.complete(decision.taskId, variables)
    }

}