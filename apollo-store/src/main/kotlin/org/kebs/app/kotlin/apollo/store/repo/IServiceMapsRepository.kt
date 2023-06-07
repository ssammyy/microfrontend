/*
 *
 * $$$$$$$\   $$$$$$\  $$\   $$\        $$$$$$\  $$\       $$$$$$\  $$$$$$$\   $$$$$$\  $$\
 * $$  __$$\ $$  __$$\ $$ | $$  |      $$  __$$\ $$ |     $$  __$$\ $$  __$$\ $$  __$$\ $$ |
 * $$ |  $$ |$$ /  \__|$$ |$$  /       $$ /  \__|$$ |     $$ /  $$ |$$ |  $$ |$$ /  $$ |$$ |
 * $$$$$$$\ |\$$$$$\  $$$$$  /        $$ |$$$$\ $$ |     $$ |  $$ |$$$$$$$\ |$$$$$$$$ |$$ |
 * $$  __$$\  \____$$\ $$  $$<         $$ |\_$$ |$$ |     $$ |  $$ |$$  __$$\ $$  __$$ |$$ |
 * $$ |  $$ |$$\   $$ |$$ |\$\        $$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |$$ |  $$ |$$ |
 * $$$$$$$  |\$$$$$  |$$ | \$\       \$$$$$  |$$$$$$$$\ $$$$$$  |$$$$$$$  |$$ |  $$ |$$$$$$$$\
 * \_______/  \______/ \__|  \__|       \______/ \________|\______/ \_______/ \__|  \__|\________|
 * $$$$$$$$\ $$$$$$$$\  $$$$$$\  $$\   $$\ $$\   $$\  $$$$$$\  $$\       $$$$$$\   $$$$$$\  $$$$$$\ $$$$$$$$\  $$$$$$\
 * \__$$  __|$$  _____|$$  __$$\ $$ |  $$ |$$$\  $$ |$$  __$$\ $$ |     $$  __$$\ $$  __$$\ \_$$  _|$$  _____|$$  __$$\
 *    $$ |   $$ |      $$ /  \__|$$ |  $$ |$$$$\ $$ |$$ /  $$ |$$ |     $$ /  $$ |$$ /  \__|  $$ |  $$ |      $$ /  \__|
 *    $$ |   $$$$$\    $$ |      $$$$$$$$ |$$ $$\$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |$$$$\   $$ |  $$$$$\    \$$$$$\
 *    $$ |   $$  __|   $$ |      $$  __$$ |$$ \$$$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |\_$$ |  $$ |  $$  __|    \____$$\
 *    $$ |   $$ |      $$ |  $$\ $$ |  $$ |$$ |\$$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |  $$ |  $$ |      $$\   $$ |
 *    $$ |   $$$$$$$$\ \$$$$$  |$$ |  $$ |$$ | \$ | $$$$$$  |$$$$$$$$\ $$$$$$  |\$$$$$  |$$$$$$\ $$$$$$$$\ \$$$$$  |
 *    \__|   \________| \______/ \__|  \__|\__|  \__| \______/ \________|\______/  \______/ \______|\________| \______/
 *
 *
 *
 *
 *
 *   Copyright (c) 2020.  BSK
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository


@Repository
interface IServiceMapsRepository : HazelcastRepository<ServiceMapsEntity, Int> {
    fun findByServiceTopic(serviceTopic: String): ServiceMapsEntity?
    fun findByIdAndStatus(id: Int?, status: Int): ServiceMapsEntity?
}

@Repository
interface IServiceRequestsRepository : HazelcastRepository<ServiceRequestsEntity, Long> {
    fun findFirstByTransactionReference(transactionReference: String): ServiceRequestsEntity?
}


@Repository
interface IWorkflowTransactionsRepository : HazelcastRepository<WorkflowTransactionsEntity, Long>


interface FileStats {
    fun getTotalDocuments(): Long?
    fun getFlowDirection(): String?
    fun getFileType(): String?
    fun getResponseStatus(): Int?

}

@Repository
interface ISftpTransmissionEntityRepository : HazelcastRepository<SftpTransmissionEntity, Long> {
    fun findFirstByFilenameOrderByCreatedOn(fileName: String): SftpTransmissionEntity?
    fun findFirstByVarField10(exchangeId: String): SftpTransmissionEntity?
    fun findFirstByTransactionReference(exchangeId: String): SftpTransmissionEntity?
    fun findFirstByFilenameContainingOrderByTransactionDateDesc(fileName: String, pageable: Pageable): Page<SftpTransmissionEntity>
    fun findByTransactionStatusInAndFlowDirectionOrderByTransactionDateDesc(statuses: List<Int>, flowDirection: String?, pageable: Pageable): Page<SftpTransmissionEntity>
    fun findByTransactionStatusNotInAndFlowDirectionOrderByTransactionDateDesc(statuses: List<Int>, flowDirection: String?, pageable: Pageable): Page<SftpTransmissionEntity>

    @Query("select count(*) as totalDocuments, FLOW_DIRECTION as flowDirection,RESPONSE_STATUS as responseStatus,FILE_TYPE as fileType from LOG_SFTP_TRANSMISSION where to_char(TRANSACTION_DATE,'DD-MM-YYYY')=:dateOfRef group by FLOW_DIRECTION, RESPONSE_STATUS,FILE_TYPE", nativeQuery = true)
    fun findStatisticsForDate(@Param("dateOfRef") date: String): List<FileStats>
}

@Repository
interface IServiceMapsWorkflowEventsRepository : HazelcastRepository<ServiceMapsWorkflowEventsEntity, Long>

@Repository
interface IServiceMapsWorkflowsRepository : HazelcastRepository<ServiceMapsWorkflowsEntity, Long>

@Repository
interface IIntegrationConfigurationRepository : HazelcastRepository<IntegrationConfigurationEntity, Long> {
    fun findByWorkflowId(workflowId: Long): IntegrationConfigurationEntity?
    fun findByConfigKeyword(configKeyworkd: String): IntegrationConfigurationEntity?
}

@Repository
interface IServiceMapsWorkflowsFunctionsVwRepository : HazelcastRepository<ServiceMapsWorkflowsFunctionsVwEntity, Long> {
    fun findFirstByWorkflowId(workflowId: Long): ServiceMapsWorkflowsFunctionsVwEntity?

    fun findByServiceMapOrderBySequenceNumber(serviceMap: Int): List<ServiceMapsWorkflowsFunctionsVwEntity>?
}

@Repository
interface IKraPinValidationsRepository : HazelcastRepository<KraPinValidations, Long>, JpaSpecificationExecutor<KraPinValidations> {
    fun findFirstByKraPinAndStatusOrderByIdDesc(kraPin: String, status: Int?): KraPinValidations?
}

@Repository
interface ICertifiedProductsDetailsRepository : JpaRepository<CertifiedProductsDetailsEntity, Long>, JpaSpecificationExecutor<CertifiedProductsDetailsEntity>
