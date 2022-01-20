package org.kebs.app.kotlin.apollo.api.service

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.controllers.diControllers.pvoc.ExceptionPayload
import org.kebs.app.kotlin.apollo.api.handlers.forms.WaiverApplication
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.response.*
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.PvocBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.store.model.pvc.*
import org.kebs.app.kotlin.apollo.store.repo.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.math.BigDecimal
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

enum class PvocExemptionStatus(val status: String) {
    NEW_APPLICATIONS("NEW"), PVOC_APPROVED("PVOC_APPROVE"), PVOC_REJECTED("PVOC_REJECTED"), DEFFERED("DEFFERED"), CERT_APPROVED("CERT_APPROVE"), CERT_REJECTED("CERT_REJECTED")
}

@Service
class PvocService(
        private val iPvocApplicationProductsRepo: IPvocApplicationProductsRepo,
        private val iPvocApplicationRepo: IPvocApplicationRepo,
        private val iPvocExceptionApplicationStatusEntityRepo: IPvocExceptionApplicationStatusEntityRepo,
        private val iPvocExceptionRawMaterialCategoryEntityRepo: IPvocExceptionRawMaterialCategoryEntityRepo,
        private val iPvocExceptionMainMachineryCategoryEntityRepo: IPvocExceptionMainMachineryCategoryEntityRepo,
        private val iPvocExceptionIndustrialSparesCategoryEntityRepo: IPvocExceptionIndustrialSparesCategoryEntityRepo,
        private val iPvocWaiversApplicationDocumentRepo: IPvocWaiversApplicationDocumentRepo,
        private val pvocBpmn: PvocBpmn,
        private val ipvocExemptionCertificateRepository: IPvocExceptionCertificateRepository,
        private val iPvocWaiversCategoriesRepo: IPvocWaiversCategoriesRepo,
        private val iPvocMasterListRepo: IPvocMasterListRepo,
        private val iwaiversApplicationRepo: IwaiversApplicationRepo,
        private val commonDaoServices: CommonDaoServices,
        private val standardLevyRepo: IStandardLevyPaymentsRepository,
        private val userRolesService: UserRolesService
) {
    fun checkExemptionApplicable(): ApiResponseModel {
        val response = ApiResponseModel()
        commonDaoServices.getLoggedInUser().let { userDetails ->
            userDetails?.companyId?.let {
                this.standardLevyRepo.findFirstByManufacturerEntityOrderByCreatedOnDesc(it)?.let { levy ->
                    if (levy.levyPayable!! <= BigDecimal.ZERO) {
                        response.responseCode = ResponseCodes.SUCCESS_CODE
                        response.message = "Success"
                    } else {
                        response.data = levy.levyPayable
                        response.responseCode = ResponseCodes.FAILED_CODE
                        response.message = "not eligible for exemption application"
                    }
                    response
                } ?: run {
                    response.responseCode = ResponseCodes.NOT_FOUND
                    response.message = "Record not available"
                    response
                }

            }
        }
        return response
    }

    var statuses = iPvocExceptionApplicationStatusEntityRepo.findFirstByMapId(1)
    fun waiverApplicationHistory(page: Int, size: Int): ApiResponseModel {
        val response = ApiResponseModel()
        SecurityContextHolder.getContext().authentication.name.let { username ->
            val requests = this.iwaiversApplicationRepo.findAllByCreatedBy(username, PageRequest.of(page, size))
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Success"
            response.data = requests?.toList()
            response.totalPages = requests?.totalPages
        }
        return response
    }


    fun kimsWaiverApplications(waiverStatus: String, keywords: String?, page: PageRequest): ApiResponseModel {
        val response = ApiResponseModel()
        var requests: Page<PvocWaiversApplicationEntity>?
        if (StringUtils.hasLength(keywords)) {
            requests = this.iwaiversApplicationRepo.findAllBySerialNoContains(keywords!!, page)
        } else {
            requests = when (waiverStatus) {
//                "ASSIGNED" -> this.iwaiversApplicationRepo.
                "NEW" -> this.iwaiversApplicationRepo.findAllByReviewStatusOrderByCreatedOnDesc("NEW", page)
                "DEFFERED" -> this.iwaiversApplicationRepo.findAllByReviewStatusOrderByCreatedOnDesc("DEFFERED", page)
                "REVIEW_REJECTED" -> this.iwaiversApplicationRepo.findAllByReviewStatusOrderByCreatedOnDesc("REVIEW_REJECTED", page)
                "REVIEW_APPROVED" -> this.iwaiversApplicationRepo.findAllByReviewStatusOrderByCreatedOnDesc("REVIEW_APPROVED", page)
                "NSC_APPROVED" -> this.iwaiversApplicationRepo.findAllByNscApprovalStatusOrderByCreatedOnDesc("APPROVED", page)
                "NSC_REJECTED" -> this.iwaiversApplicationRepo.findAllByNscApprovalStatusOrderByCreatedOnDesc("REJECTED", page)
                "CS_APPROVED" -> this.iwaiversApplicationRepo.findAllByCsApprovalStatusOrderByCreatedOnDesc("APPROVED", page)
                "CS_REJECTED" -> this.iwaiversApplicationRepo.findAllByCsApprovalStatusOrderByCreatedOnDesc("REJECTED", page)
                else -> null
            }
        }
        if (requests != null) {
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Success"
            response.data = PvocWaiverDao.fromList(requests.toList())
            response.totalPages = requests.totalPages
            response.totalCount = requests.totalElements
        } else {
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Invalid waiver application status"
        }
        return response
    }

    fun updateWaiverReviewStatus(requestId: Long, remarks: String, reviewOfficer: String, reviewStatus: String) {
        val optional = this.iwaiversApplicationRepo.findById(requestId)
        if (optional.isPresent) {
            val request = optional.get()
            request.reviewStatus = reviewStatus
            request.varField2 = remarks
            request.modifiedBy = reviewOfficer
            request.modifiedOn = Timestamp.from(Instant.now())
            this.iwaiversApplicationRepo.save(request)
        }
    }

    fun updateWaiverNscStatus(requestId: Long, remarks: String, reviewOfficer: String, approvalStatus: String) {
        val optional = this.iwaiversApplicationRepo.findById(requestId)
        if (optional.isPresent) {
            val request = optional.get()
            when (approvalStatus) {
                "APPROVED" -> {
                    request.nscApprovalStatus = "NSC_APPROVED"
                    request.rejectionStatus = 0
                }
                "REJECTED" -> {
                    request.rejectionStatus = 1
                    request.nscApprovalStatus = "NSC_REJECTED"
                }
            }
            request.varField2 = remarks
            request.modifiedBy = reviewOfficer
            request.modifiedOn = Timestamp.from(Instant.now())
            this.iwaiversApplicationRepo.save(request)
        }
    }

    fun updateWaiverCsStatus(requestId: Long, remarks: String, reviewOfficer: String, approvalStatus: String) {
        val optional = this.iwaiversApplicationRepo.findById(requestId)
        if (optional.isPresent) {
            val request = optional.get()
            request.csResponceMessage = remarks
            when (approvalStatus) {
                "APPROVED" -> {
                    request.csApprovalStatus = approvalStatus
                }
                "REJECTED" -> {
                    request.rejectionStatus = 1
                    request.nscApprovalStatus = approvalStatus
                }
            }
            request.varField2 = remarks
            request.modifiedBy = reviewOfficer
            request.modifiedOn = Timestamp.from(Instant.now())
            this.iwaiversApplicationRepo.save(request)
        }
    }

    fun sendWaiverRequestToCs(requestId: Long, reviewOfficer: String) {

    }

    fun sendWaiverApprovalLetter(requestId: Long, approvalStatus: String) {

    }

    fun exemptionApplicationHistory(page: Int, size: Int): ApiResponseModel {
        val response = ApiResponseModel()
        commonDaoServices.getLoggedInUser().let { userDetails ->
            userDetails?.email?.let { email ->
                val requests = this.iPvocApplicationRepo.findAllByCreatedByOrderByCreatedOnDesc(email, PageRequest.of(page, size))
                response.data = requests?.toList()
                response.message = "Success"
                response.totalPages = requests?.totalPages
                response.responseCode = ResponseCodes.SUCCESS_CODE
            } ?: run {
                response.message = "Could not get username"
                response.responseCode = ResponseCodes.FAILED_CODE
            }
        } ?: run {
            response.message = "Could not get authentication"
            response.responseCode = ResponseCodes.FAILED_CODE
        }
        return response
    }

    fun applyOrRenewWaiver(waiver: WaiverApplication, documents: List<MultipartFile>?): ApiResponseModel {
        val response = ApiResponseModel()
        val r = Random()
        val randomNumber = String.format("%s%04d", commonDaoServices.convertDateToString(LocalDateTime.now(), "yyyyMMdd"), Integer.valueOf(r.nextInt(9001)))
        val waiverApp = PvocWaiversApplicationEntity()
        waiverApp.category = waiver.category
        waiverApp.address = waiver.postalAddress
        waiverApp.applicantName = waiver.applicantName
        waiverApp.approvalStatus = 0
        waiverApp.emailAddress = waiver.emailAddress
        waiverApp.phoneNumber = waiver.telephoneNumber
        waiverApp.justification = waiver.justification
        waiverApp.kraPin = waiver.kraPin
        waiverApp.varField1 = waiver.contactPersonName
        waiverApp.varField2 = waiver.contactPersonPhone
        waiverApp.varField3 = waiver.contactPersonEmail
        SecurityContextHolder.getContext().authentication.name.let { username ->
            waiverApp.createdBy = username
            waiverApp.createdOn = Timestamp.from(Instant.now())
            waiverApp.status = 1
            waiverApp.serialNo = randomNumber
            iwaiversApplicationRepo.save(waiverApp)
                    .let { w ->
                        documents?.forEach { file ->
                            val waiverDocs = PvocWaiversApplicationDocumentsEntity()
                            waiverDocs.name = file.originalFilename
                            waiverDocs.fileType = file.contentType
                            waiverDocs.documentType = file.bytes
                            waiverDocs.status = 1
                            waiverDocs.createdBy = w.createdBy
                            waiverDocs.createdOn = Timestamp.from(Instant.now())
                            waiverDocs.waiverId = w.id
                            iPvocWaiversApplicationDocumentRepo.save(waiverDocs)
                        }
                        waiver.products?.forEach({ product ->
                            val waiverProduct = PvocMasterListEntity()
                            waiverProduct.hsCode = product.hsCode
                            waiverProduct.waiversApplicationId = waiverApp.id
                            waiverProduct.unit = product.productUnit.toString()
                            waiverProduct.quantity = product.quantity.toString()
                            waiverProduct.totalAmount = (product.productUnit * product.quantity).toString()
                            waiverProduct.origin = product.countryOfOrigin
                            waiverProduct.productDescription = product.productDescription
                            waiverProduct.serialNo = waiverProduct.serialNo
                            waiverProduct.currency = product.currency
                            waiverProduct.createdBy = username
                            waiverProduct.createdOn = waiverApp.createdOn
                            waiverProduct.status = 1
                            this.iPvocMasterListRepo.save(waiverProduct)
                        })
                        response.data = waiverApp
                        response.responseCode = ResponseCodes.SUCCESS_CODE
                        response.message = "Waiver application have been received"
                    }
        }
        return response
    }


    fun updateReviewRequest(requestId: Long, remarks: String, reviewOfficer: String, reviewStatus: String) {
        val optional = this.iPvocApplicationRepo.findById(requestId)
        if (optional.isPresent) {
            val request = optional.get()
            request.reviewStatus = reviewStatus
            request.varField2 = remarks
            request.modifiedBy = reviewOfficer
            request.modifiedOn = Timestamp.from(Instant.now())
            this.iPvocApplicationRepo.save(request)
        }
    }

    fun updateCommitteeStatus(requestId: Long, remarks: String, reviewOfficer: String, approvalStatus: String) {
        val optional = this.iPvocApplicationRepo.findById(requestId)
        if (optional.isPresent) {
            val request = optional.get()
            when (approvalStatus) {
                "APPROVED" -> {
                    request.finalApproval = approvalStatus
                    request.finished = 1
                }
                "REJECTED" -> {
                    request.finalApproval = approvalStatus
                    request.finished = 1
                }
            }
            request.varField2 = remarks
            request.modifiedBy = reviewOfficer
            request.modifiedOn = Timestamp.from(Instant.now())
            this.iPvocApplicationRepo.save(request)
        }
    }

    fun approveDeferRejectExemption(requestId: Long, taskStatus: String, taskId: String, remarks: String): ApiResponseModel {
        var response = ApiResponseModel()
        try {
            val data = mutableMapOf<String, Any>()
            data["remarks"] = remarks
            data["approve"] = taskStatus
            this.pvocBpmn.pvocCompleteTask(taskId, data)
            response.message = "Success"
            response.responseCode = ResponseCodes.SUCCESS_CODE
        } catch (ex: Exception) {
            response.message = "Task approval failed"
            response.responseCode = ResponseCodes.FAILED_CODE
        }
        return response
    }

    fun generateExemptionCertificate(requestId: Long, reviewOfficer: String) {
        val optional = this.iPvocApplicationRepo.findById(requestId)
        if (optional.isPresent) {
            val request = optional.get()
            val exemption = PvocExceptionCertificate()
            exemption.applicationId = requestId
            exemption.createdBy = reviewOfficer
            exemption.modifiedBy = reviewOfficer
            exemption.certificateRevoked = false
            exemption.certificateVersion = 1
            exemption.certificateNumber = "COE".format("%0x05%0x03", requestId, exemption.certificateVersion)
            val issueDate = LocalDateTime.now()
            exemption.createdOn = Timestamp.valueOf(issueDate)
            exemption.expiresOn = Timestamp.valueOf(issueDate.plusYears(1))
            request.modifiedOn = Timestamp.from(Instant.now())
            val g = this.ipvocExemptionCertificateRepository.save(exemption)
            request.varField2 = g.certificateNumber
            this.iPvocApplicationRepo.save(request)
        }
    }

    fun completeExemptionApplication(requestId: Long) {
        val optional = this.iPvocApplicationRepo.findById(requestId)
        if (optional.isPresent) {
            val request = optional.get()
            request.finished = 1
            when (request.finalApproval) {
                "APPROVED" -> {
                    // Send excemption certificate email
                }
                "REJECTED" -> {
                    // Send rejection email
                }
                else -> {

                }
            }
            iPvocApplicationRepo.save(request)
        }
    }


    fun listOrSearchApplicationExceptions(status: String, keywords: String?, page: PageRequest): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val pg = when (status) {
                "NEW" -> this.iPvocApplicationRepo.findAllByReviewStatus(PvocExemptionStatus.NEW_APPLICATIONS.status, page)
                "APPROVED" -> this.iPvocApplicationRepo.findAllByReviewStatus(PvocExemptionStatus.PVOC_APPROVED.status, page)
                "REJECTED" -> this.iPvocApplicationRepo.findAllByReviewStatus(PvocExemptionStatus.PVOC_REJECTED.status, page)
                "DEFFERED" -> this.iPvocApplicationRepo.findAllByReviewStatus(PvocExemptionStatus.DEFFERED.status, page)
                "CERT_REJECTED" -> this.iPvocApplicationRepo.findAllByReviewStatus(PvocExemptionStatus.CERT_REJECTED.status, page)
                "CERT_APPROVED" -> this.iPvocApplicationRepo.findAllByReviewStatus(PvocExemptionStatus.CERT_APPROVED.status, page)
                else -> {
                    response.responseCode = ResponseCodes.FAILED_CODE
                    response.message = "Invalid request status: $status"
                    null
                }
            }
            if (pg != null) {
                response.data = PvocApplicationDto.fromList(pg.toList())
                response.message = "Exemption application"
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.totalPages = pg.totalPages
                response.totalCount = pg.totalElements
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to get exemption requests", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Request not found"
        }
        return response
    }

    /**
     * Save application exemption.
     */
    fun saveApplicationExemption(exemptionPayload: ExceptionPayload, documents: List<MultipartFile>?): ApiResponseModel {
        val response = ApiResponseModel()
        val manufacturer = exemptionPayload.manufacturer
        val products = exemptionPayload.products
        val rawMaterials = exemptionPayload.rawMaterials
        val spares = exemptionPayload.spares
        val mainMachinaries = exemptionPayload.mainMachinary
        commonDaoServices.getLoggedInUser().let { userDetails ->
            var pvocExceptionApp = PvocApplicationEntity()
            pvocExceptionApp.companyPinNo = manufacturer?.companyPinNo
            pvocExceptionApp.email = manufacturer?.email
            pvocExceptionApp.status = 1
            pvocExceptionApp.reviewStatus = statuses?.initialStatus
            pvocExceptionApp.telephoneNo = manufacturer?.telephoneNo
            pvocExceptionApp.conpanyName = manufacturer?.companyName
            pvocExceptionApp.postalAadress = manufacturer?.postalAddress
            pvocExceptionApp.physicalLocation = manufacturer?.physicalLocation
            pvocExceptionApp.contactPersorn = manufacturer?.contactPersonName
            pvocExceptionApp.contactEmail = manufacturer?.contactPersonEmail
            pvocExceptionApp.contactName = manufacturer?.contactPersonPhone
            with(pvocExceptionApp) {
                createdBy = userDetails?.email
                createdOn = Timestamp.from(Instant.now())
                finalApproval = "NEW"
                applicationDate = Date.from(Instant.now())
                finished = 0
            }
            pvocExceptionApp = iPvocApplicationRepo.save(pvocExceptionApp)
            response.data = exemptionPayload
            response.message = "Success"
            response.responseCode = ResponseCodes.SUCCESS_CODE

            products?.forEach { product ->
                val pvocApplicationProductsEntity = PvocApplicationProductsEntity()
                with(pvocApplicationProductsEntity) {
                    productName = product.productName
                    brand = product.brandName
                    pvocApplicationId = pvocExceptionApp
                    expirelyDate = product.expriryDate.toString()
                    kebsStandardizationMarkPermit = product.permitNumber
                    createdBy = pvocExceptionApp.createdBy
                    createdOn = pvocExceptionApp.createdOn
                    iPvocApplicationProductsRepo.save(pvocApplicationProductsEntity)
                    KotlinLogging.logger { }.info { "Main App save OK" }
                }
            }

            rawMaterials?.forEach { rawMat ->
                val pvocRawMaterialCategory = PvocExceptionRawMaterialCategoryEntity()
                with(pvocRawMaterialCategory) {
                    hsCode = rawMat.hsCode
                    rawMaterialDescription = rawMat.rawMaterialDescription
                    endProduct = rawMat.endProduct
                    countryOfOrgin = rawMat.countryOfOrigin
                    exceptionId = pvocExceptionApp.id
                    createdBy = pvocExceptionApp.createdBy
                    createdOn = pvocExceptionApp.createdOn
                    iPvocExceptionRawMaterialCategoryEntityRepo.save(pvocRawMaterialCategory)
                    KotlinLogging.logger { }.info { "Raw save OK" }
                }
            }

            mainMachinaries?.forEach { machinary ->
                val machine = PvocExceptionMainMachineryCategoryEntity()
                with(machine) {
                    hsCode = machinary.hsCode
                    machineDescription = machinary.machineDescription
                    countryOfOrigin = machinary.countryOfOrigin
                    makeModel = machinary.makeModel
                    exceptionId = pvocExceptionApp.id
                    createdBy = pvocExceptionApp.createdBy
                    createdOn = pvocExceptionApp.createdOn
                    iPvocExceptionMainMachineryCategoryEntityRepo.save(machine)
                    KotlinLogging.logger { }.info { "Machine save OK" }
                }
            }

            spares?.forEach { spare ->
                val spareEntity = PvocExceptionIndustrialSparesCategoryEntity()
                with(spareEntity) {
                    hsCode = spare.hsCode
                    machineToFit = spare.machineToFit
                    countryOfOrigin = spare.countryOfOrigin
                    industrialSpares = spare.industrialSpares
                    exceptionId = pvocExceptionApp.id
                    createdBy = pvocExceptionApp.createdBy
                    createdOn = pvocExceptionApp.createdOn
                    iPvocExceptionIndustrialSparesCategoryEntityRepo.save(spareEntity)
                    KotlinLogging.logger { }.info { "Spare save OK" }
                }
            }
            // Save documents
//            documents?.forEach(file->
//
//            )
            // PVOC application BPM process
            pvocBpmn.startPvocApplicationExemptionsProcess(pvocExceptionApp)
            // Save the update
            this.iPvocApplicationRepo.save(pvocExceptionApp)
            return response
        }
    }

    fun removeExemptionItem(requestId: Long, itemId: Long, itemType: String): ApiResponseModel {
        val response = ApiResponseModel()
        when (itemType) {
            "SPARE" -> {
                val optional = iPvocExceptionIndustrialSparesCategoryEntityRepo.findAllByIdAndExceptionId(itemId, requestId)
                if (optional.isPresent) {
                    val spare = optional.get()
                    iPvocExceptionIndustrialSparesCategoryEntityRepo.delete(spare)
                    response.data = spare
                    response.message = "Deleted"
                    response.responseCode = ResponseCodes.SUCCESS_CODE
                } else {
                    response.message = "Record not found"
                    response.responseCode = ResponseCodes.NOT_FOUND
                }
            }
            "MACHINERY" -> {
                val optional = iPvocExceptionMainMachineryCategoryEntityRepo.findAllByIdAndExceptionId(itemId, requestId)
                if (optional.isPresent) {
                    val machine = optional.get()
                    iPvocExceptionMainMachineryCategoryEntityRepo.delete(machine)
                    response.data = machine
                    response.message = "Deleted"
                    response.responseCode = ResponseCodes.SUCCESS_CODE
                } else {
                    response.message = "Record not found"
                    response.responseCode = ResponseCodes.NOT_FOUND
                }
            }
            "RAW_MATERIAL" -> {
                val optional = iPvocExceptionRawMaterialCategoryEntityRepo.findAllByIdAndExceptionId(itemId, requestId)
                if (optional.isPresent) {
                    val rawMaterial = optional.get()
                    iPvocExceptionRawMaterialCategoryEntityRepo.delete(rawMaterial)
                    response.data = rawMaterial
                    response.message = "Deleted"
                    response.responseCode = ResponseCodes.SUCCESS_CODE
                } else {
                    response.message = "Record not found"
                    response.responseCode = ResponseCodes.NOT_FOUND
                }
            }
            "PRODUCT" -> {
                val optional = iPvocApplicationProductsRepo.findAllByIdAndPvocApplicationId_Id(itemId, requestId)
                if (optional.isPresent) {
                    val product = optional.get()
                    iPvocApplicationProductsRepo.delete(product)
                    response.data = product
                    response.message = "Deleted"
                    response.responseCode = ResponseCodes.SUCCESS_CODE
                } else {
                    response.message = "Record not found"
                    response.responseCode = ResponseCodes.NOT_FOUND
                }
            }
            else -> {
                response.message = "Invalid product type"
                response.responseCode = ResponseCodes.NOT_FOUND
            }
        }
        return response
    }

    fun retrieveWaiverById(id: Long): ApiResponseModel {
        val response = ApiResponseModel()
        val data = HashMap<String, Any?>()
        SecurityContextHolder.getContext().authentication.name.let { username ->
            iwaiversApplicationRepo.findFirstByCreatedByAndId(username, id)?.let { waiver ->
                data.put("masterLists", this.iPvocMasterListRepo.findAllByWaiversApplicationId(waiver.id))
                data.put("waiverDetails", waiver)
                data.put("remarkData", PvocWaiversRemarksEntity())
                response.data = data
                response.message = "Success"
                response.responseCode = ResponseCodes.SUCCESS_CODE
            } ?: run {
                response.message = "Waiver with $id does not exist"
                response.responseCode = ResponseCodes.NOT_FOUND
            }
        }
        return response
    }

    fun listWaiverCategories(): ApiResponseModel {
        val response = ApiResponseModel()
        response.data = this.iPvocWaiversCategoriesRepo.findAllByStatus(1)
        response.responseCode = ResponseCodes.SUCCESS_CODE
        response.message = "Success"
        return response
    }

    fun retrieveMyExemptionApplicationById(id: Long): ApiResponseModel {
        val response = ApiResponseModel()
        val data = HashMap<String, Any?>()
        SecurityContextHolder.getContext().authentication.name.let { username ->
            iPvocApplicationRepo.findFirstByCreatedByAndId(username, id)?.let { exemption ->
                data.put("exemption", PvocApplicationDto.fromEntity(exemption))
                data.put("products", this.iPvocApplicationProductsRepo.findAllByPvocApplicationId_Id(exemption.id))
                data.put("rawMaterials", PvocExceptionRawMaterialDao.fromList(this.iPvocExceptionRawMaterialCategoryEntityRepo.findAllByExceptionId(exemption.id)))
                data.put("machinery", this.iPvocExceptionMainMachineryCategoryEntityRepo.findAllByExceptionId(exemption.id))
                data.put("spares", this.iPvocExceptionIndustrialSparesCategoryEntityRepo.findAllByExceptionId(exemption.id))
                response.data = data
                response.message = "Success"
                response.responseCode = ResponseCodes.SUCCESS_CODE
            } ?: run {
                response.message = "Exemption with $id does not exist"
                response.responseCode = ResponseCodes.NOT_FOUND
            }
        }
        return response
    }

    fun retrieveExemptionApplicationDetails(id: Long): ApiResponseModel {
        val response = ApiResponseModel()
        val data = HashMap<String, Any?>()
        val exemptionOptional = iPvocApplicationRepo.findById(id)
        if (exemptionOptional.isPresent) {
            val exemption = exemptionOptional.get()
            data.put("exemption", PvocApplicationDto.fromEntity(exemption))
            // Exemption details
            data.put("products", PvocApplicationProductDao.fromList(this.iPvocApplicationProductsRepo.findAllByPvocApplicationId_Id(exemption.id)))
            data.put("rawMaterials", PvocExceptionRawMaterialDao.fromList(this.iPvocExceptionRawMaterialCategoryEntityRepo.findAllByExceptionId(exemption.id)))
            data.put("machinery", PvocExceptionMainMachineryDao.fromList(this.iPvocExceptionMainMachineryCategoryEntityRepo.findAllByExceptionId(exemption.id)))
            data.put("spares", PvocExceptionIndustrialSparesDao.fromList(this.iPvocExceptionIndustrialSparesCategoryEntityRepo.findAllByExceptionId(exemption.id)))
            // Add Tasks based on roles
            try {
                val auth = commonDaoServices.loggedInUserAuthentication()
                when {
                    auth.authorities.stream().anyMatch { authority -> authority.authority == "PVOC_SECTION_OFFICER" } -> {
                        data.put("is_section_officer", true)
                        data.put("tasks", this.pvocBpmn.getExemptionTasks("SECTION_OFFICER", exemption.id!!))
                    }
                    auth.authorities.stream().anyMatch { authority -> authority.authority == "PVOC_EXEMPTION_CHAIRMAN" } -> {
                        data.put("is_section_officer", false)
                        data.put("tasks", this.pvocBpmn.getExemptionTasks("EXEMPTION_CHAIRMAN", exemption.id!!))
                    }
                }
            } catch (ex: Exception) {
                KotlinLogging.logger { }.error("Failed to add data", ex)
            }
            // Data
            response.data = data
            response.message = "Success"
            response.responseCode = ResponseCodes.SUCCESS_CODE
        } else {
            response.message = "Record not found"
            response.responseCode = ResponseCodes.NOT_FOUND
        }
        return response
    }
}
