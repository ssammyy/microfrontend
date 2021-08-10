package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std

import org.kebs.app.kotlin.apollo.common.dto.std.*
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.std.*
import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.engine.repository.Deployment
import org.flowable.task.api.Task
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.util.ArrayList

@Service
class InternationalStandardService (private val runtimeService: RuntimeService,
                                    private val taskService: TaskService,
                                    @Qualifier("processEngine") private val processEngine: ProcessEngine,
                                    private val repositoryService: RepositoryService,
                                    private val isAdoptionProposalRepository: ISAdoptionProposalRepository,
                                    private val isAdoptionCommentsRepository: ISAdoptionCommentsRepository,
                                    private val iSAdoptionJustificationRepository: ISAdoptionJustificationRepository,
                                    private val iSUploadStandardRepository: ISUploadStandardRepository,
                                    private val iSGazetteNoticeRepository: ISGazetteNoticeRepository,
                                    private val iSGazettementRepository: ISGazettementRepository

                                    )
{
    val PROCESS_DEFINITION_KEY = "sd_InternationalStandardsForAdoptionProcess"
    val TASK_CANDIDATE_TC_SEC ="TC_SEC"
    val TASK_CANDIDATE_SPC_SEC ="SPC_SEC"
    val TASK_CANDIDATE_STAKEHOLDERS ="STAKEHOLDERS"
    val TASK_CANDIDATE_HOP ="HOP"
    val TASK_CANDIDATE_SAC_SEC ="SAC_SEC"
    val TASK_CANDIDATE_HO_SIC ="HO_SIC"

    //deploy bpmn file
    fun deployProcessDefinition(): Deployment =repositoryService
        .createDeployment()
        .addClasspathResource("processes/std/International_Standards_for_Adoption_Process_Flow.bpmn20.xml")
        .deploy()

    //start the process by process Key
    fun startProcessByKey() : ProcessInstanceResponse
    {

        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY)
        return ProcessInstanceResponse(processInstance.id, processInstance.isEnded)
    }

//    fun startProcessInstance (): ProcessInstanceResponse{
//        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY)
//        return ProcessInstanceResponse(processInstance.id, processInstance.isEnded)
//    }
    //prepare Adoption Proposal
    fun prepareAdoptionProposal(iSAdoptionProposal: ISAdoptionProposal) : ProcessInstanceResponse
    {

        val variables: MutableMap<String, Any> = HashMap()
        iSAdoptionProposal.proposal_doc_name?.let{ variables.put("proposal_doc_name", it)}
        iSAdoptionProposal.uploadedBy?.let{ variables.put("uploadedBy", it)}
        iSAdoptionProposal.accentTo?.let{ variables.put("accentTo", it)}
        iSAdoptionProposal.submissionDate = Timestamp(System.currentTimeMillis())
        variables["submissionDate"] = iSAdoptionProposal.submissionDate!!


        isAdoptionProposalRepository.save(iSAdoptionProposal)
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables)
        return ProcessInstanceResponse(processInstance.id, processInstance.isEnded)

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

    //Return task details for Stakeholders
    fun getISProposals():List<TaskDetails>
    {

        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_STAKEHOLDERS).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    //Submit Adoption Proposal comments
    fun submitAPComments(isAdoptionComments: ISAdoptionComments){
        val variables: MutableMap<String, Any> = HashMap()
        isAdoptionComments.user_id?.let{ variables.put("user_id", it)}
        isAdoptionComments.adoption_proposal_comment?.let{ variables.put("adoption_proposal_comment", it)}
        isAdoptionComments.comment_time?.let{ variables.put("comment_time", it)}

        print(isAdoptionComments.toString())

        isAdoptionCommentsRepository.save(isAdoptionComments)
        taskService.complete(isAdoptionComments.taskId, variables)
        println("Comment Submitted")
    }

    //Return task details for TC_SEC
    fun getTCSECTasks():List<TaskDetails>
    {

        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_TC_SEC).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

//    // Decision on Proposal
    fun decisionOnProposal(iSAdoptionProposal: ISAdoptionProposal)
    {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        variables["Yes"] = iSAdoptionProposal.accentTo
        taskService.complete(iSAdoptionProposal.taskId, variables)
    }

    //Return task details for TC_SEC
    fun getTCSeCTasks():List<TaskDetails>
    {

        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_TC_SEC).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    //prepare justification
    fun prepareJustification(iSAdoptionJustification: ISAdoptionJustification)
    {
        val variables: MutableMap<String, Any> = HashMap()
        iSAdoptionJustification.meetingDate?.let{ variables.put("meetingDate", it)}
        iSAdoptionJustification.tc_id?.let{ variables.put("tc_id", it)}
        iSAdoptionJustification.tcSec_id?.let{ variables.put("tcSec_id", it)}
        iSAdoptionJustification.slNumber?.let{ variables.put("slNumber", it)}
        iSAdoptionJustification.requestNumber?.let{ variables.put("requestNumber", it)}
        iSAdoptionJustification.requestedBy?.let{ variables.put("requestedBy", it)}
        iSAdoptionJustification.issuesAddressed?.let{ variables.put("issuesAddressed", it)}
        iSAdoptionJustification.tcAcceptanceDate?.let{ variables.put("tcAcceptanceDate", it)}
        iSAdoptionJustification.referenceMaterial?.let{ variables.put("referenceMaterial", it)}
        iSAdoptionJustification.department?.let{ variables.put("department", it)}
        iSAdoptionJustification.status?.let{ variables.put("status", it)}
        iSAdoptionJustification.remarks?.let{ variables.put("remarks", it)}


        print(iSAdoptionJustification.toString())

        iSAdoptionJustificationRepository.save(iSAdoptionJustification)
        taskService.complete(iSAdoptionJustification.taskId, variables)
        println("Justification Prepared")


    }

    //Return task details for SPC_SEC
    fun getSPCSECTasks():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_SPC_SEC).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }


    // Decision
    fun decisionOnJustification(iSAdoptionJustification: ISAdoptionJustification)
    {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        variables["Yes"] = iSAdoptionJustification.accentTo
        taskService.complete(iSAdoptionJustification.taskId, variables)
    }

    //Return task details for SAC_SEC
    fun getSACSECTasks():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_SAC_SEC).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    // Decision
    fun approveStandard(iSAdoptionJustification: ISAdoptionJustification)
    {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        variables["Yes"] = iSAdoptionJustification.accentTo
        taskService.complete(iSAdoptionJustification.taskId, variables)
    }

    //Return task details for Head of Publishing
    fun getHOPTasks():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_HOP).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    // Upload NWA Standard
    fun uploadISStandard(iSUploadStandard: ISUploadStandard)
    {
        val variable:MutableMap<String, Any> = HashMap()
        iSUploadStandard.title?.let{variable.put("title", it)}
        iSUploadStandard.scope?.let{variable.put("scope", it)}
        iSUploadStandard.normativeReference?.let{variable.put("normativeReference", it)}
        iSUploadStandard.symbolsAbbreviatedTerms?.let{variable.put("symbolsAbbreviatedTerms", it)}
        iSUploadStandard.clause?.let{variable.put("clause", it)}
        iSUploadStandard.special?.let{variable.put("special", it)}
        iSUploadStandard.iSNumber?.let{variable.put("ISNumber", it)}

        print(iSUploadStandard.toString())

        iSUploadStandardRepository.save(iSUploadStandard)
        taskService.complete(iSUploadStandard.taskId, variable)
        println("International Standard Uploaded")

    }

    //Return task details for HO SIC
    fun getHoSiCTasks():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_HO_SIC).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    // Upload NWA Gazette notice on Website
    fun uploadGazetteNotice(iSGazetteNotice: ISGazetteNotice)
    {
        val variable:MutableMap<String, Any> = HashMap()
        iSGazetteNotice.iSNumber?.let{variable.put("ISNumber", it)}
        iSGazetteNotice.dateUploaded?.let{variable.put("dateUploaded", it)}
        iSGazetteNotice.description?.let{variable.put("description", it)}

        print(iSGazetteNotice.toString())

        iSGazetteNoticeRepository.save(iSGazetteNotice)
        taskService.complete(iSGazetteNotice.taskId, variable)
        println("IS Gazette Notice has been uploaded")

    }

    // Upload NWA Gazette date
    fun updateGazettementDate(iSGazettement: ISGazettement)
    {
        val variable:MutableMap<String, Any> = HashMap()
        iSGazettement.iSNumber?.let{variable.put("iSNumber", it)}
        iSGazettement.dateOfGazettement?.let{variable.put("dateOfGazettement", it)}
        iSGazettement.description?.let{variable.put("description", it)}

        print(iSGazettement.toString())

        iSGazettementRepository.save(iSGazettement)
        taskService.complete(iSGazettement.taskId, variable)
        println("IS Gazettement date has been updated")

    }


}
