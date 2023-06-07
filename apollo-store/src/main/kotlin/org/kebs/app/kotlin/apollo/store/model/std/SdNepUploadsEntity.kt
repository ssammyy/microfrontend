package org.kebs.app.kotlin.apollo.store.model.std

import org.kebs.app.kotlin.apollo.store.model.std.DatKebsSdNwaUploadsEntity
import java.io.Serializable
import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "SD_NEP_UPLOADS")
class SdNepUploadsEntity: Serializable {
    @Column(name = "ID")
    @SequenceGenerator(
        name = "SD_NEP_UPLOADS_SEQ_GEN",
        allocationSize = 1,
        sequenceName = "SD_NEP_UPLOADS_SEQ"
    )
    @GeneratedValue(generator = "SD_NEP_UPLOADS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = 0

    @Column(name = "FILEPATH")
    @Basic
    var filepath: String? = null

    @Column(name = "NAME")
    @Basic
    var name: String? = null

    @Column(name = "FILE_TYPE")
    @Basic
    var fileType: String? = null

    @Column(name = "DOCUMENT_TYPE")
    @Basic
    var documentType: String? = null

    @Column(name = "DOCUMENT")
    @Lob
    var document: ByteArray? = null

    @Column(name = "TRANSACTION_DATE")
    @Basic
    var transactionDate: Date? = null

    @Column(name = "NEP_DOCUMENT_ID")
    @Basic
    var nepDraftId: Long? = null

    @Column(name = "DESCRIPTION")
    @Basic
    var description: String? = null

    @Column(name = "STATUS")
    @Basic
    var status: Long? = null


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

}
