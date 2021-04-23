package org.kebs.app.kotlin.apollo.api.ports.provided.dao

import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.qa.PermitApplicationsEntity
import org.kebs.app.kotlin.apollo.store.model.qa.PermitTypesEntity
import org.kebs.app.kotlin.apollo.store.repo.qa.IPermitTypesEntityRepository
import org.kebs.app.kotlin.apollo.store.repo.qa.IPermitApplicationsRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class QADaoServices(
    private val applicationMapProperties: ApplicationMapProperties,
    private val commonDaoServices: CommonDaoServices,
    private val permitRepo: IPermitApplicationsRepository,
    private val permitTypesRepo: IPermitTypesEntityRepository
) {

    final var appId = applicationMapProperties.mapQualityAssurance

    val permitList =  "redirect:/api/qa/permits-list?permitTypeID"

    fun findPermitTypesList(status: Int): List<PermitTypesEntity> {
        permitTypesRepo.findByStatus(status)?.let {
                return it
            } ?: throw ExpectedDataNotFound("No Permit Type List found")
    }

    fun findPermitType(id: Long): PermitTypesEntity {
        permitTypesRepo.findByIdOrNull(id)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Permit Type found with the following [ID=$id]")
    }

    fun findAllUserPermitWithPermitType(user: UsersEntity, permitType: Long): List<PermitApplicationsEntity> {
        val userId = user.id ?: throw ExpectedDataNotFound("No ID Found")
            permitRepo.findByUserIdAndPermitType(userId, permitType)
                ?.let { permitList ->
                    return permitList
                }

            ?: throw ExpectedDataNotFound("No Permit Found for the following user with USERNAME = ${user.userName}")
    }

    fun findPermitBYID(id: Long): PermitApplicationsEntity {
        permitRepo.findByIdOrNull(id)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Permit Type found with the following [ID=$id]")
    }

    fun findPermitBYUserIDAndPermitTypeIDANdId(id: Long, userId: Long,permitType: Long): PermitApplicationsEntity {
        permitRepo.findByIdAndUserIdAndPermitType(id,userId,permitType)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Permit Type found with the following [ID=$id]")
    }

    fun permitSave(permits: PermitApplicationsEntity,permitTypeDetails: PermitTypesEntity, user: UsersEntity, map: ServiceMapsEntity): PermitApplicationsEntity {

        with(permits) {
            userId = user.id
            permitType = permitTypeDetails.id
            permitNumber = "${permitTypeDetails.markNumber}${generateRandomText(5, map.secureRandom, map.messageDigestAlgorithm, false)}".toUpperCase()
            enabled = map.initStatus
            status = map.activeStatus
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        return permitRepo.save(permits)
    }

}