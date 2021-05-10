package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.InvoiceEntity
import org.kebs.app.kotlin.apollo.store.model.ManufacturersEntity
import org.kebs.app.kotlin.apollo.store.model.PermitApplicationEntity
import org.kebs.app.kotlin.apollo.store.model.PetroleumInstallationInspectionEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository


@Repository
interface IInvoiceRepository : HazelcastRepository<InvoiceEntity, Long> {
    fun findByPermitId(permitId: Long): InvoiceEntity?
    fun findByPermitIdAndUserId(permitId: Long, userId: Long): InvoiceEntity?
    fun findAllByStatus(status: Long): List<InvoiceEntity>?
    fun findAllByUserIdAndStatus(userId: Long, status: Int): List<InvoiceEntity>?
    fun findByStatus(status: Long, pages: Pageable): Page<InvoiceEntity>?
    /*
    fun findAllByManufacturer(manufacturer: ManufacturersEntity, page: Pageable): Page<InvoiceEntity>?
    fun findAllByManufacturer(manufacturer: ManufacturersEntity): List<InvoiceEntity>?
    */
    fun findAllByManufacturer(manufacturer: Long, page: Pageable): Page<InvoiceEntity>?
    fun findAllByManufacturer(manufacturer: Long): List<InvoiceEntity>?

    fun findByInstallationInspectionId(installationInspectionId: PetroleumInstallationInspectionEntity): InvoiceEntity?
}
