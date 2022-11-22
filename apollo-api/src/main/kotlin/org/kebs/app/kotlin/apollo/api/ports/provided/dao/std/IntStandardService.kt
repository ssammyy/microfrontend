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
    fun prepareAdoptionProposal(iSAdoptionProposal: ISAdoptionProposal) : ISAdoptionProposal
    {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val variables: MutableMap<String, Any> = HashMap()
        iSAdoptionProposal.proposal_doc_name=iSAdoptionProposal.proposal_doc_name
        iSAdoptionProposal.circulationDate=iSAdoptionProposal.circulationDate
        iSAdoptionProposal.closingDate=iSAdoptionProposal.circulationDate
        iSAdoptionProposal.tcSecName=iSAdoptionProposal.proposal_doc_name
        iSAdoptionProposal.title=iSAdoptionProposal.title
        iSAdoptionProposal.scope=iSAdoptionProposal.scope
        iSAdoptionProposal.adoptionAcceptableAsPresented=iSAdoptionProposal.adoptionAcceptableAsPresented
        iSAdoptionProposal.reasonsForNotAcceptance=iSAdoptionProposal.reasonsForNotAcceptance
        iSAdoptionProposal.recommendations=iSAdoptionProposal.recommendations
        iSAdoptionProposal.nameOfRespondent=iSAdoptionProposal.nameOfRespondent
        iSAdoptionProposal.positionOfRespondent=iSAdoptionProposal.positionOfRespondent
        iSAdoptionProposal.nameOfOrganization=iSAdoptionProposal.nameOfOrganization
        iSAdoptionProposal.dateOfApplication=iSAdoptionProposal.dateOfApplication
        iSAdoptionProposal.uploadedBy=iSAdoptionProposal.uploadedBy
        iSAdoptionProposal.preparedDate = commonDaoServices.getTimestamp()
        iSAdoptionProposal.status = 0
        iSAdoptionProposal.proposalNumber = getPRNumber()

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

        return isAdoptionProposalRepository.save(iSAdoptionProposal)


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
        isAdoptionComments.user_id=isAdoptionComments.user_id
        isAdoptionComments.adoption_proposal_comment=isAdoptionComments.adoption_proposal_comment
        isAdoptionComments.commentTitle=isAdoptionComments.commentTitle
        isAdoptionComments.commentDocumentType=isAdoptionComments.commentDocumentType
        isAdoptionComments.comNameOfOrganization=isAdoptionComments.comNameOfOrganization
        isAdoptionComments.comClause=isAdoptionComments.comClause
        isAdoptionComments.comParagraph=isAdoptionComments.comParagraph
        isAdoptionComments.typeOfComment=isAdoptionComments.typeOfComment
        isAdoptionComments.proposedChange=isAdoptionComments.proposedChange
        isAdoptionComments.proposalID=isAdoptionComments.proposalID

        isAdoptionComments.comment_time = Timestamp(System.currentTimeMillis())
        isAdoptionCommentsRepository.save(isAdoptionComments)
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
        iSUploadStandard.iSNumber = getRQNumber()

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

    fun draughtStandardDraft(nwaWorkShopDraft: NWAWorkShopDraft) : ProcessInstanceWD
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
        nwaWorkshopDraftRepository.findByIdOrNull(nwaWorkShopDraft.id)?.let { nwaWorkShopDraft->

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

    fun proofReadStandardDraft(nwaWorkShopDraft: NWAWorkShopDraft) : ProcessInstanceWD
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
        nwaWorkshopDraftRepository.findByIdOrNull(nwaWorkShopDraft.id)?.let { nwaWorkShopDraft->

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