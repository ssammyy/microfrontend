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
    val variable: MutableMap<String, Any> = HashMap()


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
        committeePD.pdName?.let { variable.put("pdName", it) }
        committeePD.createdOn = Timestamp(System.currentTimeMillis())
        variable["createdOn"] = committeePD.createdOn!!
        committeePD.nwiID = nwIId.toString()
        variable["nwiID"] = committeePD.nwiID ?: throw ExpectedDataNotFound("No NWI ID  Found")
        committeePD.pdBy = loggedInUser.id.toString()
        variable["pdBy"] = committeePD.pdBy ?: throw ExpectedDataNotFound("No USER ID Found")
        committeePD.status = "Commenting By TC"
        variable["status"] = committeePD.status!!
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

        if (docType == "PD") {
            comments.pdId.let { variable.put("pdId", it) }
        } else if (docType == "CD") {
            comments.cdId.let { variable.put("cdId", it) }

        } else if (docType == "PRD") {
            comments.prdId.let { it?.let { it1 -> variable.put("prdId", it1) } }

        }

        comments.createdBy = loggedInUser.id.toString()
        variable["createdBy"] = comments.createdBy ?: throw ExpectedDataNotFound("No USER ID Found")
        comments.userId = loggedInUser.id!!
        variable["userId"] = comments.userId ?: throw ExpectedDataNotFound("No USER ID Found")
        comments.status = 1.toString()
        variable["status"] = comments.status!!

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
        variable["title"] = commentToEdit.title!!
        commentToEdit.documentType = comments.documentType
        variable["documentType"] = commentToEdit.documentType!!
        commentToEdit.circulationDate = comments.circulationDate
        variable["circulationDate"] = commentToEdit.circulationDate!!
        commentToEdit.closingDate = comments.closingDate
        variable["closingDate"] = commentToEdit.closingDate!!
        commentToEdit.recipientId = comments.recipientId
        variable["recipientId"] = commentToEdit.recipientId
        commentToEdit.organization = comments.organization
        variable["organization"] = commentToEdit.organization!!
        commentToEdit.clause = comments.clause
        variable["clause"] = commentToEdit.clause!!
        commentToEdit.paragraph = comments.paragraph
        variable["paragraph"] = commentToEdit.paragraph!!
        commentToEdit.commentType = comments.commentType
        variable["commentType"] = commentToEdit.commentType!!
        commentToEdit.proposedChange = comments.proposedChange
        variable["proposedChange"] = commentToEdit.proposedChange!!
        commentToEdit.observation = comments.observation
        variable["observation"] = commentToEdit.observation!!
        commentToEdit.commentsMade = comments.commentsMade
        variable["commentsMade"] = commentToEdit.commentsMade!!
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

    //upload Committee Draft
    fun uploadCD(
        committeeCD: CommitteeCD,
        pdID: Long
    ): ProcessInstanceResponseValue {
        //Save Committee Draft
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        committeeCD.cdName?.let { variable.put("cdName", it) }
        committeeCD.createdOn = Timestamp(System.currentTimeMillis())
        variable["createdOn"] = committeeCD.createdOn!!
        committeeCD.pdID = pdID
        variable["pdID"] = committeeCD.pdID ?: throw ExpectedDataNotFound("No pdID ID  Found")
        committeeCD.cdBy = loggedInUser.id!!
        variable["cdBy"] = committeeCD.cdBy ?: throw ExpectedDataNotFound("No USER ID Found")
        committeeCD.createdBy = loggedInUser.id.toString()
        variable["createdBy"] = committeeCD.createdBy ?: throw ExpectedDataNotFound("No USER ID Found")
        committeeCD.status = "Commenting By TC. Awaiting Approval"
        variable["status"] = committeeCD.status!!
        committeeCD.approved = "Not Approved"
        variable["approved"] = committeeCD.approved!!
        committeeCDRepository.save(committeeCD)

        committeeCD.id.let { variable.put("id", it) }

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
        variable["approved"] = approveCommitteeDraft.approved!!
        approveCommitteeDraft.status = "Approved. Prepare Public Review Draft."
        variable["status"] = approveCommitteeDraft.status!!
        approveCommitteeDraft.modifiedOn = Timestamp(System.currentTimeMillis())
        variable["modifiedOn"] = approveCommitteeDraft.modifiedOn!!
        approveCommitteeDraft.modifiedBy = loggedInUser.id.toString()
        variable["modifiedBy"] = approveCommitteeDraft.modifiedBy ?: throw ExpectedDataNotFound("No USER ID Found")

        approveCommitteeDraft.ksNumber = "KS${approveCommitteeDraft.id}${
            generateRandomText(
                5,
                "SHA1PRNG",
                "SHA-512",
                true
            )
        }".toUpperCase()
        variable["ksNumber"] = approveCommitteeDraft.ksNumber!!

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
