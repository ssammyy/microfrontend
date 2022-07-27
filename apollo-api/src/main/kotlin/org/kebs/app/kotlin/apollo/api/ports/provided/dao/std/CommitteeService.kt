package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std


import mu.KotlinLogging
import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.task.api.Task
import org.kebs.app.kotlin.apollo.api.errors.std.ResourceNotFoundException
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.dto.std.ProcessInstanceResponse
import org.kebs.app.kotlin.apollo.common.dto.std.ProcessInstanceResponseValue
import org.kebs.app.kotlin.apollo.common.dto.std.TaskDetails
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.WorkplanEntity
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.std.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.multipart.MultipartFile
import java.sql.Timestamp

@Service
class CommitteeService(
    private val runtimeService: RuntimeService,
    private val taskService: TaskService,
    @Qualifier("processEngine") private val processEngine: ProcessEngine,
    private val repositoryService: RepositoryService,
    private val committeeNWIRepository: CommitteeNWIRepository,
    private val committeeDraftsRepository: CommitteeDraftsRepository,
    private val committeePDRepository: CommitteePDRepository,
    private val committeePdDraftsRepository: CommitteePdDraftsRepository,
    private val committeeCDRepository: CommitteeCDRepository,
    private val standardWorkPlanRepository: StandardWorkPlanRepository,
    private val standardNWIRepository: StandardNWIRepository,
    private val commentsRepository: CommentsRepository,

    val commonDaoServices: CommonDaoServices,
    private val sdDocumentsRepository: StandardsDocumentsRepository,


    ) {
    val PROCESS_DEFINITION_KEY = "committee_stage"
    val TASK_CANDIDATE_GROUP_TC_SEC = "TC-sec"
    val TASK_CANDIDATE_GROUP_TC = "TC"
    val TASK_CANDIDATE_GROUP_HOF_SIC = "HOF-SIC"
    val variable: MutableMap<String, Any> = HashMap()


    fun deployProcessDefinition() {
        repositoryService
            .createDeployment()
            .addClasspathResource("processes/std/committee_stage.bpmn20.xml")
            .deploy()
    }

    //get all NWIs
    fun getAllNWIs(): MutableList<StandardNWI> {
        return standardNWIRepository.findAll()
    }

    fun getWorkPlan(referenceNumber: String): MutableList<StandardWorkPlan> {
        return standardWorkPlanRepository.findByReferenceNo(referenceNumber)
    }


    //upload Preliminary Draft
    fun uploadPD(
        committeePD: CommitteePD,
        nwIId: Long
    ): ProcessInstanceResponseValue {
        //Save Preliminary Draft
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        committeePD.pdName?.let { variable.put("pdName", it) }
        committeePD.createdOn = Timestamp(System.currentTimeMillis())
        variable["createdOn"] = committeePD.createdOn!!
        committeePD.nwiID = nwIId.toString()
        variable["nwiID"] = committeePD.nwiID ?: throw ExpectedDataNotFound("No NWI ID  Found")
        committeePD.pdBy = loggedInUser.id.toString()
        variable["pdBy"] = committeePD.pdBy ?: throw ExpectedDataNotFound("No USER ID Found")
        committeePDRepository.save(committeePD)

        committeePD.id.let { variable.put("id", it) }

        //update documents with PDId
        try {
            val updateDocuments = sdDocumentsRepository.updateDocsWithPDid(committeePD.id, nwIId)
            KotlinLogging.logger { }.info("The response is $updateDocuments")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
        }


        //update NWI with Pd Status
        val b: StandardNWI = standardNWIRepository.findById(nwIId).orElse(null);
        b.status = "Preliminary Draft Uploaded";
        standardNWIRepository.save(b)

        return ProcessInstanceResponseValue(committeePD.id, "Complete", true, "committeePD.id")

    }


    //get all Preliminary Drafts
    fun getAllPd(): MutableList<CommitteePD> {
        return committeePDRepository.findAll()
    }

    // make a comment on Preliminary Draft
    fun makeComment(comments: Comments) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        comments.title?.let { variable.put("title", it) }
        comments.documentType?.let { variable.put("documentType", it) }
        comments.circulationDate?.let { variable.put("circulationDate", it) }
        comments.closingDate?.let { variable.put("closingDate", it) }
        comments.recipientId.let { variable.put("recipientId", it) }
        comments.organization?.let { variable.put("organization", it) }
        comments.clause?.let { variable.put("clause", it) }
        comments.paragraph?.let { variable.put("paragraph", it) }
        comments.commentType?.let { variable.put("commentType", it) }
        comments.commentsMade?.let { variable.put("commentsMade", it) }
        comments.proposedChange?.let { variable.put("proposedChange", it) }
        comments.observation?.let { variable.put("observation", it) }
        comments.createdOn = Timestamp(System.currentTimeMillis())
        variable["createdOn"] = comments.createdOn!!
        comments.pdId.let { variable.put("pdId", it) }
        comments.createdBy = loggedInUser.id.toString()
        variable["createdBy"] = comments.createdBy ?: throw ExpectedDataNotFound("No USER ID Found")
        comments.status = 1.toString()
        variable["status"] = comments.status!!

        commentsRepository.save(comments)

    }

    //get all user Comments on Pd
    fun getAllCommentsOnPd(): List<Comments> {
        return commentsRepository.findAll()
    }

    //get comments made by logged in user on Pd
    fun getUserLoggedInCommentsOnPD(@PathVariable(value = "pdId") preliminaryDraftId: Long): List<Comments> {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        return loggedInUser.id?.let {
            commentsRepository.findByUserIdAndPdIdAndStatus(
                it, preliminaryDraftId,
                1.toString()
            )
        }!!
    }

    //edit comment
    fun editComment(comments: Comments) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val commentToEdit =
            commentsRepository.findById(comments.id).orElseThrow { RuntimeException("No comment found") }

        commentToEdit.title?.let { variable.put("title", it) }
        commentToEdit.documentType?.let { variable.put("documentType", it) }
        commentToEdit.circulationDate?.let { variable.put("circulationDate", it) }
        commentToEdit.closingDate?.let { variable.put("closingDate", it) }
        commentToEdit.recipientId.let { variable.put("recipientId", it) }
        commentToEdit.organization?.let { variable.put("organization", it) }
        commentToEdit.clause?.let { variable.put("clause", it) }
        commentToEdit.paragraph?.let { variable.put("paragraph", it) }
        commentToEdit.commentType?.let { variable.put("commentType", it) }
        commentToEdit.commentsMade?.let { variable.put("commentsMade", it) }
        commentToEdit.proposedChange?.let { variable.put("proposedChange", it) }
        commentToEdit.observation?.let { variable.put("observation", it) }
        commentToEdit.modifiedOn = Timestamp(System.currentTimeMillis())
        variable["modifiedOn"] = commentToEdit.modifiedOn!!
        commentToEdit.modifiedBy = loggedInUser.id.toString()
        variable["modifiedBy"] = commentToEdit.modifiedBy ?: throw ExpectedDataNotFound("No USER ID Found")

        commentsRepository.save(commentToEdit)

    }


    //delete comment
    fun deleteComment(comments: Comments) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val commentToDelete =
            commentsRepository.findById(comments.id).orElseThrow { RuntimeException("No comment found") }

        commentToDelete.status = 0.toString()
        variable["status"] = commentToDelete.status!!
        commentToDelete.deletedOn = Timestamp(System.currentTimeMillis())
        variable["deletedOn"] = commentToDelete.deletedOn!!
        commentToDelete.deleteBy = loggedInUser.id.toString()
        variable["deleteBy"] = commentToDelete.deleteBy ?: throw ExpectedDataNotFound("No USER ID Found")

        commentsRepository.save(commentToDelete)

    }

    fun prepareNWI(committeeNWI: CommitteeNWI): ProcessInstanceResponse {
        committeeNWI.slNo?.let { variable.put("slNo", it) }
        committeeNWI.reference?.let { variable.put("reference", it) }
        committeeNWI.ta?.let { variable.put("ta", it) }
        committeeNWI.ed?.let { variable.put("ed", it) }
        committeeNWI.title?.let { variable.put("title", it) }
        committeeNWI.stage_date?.let { variable.put("stage_date", it) }
        committeeNWI.approved.let { variable.put("approved", it) }

        print(committeeNWI.toString())


        committeeNWIRepository.save(committeeNWI)
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variable)
        return ProcessInstanceResponse(processInstance.id, processInstance.isEnded)

    }

    private fun getTaskDetails(tasks: List<Task>): List<TaskDetails> {
        val taskDetails: MutableList<TaskDetails> = ArrayList()
        for (task in tasks) {
            val processVariables = taskService.getVariables(task.id)
            taskDetails.add(TaskDetails(task.id, task.name, processVariables))
        }
        return taskDetails
    }

    fun getTCSECTasks(): List<TaskDetails> {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_TC_SEC).list()
        return getTaskDetails(tasks)
    }

    fun approveNWI(taskId: String?, approved: Boolean) {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        variables["approved"] = approved
        taskService.complete(taskId, variables)
    }

    fun uploadDrafts(committeeDrafts: CommitteeDrafts, taskId: String?) {
        committeeDrafts.draftName?.let { variable.put("draftName", it) }
        committeeDrafts.draftBy?.let { variable.put("draftBy", it) }
        print(committeeDrafts.toString())

        committeeDraftsRepository.save(committeeDrafts)
        taskService.complete(taskId)
        println("TC-SEC has uploaded draft document")

    }

    // get all work items that have been approved and have a workPlan attached

    fun getApprovedNwis(): List<ApprovedNwi> {
        return standardWorkPlanRepository.findAllApproved()
    }


    fun preparePD(committeePD: CommitteePD): ProcessInstanceResponse {
        committeePD.nwiID?.let { variable.put("nwiID", it) }
        committeePD.pdName?.let { variable.put("pdName", it) }
        committeePD.pdBy?.let { variable.put("pdBy", it) }
        print(committeePD.toString())
        committeePDRepository.save(committeePD)
        println("TC-SEC has prepared Preliminary Draft document")
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variable)
        return ProcessInstanceResponse(processInstance.id, processInstance.isEnded)
    }

    fun uploadDraftsPD(committeeDraftsPD: CommitteeDraftsPD, taskId: String?) {
        committeeDraftsPD.PdDraftName?.let { variable.put("PdDraftName", it) }
        committeeDraftsPD.PddraftBy?.let { variable.put("PddraftBy", it) }
        print(committeeDraftsPD.toString())
        committeePdDraftsRepository.save(committeeDraftsPD)
        taskService.complete(taskId)
        println("TC-SEC has uploaded discussions on Preliminary Draft")


    }

    fun prepareCD(committeeCD: CommitteeCD): ProcessInstanceResponse {
        committeeCD.nwiID?.let { variable.put("nwiID", it) }
        committeeCD.pdID?.let { variable.put("pdID", it) }
        committeeCD.cdName?.let { variable.put("cdName", it) }
        committeeCD.cdBy?.let { variable.put("cdBy", it) }
        print(committeeCD.toString())
        committeeCDRepository.save(committeeCD)
        println("TC-SEC has prepared Preliminary Draft document")
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variable)
        return ProcessInstanceResponse(processInstance.id, processInstance.isEnded)

    }

    //    fun approveCD(committeeCD: CommitteeCD,taskId: String?, approved: Boolean) {
//        val variables: MutableMap<String, Any> = java.util.HashMap()
//        committeeCD.approved?.let { variable.put("approved", it) }
//        variables["approved"] = approved
//        committeeCDRepository.save(committeeCD)
//        taskService.complete(taskId, variables)
//    }
    fun approveCD(taskId: String?, approved: Boolean) {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        variables["approved"] = approved
        taskService.complete(taskId, variables)
    }

    fun getNWIs(): MutableList<CommitteeNWI> {
        return committeeNWIRepository.findAll()
    }

    fun getPds(): MutableList<CommitteePD> {
        return committeePDRepository.findAll()
    }

    fun getCds(): MutableList<CommitteeCD> {
        return committeeCDRepository.findAll()
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
                activity.activityId + " took " + activity.durationInMillis + " milliseconds"
            )
        }

    }

    fun getPreliminaryDraftById(@PathVariable(value = "id") preliminaryDraftId: Long): ResponseEntity<CommitteePD?>? {
        val pd: CommitteePD = committeePDRepository.findById(preliminaryDraftId)
            .orElseThrow { ResourceNotFoundException("PD not found for this id :: $preliminaryDraftId") }
        return ResponseEntity.ok().body<CommitteePD?>(pd)
    }


    fun uploadSDFileCommittee(
        uploads: DatKebsSdStandardsEntity,
        docFile: MultipartFile,
        doc: String,
        nwi: Long,
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
            sdDocumentId = nwi
            createdBy = commonDaoServices.concatenateName(commonDaoServices.loggedInUserDetails())
            createdOn = commonDaoServices.getTimestamp()
        }

        if (DocDescription == "Minutes For PD") {
            //update documents with PDId
            val u: StandardNWI = standardNWIRepository.findById(nwi).orElse(null);
            u.status = "Minutes Uploaded";
            standardNWIRepository.save(u)
        }
        if (DocDescription == "Draft Documents For PD") {
            //update documents with PDId
            val u: StandardNWI = standardNWIRepository.findById(nwi).orElse(null);
            u.status = "Draft Documents For PD Uploaded";
            standardNWIRepository.save(u)
        }
        return sdDocumentsRepository.save(uploads)
    }

}
