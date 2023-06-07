package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std

import com.beust.klaxon.JsonReader
import com.beust.klaxon.Klaxon
import mu.KotlinLogging
import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.dto.std.*
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.IUserRepository
import org.kebs.app.kotlin.apollo.store.repo.std.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.io.StringReader
import java.sql.Timestamp

@Service
class PublicReviewService(
    private val committeePDRepository: CommitteePDRepository,
    private val committeeCDRepository: CommitteeCDRepository,
    private val standardNWIRepository: StandardNWIRepository,
    private val repositoryService: RepositoryService,
    private val publicReviewDraftRepository: PublicReviewDraftRepository,
    private val standardRequestRepository: StandardRequestRepository,
    val commonDaoServices: CommonDaoServices,
    private val commentsRepository: CommentsRepository,
    private val sdDocumentsRepository: StandardsDocumentsRepository,
    private val publicReviewStakeHoldersRepo: PublicReviewStakeHoldersRepository,
    private val usersRepo: IUserRepository,
    private val applicationMapProperties: ApplicationMapProperties,
    private val notifications: Notifications,

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
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        return committeeCDRepository.findApprovedCommitteeDraft(loggedInUser.id.toString())
    }


    fun uploadPRD(
        publicReviewDraft: PublicReviewDraft,
        cdId: Long
    ): ProcessInstanceResponseValue {
        //Save Preliminary Draft
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        publicReviewDraft.createdOn = Timestamp(System.currentTimeMillis())
        publicReviewDraft.cdID = cdId
        publicReviewDraft.prdBy = loggedInUser.id!!
        publicReviewDraft.createdBy = loggedInUser.id.toString()
        publicReviewDraft.status = "Send Draft To Head Of Trade Affairs"
        publicReviewDraft.versionNumber = 1.toString()

        publicReviewDraftRepository.save(publicReviewDraft)

        //update documents with PRDId
//        try {
//            val updateDocuments = sdDocumentsRepository.updateDocsWithPRDid(cdId, publicReviewDraft.id)
//            KotlinLogging.logger { }.info("The response is $updateDocuments")
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.error(e.message)
//        }

        //get Committee Draft and update
        val committeeDraft: CommitteeCD = committeeCDRepository.findById(cdId).orElse(null);
        committeeDraft.status = "Public Review Draft Uploaded";
        committeeCDRepository.save(committeeDraft)


        val b: CommitteePD = committeePDRepository.findById(committeeDraft.pdID).orElse(null)
        val c: StandardNWI = standardNWIRepository.findById(b.nwiID?.toLong() ?: -1).orElse(null)
        val standardRequestToUpdate = c.standardId?.let {
            standardRequestRepository.findById(it)
                .orElseThrow { RuntimeException("No Standard Request found") }
        }
        if (standardRequestToUpdate != null) {
            standardRequestToUpdate.ongoingStatus = "Under Public Review"
            standardRequestRepository.save(standardRequestToUpdate)

        }


//        //update NWI with Pd Status
//        val b: StandardNWI = standardNWIRepository.findById(nwIId).orElse(null);
//        b.status = "Preliminary Draft Uploaded";
//        standardNWIRepository.save(b)

        return ProcessInstanceResponseValue(publicReviewDraft.id, "Complete", true, "publicReviewDraft.id")

    }

    //get all Prd
    fun getAllPrd(): MutableList<PrdWithUserName> {
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        return publicReviewDraftRepository.findPublicReviewDraft(loggedInUser.id.toString())
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
        publicReviewDraftToBeUpdated.modifiedOn = Timestamp(System.currentTimeMillis())
        publicReviewDraftToBeUpdated.modifiedBy = loggedInUser.id.toString()

        publicReviewDraftRepository.save(publicReviewDraftToBeUpdated)

    }
    fun sendPublicReview(publicReviewDto: PublicReviewDto): PublicReviewDraft {
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        var tcName = loggedInUser.firstName + loggedInUser.lastName
        val reviewDraft=PublicReviewDraft()
        val stakeholdersOne = publicReviewDto.stakeholdersList
        stakeholdersOne?.forEach { s ->
            val subject = "Invitation to Provide Comments on Public Review Draft"
            val recipient = s.email
            val user = s.name
            val userId = usersRepo.getUserId(s.email)
            val userPhone = usersRepo.getUserPhone(s.email)

            val shs = PublicReviewStakeHolders()
            shs.name = user
            shs.email = recipient
            shs.prId = publicReviewDto.prId
            shs.dateOfCreation = Timestamp(System.currentTimeMillis())
            shs.telephone = userPhone
            shs.userId = userId
            shs.status = 0
            publicReviewStakeHoldersRepo.save(shs)




        }

        val stakeholdersTwo = publicReviewDto.addStakeholdersList
        stakeholdersTwo?.forEach { t ->
            val sub = "Invitation to Provide Comments on Public Review Draft"
            val rec = t.stakeHolderEmail
            val userN = t.stakeHolderName
            val phoneNumber = t.stakeHolderPhone

            val st = PublicReviewStakeHolders()
            st.name = userN
            st.email = rec
            st.prId = publicReviewDto.prId
            st.dateOfCreation = Timestamp(System.currentTimeMillis())
            st.telephone = phoneNumber
            st.status = 0
            val sid = publicReviewStakeHoldersRepo.save(st)
            val pid = sid.id
            val encryptedId = BCryptPasswordEncoder().encode(pid.toString())

            publicReviewStakeHoldersRepo.findByIdOrNull(pid)?.let { prs ->

                with(prs) {
                    encrypted=encryptedId

                }
                publicReviewStakeHoldersRepo.save(prs)
            } ?: throw Exception("STAKEHOLDERS INFORMATION NOT FOUND")
            val link =
                "${applicationMapProperties.baseUrlQRValue}commentOnPublicReview?reviewID=${encryptedId}"


            val messageBody =
                "Dear $userN,\nThe Kenya Bureau of Standards has prepared a Public Review draft.\n" +
                        "To provide your feedback, please use the following link: [$link]. You will be prompted to provide your comments on the Review Draft.\n" +
                        "Thank you in advance for your participation and contributions.\nBest regards,\n " +
                        "$tcName "

            if (rec != null) {
                notifications.sendEmail(rec, sub, messageBody)
            }
        }


            publicReviewDraftRepository.findByIdOrNull(publicReviewDto.prId)?.let { pr ->

                with(pr) {
                    status = "Posted On Website For Comments"

                }
                publicReviewDraftRepository.save(pr)
            } ?: throw Exception("REVIEW DRAFT NOT FOUND")

        return reviewDraft

    }

    fun sendToDepartments(department: Department, publicReviewId: Long) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val publicReviewDraftToBeUpdated =
            publicReviewDraftRepository.findById(publicReviewId).orElseThrow { RuntimeException("No comment found") }
        publicReviewDraftToBeUpdated.status = "Sent To Departments"
        publicReviewDraftToBeUpdated.modifiedOn = Timestamp(System.currentTimeMillis())
        publicReviewDraftToBeUpdated.modifiedBy = loggedInUser.id.toString()

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



        publicReviewDraftRepository.save(publicReviewDraftToBeUpdated)

    }

    fun postToWebsite(publicReviewDraft: PublicReviewDraft) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val publicReviewDraftToBeUpdated =
            publicReviewDraftRepository.findById(publicReviewDraft.id)
                .orElseThrow { RuntimeException("No PRD found") }

        publicReviewDraftToBeUpdated.status = "Posted On Website For Comments"
        publicReviewDraftToBeUpdated.varField2 = "Posted On Website For Comments"
        publicReviewDraftToBeUpdated.modifiedOn = Timestamp(System.currentTimeMillis())
        publicReviewDraftToBeUpdated.modifiedBy = loggedInUser.id.toString()

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
        publicReviewDraft.cdID = editedCommitteeDraft.cdID
        publicReviewDraft.prdBy = loggedInUser.id!!
        publicReviewDraft.createdBy = loggedInUser.id.toString()
        publicReviewDraft.status = "Send Draft To Head Of Trade Affairs"
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
        publicReviewDraftRepository.save(editedCommitteeDraft)
        return ProcessInstanceResponseValue(publicReviewDraft.id, "Complete", true, "editedPublicReviewDraft.id")


    }

    fun approvePrd(publicReviewDraft: PublicReviewDraft) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val approveCommitteeDraft =
            publicReviewDraftRepository.findById(publicReviewDraft.id)
                .orElseThrow { RuntimeException("No Public Review Draft found") }

        approveCommitteeDraft.status = "Approved For Balloting"
        approveCommitteeDraft.modifiedOn = Timestamp(System.currentTimeMillis())
        approveCommitteeDraft.modifiedBy = loggedInUser.id.toString()
        approveCommitteeDraft.varField3 = "Approved"
        publicReviewDraftRepository.save(approveCommitteeDraft)


    }

    //get all Prd Approved
    fun getAllPrdApproved(): MutableList<PrdWithUserName> {
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        return publicReviewDraftRepository.findApprovedPublicReviewDraft(loggedInUser.id.toString())
    }



}
