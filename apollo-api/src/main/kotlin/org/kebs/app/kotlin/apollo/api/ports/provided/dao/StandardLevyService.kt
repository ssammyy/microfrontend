package org.kebs.app.kotlin.apollo.api.ports.provided.dao

import com.google.gson.Gson
import mu.KotlinLogging
import org.flowable.engine.*
import org.flowable.engine.repository.Deployment
import org.flowable.task.api.Task
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.StandardsLevyBpmn
import org.kebs.app.kotlin.apollo.common.dto.ms.LevyPaymentDTO
import org.kebs.app.kotlin.apollo.common.dto.std.TaskDetailsBody
import org.kebs.app.kotlin.apollo.common.dto.stdLevy.*
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.registration.CompanyProfileEditEntity
import org.kebs.app.kotlin.apollo.store.model.registration.CompanyProfileEntity
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.*
import org.kebs.app.kotlin.apollo.store.repo.std.CompanyStandardRepository
import org.kebs.app.kotlin.apollo.store.repo.std.UserListRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.sql.Date
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*


@Service
class StandardLevyService(
    private val runtimeService: RuntimeService,
    private val taskService: TaskService,
    private val historyService: HistoryService,
    @Qualifier("processEngine") private val processEngine: ProcessEngine,
    private val repositoryService: RepositoryService,
    private val iStagingStandardsLevyManufacturerPenaltyRepository: IStagingStandardsLevyManufacturerPenaltyRepository,
    private val iStandardLevyPaymentsRepository: IStandardLevyPaymentsRepository,
    private val companyProfileRepo: ICompanyProfileRepository,
    private val commonDaoServices: CommonDaoServices,
    private val standardLevyFactoryVisitReportRepo: IStandardLevyFactoryVisitReportRepository,
    private val slUploadsRepo: ISlVisitUploadsRepository,
    private val bpmnService: StandardsLevyBpmn,
    private val userListRepository: UserListRepository,
    private val usersEntityRepository: UsersEntityRepository,
    private val iUserRoleAssignmentsRepository: IUserRoleAssignmentsRepository,
    private val iBusinessNatureRepository: IBusinessNatureRepository,
    private val companyProfileEditEntityRepository: CompanyProfileEditEntityRepository,
    private val stdLevyNotificationFormRepo: IStdLevyNotificationFormRepository,
    private val stdLevyNotificationFormRepository: StdLevyNotificationFormRepository,
    private val notifications: Notifications,
    private val manufacturePlantRepository: IManufacturePlantDetailsRepository,
    private val standardLevySiteVisitRemarksRepository: StandardLevySiteVisitRemarksRepository,
    private val applicationMapProperties: ApplicationMapProperties,
    private val daoService: DaoService,
    private val iRegionsRepository: IRegionsRepository,
    private val iTownsRepository: ITownsRepository,
    private val iCountiesRepository: ICountiesRepository,
    private val businessLinesRepo: IBusinessLinesRepository,
    private val businessNatureRepo: IBusinessNatureRepository,
    private val iCompanyProfileDirectorsRepository: ICompanyProfileDirectorsRepository,
    private val standardLevyOperationsClosureRepository: StandardLevyOperationsClosureRepository,
    private val standardLevyOperationsSuspensionRepository: StandardLevyOperationsSuspensionRepository,
    private val slWindingUpReportUploadsEntityRepository: SlWindingUpReportUploadsEntityRepository,
    private val emailVerificationTokenEntityRepo: EmailVerificationTokenEntityRepo,
    private val iUserRepository: IUserRepository,
    private val companyRepo: ICompanyProfileRepository,
    private val companyStandardRepository: CompanyStandardRepository,


    ) {
    val PROCESS_DEFINITION_KEY = "sl_SiteVisitProcessFlow"
    val TASK_CANDIDATE_SL_PRINCIPAL_LEVY_OFFICER = "SL_PRINCIPAL_LEVY_OFFICER"
    val TASK_CANDIDATE_SL_Assistant_Manager = "SL_Assistant_Manager"
    val TASK_CANDIDATE_SL_Manager = "SL_Manager"
    val TASK_CANDIDATE_Manufacturer = "Manufacturer"

    //deploy bpmn file
    fun deployProcessDefinition(): Deployment = repositoryService
        .createDeployment()
        .addClasspathResource("processes/stdLevy/Sl_Site_Visit.bpmn20.xml")
        .deploy()

    //start the process by process Key
    fun startProcessByKey(): ProcessInstanceSiteResponse {

        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY)
        return ProcessInstanceSiteResponse(processInstance.id, processInstance.isEnded)
    }

    //List Manufactures
//    fun getManufactureList(): MutableIterable<CompanyProfileEntity> {
//        return companyProfileRepo.findAll()
//    }



    fun getManufacturerPenaltyHistory(): MutableIterable<StagingStandardsLevyManufacturerPenalty> {
        return iStagingStandardsLevyManufacturerPenaltyRepository.findAll()
    }

    fun getManufacturerPenalty(): MutableIterable<StagingStandardsLevyManufacturerPenalty> {
        return iStagingStandardsLevyManufacturerPenaltyRepository.findAll()
    }

    fun getPaidLevies(): MutableIterable<StandardLevyPaymentsEntity> {
        return iStandardLevyPaymentsRepository.findAll()
    }

    fun editCompanyDetails(
        companyProfileEntity: CompanyProfileEntity,
        companyProfileEditEntity: CompanyProfileEditEntity,
        standardLevySiteVisitRemarks: StandardLevySiteVisitRemarks
    ): ProcessInstanceSiteResponse {

        val loggedInUser = commonDaoServices.loggedInUserDetailsEmail()
        val variables: MutableMap<String, Any> = mutableMapOf()
        companyProfileEditEntity.name?.let { variables["companyName"] = it }
        companyProfileEditEntity.kraPin?.let { variables["kraPin"] = it }
        companyProfileEditEntity.registrationNumber?.let { variables["registrationNumber"] = it }
        companyProfileEditEntity.entryNumber?.let { variables["entryNumber"] = it }
        companyProfileEditEntity.manufactureId?.let { variables["manufactureId"] = it }
        companyProfileEntity.physicalAddress?.let { variables["physicalAddress"] = it }
        companyProfileEditEntity.physicalAddress?.let { variables["physicalAddressEdit"] = it }
        companyProfileEditEntity.postalAddress?.let { variables["postalAddressEdit"] = it }
        companyProfileEntity.postalAddress?.let { variables["postalAddress"] = it }
        companyProfileEditEntity.ownership?.let { variables["ownershipEdit"] = it }
        companyProfileEntity.ownership?.let { variables["ownership"] = it }
        companyProfileEditEntity.taskType?.let { variables["taskType"] = it }
        companyProfileEditEntity.userType?.let { variables["userType"] = it }
        companyProfileEditEntity.assignedTo?.let { variables["assignedTo"] = it }
        companyProfileEditEntity.createdBy = loggedInUser.id.toString()
        companyProfileEditEntity.createdBy?.let{variables.put("originator", it)}
        companyProfileEditEntity.createdOn = commonDaoServices.getTimestamp()
        companyProfileEditEntity.createdOn?.let{variables.put("createdOn", it)}
        companyProfileEditEntity.typeOfManufacture?.let{variables.put("typeOfManufacture", it)}
        companyProfileEditEntity.otherBusinessNatureType?.let{variables.put("otherBusinessNatureType", it)}
        companyProfileEditEntity.yearlyTurnover?.let{variables.put("yearlyTurnoverEdit", it)}
        companyProfileEntity.yearlyTurnover?.let{variables.put("yearlyTurnover", it)}
        companyProfileEditEntity.companyTelephone?.let{variables.put("companyTelephoneEdit", it)}
        companyProfileEntity.companyTelephone?.let{variables.put("companyTelephone", it)}
        companyProfileEditEntity.companyEmail?.let{variables.put("companyEmailEdit", it)}
        companyProfileEntity.companyEmail?.let{variables.put("companyEmail", it)}

        companyProfileEditEntity.status=1
        val userIntType = companyProfileEditEntity.userType
        val plUserTypes = 61L
        val asManagerUserTypes = 62L
        val editDetails = companyProfileEditEntityRepository.save(companyProfileEditEntity)
        variables["editID"] = editDetails.id

        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables)

        val approverFname=loggedInUser.firstName
        val approverLname=loggedInUser.lastName
        val approveName= "$approverFname  $approverLname"
        standardLevySiteVisitRemarks.siteVisitId= editDetails.id
        standardLevySiteVisitRemarks.remarks= standardLevySiteVisitRemarks.remarks
        standardLevySiteVisitRemarks.role = standardLevySiteVisitRemarks.role
        standardLevySiteVisitRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
        standardLevySiteVisitRemarks.remarkBy = approveName

        standardLevySiteVisitRemarksRepository.save(standardLevySiteVisitRemarks)


        taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
            ?.let { t ->
                t.list()[0]
                    ?.let { task ->
                        task.assignee =
                            "${companyProfileEditEntity.assignedTo ?: throw NullValueNotAllowedException(" invalid user id provided")}"  //set the assignee}"

                        taskService.saveTask(task)
                    }
                    ?: KotlinLogging.logger { }.error("Task list empty for $PROCESS_DEFINITION_KEY ")


            }
            ?: KotlinLogging.logger { }.error("No task found for $PROCESS_DEFINITION_KEY ")

        var userList= companyStandardRepository.getUserEmail(companyProfileEditEntity?.assignedTo)
        userList.forEach { item->
            val recipient= item.getUserEmail()
            val subject = "Company Details Edited"
            val messageBody= "Dear ${item.getFirstName()} ${item.getLastName()}, Company Details for ${companyProfileEditEntity.name} have been edited. Kindly login to the KIMS System to Confirm "
            if (recipient != null) {
                notifications.sendEmail(recipient, subject, messageBody)
            }
        }

        when (userIntType) {
            plUserTypes -> {
                bpmnService.slAssignTask(
                    processInstance.processInstanceId,
                    "Confirm Edited Details",
                    companyProfileEditEntity?.assignedTo
                        ?: throw NullValueNotAllowedException("invalid user id provided")
                )

            }
            asManagerUserTypes -> {
                bpmnService.slAssignTask(
                    processInstance.processInstanceId,
                    "Approve Company Details",
                    companyProfileEditEntity?.assignedTo
                        ?: throw NullValueNotAllowedException("invalid user id provided")
                )
            }
        }

        return ProcessInstanceSiteResponse(processInstance.id, processInstance.isEnded)


    }
    fun getComEditRemarks(editID: Long): List<StandardLevySiteVisitRemarks> {
        standardLevySiteVisitRemarksRepository.findAllBySiteVisitIdOrderByIdDesc(editID)?.let {
            return it
        }
            ?: throw ExpectedDataNotFound("No Data Found")
    }

    fun editCompanyDetailsConfirm(
        companyProfileEntity: CompanyProfileEntity,
        standardLevySiteVisitRemarks: StandardLevySiteVisitRemarks
    ): List<TaskDetailsBody> {

        companyProfileRepo.findByIdOrNull(companyProfileEntity.id)
            ?.let { entity ->

                val loggedInUser = commonDaoServices.loggedInUserDetailsEmail()
                val variables: MutableMap<String, Any> = mutableMapOf()
                companyProfileEntity.physicalAddress?.let { variables["physicalAddressEdit"] = it }
                companyProfileEntity.postalAddress?.let { variables["postalAddressEdit"] = it }
                companyProfileEntity.ownership?.let { variables["ownershipEdit"] = it }
                companyProfileEntity.yearlyTurnover?.let{variables.put("yearlyTurnoverEdit", it)}
                companyProfileEntity.companyTelephone?.let{variables.put("companyTelephoneEdit", it)}
                companyProfileEntity.companyEmail?.let{variables.put("companyEmailEdit", it)}
                companyProfileEntity.createdBy = loggedInUser.userName
                companyProfileEntity.createdBy?.let{variables.put("createdBy", it)}
                companyProfileEntity.modifiedOn = commonDaoServices.getTimestamp()
                companyProfileEntity.modifiedOn?.let{variables.put("modifiedOn", it)}
                companyProfileEntity.taskId?.let { variables.put("taskId", it) }
                companyProfileEntity.slBpmnProcessInstance?.let { variables.put("processId", it) }
                companyProfileEntity.assignedTo?.let { variables.put("assignedTo", it) }

                companyProfileEntity.accentTo?.let { variables["No"] = it }
                companyProfileEntity.accentTo?.let { variables["Yes"] = it }
                standardLevySiteVisitRemarks.siteVisitId?.let { variables.put("editID", it) }
                val approverFname=loggedInUser.firstName
                val approverLname=loggedInUser.lastName
                val approveName= "$approverFname  $approverLname"
                standardLevySiteVisitRemarks.siteVisitId= standardLevySiteVisitRemarks.siteVisitId
                standardLevySiteVisitRemarks.remarks= standardLevySiteVisitRemarks.remarks
                standardLevySiteVisitRemarks.role = standardLevySiteVisitRemarks.role
                standardLevySiteVisitRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
                standardLevySiteVisitRemarks.remarkBy = approveName

                standardLevySiteVisitRemarksRepository.save(standardLevySiteVisitRemarks)


                if (variables["Yes"] == true) {
                    entity.apply {
                        physicalAddress = companyProfileEntity.physicalAddress
                        postalAddress = companyProfileEntity.postalAddress
                        ownership = companyProfileEntity.ownership
                        yearlyTurnover = companyProfileEntity.yearlyTurnover
                        companyTelephone = companyProfileEntity.companyTelephone
                        companyEmail = companyProfileEntity.companyEmail
                        modifiedBy = loggedInUser.userName
                        modifiedOn = commonDaoServices.getTimestamp()
                    }

                    companyProfileRepo.save(entity)

                    var userList= companyStandardRepository.getUserEmail(companyProfileEntity?.assignedTo)
                    userList.forEach { item->
                        val recipient= item.getUserEmail()
                        val subject = "Company Details Approved"
                        val messageBody= "Dear ${item.getFirstName()} ${item.getLastName()}, Company Details Edited for ${companyProfileEntity.name} have been Approved."
                        if (recipient != null) {
                            notifications.sendEmail(recipient, subject, messageBody)
                        }
                    }

                    taskService.complete(companyProfileEntity.taskId, variables)
                }
                if (variables["No"] == false) {
                    var userList= companyStandardRepository.getUserEmail(companyProfileEntity?.assignedTo)
                    userList.forEach { item->
                        val recipient= item.getUserEmail()
                        val subject = "Company Details Rejected"
                        val messageBody= "Dear ${item.getFirstName()} ${item.getLastName()}, Company Details Edited for ${companyProfileEntity.name} were rejected."
                        if (recipient != null) {
                            notifications.sendEmail(recipient, subject, messageBody)
                        }
                    }
                    taskService.complete(companyProfileEntity.taskId, variables)

                }

            } ?: throw Exception("COMPANY NOT FOUND")
return getUserTasks();

    }

    fun editCompanyDetailsConfirmLvlOne(
        companyProfileEntity: CompanyProfileEntity,
        standardLevySiteVisitRemarks: StandardLevySiteVisitRemarks
    ): List<TaskDetailsBody> {

        companyProfileRepo.findByIdOrNull(companyProfileEntity.id)
            ?.let { entity ->

                val loggedInUser = commonDaoServices.loggedInUserDetailsEmail()
                val variables: MutableMap<String, Any> = mutableMapOf()
                companyProfileEntity.physicalAddress?.let { variables["physicalAddressEdit"] = it }
                companyProfileEntity.postalAddress?.let { variables["postalAddressEdit"] = it }
                companyProfileEntity.ownership?.let { variables["ownershipEdit"] = it }
                companyProfileEntity.yearlyTurnover?.let{variables.put("yearlyTurnoverEdit", it)}
                companyProfileEntity.companyTelephone?.let{variables.put("companyTelephoneEdit", it)}
                companyProfileEntity.companyEmail?.let{variables.put("companyEmailEdit", it)}
                companyProfileEntity.createdBy = loggedInUser.id.toString()
                companyProfileEntity.createdBy?.let{variables.put("createdBy", it)}
                companyProfileEntity.modifiedOn = commonDaoServices.getTimestamp()
                companyProfileEntity.modifiedOn?.let{variables.put("modifiedOn", it)}
                companyProfileEntity.taskId?.let { variables.put("taskId", it) }
                companyProfileEntity.slBpmnProcessInstance?.let { variables.put("processId", it) }
                companyProfileEntity.assignedTo?.let { variables.put("assignedTo", it) }
                companyProfileEntity.accentTo?.let { variables["No"] = it }
                companyProfileEntity.accentTo?.let { variables["Yes"] = it }
                standardLevySiteVisitRemarks.siteVisitId?.let { variables.put("editID", it) }
                val approverFname=loggedInUser.firstName
                val approverLname=loggedInUser.lastName
                val approveName= "$approverFname  $approverLname"
                standardLevySiteVisitRemarks.siteVisitId= standardLevySiteVisitRemarks.siteVisitId
                standardLevySiteVisitRemarks.remarks= standardLevySiteVisitRemarks.remarks
                standardLevySiteVisitRemarks.role = standardLevySiteVisitRemarks.role
                standardLevySiteVisitRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
                standardLevySiteVisitRemarks.remarkBy = approveName

                standardLevySiteVisitRemarksRepository.save(standardLevySiteVisitRemarks)


                if (variables["Yes"] == true) {
                    var userList= companyStandardRepository.getUserEmail(companyProfileEntity?.assignedTo)
                    userList.forEach { item->
                        val recipient= item.getUserEmail()
                        val subject = "Company Details Approved"
                        val messageBody= "Dear ${item.getFirstName()} ${item.getLastName()}, Company Details edited for ${companyProfileEntity.name} have been approved"
                        if (recipient != null) {
                            notifications.sendEmail(recipient, subject, messageBody)
                        }
                    }

                    runtimeService.createProcessInstanceQuery()
                        .processInstanceId(companyProfileEntity.slBpmnProcessInstance).list()
                        ?.let { l ->
                            val processInstance = l[0]
                            taskService.complete(companyProfileEntity.taskId, variables)

                            taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
                                ?.let { t ->
                                    t.list()[0]
                                        ?.let { task ->
                                            task.assignee = "${
                                                companyProfileEntity.assignedTo ?: throw NullValueNotAllowedException(
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
                                "Approve Edited Company Details",
                                companyProfileEntity.assignedTo
                                    ?: throw NullValueNotAllowedException("invalid user id provided")
                            )

                        }
                        ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${companyProfileEntity.slBpmnProcessInstance} ")
                }
                if (variables["No"] == false) {
                    var userList= companyStandardRepository.getUserEmail(companyProfileEntity?.assignedTo)
                    userList.forEach { item->
                        val recipient= item.getUserEmail()
                        val subject = "Company Details Rejected"
                        val messageBody= "Dear ${item.getFirstName()} ${item.getLastName()}, Company Details edited for ${companyProfileEntity.name} have been rejected."
                        if (recipient != null) {
                            notifications.sendEmail(recipient, subject, messageBody)
                        }
                    }
                    taskService.complete(companyProfileEntity.taskId, variables)

                }

            } ?: throw Exception("COMPANY NOT FOUND")
        return getUserTasks();

    }


    fun editCompanyDetailsConfirmLvlTwo(
        companyProfileEntity: CompanyProfileEntity,
        standardLevySiteVisitRemarks: StandardLevySiteVisitRemarks
    ): List<TaskDetailsBody> {

        companyProfileRepo.findByIdOrNull(companyProfileEntity.id)
            ?.let { entity ->

                val loggedInUser = commonDaoServices.loggedInUserDetailsEmail()
                val variables: MutableMap<String, Any> = mutableMapOf()
                companyProfileEntity.physicalAddress?.let { variables["physicalAddressEdit"] = it }
                companyProfileEntity.postalAddress?.let { variables["postalAddressEdit"] = it }
                companyProfileEntity.ownership?.let { variables["ownershipEdit"] = it }
                companyProfileEntity.yearlyTurnover?.let{variables.put("yearlyTurnoverEdit", it)}
                companyProfileEntity.companyTelephone?.let{variables.put("companyTelephoneEdit", it)}
                companyProfileEntity.companyEmail?.let{variables.put("companyEmailEdit", it)}
                companyProfileEntity.createdBy = loggedInUser.userName
                companyProfileEntity.createdBy?.let{variables.put("createdBy", it)}
                companyProfileEntity.modifiedOn = commonDaoServices.getTimestamp()
                companyProfileEntity.modifiedOn?.let{variables.put("modifiedOn", it)}
                companyProfileEntity.taskId?.let { variables.put("taskId", it) }
                companyProfileEntity.assignedTo?.let { variables.put("assignedTo", it) }
                companyProfileEntity.accentTo?.let { variables["No"] = it }
                companyProfileEntity.accentTo?.let { variables["Yes"] = it }
                standardLevySiteVisitRemarks.siteVisitId?.let { variables.put("editID", it) }
                val approverFname=loggedInUser.firstName
                val approverLname=loggedInUser.lastName
                val approveName= "$approverFname  $approverLname"
                standardLevySiteVisitRemarks.siteVisitId= standardLevySiteVisitRemarks.siteVisitId
                standardLevySiteVisitRemarks.remarks= standardLevySiteVisitRemarks.remarks
                standardLevySiteVisitRemarks.role = standardLevySiteVisitRemarks.role
                standardLevySiteVisitRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
                standardLevySiteVisitRemarks.remarkBy = approveName

                standardLevySiteVisitRemarksRepository.save(standardLevySiteVisitRemarks)


                if (variables["Yes"] == true) {
                    var userList= companyStandardRepository.getUserEmail(companyProfileEntity?.assignedTo)
                    userList.forEach { item->
                        val recipient= item.getUserEmail()
                        val subject = "Company Details Approved"
                        val messageBody= "Dear ${item.getFirstName()} ${item.getLastName()}, Company Details edited for ${companyProfileEntity.name} have been approved."
                        if (recipient != null) {
                            notifications.sendEmail(recipient, subject, messageBody)
                        }
                    }
                    entity.apply {
                        physicalAddress = companyProfileEntity.physicalAddress
                        postalAddress = companyProfileEntity.postalAddress
                        ownership = companyProfileEntity.ownership
                        yearlyTurnover = companyProfileEntity.yearlyTurnover
                        companyTelephone = companyProfileEntity.companyTelephone
                        companyEmail = companyProfileEntity.companyEmail
                        modifiedBy = loggedInUser.userName
                        modifiedOn = commonDaoServices.getTimestamp()
                    }

                    companyProfileRepo.save(entity)

                    taskService.complete(companyProfileEntity.taskId, variables)
                }
                if (variables["No"] == false) {
                    var userList= companyStandardRepository.getUserEmail(companyProfileEntity?.assignedTo)
                    userList.forEach { item->
                        val recipient= item.getUserEmail()
                        val subject = "Company Details Rejected"
                        val messageBody= "Dear ${item.getFirstName()} ${item.getLastName()}, Company Details edited for ${companyProfileEntity.name} have been rejected."
                        if (recipient != null) {
                            notifications.sendEmail(recipient, subject, messageBody)
                        }
                    }
                    runtimeService.createProcessInstanceQuery()
                        .processInstanceId(companyProfileEntity.slBpmnProcessInstance).list()
                        ?.let { l ->
                            val processInstance = l[0]
                            taskService.complete(companyProfileEntity.taskId, variables)

                            taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
                                ?.let { t ->
                                    t.list()[0]
                                        ?.let { task ->
                                            task.assignee = "${
                                                companyProfileEntity.assignedTo ?: throw NullValueNotAllowedException(
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
                                "Confirm Edited Details",
                                companyProfileEntity.assignedTo
                                    ?: throw NullValueNotAllowedException("invalid user id provided")
                            )

                        }
                        ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${companyProfileEntity.slBpmnProcessInstance} ")

                }

            } ?: throw Exception("COMPANY NOT FOUND")
        return getUserTasks();

    }

    fun assignCompany(
        companyProfileEntity: CompanyProfileEntity
    ): ProcessInstanceSiteResponse {
        val loggedInUser = commonDaoServices.loggedInUserDetailsEmail()

        val variables: MutableMap<String, Any> = mutableMapOf()
                    companyProfileEntity.assignedTo?.let { variables["assignedTo"] = it }
                    companyProfileEntity.name?.let { variables["companyName"] = it }
                    companyProfileEntity.kraPin?.let { variables["kraPin"] = it }
                    companyProfileEntity.status?.let { variables["status"] = it }
                    companyProfileEntity.registrationNumber?.let { variables["registrationNumber"] = it }
                    companyProfileEntity.postalAddress?.let { variables["postalAddress"] = it }
                    companyProfileEntity.physicalAddress?.let { variables["physicalAddress"] = it }
                    companyProfileEntity.plotNumber?.let { variables["plotNumber"] = it }
                    companyProfileEntity.companyEmail?.let { variables["companyEmail"] = it }
                    companyProfileEntity.companyTelephone?.let { variables["companyTelephone"] = it }
                    companyProfileEntity.yearlyTurnover?.let { variables["yearlyTurnover"] = it }
                    companyProfileEntity.buildingName?.let { variables["buildingName"] = it }
                    companyProfileEntity.directorIdNumber?.let { variables["directorIdNumber"] = it }
                    companyProfileEntity.businessLineName?.let { variables["businessLineName"] = it }
                    companyProfileEntity.businessLines?.let { variables["businessLines"] = it }
                    companyProfileEntity.businessNatureName?.let { variables["businessNatureName"] = it }
                    companyProfileEntity.businessNatures?.let { variables["businessNatures"] = it }
                    companyProfileEntity.regionName?.let { variables["regionName"] = it }
                    companyProfileEntity.region?.let { variables["region"] = it }
                    companyProfileEntity.countyName?.let { variables["countyName"] = it }
                    companyProfileEntity.county?.let { variables["county"] = it }
                    companyProfileEntity.townName?.let { variables["townName"] = it }
                    companyProfileEntity.town?.let { variables["town"] = it }
                    companyProfileEntity.branchName?.let { variables["branchName"] = it }
                    companyProfileEntity.streetName?.let { variables["streetName"] = it }
                    companyProfileEntity.manufactureStatus?.let { variables["manufactureStatus"] = it }
                    companyProfileEntity.entryNumber?.let { variables["entryNumber"] = it }
                    companyProfileEntity.id?.let { variables["manufacturerEntity"] = it }
                        ?: throw Exception("COMPANY NOT FOUND")
                    companyProfileEntity.userId?.let { variables["contactId"] = it }
                    companyProfileEntity.taskType?.let { variables["taskType"] = it }
                    companyProfileEntity.typeOfManufacture?.let { variables["typeOfManufacture"] = it }
                    companyProfileEntity.otherBusinessNatureType?.let { variables["otherBusinessNatureType"] = it }

                    companyProfileEntity.assignedTo = companyProfileEntity.assignedTo
                    companyProfileEntity.assignStatus = 1
                    companyProfileEntity.createdBy = loggedInUser.userName
                    companyProfileEntity.createdOn = commonDaoServices.getTimestamp()
                    val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables)
                    companyProfileEntity.slBpmnProcessInstance = processInstance?.processInstanceId

//                val gson = Gson()
//                 KotlinLogging.logger { }.info { "Save Entity" + gson.toJson(companyProfileEntity) }

        var userList= companyStandardRepository.getUserEmail(companyProfileEntity?.assignedTo)
        userList.forEach { item->
            val recipient= item.getUserEmail()
            val subject = "Schedule Site Visit"
            val messageBody= "Dear ${item.getFirstName()} ${item.getLastName()}, Company  ${companyProfileEntity.name} has been assigned to you. Kindly Log into the KIMS System to schedule a site visit. NB. Auto Generated E-Mail From KEBS "
            if (recipient != null) {
                notifications.sendEmail(recipient, subject, messageBody)
            }
        }

                    companyProfileRepo.save(companyProfileEntity)
                    taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
                        ?.let { t ->
                            t.list()[0]
                                ?.let { task ->
                                    task.assignee =
                                        "${companyProfileEntity.assignedTo ?: throw NullValueNotAllowedException(" invalid user id provided")}"  //set the assignee}"

                                    taskService.saveTask(task)
                                }
                                ?: KotlinLogging.logger { }.error("Task list empty for $PROCESS_DEFINITION_KEY ")


                        }
                        ?: KotlinLogging.logger { }.error("No task found for $PROCESS_DEFINITION_KEY ")
                    bpmnService.slAssignTask(
                        processInstance.processInstanceId,
                        "Schedule Site Visit",
                        companyProfileEntity?.assignedTo
                            ?: throw NullValueNotAllowedException("invalid user id provided")
                    )
                    return ProcessInstanceSiteResponse(processInstance.id, processInstance.isEnded)
    }



    fun scheduleSiteVisit(
        standardLevyFactoryVisitReportEntity: StandardLevyFactoryVisitReportEntity
    ): ProcessInstanceResponseSite {
        val loggedInUser = commonDaoServices.loggedInUserDetailsEmail()
        val variables: MutableMap<String, Any> = java.util.HashMap()
        standardLevyFactoryVisitReportEntity.manufacturerEntity?.let { variables["manufacturerEntity"] = it }
        standardLevyFactoryVisitReportEntity.scheduledVisitDate?.let { variables["scheduledVisitDate"] = it }
        standardLevyFactoryVisitReportEntity.createdBy?.let { variables["createdBy"] = it }
        standardLevyFactoryVisitReportEntity.status?.let { variables["visitStatus"] = it }
        standardLevyFactoryVisitReportEntity.assigneeId?.let { variables["originator"] = it }
        standardLevyFactoryVisitReportEntity.companyName?.let { variables["companyName"] = it }
        standardLevyFactoryVisitReportEntity.entryNumber?.let { variables["entryNumber"] = it }
        standardLevyFactoryVisitReportEntity.kraPin?.let { variables["kraPin"] = it }
        standardLevyFactoryVisitReportEntity.registrationNumber?.let { variables["registrationNumber"] = it }

        val visitDetails = standardLevyFactoryVisitReportRepo.save(standardLevyFactoryVisitReportEntity)
        visitDetails.id?.let { variables["visitID"] = it }


        companyProfileRepo.findByIdOrNull(standardLevyFactoryVisitReportEntity.manufacturerEntity)
            ?.let { companyProfileEntity ->
                runtimeService.createProcessInstanceQuery()
                    .processInstanceId(standardLevyFactoryVisitReportEntity.slProcessInstanceId).list()
                    ?.let { l ->
                        val processInstance = l[0]

                        taskService.complete(standardLevyFactoryVisitReportEntity.taskId, variables)
                        taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
                            ?.let { t ->
                                t.list()[0]
                                    ?.let { task ->
                                        task.assignee =
                                            "${loggedInUser.id ?: throw NullValueNotAllowedException(" invalid user id provided")}"  //set the assignee}"
                                        task.dueDate =
                                            standardLevyFactoryVisitReportEntity.scheduledVisitDate  //set the due date
                                        taskService.saveTask(task)
                                    }
                                    ?: KotlinLogging.logger { }.error("Task list empty for $PROCESS_DEFINITION_KEY ")


                            }
                            ?: KotlinLogging.logger { }.error("No task found for $PROCESS_DEFINITION_KEY ")
                        bpmnService.slAssignTask(
                            processInstance.processInstanceId,
                            "viewSchedule",
                            loggedInUser.id ?: throw NullValueNotAllowedException("invalid user id provided")
                        )
                        return ProcessInstanceResponseSite(
                            visitDetails.id.toString(),
                            processInstance.id,
                            processInstance.isEnded
                        )
                    }
                    ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${standardLevyFactoryVisitReportEntity.slProcessInstanceId} ")

            }
            ?: throw NullValueNotAllowedException("COMPANY NOT FOUND")


    }

    fun decisionOnSiteVisitSchedule(
        standardLevyFactoryVisitReportEntity: StandardLevyFactoryVisitReportEntity
    ): List<TaskDetailsBody> {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetailsEmail()
        standardLevyFactoryVisitReportEntity.accentTo?.let { variables["No"] = it }
        standardLevyFactoryVisitReportEntity.accentTo?.let { variables["Yes"] = it }
        standardLevyFactoryVisitReportEntity.id?.let { variables["visitID"] = it }
        standardLevyFactoryVisitReportEntity.manufacturerEntity?.let { variables["manufacturerEntity"] = it }
        standardLevyFactoryVisitReportEntity.scheduledVisitDate?.let { variables["scheduledVisitDate"] = it }


        if (variables["Yes"] == true) {
            val userID= companyProfileRepo.getContactPersonId(standardLevyFactoryVisitReportEntity.manufacturerEntity)
            var userList= companyStandardRepository.getUserEmail(loggedInUser.id)
            userList.forEach { item->
                val recipient= item.getUserEmail()
                val subject = "Site Visit"
                val messageBody= "Dear ${item.getFirstName()} ${item.getLastName()}, A schedule for site visit to ${standardLevyFactoryVisitReportEntity.companyName} has been done. NB. Auto Generated E-Mail From KEBS "
                if (recipient != null) {
                    notifications.sendEmail(recipient, subject, messageBody)
                }
            }

            var manufacturerList= companyStandardRepository.getUserEmail(userID)
            manufacturerList.forEach { item->
                val recipient= item.getUserEmail()
                val subject = "Site Visit"
                val messageBody= "Dear ${item.getFirstName()} ${item.getLastName()}, Your Firm,  ${standardLevyFactoryVisitReportEntity.companyName} has a scheduled site visit on ${standardLevyFactoryVisitReportEntity.scheduledVisitDate} by Standards Levy Officers From KEBS. NB. Auto Generated E-Mail From KEBS "
                if (recipient != null) {
                    notifications.sendEmail(recipient, subject, messageBody)
                }
            }

            standardLevyFactoryVisitReportRepo.findByIdOrNull(standardLevyFactoryVisitReportEntity.id)
                ?.let { entity ->

                    entity.apply {
                        scheduledVisitDate=standardLevyFactoryVisitReportEntity.scheduledVisitDate
                    }
                    standardLevyFactoryVisitReportRepo.save(entity)
                } ?: throw Exception("TASK NOT FOUND")

            companyProfileRepo.findByIdOrNull(standardLevyFactoryVisitReportEntity.manufacturerEntity)
                ?.let { companyProfileEntity ->
                    runtimeService.createProcessInstanceQuery()
                        .processInstanceId(standardLevyFactoryVisitReportEntity.slProcessInstanceId).list()
                        ?.let { l ->
                            val processInstance = l[0]

                            taskService.complete(standardLevyFactoryVisitReportEntity.taskId, variables)

                            taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
                                ?.let { t ->
                                    t.list()[0]
                                        ?.let { task ->
                                            task.assignee = "${
                                                loggedInUser.id?: throw NullValueNotAllowedException(
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
                                "Prepare Report on Visit",
                                loggedInUser.id
                                    ?: throw NullValueNotAllowedException("invalid user id provided")
                            )

                        }
                        ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${standardLevyFactoryVisitReportEntity.slProcessInstanceId} ")

                }
                ?: throw NullValueNotAllowedException("COMPANY NOT FOUND")
        } else if (variables["No"] == false) {

            taskService.complete(standardLevyFactoryVisitReportEntity.taskId, variables)
        }
        return getUserTasks()


    }

    private fun getTaskDetails(tasks: List<Task>): List<TaskDetailsBody> {
        val taskDetails: MutableList<TaskDetailsBody> = ArrayList()
        for (task in tasks) {
            val processVariables = taskService.getVariables(task.id)
            taskDetails.add(TaskDetailsBody(task.id, task.name,task.processInstanceId, processVariables))

        }
        return taskDetails
    }

    //Return task details for PL OFFICER
    fun getUserTasks(): List<TaskDetailsBody> {
        val tasks = taskService.createTaskQuery()
            .taskAssignee("${commonDaoServices.loggedInUserDetailsEmail().id ?: throw NullValueNotAllowedException(" invalid user id provided")}")
            .list()
        return getTaskDetails(tasks)
    }

    fun getCompletedTasks(): List<TaskDetailsBody> {


//        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_SL_PRINCIPAL_LEVY_OFFICER).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        val tasks = historyService.createHistoricTaskLogEntryQuery()
            .list()
        val tasksDetails = mutableListOf<TaskDetailsBody>()
        tasks.forEach { it ->
            val t = TaskDetailsBody(it.taskId, it.data,it.processInstanceId, mutableMapOf())
            tasksDetails.add(t)

        }

        return tasksDetails
    }

    //Return task details for PL OFFICER
    fun viewFeedBack(): List<TaskDetailsBody> {

//        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_SL_PRINCIPAL_LEVY_OFFICER).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        val tasks = taskService.createTaskQuery()
            .taskAssignee("${commonDaoServices.loggedInUserDetailsEmail().id ?: throw NullValueNotAllowedException(" invalid user id provided")}")
            .list()
        return getTaskDetails(tasks)
    }

    fun reportOnSiteVisitTest(standardLevyFactoryVisitReportEntity: StandardLevyFactoryVisitReportEntity) {
        val loggedInUser = commonDaoServices.loggedInUserDetailsEmail()
        val variables: MutableMap<String, Any> = java.util.HashMap()
        standardLevyFactoryVisitReportEntity.visitDate?.let { variables["visitDate"] = it }
        standardLevyFactoryVisitReportEntity.purpose?.let { variables["purpose"] = it }
        standardLevyFactoryVisitReportEntity.personMet?.let { variables["personMet"] = it }
        standardLevyFactoryVisitReportEntity.actionTaken?.let { variables["actionTaken"] = it }
        standardLevyFactoryVisitReportEntity.id?.let { variables["visitID"] = it }
        standardLevyFactoryVisitReportEntity.status?.let { variables["visitStatus"] = it }
        standardLevyFactoryVisitReportEntity.assigneeId?.let { variables["assigneeId"] = it }
        standardLevyFactoryVisitReportEntity.userType?.let { variables["userType"] = it }
        standardLevyFactoryVisitReportEntity.makeRemarks?.let { variables["makeRemarks"] = it }
        standardLevyFactoryVisitReportEntity.complianceStatus?.let { variables["complianceStatus"] = it }
        val userIntType = standardLevyFactoryVisitReportEntity.userType
        val plUserTypes = 61L
        val asManagerUserTypes = 62L
        val managerUserTypes = 63L
        val gson = Gson()
//        when (userIntType) {
//            plUserTypes -> {
//
//                KotlinLogging.logger { }.info { "Notification Report" }
//            }
//            asManagerUserTypes -> {
//                KotlinLogging.logger { }.info { "Assignment Report" }
//            }
//            managerUserTypes -> {
//                KotlinLogging.logger { }.info { "View Report" }
//            }
//        }


    }

    fun reportOnSiteVisit(standardLevyFactoryVisitReportEntity: StandardLevyFactoryVisitReportEntity): ProcessInstanceResponseValueSite {
        val loggedInUser = commonDaoServices.loggedInUserDetailsEmail()
        val variables: MutableMap<String, Any> = java.util.HashMap()
        standardLevyFactoryVisitReportEntity.visitDate?.let { variables["visitDate"] = it }
        standardLevyFactoryVisitReportEntity.purpose?.let { variables["purpose"] = it }
        standardLevyFactoryVisitReportEntity.personMet?.let { variables["personMet"] = it }
        standardLevyFactoryVisitReportEntity.actionTaken?.let { variables["actionTaken"] = it }
        standardLevyFactoryVisitReportEntity.id?.let { variables["visitID"] = it }
        standardLevyFactoryVisitReportEntity.status?.let { variables["visitStatus"] = it }
        standardLevyFactoryVisitReportEntity.assigneeId?.let { variables["assigneeId"] = it }
        standardLevyFactoryVisitReportEntity.userType?.let { variables["userType"] = it }
        standardLevyFactoryVisitReportEntity.makeRemarks?.let { variables["makeRemarks"] = it }
        standardLevyFactoryVisitReportEntity.complianceStatus?.let { variables["complianceStatus"] = it }
        val userIntType = standardLevyFactoryVisitReportEntity.userType
        val plUserTypes = 61L
        val asManagerUserTypes = 62L
        val managerUserTypes = 63L

        standardLevyFactoryVisitReportRepo.findByIdOrNull(standardLevyFactoryVisitReportEntity.id)
            ?.let { entity ->

                entity.apply {
                    visitDate = standardLevyFactoryVisitReportEntity.visitDate
                    purpose = standardLevyFactoryVisitReportEntity.purpose
                    personMet = standardLevyFactoryVisitReportEntity.personMet
                    actionTaken = standardLevyFactoryVisitReportEntity.actionTaken
                    makeRemarks = standardLevyFactoryVisitReportEntity.makeRemarks
                    status = 1
                    assigneeId = standardLevyFactoryVisitReportEntity.assigneeId
                    conductedBy = loggedInUser.id
                    complianceStatus = standardLevyFactoryVisitReportEntity.complianceStatus

                }
                standardLevyFactoryVisitReportRepo.save(entity)
            } ?: throw Exception("SCHEDULED VISIT NOT FOUND")


        companyProfileRepo.findByIdOrNull(standardLevyFactoryVisitReportEntity.manufacturerEntity)
            ?.let { companyProfileEntity ->
                runtimeService.createProcessInstanceQuery()
                    .processInstanceId(standardLevyFactoryVisitReportEntity.slProcessInstanceId).list()
                    ?.let { l ->
                        val processInstance = l[0]


                        taskService.complete(standardLevyFactoryVisitReportEntity.taskId, variables)

                        var userList= companyStandardRepository.getUserEmail(standardLevyFactoryVisitReportEntity.assigneeId)
                        userList.forEach { item->
                            val recipient= item.getUserEmail()
                            val subject = "Site Visit Report"
                            val messageBody= "Dear ${item.getFirstName()} ${item.getLastName()}, A report for site visit to,  ${standardLevyFactoryVisitReportEntity.companyName} has been prepared.Kindly Log into the KIMS to view. NB. Auto Generated E-Mail From KEBS "
                            if (recipient != null) {
                                notifications.sendEmail(recipient, subject, messageBody)
                            }
                        }

                        taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
                            ?.let { t ->
                                t.list()[0]
                                    ?.let { task ->
                                        task.assignee = "${
                                            standardLevyFactoryVisitReportEntity.assigneeId ?: throw NullValueNotAllowedException(
                                                " invalid user id provided"
                                            )
                                        }"  //set the assignee}"
                                        // task.dueDate = standardLevyFactoryVisitReportEntity.scheduledVisitDate  //set the due date
                                        taskService.saveTask(task)
                                    }
                                    ?: KotlinLogging.logger { }.error("Task list empty for $PROCESS_DEFINITION_KEY ")


                            }
                            ?: KotlinLogging.logger { }.error("No task found for $PROCESS_DEFINITION_KEY ")

//                        if(userIntType==true){
//                            bpmnService.slAssignTask(
//                                processInstance.processInstanceId,
//                                "Notification of Report",
//                                standardLevyFactoryVisitReportEntity.assigneeId
//                                    ?: throw NullValueNotAllowedException("invalid user id provided")
//                            )
//                        }else if(userIntType==false){
//                            bpmnService.slAssignTask(
//                                processInstance.processInstanceId,
//                                "View The Report",
//                                standardLevyFactoryVisitReportEntity.assigneeId
//                                    ?: throw NullValueNotAllowedException("invalid user id provided")
//                            )
//                        }


                        when (userIntType) {
                            plUserTypes -> {
                                bpmnService.slAssignTask(
                                    processInstance.processInstanceId,
                                    "Notification of Report",
                                    standardLevyFactoryVisitReportEntity.assigneeId
                                        ?: throw NullValueNotAllowedException("invalid user id provided")
                                )
                            }
                            asManagerUserTypes -> {
                                bpmnService.slAssignTask(
                                    processInstance.processInstanceId,
                                    "View The Report",
                                    standardLevyFactoryVisitReportEntity.assigneeId
                                        ?: throw NullValueNotAllowedException("invalid user id provided")
                                )
                            }
//                            managerUserTypes -> {
//                                bpmnService.slAssignTask(
//                                    processInstance.processInstanceId,
//                                    "View The Report",
//                                    standardLevyFactoryVisitReportEntity.assigneeId
//                                        ?: throw NullValueNotAllowedException("invalid user id provided")
//                                )
//                            }
                        }


                        return ProcessInstanceResponseValueSite(
                            standardLevyFactoryVisitReportEntity.id,
                            processInstance.id,
                            processInstance.isEnded
                        )
                    }
                    ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${standardLevyFactoryVisitReportEntity.slProcessInstanceId} ")

            }
            ?: throw NullValueNotAllowedException("COMPANY NOT FOUND")


    }

    // Upload PD document
    fun uploadSiteReport(
        uploads: SlVisitUploadsEntity,
        docFile: MultipartFile,
        doc: String,
        user: UsersEntity,
        DocDescription: String
    ): SlVisitUploadsEntity {

        with(uploads) {
//            filepath = docFile.path
            name = commonDaoServices.saveDocuments(docFile)
//            fileType = docFile.contentType
            fileType = docFile.contentType
            documentType = doc
            description = DocDescription
            document = docFile.bytes
            transactionDate = LocalDate.now()
            status = 1
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = LocalDateTime.now()
        }

        return slUploadsRepo.save(uploads)
    }

    //Return task details for Ast Manager
    fun getSiteReport(): List<TaskDetailsBody> {

        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_SL_Assistant_Manager)
            .processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    // View Report Document
    fun findUploadedReportFileBYId(visitId: Long): SlVisitUploadsEntity {
        return slUploadsRepo.findAllById(visitId)
            ?: throw ExpectedDataNotFound("No File found with the following [ id=$visitId]")
    }

    fun getVisitDocumentList(visitId: Long): List<SiteVisitListHolder> {
        return slUploadsRepo.findAllDocumentId(visitId)
    }

    fun decisionOnSiteReport(
        standardLevyFactoryVisitReportEntity: StandardLevyFactoryVisitReportEntity,
        standardLevySiteVisitRemarks: StandardLevySiteVisitRemarks
    ): List<TaskDetailsBody> {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetailsEmail()
        standardLevyFactoryVisitReportEntity.assistantManagerRemarks?.let { variables["assistantManagerRemarks"] = it }
        standardLevyFactoryVisitReportEntity.accentTo?.let { variables["No"] = it }
        standardLevyFactoryVisitReportEntity.accentTo?.let { variables["Yes"] = it }
        standardLevyFactoryVisitReportEntity.assigneeId?.let { variables["assigneeId"] = it }
        variables["status"] = 1
        val approverFname=loggedInUser.firstName
        val approverLname=loggedInUser.lastName
        val approveName= "$approverFname  $approverLname"
        standardLevySiteVisitRemarks.siteVisitId= standardLevySiteVisitRemarks.siteVisitId
        standardLevySiteVisitRemarks.remarks= standardLevySiteVisitRemarks.remarks
        standardLevySiteVisitRemarks.status = standardLevySiteVisitRemarks.status
        standardLevySiteVisitRemarks.role = standardLevySiteVisitRemarks.role
        standardLevySiteVisitRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
        standardLevySiteVisitRemarks.remarkBy = approveName

//        val gson = Gson()
//         KotlinLogging.logger { }.info { "Manufacturer" + gson.toJson(standardLevyFactoryVisitReportEntity) }
        variables["approvalStatus"]= "Approved by $approverFname  $approverLname"
        loggedInUser.id?.let { variables["levelOneId"] = it }
        if (variables["Yes"] == true) {

            var userList= companyStandardRepository.getUserEmail(standardLevyFactoryVisitReportEntity.assigneeId)
            userList.forEach { item->
                val recipient= item.getUserEmail()
                val subject = "Site Visit Report Approved"
                val messageBody= "Dear ${item.getFirstName()} ${item.getLastName()}, Report for site visit to,  ${standardLevyFactoryVisitReportEntity.companyName} has been approved. NB. Auto Generated E-Mail From KEBS "
                if (recipient != null) {
                    notifications.sendEmail(recipient, subject, messageBody)
                }
            }

            variables["approvalStatus"]= "Approved by $approverFname  $approverLname"
            variables["approvalStatusId"]=1
            standardLevyFactoryVisitReportRepo.findByIdOrNull(standardLevyFactoryVisitReportEntity.id)
                ?.let { entity ->

                    entity.apply {
                        assistantManagerRemarks = standardLevyFactoryVisitReportEntity.assistantManagerRemarks
                        status = 1
                        assigneeId = standardLevyFactoryVisitReportEntity.assigneeId
                        accentTo = true
                    }
                    standardLevyFactoryVisitReportRepo.save(entity)

                    standardLevySiteVisitRemarksRepository.save(standardLevySiteVisitRemarks)
                } ?: throw Exception("TASK NOT FOUND")

            companyProfileRepo.findByIdOrNull(standardLevyFactoryVisitReportEntity.manufacturerEntity)
                ?.let { companyProfileEntity ->
                    runtimeService.createProcessInstanceQuery()
                        .processInstanceId(standardLevyFactoryVisitReportEntity.slProcessInstanceId).list()
                        ?.let { l ->
                            val processInstance = l[0]

                            taskService.complete(standardLevyFactoryVisitReportEntity.taskId, variables)

                            taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
                                ?.let { t ->
                                    t.list()[0]
                                        ?.let { task ->
                                            task.assignee = "${
                                                standardLevyFactoryVisitReportEntity.assigneeId ?: throw NullValueNotAllowedException(
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
                                "View Report",
                                standardLevyFactoryVisitReportEntity?.assigneeId
                                    ?: throw NullValueNotAllowedException("invalid user id provided")
                            )

                        }
                        ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${standardLevyFactoryVisitReportEntity.slProcessInstanceId} ")

                }
                ?: throw NullValueNotAllowedException("COMPANY NOT FOUND")
        } else if (variables["No"] == false) {

            variables["rejectStatus"]= "Rejected by $approverFname  $approverLname"
            variables["approvalStatusId"]=0
            standardLevyFactoryVisitReportRepo.findByIdOrNull(standardLevyFactoryVisitReportEntity.id)
                ?.let { entity ->

                    entity.apply {
                        assistantManagerRemarks = standardLevyFactoryVisitReportEntity.assistantManagerRemarks
                        status = 1
                        assigneeId = standardLevyFactoryVisitReportEntity.assigneeId
                        accentTo = false
                    }
                    standardLevyFactoryVisitReportRepo.save(entity)

                    standardLevySiteVisitRemarksRepository.save(standardLevySiteVisitRemarks)
                } ?: throw Exception("TASK NOT FOUND")
            //val userEmail = standardLevyFactoryVisitReportEntity.assigneeId?.let { commonDaoServices.getUserEmail(it) };
            val recipient= standardLevyFactoryVisitReportEntity.assigneeId?.let { commonDaoServices.getUserEmail(it) };
            val subject = "Site Visit Report Rejected  "
            val messageBody= "Site visit report has been rejected by  "+ commonDaoServices.loggedInUserDetailsEmail().userName+".Log in to KIMS to make recommended changes."
            if (recipient != null) {
                notifications.sendEmail(recipient, subject, messageBody)
            }


            companyProfileRepo.findByIdOrNull(standardLevyFactoryVisitReportEntity.manufacturerEntity)
                ?.let { companyProfileEntity ->
                    runtimeService.createProcessInstanceQuery()
                        .processInstanceId(standardLevyFactoryVisitReportEntity.slProcessInstanceId).list()
                        ?.let { l ->
                            val processInstance = l[0]

                            taskService.complete(standardLevyFactoryVisitReportEntity.taskId, variables)

                            taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
                                ?.let { t ->
                                    t.list()[0]
                                        ?.let { task ->
                                            task.assignee = "${
                                                standardLevyFactoryVisitReportEntity.assigneeId ?: throw NullValueNotAllowedException(
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
                                "Prepare Report on Visit",
                                standardLevyFactoryVisitReportEntity?.assigneeId
                                    ?: throw NullValueNotAllowedException("invalid user id provided")
                            )


                        }
                        ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${standardLevyFactoryVisitReportEntity.slProcessInstanceId} ")

                }
                ?: throw NullValueNotAllowedException("COMPANY NOT FOUND")
        }
        return getUserTasks()


    }

    //Return task details for  Manager
    fun getSiteReportLevelTwo(): List<TaskDetailsBody> {

        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_SL_Manager)
            .processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    fun decisionOnSiteReportLevelTwo(standardLevyFactoryVisitReportEntity: StandardLevyFactoryVisitReportEntity,
                                     standardLevySiteVisitRemarks: StandardLevySiteVisitRemarks): List<TaskDetailsBody> {
        val variables: MutableMap<String, Any> = mutableMapOf()
        val loggedInUser = commonDaoServices.loggedInUserDetailsEmail()
        standardLevyFactoryVisitReportEntity.accentTo?.let { variables["Yes"] = it }
        standardLevyFactoryVisitReportEntity.accentTo?.let { variables["No"] = it }
        standardLevyFactoryVisitReportEntity.cheifManagerRemarks?.let { variables["cheifManagerRemarks"] = it }
        variables["visitStatus"] = 2
        standardLevyFactoryVisitReportEntity.assigneeId?.let { variables["assigneeId"] = it }
        val userIntType = standardLevyFactoryVisitReportEntity.userType
        val userIntTypes = userIntType.toString()
        val approverFname=loggedInUser.firstName
        val approverLname=loggedInUser.lastName
        val approveName= "$approverFname  $approverLname"
        standardLevySiteVisitRemarks.siteVisitId= standardLevySiteVisitRemarks.siteVisitId
        standardLevySiteVisitRemarks.remarks= standardLevySiteVisitRemarks.remarks
        standardLevySiteVisitRemarks.status = standardLevySiteVisitRemarks.status
        standardLevySiteVisitRemarks.role = standardLevySiteVisitRemarks.role
        standardLevySiteVisitRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
        standardLevySiteVisitRemarks.remarkBy = approveName

        if (variables["Yes"] == true) {
            val recipient= standardLevyFactoryVisitReportEntity.assigneeId?.let { commonDaoServices.getUserEmail(it) };
            val subject = "Site Visit Report Approved  "
            val messageBody= "Site visit report has been approved by  "+ commonDaoServices.loggedInUserDetailsEmail().userName+"."
            if (recipient != null) {
                notifications.sendEmail(recipient, subject, messageBody)
            }
            variables["approvalStatusLevelTwo"]= "Approved by $approverFname  $approverLname"
            variables["approvalStatusId"]=1
            standardLevyFactoryVisitReportRepo.findByIdOrNull(standardLevyFactoryVisitReportEntity.id)
                ?.let { entity ->

                    entity.apply {
                        cheifManagerRemarks = standardLevyFactoryVisitReportEntity.cheifManagerRemarks
                        status = 2
                        accentTo = true
                    }
                    standardLevyFactoryVisitReportRepo.save(entity)
                    standardLevySiteVisitRemarksRepository.save(standardLevySiteVisitRemarks)
                } ?: throw Exception("TASK NOT FOUND")

            companyProfileRepo.findByIdOrNull(standardLevyFactoryVisitReportEntity.manufacturerEntity)
                ?.let { companyProfileEntity ->
                    runtimeService.createProcessInstanceQuery()
                        .processInstanceId(standardLevyFactoryVisitReportEntity.slProcessInstanceId).list()
                        ?.let { l ->
                            val processInstance = l[0]

                            taskService.complete(standardLevyFactoryVisitReportEntity.taskId, variables)

                            taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
                                ?.let { t ->
                                    t.list()[0]
                                        ?.let { task ->
                                            task.assignee = "${
                                                standardLevyFactoryVisitReportEntity.assigneeId ?: throw NullValueNotAllowedException(
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
                                "Draft Feedback and share with manufacturer",
                                standardLevyFactoryVisitReportEntity?.assigneeId
                                    ?: throw NullValueNotAllowedException("invalid user id provided")
                            )


                        }
                        ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${standardLevyFactoryVisitReportEntity.slProcessInstanceId} ")

                }
                ?: throw NullValueNotAllowedException("COMPANY NOT FOUND")
        } else if (variables["No"] == false) {
            variables["rejectStatusLevelTwo"]= "Rejected by $approverFname  $approverLname"
            variables["approvalStatusId"]=0
            standardLevyFactoryVisitReportRepo.findByIdOrNull(standardLevyFactoryVisitReportEntity.id)
                ?.let { entity ->

                    entity.apply {
                        cheifManagerRemarks = standardLevyFactoryVisitReportEntity.cheifManagerRemarks
                        status = 2
                        accentTo = false
                    }
                    standardLevyFactoryVisitReportRepo.save(entity)
                    standardLevySiteVisitRemarksRepository.save(standardLevySiteVisitRemarks)
                } ?: throw Exception("TASK NOT FOUND")

            //val userEmail = standardLevyFactoryVisitReportEntity.assigneeId?.let { commonDaoServices.getUserEmail(it) };
            val recipient= standardLevyFactoryVisitReportEntity.assigneeId?.let { commonDaoServices.getUserEmail(it) };
            val subject = "Report Rejected  "
            val messageBody= "Site visit report has been rejected by  "+ commonDaoServices.loggedInUserDetailsEmail().userName+".Log in to KIMS to make recommended changes."
            if (recipient != null) {
                notifications.sendEmail(recipient, subject, messageBody)
            }

            companyProfileRepo.findByIdOrNull(standardLevyFactoryVisitReportEntity.manufacturerEntity)
                ?.let { companyProfileEntity ->
                    runtimeService.createProcessInstanceQuery()
                        .processInstanceId(standardLevyFactoryVisitReportEntity.slProcessInstanceId).list()
                        ?.let { l ->
                            val processInstance = l[0]

                            taskService.complete(standardLevyFactoryVisitReportEntity.taskId, variables)

                            taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
                                ?.let { t ->
                                    t.list()[0]
                                        ?.let { task ->
                                            task.assignee = "${
                                                standardLevyFactoryVisitReportEntity.assigneeId ?: throw NullValueNotAllowedException(
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

                            if (userIntTypes.equals(61)) {

                                bpmnService.slAssignTask(
                                    processInstance.processInstanceId,
                                    "Notification of Report",
                                    standardLevyFactoryVisitReportEntity?.assigneeId
                                        ?: throw NullValueNotAllowedException("invalid user id provided")
                                )
                            } else if (userIntTypes.equals(62)) {
                                bpmnService.slAssignTask(
                                    processInstance.processInstanceId,
                                    "Prepare Report on Visit",
                                    standardLevyFactoryVisitReportEntity?.assigneeId
                                        ?: throw NullValueNotAllowedException("invalid user id provided")
                                )
                            } else if (userIntTypes.equals(63)) {
                                bpmnService.slAssignTask(
                                    processInstance.processInstanceId,
                                    "Prepare Report on Visit",
                                    standardLevyFactoryVisitReportEntity.assigneeId
                                        ?: throw NullValueNotAllowedException("invalid user id provided")
                                )
                            } else {
                                bpmnService.slAssignTask(
                                    processInstance.processInstanceId,
                                    "Prepare Report on Visit",
                                    standardLevyFactoryVisitReportEntity.assigneeId
                                        ?: throw NullValueNotAllowedException("invalid user id provided")
                                )
                            }
                        }
                        ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${standardLevyFactoryVisitReportEntity.slProcessInstanceId} ")

                }
                ?: throw NullValueNotAllowedException("COMPANY NOT FOUND")
        }

        return getUserTasks()
    }

    fun siteVisitReportFeedbackTest(
        standardLevyFactoryVisitReportEntity: StandardLevyFactoryVisitReportEntity
    ): String {
        val loggedInUser = commonDaoServices.loggedInUserDetailsEmail()
        val variables: MutableMap<String, Any> = java.util.HashMap()
        standardLevyFactoryVisitReportEntity.officersFeedback?.let { variables["officersFeedback"] = it }
        standardLevyFactoryVisitReportEntity.id?.let { variables["visitID"] = it }
        standardLevyFactoryVisitReportEntity.status?.let { variables["visitStatus"] = it }
        standardLevyFactoryVisitReportEntity.assigneeId?.let { variables["assigneeId"] = it }

        standardLevyFactoryVisitReportRepo.findByIdOrNull(standardLevyFactoryVisitReportEntity.id)
            ?.let { entity ->

                entity.apply {
                    officersFeedback = standardLevyFactoryVisitReportEntity.officersFeedback
                    assigneeId = standardLevyFactoryVisitReportEntity.assigneeId
                    status = 3
                    slProcessStatus = 1

                }
                standardLevyFactoryVisitReportRepo.save(entity)
            } ?: throw Exception("SCHEDULED VISIT NOT FOUND")

        return "Saved"
    }
    fun siteVisitReportFeedbacksTest(
        standardLevyFactoryVisitReportEntity: StandardLevyFactoryVisitReportEntity
    ): String {
        val loggedInUser = commonDaoServices.loggedInUserDetailsEmail()
        val variables: MutableMap<String, Any> = java.util.HashMap()
        standardLevyFactoryVisitReportEntity.officersFeedback?.let { variables["officersFeedback"] = it }
        standardLevyFactoryVisitReportEntity.id?.let { variables["visitID"] = it }
        standardLevyFactoryVisitReportEntity.status?.let { variables["visitStatus"] = it }
        standardLevyFactoryVisitReportEntity.assigneeId?.let { variables["assigneeId"] = it }
        val complianceStatus =
            standardLevyFactoryVisitReportRepo.findByIdOrNull(standardLevyFactoryVisitReportEntity.id)?.complianceStatus
        val companyName = companyRepo.getComName(standardLevyFactoryVisitReportEntity.assigneeId)
        val registrationNumber = companyRepo.getRegNo(standardLevyFactoryVisitReportEntity.assigneeId)
        val kraPin = companyRepo.getKraPin(standardLevyFactoryVisitReportEntity.assigneeId)
        val conductedById =
            standardLevyFactoryVisitReportRepo.findByIdOrNull(standardLevyFactoryVisitReportEntity.id)?.conductedBy
        val conductedByFName = iUserRepository.findByIdOrNull(conductedById)?.firstName
        val conductedByLName = iUserRepository.findByIdOrNull(conductedById)?.lastName
        val dateConducted =
            standardLevyFactoryVisitReportRepo.findByIdOrNull(standardLevyFactoryVisitReportEntity.id)?.visitDate
        var emailBody = ""
        var thisMail=""
        standardLevyFactoryVisitReportRepo.findByIdOrNull(standardLevyFactoryVisitReportEntity.id)
            ?.let { entity ->
                entity.apply {
                    officersFeedback = standardLevyFactoryVisitReportEntity.officersFeedback
                    assigneeId = standardLevyFactoryVisitReportEntity.assigneeId
                    status = 3
                    slProcessStatus = 1

                }
                standardLevyFactoryVisitReportRepo.save(entity)
            } ?: throw Exception("SCHEDULED VISIT NOT FOUND")
        if (complianceStatus != null) {
            if (complianceStatus =="1".toLong()) {
                emailBody =
                    "Following a visit  ${companyName},Registration Number ${registrationNumber},and  KRA pin $kraPin on date ${dateConducted} by ${conductedByFName},${conductedByLName}. $companyName  was found to be in compliance with the Levy Order of 1st July 1990 and Standards Levy (amendment) of 1999. Standards Levy is for the development and promotion of Standardization, Metrology and conformity assessment services.Thank you for the continued cooperation, for any further clarifications do not hesitate to contact us through standardslevy@kebs.org"

                thisMail="Saved"
            } else if (complianceStatus=="0".toLong()) {
                emailBody =
                    "Following a visit  ${companyName}, registration number ${registrationNumber}, and  KRA pin ${kraPin} on date ${dateConducted} by  ${conductedByFName},${conductedByLName}.\n" +
                            "\n" +
                            "$companyName was found NOT to be in compliance with the Levy Order of 1st July 1990 and Standards Levy (amendment) of 1999. \n" +
                            "\n" +
                            "The standards levy is payable at the rate of 0.2% of your monthly turnover excluding VAT and discounts where applicable, subject to a minimum of KSh. 1,000 per month and a maximum of KSh. 400,000 per annum. Failure to pay the levy attracts a penalty at a rate of 5% per month cumulatively. \n" +
                            " \n" +
                            "In line with the above, you are required to remit all outstanding arrears of XXXXX and penalties of XXXX through the ITAX system to avoid further accrual of penalties. \n" +
                            "\n" +
                            "For any clarification, do not hesitate to contact us through standardslevy@kebs.org\n" +
                            "\n"
            }
        }
        val gson = Gson()
        KotlinLogging.logger { }.info { "Compliance" + gson.toJson(complianceStatus) }
        KotlinLogging.logger { }.info { "Company Name" + gson.toJson(companyName) }
        KotlinLogging.logger { }.info { "Reg No" + gson.toJson(registrationNumber) }
        KotlinLogging.logger { }.info { "Kra Pin" + gson.toJson(kraPin) }
        KotlinLogging.logger { }.info { "Email" + gson.toJson(emailBody) }
        KotlinLogging.logger { }.info { "Conducted By" + gson.toJson(conductedById) }
        KotlinLogging.logger { }.info { "Conducted By Name" + gson.toJson(conductedByFName) }
        KotlinLogging.logger { }.info { "Date Conducted" + gson.toJson(dateConducted) }
        KotlinLogging.logger { }.info { "This Check" + gson.toJson(thisMail) }

        val recipient = standardLevyFactoryVisitReportEntity.assigneeId?.let { commonDaoServices.getUserEmail(it) };
        val subject = "Site Visit Report"
        if (recipient != null) {
            notifications.sendEmail(recipient, subject, emailBody)
        }
        return "Saved"
    }

    fun siteVisitReportFeedback(
        standardLevyFactoryVisitReportEntity: StandardLevyFactoryVisitReportEntity
    ): ProcessInstanceResponseValueSite {
        val loggedInUser = commonDaoServices.loggedInUserDetailsEmail()
        val variables: MutableMap<String, Any> = java.util.HashMap()
        standardLevyFactoryVisitReportEntity.officersFeedback?.let { variables["officersFeedback"] = it }
        standardLevyFactoryVisitReportEntity.id?.let { variables["visitID"] = it }
        standardLevyFactoryVisitReportEntity.status?.let { variables["visitStatus"] = it }
        standardLevyFactoryVisitReportEntity.assigneeId?.let { variables["assigneeId"] = it }
        val complianceStatus =
            standardLevyFactoryVisitReportRepo.findByIdOrNull(standardLevyFactoryVisitReportEntity.id)?.complianceStatus
        val companyName = companyRepo.getComName(standardLevyFactoryVisitReportEntity.assigneeId)
        val registrationNumber = companyRepo.getRegNo(standardLevyFactoryVisitReportEntity.assigneeId)
        val kraPin = companyRepo.getKraPin(standardLevyFactoryVisitReportEntity.assigneeId)
        val conductedById= standardLevyFactoryVisitReportRepo.findByIdOrNull(standardLevyFactoryVisitReportEntity.id)?.conductedBy
        val conductedByFName= iUserRepository.findByIdOrNull(conductedById)?.firstName
        val conductedByLName= iUserRepository.findByIdOrNull(conductedById)?.lastName
        val dateConducted = standardLevyFactoryVisitReportRepo.findByIdOrNull(standardLevyFactoryVisitReportEntity.id)?.visitDate
        var emailBody =""
        standardLevyFactoryVisitReportRepo.findByIdOrNull(standardLevyFactoryVisitReportEntity.id)
            ?.let { entity ->
                entity.apply {
                    officersFeedback = standardLevyFactoryVisitReportEntity.officersFeedback
                    assigneeId = standardLevyFactoryVisitReportEntity.assigneeId
                    status = 3
                    slProcessStatus = 1

                }
                standardLevyFactoryVisitReportRepo.save(entity)
            } ?: throw Exception("SCHEDULED VISIT NOT FOUND")
        if (complianceStatus != null) {
            if (complianceStatus =="1".toLong()) {
                 emailBody="Following a visit  ${companyName},Registration Number ${registrationNumber},and  KRA pin $kraPin on date ${dateConducted} by ${conductedByFName},${conductedByLName}.\n" +
                         "\n" +
                         "$companyName  was found to be in compliance with the Levy Order of 1st July 1990 and Standards Levy (amendment) of 1999.\n" +
                         "\n" +
                         " Standards Levy is for the development and promotion of Standardization, Metrology and conformity assessment services.\n" +
                         "\n" +
                         "Thank you for the continued cooperation, for any further clarifications do not hesitate to contact us through standardslevy@kebs.org"

            }else if(complianceStatus =="0".toLong()){
                 emailBody="Following a visit  ${companyName}, registration number ${registrationNumber}, and  KRA pin ${kraPin} on date ${dateConducted} by  ${conductedByFName},${conductedByLName}.\n" +
                        "\n" +
                        "$companyName was found NOT to be in compliance with the Levy Order of 1st July 1990 and Standards Levy (amendment) of 1999. \n" +
                        "\n" +
                        "The standards levy is payable at the rate of 0.2% of your monthly turnover excluding VAT and discounts where applicable, subject to a minimum of KSh. 1,000 per month and a maximum of KSh. 400,000 per annum.\n" +
                         "\n" +
                         " Failure to pay the levy attracts a penalty at a rate of 5% per month cumulatively. \n" +
                        " \n" +
                        "In line with the above, you are required to remit all outstanding arrears of XXXXX and penalties of XXXX through the ITAX system to avoid further accrual of penalties. \n" +
                        "\n" +
                        "For any clarification, do not hesitate to contact us through standardslevy@kebs.org\n" +
                        "\n"
            }
        }

        val recipient = standardLevyFactoryVisitReportEntity.assigneeId?.let { commonDaoServices.getUserEmail(it) };
        val subject = "Site Visit Report"
        if (recipient != null) {
            notifications.sendEmail(recipient, subject, emailBody)
        }

        companyProfileRepo.findByIdOrNull(standardLevyFactoryVisitReportEntity.manufacturerEntity)
            ?.let { comEntity ->
                comEntity.apply {
                    assignedTo = 0
                    assignStatus = 0
                }
                companyProfileRepo.save(comEntity)
                runtimeService.createProcessInstanceQuery()
                    .processInstanceId(standardLevyFactoryVisitReportEntity.slProcessInstanceId).list()
                    ?.let { l ->
                        val processInstance = l[0]
                        taskService.complete(standardLevyFactoryVisitReportEntity.taskId, variables)

                        taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId)
                            ?.let { t ->
                                t.list()[0]
                                    ?.let { task ->
                                        task.assignee = "${
                                            standardLevyFactoryVisitReportEntity.assigneeId ?: throw NullValueNotAllowedException(
                                                " invalid user id provided"
                                            )
                                        }"  //set the assignee}"
                                        //task.dueDate = standardLevyFactoryVisitReportEntity.scheduledVisitDate  //set the due date
                                        taskService.saveTask(task)
                                    }
                                    ?: KotlinLogging.logger { }.error("Task list empty for $PROCESS_DEFINITION_KEY ")


                            }
                            ?: KotlinLogging.logger { }.error("No task found for $PROCESS_DEFINITION_KEY ")
                        bpmnService.slAssignTask(
                            processInstance.processInstanceId,
                            "Receive Feedback",
                            standardLevyFactoryVisitReportEntity.assigneeId
                                ?: throw NullValueNotAllowedException("invalid user id provided")
                        )
                        return ProcessInstanceResponseValueSite(
                            standardLevyFactoryVisitReportEntity.id,
                            processInstance.id,
                            processInstance.isEnded
                        )
                    }
                    ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${standardLevyFactoryVisitReportEntity.slProcessInstanceId} ")

            }
            ?: throw NullValueNotAllowedException("COMPANY NOT FOUND")


    }

    //Return task details for  Manufacturer
    fun getSiteFeedback(): List<TaskDetailsBody> {

        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_Manufacturer)
            .processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    fun getManufacturerList(): MutableList<ManufactureListHolder> {
        return companyProfileRepo.getManufacturerList()
    }

    fun getMnCompleteTask(assignedTo: Long): MutableList<CompanyProfileEntity> {
        return companyProfileRepo.getMnCompleteTask(assignedTo)
    }

    fun getMnPendingTask(assignedTo: Long): MutableList<CompanyProfileEntity> {
        return companyProfileRepo.getMnPendingTask(assignedTo)
    }

    fun getPlList(): List<UserDetailHolder> {
        return userListRepository.getPlList()
    }

    fun getSlLvTwoList(): List<UserDetailHolder> {
        return userListRepository.getSlLvTwoList()
    }

    fun getSlUsers(): List<UserDetailHolder> {
        return userListRepository.getSlLvThreeList()
    }

    fun getApproveLevelOne(): List<UserDetailHolder> {
        return userListRepository.getApproveLevelOne()
    }

    fun getApproveLevelTwo(): List<UserDetailHolder> {
        return userListRepository.getApproveLevelTwo()
    }

    fun getApproveLevelThree(): List<UserDetailHolder> {
        return userListRepository.getApproveLevelThree()
    }

    fun getAssignLevelOne(): List<UserDetailHolder> {
        return userListRepository.getAssignLevelOne()
    }

    fun getAssignLevelTwo(): List<UserDetailHolder> {
        return userListRepository.getAssignLevelTwo()
    }

    fun getAssignLevelThree(): List<UserDetailHolder> {
        return userListRepository.getAssignLevelThree()
    }

    fun getSlLoggedIn(): UserTypeHolder {
        commonDaoServices.loggedInUserDetailsEmail().id?.let { id ->
            return usersEntityRepository.getSlLoggedById(id)
        } ?: throw NullValueNotAllowedException("User Not Found")
    }

    //    fun getRoleByUserId(): MutableList<UserRoleAssignmentsEntity>? {
//        val taskDetails: MutableList<UserRoleAssignmentsEntity> = ArrayList()
//        commonDaoServices.loggedInUserDetails().id?.let { id ->
//            return iUserRoleAssignmentsRepository.getRoleByUserId(id)
//        }?: throw NullValueNotAllowedException ("Role Not Found")
//    }
//
    fun getRoles(): List<UserRoleHolder> {
        commonDaoServices.loggedInUserDetailsEmail().id?.let { id ->
            return iUserRoleAssignmentsRepository.getRoleByUserId(id)
        } ?: throw NullValueNotAllowedException("Role Not Found")
    }

//    fun getNotificationFormDetails(): LevyCustomResponse?{
//        val result = LevyCustomResponse()
//        try {
//            commonDaoServices.loggedInUserDetailsEmail().id
//                ?.let { id ->
//                    companyProfileRepo.getManufactureId(id)
//                        .let {
//                            stdLevyNotificationFormRepository.findTopByManufactureIdOrderByIdDesc(it)
//                                ?.let {
//                                    result.payload= stdLevyNotificationFormRepository.getNotificationFormDetails(it)
//                                }
//
//                        }
//                } ?: throw NullValueNotAllowedException("Form Not Found")
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.debug(e.message, e)
//            KotlinLogging.logger { }.error(e.message, e)
//            result.apply {
//                response = e.message
//            }
//        }
//
//        return result
//    }

    fun getNotificationFormDetails(): NotificationFormDetailsHolder{

        commonDaoServices.loggedInUserDetailsEmail().id
            ?.let { id ->
                companyProfileRepo.getManufactureId(id)
                    .let {
                        stdLevyNotificationFormRepository.findTopByManufactureIdOrderByIdDesc(it)
                            ?.let {
                                return stdLevyNotificationFormRepository.getNotificationFormDetails(it)
                            }

                    }
            } ?: throw ExpectedDataNotFound("User Not Found")

    }

    fun getBranchName(): BranchNameHolder{
        commonDaoServices.loggedInUserDetailsEmail().id
            ?.let { id ->
                companyProfileRepo.getManufactureId(id)
                    .let {
                        manufacturePlantRepository.findTopByManufactureIdOrderByIdDesc(it)
                            ?.let {
                                return manufacturePlantRepository.findBranchName(it)
                            }

                    }
            } ?: throw ExpectedDataNotFound("Branch Not Found")

    }


    fun getSlForm(): ResponseNotification {

        commonDaoServices.loggedInUserDetailsEmail().id
            ?.let { id ->
                companyProfileRepo.getManufactureId(id)
                    .let {
                        it?.let { it1 ->
                            stdLevyNotificationFormRepository.countByManufacturerId(it1)
                                ?.let {

                                    //Manufacturer
                                    return ResponseNotification(1)
                                }
                        }
                            ?: run {
                                //Contractor
                                return ResponseNotification(0)
                            }
                    }

            }
            ?: return ResponseNotification(0)
    }

    fun getSLNotificationStatus(): ResponseNotification {

        commonDaoServices.loggedInUserDetailsEmail().id
            ?.let { id ->
                companyProfileRepo.getManufactureId(id)
                    .let {
                        stdLevyNotificationFormRepository.findTopByManufactureIdOrderByIdDesc(it)
                            ?.let {

                                //Manufacturer
                                return ResponseNotification(1)
                            }
                            ?: run {
                                //Contractor
                                return ResponseNotification(0)
                            }
                    }

            }
            ?: return ResponseNotification(0)
    }


    fun getIfRecordExists(): Boolean {
        val userId = commonDaoServices.loggedInUserDetailsEmail().id
        val gson = Gson()
        KotlinLogging.logger { }.info { "User ID" + gson.toJson(userId) }
        val companyId = companyProfileRepo.getManufactureId(userId) ?: throw ExpectedDataNotFound("NO Company Found")
        KotlinLogging.logger { }.info { "Company ID" + gson.toJson(companyId) }
        //KotlinLogging.logger { }.info { "User ID" + gson.toJson(userId) }
        stdLevyNotificationFormRepository.findTopByManufactureIdOrderByIdDesc(companyId)?.let {
            return true
        }
            ?: return false

    }

//    fun getManufacturerStatuss(userID:Long): BusinessTypeHolder {
//        val businessNature = companyProfileRepo.getBusinessNature(userID)
//        return iBusinessNatureRepository.getManufacturerStatus(businessNature)
//    }
    fun getVerificationStatus(): Int {
        return commonDaoServices.loggedInUserDetailsEmail().emailActivationStatus
    }

    fun getManufacturerStatus(): BusinessTypeHolder{
        commonDaoServices.loggedInUserDetailsEmail().id
            ?.let { id ->
                companyProfileRepo.getBusinessNature(id)
                    .let {
                    return  iBusinessNatureRepository.getManufacturerStatus(it)

                    }
            } ?: throw NullValueNotAllowedException("User Not Found")

    }

//    fun getOperationStatus(): ManufacturerStatusHolder{
//        commonDaoServices.loggedInUserDetailsEmail().id
//            ?.let { id ->
//             return   companyProfileRepo.getOperationStatus(id)
//
//            } ?: throw NullValueNotAllowedException("User Not Found")
//
//    }


    fun getSlNotificationFormDetails(manufactureId: Long): NotificationFormDetailsHolder {
        stdLevyNotificationFormRepository.findTopByManufactureIdOrderByIdDesc(manufactureId)
            ?.let {
                return stdLevyNotificationFormRepository.getSlNotificationFormDetails(it)
            }
            ?: throw ExpectedDataNotFound("No Data Found")
    }


    fun getCompanyEditedDetails(manufactureId: Long): CompanyProfileEditEntity {
        return companyProfileEditEntityRepository.findFirstByManufactureIdOrderByIdDesc(manufactureId)
            ?: throw ExpectedDataNotFound("No Data Found")
    }



    fun getCompanyDirectors(): List<DirectorListHolder>? {
        commonDaoServices.loggedInUserDetailsEmail().id
            ?.let { id ->
                companyProfileRepo.getManufactureId(id)
                    .let {

                        return iCompanyProfileDirectorsRepository.getCompanyDirectors(it)


                    }

            }?: throw ExpectedDataNotFound("No Data Found")

    }


    fun getSiteVisitRemarks(siteVisitId: Long): List<StandardLevySiteVisitRemarks> {
        standardLevySiteVisitRemarksRepository.findAllBySiteVisitIdOrderByIdDesc(siteVisitId)?.let {
          return it
        }
            ?: throw ExpectedDataNotFound("No Data Found")
    }

    fun getCompanyProcessId(manufactureId: Long): CompanyProfileEditEntity {
        return companyProfileEditEntityRepository.findStatusByManufactureId(manufactureId)
            ?: throw ExpectedDataNotFound("No Data Found")
    }



    fun getCompleteTasks(): List<CompleteTasksDetailHolder> {
        return standardLevyFactoryVisitReportRepo.getCompleteTasks()
    }



    fun closeTask(taskId: String) {
        taskService.complete(taskId)
        taskService.deleteTask(taskId, true)

    }

    fun closeProcess(taskId: String) {
        runtimeService.deleteProcessInstance(taskId, "cleaning")
    }

    fun getLevyPayments(): List<LevyPayments> {
        return companyProfileRepo.getLevyPayments().distinct()
    }



    fun suspendCompanyOperations(
        standardLevyOperationsSuspension: StandardLevyOperationsSuspension
    ): StandardLevyOperationsSuspension {
        standardLevyOperationsSuspension.companyId= standardLevyOperationsSuspension.companyId
        standardLevyOperationsSuspension.reason = standardLevyOperationsSuspension.reason
        standardLevyOperationsSuspension.dateOfSuspension= standardLevyOperationsSuspension.dateOfSuspension
        standardLevyOperationsSuspension.status= 0

        return standardLevyOperationsSuspensionRepository.save(standardLevyOperationsSuspension)
    }

    fun resumeCompanyOperations(
        standardLevyOperationsSuspension: StandardLevyOperationsSuspension
    ): StandardLevyOperationsSuspension {
        standardLevyOperationsSuspension.companyId= standardLevyOperationsSuspension.companyId
        standardLevyOperationsSuspension.reason = standardLevyOperationsSuspension.reason
        standardLevyOperationsSuspension.dateOfSuspension= standardLevyOperationsSuspension.dateOfSuspension
        standardLevyOperationsSuspension.status= 2

        return standardLevyOperationsSuspensionRepository.save(standardLevyOperationsSuspension)
    }

    fun getCompanySuspensionRequest(): MutableList<CompanySuspensionList> {
        return standardLevyOperationsSuspensionRepository.getCompanySuspensionRequest()
    }

    fun confirmCompanySuspension(
        companyProfileEntity: CompanyProfileEntity,
        standardLevyOperationsSuspension: StandardLevyOperationsSuspension
    ): String {
        companyProfileEntity.id = companyProfileEntity.id
        standardLevyOperationsSuspension.id = standardLevyOperationsSuspension.id
        companyProfileRepo.findByIdOrNull(companyProfileEntity.id)
            ?.let { entity ->

                entity.apply {
                   status=2
                }
                companyProfileRepo.save(entity)

            }?: throw Exception("COMPANY NOT FOUND")
        standardLevyOperationsSuspensionRepository.findByIdOrNull(standardLevyOperationsSuspension.id)
            ?.let { entity ->

                entity.apply {
                    status=1
                }
                standardLevyOperationsSuspensionRepository.save(entity)
                val companyName= companyProfileRepo.getCompanyName(companyProfileEntity.id)
                val userID= companyProfileRepo.getContactPersonId(companyProfileEntity.id)
                val recipient = userID?.let { commonDaoServices.getUserEmail(it) };
                val subject = "Request For Suspension of Company"
                val messageBody= "Dear ${companyName}, Your request for suspension of company operations has been approved. Regards, KEBS,"
                if (recipient != null) {
                    notifications.sendEmail(recipient, subject, messageBody)

                }
            }

        return "Company Suspended"
    }
    fun confirmCompanyResumption(
        companyProfileEntity: CompanyProfileEntity,
        standardLevyOperationsSuspension: StandardLevyOperationsSuspension
    ): String {
        companyProfileEntity.id = companyProfileEntity.id
        standardLevyOperationsSuspension.id = standardLevyOperationsSuspension.id
        companyProfileRepo.findByIdOrNull(companyProfileEntity.id)
            ?.let { entity ->

                entity.apply {
                    status=1
                }
                companyProfileRepo.save(entity)

            }?: throw Exception("COMPANY NOT FOUND")
        standardLevyOperationsSuspensionRepository.findByIdOrNull(standardLevyOperationsSuspension.id)
            ?.let { entity ->

                entity.apply {
                    status=1
                }
                standardLevyOperationsSuspensionRepository.save(entity)
                val companyName= companyProfileRepo.getCompanyName(companyProfileEntity.id)
                val userID= companyProfileRepo.getContactPersonId(companyProfileEntity.id)
                val recipient = userID?.let { commonDaoServices.getUserEmail(it) };
                val subject = "Request For Resumption of Company Operations"
                val messageBody= "Dear ${companyName}, Your request for resumption of company operations has been approved. Regards, KEBS,"
                if (recipient != null) {
                    notifications.sendEmail(recipient, subject, messageBody)

                }
            }

        return "Company Suspension Lifted"
    }

    fun rejectCompanySuspension(
        standardLevyOperationsSuspension: StandardLevyOperationsSuspension,
        companyProfileEntity: CompanyProfileEntity,
    ): String {
        companyProfileEntity.id = companyProfileEntity.id
        standardLevyOperationsSuspension.id = standardLevyOperationsSuspension.id
        standardLevyOperationsSuspensionRepository.findByIdOrNull(standardLevyOperationsSuspension.id)
            ?.let { entity ->

                entity.apply {
                    status=1
                }
                standardLevyOperationsSuspensionRepository.save(entity)
                val companyName= companyProfileRepo.getCompanyName(companyProfileEntity.id)
                val userID= companyProfileRepo.getContactPersonId(companyProfileEntity.id)
                val recipient = userID?.let { commonDaoServices.getUserEmail(it) };
                val subject = "Request For Suspension of Company"
                val messageBody= "Dear ${companyName}, Your request for suspension of company operations has been declined. Regards, KEBS,"
                if (recipient != null) {
                    notifications.sendEmail(recipient, subject, messageBody)

                }
            }

        return "Company Suspension Rejected"
    }

    fun rejectCompanyResumption(
        standardLevyOperationsSuspension: StandardLevyOperationsSuspension,
        companyProfileEntity: CompanyProfileEntity,
    ): String {
        companyProfileEntity.id = companyProfileEntity.id
        standardLevyOperationsSuspension.id = standardLevyOperationsSuspension.id
        standardLevyOperationsSuspensionRepository.findByIdOrNull(standardLevyOperationsSuspension.id)
            ?.let { entity ->

                entity.apply {
                    status=1
                }
                standardLevyOperationsSuspensionRepository.save(entity)
                val companyName= companyProfileRepo.getCompanyName(companyProfileEntity.id)
                val userID= companyProfileRepo.getContactPersonId(companyProfileEntity.id)
                val recipient = userID?.let { commonDaoServices.getUserEmail(it) };
                val subject = "Request For Suspension of Company"
                val messageBody= "Dear ${companyName}, Your request for resumption of company operations has been declined. Regards, KEBS,"
                if (recipient != null) {
                    notifications.sendEmail(recipient, subject, messageBody)

                }
            }

        return "Company Resumption Rejected"
    }



    fun closeCompanyOperations(
        standardLevyOperationsClosure: StandardLevyOperationsClosure
    ): LevyClosureValue {
        standardLevyOperationsClosure.companyId = standardLevyOperationsClosure.companyId
        standardLevyOperationsClosure.reason = standardLevyOperationsClosure.reason
        standardLevyOperationsClosure.dateOfClosure = standardLevyOperationsClosure.dateOfClosure
        standardLevyOperationsClosure.status = 0

        standardLevyOperationsClosureRepository.save(standardLevyOperationsClosure)

        return LevyClosureValue(standardLevyOperationsClosure.id)
    }

    // Upload Winding Up Report
    fun uploadWindingUpReport(
        uploads: SlWindingUpReportUploadsEntity,
        docFile: MultipartFile,
        doc: String,
        user: UsersEntity,
        DocDescription: String
    ): SlWindingUpReportUploadsEntity {

        with(uploads) {
//            filepath = docFile.path
            name = commonDaoServices.saveDocuments(docFile)
//            fileType = docFile.contentType
            fileType = docFile.contentType
            documentType = doc
            description = DocDescription
            document = docFile.bytes
            status = 1
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }

        return slWindingUpReportUploadsEntityRepository.save(uploads)
    }

    // View Report Document
    fun findWindingReportFileBYId(closureID: Long): SlWindingUpReportUploadsEntity {
        return slWindingUpReportUploadsEntityRepository.findAllById(closureID)
            ?: throw ExpectedDataNotFound("No File found with the following [ id=$closureID]")
    }

    fun getWindingReportDocumentList(closureID: Long): List<WindingUpReportListHolder> {
        return slWindingUpReportUploadsEntityRepository.findAllDocumentId(closureID)
    }

    fun confirmCompanyClosure(
        companyProfileEntity: CompanyProfileEntity,
        standardLevyOperationsClosure: StandardLevyOperationsClosure
    ): String {
        companyProfileEntity.id = companyProfileEntity.id
        standardLevyOperationsClosure.id = standardLevyOperationsClosure.id
        companyProfileRepo.findByIdOrNull(companyProfileEntity.id)
            ?.let { entity ->

                entity.apply {
                    status=0
                }
                companyProfileRepo.save(entity)

            }?: throw Exception("COMPANY NOT FOUND")
        standardLevyOperationsClosureRepository.findByIdOrNull(standardLevyOperationsClosure.id)
            ?.let { entity ->
                entity.apply {
                    status=1
                }
                standardLevyOperationsClosureRepository.save(entity)

                val companyName= companyProfileRepo.getCompanyName(companyProfileEntity.id)
                val userID= companyProfileRepo.getContactPersonId(companyProfileEntity.id)
                val recipient = userID?.let { commonDaoServices.getUserEmail(it) };
                val subject = "Request For Close of Company"
                val messageBody= "Dear ${companyName}, Your request for closure of company operations has been approved. Regards, KEBS,"
                if (recipient != null) {
                    notifications.sendEmail(recipient, subject, messageBody)

                }
            }



        return "Company Closed"
    }

    fun rejectCompanyClosure(
        standardLevyOperationsClosure: StandardLevyOperationsClosure,
        companyProfileEntity: CompanyProfileEntity
    ): String {

        companyProfileEntity.id = companyProfileEntity.id
        standardLevyOperationsClosure.id = standardLevyOperationsClosure.id
        standardLevyOperationsClosureRepository.findByIdOrNull(standardLevyOperationsClosure.id)
            ?.let { entity ->
                entity.apply {
                    status=1
                }
                standardLevyOperationsClosureRepository.save(entity)

                val companyName= companyProfileRepo.getCompanyName(companyProfileEntity.id)
                val userID= companyProfileRepo.getContactPersonId(companyProfileEntity.id)
                val recipient = userID?.let { commonDaoServices.getUserEmail(it) };
                val subject = "Request For Close of Company"
                val messageBody= "Dear ${companyName}, Your request for closure of company operations has been declined. Regards, KEBS,"
                if (recipient != null) {
                    notifications.sendEmail(recipient, subject, messageBody)

                }

            }


        return "Company Closure Rejected"
    }



    fun getCompanyClosureRequest(): MutableList<CompanyClosureList> {
        return standardLevyOperationsClosureRepository.getCompanyClosureRequest()
    }

    fun sendLevyPaymentReminders(): String {
        var companylist= companyProfileRepo.getManufactureEmailAddressList()

        companylist.forEach { item->
           val recipient= item.getCompanyEmail()
            val subject = "Levy Payment"
            val messageBody= "Dear ${item.getCompanyName()},You are reminded to Remit your levy for month ended. Ignore if paid"
            if (recipient != null) {
                notifications.sendEmail(recipient, subject, messageBody)
            }
        }

        return "Email Sent"
    }

    fun getLevyPenalty(): MutableList<LevyPenalty> {
        return companyProfileRepo.getLevyPenalty()
    }

    fun getManufacturesLevyPenaltyList(companyId: Long): MutableList<LevyPenalty>{
        return companyProfileRepo.getManufacturesLevyPenaltyList(companyId)
    }

    fun getManufacturesLevyPenalty(): MutableList<LevyPenalty>{
        commonDaoServices.loggedInUserDetailsEmail().id
            ?.let { id ->
                companyProfileRepo.getManufactureEntryNo(id)
                    .let {
                        return companyProfileRepo.getManufacturesLevyPenalty(it)
                    }

            }
            ?: throw ExpectedDataNotFound("No Data Found")
    }

    fun getManufacturesPayments(): MutableList<LevyPayment>{
        commonDaoServices.loggedInUserDetailsEmail().id
            ?.let { id ->
                companyProfileRepo.getManufactureEntryNo(id)
                    .let {

                        return companyProfileRepo.getManufacturesPayments(it.toString())
                    }

            }
            ?: throw ExpectedDataNotFound("No Data Found")
    }

    fun getManufacturesLevyPaymentsList(companyId: Long): MutableList<LevyPayments>{
        return companyProfileRepo.getManufacturesLevyPaymentsList(companyId)
    }

    fun getLevyPaymentsReceipt(id: Long): MutableList<LevyPayments>{
        return companyProfileRepo.getLevyPaymentsReceipt(id)
    }



    fun getManufacturesLevyPayments(): MutableList<LevyPayments>{
        commonDaoServices.loggedInUserDetailsEmail().id
            ?.let { id ->
                companyProfileRepo.getManufactureEntryNo(id)
                    .let {
                        return companyProfileRepo.getManufacturesLevyPayments(it)
                    }

            }
            ?: throw ExpectedDataNotFound("No Data Found")
    }

    fun getLevyDefaulters(): MutableList<LevyPenalty> {
        return companyProfileRepo.getLevyDefaulters()
    }

    fun getRandomToken(): String? {
        val rnd = Random()
        val number = rnd.nextInt(999999)

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number)
    }

    fun confirmEmailAddress(
        usersEntity: UsersEntity,
        emailVerificationTokenEntity: EmailVerificationTokenEntity
    ): ResponseMessage {
        var slResponse=""
        emailVerificationTokenEntity.token=emailVerificationTokenEntity.token
        usersEntity.id = usersEntity.id
        emailVerificationTokenEntity.token?.let {
            emailVerificationTokenEntityRepo.findFirstByToken(it)
                ?.let {
                    iUserRepository.findByIdOrNull(usersEntity.id)
                        ?.let { entity ->
                            entity.apply {
                                emailActivationStatus=1
                            }
                            iUserRepository.save(entity)

                        }
                    slResponse="Email Verified"
                }?: throw Exception("Token Used is Incorrect")
            slResponse="Token Used is Incorrect"
        }

        return ResponseMessage(slResponse)

    }

    fun sendEmailVerificationToken(
        emailVerificationTokenEntity: EmailVerificationTokenEntity
    ): String? {
        emailVerificationTokenEntity.createdBy= emailVerificationTokenEntity.createdBy
        emailVerificationTokenEntity.email = emailVerificationTokenEntity.email
        val verificationToken= getRandomToken()
        val tokensEntity = EmailVerificationTokenEntity()
        with(tokensEntity) {
            token = verificationToken
            email = emailVerificationTokenEntity.email
            status = 10
            createdBy = emailVerificationTokenEntity.email
            createdOn = Timestamp.from(Instant.now())
            tokenExpiryDate = Timestamp.from(Instant.now().plus(10, ChronoUnit.MINUTES))
            transactionDate = Date(java.util.Date().time)
            varField1 = UUID.randomUUID().toString()
        }

        emailVerificationTokenEntityRepo.save(tokensEntity)
//        val gson = Gson()
//        KotlinLogging.logger { }.info { "Save Entity" + gson.toJson(tokensEntity) }


        val firstName=  commonDaoServices.loggedInUserDetailsEmail().firstName
        val secondName=  commonDaoServices.loggedInUserDetailsEmail().lastName
        val recipient = emailVerificationTokenEntity.email
        val subject = "Email Verification"
        val messageBody= "Dear $firstName $secondName , Use this code $verificationToken to verify email. Regards, KEBS,"
        if (recipient != null) {
            notifications.sendEmail(recipient, subject, messageBody)

        }

        return "Verification Code Sent"


    }
    fun getPenaltyDetails(): MutableList<PenaltyDetails>{
       return companyProfileRepo.getPenaltyDetails();
    }

    fun getAllCompany(): MutableIterable<CompanyProfileEntity> {
        return companyProfileRepo.findAll()
    }

    fun mapPaymentDetails(data: List<LevyPayments>): List<LevyPaymentDTO>{
        return data.map {
            LevyPaymentDTO(
                it.getId(),
                it.getEntryNumber(),
                it.getPaymentDate(),
                it.getTotalPaymentAmt(),
                it.getCompanyName(),
                it.getKraPin(),
                it.getPeriodFrom(),
                it.getPeriodTo(),
                it.getPaymentSlipNo(),
                it.getPaymentSlipDate(),
                it.getPaymentType(),
                it.getTotalDeclAmt(),
                it.getTotalPenaltyAmt(),
                it.getBankRefNo(),
                it.getBankName(),
                it.getCommodityType(),
                it.getQtyManf(),
                it.getExFactVal(),
                it.getLevyPaid(),
                it.getPenaltyPaid()
            )
        }
    }


    fun updatePenaltyDetails(): String {
        var updatedPenalties= companyProfileRepo.updatedPenalty()
        updatedPenalties.forEach { item->
            val penaltyId=item.getPenaltyOrderNo()
            if (penaltyId != null) {
                companyProfileRepo.updatePenaltyStatus(penaltyId)
            }
        }

        return "Updated"
    }

    fun getRegisteredFirms(): MutableList<RegisteredFirms> {
        return companyProfileRepo.getRegisteredFirms()
    }

    fun getAllLevyPayments(): MutableList<AllLevyPayments> {
        return companyProfileRepo.getAllLevyPayments()
    }

    fun getPenaltyReport(): MutableList<AllLevyPayments> {
        return companyProfileRepo.getPenaltyReport()
    }
    fun getActiveFirms(): MutableList<RegisteredFirms> {
        return companyProfileRepo.getActiveFirms()
    }
    fun getDormantFirms(): MutableList<RegisteredFirms> {
        return companyProfileRepo.getDormantFirms()
    }
    fun getClosedFirms(): MutableList<RegisteredFirms> {
        return companyProfileRepo.getClosedFirms()
    }





}