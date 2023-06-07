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

package org.kebs.app.kotlin.apollo.store.model.ms

import org.kebs.app.kotlin.apollo.store.model.RegionsEntity
import java.io.Serializable
import java.sql.Date
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_MS_COMPLAINT_LOCATION")
class ComplaintLocationEntity : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_MS_COMPLAINT_LOCATION_SEQ_GEN", allocationSize = 1, sequenceName = "DAT_KEBS_MS_COMPLAINT_LOCATION_SEQ")
    @GeneratedValue(generator = "DAT_KEBS_MS_COMPLAINT_LOCATION_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = 0

    @Column(name = "COUNTY")
    @Basic
    var county: Long? = null

    @Column(name = "SUBCOUNTY")
    @Basic
    var subcounty: String? = null

    @Column(name = "EMAIL")
    @Basic
    var email: String? = null

    @Column(name = "NAME_CONTACT_PERSON")
    @Basic
    var nameContactPerson: String? = null

    @Column(name = "PHONE_NUMBER")
    @Basic
    var phoneNumber: String? = null

    @Column(name = "TELEPHONE_NUMBER")
    @Basic
    var telephoneNumber: String? = null

    @Column(name = "BUSINESS_ADDRESS")
    @Basic
    var businessAddress: String? = null

    @Column(name = "TOWN")
    @Basic
    var town: Long? = null

    @Column(name = "MARKET_CENTER")
    @Basic
    var marketCenter: String? = null

    @Column(name = "BUILDING")
    @Basic
    var building: String? = null

    @Column(name = "PRODUCT_BRAND")
    @Basic
    var productBrand: String? = null

    @Column(name = "TRANSACTION_DATE")
    @Basic
    var transactionDate: Date? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null

    @Column(name = "REMARKS")
    @Basic
    var remarks: String? = null

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

    @Column(name = "LAST_MODIFIED_BY")
    @Basic
    var lastModifiedBy: String? = null

    @Column(name = "LAST_MODIFIED_ON")
    @Basic
    var lastModifiedOn: Timestamp? = null

    @Column(name = "UPDATE_BY")
    @Basic
    var updateBy: String? = null

    @Column(name = "UPDATED_ON")
    @Basic
    var updatedOn: Timestamp? = null

    @Column(name = "DELETE_BY")
    @Basic
    var deleteBy: String? = null

    @Column(name = "DELETED_ON")
    @Basic
    var deletedOn: Timestamp? = null

    @Column(name = "VERSION")
    @Basic
    var version: Long? = null

    @Column(name = "COMPLAINT_ID")
    @Basic
    var complaintId: Long? = null

    @Column(name = "REGION")
    @Basic
    var region: Long? = null


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as ComplaintLocationEntity
        return id == that.id &&
                complaintId == that.complaintId &&
                town == that.town &&
                region == that.region &&
                marketCenter == that.marketCenter &&
                building == that.building &&
                productBrand == that.productBrand &&
                transactionDate == that.transactionDate &&
                status == that.status &&
                remarks == that.remarks &&
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
                complaintId,
                town,
                region,
                marketCenter,
                building,
                productBrand,
                transactionDate,
                status,
                remarks,
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
