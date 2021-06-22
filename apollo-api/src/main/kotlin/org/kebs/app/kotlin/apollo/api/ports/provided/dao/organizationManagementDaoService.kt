package org.kebs.app.kotlin.apollo.api.ports.provided.dao

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.common.dto.OrganizationUserEntityDto
import org.kebs.app.kotlin.apollo.common.dto.PlantEntityDto
import org.kebs.app.kotlin.apollo.common.dto.ProfileDirectorsEntityDto
import org.kebs.app.kotlin.apollo.common.dto.UserCompanyEntityDto
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.InvalidValueException
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.UserRoleAssignmentsEntity
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.qa.ManufacturePlantDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.registration.CompanyProfileDirectorsEntity
import org.kebs.app.kotlin.apollo.store.model.registration.CompanyProfileEntity
import org.kebs.app.kotlin.apollo.store.repo.*
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.sql.Date
import java.sql.Timestamp
import java.time.Instant
import java.util.*

@Service
class RegistrationManagementDaoService(
    private val applicationMapProperties: ApplicationMapProperties,
    private val companyRepo: ICompanyProfileRepository,
    private val userRolesRepo: IUserRoleAssignmentsRepository,
    private val brsLookupManufacturerDataRepo: IBrsLookupManufacturerDataRepository,
    private val brsLookupManufacturerPartnerRepo: IBrsLookupManufacturerPartnersRepository,
    private val companyProfileDirectorsRepo: ICompanyProfileDirectorsRepository,
    private val manufacturePlantRepository: IManufacturePlantDetailsRepository,
    private val usersRepo: IUserRepository
) {


    /**
     * Add or Edit Company Branches user information
     * @param u request payload to be used to add or edit
     * @param user the current user
     */
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updateBranchUsers(
        u: OrganizationUserEntityDto,
        user: UsersEntity,
        branchId: Long,
        companyProfileId: Long
    ): OrganizationUserEntityDto? {
        companyRepo.findByIdOrNull(companyProfileId)
            ?.let { company ->
                if (company.userId != user.id) {
                    throw ExpectedDataNotFound("Authorization failed")
                } else {
                    if ((u.id ?: -2L) < 0) {

                        var entity = UsersEntity().apply {
                            firstName = u.firstName
                            lastName = u.lastName
                            email = u.email
                            /**
                             * TODO: Revisit number validation
                             */
                            personalContactNumber = u.cellphone
                            registrationDate = Date(Date().time)
                            typeOfUser = applicationMapProperties.transactionActiveStatus
                            title = u.title
                            email = u.email
                            userName = u.userName
                            cellphone = u.cellphone
                            userRegNo = "KEBS${generateTransactionReference(5).toUpperCase()}"
                            credentials = BCryptPasswordEncoder().encode(u.credentials)
                            enabled = applicationMapProperties.transactionActiveStatus
                            status = applicationMapProperties.transactionActiveStatus
                            accountLocked = applicationMapProperties.transactionActiveStatus
                            plantId = u.plantId
                            companyId = u.companyId
                            approvedDate = Timestamp.from(Instant.now())
                        }
                        entity = usersRepo.save(entity)
                        /**
                         * TODO: Create Manufacturer Id role
                         */
                        userRolesRepo.save(
                            UserRoleAssignmentsEntity().apply {
                                userId = user.id
                                roleId = applicationMapProperties.manufacturerRoleId
                                status = applicationMapProperties.transactionActiveStatus
                                createdBy = "${user.userName}"
                                createdOn = Timestamp.from(Instant.now())

                            }
                        )
                        return OrganizationUserEntityDto(
                            entity.id,
                            entity.firstName,
                            entity.lastName,
                            entity.userName,
                            entity.email,
                            entity.enabled == 1,
                            entity.status == 1,
                            entity.title,
                            null,
                            entity.cellphone
                        ).apply {
                            plantId = entity.plantId
                            companyId = entity.companyId
                        }

                    } else {
                        usersRepo.findByIdOrNull(u.id ?: throw NullValueNotAllowedException("Invalid Record"))
                            ?.let { entity ->
                                entity.apply {
                                    firstName = u.firstName
                                    lastName = u.lastName
                                    email = u.email
                                    /**
                                     * TODO: Revisit number validation
                                     */
                                    personalContactNumber = u.cellphone
                                    cellphone = u.cellphone
                                    registrationDate = Date(Date().time)
                                    typeOfUser = applicationMapProperties.transactionActiveStatus
                                    title = u.title
                                    email = u.email
                                    userName = u.userName
                                    plantId = u.plantId
                                    companyId = u.companyId
                                    approvedDate = Timestamp.from(Instant.now())
                                }
                                val userEntity = usersRepo.save(entity)
                                /**
                                 * TODO: Create Manufacturer Id role
                                 */
                                userRolesRepo.save(
                                    UserRoleAssignmentsEntity().apply {
                                        userId = user.id
                                        roleId = applicationMapProperties.manufacturerRoleId
                                        status = applicationMapProperties.transactionActiveStatus
                                        createdBy = "${user.userName}"
                                        createdOn = Timestamp.from(Instant.now())

                                    }
                                )
                                return OrganizationUserEntityDto(
                                    userEntity.id,
                                    userEntity.firstName,
                                    userEntity.lastName,
                                    userEntity.userName,
                                    userEntity.email,
                                    userEntity.enabled == 1,
                                    userEntity.status == 1,
                                    userEntity.title,
                                    null,
                                    userEntity.cellphone
                                ).apply {
                                    plantId = userEntity.plantId
                                    companyId = userEntity.companyId
                                }
                            }
                            ?: throw NullValueNotAllowedException("Record not found")

                    }
                }
            }
            ?: throw ExpectedDataNotFound("A valid company is required")

    }

    /**
     * Add or Edit Company Director's information
     * @param dto request payload to be used to add or edit
     * @param user the current user
     */
    fun updateProfileDirectors(dto: ProfileDirectorsEntityDto, user: UsersEntity): ProfileDirectorsEntityDto? {
        if ((dto.id ?: -2L) < 0) {

            var entity = CompanyProfileDirectorsEntity().apply {
                companyProfileId = dto.companyProfileId
                directorName = dto.directorName
                directorId = dto.directorId
                description = dto.description
                createdBy = user.userName
                createdOn = Timestamp.from(Instant.now())
                status = if (dto.status) 1 else 0
            }
            entity = companyProfileDirectorsRepo.save(entity)
            return profileDirectorsDtoFromEntity(entity)

        } else {
            companyProfileDirectorsRepo.findByIdOrNull(dto.id ?: throw NullValueNotAllowedException("Invalid Record"))
                ?.let { entity ->
                    entity.apply {
                        companyProfileId = dto.companyProfileId
                        directorName = dto.directorName
                        directorId = dto.directorId
                        description = dto.description
                        createdBy = user.userName
                        createdOn = Timestamp.from(Instant.now())
                        status = if (dto.status) 1 else 0
                    }
                    val directorsEntity = companyProfileDirectorsRepo.save(entity)
                    return profileDirectorsDtoFromEntity(directorsEntity)
                }
                ?: throw NullValueNotAllowedException("Record not found")

        }
    }

    private fun profileDirectorsDtoFromEntity(entity: CompanyProfileDirectorsEntity): ProfileDirectorsEntityDto {
        return ProfileDirectorsEntityDto().apply {
            companyProfileId = entity.companyProfileId
            directorName = entity.directorName
            directorId = entity.directorId
            description = entity.description
            status = entity.status == 1

        }
    }

    fun generateTransactionReference(length: Int = 12): String {
        return generateRandomText(length, "SHA1PRNG", "SHA-1")
    }

    /**
     * Add or Edit Company branch information
     * @param dto request payload to be used to add or edit
     * @param user the current user
     */
    fun updatePlantEntity(dto: PlantEntityDto, user: UsersEntity): PlantEntityDto? {
        if ((dto.id ?: -2L) < 0) {
            val entity = ManufacturePlantDetailsEntity().apply {
                town = dto.town
                county = dto.county
                companyProfileId = dto.companyProfileId
                physicalAddress = dto.physicalAddress
                street = dto.street
                buildingName = dto.buildingName
                nearestLandMark = dto.buildingName
                postalAddress = dto.postalAddress
                telephone = dto.telephone
                emailAddress = dto.emailAddress
                plotNo = dto.plotNo
                contactPerson = dto.contactPerson
                designation = dto.designation
                descriptions = "Head Office"
                region = dto.region

                createdBy = user.userName
                createdOn = Timestamp.from(Instant.now())
                status = if (dto.status) 1 else 0
            }
            manufacturePlantRepository.save(entity)
            return plantEntityDtoFromEntity(entity)

        } else {
            manufacturePlantRepository.findByIdOrNull(dto.id ?: throw NullValueNotAllowedException("Invalid Record"))
                ?.let { entity ->
                    entity.apply {
                        town = dto.town
                        county = dto.county
                        physicalAddress = dto.physicalAddress
                        street = dto.street
                        buildingName = dto.buildingName
                        nearestLandMark = dto.buildingName
                        postalAddress = dto.postalAddress
                        telephone = dto.telephone
                        emailAddress = dto.emailAddress
                        plotNo = dto.plotNo
                        contactPerson = dto.contactPerson
                        designation = dto.designation
                        descriptions = "Head Office"
                        region = dto.region

                        lastModifiedBy = user.userName
                        lastModifiedOn = Timestamp.from(Instant.now())
                        status = if (dto.status) 1 else 0
                    }
                    val plantDetailsEntity = manufacturePlantRepository.save(entity)
                    return plantEntityDtoFromEntity(plantDetailsEntity)
                }
                ?: throw NullValueNotAllowedException("Record not found")

        }
    }


    /**
     * Fetch a company's branches based on the company ID and the user logged in
     * @param companyId the primary key of the parent company
     * @param userId the currently logged-in user
     */
    fun fetchBranchesByCompanyIdAndUserId(companyId: Long, userId: Long): List<PlantEntityDto>? {
        return companyRepo.findByIdOrNull(companyId)
            ?.let { entity ->
                if (entity.userId != userId) {
                    throw ExpectedDataNotFound("Authorization failed")
                } else {
                    manufacturePlantRepository.findByCompanyProfileId(companyId)
                        ?.sortedBy { it.id }?.map {
                            plantEntityDtoFromEntity(it)
                        }
                }
            }
            ?: throw ExpectedDataNotFound("A valid company is required")

    }

    /**
     * Fetch a company's users based on the company ID, branch ID and the user logged in
     * @param companyProfileId the primary key of the parent company
     * @param branchId the branch to fetch users by
     * @param userId the currently logged-in user
     */
    fun fetchUsersByCompanyIdAndBranchIdAndUserId(
        companyProfileId: Long,
        branchId: Long,
        userId: Long
    ): List<OrganizationUserEntityDto>? {
        return companyRepo.findByIdOrNull(companyProfileId)
            ?.let { entity ->
                if (entity.userId != userId) {
                    throw ExpectedDataNotFound("Authorization failed")
                } else {
                    usersRepo.findAllByCompanyIdAndPlantId(companyProfileId, branchId).sortedBy { it.id }.map {
                        OrganizationUserEntityDto(
                            it.id,
                            it.firstName,
                            it.lastName,
                            it.userName,
                            it.email,
                            it.enabled == 1,
                            it.status == 1,
                            it.title,
                            null,
                            it.cellphone
                        ).apply {
                            plantId = it.plantId
                            companyId = it.companyId
                        }
                    }
                }
            }
            ?: throw ExpectedDataNotFound("A valid company is required")

    }

    /**
     * Fetch a company's directors based on the company ID and the user logged in
     * @param companyId the primary key of the parent company
     * @param userId the currently logged-in user
     */
    fun fetchDirectorsByCompanyIdAndUserId(companyId: Long, userId: Long): List<ProfileDirectorsEntityDto>? {
        return companyRepo.findByIdOrNull(companyId)
            ?.let { entity ->
                if (entity.userId != userId) {
                    throw ExpectedDataNotFound("Authorization failed")
                } else {
                    companyProfileDirectorsRepo.findByCompanyProfileId(companyId)
                        ?.sortedBy { it.id }?.map {
                            profileDirectorsDtoFromEntity(it)
                        }
                }
            }
            ?: throw ExpectedDataNotFound("A valid company is required")

    }

    /**
     * Fetch a details for a specific company's branches based on the ID and the user logged in
     * @param companyId the primary key of the parent company
     * @param userId the currently logged-in user
     */
    fun fetchBranchesByIdAndUserId(companyId: Long, userId: Long): PlantEntityDto? {
        return companyRepo.findByIdOrNull(companyId)
            ?.let { entity ->
                if (entity.userId != userId) {
                    throw ExpectedDataNotFound("Authorization failed")
                } else {
                    manufacturePlantRepository.findByIdOrNull(companyId)
                        ?.let {
                            plantEntityDtoFromEntity(it)

                        }
                        ?: throw ExpectedDataNotFound("Record not found")
                }
            }
            ?: throw ExpectedDataNotFound("A valid company is required")

    }

    /**
     * Fetch a details for a specific company's branches based on the ID and the user logged in
     * @param companyId the primary key of the parent company
     * @param userId the currently logged-in user
     */
    fun fetchDirectorsByIdAndUserId(companyId: Long, userId: Long): ProfileDirectorsEntityDto? {
        return companyRepo.findByIdOrNull(companyId)
            ?.let { entity ->
                if (entity.userId != userId) {
                    throw ExpectedDataNotFound("Authorization failed")
                } else {
                    companyProfileDirectorsRepo.findByIdOrNull(companyId)
                        ?.let {
                            profileDirectorsDtoFromEntity(it)

                        }
                        ?: throw ExpectedDataNotFound("Record not found")
                }
            }
            ?: throw ExpectedDataNotFound("A valid company is required")

    }

    /**
     * Fetch a details for a specific company's branches based on the ID and the user logged in
     * @param companyProfileId the primary key of the parent company
     * @param userId the currently logged-in user
     * @param id the primary key of the user
     */
    fun fetchUserByIdAndUserId(companyProfileId: Long, userId: Long, id: Long): OrganizationUserEntityDto? {
        return companyRepo.findByIdOrNull(companyProfileId)
            ?.let { entity ->
                if (entity.userId != userId) {
                    throw ExpectedDataNotFound("Authorization failed")
                } else {
                    usersRepo.findByIdOrNull(id)
                        ?.let {
                            OrganizationUserEntityDto(
                                it.id,
                                it.firstName,
                                it.lastName,
                                it.userName,
                                it.email,
                                it.enabled == 1,
                                it.status == 1,
                                it.title,
                                null,
                                it.cellphone
                            ).apply {
                                plantId = it.plantId
                                companyId = it.companyId
                            }

                        }
                        ?: throw ExpectedDataNotFound("Record not found")
                }
            }
            ?: throw ExpectedDataNotFound("A valid company is required")

    }

    /**
     * Map a PlantEntityDto from ManufacturePlantDetailsEntity
     */
    private fun plantEntityDtoFromEntity(it: ManufacturePlantDetailsEntity): PlantEntityDto {
        return PlantEntityDto().apply {
            id = it.id
            companyProfileId = it.companyProfileId
            location = it.location
            street = it.street
            buildingName = it.buildingName
            nearestLandMark = it.nearestLandMark
            postalAddress = it.postalAddress
            telephone = it.telephone
            emailAddress = it.emailAddress
            physicalAddress = it.physicalAddress
            faxNo = it.faxNo
            plotNo = it.plotNo
            designation = it.designation
            contactPerson = it.contactPerson
            status = it.status == applicationMapProperties.transactionActiveStatus
            descriptions = it.descriptions
            region = it.region
            county = it.county
            town = it.town
        }
    }

    /**
     * Fetch all companies but based on the user that has logged in,
     * if it is a KEBS Employee return last 20 companies registered
     * if it is a manufacturer main user return only companies associated with the user
     * for all other users return a generic error
     * @param userId the user making the request
     */
    fun fetchCompaniesByUserId(userId: Long): List<UserCompanyEntityDto>? {
        val isEmployee = userRolesRepo.findByUserIdAndRoleIdAndStatus(
            userId,
            applicationMapProperties.slEmployeeRoleId
                ?: throw ExpectedDataNotFound("Role definition for employees not done"),
            1
        )?.id != null
        when {
            isEmployee -> {
                return companyRepo.findAllByOrderByIdDesc(
                    PageRequest.of(
                        applicationMapProperties.pageStart,
                        applicationMapProperties.pageRecords
                    )
                )?.sortedBy { it.id }?.map {
                    UserCompanyEntityDto(
                        it.name,
                        it.kraPin,
                        it.userId,
                        null,
                        it.registrationNumber,
                        it.postalAddress,
                        it.physicalAddress,
                        it.plotNumber,
                        it.companyEmail,
                        it.companyTelephone,
                        it.yearlyTurnover,
                        it.businessLines,
                        it.businessNatures,
                        it.buildingName,
                        it.streetName,
                        it.directorIdNumber,
                        it.region,
                        it.county,
                        it.town
                    ).apply {
                        id = it.id
                        status = it.status == 1
                    }
                }

            }
            else -> {
                return companyRepo.findAllByUserId(userId).sortedBy { it.id }.map {
                    UserCompanyEntityDto(
                        it.name,
                        it.kraPin,
                        it.userId,
                        null,
                        it.registrationNumber,
                        it.postalAddress,
                        it.physicalAddress,
                        it.plotNumber,
                        it.companyEmail,
                        it.companyTelephone,
                        it.yearlyTurnover,
                        it.businessLines,
                        it.businessNatures,
                        it.buildingName,
                        it.streetName,
                        it.directorIdNumber,
                        it.region,
                        it.county,
                        it.town
                    ).apply {
                        id = it.id
                        status = it.status == 1
                    }
                }
            }
        }
    }

    /**
     * Fetch single company
     */
    fun fetchCompanyById(identifier: Long, userId: Long): UserCompanyEntityDto? =
        companyRepo.findByIdOrNull(identifier)?.let {
            if (it.userId == userId) {
                UserCompanyEntityDto(
                    it.name,
                    it.kraPin,
                    it.userId,
                    null,
                    it.registrationNumber,
                    it.postalAddress,
                    it.physicalAddress,
                    it.plotNumber,
                    it.companyEmail,
                    it.companyTelephone,
                    it.yearlyTurnover,
                    it.businessLines,
                    it.businessNatures,
                    it.buildingName,
                    it.streetName,
                    it.directorIdNumber,
                    it.region,
                    it.county,
                    it.town
                ).apply {
                    id = it.id
                    status = it.status == 1
                }
            } else throw InvalidValueException("Attempt to fetch Organization rejected")
        }

    fun updateCompanyDetails(dto: UserCompanyEntityDto, user: UsersEntity): UserCompanyEntityDto? {
        if ((dto.id ?: -2L) < 0) {
            return registerCompany(dto, user)

        } else {
            companyRepo.findByIdOrNull(dto.id ?: throw NullValueNotAllowedException("Invalid Record"))
                ?.let { entity ->
                    entity.apply {
                        name = dto.name
                        kraPin = dto.kraPin
                        userId = user.id

                        registrationNumber = dto.registrationNumber
                        postalAddress = dto.postalAddress
                        physicalAddress = dto.physicalAddress
                        plotNumber = dto.plotNumber
                        companyEmail = dto.companyEmail
                        companyTelephone = dto.companyTelephone
                        yearlyTurnover = dto.yearlyTurnover
                        businessLines = dto.businessLines
                        businessNatures = dto.businessNatures
                        buildingName = dto.buildingName
                        directorIdNumber = dto.directorIdNumber
                        streetName = dto.streetName
                        county = dto.county
                        town = dto.town
                        region = dto.region
                        manufactureStatus = applicationMapProperties.transactionActiveStatus
                        status = applicationMapProperties.transactionActiveStatus
                        createdBy = user.userName
                        createdOn = Timestamp.from(Instant.now())
                    }

                    val companyProfileEntity = companyRepo.save(entity)

                    return UserCompanyEntityDto(
                        companyProfileEntity.name,
                        companyProfileEntity.kraPin,
                        companyProfileEntity.userId,
                        null,
                        companyProfileEntity.registrationNumber,
                        companyProfileEntity.postalAddress,
                        companyProfileEntity.physicalAddress,
                        companyProfileEntity.plotNumber,
                        companyProfileEntity.companyEmail,
                        companyProfileEntity.companyTelephone,
                        companyProfileEntity.yearlyTurnover,
                        companyProfileEntity.businessLines,
                        companyProfileEntity.businessNatures,
                        companyProfileEntity.buildingName,
                        companyProfileEntity.streetName,
                        companyProfileEntity.directorIdNumber,
                        companyProfileEntity.region,
                        companyProfileEntity.county,
                        companyProfileEntity.town
                    ).apply {
                        id = companyProfileEntity.id

                        status = companyProfileEntity.status == 1
                    }

                }
                ?: throw NullValueNotAllowedException("Record not found")

        }
    }

    /**
     * Save details provided to the Database, we expect that the organization, initial user and branch should be saved,
     * I have adopted Head Office as the description of the main branch, the names of directors previously fetched from
     * BRS are now persisted to the database
     * @param dto contains the details provided by the registrant
     * @param user the user making the request
     * @return response indicating whether we were able to successful save the information
     */
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun registerCompany(dto: UserCompanyEntityDto, user: UsersEntity): UserCompanyEntityDto? {
        try {
            companyRepo.findByRegistrationNumber(
                dto.registrationNumber
                    ?: throw NullValueNotAllowedException("Registration Number is required")
            )
                ?.let { throw ExpectedDataNotFound("The Company with this [Registration Number : ${dto.registrationNumber}] already exists") }
                ?: run {


                    var companyProfileEntity = CompanyProfileEntity().apply {
                        name = dto.name
                        kraPin = dto.kraPin
                        userId = user.id

                        registrationNumber = dto.registrationNumber
                        postalAddress = dto.postalAddress
                        physicalAddress = dto.physicalAddress
                        plotNumber = dto.plotNumber
                        companyEmail = dto.companyEmail
                        companyTelephone = dto.companyTelephone
                        yearlyTurnover = dto.yearlyTurnover
                        businessLines = dto.businessLines
                        businessNatures = dto.businessNatures
                        buildingName = dto.buildingName
                        directorIdNumber = dto.directorIdNumber
                        streetName = dto.streetName
                        county = dto.county
                        town = dto.town
                        region = dto.region
                        manufactureStatus = 1
                        createdBy = user.userName
                        createdOn = Timestamp.from(Instant.now())
                    }

                    companyProfileEntity = companyRepo.save(companyProfileEntity)

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
                                            createdOn = Timestamp.from(Instant.now())
                                            createdBy = user.userName
                                        }

                                        companyProfileDirectorsRepo.save(companyDirectors)


                                    }
                                }

                        }
                        ?: throw InvalidValueException("No record of look up found on the Datastore")

                    val branch = ManufacturePlantDetailsEntity().apply {
                        companyProfileId = companyProfileEntity.id
                        town = dto.town
                        county = dto.county
                        physicalAddress = dto.physicalAddress
                        street = dto.streetName
                        buildingName = dto.buildingName
                        nearestLandMark = dto.buildingName
                        postalAddress = dto.postalAddress
                        telephone = dto.companyTelephone
                        emailAddress = dto.companyEmail
                        plotNo = dto.plotNumber
                        contactPerson = user.userName
                        descriptions = "Head Office"
                        region = dto.region

                        createdBy = companyProfileEntity.name
                        createdOn = Timestamp.from(Instant.now())
                        status = 1
                    }
                    manufacturePlantRepository.save(branch)


                    return UserCompanyEntityDto(
                        companyProfileEntity.name,
                        companyProfileEntity.kraPin,
                        companyProfileEntity.userId,
                        null,
                        companyProfileEntity.registrationNumber,
                        companyProfileEntity.postalAddress,
                        companyProfileEntity.physicalAddress,
                        companyProfileEntity.plotNumber,
                        companyProfileEntity.companyEmail,
                        companyProfileEntity.companyTelephone,
                        companyProfileEntity.yearlyTurnover,
                        companyProfileEntity.businessLines,
                        companyProfileEntity.businessNatures,
                        companyProfileEntity.buildingName,
                        companyProfileEntity.streetName,
                        companyProfileEntity.directorIdNumber,
                        companyProfileEntity.region,
                        companyProfileEntity.county,
                        companyProfileEntity.town
                    ).apply { id = companyProfileEntity.id }


                }

        } catch (e: Exception) {
            KotlinLogging.logger { }.debug(e.message, e)
            KotlinLogging.logger { }.error(e.message, e)
            throw e
        }

    }

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

}