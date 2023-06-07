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
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "CFG_PAYMENT_TYPES")
class PaymentTypesEntity : Serializable {
    @Column(name = "ID", nullable = false, precision = 0)
    @Id
    var id: Long = 0

    @Column(name = "TYPE_CODE", nullable = true, length = 128)
    @Basic
    var typeCode: String? = null

    @Column(name = "TYPE_NAME", nullable = true, length = 128)
    @Basic
    var typeName: String? = null

    @Column(name = "STATUS", nullable = true, precision = 0)
    @Basic
    var status: Int? = null

    @Column(name = "DESCRIPTIONS", nullable = true, length = 3800)
    @Basic
    var descriptions: String? = null

    @Column(name = "VAR_FIELD_1", nullable = true, length = 350)
    @Basic
    var varField1: String? = null

    @Column(name = "VAR_FIELD_2", nullable = true, length = 350)
    @Basic
    var varField2: String? = null

    @Column(name = "VAR_FIELD_3", nullable = true, length = 350)
    @Basic
    var varField3: String? = null

    @Column(name = "VAR_FIELD_4", nullable = true, length = 350)
    @Basic
    var varField4: String? = null

    @Column(name = "VAR_FIELD_5", nullable = true, length = 350)
    @Basic
    var varField5: String? = null

    @Column(name = "VAR_FIELD_6", nullable = true, length = 350)
    @Basic
    var varField6: String? = null

    @Column(name = "VAR_FIELD_7", nullable = true, length = 350)
    @Basic
    var varField7: String? = null

    @Column(name = "VAR_FIELD_8", nullable = true, length = 350)
    @Basic
    var varField8: String? = null

    @Column(name = "VAR_FIELD_9", nullable = true, length = 350)
    @Basic
    var varField9: String? = null

    @Column(name = "VAR_FIELD_10", nullable = true, length = 350)
    @Basic
    var varField10: String? = null

    @Column(name = "CREATED_BY", nullable = false, length = 100)
    @Basic
    var createdBy: String? = null

    @Column(name = "CREATED_ON", nullable = false)
    @Basic
    var createdOn: Timestamp? = null

    @Column(name = "LAST_MODIFIED_BY", nullable = true, length = 100)
    @Basic
    var lastModifiedBy: String? = null

    @Column(name = "LAST_MODIFIED_ON", nullable = true)
    @Basic
    var lastModifiedOn: Timestamp? = null

    @Column(name = "UPDATE_BY", nullable = true, length = 100)
    @Basic
    var updateBy: String? = null

    @Column(name = "UPDATED_ON", nullable = true)
    @Basic
    var updatedOn: Timestamp? = null

    @Column(name = "DELETE_BY", nullable = true, length = 100)
    @Basic
    var deleteBy: String? = null

    @Column(name = "DELETED_ON", nullable = true)
    @Basic
    var deletedOn: Timestamp? = null

    @Column(name = "VERSION", nullable = true, precision = 0)
    @Basic
    var version: Long? = null

    @OneToMany(mappedBy = "paymentTypeId")
    var datPaymentsLogsById: Collection<PaymentsLogEntity>? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as PaymentTypesEntity
        return id == that.id &&
                typeCode == that.typeCode &&
                typeName == that.typeName &&
                status == that.status &&
                descriptions == that.descriptions &&
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
                lastModifiedBy == that.lastModifiedBy &&
                lastModifiedOn == that.lastModifiedOn &&
                updateBy == that.updateBy &&
                updatedOn == that.updatedOn &&
                deleteBy == that.deleteBy &&
                deletedOn == that.deletedOn &&
                version == that.version
    }

    override fun hashCode(): Int {
        return Objects.hash(
            id,
            typeCode,
            typeName,
            status,
            descriptions,
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
            lastModifiedBy,
            lastModifiedOn,
            updateBy,
            updatedOn,
            deleteBy,
            deletedOn,
            version
        )
    }

}