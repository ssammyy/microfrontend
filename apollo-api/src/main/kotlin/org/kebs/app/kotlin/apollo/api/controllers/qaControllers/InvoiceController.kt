package org.kebs.app.kotlin.apollo.api.controllers.qaControllers

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.export.ExportFile
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.objects.DisplayInvoiceDetails
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.QualityAssuranceBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.QualityAssuranceDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.mpesa.MPesaService
//import org.kebs.app.kotlin.apollo.api.ports.provided.mpesa.MpesaToken
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.common.exceptions.ServiceMapNotFoundException
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.repo.*
import org.kebs.app.kotlin.apollo.store.repo.qa.IPermitTypesEntityRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.math.BigDecimal
import java.sql.Timestamp
import java.time.Instant
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession


@Controller
class InvoiceController(
    private val usersRepository: IUserRepository,
    private val manufacturerContactsRepo: IManufacturerContactsRepository,
    private val manufacturerRepo: IManufacturerRepository,
    private val permitRepository: IPermitRepository,
    private val serviceMapsRepo: IServiceMapsRepository,
    private val invoiceRepository: IInvoiceRepository,
    private val productsRepo: IProductsRepository,
    private val daoServices: QualityAssuranceDaoServices,
    private val permitTypesRepo: IPermitTypesEntityRepository,
    private val paymentMethodsRepository: IPaymentMethodsRepository,
    private val qualityAssuranceBpmn: QualityAssuranceBpmn,
    private val mpesaService: MPesaService,
    //private val mpesaToken: MpesaToken,
    private val exportFile: ExportFile,
    private val notifications: Notifications,
    private val mpesaTransactionRepo: IMpesaTransactionsRepository,
    private val petroleumInstallationInspectionRepo: IPetroleumInstallationInspectionRepository,
    applicationMapProperties: ApplicationMapProperties
) {

    var globalAppId: Int? = 150
    val uploadDir = applicationMapProperties.localFilePath

    @Value("\${bpmn.qa.sf.application.payment.process.definition.key}")
    lateinit var qaSfApplicationPaymentProcessDefinitionKey: String

    @Value("\${mpesa.transaction.token}")
    lateinit var mtoken: String

    val hofId = applicationMapProperties.hofId

    val invoiceSource = "QA"

    @GetMapping("/all-invoices")
    fun getAllInvoices(
            model: Model,
            @RequestParam(value = "page", required = false) page: Int?,
            @RequestParam(value = "records", required = false) records: Int?,
            @RequestParam(value = "appId", required = false) appId: Int?

    ): String =
            serviceMapsRepo.findByIdOrNull(appId)
                    ?.let { _ ->
                        SecurityContextHolder.getContext().authentication
                                ?.let { authentication ->
                                    usersRepository.findByUserName(authentication.name)
                                            ?.let { user ->
                                                manufacturerRepo.findByUserId(user)
                                                        ?.let { manufacturer ->
                                                            /*
                                                            val pageable = PageRequest.of(page ?: 1, records ?: 10)
                                                             */

                                                            val permitIds = mutableListOf<Long>()
                                                            var lstDisplayInvoiceDetails = mutableListOf<DisplayInvoiceDetails>()
                                                            val lstInvoices = invoiceRepository.findAllByManufacturer(manufacturer.id)
                                                            lstInvoices?.let{ invoices->
                                                                for (invoice in invoices){
                                                                    invoice.permitId?.let{
                                                                        permitIds.add(it)
                                                                    }
                                                                }
                                                                var permits = permitRepository.findByIdIsIn(permitIds)
                                                                for (invoice in invoices){
                                                                    var displayInvoiceDetails = DisplayInvoiceDetails()
                                                                    var permit = PermitApplicationEntity()

                                                                    invoice.permitId?.let{
                                                                        permits.find { permit -> it == permit.id }?.let{ permitApplicationEntity ->
                                                                            permit = permitApplicationEntity
                                                                        }
                                                                    }

                                                                    invoice.id?.let{displayInvoiceDetails.id = it}
                                                                    permit.expiryDate?.let{displayInvoiceDetails.expiryDate = it}
                                                                    permit.permitType?.let{displayInvoiceDetails.permitType = it.toString()}
                                                                    invoice.status?.let{displayInvoiceDetails.status = it}
                                                                    permit.product?.let{displayInvoiceDetails.product = it}
                                                                    permit.tradeMark?.let{displayInvoiceDetails.tradeMark = it}
                                                                    invoice.createdOn?.let{displayInvoiceDetails.createdOn = it}
                                                                    lstDisplayInvoiceDetails.add(displayInvoiceDetails)
                                                                }
                                                            }

                                                            //model.addAttribute("invoices", invoiceRepository.findAllByManufacturer(manufacturer, pageable))
                                                            //model.addAttribute("invoices", invoiceRepository.findAllByManufacturer(manufacturer.id))
                                                            model.addAttribute("invoices", lstDisplayInvoiceDetails)
                                                            return "quality-assurance/customer/my-invoices"
                                                        }
                                                        ?: throw NullValueNotAllowedException("No company associated with your record")
                                            }
                                            ?: throw UsernameNotFoundException("Invalid Session")
                                }
                                ?: throw UsernameNotFoundException("Invalid Session")

                    }
                    ?: throw ServiceMapNotFoundException("No service map found for appId=$appId, aborting")

    @GetMapping("/generated/invoice/{id}")
    fun generatedInvoice(
            model: Model, @PathVariable("id") id: Long,
            @RequestParam(value = "appId", required = false) appId: Int?,
            session: HttpSession
    ): String {

        var applicationState: String
        invoiceRepository.findByIdOrNull(id)
                ?.let { invoiceEntity ->
                    model.addAttribute("invoice", invoiceEntity)
                    session.setAttribute("paymentMethods", paymentMethodsRepository.findAll())

                    model.addAttribute("appId", appId)
                    model.addAttribute("mpesa", CdLaboratoryParametersEntity())

                    invoiceEntity.installationInspectionId
                            ?.let { fuelEnt ->
                                model.addAttribute("fuelEnt", fuelEnt)
                            }
                    invoiceEntity.permitId
                            ?.let { permitId ->
                                permitRepository.findById(permitId).let{ optPermit->
                                    var pm = optPermit.get()
                                    applicationState = if (pm.status != 1) {
                                        "Application"
                                    } else {
                                        "Renewal"
                                    }
                                    val permit = permitRepository.findByIdOrNull(pm.id)
                                    daoServices.extractManufacturerFromUser(daoServices.extractUserFromContext(SecurityContextHolder.getContext()))
                                        ?.let { manufacturersEntity ->
                                            model.addAttribute("phoneNumber", manufacturerContactsRepo.findByManufacturerId(manufacturersEntity)?.cellPhone)
                                            model.addAttribute("applicationState", applicationState)
//                                            model.addAttribute("appId", appId)
//                                            model.addAttribute("mpesa", CdLaboratoryParametersEntity())

                                            model.addAttribute("permit", permit)
                                            model.addAttribute("product", productsRepo.findByIdOrNull(pm.product))
                                            model.addAttribute("permitType", permitTypesRepo.findByIdOrNull(permit?.permitType))
//                                            session.setAttribute("paymentMethods", paymentMethodsRepository.findAll())
                                        }
                                }

                            }

                }



        return "quality-assurance/customer/generated-invoice"
    }

    @GetMapping("/renewal/invoice/{id}")
    fun renewalInvoice(
            @RequestParam(value = "appId", required = false) appId: Int?,
            model: Model,
            @PathVariable("id") id: Long,
            session: HttpSession): String {
        serviceMapsRepo.findByIdOrNull(appId)
                ?.let { map ->
                    model.addAttribute("invoice", invoiceRepository.findByIdOrNull(id))

                    invoiceRepository.findByIdOrNull(id)?.let{ invoice ->
                        invoice.permitId?.let{ permitId->
                            val pm = permitRepository.findById(permitId).get()
                            model.addAttribute("permit", permitRepository.findByIdOrNull(pm?.id))
                            if (pm?.status != map.activeStatus) {
                                model.addAttribute("application", "application")
                            } else {
                                model.addAttribute("renewal", "renewal")
                            }
                        }

                    }

                    session.setAttribute("paymentMethods", paymentMethodsRepository.findAll())

                }

        return "quality-assurance/customer/renewal-invoice"
    }

    @GetMapping("/payment/methods/{id}")
    fun getPaymentInstructions(@PathVariable("id") id: Long) {
        paymentMethodsRepository.findById(id)
    }


    @GetMapping("/generated/invoice/pay/{id}/{permitId}")
    fun pay(
            model: Model,
            @RequestParam(value = "appId", required = false) appId: Int?,
            @PathVariable("id") id: Long,
//            @PathVariable("permitId") permitId: Long,
//            @ModelAttribute(" invoice") invoice: InvoiceEntity,
            session: HttpSession
    ): String {
        return try {
            serviceMapsRepo.findByIdOrNull(appId)
                    ?.let { map ->
                        val invoice = invoiceRepository.findById(id).get()
                        with(invoice) {
                            status = map.activeStatus
                        }

                        invoiceRepository.save(invoice)
                        KotlinLogging.logger { }.debug("Invoice status updated to ${invoice.status}")
                        val user: UsersEntity = session.getAttribute("user") as UsersEntity

                        permitRepository.findByIdOrNull(invoice.permitId)
                                ?.let { permit ->
                                    with(permit) {
                                        paymentStatus = map.activeStatus
                                        modifiedBy = user.userName
                                        modifiedOn = Timestamp.from(Instant.now())
                                    }

                                    permitRepository.save(permit)

                                    permit.id?.let {
                                        qualityAssuranceBpmn.qaSfAPPaymentComplete(it).let {
                                            permit.id?.let { it1 ->
                                                qualityAssuranceBpmn.fetchTaskByPermitId(it1, qaSfApplicationPaymentProcessDefinitionKey)?.let { taskDetails ->
                                                    println("Task details after making the payment")
                                                    for (taskDetail in taskDetails) {
                                                        taskDetail.task.let { task ->
                                                            println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    KotlinLogging.logger { }.debug("Permit payment status updated to ${permit.paymentStatus}")
                                }

                        "redirect:/generated/invoice/{$id}"
                    }
                    ?: throw ServiceMapNotFoundException("ServiceMap with id=$appId not found")

        } catch (e: Exception) {
            "redirect:/generated/invoice/{id}"
        }
    }

    @GetMapping("/check-payment")
    fun checkPayment(
            @RequestParam("invoiceId") invoiceId: Long,
            @RequestParam("mpesaCode", required = false) mpesaCode: String,
            request: HttpServletRequest
    ): String {
        serviceMapsRepo.findByIdAndStatus(globalAppId, 1)
                ?.let { map ->
                    invoiceRepository.findByIdOrNull(invoiceId)
                            ?.let { invoice ->
//                                val transaction = mpesaTransactionRepo.findByMpesareceiptnumberAndUsedTransactionReference(mpesaCode, map.activeStatus)
                                mpesaService.findMpesaTransactionEntityByInvoice(invoiceId, invoiceSource)
                                        .let {transaction->
                                    with(invoice) {
                                        status = map.activeStatus
                                        paidOn = Timestamp.from(Instant.now())
                                        amountPaid = transaction.amount
                                        phone = transaction.phonenumber
                                    }
                                    invoiceRepository.save(invoice)
                                            /*
                                            .let {
                                                mpesaService.updateUsedStatus(transaction)
                                            }
                                            */

                                    invoice.permitId?.let{ permitId->
                                        val pm = permitRepository.findById(permitId).get()
                                        with(pm) {
                                            this?.paymentStatus = map.activeStatus
                                        }
                                        pm?.let { permitRepository.save(it) }

                                        // Start QA review process (BPMN)
                                        pm?.id?.let {
                                            qualityAssuranceBpmn.startQAAppReviewProcess(it, hofId)
                                        }
                                    }
                                }
                            }
                }
        return "redirect:/generated/invoice/${invoiceId}"
    }

    @GetMapping("/mpesa-stkpush")
    fun payPermitInvoice(
            @RequestParam("amount", required = false) amount: BigDecimal?,
            @RequestParam("phone", required = false) phone: String?,
            @RequestParam("invoiceId") invoiceId: Long,
            redirectAttributes: RedirectAttributes
    ): String {
        invoiceRepository.findByIdOrNull(invoiceId)
                ?.let { invoiceEntity ->
                    //mpesaService.loginMethod()
                    //mpesaService.mainMpesaTransaction(amount.toString(),phone.toString(),invoiceId.toString(),"",invoiceSource)
                    KotlinLogging.logger { }.debug("MPESA STK PUSH ${amount}, $phone")
//                    if (phone != null) {
                    /*
                    phone?.let {
                        mpesaService.sanitizePhoneNumber(it)?.let {
                            amount?.let { amt -> mpesaService.pushRequest(it, amt, invoiceId, "PERMIT") }
                        }
                    }
                    */
//                    }


//                    permitRepository.findByIdOrNull(id)
//                            ?.let { pm ->
//                                pm.paymentStatus = 1
//                                permitRepository.save(pm)
//                            }
                    //redirectAttributes.addFlashAttribute("alert", "Check Your Phone for a Payment Request")
                    serviceMapsRepo.findByIdAndStatus(globalAppId, 1)
                            ?.let { map ->
                                with(invoiceEntity) {
                                    status = map.activeStatus
                                    paidOn = java.sql.Timestamp.from(java.time.Instant.now())
                                    //amountPaid = transaction.amount
                                    //phone = transaction.phonenumber
                                }

                                invoiceEntity.permitId?.let{ permitId->
                                    val pm = permitRepository.findById(permitId).get()
                                    with(pm) {
                                        this?.paymentStatus = map.activeStatus
                                    }
                                    pm?.let { permitRepository.save(it) }

                                    // Start QA review process (BPMN)
                                    pm?.id?.let {
                                        qualityAssuranceBpmn.startQAAppReviewProcess(it, hofId)
                                    }
                                }
                            }


                    return "redirect:/generated/invoice/${invoiceId}"
                }
                ?: throw Exception("INVOICE WITH ID $invoiceId WAS NOT FOUND")


    }
}

