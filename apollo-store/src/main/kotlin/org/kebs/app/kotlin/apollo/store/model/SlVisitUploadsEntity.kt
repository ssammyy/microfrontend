package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_SL_VISIT_UPLOADS")
class SlVisitUploadsEntity : Serializable {
    @Id
    @SequenceGenerator(name = "DAT_KEBS_SL_VISIT_UPLOADS_SEQ_GEN", allocationSize = 1, sequenceName = "DAT_KEBS_SL_VISIT_UPLOADS_SEQ")
    @GeneratedValue(generator = "DAT_KEBS_SL_VISIT_UPLOADS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID", nullable = false)
    var id: Long? = null

    @Column(name = "FILEPATH")
    var filepath: String? = null

    @Column(name = "DESCRIPTION")
    var description: String? = null

    @Column(name = "NAME")
    var name: String? = null

    @Column(name = "FILE_TYPE")
    var fileType: String? = null

    @Column(name = "DOCUMENT_TYPE")
    var documentType: String? = null

    @Column(name = "DOCUMENT")
    var document: ByteArray? = null

    @Column(name = "TRANSACTION_DATE", nullable = false)
    var transactionDate: LocalDate? = null

    @Column(name = "VISIT_ID", nullable = false)
    var visitId: Long? = null

    @Column(name = "STATUS", nullable = false)
    var status: Int? = null

    @Column(name = "VAR_FIELD_1")
    var varField1: String? = null

    @Column(name = "VAR_FIELD_2")
    var varField2: String? = null

    @Column(name = "VAR_FIELD_3")
    var varField3: String? = null

    @Column(name = "VAR_FIELD_4")
    var varField4: String? = null

    @Column(name = "VAR_FIELD_5")
    var varField5: String? = null

    @Column(name = "VAR_FIELD_6")
    var varField6: String? = null

    @Column(name = "VAR_FIELD_7")
    var varField7: String? = null

    @Column(name = "VAR_FIELD_8")
    var varField8: String? = null

    @Column(name = "VAR_FIELD_9")
    var varField9: String? = null

    @Column(name = "VAR_FIELD_10")
    var varField10: String? = null

    @Column(name = "CREATED_BY", nullable = false)
    var createdBy: String? = null

    @Column(name = "CREATED_ON", nullable = false)
    var createdOn: LocalDateTime? = null

    @Column(name = "MODIFIED_BY")
    var modifiedBy: String? = null

    @Column(name = "MODIFIED_ON")
    var modifiedOn: LocalDateTime? = null

    @Column(name = "DELETE_BY")
    var deleteBy: String? = null

    @Column(name = "DELETED_ON")
    var deletedOn: LocalDateTime? = null

    companion object {
        private const val serialVersionUID = 1L
    }
}
