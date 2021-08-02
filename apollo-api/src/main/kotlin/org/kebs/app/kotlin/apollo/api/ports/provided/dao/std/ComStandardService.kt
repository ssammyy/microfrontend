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
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.util.*
import kotlin.collections.HashMap

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
    private val userListRepository: UserListRepository
) {
    val PROCESS_DEFINITION_KEY = "sd_CompanyStandardsProcessFlow"
    val TASK_CANDIDATE_COM_SEC ="COM_SEC"
    val TASK_CANDIDATE_HOD ="HOD"
    val TASK_CANDIDATE_PL ="PL"
    val TASK_CANDIDATE_JC_SEC ="JC_SEC"
    val TASK_CANDIDATE_SPC_SEC ="SPC_SEC"
    val TASK_CANDIDATE_SAC_SEC ="SAC_SEC"
    val TASK_CANDIDATE_HOP ="HOP"

    //deploy bpmn file
    fun deployProcessDefinition(): Deployment =repositoryService
        .createDeployment()
        .addClasspathResource("processes/std/Company_Standards_Process_Flow.bpmn20.xml")
        .deploy()

    //start the process by process Key
//    fun startProcessByKey() : ProcessInstanceResponse
//    {
//        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY)
//        return ProcessInstanceResponse(processInstance.id, processInstance.isEnded)
//    }

    fun startProcessInstance (): ProcessInstanceResponse{
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY)
        return ProcessInstanceResponse(processInstance.id, processInstance.isEnded)
    }

    //request for company standard
    fun requestForStandard(companyStandardRequest: CompanyStandardRequest) : ProcessInstanceResponse
    {

        val variables: MutableMap<String, Any> = HashMap()
        companyStandardRequest.companyName?.let{ variables.put("companyName", it)}
        companyStandardRequest.departmentId?.let{ variables.put("departmentId", it)}
        companyStandardRequest.tcId?.let{ variables.put("tcId", it)}
        companyStandardRequest.productId?.let{ variables.put("productId", it)}
        companyStandardRequest.productSubCategoryId?.let{ variables.put("productSubCategoryId", it)}
        //companyStandardRequest.submissionDate?.let{ variables.put("submissionDate", it)}
        companyStandardRequest.companyPhone?.let{ variables.put("companyPhone", it)}
        companyStandardRequest.companyEmail?.let{ variables.put("companyEmail", it)}
        companyStandardRequest.submissionDate = Timestamp(System.currentTimeMillis())
        variables["submissionDate"] = companyStandardRequest.submissionDate!!
        companyStandardRequest.requestNumber = getRQNumber()

        variables["requestNumber"] = companyStandardRequest.requestNumber!!

        variables["tcName"] = technicalCommitteeRepository.findNameById(companyStandardRequest.tcId?.toLong())
        companyStandardRequest.tcName = technicalCommitteeRepository.findNameById(companyStandardRequest.tcId?.toLong())

        variables["departmentName"] = departmentRepository.findNameById(companyStandardRequest.departmentId?.toLong())
        companyStandardRequest.departmentName = departmentRepository.findNameById(companyStandardRequest.departmentId?.toLong())

        variables["productName"] = productRepository.findNameById(companyStandardRequest.productId?.toLong())
        companyStandardRequest.productName = productRepository.findNameById(companyStandardRequest.productId?.toLong())

        variables["productSubCategoryName"] =
            productSubCategoryRepository.findNameById(companyStandardRequest.productSubCategoryId?.toLong())
        companyStandardRequest.productSubCategoryName =
            productSubCategoryRepository.findNameById(companyStandardRequest.productSubCategoryId?.toLong())

        comStandardRequestRepository.save(companyStandardRequest)
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

    //Return task details for HOD
    fun getHODTasks():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_HOD).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    //*** Not used *** but closes any Task, linked to task close endpoint
    fun closeTask(taskId: String) {
        taskService.complete(taskId)
    }

    fun getProducts(): MutableList<Product>
    {
        return productRepository.findAll()
    }

    fun getDepartments(): MutableList<Department>
    {
        return departmentRepository.findAll()
    }
    fun getProductCategories(id:String?): MutableList<ProductSubCategory>
    {
        return productSubCategoryRepository.findAll()
    }

    fun getUserList(): MutableList<UsersEntity> {
        return userListRepository.findAll()
    }




    fun assignRequest(comStdAction: ComStdAction){
        val variables: MutableMap<String, Any> = HashMap()
        comStdAction.requestNumber?.let{ variables.put("requestNumber", it)}
        comStdAction.assignedTo?.let{ variables.put("assignedTo", it)}
        comStdAction.dateAssigned = Timestamp(System.currentTimeMillis())
        variables["dateAssigned"] = comStdAction.dateAssigned!!
        variables["plAssigned"] = userListRepository.findNameById(comStdAction.assignedTo?.toLong())
        comStdAction.plAssigned = userListRepository.findNameById(comStdAction.assignedTo?.toLong())
        print(comStdAction.toString())

        comStdActionRepository.save(comStdAction)
        taskService.complete(comStdAction.taskId, variables)
        println("Company Standard assigned to Project Leader")

    }

    //Return task details for Project Leader
    fun getPlTasks():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_PL).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    //Prepare Justification
    fun prepareJustification(comJcJustification: ComJcJustification)
    {
        val variables: MutableMap<String, Any> = HashMap()
        comJcJustification.meetingDate?.let{ variables.put("meetingDate", it)}
        comJcJustification.projectLeader?.let{ variables.put("projectLeader", it)}
        comJcJustification.reason?.let{ variables.put("reason", it)}
        comJcJustification.slNumber?.let{ variables.put("slNumber", it)}
        comJcJustification.requestNumber?.let{ variables.put("requestNumber", it)}
        comJcJustification.requestedBy?.let{ variables.put("requestedBy", it)}
        comJcJustification.issuesAddressed?.let{ variables.put("issuesAddressed", it)}
        comJcJustification.tcAcceptanceDate?.let{ variables.put("tcAcceptanceDate", it)}
        comJcJustification.referenceMaterial?.let{ variables.put("referenceMaterial", it)}
        comJcJustification.department?.let{ variables.put("department", it)}
        comJcJustification.status?.let{ variables.put("status", it)}
        comJcJustification.remarks?.let{ variables.put("remarks", it)}
        comJcJustification.datePrepared = Timestamp(System.currentTimeMillis())
        variables["datePrepared"] = comJcJustification.datePrepared!!

        print(comJcJustification.toString())

        comJcJustificationRepository.save(comJcJustification)
        taskService.complete(comJcJustification.taskId, variables)
        println("Justification Prepared")


    }

    //Return task details for SPC_SEC
    fun getSpcSecTasks():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_SPC_SEC).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    // Decision on Justification
    fun decisionOnJustification(comJcJustification: ComJcJustification)
    {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        variables["Yes"] = comJcJustification.accentTo
        taskService.complete(comJcJustification.taskId, variables)
    }

    //Return task details for SAC_SEC
    fun getSacSecTasks():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_SAC_SEC).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    // Decision
    fun approveJustification(comJcJustification: ComJcJustification)
    {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        variables["Yes"] = comJcJustification.accentTo
        taskService.complete(comJcJustification.taskId, variables)
    }

    //Upload Company Draft
    fun uploadDraft(comStdDraft: ComStdDraft)
    {
        val variables: MutableMap<String, Any> = HashMap()
        comStdDraft.documentName?.let{ variables.put("documentName", it)}
        comStdDraft.uploadedBy?.let{ variables.put("uploadedBy", it)}
        comStdDraft.uploadDate = Timestamp(System.currentTimeMillis())
        variables["uploadDate"] = comStdDraft.uploadDate!!
        variables["uploadedByName"] = userListRepository.findNameById(comStdDraft.uploadedBy?.toLong())
        comStdDraft.uploadedByName = userListRepository.findNameById(comStdDraft.uploadedBy?.toLong())

        print(comStdDraft.toString())

        comStdDraftRepository.save(comStdDraft)
        taskService.complete(comStdDraft.taskId, variables)
        println("Upload Company standard Draft")


    }

    //Return task details for JC_SEC
    fun getJcSecTasks():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_JC_SEC).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    // Decision on Company Draft
    fun decisionOnCompanyStdDraft(comStdDraft: ComStdDraft)
    {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        variables["Yes"] = comStdDraft.accentTo
        taskService.complete(comStdDraft.taskId, variables)
    }

    //Return task details for COM_SEC
    fun getComSecTasks():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_COM_SEC).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    // Upload Company Standard
    fun uploadComStandard(companyStandard: CompanyStandard)
    {
        val variable:MutableMap<String, Any> = HashMap()
        companyStandard.title?.let{variable.put("title", it)}
        companyStandard.scope?.let{variable.put("scope", it)}
        companyStandard.normativeReference?.let{variable.put("normativeReference", it)}
        companyStandard.symbolsAbbreviatedTerms?.let{variable.put("symbolsAbbreviatedTerms", it)}
        companyStandard.clause?.let{variable.put("clause", it)}
        companyStandard.special?.let{variable.put("special", it)}
        companyStandard.comStdNumber?.let{variable.put("comStdNumber", it)}

        print(companyStandard.toString())

        companyStandardRepository.save(companyStandard)
        taskService.complete(companyStandard.taskId, variable)
        println("Company Standard Uploaded")

    }

    //Return task details for HOP
    fun getHopTasks():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_HOP).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
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

    fun getRQNumber(): String
    {
        val allRequests =comStandardRequestRepository.findAllByOrderByIdDesc()

        var lastId:String?="0"
        var finalValue =1
        var startId="RQ"


        for(item in allRequests){
            println(item)
            lastId = item.requestNumber
            break
        }

        if(lastId != "0")
        {
            val strs = lastId?.split(":")?.toTypedArray()

            val firstPortion = strs?.get(0)

            val lastPortArray = firstPortion?.split("/")?.toTypedArray()

            val intToIncrement =lastPortArray?.get(1)

            finalValue = (intToIncrement?.toInt()!!)
            finalValue += 1
        }


        val year = Calendar.getInstance()[Calendar.YEAR]

        return "$startId/$finalValue:$year";
    }


}
