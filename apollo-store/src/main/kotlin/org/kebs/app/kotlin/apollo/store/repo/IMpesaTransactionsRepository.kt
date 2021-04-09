package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.CocItemsEntity
import org.kebs.app.kotlin.apollo.store.model.InvoiceEntity
import org.kebs.app.kotlin.apollo.store.model.MpesaTransactionEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository

@Repository
interface IMpesaTransactionsRepository : HazelcastRepository<MpesaTransactionEntity, Long> {
    fun findByInvoiceIdAndStatus(invoiceId: Long?, status: Int): MpesaTransactionEntity?
    fun findByMpesareceiptnumberAndUsedTransactionReference(mpesareceiptnumber: String, usedTransactionReference: Int): MpesaTransactionEntity?
    fun findByMpesareceiptnumber(mpesareceiptnumber: String): MpesaTransactionEntity?
    fun findByMerchantRequestIdAndCheckoutRequestId(merchantRequestId: String, checkoutRequestId: String): MpesaTransactionEntity?
    fun findByMpesareceiptnumberAndInvoiceId(mpesareceiptnumber: String, invoiceId:Long): MpesaTransactionEntity?
    fun findByInvoiceIdAndInvoiceSource(invoiceId: Long, invoiceSource:String) : MpesaTransactionEntity?
//    fun findByInvoiceIDAndStatus(invoiceID: InvoiceEntity, status: Int): MpesaTransactionEntity?
}