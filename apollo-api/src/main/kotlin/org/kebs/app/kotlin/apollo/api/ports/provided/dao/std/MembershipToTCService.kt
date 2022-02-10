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
import org.kebs.app.kotlin.apollo.common.dto.std.ProcessInstanceResponse
import org.kebs.app.kotlin.apollo.common.dto.std.TaskDetails
import org.kebs.app.kotlin.apollo.store.model.std.CallForTCApplication
import org.kebs.app.kotlin.apollo.store.model.std.DecisionFeedback
import org.kebs.app.kotlin.apollo.store.model.std.MembershipTCApplication
import org.kebs.app.kotlin.apollo.store.model.std.TechnicalCommitteMember
import org.kebs.app.kotlin.apollo.store.repo.std.CallForApplicationTCRepository
import org.kebs.app.kotlin.apollo.store.repo.std.DecisionFeedbackRepository
import org.kebs.app.kotlin.apollo.store.repo.std.MembershipTCRepository
import org.kebs.app.kotlin.apollo.store.repo.std.TechnicalCommitteMemberRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.set

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

    fun submitCallsForTCMembers(callForTCApplication: CallForTCApplication): ProcessInstanceResponse {
        val variable: MutableMap<String, Any> = HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        callForTCApplication.tc?.let { variable.put("tc", it) }
        callForTCApplication.tcId?.let { variable.put("tcId", it) }
        callForTCApplication.title?.let { variable.put("title", it) }
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
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variable)
        //print(variables)

        //val getProcessInstance = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(PROCESS_DEFINITION_KEY).singleResult()

//        val gottenVariables = processIntance.body
//
//        println(gottenVariables)

        return ProcessInstanceResponse(processInstance.id, processInstance.isEnded)

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

    fun getCallForApplications(): List<TaskDetails> {
        val tasks = taskService.createTaskQuery().active().taskCandidateGroup(APPLICANTS).list()
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

    fun submitTCMemberApplication(membershipTCApplication: MembershipTCApplication)
    {
        val variable:MutableMap<String, Any> = HashMap()
        membershipTCApplication.technicalCommittee?.let{variable.put("technicalCommittee", it)}
        membershipTCApplication.organization?.let{variable.put("organization", it)}
        membershipTCApplication.nomineeName?.let{variable.put("nomineeName", it)}
        membershipTCApplication.position?.let{variable.put("position", it)}
        membershipTCApplication.postalAddress?.let{variable.put("postalAddress", it)}
        membershipTCApplication.mobileNumber?.let{variable.put("mobileNumber", it)}
        membershipTCApplication.email?.let{variable.put("email", it)}
        membershipTCApplication.authorizingName?.let{variable.put("authorizingName", it)}
        membershipTCApplication.authorisingPersonPosition?.let{variable.put("authorisingPersonPosition", it)}
        membershipTCApplication.authorisingPersonEmail?.let{variable.put("authorisingPersonEmail", it)}
        membershipTCApplication.qualifications?.let{variable.put("qualifications", it)}
        membershipTCApplication.commitment?.let{variable.put("commitment", it)}

        println(membershipTCApplication.toString())
        membershipTCRepository.save(membershipTCApplication)

        taskService.complete(membershipTCApplication.taskId,variable)
        println("Applicant has uploaded application")
    }

    fun getApplicationsForReview():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(HOF).list()
        return getTaskDetails(tasks)
    }

    fun decisionOnApplicantRecommendation(decisionFeedback: DecisionFeedback)
    {
        val variables: MutableMap<String, Any> = java.util.HashMap()

        decisionFeedback.user_id.let{variables.put("user_id", it)}
        decisionFeedback.item_id?.let{variables.put("item_id", it)}
        decisionFeedback.comment?.let{variables.put("comment", it)}
        decisionFeedback.taskId?.let{variables.put("taskId", it)}

        variables["readvertise"] = decisionFeedback.status!!
        taskService.complete(decisionFeedback.taskId, variables)

        decisionFeedbackRepository.save(decisionFeedback)
    }

    fun getRecommendationsFromHOF():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(SPC).list()
        return getTaskDetails(tasks)
    }

    fun completeSPCReview(taskId: String)
    {
        taskService.complete(taskId)
    }

    fun getRecommendationsFromSPC():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(SAC).list()
        return getTaskDetails(tasks)
    }

    fun decisionOnSPCRecommendation(decisionFeedback: DecisionFeedback)
    {
        val variables: MutableMap<String, Any> = java.util.HashMap()

        decisionFeedback.user_id?.let{variables.put("user_id", it)}
        decisionFeedback.item_id?.let{variables.put("item_id", it)}
        decisionFeedback.comment?.let{variables.put("comment", it)}
        decisionFeedback.taskId?.let{variables.put("taskId", it)}

        variables["approve"] = decisionFeedback.status!!
        taskService.complete(decisionFeedback.taskId, variables)

        decisionFeedbackRepository.save(decisionFeedback)
    }

    fun getTCMemberCreationTasks():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(HOD_SIC).list()
        return getTaskDetails(tasks)
    }

    fun saveTCMember(technicalCommitteMember: TechnicalCommitteMember)
    {
        val variable:MutableMap<String, Any> = HashMap()
        technicalCommitteMember.userId.let{variable.put("userId", it)}
        technicalCommitteMember.tc?.let{variable.put("tc", it)}
        technicalCommitteMember.name?.let{variable.put("name", it)}
        technicalCommitteMember.email?.let{variable.put("email", it)}

        println(technicalCommitteMember.toString())
        technicalCommitteMemberRepository.save(technicalCommitteMember)

        taskService.complete(technicalCommitteMember.taskId,variable)
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
                    activity.activityId + " took " + activity.durationInMillis + " milliseconds")
        }

        return activities

    }

}
