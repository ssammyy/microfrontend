package org.kebs.app.kotlin.apollo.api.controllers.diControllers.pvoc

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.PvocBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.PvocDaoServices
import org.kebs.app.kotlin.apollo.api.service.UserRolesService
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.common.exceptions.PvocRemarksNotFoundException
import org.kebs.app.kotlin.apollo.common.exceptions.SupervisorNotFoundException
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.repo.*
import org.springframework.beans.support.MutableSortDefinition
import org.springframework.beans.support.PagedListHolder
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


class Manufacturer {
    var companyName: String? = null
    var email: String? = null
    var companyPinNo: String? = null
    var telephoneNo: String? = null
    var postalAadress: String? = null
    var physicalLocation: String? = null
    var contactPersorn: String? = null
}


class PermitProduct {
    var id: Long? = null
    var productName: String? = null
    var permitNumber: String? = null
    var permitId: Long? = null
    var expriryDate: String? = null
    var brandId: Long? = null
    var brandName: String? = null
    var status: Int? = 0
    var selectedProduct: Boolean = false
}

class RawMaterials {
    var hsCode: String? = null
    var rawMaterialDescription: String? = null
    var endProduct: String? = null
    var countryOfOrigin: String? = null

}

class MainMachinary {
    var hsCode: String? = null
    var machineDescription: String? = null
    var makeModel: String? = null
    var countryOfOrigin: String? = null
}


class Spares {
    var hsCode: String? = null
    var industrialSpares: String? = null
    var machineToFit: String? = null
    var countryOfOrigin: String? = null
}

class ExceptionPayload {
    var manufacturer: Manufacturer? = null
    var products: ArrayList<PermitProduct>? = null
    var rawMaterials: ArrayList<RawMaterials>? = null
    var mainMachinary: ArrayList<MainMachinary>? = null
    var spares: ArrayList<Spares>? = null
}

class RawMaterialsCheck {
    var rawMaterials: MutableList<PvocExceptionRawMaterialCategoryEntity?>? = null
}

class MachinerysCheck {
    var machineries: MutableList<PvocExceptionMainMachineryCategoryEntity?>? = null
    var machineriesArrayList: MutableList<PvocExceptionMainMachineryCategoryEntity> = ArrayList()
}

class SparesCheck {
    var spares: MutableList<PvocExceptionIndustrialSparesCategoryEntity?>? = null
    var sparesArrayList: MutableList<PvocExceptionIndustrialSparesCategoryEntity> = ArrayList()
}

@Controller
@RequestMapping("/api/di/pvoc/")
class DIPvocController(
    private val iPvocApplicationProductsRepo: IPvocApplicationProductsRepo,
    private val iPvocApplicationRepo: IPvocApplicationRepo,
    private val iManufacturerRepository: IManufacturerRepository,
    private val iUserRepository: IUserRepository,
    private val iRemarksRepository: IRemarksRepository,
    iPvocExceptionApplicationStatusEntityRepo: IPvocExceptionApplicationStatusEntityRepo,
    private val pvocBpmn: PvocBpmn,
    private val commonDaoServices: CommonDaoServices,
    private val pvocDaoServices: PvocDaoServices,
    private val iPvocExceptionIndustrialSparesCategoryEntityRepo: IPvocExceptionIndustrialSparesCategoryEntityRepo,
    private val iPvocExceptionMainMachineryCategoryEntityRepo: IPvocExceptionMainMachineryCategoryEntityRepo,
    private val iPvocExceptionRawMaterialCategoryEntityRepo: IPvocExceptionRawMaterialCategoryEntityRepo,
    private val userRolesService: UserRolesService


) {

    fun getTasks(userId: Long): MutableList<PvocApplicationEntity?> {
        pvocBpmn.fetchAllTasksByAssignee(userId)
            ?.let { listTaskDetails ->
                var tasks = mutableListOf<PvocApplicationEntity?>()
                val ids = mutableListOf<Long>()

                listTaskDetails.sortedByDescending { it.objectId }
                    .forEach { taskDetails ->
                        ids.add(taskDetails.objectId)
                    }
                iPvocApplicationRepo.findByIdIsIn(ids)?.let {
                    tasks = it.toMutableList()
                }
                return tasks
            } ?: throw Exception("Failed")
    }

    //Get the for for application
    val pvocReviewStatus = iPvocExceptionApplicationStatusEntityRepo.findFirstByMapId(1)

    @GetMapping("application")
    @PreAuthorize("hasAuthority('PVOC_APPLICATION_READ')")
    fun applicationForm(model: Model): String {
        return "destination-inspection/pvoc/ExceptionApplicationForm2"
    }

    //    @PreAuthorize("hasAuthority('PVOC_APPLICATION_READ') or hasAuthority('PVOC_APPLICATION_PROCESS')")
    @GetMapping("officer")
    fun officerExceptionApplicationFormsIndex(
        @RequestParam(value = "fromDate", required = false) fromDate: String?,
        @RequestParam(value = "toDate", required = false) toDate: String?,
        @RequestParam(value = "filter", required = false) filter: String?,
        @RequestParam(value = "currentPage", required = false) currentPage: String?,//currentPage
        @RequestParam(value = "pageSize", required = false) pageSize: String?,
        model: Model
    ): String {
        if (currentPage != null) {
            pageSize?.toInt()?.let { it ->
                PageRequest.of(currentPage.toInt(), it)
                    .let { page ->
                        val dateFrom =
                            Date.valueOf(LocalDate.parse(fromDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                        val dateTo = Date.valueOf(LocalDate.parse(toDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                        SecurityContextHolder.getContext().authentication
                            ?.let { auth ->
                                when {
                                    auth.authorities.stream()
                                        .anyMatch { authority -> authority.authority == "PVOC_APPLICATION_PROCESS" || authority.authority == "PVOC_APPLICATION_PROCESS_CHAIRMAN" } -> {
                                        when (filter) {
                                            "filter" -> {
                                                model.addAttribute("pvocFilter", PvocApplicationEntity())
                                                model.addAttribute(
                                                    "exceptionApplications",
                                                    iPvocApplicationRepo.findAllByCreatedOnBetween(
                                                        dateFrom,
                                                        dateTo,
                                                        page
                                                    )
                                                )
                                            }
                                            else -> {
                                                model.addAttribute("pvocFilter", PvocApplicationEntity())
                                                commonDaoServices.getLoggedInUser()
                                                    ?.let { loggedInUser ->
                                                        loggedInUser.id?.let {
                                                            getTasks(it).let { tasks ->
                                                                val listPage: PagedListHolder<*> =
                                                                    PagedListHolder<PvocApplicationEntity?>(
                                                                        tasks,
                                                                        MutableSortDefinition(false)
                                                                    )
                                                                listPage.pageSize =
                                                                    page.pageSize // number of items per page
                                                                listPage.page = page.pageNumber
                                                                KotlinLogging.logger { }
                                                                    .info { "tasks ==>" + listPage.pageList.count() }
                                                                model.addAttribute(
                                                                    "exceptionApplications",
                                                                    listPage.pageList
                                                                )
                                                            }
                                                        }

                                                    }
                                            }
                                        }
                                    }
                                    auth.authorities.stream()
                                        .anyMatch { authority -> authority.authority == "PVOC_APPLICATION_READ" } -> {
                                        commonDaoServices.getLoggedInUser().let { user ->
                                            user?.id?.let {
                                                iManufacturerRepository.findByIdAndStatus(it, 1)
                                                    .let { manufacturer ->
                                                        manufacturer?.name?.let { it ->
                                                            when (filter) {
                                                                "filter" -> {
                                                                    iPvocApplicationRepo.findAllByCreatedOnBetweenAndConpanyNameAndStatus(
                                                                        dateFrom,
                                                                        dateTo,
                                                                        it,
                                                                        1,
                                                                        page
                                                                    )
                                                                        .let { pvocApps ->
                                                                            model.addAttribute(
                                                                                "pvocFilter",
                                                                                PvocApplicationEntity()
                                                                            )
                                                                            model.addAttribute(
                                                                                "exceptionApplications",
                                                                                pvocApps
                                                                            )

                                                                        }
                                                                }
                                                                else -> {
                                                                    iPvocApplicationRepo.findAllByConpanyNameAndStatus(
                                                                        it,
                                                                        1,
                                                                        page
                                                                    )
                                                                        .let { pvocApps ->
                                                                            model.addAttribute(
                                                                                "pvocFilter",
                                                                                PvocApplicationEntity()
                                                                            )
                                                                            model.addAttribute(
                                                                                "exceptionApplications",
                                                                                pvocApps
                                                                            )
                                                                        }
                                                                }
                                                            }
                                                        }
                                                    }
                                            }
                                        }
                                    }
                                    else -> throw SupervisorNotFoundException("Only users with the following privilege PVOC Appliaction READ or PVOC APPLICATION PROCESS, can access this page")
                                }
                            }

                    }
            }
        }
        return "destination-inspection/pvoc/ExceptionApplicationsForms"
    }

    @GetMapping("application/unfinished")
    fun exceptionApplicationFormsUnifinishedIndex(
        @RequestParam(value = "fromDate", required = false) fromDate: String?,
        @RequestParam(value = "toDate", required = false) toDate: String?,
        @RequestParam(value = "filter", required = false) filter: String?,
        @RequestParam(value = "currentPage", required = false) currentPage: String?,//currentPage
        @RequestParam(value = "pageSize", required = false) pageSize: String?,
        model: Model
    ): String {
        SecurityContextHolder.getContext().authentication
            .let { auth ->
                when {
                    auth.authorities.stream()
                        .anyMatch { authority -> authority.authority == "PVOC_APPLICATION_READ" } -> {
                        commonDaoServices.getLoggedInUser()?.let { userId ->
                            iManufacturerRepository.findByUserId(userId)?.name?.let { companyName ->
                                iPvocApplicationRepo.findAllByConpanyNameAndFinished(
                                    companyName,
                                    0
                                )?.let { exemptions ->
                                    KotlinLogging.logger {  }.info { "Appps unfinished "+ exemptions.count() }
                                    model.addAttribute("exceptionApplications", exemptions)
                                }?: throw Exception("No unifinished applications currently")
                            } ?: throw Exception("Please login")
                        } ?: throw Exception("Please login")
                    }
                }
                return "destination-inspection/pvoc/UnfinishedExceptions"
            }
    }

    //@PostAuthorize("returnObject.companyPinNo == pvocApplicationEntity.companyPinNo")
    @PreAuthorize("hasAuthority('PVOC_APPLICATION_READ') or hasAuthority('PVOC_APPLICATION_PROCESS') or hasAnyAuthority('PVOC_APPLICATION_PROCESS_CHAIRMAN')")
    @GetMapping("pvoc-application-details/{id}")
    fun pvocApplicationDetails(@PathVariable("id") id: Long, model: Model): String {
        val products: MutableList<PvocApplicationProductsEntity> = ArrayList()
        val rawMaterisl: MutableList<PvocExceptionRawMaterialCategoryEntity> = ArrayList()
        val machineries: MutableList<PvocExceptionMainMachineryCategoryEntity> = ArrayList()
        val spares: MutableList<PvocExceptionIndustrialSparesCategoryEntity> = ArrayList()
        iPvocApplicationRepo.findByIdOrNull(id)
            ?.let { pvoc ->

                pvoc.id?.let { it1 ->
                    iPvocExceptionRawMaterialCategoryEntityRepo.findAllByExceptionId(it1).let { rawMaterials ->
                        rawMaterials.forEach { raw ->
                            rawMaterisl.add(raw)
                        }
                        model.addAttribute("rawMaterials", rawMaterisl)
                        model.addAttribute("rawMaterialsCount", rawMaterials.count())
                    }
                    iPvocExceptionIndustrialSparesCategoryEntityRepo.findAllByExceptionId(it1).let { sparesss ->
                        sparesss.forEach { spare ->
                            spares.add(spare)
                        }
                        model.addAttribute("spares", spares)
                        model.addAttribute("sparesssCount", spares.count())
                    }
                    iPvocExceptionMainMachineryCategoryEntityRepo.findAllByExceptionId(it1).let { machinerzs ->
                        machinerzs.forEach { machine ->
                            machineries.add(machine)
                        }
                        model.addAttribute("machineries", machineries)
                        model.addAttribute("macheineriesCount", machineries.count())
                    }

                }
                model.addAttribute("rawMaterial", PvocExceptionRawMaterialCategoryEntity())
                model.addAttribute("mainMachinery", MachinerysCheck())
                model.addAttribute("pvocApp", PvocApplicationEntity())
                model.addAttribute("sparess", SparesCheck().spares)
                model.addAttribute("pvoc", pvoc)
                model.addAttribute("remarkData", RemarksEntity())
                iPvocApplicationProductsRepo.findAllByPvocApplicationId(pvoc)?.forEach {
                    products.add(it)
                } ?: throw Exception("No Products Available")
                model.addAttribute("products", products)
//                    model.addAttribute("checkBoxChecked", checkBoxChecked)
                return "destination-inspection/pvoc/PvocApplicationFormDetailView"
            } ?: throw Exception("Product with $id id does not exist")
    }


    @PreAuthorize("hasAuthority('PVOC_APPLICATION_PROCESS')")
    @PostMapping("pvoc_process_exceptions_application/{id}/{remarksType}")
    fun exceptionsRemarks(
        @PathVariable("id") id: Long,
        @PathVariable("remarksType") remarksType: String,
        @ModelAttribute remarkData: RemarksEntity
    ): String {
        commonDaoServices.getLoggedInUser()
            ?.let { userDetails ->
                iPvocApplicationRepo.findByIdOrNull(id).let { doc ->
                    remarkData.firstName = userDetails.firstName
                    remarkData.lastName = userDetails.lastName
                    remarkData.userId = userDetails.id
                    remarkData.pvocExceptionApplicationId = doc?.id
                    remarkData.remarkStatus = 1
                    remarkData.createdBy = userDetails.firstName + " " + userDetails.lastName
                    remarkData.createdOn = Timestamp.from(Instant.now())
                    when (remarksType) {
                        "deffered" -> {
                            doc?.reviewStatus = pvocReviewStatus?.differedStatus
                            remarkData.remarksProcess = pvocReviewStatus?.differedStatus
                            iRemarksRepository.save(remarkData)
                            doc?.email?.let {
                                iUserRepository.findByEmail(it).let { user ->
                                    user?.id?.let { it1 -> pvocBpmn.pvocEaCheckApplicationComplete(id, it1, false) }
                                }
                            }
                        }
                        "excepted" -> {
                            doc?.reviewStatus = pvocReviewStatus?.varField1
                            remarkData.remarksProcess = pvocReviewStatus?.exceptionStatus
                            iRemarksRepository.save(remarkData)
                            userRolesService.getUserId("userRolesService")?.let {
                                pvocBpmn.pvocEaCheckApplicationComplete(id,
                                    it, true)
                            }
                        }
                        "rejected" -> {
                            doc?.reviewStatus = pvocReviewStatus?.varField1
                            remarkData.remarksProcess = pvocReviewStatus?.rejectedStatus
                            iRemarksRepository.save(remarkData)
                            userRolesService.getUserId("userRolesService")?.let {
                                pvocBpmn.pvocEaCheckApplicationComplete(id,
                                    it, false)
                            }
                            //pvocBpmn.pvocEaCheckApplicationComplete(id, 1007, false)
                        }
                    }
                    doc?.let { it -> iPvocApplicationRepo.save(it) }
                    return "redirect:/api/di/pvoc/pvoc-application-details/{id}"
                }
            }
            ?: throw NullValueNotAllowedException("Username cannot be empty")
    }

    //@PreAuthorize("hasAuthority('PVOC_APPLICATION_PROCESS_CHAIR')")
    @PostMapping("pvoc_process_exceptions_application-chairman/{id}/{remarksType}")
    fun exceptionsRemarksByChairman(
        @PathVariable("id") id: Long,
        @PathVariable("remarksType") remarksType: String,
        @ModelAttribute remarkData: RemarksEntity
    ): String {
        commonDaoServices.getLoggedInUser().let { userDetails ->
            iPvocApplicationRepo.findByIdOrNull(id).let { doc ->
                remarkData.firstName = userDetails?.firstName
                remarkData.lastName = userDetails?.lastName
                remarkData.userId = userDetails?.id
                remarkData.pvocExceptionApplicationId = doc?.id
                remarkData.remarkStatus = 1
                remarkData.createdBy = userDetails?.firstName + " " + userDetails?.lastName
                remarkData.createdOn = Timestamp.from(Instant.now())
                when (remarksType) {
                    "deffered" -> {
                        doc?.reviewStatus = pvocReviewStatus?.differedStatus
                        remarkData.remarksProcess = pvocReviewStatus?.differedStatus
                        doc?.finalApproval = 1
                        doc?.sn = pvocDaoServices.generateRandomNumbers("PVOC-EXEMPTION")
                        doc?.id?.let {
                            pvocBpmn.pvocAeDeferApplicationComplete(it)
                            userRolesService.getUserId("PERMIT_APPLICATION")?.let { it1 ->
                                pvocBpmn.pvocEaCheckApplicationComplete(it,
                                    it1, false)
                            }
                        }
                    }
                    "excepted" -> {
                        doc?.reviewStatus = pvocReviewStatus?.exceptionStatus
                        remarkData.remarksProcess = pvocReviewStatus?.exceptionStatus
                        doc?.id?.let {
                            pvocBpmn.pvocEaApproveApplicationComplete(it, true)
                            userRolesService.getUserId("PERMIT_APPLICATION")?.let { it1 ->
                                pvocBpmn.pvocEaCheckApplicationComplete(it,
                                    it1, true)
                            }
//                            pvocBpmn.pvocEaCheckApplicationComplete(it, 100, true)
                        }
                        doc?.finalApproval = 1
                        doc?.sn = pvocDaoServices.generateRandomNumbers("PVOC-EXEMPTION")
                    }
                    "rejected" -> {
                        doc?.reviewStatus = pvocReviewStatus?.rejectedStatus
                        remarkData.remarksProcess = pvocReviewStatus?.rejectedStatus
                        doc?.finalApproval = 1
                        doc?.sn = pvocDaoServices.generateRandomNumbers("PVOC-EXEMPTION")
                        doc?.id?.let {
                            pvocBpmn.pvocAeRejectApplicationComplete(it)
                            userRolesService.getUserId("PERMIT_APPLICATION")?.let { it1 ->
                                pvocBpmn.pvocEaCheckApplicationComplete(it,
                                    it1, false)
                            }
//                            pvocBpmn.pvocEaCheckApplicationComplete(it, 100, false)
                        }
                    }
                }
                iRemarksRepository.save(remarkData)
                doc?.let { it -> iPvocApplicationRepo.save(it) }
                return "redirect:/api/di/pvoc/pvoc-application-details/{id}"
            }
        }
    }


    @GetMapping("remarks-view/{id}")
    fun remarksView(model: Model, @PathVariable("id") id: Long): String {

        iRemarksRepository.findAllByPvocExceptionApplicationId(id)
            ?.let { remarks ->
                model.addAttribute("remarks", remarks)
                return "destination-inspection/pvoc/RemarksView"
            }
            ?: throw PvocRemarksNotFoundException("The Remarks with the following [id=$id], does not exist")

    }

    @PostMapping("exceptions-machinery-items-approve/{id}")
    fun exceptionsMachineryItemsApprove(
        @PathVariable("id") id: Long,
        @ModelAttribute("mainMachineryss") mainMachineryss: MachinerysCheck
    ): String {
        mainMachineryss.machineries?.let { machineries ->
            machineries.forEach { rawMat ->
                iPvocExceptionMainMachineryCategoryEntityRepo.findByIdOrNull(rawMat?.id)?.let { data ->
                    data.checkBoxChecked = rawMat?.checkBoxChecked ?: data.checkBoxChecked
                    data.remarks = rawMat?.remarks ?: data.remarks
                    data.reviewStatus = rawMat?.reviewStatus ?: data.reviewStatus
                    iPvocExceptionMainMachineryCategoryEntityRepo.save(data)
                } ?: throw Exception("The Raw Material with ${rawMat?.id} id does not exist")
            }
            return "redirect:/api/di/pvoc/pvoc-application-details/${id}"
        } ?: throw Exception("No items to update")
    }

    @PostMapping("exceptions-raw-materials-items-approve/{id}")
    fun exceptionsRawMaterialsItemsApprove(
        @PathVariable("id") id: Long,
        @ModelAttribute("rawMaterial") rawMaterial: RawMaterialsCheck
    ): String {
        rawMaterial.rawMaterials?.let { rawMats ->
            rawMats.forEach { rawMat ->
                iPvocExceptionRawMaterialCategoryEntityRepo.findByIdOrNull(rawMat?.id)?.let { data ->
                    data.checkBoxChecked = rawMat?.checkBoxChecked ?: data.checkBoxChecked
                    data.remarks = rawMat?.remarks ?: data.remarks
                    data.reviewStatus = rawMat?.reviewStatus ?: data.reviewStatus
                    iPvocExceptionRawMaterialCategoryEntityRepo.save(data)
                } ?: throw Exception("The Raw Material with ${rawMat?.id} id does not exist")
            }
            return "redirect:/api/di/pvoc/pvoc-application-details/${id}"
        } ?: throw Exception("The list is empty")
    }


    @PostMapping("exceptions-spares-items-approve/{id}")
    fun exceptionsSparesItemsApproveApprove(
        @PathVariable("id") id: Long,
        @ModelAttribute("sparess") sparess: SparesCheck
    ): String {
        sparess.spares?.forEach { rawMat ->
            iPvocExceptionIndustrialSparesCategoryEntityRepo.findByIdOrNull(rawMat?.id)?.let { data ->
                data.checkBoxChecked = rawMat?.checkBoxChecked ?: data.checkBoxChecked
                data.remarks = rawMat?.remarks ?: data.remarks
                data.reviewStatus = rawMat?.reviewStatus ?: data.reviewStatus
                iPvocExceptionIndustrialSparesCategoryEntityRepo.save(data)
            } ?: throw Exception("The Spare with ${rawMat?.id} id does not exist")
        }
        return "redirect:/api/di/pvoc/pvoc-application-details/${id}"
    }

    @GetMapping("single-exception-download/{id}")
    fun singleExceptionDownload(@PathVariable("id") id: Long, model: Model): String {
        iPvocApplicationRepo.findByIdOrNull(id)?.let { exception ->
            exception.id?.let {
                iPvocExceptionRawMaterialCategoryEntityRepo.findAllByExceptionIdAndReviewStatus(it, "Exempt")
                    .let { rawMaterials ->
                        model.addAttribute("rawMaterial", rawMaterials)
                        model.addAttribute("rawMaterialCount", rawMaterials.count())
                    }

                iPvocExceptionMainMachineryCategoryEntityRepo.findAllByExceptionIdAndReviewStatus(it, "Exempt")
                    .let { machineries ->
                        model.addAttribute("machineries", machineries)
                        model.addAttribute("machineryCount", machineries.count())
                    }

                iPvocExceptionIndustrialSparesCategoryEntityRepo.findAllByExceptionIdAndReviewStatus(it, "Exempt")
                    .let { spares ->
                        model.addAttribute("spares", spares)
                        model.addAttribute("sparesCount", spares.count())
                    }

            }
            model.addAttribute("pvoc", exception)
            return "destination-inspection/pvoc/ExemptionCertDownload"
        } ?: throw Exception("An error occured try again later")
    }

    @GetMapping("single-exception-generate/{id}")
    fun singleExceptionReportGenerate(@PathVariable("id") id: Long, model: Model): String {
        iPvocApplicationRepo.findByIdOrNull(id)?.let { exception ->
            exception.id?.let {
                iPvocExceptionRawMaterialCategoryEntityRepo.findAllByExceptionId(it).let { rawMaterials ->
                    model.addAttribute("rawMaterial", rawMaterials)
                    model.addAttribute("rawMaterialCount", rawMaterials.count())
                }

                iPvocApplicationProductsRepo.findAllByPvocApplicationId(exception).let { products ->
                    model.addAttribute("products", products)
                    model.addAttribute("productCount", products?.count())
                }

                iPvocExceptionMainMachineryCategoryEntityRepo.findAllByExceptionId(it).let { machineries ->
                    model.addAttribute("machineries", machineries)
                    model.addAttribute("machineryCount", machineries.count())
                }

                iPvocExceptionIndustrialSparesCategoryEntityRepo.findAllByExceptionId(it).let { spares ->
                    model.addAttribute("spares", spares)
                    model.addAttribute("sparesCount", spares.count())
                }

                iRemarksRepository.findAllByPvocExceptionApplicationId(it).let { remarks ->
                    model.addAttribute("remarks", remarks)
                    model.addAttribute("remarksCount", remarks?.count())
                }
            }
            model.addAttribute("pvoc", exception)
            return "destination-inspection/pvoc/ExceptionReport"
        } ?: throw Exception("An error occured try again later")

    }
}


//@JsonIgnoreProperties(ignoreUnknown = true)
//@Getter
//@Setter
//@ToString
//@Validated
//class CustomerRegisterRequestDTO {
//    @JsonProperty("first_name")
//    @NotNull
//    @NotEmpty(message = "First Name is mandatory")
//    @Size(min = 2, message = "First Name should have at least 2 characters")
//    private val firstName: String? = null
//
//    @JsonProperty("last_name")
//    @NotNull
//    @NotEmpty(message = "Last Name is mandatory")
//    @Size(min = 2, message = "Last Name should have at least 2 characters")
//    private val lastName: String? = null
//
//    @JsonProperty("phone")
//    @NotNull
//    @NotEmpty(message = "Phone number is mandatory")
//    @Size(min = 8, message = "Phone number should have at least 8 characters")
//    private val phone: String? = null
//
//    @JsonProperty("national_id")
//    @NotNull
//    @NotEmpty(message = "National Id is mandatory")
//    private val ntionalId: String? = null
//}
