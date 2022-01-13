package org.kebs.app.kotlin.apollo.api.ports.provided.scheduler

import mu.KotlinLogging
import org.joda.time.DateTime
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.BpmnCommonFunctions
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.DestinationInspectionBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.QualityAssuranceBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.NewMarketSurveillanceDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.QADaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.lims.LimsServices
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.SchedulerEntity
import org.kebs.app.kotlin.apollo.store.repo.ICompanyProfileRepository
import org.kebs.app.kotlin.apollo.store.repo.ISchedulerRepository
import org.kebs.app.kotlin.apollo.store.repo.IUserRepository
import org.kebs.app.kotlin.apollo.store.repo.ms.IFuelInspectionRepository
import org.kebs.app.kotlin.apollo.store.repo.qa.IPermitApplicationsRepository
import org.kebs.app.kotlin.apollo.store.repo.qa.IQaSampleSubmissionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Lazy
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp
import java.time.Instant
import java.util.*

@Service
class SchedulerImpl(
        private val schedulerRepo: ISchedulerRepository,
        private val notifications: Notifications,
        private val bpmnCommonFunctions: BpmnCommonFunctions,
        private val userRepo: IUserRepository,
        private val companyRepo: ICompanyProfileRepository,
        private val diBpmn: DestinationInspectionBpmn,
        private val applicationMapProperties: ApplicationMapProperties,
//    private val qualityAssuranceBpmn: QualityAssuranceBpmn,
        private val sampleSubmissionRepo: IQaSampleSubmissionRepository,
        private val limsServices: LimsServices,
//    private val qaDaoServices: QADaoServices,
//    private val diDaoServices: DestinationInspectionDaoServices,
        private val commonDaoServices: CommonDaoServices,
) {

    @Lazy
    @Autowired
    lateinit var diDaoServices: DestinationInspectionDaoServices

    @Autowired
    lateinit var qaDaoServices: QADaoServices

    @Autowired
    lateinit var marketSurveillanceDaoServices: NewMarketSurveillanceDaoServices

    @Autowired
    lateinit var permitRepo: IPermitApplicationsRepository

    @Autowired
    lateinit var fuelInspectionRepo: IFuelInspectionRepository

    final val diAppId = applicationMapProperties.mapImportInspection

    final val qaAppId = applicationMapProperties.mapQualityAssurance

    @Value("\${scheduler.ms.director.id}")
    lateinit var msDirectorId: String

    @Value("\${scheduler.ms.overdue.tasks.notification.id}")
    lateinit var overdueTasksNotificationId: String

    private val defaultTaskType = "NOTIFICATION"
    private val successMessage = "Success"
    private val notificationNotFoundMessage = "Notification <ID> not found"
    private val defaultStatus = 1
    fun createScheduledAlert(processInstanceId: String,
                             taskType: String?,
                             taskId: String,
                             assigneeId: Long,
                             processName: String?,
                             interval: Int,  //interval in days
                             count: Int,  //Number of notifications to be sent
                             notificationId: Int,
                             taskDefinitionKey: String
    ): Boolean {
        KotlinLogging.logger { }.info("Creating scheduled alerts entry for PID $processInstanceId")
        try {
            if (interval > 0 && count > 0) {
                var i = 0
                var schedDate = DateTime.now().toDate()
                while (i < count) {
                    schedDate = DateTime(schedDate).plusDays(interval).toDate()
                    val sched = SchedulerEntity()
                    sched.taskType = defaultTaskType
                    taskType?.let {
                        sched.taskType = taskType
                    }
                    sched.userId = assigneeId
                    sched.scheduledDate = Timestamp.from(schedDate.toInstant())
                    sched.createdOn = Timestamp.from(Instant.now())
                    sched.processInstanceId = processInstanceId
                    sched.taskId = taskId
                    sched.status = 1
                    taskDefinitionKey.let {
                        sched.taskDefinitionKey = taskDefinitionKey
                    }
                    sched.notificationId = notificationId
                    schedulerRepo.save(sched)
                    i++
                }

            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return false
    }

    fun createScheduledAlert(notificationId: Int, scheduledDate: Date, assigneeId: Long
    ): Boolean {
        KotlinLogging.logger { }.info("Creating scheduled alerts on $scheduledDate")
        try {
            val sched = SchedulerEntity()
            sched.taskType = defaultTaskType
            sched.userId = assigneeId
            sched.scheduledDate = Timestamp.from(scheduledDate.toInstant())
            sched.createdOn = Timestamp.from(Instant.now())
            sched.status = 1
            sched.notificationId = notificationId
            schedulerRepo.save(sched)
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return false
    }

    fun cancelScheduledAlert(processInstanceId: String, taskDefinitionKey: String): Boolean {
        KotlinLogging.logger { }.info("Cancel scheduled alerts entry for $processInstanceId -- $taskDefinitionKey")
        try {
            schedulerRepo.findByProcessInstanceIdAndTaskDefinitionKeyAndStatus(processInstanceId, taskDefinitionKey, 1)?.let { lstSchedulerEntity ->
                for (sched in lstSchedulerEntity) {
                    sched.status = 0
                    sched.cancelledOn = Timestamp.from(Instant.now())
                    schedulerRepo.save(sched)
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return false
    }

    fun sendNotifications(dateParam: Date): Boolean {
        KotlinLogging.logger { }.info("Sending out alerts for $dateParam")
        try {
            val startDate = DateTime(dateParam)
                    .withHourOfDay(0)
                    .withMinuteOfHour(0)
                    .withSecondOfMinute(0).toDate()
            val endDate = DateTime(dateParam)
                    .withHourOfDay(23)
                    .withMinuteOfHour(59)
                    .withSecondOfMinute(59).toDate()
            KotlinLogging.logger { }.info("Searching for messages between $startDate and $endDate ..............")
            schedulerRepo.findByScheduledDateBetweenAndStatus(Timestamp.from(startDate.toInstant()), Timestamp.from(endDate.toInstant()), 1)?.let { lstSchedulerEntity ->
                for (sched in lstSchedulerEntity) {
                    //Build the notification
                    sched.userId?.let { userId ->
                        sched.notificationId?.let { notificationId ->
                            notifications.sendEmailServiceTask(userId, notificationId).let { result ->
                                if (result) {
                                    sched.result = successMessage
                                    sched.status = 0
                                    sched.executedOn = Timestamp.from(Instant.now())
                                    schedulerRepo.save(sched)
                                } else {
                                    sched.result = notificationNotFoundMessage
                                    sched.status = 0
                                    sched.executedOn = Timestamp.from(Instant.now())
                                    schedulerRepo.save(sched)
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return false
    }

    fun sendOverdueTaskNotifications(dateParam: Date, processDefinitionKeyPrefix: String): Boolean {
        KotlinLogging.logger { }.info("Sending out ms overdue task  notifications for $dateParam")
        var message = ""
        try {
            KotlinLogging.logger { }.info("Fetching ms overdue tasks for $dateParam")
            bpmnCommonFunctions.getOverdueTasks(dateParam, processDefinitionKeyPrefix)?.let { tasks ->
                for (task in tasks) {
                    bpmnCommonFunctions.getTaskVariables(task.id)?.let { taskVariable ->
                        task.let { task ->
                            userRepo.findByIdOrNull(task.assignee.toLong())?.let { usersEntity ->
                                message += "${task.name} assigned to ${usersEntity.firstName} ${usersEntity.lastName} due on ${task.dueDate}\n"
                            }
                        }
                    }
                }
                KotlinLogging.logger { }.info("Completed fetching ms overdue tasks for $dateParam")
                notifications.sendEmailServiceTask(msDirectorId.toLong(), 0, overdueTasksNotificationId.toInt(), null, message)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return false
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updatePaidDemandNotesStatus(): Boolean {
        val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
        val paidDemandNotesList = diDaoServices.findAllDemandNotesWithSwPending(map.activeStatus)
        if (paidDemandNotesList.isEmpty()) {
            return true
        }
        KotlinLogging.logger { }.info("Found ${paidDemandNotesList.size} demand notes with paid status")
        //If list is not empty
        for (demandNote in paidDemandNotesList) {
            diBpmn.triggerDemandNotePaidBpmTask(demandNote)
        }
        return true
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updateFirmTypeStatus() {

        val cps = companyRepo.findAll()
        for (cp in cps) {
            try {
                val firmType = qaDaoServices.manufactureType(
                        cp.yearlyTurnover ?: throw NullValueNotAllowedException("Invalid Record")
                ).id
                cp.firmCategory = firmType
                companyRepo.save(cp)
            } catch (e: Exception) {
                KotlinLogging.logger { }.error(e.message)
                KotlinLogging.logger { }.debug(e.message, e)

                continue
            }
        }


    }


    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updateLabResultsWithDetails() {
        val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
        //Find all Sample with Lab results inactive
        KotlinLogging.logger { }.info { "::::::::::::::::::::::::STARTED LAB RESULTS SCHEDULER::::::::::::::::::" }
        var samples = 1
        val ssfFoundList = sampleSubmissionRepo.findByLabResultsStatus(map.inactiveStatus)
        if (ssfFoundList != null) {
            for (ssfFound in ssfFoundList) {
                try {
                    KotlinLogging.logger { }
                            .info { "::::::::::::::::::::::::SAMPLES WITH RESULTS FOUND = ${samples++}::::::::::::::::::" }
                    val bsNumber = ssfFound.bsNumber ?: throw Exception("DATA NOT FOUND")
                    when {
                        limsServices.mainFunctionLims(bsNumber) == true -> {
                            with(ssfFound) {
                                modifiedBy = "SYSTEM SCHEDULER"
                                modifiedOn = commonDaoServices.getTimestamp()
                                labResultsStatus = map.activeStatus
                                resultsDate = commonDaoServices.getCurrentDate()
                            }
                            sampleSubmissionRepo.save(ssfFound)
                            when {
                                ssfFound.permitRefNumber != null -> {
                                    qaDaoServices.findPermitWithPermitRefNumberLatest(
                                            ssfFound.permitRefNumber ?: throw Exception("PERMIT WITH REF NO, NOT FOUND")
                                    )
                                        .let { pm ->
                                            with(pm) {
                                                userTaskId = applicationMapProperties.mapUserTaskNameQAO
                                                permitStatus = applicationMapProperties.mapQaStatusPLABResultsCompletness
                                                modifiedBy = "SYSTEM SCHEDULER"
                                                modifiedOn = commonDaoServices.getTimestamp()
                                            }
                                            permitRepo.save(pm)
                                            .let { pm ->
                                                with(pm) {
                                                    userTaskId = applicationMapProperties.mapUserTaskNameQAO
                                                    permitStatus =
                                                            applicationMapProperties.mapQaStatusPLABResultsCompletness
                                                    modifiedBy = "SYSTEM SCHEDULER"
                                                    modifiedOn = commonDaoServices.getTimestamp()
                                                }
                                                permitRepo.save(pm)

                                                qaDaoServices.sendEmailWithLabResultsFound(
                                                        commonDaoServices.findUserByID(
                                                                pm.qaoId ?: throw Exception("QAO ID, NOT FOUND")
                                                        ).email ?: throw Exception("EMAIL FOR QAO, NOT FOUND"),
                                                        pm.permitRefNumber
                                                                ?: throw Exception("PERMIT WITH REF NO, NOT FOUND")
                                                )
                                            }
                                }
                                }
                                ssfFound.cdItemId != null -> {
                                    diDaoServices.findItemWithItemID(
                                            ssfFound.cdItemId ?: throw Exception("CD ITEM ID NOT FOUND")
                                    )
                                            .let { cdItem ->
                                                diDaoServices.findCD(
                                                        cdItem.cdDocId?.id ?: throw Exception("CD ID NOT FOUND")
                                                )
                                                        .let { updatedCDDetails ->
                                                            updatedCDDetails.cdStandard?.let { cdStd ->
                                                                diDaoServices.updateCDStatus(
                                                                        cdStd,
                                                                        applicationMapProperties.mapDIStatusTypeInspectionSampleResultsReceivedId
                                                                )
                                                            }
                                                        }

                                        }
                                }
                                ssfFound.fuelInspectionId != null -> {
                                    marketSurveillanceDaoServices.findFuelInspectionDetailByID(ssfFound.fuelInspectionId?: throw Exception("FUEL INSPECTION ID NOT FOUND"))
                                        .let {  fuelInspection->
                                            with(fuelInspection){
                                                userTaskId = applicationMapProperties.mapMSUserTaskNameOFFICER
                                                lastModifiedBy = "SYSTEM SCHEDULER"
                                                lastModifiedOn = commonDaoServices.getTimestamp()
                                            }
                                            fuelInspectionRepo.save(fuelInspection)
                                        }
                                }
                                }
                            }


                        }

                } catch (e: Exception) {
                    KotlinLogging.logger { }.error(e.message)
                    KotlinLogging.logger { }.debug(e.message, e)

                    continue
                }

            }
        }
    }
}
