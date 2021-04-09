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
import org.kebs.app.kotlin.apollo.store.model.ms.ComplaintCustomersEntity
import org.kebs.app.kotlin.apollo.store.model.ms.ComplaintEntity
import org.kebs.app.kotlin.apollo.store.model.ms.ComplaintLocationEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository
import java.sql.Date

@Repository
interface IComplaintRepository : HazelcastRepository<ComplaintEntity, Long>{
    override fun findAll(): List<ComplaintEntity>
    fun findAllByOrderByIdDesc(pageable: Pageable): Page<ComplaintEntity>
    fun findByHodAssigned(hodAssigned: Long): List<ComplaintEntity>
    fun findByHofAssigned(hofAssigned: Long): List<ComplaintEntity>
    fun findByAssignedIo(assignedIo: Long): List<ComplaintEntity>

    //    fun findByIdAndMsProcessStatus(id: Long, msProcessStatus: Int, pageable: Pageable):  Page<ComplaintEntity>?
    fun findByIdAndMsProcessStatus(id: Long, msProcessStatus: Int): ComplaintEntity?
    fun findByUuid(uuid: String): ComplaintEntity?
    fun findByReferenceNumber(referenceNumber: String): ComplaintEntity?

//    fun findAllByUserNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(userName: String?, email: String?, firstName: String?, lastName: String?): List<ComplaintEntity>?

//    fun findAllByReferenceNumberContainingIgnoreCaseOrTransactionDateContainingIgnoreCase(
//        referenceNumber: String,
//        transactionDate: Date
//    ): List<ComplaintEntity>?
}

@Repository
interface IComplaintAcceptWorkFlowRepository : HazelcastRepository<ComplaintAcceptWorkFlowEntity, Long>

@Repository
interface IComplaintCompanyRepository : HazelcastRepository<ComplaintCompanyEntity, Long>

@Repository
interface IComplaintDocumentsRepository : HazelcastRepository<ComplaintDocumentsEntity, Long>{
    fun findByComplaintId(complaintId: Long): ComplaintDocumentsEntity?
}

@Repository
interface IComplaintCustomersRepository : HazelcastRepository<ComplaintCustomersEntity, Long>{
    fun findByComplaintId(complaintId: Long): ComplaintCustomersEntity?
}

@Repository
interface IComplaintHandlersRepository : HazelcastRepository<ComplaintHandlersEntity, Long>

@Repository
interface IComplaintKebsRemarksRepository : HazelcastRepository<ComplaintKebsRemarksEntity, Long>

@Repository
interface IComplaintOfficerRepository : HazelcastRepository<ComplaintOfficerEntity, Long>

@Repository
interface IComplaintRemarksRepository : HazelcastRepository<ComplaintRemarksEntity, Long>

@Repository
interface IComplaintRemediesRepository : HazelcastRepository<ComplaintRemediesEntity, Long>

@Repository
interface IComplaintWitnessRepository : HazelcastRepository<ComplaintWitnessEntity, Long>

@Repository
interface IComplaintLocationRepository : HazelcastRepository<ComplaintLocationEntity, Long>{
    fun findByComplaintId(complaintId: Long): ComplaintLocationEntity?
}