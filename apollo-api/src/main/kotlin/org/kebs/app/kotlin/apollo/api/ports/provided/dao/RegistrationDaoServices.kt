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

package org.kebs.app.kotlin.apollo.api.ports.provided.dao

import mu.KotlinLogging
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.engine.runtime.ProcessInstance
import org.flowable.task.api.Task
import org.kebs.app.kotlin.apollo.adaptor.kafka.producer.service.SendToKafkaQueue
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.StandardsLevyBpmn
import org.kebs.app.kotlin.apollo.common.dto.UserCompanyEntityDto
import org.kebs.app.kotlin.apollo.common.dto.UserEntityDto
import org.kebs.app.kotlin.apollo.common.dto.UserPasswordVerificationValuesDto
import org.kebs.app.kotlin.apollo.common.dto.UserRequestEntityDto
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.MissingConfigurationException
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.common.exceptions.ServiceMapNotFoundException
import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.registration.CompanyProfileEntity
import org.kebs.app.kotlin.apollo.store.repo.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Lazy
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@Service
class RegistrationDaoServices(
    private val usersRepo: IUserRepository,
    private val applicationMapProperties: ApplicationMapProperties,
    private val userVerificationTokensRepository: IUserVerificationTokensRepository,

    private val employeesRepo: IEmployeesRepository,
    private val userProfilesRepo: IUserProfilesRepository,
    private val iImporterRepo: IImporterRepository,
    private val iImporterContactRepo: IImporterContactRepository,
    private val serviceRequestsRepository: IServiceRequestsRepository,
    private val serviceMapsRepository: IServiceMapsRepository,
    private val notifications: Notifications,
    private val commonDaoServices: CommonDaoServices,

    private val notificationsRepo: INotificationsRepository,
    private val workflowTransactionsRepository: IWorkflowTransactionsRepository,
    private val verificationTokensRepo: IUserVerificationTokensRepository,
    private val contactDetailsRepository: IManufacturerContactsRepository,
    private val manufacturersRepo: IManufacturerRepository,
    private val manufacturerAddressesEntityRepo: IManufacturerAddressRepository,
    private val stdLevyNotificationFormRepo: IStdLevyNotificationFormRepository,
    private val userTypesRepo: IUserTypesEntityRepository,
    private val subSectionsLevel1Repo: ISubSectionsLevel1Repository,
    private val subSectionsLevel2Repo: ISubSectionsLevel2Repository,
    private val titlesRepository: ITitlesRepository,
    private val divisionsRepo: IDivisionsRepository,
    private val departmentsRepo: IDepartmentsRepository,
    private val directorateRepo: IDirectoratesRepository,
    private val designationRepo: IDesignationsRepository,
    private val sectionsRepo: ISectionsRepository,
    private val subRegionsRepo: ISubRegionsRepository,
    private val regionsRepo: IRegionsRepository,
    private val countiesRepo: ICountiesRepository,
    private val townsRepo: ITownsRepository,
    private val userRolesRepo: IUserRolesRepository,
    private val userRolesAssignmentsRepository: IUserRoleAssignmentsRepository,
    private val sendToKafkaQueue: SendToKafkaQueue,
    private val runtimeService: RuntimeService,
    private val taskService: TaskService,
    private val bufferRepo: INotificationsBufferRepository,
    private val yearlyTurnoverRepo: IManufacturerPaymentDetailsRepository,
    private val standardLevyPaymentsRepository: IStandardLevyPaymentsRepository,
    private val standardsLevyBpmn: StandardsLevyBpmn
) {


    @Lazy
    @Autowired
    lateinit var systemsAdminDaoService: SystemsAdminDaoService

    /***********************************************************************************
     * NEW REGISTRATIONION SERVICES
     ***********************************************************************************/

    fun findServiceRequestByTransactionRef(ref: String): ServiceRequestsEntity? =
        serviceRequestsRepository.findFirstByTransactionReference(ref)

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun validateVerificationToken(data: UserPasswordVerificationValuesDto, appID: Int): ServiceRequestsEntity {
        var log: WorkflowTransactionsEntity? = null
        val token = data.otpVerification ?: throw Exception("Verification Token not found")
        var sr = findServiceRequestByTransactionRef(token)
            ?: throw Exception("Verification Token not found in service request transactions")
        try {
            sr.serviceMapsId
                ?.let { map ->
                    sr.payload = data.otpVerification
                    log = createTransactionLog(sr, map)
                    val verificationToken = verificationTokensRepo.findByTokenAndStatus(token, map.initStatus)
                        ?: throw Exception("Verification Token not found")
                    log?.integrationResponse = "${verificationToken.id}"
                    val expiry = verificationToken.tokenExpiryDate
                        ?: throw Exception("Verification Token without a valid expiry found")
                    when {
                        expiry.after(Timestamp.from(Instant.now())) -> {
                            /**
                             * If user exists activate and enable
                             */
                            verificationToken.userId
                                ?.let { user ->
                                    user.credentials = BCryptPasswordEncoder().encode(data.password)
                                    user.confirmCredentials = BCryptPasswordEncoder().encode(data.passwordConfirmation)
                                    activateUser(user, map, sr)
                                    resetUserPassword(map, user, true, sr)?.let { requestsEntity ->
                                        sr = requestsEntity
                                    }
                                    val emailEntity = commonDaoServices.userRegisteredSuccessfulEmailCompose(
                                        user,
                                        sr,
                                        map,
                                        verificationToken.token
                                    )
                                    sr.payload?.let {
                                        commonDaoServices.sendEmailAfterCompose(
                                            user,
                                            applicationMapProperties.mapUserPasswordChangedNotification,
                                            emailEntity,
                                            appID,
                                            it
                                        )
                                    }
                                }

                            verificationToken.status = map.inactiveStatus
                            verificationToken.lastModifiedOn = Timestamp.from(Instant.now())
                            verificationToken.lastModifiedBy = "Verification Token Received"
                            verificationTokensRepo.save(verificationToken)

                        }
                        else -> {
                            verificationToken.status = map.failedStatus
                            verificationToken.lastModifiedOn = Timestamp.from(Instant.now())
                            verificationToken.lastModifiedBy = "Expired Verification Token Received"
                            verificationTokensRepo.save(verificationToken)
                            throw Exception("Expired Verification Token Received")
                        }
                    }

                    sr.responseStatus = sr.serviceMapsId?.successStatusCode
                    sr.responseMessage = "Success ${sr.payload}"
                    sr.status = map.successStatus
                    sr.processingEndDate = Timestamp.from(Instant.now())

                    log?.responseMessage = "Token generation successful"
                    log?.responseStatus = map.successStatusCode
                    log?.transactionStatus = map.successStatus

                }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            log?.responseMessage = e.message
            log?.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            log?.transactionStatus = sr.serviceMapsId?.exceptionStatus!!
        }
        log?.transactionCompletedDate = Timestamp.from(Instant.now())
        try {
            sr = serviceRequestsRepository.save(sr)
            log?.let { log = workflowTransactionsRepository.save(it) }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error { e }
        }
        return sr

    }

    fun activateUser(user: UsersEntity, map: ServiceMapsEntity, sr: ServiceRequestsEntity): Int? {
        with(user) {
            enabled = map.activeStatus
            status = map.activeStatus
            accountLocked = map.activeStatus
            approvedDate = Timestamp.from(Instant.now())
            usersRepo.save(user)
        }
        sr.status = 35
        serviceRequestsRepository.save(sr)

        return sr.status
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun resetUserPassword(
        s: ServiceMapsEntity,
        usersEntity: UsersEntity,
        initial: Boolean,
        sr: ServiceRequestsEntity? = null
    ): ServiceRequestsEntity? {
        sr ?: commonDaoServices.createServiceRequest(s)
        try {
            var user = usersEntity
            with(user) {
                confirmCredentials = ""
                accountExpired = s.inactiveStatus
                accountLocked = s.inactiveStatus
                credentialsExpired = s.inactiveStatus
                status = s.activeStatus
                approvedDate = commonDaoServices.getTimestamp()
                modifiedBy = userName
                modifiedOn = commonDaoServices.getTimestamp()
                user = usersRepo.save(user)

            }
            sr?.payload = "User[id= ${user.id}] Activated"
//            when {
//
//                initial -> {
//                    /**
//                     ** Assign default role and /or profile
//                     **/
//                    userTypesRepo.findByIdOrNull(user.userTypes)
//                            ?.let { userType ->
//                                if (user.id?.let { userRolesAssignmentsRepository.findByUserId(it) } == null) {
//                                    val assignmentsEntity = userRolesAssignmentsRepository.save(userRoleAssignment(user, s))
//                                    sr?.payload = "${sr?.payload}: Default Role Assigned[id=${assignmentsEntity.id}]"
//                                }
//                            }
//
//
//                }
//            }
//
//

            sr?.responseStatus = sr?.serviceMapsId?.successStatusCode
            sr?.responseMessage = "Success ${sr?.payload}"
            sr?.status = s.successStatus
            sr?.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.trace(e.message, e)
            sr?.status = sr?.serviceMapsId?.exceptionStatus
            sr?.responseStatus = sr?.serviceMapsId?.exceptionStatusCode
            sr?.responseMessage = e.message

        }

        sr?.let { serviceRequestsRepository.save(it) }

        KotlinLogging.logger { }.trace("${sr?.id} ${sr?.responseStatus}")

        return sr
    }


    /***********************************************************************************
     * OLD REGISTRATIONION SERVICES
     ***********************************************************************************/
    @Value("\${user.profile.employee.userTypeID}")
    lateinit var employeeUserTypeID: String

    @Value("\${user.profile.importer.userTypeID}")
    lateinit var importerUserTypeID: String

    @Value("\${user.profile.manufacturer.userTypeID}")
    lateinit var manufacturerUserTypeID: String

    private fun generateCredentials(map: ServiceMapsEntity): String =
        map.passwordLength
            ?.let {
                BCryptPasswordEncoder().encode(
                    generateRandomText(
                        it,
                        map.secureRandom,
                        map.messageDigestAlgorithm,
                        false
                    )
                )
            }
            ?: ""


    fun assignUserType(userType: Long?): UserTypesEntity? = userTypesRepo.findByIdOrNull(userType)
    fun assignCounty(countyId: Long?): CountiesEntity? = countiesRepo.findByIdOrNull(countyId)
    fun assignTown(townId: Long?): TownsEntity? = townsRepo.findByIdOrNull(townId)
    fun assignDepartment(departmentId: Long?): DepartmentsEntity? = departmentsRepo.findByIdOrNull(departmentId)
    fun assignDirectorate(directorateId: Long?): DirectoratesEntity? = directorateRepo.findByIdOrNull(directorateId)
    fun assignDivision(divisionId: Long?): DivisionsEntity? = divisionsRepo.findByIdOrNull(divisionId)
    fun assignSubRegion(subRegionId: Long?): SubRegionsEntity? = subRegionsRepo.findByIdOrNull(subRegionId)
    fun assignDesignation(designationId: Long?): DesignationsEntity? = designationRepo.findByIdOrNull(designationId)
    fun assignSection(sectionId: Long?): SectionsEntity? = sectionsRepo.findByIdOrNull(sectionId)
    fun assignRegion(regionId: Long?): RegionsEntity? = regionsRepo.findByIdOrNull(regionId)
    fun assignSubSectionL1(subSectionL1Id: Long?): SubSectionsLevel1Entity? =
        subSectionsLevel1Repo.findByIdOrNull(subSectionL1Id)

    fun assignSubSectionL2(subSectionL2Id: Long?): SubSectionsLevel2Entity? =
        subSectionsLevel2Repo.findByIdOrNull(subSectionL2Id)

    fun assignUserTypeByTypeName(userType: String, s: ServiceMapsEntity): UserTypesEntity? =
        userTypesRepo.findByTypeNameAndStatus(userType, s.activeStatus)


    fun userRoleAssignment(user: UsersEntity, active: Int, roleID: Long): UserRoleAssignmentsEntity {
        val assignmentsEntity = UserRoleAssignmentsEntity()
        with(assignmentsEntity) {
//            userRolesRepo.findByIdOrNull(user.userTypes?.defaultRole)

            userId = user.id
//            roleId = userTypesRepo.findByIdOrNull(user.userTypes)?.defaultRole ?: -1L
            roleId = roleID
            status = active
            createdBy = "${user.userName}"
            createdOn = Timestamp.from(Instant.now())

        }
        return assignmentsEntity
    }

    fun userRoleAssignmentUpdate(user: UsersEntity, s: ServiceMapsEntity): UserRoleAssignmentsEntity {
        userRolesAssignmentsRepository.findByUserId(user.id ?: -1L)
            ?.let { userRoleAssignmentsEntity ->
                with(userRoleAssignmentsEntity) {
                    userId = user.id
//                        roleId = user.roleId
                    status = s.activeStatus
                    lastModifiedBy = "${user.firstName} ${user.lastName}"
                    lastModifiedOn = Timestamp.from(Instant.now())

                }
                return userRoleAssignmentsEntity
            }
            ?: throw Exception("User with the following [id=$user.id], recheck your user")

    }

    //    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun registerManufacturer(
        s: ServiceMapsEntity,
        manufacturerContactsEntity: ManufacturerContactsEntity,
        stdLevyNotificationFormEntity: StdLevyNotificationFormEntity,
        manufacturersEntity: ManufacturersEntity,
        manufacturerAddressesEntity: ManufacturerAddressesEntity,
        userTypeId: Int?,
        userId: Long,
        yearlyTurnoverEntity: ManufacturePaymentDetailsEntity
    ): ServiceRequestsEntity {

        var sr = commonDaoServices.createServiceRequest(s)
        try {

//            val user = usersRepo.save(extractUserFromManufacturer(manufacturerContactsEntity, s, userTypeId))
            val user = usersRepo.save(updateUserFromUserEntity(userId, s))
            sr.payload = "User[id= ${user.id}]"
            sr.names = "${user.firstName} ${user.lastName}"

            val manufacturer = manufacturersInit(manufacturersEntity, sr, user, s)
            sr.payload = "${sr.payload}: Manufacturer[id=${manufacturer.id}]"

            val add = manufacturerAddressesInit(manufacturerAddressesEntity, manufacturer, s, sr)
            sr.payload = "${sr.payload}: ManufacturerAddresses[id=${add.id}]"

            val stdLevyForm = manufacturerStdLevyInit(stdLevyNotificationFormEntity, manufacturer, s, sr)
            sr.payload = "${sr.payload}: ManufacturerStdLevyForm[id=${stdLevyForm.id}]"

            val contact = contactDetailsRepository.save(
                manufacturerContactsInit(
                    manufacturerContactsEntity,
                    sr,
                    user,
                    manufacturer
                )
            )
            sr.payload = "${sr.payload}: ManufacturerContact[id=${contact.id}]"

            val turnover = yearlyTurnoverInit(yearlyTurnoverEntity, manufacturer, s, sr)
            sr.payload = "${sr.payload}: YearlyTurnover[id=${turnover.id}]"

            // Mimic KRA table
            val standardLevyPayments = StandardLevyPaymentsEntity()
            val kra = kraInit(manufacturer, standardLevyPayments, turnover, sr, s)
            sr.payload = "${sr.payload}: Amount to pay[id=${kra.id}]"

            // Start standard levy bpmn registration process
            standardsLevyBpmn
                .let {
                    it.startSlRegistrationProcess(kra.id, 681)
                    it.slManufacturerRegistrationComplete(kra.id, true)
                    it.slFillSl1CFormComplete(kra.id)
                    it.slSubmitDetailsComplete(kra.id)
                }

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = s.successStatus

            val variables = mutableMapOf<String, Any?>()
            variables["map"] = s
            variables["contact"] = manufacturerContactsEntity
            variables["manufacturer"] = manufacturersEntity
            variables["address"] = manufacturerAddressesEntity
            variables["userTypeId"] = user.userTypes
            variables["user"] = user
            variables["continue"] = false

            variables["transactionRef"] = sr.transactionReference
            variables["sr"] = sr
//            runtimeService.startProcessInstanceByKey(s.bpmnProcessKey, variables)
            sr = serviceRequestsRepository.save(sr)
            runtimeService
                .createProcessInstanceBuilder()
                .processDefinitionKey(s.bpmnProcessKey)
                .businessKey(sr.transactionReference)
                .variables(variables)
                .start()
            KotlinLogging.logger { }.info { "Started bpmn registration process" }

//            sendToKafkaQueue.submitAsyncRequestToBus(manufacturer, s.serviceTopic)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepository.save(sr)

        }

//        sr = serviceRequestsRepository.save(sr)
        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return sr
    }

    fun registerImporter(
        s: ServiceMapsEntity,
        importerContactDetailsEntity: ImporterContactDetailsEntity,
        userId: Long
    ): ServiceRequestsEntity {

        var sr = commonDaoServices.createServiceRequest(s)
        try {
//            KotlinLogging.logger { }.info { "User ID:  = ${usersEntity.id}" }
            val user = usersRepo.save(updateUserFromUserEntity(userId, s))
            sr.payload = "User[id= ${user.id}]"
            sr.names = "${user.firstName} ${user.lastName}"

            val importer = importerInit(importerContactDetailsEntity, sr, user, s)
            sr.payload = "${sr.payload}: Importer[id=${importer.id}]"
//
//            val importer = importerInit(importerExporterContactDetailsEntity, importer, sr, user, s)
//            sr.payload = "${sr.payload}: ImporterExporter[id=${importerExporter.id}]"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = s.successStatus

            val variables = mutableMapOf<String, Any?>()
            variables["map"] = s
            variables["importer"] = importer
            variables["userTypeId"] = user.userTypes
            variables["user"] = user
            variables["continue"] = false

            variables["transactionRef"] = sr.transactionReference
            variables["sr"] = sr
//            runtimeService.startProcessInstanceByKey(s.bpmnProcessKey, variables)
            sr = serviceRequestsRepository.save(sr)
            runtimeService
                .createProcessInstanceBuilder()
                .processDefinitionKey(s.bpmnProcessKey)
                .businessKey(sr.transactionReference)
                .variables(variables)
                .start()

//            sendToKafkaQueue.submitAsyncRequestToBus(manufacturer, s.serviceTopic)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepository.save(sr)

        }

//        sr = serviceRequestsRepository.save(sr)
        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return sr
    }

    fun registerUser(
        s: ServiceMapsEntity,
        u: UsersEntity,
        up: UserProfilesEntity?
    ): ServiceRequestsEntity {

        var sr = commonDaoServices.createServiceRequest(s)
        try {

            var user = UserEntityDto()
            with(user) {
                id = -1
                firstName = u.firstName
                lastName = u.lastName
                email = u.email
                personalContactNumber = u.personalContactNumber
                typeOfUser = u.typeOfUser
                userPinIdNumber = u.userPinIdNumber
                userName = u.userPinIdNumber
                when {
                    up!=null -> {
                        title = u.title
                        email = u.email
                        firstName = u.firstName
                        lastName = u.lastName
                        directorate = up.confirmDirectorateId
                        designation = up.confirmDesignationId
                        department = up.confirmDepartmentId
                        division = up.confirmDivisionId
                        section = up.confirmSectionId
                        l1SubSubSection = up.confirmSubSectionL1Id
                        l2SubSubSection = up.confirmSubSectionL2Id
                        region = up.confirmRegionId
                        county = up.confirmCountyId
                        town = up.confirmTownId
                        userRegNo ="KEBS#EMP${generateRandomText(5, s.secureRandom, s.messageDigestAlgorithm, true).toUpperCase()}"
                    }
                    else -> {
                        userRegNo = "KEBS${generateRandomText(5, s.secureRandom, s.messageDigestAlgorithm, true).toUpperCase()}"
                    }
                }
            }
            user = systemsAdminDaoService.updateUserDetails(user) ?: throw NullValueNotAllowedException("Registration failed")

            sr.payload = "User[id= ${user.id}]"
            sr.names = "${user.firstName} ${user.lastName}"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = s.successStatus
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


    fun addUserManufactureProfile(
        s: ServiceMapsEntity,
        u: UsersEntity,
        cp: CompanyProfileEntity
    ): ServiceRequestsEntity {

        var sr = commonDaoServices.createServiceRequest(s)
        try {

            val userCompanyDetails = UserCompanyEntityDto()
            with(userCompanyDetails) {
                name = cp.name
                kraPin = cp.kraPin
                userId = u.id
                profileType = applicationMapProperties.mapUserRequestManufacture
                registrationNumber = cp.registrationNumber
                postalAddress = cp.postalAddress
                companyEmail = cp.companyEmail
                companyTelephone = cp.companyTelephone
                yearlyTurnover = cp.yearlyTurnover
                businessLines = cp.businessLines
                businessNatures = cp.businessNatures
                buildingName = cp.buildingName
                streetName = cp.streetName
                county = cp.county
                town = cp.town
                region = county?.let { commonDaoServices.findCountiesEntityByCountyId(it, s.activeStatus).regionId }

            }
           val userCompany = systemsAdminDaoService.updateUserCompanyDetails(userCompanyDetails) ?: throw NullValueNotAllowedException("Registration failed")

            sr.payload = "User[id= ${userCompany.userId}]"
            sr.names = "${userCompany.name} ${userCompany.kraPin}"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = s.successStatus
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

    fun addUserRequestDetails(
        s: ServiceMapsEntity,
        u: UsersEntity,
        dto: UserRequestEntityDto
    ): ServiceRequestsEntity {

        var sr = commonDaoServices.createServiceRequest(s)
        try {

           val userRequest = systemsAdminDaoService.userRequest(dto)

            sr.payload = "User Request [id= ${userRequest?.id}]"
            sr.names = "${userRequest?.userId} ${userRequest?.userRoleAssigned}"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = s.successStatus
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


    private fun manufacturerContactsInit(
        manufacturerContactsEntity: ManufacturerContactsEntity,
        sr: ServiceRequestsEntity,
        usersEntity: UsersEntity,
        manufacturer: ManufacturersEntity
    ): ManufacturerContactsEntity {
        with(manufacturerContactsEntity) {
            lastName = usersEntity.lastName
            firstName = usersEntity.firstName
            emailAddress = usersEntity.email
            status = sr.serviceMapsId?.inactiveStatus!!
            registrationDate = Date(Date().time)
            createdOn = Timestamp.from(Instant.now())
            createdBy = usersEntity.firstName + " " + usersEntity.lastName
            manufacturerId = manufacturer

        }
        KotlinLogging.logger { }.info { "My Service Request map Id:  = ${manufacturerContactsEntity.firstName}" }
        return manufacturerContactsEntity
    }

    private fun manufacturerAddressesInit(
        manufacturerAddressesEntity: ManufacturerAddressesEntity,
        manufacturer: ManufacturersEntity,
        s: ServiceMapsEntity,
        sr: ServiceRequestsEntity
    ): ManufacturerAddressesEntity {
        var add = manufacturerAddressesEntity
        with(add) {
            registrationDate = Date(Date().time)
            manufacturerId = manufacturer
            versions = s.initStatus
            createdOn = Date(Date().time)
            createdBy = sr.id

        }
        KotlinLogging.logger { }.info { "My Service Request map Id:  = ${add.registrationDate}" }
        add = manufacturerAddressesEntityRepo.save(add)
        return add
    }

    private fun manufacturerStdLevyInit(
        stdLevyNotificationFormEntity: StdLevyNotificationFormEntity,
        manufacturer: ManufacturersEntity,
        s: ServiceMapsEntity,
        sr: ServiceRequestsEntity
    ): StdLevyNotificationFormEntity {
        var add = stdLevyNotificationFormEntity
        with(add) {
            manufacturerId = manufacturer
            createdOn = Timestamp.from(Instant.now())
            createdBy = manufacturer.userId?.firstName + " " + manufacturer.userId?.lastName

        }
//        KotlinLogging.logger { }.info { "My Service Request map Id:  = ${add.registrationDate}" }
        add = stdLevyNotificationFormRepo.save(add)
        return add
    }


    private fun importerInit(
        importerContactDetailsEntity: ImporterContactDetailsEntity,
        sr: ServiceRequestsEntity,
        user: UsersEntity,
        s: ServiceMapsEntity
    ): ImporterContactDetailsEntity {
        var importerExporter = importerContactDetailsEntity
        with(importerExporter) {
            firstName = user.firstName
            lastName = user.lastName
            emailAddress = user.email
            registrationDate = Date(Date().time)
            status = sr.serviceMapsId?.activeStatus!!
            userId = user
//            version = s.initStatus
            createdOn = Date(Date().time)
            createdBy = user.firstName + " " + user.lastName
        }
        importerExporter = iImporterContactRepo.save(importerExporter)
        return importerExporter
    }


    private fun manufacturersInit(
        manufacturersEntity: ManufacturersEntity,
        sr: ServiceRequestsEntity,
        user: UsersEntity?,
        s: ServiceMapsEntity
    ): ManufacturersEntity {
        var manufacturer = manufacturersEntity
        with(manufacturer) {
//            businessLineId = manufacturersEntity.confirmLineBusinessId?.let { commonDaoServices.findBusinessLineEntityByID(it, s.activeStatus) }
//            businessNatureId = manufacturersEntity.confirmBusinessNatureId?.let { commonDaoServices.findBusinessNatureEntityByID(it,s.activeStatus) }
            registrationDate = Date(Date().time)
            status = s.activeStatus
            userId = user
            version = s.initStatus
            createdOn = Date(Date().time)
            createdBy = user?.id
        }
        manufacturer = manufacturersRepo.save(manufacturer)
        return manufacturer
    }

    private fun extractUserFromManufacturer(
        manufacturerContactsEntity: ManufacturerContactsEntity,
        s: ServiceMapsEntity,
        usertypeId: Long?
    ): UsersEntity {
        val user = UsersEntity()
        with(user) {
            firstName = manufacturerContactsEntity.firstName
            lastName = manufacturerContactsEntity.lastName
            email = manufacturerContactsEntity.emailAddress
            userName = email
            credentials = generateCredentials(s)
            confirmCredentials = user.credentials
            createdBy = "RegistrationProcess"
            createdOn = Timestamp.from(Instant.now())
            enabled = s.inactiveStatus
            accountExpired = s.activeStatus
            accountLocked = s.inactiveStatus
            credentialsExpired = s.activeStatus
            status = s.inactiveStatus
            registrationDate = Date(Date().time)
            userTypes = usertypeId

        }
        return user
    }

    private fun updateUserFromUserEntity(userId: Long, s: ServiceMapsEntity): UsersEntity {
        var user = UsersEntity()
        usersRepo.findByIdOrNull(userId)
            ?.let { usersEntity ->

//                    KotlinLogging.logger { }.info { "User ID before update:  = ${usersEntity.id}" }
                usersEntity.userProfileStatus = s.activeStatus
                usersEntity.modifiedBy = usersEntity.firstName + " " + usersEntity.lastName
                usersEntity.modifiedOn = Timestamp.from(Instant.now())

                user = usersEntity

            }
        return user
    }

    private fun extractUserFromUserEntity(
        usersEntity: UsersEntity,
        s: ServiceMapsEntity,
        usertypeId: Long?
    ): UsersEntity {
        with(usersEntity) {
            userName = email
            credentials = generateCredentials(s)
            confirmCredentials = usersEntity.credentials
            createdBy = "RegistrationProcess"
            createdOn = Timestamp.from(Instant.now())
            enabled = s.inactiveStatus
            userProfileStatus = s.inactiveStatus
            accountExpired = s.activeStatus
            accountLocked = s.inactiveStatus
            credentialsExpired = s.activeStatus
            status = s.inactiveStatus
            registrationDate = Date(Date().time)
            userTypes = usertypeId
            userProfileStatus = s.inactiveStatus
            userUuid = commonDaoServices.generateUUIDString()

        }
        return usersEntity
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun registerEmployee(
        s: ServiceMapsEntity,
        usersEntity: UsersEntity,
        userProfilesEntity: UserProfilesEntity,
        businessKey: String? = null
    ): ServiceRequestsEntity {


        var sr = commonDaoServices.createServiceRequest(s)

        try {
            var user =
                usersRepo.save(extractUserFromUserEntity(usersEntity, s, applicationMapProperties.mapUserTypeEmployee))
            sr.payload = "User[id= ${user.id}]"
            sr.names = "${user.firstName} ${user.lastName}"

            val userProfile = userProfilesRepo.save(userProfilesInit(userProfilesEntity, user, s))
            sr.payload = "${sr.payload}: UserProfile[id=${userProfile.id}]"

//            val employee = employeesInit(userProfile, user, s)
//            sr.payload = "${sr.payload}: Employee[id=${employee.id}]"

//            user = usersRepo.save(userUpdateRole(userProfile, user, s))
            sr.payload = "User[id= ${user.id}]"
            sr.names = "${user.firstName} ${user.lastName}"

//<<<<<<< HEAD
//            val userType: UserTypesEntity = findUserTypeByID(employeeUserTypeID.toInt())

//            val assignmentsEntity = userRolesAssignmentsRepository.save(userRoleAssignment(user, s))
//            sr.payload = "${sr.payload}: Default Role Assigned[id=${assignmentsEntity.id}]"
//=======
////            val assignmentsEntity = userRolesAssignmentsRepository.save(userRoleAssignmentUpdate(user, s))
////            sr.payload = "${sr.payload}: Default Role Assigned[id=${assignmentsEntity.id}]"
//>>>>>>> origin/feature/staging/standards-levy/uat/user-journey-dev-v2


            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = s.successStatus

            val variables = mutableMapOf<String, Any?>()
            variables["map"] = s
            variables["user"] = user
            variables["userProfile"] = userProfile
//            variables["employee"] = employee
            variables["userTypeId"] = user.userTypes
            variables["transactionRef"] = sr.transactionReference
            variables["sr"] = sr
            variables["continue"] = false

//            runtimeService.startProcessInstanceByKey(s.bpmnProcessKey, variables)
            sr = serviceRequestsRepository.save(sr)
            runtimeService
                .createProcessInstanceBuilder()
                .processDefinitionKey(s.bpmnProcessKey)
                .businessKey(sr.transactionReference)
                .variables(variables)
                .start()

//            sendToKafkaQueue.submitAsyncRequestToBus(manufacturer, s.serviceTopic)
            sr.processingEndDate = Timestamp.from(Instant.now())


        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.trace(e.message, e)
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepository.save(sr)
        }

        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return sr


    }

    private fun userProfilesInit(
        userProfilesEntity: UserProfilesEntity,
        user: UsersEntity,
        s: ServiceMapsEntity
    ): UserProfilesEntity {
        var userProfile = userProfilesEntity
        with(userProfile) {
            id = null
            userId = user
            userProfileUuid = commonDaoServices.generateUUIDString()
            directorateId = assignDirectorate(userProfilesEntity.confirmDirectorateId)
            departmentId = assignDepartment(userProfilesEntity.confirmDepartmentId)
            regionId = assignRegion(userProfilesEntity.confirmRegionId)
            countyID = assignCounty(userProfilesEntity.confirmCountyId)
            townID = assignTown(userProfilesEntity.confirmTownId)
            divisionId = assignDivision(userProfilesEntity.confirmDivisionId)
            designationId = assignDesignation(userProfilesEntity.confirmDesignationId)
            subRegionId = assignSubRegion(userProfilesEntity.confirmSubRegionId)
            sectionId = assignSection(userProfilesEntity.confirmSectionId)
            subSectionL1Id = assignSubSectionL1(userProfilesEntity.confirmSubSectionL1Id)
            subSectionL2Id = assignSubSectionL2(userProfilesEntity.confirmSubSectionL2Id)
//            when {
//                userProfilesEntity.confirmSectionId != null -> {
//                    sectionId = assignSection(userProfilesEntity.confirmSectionId)
//                    KotlinLogging.logger { }.info { "MY sectionId:  = ${sectionId.toString()}" }
//                }
//            }
//            if( userProfilesEntity.confirmSubSectionL1Id != null) {
//                subSectionL1Id = assignSubSectionL1(userProfilesEntity.confirmSubSectionL1Id)
//                KotlinLogging.logger { }.info { "MY subSectionL1Id:  = ${subSectionL1Id.toString()}" }
//            }
            createdBy = user.userName
            createdOn = Timestamp.from(Instant.now())
            status = s.activeStatus
        }
        userProfile = userProfilesRepo.save(userProfile)
        KotlinLogging.logger { }.info { "MY userProfile:  = $userProfile" }
        return userProfile
    }

    private fun userUpdateRole(
        userProfilesEntity: UserProfilesEntity,
        user: UsersEntity,
        s: ServiceMapsEntity
    ): UsersEntity {

        userProfilesEntity.designationId?.let {
            userRolesRepo.findByDesignationIdAndStatus(it.id, s.activeStatus)
                ?.let {
//                        userTypesRepo.findByDefaultRole(userRolesEntity)
//                                ?.let { userType ->
                    with(user) {
//                            roleId = userRolesEntity
//                            userTypes = userType
//<<<<<<< HEAD
                        KotlinLogging.logger { }.info { "MY USER ROLE UPDATED:  = ${user.userName}" }
                        return user
                    }
//=======
//                        return user
//                    }
//>>>>>>> origin/feature/staging/standards-levy/uat/user-journey-dev-v2
//                                }
//                                ?: throw Exception("Missing Role with the following [id=$userRolesEntity.id], recheck configuration Your UserType")

                }
                ?: throw Exception("Missing Role with The Designation Entity with the following [id=$userProfilesEntity.designationId], recheck configuration")

        }
            ?: throw Exception("Missing Designation Entity with the following [id=$userProfilesEntity.designationId], recheck configuration")

    }

    private fun employeesInit(
        userProfilesEntity: UserProfilesEntity,
        user: UsersEntity,
        s: ServiceMapsEntity
    ): EmployeesEntity {
        var employee = EmployeesEntity()
        with(employee) {
            id = null
            department = userProfilesEntity.departmentId?.department
            userId = user
            createdBy = user.id
            createdOn = Timestamp.from(Instant.now())
            status = s.inactiveStatus
        }
        employee = employeesRepo.save(employee)
        return employee
    }

    private fun yearlyTurnoverInit(
        yearlyTurnoverEntity: ManufacturePaymentDetailsEntity,
        manufacturer: ManufacturersEntity,
        s: ServiceMapsEntity,
        sr: ServiceRequestsEntity
    ): ManufacturePaymentDetailsEntity {
        var turnover = yearlyTurnoverEntity
        with(turnover) {
            manufacturerId = manufacturer.id
            createdBy = sr.transactionReference
            createdOn = Timestamp.from(Instant.now())
            status = s.activeStatus
        }
        turnover = yearlyTurnoverRepo.save(turnover)
        return turnover
    }

    private fun kraInit(
        manufacturer: ManufacturersEntity,
        standardLevyPayments: StandardLevyPaymentsEntity,
        turnover: ManufacturePaymentDetailsEntity,
        sr: ServiceRequestsEntity,
        s: ServiceMapsEntity
    ): StandardLevyPaymentsEntity {
        var kra = standardLevyPayments
        with(kra) {
            manufacturerEntity = manufacturer
            createdBy = sr.transactionReference
            createdOn = Timestamp.from(Instant.now())
            status = s.activeStatus
            paymentAmount = calculateLevyPayable((turnover))
            levyPayable = calculateLevyPayable(turnover)
        }
        kra = standardLevyPaymentsRepository.save(kra)
        return kra
    }

    private fun calculateLevyPayable(turnover: ManufacturePaymentDetailsEntity): BigDecimal {
        var amout: BigDecimal? = null
        val turnoverAmount = turnover.turnOverAmount
        amout = if (turnoverAmount!! < 200000.toBigDecimal()) {
            0.0.toBigDecimal()
        } else {
            0.2.toBigDecimal().times(turnoverAmount)
        }
        return amout
    }

    private fun extractUsersFromEmployee(
        usersEntity: UsersEntity,
        s: ServiceMapsEntity,
        userTypeName: String
    ): UsersEntity {
        var user = usersEntity
        with(user) {
            id = null
            userName = email
            credentials = generateCredentials(s)
            confirmCredentials = user.credentials
            createdBy = userName
            createdOn = Timestamp.from(Instant.now())
            enabled = s.inactiveStatus
            accountExpired = s.activeStatus
            accountLocked = s.inactiveStatus
            credentialsExpired = s.activeStatus
            status = s.inactiveStatus
            registrationDate = Date(Date().time)
            userTypes = assignUserTypeByTypeName(userTypeName, s)?.id
        }

        user = usersRepo.save(user)
        return user
    }

    fun completeTask(processInstance: ProcessInstance, sr: ServiceRequestsEntity): ServiceRequestsEntity {
        taskService.createTaskQuery().processInstanceId(processInstance.processInstanceId).singleResult()
            ?.let { task ->

                taskService.complete(task.id)
            }
        return sr

    }


    fun processInstance(sr: ServiceRequestsEntity): ProcessInstance? =
        runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(sr.transactionReference).singleResult()

    fun generateVerificationToken(
        sr: ServiceRequestsEntity,
        user: UsersEntity,
        map: ServiceMapsEntity
    ): UserVerificationTokensEntity? {
        var tokensEntity = UserVerificationTokensEntity()
        with(tokensEntity) {
            token = sr.transactionReference
            userId = user
            status = map.initStatus
            createdBy = sr.transactionReference
            createdOn = Timestamp.from(Instant.now())
            map.tokenExpiryHours?.let { h -> tokenExpiryDate = Timestamp.from(Instant.now().plus(h, ChronoUnit.HOURS)) }
                ?: throw Exception("Missing Configuration: Hours to Token Expiry")
            transactionDate = Date(Date().time)
        }

        tokensEntity = verificationTokensRepo.save(tokensEntity)
        return tokensEntity
    }

    /**
     * Check BRS
     */
    fun checkBrs(sr: ServiceRequestsEntity, user: UsersEntity, map: ServiceMapsEntity): Boolean {
        val variables = mutableMapOf<String, Any?>()
        variables["continue"] = true
        return true
    }


    fun submitKraToQueue(map: ServiceMapsEntity, user: UsersEntity, sr: ServiceRequestsEntity): Int? {
//        return try {
//            runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(sr.transactionReference).singleResult()
//                    ?.let { proc ->
//                        val task: Task = taskService.createTaskQuery().processInstanceId(proc.processInstanceId).singleResult()
//
//                        val variables: MutableMap<String, Any> = taskService.getVariables(task.id)
//                        variables["continue"] = true
//                        sr.status
//                    }
//                    ?: throw ProcessInstanceNotFoundException("No process instance with [businessKey=${sr.transactionReference}]")
//        } catch (e: java.lang.Exception) {
//            KotlinLogging.logger { }.error(e.message)
//            KotlinLogging.logger { }.trace(e.message, e)
//            sr.status = sr.serviceMapsId?.exceptionStatus
//            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
//            sr.responseMessage = "${e.message}| ${sr.responseStatus}"
//            serviceRequestsRepository.save(sr)
//
//            sr.status
//        }
        val variables = mutableMapOf<String, Any?>()
        variables["continue"] = true
        return sr.status
    }

    fun submitBrsToQueue(map: ServiceMapsEntity, user: UsersEntity, sr: ServiceRequestsEntity): Int? {
        return try {
            runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(sr.transactionReference)
                .singleResult()
                ?.let { proc ->
                    val task: Task =
                        taskService.createTaskQuery().processInstanceId(proc.processInstanceId).singleResult()

                    val variables: MutableMap<String, Any> = taskService.getVariables(task.id)
                    variables["continue"] = true
                    KotlinLogging.logger { }.info { "Reached here" }
                    sr.status
                }
//                    ?:throw NullValueNotAllowedException("No Notification configured for  combination [SrStatus=${sr.status}, mapId=${map.id}] ")
        } catch (e: java.lang.Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.trace(e.message, e)
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = "${e.message}| ${sr.responseStatus}"
            serviceRequestsRepository.save(sr)

            sr.status
        }
//        val variables = mutableMapOf<String, Any?>()
//        variables["continue"] = true
//        return sr.status
    }

    fun submitEmailToQueue(map: ServiceMapsEntity, user: UsersEntity, sr: ServiceRequestsEntity): Int? {
        return try {

            KotlinLogging.logger { }.info { "Started Mail process" }
            notificationsUseCase(map, mutableListOf(user.email), sr, sr)
                ?.let { list ->
                    list.forEach { buffer ->
                        /**
                         * TODO: Make topic a field on the Buffer table
                         */
                        buffer.recipient?.let { recipient ->
                            KotlinLogging.logger { }.info { "Started recipient $recipient" }
                            buffer.subject?.let { subject ->
                                KotlinLogging.logger { }.info { "Started subject $subject" }
                                buffer.messageBody?.let { messageBody ->
                                    KotlinLogging.logger { }.info { "Started messageBody $messageBody" }
                                    notifications.sendEmail(recipient, subject, messageBody)
                                    KotlinLogging.logger { }.info { "Email sent" }
                                }
                            }
                        }


//                            buffer.varField1?.let { topic ->
//                                sendToKafkaQueue.submitAsyncRequestToBus(buffer, topic)
//                            }
//                            KotlinLogging.logger { }.info { "Email sent" }

                    }
                    serviceRequestsRepository.save(sr)


//                        val task = taskService.createTaskQuery().processInstanceId(proc.processInstanceId).singleResult()
//                        val variables: MutableMap<String, Any> = taskService.getVariables(task.id)
//                        variables["continue"] = true
//                        variables["sr"] = sr
//                        variables["user"] = user
//                        taskService.complete(task.id, variables)
                    sr.status

                }
                ?: throw NullValueNotAllowedException("No Notification configured for  combination [SrStatus=${sr.status}, mapId=${map.id}] ")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.trace(e.message, e)
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = "${e.message}| ${sr.responseStatus}"
            serviceRequestsRepository.save(sr)
//            val task = taskService.createTaskQuery().processInstanceId(proc.processInstanceId).singleResult()
//            val variables = mutableMapOf<String, Any?>()
//            variables["continue"] = false
//            variables["sr"] = sr
//            taskService.complete(task.id, variables)
            sr.status

        }
    }

    fun createTransactionLog(sr: ServiceRequestsEntity, map: ServiceMapsEntity): WorkflowTransactionsEntity {
        val log = WorkflowTransactionsEntity()
        with(log) {
            transactionDate = Date(Date().time)
            transactionStartDate = Timestamp.from(Instant.now())
            serviceRequests = sr
            retried = 0
            transactionStatus = map.initStatus
            createdBy = "${sr.id}_${sr.transactionReference}"
            transactionReference = "${sr.transactionReference}_${sr.currentStage}"
            createdOn = Timestamp.from(Instant.now())

        }
        return log
    }


//    fun createServiceRequest(s: ServiceMapsEntity): ServiceRequestsEntity {
//        var serviceRequests = ServiceRequestsEntity()
//dvbnfnb
//        with(serviceRequests) {
//            serviceMapsId = s
//            processingStartDate = Timestamp.from(Instant.now())
//            transactionReference = commonDaoServices.generateTransactionReference(s)
//            requestDate = Date(Date().time)
//            createdBy = transactionReference
//            createdOn = Timestamp.from(Instant.now())
//            eventBusSubmitDate = Timestamp.from(Instant.now())
//            try {
//                requestId = s.id.toLong()
//            } catch (e: Exception) {
//                0L
//            }
//
//
//        }
//        serviceRequests = serviceRequestsRepository.save(serviceRequests)
//
//        return serviceRequests
//    }


    fun notificationsUseCase(
        map: ServiceMapsEntity,
        email: MutableList<String?>,
        data: Any?,
        sr: ServiceRequestsEntity? = null
    ): List<NotificationsBufferEntity>? {
        notificationsRepo.findByServiceMapIdAndServiceRequestStatusAndStatus(map, sr?.status, map.activeStatus)
            ?.let { notifications ->
                return commonDaoServices.generateBufferedNotification(notifications, map, email, data, sr)
            }
            ?: throw MissingConfigurationException("Notification for current Scenario is missing, review setup and try again later")

    }


    fun extractServiceMapFromAppId(mapId: String): ServiceMapsEntity? =
        serviceMapsRepository.findByIdOrNull(mapId.toIntOrNull())


    fun extractUserFromEmail(email: String): UsersEntity? = usersRepo.findByEmail(email)


    fun extractUserFromValidationToken(transactionReference: String?, map: ServiceMapsEntity): UsersEntity? =
        verificationTokensRepo.findByTokenAndStatus(transactionReference, map.successStatus)?.userId

    fun findUserById(userId: String): UsersEntity? = usersRepo.findByIdOrNull(userId.toLongOrNull())

    fun findUserTypeByID(id: Long): UserTypesEntity {
        userTypesRepo.findByIdOrNull(id)
            ?.let { userType ->
                return userType
            }
            ?: throw ExpectedDataNotFound("User type with ID  = ${id}, does not Exist")
    }


    fun valVerificatToken(sr: ServiceRequestsEntity, token: String): Int {
        var log: WorkflowTransactionsEntity? = null

        try {
//            runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(sr.transactionReference).singleResult()
//                    ?.let { proc ->
//                        val task: Task = taskService.createTaskQuery().processInstanceId(proc.processInstanceId).singleResult()

//                        val variables: MutableMap<String, Any> = taskService.getVariables(task.id)
//                        variables["continue"] = false


//
            sr.serviceMapsId
                ?.let { map ->
                    sr.payload = token

                    log = createTransactionLog(sr, map)


                    verificationTokensRepo.findByTokenAndStatus(token, map.initStatus)
                        ?.let { verificationToken ->
                            log?.integrationResponse = "${verificationToken.id}"
                            verificationToken.tokenExpiryDate
                                ?.let { expiry ->
                                    when {
                                        expiry.after(Timestamp.from(Instant.now())) -> {
                                            /**
                                             * If user exists activate and enable
                                             */
                                            verificationToken.userId
                                                ?.let {
                                                    activateUser(it, map, sr)
//                                                                                variables["continue"] = true

                                                }

                                            verificationToken.status = map.successStatus
                                            verificationToken.lastModifiedOn = Timestamp.from(Instant.now())
                                            verificationToken.lastModifiedBy = "Verification Token Received"
                                            verificationTokensRepo.save(verificationToken)

                                        }
                                        else -> {
                                            verificationToken.status = map.failedStatus
                                            verificationToken.lastModifiedOn = Timestamp.from(Instant.now())
                                            verificationToken.lastModifiedBy = "Expired Verification Token Received"
                                            verificationTokensRepo.save(verificationToken)
                                            throw Exception("Expired Verification Token Received")
                                        }
                                    }

                                }
                                ?: throw Exception("Verification Token without a valid expiry found")


                        } ?: throw Exception("Verification Token not found")




                    sr.responseStatus = sr.serviceMapsId?.successStatusCode
                    sr.responseMessage = "Success ${sr.payload}"
                    sr.status = map.successStatus
                    sr.processingEndDate = Timestamp.from(Instant.now())

                    log?.responseMessage = "Token generation successful"
                    log?.responseStatus = map.successStatusCode
                    log?.transactionStatus = map.successStatus

                }
//                        taskService.complete(task.id, variables)


//                    }
//                    ?: throw ProcessInstanceNotFoundException("No process instance with [businessKey=${sr.transactionReference}]")


        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            log?.responseMessage = e.message
            log?.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            log?.transactionStatus = sr.serviceMapsId?.exceptionStatus!!
        }
        log?.transactionCompletedDate = Timestamp.from(Instant.now())

        try {
            serviceRequestsRepository.save(sr)
            log?.let { log = workflowTransactionsRepository.save(it) }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error { e }

        }
        return log?.transactionStatus ?: 25

    }


    fun findNotificationByToken(
        token: String?,
        appId: String?,
        emails: List<String>,
        user: UsersEntity
    ): List<NotificationsBufferEntity>? {
        token
            ?.let { ref ->
                serviceRequestsRepository.findFirstByTransactionReference(ref)
                    ?.let { sr ->
                        sr.status = 40
                        sr.eventBusSubmitDate = Timestamp.from(Instant.now())
                        sr.requestId = user.id
                        serviceRequestsRepository.save(sr)
                        serviceMapsRepository.findByIdOrNull(appId?.toIntOrNull())
                            ?.let { map ->
                                return notificationsUseCase(map, emails.toMutableList(), sr, sr)

                            }
                            ?: throw ServiceMapNotFoundException("Missing application mapping for [id=$appId], recheck configuration")


                    }
                    ?: throw NullValueNotAllowedException("No matching service request for token")

            }
            ?: throw NullValueNotAllowedException("Invalid token received")


    }

}