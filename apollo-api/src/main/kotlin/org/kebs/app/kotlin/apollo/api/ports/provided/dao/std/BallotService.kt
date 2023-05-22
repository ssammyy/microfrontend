package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std

import org.flowable.engine.RepositoryService
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.dto.std.ProcessInstanceResponseValue
import org.kebs.app.kotlin.apollo.common.dto.std.ServerResponse
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.std.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.sql.Timestamp


@Service

class BallotService(
    private val repositoryService: RepositoryService,
    private val ballotRepository: BallotingRepository,
    private val ballotvoteRepository: BallotVoteRepository,
    private val publicReviewDraftRepository: PublicReviewDraftRepository,
    val commonDaoServices: CommonDaoServices,
    private val standardRequestRepository: StandardRequestRepository,
    private val committeeCDRepository: CommitteeCDRepository,
    private val committeePDRepository: CommitteePDRepository,
    private val standardNWIRepository: StandardNWIRepository,
    private val userListRepository: UserListRepository,
    private val comStdDraftRepository: ComStdDraftRepository,
    private val isAdoptionProposalRepository: ISAdoptionProposalRepository,


    ) {
    val PROCESS_DEFINITION_KEY = "Balloting"
    val variable: MutableMap<String, Any> = HashMap()

    fun deployProcessDefinition() {
        repositoryService
            .createDeployment()
            .addClasspathResource("processes/std/balloting.bpmn20.xml")
            .deploy()
    }

    fun prepareBallot(
        ballot: Ballot
    ): ProcessInstanceResponseValue {

        val loggedInUser = commonDaoServices.loggedInUserDetails()

        ballot.createdOn = Timestamp(System.currentTimeMillis())

        ballot.ballotDraftBy = loggedInUser.id.toString()
        ballot.createdBy = loggedInUser.id.toString()
        ballot.status = "Created & Submitted For Voting"

        ballotRepository.save(ballot)

        //get prd Draft and update
        val publicReviewDraft: PublicReviewDraft = publicReviewDraftRepository.findById(ballot.prdID).orElse(null);
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
                ballotVote.status = 1
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
            println(approveBallotDraft.prdID)
            val publicReviewDraft: PublicReviewDraft = publicReviewDraftRepository.findById(approveBallotDraft.prdID).orElse(null);
            val committeeDraft: CommitteeCD = committeeCDRepository.findById(publicReviewDraft.cdID).orElse(null);
            val preliminaryDraft: CommitteePD = committeePDRepository.findById(committeeDraft.pdID).orElse(null)
            val nwiItem: StandardNWI =
                standardNWIRepository.findById(preliminaryDraft.nwiID?.toLong() ?: -1).orElse(null)
            val standardRequest: StandardRequest =
                standardRequestRepository.findById(nwiItem.standardId ?: -1).orElse(null)
            val uploadedDate= Timestamp(System.currentTimeMillis())
            val deadline: Timestamp = Timestamp.valueOf(uploadedDate.toLocalDateTime().plusDays(7))
            val tcSecId= standardRequest.tcSecAssigned?.toLong()


            approveBallotDraft.status = "Standard Approved"
            approveBallotDraft.approvalStatus = "Sent To Head Of Publishing"
            approveBallotDraft.modifiedOn = Timestamp(System.currentTimeMillis())
            approveBallotDraft.modifiedBy = loggedInUser.id.toString()

            comStdDraftRepository.findByIdOrNull(publicReviewDraft.stdDraftId)?.let { comStdDraft ->
                with(comStdDraft) {
                    standardType="FD KS"
                    status=4

                }
                comStdDraftRepository.save(comStdDraft)
            } ?: throw Exception("DRAFT NOT FOUND")


        } else if (ballot.approvalStatus.equals("Not Approved")) {
            approveBallotDraft.status = "Standard Not Approved"
            approveBallotDraft.approvalStatus = "Forward To TC For Further Deliberations"
            approveBallotDraft.modifiedOn = Timestamp(System.currentTimeMillis())
            approveBallotDraft.modifiedBy = loggedInUser.id.toString()

        }
        ballotRepository.save(approveBallotDraft)

    }


    fun editBallot(ballot: Ballot) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val approveBallotDraft =
            ballotRepository.findById(ballot.id).orElseThrow { RuntimeException("No Ballot Draft found") }
        approveBallotDraft.approvalStatus = "Edited By Hop"
        approveBallotDraft.modifiedOn = Timestamp(System.currentTimeMillis())
        approveBallotDraft.modifiedBy = loggedInUser.id.toString()


    }

    fun forwardToSacForApproval(ballot: Ballot) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val approveBallotDraft =
            ballotRepository.findById(ballot.id).orElseThrow { RuntimeException("No Ballot Draft found") }
        approveBallotDraft.approvalStatus = "Forwarded To SAC For Approval"
        approveBallotDraft.modifiedOn = Timestamp(System.currentTimeMillis())
        approveBallotDraft.modifiedBy = loggedInUser.id.toString()

    }

    fun decisionOnFDKSTD(ballot: Ballot) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val approveBallotDraft =
            ballotRepository.findById(ballot.id).orElseThrow { RuntimeException("No Ballot Draft found") }


        if (ballot.approvalStatus.equals("Approved")) {
            approveBallotDraft.status = "FDKSTD Approved"
            approveBallotDraft.approvalStatus = "FDKSTD Approved"
            approveBallotDraft.modifiedOn = Timestamp(System.currentTimeMillis())
            approveBallotDraft.modifiedBy = loggedInUser.id.toString()


        } else if (ballot.approvalStatus.equals("Not Approved")) {
            approveBallotDraft.status = "FDKSTD Not Approved"
            approveBallotDraft.approvalStatus = "FDKSTD Not Approved"
            approveBallotDraft.modifiedOn = Timestamp(System.currentTimeMillis())
            approveBallotDraft.modifiedBy = loggedInUser.id.toString()

        }
        ballotRepository.save(approveBallotDraft)

    }


}
