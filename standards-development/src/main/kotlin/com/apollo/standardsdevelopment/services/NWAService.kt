package org.kebs.app.kotlin.apollo.standardsdevelopment.services

import org.kebs.app.kotlin.apollo.standardsdevelopment.dto.ProcessInstanceResponse
import org.kebs.app.kotlin.apollo.standardsdevelopment.dto.TaskDetails
import org.kebs.app.kotlin.apollo.standardsdevelopment.models.NWAJustification
import org.kebs.app.kotlin.apollo.standardsdevelopment.repositories.NWAJustificationRepository
import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.engine.repository.Deployment
import org.flowable.task.api.Task
import org.springframework.stereotype.Service
import java.util.ArrayList

@Service
class NWAService(private val runtimeService: RuntimeService,
                 private val taskService: TaskService,
                 private val processEngine: ProcessEngine,
                 private val repositoryService: RepositoryService,
                 private val nwaJustificationRepository: NWAJustificationRepository
) {

    val PROCESS_DEFINITION_KEY = "std_KenyaNationalWorkshopAgreementModule"
    val TASK_CANDIDATE_KNW_SEC ="KNW_SEC"

    //deploy bpmn file
    fun deployProcessDefinition(): Deployment =repositoryService
        .createDeployment()
        .addClasspathResource("processes/Kenya_National_workshop_Agreement_Module.bpmn20.xml")
        .deploy()

    //start the process by process Key
    fun startProcessByKey() :ProcessInstanceResponse
    {

        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY)
        return ProcessInstanceResponse(processInstance.id, processInstance.isEnded)
    }

    //prepare justificaion
    fun prepareJustification(nwaJustification: NWAJustification){
        val variables: MutableMap<String, Any> = HashMap()
        nwaJustification.meetingDate?.let{ variables.put("meetingDate", it)}
        nwaJustification.knw?.let{ variables.put("knw", it)}
        nwaJustification.knwSecretary?.let{ variables.put("knwSecretary", it)}
        nwaJustification.sl?.let{ variables.put("sl", it)}
        nwaJustification.requestNumber?.let{ variables.put("requestNumber", it)}
        nwaJustification.requestedBy?.let{ variables.put("requestedBy", it)}
        nwaJustification.issuesAddressed?.let{ variables.put("issuesAddressed", it)}
        nwaJustification.knwAcceptanceDate?.let{ variables.put("knwAcceptanceDate", it)}
        nwaJustification.referenceMaterial?.let{ variables.put("referenceMaterial", it)}
        nwaJustification.department?.let{ variables.put("department", it)}
        variables["Yes"] = nwaJustification.approved

        nwaJustificationRepository.save(nwaJustification)

        taskService.complete(nwaJustification.taskId)
    }



        //Function to retrieve task details for any candidate group
    private fun getTaskDetails(tasks: List<Task>): List<TaskDetails> {
        val taskDetails: MutableList<TaskDetails> = ArrayList()
        for (task in tasks) {
            val processVariables = taskService.getVariables(task.id)
            taskDetails.add(TaskDetails(task.id, task.name, processVariables))
        }
        return taskDetails
    }

    //Return task details for KNW_SEC
    fun getKNWTasks():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_KNW_SEC).list()
        return getTaskDetails(tasks)
    }

    //Decision
    fun decisionOnJustification(nwaJustification: NWAJustification)
    {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        variables["Yes"] = nwaJustification.approved
        taskService.complete(nwaJustification.taskId, variables)
    }

    //check task history
    //returns process history for processes executed
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
                activity.activityId + " took " + activity.durationInMillis + " milliseconds")
        }

    }
}
