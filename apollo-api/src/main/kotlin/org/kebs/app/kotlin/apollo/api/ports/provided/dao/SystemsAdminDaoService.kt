package org.kebs.app.kotlin.apollo.api.ports.provided.dao

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.criteria.SearchCriteria
import org.kebs.app.kotlin.apollo.api.ports.provided.spec.UserSpecification
import org.kebs.app.kotlin.apollo.common.dto.*
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.InvalidInputException
import org.kebs.app.kotlin.apollo.common.exceptions.InvalidValueException
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.di.UsersCfsAssignmentsEntity
import org.kebs.app.kotlin.apollo.store.model.qa.ManufacturePlantDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.registration.CompanyProfileDirectorsEntity
import org.kebs.app.kotlin.apollo.store.model.registration.CompanyProfileEntity
import org.kebs.app.kotlin.apollo.store.model.registration.UserRequestsEntity
import org.kebs.app.kotlin.apollo.store.repo.*
import org.kebs.app.kotlin.apollo.store.repo.di.IUsersCfsAssignmentsRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.sql.Date
import java.sql.Timestamp
import java.time.Instant

@Component
class SystemsAdminDaoService(
    private val privilegesRepo: IUserPrivilegesRepository,
    private val usersRepo: IUserRepository,
    private val qaDaoServices: QADaoServices,
    private val userRequestTypesRepo: IUserRequestTypesRepository,
    private val usersCfsRepo: IUsersCfsAssignmentsRepository,
    private val userProfilesRepo: IUserProfilesRepository,
    private val commonDaoServices: CommonDaoServices,
    private val rolesRepo: IUserRolesRepository,
    private val businessLinesRepo: IBusinessLinesRepository,
    private val businessNatureRepo: IBusinessNatureRepository,
    private val companyProfileRepo: ICompanyProfileRepository,
    private val brsLookupManufacturerDataRepo: IBrsLookupManufacturerDataRepository,
    private val brsLookupManufacturerPartnerRepo: IBrsLookupManufacturerPartnersRepository,
    private val manufacturePlantRepository: IManufacturePlantDetailsRepository,
    private val rolePrivilegesRepo: IUserRolesPrivilegesRepository,
    private val titlesRepo: ITitlesRepository,
    private val userTypesRepo: IUserTypesEntityRepository,
    private val userRequestRepo: IUserRequestsRepository,
    private val userRolesRepo: IUserRoleAssignmentsRepository,
    private val registrationDaoServices: RegistrationDaoServices,
    private val designationsRepo: IDesignationsRepository,
    private val departmentsRepo: IDepartmentsRepository,
    private val divisionsRepo: IDivisionsRepository,
    private val directoratesRepo: IDirectoratesRepository,
    private val regionsRepo: IRegionsRepository,
    private val subRegionsRepo: ISubRegionsRepository,
    private val sectionsRepo: ISectionsRepository,
    private val subSectionsL1Repo: ISubSectionsLevel1Repository,
    private val subSectionsL2Repo: ISubSectionsLevel2Repository,
    private val applicationMapProperties: ApplicationMapProperties,
    private val countiesRepo: ICountiesRepository,
    private val townsRepo: ITownsRepository,
    private val serviceRequestsRepository: IServiceRequestsRepository,
    private val companyProfileDirectorsRepo: ICompanyProfileDirectorsRepository,
) {

    final val appId: Int = applicationMapProperties.mapUserRegistration

    fun listTitles(status: Int): List<TitlesEntityDto>? =
        titlesRepo.findAll().sortedBy { it.id }.map { TitlesEntityDto(it.id, it.title, it.remarks, it.status == 1) }

    fun listUserTypes(status: Int): List<UserTypesEntityDto>? = userTypesRepo.findAll().sortedBy { it.id }
        .map { UserTypesEntityDto(it.id, it.typeName, it.descriptions, it.status == 1, it.defaultRole) }


    fun loggedInUserDetails(): UsersEntity {
        SecurityContextHolder.getContext().authentication?.name
            ?.let { username ->
                usersRepo.findByUserName(username)
                    ?.let { loggedInUser ->
                        return loggedInUser
                    }
                    ?: throw ExpectedDataNotFound("No userName with the following userName=$username, Exist in the users table")
            }
            ?: throw ExpectedDataNotFound("No user has logged in")
    }

    fun checkIfUserLoggedIn(userEmail: String): String? {
        SecurityContextHolder.getContext().authentication?.name
            ?.let { username ->
                return when (val loggedInUser = usersRepo.findByUserName(username)) {
                    null -> {
                        userEmail
                    }
                    else -> {
                        loggedInUser.userName
                    }
                }

            }
            ?: throw ExpectedDataNotFound("No user has logged in")
    }


    fun listPrivileges(page: Int, records: Int): Page<UserPrivilegesEntity>? =
        PageRequest.of(page, records)
            .let { pages ->
                privilegesRepo.findAll(pages)
            }

    fun addOrEditPrivilege(privilegesEntity: UserPrivilegesEntity): UserPrivilegesEntity? {
        return privilegesRepo.save(privilegesEntity)
    }

    fun addOrEditUser(usersEntity: UsersEntity): UsersEntity? {
        return usersRepo.save(usersEntity)
    }

    fun getAuthority(id: String): UserPrivilegesEntity? {
        return privilegesRepo.findByIdOrNull(id.toLongOrNull())

    }

    fun getUser(id: String): UsersEntity? {
        return usersRepo.findByIdOrNull(id.toLongOrNull())

    }

    fun getUserDetails(id: Long): UserDetailsDto {
        val user = commonDaoServices.findUserByID(id)
        val employeeProfile = userProfilesRepo.findByUserId(user)
        val companyProfile = user.id?.let { userId ->
            companyProfileRepo.findByUserId(userId)?.let { returnCompanyProfileEntityDto(it) }
        }
        val employeeProfileDto = employeeProfile?.let { getEmployeeProfileDto(it) }
        return UserDetailsDto(
            user.id,
            user.firstName,
            user.lastName,
            user.userName,
            user.email,
            user.userPinIdNumber,
            user.personalContactNumber,
            user.typeOfUser,
            user.userRegNo,
            user.enabled == 1,
            user.accountExpired == 1,
            user.accountLocked == 1,
            user.credentialsExpired == 1,
            user.status == 1,
            user.registrationDate,
            user.title?.let { titlesRepo.findByIdOrNull(user.title)?.title },
            employeeProfileDto,
            companyProfile
        )

    }

    private fun getEmployeeProfileDto(employeeProfile: UserProfilesEntity): EmployeeProfileDetailsDto {
        return EmployeeProfileDetailsDto(
            employeeProfile.directorateId?.id?.let { directoratesRepo.findByIdOrNull(it)?.directorate },
            employeeProfile.departmentId?.id?.let { departmentsRepo.findByIdOrNull(it)?.department },
            employeeProfile.divisionId?.id?.let { divisionsRepo.findByIdOrNull(it)?.division },
            employeeProfile.sectionId?.id?.let { sectionsRepo.findByIdOrNull(it)?.section },
            employeeProfile.subSectionL1Id?.id?.let { subSectionsL1Repo.findByIdOrNull(it)?.subSection },
            employeeProfile.subSectionL2Id?.id?.let { subSectionsL2Repo.findByIdOrNull(it)?.subSection },
            employeeProfile.designationId?.id?.let { designationsRepo.findByIdOrNull(it)?.designationName },
            employeeProfile.id,
            employeeProfile.regionId?.id?.let { regionsRepo.findByIdOrNull(it)?.region },
            employeeProfile.countyID?.id?.let { countiesRepo.findByIdOrNull(it)?.county },
            employeeProfile.townID?.id?.let { townsRepo.findByIdOrNull(it)?.town },
            employeeProfile.status == 1

        )
    }

    fun listUsers(page: Int, records: Int): List<UserEntityDto>? {
        val userList = mutableListOf<UserEntityDto>()

        PageRequest.of(page, records)
            .let {
                usersRepo.findAll(it)
                    .map { u ->
                        userList.add(
                            UserEntityDto(
                                u.id,
                                u.firstName,
                                u.lastName,
                                u.userName,
                                u.userPinIdNumber,
                                u.personalContactNumber,
                                u.typeOfUser,
                                u.email,
                                u.userRegNo,
                                u.enabled == 1,
                                u.accountExpired == 1,
                                u.accountLocked == 1,
                                u.credentialsExpired == 1,
                                u.status == 1,
                                u.registrationDate,
                                u.userTypes,
                                u.title,
                            )
                        )
                    }
            }

//        return usersRepo.findAll().toList().sortedBy { it.id }
        return userList.sortedByDescending { it.id }
    }

    fun listUsersRequest(page: Int, records: Int): List<UserRequestListEntityDto>? {
        val userRequestList = mutableListOf<UserRequestListEntityDto>()
        PageRequest.of(page, records)
            .let {
                userRequestRepo.findAll(it).map { u ->
                    userRequestList.add(
                        UserRequestListEntityDto(
                            u.id,
                            u.requestId?.let { userRequestTypesRepo.findByIdOrNull(u.requestId)?.userRequest },
                            u.userId?.let { usersRepo.findByIdOrNull(u.userId)?.userName },
                            u.userId,
                            u.userRoleAssigned?.let { privilegesRepo.findByIdOrNull(u.userRoleAssigned)?.name },
                            u.requestStatus == 1,
                            u.description,
                            u.status == 1,
                        )
                    )
                }
            }

        return userRequestList.sortedByDescending { it.id }
    }

    fun listAllRoles(): List<RolesEntityDto>? = rolesRepo.findAll().sortedBy { it.id }
        .map { RolesEntityDto(it.id, it.roleName, it.descriptions, it.status == 1) }

    fun listAllRolesByStatus(status: Int): List<RolesEntityDto>? = rolesRepo.findByStatus(status)?.sortedBy { it.id }
        ?.map { RolesEntityDto(it.id, it.roleName, it.descriptions, it.status == 1) }

    fun listAllAuthorities(): List<AuthoritiesEntityDto>? = privilegesRepo.findAll().sortedBy { it.id }
        .map { AuthoritiesEntityDto(it.id, it.name, it.descriptions, it.status == 1) }

    fun listAllAuthoritiesByStatus(status: Int): List<AuthoritiesEntityDto>? =
        privilegesRepo.findByStatus(status)?.sortedBy { it.id }
            ?.map { AuthoritiesEntityDto(it.id, it.name, it.descriptions, it.status == 1) }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updateUserDetails(dto: UserEntityDto): UserEntityDto? {

        when {
            dto.id ?: -1L < 1L -> {
                var user = UsersEntity()
                user.title = dto.title
                user.firstName = dto.firstName
                user.lastName = dto.lastName
                user.userName = dto.userName
                user.userPinIdNumber = dto.userPinIdNumber
                user.personalContactNumber = dto.personalContactNumber
                user.typeOfUser = dto.typeOfUser
                user.email = dto.email
                user.enabled = when (dto.enabled) {
                    true -> 1
                    else -> 0
                }

                user.accountExpired = when (dto.accountExpired) {
                    true -> 1
                    else -> 0
                }
                user.accountLocked = when (dto.accountLocked) {
                    true -> 1
                    else -> 0
                }
                user.credentialsExpired = when (dto.credentialsExpired) {
                    true -> 1
                    else -> 0
                }


                user.status = when (dto.status) {
                    true -> 1
                    else -> 0
                }
                //Todo: Removal of user Types
//                user.userTypes = dto.userType
                user.userRegNo = dto.userRegNo
                user.registrationDate = commonDaoServices.getCurrentDate()
                user.createdBy = user.userName?.let { checkIfUserLoggedIn(it) }
                user.createdOn = Timestamp.from(Instant.now())
                user.registrationDate = Date(java.util.Date().time)


                user = usersRepo.save(user)
//
                val userRole = userRolesRepo.save(
                    registrationDaoServices.userRoleAssignment(
                        user,
                        1,
                        applicationMapProperties.mapUserRegistrationUserRoleID
                    )
                )

                //Todo: ask Ken How we will go about this function and adding of BPM function for sending mail
                userRegistrationMailSending(
                    user,
                    userRole,
                    applicationMapProperties.mapUserRegistrationActivationNotification
                )

                dto.directorate?.let { createUserProfilesEntity(dto, user) }


                return dto

            }
            else -> {
                usersRepo.findByIdOrNull(dto.id)
                    ?.let { user ->
                        user.title = dto.title
                        user.firstName = dto.firstName
                        user.lastName = dto.lastName
                        user.userName = dto.userName
                        user.email = dto.email
                        user.enabled = when (dto.enabled) {
                            true -> 1
                            else -> 0
                        }

                        user.accountExpired = when (dto.accountExpired) {
                            true -> 1
                            else -> 0
                        }
                        user.accountLocked = when (dto.accountLocked) {
                            true -> 1
                            else -> 0
                        }
                        user.credentialsExpired = when (dto.credentialsExpired) {
                            true -> 1
                            else -> 0
                        }
                        user.status = when (dto.status) {
                            true -> 1
                            else -> 0
                        }
                        user.userTypes = dto.userType

                        user.modifiedBy = loggedInUserDetails().userName
                        user.modifiedOn = Timestamp.from(Instant.now())
                        usersRepo.save(user)
                        //Todo: Ask Ken what this is all about
                        userProfilesRepo.findByIdOrNull(dto.profileId)
                            ?.let { profile ->
                                profile.lastModifiedOn = Timestamp.from(Instant.now())
                                profile.lastModifiedBy = loggedInUserDetails().userName
                                dto.region?.let { profile.regionId = regionsRepo.findByIdOrNull(dto.region) }
                                dto.county?.let { profile.countyID = countiesRepo.findByIdOrNull(dto.county) }
                                dto.town?.let { profile.townID = townsRepo.findByIdOrNull(dto.town) }
                                dto.subRegion?.let {
                                    profile.subRegionId = subRegionsRepo.findByIdOrNull(dto.subRegion)
                                }
                                dto.designation?.let {
                                    profile.designationId = designationsRepo.findByIdOrNull(dto.designation)
                                }
                                dto.directorate?.let {
                                    profile.directorateId = directoratesRepo.findByIdOrNull(dto.directorate)
                                }
                                dto.department?.let {
                                    profile.departmentId = departmentsRepo.findByIdOrNull(dto.department)
                                }
                                dto.division?.let {
                                    profile.divisionId = divisionsRepo.findByIdOrNull(dto.division)
                                }
                                dto.section?.let {
                                    profile.sectionId = sectionsRepo.findByIdOrNull(dto.section)
                                }
                                dto.l1SubSubSection?.let {
                                    profile.subSectionL1Id =
                                        subSectionsL1Repo.findByIdOrNull(dto.l1SubSubSection)
                                }
                                dto.l2SubSubSection?.let {
                                    profile.subSectionL2Id =
                                        subSectionsL2Repo.findByIdOrNull(dto.l2SubSubSection)
                                }
                                userProfilesRepo.save(profile)
                            }
                            ?: run {
                                createUserProfilesEntity(dto, user)

                            }

                        return dto
                    }
                    ?: throw NullValueNotAllowedException("Record not found, check and try again")

            }
        }
    }


    fun updateUserCompanyDetails(dto: UserCompanyEntityDto): UserCompanyDto {
        dto.userId?.let {
            companyProfileRepo.findByUserId(it)
                ?.let { companyProfile ->
                    with(companyProfile) {
                        name = dto.name
                        kraPin = dto.kraPin
                        userId = dto.userId
                        registrationNumber = dto.registrationNumber
                        directorIdNumber = dto.directorIdNumber
                        postalAddress = dto.postalAddress
                        physicalAddress = dto.physicalAddress
                        plotNumber = dto.plotNumber
                        companyEmail = dto.companyEmail
                        companyTelephone = dto.companyTelephone
                        yearlyTurnover = dto.yearlyTurnover
                        businessLines = dto.businessLines
                        businessNatures = dto.businessNatures
                        buildingName = dto.buildingName
                        streetName = dto.streetName
                        region = dto.region
                        county = dto.county
                        town = dto.town
                        factoryVisitDate = dto.factoryVisitDate
                        factoryVisitStatus = dto.factoryVisitStatus
                        manufactureStatus = dto.manufactureStatus
                        firmCategory = qaDaoServices.manufactureType(
                            yearlyTurnover ?: throw Exception("INVALID YEARLY TURN OVER")
                        ).id
                        status = 1
                        modifiedBy = loggedInUserDetails().userName
                        modifiedOn = Timestamp.from(Instant.now())
                    }
                    companyProfileRepo.save(companyProfile)
                    return returnCompanyProfileEntityDto(companyProfile)
                }
        }
            ?: kotlin.run {
                var companyProfile = CompanyProfileEntity()
                with(companyProfile) {
                    name = dto.name
                    kraPin = dto.kraPin
                    userId = dto.userId
                    registrationNumber = dto.registrationNumber
                    directorIdNumber = dto.directorIdNumber
                    postalAddress = dto.postalAddress
                    physicalAddress = dto.physicalAddress
                    plotNumber = dto.plotNumber
                    companyEmail = dto.companyEmail
                    companyTelephone = dto.companyTelephone
                    yearlyTurnover = dto.yearlyTurnover
                    businessLines = dto.businessLines
                    businessNatures = dto.businessNatures
                    buildingName = dto.buildingName
                    streetName = dto.streetName
                    region = dto.region
                    county = dto.county
                    town = dto.town
                    firmCategory =
                        qaDaoServices.manufactureType(yearlyTurnover ?: throw Exception("INVALID YEARLY TURN OVER")).id
                    factoryVisitDate = dto.factoryVisitDate
                    factoryVisitStatus = dto.factoryVisitStatus
                    manufactureStatus = dto.manufactureStatus
                    userId = dto.userId
                    status = 0
                    createdBy = loggedInUserDetails().userName
                    createdOn = java.sql.Timestamp.from(java.time.Instant.now())
                }

                companyProfile = companyProfileRepo.save(companyProfile)
                dto.profileType?.let { it1 -> companyProfile.userId?.let { it2 -> updateUserProfile(it1, it2, 1) } }
                return returnCompanyProfileEntityDto(companyProfile)
            }
    }

    fun userRegistrationMailSending(
        user: UsersEntity,
        userRole: UserRoleAssignmentsEntity?,
        emailUuid: String
    ): ServiceRequestsEntity {


        val map = commonDaoServices.serviceMapDetails(appId)

        var sr = commonDaoServices.createServiceRequest(map)
        try {
            val payload = "$user $userRole"
            sr = commonDaoServices.mapServiceRequestForSuccess(map, payload, user)
            val emailEntity = commonDaoServices.userRegisteredSuccessfulEmailCompose(user, sr, map, null)
            commonDaoServices.sendEmailAfterCompose(user, emailUuid, emailEntity, appId, payload)

            sr.payload = "User[id= ${user.id}]"
            sr.names = "${user.firstName} ${user.lastName}"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = map.successStatus
            sr = serviceRequestsRepository.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepository.save(sr)

        }

        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return sr
    }


    private fun createUserProfilesEntity(dto: UserEntityDto, user: UsersEntity): UserProfilesEntity {
        var profile = UserProfilesEntity()
        profile.userId = user
        profile.status = 1
        profile.createdOn = Timestamp.from(Instant.now())
        profile.createdBy = loggedInUserDetails().userName
        dto.region?.let { profile.regionId = regionsRepo.findByIdOrNull(dto.region) }
        dto.county?.let { profile.countyID = countiesRepo.findByIdOrNull(dto.county) }
        dto.town?.let { profile.townID = townsRepo.findByIdOrNull(dto.town) }
        dto.subRegion?.let { profile.subRegionId = subRegionsRepo.findByIdOrNull(dto.subRegion) }
        dto.designation?.let { profile.designationId = designationsRepo.findByIdOrNull(dto.designation) }
        dto.directorate?.let { profile.directorateId = directoratesRepo.findByIdOrNull(dto.directorate) }
        dto.department?.let { profile.departmentId = departmentsRepo.findByIdOrNull(dto.department) }
        dto.division?.let { profile.divisionId = divisionsRepo.findByIdOrNull(dto.division) }
        dto.section?.let { profile.sectionId = sectionsRepo.findByIdOrNull(dto.section) }
        dto.l1SubSubSection?.let { profile.subSectionL1Id = subSectionsL1Repo.findByIdOrNull(dto.l1SubSubSection) }
        dto.l2SubSubSection?.let { profile.subSectionL2Id = subSectionsL2Repo.findByIdOrNull(dto.l2SubSubSection) }

        profile = userProfilesRepo.save(profile)
        //Having EmployeeRole
        userRolesRepo.save(
            registrationDaoServices.userRoleAssignment(
                user,
                1,
                applicationMapProperties.mapUserRegistrationEmployeeRoleID
            )
        )
        return profile
    }

    fun updateRole(role: RolesEntityDto): RolesEntityDto? {
        when {
            role.id ?: 0L < 1L -> {
                val r = UserRolesEntity()
                r.roleName = role.roleName
                r.descriptions = role.descriptions
                if (role.status == true) r.status = 1 else r.status = 0
                r.createdBy = loggedInUserDetails().userName
                r.createdOn = Timestamp.from(Instant.now())
                rolesRepo.save(r)
                return role

            }
            else -> {
                rolesRepo.findByIdOrNull(role.id)
                    ?.let { r ->
                        r.roleName = role.roleName
                        if (role.status == true) r.status = 1 else r.status = 0
                        r.descriptions = role.descriptions
                        r.modifiedBy = loggedInUserDetails().userName
                        r.modifiedOn = Timestamp.from(Instant.now())
                        rolesRepo.save(r)
                        return role

                    }
                    ?: throw NullValueNotAllowedException("Record not found, check and try again")

            }
        }


    }

    fun updateAuthorities(privilege: AuthoritiesEntityDto): AuthoritiesEntityDto? {
        when {
            (privilege.id ?: -1L) < 1L -> {
                val r = UserPrivilegesEntity()
                r.name = privilege.name
                r.descriptions = privilege.descriptions
                if (privilege.status == true) r.status = 1 else r.status = 0
                r.createdBy = loggedInUserDetails().userName
                r.createdOn = Timestamp.from(Instant.now())
                privilegesRepo.save(r)
                return privilege

            }
            else -> {
                privilegesRepo.findByIdOrNull(privilege.id)
                    ?.let { r ->
                        r.name = privilege.name
                        r.descriptions = privilege.descriptions
                        if (privilege.status == true) r.status = 1 else r.status = 0
                        r.modifiedBy = loggedInUserDetails().userName
                        r.modifiedOn = Timestamp.from(Instant.now())
                        privilegesRepo.save(r)
                        return privilege
                    }
                    ?: throw NullValueNotAllowedException("Record not found, check and try again")

            }
        }


    }

    fun updateTitle(entity: TitlesEntityDto): TitlesEntityDto? {
        when {
            entity.id ?: 0L < 1L -> {
                val r = TitlesEntity()
                r.title = entity.title
                r.remarks = entity.remarks
                if (entity.status == true) r.status = 1 else r.status = 0
                r.createdBy = loggedInUserDetails().userName
                r.createdOn = Timestamp.from(Instant.now())
                titlesRepo.save(r)
                return entity

            }
            else -> {
                titlesRepo.findByIdOrNull(entity.id)
                    ?.let { r ->
                        r.title = entity.title
                        r.remarks = entity.remarks
                        if (entity.status == true) r.status = 1 else r.status = 0

                        r.lastModifiedBy = loggedInUserDetails().userName
                        r.lastModifiedOn = Timestamp.from(Instant.now())
                        titlesRepo.save(r)
                        return entity
                    }
                    ?: throw NullValueNotAllowedException("Record not found, check and try again")

            }
        }

    }


    fun updateUserType(entity: UserTypesEntityDto): UserTypesEntityDto? {
        when {
            entity.id ?: 0L < 1L -> {
                val r = UserTypesEntity()
                r.typeName = entity.typeName
                if (entity.status == true) r.status = 1 else r.status = 0
                r.defaultRole = entity.defaultRoleId
                r.descriptions = entity.descriptions
                r.createdBy = loggedInUserDetails().userName
                r.createdOn = Timestamp.from(Instant.now())
                userTypesRepo.save(r)
                return entity

            }
            else -> {
                userTypesRepo.findByIdOrNull(entity.id)
                    ?.let { r ->
                        r.typeName = entity.typeName
                        if (entity.status == true) r.status = 1 else r.status = 0
                        r.defaultRole = entity.defaultRoleId
                        r.descriptions = entity.descriptions

                        r.modifiedBy = loggedInUserDetails().userName
                        r.modifiedOn = Timestamp.from(Instant.now())
                        userTypesRepo.save(r)
                        return entity
                    }
                    ?: throw NullValueNotAllowedException("Record not found, check and try again")

            }
        }

    }

    fun listRbacRolesByStatus(status: Int): List<UserRolesEntity>? = rolesRepo.findByStatus(status)?.sortedBy { it.id }
    fun listAuthoritiesByRoleAndStatus(roleId: Long, status: Int): List<UserPrivilegesEntity>? =
        privilegesRepo.findPrivilegesForRole(roleId, status)?.sortedBy { it.name }


    fun revokeAuthorizationFromRole(roleId: Long, privilegeId: Long, status: Int): RolesPrivilegesEntity? {
        return rolesRepo.findByIdOrNull(roleId)
            ?.let { role ->
                privilegesRepo.findByIdOrNull(privilegeId)
                    ?.let { privilege ->
                        rolePrivilegesRepo.findByUserRolesAndPrivilegeAndStatus(role, privilege, status)
                            ?.let { rolePrivilege ->
                                rolePrivilege.status = 0
                                rolePrivilege.modifiedBy = loggedInUserDetails().userName
                                rolePrivilege.modifiedOn = Timestamp.from(Instant.now())
                                rolePrivilege.varField1 = "${rolePrivilege.status}"
                                rolePrivilegesRepo.save(rolePrivilege)
                            }
                            ?: throw InvalidInputException("Revoking authorization that does not exist")


                    }
                    ?: throw InvalidValueException("Record with id=$privilegeId not found, check and try again")

            }
            ?: throw InvalidValueException("Record with id=$roleId not found, check and try again")
    }


    fun assignAuthorizationFromRole(roleId: Long, privilegeId: Long, status: Int): RolesPrivilegesEntity? {
        return rolesRepo.findByIdOrNull(roleId)
            ?.let { role ->
                privilegesRepo.findByIdOrNull(privilegeId)
                    ?.let { privilege ->
                        rolePrivilegesRepo.findByUserRolesAndPrivilegeAndStatus(role, privilege, status)
                            ?.let { rolePrivilege ->
                                rolePrivilege.status = 1
                                rolePrivilege.modifiedBy = loggedInUserDetails().userName
                                rolePrivilege.modifiedOn = Timestamp.from(Instant.now())
                                rolePrivilege.varField1 = "${rolePrivilege.status}"
                                rolePrivilegesRepo.save(rolePrivilege)
                            }
                            ?: kotlin.run {
                                val rolePrivilege = RolesPrivilegesEntity()
                                rolePrivilege.userRoles = role
                                rolePrivilege.privilege = privilege
                                rolePrivilege.status = 1
                                rolePrivilege.createdBy = loggedInUserDetails().userName
                                rolePrivilege.createdOn = Timestamp.from(Instant.now())
                                rolePrivilegesRepo.save(rolePrivilege)

                            }


                    }
                    ?: throw InvalidValueException("Record with id=$privilegeId not found, check and try again")

            }
            ?: throw InvalidValueException("Record with id=$roleId not found, check and try again")
    }

    fun assignRoleToUser(userId: Long, roleId: Long, status: Int): UserRoleAssignmentsEntity? {
        return usersRepo.findByIdOrNull(userId)
            ?.let { user ->
                rolesRepo.findByIdOrNull(roleId)
                    ?.let { role ->
                        /* todo: Discuss with KEN on how the function works */
                        userRolesRepo.findByUserIdAndRoleId(user.id ?: -1L, role.id)
                            ?.let { usersRole ->
                                usersRole.status = 1
                                usersRole.lastModifiedBy = loggedInUserDetails().userName
                                usersRole.lastModifiedOn = Timestamp.from(Instant.now())
                                usersRole.varField1 = "${usersRole.status}"
                                userRolesRepo.save(usersRole)


                            }
                            ?: kotlin.run {
                                val usersRole = UserRoleAssignmentsEntity()
                                usersRole.userId = user.id
                                usersRole.roleId = role.id
                                usersRole.status = 1
                                usersRole.createdBy = loggedInUserDetails().userName
                                usersRole.createdOn = Timestamp.from(Instant.now())
                                userRolesRepo.save(usersRole)

                            }


                    }
                    ?: throw InvalidValueException("Record with id=$roleId not found, check and try again")

            }
            ?: throw InvalidValueException("Record with id=$userId not found, check and try again")
    }

    fun revokeRoleFromUser(userId: Long, roleId: Long, status: Int): UserRoleAssignmentsEntity? {
        return usersRepo.findByIdOrNull(userId)
            ?.let { user ->
                rolesRepo.findByIdOrNull(roleId)
                    ?.let { role ->
                        /* todo: Discuss with KEN on how the function works */
                        userRolesRepo.findByUserIdAndRoleIdAndStatus(user.id ?: -1L, role.id, status)
                            ?.let { userRole ->
                                userRole.status = 0
                                userRole.lastModifiedBy = loggedInUserDetails().userName
                                userRole.lastModifiedOn = Timestamp.from(Instant.now())
                                userRole.varField1 = "${userRole.status}"
                                userRolesRepo.save(userRole)
                            }
                            ?: throw InvalidInputException("Revoking Role that does not exist")


                    }
                    ?: throw InvalidValueException("Record with id=$roleId not found, check and try again")

            }
            ?: throw InvalidValueException("Record with id=$userId not found, check and try again")
    }

    fun listRbacUsersByStatus(status: Int): List<UserEntityDto> {
        val userList = mutableListOf<UserEntityDto>()
        usersRepo.findRbacUsersByStatus(status)
            ?.map { u ->
//                val profile = userProfilesRepo.findBfindByFirstUserIdOrderByIdDesc(u)
                userList.add(
                    UserEntityDto(
                        u.id,
                        u.firstName,
                        u.lastName,
                        u.userName,
                        u.userPinIdNumber,
                        u.personalContactNumber,
                        u.typeOfUser,
                        u.email,
                        u.userRegNo,
                        u.enabled == 1,
                        u.accountExpired == 1,
                        u.accountLocked == 1,
                        u.credentialsExpired == 1,
                        u.status == 1,
                        u.registrationDate,
                        u.userTypes,
                        u.title
                    )
                )
            }

        return userList
    }


    fun assignCFSToUser(userProfileId: Long, cfsId: Long): UsersCfsAssignmentsEntity? {
        return userProfilesRepo.findByIdOrNull(userProfileId)
            ?.let { userProfile ->
                subSectionsL2Repo.findByIdOrNull(cfsId)
                    ?.let { cfs ->
                        /* todo: Discuss with KEN on how the function works */
                        usersCfsRepo.findByUserProfileIdAndCfsId(userProfile.id ?: -1L, cfs.id)
                            ?.let { usersCfs ->
                                usersCfs.status = 1
                                usersCfs.modifiedBy = loggedInUserDetails().userName
                                usersCfs.modifiedOn = Timestamp.from(Instant.now())
                                usersCfs.varField1 = "${usersCfs.status}"
                                usersCfsRepo.save(usersCfs)
                            }
                            ?: kotlin.run {
                                val usersCfs = UsersCfsAssignmentsEntity()
                                usersCfs.userProfileId = userProfile.id
                                usersCfs.cfsId = cfs.id
                                usersCfs.status = 1
                                usersCfs.createdBy = loggedInUserDetails().userName
                                usersCfs.createdOn = Timestamp.from(Instant.now())
                                usersCfsRepo.save(usersCfs)

                            }


                    }
                    ?: throw InvalidValueException("Record with id=$cfsId not found, check and try again")

            }
            ?: throw InvalidValueException("Record with id=$userProfileId not found, check and try again")
    }

    fun returnUserRequestEntityDto(userRequest: UserRequestsEntity): UserRequestEntityDto {
        return UserRequestEntityDto(
            userRequest.id,
            userRequest.requestId,
            userRequest.userId,
            userRequest.userRoleAssigned,
            userRequest.requestStatus == 1,
            userRequest.description,
            userRequest.status == 1
        )
    }

    fun returnCompanyProfileEntityDto(cp: CompanyProfileEntity): UserCompanyDto {

        return UserCompanyDto(
            cp.id,
            cp.name,
            cp.kraPin,
            cp.userId,
            cp.registrationNumber,
            cp.postalAddress,
            cp.physicalAddress,
            cp.plotNumber,
            cp.companyEmail,
            cp.companyTelephone,
            cp.yearlyTurnover,
            cp.businessLines?.let { businessLinesRepo.findByIdOrNull(cp.businessLines)?.name },
            cp.businessNatures?.let { businessNatureRepo.findByIdOrNull(cp.businessNatures)?.name },
            cp.buildingName,
            cp.streetName,
            cp.region?.let { regionsRepo.findByIdOrNull(cp.region)?.region },
            cp.county?.let { countiesRepo.findByIdOrNull(cp.county)?.county },
            cp.town?.let { townsRepo.findByIdOrNull(cp.town)?.town },
            cp.factoryVisitDate,
            cp.factoryVisitStatus
        )
    }

    fun userRequest(userRequestDto: UserRequestEntityDto): UserRequestEntityDto? {
        return usersRepo.findByIdOrNull(userRequestDto.userId)
            ?.let { userDetails ->
                userRequestTypesRepo.findByIdOrNull(userRequestDto.requestId)
                    ?.let { requestDetails ->
                        requestDetails.id?.let {
                            userRequestRepo.findByUserIdAndRequestId(userDetails.id ?: -1L, it)
                                ?.let { usersRequestDetails ->
                                    with(usersRequestDetails) {
                                        requestStatus = 1
                                        userRoleAssigned = userRequestDto.userRoleAssigned
                                        status = 1
                                        modifiedBy = loggedInUserDetails().userName
                                        modifiedOn = java.sql.Timestamp.from(java.time.Instant.now())
                                        varField1 = "$status"
                                    }
                                    val request = userRequestRepo.save(usersRequestDetails)
                                    request.requestId?.let { it1 ->
                                        request.userId?.let { it2 ->
                                            updateUserProfile(
                                                it1,
                                                it2,
                                                0
                                            )
                                        }
                                    }
                                    returnUserRequestEntityDto(request)
                                }
                        }
                            ?: kotlin.run {
                                var userRequest = UserRequestsEntity()
                                with(userRequest) {
                                    userId = userDetails.id
                                    requestId = requestDetails.id
                                    userRoleAssigned = null
                                    requestStatus = 0
                                    status = 1
                                    description = userRequestDto.description
                                    createdBy = loggedInUserDetails().userName
                                    createdOn = Timestamp.from(Instant.now())
                                }
                                userRequest = userRequestRepo.save(userRequest)
                                userRequest.requestId?.let { it1 ->
                                    userRequest.userId?.let { it2 ->
                                        updateUserProfile(
                                            it1,
                                            it2,
                                            0
                                        )
                                    }
                                }
                                returnUserRequestEntityDto(userRequest)

                            }


                    }
                    ?: throw InvalidValueException("Record with id=${userRequestDto.requestId} not found, check and try again")

            }
            ?: throw InvalidValueException("Record with id=${userRequestDto.userId} not found, check and try again")
    }

    fun updateUserProfile(requestID: Long, userId: Long, status: Int) {
        usersRepo.findByIdOrNull(userId)?.let { u ->
            when (requestID) {
                applicationMapProperties.mapUserRequestManufacture -> {
                    u.manufactureProfile = status
                }
                applicationMapProperties.mapUserRequestImporter -> {
                    u.importerProfile = status
                }
            }
            usersRepo.save(u)
        }

    }

    fun revokeCfsFromUser(userProfileId: Long, cfsId: Long, status: Int): UsersCfsAssignmentsEntity? {
        return userProfilesRepo.findByIdOrNull(userProfileId)
            ?.let { userProfile ->
                subSectionsL2Repo.findByIdOrNull(cfsId)
                    ?.let { cfs ->
                        /* todo: Discuss with KEN on how the function works */
                        usersCfsRepo.findByUserProfileIdAndCfsIdAndStatus(userProfile.id ?: -1L, cfs.id, status)
                            ?.let { usersCfs ->
                                usersCfs.status = 1
                                usersCfs.modifiedBy = loggedInUserDetails().userName
                                usersCfs.modifiedOn = Timestamp.from(Instant.now())
                                usersCfs.varField1 = "${usersCfs.status}"
                                usersCfsRepo.save(usersCfs)
                            }
                            ?: throw InvalidInputException("Revoking Role that does not exist")


                    }
                    ?: throw InvalidValueException("Record with id=$cfsId not found, check and try again")

            }
            ?: throw InvalidValueException("Record with id=$userProfileId not found, check and try again")
    }

    fun listRbacRolesByUsersIdAndByStatus(userId: Long, status: Int): List<UserRolesEntity>? =
        rolesRepo.findRbacRolesByUserId(userId, status)


    fun userSearchResultListing(search: UserSearchValues): List<UserEntityDto>? {
        val userList = mutableListOf<UserEntityDto>()
        val userNameSpec: UserSpecification?
        var emailSpec: UserSpecification? = null
        var firstNameSpec: UserSpecification? = null
        var lastNameSpec: UserSpecification? = null


        userNameSpec = UserSpecification(SearchCriteria("userName", ":", search.userName))
        search.email?.let {
            emailSpec = UserSpecification(SearchCriteria("email", ":", search.email))
        }
        search.firstName?.let {

            firstNameSpec = UserSpecification(SearchCriteria("firstName", ":", search.firstName))
        }
        search.lastName?.let {


            lastNameSpec = UserSpecification(SearchCriteria("lastName", ":", search.lastName))
        }
        usersRepo.findAll(userNameSpec.or(firstNameSpec).or(lastNameSpec).or(emailSpec))
            .map { u ->
                val profile = userProfilesRepo.findFirstByUserIdOrderByIdDesc(u)
                userList.add(
                    UserEntityDto(
                        u.id,
                        u.firstName,
                        u.lastName,
                        u.userName,
                        u.userPinIdNumber,
                        u.personalContactNumber,
                        u.typeOfUser,
                        u.email,
                        u.userRegNo,
                        u.enabled == 1,
                        u.accountExpired == 1,
                        u.accountLocked == 1,
                        u.credentialsExpired == 1,
                        u.status == 1,
                        u.registrationDate,
                        u.userTypes,
                        u.title,
                        profile?.directorateId?.id,
                        profile?.departmentId?.id,
                        profile?.divisionId?.id,
                        profile?.sectionId?.id,
                        profile?.subSectionL1Id?.id,
                        profile?.subSectionL2Id?.id,
                        profile?.designationId?.id,
                        profile?.id,
                        profile?.regionId?.id,
                        profile?.subRegionId?.id
                    )
                )
            }

        return userList.sortedBy { it.id }

    }

    /**
     * Send received registration number to BRS for validation
     * @param request contains the business registration number to be validated
     * @return if a valid company exists return the company details, if none throw and exception
     */
    fun validateBrsNumber(request: BrsConfirmationRequest): UserCompanyEntityDto? {
        val profile = CompanyProfileEntity().apply {
            registrationNumber = request.registrationNumber
            directorIdNumber = request.directorIdNumber
        }
        commonDaoServices.findCompanyProfileWithRegistrationNumber(request.registrationNumber)
            ?.let { throw ExpectedDataNotFound("The Company with this [Registration Number : ${request.registrationNumber}] already exist") }
            ?: run {
                val brsCheck = registrationDaoServices.checkBrs(profile)
                if (brsCheck.first) {
//                    KotlinLogging.logger {  }.info("${profile.directorIdNumber} ${request.directorIdNumber}")
                    val brs = brsCheck.second
                        ?: throw ExpectedDataNotFound("The Company Details Verification details could not be found")
                    return UserCompanyEntityDto().apply {
                        name = brs.businessName
                        kraPin = brs.kraPin
                        directorIdNumber = profile.directorIdNumber
                        profileType = applicationMapProperties.mapUserRequestManufacture
                        registrationNumber = brs.registrationNumber
                        postalAddress = brs.postalAddress
                        physicalAddress = brs.physicalAddress
                        plotNumber = null
                        companyEmail = brs.email
                        companyTelephone = brs.phoneNumber
                        yearlyTurnover = null
                        businessLines = null
                        businessNatures = null
                        buildingName = null
                        streetName = null
                        county = null
                        town = null
                        region = null
                        status = true

                    }

                } else {
                    throw ExpectedDataNotFound("The Company Details Verification failed Due to Invalid Registration Number and/or Director Id")

                }
            }

    }

    /**
     * Send OTP sms to the phone number provided so that we can validate that the user is holding the number
     * @param request object containing the phone number to be validated
     *
     */
    fun sendValidationTokenToCellphoneNumber(request: ValidatePhoneNumberRequestDto): CustomResponse? {
        val result = CustomResponse()
        try {
            /**
             * Generate token
             */
            val otp = commonDaoServices.generateTransactionReference(8).toUpperCase()
            val token = commonDaoServices.generateVerificationToken(otp, request.phone)
            commonDaoServices.sendOtpViaSMS(token)

            result.apply {
                payload = "Success, check your phone for the SMS"
                status = 200
                response = "00"
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.debug(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            result.apply {
                payload = e.message
                status = 500
                response = "99"
            }
        }

        return result
    }

    /**
     * Receive payload with OTP and Phone number and validate that it is valid
     * @param request
     */
    fun validatePhoneNumberAndToken(request: ValidatePhoneNumberTokenRequestDto): CustomResponse? =
        commonDaoServices.validateOTPToken(
            request.token ?: throw NullValueNotAllowedException("Invalid Token provided"), request.phone
        )


    /**
     * Save details provided to the Database, we expect that the organization, initial user and branch should be saved,
     * I have adopted Head Office as the description of the main branch, the names of directors previously fetched from
     * BRS are now persisted to the database
     * @param dto contains the details provided by the registrant
     * @return response indicating whether we were able to successful save the information
     */
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun registerCompany(dto: RegistrationPayloadDto): CustomResponse? {
        val result = CustomResponse()
        try {
            commonDaoServices.findCompanyProfileWithRegistrationNumber(
                dto.company.registrationNumber
                    ?: throw NullValueNotAllowedException("Registration Number is required")
            )
                ?.let { throw ExpectedDataNotFound("The Company with this [Registration Number : ${dto.company.registrationNumber}] already exists") }
                ?: run {

                    val u = dto.user
                    var user = UsersEntity().apply {
                        firstName = u.firstName
                        lastName = u.lastName
                        email = u.email
                        /**
                         * TODO: Revisit number validation
                         */
                        personalContactNumber = dto.company.companyTelephone
                        registrationDate = Date(java.util.Date().time)
                        typeOfUser = applicationMapProperties.transactionActiveStatus
                        title = u.title
                        email = u.email
                        userName = u.userName
                        cellphone = u.cellphone
                        userRegNo = "KEBS${commonDaoServices.generateTransactionReference(5).toUpperCase()}"
                        credentials = BCryptPasswordEncoder().encode(u.credentials)
                        enabled = applicationMapProperties.transactionActiveStatus
                        status = applicationMapProperties.transactionActiveStatus
                        accountLocked = applicationMapProperties.transactionActiveStatus
                        approvedDate = Timestamp.from(Instant.now())
                    }

                    user = usersRepo.save(user)


                    /**
                     * DONE: Create Manufacturer Id role
                     */
                    userRolesRepo.save(
                        registrationDaoServices.userRoleAssignment(
                            user,
                            1,
                            applicationMapProperties.manufacturerAdminRoleId
                                ?: throw NullValueNotAllowedException("Manufacturer Admin role not defined")
                        )
                    )


                    var companyProfileEntity = CompanyProfileEntity().apply {
                        name = dto.company.name
                        kraPin = dto.company.kraPin
                        userId = user.id

                        registrationNumber = dto.company.registrationNumber
                        postalAddress = dto.company.postalAddress
                        physicalAddress = dto.company.physicalAddress
                        plotNumber = dto.company.plotNumber
                        companyEmail = dto.company.companyEmail
                        companyTelephone = dto.company.companyTelephone
                        yearlyTurnover = dto.company.yearlyTurnover
                        businessLines = dto.company.businessLines
                        businessNatures = dto.company.businessNatures
                        buildingName = dto.company.buildingName
                        directorIdNumber = dto.company.directorIdNumber
                        streetName = dto.company.streetName
                        county = dto.company.county
                        town = dto.company.town
                        region = dto.company.region
                        manufactureStatus = applicationMapProperties.transactionActiveStatus
                        status = applicationMapProperties.transactionActiveStatus
                        createdBy = user.userName
                        createdOn = Timestamp.from(Instant.now())
                    }

                    companyProfileEntity = companyProfileRepo.save(companyProfileEntity)

                    brsLookupManufacturerDataRepo.findFirstByRegistrationNumberAndStatusOrderById(
                        companyProfileEntity.registrationNumber
                            ?: throw NullValueNotAllowedException("Invalid BRS Number"), 30
                    )
                        ?.let { record ->
                            brsLookupManufacturerPartnerRepo.findBrsLookupManufacturerPartnersEntitiesByManufacturerIdAndStatus(
                                record.id,
                                30
                            )
                                ?.forEach { partner ->
                                    companyProfileEntity.id?.let {

                                        val companyDirectors = CompanyProfileDirectorsEntity().apply {
                                            companyProfileId = companyProfileEntity.id
                                            directorName = partner.names
                                            directorId = partner.idNumber
                                            userType = partner.idType
                                            status = 1
                                            createdOn = commonDaoServices.getTimestamp()
                                            createdBy = commonDaoServices.concatenateName(user)
                                        }

                                        companyProfileDirectorsRepo.save(companyDirectors)


                                    }
                                }

                        }
                        ?: throw InvalidValueException("No record of look up found on the Datastore")

                    var branch = ManufacturePlantDetailsEntity().apply {
                        companyProfileId = companyProfileEntity.id
                        town = dto.company.town
                        county = dto.company.county
                        physicalAddress = dto.company.physicalAddress
                        street = dto.company.streetName
                        buildingName = dto.company.buildingName
                        nearestLandMark = dto.company.buildingName
                        postalAddress = dto.company.postalAddress
                        telephone = dto.company.companyTelephone
                        emailAddress = dto.company.companyEmail
                        plotNo = dto.company.plotNumber
                        contactPerson = commonDaoServices.concatenateName(user)
                        descriptions = "Head Office"
                        region = dto.company.region

                        createdBy = companyProfileEntity.name
                        createdOn = Timestamp.from(Instant.now())
                        status = applicationMapProperties.transactionActiveStatus
                    }
                    branch = manufacturePlantRepository.save(branch)

                    user.companyId = companyProfileEntity.id
                    user.plantId = branch.id
                    usersRepo.save(user)

                    result.apply {
                        payload = "Successfully Created"
                        status = 200
                        response = "00"
                    }


                }

        } catch (e: Exception) {
            KotlinLogging.logger { }.debug(e.message, e)
            KotlinLogging.logger { }.error(e.message, e)
            result.apply {
                payload = e.message
                status = 500
                response = "99"
            }
        }
        return result

    }


}