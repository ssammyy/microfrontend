package org.kebs.app.kotlin.apollo.store.model.pvc

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_PVOC_QUERIES")
class PvocQueriesEntity : Serializable {
    @Column(name = "ID", nullable = false, precision = 0)
    @Id
    @SequenceGenerator(name = "DAT_KEBS_PVOC_QUERIES_SEQ_GEN", sequenceName = "DAT_KEBS_PVOC_QUERIES_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_PVOC_QUERIES_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long? = 0

    @Column(name = "PARTNER_ID", nullable = true, length = 4000)
    @Basic
    var partnerId: Long? = null

    @Column(name = "SERIAL_NUMBER", nullable = true, length = 50)
    @Basic
    var serialNumber: String? = null

    @Column(name = "CERT_NUMBER", nullable = true, length = 50)
    @Basic
    var certNumber: String? = null

    @Column(name = "CERT_TYPE", nullable = true, length = 50)
    @Basic
    var certType: String? = null

    @Column(name = "QUERY_ORIGIN", nullable = true, length = 50)
    @Basic
    var queryOrigin: String? = null

    @Column(name = "RFC_NUMBER", nullable = true, length = 50)
    @Basic
    var rfcNumber: String? = null

    @Column(name = "IDF_NUMBER", nullable = true, length = 50)
    @Basic
    var idfNumber: String? = null

    @Column(name = "INVOICE_NUMBER", nullable = true, length = 50)
    @Basic
    var invoiceNumber: String? = null

    @Column(name = "UCR_NUMBER", nullable = false, length = 50)
    @Basic
    var ucrNumber: String? = null

    @Column(name = "QUERY_DETAILS", nullable = true, length = 4000)
    @Basic
    var queryDetails: String? = null

    @Column(name = "QUERY_RESPONSE", nullable = true, length = 4000)
    @Basic
    var queryResponse: String? = null

    @Column(name = "PARTNER_RESPONSE", nullable = true, length = 4000)
    @Basic
    var partnerResponse: String? = null

    @Column(name = "RESPONSE_ANALYSIS", nullable = true, length = 4000)
    @Basic
    var responseAnalysis: String? = null

    @Column(name = "CONCLUSION", nullable = true, length = 4000)
    @Basic
    var conclusion: String? = null

    @Column(name = "CONCLUSION_DATE", nullable = true)
    var conclusionDate: Timestamp? = null

    @Column(name = "LINK_TO_UPLOADS", nullable = true, length = 4000)
    @Basic
    var linkToUploads: String? = null

    @Column(name = "STATUS", nullable = true, precision = 0)
    @Basic
    var status: Long? = null

    @Column(name = "VAR_FIELD_1", nullable = true, length = 350)
    @Basic
    var varField1: String? = null

    @Column(name = "VAR_FIELD_2", nullable = true, length = 350)
    @Basic
    var varField2: String? = null

    @Column(name = "VAR_FIELD_3", nullable = true, length = 350)
    @Basic
    var varField3: String? = null

    @Column(name = "VAR_FIELD_4", nullable = true, length = 350)
    @Basic
    var varField4: String? = null

    @Column(name = "VAR_FIELD_5", nullable = true, length = 350)
    @Basic
    var varField5: String? = null

    @Column(name = "VAR_FIELD_6", nullable = true, length = 350)
    @Basic
    var varField6: String? = null

    @Column(name = "VAR_FIELD_7", nullable = true, length = 350)
    @Basic
    var varField7: String? = null

    @Column(name = "VAR_FIELD_8", nullable = true, length = 350)
    @Basic
    var varField8: String? = null

    @Column(name = "VAR_FIELD_9", nullable = true, length = 350)
    @Basic
    var varField9: String? = null

    @Column(name = "VAR_FIELD_10", nullable = true, length = 350)
    @Basic
    var varField10: String? = null

    @Column(name = "CREATED_BY", nullable = true, length = 100)
    @Basic
    var createdBy: String? = null

    @Column(name = "CREATED_ON", nullable = true)
    @Basic
    var createdOn: Timestamp? = null

    @Column(name = "MODIFIED_BY", nullable = true, length = 100)
    @Basic
    var modifiedBy: String? = null

    @Column(name = "MODIFIED_ON", nullable = true)
    @Basic
    var modifiedOn: Timestamp? = null

    @Column(name = "DELETE_BY", nullable = true, length = 100)
    @Basic
    var deleteBy: String? = null

    @Column(name = "DELETED_ON", nullable = true)
    @Basic
    var deletedOn: Timestamp? = null

    @Column(name = "PVOC_AGENT_REPLY_STATUS", nullable = true)
    @Basic
    var pvocAgentReplyStatus: Int? = null

    @Column(name = "KEBS_REPLY_STATUS", nullable = true)
    @Basic
    var kebsReplyReplyStatus: Int? = null

    @Column(name = "CONCLUSION_STATUS", nullable = true)
    @Basic
    var conclusionStatus: Int? = null

    @OneToMany(mappedBy = "queryId")
    var responses: Set<PvocQueryResponseEntity>? = null
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as PvocQueriesEntity
        return id == that.id &&
                certNumber == that.certNumber &&
                rfcNumber == that.rfcNumber &&
                idfNumber == that.idfNumber &&
                invoiceNumber == that.invoiceNumber &&
                ucrNumber == that.ucrNumber &&
                partnerResponse == that.partnerResponse &&
                conclusion == that.conclusion &&
                linkToUploads == that.linkToUploads &&
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
                pvocAgentReplyStatus == that.pvocAgentReplyStatus &&
                kebsReplyReplyStatus == that.kebsReplyReplyStatus &&
                conclusionStatus == that.conclusionStatus &&
                deletedOn == that.deletedOn
    }

    override fun hashCode(): Int {
        return Objects.hash(
                id,
                certNumber,
                rfcNumber,
                idfNumber,
                invoiceNumber,
                ucrNumber,
                partnerResponse,
                conclusion,
                linkToUploads,
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
                deletedOn,
                pvocAgentReplyStatus,
                kebsReplyReplyStatus
        )
    }
}
