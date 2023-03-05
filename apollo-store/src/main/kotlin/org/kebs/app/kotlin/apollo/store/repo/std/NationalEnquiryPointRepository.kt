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
    fun findAllByNepDocumentId(id: Long): SdNepDocumentUploadsEntity
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
    fun findAllByNepDocumentId(id: Long): SdNepDocUploadsEntity
}
interface NationalEnquiryEntityRepository : JpaRepository<NationalEnquiryEntity, Long> {
    @Query(
        value = "SELECT * FROM SD_NEP_EQUIRY  WHERE STATUS=0 AND ID=:enquiryId ORDER BY ID DESC",nativeQuery = true)
    fun getNepDivisionRequests(enquiryId: Long): MutableList<NationalEnquiryEntity>

    @Query(
        value = "SELECT * FROM SD_NEP_EQUIRY  WHERE STATUS=1 ORDER BY ID DESC",nativeQuery = true)
    fun getNepDivisionResponse(): MutableList<NationalEnquiryEntity>

}

interface SdNepDraftRepository : JpaRepository<SdNepDraft, Long> {
    @Query(
        value = "SELECT * FROM SD_NEP_DRAFT  WHERE STATUS=0 ORDER BY ID DESC",nativeQuery = true)
    fun getDraftNotification(): MutableList<SdNepDraft>

    @Query(
        value = "SELECT * FROM SD_NEP_DRAFT  WHERE STATUS=1 ORDER BY ID DESC",nativeQuery = true)
    fun getNotificationForApproval(): MutableList<SdNepDraft>

    @Query(
        value = "SELECT * FROM SD_NEP_DRAFT  WHERE STATUS=2 ORDER BY ID DESC",nativeQuery = true)
    fun getDraftNotificationForUpload(): MutableList<SdNepDraft>
}
interface SdNepDraftUploadsEntityRepository : JpaRepository<SdNepDraftUploadsEntity, Long> {
    fun findAllByNepDraftId(id: Long): SdNepDraftUploadsEntity
}
interface NEPWtoNotificationRepository : JpaRepository<NEPWtoNotification, Long> {

}

interface NepNotificationFormEntityRepository : JpaRepository<NepNotificationFormEntity, Long> {
    @Query(
        value = "SELECT * FROM SD_NEP_NOTIFICATION_FORM  WHERE STATUS=0 ORDER BY ID DESC",nativeQuery = true)
    fun getDraftNotification(): MutableList<NepNotificationFormEntity>

    @Query(
        value = "SELECT * FROM SD_NEP_NOTIFICATION_FORM  WHERE STATUS=1 ORDER BY ID DESC",nativeQuery = true)
    fun getNotificationForApproval(): MutableList<NepNotificationFormEntity>

    @Query(
        value = "SELECT * FROM SD_NEP_NOTIFICATION_FORM  WHERE STATUS=2 ORDER BY ID DESC",nativeQuery = true)
    fun getDraftNotificationForUpload(): MutableList<NepNotificationFormEntity>

}




