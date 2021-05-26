package org.kebs.app.kotlin.apollo.api.ports.provided.bpmn

import mu.KotlinLogging
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.engine.delegate.DelegateExecution
import org.flowable.task.service.delegate.DelegateTask
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.ports.provided.scheduler.SchedulerImpl
import org.kebs.app.kotlin.apollo.store.repo.IManufacturerRepository
import org.kebs.app.kotlin.apollo.store.repo.IUserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.io.UnsupportedEncodingException
import java.lang.Double.parseDouble
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart


@Component
class BpmnNotifications(
        private val notifications: Notifications,
        private val schedulerImpl: SchedulerImpl
)
{
    @Value("\${qa.bpmn.email.smtpStartTlsEnable}")
    lateinit var smtpStartTlsEnable: String
    @Value("\${qa.bpmn.email.smtpHost}")
    lateinit var smtpHost: String
    @Value("\${qa.bpmn.email.smtpPort}")
    lateinit var smtpPort: String
    @Value("\${qa.bpmn.email.smtpAuth}")
    lateinit var smtpAuth: String
    @Value("\${qa.bpmn.email.username}")
    lateinit var smtpUsername: String
    @Value("\${qa.bpmn.email.password}")
    lateinit var smtpPassword: String
    @Value("\${qa.bpmn.email.protocol}")
    lateinit var protocol: String
    @Value("\${qa.bpmn.email.default.subject}")
    lateinit var emailDefaultSubject: String
    @Value("\${qa.bpmn.email.default.body}")
    lateinit var emailDefaultMessage: String



    //TODO - This will be replaced with Ken's message sending function
    fun sendEmail() {
        KotlinLogging.logger { }.info("Now sending email.......")
    }

    fun sendEmail(email: String) {
        KotlinLogging.logger { }.info("Now sending email to $email.......")
    }

    fun sendEmailPlain(email: String) {
        KotlinLogging.logger { }.info("Now sending email to $email.......")
    }

    fun sendEmailServiceTask(email: String) {
        KotlinLogging.logger { }.info("Now sending email to $email.......")
    }

    fun sendEmailServiceTask(email: String, notificationId:Int) {
        notifications.sendEmailServiceTask(email,notificationId)
    }

    fun sendEmailServiceTask(userId: Long, notificationId: Int) {
        notifications.sendEmailServiceTask(userId,notificationId)
    }

    fun sendEmailServiceTask(userId: Long, notificationId: Int, isManufacturer:Boolean) {
        notifications.sendEmailServiceTask(userId,notificationId,isManufacturer)
    }

    fun sendEmailInvoice(email: String) {
        KotlinLogging.logger { }.info("Now sending email to $email.......")
    }

    fun sendEmail(email: String, manufacturerEmail: String) {
        KotlinLogging.logger { }.info("Now sending email to $email and $manufacturerEmail............")
    }

    fun sendEmail(delegateTask: DelegateTask?){
        notifications.sendEmail(delegateTask)
    }

    fun sendEmail(delegateExecution: DelegateExecution, notificationId: Int, parameter1:String){
        notifications.sendEmail(delegateExecution,notificationId,parameter1)
    }

    fun sendEmail(delegateExecution: DelegateExecution, notificationId: Int){
        notifications.sendEmail(delegateExecution,notificationId,null)
    }

    fun scheduleNotifications(delegateTask: DelegateTask?, notificationId:Int,interval:Int,count:Int){
        delegateTask?.let{ task->
            schedulerImpl.createScheduledAlert(
                    task.processInstanceId,
                    null,
                    task.id,
                    task.assignee.toLong(),
                    null,
                    interval,
                    count,
                    notificationId,
                    task.taskDefinitionKey
            )
        }

    }

    fun cancelScheduledNotifications(delegateTask: DelegateTask?){
        delegateTask?.let{ task->
            schedulerImpl.cancelScheduledAlert(
                    task.processInstanceId,
                    task.taskDefinitionKey
            )
        }

    }
}
