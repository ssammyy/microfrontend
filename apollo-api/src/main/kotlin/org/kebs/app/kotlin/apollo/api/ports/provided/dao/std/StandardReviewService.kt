package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std


import org.kebs.app.kotlin.apollo.common.dto.std.*
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.std.*
import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.engine.repository.Deployment
import org.flowable.task.api.Task
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.util.ArrayList


@Service
class StandardReviewService(
    private val runtimeService: RuntimeService,
    private val taskService: TaskService,
    @Qualifier("processEngine") private val processEngine: ProcessEngine,
    private val repositoryService: RepositoryService,
    @Autowired
    private val standardRepository: StandardRepository,
    private val standardReviewRepository: StandardReviewRepository,
    private val standardReviewCommentsRepository: StandardReviewCommentsRepository,
    private val standardReviewRecommendationsRepository: StandardReviewRecommendationsRepository
) {

    val PROCESS_DEFINITION_KEY = "sd_Review_Procedure_module"
    val TASK_CANDIDATE_GROUP_TC_SEC ="TC_SEC"
    val TASK_CANDIDATE_GROUP_STAKEHOLDERS ="STAKEHOLDERS"
    val TASK_CANDIDATE_GROUP_SPC_SEC ="SPC_SEC"
    val TASK_CANDIDATE_GROUP_SAC_SEC="SAC_SEC"
    val TASK_CANDIDATE_GROUP_HOP="HOP"

    fun deployProcessDefinition(): Deployment =repositoryService
        .createDeployment()
        .addClasspathResource("processes/std/Review_Procedure_Process_Flow.bpmn20.xml")
        .deploy()

    //start the process by process Key
    fun startProcessByKey() : ProcessInstanceResponse
    {

        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY)
        return ProcessInstanceResponse(processInstance.id, processInstance.isEnded)
    }

    //*** Not used *** but closes any Task, linked to task close endpoint
    fun closeTask(taskId: String) {
        taskService.complete(taskId)
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



    fun reviewedStandards(): MutableList<Standard>
    {
        return standardRepository.findAll()
    }

    fun saveStandard(standard: Standard){
        standardRepository.save(standard)

    }


    fun standardReviewForm(standardReview: StandardReview) : ProcessInstanceResponse
    {
        val variables: MutableMap<String, Any> = HashMap()
        standardReview.title?.let{ variables.put("title", it)}
        standardReview.standardNumber?.let{ variables.put("standardNumber", it)}
        standardReview.documentType?.let{ variables.put("documentType", it)}
        standardReview.preparedBy?.let{ variables.put("preparedBy", it)}
        standardReview.datePrepared?.let{ variables.put("datePrepared", it)}


        standardReviewRepository.save(standardReview)
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables)
        return ProcessInstanceResponse(processInstance.id, processInstance.isEnded)
    }


    //Return task details for Stakeholders
    fun getReviewForms():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_STAKEHOLDERS).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    fun commentsOnReview(standardReviewComments: StandardReviewComments)
    {
        val variables: MutableMap<String, Any> = HashMap()
        standardReviewComments.comments?.let{ variables.put("comments", it)}
        standardReviewComments.commentBy?.let{ variables.put("commentBy", it)}
        standardReviewComments.dateOfComment?.let{ variables.put("dateOfComment", it)}

        print(standardReviewComments.toString())

        standardReviewCommentsRepository.save(standardReviewComments)
        taskService.complete(standardReviewComments.taskId, variables)
        println("Comments Submitted")
    }

    //Return task details for TSC_SEC
    fun getReviewTasks():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_TC_SEC).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    fun reviewRecommendations(standardReviewRecommendations: StandardReviewRecommendations)
    {
        val variables: MutableMap<String, Any> = HashMap()
        standardReviewRecommendations.recommendation?.let{ variables.put("recommendation", it)}
        standardReviewRecommendations.recommendationBy?.let{ variables.put("recommendationBy", it)}
        standardReviewRecommendations.dateOfRecommendation?.let{ variables.put("dateOfRecommendation", it)}
        standardReviewRecommendations.accentTo?.let{ variables.put("accentTo", it)}

        print(standardReviewRecommendations.toString())

        standardReviewRecommendationsRepository.save(standardReviewRecommendations)
        taskService.complete(standardReviewRecommendations.taskId, variables)
        println("Recommendations Submitted")
    }

    //Return task details for SPC_SEC
    fun getRecommendations():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_SPC_SEC).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    // Decision on Recommendation
    fun decisionOnRecommendation(standardReviewRecommendations: StandardReviewRecommendations)
    {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        variables["Yes"] = standardReviewRecommendations.accentTo
        taskService.complete(standardReviewRecommendations.taskId, variables)
    }

    //Return task details for SAC_SEC
    fun getSacList():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_SAC_SEC).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    // Decision on Recommendation
    fun decisionOfSac(standardReviewRecommendations: StandardReviewRecommendations)
    {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        variables["Yes"] = standardReviewRecommendations.accentTo
        taskService.complete(standardReviewRecommendations.taskId, variables)
    }

    //Return task details for HOP
    fun getPublishingTasks():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_HOP).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    fun getStandardsForReview(): MutableList<ReviewStandards> {
        return standardRepository.getStandardsForReview()
    }


}
