package org.kebs.app.kotlin.apollo.api.ports.provided.bpmn

import mu.KotlinLogging
import org.flowable.engine.delegate.DelegateExecution
import org.flowable.task.service.delegate.DelegateTask
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.QADaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.scheduler.SchedulerImpl
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component


@Component
class BpmnNotifications(
    private val notifications: Notifications,
    private val schedulerImpl: SchedulerImpl,
    private val qaDaoServices: QADaoServices
)
{

    @Value("\${bpmn.candidate.group.pcm}")
    lateinit var pcmCandidateGroup: String

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

    fun sendEmailToCandidateGroup(candidateGroup: String) {
        KotlinLogging.logger { }.info("Submitting Emails to Candidate Group: $candidateGroup............")
        when (candidateGroup) {
            pcmCandidateGroup -> {
                val pcmUserList = qaDaoServices.findAllPcmOfficers()
                for (user in pcmUserList) {
                    user.email?.let { this.sendEmail(it) }
                }
            }
        }
    }

}
