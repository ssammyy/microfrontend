package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.dto.std.NamesList
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
    private val comStdJointCommitteeRepository: ComStdJointCommitteeRepository
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
                comStandardJointCommittee.name="JC"
                comStandardJointCommittee.email=recipient
                comStandardJointCommittee.requestId=comStandardJointCommittee.requestId
                comStdJointCommitteeRepository.save(comStandardJointCommittee)

                val subject = "New Standard Request"
                val messageBody= "Hope You are Well,A new standard request has been received.You have been selected to be in the Joint Committee. "
                notifications.sendEmail(recipient, subject, messageBody)

            }

        }

        detailBody?.forEach { d ->
            val subject = "New Standard Request"
            val recipient = d.email
            val user = d.name

            comStandardJointCommittee.name=user
            comStandardJointCommittee.email=recipient
            comStandardJointCommittee.requestId=comStandardJointCommittee.requestId
            comStdJointCommitteeRepository.save(comStandardJointCommittee)

                val messageBody= "Dear $user ,A new standard request has been received.You have been selected to be in the Joint Committee. "
            if (recipient != null) {
                notifications.sendEmail(recipient, subject, messageBody)
            }
        }

        //val gson = Gson()
       // KotlinLogging.logger { }.info { "JOINT COMMITTEE" + gson.toJson(detailBody) }

        comStandardRequestRepository.findByIdOrNull(comStandardJointCommittee.requestId)?.let { companyStandardRequest ->
            with(companyStandardRequest) {
                status = 2

            }
            result=   comStandardRequestRepository.save(companyStandardRequest)
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
        comStdDraft.title = comStdDraft.title
//        comStdDraft.scope = comStdDraft.scope
//        comStdDraft.normativeReference = comStdDraft.normativeReference
//        comStdDraft.symbolsAbbreviatedTerms = comStdDraft.symbolsAbbreviatedTerms
//        comStdDraft.clause = comStdDraft.clause
//        comStdDraft.special = comStdDraft.special
        comStdDraft.uploadedBy = loggedInUser.id
        comStdDraft.requestNumber = comStdDraft.requestNumber
        comStdDraft.requestId = comStdDraft.requestId
        comStdDraft.uploadDate = Timestamp(System.currentTimeMillis())
        comStdDraft.status=0
        comStdDraft.createdBy = userListRepository.findNameById(loggedInUser.id)
        comStdDraft.draftNumber = getDRNumber()
        val deadline: Timestamp = Timestamp.valueOf(comStdDraft.uploadDate!!.toLocalDateTime().plusDays(7))
        comStdDraft.deadlineDate=deadline
        val committeeLists=comStdJointCommitteeRepository.getCommitteeList(comStdDraft.requestId)

        comStdDraft.departmentId = comStdDraft.departmentId
        comStdDraft.subject=comStdDraft.subject
        comStdDraft.description=comStdDraft.description
        comStdDraft.contactOneFullName=comStdDraft.contactOneFullName
        comStdDraft.contactOneTelephone=comStdDraft.contactOneTelephone
        comStdDraft.contactOneEmail=comStdDraft.contactOneEmail
        comStdDraft.contactTwoFullName=comStdDraft.contactTwoFullName
        comStdDraft.contactTwoTelephone=comStdDraft.contactTwoTelephone
        comStdDraft.contactTwoEmail=comStdDraft.contactTwoEmail
        comStdDraft.contactThreeFullName=comStdDraft.contactThreeFullName
        comStdDraft.contactThreeTelephone=comStdDraft.contactThreeTelephone
        comStdDraft.contactThreeEmail=comStdDraft.contactThreeEmail

        //val jointLists= mapKEBSOfficersNameListDto(committeeList)
        committeeLists.forEach { c ->
            val subject = "Company Standard Draft"
            val messageBody= "Hope You are Well,A Draft for a company standard has been uploaded. "
            if (c.getEmail() != null) {
                notifications.sendEmail(c.getEmail()!!, subject, messageBody)
            }
        }
//        if (jointLists != null) {
//            for (recipient in jointLists) {
//                val subject = "Company Standard Draft"
//                val messageBody= "Hope You are Well,A Draft for a company standard has been uploaded. "
//                notifications.sendEmail(recipient, subject, messageBody)
//
//            }
//
//        }

        comStandardRequestRepository.findByIdOrNull(comStdDraft.requestId)?.let { companyStandardRequest ->
            with(companyStandardRequest) {
                status = 3

            }
            comStandardRequestRepository.save(companyStandardRequest)
        }?: throw Exception("REQUEST NOT FOUND")

        return comStdDraftRepository.save(comStdDraft)


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

    fun getUploadedStdDraftForComment(): MutableList<ComStdDraft> {
        return comStdDraftRepository.getUploadedStdDraftForComment()
    }

    fun getAllComments(requestId: Long): MutableIterable<CompanyStandardRemarks>? {
        return companyStandardRemarksRepository.findByRequestId(requestId)
    }


    fun commentOnDraft(companyStandardRemarks: CompanyStandardRemarks):CompanyStandardRemarks{
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val fName = loggedInUser.firstName
        val sName = loggedInUser.lastName
        val usersName = "$fName  $sName"
        companyStandardRemarks.requestId= companyStandardRemarks.requestId
        companyStandardRemarks.remarks= companyStandardRemarks.remarks
        companyStandardRemarks.status = 1.toString()
        companyStandardRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
        companyStandardRemarks.remarkBy = usersName
        companyStandardRemarks.role = "Joint Committee"


        return companyStandardRemarksRepository.save(companyStandardRemarks)

    }


    fun decisionOnStdDraft(
        comStdDraft: ComStdDraft,
        companyStandardRemarks: CompanyStandardRemarks
    ) : String {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        comStdDraft.accentTo=comStdDraft.accentTo
        val decision=comStdDraft.accentTo

        val fName = loggedInUser.firstName
        val sName = loggedInUser.lastName
        val usersName = "$fName  $sName"
        companyStandardRemarks.requestId= companyStandardRemarks.requestId
        companyStandardRemarks.remarks= companyStandardRemarks.remarks
        companyStandardRemarks.status = 1.toString()
        companyStandardRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
        companyStandardRemarks.remarkBy = usersName
        companyStandardRemarks.role = "Joint Committee"
        val deadline: Timestamp = Timestamp.valueOf(companyStandardRemarks.dateOfRemark!!.toLocalDateTime().plusMonths(5))


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
            }?: throw Exception("DRAFT NOT FOUND")

        } else if (decision == "No") {
            comStdDraftRepository.findByIdOrNull(comStdDraft.id)?.let { comStdDraft ->

                with(comStdDraft) {
                    status = 2
                }
                comStdDraftRepository.save(comStdDraft)
                companyStandardRemarksRepository.save(companyStandardRemarks)


            } ?: throw Exception("DRAFT NOT FOUND")

            comStandardRequestRepository.findByIdOrNull(comStdDraft.requestId)?.let { companyStandardRequest ->
                with(companyStandardRequest) {
                    status = 2

                }
                comStandardRequestRepository.save(companyStandardRequest)
            }?: throw Exception("REQUEST NOT FOUND")


        }

        return "Actioned"
    }



    fun getApprovedStdDraft(): MutableList<ComStdDraft> {
        return comStdDraftRepository.getApprovedStdDraft()
    }

    fun decisionOnComStdDraft(
        comStdDraft: ComStdDraft,
        companyStandardRemarks: CompanyStandardRemarks
    ) : String {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        comStdDraft.accentTo=comStdDraft.accentTo
        val decision=comStdDraft.accentTo

        val fName = loggedInUser.firstName
        val sName = loggedInUser.lastName
        val usersName = "$fName  $sName"
        companyStandardRemarks.requestId= companyStandardRemarks.requestId
        companyStandardRemarks.remarks= companyStandardRemarks.remarks
        companyStandardRemarks.status = 1.toString()
        companyStandardRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
        companyStandardRemarks.remarkBy = usersName
        companyStandardRemarks.role = "Company"
        val deadline: Timestamp = Timestamp.valueOf(companyStandardRemarks.dateOfRemark!!.toLocalDateTime().plusMonths(2))


        if (decision == "Yes") {
            comStdDraftRepository.findByIdOrNull(comStdDraft.id)?.let { comStdDraft ->
                with(comStdDraft) {
                    status = 3
                    title=comStdDraft.title
                    scope=comStdDraft.scope
                    normativeReference=comStdDraft.normativeReference
                    symbolsAbbreviatedTerms=comStdDraft.symbolsAbbreviatedTerms
                    clause=comStdDraft.clause
                    special=comStdDraft.special
                    requestNumber=comStdDraft.requestNumber
                    comStdNumber=getCSNumber()
                    deadlineDate=deadline
                }
                comStdDraftRepository.save(comStdDraft)
                companyStandardRemarksRepository.save(companyStandardRemarks)
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
        val month = Calendar.getInstance()[Calendar.MONTH]

        return "$startId/$finalValue/$month:$year"


    }




    fun findUploadedFileBYId(comDraftDocumentId: Long): ComStandardDraftUploads {
        comStandardDraftUploadsRepository.findByComDraftDocumentId(comDraftDocumentId)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No File found with the following [ id=$comDraftDocumentId]")
    }



    fun getStdDraftForEditing(): MutableList<ComStdDraft> {
        return comStdDraftRepository.getStdDraftForEditing()
    }





    fun submitDraftForEditing(companyStandard: CompanyStandard,comStdDraft:ComStdDraft) : CompanyStandard
    {
        val variable: MutableMap<String, Any> = HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        companyStandard.title=companyStandard.title
        companyStandard.departmentId = companyStandard.departmentId
        companyStandard.subject=companyStandard.subject
        companyStandard.description=companyStandard.description
        companyStandard.contactOneFullName=companyStandard.contactOneFullName
        companyStandard.contactOneTelephone=companyStandard.contactOneTelephone
        companyStandard.contactOneEmail=companyStandard.contactOneEmail
        companyStandard.contactTwoFullName=companyStandard.contactTwoFullName
        companyStandard.contactTwoTelephone=companyStandard.contactTwoTelephone
        companyStandard.contactTwoEmail=companyStandard.contactTwoEmail
        companyStandard.contactThreeFullName=companyStandard.contactThreeFullName
        companyStandard.contactThreeTelephone=companyStandard.contactThreeTelephone
        companyStandard.contactThreeEmail=companyStandard.contactThreeEmail
        companyStandard.requestNumber=companyStandard.requestNumber
        companyStandard.status=0
        companyStandard.uploadDate = Timestamp(System.currentTimeMillis())
        companyStandard.comStdNumber = companyStandard.comStdNumber
        companyStandard.preparedBy=companyStandard.preparedBy
        companyStandard.documentType=companyStandard.documentType
        companyStandard.draftId=comStdDraft.id
        companyStandard.requestId=comStdDraft.requestId


        comStdDraftRepository.findByIdOrNull(comStdDraft.id)?.let { comStdDraft ->

            with(comStdDraft) {
                status = 4
            }
            comStdDraftRepository.save(comStdDraft)


        } ?: throw Exception("DRAFT NOT FOUND")
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

    fun getComStdPublishing(): MutableList<COMUploadedDraft> {
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
        companyStandardRemarks.requestId= companyStandardRemarks.requestId
        companyStandardRemarks.remarks= companyStandardRemarks.remarks
        companyStandardRemarks.status = 1.toString()
        companyStandardRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
        companyStandardRemarks.remarkBy = usersName
        companyStandardRemarks.role = "HOP"

        if (decision == "Yes") {
            companyStandardRepository.findByIdOrNull(companyStandard.id)?.let { companyStandard ->
                with(companyStandard) {
                    status = 1

                }
                companyStandardRepository.save(companyStandard)
                companyStandardRemarksRepository.save(companyStandardRemarks)
            }?: throw Exception("DRAFT NOT FOUND")

        } else if (decision == "No") {
            comStdDraftRepository.findByIdOrNull(companyStandard.draftId)?.let { comStdDraft ->

                with(comStdDraft) {
                    status = 1
                }
                comStdDraftRepository.save(comStdDraft)

                companyStandardRepository.findByIdOrNull(companyStandard.id)?.let { companyStandard ->

                    with(companyStandard) {
                        status = 2
                    }
                    companyStandardRepository.save(companyStandard)
                    companyStandardRemarksRepository.save(companyStandardRemarks)

                } ?: throw Exception("DRAFT NOT FOUND")

            } ?: throw Exception("DRAFT NOT FOUND")


        }

        return "Actioned"
    }
    fun getApprovedEditedDraft(): MutableList<COMUploadedDraft> {
        return companyStandardRepository.getApprovedEditedDraft()
    }

    fun editStandardDraft(companyStandard: CompanyStandard) : CompanyStandard {
        val variable: MutableMap<String, Any> = HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val draft= CompanyStandard()
        companyStandard.title=companyStandard.title
        companyStandard.scope=companyStandard.scope
        companyStandard.normativeReference=companyStandard.normativeReference
        companyStandard.symbolsAbbreviatedTerms=companyStandard.symbolsAbbreviatedTerms
        companyStandard.clause=companyStandard.clause
        companyStandard.special=companyStandard.special
        companyStandard.requestNumber=companyStandard.requestNumber
        companyStandard.uploadDate = Timestamp(System.currentTimeMillis())
        companyStandard.comStdNumber = companyStandard.comStdNumber
        companyStandard.preparedBy=companyStandard.preparedBy
        companyStandard.documentType=companyStandard.documentType
        companyStandard.draughting=companyStandard.draughting
        val draughting=companyStandard.draughting


        companyStandardRepository.findByIdOrNull(companyStandard.id)?.let { companyStandard ->

            with(companyStandard) {
                status = if (draughting =="Yes"){
                    3
                }else{
                    4
                }
                title = companyStandard.title
                scope = companyStandard.scope
                normativeReference = companyStandard.normativeReference
                symbolsAbbreviatedTerms = companyStandard.symbolsAbbreviatedTerms
                clause = companyStandard.clause
                special = companyStandard.special
                comStdNumber = companyStandard.comStdNumber
                documentType = companyStandard.documentType
            }
            companyStandardRepository.save(companyStandard)

        } ?: throw Exception("DRAFT NOT FOUND")

        return draft
    }

    fun getComEditedDraft(): MutableList<COMUploadedDraft> {
        return companyStandardRepository.getComEditedDraft()
    }

    fun draughtStandard(companyStandard: CompanyStandard) : CompanyStandard {
        val variable: MutableMap<String, Any> = HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val draft= CompanyStandard()
        companyStandard.title = companyStandard.title
        companyStandard.scope = companyStandard.scope
        companyStandard.normativeReference = companyStandard.normativeReference
        companyStandard.symbolsAbbreviatedTerms = companyStandard.symbolsAbbreviatedTerms
        companyStandard.clause = companyStandard.clause
        companyStandard.special = companyStandard.special
        companyStandard.comStdNumber = companyStandard.comStdNumber
        companyStandard.requestNumber = companyStandard.requestNumber
        companyStandard.documentType=companyStandard.documentType
        companyStandardRepository.findByIdOrNull(companyStandard.id)?.let { companyStandard ->

            with(companyStandard) {
                status = 4
                title = companyStandard.title
                scope = companyStandard.scope
                normativeReference = companyStandard.normativeReference
                symbolsAbbreviatedTerms = companyStandard.symbolsAbbreviatedTerms
                clause = companyStandard.clause
                special = companyStandard.special
                comStdNumber = companyStandard.comStdNumber
                documentType = companyStandard.documentType
            }
            companyStandardRepository.save(companyStandard)

        } ?: throw Exception("DRAFT NOT FOUND")

        return draft
    }

    fun getDraughtedDraft(): MutableList<COMUploadedDraft> {
        return companyStandardRepository.getDraughtedDraft()
    }



    fun proofReadStandard(companyStandard: CompanyStandard) : CompanyStandard {
        val variable: MutableMap<String, Any> = HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val draft= CompanyStandard()
        companyStandard.title = companyStandard.title
        companyStandard.scope = companyStandard.scope
        companyStandard.normativeReference = companyStandard.normativeReference
        companyStandard.symbolsAbbreviatedTerms = companyStandard.symbolsAbbreviatedTerms
        companyStandard.clause = companyStandard.clause
        companyStandard.special = companyStandard.special
        companyStandard.comStdNumber = companyStandard.comStdNumber
        companyStandard.documentType=companyStandard.documentType
        companyStandard.requestNumber=companyStandard.requestNumber
        companyStandardRepository.findByIdOrNull(companyStandard.id)?.let { companyStandard ->

            with(companyStandard) {
                status = 5
                title = companyStandard.title
                scope = companyStandard.scope
                normativeReference = companyStandard.normativeReference
                symbolsAbbreviatedTerms = companyStandard.symbolsAbbreviatedTerms
                clause = companyStandard.clause
                special = companyStandard.special
                comStdNumber = companyStandard.comStdNumber
                documentType = companyStandard.documentType
            }
            companyStandardRepository.save(companyStandard)

        } ?: throw Exception("DRAFT NOT FOUND")

        return draft
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
        companyStandardRemarks.requestId= companyStandardRemarks.requestId
        companyStandardRemarks.remarks= companyStandardRemarks.remarks
        companyStandardRemarks.status = 1.toString()
        companyStandardRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
        companyStandardRemarks.remarkBy = usersName
        companyStandardRemarks.role = "HOP"


        companyStandardRepository.findByIdOrNull(companyStandard.id)?.let { companyStandard ->
            with(companyStandard) {
                status = 6

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
        companyStandardRemarks.requestId= companyStandardRemarks.requestId
        companyStandardRemarks.remarks= companyStandardRemarks.remarks
        companyStandardRemarks.status = 1.toString()
        companyStandardRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
        companyStandardRemarks.remarkBy = usersName
        companyStandardRemarks.role = "HOP"

        if (decision == "Yes") {
            companyStandardRepository.findByIdOrNull(companyStandard.id)?.let { companyStandard ->
                with(companyStandard) {
                    status = 7

                }
                companyStandardRepository.save(companyStandard)
                companyStandardRemarksRepository.save(companyStandardRemarks)
            }?: throw Exception("DRAFT NOT FOUND")

        } else if (decision == "No") {

            companyStandardRepository.findByIdOrNull(companyStandard.id)?.let { companyStandard ->

                with(companyStandard) {
                    status = 1
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

    fun approveInternationalStandard(
        companyStandard: CompanyStandard,
        companyStandardRemarks: CompanyStandardRemarks,
        standard: Standard
    ) : String {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        companyStandard.accentTo=companyStandard.accentTo
        val decision=companyStandard.accentTo

        standard.title=standard.title
        standard.normativeReference=standard.normativeReference
        standard.symbolsAbbreviatedTerms=standard.symbolsAbbreviatedTerms
        standard.clause=standard.clause
        standard.scope=standard.scope
        standard.special=standard.special
        //val valueFound =getISDNumber()
        standard.standardNumber=standard.standardNumber
        //standard.isdn=valueFound.second
        standard.standardType="Company Standard"
        standard.status=0
        standard.dateFormed=Timestamp(System.currentTimeMillis())



        val fName = loggedInUser.firstName
        val sName = loggedInUser.lastName
        val usersName = "$fName  $sName"
        companyStandardRemarks.requestId= companyStandardRemarks.requestId
        companyStandardRemarks.remarks= companyStandardRemarks.remarks
        companyStandardRemarks.status = 1.toString()
        companyStandardRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
        companyStandardRemarks.remarkBy = usersName
        companyStandardRemarks.role = "SAC"

        if (decision == "Yes") {
            companyStandardRepository.findByIdOrNull(companyStandard.id)?.let { companyStandard ->
                with(companyStandard) {
                    status = 8

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

        } else if (decision == "No") {

            companyStandardRepository.findByIdOrNull(companyStandard.id)?.let { companyStandard ->

                with(companyStandard) {
                    status = 1
                }
                companyStandardRepository.save(companyStandard)
                companyStandardRemarksRepository.save(companyStandardRemarks)

            } ?: throw Exception("DRAFT NOT FOUND")


        }

        return "Actioned"
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
        val month = Calendar.getInstance()[Calendar.MONTH]

        return "$startId/$finalValue/$month:$year"

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
