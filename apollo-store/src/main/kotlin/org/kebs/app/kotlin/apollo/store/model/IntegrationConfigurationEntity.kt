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

package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "CFG_INTEGRATION_CONFIGURATION")
class IntegrationConfigurationEntity : Serializable {


    @Id
    @SequenceGenerator(name = "CFG_INTEGRATION_CONFIGURATION_SEQ_GEN", sequenceName = "CFG_INTEGRATION_CONFIGURATION_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "CFG_INTEGRATION_CONFIGURATION_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    var id: Long = 0

    @Column(name = "ACCOUNT")
    @Basic
    var account: String = ""

    @Column(name = "HASHING_ALGORITHM")
    @Basic
    var hashingAlgorithm: String? = null

    @Column(name = "ACCOUNT_REFERENCE")
    @Basic
    var accountReference: String = ""

    @Column(name = "TRANSACTION_REFERENCE")
    @Basic
    var transactionReference: String = ""

    @Column(name = "TOKEN")
    @Basic
    var token: String = ""

    @Column(name = "UNKNOWN_HOST_KEY")
    @Basic
    var unknownHostKey: Boolean? = null

    @Column(name = "PORT_NUMBER")
    @Basic
    var portNumber: Int = 0

    @Column(name = "TOKEN_TIME_GENERATED")
    @Basic
    var tokenTimeGenerated: Timestamp? = null

    @Column(name = "TOKEN_TIME_EXPIRES")
    @Basic
    var tokenTimeExpires: Timestamp? = null

    @Column(name = "TOKEN_TIME_LAPSE")
    @Basic
    var tokenTimeLapse: Int = 0

    @Column(name = "CLIENT_AUTHENTICATION_REALM")
    @Basic
    var clientAuthenticationRealm: String = ""

    @Column(name = "CLIENT_METHOD")
    @Basic
    var clientMethod: String = ""

    @Column(name = "CLIENT_AUTHENTICATION")
    @Basic
    var clientAuthentication: String = ""

    @Column(name = "BODY_PARAMS")
    @Basic
    var bodyParams: String? = null

    @Column(name = "CONFIG_KEYWORD")
    @Basic
    var configKeyword: String? = null

    @Column(name = "CONNECTION_REQUEST_TIMEOUT")
    @Basic
    var connectionRequestTimeout: Int = 0

    @Column(name = "CONNECT_TIMEOUT")
    @Basic
    var connectTimeout: Int = 0

    @Column(name = "EXCEPTION_CODE")
    @Basic
    var exceptionCode: String = "99"

    @Column(name = "SUCCESS_CODE")
    @Basic
    var successCode: String = "00"

    @Column(name = "FAILURE_CODE")
    @Basic
    var failureCode: String = "05"

    @Column(name = "FOLLOW_REDIRECTS")
    @Basic
    var followRedirects: Int = 0

    @Column(name = "HEADER_PARAMS")
    @Basic
    var headerParams: String? = null

    @Column(name = "MAX_CONN_PER_ROUTE")
    @Basic
    var maxConnPerRoute: Int = 0

    @Column(name = "MAX_CONN_TOTAL")
    @Basic
    var maxConnTotal: Int = 0

    @Column(name = "OK_LOWER")
    @Basic
    var okLower: Int = 0

    @Column(name = "OK_UPPER")
    @Basic
    var okUpper: Int = 0

    @Column(name = "PASSWORD")
    @Basic
    var password: String = ""

    @Column(name = "REQUEST_KEYWORD")
    @Basic
    var requestKeyword: String? = null

    @Column(name = "REQUEST_PARAMS")
    @Basic
    var requestParams: String = ""

    @Column(name = "REQUEST_PARAMS_SEPARATOR")
    @Basic
    var requestParamsSeparator: String = "_"

    @Column(name = "REQUEST_PARAMS_SPEL")
    @Basic
    var requestParamsSpel: String? = null

    @Column(name = "REQUEST_PARAMS_VALUES")
    @Basic
    var requestParamsValues: String? = null

    @Column(name = "RESPONSE_PLACE_HOLDER")
    @Basic
    var responsePlaceHolder: String? = null

    @Column(name = "SENDER")
    @Basic
    var sender: String? = null

    @Column(name = "SOCKET_TIMEOUT")
    @Basic
    var socketTimeout: Int = 0

    @Column(name = "URL")
    @Basic
    var url: String? = null

    @Column(name = "GSON_DATE_FORMT")
    @Basic
    var gsonDateFormt: String? = null

    @Column(name = "GSON_OUTPUT_BEAN")
    @Basic
    var gsonOutputBean: String? = null

    @Column(name = "USERNAME")
    @Basic
    var username: String = ""

    @Column(name = "STATUS")
    @Basic
    var status: Int = 0

    @Column(name = "DESCRIPTIONS")
    @Basic
    var descriptions: String? = null

    @Column(name = "VAR_FIELD_1")
    @Basic
    var varField1: String? = null

    @Column(name = "VAR_FIELD_2")
    @Basic
    var varField2: String? = null

    @Column(name = "VAR_FIELD_3")
    @Basic
    var varField3: String? = null

    @Column(name = "VAR_FIELD_4")
    @Basic
    var varField4: String? = null

    @Column(name = "VAR_FIELD_5")
    @Basic
    var varField5: String? = null

    @Column(name = "VAR_FIELD_6")
    @Basic
    var varField6: String? = null

    @Column(name = "VAR_FIELD_7")
    @Basic
    var varField7: String? = null

    @Column(name = "VAR_FIELD_8")
    @Basic
    var varField8: String? = null

    @Column(name = "VAR_FIELD_9")
    @Basic
    var varField9: String? = null

    @Column(name = "VAR_FIELD_10")
    @Basic
    var varField10: String? = null

    @Column(name = "CREATED_BY")
    @Basic
    var createdBy: String? = null

    @Column(name = "CREATED_ON")
    @Basic
    var createdOn: Timestamp? = null

    @Column(name = "MODIFIED_BY")
    @Basic
    var modifiedBy: String? = null

    @Column(name = "MODIFIED_ON")
    @Basic
    var modifiedOn: Timestamp? = null

    @Column(name = "DELETE_BY")
    @Basic
    var deleteBy: String? = null

    @Column(name = "DELETED_ON")
    @Basic
    var deletedOn: Timestamp? = null


    @Column(name = "WORKFLOW_ID")
    @Basic
    var workflowId: Long? = null


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IntegrationConfigurationEntity

        if (account != other.account) return false
        if (accountReference != other.accountReference) return false
        if (transactionReference != other.transactionReference) return false
        if (token != other.token) return false
        if (unknownHostKey != other.unknownHostKey) return false
        if (tokenTimeExpires != other.tokenTimeExpires) return false
        if (tokenTimeGenerated != other.tokenTimeGenerated) return false
        if (tokenTimeLapse != other.tokenTimeLapse) return false
        if (clientAuthenticationRealm != other.clientAuthenticationRealm) return false
        if (clientMethod != other.clientMethod) return false
        if (clientAuthentication != other.clientAuthentication) return false
        if (id != other.id) return false
        if (bodyParams != other.bodyParams) return false
        if (configKeyword != other.configKeyword) return false
        if (connectionRequestTimeout != other.connectionRequestTimeout) return false
        if (connectTimeout != other.connectTimeout) return false
        if (exceptionCode != other.exceptionCode) return false
        if (successCode != other.successCode) return false
        if (failureCode != other.failureCode) return false
        if (followRedirects != other.followRedirects) return false
        if (headerParams != other.headerParams) return false
        if (maxConnPerRoute != other.maxConnPerRoute) return false
        if (maxConnTotal != other.maxConnTotal) return false
        if (okLower != other.okLower) return false
        if (okUpper != other.okUpper) return false
        if (password != other.password) return false
        if (requestKeyword != other.requestKeyword) return false
        if (requestParams != other.requestParams) return false
        if (requestParamsSeparator != other.requestParamsSeparator) return false
        if (requestParamsSpel != other.requestParamsSpel) return false
        if (requestParamsValues != other.requestParamsValues) return false
        if (responsePlaceHolder != other.responsePlaceHolder) return false
        if (sender != other.sender) return false
        if (socketTimeout != other.socketTimeout) return false
        if (url != other.url) return false
        if (gsonDateFormt != other.gsonDateFormt) return false
        if (gsonOutputBean != other.gsonOutputBean) return false
        if (username != other.username) return false
        if (status != other.status) return false
        if (descriptions != other.descriptions) return false
        if (varField1 != other.varField1) return false
        if (varField2 != other.varField2) return false
        if (varField3 != other.varField3) return false
        if (varField4 != other.varField4) return false
        if (varField5 != other.varField5) return false
        if (varField6 != other.varField6) return false
        if (varField7 != other.varField7) return false
        if (varField8 != other.varField8) return false
        if (varField9 != other.varField9) return false
        if (varField10 != other.varField10) return false
        if (createdBy != other.createdBy) return false
        if (createdOn != other.createdOn) return false
        if (modifiedBy != other.modifiedBy) return false
        if (modifiedOn != other.modifiedOn) return false
        if (deleteBy != other.deleteBy) return false
        if (deletedOn != other.deletedOn) return false
        if (workflowId != other.workflowId) return false
        if (portNumber != other.portNumber) return false

        return true
    }

    override fun hashCode(): Int {
        var result = clientAuthenticationRealm.hashCode()
        result = 31 * result + account.hashCode()
        result = 31 * result + accountReference.hashCode()
        result = 31 * result + transactionReference.hashCode()
        result = 31 * result + token.hashCode()
        result = 31 * result + unknownHostKey.hashCode()
        result = 31 * result + (tokenTimeExpires?.hashCode() ?: 0)
        result = 31 * result + (tokenTimeGenerated?.hashCode() ?: 0)
        result = 31 * result + tokenTimeLapse
        result = 31 * result + clientMethod.hashCode()
        result = 31 * result + clientAuthentication.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + (bodyParams?.hashCode() ?: 0)
        result = 31 * result + (configKeyword?.hashCode() ?: 0)
        result = 31 * result + connectionRequestTimeout
        result = 31 * result + connectTimeout
        result = 31 * result + exceptionCode.hashCode()
        result = 31 * result + successCode.hashCode()
        result = 31 * result + failureCode.hashCode()
        result = 31 * result + followRedirects
        result = 31 * result + (headerParams?.hashCode() ?: 0)
        result = 31 * result + maxConnPerRoute
        result = 31 * result + maxConnTotal
        result = 31 * result + okLower
        result = 31 * result + okUpper
        result = 31 * result + password.hashCode()
        result = 31 * result + (requestKeyword?.hashCode() ?: 0)
        result = 31 * result + requestParams.hashCode()
        result = 31 * result + requestParamsSeparator.hashCode()
        result = 31 * result + (requestParamsSpel?.hashCode() ?: 0)
        result = 31 * result + (requestParamsValues?.hashCode() ?: 0)
        result = 31 * result + (responsePlaceHolder?.hashCode() ?: 0)
        result = 31 * result + (sender?.hashCode() ?: 0)
        result = 31 * result + socketTimeout
        result = 31 * result + (url?.hashCode() ?: 0)
        result = 31 * result + (gsonDateFormt?.hashCode() ?: 0)
        result = 31 * result + (gsonOutputBean?.hashCode() ?: 0)
        result = 31 * result + username.hashCode()
        result = 31 * result + status
        result = 31 * result + (descriptions?.hashCode() ?: 0)
        result = 31 * result + (varField1?.hashCode() ?: 0)
        result = 31 * result + (varField2?.hashCode() ?: 0)
        result = 31 * result + (varField3?.hashCode() ?: 0)
        result = 31 * result + (varField4?.hashCode() ?: 0)
        result = 31 * result + (varField5?.hashCode() ?: 0)
        result = 31 * result + (varField6?.hashCode() ?: 0)
        result = 31 * result + (varField7?.hashCode() ?: 0)
        result = 31 * result + (varField8?.hashCode() ?: 0)
        result = 31 * result + (varField9?.hashCode() ?: 0)
        result = 31 * result + (varField10?.hashCode() ?: 0)
        result = 31 * result + (createdBy?.hashCode() ?: 0)
        result = 31 * result + (createdOn?.hashCode() ?: 0)
        result = 31 * result + (modifiedBy?.hashCode() ?: 0)
        result = 31 * result + (modifiedOn?.hashCode() ?: 0)
        result = 31 * result + (deleteBy?.hashCode() ?: 0)
        result = 31 * result + (deletedOn?.hashCode() ?: 0)
        result = 31 * result + (workflowId?.hashCode() ?: 0)
        result = 31 * result + portNumber.hashCode()
        return result
    }
}
