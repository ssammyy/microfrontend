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
import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "LOG_WORKFLOW_TRANSACTIONS")
class WorkflowTransactionsEntity : Serializable {
    @Column(name = "ID")
    @Id
    @SequenceGenerator(name = "LOG_WORKFLOW_TRANSACTIONS_SEQ_GEN", sequenceName = "LOG_WORKFLOW_TRANSACTIONS_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "LOG_WORKFLOW_TRANSACTIONS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long = 0

    @Column(name = "TRANSACTION_DATE")
    @Basic
    var transactionDate: Date? = null

    @Column(name = "TRANSACTION_STATUS")
    @Basic
    var transactionStatus: Int = 0

    @Column(name = "TRANSACTION_REFERENCE")
    @Basic
    var transactionReference: String? = null

    @Column(name = "RETRIES")
    @Basic
    var retries: Int = 0

    @Column(name = "RETRIED")
    @Basic
    var retried: Int = 0

    @Column(name = "RESPONSE_REFERENCE")
    @Basic
    var responseReference: String? = null

    @Column(name = "LAST_UPDATED")
    @Basic
    var lastUpdated: Time? = null

    @Column(name = "CURRENT_SEQUENCE")
    @Basic
    var currentSequence: Int? = 0

    @Column(name = "EXECUTION_ORDER")
    @Basic
    var executionOrder: Int? = 0

    @Column(name = "CB_UPDATE_REMOTE_DATE")
    @Basic
    var cbUpdateRemoteDate: String? = null

    @Column(name = "INTEGRATION_REQUEST")
    @Basic
    var integrationRequest: String? = null

    @Column(name = "INTEGRATION_RESPONSE")
    @Basic
    var integrationResponse: String? = null

    @Column(name = "RESPONSE_STATUS")
    @Basic
    var responseStatus: String? = null

    @Column(name = "RESPONSE_MESSAGE")
    @Basic
    var responseMessage: String? = null

    @Column(name = "TRANSACTION_START_DATE")
    @Basic
    var transactionStartDate: Timestamp? = null

    @Column(name = "TRANSACTION_COMPLETED_DATE")
    @Basic
    var transactionCompletedDate: Timestamp? = null

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

    @Column(name = "DELETE_DON")
    @Basic
    var deleteDon: Timestamp? = null

    @JoinColumn(name = "SERVICE_REQUEST", referencedColumnName = "ID")
    @ManyToOne
    var serviceRequests: ServiceRequestsEntity? = null

    @JoinColumn(name = "METHOD_ID", referencedColumnName = "ID")
    @ManyToOne
    var methodId: ServiceMapsWorkflowsEntity? = null

    @JoinColumn(name = "INTEGRATION_ID", referencedColumnName = "ID")
    @ManyToOne
    var integrationId: IntegrationConfigurationEntity? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as WorkflowTransactionsEntity
        return id == that.id &&
                transactionDate == that.transactionDate &&
                transactionStatus == that.transactionStatus &&
                transactionReference == that.transactionReference &&
                retries == that.retries &&
                retried == that.retried &&
                responseReference == that.responseReference &&
                lastUpdated == that.lastUpdated &&
                currentSequence == that.currentSequence &&
                executionOrder == that.executionOrder &&
                cbUpdateRemoteDate == that.cbUpdateRemoteDate &&
                integrationRequest == that.integrationRequest &&
                integrationResponse == that.integrationResponse &&
                responseStatus == that.responseStatus &&
                responseMessage == that.responseMessage &&
                transactionStartDate == that.transactionStartDate &&
                transactionCompletedDate == that.transactionCompletedDate &&
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
                modifiedOn == that.modifiedOn &&
                deleteBy == that.deleteBy &&
                deleteDon == that.deleteDon
    }

    override fun hashCode(): Int {
        return Objects.hash(
                id,
                transactionDate,
                transactionStatus,
                transactionReference,
                retries,
                retried,
                responseReference,
                lastUpdated,
                currentSequence,
                executionOrder,
                cbUpdateRemoteDate,
                integrationRequest,
                integrationResponse,
                responseStatus,
                responseMessage,
                transactionStartDate,
                transactionCompletedDate,
                varField1,
                varField2,
                varField3,
                varField4,
                varField5,
                varField6,
                varField7,
                varField8,
                varField9,
                varField10,
                createdBy,
                createdOn,
                modifiedBy,
                modifiedOn,
                deleteBy,
                deleteDon
        )
    }

}