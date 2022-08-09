package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std

import com.beust.klaxon.JsonReader
import com.beust.klaxon.Klaxon
import mu.KotlinLogging
import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.dto.std.*
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.std.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.io.StringReader
import java.sql.Timestamp

@Service
class PublicReviewService(
    private val runtimeService: RuntimeService,
    private val committeeCDRepository: CommitteeCDRepository,
    @Qualifier("processEngine") private val processEngine: ProcessEngine,
    private val repositoryService: RepositoryService,
    private val publicReviewDraftRepository: PublicReviewDraftRepository,
    private val publicReviewDraftCommentsRepository: PublicReviewDraftCommentsRepository,
    val commonDaoServices: CommonDaoServices,
    private val commentsRepository: CommentsRepository,
    private val sdDocumentsRepository: StandardsDocumentsRepository,

    ) {
    val PROCESS_DEFINITION_KEY = "publicreview"
    val variable: MutableMap<String, Any> = HashMap()


    fun deployProcessDefinition() {
        repositoryService
            .createDeployment()
            .addClasspathResource("processes/std/public_review.bpmn20.xml")
            .deploy()
    }

    // get all approved CDs
    fun getAllApprovedCd(): MutableList<CdWithUserName> {
        return committeeCDRepository.findApprovedCommitteeDraft()
    }


    fun uploadPRD(
        publicReviewDraft: PublicReviewDraft,
        cdId: Long
    ): ProcessInstanceResponseValue {
        //Save Preliminary Draft
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        publicReviewDraft.prdName?.let { variable.put("prdName", it) }
        publicReviewDraft.ksNumber?.let { variable.put("ksNumber", it) }

        publicReviewDraft.createdOn = Timestamp(System.currentTimeMillis())
        variable["createdOn"] = publicReviewDraft.createdOn!!
        publicReviewDraft.cdID = cdId
        variable["cdID"] = publicReviewDraft.cdID
        publicReviewDraft.prdBy = loggedInUser.id!!
        variable["prdBy"] = publicReviewDraft.prdBy ?: throw ExpectedDataNotFound("No USER ID Found")
        publicReviewDraft.createdBy = loggedInUser.id.toString()
        variable["createdBy"] = publicReviewDraft.createdBy ?: throw ExpectedDataNotFound("No USER ID Found")
        publicReviewDraft.status = "Send Draft To Head Of Trade Affairs"
        variable["status"] = publicReviewDraft.status!!
        publicReviewDraft.versionNumber = 1.toString()
        variable["varField8"] = publicReviewDraft.versionNumber!!

        publicReviewDraftRepository.save(publicReviewDraft)

        publicReviewDraft.id.let { variable.put("id", it) }

        //update documents with PRDId
        try {
            val updateDocuments = sdDocumentsRepository.updateDocsWithPRDid(cdId, publicReviewDraft.id)
            KotlinLogging.logger { }.info("The response is $updateDocuments")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
        }

        //get Committee Draft and update
        val committeeDraft: CommitteeCD = committeeCDRepository.findById(cdId).orElse(null);
        committeeDraft.status = "Public Review Draft Uploaded";
        committeeCDRepository.save(committeeDraft)

//        //update NWI with Pd Status
//        val b: StandardNWI = standardNWIRepository.findById(nwIId).orElse(null);
//        b.status = "Preliminary Draft Uploaded";
//        standardNWIRepository.save(b)

        return ProcessInstanceResponseValue(publicReviewDraft.id, "Complete", true, "publicReviewDraft.id")

    }

    //get all Prd
    fun getAllPrd(): MutableList<PrdWithUserName> {
        return publicReviewDraftRepository.findPublicReviewDraft()
    }

    fun getAllPrdDocuments(publicReviewDraftId: Long): Collection<DatKebsSdStandardsEntity?>? {

        val publicReview = publicReviewDraftRepository.findById(publicReviewDraftId)
            .orElseThrow { RuntimeException("No public review draft found") }


        //we want to know if it's edited or not
        return if (publicReview.originalVersion.equals(null)) {
            sdDocumentsRepository.findStandardDocumentPrdId(publicReviewDraftId)

        } else {
            sdDocumentsRepository.findStandardDocumentPrdId(publicReview.originalVersion!!.toLong())

        }


    }

    //send to organisations
    fun sendToOrganisation(publicReviewDraft: PublicReviewDraft) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val publicReviewDraftToBeUpdated =
            publicReviewDraftRepository.findById(publicReviewDraft.id)
                .orElseThrow { RuntimeException("No public review draft found") }

        publicReviewDraftToBeUpdated.status = "Sent To Head Of Trade Affairs"
        variable["status"] = publicReviewDraftToBeUpdated.status!!
        publicReviewDraftToBeUpdated.modifiedOn = Timestamp(System.currentTimeMillis())
        variable["modifiedOn"] = publicReviewDraftToBeUpdated.modifiedOn!!
        publicReviewDraftToBeUpdated.modifiedBy = loggedInUser.id.toString()
        variable["modifiedBy"] =
            publicReviewDraftToBeUpdated.modifiedBy ?: throw ExpectedDataNotFound("No USER ID Found")

        publicReviewDraftRepository.save(publicReviewDraftToBeUpdated)

    }

    fun sendToDepartments(department: Department, publicReviewId: Long) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val publicReviewDraftToBeUpdated =
            publicReviewDraftRepository.findById(publicReviewId).orElseThrow { RuntimeException("No comment found") }
        publicReviewDraftToBeUpdated.status = "Sent To Departments"
        variable["status"] = publicReviewDraftToBeUpdated.status!!
        publicReviewDraftToBeUpdated.modifiedOn = Timestamp(System.currentTimeMillis())
        variable["modifiedOn"] = publicReviewDraftToBeUpdated.modifiedOn!!
        publicReviewDraftToBeUpdated.modifiedBy = loggedInUser.id.toString()
        variable["modifiedBy"] =
            publicReviewDraftToBeUpdated.modifiedBy ?: throw ExpectedDataNotFound("No USER ID Found")
        var allOrganization = ""
        val klaxon = Klaxon()
        JsonReader(StringReader(department.department.toString())).use { reader ->
            reader.beginArray {
                while (reader.hasNext()) {
                    val departmentToBeInserted = klaxon.parse<Department>(reader)
                    allOrganization += departmentToBeInserted?.id.toString() + ","
                }
            }
        }
        publicReviewDraftToBeUpdated.varField1 = allOrganization
        println(allOrganization)

        variable["varField1"] = publicReviewDraftToBeUpdated.varField1!!


        publicReviewDraftRepository.save(publicReviewDraftToBeUpdated)

    }

    fun postToWebsite(publicReviewDraft: PublicReviewDraft) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val publicReviewDraftToBeUpdated =
            publicReviewDraftRepository.findById(publicReviewDraft.id)
                .orElseThrow { RuntimeException("No PRD found") }

        publicReviewDraftToBeUpdated.status = "Posted On Website For Comments"
        variable["status"] = publicReviewDraftToBeUpdated.status!!
        publicReviewDraftToBeUpdated.varField2 = "Posted On Website For Comments"
        variable["varField1"] = publicReviewDraftToBeUpdated.varField2!!
        publicReviewDraftToBeUpdated.modifiedOn = Timestamp(System.currentTimeMillis())
        variable["modifiedOn"] = publicReviewDraftToBeUpdated.modifiedOn!!
        publicReviewDraftToBeUpdated.modifiedBy = loggedInUser.id.toString()
        variable["modifiedBy"] =
            publicReviewDraftToBeUpdated.modifiedBy ?: throw ExpectedDataNotFound("No USER ID Found")

        publicReviewDraftRepository.save(publicReviewDraftToBeUpdated)

    }

    //comments will be made by other users who have logged in: uses CommitteeService

    //make comments by users who do not have accounts
    fun makeCommentUnLoggedInUsers(comments: Comments) {
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
        comments.prdId.let { it?.let { it1 -> variable.put("prdId", it1) } }
        comments.createdBy?.let { variable.put("userEmail", it) }
        comments.varField5?.let { variable.put("names", it) }
        comments.varField6?.let { variable.put("phoneNumber", it) }
        comments.status = 1.toString()
        variable["status"] = comments.status!!

        commentsRepository.save(comments)

    }


    //get all comments on Prd

    //get all user Comments on Pd based on PdId
    fun getAllCommentsOnPrd(publicReviewDraftId: Long): List<CommentsWithPrdId> {
        return commentsRepository.findByPrdId(publicReviewDraftId)
    }


    //get comments made by logged in user on Prd
    fun getUserLoggedInCommentsOnCD(): List<CommentsWithPrdId> {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        return loggedInUser.id?.let {
            commentsRepository.getUserLoggedInCommentsOnPublicReviewDraft(
                it
            )
        }!!
    }



    fun uploadEditedDraft(
        publicReviewDraft: PublicReviewDraft,
        previousPublicReviewDraftId: Long
    ): ProcessInstanceResponseValue {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val editedCommitteeDraft =
            publicReviewDraftRepository.findById(previousPublicReviewDraftId)
                .orElseThrow { RuntimeException("No Public Review Draft found") }

        publicReviewDraft.prdName?.let { variable.put("prdName", it) }
        publicReviewDraft.ksNumber?.let { variable.put("ksNumber", it) }

        publicReviewDraft.createdOn = Timestamp(System.currentTimeMillis())
        variable["createdOn"] = publicReviewDraft.createdOn!!
        publicReviewDraft.cdID = editedCommitteeDraft.cdID
        variable["cdID"] = publicReviewDraft.cdID
        publicReviewDraft.prdBy = loggedInUser.id!!
        variable["prdBy"] = publicReviewDraft.prdBy ?: throw ExpectedDataNotFound("No USER ID Found")
        publicReviewDraft.createdBy = loggedInUser.id.toString()
        variable["createdBy"] = publicReviewDraft.createdBy ?: throw ExpectedDataNotFound("No USER ID Found")
        publicReviewDraft.status = "Send Draft To Head Of Trade Affairs"
        variable["status"] = publicReviewDraft.status!!
        publicReviewDraftRepository.save(publicReviewDraft)
        publicReviewDraft.versionNumber = (editedCommitteeDraft.versionNumber?.toInt()?.plus(1)).toString()
        publicReviewDraft.previousVersion = editedCommitteeDraft.id.toString()

        if (editedCommitteeDraft.originalVersion.equals(null)) {
            publicReviewDraft.originalVersion = editedCommitteeDraft.id.toString()

        } else {
            //get original version
            publicReviewDraft.originalVersion = editedCommitteeDraft.originalVersion
        }


        publicReviewDraftRepository.save(publicReviewDraft)

        editedCommitteeDraft.status = "Edited"
        variable["status"] = editedCommitteeDraft.status!!
        publicReviewDraftRepository.save(editedCommitteeDraft)
        return ProcessInstanceResponseValue(publicReviewDraft.id, "Complete", true, "editedPublicReviewDraft.id")


    }

    fun approvePrd(publicReviewDraft: PublicReviewDraft) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val approveCommitteeDraft =
            publicReviewDraftRepository.findById(publicReviewDraft.id)
                .orElseThrow { RuntimeException("No Public Review Draft found") }

        approveCommitteeDraft.status = "Approved For Balloting"
        variable["status"] = approveCommitteeDraft.status!!
        approveCommitteeDraft.modifiedOn = Timestamp(System.currentTimeMillis())
        variable["modifiedOn"] = approveCommitteeDraft.modifiedOn!!
        approveCommitteeDraft.modifiedBy = loggedInUser.id.toString()
        variable["modifiedBy"] = approveCommitteeDraft.modifiedBy ?: throw ExpectedDataNotFound("No USER ID Found")
        approveCommitteeDraft.varField3 = "Approved"
        variable["varField3"] = approveCommitteeDraft.varField3!!
        publicReviewDraftRepository.save(approveCommitteeDraft)


    }

    //get all Prd Approved
    fun getAllPrdApproved(): MutableList<PrdWithUserName> {
        return publicReviewDraftRepository.findApprovedPublicReviewDraft()
    }



}
