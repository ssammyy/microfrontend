package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std

import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.engine.repository.Deployment
import org.flowable.task.api.Task
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.dto.std.Decision
import org.kebs.app.kotlin.apollo.common.dto.std.ProcessInstanceResponseValue
import org.kebs.app.kotlin.apollo.common.dto.std.TaskDetails
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.std.BallotingRepository
import org.kebs.app.kotlin.apollo.store.repo.std.DecisionFeedbackRepository
import org.kebs.app.kotlin.apollo.store.repo.std.StandardDraftRepository
import org.kebs.app.kotlin.apollo.store.repo.std.StandardsDocumentsRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.sql.Timestamp


@Service
class PublishingService(
    private val runtimeService: RuntimeService,
    private val taskService: TaskService,
    @Qualifier("processEngine") private val processEngine: ProcessEngine,
    private val repositoryService: RepositoryService,
    private val standardDraftRepository: StandardDraftRepository,
    private val decisionFeedbackRepository: DecisionFeedbackRepository,
    val commonDaoServices: CommonDaoServices,
    private val sdNwaUploadsEntityRepository: StandardsDocumentsRepository,
    private val ballotRepository: BallotingRepository,


    ) {
    val PROCESS_DEFINITION_KEY = "sd_publishing"
    val TASK_HEAD_OF_PUBLISHING = "head_of_publishing"
    val TASK_EDITOR = "editor"
    val TASK_PROOFREADER = "proofreader"
    val TASK_DRAUGHTSMAN = "draughtsman"

    fun deployProcessDefinition(): Deployment = repositoryService
        .createDeployment()
        .addClasspathResource("processes/std/publishing_module.bpmn20.xml")
        .deploy()

    fun getAllBallotDraftsForPublishing(): List<Ballot> {
        return ballotRepository.findByApprovalStatusAndVarField2IsNull("Sent To Head Of Publishing")
    }

    fun getAllBallotDraftsForPublishingApprovedForEditing(): List<Ballot> {
        return ballotRepository.findByVarField2("Approved For Editing")
    }

    fun getAllBallotDraftsForPublishingRejectedForEditing(): List<Ballot> {
        return ballotRepository.findByVarField2("Rejected For Editing")
    }


    fun submitDraftStandard(ballot: Ballot, decision: String): ProcessInstanceResponseValue {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val approveBallotDraft =
            ballotRepository.findById(ballot.id).orElseThrow { RuntimeException("No Ballot Draft found") }
        val standardDraft = StandardDraft();

        if (decision == "True") {
            approveBallotDraft.varField2 = "Approved For Editing"
            approveBallotDraft.varField1 = ballot.varField1 //reason provided for decision

            //create a new standard from ballot: These will be saved in a new table so  all the details from previous batch will be present
            val user = ballot.createdBy?.let { commonDaoServices.findUserByID(it.toLong()) }
            standardDraft.standardOfficerId = ballot.createdBy
            standardDraft.title = ballot.ballotName
            standardDraft.versionNumber = 1
            standardDraft.standardOfficerName = user?.firstName + " " + user?.lastName
            standardDraft.draftId = ballot.id
            standardDraft.requestorId = loggedInUser.id.toString()
            standardDraft.submission_date = Timestamp(System.currentTimeMillis())
            standardDraft.approvalStatus = "Approved For Editing"
            standardDraft.editedStatus = "Not Edited"
            standardDraft.proofreadStatus = "Not ProofRead"
            standardDraft.draughtingStatus = "Not Draughted"
            standardDraftRepository.save(standardDraft)
        }
        if (decision == "False") {
            approveBallotDraft.varField2 = "Rejected For Editing"
            approveBallotDraft.varField1 = ballot.varField1

        }
        ballotRepository.save(approveBallotDraft)


        return ProcessInstanceResponseValue(
            1, " processInstance.id", true, "1"
        )
    }

    fun getHOPTasks(): List<TaskDetails> {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_HEAD_OF_PUBLISHING).list()
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

    fun decisionOnKSDraft(decision: Decision, draftStandardID: Long) {
        updateDraftDecision(decision.taskId, draftStandardID)
    }

    private fun updateDraftDecision(taskId: String, draftStandardID: Long) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val u: StandardDraft = standardDraftRepository.findById(draftStandardID).orElse(null);
        u.approvedBy = loggedInUser.firstName + " " + loggedInUser.lastName
        u.approvalStatus = "Approved For Editing"
        u.status = "Approved For Editing";
        standardDraftRepository.save(u)

    }

    fun uploadFeedbackOnDraft(decisionFeedback: DecisionFeedback) {
        decisionFeedbackRepository.save(decisionFeedback)
        taskService.complete(decisionFeedback.taskId)
    }

    fun getEditorTasks(): List<StandardDraft> {
        return standardDraftRepository.getEditorDrafts()

    }

    fun editDraftStandard(standardDraft: StandardDraft, draftStandardID: Long) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val editedStandardDraft: StandardDraft = standardDraftRepository.findById(draftStandardID).orElse(null);
        editedStandardDraft.editedBY = loggedInUser.firstName + " " + loggedInUser.lastName
        editedStandardDraft.editedStatus = "Edited"
        editedStandardDraft.editedDate = Timestamp(System.currentTimeMillis())
        editedStandardDraft.approvalStatus = "Awaiting Decision On Draughting"
        standardDraftRepository.save(editedStandardDraft)
    }

    fun getEditorTasksForDraughting(): List<StandardDraft> {
        return standardDraftRepository.findByApprovalStatusAndDraftIdIsNotNull("Awaiting Decision On Draughting")
    }

    fun approvedForProofReading(standardDraft: StandardDraft, decision: String) {

        val u: StandardDraft = standardDraftRepository.findById(standardDraft.id).orElse(null);
        u.approvalStatus = "Approved For Proofreading"
        standardDraftRepository.save(u)
    }

    fun approvedForDraughting(standardDraft: StandardDraft, decision: String) {

        val u: StandardDraft = standardDraftRepository.findById(standardDraft.id).orElse(null);
        u.approvalStatus = "Approved For Draughting"
        standardDraftRepository.save(u)
    }


    fun decisionOnProofReading(decision: Decision, draftStandardID: Long) {

        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val u: StandardDraft = standardDraftRepository.findById(draftStandardID).orElse(null);
        u.proofReadBy = loggedInUser.firstName + " " + loggedInUser.lastName
        u.proofreadStatus = "ProofRead"
        u.approvalStatus = "ProofRead Done"
        u.proofReadDate = Timestamp(System.currentTimeMillis())
        standardDraftRepository.save(u)
    }

    fun getProofreaderTasks(): List<StandardDraft> {
        return standardDraftRepository.findByApprovalStatusAndDraftIdIsNotNull("Approved For Proofreading")

    }

    fun getDraughtsmanTasks(): List<StandardDraft> {
        return standardDraftRepository.findByApprovalStatusAndDraftIdIsNotNull("Approved For Draughting")

    }

    fun uploadDraftStandardDraughtChanges(standardDraft: StandardDraft, decision: String) {

        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val draughtedStandardDraft: StandardDraft = standardDraftRepository.findById(standardDraft.id).orElse(null);
        draughtedStandardDraft.draughtingBy = loggedInUser.firstName + " " + loggedInUser.lastName
        draughtedStandardDraft.draughtingDate = Timestamp(System.currentTimeMillis())
        if (decision == "True") {
            draughtedStandardDraft.draughtingStatus = "Draughted"
        } else if (decision == "False") {
            draughtedStandardDraft.draughtingStatus = "Draughting Not Needed"
        }
        draughtedStandardDraft.approvalStatus = "Draughting Done"

        standardDraftRepository.save(draughtedStandardDraft)
//        taskService.complete(standardDraft.taskId)

    }

    fun approveDraughtChange(standardDraft: StandardDraft) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        standardDraft.approvedBy = loggedInUser.id.toString();
        standardDraft.approvalStatus = "Draughting Approved"
        standardDraft.status = "1"
        standardDraftRepository.save(standardDraft)
//        taskService.complete(standardDraft.taskId)
    }

    fun sendToOfficer(standardDraft: StandardDraft, decision: String) {

        val u: StandardDraft = standardDraftRepository.findById(standardDraft.id).orElse(null);
        u.status = "Sent To Standard Officer"
        standardDraftRepository.save(u)
        val approveBallotDraft = ballotRepository.findById(standardDraft.draftId).orElseThrow { RuntimeException("No Ballot Draft found") }
        approveBallotDraft?.editedStatus = "Edited"
        ballotRepository.save(approveBallotDraft)
    }


    fun uploadSDFile(
        uploads: DatKebsSdStandardsEntity,
        docFile: MultipartFile,
        doc: String,
        user: UsersEntity,
        DocDescription: String
    ): DatKebsSdStandardsEntity {

        with(uploads) {
//            filepath = docFile.path
            name = commonDaoServices.saveDocuments(docFile)
//            fileType = docFile.contentType
            fileType = docFile.contentType
            documentType = doc
            description = DocDescription
            document = docFile.bytes
            transactionDate = commonDaoServices.getCurrentDate()
            status = 1
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }

        return sdNwaUploadsEntityRepository.save(uploads)
    }



    //Approval Process

    //Get all required for Approval After Editing
    fun getAllEditedBallotDraftsForApproval(): List<Ballot> {
        return ballotRepository.findByEditedStatus("Edited")
    }

    //Decision On Draft


}
