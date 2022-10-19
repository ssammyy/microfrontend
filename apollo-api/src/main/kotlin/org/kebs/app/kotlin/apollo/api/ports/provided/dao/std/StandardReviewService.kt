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
//    private fun getTaskDetails(tasks: List<Task>): List<TaskDetails> {
//        val taskDetails: MutableList<TaskDetails> = ArrayList()
//        for (task in tasks) {
//            val processVariables = taskService.getVariables(task.id)
//            taskDetails.add(TaskDetails(task.id, task.name, processVariables))
//
//        }
//        return taskDetails
//    }



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
//    fun getRecommendations():List<TaskDetails>
//    {
//        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_SPC_SEC).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
//        return getTaskDetails(tasks)
//    }



    //Return task details for SAC_SEC
//    fun getSacList():List<TaskDetails>
//    {
//        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_SAC_SEC).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
//        return getTaskDetails(tasks)
//    }

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
        var userList= companyStandardRepository.getStakeHoldersEmailList()

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

        //taskService.complete(nwaJustification.taskId, variables)
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables)
        taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
            ?.let { t ->
                t.list()[0]
                    ?.let { task ->
                        task.assignee =
                            "${standardReview.assignedTo ?: throw NullValueNotAllowedException(" invalid user id provided")}"  //set the assignee}"

                        taskService.saveTask(task)
                    }
                    ?: KotlinLogging.logger { }.error("Task list empty for $PROCESS_DEFINITION_KEY ")


            }
            ?: KotlinLogging.logger { }.error("No task found for $PROCESS_DEFINITION_KEY ")
        bpmnService.slAssignTask(
            processInstance.processInstanceId,
            "makeRecommendations",
            standardReview?.assignedTo
                ?: throw NullValueNotAllowedException("invalid user id provided")
        )

        return ProcessInstanceRecommendations(ispDetails.id, processInstance.id, processInstance.isEnded)

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
        standardReviewProposalComments.userName?.let{ variables.put("userName", it)}
        standardReviewProposalComments.adoptionComment?.let{ variables.put("adoptionComment", it)}
        standardReviewProposalComments.proposalId?.let{ variables.put("proposalId", it)}
        standardReviewProposalComments.title?.let{ variables.put("title", it)}
        standardReviewProposalComments.documentType?.let{ variables.put("documentType", it)}
        standardReviewProposalComments.clause?.let{ variables.put("clause", it)}
        standardReviewProposalComments.paragraph?.let{ variables.put("paragraph", it)}
        standardReviewProposalComments.typeOfComment?.let{ variables.put("typeOfComment", it)}
        standardReviewProposalComments.proposedChange?.let{ variables.put("proposedChange", it)}

        standardReviewProposalComments.commentTime = Timestamp(System.currentTimeMillis())
        variables["commentTime"] = standardReviewProposalComments.commentTime!!

        standardReviewProposalCommentsRepository.save(standardReviewProposalComments)

    }

    fun getStandardsProposalComments(proposalId: Long): MutableIterable<ReviewStandards>? {
        return standardReviewProposalCommentsRepository.getStandardsProposalComments(proposalId)
    }

    //Make Recommendations on Adoption Proposal
    fun makeRecommendationsOnAdoptionProposal(standardReviewProposalRecommendations: StandardReviewProposalRecommendations) : ProcessInstanceRecommendations
    {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val variables: MutableMap<String, Any> = HashMap()
        standardReviewProposalRecommendations.proposalId?.let{ variables.put("proposalId", it)}
        standardReviewProposalRecommendations.summaryOfRecommendations?.let{ variables.put("summaryOfRecommendations", it)}
        standardReviewProposalRecommendations.userID?.let{ variables.put("userID", it)}
        standardReviewProposalRecommendations.processId?.let{ variables.put("processId", it)}
        standardReviewProposalRecommendations.taskId?.let{ variables.put("taskId", it)}
        standardReviewProposalRecommendations.recommendationTime = commonDaoServices.getTimestamp()
        variables["recommendationTime"] = standardReviewProposalRecommendations.recommendationTime!!


        val ispDetails = standardReviewProposalRecommendationsRepo.save(standardReviewProposalRecommendations)
        variables["RecommendationID"] = ispDetails.id

        //taskService.complete(nwaJustification.taskId, variables)
        runtimeService.createProcessInstanceQuery()
            .processInstanceId(standardReviewProposalRecommendations.processId).list()
            ?.let { l ->
                val processInstance = l[0]

                taskService.complete(standardReviewProposalRecommendations.taskId, variables)

                taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
                    ?.let { t ->
                        t.list()[0]
                            ?.let { task ->
                                task.assignee =
                                    "${standardReviewProposalRecommendations.assignedTo ?: throw NullValueNotAllowedException(" invalid user id provided")}"  //set the assignee}"
                                taskService.saveTask(task)
                            }
                            ?: KotlinLogging.logger { }.error("Task list empty for $PROCESS_DEFINITION_KEY ")


                    }
                    ?: KotlinLogging.logger { }.error("No task found for $PROCESS_DEFINITION_KEY ")
                bpmnService.slAssignTask(
                    processInstance.processInstanceId,
                    "viewRecommendations",
                    standardReviewProposalRecommendations.assignedTo ?: throw NullValueNotAllowedException("invalid user id provided")
                )
                return ProcessInstanceRecommendations(ispDetails.id, processInstance.id, processInstance.isEnded
                )
            }
            ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${standardReviewProposalRecommendations.processId} ")


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
                    if (userIntType == longProcess) {
                        bpmnService.slAssignTask(
                            processInstance.processInstanceId,
                            "submitDraftStandardForEditing",
                            iSDecision.assignedTo
                                ?: throw NullValueNotAllowedException("invalid user id provided")
                        )
                    }else {
                        bpmnService.slAssignTask(
                            processInstance.processInstanceId,
                            "recieveApprovedList",
                            iSDecision.assignedTo
                                ?: throw NullValueNotAllowedException("invalid user id provided")
                        )
                    }


                        ?: KotlinLogging.logger { }.error("No task found for $PROCESS_DEFINITION_KEY ")

                }
                ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${iSDecision.processId} ")



        }else if(variables["No"]==false) {


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
                        "makeRecommendations",
                        iSDecision.assignedTo
                            ?: throw NullValueNotAllowedException("invalid user id provided")
                    )

                }
                ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${iSDecision.processId} ")



        }

        return  getUserTasks()

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
                            "recieveGazzetteNotice",
                            iSDecision.assignedTo
                                ?: throw NullValueNotAllowedException("invalid user id provided")
                        )

                    }
                    ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${iSDecision.processId} ")



        }else if(variables["No"]==false) {


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
                        "makeRecommendations",
                        iSDecision.preparedBy
                            ?: throw NullValueNotAllowedException("invalid user id provided")
                    )

                }
                ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${iSDecision.processId} ")
        }

        return  getUserTasks()
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
        runtimeService.createProcessInstanceQuery()
            .processInstanceId(iSDecision.processId).list()
            ?.let { l ->
                val processInstance = l[0]

                taskService.complete(iSDecision.taskId, variables)

                taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
                    ?.let { t ->
                        t.list()[0]
                            ?.let { task ->
                                task.assignee =
                                    "${iSDecision.assignedTo ?: throw NullValueNotAllowedException(" invalid user id provided")}"  //set the assignee}"
                                taskService.saveTask(task)
                            }
                            ?: KotlinLogging.logger { }.error("Task list empty for $PROCESS_DEFINITION_KEY ")


                    }
                    ?: KotlinLogging.logger { }.error("No task found for $PROCESS_DEFINITION_KEY ")
                bpmnService.slAssignTask(
                    processInstance.processInstanceId,
                    "uploadNoticeOnWebsite",
                    iSDecision.assignedTo ?: throw NullValueNotAllowedException("invalid user id provided")
                )
                return ProcessInstanceResponse(processInstance.id, processInstance.isEnded
                )
            }
            ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${iSDecision.processId} ")
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
        val variables: MutableMap<String, Any> = HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
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
        variables["draftId"] = ispDetails.id
        variables["drafterId"] = loggedInUser.id!!

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
                    "recieveNotificationOfDraft",
                    iSAdoptionJustification.assignedTo ?: throw NullValueNotAllowedException("invalid user id provided")
                )
                return ProcessInstanceResponseValue(ispDetails.id, processInstance.id, processInstance.isEnded,
                    iSAdoptionJustification.requestNumber!!
                )
            }
            ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${iSAdoptionJustification.processId} ")


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
                        "editStandard",
                        iSDecision.assignedTo
                            ?: throw NullValueNotAllowedException("invalid user id provided")
                    )

                }
                ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${iSDecision.processId} ")



        }else if(variables["No"]==false) {
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
                                        iSDecision.drafterId ?: throw NullValueNotAllowedException(
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
                        "submitDraftStandardForEditing",
                        iSDecision.drafterId
                            ?: throw NullValueNotAllowedException("invalid user id provided")
                    )

                }
                ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${iSDecision.processId} ")
        }

        return  getUserTasks()

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
            runtimeService.createProcessInstanceQuery()
                .processInstanceId(iSAdoptionJustification.processId).list()
                ?.let { l ->
                    val processInstance = l[0]

                    taskService.complete(iSAdoptionJustification.taskId, variable)

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
                        "draughting",
                        iSAdoptionJustification.assignedTo ?: throw NullValueNotAllowedException("invalid user id provided")
                    )
                    return ProcessInstanceDraft(
                        iSAdoptionJustification.id,
                        processInstance.id,
                        processInstance.isEnded
                    )
                }
                ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${iSAdoptionJustification.processId} ")
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
            runtimeService.createProcessInstanceQuery()
                .processInstanceId(iSAdoptionJustification.processId).list()
                ?.let { l ->
                    val processInstance = l[0]

                    taskService.complete(iSAdoptionJustification.taskId, variable)

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
                        "proofread",
                        iSAdoptionJustification.assignedTo ?: throw NullValueNotAllowedException("invalid user id provided")
                    )
                    return ProcessInstanceDraft(
                        iSAdoptionJustification.id,
                        processInstance.id,
                        processInstance.isEnded
                    )
                }
                ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${iSAdoptionJustification.processId} ")
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
            runtimeService.createProcessInstanceQuery()
                .processInstanceId(iSAdoptionJustification.processId).list()
                ?.let { l ->
                    val processInstance = l[0]

                    taskService.complete(iSAdoptionJustification.taskId, variable)

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
                        "checkDraftStandard",
                        iSAdoptionJustification.assignedTo ?: throw NullValueNotAllowedException("invalid user id provided")
                    )
                    return ProcessInstanceDraft(
                        iSAdoptionJustification.id,
                        processInstance.id,
                        processInstance.isEnded
                    )
                }
                ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${iSAdoptionJustification.processId} ")
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
                        "recieveApprovedList",
                        iSDecision.assignedTo
                            ?: throw NullValueNotAllowedException("invalid user id provided")
                    )

                }
                ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${iSDecision.processId} ")



        }else if(variables["No"]==false) {
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
                                        iSDecision.drafterId ?: throw NullValueNotAllowedException(
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
                        "submitDraftStandardForEditing",
                        iSDecision.drafterId
                            ?: throw NullValueNotAllowedException("invalid user id provided")
                    )

                }
                ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${iSDecision.processId} ")
        }

        return  getUserTasks()

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
