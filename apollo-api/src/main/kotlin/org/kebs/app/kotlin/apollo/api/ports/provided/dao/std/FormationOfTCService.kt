package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std


import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.engine.history.HistoricActivityInstance
import org.flowable.engine.repository.Deployment
import org.flowable.task.api.Task
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.dto.std.*
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.std.DecisionFeedbackRepository
import org.kebs.app.kotlin.apollo.store.repo.std.JustificationForTCRepository
import org.kebs.app.kotlin.apollo.store.repo.std.TechnicalCommitteeRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.util.*

@Service
class FormationOfTCService(
    private val runtimeService: RuntimeService,
    private val taskService: TaskService,
    @Qualifier("processEngine") private val processEngine: ProcessEngine,
    private val repositoryService: RepositoryService,
    private val justificationForTCRepository: JustificationForTCRepository,
    private val decisionFeedbackRepository: DecisionFeedbackRepository,
    private val commonDaoServices: CommonDaoServices,
    private val technicalCommitteeRepository: TechnicalCommitteeRepository,

    ) {

    val PROCESS_DEFINITION_KEY = "sd_formation_of_technical_committee"
    val TASK_SPC = "SPC"
    val TASK_SAC = "SAC"

    fun deployProcessDefinition(): Deployment = repositoryService
        .createDeployment()
        .addClasspathResource("processes/std/formation_of_technical_committee.bpmn20.xml")
        .deploy()

    fun submitJustificationForFormationOfTC(justificationForTC: JustificationForTC): ProcessInstanceResponseValue {
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        justificationForTC.createdOn = Timestamp(System.currentTimeMillis())
        justificationForTC.createdBy = loggedInUser.id
        justificationForTC.version = "1"
        justificationForTC.status = 1  //uploaded awaiting decision

        justificationForTCRepository.save(justificationForTC)

        return ProcessInstanceResponseValue(
            justificationForTC.id,
            "Complete",
            true,
            justificationForTC.version ?: throw NullValueNotAllowedException("ID is required")
        )

    }

    fun getAllHofJustifications(): List<JustificationForTC> {
        return justificationForTCRepository.findAll()
    }


    fun approveJustification(justificationForTC: JustificationForTC): ServerResponse {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val u: JustificationForTC = justificationForTCRepository.findById(justificationForTC.id).orElse(null)
        u.status = 2   //approved
        u.hofId = loggedInUser.id
        u.hofReviewDate = Timestamp(System.currentTimeMillis())
        justificationForTCRepository.save(u)
        return ServerResponse(
            HttpStatus.OK,
            "Approved", "Justification Approved."
        )
    }

    fun rejectJustification(justificationForTC: JustificationForTC): ServerResponse {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val u: JustificationForTC = justificationForTCRepository.findById(justificationForTC.id).orElse(null)
        u.status = 3   //rejected
        u.hofId = loggedInUser.id
        u.hofReviewDate = Timestamp(System.currentTimeMillis())
        justificationForTCRepository.save(u)
        return ServerResponse(
            HttpStatus.OK,
            "Rejected", "Justification Rejected. Sent Back To Tc-Sec"
        )
    }

    fun getAllSpcJustifications(): List<JustificationForTC> {
        return justificationForTCRepository.findAllByStatus(2)
    }

    fun getAllJustificationsRejectedBySpc(): List<JustificationForTC> {
        return justificationForTCRepository.findAllByStatus(3)
    }


    fun approveJustificationSPC(justificationForTC: JustificationForTC): ServerResponse {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val u: JustificationForTC = justificationForTCRepository.findById(justificationForTC.id).orElse(null)
        u.status = 4   //approved by SPC
        u.spcId = loggedInUser.id
        u.spcReviewDate = Timestamp(System.currentTimeMillis())

        justificationForTCRepository.save(u)
        return ServerResponse(
            HttpStatus.OK,
            "Approved", "Justification Approved."
        )
    }

    fun rejectJustificationSPC(justificationForTC: JustificationForTC): ServerResponse {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val u: JustificationForTC = justificationForTCRepository.findById(justificationForTC.id).orElse(null)
        u.status = 5   //rejected by SPC
        u.spcId = loggedInUser.id
        u.spcReviewDate = Timestamp(System.currentTimeMillis())

        justificationForTCRepository.save(u)
        return ServerResponse(
            HttpStatus.OK,
            "Rejected", "Justification Rejected."
        )
    }


    fun sacGetAllApprovedJustificationsBySpc(): List<JustificationForTC> {
        return justificationForTCRepository.findAllByStatus(5)
    }

    fun approveJustificationSAC(justificationForTC: JustificationForTC): ServerResponse {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val u: JustificationForTC = justificationForTCRepository.findById(justificationForTC.id).orElse(null)
        u.status = 6   //approved by SAC
        u.sacId = loggedInUser.id
        u.sacReviewDate = Timestamp(System.currentTimeMillis())
        u.tcNumber = generateTCNumber("KEBS")
        justificationForTCRepository.save(u)

        val tc = TechnicalCommittee()
        tc.departmentId = u.departmentId!!
        tc.title = u.nameOfTC
        tc.createdOn = Timestamp(System.currentTimeMillis())
        tc.status = 1.toString()
        tc.createdBy = loggedInUser.id.toString()
        tc.technicalCommitteeNo = u.tcNumber

        technicalCommitteeRepository.save(tc)
        return ServerResponse(
            HttpStatus.OK,
            "Success", "Technical Committee Saved."
        )
    }


    fun advertiseTcToWebsite(justificationForTC: JustificationForTC): ServerResponse {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val u: JustificationForTC = justificationForTCRepository.findById(justificationForTC.id).orElse(null)
        u.status = 7   //advertised To Website
        justificationForTCRepository.save(u)

        //find technical committee

//        val tc:TechnicalCommittee = technicalCommitteeRepository.findAllByOrderByIdDesc()
//        tc.departmentId = u.departmentId!!
//        tc.title = u.nameOfTC
//        tc.createdOn = Timestamp(System.currentTimeMillis())
//        tc.status = 1.toString()
//        tc.createdBy = loggedInUser.id.toString()
//        tc.technicalCommitteeNo = u.tcNumber
//
//        technicalCommitteeRepository.save(tc)
        return ServerResponse(
            HttpStatus.OK,
            "Success", "Technical Committee Saved."
        )
    }


    fun getSPCTasks(): List<TaskDetails> {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_SPC).list()
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


    fun decisionOnJustificationForTC(decisionFeedback: DecisionFeedback) {
        val variables: MutableMap<String, Any> = java.util.HashMap()

        decisionFeedback.user_id?.let { variables.put("user_id", it) }
        decisionFeedback.item_id?.let { variables.put("item_id", it) }
        decisionFeedback.comment?.let { variables.put("comment", it) }
        decisionFeedback.taskId?.let { variables.put("taskId", it) }

        variables["approved"] = decisionFeedback.status!!
        taskService.complete(decisionFeedback.taskId, variables)
    }

    fun uploadFeedbackOnJustification(decisionFeedback: DecisionFeedback) {
        val variable: MutableMap<String, Any> = HashMap()

        decisionFeedback.user_id.let { variable.put("user_id", it) }
        decisionFeedback.item_id?.let { variable.put("item_id", it) }
        decisionFeedback.status?.let { variable.put("status", it) }
        decisionFeedback.comment?.let { variable.put("comment", it) }
        decisionFeedback.taskId?.let { variable.put("taskId", it) }


        print(decisionFeedback.toString())

        decisionFeedbackRepository.save(decisionFeedback)

        taskService.complete(decisionFeedback.taskId)
    }

    fun getSACTasks(): List<TaskDetails> {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_SAC).list()
        return getTaskDetails(tasks)
    }

    fun decisionOnSPCFeedback(decisionFeedback: DecisionFeedback) {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        decisionFeedback.user_id.let { variables.put("user_id", it) }
        decisionFeedback.item_id?.let { variables.put("item_id", it) }
        decisionFeedback.comment?.let { variables.put("comment", it) }
        decisionFeedback.taskId?.let { variables.put("taskId", it) }

        variables["approved"] = decisionFeedback.status!!
        taskService.complete(decisionFeedback.taskId, variables)
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


    fun generateTCNumber(departmentAbbrv: String?): String {
        val allRequests = technicalCommitteeRepository.findAllByOrderByIdDesc()
        var lastId: String? = "0"
        var finalValue = 1
        for (item in allRequests) {
            lastId = item.id.toString()
            break
        }
        if (lastId != "0") {
            finalValue = (lastId?.toInt()!!)
            finalValue += 1
        }
        val year = Calendar.getInstance()[Calendar.YEAR]
        return "$departmentAbbrv/TC/$finalValue:$year"
    }

}
