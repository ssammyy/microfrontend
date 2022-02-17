package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std

import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.engine.history.HistoricActivityInstance
import org.flowable.engine.repository.Deployment
import org.flowable.task.api.Task
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.dto.std.ID
import org.kebs.app.kotlin.apollo.common.dto.std.TaskDetails
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.std.CallForTCApplication
import org.kebs.app.kotlin.apollo.store.model.std.DatKebsSdStandardsEntity
import org.kebs.app.kotlin.apollo.store.model.std.MembershipTCApplication
import org.kebs.app.kotlin.apollo.store.model.std.TechnicalCommitteeMember
import org.kebs.app.kotlin.apollo.store.repo.std.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.sql.Timestamp
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.set
import org.kebs.app.kotlin.apollo.common.dto.std.ProcessInstanceResponseValue as ProcessInstanceResponseValue1

@Service
class MembershipToTCService(
    private val runtimeService: RuntimeService,
    private val taskService: TaskService,
    @Qualifier("processEngine") private val processEngine: ProcessEngine,
    private val repositoryService: RepositoryService,
    private val membershipTCRepository: MembershipTCRepository,
    private val callForApplicationTCRepository: CallForApplicationTCRepository,
    private val technicalCommitteMemberRepository: TechnicalCommitteMemberRepository,
    private val decisionFeedbackRepository: DecisionFeedbackRepository,
    val commonDaoServices: CommonDaoServices,
    private val technicalCommitteeRepository: TechnicalCommitteeRepository,
    private val sdNwaUploadsEntityRepository: StandardsDocumentsRepository,


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

    fun getCallForApplications(): List<CallForTCApplication> {
        return callForApplicationTCRepository.findAll()

    }

    private fun getTaskDetails(tasks: List<Task>): List<TaskDetails> {
        val taskDetails: MutableList<TaskDetails> = ArrayList()
        for (task in tasks) {
            val processVariables = taskService.getVariables(task.id)
            taskDetails.add(TaskDetails(task.id, task.name, processVariables))
        }
        return taskDetails
    }

    fun submitTCMemberApplication(membershipTCApplication: MembershipTCApplication): ProcessInstanceResponseValue1 {
        val variable: MutableMap<String, Any> = HashMap()
        membershipTCApplication.technicalCommittee?.let { variable.put("technicalCommittee", it) }
        membershipTCApplication.organization?.let { variable.put("organization", it) }
        membershipTCApplication.nomineeName?.let { variable.put("nomineeName", it) }
        membershipTCApplication.position?.let { variable.put("position", it) }
        membershipTCApplication.postalAddress?.let { variable.put("postalAddress", it) }
        membershipTCApplication.mobileNumber?.let { variable.put("mobileNumber", it) }
        membershipTCApplication.email?.let { variable.put("email", it) }
        membershipTCApplication.authorizingName?.let { variable.put("authorizingName", it) }
        membershipTCApplication.authorisingPersonPosition?.let { variable.put("authorisingPersonPosition", it) }
        membershipTCApplication.authorisingPersonEmail?.let { variable.put("authorisingPersonEmail", it) }
        membershipTCApplication.qualifications?.let { variable.put("qualifications", it) }
        membershipTCApplication.commitment?.let { variable.put("commitment", it) }
        membershipTCApplication.tcApplicationId?.let { variable.put("commitment", it) }
        membershipTCApplication.dateOfApplication = Timestamp(System.currentTimeMillis())
        membershipTCApplication.dateOfApplication?.let { variable.put("dateOfPublishing", it) }
        membershipTCRepository.save(membershipTCApplication)
        println("Applicant has uploaded application")

        variable["id"] = membershipTCApplication.id
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variable)
        return ProcessInstanceResponseValue1(
            membershipTCApplication.id, processInstance.id, processInstance.isEnded,
            membershipTCApplication.technicalCommittee ?: throw NullValueNotAllowedException("ID is required")
        )

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

    //SPC
    fun getRecommendationsFromHOF(): List<MembershipTCApplication> {

        return membershipTCRepository.findByStatus("1")
    }


    fun completeSPCReview(membershipTCApplication: MembershipTCApplication, applicationID: Long) {
        val variable: MutableMap<String, Any> = java.util.HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val u: MembershipTCApplication = membershipTCRepository.findById(applicationID).orElse(null);
        u.status = "2"
        u.commentsBySpc = membershipTCApplication.commentsBySpc
        u.spcId = loggedInUser.id.toString()
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

}
