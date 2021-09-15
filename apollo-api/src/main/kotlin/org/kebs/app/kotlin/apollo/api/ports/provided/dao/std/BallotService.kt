package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std

import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.task.api.Task
import org.kebs.app.kotlin.apollo.api.errors.std.ResourceNotFoundException
import org.kebs.app.kotlin.apollo.common.dto.std.ProcessInstanceResponse
import org.kebs.app.kotlin.apollo.common.dto.std.TaskDetails
import org.kebs.app.kotlin.apollo.store.model.std.Ballot
import org.kebs.app.kotlin.apollo.store.model.std.BallotVote
import org.kebs.app.kotlin.apollo.store.repo.std.BallotVoteRepository
import org.kebs.app.kotlin.apollo.store.repo.std.BallotingRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.MutableMap


@Service

class BallotService(
    private val runtimeService: RuntimeService,
    private val taskService: TaskService,
    @Qualifier("processEngine")
    private val processEngine: ProcessEngine,
    private val repositoryService: RepositoryService,
    private val ballotRepository: BallotingRepository,
    private val ballotvoteRepository: BallotVoteRepository
) {
    val PROCESS_DEFINITION_KEY = "Balloting"
    val TASK_CANDIDATE_GROUP_TC_SEC = "TC-Sec"
    val TASK_CANDIDATE_GROUP_TC = "TC"
    val variable: MutableMap<String, Any> = HashMap()

    fun deployProcessDefinition() {
        repositoryService
            .createDeployment()
            .addClasspathResource("processes/std/balloting.bpmn20.xml")
            .deploy()
    }

    fun prepareBallot(ballot: Ballot): ProcessInstanceResponse {
        ballot.prID.let { variable.put("prID", it) }
        ballot.ballotName?.let { variable.put("ballotName", it) }
        ballot.ballotDraftBy?.let { variable.put("ballotDraftBy", it) }
        ballot.ballotPath?.let { variable.put("ballotPath", it) }
//        val date = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
//        println(date)
        ballot.createdOn?.let { variable.put("createdOn", it) }
        ballotRepository.save(ballot)
        sendNotification(ballot.id)
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variable)
        return ProcessInstanceResponse(processInstance.id, processInstance.isEnded)

    }

    private fun sendNotification(id: Long) {
        print("Notification sent To TC members ")

    }

    fun voteForBallot(ballotVote: BallotVote): ProcessInstanceResponse {
        ballotVote.ballotId.let { variable.put("ballotId", it) }
        ballotVote.userId.let { variable.put("userId", it) }
        ballotVote.approvalStatus.let { variable.put("approvalStatus", it) }
        ballotVote.comment?.let { variable.put("comment", it) }
        ballotvoteRepository.save(ballotVote)
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variable)
        return ProcessInstanceResponse(processInstance.id, processInstance.isEnded)
    }

    fun getBallots(): MutableList<Ballot> {
        return ballotRepository.findAll()
    }

    //request task list retrieval
    fun getTCTasks(): List<TaskDetails?>? {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_TC)
            .processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    fun getAllVotes(): MutableList<BallotVote> {
        return ballotvoteRepository.findAll()
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

    fun getBallotById(@PathVariable(value = "id") publicReviewId: Long): ResponseEntity<Ballot?>? {
        val employee: Ballot = ballotRepository.findById(publicReviewId)
            .orElseThrow { ResourceNotFoundException("Vote not found for this id :: $publicReviewId") }
        return ResponseEntity.ok().body<Ballot?>(employee)
    }

    //request task list retrieval
    fun getTCSecTasks(): List<TaskDetails?> {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_TC_SEC)
            .processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    //Main task details function where tasks for different candidate groups can be queried
    private fun getTaskDetails(tasks: List<Task>): List<TaskDetails> {
        val taskDetails: MutableList<TaskDetails> = ArrayList()
        for (task in tasks) {
            val processVariables = taskService.getVariables(task.id)
            taskDetails.add(TaskDetails(task.id, task.name, processVariables))
        }
        return taskDetails
    }
    fun closeTask(taskId: String) {
        taskService.complete(taskId)
    }


}
