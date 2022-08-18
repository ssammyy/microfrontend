package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std

import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.task.api.Task
import org.kebs.app.kotlin.apollo.api.errors.std.ResourceNotFoundException
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.dto.std.ProcessInstanceResponseValue
import org.kebs.app.kotlin.apollo.common.dto.std.ServerResponse
import org.kebs.app.kotlin.apollo.common.dto.std.TaskDetails
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.InvalidValueException
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.std.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable
import java.sql.Timestamp
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
    private val ballotvoteRepository: BallotVoteRepository,
    private val publicReviewDraftRepository: PublicReviewDraftRepository,
    private val publicReviewDraftCommentsRepository: PublicReviewDraftCommentsRepository,
    val commonDaoServices: CommonDaoServices,
    private val commentsRepository: CommentsRepository,
    private val sdDocumentsRepository: StandardsDocumentsRepository
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

    fun prepareBallot(
        ballot: Ballot, prdId: Long
    ): ProcessInstanceResponseValue {

        val loggedInUser = commonDaoServices.loggedInUserDetails()
        ballot.ballotName?.let { variable.put("ballotName", it) }
        ballot.prdID = prdId
        variable["prdID"] = ballot.prdID


        ballot.createdOn = Timestamp(System.currentTimeMillis())
        variable["createdOn"] = ballot.createdOn!!

        ballot.ballotDraftBy = loggedInUser.id.toString()
        variable["ballotDraftBy"] = ballot.ballotDraftBy ?: throw ExpectedDataNotFound("No USER ID Found")
        ballot.createdBy = loggedInUser.id.toString()
        variable["createdBy"] = ballot.createdBy ?: throw ExpectedDataNotFound("No USER ID Found")
        ballot.status = "Created & Submitted For Voting"
        variable["status"] = ballot.status!!

        ballotRepository.save(ballot)

        //get prd Draft and update
        val publicReviewDraft: PublicReviewDraft = publicReviewDraftRepository.findById(prdId).orElse(null);
        publicReviewDraft.status = "Ballot Draft Uploaded";
        publicReviewDraftRepository.save(publicReviewDraft)

        return ProcessInstanceResponseValue(ballot.id, "Complete", true, "ballot.id")

    }

    //get all Ballot With Votes
    fun getAllBallotDrafts(): MutableList<BallotWithUserName> {
        return ballotRepository.findBallotDraft()
    }


    fun voteForBallot(ballotVote: BallotVote): ServerResponse {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        //Ballot Approval Status
        //1 - Approved
        //2 - Approved With Comments
        //3 - Disapprove With Comments
        //4 - Abstention
        ballotVote.approvalStatus.let { variable.put("approvalStatus", it) }
        ballotVote.comment?.let { variable.put("comment", it) }
        ballotVote.ballotId.let { variable.put("ballotId", it) }
        ballotVote.userId = loggedInUser.id!!
        variable["userId"] = ballotVote.userId


//        //check if person has voted
        ballotvoteRepository.findByUserIdAndAndBallotIdAndStatus(ballotVote.userId, ballotVote.ballotId, 1)
            ?.let {
                // throw InvalidValueException("You Have Already Voted")
                return ServerResponse(
                    HttpStatus.OK,
                    "Voted", "You Have Already Voted"
                )

            }
            ?: run {
                ballotVote.createdOn = Timestamp(System.currentTimeMillis())
                variable["createdOn"] = ballotVote.createdOn!!
                ballotVote.status = 1
                variable["status"] = ballotVote.status!!
                ballotvoteRepository.save(ballotVote)
                return ServerResponse(
                    HttpStatus.OK,
                    "Voted", "You Have Voted"
                )

            }
    }

    fun getUserLoggedInBallots(): List<VotesWithBallotId>? {
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        return loggedInUser.id?.let { ballotvoteRepository.getUserLoggedInVotes(it) }
    }

    fun getAllVotesOnBallot(ballotID: Long): List<VotesWithBallotId> {
        return ballotvoteRepository.getBallotVotes(ballotID)
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
