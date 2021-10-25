package org.kebs.app.kotlin.apollo.api.scheduler

import mu.KotlinLogging
import org.joda.time.DateTime
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.QADaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.scheduler.SchedulerImpl
import org.kebs.app.kotlin.apollo.api.ports.provided.scheduler.SftpSchedulerImpl
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled


@Configuration
@EnableScheduling
@Profile("prod")
class Scheduler(
        private val schedulerImpl: SchedulerImpl,
        private val sftpSchedulerImpl: SftpSchedulerImpl,
        private val qaDaoServices: QADaoServices
) {
    @Value("\${scheduler.run.send.notifications}")
    lateinit var runSendNotifications: String

    @Value("\${scheduler.run.ms.overdue.task.notifications}")
    lateinit var runMsOverdueTaskNotifications: String

    private val msMarketSurveillancePrefix = "msMarketSurveillance"

    @Scheduled(cron = "\${scheduler.cron.daily}")
    fun runDailyScheduler() {
        //KotlinLogging.logger { }.info("Now running scheduler.......")
        //Trigger notifications

        val currentDate = DateTime().toDate()

        if (runSendNotifications.toInt() == 1) {
            schedulerImpl.sendNotifications(currentDate)
        }

        if (runMsOverdueTaskNotifications.toInt() == 1) {
            schedulerImpl.sendOverdueTaskNotifications(currentDate, msMarketSurveillancePrefix)
        }
    }

    @Scheduled(fixedDelay = 60_000)//60 Seconds for now
    fun updateDemandNotes() {
        KotlinLogging.logger { }.debug("UPDATING DEMAND NOTES on SW")
        schedulerImpl.updatePaidDemandNotesStatus()
        KotlinLogging.logger { }.debug("UPDATED DEMAND NOTES on SW")
    }

    @Scheduled(fixedDelay = 100000)//1.6666667 Minutes for now
    fun runSchedulerAfterEveryFiveMin() {
        qaDaoServices.assignPermitApplicationAfterPayment()
        qaDaoServices.updatePermitWithDiscountWithPaymentDetails()
        schedulerImpl.updateLabResultsWithDetails()
        schedulerImpl.updateFirmTypeStatus()
    }

    @Scheduled(fixedDelay = 600000)
    @Profile("prod")
    fun fetchKeswsFiles() {
        sftpSchedulerImpl.downloadKeswsFiles()
    }

}

/**
 * Enabled only in dev environment
 */
@Configuration
@EnableScheduling
@Profile("default")
class SchedulerDevelopment(
        private val schedulerImpl: SchedulerImpl
) {
    @Scheduled(fixedDelay = 5_000)//60 Seconds for now
    fun updateDemandNotes() {
        KotlinLogging.logger { }.info("DEV: UPDATING DEMAND NOTES on SW")
        schedulerImpl.updatePaidDemandNotesStatus()
        KotlinLogging.logger { }.info("DEV: UPDATED DEMAND NOTES on SW")
    }
}