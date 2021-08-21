package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.StagingPaymentReconciliation
import org.kebs.app.kotlin.apollo.store.model.di.CdStatusTypesEntity
import org.kebs.app.kotlin.apollo.store.model.invoice.InvoiceBatchDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.invoice.LogStgPaymentReconciliationDetailsToSageEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository

interface IStagingPaymentReconciliationRepo : HazelcastRepository<StagingPaymentReconciliation, Long> {
    fun findByReferenceCode(referenceCode: String): StagingPaymentReconciliation?
    fun findByReferenceCodeAndInvoiceId(referenceCode: String, invoiceId: Long): StagingPaymentReconciliation?
    fun findByPaymentTablesUpdatedStatus(paymentTablesUpdatedStatus: Int): List<StagingPaymentReconciliation>?
}

@Repository
interface InvoiceBatchDetailsRepo : HazelcastRepository<InvoiceBatchDetailsEntity, Long> {
//    fun findByTypeNameAndStatus(typeName: String, status: Long): IInvoiceBatchDetailsEntityRepo?

//    fun findByStatus(status: Int): List<IInvoiceBatchDetailsEntityRepo>?
//    fun findAllById(Id: Long): List<ConsignmentDocumentTypesEntity>?
}

@Repository
interface ILogStgPaymentReconciliationDetailsToSageRepo :
    HazelcastRepository<LogStgPaymentReconciliationDetailsToSageEntity, Long> {
    fun findByStgPaymentId(stgPaymentId: Long): LogStgPaymentReconciliationDetailsToSageEntity?

//    fun findByStatus(status: Int): List<IInvoiceBatchDetailsEntityRepo>?
//    fun findAllById(Id: Long): List<ConsignmentDocumentTypesEntity>?
}
