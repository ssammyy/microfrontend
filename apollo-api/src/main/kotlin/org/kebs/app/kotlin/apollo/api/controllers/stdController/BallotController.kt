package org.kebs.app.kotlin.apollo.api.controllers.stdController


import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.BallotService
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.CommitteeService
import org.kebs.app.kotlin.apollo.common.dto.std.*
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.std.BallotingRepository
import org.kebs.app.kotlin.apollo.store.repo.std.CommitteeCDRepository
import org.kebs.app.kotlin.apollo.store.repo.std.PublicReviewDraftRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile


@CrossOrigin(origins = ["http://localhost:4200"])

@RestController
@RequestMapping("api/v1/migration/ballot")


class BallotController(
    val ballotService: BallotService, val committeeService: CommitteeService,
    val ballotingRepository: BallotingRepository,
    val publicReviewDraftRepository: PublicReviewDraftRepository,
) {

    //********************************************************** deployment endpoints **********************************************************
    @PostMapping("/deploy_ballot")
    fun deployWorkflow() {
        ballotService.deployProcessDefinition()
    }

    //committee draft upload minutes
    @PostMapping("/upload/ballotMinutes")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadBallotMinutes(
        @RequestParam("prdId") prdId: Long,
        @RequestParam("docFile") docFile: List<MultipartFile>,
        @RequestParam("type") type: String,

        model: Model
    ): CommonDaoServices.MessageSuccessFailDTO {

        var docDescription: String;

        val application = publicReviewDraftRepository.findByIdOrNull(prdId)
            ?: throw Exception("APPLICATION DOES NOT EXIST")
        docFile.forEach { u ->
            val upload = DatKebsSdStandardsEntity()
            with(upload) {
                sdDocumentId = application.id
                documentTypeDef = type

            }
            docDescription = "Minutes For Ballot"

            committeeService.uploadSDFileCommittee(
                upload,
                u,
                "MINUTES FOR BALLOT",
                application.id,
                docDescription

            )
        }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.message = "Minutes Uploaded successfully"

        return sm
    }

    //public review draft upload other Draft Documents
    @PostMapping("/upload/ballotDraftDocuments")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadBallotDraftDocuments(
        @RequestParam("prdId") prdId: Long,
        @RequestParam("docFile") docFile: List<MultipartFile>,
        @RequestParam("type") type: String,

        model: Model
    ): CommonDaoServices.MessageSuccessFailDTO {

        var docDescription: String;

        val application = publicReviewDraftRepository.findByIdOrNull(prdId)
            ?: throw Exception("APPLICATION DOES NOT EXIST")

        docFile.forEach { u ->
            val upload = DatKebsSdStandardsEntity()
            with(upload) {
                sdDocumentId = application.id
                documentTypeDef = type

            }
            docDescription = "Draft Documents For Ballot"

            committeeService.uploadSDFileCommittee(
                upload,
                u,
                "DRAFT DOCUMENTS FOR BALLOT",
                application.id,
                docDescription

            )
        }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.message = "Draft Documents Uploaded successfully"


        return sm
    }


    @PostMapping("/prepareBallot")
    fun prepareBallot(@RequestBody ballot: Ballot, @RequestParam("prdId") prdId: Long): ProcessInstanceResponseValue {

        return ballotService.prepareBallot(ballot, prdId)

    }


    //public review draft upload other Public Review Draft Document
    @PostMapping("/upload/ballot")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadBallotDocument(
        @RequestParam("ballotId") ballotId: Long,
        @RequestParam("docFile") docFile: List<MultipartFile>,
        @RequestParam("type") type: String,

        model: Model
    ): CommonDaoServices.MessageSuccessFailDTO {

        var docDescription: String;

        val application = ballotingRepository.findByIdOrNull(ballotId)
            ?: throw Exception("APPLICATION DOES NOT EXIST")

        docFile.forEach { u ->
            val upload = DatKebsSdStandardsEntity()
            with(upload) {
                sdDocumentId = application.id
                documentTypeDef = type

            }
            docDescription = "Ballot Document"

            committeeService.uploadSDFileCommittee(
                upload,
                u,
                "BALLOT DOCUMENT",
                application.id,
                docDescription

            )
        }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.message = "Ballot Draft Document Uploaded successfully"

        return sm
    }


    @PostMapping("/voteBallot")
    fun voteForBallot(@RequestBody ballotVote: BallotVote): ServerResponse {
//        return ServerResponse(
//            HttpStatus.OK,
//            "Vote Submitted",
//            ballotService.voteForBallot(ballotVote)
//        )
        return ballotService.voteForBallot(ballotVote)

    }

    @GetMapping("/getBallots")
    fun getAllBallots(): MutableList<BallotWithUserName> {
        return ballotService.getAllBallotDrafts()
    }

    @GetMapping("/getAllBallotVotes")
    fun getAllBallotVotes(@RequestParam("ballotID") ballotID: Long): List<VotesWithBallotId> {
        return ballotService.getAllVotesOnBallot(ballotID)
    }

    @GetMapping("/getMyBallotVotes")
    fun getMyBallotVotes(): List<VotesWithBallotId>? {
        return ballotService.getUserLoggedInBallots()
    }


    @GetMapping("/getBallot/{id}")
    fun getBallotById(@PathVariable("id") id: Long): ResponseEntity<Ballot?>? {
        return ballotService.getBallotById(id)
    }

    //Retrieves requests made by users on the front end
    @GetMapping("/tc/tasks")
    fun getTasks(): List<TaskDetails> {
        return ballotService.getTCTasks() as List<TaskDetails>
    }

    //Retrieves requests made by users on the front end
    @GetMapping("/tcSec/tasks")
    fun getTCSECTasks(): List<TaskDetails> {
        return ballotService.getTCSecTasks() as List<TaskDetails>
    }

    //Retrieves requests made by users on the front end
    @PostMapping("/close")
    fun clos(@RequestBody responseMessage: ResponseMessage) {
        return ballotService.closeTask(responseMessage.message)
    }

}
