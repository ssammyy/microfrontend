package com.apollo.standardsdevelopment.services

import com.apollo.standardsdevelopment.dto.ProcessInstanceResponse
import com.apollo.standardsdevelopment.dto.TaskDetails
import com.apollo.standardsdevelopment.models.*
import com.apollo.standardsdevelopment.repositories.*
import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.engine.repository.Deployment
import org.flowable.task.api.Task
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.HashMap

@Service
class StandardRequestService(private val runtimeService: RuntimeService,
                             private val taskService: TaskService,
                             private val processEngine: ProcessEngine,
                             private val repositoryService: RepositoryService,
                             private val standardRequestRepository: StandardRequestRepository,
                             private val standardNWIRepository: StandardNWIRepository,
                             private val standardJustificationRepository: StandardJustificationRepository,
                             private val standardWorkPlanRepository: StandardWorkPlanRepository,
                             private val productRepository: ProductRepository
) {

    val PROCESS_DEFINITION_KEY = "requestModule"
    val TASK_CANDIDATE_GROUP_HOF ="HOF"
    val TASK_CANDIDATE_GROUP_TC_SEC ="TC-sec"
    val TASK_CANDIDATE_GROUP_TC ="TC"
    val TASK_CANDIDATE_GROUP_SPC_SEC="SPC-sec"

    fun deployProcessDefinition(): Deployment =repositoryService
                .createDeployment()
                .addClasspathResource("processes/request_module.bpmn20.xml")
                .deploy()


    fun requestForStandard(standardRequest: StandardRequest): ProcessInstanceResponse
    {
        val variables: MutableMap<String, Any> = HashMap()
        standardRequest.requestNumber?.let { variables.put("requestNumber", it) }
        standardRequest.user?.id?.let { variables.put("requestor_id", it) }
        standardRequest.technicalCommittee?.id?.let { variables.put("tc_id", it) }
        standardRequest.department?.id?.let { variables.put("department_id", it) }
        standardRequest.productSubCategory?.id?.let { variables.put("product_category", it) }

        standardRequestRepository.save(standardRequest)
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables)
        return ProcessInstanceResponse(processInstance.id, processInstance.isEnded)
    }

    fun getHOFTasks():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_HOF).list()
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

    fun hofReview(taskId: String?)
    {
        println("HOF has finished the review")
        taskService.complete(taskId)
    }

    fun getTCSECTasks():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_TC_SEC).list()
        return getTaskDetails(tasks)
    }

    fun uploadNWI(standardNWI: StandardNWI, taskId: String?)
    {
       val variable:MutableMap<String, Any> = HashMap()
        standardNWI.proposalTitle?.let{variable.put("proposal_title", it)}
        standardNWI.nameOfProposer?.let{variable.put("name_of_proposer", it)}

        print(standardNWI.toString())

        standardNWIRepository.save(standardNWI)
        taskService.complete(taskId)
        println("TC-SEC has uploaded NWI")

    }


    fun getTCTasks():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_TC).list()
        return getTaskDetails(tasks)
    }

    fun decisionOnNWI(taskId: String?, approved: Boolean)
    {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        variables["approved"] = approved
        taskService.complete(taskId, variables)
    }

    fun getTCSecTasks():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_TC_SEC).list()

        return getTaskDetails(tasks)
    }


    fun uploadJustification(standardJustification: StandardJustification, taskId: String?)
    {
        val variable:MutableMap<String, Any> = HashMap()
        standardJustification.title?.let{variable.put("title", it)}
        standardJustification.scope?.let{variable.put("scope", it)}
        standardJustification.purpose?.let{variable.put("purpose", it)}

        print(standardJustification.toString())

        standardJustificationRepository.save(standardJustification)

        taskService.complete(taskId)
        println("TC-SEC has uploaded Justification")

    }

    fun getSPCSecTasks():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_SPC_SEC).list()
        return getTaskDetails(tasks)
    }

    fun decisionOnJustification(taskId: String?, approved: Boolean)
    {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        variables["spc_approved"] = approved
        taskService.complete(taskId, variables)
    }

    fun getTCSecTasksWorkPlan():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_TC_SEC).list()
        return getTaskDetails(tasks)
    }

    fun uploadWorkPlan(standardWorkPlan: StandardWorkPlan, taskId: String?)
    {
        val variable:MutableMap<String, Any> = HashMap()
        standardWorkPlan.title?.let{variable.put("title", it)}


        standardWorkPlanRepository.save(standardWorkPlan)
        taskService.complete(taskId)
        println("TC-SEC has uploaded workplan")

    }

    fun getProducts(): MutableList<Product>
    {
        return productRepository.findAll()
    }

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
