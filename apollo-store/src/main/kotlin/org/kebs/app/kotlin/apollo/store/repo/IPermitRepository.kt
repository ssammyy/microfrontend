package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.qa.PermitTypesEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface IPermitRepository : HazelcastRepository<PermitApplicationEntity, Long> {
    fun findByManufacturerIdAndPermitTypeOrderByIdDesc(manufacturerId: Long, permitType: Long): List<PermitApplicationEntity>?
    fun findByManufacturerId(manufacturerId: Long, pageable: Pageable?): Page<PermitApplicationEntity>?
    fun findByManufacturerIdOrderByIdDesc(manufacturerId: Long): List<PermitApplicationEntity>?


    fun findByPaymentStatusOrderByIdDesc(status: Long): List<PermitApplicationEntity>?
    fun findByPermitSurveillanceCompletenessStatus(status: Int?, pageable: Pageable?): Page<PermitApplicationEntity>?
    fun findByPaymentStatusOrderByIdDesc(status: Int?, pageable: Pageable?): Page<PermitApplicationEntity>?
    fun findByStatus(status: Int?): List<PermitApplicationEntity>
    fun findByStatusAndUserId(status: Int?, userId: UsersEntity): List<PermitApplicationEntity>
    fun findByUserIdAndStatusAndForeignApplication(userId: UsersEntity, status: Int, foreignApplication: Int, pageable: Pageable?): Page<PermitApplicationEntity>

    //    fun findByIdOrNull(id: Long): Page<PermitApplicationEntity>
    fun findByStatusAndUserIdAndPermitType(status: Int?, userId: UsersEntity, permitType: Long, pageable: Pageable?): Page<PermitApplicationEntity>?

    fun findByStatusAndUserIdAndPermitType(status: Int?, userId: UsersEntity, permitType: Long): List<PermitApplicationEntity>?

    //    fun findByStatusAndUserIdAndForeignApplicationAndPermitType(status: Int?, userId: UsersEntity, permitType: Long, foreignApplication: Long?, pageable: Pageable?): Page<PermitApplicationEntity>?
    fun findByStatusAndUserIdAndForeignApplicationAndPermitType(status: Int?, userId: UsersEntity?, foreignApplication: Int, permitType: Long?, pageable: Pageable?): Page<PermitApplicationEntity>?
    fun findByStatusAndUserIdAndForeignApplicationAndPermitType(status: Int?, userId: UsersEntity?, foreignApplication: Int, permitType: Long?): List<PermitApplicationEntity>?

    fun findByStatusAndUserId(status: Int?, userId: UsersEntity, pageable: Pageable?): Page<PermitApplicationEntity>
    fun findByStatus(status: Int?, pages: Pageable?): Page<PermitApplicationEntity>?
    fun findByPermitTypeAndStatus(permitType: PermitTypesEntity, status: Int?): List<PermitApplicationEntity>?
    fun findByManufacturerName(manufacturerName: String): List<PermitApplicationEntity>?
    fun findByManufacturerEntity(manufacturerEntity: ManufacturersEntity): List<PermitApplicationEntity>?

    fun findByPermitTypeAndStatus(permitType: Long?, status: Int, pageable: Pageable): Page<PermitApplicationEntity>?
    fun findAllByPermitTypeAndUserIdAndStatus(permitType: PermitTypesEntity, userId: UsersEntity, status: Int?, pageable: Pageable?): Page<PermitApplicationEntity>?
    fun findBySaveReasonAndPaymentStatus(saveReason: String, paymentStatus: Int): List<PermitApplicationEntity>
    //    fun findByStatusAndUserId(status: Int?, userId: UsersEntity?): List<PermitApplicationEntity>
    fun findByIdIsIn(ids : List<Long>) : List<PermitApplicationEntity>
}

@Repository
interface IForeignDmarkPermitApplicationRepository : HazelcastRepository<DmarkForeignApplicationsEntity, Long> {
    fun findBySaveReasonAndStatus(saveReason: String, status: Long): List<DmarkForeignApplicationsEntity>
}

/*
@Repository
interface IPermitTypesEntityRepository : HazelcastRepository<PermitTypesEntity, Long> {
    fun findByStatus(status: Int): List<PermitTypesEntity>
    fun findByStatusAndId(status: Long, id: Long): PermitTypesEntity?
    fun findByTypeName(typeName: String): PermitTypesEntity?
    fun findByIdAndStatus(id: Long, status: Long): List<PermitTypesEntity>
}
*/

@Repository
interface IReviewRepository : HazelcastRepository<HofQamReviewEntity, Int> {
    fun findByPermitIdAndCompletenessStatus(permitId: Long, completenessStatus: Int): HofQamReviewEntity?

}

@Repository
interface IAwardReviewRepository : HazelcastRepository<PermitAwardingEntity, Int> {
    fun findByPermitIdAndManufacturerIdAndAwardedStatus(permitId: Long, manufacturerId: Long, awardedStatus: Int): PermitAwardingEntity?
}

@Repository
interface ITurnOverRatesRepository : HazelcastRepository<CfgTurnOverRatesEntity, Long> {
    fun findByIdAndFirmType(id: Long, firmType: String): CfgTurnOverRatesEntity?
}