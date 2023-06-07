package org.kebs.app.kotlin.apollo.standardsdevelopment.services

import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.engine.repository.Deployment
import org.flowable.task.api.Task
import org.kebs.app.kotlin.apollo.standardsdevelopment.dto.ProcessInstanceResponse
import org.kebs.app.kotlin.apollo.standardsdevelopment.dto.TaskDetails
import org.kebs.app.kotlin.apollo.standardsdevelopment.models.SchemeMembershipRequest
import org.kebs.app.kotlin.apollo.standardsdevelopment.repositories.SchemeMembershipRequestRepository
import org.springframework.stereotype.Service

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

    //save a notification on the database and send out if it has been accepted or rejected
    fun joinRequest(schemeMembershipRequest: SchemeMembershipRequest): ProcessInstanceResponse {
        val variables: MutableMap<String, Any> = HashMap()
        schemeMembershipRequest.name.let { variables.put("name", it) }
        schemeMembershipRequest.address.let { variables.put("address", it) }
        schemeMembershipRequest.date.let { variables.put("date", it) }
        schemeMembershipRequest.designationOccupation.let { variables.put("designationOccupation", it) }
        schemeMembershipRequest.email.let { variables.put("email", it) }
        schemeMembershipRequest.phone.let { variables.put("phone", it) }


                schemeMembershipRequestRepository.save(schemeMembershipRequest)

                val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables)
                return ProcessInstanceResponse(processInstance.id, processInstance.isEnded)
            }

    //HOD SEC assigns Join request to an SIC officer
  //  fun assignSicOfficer
}
