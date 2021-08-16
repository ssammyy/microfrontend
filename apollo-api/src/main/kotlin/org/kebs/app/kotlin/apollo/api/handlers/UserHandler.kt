package org.kebs.app.kotlin.apollo.api.handlers

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.adaptor.kafka.producer.service.SendToKafkaQueue
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
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
        private val countyRepo: ICountiesRepository,
        private val masterDataDaoService: MasterDataDaoService,
        private val businessLinesRepository: IBusinessLinesRepository,
        private val commonDaoServices: CommonDaoServices,
        private val qaDaoServices: QADaoServices,
        private val applicationMapProperties: ApplicationMapProperties,
        private val businessNatureRepository: IBusinessNatureRepository
) {

    final val appId: Int = applicationMapProperties.mapUserRegistration

    private val usersNotificationListPage = "auth/user-notifications"
    private val userProfilePage = "auth/user-profile"

    fun notificationList(req: ServerRequest): ServerResponse {
        val response=ApiResponseModel()
        commonDaoServices.loggedInUserDetails()
                .let { userDetails ->
                    userDetails.email?.let {
                        response.data=commonDaoServices.findAllUserNotification(it)
                        response.message="Success"
                        response.responseCode=ResponseCodes.SUCCESS_CODE
                    }?:run {
                        response.message="Email not configured"
                        response.responseCode=ResponseCodes.NOT_FOUND
                    }
                }
        return ok().body(response)
    }

    fun userProfile(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        response.responseCode = ResponseCodes.SUCCESS_CODE
        val map = commonDaoServices.serviceMapDetails(appId)
        val userDetails = commonDaoServices.loggedInUserDetails()
        val auth = commonDaoServices.loggedInUserAuthentication()
        val dataMap = mutableMapOf<String, Any?>()
        when (userDetails.manufactureProfile) {
            map.activeStatus -> {
                userDetails.id?.let {
                    val manufactureProfile = commonDaoServices.findCompanyProfile(it)
                    dataMap.put("manufacture_profile", manufactureProfile)
                    dataMap.put("directors_details", commonDaoServices.companyDirectorList(manufactureProfile.id!!))
                    dataMap.put("businessLineValue", manufactureProfile.businessLines?.let { businessLinesRepository.findByIdOrNull(it)?.name })
                    dataMap.put("business_nature_value", manufactureProfile.businessNatures?.let { businessNatureRepository.findByIdOrNull(it) })
                    dataMap.put("plants_details", qaDaoServices.findAllPlantDetails(userDetails.id!!))
                    dataMap.put("contract_undertaken_details", commonDaoServices.findAllContractsUnderTakenDetails(manufactureProfile.id!!))
                    dataMap.put("commodity_details", commonDaoServices.findAllCommoditiesDetails(manufactureProfile.id!!))
                    dataMap.put("region_value", manufactureProfile.region?.let { commonDaoServices.findRegionEntityByRegionID(it, map.activeStatus).region })
                    dataMap.put("county_value", manufactureProfile.county?.let { commonDaoServices.findCountiesEntityByCountyId(it, map.activeStatus).county })
                    dataMap.put("town_value", manufactureProfile.town?.let { commonDaoServices.findTownEntityByTownId(it).town })
                    response.responseCode = ResponseCodes.SUCCESS_CODE
                    response.message = "Success"
                } ?: run {
                    response.responseCode = ResponseCodes.NOT_FOUND
                    response.message = "Missing Manufacture Company Details, Fill The Details"
                }
            }
        }

        if (auth.authorities.stream().anyMatch { authority -> authority.authority == applicationMapProperties.mapQualityAssuranceEmployeeRoleName }) {
            val employeeProfiles = commonDaoServices.findUserProfileByUserID(userDetails, map.activeStatus)
            dataMap.put("employee_profiles", employeeProfiles)
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Success"
        }
        if (response.responseCode == ResponseCodes.SUCCESS_CODE) {
            dataMap.put("counties", countyRepo.findByStatusOrderByCounty(map.activeStatus))
            dataMap.put("user_requests", masterDataDaoService.getUserRequestTypesByStatus(map.activeStatus))
            dataMap.put("users_details", userDetails)
            dataMap.put("manufacture_business_id", applicationMapProperties.mapUserTypeManufactureID)
            dataMap.put("contracts_business_id", applicationMapProperties.mapUserTypeContractorID)
            dataMap.put("business_lines", businessLinesRepository.findByStatus(map.activeStatus))
            response.message = "Success"
        }
        response.data = dataMap
        return ok().body(response)
    }


}