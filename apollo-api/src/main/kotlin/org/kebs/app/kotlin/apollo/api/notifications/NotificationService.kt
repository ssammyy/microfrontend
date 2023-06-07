package org.kebs.app.kotlin.apollo.api.notifications


import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.sms.ISmsService
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.repo.INotificationTypesRepository
import org.kebs.app.kotlin.apollo.store.repo.INotificationsRepository
import org.springframework.expression.spel.SpelParserConfiguration
import org.springframework.expression.spel.support.StandardEvaluationContext
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.thymeleaf.templateresolver.StringTemplateResolver
import java.util.*
import javax.activation.DataHandler
import javax.activation.FileDataSource
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart


@Service("notificationService")
class NotificationService(
        val applicationMapProperties: ApplicationMapProperties,
        private val notificationTypesRepository: INotificationTypesRepository,
        private val notificationRepository: INotificationsRepository,
        private val smsService: ISmsService,
) {
    val templateEngine: TemplateEngine = TemplateEngine()

    fun parseString(data: Map<String, Any>, template: String): String {
        val stringTemplateResolver = StringTemplateResolver()
        templateEngine.setTemplateResolver(stringTemplateResolver)
        val ctx = Context(Locale.UK)
        if (!data.isEmpty()) {
            for ((k, v) in data) {
                ctx.setVariable(k, v)
            }
        }
        return templateEngine.process(template, ctx);
    }

    fun sendEmail(emailAddress: String, notificationCode: String, data: Map<String, Any>, attachments: List<String>?=null) {
        val notType = notificationTypesRepository.findByTypeCode(NotificationTypeCodes.EMAIL.name)
        if (notType.isPresent) {
            val notification = notificationRepository.findFirstByNotificationTypeAndReferenceNameOrderByNotificationType(notType.get(), notificationCode)
            if (notification.isPresent) {
                // Parse email template
                val templateStr = this.parseString(data, notification.get().textTemplate ?: "")
                // Deliver email
                this.processEmail(emailAddress,
                        parseString(data, notification.get().subject ?: notificationCode),
                        templateStr, attachments)

            }
        }
    }

    fun sendSmsMessage(phoneNumber: String, notificationCode: String, data: Map<String, Any>) {
        val type = notificationTypesRepository.findByTypeCode(NotificationTypeCodes.SMS.name)
        if (type.isPresent) {
            val notification = notificationRepository.findFirstByNotificationTypeAndReferenceNameOrderByNotificationType(type.get(), notificationCode)
            if (notification.isPresent) {
                // Parse SMS template
                val smsMessage = this.parseString(data, notification.get().textTemplate ?: notificationCode)
                // Deliver SMS
                this.smsService.sendSms(phoneNumber, smsMessage)
            }
        }
    }

    fun processEmail(recipientEmail: String, subject: String, messageText: String, attachments: List<String>?) {
        KotlinLogging.logger { }.info("Sending email to $recipientEmail")

        val props = Properties()
        props.put("mail.smtp.starttls.enable", applicationMapProperties.mapApplicationEmailSmtpStartTlsEnable)
        props.put("mail.smtp.host", applicationMapProperties.mapApplicationEmailSmtpHost)
        props.put("mail.smtp.port", applicationMapProperties.mapApplicationEmailSmtpPort)
        props.put("mail.smtp.ssl.trust", applicationMapProperties.mapApplicationEmailSmtpHost)
        props.put("mail.smtp.auth", applicationMapProperties.mapApplicationEmailSmtpAuth)
        props.put("mail.smtp.user", applicationMapProperties.mapApplicationEmailUsername)
        props.put(
                "mail.smtp.password", applicationMapProperties.mapApplicationEmailPassword
        )

        //Establishing a session with required user details
        val session: Session = Session.getInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                //return new PasswordAuthentication(username, password);
                return PasswordAuthentication(
                        applicationMapProperties.mapApplicationEmailUsername,
                        applicationMapProperties.mapApplicationEmailPassword
                )
            }
        })

        try {
            val msg = MimeMessage(session)
            //String to = recepient;
            val address: Array<InternetAddress> = InternetAddress.parse(recipientEmail, true)
            msg.setRecipients(Message.RecipientType.TO, address)
            msg.subject = subject
            msg.sentDate = Date()
            msg.setHeader("XPriority", "1")
            msg.setFrom(InternetAddress(applicationMapProperties.mapApplicationEmailUsername, applicationMapProperties.mapApplicationEmailPassword))
            //val messageText = message
            var messageBodyPart: BodyPart = MimeBodyPart()
            messageBodyPart.setText(messageText)
            val multipart: Multipart = MimeMultipart()
            multipart.addBodyPart(messageBodyPart)
            attachments?.let { filepaths ->
                filepaths.forEach { fileName ->
                    messageBodyPart = MimeBodyPart()
                    val source = FileDataSource(fileName)
                    messageBodyPart.dataHandler = DataHandler(source)
                    messageBodyPart.fileName = source.name
                    multipart.addBodyPart(messageBodyPart)
                }
            }
            msg.setContent(multipart)
            val transport: Transport = session.getTransport(applicationMapProperties.mapApplicationEmailProtocol)
            transport.connect(applicationMapProperties.mapApplicationEmailSmtpHost, applicationMapProperties.mapApplicationEmailUsername,
                    applicationMapProperties.mapApplicationEmailPassword)
            //Add mail delivery check, update status if mail failed
            transport.sendMessage(msg, msg.allRecipients)
            KotlinLogging.logger { }.info("Mail has been sent successfully")
        } catch (ex: Exception) {
            println("Unable to send an email: $ex")
            KotlinLogging.logger { }.error("Unable to send an email : " + ex.message, ex)
        }
    }
}