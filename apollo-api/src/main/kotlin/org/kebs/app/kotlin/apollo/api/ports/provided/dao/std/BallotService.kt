package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std

import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.dto.std.ProcessInstanceResponseValue
import org.kebs.app.kotlin.apollo.common.dto.std.ServerResponse
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.std.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.sql.Timestamp


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
    private val standardRequestRepository: StandardRequestRepository,
    private val committeeCDRepository: CommitteeCDRepository,
    private val committeePDRepository: CommitteePDRepository,
    private val standardNWIRepository: StandardNWIRepository,
    private val userListRepository: UserListRepository,
    private val comStdDraftRepository: ComStdDraftRepository,
    private val isAdoptionProposalRepository: ISAdoptionProposalRepository,


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
            val proposal=ISAdoptionProposal()
            val closingDate=Timestamp.valueOf(nwiItem.closingDate+"00:00:00")
            val circulationDate=Timestamp.valueOf(nwiItem.circulationDate+"00:00:00")
            proposal.proposal_doc_name="Public Review Draft"
            proposal.circulationDate=circulationDate
            proposal.closingDate=closingDate
            proposal.tcSecName=userListRepository.findNameById(tcSecId)
            proposal.tcSecEmail=userListRepository.findEmailById(tcSecId)
            proposal.preparedDate=uploadedDate
            proposal.title=nwiItem.proposalTitle
            proposal.scope=nwiItem.scope
            proposal.requestId=standardRequest.id
            proposal.iStandardNumber=publicReviewDraft.ksNumber
            proposal.tcSecAssigned=standardRequest.tcSecAssigned

            val prop=isAdoptionProposalRepository.save(proposal)

            val comDraft = ComStdDraft()
            comDraft.title=nwiItem.proposalTitle
            comDraft.scope=nwiItem.scope
            comDraft.normativeReference=nwiItem.referenceNumber
            comDraft.uploadDate=uploadedDate
            comDraft.deadlineDate=Timestamp.valueOf(nwiItem.targetDate+"00:00:00")
            comDraft.uploadedBy=loggedInUser.id
            comDraft.createdBy=userListRepository.findNameById(loggedInUser.id)
            comDraft.requestNumber=standardRequest.requestNumber
            comDraft.requestId=standardRequest.id
            comDraft.status=4
            comDraft.comStdNumber=publicReviewDraft.ksNumber
            comDraft.departmentId= standardRequest.departmentId?.toLong()
            comDraft.departmentName=standardRequest.departmentName
            comDraft.subject=standardRequest.subject
            comDraft.description=standardRequest.description
            comDraft.standardType="Public Review Draft"
            comDraft.proposalId=prop.id

            val draftId=comStdDraftRepository.save(comDraft)


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
