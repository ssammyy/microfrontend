package org.kebs.app.kotlin.apollo.api.controllers.qaControllers

import org.kebs.app.kotlin.apollo.api.export.ExportFile
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.QualityAssuranceBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.QualityAssuranceDaoServices
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.PermitApplicationEntity
import org.kebs.app.kotlin.apollo.store.repo.IPermitRepository
import org.kebs.app.kotlin.apollo.store.repo.IUserRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.sql.Timestamp
import java.time.Instant
import java.time.temporal.ChronoUnit

@Controller
@RequestMapping("/psc_pcm")
class PscPcmController(
        private val daoServices: QualityAssuranceDaoServices,
        private val qualityAssuranceBpmn: QualityAssuranceBpmn,
        private val permitRepo: IPermitRepository,
        private val userRepository: IUserRepository,
        private val exportFile: ExportFile,
        private val notifications: Notifications,
        applicationMapProperties: ApplicationMapProperties
) {

    val pscAssigneeId = applicationMapProperties.pscAssigneeId
    val qaoAssigneeId = applicationMapProperties.qaoAssigneeId
    val pcmAssigneeId = applicationMapProperties.pcmAssigneeId
    val uploadDir = applicationMapProperties.localFilePath

    val hofId = applicationMapProperties.hofId
    val hodId = applicationMapProperties.qaHod

//    @GetMapping("")
//    fun pscPcmTasks(
//            model: Model
//
//            ): String {
//        var result = ""
//        SecurityContextHolder.getContext().authentication
//                ?.let { user ->
////                    userRepository.findByUserName(user.name)
////                            ?.let { loggedInUser ->
////                                loggedInUser.id?.let {
////                                    qualityAssuranceBpmn.fetchQaAppReviewTasks(it)?.let { lstTaskDetails ->
////                                        model.addAttribute("tasks", lstTaskDetails)
////                                    }
////                                }
////                            }
//                    userRepository.findByUserName(user.name)
//                            ?.let { loggedInUser ->
//                                loggedInUser.id?.let {
//                                    qualityAssuranceBpmn.fetchAllTasksByAssignee(it)?.let { lstTaskDetails ->
//                                        model.addAttribute("tasks", lstTaskDetails)
//                                        val tasks = mutableListOf<PermitApplicationEntity?>()
//                                        lstTaskDetails.forEach { taskDetails ->
//                                            taskDetails.task.let{ task->
//                                                println("${taskDetails.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                                                tasks.add(permitRepo.findByIdOrNull(taskDetails.permitId))
//                                            }
//                                        }
//                                        model.addAttribute("permitApplications", tasks)
//                                    }
//                                }
//                            }
//                }
//        return "quality-assurance/PSCPCM/tasks"
//    }


    @GetMapping("/approve_award_permit")
    fun approvePermit(
            model: Model,
            redirectAttributes: RedirectAttributes,
            @RequestParam("id") id: Long,
            @RequestParam("status", required = false) status: Int?,
            @RequestParam("type") type: String?,
            @RequestParam("awardStatus", required = false) awardStatus: Int?,
            @RequestParam("pacDeferralComments", required = false) pacDeferralComments: String?
    ): String {
        var result = ""

        SecurityContextHolder.getContext().authentication
                ?.let { user ->
                    when (type) {
                        "approve" -> {
                            if (user.authorities.contains(SimpleGrantedAuthority("APPROVE_PERMIT"))) {
                                permitRepo.findByIdOrNull(id)
                                        ?.let { pm ->
                                            pm.permitApprovalStatusPsc = status
                                            pm.modifiedOn = Timestamp.from(Instant.now())
                                            pm.modifiedBy = user.name
                                            permitRepo.save(pm)

                                            val emails = mutableListOf<String?>()
                                            emails.add(userRepository.findByIdOrNull(pm.allocatedToAssessor)?.email)
                                            emails.add(userRepository.findByIdOrNull(qaoAssigneeId)?.email)


                                            if (status == 0) {
                                                pm.userId?.email?.let {
                                                    notifications.sendEmail(it, "Permit Deferral Comments", "Dear, ${pm.userId?.firstName}.\n" +
                                                            "The following comment was made with the deferral of your permit. Kindly review.\n" +
                                                            "$pacDeferralComments\n" +
                                                            "Regards, \n" +
                                                            "The KIMS team.")
                                                }
                                                emails.forEach { email ->
                                                    email?.let {
                                                        notifications.sendEmail(it, "Deferred Permit Comments", "Dear,\n" +
                                                                "Review the following comment made on permit ${pm.permitNumber}\n" +
                                                                "\n" +
                                                                "`$pacDeferralComments`\n" +
                                                                "Regards, the KIMS team.")
                                                    }
                                                }
                                            }
                                        }
                                qualityAssuranceBpmn.qaSfPAPscApprovalComplete(id, qaoAssigneeId, pcmAssigneeId, true)

                                result = "redirect:/api/qa/hof"
                                redirectAttributes.addFlashAttribute("success", "You have successfully approved.")
                            } else {
                                result = "redirect:/api/qa/hof"
                                redirectAttributes.addFlashAttribute("error", "You do not have rights to approve permits.")
                            }
                        }
                        "award" -> {
                            if (user.authorities.contains(SimpleGrantedAuthority("GIVE_RECOMMENDATION_ON_PERMIT_AWARD"))) {
                                permitRepo.findByIdOrNull(id)
                                        ?.let { pm ->
                                            pm.permitAwardStatusPscPcm = awardStatus
                                            pm.modifiedOn = Timestamp.from(Instant.now())
                                            pm.modifiedBy = user.name
                                            if (awardStatus == 1) {
                                                pm.dateAwarded = Timestamp.from(Instant.now())
                                            }
                                            // Schedule alert to four months prior to expiry of permit - add 8 months hrs to current timestamp
                                            pm.userId?.id?.let { qualityAssuranceBpmn.scheduleReminder(223, Timestamp.from(Instant.now().plus(5760, ChronoUnit.HOURS)), it) }
                                            pm.allocatedToAssessor?.let { qualityAssuranceBpmn.scheduleReminder(242, Timestamp.from(Instant.now().plus(5760, ChronoUnit.HOURS)), it) }

                                            // Reminders to QAO and manufacturer 8 months after permit awarding
                                            pm.userId?.id?.let { qualityAssuranceBpmn.scheduleReminder(223, Timestamp.from(Instant.now().plus(5760, ChronoUnit.HOURS)), it) }
                                            qualityAssuranceBpmn.scheduleReminder(242, Timestamp.from(Instant.now().plus(5760, ChronoUnit.HOURS)), 402)

                                            permitRepo.save(pm)


                                            when (awardStatus) {
                                                1 -> {
                                                    qualityAssuranceBpmn.qaSfPAPermitAwardComplete(id, qaoAssigneeId, true)
                                                    redirectAttributes.addFlashAttribute("success", "You have successfully awarded a permit.")

                                                    exportFile.parseThymeleafTemplate("templates/TestPdf/permit-cert", "permit", pm)?.let { htmlString ->
                                                        exportFile.generatePdfFromHtml(htmlString)
                                                        pm.userId?.email?.let {
                                                            notifications.processEmail(it, "Permit Award", "Dear, ${pm.userId?.firstName} ${pm.userId?.firstName}\n" +
                                                                    "Congratulations on the permit award.\n" +
                                                                    "Regards," +
                                                                    "The KIMS team", "${uploadDir}pdf_output.pdf")

                                                            val kebsRecipients = mutableListOf<String?>()
                                                            kebsRecipients.add(userRepository.findByIdOrNull(pscAssigneeId)?.email)
                                                            kebsRecipients.add(userRepository.findByIdOrNull(pcmAssigneeId)?.email)
                                                            kebsRecipients.add(userRepository.findByIdOrNull(hofId)?.email)
                                                            kebsRecipients.add(userRepository.findByIdOrNull(hodId)?.email)
                                                            kebsRecipients.add(userRepository.findByIdOrNull(qaoAssigneeId)?.email)

                                                            kebsRecipients.forEach { email ->
                                                                email?.let { singleEmail ->
                                                                    notifications.processEmail(singleEmail, "Permit Award", "Dear, \n" +
                                                                            "A permit has been generated for ${pm.manufacturerEntity?.name}.\n" +
                                                                            "See attached.\n" +
                                                                            "Regards," +
                                                                            "The KIMS team", "${uploadDir}pdf_output.pdf")
                                                                }
                                                            }


                                                        }
                                                    }

                                                }
                                                else -> {
                                                    qualityAssuranceBpmn.qaSfPAPermitAwardComplete(id, qaoAssigneeId, false)
                                                    redirectAttributes.addFlashAttribute("info", "You have differed this permit application.")
                                                }
                                            }
                                            result = "redirect:/api/qa/hof"
                                        }

                            } else {
                                result = "redirect:/api/qa/hof"
                                redirectAttributes.addFlashAttribute("error", "You do not have rights to award permits.")
                            }
                        }
                        else -> {
                            result = "quality-assurance/qa-index"
                            redirectAttributes.addFlashAttribute("error", "We couldn't process your request.")
                        }
                    }

                }
        return result
    }

}