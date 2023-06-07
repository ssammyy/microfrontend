package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.CdInspectionChecklistEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository

@Repository
interface ICheckListRepository : HazelcastRepository<CdInspectionChecklistEntity, Long>{
    fun findByItemId(itemId: Long): CdInspectionChecklistEntity?

    fun findFirstByCdIdNumber(cdIdNumber: Long): CdInspectionChecklistEntity?

    fun findFirstByItemId(itemId: Long): List<CdInspectionChecklistEntity>

    fun findByCdIdNumberAndItemNumber(cdIdNumber: Long, itemNumber: Long): List<CdInspectionChecklistEntity>

    fun findFirstByItemIdAndStatus(itemId: Long, status: Int): CdInspectionChecklistEntity?

    override fun findAll(): List<CdInspectionChecklistEntity>
//    override fun findById(id: Long?): CdInspectionChecklistEntity?
}
//@Repository
//interface ICheckList : HazelcastRepository<CdInspectionChecklistEntity, Long> {
//    fun findByUcrNumber(ucrNumber: String): CdInspectionChecklistEntity?
//
//}