package org.kebs.app.kotlin.apollo.api.controllers.qaControllers


import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.adaptor.kafka.producer.service.SendToKafkaQueue
import org.kebs.app.kotlin.apollo.api.utils.DummyProduct
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.QualityAssuranceBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.QualityAssuranceDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.RegistrationDaoServices
import org.kebs.app.kotlin.apollo.common.exceptions.MissingConfigurationException
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.common.exceptions.ServiceMapNotFoundException
import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.qa.PermitTypesEntity
import org.kebs.app.kotlin.apollo.store.repo.*
import org.kebs.app.kotlin.apollo.store.repo.qa.IPermitTypesEntityRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.sql.Timestamp
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.servlet.http.HttpSession
import javax.validation.Valid



@Controller
@RequestMapping("/api/permit")
@SessionAttributes("appId")
class QAController(

    private val daoServices: QualityAssuranceDaoServices,
    private val questionnaireSta10Repo: IQuestionnaireSta10Repository,
    private val permitRepo: IPermitRepository,
    private val serviceRequestsRepository: IServiceRequestsRepository,
    private val sendToKafkaQueue: SendToKafkaQueue,
    private val bufferRepo: INotificationsBufferRepository,
    private val notificationsRepo: INotificationsRepository,
    private val serviceMapsRepo: IServiceMapsRepository,
    private val countriesRepository: ICountriesRepository,
    private val permitTypesRepo: IPermitTypesEntityRepository,
    private val manufacturerRepository: IManufacturerRepository,
    private val userRepository: IUserRepository,
    private val userTypesRepo: IUserTypesEntityRepository,
    private val permitRemarksRepository: IPermitApplicationRemarksRepository,
    private val productSubCategoryRepo: IProductSubcategoryRepository,
    private val standardsCategoryRepository: IStandardCategoryRepository,
    private val sampleStandardsRepository: ISampleStandardsRepository,
    private val broadProductCategoryRepository: IBroadProductCategoryRepository,
    private val iManufacturerProductBrandRepository: IManufacturerProductBrandRepository,
    private val invoiceRepository: IInvoiceRepository,
    private val sender: JavaMailSender,
    private val schemeRepo: ISchemeOfSupervisionRepository,
    private val applicationQuestionnaireRepo: IApplicationQuestionnaireRepository,
    private val notificationTypesRepo: INotificationTypesRepository,
    applicationMapProperties: ApplicationMapProperties,
    private val controllerDao: CommonDaoServices,
    private val qualityAssuranceBpmn: QualityAssuranceBpmn,
    private val productsRepo: IProductsRepository,
    private val factoryInspectionRepository: IFactoryInspectionReportRepository,
    private val standardLevyPaymentsRepository: IStandardLevyPaymentsRepository
) {

    val appId: Int = applicationMapProperties.mapPermitApplication
    val assigneeId: Long = applicationMapProperties.assigneeId

    @Value("\${bpmn.qa.app.review.process.definition.key}")
    lateinit var qaAppReviewProcessDefinitionKey: String

    val hofId = applicationMapProperties.hofId

    val qaoId = applicationMapProperties.qaoAssigneeId


    @GetMapping("")
    fun qaIndex(
            model: Model
    ): String {
        var result = ""
        SecurityContextHolder.getContext().authentication
                ?.let { m ->
                    result = if (m.authorities.contains(SimpleGrantedAuthority("PERMIT_APPLICATION"))) {
                        "redirect:/api/permit/customer"
                    }
//                    else if (m.authorities.contains(SimpleGrantedAuthority("ALLOCATE_DMARK_TO_QAO"))  ||
//                            m.authorities.contains(SimpleGrantedAuthority("ASSIGN_OFFICER")) ||
//                            m.authorities.contains(SimpleGrantedAuthority("APPROVE_PERMIT")) ||
//                            m.authorities.contains(SimpleGrantedAuthority("GIVE_RECOMMENDATION_ON_PERMIT_AWARD"))) {
//                        "redirect:/api/qa/hof"
//                    }
//                    else if (m.authorities.contains(SimpleGrantedAuthority("SCHEDULE_FACTORY_VISIT")) ||
//                            m.authorities.contains(SimpleGrantedAuthority("ASSESS_DMARK")) ||
//                            m.authorities.contains(SimpleGrantedAuthority("REVIEW_DMARK_ASSESSMENT")) ||
//                            m.authorities.contains(SimpleGrantedAuthority("PETROLEUM_ALLOCATE_INSPECTION_OFFICER")) ||
//                            m.authorities.contains(SimpleGrantedAuthority("RECEIVE_EPRA_NOTIFICATION_LETTER"))) {
//                        "redirect:/api/qao/applications"
//                    }
//                    else if (m.authorities.contains(SimpleGrantedAuthority("GENERATE_BS_NUMBER"))) {
//                        "redirect:/inspection?whereTo=lab_home"
////                        "redirect:/api/qao/lab"
//                    }
//                    else if (m.authorities.contains(SimpleGrantedAuthority("APPROVE_PERMIT"))) {
//                        "redirect:/psc_pcm"
//                    }
//                    else if (m.authorities.contains(SimpleGrantedAuthority("GIVE_RECOMMENDATION_ON_PERMIT_AWARD"))) {
//                        "redirect:/psc_pcm"
//                    }
                    else {
                        "quality-assurance/secured-home"
                    }
                }
            model.addAttribute("appId", appId)
        return result
    }

    /**
     * Customer home
     */
    @GetMapping("/customer")
    fun customerHomePage(
            model: Model,
            @RequestParam(value = "appId", required = false) appId: Int?
    ): String {
        model.addAttribute("permitTypes", permitTypesRepo.findByStatus(1))
        model.addAttribute("title", "Customer home")
        model.addAttribute("appId", appId)

        SecurityContextHolder.getContext().authentication?.name
                ?.let { username ->
                    userRepository.findByUserName(username)
                            ?.let { loggedInUser ->
                                daoServices.extractManufacturerFromUser(loggedInUser)
                                        ?.let { manufacturer ->
                                            loggedInUser.id?.let {
                                                qualityAssuranceBpmn.fetchAllTasksByAssignee(it)?.let { lstTaskDetails ->
                                                    model.addAttribute("tasks", lstTaskDetails)
                                                }
                                                model.addAttribute("manufacturer", manufacturer)
//                                                val baseUrl: String = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
//                                                KotlinLogging.logger {  }.info { "Base Url: $baseUrl" }
                                                standardLevyPaymentsRepository.findByManufacturerEntity(manufacturer)
                                                        .let { payments ->
                                                            model.addAttribute("payments", payments)
//                                                            KotlinLogging.logger {  }.info { payments?.size }
                                                            //model.addAttribute("manufacturer", manufacturer)
                                                        }
                                            }
                                        }

                            }
                }

        return "quality-assurance/customer/customer-home"
    }

    @GetMapping("/my-applications")
    fun myApplications(
            model: Model,
            @RequestParam(value = "page", required = false) page: Int?,
            @RequestParam(value = "records", required = false) records: Int?,
            @RequestParam(value = "appstatus", required = false) appstatus: Int?,
            redirectAttributes: RedirectAttributes
    ): String {
        return try {
            val user = SecurityContextHolder.getContext().authentication?.name
            user
                    ?.let { username ->
                        userRepository.findByUserName(username)
                                ?.let { loggedInUser ->
                                    val pageable = PageRequest.of(page ?: 1, records ?: 10)
                                    val dummies = mutableListOf<DummyProduct>()
//                                    permitRepo.findByStatusAndUserId(appstatus, loggedInUser, pageable)
//                                            .let { permits ->
//                                                permits.forEach { i ->
//                                                    val dummyId = DummyProduct()
//                                                    with(dummyId){
//                                                        id = i.id
//                                                        permitNumber = i.permitNumber
//                                                        product = i.product
//                                                        productName = productsRepo.findByIdOrNull(i.product)?.name
//                                                        brandName = i.tradeMark
//                                                        expiryDate = i.expiryDate
//                                                        status = i.status
//                                                        type = i.permitType
//                                                        dateCreated = i.dateCreated
//                                                        permitType = permitTypesRepo.findByIdOrNull(i.permitType)?.typeName
//                                                    }
//                                                    KotlinLogging.logger {  }.info { permitTypesRepo.findByIdOrNull(i.permitType)?.typeName }
//                                                    dummies.add(dummyId)
//                                                }
                                                model.addAttribute("applications", daoServices.findAllByManufacture(daoServices.loggedInUserDetails()))
                                                KotlinLogging.logger {  }.info { dummies }
//                                            }

                                }
                                ?: throw UsernameNotFoundException("Invalid session")
                    }
                    ?: throw UsernameNotFoundException("Invalid session")
            "quality-assurance/customer/application-history"
        } catch (e: java.lang.Exception) {
            redirectAttributes.addFlashAttribute("error", "Couldn't load your applications!")
            KotlinLogging.logger { }.info { "Caught an error, $e" }
            "quality-assurance/customer/application-history"
        }
    }

    /**
     * application details page
     */
    @GetMapping("/my-applications/{id}")
    fun myApplicationDetails(
            model: Model, @PathVariable("id") id: Long,
            @RequestParam(value = "page", required = false) page: Int?,
            @RequestParam(value = "records", required = false) records: Int?,
            @RequestParam("schemeOfSupervisionId", required = false
            ) schemeOfSupervisionId: Long?
    ): String {
        permitRepo.findByIdOrNull(id)
                ?.let { pm ->
                    permitRemarksRepository.findByPermitId(pm)
//                    model.addAttribute("comments", permitRemarksRepository.findAllByPermitId(pm, PageRequest.of(page ?: 1, records ?: 10)))
                    model.addAttribute("comments", permitRemarksRepository.findByPermitId(pm))
                    model.addAttribute("app", pm)
                    model.addAttribute("remarks", PermitApplicationRemarksEntity())
                    model.addAttribute("std", sampleStandardsRepository.findBySubCategoryId(pm.productSubCategory))
                    questionnaireSta10Repo.findByPermitId(pm.id)
                            ?.let { questionnaire ->
                                model.addAttribute("questionnaire", questionnaire)
                                pm.extraDocuments
                                        ?.let { documents ->
                                            val documentList: List<String>? = documents.split(",")
                                            model.addAttribute("documents", documentList)
                                        }
                                questionnaire.certificateFile
                                        ?.let { certDoc ->
                                            val documentList: List<String>? = certDoc.split(",")
                                            model.addAttribute("certDocs", documentList)
                                        }
                                questionnaire.testingRecordFile
                                        ?.let { testingRecordDoc ->
                                            val documentList: List<String>? = testingRecordDoc.split(",")
                                            model.addAttribute("testingRecordDocs", documentList)
                                        }
                                questionnaire.processMonitoringRecordsFile
                                        ?.let { testingRecordDoc ->
                                            val documentList: List<String>? = testingRecordDoc.split(",")
                                            model.addAttribute("testingRecordDocs", documentList)
                                        }
                                questionnaire.frequencyUpload
                                        ?.let { testingRecordDoc ->
                                            val documentList: List<String>? = testingRecordDoc.split(",")
                                            model.addAttribute("testingRecordDocs", documentList)
                                        }
                                questionnaire.criticalProcessParametersUpload
                                        ?.let { criticalProcessParametersUploadDoc ->
                                            val documentList: List<String>? = criticalProcessParametersUploadDoc.split(",")
                                            model.addAttribute("criticalProcessParametersUploadDocs", documentList)
                                        }
                                questionnaire.operationsUpload
                                        ?.let { operationsUploadDoc ->
                                            val documentList: List<String>? = operationsUploadDoc.split(",")
                                            model.addAttribute("operationsUploadDocs", documentList)
                                        }
                                questionnaire.processFlowOfProductionUpload
                                        ?.let { processFlowOfProductionUploadDoc ->
                                            val documentList: List<String>? = processFlowOfProductionUploadDoc.split(",")
                                            model.addAttribute("processFlowOfProductionUploadDocs", documentList)
                                        }
                            }
                    model.addAttribute("questionnaire", questionnaireSta10Repo.findByPermitId(pm.id))
                    model.addAttribute("standardSchemeOfSupervision", schemeRepo.findByIdOrNull(1))
                    model.addAttribute("title", "Application details")
                    factoryInspectionRepository.findByPermitApplicationId(pm)
                            ?.let { fr->
                                model.addAttribute("report", fr)
                            }
                    permitTypesRepo.findByIdOrNull(pm.permitType)
                            ?.let { type ->
                                model.addAttribute("type", type)
                            }
                    pm.id?.let {
                        applicationQuestionnaireRepo.findByPermitId(it)
                                ?.let { qn ->
                                    model.addAttribute("qn", qn)
                                    pm.labReportsFilepath1
                                            ?.let { labDocs ->
                                                val labDocList: List<String>? = labDocs.split(",")
                                                model.addAttribute("labDocs", labDocList)
                                            }
                                    pm.extraDocuments
                                            ?.let { documents ->
                                                val documentList: List<String>? = documents.split(",")
                                                model.addAttribute("documents", documentList)
                                            }
                                    qn.illustrationFile
                                            ?.let { illustration ->
                                                val documentList: List<String>? = illustration.split(",")
                                                model.addAttribute("illustrations", documentList)
                                            }
                                    qn.manufacturingStepsFile
                                            ?.let { manufacturingSteps ->
                                                val documentList: List<String>? = manufacturingSteps.split(",")
                                                model.addAttribute("manufacturingStepsFiles", documentList)
                                            }
                                    qn.testsAgainstTheStandardFile
                                            ?.let { testsAgainstTheStandard ->
                                                val documentList: List<String>? = testsAgainstTheStandard.split(",")
                                                model.addAttribute("testsAgainstTheStandardFiles", documentList)
                                            }
                                    qn.qualityManualFile
                                            ?.let { qualityManualFile ->
                                                val documentList: List<String>? = qualityManualFile.split(",")
                                                model.addAttribute("qualityManualFiles", documentList)
                                            }
                                    qn.testReportsLevelOfDefectivesFile
                                            ?.let { testReportsLevelOfDefectives ->
                                                val documentList: List<String>? = testReportsLevelOfDefectives.split(",")
                                                model.addAttribute("testReportsLevelOfDefectivesFiles", documentList)
                                            }
                                }
                    }
                }


        return "quality-assurance/customer/application-details"
    }

    /**
     * view smark permits
     */
    @GetMapping("/my-spermits")
    fun sMarks(
            model: Model,
            @RequestParam(value = "page", required = false) page: Int?,
            @RequestParam(value = "records", required = false) records: Int?,
            @RequestParam(value = "permitTypeId", required = false) permitTypeId: Long,
            @RequestParam(value = "appstatus", required = false) appstatus: Int?,
            @RequestParam(value = "appId", required = false) appId: Int?
    ): String {

        val pageable = PageRequest.of(page ?: 1, records ?: 10)

        model.addAttribute("permits", daoServices.findAllByManufactureAndType(daoServices.loggedInUserDetails(), permitTypeId))
        model.addAttribute("appId", appId)
        return "quality-assurance/customer/smark/my-smark-permits"

    }



    /**
     * SMARK/ FMARK && DMARK domestic permit details
     */
    @GetMapping("/permit-details/{id}")
    fun permitDetails(
            model: Model, @PathVariable("id") id: Long,
            @RequestParam(value = "appId", required = false) appId: Int?
    ): String {
        model.addAttribute("permit", PermitApplicationEntity())
        permitRepo.findByIdOrNull(id)
                ?.let { permDetails ->
                    model.addAttribute("pr", permDetails)
                    permDetails.productSubCategory?.let { categoryId ->
                        sampleStandardsRepository.findBySubCategoryId(categoryId)
                                ?.let { sampleStandards ->
                                    model.addAttribute("std", sampleStandards)
                                    model.addAttribute("appId", appId)

                                }

                    }
                }

        return "quality-assurance/customer/smark/renew"
    }

//    Manufacturer Correcting the application
    @GetMapping("/correct_resubmit_application")
    fun manufacturerCorrectApplication(
            model: Model,
            @RequestParam("permitId") permitId: Long,
            @RequestParam("action") action: Int,
            redirectAttributes: RedirectAttributes
    ): String {
    var result = ""
        when(action) {
//            1 -> {
//                qualityAssuranceBpmn.qaAppReviewCorrectApplication(permitId).let {
//                    redirectAttributes.addFlashAttribute("message", "You have successfully corrected your application. Submit it for further review.")
//                    result =  "redirect:/api/v1/permit/my-applications/${permitId}"
//                }
//            }
            2 -> {
                qualityAssuranceBpmn.qaAppReviewCorrectApplication(permitId)
                        .let {
                            qualityAssuranceBpmn.qaAppReviewResubmitApplication(permitId)
                                    .let {
                                        redirectAttributes.addFlashAttribute("alert", "Your application has been resubmitted.")
                                        result =  "redirect:/api/permit/customer"
                                    }
                        }
            }
        }
        return result
    }


    /////////////////////////////////
    @GetMapping("/application-type/{id}")
    fun applyOrRenew(model: Model,
                     @PathVariable("id") id: Long,
                     @RequestParam(value = "appId", required = false) appId: Int?,
                     session: HttpSession, @ModelAttribute permitType: PermitTypesEntity
    ): String {
//            model.addAttribute("ptype", permitTypesRepo.findById(id.toInt()))
        session.setAttribute("ptype", permitTypesRepo.findById(id))
        model.addAttribute("permitTypeId", id)
        model.addAttribute("appId", appId)
        var result = ""
        /**
         * TODO: Lets discuss to understand better what you're trying to achieve
         */
        permitTypesRepo.findByIdOrNull(id)
                ?.let { t ->
                    result = if (t.id == 1L) {
                        model.addAttribute("title", "DMARK/ FOREIGN")
                        "quality-assurance/customer/dmark/select-dmark-application-type"
                    } else {
                        model.addAttribute("title", "Apply/ Renew")
                        "quality-assurance/customer/apply-or-renew"
                    }
                }
        return result
    }

    @GetMapping("/apply/new/{id}")
    fun applyNewPermit(
            model: Model,
            @PathVariable("id") id: Long?,
            session: HttpSession,
            @RequestParam(value = "appId", required = false) appId: Int?
//                           @SessionAttribute("ptype") ptype: PermitTypesEntity
    ): String {
        var result = ""
        return try {
            serviceMapsRepo.findByIdOrNull(appId)
                    ?.let { map ->
                        permitTypesRepo.findByIdOrNull(id)
                                ?.let { t ->
                                    model.addAttribute("ptype", t)
                                    model.addAttribute("permitType", t.id)
                                    model.addAttribute("myPermit", PermitApplicationEntity())
                                    model.addAttribute("productCategories", standardsCategoryRepository.findAll())
                                    model.addAttribute("map", map.id)
                                    model.addAttribute("appId", appId)

                                    result = if (t.id == 1L) {
                                        "quality-assurance/customer/dmark/select-dmark-application-type"
                                    } else {
                                        "quality-assurance/customer/fmark-smark-application"
                                    }

                                }

                    }
            return result
        } catch (e: Exception) {
            KotlinLogging.logger { }.info { e }
            /**
             * TODO: Does not make sense to return an empty string
             */

            "quality-assurance/customer/fmark-smark-application"
        }
    }

    /**
     * All user permits
     */
    @GetMapping("/all-permits")
    fun getAllPermits(
            model: Model,
            @RequestParam(value = "page", required = false) page: Int?,
            @RequestParam(value = "records", required = false) records: Int?,
            @RequestParam(value = "appstatus", required = false) appstatus: Int?
    ): String {
        return try {
            SecurityContextHolder.getContext().authentication?.name
                    ?.let { username ->
                        userRepository.findByUserName(username)
                                ?.let { loggedInUser ->
//                                    model.addAttribute("permits", permitRepo.findByStatusAndUserId(status, loggedInUser, PageRequest.of(page ?: 1, records ?: 10)))
                                    val pageable = PageRequest.of(page ?: 1, records ?: 10)
//                                    val dummies = mutableListOf<DummyProduct>()
//                                    permitRepo.findByStatusAndUserId(appstatus, loggedInUser, pageable)
//                                            .let { permits ->
//                                                permits.forEach { i->
//                                                    val dummyId = DummyProduct()
//                                                    with(dummyId){
//                                                        id = i.id
//                                                        permitNumber = i.permitNumber
//                                                        product = i.product
//                                                        productName = productsRepo.findByIdOrNull(i.product)?.name
//                                                        brandName = i.tradeMark
//                                                        expiryDate = i.expiryDate
//                                                        status = i.status
//                                                        type = i.permitType
//                                                        processStatus = i.processStatus
//                                                        permitType = permitTypesRepo.findByIdOrNull(i.permitType)?.typeName
//                                                    }
//                                                    KotlinLogging.logger {  }.info { permitTypesRepo.findByIdOrNull(i.permitType)?.typeName }
//                                                    println("Next")
//                                                    KotlinLogging.logger {  }.info { permitTypesRepo.findByIdOrNull(i.permitType) }
//                                                    dummies.add(dummyId)
//
//                                                }
                                    model.addAttribute("permits", daoServices.findAllByManufacture(daoServices.loggedInUserDetails()))
//                                            }

                                    loggedInUser.id?.let {
                                        qualityAssuranceBpmn.fetchAllTasksByAssignee(it)?.let { lstTaskDetails ->
                                            model.addAttribute("tasks", lstTaskDetails)
                                        }
                                    }
                                }
                    }
            "quality-assurance/customer/my-permits"
        } catch (e: Exception) {
            model.addAttribute("error", "Couldn't load your permits!")
            KotlinLogging.logger { }.info { "Caught an exception, $e" }
            "quality-assurance/customer/my-permits"
        }

    }

    /**
     * New FMARK application
     */
    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @PostMapping("/apply/new/smark")
    fun smarkApplication(
            model: Model,
            @ModelAttribute("myPermit") myPermit: PermitApplicationEntity,
            @RequestParam(value = "appId", required = false) appId: Int?,
            session: HttpSession,
            @RequestParam(value = "pType", required = false) pType: Long,
            @RequestParam("fileElem", required = false) fileElem: Array<MultipartFile>?,
            redirectAttributes: RedirectAttributes
    ): String {
        return try {
            KotlinLogging.logger { }.error( "Permit app id ${myPermit.id}" )

            if (fileElem?.isEmpty() == true) {
                KotlinLogging.logger {  }.info { "fileElem is empty" }
            }
            else {
                fileElem.let { it?.let { it1 -> daoServices.storeFiles(it1) } }
            }

            serviceMapsRepo.findByIdOrNull(appId)
                    ?.let { map ->
                        var pmForm = myPermit
                        SecurityContextHolder.getContext().authentication?.name
                                ?.let { username ->
                                    userRepository.findByUserName(username)
                                            ?.let { loggedInUser ->
                                                daoServices.extractManufacturerFromUser(loggedInUser)
                                                        ?.let { man ->
                                                            var userTypeId:Long = 0
                                                            userTypesRepo.findByIdOrNull(4)?.id?.let{
                                                                userTypeId = it
                                                            }
                                                            if (loggedInUser.userTypes !== userTypeId) {
                                                                with(pmForm) {
                                                                    dateCreated = Timestamp.from(Instant.now())
                                                                    status = map.inactiveStatus
                                                                    createdOn = Timestamp.from(Instant.now())
//                                                                    map.tokenExpiryHours?.let { expiryDate = Timestamp.from(Instant.now().plus(it, ChronoUnit.HOURS)) }
                                                                    dateAwarded = Timestamp.from(Instant.now())
                                                                    factoryInspectionReportStatus = map.inactiveStatus
                                                                    schemeOfSupervisionStatus = map.inactiveStatus
                                                                    factoryInspectionScheduleStatus = map.inactiveStatus
                                                                    userId = loggedInUser
                                                                    createdBy = "${loggedInUser.firstName}  ${loggedInUser.lastName}"
//                                                                    manufacturer = manufacturerRepository.findByUserId(loggedInUser)?.id
                                                                    manufacturer = man.id
//                                                                    manufacturerEntity = manufacturerRepository.findByUserId(loggedInUser)
                                                                    manufacturerEntity = man
                                                                    permitNumber = generateRandomText(map.transactionRefLength, map.secureRandom, map.messageDigestAlgorithm, false)
//                                                                    manufacturerId = manufacturerRepository.findByUserId(loggedInUser)?.id
                                                                    manufacturerId = man.id
                                                                    manufacturerName = man.name
                                                                    if (fileElem?.isNotEmpty() == true) {
                                                                        val fileNames4 = StringBuilder()
                                                                        fileElem.forEach { file ->
                                                                            fileNames4.append(file.originalFilename + ",")
                                                                            val documentList: List<String>? = fileNames4.split(",")
                                                                            val newDocumentList = documentList?.dropLast(1)
                                                                            extraDocuments = newDocumentList.toString().replace("[", "").replace("]", "")
                                                                        }
                                                                    }

                                                                }
                                                            }
                                                            else {
                                                                redirectAttributes.addFlashAttribute("message", "Caught an exception")
                                                                return "redirect:/api/permit/apply/new/smark"
                                                            }
                                                            pmForm = permitRepo.save(pmForm)
                                                            KotlinLogging.logger {  }.info { "Permit saved with id ${pmForm.id}" }

//                                                            // Start QA review process (BPMN)
//                                                            pmForm.id?.let {
//                                                                qualityAssuranceBpmn.startQAAppReviewProcess(it, hofId)
//                                                            }


                                                            redirectAttributes.addFlashAttribute("message", "Your application has been saved")
                                                            KotlinLogging.logger { }.trace("${this::smarkApplication.name} saved with id =[${pmForm.id}] ")
                                                            KotlinLogging.logger { }.debug { "Permit app id ${pmForm.id}" }
                                                            model.addAttribute("generatedPermitId", pmForm.id)
                                                            model.addAttribute("countries", countriesRepository.findAll())
                                                            model.addAttribute("qn", QuestionnaireSta10Entity())
                                                            model.addAttribute("appId", appId)
                                                            model.addAttribute("permitForm", pmForm)
                                                            val applicationFor = "application"
                                                            model.addAttribute("application", applicationFor)
                                                            model.addAttribute("title", "STA 10 Questionnaire")
                                                            "quality-assurance/customer/fmark/sta10"
                                                        }


                                            }
                                            ?: throw UsernameNotFoundException("Invalid session")

                                }
                                ?: throw UsernameNotFoundException("Invalid session")


                    }
                    ?: throw ServiceMapNotFoundException("Application configuration map not found check configuration")


        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message,e)

            "redirect:/api/permit/apply/new/smark"
        }

    }

    /**
     * Renew permit
     */
    @GetMapping("/permit/renew/{id}")
    fun renewalUI(
            model: Model,
            @PathVariable("id") id: Long
//            @RequestParam("appId") appId: Long
    ): String {
        permitRepo.findByIdOrNull(id)
                ?.let { permitDetails ->
                    model.addAttribute("pr", permitDetails)
                    model.addAttribute("std", sampleStandardsRepository.findBySubCategoryId(permitDetails.productSubCategory))
                    val days = Duration.between(permitDetails.expiryDate?.toInstant(), Instant.now()).toDays()
                    model.addAttribute("daysToExpiry", days)
                    model.addAttribute("permit", PermitApplicationEntity())
                    manufacturerRepository.findByIdOrNull(permitDetails.manufacturer)
                            ?.let { man->
                                model.addAttribute("man", man)
                            }
                    model.addAttribute("title", "Renew permit ${permitDetails.permitNumber}")
                    model.addAttribute("appId", appId)
                    model.addAttribute("permitType", permitTypesRepo.findByIdOrNull(permitDetails.permitType))
                    model.addAttribute("product", productsRepo.findByIdOrNull(permitDetails.product))

                }
        return "quality-assurance/customer/smark/renew"
    }


    @GetMapping("/permit/renew-get/{id}")
    fun renewPermitAction(
            model: Model,
            @PathVariable("id") id: Long,
            redirectAttributes: RedirectAttributes
//            @RequestParam(value = "appId", required = false) appId: Int?

    ): String {
        var result = ""
        serviceMapsRepo.findByIdOrNull(103)
                ?.let { map ->
                    try {
                        permitRepo.findByIdOrNull(id)
                                ?.let { per ->
                                    //            val inv = per?.let { qCont.invoiceGen(InvoiceEntity(), it) }
                                    invoiceRepository.findByPermitId(per)
                                            ?.let { i ->
                                                model.addAttribute("invoice", i)
                                            }


                                    model.addAttribute("permit", per)
                                    model.addAttribute("std", per.productSubCategory?.let { sampleStandardsRepository.findBySubCategoryId(it) })
                                    KotlinLogging.logger { }.debug { per.broadProductCategory }

                                    with(per) {
                                        map.tokenExpiryHours?.let { expiryDate = Timestamp.from(Instant.now().plus(it, ChronoUnit.HOURS)) }
                                    }
                                    permitRepo.save(per)

                                    model.addAttribute("permitType", permitTypesRepo.findByIdOrNull(per.permitType))
                                    per.productSubCategory?.let {
                                        model.addAttribute("productSubCategory",productSubCategoryRepo.findByIdOrNull(it))
                                    }

                                    model.addAttribute("permit", permitRepo.findByIdOrNull(per.id))
                                    model.addAttribute("product", productsRepo.findByIdOrNull(per.product))
                                    model.addAttribute("productSubCategory",productSubCategoryRepo.findByIdOrNull(per.productSubCategory))

                                    val questionnaire = questionnaireSta10Repo.findByPermitId(per.id)
                                    model.addAttribute("questionnaire", questionnaire)

                                    model.addAttribute("productCategories", standardsCategoryRepository.findAll())

                                    model.addAttribute("title", "Review questionnaire")
                                    model.addAttribute("appId", appId)

                                    // Extra docs
                                    per.extraDocuments
                                            ?.let { documents ->
                                                val documentList: List<String>? = documents.split(",")
                                                model.addAttribute("documents", documentList)
                                            }
                                    questionnaire?.certificateFile
                                            ?.let { certDoc ->
                                                val documentList: List<String>? = certDoc.split(",")
                                                model.addAttribute("certDocs", documentList)
                                            }

                                    questionnaire?.testingRecordFile
                                            ?.let { testingRecordDoc ->
                                                val documentList: List<String>? = testingRecordDoc.split(",")
                                                model.addAttribute("testingRecordDocs", documentList)
                                            }
                                    questionnaire?.processMonitoringRecordsFile
                                            ?.let { testingRecordDoc ->
                                                val documentList: List<String>? = testingRecordDoc.split(",")
                                                model.addAttribute("processMonitoringRec", documentList)
                                            }
                                    questionnaire?.frequencyUpload
                                            ?.let { frequencyDoc ->
                                                val documentList: List<String>? = frequencyDoc.split(",")
                                                model.addAttribute("frequencyDocs", documentList)
                                            }
                                    questionnaire?.criticalProcessParametersUpload
                                            ?.let { criticalProcessParametersUploadDoc ->
                                                val documentList: List<String>? = criticalProcessParametersUploadDoc.split(",")
                                                model.addAttribute("criticalProcessParametersUploadDocs", documentList)
                                            }
                                    questionnaire?.operationsUpload
                                            ?.let { operationsUploadDoc ->
                                                val documentList: List<String>? = operationsUploadDoc.split(",")
                                                model.addAttribute("operationsUploadDocs", documentList)
                                            }
                                    questionnaire?.processFlowOfProductionUpload
                                            ?.let { processFlowOfProductionUploadDoc ->
                                                val documentList: List<String>? = processFlowOfProductionUploadDoc.split(",")
                                                model.addAttribute("processFlowOfProductionUploadDocs", documentList)
                                            }
                                    result = "quality-assurance/customer/review-questionnaire"
                                }
                                ?: throw NullValueNotAllowedException("No Permit [id=$id] found")

                    } catch (e: Exception) {
                        KotlinLogging.logger { }.error(e.message,e)
                        redirectAttributes.addFlashAttribute("error", "Something went wrong. Try again!")
                        result = "redirect:/api/permit/permit/renew-get/{id}"
                    }
                }
        return result
    }

    /**
     * update STA 10 form
     */
    @GetMapping("/update/sta10/{id}/{invoiceId}")
    fun updateSta10Qn(model: Model,
                      @PathVariable("id") id: Long,
//                      @ModelAttribute @Valid permit: PermitApplicationEntity,
                      @PathVariable("invoiceId") invoiceId: Long,
                      @RequestParam(value = "appId", required = false) appId: Int?
    ): String {
        questionnaireSta10Repo.findByIdOrNull(id)
                ?.let { questionnaire ->
                    /**
                     * TODO: WHy not return the entire questionarre to the form and evaluate the records there?
                     */
                    model.addAttribute("questionnaire", questionnaire)
                    val pm = permitRepo.findByIdOrNull(questionnaire.permitId)
                    model.addAttribute("invoice", invoiceRepository.findByIdOrNull(invoiceId))
//                    model.addAttribute("permit",pm)
                    model.addAttribute("pId",pm?.id)
//                    model.addAttribute("permitId", pm?.id)
                    model.addAttribute("appId", appId)

                    model.addAttribute("title", "Update STA10")
                    return "quality-assurance/customer/smark/sta10-review"

                }
                ?: throw NullValueNotAllowedException("No Questionnaire [id=$id] found")


    }


    /**
     * update sta3
     */
    @GetMapping("/update/sta3/{id}")
    fun updateSta3Qn(model: Model,
                     @PathVariable("id") id: Long,
                     @ModelAttribute @Valid permit: PermitApplicationEntity,
                     session: HttpSession,
                     @RequestParam("invoiceId", required = false) invoiceId: Long?
    ): String {
        applicationQuestionnaireRepo.findByIdOrNull(id)
                ?.let { questionnaire ->
                    val pm = permitRepo.findByIdOrNull(questionnaire.permitId)

                    model.addAttribute("questionnaire", questionnaire)
                    model.addAttribute("permitId", permitRepo.findByIdOrNull(questionnaire.permitId))
                    model.addAttribute("invoice", invoiceRepository.findByIdOrNull(invoiceId))
                    model.addAttribute("permit", permit)
                    model.addAttribute("qn", ApplicationQuestionnaireEntity())
                    model.addAttribute("title", "Update STA3")
                    model.addAttribute("pId",pm?.id)
                    return "quality-assurance/customer/dmark/update_sta3"
                }
                ?: throw NullValueNotAllowedException("No Questionnaire [id=$id] found")


    }

    @PostMapping("saveNoOfSites")
    fun updateNoOfSites(
            model: Model,
            @RequestParam("id", required = true) id: Long,
            @ModelAttribute("permit") @Valid permit: PermitApplicationEntity
    ): String {

//        permitRepo.findByIdOrNull(permit.id)
//                ?.let { i ->
//                    i.noOfSitesProducingTheBrand = permit.noOfSitesProducingTheBrand
//                    permitRepo.save(i)
//                }
        permitRepo.findByIdOrNull(id)
                ?.let { i ->
                    with (i) {

                        permit.noOfSitesProducingTheBrand?.let { noOfSitesProducingTheBrand = permit.noOfSitesProducingTheBrand }
                        permit.product?.let {  product = permit.product }
                        permit.productSubCategory?.let { productCategory = permit.productSubCategory }
                        permit.broadProductCategory?.let { broadProductCategory = permit.broadProductCategory }
                        permit.productCategory?.let { productCategory = permit.productCategory }
                        permit.siteApplicable?.let { siteApplicable = permit.siteApplicable }
                    }
                    permitRepo.save(i)
                }

        return "redirect:/api/permit/permit/renew-get/${id}"
    }

    @PostMapping("/remark/{id}")
    fun leaveRemarks(
            model: Model,
            @ModelAttribute @Valid remarksEntity: PermitApplicationRemarksEntity,
            redirectAttributes: RedirectAttributes,
            @PathVariable("id") id: Long
    ): String {
        return try {
            serviceMapsRepo.findByIdOrNull(appId)
                    ?.let { map ->
                        KotlinLogging.logger {  }.info { "Service map found with id ${map.id}" }
                        daoServices.extractUserFromContext(SecurityContextHolder.getContext())
                                ?.let { loggedInUser ->
                                    permitRepo.findByIdOrNull(id)
                                            ?.let { foundApplication ->
                                                val sr = controllerDao.createServiceRequest(map)
                                                KotlinLogging.logger {  }.info { "Service Request generated with id ${sr.id}" }
                                                model.addAttribute("app", foundApplication)
                                                var remarks = remarksEntity
                                                KotlinLogging.logger {  }.info { "Generated service req $sr" }
                                                val man = manufacturerRepository.findByIdOrNull(foundApplication.manufacturer)
                                                with(remarks) {
                                                    manufacturerid = man
                                                    /**
                                                     * Using an appId as above makes it configurable
                                                     */
                                                    status = map.initStatus
                                                    createdOn = Timestamp.from(Instant.now())
                                                    createdBy = loggedInUser.userName
                                                    permitId = foundApplication

                                                    notificationsRepo.findByIdOrNull(50)
                                                            ?.let { notification ->
                                                                KotlinLogging.logger {  }.info { "Got the notifications" }
                                                                generateBufferedNotification(notification, map, man?.companyEmail, sr)
                                                                        ?.let { m ->
                                                                            sendToKafkaQueue.submitAsyncRequestToBus(m, map.serviceTopic)
                                                                            KotlinLogging.logger {  }.info { "Req submitted to bus" }
                                                                        }
                                                            }
                                                            ?: throw MissingConfigurationException("Notification for current Scenario is missing, review setup and try again later")
                                                }
                                                remarks = permitRemarksRepository.save(remarks)
                                                KotlinLogging.logger {  }.info { "Remark saved with id ${remarks.id}" }

                                            }

                                }
                    }

            "redirect:/api/permit/my-applications/{id}"
        } catch (e: Exception) {
            KotlinLogging.logger { }.error { "Caught an error, $e" }
            redirectAttributes.addFlashAttribute("message", "Couldn't save your comment. Try again later")
            "redirect:/api/permit/my-applications/{id}"
        }
    }

    @GetMapping("/accept/{id}/scheme")
    fun acceptScheme(
            model: Model,
            @PathVariable("id") id: Long,
            /**
             * TODO: May work better as an int, review
             * Done
             */
            @RequestParam(value = "acceptanceStatus") acceptanceStatus: Int?
    ): String {
        permitRepo.findByIdOrNull(id)
                ?.let { permit ->
                    with(permit) {
                        schemeOfSupervisionAcceptanceStatus = acceptanceStatus
                        modifiedOn = Timestamp.from(Instant.now())
                        modifiedBy = daoServices.extractUserFromContext(SecurityContextHolder.getContext())?.userName
                    }
                    permitRepo.save(permit)
                    KotlinLogging.logger { }.info { "Permit application updated on ${permit.modifiedOn}" }

                    /**
                     * Accept/ reject scheme of supervision
                     */
                    var accepted: Boolean = false
                    when(acceptanceStatus) {
                        1 -> {
                            accepted = true
                        }
                        0 -> {
                            accepted = false
                        }
                    }
                    permit.id?.let { qualityAssuranceBpmn.qaSfMIAcceptSupervisionScheme(it, qaoId, accepted) }

                }

        return "redirect:/api/permit/my-applications/{id}"
    }

    fun generateBufferedNotification(notification: NotificationsEntity, map: ServiceMapsEntity, email: String?, data: Any?, sr: ServiceRequestsEntity? = null): NotificationsBufferEntity? {
//        val buffers = NotificationsBufferEntity()

//        notifications.forEach { notification ->
            var buffer = NotificationsBufferEntity()
            with(buffer) {
                messageBody = composeMessage(notification)
                subject = notification.subject
                serviceRequestId = sr?.id
                transactionReference = sr?.transactionReference
                sender = notification.sender
                this.recipient = email
                status = map.initStatus
                createdOn = Timestamp.from(Instant.now())
                createdBy = sr?.transactionReference
                notificationId = notification.id
                actorClassName = notification.actorClass
            }
            buffer = bufferRepo.save(buffer)
            KotlinLogging.logger {  }.info { "Buffer saved with id ${buffer.id}" }


        return buffer

    }

    private fun composeMessage(notification: NotificationsEntity): String? {
        return notification.description

    }
}
