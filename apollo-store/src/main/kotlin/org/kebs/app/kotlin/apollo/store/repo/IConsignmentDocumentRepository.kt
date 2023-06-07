package org.kebs.app.kotlin.apollo.store.repo

//package org.kebs.app.kotlin.apollo.store.repo
//
//
//import org.kebs.app.kotlin.apollo.store.model.*
//import org.springframework.data.hazelcast.repository.HazelcastRepository
//import org.springframework.data.jpa.repository.Query
//import org.springframework.data.repository.query.Param
//import org.springframework.stereotype.Repository
//import java.util.*
//
//@Repository
//interface IConsignmentDocumentRepository : HazelcastRepository<ConsignmentDocumentEntity, Long> {
//    /*
//    * Paula repo functions
//    * */
//    fun findByAssignedStatus(assignedStatus: Int): List<ConsignmentDocumentEntity>
//
//    fun findByAssignedStatusAndRejectedStatus(assignedStatus: Int, rejectedStatus: Int): List<ConsignmentDocumentEntity>
//
//    // fun findByAssignedIoAndAssignedStatusAndRejectedStatus(assignedIo: Long, assignedStatus: Int, rejectedStatus: Int): List<ConsignmentDocumentEntity>
//
//    fun findByBlacklistApproved(blackListApproved: Int): List<ConsignmentDocumentEntity>
//
//    fun findByBlacklistStatus(blacklistStatus: Int): List<ConsignmentDocumentEntity>
//
//    /**
//     * TODO: Review together
//     */
//
//    fun findByImporterIdAndBlacklistStatus(importerId: Long, blacklistStatus: Int): List<ConsignmentDocumentEntity>
//
//    fun findByClearingAgentIdAndBlacklistStatus(clearingAgentId: Long, blacklistStatus: Int): List<ConsignmentDocumentEntity>
//
//    fun findByTargetApproved(targetApproved: Int): List<ConsignmentDocumentEntity>
//
//    fun findByTargetedStatus(targetedStatus: Int): List<ConsignmentDocumentEntity>
//
//    fun findByUcrNumber(ucrNumber: String): ConsignmentDocumentEntity?
//
//    fun findAllByApplicantId(id: Long): List<ConsignmentDocumentEntity>
//
//    fun findAllByApplicantIdAndRejectedStatus(id: Long, rejectedStatus: Int): List<ConsignmentDocumentEntity>
//
//    override fun findAll(): List<ConsignmentDocumentEntity>
//
//    override fun findById(id: Long): Optional<ConsignmentDocumentEntity>
//    override fun <S : ConsignmentDocumentEntity?> save(entity: S): S
//
//     fun findFirstByUcrNumber(ucrNumber: String) : ConsignmentDocumentEntity?
//
//    fun findAllById(id: Long): List<ConsignmentDocumentEntity>?
//
//    fun findByAssignedIoAndAssignedStatus(assignedIo: Long, assignedStatus: Int): List<ConsignmentDocumentEntity>
//
//    fun findAllByApplicantId(applicantId: CdApplicantDetailsEntity): List<ConsignmentDocumentEntity>
//    fun findAllByAssignedIo(assignedIo: Long): List<ConsignmentDocumentEntity>
//
//    @Query("SELECT cd as ConsignmentDocumentEntity, coc as CocsBakEntity FROM ConsignmentDocumentEntity cd left join CocsBakEntity coc on  cd.ucrNumber = coc.ucrNumber where coc.clean = :clean")
//    fun CleanCocs(@Param("clean") clean: String): List<ConsignmentDocumentEntity>?
//
//    @Query("SELECT cd as ConsignmentDocumentEntity, coc as CocsBakEntity FROM ConsignmentDocumentEntity cd left join CocsBakEntity coc on  cd.ucrNumber = coc.ucrNumber where (coc.clean = :clean and cd.id = :id)")
//    fun CleanCocsById(@Param("clean") clean: String, @Param("id") id: Long): List<ConsignmentDocumentEntity>?
//
//    //get all cor vehicles
//    @Query("SELECT cd as ConsignmentDocumentEntity, cor as CorsEntity FROM ConsignmentDocumentEntity cd left join CorsEntity cor on  cd.ucrNumber = cor.ucrNumber where cor.corNumber is not null ")
//    fun corsVehicles(): List<ConsignmentDocumentEntity>?
//
//    //get all assigned vehicles to IO
//    @Query("SELECT cd as ConsignmentDocumentEntity, cor as CorsEntity FROM ConsignmentDocumentEntity cd left join CorsEntity cor on  cd.ucrNumber = cor.ucrNumber where cor.corNumber is not null and cd.assignedIo = :assignedIo and cd.targetedStatus = :targetedStatus ")
//    fun corsVehiclesByIO(@Param("assignedIo") assignedIo: Long, @Param("targetedStatus") targetedStatus: Int): List<ConsignmentDocumentEntity>?
//
//    //get once vehicle with cor
//    @Query("SELECT cd as ConsignmentDocumentEntity, cor as CorsEntity FROM ConsignmentDocumentEntity cd left join CorsEntity cor on  cd.ucrNumber = cor.ucrNumber where cor.corNumber is not null and cd.id = :id ")
//    fun corsVehiclesById(@Param("id") id: Long): List<ConsignmentDocumentEntity>?
//
//    //get one cor assigned to IO
//    @Query("SELECT cd as ConsignmentDocumentEntity, cor as CorsEntity FROM ConsignmentDocumentEntity cd left join CorsEntity cor on  cd.ucrNumber = cor.ucrNumber where cor.corNumber is not null and cd.id = :id and cd.assignedIo = :assignedIo and cd.targetedStatus = :targetedStatus ")
//    fun corsVehiclesByIdAndIO(@Param("id") id: Long, @Param("assignedIo") assignedIo: Long, @Param("targetedStatus") targetedStatus: Int): List<ConsignmentDocumentEntity>?
//
//    @Query("SELECT cd as ConsignmentDocumentEntity, cor as CorsEntity FROM ConsignmentDocumentEntity cd left join CorsEntity cor on  cd.ucrNumber = cor.ucrNumber where cor.corNumber is null ")
//    fun nonCorsVehicles(): List<ConsignmentDocumentEntity>?
//
//    @Query("SELECT cd as ConsignmentDocumentEntity, cor as CorsEntity FROM ConsignmentDocumentEntity cd left join CorsEntity cor on  cd.ucrNumber = cor.ucrNumber where cor.corNumber is null and cd.id = :id ")
//    fun nonCorsVehiclesById(@Param("id") id: Long): List<ConsignmentDocumentEntity>?
//
//    @Query("SELECT cd as ConsignmentDocumentEntity, cor as CorsEntity FROM ConsignmentDocumentEntity cd left join CorsEntity cor on  cd.ucrNumber = cor.ucrNumber where cor.corNumber is not null and cd.targetedStatus = :targetedStatus ")
//    fun findAllByTargetedStatus(@Param("targetedStatus") targetedStatus: Int): List<ConsignmentDocumentEntity>
//    fun findByAssignedIoAndAssignedStatusAndRejectedStatus(it: Long, i: Int, i1: Int)
//
//}
//
////@Repository
////interface IRiskInspectionCheck : HazelcastRepository<RiskProfileEntity, Long> {
////    fun findByHsCode(hsCode: String): List<RiskProfileEntity>?
////}
//
//
//@Repository
//interface ICdApplicantEntityRepo : HazelcastRepository<CdApplicantDetailsEntity, Long> {
//    fun findAllById(Id: Long): List<CdApplicantDetailsEntity>?
//}
//
//
//@Repository
//interface ICdApplicantStatusEntityRepo : HazelcastRepository<CdApplicationStatusEntity, Long> {
//    fun findAllById(Id: Long): List<CdApplicantDetailsEntity>?
//}
//
//@Repository
//interface ICdTransportDetailsEntityRepo : HazelcastRepository<CdTransportDetailsEntity, Long> {
//    fun findAllById(Id: Long): List<CdTransportDetailsEntity>?
//}
//
//@Repository
//interface ICdExporterDetailsEntityRepo : HazelcastRepository<CdExporterDetailsEntity, Long> {
//    fun findAllById(Id: Long): List<CdExporterDetailsEntity>?
//}
//
//@Repository
//interface ICdImporterDetailsEntityRepo : HazelcastRepository<CdImporterDetailsEntity, Long> {
//    fun findAllById(Id: Long): List<CdImporterDetailsEntity>?
//}
//
//@Repository
//interface ICdConsignorDetailsEntityRepo : HazelcastRepository<CdConsignorDetailsEntity, Long> {
//    fun findAllById(Id: Long): List<CdImporterDetailsEntity>?
//}
//
//@Repository
//interface ICdConsigneeDetailsEntityRepo : HazelcastRepository<CdConsigneeDetailsEntity, Long> {
//    fun findAllById(Id: Long): CdImporterDetailsEntity?
//}
//
//@Repository
//interface  IConsignmentItems : HazelcastRepository<CdItemDetailsEntity, Long>{
////    fun findByConsignmentId(consigmentId: Long): List<CdItemDetailsEntity>
////    fun findAllByConsignmentId(consigmentId: Long): List<CdItemDetailsEntity>
//    fun findAllByConsigmentId(consigmentId: Long) : List<CdItemDetailsEntity>
////    fun findByConsigmentId(consignm: Long): List<CdItemDetailsEntity>
//}

//@Repository
//interface ICocsRepository : HazelcastRepository<CocsBakEntity, Long>{
//    fun findByUcrNumber(ucrNumber: String): CocsBakEntity?
//}


//@Repository
//interface  IPvocPatnersRegion : HazelcastRepository<PvocPartnersRegionEntity, Long>{
//}
//
//
//@Repository
//interface IRiskTypes : HazelcastRepository<CfgRiskTypesEntity, Long>{
////    fun findByUcrNumber(ucrNumber: String): CfgRiskTypesEntity?
//}
//
//@Repository
//interface IRiskProfile : HazelcastRepository<RiskProfileEntity, Long>{
//    fun findByImporterName(importerName: String): RiskProfileEntity?
//}

//@Repository
//interface IDemandNote : HazelcastRepository<CdDemandNoteEntity, Long>{
//    fun findByUcrNumber(ucrNumber: String): CdDemandNoteEntity?
//
//    fun findByItemIdNo(itemIdNo: Long): CdDemandNoteEntity?
//}

//@Repository
//interface  IDestinationInspectionFee : HazelcastRepository<DestinationInspectionFeeEntity, Long>{
//}
//
//@Repository
//interface  IPvocPatnersCountries : HazelcastRepository<PvocPartnersCountriesEntity, Long>{
//    fun findByCountryName(countryName: String): PvocPartnersCountriesEntity?
////    @Query("SELECT m FROM Movie m WHERE m.director LIKE %?#{escape([0])} escape ?#{escapeCharacter()}")
//
//    fun findByCountryNameContainingIgnoreCase(countryName: String): PvocPartnersCountriesEntity?
//}

//@Repository
//interface ICocsBakRepository : HazelcastRepository<CocsBakEntity, Long>{
//    fun findFirstByUcrNumber(ucrNumber: String): CocsBakEntity?
//
////    fun findByIsClean(isClean: String): List<CocsBakEntity>
//}

//@Repository
//interface ICocItemRepository : HazelcastRepository<CocItemsEntity, Long>{
//    fun findByCocId(cocId: Long): List<CocItemsEntity>
//}
//
//@Repository
//interface ILocalCocItems : HazelcastRepository<CdLocalCocItemsEntity, Long>{
//    fun findByUcrNumber(ucrNumber: String): CdLocalCocItemsEntity?
//}

//@Repository
//interface ILocalCoc : HazelcastRepository<CdLocalCocEntity, Long>{
//    fun findByUcrNumber(ucrNumber: String): CdLocalCocEntity?
//}

//@Repository
//interface IRemarksAdd : HazelcastRepository<RemarksEntity, Long>{
//    fun findByConsignmentDocumentId(consignmentDocumentId: Long): List<RemarksEntity>
//}
//
//@Repository
//interface ISampleForm : HazelcastRepository<CdSampleSubmissionItemsEntity, Long>{
//    fun findByUcrNumber(ucrNumber: String): CdSampleSubmissionItemsEntity?
//}
//
//@Repository
//interface ILaboratory : HazelcastRepository<CdLaboratoryEntity, Long>{
////    fun findByUcrNumber(ucrNumber: String): CdLaboratoryEntity?
//}
//
//@Repository
//interface ISampleCollectForm : HazelcastRepository<CdSampleCollectionEntity, Long>{
//    fun findByUcrNumber(ucrNumber: String): CdSampleCollectionEntity?
//    fun findByItemId(itemId: Long): CdSampleCollectionEntity?
//    fun findByItemHscode(itemHscode: String): CdSampleCollectionEntity?
//}

//@Repository
//interface IChecklistCategory : HazelcastRepository<CdChecklistCategoryEntity, Long>{
//    fun findByIdAndStatus(id: Long, status: Int): CdChecklistCategoryEntity?
//}
//
//@Repository
//interface IChecklistInspectionTypes : HazelcastRepository<CdChecklistTypesEntity, Long>{
//    fun findByIdAndStatus(id: Long, status: Int): CdChecklistTypesEntity?
//
//    fun findByTypeName(typeName: String): CdChecklistTypesEntity?
//}
//
//@Repository
//interface ITargetedCdInspection : HazelcastRepository<CdTragetTypesEntity, Long>{
//    fun findByIdAndStatus(id: Long, status: Int): CdTragetTypesEntity?
//}