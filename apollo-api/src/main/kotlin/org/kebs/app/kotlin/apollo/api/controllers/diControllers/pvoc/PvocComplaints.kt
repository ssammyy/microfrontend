package org.kebs.app.kotlin.apollo.api.controllers.diControllers.pvoc

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.exceptions.SupervisorNotFoundException
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.repo.*
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.sql.Date
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.jvm.internal.Intrinsics

@Controller
@RequestMapping("/api/di/pvoc/")
class PvocComplaints(
    private val iPvocComplaintRepo: IPvocComplaintRepo,
    private val iUserRepository: IUserRepository,
    private val commonDaoServices: CommonDaoServices,
    private val notifications: Notifications,
    private val iCocsRepository: ICocsRepository,
    private val iPvocQuerriesRepository: IPvocQuerriesRepository,
    private val iCocItemRepository: ICocItemRepository,
    applicationMapProperties: ApplicationMapProperties,
    private val iPvocComplaintCertificationsSubCategoryRepo: IPvocComplaintCertificationsSubCategoryRepo,
    private val iPvocComplaintCategoryRepo: IPvocComplaintCategoryRepo,
    private val iUserRoleAssignmentsRepository: IUserRoleAssignmentsRepository,
    private val iRfcCocEntityRepo: IRfcCocEntityRepo,
    private val pvocComplaintRemarksEntityRepo: PvocComplaintRemarksEntityRepo,
    pvocComplainStatusEntityRepo: PvocComplainStatusEntityRepo
) {

    final val appId = applicationMapProperties.mapImportInspection

    final val statuses = pvocComplainStatusEntityRepo.findByStatus(1)

    @GetMapping("email_entry")
    fun emailEntry(model: Model): String {
        model.addAttribute("emailObject", PvocComplaintsEmailVerificationEntity())
        return "destination-inspection/pvoc/complaint/EmailTemplate"
    }

    @PostMapping("save_email")
    fun saveEmail(model: Model,
                  emailObject: PvocComplaintsEmailVerificationEntity) : String {
        val map = commonDaoServices.serviceMapDetails(appId)
        val sr: ServiceRequestsEntity
        val payload = "Complaint Verification Token [EMail for Verification = ${emailObject.email}]"
        sr = commonDaoServices.mapServiceRequestForSuccess(map, payload, null)
        val token = commonDaoServices.generateEmailVerificationToken(sr, emailObject, map)
        val messageBody = "Please Click the link Bellow \n" +
                "\n " +
//                "https://localhost:8006/pvoc/complaint/?token=${token.token}"
                "https://kims.kebs.org:8006/pvoc/complaint/?token=${token.token}"
        emailObject.email?.let { notifications.sendEmail(it, "Complaint Verification", messageBody) }
        return "redirect:/api/di/pvoc/email_entry"
    }

    @PreAuthorize("hasAuthority('PVOC_APPLICATION_READ') or hasAuthority('PVOC_APPLICATION_PROCESS')")
    @GetMapping("complaints-list")
    fun complaintsList(
        model: Model,
        @RequestParam(value = "fromDate", required = false) fromDate: String,
        @RequestParam(value = "toDate", required = false) toDate: String,
        @RequestParam(value = "filter", required = false) filter: String,
        @RequestParam(value = "currentPage", required = false) currentPage: Int,
        @RequestParam(value = "pageSize", required = false) pageSize: Int): String {

        val dateFrom = Date.valueOf(LocalDate.parse(fromDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")))
        val dateTo = Date.valueOf(LocalDate.parse(toDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")))
        PageRequest.of(currentPage, pageSize)
            .let { page ->
                SecurityContextHolder.getContext().authentication
                    ?.let { auth ->
                        when {
                            auth.authorities.stream().anyMatch { authority -> authority.authority == "PVOC_APPLICATION_PROCESS" } -> {
                                when(filter){
                                    "filter" ->{
                                        iPvocComplaintRepo.findAllByStatusAndCreatedOnBetweenOrderByCreatedOnDesc(1, dateFrom, dateTo, page).let { complaints ->
                                            model.addAttribute("complaints", complaints)
                                        }
                                    }
                                    else ->{
                                        iPvocComplaintRepo.findAllByStatusOrderByCreatedOnDesc(1,  page).let { complaints ->
                                            model.addAttribute("complaints", complaints)
                                        }
                                    }

                                }
                            }
                            else -> throw SupervisorNotFoundException("Only users with the following privilege PVOC Appliaction READ or PVOC APPLICATION PROCESS, can access this page")
                        }
                    }
                return "destination-inspection/pvoc/complaint/ComplaintsList"
            }
    }

    @GetMapping("complaint-details/{id}")
    fun complaintDetails(@PathVariable("id") id: Long, model: Model): String {
        val usersIds = mutableListOf<Long?>()
        iPvocComplaintRepo.findByIdOrNull(id)?.let { complaint ->
            iUserRoleAssignmentsRepository.findByRoleIdAndStatus(261, 1)?.let { mpvocs ->
                mpvocs.forEach { mpvoc ->
                    mpvoc.userId?.let { usersIds.add(it) }
                }
            }
            iUserRepository.findAllByIdIn(usersIds).let { mpvocs ->
                model.addAttribute("mpvocs", mpvocs)
            }
            complaint.rfcNo?.let {
                complaint.cocNo?.let { it1 ->
                    iPvocQuerriesRepository.findByCocNumberOrRfcNumber(
                        it1, it
                    ).let { enquires ->
                        KotlinLogging.logger {  }.info { "Queries ==> "+enquires?.count() }
                        model.addAttribute("enquires", enquires)
                    }
                }
            }
            model.addAttribute("complaint", complaint)
            model.addAttribute("complaintObj", PvocComplaintEntity())
            model.addAttribute("remarksObj", PvocComplaintRemarksEntity())
            return "destination-inspection/pvoc/complaint/ComplaintDetails"
        } ?: throw Exception("Complaint with {id} id does not exist")
    }

    @PostMapping("complaints-save")
    fun complaintsSave(complaint: PvocComplaintEntity,
                       @RequestParam("userId", required = false)  userId : String,
                       @RequestParam("categoryId", required = false) categoryId: String,
                       @RequestParam("subCategoryId", required = false) subCategoryId: String): String {
        userId.replace(",", "")
        iUserRepository.findByIdOrNull(userId.toLong())?.let { agent ->
            iPvocComplaintCategoryRepo.findByIdOrNull(categoryId.toLong()).let { category ->
                iPvocComplaintCertificationsSubCategoryRepo.findByIdOrNull(subCategoryId.toLong()).let { subCategory ->
                    complaint.compliantSubCategory = subCategory
                    complaint.compliantNature = category
                    complaint.pvocAgent = agent
                    complaint.createdBy = complaint.email
                    complaint.status = 1
                    complaint.createdOn = Timestamp.from(Instant.now())
                    val newComp = iPvocComplaintRepo.save(complaint)
                    val messageBody = " Hi ${complaint.complaintName} \n Thank for sending this complain. We will get back to you shortly"
                    complaint.email?.let { notifications.sendEmail(it, "Complaint  Submission", messageBody) }
                    val messageBody2 = "Please Click the link bellow to review the complaint \n" +
                            "\n " +
//                            "https://localhost:8006/pvoc/complaint-details/${newComp.id}"
                            "https://kims.kebs.org:8006/pvoc/complaint-details/${newComp.id}"
                    agent.email?.let { notifications.sendEmail(it, "Complaint  Submission", messageBody2) }
                    return "redirect:/"
                }
            }
        } ?: throw Exception("User not found")
    }


    @PostMapping("assign-mpvoc")
    fun assignMpvoc(
        @RequestParam("userId", required=false) userId : Long,
        @RequestParam("id", required = false) id: Long,
    ) : String {
        iPvocComplaintRepo.findByIdOrNull(id)?.let { complain ->
            iUserRepository.findByIdOrNull(userId)?.let { user ->
                complain.mpvoc = user
                iPvocComplaintRepo.save(complain)
                return "redirect:/api/di/pvoc/complaint-details/${id}"
            }
        }?: throw Exception("Compaint with $id id does not exist")
    }

    @PostMapping("assign-hod")
    fun assignHod(
        @RequestParam("userId", required=false) userId : Long,
        @RequestParam("id", required = false) id: Long,
    ) : String {
        iPvocComplaintRepo.findByIdOrNull(id)?.let { complain ->
            iUserRepository.findByIdOrNull(userId)?.let { user ->
                complain.hod = user
                iPvocComplaintRepo.save(complain)
                return "redirect:/api/di/pvoc/complaint-details/${id}"
            }
        }?: throw Exception("Compaint with $id id does not exist")
    }

    @GetMapping("coc-details")
    fun cocDetails(model: Model, @RequestParam("cocNo") cocNo : String) : String{
        iCocsRepository.findFirstByCocNumber(cocNo).let { cocDetails ->
            model.addAttribute("coc", cocDetails)
            model.addAttribute("coc_items", cocDetails?.id?.let { iCocItemRepository.findByCocId(it) })
            return "destination-inspection/pvoc/complaint/COC_Details"
        }

    }

    @GetMapping("rfc-details/{rfcNo}")
    fun rfcDetails(model: Model,@PathVariable("rfcNo") rfcNo: String) : String{
        iRfcCocEntityRepo.findByRfcNumber(rfcNo).let { cocRfc ->
            model.addAttribute("cocRfc", cocRfc)
            return "destination-inspection/pvoc/complaint/CocRFC"
        }
    }

    @PostMapping("mpvoc-remarks")
    fun approveWithRemarks(@RequestParam("id") id: Long, remarksObj: PvocComplaintRemarksEntity): String{
        iPvocComplaintRepo.findByIdOrNull(id)?.let { complaint ->
            remarksObj.complaintId = complaint.id
            remarksObj.userId = complaint.mpvoc
            remarksObj.createdBy = complaint.mpvoc?.firstName +' '+ complaint.mpvoc?.lastName
            remarksObj.createdOn = Timestamp.from(Instant.now())
            pvocComplaintRemarksEntityRepo.save(remarksObj)
            complaint.reviewStatus = "Rejected"
            iPvocComplaintRepo.save(complaint)
            return "redirect:/api/di/pvoc/complaint-details/${id}"
        }?: throw Exception("Complaint with ${id} id does not exist")
    }

    @GetMapping("view-remarks/{id}")
    fun viewRemarks(@PathVariable("id") id: Long, model: Model) : String {
        pvocComplaintRemarksEntityRepo.findAllByComplaintId(id)?.let { remarks ->
            model.addAttribute("remarks", remarks)
            return "destination-inspection/pvoc/complaint/RemarksView"
        }?: throw Exception("No complaints currently")
    }

    @PostMapping("remarks-approve")
    fun approveWithRemarksHod(@RequestParam("id") id: Long, remarksObj: PvocComplaintRemarksEntity): String{
        iPvocComplaintRepo.findByIdOrNull(id)?.let { complaint ->
            remarksObj.complaintId = complaint.id
            remarksObj.userId = complaint.hod
            remarksObj.createdBy = complaint.mpvoc?.firstName +' '+ complaint.mpvoc?.lastName
            remarksObj.createdOn = Timestamp.from(Instant.now())
            pvocComplaintRemarksEntityRepo.save(remarksObj)
            complaint.reviewStatus = statuses.approve
            iPvocComplaintRepo.save(complaint)
            return "redirect:/api/di/pvoc/complaint-details/${id}"
        }?: throw Exception("Complaint with ${id} id does not exist")
    }

    @PostMapping("remarks-reject")
    fun rejectWithRemarksHod(@RequestParam("id") id: Long, remarksObj: PvocComplaintRemarksEntity): String{
        iPvocComplaintRepo.findByIdOrNull(id)?.let { complaint ->
            remarksObj.complaintId = complaint.id
            remarksObj.userId = complaint.hod
            remarksObj.createdBy = complaint.mpvoc?.firstName +' '+ complaint.mpvoc?.lastName
            remarksObj.createdOn = Timestamp.from(Instant.now())
            pvocComplaintRemarksEntityRepo.save(remarksObj)
            complaint.reviewStatus = statuses.reject
            iPvocComplaintRepo.save(complaint)
            return "redirect:/api/di/pvoc/complaint-details/${id}"
        }?: throw Exception("Complaint with ${id} id does not exist")
    }


    @PostMapping("save-kebs-query-complain")
    fun saveKebsQuery(@RequestParam("cocId") cocId: Long, pvocQuery: PvocQueriesEntity): String {
        SecurityContextHolder.getContext().authentication.name.let { username ->
            iUserRepository.findByUserName(username).let { userDetails ->
                pvocQuery.invoiceNumber = pvocQuery.invoiceNumber ?:"No Invoice No"
                pvocQuery.rfcNumber = pvocQuery.rfcNumber ?:"No RFC No"
                pvocQuery.ucrNumber = pvocQuery.ucrNumber ?: "No UCR No"
                pvocQuery.createdBy = userDetails?.firstName + ' ' + userDetails?.lastName
                pvocQuery.status = 1
                pvocQuery.partnerResponceAnalysisStatus = 0
                pvocQuery.kebsReplyReplyStatus = 0
                pvocQuery.pvocAgentReplyStatus = 0
                pvocQuery.createdOn = Timestamp.from(Instant.now())
                iPvocQuerriesRepository.save(pvocQuery)
                return "redirect:/api/di/pvoc/complaint-details/${cocId}"
            }
        }
    }


    @PostMapping("recommendation")
    fun recommendation(@RequestParam("id") id: Long, complaintObj: PvocComplaintEntity): String{
        iPvocComplaintRepo.findByIdOrNull(id)?.let { complaint ->
            complaint.recomendation = complaintObj.recomendation
            iPvocComplaintRepo.save(complaint)
            return "redirect:/api/di/pvoc/complaint-details/${id}"
        }?: throw Exception("Document not found")
    }
}