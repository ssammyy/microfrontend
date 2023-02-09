package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import mu.KotlinLogging
import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.dto.std.ComDraftCommentDto
import org.kebs.app.kotlin.apollo.common.dto.std.NamesList
import org.kebs.app.kotlin.apollo.common.dto.std.ResponseMsg
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.std.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.lang.reflect.Type
import java.sql.Timestamp
import java.util.*


@Service
class ComStandardService(
    private val runtimeService: RuntimeService,
    private val taskService: TaskService,
    @Qualifier("processEngine") private val processEngine: ProcessEngine,
    private val repositoryService: RepositoryService,
    private val comStandardRequestRepository: ComStandardRequestRepository,
    private val productRepository: ProductRepository,
    private val departmentRepository: DepartmentRepository,
    private val productSubCategoryRepository: ProductSubCategoryRepository,
    private val comStdActionRepository: ComStdActionRepository,
    private val comJcJustificationRepository: ComJcJustificationRepository,
    private val comStdDraftRepository: ComStdDraftRepository,
    private val companyStandardRepository: CompanyStandardRepository,
    private val technicalCommitteeRepository: TechnicalCommitteeRepository,
    private val userListRepository: UserListRepository,
    private val comJcJustificationUploadsRepository: ComJcJustificationUploadsRepository,
    private val notifications: Notifications,
    private val commonDaoServices: CommonDaoServices,
    private val comStandardDraftUploadsRepository: ComStandardDraftUploadsRepository,
    private val comStandardUploadsRepository: ComStandardUploadsRepository,
    private val comStandardJCRepository: ComStandardJCRepository,
    private val comStandardRequestUploadsRepository: ComStandardRequestUploadsRepository,
    private val justificationForTCRepository: JustificationForTCRepository,
    private val internationalStandardRemarksRepository: InternationalStandardRemarksRepository,
    private val companyStandardRemarksRepository: CompanyStandardRemarksRepository,
    private val standardRepository: StandardRepository,
    private val comStdJointCommitteeRepository: ComStdJointCommitteeRepository,
    private val comStandardDraftCommentsRepository: ComStandardDraftCommentsRepository,
    private val sdDocumentsRepository: StandardsDocumentsRepository,
) {

    //request for company standard
    fun requestForStandard(companyStandardRequest: CompanyStandardRequest): CompanyStandardRequest {

        val variables: MutableMap<String, Any> = HashMap()
        companyStandardRequest.companyName = companyStandardRequest.companyName
//        companyStandardRequest.tcId = companyStandardRequest.tcId
//        companyStandardRequest.productId = companyStandardRequest.productId
//        companyStandardRequest.productSubCategoryId = companyStandardRequest.productSubCategoryId
        //companyStandardRequest.submissionDate?.let{ variables.put("submissionDate", it)}
        companyStandardRequest.companyPhone = companyStandardRequest.companyPhone
        companyStandardRequest.companyEmail = companyStandardRequest.companyEmail
        companyStandardRequest.submissionDate = Timestamp(System.currentTimeMillis())
        companyStandardRequest.requestNumber = getRQNumber()
        companyStandardRequest.status = 0

        companyStandardRequest.departmentId = companyStandardRequest.departmentId
        companyStandardRequest.subject=companyStandardRequest.subject
        companyStandardRequest.description=companyStandardRequest.description
        companyStandardRequest.contactOneFullName=companyStandardRequest.contactOneFullName
        companyStandardRequest.contactOneTelephone=companyStandardRequest.contactOneTelephone
        companyStandardRequest.contactOneEmail=companyStandardRequest.contactOneEmail
        companyStandardRequest.contactTwoFullName=companyStandardRequest.contactTwoFullName
        companyStandardRequest.contactTwoTelephone=companyStandardRequest.contactTwoTelephone
        companyStandardRequest.contactTwoEmail=companyStandardRequest.contactTwoEmail
        companyStandardRequest.contactThreeFullName=companyStandardRequest.contactThreeFullName
        companyStandardRequest.contactThreeTelephone=companyStandardRequest.contactThreeTelephone
        companyStandardRequest.contactThreeEmail=companyStandardRequest.contactThreeEmail

        //companyStandardRequest.tcName = technicalCommitteeRepository.findNameById(companyStandardRequest.tcId?.toLong())
        companyStandardRequest.departmentName = departmentRepository.findNameById(companyStandardRequest.departmentId?.toLong())

        //companyStandardRequest.productName = productRepository.findNameById(companyStandardRequest.productId?.toLong())

        //companyStandardRequest.productSubCategoryName = productSubCategoryRepository.findNameById(companyStandardRequest.productSubCategoryId?.toLong())

        return comStandardRequestRepository.save(companyStandardRequest)
    }

    fun uploadCommitmentLetter(
        uploads: ComStandardRequestUploads,
        docFile: MultipartFile,
        doc: String,
        user: String,
        DocDescription: String
    ): ComStandardRequestUploads {

        with(uploads) {
//            filepath = docFile.path
            name = commonDaoServices.saveDocuments(docFile)
//            fileType = docFile.contentType
            fileType = commonDaoServices.getFileTypeByMimetypesFileTypeMap(docFile.name)
            documentType = doc
            description = DocDescription
            document = docFile.bytes
            transactionDate = commonDaoServices.getCurrentDate()
            status = 1
            createdBy = user
            createdOn = commonDaoServices.getTimestamp()
        }

        return comStandardRequestUploadsRepository.save(uploads)
    }

    fun findUploadedReportFileBYId(comStdRequestID: Long): ComStandardRequestUploads {
        return comStandardRequestUploadsRepository.findAllByComStdRequestId(comStdRequestID)
    }


    fun getCompanyStandardRequest(): MutableList<ComStdRequest> {
        return comStandardRequestRepository.getCompanyStandardRequest()
    }

    fun getCompanyStandardRequestProcess(): MutableList<ComStdRequest> {
        return comStandardRequestRepository.getCompanyStandardRequestProcess()
    }

    fun assignRequest(companyStandardRequest: CompanyStandardRequest): CompanyStandardRequest {
        var request= CompanyStandardRequest()
        comStandardRequestRepository.findByIdOrNull(companyStandardRequest.id)?.let { companyStandardRequest ->
            with(companyStandardRequest) {
                status = 1
                assignedTo = companyStandardRequest.assignedTo

            }
            request= comStandardRequestRepository.save(companyStandardRequest)

        }?: throw Exception("STANDARD NOT FOUND")

        return request

    }

    fun getAssignedCompanyStandardRequest(): MutableList<ComStdRequest> {
        return comStandardRequestRepository.getCompanyStandardRequest()
    }

    val gson = Gson()
    fun mapKEBSOfficersNameListDto(officersName: String): List<String>? {
        val userListType: Type = object : TypeToken<ArrayList<String?>?>() {}.type
        return gson.fromJson(officersName, userListType)
    }

    fun formJointCommittee(comStandardJointCommittee: ComStandardJointCommittee, detailBody: MutableList<NamesList>?) : String
    {
        var result= CompanyStandardRequest()

        val jointLists= comStandardJointCommittee.name?.let { mapKEBSOfficersNameListDto(it) }

        if (jointLists != null) {
            for (recipient in jointLists) {

                val user = "Joint Committee Member"
                val cj = ComStandardJointCommittee()
                cj.name = user
                cj.email = recipient
                cj.requestId = comStandardJointCommittee.requestId
                cj.dateOfCreation=Timestamp(System.currentTimeMillis())
                cj.telephone="NA"

                comStdJointCommitteeRepository.save(cj)

                val subject = "New Standard Request"
                val messageBody =
                    "Hope You are Well,A new standard request has been received.You have been selected to be in the Joint Committee. "
                notifications.sendEmail(recipient, subject, messageBody)

            }

        }

        detailBody?.forEach { d ->
            val subject = "New Standard Request"
            val recipient = d.email
            val user = d.name
            val jc = ComStandardJointCommittee()
            jc.name=user
            jc.email=recipient
            jc.requestId=comStandardJointCommittee.requestId
            jc.dateOfCreation=Timestamp(System.currentTimeMillis())
            jc.telephone="NA"
            comStdJointCommitteeRepository.save(jc)

                val messageBody= "Dear $user ,A new standard request has been received.You have been selected to be in the Joint Committee. "
            if (recipient != null) {
                notifications.sendEmail(recipient, subject, messageBody)
            }
        }


        KotlinLogging.logger { }.info { "JOINT COMMITTEE EMAIL" + gson.toJson(detailBody) }

        comStandardRequestRepository.findByIdOrNull(comStandardJointCommittee.requestId)?.let { companyStandardRequest ->
            with(companyStandardRequest) {
                status = 2

            }
             comStandardRequestRepository.save(companyStandardRequest)
        }?: throw Exception("REQUEST NOT FOUND")
        return "Saved"
    }

    fun submitJustificationForFormationOfTC(justificationForTC: JustificationForTC): JustificationForTC
    {

        justificationForTC.proposer=justificationForTC.proposer
        justificationForTC.purpose=justificationForTC.purpose
        justificationForTC.subject=justificationForTC.subject
        justificationForTC.scope=justificationForTC.scope
        justificationForTC.targetDate=justificationForTC.targetDate
        justificationForTC.proposedRepresentation=justificationForTC.proposedRepresentation
        justificationForTC.programmeOfWork=justificationForTC.programmeOfWork
        justificationForTC.organization=justificationForTC.organization
        justificationForTC.liaisonOrganization=justificationForTC.liaisonOrganization
        justificationForTC.dateOfPresentation=justificationForTC.dateOfPresentation
        justificationForTC.nameOfTC=justificationForTC.nameOfTC
        justificationForTC.referenceNumber=justificationForTC.referenceNumber
        justificationForTC.comRequestId=justificationForTC.comRequestId
        justificationForTC.status=0


        return justificationForTCRepository.save(justificationForTC)

    }

    fun getComTcJustification(): MutableList<JustificationForTC> {
        return justificationForTCRepository.getComTcJustification()
    }

    fun approveJustification(
        justificationForTC: JustificationForTC,
        companyStandardRemarks: CompanyStandardRemarks
    ) : String {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        justificationForTC.accentTo=justificationForTC.accentTo
        val decision=justificationForTC.accentTo

        val fName = loggedInUser.firstName
        val sName = loggedInUser.lastName
        val usersName = "$fName  $sName"
        companyStandardRemarks.requestId= companyStandardRemarks.requestId
        companyStandardRemarks.remarks= companyStandardRemarks.remarks
        companyStandardRemarks.status = 1.toString()
        companyStandardRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
        companyStandardRemarks.remarkBy = usersName
        companyStandardRemarks.role = "HOF"

        if (decision == "Yes") {
            justificationForTCRepository.findByIdOrNull(justificationForTC.id)?.let { justificationForTC ->
                with(justificationForTC) {
                    status = 1

                }
                justificationForTCRepository.save(justificationForTC)
                companyStandardRemarksRepository.save(companyStandardRemarks)
            }?: throw Exception("JUSTIFICATION NOT FOUND")

        } else if (decision == "No") {
            justificationForTCRepository.findByIdOrNull(justificationForTC.id)?.let { justificationForTC ->

                with(justificationForTC) {
                    status = 2
                }
                    justificationForTCRepository.save(justificationForTC)
                    companyStandardRemarksRepository.save(companyStandardRemarks)


            } ?: throw Exception("JUSTIFICATION NOT FOUND")

        }

        return "Actioned"
    }

    fun getComApprovedTcJustification(): MutableList<JustificationForTC> {
        return justificationForTCRepository.getComApprovedTcJustification()
    }
    fun approveSpcJustification(
        justificationForTC: JustificationForTC,
        companyStandardRemarks: CompanyStandardRemarks
    ) : String {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        justificationForTC.accentTo=justificationForTC.accentTo
        val decision=justificationForTC.accentTo

        val fName = loggedInUser.firstName
        val sName = loggedInUser.lastName
        val usersName = "$fName  $sName"
        companyStandardRemarks.requestId= companyStandardRemarks.requestId
        companyStandardRemarks.remarks= companyStandardRemarks.remarks
        companyStandardRemarks.status = 1.toString()
        companyStandardRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
        companyStandardRemarks.remarkBy = usersName
        companyStandardRemarks.role = "SPC"

        if (decision == "Yes") {
            justificationForTCRepository.findByIdOrNull(justificationForTC.id)?.let { justificationForTC ->
                with(justificationForTC) {
                    status = 3

                }
                justificationForTCRepository.save(justificationForTC)
                companyStandardRemarksRepository.save(companyStandardRemarks)
            }?: throw Exception("JUSTIFICATION NOT FOUND")

        } else if (decision == "No") {

            justificationForTCRepository.findByIdOrNull(justificationForTC.id)?.let { justificationForTC ->

                with(justificationForTC) {
                    status = 0
                }
                justificationForTCRepository.save(justificationForTC)
                companyStandardRemarksRepository.save(companyStandardRemarks)

            } ?: throw Exception("JUSTIFICATION NOT FOUND")

        }

        return "Actioned"
    }

    fun getApprovedSpcComTcJustification(): MutableList<JustificationForTC> {
        return justificationForTCRepository.getApprovedSpcComTcJustification()
    }


    fun approveSacJustification(
        justificationForTC: JustificationForTC,
        companyStandardRemarks: CompanyStandardRemarks
    ) : String {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        justificationForTC.accentTo=justificationForTC.accentTo
        val decision=justificationForTC.accentTo

        val fName = loggedInUser.firstName
        val sName = loggedInUser.lastName
        val usersName = "$fName  $sName"
        companyStandardRemarks.requestId= companyStandardRemarks.requestId
        companyStandardRemarks.remarks= companyStandardRemarks.remarks
        companyStandardRemarks.status = 1.toString()
        companyStandardRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
        companyStandardRemarks.remarkBy = usersName
        companyStandardRemarks.role = "SPC"

        if (decision == "Yes") {
            justificationForTCRepository.findByIdOrNull(justificationForTC.id)?.let { justificationForTC ->
                with(justificationForTC) {
                    status = 4
                    tcNumber=getJCNumber()
                }
                justificationForTCRepository.save(justificationForTC)
                companyStandardRemarksRepository.save(companyStandardRemarks)
            }?: throw Exception("JUSTIFICATION NOT FOUND")

        } else if (decision == "No") {
            justificationForTCRepository.findByIdOrNull(justificationForTC.id)?.let { justificationForTC ->

                with(justificationForTC) {
                    status = 3
                }
                justificationForTCRepository.save(justificationForTC)
                companyStandardRemarksRepository.save(companyStandardRemarks)


            } ?: throw Exception("JUSTIFICATION NOT FOUND")

        }

        return "Actioned"
    }

    fun getApprovedComTcJustification(): MutableList<JustificationForTC> {
        return justificationForTCRepository.getApprovedComTcJustification()
    }



    fun getProducts(): MutableList<Product> {
        return productRepository.findAll()
    }

    fun getDepartments(): MutableList<Department> {
        return departmentRepository.findAll()
    }

    fun getProductCategories(id: String?): MutableList<ProductSubCategory> {
        return productSubCategoryRepository.findAll()
    }

    fun getUsers(): MutableList<UserHolder> {
        return userListRepository.getUsers()
    }


    fun uploadJCFile(
        uploads: ComJcJustificationUploads,
        docFile: MultipartFile,
        doc: String,
        user: UsersEntity,
        DocDescription: String
    ): ComJcJustificationUploads {

        with(uploads) {
//            filepath = docFile.path
            name = commonDaoServices.saveDocuments(docFile)
//            fileType = docFile.contentType
            fileType = commonDaoServices.getFileTypeByMimetypesFileTypeMap(docFile.name)
            documentType = doc
            description = DocDescription
            document = docFile.bytes
            transactionDate = commonDaoServices.getCurrentDate()
            status = 1
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }

        return comJcJustificationUploadsRepository.save(uploads)
    }


    //Upload Company Draft
    fun uploadDraft(comStdDraft: ComStdDraft): ComStdDraft {
        val variables: MutableMap<String, Any> = HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val cs = ComStdDraft()
        val uploadedDate= Timestamp(System.currentTimeMillis())

        cs.title = comStdDraft.title
//        comStdDraft.scope = comStdDraft.scope
//        comStdDraft.normativeReference = comStdDraft.normativeReference
//        comStdDraft.symbolsAbbreviatedTerms = comStdDraft.symbolsAbbreviatedTerms
//        comStdDraft.clause = comStdDraft.clause
//        comStdDraft.special = comStdDraft.special
        cs.companyName=comStdDraft.companyName
        cs.companyPhone=comStdDraft.companyPhone
        cs.uploadedBy = loggedInUser.id
        cs.requestNumber = comStdDraft.requestNumber
        cs.requestId = comStdDraft.requestId
        cs.uploadDate = uploadedDate
        cs.status = 0
        cs.createdBy = userListRepository.findNameById(loggedInUser.id)
        cs.draftNumber = getDRNumber()
        val deadline: Timestamp = Timestamp.valueOf(uploadedDate.toLocalDateTime().plusDays(7))
        cs.deadlineDate = deadline
        cs.departmentId = comStdDraft.departmentId
        cs.subject = comStdDraft.subject
        cs.description = comStdDraft.description
        cs.contactOneFullName = comStdDraft.contactOneFullName
        cs.contactOneTelephone = comStdDraft.contactOneTelephone
        cs.contactOneEmail = comStdDraft.contactOneEmail
        cs.contactTwoFullName = comStdDraft.contactTwoFullName
        cs.contactTwoTelephone = comStdDraft.contactTwoTelephone
        cs.contactTwoEmail = comStdDraft.contactTwoEmail
        cs.contactThreeFullName = comStdDraft.contactThreeFullName
        cs.contactThreeTelephone = comStdDraft.contactThreeTelephone
        cs.contactThreeEmail = comStdDraft.contactThreeEmail
        cs.standardType="Company Standard"

        val draftId=comStdDraftRepository.save(cs)
        val draftNo=draftId.id

        val committeeLists = comStdJointCommitteeRepository.getCommitteeList(comStdDraft.requestId)


//        val gson = Gson()
//        KotlinLogging.logger { }.info { "List of Draft" + gson.toJson(comStdDraft) }
//        KotlinLogging.logger { }.info { "Joint Committee" + gson.toJson(committeeLists) }

      //  val jointLists= mapKEBSOfficersNameListDto(committeeList)
        comStandardRequestRepository.findByIdOrNull(comStdDraft.requestId)?.let { companyStandardRequest ->
            with(companyStandardRequest) {
                status = 3

            }
            comStandardRequestRepository.save(companyStandardRequest)
        }?: throw Exception("REQUEST NOT FOUND")


        val targetUrl = "https://kimsint.kebs.org/comStdDraftComment/$draftNo";
        committeeLists.forEach { c ->
            val subject = "Company Standard Draft"
            val messageBody= "Hope You are Well,A Draft for a company standard has been uploaded. Click on the Link below to comment. $targetUrl  "
            if (c.getEmail() != null) {
                notifications.sendEmail(c.getEmail()!!, subject, messageBody)
            }
        }

        return draftId

    }

    // Upload nwa Standard Document
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

    fun getUploadedStdDraft(): MutableList<ComStdDraft> {
        return comStdDraftRepository.getUploadedStdDraft()
    }

    //View Company Draft
    fun findUploadedCDRFileBYId(comStdDraftID: Long): ComStandardDraftUploads {
        return comStandardDraftUploadsRepository.findAllById(comStdDraftID)
    }

    fun getDraftDocumentList(comStdDraftID: Long): List<SiteVisitListHolder> {
        return comStandardDraftUploadsRepository.findAllDocumentId(comStdDraftID)
    }


    fun getUploadedStdDraftForComment(comDraftID: Long): MutableList<ComStdDraft> {
        return comStdDraftRepository.getUploadedStdDraftForComment(comDraftID)
    }

    fun getAllComments(requestId: Long): MutableIterable<CompanyStandardRemarks>? {
        return companyStandardRemarksRepository.findCommentsOnDraft(requestId)
    }

    //Submit Adoption Proposal comments
    fun submitDraftComments(comDraft: List<ComDraftCommentDto>){
        val variables: MutableMap<String, Any> = HashMap()
        var  comDraftCommentsSaved = ComDraftComments();
        comDraft.forEach { com ->
            val  comDraftComments = ComDraftComments()
            comDraftComments.uploadDate = com.uploadDate
            comDraftComments.emailOfRespondent = com.emailOfRespondent
            comDraftComments.phoneOfRespondent = com.phoneOfRespondent
            comDraftComments.observation = com.observation
            comDraftComments.draftComment = com.draftComment
            comDraftComments.commentTitle = com.commentTitle
            comDraftComments.commentDocumentType = com.commentDocumentType
            comDraftComments.comClause = com.comClause
            comDraftComments.comParagraph = com.comParagraph
            comDraftComments.typeOfComment = com.typeOfComment
            comDraftComments.proposedChange = com.proposedChange
            comDraftComments.requestID = com.requestID
            comDraftComments.draftID = com.draftID
            comDraftComments.recommendations = com.recommendations
            comDraftComments.nameOfRespondent = com.nameOfRespondent
            comDraftComments.positionOfRespondent = com.positionOfRespondent
            comDraftComments.nameOfOrganization = com.nameOfOrganization
            comDraftComments.adoptStandard = com.adoptStandard
            comDraftComments.adoptDraft = com.adoptDraft
            comDraftComments.reason = com.reason
            comDraftComments.commentTime = Timestamp(System.currentTimeMillis())
            comStandardDraftCommentsRepository.save(comDraftComments)

            val commentNumber = comStdDraftRepository.getDraftCommentCount(com.draftID)

            comStdDraftRepository.findByIdOrNull(com.draftID)?.let { comStdDraft ->
                with(comStdDraft) {
                    commentCount = commentNumber + 1

                }
                comStdDraftRepository.save(comStdDraft)
            } ?: throw Exception("REQUEST NOT FOUND")
        }

        println("Comment Submitted")
    }

    fun getDraftComments(draftID: Long): MutableIterable<ComDraftComments>? {
        return comStandardDraftCommentsRepository.findByDraftIDOrderByIdDesc(draftID)
    }

    fun getDraftCommentList(draftID: Long): List<SiteVisitListHolder> {
        return comStandardDraftCommentsRepository.findAllCommentsId(draftID)
    }

    fun commentOnDraft(companyStandardRemarks: CompanyStandardRemarks):CompanyStandardRemarks{
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val fName = loggedInUser.firstName
        val sName = loggedInUser.lastName
        val usersName = "$fName  $sName"
        companyStandardRemarks.requestId= companyStandardRemarks.requestId
        companyStandardRemarks.remarks= companyStandardRemarks.remarks
        //companyStandardRemarks.status = 1.toString()
        companyStandardRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
        companyStandardRemarks.remarkBy = usersName
        companyStandardRemarks.role = "Joint Committee"
        companyStandardRemarks.standardType = "Company Standard"


        return companyStandardRemarksRepository.save(companyStandardRemarks)

    }


    fun decisionOnStdDraft(
        comStdDraft: ComStdDraft,
        companyStandardRemarks: CompanyStandardRemarks
    ) : ResponseMsg {
        var response=""
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        comStdDraft.accentTo=comStdDraft.accentTo
        val decision=comStdDraft.accentTo
        val commentNumber=comStdDraftRepository.getDraftCommentCount(comStdDraft.id)
       // val countNo=commentNumber.toString()
        val fName = loggedInUser.firstName
        val sName = loggedInUser.lastName
        val usersName = "$fName  $sName"
        companyStandardRemarks.requestId= comStdDraft.id
        companyStandardRemarks.remarks= companyStandardRemarks.remarks
        companyStandardRemarks.status = 1.toString()
        companyStandardRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
        companyStandardRemarks.remarkBy = usersName
        companyStandardRemarks.role = "Joint Committee"
        companyStandardRemarks.standardType = "Company Standard"
        val deadline: Timestamp = Timestamp.valueOf(companyStandardRemarks.dateOfRemark!!.toLocalDateTime().plusMonths(5))

        if (commentNumber>0){
            if (decision == "Yes") {
                comStdDraftRepository.findByIdOrNull(comStdDraft.id)?.let { comStdDraft ->
                    with(comStdDraft) {
                        status = 1
                        title=comStdDraft.title
                        scope=comStdDraft.scope
                        normativeReference=comStdDraft.normativeReference
                        symbolsAbbreviatedTerms=comStdDraft.symbolsAbbreviatedTerms
                        clause=comStdDraft.clause
                        special=comStdDraft.special
                        requestNumber=comStdDraft.requestNumber
                        deadlineDate=deadline
                    }
                    comStdDraftRepository.save(comStdDraft)
                    companyStandardRemarksRepository.save(companyStandardRemarks)
                    response="Draft Approved"
                }?: throw Exception("DRAFT NOT FOUND")

                val recipient=comStdDraft.contactOneEmail
                val contactName=comStdDraft.contactOneFullName
                val companyName=comStdDraft.companyName
                val companyPhone= comStdDraft.companyPhone
                val contactTel=comStdDraft.contactOneTelephone
                val draftId=comStdDraft.id
                val targetUrl = "https://kimsint.kebs.org/comStdApproved/$draftId";
                val subject = "Company Standard Draft"
                val messageBody= "Dear $contactName, Hope You are Well,A Draft for a company standard for $companyName has been uploaded. Click on the Link below to make Decision. $targetUrl  "

                if (recipient != null) {
                    notifications.sendEmail(recipient, subject, messageBody)
                }


            } else if (decision == "No") {
                comStdDraftRepository.findByIdOrNull(comStdDraft.id)?.let { comStdDraft ->

                    with(comStdDraft) {
                        status = 2
                    }
                    comStdDraftRepository.save(comStdDraft)
                    companyStandardRemarksRepository.save(companyStandardRemarks)

                    response="Draft Not Approved"
                } ?: throw Exception("DRAFT NOT FOUND")

                comStandardRequestRepository.findByIdOrNull(comStdDraft.requestId)?.let { companyStandardRequest ->
                    with(companyStandardRequest) {
                        status = 2

                    }
                    comStandardRequestRepository.save(companyStandardRequest)
                }?: throw Exception("REQUEST NOT FOUND")


            }

        }else{
            response="A Decision cannot be made on the Draft since none of the Joint Committee Members has commented on it."
        }

        return ResponseMsg(response)
    }



    fun getApprovedStdDraft(comDraftID: Long): MutableList<ComStdDraft> {
        return comStdDraftRepository.getApprovedStdDraft(comDraftID)
    }

    fun decisionOnComStdDraft(
        comStdDraft: ComStdDraft,
        companyStandardRemarks: CompanyStandardRemarks
    ) : String {
        //val loggedInUser = commonDaoServices.loggedInUserDetails()
        comStdDraft.accentTo=comStdDraft.accentTo
        val decision=comStdDraft.accentTo

        val fName = "Company"
        val sName = ""
        val usersName = "$fName  $sName"
        companyStandardRemarks.requestId= comStdDraft.id
        companyStandardRemarks.remarks= companyStandardRemarks.remarks
        companyStandardRemarks.status = 1.toString()
        companyStandardRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
        companyStandardRemarks.remarkBy = usersName
        companyStandardRemarks.role = "Company"
        companyStandardRemarks.standardType = "Company Standard"
        val deadline: Timestamp = Timestamp.valueOf(companyStandardRemarks.dateOfRemark!!.toLocalDateTime().plusMonths(2))
        val comStandard= getCSNumber()

        if (decision == "Yes") {
            comStdDraftRepository.findByIdOrNull(comStdDraft.id)?.let { comStdDraft ->
                with(comStdDraft) {
                    status = 4
                    comStdNumber=comStandard
                    deadlineDate=deadline
                }
               val cs=CompanyStandard()
                cs.companyName=comStdDraft.companyName
                cs.companyPhone=comStdDraft.companyPhone
                cs.contactOneFullName=comStdDraft.contactOneFullName
                cs.contactOneEmail=comStdDraft.contactOneEmail
                cs.contactOneTelephone=comStdDraft.contactOneTelephone
                cs.departmentId=comStdDraft.departmentId
                cs.subject=comStdDraft.subject
                cs.description=comStdDraft.description
                cs.contactTwoFullName=comStdDraft.contactTwoFullName
                cs.contactTwoTelephone=comStdDraft.contactTwoTelephone
                cs.contactTwoEmail=comStdDraft.contactTwoEmail
                cs.contactThreeFullName=comStdDraft.contactThreeFullName
                cs.contactThreeTelephone=comStdDraft.contactThreeTelephone
                cs.contactThreeEmail=comStdDraft.contactThreeEmail
                cs.requestNumber=comStdDraft.requestNumber
                cs.status=0
                cs.uploadDate = Timestamp(System.currentTimeMillis())
                cs.comStdNumber = comStandard
                cs.draftId=comStdDraft.id
                cs.requestId=comStdDraft.requestId
                cs.standardType="Company Standard"

                val draftStandard= companyStandardRepository.save(cs)

                comStdDraftRepository.save(comStdDraft)
                companyStandardRemarksRepository.save(companyStandardRemarks)

                var userList= companyStandardRepository.getHopEmailList()

                //email to Head of publishing
                val targetUrl = "https://kimsint.kebs.org/hopTasks";
                userList.forEach { item->
                    //val recipient="stephenmuganda@gmail.com"
                    val recipient= item.getUserEmail()
                    val subject = "Standard"
                    val messageBody= "Dear ${item.getFirstName()} ${item.getLastName()}, A standard has been uploaded.Login to KIEMS $targetUrl to initiate piblishing"
                    if (recipient != null) {
                        notifications.sendEmail(recipient, subject, messageBody)
                    }
                }

            }?: throw Exception("DRAFT NOT FOUND")

        } else if (decision == "No") {
            comStdDraftRepository.findByIdOrNull(comStdDraft.id)?.let { comStdDraft ->

                with(comStdDraft) {
                    status = 1
                }
                comStdDraftRepository.save(comStdDraft)
                companyStandardRemarksRepository.save(companyStandardRemarks)


            } ?: throw Exception("DRAFT NOT FOUND")


        }

        return "Actioned"
    }

    fun findUploadedFileBYId(comDraftDocumentId: Long): ComStandardDraftUploads {
        comStandardDraftUploadsRepository.findByComDraftDocumentId(comDraftDocumentId)?.let {
            return it
        }
    }

    fun viewCompanyDraft(comStdDraftID: Long): ComStandardDraftUploads {
        comStandardDraftUploadsRepository.findByComDraftDocumentId(comStdDraftID)?.let {
            return it
        }
    }

    fun getStdDraftForEditing(): MutableList<ComStandard> {
        return companyStandardRepository.getComStdForEditing()
    }



    fun submitDraftForEditing(companyStandard: CompanyStandard) : CompanyStandard
    {
        val variable: MutableMap<String, Any> = HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        companyStandardRepository.findByIdOrNull(companyStandard.id)?.let { companyStandard ->
            with(companyStandard) {
                title=companyStandard.title
                departmentId = companyStandard.departmentId
                subject=companyStandard.subject
                description=companyStandard.description
                contactOneFullName=companyStandard.contactOneFullName
                contactOneTelephone=companyStandard.contactOneTelephone
                contactOneEmail=companyStandard.contactOneEmail
                contactTwoFullName=companyStandard.contactTwoFullName
                contactTwoTelephone=companyStandard.contactTwoTelephone
                contactTwoEmail=companyStandard.contactTwoEmail
                contactThreeFullName=companyStandard.contactThreeFullName
                contactThreeTelephone=companyStandard.contactThreeTelephone
                contactThreeEmail=companyStandard.contactThreeEmail
                requestNumber=companyStandard.requestNumber
                status=companyStandard.status
                comStdNumber = companyStandard.comStdNumber
                documentType=companyStandard.documentType
                draftId=companyStandard.draftId
                requestId=companyStandard.requestId
                standardType="Company Standard"

            }

        }?: throw Exception("DRAFT NOT FOUND")

        val draftStandard= companyStandardRepository.save(companyStandard)

        var userList= companyStandardRepository.getHopEmailList()

        //email to Head of publishing
        val targetUrl = "https://kimsint.kebs.org/";
        userList.forEach { item->
            //val recipient="stephenmuganda@gmail.com"
            val recipient= item.getUserEmail()
            val subject = "Standard"
            val messageBody= "Dear ${item.getFirstName()} ${item.getLastName()}, A standard has been uploaded."
            if (recipient != null) {
                notifications.sendEmail(recipient, subject, messageBody)
            }
        }


        return draftStandard
    }

    fun getUploadedDraft(): MutableList<COMUploadedDraft> {
        return companyStandardRepository.getUploadedDraft()
    }

    fun getComStdPublishing(): MutableList<ComStandard> {
        return companyStandardRepository.getComStdPublishing()
    }




    fun checkRequirements(
        companyStandard: CompanyStandard,
        companyStandardRemarks: CompanyStandardRemarks
    ) : String {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        companyStandard.accentTo=companyStandard.accentTo
        val decision=companyStandard.accentTo

        val fName = loggedInUser.firstName
        val sName = loggedInUser.lastName
        val usersName = "$fName  $sName"
        companyStandardRemarks.requestId= companyStandard.draftId
        companyStandardRemarks.remarks= companyStandardRemarks.remarks
        companyStandardRemarks.status = 1.toString()
        companyStandardRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
        companyStandardRemarks.remarkBy = usersName
        companyStandardRemarks.role = "HOP"
        if (decision == "Yes") {
            companyStandardRepository.findByIdOrNull(companyStandard.id)?.let { companyStandard ->
                with(companyStandard) {
                    status = 3

                }
                companyStandardRepository.save(companyStandard)
                companyStandardRemarksRepository.save(companyStandardRemarks)
            }?: throw Exception("DRAFT NOT FOUND")

        } else if (decision == "No") {

            companyStandardRepository.findByIdOrNull(companyStandard.id)?.let { companyStandard ->

                with(companyStandard) {
                    status = 0
                }
                companyStandardRepository.save(companyStandard)
                companyStandardRemarksRepository.save(companyStandardRemarks)

            } ?: throw Exception("DRAFT NOT FOUND")


        }

        return "Actioned"
    }
    fun getApprovedEditedDraft(): MutableList<COMUploadedDraft> {
        return companyStandardRepository.getApprovedEditedDraft()
    }

    fun editStandardDraft(companyStandard: CompanyStandard): CompanyStandard {
        val variable: MutableMap<String, Any> = HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        //val draft= CompanyStandard()

        val draught = companyStandard.status
        val gson = Gson()

        companyStandardRepository.findByIdOrNull(companyStandard.id)?.let { companyStandard ->

            with(companyStandard) {
                draughting = companyStandard.draughting
                title = companyStandard.title
                departmentId = companyStandard.departmentId
                subject = companyStandard.subject
                description = companyStandard.description
                contactOneFullName = companyStandard.contactOneFullName
                contactOneTelephone = companyStandard.contactOneTelephone
                contactOneEmail = companyStandard.contactOneEmail
                requestNumber = companyStandard.requestNumber
                status = companyStandard.status
                comStdNumber = companyStandard.comStdNumber
                documentType = companyStandard.documentType
                draftId = companyStandard.draftId
                requestId = companyStandard.requestId
                standardType=companyStandard.standardType
            }

            //KotlinLogging.logger { }.info { "Status Check" + gson.toJson(companyStandard) }
        } ?: throw Exception("DRAFT NOT FOUND")

        return companyStandardRepository.save(companyStandard)
    }

    fun getComEditedDraft(): MutableList<COMUploadedDraft> {
        return companyStandardRepository.getComEditedDraft()
    }

    fun draughtStandard(companyStandard: CompanyStandard) : CompanyStandard {
        val variable: MutableMap<String, Any> = HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()


        companyStandardRepository.findByIdOrNull(companyStandard.id)?.let { companyStandard ->

            with(companyStandard) {
                draughting = companyStandard.draughting
                title = companyStandard.title
                departmentId = companyStandard.departmentId
                subject = companyStandard.subject
                description = companyStandard.description
                contactOneFullName = companyStandard.contactOneFullName
                contactOneTelephone = companyStandard.contactOneTelephone
                contactOneEmail = companyStandard.contactOneEmail
                contactTwoFullName = companyStandard.contactTwoFullName
                contactTwoTelephone = companyStandard.contactTwoTelephone
                contactTwoEmail = companyStandard.contactTwoEmail
                contactThreeFullName = companyStandard.contactThreeFullName
                contactThreeTelephone = companyStandard.contactThreeTelephone
                contactThreeEmail = companyStandard.contactThreeEmail
                requestNumber = companyStandard.requestNumber
                status = companyStandard.status
                comStdNumber = companyStandard.comStdNumber
                documentType = companyStandard.documentType
                draftId = companyStandard.draftId
                requestId = companyStandard.requestId
            }


        } ?: throw Exception("DRAFT NOT FOUND")

        return companyStandardRepository.save(companyStandard)
    }

    fun getDraughtedDraft(): MutableList<COMUploadedDraft> {
        return companyStandardRepository.getDraughtedDraft()
    }



    fun proofReadStandard(companyStandard: CompanyStandard) : CompanyStandard {
        val variable: MutableMap<String, Any> = HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val draft= CompanyStandard()

        companyStandardRepository.findByIdOrNull(companyStandard.id)?.let { companyStandard ->

            with(companyStandard) {
                draughting = companyStandard.draughting
                title = companyStandard.title
                departmentId = companyStandard.departmentId
                subject = companyStandard.subject
                description = companyStandard.description
                contactOneFullName = companyStandard.contactOneFullName
                contactOneTelephone = companyStandard.contactOneTelephone
                contactOneEmail = companyStandard.contactOneEmail
                contactTwoFullName = companyStandard.contactTwoFullName
                contactTwoTelephone = companyStandard.contactTwoTelephone
                contactTwoEmail = companyStandard.contactTwoEmail
                contactThreeFullName = companyStandard.contactThreeFullName
                contactThreeTelephone = companyStandard.contactThreeTelephone
                contactThreeEmail = companyStandard.contactThreeEmail
                requestNumber = companyStandard.requestNumber
                status = companyStandard.status
                comStdNumber = companyStandard.comStdNumber
                documentType = companyStandard.documentType
                draftId = companyStandard.draftId
                requestId = companyStandard.requestId
            }


        } ?: throw Exception("DRAFT NOT FOUND")

        return companyStandardRepository.save(companyStandard)
    }

    fun getProofReadDraft(): MutableList<COMUploadedDraft> {
        return companyStandardRepository.getProofReadDraft()
    }

    fun approveProofReadStandard(
        companyStandard: CompanyStandard,
        companyStandardRemarks: CompanyStandardRemarks
    ) : String {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        companyStandard.accentTo=companyStandard.accentTo
        val decision=companyStandard.accentTo

        val fName = loggedInUser.firstName
        val sName = loggedInUser.lastName
        val usersName = "$fName  $sName"
        companyStandardRemarks.requestId= companyStandard.draftId
        companyStandardRemarks.remarks= companyStandardRemarks.remarks
        companyStandardRemarks.status = 1.toString()
        companyStandardRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
        companyStandardRemarks.remarkBy = usersName
        companyStandardRemarks.role = "HOP"

        companyStandardRepository.findByIdOrNull(companyStandard.id)?.let { companyStandard ->
            with(companyStandard) {
                status = 7

            }
            companyStandardRepository.save(companyStandard)
            companyStandardRemarksRepository.save(companyStandardRemarks)
        }?: throw Exception("DRAFT NOT FOUND")



        return "Actioned"
    }

    fun getApprovedProofReadDraft(): MutableList<COMUploadedDraft> {
        return companyStandardRepository.getApprovedProofReadDraft()
    }

    fun approveEditedStandard(
        companyStandard: CompanyStandard,
        companyStandardRemarks: CompanyStandardRemarks
    ) : String {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        companyStandard.accentTo=companyStandard.accentTo
        val decision=companyStandard.accentTo

        val fName = loggedInUser.firstName
        val sName = loggedInUser.lastName
        val usersName = "$fName  $sName"
        companyStandardRemarks.requestId= companyStandard.draftId
        companyStandardRemarks.remarks= companyStandardRemarks.remarks
        companyStandardRemarks.status = 1.toString()
        companyStandardRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
        companyStandardRemarks.remarkBy = usersName
        companyStandardRemarks.role = "HOP"
        if (decision == "Yes") {
            companyStandardRepository.findByIdOrNull(companyStandard.id)?.let { companyStandard ->
                with(companyStandard) {
                    status = 8

                }
                companyStandardRepository.save(companyStandard)
                companyStandardRemarksRepository.save(companyStandardRemarks)
            }?: throw Exception("DRAFT NOT FOUND")

        } else if (decision == "No") {

            companyStandardRepository.findByIdOrNull(companyStandard.id)?.let { companyStandard ->

                with(companyStandard) {
                    status = 3
                }
                companyStandardRepository.save(companyStandard)
                companyStandardRemarksRepository.save(companyStandardRemarks)

            } ?: throw Exception("DRAFT NOT FOUND")

        }

        return "Actioned"
    }

    fun getApprovedCompanyStdDraft(): MutableList<COMUploadedDraft> {
        return companyStandardRepository.getApprovedCompanyStdDraft()
    }

    fun getAppStdPublishing(): MutableList<ComStandard> {
        return companyStandardRepository.getAppStdPublishing()
    }

    fun getAppStd(): MutableList<ComStandard> {
        return companyStandardRepository.getAppStd()
    }

    fun rejectCompanyStandard(
        companyStandard: CompanyStandard,
        companyStandardRemarks: CompanyStandardRemarks,
    ): String{
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val fName = loggedInUser.firstName
        val sName = loggedInUser.lastName
        val usersName = "$fName  $sName"
        companyStandard.id=companyStandard.id

        companyStandardRemarks.requestId= companyStandard.draftId
        companyStandardRemarks.remarks= companyStandardRemarks.remarks
        companyStandardRemarks.status = 1.toString()
        companyStandardRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
        companyStandardRemarks.remarkBy = usersName
        companyStandardRemarks.role = "Standard Editor"

        companyStandardRepository.findByIdOrNull(companyStandard.id)?.let { companyStandard ->

            with(companyStandard) {
                status = 3
            }
            companyStandardRepository.save(companyStandard)
            companyStandardRemarksRepository.save(companyStandardRemarks)

        } ?: throw Exception("DRAFT NOT FOUND")

        return "Standard Rejected"
    }

    fun approveCompanyStandard(
        companyStandard: CompanyStandard,
        companyStandardRemarks: CompanyStandardRemarks,
        standard: Standard
    ) : Standard {
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        standard.title=standard.title
        standard.standardNumber=standard.standardNumber
        standard.status=0
        standard.dateFormed=Timestamp(System.currentTimeMillis())
        standard.comStdId=standard.comStdId


        val fName = loggedInUser.firstName
        val sName = loggedInUser.lastName
        val usersName = "$fName  $sName"
        companyStandardRemarks.requestId= companyStandard.draftId
        companyStandardRemarks.remarks= companyStandardRemarks.remarks
        companyStandardRemarks.status = 1.toString()
        companyStandardRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
        companyStandardRemarks.remarkBy = usersName
        companyStandardRemarks.role = "Standard Editor"

            companyStandardRepository.findByIdOrNull(companyStandard.id)?.let { companyStandard ->
                with(companyStandard) {
                    status = 9

                }
                companyStandardRepository.save(companyStandard)
                standardRepository.save(standard)
                companyStandardRemarksRepository.save(companyStandardRemarks)
                var userList= companyStandardRepository.getSacSecEmailList()
                val targetUrl = "https://kimsint.kebs.org/";
                userList.forEach { item->
                    //val recipient="stephenmuganda@gmail.com"
                    val recipient= item.getUserEmail()
                    val subject = "New Company Standard"+ standard.standardNumber
                    val messageBody= "Dear ${item.getFirstName()} ${item.getLastName()},A New standard has been approved and uploaded.Click on the Link below to view. ${targetUrl} "
                    if (recipient != null) {
                        notifications.sendEmail(recipient, subject, messageBody)
                    }
                }

            }?: throw Exception("DRAFT NOT FOUND")

        return standardRepository.save(standard)
    }

    // Upload Standard Document
    fun uploadCompanyStandardUpload(
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

        return sdDocumentsRepository.save(uploads)
    }






    // Upload nwa Standard Document
    fun uploadSTDFile(
        uploads: ComStandardUploads,
        docFile: MultipartFile,
        doc: String,
        user: UsersEntity,
        DocDescription: String
    ): ComStandardUploads {

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

        return comStandardUploadsRepository.save(uploads)
    }


    // View STD Document upload
    fun findUploadedSTDFileBYId(comStandardID: Long): ComStandardUploads {
        return comStandardUploadsRepository.findByComStdDocumentId(comStandardID)
            ?: throw ExpectedDataNotFound("No File found with the following [ id=$comStandardID]")
    }

    fun editSTDFile(
        uploads: ComStandardUploads,
        docFile: MultipartFile,
        doc: String,
        user: UsersEntity,
        DocDescription: String
    ): ComStandardUploads {
        comStandardUploadsRepository.findByIdOrNull(uploads.comStdDocumentId)?.let { uploads ->
            with(uploads) {
//            filepath = docFile.path
                name = commonDaoServices.saveDocuments(docFile)
//            fileType = docFile.contentType
                fileType = commonDaoServices.getFileTypeByMimetypesFileTypeMap(docFile.name)
                documentType = doc
                description = DocDescription
                document = docFile.bytes
                transactionDate = commonDaoServices.getCurrentDate()
                status = 1
                createdBy = commonDaoServices.concatenateName(user)
                createdOn = commonDaoServices.getTimestamp()
            }

            return comStandardUploadsRepository.save(uploads)
        } ?: throw Exception("STANDARD NOT FOUND")
    }


    //Get Process History
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

    fun getDRNumber(): String {
        var allRequests = comStdDraftRepository.getMaxDraftId()

        var lastId: String? = "0"
//        var finalValue = 1
        var startId = "DRAFT"

        //allRequests = allRequests+1

        val c = allRequests
        val d = c.toInt()
        val x = 1
        val z = x  + d

        val finalValue = z.toString()

//        println("Sum of x+y = $finalValue")

        val year = Calendar.getInstance()[Calendar.YEAR]
        val month = Calendar.getInstance().get(Calendar.MONTH)+1

        return "$startId/$finalValue/$month:$year"


    }

    fun getCSNumber(): String {
        var allRequests = companyStandardRepository.getMaxComStdId()

        var lastId: String? = "0"
//        var finalValue = 1
        var startId = "CS"

        //allRequests = allRequests+1

        val c = allRequests
        val d = c.toInt()
        val x = 1
        val z = x  + d

        val finalValue = z.toString()

//        println("Sum of x+y = $finalValue")

        val year = Calendar.getInstance()[Calendar.YEAR]
        val month = Calendar.getInstance().get(Calendar.MONTH)+1

        return "$startId/$finalValue/$month:$year"

    }

    fun getRQNumber(): String {
        val allRequests = comStandardRequestRepository.findAllByOrderByIdDesc()

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

        return "$startId/$finalValue:$year";
    }

    fun getJCNumber(): String {
        val allRequests = justificationForTCRepository.findAllByOrderByIdDesc()

        var lastId: String? = "0"
        var finalValue = 1
        var startId = "JC"


        for (item in allRequests) {
            println(item)
            lastId = item.tcNumber
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


}
