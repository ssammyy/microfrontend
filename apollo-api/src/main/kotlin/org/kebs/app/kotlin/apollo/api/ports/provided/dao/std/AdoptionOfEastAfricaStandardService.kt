package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std

import org.kebs.app.kotlin.apollo.common.dto.std.*
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.std.*
import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.engine.history.HistoricActivityInstance
import org.flowable.engine.repository.Deployment
import org.flowable.engine.runtime.ProcessInstance
import org.flowable.task.api.Task
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.util.ArrayList

@Service
class AdoptionOfEastAfricaStandardService(private val runtimeService: RuntimeService,
                                          private val taskService: TaskService,
                                          @Qualifier("processEngine") private val processEngine: ProcessEngine,
                                          private val repositoryService: RepositoryService,
                                          private val sacSummaryRepository: SACSummaryRepository,
                                          private val decisionFeedbackRepository: DecisionFeedbackRepository
) {

    val PROCESS_DEFINITION_KEY = "adoption_of_east_africa_standard"
    val SPC_SEC = "SPC-SEC"
    val SAC_SEC = "SAC-SEC"
    val TC_sec = "TC-sec"

    fun deployProcessDefinition(): Deployment =repositoryService
            .createDeployment()
            .addClasspathResource("processes/adoption_of_east_africa_standard.bpmn20.xml")
            .deploy()

    fun submitSACSummary(sacSummary: SACSummary):ProcessInstanceResponse
    {
        val variable:MutableMap<String,Any> = HashMap()
        sacSummary.sl?.let { variable.put("sl",it) }
        sacSummary.ks?.let { variable.put("ks",it) }
        sacSummary.requestedBy?.let { variable.put("requestedBy",it) }
        sacSummary.issuesAddressed?.let { variable.put("issuesAddressed",it) }
        sacSummary.backgroundInformation?.let { variable.put("backgroundInformation",it) }

        sacSummaryRepository.save(sacSummary)

        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variable)
        return ProcessInstanceResponse(processInstance.id, processInstance.isEnded)
    }

    fun getSACSummaryTask():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(SPC_SEC).list()
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

    fun decisionOnSACSummary(decisionFeedback: DecisionFeedback)
    {
        val variables: MutableMap<String, Any> = java.util.HashMap()

        decisionFeedback.user_id?.let{variables.put("user_id", it)}
        decisionFeedback.item_id?.let{variables.put("item_id", it)}
        decisionFeedback.comment?.let{variables.put("comment", it)}
        decisionFeedback.taskId?.let{variables.put("taskId", it)}

        variables["approved"] = decisionFeedback.status!!
        taskService.complete(decisionFeedback.taskId, variables)

        decisionFeedbackRepository.save(decisionFeedback)
    }

    fun getSACSECTask():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(SAC_SEC).list()
        return getTaskDetails(tasks)
    }

    fun decisionOnSACSEC(decisionFeedback: DecisionFeedback)
    {
        val variables: MutableMap<String, Any> = java.util.HashMap()

        decisionFeedback.user_id?.let{variables.put("user_id", it)}
        decisionFeedback.item_id?.let{variables.put("item_id", it)}
        decisionFeedback.comment?.let{variables.put("comment", it)}
        decisionFeedback.taskId?.let{variables.put("taskId", it)}

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
                    activity.activityId + " took " + activity.durationInMillis + " milliseconds")
        }
        return activities
    }

}
