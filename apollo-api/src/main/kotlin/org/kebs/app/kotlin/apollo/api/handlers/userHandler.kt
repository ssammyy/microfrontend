package org.kebs.app.kotlin.apollo.api.handlers

import org.kebs.app.kotlin.apollo.adaptor.kafka.producer.service.SendToKafkaQueue
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.RegistrationDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.SystemsAdminDaoService
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.NotificationsBufferEntity
import org.kebs.app.kotlin.apollo.store.model.qa.ManufacturePlantDetailsEntity
import org.kebs.app.kotlin.apollo.store.repo.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.ServerResponse.ok
import org.springframework.web.servlet.function.paramOrNull


@Service
class userHandler(
        private val daoService: SystemsAdminDaoService,
        private val sendToKafkaQueue: SendToKafkaQueue,
        private val countriesRepository: ICountriesRepository,
        private val serviceMapsRepository: IServiceMapsRepository,
        private val countyRepo: ICountiesRepository,
        private val businessLinesRepository: IBusinessLinesRepository,
        private val manufacturePlantRepository: IManufacturePlantDetailsRepository,
        private val commonDaoServices: CommonDaoServices,
        private val applicationMapProperties: ApplicationMapProperties,
        private val businessNatureRepository: IBusinessNatureRepository,
        private val contactTypesRepository: IContactTypesRepository,
        private val userTypesRepo: IUserTypesEntityRepository,
        private val SubSectionsLevel1Repo: ISubSectionsLevel1Repository,
        private val SubSectionsLevel2Repo: ISubSectionsLevel2Repository,
        private val titlesRepository: ITitlesRepository,
        private val divisionsRepo: IDivisionsRepository,
        private val departmentsRepo: IDepartmentsRepository,
        private val directorateRepo: IDirectoratesRepository,
        private val designationRepository: IDesignationsRepository,
        private val sectionsRepo: ISectionsRepository,
        private val subRegionsRepository: ISubRegionsRepository,
        private val regionsRepository: IRegionsRepository,
        private val usersRepo: IUserRepository,
        private val userProfilesRepo: IUserProfilesRepository,
        private val userVerificationTokensRepository: IUserVerificationTokensRepository,
        private val daoServices: RegistrationDaoServices
) {

    final val appId: Int = applicationMapProperties.mapUserRegistration

    private final val usersNotificationListPage = "auth/user-notifications"
    private final val userProfilePage = "auth/user-profile"

    fun notificationList(req: ServerRequest): ServerResponse =
            commonDaoServices.loggedInUserDetails()
                    .let { userDetails ->
                        req.attributes()["notifications"] = userDetails.email?.let { commonDaoServices.findAllUserNotification(it) }
                        req.attributes()["notificationBuffer"] = NotificationsBufferEntity()
                        return ok().render(usersNotificationListPage, req.attributes())
                    }

    fun userProfile(req: ServerRequest): ServerResponse =

            req.paramOrNull("userName")
                    ?.let { userName ->
                        val map = commonDaoServices.serviceMapDetails(appId)
                        val userDetails = commonDaoServices.findUserByUserName(userName)
                        var profile :String? = null
                        when (userDetails.userTypes) {
                            applicationMapProperties.mapUserTypeEmployee -> {
                                profile = applicationMapProperties.mapUserTypeNameEmployee
                                val myUserProfile = commonDaoServices.findUserProfileByUserID(userDetails, map.activeStatus)
                                req.attributes()["userProfilesEntity"] = myUserProfile
                                req.attributes()["myUserProfile"] = myUserProfile
                            }
                            applicationMapProperties.mapUserTypeManufacture -> {
                                profile = applicationMapProperties.mapUserTypeNameManufacture
                                req.attributes()["manufacturerContactEntity"] = commonDaoServices.findManufacturerContactDetailsByManufacturerProfile(userDetails, map.activeStatus)
                                req.attributes()["manufacturePlantDetailsEntity"] = ManufacturePlantDetailsEntity()

                                val manufacture = commonDaoServices.findManufacturerProfileByUserID(userDetails, map.activeStatus)
                                req.attributes()["manufacturePlants"] = manufacturePlantRepository.findByManufactureId(manufacture.id)
                                req.attributes()["manufacturersEntity"] = manufacture
                                req.attributes()["businessLineValue"] = businessLinesRepository.findByIdOrNull(manufacture.businessLineId)?.name
                                req.attributes()["businessNatureValue"] = businessNatureRepository.findByIdOrNull(manufacture.businessNatureId)?.name
                            }
                            applicationMapProperties.mapUserTypeImporter -> {
                                profile = applicationMapProperties.mapUserTypeNameImporter
                                req.attributes()["importerEntity"] = commonDaoServices.findImporterProfileByUserID(userDetails, map.activeStatus)
                            }
                        }

                        req.attributes()["counties"] = countyRepo.findByStatusOrderByCounty(map.activeStatus)
                        req.attributes()["usersEntity"] =userDetails
                        req.attributes()["profile"] = profile
                        return ok().render(userProfilePage, req.attributes())
                    }
                    ?: throw ExpectedDataNotFound("Missing username, recheck configuration")

    }