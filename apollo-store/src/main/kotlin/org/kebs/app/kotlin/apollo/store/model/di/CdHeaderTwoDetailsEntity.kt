package org.kebs.app.kotlin.apollo.store.model.di

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_CD_HEADER_TWO_DETAILS")
class CdHeaderTwoDetailsEntity : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_CD_HEADER_TWO_DETAILS_SEQ_GEN", sequenceName = "DAT_KEBS_CD_HEADER_TWO_DETAILS_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_CD_HEADER_TWO_DETAILS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long = 0

    @Column(name = "TERMS_OF_PAYMENT")
    @Basic
    var termsOfPayment: String? = null

    @Column(name = "TERMS_OF_PAYMENT_DESC")
    @Basic
    var termsOfPaymentDesc: String? = null

    @Column(name = "LOCAL_BANK_CODE")
    @Basic
    var localBankCode: String? = null

    @Column(name = "LOCAL_BANK_DESC")
    @Basic
    var localBankDesc: String? = null

    @Column(name = "RECEIPT_OF_REMITTANCE")
    @Basic
    var receiptOfRemittance: String? = null

    @Column(name = "REMITTANCE_CURRENCY")
    @Basic
    var remittanceCurrency: String? = null

    @Column(name = "REMITTANCE_AMOUNT")
    @Basic
    var remittanceAmount: String? = null

    @Column(name = "REMITTANCE_DATE")
    @Basic
    var remittanceDate: String? = null

    @Column(name = "REMITTANCE_REFERENCE")
    @Basic
    var remittanceReference: String? = null

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
        val that = other as CdHeaderTwoDetailsEntity
        return id == that.id &&
                termsOfPayment == that.termsOfPayment &&
                termsOfPaymentDesc == that.termsOfPaymentDesc &&
                localBankCode == that.localBankCode &&
                localBankDesc == that.localBankDesc &&
                receiptOfRemittance == that.receiptOfRemittance &&
                remittanceCurrency == that.remittanceCurrency &&
                remittanceAmount == that.remittanceAmount &&
                remittanceDate == that.remittanceDate &&
                remittanceReference == that.remittanceReference &&
                description == that.description &&
                status == that.status &&
                varField1 == that.varField1 &&
                varField2 == that.varField2 &&
                varField3 == that.varField3 &&
                varField4 == that.varField4 &&
                varField5 == that.varField5 &&
                varField6 == that.varField6 &&
                varField7 == that.varField7 &&
                varField8 == that.varField8 &&
                varField9 == that.varField9 &&
                varField10 == that.varField10 &&
                createdBy == that.createdBy &&
                createdOn == that.createdOn &&
                modifiedBy == that.modifiedBy &&
                modifiedOn == that.modifiedOn &&
                deleteBy == that.deleteBy &&
                deletedOn == that.deletedOn
    }

    override fun hashCode(): Int {
        return Objects.hash(id, termsOfPayment, termsOfPaymentDesc, localBankCode, localBankDesc, receiptOfRemittance, remittanceCurrency, remittanceAmount, remittanceDate, remittanceReference, description, status, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn)
    }
}