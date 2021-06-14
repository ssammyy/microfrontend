package org.kebs.app.kotlin.apollo.api.controllers.qaControllers
//
//import mu.KotlinLogging
//import org.kebs.app.kotlin.apollo.adaptor.kafka.producer.service.SendToKafkaQueue
//import org.kebs.app.kotlin.apollo.api.notifications.Notifications
//import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.QualityAssuranceBpmn
//import org.kebs.app.kotlin.apollo.api.ports.provided.dao.QualityAssuranceDaoServices
//import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
//import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
//import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
//import org.kebs.app.kotlin.apollo.store.model.*
//import org.kebs.app.kotlin.apollo.store.model.qa.QaWorkplanEntity
//import org.kebs.app.kotlin.apollo.store.repo.*
//import org.kebs.app.kotlin.apollo.store.repo.di.ILaboratoryRepository
//import org.kebs.app.kotlin.apollo.store.repo.di.ISampleCollectRepository
//import org.kebs.app.kotlin.apollo.store.repo.qa.IPermitTypesEntityRepository
//import org.kebs.app.kotlin.apollo.store.repo.qa.IQaWorkplanRepository
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.data.domain.PageRequest
//import org.springframework.data.repository.findByIdOrNull
//import org.springframework.mail.javamail.JavaMailSender
//import org.springframework.security.access.prepost.PreAuthorize
//import org.springframework.security.core.context.SecurityContextHolder
//import org.springframework.stereotype.Controller
//import org.springframework.ui.Model
//import org.springframework.web.bind.annotation.*
//import org.springframework.web.servlet.mvc.support.RedirectAttributes
//import java.sql.Timestamp
//import java.time.Instant
//import java.time.temporal.ChronoUnit
//import javax.validation.Valid
//
//@Controller
//@RequestMapping("/api/qao")
//class QAQAOController(
//    private val permitRepository: IPermitRepository,
//    applicationMapProperties: ApplicationMapProperties,
//    private val procStatus: IProcessStatusRepository,
//    private val sampleStandardsRepository: ISampleStandardsRepository,
//    private val sendToKafkaQueue: SendToKafkaQueue,
//    private val serviceMapsRepository: IServiceMapsRepository,
//    private val usersRepository: IUserRepository,
//    private val questionnaireSta10Repo: IQuestionnaireSta10Repository,
//    private val sender: JavaMailSender,
//    private val notifications: Notifications,
//    private val applicationQuestionnaireRepo: IApplicationQuestionnaireRepository,
//    private val factoryInspectionRepository: IFactoryInspectionReportRepository,
//    private val factoryVisitRepo: IFactoryVisitsRepository,
//    private val qualityAssuranceBpmn: QualityAssuranceBpmn,
//    private val permitTypesRepo: IPermitTypesEntityRepository,
//    private val manufacturerRepository: IManufacturerRepository,
//    private val daoServices: QualityAssuranceDaoServices,
//    private val labReportsRepository: ILabReportsRepository,
//    private val sampleCollectionDocumentsRepository: ISampleCollectionDocumentsRepository,
//    private val sampleSubmissionDocumentsRepository: ISampleSubmissionDocumentsRepository,
//    private val sampleSubmissionRepository: ISampleSubmissionRepository,
//    private val sampleCollectionRepository: ISampleCollectRepository,
//    private val listOfLabsRepo: ILaboratoryRepository,
//    private val qaWorkplanRepository: IQaWorkplanRepository
//) {
//    val appId = applicationMapProperties.mapPermitApplication
//    val labAssigneeId = applicationMapProperties.labAssigneeId
//    val qaoAssigneeId = applicationMapProperties.qaoAssigneeId
//    val hofId = applicationMapProperties.hofId
//    val hodId = applicationMapProperties.qaHod
//    val pacId = applicationMapProperties.pacAssigneeId
//    val assessorId = applicationMapProperties.assessorId
//    val uploadDir = applicationMapProperties.localFilePath
//
//    val thirtyDayWindow: Int = applicationMapProperties.thirtyDayWindow
//
//    @Value("\${bpmn.qa.sf.mark.inspection.process.definition.key}")
//    lateinit var qaSfMarkInspectionProcessDefinitionKey: String
//    val pscAssigneeId = applicationMapProperties.pscAssigneeId
//
//    @GetMapping("")
//    fun qAOHome(model: Model): String {
//        model.addAttribute("title", "QAO Home")
//        return "quality-assurance/QAO/qao-home"
//    }
//
//    @GetMapping("/applications")
//    fun applications(
//            model: Model,
//            @RequestParam(value = "page", required = false) page: Int?,
//            @RequestParam(value = "records", required = false) records: Int?,
//            @RequestParam(value = "status", required = false) status: Int?,
//            redirectAttributes: RedirectAttributes
//    ): String {
//        var result = ""
//        SecurityContextHolder.getContext().authentication?.name
//                ?.let { username ->
//                    usersRepository.findByUserName(username)
//                            ?.let { loggedInUser ->
//                                loggedInUser.id
//                                        ?.let { usr ->
//                                            qualityAssuranceBpmn.fetchAllTasksByAssignee(usr)?.let { lstTaskDetails ->
//                                                model.addAttribute("tasks", lstTaskDetails)
//                                                //var tasks = mutableListOf<PermitApplicationEntity?>()
//                                                val permitIds = mutableListOf<Long>()
//                                                lstTaskDetails.forEach{ taskDetail->
//                                                    permitIds.add(taskDetail.permitId)
//                                                }
//                                                var tasks = permitRepository.findByIdIsIn(permitIds).toMutableList()
//                                                tasks.sortByDescending { pa-> pa?.id }
//                                                result = if (tasks.size > 0) {
//                                                    redirectAttributes.addFlashAttribute("alert", "You have ${tasks.size} tasks.")
//                                                    model.addAttribute("permitApplications", tasks)
//                                                    "quality-assurance/QAO/applications"
//                                                } else {
//                                                    redirectAttributes.addFlashAttribute("alert", "You have no tasks.")
//                                                    "quality-assurance/QAO/applications"
//                                                }
//                                            }
//                                        }
//                            }
//                }
//        return result
//    }
//
//    /**
//     * Get application details
//     */
//    @GetMapping("/application/details/{id}")
//    fun applicationDetails(model: Model, @PathVariable("id") id: Long, redirectAttributes: RedirectAttributes): String {
//        permitRepository.findByIdOrNull(id)
//                ?.let { pm ->
//                    factoryInspectionRepository.findByPermitApplicationId(pm)
//                            .let { factoryInspection ->
//                                model.addAttribute("factoryInspection", factoryInspection)
//                            }
//                    model.addAttribute("app", pm)
//                    redirectAttributes.addFlashAttribute("pm", pm)
//                    model.addAttribute("visits", FactoryVisitsEntity())
//                    model.addAttribute("std", pm.productSubCategory?.let { sampleStandardsRepository.findBySubCategoryId(it) })
//
//                    // Add users for selection
////                    model.addAttribute("usr", usersRepository.findByIdOrNull(541))
//
////        model.addAttribute("blankFactoryInspectionEntity", FactoryInspectionReportEntity())
////                    model.addAttribute("questionnaire", questionnaireSta10Repo.findByPermitId(pm.id))
//                    questionnaireSta10Repo.findByPermitId(pm.id)
//                            ?.let { questionnaire ->
//                                model.addAttribute("questionnaire", questionnaire)
//                                pm.extraDocuments
//                                        ?.let { documents ->
//                                            val documentList: List<String>? = documents.split(",")
//                                            model.addAttribute("documents", documentList)
//                                        }
//                                questionnaire.certificateFile
//                                        ?.let { certDoc ->
//                                            val documentList: List<String>? = certDoc.split(",")
//                                            model.addAttribute("certDocs", documentList)
//                                        }
//                                questionnaire.testingRecordFile
//                                        ?.let { testingRecordDoc ->
//                                            val documentList: List<String>? = testingRecordDoc.split(",")
//                                            model.addAttribute("testingRecordDocs", documentList)
//                                        }
//                                questionnaire.processMonitoringRecordsFile
//                                        ?.let { testingRecordDoc ->
//                                            val documentList: List<String>? = testingRecordDoc.split(",")
//                                            model.addAttribute("testingRecordDocs", documentList)
//                                        }
//                                questionnaire.frequencyUpload
//                                        ?.let { testingRecordDoc ->
//                                            val documentList: List<String>? = testingRecordDoc.split(",")
//                                            model.addAttribute("testingRecordDocs", documentList)
//                                        }
//                                questionnaire.criticalProcessParametersUpload
//                                        ?.let { criticalProcessParametersUploadDoc ->
//                                            val documentList: List<String>? = criticalProcessParametersUploadDoc.split(",")
//                                            model.addAttribute("criticalProcessParametersUploadDocs", documentList)
//                                        }
//                                questionnaire.operationsUpload
//                                        ?.let { operationsUploadDoc ->
//                                            val documentList: List<String>? = operationsUploadDoc.split(",")
//                                            model.addAttribute("operationsUploadDocs", documentList)
//                                        }
//                                questionnaire.processFlowOfProductionUpload
//                                        ?.let { processFlowOfProductionUploadDoc ->
//                                            val documentList: List<String>? = processFlowOfProductionUploadDoc.split(",")
//                                            model.addAttribute("processFlowOfProductionUploadDocs", documentList)
//                                        }
//                            }
//                    model.addAttribute("title", "Application details")
//
//
//                    pm.id?.let{ permitId->
//                        // Fetch samples collected on details page
//                        sampleCollectionRepository.findByPermitId(permitId)
//                            ?.let {
//                                model.addAttribute("fetchedSampleCollected", it)
//                            }
//                        // Fetch samples submitted on details page
//                        sampleSubmissionRepository.findByPermitId(permitId)
//                            ?.let {
//                                model.addAttribute("fetchedSampleSubmitted", it)
//                            }
//
//                        labReportsRepository.findByPermit(permitId)
//                            ?.let {
//                                println("+++++++++++++++++ -> Adding the lab report to the model with id ${it.id}")
//                                model.addAttribute("labReport", it)
//
//                            }
//
//                    }
////                    model.addAttribute("qn", applicationQuestionnaire.findByPermitId(pm.id))
//                    pm.id?.let {
//                        applicationQuestionnaireRepo.findByPermitId(it)
//                                ?.let { qn ->
//                                    model.addAttribute("qn", qn)
//                                    pm.labReportsFilepath1
//                                            ?.let { labDocs ->
//                                                val labDocList: List<String>? = labDocs.split(",")
//                                                model.addAttribute("labDocs", labDocList)
//                                            }
//                                    pm.extraDocuments
//                                            ?.let { documents ->
//                                                val documentList: List<String>? = documents.split(",")
//                                                model.addAttribute("documents", documentList)
//                                            }
//                                    qn.illustrationFile
//                                            ?.let { illustration ->
//                                                val documentList: List<String>? = illustration.split(",")
//                                                model.addAttribute("illustrations", documentList)
//                                            }
//                                    qn.manufacturingStepsFile
//                                            ?.let { manufacturingSteps ->
//                                                val documentList: List<String>? = manufacturingSteps.split(",")
//                                                model.addAttribute("manufacturingStepsFiles", documentList)
//                                            }
//                                    qn.testsAgainstTheStandardFile
//                                            ?.let { testsAgainstTheStandard ->
//                                                val documentList: List<String>? = testsAgainstTheStandard.split(",")
//                                                model.addAttribute("testsAgainstTheStandardFiles", documentList)
//                                            }
//                                    qn.qualityManualFile
//                                            ?.let { qualityManualFile ->
//                                                val documentList: List<String>? = qualityManualFile.split(",")
//                                                model.addAttribute("qualityManualFiles", documentList)
//                                            }
//                                    qn.testReportsLevelOfDefectivesFile
//                                            ?.let { testReportsLevelOfDefectives ->
//                                                val documentList: List<String>? = testReportsLevelOfDefectives.split(",")
//                                                model.addAttribute("testReportsLevelOfDefectivesFiles", documentList)
//                                            }
//                                }
//                    }
//                    model.addAttribute("permitType", permitTypesRepo.findByIdOrNull(pm.permitType)?.mark)
//
//                    listOfLabsRepo.findAll()
//                            .let { labs ->
//                                model.addAttribute("labs", labs)
//                            }
//                    /**
//                     * Add these objects to the model attribute
//                     * to be used to save ssf and scf
//                     */
//                    model.addAttribute("scfData", CdSampleCollectionEntity())
//                    model.addAttribute("ssfData", CdSampleSubmissionItemsEntity())
//
//
////                    labReportRepository.findByPermitId(pm)
////                            ?.let{ labReport ->
////                                model.addAttribute("labReport", labReport)
////                            }
//                    /*
//                    val labReport: MutableMap<String, String>? = null
//                    model.addAttribute("labReport", labReport)
//                    */
//                    return "quality-assurance/QAO/application-details"
//
//                }
//                ?: throw NullValueNotAllowedException("Permit not found for id=$id")
//
//    }
//
//    /**
//     * Assign compliance status
//     */
//    @GetMapping("/assign_compliance_status")
//    fun assignComplianceStatus(
//            model: Model,
//            @RequestParam("id") id: Long,
//            @RequestParam("appId") appId: Int?,
//            redirectAttributes: RedirectAttributes
//    ): String {
//        serviceMapsRepository.findByIdAndStatus(appId, 1)
//                ?.let { map ->
//                    permitRepository.findByIdOrNull(id)
//                            ?.let { permit ->
//                                permit.labComplianceStatus = map.id.toInt()
//                                permitRepository.save(permit)
//
//                                qualityAssuranceBpmn.qaSfMIAssignComplianceStatusComplete(id, hofId, true)
//
//                                permit.userId?.email
//                                        ?.let {
//                                            notifications.sendEmail(it, "Lab Report Compliance Status", "Dear, ${permit.userId?.firstName} ${permit.userId?.lastName}" +
//                                                    "")
//                                        }
//                                redirectAttributes.addFlashAttribute("message", "You have issued a compliance status")
//                            }
//                }
//        return "redirect:/api/qao/applications"
//
//    }
//
//    /**
//     * Check lab report for completeness
//     */
////    @GetMapping("/lab_report_completeness")
////    fun labReportCompleteness(
////            model: Model,
////            @RequestParam("completenessStatus") completenessStatus: Int,
////            @RequestParam("id") id: Long,
////            redirectAttributes: RedirectAttributes
////    ): String {
////        val permit = permitRepository.findById(id).get()
////        when (completenessStatus) {
////            1 -> {
////                if (permit.permitType != 1L) {
////                    qualityAssuranceBpmn.qaSfMICheckLabReportComplete(id, qaoAssigneeId, labAssigneeId, true)
////                } else {
////                    qualityAssuranceBpmn.qaDmARCheckLabReportsComplete(id, qaoAssigneeId, labAssigneeId, true)
////                }
////
////                with(permit) {
////                    labReportAcceptanceStatus = 1
////                }
////                permitRepository.save(permit)
////
////                redirectAttributes.addFlashAttribute("alert", "You have marked the lab test results as positive")
////            }
////            0 -> {
////                if (permit.permitType != 1L) {
////                    qualityAssuranceBpmn.qaSfMICheckLabReportComplete(id, qaoAssigneeId, labAssigneeId, false)
////                } else {
////                    qualityAssuranceBpmn.qaDmARCheckLabReportsComplete(id, qaoAssigneeId, labAssigneeId, false)
////                }
////
////                with(permit) {
////                    labReportAcceptanceStatus = 0
////                }
////
////                permitRepository.save(permit)
////                redirectAttributes.addFlashAttribute("alert", "You have marked the lab test results as negative. A notification has been sent to the lab.")
////            }
////        }
////        return "redirect:/api/qao/applications"
////    }
//
//    @GetMapping("/factory/inspection/{id}")
//    fun schedulePage(model: Model, @PathVariable("id") id: Long): String {
//        model.addAttribute("blankFactoryInspectionEntity", FactoryInspectionReportEntity())
//        model.addAttribute("app", permitRepository.findByIdOrNull(id))
//        model.addAttribute("title", "Factory inspection form")
//        return "quality-assurance/QAO/factory_inspection_form"
//    }
//
//    /**
//     * Schedule factory visit
//     */
//    @PostMapping("/schedule-factory-visit/{id}")
//    fun scheduleFactoryVisit(model: Model,
//                             @PathVariable("id") id: Long,
//                             @RequestParam(value = "companyName", required = false) companyName: String?,
//                             @RequestParam(value = "appId", required = false) appId: Int?,
//                             @ModelAttribute @Valid factoryVisit: FactoryVisitsEntity,
//                             @ModelAttribute @Valid pm: PermitApplicationEntity,
//                             redirectAttributes: RedirectAttributes
//    ): String {
//        return try {
//            SecurityContextHolder.getContext().authentication.name
//                    ?.let { userName ->
//                        serviceMapsRepository.findByIdOrNull(appId)
//                                ?.let { map ->
//                                    permitRepository.findByIdOrNull(id)
//                                            ?.let { foundApplication ->
//                                                model.addAttribute("app", foundApplication)
//                                                var visit = factoryVisit
//                                                with(visit) {
//                                                    company = companyName
//                                                    permitApplicationId = foundApplication
//                                                    status = map.inactiveStatus.toLong()
//                                                    createdOn = Timestamp.from(Instant.now())
//                                                    createdBy = userName
//                                                }
//                                                visit = factoryVisitRepo.save(visit)
//                                                with(foundApplication) {
//                                                    val ps = procStatus.findByIdOrNull(4)
//                                                    model.addAttribute("processStatus", ps)
//                                                    modifiedOn = Timestamp.from(Instant.now())
//                                                    factoryInspectionScheduleStatus = map.activeStatus
//                                                    processStatus = ps
//                                                }
//                                                permitRepository.save(foundApplication)
//                                                // Schedule visit: BPMN
//                                                /**
//                                                 * StartS/FMARK inspection process
//                                                 */
//                                                if (foundApplication.permitType != 1L) {
//                                                    manufacturerRepository.findByIdOrNull(foundApplication.manufacturer)
//                                                            ?.let { man ->
//                                                                foundApplication.id?.let { man.userId?.id?.let { it1 -> qualityAssuranceBpmn.startQASFMarkInspectionProcess(it, it1) } }
//                                                                        ?.let {
//                                                                            man.userId?.id?.let { it1 -> qualityAssuranceBpmn.qaSfMIGenSupervisionSchemeComplete(foundApplication.id!!, it1) }
//                                                                                    ?.let {
////                                                                                        qualityAssuranceBpmn.qaSfMIAcceptSupervisionScheme(foundApplication.id!!, qaoAssigneeId, true)
////                                                                                        usersRepository.findByIdOrNull(hofId)
////                                                                                                ?.let { hof ->
////                                                                                                    hof.email?.let { email ->
////                                                                                                        notifications.sendEmail(email, "Calender Schedule for ${foundApplication.manufacturerEntity?.name}", "Dear, ${foundApplication.userId?.firstName} ${foundApplication.userId?.firstName}" +
////                                                                                                                "Factory visit for ${foundApplication.manufacturerEntity?.name} has been scheduled for ${visit.dateOfVisit}." +
////                                                                                                                "Review and revert." +
////                                                                                                                "Regards," +
////                                                                                                                "The KIMS team.")
////                                                                                                    }
////                                                                                                }
//                                                                                    }
//                                                                        }
//                                                            }
//                                                } else {
////                                                    foundApplication.allocatedToAssessor?.let {
////                                                    qualityAssuranceBpmn.qaScheduleFactoryVisitComplete(id, assessorId)
////                                                            .let {
////                                                                qualityAssuranceBpmn.qaDmasGenerateAssessmentRptComplete(id, hofId)
////                                                                        .let {
////                                                                            qualityAssuranceBpmn.qaDmasApproveAssessmentRptComplete(id, assessorId, true)
////                                                                                    .let {
////                                                                                        qualityAssuranceBpmn.qaDmasAssessmentRptCompliantComplete(id, pacId, true)
////                                                                                    }
////                                                                        }
////                                                            }
//                                                    KotlinLogging.logger { }.info { "Its a dmark" }
////                                                    }
//                                                }
//
//                                                model.addAttribute("visit", visit)
//                                                redirectAttributes.addFlashAttribute("alert", "Factory visit scheduled")
//                                                "redirect:/api/qao/application/details/{id}"
//
//                                            }
//                                }
//
//                    }
//                    ?: throw NullValueNotAllowedException("Empty user not allowed")
//
//
//        } catch (e: Exception) {
//            model.addAttribute("app", pm)
//            redirectAttributes.addFlashAttribute("message", "Something went wrong. Try again!")
//            "redirect:/api/qao/application/details/{id}"
//        }
//    }
//
//
//    @PostMapping("/send-scheme-of-supervision/{id}")
//    fun saveSchemeOfSupervision(model: Model,
//                                @PathVariable("id") id: Long,
//                                @ModelAttribute @Valid factoryVisit: FactoryVisitsEntity,
//                                @ModelAttribute @Valid pm: PermitApplicationEntity,
//                                redirectAttributes: RedirectAttributes
//    ): String {
//        return try {
//
//            var permit = permitRepository.findById(id).get()
//            val man = manufacturerRepository.findByIdOrNull(permit.manufacturer)
//            with(permit) {
//                schemeOfSupervisionStatus = 1
//                modifiedOn = Timestamp.from(Instant.now())
//            }
//            permit = permitRepository.save(permit)
//
//            permit.userId?.email?.let {
//                notifications.sendEmail(it, "Scheme of Supervision", "Dear, ${permit.userId?.firstName} ${permit.userId?.firstName}" +
//                        "A Scheme of supervision and control has been generated for your permit application." +
//                        "Kindly login to the KIMS platform and review." +
//                        "Regards," +
//                        "The KIMS team.")
//            }
//            qualityAssuranceBpmn.qaSfMIGenSupervisionSchemeComplete(id, qaoAssigneeId)
//            model.addAttribute("message", "Success")
//            "redirect:/api/qao/application/details/{id}"
//        } catch (e: Exception) {
//            model.addAttribute("error", "Something went wrong. Try again!")
//            KotlinLogging.logger { }.error { "Caught an error, $e" }
//            "redirect:/api/qao/application/details/{id}"
//        }
//    }
//
//    @PostMapping("/save-factory-inspection/{id}")
//    fun saveFactoryInspection(
//            model: Model,
//            @PathVariable("id") id: Long,
//            redirectAttributes: RedirectAttributes,
//            @ModelAttribute @Valid factoryInspectionReportEntity: FactoryInspectionReportEntity
//    ): String {
//        return try {
//
//            var foundApplication = permitRepository.findById(id).get()
//            model.addAttribute("app", foundApplication)
//            var factoryInspection = factoryInspectionReportEntity
//            with(factoryInspection) {
//                createdBy = "admin"
//                status = 0
//                createdOn = Timestamp.from(Instant.now())
//                permitApplicationId = foundApplication
//            }
//            factoryInspection = factoryInspectionRepository.save(factoryInspection)
//            KotlinLogging.logger { }.info { "Saved factory inspection report with id ${factoryInspection.id}" }
//            with(foundApplication) {
//                modifiedOn = Timestamp.from(Instant.now())
//                factoryInspectionReportStatus = 1
//            }
//            foundApplication = permitRepository.save(foundApplication)
//            KotlinLogging.logger { }.info { "Saved factory inspection report with id ${foundApplication.id}" }
//
//            /**
//             * Fill factory inspection form BPMN
//             */
//            daoServices.extractUserFromContext(SecurityContextHolder.getContext())
//                    ?.let { user ->
//                        foundApplication.id?.let { user.id?.let { it1 -> qualityAssuranceBpmn.qaSfMIFillFactoryInspectionForms(it, it1, labAssigneeId) } }
//                        foundApplication.id?.let {
//                            user.id?.let { it1 ->
//                                if (foundApplication.permitType == 1L) {
//                                    qualityAssuranceBpmn.qaDmARFactoryInspectionComplete(foundApplication.id!!, qaoAssigneeId)
//                                            .let {
//                                                qualityAssuranceBpmn.qaDmARGenerateInspectionReportComplete(foundApplication.id!!, hodId)
//                                            }
//                                } else {
//                                    qualityAssuranceBpmn.qaSfMIFillFactoryInspectionForms(it, it1, labAssigneeId)
//                                            .let {
//                                                qualityAssuranceBpmn.qaSfMIGenerateInspectionReport(foundApplication.id!!, hofId)
//                                                KotlinLogging.logger { }.info { "Generate inspection report" }
//                                                qualityAssuranceBpmn.qaSfMIReceiveSSFComplete(foundApplication.id!!, qaoAssigneeId)
//                                                KotlinLogging.logger { }.info { "Next task: Submit samples: $qaoAssigneeId" }
//                                            }
//                                }
//
//                            }
//                        }
//                        redirectAttributes.addFlashAttribute("alert", "Saved factory inspection. Await approval from HOD.")
//                    }
//
//            "redirect:/api/qao/application/details/{id}"
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.info { "Caught an error $e" }
//            redirectAttributes.addFlashAttribute("error", "Caught an error")
//            "redirect:/api/qao/application/details/{id}"
//        }
//    }
//
//
//    @PreAuthorize("hasAuthority('SCHEDULE_FACTORY_VISIT') or hasAuthority('ASSESS_DMARK') or hasAuthority('REVIEW_DMARK_ASSESSMENT')")
//    @GetMapping("/generate_inspection_report")
//    fun generateInspectionReport(
//            model: Model,
//            @RequestParam("id") id: Long,
//            @RequestParam("whereTo", required = false) whereTo: String?,
//            @RequestParam("complianceStatus", required = false) complianceStatus: Int?,
//            @RequestParam("surveillanceCompletenessStatus", required = false) surveillanceCompletenessStatus: Int?,
//            @RequestParam("conformityStatusApproval", required = false) conformityStatusApproval: Int?,
//            @RequestParam("justificationScheduledDate", required = false) justificationScheduledDate: String?,
//            @RequestParam("setAssessmentCriterion", required = false) setAssessmentCriterion: String?,
//            @RequestParam("complianceStatusDeferralComments", required = false) complianceStatusDeferralComments: String?,
//            redirectAttributes: RedirectAttributes
//    ): String {
//        var result = ""
//        permitRepository.findByIdOrNull(id)
//                ?.let { permit ->
//                    when (whereTo) {
//                        "justification_schedule" -> {
//                            with(permit) {
//                                justificationScheduleDate = justificationScheduledDate
//                            }
//                            permitRepository.save(permit)
//
//                            qualityAssuranceBpmn.qaScheduleFactoryVisitComplete(id, assessorId)
//                                    .let {
//                                        qualityAssuranceBpmn.qaDmasGenerateAssessmentRptComplete(id, hodId)
//                                                .let {
//                                                    qualityAssuranceBpmn.qaDmasApproveAssessmentRptComplete(id, assessorId, true)
//                                                            .let {
//                                                                qualityAssuranceBpmn.qaDmasAssessmentRptCompliantComplete(id, pacId, true)
//
//                                                                permit.userId?.email
//                                                                        ?.let {
//                                                                            notifications.sendEmail(it, "Factory Visit Schedule", "Dear, ${permit.userId?.firstName} ${permit.userId?.lastName}" +
//                                                                                    "We have scheduled a factory visit for your factory on ${justificationScheduledDate}." +
//                                                                                    "The assessment criterion is af follows." +
//                                                                                    "$setAssessmentCriterion" +
//                                                                                    "" +
//                                                                                    "Regards," +
//                                                                                    "The KIMS team")
//                                                                        }
//
//                                                            }
//                                                }
//                                    }
//
//                            redirectAttributes.addFlashAttribute("alert", "Inspection scheduled")
//                            result = "redirect:/api/qao/application/details/${id}"
//                        }
//                        "inspection_report" -> {
//                            factoryInspectionRepository.findByPermitApplicationId(permit)
//                            permit.id?.let { qualityAssuranceBpmn.qaSfMIGenerateInspectionReport(it, hofId) }
//                            permit.id?.let { qualityAssuranceBpmn.qaSfMIReceiveSSFComplete(it, labAssigneeId) }
//                            result = "redirect:/api/qao/application/details/${id}"
//                        }
//                        "submit_samples" -> {
////                            permit.id?.let { qualityAssuranceBpmn.qaSfMISubmitSamplesComplete(it, labAssigneeId) }
//
//                            if (permit.permitType != 1L) {
//                                qualityAssuranceBpmn.qaSfMISubmitSamplesComplete(id, labAssigneeId)
//                            } else {
//                                qualityAssuranceBpmn.qaDmARCheckTestReportsComplete(id, qaoAssigneeId, false)
//                                        .let {
//                                            qualityAssuranceBpmn.qaDmARFillSSFComplete(id, labAssigneeId)
//                                                    .let {
//                                                        qualityAssuranceBpmn.qaDmARReceiveSSFComplete(id, qaoAssigneeId)
//                                                                .let {
//                                                                    qualityAssuranceBpmn.qaDmARSubmitSamplesComplete(id, labAssigneeId)
//                                                                }
//                                                    }
//                                        }
//                            }
//
////                            qualityAssuranceBpmn.fetchQaAppReviewTasks(labAssigneeId)
////                                    ?.let { tasks ->
////                                        for (taskDetail in tasks) {
////                                            taskDetail.task.let { task ->
////                                                println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
////                                            }
////                                        }
////                                    }
////                                    result = "quality-assurance/QAO/submit_samples"
//                            // For demo only: Samples submission form to be implemented later
//                            redirectAttributes.addFlashAttribute("alert", "Samples submitted. Awaiting for lab results.")
//                            with(permit) {
//                                sampleSubmissionStatus = 1
//                            }
//                            permitRepository.save(permit)
//                            result = "redirect:/api/qao/application/details/${id}"
//                        }
//                        "compliance_status" -> {
//                            when (complianceStatus) {
//                                1 -> {
//                                    if (permit.permitType != 1L) {
//                                        qualityAssuranceBpmn.qaSfMIAssignComplianceStatusComplete(id, hofId, true)
//                                    } else {
//                                        qualityAssuranceBpmn.qaDmARAssignComplianceStatusComplete(id, qaoAssigneeId, true)
//                                    }
//
//                                    // 30 day window with periodic reminders
//                                    permit.userId?.id?.let { qualityAssuranceBpmn.scheduleReminder(thirtyDayWindow, Timestamp.from(Instant.now().plus(120, ChronoUnit.HOURS)), it) }
//                                    permit.userId?.id?.let { qualityAssuranceBpmn.scheduleReminder(thirtyDayWindow, Timestamp.from(Instant.now().plus(240, ChronoUnit.HOURS)), it) }
//                                    permit.userId?.id?.let { qualityAssuranceBpmn.scheduleReminder(thirtyDayWindow, Timestamp.from(Instant.now().plus(360, ChronoUnit.HOURS)), it) }
//                                    permit.userId?.id?.let { qualityAssuranceBpmn.scheduleReminder(thirtyDayWindow, Timestamp.from(Instant.now().plus(480, ChronoUnit.HOURS)), it) }
//                                    permit.userId?.id?.let { qualityAssuranceBpmn.scheduleReminder(thirtyDayWindow, Timestamp.from(Instant.now().plus(600, ChronoUnit.HOURS)), it) }
//                                    permit.userId?.id?.let { qualityAssuranceBpmn.scheduleReminder(thirtyDayWindow, Timestamp.from(Instant.now().plus(720, ChronoUnit.HOURS)), it) }
//
//                                    with(permit) {
//                                        companyComplianceStatus = 1
//                                    }
//                                    permitRepository.save(permit)
//
//                                    permit.id?.let{ permitId->
//                                        labReportsRepository.findByPermit(permitId)
//                                            ?.let { labRpt ->
//                                                permit.userId?.email
//                                                    ?.let {
//                                                        notifications.processEmail(it, "Lab Report Compliance Status", "Dear, ${permit.userId?.firstName} ${permit.userId?.lastName}" +
//                                                                "Your lab report has been awarded a compliance status." +
//                                                                "Regards," +
//                                                                "The KIMS team", "${uploadDir}${labRpt.labReportFilePath}")
//                                                    }
//                                            }
//                                    }
//
//
//
//
//
//                                    redirectAttributes.addFlashAttribute("alert", "You have issued the company a compliance status. Await approval from HOF.")
//                                    result = "redirect:/api/qao/application/details/${id}"
//                                }
//                                0 -> {
//                                    if (permit.permitType != 1L) {
//                                        qualityAssuranceBpmn.qaSfMIAssignComplianceStatusComplete(id, hofId, false)
//                                    } else {
//                                        qualityAssuranceBpmn.qaDmARAssignComplianceStatusComplete(id, qaoAssigneeId, false)
//                                    }
//
//                                    with(permit) {
//                                        companyComplianceStatus = 0
//                                    }
//                                    permitRepository.save(permit)
//                                    redirectAttributes.addFlashAttribute("alert", "You have marked the company as non-compliant. The manufacturer has received a notification for corrective action.")
//
//                                    permit.id?.let{ permitId->
//                                        labReportsRepository.findByPermit(permitId)
//                                            ?.let { labRpt ->
//                                                permit.userId?.email
//                                                    ?.let {
//                                                        notifications.processEmail(it, "Lab Report Compliance Status", "Dear, ${permit.userId?.firstName} ${permit.userId?.lastName}" +
//                                                                "Your lab report has been marked as non-compliant. Please login and review." +
//                                                                "Review the comment below." +
//                                                                "$complianceStatusDeferralComments" +
//                                                                "" +
//                                                                "Regards," +
//                                                                "The KIMS team", "${uploadDir}${labRpt.labReportFilePath}")
//                                                    }
//                                            }
//                                    }
//                                    result = "redirect:/api/qao/application/details/${id}"
//                                }
//                                else -> {
//                                    result = "redirect:/api/qao/application/details/${id}"
//                                    redirectAttributes.addFlashAttribute("error", "We couldn't perform the action. Try again later")
//                                }
//                            }
//                        }
//                        "justification_report" -> {
//                            with(permit) {
//                                justificationReportStatus = 1
//                            }
//                            permitRepository.save(permit)
//                            qualityAssuranceBpmn.startQADmAssessmentProcess(id, hodId)
//                                    ?.let {
//                                        qualityAssuranceBpmn.qaDmasGenJustificationRptComplete(id, hodId)
//                                    }
//                            redirectAttributes.addFlashAttribute("info", "Justification report generated")
//                            result = "redirect:/api/qao/applications"
//                        }
//                        "permit_surveillance" -> {
//                            when (surveillanceCompletenessStatus) {
//                                1 -> {
//                                    with(permit) {
//                                        permitSurveillanceCompletenessStatus = surveillanceCompletenessStatus
//                                    }
//                                    permitRepository.save(permit)
//                                    qualityAssuranceBpmn.qaSfMICheckIfPermitSurveillanceComplete(id, qaoAssigneeId, true)
//                                            .let {
//                                                qualityAssuranceBpmn.qaSfMIFactorySurveillancePlanComplete(id, pscAssigneeId)
//                                            }
//                                    result = "redirect:/api/qao/application/details/${id}"
//                                }
//                                0 -> {
//                                    with(permit) {
//                                        permitSurveillanceCompletenessStatus = surveillanceCompletenessStatus
//                                    }
//                                    permitRepository.save(permit)
//                                    result = "redirect:/api/qao/application/details/${id}"
//                                }
//                                else -> {
//                                    redirectAttributes.addFlashAttribute("error", "Caught an exception.")
//                                    result = "redirect:/api/qao/application/details/${id}"
//                                }
//                            }
//
//                        }
//                        "conformity_status_approval" -> {
//                            when (conformityStatusApproval) {
//                                1 -> {
//                                    with(permit) {
//                                        dmarkConformityStatusApproval = conformityStatusApproval
//                                    }
//                                    redirectAttributes.addFlashAttribute("alert", "You have marked the conformity status of the firm as compliant.")
//                                    permitRepository.save(permit)
//
//                                    permit.manufacturerEntity?.userId.let {
//                                        it?.email?.let { userEmail ->
//                                            notifications.sendEmail(userEmail, "Conformity Status Update", "Dear ${it.firstName}" + " " + "${it.lastName}" +
//                                                    "Your company has been declared as compliant after thorough assessment" +
//                                                    "Regards," +
//                                                    "The KIMS team")
//                                        }
//                                    }
//                                    qualityAssuranceBpmn.qaDmasPacApprovalComplete(id, assessorId, qaoAssigneeId, true)
//                                            .let {
//                                                qualityAssuranceBpmn.qaDmasSurveillanceWorkplanComplete(id)
//                                            }
//
//                                    result = "redirect:/api/qao/applications"
//                                }
//                                0 -> {
//                                    with(permit) {
//                                        dmarkConformityStatusApproval = conformityStatusApproval
//                                    }
//                                    permitRepository.save(permit)
//                                    redirectAttributes.addFlashAttribute("alert", "You have marked the conformity status of the firm as non-compliant.")
//                                    result = "redirect:/api/qao/applications"
//                                    qualityAssuranceBpmn.qaDmasPacApprovalComplete(id, assessorId, qaoAssigneeId, false)
//
//                                    permit.manufacturerEntity?.userId.let {
//                                        it?.email?.let { userEmail ->
//                                            notifications.sendEmail(userEmail, "Conformity Status Update", "Dear ${it.firstName}" + " " + "${it.lastName}" +
//                                                    "Your company has been declared as non-compliant after thorough assessment" +
//                                                    "Regards," +
//                                                    "The KIMS team")
//                                        }
//                                    }
//                                }
//                                else -> {
//                                    redirectAttributes.addFlashAttribute("error", "Caught an exception.")
//                                    result = "redirect:/api/qao/applications"
//                                }
//                            }
//                        }
//                        else -> {
//                            result = "redirect:/api/qao/application/details/${id}"
//                            redirectAttributes.addFlashAttribute("error", "We couldn't perform the action. Try again later")
//                        }
//                    }
//
//
//                }
//
//
//
//        return result
//    }
//
//    /**
//     * Submit Lab samples
//     */
//
//    @GetMapping("/lab")
//    fun labHome(
//            model: Model,
//            @RequestParam(value = "page", required = false) page: Int?,
//            @RequestParam(value = "records", required = false) records: Int?,
//            @RequestParam(value = "status", required = false) status: Int?
//    ): String {
//        var result = "quality-assurance/Lab/home"
//        SecurityContextHolder.getContext().authentication?.name
//                ?.let { username ->
//                    usersRepository.findByUserName(username)
//                            ?.let { loggedInUser ->
//                                loggedInUser.id
//                                        ?.let { usr ->
//                                            qualityAssuranceBpmn.fetchAllTasksByAssignee(usr)?.let { lstTaskDetails ->
//                                                model.addAttribute("tasks", lstTaskDetails)
//                                                val tasks = mutableListOf<PermitApplicationEntity?>()
//                                                lstTaskDetails.sortedByDescending { it.permitId }.forEach { taskDetails ->
//                                                    taskDetails.task.let {
//                                                        result = if (permitRepository.findByIdOrNull(taskDetails.permitId) == null) {
//                                                            "redirect:/"
//                                                        } else {
////                                                            println("${taskDetails.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
//                                                            tasks.add(permitRepository.findByIdOrNull(taskDetails.permitId))
//                                                            model.addAttribute("permitApplications", tasks)
//                                                            "quality-assurance/Lab/home"
//                                                        }
//                                                    }
//                                                }
//                                            }
//                                        }
//                            }
//                }
//        return result
//    }
//
//    @PreAuthorize("hasAuthority('GENERATE_BS_NUMBER')")
//    @GetMapping("/generate_bs")
//    fun genBs(
//            model: Model,
//            redirectAttributes: RedirectAttributes,
//            @RequestParam("appId") appId: Int?,
//            @RequestParam("id") id: Long
//    ): String {
//        serviceMapsRepository.findByIdOrNull(appId)
//                ?.let { map ->
//                    permitRepository.findByIdOrNull(id)
//                            ?.let { permit ->
//                                permit.bsNumber = generateRandomText(map.transactionRefLength, map.secureRandom, map.messageDigestAlgorithm, false)
//                                permitRepository.save(permit)
//                            }
//                }
//        return "redirect:/api/qao/lab"
//    }
//
//
//    @GetMapping("/inspection_report")
//    fun generateInspectionReportPdf(
//            model: Model,
//            @RequestParam("id") id: Long
//    ): String {
//        factoryInspectionRepository.findByPermitApplicationId(permitRepository.findByIdOrNull(id))
//                ?.let { fr ->
//                    model.addAttribute("report", fr)
//                    qualityAssuranceBpmn.qaSfMIGenerateInspectionReport(id, hofId)
//                    qualityAssuranceBpmn.qaSfMIReceiveSSFComplete(id, labAssigneeId)
//                }
//        return "quality-assurance/QAO/inspection_report"
//    }
//
//    @PreAuthorize("hasAuthority('SCHEDULE_FACTORY_VISIT') or hasAuthority('ASSESS_DMARK') or hasAuthority('REVIEW_DMARK_ASSESSMENT')")
//    @GetMapping("/workplan")
//    fun getWorkplanPage(
//            model: Model,
//            @RequestParam("whereTo") whereTo: String,
//            redirectAttributes: RedirectAttributes,
//            @RequestParam("page") page: Int?,
//            @RequestParam("records") records: Int?,
//            @RequestParam("workplanId", required = false) workplanId: Long?
//    ): String {
//        var result = ""
//        serviceMapsRepository.findByIdOrNull(appId)
//                ?.let { map ->
//                    when (whereTo) {
//                        "prepare_workplan_page" -> {
//                            val pageable = PageRequest.of(page ?: 1, records ?: 10)
//                            permitRepository.findByPermitSurveillanceCompletenessStatus(null, pageable)
//                                    ?.let { permits ->
//                                        model.addAttribute("permits", permits)
//                                        model.addAttribute("workplan", QaWorkplanEntity())
//                                        result = "quality-assurance/QAO/new-workplan"
//                                    }
//                        }
//                        "single_workplan" -> {
//                            qaWorkplanRepository.findByIdOrNull(workplanId)
//                                    ?.let { workplan ->
//                                        KotlinLogging.logger {  }.info { workplan.permits }
//                                        workplan.permits.toString().let { pms ->
//                                            val newList: List<String> = pms.split(",")
//                                            newList.let { permitList ->
//                                                model.addAttribute("permitList", permitList)
//                                                KotlinLogging.logger {  }.info { "Permit list, $permitList" }
//                                                model.addAttribute("workplan", workplan)
//                                                result = "quality-assurance/QAO/workplan"
//                                            }
//
//                                        }
//                                    }
//                        }
//                        "workplans" -> {
//                            daoServices.extractUserFromContext(SecurityContextHolder.getContext())
//                                    ?.let { user ->
//                                        val pageable = PageRequest.of(page ?: 1, records ?: 10)
//                                        //                                        qaWorkplanRepository.findAllByUserIdOrderById(user, pageable)
//                                        qaWorkplanRepository.findByUserId(user)
//                                                ?.let { workplans ->
//                                                    model.addAttribute("workplans", workplans)
//                                                    result = "quality-assurance/QAO/applications"
//                                                }
//
//                                    }
//                        }
//                        else -> {
//                            redirectAttributes.addFlashAttribute("error", "Caught an exception")
//                            result = "quality-assurance/QAO/new-workplan"
//                        }
//                    }
//                }
//        return result
//    }
//
//    @PreAuthorize("hasAuthority('SCHEDULE_FACTORY_VISIT') or hasAuthority('ASSESS_DMARK') or hasAuthority('REVIEW_DMARK_ASSESSMENT')")
//    @PostMapping("/new-workplan")
//    fun saveWorkplan(
//        model: Model,
//        @RequestParam("permitId") permitId: Array<String>,
//        @ModelAttribute("workplan") workplan: QaWorkplanEntity,
//        redirectAttributes: RedirectAttributes
//    ): String {
//        var result = ""
//
//        serviceMapsRepository.findByIdOrNull(appId)
//                ?.let { map ->
//                    daoServices.extractUserFromContext(SecurityContextHolder.getContext())
//                            ?.let { user ->
//                                val pList = mutableListOf<String>()
//                                permitId.forEach { p ->
//                                    pList.add(p)
//                                }
//
//                                with(workplan) {
//                                    status = map.activeStatus
//                                    permits = pList.toString().replace("[", "").replace("]", "")
//                                    userId = user
//                                    createdBy = "${user.firstName} ${user.lastName}"
//                                    createdOn = Timestamp.from(Instant.now())
//                                }
//                                qaWorkplanRepository.save(workplan)
//                                redirectAttributes.addFlashAttribute("alert", "You have successfully generated a workplan.")
//                                result = "redirect:/api/permit?appId=${map.id}"
//                            }
//
//                }
//        return result
//    }
//
//
//}