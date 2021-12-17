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
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.qa.QaUploadsEntity
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
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
    private val userListRepository: UserListRepository,
    private val comJcJustificationUploadsRepository: ComJcJustificationUploadsRepository,
    private val notifications: Notifications,
    private val commonDaoServices: CommonDaoServices,
    private val comStandardDraftUploadsRepository: ComStandardDraftUploadsRepository,
    private val comStandardUploadsRepository: ComStandardUploadsRepository,
    private val comStandardJCRepository: ComStandardJCRepository
) {
    val PROCESS_DEFINITION_KEY = "sd_CompanyStandard"
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
        .addClasspathResource("processes/std/Company_Standard.bpmn20.xml")
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

    fun formJointCommittee(comStandardJC: ComStandardJC,user: UsersEntity ){
        val variables: MutableMap<String, Any> = HashMap()
        comStandardJC.requestNumber?.let{variables.put("requestNumber", it)}
        comStandardJC.idOfJc?.let{ variables.put("idOfJc", it)}
        comStandardJC.dateOfFormation = Timestamp(System.currentTimeMillis())
        variables["dateOfFormation"] = comStandardJC.dateOfFormation!!
        variables["nameOfJc"] = userListRepository.findNameById(comStandardJC.idOfJc?.toLong())
        comStandardJC.nameOfJc = userListRepository.findNameById(comStandardJC.idOfJc?.toLong())
        comStandardJC.createdBy= commonDaoServices.concatenateName(user)
        comStandardJC.createdOn = commonDaoServices.getTimestamp()
        print(comStandardJC.toString())

        comStandardJCRepository.save(comStandardJC)
        taskService.complete(comStandardJC.taskId, variables)
        println("Joint Committee formed")
    }

    //Return task details for Project Leader
    fun getPlTasks():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_PL).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    //Prepare Justification
    fun prepareJustification(comJcJustification: ComJcJustification): ProcessInstanceComJc {
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


        val comDetails = comJcJustificationRepository.save(comJcJustification)
        variables["ID"] = comDetails.id
        taskService.complete(comJcJustification.taskId, variables)
        println("Justification Prepared")
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables)
        return ProcessInstanceComJc(comDetails.id, processInstance.id, processInstance.isEnded,comJcJustification.datePrepared!!)


    }

    fun uploadJCFile(
        uploads: ComJcJustificationUploads,
        docFile: MultipartFile,
        doc: String,
        user: UsersEntity,
        DocDescription: String
    ): ComJcJustificationUploads {

        with(uploads) {
//            filepath = docFile.path
            name = commonDaoServices.saveDocuments(docFile)
//            fileType = docFile.contentType
            fileType = commonDaoServices.getFileTypeByMimetypesFileTypeMap(docFile.name)
            documentType = doc
            description=DocDescription
            document = docFile.bytes
            transactionDate = commonDaoServices.getCurrentDate()
            status = 1
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }

        return comJcJustificationUploadsRepository.save(uploads)
    }


    //Return task details for SPC_SEC
    fun getSpcSecTasks():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_SPC_SEC).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    // Decision on Justification
    fun decisionOnJustification(comJustificationDecision: ComJustificationDecision) : List<TaskDetails> {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        variables["Yes"] = comJustificationDecision.accentTo
        variables["No"] = comJustificationDecision.accentTo
        comJustificationDecision.comments.let { variables.put("comments", it) }
        if(variables["Yes"]==true){
            comJcJustificationRepository.findByIdOrNull(comJustificationDecision.approvalID)?.let { comJcJustification->
                with(comJcJustification){

                    remarks=comJustificationDecision.comments
                    accentTo = true
                }
                comJcJustificationRepository.save(comJcJustification)
            }?: throw Exception("TASK NOT FOUND")

        }else if(variables["No"]==false) {
            comJcJustificationRepository.findByIdOrNull(comJustificationDecision.approvalID)?.let { comJcJustification->

                with(comJcJustification){
                    remarks=comJustificationDecision.comments
                    // accentTo = false
                }
                comJcJustificationRepository.save(comJcJustification)
            }?: throw Exception("TASK NOT FOUND")

        }
        taskService.complete(comJustificationDecision.taskId, variables)
        return  getSpcSecTasks()
    }



    // Decision
    fun approveJustification(comJustificationDecision: ComJustificationDecision) : List<TaskDetails> {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        variables["Yes"] = comJustificationDecision.accentTo
        variables["No"] = comJustificationDecision.accentTo
        comJustificationDecision.comments.let { variables.put("comments", it) }
        if(variables["Yes"]==true){
            comJcJustificationRepository.findByIdOrNull(comJustificationDecision.approvalID)?.let { comJcJustification->
                with(comJcJustification){

                    remarks=comJustificationDecision.comments
                    accentTo = true
                }
                comJcJustificationRepository.save(comJcJustification)
            }?: throw Exception("TASK NOT FOUND")

        }else if(variables["No"]==false) {
            comJcJustificationRepository.findByIdOrNull(comJustificationDecision.approvalID)?.let { comJcJustification->

                with(comJcJustification){
                    remarks=comJustificationDecision.comments
                    // accentTo = false
                }
                comJcJustificationRepository.save(comJcJustification)
            }?: throw Exception("TASK NOT FOUND")

        }
        taskService.complete(comJustificationDecision.taskId, variables)
        return  getSacSecTasks()
    }


    //Upload Company Draft
    fun uploadDraft(comStdDraft: ComStdDraft): ProcessInstanceComDraft {
        val variables: MutableMap<String, Any> = HashMap()
        comStdDraft.title?.let{variables.put("title", it)}
        comStdDraft.scope?.let{variables.put("scope", it)}
        comStdDraft.normativeReference?.let{variables.put("normativeReference", it)}
        comStdDraft.symbolsAbbreviatedTerms?.let{variables.put("symbolsAbbreviatedTerms", it)}
        comStdDraft.clause?.let{variables.put("clause", it)}
        comStdDraft.special?.let{variables.put("special", it)}
        comStdDraft.uploadedBy?.let{ variables.put("uploadedBy", it)}
        comStdDraft.uploadDate = Timestamp(System.currentTimeMillis())
        variables["uploadDate"] = comStdDraft.uploadDate!!
        variables["uploadedByName"] = userListRepository.findNameById(comStdDraft.uploadedBy?.toLong())
        comStdDraft.createdBy = userListRepository.findNameById(comStdDraft.uploadedBy?.toLong())
        comStdDraft.draftNumber = getDRNumber()

        variables["draftNumber"] = comStdDraft.draftNumber!!

        //send email to JC

        val comDetails = comStdDraftRepository.save(comStdDraft)
        variables["ID"] = comDetails.id
        taskService.complete(comStdDraft.taskId, variables)
        println("Upload Company standard Draft")
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables)
        return ProcessInstanceComDraft(comDetails.id, processInstance.id, processInstance.isEnded,comStdDraft.draftNumber!!)


    }
    // Upload nwa Standard Document
    fun uploadDrFile(
        uploads: ComStandardDraftUploads,
        docFile: MultipartFile,
        doc: String,
        user: UsersEntity,
        DocDescription: String
    ): ComStandardDraftUploads {

        with(uploads) {
//            filepath = docFile.path
            name = commonDaoServices.saveDocuments(docFile)
//            fileType = docFile.contentType
            fileType = docFile.contentType
            documentType = doc
            description=DocDescription
            document = docFile.bytes
            transactionDate = commonDaoServices.getCurrentDate()
            status = 1
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }

        return comStandardDraftUploadsRepository.save(uploads)
    }




    fun getDRNumber(): String
    {
        var allRequests =comStdDraftRepository.findAllByOrderByIdDesc()

        var lastId:String?="0"
        var finalValue =1
        var startId="DR"

//        for(item in allRequests){
//            println(item)
//            lastId = item.draftNumber
//            break
//        }
        allRequests = allRequests.plus(1) as MutableList<ComStdDraft>
        println(allRequests)
//        if(lastId != "0")
//        {
//            val strs = lastId?.split(":")?.toTypedArray()
//            val firstPortion = strs?.get(0)
//            val lastPortArray = firstPortion?.split("/")?.toTypedArray()
//            val intToIncrement =lastPortArray?.get(1)
//            finalValue = (intToIncrement?.toInt()!!)
//            finalValue += 1
//        }


        val year = Calendar.getInstance()[Calendar.YEAR]
        val month = Calendar.getInstance()[Calendar.MONTH]

        return "$startId/$allRequests/$month:$year"
    }

    //Return task details for JC_SEC
    fun getJcSecTasks():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_JC_SEC).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    //View Company Draft
    fun findUploadedCDRFileBYId(comDraftDocumentId: Long): MutableList<ComStandardDraftUploads> {
        return comStandardDraftUploadsRepository.findByComDraftDocumentId(comDraftDocumentId) ?: throw ExpectedDataNotFound("No File found with the following [ id=$comDraftDocumentId]")
    }
    fun findUploadedFileBYId(comDraftDocumentId: Long): MutableList<ComStandardDraftUploads> {
        comStandardDraftUploadsRepository.findByComDraftDocumentId(comDraftDocumentId)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No File found with the following [ id=$comDraftDocumentId]")
    }



    // Decision on Company Draft
    fun decisionOnCompanyStdDraft(comDraftDecision: ComDraftDecision) : List<TaskDetails> {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        variables["Yes"] = comDraftDecision.accentTo
        variables["No"] = comDraftDecision.accentTo
        comDraftDecision.comments.let { variables.put("comments", it) }
        if(variables["Yes"]==true){
            comStdDraftRepository.findByIdOrNull(comDraftDecision.approvalID)?.let { comStdDraft->
                with(comStdDraft){

                    remarks=comDraftDecision.comments
                    accentTo = true
                }
                comStdDraftRepository.save(comStdDraft)
            }?: throw Exception("TASK NOT FOUND")

        }else if(variables["No"]==false) {
            comStdDraftRepository.findByIdOrNull(comDraftDecision.approvalID)?.let { comStdDraft->

                with(comStdDraft){
                    remarks=comDraftDecision.comments
                    // accentTo = false
                }
                comStdDraftRepository.save(comStdDraft)
            }?: throw Exception("TASK NOT FOUND")

        }
        taskService.complete(comDraftDecision.taskId, variables)
        return  getJcSecTasks()
    }

    //Return task details for COM_SEC
    fun getComSecTasks():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_COM_SEC).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    // Decision on Company Draft
    fun decisionOnComStdDraft(comDraftDecision: ComDraftDecision) : List<TaskDetails> {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        variables["Yes"] = comDraftDecision.accentTo
        variables["No"] = comDraftDecision.accentTo
        comDraftDecision.comments.let { variables.put("comments", it) }
        if(variables["Yes"]==true){
            comStdDraftRepository.findByIdOrNull(comDraftDecision.approvalID)?.let { comStdDraft->
                with(comStdDraft){

                    remarks=comDraftDecision.comments
                    accentTo = true
                }
                comStdDraftRepository.save(comStdDraft)
            }?: throw Exception("TASK NOT FOUND")

        }else if(variables["No"]==false) {
            comStdDraftRepository.findByIdOrNull(comDraftDecision.approvalID)?.let { comStdDraft->

                with(comStdDraft){
                    remarks=comDraftDecision.comments
                    // accentTo = false
                }
                comStdDraftRepository.save(comStdDraft)
            }?: throw Exception("TASK NOT FOUND")

        }
        taskService.complete(comDraftDecision.taskId, variables)
        return  getComSecTasks()
    }

    //Return task details for HOP
    fun getHopTasks():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_HOP).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    // Upload Company Standard
    fun uploadComStandard(companyStandard: CompanyStandard) : ProcessInstanceComStandard
    {
        val variable:MutableMap<String, Any> = HashMap()
        companyStandard.title?.let{variable.put("title", it)}
        companyStandard.scope?.let{variable.put("scope", it)}
        companyStandard.normativeReference?.let{variable.put("normativeReference", it)}
        companyStandard.symbolsAbbreviatedTerms?.let{variable.put("symbolsAbbreviatedTerms", it)}
        companyStandard.clause?.let{variable.put("clause", it)}
        companyStandard.special?.let{variable.put("special", it)}

        companyStandard.comStdNumber = getCSNumber()

        variable["comStdNumber"] = companyStandard.comStdNumber!!

        val comDetails = companyStandardRepository.save(companyStandard)
        variable["ID"] = comDetails.id
        taskService.complete(companyStandard.taskId, variable)
        println("Company Standard Uploaded")
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variable)
        return ProcessInstanceComStandard(comDetails.id, processInstance.id, processInstance.isEnded,companyStandard.comStdNumber?: throw NullValueNotAllowedException("Standard Number is required"))

    }

    fun getCSNumber(): String
    {
        var allRequests =companyStandardRepository.findAllByOrderByIdDesc()

        var lastId:String?="0"
        var finalValue =1
        var startId="CS"
        allRequests = allRequests.plus(1) as MutableList<CompanyStandard>
        println(allRequests)

        val year = Calendar.getInstance()[Calendar.YEAR]
        val month = Calendar.getInstance()[Calendar.MONTH]

        return "$startId/$allRequests/$month:$year"
    }

    // Upload nwa Standard Document
    fun uploadSTDFile(
        uploads: ComStandardUploads,
        docFile: MultipartFile,
        doc: String,
        user: UsersEntity,
        DocDescription: String
    ): ComStandardUploads {

        with(uploads) {
//            filepath = docFile.path
            name = commonDaoServices.saveDocuments(docFile)
//            fileType = docFile.contentType
            fileType = docFile.contentType
            documentType = doc
            description=DocDescription
            document = docFile.bytes
            transactionDate = commonDaoServices.getCurrentDate()
            status = 1
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }

        return comStandardUploadsRepository.save(uploads)
    }



    //Return task details for SAC_SEC
    fun getSacSecTasks():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_SAC_SEC).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }


    // View STD Document upload
    fun findUploadedSTDFileBYId(comStdDocumentId: Long): ComStandardUploads {
        return comStandardUploadsRepository.findByComStdDocumentId(comStdDocumentId) ?: throw ExpectedDataNotFound("No File found with the following [ id=$comStdDocumentId]")
    }


    fun editCompanyStandard(editCompanyStandard: EditCompanyStandard): ProcessInstanceEditStandard
    {
        val variable:MutableMap<String, Any> = HashMap()
        companyStandardRepository.findByIdOrNull(editCompanyStandard.id)?.let { editCompanyStandard->

            with(editCompanyStandard){

                title=editCompanyStandard.title
                scope=editCompanyStandard.scope
                normativeReference=editCompanyStandard.normativeReference
                symbolsAbbreviatedTerms=editCompanyStandard.symbolsAbbreviatedTerms
                clause=editCompanyStandard.clause
                special=editCompanyStandard.special

                // accentTo = false
            }
            companyStandardRepository.save(editCompanyStandard)
            taskService.complete(editCompanyStandard.taskId, variable)
            println("Company Standard Edited")
            val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variable)
            return ProcessInstanceEditStandard(editCompanyStandard.id, processInstance.id, processInstance.isEnded,editCompanyStandard.comStdNumber!!)
        }?: throw Exception("STANDARD NOT FOUND")

    }
    fun editSTDFile(
        uploads: ComStandardUploads,
        docFile: MultipartFile,
        doc: String,
        user: UsersEntity,
        DocDescription: String
    ): ComStandardUploads {
        comStandardUploadsRepository.findByIdOrNull(uploads.comStdDocumentId)?.let { uploads->
        with(uploads) {
//            filepath = docFile.path
            name = commonDaoServices.saveDocuments(docFile)
//            fileType = docFile.contentType
            fileType = commonDaoServices.getFileTypeByMimetypesFileTypeMap(docFile.name)
            documentType = doc
            description=DocDescription
            document = docFile.bytes
            transactionDate = commonDaoServices.getCurrentDate()
            status = 1
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }

        return comStandardUploadsRepository.save(uploads)
        }?: throw Exception("STANDARD NOT FOUND")
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
