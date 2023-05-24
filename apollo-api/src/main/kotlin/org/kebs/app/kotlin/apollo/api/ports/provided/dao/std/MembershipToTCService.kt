package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std

import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.TaskService
import org.flowable.engine.history.HistoricActivityInstance
import org.flowable.engine.repository.Deployment
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.dto.std.ID
import org.kebs.app.kotlin.apollo.common.dto.std.ProcessInstanceResponseValue
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.UserSignatureRepository
import org.kebs.app.kotlin.apollo.store.repo.std.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import java.sql.Timestamp
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@Service
class MembershipToTCService(
    private val taskService: TaskService,
    private val notifications: Notifications,
    @Qualifier("processEngine") private val processEngine: ProcessEngine,

    private val repositoryService: RepositoryService,
    private val membershipTCRepository: MembershipTCRepository,
    private val callForApplicationTCRepository: CallForApplicationTCRepository,
    private val technicalCommitteeMemberRepository: TechnicalCommitteMemberRepository,
    val commonDaoServices: CommonDaoServices,
    private val technicalCommitteeRepository: TechnicalCommitteeRepository,

    private val sdNwaUploadsEntityRepository: StandardsDocumentsRepository,
    private val applicationMapProperties: ApplicationMapProperties,
    private val draftDocumentService: DraftDocumentService,
    private val usersSignatureRepository: UserSignatureRepository,
    private val templateEngine: TemplateEngine


    ) {


    fun deployProcessDefinition(): Deployment = repositoryService
        .createDeployment()
        .addClasspathResource("processes/std/membership_to_tc.bpmn20.xml")
        .deploy()


    //Create Form For Applicants To Apply
    //Status
    // if 1- submitted to HOf for review
    // if 2- submitted to SPC for review
    // if 3- submitted to SAC for review
    // if 4- submitted to HOF for review for approval
    // if 5- submitted to SPC for review for rejection
    // if 6- HOP Approved and sent as email


    fun submitCallsForTCMembers(callForTCApplication: CallForTCApplication) {
        val variable: MutableMap<String, Any> = HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        callForTCApplication.tcId?.let { variable.put("tcId", it) }
        //select TC TITLE
        val technicalCommittee =
            callForTCApplication.tcId?.let { technicalCommitteeRepository.findById(it).orElse(null) }
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

    fun getCallForApplications(): List<TechnicalCommittee> {

        return technicalCommitteeRepository.findAllByAdvertisingStatus("1")

    }

    fun submitTCMemberApplication(membershipTCApplication: MembershipTCApplication): ProcessInstanceResponseValue {
        membershipTCApplication.dateOfApplication = Timestamp(System.currentTimeMillis())
        val u: TechnicalCommittee = technicalCommitteeRepository.findById(membershipTCApplication.tcId!!).orElse(null)
        val encryptedId = BCryptPasswordEncoder().encode(membershipTCApplication.tcId.toString())
        membershipTCApplication.varField10 = encryptedId
        membershipTCApplication.technicalCommittee = u.title


        if (membershipTCApplication.authorizingPerson != null) {

            notifications.sendEmail(
                membershipTCApplication.email!!,
                "Application Acknowledgement:  ${u.title} - Evaluation Pending Verification",
                "Dear " + membershipTCApplication.nomineeName!! + ",\n Thank you for applying to join the: " + u.title!! + "\n We acknowledge receipt of your application and appreciate your interest in becoming a member.\n" +
                        "\n" +
                        "We are currently in the process of verifying your affiliation with your organization. Once the authorizing person confirms your membership, we will proceed with evaluating your application.\n" +
                        "\n" +
                        "Best regards,\n" +
                        "\n" +
                        "Director Standards Development and Trade"
            )
        } else {
            membershipTCApplication.approvedByOrganization = "APPROVED"
            notifications.sendEmail(
                membershipTCApplication.email!!,
                "Application Acknowledgement:  ${u.title} - Evaluation in Progress",
                "Dear " + membershipTCApplication.nomineeName!! + ",\n Thank you for submitting your application to join the KEBS " + u.title!! + " Tc. \n We appreciate your interest in becoming a member of this Technical Committee.\n" +
                        "\n" +
                        "We acknowledge receipt of your application, and our team has commenced the evaluation process. We will keep you updated on the outcome once the process is completed.\n" +
                        "\n" +
                        "Best regards,\n" +
                        "\n" +
                        "Director Standards Development and Trade"
            )
        }

        val link =
            "${applicationMapProperties.baseUrlQRValue}authorizerApproveApplication?applicationID=${encryptedId}"

        membershipTCApplication.authorizingPersonEmail?.let { email ->
            // Check if authorizingPersonEmail is not null
            notifications.sendEmail(
                email,
                "Membership Application Verification for TC Committee",
                "Dear ${membershipTCApplication.authorizingPerson},\n" +
                        "We hope this email finds you well. We are contacting you on behalf of the Kenya Bureau of Standards (KEBS) regarding a recent membership application for the " +
                        "Technical Committee ${membershipTCApplication.technicalCommittee}, identified by TC number ${u.technicalCommitteeNo}.\n" +
                        "An individual named ${membershipTCApplication.nomineeName} has applied to join  ${membershipTCApplication.technicalCommittee} and has provided your contact information as the designated authorizing person from their organization." +
                        " We kindly request your assistance in verifying the applicant's membership status within your organization.\n" +
                        " To facilitate the verification process, we have established a secure online platform where you can confirm the applicant's affiliation. Kindly access the verification page by clicking on the link provided below:.\n" +
                        "\n$link\n\n" +
                        "Your participation is of utmost importance in upholding the integrity and transparency of the TC selection process.\n" +
                        "\n" +
                        "Thank you for your invaluable cooperation and dedication.\n" +
                        "\n" +
                        "Best regards,\n" +
                        "\n" +
                        "Director of Standards Development and Trade\n\n\n\n"
            )
        }
        membershipTCRepository.save(membershipTCApplication)


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
                ResponseEntity.ok("This Link Has Already Been Used")

            } else {
                u.approvedByOrganization = "APPROVED"
                membershipTCRepository.save(u)
                //send email
                notifications.sendEmail(
                    u.email!!,
                    "Application Update: ${u.technicalCommittee} - Verification Completed, Evaluation in Progress",
                    "Dear " + u.nomineeName!! + ",\n We are pleased to inform you that your affiliation with ${u.organisationName} has been verified, confirming your eligibility to join the KEBS ${u.technicalCommittee} . Our team appreciates your cooperation during the verification process." +
                            "\n The KEBS Standard Development team has now commenced the evaluation of your application, and we will inform you of the outcome once the process is completed. \n" +
                            "\n" +
                            "Best regards,\n" +
                            "\n" +
                            "\n" +
                            "Director, Standards Development and Trade"
                )
                ResponseEntity.ok("Saved")

            }

        } else {
            throw ExpectedDataNotFound("This is not approved for approval")
        }


    }

    fun rejectUserApplication(
        applicationID: String
    ): ResponseEntity<String> {

        val u: MembershipTCApplication? = membershipTCRepository.findByVarField10(applicationID)
        return if (u != null) {
            if (u.approvedByOrganization != null) {
                ResponseEntity.ok("This Link Has Already Been Used")

            } else {
                u.approvedByOrganization = "REJECTED"
                membershipTCRepository.save(u)
                //send email
                notifications.sendEmail(
                    u.email!!,
                    "Technical Committee Application Rejected",
                    "Hello " + u.nomineeName!! + ",\n Your Application Has Been Rejected .\n Please Try Again."
                )
                ResponseEntity.ok("Saved")

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
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val u: MembershipTCApplication = membershipTCRepository.findById(applicationID).orElse(null)
        u.status = "1"
        u.comments_by_hof = membershipTCApplication.comments_by_hof
        u.hofId = loggedInUser.id.toString()
        membershipTCRepository.save(u)

    }

    fun rejectApplicantRecommendation(membershipTCApplication: MembershipTCApplication, applicationID: Long) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val u: MembershipTCApplication = membershipTCRepository.findById(applicationID).orElse(null)
        u.status = "0" //application rejected
        u.comments_by_hof = membershipTCApplication.comments_by_hof
        u.hofId = loggedInUser.id.toString()
        membershipTCRepository.save(u)

        //send rejection email
        notifications.sendEmail(
            u.email!!,
            "Technical Committee Application Rejected",
            "Hello " + u.nomineeName!! + ",\n Your Application Has Been Rejected because of " + u.comments_by_hof + " .\n Please Try Again."
        )
        ResponseEntity.ok("Saved")


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
        val u: MembershipTCApplication = membershipTCRepository.findById(applicationID).orElse(null)
        u.status = "2"
        u.commentsBySpc = membershipTCApplication.commentsBySpc
        u.spcId = loggedInUser.id.toString()
        membershipTCRepository.save(u)

    }

    fun resubmitReview(membershipTCApplication: MembershipTCApplication, applicationID: Long) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val u: MembershipTCApplication = membershipTCRepository.findById(applicationID).orElse(null)
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

        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val u: MembershipTCApplication = membershipTCRepository.findById(applicationID).orElse(null)
        if (decision == "YES") {
            u.status = "13" // approved and send to NSC for final approval
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


    //NSC Get Accepted
    fun getSacAccepted(): List<MembershipTCApplication> {

        return membershipTCRepository.findByStatus("13")
    }


    fun decisionOnSACRecommendation(
        membershipTCApplication: MembershipTCApplication,
        applicationID: Long,
        decision: String
    ) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val u: MembershipTCApplication = membershipTCRepository.findById(applicationID).orElse(null)
        if (decision == "YES") {
            u.status = "4" // approved and send to HOF for Account Creation
        } else {
            u.status = "14" //rejected and sent back to SPC with recommendations

        }
        u.commentsByNsc = membershipTCApplication.commentsByNsc
        u.nscId = loggedInUser.id.toString()
        membershipTCRepository.save(u)
    }

    //NSC Get Rejected
    fun getNscRejected(): List<MembershipTCApplication> {

        return membershipTCRepository.findByStatus("14")
    }


    //HOF Get Accepted
    fun getAccepted(): List<MembershipTCApplication> {

        return membershipTCRepository.findByStatus("4")
    }

    //HOF Send Email
    fun sendEmailToApproved(
        applicationID: Long,
        docFile: List<MultipartFile>
    ) {
        val user = commonDaoServices.findUserByID(4102)
        val mySignature: ByteArray?
        var image: String? = null
        val signatureFromDb = user.id?.let { usersSignatureRepository.findByUserId(it) }

        // Convert the image ByteArray to Base64-encoded string
        val u: MembershipTCApplication = membershipTCRepository.findById(applicationID).orElse(null)

        // Send email
        val encryptedId = BCryptPasswordEncoder().encode(u.id.toString())
        val link = "${applicationMapProperties.baseUrlQRValue}approveApplication?applicationID=${encryptedId}"

// Load the email template
        val context = Context()
        context.setVariable("subject", "Technical Committee Appointment Letter")
        context.setVariable("nomineeName", u.nomineeName)
        context.setVariable("technicalCommittee", u.technicalCommittee)
        context.setVariable("link", link)
        context.setVariable("diUser", user.firstName +" "+user.lastName)
        if (signatureFromDb != null) {
            mySignature = signatureFromDb.signature
            image = mySignature?.let { convertByteArrayToBase64(it) }
            context.setVariable("image", image)
        }

        u.email?.let {
                notifications.processEmailAttachment(
                    it,
                    "Technical Committee Appointment Letter",
                    docFile,
                    context,image
                )

        }

        u.status = "5" // Approved and appointment letter email has been sent by HOD
        u.varField10 = encryptedId

        membershipTCRepository.save(u)
    }

    fun convertByteArrayToBase64(byteArray: ByteArray): String {
        val base64Encoder = Base64.getEncoder()
        val encodedBytes = base64Encoder.encode(byteArray)
        return String(encodedBytes)
    }


    //Approve Process
    fun approveUser(
        applicationID: String
    ): ResponseEntity<String> {

        val u: MembershipTCApplication? = membershipTCRepository.findByVarField10(applicationID)
        return if (u != null) {
            if (u.status.equals("6")) {
                ResponseEntity.ok("This Link Has Already Been Used")

            } else {
                u.status = "6"
                membershipTCRepository.save(u)
                ResponseEntity.ok("Saved")
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

        val u: MembershipTCApplication = membershipTCRepository.findById(applicationID).orElse(null)
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
        val u: MembershipTCApplication = membershipTCRepository.findById(applicationID).orElse(null)
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
        val u: MembershipTCApplication = membershipTCRepository.findById(applicationID).orElse(null)
        //send email
        val encryptedId = BCryptPasswordEncoder().encode(u.id.toString())
        val link =
            "${applicationMapProperties.baseUrlQRValue}getInduction?applicationID=${encryptedId}"
        val messageBody =
            " Hello ${u.nomineeName} \n Welcome To KEBS KIMS. \n Your Login Credentials are as follows:TBD " +
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
                ResponseEntity.ok("This Link Has Already Been Used")

            } else {
                u.status = "10"
                membershipTCRepository.save(u)
                ResponseEntity.ok("Saved")
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
        val u: MembershipTCApplication = membershipTCRepository.findById(applicationID).orElse(null)
        //send email
        val messageBody =
            " Hello ${u.nomineeName} \n We will be having our first meeting scheduled on ${meetingDate}. \n Looking Forward To Seeing You "
        u.email?.let { notifications.sendEmail(it, "Technical Committee First Meeting", messageBody) }
        u.status = "11" // approved and first meeting letter email has been sent by HOD

        membershipTCRepository.save(u)
    }

    fun nonRecommend(membershipTCApplication: MembershipTCApplication, applicationID: Long) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val u: MembershipTCApplication = membershipTCRepository.findById(applicationID).orElse(null)
        u.status = "12"
        u.commentsBySpc = membershipTCApplication.commentsBySpc
        u.spcId = loggedInUser.id.toString()
        membershipTCRepository.save(u)
        //send rejection email
        notifications.sendEmail(
            u.email!!,
            "Technical Committee Application Rejected",
            "Hello " + u.nomineeName!! + ",\n Your Application Has Been Rejected because of " + u.commentsBySpc + " .\n Please Try Again."
        )
        ResponseEntity.ok("Saved")


    }

    fun saveTCMember(technicalCommitteeMember: TechnicalCommitteeMember) {
        val variable: MutableMap<String, Any> = HashMap()
        technicalCommitteeMember.userId.let { variable.put("userId", it) }
        technicalCommitteeMember.tc?.let { variable.put("tc", it) }
        technicalCommitteeMember.name?.let { variable.put("name", it) }
        technicalCommitteeMember.email?.let { variable.put("email", it) }

        println(technicalCommitteeMember.toString())
        technicalCommitteeMemberRepository.save(technicalCommitteeMember)

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

    fun uploadSDFileNotLoggedIn(
        uploads: DatKebsSdStandardsEntity,
        docFile: MultipartFile,
        doc: String,
        nomineeName: String,
        docDescription: String
    ): DatKebsSdStandardsEntity {

        with(uploads) {
//            filepath = docFile.path
            name = commonDaoServices.saveDocuments(docFile)
//            fileType = docFile.contentType
            fileType = docFile.contentType
            documentType = doc
            description = docDescription
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
