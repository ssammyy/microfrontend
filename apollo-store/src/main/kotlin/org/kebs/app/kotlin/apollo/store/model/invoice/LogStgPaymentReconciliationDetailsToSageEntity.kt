package org.kebs.app.kotlin.apollo.store.model.invoice

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "LOG_STG_PAYMENT_RECONCILIATION_DETAILS_TO_SAGE")
class LogStgPaymentReconciliationDetailsToSageEntity : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(
        name = "LOG_STG_PAYMENT_RECONCILIATION_DETAILS_TO_SAGE_SEQ_GEN",
        allocationSize = 1,
        sequenceName = "LOG_STG_PAYMENT_RECONCILIATION_DETAILS_TO_SAGE_SEQ"
    )
    @GeneratedValue(
        generator = "LOG_STG_PAYMENT_RECONCILIATION_DETAILS_TO_SAGE_SEQ_GEN",
        strategy = GenerationType.SEQUENCE
    )
    @Id
    var id: Long? = 0

    @Column(name = "STG_PAYMENT_ID")
    @Basic
    var stgPaymentId: Long? = null

    @Column(name = "SERVICE_NAME")
    @Basic
    var serviceName: String? = null

    @Column(name = "REQUEST_MESSAGE_ID")
    @Basic
    var requestMessageId: String? = null

    @Column(name = "CONNECTION_ID")
    @Basic
    var connectionId: String? = null

    @Column(name = "CONNECTION_PASSWORD")
    @Basic
    var connectionPassword: String? = null

    @Column(name = "REQUEST_DOCUMENT_NO")
    @Basic
    var requestDocumentNo: String? = null

    @Column(name = "DOCUMENT_DATE")
    @Basic
    var documentDate: String? = null

    @Column(name = "DOC_TYPE")
    @Basic
    var docType: String? = null

    @Column(name = "CURRENCY_CODE")
    @Basic
    var currencyCode: String? = null

    @Column(name = "CUSTOMER_CODE")
    @Basic
    var customerCode: String? = null

    @Column(name = "CUSTOMER_NAME")
    @Basic
    var customerName: String? = null

    @Column(name = "INVOICE_DESC")
    @Basic
    var invoiceDesc: String? = null

    @Column(name = "REVENUE_ACC")
    @Basic
    var revenueAcc: String? = null

    @Column(name = "REVENUE_ACC_DESC")
    @Basic
    var revenueAccDesc: String? = null

    @Column(name = "TAXABLE")
    @Basic
    var taxable: String? = null

    @Column(name = "INVOICE_AMNT")
    @Basic
    var invoiceAmnt: String? = null

    @Column(name = "RESPONSE_MESSAGE_ID")
    @Basic
    var responseMessageId: String? = null

    @Column(name = "STATUS_CODE")
    @Basic
    var statusCode: String? = null

    @Column(name = "STATUS_DESCRIPTION")
    @Basic
    var statusDescription: String? = null

    @Column(name = "RESPONSE_DOCUMENT_NO")
    @Basic
    var responseDocumentNo: String? = null

    @Column(name = "RESPONSE_DATE")
    @Basic
    var responseDate: Timestamp? = null

    @Column(name = "DESCRIPTION")
    @Basic
    var description: String? = null

    @Column(name = "STATUS")
    @Basic
    var status: Long? = null

    @Column(name = "VAR_FIELD_1")
    @Basic
    var varField1: String? = null

    @Column(name = "VAR_FIELD_2")
    @Basic
    var varField2: String? = null

    @Column(name = "VAR_FIELD_3")
    @Basic
    var varField3: String? = null

    @Column(name = "VAR_FIELD_4")
    @Basic
    var varField4: String? = null

    @Column(name = "VAR_FIELD_5")
    @Basic
    var varField5: String? = null

    @Column(name = "VAR_FIELD_6")
    @Basic
    var varField6: String? = null

    @Column(name = "VAR_FIELD_7")
    @Basic
    var varField7: String? = null

    @Column(name = "VAR_FIELD_8")
    @Basic
    var varField8: String? = null

    @Column(name = "VAR_FIELD_9")
    @Basic
    var varField9: String? = null

    @Column(name = "VAR_FIELD_10")
    @Basic
    var varField10: String? = null

    @Column(name = "CREATED_BY")
    @Basic
    var createdBy: String? = null

    @Column(name = "CREATED_ON")
    @Basic
    var createdOn: Timestamp? = null

    @Column(name = "MODIFIED_BY")
    @Basic
    var modifiedBy: String? = null

    @Column(name = "MODIFIED_ON")
    @Basic
    var modifiedOn: Timestamp? = null

    @Column(name = "DELETE_BY")
    @Basic
    var deleteBy: String? = null

    @Column(name = "DELETED_ON")
    @Basic
    var deletedOn: Timestamp? = null
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as LogStgPaymentReconciliationDetailsToSageEntity
        return id == that.id && stgPaymentId == that.stgPaymentId && serviceName == that.serviceName && requestMessageId == that.requestMessageId && connectionId == that.connectionId && connectionPassword == that.connectionPassword && requestDocumentNo == that.requestDocumentNo && documentDate == that.documentDate && docType == that.docType && currencyCode == that.currencyCode && customerCode == that.customerCode && customerName == that.customerName && invoiceDesc == that.invoiceDesc && revenueAcc == that.revenueAcc && revenueAccDesc == that.revenueAccDesc && taxable == that.taxable && invoiceAmnt == that.invoiceAmnt && responseMessageId == that.responseMessageId && statusCode == that.statusCode && statusDescription == that.statusDescription && responseDocumentNo == that.responseDocumentNo && responseDate == that.responseDate && description == that.description && status == that.status && varField1 == that.varField1 && varField2 == that.varField2 && varField3 == that.varField3 && varField4 == that.varField4 && varField5 == that.varField5 && varField6 == that.varField6 && varField7 == that.varField7 && varField8 == that.varField8 && varField9 == that.varField9 && varField10 == that.varField10 && createdBy == that.createdBy && createdOn == that.createdOn && modifiedBy == that.modifiedBy && modifiedOn == that.modifiedOn && deleteBy == that.deleteBy && deletedOn == that.deletedOn
    }

    override fun hashCode(): Int {
        return Objects.hash(
            id,
            stgPaymentId,
            serviceName,
            requestMessageId,
            connectionId,
            connectionPassword,
            requestDocumentNo,
            documentDate,
            docType,
            currencyCode,
            customerCode,
            customerName,
            invoiceDesc,
            revenueAcc,
            revenueAccDesc,
            taxable,
            invoiceAmnt,
            responseMessageId,
            statusCode,
            statusDescription,
            responseDocumentNo,
            responseDate,
            description,
            status,
            varField1,
            varField2,
            varField3,
            varField4,
            varField5,
            varField6,
            varField7,
            varField8,
            varField9,
            varField10,
            createdBy,
            createdOn,
            modifiedBy,
            modifiedOn,
            deleteBy,
            deletedOn
        )
    }
}