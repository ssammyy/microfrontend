package org.kebs.app.kotlin.apollo.api.handlers

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.StandardsLevyBpmn
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.common.exceptions.ServiceMapNotFoundException
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.ManufacturersEntity
import org.kebs.app.kotlin.apollo.store.model.StandardLevyFactoryVisitReportEntity
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.repo.*
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.ServerResponse.ok
import org.springframework.web.servlet.function.paramOrNull
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.math.BigDecimal

@Component
class StandardLevyHandler(
    applicationMapProperties: ApplicationMapProperties,
    private val serviceMapsRepository: IServiceMapsRepository,
    private val manufacturerRepository: IManufacturerRepository,
    private val manufacturerContactsRepository: IManufacturerContactsRepository,
    private val iManufacturePaymentDetailsRepository: IManufacturerPaymentDetailsRepository,
    private val businessNatureRepository: IBusinessNatureRepository,
    private val standardLevyFactoryVisitReportRepo: IStandardLevyFactoryVisitReportRepository,
    private val standardLevyPaymentsRepository: IStandardLevyPaymentsRepository,
    private val standardsLevyBpmn: StandardsLevyBpmn,
    private val userRepo: IUserRepository
) {

    private val redirectAttributes: RedirectAttributes? = null
    private val slManager = applicationMapProperties.slManager
    private val principalLevyOfficer = applicationMapProperties.principalLevyOfficer

    final val appId = applicationMapProperties.mapPermitApplication
    private val slAllManufacturers = "standard-levy/manufacturers"
    private val slHome = "standard-levy/home"
    private val singleManufacturerPage = "standard-levy/single-manufacturers"
    private val allPayments = "standard-levy/payments"

    @PreAuthorize("hasAuthority('SL_APPROVE_VISIT_REPORT') or hasAuthority('SL_SECOND_APPROVE_VISIT_REPORT') or hasAuthority('SL_MANUFACTURERS_VIEW')")
    fun home(req: ServerRequest): ServerResponse =
        try {
            serviceMapsRepository.findByIdAndStatus(appId, 1)
                ?.let { map ->
                    req.attributes()["map"] = map
                    ok().render(slHome)
                }
                ?: throw ServiceMapNotFoundException("Missing application mapping for [id=$appId], recheck configuration")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "Unknown error")
        }


    fun loadManufacturers(req: ServerRequest): ServerResponse {
        return try {
            serviceMapsRepository.findByIdOrNull(appId)
                ?.let { map ->
                    SecurityContextHolder.getContext().authentication
                        ?.let { auth ->
                            val currentPage = req.paramOrNull("currentPage")?.toIntOrNull() ?: 1
                            val pgSize = req.paramOrNull("pageSize")?.toIntOrNull() ?: 1
                            PageRequest.of(currentPage, pgSize)
                                .let { page ->
                                    userRepo.findByUserName(auth.name)
                                        ?.let { user ->
                                            val whereTo = req.paramOrNull("whereTo") ?: throw NullValueNotAllowedException("whereTo parameter is required")
                                            when (whereTo) {
                                                "load_manufacturers" -> {
                                                    manufacturerRepository.findByOrderByIdDesc(page)
                                                        .let { manufacturers ->
                                                            req.attributes()["manufacturers"] = manufacturers
                                                            req.attributes()["map"] = map
                                                            req.attributes()["type"] = "load_manufacturers"
                                                            ok().render(slAllManufacturers, req.attributes())

                                                        }
                                                }
                                                "load_tasks" -> {
                                                    user.id
                                                        ?.let { userId ->
                                                            standardsLevyBpmn.fetchAllTasksByAssignee(userId)
                                                                ?.let { lstTaskDetails ->
                                                                    val tasks = mutableListOf<ManufacturersEntity?>()
                                                                    lstTaskDetails.sortedByDescending { it.objectId }.forEach { details ->
                                                                        if (standardLevyPaymentsRepository.findByIdOrNull(details.objectId) == null) {
                                                                            redirectAttributes?.addFlashAttribute("error", "Caught an exception while loading your tasks")
                                                                            req.attributes()["map"] = map
                                                                            KotlinLogging.logger { }.info { "there" }
                                                                            req.attributes()["type"] = "load_tasks"
//                                                                            ok().render(slAllManufacturers, req.attributes())
                                                                        } else {
                                                                            standardLevyPaymentsRepository.findByIdOrNull(details.objectId)
                                                                                ?.let { paymentsEntity ->
                                                                                    tasks.add(paymentsEntity.manufacturerEntity)
                                                                                    req.attributes()["tasks"] = tasks
                                                                                    req.attributes()["map"] = map
                                                                                    KotlinLogging.logger { }.info { "here" }
                                                                                    redirectAttributes?.addFlashAttribute("success", "View your tasks")
                                                                                }
                                                                                ?: throw ExpectedDataNotFound("No payment with id=${details.objectId}")
                                                                        }
                                                                    }
                                                                    ok().render(slAllManufacturers, req.attributes())


                                                                }
                                                                ?: ok().render(slAllManufacturers, req.attributes())
                                                        }
                                                        ?: ok().render(slAllManufacturers, req.attributes())

                                                }
                                                "load_levy_payments" -> {
                                                    standardLevyPaymentsRepository.findByOrderByIdDesc(page)
                                                        .let { payments ->
                                                            KotlinLogging.logger { }.info("Records found ${payments.count()}")
                                                            req.attributes()["payments"] = payments
                                                            req.attributes()["map"] = map
                                                            ok().render(allPayments, req.attributes())
                                                        }
                                                }
                                                else -> {
                                                    redirectAttributes?.addFlashAttribute("error", "")
                                                    ok().render("redirect:/sl", req.attributes())
                                                }
                                            }
                                        }
                                        ?: ok().render(slAllManufacturers, req.attributes())

                                }
                        }
                        ?: throw NullValueNotAllowedException("Invalid session")
                }
                ?: throw ServiceMapNotFoundException("Missing application mapping for [id=$appId], recheck configuration")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "Unknown error")

        }
    }

    fun singleManufacturer(req: ServerRequest): ServerResponse =
        try {
            serviceMapsRepository.findByIdOrNull(appId)
                ?.let { map ->
                    SecurityContextHolder.getContext().authentication?.name
                        ?.let {
                            req.paramOrNull("manufacturerId")
                                ?.let { manufacturerId ->
                                    manufacturerRepository.findByIdOrNull(manufacturerId.toLong())
                                        ?.let { manufacturer ->
                                            manufacturerContactsRepository.findByManufacturerId(manufacturer)
                                                .let { contacts ->
                                                    iManufacturePaymentDetailsRepository.findByManufacturerId(manufacturer.id)
                                                        .let { turnover ->
                                                            businessNatureRepository.findByIdOrNull(manufacturer.businessNatureId)
                                                                .let { nature ->
                                                                    standardLevyFactoryVisitReportRepo.findByManufacturerEntity(manufacturer)
                                                                        .let {
                                                                            req.attributes()["visitReport"] = it
                                                                        }

                                                                    standardLevyPaymentsRepository.findByManufacturerEntity(manufacturer)
                                                                        .let { paymentHistory ->
                                                                            req.attributes()["paymentHistory"] = paymentHistory
                                                                            KotlinLogging.logger { }.info { "Payment history, $paymentHistory" }
                                                                        }
//                                                                                                        }
                                                                    req.attributes()["reportData"] = StandardLevyFactoryVisitReportEntity()
                                                                    req.attributes()["manufacturer"] = manufacturer
                                                                    req.attributes()["map"] = map
                                                                    req.attributes()["contacts"] = contacts
                                                                    req.attributes()["turnover"] = turnover
                                                                    req.attributes()["nature"] = nature
                                                                    return ok().render(singleManufacturerPage, req.attributes())
                                                                }
                                                        }
                                                }

                                        }
                                        ?: throw ExpectedDataNotFound("Manufacturer [id=${req.paramOrNull("manufacturerId")}] does not exist")
                                }
                                ?: throw ExpectedDataNotFound("Manufacturer [id=${req.paramOrNull("manufacturerId")}] does not exist")
                        }
                        ?: throw NullValueNotAllowedException("Invalid session")

                }
                ?: throw ServiceMapNotFoundException("Missing application mapping for [id=$appId], recheck configuration")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "Unknown error")
        }


    @PreAuthorize("hasAuthority('SL_SCHEDULE_FACTORY_VISIT_MANUFACTURER') and hasAuthority('SL_MANUFACTURERS_VIEW')")
    fun scheduleVisit(req: ServerRequest): ServerResponse =
        try {
            serviceMapsRepository.findByIdOrNull(appId)
                ?.let { map ->
                    req.paramOrNull("manufacturerId")
                        ?.let { manufacturerId ->
                            manufacturerRepository.findByIdOrNull(manufacturerId.toLong())
                                ?.let { manufacturer ->
                                    req.paramOrNull("scheduleDate")
                                        ?.let { scheduleDate ->
                                            with(manufacturer) {
                                                factoryVisitDate = scheduleDate
                                                factoryVisitStatus = map.activeStatus
                                            }
                                            manufacturerRepository.save(manufacturer)

                                            val kraId = req.paramOrNull("kraId")?.toLong() ?: throw NullValueNotAllowedException("Invalid KraId")

                                            // Schedule site visit complete
                                            standardsLevyBpmn.slsvScheduleVisitComplete(kraId)

                                            redirectAttributes?.addFlashAttribute("alert", "You have scheduled the factory visit")
                                            return ok().render("redirect:/sl/manufacturer?manufacturerId=${manufacturer.id}&appId=${appId}", req.attributes())
                                        }

                                }
                        }

                }
                ?: throw ServiceMapNotFoundException("Missing application mapping for [id=$appId], recheck configuration")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "Unknown error")

        }


    fun generalActions(req: ServerRequest): ServerResponse =
        try {
            serviceMapsRepository.findByIdOrNull(appId)
                ?.let { map ->
                    manufacturerRepository.findByIdOrNull(req.paramOrNull("manufacturerId")?.toLong())
                        ?.let { manufacturer ->
                            req.paramOrNull("whereTo")
                                ?.let { whereTo ->
                                    return when (whereTo) {
                                        "assistant_manager_approval" -> {
                                            standardLevyFactoryVisitReportRepo.findByManufacturerEntity(manufacturer)
                                                ?.let { visitReport ->
                                                    with(visitReport) {
                                                        assistantManagerApproval = req.paramOrNull("approvalStatus")?.toIntOrNull() ?: throw NullValueNotAllowedException("Invalid approvalStatus")
                                                    }
                                                    standardLevyFactoryVisitReportRepo.save(visitReport)

                                                    val kraId = req.paramOrNull("kraId")?.toLongOrNull() ?: throw NullValueNotAllowedException("Invalid KraId")
                                                    //Assistant manager approve complete
                                                    standardsLevyBpmn.slsvApproveReportAsstManagerComplete(kraId, slManager, true)

                                                    redirectAttributes?.addFlashAttribute("success", "You have approved the report.")
                                                    ok().render("redirect:/sl/manufacturer?manufacturerId=${manufacturer.id}&appId=${map.id}", req.attributes())
                                                }
                                                ?: throw NullValueNotAllowedException("Required report data is not present")
                                        }

                                        "manager_approval" -> {
                                            standardLevyFactoryVisitReportRepo.findByManufacturerEntity(manufacturer)
                                                ?.let { visitReport ->
                                                    with(visitReport) {
                                                        managersApproval = req.paramOrNull("approvalStatus")?.toIntOrNull()
                                                    }
                                                    standardLevyFactoryVisitReportRepo.save(visitReport)

                                                    val kraId = req.paramOrNull("kraId")?.toLongOrNull() ?: throw NullValueNotAllowedException("Invalid kraId")
                                                    //Assistant manager approve complete
                                                    standardsLevyBpmn.slsvApproveReportManagerComplete(kraId, principalLevyOfficer, true)

                                                    redirectAttributes?.addFlashAttribute("success", "You have approved the report.")
                                                    ok().render("redirect:/sl/manufacturer?manufacturerId=${manufacturer.id}&appId=${map.id}", req.attributes())
                                                }
                                                ?: throw NullValueNotAllowedException("Required report data is not present")

                                        }
                                        "submit_feedback" -> {
                                            standardLevyFactoryVisitReportRepo.findByManufacturerEntity(manufacturer)
                                                ?.let { visitReport ->
                                                    with(visitReport) {
                                                        officersFeedback = req.paramOrNull("feedback")
                                                    }
                                                    standardLevyFactoryVisitReportRepo.save(visitReport)

                                                    val kraId = req.paramOrNull("kraId")?.toLongOrNull() ?: throw NullValueNotAllowedException("Invalid kraId")
                                                    //Assistant manager approve complete
                                                    standardsLevyBpmn.slsvApproveReportManagerComplete(kraId, principalLevyOfficer, true)

                                                    redirectAttributes?.addFlashAttribute("alert", "You have added your feedbackt.")
                                                    ok().render("redirect:/sl/manufacturer?manufacturerId=${manufacturer.id}&appId=${map.id}", req.attributes())
                                                }
                                                ?: throw NullValueNotAllowedException("Required report data is not present")
                                        }
                                        else -> {
                                            redirectAttributes?.addFlashAttribute("error", "Caught an exception. ")
                                            ok().render("redirect:/sl/manufacturer?manufacturerId=${manufacturer.id}&appId=${map.id}", req.attributes())
                                        }
                                    }
                                }
                        }

                }
                ?: throw ServiceMapNotFoundException("Missing application mapping for [id=$appId], recheck configuration")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "Unknown error")
        }

}

data class PaymentsEntityDto(
    val id: Long?,
    val manufacturer: String?,
    val paymentDate: String?,
    val paymentAmount: BigDecimal?,
    val visitStatus: Long?,
    val assignedTo: UsersEntity?
)
