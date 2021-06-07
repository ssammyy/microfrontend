package org.kebs.app.kotlin.apollo.api.notifications

import mu.KotlinLogging
import org.flowable.engine.RepositoryService
import org.flowable.engine.delegate.DelegateExecution
import org.flowable.task.api.Task
import org.flowable.task.service.delegate.DelegateTask
import org.jasypt.encryption.StringEncryptor
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.BpmnCommonFunctions
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.repo.IManufacturerRepository
import org.kebs.app.kotlin.apollo.store.repo.INotificationsRepository
import org.kebs.app.kotlin.apollo.store.repo.IUserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.io.UnsupportedEncodingException
import java.lang.Double
import java.util.*
import javax.activation.DataHandler
import javax.activation.DataSource
import javax.activation.FileDataSource
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart


@Component
class Notifications(
        private val repositoryService: RepositoryService,
        private val userRepo: IUserRepository,
        val manufacturerRepo: IManufacturerRepository,
        val notificationsRepo: INotificationsRepository,
        val bpmnCommonFunctions: BpmnCommonFunctions,
        val applicationMapProperties: ApplicationMapProperties,
        val jasyptStringEncryptor: StringEncryptor
)
{
//    @Value("\${qa.bpmn.email.smtpStartTlsEnable}")
//    lateinit var smtpStartTlsEnable: String
//    @Value("\${qa.bpmn.email.smtpHost}")
//    lateinit var smtpHost: String
//    @Value("\${qa.bpmn.email.smtpPort}")
//    lateinit var smtpPort: String
//    @Value("\${qa.bpmn.email.smtpAuth}")
//    lateinit var smtpAuth: String
//    @Value("\${qa.bpmn.email.username}")
//    lateinit var smtpUsername: String
//    @Value("\${qa.bpmn.email.password}")
//    lateinit var smtpPassword: String
//    @Value("\${qa.bpmn.email.protocol}")
//    lateinit var protocol: String
    //@Value("\${qa.bpmn.email.default.subject}")
    //lateinit var emailDefaultSubject: String
    //@Value("\${qa.bpmn.email.default.body}")
    //lateinit var emailDefaultMessage: String

    @Value("\${bpmn.notification.default.id}")
    lateinit var defaultEmailNotificationId: String


    fun sendEmail(delegateTask: DelegateTask?){
        val task:Task? = delegateTask as Task
        buildEmail(task, defaultEmailNotificationId.toInt(), null, null)
    }

    fun sendEmail(delegateExecution: DelegateExecution, notificationId: Int, parameter1: String?){
        bpmnCommonFunctions.getTaskByProcessInstanceId(delegateExecution.processInstanceId)?.let { task ->
            try{
                val num = Double.parseDouble(parameter1)
                parameter1?.let{
                    buildEmail(task, notificationId, null, parameter1.toLong())
                }
            } catch (e: Exception){
                buildEmail(task, notificationId, parameter1, null)
            }
        }
    }

    fun sendEmail(recipientEmail: String, subject: String, messageText: String, attachmentFilePath: String? = null){
        processEmail(recipientEmail, subject, messageText, null)
    }

    fun sendEmailServiceTask(recipientEmail: String, notificationId: Int){
        //get the notification
        notificationsRepo.findByIdOrNull(notificationId)?.let { notifEntity ->
            val message = notifEntity.description.toString().replace("\\n", System.getProperty("line.separator"), true)
            KotlinLogging.logger { }.info("Sending email below to $recipientEmail \n ${notifEntity.subject.toString()} \n $message")
            processEmail(recipientEmail, notifEntity.subject.toString(), message, null)
        }?: run{KotlinLogging.logger { }.info("No notification found with id $notificationId")}
    }

    fun sendEmailServiceTask(userId: Long, notificationId: Int) : Boolean{
        //get the notification
        notificationsRepo.findByIdOrNull(notificationId)?.let { notifEntity ->
            var message = notifEntity.description.toString().replace("\\n", System.getProperty("line.separator"), true)
            userRepo.findByIdOrNull(userId)?.let { usersEntity ->
                message = message.replace("<first_name>", usersEntity.firstName.toString(), true)
                message = message.replace("<last_name>", usersEntity.lastName.toString(), true)
                KotlinLogging.logger { }.info("Sending email below to ${usersEntity.email.toString()} \n ${notifEntity.subject.toString()} \n $message")
                processEmail(usersEntity.email.toString(), notifEntity.subject.toString(), message, null)
                return true
            }
        }?: run{KotlinLogging.logger { }.info("No notification found with id $notificationId")}
        return false
    }

    fun sendEmailServiceTask(userId: Long, assigneeId:Long, notificationId: Int, task: Task?, messageBody:String?) : Boolean{
        //get the notification
        notificationsRepo.findByIdOrNull(notificationId)?.let { notifEntity ->
            var message = notifEntity.description.toString().replace("\\n", System.getProperty("line.separator"), true)
            userRepo.findByIdOrNull(userId)?.let { usersEntity ->
                message = message.replace("<first_name>", usersEntity.firstName.toString(), true)
                message = message.replace("<last_name>", usersEntity.lastName.toString(), true)
                userRepo.findByIdOrNull(assigneeId)?.let { aUsersEntity ->
                    message = message.replace("<assignee_first_name>", aUsersEntity.firstName.toString(), true)
                    message = message.replace("<assignee_last_name>", aUsersEntity.lastName.toString(), true)
                }
                task?.let{ task->
                    message = message.replace("<task_name>", task.name, true)
                }
                messageBody?.let{ messageBody->
                    message = message.replace("<message>", messageBody, true)
                }
                KotlinLogging.logger { }.info("Sending email below to ${usersEntity.email.toString()} \n ${notifEntity.subject.toString()} \n $message")
                processEmail(usersEntity.email.toString(), notifEntity.subject.toString(), message, null)
                return true
            }
        }?: run{KotlinLogging.logger { }.info("No notification found with id $notificationId")}
        return false
    }

    fun buildEmail(task: Task?, notificationId: Int, emailAddress: String?, userId: Long?){
        //var finalEmailMessage = ""
        var message = ""
        var finalEmailAddress = ""
        var firstName=""
        var lastName = ""
        try{
            notificationsRepo.findByIdOrNull(notificationId)?.let { notifEntity ->
                message = notifEntity.description.toString().replace("\\n", System.getProperty("line.separator"), true)
                task?.let{ task ->
                    val variables:Map<String, Any>? = bpmnCommonFunctions.getTaskVariables(task.id)

                    emailAddress?.let{
                        finalEmailAddress = it
                    }?: run{
                        var assigneeId:Long = task.assignee.toLong()
                        userId?.let{
                            assigneeId = it
                        }
                        userRepo.findByIdOrNull(assigneeId)?.let { usersEntity ->
                            finalEmailAddress = usersEntity.email.toString()
                            firstName = usersEntity.firstName.toString()
                            lastName = usersEntity.lastName.toString()
                        }
                        message = message.replace("<first_name>", firstName, true)
                        message = message.replace("<last_name>", lastName, true)
                        message = message.replace("<task_name>", task.name, true)
                        repositoryService.createProcessDefinitionQuery().processDefinitionId(task.processDefinitionId).list()[0].let { processDefinition->
                            message = message.replace("<process_name>", processDefinition.name, true)
                        }
                    }
                    variables?.let{
                        it["paymentAmount"]?.let { taskVariable->
                            message = message.replace("<paymentAmount>", taskVariable.toString(), true)
                        }
                    }
                }
                KotlinLogging.logger { }.info("Sending email below to $finalEmailAddress \n $message")
                processEmail(finalEmailAddress, notifEntity.subject.toString(), message, null)
            }

        } catch (e: Exception){
            KotlinLogging.logger { }.error(e.message, e)
        }
    }

    fun processEmail(recipientEmail: String, subject: String, messageText: String, attachmentFilePath: String?){
        KotlinLogging.logger { }.info("Sending email to $recipientEmail")

        val props = Properties()
        props.put("mail.smtp.starttls.enable", applicationMapProperties.mapApplicationEmailSmtpStartTlsEnable)
        props.put("mail.smtp.host", applicationMapProperties.mapApplicationEmailSmtpHost);
        props.put("mail.smtp.port", applicationMapProperties.mapApplicationEmailSmtpPort);
        props.put("mail.smtp.auth", applicationMapProperties.mapApplicationEmailSmtpAuth);
        props.put("mail.smtp.user", applicationMapProperties.mapApplicationEmailUsername);
        props.put("mail.smtp.password", jasyptStringEncryptor.decrypt(applicationMapProperties.mapApplicationEmailPassword));

        //Establishing a session with required user details
        val session: Session = Session.getInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                //return new PasswordAuthentication(username, password);
                return PasswordAuthentication(applicationMapProperties.mapApplicationEmailUsername, jasyptStringEncryptor.decrypt(applicationMapProperties.mapApplicationEmailPassword))
            }
        })

        try {
            val msg = MimeMessage(session)
            //String to = recepient;
            val address: Array<InternetAddress> = InternetAddress.parse(recipientEmail, true)
            msg.setRecipients(Message.RecipientType.TO, address)
            msg.subject = subject
            msg.sentDate= Date()
            msg.setHeader("XPriority", "1")
            msg.setFrom(InternetAddress(applicationMapProperties.mapApplicationEmailUsername, jasyptStringEncryptor.decrypt(applicationMapProperties.mapApplicationEmailPassword)))
            //val messageText = message
            var messageBodyPart: BodyPart = MimeBodyPart()
            messageBodyPart.setText(messageText)
            val multipart: Multipart = MimeMultipart()
            multipart.addBodyPart(messageBodyPart)
            attachmentFilePath?.let{ filepath->
                if(!filepath.isBlank()){
                    // Add the attachment
                    messageBodyPart = MimeBodyPart()
                    val source = FileDataSource(filepath)
                    messageBodyPart.dataHandler = DataHandler(source)
                    messageBodyPart.fileName = source.name
                    multipart.addBodyPart(messageBodyPart)
                }
            }
            msg.setContent(multipart)
            val transport: Transport = session.getTransport(applicationMapProperties.mapApplicationEmailProtocol)
            transport.connect(applicationMapProperties.mapApplicationEmailSmtpHost, applicationMapProperties.mapApplicationEmailUsername,
                jasyptStringEncryptor.decrypt(applicationMapProperties.mapApplicationEmailPassword))
            //TODO: Add mail delivery check, update status if mail failed
            transport.sendMessage(msg, msg.allRecipients)
            KotlinLogging.logger { }.info("Mail has been sent successfully")
        } catch (ex: Exception) {
            println("Unable to send an email: $ex")
            KotlinLogging.logger { }.error("Unable to send an email : " + ex.message, ex)
        }
//        } catch (e: UnsupportedEncodingException) {
//            KotlinLogging.logger { }.error(e.message, e)
//        }
    }

}
