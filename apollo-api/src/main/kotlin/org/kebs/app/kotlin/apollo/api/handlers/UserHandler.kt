package org.kebs.app.kotlin.apollo.api.handlers

import org.kebs.app.kotlin.apollo.adaptor.kafka.producer.service.SendToKafkaQueue
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.*
import org.kebs.app.kotlin.apollo.common.dto.UserRequestEntityDto
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.NotificationsBufferEntity
import org.kebs.app.kotlin.apollo.store.model.qa.ManufacturePlantDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.registration.CompanyProfileCommoditiesManufactureEntity
import org.kebs.app.kotlin.apollo.store.model.registration.CompanyProfileContractsUndertakenEntity
import org.kebs.app.kotlin.apollo.store.model.registration.CompanyProfileEntity
import org.kebs.app.kotlin.apollo.store.repo.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.ServerResponse.ok
import org.springframework.web.servlet.function.paramOrNull


@Service
class UserHandler(
    private val daoService: SystemsAdminDaoService,
    private val sendToKafkaQueue: SendToKafkaQueue,
    private val countriesRepository: ICountriesRepository,
    private val serviceMapsRepository: IServiceMapsRepository,
    private val countyRepo: ICountiesRepository,
    private val companyProfileDirectorsRepo: ICompanyProfileDirectorsRepository,
    private val systemsAdminDaoService: SystemsAdminDaoService,
    private val masterDataDaoService: MasterDataDaoService,
    private val businessLinesRepository: IBusinessLinesRepository,
    private val manufacturePlantRepository: IManufacturePlantDetailsRepository,
    private val commonDaoServices: CommonDaoServices,
    private val qaDaoServices: QADaoServices,
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

    fun userProfile(req: ServerRequest): ServerResponse {

                        val map = commonDaoServices.serviceMapDetails(appId)
                        val userDetails = commonDaoServices.loggedInUserDetails()
                        val auth = commonDaoServices.loggedInUserAuthentication()
//        if (auth.authorities.stream().anyMatch { authority -> authority.authority == applicationMapProperties.mapQualityAssuranceManufactureRoleName }) {
            when (userDetails.manufactureProfile) {
                map.activeStatus -> {
                    val manufactureProfile= userDetails.id?.let { commonDaoServices.findCompanyProfile(it) } ?: throw ExpectedDataNotFound("Missing Manufacture Company Details, Fill The Details")
                    req.attributes()["manufactureProfile"] = manufactureProfile
                    req.attributes()["directorsDetails"] = companyProfileDirectorsRepo.findByCompanyProfileId(manufactureProfile.id?: throw ExpectedDataNotFound("CompanyProfile ID Not Found"))
                    req.attributes()["businessLineValue"] = manufactureProfile.businessLines?.let {  businessLinesRepository.findByIdOrNull(it)?.name}
                    req.attributes()["businessNatureValue"] =manufactureProfile.businessNatures?.let {  businessNatureRepository.findByIdOrNull(it)?.name}
    //                                        req.attributes()["userClassificationValue"] = null
                    req.attributes()["plantsDetails"] = qaDaoServices.findAllPlantDetails(userDetails.id!!)
                    req.attributes()["contractUndertakenDetails"] = commonDaoServices.findAllContractsUnderTakenDetails(manufactureProfile.id?: throw ExpectedDataNotFound("CompanyProfile ID Not Found"))
                    req.attributes()["commodityDetails"] = commonDaoServices.findAllCommoditiesDetails(manufactureProfile.id?: throw ExpectedDataNotFound("CompanyProfile ID Not Found"))
                    req.attributes()["regionValue"] = manufactureProfile.region?.let { commonDaoServices.findRegionEntityByRegionID(it, map.activeStatus).region }
                    req.attributes()["countyValue"] = manufactureProfile.county?.let { commonDaoServices.findCountiesEntityByCountyId(it, map.activeStatus).county }
                    req.attributes()["townValue"] = manufactureProfile.town?.let { commonDaoServices.findTownEntityByTownId(it).town }
                    req.attributes()["manufacturePlantDetails"] = ManufacturePlantDetailsEntity()

                }
            }
//        }
       if (auth.authorities.stream().anyMatch { authority -> authority.authority == applicationMapProperties.mapQualityAssuranceEmployeeRoleName }) {

            val employeeProfiles= commonDaoServices.findUserProfileByUserID(userDetails, map.activeStatus)
            req.attributes()["employeeProfiles"] = employeeProfiles

        }

                        req.attributes()["counties"] = countyRepo.findByStatusOrderByCounty(map.activeStatus)
                        req.attributes()["userRequests"] = masterDataDaoService.getUserRequestTypesByStatus(map.activeStatus)
                        req.attributes()["userRequestEntityDto"] = UserRequestEntityDto()
                        req.attributes()["usersEntity"] =userDetails
//                        req.attributes()["profile"] = profile
                        req.attributes()["companyProfileEntity"] = CompanyProfileEntity()
                        req.attributes()["CompanyProfileCommoditiesManufactureEntity"] = CompanyProfileCommoditiesManufactureEntity()
                        req.attributes()["CompanyProfileContractsUndertakenEntity"] = CompanyProfileContractsUndertakenEntity()
                        req.attributes()["businessLines"] =  businessLinesRepository.findByStatus(map.activeStatus)
                        return ok().render(userProfilePage, req.attributes())
    }


    }