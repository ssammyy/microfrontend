package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std


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
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.StandardsLevyBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.util.*
import kotlin.collections.HashMap


@Service
class StandardReviewService(
    private val runtimeService: RuntimeService,
    private val taskService: TaskService,
    @Qualifier("processEngine") private val processEngine: ProcessEngine,
    private val repositoryService: RepositoryService,
    @Autowired
    private val standardRepository: StandardRepository,
    private val standardReviewRepository: StandardReviewRepository,
    private val standardReviewCommentsRepository: StandardReviewCommentsRepository,
    private val standardReviewRecommendationsRepository: StandardReviewRecommendationsRepository,
    private val companyStandardRepository: CompanyStandardRepository,
    private val notifications: Notifications,
    private val standardReviewProposalCommentsRepository: StandardReviewProposalCommentsRepository,
    private val standardReviewProposalRecommendationsRepo: StandardReviewProposalRecommendationsRepo,
    private val commonDaoServices: CommonDaoServices,
    private val bpmnService: StandardsLevyBpmn,
    private val internationalStandardRemarksRepository : InternationalStandardRemarksRepository,
    private val iSAdoptionJustificationRepository: ISAdoptionJustificationRepository,
    private val departmentListRepository: DepartmentListRepository,
) {

    val PROCESS_DEFINITION_KEY = "sd_Review_Procedure_module"
    val TASK_CANDIDATE_GROUP_TC_SEC ="TC_SEC"
    val TASK_CANDIDATE_GROUP_STAKEHOLDERS ="STAKEHOLDERS"
    val TASK_CANDIDATE_GROUP_SPC_SEC ="SPC_SEC"
    val TASK_CANDIDATE_GROUP_SAC_SEC="SAC_SEC"
    val TASK_CANDIDATE_GROUP_HOP="HOP"
    val TASK_CANDIDATE_GROUP_EDITOR="EDITOR"
    val TASK_CANDIDATE_GROUP_PROOFREADER="PROOFREADER"
    val TASK_CANDIDATE_GROUP_DRAUGHTSMAN="DRAUGHTSMAN"
    val TASK_CANDIDATE_GROUP_HO_SIC="HO_SIC"



    fun deployProcessDefinition(): Deployment =repositoryService
        .createDeployment()
        .addClasspathResource("processes/std/Review_Procedure_Process_Flow.bpmn20.xml")
        .deploy()

    //start the process by process Key
    fun startProcessByKey() : ProcessInstanceResponse
    {

        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY)
        return ProcessInstanceResponse(processInstance.id, processInstance.isEnded)
    }

    //*** Not used *** but closes any Task, linked to task close endpoint
    fun closeTask(taskId: String) {
        taskService.complete(taskId)
    }

    //Function to retrieve task details for any candidate group
    private fun getTaskDetails(tasks: List<Task>): List<StandardReviewTasks> {
        val taskDetails: MutableList<StandardReviewTasks> = ArrayList()
        for (task in tasks) {
            val processVariables = taskService.getVariables(task.id)
            taskDetails.add(StandardReviewTasks(task.id, task.name,task.processInstanceId, processVariables))


        }
        return taskDetails
    }



    fun reviewedStandards(): MutableList<Standard>
    {
        return standardRepository.findAll()
    }

    fun saveStandard(standard: Standard){
        standardRepository.save(standard)

    }





    //Return task details for Stakeholders
//    fun getReviewForms():List<TaskDetails>
//    {
//        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_STAKEHOLDERS).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
//        return getTaskDetails(tasks)
//    }

    fun commentsOnReview(standardReviewComments: StandardReviewComments)
    {
        val variables: MutableMap<String, Any> = HashMap()
        standardReviewComments.comments?.let{ variables.put("comments", it)}
        standardReviewComments.commentBy?.let{ variables.put("commentBy", it)}
        standardReviewComments.dateOfComment?.let{ variables.put("dateOfComment", it)}

        print(standardReviewComments.toString())

        standardReviewCommentsRepository.save(standardReviewComments)
        taskService.complete(standardReviewComments.taskId, variables)
        println("Comments Submitted")
    }

    //Return task details for TSC_SEC
//    fun getReviewTasks():List<TaskDetails>
//    {
//        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_TC_SEC).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
//        return getTaskDetails(tasks)
//    }

    fun reviewRecommendations(standardReviewRecommendations: StandardReviewRecommendations)
    {
        val variables: MutableMap<String, Any> = HashMap()
        standardReviewRecommendations.recommendation?.let{ variables.put("recommendation", it)}
        standardReviewRecommendations.recommendationBy?.let{ variables.put("recommendationBy", it)}
        standardReviewRecommendations.dateOfRecommendation?.let{ variables.put("dateOfRecommendation", it)}
        standardReviewRecommendations.accentTo?.let{ variables.put("accentTo", it)}

        print(standardReviewRecommendations.toString())

        standardReviewRecommendationsRepository.save(standardReviewRecommendations)
        taskService.complete(standardReviewRecommendations.taskId, variables)
        println("Recommendations Submitted")
    }

    //Return task details for SPC_SEC
    fun getSpcSecTasks():List<StandardReviewTasks>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_SPC_SEC).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    fun getHopTasks():List<StandardReviewTasks>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_HOP).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }



    //Return task details for SAC_SEC
    fun getSacSecTasks():List<StandardReviewTasks>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_SAC_SEC).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    fun getHoSicTasks():List<StandardReviewTasks>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_HO_SIC).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    fun getEditorTasks():List<StandardReviewTasks>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_EDITOR).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    fun getProofReaderTasks():List<StandardReviewTasks>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_PROOFREADER).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    fun getDraughtsManTasks():List<StandardReviewTasks>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_DRAUGHTSMAN).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    fun getTcSecTasks():List<StandardReviewTasks>
    {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_TC_SEC).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    // Decision on Recommendation
    fun decisionOfSac(standardReviewRecommendations: StandardReviewRecommendations)
    {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        variables["Yes"] = standardReviewRecommendations.accentTo
        taskService.complete(standardReviewRecommendations.taskId, variables)
    }

    //Return task details for HOP
//    fun getPublishingTasks():List<TaskDetails>
//    {
//        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_HOP).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
//        return getTaskDetails(tasks)
//    }

    fun getStandardsForReview(): MutableList<ReviewStandards> {
        return standardRepository.getStandardsForReview()
    }

    fun standardReviewForm(standardReview: StandardReview) : ProcessInstanceRecommendations
    {
        val variables: MutableMap<String, Any> = HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        standardReview.title?.let{ variables.put("title", it)}
        standardReview.standardNumber?.let{ variables.put("standardNumber", it)}
        standardReview.documentType?.let{ variables.put("documentType", it)}
        standardReview.preparedBy?.let{ variables.put("preparedBy", it)}
        standardReview.preparedBy = loggedInUser.id
        standardReview.datePrepared?.let{ variables.put("datePrepared", it)}
        standardReview.scope?.let{ variables.put("scope", it)}
        standardReview.normativeReference?.let{ variables.put("normativeReference", it)}
        standardReview.symbolsAbbreviatedTerms?.let{ variables.put("symbolsAbbreviatedTerms", it)}
        standardReview.clause?.let{ variables.put("clause", it)}
        standardReview.special?.let{ variables.put("special", it)}
        standardReview.standardType?.let{ variables.put("standardType", it)}
        variables["preparedBy"] = standardReview.preparedBy!!

        val ispDetails = standardReviewRepository.save(standardReview)
        variables["reviewID"] = ispDetails.id
        var userList= companyStandardRepository.getTcSecEmailList()

        //email to stakeholders
        val targetUrl = "https://kimsint.kebs.org/";
        userList.forEach { item->
            //val recipient="stephenmuganda@gmail.com"
            val recipient= item.getUserEmail()
            val subject = "New Adoption Proposal Review"+  standardReview.standardNumber
            val messageBody= "Dear ${item.getFirstName()} ${item.getLastName()},An adoption document has been uploaded Kindly login to the system to comment on it.Click on the Link below to view. ${targetUrl} "
            if (recipient != null) {
                notifications.sendEmail(recipient, subject, messageBody)
            }
        }

        //taskService.complete(standardReview.taskId, variables)
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables)
        return ProcessInstanceRecommendations(ispDetails.id, processInstance.id, processInstance.isEnded)

    }





    fun getUserTasks(): List<StandardReviewTasks> {
        val tasks = taskService.createTaskQuery()
            .taskAssignee("${commonDaoServices.loggedInUserDetails().id ?: throw NullValueNotAllowedException(" invalid user id provided")}")
            .list()
        return getTaskDetails(tasks)
    }


    fun getStandardsProposalForComment(): MutableList<ReviewStandards> {
        return standardReviewRepository.getStandardsProposalForComment()
    }

    //Submit Adoption Proposal comments
    fun submitAPComments(standardReviewProposalComments: StandardReviewProposalComments){
        val variables: MutableMap<String, Any> = HashMap()
        standardReviewProposalComments.userName=standardReviewProposalComments.userName
        standardReviewProposalComments.adoptionComment=standardReviewProposalComments.adoptionComment
        standardReviewProposalComments.proposalId=standardReviewProposalComments.proposalId
        standardReviewProposalComments.title=standardReviewProposalComments.title
        standardReviewProposalComments.documentType=standardReviewProposalComments.documentType
        standardReviewProposalComments.clause=standardReviewProposalComments.clause
        standardReviewProposalComments.paragraph=standardReviewProposalComments.paragraph
        standardReviewProposalComments.typeOfComment=standardReviewProposalComments.typeOfComment
        standardReviewProposalComments.proposedChange=standardReviewProposalComments.proposedChange

        standardReviewProposalComments.commentTime = Timestamp(System.currentTimeMillis())

        standardReviewProposalCommentsRepository.save(standardReviewProposalComments)

    }

    fun getStandardsProposalComments(proposalId: Long): MutableIterable<ReviewStandards>? {
        return standardReviewProposalCommentsRepository.getStandardsProposalComments(proposalId)
    }

    //Make Recommendations on Adoption Proposal
    fun makeRecommendationsOnAdoptionProposal(standardReviewProposalRecommendations: StandardReviewProposalRecommendations) : ProcessInstanceRecommendation
    {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val variables: MutableMap<String, Any> = HashMap()
        standardReviewProposalRecommendations.proposalId=standardReviewProposalRecommendations.proposalId
        standardReviewProposalRecommendations.summaryOfRecommendations?.let{ variables.put("summaryOfRecommendations", it)}
        standardReviewProposalRecommendations.userID=loggedInUser.id
        variables["userID"]=standardReviewProposalRecommendations.userID!!
        standardReviewProposalRecommendations.processId?.let{ variables.put("processId", it)}
        standardReviewProposalRecommendations.feedback?.let{ variables.put("feedback", it)}
        standardReviewProposalRecommendations.taskId?.let{ variables.put("taskId", it)}
        standardReviewProposalRecommendations.recommendationTime = commonDaoServices.getTimestamp()
        variables["recommendationTime"] = standardReviewProposalRecommendations.recommendationTime!!

        val ispDetails = standardReviewProposalRecommendationsRepo.save(standardReviewProposalRecommendations)
        variables["RecommendationID"] = ispDetails.id

        taskService.complete(standardReviewProposalRecommendations.taskId, variables)
        //val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables)
        return ProcessInstanceRecommendation(ispDetails.id )
    }

    // Decision on Recommendations
    fun decisionOnRecommendation(iSDecision: ISDecision,
                           internationalStandardRemarks: InternationalStandardRemarks
    ) : List<StandardReviewTasks> {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        variables["Yes"] = iSDecision.accentTo
        variables["No"] = iSDecision.accentTo
        iSDecision.comments.let { variables.put("comments", it) }
        iSDecision.taskId.let { variables.put("taskId", it) }
        iSDecision.processId.let { variables.put("processId", it) }
        iSDecision.assignedTo.let { variables.put("assignedTo", it) }
        val fname=loggedInUser.firstName
        val sname=loggedInUser.lastName
        val usersName= "$fname  $sname"
        internationalStandardRemarks.proposalId= iSDecision.reviewID
        internationalStandardRemarks.remarks= iSDecision.comments
        internationalStandardRemarks.status = 1.toString()
        internationalStandardRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
        internationalStandardRemarks.remarkBy = usersName
        val userIntType = iSDecision.taskType
        val longProcess = 1L
        val shortProcess = 0L

        if(variables["Yes"]==true){

            internationalStandardRemarksRepository.save(internationalStandardRemarks)
                    taskService.complete(iSDecision.taskId, variables)

                    if (userIntType == longProcess) {
                        taskService.complete(iSDecision.taskId, variables)
                    }else {
                        taskService.complete(iSDecision.taskId, variables)
                    }

        }else if(variables["No"]==false) {


            internationalStandardRemarksRepository.save(internationalStandardRemarks)
            taskService.complete(iSDecision.taskId, variables)

        }

        return  getSpcSecTasks()

    }

    // KNW SEC Decision on Justification
    fun levelUpDecisionOnRecommendations(
        iSDecision: ISDecision,
        internationalStandardRemarks: InternationalStandardRemarks,
        standard:Standard
    ) : List<StandardReviewTasks> {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        variables["Yes"] = iSDecision.accentTo
        variables["No"] = iSDecision.accentTo
        loggedInUser.id?.let { variables["originator"] = it }
        iSDecision.comments.let { variables.put("comments", it) }
        iSDecision.taskId?.let { variables.put("taskId", it) }
        iSDecision.processId?.let { variables.put("processId", it) }
        iSDecision.assignedTo= companyStandardRepository.getSpcSecId()

        //Save Standard
        standard.title?.let{ variables.put("title", it)}
        standard.scope?.let{ variables.put("scope", it)}
        standard.normativeReference?.let{ variables.put("normativeReference", it)}
        standard.symbolsAbbreviatedTerms?.let{ variables.put("symbolsAbbreviatedTerms", it)}
        standard.clause?.let{ variables.put("clause", it)}
        standard.special?.let{ variables.put("special", it)}
        standard.standardType?.let{ variables.put("standardType", it)}
        standard.status=1
        standard.dateFormed = Timestamp(System.currentTimeMillis())
        variables["dateFormed"] = standard.dateFormed!!
        standard.standardNumber = getKSNumber()
        variables["standardNumber"] = standard.standardNumber!!

        val fname=loggedInUser.firstName
        val sname=loggedInUser.lastName
        val usersName= "$fname  $sname"
        internationalStandardRemarks.proposalId= iSDecision.reviewID
        internationalStandardRemarks.remarks= iSDecision.comments
        internationalStandardRemarks.status = 1.toString()
        internationalStandardRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
        internationalStandardRemarks.remarkBy = usersName
        if(variables["Yes"]==true){

            internationalStandardRemarksRepository.save(internationalStandardRemarks)
            val standardDetails = standardRepository.save(standard)
            variables["standardID"] = standardDetails.id

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


        }else if(variables["No"]==false) {


            internationalStandardRemarksRepository.save(internationalStandardRemarks)
            taskService.complete(iSDecision.taskId, variables)
        }

        return  getSacSecTasks()
    }

    fun updateGazette(internationalStandardRemarks: InternationalStandardRemarks,
                      standard:Standard,iSDecision: ISDecision): ProcessInstanceResponse
    {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val variables: MutableMap<String, Any> = HashMap()

        standardRepository.findByIdOrNull(iSDecision.standardID)?.let { standard->

            with(standard){
                standard.isGazetted=1
            }
            standardRepository.save(standard)
            taskService.complete(iSDecision.taskId, variables)
            val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables)
            return ProcessInstanceResponse(processInstance.id, processInstance.isEnded
            )

        }?: throw Exception("Standard ID Not Found")

    }

    // Upload NWA Gazette date
    fun updateGazettementDate(nWAGazettement: NWAGazettement,iSDecision: ISDecision )
    {
        val variable:MutableMap<String, Any> = java.util.HashMap()
        standardRepository.findByIdOrNull(iSDecision.standardID)?.let { standard->

            with(standard){
                standard.dateOfGazettement=Timestamp(System.currentTimeMillis())
                standard.description=iSDecision.description
            }
            standardRepository.save(standard)
        taskService.complete(iSDecision.taskId, variable)
        }?: throw Exception("Standard ID Not Found")

        println("NWA Gazettement date has been updated")

    }

    //Type 1
    //prepare justification
    fun submitDraftForEditing(iSAdoptionJustification: ISAdoptionJustification) : ProcessInstanceResponseValue
    {
        val variable: MutableMap<String, Any> = HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        iSAdoptionJustification.title?.let{variable.put("title", it)}
        iSAdoptionJustification.scope?.let{variable.put("scope", it)}
        iSAdoptionJustification.normativeReference?.let{variable.put("normativeReference", it)}
        iSAdoptionJustification.symbolsAbbreviatedTerms?.let{variable.put("symbolsAbbreviatedTerms", it)}
        iSAdoptionJustification.clause?.let{variable.put("clause", it)}
        iSAdoptionJustification.special?.let{variable.put("special", it)}
        iSAdoptionJustification.taskId?.let{variable.put("taskId", it)}
        iSAdoptionJustification.processId?.let{variable.put("processId", it)}
        iSAdoptionJustification.assignedTo= companyStandardRepository.getSpcSecId()

        iSAdoptionJustification.submissionDate = Timestamp(System.currentTimeMillis())
        variable["submissionDate"] = iSAdoptionJustification.submissionDate!!

        iSAdoptionJustification.requestNumber = getRQNumber()

        variable["requestNumber"] = iSAdoptionJustification.requestNumber!!

        val ispDetails = iSAdoptionJustificationRepository.save(iSAdoptionJustification)
        variable["draftId"] = ispDetails.id
        variable["drafterId"] = loggedInUser.id!!

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
        taskService.complete(iSAdoptionJustification.taskId, variable)
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variable)
        return ProcessInstanceResponseValue(ispDetails.id, processInstance.id, processInstance.isEnded,
            iSAdoptionJustification.requestNumber!!
        )

    }


    // Decision on Requirements
    fun checkRequirements(iSDecision: ISDecision,
                        internationalStandardRemarks: InternationalStandardRemarks
    ) : List<StandardReviewTasks> {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        variables["Yes"] = iSDecision.accentTo
        variables["No"] = iSDecision.accentTo
        iSDecision.comments.let { variables.put("comments", it) }
        iSDecision.taskId.let { variables.put("taskId", it) }
        iSDecision.processId.let { variables.put("processId", it) }
        iSDecision.assignedTo.let { variables.put("assignedTo", it) }
        val fname=loggedInUser.firstName
        val sname=loggedInUser.lastName
        val usersName= "$fname  $sname"
        internationalStandardRemarks.proposalId= iSDecision.reviewID
        internationalStandardRemarks.remarks= iSDecision.comments
        internationalStandardRemarks.status = 1.toString()
        internationalStandardRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
        internationalStandardRemarks.remarkBy = usersName


        if(variables["Yes"]==true){

            internationalStandardRemarksRepository.save(internationalStandardRemarks)

        }else if(variables["No"]==false) {
            internationalStandardRemarksRepository.save(internationalStandardRemarks)

        }
        taskService.complete(iSDecision.taskId, variables)
        return  getHopTasks()

    }

    fun editStandardDraft(iSAdoptionJustification: ISAdoptionJustification,
                             iSDraftStdUpload:ISDraftStdUpload) : ProcessInstanceDraft
    {
        val variable:MutableMap<String, Any> = java.util.HashMap()
        iSAdoptionJustification.title?.let{variable.put("title", it)}
        iSAdoptionJustification.scope?.let{variable.put("scope", it)}
        iSAdoptionJustification.normativeReference?.let{variable.put("normativeReference", it)}
        iSAdoptionJustification.symbolsAbbreviatedTerms?.let{variable.put("symbolsAbbreviatedTerms", it)}
        iSAdoptionJustification.clause?.let{variable.put("clause", it)}
        iSAdoptionJustification.special?.let{variable.put("special", it)}
        iSAdoptionJustification.taskId?.let{variable.put("taskId", it)}
        iSAdoptionJustification.processId?.let{variable.put("processId", it)}
        iSAdoptionJustification.assignedTo= companyStandardRepository.getProofReaderId()

        //print(nwaWorkShopDraft.toString())
        iSAdoptionJustificationRepository.findByIdOrNull(iSDraftStdUpload.draftId)?.let { iSAdoptionJustification->

            with(iSAdoptionJustification){
                title=iSAdoptionJustification.title
                scope=iSAdoptionJustification.scope
                normativeReference=iSAdoptionJustification.normativeReference
                symbolsAbbreviatedTerms=iSAdoptionJustification.symbolsAbbreviatedTerms
                clause=iSAdoptionJustification.clause
                special=iSAdoptionJustification.special
                assignedTo=iSAdoptionJustification.assignedTo
            }

            iSAdoptionJustificationRepository.save(iSAdoptionJustification)
            taskService.complete(iSAdoptionJustification.taskId, variable)
            val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variable)
            return ProcessInstanceDraft(
                iSAdoptionJustification.id,
                processInstance.id,
                processInstance.isEnded
            )

        }?: throw Exception("RECORD NOT FOUND")


    }

    fun draftStandard(iSAdoptionJustification: ISAdoptionJustification,
                          iSDraftStdUpload:ISDraftStdUpload) : ProcessInstanceDraft
    {
        val variable:MutableMap<String, Any> = java.util.HashMap()
        iSAdoptionJustification.title?.let{variable.put("title", it)}
        iSAdoptionJustification.scope?.let{variable.put("scope", it)}
        iSAdoptionJustification.normativeReference?.let{variable.put("normativeReference", it)}
        iSAdoptionJustification.symbolsAbbreviatedTerms?.let{variable.put("symbolsAbbreviatedTerms", it)}
        iSAdoptionJustification.clause?.let{variable.put("clause", it)}
        iSAdoptionJustification.special?.let{variable.put("special", it)}
        iSAdoptionJustification.taskId?.let{variable.put("taskId", it)}
        iSAdoptionJustification.processId?.let{variable.put("processId", it)}
        iSAdoptionJustification.assignedTo= companyStandardRepository.getProofReaderId()

        //print(nwaWorkShopDraft.toString())
        iSAdoptionJustificationRepository.findByIdOrNull(iSDraftStdUpload.draftId)?.let { iSAdoptionJustification->

            with(iSAdoptionJustification){
                title=iSAdoptionJustification.title
                scope=iSAdoptionJustification.scope
                normativeReference=iSAdoptionJustification.normativeReference
                symbolsAbbreviatedTerms=iSAdoptionJustification.symbolsAbbreviatedTerms
                clause=iSAdoptionJustification.clause
                special=iSAdoptionJustification.special
                assignedTo=iSAdoptionJustification.assignedTo
            }

            iSAdoptionJustificationRepository.save(iSAdoptionJustification)
            taskService.complete(iSAdoptionJustification.taskId, variable)
            val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variable)
            return ProcessInstanceDraft(
                iSAdoptionJustification.id,
                processInstance.id,
                processInstance.isEnded
            )

        }?: throw Exception("RECORD NOT FOUND")


    }


    fun proofReadStandard(iSAdoptionJustification: ISAdoptionJustification,
                      iSDraftStdUpload:ISDraftStdUpload) : ProcessInstanceDraft
    {
        val variable:MutableMap<String, Any> = java.util.HashMap()
        iSAdoptionJustification.title?.let{variable.put("title", it)}
        iSAdoptionJustification.scope?.let{variable.put("scope", it)}
        iSAdoptionJustification.normativeReference?.let{variable.put("normativeReference", it)}
        iSAdoptionJustification.symbolsAbbreviatedTerms?.let{variable.put("symbolsAbbreviatedTerms", it)}
        iSAdoptionJustification.clause?.let{variable.put("clause", it)}
        iSAdoptionJustification.special?.let{variable.put("special", it)}
        iSAdoptionJustification.taskId?.let{variable.put("taskId", it)}
        iSAdoptionJustification.processId?.let{variable.put("processId", it)}
        iSAdoptionJustification.assignedTo= companyStandardRepository.getProofReaderId()

        //print(nwaWorkShopDraft.toString())
        iSAdoptionJustificationRepository.findByIdOrNull(iSDraftStdUpload.draftId)?.let { iSAdoptionJustification->

            with(iSAdoptionJustification){
                title=iSAdoptionJustification.title
                scope=iSAdoptionJustification.scope
                normativeReference=iSAdoptionJustification.normativeReference
                symbolsAbbreviatedTerms=iSAdoptionJustification.symbolsAbbreviatedTerms
                clause=iSAdoptionJustification.clause
                special=iSAdoptionJustification.special
                assignedTo=iSAdoptionJustification.assignedTo
            }

            iSAdoptionJustificationRepository.save(iSAdoptionJustification)
            taskService.complete(iSAdoptionJustification.taskId, variable)
            val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variable)
            return ProcessInstanceDraft(
                iSAdoptionJustification.id,
                processInstance.id,
                processInstance.isEnded
            )

        }?: throw Exception("RECORD NOT FOUND")


    }

    // Decision on Requirements
    fun checkStandardDraft(iSDecision: ISDecision,
                          internationalStandardRemarks: InternationalStandardRemarks
    ) : List<StandardReviewTasks> {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        variables["Yes"] = iSDecision.accentTo
        variables["No"] = iSDecision.accentTo
        iSDecision.comments.let { variables.put("comments", it) }
        iSDecision.taskId.let { variables.put("taskId", it) }
        iSDecision.processId.let { variables.put("processId", it) }
        iSDecision.assignedTo.let { variables.put("assignedTo", it) }
        val fname=loggedInUser.firstName
        val sname=loggedInUser.lastName
        val usersName= "$fname  $sname"
        internationalStandardRemarks.proposalId= iSDecision.reviewID
        internationalStandardRemarks.remarks= iSDecision.comments
        internationalStandardRemarks.status = 1.toString()
        internationalStandardRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
        internationalStandardRemarks.remarkBy = usersName


        if(variables["Yes"]==true){

            internationalStandardRemarksRepository.save(internationalStandardRemarks)

        }else if(variables["No"]==false) {
            internationalStandardRemarksRepository.save(internationalStandardRemarks)

        }

        return  getHopTasks()

    }




    fun getKSNumber(): String
    {
        val allRequests =standardRepository.findAllByOrderByIdDesc()

        var lastId:String?="0"
        var finalValue =1
        var startId="KS"


        for(item in allRequests){
            println(item)
            lastId = item.standardNumber
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







}
