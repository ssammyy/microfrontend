package org.kebs.app.kotlin.apollo.api.ports.provided.bpmn

import mu.KotlinLogging
import org.flowable.engine.ManagementService
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.engine.delegate.DelegateExecution
import org.flowable.engine.runtime.ProcessInstance
import org.flowable.task.api.Task
import org.flowable.task.service.delegate.DelegateTask
import org.joda.time.DateTime
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.di.BpmnTaskDetails
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.store.repo.IUserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Lazy
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.HashMap

@Service
class BpmnCommonFunctions(

    private val runtimeService: RuntimeService,
    private val repositoryService: RepositoryService,
    private val taskService: TaskService,
    private val managementService: ManagementService,
    private val userRepo: IUserRepository
//    private val destinationInspectionService: DestinationInspectionDaoServices,

    ) {
    @Value("\${bpmn.task.default.duration}")
    lateinit var defaultDuration: Integer

    @Value("\${bpmn.timer.boundary.task.delay}")
    var timerBoundaryTaskDelay: Long = 0

    @Lazy
    @Autowired
    lateinit var destinationInspectionDaoServices: DestinationInspectionDaoServices


    fun startBpmnProcess(
            processDefinitionKey: String,
            businessKey: String,
            processTaskVariables: HashMap<String, Any>,
            assigneeId: Long
    ): HashMap<String, String>? {
        KotlinLogging.logger { }.info("Starting bpmn process $processDefinitionKey with key $businessKey")
        //var processInstanceId : String = ""
        val returnMap: HashMap<String, String> = HashMap()
        try {
            var processInstanceId: String = ""
            //Start the process
            runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, processTaskVariables)?.let {processInstance ->
                processInstanceId = processInstance.processInstanceId
                KotlinLogging.logger { }.info("bpmn process started with pid $processInstanceId")
            }?: run{
                KotlinLogging.logger { }.info("unable to start bpmn process started with pid"); return null
            }

            Thread.sleep(timerBoundaryTaskDelay)
            //Fetch the task
            KotlinLogging.logger { }.info("Fetching taskId for pid $processInstanceId")
            taskService.createTaskQuery().processInstanceId(processInstanceId).list()[0]?.let{task ->
                KotlinLogging.logger { }.info("pid $processInstanceId returned taskId ${task.id}")
                //Populate map to return
                returnMap["processInstanceId"] = processInstanceId
                returnMap["taskId"] = task.id

                task.assignee = assigneeId.toString()  //set the assignee
                task.dueDate = DateTime(Date()).plusDays(defaultDuration.toInt()).toDate()  //set the due date
                taskService.saveTask(task)

                return returnMap
            }?: run{
                KotlinLogging.logger { }
                    .info("unable to fetch task for bpmn process started with pid $processInstanceId"); return null
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }

    fun getTasks(parameterName: String, parameterValue: String): List<Task>? {
        var tasks: List<Task>? = null
        try {
            KotlinLogging.logger { }.trace("Fetching task where $parameterName = $parameterValue")
            val pName: String = parameterName.toUpperCase()
            if (pName == "PROCESSINSTANCEID") {
                tasks = taskService.createTaskQuery().processInstanceId(parameterValue).includeProcessVariables().includeTaskLocalVariables().list()
            } else if (pName == "TASKID") {
                tasks = taskService.createTaskQuery().taskId(parameterValue).includeProcessVariables().includeTaskLocalVariables().list()
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return tasks
    }

    fun getTaskByTaskDefinitionKey(processInstanceId: String, taskDefinitionKey: String): Task? {
        try {
            KotlinLogging.logger { }.info("Fetching task where processInstanceId = $processInstanceId and taskDefinitionKey like $taskDefinitionKey")
            return taskService.createTaskQuery().processInstanceId(processInstanceId).taskDefinitionKeyLike(taskDefinitionKey).list()[0]
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }

    fun getTasksByAssigneeAndBusinessKey(assigneeId: Long, businessKey: String): List<Task>? {
        try {
            KotlinLogging.logger { }.trace("Fetching $businessKey tasks for $assigneeId")
            return taskService.createTaskQuery().processInstanceBusinessKey(businessKey).taskAssigneeLikeIgnoreCase(assigneeId.toString()).list()
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }
    fun getTaskByProcessInstanceId(processInstanceId: String): Task? {
        try {
            KotlinLogging.logger { }.trace("Fetching task where processInstanceId = $processInstanceId")
            return taskService.createTaskQuery().processInstanceId(processInstanceId).list()[0]
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }

    fun fetchAllTasksByAssignee(assigneeId: Long, pidPrefix:String): List<BpmnTaskDetails>? {
        try {
            taskService.createTaskQuery().taskAssignee(assigneeId.toString()).processDefinitionKeyLikeIgnoreCase("$pidPrefix%").list()?.let{ tasks->
                return generateTaskDetails(tasks)
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }

    fun fetchAllTasks(pidPrefix:String): List<BpmnTaskDetails>? {
        try {
            taskService.createTaskQuery().processDefinitionKeyLikeIgnoreCase("$pidPrefix%").list()?.let{ tasks->
                return generateTaskDetails(tasks)
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }


    fun getTaskById(taskId: String): Task? {
        try {
            KotlinLogging.logger { }.trace("Fetching task for id = $taskId")
            return taskService.createTaskQuery().taskId(taskId).singleResult()
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }

    fun getTaskVariables(taskId: String): Map<String, Any>? {
        KotlinLogging.logger { }.trace("Fetching task variables for $taskId")
        try {
            return taskService.getVariables(taskId)
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }

    fun getProcessVariables(processInstanceId: String): Map<String, Any>? {
        KotlinLogging.logger { }.trace("Fetching process variables for $processInstanceId")
        try {
            return runtimeService.getVariables(processInstanceId)
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }

    fun getOverdueTasks(dueDate:Date, processDefinitionKeyPrefix:String): List<Task>? {
        try {
            KotlinLogging.logger { }.trace("Fetching task where due date <  $dueDate")
            return taskService.createTaskQuery().taskDueBefore(dueDate).processDefinitionKeyLikeIgnoreCase("$processDefinitionKeyPrefix%").list()
            //return taskService.createTaskQuery().taskDueBefore(dueDate).list()
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return null
    }

    fun getJobs(processInstanceId: String) {
        try {
            KotlinLogging.logger { }.trace("Fetching jobs for $processInstanceId")
            println("Jobs--------------")
            managementService.createJobQuery().processInstanceId(processInstanceId).list()?.let{ jobsList->
                for(job in jobsList){
                    KotlinLogging.logger { }.info("${job.createTime} -- ${job.id} -- ${job.processInstanceId} -- ${job.jobType}")
                }
            }
            println("Timer Jobs--------------")
            managementService.createTimerJobQuery().processInstanceId(processInstanceId).list()?.let{ jobsList->
                for(job in jobsList){
                    KotlinLogging.logger { }.info("${job.createTime} -- ${job.id} -- ${job.processInstanceId} -- ${job.jobType}")
                }
            }


        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        //return null
    }

    fun updateVariable(taskId: String, variableName: String, value: String) {
        KotlinLogging.logger { }.info("Updating $variableName to $value for $taskId")
        try {
            taskService.setVariable(taskId, variableName, value)
            KotlinLogging.logger { }.info("$variableName updated to $value for $taskId")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
    }

    fun completeTask(taskId: String) {
        try {
            KotlinLogging.logger { }.info("Completing task $taskId")
            taskService.complete(taskId)
            KotlinLogging.logger { }.info("Completed task $taskId")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
    }

    fun completeTask(variables: HashMap<String, Any>,taskDefinitionKey: String) : HashMap<String, Any>? {
        try {
            getTaskByTaskDefinitionKey(variables["processInstanceId"].toString(), taskDefinitionKey)?.let { task ->
                completeTask(task.id)
                variables["assigneeId"] = task.assignee
                variables["taskId"] = task.id
                variables["task"] = task
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return variables
    }

    fun generateTaskDetails(tasks:List<Task>) : List<BpmnTaskDetails>{
        val taskList: MutableList<BpmnTaskDetails> = mutableListOf()
        for (task in tasks) {
            getTaskVariables(task.id)?.let { it ->
                taskList.add(BpmnTaskDetails(it["objectId"].toString().toLong(),task))
            }
        }
        return taskList
    }

    fun assignTask(processInstanceId: String, taskDefinitionKey:String?, assigneeId: Long): Boolean {
        KotlinLogging.logger { }.info("Assign next task begin")
        try {
            var localAssigneeId: String = assigneeId.toString()
            var task: Task? = null

            //complaint.let {
            taskDefinitionKey?.let{
                task = getTaskByTaskDefinitionKey(processInstanceId,taskDefinitionKey)
            }?: run {
                task = getTasks("processInstanceId",processInstanceId)?.get(0)
            }

            task?.let {task->
                userRepo.findByIdOrNull(localAssigneeId.toLong())?.let { usersEntity ->
                    updateVariable(task.id, "email", usersEntity.email.toString())
                }
                //Refetch the task because we have updated the email variable
                getTaskById(task.id)?.let { updatedTask->
                    updatedTask.assignee = localAssigneeId
                    taskService.saveTask(updatedTask)
                    KotlinLogging.logger { }.info("Task ${updatedTask.name} assigned to $localAssigneeId")
                    return true
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return false
    }

    fun booleanToInt(b: Boolean): Int {
        return if (b) 1 else 0
    }

    fun generateBSNumber() {
        KotlinLogging.logger { }.info("Generating BS Number............")
    }

    fun submitQaDmAppReviewForAssessment(permitId:String) {
        KotlinLogging.logger { }.info("Submitting permit $permitId for assessment............")
    }

    fun autoPopulateApplication(permitId:String) {
        KotlinLogging.logger { }.info("Autopopulating application for permit $permitId............")
    }

    fun computePayment(permitId:String) {
        KotlinLogging.logger { }.info("Computing payment for application for permit $permitId............")
    }

    fun checkIfRenewal(permitId:String) : String {
        KotlinLogging.logger { }.info("Checking if renewal $permitId............")
        return "0"
    }

    fun updateCdStatusOnSw(cdId:String) : String {
        KotlinLogging.logger { }.info("Updating CD status on SW for $cdId............")
        return "0"
    }

    //TODO: Implement KeSws request for inspection here
    fun submitMotorVehicleInspectionRequest(cdItemId:String) {
        KotlinLogging.logger { }.info("Submitting Motor Vehcle Item $cdItemId Inspection request to KeSWS............")
        //Update inspectionNotificationStatus & inspectionNotificationDate after request to KeSws
        destinationInspectionDaoServices.updateInspectionNotificationSent(cdItemId.toLong())
    }

    fun sendSwPaymentDemandNote(cdId:String) {
        KotlinLogging.logger { }.info("Sending Demand Note for CD $cdId to KeSWS............")
    }

    fun sendSwPaymentComplete(cdId:String) {
        KotlinLogging.logger { }.info("Sending Demand Note Payment Complete for CD $cdId to KeSWS............")
    }

    fun notifyTargetApproval(objectId: Long, supervisorTargetApproved: Int) {
        KotlinLogging.logger { }.info("Sending email for Object ID: $objectId target approval status is: $supervisorTargetApproved............")
    }

}
