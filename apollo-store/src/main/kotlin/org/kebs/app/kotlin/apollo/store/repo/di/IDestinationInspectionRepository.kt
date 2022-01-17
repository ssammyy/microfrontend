package org.kebs.app.kotlin.apollo.store.repo.di

import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.di.*
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocPartnerTypeEntity
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocPartnersCountriesEntity
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocPartnersRegionEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.math.BigDecimal


/***********
 *  NEW repos
 *********/

@Repository
interface IConsignmentDocumentDetailsRepository : HazelcastRepository<ConsignmentDocumentDetailsEntity, Long> {
    fun findByFreightStation_IdInAndCdTypeAndUcrNumberIsNotNullAndOldCdStatusIsNullAndCompliantStatusIsNullAndApproveRejectCdStatusIsNull(
            freightStation: List<Long>,
            cdType: ConsignmentDocumentTypesEntity,
            page: Pageable
    ): Page<ConsignmentDocumentDetailsEntity>

    fun findByFreightStation_IdInAndUcrNumberIsNotNullAndOldCdStatusIsNullAndCompliantStatusIsNullAndApproveRejectCdStatusIsNull(
            freightStation: List<Long>,
            page: Pageable
    ): Page<ConsignmentDocumentDetailsEntity>


    fun findAllByAssignedInspectionOfficerAndCdTypeAndUcrNumberIsNotNullAndOldCdStatusIsNullAndCompliantStatusIsNull(
            assignedInspectionOfficer: UsersEntity,
            cdType: ConsignmentDocumentTypesEntity,
            page: Pageable
    ): Page<ConsignmentDocumentDetailsEntity>


    fun findAllByAssignerAndCdTypeAndUcrNumberIsNotNullAndOldCdStatusIsNullAndCompliantStatusIsNotNullOrApproveRejectCdStatusIsNotNull(
            assignedInspectionOfficer: UsersEntity,
            cdType: ConsignmentDocumentTypesEntity,
            page: Pageable
    ): Page<ConsignmentDocumentDetailsEntity>

    fun findAllByAssignerAndUcrNumberIsNotNullAndOldCdStatusIsNullAndCompliantStatusIsNotNullOrApproveRejectCdStatusIsNotNull(
            assignedInspectionOfficer: UsersEntity,
            page: Pageable
    ): Page<ConsignmentDocumentDetailsEntity>

    fun countByUcrNumber(ucrNumber: String): Long

    fun findByUcrNumber(ucrNumber: String): ConsignmentDocumentDetailsEntity?
    fun findByUcrNumberAndOldCdStatus(ucrNumber: String, oldCdStatus: Int): List<ConsignmentDocumentDetailsEntity>?
    fun findTopByUcrNumberOrderByIdDesc(ucrNumber: String): ConsignmentDocumentDetailsEntity?
    fun findByUuid(uuid: String): ConsignmentDocumentDetailsEntity?
    fun findByUuidIn(uuid: Iterable<String>): List<ConsignmentDocumentDetailsEntity>
    fun findByFreightStation_IdInAndCdTypeAndAssignedInspectionOfficerIsNullAndOldCdStatusIsNullAndCompliantStatusIn(
            cfsIds: MutableList<Long>,
            cdType: ConsignmentDocumentTypesEntity,
            statuses: List<Int?>,
            page: Pageable): Page<ConsignmentDocumentDetailsEntity>

    fun findByFreightStation_IdInAndCdTypeAndAssignedInspectionOfficerIsNullAndOldCdStatusIsNull(
            cfsIds: MutableList<Long>,
            cdType: ConsignmentDocumentTypesEntity,
            page: Pageable): Page<ConsignmentDocumentDetailsEntity>

    fun findByFreightStation_IdInAndAssignedInspectionOfficerIsNullAndOldCdStatusIsNull(
            cfsIds: MutableList<Long>,
            page: Pageable): Page<ConsignmentDocumentDetailsEntity>

    fun findByFreightStation_IdInAndAssignedInspectionOfficerIsNullAndOldCdStatusIsNullAndCompliantStatusIn(
            cfsIds: MutableList<Long>,
            statuses: List<Int?>,
            page: Pageable): Page<ConsignmentDocumentDetailsEntity>


    fun findAllByAssignedInspectionOfficerAndUcrNumberIsNotNullAndOldCdStatusIsNullAndCompliantStatusIsNull(
            usersEntity: UsersEntity,
            page: Pageable): Page<ConsignmentDocumentDetailsEntity>

    fun findAllByAssignedInspectionOfficerAndCdTypeAndUcrNumberIsNotNullAndOldCdStatusIsNullAndCompliantStatusIn(
            usersEntity: UsersEntity,
            cdStatus: ConsignmentDocumentTypesEntity,
            statuses: List<Int>, page: Pageable): Page<ConsignmentDocumentDetailsEntity>

    fun findAllByAssignedInspectionOfficerAndUcrNumberIsNotNullAndOldCdStatusIsNullAndCompliantStatusIn(
            usersEntity: UsersEntity,
            statuses: List<Int>,
            page: Pageable): Page<ConsignmentDocumentDetailsEntity>
}

@Repository
interface IConsignmentDocumentTypesEntityRepository : HazelcastRepository<ConsignmentDocumentTypesEntity, Long> {
    fun findFirstByVarField1(typeName: String): ConsignmentDocumentTypesEntity?
    fun findByUuid(uuid: String): ConsignmentDocumentTypesEntity?

    fun findByStatus(status: Int): List<ConsignmentDocumentTypesEntity>?
//    fun findAllById(Id: Long): List<ConsignmentDocumentTypesEntity>?
}

@Repository
interface ICdStatusTypesEntityRepository : HazelcastRepository<CdStatusTypesEntity, Long> {
    fun findByTypeNameAndStatus(typeName: String, status: Long): CdStatusTypesEntity?
    fun findByCategoryAndStatus(category: String, status: Int): CdStatusTypesEntity?
    fun findByStatus(status: Int): List<CdStatusTypesEntity>?
//    fun findAllById(Id: Long): List<ConsignmentDocumentTypesEntity>?
}

@Repository
interface ICdCfsUserCfsRepository : HazelcastRepository<CdCfsUserCfsEntity, Long> {

    fun findByCdCfs(cdCfs: Long): CdCfsUserCfsEntity?
//    fun findByTypeNameAndStatus(typeName: String, status: Long): CfgKebsCdcfsUsercfsEntity?

    fun findByStatus(status: Int): List<CdCfsUserCfsEntity>?
//    fun findAllById(Id: Long): List<ConsignmentDocumentTypesEntity>?
}

@Repository
interface ICountryTypeCodesRepository : HazelcastRepository<CountryTypeCodesEntity, Long> {
//    fun findByTypeNameAndStatus(typeName: String, status: Long): CfgKebsCdcfsUsercfsEntity?


    fun findByStatus(status: Int): List<CountryTypeCodesEntity>?
    fun findByCountryCode(countryName: String): CountryTypeCodesEntity?
}

@Repository
interface IBlackListUserTargetRepository : HazelcastRepository<CdBlackListUserTargetTypesEntity, Long> {
    fun findByIdAndStatus(id: Long, status: Int): CdBlackListUserTargetTypesEntity?
    fun findAllByStatusOrderByTypeName(status: Int): List<CdBlackListUserTargetTypesEntity>?
}

@Repository
interface ICfsTypeCodesRepository : HazelcastRepository<CfsTypeCodesEntity, Long> {
    fun findByCfsCode(cfsCode: String): CfsTypeCodesEntity?
    fun findByStatus(status: Int): List<CfsTypeCodesEntity>?
    fun findByStatusOrderByCfsName(status: Int): List<CfsTypeCodesEntity>?

    @Query(
            "SELECT r.* FROM CFG_USERS_CFS_ASSIGNMENTS UR, CFG_KEBS_CFS_TYPE_CODES R WHERE UR.CFS_ID = R.ID AND UR.STATUS = :status AND UR.USER_PROFILE_ID = :userProfileID order by r.ID",
            nativeQuery = true
    )
    fun findRbacCfsByUserProfileID(
            @Param("userProfileID") userProfileID: Long,
            @Param("status") status: Int
    ): List<CfsTypeCodesEntity>?

    @Query("SELECT unique b.USER_ID FROM CFG_USERS_CFS_ASSIGNMENTS a left join DAT_KEBS_USER_PROFILES B on(a.USER_PROFILE_ID=B.ID) WHERE a.STATUS=:status and b.STATUS=:status and CFS_ID=:id", nativeQuery = true)
    fun findCfsUserIds(@Param("id") id: Long, @Param("status") status: Int): List<Long>
//    fun findAllById(Id: Long): List<ConsignmentDocumentTypesEntity>?
}

@Repository
interface IPortsTypeCodesRepository : HazelcastRepository<PortsTypeCodesEntity, Long> {

    fun findByPortCode(portCode: String): PortsTypeCodesEntity?

    fun findByStatus(status: Int): List<PortsTypeCodesEntity>?
//    fun findAllById(Id: Long): List<ConsignmentDocumentTypesEntity>?
}

@Repository
interface ICdPortsUserPortsRepository : HazelcastRepository<CdPortsUserPortsEntity, Long> {

    fun findByCdPorts(cdPorts: Long): CdPortsUserPortsEntity?
//    fun findByTypeNameAndStatus(typeName: String, status: Long): CfgKebsCdcfsUsercfsEntity?

    fun findByStatus(status: Int): List<CdPortsUserPortsEntity>?
//    fun findAllById(Id: Long): List<ConsignmentDocumentTypesEntity>?
}

@Repository
interface ICustomsOfficeTypeCodesRepository : HazelcastRepository<CustomsOfficeTypeCodesEntity, Long> {
//    fun findByTypeNameAndStatus(typeName: String, status: Long): CfgKebsCdcfsUsercfsEntity?

    fun findByStatus(status: Int): List<CustomsOfficeTypeCodesEntity>?
//    fun findAllById(Id: Long): List<ConsignmentDocumentTypesEntity>?
}

@Repository
interface ICdImporterEntityRepository : HazelcastRepository<CdImporterDetailsEntity, Long> {
    fun findAllById(Id: Long): List<CdImporterDetailsEntity>?
}

@Repository
interface ICdFileXmlRepository : HazelcastRepository<CdFileXmlEntity, Long> {
    fun findAllById(Id: Long): List<CdFileXmlEntity>?
    fun findByCdIdAndStatus(cdId: Long, status: Int): CdFileXmlEntity?
}

@Repository
interface ICfgMoneyTypeCodesRepository : HazelcastRepository<MoneyTypeCodesEntity, Long> {
    fun findAllById(Id: Long): List<MoneyTypeCodesEntity>?
    fun findByStatus(status: Int): List<MoneyTypeCodesEntity>?

    fun findByTypeCode(typeCode: String): MoneyTypeCodesEntity?
}

@Repository
interface ICfgCurrencyExchangeRateRepository : HazelcastRepository<CurrencyExchangeRates, Long> {
    @Query("SELECT * from CFG_CURRENCY_EXCHANGE_RATES where to_char(APPLICABLE_DATE,'DD-MM-YYYY')=:date and STATUS=:status", nativeQuery = true)
    fun findByApplicableDateAndStatus(@Param("date") date: String, @Param("status") status: Int): List<CurrencyExchangeRates>

    @Query("SELECT * from CFG_CURRENCY_EXCHANGE_RATES where CURRENCY_CODE=:code and to_char(APPLICABLE_DATE,'DD-MM-YYYY')=:date and STATUS=1 order by  APPLICABLE_DATE desc fetch first 1 row only", nativeQuery = true)
    fun findFirstByCurrencyCodeAndApplicableDateOrderByApplicableDateDesc(@Param("code") currencyCode: String, @Param("date") date: String): CurrencyExchangeRates?
}

@Repository
interface IDiUploadsRepository : HazelcastRepository<DiUploadsEntity, Long> {
    fun findAllByCdId(cdId: ConsignmentDocumentDetailsEntity): List<DiUploadsEntity>?
    fun findAllById(Id: Long): List<DiUploadsEntity>?
}

@Repository
interface ICdExporterEntityRepository : HazelcastRepository<CdExporterDetailsEntity, Long> {
    fun findAllById(Id: Long): List<CdExporterDetailsEntity>?
}

@Repository
interface ICdTransportEntityRepository : HazelcastRepository<CdTransportDetailsEntity, Long> {
    fun findAllById(Id: Long): List<CdTransportDetailsEntity>?
}

@Repository
interface IMinistryStationEntityRepository : HazelcastRepository<MinistryStationEntity, Long> {
    fun findAllById(Id: Long): List<MinistryStationEntity>?
    fun findByStatus(status: Int): List<MinistryStationEntity>?
}

@Repository
interface ICdTransactionsRepository : HazelcastRepository<CdTransactionsEntity, Long> {
    fun findAllById(Id: Long): List<CdTransactionsEntity>?
}

@Repository
interface ILocalCocTypesRepository : HazelcastRepository<LocalCocTypesEntity, Long> {
    fun findAllById(Id: Long): List<LocalCocTypesEntity>?
    fun findByStatus(status: Int): List<LocalCocTypesEntity>?
    fun findByCocTypeCode(cocTypeCode: String): LocalCocTypesEntity?
}

@Repository
interface ICocTypesEntityRepository : HazelcastRepository<CocTypesEntity, Long> {
    fun findAllById(Id: Long): List<CocTypesEntity>?
    fun findByStatus(status: Int): List<CocTypesEntity>?
    fun findByTypeName(typeName: String): CocTypesEntity?
}

@Repository
interface ICdStandardsEntityRepository : HazelcastRepository<CdStandardsEntity, Long> {
    fun findAllById(Id: Long): List<CdStandardsEntity>?
}

@Repository
interface IDestinationInspectionFeeRepository : HazelcastRepository<DestinationInspectionFeeEntity, Long> {
    fun findByStatus(status: Int): List<DestinationInspectionFeeEntity>
}

@Repository
interface InspectionFeeRangesRepository : HazelcastRepository<InspectionFeeRanges, Long> {
    fun findByInspectionFee(inspectionFee: DestinationInspectionFeeEntity): List<InspectionFeeRanges>

    @Query("select * From CFG_KEBS_DESTINATION_FEE_RANGES where (INSPECTION_FEE_ID=:id and  :amount between MINIMUM_KSH and MAXIMUM_KSH and STATUS=1) and (DESCRIPTION=:docType or DESCRIPTION is null )", nativeQuery = true)
    fun findByInspectionFeeAndMinimumKshGreaterThanEqualAndMaximumKshLessThanEqual(@Param("id") inspectionFeeId: Long, @Param("amount") amount: BigDecimal, @Param("docType") documentType: String): List<InspectionFeeRanges>
    fun findByInspectionFeeAndMinimumKshGreaterThanEqual(inspectionFee: DestinationInspectionFeeEntity, amount: BigDecimal): List<InspectionFeeRanges>

}

@Repository
interface ICdServiceProviderEntityRepository : HazelcastRepository<CdServiceProviderEntity, Long> {
    fun findAllById(Id: Long): List<CdServiceProviderEntity>?
}

@Repository
interface ICdConsigneeEntityRepository : HazelcastRepository<CdConsigneeDetailsEntity, Long> {
    fun findAllById(Id: Long): List<CdConsigneeDetailsEntity>?
}

@Repository
interface ICdConsignorEntityRepository : HazelcastRepository<CdConsignorDetailsEntity, Long> {
    fun findAllById(Id: Long): List<CdConsignorDetailsEntity>?
}

interface TransactionStats {
    fun setTotalCount(): Long
    fun setTotalAmount(): BigDecimal
    fun setPaymentStatus(): Int
}

@Repository
interface IDemandNoteRepository : HazelcastRepository<CdDemandNoteEntity, Long> {
    fun findByUcrNumber(ucrNumber: String): CdDemandNoteEntity?
    fun findAllByCdIdAndStatusIn(cdId: Long, statuses: List<Int>): List<CdDemandNoteEntity>
    fun findFirstByCdIdAndStatusIn(cdId: Long, statuses: List<Int>): CdDemandNoteEntity?
    fun findByCdId(cdId: Long): CdDemandNoteEntity?
    fun findByDemandNoteNumber(demandNoteNumber: String): CdDemandNoteEntity?

    @Query("select * from DAT_KEBS_CD_DEMAND_NOTE where  to_char(DATE_GENERATED,'DD-MM-YYYY')=:date and STATUS in(:status)", nativeQuery = true)
    fun findByModifiedOnAndModifiedOnLessThanAndStatusOrderByIdAsc(@Param("date") dateGenerated: String, @Param("status") status: List<Int>, pageable: Pageable): Page<CdDemandNoteEntity>

    @Query("select * from DAT_KEBS_CD_DEMAND_NOTE where  to_char(DATE_GENERATED,'DD-MM-YYYY')=:date and PAYMENT_STATUS=:paymentStatus and STATUS in(:status)", nativeQuery = true)
    fun findByModifiedOnAndModifiedOnLessThanAndPaymentStatusAndStatusOrderByIdAsc(@Param("date") dateGenerated: String, @Param("paymentStatus") paymentStatus: Int, @Param("status") status: List<Int>, page: Pageable): Page<CdDemandNoteEntity>
    fun findByPaymentStatusAndStatusOrderByIdAsc(paymentStatus: Int, status: Int, page: Pageable): Page<CdDemandNoteEntity>
    fun findByStatusOrderByIdAsc(status: Int, page: Pageable): Page<CdDemandNoteEntity>
    fun findByDemandNoteNumberContainingAndStatusOrderByIdAsc(demandNoteNumber: String, status: Int, page: Pageable): Page<CdDemandNoteEntity>
    fun findByInvoiceBatchNumberId(invoiceBatchNumberId: Long): List<CdDemandNoteEntity>?
    fun findByCdIdAndPaymentStatus(cdId: Long, paymentStatus: Int): CdDemandNoteEntity?
    fun findFirstByUcrNumberAndPaymentStatusIn(refNum: String, paymentStatuses: List<Int>): CdDemandNoteEntity?
    fun findByCdIdAndPaymentStatusIn(cdId: Long, paymentStatuses: List<Int>): List<CdDemandNoteEntity>
    fun findAllByPaymentStatusAndSwStatusIn(paymentStatus: Int, swStatus: List<Int?>): List<CdDemandNoteEntity>
    fun findAllByPaymentStatus(paymentStatus: Int): List<CdDemandNoteEntity>?
    fun findFirstByPaymentStatusAndCdRefNoIsNotNull(paymentStatus: Int): CdDemandNoteEntity?
    fun findFirstByPaymentStatusAndCdRefNoIsNotNullOrderByCreatedOnDesc(paymentStatus: Int): CdDemandNoteEntity?
    fun findFirstByPaymentStatusAndCdRefNoIsNotNullAndImporterPinOrderByCreatedOnDesc(paymentStatus: Int,importerPin: String): CdDemandNoteEntity?

    @Query("select count(*) as totalCount, sum(AMOUNT_PAYABLE) totalAmount,paymentStatus from DAT_KEBS_CD_DEMAND_NOTE where  to_char(DATE_GENERATED,'DD-MM-YYYY')=:date group by PAYMENT_STATUS", nativeQuery = true)
    fun transactionStats(date: String): List<TransactionStats>
}

@Repository
interface IDemandNoteItemsDetailsRepository : HazelcastRepository<CdDemandNoteItemsDetailsEntity, Long> {
    fun findByDemandNoteId(demandNoteId: Long): List<CdDemandNoteItemsDetailsEntity>
    fun findByItemId(itemId: Long?): List<CdDemandNoteItemsDetailsEntity>
    fun findByItemIdAndDemandNoteId(itemId: Long?, demandNoteId: Long?): CdDemandNoteItemsDetailsEntity?
}


@Repository
interface IChecklistCategoryRepository : HazelcastRepository<CdChecklistCategoryEntity, Long> {
    fun findByIdAndStatus(id: Long, status: Int): CdChecklistCategoryEntity?
    fun findByStatus(status: Int): List<CdChecklistCategoryEntity>
}

@Repository
interface IChecklistInspectionTypesRepository : HazelcastRepository<CdChecklistTypesEntity, Long> {

    fun findByStatus(status: Int): List<CdChecklistTypesEntity>

    fun findByIdAndStatus(id: Long, status: Int): CdChecklistTypesEntity?

    fun findByTypeName(typeName: String): CdChecklistTypesEntity?
}


@Repository
interface ICdInspectionChecklistRepository : HazelcastRepository<CdInspectionChecklistEntity, Long> {
    fun findByItemId(itemId: Long): CdInspectionChecklistEntity?

    fun findFirstByCdIdNumber(cdIdNumber: Long): CdInspectionChecklistEntity?

    fun findFirstByItemId(itemId: Long): List<CdInspectionChecklistEntity>

    fun findByCdIdNumberAndItemNumber(cdIdNumber: Long, itemNumber: Long): List<CdInspectionChecklistEntity>

    fun findFirstByItemIdAndStatus(itemId: Long, status: Int): CdInspectionChecklistEntity?

    override fun findAll(): List<CdInspectionChecklistEntity>

}

@Repository
interface ICdInspectionGeneralRepository : HazelcastRepository<CdInspectionGeneralEntity, Long> {
    fun findFirstByCdDetails(cdItemDetails: ConsignmentDocumentDetailsEntity): CdInspectionGeneralEntity?
    fun findAllByCdDetails(cdItemDetails: ConsignmentDocumentDetailsEntity): List<CdInspectionGeneralEntity>
    fun findFirstByCdDetails_Uuid(docId: String): CdInspectionGeneralEntity?
}

@Repository
interface ICdInspectionAgrochemChecklistRepository : HazelcastRepository<CdInspectionAgrochemChecklist, Long> {
    fun findByInspectionGeneral(inspectionGeneral: CdInspectionGeneralEntity): CdInspectionAgrochemChecklist?
}

@Repository
interface ICdInspectionAgrochemItemChecklistRepository : HazelcastRepository<CdInspectionAgrochemItemChecklistEntity, Long> {
    fun findByInspectionAndItemId_Id(inspectionGeneral: CdInspectionAgrochemChecklist, itemId: Long?): CdInspectionAgrochemItemChecklistEntity?
    fun findByInspection_InspectionGeneralAndItemId_Id(inspectionGeneral: CdInspectionGeneralEntity, itemId: Long?): CdInspectionAgrochemItemChecklistEntity?
    fun findByInspection(inspectionGeneral: CdInspectionAgrochemChecklist): List<CdInspectionAgrochemItemChecklistEntity>
}

@Repository
interface ICdInspectionEngineeringItemChecklistRepository : HazelcastRepository<CdInspectionEngineeringItemChecklistEntity, Long> {
    fun findByInspectionAndItemId_Id(inspectionGeneral: CdInspectionEngineeringChecklist, itemId: Long?): CdInspectionEngineeringItemChecklistEntity?
    fun findByInspection_InspectionGeneralAndItemId_Id(inspectionGeneral: CdInspectionGeneralEntity, itemId: Long?): CdInspectionEngineeringItemChecklistEntity?
    fun findByInspection(inspectionGeneral: CdInspectionEngineeringChecklist): List<CdInspectionEngineeringItemChecklistEntity>
}

@Repository
interface ICdInspectionEngineeringChecklistRepository : HazelcastRepository<CdInspectionEngineeringChecklist, Long> {
    fun findByInspectionGeneral(inspectionGeneral: CdInspectionGeneralEntity): CdInspectionEngineeringChecklist?
}

@Repository
interface ICdInspectionOtherChecklistRepository : HazelcastRepository<CdInspectionOtherChecklist, Long> {
    fun findByInspectionGeneral(inspectionGeneral: CdInspectionGeneralEntity): CdInspectionOtherChecklist?
}

@Repository
interface ICdInspectionOtherItemChecklistRepository : HazelcastRepository<CdInspectionOtherItemChecklistEntity, Long> {
    fun findByInspectionAndItemId_Id(inspectionGeneral: CdInspectionOtherChecklist, itemId: Long?): CdInspectionOtherItemChecklistEntity?
    fun findByInspection(inspection: CdInspectionOtherChecklist): List<CdInspectionOtherItemChecklistEntity>
    fun findByInspection_InspectionGeneralAndItemId_Id(inspectionGeneral: CdInspectionGeneralEntity, cdItemID: Long): CdInspectionOtherItemChecklistEntity?
}

@Repository
interface ICdInspectionMotorVehicleChecklistRepository : HazelcastRepository<CdInspectionMotorVehicleChecklist, Long> {
    fun findByInspectionGeneral(inspectionGeneral: CdInspectionGeneralEntity): CdInspectionMotorVehicleChecklist?
}

@Repository
interface ICdInspectionMotorVehicleItemChecklistRepository : HazelcastRepository<CdInspectionMotorVehicleItemChecklistEntity, Long> {
    fun findByInspectionAndItemId(inspectionGeneral: CdInspectionMotorVehicleChecklist, itemId: CdItemDetailsEntity?): CdInspectionMotorVehicleItemChecklistEntity?
    fun findByInspection(inspectionGeneral: CdInspectionMotorVehicleChecklist): List<CdInspectionMotorVehicleItemChecklistEntity>
    fun findByMinistryReportSubmitStatusInAndSampled(status: List<Int>, ministrySubmitted: String, page: Pageable): Page<CdInspectionMotorVehicleItemChecklistEntity>
    fun findAllByInspection(inspectionGeneral: CdInspectionMotorVehicleChecklist): List<CdInspectionMotorVehicleItemChecklistEntity>
    fun findFirstByInspection(inspectionGeneral: CdInspectionMotorVehicleChecklist): CdInspectionMotorVehicleItemChecklistEntity?
    fun findByInspection_InspectionGeneralAndItemId(inspectionGeneral: CdInspectionGeneralEntity, itemId: CdItemDetailsEntity?): CdInspectionMotorVehicleItemChecklistEntity?
}

@Repository
interface ILaboratoryRepository : HazelcastRepository<CdLaboratoryEntity, Long> {
    fun findByStatus(status: Int): List<CdLaboratoryEntity>
//    fun findByUcrNumber(ucrNumber: String): CdLaboratoryEntity?
}

@Repository
interface ISampleCollectRepository : HazelcastRepository<CdSampleCollectionEntity, Long> {
    fun findByUcrNumber(ucrNumber: String): CdSampleCollectionEntity?
    fun findByItemId(itemId: Long): CdSampleCollectionEntity?

    fun findFirstByItemId(itemId: Long): List<CdSampleCollectionEntity>

    fun findFirstByItemIdAndStatus(itemId: Long, status: Int): CdSampleCollectionEntity?

    fun findByItemHscode(itemHscode: String): CdSampleCollectionEntity?


    fun findByPermitId(permitId: Long): CdSampleCollectionEntity?
}

@Repository
interface ISampleSubmitRepository : HazelcastRepository<CdSampleSubmissionItemsEntity, Long> {
    fun findByUcrNumber(ucrNumber: String): CdSampleSubmissionItemsEntity?

    fun findFirstByItemId(itemId: Long): List<CdSampleSubmissionItemsEntity>

    fun findByItemId(itemId: Long): CdSampleSubmissionItemsEntity?

    override fun findAll(): List<CdSampleSubmissionItemsEntity>
    fun findByItemIdAndStatus(itemId: Long, status: Int): List<CdSampleSubmissionItemsEntity>

}

@Repository
interface ICdSampleSubmissionParametersRepository : HazelcastRepository<CdSampleSubmissionParamatersEntity, Long> {


    fun findByIdAndStatus(id: Long, status: Int): List<CdSampleSubmissionParamatersEntity>?
    fun findBySampleSubmissionId(sampleSubmissionId: CdSampleSubmissionItemsEntity): List<CdSampleSubmissionParamatersEntity>?

}

//@Repository
//interface ICdApplicationStatusEntityRepository : HazelcastRepository<CdApplicationStatusEntity, Long> {
//    fun findAllById(Id: Long): List<CdApplicationStatusEntity>?
//}

@Repository
interface ICdValuesHeaderLevelEntityRepository : HazelcastRepository<CdValuesHeaderLevelEntity, Long> {
    fun findAllById(Id: Long): List<CdValuesHeaderLevelEntity>?
}

@Repository
interface IUsersCfsAssignmentsRepository : HazelcastRepository<UsersCfsAssignmentsEntity, Long> {
    fun findAllById(Id: Long): List<UsersCfsAssignmentsEntity>?
    fun findAllByCfsId(Id: Long): List<UsersCfsAssignmentsEntity>?

    @Query("select CKCTC.CFS_CODE from CFG_USERS_CFS_ASSIGNMENTS CCA left join CFG_KEBS_CFS_TYPE_CODES CKCTC on CCA.CFS_ID = CKCTC.ID where CCA.STATUS=1 and CCA.USER_PROFILE_ID=:profileId", nativeQuery = true)
    fun findAllUserCfsCodes(@Param("profileId") Id: Long): List<String>
    fun findByUserProfileId(userProfileId: Long): List<UsersCfsAssignmentsEntity>?
    fun findByUserProfileIdAndCfsId(userProfileId: Long, cfsId: Long): UsersCfsAssignmentsEntity?
    fun findByUserProfileIdAndCfsIdAndStatus(userProfileId: Long, cfsId: Long, status: Int): UsersCfsAssignmentsEntity?
}

@Repository
interface ICdStandardsTwoEntityRepository : HazelcastRepository<CdStandardsTwoEntity, Long> {
    fun findAllById(Id: Long): List<CdStandardsTwoEntity>?
}

@Repository
interface ICdApplicantDefinedThirdPartyDetailsRepository :
        HazelcastRepository<CdApplicantDefinedThirdPartyDetailsEntity, Long>

@Repository
interface ICdApprovalHistoryRepository : HazelcastRepository<CdApprovalHistoryEntity, Long>

@Repository
interface ICdContainerRepository : HazelcastRepository<CdContainerEntity, Long>

@Repository
interface ICdDocumentFeeRepository : HazelcastRepository<CdDocumentFeeEntity, Long>

@Repository
interface ICdDocumentHeaderRepository : HazelcastRepository<CdDocumentHeaderEntity, Long>

@Repository
interface ICdHeaderTwoDetailsRepository : HazelcastRepository<CdHeaderTwoDetailsEntity, Long>

@Repository
interface ICdPgaHeaderFieldsRepository : HazelcastRepository<CdPgaHeaderFieldsEntity, Long>

@Repository
interface ICdItemCommodityDetailsRepository : HazelcastRepository<CdItemCommodityDetailsEntity, Long>

@Repository
interface ICdProcessingFeeRepository : HazelcastRepository<CdProcessingFeeEntity, Long>

@Repository
interface ICdProducts2EndUserDetailsRepository : HazelcastRepository<CdProducts2EndUserDetailsEntity, Long>

@Repository
interface ICdProducts2Repository : HazelcastRepository<CdProducts2Entity, Long>

@Repository
interface ICdRiskDetailsActionDetailsRepository : HazelcastRepository<CdRiskDetailsActionDetailsEntity, Long>

@Repository
interface ICdRiskDetailsAssessmentRepository : HazelcastRepository<CdRiskDetailsAssessmentEntity, Long>

@Repository
interface ICdThirdPartyDetailsRepository : HazelcastRepository<CdThirdPartyDetailsEntity, Long>

@Repository
interface IConsignmentItemsRepository : HazelcastRepository<CdItemDetailsEntity, Long> {
    fun findByCdDocId(cdDocId: ConsignmentDocumentDetailsEntity): List<CdItemDetailsEntity>?
    fun findByCdDocIdAndDnoteStatus(cdDocId: ConsignmentDocumentDetailsEntity, dnoteStatus: Int): List<CdItemDetailsEntity>?
    fun findByCdDocIdAndSampledStatus(cdDocId: ConsignmentDocumentDetailsEntity, sampledStatus: Int): List<CdItemDetailsEntity>
    fun findByUuid(uuid: String): CdItemDetailsEntity?
    fun findByCdDocIdAndId(cdType: ConsignmentDocumentDetailsEntity, id: Long?): CdItemDetailsEntity
    fun findByMinistrySubmissionStatus(status: Int): List<CdItemDetailsEntity>?
    fun findByMinistrySubmissionStatus(status: Int, page: Pageable): Page<CdItemDetailsEntity>

    @Query(
            "SELECT DAT_KEBS_CD_ITEM_DETAILS.* FROM\n" +
                    "    DAT_KEBS_CD_ITEM_DETAILS\n" +
                    "        INNER JOIN DAT_KEBS_CD_INSPECTION_GENERAL ON DAT_KEBS_CD_ITEM_DETAILS.ID = DAT_KEBS_CD_INSPECTION_GENERAL.CD_ITEM_DETAILS_ID\n" +
                    "        INNER JOIN DAT_KEBS_CD_INSPECTION_MOTOR_VEHICLE_ITEM_CHECKLIST ON DAT_KEBS_CD_INSPECTION_GENERAL.ID = DAT_KEBS_CD_INSPECTION_MOTOR_VEHICLE_ITEM_CHECKLIST.INSPECTION_GENERAL_ID\n" +
                    "WHERE DAT_KEBS_CD_ITEM_DETAILS.MINISTRY_SUBMISSION_STATUS = 1 AND\n" +
                    "      DAT_KEBS_CD_INSPECTION_MOTOR_VEHICLE_ITEM_CHECKLIST.MINISTRY_REPORT_SUBMIT_STATUS = 1",
            nativeQuery = true
    )
    fun findCompletedMinistrySubmissions(page: Pageable): Page<CdItemDetailsEntity>

    @Query(
            "SELECT DAT_KEBS_CD_ITEM_DETAILS.* FROM\n" +
                    "    DAT_KEBS_CD_ITEM_DETAILS\n" +
                    "        INNER JOIN DAT_KEBS_CD_INSPECTION_GENERAL ON DAT_KEBS_CD_ITEM_DETAILS.ID = DAT_KEBS_CD_INSPECTION_GENERAL.CD_ITEM_DETAILS_ID\n" +
                    "        INNER JOIN DAT_KEBS_CD_INSPECTION_MOTOR_VEHICLE_ITEM_CHECKLIST ON DAT_KEBS_CD_INSPECTION_GENERAL.ID = DAT_KEBS_CD_INSPECTION_MOTOR_VEHICLE_ITEM_CHECKLIST.INSPECTION_GENERAL_ID\n" +
                    "WHERE DAT_KEBS_CD_ITEM_DETAILS.MINISTRY_SUBMISSION_STATUS = 1 AND\n" +
                    "      DAT_KEBS_CD_INSPECTION_MOTOR_VEHICLE_ITEM_CHECKLIST.MINISTRY_REPORT_SUBMIT_STATUS IS NULL",
            nativeQuery = true
    )
    fun findOngoingMinistrySubmissions(page: Pageable): Page<CdItemDetailsEntity>

}

@Repository
interface ICdItemsCurrierRepository : HazelcastRepository<CdItemsCurrierDetailsEntity, Long>

@Repository
interface ICdItemNonStandardEntityRepository : HazelcastRepository<CdItemNonStandardEntity, Long> {
    fun findByCdItemDetailsId(cdItemDetailsEntity: CdItemDetailsEntity): CdItemNonStandardEntity?
}

@Repository
interface ILocalCocEntityRepository : HazelcastRepository<CdLocalCocEntity, Long> {
    fun findByUcrNumber(ucrNumber: String): CdLocalCocEntity?
}

@Repository
interface ILocalCorEntityRepository : HazelcastRepository<CdLocalCorEntity, Long> {
    fun findByUcrNumber(ucrNumber: String): CdLocalCorEntity?
}

@Repository
interface ILocalCocItemsEntityRepository : HazelcastRepository<CdLocalCocItemsEntity, Long> {
    fun findByLocalCocId(localCocId: Long): List<CdLocalCocItemsEntity>?
}

@Repository
interface IPvocPartnersCountriesRepository : HazelcastRepository<PvocPartnersCountriesEntity, Long> {
    fun findByCountryName(countryName: String): PvocPartnersCountriesEntity?
    fun findByCountryNameContainingIgnoreCase(countryName: String): PvocPartnersCountriesEntity?
    fun findByAbbreviationIgnoreCase(abbreviation: String): PvocPartnersCountriesEntity?
    fun findAllByStatus(status: Int): List<PvocPartnersCountriesEntity>
}

@Repository
interface IPvocPartnersRegion : HazelcastRepository<PvocPartnersRegionEntity, Long>{
    fun findAllByStatus(status: Int): List<PvocPartnersRegionEntity>
}

@Repository
interface IPvocPartnerTypeRepository : HazelcastRepository<PvocPartnerTypeEntity, Long>{
    fun findAllByStatus(status: Int): List<PvocPartnerTypeEntity>
}

@Repository
interface IIDFDetailsEntityRepository : HazelcastRepository<IDFDetailsEntity, Long> {
    fun findByBaseDocRefNo(baseDocRefNo: String): IDFDetailsEntity?
    fun findFirstByUcrNo(ucrNo: String): IDFDetailsEntity?
}

@Repository
interface IIDFItemDetailsEntityRepository : HazelcastRepository<IDFItemDetailsEntity, Long> {
    //fun findByIDFDetailsId(IDFDetailsId: IDFDetailsEntity): List<IDFItemDetailsEntity>?

    fun findAllByIdfDetails(idfDetailsEntity: IDFDetailsEntity): List<IDFItemDetailsEntity>?

}

@Repository
interface IDeclarationDetailsEntityRepository : HazelcastRepository<DeclarationDetailsEntity, Long> {
    fun findFirstByRefNum(refNum: String): DeclarationDetailsEntity?

    fun findByDeclarationRefNo(declarationRefNo: String): DeclarationDetailsEntity?
}

@Repository
interface ICdDocumentModificationHistoryRepository : HazelcastRepository<CdDocumentModificationHistory, Long> {
    fun findAllByCdId(cdId: Long): List<CdDocumentModificationHistory>
    fun findAllByUcrNumberOrCdId(ucrNumber: String, cdId: Long): List<CdDocumentModificationHistory>
}

@Repository
interface IDeclarationItemDetailsEntityRepository : HazelcastRepository<DeclarationItemDetailsEntity, Long> {
    fun findAllByDeclarationDetailsId(declarationDetailsId: DeclarationDetailsEntity): List<DeclarationItemDetailsEntity>?

}

@Repository
interface IManifestDetailsEntityRepository : HazelcastRepository<ManifestDetailsEntity, Long> {
    fun findByManifestNumber(manifestNumber: String): ManifestDetailsEntity?

    fun findFirstByTdBillCode(tdBillCode: String): ManifestDetailsEntity?
}

//
///***********
// *  **************************************************************************** repos
// *********/
//
//@Repository
//interface IRiskInspectionCheckRepository : HazelcastRepository<RiskProfileEntity, Long>{
//    fun findByHsCode(hsCode: String): RiskProfileEntity?
//}
//
//
//
//@Repository
//interface ICdStatusesValuesRepository : HazelcastRepository<CdStatusesValuesEntity, Long> {
//    fun findByServiceMapId(serviceMapId: Int): CdStatusesValuesEntity?
//}
//
//@Repository
//interface ICdInspectionScheduledRepository : HazelcastRepository<CdInspectionScheduledDetailsEntity, Long> {
//    fun findByCdIdAndStatus(cdId: Long, status: Int): List<CdInspectionScheduledDetailsEntity>
//}
//
//@Repository
//interface IRemarksAddRepository : HazelcastRepository<RemarksEntity, Long>{
//    fun findByConsignmentDocumentId(consignmentDocumentId: Long): List<RemarksEntity>
//
//    fun findFirstByConsignmentDocumentIdAndRemarksProcess(consignmentDocumentId: Long, remarksProcess: String): RemarksEntity?
//}
//
//@Repository
//interface ICocItemRepository : HazelcastRepository<CocItemsEntity, Long>{
//    fun findByCocId(cocId: Long): List<CocItemsEntity>
//}
//
//@Repository
//interface ILabParametersRepository : HazelcastRepository<CdLaboratoryParametersEntity, Long> {
//    override fun findAll(): List<CdLaboratoryParametersEntity>
//
//    fun findFirstByItemId(itemId: Long): List<CdLaboratoryParametersEntity>
//
//    fun findBySampleSubmissionId(sampleSubmissionId: Long): List<CdLaboratoryParametersEntity>
//
//
//    fun findFirstByItemIdAndStatus(itemId: Long, status: Int): CdLaboratoryParametersEntity?
//
//    fun findByItemIdAndStatus(itemId: Long, status: Int): List<CdLaboratoryParametersEntity>
//}
//
///***********
// *  Old repos
// *********/
//
////@Repository
////interface ITargetedCdInspectionRepository : HazelcastRepository<CdTragetTypesEntity, Long>{
////    fun findByIdAndStatus(id: Long, status: Int): CdTragetTypesEntity?
////}
////
////
////@Repository
////interface IChecklistCategory : HazelcastRepository<CdChecklistCategoryEntity, Long>{
////    fun findByIdAndStatus(id: Long, status: Int): CdChecklistCategoryEntity?
////}
////
////@Repository
////interface IChecklistInspectionTypes : HazelcastRepository<CdChecklistTypesEntity, Long>{
////    fun findByIdAndStatus(id: Long, status: Int): CdChecklistTypesEntity?
////
////    fun findByTypeName(typeName: String): CdChecklistTypesEntity?
////}
////
////@Repository
////interface IRiskInspectionCheck : HazelcastRepository<RiskProfileEntity, Long>{
////    fun findByHsCode(hsCode: String): RiskProfileEntity?
////}
////
////@Repository
////interface ICorsItems : HazelcastRepository<CorsEntity, Long>{
////    fun findByIdAndStatus(id: Long, status: Int): CorsEntity
////}
////
////
////
////
////@Repository
////interface ISampleSubmitRepository : HazelcastRepository<CdSampleSubmissionItemsEntity, Long> {
////    fun findByUcrNumber(ucrNumber: String): CdSampleSubmissionItemsEntity?
////
////    fun findFirstByItemId(itemId: Long): List<CdSampleSubmissionItemsEntity>
////
////    override fun findAll(): List<CdSampleSubmissionItemsEntity>
////    fun findByItemIdAndStatus(itemId: Long, status: Int): List<CdSampleSubmissionItemsEntity>
////
////}
////
////@Repository
////interface ILaboratoryParametersRepository : HazelcastRepository<CdLaboratoryParametersEntity, Long>{
////    override fun findAll(): List<CdLaboratoryParametersEntity>
//////    fun findByUcrNumber(ucrNumber: String): CdLaboratoryEntity?
////}
////
////@Repository
////interface ILaboratoryRepository : HazelcastRepository<CdLaboratoryEntity, Long>{
//////    fun findByUcrNumber(ucrNumber: String): CdLaboratoryEntity?
////}
////
////@Repository
////interface ISampleCollectRepository : HazelcastRepository<CdSampleCollectionEntity, Long> {
////    fun findByUcrNumber(ucrNumber: String): CdSampleCollectionEntity?
////    fun findByItemId(itemId: Long): CdSampleCollectionEntity?
////
////    fun findFirstByItemId(itemId: Long): List<CdSampleCollectionEntity>
////
////    fun findFirstByItemIdAndStatus(itemId: Long, status: Int): CdSampleCollectionEntity?
////
////    fun findByItemHscode(itemHscode: String): CdSampleCollectionEntity?
////
////    fun findByPermitId(permitId: PermitApplicationEntity): CdSampleCollectionEntity?
////}
////
//////@Repository
//////interface ICheckList : HazelcastRepository<CdInspectionChecklistEntity, Long>{
//////    fun findByUcrNumber(ucrNumber: String): CdInspectionChecklistEntity?
//////
//////}
////
////
////
////@Repository
////interface ILocalCoc : HazelcastRepository<CdLocalCocEntity, Long>{
////    fun findByUcrNumber(ucrNumber: String): CdLocalCocEntity?
////}
////
////@Repository
////interface IRemarksRepository : HazelcastRepository<RemarksEntity, Long>{
////    fun findByConsignmentDocumentId(consignmentDocumentId: Long): List<RemarksEntity>
////    fun findByUserId(user_id: Long) : List<RemarksEntity>
////    fun findAllByConsignmentDocumentId(consignmentDocumentId: Long) : List<RemarksEntity>
////    fun findAllByPvocExceptionApplicationId(pvocExceptionApplicationId: Long) : List<RemarksEntity>?
////}
////
////@Repository
////interface ICocsBakRepository : HazelcastRepository<CocsBakEntity, Long>{
////    fun findByUcrNumber(ucrNumber: String): CocsBakEntity?
////    fun findFirstByCocNumber(cocNumber: String) : CocsBakEntity?
////    fun findAllByRouteAndShipmentSealNumbersIsNull(route : String,  pageable: Pageable) : Page<CocsBakEntity>?
////}
//
//
//
//@Repository
//interface ILocalCocItems : HazelcastRepository<CdLocalCocItemsEntity, Long>{
//    fun findByUcrNumber(ucrNumber: String): CdLocalCocItemsEntity?
//}
//
//@Repository
//interface IDemandNoteRepository : HazelcastRepository<CdDemandNoteEntity, Long>{
//    fun findByUcrNumber(ucrNumber: String): CdDemandNoteEntity?
//
//    fun findFirstByItemIdNo(itemIdNo: Long): List<CdDemandNoteEntity>
//
//    fun findByItemIdNo(itemIdNo: Long): CdDemandNoteEntity?
//}
//
//@Repository
//interface IDestinationInspectionFeeRepository : HazelcastRepository<DestinationInspectionFeeEntity, Long>
//
//
//@Repository
//interface IPvocPatnersRegion : HazelcastRepository<PvocPartnersRegionEntity, Long>
//
//
////@Repository
////interface IRiskTypes : HazelcastRepository<CfgRiskTypesEntity, Long>{
//////    fun findByUcrNumber(ucrNumber: String): CfgRiskTypesEntity?
////}
//
//@Repository
//interface IRiskProfile : HazelcastRepository<RiskProfileEntity, Long>{
//    fun findByImporterName(importerName: String): RiskProfileEntity?
//}
//
//@Repository
//interface ICocsRepository : HazelcastRepository<CocsBakEntity, Long>{
//    fun findByUcrNumber(ucrNumber: String): CocsBakEntity?
//}
