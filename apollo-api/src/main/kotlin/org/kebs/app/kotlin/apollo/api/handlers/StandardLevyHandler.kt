package org.kebs.app.kotlin.apollo.api.handlers

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.StandardsLevyBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.InvalidInputException
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.common.exceptions.ServiceMapNotFoundException
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.StandardLevyFactoryVisitReportEntity
import org.kebs.app.kotlin.apollo.store.repo.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.ServerResponse.ok
import org.springframework.web.servlet.function.body
import org.springframework.web.servlet.function.paramOrNull
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.sql.Date
import java.time.LocalDate


@Component
class StandardLevyHandler(
    private val applicationMapProperties: ApplicationMapProperties,
    private val serviceMapsRepository: IServiceMapsRepository,
    private val businessNatureRepository: IBusinessNatureRepository,
    private val standardLevyFactoryVisitReportRepo: IStandardLevyFactoryVisitReportRepository,
    private val standardLevyPaymentsRepository: IStandardLevyPaymentsRepository,
    private val standardsLevyBpmn: StandardsLevyBpmn,
    private val commonDaoServices: CommonDaoServices,
    private val companyProfileRepo: ICompanyProfileRepository,
    private val userRepo: IUserRepository,
    private val townsRepo: ITownsRepository,
    private val slVisitsUploadRepo: ISlVisitUploadsRepository,
    private val userRolesRepo: IUserRoleAssignmentsRepository,


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
                                                            val tasks = mutableListOf<SlTasksEntityDto?>()
                                                            lstTaskDetails.forEach { task ->
                                                                standardLevyFactoryVisitReportRepo.findBySlProcessInstanceId(task.task.processInstanceId)
                                                                    ?.let { report ->
                                                                        val dto = SlTasksEntityDto(report.manufacturerEntity, task.task.createTime, task.task.dueDate, task.task.processInstanceId, task.task.name)
                                                                        tasks.add(dto)
                                                                    }
                                                                    ?: KotlinLogging.logger { }.info("SL Task not tied to a report")
                                                            }
                                                            KotlinLogging.logger { }.debug(" Found ${lstTaskDetails.count()} tasks")
                                                            req.attributes()["map"] = map

                                                            req.attributes()["type"] = "load_tasks"
                                                            tasks.sortBy { it?.assignedDate }
                                                            req.attributes()["tasks"] = tasks

                                                            ok().render(slAllManufacturers, req.attributes())


                                                        }
                                                        ?: throw ExpectedDataNotFound("Invalid DATA")
                                                }
                                                ?: throw ExpectedDataNotFound("Invalid DATA")

                                        }
                                        "load_levy_payments" -> {
                                            val userId = commonDaoServices.loggedInUserDetails().id ?: -3L
                                            val isEmployee = userRolesRepo.findByUserIdAndRoleIdAndStatus(userId, applicationMapProperties.slEmployeeRoleId ?: throw NullValueNotAllowedException("Role definition for employees not done"), 1)?.id != null



                                            if (isEmployee) {
                                                standardLevyPaymentsRepository.findAllByOrderByIdDesc()
                                                    .let { payments ->
                                                        KotlinLogging.logger { }
                                                            .info("Records found ${payments?.count()}")
                                                        KotlinLogging.logger { }.info("Records found ${payments?.count()}")
                                                        req.attributes()["payments"] = payments

                                                    }
                                            } else {
                                                standardLevyPaymentsRepository.findByManufacturerEntityOrderByIdDesc(companyProfileRepo.findByUserId(userId)?.id ?: throw NullValueNotAllowedException("Invalid Request"))
                                                    .let { payments ->
                                                        KotlinLogging.logger { }
                                                            .info("Records found ${payments?.count()}")
                                                        KotlinLogging.logger { }.info("Records found ${payments?.count()}")
                                                        req.attributes()["payments"] = payments

                                                    }
                                            }
                                            req.attributes()["map"] = map
                                            ok().render(allPayments, req.attributes())
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
                                    commonDaoServices.findCompanyProfileWithID(manufacturerId)
                                        .let { manufacturer ->
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
                                                            userRepo.getUsersWithAuthorizationId(applicationMapProperties.slLevelOneApprovalAuthorityId ?: throw InvalidInputException("No Authorization for Level One Approvals defined, aborting"))
                                                                ?.let { u ->
                                                                    req.attributes()["slLevelOneApprovals"] = u
                                                                }
                                                                ?: throw InvalidInputException("No Authorization for Level One Approvals defined, aborting")
                                                            userRepo.getUsersWithAuthorizationId(applicationMapProperties.slLevelTwoApprovalAuthorityId ?: throw InvalidInputException("No Authorization for Level One Approvals defined, aborting"))
                                                                ?.let { u ->
                                                                    req.attributes()["slLevelTwoApprovals"] = u
                                                                }
                                                                ?: throw InvalidInputException("No Authorization for Level One Approvals defined, aborting")
//                                                            standardLevyFactoryVisitReportRepo.findFirstByManufacturerEntityAndStatusOrderByIdDesc(manufacturer.id ?: throw ExpectedDataNotFound("INVALID ID"),0)
//                                                                ?.let {
//                                                                    req.attributes()["visitReport"] = it
//                                                                }
//                                                                ?: run{
//                                                                    req.attributes()["visitReport"] = StandardLevyFactoryVisitReportEntity()
//                                                                }

                                                            standardLevyPaymentsRepository.findByManufacturerEntity(
                                                                manufacturer.id
                                                                    ?: throw ExpectedDataNotFound("INVALID ID")
                                                            )
                                                                .let { paymentHistory ->
//                                                                    KotlinLogging.logger { }.info { "paymentHistory ==>  " + paymentHistory?.id }
                                                                    req.attributes()["paymentHistory"] = paymentHistory

                                                                }


                                                            //                                                                                                        }
                                                            /**
                                                             * Check if a report exists that is not yet approved and load that
                                                             */
                                                            manufacturer.id
                                                                ?.let {
                                                                    standardLevyFactoryVisitReportRepo.findFirstByManufacturerEntityAndStatusOrderByIdDesc(it, 0)
                                                                        ?.let { reportEntity ->
                                                                            req.attributes()["visitReport"] = reportEntity
                                                                            /**
                                                                             * Are there any uploaded files
                                                                             */
                                                                            slVisitsUploadRepo.findAllByVisitIdAndDocumentTypeIsNotNullOrderById(reportEntity.id ?: -1L)
                                                                                .let { uploadedFiles ->
                                                                                    req.attributes()["uploadedFiles"] = uploadedFiles
                                                                                }

                                                                        }
                                                                        ?: run { req.attributes()["visitReport"] = StandardLevyFactoryVisitReportEntity() }

                                                                }
                                                                ?: throw InvalidInputException("Empty entry_number not allowed")

                                                            req.attributes()["manufacturer"] = manufacturer
                                                            req.attributes()["counties"] = commonDaoServices.findCountyListByStatus(map.activeStatus)
                                                            req.attributes()["towns"] = townsRepo.findByStatusOrderByTown(map.activeStatus)
//                                                            req.attributes()["companyProfile"] = CompanyProfileEntity()
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
                        factoryVisitReportEntity.status = 0
                        factoryVisitReportEntity.assistantManagerApproval = 0
                        factoryVisitReportEntity.managersApproval = 0
                        factoryVisitReportEntity.scheduledVisitDate = manufacturerDetails.factoryVisitDate as Date
                        factoryVisitReportEntity.createdBy = commonDaoServices.checkLoggedInUser()
                        factoryVisitReportEntity.createdOn = commonDaoServices.getTimestamp()
                        val savedReport = standardLevyFactoryVisitReportRepo.save(factoryVisitReportEntity)
                        KotlinLogging.logger { }.info("New id ${savedReport.id}")
                        standardsLevyBpmn.startSlSiteVisitProcess(savedReport.id ?: throw NullValueNotAllowedException("Id should not be null"), commonDaoServices.getLoggedInUser()?.id ?: throw Exception("Please login"))
                        standardsLevyBpmn.slSvQueryManufacturerDetailsComplete(savedReport.id ?: throw NullValueNotAllowedException("Id should not be null"),manufacturerId)
                        standardsLevyBpmn.slSvScheduleVisitComplete(savedReport.id ?: throw NullValueNotAllowedException("Id should not be null"))
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
            }
            ?: throw ServiceMapNotFoundException("Missing application mapping for [id=$appId], recheck configuration")

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
                                                    standardsLevyBpmn.slSvQueryManufacturerDetailsComplete(visit.id ?: throw NullValueNotAllowedException("Id should not be null"),manufacturer.id.toString())
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

    fun actionSaveFactoryVisitReport(req: ServerRequest): ServerResponse {
        commonDaoServices.getLoggedInUser().let { user ->
            val body = req.body<StandardLevyFactoryVisitReportEntity>()
            req.pathVariable("manufacturerId").toLongOrNull()?.let { manufacturerId ->
                standardLevyFactoryVisitReportRepo.findByManufacturerEntity(manufacturerId)?.let { report ->
                    report.purpose = body.purpose
                    report.personMet = body.personMet
                    report.actionTaken = body.actionTaken
                    report.remarks = body.remarks
                    standardLevyFactoryVisitReportRepo.save(report)
                    return ok().render(
                        "redirect:/sl/manufacturer?manufacturerId=${manufacturerId}",
                        req.attributes()
                    )
                }
            } ?: throw InvalidInputException("Please login")
        }
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

class SlTasksEntityDto(
    val manufacturerId: Long?,
    val assignedDate: java.util.Date?,
    val dueDate: java.util.Date?,
    val taskId: String?,
    val name: String?,
)

