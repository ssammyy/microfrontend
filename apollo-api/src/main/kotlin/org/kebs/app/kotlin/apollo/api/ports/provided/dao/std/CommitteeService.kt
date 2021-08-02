package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std


import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.task.api.Task
import org.kebs.app.kotlin.apollo.api.errors.std.ResourceNotFoundException
import org.kebs.app.kotlin.apollo.common.dto.std.ProcessInstanceResponse
import org.kebs.app.kotlin.apollo.common.dto.std.TaskDetails
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.std.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable

@Service
class CommitteeService(
    private val runtimeService: RuntimeService,
    private val taskService: TaskService,
    @Qualifier("processEngine") private val processEngine: ProcessEngine,
    private val repositoryService: RepositoryService,
    private val committeeNWIRepository: CommitteeNWIRepository,
    private val committeeDraftsRepository: CommitteeDraftsRepository,
    private val committeePDRepository: CommitteePDRepository,
    private val committeePdDraftsRepository: CommitteePdDraftsRepository,
    private val committeeCDRepository: CommitteeCDRepository,


    ) {
    val PROCESS_DEFINITION_KEY = "committee_stage"
    val TASK_CANDIDATE_GROUP_TC_SEC = "TC-sec"
    val TASK_CANDIDATE_GROUP_TC = "TC"
    val TASK_CANDIDATE_GROUP_HOF_SIC = "HOF-SIC"
    val variable: MutableMap<String, Any> = HashMap()


    fun deployProcessDefinition() {
        repositoryService
            .createDeployment()
            .addClasspathResource("processes/std/committee_stage.bpmn20.xml")
            .deploy()
    }

    fun prepareNWI(committeeNWI: CommitteeNWI): ProcessInstanceResponse {
        committeeNWI.slNo?.let { variable.put("slNo", it) }
        committeeNWI.reference?.let { variable.put("reference", it) }
        committeeNWI.ta?.let { variable.put("ta", it) }
        committeeNWI.ed?.let { variable.put("ed", it) }
        committeeNWI.title?.let { variable.put("title", it) }
        committeeNWI.stage_date?.let { variable.put("stage_date", it) }
        committeeNWI.approved.let { variable.put("approved", it) }

        print(committeeNWI.toString())


        committeeNWIRepository.save(committeeNWI)
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variable)
        return ProcessInstanceResponse(processInstance.id, processInstance.isEnded)

    }

    private fun getTaskDetails(tasks: List<Task>): List<TaskDetails> {
        val taskDetails: MutableList<TaskDetails> = ArrayList()
        for (task in tasks) {
            val processVariables = taskService.getVariables(task.id)
            taskDetails.add(TaskDetails(task.id, task.name, processVariables))
        }
        return taskDetails
    }

    fun getTCSECTasks(): List<TaskDetails> {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_TC_SEC).list()
        return getTaskDetails(tasks)
    }

    fun approveNWI(taskId: String?, approved: Boolean) {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        variables["approved"] = approved
        taskService.complete(taskId, variables)
    }

    fun uploadDrafts(committeeDrafts: CommitteeDrafts, taskId: String?) {
        committeeDrafts.draftName?.let { variable.put("draftName", it) }
        committeeDrafts.draftBy?.let { variable.put("draftBy", it) }
        print(committeeDrafts.toString())

        committeeDraftsRepository.save(committeeDrafts)
        taskService.complete(taskId)
        println("TC-SEC has uploaded draft document")

    }

    fun preparePD(committeePD: CommitteePD): ProcessInstanceResponse {
        committeePD.nwiID?.let { variable.put("nwiID", it) }
        committeePD.pdName?.let { variable.put("pdName", it) }
        committeePD.pdBy?.let { variable.put("pdBy", it) }
        print(committeePD.toString())
        committeePDRepository.save(committeePD)
        println("TC-SEC has prepared Preliminary Draft document")
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variable)
        return ProcessInstanceResponse(processInstance.id, processInstance.isEnded)
    }

    fun uploadDraftsPD(committeeDraftsPD: CommitteeDraftsPD, taskId: String?) {
        committeeDraftsPD.PdDraftName?.let { variable.put("PdDraftName", it) }
        committeeDraftsPD.PddraftBy?.let { variable.put("PddraftBy", it) }
        print(committeeDraftsPD.toString())
        committeePdDraftsRepository.save(committeeDraftsPD)
        taskService.complete(taskId)
        println("TC-SEC has uploaded discussions on Preliminary Draft")


    }

    fun prepareCD(committeeCD: CommitteeCD): ProcessInstanceResponse {
        committeeCD.nwiID?.let { variable.put("nwiID", it) }
        committeeCD.pdID?.let { variable.put("pdID", it) }
        committeeCD.cdName?.let { variable.put("cdName", it) }
        committeeCD.cdBy?.let { variable.put("cdBy", it) }
        print(committeeCD.toString())
        committeeCDRepository.save(committeeCD)
        println("TC-SEC has prepared Preliminary Draft document")
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variable)
        return ProcessInstanceResponse(processInstance.id, processInstance.isEnded)

    }

    //    fun approveCD(committeeCD: CommitteeCD,taskId: String?, approved: Boolean) {
//        val variables: MutableMap<String, Any> = java.util.HashMap()
//        committeeCD.approved?.let { variable.put("approved", it) }
//        variables["approved"] = approved
//        committeeCDRepository.save(committeeCD)
//        taskService.complete(taskId, variables)
//    }
    fun approveCD(taskId: String?, approved: Boolean) {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        variables["approved"] = approved
        taskService.complete(taskId, variables)
    }

    fun getNWIs(): MutableList<CommitteeNWI> {
        return committeeNWIRepository.findAll()
    }

    fun getPds(): MutableList<CommitteePD> {
        return committeePDRepository.findAll()
    }

    fun getCds(): MutableList<CommitteeCD> {
        return committeeCDRepository.findAll()
    }

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

    fun getPreliminaryDraftById(@PathVariable(value = "id") preliminaryDraftId: Long): ResponseEntity<CommitteePD?>? {
        val pd: CommitteePD = committeePDRepository.findById(preliminaryDraftId)
            .orElseThrow { ResourceNotFoundException("PD not found for this id :: $preliminaryDraftId") }
        return ResponseEntity.ok().body<CommitteePD?>(pd)
    }

}
