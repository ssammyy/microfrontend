package org.kebs.app.kotlin.apollo.api.ports.provided.dao.admin

import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.store.model.BusinessLinesEntity
import org.kebs.app.kotlin.apollo.store.model.BusinessNatureEntity
import org.kebs.app.kotlin.apollo.store.repo.IBusinessLinesRepository
import org.kebs.app.kotlin.apollo.store.repo.IBusinessNatureRepository
import org.springframework.stereotype.Service
import java.sql.Timestamp

@Service
class AdminDaoServices(
    private val businessNatureRepository: IBusinessNatureRepository,
    private val businessLineRepository: IBusinessLinesRepository,
    private val commonDaoServices: CommonDaoServices,


    ) {


    fun getAllBusinessLines(): MutableIterable<BusinessLinesEntity> {
        return businessLineRepository.findAll()
    }

    fun getAllBusinessNature(): MutableIterable<BusinessNatureEntity> {
        return businessNatureRepository.findAll()
    }

    fun createBusinessLine(businessLinesEntity: BusinessLinesEntity) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        businessLinesEntity.createdBy = loggedInUser.id.toString()
        businessLinesEntity.createdOn = Timestamp(System.currentTimeMillis())
        businessLineRepository.save(businessLinesEntity)

    }

    fun createBusinessNature(businessNatureEntity: BusinessNatureEntity) {
        val loggeInUSer = commonDaoServices.loggedInUserDetails()
        businessNatureEntity.createdBy = loggeInUSer.id.toString()
        businessNatureEntity.createdOn = Timestamp(System.currentTimeMillis())
        businessNatureRepository.save(businessNatureEntity)

    }

    fun updateBusinessLine(businessLinesEntity: BusinessLinesEntity) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        businessLinesEntity.modifiedBy = loggedInUser.id.toString()
        businessLinesEntity.modifiedOn = Timestamp(System.currentTimeMillis())
        businessLineRepository.save(businessLinesEntity)
    }

    fun updateBusinessNature(businessNatureEntity: BusinessNatureEntity) {
        val loggeInUSer = commonDaoServices.loggedInUserDetails()
        businessNatureEntity.modifiedBy = loggeInUSer.id.toString()
        businessNatureEntity.modifiedOn = Timestamp(System.currentTimeMillis())
        businessNatureRepository.save(businessNatureEntity)

    }


}
