package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std

import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.engine.repository.Deployment
import org.flowable.task.api.Task
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.dto.std.*
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.std.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.sql.Timestamp
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.set

@Service
class NWAService(private val runtimeService: RuntimeService,
                 private val taskService: TaskService,
                 @Qualifier("processEngine") private val processEngine: ProcessEngine,
                 private val repositoryService: RepositoryService,
                 private val commonDaoServices: CommonDaoServices,
                 private val nwaJustificationRepository: NwaJustificationRepository,
                 private val nwaDisDtJustificationRepository: NWADISDTJustificationRepository,
                 private val nwaPreliminaryDraftRepository: NwaPreliminaryDraftRepository,
                 private val technicalCommitteeRepository: TechnicalCommitteeRepository,
                 private val departmentRepository: DepartmentRepository,
                 private val sdNwaUploadsEntityRepository: DatKebsSdNwaUploadsEntityRepository,
                 private val nwaWorkshopDraftRepository: NwaWorkShopDraftRepository,
                 private val nwaStandardRepository: NwaStandardRepository,
                 private val nwaGazettementRepository: NwaGazettementRepository,
                 private val nwaGazetteNoticeRepository: NwaGazetteNoticeRepository,
                 private val sdDiJustificationUploadsRepository: SDDIJustificationUploadsRepository,
                 private val nwaPreliminaryDraftUploadsRepository: NWAPreliminaryDraftUploadsRepository,
                 private val nwaStandardUploadsRepository: NWAStandardUploadsRepository,
                 private val nwaWorkShopDraftUploadsRepository: NWAWorkShopDraftUploadsRepository


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
        .addClasspathResource("processes/std/Kenya_National_Workshop_Agreement_Module.bpmn20.xml")
        .deploy()

    //start the process by process Key
    fun startProcessByKey() :ProcessInstanceResponse
    {

        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY)
        return ProcessInstanceResponse( processInstance.id, processInstance.isEnded)
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

//    fun startProcessInstance (): ProcessInstanceResponse{
//        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY)
//        return ProcessInstanceResponse(processInstance.id, processInstance.isEnded)
//    }

    //prepare justification
    fun prepareJustification(nwaJustification: NWAJustification): ProcessInstanceResponseValue
    {
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        val variables: MutableMap<String, Any> = HashMap()
        nwaJustification.meetingDate?.let{ variables.put("meetingDate", it)}
        nwaJustification.knw?.let{ variables.put("knw", it)}
        nwaJustification.knwSecretary?.let{ variables.put("knwSecretary", it)}
        nwaJustification.sl?.let{ variables.put("sl", it)}
        //nwaJustification.requestNumber?.let{ variables.put("requestNumber", it)}
        nwaJustification.requestedBy?.let{ variables.put("requestedBy", it)}
        nwaJustification.issuesAddressed?.let{ variables.put("issuesAddressed", it)}
        nwaJustification.knwAcceptanceDate?.let{ variables.put("knwAcceptanceDate", it)}
        nwaJustification.referenceMaterial?.let{ variables.put("referenceMaterial", it)}
        nwaJustification.department?.let{ variables.put("department", it)}
        nwaJustification.status?.let{ variables.put("status", it)}
        nwaJustification.remarks?.let{ variables.put("remarks", it)}

        nwaJustification.submissionDate = Timestamp(System.currentTimeMillis())
        variables["submissionDate"] = nwaJustification.submissionDate!!

        nwaJustification.requestNumber = getRQNumber()

        variables["requestNumber"] = nwaJustification.requestNumber!!

        variables["knwCommittee"] = technicalCommitteeRepository.findNameById(nwaJustification.knw?.toLong())
        nwaJustification.knwCommittee = technicalCommitteeRepository.findNameById(nwaJustification.knw?.toLong())

        variables["departmentName"] = departmentRepository.findNameById(nwaJustification.department?.toLong())
        nwaJustification.departmentName = departmentRepository.findNameById(nwaJustification.department?.toLong())


        val nwaDetails = nwaJustificationRepository.save(nwaJustification)
        variables["ID"] = nwaDetails.id
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables)
        return ProcessInstanceResponseValue(nwaDetails.id, processInstance.id, processInstance.isEnded,
            nwaJustification.requestNumber!!
        )

    }


    fun uploadSDFile(
        uploads: DatKebsSdNwaUploadsEntity,
        docFile: MultipartFile,
        doc: String,
        user: UsersEntity,
        DocDescription: String
    ): DatKebsSdNwaUploadsEntity {

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

        return sdNwaUploadsEntityRepository.save(uploads)
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

    fun decisionOnJustification(nwaJustificationDecision: NWAJustificationDecision) : List<TaskDetails> {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        variables["Yes"] = nwaJustificationDecision.accentTo
        if(variables["Yes"]==true){
            nwaJustificationRepository.findByIdOrNull(nwaJustificationDecision.approvalID)?.let { nwaJustification->

                with(nwaJustification){

                    accentTo = true
                }
                nwaJustificationRepository.save(nwaJustification)
            }?: throw Exception("TASK NOT FOUND")

        }
        taskService.complete(nwaJustificationDecision.taskId, variables)
        return  getSPCSECTasks()
    }



    // Prepare Justification for DI-SDT approval
    fun prepareDisDtJustification(nwaDiSdtJustification: NWADiSdtJustification) : ProcessInstanceDISDT
    {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val variable:MutableMap<String, Any> = HashMap()
        nwaDiSdtJustification.cost?.let{variable.put("cost", it)}
        nwaDiSdtJustification.numberOfMeetings?.let{variable.put("numberOfMeetings", it)}
        nwaDiSdtJustification.identifiedNeed?.let{variable.put("identifiedNeed", it)}
        nwaDiSdtJustification.dateOfApproval?.let{variable.put("dateOfApproval", it)}
        nwaDiSdtJustification.datePrepared = commonDaoServices.getTimestamp()
        variable["datePrepared"] = nwaDiSdtJustification.datePrepared!!

        //print(nwaDiSdtJustification.toString())


        val nwaDetails = nwaDisDtJustificationRepository.save(nwaDiSdtJustification)
        variable["ID"] = nwaDetails.id
        taskService.complete(nwaDiSdtJustification.taskId, variable)
        println("Justification for DI-SDT prepared")
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variable)
        return ProcessInstanceDISDT(nwaDetails.id, processInstance.id, processInstance.isEnded,nwaDiSdtJustification.datePrepared!!)


    }
    fun uploadSDIFile(
        uploads: SDDIJustificationUploads,
        docFile: MultipartFile,
        doc: String,
        user: UsersEntity,
        DocDescription: String
    ): SDDIJustificationUploads {

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

        return sdDiJustificationUploadsRepository.save(uploads)
    }

    //Return task details for SPC_SEC
    fun getDISDTTasks():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_DI_SDT).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    //Decision on DI-SDT
    fun decisionOnDiSdtJustification(workshopAgreement: WorkshopAgreement) : List<TaskDetails> {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        variables["Yes"] = workshopAgreement.accentTo
        if(variables["Yes"]==true){
            nwaDisDtJustificationRepository.findByIdOrNull(workshopAgreement.approvalID)?.let { nwaDiSdtJustification->
                val valueFound =getCDNumber()
                with(nwaDiSdtJustification){
                    cdAppNumber = valueFound.first
                    cdn = valueFound.second
                    accentTo = true
                }
                nwaDisDtJustificationRepository.save(nwaDiSdtJustification)
            }?: throw Exception("TASK NOT FOUND")

        }
        taskService.complete(workshopAgreement.taskId, variables)
      return  getDISDTTasks()
    }



    // Prepare Preliminary Draft
    fun preparePreliminaryDraft(nwaPreliminaryDraft: NWAPreliminaryDraft) : ProcessInstancePD
    {
        val variable:MutableMap<String, Any> = HashMap()
        nwaPreliminaryDraft.title?.let{variable.put("title", it)}
        nwaPreliminaryDraft.scope?.let{variable.put("scope", it)}
        nwaPreliminaryDraft.normativeReference?.let{variable.put("normativeReference", it)}
        nwaPreliminaryDraft.symbolsAbbreviatedTerms?.let{variable.put("symbolsAbbreviatedTerms", it)}
        nwaPreliminaryDraft.clause?.let{variable.put("clause", it)}
        nwaPreliminaryDraft.special?.let{variable.put("special", it)}
        nwaPreliminaryDraft.datePdPrepared = commonDaoServices.getTimestamp()


        variable["datePdPrepared"] = nwaPreliminaryDraft.datePdPrepared!!

        val nwaDetails = nwaPreliminaryDraftRepository.save(nwaPreliminaryDraft)
        variable["ID"] = nwaDetails.id
        taskService.complete(nwaPreliminaryDraft.taskId, variable)
        println("Preliminary Draft Prepared")
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variable)
        return ProcessInstancePD(nwaDetails.id, processInstance.id, processInstance.isEnded,nwaPreliminaryDraft.datePdPrepared!!)

    }
    fun uploadPDFile(
        uploads: NWAPreliminaryDraftUploads,
        docFile: MultipartFile,
        doc: String,
        user: UsersEntity,
        DocDescription: String
    ): NWAPreliminaryDraftUploads {

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

        return nwaPreliminaryDraftUploadsRepository.save(uploads)
    }

    //Decision on Preliminary Draft

    fun decisionOnPD(nwaPreliminaryDraftDecision: NWAPreliminaryDraftDecision) : List<TaskDetails> {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        variables["Yes"] = nwaPreliminaryDraftDecision.accentTo
        if(variables["Yes"]==true){
            nwaPreliminaryDraftRepository.findByIdOrNull(nwaPreliminaryDraftDecision.approvalID)?.let { nwaPreliminaryDraft->

                with(nwaPreliminaryDraft){

                    accentTo = true
                }
                nwaPreliminaryDraftRepository.save(nwaPreliminaryDraft)
            }?: throw Exception("TASK NOT FOUND")

        }
        taskService.complete(nwaPreliminaryDraftDecision.taskId, variables)
        return  getKNWTasks()
    }

    //Return task details for Head of Publishing
    fun getHOPTasks():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_HOP).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    // Edit Workshop  Draft
    fun editWorkshopDraft(nwaWorkShopDraft: NWAWorkShopDraft) : ProcessInstanceWD
    {
        val variable:MutableMap<String, Any> = HashMap()
        nwaWorkShopDraft.title?.let{variable.put("title", it)}
        nwaWorkShopDraft.scope?.let{variable.put("scope", it)}
        nwaWorkShopDraft.normativeReference?.let{variable.put("normativeReference", it)}
        nwaWorkShopDraft.symbolsAbbreviatedTerms?.let{variable.put("symbolsAbbreviatedTerms", it)}
        nwaWorkShopDraft.clause?.let{variable.put("clause", it)}
        nwaWorkShopDraft.special?.let{variable.put("special", it)}
        nwaWorkShopDraft.dateWdPrepared = Timestamp(System.currentTimeMillis())
        variable["dateWdPrepared"] = nwaWorkShopDraft.dateWdPrepared!!

        //print(nwaWorkShopDraft.toString())

        val nwaDetails = nwaWorkshopDraftRepository.save(nwaWorkShopDraft)
        variable["ID"] = nwaDetails.id
        taskService.complete(nwaWorkShopDraft.taskId, variable)
        println("Workshop Draft Edited")
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variable)
        return ProcessInstanceWD(nwaDetails.id, processInstance.id, processInstance.isEnded,nwaWorkShopDraft.dateWdPrepared!!)

    }
    //upload Workshop draft Document
    fun uploadWDFile(
        uploads: NWAWorkShopDraftUploads,
        docFile: MultipartFile,
        doc: String,
        user: UsersEntity,
        DocDescription: String
    ): NWAWorkShopDraftUploads {

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

        return nwaWorkShopDraftUploadsRepository.save(uploads)
    }

    //Return task details for SAC SEC
    fun getSacSecTasks():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_SAC_SEC).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    //Decision on WorkShop Draft
    fun decisionOnWD(nwaWorkshopDraftDecision: NWAWorkshopDraftDecision) : List<TaskDetails> {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        variables["Yes"] = nwaWorkshopDraftDecision.accentTo
        if(variables["Yes"]==true){
            nwaWorkshopDraftRepository.findByIdOrNull(nwaWorkshopDraftDecision.approvalID)?.let { nwaWorkShopDraft->

                with(nwaWorkShopDraft){

                    accentTo = true
                }
                nwaWorkshopDraftRepository.save(nwaWorkShopDraft)
            }?: throw Exception("TASK NOT FOUND")

        }
        taskService.complete(nwaWorkshopDraftDecision.taskId, variables)
        return  getSacSecTasks()
    }

    // Upload NWA Standard
    fun uploadNwaStandard(nWAStandard: NWAStandard) : ProcessInstanceUS
    {
        val variable:MutableMap<String, Any> = HashMap()
        nWAStandard.title?.let{variable.put("title", it)}
        nWAStandard.scope?.let{variable.put("scope", it)}
        nWAStandard.normativeReference?.let{variable.put("normativeReference", it)}
        nWAStandard.symbolsAbbreviatedTerms?.let{variable.put("symbolsAbbreviatedTerms", it)}
        nWAStandard.clause?.let{variable.put("clause", it)}
        nWAStandard.special?.let{variable.put("special", it)}
        //nWAStandard.ksNumber?.let{variable.put("ksNumber", it)}
        nWAStandard.dateSdUploaded = Timestamp(System.currentTimeMillis())
        variable["dateSdUploaded"] = nWAStandard.dateSdUploaded!!
        nWAStandard.ksNumber = getKSNumber()

        variable["ksNumber"] = nWAStandard.ksNumber!!
        //print(nWAStandard.toString())


        val nwaDetails = nwaStandardRepository.save(nWAStandard)
        variable["ID"] = nwaDetails.id
        taskService.complete(nWAStandard.taskId, variable)
        println("NWA Standard Uploaded")
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variable)
        return ProcessInstanceUS(nwaDetails.id, processInstance.id, processInstance.isEnded,nWAStandard.ksNumber!!)

    }
    // Upload nwa Standard Document
    fun uploadSTDFile(
        uploads: NWAStandardUploads,
        docFile: MultipartFile,
        doc: String,
        user: UsersEntity,
        DocDescription: String
    ): NWAStandardUploads {

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

        return nwaStandardUploadsRepository.save(uploads)
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
        //nWAGazetteNotice.dateUploaded?.let{variable.put("dateUploaded", it)}
        nWAGazetteNotice.description?.let{variable.put("description", it)}
        nWAGazetteNotice.dateUploaded = Timestamp(System.currentTimeMillis())
        variable["dateUploaded"] = nWAGazetteNotice.dateUploaded!!

        print(nWAGazetteNotice.toString())
        val nwaDetails = nwaGazetteNoticeRepository.save(nWAGazetteNotice)
        variable["ID"] = nwaDetails.id

        taskService.complete(nWAGazetteNotice.taskId, variable)
        println("NWA Gazette Notice has been uploaded")

    }

    // Upload NWA Gazette date
    fun updateGazettementDate(nWAGazettement: NWAGazettement)
    {
        val variable:MutableMap<String, Any> = HashMap()
        nWAGazettement.ksNumber?.let{variable.put("ksNumber", it)}
        //nWAGazettement.dateOfGazettement?.let{variable.put("dateOfGazettement", it)}
        nWAGazettement.description?.let{variable.put("description", it)}

        nWAGazettement.dateOfGazettement = Timestamp(System.currentTimeMillis())
        variable["dateOfGazettement"] = nWAGazettement.dateOfGazettement!!
        print(nWAGazettement.toString())

        nwaGazettementRepository.save(nWAGazettement)
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
    fun getRQNumber(): String
    {
        val allRequests =nwaJustificationRepository.findAllByOrderByIdDesc()

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

        return "$startId/$finalValue:$year"
    }

    fun getCDNumber(): Pair<String, Long>
    {
        //val allRequests: Int
        var allRequests =nwaDisDtJustificationRepository.getMaxCDN()

       // var lastId:String?="0"
        var finalValue =1
        var startId="CD"

        allRequests = allRequests.plus(1)
//        for(item in allRequests){
//            println(item)
//            lastId = item.cdAppNumber
//            break
//        }




        val year = Calendar.getInstance()[Calendar.YEAR]

        return Pair("$startId/$allRequests:$year", allRequests)
    }

    fun getKSNumber(): String
    {
        val allRequests =nwaStandardRepository.findAllByOrderByIdDesc()

        var lastId:String?="0"
        var finalValue =1
        var startId="KS"


        for(item in allRequests){
            println(item)
            lastId = item.ksNumber
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

        return "$startId/$finalValue:$year"
    }
}
