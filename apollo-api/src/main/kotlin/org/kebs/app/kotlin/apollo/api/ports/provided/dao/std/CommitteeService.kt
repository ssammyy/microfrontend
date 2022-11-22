package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std


import mu.KotlinLogging
import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.dto.std.ProcessInstanceResponseValue
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.IUserRepository
import org.kebs.app.kotlin.apollo.store.repo.std.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.sql.Timestamp

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


    ) {
    val PROCESS_DEFINITION_KEY = "committee_stage"
//    val variable: MutableMap<String, Any> = HashMap()


    fun deployProcessDefinition() {
        repositoryService
            .createDeployment()
            .addClasspathResource("processes/std/committee_stage.bpmn20.xml")
            .deploy()
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
    fun getAllPd(): MutableList<PdWithUserName> {
        return committeePDRepository.findPreliminaryDraft()
    }

    //get all Docs On PDs
    fun getAllPdDocuments(preliminaryDraftId: Long): Collection<DatKebsSdStandardsEntity?>? {
        return sdDocumentsRepository.findStandardDocumentPdId(preliminaryDraftId)
    }

    // make a comment on Preliminary Draft
    fun makeComment(comments: Comments, docType: String) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        comments.createdOn = Timestamp(System.currentTimeMillis())
        comments.createdBy = loggedInUser.id.toString()
        comments.userId = loggedInUser.id!!
        comments.status = 1.toString()

        commentsRepository.save(comments)

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
        val b: CommitteePD = committeePDRepository.findById(pdID).orElse(null);
        b.status = "Committee Draft Uploaded";
        committeePDRepository.save(b)

        return ProcessInstanceResponseValue(committeeCD.id, "Complete", true, "committeePD.id")

    }


    // get all CDs
    fun getAllCd(): MutableList<CdWithUserName> {
        return committeeCDRepository.findCommitteeDraft()
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

        approveCommitteeDraft.ksNumber = "KS${approveCommitteeDraft.id}${
            generateRandomText(
                5,
                "SHA1PRNG",
                "SHA-512",
                true
            )
        }".toUpperCase()

        //send email to hof sic
        val messageBody =
            " Hello a Committee Draft has been created with the KS Number: " +
                    "${approveCommitteeDraft.ksNumber}. Please login to the protal to view it.  \n " +

                    "\n\n\n\n\n\n"
        notifications.sendEmail("marvoceo@gmail.com", "Committee Draft Creation", messageBody)

        committeeCDRepository.save(approveCommitteeDraft)

    }
    // get all work items that have been approved and have a workPlan attached

    fun getApprovedNwis(): List<ApprovedNwi> {
        return standardWorkPlanRepository.findAllApproved()
    }


    fun getAllNwiSApprovedForPd(): List<StandardNWI> {
        return standardNWIRepository.findAllByPdStatus("Prepare Minutes and Drafts For Preliminary Draft")
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
            u.pdStatus = "Minutes Uploaded";
            standardNWIRepository.save(u)
        }
        if (DocDescription == "Draft Documents For PD") {
            //update documents with PDId
            val u: StandardNWI = standardNWIRepository.findById(nwi).orElse(null);
            u.pdStatus = "Draft Documents For PD Uploaded";
            standardNWIRepository.save(u)
        }
        if (DocDescription == "Minutes For CD") {
            //update documents with CDId
            val u: CommitteePD = committeePDRepository.findById(nwi).orElse(null);
            u.status = "Minutes Uploaded";
            committeePDRepository.save(u)
        }
        if (DocDescription == "Draft Documents For CD") {
            //update documents with CDId
            val u: CommitteePD = committeePDRepository.findById(nwi).orElse(null);
            u.status = "Draft Documents For CD Uploaded";
            committeePDRepository.save(u)
        }
        if (DocDescription == "Minutes For PRD") {
            //update documents with PRDId
            val u: CommitteeCD = committeeCDRepository.findById(nwi).orElse(null);
            u.status = "Minutes For PRD Uploaded";
            committeeCDRepository.save(u)
        }
        if (DocDescription == "Draft Documents For PRD") {
            //update documents with PRDId
            val u: CommitteeCD = committeeCDRepository.findById(nwi).orElse(null);
            u.status = "Draft Documents For PRD Uploaded";
            committeeCDRepository.save(u)
        }
        if (DocDescription == "Minutes For Ballot") {
            //update documents with PRDId
            val u: PublicReviewDraft = publicReviewDraftRepository.findById(nwi).orElse(null);
            u.status = "Minutes For Ballot Uploaded";
            publicReviewDraftRepository.save(u)
        }
        if (DocDescription == "Draft Documents For Ballot") {
            //update documents with PRDId
            val u: PublicReviewDraft = publicReviewDraftRepository.findById(nwi).orElse(null);
            u.status = "Draft Documents For Ballot Uploaded";
            publicReviewDraftRepository.save(u)
        }
        return sdDocumentsRepository.save(uploads)
    }

}
