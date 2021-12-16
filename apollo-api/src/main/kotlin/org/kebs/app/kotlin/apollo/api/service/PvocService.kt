package org.kebs.app.kotlin.apollo.api.service;

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.controllers.diControllers.pvoc.ExceptionPayload
import org.kebs.app.kotlin.apollo.api.handlers.forms.WaiverApplication
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.PvocBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.store.model.pvc.*
import org.kebs.app.kotlin.apollo.store.repo.*
import org.springframework.data.domain.PageRequest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.math.BigDecimal
import java.sql.Timestamp
import java.time.Instant
import java.util.*

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
        private val iPvocWaiversCategoriesRepo: IPvocWaiversCategoriesRepo,
        private val iPvocMasterListRepo: IPvocMasterListRepo,
        private val iwaiversApplicationRepo: IwaiversApplicationRepo,
        private val commonDaoServices: CommonDaoServices,
        private val standardLevyRepo: IStandardLevyPaymentsRepository,
        private val userRolesService: UserRolesService,
) {
    fun checkExemptionApplicable(): ApiResponseModel {
        val response = ApiResponseModel()
        commonDaoServices.getLoggedInUser().let { userDetails ->
            userDetails?.companyId?.let {
                this.standardLevyRepo.findFirstByManufacturerEntityOrderByCreatedOnDesc(it)
                        .let { levy ->
                            if (levy?.levyPayable == null || levy.levyPayable!! <= BigDecimal.ZERO) {
                                response.responseCode = ResponseCodes.SUCCESS_CODE
                                response.message = "Success"
                            } else {
                                response.data = levy.levyPayable
                                response.responseCode = ResponseCodes.FAILED_CODE
                                response.message = "not eligible for exemption application"
                            }
                        } ?: run {
                    response.responseCode = ResponseCodes.NOT_FOUND
                    response.message = "Record not available"
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
        val randomNumber = String.format("%04d", Integer.valueOf(r.nextInt(9001)))
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
            pvocExceptionApp.varField8 = manufacturer?.contactPersonEmail
            pvocExceptionApp.varField9 = manufacturer?.contactPersonPhone
            with(pvocExceptionApp) {
                createdBy = userDetails?.email
                createdOn = Timestamp.from(Instant.now())
                applicationDate = Date.from(Instant.now())
                finished = 1
                pvocExceptionApp = iPvocApplicationRepo.save(pvocExceptionApp)
                response.data = exemptionPayload
                response.message = "Success"
                response.responseCode = ResponseCodes.SUCCESS_CODE
            }

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
            pvocExceptionApp.id?.let {
                userDetails?.id?.let { it1 ->
                    pvocBpmn.startPvocApplicationExemptionsProcess(
                            it,
                            it1
                    )
                }
            }

            pvocExceptionApp.id?.let {
                userRolesService.getUserId("PVOC_APPLICATION_PROCESS")?.let { it1 ->
                    pvocBpmn.pvocEaSubmitApplicationComplete(it,
                            it1
                    )
                }
            }
            return response
        }

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

    fun retrieveExemptionById(id: Long): ApiResponseModel {
        val response = ApiResponseModel()
        val data = HashMap<String, Any?>()
        SecurityContextHolder.getContext().authentication.name.let { username ->
            iPvocApplicationRepo.findFirstByCreatedByAndId(username, id)?.let { exemption ->
                data.put("exemption", exemption)
                data.put("products", this.iPvocApplicationProductsRepo.findAllByPvocApplicationId_Id(exemption.id))
                data.put("rawMaterials", this.iPvocExceptionRawMaterialCategoryEntityRepo.findAllByExceptionId(exemption.id))
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
}
