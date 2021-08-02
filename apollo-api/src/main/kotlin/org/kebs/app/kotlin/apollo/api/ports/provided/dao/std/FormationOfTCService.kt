package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std




import org.kebs.app.kotlin.apollo.store.model.std.DecisionFeedback

import org.kebs.app.kotlin.apollo.store.repo.std.DecisionFeedbackRepository
import org.kebs.app.kotlin.apollo.store.repo.std.JustificationForTCRepository
import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.engine.history.HistoricActivityInstance
import org.flowable.engine.repository.Deployment
import org.flowable.task.api.Task
import org.kebs.app.kotlin.apollo.common.dto.std.*
import org.kebs.app.kotlin.apollo.store.model.std.*
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
        .addClasspathResource("processes/std/formation_of_technical_committee.bpmn20.xml")
        .deploy()

    fun submitJustificationForFormationOfTC(justificationForTC: JustificationForTC): ProcessInstanceResponse
    {
        val variable:MutableMap<String,Any> = HashMap()
        justificationForTC.proposer?.let { variable.put("proposer",it) }
        justificationForTC.purpose?.let { variable.put("purpose",it) }
        justificationForTC.subject?.let { variable.put("subject",it) }
        justificationForTC.scope?.let { variable.put("scope",it) }
        justificationForTC.targetDate?.let { variable.put("targetDate",it) }
        justificationForTC.proposedRepresentation?.let { variable.put("proposedRepresentation",it) }
        justificationForTC.programmeOfWork?.let { variable.put("programmeOfWork",it) }
        justificationForTC.organization?.let { variable.put("organization",it) }
        justificationForTC.liaisonOrganization?.let { variable.put("liaisonOrganization",it) }
        justificationForTC.dateOfPresentation?.let { variable.put("dateOfPresentation",it) }
        justificationForTC.nameOfTC?.let { variable.put("nameOfTC",it) }
        justificationForTC.referenceNumber?.let { variable.put("referenceNumber",it) }


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


    fun decisionOnJustificationForTC(decisionFeedback: DecisionFeedback)
    {
        val variables: MutableMap<String, Any> = java.util.HashMap()

        decisionFeedback.user_id?.let{variables.put("user_id", it)}
        decisionFeedback.item_id?.let{variables.put("item_id", it)}
        decisionFeedback.comment?.let{variables.put("comment", it)}
        decisionFeedback.taskId?.let{variables.put("taskId", it)}

        variables["approved"] = decisionFeedback.status!!
        taskService.complete(decisionFeedback.taskId, variables)
    }

    fun uploadFeedbackOnJustification(decisionFeedback: DecisionFeedback)
    {
        val variable:MutableMap<String, Any> = HashMap()

        decisionFeedback.user_id.let{variable.put("user_id", it)}
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

    fun decisionOnSPCFeedback(decisionFeedback: DecisionFeedback)
    {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        decisionFeedback.user_id.let{variables.put("user_id", it)}
        decisionFeedback.item_id?.let{variables.put("item_id", it)}
        decisionFeedback.comment?.let{variables.put("comment", it)}
        decisionFeedback.taskId?.let{variables.put("taskId", it)}

        variables["approved"] = decisionFeedback.status!!
        taskService.complete(decisionFeedback.taskId, variables)
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
