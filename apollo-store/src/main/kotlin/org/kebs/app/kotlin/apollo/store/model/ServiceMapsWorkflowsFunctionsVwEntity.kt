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
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "CRM_SERVICE_MAPS_WORKFLOWS_FUNCTIONS_VW")
class ServiceMapsWorkflowsFunctionsVwEntity : Serializable {
    @Id
    @Column(name = "ID")
    @Basic
    var id: Long = 0

    @Column(name = "SERVICE_MAP")
    @Basic
    var serviceMap: Int? = null

    @Column(name = "WORKFLOW_ID")
    @Basic
    var workflowId: Long = 0

    @Column(name = "BEAN_NAME")
    @Basic
    var beanName: String? = null

    @Column(name = "METHOD_NAME")
    @Basic
    var methodName: String = ""

    @Column(name = "PERIOD_BEFORE_DUPLICATES")
    @Basic
    var periodBeforeDuplicates: Long? = null

    @Column(name = "EVENT_ID")
    @Basic
    var eventId: Long = 0

    @Column(name = "FUNCTION_NAME")
    @Basic
    var functionName: String? = null

    @Column(name = "EXECUTION_ORDER")
    @Basic
    var executionOrder: Int = 0

    @Column(name = "SEQUENCE_NUMBER")
    @Basic
    var sequenceNumber: Int = 0

    @Column(name = "INTEGRATION_ID")
    @Basic
    var integrationId: Long = 0

    @Column(name = "URL")
    @Basic
    var url: String? = null

    @Column(name = "DESCRIPTIONS")
    @Basic
    var descriptions: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as ServiceMapsWorkflowsFunctionsVwEntity
        return id == that.id && workflowId == that.workflowId && eventId == that.eventId && executionOrder == that.executionOrder && sequenceNumber == that.sequenceNumber &&
                beanName == that.beanName &&
                methodName == that.methodName &&
                periodBeforeDuplicates == that.periodBeforeDuplicates &&
                functionName == that.functionName
    }

    override fun hashCode(): Int {
        return Objects.hash(id, workflowId, beanName, methodName, periodBeforeDuplicates, eventId, functionName, executionOrder, sequenceNumber)
    }
}