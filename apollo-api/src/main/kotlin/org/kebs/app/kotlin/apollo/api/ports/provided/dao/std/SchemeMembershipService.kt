package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std

import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.engine.repository.Deployment
import org.flowable.task.api.Task
import org.kebs.app.kotlin.apollo.common.dto.std.ProcessInstanceResponse
import org.kebs.app.kotlin.apollo.common.dto.std.TaskDetails
import org.kebs.app.kotlin.apollo.store.model.std.SchemeMembershipRequest
import org.kebs.app.kotlin.apollo.store.repo.std.SchemeMembershipRequestRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class SchemeMembershipService(private val runtimeService: RuntimeService,
                              private val taskService: TaskService,
                              private val processEngine: ProcessEngine,
                              private val repositoryService: RepositoryService,
                              private val schemeMembershipRequestRepository: SchemeMembershipRequestRepository
) {

    var PROCESS_DEFINITION_KEY: String = "schemeMembership"
    val TASK_CANDIDATE_GROUP_HOD_SEC = "SD_HEAD_OF_SIC"
    val TASK_CANDIDATE_GROUP_SIC_OFFICER = "SD_SIC_OFFICER"
    val sic_assignee = "3"

    //deployment of flowable function
    fun deployProcessDefinition(): Deployment = repositoryService
            .createDeployment()
            .addClasspathResource("processes/Scheme_Membership.bpmn20.xml")
            .deploy()

    //Main task details function where tasks for different candidate groups can be queried
    private fun getTaskDetails(tasks: List<Task>): List<TaskDetails>? {
        val taskDetails: MutableList<TaskDetails> = ArrayList()
        for (task in tasks) {
            val processVariables = taskService.getVariables(task.id)
            taskDetails.add(TaskDetails(task.id, task.name, processVariables))
        }
        return taskDetails
    }

    //scheme membership join request
    fun joinRequest(schemeMembershipRequest: SchemeMembershipRequest): ProcessInstanceResponse {
        val variables: MutableMap<String, Any> = HashMap()
        schemeMembershipRequest.name?.let { variables.put("name", it) }
        schemeMembershipRequest.address?.let { variables.put("address", it) }
        schemeMembershipRequest.date?.let { variables.put("date", it) }
        schemeMembershipRequest.designationOccupation?.let { variables.put("designationOccupation", it) }
        schemeMembershipRequest.email?.let { variables.put("email", it) }
        schemeMembershipRequest.phone?.let { variables.put("phone", it) }
        schemeMembershipRequest.account_type?.let { variables.put("account_type", it) }


        schemeMembershipRequestRepository.save(schemeMembershipRequest)

        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables)
        return ProcessInstanceResponse(processInstance.id, processInstance.isEnded)
    }

    //HOD tasks
    //request task list retrieval
    fun getHodTasks(): List<TaskDetails?>? {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_HOD_SEC).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    fun getAllUnassignedTasks(): MutableIterable<SchemeMembershipRequest> =
            schemeMembershipRequestRepository.findAll()
    //HOD SEC assigns Join request to an SIC officer


    //  fun assignSicOfficer
    fun assignSICOfficer (schemeMembershipRequest: SchemeMembershipRequest){
        val variables: MutableMap<String, Any> = HashMap()
        schemeMembershipRequest.taskID?.let { variables.put("taskID", it) }
        schemeMembershipRequest.sic_assigned_id?.let { variables.put("sic_id", it) }

        taskService.complete(schemeMembershipRequest.taskID, variables)
    }

    //get assignee SIC officer tasks
    fun getSICTasks(): List<TaskDetails?>? {
        val tasks = taskService.createTaskQuery().taskAssignee(sic_assignee).list()
        return getTaskDetails(tasks)
    }

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
