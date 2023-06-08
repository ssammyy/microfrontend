package org.kebs.app.kotlin.apollo.store.model.pvc

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_PVOC_QUERY_RESPONSES")
class PvocQueryResponseEntity : Serializable {
    @Column(name = "ID", nullable = false, precision = 0)
    @Id
    @SequenceGenerator(name = "DAT_KEBS_PVOC_QUERY_RESPONSES_SEQ_GEN", sequenceName = "DAT_KEBS_PVOC_QUERY_RESPONSES_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_PVOC_QUERY_RESPONSES_SEQ_GEN", strategy = GenerationType.SEQUENCE)

    var id: Long = 0

    @JoinColumn(name = "QUERY_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    var queryId: PvocQueriesEntity? = null

    @Column(name = "SERIAL_NUMBER", nullable = true, length = 4000)
    @Basic
    var serialNumber: String? = null

    @Column(name = "QUERY_RESPONSE", nullable = true, length = 4000)
    @Basic
    var response: String? = null

    @Column(name = "RESPONSE_SOURCE", nullable = true)
    @Basic
    var responseFrom: String? = null

    @Column(name = "LINK_TO_UPLOADS", nullable = true, length = 1024)
    @Basic
    var linkToUploads: String? = null

    @Column(name = "RESPONSE_STATUS", nullable = true, precision = 0)
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as PvocQueryResponseEntity
        return id == that.id &&
                queryId == that.queryId &&
                linkToUploads == that.linkToUploads &&
                status == that.status &&
                varField1 == that.varField1 &&
                varField2 == that.varField2 &&
                varField3 == that.varField3 &&
                varField4 == that.varField4 &&
                createdBy == that.createdBy &&
                createdOn == that.createdOn &&
                modifiedBy == that.modifiedBy &&
                modifiedOn == that.modifiedOn &&
                deleteBy == that.deleteBy &&
                deletedOn == that.deletedOn
    }

    override fun hashCode(): Int {
        return Objects.hash(
                id,
                queryId,
                linkToUploads,
                status,
                varField1,
                varField2,
                varField3,
                varField4,
                createdBy,
                createdOn,
                modifiedBy,
                modifiedOn,
                deleteBy,
                deletedOn
        )
    }
}