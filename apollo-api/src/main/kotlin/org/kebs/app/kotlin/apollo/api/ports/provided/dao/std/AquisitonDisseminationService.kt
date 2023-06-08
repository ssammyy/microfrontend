package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std


import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.engine.repository.Deployment
import org.flowable.task.api.Task
import org.kebs.app.kotlin.apollo.common.dto.std.ProcessInstanceResponse
import org.kebs.app.kotlin.apollo.common.dto.std.TaskDetails
import org.kebs.app.kotlin.apollo.store.model.std.SdNewRequestEntity
import org.kebs.app.kotlin.apollo.store.repo.std.SdNewRequestRepository
import org.springframework.stereotype.Service
import java.util.ArrayList

@Service
class AquisitonDisseminationService(
    private val runtimeService: RuntimeService,
    private val taskService: TaskService,
    private val repositoryService: RepositoryService,
    private val sdNewRequestRepository: SdNewRequestRepository
) {
    var PROCESS_DEFINITION_KEY: String = "acquisitionDisseminationProcessFlow"
    val TASK_CANDIDATE_HOD_SIC = "SD_HOD_SIC"
    val user_id = "3"
    val process_name: String = "aquisition"
    val sic_assignee: String = user_id.plus(process_name)

    //deployment of flowable function
    fun deployProcessDefinition(): Deployment = repositoryService
        .createDeployment()
        .addClasspathResource("processes/Acquisition_and_Dissemination_Process_Flow.bpmn20.xml")
        .deploy()

    //request for Publication or Standard
    fun makeRequest(sdNewRequestEntity: SdNewRequestEntity): ProcessInstanceResponse {
        val variables: MutableMap<String, Any> = HashMap()
        sdNewRequestEntity.customerName.let { variables.put("requesterName", it!!) }
        sdNewRequestEntity.requestType.let { variables.put("requestType", it!!) }
        sdNewRequestEntity.requestTitle.let { variables.put("requestTitle", it!!) }
        sdNewRequestEntity.requestDate.let { variables.put("requestDate", it!!) }
        sdNewRequestEntity.quantity.let { variables.put("quantity", it!!) }

        sdNewRequestRepository.save(sdNewRequestEntity)

        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables)
        return ProcessInstanceResponse(processInstance.id, processInstance.isEnded)
    }

    //Main task details function where tasks for different candidate groups can be queried
    private fun getTaskDetails(tasks: List<Task>): List<TaskDetails>? {
        val taskDetails: MutableList<TaskDetails> = ArrayList()
        for (task in tasks) {
            val processVariables = taskService.getVariables(task.id)
            taskDetails.add(TaskDetails(task.id, task.name, processVariables))
        }
        return taskDetails
    }

    //HOD tasks
    //request task list retrieval
    fun getHodTasks(): List<TaskDetails?>? {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_HOD_SIC)
            .processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    //  fun assignSicOfficer
    fun assignSICOfficer(sdNewRequestEntity: SdNewRequestEntity) {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        sdNewRequestEntity.taskID?.let { variables.put("taskID", it) }
        (sdNewRequestEntity.sic_assigned_id).plus(process_name)?.let { variables.put("sic_id", it) }

        taskService.complete(sdNewRequestEntity.taskID, variables)
    }

    //get assignee SIC officer tasks
    fun getSICTasks(): List<TaskDetails?>? {
        val tasks = taskService.createTaskQuery().taskAssignee(sic_assignee).list()
        return getTaskDetails(tasks)
    }

    //  fun select if publication is available
    fun publicationAvailable(sdNewRequestEntity: SdNewRequestEntity) {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        sdNewRequestEntity.isAvailable?.let { variables.put("Yes", it) }
        sdNewRequestEntity.taskID?.let { variables.put("taskID", it) }

        taskService.complete(sdNewRequestEntity.taskID, variables)
    }

    //fun send customer email with list
    fun sendEmailList(sdNewRequestEntity: SdNewRequestEntity) {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        sdNewRequestEntity.emailList?.let { variables.put("emailList", it) }

        taskService.complete(sdNewRequestEntity.taskID, variables)
    }


}
