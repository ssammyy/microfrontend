package org.kebs.app.kotlin.apollo.api.scheduler

import mu.KotlinLogging
import org.joda.time.DateTime
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.InvoiceDaoService
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.MarketSurveillanceFuelDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.QADaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.StandardLevyService
import org.kebs.app.kotlin.apollo.api.ports.provided.kra.SendEntryNumberToKraServices
import org.kebs.app.kotlin.apollo.api.ports.provided.scheduler.SchedulerImpl
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled


@Configuration
@EnableScheduling
@Profile("prod")
class Scheduler(
    private val msDaoServices: MarketSurveillanceFuelDaoServices,
        private val schedulerImpl: SchedulerImpl,
        private val qaDaoServices: QADaoServices,
        private val invoiceDaoService: InvoiceDaoService
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

//    @Scheduled(fixedDelay = 60_000)//60 Seconds for now
    fun updateDemandNotes() {
        KotlinLogging.logger { }.debug("UPDATING DEMAND NOTES on SW")
        schedulerImpl.updatePaidDemandNotesStatus()
//        msDaoServices.updateRemediationDetailsAfterPaymentDone()
        KotlinLogging.logger { }.debug("UPDATED DEMAND NOTES on SW")
    }

    @Scheduled(fixedDelay = 60_000) //1 Minutes for now
    fun runSchedulerAfterEveryFiveMin() {
        invoiceDaoService.updateOfInvoiceTables()

        qaDaoServices.assignPermitApplicationAfterPayment()
        qaDaoServices.updatePermitWithDiscountWithPaymentDetails()
        schedulerImpl.updateLabResultsWithDetails()
        schedulerImpl.updateFirmTypeStatus()
    }

}

/**
 * Enabled only in dev environment
 */
@Configuration
@EnableScheduling
@Profile("prod")
class SchedulerDevelopment(
    private val schedulerImpl: SchedulerImpl,
    private val qaDaoServices: QADaoServices,
    private val standardLevyService: StandardLevyService,
    private val sendEntryNumberToKraServices : SendEntryNumberToKraServices,
    private val invoiceDaoService: InvoiceDaoService
) {
    @Scheduled(fixedDelay = 5_000)//60 Seconds for now
    fun updateDemandNotes() {
        invoiceDaoService.updateOfInvoiceTables()
        qaDaoServices.assignPermitApplicationAfterPayment()
        qaDaoServices.updatePermitWithDiscountWithPaymentDetails()
        schedulerImpl.updateLabResultsWithDetails()
        schedulerImpl.updateFirmTypeStatus()
        schedulerImpl.updatePaidDemandNotesStatus()
//        standardLevyService.sendLevyPaymentReminders()
        //   KotlinLogging.logger { }.info("DEV: UPDATED DEMAND NOTES on SW")
//        msDaoServices.updateRemediationDetailsAfterPaymentDone()
        KotlinLogging.logger { }.trace("DEV: UPDATED DEMAND NOTES on SW")
    }

    @Scheduled(cron = "\${scheduler.cron.monthly}")
    //@Scheduled(fixedDelay = 600_000) //3 Minutes for now
    fun runMonthlyScheduler() {
        standardLevyService.sendLevyPaymentReminders()
        sendEntryNumberToKraServices.postPenaltyDetailsToKra()
    }

    @Scheduled(cron = "\${scheduler.cron.daily}")
    fun runDailyScheduler() {
        schedulerImpl.updateOverDueTask()
    }
}
