/*
 *
 * $$$$$$$\   $$$$$$\  $$\   $$\        $$$$$$\  $$\       $$$$$$\  $$$$$$$\   $$$$$$\  $$\
 * $$  __$$\ $$  __$$\ $$ | $$  |      $$  __$$\ $$ |     $$  __$$\ $$  __$$\ $$  __$$\ $$ |
 * $$ |  $$ |$$ /  \__|$$ |$$  /       $$ /  \__|$$ |     $$ /  $$ |$$ |  $$ |$$ /  $$ |$$ |
 * $$$$$$$\ |\$$$$$\  $$$$$  /        $$ |$$$$\ $$ |     $$ |  $$ |$$$$$$$\ |$$$$$$$$ |$$ |
 * $$  __$$\  \____$$\ $$  $$<         $$ |\_$$ |$$ |     $$ |  $$ |$$  __$$\ $$  __$$ |$$ |
 * $$ |  $$ |$$\   $$ |$$ |\$\        $$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |$$ |  $$ |$$ |
 * $$$$$$$  |\$$$$$  |$$ | \$\       \$$$$$  |$$$$$$$$\ $$$$$$  |$$$$$$$  |$$ |  $$ |$$$$$$$$\
 * \_______/  \______/ \__|  \__|       \______/ \________|\______/ \_______/ \__|  \__|\________|
 * $$$$$$$$\ $$$$$$$$\  $$$$$$\  $$\   $$\ $$\   $$\  $$$$$$\  $$\       $$$$$$\   $$$$$$\  $$$$$$\ $$$$$$$$\  $$$$$$\
 * \__$$  __|$$  _____|$$  __$$\ $$ |  $$ |$$$\  $$ |$$  __$$\ $$ |     $$  __$$\ $$  __$$\ \_$$  _|$$  _____|$$  __$$\
 *    $$ |   $$ |      $$ /  \__|$$ |  $$ |$$$$\ $$ |$$ /  $$ |$$ |     $$ /  $$ |$$ /  \__|  $$ |  $$ |      $$ /  \__|
 *    $$ |   $$$$$\    $$ |      $$$$$$$$ |$$ $$\$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |$$$$\   $$ |  $$$$$\    \$$$$$\
 *    $$ |   $$  __|   $$ |      $$  __$$ |$$ \$$$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |\_$$ |  $$ |  $$  __|    \____$$\
 *    $$ |   $$ |      $$ |  $$\ $$ |  $$ |$$ |\$$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |  $$ |  $$ |      $$\   $$ |
 *    $$ |   $$$$$$$$\ \$$$$$  |$$ |  $$ |$$ | \$ | $$$$$$  |$$$$$$$$\ $$$$$$  |\$$$$$  |$$$$$$\ $$$$$$$$\ \$$$$$  |
 *    \__|   \________| \______/ \__|  \__|\__|  \__| \______/ \________|\______/  \______/ \______|\________| \______/
 *
 *
 *
 *
 *
 *   Copyright (c) 2020.  BSK
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.kebs.app.kotlin.apollo.ipc.adaptor.workflows.service.notifications

import freemarker.template.Configuration
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.common.exceptions.MissingAttachmentException
import org.kebs.app.kotlin.apollo.config.properties.notifs.NotifsProperties
import org.kebs.app.kotlin.apollo.store.model.NotificationsBufferEntity
import org.kebs.app.kotlin.apollo.store.model.ServiceRequestsEntity
import org.kebs.app.kotlin.apollo.store.repo.INotificationsBufferRepository
import org.kebs.app.kotlin.apollo.store.repo.INotificationsRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.repository.findByIdOrNull
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import java.io.File
import java.util.*

@Service
class SendEmailService(

        private val notifs: NotifsProperties,
        private val utils: NotificationUtils,
        private val notificationsRepo: INotificationsRepository,
        private val buffersRepo: INotificationsBufferRepository,
        @Qualifier("getFreeMarkerConfiguration")
        private val templatesConfig: Configuration
) {

    fun sendEmail(buffer: NotificationsBufferEntity?) {
        notificationsRepo.findByIdOrNull(buffer?.notificationId)
                ?.let { notif ->

                    val attachmentFile: File?
                    try {
                        val mailSender = JavaMailSenderImpl()
                        mailSender.host = notifs.host
                        mailSender.port = notifs.port
                        mailSender.username = notifs.username
                        mailSender.password = notifs.password
                        val props = Properties()
                        props[notifs.mailTransportProtocolKey] = notifs.mailTransportProtocol
                        props[notifs.mailStmpAuthKey] = notifs.mailStmpAuth
                        props[notifs.mailStmpStartTlsKey] = notifs.mailStmpStartTls
                        props[notifs.mailStmpDebugKey] = notifs.mailStmpDebug
                        mailSender.javaMailProperties = props

                        val mailMessage = mailSender.createMimeMessage()
                        val helper = MimeMessageHelper(mailMessage, true)

                        /**
                         * To be set again when templates are confirmed
                         */
//            val payload = JSONObject()
//            notification = FreeMarkerTemplateUtils.processTemplateIntoString(templatesConfig.getTemplate("general-notification.ftl"), payload.toMap())

                        helper.setFrom(notifs.username)
                        helper.setTo(buffer?.recipient!!)
                        helper.setSubject(buffer.subject!!)
                        helper.setText(buffer.messageBody!!, true)
                        when {
                            !buffer.attachment.isNullOrBlank() -> {
                                attachmentFile = File(buffer.attachment!!)
                                when (attachmentFile.exists()) {
                                    true -> helper.addAttachment(buffer.attachment!!, attachmentFile)
                                    else -> throw MissingAttachmentException("Email attachment missing path=[${buffer.attachment}]")
                                }
                            }
                        }
                        mailSender.send(mailMessage)

                        buffer.responseMessage = "Email sent successfully"



                    } catch (e: Exception) {
                        KotlinLogging.logger { }.error(e.message)
                        KotlinLogging.logger { }.debug(e.message, e)
                        buffer?.responseMessage = e.message

                    }

                }
        buffer?.let { buffersRepo.save(it) }


    }


    fun sendMail(requestsEntity: ServiceRequestsEntity, notificationsBuffer: NotificationsBufferEntity): String {

        val attachmentFile: File?
        val notification = utils.stageEmailNotification(notificationsBuffer, requestsEntity)

        try {
            val mailSender = JavaMailSenderImpl()
            mailSender.host = notifs.host
            mailSender.port = notifs.port
            mailSender.username = notifs.username
            mailSender.password = notifs.password
            val props = Properties()
            props[notifs.mailTransportProtocolKey] = notifs.mailTransportProtocol
            props[notifs.mailStmpAuthKey] = notifs.mailStmpAuth
            props[notifs.mailStmpStartTlsKey] = notifs.mailStmpStartTls
            props[notifs.mailStmpDebugKey] = notifs.mailStmpDebug
            mailSender.javaMailProperties = props

            val mailMessage = mailSender.createMimeMessage()
            val helper = MimeMessageHelper(mailMessage, true)

            /**
             * To be set again when templates are confirmed
             */
//            val payload = JSONObject()
//            notification = FreeMarkerTemplateUtils.processTemplateIntoString(templatesConfig.getTemplate("general-notification.ftl"), payload.toMap())

            helper.setFrom(notifs.username)
            helper.setTo(notificationsBuffer.recipient!!)
            helper.setSubject(notificationsBuffer.subject!!)
            helper.setText(notification, true)
            if (notificationsBuffer.attachment != null && notificationsBuffer.attachment != "") {
                attachmentFile = File(notificationsBuffer.attachment!!)
                when (attachmentFile.exists()) {
                    true -> helper.addAttachment(notificationsBuffer.varField2!!, attachmentFile)
                    else -> throw RuntimeException("Email attachment is missing its name.")
                }
            }
            mailSender.send(mailMessage)

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            throw RuntimeException("Exception occured while sending notifs notification ${e.message}")
        }
        return "1"
    }
}
