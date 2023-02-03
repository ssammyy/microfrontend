package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
import org.kebs.app.kotlin.apollo.common.dto.std.ISJustificationDecision
import org.kebs.app.kotlin.apollo.common.dto.std.InternationalStandardTasks
import org.kebs.app.kotlin.apollo.common.dto.std.NamesList
import org.kebs.app.kotlin.apollo.common.dto.std.ProcessInstanceResponse
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.std.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.lang.reflect.Type
import java.sql.Timestamp
import java.util.*


@Service
class IntStandardService(
    private val runtimeService: RuntimeService,
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
    private val internationalStandardRemarksRepository: InternationalStandardRemarksRepository,
    private val nwaWorkshopDraftRepository: NwaWorkShopDraftRepository,
    private val standardRepository: StandardRepository,
    private val userListRepository: UserListRepository,
    private val comStdDraftRepository: ComStdDraftRepository,
    private val comStandardDraftUploadsRepository: ComStandardDraftUploadsRepository,
    private val comStandardDraftCommentsRepository: ComStandardDraftCommentsRepository,


    ) {
    val PROCESS_DEFINITION_KEY = "sd_InternationalStandardsForAdoption"
    val gson = Gson()
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
    //find stakeholder
    fun findStandardStakeholders(): List<UserDetailHolder>? {
        return userListRepository.findStandardStakeholders()
    }


    fun mapKEBSOfficersNameListDto(officersName: String): List<String>? {
        val userListType: Type = object : TypeToken<ArrayList<String?>?>() {}.type
        return gson.fromJson(officersName, userListType)
    }


    //prepare Adoption Proposal
    fun prepareAdoptionProposal(iSAdoptionProposal: ISAdoptionProposal, stakeholders: MutableList<NamesList>?) : ComStdDraft
    {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val variables: MutableMap<String, Any> = HashMap()
        val datePrepared=commonDaoServices.getTimestamp()
        iSAdoptionProposal.proposal_doc_name=iSAdoptionProposal.proposal_doc_name
        iSAdoptionProposal.circulationDate=iSAdoptionProposal.circulationDate
        iSAdoptionProposal.closingDate=iSAdoptionProposal.circulationDate
        iSAdoptionProposal.tcSecName=iSAdoptionProposal.tcSecName
        iSAdoptionProposal.title=iSAdoptionProposal.title
        iSAdoptionProposal.scope=iSAdoptionProposal.scope
        iSAdoptionProposal.iStandardNumber=iSAdoptionProposal.iStandardNumber

        iSAdoptionProposal.uploadedBy=iSAdoptionProposal.uploadedBy
        iSAdoptionProposal.preparedDate = datePrepared
        iSAdoptionProposal.status = 0
        iSAdoptionProposal.proposalNumber = getPRNumber()
        val deadline: Timestamp = Timestamp.valueOf(datePrepared.toLocalDateTime().plusDays(30))
        iSAdoptionProposal.deadlineDate=deadline

        val proposal =isAdoptionProposalRepository.save(iSAdoptionProposal)

        val proposalId=proposal.id

        val cs = ComStdDraft()
        cs.draftNumber = getDRNumber()
        cs.title= iSAdoptionProposal.title
        cs.deadlineDate=deadline
        cs.proposalId=proposalId
        cs.comStdNumber=iSAdoptionProposal.iStandardNumber
        cs.scope=iSAdoptionProposal.scope
        cs.companyName="KEBS"
        cs.contactOneEmail=loggedInUser.email
        cs.contactOneFullName=loggedInUser.firstName + loggedInUser.lastName
        cs.contactOneTelephone=loggedInUser.cellphone
        cs.status = 0
        cs.uploadDate=datePrepared
        cs.standardType="International Standard"


        val draftId=comStdDraftRepository.save(cs)

        //iSAdoptionProposal.stakeholdersList=iSAdoptionProposal.stakeholdersList
        iSAdoptionProposal.addStakeholdersList=iSAdoptionProposal.addStakeholdersList

        //val listOne= iSAdoptionProposal.stakeholdersList?.let { mapKEBSOfficersNameListDto(it) }
        val listTwo= iSAdoptionProposal.addStakeholdersList?.let { mapKEBSOfficersNameListDto(it) }

        val targetUrl = "https://kimsint.kebs.org/isPropComments/$draftId";
        stakeholders?.forEach { s ->
            val subject = "New Adoption Proposal Document"+  iSAdoptionProposal.proposalNumber
            val recipient = s.email
            val user = s.name
            val messageBody= "Dear $user,An adoption document has been uploaded.Click on the Link below to post Comment. $targetUrl "
            if (recipient != null) {
                notifications.sendEmail(recipient, subject, messageBody)
            }
        }

        val targetUrl2 = "https://kimsint.kebs.org/isProposalComments/$draftId";
        if (listTwo != null) {
            for (recipient in listTwo) {
                val subject = "New Adoption Proposal Document"+  iSAdoptionProposal.proposalNumber
                val messageBody= "Hope You are Well,An adoption document has been uploaded.Click on the Link below to post Comment. $targetUrl2 "
                notifications.sendEmail(recipient, subject, messageBody)

            }
        }

     return draftId

    }

    // Upload International Draft
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
            description = DocDescription
            document = docFile.bytes
            transactionDate = commonDaoServices.getCurrentDate()
            status = 1
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }

        return comStandardDraftUploadsRepository.save(uploads)
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

    fun getProposals(proposalId: Long): MutableList<ProposalDetails> {
        return isAdoptionProposalRepository.getProposals(proposalId)
    }

    fun submitDraftComments(comDraftComments: ComDraftComments){
        val variables: MutableMap<String, Any> = HashMap()
        comDraftComments.uploadDate=comDraftComments.uploadDate
        comDraftComments.emailOfRespondent=comDraftComments.emailOfRespondent
        comDraftComments.phoneOfRespondent=comDraftComments.phoneOfRespondent
        comDraftComments.observation=comDraftComments.observation
        comDraftComments.draftComment=comDraftComments.draftComment
        comDraftComments.commentTitle=comDraftComments.commentTitle
        comDraftComments.commentDocumentType=comDraftComments.commentDocumentType
        comDraftComments.comClause=comDraftComments.comClause
        comDraftComments.comParagraph=comDraftComments.comParagraph
        comDraftComments.typeOfComment=comDraftComments.typeOfComment
        comDraftComments.proposedChange=comDraftComments.proposedChange
        comDraftComments.requestID=comDraftComments.requestID
        comDraftComments.draftID=comDraftComments.draftID
        comDraftComments.recommendations=comDraftComments.recommendations
        comDraftComments.nameOfRespondent=comDraftComments.nameOfRespondent
        comDraftComments.positionOfRespondent=comDraftComments.positionOfRespondent
        comDraftComments.nameOfOrganization=comDraftComments.nameOfOrganization
        comDraftComments.adoptStandard=comDraftComments.adoptStandard
        comDraftComments.adoptDraft=comDraftComments.adoptDraft
        comDraftComments.reason=comDraftComments.reason
        comDraftComments.commentTime = Timestamp(System.currentTimeMillis())
        comStandardDraftCommentsRepository.save(comDraftComments)

        val commentNumber=comStdDraftRepository.getDraftCommentCount(comDraftComments.draftID)



        comStdDraftRepository.findByIdOrNull(comDraftComments.draftID)?.let { comStdDraft ->
            with(comStdDraft) {
                commentCount= commentNumber+1

            }
            comStdDraftRepository.save(comStdDraft)
        }?: throw Exception("REQUEST NOT FOUND")


        println("Comment Submitted")
    }


    //Submit Adoption Proposal comments
    fun submitAPComments(isAdoptionComments: ISAdoptionComments){
        val variables: MutableMap<String, Any> = HashMap()

        isAdoptionComments.adoption_proposal_comment=isAdoptionComments.adoption_proposal_comment
        isAdoptionComments.proposalID=isAdoptionComments.proposalID
        isAdoptionComments.commentTitle=isAdoptionComments.commentTitle
        isAdoptionComments.commentDocumentType=isAdoptionComments.commentDocumentType
        isAdoptionComments.comNameOfOrganization=isAdoptionComments.comNameOfOrganization
        isAdoptionComments.comClause=isAdoptionComments.comClause
        isAdoptionComments.scope=isAdoptionComments.scope
        isAdoptionComments.dateOfApplication=isAdoptionComments.dateOfApplication
        isAdoptionComments.comParagraph=isAdoptionComments.comParagraph
        isAdoptionComments.observation=isAdoptionComments.observation
        isAdoptionComments.typeOfComment=isAdoptionComments.typeOfComment
        isAdoptionComments.proposedChange=isAdoptionComments.proposedChange
        isAdoptionComments.recommendations=isAdoptionComments.recommendations
        isAdoptionComments.nameOfRespondent=isAdoptionComments.nameOfRespondent
        isAdoptionComments.positionOfRespondent=isAdoptionComments.positionOfRespondent
        isAdoptionComments.nameOfOrganization=isAdoptionComments.nameOfOrganization

        isAdoptionComments.comment_time = Timestamp(System.currentTimeMillis())
        isAdoptionCommentsRepository.save(isAdoptionComments)

        val commentNumber=isAdoptionProposalRepository.getCommentCount(isAdoptionComments.proposalID)

        isAdoptionProposalRepository.findByIdOrNull(isAdoptionComments.proposalID)?.let { iSAdoptionProposal ->
            with(iSAdoptionProposal) {
                noOfComments= commentNumber+1

            }
            isAdoptionProposalRepository.save(iSAdoptionProposal)
        }?: throw Exception("REQUEST NOT FOUND")

        println("Comment Submitted")
    }


    fun getProposalComments(proposalId: Long): MutableIterable<ISProposalComments>? {
        return isAdoptionCommentsRepository.getProposalComments(proposalId)
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
    fun decisionOnProposal(
        iSAdoptionProposal: ISAdoptionProposal,
        internationalStandardRemarks: InternationalStandardRemarks
    ) : String {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        iSAdoptionProposal.accentTo=iSAdoptionProposal.accentTo
        val decision=iSAdoptionProposal.accentTo

        val fName = loggedInUser.firstName
        val sName = loggedInUser.lastName
        val usersName = "$fName  $sName"
        internationalStandardRemarks.proposalId= internationalStandardRemarks.proposalId
        internationalStandardRemarks.remarks= internationalStandardRemarks.remarks
        internationalStandardRemarks.status = 1.toString()
        internationalStandardRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
        internationalStandardRemarks.remarkBy = usersName
        internationalStandardRemarks.role = "TC SEC"


        if (decision == "Yes") {

            isAdoptionProposalRepository.findByIdOrNull(internationalStandardRemarks.proposalId)?.let { iSAdoptionProposal ->
                with(iSAdoptionProposal) {
                    status = 1

                }
                isAdoptionProposalRepository.save(iSAdoptionProposal)
                internationalStandardRemarksRepository.save(internationalStandardRemarks)
            }?: throw Exception("PROPOSAL NOT FOUND")

        } else if (decision == "No") {
            isAdoptionProposalRepository.findByIdOrNull(internationalStandardRemarks.proposalId)?.let { iSAdoptionProposal ->

                with(iSAdoptionProposal) {
                    status = 4
                }
                isAdoptionProposalRepository.save(iSAdoptionProposal)
                internationalStandardRemarksRepository.save(internationalStandardRemarks)
            } ?: throw Exception("PROPOSAL NOT FOUND")

        }

        return "Actioned"
    }

    fun getApprovedProposals(): MutableList<ProposalDetails>{
        return isAdoptionProposalRepository.getApprovedProposals();
    }


    fun getUserComments(id: Long): MutableIterable<InternationalStandardRemarks>? {
        return internationalStandardRemarksRepository.findAllByProposalIdOrderByIdDesc(id)
    }

    //prepare justification
    fun prepareJustification(iSAdoptionJustification: ISAdoptionJustification) : ISAdoptionJustification
    {
        val variables: MutableMap<String, Any> = HashMap()
        iSAdoptionJustification.meetingDate=iSAdoptionJustification.meetingDate
        iSAdoptionJustification.tcSec_id=iSAdoptionJustification.tcSec_id
        iSAdoptionJustification.slNumber=iSAdoptionJustification.slNumber
        iSAdoptionJustification.edition=iSAdoptionJustification.edition
        iSAdoptionJustification.requestedBy=iSAdoptionJustification.requestedBy
        iSAdoptionJustification.issuesAddressed=iSAdoptionJustification.issuesAddressed
        iSAdoptionJustification.tcAcceptanceDate=iSAdoptionJustification.tcAcceptanceDate
        iSAdoptionJustification.referenceMaterial=iSAdoptionJustification.referenceMaterial
        iSAdoptionJustification.department=iSAdoptionJustification.department
        iSAdoptionJustification.status= 0.toString()
        iSAdoptionJustification.positiveVotes=iSAdoptionJustification.positiveVotes
        iSAdoptionJustification.negativeVotes=iSAdoptionJustification.negativeVotes
        iSAdoptionJustification.remarks=iSAdoptionJustification.remarks
        iSAdoptionJustification.proposalId=iSAdoptionJustification.proposalId

        iSAdoptionJustification.submissionDate = Timestamp(System.currentTimeMillis())

        iSAdoptionJustification.requestNumber = getRQNumber()
        iSAdoptionJustification.departmentName = departmentListRepository.findNameById(iSAdoptionJustification.department?.toLong())


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
        isAdoptionProposalRepository.findByIdOrNull(iSAdoptionJustification.proposalId)?.let { iSAdoptionProposal ->
            with(iSAdoptionProposal) {
                status = 2

            }
            isAdoptionProposalRepository.save(iSAdoptionProposal)
        } ?: throw Exception("PROPOSAL NOT FOUND")

        return iSAdoptionJustificationRepository.save(iSAdoptionJustification)

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

    fun getISJustification(): MutableList<ISAdoptionProposalJustification> {
        return iSAdoptionJustificationRepository.getISJustification()
    }

    //Get IS justification Document
    fun findUploadedJSFileBYId(isJSDocumentId: Long): ISJustificationUploads {
        return isJustificationUploadsRepository.findByIsJSDocumentId(isJSDocumentId) ?: throw ExpectedDataNotFound("No File found with the following [ id=$isJSDocumentId]")
    }


    // Decision on Justification
    fun decisionOnJustification(
        iSAdoptionJustification: ISAdoptionJustification,
        internationalStandardRemarks: InternationalStandardRemarks
    ) : String {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        iSAdoptionJustification.accentTo=iSAdoptionJustification.accentTo
        val decision=iSAdoptionJustification.accentTo

        val fName = loggedInUser.firstName
        val sName = loggedInUser.lastName
        val usersName = "$fName  $sName"
        internationalStandardRemarks.proposalId= internationalStandardRemarks.proposalId
        internationalStandardRemarks.remarks= internationalStandardRemarks.remarks
        internationalStandardRemarks.status = 1.toString()
        internationalStandardRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
        internationalStandardRemarks.remarkBy = usersName
        internationalStandardRemarks.role = "TC SEC"

        if (decision == "Yes") {
            iSAdoptionJustificationRepository.findByIdOrNull(iSAdoptionJustification.id)?.let { iSAdoptionJustification ->
                with(iSAdoptionJustification) {
                    status = 1.toString()

                }
                iSAdoptionJustificationRepository.save(iSAdoptionJustification)
                internationalStandardRemarksRepository.save(internationalStandardRemarks)
            }?: throw Exception("JUSTIFICATION NOT FOUND")

        } else if (decision == "No") {
            iSAdoptionJustificationRepository.findByIdOrNull(iSAdoptionJustification.id)?.let { iSAdoptionJustification ->

                with(iSAdoptionJustification) {
                    status = 2.toString()
                }
                iSAdoptionJustificationRepository.save(iSAdoptionJustification)
                internationalStandardRemarksRepository.save(internationalStandardRemarks)
            } ?: throw Exception("JUSTIFICATION NOT FOUND")

        }

        return "Actioned"
    }

    fun getApprovedISJustification(): MutableList<ISAdoptionProposalJustification> {
        return iSAdoptionJustificationRepository.getApprovedISJustification()
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

    fun submitDraftForEditing(iSUploadStandard: ISUploadStandard) : ISUploadStandard
    {
        val variable: MutableMap<String, Any> = HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        iSUploadStandard.title=iSUploadStandard.title
        iSUploadStandard.scope=iSUploadStandard.scope
        iSUploadStandard.normativeReference=iSUploadStandard.normativeReference
        iSUploadStandard.symbolsAbbreviatedTerms=iSUploadStandard.symbolsAbbreviatedTerms
        iSUploadStandard.clause=iSUploadStandard.clause
        iSUploadStandard.special=iSUploadStandard.special
        iSUploadStandard.justificationNo=iSUploadStandard.justificationNo
        iSUploadStandard.proposalId=iSUploadStandard.proposalId
        iSUploadStandard.status=0
        iSUploadStandard.uploadDate = Timestamp(System.currentTimeMillis())
        iSUploadStandard.iSNumber = iSUploadStandard.iSNumber
        iSUploadStandard.preparedBy=iSUploadStandard.preparedBy
        iSUploadStandard.documentType=iSUploadStandard.documentType

        var userList= companyStandardRepository.getHopEmailList()

        //email to Head of publishing
        val targetUrl = "https://kimsint.kebs.org/";
        userList.forEach { item->
            //val recipient="stephenmuganda@gmail.com"
            val recipient= item.getUserEmail()
            val subject = "Standard Draft"
            val messageBody= "Dear ${item.getFirstName()} ${item.getLastName()}, A standard draft has been uploaded."
            if (recipient != null) {
                notifications.sendEmail(recipient, subject, messageBody)
            }
        }

        iSAdoptionJustificationRepository.findByIdOrNull(iSUploadStandard.justificationNo)?.let { iSAdoptionJustification ->
            with(iSAdoptionJustification) {
                status = 3.toString()

            }
            iSAdoptionJustificationRepository.save(iSAdoptionJustification)
        }?: throw Exception("JUSTIFICATION NOT FOUND")

        return iSUploadStandardRepository.save(iSUploadStandard)
    }

    fun getUploadedDraft(): MutableList<ISUploadedDraft> {
        return iSUploadStandardRepository.getUploadedDraft()
    }

    fun getIsPublishingTasks(): MutableList<ISUploadedDraft> {
        return iSUploadStandardRepository.getIsPublishingTasks()
    }


    fun checkRequirements(
        iSUploadStandard: ISUploadStandard,
        internationalStandardRemarks: InternationalStandardRemarks
    ) : String {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        iSUploadStandard.accentTo=iSUploadStandard.accentTo
        val decision=iSUploadStandard.accentTo

        val fName = loggedInUser.firstName
        val sName = loggedInUser.lastName
        val usersName = "$fName  $sName"
        internationalStandardRemarks.proposalId= internationalStandardRemarks.proposalId
        internationalStandardRemarks.remarks= internationalStandardRemarks.remarks
        internationalStandardRemarks.status = 1.toString()
        internationalStandardRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
        internationalStandardRemarks.remarkBy = usersName
        internationalStandardRemarks.role = "HOP"

        if (decision == "Yes") {
            iSUploadStandardRepository.findByIdOrNull(iSUploadStandard.id)?.let { iSUploadStandard ->
                with(iSUploadStandard) {
                    status = 1

                }
                iSUploadStandardRepository.save(iSUploadStandard)
                internationalStandardRemarksRepository.save(internationalStandardRemarks)
            }?: throw Exception("DRAFT NOT FOUND")

        } else if (decision == "No") {
            iSAdoptionJustificationRepository.findByIdOrNull(iSUploadStandard.justificationNo)?.let { iSAdoptionJustification ->

                with(iSAdoptionJustification) {
                    status = 1.toString()
                }
                iSAdoptionJustificationRepository.save(iSAdoptionJustification)

                iSUploadStandardRepository.findByIdOrNull(iSUploadStandard.id)?.let { iSUploadStandard ->

                    with(iSUploadStandard) {
                        status = 2
                    }
                    iSUploadStandardRepository.save(iSUploadStandard)
                    internationalStandardRemarksRepository.save(internationalStandardRemarks)

                } ?: throw Exception("JUSTIFICATION NOT FOUND")

            } ?: throw Exception("JUSTIFICATION NOT FOUND")


        }

        return "Actioned"
    }

    fun getApprovedDraft(): MutableList<ISUploadedDraft> {
        return iSUploadStandardRepository.getApprovedDraft()
    }




    fun editStandardDraft(iSUploadStandard: ISUploadStandard) : ISUploadStandard {
        val variable: MutableMap<String, Any> = HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val draft= ISUploadStandard()
        iSUploadStandard.title = iSUploadStandard.title
        iSUploadStandard.scope = iSUploadStandard.scope
        iSUploadStandard.normativeReference = iSUploadStandard.normativeReference
        iSUploadStandard.symbolsAbbreviatedTerms = iSUploadStandard.symbolsAbbreviatedTerms
        iSUploadStandard.clause = iSUploadStandard.clause
        iSUploadStandard.special = iSUploadStandard.special
        iSUploadStandard.justificationNo = iSUploadStandard.justificationNo
        iSUploadStandard.proposalId = iSUploadStandard.proposalId
        iSUploadStandard.documentType=iSUploadStandard.documentType
        iSUploadStandard.iSNumber=iSUploadStandard.iSNumber
        iSUploadStandard.draughting=iSUploadStandard.draughting
        val draughting=iSUploadStandard.draughting
        val uploadDate = Timestamp(System.currentTimeMillis())
        val deadline: Timestamp = Timestamp.valueOf(uploadDate.toLocalDateTime().plusDays(30))
        iSUploadStandard.deadlineDate=deadline


        iSUploadStandardRepository.findByIdOrNull(iSUploadStandard.id)?.let { iSUploadStandard ->

            with(iSUploadStandard) {
                status = if (draughting =="Yes"){
                    3
                }else{
                    4
                }
                title = iSUploadStandard.title
                scope = iSUploadStandard.scope
                normativeReference = iSUploadStandard.normativeReference
                symbolsAbbreviatedTerms = iSUploadStandard.symbolsAbbreviatedTerms
                clause = iSUploadStandard.clause
                special = iSUploadStandard.special
                iSNumber = iSUploadStandard.iSNumber
                documentType = iSUploadStandard.documentType
            }
            iSUploadStandardRepository.save(iSUploadStandard)

        } ?: throw Exception("DRAFT NOT FOUND")

        return draft
    }

    fun getEditedDraft(): MutableList<ISUploadedDraft> {
        return iSUploadStandardRepository.getEditedDraft()
    }


    fun draughtStandard(iSUploadStandard: ISUploadStandard) : ISUploadStandard {
        val variable: MutableMap<String, Any> = HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val draft= ISUploadStandard()
        iSUploadStandard.title = iSUploadStandard.title
        iSUploadStandard.scope = iSUploadStandard.scope
        iSUploadStandard.normativeReference = iSUploadStandard.normativeReference
        iSUploadStandard.symbolsAbbreviatedTerms = iSUploadStandard.symbolsAbbreviatedTerms
        iSUploadStandard.clause = iSUploadStandard.clause
        iSUploadStandard.special = iSUploadStandard.special
        iSUploadStandard.justificationNo = iSUploadStandard.justificationNo
        iSUploadStandard.proposalId = iSUploadStandard.proposalId
        iSUploadStandard.documentType=iSUploadStandard.documentType
        iSUploadStandard.iSNumber=iSUploadStandard.iSNumber
        iSUploadStandardRepository.findByIdOrNull(iSUploadStandard.id)?.let { iSUploadStandard ->

            with(iSUploadStandard) {
                status = 4
                title = iSUploadStandard.title
                scope = iSUploadStandard.scope
                normativeReference = iSUploadStandard.normativeReference
                symbolsAbbreviatedTerms = iSUploadStandard.symbolsAbbreviatedTerms
                clause = iSUploadStandard.clause
                special = iSUploadStandard.special
                iSNumber = iSUploadStandard.iSNumber
                documentType = iSUploadStandard.documentType
            }
            iSUploadStandardRepository.save(iSUploadStandard)

        } ?: throw Exception("DRAFT NOT FOUND")

        return draft
    }

    fun getDraughtedDraft(): MutableList<ISUploadedDraft> {
        return iSUploadStandardRepository.getDraughtedDraft()
    }



    fun proofReadStandard(iSUploadStandard: ISUploadStandard) : ISUploadStandard {
        val variable: MutableMap<String, Any> = HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val draft= ISUploadStandard()
        iSUploadStandard.title = iSUploadStandard.title
        iSUploadStandard.scope = iSUploadStandard.scope
        iSUploadStandard.normativeReference = iSUploadStandard.normativeReference
        iSUploadStandard.symbolsAbbreviatedTerms = iSUploadStandard.symbolsAbbreviatedTerms
        iSUploadStandard.clause = iSUploadStandard.clause
        iSUploadStandard.special = iSUploadStandard.special
        iSUploadStandard.justificationNo = iSUploadStandard.justificationNo
        iSUploadStandard.proposalId = iSUploadStandard.proposalId
        iSUploadStandard.documentType=iSUploadStandard.documentType
        iSUploadStandard.iSNumber=iSUploadStandard.iSNumber
        iSUploadStandardRepository.findByIdOrNull(iSUploadStandard.id)?.let { iSUploadStandard ->

            with(iSUploadStandard) {
                status = 5
                title = iSUploadStandard.title
                scope = iSUploadStandard.scope
                normativeReference = iSUploadStandard.normativeReference
                symbolsAbbreviatedTerms = iSUploadStandard.symbolsAbbreviatedTerms
                clause = iSUploadStandard.clause
                special = iSUploadStandard.special
                iSNumber = iSUploadStandard.iSNumber
                documentType = iSUploadStandard.documentType
            }
            iSUploadStandardRepository.save(iSUploadStandard)

        } ?: throw Exception("DRAFT NOT FOUND")

        return draft
    }

    fun getProofReadDraft(): MutableList<ISUploadedDraft> {
        return iSUploadStandardRepository.getProofReadDraft()
    }

    fun approveProofReadStandard(
        iSUploadStandard: ISUploadStandard,
        internationalStandardRemarks: InternationalStandardRemarks
    ) : String {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        iSUploadStandard.accentTo=iSUploadStandard.accentTo
        val decision=iSUploadStandard.accentTo

        val fName = loggedInUser.firstName
        val sName = loggedInUser.lastName
        val usersName = "$fName  $sName"
        internationalStandardRemarks.proposalId= internationalStandardRemarks.proposalId
        internationalStandardRemarks.remarks= internationalStandardRemarks.remarks
        internationalStandardRemarks.status = 1.toString()
        internationalStandardRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
        internationalStandardRemarks.remarkBy = usersName
        internationalStandardRemarks.role = "HOP"


            iSUploadStandardRepository.findByIdOrNull(iSUploadStandard.id)?.let { iSUploadStandard ->
                with(iSUploadStandard) {
                    status = 6

                }
                iSUploadStandardRepository.save(iSUploadStandard)
                internationalStandardRemarksRepository.save(internationalStandardRemarks)
            }?: throw Exception("DRAFT NOT FOUND")



        return "Actioned"
    }

    fun getApprovedProofReadDraft(): MutableList<ISUploadedDraft> {
        return iSUploadStandardRepository.getApprovedProofReadDraft()
    }

    fun approveEditedStandard(
        iSUploadStandard: ISUploadStandard,
        internationalStandardRemarks: InternationalStandardRemarks
    ) : String {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        iSUploadStandard.accentTo=iSUploadStandard.accentTo
        val decision=iSUploadStandard.accentTo

        val fName = loggedInUser.firstName
        val sName = loggedInUser.lastName
        val usersName = "$fName  $sName"
        internationalStandardRemarks.proposalId= internationalStandardRemarks.proposalId
        internationalStandardRemarks.remarks= internationalStandardRemarks.remarks
        internationalStandardRemarks.status = 1.toString()
        internationalStandardRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
        internationalStandardRemarks.remarkBy = usersName
        internationalStandardRemarks.role = "HOP"

        if (decision == "Yes") {
            iSUploadStandardRepository.findByIdOrNull(iSUploadStandard.id)?.let { iSUploadStandard ->
                with(iSUploadStandard) {
                    status = 7

                }
                iSUploadStandardRepository.save(iSUploadStandard)
                internationalStandardRemarksRepository.save(internationalStandardRemarks)
            }?: throw Exception("DRAFT NOT FOUND")

        } else if (decision == "No") {

                iSUploadStandardRepository.findByIdOrNull(iSUploadStandard.id)?.let { iSUploadStandard ->

                    with(iSUploadStandard) {
                        status = 1
                    }
                    iSUploadStandardRepository.save(iSUploadStandard)
                    internationalStandardRemarksRepository.save(internationalStandardRemarks)

                } ?: throw Exception("DRAFT NOT FOUND")




        }

        return "Actioned"
    }

    fun getApprovedEditedDraft(): MutableList<ISUploadedDraft> {
        return iSUploadStandardRepository.getApprovedEditedDraft()
    }

    fun approveInternationalStandard(
        iSUploadStandard: ISUploadStandard,
        internationalStandardRemarks: InternationalStandardRemarks,
        standard: Standard
    ) : String {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        iSUploadStandard.accentTo=iSUploadStandard.accentTo
        val decision=iSUploadStandard.accentTo

        standard.title=standard.title
        standard.normativeReference=standard.normativeReference
        standard.symbolsAbbreviatedTerms=standard.symbolsAbbreviatedTerms
        standard.clause=standard.clause
        standard.scope=standard.scope
        standard.special=standard.special
        //val valueFound =getISDNumber()
        standard.standardNumber=standard.standardNumber
        //standard.isdn=valueFound.second
        standard.standardType="International Standard"
        standard.status=0
        standard.dateFormed=Timestamp(System.currentTimeMillis())



        val fName = loggedInUser.firstName
        val sName = loggedInUser.lastName
        val usersName = "$fName  $sName"
        internationalStandardRemarks.proposalId= internationalStandardRemarks.proposalId
        internationalStandardRemarks.remarks= internationalStandardRemarks.remarks
        internationalStandardRemarks.status = 1.toString()
        internationalStandardRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
        internationalStandardRemarks.remarkBy = usersName
        internationalStandardRemarks.role = "SAC"

        if (decision == "Yes") {
            iSUploadStandardRepository.findByIdOrNull(iSUploadStandard.id)?.let { iSUploadStandard ->
                with(iSUploadStandard) {
                    status = 8

                }
                iSUploadStandardRepository.save(iSUploadStandard)
                standardRepository.save(standard)
                internationalStandardRemarksRepository.save(internationalStandardRemarks)
                var userList= companyStandardRepository.getSacSecEmailList()
                val targetUrl = "https://kimsint.kebs.org/";
                userList.forEach { item->
                    //val recipient="stephenmuganda@gmail.com"
                    val recipient= item.getUserEmail()
                    val subject = "New Company Standard"+ standard.standardNumber
                    val messageBody= "Dear ${item.getFirstName()} ${item.getLastName()},A New standard has been approved and uploaded.Click on the Link below to view. ${targetUrl} "
                    if (recipient != null) {
                        notifications.sendEmail(recipient, subject, messageBody)
                    }
                }

            }?: throw Exception("DRAFT NOT FOUND")

        } else if (decision == "No") {

            iSUploadStandardRepository.findByIdOrNull(iSUploadStandard.id)?.let { iSUploadStandard ->

                with(iSUploadStandard) {
                    status = 1
                }
                iSUploadStandardRepository.save(iSUploadStandard)
                internationalStandardRemarksRepository.save(internationalStandardRemarks)

            } ?: throw Exception("DRAFT NOT FOUND")




        }

        return "Actioned"
    }

    fun getStandardForGazettement(): MutableList<ISUploadedDraft> {
        return standardRepository.getStandardForGazettement()
    }

    fun uploadGazetteNotice(standard: Standard) : Standard {
        val standard= Standard()
        standard.description=standard.description

        standardRepository.findByIdOrNull(standard.id)?.let { standard ->

            with(standard) {
                status = 1
                description = standard.description
                standard.isGazetted=1
                standard.dateOfGazettement= Timestamp(System.currentTimeMillis())
            }
            standardRepository.save(standard)

        } ?: throw Exception("STANDARD NOT FOUND")

        return standard
    }


    // Upload NWA Standard
//    fun uploadISStandard(iSUploadStandard: ISUploadStandard,isJustificationDecision: ISJustificationDecision,
//                         internationalStandardRemarks: InternationalStandardRemarks,standard: Standard):  List<InternationalStandardTasks>
//    {
//        val variable:MutableMap<String, Any> = HashMap()
//        val loggedInUser = commonDaoServices.loggedInUserDetails()
//        iSUploadStandard.title?.let{variable.put("title", it)}
//        iSUploadStandard.scope?.let{variable.put("scope", it)}
//        iSUploadStandard.normativeReference?.let{variable.put("normativeReference", it)}
//        iSUploadStandard.symbolsAbbreviatedTerms?.let{variable.put("symbolsAbbreviatedTerms", it)}
//        iSUploadStandard.clause?.let{variable.put("clause", it)}
//        iSUploadStandard.special?.let{variable.put("special", it)}
//        iSUploadStandard.taskId?.let{variable.put("taskId", it)}
//        iSUploadStandard.processId?.let{variable.put("processId", it)}
//        val isStandard= getISNumber()
//        iSUploadStandard.uploadDate = Timestamp(System.currentTimeMillis())
//        variable["uploadDate"] = iSUploadStandard.uploadDate!!
//
//        //iSUploadStandard.iSNumber?.let{variable.put("ISNumber", it)}
//        standard.title=iSUploadStandard.title
//        standard.scope= iSUploadStandard.scope
//        standard.normativeReference= iSUploadStandard.normativeReference
//        standard.symbolsAbbreviatedTerms= iSUploadStandard.symbolsAbbreviatedTerms
//        standard.clause= iSUploadStandard.clause
//        standard.special=iSUploadStandard.special
//        standard.standardNumber= isStandard
//        standard.status=1
//        standard.standardType="International Standard"
//        standard.dateFormed=iSUploadStandard.uploadDate
//
//
//        iSUploadStandard.iSNumber = isStandard
//        variable["iSNumber"] = iSUploadStandard.iSNumber!!
//
//
//
//        //email to legal
//        var userList= companyStandardRepository.getSacSecEmailList()
//        val targetUrl = "https://kimsint.kebs.org/";
//        userList.forEach { item->
//            val recipient="stephenmuganda@gmail.com"
//            //val recipient= item.getUserEmail()
//            val subject = "International Standard Uploaded"+  iSUploadStandard.iSNumber
//            val messageBody= "Dear ${item.getFirstName()} ${item.getLastName()},International Standard has been uploaded by the Head of Publishing.Click on the Link below to view. ${targetUrl} "
//            if (recipient != null) {
//                notifications.sendEmail(recipient, subject, messageBody)
//            }
//        }
//
//        if(variable["Yes"]==true){
//            iSUploadStandard.assignedTo= companyStandardRepository.getHoSicId()
//                iSUploadStandardRepository.save(iSUploadStandard)
//                standardRepository.save(standard)
//                runtimeService.createProcessInstanceQuery()
//                    .processInstanceId(iSUploadStandard.processId).list()
//                    ?.let { l ->
//                        val processInstance = l[0]
//                        taskService.complete(iSUploadStandard.taskId, variable)
//
//                        taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
//                            ?.let { t ->
//                                t.list()[0]
//                                    ?.let { task ->
//                                        task.assignee = "${
//                                            iSUploadStandard.assignedTo ?: throw NullValueNotAllowedException(
//                                                " invalid user id provided"
//                                            )
//                                        }"  //set the assignee}"
//                                        //task.dueDate = standardLevyFactoryVisitReportEntity.scheduledVisitDate  //set the due date
//                                        taskService.saveTask(task)
//                                    }
//                                    ?: KotlinLogging.logger { }
//                                        .error("Task list empty for $PROCESS_DEFINITION_KEY ")
//
//
//                            }
//                            ?: KotlinLogging.logger { }.error("No task found for $PROCESS_DEFINITION_KEY ")
//                        bpmnService.slAssignTask(
//                            processInstance.processInstanceId,
//                            "uploadGazetteNotice",
//                            iSUploadStandard.assignedTo
//                                ?: throw NullValueNotAllowedException("invalid user id provided")
//                        )
//
//                    }
//                    ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${iSUploadStandard.processId} ")
//
//
//        }else if(variable["No"]==false) {
//            val fname=loggedInUser.firstName
//            val sname=loggedInUser.lastName
//            val usersName= "$fname  $sname"
//            internationalStandardRemarks.proposalId= isJustificationDecision.approvalID
//            internationalStandardRemarks.remarks= isJustificationDecision.comments
//            internationalStandardRemarks.status = 1.toString()
//            internationalStandardRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
//            internationalStandardRemarks.remarkBy = usersName
//            isJustificationDecision.assignedTo= companyStandardRepository.getEditorId()
//
//                internationalStandardRemarksRepository.save(internationalStandardRemarks)
//                runtimeService.createProcessInstanceQuery()
//                    .processInstanceId(iSUploadStandard.processId).list()
//                    ?.let { l ->
//                        val processInstance = l[0]
//                        taskService.complete(iSUploadStandard.taskId, variable)
//
//                        taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
//                            ?.let { t ->
//                                t.list()[0]
//                                    ?.let { task ->
//                                        task.assignee = "${
//                                            iSUploadStandard.assignedTo ?: throw NullValueNotAllowedException(
//                                                " invalid user id provided"
//                                            )
//                                        }"  //set the assignee}"
//                                        //task.dueDate = standardLevyFactoryVisitReportEntity.scheduledVisitDate  //set the due date
//                                        taskService.saveTask(task)
//                                    }
//                                    ?: KotlinLogging.logger { }
//                                        .error("Task list empty for $PROCESS_DEFINITION_KEY ")
//
//
//                            }
//                            ?: KotlinLogging.logger { }.error("No task found for $PROCESS_DEFINITION_KEY ")
//                        bpmnService.slAssignTask(
//                            processInstance.processInstanceId,
//                            "draftStandardEditing",
//                            iSUploadStandard.assignedTo
//                                ?: throw NullValueNotAllowedException("invalid user id provided")
//                        )
//
//                    }
//                    ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${iSUploadStandard.processId} ")
//
//
//
//        }
//
//        return  getUserTasks()
//
//    }

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




//    // Upload NWA Gazette date
//    fun updateGazettementDate(iSGazettement: ISGazettement)
//    {
//        val variable:MutableMap<String, Any> = HashMap()
//        iSGazettement.iSNumber?.let{variable.put("iSNumber", it)}
//        iSGazettement.dateOfGazettement = Timestamp(System.currentTimeMillis())
//        variable["dateOfGazettement"] = iSGazettement.dateOfGazettement!!
//        iSGazettement.description?.let{variable.put("description", it)}
//        iSGazettement.taskId?.let{variable.put("taskId", it)}
//        iSGazettement.processId?.let{variable.put("processId", it)}
//
//        print(iSGazettement.toString())
//
//        iSGazettementRepository.save(iSGazettement)
//        taskService.complete(iSGazettement.taskId, variable)
//        println("IS Gazzettement date has been updated")
//
//    }

    fun getISDNumber(): Pair<String, Long>
    {
        var allRequests =standardRepository.getMaxISDN()
        allRequests = if (allRequests.equals(null)){
            0
        }else{
            allRequests
        }

        var startId="ISN"
        allRequests = allRequests.plus(1)
        val year = Calendar.getInstance()[Calendar.YEAR]
        return Pair("$startId/$allRequests:$year", allRequests)
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

    fun getDRNumber(): String {
        var allRequests = comStdDraftRepository.getMaxDraftId()

        var lastId: String? = "0"
//        var finalValue = 1
        var startId = "DRAFT"

        //allRequests = allRequests+1

        val c = allRequests
        val d = c.toInt()
        val x = 1
        val z = x  + d

        val finalValue = z.toString()

//        println("Sum of x+y = $finalValue")

        val year = Calendar.getInstance()[Calendar.YEAR]
        val month = Calendar.getInstance().get(Calendar.MONTH)+1

        return "$startId/$finalValue/$month:$year"


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

private fun <E> MutableList<E>.addAll(elements: E) {

}
