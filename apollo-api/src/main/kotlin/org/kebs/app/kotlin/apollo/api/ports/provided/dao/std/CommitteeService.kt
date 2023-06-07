package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std


import mu.KotlinLogging
import org.flowable.engine.RepositoryService
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.dto.std.ProcessInstanceResponseValue
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.IUserRepository
import org.kebs.app.kotlin.apollo.store.repo.std.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*


@Service
class CommitteeService(
    private val repositoryService: RepositoryService,
    private val committeePDRepository: CommitteePDRepository,
    private val committeeCDRepository: CommitteeCDRepository,
    private val standardWorkPlanRepository: StandardWorkPlanRepository,
    private val standardNWIRepository: StandardNWIRepository,
    private val commentsRepository: CommentsRepository,
    private val usersRepo: IUserRepository,
    val commonDaoServices: CommonDaoServices,
    private val notifications: Notifications,
    private val sdDocumentsRepository: StandardsDocumentsRepository,
    private val publicReviewDraftRepository: PublicReviewDraftRepository,
    private val standardRequestRepository: StandardRequestRepository,


    ) {
    val PROCESS_DEFINITION_KEY = "committee_stage"
//    val variable: MutableMap<String, Any> = HashMap()


    fun deployProcessDefinition() {
        repositoryService
            .createDeployment()
            .addClasspathResource("processes/std/committee_stage.bpmn20.xml")
            .deploy()
    }


    fun getAllNwiSApprovedForPd(): List<StandardNWI> {
        val loggedInUser = commonDaoServices.loggedInUserDetails()


        return standardNWIRepository.findAllByPdStatusAndTcSec(
            "Prepare Minutes and Drafts For Preliminary Draft",
            loggedInUser.id.toString()
        )
    }

    fun getAllNwiSApprovedForPdPendingTasks(): List<StandardNWI> {
        val loggedInUser = commonDaoServices.loggedInUserDetails()


        return standardNWIRepository.findAllByPdStatusAndTcSecAndPrPdStatusIsNull(
            "Prepare Minutes and Drafts For Preliminary Draft",
            loggedInUser.id.toString()
        )
    }


    //upload Preliminary Draft
    fun uploadPD(
        committeePD: CommitteePD,
        nwIId: Long
    ): ProcessInstanceResponseValue {
        //Save Preliminary Draft
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        committeePD.createdOn = Timestamp(System.currentTimeMillis())
        committeePD.nwiID = nwIId.toString()
        committeePD.pdBy = loggedInUser.id.toString()
        committeePD.status = "Commenting By TC"
        committeePDRepository.save(committeePD)

        //update NWI with Pd Status
        val b: StandardNWI = standardNWIRepository.findById(nwIId).orElse(null)
//        b.pdStatus = "Preliminary Draft Uploaded";
        b.prPdStatus = "Preliminary Draft Uploaded"
        standardNWIRepository.save(b)

        val standardRequestToUpdate = b.standardId?.let {
            standardRequestRepository.findById(it)
                .orElseThrow { RuntimeException("No Standard Request found") }
        }
        if (standardRequestToUpdate != null) {
            standardRequestToUpdate.ongoingStatus = "Preliminary Draft Uploaded"
            standardRequestRepository.save(standardRequestToUpdate)

        }

        return ProcessInstanceResponseValue(committeePD.id, "Complete", true, "committeePD.id")

    }


    //get all Preliminary Drafts
    fun getAllPd(): MutableList<PdWithUserName> {
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        return committeePDRepository.findPreliminaryDraftUnAssigned()
    }

    //get all Docs On PDs
    fun getAllPdDocuments(preliminaryDraftId: Long): Collection<DatKebsSdStandardsEntity?>? {
        return sdDocumentsRepository.findStandardDocumentPdId(preliminaryDraftId)
    }

    // make a comment on Preliminary Draft
    fun makeComment(preliminaryDraftId: Long, docType: String, body: List<CommentsDto>) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()


        body.forEach { bodyDetails ->
            addComment(
                bodyDetails,
                preliminaryDraftId,
                docType,
                loggedInUser
            )

        }

    }


    private fun addComment(
        bodyDetails: CommentsDto,
        preliminaryDraftId: Long,
        docType: String,
        loggedInUser: UsersEntity
    ): Comments {
        var comment = Comments()
        var preliminaryDraft: CommitteePD? = null
        var nwi: StandardNWI? = null

        when (docType) {
            "PD" -> {
                preliminaryDraft = committeePDRepository.findByIdOrNull(preliminaryDraftId)
                preliminaryDraft?.let {
                    nwi = standardNWIRepository.findByIdOrNull(it.nwiID?.toLong() ?: -1L)
                }
            }
            "CD" -> {
                val committeeDraft = committeeCDRepository.findByIdOrNull(preliminaryDraftId)
                committeeDraft?.let {
                    preliminaryDraft = committeePDRepository.findByIdOrNull(committeeDraft.pdID)
                    preliminaryDraft?.let {
                        nwi = standardNWIRepository.findByIdOrNull(it.nwiID?.toLong() ?: -1L)
                    }
                }
            }
            "PRD" -> {
                val publicReviewDraft = publicReviewDraftRepository.findByIdOrNull(preliminaryDraftId)
                val committeeDraft = committeeCDRepository.findByIdOrNull(publicReviewDraft?.cdID ?: -1L)
                preliminaryDraft = committeePDRepository.findByIdOrNull(committeeDraft?.pdID ?: -1L)
                nwi = standardNWIRepository.findByIdOrNull(preliminaryDraft?.nwiID?.toLong() ?: -1L)


            }
        }

        try {
            val existingComment = commentsRepository.findByIdOrNull(bodyDetails.id ?: -1L)
            if (existingComment != null) {
                comment = saveComment(
                    bodyDetails,
                    preliminaryDraftId,
                    docType,
                    loggedInUser,
                    existingComment,
                    true,
                    nwi!!
                )
            } else {
                comment = saveComment(
                    bodyDetails,
                    preliminaryDraftId,
                    docType,
                    loggedInUser,
                    comment,
                    false,
                    nwi!!
                )
            }

            comment = commentsRepository.save(comment)
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return comment
    }


    fun saveComment(
        details: CommentsDto,
        preliminaryDraftIdAssigned: Long,
        docType: String,
        loggedInUser: UsersEntity,
        comments: Comments,
        update: Boolean,
        nwi: StandardNWI
    ): Comments {

        with(comments)
        {
            clause = details.clause
            paragraph = details.paragraph
            commentType = details.typeOfComment
            commentsMade = details.comment
            proposedChange = details.proposedChange
            when (docType) {
                "PD" -> {
                    pdId = preliminaryDraftIdAssigned
                }
                "CD" -> {
                    cdId = preliminaryDraftIdAssigned
                }
                "PRD" -> {
                    prdId = preliminaryDraftIdAssigned.toString()
                }
            }
            documentType = docType
            userId = loggedInUser.id!!
            status = 1.toString()
            title = nwi.proposalTitle
            circulationDate = nwi.circulationDate?.let { convertToTimestamp(it) }
            closingDate = nwi.closingDate?.let { convertToTimestamp(it) }
            organization = nwi.organization

            when {
                update -> {
                    modifiedBy = loggedInUser.id!!.toString()
                    modifiedOn = commonDaoServices.getTimestamp()
                }

                else -> {
                    createdBy = loggedInUser.id!!.toString()
                    createdOn = commonDaoServices.getTimestamp()
                }
            }
            return comments


        }

    }

    //get all user Comments on Pd based on PdId
    fun getAllCommentsOnPd(preliminaryDraftId: Long): List<CommentsWithPdId> {
        return commentsRepository.findByPdId(preliminaryDraftId)
    }

    //get all comments and with PDName on PdId
    fun getAllCommentsOnPdWithPdName(): List<CommentsWithPdId> {
        return commentsRepository.getAllCommentsOnPreliminaryDraft()
    }

    //get comments made by logged in user on Pd
    fun getUserLoggedInCommentsOnPD(): List<CommentsWithPdId> {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        return loggedInUser.id?.let { commentsRepository.getUserLoggedInCommentsOnPreliminaryDraft(it) }!!

    }

    //edit comment
    fun editComment(comments: Comments) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val commentToEdit =
            commentsRepository.findById(comments.id).orElseThrow { RuntimeException("No comment found") }

        commentToEdit.title = comments.title
        commentToEdit.documentType = comments.documentType
        commentToEdit.circulationDate = comments.circulationDate
        commentToEdit.closingDate = comments.closingDate
        commentToEdit.recipientId = comments.recipientId
        commentToEdit.organization = comments.organization
        commentToEdit.clause = comments.clause
        commentToEdit.paragraph = comments.paragraph
        commentToEdit.commentType = comments.commentType
        commentToEdit.proposedChange = comments.proposedChange
        commentToEdit.observation = comments.observation
        commentToEdit.commentsMade = comments.commentsMade
        commentToEdit.modifiedOn = Timestamp(System.currentTimeMillis())
        commentToEdit.modifiedBy = loggedInUser.id.toString()

        commentsRepository.save(commentToEdit)

    }


    //delete comment
    fun deleteComment(comments: Comments) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val commentToDelete =
            commentsRepository.findById(comments.id).orElseThrow { RuntimeException("No comment found") }

        commentToDelete.status = 0.toString()
        commentToDelete.deletedOn = Timestamp(System.currentTimeMillis())
        commentToDelete.deleteBy = loggedInUser.id.toString()

        commentsRepository.save(commentToDelete)

    }


    //get all pds pending cds

    fun getAllPdPendingCds(): MutableList<PdWithUserName> {

        return committeePDRepository.findPreliminaryDraftPendingCds()
    }


    //upload Committee Draft
    fun uploadCD(
        committeeCD: CommitteeCD,
        pdID: Long
    ): ProcessInstanceResponseValue {
        //Save Committee Draft
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        committeeCD.createdOn = Timestamp(System.currentTimeMillis())
        committeeCD.pdID = pdID
        committeeCD.cdBy = loggedInUser.id!!
        committeeCD.createdBy = loggedInUser.id.toString()
        committeeCD.status = "Commenting By TC. Awaiting Approval"
        committeeCD.approved = "Not Approved"
        committeeCDRepository.save(committeeCD)


        //update documents with CDId
        try {
            val updateDocuments = sdDocumentsRepository.updateDocsWithCDid(committeeCD.id, pdID)
            KotlinLogging.logger { }.info("The response is $updateDocuments")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
        }


        //update PD with CD Status
        val b: CommitteePD = committeePDRepository.findById(pdID).orElse(null)
        b.status = "Committee Draft Uploaded"
        committeePDRepository.save(b)



        val c: StandardNWI = standardNWIRepository.findById(b.nwiID?.toLong() ?: -1).orElse(null)
        val standardRequestToUpdate = c.standardId?.let {
            standardRequestRepository.findById(it)
                .orElseThrow { RuntimeException("No Standard Request found") }
        }
        if (standardRequestToUpdate != null) {
            standardRequestToUpdate.ongoingStatus = "Committee Draft Uploaded"
            standardRequestRepository.save(standardRequestToUpdate)

        }

        return ProcessInstanceResponseValue(committeeCD.id, "Complete", true, "committeePD.id")

    }


    // get all CDs
    fun getAllCd(): MutableList<CdWithUserName> {
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        return committeeCDRepository.findCommitteeDraft(loggedInUser.id.toString())
    }


    //get comments made by logged in user on Cd
    fun getUserLoggedInCommentsOnCD(): List<CommentsWithCdId> {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        return loggedInUser.id?.let {
            commentsRepository.getUserLoggedInCommentsOnCommitteeDraft(
                it
            )
        }!!
    }

    fun getAllCdDocuments(committeeDraftId: Long): Collection<DatKebsSdStandardsEntity?>? {
        return sdDocumentsRepository.findStandardDocumentCdId(committeeDraftId)
    }

    //get all user Comments on Pd based on PdId
    fun getAllCommentsOnCd(committeeDraftId: Long): List<CommentsWithCdId> {
        return commentsRepository.findByCdId(committeeDraftId)
    }


    //Approve Committee Draft and assign KsNumber
    fun approveCd(committeeCD: CommitteeCD) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val approveCommitteeDraft =
            committeeCDRepository.findById(committeeCD.id).orElseThrow { RuntimeException("No Committee Draft found") }

        approveCommitteeDraft.approved = "Approved"
        approveCommitteeDraft.status = "Approved. Prepare Public Review Draft."
        approveCommitteeDraft.modifiedOn = Timestamp(System.currentTimeMillis())
        approveCommitteeDraft.modifiedBy = loggedInUser.id.toString()
        approveCommitteeDraft.ksNumber = "DKS${approveCommitteeDraft.id}${Calendar.getInstance().get(Calendar.YEAR)}".uppercase(Locale.getDefault())

        //send email to hof sic
        val messageBody =
            " Hello a Committee Draft has been created with the KS Number: " +
                    "${approveCommitteeDraft.ksNumber}. Please login to the protal to view it.  \n " +

                    "\n\n\n\n\n\n"
        notifications.sendEmail("2nduatsd@gmail.com", "Committee Draft Creation", messageBody)

        committeeCDRepository.save(approveCommitteeDraft)

        //update PD with CD Status
        val b: CommitteePD = committeePDRepository.findById(approveCommitteeDraft.pdID).orElse(null)
        val c: StandardNWI = standardNWIRepository.findById(b.nwiID?.toLong() ?: -1).orElse(null)
        val standardRequestToUpdate = c.standardId?.let {
            standardRequestRepository.findById(it)
                .orElseThrow { RuntimeException("No Standard Request found") }
        }
        if (standardRequestToUpdate != null) {
            standardRequestToUpdate.ongoingStatus = "Committee Draft Approved. Prepare Public Review Draft"
            standardRequestRepository.save(standardRequestToUpdate)

        }

    }
    // get all work items that have been approved and have a workPlan attached

    fun getApprovedNwis(): List<ApprovedNwi> {
        return standardWorkPlanRepository.findAllApproved()
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
            val pd: CommitteePD = committeePDRepository.findById(nwi).orElse(null)

            val u: StandardNWI = standardNWIRepository.findById(pd.nwiID?.toLong() ?: -1L).orElse(null)
            u.minutesPdStatus = "Minutes Uploaded"
            standardNWIRepository.save(u)
        }
        if (DocDescription == "Draft Documents For PD") {
            //update documents with PDId
            val pd: CommitteePD = committeePDRepository.findById(nwi).orElse(null)

            val u: StandardNWI = standardNWIRepository.findById(pd.nwiID?.toLong() ?: -1L).orElse(null)

            u.draftDocsPdStatus = "Draft Documents For PD Uploaded"
            standardNWIRepository.save(u)
        }
//        if (DocDescription == "Minutes For CD") {
//            //update documents with CDId
//            val u: CommitteeCD = committeeCDRepository.findById(nwi).orElse(null)
//            u.status = "Minutes Uploaded"
//            committeeCDRepository.save(u)
//        }
//        if (DocDescription == "Draft Documents For CD") {
//            //update documents with CDId
//            val u: CommitteePD = committeePDRepository.findById(nwi).orElse(null)
//            u.status = "Draft Documents For CD Uploaded"
//            committeePDRepository.save(u)
//        }
        if (DocDescription == "Minutes For PRD") {
            //update documents with PRDId
            val u: CommitteeCD = committeeCDRepository.findById(nwi).orElse(null)
            u.status = "Minutes For PRD Uploaded"
            committeeCDRepository.save(u)
        }
        if (DocDescription == "Draft Documents For PRD") {
            //update documents with PRDId
            val u: CommitteeCD = committeeCDRepository.findById(nwi).orElse(null)
            u.status = "Draft Documents For PRD Uploaded"
            committeeCDRepository.save(u)
        }
        return sdDocumentsRepository.save(uploads)
    }

    fun getAllSdUsers(
    ): List<UsersEntity> {
        usersRepo.findSdUser()
            ?.let {
                return it
            } ?: throw ExpectedDataNotFound("NO USER LIST FOUND")
    }


//    fun mapAllRequestProcesses(
//        nwIId: Long
//    ): AllRequestDetailsApplyDto {
//
//
//
//    }


    fun convertToTimestamp(myStringDate: String): Timestamp {

        val dateFormat = SimpleDateFormat("yyyy-mm-dd")
        val parsedDate: Date = dateFormat.parse(myStringDate)
        return Timestamp(parsedDate.time)


    }

}
