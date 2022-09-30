package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std

import com.google.gson.Gson
import mu.KotlinLogging
import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.engine.repository.Deployment
import org.flowable.task.api.Task
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.StandardsLevyBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.dto.std.*
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.registration.CompanyProfileEntity
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.std.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.sql.Timestamp
import java.util.*
import kotlin.collections.HashMap

@Service
class IntStandardService (private val runtimeService: RuntimeService,
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
                          private val notifications: Notifications,
                          private val companyStandardRepository: CompanyStandardRepository,
                          private val bpmnService: StandardsLevyBpmn,
                          private val internationalStandardRemarksRepository : InternationalStandardRemarksRepository,
                          private val nwaWorkshopDraftRepository: NwaWorkShopDraftRepository,
                          private val standardRepository : StandardRepository

                          ) {
    val PROCESS_DEFINITION_KEY = "sd_InternationalStandardsForAdoption"
    //deploy bpmn file
    fun deployProcessDefinition(): Deployment =repositoryService
        .createDeployment()
        .addClasspathResource("processes/std/International_Standards_for_Adoption_Process.bpmn20.xml")
        .deploy()

    //start the process by process Key
    fun startProcessByKey() : ProcessInstanceResponse
    {

        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY)
        return ProcessInstanceResponse(processInstance.id, processInstance.isEnded)
    }

    //prepare Adoption Proposal
    fun prepareAdoptionProposal(iSAdoptionProposal: ISAdoptionProposal) : ProcessInstanceProposal
    {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val variables: MutableMap<String, Any> = HashMap()
        iSAdoptionProposal.proposal_doc_name?.let{ variables.put("proposal_doc_name", it)}
        iSAdoptionProposal.circulationDate?.let{ variables.put("circulationDate", it)}
        iSAdoptionProposal.closingDate?.let{ variables.put("closingDate", it)}
        iSAdoptionProposal.tcSecName?.let{ variables.put("tcSecName", it)}
        iSAdoptionProposal.title?.let{ variables.put("title", it)}
        iSAdoptionProposal.scope?.let{ variables.put("scope", it)}
        iSAdoptionProposal.adoptionAcceptableAsPresented?.let{ variables.put("adoptionAcceptableAsPresented", it)}
        iSAdoptionProposal.reasonsForNotAcceptance?.let{ variables.put("reasonsForNotAcceptance", it)}
        iSAdoptionProposal.recommendations?.let{ variables.put("recommendations", it)}
        iSAdoptionProposal.nameOfRespondent?.let{ variables.put("nameOfRespondent", it)}
        iSAdoptionProposal.positionOfRespondent?.let{ variables.put("positionOfRespondent", it)}
        iSAdoptionProposal.nameOfOrganization?.let{ variables.put("nameOfOrganization", it)}
        iSAdoptionProposal.dateOfApplication?.let{ variables.put("dateOfApplication", it)}
        iSAdoptionProposal.uploadedBy?.let{ variables.put("uploadedBy", it)}
        iSAdoptionProposal.preparedDate = commonDaoServices.getTimestamp()
        iSAdoptionProposal.status = 0
        variables["preparedDate"] = iSAdoptionProposal.preparedDate!!
        iSAdoptionProposal.proposalNumber = getPRNumber()
        iSAdoptionProposal.assignedTo= companyStandardRepository.getTcSecId()
        variables["proposalNumber"] = iSAdoptionProposal.proposalNumber!!

        var userList= companyStandardRepository.getStakeHoldersEmailList()

        //email to stakeholders
        val targetUrl = "https://kimsint.kebs.org/";
        userList.forEach { item->
            val recipient="stephenmuganda@gmail.com"
            //val recipient= item.getUserEmail()
            val subject = "New Adoption Proposal Document"+  iSAdoptionProposal.proposalNumber
            val messageBody= "Dear ${item.getFirstName()} ${item.getLastName()},An adoption document has been uploaded Kindly login to the system to comment on it.Click on the Link below to view. ${targetUrl} "
            if (recipient != null) {
                notifications.sendEmail(recipient, subject, messageBody)
            }
        }

        val ispDetails = isAdoptionProposalRepository.save(iSAdoptionProposal)
        variables["ID"] = ispDetails.id

        //taskService.complete(nwaJustification.taskId, variables)
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables)
        taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
            ?.let { t ->
                t.list()[0]
                    ?.let { task ->
                        task.assignee =
                            "${iSAdoptionProposal.assignedTo ?: throw NullValueNotAllowedException(" invalid user id provided")}"  //set the assignee}"

                        taskService.saveTask(task)
                    }
                    ?: KotlinLogging.logger { }.error("Task list empty for $PROCESS_DEFINITION_KEY ")


            }
            ?: KotlinLogging.logger { }.error("No task found for $PROCESS_DEFINITION_KEY ")
        bpmnService.slAssignTask(
            processInstance.processInstanceId,
            "receiveComments",
            iSAdoptionProposal?.assignedTo
                ?: throw NullValueNotAllowedException("invalid user id provided")
        )

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

    fun getProposal(): MutableList<ProposalDetails>{
        return isAdoptionProposalRepository.getProposalDetails();
    }


    //Submit Adoption Proposal comments
    fun submitAPComments(isAdoptionComments: ISAdoptionComments){
        val variables: MutableMap<String, Any> = HashMap()
        isAdoptionComments.user_id?.let{ variables.put("user_id", it)}
        isAdoptionComments.taskId?.let{ variables.put("taskId", it)}
        isAdoptionComments.adoption_proposal_comment?.let{ variables.put("adoption_proposal_comment", it)}
        isAdoptionComments.commentTitle?.let{ variables.put("commentTitle", it)}
        isAdoptionComments.commentDocumentType?.let{ variables.put("commentDocumentType", it)}
        isAdoptionComments.comNameOfOrganization?.let{ variables.put("comNameOfOrganization", it)}
        isAdoptionComments.comClause?.let{ variables.put("comClause", it)}
        isAdoptionComments.comParagraph?.let{ variables.put("comParagraph", it)}
        isAdoptionComments.typeOfComment?.let{ variables.put("typeOfComment", it)}
        isAdoptionComments.proposedChange?.let{ variables.put("proposedChange", it)}

        isAdoptionComments.comment_time = Timestamp(System.currentTimeMillis())
        variables["comment_time"] = isAdoptionComments.comment_time!!

        print(isAdoptionComments.toString())

        isAdoptionCommentsRepository.save(isAdoptionComments)
        println("Comment Submitted")
    }

    //Function to retrieve task details for any candidate group
    private fun getTaskDetails(tasks: List<Task>): List<InternationalStandardTasks> {
        val taskDetails: MutableList<InternationalStandardTasks> = ArrayList()
        for (task in tasks) {
            val processVariables = taskService.getVariables(task.id)
            taskDetails.add(InternationalStandardTasks(task.id, task.name,task.processInstanceId, processVariables))

        }
        return taskDetails
    }

    fun getUserTasks(): List<InternationalStandardTasks> {
        val tasks = taskService.createTaskQuery()
            .taskAssignee("${commonDaoServices.loggedInUserDetails().id ?: throw NullValueNotAllowedException(" invalid user id provided")}")
            .list()
        return getTaskDetails(tasks)
    }

    //Get justification Document
    fun findUploadedFileBYId(isDocumentId: Long): SdIsDocumentUploads {
        return sdIsDocumentUploadsRepository.findByIsDocumentId(isDocumentId) ?: throw ExpectedDataNotFound("No File found with the following [ id=$isDocumentId]")
    }

    fun getAllComments(proposalId: Long): MutableIterable<ISAdoptionComments>? {
        return isAdoptionCommentsRepository.findByProposalID(proposalId)
    }

    // Decision on Proposal
    fun decisionOnProposal(iSDecision: ISDecision,
                           internationalStandardRemarks: InternationalStandardRemarks
    ) : List<InternationalStandardTasks> {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        variables["Yes"] = iSDecision.accentTo
        variables["No"] = iSDecision.accentTo
        iSDecision.comments.let { variables.put("comments", it) }
        iSDecision.taskId.let { variables.put("taskId", it) }
        iSDecision.processId.let { variables.put("processId", it) }
        iSDecision.assignedTo= companyStandardRepository.getTcSecId()
        val fname=loggedInUser.firstName
        val sname=loggedInUser.lastName
        val usersName= "$fname  $sname"
        internationalStandardRemarks.proposalId= iSDecision.approvalID
        internationalStandardRemarks.remarks= iSDecision.comments
        internationalStandardRemarks.status = 1.toString()
        internationalStandardRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
        internationalStandardRemarks.remarkBy = usersName

        if(variables["Yes"]==true){

                internationalStandardRemarksRepository.save(internationalStandardRemarks)
                runtimeService.createProcessInstanceQuery()
                    .processInstanceId(iSDecision.processId).list()
                    ?.let { l ->
                        val processInstance = l[0]
                        taskService.complete(iSDecision.taskId, variables)

                        taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
                            ?.let { t ->
                                t.list()[0]
                                    ?.let { task ->
                                        task.assignee = "${
                                            iSDecision.assignedTo ?: throw NullValueNotAllowedException(
                                                " invalid user id provided"
                                            )
                                        }"  //set the assignee}"
                                        //task.dueDate = standardLevyFactoryVisitReportEntity.scheduledVisitDate  //set the due date
                                        taskService.saveTask(task)
                                    }
                                    ?: KotlinLogging.logger { }
                                        .error("Task list empty for $PROCESS_DEFINITION_KEY ")


                            }
                            ?: KotlinLogging.logger { }.error("No task found for $PROCESS_DEFINITION_KEY ")
                        bpmnService.slAssignTask(
                            processInstance.processInstanceId,
                            "prepareJustification",
                            iSDecision.assignedTo
                                ?: throw NullValueNotAllowedException("invalid user id provided")
                        )

                    }
                    ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${iSDecision.processId} ")


        }else if(variables["No"]==false) {

                internationalStandardRemarksRepository.save(internationalStandardRemarks)
                taskService.complete(iSDecision.taskId, variables)
                println("Proposal Rejected")


        }

        return  getUserTasks()

    }

    fun getUserComments(id: Long): MutableIterable<InternationalStandardRemarks>? {
        return internationalStandardRemarksRepository.findAllByProposalIdOrderByIdDesc(id)
    }

    //prepare justification
    fun prepareJustification(iSAdoptionJustification: ISAdoptionJustification) : ProcessInstanceResponseValue
    {
        val variables: MutableMap<String, Any> = HashMap()
        iSAdoptionJustification.meetingDate?.let{ variables.put("meetingDate", it)}
        //iSAdoptionJustification.tc_id?.let{ variables.put("tc_id", it)}
        iSAdoptionJustification.tcSec_id?.let{ variables.put("tcSec_id", it)}
        iSAdoptionJustification.slNumber?.let{ variables.put("slNumber", it)}
        iSAdoptionJustification.edition?.let{ variables.put("edition", it)}
        iSAdoptionJustification.requestedBy?.let{ variables.put("requestedBy", it)}
        iSAdoptionJustification.issuesAddressed?.let{ variables.put("issuesAddressed", it)}
        iSAdoptionJustification.tcAcceptanceDate?.let{ variables.put("tcAcceptanceDate", it)}
        iSAdoptionJustification.referenceMaterial?.let{ variables.put("referenceMaterial", it)}
        iSAdoptionJustification.department?.let{ variables.put("department", it)}
        iSAdoptionJustification.status?.let{ variables.put("status", it)}
        iSAdoptionJustification.positiveVotes?.let{ variables.put("positiveVotes", it)}
        iSAdoptionJustification.negativeVotes?.let{ variables.put("negativeVotes", it)}
        iSAdoptionJustification.remarks?.let{ variables.put("remarks", it)}
        iSAdoptionJustification.taskId?.let{ variables.put("taskId", it)}
        iSAdoptionJustification.assignedTo= companyStandardRepository.getSpcSecId()

        iSAdoptionJustification.submissionDate = Timestamp(System.currentTimeMillis())
        variables["submissionDate"] = iSAdoptionJustification.submissionDate!!

        iSAdoptionJustification.requestNumber = getRQNumber()

        variables["requestNumber"] = iSAdoptionJustification.requestNumber!!

//        variables["tcCommittee"] = technicalComListRepository.findNameById(iSAdoptionJustification.tc_id?.toLong())
//        iSAdoptionJustification.tcCommittee = technicalComListRepository.findNameById(iSAdoptionJustification.tc_id?.toLong())

        variables["departmentName"] = departmentListRepository.findNameById(iSAdoptionJustification.department?.toLong())
        iSAdoptionJustification.departmentName = departmentListRepository.findNameById(iSAdoptionJustification.department?.toLong())

        val ispDetails = iSAdoptionJustificationRepository.save(iSAdoptionJustification)
        //variables["ID"] = ispDetails.id

        var userList= companyStandardRepository.getHopEmailList()

        //email to Head of publishing
        val targetUrl = "https://kimsint.kebs.org/";
        userList.forEach { item->
            val recipient="stephenmuganda@gmail.com"
            //val recipient= item.getUserEmail()
            val subject = "Justification"
            val messageBody= "Dear ${item.getFirstName()} ${item.getLastName()}, Justification for International Standard has been prepared."
            if (recipient != null) {
                notifications.sendEmail(recipient, subject, messageBody)
            }
        }
        runtimeService.createProcessInstanceQuery()
            .processInstanceId(iSAdoptionJustification.processId).list()
            ?.let { l ->
                val processInstance = l[0]

                taskService.complete(iSAdoptionJustification.taskId, variables)

                taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
                    ?.let { t ->
                        t.list()[0]
                            ?.let { task ->
                                task.assignee =
                                    "${iSAdoptionJustification.assignedTo ?: throw NullValueNotAllowedException(" invalid user id provided")}"  //set the assignee}"
                                taskService.saveTask(task)
                            }
                            ?: KotlinLogging.logger { }.error("Task list empty for $PROCESS_DEFINITION_KEY ")


                    }
                    ?: KotlinLogging.logger { }.error("No task found for $PROCESS_DEFINITION_KEY ")
                bpmnService.slAssignTask(
                    processInstance.processInstanceId,
                    "decisionOnJustification",
                    iSAdoptionJustification.assignedTo ?: throw NullValueNotAllowedException("invalid user id provided")
                )
                return ProcessInstanceResponseValue(ispDetails.id, processInstance.id, processInstance.isEnded,
                    iSAdoptionJustification.requestNumber!!
                )
            }
            ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${iSAdoptionJustification.processId} ")


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

    //Get IS justification Document
    fun findUploadedJSFileBYId(isJSDocumentId: Long): ISJustificationUploads {
        return isJustificationUploadsRepository.findByIsJSDocumentId(isJSDocumentId) ?: throw ExpectedDataNotFound("No File found with the following [ id=$isJSDocumentId]")
    }

    fun decisionOnJustification(isJustificationDecision: ISJustificationDecision,
                           internationalStandardRemarks: InternationalStandardRemarks
    ) : List<InternationalStandardTasks> {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        variables["Yes"] = isJustificationDecision.accentTo
        variables["No"] = isJustificationDecision.accentTo
        isJustificationDecision.comments.let { variables.put("comments", it) }
        isJustificationDecision.taskId.let { variables.put("taskId", it) }
        isJustificationDecision.processId.let { variables.put("processId", it) }
        isJustificationDecision.assignedTo= companyStandardRepository.getSaSecId()
        val fname=loggedInUser.firstName
        val sname=loggedInUser.lastName
        val usersName= "$fname  $sname"
        internationalStandardRemarks.proposalId= isJustificationDecision.approvalID
        internationalStandardRemarks.remarks= isJustificationDecision.comments
        internationalStandardRemarks.status = 1.toString()
        internationalStandardRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
        internationalStandardRemarks.remarkBy = usersName

        if(variables["Yes"]==true){
            var userList= companyStandardRepository.getHopEmailList()

            //email to Head of publishing
            val targetUrl = "https://kimsint.kebs.org/";
            userList.forEach { item->
                val recipient="stephenmuganda@gmail.com"
                //val recipient= item.getUserEmail()
                val subject = "Justification"
                val messageBody= "Dear ${item.getFirstName()} ${item.getLastName()}, Justification for International Standard has been Accepted by the SPC."
                if (recipient != null) {
                    notifications.sendEmail(recipient, subject, messageBody)
                }
            }

                internationalStandardRemarksRepository.save(internationalStandardRemarks)
                runtimeService.createProcessInstanceQuery()
                    .processInstanceId(isJustificationDecision.processId).list()
                    ?.let { l ->
                        val processInstance = l[0]
                        taskService.complete(isJustificationDecision.taskId, variables)

                        taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
                            ?.let { t ->
                                t.list()[0]
                                    ?.let { task ->
                                        task.assignee = "${
                                            isJustificationDecision.assignedTo ?: throw NullValueNotAllowedException(
                                                " invalid user id provided"
                                            )
                                        }"  //set the assignee}"
                                        //task.dueDate = standardLevyFactoryVisitReportEntity.scheduledVisitDate  //set the due date
                                        taskService.saveTask(task)
                                    }
                                    ?: KotlinLogging.logger { }
                                        .error("Task list empty for $PROCESS_DEFINITION_KEY ")


                            }
                            ?: KotlinLogging.logger { }.error("No task found for $PROCESS_DEFINITION_KEY ")
                        bpmnService.slAssignTask(
                            processInstance.processInstanceId,
                            "justificationDecision",
                            isJustificationDecision.assignedTo
                                ?: throw NullValueNotAllowedException("invalid user id provided")
                        )

                    }
                    ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${isJustificationDecision.processId} ")


        }else if(variables["No"]==false) {

                internationalStandardRemarksRepository.save(internationalStandardRemarks)
                taskService.complete(isJustificationDecision.taskId, variables)
                println("Proposal Rejected")


        }

        return  getUserTasks()

    }

    fun justificationDecision(isJustificationDecision: ISJustificationDecision,
                                internationalStandardRemarks: InternationalStandardRemarks
    ) : List<InternationalStandardTasks> {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        variables["Yes"] = isJustificationDecision.accentTo
        variables["No"] = isJustificationDecision.accentTo
        isJustificationDecision.comments.let { variables.put("comments", it) }
        isJustificationDecision.taskId.let { variables.put("taskId", it) }
        isJustificationDecision.processId.let { variables.put("processId", it) }

        val fname=loggedInUser.firstName
        val sname=loggedInUser.lastName
        val usersName= "$fname  $sname"
        internationalStandardRemarks.proposalId= isJustificationDecision.approvalID
        internationalStandardRemarks.remarks= isJustificationDecision.comments
        internationalStandardRemarks.status = 1.toString()
        internationalStandardRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
        internationalStandardRemarks.remarkBy = usersName

        if(variables["Yes"]==true){
            var userList= companyStandardRepository.getStakeHoldersEmailList()
            //email to Head of publishing
            userList.forEach { item->
                val recipient="stephenmuganda@gmail.com"
                //val recipient= item.getUserEmail()
                val subject = "International Standard"
                val messageBody= "Dear ${item.getFirstName()} ${item.getLastName()}, International Standard Adoption Proposal has been approved for publishing."
                if (recipient != null) {
                    notifications.sendEmail(recipient, subject, messageBody)
                }
            }
            isJustificationDecision.assignedTo= companyStandardRepository.getHopId()


                internationalStandardRemarksRepository.save(internationalStandardRemarks)
                runtimeService.createProcessInstanceQuery()
                    .processInstanceId(isJustificationDecision.processId).list()
                    ?.let { l ->
                        val processInstance = l[0]
                        taskService.complete(isJustificationDecision.taskId, variables)

                        taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
                            ?.let { t ->
                                t.list()[0]
                                    ?.let { task ->
                                        task.assignee = "${
                                            isJustificationDecision.assignedTo ?: throw NullValueNotAllowedException(
                                                " invalid user id provided"
                                            )
                                        }"  //set the assignee}"
                                        //task.dueDate = standardLevyFactoryVisitReportEntity.scheduledVisitDate  //set the due date
                                        taskService.saveTask(task)
                                    }
                                    ?: KotlinLogging.logger { }
                                        .error("Task list empty for $PROCESS_DEFINITION_KEY ")


                            }
                            ?: KotlinLogging.logger { }.error("No task found for $PROCESS_DEFINITION_KEY ")
                        bpmnService.slAssignTask(
                            processInstance.processInstanceId,
                            "checkRequirementsMet",
                            isJustificationDecision.assignedTo
                                ?: throw NullValueNotAllowedException("invalid user id provided")
                        )

                    }
                    ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${isJustificationDecision.processId} ")


        }else if(variables["No"]==false) {
            isJustificationDecision.assignedTo= companyStandardRepository.getSpcSecId()


                internationalStandardRemarksRepository.save(internationalStandardRemarks)
                runtimeService.createProcessInstanceQuery()
                    .processInstanceId(isJustificationDecision.processId).list()
                    ?.let { l ->
                        val processInstance = l[0]
                        taskService.complete(isJustificationDecision.taskId, variables)

                        taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
                            ?.let { t ->
                                t.list()[0]
                                    ?.let { task ->
                                        task.assignee = "${
                                            isJustificationDecision.assignedTo ?: throw NullValueNotAllowedException(
                                                " invalid user id provided"
                                            )
                                        }"  //set the assignee}"
                                        //task.dueDate = standardLevyFactoryVisitReportEntity.scheduledVisitDate  //set the due date
                                        taskService.saveTask(task)
                                    }
                                    ?: KotlinLogging.logger { }
                                        .error("Task list empty for $PROCESS_DEFINITION_KEY ")


                            }
                            ?: KotlinLogging.logger { }.error("No task found for $PROCESS_DEFINITION_KEY ")
                        bpmnService.slAssignTask(
                            processInstance.processInstanceId,
                            "decisionOnJustification",
                            isJustificationDecision.assignedTo
                                ?: throw NullValueNotAllowedException("invalid user id provided")
                        )

                    }
                    ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${isJustificationDecision.processId} ")



        }

        return  getUserTasks()

    }

    fun checkRequirements(isJustificationDecision: ISJustificationDecision,
                              internationalStandardRemarks: InternationalStandardRemarks
    ) : List<InternationalStandardTasks> {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        variables["Yes"] = isJustificationDecision.accentTo
        variables["No"] = isJustificationDecision.accentTo
        isJustificationDecision.comments.let { variables.put("comments", it) }
        isJustificationDecision.taskId.let { variables.put("taskId", it) }
        isJustificationDecision.processId.let { variables.put("processId", it) }

        val fname=loggedInUser.firstName
        val sname=loggedInUser.lastName
        val usersName= "$fname  $sname"
        internationalStandardRemarks.proposalId= isJustificationDecision.approvalID
        internationalStandardRemarks.remarks= isJustificationDecision.comments
        internationalStandardRemarks.status = 1.toString()
        internationalStandardRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
        internationalStandardRemarks.remarkBy = usersName

        if(variables["Yes"]==true){
            isJustificationDecision.assignedTo= companyStandardRepository.getEditorId()

                internationalStandardRemarksRepository.save(internationalStandardRemarks)
                runtimeService.createProcessInstanceQuery()
                    .processInstanceId(isJustificationDecision.processId).list()
                    ?.let { l ->
                        val processInstance = l[0]
                        taskService.complete(isJustificationDecision.taskId, variables)

                        taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
                            ?.let { t ->
                                t.list()[0]
                                    ?.let { task ->
                                        task.assignee = "${
                                            isJustificationDecision.assignedTo ?: throw NullValueNotAllowedException(
                                                " invalid user id provided"
                                            )
                                        }"  //set the assignee}"
                                        //task.dueDate = standardLevyFactoryVisitReportEntity.scheduledVisitDate  //set the due date
                                        taskService.saveTask(task)
                                    }
                                    ?: KotlinLogging.logger { }
                                        .error("Task list empty for $PROCESS_DEFINITION_KEY ")


                            }
                            ?: KotlinLogging.logger { }.error("No task found for $PROCESS_DEFINITION_KEY ")
                        bpmnService.slAssignTask(
                            processInstance.processInstanceId,
                            "draftStandardEditing",
                            isJustificationDecision.assignedTo
                                ?: throw NullValueNotAllowedException("invalid user id provided")
                        )

                    }
                    ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${isJustificationDecision.processId} ")


        }else if(variables["No"]==false) {
            isJustificationDecision.assignedTo= companyStandardRepository.getSaSecId()

                internationalStandardRemarksRepository.save(internationalStandardRemarks)
                runtimeService.createProcessInstanceQuery()
                    .processInstanceId(isJustificationDecision.processId).list()
                    ?.let { l ->
                        val processInstance = l[0]
                        taskService.complete(isJustificationDecision.taskId, variables)

                        taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
                            ?.let { t ->
                                t.list()[0]
                                    ?.let { task ->
                                        task.assignee = "${
                                            isJustificationDecision.assignedTo ?: throw NullValueNotAllowedException(
                                                " invalid user id provided"
                                            )
                                        }"  //set the assignee}"
                                        //task.dueDate = standardLevyFactoryVisitReportEntity.scheduledVisitDate  //set the due date
                                        taskService.saveTask(task)
                                    }
                                    ?: KotlinLogging.logger { }
                                        .error("Task list empty for $PROCESS_DEFINITION_KEY ")


                            }
                            ?: KotlinLogging.logger { }.error("No task found for $PROCESS_DEFINITION_KEY ")
                        bpmnService.slAssignTask(
                            processInstance.processInstanceId,
                            "justificationDecision",
                            isJustificationDecision.assignedTo
                                ?: throw NullValueNotAllowedException("invalid user id provided")
                        )

                    }
                    ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${isJustificationDecision.processId} ")



        }

        return  getUserTasks()

    }

    fun editStandardDraft(nwaWorkShopDraft: NWAWorkShopDraft) : ProcessInstanceWD
    {
        val variable:MutableMap<String, Any> = java.util.HashMap()
        nwaWorkShopDraft.title?.let{variable.put("title", it)}
        nwaWorkShopDraft.scope?.let{variable.put("scope", it)}
        nwaWorkShopDraft.normativeReference?.let{variable.put("normativeReference", it)}
        nwaWorkShopDraft.symbolsAbbreviatedTerms?.let{variable.put("symbolsAbbreviatedTerms", it)}
        nwaWorkShopDraft.clause?.let{variable.put("clause", it)}
        nwaWorkShopDraft.special?.let{variable.put("special", it)}
        nwaWorkShopDraft.taskId?.let{variable.put("taskId", it)}
        nwaWorkShopDraft.processId?.let{variable.put("processId", it)}
        nwaWorkShopDraft.dateWdPrepared = commonDaoServices.getTimestamp()
        variable["dateWdPrepared"] = nwaWorkShopDraft.dateWdPrepared!!
        nwaWorkShopDraft.assignedTo= companyStandardRepository.getDraughtsmanId()

        //print(nwaWorkShopDraft.toString())

        val nwaDetails = nwaWorkshopDraftRepository.save(nwaWorkShopDraft)
        variable["draftId"] = nwaDetails.id
        runtimeService.createProcessInstanceQuery()
            .processInstanceId(nwaWorkShopDraft.processId).list()
            ?.let { l ->
                val processInstance = l[0]

                taskService.complete(nwaWorkShopDraft.taskId, variable)

                taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
                    ?.let { t ->
                        t.list()[0]
                            ?.let { task ->
                                task.assignee =
                                    "${nwaWorkShopDraft.assignedTo ?: throw NullValueNotAllowedException(" invalid user id provided")}"  //set the assignee}"
                                taskService.saveTask(task)
                            }
                            ?: KotlinLogging.logger { }.error("Task list empty for $PROCESS_DEFINITION_KEY ")


                    }
                    ?: KotlinLogging.logger { }.error("No task found for $PROCESS_DEFINITION_KEY ")
                bpmnService.slAssignTask(
                    processInstance.processInstanceId,
                    "draughting",
                    nwaWorkShopDraft.assignedTo ?: throw NullValueNotAllowedException("invalid user id provided")
                )
                return ProcessInstanceWD(
                    nwaDetails.id,
                    processInstance.id,
                    processInstance.isEnded,nwaWorkShopDraft.dateWdPrepared?: throw NullValueNotAllowedException("Date is required")
                )
            }
            ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${nwaWorkShopDraft.processId} ")



    }

    fun draughtStandardDraft(nwaWorkShopDraft: NWAWorkShopDraft,
                             iSDraftStdUpload:ISDraftStdUpload) : ProcessInstanceWD
    {
        val variable:MutableMap<String, Any> = java.util.HashMap()
        nwaWorkShopDraft.title?.let{variable.put("title", it)}
        nwaWorkShopDraft.scope?.let{variable.put("scope", it)}
        nwaWorkShopDraft.normativeReference?.let{variable.put("normativeReference", it)}
        nwaWorkShopDraft.symbolsAbbreviatedTerms?.let{variable.put("symbolsAbbreviatedTerms", it)}
        nwaWorkShopDraft.clause?.let{variable.put("clause", it)}
        nwaWorkShopDraft.special?.let{variable.put("special", it)}
        nwaWorkShopDraft.taskId?.let{variable.put("taskId", it)}
        nwaWorkShopDraft.processId?.let{variable.put("processId", it)}
        nwaWorkShopDraft.dateWdPrepared = commonDaoServices.getTimestamp()
        variable["dateWdPrepared"] = nwaWorkShopDraft.dateWdPrepared!!
        nwaWorkShopDraft.assignedTo= companyStandardRepository.getProofReaderId()

        //print(nwaWorkShopDraft.toString())
        nwaWorkshopDraftRepository.findByIdOrNull(iSDraftStdUpload.draftId)?.let { nwaWorkShopDraft->

            with(nwaWorkShopDraft){
                title=nwaWorkShopDraft.title
                scope=nwaWorkShopDraft.scope
                normativeReference=nwaWorkShopDraft.normativeReference
                symbolsAbbreviatedTerms=nwaWorkShopDraft.symbolsAbbreviatedTerms
                clause=nwaWorkShopDraft.clause
                special=nwaWorkShopDraft.special
                assignedTo=nwaWorkShopDraft.assignedTo
            }

        nwaWorkshopDraftRepository.save(nwaWorkShopDraft)
        runtimeService.createProcessInstanceQuery()
            .processInstanceId(nwaWorkShopDraft.processId).list()
            ?.let { l ->
                val processInstance = l[0]

                taskService.complete(nwaWorkShopDraft.taskId, variable)

                taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
                    ?.let { t ->
                        t.list()[0]
                            ?.let { task ->
                                task.assignee =
                                    "${nwaWorkShopDraft.assignedTo ?: throw NullValueNotAllowedException(" invalid user id provided")}"  //set the assignee}"
                                taskService.saveTask(task)
                            }
                            ?: KotlinLogging.logger { }.error("Task list empty for $PROCESS_DEFINITION_KEY ")


                    }
                    ?: KotlinLogging.logger { }.error("No task found for $PROCESS_DEFINITION_KEY ")
                bpmnService.slAssignTask(
                    processInstance.processInstanceId,
                    "proofreading",
                    nwaWorkShopDraft.assignedTo ?: throw NullValueNotAllowedException("invalid user id provided")
                )
                return ProcessInstanceWD(
                    nwaWorkShopDraft.id,
                    processInstance.id,
                    processInstance.isEnded,nwaWorkShopDraft.dateWdPrepared?: throw NullValueNotAllowedException("Date is required")
                )
            }
            ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${nwaWorkShopDraft.processId} ")
        }?: throw Exception("RECORD NOT FOUND")


    }

    fun proofReadStandardDraft(nwaWorkShopDraft: NWAWorkShopDraft,
                               iSDraftStdUpload:ISDraftStdUpload) : ProcessInstanceWD
    {
        val variable:MutableMap<String, Any> = java.util.HashMap()
        nwaWorkShopDraft.title?.let{variable.put("title", it)}
        nwaWorkShopDraft.scope?.let{variable.put("scope", it)}
        nwaWorkShopDraft.normativeReference?.let{variable.put("normativeReference", it)}
        nwaWorkShopDraft.symbolsAbbreviatedTerms?.let{variable.put("symbolsAbbreviatedTerms", it)}
        nwaWorkShopDraft.clause?.let{variable.put("clause", it)}
        nwaWorkShopDraft.special?.let{variable.put("special", it)}
        nwaWorkShopDraft.taskId?.let{variable.put("taskId", it)}
        nwaWorkShopDraft.processId?.let{variable.put("processId", it)}
        nwaWorkShopDraft.dateWdPrepared = commonDaoServices.getTimestamp()
        variable["dateWdPrepared"] = nwaWorkShopDraft.dateWdPrepared!!
        nwaWorkShopDraft.assignedTo= companyStandardRepository.getHopId()

        //print(nwaWorkShopDraft.toString())
        nwaWorkshopDraftRepository.findByIdOrNull(iSDraftStdUpload.draftId)?.let { nwaWorkShopDraft->

            with(nwaWorkShopDraft){
                title=nwaWorkShopDraft.title
                scope=nwaWorkShopDraft.scope
                normativeReference=nwaWorkShopDraft.normativeReference
                symbolsAbbreviatedTerms=nwaWorkShopDraft.symbolsAbbreviatedTerms
                clause=nwaWorkShopDraft.clause
                special=nwaWorkShopDraft.special
                assignedTo=nwaWorkShopDraft.assignedTo
            }

            nwaWorkshopDraftRepository.save(nwaWorkShopDraft)
            runtimeService.createProcessInstanceQuery()
                .processInstanceId(nwaWorkShopDraft.processId).list()
                ?.let { l ->
                    val processInstance = l[0]

                    taskService.complete(nwaWorkShopDraft.taskId, variable)

                    taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
                        ?.let { t ->
                            t.list()[0]
                                ?.let { task ->
                                    task.assignee =
                                        "${nwaWorkShopDraft.assignedTo ?: throw NullValueNotAllowedException(" invalid user id provided")}"  //set the assignee}"
                                    taskService.saveTask(task)
                                }
                                ?: KotlinLogging.logger { }.error("Task list empty for $PROCESS_DEFINITION_KEY ")


                        }
                        ?: KotlinLogging.logger { }.error("No task found for $PROCESS_DEFINITION_KEY ")
                    bpmnService.slAssignTask(
                        processInstance.processInstanceId,
                        "approveStandardChanges",
                        nwaWorkShopDraft.assignedTo ?: throw NullValueNotAllowedException("invalid user id provided")
                    )
                    return ProcessInstanceWD(
                        nwaWorkShopDraft.id,
                        processInstance.id,
                        processInstance.isEnded,nwaWorkShopDraft.dateWdPrepared?: throw NullValueNotAllowedException("Date is required")
                    )
                }
                ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${nwaWorkShopDraft.processId} ")
        }?: throw Exception("COMMENT NOT FOUND")


    }

    // Upload NWA Standard
    fun uploadISStandard(iSUploadStandard: ISUploadStandard,isJustificationDecision: ISJustificationDecision,
                         internationalStandardRemarks: InternationalStandardRemarks,standard: Standard):  List<InternationalStandardTasks>
    {
        val variable:MutableMap<String, Any> = HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        iSUploadStandard.title?.let{variable.put("title", it)}
        iSUploadStandard.scope?.let{variable.put("scope", it)}
        iSUploadStandard.normativeReference?.let{variable.put("normativeReference", it)}
        iSUploadStandard.symbolsAbbreviatedTerms?.let{variable.put("symbolsAbbreviatedTerms", it)}
        iSUploadStandard.clause?.let{variable.put("clause", it)}
        iSUploadStandard.special?.let{variable.put("special", it)}
        iSUploadStandard.taskId?.let{variable.put("taskId", it)}
        iSUploadStandard.processId?.let{variable.put("processId", it)}
        val isStandard= getISNumber()
        iSUploadStandard.uploadDate = Timestamp(System.currentTimeMillis())
        variable["uploadDate"] = iSUploadStandard.uploadDate!!

        //iSUploadStandard.iSNumber?.let{variable.put("ISNumber", it)}
        standard.title=iSUploadStandard.title
        standard.scope= iSUploadStandard.scope
        standard.normativeReference= iSUploadStandard.normativeReference
        standard.symbolsAbbreviatedTerms= iSUploadStandard.symbolsAbbreviatedTerms
        standard.clause= iSUploadStandard.clause
        standard.special=iSUploadStandard.special
        standard.standardNumber= isStandard
        standard.status=1
        standard.standardType="International Standard"
        standard.dateFormed=iSUploadStandard.uploadDate


        iSUploadStandard.iSNumber = isStandard
        variable["iSNumber"] = iSUploadStandard.iSNumber!!



        //email to legal
        var userList= companyStandardRepository.getSacSecEmailList()
        val targetUrl = "https://kimsint.kebs.org/";
        userList.forEach { item->
            val recipient="stephenmuganda@gmail.com"
            //val recipient= item.getUserEmail()
            val subject = "International Standard Uploaded"+  iSUploadStandard.iSNumber
            val messageBody= "Dear ${item.getFirstName()} ${item.getLastName()},International Standard has been uploaded by the Head of Publishing.Click on the Link below to view. ${targetUrl} "
            if (recipient != null) {
                notifications.sendEmail(recipient, subject, messageBody)
            }
        }

        if(variable["Yes"]==true){
            iSUploadStandard.assignedTo= companyStandardRepository.getHoSicId()
                iSUploadStandardRepository.save(iSUploadStandard)
                standardRepository.save(standard)
                runtimeService.createProcessInstanceQuery()
                    .processInstanceId(iSUploadStandard.processId).list()
                    ?.let { l ->
                        val processInstance = l[0]
                        taskService.complete(iSUploadStandard.taskId, variable)

                        taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
                            ?.let { t ->
                                t.list()[0]
                                    ?.let { task ->
                                        task.assignee = "${
                                            iSUploadStandard.assignedTo ?: throw NullValueNotAllowedException(
                                                " invalid user id provided"
                                            )
                                        }"  //set the assignee}"
                                        //task.dueDate = standardLevyFactoryVisitReportEntity.scheduledVisitDate  //set the due date
                                        taskService.saveTask(task)
                                    }
                                    ?: KotlinLogging.logger { }
                                        .error("Task list empty for $PROCESS_DEFINITION_KEY ")


                            }
                            ?: KotlinLogging.logger { }.error("No task found for $PROCESS_DEFINITION_KEY ")
                        bpmnService.slAssignTask(
                            processInstance.processInstanceId,
                            "uploadGazetteNotice",
                            iSUploadStandard.assignedTo
                                ?: throw NullValueNotAllowedException("invalid user id provided")
                        )

                    }
                    ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${iSUploadStandard.processId} ")


        }else if(variable["No"]==false) {
            val fname=loggedInUser.firstName
            val sname=loggedInUser.lastName
            val usersName= "$fname  $sname"
            internationalStandardRemarks.proposalId= isJustificationDecision.approvalID
            internationalStandardRemarks.remarks= isJustificationDecision.comments
            internationalStandardRemarks.status = 1.toString()
            internationalStandardRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
            internationalStandardRemarks.remarkBy = usersName
            isJustificationDecision.assignedTo= companyStandardRepository.getEditorId()

                internationalStandardRemarksRepository.save(internationalStandardRemarks)
                runtimeService.createProcessInstanceQuery()
                    .processInstanceId(iSUploadStandard.processId).list()
                    ?.let { l ->
                        val processInstance = l[0]
                        taskService.complete(iSUploadStandard.taskId, variable)

                        taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
                            ?.let { t ->
                                t.list()[0]
                                    ?.let { task ->
                                        task.assignee = "${
                                            iSUploadStandard.assignedTo ?: throw NullValueNotAllowedException(
                                                " invalid user id provided"
                                            )
                                        }"  //set the assignee}"
                                        //task.dueDate = standardLevyFactoryVisitReportEntity.scheduledVisitDate  //set the due date
                                        taskService.saveTask(task)
                                    }
                                    ?: KotlinLogging.logger { }
                                        .error("Task list empty for $PROCESS_DEFINITION_KEY ")


                            }
                            ?: KotlinLogging.logger { }.error("No task found for $PROCESS_DEFINITION_KEY ")
                        bpmnService.slAssignTask(
                            processInstance.processInstanceId,
                            "draftStandardEditing",
                            iSUploadStandard.assignedTo
                                ?: throw NullValueNotAllowedException("invalid user id provided")
                        )

                    }
                    ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${iSUploadStandard.processId} ")



        }

        return  getUserTasks()

    }

    fun uploadISDFile(
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
        iSGazetteNotice.taskId?.let{variable.put("taskId", it)}
        iSGazetteNotice.processId?.let{variable.put("processId", it)}
        iSGazetteNotice.assignedTo= companyStandardRepository.getHoSicId()

        iSGazetteNotice.dateUploaded = Timestamp(System.currentTimeMillis())
        variable["dateUploaded"] = iSGazetteNotice.dateUploaded!!
        print(iSGazetteNotice.toString())

        val isuDetails = iSGazetteNoticeRepository.save(iSGazetteNotice)
        println("IS Gazette Notice has been uploaded")

        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variable)
        taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
            ?.let { t ->
                t.list()[0]
                    ?.let { task ->
                        task.assignee =
                            "${iSGazetteNotice.assignedTo ?: throw NullValueNotAllowedException(" invalid user id provided")}"  //set the assignee}"

                        taskService.saveTask(task)
                    }
                    ?: KotlinLogging.logger { }.error("Task list empty for $PROCESS_DEFINITION_KEY ")


            }
            ?: KotlinLogging.logger { }.error("No task found for $PROCESS_DEFINITION_KEY ")
        bpmnService.slAssignTask(
            processInstance.processInstanceId,
            "updateGazetteDate",
            iSGazetteNotice?.assignedTo
                ?: throw NullValueNotAllowedException("invalid user id provided")
        )

        return ProcessInstanceResponseGazzette(isuDetails.id, processInstance.id, processInstance.isEnded
        )
    }

    // Upload NWA Gazette date
    fun updateGazettementDate(iSGazettement: ISGazettement)
    {
        val variable:MutableMap<String, Any> = HashMap()
        iSGazettement.iSNumber?.let{variable.put("iSNumber", it)}
        iSGazettement.dateOfGazettement = Timestamp(System.currentTimeMillis())
        variable["dateOfGazettement"] = iSGazettement.dateOfGazettement!!
        iSGazettement.description?.let{variable.put("description", it)}
        iSGazettement.taskId?.let{variable.put("taskId", it)}
        iSGazettement.processId?.let{variable.put("processId", it)}

        print(iSGazettement.toString())

        iSGazettementRepository.save(iSGazettement)
        taskService.complete(iSGazettement.taskId, variable)
        println("IS Gazzettement date has been updated")

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
}