package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.di.CdInspectionMinistryGeneralEntity
import org.kebs.app.kotlin.apollo.store.model.di.CdInspectionMinistryStatusEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository

interface ICdInspectionMinistryGeneralEntityRepo : HazelcastRepository<CdInspectionMinistryGeneralEntity, Long> {
    fun findFirstByChassisNo(chassisNo: String): CdInspectionMinistryGeneralEntity?
}

interface ICdInspectionMinistryStatusEntityRepo : HazelcastRepository<CdInspectionMinistryStatusEntity, Long> {
}

interface IMinistryInspectionBodyWorkEntityRepo : HazelcastRepository<MinistryInspectionBodyWorkEntity, Long> {
    fun findFirstByGenerarInspection(generalInspection: Long): MinistryInspectionBodyWorkEntity?
}

interface IMinistryInspectionEngineComponentsEntityRepo : HazelcastRepository<MinistryInspectionEngineComponentsEntity, Long> {
    fun findFirstByGenerarInspection(generalInspection: Long): MinistryInspectionEngineComponentsEntity?
}

interface IMinistryInspectionEngineFunctioningEntityRepo : HazelcastRepository<MinistryInspectionEngineFunctioningEntity, Long> {
    fun findFirstByGenerarInspection(generalInspection: Long): MinistryInspectionEngineFunctioningEntity?
}

interface IMinistryInspectionTestDriveEntityRepo : HazelcastRepository<MinistryInspectionTestDriveEntity, Long> {
    fun findFirstByGenerarInspection(generalInspection: Long): MinistryInspectionTestDriveEntity?
}

interface IMinistryInspectionUnderBodyInspectionEntityRepo : HazelcastRepository<MinistryInspectionUnderBodyInspectionEntity, Long> {
    fun findFirstByGenerarInspection(generalInspection: Long): MinistryInspectionUnderBodyInspectionEntity?
}