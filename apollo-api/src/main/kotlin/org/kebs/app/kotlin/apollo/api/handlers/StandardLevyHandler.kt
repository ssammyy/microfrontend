package org.kebs.app.kotlin.apollo.api.handlers

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.StandardsLevyBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.common.exceptions.ServiceMapNotFoundException
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.StandardLevyFactoryVisitReportEntity
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.registration.CompanyProfileEntity
import org.kebs.app.kotlin.apollo.store.repo.*
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
import java.sql.Date
import java.time.LocalDate


@Component
class StandardLevyHandler(
    applicationMapProperties: ApplicationMapProperties,
    private val serviceMapsRepository: IServiceMapsRepository,
    private val businessNatureRepository: IBusinessNatureRepository,
    private val standardLevyFactoryVisitReportRepo: IStandardLevyFactoryVisitReportRepository,
    private val standardLevyPaymentsRepository: IStandardLevyPaymentsRepository,
    private val standardsLevyBpmn: StandardsLevyBpmn,
    private val commonDaoServices: CommonDaoServices,
    private val companyProfileRepo: ICompanyProfileRepository,
    private val userRepo: IUserRepository,

    ) {

    private val redirectAttributes: RedirectAttributes? = null
    private val slManager = applicationMapProperties.slManager
    private val principalLevyOfficer = applicationMapProperties.principalLevyOfficer

    final val appId = applicationMapProperties.mapPermitApplication
    private val slAllManufacturers = "standard-levy/manufacturers"
    private val slHome = "standard-levy/home"
    private val singleManufacturerPage = "standard-levy/single-manufacturers"
    private val allPayments = "standard-levy/payments"

    //var formatter = DateTimeFormatter.ofPattern("yyyy-mm-dd")

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

                            userRepo.findByUserName(auth.name)
                                ?.let { user ->
                                    val whereTo = req.paramOrNull("whereTo")
                                        ?: throw ExpectedDataNotFound("whereTo parameter is required")
                                    when (whereTo) {
                                        "load_manufacturers" -> {
                                            commonDaoServices.findCompanyProfileWhoAreManufactures(map.activeStatus)
                                                .let { manufacturer ->
                                                    req.attributes()["manufacturers"] = manufacturer
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
                                                            val tasks = mutableListOf<CompanyProfileEntity?>()
                                                            lstTaskDetails.sortedByDescending { it.objectId }
                                                                .forEach { details ->
                                                                    if (standardLevyPaymentsRepository.findByIdOrNull(
                                                                            details.objectId
                                                                        ) == null
                                                                    ) {
                                                                        redirectAttributes?.addFlashAttribute(
                                                                            "error",
                                                                            "Caught an exception while loading your tasks"
                                                                        )
                                                                        req.attributes()["map"] = map
                                                                        KotlinLogging.logger { }
                                                                            .info { "there" }
                                                                        req.attributes()["type"] = "load_tasks"
//                                                                            ok().render(slAllManufacturers, req.attributes())
                                                                    } else {
                                                                        standardLevyPaymentsRepository.findByIdOrNull(
                                                                            details.objectId
                                                                        )
                                                                            ?.let { paymentsEntity ->

                                                                                companyProfileRepo.findByIdOrNull(paymentsEntity.manufacturerEntity)
                                                                                    ?.let { tasks.add(it) }
                                                                                    ?: throw ExpectedDataNotFound("INVALID MANUFACTURER ID")


                                                                                req.attributes()["tasks"] =
                                                                                    tasks
                                                                                req.attributes()["map"] = map
                                                                                KotlinLogging.logger { }
                                                                                    .info { "here" }
                                                                                redirectAttributes?.addFlashAttribute(
                                                                                    "success",
                                                                                    "View your tasks"
                                                                                )
                                                                            }
                                                                            ?: throw ExpectedDataNotFound("No payment with id=${details.objectId}")
                                                                    }
                                                                }
                                                            ok().render(slAllManufacturers, req.attributes())


                                                        }
                                                        ?: throw ExpectedDataNotFound("Invalid DATA")
                                                }
                                                ?: throw ExpectedDataNotFound("Invalid DATA")

                                        }
                                        "load_levy_payments" -> {
                                            standardLevyPaymentsRepository.findAllByStatusOrderByIdDesc(1)
                                                .let { payments ->
                                                    KotlinLogging.logger { }
                                                        .info("Records found ${payments?.count()}")
                                                    KotlinLogging.logger { }.info("Records found ${payments?.count()}")
                                                    req.attributes()["payments"] = payments
                                                    req.attributes()["map"] = map
                                                    ok().render(allPayments, req.attributes())
                                                }
                                        }

                                        "load_levy_no_payments" -> {
                                            standardLevyPaymentsRepository.findAllByStatusOrderByIdDesc(0)
                                                .let { payments ->
                                                    KotlinLogging.logger { }
                                                        .info("Records found ${payments?.count()}")
                                                    KotlinLogging.logger { }.info("Records found ${payments?.count()}")
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
                                ?: throw ExpectedDataNotFound("Invalid DATA")

                        }
                        ?: throw ExpectedDataNotFound("Invalid session")
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
                            req.paramOrNull("manufacturerId")?.toLongOrNull()
                                ?.let { manufacturerId ->
                                    KotlinLogging.logger { }.info { "Manufacturer id juu ==> " + manufacturerId }
                                    commonDaoServices.findCompanyProfileWithID(manufacturerId)
                                        .let { manufacturer ->
                                            KotlinLogging.logger { }.info { "Manufacturer id ==> " + manufacturer.id }
                                            commonDaoServices.findAllPlantDetails(
                                                manufacturer.userId ?: throw ExpectedDataNotFound("INVALID USER ID")
                                            )
                                                .let { contacts ->
                                                    if (contacts.isEmpty()) {
//                                                        throw ExpectedDataNotFound("Contact information does not exist")
                                                        KotlinLogging.logger { }.error("Contact information does not exist for manufacturer id = ${manufacturer.userId}")
                                                    } else {
                                                        req.attributes()["contacts"] = contacts[0]
                                                    }
                                                    businessNatureRepository.findByIdOrNull(manufacturer.businessNatures)
                                                        .let { nature ->
                                                            standardLevyFactoryVisitReportRepo.findByManufacturerEntity(manufacturer.id ?: throw ExpectedDataNotFound("INVALID ID"))
                                                                .let {
                                                                    req.attributes()["visitReport"] = it
                                                                }

                                                            standardLevyPaymentsRepository.findByManufacturerEntity(
                                                                manufacturer.id
                                                                    ?: throw ExpectedDataNotFound("INVALID ID")
                                                            )
                                                                .let { paymentHistory ->
//                                                                    KotlinLogging.logger { }.info { "paymentHistory ==>  " + paymentHistory?.id }
                                                                    req.attributes()["paymentHistory"] = paymentHistory

                                                                }
                                                            //                                                                                                        }
                                                            req.attributes()["reportData"] =
                                                                StandardLevyFactoryVisitReportEntity()
                                                            req.attributes()["manufacturer"] = manufacturer
                                                            req.attributes()["map"] = map

                                                            req.attributes()["turnover"] = manufacturer.yearlyTurnover
                                                            req.attributes()["nature"] = nature
                                                            return ok().render(singleManufacturerPage, req.attributes())
                                                        }

                                                }

                                        }
                                }
                                ?: throw ExpectedDataNotFound("Manufacturer [id=${req.paramOrNull("manufacturerId")}] does not exist")
                        }
                        ?: throw ExpectedDataNotFound("Invalid session")

                }
                ?: throw ServiceMapNotFoundException("Missing application mapping for [id=$appId], recheck configuration")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "Unknown error")
        }


    @PreAuthorize("hasAuthority('SL_SCHEDULE_FACTORY_VISIT_MANUFACTURER') and hasAuthority('SL_MANUFACTURERS_VIEW')")
    fun scheduleVisit(req: ServerRequest): ServerResponse {
        serviceMapsRepository.findByIdOrNull(appId)
            ?.let { map ->
                req.pathVariable("manufacturer").let { manufacturerId ->
                    commonDaoServices.findCompanyProfileWithID(manufacturerId.toLong()).let { manufacturerDetails ->
                        manufacturerDetails.factoryVisitDate = Date.valueOf(req.paramOrNull("scheduleDate"))
                        manufacturerDetails.factoryVisitStatus = map.activeStatus
                        manufacturerDetails.createdBy = "Admin"
                        manufacturerDetails.createdOn = commonDaoServices.getTimestamp()
                        companyProfileRepo.save(manufacturerDetails)
                        val factoryVisitReportEntity = StandardLevyFactoryVisitReportEntity()
                        factoryVisitReportEntity.manufacturerEntity = manufacturerDetails.id
                        factoryVisitReportEntity.scheduledVisitDate = manufacturerDetails.factoryVisitDate as Date
                        factoryVisitReportEntity.createdBy = "Admin"
                        factoryVisitReportEntity.createdOn = commonDaoServices.getTimestamp()
                        val savedReport = standardLevyFactoryVisitReportRepo.save(factoryVisitReportEntity)
                        KotlinLogging.logger { }.info("New id ${savedReport.id}")
                        standardsLevyBpmn.startSlSiteVisitProcess(savedReport.id ?: throw NullValueNotAllowedException("Id should not be null"), commonDaoServices.getLoggedInUser()?.id ?: throw Exception("Please login"))
                        standardsLevyBpmn.slSvQueryManufacturerDetailsComplete(savedReport.id ?: throw NullValueNotAllowedException("Id should not be null"))
                        standardsLevyBpmn.slSvScheduleVisitComplete(savedReport.id ?: throw NullValueNotAllowedException("Id should not be null"))

                        //manufacturerDetails.id.let { standardsLevyBpmn.startSlSiteVisitProcess(manufacturerDetails.id, 54) }
                        //manufacturerDetails.id.let { standardsLevyBpmn.slsvQueryManufacturerDetailsComplete(it) }
                        //manufacturerDetails.id.let { standardsLevyBpmn.slsvScheduleVisitComplete(it) }
                        redirectAttributes?.addFlashAttribute(
                            "alert",
                            "You have scheduled the factory visit"
                        )
                        return ok().render(
                            "redirect:/sl/manufacturer?manufacturerId=${manufacturerDetails.id}&appId=${appId}",
                            req.attributes()
                        )

                    }

                }
//                    req.body ()
//                        ?.let { manufacturerId ->
//                            manufacturerRepository.findByIdOrNull(manufacturerId.toLong())
//                                ?.let { manufacturer ->
//                                    req.param("scheduleDate")
//                                        .let { scheduleDate ->
//                                            KotlinLogging.logger {  }.info { "Scheduled date  ==> "+ scheduleDate }
//                                            with(manufacturer) {
//                                                factoryVisitDate = scheduleDate.toString()
//                                                factoryVisitStatus = map.activeStatus
//                                            }
//                                            manufacturerRepository.save(manufacturer)
//
//                                            val kraId = req.paramOrNull("kraId")?.toLong()
//
//                                            KotlinLogging.logger {  }.info { "kraId ==> $kraId" }
//
//                                            // Schedule site visit complete
//                                            if (kraId != null) {
//
//                                            }
//
//
//                                        }
//
//                                }
//                        }

            }
            ?: throw ServiceMapNotFoundException("Missing application mapping for [id=$appId], recheck configuration")

    }

//    fun scheduleVisit(req: ServerRequest): ServerResponse {
//        try {
//            req.pathVariable("manufacturer").toLongOrNull()
//                ?.let { id ->
//                    val dto = req.body<StandardLevyFactoryVisitReportEntity>()
//                    dto.userId = id
//                    daoService.userRequest(dto)?.let {
//                        return ok().body(it)
//                    }
//                        ?: throw NullValueNotAllowedException("User ID is null")
//                } ?: throw NullValueNotAllowedException("Update failed")
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.error(e.message)
//            KotlinLogging.logger { }.debug(e.message, e)
//            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
//        }
//    }

    @PreAuthorize("hasAuthority('SL_SCHEDULE_FACTORY_VISIT_MANUFACTURER') and hasAuthority('SL_MANUFACTURERS_VIEW')")
    fun paidLevies(req: ServerRequest) {
        standardLevyPaymentsRepository.findAllByStatusOrderByIdDesc(1)?.let { paidLevies ->
            req.attributes()["paidLevies"] = paidLevies
        }
        ok().render("destination-inspection/pvoc/complaint/ComplaintsForm", req.attributes())
    }

    @PreAuthorize("hasAuthority('SL_SCHEDULE_FACTORY_VISIT_MANUFACTURER') and hasAuthority('SL_MANUFACTURERS_VIEW')")
    fun actionScheduleVisit(req: ServerRequest): ServerResponse =
        try {
            serviceMapsRepository.findByIdOrNull(appId)
                ?.let { map ->
                    req.paramOrNull("manufacturerId")
                        ?.let { manufacturerId ->
                            commonDaoServices.findCompanyProfileWithID(
                                manufacturerId.toLongOrNull()
                                    ?: throw ExpectedDataNotFound("Invalid manufacturer id")
                            )
                                .let { manufacturer ->
                                    req.paramOrNull("scheduleDate")
                                        ?.let { scheduleDate ->
                                            val locald = LocalDate.parse(scheduleDate)
                                            val date: Date = Date.valueOf(locald) // Magic happens here!

                                            with(manufacturer) {
                                                factoryVisitDate = date
                                                factoryVisitStatus = map.activeStatus
                                            }
                                            companyProfileRepo.save(manufacturer)


                                            // Schedule site visit complete
                                            var visit = StandardLevyFactoryVisitReportEntity().apply {
                                                manufacturerEntity = manufacturer.id
                                                scheduledVisitDate = manufacturer.factoryVisitDate
                                                status = map.initStatus
                                            }
                                            visit = standardLevyFactoryVisitReportRepo.save(visit)

                                            val userName = SecurityContextHolder.getContext().authentication?.name
                                                ?: throw ExpectedDataNotFound("Invalid session")
                                            userRepo.findByUserName(userName)
                                                ?.let {
                                                    standardsLevyBpmn.startSlSiteVisitProcess(visit.id ?: throw NullValueNotAllowedException("Id should not be null"), it.id ?: throw ExpectedDataNotFound("Invalid user"))
                                                    standardsLevyBpmn.slSvQueryManufacturerDetailsComplete(visit.id ?: throw NullValueNotAllowedException("Id should not be null"))
                                                    standardsLevyBpmn.slSvScheduleVisitComplete(visit.id ?: throw NullValueNotAllowedException("Id should not be null"))
                                                }
                                                ?: throw ExpectedDataNotFound("User not found")
                                            //standardsLevyBpmn.slsvScheduleVisitComplete(kraId)

                                            redirectAttributes?.addFlashAttribute(
                                                "alert",
                                                "You have scheduled the factory visit"
                                            )
                                            return ok().render(
                                                "redirect:/sl/manufacturer?manufacturerId=${manufacturer.id}&appId=${appId}",
                                                req.attributes()
                                            )
                                        }

                                }
                        }

                }
                ?: throw ServiceMapNotFoundException("Missing application mapping for [id=$appId], recheck configuration")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            throw e

        }


    fun generalActions(req: ServerRequest): ServerResponse =
        try {
            serviceMapsRepository.findByIdOrNull(appId)
                ?.let { map ->
                    commonDaoServices.findCompanyProfileWithID(
                        req.paramOrNull("manufacturerId")?.toLong() ?: throw ExpectedDataNotFound("INVALID ID")
                    )
                        .let { manufacturer ->
                            req.paramOrNull("whereTo")
                                ?.let { whereTo ->
                                    return when (whereTo) {
                                        "assistant_manager_approval" -> {
                                            standardLevyFactoryVisitReportRepo.findByManufacturerEntity(
                                                manufacturer.id ?: throw ExpectedDataNotFound("INVALID ID")
                                            )
                                                ?.let { visitReport ->
                                                    with(visitReport) {
                                                        assistantManagerApproval =
                                                            req.paramOrNull("approvalStatus")?.toIntOrNull()
                                                                ?: throw ExpectedDataNotFound("Invalid approvalStatus")
                                                    }
                                                    standardLevyFactoryVisitReportRepo.save(visitReport)

                                                    val kraId = req.paramOrNull("kraId")?.toLongOrNull()
                                                        ?: throw ExpectedDataNotFound("Invalid KraId")
                                                    //Assistant manager approve complete
                                                    standardsLevyBpmn.slsvApproveReportAsstManagerComplete(
                                                        kraId,
                                                        slManager,
                                                        true
                                                    )

                                                    redirectAttributes?.addFlashAttribute(
                                                        "success",
                                                        "You have approved the report."
                                                    )
                                                    ok().render(
                                                        "redirect:/sl/manufacturer?manufacturerId=${manufacturer.id}&appId=${map.id}",
                                                        req.attributes()
                                                    )
                                                }
                                                ?: throw ExpectedDataNotFound("Required report data is not present")
                                        }

                                        "manager_approval" -> {
                                            standardLevyFactoryVisitReportRepo.findByManufacturerEntity(
                                                manufacturer.id ?: throw ExpectedDataNotFound("INVALID ID")
                                            )
                                                ?.let { visitReport ->
                                                    with(visitReport) {
                                                        managersApproval =
                                                            req.paramOrNull("approvalStatus")?.toIntOrNull()
                                                    }
                                                    standardLevyFactoryVisitReportRepo.save(visitReport)

                                                    val kraId = req.paramOrNull("kraId")?.toLongOrNull()
                                                        ?: throw ExpectedDataNotFound("Invalid kraId")
                                                    //Assistant manager approve complete
                                                    standardsLevyBpmn.slsvApproveReportManagerComplete(
                                                        kraId,
                                                        principalLevyOfficer,
                                                        true
                                                    )

                                                    redirectAttributes?.addFlashAttribute(
                                                        "success",
                                                        "You have approved the report."
                                                    )
                                                    ok().render(
                                                        "redirect:/sl/manufacturer?manufacturerId=${manufacturer.id}&appId=${map.id}",
                                                        req.attributes()
                                                    )
                                                }
                                                ?: throw ExpectedDataNotFound("Required report data is not present")

                                        }
                                        "submit_feedback" -> {
                                            standardLevyFactoryVisitReportRepo.findByManufacturerEntity(
                                                manufacturer.id ?: throw ExpectedDataNotFound("INVALID ID")
                                            )
                                                ?.let { visitReport ->
                                                    with(visitReport) {
                                                        officersFeedback = req.paramOrNull("feedback")
                                                    }
                                                    standardLevyFactoryVisitReportRepo.save(visitReport)

                                                    val kraId = req.paramOrNull("kraId")?.toLongOrNull()
                                                        ?: throw ExpectedDataNotFound("Invalid kraId")
                                                    //Assistant manager approve complete
                                                    standardsLevyBpmn.slsvApproveReportManagerComplete(
                                                        kraId,
                                                        principalLevyOfficer,
                                                        true
                                                    )

                                                    redirectAttributes?.addFlashAttribute(
                                                        "alert",
                                                        "You have added your feedbackt."
                                                    )
                                                    ok().render(
                                                        "redirect:/sl/manufacturer?manufacturerId=${manufacturer.id}&appId=${map.id}",
                                                        req.attributes()
                                                    )
                                                }
                                                ?: throw ExpectedDataNotFound("Required report data is not present")
                                        }
                                        else -> {
                                            redirectAttributes?.addFlashAttribute("error", "Caught an exception. ")
                                            ok().render(
                                                "redirect:/sl/manufacturer?manufacturerId=${manufacturer.id}&appId=${map.id}",
                                                req.attributes()
                                            )
                                        }
                                    }
                                }
                        }

                }
                ?: throw ServiceMapNotFoundException("Missing application mapping for [id=$appId], recheck configuration")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
//            ServerResponse.badRequest().body(e.message ?: "Unknown error")
            throw e
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
