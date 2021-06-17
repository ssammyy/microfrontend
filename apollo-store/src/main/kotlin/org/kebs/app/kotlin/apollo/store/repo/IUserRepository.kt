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
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository


@Repository
interface IUserRepository : HazelcastRepository<UsersEntity, Long>, JpaSpecificationExecutor<UsersEntity> {
    //    @Query("SELECT u.Id, u.firstName, u.lastName, u.notifs, u.role, u.status from datKebsUsers u where u.status=?1")
    fun findByStatus(status: Int): List<UsersEntity>

    fun findAllByOrderByIdAsc(): List<UsersEntity>

    fun findAllByUserTypes(userType: Long): List<UsersEntity>?

    //    @Query("SELECT u.Id, u.firstName, u.lastName, u.notifs, u.role, u.status from datKebsUsers u where u.notifs=?1")
    fun findByEmail(email: String): UsersEntity?

    //    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    fun findByUserName(userName: String): UsersEntity?

    fun findByUserPinIdNumber(userPinIdNumber: String): UsersEntity?

    fun findFirstByIdAndStatus(id: Long, status: Int): UsersEntity?
    fun findAllById(supervisorId: Long?): MutableIterable<UsersEntity>

    @Query("SELECT DISTINCT u.* FROM CFG_USER_ROLES_ASSIGNMENTS cura, DAT_KEBS_USERS u where cura.ROLE_ID != 1 and cura.USER_ID = u.ID and u.STATUS = :status and cura.STATUS = 1", nativeQuery = true)
    fun findRbacUsersByStatus(@Param("status") status: Int): List<UsersEntity>?
    fun findAllByIdIn(userList: MutableList<Long?>): List<UsersEntity>?

    @Query("SELECT u from UsersEntity u where u.userName = :userName or u.email = :email or u.firstName = :firstName or u.lastName= :lastName")
    fun findUsingSearchValues(
        @Param("userName") userName: String?,
        @Param("email") email: String?,
        @Param("firstName") firstName: String?,
        @Param("lastName") lastName: String?
    ): List<UsersEntity>?

    fun findAllByUserNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(userName: String?, email: String?, firstName: String?, lastName: String?): List<UsersEntity>?

    @Query("select distinct u.*from CFG_ROLES_PRIVILEGES rp, CFG_USER_PRIVILEGES p, CFG_USER_ROLES_ASSIGNMENTS ra, DAT_KEBS_USERS u where rp.ROLES_ID = ra.ROLE_ID and ra.USER_ID = u.ID and rp.PRIVILEGE_ID = :authorityId", nativeQuery = true)
    fun getUsersWithAuthorizationId(@Param("authorityId") authorityId: Long): List<UsersEntity>?


}

@Repository
interface IUserPrivilegesRepository : HazelcastRepository<UserPrivilegesEntity, Long> {
    @Query(
        "SELECT DISTINCT CUP.* FROM CFG_ROLES_PRIVILEGES rp, CFG_USER_ROLES cur , CFG_USER_PRIVILEGES cup  WHERE CUP.ID = rp.PRIVILEGE_ID  AND CUR.ID = rp.ROLES_ID  AND rp.ROLES_ID  IN (:userRoles)  and rp.STATUS = :status",
        nativeQuery = true
    )
    fun findAuthoritiesList(@Param("userRoles") userRoles: List<Long?>, @Param("status") status: Int): List<UserPrivilegesEntity>?

    @Query(value = "SELECT CUP.* FROM CFG_ROLES_PRIVILEGES crp ,CFG_USER_PRIVILEGES cup WHERE crp.PRIVILEGE_ID  = CUP.ID AND crp.ROLES_ID  = :roleId AND crp.STATUS  = :rStatus", nativeQuery = true)
    fun findPrivilegesForRole(@Param("roleId") roleId: Long, @Param("rStatus") rStatus: Int): List<UserPrivilegesEntity>?
    fun findByStatus(status: Int): List<UserPrivilegesEntity>?

    fun findByName(name: String): UserPrivilegesEntity


}

@Repository
interface IUserRolesPrivilegesRepository : HazelcastRepository<RolesPrivilegesEntity, Long> {
    fun findByUserRolesAndStatus(userRoles: UserRolesEntity?, status: Int?): List<RolesPrivilegesEntity>?

    fun findByPrivilege(privilege: UserPrivilegesEntity): List<RolesPrivilegesEntity>?


    @Query(value = "SELECT * FROM CFG_ROLES_PRIVILEGES p WHERE p.ROLES_ID  IN (:userRoles) and STATUS = :status", nativeQuery = true)
    fun findSomeData(@Param("userRoles") userRoles: List<UserRolesEntity?>, @Param("status") status: Int): List<RolesPrivilegesEntity>?
    fun findByStatus(status: Int): List<RolesPrivilegesEntity>?
    fun findByUserRolesAndPrivilegeAndStatus(role: UserRolesEntity, privilege: UserPrivilegesEntity, status: Int): RolesPrivilegesEntity?
}

@Repository
interface IUserRolesRepository : HazelcastRepository<UserRolesEntity, Long> {
    fun findByRoleNameAndStatus(roleName: String, status: Int): UserRolesEntity?
    fun findByDesignationIdAndStatus(designationId: Long, status: Int): UserRolesEntity?
    fun findByStatus(status: Int): List<UserRolesEntity>?

    @Query(value = "SELECT DISTINCT CUR.*  FROM CFG_ROLES_PRIVILEGES p, CFG_USER_ROLES cur WHERE P.ROLES_ID = cur.ID and p.STATUS = :status AND cur.STATUS =:rStatus", nativeQuery = true)
    fun findOnlyDistinctRoles(@Param("status") status: Int, @Param("rStatus") rStatus: Int): List<UserRolesEntity>?

    @Query("SELECT r.* FROM CFG_USER_ROLES_ASSIGNMENTS UR, CFG_USER_ROLES R WHERE UR.ROLE_ID = R.ID AND UR.STATUS = :status AND UR.USER_ID = :userId order by r.ID", nativeQuery = true)
    fun findRbacRolesByUserId(@Param("userId") userId: Long, @Param("status") status: Int): List<UserRolesEntity>?
}

@Repository
interface IUserRoleAssignmentsRepository : HazelcastRepository<UserRoleAssignmentsEntity, Long> {
    fun findByUserIdAndStatus(userId: Long, status: Int): List<UserRoleAssignmentsEntity>?
    fun findByRoleIdAndStatus(roleId: Long, status: Int): List<UserRoleAssignmentsEntity>?
    fun findByRoleId(roleId: Long): List<UserRoleAssignmentsEntity>
    fun findByUserId(userId: Long): UserRoleAssignmentsEntity?


    @Query("SELECT * FROM CFG_USER_ROLES_ASSIGNMENTS cura WHERE CURA.USER_ID = :userId AND STATUS = :status", nativeQuery = true)
    fun findUserRoleAssignments(@Param("userId") userId: Long, @Param("status") status: Int): List<UserRoleAssignmentsEntity>?
    fun findByUserIdAndRoleIdAndStatus(userId: Long, roleId: Long, status: Int): UserRoleAssignmentsEntity?
    fun findByUserIdAndRoleId(userId: Long, roleId: Long): UserRoleAssignmentsEntity?

}

@Repository
interface IUserTypesEntityRepository : HazelcastRepository<UserTypesEntity, Long> {
    fun findByStatus(status: Int): List<UserTypesEntity>
    fun findByTypeNameAndStatus(typeName: String, status: Int): UserTypesEntity?
    fun findByDefaultRole(defaultRole: Long): UserTypesEntity?

}

@Repository
interface IUserRequestsRepository : HazelcastRepository<UserRequestsEntity, Long>, JpaSpecificationExecutor<UsersEntity> {
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

}

@Repository
interface ICompanyProfileCommoditiesManufactureRepository : HazelcastRepository<CompanyProfileCommoditiesManufactureEntity, Long> {
    fun findByCompanyProfileId(companyProfileId: Long): List<CompanyProfileCommoditiesManufactureEntity>?
}

@Repository
interface ICompanyProfileContractsUndertakenRepository : HazelcastRepository<CompanyProfileContractsUndertakenEntity, Long> {
    fun findByCompanyProfileId(companyProfileId: Long): List<CompanyProfileContractsUndertakenEntity>?
}

@Repository
interface ICompanyProfileDirectorsRepository : HazelcastRepository<CompanyProfileDirectorsEntity, Long> {
    fun findByCompanyProfileId(companyProfileId: Long): List<CompanyProfileDirectorsEntity>?
}

@Repository
interface IUserProfilesRepository : HazelcastRepository<UserProfilesEntity, Long> {
    fun findByUserIdAndStatus(userId: UsersEntity, status: Int): UserProfilesEntity?
    fun findByUserId(userId: UsersEntity): UserProfilesEntity?
    fun findFirstByUserIdOrderByIdDesc(userId: UsersEntity): UserProfilesEntity?

    fun findBySectionIdAndStatus(sectionId: SectionsEntity, status: Int): List<UserProfilesEntity>?

    fun findBySectionIdAndDesignationIdAndStatus(sectionId: SectionsEntity, designationId: DesignationsEntity, status: Int): List<UserProfilesEntity>?

    fun findByDesignationIdAndSectionIdAndStatus(designationId: DesignationsEntity, sectionId: SectionsEntity, status: Int): UserProfilesEntity?

    fun findByRegionIdAndDesignationId(regionId: RegionsEntity, designationId: DesignationsEntity): List<UserProfilesEntity>?

    fun findBySubSectionL1IdAndStatus(subSectionL1Id: SubSectionsLevel1Entity, status: Int): List<UserProfilesEntity>?

    fun findBySubSectionL2IdAndStatus(subSectionL2Id: SubSectionsLevel2Entity, status: Int): List<UserProfilesEntity>?

    fun findByDesignationIdAndStatus(designationId: DesignationsEntity, status: Int): UserProfilesEntity?


    fun findByRegionIdAndStatusAndDesignationId(regionId: RegionsEntity, status: Int, designationId: DesignationsEntity): UserProfilesEntity?

    fun findByDesignationIdAndRegionIdAndDepartmentIdAndStatus(designationId: DesignationsEntity, regionId: RegionsEntity, departmentId: DepartmentsEntity, status: Int): UserProfilesEntity?

    fun findAllByDesignationIdAndRegionIdAndDepartmentIdAndStatus(designationId: DesignationsEntity, regionId: RegionsEntity, departmentId: DepartmentsEntity, status: Int): List<UserProfilesEntity>?

    fun findByRegionIdAndDesignationIdAndStatus(regionId: RegionsEntity, designationId: DesignationsEntity, status: Int): List<UserProfilesEntity>?

    fun findByRegionIdAndDesignationIdAndDepartmentIdAndStatus(regionId: RegionsEntity, designationId: DesignationsEntity, departmentId: DepartmentsEntity, status: Int): List<UserProfilesEntity>?

    fun findByRegionIdAndDepartmentIdAndDivisionIdAndSectionIdAndStatus(regionId: RegionsEntity, departmentId: DepartmentsEntity, divisionId: DivisionsEntity, sectionId: SectionsEntity, status: Int): List<UserProfilesEntity>?

    fun findAllByDivisionIdAndStatus(divisionId: DivisionsEntity, status: Int): List<UserProfilesEntity>?
//    @Query("select p from UserProfilesEntity as p where p.userId.id = :usersId")
//    fun findByUserId_IdAndStatus(@Param("usersId") usersId: Long, @Param("status") status: Int): UserProfilesEntity?

//    fun findByUserId_IdAndStatus(userId_id: Long, status: Int): UserProfilesEntity?

    fun findByUserIdAndDesignationIdAndSubRegionId(userId: UsersEntity, designationId: DesignationsEntity, subRegionId: SubRegionsEntity): UserProfilesEntity?
}

@Repository
interface IDesignationsRepository : HazelcastRepository<DesignationsEntity, Long> {
    fun findByStatus(status: Int): List<DesignationsEntity>?

    fun findByDesignationNameAndStatus(designationName: String, status: Int): DesignationsEntity?
    fun findByDirectorateIdAndStatus(directorateId: DirectoratesEntity, status: Int): List<DesignationsEntity>?

    @Query("select * from CFG_KEBS_DESIGNATIONS des left outer join CFG_KEBS_DIRECTORATES dir on des.DIRECTORATE_ID = dir.ID where des.ID = :id", nativeQuery = true)
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
