package org.kebs.app.kotlin.apollo.store.model.di

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_CD_DOCUMENT_HEADER")
class CdDocumentHeaderEntity : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_CD_DOCUMENT_HEADER_SEQ_GEN", sequenceName = "DAT_KEBS_CD_DOCUMENT_HEADER_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_CD_DOCUMENT_HEADER_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long = 0

    @Column(name = "DOCUMENT_TYPE")
    @Basic
    var documentType: String? = null

    @Column(name = "DOCUMENT_NAME")
    @Basic
    var documentName: String? = null

    @Column(name = "DOCUMENT_NUMBER")
    @Basic
    var documentNumber: String? = null

    @Column(name = "COMMON_REF_NUMBER")
    @Basic
    var commonRefNumber: String? = null

    @Column(name = "MESSAGE_TYPE")
    @Basic
    var messageType: String? = null

    @Column(name = "SENDER_ID")
    @Basic
    var senderId: String? = null

    @Column(name = "REGIME_CODE")
    @Basic
    var regimeCode: String? = null

    @Column(name = "CMS_REGIME_CODE")
    @Basic
    var cmsRegimeCode: String? = null

    @Column(name = "APPROVAL_CATEGORY")
    @Basic
    var approvalCategory: String? = null

    @Column(name = "RECEIVING_PARTY")
    @Basic
    var receivingParty: String? = null

    @Column(name = "NOTIFY_PARTY")
    @Basic
    var notifyParty: String? = null

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
        val that = other as CdDocumentHeaderEntity
        return id == that.id &&
                documentType == that.documentType &&
                documentName == that.documentName &&
                documentNumber == that.documentNumber &&
                commonRefNumber == that.commonRefNumber &&
                messageType == that.messageType &&
                senderId == that.senderId &&
                regimeCode == that.regimeCode &&
                cmsRegimeCode == that.cmsRegimeCode &&
                approvalCategory == that.approvalCategory &&
                receivingParty == that.receivingParty &&
                notifyParty == that.notifyParty &&
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
        return Objects.hash(id, documentType, documentName, documentNumber, commonRefNumber, messageType, senderId, regimeCode, cmsRegimeCode, approvalCategory, receivingParty, notifyParty, description, status, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn)
    }
}