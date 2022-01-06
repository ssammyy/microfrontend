package org.kebs.app.kotlin.apollo.api.ports.provided.dao

import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.engine.repository.Deployment
import org.flowable.task.api.Task
import org.kebs.app.kotlin.apollo.common.dto.std.NWAPreliminaryDraftDecision
import org.kebs.app.kotlin.apollo.common.dto.std.TaskDetails
import org.kebs.app.kotlin.apollo.common.dto.stdLevy.*
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.registration.CompanyProfileEntity
import org.kebs.app.kotlin.apollo.store.model.std.NWAJustification
import org.kebs.app.kotlin.apollo.store.model.std.NWAPreliminaryDraftUploads
import org.kebs.app.kotlin.apollo.store.model.std.TechnicalCommittee
import org.kebs.app.kotlin.apollo.store.repo.*
import org.kebs.app.kotlin.apollo.store.repo.stdLevy.ManufacturePenaltyDetailsDTO
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.ArrayList
import java.util.HashMap

@Service
class StandardLevyService(
    private val runtimeService: RuntimeService,
    private val taskService: TaskService,
    @Qualifier("processEngine") private val processEngine: ProcessEngine,
    private val repositoryService: RepositoryService,
    private val iStagingStandardsLevyManufacturerPenaltyRepository: IStagingStandardsLevyManufacturerPenaltyRepository,
    private val iStandardLevyPaymentsRepository: IStandardLevyPaymentsRepository,
    private val companyProfileRepo: ICompanyProfileRepository,
    private val commonDaoServices: CommonDaoServices,
    private val standardLevyFactoryVisitReportRepo: IStandardLevyFactoryVisitReportRepository,
    private val slUploadsRepo: ISlVisitUploadsRepository,
) {
    val PROCESS_DEFINITION_KEY = "sl_SiteVisitProcessFlow"
    val TASK_CANDIDATE_SL_PRINCIPAL_LEVY_OFFICER ="SL_PRINCIPAL_LEVY_OFFICER"
    val TASK_CANDIDATE_SL_Assistant_Manager ="SL_Assistant_Manager"
    val TASK_CANDIDATE_SL_Manager ="SL_Manager"
    val TASK_CANDIDATE_Manufacturer ="Manufacturer"

    //deploy bpmn file
    fun deployProcessDefinition(): Deployment =repositoryService
        .createDeployment()
        .addClasspathResource("processes/stdLevy/Sl_Site_Visit.bpmn20.xml")
        .deploy()

    //start the process by process Key
    fun startProcessByKey() : ProcessInstanceSiteResponse
    {

        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY)
        return ProcessInstanceSiteResponse( processInstance.id, processInstance.isEnded)
    }

    //List Manufactures
    fun getManufactureList(): MutableIterable<CompanyProfileEntity>
    {
        return companyProfileRepo.findAll()
    }


    fun getManufacturerPenaltyHistory(): MutableIterable<StagingStandardsLevyManufacturerPenalty>
    {
        return iStagingStandardsLevyManufacturerPenaltyRepository.findAll()
    }
    fun getManufacturerPenalty(): MutableIterable<StagingStandardsLevyManufacturerPenalty>
    {
        return iStagingStandardsLevyManufacturerPenaltyRepository.findAll()
    }

    fun getPaidLevies(): MutableIterable<StandardLevyPaymentsEntity>
    {
        return iStandardLevyPaymentsRepository.findAll()
    }

    fun scheduleSiteVisit(
        standardLevyFactoryVisitReportEntity: StandardLevyFactoryVisitReportEntity,
        stdLevyScheduleSiteVisitDTO: StdLevyScheduleSiteVisitDTO,
    ) : ProcessInstanceResponseSite
    {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val variables: MutableMap<String, Any> = java.util.HashMap()
        variables["manufacturerEntity"] = stdLevyScheduleSiteVisitDTO.manufacturerEntity!!
        variables["scheduledVisitDate"] = stdLevyScheduleSiteVisitDTO.scheduledVisitDate!!
        variables["status"] = 0
        variables["createdBy"] = loggedInUser.id!!
        var visit = standardLevyFactoryVisitReportEntity
        with(visit){
            manufacturerEntity=stdLevyScheduleSiteVisitDTO.manufacturerEntity
            scheduledVisitDate=stdLevyScheduleSiteVisitDTO.scheduledVisitDate
            status=0
            createdBy= loggedInUser.id.toString()
            createdOn= Timestamp(System.currentTimeMillis())

        }
        val visitDetails = standardLevyFactoryVisitReportRepo.save(standardLevyFactoryVisitReportEntity)
        variables["ID"] = visitDetails.id!!

        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables)
        return ProcessInstanceResponseSite(visitDetails.id.toString(),processInstance.id, processInstance.isEnded)


    }
    private fun getTaskDetails(tasks: List<Task>): List<TaskDetails> {
        val taskDetails: MutableList<TaskDetails> = ArrayList()
        for (task in tasks) {
            val processVariables = taskService.getVariables(task.id)
            taskDetails.add(TaskDetails(task.id, task.name, processVariables))

        }
        return taskDetails
    }

    //Return task details for PL OFFICER
    fun getScheduledVisits():List<TaskDetails>
    {

        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_SL_PRINCIPAL_LEVY_OFFICER).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    fun reportOnSiteVisit(
        standardLevyFactoryVisitReportEntity: StandardLevyFactoryVisitReportEntity,
        reportOnSiteVisitDTO: ReportOnSiteVisitDTO,
    ) : ProcessInstanceResponseValueSite
    {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val variables: MutableMap<String, Any> = java.util.HashMap()
        variables["visitDate"] = reportOnSiteVisitDTO.visitDate!!
        variables["purpose"] = reportOnSiteVisitDTO.purpose!!
        variables["personMet"] = reportOnSiteVisitDTO.personMet!!
        variables["actionTaken"] = reportOnSiteVisitDTO.actionTaken!!
        variables["visitID"] = reportOnSiteVisitDTO.visitID!!
        variables["status"] = 1

        standardLevyFactoryVisitReportRepo.findByIdOrNull(reportOnSiteVisitDTO.visitID)?.let { standardLevyFactoryVisitReportEntity->

            with(standardLevyFactoryVisitReportEntity){
                visitDate=reportOnSiteVisitDTO.visitDate!!
                purpose=reportOnSiteVisitDTO.purpose!!
                personMet=reportOnSiteVisitDTO.personMet!!
                actionTaken=reportOnSiteVisitDTO.actionTaken!!
                status=1

            }
            standardLevyFactoryVisitReportRepo.save(standardLevyFactoryVisitReportEntity)
        }?: throw Exception("SCHEDULED VISIT NOT FOUND")

        taskService.complete(reportOnSiteVisitDTO.taskId, variables)
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables)
        return ProcessInstanceResponseValueSite(reportOnSiteVisitDTO.visitID,processInstance.id, processInstance.isEnded
        )

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
            description=DocDescription
            document = docFile.bytes
            transactionDate = LocalDate.now()
            status = 1
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = LocalDateTime.now()
        }

        return slUploadsRepo.save(uploads)
    }

    //Return task details for Ast Manager
    fun getSiteReport():List<TaskDetails>
    {

        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_SL_Assistant_Manager).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    // View Report Document
    fun findUploadedReportFileBYId(visitId: Long): SlVisitUploadsEntity {
        return slUploadsRepo.findByVisitId(visitId) ?: throw ExpectedDataNotFound("No File found with the following [ id=$visitId]")
    }

    fun decisionOnSiteReport(siteVisitReportDecision: SiteVisitReportDecision) : List<TaskDetails> {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        variables["Yes"] = siteVisitReportDecision.accentTo
        variables["status"] = 1
        siteVisitReportDecision.comments.let { variables.put("comments", it) }

        standardLevyFactoryVisitReportRepo.findByIdOrNull(siteVisitReportDecision.visitID)?.let { standardLevyFactoryVisitReportEntity->

                with(standardLevyFactoryVisitReportEntity){
                    assistantManagerRemarks=siteVisitReportDecision.comments
                    status = 1
                }
            standardLevyFactoryVisitReportRepo.save(standardLevyFactoryVisitReportEntity)
            }?: throw Exception("TASK NOT FOUND")


        taskService.complete(siteVisitReportDecision.taskId, variables)
        return  getSiteReport()
    }

    //Return task details for  Manager
    fun getSiteReportLevelTwo():List<TaskDetails>
    {

        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_SL_Manager).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }

    fun decisionOnSiteReportLevelTwo(siteVisitReportDecision: SiteVisitReportDecision) : List<TaskDetails> {
        val variables: MutableMap<String, Any> = mutableMapOf()
        variables["Yes"] = siteVisitReportDecision.accentTo
        variables["status"] = 1
        siteVisitReportDecision.comments.let { variables.put("comments", it) }

        standardLevyFactoryVisitReportRepo.findByIdOrNull(siteVisitReportDecision.visitID)?.let { standardLevyFactoryVisitReportEntity->

            with(standardLevyFactoryVisitReportEntity){
                cheifManagerRemarks=siteVisitReportDecision.comments
                status = 2
            }
            standardLevyFactoryVisitReportRepo.save(standardLevyFactoryVisitReportEntity)
        }?: throw Exception("TASK NOT FOUND")


        taskService.complete(siteVisitReportDecision.taskId, variables)
        return  getSiteReport()
    }

    fun siteVisitReportFeedback(
        standardLevyFactoryVisitReportEntity: StandardLevyFactoryVisitReportEntity,
        reportOnSiteVisitDTO: ReportOnSiteVisitDTO,
    ) : ProcessInstanceResponseValueSite
    {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val variables: MutableMap<String, Any> = java.util.HashMap()
        variables["officersFeedback"] = reportOnSiteVisitDTO.officersFeedback!!
        variables["visitID"] = reportOnSiteVisitDTO.visitID!!
        variables["status"] = 3

        standardLevyFactoryVisitReportRepo.findByIdOrNull(reportOnSiteVisitDTO.visitID)?.let { standardLevyFactoryVisitReportEntity->

            with(standardLevyFactoryVisitReportEntity){
                officersFeedback=reportOnSiteVisitDTO.officersFeedback!!
                status=3

            }
            standardLevyFactoryVisitReportRepo.save(standardLevyFactoryVisitReportEntity)
        }?: throw Exception("SCHEDULED VISIT NOT FOUND")

        taskService.complete(reportOnSiteVisitDTO.taskId, variables)
        val processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables)
        return ProcessInstanceResponseValueSite(reportOnSiteVisitDTO.visitID,processInstance.id, processInstance.isEnded
        )

    }

    //Return task details for  Manufacturer
    fun getSiteFeedback():List<TaskDetails>
    {

        val tasks = taskService.createTaskQuery().taskCandidateGroup(TASK_CANDIDATE_Manufacturer).processDefinitionKey(PROCESS_DEFINITION_KEY).list()
        return getTaskDetails(tasks)
    }


}