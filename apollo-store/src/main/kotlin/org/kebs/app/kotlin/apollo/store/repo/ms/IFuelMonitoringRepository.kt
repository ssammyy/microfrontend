/*
 *
 *  *
 *  *
 *  *  *    Copyright (c) ${YEAR}.   BSK Global Technologies
 *  *  *
 *  *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *    you may not use this file except in compliance with the License.
 *  *  *    You may obtain a copy of the License at
 *  *  *
 *  *  *       http://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  *    Unless required by applicable law or agreed to in writing, software
 *  *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *   See the License for the specific language governing permissions and
 *  *  *   limitations under the License.
 *  *
 *
 */

package org.kebs.app.kotlin.apollo.store.repo.ms

import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.ms.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.sql.Date

@Repository
interface IFuelBatchRepository : HazelcastRepository<MsFuelBatchInspectionEntity, Long>, JpaSpecificationExecutor<MsFuelBatchInspectionEntity> {
    override fun findAll(): List<MsFuelBatchInspectionEntity>
    fun findAllByOrderByIdDesc(pageable: Pageable): Page<MsFuelBatchInspectionEntity>
    fun findAllOrderByIdAsc(): List<MsFuelBatchInspectionEntity>?
    fun findByReferenceNumber(referenceNumber: String): MsFuelBatchInspectionEntity?
}

@Repository
interface IFuelInspectionRepository : HazelcastRepository<MsFuelInspectionEntity, Long> {
    override fun findAll( pageable: Pageable): Page<MsFuelInspectionEntity>
    fun findAllByOrderByIdDesc( pageable: Pageable): Page<MsFuelInspectionEntity>
    fun findAllByOrderByIdAsc(): List<MsFuelInspectionEntity>
    fun findAllByBatchId(batchId: Long): List<MsFuelInspectionEntity>?

    fun findByReferenceNumber(referenceNumber: String): MsFuelInspectionEntity?

//    fun findByIdAndMsProcessStatus(id: Long, msProcessStatus: Int):  MsFuelInspectionEntity?
//    fun findByUserId(userId: UsersEntity): List<WorkplanEntity>?
//    fun findByUserId(userId: UsersEntity, pages: Pageable?): Page<WorkplanEntity>?
}

@Repository
interface IFuelInspectionOfficerRepository : HazelcastRepository<MsFuelInspectionOfficersEntity, Long> {
    override fun findAll( pageable: Pageable): Page<MsFuelInspectionOfficersEntity>
    fun findAllByOrderByIdDesc( pageable: Pageable): Page<MsFuelInspectionOfficersEntity>
    fun findByMsFuelInspectionId(msFuelInspectionId: MsFuelInspectionEntity): MsFuelInspectionOfficersEntity?
    fun findByMsFuelInspectionIdAndStatus(msFuelInspectionId: MsFuelInspectionEntity, status: Int): MsFuelInspectionOfficersEntity?
//    fun findByUserId(userId: UsersEntity): List<WorkplanEntity>?
//    fun findByUserId(userId: UsersEntity, pages: Pageable?): Page<WorkplanEntity>?
}
