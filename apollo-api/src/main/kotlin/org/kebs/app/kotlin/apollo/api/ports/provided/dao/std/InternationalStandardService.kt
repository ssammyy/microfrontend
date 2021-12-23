package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std

import com.google.gson.Gson
import mu.KotlinLogging
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
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.sql.Timestamp
import java.util.*
import kotlin.collections.HashMap

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
                                    private val iSGazettementRepository: ISGazettementRepository,
                                    private val commonDaoServices: CommonDaoServices,
                                    private val sdIsDocumentUploadsRepository: SdIsDocumentUploadsRepository,
                                    private val technicalCommitteeRepository: TechnicalCommitteeRepository,
                                    private val technicalComListRepository: TechnicalComListRepository,
                                    private val departmentRepository: DepartmentRepository,
                                    private val departmentListRepository: DepartmentListRepository,
                                    private val isJustificationUploadsRepository: ISJustificationUploadsRepository,
                                    private val isStandardUploadsRepository: ISStandardUploadsRepository,
                                    private val sdisGazetteNoticeUploadsRepository: SDISGazetteNoticeUploadsRepository,
                                    private val notifications: Notifications

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
    fun prepareAdoptionProposal(iSAdoptionProposal: ISAdoptionProposal) : ProcessInstanceProposal
    {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val variables: MutableMap<String, Any> = HashMap()
        iSAdoptionProposal.proposal_doc_name?.let{ variables.put("proposal_doc_name", it)}
        iSAdoptionProposal.uploadedBy?.let{ variables.put("uploadedBy", it)}
        iSAdoptionProposal.accentTo?.let{ variables.put("accentTo", it)}
        iSAdoptionProposal.preparedDate = commonDaoServices.getTimestamp()
        variables["preparedDate"] = iSAdoptionProposal.preparedDate!!
        iSAdoptionProposal.proposalNumber = getPRNumber()

        variables["proposalNumber"] = iSAdoptionProposal.proposalNumber!!
//email to stakeholders
        val recipient= "Ashraf.Mohammed@bskglobaltech.com"
        val subject = "New Adoption Proposal Document"
        val messageBody= "An adoption document has been uploaded Kindly login to the system to comment on it"
        notifications.sendEmail(recipient, subject, messageBody)

        val ispDetails = isAdoptionProposalRepository.save(iSAdoptionProposal)
        variables["ID"] = ispDetails.id
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables)
        return ProcessInstanceProposal(ispDetails.id, processInstance.id, processInstance.isEnded,
            iSAdoptionProposal.proposalNumber!!
        )

    }
    fun uploadISFile(
        uploads: SdIsDocumentUploads,
        docFile: MultipartFile,
        doc: String,
        user: UsersEntity,
        DocDescription: String
    ): SdIsDocumentUploads {

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

        return sdIsDocumentUploadsRepository.save(uploads)
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

    //Get justification Document
    fun findUploadedFileBYId(isDocumentId: Long): SdIsDocumentUploads {
        return sdIsDocumentUploadsRepository.findByIsDocumentId(isDocumentId) ?: throw ExpectedDataNotFound("No File found with the following [ id=$isDocumentId]")
    }

    //Submit Adoption Proposal comments
    fun submitAPComments(isAdoptionComments: ISAdoptionComments){
        val variables: MutableMap<String, Any> = HashMap()
        isAdoptionComments.user_id?.let{ variables.put("user_id", it)}
        isAdoptionComments.adoption_proposal_comment?.let{ variables.put("adoption_proposal_comment", it)}

        isAdoptionComments.comment_time = Timestamp(System.currentTimeMillis())
        variables["comment_time"] = isAdoptionComments.comment_time!!

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
    fun decisionOnProposal(iSDecision: ISDecision) : List<TaskDetails> {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        variables["Yes"] = iSDecision.accentTo
        variables["No"] = iSDecision.accentTo
        iSDecision.comments.let { variables.put("comments", it) }
        if(variables["Yes"]==true){
            isAdoptionProposalRepository.findByIdOrNull(iSDecision.approvalID)?.let { iSAdoptionProposal->

                with(iSAdoptionProposal){
                    remarks=iSDecision.comments
                    accentTo = true
                }
                isAdoptionProposalRepository.save(iSAdoptionProposal)
            }?: throw Exception("TASK NOT FOUND")

        }else if(variables["No"]==false) {
            isAdoptionProposalRepository.findByIdOrNull(iSDecision.approvalID)?.let { iSAdoptionProposal->

                with(iSAdoptionProposal){
                    remarks=iSDecision.comments
                    accentTo = false
                }
                isAdoptionProposalRepository.save(iSAdoptionProposal)
            }?: throw Exception("TASK NOT FOUND")

        }
        taskService.complete(iSDecision.taskId, variables)
        return  getTCSECTasks()
    }

    //Return task details for TC_SEC
    fun getTCSeCTasks():List<TaskDetails>
    {

        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_TC_SEC).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    //prepare justification
    fun prepareJustification(iSAdoptionJustification: ISAdoptionJustification) : ProcessInstanceResponseValue
    {
        val variables: MutableMap<String, Any> = HashMap()
        iSAdoptionJustification.meetingDate?.let{ variables.put("meetingDate", it)}
        iSAdoptionJustification.tc_id?.let{ variables.put("tc_id", it)}
        iSAdoptionJustification.tcSec_id?.let{ variables.put("tcSec_id", it)}
        iSAdoptionJustification.slNumber?.let{ variables.put("slNumber", it)}
        //iSAdoptionJustification.requestNumber?.let{ variables.put("requestNumber", it)}
        iSAdoptionJustification.requestedBy?.let{ variables.put("requestedBy", it)}
        iSAdoptionJustification.issuesAddressed?.let{ variables.put("issuesAddressed", it)}
        iSAdoptionJustification.tcAcceptanceDate?.let{ variables.put("tcAcceptanceDate", it)}
        iSAdoptionJustification.referenceMaterial?.let{ variables.put("referenceMaterial", it)}
        iSAdoptionJustification.department?.let{ variables.put("department", it)}
        iSAdoptionJustification.status?.let{ variables.put("status", it)}
        iSAdoptionJustification.remarks?.let{ variables.put("remarks", it)}

        iSAdoptionJustification.submissionDate = Timestamp(System.currentTimeMillis())
        variables["submissionDate"] = iSAdoptionJustification.submissionDate!!

        iSAdoptionJustification.requestNumber = getRQNumber()

        variables["requestNumber"] = iSAdoptionJustification.requestNumber!!

        variables["tcCommittee"] = technicalComListRepository.findNameById(iSAdoptionJustification.tc_id?.toLong())
        iSAdoptionJustification.tcCommittee = technicalComListRepository.findNameById(iSAdoptionJustification.tc_id?.toLong())

        variables["departmentName"] = departmentListRepository.findNameById(iSAdoptionJustification.department?.toLong())
        iSAdoptionJustification.departmentName = departmentListRepository.findNameById(iSAdoptionJustification.department?.toLong())


        val ispDetails = iSAdoptionJustificationRepository.save(iSAdoptionJustification)
        variables["ID"] = ispDetails.id
        taskService.complete(iSAdoptionJustification.taskId, variables)
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables)
        return ProcessInstanceResponseValue(ispDetails.id, processInstance.id, processInstance.isEnded,
            iSAdoptionJustification.requestNumber!!
        )

    }


    fun uploadISJFile(
        uploads: ISJustificationUploads,
        docFile: MultipartFile,
        doc: String,
        user: UsersEntity,
        DocDescription: String
    ): ISJustificationUploads {

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

        return isJustificationUploadsRepository.save(uploads)
    }



    //Return task details for SPC_SEC
    fun getSPCSECTasks():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_SPC_SEC).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }
    //Get IS justification Document
    fun findUploadedJSFileBYId(isJSDocumentId: Long): ISJustificationUploads {
        return isJustificationUploadsRepository.findByIsJSDocumentId(isJSDocumentId) ?: throw ExpectedDataNotFound("No File found with the following [ id=$isJSDocumentId]")
    }

    // Decision
    fun decisionOnJustification(isJustificationDecision: ISJustificationDecision) : List<TaskDetails> {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        variables["Yes"] = isJustificationDecision.accentTo
        variables["No"] = isJustificationDecision.accentTo
        isJustificationDecision.comments.let { variables.put("comments", it) }
        if(variables["Yes"]==true){
            iSAdoptionJustificationRepository.findByIdOrNull(isJustificationDecision.approvalID)?.let { iSAdoptionJustification->

                with(iSAdoptionJustification){
                    remarks=isJustificationDecision.comments
                    accentTo = true
                }
                iSAdoptionJustificationRepository.save(iSAdoptionJustification)
            }?: throw Exception("TASK NOT FOUND")

        }else if(variables["No"]==false) {
            iSAdoptionJustificationRepository.findByIdOrNull(isJustificationDecision.approvalID)?.let { iSAdoptionJustification->

                with(iSAdoptionJustification){
                    remarks=isJustificationDecision.comments
                    accentTo = false
                }
                iSAdoptionJustificationRepository.save(iSAdoptionJustification)
            }?: throw Exception("TASK NOT FOUND")

        }
        taskService.complete(isJustificationDecision.taskId, variables)
        return  getSPCSECTasks()
    }

    //Return task details for SAC_SEC
    fun getSACSECTasks():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_SAC_SEC).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    // Decision
    fun approveStandard(isJustificationDecision: ISJustificationDecision) : List<TaskDetails> {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        variables["Yes"] = isJustificationDecision.accentTo
        variables["No"] = isJustificationDecision.accentTo
        isJustificationDecision.comments.let { variables.put("comments", it) }
        if(variables["Yes"]==true){
            iSAdoptionJustificationRepository.findByIdOrNull(isJustificationDecision.approvalID)?.let { iSAdoptionJustification->

                with(iSAdoptionJustification){
                    remarks=isJustificationDecision.comments
                    accentTo = true
                }
                iSAdoptionJustificationRepository.save(iSAdoptionJustification)
                //email to stakeholders
                val recipient= "Ashraf.Mohammed@bskglobaltech.com"
                val subject = "Justification Approved"
                val messageBody= "Justification for International Standard has been approved by the SAC."
                notifications.sendEmail(recipient, subject, messageBody)

            }?: throw Exception("TASK NOT FOUND")

        }else if(variables["No"]==false) {
            iSAdoptionJustificationRepository.findByIdOrNull(isJustificationDecision.approvalID)?.let { iSAdoptionJustification->

                with(iSAdoptionJustification){
                    remarks=isJustificationDecision.comments
                    accentTo = false
                }
                iSAdoptionJustificationRepository.save(iSAdoptionJustification)
            }?: throw Exception("TASK NOT FOUND")

        }
        taskService.complete(isJustificationDecision.taskId, variables)
        return  getSACSECTasks()
    }

    //Return task details for Head of Publishing
    fun getHOPTasks():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_HOP).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    // Upload NWA Standard
    fun uploadISStandard(iSUploadStandard: ISUploadStandard): ProcessInstanceResponseValues
    {
        val variable:MutableMap<String, Any> = HashMap()
        iSUploadStandard.title?.let{variable.put("title", it)}
        iSUploadStandard.scope?.let{variable.put("scope", it)}
        iSUploadStandard.normativeReference?.let{variable.put("normativeReference", it)}
        iSUploadStandard.symbolsAbbreviatedTerms?.let{variable.put("symbolsAbbreviatedTerms", it)}
        iSUploadStandard.clause?.let{variable.put("clause", it)}
        iSUploadStandard.special?.let{variable.put("special", it)}
        //iSUploadStandard.iSNumber?.let{variable.put("ISNumber", it)}


        iSUploadStandard.uploadDate = Timestamp(System.currentTimeMillis())
        variable["uploadDate"] = iSUploadStandard.uploadDate!!

        iSUploadStandard.iSNumber = getISNumber()

        variable["iSNumber"] = iSUploadStandard.iSNumber!!

        val isuDetails = iSUploadStandardRepository.save(iSUploadStandard)
        //email to legal
        val recipient= "Ashraf.Mohammed@bskglobaltech.com"
        val subject = "International Standard Uploaded"
        val messageBody= "International Standard has been uploaded by the Head of Publishing."
        notifications.sendEmail(recipient, subject, messageBody)

        variable["ID"] = isuDetails.id
        taskService.complete(iSUploadStandard.taskId, variable)
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variable)
        return ProcessInstanceResponseValues(isuDetails.id, processInstance.id, processInstance.isEnded,
            iSUploadStandard.iSNumber!!
        )

    }

    fun uploadISFile(
        uploads: ISStandardUploads,
        docFile: MultipartFile,
        doc: String,
        user: UsersEntity,
        DocDescription: String
    ): ISStandardUploads {

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

        return isStandardUploadsRepository.save(uploads)
    }



    //Return task details for HO SIC
    fun getHoSiCTasks():List<TaskDetails>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_HO_SIC).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    //Get IS Standard Document
    fun findUploadedSTFileBYId(isStandardID: Long): ISStandardUploads {
        return isStandardUploadsRepository.findByIsStdDocumentId(isStandardID) ?: throw ExpectedDataNotFound("No File found with the following [ id=$isStandardID]")
    }

    // Upload NWA Gazette notice on Website
    fun uploadGazetteNotice(iSGazetteNotice: ISGazetteNotice) : ProcessInstanceResponseGazzette
    {
        val variable:MutableMap<String, Any> = HashMap()
        iSGazetteNotice.iSNumber?.let{variable.put("iSNumber", it)}
        iSGazetteNotice.description?.let{variable.put("description", it)}

        iSGazetteNotice.dateUploaded = Timestamp(System.currentTimeMillis())
        variable["dateUploaded"] = iSGazetteNotice.dateUploaded!!
        print(iSGazetteNotice.toString())

        val isuDetails = iSGazetteNoticeRepository.save(iSGazetteNotice)
        println("IS Gazette Notice has been uploaded")
        variable["ID"] = isuDetails.id
        val gson = Gson()
        KotlinLogging.logger { }.info { "Gazette ID " + gson.toJson(isuDetails) }
        KotlinLogging.logger { }.info { "Gazette Notice" + gson.toJson(isuDetails) }
        taskService.complete(iSGazetteNotice.taskId, variable)
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variable)
        return ProcessInstanceResponseGazzette(isuDetails.id, processInstance.id, processInstance.isEnded
        )
    }
    fun uploadISGFile(
        uploads: SDISGazetteNoticeUploads,
        docFile: MultipartFile,
        doc: String,
        user: UsersEntity,
        DocDescription: String
    ): SDISGazetteNoticeUploads {

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

        return sdisGazetteNoticeUploadsRepository.save(uploads)
    }

    //Get IS Gazetted Standard Document
    fun findUploadedSTGFileBYId(isStandardID: Long): SDISGazetteNoticeUploads {
        return sdisGazetteNoticeUploadsRepository.findByIsGnDocumentId(isStandardID) ?: throw ExpectedDataNotFound("No File found with the following [ id=$isStandardID]")
    }

    // Upload NWA Gazette date
    fun updateGazettementDate(iSGazettement: ISGazettement)
    {
        val variable:MutableMap<String, Any> = HashMap()
        iSGazettement.iSNumber?.let{variable.put("iSNumber", it)}
        iSGazettement.dateOfGazettement = Timestamp(System.currentTimeMillis())
        variable["dateOfGazettement"] = iSGazettement.dateOfGazettement!!
        iSGazettement.description?.let{variable.put("description", it)}

        print(iSGazettement.toString())

        iSGazettementRepository.save(iSGazettement)
        taskService.complete(iSGazettement.taskId, variable)
        println("IS Gazzettement date has been updated")

    }

    fun getRQNumber(): String
    {
        val allRequests =iSAdoptionJustificationRepository.findAllByOrderByIdDesc()

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
    fun getISNumber(): String
    {
        val allRequests =iSUploadStandardRepository.findAllByOrderByIdDesc()

        var lastId:String?="0"
        var finalValue =1
        var startId="IS"


        for(item in allRequests){
            println(item)
            lastId = item.iSNumber
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

    fun getPRNumber(): String
    {
        val allRequests =isAdoptionProposalRepository.findAllByOrderByIdDesc()

        var lastId:String?="0"
        var finalValue =1
        var startId="PR"


        for(item in allRequests){
            println(item)
            lastId = item.proposalNumber
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
