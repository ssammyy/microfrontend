package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import mu.KotlinLogging
import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.engine.repository.Deployment
import org.flowable.task.api.Task
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.StandardsLevyBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.dto.std.*
import org.kebs.app.kotlin.apollo.common.dto.stdLevy.CommentForm
import org.kebs.app.kotlin.apollo.common.dto.stdLevy.NotificationForm
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.IUserRepository
import org.kebs.app.kotlin.apollo.store.repo.std.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.lang.reflect.Type
import java.sql.Timestamp
import java.util.*


@Service
class IntStandardService(
    private val runtimeService: RuntimeService,
    private val taskService: TaskService,
    @Qualifier("processEngine") private val processEngine: ProcessEngine,
    private val repositoryService: RepositoryService,
    private val isAdoptionProposalRepository: ISAdoptionProposalRepository,
    private val isAdoptionCommentsRepository: ISAdoptionCommentsRepository,
    private val iSAdoptionJustificationRepository: ISAdoptionJustificationRepository,
    private val iSUploadStandardRepository: ISUploadStandardRepository,
    private val iSGazetteNoticeRepository: ISGazetteNoticeRepository,
    private val iSGazettementRepository: ISGazettementRepository,
    private val commonDaoServices: CommonDaoServices,
    private val sdIsDocumentUploadsRepository: SdIsDocumentUploadsRepository,
    private val technicalCommitteeRepository: TechnicalCommitteeRepository,
    private val technicalComListRepository: TechnicalComListRepository,
    private val departmentRepository: DepartmentRepository,
    private val departmentListRepository: DepartmentListRepository,
    private val isJustificationUploadsRepository: ISJustificationUploadsRepository,
    private val isAdoptionJustificationRepository: ISAdoptionJustificationRepository,
    private val isStandardUploadsRepository: ISStandardUploadsRepository,
    private val sdisGazetteNoticeUploadsRepository: SDISGazetteNoticeUploadsRepository,
    private val notifications: Notifications,
    private val companyStandardRepository: CompanyStandardRepository,
    private val bpmnService: StandardsLevyBpmn,
    private val internationalStandardRemarksRepository: InternationalStandardRemarksRepository,
    private val nwaWorkshopDraftRepository: NwaWorkShopDraftRepository,
    private val standardRepository: StandardRepository,
    private val userListRepository: UserListRepository,
    private val comStdDraftRepository: ComStdDraftRepository,
    private val comStandardDraftUploadsRepository: ComStandardDraftUploadsRepository,
    private val comStandardDraftCommentsRepository: ComStandardDraftCommentsRepository,
    private val companyStandardRemarksRepository: CompanyStandardRemarksRepository,
    private val iStdStakeHoldersRepository: IStdStakeHoldersRepository,
    private val sdWorkshopStdRepository: SDWorkshopStdRepository,
    private val standardRequestRepository: StandardRequestRepository,
    private val usersRepo: IUserRepository,
    private val applicationMapProperties: ApplicationMapProperties,
    private val stakeholdersSdListRepository: StakeholdersSdListRepository,
    private val stakeholdersCategoriesRepository: StakeholdersCategoriesRepository,
    private val stakeholdersSubCategoriesRepository: StakeholdersSubCategoriesRepository


) {
    val callUrl = applicationMapProperties.mapKebsLevyUrl
    val PROCESS_DEFINITION_KEY = "sd_InternationalStandardsForAdoption"
    val gson = Gson()

    //deploy bpmn file
    fun deployProcessDefinition(): Deployment = repositoryService
        .createDeployment()
        .addClasspathResource("processes/std/International_Standards_for_Adoption_Process.bpmn20.xml")
        .deploy()

    //start the process by process Key
    fun startProcessByKey(): ProcessInstanceResponse {

        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY)
        return ProcessInstanceResponse(processInstance.id, processInstance.isEnded)
    }

    fun getEditorDetails(): List<UserDetailHolder> {
        return userListRepository.getEditorDetails()
    }

    fun getStakeholderListSd(id: Long): List<StakeholdersSdList>? {
        return stakeholdersSdListRepository.getStakeholderListSd(id)
    }

    fun getCategoriesSd(): List<StakeholdersCategories> {
        return stakeholdersCategoriesRepository.getCategoriesSd()
    }

    fun getSubCategoriesSd(id: Long): List<StakeholdersSubCategories>? {
        return stakeholdersSubCategoriesRepository.getSubCategoriesSd(id)
    }

    fun getDraughtsManDetails(): List<UserDetailHolder> {
        return userListRepository.getDraughtsManDetails()
    }

    fun getProofReaderDetails(): List<UserDetailHolder> {
        return userListRepository.getProofReaderDetails()
    }

    fun getTcSecDetails(): List<UserDetailHolder> {
        return userListRepository.getTcSecDetails()
    }


    //find stakeholder
    fun findStandardStakeholders(): List<UserDetailHolder>? {
        return userListRepository.findStandardStakeholders()
    }


    fun mapKEBSOfficersNameListDto(officersName: String): List<String>? {
        val userListType: Type = object : TypeToken<ArrayList<String?>?>() {}.type
        return gson.fromJson(officersName, userListType)
    }

    fun getIntStandardProposals(): MutableList<StandardRequest> {
        val loggedInUser = commonDaoServices.loggedInUserDetails().id
        return standardRequestRepository.getIntStandardProposals(loggedInUser)
    }


    //prepare Adoption Proposal
    fun prepareAdoptionProposal(isAdoptionProposalDto: ISAdoptionProposalDto): ComStdDraft {

        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val variables: MutableMap<String, Any> = HashMap()
        val datePrepared = commonDaoServices.getTimestamp()
        var iSAdoptionProposal = ISAdoptionProposal();
        val circulationDate = isAdoptionProposalDto.circulationDate
        iSAdoptionProposal.proposal_doc_name = isAdoptionProposalDto.proposal_doc_name
        iSAdoptionProposal.circulationDate = isAdoptionProposalDto.circulationDate
        val closingDate: Timestamp = Timestamp.valueOf(circulationDate.toLocalDateTime().plusDays(30))
        iSAdoptionProposal.closingDate = closingDate
        iSAdoptionProposal.tcSecName = loggedInUser.firstName + loggedInUser.lastName
        iSAdoptionProposal.tcSecEmail = loggedInUser.email
        iSAdoptionProposal.title = isAdoptionProposalDto.title
        iSAdoptionProposal.scope = isAdoptionProposalDto.scope
        var standardString = "KS " + isAdoptionProposalDto.iStandardNumber
        var standardTitle = isAdoptionProposalDto.title

        //val str = "abcdefgh"
        var firstChar = standardString?.get(0)
        var secondChar = standardString?.get(1)
        var thirdChar = standardString?.get(0)
        var tcName = loggedInUser.firstName + loggedInUser.lastName

        iSAdoptionProposal.iStandardNumber = standardString
        iSAdoptionProposal.requestId = isAdoptionProposalDto.requestId
        iSAdoptionProposal.adoptionProposalLink = isAdoptionProposalDto.adoptionProposalLink
        iSAdoptionProposal.tcSecAssigned = isAdoptionProposalDto.tcSecAssigned

        iSAdoptionProposal.uploadedBy = isAdoptionProposalDto.uploadedBy
        iSAdoptionProposal.preparedDate = datePrepared
        iSAdoptionProposal.status = 0
        iSAdoptionProposal.proposalNumber = getPRNumber()
        val deadline: Timestamp = Timestamp.valueOf(datePrepared.toLocalDateTime().plusDays(30))
        iSAdoptionProposal.deadlineDate = deadline

        val proposal = isAdoptionProposalRepository.save(iSAdoptionProposal)

        val proposalId = proposal.id

        val cs = ComStdDraft()
        cs.draftNumber = getDRNumber()
        cs.title = isAdoptionProposalDto.title
        cs.deadlineDate = deadline
        cs.proposalId = proposalId
        cs.requestId = isAdoptionProposalDto.requestId
        cs.comStdNumber = isAdoptionProposalDto.iStandardNumber
        cs.scope = isAdoptionProposalDto.scope
        cs.companyName = "KEBS"
        cs.contactOneEmail = loggedInUser.email
        cs.contactOneFullName = loggedInUser.firstName + loggedInUser.lastName
        cs.contactOneTelephone = loggedInUser.cellphone
        cs.status = 0
        cs.uploadDate = datePrepared
        cs.departmentId = isAdoptionProposalDto.departmentId
        cs.departmentName = isAdoptionProposalDto.departmentName
        cs.standardType = "International Standard"

        val draftId = comStdDraftRepository.save(cs)
        val draftNumber = draftId.id
        //iSAdoptionProposal.stakeholdersList=iSAdoptionProposal.stakeholdersList
        iSAdoptionProposal.addStakeholdersList = iSAdoptionProposal.addStakeholdersList

        //val listOne= iSAdoptionProposal.stakeholdersList?.let { mapKEBSOfficersNameListDto(it) }
        val listTwo = iSAdoptionProposal.addStakeholdersList?.let { mapKEBSOfficersNameListDto(it) }

        val targetUrl = "${callUrl}/isPropComments";
        val stakeholdersOne = isAdoptionProposalDto.stakeholdersList
        stakeholdersOne?.forEach { s ->
            val subject = "Invitation to Provide Feedback on Proposed Adoption of International Standard"
            val recipient = s.email
            val user = s.name
            val userId = usersRepo.getUserId(s.email)
            val userPhone = usersRepo.getUserPhone(s.email)

            val shs = IStandardStakeHolders()
            shs.name = user
            shs.email = recipient
            shs.draftId = draftNumber
            shs.dateOfCreation = Timestamp(System.currentTimeMillis())
            shs.telephone = userPhone
            shs.userId = userId
            shs.status = 0
            iStdStakeHoldersRepository.save(shs)

            val messageBody =
                "Dear $user,\nThe Kenya Bureau of Standards is considering the adoption of the International Standard [$standardString, $standardTitle].\n" +
                        "To provide your feedback, please use the following link: [$targetUrl]. You will be prompted to provide your comments on the Adoption Proposal.\n" +
                        "We highly encourage you to share your thoughts with us. If you have any questions or concerns, please do not hesitate to reach out to us at [tcsec@email.org].\n" +
                        "Please note that the absence of any reply or comments will be considered as acceptance of the proposal for adoption and will constitute an approval vote.\n" +
                        "Thank you in advance for your participation and contributions.\nBest regards,\n " +
                        "$tcName "
            if (recipient != null) {
                // notifications.sendEmail(recipient, subject, messageBody)
            }
        }


//                val gson = Gson()
//        KotlinLogging.logger { }.info { "WORKSHOP DRAFT DECISION" + gson.toJson(targetUrl2) }
        val stakeholdersTwo = isAdoptionProposalDto.addStakeholdersList
        stakeholdersTwo?.forEach { t ->
            val sub = "Invitation to Provide Feedback on Proposed Adoption of International Standard"
            val rec = t.stakeHolderEmail
            val userN = t.stakeHolderName
            val phoneNumber = t.stakeHolderPhone

            val st = IStandardStakeHolders()
            st.name = userN
            st.email = rec
            st.draftId = draftNumber
            st.dateOfCreation = Timestamp(System.currentTimeMillis())
            st.telephone = phoneNumber
            st.status = 0
            val sid = iStdStakeHoldersRepository.save(st)
            val pid = sid.id
            val targetUrl2 = "${callUrl}/isProposalComments/$draftNumber/$pid";

            val messageBody =
                "Dear $userN,\nThe Kenya Bureau of Standards is considering the adoption of the International Standard [$standardString, $standardTitle].\n" +
                        "To provide your feedback, please use the following link: [$targetUrl2]. You will be prompted to provide your comments on the Adoption Proposal.\n" +
                        "We highly encourage you to share your thoughts with us. If you have any questions or concerns, please do not hesitate to reach out to us at [tcsec@email.org].\n" +
                        "Please note that the absence of any reply or comments will be considered as acceptance of the proposal for adoption and will constitute an approval vote.\n" +
                        "Thank you in advance for your participation and contributions.\nBest regards,\n " +
                        "$tcName "
            if (rec != null) {
                notifications.sendEmail(rec, sub, messageBody)
            }

        }

        standardRequestRepository.findByIdOrNull(isAdoptionProposalDto.requestId)?.let { standard ->

            with(standard) {
                status = "Adoption Proposal Prepared"

            }
            standardRequestRepository.save(standard)
        } ?: throw Exception("REQUEST NOT FOUND")


        return draftId

    }

    // Upload International Draft
    fun uploadDrFile(
        uploads: ComStandardDraftUploads,
        docFile: MultipartFile,
        doc: String,
        user: UsersEntity,
        DocDescription: String
    ): ComStandardDraftUploads {

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

        return comStandardDraftUploadsRepository.save(uploads)
    }


    fun uploadISFile(
        uploads: SdIsDocumentUploads,
        docFile: MultipartFile,
        doc: String,
        user: UsersEntity,
        DocDescription: String
    ): SdIsDocumentUploads {

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

        return sdIsDocumentUploadsRepository.save(uploads)
    }

    fun getProposal(): MutableList<ProposalDetails> {
        return isAdoptionProposalRepository.getProposalDetails();
    }


    fun getProposals(proposalId: Long, commentId: Long): MutableList<ProposalDetails> {

        return isAdoptionProposalRepository.getProposals(proposalId, commentId)
    }

    fun getWebProposals(): MutableList<ProposalDetails> {

        return isAdoptionProposalRepository.getWebProposals()
    }

    fun getSessionProposals(): MutableList<ProposalDetails>? {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        return loggedInUser.id?.let { isAdoptionProposalRepository.getSessionProposals(it) }
    }

    //Submit Adoption Proposal comments


    //    fun submitDraftComments(comDraft: List<IntDraftCommentDto>){
//        val variables: MutableMap<String, Any> = HashMap()
//        var  comDraftCommentsSaved = ComDraftComments();
//        comDraft.forEach { com->
//           val  comDraftComments = ComDraftComments();
//            comDraftComments.uploadDate=com.preparedDate
//            comDraftComments.emailOfRespondent=com.emailOfRespondent
//            comDraftComments.phoneOfRespondent=com.phoneOfRespondent
//            comDraftComments.observation=com.observation
//            comDraftComments.draftComment=com.comment
//            comDraftComments.commentTitle=com.commentTitle
//            comDraftComments.commentDocumentType=com.commentDocumentType
//            comDraftComments.comClause=com.clause
//            comDraftComments.comParagraph=com.paragraph
//            comDraftComments.typeOfComment=com.typeOfComment
//            comDraftComments.proposedChange=com.proposedChange
//            comDraftComments.requestID=com.requestID
//            comDraftComments.draftID=com.draftID
//            comDraftComments.nameOfRespondent=com.nameOfRespondent
//            comDraftComments.nameOfOrganization=com.nameOfOrganization
//            comDraftComments.scope=com.scope
//            comDraftComments.commentTime = Timestamp(System.currentTimeMillis())
//            comDraftCommentsSaved = comStandardDraftCommentsRepository.save(comDraftComments)
//
//            val commentNumber=comStdDraftRepository.getISDraftCommentCount(com.draftID)
//            comStdDraftRepository.findByIdOrNull(com.draftID)?.let { comStdDraft ->
//                with(comStdDraft) {
//                    commentCount= commentNumber+1
//
//                }
//                comStdDraftRepository.save(comStdDraft)
//            }?: throw Exception("REQUEST NOT FOUND")
//
//        }
//
//        println("Comment Submitted")
//    }
    fun getProposalComment(id: Long): MutableIterable<ComDraftComments>? {
        return comStandardDraftCommentsRepository.findAllById(id)
    }

    fun submitDraftComments(com: ProposalCommentsDto) {
        val variables: MutableMap<String, Any> = HashMap()
        var comDraftCommentsSaved = ComDraftComments();

        val comDraftComments = ComDraftComments();
        comDraftComments.reason = com.reasons
        comDraftComments.recommendations = com.recommendations
        comDraftComments.adoptDraft = com.adoptionAcceptableAsPresented
        comDraftComments.requestID = com.requestId
        comDraftComments.draftID = com.draftId
        comDraftComments.positionOfRespondent = com.positionOfRespondent
        comDraftComments.nameOfRespondent = com.nameOfRespondent
        comDraftComments.phoneOfRespondent = com.phoneOfRespondent
        comDraftComments.emailOfRespondent = com.emailOfRespondent
        comDraftComments.nameOfOrganization = com.nameOfOrganization
        comDraftComments.commentTime = Timestamp(System.currentTimeMillis())
        comDraftCommentsSaved = comStandardDraftCommentsRepository.save(comDraftComments)
        val adoptDecision = com.adoptionAcceptableAsPresented
        var newAdopt: Long
        var newNotAdopt: Long

        val commentNumber = comStdDraftRepository.getISDraftCommentCount(com.draftId)
        val adoptNumber = comStdDraftRepository.getISDraftAdoptCount(com.draftId)
        val notAdoptNumber = comStdDraftRepository.getISDraftNotAdoptCount(com.draftId)
        val title=com.commentTitle
        val standardNo= com.standardNumber
        if (adoptDecision == "Yes") {
            newAdopt = adoptNumber + 1
            newNotAdopt = notAdoptNumber

        } else {
            newAdopt = adoptNumber
            newNotAdopt = notAdoptNumber + 1
        }
        comStdDraftRepository.findByIdOrNull(com.draftId)?.let { comStdDraft ->
            with(comStdDraft) {
                commentCount = commentNumber + 1
                adopt = newAdopt
                notAdopt = newNotAdopt

            }
            comStdDraftRepository.save(comStdDraft)
        } ?: throw Exception("REQUEST NOT FOUND")

        iStdStakeHoldersRepository.findByIdOrNull(com.stakeHolderId)?.let { stakeHolder ->
            with(stakeHolder) {
                status = 1
                commentId = comDraftCommentsSaved.id
            }
            iStdStakeHoldersRepository.save(stakeHolder)
        } ?: throw Exception("USER NOT FOUND")

        val sub = "Acknowledgement of Feedback on Proposed Adoption of International Standard"
        val rec = com.emailOfRespondent
        val userN = com.nameOfRespondent
        val kebsEmail = "tcsec@kebs.org"
        val messageBody = "Dear $userN,\nWe appreciate your feedback regarding the proposed adoption of [$title, $standardNo]. Your comment has been received and duly noted. If you have any further information that may influence your decision," +
                " we encourage you to share it with the TC-Secretary by emailing. $kebsEmail.\n Thank you for your valuable input.\nKind regards, "
        if (rec != null) {
            notifications.sendEmail(rec, sub, messageBody)
        }




        println("Comment Submitted")
    }

    fun submitWebsiteComments(com: ProposalCommentsDto): CommentForm {
        val variables: MutableMap<String, Any> = HashMap()
        //var  comDraftCommentsSaved = ComDraftComments();
        var slFormResponse = ""
        var responseStatus = ""
        var responseButton = ""
        var response = ""

        val comDraftComments = ComDraftComments();
        comDraftComments.reason = com.reasons
        comDraftComments.recommendations = com.recommendations
        comDraftComments.adoptDraft = com.adoptionAcceptableAsPresented
        comDraftComments.requestID = com.requestId
        comDraftComments.draftID = com.draftId
        comDraftComments.positionOfRespondent = com.positionOfRespondent
        comDraftComments.nameOfRespondent = com.nameOfRespondent
        comDraftComments.phoneOfRespondent = com.phoneOfRespondent
        comDraftComments.emailOfRespondent = com.emailOfRespondent
        comDraftComments.nameOfOrganization = com.nameOfOrganization
        comDraftComments.commentTime = Timestamp(System.currentTimeMillis())
        val title=com.commentTitle
        val standardNo= com.standardNumber

        val adoptDecision = com.adoptionAcceptableAsPresented
        var newAdopt: Long
        var newNotAdopt: Long
        val commentCounts = iStdStakeHoldersRepository.countComments(com.emailOfRespondent, com.draftId)
        val toCheckCom: Long = 0
        if (commentCounts == toCheckCom) {
            val comDraftCommentsSaved = comStandardDraftCommentsRepository.save(comDraftComments)
            val commentNumber = comStdDraftRepository.getISDraftCommentCount(com.draftId)
            val adoptNumber = comStdDraftRepository.getISDraftAdoptCount(com.draftId)
            val notAdoptNumber = comStdDraftRepository.getISDraftNotAdoptCount(com.draftId)
            if (adoptDecision == "Yes") {
                newAdopt = adoptNumber + 1
                newNotAdopt = notAdoptNumber

            } else {
                newAdopt = adoptNumber
                newNotAdopt = notAdoptNumber + 1
            }
            comStdDraftRepository.findByIdOrNull(com.draftId)?.let { comStdDraft ->
                with(comStdDraft) {
                    commentCount = commentNumber + 1
                    adopt = newAdopt
                    notAdopt = newNotAdopt

                }
                comStdDraftRepository.save(comStdDraft)
            } ?: throw Exception("REQUEST NOT FOUND")


            val st = IStandardStakeHolders()
            st.name = com.nameOfRespondent
            st.email = com.emailOfRespondent
            st.draftId = com.draftId
            st.dateOfCreation = Timestamp(System.currentTimeMillis())
            st.telephone = com.phoneOfRespondent
            st.status = 1
            st.commentId = comDraftCommentsSaved.id
            val sid = iStdStakeHoldersRepository.save(st)

            val sub = "Acknowledgement of Feedback on Proposed Adoption of International Standard"
            val rec = com.emailOfRespondent
            val userN = com.nameOfRespondent
            val kebsEmail = "tcsec@kebs.org"
            val messageBody = "Dear $userN,\nWe appreciate your feedback regarding the proposed adoption of [$title, $standardNo]. Your comment has been received and duly noted. If you have any further information that may influence your decision," +
                    " we encourage you to share it with the TC-Secretary by emailing. $kebsEmail.\n Thank you for your valuable input.\nKind regards, "
            if (rec != null) {
                notifications.sendEmail(rec, sub, messageBody)
            }

            slFormResponse = "Comment Saved"
            responseStatus = "success"
            responseButton = "btn btn-success form-wizard-next-btn"
            response = "Saved"
        } else {
            slFormResponse = "You have already commented on this proposal you can't make another comment on it"
            responseStatus = "error"
            responseButton = "btn btn-danger form-wizard-next-btn"
            response = "Not Saved"
        }

        return CommentForm(slFormResponse, responseStatus, responseButton, response)


    }

    fun submitDraftComment(com: ProposalCommentsDto) {
        val variables: MutableMap<String, Any> = HashMap()
        //var  comDraftCommentsSaved = ComDraftComments();
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        val comDraftComments = ComDraftComments();
        comDraftComments.reason = com.reasons
        comDraftComments.recommendations = com.recommendations
        comDraftComments.adoptDraft = com.adoptionAcceptableAsPresented
        comDraftComments.requestID = com.requestId
        comDraftComments.draftID = com.draftId
        comDraftComments.positionOfRespondent = com.positionOfRespondent
        comDraftComments.nameOfRespondent = com.nameOfRespondent
        comDraftComments.phoneOfRespondent = loggedInUser.cellphone
        comDraftComments.emailOfRespondent = loggedInUser.email
        comDraftComments.nameOfOrganization = com.nameOfOrganization
        comDraftComments.commentTime = Timestamp(System.currentTimeMillis())
        val comDraftCommentsSaved = comStandardDraftCommentsRepository.save(comDraftComments)
        val adoptDecision = com.adoptionAcceptableAsPresented
        var newAdopt: Long
        var newNotAdopt: Long

        val commentNumber = comStdDraftRepository.getISDraftCommentCount(com.draftId)
        val adoptNumber = comStdDraftRepository.getISDraftAdoptCount(com.draftId)
        val notAdoptNumber = comStdDraftRepository.getISDraftNotAdoptCount(com.draftId)
        if (adoptDecision == "Yes") {
            newAdopt = adoptNumber + 1
            newNotAdopt = notAdoptNumber

        } else {
            newAdopt = adoptNumber
            newNotAdopt = notAdoptNumber + 1
        }
        comStdDraftRepository.findByIdOrNull(com.draftId)?.let { comStdDraft ->
            with(comStdDraft) {
                commentCount = commentNumber + 1
                adopt = newAdopt
                notAdopt = newNotAdopt

            }
            comStdDraftRepository.save(comStdDraft)
        } ?: throw Exception("REQUEST NOT FOUND")

        iStdStakeHoldersRepository.findByIdOrNull(com.stakeHolderId)?.let { stakeHolder ->
            with(stakeHolder) {
                status = 1
                commentId = comDraftCommentsSaved.id
            }
            iStdStakeHoldersRepository.save(stakeHolder)
        } ?: throw Exception("USER NOT FOUND")



        println("Comment Submitted")
    }

    fun editSubmitDraftComment(com: EditProposalCommentsDto) {
        val adoptDecision = com.adoptionAcceptableAsPresented
        var selectedDecision = " "
        if (adoptDecision == "Yes") {
            selectedDecision = "Yes"
        } else {
            selectedDecision = "No"
        }

        comStandardDraftCommentsRepository.findByIdOrNull(com.commentId)?.let { comDraftComments ->
            with(comDraftComments) {
                reason = com.reasons
                recommendations = com.recommendations
                adoptDraft = selectedDecision
                positionOfRespondent = com.positionOfRespondent
                nameOfRespondent = com.nameOfRespondent
                nameOfOrganization = com.nameOfOrganization
                commentTime = Timestamp(System.currentTimeMillis())

            }
            comStandardDraftCommentsRepository.save(comDraftComments)
        } ?: throw Exception("DRAFT NOT FOUND")


        val initialDecision = com.initialAdoptionAcceptableAsPresented
        var newAdopt: Long
        var newNotAdopt: Long

        val commentNumber = comStdDraftRepository.getISDraftCommentCount(com.draftId)
        val adoptNumber = comStdDraftRepository.getISDraftAdoptCount(com.draftId)
        val notAdoptNumber = comStdDraftRepository.getISDraftNotAdoptCount(com.draftId)
        if (adoptDecision == initialDecision) {
            newAdopt = adoptNumber
            newNotAdopt = notAdoptNumber
        } else {
            if (adoptDecision == "Yes") {
                newAdopt = adoptNumber + 1
                newNotAdopt = notAdoptNumber - 1

            } else {
                newAdopt = adoptNumber - 1
                newNotAdopt = notAdoptNumber + 1
            }
        }

        comStdDraftRepository.findByIdOrNull(com.draftId)?.let { comStdDraft ->
            with(comStdDraft) {
                adopt = newAdopt
                notAdopt = newNotAdopt

            }
            comStdDraftRepository.save(comStdDraft)
        } ?: throw Exception("DRAFT NOT FOUND")

        println("Comment Updated")
    }


    //Submit Adoption Proposal comments
    fun submitAPComments(isAdoptionComments: ISAdoptionComments) {
        val variables: MutableMap<String, Any> = HashMap()

        isAdoptionComments.adoption_proposal_comment = isAdoptionComments.adoption_proposal_comment
        isAdoptionComments.proposalID = isAdoptionComments.proposalID
        isAdoptionComments.commentTitle = isAdoptionComments.commentTitle
        isAdoptionComments.commentDocumentType = isAdoptionComments.commentDocumentType
        isAdoptionComments.comNameOfOrganization = isAdoptionComments.comNameOfOrganization
        isAdoptionComments.comClause = isAdoptionComments.comClause
        isAdoptionComments.scope = isAdoptionComments.scope
        isAdoptionComments.dateOfApplication = isAdoptionComments.dateOfApplication
        isAdoptionComments.comParagraph = isAdoptionComments.comParagraph
        isAdoptionComments.observation = isAdoptionComments.observation
        isAdoptionComments.typeOfComment = isAdoptionComments.typeOfComment
        isAdoptionComments.proposedChange = isAdoptionComments.proposedChange
        isAdoptionComments.recommendations = isAdoptionComments.recommendations
        isAdoptionComments.nameOfRespondent = isAdoptionComments.nameOfRespondent
        isAdoptionComments.positionOfRespondent = isAdoptionComments.positionOfRespondent
        isAdoptionComments.nameOfOrganization = isAdoptionComments.nameOfOrganization

        isAdoptionComments.comment_time = Timestamp(System.currentTimeMillis())
        isAdoptionCommentsRepository.save(isAdoptionComments)

        val commentNumber = isAdoptionProposalRepository.getCommentCount(isAdoptionComments.proposalID)

        isAdoptionProposalRepository.findByIdOrNull(isAdoptionComments.proposalID)?.let { iSAdoptionProposal ->
            with(iSAdoptionProposal) {
                noOfComments = commentNumber + 1

            }
            isAdoptionProposalRepository.save(iSAdoptionProposal)
        } ?: throw Exception("REQUEST NOT FOUND")

        println("Comment Submitted")
    }


    fun getProposalComments(proposalId: Long): MutableIterable<ISProposalComments>? {
        return isAdoptionCommentsRepository.getProposalComments(proposalId)
    }


    //Function to retrieve task details for any candidate group
    private fun getTaskDetails(tasks: List<Task>): List<InternationalStandardTasks> {
        val taskDetails: MutableList<InternationalStandardTasks> = ArrayList()
        for (task in tasks) {
            val processVariables = taskService.getVariables(task.id)
            taskDetails.add(InternationalStandardTasks(task.id, task.name, task.processInstanceId, processVariables))

        }
        return taskDetails
    }

    fun getUserTasks(): List<InternationalStandardTasks> {
        val tasks = taskService.createTaskQuery()
            .taskAssignee("${commonDaoServices.loggedInUserDetails().id ?: throw NullValueNotAllowedException(" invalid user id provided")}")
            .list()
        return getTaskDetails(tasks)
    }

    //Get justification Document
    fun findUploadedFileBYId(isDocumentId: Long): SdIsDocumentUploads {
        return sdIsDocumentUploadsRepository.findByIsDocumentId(isDocumentId)
            ?: throw ExpectedDataNotFound("No File found with the following [ id=$isDocumentId]")
    }

    fun getAllComments(proposalId: Long): MutableIterable<ISAdoptionComments>? {
        return isAdoptionCommentsRepository.findByProposalID(proposalId)
    }

    fun decisionOnProposal(
        comStdDraft: ComStdDraft,
        companyStandardRemarks: CompanyStandardRemarks
    ): NotificationForm {

        var slFormResponse = ""
        var responseStatus = ""
        var responseButton = ""
        var response = ""

        val loggedInUser = commonDaoServices.loggedInUserDetails()
        comStdDraft.accentTo = comStdDraft.accentTo
        val decision = comStdDraft.accentTo
        val commentNumber = comStdDraftRepository.getISDraftCommentCount(comStdDraft.id)
        // val countNo=commentNumber.toString()
        val fName = loggedInUser.firstName
        val sName = loggedInUser.lastName
        val usersName = "$fName  $sName"
        var dateOfRemark = Timestamp(System.currentTimeMillis())
        companyStandardRemarks.requestId = comStdDraft.id
        companyStandardRemarks.remarks = companyStandardRemarks.remarks
        companyStandardRemarks.status = 1.toString()
        companyStandardRemarks.dateOfRemark = dateOfRemark
        companyStandardRemarks.remarkBy = usersName
        companyStandardRemarks.role = "TC Secretary"
        companyStandardRemarks.standardType = "International Standard"
        val deadline: Timestamp =
            Timestamp.valueOf(companyStandardRemarks.dateOfRemark!!.toLocalDateTime().plusMonths(5))

//                val gson = Gson()
//              KotlinLogging.logger { }.info { "WORKSHOP DRAFT" + gson.toJson(comStdDraft) }

        // if (commentNumber>0){
        if (decision == "Yes") {
            comStdDraftRepository.findByIdOrNull(comStdDraft.id)?.let { comStdDraft ->
                with(comStdDraft) {
                    status = 1
                    tcAcceptanceDate = dateOfRemark
                    tcDecision="Proposal Approved by Technical Committee.The TC Secretary was $usersName"


                }
                comStdDraftRepository.save(comStdDraft)
                companyStandardRemarksRepository.save(companyStandardRemarks)
                slFormResponse = "Proposal Was Approved"
                responseStatus = "success"
                responseButton = "btn btn-success form-wizard-next-btn"
                response = "Approved"
            } ?: throw Exception("DRAFT NOT FOUND")




        } else if (decision == "No") {
            comStdDraftRepository.findByIdOrNull(comStdDraft.id)?.let { comStdDraft ->

                with(comStdDraft) {
                    status = 1
                    tcAcceptanceDate = dateOfRemark
                    tcDecision="Proposal Was not Approved by Technical Committee.The TC Secretary was $usersName"
                }
                comStdDraftRepository.save(comStdDraft)
                companyStandardRemarksRepository.save(companyStandardRemarks)

                slFormResponse = "Proposal Was Not Approved"
                responseStatus = "error"
                responseButton = "btn btn-danger form-wizard-next-btn"
                response = "Not Approved"
            } ?: throw Exception("DRAFT NOT FOUND")


        }
        return NotificationForm(slFormResponse, responseStatus, responseButton, response)
    }

    fun getDraftComments(requestId: Long): MutableIterable<CompanyStandardRemarks>? {
        return companyStandardRemarksRepository.getDraftComments(requestId)
    }


    fun getApprovedProposals(): MutableList<ProposalDetails> {
        return isAdoptionProposalRepository.getApprovedProposals();
    }


    fun getUserComments(id: Long): MutableIterable<InternationalStandardRemarks>? {
        return internationalStandardRemarksRepository.findAllByProposalIdOrderByIdDesc(id)
    }

    //prepare justification
    fun prepareJustification(isProposalJustification: ISProposalJustification): String {

        val justification = ISAdoptionJustification();
        val variables: MutableMap<String, Any> = HashMap()
        justification.standardNumber = isProposalJustification.standardNumber
        justification.meetingDate = isProposalJustification.meetingDate
        justification.slNumber = isProposalJustification.slNumber
        justification.edition = isProposalJustification.edition
        justification.requestedBy = isProposalJustification.requestedBy
        justification.issuesAddressed = isProposalJustification.issuesAddressed
        justification.tcAcceptanceDate = isProposalJustification.tcAcceptanceDate
        justification.department = isProposalJustification.department
        justification.proposalId = isProposalJustification.proposalId
        justification.draftId = isProposalJustification.draftId
        justification.scope = isProposalJustification.scope
        justification.purposeAndApplication = isProposalJustification.purposeAndApplication
        justification.intendedUsers = isProposalJustification.intendedUsers
        justification.circulationDate = isProposalJustification.circulationDate
        justification.closingDate = isProposalJustification.closingDate
        justification.tcSec_id = isProposalJustification.tcSecName
        justification.title = isProposalJustification.title
        justification.standardNumber = isProposalJustification.standardNumber
        justification.referenceMaterial = isProposalJustification.referenceMaterial
        justification.status = 0.toString()

        justification.submissionDate = Timestamp(System.currentTimeMillis())

        justification.requestNumber = getRQNumber()
        justification.departmentName =
            departmentListRepository.findNameById(isProposalJustification.department?.toLong())

        isAdoptionJustificationRepository.save(justification)

        comStdDraftRepository.findByIdOrNull(isProposalJustification.draftId)?.let { comStdDraft ->

            with(comStdDraft) {
                status = 3
            }
            comStdDraftRepository.save(comStdDraft)

        } ?: throw Exception("DRAFT NOT FOUND")

        //variables["ID"] = ispDetails.id

        var userList = companyStandardRepository.getHopEmailList()

        //email to Head of publishing
        val targetUrl = "${callUrl}/";
        userList.forEach { item ->
            //  val recipient="stephenmuganda@gmail.com"
            val recipient = item.getUserEmail()
            val subject = "Justification"
            val messageBody =
                "Dear ${item.getFirstName()} ${item.getLastName()}, Justification for International Standard has been prepared."
            if (recipient != null) {
                // notifications.sendEmail(recipient, subject, messageBody)
            }
        }


        return "Justification Uploaded"

    }

    fun editJustification(isProposalJustification: ISProposalJustification): String {


        val variables: MutableMap<String, Any> = HashMap()
        isAdoptionJustificationRepository.findByIdOrNull(isProposalJustification.justificationId)
            ?.let { justification ->

                with(justification) {
                    standardNumber = isProposalJustification.standardNumber
                    meetingDate = isProposalJustification.meetingDate
                    slNumber = isProposalJustification.slNumber
                    edition = isProposalJustification.edition
                    requestedBy = isProposalJustification.requestedBy
                    issuesAddressed = isProposalJustification.issuesAddressed
                    tcAcceptanceDate = isProposalJustification.tcAcceptanceDate
                    department = isProposalJustification.department
                    proposalId = isProposalJustification.proposalId
                    draftId = isProposalJustification.draftId
                    scope = isProposalJustification.scope
                    purposeAndApplication = isProposalJustification.purposeAndApplication
                    intendedUsers = isProposalJustification.intendedUsers
                    circulationDate = isProposalJustification.circulationDate
                    closingDate = isProposalJustification.closingDate
                    tcSec_id = isProposalJustification.tcSecName
                    title = isProposalJustification.title
                    standardNumber = isProposalJustification.standardNumber
                    referenceMaterial = isProposalJustification.referenceMaterial
                    status = 0.toString()
                    submissionDate = Timestamp(System.currentTimeMillis())


                    departmentName = departmentListRepository.findNameById(isProposalJustification.department?.toLong())

                }
                isAdoptionJustificationRepository.save(justification)

            } ?: throw Exception("JUSTIFICATION NOT FOUND")

        comStdDraftRepository.findByIdOrNull(isProposalJustification.draftId)?.let { comStdDraft ->

            with(comStdDraft) {
                status = 3
            }
            comStdDraftRepository.save(comStdDraft)

        } ?: throw Exception("DRAFT NOT FOUND")

        //variables["ID"] = ispDetails.id

        var userList = companyStandardRepository.getHopEmailList()

        //email to Head of publishing
        val targetUrl = "${callUrl}/";
        userList.forEach { item ->
            //  val recipient="stephenmuganda@gmail.com"
            val recipient = item.getUserEmail()
            val subject = "Justification"
            val messageBody =
                "Dear ${item.getFirstName()} ${item.getLastName()}, Justification for International Standard has been prepared."
            if (recipient != null) {
                // notifications.sendEmail(recipient, subject, messageBody)
            }
        }


        return "Justification Uploaded"

    }

    fun getJustification(): MutableList<JustificationDetails> {
        return isAdoptionProposalRepository.getISJustification();
    }

    fun uploadISJFile(
        uploads: ISJustificationUploads,
        docFile: MultipartFile,
        doc: String,
        user: UsersEntity,
        DocDescription: String
    ): ISJustificationUploads {

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

        return isJustificationUploadsRepository.save(uploads)
    }

    fun getJustificationStatus(draftId: Long): JustificationStatus {
        return isAdoptionJustificationRepository.getJustificationCount(draftId)
    }

    fun getISJustification(draftId: Long): MutableList<ISAdoptionJustification> {
        return isAdoptionJustificationRepository.getISJustification(draftId);
    }

    //Get IS justification Document
    fun findUploadedJSFileBYId(isJSDocumentId: Long): ISJustificationUploads {
        return isJustificationUploadsRepository.findByIsJSDocumentId(isJSDocumentId)
            ?: throw ExpectedDataNotFound("No File found with the following [ id=$isJSDocumentId]")
    }

    fun decisionOnJustification(
        comStdDraft: ComStdDraft,
        companyStandardRemarks: CompanyStandardRemarks
    ): NotificationForm {

        var slFormResponse = ""
        var responseStatus = ""
        var responseButton = ""
        var response = ""

        val loggedInUser = commonDaoServices.loggedInUserDetails()
        comStdDraft.accentTo = comStdDraft.accentTo
        val decision = comStdDraft.accentTo
        val commentNumber = comStdDraftRepository.getISDraftCommentCount(comStdDraft.id)
        // val countNo=commentNumber.toString()
        val fName = loggedInUser.firstName
        val sName = loggedInUser.lastName
        val usersName = "$fName  $sName"
        companyStandardRemarks.requestId = comStdDraft.id
        companyStandardRemarks.remarks = companyStandardRemarks.remarks
        companyStandardRemarks.status = 1.toString()
        companyStandardRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
        companyStandardRemarks.remarkBy = usersName
        companyStandardRemarks.role = "TC Secretary"
        companyStandardRemarks.standardType = "International Standard"
        val deadline: Timestamp =
            Timestamp.valueOf(companyStandardRemarks.dateOfRemark!!.toLocalDateTime().plusMonths(5))


        if (decision == "Yes") {
            comStdDraftRepository.findByIdOrNull(comStdDraft.id)?.let { comStdDraft ->
                with(comStdDraft) {
                    status = 4

                }
                comStdDraftRepository.save(comStdDraft)
                companyStandardRemarksRepository.save(companyStandardRemarks)
                slFormResponse = "Justification Was Approved"
                responseStatus = "success"
                responseButton = "btn btn-success form-wizard-next-btn"
                response = "Approved"
            } ?: throw Exception("DRAFT NOT FOUND")


        } else if (decision == "No") {
            comStdDraftRepository.findByIdOrNull(comStdDraft.id)?.let { comStdDraft ->

                with(comStdDraft) {
                    status = 1
                }
                comStdDraftRepository.save(comStdDraft)
                companyStandardRemarksRepository.save(companyStandardRemarks)

                slFormResponse = "Justification Was Not Approved"
                responseStatus = "error"
                responseButton = "btn btn-danger form-wizard-next-btn"
                response = "Not Approved"
            } ?: throw Exception("DRAFT NOT FOUND")


        }

        return NotificationForm(slFormResponse, responseStatus, responseButton, response)
    }

    fun getApprovedJustification(): MutableList<ProposalDetails> {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        return isAdoptionProposalRepository.getApprovedJustification(loggedInUser.id);
    }

    fun getApprovedBallotDrafts(): MutableList<ProposalDetails> {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        return isAdoptionProposalRepository.getApprovedBallotDrafts(loggedInUser.id);
    }




    fun submitDraftForEditing(isDraftDto: CSDraftDto): CompanyStandard {
        val variable: MutableMap<String, Any> = HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        val com = CompanyStandard()

        com.comStdNumber = isDraftDto.comStdNumber
        com.title = isDraftDto.title
        com.scope = isDraftDto.scope
        com.normativeReference = isDraftDto.normativeReference
        com.symbolsAbbreviatedTerms = isDraftDto.symbolsAbbreviatedTerms
        com.clause = isDraftDto.clause
        com.documentType = isDraftDto.documentType
        com.preparedBy = isDraftDto.preparedBy
        com.documentType = isDraftDto.docName
        com.special = isDraftDto.special
        com.requestId = isDraftDto.requestId
        com.draftId = isDraftDto.draftId
        com.departmentId = isDraftDto.departmentId
        com.subject = isDraftDto.subject
        com.description = isDraftDto.description
        com.status = 1
        com.standardType =isDraftDto.standardType
        com.preparedBy = loggedInUser.firstName + loggedInUser.lastName
        com.draftReviewStatus=isDraftDto.draftReviewStatus


        val draftStandard = companyStandardRepository.save(com)
        comStdDraftRepository.findByIdOrNull(isDraftDto.draftId)?.let { comStdDraft ->
            with(comStdDraft) {
                status = 6

            }
            comStdDraftRepository.save(comStdDraft)
        } ?: throw Exception("DRAFT NOT FOUND")

        var userList = companyStandardRepository.getHopEmailList()

        //email to Head of publishing
        val targetUrl = "${callUrl}/";
        userList.forEach { item ->
            //val recipient="stephenmuganda@gmail.com"
            val recipient = item.getUserEmail()
            val subject = "Standard"
            val messageBody = "Dear ${item.getFirstName()} ${item.getLastName()}, A standard has been uploaded."
            if (recipient != null) {
                // notifications.sendEmail(recipient, subject, messageBody)
            }
        }
        return draftStandard
    }


    // Decision on Justification
    fun decisionOnJustificationx(
        iSAdoptionJustification: ISAdoptionJustification,
        internationalStandardRemarks: InternationalStandardRemarks
    ): NotificationForm {
        var slFormResponse = ""
        var responseStatus = ""
        var responseButton = ""
        var response = ""

        val loggedInUser = commonDaoServices.loggedInUserDetails()
        iSAdoptionJustification.accentTo = iSAdoptionJustification.accentTo
        val decision = iSAdoptionJustification.accentTo

        val fName = loggedInUser.firstName
        val sName = loggedInUser.lastName
        val usersName = "$fName  $sName"
        internationalStandardRemarks.proposalId = internationalStandardRemarks.proposalId
        internationalStandardRemarks.remarks = internationalStandardRemarks.remarks
        internationalStandardRemarks.status = 1.toString()
        internationalStandardRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
        internationalStandardRemarks.remarkBy = usersName
        internationalStandardRemarks.role = "TC SEC"

        if (decision == "Yes") {
            iSAdoptionJustificationRepository.findByIdOrNull(iSAdoptionJustification.id)
                ?.let { iSAdoptionJustification ->
                    with(iSAdoptionJustification) {
                        status = 1.toString()

                    }
                    iSAdoptionJustificationRepository.save(iSAdoptionJustification)
                    internationalStandardRemarksRepository.save(internationalStandardRemarks)
                } ?: throw Exception("JUSTIFICATION NOT FOUND")
            slFormResponse = "Justification Was Approved"
            responseStatus = "success"
            responseButton = "btn btn-success form-wizard-next-btn"
            response = "Approved"

        } else if (decision == "No") {
            iSAdoptionJustificationRepository.findByIdOrNull(iSAdoptionJustification.id)
                ?.let { iSAdoptionJustification ->

                    with(iSAdoptionJustification) {
                        status = 2.toString()
                    }
                    iSAdoptionJustificationRepository.save(iSAdoptionJustification)
                    internationalStandardRemarksRepository.save(internationalStandardRemarks)
                } ?: throw Exception("JUSTIFICATION NOT FOUND")
            slFormResponse = "Justification Was Not Approved"
            responseStatus = "error"
            responseButton = "btn btn-danger form-wizard-next-btn"
            response = "Not Approved"

        }

        return NotificationForm(slFormResponse, responseStatus, responseButton, response)
    }

    fun getApprovedISJustification(): MutableList<ISAdoptionProposalJustification> {
        return iSAdoptionJustificationRepository.getApprovedISJustification()
    }


    fun justificationDecision(
        isJustificationDecision: ISJustificationDecision,
        internationalStandardRemarks: InternationalStandardRemarks
    ): List<InternationalStandardTasks> {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        variables["Yes"] = isJustificationDecision.accentTo
        variables["No"] = isJustificationDecision.accentTo
        isJustificationDecision.comments.let {
            if (it != null) {
                variables.put("comments", it)
            }
        }
        isJustificationDecision.taskId.let { variables.put("taskId", it) }
        isJustificationDecision.processId.let { variables.put("processId", it) }

        val fname = loggedInUser.firstName
        val sname = loggedInUser.lastName
        val usersName = "$fname  $sname"
        internationalStandardRemarks.proposalId = isJustificationDecision.approvalID
        internationalStandardRemarks.remarks = isJustificationDecision.comments
        internationalStandardRemarks.status = 1.toString()
        internationalStandardRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
        internationalStandardRemarks.remarkBy = usersName

        if (variables["Yes"] == true) {
            var userList = companyStandardRepository.getStakeHoldersEmailList()
            //email to Head of publishing
            userList.forEach { item ->
                //val recipient="stephenmuganda@gmail.com"
                val recipient = item.getUserEmail()
                val subject = "International Standard"
                val messageBody =
                    "Dear ${item.getFirstName()} ${item.getLastName()}, International Standard Adoption Proposal has been approved for publishing."
                if (recipient != null) {
                    // notifications.sendEmail(recipient, subject, messageBody)
                }
            }
            isJustificationDecision.assignedTo = companyStandardRepository.getHopId()


            internationalStandardRemarksRepository.save(internationalStandardRemarks)
            runtimeService.createProcessInstanceQuery()
                .processInstanceId(isJustificationDecision.processId).list()
                ?.let { l ->
                    val processInstance = l[0]
                    taskService.complete(isJustificationDecision.taskId, variables)

                    taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
                        ?.let { t ->
                            t.list()[0]
                                ?.let { task ->
                                    task.assignee = "${
                                        isJustificationDecision.assignedTo ?: throw NullValueNotAllowedException(
                                            " invalid user id provided"
                                        )
                                    }"  //set the assignee}"
                                    //task.dueDate = standardLevyFactoryVisitReportEntity.scheduledVisitDate  //set the due date
                                    taskService.saveTask(task)
                                }
                                ?: KotlinLogging.logger { }
                                    .error("Task list empty for $PROCESS_DEFINITION_KEY ")


                        }
                        ?: KotlinLogging.logger { }.error("No task found for $PROCESS_DEFINITION_KEY ")
                    bpmnService.slAssignTask(
                        processInstance.processInstanceId,
                        "checkRequirementsMet",
                        isJustificationDecision.assignedTo
                            ?: throw NullValueNotAllowedException("invalid user id provided")
                    )

                }
                ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${isJustificationDecision.processId} ")


        } else if (variables["No"] == false) {
            isJustificationDecision.assignedTo = companyStandardRepository.getSpcSecId()


            internationalStandardRemarksRepository.save(internationalStandardRemarks)
            runtimeService.createProcessInstanceQuery()
                .processInstanceId(isJustificationDecision.processId).list()
                ?.let { l ->
                    val processInstance = l[0]
                    taskService.complete(isJustificationDecision.taskId, variables)

                    taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
                        ?.let { t ->
                            t.list()[0]
                                ?.let { task ->
                                    task.assignee = "${
                                        isJustificationDecision.assignedTo ?: throw NullValueNotAllowedException(
                                            " invalid user id provided"
                                        )
                                    }"  //set the assignee}"
                                    //task.dueDate = standardLevyFactoryVisitReportEntity.scheduledVisitDate  //set the due date
                                    taskService.saveTask(task)
                                }
                                ?: KotlinLogging.logger { }
                                    .error("Task list empty for $PROCESS_DEFINITION_KEY ")


                        }
                        ?: KotlinLogging.logger { }.error("No task found for $PROCESS_DEFINITION_KEY ")
                    bpmnService.slAssignTask(
                        processInstance.processInstanceId,
                        "decisionOnJustification",
                        isJustificationDecision.assignedTo
                            ?: throw NullValueNotAllowedException("invalid user id provided")
                    )

                }
                ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${isJustificationDecision.processId} ")


        }

        return getUserTasks()

    }


    fun getUploadedDraft(): MutableList<ISUploadedDraft> {
        return iSUploadStandardRepository.getUploadedDraft()
    }

    fun getIsPublishingTasks(): MutableList<ISUploadedDraft> {
        return iSUploadStandardRepository.getIsPublishingTasks()
    }


    fun checkRequirements(
        iSDraftDecisions: ISDraftDecisions
    ): NotificationForm {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val comRemarks = CompanyStandardRemarks()

        val decision = iSDraftDecisions.accentTo
        val typeOfStandard = iSDraftDecisions.standardType
        val timeOfRemark = Timestamp(System.currentTimeMillis())

        val fName = loggedInUser.firstName
        val sName = loggedInUser.lastName
        val usersName = "$fName  $sName"
        comRemarks.requestId = iSDraftDecisions.draftId
        comRemarks.remarks = iSDraftDecisions.comments
        comRemarks.status = 1.toString()
        comRemarks.dateOfRemark = timeOfRemark
        comRemarks.remarkBy = usersName
        comRemarks.role = "HOP"
        var slFormResponse = ""
        var responseStatus = ""
        var responseButton = ""
        var response = ""
        val deadline: Timestamp = Timestamp.valueOf(timeOfRemark.toLocalDateTime().plusMonths(5))


        if (decision == "Yes") {
            companyStandardRepository.findByIdOrNull(iSDraftDecisions.id)?.let { companyStandard ->
                with(companyStandard) {
                    status = 3
                    draftStatus = iSDraftDecisions.draftStatus
                    coverPageStatus = iSDraftDecisions.coverPageStatus
                    assignedTo = iSDraftDecisions.assignedTo

                }
                companyStandardRepository.save(companyStandard)
                companyStandardRemarksRepository.save(comRemarks)
            } ?: throw Exception("DRAFT NOT FOUND")

            slFormResponse = "Draft Was Approved"
            responseStatus = "success"
            responseButton = "btn btn-success form-wizard-next-btn"
            response = "Approved"

        } else if (decision == "No") {
            if (typeOfStandard == "Company Standard") {
                companyStandardRepository.findByIdOrNull(iSDraftDecisions.id)?.let { companyStandard ->

                    with(companyStandard) {
                        status = 0
                        draftStatus = iSDraftDecisions.draftStatus
                        coverPageStatus = iSDraftDecisions.coverPageStatus
                    }
                    companyStandardRepository.save(companyStandard)
                    companyStandardRemarksRepository.save(comRemarks)

                } ?: throw Exception("DRAFT NOT FOUND")
            } else if (typeOfStandard == "International Standard") {
                comStdDraftRepository.findByIdOrNull(iSDraftDecisions.draftId)?.let { comStdDraft ->
                    with(comStdDraft) {
                        status = 4
                        draftStatus = iSDraftDecisions.draftStatus
                        coverPageStatus = iSDraftDecisions.coverPageStatus

                    }
                    comStdDraftRepository.save(comStdDraft)
                    companyStandardRemarksRepository.save(comRemarks)

                    companyStandardRepository.findByIdOrNull(iSDraftDecisions.id)?.let { companyStandard ->

                        with(companyStandard) {
                            status = 11
                        }
                        companyStandardRepository.save(companyStandard)

                    } ?: throw Exception("STANDARD NOT FOUND")
                } ?: throw Exception("DRAFT NOT FOUND")
            }

            slFormResponse = "Draft Was Not Approved"
            responseStatus = "error"
            responseButton = "btn btn-danger form-wizard-next-btn"
            response = "Not Approved"

        }

        return NotificationForm(slFormResponse, responseStatus, responseButton, response)
    }

    fun getApprovedDraft(): MutableList<ISUploadedDraft> {
        return iSUploadStandardRepository.getApprovedDraft()
    }

    fun editStandardDraft(isDraftDto: ISDraftDto): CompanyStandard {
        val variable: MutableMap<String, Any> = HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val standard = CompanyStandard()


        val draught = isDraftDto.draughting
        val uploadDate = Timestamp(System.currentTimeMillis())
        val deadline: Timestamp = Timestamp.valueOf(uploadDate.toLocalDateTime().plusDays(30))



        companyStandardRepository.findByIdOrNull(isDraftDto.id)?.let { companyStandard ->

            with(companyStandard) {

                if (draught == "Yes") {
                    status = 4

                } else if (draught == "No") {
                    status = 5

                }
//                status = if (draughting =="Yes"){
//                    4
//                }else{
//                    5
//                }

                requestId = isDraftDto.proposalId
                title = isDraftDto.title
                documentType = isDraftDto.docName
                comStdNumber = isDraftDto.standardNumber
                draughting = isDraftDto.draughting
                requestNumber = isDraftDto.requestNumber
                assignedTo = isDraftDto.assignedTo

            }
            companyStandardRepository.save(companyStandard)

        } ?: throw Exception("DRAFT NOT FOUND")

        return standard
    }

    fun getEditedDraft(): MutableList<ISUploadedDraft> {
        return iSUploadStandardRepository.getEditedDraft()
    }


    fun draughtStandard(isDraftDto: ISDraftDto): ISUploadStandard {
        val variable: MutableMap<String, Any> = HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val standard = ISUploadStandard()

        companyStandardRepository.findByIdOrNull(isDraftDto.id)?.let { companyStandard ->

            with(companyStandard) {
                status = 12
                requestId = isDraftDto.proposalId
                title = isDraftDto.title
                documentType = isDraftDto.docName
                comStdNumber = isDraftDto.standardNumber
                assignedTo = isDraftDto.assignedTo
            }
            companyStandardRepository.save(companyStandard)

        } ?: throw Exception("DRAFT NOT FOUND")

        return standard
    }

    fun assignProofReader(isDraftDto: ISDraftDto): ISUploadStandard {
        val variable: MutableMap<String, Any> = HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val standard = ISUploadStandard()


        companyStandardRepository.findByIdOrNull(isDraftDto.id)?.let { companyStandard ->

            with(companyStandard) {
                status = 5
                assignedTo = isDraftDto.assignedTo
            }
            companyStandardRepository.save(companyStandard)

        } ?: throw Exception("DRAFT NOT FOUND")

        return standard
    }

    fun getDraughtedDraft(): MutableList<ISUploadedDraft> {
        return iSUploadStandardRepository.getDraughtedDraft()
    }

    fun proofReadStandard(isDraftDto: ISDraftDto): ISUploadStandard {
        val variable: MutableMap<String, Any> = HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val standard = ISUploadStandard()


        companyStandardRepository.findByIdOrNull(isDraftDto.id)?.let { companyStandard ->

            with(companyStandard) {
                //status = 6
                status = 13
                title = isDraftDto.title
                documentType = isDraftDto.docName
                comStdNumber = isDraftDto.standardNumber
                requestId = isDraftDto.proposalId
                assignedTo = isDraftDto.assignedTo
            }
            companyStandardRepository.save(companyStandard)

        } ?: throw Exception("DRAFT NOT FOUND")

        return standard
    }


    fun approveProofReadLevel(
        iSDraftDecisions: ISDecisions
    ): NotificationForm {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val comRemarks = CompanyStandardRemarks()
        val decision = iSDraftDecisions.accentTo
        val timeOfRemark = Timestamp(System.currentTimeMillis())
        val typeOfStandard = iSDraftDecisions.standardType

        val fName = loggedInUser.firstName
        val sName = loggedInUser.lastName
        val usersName = "$fName  $sName"

        var slFormResponse = ""
        var responseStatus = ""
        var responseButton = ""
        var response = ""

        comRemarks.requestId = iSDraftDecisions.draftId
        comRemarks.remarks = iSDraftDecisions.comments
        comRemarks.status = 1.toString()
        comRemarks.dateOfRemark = timeOfRemark
        comRemarks.remarkBy = usersName
        comRemarks.role = "HOP"
        var url = ""

        if (decision == "Yes") {
            companyStandardRepository.findByIdOrNull(iSDraftDecisions.id)?.let { companyStandard ->

                with(companyStandard) {
                    status = 6

                }

                companyStandardRepository.save(companyStandard)
                companyStandardRemarksRepository.save(comRemarks)
            } ?: throw Exception("DRAFT NOT FOUND")
            slFormResponse = "Draft Was Approved"
            responseStatus = "success"
            responseButton = "btn btn-success form-wizard-next-btn"
            response = "Approved"

        } else if (decision == "No") {
            comStdDraftRepository.findByIdOrNull(iSDraftDecisions.draftId)?.let { comStdDraft ->

                with(comStdDraft) {
                    status = 4
                }
                comStdDraftRepository.save(comStdDraft)

            } ?: throw Exception("DRAFT NOT FOUND")

            companyStandardRepository.findByIdOrNull(iSDraftDecisions.id)?.let { companyStandard ->
                with(companyStandard) {
                    status = 14
                }
                companyStandardRepository.save(companyStandard)
                companyStandardRemarksRepository.save(comRemarks)

            } ?: throw Exception("DRAFT NOT FOUND")

            slFormResponse = "Draft Was Not Approved"
            responseStatus = "error"
            responseButton = "btn btn-danger form-wizard-next-btn"
            response = "Not Approved"


        }

        return NotificationForm(slFormResponse, responseStatus, responseButton, response)
    }


    fun getProofReadDraft(): MutableList<ISUploadedDraft> {
        return iSUploadStandardRepository.getProofReadDraft()
    }

    fun approveProofReadStandardx(
        iSDraftDecisions: ISDrDecisions
    ): String {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val comRemarks = CompanyStandardRemarks()

        val timeOfRemark = Timestamp(System.currentTimeMillis())

        val fName = loggedInUser.firstName
        val sName = loggedInUser.lastName
        val usersName = "$fName  $sName"
        comRemarks.requestId = iSDraftDecisions.draftId
        comRemarks.remarks = iSDraftDecisions.comments
        comRemarks.status = 1.toString()
        comRemarks.dateOfRemark = timeOfRemark
        comRemarks.remarkBy = usersName
        comRemarks.role = "HOP"

        companyStandardRepository.findByIdOrNull(iSDraftDecisions.id)?.let { companyStandard ->

            with(companyStandard) {
                status = 7

            }
            companyStandardRepository.save(companyStandard)
            companyStandardRemarksRepository.save(comRemarks)
        } ?: throw Exception("DRAFT NOT FOUND")

        return "Actioned"
    }


    fun getApprovedProofReadDraft(): MutableList<ISUploadedDraft> {
        return iSUploadStandardRepository.getApprovedProofReadDraft()
    }

    fun approveProofReadStandard(
        iSDraftDecisions: ISHopDecision
    ): String {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val comRemarks = CompanyStandardRemarks()
        //val decision=iSDraftDecisions.accentTo
        val timeOfRemark = Timestamp(System.currentTimeMillis())
        val decision = iSDraftDecisions.accentTo
        val typeOfStandard=iSDraftDecisions.standardType
        val reviewDraftStatus=iSDraftDecisions.draftReviewStatus
        val toCheckCom: Long = 0

        val fName = loggedInUser.firstName
        val sName = loggedInUser.lastName
        val usersName = "$fName  $sName"
        comRemarks.requestId = iSDraftDecisions.draftId
        comRemarks.remarks = iSDraftDecisions.comments
        comRemarks.status = 1.toString()
        comRemarks.dateOfRemark = timeOfRemark
        comRemarks.remarkBy = usersName
        comRemarks.role = "HOP"
        var url = ""

         if (decision == "Yes") {
             if(typeOfStandard=="Public Review Draft"){
                 if(reviewDraftStatus==toCheckCom){
                     companyStandardRepository.findByIdOrNull(iSDraftDecisions.id)?.let { companyStandard ->

                         with(companyStandard) {
                             status = 25

                         }

                         url = "intSacList"
                         companyStandardRepository.save(companyStandard)
                         companyStandardRemarksRepository.save(comRemarks)
                     } ?: throw Exception("DRAFT NOT FOUND")

                     comStdDraftRepository.findByIdOrNull(iSDraftDecisions.draftId)?.let { comStdDraftTbl ->

                         with(comStdDraftTbl) {
                             status = 25
                             draftReviewStatus=1

                         }

                         url = "intSacList"
                         comStdDraftRepository.save(comStdDraftTbl)
                     } ?: throw Exception("DRAFT NOT FOUND")

                 }else{
                     companyStandardRepository.findByIdOrNull(iSDraftDecisions.id)?.let { companyStandard ->

                         with(companyStandard) {
                             status = 8

                         }
                         url = "intSacList"
                         companyStandardRepository.save(companyStandard)
                         companyStandardRemarksRepository.save(comRemarks)
                         var userList = companyStandardRepository.getSacSecEmailList()
                         val targetUrl = "${callUrl}/$url";
                         userList.forEach { item ->
                             //val recipient="stephenmuganda@gmail.com"
                             val recipient = item.getUserEmail()
                             val subject = "New Standard"
                             val messageBody =
                                 "Dear ${item.getFirstName()} ${item.getLastName()},A New standard has been approved and uploaded for SAC Decision.Click on the link below to view $targetUrl "
                             if (recipient != null) {
                                 // notifications.sendEmail(recipient, subject, messageBody)
                             }
                         }
                     } ?: throw Exception("DRAFT NOT FOUND")
                 }


             }else{
                 companyStandardRepository.findByIdOrNull(iSDraftDecisions.id)?.let { companyStandard ->

                     with(companyStandard) {
                         status = 8

                     }

                     url = "intSacList"
                     companyStandardRepository.save(companyStandard)
                     companyStandardRemarksRepository.save(comRemarks)
                     var userList = companyStandardRepository.getSacSecEmailList()
                     val targetUrl = "${callUrl}/$url";
                     userList.forEach { item ->
                         //val recipient="stephenmuganda@gmail.com"
                         val recipient = item.getUserEmail()
                         val subject = "New Standard"
                         val messageBody =
                             "Dear ${item.getFirstName()} ${item.getLastName()},A New standard has been approved and uploaded for SAC Decision.Click on the link below to view $targetUrl "
                         if (recipient != null) {
                             // notifications.sendEmail(recipient, subject, messageBody)
                         }
                     }
                 } ?: throw Exception("DRAFT NOT FOUND")
             }


        } else if (decision == "No") {
            companyStandardRepository.findByIdOrNull(iSDraftDecisions.id)?.let { companyStandard ->
                with(companyStandard) {
                        status = 21
                    }
                companyStandardRepository.save(companyStandard)
                    companyStandardRemarksRepository.save(comRemarks)

                } ?: throw Exception("DRAFT NOT FOUND")

             comStdDraftRepository.findByIdOrNull(iSDraftDecisions.draftId)?.let { comStdDraft ->
                 with(comStdDraft) {
                     status = 4

                 }
                 comStdDraftRepository.save(comStdDraft)
                 //companyStandardRemarksRepository.save(companyStandardRemarks)
                 // response = "Justification Was Approved"
             } ?: throw Exception("DRAFT NOT FOUND")

             isAdoptionProposalRepository.findByIdOrNull(iSDraftDecisions.proposalId)?.let { prop ->
                 with(prop) {
                     tcSecAssigned=iSDraftDecisions.assignedTo.toString()

                 }
                 isAdoptionProposalRepository.save(prop)
                 //companyStandardRemarksRepository.save(companyStandardRemarks)
                 // response = "Justification Was Approved"
             } ?: throw Exception("DRAFT NOT FOUND")



        }

        return "Actioned"
    }

    fun getApprovedEditedDraft(): MutableList<ISUploadedDraft> {
        return iSUploadStandardRepository.getApprovedEditedDraft()
    }


    fun approveInternationalStandard(
        iSDraftDecisions: ISDraftDecisionsStd
    ): NotificationForm {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val timeOfRemark = Timestamp(System.currentTimeMillis())
        val decision = iSDraftDecisions.accentTo
        val standard = Standard()
        val comRemarks = CompanyStandardRemarks()
        val stdDraft = ISUploadStandard()
        val typeOfStandard = iSDraftDecisions.standardType

        val fName = loggedInUser.firstName
        val sName = loggedInUser.lastName
        val usersName = "$fName  $sName"
        comRemarks.requestId = iSDraftDecisions.draftId
        comRemarks.remarks = iSDraftDecisions.comments
        comRemarks.status = 1.toString()
        comRemarks.dateOfRemark = timeOfRemark
        comRemarks.remarkBy = usersName
        comRemarks.role = "SAC"

        var slFormResponse = ""
        var responseStatus = ""
        var responseButton = ""
        var response = ""

        if (decision == "Yes") {
            companyStandardRepository.findByIdOrNull(iSDraftDecisions.id)?.let { companyStandard ->
                with(companyStandard) {
                    status = 15

                }
                companyStandardRepository.save(companyStandard)
                companyStandardRemarksRepository.save(comRemarks)

                slFormResponse = "Standard Was Approved"
                responseStatus = "success"
                responseButton = "btn btn-success form-wizard-next-btn"
                response = "Approved"

            } ?: throw Exception("DRAFT NOT FOUND")


        } else if (decision == "No") {
            if (typeOfStandard == "International Standard") {
                standardRequestRepository.findByIdOrNull(iSDraftDecisions.requestId)?.let { standard ->

                    with(standard) {
                        status = "Prepare Preliminary Draft"

                    }
                    standardRequestRepository.save(standard)
                    companyStandardRepository.findByIdOrNull(iSDraftDecisions.id)?.let { companyStandard ->
                        with(companyStandard) {
                            status = 11
                        }
                        companyStandardRepository.save(companyStandard)
                        companyStandardRemarksRepository.save(comRemarks)

                    } ?: throw Exception("DRAFT NOT FOUND")
                } ?: throw Exception("REQUEST NOT FOUND")
            } else if (typeOfStandard == "Kenya Standard") {
                companyStandardRepository.findByIdOrNull(iSDraftDecisions.id)?.let { companyStandard ->
                    with(companyStandard) {
                        status = 1
                    }
                    companyStandardRepository.save(companyStandard)
                    companyStandardRemarksRepository.save(comRemarks)

                } ?: throw Exception("DRAFT NOT FOUND")
            }
            slFormResponse = "Standard Was Not Approved"
            responseStatus = "error"
            responseButton = "btn btn-danger form-wizard-next-btn"
            response = "Not Approved"

        }

        return NotificationForm(slFormResponse, responseStatus, responseButton, response)
    }

    fun approveInternationalStandardNSC(
        iSDraftDecisions: ISDraftDecisionsStd
    ): NotificationForm {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val timeOfRemark = Timestamp(System.currentTimeMillis())
        val decision = iSDraftDecisions.accentTo
        val standard = Standard()
        val comRemarks = CompanyStandardRemarks()
        val stdDraft = ISUploadStandard()
        val typeOfStandard = iSDraftDecisions.standardType

        val fName = loggedInUser.firstName
        val sName = loggedInUser.lastName
        val usersName = "$fName  $sName"
        comRemarks.requestId = iSDraftDecisions.draftId
        comRemarks.remarks = iSDraftDecisions.comments
        comRemarks.status = 1.toString()
        comRemarks.dateOfRemark = timeOfRemark
        comRemarks.remarkBy = usersName
        comRemarks.role = "SAC"
        var slFormResponse = ""
        var responseStatus = ""
        var responseButton = ""
        var response = ""

        if (decision == "Yes") {
            if (typeOfStandard == "International Standard") {
                companyStandardRepository.findByIdOrNull(iSDraftDecisions.id)?.let { companyStandard ->
                    with(companyStandard) {
                        status = 9

                    }
                    companyStandardRepository.save(companyStandard)
                    companyStandardRemarksRepository.save(comRemarks)
                    var userList = iStdStakeHoldersRepository.getStakeHoldersList(iSDraftDecisions.draftId)
                    val targetUrl = "${callUrl}/";
                    userList.forEach { item ->
                        //val recipient="stephenmuganda@gmail.com"
                        val recipient = item.getEmail()
                        val subject = "New International Standard" + standard.standardNumber
                        val messageBody = "Dear ${item.getName()} ,Adoption for New standard has been approved "
                        if (recipient != null) {
                            // notifications.sendEmail(recipient, subject, messageBody)
                        }
                    }

                } ?: throw Exception("DRAFT NOT FOUND")
            } else if (typeOfStandard == "Kenya Standard") {
                var kenyaStd = getKSNumber()
                var ks = SDWorkshopStd()
                ks.nwaStdNumber = kenyaStd
                ks.requestId = iSDraftDecisions.draftId
                companyStandardRepository.findByIdOrNull(iSDraftDecisions.id)?.let { companyStandard ->
                    with(companyStandard) {
                        status = 9
                        comStdNumber = kenyaStd

                    }
                    sdWorkshopStdRepository.save(ks)
                    companyStandardRepository.save(companyStandard)
                    companyStandardRemarksRepository.save(comRemarks)

                } ?: throw Exception("DRAFT NOT FOUND")
            }

            slFormResponse = "Standard Was Approved"
            responseStatus = "success"
            responseButton = "btn btn-success form-wizard-next-btn"
            response = "Approved"

        } else if (decision == "No") {
            companyStandardRepository.findByIdOrNull(iSDraftDecisions.id)?.let { companyStandard ->
                with(companyStandard) {
                    status = 8

                }
                companyStandardRepository.save(companyStandard)
                companyStandardRemarksRepository.save(comRemarks)
                slFormResponse = "Standard Was Not Approved"
                responseStatus = "error"
                responseButton = "btn btn-danger form-wizard-next-btn"
                response = "Not Approved"
            } ?: throw Exception("DRAFT NOT FOUND")

        }

        return NotificationForm(slFormResponse, responseStatus, responseButton, response)
    }

    fun getStakeHoldersList(draftId: Long): MutableList<EmailList>? {
        return iStdStakeHoldersRepository.getStakeHoldersList(draftId)
    }

    fun uploadInternationalStandard(iStandardUploadDto: IStandardUploadDto): Standard {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val timeOfRemark = Timestamp(System.currentTimeMillis())
        val standard = Standard()
        val dueDate: Timestamp = Timestamp.valueOf(timeOfRemark.toLocalDateTime().plusYears(5))


        standard.title = iStandardUploadDto.title
        standard.normativeReference = iStandardUploadDto.normativeReference
        standard.symbolsAbbreviatedTerms = iStandardUploadDto.symbolsAbbreviatedTerms
        standard.clause = iStandardUploadDto.clause
        standard.scope = iStandardUploadDto.scope
        standard.special = iStandardUploadDto.special
        standard.standardNumber = iStandardUploadDto.standardNumber
        standard.standardType = iStandardUploadDto.standardType
        standard.status = 0
        standard.dateFormed = timeOfRemark
        standard.createdBy = loggedInUser.id
        standard.dueDateForReview = dueDate
        companyStandardRepository.findByIdOrNull(iStandardUploadDto.id)?.let { companyStandard ->
            with(companyStandard) {
                status = 10

            }
            companyStandardRepository.save(companyStandard)
            standardRepository.save(standard)
        } ?: throw Exception("DRAFT NOT FOUND")

        return standard
    }

    fun getInternationalStandards(): MutableList<Standard> {
        return standardRepository.getInternationalStandards()
    }

    fun getCompanyStandards(): MutableList<Standard> {
        return standardRepository.getCompanyStandards()
    }

    fun getStandards(): MutableList<Standard> {
        return standardRepository.getStandards()
    }

    fun getStandardForGazettement(): MutableList<ISUploadedDraft> {
        return standardRepository.getStandardForGazettement()
    }

    fun uploadGazetteNotice(standard: Standard): Standard {
        val standard = Standard()
        standard.description = standard.description

        standardRepository.findByIdOrNull(standard.id)?.let { standard ->

            with(standard) {
                status = 1
                description = standard.description
                standard.isGazetted = 1
                standard.dateOfGazettement = Timestamp(System.currentTimeMillis())
            }
            standardRepository.save(standard)

        } ?: throw Exception("STANDARD NOT FOUND")

        return standard
    }


    fun uploadISDFile(
        uploads: ISStandardUploads,
        docFile: MultipartFile,
        doc: String,
        user: UsersEntity,
        DocDescription: String
    ): ISStandardUploads {

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

        return isStandardUploadsRepository.save(uploads)
    }

    //Get IS Standard Document
    fun findUploadedSTFileBYId(isStandardID: Long): ISStandardUploads {
        return isStandardUploadsRepository.findByIsStdDocumentId(isStandardID)
            ?: throw ExpectedDataNotFound("No File found with the following [ id=$isStandardID]")
    }

    fun multipleDecisionOnJustification(isSpcMultipleDecisionDto: ISSpcMultipleDecisionDto): String {
        val spcList = isSpcMultipleDecisionDto.decisionList
        spcList?.forEach { s ->
            val decision = s.accentTo
            val draftId = s.draftId

            if (decision == "Yes") {
                comStdDraftRepository.findByIdOrNull(draftId)?.let { comStdDraft ->
                    with(comStdDraft) {
                        status = 4

                    }
                    comStdDraftRepository.save(comStdDraft)
                    //companyStandardRemarksRepository.save(companyStandardRemarks)
                    // response = "Justification Was Approved"
                } ?: throw Exception("DRAFT NOT FOUND")


            } else if (decision == "No") {
                comStdDraftRepository.findByIdOrNull(draftId)?.let { comStdDraft ->

                    with(comStdDraft) {
                        status = 1
                    }
                    comStdDraftRepository.save(comStdDraft)
                    // companyStandardRemarksRepository.save(companyStandardRemarks)

                    // response = "Justification Was Not Approved"
                } ?: throw Exception("DRAFT NOT FOUND")


            }
        }
        return "Actioned"
    }

    fun multipleDecisionOnSacList(isSacMultipleDecisionDto: ISSacMultipleDecisionDto): String {
        val sacList = isSacMultipleDecisionDto.decisionList
        sacList?.forEach { s ->
            val decision = s.accentTo
            val cid = s.id
            val draftId = s.draftId
            val requestId = s.requestId
            val standardType = s.standardType
            val standardNo = s.comStdNumber
            if (decision == "Yes") {
                companyStandardRepository.findByIdOrNull(cid)?.let { companyStandard ->
                    with(companyStandard) {
                        status = 15

                    }
                    companyStandardRepository.save(companyStandard)
                    // companyStandardRemarksRepository.save(comRemarks)

                } ?: throw Exception("DRAFT NOT FOUND")


            } else if (decision == "No") {
                if (standardType == "International Standard") {
                    standardRequestRepository.findByIdOrNull(requestId)?.let { standard ->

                        with(standard) {
                            status = "Prepare Preliminary Draft"

                        }
                        standardRequestRepository.save(standard)
                        companyStandardRepository.findByIdOrNull(cid)?.let { companyStandard ->
                            with(companyStandard) {
                                status = 11
                            }
                            companyStandardRepository.save(companyStandard)
                            // companyStandardRemarksRepository.save(comRemarks)

                        } ?: throw Exception("DRAFT NOT FOUND")
                    } ?: throw Exception("REQUEST NOT FOUND")
                } else if (standardType == "Kenya Standard") {
                    companyStandardRepository.findByIdOrNull(cid)?.let { companyStandard ->
                        with(companyStandard) {
                            status = 1
                        }
                        companyStandardRepository.save(companyStandard)
                        //companyStandardRemarksRepository.save(comRemarks)

                    } ?: throw Exception("DRAFT NOT FOUND")
                }

            }
        }

        return "Decision Saved"
    }

    fun multipleDecisionOnNscList(isSacMultipleDecisionDto: ISSacMultipleDecisionDto): String {
        val sacList = isSacMultipleDecisionDto.decisionList
        sacList?.forEach { s ->
            val decision = s.accentTo
            val cid = s.id
            val draftId = s.draftId
            val requestId = s.requestId
            val standardType = s.standardType
            val standardNo = s.comStdNumber
            if (decision == "Yes") {
                when (standardType) {
                    "International Standard" -> {
                        companyStandardRepository.findByIdOrNull(cid)?.let { companyStandard ->
                            setCompanyStandardStatus(companyStandard, 9)
                        } ?: throw Exception("DRAFT NOT FOUND")
                    }

                    "Kenya Standard" -> {
                        var kenyaStd = getKSNumber()
                        var ks = SDWorkshopStd()
                        ks.nwaStdNumber = kenyaStd
                        ks.requestId = draftId
                        companyStandardRepository.findByIdOrNull(cid)?.let { companyStandard ->
                            with(companyStandard) {
                                status = 9
                                comStdNumber = kenyaStd
                            }
                            sdWorkshopStdRepository.save(ks)
                            companyStandardRepository.save(companyStandard)
                            //companyStandardRemarksRepository.save(comRemarks)
                        } ?: throw Exception("DRAFT NOT FOUND")
                    }

                    else -> throw IllegalArgumentException("Invalid standard type: $standardType")
                }
            } else if (decision == "No") {
                companyStandardRepository.findByIdOrNull(cid)?.let { companyStandard ->
                    with(companyStandard) {
                        status = 8

                    }
                    companyStandardRepository.save(companyStandard)
                    //companyStandardRemarksRepository.save(comRemarks)

                } ?: throw Exception("DRAFT NOT FOUND")

            }


        }
        return "Actioned"
    }

    private fun setCompanyStandardStatus(
        companyStandard: CompanyStandard,
        status: Long
    ) {
        companyStandard.status = status
        companyStandardRepository.save(companyStandard)
        //companyStandardRemarksRepository.save(comRemarks)
    }


    fun getISDNumber(): Pair<String, Long> {
        var allRequests = standardRepository.getMaxISDN()
        allRequests = if (allRequests.equals(null)) {
            0
        } else {
            allRequests
        }

        var startId = "ISN"
        allRequests = allRequests.plus(1)
        val year = Calendar.getInstance()[Calendar.YEAR]
        return Pair("$startId/$allRequests:$year", allRequests)
    }


    fun getPRNumber(): String {
        val allRequests = isAdoptionProposalRepository.findAllByOrderByIdDesc()

        var lastId: String? = "0"
        var finalValue = 1
        var startId = "PR"


        for (item in allRequests) {
            println(item)
            lastId = item.proposalNumber
            break
        }

        if (lastId != "0") {
            val strs = lastId?.split(":")?.toTypedArray()

            val firstPortion = strs?.get(0)

            val lastPortArray = firstPortion?.split("/")?.toTypedArray()

            val intToIncrement = lastPortArray?.get(1)

            finalValue = (intToIncrement?.toInt()!!)
            finalValue += 1
        }


        val year = Calendar.getInstance()[Calendar.YEAR]

        return "$startId/$finalValue:$year";
    }

    fun getRQNumber(): String {
        val allRequests = iSAdoptionJustificationRepository.findAllByOrderByIdDesc()

        var lastId: String? = "0"
        var finalValue = 1
        var startId = "RQ"


        for (item in allRequests) {
            println(item)
            lastId = item.requestNumber
            break
        }

        if (lastId != "0") {
            val strs = lastId?.split(":")?.toTypedArray()

            val firstPortion = strs?.get(0)

            val lastPortArray = firstPortion?.split("/")?.toTypedArray()

            val intToIncrement = lastPortArray?.get(1)

            finalValue = (intToIncrement?.toInt()!!)
            finalValue += 1
        }


        val year = Calendar.getInstance()[Calendar.YEAR]

        return "$startId/$finalValue:$year"
    }

    fun getDRNumber(): String {
        var allRequests = comStdDraftRepository.getMaxDraftId()

        var lastId: String? = "0"
//        var finalValue = 1
        var startId = "DRAFT"

        //allRequests = allRequests+1

        val c = allRequests
        val d = c.toInt()
        val x = 1
        val z = x + d

        val finalValue = z.toString()

//        println("Sum of x+y = $finalValue")

        val year = Calendar.getInstance()[Calendar.YEAR]
        val month = Calendar.getInstance().get(Calendar.MONTH) + 1

        return "$startId/$finalValue/$month:$year"


    }

    fun getISNumber(): String {
        val allRequests = iSUploadStandardRepository.findAllByOrderByIdDesc()

        var lastId: String? = "0"
        var finalValue = 1
        var startId = "IS"


        for (item in allRequests) {
            println(item)
            lastId = item.iSNumber
            break
        }

        if (lastId != "0") {
            val strs = lastId?.split(":")?.toTypedArray()

            val firstPortion = strs?.get(0)

            val lastPortArray = firstPortion?.split("/")?.toTypedArray()

            val intToIncrement = lastPortArray?.get(1)

            finalValue = (intToIncrement?.toInt()!!)
            finalValue += 1
        }


        val year = Calendar.getInstance()[Calendar.YEAR]

        return "$startId/$finalValue:$year"
    }

    fun getKSNumber(): String {
        val allRequests = sdWorkshopStdRepository.findAllByOrderByIdDesc()

        var lastId: String? = "0"
        var finalValue = 1
        var startId = "NWA"


        for (item in allRequests) {
            println(item)
            lastId = item.nwaStdNumber
            break
        }

        if (lastId != "0") {
            val strs = lastId?.split(":")?.toTypedArray()

            val firstPortion = strs?.get(0)

            val lastPortArray = firstPortion?.split("/")?.toTypedArray()

            val intToIncrement = lastPortArray?.get(1)

            finalValue = (intToIncrement?.toInt()!!)
            finalValue += 1
        }


        val year = Calendar.getInstance()[Calendar.YEAR]

        return "$startId/$finalValue:$year"
    }
}


private fun <E> MutableList<E>.addAll(elements: E) {

}
