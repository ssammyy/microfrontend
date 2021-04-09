package org.kebs.app.kotlin.apollo.api.controllers.qaControllers

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.utils.DummyProduct
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.QualityAssuranceBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.QualityAssuranceDaoServices
import org.kebs.app.kotlin.apollo.common.exceptions.ServiceMapNotFoundException
import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
//import org.kebs.app.kotlin.apollo.api.service.FileService
import org.kebs.app.kotlin.apollo.store.model.ApplicationQuestionnaireEntity
import org.kebs.app.kotlin.apollo.store.model.DmarkForeignApplicationsEntity
import org.kebs.app.kotlin.apollo.store.model.InvoiceEntity
import org.kebs.app.kotlin.apollo.store.model.PermitApplicationEntity
import org.kebs.app.kotlin.apollo.store.repo.*
import org.kebs.app.kotlin.apollo.store.repo.qa.IPermitTypesEntityRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.sql.Date
import java.sql.Timestamp
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import javax.servlet.http.HttpSession
import javax.validation.Valid

@Controller
@RequestMapping("/dmark/")
class QADmarkController(
    private val permitRepo: IPermitRepository,
    private val permitTypesRepo: IPermitTypesEntityRepository,
    private val standardsCategoryRepository: IStandardCategoryRepository,
    private val sampleStandardsRepository: ISampleStandardsRepository,
    private val applicationQuestionnaireRepo: IApplicationQuestionnaireRepository,
    private val manufacturerRepository: IManufacturerRepository,
    private val userRepository: IUserRepository,
    private val productsRepo: IProductsRepository,
    private val productSubCategoryRepo: IProductSubcategoryRepository,
    private val serviceMapsRepository: IServiceMapsRepository,
    applicationMapProperties: ApplicationMapProperties,
    private val daoServices: QualityAssuranceDaoServices,
    private val invoiceRepository: IInvoiceRepository,
    private val countriesRepository: ICountriesRepository,
    private val qualityAssuranceBpmn: QualityAssuranceBpmn
) {

    /**
     * Select whether domestic or foreign
     */
    val hofId = applicationMapProperties.hofId
    val pcmId = applicationMapProperties.pcmAssigneeId
    val hodId = applicationMapProperties.qaHod

    @GetMapping("foreign-or-domestic")
    fun dmarkApplicationType(
            model: Model,
            @RequestParam(value = "appId", required = false) appId: Int?,
            @RequestParam("fDomestic") fDomestic: String?
    ): String {
        model.addAttribute("productCategories", standardsCategoryRepository.findAll())
        model.addAttribute("appId", appId)
        model.addAttribute("title", "Foreign/ Domestic")
        model.addAttribute("fDomestic", fDomestic)
        return "quality-assurance/customer/dmark/select-dmark-application-type"
    }

    /**
     * Select application type
     */
    @GetMapping("select")
    fun dmarkSelection(model: Model,
                       @RequestParam(value = "appId", required = false) appId: Int?,
                       @RequestParam("fDomestic") fDomestic: String?
    ): String {
        model.addAttribute("appId", appId)
        model.addAttribute("title", "New/ Renew")
        model.addAttribute("fDomestic", fDomestic)
        return "quality-assurance/customer/dmark/dmark-domestic/dmark-apply-or-renew"
    }

    /**
     * New DMARK Application page
     */
    @GetMapping("apply")
    fun newApplication(
            model: Model,
            @RequestParam(value = "appId", required = false) appId: Int?,
            @RequestParam(value = "pTypeId", required = true) pTypeId: Long?,
            @RequestParam("fDomestic") fDomestic: String?,
            session: HttpSession
    ): String {
        serviceMapsRepository.findByIdOrNull(appId)
                ?.let { map ->
                    session.setAttribute("productCategories", standardsCategoryRepository.findAll())
                    model.addAttribute("dmark", PermitApplicationEntity())
                    model.addAttribute("ptype", permitTypesRepo.findByIdOrNull(pTypeId))
                    model.addAttribute("appId", appId)
                    model.addAttribute("map", map)
                    session.setAttribute("countries", countriesRepository.findAll())
                    model.addAttribute("title", "New Application")
                    model.addAttribute("fDomestic", fDomestic)
                    return "quality-assurance/customer/dmark/dmark-domestic/new-dmark-application"
                }
                ?: throw ServiceMapNotFoundException("Application configuration map not found check configuration")

    }

    // Dmark application
    @PostMapping("apply")
    fun saveDmark(
            model: Model,
            @ModelAttribute("permit") @Valid permitForm: PermitApplicationEntity,
            results: BindingResult,
            session: HttpSession,
            redirectAttributes: RedirectAttributes,
            @RequestParam("file1", required = false) file1: Array<MultipartFile>?,
            @RequestParam("fileElem", required = false) fileElem: Array<MultipartFile>?,
            @RequestParam(value = "pTypeId", required = true) pTypeId: Long?,
            @RequestParam("fDomestic") fDomestic: String?,
            @RequestParam(value = "appId", required = false) appId: Int?
    ): String {

        return try {
            serviceMapsRepository.findByIdOrNull(appId)
                    ?.let { map ->

                        if (fileElem?.isEmpty()!! || file1?.isEmpty()!!) {
                            KotlinLogging.logger { }.info { "One of the files wasn't uploaded" }

                        } else {
                            fileElem.let { daoServices.storeFiles(it) }
                            file1.let { daoServices.storeFiles(it) }
                        }

                        var pmform = permitForm
                        val user = SecurityContextHolder.getContext().authentication?.name

                        user
                                ?.let { username ->
                                    userRepository.findByUserName(username)
                                            ?.let { loggedInUser ->
                                                val man = manufacturerRepository.findByUserId(loggedInUser)
                                                with(pmform) {
                                                    permitType = permitTypesRepo.findByIdOrNull(pTypeId)?.id
                                                    dateCreated = Timestamp.from(Instant.now())
                                                    status = map.inactiveStatus
                                                    createdOn = Timestamp.from(Instant.now())
                                                    expiryDate = map.tokenExpiryHours?.let { Timestamp.from(Instant.now().plus(it, ChronoUnit.HOURS)) }
                                                            ?: Timestamp.from(Instant.now())
                                                    dateAwarded = Timestamp.from(Instant.now())
                                                    manufacturer = man?.id
                                                    manufacturerId = man?.id
                                                    manufacturerEntity = manufacturerRepository.findByUserId(loggedInUser)
                                                    permitNumber = generateRandomText(map.transactionRefLength, map.secureRandom, map.messageDigestAlgorithm)
                                                    factoryInspectionReportStatus = map.inactiveStatus
                                                    schemeOfSupervisionStatus = map.inactiveStatus
                                                    factoryInspectionScheduleStatus = map.inactiveStatus
                                                    foreignApplication = when(fDomestic) {
                                                        "Foreign" -> {
                                                            map.activeStatus
                                                        }
                                                        else -> {
                                                            map.inactiveStatus
                                                        }
                                                    }


                                                    if (file1 != null) {
                                                        if (file1.isNotEmpty()) {
                                                            val fileNames1 = StringBuilder()
                                                            if (file1 != null) {
                                                                file1.forEach { file ->
                                                                    fileNames1.append(file.originalFilename + ",")
                                                                    val documentList: List<String>? = fileNames1.split(",")
                                                                    val newDocumentList = documentList?.dropLast(1)
                                                                    labReportsFilepath1 = newDocumentList.toString().replace("[", "").replace("]", "")
                                                                }
                                                            }
                                                        }
                                                    }

                                                    if (fileElem.isNotEmpty()) {
                                                        val fileNames4 = StringBuilder()
                                                        fileElem.forEach { file ->
                                                            fileNames4.append(file.originalFilename + ",")
                                                            val documentList: List<String>? = fileNames4.split(",")
                                                            val newDocumentList = documentList?.dropLast(1)
                                                            extraDocuments = newDocumentList.toString().replace("[", "").replace("]", "")
                                                        }
                                                    }

                                                    userId = loggedInUser
                                                }

//                                                model.addAttribute("message", "You successfully uploaded files " + file1?.originalFilename + file2?.originalFilename + file3?.originalFilename + "!")
                                                pmform = permitRepo.save(pmform)

                                                /**
                                                 * Start BPMN application review
                                                 */
                                                pmform.id?.let {
                                                    qualityAssuranceBpmn.startQADMApplicationReviewProcess(it, hodId)
                                                }

                                                model.addAttribute("generatedPermitId", pmform.id)
                                                model.addAttribute("questionnaire", ApplicationQuestionnaireEntity())
                                                model.addAttribute("appId", appId)
                                                "quality-assurance/customer/dmark/sta3_domestic"
                                            }
                                            ?: throw UsernameNotFoundException("Invalid session")
                                }
                                ?: throw UsernameNotFoundException("Invalid session")

                    }
                    ?: throw ServiceMapNotFoundException("No Application Map configured for id=$appId")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error { e }
            "quality-assurance/customer/dmark/sta3_domestic"
        }
    }

    /**
     * View dmark permits
     */
    @GetMapping("my-dpermits")
    fun mydPermits(
            model: Model,
            @RequestParam(value = "page", required = false) page: Int?,
            @RequestParam(value = "records", required = false) records: Int?,
            @RequestParam(value = "pTypeId", required = true) pTypeId: Long?,
            @RequestParam(value = "appId", required = false) appId: Int?,
            @RequestParam(value = "appstatus", required = false) appstatus: Int?,
            @RequestParam("fDomestic") fDomestic: String?
    ): String {
        serviceMapsRepository.findByIdOrNull(appId)
                ?.let { map ->
                    daoServices.extractUserFromContext(SecurityContextHolder.getContext())
                            ?.let { user ->
//                                permitTypesRepo.findByIdOrNull(pTypeId)
//                                        ?.let { _ -> }
                                val pageable = PageRequest.of(page ?: 1, records ?: 10)
                                val dummies = mutableListOf<DummyProduct>()
                                when(fDomestic) {
                                    "Foreign" -> {
//                                        permitRepo.findByStatusAndUserIdAndForeignApplicationAndPermitType(appstatus, user,  1, pTypeId)
//                                                .let { permits ->
//                                                    permits?.forEach { i ->
//                                                        val dummyId = DummyProduct()
//                                                        with(dummyId) {
//                                                            id = i.id
//                                                            permitNumber = i.permitNumber
//                                                            product = i.product
//                                                            productName = productsRepo.findByIdOrNull(i.product)?.name
//                                                            brandName = i.tradeMark
//                                                            expiryDate = i.expiryDate
//                                                            status = i.status
//                                                            type = i.permitType
//                                                            permitType = permitTypesRepo.findByIdOrNull(i.permitType)!!.typeName
//                                                        }
//                                                        dummies.add(dummyId)
//                                                    }
                                                    model.addAttribute("permits", daoServices.findAllByManufactureAndType(daoServices.loggedInUserDetails(), 1))
                                                    model.addAttribute("map", map)
                                                    return "quality-assurance/customer/dmark/dmark-domestic/my-dmark-permits"
//                                                }
                                    }
                                    "Domestic" -> {
//                                        permitRepo.findByStatusAndUserIdAndForeignApplicationAndPermitType(appstatus, user, 0, pTypeId)
//                                                .let { permits ->
//                                                    permits?.forEach { i ->
//                                                        val dummyId = DummyProduct()
//                                                        with(dummyId) {
//                                                            id = i.id
//                                                            permitNumber = i.permitNumber
//                                                            product = i.product
//                                                            productName = productsRepo.findByIdOrNull(i.product)?.name
//                                                            brandName = i.tradeMark
//                                                            expiryDate = i.expiryDate
//                                                            status = i.status
//                                                            type = i.permitType
//                                                        }
//                                                        dummies.add(dummyId)
//
//                                                    }
                                        model.addAttribute("permits", pTypeId?.let { daoServices.findAllByManufactureAndType(daoServices.loggedInUserDetails(), it) })
                                                    model.addAttribute("map", map)
                                                    return "quality-assurance/customer/dmark/dmark-domestic/my-dmark-permits"
//                                                }
                                    }
                                    else -> KotlinLogging.logger {  }.info { "Foreign application did not match the expected values" }
                                }

                            }
                            ?:throw UsernameNotFoundException("Invalid Session")
                }
                ?:throw ServiceMapNotFoundException("No service map found for appId=$appId, aborting")
        return "quality-assurance/customer/dmark/dmark-domestic/my-dmark-permits"
    }

        @GetMapping("renew/{id}")
        fun renewalUI(
                model: Model, @PathVariable("id") id: Long,
                @RequestParam(value = "appId", required = false) appId: Int?
        ): String {
            val permitDetails = permitRepo.findByIdOrNull(id)
            model.addAttribute("pr", permitDetails)
            model.addAttribute("std", permitDetails?.productSubCategory?.let { sampleStandardsRepository.findBySubCategoryId(it) })
            model.addAttribute("permit", PermitApplicationEntity())
            model.addAttribute("appId", appId)

            daoServices.extractManufacturerFromUser(daoServices.extractUserFromContext(SecurityContextHolder.getContext()))
                    ?.let { manufacturersEntity ->
                        model.addAttribute("man", manufacturersEntity)
                    }

            val days = Duration.between(permitDetails?.expiryDate?.toInstant(), Instant.now()).toDays()
            model.addAttribute("daysToExpiry", days)
            return "quality-assurance/customer/dmark/dmark-domestic/dmark-renewal-page"
        }

        @GetMapping("renew-dmark/{id}")
        fun renewPermitAction(model: Model,
                              @PathVariable("id") id: Long,
                              @RequestParam(value = "appId", required = false) appId: Int?,
                              @ModelAttribute @Valid permitEnt: PermitApplicationEntity,
                              @ModelAttribute @Valid invoiceEntity: InvoiceEntity?
        ): String {
            serviceMapsRepository.findByIdAndStatus(appId, 1)
                    ?.let { map ->
                        permitRepo.findByIdOrNull(id)
                                ?.let { pm ->
                                    if(pm.foreignApplication == 0) {
                                        invoiceRepository.findByPermitId(pm)
                                                ?.let { i ->
                                                    model.addAttribute("invoice", i)
                                                }


                                    }
                                    model.addAttribute("permit", pm)
                                    model.addAttribute("map", map)
                                    model.addAttribute("std", pm.productSubCategory?.let { sampleStandardsRepository.findBySubCategoryId(it) })
                                    KotlinLogging.logger { }.info { pm.broadProductCategory }
                                    val questionnaire = pm.id?.let { applicationQuestionnaireRepo.findByPermitId(it) }
                                    model.addAttribute("questionnaire", questionnaire)
                                    model.addAttribute("productCategories", standardsCategoryRepository.findAll())

                                    model.addAttribute("permitType", permitTypesRepo.findByIdOrNull(pm?.permitType)?.typeName)

                                    model.addAttribute("std", pm.productSubCategory?.let { sampleStandardsRepository.findBySubCategoryId(it) })
                                    model.addAttribute("product", productsRepo.findByIdOrNull(pm.product))
                                    model.addAttribute("productSubCategory",productSubCategoryRepo.findByIdOrNull(pm.productSubCategory))
                                    model.addAttribute("appl", permitTypesRepo.findByIdOrNull(pm.permitType)?.typeName)

                                    pm.labReportsFilepath1
                                            ?.let { labDocs ->
                                                val labDocList: List<String>? = labDocs?.split(",")
                                                model.addAttribute("labDocs", labDocList)
                                            }


                                    pm.extraDocuments
                                            ?.let { documents ->
                                                val documentList: List<String>? = documents.split(",")
                                                model.addAttribute("documents", documentList)
                                            }
                                    questionnaire?.illustrationFile
                                            ?.let { illustration ->
                                                val documentList: List<String>? = illustration.split(",")
                                                model.addAttribute("illustrations", documentList)
                                            }
                                    questionnaire?.manufacturingStepsFile
                                            ?.let { manufacturingSteps ->
                                                val documentList: List<String>? = manufacturingSteps.split(",")
                                                model.addAttribute("manufacturingStepsFiles", documentList)
                                            }
                                    questionnaire?.testsAgainstTheStandardFile
                                            ?.let { testsAgainstTheStandard ->
                                                val documentList: List<String>? = testsAgainstTheStandard.split(",")
                                                model.addAttribute("testsAgainstTheStandardFiles", documentList)
                                            }
                                    questionnaire?.qualityManualFile
                                            ?.let { qualityManualFile ->
                                                val documentList: List<String>? = qualityManualFile.split(",")
                                                model.addAttribute("qualityManualFiles", documentList)
                                            }
                                    questionnaire?.testReportsLevelOfDefectivesFile
                                            ?.let { testReportsLevelOfDefectives ->
                                                val documentList: List<String>? = testReportsLevelOfDefectives.split(",")
                                                model.addAttribute("testReportsLevelOfDefectivesFiles", documentList)
                                            }

                                }
                        return "quality-assurance/customer/dmark/review-sta3-questionnaire"
                    }
                    ?:throw ServiceMapNotFoundException("Map Not found")
        }

        @PostMapping("saveNoOfSites/{id}")
        fun updateNoOfSites(
                model: Model,
                @PathVariable("id") id: Long,
                @ModelAttribute("permit") @Valid permit: PermitApplicationEntity,
                @RequestParam("appId") appId: Int?
        ): String {

            permitRepo.findByIdOrNull(permit.id)
                    ?.let { i ->
                        with(i) {
                            permit.noOfSitesProducingTheBrand?.let { noOfSitesProducingTheBrand = permit.noOfSitesProducingTheBrand }
                            permit.product?.let {  product = permit.product }
                            permit.productSubCategory?.let { productCategory = permit.productSubCategory }
                            permit.broadProductCategory?.let { broadProductCategory = permit.broadProductCategory }
                            permit.productCategory?.let { productCategory = permit.productCategory }
                            permit.siteApplicable?.let { siteApplicable = permit.siteApplicable }
                        }
//                        i.noOfSitesProducingTheBrand = permit.noOfSitesProducingTheBrand
//                        i.tradeMark = permit.tradeMark
//                        i.siteApplicable = permit.siteApplicable
                        permitRepo.save(i)
                        model.addAttribute("appId", appId)
                        KotlinLogging.logger { }.info { i.noOfSitesProducingTheBrand }
                    }
//            permitRepo.save(permit)
//            model.addAttribute("appId", appId)
//            KotlinLogging.logger { }.info { "Updated application details" }

            return "redirect:/dmark/renew-dmark/{id}?appId=$appId"
        }
    }