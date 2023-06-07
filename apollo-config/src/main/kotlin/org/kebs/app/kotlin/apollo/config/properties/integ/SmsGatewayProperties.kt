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

package org.kebs.app.kotlin.apollo.config.properties.integ

import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
@EncryptablePropertySource(value = ["file:\${CONFIG_PATH}/sms-broker.properties"])
class SmsGatewayProperties {
    @Value("\${org.app.properties.gateway.sms.gateway.ip}")
    val gatewayIp: String? = null
    @Value("\${org.app.properties.gateway.sms.gateway.port}")
    val gatewayPort: String? = null
    @Value("\${org.app.properties.gateway.sms.gateway.user}")
    val gatewayUser: String? = null
    @Value("\${org.app.properties.gateway.sms.gateway.user.keyword}")
    val gatewayUserKeyword: String? = null
    @Value("\${org.app.properties.gateway.sms.gateway.psswd}")
    val gatewayPsswd: String? = null
    @Value("\${org.app.properties.gateway.sms.gateway.psswd.keyword}")
    val gatewayPsswdKeyword: String? = null
    @Value("\${org.app.properties.gateway.sms.gateway.to.keyword}")
    val gatewayToKeyword: String? = null
    @Value("\${org.app.properties.gateway.sms.gateway.from.keyword}")
    val gatewayFromKeyword: String? = null
    @Value("\${org.app.properties.gateway.sms.gateway.message.keyword}")
    val gatewayMessageKeyword: String? = null
    @Value("\${org.app.properties.gateway.sms.gateway.response.code.http.ok}")
    val gatewayResponseCodeHttpOk: Int? = null
    @Value("\${org.app.properties.gateway.sms.gateway.response.code.http.created}")
    val gatewayResponseCodeHttpCreated: Int? = null
    @Value("\${org.app.properties.gateway.sms.gateway.response.code.http.accepted}")
    val gatewayResponseCodeHttpAccepted: Int? = null
    @Value("\${org.app.properties.gateway.sms.gateway.integ.http.method.post}")
    val gatewayIntegHttpMethodPost: String? = null

    @Value("\${org.app.properties.gateway.sms.gateway.integ.http.method.get}")
    val gatewayIntegHttpMethod: String? = null

    @Value("\${org.app.properties.gateway.sms.gateway.sender}")
    val gatewaySender: String? = null
    @Value("\${org.app.properties.gateway.sms.gateway.sender.yu}")
    val gatewaySenderYu: String? = null
    @Value("\${org.app.properties.gateway.sms.gateway.sender.mvno}")
    val gatewaySenderMvno: String? = null
    @Value("\${org.app.properties.gateway.sms.gateway.countrycode}")
    val gatewayCountrycode: String? = null
    @Value("\${org.app.properties.gateway.sms.gateway.msisdnlength}")
    val gatewayMsisdnlength: String? = null
    @Value("\${org.app.properties.gateway.sms.gateway.type}")
    val gatewayType: String? = null
    @Value("\${org.app.properties.gateway.sms.codes.response.file}")
    val codesResponseFile: String? = null
    @Value("\${org.app.properties.gateway.sms.gateway.default.response}")
    val gatewayDefaultResponse: String? = null
    @Value("\${org.app.properties.gateway.sms.gateway.default.encoding}")
    val gatewayDefaultEncoding: String? = null
    @Value("\${org.app.properties.gateway.sms.gateway.default.error.response}")
    val gatewayDefaultErrorResponse: String? = null
    @Value("\${org.app.properties.gateway.sms.gateway.url}")
    val gatewayUrl: String? = null
    @Value("\${org.app.properties.gateway.sms.gateway.url.keywords}")
    val gatewayUrlKeywords: String? = null
    @Value("\${org.app.properties.gateway.sms.gateway.default.maxretry}")
    val gatewayDefaultMaxretry: Int? = null
    @Value("\${org.app.properties.gateway.sms.response.log.sdf}")
    val responseLogSdf: String? = null
    @Value("\${org.app.properties.gateway.sms.out.notification.default}")
    val outNotificationDefault: String? = null
    @Value("\${org.app.properties.gateway.sms.in.status.init}")
    val inStatusInit: String? = null
    @Value("\${org.app.properties.gateway.sms.in.status.response.init}")
    val inStatusResponseInit: String? = null
    @Value("\${org.app.properties.gateway.sms.in.status.processing}")
    val inStatusProcessing: String? = null
    @Value("\${org.app.properties.gateway.sms.in.auto.responseKeyword}")
    val inAutoResponseKeyword: String? = null
    @Value("\${org.app.properties.gateway.sms.in.status.final}")
    val inStatusFinal: String? = null
    @Value("\${org.app.properties.gateway.sms.in.status.fail}")
    val inStatusFail: String? = null
    @Value("\${org.app.properties.gateway.sms.out.status.init}")
    val outStatusInit: Int? = null
    @Value("\${org.app.properties.gateway.sms.out.status.processing}")
    val outStatusProcessing: String? = null
    @Value("\${org.app.properties.gateway.sms.response.kannel.status.successKeyword}")
    val responseKannelStatusSuccessKeyword: String? = null
    @Value("\${org.app.properties.gateway.sms.out.status.final}")
    val outStatusFinal: Int? = null
    @Value("\${org.app.properties.gateway.sms.out.status.fail}")
    val outStatusFail: Int? = null
    @Value("\${org.app.properties.gateway.sms.keyword.out}")
    val keywordOut: String? = null
    @Value("\${org.app.properties.gateway.sms.keyword.in}")
    val keywordIn: String? = null
    @Value("\${org.app.properties.gateway.sms.app.name.user}")
    val appNameUser: String? = null
    @Value("\${org.app.properties.gateway.sms.app.name}")
    val appName: String? = null
    @Value("\${org.app.properties.gateway.sms.bulksms.url}")
    val bulksmsUrl: String? = null
    @Value("\${org.app.properties.gateway.sms.bulksms.single.url}")
    val bulksmsSingleUrl: String? = null
    @Value("\${org.app.properties.gateway.sms.bulksms.user}")
    val bulksmsUser: String? = null
    @Value("\${org.app.properties.gateway.sms.bulksms.psswd}")
    val bulksmsPsswd: String? = null
    @Value("\${org.app.properties.gateway.sms.bulksms.encoding}")
    val bulksmsEncoding: String? = null
    @Value("\${org.app.properties.gateway.sms.bulksms.sender}")
    val bulksmsSender: String? = null
    @Value("\${org.app.properties.gateway.sms.response.kannel.keyword.separatorKeyword}")
    val responseKannelKeywordSeparatorKeyword: String? = null
    @Value("\${org.app.properties.gateway.sms.africastalking.sender}")
    val africastalkingSender: String? = null
    @Value("\${org.app.properties.gateway.sms.africastalking.username}")
    val africastalkingUsername: String? = null
    @Value("\${org.app.properties.gateway.sms.africastalking.apiKey}")
    val africastalkingApiKey: String? = null
    @Value("\${org.app.properties.gateway.sms.autostrike.applied.penalty.notification}")
    val xpressAutostrikeAppliedPenaltyNotif: String? = null
    @Value("\${org.app.properties.gateway.sms.autostrike.savings.collection.notification}")
    val xpressSavingsAutostrikeSavingsCollectionNotif: String? = null
    @Value("\${org.app.properties.gateway.sms.loan.balance.overdue.notification")
    val xpressLoanBalOverdueNotif: String? = null
    @Value("\${org.app.properties.gateway.sms.loan.balance.notification}")
    val xpressLoanBalNotif: String? = null


}
