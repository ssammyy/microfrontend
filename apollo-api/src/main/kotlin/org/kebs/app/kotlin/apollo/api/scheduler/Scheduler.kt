package org.kebs.app.kotlin.apollo.api.scheduler

import mu.KotlinLogging
import org.joda.time.DateTime
import org.kebs.app.kotlin.apollo.api.ports.provided.scheduler.SchedulerImpl
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
@Configuration
@EnableScheduling
class Scheduler (
        private val schedulerImpl: SchedulerImpl
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

        if (runSendNotifications.toInt()==1){
            schedulerImpl.sendNotifications(currentDate)
        }

        if (runMsOverdueTaskNotifications.toInt()==1) {
            schedulerImpl.sendOverdueTaskNotifications(currentDate,msMarketSurveillancePrefix)
        }
    }

    @Scheduled(fixedRate = 100 * 1000)
    fun runSchedulerAfterEveryFiveMin() {
        schedulerImpl.updatePaidDemandNotesStatus()
        schedulerImpl.assignPermitApplicationAfterPayment()
    }

//    @Scheduled(fixedRate = 30 * 60000)
//    fun runSchedulerAfterEvery30Min() {
//
//    }
}
