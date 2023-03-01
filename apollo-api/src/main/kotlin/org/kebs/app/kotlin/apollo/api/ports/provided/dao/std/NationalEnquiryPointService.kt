package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std


import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.engine.repository.Deployment
import org.flowable.task.api.Task
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.dto.std.*
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.std.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.sql.Timestamp
import java.time.LocalDate


@Service
class NationalEnquiryPointService(
    private val runtimeService: RuntimeService,
    private val taskService: TaskService,
    private val processEngine: ProcessEngine,
    private val repositoryService: RepositoryService,
    private val nationalEnquiryPointRepository: NationalEnquiryPointEntityRepository,
    private val informationTrackerRepository: InformationTrackerRepository,
    private val departmentResponseRepository: DepartmentResponseRepository,
    private val notifications: Notifications,
    private val commonDaoServices: CommonDaoServices,
    private val nepDocUploads: SdNepDocumentUploadsEntityRepository,
    private val nepRemarksRepository: NepRemarksRepository,
    private val nationalEnquiryEntityRepository: NationalEnquiryEntityRepository,
    private val sdNepDocUploads: SdNepDocUploadsEntityRepository,
    private val nepDraftRepo: SdNepDraftRepository,
    private val nepDraftDocRepo: SdNepDraftUploadsEntityRepository,
    private val nepWtoNotificationRepo: NEPWtoNotificationRepository

) {

    var PROCESS_DEFINITION_KEY: String = "nationalEnquiryPoint"
    val requesterId: String = "requesterId"
    val TASK_CANDIDATE_GROUP_NEP = "SD_NEP_OFFICER"
    val TASK_CANDIDATE_GROUP_DEPT = "DIVISION_ORG"

    //deployment of flowable function
    fun deployProcessDefinition(): Deployment = repositoryService
            .createDeployment()
            .addClasspathResource("processes/std/National_Enquiry_Point.bpmn20.xml")
            .deploy()


    //********************************************************** process service methods **********************************************************

    //make enquiry and process initiation function
    fun notificationRequest(nep: NepRequestsDto): NationalEnquiryPointEntity {

        val nationalEnquiryPoint=NationalEnquiryPointEntity();

        nationalEnquiryPoint.requesterName=nep.requesterName
        nationalEnquiryPoint.requesterComment=nep.requesterComment
        nationalEnquiryPoint.requesterCountry=nep.requesterCountry
        nationalEnquiryPoint.requesterEmail=nep.requesterEmail
        nationalEnquiryPoint.requesterInstitution=nep.requesterInstitution
        nationalEnquiryPoint.requesterPhone=nep.requesterPhone
        nationalEnquiryPoint.requesterSubject=nep.requesterSubject
        nationalEnquiryPoint.requestDate=nep.requestDate

        return nationalEnquiryPointRepository.save(nationalEnquiryPoint)
    }

    fun uploadNepDocument(
        uploads: SdNepDocumentUploadsEntity,
        docFile: MultipartFile,
        doc: String,
        user: String,
        DocDescription: String
    ): SdNepDocumentUploadsEntity {


        with(uploads) {

            name = commonDaoServices.saveDocuments(docFile)
            fileType = commonDaoServices.getFileTypeByMimetypesFileTypeMap(docFile.name)
            documentType = doc
            description = DocDescription
            document = docFile.bytes
            transactionDate = commonDaoServices.getCurrentDate()
            status = 1
            createdBy = user
            createdOn = commonDaoServices.getTimestamp()
        }

        val saved= nepDocUploads.save(uploads)
        nationalEnquiryPointRepository.findByIdOrNull(saved.nepDocumentId)?.let { sts ->

            with(sts) {
                docUploadStatus = 1

            }
        }?: throw Exception("ENQUIRY NOT FOUND")

        return saved

    }

    fun findUploadedReportFileBYId(enquiryId: Long): SdNepDocumentUploadsEntity {
        return nepDocUploads.findAllByNepDocumentId(enquiryId)
    }

    fun getNepRequests(): MutableList<NationalEnquiryPointEntity>
    {
        return nationalEnquiryPointRepository.getNepRequests()
    }

    fun decisionOnEnquiryInfo(
        nepInfoCheckDto: NepInfoCheckDto
    ) : NationalEnquiryEntity {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val decision = nepInfoCheckDto.accentTo
        val nep=NationalEnquiryPointEntity();
        val rem= NepRemarks()
        val nationalEnquiry=NationalEnquiryEntity();
        val informationTracker= InformationTracker()

        rem.requestId=nepInfoCheckDto.requestId
        rem.remarks=nepInfoCheckDto.comments
        rem.remarkBy=loggedInUser.firstName + loggedInUser.lastName
        rem.role=loggedInUser.typeOfUser
        rem.description="Remarks on Request recieved"
        rem.dateOfRemark= Timestamp(System.currentTimeMillis())
        if (decision == "Yes") {
            informationTracker.enquiryId=nepInfoCheckDto.enquiryId
            informationTracker.nepOfficerId=loggedInUser.id
            informationTracker.feedbackSent=nepInfoCheckDto.feedbackSent
            informationTracker.requesterEmail=nepInfoCheckDto.requesterEmail

            nationalEnquiryPointRepository.findByIdOrNull(nepInfoCheckDto.enquiryId)?.let { sts ->

                with(sts) {
                    status=1

                }
                nationalEnquiryPointRepository.save(sts)
                nepRemarksRepository.save(rem)
                informationTrackerRepository.save(informationTracker)
            }?: throw Exception("REQUEST NOT FOUND")
            val subject = nepInfoCheckDto.requesterSubject
            val messageBody= "Dear ${nepInfoCheckDto.requesterName}, This is in response to your enquiry on the above subject.Herein is the feedback as per your request; ${nepInfoCheckDto.feedbackSent}"
            nepInfoCheckDto.requesterEmail?.let {
                if (subject != null) {
                    notifications.sendEmail(it, subject, messageBody)
                }
            }

        }else{


            nationalEnquiry.requesterName=loggedInUser.firstName + loggedInUser.lastName
            nationalEnquiry.requesterComment=nepInfoCheckDto.requesterComment
            nationalEnquiry.requesterCountry="Kenya"
            nationalEnquiry.requesterEmail=nepInfoCheckDto.requesterEmail
            nationalEnquiry.requesterInstitution="Kenya Buraeu of Standards"
            nationalEnquiry.requesterPhone=loggedInUser.cellphone
            nationalEnquiry.requesterSubject=nepInfoCheckDto.requesterSubject
            nationalEnquiry.requestDate=Timestamp(System.currentTimeMillis())
            nationalEnquiry.requesterid=nepInfoCheckDto.enquiryId
            nationalEnquiry.docUploadStatus=nepInfoCheckDto.docUploadStatus
            nationalEnquiryPointRepository.findByIdOrNull(nepInfoCheckDto.enquiryId)?.let { sts ->

                with(sts) {
                    status=2

                }
                nationalEnquiryPointRepository.save(sts)
                nepRemarksRepository.save(rem)
                val up=nationalEnquiryEntityRepository.save(nationalEnquiry)
                informationTrackerRepository.save(informationTracker)
                val targetUrl = "https://kimsint.kebs.org/divResponse/${up.id}";
                val subject = nepInfoCheckDto.requesterSubject
                val messageBody= "Dear Sir/Madam, find below an enquiry for your review and response; ${nepInfoCheckDto.requesterComment}. Click on the link below to respond. ${targetUrl}"
                nepInfoCheckDto.emailAddress.let {
                    if (subject != null) {
                        if (it != null) {
                            notifications.sendEmail(it, subject, messageBody)
                        }
                    }
                }

            }?: throw Exception("REQUEST NOT FOUND")

        }

      return nationalEnquiry
    }

    fun uploadNepDoc(
        uploads: SdNepDocUploadsEntity,
        docFile: MultipartFile,
        doc: String,
        user: String,
        DocDescription: String
    ): SdNepDocUploadsEntity {

        with(uploads) {

            name = commonDaoServices.saveDocuments(docFile)
            fileType = commonDaoServices.getFileTypeByMimetypesFileTypeMap(docFile.name)
            documentType = doc
            description = DocDescription
            document = docFile.bytes
            transactionDate = commonDaoServices.getCurrentDate()
            status = 1
            createdBy = user
            createdOn = commonDaoServices.getTimestamp()
        }


        return sdNepDocUploads.save(uploads)
    }



    fun getNepDivisionRequests(enquiryId: Long): MutableList<NationalEnquiryEntity>
    {
        return nationalEnquiryEntityRepository.getNepDivisionRequests(enquiryId)
    }

    fun responseOnEnquiryInfo(
        nepInfoCheckDto: DivResponseDto
    ) : NationalEnquiryEntity {
        //val loggedInUser = commonDaoServices.loggedInUserDetails()

        val nationalEnquiry=NationalEnquiryEntity();

            nationalEnquiryEntityRepository.findByIdOrNull(nepInfoCheckDto.requestId)?.let { sts ->

                with(sts) {
                    status=1
                    requesterFeedBack=nepInfoCheckDto.requesterFeedBack
                    requesterSubject=nepInfoCheckDto.requesterSubject

                }
                nationalEnquiryEntityRepository.save(sts)
                nationalEnquiryPointRepository.findByIdOrNull(nepInfoCheckDto.requesterid)?.let { stsr ->

                    with(stsr) {
                        status=3

                    }
                    nationalEnquiryPointRepository.save(stsr)
                }?: throw Exception("REQUEST NOT FOUND")



            }?: throw Exception("REQUEST NOT FOUND")



        return nationalEnquiry
    }
    fun getNepDivisionResponse(): MutableList<NationalEnquiryEntity>
    {
        return nationalEnquiryEntityRepository.getNepDivisionResponse()
    }

    fun sendFeedBack(
        nepInfoCheckDto: NepInfoCheckDto
    ) : NationalEnquiryEntity {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val nep=NationalEnquiryPointEntity();
        val rem= NepRemarks()
        val nationalEnquiry=NationalEnquiryEntity();
        val informationTracker= InformationTracker()

        rem.requestId=nepInfoCheckDto.requestId
        rem.remarks=nepInfoCheckDto.comments
        rem.remarkBy=loggedInUser.firstName + loggedInUser.lastName
        rem.role=loggedInUser.typeOfUser
        rem.description="Remarks on Request received"
        rem.dateOfRemark= Timestamp(System.currentTimeMillis())

            informationTracker.enquiryId=nepInfoCheckDto.enquiryId
            informationTracker.nepOfficerId=loggedInUser.id
            informationTracker.feedbackSent=nepInfoCheckDto.feedbackSent
            informationTracker.requesterEmail=nepInfoCheckDto.requesterEmail

            nationalEnquiryPointRepository.findByIdOrNull(nepInfoCheckDto.enquiryId)?.let { sts ->

                with(sts) {
                    status=1

                }
                nationalEnquiryPointRepository.save(sts)
                nepRemarksRepository.save(rem)
                informationTrackerRepository.save(informationTracker)
            }?: throw Exception("REQUEST NOT FOUND")
        nationalEnquiryEntityRepository.findByIdOrNull(nepInfoCheckDto.requestId)?.let { stsr ->

            with(stsr) {
                status=2

            }
            nationalEnquiryEntityRepository.save(stsr)
        }?: throw Exception("REQUEST NOT FOUND")

            val subject = nepInfoCheckDto.requesterSubject
        val messageBody= "Dear ${nepInfoCheckDto.requesterName}, This is in response to your enquiry on the above subject.Herein is the feedback as per your request; ${nepInfoCheckDto.feedbackSent}"
            nepInfoCheckDto.requesterEmail?.let {
                if (subject != null) {
                    notifications.sendEmail(it, subject, messageBody)
                }
            }


        return nationalEnquiry
    }

    //request task list retrieval
    fun getManagerTasks(): List<TaskDetails?>? {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_NEP)
                .processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    //*** Not used *** but closes any Task, linked to task close endpoint
    fun closeTask(taskId: String) {
        taskService.complete(taskId)
    }

    //Main task details function where tasks for different candidate groups can be queried
    private fun getTaskDetails(tasks: List<Task>): List<TaskDetails>? {
        val taskDetails: MutableList<TaskDetails> = ArrayList()
        for (task in tasks) {
            val processVariables = taskService.getVariables(task.id)
            taskDetails.add(TaskDetails(task.id, task.name, processVariables))
        }
        return taskDetails
    }

    //Checker function by NEP_OFFICER for if informaion is available or not
    fun informationAvailable(taskId: String, isAvailable: Boolean): String {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        isAvailable.let { variables.put("Yes", it) }

        taskService.complete(taskId, variables)

        return "Information Is Available"
    }

    //send feedback email if info is available and save response
    fun sendEmailInfoAvailable(informationTracker: InformationTracker, taskId: String): Boolean {
        val variable: MutableMap<String, Any> = HashMap()
        informationTracker.nepOfficerId?.let { variable.put("NEPOfficer", it) }
        informationTracker.feedbackSent?.let { variable.put("feedbackSent", it) }
        informationTracker.requesterEmail?.let { variable.put("requesterEmail", it) }

        informationTrackerRepository.save(informationTracker)
        taskService.complete(taskId, variable)

        //email send 
        val subject = "ENQUIRY RESPONSE"
        val messageBody = informationTracker.feedbackSent
        informationTracker.requesterEmail?.let {
            if (messageBody != null) {
                notifications.sendEmail(it, subject, messageBody)
            }
        }
        //


        println("Process has ended and an email has been sent out with feedback")

        return true
    }

    //function to handle the response by departments or organizations responding to enquiries
    fun departmentOrganizationResponse(departmentResponse: DepartmentResponse, taskId: String): String {
        val variable: MutableMap<String, Any> = HashMap()
        departmentResponse.departmentResponseID.let { variable.put("departmentResponseID", it) }
        departmentResponse.enquiryID.let { variable.put("enquiryID", it) }
        departmentResponse.feedbackProvided.let { variable.put("feedbackProvided", it) }

        departmentResponseRepository.save(departmentResponse)

        taskService.complete(taskId, variable)

        return "Response sent successfully"
    }

    //displays tasks due for organizations
    fun getDepartmentTasks(): List<TaskDetails?>? {
        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_GROUP_DEPT)
                .processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    //returns process history for processes executed
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
                    activity.activityId + " took " + activity.durationInMillis + " milliseconds"
            )
        }

    }
    //make enquiry and process initiation function
    fun notificationOfReview(nep: NepDraftDto): SdNepDraft {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val sNep=SdNepDraft();

        sNep.title=nep.title
        sNep.scope=nep.scope
        sNep.normativeReference=nep.normativeReference
        sNep.symbolsAbbreviatedTerms=nep.symbolsAbbreviatedTerms
        sNep.clause=nep.clause
        sNep.special=nep.special
        sNep.typeOfNotification=nep.typeOfNotification
        sNep.preparedBy=loggedInUser.id
        sNep.datePrepared=Timestamp(System.currentTimeMillis())

        return nepDraftRepo.save(sNep)
    }

    fun uploadNepDraftDoc(
        uploads: SdNepDraftUploadsEntity,
        docFile: MultipartFile,
        doc: String,
        user: String,
        DocDescription: String
    ): SdNepDraftUploadsEntity {

        with(uploads) {

            name = commonDaoServices.saveDocuments(docFile)
            fileType = commonDaoServices.getFileTypeByMimetypesFileTypeMap(docFile.name)
            documentType = doc
            description = DocDescription
            document = docFile.bytes
            transactionDate = commonDaoServices.getCurrentDate()
            status = 1
            createdBy = user
            createdOn = commonDaoServices.getTimestamp()
        }

        val nv= nepDraftDocRepo.save(uploads)
        nepDraftRepo.findByIdOrNull(nv.nepDraftId)?.let { sts ->

            with(sts) {
                uploadDocument = 1

            }
        }?: throw Exception("ENQUIRY NOT FOUND")

        return nv
    }

    fun getDraftNotification(): MutableList<SdNepDraft>
    {
        return nepDraftRepo.getDraftNotification()
    }



    fun findUploadedDraftBYId(draftId: Long): SdNepDraftUploadsEntity {
        return nepDraftDocRepo.findAllByNepDraftId(draftId)
    }

    fun decisionOnReviewDraft(
        nep: NepDraftDecisionDto
    ) : SdNepDraft {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val decision = nep.accentTo
        val rem= NepRemarks()
        val snep=SdNepDraft();
        val informationTracker= InformationTracker()

        rem.nepDraftId=nep.draftId
        rem.remarks=nep.remarks
        rem.remarkBy=loggedInUser.firstName + loggedInUser.lastName
        rem.role=loggedInUser.typeOfUser
        rem.description="Remarks on Draft Notification"
        rem.dateOfRemark= Timestamp(System.currentTimeMillis())
        if (decision == "Yes") {

            nepDraftRepo.findByIdOrNull(nep.draftId)?.let { st ->

                with(st) {
                    status=1
                    notification=nep.notification

                }
                nepDraftRepo.save(st)
                nepRemarksRepository.save(rem)

            }?: throw Exception("DRAFT NOT FOUND")


        }else{

            nepDraftRepo.findByIdOrNull(nep.draftId)?.let { sts ->

                with(sts) {
                    status=3

                }
                nepDraftRepo.save(sts)
                nepRemarksRepository.save(rem)
            }?: throw Exception("DRAFT NOT FOUND")

        }

        return snep
    }

    fun getNotificationForApproval(): MutableList<SdNepDraft>
    {
        return nepDraftRepo.getNotificationForApproval()
    }

    fun decisionOnNotification(
        nep: NepDraftDecisionDto
    ) : SdNepDraft {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val decision = nep.accentTo
        val rem= NepRemarks()
        val snep=SdNepDraft();
        val informationTracker= InformationTracker()

        rem.requestId=nep.draftId
        rem.remarks=nep.remarks
        rem.remarkBy=loggedInUser.firstName + loggedInUser.lastName
        rem.role=loggedInUser.typeOfUser
        rem.description="Remarks on Request recieved"
        rem.dateOfRemark= Timestamp(System.currentTimeMillis())
        if (decision == "Yes") {

            nepDraftRepo.findByIdOrNull(nep.draftId)?.let { sts ->

                with(sts) {
                    status=2

                }
                nepDraftRepo.save(sts)
                nepRemarksRepository.save(rem)

            }?: throw Exception("DRAFT NOT FOUND")


        }else{

            nepDraftRepo.findByIdOrNull(nep.draftId)?.let { sts ->

                with(sts) {
                    status=0

                }
                nepDraftRepo.save(sts)
                nepRemarksRepository.save(rem)
            }?: throw Exception("DRAFT NOT FOUND")

        }

        return snep
    }

    fun getDraftNotificationForUpload(): MutableList<SdNepDraft>
    {
        return nepDraftRepo.getDraftNotificationForUpload()
    }

    fun uploadNotification(nep: NepDraftWtoDto): NEPWtoNotification {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val sNep=NEPWtoNotification();

        sNep.notification=nep.notification
        sNep.draftId=nep.draftId
        sNep.preparedBy=loggedInUser.id
        sNep.dateUploaded=Timestamp(System.currentTimeMillis())

        return nepWtoNotificationRepo.save(sNep)
    }







}

private fun Boolean.toBoolean() {

}

