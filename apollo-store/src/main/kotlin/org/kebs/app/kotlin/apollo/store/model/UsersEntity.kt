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
import java.sql.Timestamp
import javax.persistence.*


@Entity
@Table(name = "DAT_KEBS_USERS")
class UsersEntity : Serializable {


    @Column(name = "ID")
    @Id
    @SequenceGenerator(name = "DAT_KEBS_USERS_SEQ_GEN", sequenceName = "DAT_KEBS_USERS_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_USERS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long? = null

    @Column(name = "FIRST_NAME")
    @Basic
    var firstName: String? = null

    @Column(name = "USER_UUID")
    @Basic
    var userUuid: String? = null

    @Column(name = "LAST_NAME")
    @Basic
    var lastName: String? = null

    @Column(name = "USER_NAME")
    @Basic
    var userName: String? = null

    @Column(name = "WORK_CONTACT_NUMBER")
    @Basic
    var workContactNumber: String? = null

    @Column(name = "PERSONAL_CONTACT_NUMBER")
    @Basic
    var personalContactNumber: String? = null

    @Column(name = "CREDENTIALS")
    @Basic
    var credentials: String? = null

    @Transient
    var confirmCredentials: String? = ""

    @Transient
    var confirmUserType: Long? = 0

    @Column(name = "EMAIL")
    @Basic
    var email: String? = ""

    @Column(name = "CELL_PHONE")
    @Basic
    var cellphone: String? = null

    @Column(name = "ENABLED")
    @Basic
    var enabled: Int = 0

    @Column(name = "USER_PROFILE_STATUS")
    @Basic
    var userProfileStatus: Int? = 0

//    val isEnabled = enabled >0

    @Column(name = "ACCOUNT_EXPIRED")
    @Basic
    var accountExpired: Int = 0
//    var isAccountExpired = accountExpired <=0


    @Column(name = "ACCOUNT_LOCKED")
    @Basic
    var accountLocked: Int = 0
//    val isAccountLocked = accountLocked <=0

    @Column(name = "CREDENTIALS_EXPIRED")
    @Basic
    var credentialsExpired: Int = 0

//    @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")
//    @ManyToOne
//    var roleId: UserRolesEntity? = null

    @Column(name = "MANUFACTURE_PROFILE")
    @Basic
    var manufactureProfile: Int? = null

    @Column(name = "IMPORTER_PROFILE")
    @Basic
    var importerProfile: Int? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int = 0

    @Column(name = "REGISTRATION_DATE")
    @Basic
    var registrationDate: Date? = null

    @Column(name = "APPROVED_DATE")
    @Basic
    var approvedDate: Timestamp? = null

    @Column(name = "EMAIL_ACTIVATION_STATUS")
    @Basic
    var emailActivationStatus: Int = 0

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

    @Column(name = "USER_TYPE")
    @Basic
    var userTypes: Long? = null

    @Column(name = "USER_REG_NO")
    @Basic
    var userRegNo: String? = null

    @Column(name = "USER_PIN_ID_NUMBER")
    @Basic
    var userPinIdNumber: String? = null

    @Column(name = "TYPE_OF_USER")
    @Basic
    var typeOfUser: Int? = null


//    @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")
//    @ManyToOne
//    var roleId: UserRolesEntity? = null

    @Column(name = "TITLE")
    @Basic
    var title: Long? = null

    @Column(name = "COMPANY_ID")
    @Basic
    var companyId: Long? = null

    @Column(name = "PLANT_ID")
    @Basic
    var plantId: Long? = null

//    override fun toString(): String {
//
//        return "UsersEntity(id=$id, userUuid=$userUuid,personalContactNumber=$personalContactNumber, workContactNumber=$workContactNumber,firstName=$firstName, lastName=$lastName, userName=$userName, confirmUserType=$confirmUserType, email=$email, enabled=$enabled, userProfileStatus=$userProfileStatus, accountExpired=$accountExpired, accountLocked=$accountLocked, credentialsExpired=$credentialsExpired, status=$status, registrationDate=$registrationDate, approvedDate=$approvedDate, varField1=$varField1, varField2=$varField2, varField3=$varField3, varField4=$varField4, varField5=$varField5, varField6=$varField6, varField7=$varField7, varField8=$varField8, varField9=$varField9, varField10=$varField10, createdBy=$createdBy, createdOn=$createdOn, modifiedBy=$modifiedBy, modifiedOn=$modifiedOn, deleteBy=$deleteBy, deletedOn=$deletedOn, userTypes=$userTypes, roleId=$roleId, title=$title)"
//    }

//    @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")
//    @ManyToOne
//    var roleId: UserRolesEntity? = null

//    @OneToMany(mappedBy = "userId")
//    var userContactDetails: MutableList<ContactDetailsEntity> = mutableListOf()

//    @OneToMany(mappedBy = "userId", fetch = FetchType.LAZY)
//    var usersRoles: MutableList<UserRolesPrivilegesEntity> = mutableListOf()

//    @OneToMany(mappedBy = "userId", fetch = FetchType.LAZY)
//    var manufacturers: MutableList<ManufacturersEntity> = mutableListOf()


//    @OneToMany(mappedBy = "userId")
//    var paymentsLogs: Collection<PaymentsLogEntity>? = null

//    @JoinColumn(name = "LOCATION_ID", referencedColumnName = "ID")
//    @ManyToOne
//    var locationId: LocationsEntity? = null


}