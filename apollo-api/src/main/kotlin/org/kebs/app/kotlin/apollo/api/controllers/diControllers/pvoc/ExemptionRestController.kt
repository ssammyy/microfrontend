package org.kebs.app.kotlin.apollo.api.controllers.diControllers.pvoc

import com.google.gson.Gson
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.PvocBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.service.FileExcelProcessService
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.repo.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.sql.Timestamp
import java.time.Instant

@RestController
@RequestMapping("api/di/pvoc/rest/")
class ExemptionRestController(
    private val iPvocApplicationProductsRepo: IPvocApplicationProductsRepo,
    private val iPvocApplicationRepo: IPvocApplicationRepo,
    private val iManufacturerRepository: IManufacturerRepository,
    private val iPermitRepository: IPermitRepository,
    private val iProductsRepository: IProductsRepository,
    private val pvocBpmn: PvocBpmn,
    private val commonDaoServices: CommonDaoServices,
    private val iPvocExceptionIndustrialSparesCategoryEntityRepo: IPvocExceptionIndustrialSparesCategoryEntityRepo,
    private val iPvocExceptionMainMachineryCategoryEntityRepo: IPvocExceptionMainMachineryCategoryEntityRepo,
    private val iPvocExceptionRawMaterialCategoryEntityRepo: IPvocExceptionRawMaterialCategoryEntityRepo
) {

    @Autowired
    var fileExcelProcessService: FileExcelProcessService? = null

    @GetMapping("permits-products")
    @ResponseBody
    fun permitsProducts(): String? {
        val productsArrayList: ArrayList<PermitProduct> = ArrayList()
        val gson = Gson()
         commonDaoServices.getLoggedInUser()?.let { user ->
            iPermitRepository.findByStatusAndUserId(1, user).let { permits ->
                permits.forEach { permit ->
                    permit.product?.let { p ->
                        iProductsRepository.findByIdOrNull(p)
                            ?.let { product ->
                                KotlinLogging.logger {  }.info { permit.expiryDate }
                                val dummy = PermitProduct()
                                dummy.expriryDate = permit.expiryDate.toString()
                                dummy.brandId = permit.brandId?.id
                                dummy.brandName = permit.brandId?.brandName
                                dummy.permitId = permit.id
                                dummy.permitNumber = permit.permitNumber
                                dummy.productName = product.name
                                dummy.id = product.id
                                productsArrayList.add(dummy)
                            }
                    }

                }
            }
        }
        return gson.toJson(productsArrayList)
    }

    @GetMapping("manufacturer-details")
    fun manufacturerDetails(): ManufacturersEntity? {
        commonDaoServices.getLoggedInUser().let { user ->
            return iManufacturerRepository.findByUserId(user)
        }
    }


    @PostMapping("application-exception3", consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE] )
    fun saveApplicationViaSys(@RequestBody exemptionPayload: ExceptionPayload): UploadedFileResponse {
        val manufacturer = exemptionPayload.manufacturer
        val products = exemptionPayload.products
        val rawMaterials = exemptionPayload.rawMaterials
        val spares = exemptionPayload.spares
        val mainMachinaries = exemptionPayload.mainMachinary
        manufacturer?.companyPinNo?.let {
            manufacturer.companyName?.let { it1 ->
                iPvocApplicationRepo.findFirstByConpanyNameAndCompanyPinNo(
                    it1,
                    it
                )?.let { company ->
                    products?.forEach { product ->
                        val pvocApplicationProductsEntity = PvocApplicationProductsEntity()
                        KotlinLogging.logger { }.info { company.id }
                        with(pvocApplicationProductsEntity) {
                            productName = product.productName
                            brand = product.brandName
                            pvocApplicationId = company
                            expirelyDate = product.expriryDate.toString()
                            kebsStandardizationMarkPermit = product.permitNumber
                            createdBy = company.createdBy
                            createdOn = company.createdOn
                            iPvocApplicationProductsRepo.save(pvocApplicationProductsEntity)
                        }
                    }

                    rawMaterials?.forEach { rawMat ->
                        val pvocRawMaterialCategory = PvocExceptionRawMaterialCategoryEntity()
                        with(pvocRawMaterialCategory) {
                            hsCode = rawMat.hsCode
                            rawMaterialDescription = rawMat.rawMaterialDescription
                            dutyRate = rawMat.dutyRate?.toLong()
                            endProduct = rawMat.endProduct
                            exceptionId = company.id
                            countryOfOrgin = rawMat.countryOfOrigin
                            createdBy = company.createdBy
                            createdOn = company.createdOn
                            iPvocExceptionRawMaterialCategoryEntityRepo.save(pvocRawMaterialCategory)
                        }
                    }

                    mainMachinaries?.forEach { machinary ->
                        val machine = PvocExceptionMainMachineryCategoryEntity()
                        with(machine) {
                            hsCode = machinary.hsCode
                            machineDescription = machinary.machineDescription
                            countryOfOrigin = machinary.countryOfOrigin
                            makeModel = machinary.makeModel
                            exceptionId = company.id
                            createdBy = company.createdBy
                            createdOn = company.createdOn
                            iPvocExceptionMainMachineryCategoryEntityRepo.save(machine)
                        }
                    }

                    spares?.forEach { spare ->
                        val spareEntity = PvocExceptionIndustrialSparesCategoryEntity()
                        with(spareEntity) {
                            hsCode = spare.hsCode
                            machineToFit = spare.machineToFit
                            countryOfOrigin = spare.countryOfOrigin
                            industrialSpares = spare.industrialSpares
                            exceptionId = company.id
                            createdBy = company.createdBy
                            createdOn = company.createdOn
                            iPvocExceptionIndustrialSparesCategoryEntityRepo.save(spareEntity)
                        }
                    }
                    company.id?.let {
                        commonDaoServices.getLoggedInUser()?.id?.let { it1 ->
                            pvocBpmn.startPvocApplicationExemptionsProcess(
                                it,
                                it1
                            )
                            KotlinLogging.logger {  }.info { "Object id $it" }
                            KotlinLogging.logger {  }.info { "Object id $it1" }
                        }
                    }
                    company.id?.let { pvocBpmn.pvocEaSubmitApplicationComplete(it, 502) }
                    return UploadedFileResponse()
                }
            }
        } ?: commonDaoServices.getLoggedInUser().let { userDetails ->
            val pvocExceptionApp = PvocApplicationEntity()
            pvocExceptionApp.companyPinNo = manufacturer?.companyPinNo
            pvocExceptionApp.contactPersorn = manufacturer?.contactPersorn
            pvocExceptionApp.email = manufacturer?.email
            pvocExceptionApp.status = 1
            pvocExceptionApp.telephoneNo = manufacturer?.telephoneNo
            pvocExceptionApp.conpanyName = manufacturer?.companyName
            pvocExceptionApp.postalAadress = manufacturer?.postalAadress
            pvocExceptionApp.physicalLocation = manufacturer?.physicalLocation
            with(pvocExceptionApp) {
                createdBy = userDetails?.firstName + " " + userDetails?.lastName
                createdOn = Timestamp.from(Instant.now())
                applicationDate = java.util.Date.from(Instant.now())
                finished = 1
                iPvocApplicationRepo.save(pvocExceptionApp)
            }

            manufacturer?.companyName?.let {
                manufacturer.companyPinNo?.let { it1 ->
                    iPvocApplicationRepo.findFirstByConpanyNameAndCompanyPinNo(
                        it,
                        it1
                    )?.let { company ->
                        KotlinLogging.logger {  }.info { "Company name "+company.conpanyName }
                        products?.forEach { product ->
                            val pvocApplicationProductsEntity = PvocApplicationProductsEntity()
                            with(pvocApplicationProductsEntity) {
                                productName = product.productName
                                brand = product.brandName
                                pvocApplicationId = company
                                expirelyDate = product.expriryDate.toString()
                                kebsStandardizationMarkPermit = product.permitNumber
                                createdBy = company.createdBy
                                createdOn = company.createdOn
                                iPvocApplicationProductsRepo.save(pvocApplicationProductsEntity)
                            }
                        }

                        rawMaterials?.forEach { rawMat ->
                            val pvocRawMaterialCategory = PvocExceptionRawMaterialCategoryEntity()
                            with(pvocRawMaterialCategory) {
                                hsCode = rawMat.hsCode
                                rawMaterialDescription = rawMat.rawMaterialDescription
                                dutyRate = rawMat.dutyRate?.toLong()
                                endProduct = rawMat.endProduct
                                countryOfOrgin = rawMat.countryOfOrigin
                                exceptionId = company.id
                                createdBy = company.createdBy
                                createdOn = company.createdOn
                                iPvocExceptionRawMaterialCategoryEntityRepo.save(pvocRawMaterialCategory)
                            }
                        }

                        mainMachinaries?.forEach { machinary ->
                            val machine = PvocExceptionMainMachineryCategoryEntity()
                            with(machine) {
                                hsCode = machinary.hsCode
                                machineDescription = machinary.machineDescription
                                countryOfOrigin = machinary.countryOfOrigin
                                makeModel = machinary.makeModel
                                exceptionId = company.id
                                createdBy = company.createdBy
                                createdOn = company.createdOn
                                iPvocExceptionMainMachineryCategoryEntityRepo.save(machine)
                            }
                        }

                        spares?.forEach { spare ->
                            val spareEntity = PvocExceptionIndustrialSparesCategoryEntity()
                            with(spareEntity) {
                                hsCode = spare.hsCode
                                machineToFit = spare.machineToFit
                                countryOfOrigin = spare.countryOfOrigin
                                industrialSpares = spare.industrialSpares
                                exceptionId = company.id
                                createdBy = company.createdBy
                                createdOn = company.createdOn
                                iPvocExceptionIndustrialSparesCategoryEntityRepo.save(spareEntity)
                            }
                        }
                        pvocExceptionApp.id?.let {
                            userDetails?.id?.let { it1 ->
                                pvocBpmn.startPvocApplicationExemptionsProcess(
                                    it,
                                    it1
                                )
                                KotlinLogging.logger {  }.info { "Object id $it" }
                                KotlinLogging.logger {  }.info { "Object id $it1" }
                            }
                        }
                        pvocExceptionApp.id?.let { pvocBpmn.pvocEaSubmitApplicationComplete(it, 502) }
                        KotlinLogging.logger {  }.info { "Saved OK "+company.conpanyName }
                        return UploadedFileResponse()
                    }
                }
            }?: throw Exception("Application does not exist")
        }

    }
    @PostMapping("application-exception2", consumes = ["multipart/form-data"])
    fun saveApplicationException(
       @RequestParam("conpanyName",required = false) conpanyName : String,
       @RequestParam("uploadedfile", required = false) uploadedfile: MultipartFile,
       @RequestParam("companyPinNo",  required = false) companyPinNo : String,
       @RequestParam("email",  required = false) email : String,
       @RequestParam("telephoneNo",  required = false) telephoneNo : String,
       @RequestParam("postalAadress",  required = false) postalAadress : String,
       @RequestParam("physicalLocation",  required = false) physicalLocation : String
    ): UploadedFileResponse {
        iPvocApplicationRepo.findFirstByConpanyNameAndCompanyPinNo(conpanyName, companyPinNo)?.let { company ->
            fileExcelProcessService?.store(uploadedfile,company )
            return UploadedFileResponse()
        }?: commonDaoServices.getLoggedInUser().let { userDetails ->
            val manufacturer = PvocApplicationEntity()
            manufacturer.companyPinNo = companyPinNo
            manufacturer.email = email
            manufacturer.telephoneNo = telephoneNo
            manufacturer.conpanyName = conpanyName
            manufacturer.postalAadress = postalAadress
            manufacturer.physicalLocation = physicalLocation
            with(manufacturer){
                createdBy = userDetails?.firstName + " " + userDetails?.lastName
                createdOn = Timestamp.from(Instant.now())
                applicationDate = java.util.Date.from(Instant.now())
                finished = 0
                iPvocApplicationRepo.save(manufacturer)
            }
            fileExcelProcessService?.store(uploadedfile,manufacturer )
            return UploadedFileResponse()
        }

    }
}