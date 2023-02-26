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


        ballot.createdOn = Timestamp(System.currentTimeMillis())

        ballot.ballotDraftBy = loggedInUser.id.toString()
        ballot.createdBy = loggedInUser.id.toString()
        ballot.status = "Created & Submitted For Voting"

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
        ballotvoteRepository.findByUserIdAndBallotIdAndStatus(ballotVote.userId, ballotVote.ballotId, 1)
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

    fun getAllVotesTally(): List<VotesTally> {
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        return ballotvoteRepository.getVotesTally(loggedInUser.id.toString())

    }

    fun getAllVotesOnBallot(ballotID: Long): List<VotesWithBallotId> {
        return ballotvoteRepository.getBallotVotes(ballotID)
    }

    fun getAllVotes(): MutableList<BallotVote> {
        return ballotvoteRepository.findAll()
    }

    fun makeDecisionOnBallotDraft(ballot: Ballot) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val approveBallotDraft =
            ballotRepository.findById(ballot.id).orElseThrow { RuntimeException("No Ballot Draft found") }

        if (ballot.approvalStatus.equals("Approved")) {
            approveBallotDraft.status = "Standard Approved"
            variable["status"] = approveBallotDraft.status!!
            approveBallotDraft.approvalStatus = "Sent To Head Of Publishing"
            variable["approvalStatus"] = approveBallotDraft.approvalStatus!!
            approveBallotDraft.modifiedOn = Timestamp(System.currentTimeMillis())
            variable["modifiedOn"] = approveBallotDraft.modifiedOn!!
            approveBallotDraft.modifiedBy = loggedInUser.id.toString()
            variable["modifiedBy"] = approveBallotDraft.modifiedBy ?: throw ExpectedDataNotFound("No USER ID Found")


        } else if (ballot.approvalStatus.equals("Not Approved")) {
            approveBallotDraft.status = "Standard Not Approved"
            variable["status"] = approveBallotDraft.status!!
            approveBallotDraft.approvalStatus = "Forward To TC For Further Deliberations"
            variable["approvalStatus"] = approveBallotDraft.approvalStatus!!
            approveBallotDraft.modifiedOn = Timestamp(System.currentTimeMillis())
            variable["modifiedOn"] = approveBallotDraft.modifiedOn!!
            approveBallotDraft.modifiedBy = loggedInUser.id.toString()
            variable["modifiedBy"] = approveBallotDraft.modifiedBy ?: throw ExpectedDataNotFound("No USER ID Found")

        }
        ballotRepository.save(approveBallotDraft)

    }


    fun editBallot(ballot: Ballot) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val approveBallotDraft =
            ballotRepository.findById(ballot.id).orElseThrow { RuntimeException("No Ballot Draft found") }
        approveBallotDraft.approvalStatus = "Edited By Hop"
        variable["approvalStatus"] = approveBallotDraft.approvalStatus!!
        approveBallotDraft.modifiedOn = Timestamp(System.currentTimeMillis())
        variable["modifiedOn"] = approveBallotDraft.modifiedOn!!
        approveBallotDraft.modifiedBy = loggedInUser.id.toString()
        variable["modifiedBy"] = approveBallotDraft.modifiedBy ?: throw ExpectedDataNotFound("No USER ID Found")


    }

    fun forwardToSacForApproval(ballot: Ballot) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val approveBallotDraft =
            ballotRepository.findById(ballot.id).orElseThrow { RuntimeException("No Ballot Draft found") }
        approveBallotDraft.approvalStatus = "Forwarded To SAC For Approval"
        variable["approvalStatus"] = approveBallotDraft.approvalStatus!!
        approveBallotDraft.modifiedOn = Timestamp(System.currentTimeMillis())
        variable["modifiedOn"] = approveBallotDraft.modifiedOn!!
        approveBallotDraft.modifiedBy = loggedInUser.id.toString()
        variable["modifiedBy"] = approveBallotDraft.modifiedBy ?: throw ExpectedDataNotFound("No USER ID Found")

    }

    fun decisionOnFDKSTD(ballot: Ballot) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val approveBallotDraft =
            ballotRepository.findById(ballot.id).orElseThrow { RuntimeException("No Ballot Draft found") }

        if (ballot.approvalStatus.equals("Approved")) {
            approveBallotDraft.status = "FDKSTD Approved"
            variable["status"] = approveBallotDraft.status!!
            approveBallotDraft.approvalStatus = "FDKSTD Approved"
            variable["approvalStatus"] = approveBallotDraft.approvalStatus!!
            approveBallotDraft.modifiedOn = Timestamp(System.currentTimeMillis())
            variable["modifiedOn"] = approveBallotDraft.modifiedOn!!
            approveBallotDraft.modifiedBy = loggedInUser.id.toString()
            variable["modifiedBy"] = approveBallotDraft.modifiedBy ?: throw ExpectedDataNotFound("No USER ID Found")


        } else if (ballot.approvalStatus.equals("Not Approved")) {
            approveBallotDraft.status = "FDKSTD Not Approved"
            variable["status"] = approveBallotDraft.status!!
            approveBallotDraft.approvalStatus = "FDKSTD Not Approved"
            variable["approvalStatus"] = approveBallotDraft.approvalStatus!!
            approveBallotDraft.modifiedOn = Timestamp(System.currentTimeMillis())
            variable["modifiedOn"] = approveBallotDraft.modifiedOn!!
            approveBallotDraft.modifiedBy = loggedInUser.id.toString()
            variable["modifiedBy"] = approveBallotDraft.modifiedBy ?: throw ExpectedDataNotFound("No USER ID Found")

        }
        ballotRepository.save(approveBallotDraft)

    }


}
