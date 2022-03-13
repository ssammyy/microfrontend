package org.kebs.app.kotlin.apollo.api.ports.provided.dao

import com.google.gson.Gson
import mu.KotlinLogging
import org.flowable.engine.*
import org.flowable.engine.repository.Deployment
import org.flowable.task.api.Task
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.StandardsLevyBpmn
import org.kebs.app.kotlin.apollo.common.dto.std.TaskDetails
import org.kebs.app.kotlin.apollo.common.dto.std.TaskDetailsBody
import org.kebs.app.kotlin.apollo.common.dto.stdLevy.*
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.registration.CompanyProfileEditEntity
import org.kebs.app.kotlin.apollo.store.model.registration.CompanyProfileEntity
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.*
import org.kebs.app.kotlin.apollo.store.repo.std.UserListRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDate
import java.time.LocalDateTime
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
    private val notifications: Notifications

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
    fun getManufactureList(): MutableIterable<CompanyProfileEntity> {
        return companyProfileRepo.findAll()
    }


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
        companyProfileEditEntity: CompanyProfileEditEntity
    ): ProcessInstanceSiteResponse {

        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val variables: MutableMap<String, Any> = mutableMapOf()
        companyProfileEditEntity.name?.let { variables["companyName"] = it }
        companyProfileEditEntity.kraPin?.let { variables["kraPin"] = it }
        companyProfileEditEntity.registrationNumber?.let { variables["registrationNumber"] = it }
        companyProfileEditEntity.entryNumber?.let { variables["entryNumber"] = it }
        companyProfileEditEntity.manufactureId?.let { variables["manufactureId"] = it }
        companyProfileEditEntity.physicalAddress?.let { variables["physicalAddress"] = it }
        companyProfileEditEntity.postalAddress?.let { variables["postalAddress"] = it }
        companyProfileEditEntity.ownership?.let { variables["ownership"] = it }
        companyProfileEditEntity.taskType?.let { variables["taskType"] = it }
        companyProfileEditEntity.userType?.let { variables["userType"] = it }
        companyProfileEditEntity.assignedTo?.let { variables["assignedTo"] = it }
        companyProfileEditEntity.createdBy = loggedInUser.id.toString()
        companyProfileEditEntity.createdBy?.let{variables.put("originator", it)}
        companyProfileEditEntity.createdOn = commonDaoServices.getTimestamp()
        companyProfileEditEntity.createdOn?.let{variables.put("createdOn", it)}

        companyProfileEditEntity.status=1
        val userIntType = companyProfileEditEntity.userType
        val plUserTypes = 61L
        val asManagerUserTypes = 62L

        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables)

        companyProfileEditEntity.slBpmnProcessInstance = processInstance?.processInstanceId
        companyProfileEditEntity.slBpmnProcessInstance?.let{variables.put("slBpmnProcessInstance", it)}
          companyProfileEditEntityRepository.save(companyProfileEditEntity)

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

    fun editCompanyDetailsConfirm(
        companyProfileEntity: CompanyProfileEntity
    ): List<TaskDetailsBody> {

        companyProfileRepo.findByIdOrNull(companyProfileEntity.id)
            ?.let { entity ->

                val loggedInUser = commonDaoServices.loggedInUserDetails()
                val variables: MutableMap<String, Any> = mutableMapOf()
                companyProfileEntity.physicalAddress?.let { variables["physicalAddress"] = it }
                companyProfileEntity.postalAddress?.let { variables["postalAddress"] = it }
                companyProfileEntity.ownership?.let { variables["ownership"] = it }
                companyProfileEntity.createdBy = loggedInUser.userName
                companyProfileEntity.createdBy?.let{variables.put("createdBy", it)}
                companyProfileEntity.modifiedOn = commonDaoServices.getTimestamp()
                companyProfileEntity.modifiedOn?.let{variables.put("modifiedOn", it)}
                companyProfileEntity.taskId?.let { variables.put("taskId", it) }
                companyProfileEntity.assignedTo?.let { variables.put("assignedTo", it) }
                companyProfileEntity.accentTo?.let { variables["No"] = it }
                companyProfileEntity.accentTo?.let { variables["Yes"] = it }


                if (variables["Yes"] == true) {
                    entity.apply {
                        physicalAddress = companyProfileEntity.physicalAddress
                        postalAddress = companyProfileEntity.postalAddress
                        ownership = companyProfileEntity.ownership
                        modifiedBy = loggedInUser.userName
                        modifiedOn = commonDaoServices.getTimestamp()
                    }

                    companyProfileRepo.save(entity)

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
                                "Edit Approved",
                                companyProfileEntity.assignedTo
                                    ?: throw NullValueNotAllowedException("invalid user id provided")
                            )

                        }
                        ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${companyProfileEntity.slBpmnProcessInstance} ")
                }
                if (variables["No"] == false) {
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
                                "Edit Not Approved",
                                companyProfileEntity.assignedTo
                                    ?: throw NullValueNotAllowedException("invalid user id provided")
                            )

                        }
                        ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${companyProfileEntity.slBpmnProcessInstance} ")

                }

            } ?: throw Exception("COMPANY NOT FOUND")
return getUserTasks();

    }

    fun editCompanyDetailsConfirmLvlOne(
        companyProfileEntity: CompanyProfileEntity
    ): List<TaskDetailsBody> {

        companyProfileRepo.findByIdOrNull(companyProfileEntity.id)
            ?.let { entity ->

                val loggedInUser = commonDaoServices.loggedInUserDetails()
                val variables: MutableMap<String, Any> = mutableMapOf()
                companyProfileEntity.physicalAddress?.let { variables["physicalAddress"] = it }
                companyProfileEntity.postalAddress?.let { variables["postalAddress"] = it }
                companyProfileEntity.ownership?.let { variables["ownership"] = it }
                companyProfileEntity.createdBy = loggedInUser.id.toString()
                companyProfileEntity.createdBy?.let{variables.put("createdBy", it)}
                companyProfileEntity.modifiedOn = commonDaoServices.getTimestamp()
                companyProfileEntity.modifiedOn?.let{variables.put("modifiedOn", it)}
                companyProfileEntity.taskId?.let { variables.put("taskId", it) }
                companyProfileEntity.assignedTo?.let { variables.put("assignedTo", it) }
                companyProfileEntity.accentTo?.let { variables["No"] = it }
                companyProfileEntity.accentTo?.let { variables["Yes"] = it }


                if (variables["Yes"] == true) {

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
                                "Edit Not Approved",
                                companyProfileEntity.assignedTo
                                    ?: throw NullValueNotAllowedException("invalid user id provided")
                            )

                        }
                        ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${companyProfileEntity.slBpmnProcessInstance} ")

                }

            } ?: throw Exception("COMPANY NOT FOUND")
        return getUserTasks();

    }


    fun editCompanyDetailsConfirmLvlTwo(
        companyProfileEntity: CompanyProfileEntity
    ): List<TaskDetailsBody> {

        companyProfileRepo.findByIdOrNull(companyProfileEntity.id)
            ?.let { entity ->

                val loggedInUser = commonDaoServices.loggedInUserDetails()
                val variables: MutableMap<String, Any> = mutableMapOf()
                companyProfileEntity.physicalAddress?.let { variables["physicalAddress"] = it }
                companyProfileEntity.postalAddress?.let { variables["postalAddress"] = it }
                companyProfileEntity.ownership?.let { variables["ownership"] = it }
                companyProfileEntity.createdBy = loggedInUser.userName
                companyProfileEntity.createdBy?.let{variables.put("createdBy", it)}
                companyProfileEntity.modifiedOn = commonDaoServices.getTimestamp()
                companyProfileEntity.modifiedOn?.let{variables.put("modifiedOn", it)}
                companyProfileEntity.taskId?.let { variables.put("taskId", it) }
                companyProfileEntity.assignedTo?.let { variables.put("assignedTo", it) }
                companyProfileEntity.accentTo?.let { variables["No"] = it }
                companyProfileEntity.accentTo?.let { variables["Yes"] = it }


                if (variables["Yes"] == true) {
                    entity.apply {
                        physicalAddress = companyProfileEntity.physicalAddress
                        postalAddress = companyProfileEntity.postalAddress
                        ownership = companyProfileEntity.ownership
                        modifiedBy = loggedInUser.userName
                        modifiedOn = commonDaoServices.getTimestamp()
                    }

                    companyProfileRepo.save(entity)

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
                                "Edit Approved",
                                companyProfileEntity.assignedTo
                                    ?: throw NullValueNotAllowedException("invalid user id provided")
                            )

                        }
                        ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${companyProfileEntity.slBpmnProcessInstance} ")
                }
                if (variables["No"] == false) {
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
        val loggedInUser = commonDaoServices.loggedInUserDetails()

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
                    companyProfileEntity.businessLines?.let { variables["businessLines"] = it }
                    companyProfileEntity.businessNatures?.let { variables["businessNatures"] = it }
                    companyProfileEntity.buildingName?.let { variables["buildingName"] = it }
                    companyProfileEntity.directorIdNumber?.let { variables["directorIdNumber"] = it }
                    companyProfileEntity.region?.let { variables["region"] = it }
                    companyProfileEntity.county?.let { variables["county"] = it }
                    companyProfileEntity.town?.let { variables["town"] = it }
                    companyProfileEntity.manufactureStatus?.let { variables["manufactureStatus"] = it }
                    companyProfileEntity.entryNumber?.let { variables["entryNumber"] = it }
                    companyProfileEntity.id?.let { variables["manufacturerEntity"] = it }
                        ?: throw Exception("COMPANY NOT FOUND")
                    companyProfileEntity.userId?.let { variables["contactId"] = it }
                    companyProfileEntity.taskType?.let { variables["taskType"] = it }

                    companyProfileEntity.assignedTo = companyProfileEntity.assignedTo
                    companyProfileEntity.assignStatus = 1
                    companyProfileEntity.createdBy = loggedInUser.userName
                    companyProfileEntity.createdOn = commonDaoServices.getTimestamp()
                    val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables)
                    companyProfileEntity.slBpmnProcessInstance = processInstance?.processInstanceId

//                val gson = Gson()
//                 KotlinLogging.logger { }.info { "Save Entity" + gson.toJson(companyProfileEntity) }

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
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val variables: MutableMap<String, Any> = java.util.HashMap()
        standardLevyFactoryVisitReportEntity.manufacturerEntity?.let { variables["manufacturerEntity"] = it }
        standardLevyFactoryVisitReportEntity.scheduledVisitDate?.let { variables["scheduledVisitDate"] = it }
        standardLevyFactoryVisitReportEntity.createdBy?.let { variables["createdBy"] = it }
        standardLevyFactoryVisitReportEntity.status?.let { variables["status"] = it }
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
                    .processInstanceId(companyProfileEntity.slBpmnProcessInstance).list()
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
                            "Prepare Report on Visit",
                            loggedInUser.id ?: throw NullValueNotAllowedException("invalid user id provided")
                        )
                        return ProcessInstanceResponseSite(
                            visitDetails.id.toString(),
                            processInstance.id,
                            processInstance.isEnded
                        )
                    }
                    ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${companyProfileEntity.slBpmnProcessInstance} ")

            }
            ?: throw NullValueNotAllowedException("COMPANY NOT FOUND")


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

//        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_SL_PRINCIPAL_LEVY_OFFICER).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        val tasks = taskService.createTaskQuery()
            .taskAssignee("${commonDaoServices.loggedInUserDetails().id ?: throw NullValueNotAllowedException(" invalid user id provided")}")
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
            .taskAssignee("${commonDaoServices.loggedInUserDetails().id ?: throw NullValueNotAllowedException(" invalid user id provided")}")
            .list()
        return getTaskDetails(tasks)
    }

    fun reportOnSiteVisitTest(standardLevyFactoryVisitReportEntity: StandardLevyFactoryVisitReportEntity) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val variables: MutableMap<String, Any> = java.util.HashMap()
        standardLevyFactoryVisitReportEntity.visitDate?.let { variables["visitDate"] = it }
        standardLevyFactoryVisitReportEntity.purpose?.let { variables["purpose"] = it }
        standardLevyFactoryVisitReportEntity.personMet?.let { variables["personMet"] = it }
        standardLevyFactoryVisitReportEntity.actionTaken?.let { variables["actionTaken"] = it }
        standardLevyFactoryVisitReportEntity.id?.let { variables["visitID"] = it }
        standardLevyFactoryVisitReportEntity.status?.let { variables["status"] = it }
        standardLevyFactoryVisitReportEntity.assigneeId?.let { variables["assigneeId"] = it }
        standardLevyFactoryVisitReportEntity.userType?.let { variables["userType"] = it }
        standardLevyFactoryVisitReportEntity.makeRemarks?.let { variables["makeRemarks"] = it }
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
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val variables: MutableMap<String, Any> = java.util.HashMap()
        standardLevyFactoryVisitReportEntity.visitDate?.let { variables["visitDate"] = it }
        standardLevyFactoryVisitReportEntity.purpose?.let { variables["purpose"] = it }
        standardLevyFactoryVisitReportEntity.personMet?.let { variables["personMet"] = it }
        standardLevyFactoryVisitReportEntity.actionTaken?.let { variables["actionTaken"] = it }
        standardLevyFactoryVisitReportEntity.id?.let { variables["visitID"] = it }
        standardLevyFactoryVisitReportEntity.status?.let { variables["status"] = it }
        standardLevyFactoryVisitReportEntity.assigneeId?.let { variables["assigneeId"] = it }
        standardLevyFactoryVisitReportEntity.userType?.let { variables["userType"] = it }
        standardLevyFactoryVisitReportEntity.makeRemarks?.let { variables["makeRemarks"] = it }
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

                }
                standardLevyFactoryVisitReportRepo.save(entity)
            } ?: throw Exception("SCHEDULED VISIT NOT FOUND")


        companyProfileRepo.findByIdOrNull(standardLevyFactoryVisitReportEntity.manufacturerEntity)
            ?.let { companyProfileEntity ->
                runtimeService.createProcessInstanceQuery()
                    .processInstanceId(companyProfileEntity.slBpmnProcessInstance).list()
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
                    ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${companyProfileEntity.slBpmnProcessInstance} ")

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
        return slUploadsRepo.findByVisitId(visitId)
            ?: throw ExpectedDataNotFound("No File found with the following [ id=$visitId]")
    }

    fun decisionOnSiteReport(standardLevyFactoryVisitReportEntity: StandardLevyFactoryVisitReportEntity): List<TaskDetailsBody> {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        standardLevyFactoryVisitReportEntity.assistantManagerRemarks?.let { variables["assistantManagerRemarks"] = it }
        standardLevyFactoryVisitReportEntity.accentTo?.let { variables["No"] = it }
        standardLevyFactoryVisitReportEntity.accentTo?.let { variables["Yes"] = it }
        standardLevyFactoryVisitReportEntity.assigneeId?.let { variables["assigneeId"] = it }
        variables["status"] = 1
        loggedInUser.id?.let { variables["levelOneId"] = it }
        if (variables["Yes"] == true) {
            standardLevyFactoryVisitReportRepo.findByIdOrNull(standardLevyFactoryVisitReportEntity.id)
                ?.let { standardLevyFactoryVisitReportEntity ->

                    with(standardLevyFactoryVisitReportEntity) {
                        assistantManagerRemarks = standardLevyFactoryVisitReportEntity.assistantManagerRemarks
                        status = 1
                        assigneeId = standardLevyFactoryVisitReportEntity.assigneeId
                        accentTo = true
                    }
                    standardLevyFactoryVisitReportRepo.save(standardLevyFactoryVisitReportEntity)
                } ?: throw Exception("TASK NOT FOUND")

            companyProfileRepo.findByIdOrNull(standardLevyFactoryVisitReportEntity.manufacturerEntity)
                ?.let { companyProfileEntity ->
                    runtimeService.createProcessInstanceQuery()
                        .processInstanceId(companyProfileEntity.slBpmnProcessInstance).list()
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
                        ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${companyProfileEntity.slBpmnProcessInstance} ")

                }
                ?: throw NullValueNotAllowedException("COMPANY NOT FOUND")
        } else if (variables["No"] == false) {
            standardLevyFactoryVisitReportRepo.findByIdOrNull(standardLevyFactoryVisitReportEntity.id)
                ?.let { standardLevyFactoryVisitReportEntity ->

                    with(standardLevyFactoryVisitReportEntity) {
                        assistantManagerRemarks = standardLevyFactoryVisitReportEntity.assistantManagerRemarks
                        status = 1
                        assigneeId = standardLevyFactoryVisitReportEntity.assigneeId
                        accentTo = false
                    }
                    standardLevyFactoryVisitReportRepo.save(standardLevyFactoryVisitReportEntity)

                } ?: throw Exception("TASK NOT FOUND")
            val userEmail = standardLevyFactoryVisitReportEntity.assigneeId?.let { commonDaoServices.getUserEmail(it) };
            val recipient= "Christine.gaiti@bskglobaltech.com"
            val subject = "Report Rejected  $userEmail"
            val messageBody= "Site visit report has been rejected by  "+ commonDaoServices.loggedInUserDetails().userName+".Log in to KIMS to make recommended changes."
            notifications.sendEmail(recipient, subject, messageBody)

            companyProfileRepo.findByIdOrNull(standardLevyFactoryVisitReportEntity.manufacturerEntity)
                ?.let { companyProfileEntity ->
                    runtimeService.createProcessInstanceQuery()
                        .processInstanceId(companyProfileEntity.slBpmnProcessInstance).list()
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
                        ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${companyProfileEntity.slBpmnProcessInstance} ")

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

    fun decisionOnSiteReportLevelTwo(standardLevyFactoryVisitReportEntity: StandardLevyFactoryVisitReportEntity): List<TaskDetailsBody> {
        val variables: MutableMap<String, Any> = mutableMapOf()
        standardLevyFactoryVisitReportEntity.accentTo?.let { variables["Yes"] = it }
        standardLevyFactoryVisitReportEntity.accentTo?.let { variables["No"] = it }
        standardLevyFactoryVisitReportEntity.cheifManagerRemarks?.let { variables["cheifManagerRemarks"] = it }
        variables["status"] = 2
        standardLevyFactoryVisitReportEntity.assigneeId?.let { variables["assigneeId"] = it }
        val userIntType = standardLevyFactoryVisitReportEntity.userType
        val userIntTypes = userIntType.toString()
        if (variables["Yes"] == true) {
            standardLevyFactoryVisitReportRepo.findByIdOrNull(standardLevyFactoryVisitReportEntity.id)
                ?.let { standardLevyFactoryVisitReportEntity ->

                    with(standardLevyFactoryVisitReportEntity) {
                        cheifManagerRemarks = standardLevyFactoryVisitReportEntity.cheifManagerRemarks
                        status = 2
                        accentTo = true
                    }
                    standardLevyFactoryVisitReportRepo.save(standardLevyFactoryVisitReportEntity)
                } ?: throw Exception("TASK NOT FOUND")

            companyProfileRepo.findByIdOrNull(standardLevyFactoryVisitReportEntity.manufacturerEntity)
                ?.let { companyProfileEntity ->
                    runtimeService.createProcessInstanceQuery()
                        .processInstanceId(companyProfileEntity.slBpmnProcessInstance).list()
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
                        ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${companyProfileEntity.slBpmnProcessInstance} ")

                }
                ?: throw NullValueNotAllowedException("COMPANY NOT FOUND")
        } else if (variables["No"] == false) {
            standardLevyFactoryVisitReportRepo.findByIdOrNull(standardLevyFactoryVisitReportEntity.id)
                ?.let { standardLevyFactoryVisitReportEntity ->

                    with(standardLevyFactoryVisitReportEntity) {
                        cheifManagerRemarks = standardLevyFactoryVisitReportEntity.cheifManagerRemarks
                        status = 2
                        accentTo = false
                    }
                    standardLevyFactoryVisitReportRepo.save(standardLevyFactoryVisitReportEntity)
                } ?: throw Exception("TASK NOT FOUND")

            val userEmail = standardLevyFactoryVisitReportEntity.assigneeId?.let { commonDaoServices.getUserEmail(it) };
            val recipient= "Christine.gaiti@bskglobaltech.com"
            val subject = "Report Rejected  $userEmail"
            val messageBody= "Site visit report has been rejected by  "+ commonDaoServices.loggedInUserDetails().userName+".Log in to KIMS to make recommended changes."
            notifications.sendEmail(recipient, subject, messageBody)

            companyProfileRepo.findByIdOrNull(standardLevyFactoryVisitReportEntity.manufacturerEntity)
                ?.let { companyProfileEntity ->
                    runtimeService.createProcessInstanceQuery()
                        .processInstanceId(companyProfileEntity.slBpmnProcessInstance).list()
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
                        ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${companyProfileEntity.slBpmnProcessInstance} ")

                }
                ?: throw NullValueNotAllowedException("COMPANY NOT FOUND")
        }

        return getUserTasks()
    }

    fun siteVisitReportFeedback(
        standardLevyFactoryVisitReportEntity: StandardLevyFactoryVisitReportEntity
    ): ProcessInstanceResponseValueSite {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val variables: MutableMap<String, Any> = java.util.HashMap()
        standardLevyFactoryVisitReportEntity.officersFeedback?.let { variables["officersFeedback"] = it }
        standardLevyFactoryVisitReportEntity.id?.let { variables["visitID"] = it }
        standardLevyFactoryVisitReportEntity.status?.let { variables["status"] = it }
        standardLevyFactoryVisitReportEntity.assigneeId?.let { variables["assigneeId"] = it }

        standardLevyFactoryVisitReportRepo.findByIdOrNull(standardLevyFactoryVisitReportEntity.id)
            ?.let { standardLevyFactoryVisitReportEntity ->

                with(standardLevyFactoryVisitReportEntity) {
                    officersFeedback = standardLevyFactoryVisitReportEntity.officersFeedback
                    assigneeId = standardLevyFactoryVisitReportEntity.assigneeId
                    status = 3
                    slProcessStatus = 1

                }
                standardLevyFactoryVisitReportRepo.save(standardLevyFactoryVisitReportEntity)
            } ?: throw Exception("SCHEDULED VISIT NOT FOUND")

        val recipient = standardLevyFactoryVisitReportEntity.assigneeId?.let { commonDaoServices.getUserEmail(it) };
        val subject = "Site Visit Report"
        val messageBody= "Site visit report has been prepared and uploaded. Kindly login to the KIMS Portal to view"
        if (recipient != null) {
            notifications.sendEmail(recipient, subject, messageBody)
        }

        companyProfileRepo.findByIdOrNull(standardLevyFactoryVisitReportEntity.manufacturerEntity)
            ?.let { companyProfileEntity ->
                with(companyProfileEntity) {
                    assignedTo = 0
                    assignStatus = 0
                }
                companyProfileRepo.save(companyProfileEntity)
                runtimeService.createProcessInstanceQuery()
                    .processInstanceId(companyProfileEntity.slBpmnProcessInstance).list()
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
                            standardLevyFactoryVisitReportEntity?.assigneeId
                                ?: throw NullValueNotAllowedException("invalid user id provided")
                        )
                        return ProcessInstanceResponseValueSite(
                            standardLevyFactoryVisitReportEntity.id,
                            processInstance.id,
                            processInstance.isEnded
                        )
                    }
                    ?: throw NullValueNotAllowedException("No Process Instance found with ID = ${companyProfileEntity.slBpmnProcessInstance} ")

            }
            ?: throw NullValueNotAllowedException("COMPANY NOT FOUND")


    }

    //Return task details for  Manufacturer
    fun getSiteFeedback(): List<TaskDetailsBody> {

        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_Manufacturer)
            .processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    fun getManufacturerList(): MutableList<CompanyProfileEntity> {
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
        commonDaoServices.loggedInUserDetails().id?.let { id ->
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
        commonDaoServices.loggedInUserDetails().id?.let { id ->
            return iUserRoleAssignmentsRepository.getRoleByUserId(id)
        } ?: throw NullValueNotAllowedException("Role Not Found")
    }

    fun getSLNotificationStatus(): Boolean {
        commonDaoServices.loggedInUserDetails().id
            ?.let { id ->
                companyProfileRepo.getManufactureId(id)
                    .let {
                        stdLevyNotificationFormRepository.findTopByManufactureIdOrderByIdDesc(it)
                            ?.let {

                                //Manufacturer
                                return true
                            }
                            ?: run {
                                //Contractor
                                return false
                            }
                    }

            }
            ?: return false
    }


    fun getIfRecordExists(): Boolean {
        val userId = commonDaoServices.loggedInUserDetails().id
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

    fun getManufacturerStatus(): Boolean {
        commonDaoServices.loggedInUserDetails().id
            ?.let { id ->
                companyProfileRepo.getBusinessNature(id)
                    .let {
                        iBusinessNatureRepository.getManufacturerStatus(it)
                            ?.let {

                                //Manufacturer
                                return true
                            }
                            ?: run {
                                //Contractor
                                return false
                            }
                    }
            }
            ?: return false
    }

    fun getCompanyEditedDetails(manufactureId: Long): CompanyProfileEditEntity {
        return companyProfileEditEntityRepository.findFirstByManufactureIdOrderByIdDesc(manufactureId)
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


}