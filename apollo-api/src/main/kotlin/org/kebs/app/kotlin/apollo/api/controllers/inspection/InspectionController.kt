//<<<<<<< HEAD
////package org.kebs.app.kotlin.apollo.api.controllers.inspection
////
////import mu.KotlinLogging
////import org.kebs.app.kotlin.apollo.api.notifications.Notifications
////import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.QualityAssuranceBpmn
////import org.kebs.app.kotlin.apollo.api.ports.provided.dao.QualityAssuranceDaoServices
////import org.kebs.app.kotlin.apollo.common.exceptions.StorageException
////import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
////import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
////import org.kebs.app.kotlin.apollo.store.model.*
////import org.kebs.app.kotlin.apollo.store.repo.*
////import org.springframework.data.repository.findByIdOrNull
////import org.springframework.security.core.context.SecurityContextHolder
////import org.springframework.stereotype.Controller
////import org.springframework.ui.Model
////import org.springframework.util.StringUtils
////import org.springframework.web.bind.annotation.GetMapping
////import org.springframework.web.bind.annotation.ModelAttribute
////import org.springframework.web.bind.annotation.PostMapping
////import org.springframework.web.bind.annotation.RequestParam
////import org.springframework.web.multipart.MultipartFile
////import org.springframework.web.servlet.mvc.support.RedirectAttributes
////import java.lang.StringBuilder
////import java.sql.Date
////import java.sql.Timestamp
////import java.time.Instant
////import java.util.*
////import javax.servlet.http.HttpServletResponse
////
////
////@Controller
////class InspectionController(
////        private val serviceMapsRepository: IServiceMapsRepository,
////        applicationMapProperties: ApplicationMapProperties,
////        private val sampleSubmissionRepository: ISampleSubmissionRepository,
////        private val sampleCollectionRepository: ISampleCollectRepository,
////        private val qualityAssuranceDaoServices: QualityAssuranceDaoServices,
////        private val permitRepo: IPermitRepository,
////        private val notifications: Notifications,
////        private val usersRepository: IUserRepository,
////        private val workPlanRepo: IWorkPlanGenerateRepository,
////        private val sampleCollectionDocumentsRepository: ISampleCollectionDocumentsRepository,
////        private val sampleSubmissionDocumentsRepository: ISampleSubmissionDocumentsRepository,
////        private val labReportDocumentsRepo: ILabReportDocumentsRepository,
////        private val labReportsRepository: ILabReportsRepository,
////        private val listOfLabsRepo: ILaboratoryRepository,
////        private val qualityAssuranceBpmn: QualityAssuranceBpmn
////) {
////    val appId = applicationMapProperties.mapPermitApplication
////
////    val labAssigneeId = applicationMapProperties.labAssigneeId
////    val qaoAssigneeId = applicationMapProperties.qaoAssigneeId
////    val hofId = applicationMapProperties.hofId
////    val hodId = applicationMapProperties.qaHod
////    val pacId = applicationMapProperties.pacAssigneeId
////    val assessorId = applicationMapProperties.assessorId
////    val uploadDir = applicationMapProperties.localFilePath
////
////    /**
////     * SCF (QA, MS)
////     * SSF
////     * Generate BS Number (LIMS)
////     * Analyze samples
////     * Lab report
////     * Approve lab report
////     */
////    @GetMapping("/inspection")
////    fun generalInspection(
////            model: Model,
////            response: HttpServletResponse,
////            @RequestParam("sampleCollectedId", required = false) sampleCollectedId: Long?,
////            @RequestParam("sampleSubmittedId", required = false) sampleSubmittedId: Long?,
////            redirectAttributes: RedirectAttributes,
////            @RequestParam("whereTo", required = true) whereTo: String?,
////            @RequestParam("labReportId", required = false) labReportId: Long?,
////            @RequestParam(value = "page", required = false) page: Int?,
////            @RequestParam(value = "records", required = false) records: Int?,
////            @RequestParam("ssf_id", required = false) ssf_id: Long?,
////            @RequestParam("completenessStatus", required = false) completenessStatus: Int?,
////            @RequestParam("permitId", required = false) permitId: Long?,
////            @RequestParam("workPlanId", required = false) workPlanId: Long?,
////            @RequestParam("labReportCompletenessStatus", required = false) labReportCompletenessStatus: Int?
////    ): String {
////        var result = ""
////        serviceMapsRepository.findByIdOrNull(appId)
////                ?.let { map ->
////                    when (whereTo) {
////                        "lab_report_completeness" -> {
////                            sampleSubmissionRepository.findByIdOrNull(ssf_id)
////                                    ?.let { sample ->
////                                        labReportsRepository.findByIdOrNull(labReportId)
////                                                ?.let { report ->
////                                                    when (sample.objectId) {
////                                                        1 -> {
////                                                            permitRepo.findByIdOrNull(permitId)
////                                                                    ?.let { permit ->
////                                                                        with(sample) {
////                                                                            samplesCompleteness = completenessStatus
////                                                                        }
////                                                                        sampleSubmissionRepository.save(sample)
////
////                                                                        with(report) {
////                                                                            labReportCompletenessStatuss = completenessStatus
////                                                                        }
////                                                                        labReportsRepository.save(report)
////
////                                                                        with(permit) {
////                                                                            labReportAcceptanceStatus = completenessStatus
////                                                                        }
////                                                                        permitRepo.save(permit)
////
////                                                                        if (permit.permitType != map.activeStatus.toLong()) {
////                                                                            permitId?.let { qualityAssuranceBpmn.qaSfMICheckLabReportComplete(it, qaoAssigneeId, labAssigneeId, true) }
////                                                                        } else {
////                                                                            permitId?.let { qualityAssuranceBpmn.qaDmARCheckLabReportsComplete(it, qaoAssigneeId, labAssigneeId, true) }
////                                                                        }
////                                                                        redirectAttributes.addFlashAttribute("alert", "You have marked the lab test results as positive")
////                                                                        result = "redirect:/api/qao/application/details/${permitId}"
////                                                                    }
////                                                        }
////                                                        2 -> {
////
////                                                        }
////                                                        else -> {
////                                                            redirectAttributes.addFlashAttribute("error", "Invalid parameter passed.")
////                                                        }
////                                                    }
////                                                }
////
////                                    }
////                        }
////                        /**
////                         * Load lab ssf details
////                         */
////                        "ssf_details" -> {
////                            sampleSubmissionRepository.findByIdOrNull(ssf_id)
////                                    ?.let { single_ssf ->
////                                        model.addAttribute("single_ssf", single_ssf)
////
////
////                                        labReportsRepository.findBySampleSubmissionEntity((single_ssf))
////                                                ?.let { labReport ->
////                                                    model.addAttribute("labReport", labReport)
////                                                }
////
////                                        model.addAttribute("labData", LabReportsEntity())
////                                    }
////                            result = "quality-assurance/Lab/details"
////                        }
////                        /**
////                         * Load Lab home page with list of tasks
////                         */
////                        "lab_home" -> {
////                            SecurityContextHolder.getContext().authentication?.name
////                                    ?.let { username ->
////                                        usersRepository.findByUserName(username)
////                                                ?.let { loggedInUser ->
////                                                    loggedInUser.id
////                                                            ?.let {
////                                                                sampleSubmissionRepository.findAll()
////                                                                        .let { foundSsfs ->
////                                                                            model.addAttribute("ssf", foundSsfs)
////                                                                            result = "quality-assurance/Lab/home"
////                                                                        }
////
////                                                            }
////                                                }
////                                    }
////                        }
////                        /**
////                         * Generate BS Number
////                         */
////                        "generate_bs" -> {
////                            sampleSubmissionRepository.findByIdOrNull(ssf_id)
////                                    ?.let { single_ssf ->
////                                        with(single_ssf) {
////                                            bsNumber = generateRandomText(map.transactionRefLength, map.secureRandom, map.messageDigestAlgorithm, false)
////                                        }
////                                        sampleSubmissionRepository.save(single_ssf)
////
////                                        result = "redirect:/inspection?whereTo=ssf_details&ssf_id=${ssf_id}"
////                                        redirectAttributes.addFlashAttribute("alert", "BS No. Generated")
////                                    }
////                        }
////                        "samples_completeness" -> {
////                            permitRepo.findByIdOrNull(permitId)
////                                    ?.let { permit ->
////                                        sampleSubmissionRepository.findByIdOrNull(ssf_id)
////                                                ?.let { ss ->
////                                                    when (completenessStatus) {
////                                                        1 -> {
////                                                            if (permit.permitType != 1L) {
////                                                                permitId?.let { qualityAssuranceBpmn.qaSfMIAnalyzeSamplesComplete(it, qaoAssigneeId) }
////                                                            } else {
////                                                                permitId?.let { qualityAssuranceBpmn.qaDmARLabAnalysisComplete(it, qaoAssigneeId) }
////                                                            }
////                                                            with(ss) {
////                                                                samplesCompleteness = map.activeStatus
////                                                            }
////                                                            sampleSubmissionRepository.save(ss)
////
////                                                            with(permit) {
////                                                                labReportStatus = completenessStatus
////                                                            }
////                                                            permitRepo.save(permit)
////                                                            redirectAttributes.addFlashAttribute("alert", "You have marked the lab samples as complete")
////                                                        }
////                                                        0 -> {
////                                                            permitId?.let { qualityAssuranceBpmn.qaSfMIAnalyzeSamplesComplete(it, qaoAssigneeId) }
////                                                            with(permit) {
////                                                                labReportStatus = completenessStatus
////                                                            }
////                                                            permitRepo.save(permit)
////                                                            redirectAttributes.addFlashAttribute("alert", "You have marked the lab samples as incomplete. A notification has been sent to KEBS.")
////                                                        }
////                                                        else -> {
////                                                            KotlinLogging.logger { }.info { "Invalid parameter passed" }
////                                                        }
////                                                    }
////                                                }
////                                    }
////
////                        }
////
////                        // Download sample submitted
////                        "download_sample_submitted" -> {
////                            sampleSubmissionRepository.findByIdOrNull(sampleSubmittedId)
////                                    ?.let { ssEnt ->
////                                        sampleSubmissionDocumentsRepository.findBySampleSubmissionEntity(ssEnt)
////                                                ?.let { sampleSubmissionDoc ->
////                                                    response.contentType = sampleSubmissionDoc.fileType
////                                                    response.addHeader("Content-Dispostion", "inline; filename=${sampleSubmissionDoc.name};")
////                                                    response.outputStream
////                                                            .let { responseOutputStream ->
////                                                                sampleSubmissionDoc.document?.let { responseOutputStream.write(it) }
////                                                                responseOutputStream.close()
////                                                            }
////                                                }
////                                    }
////                        }
////
////                        // Download sample collected
////                        "download_sample_collected" -> {
////                            sampleCollectionRepository.findByIdOrNull(sampleCollectedId)
////                                    ?.let { scEnt ->
////                                        sampleCollectionDocumentsRepository.findBySampleCollectionEntity(scEnt)
////                                                ?.let { sampleCollectednDoc ->
////                                                    response.contentType = sampleCollectednDoc.fileType
////                                                    response.addHeader("Content-Dispostion", "inline; filename=${sampleCollectednDoc.name};")
////                                                    response.outputStream
////                                                            .let { responseOutputStream ->
////                                                                sampleCollectednDoc.document?.let { responseOutputStream.write(it) }
////                                                                responseOutputStream.close()
////                                                            }
////                                                }
////                                    }
////                        }
////
////                        // Download lab report
////                        "download_lab_report" -> {
////                            labReportsRepository.findByIdOrNull(labReportId)
////                                    ?.let { labRpt ->
////                                        labReportDocumentsRepo.findByLabReport(labRpt)
////                                                ?.let { labRptDoc ->
////                                                    response.contentType = labRptDoc.fileType
////                                                    response.addHeader("Content-Dispostion", "inline; filename=${labRptDoc.name};")
////                                                    response.outputStream
////                                                            .let { responseOutputStream ->
////                                                                labRptDoc.document?.let { responseOutputStream.write(it) }
////                                                                responseOutputStream.close()
////                                                            }
////                                                }
////                                    }
////                        }
////
////                        else -> {
////                            result = ""
////                        }
////                    }
////                }
////        return result
////    }
////
////
////    @PostMapping("/inspection/save")
////    fun saveInspectionData(
////            model: Model,
////            redirectAttributes: RedirectAttributes,
////            @RequestParam("whereTo", required = false) whereTo: String?,
////            @ModelAttribute("scfData") scfData: CdSampleCollectionEntity?,
////            @RequestParam("labReportFile", required = false) labReportFile: MultipartFile?,
////            @RequestParam("sampleCollectionFile", required = false) sampleCollectionFile: MultipartFile?,
////            @RequestParam("sampleSubmissionFile", required = false) sampleSubmissionFile: MultipartFile?,
////            @ModelAttribute("ssfData") ssfData: CdSampleSubmissionItemsEntity?,
////            @RequestParam("pId", required = false) pId: Long?,
////            @RequestParam("ss_id", required = false) ss_id: Long?,
////            @RequestParam("selectLab", required = false) selectLab: Long?,
////            @RequestParam("objId", required = false) objId: Int?,
////            @RequestParam("workplanId", required = false) workplanId: Long?,
////            @ModelAttribute("labReport") labReport: LabReportsEntity?,
////            @ModelAttribute("sampleCollectionDocumentsEntity") sampleCollectionDocumentsEntity: SampleCollectionDocumentsEntity?,
////            @ModelAttribute("sampleSubmissionDocumentsEntity") sampleSubmissionDocumentsEntity: SampleSubmissionDocumentsEntity?,
////            @ModelAttribute("labReportDocumentsEntity") labReportDocumentsEntity: LabReportDocumentsEntity?
////    ): String {
////        var result = ""
////        serviceMapsRepository.findByIdOrNull(appId)
////                .let { map ->
////                    when (whereTo) {
////                        /**
////                         * Save sample submission form
////                         */
////                        "save_ssf" -> {
////                            qualityAssuranceDaoServices.extractUserFromContext(SecurityContextHolder.getContext())
////                                    ?.let { user ->
////                                        when (objId) {
////                                            map?.activeStatus -> {
////                                                permitRepo.findByIdOrNull(pId)
////                                                        ?.let { pm ->
////
////                                                            var submittedSample = ssfData
////                                                            with(submittedSample) {
////                                                                this?.status = map?.inactiveStatus
////                                                                this?.createdOn = Timestamp.from(Instant.now())
////                                                                this?.createdBy = user.userName
////                                                                this?.permitId = pm
////                                                                this!!.objectId = objId
////                                                                selectedLab = listOfLabsRepo.findByIdOrNull(selectLab)
////                                                            }
////                                                            submittedSample?.let { submittedSample = sampleSubmissionRepository.save(it) }
////
////                                                            // Save sample submission documents
////                                                            var stgSampleSubmissionDocs = sampleSubmissionDocumentsEntity
////                                                            with(stgSampleSubmissionDocs) {
////                                                                this?.name = sampleCollectionFile?.let { qualityAssuranceDaoServices.saveDocuments(null, null, stgSampleSubmissionDocs,null,it) }
////                                                                this?.fileType = sampleCollectionFile?.contentType
////                                                                this?.documentType = "SAMPLE_SUBMISSION"
////                                                                this?.document = sampleCollectionFile?.bytes
////                                                                this?.transactionDate = Date(Date().time)
////                                                                this?.status = map?.activeStatus
////                                                                this?.createdBy = user.userName
////                                                                this?.createdOn = Timestamp.from(Instant.now())
////                                                                this?.permitId = pm
////                                                                this?.sampleSubmissionEntity = submittedSample
////                                                            }
////                                                            stgSampleSubmissionDocs?.let { stgSampleSubmissionDocs = sampleSubmissionDocumentsRepository.save(it) }
////
////                                                            with(pm) {
////                                                                sampleSubmittedStatus = map?.activeStatus
////                                                                sampleSubmittedId = submittedSample
////                                                            }
////                                                            permitRepo.save(pm)
////
////                                                            if (pm.permitType != 1L) {
////                                                                pm.id?.let { qualityAssuranceBpmn.qaSfMISubmitSamplesComplete(it, labAssigneeId) }
////                                                            } else {
////                                                                pm.id?.let { pId ->
////                                                                    qualityAssuranceBpmn.qaDmARCheckTestReportsComplete(pId, qaoAssigneeId, false)
////                                                                            .let {
////                                                                                qualityAssuranceBpmn.qaDmARFillSSFComplete(pId, labAssigneeId)
////                                                                                        .let {
////                                                                                            qualityAssuranceBpmn.qaDmARReceiveSSFComplete(pId, qaoAssigneeId)
////                                                                                                    .let {
////                                                                                                        qualityAssuranceBpmn.qaDmARSubmitSamplesComplete(pId, labAssigneeId)
////                                                                                                    }
////                                                                                        }
////                                                                            }
////                                                                }
////                                                            }
////
////                                                            redirectAttributes.addFlashAttribute("alert", "You have submitted your samples. Await for the BS no. to attach to the samples.")
////                                                            result = "redirect:/api/qao/applications"
////                                                        }
////                                            }
////                                            2 -> {
////                                                workPlanRepo.findByIdOrNull(workplanId)
////                                                        ?.let { workplan ->
////
////                                                            var submittedSample = ssfData
////                                                            with(submittedSample) {
////                                                                this?.status = map?.inactiveStatus
////                                                                this?.createdOn = Timestamp.from(Instant.now())
////                                                                this?.createdBy = user.userName
////                                                                this?.workplanId = workplan
////                                                                this!!.objectId = objId
////                                                            }
////                                                            submittedSample?.let { submittedSample = sampleSubmissionRepository.save(it) }
////
////
////                                                            // Save sample submission documents
////                                                            var stgSampleSubmissionDocs = sampleSubmissionDocumentsEntity
////                                                            with(stgSampleSubmissionDocs) {
////                                                                this?.name = sampleCollectionFile?.let { qualityAssuranceDaoServices.saveDocuments(null, null, stgSampleSubmissionDocs, null,it) }
////                                                                this?.fileType = sampleCollectionFile?.contentType
////                                                                this?.documentType = "SAMPLE_SUBMISSION"
////                                                                this?.document = sampleCollectionFile?.bytes
////                                                                this?.transactionDate = Date(Date().time)
////                                                                this?.status = map?.activeStatus
////                                                                this?.createdBy = user.userName
////                                                                this?.createdOn = Timestamp.from(Instant.now())
////                                                                this?.workplanId = workplan
////                                                                this?.sampleSubmissionEntity = submittedSample
////                                                            }
////                                                            stgSampleSubmissionDocs?.let { stgSampleSubmissionDocs = sampleSubmissionDocumentsRepository.save(it) }
////
////                                                            with(workplan) {
////                                                                sampleSubmittedStatus = map?.activeStatus
////                                                                sampleSubmittedId = submittedSample
////                                                            }
////                                                            workPlanRepo.save(workplan)
////
////                                                            redirectAttributes.addFlashAttribute("alert", "You have submitted your samples. Await for the BS no. to attach to the samples.")
////                                                            result = "redirect:/api/ms/workPlan-detail?workPlanId=${workplanId}"
////
////
////                                                        }
////                                            }
////                                            else -> KotlinLogging.logger { }.info { "Exception" }
////                                        }
////
////                                    }
////                        }
////                        /**
////                         * Save Sample collection form
////                         */
////                        "save_scf" -> {
////                            qualityAssuranceDaoServices.extractUserFromContext(SecurityContextHolder.getContext())
////                                    ?.let { user ->
////                                        permitRepo.findByIdOrNull(pId)
////                                                ?.let { pm ->
////                                                    // Save sample collection entity
////                                                    var sampleCollected = scfData
////                                                    with(sampleCollected) {
////                                                        this?.status = map?.inactiveStatus
////                                                        this?.createdBy = user.userName
////                                                        this?.createdOn = Timestamp.from(Instant.now())
////                                                        this?.permitId = pm
////
////                                                    }
////                                                    sampleCollected?.let { sampleCollected = sampleCollectionRepository.save(it) }
////
////                                                    // Save sample collection documents (Blob)
////                                                    var stgSampleCollectedDocs = sampleCollectionDocumentsEntity
////                                                    with(stgSampleCollectedDocs) {
////
////                                                        this?.name = sampleCollectionFile?.let { qualityAssuranceDaoServices.saveDocuments(null,sampleCollectionDocumentsEntity, null, null,it) }
////
////                                                        this?.fileType = sampleCollectionFile?.contentType
////                                                        this?.documentType = "SAMPLE_COLLECTION"
////                                                        this?.document = sampleCollectionFile?.bytes
////                                                        this?.transactionDate = Date(Date().time)
////                                                        this?.status = map?.activeStatus
////                                                        this?.createdBy = user.userName
////                                                        this?.createdOn = Timestamp.from(Instant.now())
////                                                        this?.permitId = pm
////                                                        this?.sampleCollectionEntity = sampleCollected
////                                                    }
////                                                    stgSampleCollectedDocs?.let { stgSampleCollectedDocs = sampleCollectionDocumentsRepository.save(it) }
////
////                                                    // Update permit
////                                                    with(pm) {
////                                                        sampleCollectionStatus = map?.activeStatus
////                                                    }
////                                                    permitRepo.save(pm)
////
////
////                                                    redirectAttributes.addFlashAttribute("alert", "You have submitted your sample collection form. You can now upload the sample submission form.")
////                                                    result = "redirect:/api/qao/application/details/${pId}"
////                                                }
////                                    }
////
////                        }
////                        /**
////                         * Save lab report form
////                         */
////                        "save_lab_report" -> {
////                            qualityAssuranceDaoServices.extractUserFromContext(SecurityContextHolder.getContext())
////                                    ?.let { user ->
////
////                                        permitRepo.findByIdOrNull(pId)
////                                                ?.let { permit ->
////                                                    sampleSubmissionRepository.findByIdOrNull(ss_id)
////                                                            ?.let { ss ->
////
////                                                                with(permit) {
////                                                                    labSamplesAnalysisComplete = map?.activeStatus
////                                                                    labReportStatus = map?.activeStatus
////                                                                }
////                                                                permitRepo.save(permit)
////
////                                                                var newLabReport = labReport
////                                                                with(newLabReport) {
////                                                                    this?.status = map?.inactiveStatus
////                                                                    this?.createdBy = user.userName
////                                                                    this?.createdOn = Timestamp.from(Instant.now())
////                                                                    this?.permit = permit
////                                                                    this?.sampleSubmissionEntity = ss
////                                                                    labReportFile
////                                                                            ?.let { labFile ->
////                                                                                qualityAssuranceDaoServices.storeFile(labFile)
////                                                                                val labFileName = StringBuilder()
////                                                                                labFile.originalFilename
////                                                                                        .let { labFileOriginalFileName ->
////                                                                                            labFileName.append(labFileOriginalFileName)
////                                                                                        }
////                                                                                this?.labReportFilePath = labFileName.toString()
////                                                                            }
////
////                                                                }
////
////                                                                permit.userId?.email?.let {
////                                                                    notifications.processEmail(it,"Lab Report","\n" +
////                                                                            "Dear, ${permit.userId?.firstName} ${permit.userId?.lastName}.\n" +
////                                                                            "A lab report for your permit application is ready for viewing.\n","${uploadDir}${labReportFile?.originalFilename}")
////                                                                }
////
////                                                                labReport?.let { rpt -> newLabReport = labReportsRepository.save(rpt) }
////
////
////                                                                with(labReportDocumentsEntity) {
////                                                                    labReportDocumentsEntity?.let { rpt -> this?.name = labReportFile?.let { qualityAssuranceDaoServices.saveDocuments(rpt, null, null, null,it) } }
////                                                                    this?.fileType = labReportFile?.contentType
////                                                                    this?.documentType = "LAB_REPORT"
////                                                                    this?.document = labReportFile?.bytes
////                                                                    this?.transactionDate = Date(Date().time)
////                                                                    this?.status = map?.activeStatus
////                                                                    this?.createdBy = user.userName
////                                                                    this?.createdOn = Timestamp.from(Instant.now())
////                                                                    this?.permitId = permit
////                                                                    this?.labReport = newLabReport
////                                                                    this?.sampleSubmissionEntity = ss
////                                                                }
////                                                                labReportDocumentsEntity?.let { generatedRpt -> labReportDocumentsRepo.save(generatedRpt) }
////
////                                                                permit.userId?.email?.let {
////                                                                    notifications.sendEmail(it, "Lab Report Availability", "Dear ${permit.userId?.firstName} ${permit.userId?.lastName}" +
////                                                                            "A lab report is available for your permit application. (${permit.manufacturerEntity?.name})" +
////                                                                            "Regards," +
////                                                                            "The KIMS team.")
////                                                                }
////
////                                                                result = "redirect:/inspection?whereTo=ssf_details&ssf_id=${ss_id}"
////                                                                redirectAttributes.addFlashAttribute("alert", "A lab report has been generated. A notification has been sent to KEBS.")
////
////                                                                pId?.let { qualityAssuranceBpmn.qaSfMIAnalyzeSamplesComplete(it, qaoAssigneeId) }
////                                                            }
////                                                }
////
////                                    }
////                        }
////                        else -> {
////                            result = "redirect:/inspection?whereTo=lab_details"
////                            redirectAttributes.addFlashAttribute("error", "Caught an error! Try again later.")
////                        }
////                    }
////                }
////        return result
////    }
////
////}
//=======
//package org.kebs.app.kotlin.apollo.api.controllers.inspection
//
//import mu.KotlinLogging
//import org.kebs.app.kotlin.apollo.api.notifications.Notifications
//import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.QualityAssuranceBpmn
//import org.kebs.app.kotlin.apollo.api.ports.provided.dao.QualityAssuranceDaoServices
//import org.kebs.app.kotlin.apollo.common.exceptions.StorageException
//import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
//import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
//import org.kebs.app.kotlin.apollo.store.model.*
//import org.kebs.app.kotlin.apollo.store.repo.*
//import org.springframework.data.repository.findByIdOrNull
//import org.springframework.security.core.context.SecurityContextHolder
//import org.springframework.stereotype.Controller
//import org.springframework.ui.Model
//import org.springframework.util.StringUtils
//import org.springframework.web.bind.annotation.GetMapping
//import org.springframework.web.bind.annotation.ModelAttribute
//import org.springframework.web.bind.annotation.PostMapping
//import org.springframework.web.bind.annotation.RequestParam
//import org.springframework.web.multipart.MultipartFile
//import org.springframework.web.servlet.mvc.support.RedirectAttributes
//import java.lang.StringBuilder
//import java.sql.Date
//import java.sql.Timestamp
//import java.time.Instant
//import java.util.*
//import javax.servlet.http.HttpServletResponse
//
//
//@Controller
//class InspectionController(
//        private val serviceMapsRepository: IServiceMapsRepository,
//        applicationMapProperties: ApplicationMapProperties,
//        private val sampleSubmissionRepository: ISampleSubmissionRepository,
//        private val sampleCollectionRepository: ISampleCollectRepository,
//        private val qualityAssuranceDaoServices: QualityAssuranceDaoServices,
//        private val permitRepo: IPermitRepository,
//        private val notifications: Notifications,
//        private val usersRepository: IUserRepository,
//        private val workPlanRepo: IWorkPlanGenerateRepository,
//        private val sampleCollectionDocumentsRepository: ISampleCollectionDocumentsRepository,
//        private val sampleSubmissionDocumentsRepository: ISampleSubmissionDocumentsRepository,
//        private val labReportDocumentsRepo: ILabReportDocumentsRepository,
//        private val labReportsRepository: ILabReportsRepository,
//        private val listOfLabsRepo: ILaboratoryRepository,
//        private val qualityAssuranceBpmn: QualityAssuranceBpmn
//) {
//    val appId = applicationMapProperties.mapPermitApplication
//
//    val labAssigneeId = applicationMapProperties.labAssigneeId
//    val qaoAssigneeId = applicationMapProperties.qaoAssigneeId
//    val hofId = applicationMapProperties.hofId
//    val hodId = applicationMapProperties.qaHod
//    val pacId = applicationMapProperties.pacAssigneeId
//    val assessorId = applicationMapProperties.assessorId
//    val uploadDir = applicationMapProperties.localFilePath
//
//    /**
//     * SCF (QA, MS)
//     * SSF
//     * Generate BS Number (LIMS)
//     * Analyze samples
//     * Lab report
//     * Approve lab report
//     */
//    @GetMapping("/inspection")
//    fun generalInspection(
//            model: Model,
//            response: HttpServletResponse,
//            @RequestParam("sampleCollectedId", required = false) sampleCollectedId: Long?,
//            @RequestParam("sampleSubmittedId", required = false) sampleSubmittedId: Long?,
//            redirectAttributes: RedirectAttributes,
//            @RequestParam("whereTo", required = true) whereTo: String?,
//            @RequestParam("labReportId", required = false) labReportId: Long?,
//            @RequestParam(value = "page", required = false) page: Int?,
//            @RequestParam(value = "records", required = false) records: Int?,
//            @RequestParam("ssf_id", required = false) ssf_id: Long?,
//            @RequestParam("completenessStatus", required = false) completenessStatus: Int?,
//            @RequestParam("permitId", required = false) permitId: Long?,
//            @RequestParam("workPlanId", required = false) workPlanId: Long?,
//            @RequestParam("labReportCompletenessStatus", required = false) labReportCompletenessStatus: Int?
//    ): String {
//        var result = ""
//        serviceMapsRepository.findByIdOrNull(appId)
//                ?.let { map ->
//                    when (whereTo) {
//                        "lab_report_completeness" -> {
//                            sampleSubmissionRepository.findByIdOrNull(ssf_id)
//                                    ?.let { sample ->
//                                        labReportsRepository.findByIdOrNull(labReportId)
//                                                ?.let { report ->
//                                                    when (sample.objectId) {
//                                                        1 -> {
//                                                            permitRepo.findByIdOrNull(permitId)
//                                                                    ?.let { permit ->
//                                                                        with(sample) {
//                                                                            samplesCompleteness = completenessStatus
//                                                                        }
//                                                                        sampleSubmissionRepository.save(sample)
//
//                                                                        with(report) {
//                                                                            labReportCompletenessStatuss = completenessStatus
//                                                                        }
//                                                                        labReportsRepository.save(report)
//
//                                                                        with(permit) {
//                                                                            labReportAcceptanceStatus = completenessStatus
//                                                                        }
//                                                                        permitRepo.save(permit)
//
//                                                                        if (permit.permitType != map.activeStatus.toLong()) {
//                                                                            permitId?.let { qualityAssuranceBpmn.qaSfMICheckLabReportComplete(it, qaoAssigneeId, labAssigneeId, true) }
//                                                                        } else {
//                                                                            permitId?.let { qualityAssuranceBpmn.qaDmARCheckLabReportsComplete(it, qaoAssigneeId, labAssigneeId, true) }
//                                                                        }
//                                                                        redirectAttributes.addFlashAttribute("alert", "You have marked the lab test results as positive")
//                                                                        result = "redirect:/api/qao/application/details/${permitId}"
//                                                                    }
//                                                        }
//                                                        2 -> {
//
//                                                        }
//                                                        else -> {
//                                                            redirectAttributes.addFlashAttribute("error", "Invalid parameter passed.")
//                                                        }
//                                                    }
//                                                }
//
//                                    }
//                        }
//                        /**
//                         * Load lab ssf details
//                         */
//                        "ssf_details" -> {
//                            sampleSubmissionRepository.findByIdOrNull(ssf_id)
//                                    ?.let { single_ssf ->
//                                        model.addAttribute("single_ssf", single_ssf)
//
//                                        labReportsRepository.findBySampleSubmissionEntity((single_ssf))
//                                                ?.let { labReport ->
//                                                    model.addAttribute("labReport", labReport)
//                                                }
//
//                                        model.addAttribute("labData", LabReportsEntity())
//                                    }
//                            result = "quality-assurance/Lab/details"
//
//                        }
//                        /**
//                         * Load Lab home page with list of tasks
//                         */
//                        "lab_home" -> {
//                            SecurityContextHolder.getContext().authentication?.name
//                                    ?.let { username ->
//                                        usersRepository.findByUserName(username)
//                                                ?.let { loggedInUser ->
//                                                    loggedInUser.id
//                                                            ?.let {
//                                                                sampleSubmissionRepository.findAll()
//                                                                        .let { foundSsfs ->
//                                                                            model.addAttribute("ssf", foundSsfs)
//                                                                            result = "quality-assurance/Lab/home"
//                                                                        }
//
//                                                            }
//                                                }
//                                    }
//                        }
//                        /**
//                         * Generate BS Number
//                         */
//                        "generate_bs" -> {
//                            sampleSubmissionRepository.findByIdOrNull(ssf_id)
//                                    ?.let { single_ssf ->
//                                        with(single_ssf) {
//                                            bsNumber = generateRandomText(map.transactionRefLength, map.secureRandom, map.messageDigestAlgorithm, false)
//                                        }
//                                        sampleSubmissionRepository.save(single_ssf)
//
//                                        result = "redirect:/inspection?whereTo=ssf_details&ssf_id=${ssf_id}"
//                                        redirectAttributes.addFlashAttribute("alert", "BS No. Generated")
//                                    }
//                        }
//                        "samples_completeness" -> {
//                            permitRepo.findByIdOrNull(permitId)
//                                    ?.let { permit ->
//                                        sampleSubmissionRepository.findByIdOrNull(ssf_id)
//                                                ?.let { ss ->
//                                                    when (completenessStatus) {
//                                                        1 -> {
//                                                            if (permit.permitType != 1L) {
//                                                                permitId?.let { qualityAssuranceBpmn.qaSfMIAnalyzeSamplesComplete(it, qaoAssigneeId) }
//                                                            } else {
//                                                                permitId?.let { qualityAssuranceBpmn.qaDmARLabAnalysisComplete(it, qaoAssigneeId) }
//                                                            }
//                                                            with(ss) {
//                                                                samplesCompleteness = map.activeStatus
//                                                            }
//                                                            sampleSubmissionRepository.save(ss)
//
//                                                            with(permit) {
//                                                                labReportStatus = completenessStatus
//                                                            }
//                                                            permitRepo.save(permit)
//                                                            redirectAttributes.addFlashAttribute("alert", "You have marked the lab samples as complete")
//                                                            result = "redirect:/inspection?whereTo=ssf_details&ssf_id=${ssf_id}"
//                                                        }
//                                                        0 -> {
//                                                            permitId?.let { qualityAssuranceBpmn.qaSfMIAnalyzeSamplesComplete(it, qaoAssigneeId) }
//                                                            with(permit) {
//                                                                labReportStatus = completenessStatus
//                                                            }
//                                                            permitRepo.save(permit)
//                                                            redirectAttributes.addFlashAttribute("alert", "You have marked the lab samples as incomplete. A notification has been sent to KEBS.")
//                                                            result = "redirect:/inspection?whereTo=ssf_details&ssf_id=${ssf_id}"
//                                                        }
//                                                        else -> {
//                                                            KotlinLogging.logger { }.info { "Invalid parameter passed" }
//                                                        }
//                                                    }
//                                                }
//                                    }
//
//                        }
//
//                        // Download sample submitted
//                        "download_sample_submitted" -> {
//                            sampleSubmissionRepository.findByIdOrNull(sampleSubmittedId)
//                                    ?.let { ssEnt ->
//                                        sampleSubmissionDocumentsRepository.findBySampleSubmissionEntity(ssEnt)
//                                                ?.let { sampleSubmissionDoc ->
//                                                    response.contentType = sampleSubmissionDoc.fileType
//                                                    response.addHeader("Content-Dispostion", "inline; filename=${sampleSubmissionDoc.name};")
//                                                    response.outputStream
//                                                            .let { responseOutputStream ->
//                                                                sampleSubmissionDoc.document?.let { responseOutputStream.write(it) }
//                                                                responseOutputStream.close()
//                                                            }
//                                                }
//                                    }
//                        }
//
//                        // Download sample collected
//                        "download_sample_collected" -> {
//                            sampleCollectionRepository.findByIdOrNull(sampleCollectedId)
//                                    ?.let { scEnt ->
//                                        sampleCollectionDocumentsRepository.findBySampleCollectionEntity(scEnt)
//                                                ?.let { sampleCollectednDoc ->
//                                                    response.contentType = sampleCollectednDoc.fileType
//                                                    response.addHeader("Content-Dispostion", "inline; filename=${sampleCollectednDoc.name};")
//                                                    response.outputStream
//                                                            .let { responseOutputStream ->
//                                                                sampleCollectednDoc.document?.let { responseOutputStream.write(it) }
//                                                                responseOutputStream.close()
//                                                            }
//                                                }
//                                    }
//                        }
//
//                        // Download lab report
//                        "download_lab_report" -> {
//                            labReportsRepository.findByIdOrNull(labReportId)
//                                    ?.let { labRpt ->
//                                        labReportDocumentsRepo.findByLabReport(labRpt)
//                                                ?.let { labRptDoc ->
//                                                    response.contentType = labRptDoc.fileType
//                                                    response.addHeader("Content-Dispostion", "inline; filename=${labRptDoc.name};")
//                                                    response.outputStream
//                                                            .let { responseOutputStream ->
//                                                                labRptDoc.document?.let { responseOutputStream.write(it) }
//                                                                responseOutputStream.close()
//                                                            }
//                                                }
//                                    }
//                        }
//
//                        else -> {
//                            result = ""
//                        }
//                    }
//                }
//        return result
//    }
//
//
//    @PostMapping("/inspection/save")
//    fun saveInspectionData(
//            model: Model,
//            redirectAttributes: RedirectAttributes,
//            @RequestParam("whereTo", required = false) whereTo: String?,
//            @ModelAttribute("scfData") scfData: CdSampleCollectionEntity?,
//            @RequestParam("labReportFile", required = false) labReportFile: MultipartFile?,
//            @RequestParam("sampleCollectionFile", required = false) sampleCollectionFile: MultipartFile?,
//            @RequestParam("sampleSubmissionFile", required = false) sampleSubmissionFile: MultipartFile?,
//            @ModelAttribute("ssfData") ssfData: CdSampleSubmissionItemsEntity?,
//            @RequestParam("pId", required = false) pId: Long?,
//            @RequestParam("ss_id", required = false) ss_id: Long?,
//            @RequestParam("selectLab", required = false) selectLab: Long?,
//            @RequestParam("objId", required = false) objId: Int?,
//            @RequestParam("workplanId", required = false) workplanId: Long?,
//            @ModelAttribute("labReport") labReport: LabReportsEntity?,
//            @ModelAttribute("sampleCollectionDocumentsEntity") sampleCollectionDocumentsEntity: SampleCollectionDocumentsEntity?,
//            @ModelAttribute("sampleSubmissionDocumentsEntity") sampleSubmissionDocumentsEntity: SampleSubmissionDocumentsEntity?,
//            @ModelAttribute("labReportDocumentsEntity") labReportDocumentsEntity: LabReportDocumentsEntity?
//    ): String {
//        var result = ""
//        serviceMapsRepository.findByIdOrNull(appId)
//                .let { map ->
//                    when (whereTo) {
//                        /**
//                         * Save sample submission form
//                         */
//                        "save_ssf" -> {
//                            qualityAssuranceDaoServices.extractUserFromContext(SecurityContextHolder.getContext())
//                                    ?.let { user ->
//                                        when (objId) {
//                                            map?.activeStatus -> {
//                                                permitRepo.findByIdOrNull(pId)
//                                                        ?.let { pm ->
//
//                                                            var submittedSample = ssfData
//                                                            with(submittedSample) {
//                                                                this?.status = map?.inactiveStatus
//                                                                this?.createdOn = Timestamp.from(Instant.now())
//                                                                this?.createdBy = user.userName
//                                                                this?.permitId = pm
//                                                                this!!.objectId = objId
//                                                                selectedLab = listOfLabsRepo.findByIdOrNull(selectLab)
//                                                            }
//                                                            submittedSample?.let { submittedSample = sampleSubmissionRepository.save(it) }
//
//                                                            // Save sample submission documents
//                                                            var stgSampleSubmissionDocs = sampleSubmissionDocumentsEntity
//                                                            with(stgSampleSubmissionDocs) {
//                                                                this?.name = sampleCollectionFile?.let { qualityAssuranceDaoServices.saveDocuments(null, null, stgSampleSubmissionDocs, null, it) }
//                                                                this?.fileType = sampleCollectionFile?.contentType
//                                                                this?.documentType = "SAMPLE_SUBMISSION"
//                                                                this?.document = sampleCollectionFile?.bytes
//                                                                this?.transactionDate = Date(Date().time)
//                                                                this?.status = map?.activeStatus
//                                                                this?.createdBy = user.userName
//                                                                this?.createdOn = Timestamp.from(Instant.now())
//                                                                this?.permitId = pm
//                                                                this?.sampleSubmissionEntity = submittedSample
//                                                            }
//                                                            stgSampleSubmissionDocs?.let { stgSampleSubmissionDocs = sampleSubmissionDocumentsRepository.save(it) }
//
//                                                            with(pm) {
//                                                                sampleSubmittedStatus = map?.activeStatus
//                                                                sampleSubmittedId = submittedSample
//                                                            }
//                                                            permitRepo.save(pm)
//
//                                                            if (pm.permitType != 1L) {
//                                                                pm.id?.let { qualityAssuranceBpmn.qaSfMISubmitSamplesComplete(it, labAssigneeId) }
//                                                            } else {
//                                                                pm.id?.let { pId ->
//                                                                    qualityAssuranceBpmn.qaDmARCheckTestReportsComplete(pId, qaoAssigneeId, false)
//                                                                            .let {
//                                                                                qualityAssuranceBpmn.qaDmARFillSSFComplete(pId, labAssigneeId)
//                                                                                        .let {
//                                                                                            qualityAssuranceBpmn.qaDmARReceiveSSFComplete(pId, qaoAssigneeId)
//                                                                                                    .let {
//                                                                                                        qualityAssuranceBpmn.qaDmARSubmitSamplesComplete(pId, labAssigneeId)
//                                                                                                    }
//                                                                                        }
//                                                                            }
//                                                                }
//                                                            }
//
//                                                            redirectAttributes.addFlashAttribute("alert", "You have submitted your samples. Await for the BS no. to attach to the samples.")
//                                                            result = "redirect:/api/qao/applications"
//                                                        }
//                                            }
//                                            2 -> {
//                                                workPlanRepo.findByIdOrNull(workplanId)
//                                                        ?.let { workplan ->
//
//                                                            var submittedSample = ssfData
//                                                            with(submittedSample) {
//                                                                this?.status = map?.inactiveStatus
//                                                                this?.createdOn = Timestamp.from(Instant.now())
//                                                                this?.createdBy = user.userName
//                                                                this?.workplanId = workplan
//                                                                this!!.objectId = objId
//                                                            }
//                                                            submittedSample?.let { submittedSample = sampleSubmissionRepository.save(it) }
//
//
//                                                            // Save sample submission documents
//                                                            var stgSampleSubmissionDocs = sampleSubmissionDocumentsEntity
//                                                            with(stgSampleSubmissionDocs) {
//                                                                this?.name = sampleCollectionFile?.let { qualityAssuranceDaoServices.saveDocuments(null, null, stgSampleSubmissionDocs, null, it) }
//                                                                this?.fileType = sampleCollectionFile?.contentType
//                                                                this?.documentType = "SAMPLE_SUBMISSION"
//                                                                this?.document = sampleCollectionFile?.bytes
//                                                                this?.transactionDate = Date(Date().time)
//                                                                this?.status = map?.activeStatus
//                                                                this?.createdBy = user.userName
//                                                                this?.createdOn = Timestamp.from(Instant.now())
//                                                                this?.workplanId = workplan
//                                                                this?.sampleSubmissionEntity = submittedSample
//                                                            }
//                                                            stgSampleSubmissionDocs?.let { stgSampleSubmissionDocs = sampleSubmissionDocumentsRepository.save(it) }
//
//                                                            with(workplan) {
//                                                                sampleSubmittedStatus = map?.activeStatus
//                                                                sampleSubmittedId = submittedSample
//                                                            }
//                                                            workPlanRepo.save(workplan)
//
//                                                            redirectAttributes.addFlashAttribute("alert", "You have submitted your samples. Await for the BS no. to attach to the samples.")
//                                                            result = "redirect:/api/ms/workPlan-detail?workPlanId=${workplanId}"
//
//
//                                                        }
//                                            }
//                                            else -> KotlinLogging.logger { }.info { "Exception" }
//                                        }
//
//                                    }
//                        }
//                        /**
//                         * Save Sample collection form
//                         */
//                        "save_scf" -> {
//                            qualityAssuranceDaoServices.extractUserFromContext(SecurityContextHolder.getContext())
//                                    ?.let { user ->
//                                        permitRepo.findByIdOrNull(pId)
//                                                ?.let { pm ->
//                                                    // Save sample collection entity
//                                                    var sampleCollected = scfData
//                                                    with(sampleCollected) {
//                                                        this?.status = map?.inactiveStatus
//                                                        this?.createdBy = user.userName
//                                                        this?.createdOn = Timestamp.from(Instant.now())
//                                                        this?.permitId = pm
//
//                                                    }
//                                                    sampleCollected?.let { sampleCollected = sampleCollectionRepository.save(it) }
//
//                                                    // Save sample collection documents (Blob)
//                                                    var stgSampleCollectedDocs = sampleCollectionDocumentsEntity
//                                                    with(stgSampleCollectedDocs) {
//
//                                                        this?.name = sampleCollectionFile?.let { qualityAssuranceDaoServices.saveDocuments(null, sampleCollectionDocumentsEntity, null, null, it) }
//
//                                                        this?.fileType = sampleCollectionFile?.contentType
//                                                        this?.documentType = "SAMPLE_COLLECTION"
//                                                        this?.document = sampleCollectionFile?.bytes
//                                                        this?.transactionDate = Date(Date().time)
//                                                        this?.status = map?.activeStatus
//                                                        this?.createdBy = user.userName
//                                                        this?.createdOn = Timestamp.from(Instant.now())
//                                                        this?.permitId = pm
//                                                        this?.sampleCollectionEntity = sampleCollected
//                                                    }
//                                                    stgSampleCollectedDocs?.let { stgSampleCollectedDocs = sampleCollectionDocumentsRepository.save(it) }
//
//                                                    // Update permit
//                                                    with(pm) {
//                                                        sampleCollectionStatus = map?.activeStatus
//                                                    }
//                                                    permitRepo.save(pm)
//
//
//                                                    redirectAttributes.addFlashAttribute("alert", "You have submitted your sample collection form. You can now upload the sample submission form.")
//                                                    result = "redirect:/api/qao/application/details/${pId}"
//                                                }
//                                    }
//
//                        }
//                        /**
//                         * Save lab report form
//                         */
//                        "save_lab_report" -> {
//                            qualityAssuranceDaoServices.extractUserFromContext(SecurityContextHolder.getContext())
//                                    ?.let { user ->
//
//                                        permitRepo.findByIdOrNull(pId)
//                                                ?.let { permit ->
//                                                    sampleSubmissionRepository.findByIdOrNull(ss_id)
//                                                            ?.let { ss ->
//
//                                                                with(permit) {
//                                                                    labSamplesAnalysisComplete = map?.activeStatus
//                                                                    labReportStatus = map?.activeStatus
//                                                                }
//                                                                permitRepo.save(permit)
//
//                                                                var newLabReport = labReport
//                                                                with(newLabReport) {
//                                                                    this?.status = map?.inactiveStatus
//                                                                    this?.createdBy = user.userName
//                                                                    this?.createdOn = Timestamp.from(Instant.now())
//                                                                    this?.permit = permit
//                                                                    this?.sampleSubmissionEntity = ss
//                                                                    labReportFile
//                                                                            ?.let { labFile ->
//                                                                                qualityAssuranceDaoServices.storeFile(labFile)
//                                                                                val labFileName = StringBuilder()
//                                                                                labFile.originalFilename
//                                                                                        .let { labFileOriginalFileName ->
//                                                                                            labFileName.append(labFileOriginalFileName)
//                                                                                        }
//                                                                                this?.labReportFilePath = labFileName.toString()
//                                                                            }
//
//                                                                }
//
//                                                                permit.userId?.email?.let {
//                                                                    notifications.processEmail(it, "Lab Report", "\n" +
//                                                                            "Dear, ${permit.userId?.firstName} ${permit.userId?.lastName}.\n" +
//                                                                            "A lab report for your permit application is ready for viewing.\n", "${uploadDir}${labReportFile?.originalFilename}")
//                                                                }
//
//                                                                labReport?.let { rpt -> newLabReport = labReportsRepository.save(rpt) }
//
//
//                                                                with(labReportDocumentsEntity) {
//                                                                    labReportDocumentsEntity?.let { rpt -> this?.name = labReportFile?.let { qualityAssuranceDaoServices.saveDocuments(rpt, null, null, null, it) } }
//                                                                    this?.fileType = labReportFile?.contentType
//                                                                    this?.documentType = "LAB_REPORT"
//                                                                    this?.document = labReportFile?.bytes
//                                                                    this?.transactionDate = Date(Date().time)
//                                                                    this?.status = map?.activeStatus
//                                                                    this?.createdBy = user.userName
//                                                                    this?.createdOn = Timestamp.from(Instant.now())
//                                                                    this?.permitId = permit
//                                                                    this?.labReport = newLabReport
//                                                                    this?.sampleSubmissionEntity = ss
//                                                                }
//                                                                labReportDocumentsEntity?.let { generatedRpt -> labReportDocumentsRepo.save(generatedRpt) }
//
//                                                                permit.userId?.email?.let {
//                                                                    notifications.sendEmail(it, "Lab Report Availability", "Dear ${permit.userId?.firstName} ${permit.userId?.lastName}" +
//                                                                            "A lab report is available for your permit application. (${permit.manufacturerEntity?.name})" +
//                                                                            "Regards," +
//                                                                            "The KIMS team.")
//                                                                }
//
//                                                                result = "redirect:/inspection?whereTo=ssf_details&ssf_id=${ss_id}"
//                                                                redirectAttributes.addFlashAttribute("alert", "A lab report has been generated. A notification has been sent to KEBS.")
//
//                                                                pId?.let { qualityAssuranceBpmn.qaSfMIAnalyzeSamplesComplete(it, qaoAssigneeId) }
//                                                            }
//                                                }
//
//                                    }
//                        }
//                        else -> {
//                            result = "redirect:/inspection?whereTo=lab_details"
//                            redirectAttributes.addFlashAttribute("error", "Caught an error! Try again later.")
//                        }
//                    }
//                }
//        return result
//    }
//
//}
//>>>>>>> origin/feature/staging/standards-levy/uat/user-journey-dev-v2
