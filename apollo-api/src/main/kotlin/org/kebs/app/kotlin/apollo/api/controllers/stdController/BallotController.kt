package org.kebs.app.kotlin.apollo.api.controllers.stdController


import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.BallotService
import org.kebs.app.kotlin.apollo.common.dto.std.ProcessInstanceResponse
import org.kebs.app.kotlin.apollo.common.dto.std.ResponseMessage
import org.kebs.app.kotlin.apollo.common.dto.std.TaskDetails
import org.kebs.app.kotlin.apollo.store.model.std.Ballot
import org.kebs.app.kotlin.apollo.store.model.std.BallotVote
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@CrossOrigin(origins = ["http://localhost:4200"])

@RestController
@RequestMapping("api/v1/ballot")

class BallotController(val ballotService: BallotService) {

    //********************************************************** deployment endpoints **********************************************************
    @PostMapping("/deploy_ballot")
    fun deployWorkflow() {
        ballotService.deployProcessDefinition()
    }

    @PostMapping("/prepareballot")
    fun preparePublicReview(@RequestBody ballot: Ballot): ProcessInstanceResponse {
        return ballotService.prepareBallot(ballot)
    }


    @PostMapping("/voteBallot")
    fun makeCommentOnDraft(@RequestBody ballotVote: BallotVote): ProcessInstanceResponse {
        return ballotService.voteForBallot(ballotVote)
    }

    @GetMapping("/getBallots")
    fun getPrs(): MutableList<Ballot> {
        return ballotService.getBallots()
    }

    @GetMapping("/getAllVotes")
    fun getPrComments(): MutableList<BallotVote> {
        return ballotService.getAllVotes()
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
