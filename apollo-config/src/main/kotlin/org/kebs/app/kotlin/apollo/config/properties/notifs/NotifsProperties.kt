/*
 *
 *  *
 *  *
 *  *  *    Copyright (c) ${YEAR}.   BSK Global Technologies
 *  *  *
 *  *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *    you may not use this file except in compliance with the License.
 *  *  *    You may obtain a copy of the License at
 *  *  *
 *  *  *       http://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  *    Unless required by applicable law or agreed to in writing, software
 *  *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *   See the License for the specific language governing permissions and
 *  *  *   limitations under the License.
 *  *
 *
 */

package org.kebs.app.kotlin.apollo.config.properties.notifs

import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
@EncryptablePropertySource("file:\${CONFIG_PATH}/notifs.properties")
class NotifsProperties {
    @Value("\${org.kebs.app.kotlin.apollo.mail.host}")
    val host: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.mail.port}")
    val port: Int = 0

    @Value("\${org.kebs.app.kotlin.apollo.mail.username}")
    val username: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.mail.password}")
    val password: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.mail.transport.protocol.key}")
    val mailTransportProtocolKey: String = ""
    @Value("\${org.kebs.app.kotlin.apollo.mail.transport.protocol}")
    val mailTransportProtocol: String = ""
    @Value("\${org.kebs.app.kotlin.apollo.mail.stmp.auth.key}")
    val mailStmpAuthKey: String = ""
    @Value("\${org.kebs.app.kotlin.apollo.mail.stmp.auth}")
    val mailStmpAuth: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.mail.smtp.starttls.enable.key}")
    val mailStmpStartTlsKey: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.mail.smtp.starttls.enable}")
    val mailStmpStartTls: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.mail.debug.key}")
    val mailStmpDebugKey: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.mail.debug}")
    val mailStmpDebug: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.notifs.workflow.id}")
    val notifsWorkflowId: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.notifs.sms.created.by}")
    val notifsSmsCreatedBy: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.notifs.email.created.by}")
    val notifsEmailCreatedBy: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.notifs.header.content.type.key}")
    val notifsContentTypeKey: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.notifs.header.content.type.value}")
    val notifsContentTypeValue: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.notifs.header.access.key}")
    val notifsContentAcessKey: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.notifs.service.map.id}")
    val notifsServiceMapsId: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.notifs.service.active.status}")
    val notifsServiceMapsActiveStatus: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.notifs.type.email}")
    val notifsEmailType: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.notifs.type.sms}")
    val notifsSmsType: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.notifs.type.sms.email}")
    val notifsSmsEmailType: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.notifs.actor.name}")
    val notifsActorName: String = ""
}