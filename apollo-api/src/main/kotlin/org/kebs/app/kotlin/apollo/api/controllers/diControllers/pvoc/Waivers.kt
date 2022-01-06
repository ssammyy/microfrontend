package org.kebs.app.kotlin.apollo.api.controllers.diControllers.pvoc

import mu.KotlinLogging
import okhttp3.internal.toLongOrDefault
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.PvocBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.PvocDaoServices
//import org.kebs.app.kotlin.apollo.api.ports.provided.dao.QualityAssuranceDaoServices
import org.kebs.app.kotlin.apollo.api.service.UserRolesService
import org.kebs.app.kotlin.apollo.common.exceptions.SupervisorNotFoundException
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.repo.*
import org.springframework.beans.support.MutableSortDefinition
import org.springframework.beans.support.PagedListHolder
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder.getContext
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.sql.Date
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import javax.servlet.http.HttpServletResponse
import kotlin.streams.asSequence


//

@Controller
@RequestMapping("/api/di/pvoc/")
class Waivers(
    private val iwaiversApplicationRepo: IwaiversApplicationRepo,
    private val iPvocMasterListRepo: IPvocMasterListRepo,
    iPvocWaiversStatusRepo: IPvocWaiversStatusRepo,
    private val iPvocWaiversRemarksRepo: IPvocWaiversRemarksRepo,
    private val iUserRepository: IUserRepository,
    private val iUserRoleAssignmentsRepository: IUserRoleAssignmentsRepository,
    private val iPvocWaiversReportRepo: IPvocWaiversReportRepo,
    private val iPvocWaiversApplicationDocumentRepo: IPvocWaiversApplicationDocumentRepo,
    private val iPvocWaiversWetcMinutesEntityRepo: IPvocWaiversWetcMinutesEntityRepo,
//    private val qualityAssuranceDaoServices: QualityAssuranceDaoServices,
    private val iPvocWaiversRequestLetterRepo: IPvocWaiversRequestLetterRepo,
    private val notifications: Notifications,
    private val pvocBpmn: PvocBpmn,
    private val commonDaoServices: CommonDaoServices,
    private val iUserRolesRepository: IUserRolesRepository,
    private val pvocDaoServices: PvocDaoServices,
    pvocComplainStatusEntityRepo: PvocComplainStatusEntityRepo,
    applicationMapProperties: ApplicationMapProperties,
    private val userRolesService: UserRolesService
) {

    final val appId = applicationMapProperties.mapImportInspection
    final val statuses = pvocComplainStatusEntityRepo.findByStatus(1)

    private val waiversStatus = iPvocWaiversStatusRepo.findByIdOrNull(2)

    fun getLoggedInUser(): UsersEntity? {
        getContext().authentication.let { auth ->
            iUserRepository.findByUserName(userName = auth.name).let { userDetails ->
                return userDetails
            }
        }
    }

    fun getTasks(userId: Long): MutableList<PvocWaiversApplicationEntity?> {
        pvocBpmn.fetchAllTasksByAssignee(userId)
            ?.let { listTaskDetails ->
                val tasks = mutableListOf<PvocWaiversApplicationEntity?>()
                listTaskDetails.sortedByDescending { it.objectId }
                    .forEach { taskDetails ->
                        iwaiversApplicationRepo.findByIdOrNull(taskDetails.objectId)
                            ?.let { pvocFound ->
                                tasks.add(pvocFound)
                            }
                    }
                return tasks
            } ?: throw Exception("Failed")
    }

//    @GetMapping("waivers-application")
//    fun waiversApplication(model: Model): String {
//        getContext().authentication.name.let { username ->
//            iUserRepository.findByUserName(username).let { userDetails ->
//                model.addAttribute("applicant", userDetails)
//                model.addAttribute("categories", iPvocWaiversCategoriesRepo.findAllByStatus(1))
//                model.addAttribute("waiver", PvocWaiversApplicationEntity())
//            }
//        }
//        return "destination-inspection/pvoc/WaiversApplication"
//    }

    @GetMapping("email_entry/waiver")
    fun emailEntry(model: Model): String {
        model.addAttribute("emailObject", PvocComplaintsEmailVerificationEntity())
        return "destination-inspection/pvoc/EmailTemplate"
    }

    @PostMapping("save_email/waiver")
    fun saveEmail(
        model: Model,
        emailObject: PvocComplaintsEmailVerificationEntity
    ): String {
        val map = commonDaoServices.serviceMapDetails(appId)
        val sr: ServiceRequestsEntity
        val payload = "Complaint Verification Token [EMail for Verification = ${emailObject.email}]"
        sr = commonDaoServices.mapServiceRequestForSuccess(map, payload, null)
        val token = commonDaoServices.generateEmailVerificationToken(sr, emailObject, map)
        val messageBody = "Please Click the link Bellow \n" +
                "\n " +
                "https://localhost:8006/api/di/pvoc/waivers-application/?token=${token.token}"
//                "https://kims.kebs.org:8006/api/di/pvoc/waivers-application/?token=${token.token}"

        emailObject.email?.let { notifications.sendEmail(it, "Complaint Verification", messageBody) }
        return "redirect:/"
    }


    @PostMapping("save-waiver-application")
    fun saveWaiverApplication(
        waiverApp: PvocWaiversApplicationEntity,
        @RequestParam("document1") document1: ArrayList<MultipartFile>?
    ): String {
        //save app
        getContext().authentication.name.let { username ->
            iUserRepository.findByUserName(username).let { userDetails ->
                waiverApp.applicantName = userDetails?.firstName + " " + userDetails?.lastName
            }
        }
        val waiverDocs = PvocWaiversApplicationDocumentsEntity()
        val r = Random()
        val randomNumber = String.format("%04d", Integer.valueOf(r.nextInt(9001)))
        getContext().authentication.name.let { username ->
            iUserRepository.findByUserName(username).let { userDetails ->
                waiverApp.createdBy = userDetails?.firstName + " " + userDetails?.lastName
                waiverApp.createdOn = Timestamp.from(Instant.now())
                waiverApp.status = 1
                waiverApp.serialNo = randomNumber
                iwaiversApplicationRepo.save(waiverApp)
                    .let { w ->
                        document1?.forEach { file ->
                            with(waiverDocs) {
//                                name = file.let { qualityAssuranceDaoServices.saveDocuments(null, null, null, w, it) }
                                fileType = file.contentType
                                documentType = file.bytes
                                status = 1
                                createdBy = w.createdBy
                                createdOn = Timestamp.from(Instant.now())
                                waiverId = w.id
                                iPvocWaiversApplicationDocumentRepo.save(waiverDocs)
                            }
                        }
                        return "redirect:/api/di/pvoc/add-musterlist-details/${w.id}"
                    }
            }
        }
    }

    @GetMapping("add-musterlist-details/{waiverId}")
    fun addMustarList(@PathVariable("waiverId") waiverId: Long, model: Model): String {
        iwaiversApplicationRepo.findByIdOrNull(waiverId)?.let { waiver ->
            model.addAttribute("waiverDetails", waiver)
            model.addAttribute("masterLists", iPvocMasterListRepo.findAllByWaiversApplicationId(waiverId))
            model.addAttribute("masterList", PvocMasterListEntity())
            model.addAttribute("waiver", PvocWaiversApplicationEntity())
            return "destination-inspection/pvoc/WaiversMasterListEntry"
        } ?: throw Exception("Waiver application does not exist")
    }

    @PostMapping("waivers-master-save/")
    fun saveMasterList(@RequestParam("id") id: Long, masterList: PvocMasterListEntity): String {
        getContext().authentication.name.let { username ->

            iwaiversApplicationRepo.findByIdOrNull(id)?.let { waiver ->
                masterList.waiversApplicationId = id
                masterList.createdOn = Timestamp.from(Instant.now())
                masterList.createdBy = waiver.createdBy
                masterList.status = 1
                iPvocMasterListRepo.save(masterList)
                return "redirect:/api/di/pvoc/add-musterlist-details/${id}"
            } ?: throw Exception("Waivers application with $id id does not exist")
        }
    }

    @PostMapping("finish-waiver-application/{id}")
    fun finishWaiverApplication(@PathVariable("id") id: Long): String {
        val dateFrom =
            Date.valueOf(LocalDate.parse(LocalDate.now().toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd")))
        val dateTo =
            Date.valueOf(LocalDate.parse(LocalDate.now().toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd")))
        iwaiversApplicationRepo.findByIdOrNull(id)?.let { waiverApp ->
            waiverApp.wetcMember = 720L
            waiverApp.serialNo = pvocDaoServices.generateRandomNumbers("PVOC-WAIVER")
            iwaiversApplicationRepo.save(waiverApp)
            getLoggedInUser()?.id?.let { it ->
                KotlinLogging.logger { }.info { "Entity id $id and impoter id is $it" }
                userRolesService.getUserId("WAIVERS_APPLICATION_REVIEW")?.let { it1 ->
                    pvocBpmn.startPvocWaiversApplicationsProcess(
                        id, it,
                        it1
                    )
                }
                userRolesService.getUserId("WAIVERS_APPLICATION_REVIEW")?.let { it1 ->
                    pvocBpmn.pvocWaSubmitApplicationComplete(
                        id,
                        it1
                    )
                }

            }
            return "redirect:/api/di/pvoc/all-waivers-applications-list?currentPage=0&pageSize=10&fromDate=${dateFrom}&toDate=${dateTo}&filter=123"
        } ?: throw Exception("Application does not exist")
    }


    @GetMapping("all-waivers-applications-list")
    fun waiversApplicationLists(
        model: Model,
        @RequestParam(value = "fromDate", required = false) fromDate: String,
        @RequestParam(value = "toDate", required = false) toDate: String,
        @RequestParam(value = "filter", required = false) filter: String,
        @RequestParam(value = "currentPage", required = false) currentPage: Int,
        @RequestParam(value = "pageSize", required = false) pageSize: Int
    ): String {

        val dateFrom = Date.valueOf(LocalDate.parse(fromDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")))
        val dateTo = Date.valueOf(LocalDate.parse(toDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")))

        PageRequest.of(currentPage, pageSize)
            .let { page ->
                getContext().authentication
                    ?.let { auth ->
                        when {
                            auth.authorities.stream()
                                .anyMatch { authority -> authority.authority == "MAKE_WAIVER_APPLICATION" } -> {
                                getLoggedInUser().let { userDetails ->
                                    when (filter) {
                                        "filter" -> {
                                            iwaiversApplicationRepo.findAllByStatusAndApplicantNameAndCreatedOnBetweenOrderByCreatedOnDesc(
                                                1,
                                                userDetails?.firstName + " " + userDetails?.lastName,
                                                dateFrom,
                                                dateTo,
                                                page
                                            )?.let { waivers ->
                                                model.addAttribute("waivers", waivers)
//                                                return "destination-inspection/pvoc/WaiversApplications"
                                            } ?: throw Exception("You have waiver applications")
                                        }
                                        else -> {
                                            iwaiversApplicationRepo.findAllByStatusAndApplicantNameOrderByCreatedOnDesc(
                                                1,
                                                userDetails?.firstName + " " + userDetails?.lastName,
                                                page
                                            )?.let { waivers ->
                                                model.addAttribute("waivers", waivers)
//                                                return "destination-inspection/pvoc/WaiversApplications"
                                            } ?: throw Exception("You have waiver applications")
                                        }

                                    }
                                }

                            }
                            auth.authorities.stream()
                                .anyMatch { authority -> authority.authority == "WAIVERS_APPLICATION_REPORT_GENERATE" || authority.authority == "PVOC_APPLICATION_PROCESS" || authority.authority == "WAIVERS_APPLICATION_REVIEW" } -> {
                                when (filter) {
                                    "filter" -> {
                                        iwaiversApplicationRepo.findAllByStatusAndCreatedOnBetweenOrderByCreatedOnDesc(
                                            1,
                                            dateFrom,
                                            dateTo,
                                            page
                                        )?.let { waivers ->
                                            model.addAttribute("waivers", waivers)
//                                            return "destination-inspection/pvoc/WaiversApplications"
                                        } ?: throw Exception("You have waiver applications")
                                    }
                                    else -> {
                                        getLoggedInUser()
                                            ?.let { loggedInUser ->
                                                loggedInUser.id?.let {
                                                    getTasks(it).let { tasks ->
                                                        KotlinLogging.logger { }.info { "Tasks " + tasks.count() }
                                                        val listPage: PagedListHolder<*> =
                                                            PagedListHolder<PvocWaiversApplicationEntity?>(
                                                                tasks,
                                                                MutableSortDefinition(false)
                                                            )
                                                        listPage.pageSize = page.pageSize // number of items per page
                                                        listPage.page = page.pageNumber
                                                        model.addAttribute("waivers", listPage.pageList)
                                                    }
                                                }

                                            } ?: throw Exception("Please login")
                                    }
                                }
                            }
                            else -> throw SupervisorNotFoundException("Only users with the following privilege PVOC Appliaction READ or PVOC APPLICATION PROCESS, can access this page")
                        }
                    }
            }
        return "destination-inspection/pvoc/WaiversApplications"
    }

    @GetMapping("all-waivers-applications-list/{id}")
    fun waiverApplicationDetails(@PathVariable("id") id: Long, model: Model): String {
        iwaiversApplicationRepo.findByIdOrNull(id)?.let { waiver ->
            model.addAttribute("masterLists", iPvocMasterListRepo.findAllByWaiversApplicationId(waiver.id))
            model.addAttribute("waiverDetails", waiver)
            model.addAttribute("remarkData", PvocWaiversRemarksEntity())
            return "destination-inspection/pvoc/WaiversApplicationDetails"
        }
            ?: throw Exception("Waiver with $id does not exist")
    }

    @GetMapping("aprrove-waivers-application-by-wetc-chiarman/{id}")
    fun wetcChairManApproval(
        @PathVariable("id") id: Long,
        pvocWaiversApplicationEntity: PvocWaiversApplicationEntity
    ): String {
        iwaiversApplicationRepo.findByIdOrNull(id)?.let { waiver ->
            waiver.reviewStatus = waiversStatus?.approval
            waiver.modifiedBy = waiversStatus?.wetcChairman
            waiver.approvalStatus = waiversStatus?.approvalStatus
            waiver.modifiedOn = Timestamp.from(Instant.now())
            iwaiversApplicationRepo.save(waiver)
            return "redirect:/api/di/pvoc/all-waivers-applications-list/$id"
        } ?: throw Exception("Waiver with $id does not exist")

    }

    @GetMapping("defer-waivers-application-by-wetc-chiarman/{id}")
    fun wetcChairManDeferal(@PathVariable("id") id: Long): String {
        iwaiversApplicationRepo.findByIdOrNull(id)?.let { waiver ->
            waiver.reviewStatus = waiversStatus?.defferal
            waiver.modifiedBy = waiversStatus?.wetcChairman
            waiver.approvalStatus = waiversStatus?.defferalStatus
            waiver.modifiedOn = Timestamp.from(Instant.now())
            iwaiversApplicationRepo.save(waiver)
            return "redirect:/api/di/pvoc/all-waivers-applications-list/${id}"
        } ?: throw Exception("Waiver with $id does not exist")
    }

    @GetMapping("reject-waivers-application-by-wetc-chiarman/{id}")
    fun wetcChairManReject(@PathVariable("id") id: Long): String {
        iwaiversApplicationRepo.findByIdOrNull(id)?.let { waiver ->
            waiver.reviewStatus = waiversStatus?.rejection
            waiver.modifiedBy = waiversStatus?.wetcChairman
            waiver.approvalStatus = waiversStatus?.rejectionStatus
            waiver.modifiedOn = Timestamp.from(Instant.now())
            iwaiversApplicationRepo.save(waiver)
            return "redirect:/api/di/pvoc/all-waivers-applications-list/${id}"
        } ?: throw Exception("Waiver with ${id} does not exist")
    }

    @PostMapping("waivers-review-remarks/{id}")
    fun waiverReviewRemarks(@PathVariable("id") id: Long, remarkData: PvocWaiversRemarksEntity): String {
        val user = commonDaoServices.loggedInUserDetails()
        remarkData.firstName = user.firstName
        remarkData.lastName = user.lastName
        remarkData.createdBy = user.firstName + " " + user.lastName
        remarkData.createdOn = Timestamp.from(Instant.now())
        remarkData.status = 1L
        remarkData.waiverId = id
        user.id?.let {
            remarkData.role = iUserRolesRepository.findByIdOrNull(
                iUserRoleAssignmentsRepository.findByUserIdAndStatus(it, 1)?.get(0)?.roleId
            )?.roleName
        }
        iPvocWaiversRemarksRepo.save(remarkData)
        //passing the process to wetc secretary
        userRolesService.getUserId("WAIVERS_APPLICATION_REPORT_GENERATE")?.let {
            pvocBpmn.pvocWaReviewApplicationComplete(
                id,
                it
            )
        }
        return "redirect:/api/di/pvoc/all-waivers-applications-list/${id}"
    }

    @GetMapping("waivers-remarks-view/{id}/{remarkType}")
    fun waiversRemarksView(
        @PathVariable("id") id: Long,
        @PathVariable("remarkType") remarkType: String,
        model: Model
    ): String {
        when (remarkType) {
            "waiver" -> {
                model.addAttribute("message", "Remarks for Waivers Application. Reference No : $id")
                model.addAttribute("remarks", iPvocWaiversRemarksRepo.findAllByWaiverId(id))
            }
            "report" -> {
                model.addAttribute("message", "Remarks for Waivers Application Report. Reference No : $id")
                model.addAttribute("remarks", iPvocWaiversRemarksRepo.findAllByWaiverReportId(id))
            }
            "minute" -> {
                model.addAttribute("message", "Remarks for Waivers Application Generated Minute. Reference No : $id")
                model.addAttribute("remarks", iPvocWaiversRemarksRepo.findAllByMinuteId(id))
            }
            "coc_timeline" -> {
                model.addAttribute(
                    "message",
                    "Recomendation Remarks for Waivers PvocTimeline Coc Detail. Reference No : $id"
                )
                model.addAttribute("remarks", iPvocWaiversRemarksRepo.findAllByCocTimelineId(id))
            }
        }
        return "destination-inspection/pvoc/WaiversApplicationRemarks"
    }

    //generating serial number
    private fun generatingRandomSerial(stringLen: Int): String {
        val charPool: CharRange = ('0'..'9')
        return ThreadLocalRandom.current()
            .ints(stringLen.toLong(), 0, charPool.step)
            .asSequence()
            .joinToString("")

    }


    @GetMapping("generate-waivers-report")
    fun waiversReportGenerate(
        @RequestParam("id") id: Long,
        model: Model,
        reportData: PvocWaiversReportsEntity,
        pvocWaiverDocument: PvocWaiversApplicationDocumentsEntity
    ): String {
        getLoggedInUser()?.let { user ->
            reportData.createdBy = user.firstName + " " + user.lastName
            reportData.createdOn = Timestamp.from(Instant.now())
        }
        iwaiversApplicationRepo.findByIdOrNull(id)?.let { waiver ->
            reportData.applicant = waiver.applicantName
            reportData.applicantJustfication = waiver.justification
            reportData.description = waiver.productDescription
            reportData.wetcSerial = generatingRandomSerial(3) + "-" + generatingRandomSerial(3)  //192-123"
            reportData.waiverId = id
            reportData.status = 1
            iPvocWaiversReportRepo.save(reportData)
            KotlinLogging.logger { }.info { "Doc id is $id" }
            userRolesService.getUserId("WAIVER_APPLICATION_REPORT_PROCESS")?.let {
                pvocBpmn.pvocWaGenerateReportComplete(
                    id,
                    it
                )
            }
            return "redirect:/api/di/pvoc/all-waivers-applications-list/${id}"
        } ?: throw Exception("Waivers with that $id id does not exist")
    }

    @GetMapping("waivers-reports")
    fun waiversReports(
        model: Model,
        @RequestParam(value = "fromDate", required = false) fromDate: String,
        @RequestParam(value = "toDate", required = false) toDate: String,
        @RequestParam(value = "filter", required = false) filter: String,
        @RequestParam(value = "currentPage", required = false) currentPage: Int,
        @RequestParam(value = "pageSize", required = false) pageSize: Int
    ): String {
        Date.valueOf(LocalDate.parse(fromDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")))
        Date.valueOf(LocalDate.parse(toDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")))
        PageRequest.of(currentPage, pageSize)
            .let { page ->
                getContext().authentication
                    ?.let { auth ->
                        when {
                            auth.authorities.stream()
                                .anyMatch { authority -> authority.authority == "PVOC_APPLICATION_READ" || authority.authority == "WAIVER_APPLICATION_REPORT_PROCESS" || authority.authority == "PVOC_APPLICATION_PROCESS" } -> {
                                when (filter) {
                                    "filter" -> {
                                        iPvocWaiversReportRepo.findAllByStatus(1, page).let { waivers ->
                                            model.addAttribute("waiversReports", waivers)
                                        }
                                    }
                                    else -> {
                                        iPvocWaiversReportRepo.findAllByStatus(1, page).let { waivers ->
                                            model.addAttribute("waiversReports", waivers)
                                        }
                                    }
                                }

                            }
                            else -> throw SupervisorNotFoundException("You can't access this page")
                        }
                        return "destination-inspection/pvoc/WaiverApplicationReports"
                    } ?: throw Exception("You must be loggedIn to access this page")

            }
    }

    @GetMapping("waivers-reports/{id}")
    fun waiverReportDetails(@PathVariable("id") id: Long, model: Model): String {
        iPvocWaiversReportRepo.findByIdOrNull(id)?.let { report ->
            model.addAttribute("remarksData", iPvocWaiversRemarksRepo.findAllByWaiverReportId(id))
            model.addAttribute("remarks", PvocWaiversReportsEntity())
            model.addAttribute("waiverRemarks", report.waiverId?.let { iPvocWaiversRemarksRepo.findAllByWaiverId(it) })
            model.addAttribute("documents", iPvocWaiversApplicationDocumentRepo.findAllByWaiverId(report.id))
            model.addAttribute("report", report)
            return "destination-inspection/pvoc/WaiverApplicationReportDetail"
        } ?: throw Exception("Report with $id id does not exist")
    }

    @PostMapping("waivers-reports-remarks/{id}/{remarksType}")
    fun waiversReportRemarks(
        @PathVariable("id") id: Long,
        @PathVariable("remarksType") remarksType: String,
        remarks: PvocWaiversReportsEntity
    ): String {
        iPvocWaiversReportRepo.findByIdOrNull(id)
            ?.let { report ->
                val remarkData = PvocWaiversRemarksEntity()
                val user = commonDaoServices.loggedInUserDetails()
                remarkData.firstName = user.firstName
                remarkData.lastName = user.lastName
                remarkData.createdBy = user.firstName + " " + user.lastName
                remarkData.createdOn = Timestamp.from(Instant.now())
                remarkData.status = 1L
                remarkData.waiverId = id
                user.id?.let {
                    remarkData.role = iUserRolesRepository.findByIdOrNull(
                        iUserRoleAssignmentsRepository.findByUserIdAndStatus(it, 1)?.get(0)?.roleId
                    )?.roleName
                }
                iwaiversApplicationRepo.findByIdOrNull(report.waiverId)
                    ?.let { waiver ->
                        remarkData.waiverReportId = id
                        when (remarksType) {
                            "recommend" -> {
                                remarkData.remarks = remarks.wetcRecomendation
                                report.reviewStatus = waiversStatus?.approval
                                report.waiverId = id
                                waiver.reviewStatus = waiversStatus?.approval
                                waiver.approvalStatus = waiversStatus?.approvalStatus
                                waiver.rejectionStatus = 0
                                waiver.defferalStatus = 0
                                iwaiversApplicationRepo.save(waiver)
                                pvocBpmn.pvocWaApproveWaiverComplete(id, 740)
                            }
                            "reject" -> {
                                remarkData.remarks = remarks.wetcRecomendation
                                report.reviewStatus = waiversStatus?.rejection
                                report.waiverId = id
                                waiver.reviewStatus = waiversStatus?.rejection
                                waiver.approvalStatus = waiversStatus?.rejectionStatus
                                waiver.approvalStatus = 0
                                waiver.defferalStatus = 0
                                iwaiversApplicationRepo.save(waiver)
                                pvocBpmn.pvocWaApproveWaiverComplete(id, 740)
                            }
                            "differ" -> {
                                remarkData.remarks = remarks.wetcRecomendation
                                report.reviewStatus = waiversStatus?.defferal
                                waiver.reviewStatus = waiversStatus?.defferal
                                report.waiverId = id
                                waiver.defferalStatus = waiversStatus?.defferalStatus
                                waiver.approvalStatus = 0
                                waiver.rejectionStatus = 0
                                iwaiversApplicationRepo.save(waiver)
                                pvocBpmn.pvocWaGenerateDeferralComplete(id)
//                                pvocBpmn.pvocWaApproveWaiverComplete(id, 740)
                            }
                            else -> {
                                remarkData.remarks = remarks.wetcRecomendation
                                iwaiversApplicationRepo.save(waiver)
                            }
                        }
                    }
                report.let { iPvocWaiversReportRepo.save(it) }
                iPvocWaiversRemarksRepo.save(remarkData)
            }
        return "redirect:/api/di/pvoc/waivers-reports/${id}"
    }

    @GetMapping("generate-minutes/")
    fun generateMinutes(@RequestParam("id") id: Long, model: Model): String {
        model.addAttribute("waiverId", id)
        model.addAttribute("minute", PvocWaiversWetcMinutesEntity())
        return "destination-inspection/pvoc/WaiversWETCMinutes"
    }

    @PostMapping("save-generated-minutes")
    fun saveGeneratedMinutes(@ModelAttribute minute: PvocWaiversWetcMinutesEntity): String {
        val dateFrom =
            Date.valueOf(LocalDate.parse(LocalDate.now().toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd")))
        val dateTo =
            Date.valueOf(LocalDate.parse(LocalDate.now().toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd")))
        getLoggedInUser().let { userDeails ->
            minute.createdBy = userDeails?.firstName + " " + userDeails?.lastName
            minute.createdOn = Timestamp.from(Instant.now())
            minute.status = 1

            iPvocWaiversWetcMinutesEntityRepo.save(minute)
            minute.waiverId?.let { it2 ->
                userRolesService.getUserId("WAIVERS_MINUTES_REVIEW_NCS")?.let {
                    pvocBpmn.pvocWaGenerateMinutesComplete(
                        it2,
                        it
                    )
                }
            }
//            minute.waiverId?.let { it2 -> pvocBpmn.pvocWaGenerateMinutesComplete(it2, 716) }
            minute.waiverId?.let { it2 ->
                userRolesService.getUserId("WAIVERS_MINUTES_PROCESS_NCS")?.let {
                    pvocBpmn.pvocWaGenerateMinutesComplete(
                        it2,
                        it
                    )
                }
            }
//            minute.waiverId?.let { it2 -> pvocBpmn.pvocWaGenerateMinutesComplete(it2, 742) }
            return "redirect:/api/di/pvoc/waiversWETCMinutes?currentPage=0&pageSize=10&fromDate=${dateFrom}&toDate=${dateTo}"
        }
    }

    @GetMapping("generate-waiver-request-letter/")
    fun generateWaiverRequestLetter(@RequestParam("id") id: Long, model: Model): String {
        model.addAttribute("waiverId", id)
        model.addAttribute("request_letter", PvocWaiversRequestLetterEntity())
        return "destination-inspection/pvoc/WaiverRequestLetter"
    }

    @PostMapping("save-generated-waiver-request-letter")
    fun saveGeneratedWaiverRequestLetter(@ModelAttribute request_letter: PvocWaiversRequestLetterEntity): String {
        getLoggedInUser().let { userDeails ->
            request_letter.createdBy = userDeails?.firstName + " " + userDeails?.lastName
            request_letter.createdOn = Timestamp.from(Instant.now())
            request_letter.status = 1
            iPvocWaiversRequestLetterRepo.save(request_letter)
//            request_letter.waiverId?.let { it2 -> pvocBpmn.pvocWaGenerateWaiverReqLetterComplete(it2, 740) }
            request_letter.waiverId?.let { it2 ->
                userRolesService.getUserId("WAIVERS_APPLICATION_REPORT_GENERATE")?.let {
                    pvocBpmn.pvocWaGenerateWaiverReqLetterComplete(
                        it2,
                        it
                    )
                }
            }
            return "redirect:/api/di/pvoc/waiver-request-letters-view?currentPage=0&pageSize=10"
        }
    }

    @GetMapping("waiver-request-letters-view")
    fun waiversRequestLetters(
        model: Model,
        @RequestParam(value = "currentPage", required = false) currentPage: Int,
        @RequestParam(value = "pageSize", required = false) pageSize: Int
    ): String {
        PageRequest.of(currentPage, pageSize)
            .let { page ->
                getContext().authentication
                    ?.let { auth ->
                        when {
                            auth.authorities.stream()
                                .anyMatch { authority -> authority.authority == "PVOC_APPLICATION_READ" || authority.authority == "WAIVERS_MINUTES_PROCESS_NCS" || authority.authority == "PVOC_APPLICATION_PROCESS" } -> {
                                iPvocWaiversRequestLetterRepo.findAllByStatus(1, page).let { request_letters ->
                                    model.addAttribute("request_letters", request_letters)
                                }
                            }
                            else -> throw SupervisorNotFoundException("You can't access this page")
                        }
                        return "destination-inspection/pvoc/WaiversRequestsLetters"
                    } ?: throw Exception("You must be loggedIn to access this page")

            }
    }

    @GetMapping("waiver-request-letters-view/{id}")
    fun waiversRequestLetterView(
        model: Model,
        @PathVariable("id") id: Long
    ): String {
        iPvocWaiversRequestLetterRepo.findByIdOrNull(id)?.let { requestLetter ->
            waiversStatus?.approval?.let {
                iwaiversApplicationRepo.findAllByReviewStatusOrderByCreatedOnDesc(it)
                    .let { recomendended ->
                        model.addAttribute("recomendended", recomendended)
                        model.addAttribute("count", recomendended?.count())
                    }
            }
            waiversStatus?.rejection?.let {
                iwaiversApplicationRepo.findAllByReviewStatusOrderByCreatedOnDesc(it)
                    .let { rejected ->
                        model.addAttribute("rejected", rejected)
                        model.addAttribute("count", rejected?.count())
                    }
            }
            waiversStatus?.defferal?.let {
                iwaiversApplicationRepo.findAllByReviewStatusOrderByCreatedOnDesc(it)
                    .let { differed ->
                        model.addAttribute("differed", differed)
                        model.addAttribute("count", differed?.count())
                    }
            }
            model.addAttribute("requestLetter", requestLetter)
            return "destination-inspection/pvoc/WaiverRequestLetterDetailView"
        } ?: throw Exception("Request letter with $id id does not exist")
    }

    @GetMapping("waiversWETCMinutes")
    fun waiversWETCMinutes(
        model: Model,
        @RequestParam(value = "fromDate", required = false) fromDate: String,
        @RequestParam(value = "toDate", required = false) toDate: String,
        @RequestParam(value = "filter", required = false) filter: String,
        @RequestParam(value = "currentPage", required = false) currentPage: Int,
        @RequestParam(value = "pageSize", required = false) pageSize: Int
    ): String {
        val dateFrom = Date.valueOf(LocalDate.parse(fromDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")))
        val dateTo = Date.valueOf(LocalDate.parse(toDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")))
        PageRequest.of(currentPage, pageSize)
            .let { page ->
                getContext().authentication
                    .let { auth ->
                        when {
                            auth.authorities.stream()
                                .anyMatch { authority -> authority.authority == "PVOC_APPLICATION_READ" || authority.authority == "WAIVERS_MINUTES_REVIEW_NCS" || authority.authority == "WAIVERS_MINUTES_PROCESS_NCS" || authority.authority == "PVOC_APPLICATION_PROCESS" } -> {
                                when (filter) {
                                    "filter" -> {
                                        iPvocWaiversWetcMinutesEntityRepo.findAllByStatusAndCreatedOnBetweenOrderByCreatedOnDesc(
                                            1,
                                            dateFrom,
                                            dateTo,
                                            page
                                        ).let { waiversMunutes ->
                                            model.addAttribute("waiversReports", waiversMunutes)
                                            return "destination-inspection/pvoc/WETC_WaiversMinutes"
                                        }
                                    }
                                    else -> {
                                        iPvocWaiversWetcMinutesEntityRepo.findAllByStatusOrderByCreatedOnDesc(1, page)
                                            .let { waiversMunutes ->
                                                model.addAttribute("waiversReports", waiversMunutes)
                                                return "destination-inspection/pvoc/WETC_WaiversMinutes"
                                            }
                                    }
                                }
                            }
                            else -> throw SupervisorNotFoundException("Only users with the following privilege PVOC Appliaction READ or PVOC APPLICATION PROCESS, can access this page")
                        }
                    }
            }
    }

    @GetMapping("waiversWETCMinutes/{id}")
    fun waiverWETCMinutesDetails(@PathVariable("id") id: Long, model: Model): String {
        iPvocWaiversWetcMinutesEntityRepo.findByIdOrNull(id)?.let { report ->
            model.addAttribute("remarks", PvocWaiversReportsEntity())
            model.addAttribute("report", report)
            return "destination-inspection/pvoc/WETC_WaiversMinutesDetails"
        } ?: throw Exception("Report with $id id does not exist")
    }

    @PostMapping("waivers-minutes-remarks/{id}/{remarksType}")
    fun waiversMinutesRemarks(
        @PathVariable("id") id: Long,
        @PathVariable("remarksType") remarksType: String,
        remarks: PvocWaiversReportsEntity
    ): String {
        iPvocWaiversWetcMinutesEntityRepo.findByIdOrNull(id)
            ?.let { minute ->
                val remarkData = PvocWaiversRemarksEntity()
                val user = commonDaoServices.loggedInUserDetails()
                remarkData.firstName = user.firstName
                remarkData.lastName = user.lastName
                remarkData.createdBy = user.firstName + " " + user.lastName
                remarkData.createdOn = Timestamp.from(Instant.now())
                remarkData.status = 1L
                remarkData.waiverId = minute.waiverId

                user.id?.let {
                    remarkData.role = iUserRolesRepository.findByIdOrNull(
                        iUserRoleAssignmentsRepository.findByUserIdAndStatus(it, 1)?.get(0)?.roleId
                    )?.roleName
                }
                iwaiversApplicationRepo.findByIdOrNull(minute.waiverId)
                    ?.let { waiver ->
                        when (remarksType) {
                            "recommend" -> {
                                remarkData.remarks = remarks.wetcRecomendation
                                minute.reviewStatus = waiversStatus?.approval
                                waiver.reviewStatus = waiversStatus?.approval
                                waiver.approvalStatus = waiversStatus?.approvalStatus
                                waiver.rejectionStatus = 0
                                waiver.defferalStatus = 0
                                iwaiversApplicationRepo.save(waiver)
                                userRolesService.getUserId("WAIVERS_MINUTES_PROCESS_NCS")?.let {
                                    pvocBpmn.pvocWaNscApproveComplete(
                                        waiver.id,
                                        it
                                    )
                                }
//                                pvocBpmn.pvocWaNscApproveComplete(waiver.id, 742)
                                userRolesService.getUserId("WAIVERS_APPLICATION_REPORT_GENERATE")?.let {
                                    pvocBpmn.pvocWaGenerateWaiverReqLetterComplete(
                                        waiver.id,
                                        it
                                    )
                                }
//                                pvocBpmn.pvocWaGenerateWaiverReqLetterComplete(waiver.id, 740)
                            }
                            "reject" -> {
                                remarkData.remarks = remarks.wetcRecomendation
                                minute.reviewStatus = waiversStatus?.rejection
                                waiver.reviewStatus = waiversStatus?.rejection
                                waiver.approvalStatus = waiversStatus?.rejectionStatus
                                waiver.approvalStatus = 0
                                waiver.defferalStatus = 0
                                iwaiversApplicationRepo.save(waiver)
                                userRolesService.getUserId("WAIVERS_MINUTES_PROCESS_NCS")?.let {
                                    pvocBpmn.pvocWaNscApproveComplete(
                                        waiver.id,
                                        it
                                    )
                                }
//                                pvocBpmn.pvocWaNscApproveComplete(waiver.id, 742)
                                userRolesService.getUserId("WAIVERS_APPLICATION_REPORT_GENERATE")?.let {
                                    pvocBpmn.pvocWaGenerateWaiverReqLetterComplete(
                                        waiver.id,
                                        it
                                    )
                                }
//                                pvocBpmn.pvocWaGenerateWaiverReqLetterComplete(waiver.id, 740)
                            }
                            "differ" -> {
                                remarkData.remarks = remarks.wetcRecomendation
                                minute.reviewStatus = waiversStatus?.defferal
                                waiver.reviewStatus = waiversStatus?.defferal
                                waiver.defferalStatus = waiversStatus?.defferalStatus
                                waiver.approvalStatus = 0
                                waiver.rejectionStatus = 0
                                iwaiversApplicationRepo.save(waiver)
                                userRolesService.getUserId("WAIVERS_MINUTES_PROCESS_NCS")?.let {
                                    pvocBpmn.pvocWaNscApproveComplete(
                                        waiver.id,
                                        it
                                    )
                                }
//                                pvocBpmn.pvocWaNscApproveComplete(waiver.id, 742)
                                userRolesService.getUserId("WAIVERS_APPLICATION_REPORT_GENERATE")?.let {
                                    pvocBpmn.pvocWaGenerateWaiverReqLetterComplete(
                                        waiver.id,
                                        it
                                    )
                                }
//                                pvocBpmn.pvocWaGenerateWaiverReqLetterComplete(waiver.id, 740)
                            }
                            else -> {
                                remarkData.remarks = remarks.wetcRecomendation
                                iwaiversApplicationRepo.save(waiver)
                                userRolesService.getUserId("WAIVERS_MINUTES_PROCESS_NCS")?.let {
                                    pvocBpmn.pvocWaNscApproveComplete(
                                        waiver.id,
                                        it
                                    )
                                }
//                                pvocBpmn.pvocWaNscApproveComplete(waiver.id, 742)
//                                pvocBpmn.pvocWaGenerateMinutesComplete(waiver.id, 742 )
                            }
                        }
//                                waiver.let { iwaiversApplicationRepo.save(it) }
                    } ?: throw Exception("waiver application does not exists")
                minute.let { iPvocWaiversWetcMinutesEntityRepo.save(it) }
                iPvocWaiversRemarksRepo.save(remarkData)
                return "redirect:/api/di/pvoc/waiversWETCMinutes/${id}"
            } ?: throw Exception("Minutes does not exists")
    }

    @PostMapping("save-master-list-uploaded")
    fun saveMasterListUploaded(
        @RequestParam("masterList") masterList: MultipartFile,
        @RequestParam("id") id: Long,
        redirectAttributes: RedirectAttributes
    ): String {
        val pvocWaiversApplicationDocumentsEntity = PvocWaiversApplicationDocumentsEntity()
        iwaiversApplicationRepo.findByIdOrNull(id)?.let { w ->
            with(pvocWaiversApplicationDocumentsEntity) {
//                name = masterList.let { qualityAssuranceDaoServices.saveDocuments(null, null, null, w, it) }
                fileType = masterList.contentType
                documentType = masterList.bytes
                reason = "application"
                status = 1
                waiverId = w.id
                createdBy = w.createdBy
                reason = "master"
                pvocWaiversApplicationDocumentsEntity.createdOn = w.createdOn
                iPvocWaiversApplicationDocumentRepo.save(pvocWaiversApplicationDocumentsEntity)
                return "redirect:/api/di/pvoc/add-musterlist-details/${id}"
            }
        } ?: throw Exception("The waiver application with {id} does not exist")
    }

    @GetMapping("/load/uploads")
    fun downloadWorkPlanDocument(
        response: HttpServletResponse,
        @RequestParam("id") id: Long
    ) {
        iPvocWaiversApplicationDocumentRepo.findFirstByWaiverIdAndReason(id, "master")
            .let { doc ->
                response.contentType = doc?.fileType
//                    response.setHeader("Content-Length", pdfReportStream.size().toString())
                response.addHeader("Content-Dispostion", "inline; filename=${doc?.name};")
                response.outputStream
                    .let { responseOutputStream ->
                        responseOutputStream.write(doc?.documentType)
                        responseOutputStream.close()
                    }
            }

    }

}

