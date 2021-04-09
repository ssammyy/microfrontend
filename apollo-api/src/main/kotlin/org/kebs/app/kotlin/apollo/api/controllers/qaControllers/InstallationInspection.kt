package org.kebs.app.kotlin.apollo.api.controllers.qaControllers

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.export.ExportFile
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.QualityAssuranceBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.QualityAssuranceDaoServices
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.InvoiceEntity
import org.kebs.app.kotlin.apollo.store.model.PetroleumFieldInspectionDataEntity
import org.kebs.app.kotlin.apollo.store.model.PetroleumInstallationInspectionEntity
import org.kebs.app.kotlin.apollo.store.repo.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.sql.Timestamp
import java.time.Instant

@Controller
@RequestMapping("/ii")
class InstallationInspection(
        private val installationInspectionRepository: IPetroleumInstallationInspectionRepository,
        private val userRepository: IUserRepository,
        private val qualityAssuranceBpmn: QualityAssuranceBpmn,
        private val invoiceRepo: IInvoiceRepository,
        private val installationInspectionDataRepository: IPetroleumFieldInspectionDataRepository,
        private val qualityAssuranceDaoServices: QualityAssuranceDaoServices,
        applicationMapProperties: ApplicationMapProperties,
        private val serviceMapsRepository: IServiceMapsRepository,
        private val notifications: Notifications,
        private val exportFile: ExportFile
) {
    val directorAssigneeId = applicationMapProperties.directorAssigneeId
    val petroleumManager = applicationMapProperties.pmAssigneeId
    val qaoId = applicationMapProperties.qaoAssigneeId
    val hofAssigneeId = applicationMapProperties.hofAssigneeId
    val hodId = applicationMapProperties.qaHod
    val appId = applicationMapProperties.mapQaFuelInspection
    val uploadDir = applicationMapProperties.localFilePath

    @PreAuthorize("hasAuthority('PETROLEUM_ALLOCATE_INSPECTION_OFFICER') or hasAuthority('EPRA_ADD_STATION_FOR_INSPECTION') or hasAuthority('SCHEDULE_FACTORY_VISIT') or hasAuthority('RECEIVE_EPRA_NOTIFICATION_LETTER') or hasAuthority('ASSIGN_OFFICER')")
    @GetMapping("")
    fun fuelInspection(
            model: Model,
            @RequestParam("whereTo", required = false) whereTo: String?,
            @RequestParam(value = "page", required = false) page: Int?,
            @RequestParam(value = "records", required = false) records: Int?,
            @RequestParam("appId", required = false) appId: Long?,
            @RequestParam("id", required = false) id: Long?,
            @RequestParam("scheduleDate", required = false) scheduleDate: String?,
            @RequestParam("officerToAssign", required = false) officerToAssign: Long?,
            @RequestParam("isStage", required = false) isStage: String?,
            @RequestParam("petroleumManagerId", required = false) petroleumManagerId: Long?,
            @RequestParam("inspectionReportApprovalStatus", required = false) inspectionReportApprovalStatus: Int?,
            @ModelAttribute("invoiceEntity") invoiceEntity: InvoiceEntity,
            redirectAttributes: RedirectAttributes
    ): String {
        var result = ""
        val emailList = mutableListOf<String?>()
        /**
         * Use appId for statuses and other static values
         */
        SecurityContextHolder.getContext().authentication?.name
                ?.let { username ->
                    userRepository.findByUserName(username)
                            ?.let { user ->
                                user.id
                                        ?.let { userId ->
                                            when (whereTo) {
                                                "all_tasks" -> {
                                                    qualityAssuranceBpmn.fetchAllTasksByAssignee(userId)
                                                            ?.let { listOfTasks ->
                                                                model.addAttribute("tasks", listOfTasks)

                                                                val ids = mutableListOf<Long>()
                                                                listOfTasks.forEach{ taskDetail->
                                                                    ids.add(taskDetail.objectId)
                                                                }
                                                                var tasks = installationInspectionRepository.findByIdIsIn(ids).toMutableList()
                                                                tasks.sortByDescending { pa-> pa?.id }

                                                                model.addAttribute("iiNewStation", PetroleumInstallationInspectionEntity())
                                                                model.addAttribute("usr", userRepository.findByIdOrNull(petroleumManager))
                                                                result = if (tasks.size > 0) {
                                                                    model.addAttribute("iiData", tasks)
                                                                    "quality-assurance/InstallationInspection/tasks"
                                                                } else {
                                                                    redirectAttributes.addFlashAttribute("alert", "You have no tasks.")
                                                                    "quality-assurance/InstallationInspection/tasks"
                                                                }
                                                            }
                                                            ?: throw NullValueNotAllowedException("Data not found.")
                                                }
                                                "single" -> {
                                                    installationInspectionRepository.findByIdOrNull(id)
                                                            ?.let { iiData ->
                                                                model.addAttribute("iiData", iiData)
                                                                installationInspectionDataRepository.findByInstallationInspectionData(iiData)
                                                                        ?.let { fieldInspectionData ->
                                                                            model.addAttribute("fieldInspectionData", fieldInspectionData)
                                                                        }
                                                                model.addAttribute("invoice", invoiceRepo.findByInstallationInspectionId(iiData))
                                                                result = "quality-assurance/InstallationInspection/task_details"
                                                            }
                                                    model.addAttribute("usr", userRepository.findByIdOrNull(qaoId))
                                                            ?: throw NullValueNotAllowedException("Data not found.")
                                                }
                                                "schedule" -> {
                                                    installationInspectionRepository.findByIdOrNull(id)
                                                            ?.let { iiData ->
                                                                with(iiData) {
                                                                    schedulestatus = 1
                                                                    scheduledDate = scheduleDate
                                                                }
                                                                installationInspectionRepository.save(iiData)
                                                                result = "redirect:/ii?id=${iiData.id}&whereTo=single"


                                                                iiData.contactEmail?.let {
                                                                    notifications.sendEmail(it, "Site Inspection Schedule", "Dear esteemed customer\n" +
                                                                            "A site inspection on your petrol station has been scheduled for $scheduleDate\n" +
                                                                            "Regards,\n" +
                                                                            "The KIMS team")
                                                                }
                                                                /**
                                                                 * BPMN schedule visit
                                                                 */
                                                                iiData.assignedOfficer?.let { qualityAssuranceBpmn.qaIISScheduleVisitComplete(iiData.id, it, petroleumManager) }
                                                            }
                                                            ?: throw NullValueNotAllowedException("Data not found.")
                                                }
                                                "assign_officer" -> {
                                                    installationInspectionRepository.findByIdOrNull(id)
                                                            ?.let { iiData ->
                                                                with(iiData) {
                                                                    assignedOfficer = officerToAssign
                                                                }
                                                                installationInspectionRepository.save(iiData)
                                                                result = "redirect:/ii?whereTo=all_tasks"
                                                                /**
                                                                 * BPMN petroleum manager review & allocate IO
                                                                 */
                                                                qualityAssuranceBpmn.qaIISPetroleumMangerReviewComplete(iiData.id)
                                                                        .let {
                                                                            officerToAssign?.let { it1 -> qualityAssuranceBpmn.qaIISAllocateIOComplete(iiData.id, it1) }
                                                                        }
                                                            }
                                                            ?: throw NullValueNotAllowedException("Data not found.")
                                                }
                                                "invoice" -> {
                                                    installationInspectionRepository.findByIdOrNull(id)
                                                            ?.let { iiData ->
                                                                val invoice = qualityAssuranceDaoServices.invoiceGen(invoiceEntity, null, iiData, null, 199)

                                                                with(iiData) {
                                                                    invoiceStatus = 1
                                                                    invoicePaymentStatus = 1
                                                                }
                                                                installationInspectionRepository.save(iiData)

                                                                exportFile.parseThymeleafTemplate("templates/TestPdf/proforma-invoice", "invoice", invoice)?.let { htmlString ->
                                                                    exportFile.generatePdfFromHtml(htmlString)
                                                                    iiData.contactEmail?.let {
                                                                        notifications.processEmail(it, "INVOICE", "Dear esteemed customer,\n" +
                                                                                "A proforma invoice has been generated for your petrol station.\n" +
                                                                                "${invoice?.amount}\n" +
                                                                                "Payment instructions\n" +
                                                                                "Paybill: 1234, account no: 4321.\n" +
                                                                                "Regards," +
                                                                                "The KIMS team", "${uploadDir}pdf_output.pdf")
                                                                    }
                                                                }


                                                                /**
                                                                 * Generate Invoice
                                                                 */
                                                                result = "redirect:/ii?id=${iiData.id}&whereTo=single"
                                                                /**
                                                                 * BPMN generate proforma invoice
                                                                 * Find a way to get customer id
                                                                 */
                                                                iiData.assignedOfficer?.let {
                                                                    qualityAssuranceBpmn.qaIISGenerateProformaInvComplete(iiData.id, it)
                                                                            .let {
                                                                                /**
                                                                                 * Mock customer payment
                                                                                 */
                                                                                iiData.assignedOfficer
                                                                                        ?.let { officer -> qualityAssuranceBpmn.qaIISCustomerPaymentComplete(iiData.id, officer, officer) }
                                                                            }
                                                                }
                                                            }
                                                }
                                                "conduct_inspection" -> {
                                                    installationInspectionRepository.findByIdOrNull(id)
                                                            ?.let { iiData ->
                                                                with(iiData) {
                                                                    inspectionStage = isStage
                                                                }
                                                                installationInspectionRepository.save(iiData)
                                                                result = "quality-assurance/InstallationInspection/inspection_form"
                                                                model.addAttribute("iiNewForm", PetroleumFieldInspectionDataEntity())
                                                                model.addAttribute("iiId", id)
//                                                                result = "redirect:/ii?id=${iiData.id}&whereTo=single"
                                                            }
                                                            ?: throw NullValueNotAllowedException("Not found")
                                                }
                                                "approve_inspection_report" -> {
                                                    installationInspectionRepository.findByIdOrNull(id)
                                                            ?.let { iiData ->
                                                                with(iiData) {
                                                                    inspectionReportApproval = inspectionReportApprovalStatus
                                                                }
                                                                installationInspectionRepository.save(iiData)
                                                                /**
                                                                 * Approve inspection report BPMN
                                                                 */
                                                                when (inspectionReportApprovalStatus) {
                                                                    1 -> {
                                                                        iiData.assignedOfficer?.let { qualityAssuranceBpmn.qaIIRApplicationReviewComplete(iiData.id, it, directorAssigneeId, true) }
                                                                        redirectAttributes.addFlashAttribute("alert", "You have approved the inspection report")

                                                                        userRepository.findByIdOrNull(directorAssigneeId)
                                                                                ?.let { director ->
                                                                                    userRepository.findByIdOrNull(hofAssigneeId)
                                                                                            ?.let { hof ->
                                                                                                userRepository.findByIdOrNull(hodId)
                                                                                                        ?.let { hod ->
                                                                                                            emailList.add(director.email)
                                                                                                            emailList.add(hof.email)
                                                                                                            emailList.add(hod.email)
                                                                                                            emailList.forEach { email ->
                                                                                                                email?.let {
                                                                                                                    notifications.sendEmail(it, "Findings of inspection report for ${iiData.stationName}", "Dear,\n" +
                                                                                                                            "The findings for ${iiData.stationName} have been approved.\n" +
                                                                                                                            "Regards,\n" +
                                                                                                                            "The KIMS team.")
                                                                                                                }
                                                                                                            }

                                                                                                        }
                                                                                            }

                                                                                }
//
                                                                    }
                                                                    0 -> {
                                                                        iiData.assignedOfficer
                                                                                ?.let { qualityAssuranceBpmn.qaIIRApplicationReviewComplete(iiData.id, it, directorAssigneeId, false) }
                                                                        with(iiData) {
                                                                            petroleumManagerReportApproval = 0
                                                                        }
                                                                        installationInspectionRepository.save(iiData)
                                                                        redirectAttributes.addFlashAttribute("error", "You have disannulled the inspection report")

                                                                        userRepository.findByIdOrNull(hofAssigneeId)
                                                                                ?.let { hof ->
                                                                                    userRepository.findByIdOrNull(hodId)
                                                                                            ?.let { hod ->
                                                                                                emailList.add(hof.email)
                                                                                                emailList.add(hod.email)
                                                                                                emailList.forEach { email ->
                                                                                                    email?.let {
                                                                                                        notifications.sendEmail(it, "Findings of inspection report for ${iiData.stationName}", "Dear" +
                                                                                                                "The findings for ${iiData.stationName} have been disapproved." +
                                                                                                                "Log in to the KIMS platform and review." +
                                                                                                                "Regards," +
                                                                                                                "The KIMS team.")
                                                                                                    }
                                                                                                }

                                                                                            }
                                                                                }
                                                                    }


                                                                }


                                                                result = "redirect:/ii?&whereTo=all_tasks"
                                                            }
                                                }
                                                "resubmit_inspection_report_for_approval" -> {
                                                    installationInspectionRepository.findByIdOrNull(id)
                                                            ?.let { iiData ->
                                                                with(iiData) {
                                                                    inspectionReportAvailability = null
                                                                }
                                                                installationInspectionRepository.save(iiData)
                                                                qualityAssuranceBpmn.qaIIRInspectionComplete(iiData.id, petroleumManager)
                                                            }
                                                }
                                                "director_review" -> {
                                                    installationInspectionRepository.findByIdOrNull(id)
                                                            ?.let { iiData ->
                                                                with(iiData) {
                                                                    directorsApproval = inspectionReportApprovalStatus
                                                                }
                                                                installationInspectionRepository.save(iiData)
                                                                KotlinLogging.logger { }.info { "here" }
//                                                                petroleumManagerId
//                                                                        ?.let { pManager ->
                                                                qualityAssuranceBpmn.qaIISDirectorReviewComplete(iiData.id, petroleumManager)
                                                                KotlinLogging.logger { }.info { "there" }
                                                                result = "redirect:/ii?&whereTo=all_tasks"
//                                                                        }

                                                            }
                                                }
                                                else -> {
                                                    redirectAttributes.addFlashAttribute("error", "Caught an exception")
                                                    result = "redirect:/ii?&whereTo=all_tasks"
                                                }
                                            }
                                        }
                            }
                }
                ?: throw UsernameNotFoundException("User not found.")

        return result
    }

    @PostMapping("/save_inspection")
    fun saveInspection(
            model: Model,
            @ModelAttribute("iiNewForm") iiNewForm: PetroleumFieldInspectionDataEntity?,
            @ModelAttribute("iiNewStation") iiNewStation: PetroleumInstallationInspectionEntity?,
            redirectAttributes: RedirectAttributes,
            @RequestParam("whereTo", required = true) whereTo: String?,
            @RequestParam("id", required = false) id: Long?,
            @RequestParam("petroleumManagerId", required = false) petroleumManagerId: Long?
    ): String {
        var result = ""
        SecurityContextHolder.getContext().authentication.name
                ?.let {
                    userRepository.findByUserName(it)
                            ?.let { user ->
                                when (whereTo) {
                                    "new_station" -> {
                                        var station = iiNewStation
                                        with(station) {
                                            this?.createdBy = "${user.firstName} + ${user.lastName}"
                                            this?.createdOn = Timestamp.from(Instant.now())
                                            this?.status = 1
                                            this?.directorAssignee = directorAssigneeId
                                        }
                                        station = installationInspectionRepository.save(iiNewStation!!)

                                        redirectAttributes.addFlashAttribute("alert", "You have added the station for inspection")
                                        result = "redirect:/ii?whereTo=all_tasks"
                                        /**
                                         * Start II Schedule process
                                         */
                                        station.directorAssignee
                                                ?.let { director ->
                                                    station.contactEmail
                                                            ?.let { email ->
                                                                qualityAssuranceBpmn.startQAIIScheduleProcess(station.id, director, email)
                                                            }
                                                }
                                    }
                                    else -> result = "redirect:/ii?whereTo=all_tasks"
                                }
                            }

                }
        return result
    }

    @PostMapping("/save_field_data")
    fun saveFieldData(
            @RequestParam("whereTo", required = false) whereTo: String?,
            @RequestParam("id", required = false) id: Long?,
            @ModelAttribute("iiNewForm") iiNewForm: PetroleumFieldInspectionDataEntity
    ): String {
        var result = ""
        SecurityContextHolder.getContext().authentication.name
                ?.let {
                    userRepository.findByUserName(it)
                            ?.let { user ->
                                when (whereTo) {
                                    "new_inspection_report" -> {
                                        installationInspectionRepository.findByIdOrNull(id)
                                                ?.let { iiData ->
                                                    var newFieldData = iiNewForm
                                                    with(newFieldData) {
                                                        createdBy = "${user.firstName} + ${user.lastName}"
                                                        createdOn = Timestamp.from(Instant.now())
//                                                        status = 1
                                                        installationInspectionData = iiData
                                                    }
                                                    newFieldData = installationInspectionDataRepository.save(newFieldData)
                                                    KotlinLogging.logger { }.info { "Saved field data with id, ${newFieldData.id}" }

                                                    with(iiData) {
                                                        inspectionReportAvailability = 1
                                                    }

                                                    installationInspectionRepository.save(iiData)
                                                    /**
                                                     * Start II reporting process and submit inspection report
                                                     */
                                                    iiData.assignedOfficer?.let { officer ->
                                                        iiData.contactEmail?.let { email ->
                                                            qualityAssuranceBpmn.startQAIIReportingProcess(iiData.id, officer, email)
                                                                    ?.let {
                                                                        qualityAssuranceBpmn.qaIIRInspectionComplete(iiData.id, hofAssigneeId)
                                                                        userRepository.findByIdOrNull(officer)
                                                                                ?.let { officerEnt ->
                                                                                    userRepository.findByIdOrNull(hofAssigneeId)
                                                                                            ?.let { hofUser ->
                                                                                                val emails = mutableListOf<String?>()
                                                                                                emails.add(hofUser.email)
                                                                                                emails.add(officerEnt.email)

                                                                                                exportFile.parseThymeleafTemplate("templates/TestPdf/fuel-report", "report", newFieldData)?.let { htmlString ->
                                                                                                    exportFile.generatePdfFromHtml(htmlString)
                                                                                                    emails.forEach { em ->
                                                                                                        em?.let { singleEmail ->
                                                                                                            notifications.processEmail(singleEmail, "Inspection Report Data", "Dear user" +
                                                                                                                    "A findings report for ${iiData.stationName} petrol station has been submitted for your approval.\n" +
                                                                                                                    "Login to KIMS system and review.\n" +
                                                                                                                    "Regards,\n" +
                                                                                                                    "The KIMS team.", "${uploadDir}pdf_output.pdf"
                                                                                                            )
                                                                                                            notifications.processEmail(iiData.contactEmail!!, "Inspection Report Data", "Dear esteemed customer,\n" +
                                                                                                                    "Findings of the inspection report.\n", "${uploadDir}pdf_output.pdf")
                                                                                                        }
                                                                                                    }
                                                                                                }


                                                                                            }
                                                                                }
                                                                    }
                                                        }
                                                    }
                                                    result = "redirect:/ii?whereTo=all_tasks"
                                                }
                                    }
                                    else -> result = "redirect:/ii?whereTo=all_tasks"
                                }
                            }
                }
        return result
    }
}