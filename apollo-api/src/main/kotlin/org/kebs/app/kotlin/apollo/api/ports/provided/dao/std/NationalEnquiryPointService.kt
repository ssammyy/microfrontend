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
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
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
    private val nepWtoNotificationRepo: NEPWtoNotificationRepository,
    private val nepNotificationFormEntityRepo: NepNotificationFormEntityRepository,
    private val applicationMapProperties: ApplicationMapProperties,
    private val publicReviewDraftRepository: PublicReviewDraftRepository,

    ) {
    val callUrl=applicationMapProperties.mapKebsLevyUrl
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
            val messageBody= "Dear ${nepInfoCheckDto.requesterName}, This is in response to your enquiry on the above subject.Herein is the feedback as per your request;<br><br> ${nepInfoCheckDto.feedbackSent}<br><br><br><br><br><br><br>"
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
                val targetUrl = "${callUrl}/${up.id}";
                val subject = nepInfoCheckDto.requesterSubject
                val messageBody= "Dear Sir/Madam, find below an enquiry for your review and response; <br><br> ${nepInfoCheckDto.requesterComment}.<br><br> Click on the link below to respond.<br><br> ${targetUrl}<br><br><br>"
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
        val messageBody= "Dear ${nepInfoCheckDto.requesterName},<br><br> This is in response to your enquiry on the above subject.Herein is the feedback as per your request;<br><br> ${nepInfoCheckDto.feedbackSent}<br><br><br><br>"
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
    fun notificationOfReview(nep: NepNotificationDto): NepNotificationFormEntity {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val sNep=NepNotificationFormEntity();
        val currentTime=Timestamp(System.currentTimeMillis())

        publicReviewDraftRepository.findByIdOrNull(nep.pid)?.let { prd ->

            with(prd) {
                status="Sent To Head Of Trade Affairs"
                modifiedOn=currentTime
                modifiedBy=loggedInUser.id.toString()

            }
            publicReviewDraftRepository.save(prd)
        }?: throw Exception("No public review draft found")

        sNep.datePrepared=currentTime
        val deadline: Timestamp = Timestamp.valueOf(currentTime.toLocalDateTime().plusDays(60))
        sNep.notifyingMember=nep.notifyingMember
        sNep.agencyResponsible=nep.agencyResponsible
        sNep.addressOfAgency=nep.addressOfAgency
        sNep.telephoneOfAgency=nep.telephoneOfAgency
        sNep.faxOfAgency=nep.faxOfAgency
        sNep.emailOfAgency=nep.emailOfAgency
        sNep.websiteOfAgency=nep.websiteOfAgency
        sNep.notifiedUnderArticle=nep.notifiedUnderArticle
        sNep.productsCovered=nep.productsCovered
        sNep.descriptionOfNotifiedDoc=nep.descriptionOfNotifiedDoc
        sNep.objectiveAndRationale=nep.objectiveAndRationale
        sNep.relevantDocuments=nep.relevantDocuments
        sNep.descriptionOfContent=nep.descriptionOfContent
        sNep.proposedDateOfAdoption=nep.proposedDateOfAdoption
        sNep.proposedDateOfEntryIntoForce=nep.proposedDateOfEntryIntoForce
        sNep.finalDateForComments=deadline
        sNep.textAvailableFrom=nep.textAvailableFrom
        sNep.preparedBy=loggedInUser.firstName + loggedInUser.lastName
        sNep.status=0
        sNep.pid=nep.pid
        sNep.cd_Id=nep.cd_Id
        sNep.prd_name=nep.prd_name
        sNep.ks_NUMBER=nep.ks_NUMBER
        sNep.organization=nep.organization
        sNep.prd_by=nep.prd_by
        sNep.prdStatus=nep.status
        sNep.created_on=nep.created_on
        sNep.number_OF_COMMENTS=nep.number_OF_COMMENTS
        sNep.var_FIELD_1=nep.var_FIELD_1

        return nepNotificationFormEntityRepo.save(sNep)
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
        val saved= nepDraftDocRepo.save(uploads)
        nepNotificationFormEntityRepo.findByIdOrNull(saved.nepDraftId)?.let { sts ->

            with(sts) {
                documentAttached = 1

            }
        }?: throw Exception("FORM NOT FOUND")


        return saved
    }

    fun getDraftNotification(): MutableList<NepNotificationFormEntity>
    {
        return nepNotificationFormEntityRepo.getDraftNotification()
    }



    fun findUploadedDraftBYId(draftId: Long): SdNepDraftUploadsEntity {
        return nepDraftDocRepo.findAllByNepDraftId(draftId)
    }

    fun decisionOnReviewDraft(
        nep: NepDraftDecisionDto
    ) : NepNotificationFormEntity {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val decision = nep.accentTo
        val rem= NepRemarks()
        val snep=NepNotificationFormEntity();
        val informationTracker= InformationTracker()

        rem.nepDraftId=nep.draftId
        rem.remarks=nep.remarks
        rem.remarkBy=loggedInUser.firstName + loggedInUser.lastName
        rem.role=loggedInUser.typeOfUser
        rem.description="Remarks on Draft Notification"
        rem.dateOfRemark= Timestamp(System.currentTimeMillis())
        if (decision == "Yes") {

            nepNotificationFormEntityRepo.findByIdOrNull(nep.draftId)?.let { st ->

                with(st) {
                    status=1
                    notifyingMember=nep.notifyingMember
                    agencyResponsible=nep.agencyResponsible
                    addressOfAgency=nep.addressOfAgency
                    telephoneOfAgency=nep.telephoneOfAgency
                    faxOfAgency=nep.faxOfAgency
                    emailOfAgency=nep.emailOfAgency
                    websiteOfAgency=nep.websiteOfAgency
                    notifiedUnderArticle=nep.notifiedUnderArticle
                    productsCovered=nep.productsCovered
                    descriptionOfNotifiedDoc=nep.descriptionOfNotifiedDoc
                    objectiveAndRationale=nep.objectiveAndRationale
                    descriptionOfContent=nep.descriptionOfContent
                    relevantDocuments=nep.relevantDocuments
                    //proposedDateOfAdoption=nep.proposedDateOfAdoption
                    //proposedDateOfEntryIntoForce=nep.proposedDateOfEntryIntoForce
                    //finalDateForComments=nep.finalDateForComments
                    textAvailableFrom=nep.textAvailableFrom

                }
                nepNotificationFormEntityRepo.save(st)
                nepRemarksRepository.save(rem)

            }?: throw Exception("DRAFT NOT FOUND")


        }else{

            nepNotificationFormEntityRepo.findByIdOrNull(nep.draftId)?.let { sts ->

                with(sts) {
                    status=3

                }
                nepNotificationFormEntityRepo.save(sts)
                nepRemarksRepository.save(rem)
            }?: throw Exception("DRAFT NOT FOUND")

        }

        return snep
    }

    fun getNotificationForApproval(): MutableList<NepNotificationFormEntity>
    {
        return nepNotificationFormEntityRepo.getNotificationForApproval()
    }

    fun decisionOnNotification(
        nep: NepDraftDecisionDto
    ) : NepNotificationFormEntity {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val decision = nep.accentTo
        val rem= NepRemarks()
        val snep=NepNotificationFormEntity();
        val informationTracker= InformationTracker()

        rem.requestId=nep.draftId
        rem.remarks=nep.remarks
        rem.remarkBy=loggedInUser.firstName + loggedInUser.lastName
        rem.role=loggedInUser.typeOfUser
        rem.description="Remarks on Request recieved"
        rem.dateOfRemark= Timestamp(System.currentTimeMillis())
        if (decision == "Yes") {

            nepNotificationFormEntityRepo.findByIdOrNull(nep.draftId)?.let { sts ->

                with(sts) {
                    status=2

                }
                nepNotificationFormEntityRepo.save(sts)
                nepRemarksRepository.save(rem)

            }?: throw Exception("DRAFT NOT FOUND")


        }else{

            nepNotificationFormEntityRepo.findByIdOrNull(nep.draftId)?.let { sts ->

                with(sts) {
                    status=0

                }
                nepNotificationFormEntityRepo.save(sts)
                nepRemarksRepository.save(rem)
            }?: throw Exception("DRAFT NOT FOUND")

        }

        return snep
    }

    fun getDraftNotificationForUpload(): MutableList<NepNotificationFormEntity>
    {
        return nepNotificationFormEntityRepo.getDraftNotificationForUpload()
    }

    fun uploadNotification(nep: NepDraftDecDto): NepNotificationFormEntity {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val sNep=NepNotificationFormEntity();

        nepNotificationFormEntityRepo.findByIdOrNull(nep.draftId)?.let { st ->

            with(st) {
                status = 4
                notifyingMember = nep.notifyingMember
                agencyResponsible = nep.agencyResponsible
                addressOfAgency = nep.addressOfAgency
                telephoneOfAgency = nep.telephoneOfAgency
                faxOfAgency = nep.faxOfAgency
                emailOfAgency = nep.emailOfAgency
                websiteOfAgency = nep.websiteOfAgency
                notifiedUnderArticle = nep.notifiedUnderArticle
                productsCovered = nep.productsCovered
                descriptionOfNotifiedDoc = nep.descriptionOfNotifiedDoc
                objectiveAndRationale = nep.objectiveAndRationale
                descriptionOfContent = nep.descriptionOfContent
                relevantDocuments = nep.relevantDocuments
                proposedDateOfAdoption = nep.proposedDateOfAdoption
                proposedDateOfEntryIntoForce = nep.proposedDateOfEntryIntoForce
                finalDateForComments = nep.finalDateForComments
                textAvailableFrom = nep.textAvailableFrom

            }
             nepNotificationFormEntityRepo.save(st)

        }
        return sNep
    }

    fun getUploadedNotification(): MutableList<NepNotificationFormEntity>
    {
        return nepNotificationFormEntityRepo.getUploadedNotification()
    }
}

private fun Boolean.toBoolean() {

}

