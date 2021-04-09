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
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "CFG_NOTIFICATION_TYPES")
class NotificationTypesEntity : Serializable {


    @Id
    @SequenceGenerator(name = "CFG_NOTIFICATION_TYPES_SEQ_GEN", allocationSize = 1, sequenceName = "CFG_NOTIFICATION_TYPES_SEQ")
    @GeneratedValue(generator = "CFG_NOTIFICATION_TYPES_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID", nullable = false, precision = 0)
    var id: Int = 0

    @Basic
    @Column(name = "DESCRIPTION", nullable = true, length = 250)
    var description: String? = null

    @Basic
    @Column(name = "INTEGRATION_ID")
    var integrationId: Long? = null

    @Basic
    @Column(name = "VAR_FIELD_1", nullable = true, length = 200)
    var varField1: String? = null

    @Basic
    @Column(name = "VAR_FIELD_2", nullable = true, length = 200)
    var varField2: String? = null

    @Basic
    @Column(name = "VAR_FIELD_3", nullable = true, length = 200)
    var varField3: String? = null

    @Basic
    @Column(name = "VAR_FIELD_4", nullable = true, length = 200)
    var varField4: String? = null

    @Basic
    @Column(name = "VAR_FIELD_5", nullable = true, length = 200)
    var varField5: String? = null

    @Basic
    @Column(name = "VAR_FIELD_6", nullable = true, length = 200)
    var varField6: String? = null

    @Basic
    @Column(name = "VAR_FIELD_7", nullable = true, length = 200)
    var varField7: String? = null

    @Basic
    @Column(name = "VAR_FIELD_8", nullable = true, length = 200)
    var varField8: String? = null

    @Basic
    @Column(name = "VAR_FIELD_9", nullable = true, length = 200)
    var varField9: String? = null

    @Basic
    @Column(name = "VAR_FIELD_10", nullable = true, length = 200)
    var varField10: String? = null

    @Basic
    @Column(name = "CREATED_ON", nullable = true)
    var createdOn: Timestamp? = null

    @Basic
    @Column(name = "CREATED_BY", nullable = true, length = 50)
    var createdBy: String? = null

    @Basic
    @Column(name = "MODIFIED_ON", nullable = true)
    var modifiedOn: Timestamp? = null

    @Basic
    @Column(name = "MODIFIED_BY", nullable = true, length = 50)
    var modifiedBy: String? = null

    @Basic
    @Column(name = "DELETED_ON", nullable = true)
    var deletedOn: Timestamp? = null

    @Basic
    @Column(name = "DELETED_BY", nullable = true, length = 50)
    var deletedBy: String? = null

    @Basic
    @Column(name = "DELIMITER", nullable = true, length = 25)
    var delimiter: String? = null

    @Basic
    @Column(name = "BEAN_PREFIX_REPLACEMENT", nullable = true, length = 25)
    var beanprefixreplacement: String? = null

    @Basic
    @Column(name = "BEAN_PREFIX", nullable = true, length = 25)
    var beanprefix: String? = null


    override fun hashCode(): Int {
        var result = id
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + (integrationId?.hashCode() ?: 0)
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
        result = 31 * result + (createdOn?.hashCode() ?: 0)
        result = 31 * result + (createdBy?.hashCode() ?: 0)
        result = 31 * result + (modifiedOn?.hashCode() ?: 0)
        result = 31 * result + (modifiedBy?.hashCode() ?: 0)
        result = 31 * result + (deletedOn?.hashCode() ?: 0)
        result = 31 * result + (deletedBy?.hashCode() ?: 0)
        result = 31 * result + (delimiter?.hashCode() ?: 0)
        result = 31 * result + (beanprefixreplacement?.hashCode() ?: 0)
        result = 31 * result + (beanprefix?.hashCode() ?: 0)
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NotificationTypesEntity

        if (id != other.id) return false
        if (description != other.description) return false
        if (integrationId != other.integrationId) return false
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
        if (createdOn != other.createdOn) return false
        if (createdBy != other.createdBy) return false
        if (modifiedOn != other.modifiedOn) return false
        if (modifiedBy != other.modifiedBy) return false
        if (deletedOn != other.deletedOn) return false
        if (deletedBy != other.deletedBy) return false
        if (delimiter != other.delimiter) return false
        if (beanprefixreplacement != other.beanprefixreplacement) return false
        if (beanprefix != other.beanprefix) return false

        return true
    }


}
