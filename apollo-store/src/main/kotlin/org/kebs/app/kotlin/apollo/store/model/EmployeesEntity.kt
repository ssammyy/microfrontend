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
@Table(name = "DAT_KEBS_EMPLOYEES")
class EmployeesEntity : Serializable {
    @Column(name = "ID")
    @Id
    @SequenceGenerator(name = "DAT_KEBS_EMPLOYEES_SEQ_GEN", sequenceName = "DAT_KEBS_EMPLOYEES_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_EMPLOYEES_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long? = null

    @Column(name = "DEPARTMENT")
    @Basic
    var department: String? = null

    @Column(name = "STATION")
    @Basic
    var station: String? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int = 0

    @Column(name = "VAR_FIELD1")
    @Basic
    var varField1: String? = null

    @Column(name = "VAR_FIELD2")
    @Basic
    var varField2: String? = null

    @Column(name = "VAR_FIELD3")
    @Basic
    var varField3: String? = null

    @Column(name = "VAR_FIELD4")
    @Basic
    var varField4: String? = null

    @Column(name = "VAR_FIELD5")
    @Basic
    var varField5: String? = null

    @Column(name = "VAR_FIELD6")
    @Basic
    var varField6: String? = null

    @Column(name = "VAR_FIELD7")
    @Basic
    var varField7: String? = null

    @Column(name = "VAR_FIELD8")
    @Basic
    var varField8: String? = null

    @Column(name = "VAR_FIELD9")
    @Basic
    var varField9: String? = null

    @Column(name = "VAR_FIELD10")
    @Basic
    var varField10: String? = null

    @Column(name = "CREATED_BY")
    @Basic
    var createdBy: Long? = null

    @Column(name = "CREATED_ON")
    @Basic
    var createdOn: Timestamp? = null

    @Column(name = "MODIFIED_BY")
    @Basic
    var modifiedBy: Long? = null

    @Column(name = "MODIFIED_DATE")
    @Basic
    var modifiedDate: Timestamp? = null

    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    @ManyToOne
    var userId: UsersEntity? = null


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as EmployeesEntity
        return status == that.status && userId == that.userId &&
                id == that.id &&
                department == that.department &&
                station == that.station &&
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
                modifiedDate == that.modifiedDate
    }

    override fun hashCode(): Int {
        return Objects.hash(id, department, station, status, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedDate, userId)
    }
}