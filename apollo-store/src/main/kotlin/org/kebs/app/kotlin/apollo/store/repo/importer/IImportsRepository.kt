package org.kebs.app.kotlin.apollo.store.repo.importer

import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.di.CdItemDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.importer.*
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository


@Repository
interface IRfcDocumentsDetailsRepository : HazelcastRepository<RfcDocumentsDetailsEntity, Long> {
    fun findByImporterIDAndRfcTypeIdAndStatus(importerID: UsersEntity, rfcTypeId: RfcTypesEntity, status: Int): List<RfcDocumentsDetailsEntity>?
    fun findByIdfNumberAndUcrNumber(idfNumber: String, ucrNumber: String): RfcDocumentsDetailsEntity?
}

@Repository
interface ICsApprovalApplicationsRepository : HazelcastRepository<CsApprovalApplicationsEntity, Long> {
    fun findByImporterId(importerId: UsersEntity): List<CsApprovalApplicationsEntity>?
    fun findByEntryPointId(entryPointId: SectionsEntity): List<CsApprovalApplicationsEntity>?
    fun findByUuid(uuid: String): CsApprovalApplicationsEntity?
    fun findByUcrNo(ucrNumber: String): CsApprovalApplicationsEntity?
}

@Repository
interface ICsApprovalApplicationsUploadsRepository : HazelcastRepository<CsApprovalApplicationsUploadsEntity, Long> {
    fun findByCsApprovalApplicationId(csApprovalApplicationId: CsApprovalApplicationsEntity): List<CsApprovalApplicationsUploadsEntity>?
}

@Repository
interface ITemporaryImportApplicationsRepository : HazelcastRepository<TemporaryImportApplicationsEntity, Long> {
    fun findByImporterId(importerId: UsersEntity): List<TemporaryImportApplicationsEntity>?
    fun findByEntryPointId(entryPointId: SectionsEntity): List<TemporaryImportApplicationsEntity>?
    fun findByUuid(uuid: String): TemporaryImportApplicationsEntity?
}

@Repository
interface ITemporaryImportApplicationsUploadsRepository : HazelcastRepository<TemporaryImportApplicationsUploadsEntity, Long> {
    fun findByTemporaryImportApplicationId(temporaryImportApplicationId: TemporaryImportApplicationsEntity): List<TemporaryImportApplicationsUploadsEntity>?
}


@Repository
interface IRfcCoiItemsDetailsRepository : HazelcastRepository<RfcCoiItemsDetailsEntity, Long> {
    fun findByRfcIdAndStatus(rfcId: RfcDocumentsDetailsEntity, status: Int): List<RfcCoiItemsDetailsEntity>?
}

@Repository
interface IRfcTypesTypesRepository : HazelcastRepository<RfcTypesEntity, Long> {
    fun findByStatus(status: Int): List<RfcTypesEntity>?
}

@Repository
interface IImporterDiApplicationsTypesRepository : HazelcastRepository<ImporterDiApplicationsTypesEntity, Long> {
    fun findByStatus(status: Int): List<ImporterDiApplicationsTypesEntity>?
    fun findByUuid(uuid: String): ImporterDiApplicationsTypesEntity?
}