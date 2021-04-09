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
@Table(name = "DAT_KEBS_USER_PROFILES")
class UserProfilesEntity : Serializable {
    @Column(name = "ID")
    @Id
    @SequenceGenerator(name = "DAT_KEBS_USER_PROFILES_SEQ_GEN", allocationSize = 1, sequenceName = "DAT_KEBS_USER_PROFILES_SEQ")
    @GeneratedValue(generator = "DAT_KEBS_USER_PROFILES_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null

    @Column(name = "USER_PROFILE_UUID")
    @Basic
    var userProfileUuid: String? = null

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

    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    @ManyToOne
    var userId: UsersEntity? = null

    @JoinColumn(name = "DEPARTMENT_ID", referencedColumnName = "ID")
    @ManyToOne
    var departmentId: DepartmentsEntity? = null

    @Transient
    var confirmDepartmentId: Long? = 0

    @JoinColumn(name = "DIVISION_ID", referencedColumnName = "ID")
    @ManyToOne
    var divisionId: DivisionsEntity? = null

    @Transient
    var confirmDivisionId: Long? = 0

    @JoinColumn(name = "SUB_REGION_ID", referencedColumnName = "ID")
    @ManyToOne
    var subRegionId: SubRegionsEntity? = null

    @Transient
    var confirmSubRegionId: Long? = 0

    @JoinColumn(name = "DESIGNATION_ID", referencedColumnName = "ID")
    @ManyToOne
    var designationId: DesignationsEntity? = null

    @Transient
    var confirmDesignationId: Long? = 0

    @JoinColumn(name = "SECTION_ID", referencedColumnName = "ID")
    @ManyToOne
    var sectionId: SectionsEntity? = null

    @Transient
    var confirmSectionId: Long? = 0

    @JoinColumn(name = "REGION_ID", referencedColumnName = "ID")
    @ManyToOne
    var regionId: RegionsEntity? = null

    @Transient
    var confirmRegionId: Long? = 0

    @JoinColumn(name = "COUNTY_ID", referencedColumnName = "ID")
    @ManyToOne
    var countyID: CountiesEntity? = null

    @Transient
    var confirmCountyId: Long? = 0

    @JoinColumn(name = "TOWN_ID", referencedColumnName = "ID")
    @ManyToOne
    var townID: TownsEntity? = null

    @Transient
    var confirmTownId: Long? = 0

    @JoinColumn(name = "SUBSECTION_L1_ID", referencedColumnName = "ID")
    @ManyToOne
    var subSectionL1Id: SubSectionsLevel1Entity? = null

    @Transient
    var confirmSubSectionL1Id: Long? = 0

    @JoinColumn(name = "SUBSECTION_L2_ID", referencedColumnName = "ID")
    @ManyToOne
    var subSectionL2Id: SubSectionsLevel2Entity? = null

    @Transient
    var confirmSubSectionL2Id: Long? = 0

    @JoinColumn(name = "DIRECTORATE_ID", referencedColumnName = "ID")
    @ManyToOne
    var directorateId: DirectoratesEntity? = null

    @Transient
    var confirmDirectorateId: Long? = 0

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as UserProfilesEntity
        return id == that.id &&
                status == that.status &&
                userProfileUuid == that.userProfileUuid &&
                descriptions == that.descriptions &&
                confirmDepartmentId == that.confirmDepartmentId &&
                confirmDesignationId == that.confirmDesignationId &&
                confirmDirectorateId == that.confirmDirectorateId &&
                confirmDivisionId == that.confirmDivisionId &&
                confirmRegionId == that.confirmRegionId &&
                confirmSectionId == that.confirmSectionId &&
                confirmSubRegionId == that.confirmSubRegionId &&
                confirmSubSectionL1Id == that.confirmSubSectionL1Id &&
                confirmSubSectionL2Id == that.confirmSubSectionL2Id &&
                confirmTownId == that.confirmTownId &&
                confirmCountyId == that.confirmCountyId &&
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
                userProfileUuid,
                status,
                descriptions,
                confirmTownId,
                confirmCountyId,
                confirmDirectorateId,
                confirmDivisionId,
                confirmDepartmentId,
                confirmDesignationId,
                confirmRegionId,
                confirmSectionId,
                confirmSubRegionId,
                confirmSubSectionL1Id,
                confirmSubSectionL2Id,
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