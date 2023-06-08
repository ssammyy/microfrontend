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

package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Date
import javax.persistence.*

@Entity
@Table(name = "CFG_SERVICE_MAPS")
class ServiceMapsEntity : Serializable {

    @Column(name = "ID")
    @Id
    @SequenceGenerator(name = "CFG_SERVICE_MAPS_SEQ_GEN", sequenceName = "CFG_SERVICE_MAPS_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "CFG_SERVICE_MAPS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Int = 0


    @Column(name = "BPMN_PROCESS_KEY")
    @Basic
    var bpmnProcessKey: String? = null

    @Column(name = "TRANSACTION_REF_PREFIX")
    @Basic
    var transactionRefPrefix: String? = null

    @Column(name = "ROLE_NAME")
    @Basic
    var roleName: String? = null

    @Column(name = "TOKEN_EXPIRY_HOURS")
    @Basic
    var tokenExpiryHours: Long? = null

    @Column(name = "SUCCESS_NOTIFICATION_URL")
    @Basic
    var successNotificationUrl: String? = null

    @Column(name = "FAILURE_NOTIFICATION_URL")
    @Basic
    var failureNotificationUrl: String? = null

    @Column(name = "PASSWORD_LENGTH")
    @Basic
    var passwordLength: Int? = null

    @Column(name = "UI_PAGE_SIZE")
    @Basic
    val uiPageSize: Int? = 10

    @Column(name = "SUB_REGION_ID")
    @Basic
    var subRegionId: Long? = null

    @Column(name = "DESIGNATION_ID")
    @Basic
    var designationId: Long? = null

    @Column(name = "MAIN_VERSION_ID")
    @Basic
    var mainVersionId: Int = 0

    @Column(name = "MANUFACTURER_USER_TABLE")
    @Basic
    var manufacturerUserTable: String? = null

    @Column(name = "BASIC_AUTHENTICATION_VALUE")
    @Basic
    var basicAuthenticationValue: String = ""

    @Column(name = "DIGEST_AUTHENTICATION_VALUE")
    @Basic
    var digestAuthenticationValue: String = ""


    @Column(name = "MESSAGE_DIGEST_ALGORITHM")
    @Basic
    var messageDigestAlgorithm: String = ""

    @Column(name = "SECURE_RANDOM")
    @Basic
    var secureRandom: String = ""

    @Column(name = "TRANSACTION_REF_LENGTH")
    @Basic
    var transactionRefLength: Int = 0

    @Column(name = "SERVICE_TOPIC")
    @Basic
    var serviceTopic: String = ""

    @Column(name = "REQUEST_CLASS")
    @Basic
    var requestClass: String? = null

    @Column(name = "RESPONSE_CLASS")
    @Basic
    var responseClass: String? = null

    @Column(name = "ACTOR_CLASS")
    @Basic
    var actorClass: String? = null

    @Column(name = "ACTIVE_STATUS")
    @Basic
    var activeStatus: Int = 0

    @Column(name = "TEST_STATUS")
    @Basic
    var testStatus: Int = 0

    @Column(name = "INACTIVE_STATUS")
    @Basic
    var inactiveStatus: Int = 0

    @Column(name = "INIT_STATUS")
    @Basic
    var initStatus: Int = 0

    @Column(name = "WORKING_STATUS")
    @Basic
    var workingStatus: Int = 0

    @Column(name = "FAILED_STATUS")
    @Basic
    var failedStatus: Int = 0

    @Column(name = "SUCCESS_STATUS")
    @Basic
    var successStatus: Int = 0

    @Column(name = "EXCEPTION_STATUS")
    @Basic
    var exceptionStatus: Int = 0

    @Column(name = "INVALID_STATUS")
    @Basic
    var invalidStatus: Int = 0

    @Column(name = "INVALID_STATUS_KRA")
    @Basic
    var invalidStatusKra: Int = 0

    @Column(name = "INVALID_STATUS_REGISTRAR")
    @Basic
    var invalidStatusRegistrar: Int = 0

    @Column(name = "VALID_STATUS")
    @Basic
    var validStatus: Int = 0

    @Column(name = "INIT_STAGE")
    @Basic
    var initStage: String? = null

    @Column(name = "EXCEPTION_STATUS_CODE")
    @Basic
    var exceptionStatusCode: String? = null

    @Column(name = "FAILED_STATUS_CODE")
    @Basic
    var failedStatusCode: String? = null

    @Column(name = "SUCCESS_STATUS_CODE")
    @Basic
    var successStatusCode: String = ""

    @Column(name = "HTTP_SUCCESS_RESPONSE")
    @Basic
    var httpSuccessResponse: Int = 0

    @Column(name = "HTTP_FAILURE_RESPONSE")
    @Basic
    var httpFailureResponse: Int = 0

    @Column(name = "TEST_CODE_STATUS")
    @Basic
    var testCodeStatus: Int = 0

    @Column(name = "SEPARATOR")
    @Basic
    var separator: String? = null

    @Column(name = "CURRENCY_SYMBOL")
    @Basic
    var currencySymbol: String? = null

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
    var createdOn: String? = null

    @Column(name = "MODIFIED_BY")
    @Basic
    var modifiedBy: String? = null

    @Column(name = "DELETED_BY")
    @Basic
    var deletedBy: String? = null

    @Column(name = "DELETED_ON")
    @Basic
    var deletedOn: String? = null

    @Column(name = "OBJECT_MAPPED_TO")
    @Basic
    var objectMappedTo: String? = null

    @Column(name = "OBJECT_MAPPED_FROM")
    @Basic
    var objectMappedFrom: String? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int = 0

    @Column(name = "TO_MAP")
    @Basic
    var toMap: Int = 0

    @Column(name = "EMPLOYEE_USER_TYPE")
    @Basic
    var employeeUserType: Int? = null

    @Column(name = "MANUFACTURER_USER_TYPE")
    @Basic
    var manufacturerUserType: Int? = null

//    @OneToMany(mappedBy = "serviceMapsId")
//    var serviceMaps: MutableCollection<ServiceMapsWorkflowsEntity> = mutableListOf()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as ServiceMapsEntity
        return id == that.id &&
                messageDigestAlgorithm == that.messageDigestAlgorithm &&
                secureRandom == that.secureRandom &&
                transactionRefLength == that.transactionRefLength &&
                serviceTopic == that.serviceTopic &&
                requestClass == that.requestClass &&
                responseClass == that.responseClass &&
                actorClass == that.actorClass &&
                activeStatus == that.activeStatus &&
                testStatus == that.testStatus &&
                inactiveStatus == that.inactiveStatus &&
                initStatus == that.initStatus &&
                workingStatus == that.workingStatus &&
                failedStatus == that.failedStatus &&
                successStatus == that.successStatus &&
                exceptionStatus == that.exceptionStatus &&
                invalidStatus == that.invalidStatus &&
                invalidStatusKra == that.invalidStatusKra &&
                invalidStatusRegistrar == that.invalidStatusRegistrar &&
                validStatus == that.validStatus &&
                initStage == that.initStage &&
                exceptionStatusCode == that.exceptionStatusCode &&
                failedStatusCode == that.failedStatusCode &&
                successStatusCode == that.successStatusCode &&
                httpSuccessResponse == that.httpSuccessResponse &&
                httpFailureResponse == that.httpFailureResponse &&
                testCodeStatus == that.testCodeStatus &&
                separator == that.separator &&
                currencySymbol == that.currencySymbol &&
                varField1 == that.varField1 &&
                varField2 == that.varField2 &&
                varField3 == that.varField3 &&
                varField4 == that.varField4 &&
                varField5 == that.varField5 &&
                varField6 == that.varField6 &&
                varField7 == that.varField7 &&
                varField8 == that.varField8 &&
                varField9 == that.varField9 &&
                varField10 == that.varField10 &&
                createdBy == that.createdBy &&
                createdOn == that.createdOn &&
                modifiedBy == that.modifiedBy &&
                deletedBy == that.deletedBy &&
                deletedOn == that.deletedOn &&
                objectMappedTo == that.objectMappedTo &&
                objectMappedFrom == that.objectMappedFrom &&
                status == that.status &&
                toMap == that.toMap &&
                employeeUserType == that.employeeUserType &&
                manufacturerUserType == that.manufacturerUserType 
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + (bpmnProcessKey?.hashCode() ?: 0)
        result = 31 * result + (roleName?.hashCode() ?: 0)
        result = 31 * result + (tokenExpiryHours?.hashCode() ?: 0)
        result = 31 * result + (successNotificationUrl?.hashCode() ?: 0)
        result = 31 * result + (failureNotificationUrl?.hashCode() ?: 0)
        result = 31 * result + (passwordLength ?: 0)
        result = 31 * result + (uiPageSize ?: 10)
        result = 31 * result + (subRegionId?.hashCode() ?: 0)
        result = 31 * result + (designationId?.hashCode() ?: 0)
        result = 31 * result + mainVersionId
        result = 31 * result + (manufacturerUserTable?.hashCode() ?: 0)
        result = 31 * result + basicAuthenticationValue.hashCode()
        result = 31 * result + digestAuthenticationValue.hashCode()
        result = 31 * result + messageDigestAlgorithm.hashCode()
        result = 31 * result + secureRandom.hashCode()
        result = 31 * result + transactionRefLength
        result = 31 * result + serviceTopic.hashCode()
        result = 31 * result + (requestClass?.hashCode() ?: 0)
        result = 31 * result + (responseClass?.hashCode() ?: 0)
        result = 31 * result + (actorClass?.hashCode() ?: 0)
        result = 31 * result + activeStatus
        result = 31 * result + testStatus
        result = 31 * result + inactiveStatus
        result = 31 * result + initStatus
        result = 31 * result + workingStatus
        result = 31 * result + failedStatus
        result = 31 * result + successStatus
        result = 31 * result + exceptionStatus
        result = 31 * result + invalidStatus
        result = 31 * result + invalidStatusKra
        result = 31 * result + invalidStatusRegistrar
        result = 31 * result + validStatus
        result = 31 * result + (initStage?.hashCode() ?: 0)
        result = 31 * result + (exceptionStatusCode?.hashCode() ?: 0)
        result = 31 * result + (failedStatusCode?.hashCode() ?: 0)
        result = 31 * result + successStatusCode.hashCode()
        result = 31 * result + httpSuccessResponse
        result = 31 * result + httpFailureResponse
        result = 31 * result + testCodeStatus
        result = 31 * result + (separator?.hashCode() ?: 0)
        result = 31 * result + (currencySymbol?.hashCode() ?: 0)
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
        result = 31 * result + (deletedBy?.hashCode() ?: 0)
        result = 31 * result + (deletedOn?.hashCode() ?: 0)
        result = 31 * result + (objectMappedTo?.hashCode() ?: 0)
        result = 31 * result + (objectMappedFrom?.hashCode() ?: 0)
        result = 31 * result + status
        result = 31 * result + toMap
        result = 31 * result + (employeeUserType ?: 0)
        result = 31 * result + (manufacturerUserType ?: 0)
        return result.toInt()
    }


}
