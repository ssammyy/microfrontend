package org.kebs.app.kotlin.apollo.api.controllers.qaControllers

//import org.kebs.app.kotlin.apollo.api.service.invoiceGen
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.export.ExportFile
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.objects.TemplateExportVariables
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.QualityAssuranceBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.QualityAssuranceDaoServices
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.repo.*
import org.kebs.app.kotlin.apollo.store.repo.qa.IPermitTypesEntityRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.sql.Timestamp
import java.time.Instant
import java.util.*
import javax.validation.Valid


@Controller
@RequestMapping("/questionnaire/")
class QuestionnairesController(
    private val applicationQuestionnaireRepo: IApplicationQuestionnaireRepository,
    private val invoiceRepository: IInvoiceRepository,
    private val questionnaireSta10Repo: IQuestionnaireSta10Repository,
    private val sampleStandardsRepository: ISampleStandardsRepository,
    private val productsRepo: IProductsRepository,
    private val permitTypesRepo: IPermitTypesEntityRepository,
    private val productSubCategoryRepo: IProductSubcategoryRepository,
    private val dmarkForeignApplicationsRepository: IDmarkForeignApplicationsRepository,
    applicationMapProperties: ApplicationMapProperties,
    private val exportFile: ExportFile,
    private val notifications: Notifications,
    private val paymentMethodsRepository: IPaymentMethodsRepository,
    private val permitApplicationsRepository: IPermitRepository,
    private val daoServices: QualityAssuranceDaoServices,
    private val qualityAssuranceBpmn: QualityAssuranceBpmn,
    private val standardsCategoryRepository: IStandardCategoryRepository,
    private val iManufacturerRepository: IManufacturerRepository
) {

//    var uploadDirectory = System.getProperty("user.dir") + "/uploads"
    val appId: Int? = applicationMapProperties.mapPermitQuestionnaire
    val uploadDir = applicationMapProperties.localFilePath

    val assigneeId = applicationMapProperties.assigneeId

    @Value("\${bpmn.qa.sf.application.payment.process.definition.key}")
    lateinit var qaSfApplicationPaymentProcessDefinitionKey: String
//    val rootLocation: Path = Paths.get(uploadDirectory)

    /**
     * save sta 10  :FMARK qn
     */
    @PostMapping("sta10")
    fun saveSta10(model: Model,
                  @ModelAttribute @Valid questionnaireSta10Entity: QuestionnaireSta10Entity,
                  @ModelAttribute("invoice") @Valid invoiceEntity: InvoiceEntity,
                  @RequestParam("generatedPermitId", required = false) generatedPermitId: Long,
                  @RequestParam("certFile", required = false) certFile: Array<MultipartFile>?,
                  @RequestParam("testFile", required = false) testFile: Array<MultipartFile>?,
                  @RequestParam("processMonitRecords", required = false) processMonitRecords: Array<MultipartFile>?,
                  @RequestParam("frequencyFile", required = false) frequencyFile: Array<MultipartFile>?,
                  @RequestParam("criticalProcessParamentersFile", required = false) criticalProcessParamentersFile: Array<MultipartFile>?,
                  @RequestParam("operationsFile", required = false) operationsFile: Array<MultipartFile>?,
                  @RequestParam("processFlowOfProductionFile", required = false) processFlowOfProductionFile: Array<MultipartFile>?,
                  @RequestParam(value = "appId", required = false) appId: Int?,
                  bindingResult: BindingResult,
                  redirectAttributes: RedirectAttributes
    ): String {
        return try {
                certFile?.let { daoServices.storeFiles(it) }
                testFile?.let { daoServices.storeFiles(it) }
                processMonitRecords?.let { daoServices.storeFiles(it) }
                frequencyFile?.let { daoServices.storeFiles(it) }
                criticalProcessParamentersFile?.let { daoServices.storeFiles(it) }
                operationsFile?.let { daoServices.storeFiles(it) }
                processFlowOfProductionFile?.let { daoServices.storeFiles(it) }

            var questionnaire =  questionnaireSta10Entity
            with(questionnaire){
                permitApplicationsRepository.findByIdOrNull(generatedPermitId)
                                        .let { mypermit->
                                            permitId = mypermit?.id
                                            status = 0
                                            createdBy = "admin"
                                            createdOn = Timestamp.from(Instant.now())

                                            if (certFile != null) {
                                                if(certFile.isNotEmpty()) {
                                                    val fileNames13 = StringBuilder()
                                                    certFile.forEach { file->
                                                        fileNames13.append(file.originalFilename + ",")
                                                        val documentList: List<String>? = fileNames13.split(",")
                                                        val newDocumentList = documentList?.dropLast(1)
                                                        certificateFile = newDocumentList.toString().replace("[", "").replace("]", "")
                                                    }
                                                }
                                            }
                                            if (testFile != null) {
                                                if(testFile.isNotEmpty()) {
                                                    val fileNames12 = StringBuilder()
                                                    testFile.forEach { file->
                                                        fileNames12.append(file.originalFilename + ",")
                                                        val documentList: List<String>? = fileNames12.split(",")
                                                        val newDocumentList = documentList?.dropLast(1)
                                                        testingRecordFile = newDocumentList.toString().replace("[", "").replace("]", "")
                                                    }
                                                }
                                            }
                                            if (processMonitRecords != null) {
                                                if(processMonitRecords.isNotEmpty()) {
                                                    val fileNames0 = StringBuilder()
                                                    processMonitRecords.forEach { file->
                                                        fileNames0.append(file.originalFilename + ",")
                                                        val documentList: List<String>? = fileNames0.split(",")
                                                        val newDocumentList = documentList?.dropLast(1)
                                                        processMonitoringRecordsFile = newDocumentList.toString().replace("[", "").replace("]", "")
                                                    }
                                                }
                                            }

                                            if (frequencyFile != null) {
                                                if(frequencyFile.isNotEmpty()) {
                                                    val fileNames19 = StringBuilder()
                                                    frequencyFile.forEach { file->
                                                        fileNames19.append(file.originalFilename + ",")
                                                        val documentList: List<String>? = fileNames19.split(",")
                                                        val newDocumentList = documentList?.dropLast(1)
                                                        frequencyUpload = newDocumentList.toString().replace("[", "").replace("]", "")
                                                    }
                                                }
                                            }
                                            if (criticalProcessParamentersFile != null) {
                                                if(criticalProcessParamentersFile.isNotEmpty()) {
                                                    criticalProcessParamentersFile.forEach { file ->
                                                        val fileNames10 = StringBuilder()
                                                        fileNames10.append(file.originalFilename + ",")
                                                        val documentList: List<String>? = fileNames10.split(",")
                                                        val newDocumentList = documentList?.dropLast(1)
                                                        criticalProcessParametersUpload = newDocumentList.toString()
                                                    }
                                                }
                                            }
                                            if (operationsFile != null) {
                                                if (operationsFile.isNotEmpty()) {
                                                    val fileNames01 = StringBuilder()
                                                    operationsFile.forEach { file->
                                                        fileNames01.append(file.originalFilename + ",")
                                                        val documentList: List<String>? = fileNames01.split(",")
                                                        val newDocumentList = documentList?.dropLast(1)
                                                        operationsUpload = newDocumentList.toString().replace("[", "").replace("]", "")
                                                    }
                                                }
                                            }
                                            if (processFlowOfProductionFile != null) {
                                                if(processFlowOfProductionFile.isNotEmpty()) {
                                                    val fileNames91 = StringBuilder()
                                                    processFlowOfProductionFile.forEach { file ->
                                                        fileNames91.append(file.originalFilename + ",")
                                                        val documentList: List<String>? = fileNames91.split(",")
                                                        val newDocumentList = documentList?.dropLast(1)
                                                        processFlowOfProductionUpload = newDocumentList.toString().replace("[", "").replace("]", "")
                                                    }

                                                }
                                            }
                                            questionnaire = questionnaireSta10Repo.save(questionnaire)
                                        }
            }
            val pm = permitApplicationsRepository.findByIdOrNull(questionnaire.permitId)
            model.addAttribute("productCategories", standardsCategoryRepository.findAll())
            if (pm?.foreignApplication != 1) {
                val inv = pm?.let { daoServices.invoiceGen(invoiceEntity, it, null, daoServices.extractManufacturerFromUser(daoServices.extractUserFromContext(SecurityContextHolder.getContext())), appId) }
                model.addAttribute("invoice", inv)
                var invoiceFilepath = uploadDir + "invoice-" + inv?.id.toString() + ".pdf"
                var lstTemplateVariables = mutableListOf<TemplateExportVariables>()
                iManufacturerRepository.findByIdOrNull(inv?.manufacturer).let{ manufacturersEntity ->
                    lstTemplateVariables.add(TemplateExportVariables("invoice",inv as Any))
                    lstTemplateVariables.add(TemplateExportVariables("permit",pm as Any))
                    lstTemplateVariables.add(TemplateExportVariables("manufacturer",manufacturersEntity as Any))

                    exportFile.parseThymeleafTemplate("templates/TestPdf/invoice_pdf",lstTemplateVariables.toList())?.let { htmlString->
                        exportFile.generatePdfFromHtml(htmlString,invoiceFilepath)
                        pm?.userId?.email?.let {
                            notifications.processEmail(it,"INVOICE","Dear, ${pm.userId?.firstName} ${pm.userId?.firstName}\n" +
                                    "A proforma invoice has been generated for your factory.\n" +
                                    "Total amount, KSH ${inv?.amount}\n" +
                                    "Payment instructions\n" +
                                    "Paybill: 1234, account no: 4321 \n" +
                                    "Regards, \n" +
                                    "The KIMS team",invoiceFilepath)
                        }
                    }
                }

                pm?.id?.let {
                    qualityAssuranceBpmn.startQASFApplicationPaymentProcess(it, assigneeId)?.let {
                        pm.id?.let { it1 ->
                            qualityAssuranceBpmn.fetchTaskByPermitId(it1, qaSfApplicationPaymentProcessDefinitionKey)?.let { taskDetails ->
                                println("Task details after starting the process")
                                for (taskDetail in taskDetails){
                                    taskDetail.task.let{ task->
                                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                                    }
                                }
                            }
                        }
                    }
                }
            }

//            Start QA application payment process

            model.addAttribute("questionnaire", questionnaire)
            model.addAttribute("permit", pm)
            model.addAttribute("permitType", permitTypesRepo.findByIdOrNull(pm?.permitType))
//            productSubCategoryRepo
            model.addAttribute("productSubCategory",productSubCategoryRepo.findByIdOrNull(pm?.productSubCategory))
            model.addAttribute("product", productsRepo.findByIdOrNull(pm?.product))
            model.addAttribute("std", pm?.productSubCategory?.let { sampleStandardsRepository.findBySubCategoryId(it) })
            // Extra docs
            pm?.extraDocuments
                    ?.let { documents ->
                        val documentList: List<String>? = documents.split(",")
//                        val newDocumentList = documentList?.dropLast(1)
                        model.addAttribute("documents", documentList)
                    }
            questionnaire.certificateFile
                    ?.let { certDoc ->
                    val documentList: List<String>? = certDoc.split(",")
//                    val newDocumentList = documentList?.dropLast(1)
                    model.addAttribute("certDocs", documentList)
            }

            questionnaire.testingRecordFile
                    ?.let { testingRecordDoc ->
                        val documentList: List<String>? = testingRecordDoc.split(",")
//                        val newDocumentList = documentList?.dropLast(1)
                        model.addAttribute("testingRecordDocs", documentList)
                    }

            questionnaire.processMonitoringRecordsFile
                    ?.let { testingRecordDoc ->
                        val documentList: List<String>? = testingRecordDoc.split(",")
//                        val newDocumentList = documentList?.dropLast(1)
                        model.addAttribute("testingRecordDocs", documentList)
                    }

            questionnaire.frequencyUpload
                    ?.let { testingRecordDoc ->
                        val documentList: List<String>? = testingRecordDoc.split(",")
//                        val newDocumentList = documentList?.dropLast(1)
                        model.addAttribute("testingRecordDocs", documentList)
                    }

            questionnaire.criticalProcessParametersUpload
                    ?.let { criticalProcessParametersUploadDoc ->
                        val documentList: List<String>? = criticalProcessParametersUploadDoc.split(",")
//                        val newDocumentList = documentList?.dropLast(1)
                        model.addAttribute("criticalProcessParametersUploadDocs", documentList)
                    }

            questionnaire.operationsUpload
                    ?.let { operationsUploadDoc ->
                        val documentList: List<String>? = operationsUploadDoc.split(",")
//                        val newDocumentList = documentList?.dropLast(1)
                        model.addAttribute("operationsUploadDocs", documentList)
                    }

            questionnaire.processFlowOfProductionUpload
                    ?.let { processFlowOfProductionUploadDoc ->
                        val documentList: List<String>? = processFlowOfProductionUploadDoc.split(",")
//                        val newDocumentList = documentList?.dropLast(1)
                        model.addAttribute("processFlowOfProductionUploadDocs", documentList)
                    }






//            KotlinLogging.logger {  }.info { permitApplicationsRepository.findByIdOrNull(questionnaireSta10Repo.findByPermitId(questionnaire.permitId)?.id) }
            model.addAttribute("title", "Review STA10")
            "quality-assurance/customer/review-questionnaire"
        } catch (e: Exception){
            KotlinLogging.logger {  }.info { e }
            e.printStackTrace()
            model.addAttribute("message", "Sorry, we couldn't process your request! Try again")
            "quality-assurance/customer/fmark/sta10"
        }
    }

    @PostMapping("sta10/{id}/{invoiceId}")
    fun updateSta10(model: Model,
                    @PathVariable("id") id: Long,
                    @ModelAttribute("questionnaire") @Valid questionnaireSta10Entity: QuestionnaireSta10Entity,
                    @PathVariable("invoiceId") invoiceId: Long
    ): String {
        return try {
            val q = questionnaireSta10Repo.save(questionnaireSta10Entity).id
            questionnaireSta10Repo.findByIdOrNull(q)
                    ?.let { m ->
                        val pm = permitApplicationsRepository.findByIdOrNull(m.permitId)
                        invoiceRepository.findByIdOrNull(invoiceId)
                                ?.let { inv->
                                    model.addAttribute("invoice", inv)
                                    model.addAttribute("permit", pm)
                                    model.addAttribute("std", pm?.productSubCategory?.let { sampleStandardsRepository.findBySubCategoryId(it) })
                                    KotlinLogging.logger {  }.info { permitApplicationsRepository.findByIdOrNull(m.permitId)?.description }
                                    model.addAttribute("questionnaire", m)
                                    model.addAttribute("std", pm?.productSubCategory?.let { sampleStandardsRepository.findBySubCategoryId(it) })

                                    model.addAttribute("permitType", permitTypesRepo.findByIdOrNull(pm?.permitType))

                                    model.addAttribute("productSubCategory",productSubCategoryRepo.findByIdOrNull(pm?.productSubCategory))
                                    model.addAttribute("product", productsRepo.findByIdOrNull(pm?.product))
                                    model.addAttribute("permit", permitApplicationsRepository.findByIdOrNull(pm?.id))

                                    model.addAttribute("productCategories", standardsCategoryRepository.findAll())
//                                    model.addAttribute("std", pm?.productSubCategory?.let { sampleStandardsRepository.findBySubCategoryId(it) })
//                                    model.addAttribute("product", productsRepo.findByIdOrNull(pm?.product))
//                                    model.addAttribute("productSubCategory",productSubCategoryRepo.findByIdOrNull(pm?.productSubCategory))

                                    pm?.extraDocuments
                                            ?.let { documents ->
                                                val documentList: List<String>? = documents.split(",")
//                        val newDocumentList = documentList?.dropLast(1)
                                                model.addAttribute("documents", documentList)
                                            }
                                    m.certificateFile
                                            ?.let { certDoc ->
                                                val documentList: List<String>? = certDoc.split(",")
//                    val newDocumentList = documentList?.dropLast(1)
                                                model.addAttribute("certDocs", documentList)
                                            }

                                    m.testingRecordFile
                                            ?.let { testingRecordDoc ->
                                                val documentList: List<String>? = testingRecordDoc.split(",")
//                        val newDocumentList = documentList?.dropLast(1)
                                                model.addAttribute("testingRecordDocs", documentList)
                                            }
                                    m.processMonitoringRecordsFile
                                            ?.let { processMonitRecordDoc ->
                                                val documentList: List<String>? = processMonitRecordDoc.split(",")
//                        val newDocumentList = documentList?.dropLast(1)
                                                model.addAttribute("processMonitoringRec", documentList)
                                            }
                                    m.frequencyUpload
                                            ?.let { testingRecordDoc ->
                                                val documentList: List<String>? = testingRecordDoc.split(",")
//                        val newDocumentList = documentList?.dropLast(1)
                                                model.addAttribute("testingRecordDocs", documentList)
                                            }
                                    m.criticalProcessParametersUpload
                                            ?.let { criticalProcessParametersUploadDoc ->
                                                val documentList: List<String>? = criticalProcessParametersUploadDoc.split(",")
//                        val newDocumentList = documentList?.dropLast(1)
                                                model.addAttribute("criticalProcessParametersUploadDocs", documentList)
                                            }
                                    m.operationsUpload
                                            ?.let { operationsUploadDoc ->
                                                val documentList: List<String>? = operationsUploadDoc.split(",")
//                        val newDocumentList = documentList?.dropLast(1)
                                                model.addAttribute("operationsUploadDocs", documentList)
                                            }
                                    m.processFlowOfProductionUpload
                                            ?.let { processFlowOfProductionUploadDoc ->
                                                val documentList: List<String>? = processFlowOfProductionUploadDoc.split(",")
//                        val newDocumentList = documentList?.dropLast(1)
                                                model.addAttribute("processFlowOfProductionUploadDocs", documentList)
                                            }
                                }

                    }

            "quality-assurance/customer/review-questionnaire"
        } catch (e: Exception){
            model.addAttribute("message", "Something went wrong. Try again")
            "redirect:/api/v1/permit/update/sta10/{id}/{invoiceId}"

        }
    }


    /**
     * sta3 update
     */
//    @RequestMapping("sta3/{id}/{invoiceId}", method = [RequestMethod.POST])
    @RequestMapping("sta3/{id}", method = [RequestMethod.POST])
    fun updateSta3(model: Model, @PathVariable("id") id: Long,
                   @RequestParam("invoiceId", required = false) invoiceId: Long?,
                   @ModelAttribute @Valid questionnaireEntity: ApplicationQuestionnaireEntity,
                   @ModelAttribute("permit") @Valid permits:PermitApplicationEntity,
                   redirectAttributes: RedirectAttributes
    ): String{
        return try {
            val questionnaire = applicationQuestionnaireRepo.save(questionnaireEntity).id
            applicationQuestionnaireRepo.findByIdOrNull(questionnaire)
                    ?.let { m ->
                        val pm = permitApplicationsRepository.findByIdOrNull(m.permitId)
                        when(pm?.foreignApplication) {
                            0 -> {
                            KotlinLogging.logger {  }.info { "domestic application" }
                            invoiceRepository.findByIdOrNull(invoiceId)
                                    ?.let { inv ->
                                        model.addAttribute("invoice", inv)
                                    }
                            }
                            1 -> {
                                KotlinLogging.logger {  }.info { "Foreign application" }
                            }
                        }

                        model.addAttribute("permit", permitApplicationsRepository.findByIdOrNull(m.permitId))
                        model.addAttribute("std", pm?.productSubCategory?.let { sampleStandardsRepository.findBySubCategoryId(it) })
                        model.addAttribute("questionnaire", m)
                        model.addAttribute("std", pm?.productSubCategory?.let { sampleStandardsRepository.findBySubCategoryId(it) })

                        model.addAttribute("product", productsRepo.findByIdOrNull(pm?.product))
                        model.addAttribute("productSubCategory",productSubCategoryRepo.findByIdOrNull(pm?.productSubCategory))

                        model.addAttribute("permitType", permitTypesRepo.findByIdOrNull(pm?.permitType)?.mark)

                        model.addAttribute("productCategories", standardsCategoryRepository.findAll())

                        pm?.labReportsFilepath1
                                ?.let { labDocs ->
                                    val labDocList: List<String>? = labDocs?.split(",")
                                    model.addAttribute("labDocs", labDocList)
                                }
                        pm?.extraDocuments
                                ?.let { documents ->
                                    val documentList: List<String>? = documents.split(",")
                                    model.addAttribute("documents", documentList)
                                }
                        m.illustrationFile
                                ?.let { illustration ->
                                    val documentList: List<String>? = illustration.split(",")
                                    model.addAttribute("illustrations", documentList)
                                }
                        m.manufacturingStepsFile
                                ?.let { manufacturingSteps ->
                                    val documentList: List<String>? = manufacturingSteps.split(",")
                                    model.addAttribute("manufacturingStepsFiles", documentList)
                                }
                        m.testsAgainstTheStandardFile
                                ?.let { testsAgainstTheStandard ->
                                    val documentList: List<String>? = testsAgainstTheStandard.split(",")
                                    model.addAttribute("testsAgainstTheStandardFiles", documentList)
                                }
                        m.qualityManualFile
                                ?.let { qualityManualFile ->
                                    val documentList: List<String>? = qualityManualFile.split(",")
                                    model.addAttribute("qualityManualFiles", documentList)
                                }
                        m.testReportsLevelOfDefectivesFile
                                ?.let { testReportsLevelOfDefectives ->
                                    val documentList: List<String>? = testReportsLevelOfDefectives.split(",")
                                    model.addAttribute("testReportsLevelOfDefectivesFiles", documentList)
                                }
                        }
//                "redirect:/dmark/renew-dmark/${id}?appId=150"
            "quality-assurance/customer/dmark/review-sta3-questionnaire"
        } catch (e: Exception){
            redirectAttributes.addFlashAttribute("error", "Something went wrong")
            "quality-assurance/customer/dmark/review-sta3-questionnaire"
//            "redirect:/dmark/renew-dmark/${id}"
        }
    }

    /**
     * DMARK Domestic
     */
    @GetMapping("sta3_domestic")
    fun sta3QnDomestic(model: Model): String {
        model.addAttribute("qn", ApplicationQuestionnaireEntity())
        return "quality-assurance/customer/dmark/sta3_domestic"
    }

    /**
     * save sta3
     */
    @PostMapping("sta3_domestic")
    fun saveSta3Domestic(model: Model,
                         @ModelAttribute("questionnaire") @Valid applicationQuestionnaireEntity: ApplicationQuestionnaireEntity,
                         @ModelAttribute("invoice") @Valid invoiceEntity: InvoiceEntity,
                         @ModelAttribute("permit") @Valid permits: PermitApplicationEntity,
                         @RequestParam("generatedPermitId", required = false) generatedPermitId: Long,
                         @RequestParam("manufacturingStepss") manufacturingStepss: Array<MultipartFile>?,
                         @RequestParam("qualityManualAttachment") qualityManualAttachment: Array<MultipartFile>?,
                         @RequestParam("testReportsLevelOfDefectives") testReportsLevelOfDefectives: Array<MultipartFile>?,
                         @RequestParam("testsAgainstTheStandard") testsAgainstTheStandard: Array<MultipartFile>?,
                         @RequestParam("illustration") illustration: Array<MultipartFile>?,
                         @RequestParam(value = "appId", required = false) appId: Int?
    ): String {
        return try {


            if(manufacturingStepss?.isEmpty()!! || qualityManualAttachment?.isEmpty()!! || testReportsLevelOfDefectives?.isEmpty()!! || testsAgainstTheStandard?.isEmpty()!! || illustration?.isEmpty()!!) {
                KotlinLogging.logger {  }.info { "Either one or more files is missing" }
            }
            else {
                manufacturingStepss.let { daoServices.storeFiles(it) }
                qualityManualAttachment.let { daoServices.storeFiles(it) }
                testReportsLevelOfDefectives.let { daoServices.storeFiles(it) }
                testsAgainstTheStandard.let { daoServices.storeFiles(it) }
                illustration.let { daoServices.storeFiles(it) }
            }

            var questionnaire =  applicationQuestionnaireEntity
            with(questionnaire) {
                permitApplicationsRepository.findByIdOrNull(generatedPermitId)
                        .let {mypermit->
                            permitId = mypermit?.id
                            status = 0
                            createdBy = "admin"
                            createdOn = Timestamp.from(Instant.now())
//                            illustrationFile = illustration?.originalFilename
                            if (illustration != null) {
                                if (illustration.isNotEmpty()) {
                                    val fileNames1 = StringBuilder()
                                    if (illustration != null) {
                                        illustration.forEach { file ->
                                            fileNames1.append(file.originalFilename + ",")
                                            val documentList: List<String>? = fileNames1.split(",")
                                            val newDocumentList = documentList?.dropLast(1)
                                            illustrationFile = newDocumentList.toString().replace("[", "").replace("]", "")
                                        }
                                    }
                                }
                            }
                            if (!manufacturingStepss.isNotEmpty()) {
                                val fileNames1 = StringBuilder()
                                manufacturingStepss.forEach { file ->
                                    fileNames1.append(file.originalFilename + ",")
                                    val documentList: List<String>? = fileNames1.split(",")
                                    val newDocumentList = documentList?.dropLast(1)
                                    manufacturingStepsFile = newDocumentList.toString().replace("[", "").replace("]", "")
                                }
                            }
                            if (testsAgainstTheStandard != null) {
                                if (testsAgainstTheStandard.isNotEmpty()) {
                                    val fileNames1 = StringBuilder()
                                    if (testsAgainstTheStandard != null) {
                                        testsAgainstTheStandard.forEach { file ->
                                            fileNames1.append(file.originalFilename + ",")
                                            val documentList: List<String>? = fileNames1.split(",")
                                            val newDocumentList = documentList?.dropLast(1)
                                            testsAgainstTheStandardFile = newDocumentList.toString().replace("[", "").replace("]", "")
                                        }
                                    }
                                }
                            }
                            if (qualityManualAttachment != null) {
                                if (!qualityManualAttachment.isEmpty()) {
                                    val fileNames1 = StringBuilder()
                                    if (qualityManualAttachment != null) {
                                        qualityManualAttachment.forEach { file ->
                                            fileNames1.append(file.originalFilename + ",")
                                            val documentList: List<String>? = fileNames1.split(",")
                                            val newDocumentList = documentList?.dropLast(1)
                                            qualityManualFile = newDocumentList.toString().replace("[", "").replace("]", "")
                                        }
                                    }
                                }
                            }
                            if (testReportsLevelOfDefectives != null) {
                                if (testReportsLevelOfDefectives.isNotEmpty()) {
                                    val fileNames1 = StringBuilder()
                                    if (testReportsLevelOfDefectives != null) {
                                        testReportsLevelOfDefectives.forEach { file ->
                                            fileNames1.append(file.originalFilename + ",")
                                            val documentList: List<String>? = fileNames1.split(",")
                                            val newDocumentList = documentList?.dropLast(1)
                                            testReportsLevelOfDefectivesFile = newDocumentList.toString().replace("[", "").replace("]", "")
                                        }
                                    }
                                }
                            }

                        }
            }
            questionnaire = applicationQuestionnaireRepo.save(questionnaire)
            KotlinLogging.logger { }.trace("${this::saveSta3Domestic.name} saved with id =[${questionnaire.id}] ")
            val pm = permitApplicationsRepository.findByIdOrNull(questionnaire.permitId)
            KotlinLogging.logger { }.info { pm?.id }
//            val inv = pm?.let { daoServices.invoiceGen(invoiceEntity, it) }

            when(pm?.foreignApplication) {
                0-> {
                    val inv = pm.let { daoServices.invoiceGen(invoiceEntity, it, null, daoServices.extractManufacturerFromUser(daoServices.extractUserFromContext(SecurityContextHolder.getContext())), appId) }
                    model.addAttribute("invoice", inv)
                    KotlinLogging.logger {  }.info { "Domestic application requires an invoice at this point." }
                }
                1 -> {
                    KotlinLogging.logger {  }.info { "Its a foreign application" }
                }
            }

            model.addAttribute("paymentMethods", paymentMethodsRepository.findAll())

            model.addAttribute("questionnaire", questionnaire)
            model.addAttribute("permitType", permitTypesRepo.findByIdOrNull(pm?.permitType))
            model.addAttribute("productSubCategory",productSubCategoryRepo.findByIdOrNull(pm?.productSubCategory))
            model.addAttribute("product", productsRepo.findByIdOrNull(pm?.product))
            model.addAttribute("permit", permitApplicationsRepository.findByIdOrNull(pm?.id))
            model.addAttribute("std", pm?.productSubCategory?.let { sampleStandardsRepository.findBySubCategoryId(it) })
            model.addAttribute("product", productsRepo.findByIdOrNull(pm?.product))
            model.addAttribute("productSubCategory",productSubCategoryRepo.findByIdOrNull(pm?.productSubCategory))

            model.addAttribute("productCategories", standardsCategoryRepository.findAll())

            model.addAttribute("permitType", permitTypesRepo.findByIdOrNull(pm?.permitType)?.typeName)

            pm?.labReportsFilepath1
                    ?.let { labDocs ->
                        val labDocList: List<String>? = labDocs?.split(",")
//                        val newLabDocsList = labDocList?.dropLast(1)
                        model.addAttribute("labDocs", labDocList)
                    }


            pm?.extraDocuments
                    ?.let { documents ->
                        val documentList: List<String>? = documents?.split(",")
//                        val newDocumentList = documentList?.dropLast(1)
                        model.addAttribute("documents", documentList)
                    }


            questionnaire.illustrationFile
                    ?.let { illustration ->
                        val documentList: List<String>? = illustration.split(",")
//                        val newDocumentList = documentList?.dropLast(1)
                        model.addAttribute("illustrations", documentList)
                    }
            questionnaire.manufacturingStepsFile
                    ?.let { manufacturingSteps ->
                        val documentList: List<String>? = manufacturingSteps.split(",")
//                        val newDocumentList = documentList?.dropLast(1)
                        model.addAttribute("manufacturingStepsFiles", documentList)
                    }
            questionnaire.testsAgainstTheStandardFile
                    ?.let { testsAgainstTheStandard ->
                        val documentList: List<String>? = testsAgainstTheStandard.split(",")
//                        val newDocumentList = documentList?.dropLast(1)
                        model.addAttribute("testsAgainstTheStandardFiles", documentList)
                    }
            questionnaire.qualityManualFile
                    ?.let { qualityManualFile ->
                        val documentList: List<String>? = qualityManualFile.split(",")
//                        val newDocumentList = documentList?.dropLast(1)
                        model.addAttribute("qualityManualFiles", documentList)
                    }
            questionnaire.testReportsLevelOfDefectivesFile
                    ?.let { testReportsLevelOfDefectives ->
                        val documentList: List<String>? = testReportsLevelOfDefectives.split(",")
//                        val newDocumentList = documentList?.dropLast(1)
                        model.addAttribute("testReportsLevelOfDefectivesFiles", documentList)
                    }

            "quality-assurance/customer/dmark/review-sta3-questionnaire"
        } catch (e: Exception){
            KotlinLogging.logger {  }.error { e.message }
            KotlinLogging.logger {  }.error { e }
            model.addAttribute("error", "Could not process your request. Try again!")
            "redirect:/questionnaire/sta3_domestic"
        }
    }


}


