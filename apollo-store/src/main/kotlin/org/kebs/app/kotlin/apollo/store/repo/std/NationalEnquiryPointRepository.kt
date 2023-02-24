package org.kebs.app.kotlin.apollo.store.repo.std


import org.kebs.app.kotlin.apollo.store.model.std.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface NationalEnquiryPointRepository : JpaRepository<NationalEnquiryPoint, Long>
{
    @Query(
        value = "SELECT * FROM SD_NEP_NOTIFICATION  WHERE STATUS=0 ORDER BY ID DESC",nativeQuery = true)
    fun getNepRequests(): MutableList<NationalEnquiryPoint>

}
interface SdNepDocumentUploadsEntityRepository : JpaRepository<SdNepDocumentUploadsEntity, Long>
{

}
interface NationalEnquiryPointEntityRepository : JpaRepository<NationalEnquiryPointEntity, Long>
{
    @Query(
        value = "SELECT * FROM SD_NEP_NOTIFICATION_TBL  WHERE STATUS=0 ORDER BY ID DESC",nativeQuery = true)
    fun getNepRequests(): MutableList<NationalEnquiryPointEntity>
}

interface NepRemarksRepository : JpaRepository<NepRemarks, Long>
{

}
interface SdNepDocUploadsEntityRepository : JpaRepository<SdNepDocUploadsEntity, Long> {
    fun findByNepDocumentId(id: Long): SdNepDocUploadsEntity
}
interface NationalEnquiryEntityRepository : JpaRepository<NationalEnquiryEntity, Long> {
    @Query(
        value = "SELECT * FROM SD_NEP_EQUIRY  WHERE STATUS=0 ORDER BY ID DESC",nativeQuery = true)
    fun getNepDivisionRequests(): MutableList<NationalEnquiryEntity>

}
