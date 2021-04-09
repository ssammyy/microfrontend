package org.kebs.app.kotlin.apollo.api.controllers.qaControllers

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.QualityAssuranceBpmn
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.FactoryInspectionReportEntity
import org.kebs.app.kotlin.apollo.store.model.PermitApplicationEntity
import org.kebs.app.kotlin.apollo.store.model.PermitApplicationRemarksEntity
import org.kebs.app.kotlin.apollo.store.repo.*
import org.kebs.app.kotlin.apollo.store.repo.qa.IPermitTypesEntityRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.lang.StringBuilder
import java.sql.Timestamp
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.mail.internet.MimeMessage
import javax.validation.Valid


@Controller
class QAHOFController(
    private val permitRepository: IPermitRepository,
    private val applicationQuestionnaireRepo: IApplicationQuestionnaireRepository,
    private val permitRemarksRepository: IPermitApplicationRemarksRepository,
    private val questionnaireSta10Repo: IQuestionnaireSta10Repository,
    private val usersRepository: IUserRepository,
    private val sampleStandardsRepository: ISampleStandardsRepository,
    private val sender: JavaMailSender,
    private val notifications: Notifications,
    private val manufacturerRepository: IManufacturerRepository,
    private val qualityAssuranceBpmn: QualityAssuranceBpmn,
    applicationMapProperties: ApplicationMapProperties,
    private val permitTypesRepo: IPermitTypesEntityRepository,
    private val factoryInspectionRepository: IFactoryInspectionReportRepository
) {
    @Value("\${bpmn.qa.app.review.process.definition.key}")
    lateinit var qaAppReviewProcessDefinitionKey: String
    val pscAssigneeId = applicationMapProperties.pscAssigneeId
    val qaoAssigneeId = applicationMapProperties.qaoAssigneeId
    val hodId = applicationMapProperties.qaHod
    val labId = applicationMapProperties.labAssigneeId
    val ninetyDayWindow: Int = 244


    @PostMapping("/api/qa/hof/assign/qao/{id}")
    fun saveAssignQAO(
            @ModelAttribute("permit") @Valid permit: PermitApplicationEntity,
                      model: Model,
                        redirectAttributes: RedirectAttributes,
                      @PathVariable("id") id: Long): String {
        permitRepository.findByIdOrNull(permit.id)
                ?.let { i ->
                    i.qao = permit.qao
                    permitRepository.save(i)
                    permit.id?.let {
                        permit.qao?.let { it1 -> qualityAssuranceBpmn.qaAppReviewAllocateToQAO(it, it1) }
                    }

                    // 90 Day window - 2 reminders
                    permit.qao?.let { qualityAssuranceBpmn.scheduleReminder(ninetyDayWindow, Timestamp.from(Instant.now().plus(1080, ChronoUnit.HOURS)), it) }
                    permit.qao?.let { qualityAssuranceBpmn.scheduleReminder(ninetyDayWindow, Timestamp.from(Instant.now().plus(2160, ChronoUnit.HOURS)), it) }

                    redirectAttributes.addFlashAttribute("alert", "Task had been passed to the selected Officer")
                }
        return "redirect:/api/qa/hof"
    }


    @GetMapping("/api/qa/hof/application/details/{id}")
    fun applicationDetails(model: Model, @PathVariable("id") id: Long): String {
        permitRepository.findByIdOrNull(id)
                ?.let { pm ->
                    val questionnaire = questionnaireSta10Repo.findByPermitId(pm.id)
                    val appQuestionnaire = applicationQuestionnaireRepo.findByPermitId(pm.id)

                    model.addAttribute("app", pm)
                    model.addAttribute("officers", usersRepository.findAll())
                    model.addAttribute("std", pm.productSubCategory?.let { sampleStandardsRepository.findBySubCategoryId(it) })
                    model.addAttribute("questionnaire", questionnaire)
                    model.addAttribute("questionnaire3", appQuestionnaire)
                    model.addAttribute("remarks", PermitApplicationRemarksEntity())

                    model.addAttribute("permitType", permitTypesRepo.findByIdOrNull(pm.permitType)?.mark)

                    KotlinLogging.logger {  }.info { "Checking for factory inspection" }

                    model.addAttribute("usr", usersRepository.findByIdOrNull(541))
                    model.addAttribute("officer", usersRepository.findByIdOrNull(402))

                    factoryInspectionRepository.findByPermitApplicationId(pm)
                            ?.let { inspectionReport ->
                                model.addAttribute("report", inspectionReport)
                                KotlinLogging.logger {  }.info { "Inspection report, $inspectionReport" }
                            }

                    questionnaire
                            ?.let { questionnaire ->
                                model.addAttribute("questionnaire", questionnaire)
                                pm.extraDocuments
                                        ?.let { documents ->
                                            val documentList: List<String>? = documents.split(",")
                                            model.addAttribute("documents", documentList)
                                        }
                                questionnaire.certificateFile
                                        ?.let { certDoc ->
                                            val documentList: List<String>? = certDoc.split(",")
                                            model.addAttribute("certDocs", documentList)
                                        }
                                questionnaire.testingRecordFile
                                        ?.let { testingRecordDoc ->
                                            val documentList: List<String>? = testingRecordDoc.split(",")
                                            model.addAttribute("testingRecordDocs", documentList)
                                        }
                                questionnaire.processMonitoringRecordsFile
                                        ?.let { testingRecordDoc ->
                                            val documentList: List<String>? = testingRecordDoc.split(",")
                                            model.addAttribute("testingRecordDocs", documentList)
                                        }
                                questionnaire.frequencyUpload
                                        ?.let { testingRecordDoc ->
                                            val documentList: List<String>? = testingRecordDoc.split(",")
                                            model.addAttribute("testingRecordDocs", documentList)
                                        }
                                questionnaire.criticalProcessParametersUpload
                                        ?.let { criticalProcessParametersUploadDoc ->
                                            val documentList: List<String>? = criticalProcessParametersUploadDoc.split(",")
                                            model.addAttribute("criticalProcessParametersUploadDocs", documentList)
                                        }
                                questionnaire.operationsUpload
                                        ?.let { operationsUploadDoc ->
                                            val documentList: List<String>? = operationsUploadDoc.split(",")
                                            model.addAttribute("operationsUploadDocs", documentList)
                                        }
                                questionnaire.processFlowOfProductionUpload
                                        ?.let { processFlowOfProductionUploadDoc ->
                                            val documentList: List<String>? = processFlowOfProductionUploadDoc.split(",")
                                            model.addAttribute("processFlowOfProductionUploadDocs", documentList)
                                        }
                            }

                    pm.id?.let {
                        appQuestionnaire
                                ?.let { qn ->
                                    model.addAttribute("qn", qn)
                                    pm.labReportsFilepath1
                                            ?.let { labDocs ->
                                                val labDocList: List<String>? = labDocs.split(",")
                                                model.addAttribute("labDocs", labDocList)
                                            }
                                    pm.extraDocuments
                                            ?.let { documents ->
                                                val documentList: List<String>? = documents.split(",")
                                                model.addAttribute("documents", documentList)
                                            }
                                    qn.illustrationFile
                                            ?.let { illustration ->
                                                val documentList: List<String>? = illustration.split(",")
                                                model.addAttribute("illustrations", documentList)
                                            }
                                    qn.manufacturingStepsFile
                                            ?.let { manufacturingSteps ->
                                                val documentList: List<String>? = manufacturingSteps.split(",")
                                                model.addAttribute("manufacturingStepsFiles", documentList)
                                            }
                                    qn.testsAgainstTheStandardFile
                                            ?.let { testsAgainstTheStandard ->
                                                val documentList: List<String>? = testsAgainstTheStandard.split(",")
                                                model.addAttribute("testsAgainstTheStandardFiles", documentList)
                                            }
                                    qn.qualityManualFile
                                            ?.let { qualityManualFile ->
                                                val documentList: List<String>? = qualityManualFile.split(",")
                                                model.addAttribute("qualityManualFiles", documentList)
                                            }
                                    qn.testReportsLevelOfDefectivesFile
                                            ?.let { testReportsLevelOfDefectives ->
                                                val documentList: List<String>? = testReportsLevelOfDefectives.split(",")
                                                model.addAttribute("testReportsLevelOfDefectivesFiles", documentList)
                                            }
                                }
                    }

                    return "quality-assurance/HOF/application-details"

                }
                ?: throw NullValueNotAllowedException("No Permit found for id=$id")

    }

    @PreAuthorize("hasAuthority('ASSIGN_OFFICER') or hasAuthority('ALLOCATE_DMARK_TO_QAO') or hasAuthority('GIVE_RECOMMENDATION_ON_PERMIT_AWARD') or hasAuthority('APPROVE_PERMIT')")
    @GetMapping("/api/qa/hof")
    fun applications(
            model: Model,
            @RequestParam(value = "page", required = false) page: Int?,
            @RequestParam(value = "records", required = false) records: Int?,
            @RequestParam(value = "status", required = false) status: Int?,
            redirectAttributes: RedirectAttributes
    ): String {
        var result = "quality-assurance/HOF/applications"
        SecurityContextHolder.getContext().authentication?.name
                ?.let { username ->
                    usersRepository.findByUserName(username)
                            ?.let { loggedInUser ->
                                loggedInUser.id
                                        ?.let { usr ->
                                            qualityAssuranceBpmn.fetchAllTasksByAssignee(usr)?.let { lstTaskDetails ->
                                                model.addAttribute("tasks", lstTaskDetails)
                                                //val tasks = mutableListOf<PermitApplicationEntity?>()
                                                var lstPermitIds = mutableListOf<Long>()
                                                for (taskDetail in lstTaskDetails){
                                                    lstPermitIds.add(taskDetail.permitId)
                                                }

                                                var tasks = permitRepository.findByIdIsIn(lstPermitIds.toList()).sortedByDescending { it.id }

                                                result = if(tasks.size> 0) {
                                                    redirectAttributes.addFlashAttribute("alert", "You have ${tasks.size} tasks.")
                                                    model.addAttribute("permitApplications", tasks)
                                                    "quality-assurance/HOF/applications"
                                                } else {
                                                    redirectAttributes.addFlashAttribute("alert", "You have no tasks.")
                                                    "quality-assurance/HOF/applications"
                                                }
                                                //model.addAttribute("users", usersRepository.findAll())
                                            }
                                        }
                            }
                }
        return result
    }

//    @PreAuthorize("hasAuthority('ASSIGN_OFFICER') or hasAuthority('APPROVE_PERMIT') or hasAuthority('GIVE_RECOMMENDATION_ON_PERMIT_AWARD')")
    @GetMapping("/incomplete_application")
    fun markApplicationAsIncompleteComplete(
            model: Model,
            @RequestParam("status", required = false) status: Int?,
            @RequestParam("id") id: Long,
            @RequestParam("permitRecommendation", required = false) permitRecommendation: Int?,
            @RequestParam("whereTo", required = true) whereTo: String?,
            @RequestParam("inspectionApprovalStatus", required = false) inspectionApprovalStatus: Int?,
            @RequestParam("availabilityOfLabReport", required = false) availabilityOfLabReport: Int?,
            @RequestParam("reportComplianceStatus", required = false) reportComplianceStatus: Int?,
            @RequestParam("approveJustification", required = false) approveJustification: Int?,
            @RequestParam("assessorSelectionId") assessorSelectionId: Long?,
            @RequestParam("feedbackData", required = false) feedbackData: String?,
            redirectAttributes: RedirectAttributes
    ): String {
    var result = ""
        val permit = permitRepository.findById(id).get()
        val inspectionReport = factoryInspectionRepository.findByPermitApplicationId(permit)
        when(whereTo) {
            "application_completeness" -> {
                when (status) {
                    1 -> {
                        redirectAttributes.addFlashAttribute("alert", "You have marked the application as complete.")
                        if(permit.permitType == 1L) {
                            qualityAssuranceBpmn.qaDmARCheckApplicationComplete(id, hodId, true)
                                    .let {
                                        qualityAssuranceBpmn.qaDmARAllocateToQAOComplete(id, qaoAssigneeId)
                                        redirectAttributes.addFlashAttribute("alert", "You have marked the application as complete. The task has been passed to the selected QAO")
                                    }
                        } else {
                            qualityAssuranceBpmn.qaAppReviewCheckIfApplicationComplete(id, status)
                        }

                        with(permit) {
                            applicationCompletenessStatus = status
                        }
                        permitRepository.save(permit)

//                        notifications.sendEmail("", "", "")

                        result = "redirect:/api/qa/hof/application/details/${permit.id}"
                    }
                    0 -> {
                        redirectAttributes.addFlashAttribute("error", "You have marked the application as incomplete. A notification will be sent to the manufacturer.")
                        if(permit.permitType == 1L) {
                            qualityAssuranceBpmn.qaDmARCheckApplicationComplete(id, hodId, false)
                        } else {
//                            qualityAssuranceBpmn.qaAppReviewCheckIfApplicationComplete(id, status, qaoAssigneeId)
                            permit.userId?.id
                                    ?.let { qualityAssuranceBpmn.qaAppReviewCheckIfApplicationComplete(id, status) }
                        }

                        with(permit) {
                            applicationCompletenessStatus = status
                            deferralFeedback = feedbackData
                        }
                        permitRepository.save(permit)

                        permit.userId?.email?.let {
                            notifications.sendEmail(it, "Permit Application Incompleteness", "Dear, ${permit.userId?.firstName}  ${permit.userId?.firstName} " +
                                    "Review the deferral comment below on your permit application and perform the corrective action." +
                                    "$feedbackData")
                        }

                        result = "redirect:/api/qa/hof"
                    }
                }
            }
            "permit_recommendation" -> {
                when(permitRecommendation) {
                    1 -> {
                        qualityAssuranceBpmn.qaSfMIApprovePermitRecommendationComplete(id, qaoAssigneeId, true)
                        qualityAssuranceBpmn.startQASFPermitAwardProcess(id, pscAssigneeId)
                        redirectAttributes.addFlashAttribute("alert", "You have approved the permit recommendation")
                        with(permit) {
                            hofPermitRecommendation = permitRecommendation
                        }
                        permitRepository.save(permit)
                        result = "redirect:/api/qa/hof"
                    }
                    0 -> {
                        qualityAssuranceBpmn.qaSfMIApprovePermitRecommendationComplete(id, qaoAssigneeId, false)
                        redirectAttributes.addFlashAttribute("alert", "You have disapproved the permit recommendation")
                        with(permit) {
                            hofPermitRecommendation = permitRecommendation
                        }
                        permitRepository.save(permit)
                        result = "redirect:/api/qa/hof"
                    }
                }
            }
            "inspection_report" -> {
                when(inspectionApprovalStatus) {
                    1 -> {
                        with(inspectionReport) {
                            this?.hofApproval = inspectionApprovalStatus
                        }
                        factoryInspectionRepository.save(inspectionReport!!)
                        if(permit.permitType == 1L) {
                            qualityAssuranceBpmn.qaDmARApproveInspectionReportComplete(id, qaoAssigneeId, true)
                        } else {
                            KotlinLogging.logger {  }.info { "This is an SMARK application" }
                        }
                        redirectAttributes.addFlashAttribute("alert", "You have approved the inspection report.")
                        result = "redirect:/api/qa/hof/application/details/${id}"
                    }
                    0 -> {
                        with(inspectionReport) {
                            this?.hofApproval = inspectionApprovalStatus
                        }
                        factoryInspectionRepository.save(inspectionReport!!)
                        if(permit.permitType == 1L) {
                            qualityAssuranceBpmn.qaDmARApproveInspectionReportComplete(id, qaoAssigneeId, false)
                        } else {
                            KotlinLogging.logger {  }.info { "This is an SMARK application" }
                        }
                        redirectAttributes.addFlashAttribute("alert", "You have approved the inspection report.")
                        result = "redirect:/api/qa/hof/application/details/${id}"
                    }
                }
            }
            "availability_of_lab_report"-> {
                when(availabilityOfLabReport) {
                    1 -> {
                        with(permit) {
                            labReportStatus = 1
                        }
                        permitRepository.save(permit)
                        qualityAssuranceBpmn.qaDmARCheckTestReportsComplete(id, qaoAssigneeId, true)
                        redirectAttributes.addFlashAttribute("alert", "You have confirmrd that the lab report is not available")
                        result = "redirect:/api/qao/application/details/${id}"
                    }
                    0 -> {
                        with(permit) {
                            labReportStatus = 0
                        }
                        permitRepository.save(permit)
                        qualityAssuranceBpmn.qaDmARCheckTestReportsComplete(id, qaoAssigneeId, false)
                        redirectAttributes.addFlashAttribute("alert", "You have confirmrd that the lab report is available")
                        result = "redirect:/api/qao/application/details/${id}"
                    }
                }
            }
            "report_compliance_status" -> {
                when(reportComplianceStatus) {
                    1 -> {
                        qualityAssuranceBpmn.qaDmARTestReportsCompliantComplete(id, qaoAssigneeId, true)
                        with(permit){
                            labComplianceStatus = reportComplianceStatus
                        }
                        permitRepository.save(permit)
                        result = "redirect:/api/qao/application/details/${id}"
                    }
                    0 -> {
                        qualityAssuranceBpmn.qaDmARTestReportsCompliantComplete(id, qaoAssigneeId, false)
                        with(permit){
                            labComplianceStatus = reportComplianceStatus
                        }
                        permitRepository.save(permit)

                        qualityAssuranceBpmn.qaDmARFillSSFComplete(id, labId)
                        qualityAssuranceBpmn.qaDmARReceiveSSFComplete(id, qaoAssigneeId)

                        redirectAttributes.addFlashAttribute("alert", "Lab report marked as non-compliant. Submit samples for review.")
                        result = "redirect:/api/qao/application/details/${id}"
                    }
                }
            }
            "justification_report" -> {
                when(approveJustification) {
                    1 -> {
                        with(permit) {
                            justificationApprovalStatus = approveJustification
                        }
                        permitRepository.save(permit)
                        qualityAssuranceBpmn.qaDmasApproveJustificationRptComplete(id, qaoAssigneeId, true)
                        redirectAttributes.addFlashAttribute("alert", "You have marked the report justification as complete.")
                        result = "redirect:/api/qa/hof/application/details/${id}"
                    }
                    0 -> {
                        with(permit) {
                            justificationApprovalStatus = approveJustification
                        }
                        permitRepository.save(permit)
                        qualityAssuranceBpmn.qaDmasApproveJustificationRptComplete(id, qaoAssigneeId, false)
                        redirectAttributes.addFlashAttribute("alert", "You have marked the justification report as incomplete.")
                        result = "redirect:/api/qa/hof/application/details/${id}"
                    }
                }
            }
            "select_assessor" -> {
                with(permit) {
                    allocatedToAssessor = assessorSelectionId
                }
                permitRepository.save(permit)
                assessorSelectionId?.let { qualityAssuranceBpmn.qaDmasAppointAssessorComplete(id, it) }
                redirectAttributes.addFlashAttribute("alert", "You have selected an assessor.")
                result = "redirect:/api/qa/hof"
            }
        }
            return result
    }

//    @PreAuthorize("hasAuthority('ASSIGN_OFFICER')")
    @PostMapping("/api/qa/hof/remark/{id}")
    fun leaveRemarks(
            model: Model,
            @ModelAttribute @Valid remarksEntity: PermitApplicationRemarksEntity,
            redirectAttributes: RedirectAttributes,
            @PathVariable("id") id: Long
    ): String {
        return try {
            SecurityContextHolder.getContext().authentication.name
                    ?.let { user ->
                        usersRepository.findByUserName(user)
                                ?.let { loggedInUser ->
                                    permitRepository.findByIdOrNull(id)
                                            ?.let { foundApplication ->
                                                val man = manufacturerRepository.findByIdOrNull(foundApplication.manufacturer)
                                                model.addAttribute("app", foundApplication)
                                                var remarks = remarksEntity
                                                with(remarks) {
                                                    permitId = foundApplication
                                                    manufacturerid = man
                                                    status = 0
                                                    createdOn = Timestamp.from(Instant.now())
                                                    createdBy = loggedInUser.userName
                                                }
                                                remarks = permitRemarksRepository.save(remarks)

                                                redirectAttributes.addFlashAttribute("remark", remarks)
                                                model.addAttribute("message", "Remark for permit application REF ${foundApplication.id} saved")
                                                "redirect:/api/qa/hof/application/details/{id}"

                                            }
                                            ?: throw NullValueNotAllowedException("Application not found for id=$id")

                                }
                                ?: throw NullValueNotAllowedException("Invalid User")

                    }
                    ?: throw NullValueNotAllowedException("Invalid User")


        } catch (e: Exception) {
            KotlinLogging.logger { }.error { "Caught an error, $e" }
            redirectAttributes.addFlashAttribute("message", "Couldn't save your comment. Try again later")
            "redirect:/api/qa/hof/application/details/{id}"
        }
    }
}

