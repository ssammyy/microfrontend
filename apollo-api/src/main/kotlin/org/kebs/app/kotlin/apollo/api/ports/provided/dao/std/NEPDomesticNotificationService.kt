package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std


import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.engine.repository.Deployment
import org.flowable.task.api.Task
import org.kebs.app.kotlin.apollo.common.dto.std.*
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.std.*
import org.springframework.stereotype.Service


@Service
class NEPDomesticNotificationService(
    private val runtimeService: RuntimeService,
    private val taskService: TaskService,
    private val processEngine: ProcessEngine,
    private val repositoryService: RepositoryService,
    private val notificationReceivedRepository: NotificationReceivedRepository,
    private val nepNotificationRepository: NEPNotificationRepository,
    private val applicationMapProperties: ApplicationMapProperties
) {
    val callUrl=applicationMapProperties.mapKebsLevyUrl
    var PROCESS_DEFINITION_KEY: String = "NEPDomesticNotification"
    val requesterId: String = "requesterId"
    val TASK_CANDIDATE_GROUP_NEP = "SD_NEP_OFFICER"
    val TASK_CANDIDATE_GROUP_MANAGER = "MANAGER"

    //deployment of flowable function
    fun deployProcessDefinition(): Deployment = repositoryService
        .createDeployment()
        .addClasspathResource("processes/NEP_Domestic_Notification.bpmn20.xml")
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
    fun notificationReceivedfun(notificationReceived: NotificationReceived): ProcessInstanceResponse {
        val variables: MutableMap<String, Any> = HashMap()
        notificationReceived.nepOfficerId.let { variables.put("nepOfficerId", it) }
        notificationReceived.notificationCategory.let { variables.put("NotificationCategory", it) }
        notificationReceived.notificationDueIndex.let {
            variables.put("NotificationDueIndex", it)
            notificationReceived.tcSecretaryId.let {
                variables.put("tcSecretaryId", it)


                notificationReceivedRepository.save(notificationReceived)

                val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables)
                return ProcessInstanceResponse(processInstance.id, processInstance.isEnded)
            }

        }
    }

    //request task list retrieval
    fun getManagerTasks(): List<TaskDetails?>? {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_NEP)
            .processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    //Checker function by NEP_OFFICER  to approve or reject Notification
    fun notificationAccepted(taskId: String, isAccepted: Boolean): String {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        isAccepted.let { variables.put("Yes", it) }

        taskService.complete(taskId, variables)

        return "Information Is Available"
    }

    //Draft Notification function by NEP officer
    fun draftNotification(nepNotification: NEPNotification, taskId: String) {
        val variable: MutableMap<String, Any> = HashMap()
        nepNotification.tcNotificationID.let { variable.put("tcNotificationID", it) }
        nepNotification.nepOfficerID.let { variable.put("nepOfficerID", it) }
        nepNotification.notificationDocumentIndex.let { variable.put("notificationDocumentIndex", it) }

        nepNotificationRepository.save(nepNotification)
        taskService.complete(taskId, variable)
        println("Notification created")

    }

    //check manager tasks pending approval
    fun getManagersTasks(): List<TaskDetails?>? {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_MANAGER)
            .processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    //manager accept or reject tasks
    fun managerAcceptReject(taskId: String, isAvailable: Boolean): String {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        isAvailable.let { variables.put("Accept", it) }

        taskService.complete(taskId, variables)

        return "Information Is Available"
    }

    //nep officer uploads to WTO NSS
    fun uploadToWtoNss(taskId: String) {
        taskService.complete(taskId)
        println("Uploaded and task successfully ended")
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
