package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std

import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.engine.history.HistoricActivityInstance
import org.flowable.engine.repository.Deployment
import org.flowable.task.api.Task
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.dto.std.ID
import org.kebs.app.kotlin.apollo.common.dto.std.ProcessInstanceResponseValue
import org.kebs.app.kotlin.apollo.common.dto.std.TaskDetails
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.IUserRepository
import org.kebs.app.kotlin.apollo.store.repo.std.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.sql.Timestamp
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@Service
class MembershipToTCService(
    private val runtimeService: RuntimeService,
    private val taskService: TaskService,
    private val notifications: Notifications,

    @Qualifier("processEngine") private val processEngine: ProcessEngine,
    private val repositoryService: RepositoryService,
    private val membershipTCRepository: MembershipTCRepository,
    private val callForApplicationTCRepository: CallForApplicationTCRepository,
    private val technicalCommitteMemberRepository: TechnicalCommitteMemberRepository,
    private val decisionFeedbackRepository: DecisionFeedbackRepository,

    val commonDaoServices: CommonDaoServices,
    private val technicalCommitteeRepository: TechnicalCommitteeRepository,
    private val sdNwaUploadsEntityRepository: StandardsDocumentsRepository,
    private val applicationMapProperties: ApplicationMapProperties,
    private val usersRepo: IUserRepository,
    private val draftDocumentService: DraftDocumentService,


    ) {

    val PROCESS_DEFINITION_KEY = "membership_to_TC"
    val APPLICANTS = "applicants"
    val HOF = "HOF"
    val SPC = "SPC"
    val SAC = "SAC"
    val HOD_SIC = "HOD-SIC"

    fun deployProcessDefinition(): Deployment = repositoryService
        .createDeployment()
        .addClasspathResource("processes/std/membership_to_tc.bpmn20.xml")
        .deploy()


    //Create Form For Applicants To Apply
    //Status
    // if 1- submitted to HOf for review
    // if 2- submitted to SPC for review
    // if 3- submitted to SAC for review
    // if 4- submitted to HOf for review for approval
    // if 5- submitted to SPC for review for rejection
    // if 6- HOP Approved and sent as email


    fun submitCallsForTCMembers(callForTCApplication: CallForTCApplication) {
        val variable: MutableMap<String, Any> = HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        callForTCApplication.tcId?.let { variable.put("tcId", it) }
        //select TC TITLE
        val technicalCommittee =
            callForTCApplication.tcId?.let { technicalCommitteeRepository.findById(it).orElse(null) };
        callForTCApplication.tc = technicalCommittee?.title
        callForTCApplication.tc?.let { variable.put("tc", it) }
        callForTCApplication.title?.let { variable.put("title", it) }
        callForTCApplication.description?.let { variable.put("description", it) }
        callForTCApplication.dateOfPublishing = Timestamp(System.currentTimeMillis()).toString()
        callForTCApplication.dateOfPublishing?.let { variable.put("dateOfPublishing", it) }
        callForTCApplication.status = "ACTIVE"
        callForTCApplication.status?.let { variable.put("status", it) }
        callForTCApplication.expiryDate = Timestamp(Instant.now().plus(21, ChronoUnit.DAYS).toEpochMilli())
        callForTCApplication.expiryDate?.let { variable.put("expiryDate", it) }
        callForTCApplication.createdBy = loggedInUser.id.toString()
        callForTCApplication.createdBy?.let { variable.put("createdBy", it) }
        callForTCApplication.createdOn = Timestamp(System.currentTimeMillis())
        callForTCApplication.createdOn?.let { variable.put("createdOn", it) }
        callForApplicationTCRepository.save(callForTCApplication)

    }

    //Edit Form
    fun editCallsForTCMembers(callForTCApplication: CallForTCApplication, applicationID: Long) {
        val variable: MutableMap<String, Any> = HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val u: CallForTCApplication = callForApplicationTCRepository.findById(applicationID).orElse(null);
        u.tc?.let { variable.put("tc", it) }
        u.tcId?.let { variable.put("tcId", it) }
        u.title?.let { variable.put("title", it) }
        u.modifiedBy = loggedInUser.id.toString()
        u.modifiedOn = Timestamp(System.currentTimeMillis())
        callForApplicationTCRepository.save(u)


    }

    //Delete Form
    fun deleteCallsForTCMembers(callForTCApplication: CallForTCApplication, applicationID: Long) {
        val variable: MutableMap<String, Any> = HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val u: CallForTCApplication = callForApplicationTCRepository.findById(applicationID).orElse(null);
        u.status?.let { variable.put("DELETED", it) }
        callForTCApplication.deleteBy = loggedInUser.id.toString()
        callForTCApplication.deletedOn = Timestamp(System.currentTimeMillis())
        callForApplicationTCRepository.save(callForTCApplication)


    }

    fun getCallForApplications(): List<TechnicalCommittee> {

        return technicalCommitteeRepository.findAllByAdvertisingStatus("1")

    }

    private fun getTaskDetails(tasks: List<Task>): List<TaskDetails> {
        val taskDetails: MutableList<TaskDetails> = ArrayList()
        for (task in tasks) {
            val processVariables = taskService.getVariables(task.id)
            taskDetails.add(TaskDetails(task.id, task.name, processVariables))
        }
        return taskDetails
    }

    fun submitTCMemberApplication(membershipTCApplication: MembershipTCApplication): ProcessInstanceResponseValue {
        membershipTCApplication.dateOfApplication = Timestamp(System.currentTimeMillis())
        val u: TechnicalCommittee = technicalCommitteeRepository.findById(membershipTCApplication.tcId!!).orElse(null);
        val encryptedId = BCryptPasswordEncoder().encode(membershipTCApplication.tcId.toString())
        membershipTCApplication.varField10 = encryptedId
        membershipTCApplication.technicalCommittee = u.title
        membershipTCRepository.save(membershipTCApplication)

        notifications.sendEmail(
            membershipTCApplication.email!!,
            "Technical Committee",
            "Hello " + membershipTCApplication.nomineeName!! + ",\n We have received your application For The Following Technical Committee: " + u.title!! + "\n Please Wait for Further Verification."
        )

        val link =
            "${applicationMapProperties.baseUrlQRValue}authorizerApproveApplication?applicationID=${encryptedId}"

        notifications.sendEmail(
            membershipTCApplication.authorizingPersonEmail!!,
            "Technical Committee",
            "Hello " + membershipTCApplication.authorizingPerson!! + ",\n We have received an application by " + membershipTCApplication.nomineeName + " For The Following Technical Committee: " + u.title!! + "\n" +
                    " Please Click On The Following Link To Verify That " + membershipTCApplication.nomineeName + " is a member of your organisation. "
                    + "\n " + link +
                    "\n\n\n\n\n\n"
        )

        return ProcessInstanceResponseValue(
            membershipTCApplication.id,
            "Complete",
            true,
            membershipTCApplication.nomineeName ?: throw NullValueNotAllowedException("ID is required")
        )

    }

    fun approveUserApplication(
        applicationID: String
    ): ResponseEntity<String> {

        val u: MembershipTCApplication? = membershipTCRepository.findByVarField10(applicationID)
        return if (u != null) {
            if (u.approvedByOrganization != null) {
                ResponseEntity.ok("This Link Has Already Been Used");

            } else {
                u.approvedByOrganization = "APPROVED"
                membershipTCRepository.save(u)
                //send email
                notifications.sendEmail(
                    u.email!!,
                    "Technical Committee Application Approved",
                    "Hello " + u.nomineeName!! + ",\n Your Application Has Been Approved .\n Please Wait for Further Communication."
                )
                ResponseEntity.ok("Saved");

            }

        } else {
            throw ExpectedDataNotFound("This is not approved for approval")
        }


    }

    fun getApplicationsForReview(): List<MembershipTCApplication> {
        return membershipTCRepository.findByStatusIsNull()

    }

    // HOF Has Received The applications and is reviewing them and giving a decision. Status will be set to 1
    fun decisionOnApplicantRecommendation(membershipTCApplication: MembershipTCApplication, applicationID: Long) {
        val variable: MutableMap<String, Any> = java.util.HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val u: MembershipTCApplication = membershipTCRepository.findById(applicationID).orElse(null);
        u.status = "1"
        u.comments_by_hof = membershipTCApplication.comments_by_hof
        u.hofId = loggedInUser.id.toString()
        membershipTCRepository.save(u)

    }

    fun rejectApplicantRecommendation(membershipTCApplication: MembershipTCApplication, applicationID: Long) {
        val variable: MutableMap<String, Any> = java.util.HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val u: MembershipTCApplication = membershipTCRepository.findById(applicationID).orElse(null);
        u.status = "0" //application rejected
        u.comments_by_hof = membershipTCApplication.comments_by_hof
        u.hofId = loggedInUser.id.toString()
        membershipTCRepository.save(u)

    }

    //SPC
    fun getRecommendationsFromHOF(): List<MembershipTCApplication> {

        return membershipTCRepository.findByStatus("1")
    }

    fun getAllApplications(): List<MembershipTCApplication> {

        return membershipTCRepository.findAll()
    }

    fun getRejectedFromHOF(): List<MembershipTCApplication> {

        return membershipTCRepository.findByStatus("0")
    }


    fun completeSPCReview(membershipTCApplication: MembershipTCApplication, applicationID: Long) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val u: MembershipTCApplication = membershipTCRepository.findById(applicationID).orElse(null);
        u.status = "2"
        u.commentsBySpc = membershipTCApplication.commentsBySpc
        u.spcId = loggedInUser.id.toString()
        membershipTCRepository.save(u)

    }

    fun resubmitReview(membershipTCApplication: MembershipTCApplication, applicationID: Long) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val u: MembershipTCApplication = membershipTCRepository.findById(applicationID).orElse(null);
        u.status = "2"
        u.commentsBySpc = membershipTCApplication.commentsBySpc
        u.spcId = loggedInUser.id.toString()
        u.resubmission = "1"
        membershipTCRepository.save(u)

    }

    //SAC
    fun getRecommendationsFromSPC(): List<MembershipTCApplication> {

        return membershipTCRepository.findByStatus("2")
    }

    fun decisionOnSPCRecommendation(
        membershipTCApplication: MembershipTCApplication,
        applicationID: Long,
        decision: String
    ) {
        val variable: MutableMap<String, Any> = java.util.HashMap()

        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val u: MembershipTCApplication = membershipTCRepository.findById(applicationID).orElse(null);
        if (decision == "YES") {
            u.status = "4" // approved and send to HOD to send appointment letters
        } else {
            u.status = "3" //rejected and sent back to SPC with recommendations

        }
        u.commentsBySac = membershipTCApplication.commentsBySac
        u.sacId = loggedInUser.id.toString()
        membershipTCRepository.save(u)
    }

    //SPC Get Rejected
    fun getRejected(): List<MembershipTCApplication> {

        return membershipTCRepository.findByStatus("3")
    }

    //HOF Get Accepted
    fun getAccepted(): List<MembershipTCApplication> {

        return membershipTCRepository.findByStatus("4")
    }

    //HOF Send Email
    fun sendEmailToApproved(
        membershipTCApplication: MembershipTCApplication,
        applicationID: Long
    ) {
        val u: MembershipTCApplication = membershipTCRepository.findById(applicationID).orElse(null);
        //send email
        val encryptedId = BCryptPasswordEncoder().encode(u.id.toString())
        val link =
            "${applicationMapProperties.baseUrlQRValue}approveApplication?applicationID=${encryptedId}"
        val messageBody =
            " Hello ${u.nomineeName} \n Thank you for your application. You have been appointed as a member of " +
                    "${u.technicalCommittee}. Please find attached the Terms Of Reference. Also please click on the following link to confirm appointment  \n " +
                    link +
                    "\n\n\n\n\n\n"

        u.email?.let {

            val fileName = "static/tor.pdf"
            val classLoader = javaClass.classLoader
            val fileUrl = classLoader.getResource(fileName)
            val filePath = fileUrl?.path ?: throw IllegalArgumentException("File not found: $fileName")


            notifications.sendEmail(
                it,
                "Technical Committee Appointment  Letter",
                messageBody,
                filePath
            )
        }
        u.status = "5" // approved and appointment letter email has been sent by HOD
        u.varField10 = encryptedId



        membershipTCRepository.save(u)
    }

    //Approve Process
    fun approveUser(
        applicationID: String
    ): ResponseEntity<String> {

        val u: MembershipTCApplication? = membershipTCRepository.findByVarField10(applicationID)
        return if (u != null) {
            if (u.status.equals("6")) {
                ResponseEntity.ok("This Link Has Already Been Used");

            } else {
                u.status = "6"
                membershipTCRepository.save(u)
                ResponseEntity.ok("Saved");
            }

        } else {
            throw ExpectedDataNotFound("This is not approved for appointment")
        }


        //val u: MembershipTCApplication = membershipTCRepository.findByVarField10(applicationID);


        //send email

    }

    //HOF Get Users With Approved Email Together with TC-Sec
    fun getApprovedEmail(): List<MembershipTCApplication> {

        return membershipTCRepository.findByStatus("6")
    }

    //HOD Forwards to HOD-ICT
    fun decisionOnApprovedHof(
        membershipTCApplication: MembershipTCApplication,
        applicationID: Long,
        decision: String
    ) {
        val variable: MutableMap<String, Any> = java.util.HashMap()

        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val u: MembershipTCApplication = membershipTCRepository.findById(applicationID).orElse(null);
        u.status = "7" // approved and send to HOD-ICT
        u.varField9 = decision //this is scope that is defined
        membershipTCRepository.save(u)
    }


    //HOD ICT gets forwarded users
    fun getAllUsersToCreateCredentials(): List<MembershipTCApplication> {

        return membershipTCRepository.findByStatus("7")
    }

    fun decisionUponCreation(
        membershipTCApplication: MembershipTCApplication,
        applicationID: Long
    ) {
        val u: MembershipTCApplication = membershipTCRepository.findById(applicationID).orElse(null);
        u.status = "8" // created as users on QAIMSS
        membershipTCRepository.save(u)
    }

    //HOD  get all created as users on QAIMSS
    fun getAllUsersCreatedCredentials(): List<MembershipTCApplication> {

        return membershipTCRepository.findByStatus("8")
    }

    //HOD Send Email For Induction
    fun sendEmailForInduction(
        membershipTCApplication: MembershipTCApplication,
        applicationID: Long
    ) {
        val u: MembershipTCApplication = membershipTCRepository.findById(applicationID).orElse(null);
        //send email
        val encryptedId = BCryptPasswordEncoder().encode(u.id.toString())
        val link =
            "${applicationMapProperties.baseUrlQRValue}getInduction?applicationID=${encryptedId}"
        val messageBody =
            " Hello ${u.nomineeName} \n Welcome To KEBS QAIMSS. \n Your Login Credentials are as follows:TBD " +
                    "${u.technicalCommittee}. Please click on the following link to confirm induction \n " +
                    link +
                    "\n\n\n\n\n\n"
        u.email?.let { notifications.sendEmail(it, "Technical Committee Induction  Letter", messageBody) }
        u.status = "9" // approved and induction letter email has been sent by HOD
        u.varField10 = encryptedId



        membershipTCRepository.save(u)
    }

    //Approve Process Induction
    fun approveUserInduction(
        applicationID: String
    ): ResponseEntity<String> {

        val u: MembershipTCApplication? = membershipTCRepository.findByVarField10(applicationID)
        return if (u != null) {
            if (u.status.equals("10")) {
                ResponseEntity.ok("This Link Has Already Been Used");

            } else {
                u.status = "10"
                membershipTCRepository.save(u)
                ResponseEntity.ok("Saved");
            }

        } else {
            throw ExpectedDataNotFound("This is not approved for induction")
        }


        //val u: MembershipTCApplication = membershipTCRepository.findByVarField10(applicationID);


        //send email

    }

    //Get all Approved for induction
    fun getAllUsersApprovedForInduction(): List<MembershipTCApplication> {

        return membershipTCRepository.findByStatus("10")
    }

    //HOD Send Email For Induction
    fun sendEmailForFirstMeeting(
        membershipTCApplication: MembershipTCApplication,
        applicationID: Long,
        meetingDate: String
    ) {
        val u: MembershipTCApplication = membershipTCRepository.findById(applicationID).orElse(null);
        //send email
        val messageBody =
            " Hello ${u.nomineeName} \n We will be having our first meeting scheduled on ${meetingDate}. \n Looking Forward To Seeing You "
        u.email?.let { notifications.sendEmail(it, "Technical Committee First Meeting", messageBody) }
        u.status = "11" // approved and first meeting letter email has been sent by HOD

        membershipTCRepository.save(u)
    }


    fun getTCMemberCreationTasks(): List<TaskDetails> {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(HOD_SIC).list()
        return getTaskDetails(tasks)
    }

    fun saveTCMember(technicalCommitteeMember: TechnicalCommitteeMember) {
        val variable: MutableMap<String, Any> = HashMap()
        technicalCommitteeMember.userId.let { variable.put("userId", it) }
        technicalCommitteeMember.tc?.let { variable.put("tc", it) }
        technicalCommitteeMember.name?.let { variable.put("name", it) }
        technicalCommitteeMember.email?.let { variable.put("email", it) }

        println(technicalCommitteeMember.toString())
        technicalCommitteMemberRepository.save(technicalCommitteeMember)

        taskService.complete(technicalCommitteeMember.taskId, variable)
        println("Applicant has TC Member")
    }

    fun checkProcessHistory(id: ID): List<HistoricActivityInstance> {
        val historyService = processEngine.historyService
        val activities = historyService
            .createHistoricActivityInstanceQuery()
            .processInstanceId(id.ID)
            .finished()
            .orderByHistoricActivityInstanceEndTime()
            .asc()
            .list()
        for (activity in activities) {
            println(
                activity.activityId + " took " + activity.durationInMillis + " milliseconds"
            )
        }

        return activities

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

    fun uploadSDFileNotLoggedIn(
        uploads: DatKebsSdStandardsEntity,
        docFile: MultipartFile,
        doc: String,
        nomineeName: String,
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
            createdBy = nomineeName
            createdOn = commonDaoServices.getTimestamp()
        }

        return sdNwaUploadsEntityRepository.save(uploads)
    }

    fun getUserCv(
        standardId: Long,
        documentType: String,
        documentTypeDef: String
    ): Collection<DatKebsSdStandardsEntity?>? {

        return draftDocumentService.findUserCv(standardId, documentType, documentTypeDef)
    }

}
