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

package org.kebs.app.kotlin.apollo.store.repo


import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.registration.*
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.springframework.data.domain.Pageable
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface IUserRepository : HazelcastRepository<UsersEntity, Long>, JpaSpecificationExecutor<UsersEntity> {
    //    @Query("SELECT u.Id, u.firstName, u.lastName, u.notifs, u.role, u.status from datKebsUsers u where u.status=?1")
    fun findByStatus(status: Int): List<UsersEntity>

    fun findAllByOrderByIdAsc(): List<UsersEntity>

    fun findAllByUserTypes(userType: Long): List<UsersEntity>?

    @Query(
        "select DKU.*  from DAT_KEBS_USERS DKU where DKU.ID in(select USER_ID from CFG_USER_ROLES_ASSIGNMENTS where ROLE_ID in (:profileIds)) and DKU.ID in (:cfsUserIds)",
        nativeQuery = true
    )
    fun findUsersInCfsAndProfiles(
        @Param("profileIds") profileIds: List<Long>,
        @Param("cfsUserIds") cfsUserIds: List<Long>
    ): List<UsersEntity>

    //    @Query("SELECT u.Id, u.firstName, u.lastName, u.notifs, u.role, u.status from datKebsUsers u where u.notifs=?1")
    fun findByEmail(email: String): UsersEntity?


    //    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    fun findByUserName(userName: String): UsersEntity?

    fun findByUserPinIdNumber(userPinIdNumber: String): UsersEntity?

    fun findFirstByIdAndStatus(id: Long, status: Int): UsersEntity?
    fun findAllById(supervisorId: Long?): MutableIterable<UsersEntity>

    fun findAllByCompanyIdAndPlantId(companyId: Long, plantId: Long): List<UsersEntity>

    @Query(
        "SELECT DISTINCT u.* FROM CFG_USER_ROLES_ASSIGNMENTS cura, DAT_KEBS_USERS u where cura.ROLE_ID != 1 and cura.USER_ID = u.ID and u.STATUS = :status and cura.STATUS = 1",
        nativeQuery = true
    )
    fun findRbacUsersByStatus(@Param("status") status: Int): List<UsersEntity>?
    fun findAllByIdIn(userList: MutableList<Long?>): List<UsersEntity>?

    @Query("SELECT u from UsersEntity u where u.userName = :userName or u.email = :email or u.firstName = :firstName or u.lastName= :lastName")
    fun findUsingSearchValues(
        @Param("userName") userName: String?,
        @Param("email") email: String?,
        @Param("firstName") firstName: String?,
        @Param("lastName") lastName: String?
    ): List<UsersEntity>?

    @Query(
//        "SELECT distinct u.* from DAT_KEBS_PERMIT_TRANSACTION p, DAT_KEBS_MANUFACTURE_PLANT_DETAILS b, DAT_KEBS_USER_PROFILES pf, CFG_USER_ROLES_ASSIGNMENTS r," +
//                " DAT_KEBS_USERS u where p.ATTACHED_PLANT_ID=b.ID and b.REGION= pf.REGION_ID and p.SECTION_ID = pf.SECTION_ID and pf.USER_ID = r.USER_ID and u.ID = pf.USER_ID " +
//                "and r.ROLE_ID = :roleId and pf.SECTION_ID = :sectionId and pf.REGION_ID = :regionId and pf.STATUS = :status",
//        nativeQuery = true
        "SELECT distinct u.* from APOLLO.DAT_KEBS_PERMIT_TRANSACTION p, APOLLO.DAT_KEBS_MANUFACTURE_PLANT_DETAILS b, APOLLO.DAT_KEBS_USER_PROFILES pf, " +
                "APOLLO.CFG_USER_SECTION_ASSIGNMENTS q ,APOLLO.CFG_USER_ROLES_ASSIGNMENTS r,APOLLO.DAT_KEBS_USERS u where p.ATTACHED_PLANT_ID=b.ID and b.REGION= pf.REGION_ID" +
                " and p.SECTION_ID = q.SECTION_ID and pf.USER_ID = r.USER_ID and u.ID = pf.USER_ID and r.ROLE_ID = :roleId and q.SECTION_ID = :sectionId " +
                "and pf.REGION_ID = :regionId and pf.STATUS = :status",
        nativeQuery = true
    )
    fun findOfficerPermitUsersBySectionAndRegion(
        @Param("roleId") roleId: Long,
        @Param("sectionId") sectionId: Long,
        @Param("regionId") regionId: Long,
        @Param("status") status: Int
    ): List<UsersEntity>?

    @Query(
        "SELECT DISTINCT u.* FROM CFG_USER_SECTION_ASSIGNMENTS s, CFG_USER_ROLES_ASSIGNMENTS r, DAT_KEBS_USER_PROFILES pf,  DAT_KEBS_USERS u" +
                " WHERE  pf.USER_ID = r.USER_ID and u.ID = pf.USER_ID and pf.REGION_ID = :regionId  and pf.STATUS = :status" +
                " AND u.ENABLED = :status and r.ROLE_ID = :roleId and s.SECTION_ID = :sectionId",
        nativeQuery = true
    )
    fun findOfficerPermitUsersBySectionAndRegionFromSectionUserDetails(
        @Param("roleId") roleId: Long,
        @Param("sectionId") sectionId: Long,
        @Param("regionId") regionId: Long,
        @Param("status") status: Int
    ): List<UsersEntity>?

    @Query(
        "SELECT DISTINCT u.* FROM CFG_USER_ROLES_ASSIGNMENTS r, DAT_KEBS_USER_PROFILES pf,  " +
                "DAT_KEBS_USERS u WHERE  pf.USER_ID = r.USER_ID and u.ID = pf.USER_ID and pf.REGION_ID =:regionId and pf.COUNTY_ID =:countyId  and pf.STATUS =:status\n" +
                "AND u.ENABLED =:status and r.ROLE_ID =:roleId AND r.STATUS=:status",
        nativeQuery = true
    )
    fun findOfficerUsersByRegionAndCountyAndRoleFromUserDetails(
        @Param("roleId") roleId: Long,
        @Param("countyId") countyId: Long,
        @Param("regionId") regionId: Long,
        @Param("status") status: Int
    ): List<UsersEntity>?

    fun findAllByUserNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
        userName: String?,
        email: String?,
        firstName: String?,
        lastName: String?
    ): List<UsersEntity>?

    @Query(
        "select distinct u.*from CFG_ROLES_PRIVILEGES rp, CFG_USER_PRIVILEGES p, CFG_USER_ROLES_ASSIGNMENTS ra, DAT_KEBS_USERS u where rp.ROLES_ID = ra.ROLE_ID and ra.USER_ID = u.ID and rp.PRIVILEGE_ID = :authorityId",
        nativeQuery = true
    )
    fun getUsersWithAuthorizationId(@Param("authorityId") authorityId: Long): List<UsersEntity>?


}

@Repository
interface IUserPrivilegesRepository : HazelcastRepository<UserPrivilegesEntity, Long> {
    @Query(
        "SELECT DISTINCT CUP.* FROM CFG_ROLES_PRIVILEGES rp, CFG_USER_ROLES cur , CFG_USER_PRIVILEGES cup  WHERE CUP.ID = rp.PRIVILEGE_ID  AND CUR.ID = rp.ROLES_ID  AND rp.ROLES_ID  IN (:userRoles)  and rp.STATUS = :status",
        nativeQuery = true
    )
    fun findAuthoritiesList(
        @Param("userRoles") userRoles: List<Long?>,
        @Param("status") status: Int
    ): List<UserPrivilegesEntity>?

    @Query(
        value = "SELECT CUP.* FROM CFG_ROLES_PRIVILEGES crp ,CFG_USER_PRIVILEGES cup WHERE crp.PRIVILEGE_ID  = CUP.ID AND crp.ROLES_ID  = :roleId AND crp.STATUS  = :rStatus",
        nativeQuery = true
    )
    fun findPrivilegesForRole(
        @Param("roleId") roleId: Long,
        @Param("rStatus") rStatus: Int
    ): List<UserPrivilegesEntity>?

    fun findByStatus(status: Int): List<UserPrivilegesEntity>?

    fun findByName(name: String): UserPrivilegesEntity

    @Query(
        value = "SELECT DISTINCT ROLE_ID FROM CFG_ROLES_PRIVILEGES rp left join CFG_USER_ROLES cur on(rp.ROLES_ID=cur.ID)  left join CFG_USER_PRIVILEGES cup  on(rp.PRIVILEGE_ID=cup.ID) WHERE rp.STATUS = 1 and NAME=:name",
        nativeQuery = true
    )
    fun findRoleIdsByRoleName(@Param("name") name: String): List<Long>
}

@Repository
interface IUserRolesPrivilegesRepository : HazelcastRepository<RolesPrivilegesEntity, Long> {
    fun findByUserRolesAndStatus(userRoles: UserRolesEntity?, status: Int?): List<RolesPrivilegesEntity>?

    fun findByPrivilege(privilege: UserPrivilegesEntity): List<RolesPrivilegesEntity>?


    @Query(
        value = "SELECT * FROM CFG_ROLES_PRIVILEGES p WHERE p.ROLES_ID  IN (:userRoles) and STATUS = :status",
        nativeQuery = true
    )
    fun findSomeData(
        @Param("userRoles") userRoles: List<UserRolesEntity?>,
        @Param("status") status: Int
    ): List<RolesPrivilegesEntity>?

    fun findByStatus(status: Int): List<RolesPrivilegesEntity>?
    fun findByUserRolesAndPrivilegeAndStatus(
        role: UserRolesEntity,
        privilege: UserPrivilegesEntity,
        status: Int
    ): RolesPrivilegesEntity?
}

@Repository
interface IUserRolesRepository : HazelcastRepository<UserRolesEntity, Long> {
    fun findByRoleNameAndStatus(roleName: String, status: Int): UserRolesEntity?
    fun findByDesignationIdAndStatus(designationId: Long, status: Int): UserRolesEntity?
    fun findByStatus(status: Int): List<UserRolesEntity>?

    @Query(
        value = "SELECT DISTINCT CUR.*  FROM CFG_ROLES_PRIVILEGES p, CFG_USER_ROLES cur WHERE P.ROLES_ID = cur.ID and p.STATUS = :status AND cur.STATUS =:rStatus",
        nativeQuery = true
    )
    fun findOnlyDistinctRoles(@Param("status") status: Int, @Param("rStatus") rStatus: Int): List<UserRolesEntity>?

    @Query(
        "SELECT r.* FROM CFG_USER_ROLES_ASSIGNMENTS UR, CFG_USER_ROLES R WHERE UR.ROLE_ID = R.ID AND UR.STATUS = :status AND UR.USER_ID = :userId order by r.ID",
        nativeQuery = true
    )
    fun findRbacRolesByUserId(@Param("userId") userId: Long, @Param("status") status: Int): List<UserRolesEntity>?
}

@Repository
interface IUserRoleAssignmentsRepository : HazelcastRepository<UserRoleAssignmentsEntity, Long> {
    fun findByUserIdAndStatus(userId: Long, status: Int): List<UserRoleAssignmentsEntity>?
    fun findByRoleIdAndStatus(roleId: Long, status: Int): List<UserRoleAssignmentsEntity>?
    fun findByRoleId(roleId: Long): List<UserRoleAssignmentsEntity>
    fun findByUserId(userId: Long): UserRoleAssignmentsEntity?

    @Query(
        "select  count(*)  from DAT_KEBS_USER_PROFILES DKUP left join CFG_USER_ROLES_ASSIGNMENTS CURA on(DKUP.USER_ID=CURA.USER_ID) left join CFG_USER_ROLES CUR on (CURA.ROLE_ID = CUR.ID) where upper(ROLE_NAME)=upper(:roleName) and CURA.STATUS=:assignmentStatus and DKUP.USER_ID=:userId",
        nativeQuery = true
    )
    fun checkUserHasRole(
        @Param("roleName") roleName: String,
        @Param("assignmentStatus") status: Int,
        @Param("userId") userId: Long
    ): Int

    @Query(
        "SELECT * FROM CFG_USER_ROLES_ASSIGNMENTS cura WHERE CURA.USER_ID = :userId AND STATUS = :status",
        nativeQuery = true
    )
    fun findUserRoleAssignments(
        @Param("userId") userId: Long,
        @Param("status") status: Int
    ): List<UserRoleAssignmentsEntity>?

    fun findByUserIdAndRoleIdAndStatus(userId: Long, roleId: Long, status: Int): UserRoleAssignmentsEntity?
    fun findByUserIdAndRoleId(userId: Long, roleId: Long): UserRoleAssignmentsEntity?

    @Query(
        value = "SELECT x.ROLE_ID AS ID FROM CFG_USER_ROLES_ASSIGNMENTS x  WHERE  x.USER_ID = :userId",
        nativeQuery = true
    )
    fun getRoleByUserId(@Param("userId") userId: Long?): List<UserRoleHolder>

}

@Repository
interface IUserSectionAssignmentsRepository : HazelcastRepository<UserSectionAssignmentsEntity, Long> {
    fun findByUserIdAndStatus(userId: Long, status: Int): List<UserSectionAssignmentsEntity>?
    fun findBySectionIdAndStatus(sectionId: Long, status: Int): List<UserSectionAssignmentsEntity>?
    fun findBySectionId(sectionId: Long): List<UserSectionAssignmentsEntity>
    fun findByUserId(userId: Long): UserSectionAssignmentsEntity?


    @Query(
        "SELECT * FROM CFG_USER_SECTION_ASSIGNMENTS cura WHERE CURA.USER_ID = :userId AND STATUS = :status",
        nativeQuery = true
    )
    fun findUserRoleAssignments(
        @Param("userId") userId: Long,
        @Param("status") status: Int
    ): List<UserSectionAssignmentsEntity>?

    fun findByUserIdAndSectionIdAndStatus(userId: Long, sectionId: Long, status: Int): UserSectionAssignmentsEntity?
    fun findByUserIdAndSectionId(userId: Long, sectionId: Long): UserSectionAssignmentsEntity?

}

@Repository
interface IUserTypesEntityRepository : HazelcastRepository<UserTypesEntity, Long> {
    fun findByStatus(status: Int): List<UserTypesEntity>
    fun findByTypeNameAndStatus(typeName: String, status: Int): UserTypesEntity?


    @Query(
        "SELECT u.* FROM CFG_USER_TYPES U INNER JOIN DAT_KEBS_USERS P ON U.DEFAULT_ROLE = P.USER_TYPE WHERE P.ID=:userId",
        nativeQuery = true
    )
    fun findAllUserRolesAssignedToUser(@Param("userId") userId: Long): List<UserTypesEntity>?

}

@Repository
interface IUserRequestsRepository : HazelcastRepository<UserRequestsEntity, Long>,
    JpaSpecificationExecutor<UsersEntity> {
    fun findByStatus(status: Int): List<UserRequestsEntity>?
    fun findByUserIdAndRequestId(userId: Long, requestId: Long): UserRequestsEntity?
}

@Repository
interface IUserRequestTypesRepository : HazelcastRepository<UserRequestTypesEntity, Long> {
    fun findByStatus(status: Int): List<UserRequestTypesEntity>?
    fun findAllByQaRequests(qaRequests: Int): List<UserRequestTypesEntity>?
    fun findByStatusOrderByUserRequest(status: Int): List<UserRequestTypesEntity>?


}

@Repository
interface BranchDetailsRepository : HazelcastRepository<BranchDetailsEntity, Long>


@Repository
interface ICompanyProfileRepository : HazelcastRepository<CompanyProfileEntity, Long> {
    fun findByStatus(status: Int): List<CompanyProfileEntity>?
    fun findByKraPin(kraPin: String): CompanyProfileEntity?
    fun findByUserId(userId: Long): CompanyProfileEntity?
    fun findByRegistrationNumber(registrationNumber: String): CompanyProfileEntity?
    fun findByManufactureStatus(status: Int): List<CompanyProfileEntity>?
    fun findAllByOrderByIdDesc(pageable: Pageable): List<CompanyProfileEntity>?
    fun findAllByUserId(userId: Long): List<CompanyProfileEntity>
    fun findCompanyByUserId(userId: Long): MutableList<CompanyProfileEntity>
    fun findAllByFirmCategoryAndStatus(firmCategory: Long, status: Int): List<CompanyProfileEntity>?

    @Query(
        value = "SELECT c.ID as id,c.NAME as name,c.PHYSICAL_ADDRESS as physicalAddress,c.KRA_PIN as kraPin,c.MANUFACTURE_STATUS as manufactureStatus,c.REGISTRATION_NUMBER as registrationNumber,c.POSTAL_ADDRESS as postalAddress,c.PLOT_NUMBER as plotNumber,c.COMPANY_EMAIL as companyEmail," +
                "c.COMPANY_TELEPHONE as companyTelephone,c.YEARLY_TURNOVER as yearlyTurnover,l.NAME as businessLineName,n.NAME as businessNatureName,c.BUILDING_NAME as buildingName,c.STREET_NAME as streetName,r.REGION as regionName,s.COUNTY as countyName,c.FIRM_CATEGORY as firmCategory,t.TOWN as townName," +
                "c.BUSINESS_LINES as businessLines,c.BUSINESS_NATURES as businessNatures,c.REGION as region,c.TOWN as town,c.COUNTY as county,c.USER_ID as userId," +
                "c.DIRECTOR_ID_NUMBER as directorIdNumber,c.ENTRY_NUMBER as entryNumber,c.STATUS as status,c.CLOSED_COMMODITY_MANUFACTURED as closedCommodityManufactured,c.CLOSED_CONTRACTS_UNDERTAKEN as closedContractsUndertaken,c.TASK_TYPE as taskType,c.TASK_ID as taskId,c.OWNERSHIP as ownership,c.BRANCH_NAME as branchName," +
                "c.CLOSURE_OF_OPERATIONS as closureOfOperations,c.TYPE_OF_MANUFACTURE as typeOfManufacture,c.OTHER_BUSINESS_NATURE_TYPE as otherBusinessNatureType" +
                " FROM DAT_KEBS_COMPANY_PROFILE c Join CFG_KEBS_BUSINESS_NATURE n ON c.BUSINESS_NATURES = n.ID JOIN CFG_KEBS_BUSINESS_LINES l ON c.BUSINESS_LINES = l.ID JOIN CFG_KEBS_REGIONS r ON c.REGION = r.ID JOIN CFG_KEBS_TOWNS t ON c.TOWN = t.ID JOIN CFG_KEBS_COUNTIES s ON c.COUNTY = s.ID   WHERE c.ASSIGN_STATUS='0'",
        nativeQuery = true
    )
    fun getManufacturerList(): MutableList<ManufactureListHolder>

    @Query(
        value = "SELECT ID as id,ENTRY_NUMBER as entryNumber,KRA_PIN as kraPin,NAME as name,POSTAL_ADDRESS as postalAddress,COMPANY_TELEPHONE as companyTelephone," +
                "COMPANY_EMAIL as companyEmail,STREET_NAME as streetName,BUSINESS_LINES as businessLines,BUSINESS_NATURES as businessNatures,BUSINESS_LINE_NAME as businessLineName," +
                "BUSINESS_NATURE_NAME as businessNatureName,REGION as region,REGION_NAME as regionName,TOWN as town,TOWN_NAME as townName, cast(CREATED_ON as varchar(200)) AS CreatedOn  FROM DAT_KEBS_COMPANY_PROFILE ",
        nativeQuery = true
    )
    fun getRegisteredFirms(): MutableList<RegisteredFirms>

    @Query(
        value = "SELECT ID as id,ENTRY_NUMBER as entryNumber,KRA_PIN as kraPin,NAME as name,POSTAL_ADDRESS as postalAddress,BRANCH_NAME as adminLocation," +
                "STREET_NAME as streetName,BUSINESS_LINES as businessLines,BUSINESS_NATURES as businessNatures,BUSINESS_LINE_NAME as businessLineName," +
                "BUSINESS_NATURE_NAME as businessNatureName,REGION as region,REGION_NAME as regionName,TOWN as town,TOWN_NAME as townName, cast(CREATED_ON as varchar(200)) AS CreatedOn " +
                " FROM DAT_KEBS_COMPANY_PROFILE  WHERE STATUS='1'",
        nativeQuery = true
    )
    fun getActiveFirms(): MutableList<RegisteredFirms>

    @Query(
        value = "SELECT ID as id,ENTRY_NUMBER as entryNumber,KRA_PIN as kraPin,NAME as name,POSTAL_ADDRESS as postalAddress,BRANCH_NAME as adminLocation," +
                "STREET_NAME as streetName,BUSINESS_LINES as businessLines,BUSINESS_NATURES as businessNatures,BUSINESS_LINE_NAME as businessLineName," +
                "BUSINESS_NATURE_NAME as businessNatureName,REGION as region,REGION_NAME as regionName,TOWN as town,TOWN_NAME as townName, cast(CREATED_ON as varchar(200)) AS CreatedOn" +
                " FROM DAT_KEBS_COMPANY_PROFILE  WHERE STATUS='4'",
        nativeQuery = true
    )
    fun getDormantFirms(): MutableList<RegisteredFirms>


    @Query(
        value = "SELECT c.ID as id,c.ENTRY_NUMBER as entryNumber,c.KRA_PIN as kraPin,c.NAME as name,c.POSTAL_ADDRESS as postalAddress,c.BRANCH_NAME as adminLocation," +
                "c.STREET_NAME as streetName,c.BUSINESS_LINES as businessLines,c.BUSINESS_NATURES as businessNatures,c.BUSINESS_LINE_NAME as businessLineName," +
                "c.BUSINESS_NATURE_NAME as businessNatureName,c.REGION as region,c.REGION_NAME as regionName,c.TOWN as town,c.TOWN_NAME as townName,cast(c.CREATED_ON as varchar(200)) AS CreatedOn,cast(p.DATE_OF_CLOSURE as varchar(200)) AS dateOfClosure " +
                " FROM DAT_KEBS_COMPANY_PROFILE c JOIN DAT_KEBS_CLOSURE_OF_OPERATIONS p ON c.ID=p.COMPANY_ID WHERE c.STATUS='0'",
        nativeQuery = true
    )
    fun getClosedFirms(): MutableList<RegisteredFirms>

    @Query(
        value = "SELECT ID  FROM DAT_KEBS_COMPANY_PROFILE WHERE USER_ID= :id AND ASSIGN_STATUS='0'",
        nativeQuery = true
    )
    fun getAssignStatus(@Param("id") id: Long?): Long

    @Query(
        value = "SELECT *  FROM DAT_KEBS_COMPANY_PROFILE WHERE ASSIGN_STATUS='2' AND ASSIGNED_TO = :assignedTo",
        nativeQuery = true
    )
    fun getMnCompleteTask(@Param("assignedTo") assignedTo: Long?): MutableList<CompanyProfileEntity>

    @Query(
        value = "SELECT *  FROM DAT_KEBS_COMPANY_PROFILE WHERE ASSIGN_STATUS='1' AND ASSIGNED_TO = :assignedTo ",
        nativeQuery = true
    )
    fun getMnPendingTask(@Param("assignedTo") assignedTo: Long?): MutableList<CompanyProfileEntity>

    @Query(
        value = "SELECT BUSINESS_NATURES  FROM DAT_KEBS_COMPANY_PROFILE WHERE USER_ID= :id",
        nativeQuery = true
    )
    fun getBusinessNature(@Param("id") id: Long?): Long

    @Query(
        value = "SELECT ID  FROM DAT_KEBS_COMPANY_PROFILE WHERE USER_ID= :id",
        nativeQuery = true
    )
    fun getManufactureId(@Param("id") id: Long?): Long?

    @Query(
        value = "SELECT USER_ID  FROM DAT_KEBS_COMPANY_PROFILE WHERE ID= :id",
        nativeQuery = true
    )
    fun getContactPersonId(@Param("id") id: Long?): Long?

    @Query(
        value = "SELECT NAME  FROM DAT_KEBS_COMPANY_PROFILE WHERE USER_ID= :id",
        nativeQuery = true
    )
    fun getComName(@Param("id") id: Long?): String?

    @Query(
        value = "SELECT REGISTRATION_NUMBER  FROM DAT_KEBS_COMPANY_PROFILE WHERE USER_ID= :id",
        nativeQuery = true
    )
    fun getRegNo(@Param("id") id: Long?): String?

    @Query(
        value = "SELECT KRA_PIN  FROM DAT_KEBS_COMPANY_PROFILE WHERE USER_ID= :id",
        nativeQuery = true
    )
    fun getKraPin(@Param("id") id: Long?): String?

    @Query(
        value = "SELECT NAME  FROM DAT_KEBS_COMPANY_PROFILE WHERE ID= :id",
        nativeQuery = true
    )
    fun getCompanyName(@Param("id") id: Long?): String?


    @Query(
        value = "SELECT ENTRY_NUMBER  FROM DAT_KEBS_COMPANY_PROFILE WHERE USER_ID= :id",
        nativeQuery = true
    )
    fun getManufactureEntryNo(@Param("id") id: Long?): Long?

    @Query(
        value = "SELECT ENTRY_NUMBER  FROM LOG_KEBS_STANDARD_LEVY_PAYMENTS ",
        nativeQuery = true
    )
    fun getDistinctManufactureEntryNo(): Long?


    @Query(
        value = "SELECT DISTINCT c.ENTRY_NUMBER as entryNumber,c.ID as companyId,c.NAME as companyName,c.KRA_PIN as kraPin,c.REGISTRATION_NUMBER as registrationNumber FROM DAT_KEBS_COMPANY_PROFILE c, LOG_KEBS_STANDARD_LEVY_PAYMENTS p\n" +
                "WHERE p.ENTRY_NUMBER=c.ENTRY_NUMBER ",
        nativeQuery = true
    )
    fun getLevyPayments(): List<LevyPayments>


    @Query(
        value = "SELECT p.ID as id,p.ENTRY_NUMBER as entryNumber,p.PAYMENT_DATE as paymentDate,p.PAYMENT_AMOUNT as paymentAmount," +
                "c.ID as companyId,c.NAME as companyName,c.KRA_PIN as kraPin,c.REGISTRATION_NUMBER as registrationNumber,c.ASSIGN_STATUS as assignStatus,p.PERIOD_FROM as periodFrom,p.PERIOD_TO as periodTo " +
                " FROM LOG_KEBS_STANDARD_LEVY_PAYMENTS p JOIN DAT_KEBS_COMPANY_PROFILE c ON p.ENTRY_NUMBER=c.ENTRY_NUMBER " +
                "WHERE p.ENTRY_NUMBER= :entryNumber ORDER BY p.ID DESC",
        nativeQuery = true
    )
    fun getManufacturesLevyPayments(@Param("entryNumber") entryNumber: Long?): MutableList<LevyPayments>

    @Query(
        value = "SELECT d.ID as id,p.ENTRY_NUMBER as entryNumber,TO_CHAR(TRUNC(d.PERIOD_FROM),'DD/MM/YYYY') as periodFrom,TO_CHAR(TRUNC(d.PERIOD_TO),'DD/MM/YYYY') as periodTo,h.REQUEST_HEADER_PAYMENT_SLIP_NO as paymentSlipNo,TO_CHAR(TRUNC(h.REQUEST_HEADER_PAYMENT_SLIP_DATE),'DD/MM/YYYY')  as paymentSlipDate," +
                "h.REQUEST_HEADER_PAYMENT_TYPE as paymentType,h.REQUEST_HEADER_TOTAL_DECL_AMT as totalDeclAmt,h.REQUEST_HEADER_TOTAL_PENALTY_AMT as totalPenaltyAmt,h.REQUEST_BANK_REF_NO as bankRefNo," +
                "h.REQUEST_HEADER_TOTAL_PAYMENT_AMT as totalPaymentAmt,h.REQUEST_HEADER_BANK as bankName,d.COMMODITY_TYPE as commodityType,TO_CHAR(TRUNC(p.PAYMENT_DATE),'DD/MM/YYYY') as paymentDate," +
                "d.LEVY_PAID as levyPaid,d.PENALTY_PAID as penaltyPaid,d.QTY_MANF as qtyManf,d.EX_FACT_VAL as exFactVal," +
                "c.ID as companyId,c.NAME as companyName,c.KRA_PIN as kraPin,c.REGISTRATION_NUMBER as registrationNumber,c.ASSIGN_STATUS as assignStatus " +
                " FROM LOG_SL2_PAYMENTS_HEADER h LEFT JOIN LOG_SL2_PAYMENTS_DETAILS d ON h.ID=d.HEADER_ID LEFT JOIN  LOG_KEBS_STANDARD_LEVY_PAYMENTS p ON h.ID=p.PAYMENT_ID LEFT JOIN DAT_KEBS_COMPANY_PROFILE c ON p.ENTRY_NUMBER=c.ENTRY_NUMBER  " +
                "WHERE d.ID= :id  AND d.TRANSACTION_TYPE='DECLARATION'",
        nativeQuery = true
    )
    fun getLevyPaymentsReceipt(@Param("id") id: Long?): MutableList<LevyPayments>


    @Query(
        value = "SELECT d.ID as id,p.ENTRY_NUMBER as entryNumber,d.PERIOD_FROM as periodFrom,d.PERIOD_TO as periodTo,h.REQUEST_HEADER_PAYMENT_SLIP_NO as paymentSlipNo,h.REQUEST_HEADER_PAYMENT_SLIP_DATE as paymentSlipDate," +
                "h.REQUEST_HEADER_PAYMENT_TYPE as paymentType,h.REQUEST_HEADER_TOTAL_DECL_AMT as totalDeclAmt,h.REQUEST_HEADER_TOTAL_PENALTY_AMT as totalPenaltyAmt,h.REQUEST_BANK_REF_NO as bankRefNo," +
                "h.REQUEST_HEADER_TOTAL_PAYMENT_AMT as totalPaymentAmt,h.REQUEST_HEADER_BANK as bankName,d.COMMODITY_TYPE as commodityType,h.TRANSACTION_DATE as paymentDate," +
                "d.LEVY_PAID as levyPaid,d.PENALTY_PAID as penaltyPaid," +
                "c.ID as companyId,c.NAME as companyName,c.KRA_PIN as kraPin,c.REGISTRATION_NUMBER as registrationNumber,c.ASSIGN_STATUS as assignStatus " +
                " FROM LOG_SL2_PAYMENTS_HEADER h LEFT JOIN LOG_SL2_PAYMENTS_DETAILS d ON h.ID=d.HEADER_ID LEFT JOIN  LOG_KEBS_STANDARD_LEVY_PAYMENTS p ON h.ID=p.PAYMENT_ID LEFT JOIN DAT_KEBS_COMPANY_PROFILE c ON p.ENTRY_NUMBER=c.ENTRY_NUMBER  " +
                "WHERE c.ID= :companyId AND d.TRANSACTION_TYPE='DECLARATION' ORDER BY d.ID DESC",
        nativeQuery = true
    )
    fun getManufacturesLevyPaymentsList(@Param("companyId") companyId: Long?): MutableList<LevyPayments>

    @Query(
        value = "SELECT d.ID as id,h.REQUEST_HEADER_ENTRY_NO as entryNumber,c.KRA_PIN as kraPin,c.NAME as companyName,c.ID as companyId,c.BUSINESS_LINES as businessLines,c.BUSINESS_NATURES as businessNatures,c.BUSINESS_LINE_NAME as businessLineName," +
                "c.BUSINESS_NATURE_NAME as businessNatureName,c.REGION as region,c.REGION_NAME as regionName,d.PERIOD_FROM as periodFrom,d.PERIOD_TO as periodTo,h.REQUEST_HEADER_PAYMENT_SLIP_DATE as paymentSlipDate," +
                "h.TRANSACTION_DATE as paymentDate,d.LEVY_PAID as levyPaid" +
                " FROM LOG_SL2_PAYMENTS_HEADER h LEFT JOIN LOG_SL2_PAYMENTS_DETAILS d ON h.ID=d.HEADER_ID  LEFT JOIN DAT_KEBS_COMPANY_PROFILE c ON h.REQUEST_HEADER_ENTRY_NO=c.ENTRY_NUMBER  " +
                "WHERE  d.TRANSACTION_TYPE='DECLARATION' ORDER BY d.ID DESC",
        nativeQuery = true
    )
    fun getAllLevyPayments(): MutableList<AllLevyPayments>

    @Query(
        value = "SELECT d.ID as id,h.REQUEST_HEADER_ENTRY_NO as entryNumber,c.KRA_PIN as kraPin,c.NAME as companyName,c.ID as companyId," +
                "d.PERIOD_FROM as periodFrom,d.PERIOD_TO as periodTo,h.TRANSACTION_DATE as paymentDate,d.PENALTY_PAID as penaltyPaid," +
                "h.REQUEST_HEADER_TOTAL_PENALTY_AMT as totalPenaltyAmt,p.NET_PENALTY_AMT as amountDue" +
                " FROM LOG_SL2_PAYMENTS_HEADER h LEFT JOIN LOG_SL2_PAYMENTS_DETAILS d ON h.ID=d.HEADER_ID " +
                "LEFT JOIN LOG_KEBS_STANDARD_LEVY_PAYMENTS p ON h.ID=p.PAYMENT_ID  LEFT JOIN DAT_KEBS_COMPANY_PROFILE c ON h.REQUEST_HEADER_ENTRY_NO=c.ENTRY_NUMBER  " +
                "WHERE  d.TRANSACTION_TYPE='PENALTY' ORDER BY d.ID DESC",
        nativeQuery = true
    )
    fun getPenaltyReport(): MutableList<AllLevyPayments>


    @Query(
        value = "SELECT d.ID as id,p.ENTRY_NUMBER as entryNumber,cast(d.PERIOD_FROM as varchar(200)) as periodFrom ,cast(d.PERIOD_TO as varchar(200)) as periodTo,h.REQUEST_HEADER_PAYMENT_SLIP_NO as paymentSlipNo,cast(h.REQUEST_HEADER_PAYMENT_SLIP_DATE as varchar(200)) as paymentSlipDate," +
                "h.REQUEST_HEADER_PAYMENT_TYPE as paymentType,h.REQUEST_HEADER_TOTAL_DECL_AMT as totalDeclAmt,h.REQUEST_HEADER_TOTAL_PENALTY_AMT as totalPenaltyAmt,h.REQUEST_BANK_REF_NO as bankRefNo," +
                "h.REQUEST_HEADER_TOTAL_PAYMENT_AMT as totalPaymentAmt,h.REQUEST_HEADER_BANK as bankName,d.COMMODITY_TYPE as commodityType,cast(h.TRANSACTION_DATE as varchar(200)) as paymentDate," +
                "d.LEVY_PAID as levyPaid,d.PENALTY_PAID as penaltyPaid," +
                "c.ID as companyId,c.NAME as companyName,c.KRA_PIN as kraPin,c.REGISTRATION_NUMBER as registrationNumber,c.ASSIGN_STATUS as assignStatus " +
                " FROM LOG_SL2_PAYMENTS_HEADER h LEFT JOIN LOG_SL2_PAYMENTS_DETAILS d ON h.ID=d.HEADER_ID LEFT JOIN  LOG_KEBS_STANDARD_LEVY_PAYMENTS p ON h.ID=p.PAYMENT_ID LEFT JOIN DAT_KEBS_COMPANY_PROFILE c ON p.ENTRY_NUMBER=c.ENTRY_NUMBER  " +
                "WHERE c.ENTRY_NUMBER= :entryNumber ORDER BY d.ID DESC",
        nativeQuery = true
    )
    fun getManufacturesPayments(@Param("entryNumber") entryNumber: String?): MutableList<LevyPayment>

    @Query(
        value = "SELECT COMPANY_EMAIL as companyEmail,NAME as companyName  FROM DAT_KEBS_COMPANY_PROFILE WHERE STATUS='4'",
        nativeQuery = true
    )
    fun getManufactureEmailAddressList(): MutableList<EmailListHolder>

    @Query(
        value = "SELECT DISTINCT c.ENTRY_NUMBER as entryNumber,c.ID as companyId,c.NAME as companyName,c.KRA_PIN as kraPin,c.REGISTRATION_NUMBER as registrationNumber,p.STATUS as status  FROM DAT_KEBS_COMPANY_PROFILE c, LOG_KEBS_STANDARD_LEVY_PAYMENTS p\n" +
                "WHERE p.ENTRY_NUMBER=c.ENTRY_NUMBER AND p.STATUS='1'",
        nativeQuery = true
    )
    fun getLevyPenalty(): MutableList<LevyPenalty>

    @Query(
        value = "SELECT p.ID as id,p.ENTRY_NUMBER as entryNumber,p.LEVY_PENALTY_PAYMENT_DATE as paymentDate,p.PAYMENT_AMOUNT as paymentAmount,p.NET_PENALTY_AMT as amountDue,p.PENALTY_APPLIED as penalty,p.LEVY_DUE_DATE as levyDueDate," +
                "c.ID as companyId,c.NAME as companyName,c.KRA_PIN as kraPin,c.REGISTRATION_NUMBER as registrationNumber,c.ASSIGN_STATUS as assignStatus " +
                " FROM LOG_KEBS_STANDARD_LEVY_PAYMENTS p JOIN DAT_KEBS_COMPANY_PROFILE c ON p.ENTRY_NUMBER=c.ENTRY_NUMBER  " +
                "WHERE p.ENTRY_NUMBER= :entryNumber ORDER BY p.ID DESC",
        nativeQuery = true
    )
    fun getManufacturesLevyPenalty(@Param("entryNumber") entryNumber: Long?): MutableList<LevyPenalty>

    @Query(
        value = "SELECT p.ID as id,p.ENTRY_NUMBER as entryNumber,p.LEVY_PENALTY_PAYMENT_DATE as paymentDate,p.LEVY_PENALTY_PAYABLE as paymentAmount,p.NET_PENALTY_AMT as amountDue,p.PENALTY_APPLIED as penalty,p.LEVY_DUE_DATE as levyDueDate," +
                "c.ID as companyId,c.NAME as companyName,c.KRA_PIN as kraPin,c.REGISTRATION_NUMBER as registrationNumber,c.ASSIGN_STATUS as assignStatus,p.PERIOD_FROM as periodFrom,p.PERIOD_TO as periodTo " +
                " FROM LOG_KEBS_STANDARD_LEVY_PAYMENTS p JOIN DAT_KEBS_COMPANY_PROFILE c ON p.ENTRY_NUMBER=c.ENTRY_NUMBER " +
                "WHERE c.ID= :companyId ORDER BY p.ID DESC",
        nativeQuery = true
    )
    fun getManufacturesLevyPenaltyList(@Param("companyId") companyId: Long?): MutableList<LevyPenalty>

    @Query(
        value = "SELECT DISTINCT c.ENTRY_NUMBER as entryNumber,c.ID as companyId,c.NAME as companyName,c.KRA_PIN as kraPin," +
                "c.REGISTRATION_NUMBER as registrationNumber,p.STATUS as status,c.POSTAL_ADDRESS as postalAddress," +
                "c.BUSINESS_LINES as businessLines,c.BUSINESS_NATURES as businessNatures,c.BUSINESS_LINE_NAME as businessLineName," +
                "c.BUSINESS_NATURE_NAME as businessNatureName  FROM DAT_KEBS_COMPANY_PROFILE c, LOG_KEBS_STANDARD_LEVY_PAYMENTS p\n" +
                "WHERE p.ENTRY_NUMBER=c.ENTRY_NUMBER AND p.OVERDUE='1'",
        nativeQuery = true
    )
    fun getLevyDefaulters(): MutableList<LevyPenalty>

    @Query(
        value = "SELECT COUNT(ENTRY_NUMBER)  FROM LOG_KEBS_STANDARD_LEVY_PAYMENTS WHERE ENTRY_NUMBER=:entryNumber  AND OVERDUE='1'",
        nativeQuery = true
    )
    fun findRecordCount(entryNumber: String): Long

    @Query(
        value = "SELECT p.ID as PenaltyOrderNo, c.ENTRY_NUMBER as entryNo,c.KRA_PIN as kraPin,c.NAME as manufacName,p.PERIOD_FROM as periodFrom,p.PERIOD_TO as periodTo,p.PENALTY_DATE as penaltyGenDate,p.LEVY_PENALTY_PAYABLE as penaltyPayable FROM DAT_KEBS_COMPANY_PROFILE c JOIN LOG_KEBS_STANDARD_LEVY_PAYMENTS p\n" +
                "ON c.ENTRY_NUMBER=p.ENTRY_NUMBER WHERE  p.PENALTY_APPLIED='1' ",
        nativeQuery = true
    )
    fun getPenaltyDetails(): MutableList<PenaltyDetails>

    @Query(
        value = "SELECT p.ID as PenaltyOrderNo FROM DAT_KEBS_COMPANY_PROFILE c JOIN LOG_KEBS_STANDARD_LEVY_PAYMENTS p\n" +
                "ON c.ENTRY_NUMBER=p.ENTRY_NUMBER WHERE  p.PENALTY_APPLIED='1'",
        nativeQuery = true
    )
    fun updatedPenalty(): MutableList<PenaltyDetails>

    @Query(
        value = "UPDATE LOG_KEBS_STANDARD_LEVY_PAYMENTS SET STATUS='3' WHERE ID=:id ",
        nativeQuery = true
    )
    fun updatePenaltyStatus(id: Long): Long



    @Query(
        value = "SELECT COUNT(c.ENTRY_NUMBER)  FROM DAT_KEBS_COMPANY_PROFILE c JOIN LOG_KEBS_STANDARD_LEVY_PAYMENTS p ON c.ENTRY_NUMBER=p.ENTRY_NUMBER WHERE p.PENALTY_APPLIED='1' ",
        nativeQuery = true
    )
    fun findPenaltyCount(): String

}

@Repository
interface UsersEntityRepository : JpaRepository<UsersEntity, Long> {


    @Query(value = "SELECT u.USER_TYPE as name  FROM DAT_KEBS_USERS u WHERE  u.ID = :id", nativeQuery = true)
    fun getSlLoggedById(@Param("id") id: Long?): UserTypeHolder

    @Query(value = "SELECT u.EMAIL as email  FROM DAT_KEBS_USERS u WHERE  u.ID = :id", nativeQuery = true)
    fun getUserEmailById(@Param("id") id: Long?): String

}

@Repository
interface ICompanyProfileCommoditiesManufactureRepository :
    HazelcastRepository<CompanyProfileCommoditiesManufactureEntity, Long> {
    fun findByCompanyProfileId(companyProfileId: Long): List<CompanyProfileCommoditiesManufactureEntity>?
}

@Repository
interface ICompanyProfileContractsUndertakenRepository :
    HazelcastRepository<CompanyProfileContractsUndertakenEntity, Long> {
    fun findByCompanyProfileId(companyProfileId: Long): List<CompanyProfileContractsUndertakenEntity>?
}

@Repository
interface ICompanyProfileDirectorsRepository : HazelcastRepository<CompanyProfileDirectorsEntity, Long> {
    fun findByCompanyProfileId(companyProfileId: Long): List<CompanyProfileDirectorsEntity>?

    @Query(
        value = "SELECT DIRECTOR_NAME as directorName FROM DAT_KEBS_COMPANY_PROFILE_DIRECTORS WHERE COMPANY_PROFILE_ID = :id",
        nativeQuery = true
    )
    fun getCompanyDirectors(@Param("id") id: Long?): List<DirectorListHolder>?

}

@Repository
interface IUserProfilesRepository : HazelcastRepository<UserProfilesEntity, Long> {
    fun findByUserIdAndStatus(userId: UsersEntity, status: Int): UserProfilesEntity?
    fun findByUserId(userId: UsersEntity): UserProfilesEntity?
    fun findFirstByUserIdOrderByIdDesc(userId: UsersEntity): UserProfilesEntity?

    fun findBySectionIdAndStatus(sectionId: SectionsEntity, status: Int): List<UserProfilesEntity>?

    fun findBySectionIdAndDesignationIdAndStatus(
        sectionId: SectionsEntity,
        designationId: DesignationsEntity,
        status: Int
    ): List<UserProfilesEntity>?

    fun findByDesignationIdAndSectionIdAndStatus(
        designationId: DesignationsEntity,
        sectionId: SectionsEntity,
        status: Int
    ): UserProfilesEntity?

    fun findByIdAndDesignationId_IdAndStatus(
        id: Long,
        designationId: Long,
        status: Int
    ): Optional<UserProfilesEntity>

    fun findByIdInAndDesignationId_IdAndStatus(
        id: List<Long>,
        designationId: Long,
        status: Int
    ): List<UserProfilesEntity>

    fun findByRegionIdAndDesignationId(
        regionId: RegionsEntity,
        designationId: DesignationsEntity
    ): List<UserProfilesEntity>?

    fun findBySubSectionL1IdAndStatus(subSectionL1Id: SubSectionsLevel1Entity, status: Int): List<UserProfilesEntity>?

    fun findBySubSectionL2IdAndStatus(subSectionL2Id: SubSectionsLevel2Entity, status: Int): List<UserProfilesEntity>?

    fun findByDesignationIdAndStatus(designationId: DesignationsEntity, status: Int): UserProfilesEntity?

    fun findAllByDesignationIdAndStatus(designationId: DesignationsEntity, status: Int): List<UserProfilesEntity>?


    fun findByRegionIdAndStatusAndDesignationId(
        regionId: RegionsEntity,
        status: Int,
        designationId: DesignationsEntity
    ): UserProfilesEntity?

    fun findByDesignationIdAndRegionIdAndDepartmentIdAndStatus(
        designationId: DesignationsEntity,
        regionId: RegionsEntity,
        departmentId: DepartmentsEntity,
        status: Int
    ): UserProfilesEntity?

    fun findByDesignationIdAndRegionIdAndStatus(
        designationId: DesignationsEntity,
        regionId: RegionsEntity,
        status: Int
    ): List<UserProfilesEntity>?

    fun findByRegionIdAndDepartmentIdAndStatusAndSectionIdAndDesignationId(
        regionId: RegionsEntity,
        departmentId: DepartmentsEntity,
        status: Int,
        sectionId: SectionsEntity,
        designationId: DesignationsEntity
    ): UserProfilesEntity?

    fun findAllByDesignationIdAndRegionIdAndDepartmentIdAndStatus(
        designationId: DesignationsEntity,
        regionId: RegionsEntity,
        departmentId: DepartmentsEntity,
        status: Int
    ): List<UserProfilesEntity>?

    fun findAllByDesignationIdAndRegionIdAndDepartmentIdAndStatusAndSectionId(
        designationId: DesignationsEntity,
        regionId: RegionsEntity,
        departmentId: DepartmentsEntity,
        status: Int,
        sectionId: SectionsEntity
    ): List<UserProfilesEntity>?

    fun findByRegionIdAndDesignationIdAndStatus(
        regionId: RegionsEntity,
        designationId: DesignationsEntity,
        status: Int
    ): List<UserProfilesEntity>?

    fun findByRegionIdAndDesignationIdAndDepartmentIdAndStatus(
        regionId: RegionsEntity,
        designationId: DesignationsEntity,
        departmentId: DepartmentsEntity,
        status: Int
    ): List<UserProfilesEntity>?

    fun findByRegionIdAndDepartmentIdAndDivisionIdAndSectionIdAndStatus(
        regionId: RegionsEntity,
        departmentId: DepartmentsEntity,
        divisionId: DivisionsEntity,
        sectionId: SectionsEntity,
        status: Int
    ): List<UserProfilesEntity>?

    fun findAllByDivisionIdAndStatus(divisionId: DivisionsEntity, status: Int): List<UserProfilesEntity>?
//    @Query("select p from UserProfilesEntity as p where p.userId.id = :usersId")
//    fun findByUserId_IdAndStatus(@Param("usersId") usersId: Long, @Param("status") status: Int): UserProfilesEntity?

//    fun findByUserId_IdAndStatus(userId_id: Long, status: Int): UserProfilesEntity?

    fun findByUserIdAndDesignationIdAndSubRegionId(
        userId: UsersEntity,
        designationId: DesignationsEntity,
        subRegionId: SubRegionsEntity
    ): UserProfilesEntity?
}

@Repository
interface IDesignationsRepository : HazelcastRepository<DesignationsEntity, Long> {
    fun findByStatus(status: Int): List<DesignationsEntity>?

    fun findByDesignationNameAndStatus(designationName: String, status: Int): DesignationsEntity?
    fun findByDirectorateIdAndStatus(directorateId: DirectoratesEntity, status: Int): List<DesignationsEntity>?

    @Query(
        "select * from CFG_KEBS_DESIGNATIONS des left outer join CFG_KEBS_DIRECTORATES dir on des.DIRECTORATE_ID = dir.ID where des.ID = :id",
        nativeQuery = true
    )
    fun findByIdExcludingJoin(@Param("id") id: Long?): DesignationsEntity?
}


//@Repository
//interface IRegionsRepository : HazelcastRepository<RegionsEntity, Long> {
//    fun findByStatus(status: Int): List<RegionsEntity>?
//}

@Repository
interface ITitlesRepository : HazelcastRepository<TitlesEntity, Long> {
    fun findByStatus(status: Int): List<TitlesEntity>?
}

@Repository
interface IStatusValuesRepository : HazelcastRepository<StatusValuesEntity, Long> {
    fun findByStatus(status: Int): List<StatusValuesEntity>?
}

@Repository
interface IUserVerificationTokensRepository : HazelcastRepository<UserVerificationTokensEntity, Long> {
    fun findByUserIdAndStatus(userId: UsersEntity, status: Int): UserVerificationTokensEntity?
    fun findByTokenAndStatus(token: String?, status: Int): UserVerificationTokensEntity?


}

@Repository
interface IUserVerificationTokensRepositoryB : JpaRepository<UserVerificationTokensEntity, Long> {
    @Query(
        "SELECT p.TOKEN as getTc_Title FROM DAT_KEBS_USER_VERIFICATION_TOKEN p WHERE p.USER_ID=:id  AND TRANSACTION_DATE=(SELECT MAX(p.TRANSACTION_DATE)  FROM DAT_KEBS_USER_VERIFICATION_TOKEN p   WHERE p.USER_ID=:id) ORDER BY p.id DESC",
        nativeQuery = true
    )
    fun findTokenByUserId(@Param("id") id: Long?): String

    @Query(
        "SELECT p.*  FROM DAT_KEBS_USER_VERIFICATION_TOKEN p WHERE p.USER_ID=:id  AND TRANSACTION_DATE=(SELECT MAX(p.TRANSACTION_DATE)  FROM DAT_KEBS_USER_VERIFICATION_TOKEN p   WHERE p.USER_ID=:id) ORDER BY p.id DESC",
        nativeQuery = true
    )
    fun findAllByTokenByUserId(@Param("id") id: Long?): UserVerificationTokensEntity?
    fun findByVersion(version: Long): UserVerificationTokensEntity?
    fun findByToken(token: String): UserVerificationTokensEntity?

    @Query(
        "SELECT p.USER_ID as getTc_Title FROM DAT_KEBS_USER_VERIFICATION_TOKEN p WHERE p.VAR_FIELD_1=:id ",
        nativeQuery = true
    )
    fun findAllByVarField1(@Param("id") id: String?): String?
}

@Repository
interface CompanyProfileEditEntityRepository : HazelcastRepository<CompanyProfileEditEntity, Long> {
    fun findAllByManufactureId(manufactureId: Long): CompanyProfileEditEntity

    fun findFirstByManufactureIdOrderByIdDesc(manufactureId: Long): CompanyProfileEditEntity

    @Query(
        "SELECT SL_BPMN_PROCESS_INSTANCE FROM DAT_KEBS_COMPANY_PROFILE_EDIT  WHERE MANUFACTURE_ID=:manufactureId AND STATUS='1' ",
        nativeQuery = true
    )
    fun findStatusByManufactureId(manufactureId: Long): CompanyProfileEditEntity


}

