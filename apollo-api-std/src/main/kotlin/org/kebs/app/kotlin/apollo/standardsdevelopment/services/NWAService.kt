package org.kebs.app.kotlin.apollo.standardsdevelopment.services

import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.engine.repository.Deployment
import org.flowable.task.api.Task
import org.kebs.app.kotlin.apollo.standardsdevelopment.dto.ProcessInstanceResponse
import org.kebs.app.kotlin.apollo.standardsdevelopment.dto.TaskDetails
import org.kebs.app.kotlin.apollo.standardsdevelopment.models.*
import org.kebs.app.kotlin.apollo.standardsdevelopment.repositories.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class NWAService(private val runtimeService: RuntimeService,
                 private val taskService: TaskService,
                 @Qualifier("processEngine") private val processEngine: ProcessEngine,
                 private val repositoryService: RepositoryService,
                 private val nwaJustificationRepository: NWAJustificationRepository,
                 private val nwaDisDtJustificationRepository: NWADISDTJustificationRepository,
                 private val nwaPreliminaryDraftRepository: NWAPreliminaryDraftRepository,
                 private val technicalCommitteeRepository: TechnicalCommitteeRepository,
                 private val departmentRepository: DepartmentRepository,
                 private val nWAWorkShopDraftRepository: NWAWorkShopDraftRepository,
                 private val nWAStandardRepository : NWAStandardRepository,
                 private val nWAGazettementRepository : NWAGazettementRepository,
                 private val nWAGazetteNoticeRepository : NWAGazetteNoticeRepository


) {

    val PROCESS_DEFINITION_KEY = "sd_KenyaNationalWorkshopAgreementModule"
    val TASK_CANDIDATE_KNW_SEC ="KNW_SEC"
    val TASK_CANDIDATE_SPC_SEC ="SPC_SEC"
    val TASK_CANDIDATE_DI_SDT ="DI_SDT"
    val TASK_CANDIDATE_HOP ="HOP"
    val TASK_CANDIDATE_SAC_SEC ="SAC_SEC"
    val TASK_CANDIDATE_HO_SIC ="HO_SIC"

    //deploy bpmn file
    fun deployProcessDefinition(): Deployment =repositoryService
        .createDeployment()
        .addClasspathResource("processes/Kenya_National_Workshop_Agreement_Module.bpmn20.xml")
        .deploy()

    //start the process by process Key
    fun startProcessByKey() :ProcessInstanceResponse
    {

        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY)
        return ProcessInstanceResponse(processInstance.id, processInstance.isEnded)
    }

    //*** Not used *** but closes any Task, linked to task close endpoint
    fun closeTask(taskId: String) {
        taskService.complete(taskId)
    }

    fun getKNWDepartments(): MutableList<Department>
    {
        return departmentRepository.findAll()
    }

    fun getKNWCommittee(): MutableList<TechnicalCommittee>
    {
        return technicalCommitteeRepository.findAll()
    }

    fun startProcessInstance (): ProcessInstanceResponse{
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY)
        return ProcessInstanceResponse(processInstance.id, processInstance.isEnded)
    }

    //prepare justification
    fun prepareJustification(nwaJustification: NWAJustification)
    {
        val variables: MutableMap<String, Any> = HashMap()
        nwaJustification.meetingDate?.let{ variables.put("meetingDate", it)}
        nwaJustification.knw?.let{ variables.put("knw", it)}
        nwaJustification.knwSecretary?.let{ variables.put("knwSecretary", it)}
        nwaJustification.sl?.let{ variables.put("sl", it)}
        nwaJustification.requestNumber?.let{ variables.put("requestNumber", it)}
        nwaJustification.requestedBy?.let{ variables.put("requestedBy", it)}
        nwaJustification.issuesAddressed?.let{ variables.put("issuesAddressed", it)}
        nwaJustification.knwAcceptanceDate?.let{ variables.put("knwAcceptanceDate", it)}
        nwaJustification.referenceMaterial?.let{ variables.put("referenceMaterial", it)}
        nwaJustification.department?.let{ variables.put("department", it)}
        nwaJustification.status?.let{ variables.put("status", it)}
        nwaJustification.remarks?.let{ variables.put("remarks", it)}


        print(nwaJustification.toString())

        nwaJustificationRepository.save(nwaJustification)
        taskService.complete(nwaJustification.taskId, variables)
        println("Justification Prepared")

//        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables)
//        return ProcessInstanceResponse(processInstance.id, processInstance.isEnded)

    }
    //Function to retrieve task details for any candidate group
    private fun getTaskDetails(tasks: List<Task>): List<TaskDetails> {
        val taskDetails: MutableList<TaskDetails> = ArrayList()
        for (task in tasks) {
            val processVariables = taskService.getVariables(task.id)
            taskDetails.add(TaskDetails(task.id, task.name, processVariables))

        }
        return taskDetails
    }

    //Return task details for KNW_SEC
    fun getKNWTasks():List<TaskDetails>
    {

        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_KNW_SEC).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    //Return task details for SPC_SEC
    fun getSPCSECTasks():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_SPC_SEC).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }


   // Decision
    fun decisionOnJustification(nwaJustification: NWAJustification)
    {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        variables["Yes"] = nwaJustification.approved
        taskService.complete(nwaJustification.taskId, variables)
    }



    // Prepare Justification for DI-SDT approval
    fun prepareDisDtJustification(nwaDiSdtJustification: NWADiSdtJustification)
    {
        val variable:MutableMap<String, Any> = HashMap()
        nwaDiSdtJustification.cost?.let{variable.put("cost", it)}
        nwaDiSdtJustification.numberOfMeetings?.let{variable.put("numberOfMeetings", it)}
        nwaDiSdtJustification.identifiedNeed?.let{variable.put("identifiedNeed", it)}
        nwaDiSdtJustification.dateApprovalMade?.let{variable.put("dateApprovalMade", it)}


        print(nwaDiSdtJustification.toString())

        nwaDisDtJustificationRepository.save(nwaDiSdtJustification)
        taskService.complete(nwaDiSdtJustification.taskId, variable)
        println("Justification for DI-SDT prepared")

    }

    //Return task details for SPC_SEC
    fun getDISDTTasks():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_DI_SDT).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    //Decision on DI-SDT
    fun decisionOnDiSdtJustification(nwaDiSdtJustification: NWADiSdtJustification)
    {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        variables["Yes"] = nwaDiSdtJustification.approved
        taskService.complete(nwaDiSdtJustification.taskId, variables)
    }

    // Prepare Preliminary Draft
    fun preparePreliminaryDraft(nwaPreliminaryDraft: NWAPreliminaryDraft)
    {
        val variable:MutableMap<String, Any> = HashMap()
        nwaPreliminaryDraft.title?.let{variable.put("title", it)}
        nwaPreliminaryDraft.scope?.let{variable.put("scope", it)}
        nwaPreliminaryDraft.normativeReference?.let{variable.put("normativeReference", it)}
        nwaPreliminaryDraft.symbolsAbbreviatedTerms?.let{variable.put("symbolsAbbreviatedTerms", it)}
        nwaPreliminaryDraft.clause?.let{variable.put("clause", it)}
        nwaPreliminaryDraft.special?.let{variable.put("special", it)}

        print(nwaPreliminaryDraft.toString())

        nwaPreliminaryDraftRepository.save(nwaPreliminaryDraft)
        taskService.complete(nwaPreliminaryDraft.taskId, variable)
        println("Preliminary Draft Prepared")

    }

    //Decision on Preliminary Draft
    fun decisionOnPD(nwaPreliminaryDraft: NWAPreliminaryDraft)
    {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        variables["Yes"] = nwaPreliminaryDraft.approved
        taskService.complete(nwaPreliminaryDraft.taskId, variables)
    }

    //Return task details for Head of Publishing
    fun getHOPTasks():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_HOP).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    // Edit Workshop  Draft
    fun editWorkshopDraft(nwaWorkShopDraft: NWAWorkShopDraft)
    {
        val variable:MutableMap<String, Any> = HashMap()
        nwaWorkShopDraft.title?.let{variable.put("title", it)}
        nwaWorkShopDraft.scope?.let{variable.put("scope", it)}
        nwaWorkShopDraft.normativeReference?.let{variable.put("normativeReference", it)}
        nwaWorkShopDraft.symbolsAbbreviatedTerms?.let{variable.put("symbolsAbbreviatedTerms", it)}
        nwaWorkShopDraft.clause?.let{variable.put("clause", it)}
        nwaWorkShopDraft.special?.let{variable.put("special", it)}

        print(nwaWorkShopDraft.toString())

        nWAWorkShopDraftRepository.save(nwaWorkShopDraft)
        taskService.complete(nwaWorkShopDraft.taskId, variable)
        println("Workshop Draft Edited")

    }

    //Return task details for SAC SEC
    fun getSacSecTasks():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_SAC_SEC).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    //Decision on WorkShop Draft
    fun decisionOnWD(nwaWorkShopDraft: NWAWorkShopDraft)
    {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        variables["Yes"] = nwaWorkShopDraft.approved
        taskService.complete(nwaWorkShopDraft.taskId, variables)
    }

    // Upload NWA Standard
    fun uploadNwaStandard(nWAStandard: NWAStandard)
    {
        val variable:MutableMap<String, Any> = HashMap()
        nWAStandard.title?.let{variable.put("title", it)}
        nWAStandard.scope?.let{variable.put("scope", it)}
        nWAStandard.normativeReference?.let{variable.put("normativeReference", it)}
        nWAStandard.symbolsAbbreviatedTerms?.let{variable.put("symbolsAbbreviatedTerms", it)}
        nWAStandard.clause?.let{variable.put("clause", it)}
        nWAStandard.special?.let{variable.put("special", it)}
        nWAStandard.ksNumber?.let{variable.put("ksNumber", it)}

        print(nWAStandard.toString())

        nWAStandardRepository.save(nWAStandard)
        taskService.complete(nWAStandard.taskId, variable)
        println("NWA Standard Uploaded")

    }

    //Return task details for HO SIC
    fun getHoSiCTasks():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_HO_SIC).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    // Upload NWA Gazette notice on Website
    fun uploadGazetteNotice(nWAGazetteNotice: NWAGazetteNotice)
    {
        val variable:MutableMap<String, Any> = HashMap()
        nWAGazetteNotice.ksNumber?.let{variable.put("ksNumber", it)}
        nWAGazetteNotice.dateUploaded?.let{variable.put("dateUploaded", it)}
        nWAGazetteNotice.description?.let{variable.put("description", it)}

        print(nWAGazetteNotice.toString())

        nWAGazetteNoticeRepository.save(nWAGazetteNotice)
        taskService.complete(nWAGazetteNotice.taskId, variable)
        println("NWA Gazette Notice has been uploaded")

    }

    // Upload NWA Gazette date
    fun updateGazettementDate(nWAGazettement: NWAGazettement)
    {
        val variable:MutableMap<String, Any> = HashMap()
        nWAGazettement.ksNumber?.let{variable.put("ksNumber", it)}
        nWAGazettement.dateOfGazettement?.let{variable.put("dateOfGazettement", it)}
        nWAGazettement.description?.let{variable.put("description", it)}

        print(nWAGazettement.toString())

        nWAGazettementRepository.save(nWAGazettement)
        taskService.complete(nWAGazettement.taskId, variable)
        println("NWA Gazettement date has been updated")

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
                activity.activityId + " took " + activity.durationInMillis + " milliseconds")
        }

    }
}
