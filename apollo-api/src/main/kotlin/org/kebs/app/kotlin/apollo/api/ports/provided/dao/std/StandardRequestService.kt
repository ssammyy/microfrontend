package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std


//import com.google.gson.stream.JsonReader

import mu.KotlinLogging
import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.engine.history.HistoricActivityInstance
import org.flowable.engine.repository.Deployment
import org.flowable.task.api.Task
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.web.config.EmailConfig
import org.kebs.app.kotlin.apollo.common.dto.std.*
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.IUserRepository
import org.kebs.app.kotlin.apollo.store.repo.IUserRoleAssignmentsRepository
import org.kebs.app.kotlin.apollo.store.repo.IUserRolesRepository
import org.kebs.app.kotlin.apollo.store.repo.std.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*


@Service
class StandardRequestService(
    private val runtimeService: RuntimeService,
    private val taskService: TaskService,
    @Qualifier("processEngine") private val processEngine: ProcessEngine,
    private val repositoryService: RepositoryService,
    private val standardRequestRepository: StandardRequestRepository,
    private val standardNWIRepository: StandardNWIRepository,
    private val standardJustificationRepository: StandardJustificationRepository,
    private val standardWorkPlanRepository: StandardWorkPlanRepository,
    private val productRepository: ProductRepository,
    private val departmentRepository: DepartmentRepository,
    private val tcUserAssignmentRepository: TcUserAssignmentRepository,

    private val technicalCommitteeRepository: TechnicalCommitteeRepository,
    private val productSubCategoryRepository: ProductSubCategoryRepository,
    private val hofFeedbackRepository: HOFFeedbackRepository,
    private val rankingCriteriaRepository: RankingCriteriaRepository,
    private val liaisonOrganizationRepository: LiaisonOrganizationRepository,
    private val emailConfig: EmailConfig,
    private val voteOnNWIRepository: VoteOnNWIRepository,
    private val decisionJustificationRepository: DecisionJustificationRepository,
    private val commonDaoServices: CommonDaoServices,
    private val sdNwaUploadsEntityRepository: StandardsDocumentsRepository,
    private val notifications: Notifications,
    private val draftDocumentService: DraftDocumentService,
    private val iUserRoleAssignmentsRepository: IUserRoleAssignmentsRepository,
    private val userRolesAssignRepo: IUserRoleAssignmentsRepository,
    private val userRolesRepo: IUserRolesRepository,
    private val usersRepo: IUserRepository,
    private val sdDocumentsRepository: StandardsDocumentsRepository,


    ) {

    val PROCESS_DEFINITION_KEY = "requestModule"
    val TASK_CANDIDATE_GROUP_HOF = "HOF"
    val TASK_CANDIDATE_GROUP_TC_SEC = "TC-sec"
    val TASK_CANDIDATE_GROUP_TC = "TC"
    val TASK_CANDIDATE_GROUP_SPC_SEC = "SPC-sec"
    val variable: MutableMap<String, Any> = HashMap()


    fun deployProcessDefinition(): Deployment = repositoryService
        .createDeployment()
        .addClasspathResource("processes/std/request_module.bpmn20.xml")
        .deploy()


    fun requestForStandard(standardRequest: StandardRequest): ProcessInstanceResponseValue {
        standardRequest.submissionDate = Timestamp(System.currentTimeMillis())
        standardRequest.createdOn = Timestamp(System.currentTimeMillis())
        standardRequest.departmentName = departmentRepository.findNameById(standardRequest.departmentId?.toLong())
        standardRequest.status = "Review By HOD"
        standardRequest.ongoingStatus = "Review By HOD"

        val department = departmentRepository.findByIdOrNull(standardRequest.departmentId?.toLong())
        standardRequest.requestNumber = generateSRNumber(department?.abbreviations)
        standardRequest.rank = '3'.toString()
        standardRequestRepository.save(standardRequest)
        notifications.sendEmail(
            standardRequest.email!!,
            "Standard Request Submission",
            "Hello " + standardRequest.name!! + ",\n We have received your request for standard. This is your request number: " + standardRequest.requestNumber!!
        )
        return ProcessInstanceResponseValue(
            standardRequest.id,
            "Complete",
            true,
            standardRequest.requestNumber ?: throw NullValueNotAllowedException("ID is required")
        )

    }


    fun updateDepartmentStandardRequest(standardRequest: StandardRequest) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val department = departmentRepository.findByIdOrNull(standardRequest.departmentId?.toLong())

        val standardRequestToUpdate = standardRequestRepository.findById(standardRequest.id)
            .orElseThrow { RuntimeException("No Standard Request found") }

        val s1 = standardRequestToUpdate.requestNumber
        val parts = s1?.split("/".toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()
        val part1 = parts?.get(0)
        val part2 = parts?.get(1)
        val replaceString = department?.abbreviations?.let { part1?.replace(part1, it) }
        standardRequestToUpdate.requestNumber = "$replaceString/$part2"
        standardRequestToUpdate.departmentId = standardRequest.departmentId
        standardRequestToUpdate.modifiedOn = Timestamp(System.currentTimeMillis())
        standardRequestToUpdate.modifiedBy = loggedInUser.id.toString()
        standardRequestRepository.save(standardRequestToUpdate)
    }

    fun updateTcSec(standardRequest: StandardRequest) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        val standardRequestToUpdate = standardRequestRepository.findById(standardRequest.id)
            .orElseThrow { RuntimeException("No Standard Request found") }


        standardRequestToUpdate.tcSecAssigned = standardRequest.tcSecAssigned
        standardRequestToUpdate.modifiedOn = Timestamp(System.currentTimeMillis())
        standardRequestToUpdate.modifiedBy = loggedInUser.id.toString()
        standardRequestRepository.save(standardRequestToUpdate)
    }

    fun evaluateStandardRequest(tcId: String, departmentId: String): String {
        return rankingCriteriaRepository.findRankByAll(departmentId.toLong(), tcId.toLong()).toString()
    }


    fun sendFeedback(toMail: String, body: String, toName: String, subject: String) {


        // Create a mail sender
        val mailSender = JavaMailSenderImpl()
        mailSender.host = this.emailConfig.host
        mailSender.port = this.emailConfig.port
        mailSender.username = this.emailConfig.username
        mailSender.password = this.emailConfig.password


        // Create an email instance
        val mailMessage = SimpleMailMessage()
        mailMessage.setFrom("donotreply@kebs.org")
        mailMessage.setTo(toMail)
        mailMessage.setSubject(subject)
        mailMessage.setText("$toName, $body")

        // Send mail
        mailSender.send(mailMessage)
    }


    fun getAllStandardRequests(): List<StandardsDto> {
        val standardRequest: List<StandardRequest> =
            standardRequestRepository.findAllByStatusAndNwiStatusIsNull("Review By HOD")
        return standardRequest.map { p ->
            StandardsDto(
                p.id,
                p.requestNumber,
                p.rank,
                p.name,
                p.phone,
                p.email,
                p.submissionDate,
                p.departmentId,
                p.tcId,
                p.organisationName,
                p.subject,
                p.description,
                p.economicEfficiency,
                p.healthSafety,
                p.environment,
                p.integration,
                p.exportMarkets,
                p.levelOfStandard,
                p.status,
                departmentRepository.findNameById(p.departmentId?.toLong()),
                p.createdOn,


                )
        }


    }

    fun getAllApplications(): List<AllApplicationsStandardsDto> {
        val standardRequest: List<StandardRequest> =
            standardRequestRepository.findAllByProcess("Develop a standard through committee draft")
        return standardRequest.map { p ->
            AllApplicationsStandardsDto(
                p.id,
                p.requestNumber,
                p.rank,
                p.name,
                p.phone,
                p.email,
                p.submissionDate,
                p.departmentId,
                p.tcId,
                p.organisationName,
                p.subject,
                p.description,
                p.economicEfficiency,
                p.healthSafety,
                p.environment,
                p.integration,
                p.exportMarkets,
                p.levelOfStandard,
                p.status,
                departmentRepository.findNameByIdB(p.departmentId?.toLong()) ?: "",
                p.createdOn,
                p.ongoingStatus,
                p.tcSecAssigned?.toLong()?.let { usersRepo.findById(it) }
                    ?.get()?.firstName + " " + p.tcSecAssigned?.toLong()?.let { usersRepo.findById(it) }
                    ?.get()?.lastName,


                )
        }


    }


    fun getHOFTasks(): List<TaskDetails> {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_HOF).list()
        return getTaskDetails(tasks)
    }

    private fun getTaskDetails(tasks: List<Task>): List<TaskDetails> {
        val taskDetails: MutableList<TaskDetails> = ArrayList()
        for (task in tasks) {
            val processVariables = taskService.getVariables(task.id)
            taskDetails.add(TaskDetails(task.id, task.name, processVariables))
        }
        return taskDetails
    }

    fun hofReview(hofFeedback: HOFFeedback): HOFFeedback {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val standardRequestToUpdate = hofFeedback.sdRequestID?.let {
            standardRequestRepository.findById(it.toLong())
                .orElseThrow { RuntimeException("No Standard Request found") }
        }
        if (standardRequestToUpdate != null) {
            when (hofFeedback.sdResult) {
                "Approve For Review" -> {
                    standardRequestToUpdate.status = "Assigned To TC Sec"
                    standardRequestToUpdate.process = hofFeedback.sdOutput
                    standardRequestToUpdate.tcSecAssigned = hofFeedback.isTc
                    standardRequestToUpdate.tcAssigned = hofFeedback.tcId
                    standardRequestToUpdate.modifiedOn = Timestamp(System.currentTimeMillis())
                    standardRequestToUpdate.modifiedBy = loggedInUser.id.toString()
                    standardRequestToUpdate.ongoingStatus = "Prepare New Work Item"


                }

                "Reject For Review" -> {
                    standardRequestToUpdate.status = "Declined For Review"
                    standardRequestToUpdate.modifiedOn = Timestamp(System.currentTimeMillis())
                    standardRequestToUpdate.modifiedBy = loggedInUser.id.toString()

                    val subject = "Request Declination"
                    val body = buildString {
                        append("Dear ${standardRequestToUpdate.name},\n\n")
                        append("Your request has been declined because of ${hofFeedback.reason}.\n")
                        if (hofFeedback.link != null) {
                            append("Kindly click on the link and follow the instructions.\n")
                            append("${hofFeedback.link}\n")
                        }
                        append("Best regards,\n\n")
                        append("Director Standards Development and Trade")
                    }

                    notifications.sendEmail(standardRequestToUpdate.email!!, subject, body)

                }

                "On Hold" -> {
                    standardRequestToUpdate.status = "On Hold"
                    standardRequestToUpdate.modifiedOn = Timestamp(System.currentTimeMillis())
                    standardRequestToUpdate.modifiedBy = loggedInUser.id.toString()

                }
            }
            standardRequestRepository.save(standardRequestToUpdate)
        }


        hofFeedback.reviewedBy = loggedInUser.id.toString()
        hofFeedback.createdOn = Timestamp(System.currentTimeMillis())

        hofFeedbackRepository.save(hofFeedback)
//        taskService.complete(hofFeedback.taskId)

        return hofFeedback
    }

    fun getTCSECTasks(): List<TaskDetails> {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_TC_SEC).list()
        return getTaskDetails(tasks)
    }

    fun getAllStandardRequestsToPrepareNWI(): List<StandardsDto> {
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        val standardRequest: List<StandardRequest> =
            standardRequestRepository.findAllByStatusAndNwiStatusIsNullAndProcess(
                "Assigned To TC Sec",
                "Develop a standard through committee draft"
            )

        return standardRequest.map { p ->
            StandardsDto(
                p.id,
                p.requestNumber,
                p.rank,
                p.name,
                p.phone,
                p.email,
                p.submissionDate,
                p.departmentId,
                p.tcId,
                p.organisationName,
                p.subject,
                p.description,
                p.economicEfficiency,
                p.healthSafety,
                p.environment,
                p.integration,
                p.exportMarkets,
                p.levelOfStandard,
                p.status,
                returnDepartmentName(p.departmentId!!.toLong()),
                p.createdOn,
                //Feedback Segment From Review

                p.tcSecAssigned?.toLong()?.let { usersRepo.findById(it) }
                    ?.get()?.firstName + " " + p.tcSecAssigned?.toLong()?.let { usersRepo.findById(it) }
                    ?.get()?.lastName,

                returnUsername(p.id.toString()),
                p.id.let { findHofFeedbackDetails(it.toString())?.createdOn },
                p.id.let { findHofFeedbackDetails(it.toString())?.sdOutput },
                p.id.let { findHofFeedbackDetails(it.toString())?.sdResult },
                p.id.let { findHofFeedbackDetails(it.toString())?.reason },

                )
        }


    }

    fun getAllRejectedStandardRequestsToPrepareNWI(): List<StandardsDto> {
        val standardRequest: List<StandardRequest> =
            standardRequestRepository.findAllByStatusAndNwiStatusIsNull("Declined For Review")
        return standardRequest.map { p ->
            StandardsDto(
                p.id,
                p.requestNumber,
                p.rank,
                p.name,
                p.phone,
                p.email,
                p.submissionDate,
                p.departmentId,
                p.tcId,
                p.organisationName,
                p.subject,
                p.description,
                p.economicEfficiency,
                p.healthSafety,
                p.environment,
                p.integration,
                p.exportMarkets,
                p.levelOfStandard,
                p.status,
                returnDepartmentName(p.departmentId!!.toLong()),
                p.createdOn,

                //Feedback Segment From Review
                p.tcSecAssigned?.toLong()?.let { usersRepo.findById(it) }
                    ?.get()?.firstName + " " + p.tcSecAssigned?.toLong()?.let { usersRepo.findById(it) }
                    ?.get()?.lastName,

                returnUsername(p.id.toString()),
                p.id.let { findHofFeedbackDetails(it.toString())?.createdOn },
                p.id.let { findHofFeedbackDetails(it.toString())?.sdOutput },
                p.id.let { findHofFeedbackDetails(it.toString())?.sdResult },
                p.id.let { findHofFeedbackDetails(it.toString())?.reason },


                )
        }


    }


    fun getAllOnHoldStandardRequestsToPrepareNWI(): List<StandardsDto> {
        val standardRequest: List<StandardRequest> =
            standardRequestRepository.findAllByStatusAndNwiStatusIsNull("On Hold")
        return standardRequest.map { p ->
            StandardsDto(
                p.id,
                p.requestNumber,
                p.rank,
                p.name,
                p.phone,
                p.email,
                p.submissionDate,
                p.departmentId,
                p.tcId,
                p.organisationName,
                p.subject,
                p.description,
                p.economicEfficiency,
                p.healthSafety,
                p.environment,
                p.integration,
                p.exportMarkets,
                p.levelOfStandard,
                p.status,
                returnDepartmentName(p.departmentId!!.toLong()),
                p.createdOn,

                //Feedback Segment From Review
                p.tcSecAssigned?.toLong()?.let { usersRepo.findById(it) }
                    ?.get()?.firstName + " " + p.tcSecAssigned?.toLong()?.let { usersRepo.findById(it) }
                    ?.get()?.lastName,

                returnUsername(p.id.toString()),
                p.id.let { findHofFeedbackDetails(it.toString())?.createdOn },
                p.id.let { findHofFeedbackDetails(it.toString())?.sdOutput },
                p.id.let { findHofFeedbackDetails(it.toString())?.sdResult },
                p.id.let { findHofFeedbackDetails(it.toString())?.reason },


                )
        }


    }

    fun uploadNWI(standardNWI: StandardNWI): ProcessInstanceResponseValue {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        var allOrganization = ""
//        val klaxon = Klaxon()
//        JsonReader(StringReader(standardNWI.liaisonOrganisation!!)).use { reader ->
//            reader.beginArray {
//                while (reader.hasNext()) {
//                    val liaisonOrganization = klaxon.parse<LiaisonOrganization>(reader)
//                    println(liaisonOrganization?.name)
//                    allOrganization += liaisonOrganization?.name + ","
//                }
//            }
//        }
//        standardNWI.liaisonOrganisation = allOrganization

        val standardRequestToUpdate = standardNWI.standardId?.let {
            standardRequestRepository.findById(it)
                .orElseThrow { RuntimeException("No Standard Request found") }
        }
        if (standardRequestToUpdate != null) {
            standardRequestToUpdate.nwiStatus = "New Work Item Created For Voting"
            standardRequestToUpdate.ongoingStatus = "New Work Item Created For Voting"

            standardRequestRepository.save(standardRequestToUpdate)

        }


        val technicalCommittee: TechnicalCommittee =
            technicalCommitteeRepository.findById(standardRequestToUpdate?.tcAssigned!!.toLong()).orElse(null)
        val department: Department =
            departmentRepository.findById(standardRequestToUpdate.departmentId!!.toLong()).orElse(null)




        standardNWI.status = "Vote ON NWI"
        standardNWI.createdOn = Timestamp(System.currentTimeMillis())
        standardNWI.tcSec = loggedInUser.id.toString()
        standardNWI.tcId = standardRequestToUpdate.tcAssigned
        standardNWI.nameOfTC = technicalCommittee.title
        standardNWI.nameOfDepartment = department.name
        standardNWI.organization = department.name

        standardNWI.departmentId = department.id

        standardNWIRepository.save(standardNWI)
        return ProcessInstanceResponseValue(standardNWI.id, "Complete", true, "standardNWI.id")

    }


    fun getTCTasks(): List<TaskDetails> {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_TC).list()
        return getTaskDetails(tasks)
    }

    fun getAllNwiSUnderVote(): List<StandardNWI> {
        return standardNWIRepository.findAllByStatus("Vote ON NWI")
    }

    fun getAllNwisLoggedInUserToVoteFor(): List<StandardNWI> {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        return standardNWIRepository.getPendingVotingUser(loggedInUser.id!!)
    }


    fun getAllTcSecLoggedInNwiSUnderVote(): List<StandardNWI> {
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        return standardNWIRepository.findAllByStatusAndTcSec("Vote ON NWI", loggedInUser.id.toString())
    }

    fun getAllNwiSApproved(): List<StandardNWI> {
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        return standardNWIRepository.findAllByStatusAndTcSec("Upload Justification", loggedInUser.id.toString())
    }

    fun getAllNwiSRejected(): List<StandardNWI> {
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        return standardNWIRepository.findAllByStatusAndTcSec("NWI Rejected", loggedInUser.id.toString())
    }

    fun getANwiById(nwiId: Long): List<StandardNWI> {
        return standardNWIRepository.findAllById(nwiId)
    }

    fun getRequestById(requestId: Long): List<StandardsDto> {
        val standardRequest: List<StandardRequest> =
            standardRequestRepository.findAllById(requestId)
        return standardRequest.map { p ->
            StandardsDto(
                p.id,
                p.requestNumber,
                p.rank,
                p.name,
                p.phone,
                p.email,
                p.submissionDate,
                p.departmentId,
                p.tcId,
                p.organisationName,
                p.subject,
                p.description,
                p.economicEfficiency,
                p.healthSafety,
                p.environment,
                p.integration,
                p.exportMarkets,
                p.levelOfStandard,
                p.status,
                departmentRepository.findNameById(p.departmentId?.toLong()),
                p.createdOn,

                //Feedback Segment From Review
                p.tcSecAssigned?.toLong()?.let { usersRepo.findById(it) }
                    ?.get()?.firstName + " " + p.tcSecAssigned?.toLong()?.let { usersRepo.findById(it) }
                    ?.get()?.lastName,

                returnUsername(p.id.toString()),
                p.id.let { findHofFeedbackDetails(it.toString())?.createdOn },
                p.id.let { findHofFeedbackDetails(it.toString())?.sdOutput },
                p.id.let { findHofFeedbackDetails(it.toString())?.sdResult },
                p.id.let { findHofFeedbackDetails(it.toString())?.reason },


                )
        }
    }

    fun decisionOnNWI(voteOnNWI: VoteOnNWI): ServerResponse {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        voteOnNWI.userId = loggedInUser.id!!

        //        //check if person has voted
        voteOnNWIRepository.findByUserIdAndNwiIdAndStatus(voteOnNWI.userId, voteOnNWI.nwiId, 1)
            ?.let {
                // throw InvalidValueException("You Have Already Voted")
                return ServerResponse(
                    HttpStatus.OK,
                    "Voted", "You Have Already Voted"
                )

            }
            ?: run {
                voteOnNWI.createdOn = Timestamp(System.currentTimeMillis())
                voteOnNWI.status = 1
                voteOnNWIRepository.save(voteOnNWI)
                return ServerResponse(
                    HttpStatus.OK,
                    "Voted", "You Have Voted"
                )

            }
    }

    fun decisionOnNWIVoteOnBehalf(voteOnNWI: VoteOnNWI): ServerResponse {
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        //        //check if person has voted
        voteOnNWIRepository.findByUserIdAndNwiIdAndStatus(voteOnNWI.userId, voteOnNWI.nwiId, 1)
            ?.let {
                // throw InvalidValueException("You Have Already Voted")
                return ServerResponse(
                    HttpStatus.OK,
                    "Voted", "This Member Has Already Voted"
                )

            }
            ?: run {
                voteOnNWI.votedOnBehalfBy = loggedInUser.id!!
                voteOnNWI.votedOnBehalfStatus = 1
                voteOnNWI.createdOn = Timestamp(System.currentTimeMillis())
                voteOnNWI.status = 1
                voteOnNWIRepository.save(voteOnNWI)
                return ServerResponse(
                    HttpStatus.OK,
                    "Voted", "You Have Voted"
                )

            }
    }

    fun reVoteOnNWI(voteOnNWI: VoteOnNWI): ServerResponse {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        voteOnNWI.userId = loggedInUser.id!!


        val u: VoteOnNWI = voteOnNWIRepository.findById(voteOnNWI.id).orElse(null)

        u.decision = voteOnNWI.decision
        u.createdOn = Timestamp(System.currentTimeMillis())
        u.modifiedOn = Timestamp(System.currentTimeMillis())
        u.reason = voteOnNWI.reason

        voteOnNWIRepository.save(u)
        return ServerResponse(
            HttpStatus.OK,
            "Voted", "You Vote Has Been Changed"
        )

    }


    fun getUserLoggedInBallots(): List<VotesDto> {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val voteOnNWI: List<VoteOnNWI> =
            voteOnNWIRepository.findByUserIdAndStatusOrderByIdDesc(loggedInUser.id!!, 1)
        return voteOnNWI.map { p ->
            val fullName = if (p.votedOnBehalfBy != null) {
                val user = usersRepo.findByIdOrNull(p.votedOnBehalfBy)
                "${user?.firstName} ${user?.lastName}"
            } else {
                null
            }

            VotesDto(
                p.id,
                p.decision,
                p.reason,
                p.createdOn,
                p.nwiId,
                standardNWIRepository.findByIdOrNull(p.nwiId)?.proposalTitle,
                standardNWIRepository.findByIdOrNull(p.nwiId)?.closingDate,
                standardNWIRepository.findByIdOrNull(p.nwiId)?.standardId,
                standardNWIRepository.findByIdOrNull(p.nwiId)?.standardId?.let {
                    standardRequestRepository.findByIdOrNull(it)?.requestNumber
                },
                p.votedOnBehalfBy,
                p.votedOnBehalfStatus,
                fullName
            )
        }
    }


    fun getAllVotesTally(): List<NwiVotesTally> {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        return voteOnNWIRepository.getVotesTally(loggedInUser.id.toString())

    }

    fun getAllVotesTallyOtherTcMembers(): List<NwiVotesTally> {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val uList: List<TcUserAssignment>? = tcUserAssignmentRepository.findByUserId(loggedInUser.id!!)
        val voteTallies: MutableList<NwiVotesTally> = mutableListOf()
        if (uList != null) {
            for (u in uList) {
                val voteTallyList: List<NwiVotesTally> =
                    voteOnNWIRepository.getVotesTallyLoggedInMembers(u.tcId.toString())
                voteTallies.addAll(voteTallyList)
            }
        }
        return voteTallies
    }

    fun getAllVotesOnNwi(nwiId: Long): List<VotesWithNWIId> {
        return voteOnNWIRepository.getVotesByTcMembers(nwiId)
    }


    fun getVotingStatus(): ServerResponse {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        return if(tcUserAssignmentRepository.findByTcIdAndPrincipal(loggedInUser.id!!, "1") !=null) {
            ServerResponse(
                HttpStatus.OK,
                "Approved", "Can Vote"
            )
        } else {

            ServerResponse(
                HttpStatus.OK,
                "Approved", "Cannot vote"
            )
        }
    }


    fun approveNWI(nwiId: Long): ServerResponse {
        val u: StandardNWI = standardNWIRepository.findById(nwiId).orElse(null)
        u.status = "Upload Justification"
        standardNWIRepository.save(u)
        val standardRequestToUpdate = u.standardId?.let {
            standardRequestRepository.findById(it)
                .orElseThrow { RuntimeException("No Standard Request found") }
        }
        if (standardRequestToUpdate != null) {
            standardRequestToUpdate.ongoingStatus = "New Work Item Approved For Justification"
            standardRequestRepository.save(standardRequestToUpdate)

        }
        return ServerResponse(
            HttpStatus.OK,
            "Approved", "NWI Approved. Prepare Justification"
        )
    }

    fun rejectNWI(nwiId: Long): ServerResponse {
        val u: StandardNWI = standardNWIRepository.findById(nwiId).orElse(null)
        u.status = "NWI Rejected"
        standardNWIRepository.save(u)
        val standardRequestToUpdate = u.standardId?.let {
            standardRequestRepository.findById(it)
                .orElseThrow { RuntimeException("No Standard Request found") }
        }
        if (standardRequestToUpdate != null) {
            standardRequestToUpdate.ongoingStatus = "New Work Item Rejected For Justification"
            standardRequestRepository.save(standardRequestToUpdate)

        }
        return ServerResponse(
            HttpStatus.OK,
            "Rejected", "NWI Rejected."
        )
    }

    fun deferNWI(standardNWI: StandardNWI): ServerResponse {
        val u: StandardNWI = standardNWIRepository.findById(standardNWI.id).orElse(null)
        u.status = "NWI Deferred"
        val dateString = standardNWI.deferredDate.toString() // Date string to convert
        println(standardNWI.deferredDate.toString())
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss") // Date format
        val date = format.parse(dateString) // Parse the date string into a Date object
        val timestamp = Timestamp(date.time) // Create a Timestamp object using the time from the Date object
        u.deferredDate = timestamp
        standardNWIRepository.save(u)
        val standardRequestToUpdate = u.standardId?.let {
            standardRequestRepository.findById(it)
                .orElseThrow { RuntimeException("No Standard Request found") }
        }
        if (standardRequestToUpdate != null) {
            standardRequestToUpdate.ongoingStatus = "New Work Item Deferred"
            standardRequestRepository.save(standardRequestToUpdate)

        }
        return ServerResponse(
            HttpStatus.OK,
            "Deferred", "NWI Deferred."
        )
    }


    fun getAllNwiSApprovedForJustification(): List<StandardNWI> {
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        return standardNWIRepository.findAllByStatusAndProcessStatusIsNullAndTcSec(
            "Upload Justification",
            loggedInUser.id.toString()
        )
    }


    fun getTCSecTasks(): List<TaskDetails> {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_TC_SEC).list()
        return getTaskDetails(tasks)
    }


    fun uploadJustification(standardJustification: StandardJustification): ProcessInstanceResponseValue {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        standardJustification.createdOn = Timestamp(System.currentTimeMillis())
        standardJustification.createdBy = loggedInUser.id.toString()
        standardJustification.status = "Justification Created. Awaiting Decision"
        standardJustificationRepository.save(standardJustification)
        val u: StandardNWI = standardNWIRepository.findById(standardJustification.nwiId!!.toLong()).orElse(null)
        u.processStatus = "Approve/Reject Justification"
        standardNWIRepository.save(u)
        val standardRequestToUpdate = u.standardId?.let {
            standardRequestRepository.findById(it)
                .orElseThrow { RuntimeException("No Standard Request found") }
        }
        if (standardRequestToUpdate != null) {
            standardRequestToUpdate.ongoingStatus = "Justification Created. Awaiting Decision"
            standardRequestRepository.save(standardRequestToUpdate)

        }
        return ProcessInstanceResponseValue(standardJustification.id, "Complete", true, "justification")


    }

    fun assignTcSec(technicalCommittee: TechnicalCommittee): ProcessInstanceResponseValue {
        val u: TechnicalCommittee = technicalCommitteeRepository.findById(technicalCommittee.id).orElse(null)
        u.userId = technicalCommittee.userId
        technicalCommitteeRepository.save(u)
        return ProcessInstanceResponseValue(technicalCommittee.id, "Complete", true, "assignment")

    }

    fun assignHof(department: Department): ProcessInstanceResponseValue {
        val u: Department = departmentRepository.findById(department.id).orElse(null)
        u.userId = department.userId
        val user: UsersEntity = usersRepo.findById(u.userId?.toLong() ?: -1).orElse(null)
        u.varField3 = user.firstName + " " + user.lastName
        departmentRepository.save(u)
        return ProcessInstanceResponseValue(department.id, "Complete", true, "assignment")

    }

    fun assignTcMembers(tcUserAssignments: List<TcUserAssignment>): ProcessInstanceResponseValue {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val currentTime = Timestamp(System.currentTimeMillis())

        val updatedTcUserAssignments = tcUserAssignments.map { tcUserAssignment ->
            tcUserAssignment.apply {
                createdOn = currentTime
                createdBy = loggedInUser.id.toString()
                status = 1
            }
        }

        tcUserAssignmentRepository.saveAll(updatedTcUserAssignments)

        return ProcessInstanceResponseValue(null, "Complete", true, "assignment")
    }


    fun getSPCSecTasks(): List<TaskDetails> {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_SPC_SEC).list()
        return getTaskDetails(tasks)
    }

    fun decisionOnJustification(decisionJustification: DecisionJustification) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        decisionJustification.createdOn = Timestamp(System.currentTimeMillis())
        decisionJustification.spcId = loggedInUser.id.toString()

        val j: StandardJustification? = decisionJustification.justificationId?.let {
            standardJustificationRepository.findById(it.toLong()).orElse(null)
        }
        if (j != null) {
            if (decisionJustification.decision.equals("Approved")) {
                j.status = "Justification Approved"
                val u: StandardNWI = standardNWIRepository.findById(j.nwiId!!.toLong()).orElse(null)
                u.processStatus = "Prepare Minutes and Drafts For Preliminary Draft"
                u.pdStatus = "Prepare Minutes and Drafts For Preliminary Draft"
                val cdNumber = "CD/" + u.referenceNumber
                j.cdNumber = cdNumber
                standardJustificationRepository.save(j)
                standardNWIRepository.save(u)
                val standardRequestToUpdate = u.standardId?.let {
                    standardRequestRepository.findById(it)
                        .orElseThrow { RuntimeException("No Standard Request found") }
                }
                if (standardRequestToUpdate != null) {
                    standardRequestToUpdate.ongoingStatus = "Prepare Minutes and Drafts For Preliminary Draft"
                    standardRequestRepository.save(standardRequestToUpdate)

                }

            }
            if (decisionJustification.decision.equals("Rejected")) {
                j.status = "Justification Declined"
                standardJustificationRepository.save(j)
            }
            if (decisionJustification.decision.equals("Deffered With Amendments")) {
                j.status = "Justification Deferred With Amendments"
                standardJustificationRepository.save(j)
            }
        }
        decisionJustificationRepository.save(decisionJustification)


    }

    fun getJustificationsPendingDecision(): List<StandardJustification> {
        return standardJustificationRepository.findByStatusOrderByIdAsc(
            "Justification Created. Awaiting Decision",

            )
    }

    fun getAllMyJustifications(): List<StandardJustification> {
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        return standardJustificationRepository.findByTcSecretary(
            loggedInUser.id.toString()
        )
    }

    fun getApprovedJustifications(): List<StandardJustification> {
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        return standardJustificationRepository.findByStatusAndTcSecretary(
            "Justification Approved",
            loggedInUser.id.toString()
        )
    }

    fun getRejectedJustifications(): List<StandardJustification> {
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        return standardJustificationRepository.findByStatusAndTcSecretary(
            "Justification Declined",
            loggedInUser.id.toString()
        )
    }

    fun getRejectedAmendmentJustifications(): List<StandardJustification> {
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        return standardJustificationRepository.findByStatusAndTcSecretary(
            "Justification Deferred With Amendments",
            loggedInUser.id.toString()
        )
    }

    fun getJustificationByNwiId(nwiId: Long): List<StandardJustification> {
        return standardJustificationRepository.findByNwiId(nwiId.toString())
    }


    fun getJustificationDecisionById(justificationId: Long): List<DecisionJustification> {
        return decisionJustificationRepository.findAllByJustificationId(justificationId.toString())
    }


    fun getTCSecTasksWorkPlan(): List<TaskDetails> {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_TC_SEC).list()
        return getTaskDetails(tasks)
    }

    fun uploadWorkPlan(standardWorkPlan: StandardWorkPlan) {
        standardWorkPlan.status = "Prepare Minutes and Drafts For Preliminary Draft"
        standardWorkPlanRepository.save(standardWorkPlan)
        val u: StandardNWI = standardNWIRepository.findById(standardWorkPlan.id).orElse(null)
        u.processStatus = "Prepare Minutes and Drafts For Preliminary Draft"
        standardNWIRepository.save(u)

    }


    fun getTechnicalCommitteeName(id: Long?): String {
        return technicalCommitteeRepository.findNameById(id)
    }

    fun getProducts(id: Long?): MutableList<Product> {
        return productRepository.findByTechnicalCommitteeId(id)
    }

    fun getDepartments(): MutableList<Department> {
        return departmentRepository.findAll()
    }

    fun getLiaisonOrganization(): MutableList<LiaisonOrganization> {
        return liaisonOrganizationRepository.findAll()
    }

    fun generateSRNumber(departmentAbbrv: String?): String {
        val allRequests = standardRequestRepository.findAllByOrderByIdDesc()
        var lastId: String? = "0"
        var finalValue = 1

        for (item in allRequests) {
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

        return "$departmentAbbrv/$finalValue:$year"
    }

    fun getTechnicalCommittee(id: Long?): MutableList<TechnicalCommittee> {
        return technicalCommitteeRepository.findByDepartmentId(id)
    }

    fun getTechnicalCommitteeSec(tcId: Long?): List<DataHolder> {
        return technicalCommitteeRepository.findTcSecQuery(tcId)
    }

    fun getDepartment(id: Long): MutableList<Department> {
        return departmentRepository.findAllById(id)
    }


    /*fun getProductCategories(id:ID): MutableList<ProductSubCategory>
    {
        return productSubCategoryRepository.findAll()
    }*/

    fun getProductCategories(id: Long?): MutableList<ProductSubCategory> {
        return productSubCategoryRepository.findByProductId(id)
    }

    fun getAllTcSec(): MutableList<UsersEntity> {
        val users: MutableList<UsersEntity> = ArrayList()

        userRolesRepo.findByRoleNameAndStatus("TC_SEC_SD", 1)
            ?.let { role ->
                userRolesAssignRepo.findByRoleIdAndStatus(role.id, 1)
                    ?.let { roleAssigns ->
                        roleAssigns.forEach { roleAssign ->
                            usersRepo.findByIdOrNull(roleAssign.userId)
                                ?.let { user ->
                                    users.add(user)
                                }
                        }
                    }
                    ?: throw Exception("Role [id=${role.id}] not found, may not be active or assigned yet")

            }
            ?: throw Exception("User role name does not exist")
        return users
    }

    fun getTcPrincipalMembersToVote(nwiId: Long): List<UsersEntity> {
        val u: StandardNWI = standardNWIRepository.findById(nwiId).orElse(null)

        return u.tcId?.toLong()?.let { tcId ->
            tcUserAssignmentRepository.findByTcIdAndPrincipal(tcId, "1")?.mapNotNull { tc ->
                usersRepo.findByIdOrNull(tc.userId)
            }
        } ?: emptyList()
    }

    fun getTcMembers(): MutableList<UsersEntity> {
        val users: MutableList<UsersEntity> = ArrayList()

        userRolesRepo.findByRoleNameAndStatus("TC_SD", 1)
            ?.let { role ->
                userRolesAssignRepo.findByRoleIdAndStatus(role.id, 1)
                    ?.let { roleAssigns ->
                        roleAssigns.forEach { roleAssign ->
                            usersRepo.findByIdOrNull(roleAssign.userId)
                                ?.let { user ->
                                    users.add(user)
                                }
                        }
                    }
                    ?: throw Exception("Role [id=${role.id}] not found, may not be active or assigned yet")

            }
            ?: throw Exception("User role name does not exist")
        return users
    }

    fun getAllHofs(): MutableList<UsersEntity> {
        val users: MutableList<UsersEntity> = ArrayList()

        userRolesRepo.findByRoleNameAndStatus("HOF_SD", 1)
            ?.let { role ->
                userRolesAssignRepo.findByRoleIdAndStatus(role.id, 1)
                    ?.let { roleAssigns ->
                        roleAssigns.forEach { roleAssign ->
                            usersRepo.findByIdOrNull(roleAssign.userId)
                                ?.let { user ->
                                    users.add(user)
                                }
                        }
                    }
                    ?: throw Exception("Role [id=${role.id}] not found, may not be active or assigned yet")

            }
            ?: throw Exception("User role name does not exist")
        return users
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
                activity.activityId + " took " + activity.durationInMillis + " milliseconds" + activity.processInstanceId
            )
        }

        return activities

    }


    fun closeTask(taskId: String) {
        taskService.complete(taskId)
        taskService.deleteTask(taskId, true)

    }

    fun closeProcess(taskId: String) {
        // taskService.complete(taskId)
        // taskService.deleteTask(taskId, true)

        runtimeService.deleteProcessInstance(taskId, "cleaning")
    }

    //create department
    fun createStandardsDepartment(department: Department) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        department.createdOn = Timestamp(System.currentTimeMillis())
        department.status = 1
        department.createdBy = loggedInUser.id.toString()

        departmentRepository.save(department)
    }

    //create technicalCommittee
    fun createTechnicalCommittee(technicalCommittee: TechnicalCommittee) {
//        val  department_id = departmentRepository.findByName(variable.put("name").toString())
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        technicalCommittee.createdOn = Timestamp(System.currentTimeMillis())
        technicalCommittee.status = 1.toString()
        technicalCommittee.createdBy = loggedInUser.id.toString()

        technicalCommitteeRepository.save(technicalCommittee)


    }

    //create productCategory
    fun createProductCategory(product: Product) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        product.createdOn = Timestamp(System.currentTimeMillis())
        product.status = 1
        product.createdBy = loggedInUser.id.toString()

        productRepository.save(product)


    }

    //create productSubCategory
    fun createProductSubCategory(productSubCategory: ProductSubCategory) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        productSubCategory.createdOn = Timestamp(System.currentTimeMillis())
        productSubCategory.status = 1
        productSubCategory.createdBy = loggedInUser.id.toString()
        productSubCategoryRepository.save(productSubCategory)


    }

    //get all Departments
    fun getAllDepartments(): MutableList<Department> {
        return departmentRepository.findAll()
    }

    //get all TCs
    fun getAllTcs(): List<DataHolder> {
        return technicalCommitteeRepository.findAllWithDescriptionQuery()
    }

    fun getAllTcsWithMembers(): List<DataHolder> {
        return technicalCommitteeRepository.findAllWithCountQuery()
    }

    fun getTcsWithMembersDetails(technicalCommittee: Long): List<TcMembers> {
        val u: TechnicalCommittee = technicalCommitteeRepository.findById(technicalCommittee).orElse(null)

        return technicalCommitteeRepository.findTechnicalCommitteeMembers(u.id)
    }


    //get all ProductCategory
    fun getAllProductCategories(): List<DataHolder> {
        return productRepository.findAllWithDescriptionQuery()
    }

    //get all productSubCategories
    fun getAllProductSubCategories(): List<DataHolder> {
        return productSubCategoryRepository.findAllWithDescriptionQuery()

    }

    //update Department
    fun updateDepartment(department: Department) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        department.modifiedBy = loggedInUser.id.toString()
        department.modifiedOn = Timestamp(System.currentTimeMillis())
        departmentRepository.save(department)


    }

    //delete department
    fun findPermitBYPermitNumber(permitNumber: Long) {

        val exist: Boolean = technicalCommitteeRepository.existsTechnicalCommitteeByDepartmentId(permitNumber)


        if (!exist) {
            try {
                departmentRepository.deleteById(permitNumber)

            } catch (e: Exception) {
                KotlinLogging.logger { }.error(e.message)
                KotlinLogging.logger { }.debug(e.message, e)
                throw ExpectedDataNotFound("The Department ID Does not Exist")
            }
        } else {
            println("Exists")
        }
    }

    //delete Department
    fun deleteDepartment(department: Long) {

        findPermitBYPermitNumber(department)

    }

    //update technicalCommittee
    fun updateTechnicalCommittee(technicalCommittee: TechnicalCommittee) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        technicalCommittee.createdOn = Timestamp(System.currentTimeMillis())
        technicalCommittee.status = 1.toString()
        technicalCommittee.createdBy = loggedInUser.id.toString()
        technicalCommitteeRepository.save(technicalCommittee)

    }

    //delete technicalCommittee
    fun deleteTechnicalCommittee(technicalCommitteeId: Long) {
        findTechnicalCommitteeById(technicalCommitteeId)
    }

    fun findTechnicalCommitteeById(technicalCommitteeId: Long) {

        val exist: Boolean = productRepository.existsProductByTechnicalCommitteeId(technicalCommitteeId)


        if (!exist) {
            try {
                technicalCommitteeRepository.deleteById(technicalCommitteeId)

            } catch (e: Exception) {
                KotlinLogging.logger { }.error(e.message)
                KotlinLogging.logger { }.debug(e.message, e)
                throw ExpectedDataNotFound("The Technical Committee ID Does not Exist")
            }
        } else {
            println("Exists")
        }
    }


    //update productCategory
    fun updateProductCategory(product: Product) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        product.createdOn = Timestamp(System.currentTimeMillis())
        product.status = 1
        product.createdBy = loggedInUser.id.toString()
        productRepository.save(product)


    }


    //update productSubCategory
    fun updateProductSubCategory(productSubCategory: ProductSubCategory) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        productSubCategory.createdOn = Timestamp(System.currentTimeMillis())
        productSubCategory.status = 1
        productSubCategory.createdBy = loggedInUser.id.toString()

        productSubCategoryRepository.save(productSubCategory)


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

    fun getDocuments(standardId: Long): Collection<DatKebsSdStandardsEntity?>? {

        return draftDocumentService.findUploadedDIFileBYId(standardId)
    }

    fun getDocumentsByProcessName(standardId: Long, processName: String): Collection<DatKebsSdStandardsEntity?>? {

        return draftDocumentService.findUploadedDIFileBYIdAndType(standardId, processName)
    }

    fun findHofFeedbackDetails(sdRequestNumber: String): HOFFeedback? {
        hofFeedbackRepository.findTopBySdRequestIDOrderByIdDesc(sdRequestNumber).let {
            return it
        }
    }

    fun returnUsername(sdRequestNumber: String): String {

        val hofFeedback = findHofFeedbackDetails(sdRequestNumber)
        return if (hofFeedback != null) {
            val user = usersRepo.findByIdOrNull(hofFeedback.reviewedBy!!.toLong())
            user?.firstName + " " + user?.lastName
        } else {
            "Not Assigned"
        }


    }

    fun returnDepartmentName(departmentId: Long): String? {
        val department = departmentRepository.findById(departmentId)

        return if (department.isEmpty) {
            "Not Assigned"
        } else {
            department.get().name
        }


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


        return sdDocumentsRepository.save(uploads)
    }

    fun standardReceivedReports(): MutableList<ReceivedStandards> {
        return standardRequestRepository.getReceivedStandardsReport()
    }

    fun deleteMember(tcAssignmentId: String) {
        val tcMemberExisting: TcUserAssignment =
            tcUserAssignmentRepository.findById(tcAssignmentId.toLong()).orElse(null)
        tcUserAssignmentRepository.deleteById(tcMemberExisting.id)
    }

    fun setAsPrincipal(tcAssignmentId: String) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        //we are checking if this assignment exists first
        val tcMemberExisting: TcUserAssignment =
            tcUserAssignmentRepository.findById(tcAssignmentId.toLong()).orElse(null)

        //lets us check if another user was assigned as 1 in same organisation. We hae found assignment exists
        val assignments: Optional<TcUserAssignment> =
            tcUserAssignmentRepository.findByOrganizationAndTcIdAndPrincipalIsNotNull(
                tcMemberExisting.organization!!,
                tcMemberExisting.tcId
            )
        if (assignments.isPresent) {
            //assignment exists so we remove it
            val nonNullAssignments: TcUserAssignment = assignments.get()
            // Case 1: Non-null assignments
            // Perform actions with the non-null assignments
            nonNullAssignments.principal = null
            nonNullAssignments.modifiedBy = loggedInUser.id.toString()
            nonNullAssignments.modifiedOn = Timestamp(System.currentTimeMillis())
            tcUserAssignmentRepository.save(nonNullAssignments)
            tcMemberExisting.principal = "1"
            tcMemberExisting.modifiedBy = loggedInUser.id.toString()
            tcMemberExisting.modifiedOn = Timestamp(System.currentTimeMillis())
            tcUserAssignmentRepository.save(tcMemberExisting)


        } else {
            // Case 2: Assignments is null or empty
            // Perform actions when assignments is null or empty

            tcMemberExisting.principal = "1"
            tcMemberExisting.modifiedBy = loggedInUser.id.toString()
            tcMemberExisting.modifiedOn = Timestamp(System.currentTimeMillis())
            tcUserAssignmentRepository.save(tcMemberExisting)

        }


    }

}





