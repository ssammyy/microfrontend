package org.kebs.app.kotlin.apollo.store.model.std

import org.kebs.app.kotlin.apollo.store.model.std.DatKebsSdNwaUploadsEntity
import java.io.Serializable
import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "SD_NEP_DRAFT_UPLOADS")
class SdNepDraftUploadsEntity: Serializable {
    @Column(name = "ID")
    @SequenceGenerator(
        name = "SD_NEP_DRAFT_UPLOADS_SEQ_GEN",
        allocationSize = 1,
        sequenceName = "SD_NEP_DRAFT_UPLOADS_SEQ"
    )
    @GeneratedValue(generator = "SD_NEP_DRAFT_UPLOADS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
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

    @Column(name = "NEP_DRAFT_ID")
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
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as SdNepDraftUploadsEntity
        return id == that.id && filepath == that.filepath && name == that.name && fileType == that.fileType && documentType == that.documentType && Arrays.equals(
            document,
            that.document
        ) && transactionDate == that.transactionDate && nepDraftId == that.nepDraftId && description == that.description && status == that.status  && createdBy == that.createdBy && createdOn == that.createdOn && modifiedBy == that.modifiedBy && modifiedOn == that.modifiedOn && deleteBy == that.deleteBy && deletedOn == that.deletedOn
    }

    override fun hashCode(): Int {
        var result = Objects.hash(
            id,
            filepath,
            name,
            fileType,
            documentType,
            transactionDate,
            nepDraftId,
            description,
            status,
            createdBy,
            createdOn,
            modifiedBy,
            modifiedOn,
            deleteBy,
            deletedOn
        )
        result = 31 * result + Arrays.hashCode(document)
        return result
    }
}
