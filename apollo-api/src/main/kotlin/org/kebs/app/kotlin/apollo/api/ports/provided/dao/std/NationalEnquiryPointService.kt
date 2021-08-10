package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std


import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.engine.repository.Deployment
import org.flowable.task.api.Task
import org.kebs.app.kotlin.apollo.common.dto.std.ProcessInstanceResponse
import org.kebs.app.kotlin.apollo.common.dto.std.TaskDetails
import org.kebs.app.kotlin.apollo.store.model.std.DepartmentResponse
import org.kebs.app.kotlin.apollo.store.model.std.InformationTracker
import org.kebs.app.kotlin.apollo.store.model.std.NationalEnquiryPoint
import org.kebs.app.kotlin.apollo.store.repo.std.DepartmentResponseRepository
import org.kebs.app.kotlin.apollo.store.repo.std.InformationTrackerRepository
import org.kebs.app.kotlin.apollo.store.repo.std.NationalEnquiryPointRepository
import org.springframework.stereotype.Service
import java.time.LocalDate


@Service
class NationalEnquiryPointService(
        private val runtimeService: RuntimeService,
        private val taskService: TaskService,
        private val processEngine: ProcessEngine,
        private val repositoryService: RepositoryService,
        private val nationalEnquiryPointRepository: NationalEnquiryPointRepository,
        private val informationTrackerRepository: InformationTrackerRepository,
        private val departmentResponseRepository: DepartmentResponseRepository
) {

    var PROCESS_DEFINITION_KEY: String = "nationalEnquiryPoint"
    val requesterId: String = "requesterId"
    val TASK_CANDIDATE_GROUP_NEP = "SD_NEP_OFFICER"
    val TASK_CANDIDATE_GROUP_DEPT = "DIVISION_ORG"

    //deployment of flowable function
    fun deployProcessDefinition(): Deployment = repositoryService
            .createDeployment()
            .addClasspathResource("processes/std/National_Enquiry_Point.bpmn20.xml")
            .deploy()


    //********************************************************** process service methods **********************************************************

    //make enquiry and process initiation function
    fun notificationRequest(nationalEnquiryPoint: NationalEnquiryPoint): ProcessInstanceResponse {


        val variables: MutableMap<String, Any> = HashMap()
        nationalEnquiryPoint.requesterName.let { variables.put("requesterName", it) }
        nationalEnquiryPoint.requesterComment.let { variables.put("requesterComment", it) }
        nationalEnquiryPoint.requesterCountry.let { variables.put("requesterCountry", it) }
        nationalEnquiryPoint.requesterEmail.let { variables.put("requesterEmail", it) }
        nationalEnquiryPoint.requesterInstitution.let { variables.put("requesterInstitution", it) }
        nationalEnquiryPoint.requesterPhone.let { variables.put("requesterPhone", it) }
        nationalEnquiryPoint.requesterSubject.let { variables.put("requesterSubject", it) }
        nationalEnquiryPoint.requestDate.let { variables.put("requestDate", it) }

        nationalEnquiryPointRepository.save(nationalEnquiryPoint)

        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables)
        return ProcessInstanceResponse(processInstance.id, processInstance.isEnded)
    }

    //request task list retrieval
    fun getManagerTasks(): List<TaskDetails?>? {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_NEP)
                .processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    //*** Not used *** but closes any Task, linked to task close endpoint
    fun closeTask(taskId: String) {
        taskService.complete(taskId)
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

    //Checker function by NEP_OFFICER for if informaion is available or not
    fun informationAvailable(taskId: String, isAvailable: Boolean): String {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        isAvailable.let { variables.put("Yes", it) }

        taskService.complete(taskId, variables)

        return "Information Is Available"
    }

    //send feedback email if info is available and save response
    fun sendEmailInfoAvailable(informationTracker: InformationTracker, taskId: String): String {
        val variable: MutableMap<String, Any> = HashMap()
        informationTracker.nepOfficerId?.let { variable.put("NEPOfficer", it) }
        informationTracker.feedbackSent?.let { variable.put("feedbackSent", it) }
        informationTracker.requesterEmail?.let { variable.put("requesterEmail", it) }

        informationTrackerRepository.save(informationTracker)

        taskService.complete(taskId, variable)
        println("Process has ended and an email has been sent out with feedback")

        return "Email Sent Successfully"
    }

    //function to handle the response by departments or organizations responding to enquiries
    fun departmentOrganizationResponse(departmentResponse: DepartmentResponse, taskId: String): String {
        val variable: MutableMap<String, Any> = HashMap()
        departmentResponse.departmentResponseID.let { variable.put("departmentResponseID", it) }
        departmentResponse.enquiryID.let { variable.put("enquiryID", it) }
        departmentResponse.feedbackProvided.let { variable.put("feedbackProvided", it) }

        departmentResponseRepository.save(departmentResponse)

        taskService.complete(taskId, variable)

        return "Response sent successfully"
    }

    //displays tasks due for organizations
    fun getDepartmentTasks(): List<TaskDetails?>? {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_DEPT)
                .processDefinitionKey(PROCESS_DEFINITION_KEY).list()
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
                    activity.activityId + " took " + activity.durationInMillis + " milliseconds"
            )
        }

    }
}

private fun Boolean.toBoolean() {

}

