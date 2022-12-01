package org.kebs.app.kotlin.apollo.store.model.di

import java.io.Serializable
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_CONSIGNMENT_DOCUMENT_HISTORY")
class CdDocumentModificationHistory : Serializable {
    @Column(name = "ID")
    @Id
    @SequenceGenerator(name = "DAT_CD_HISTORY_SEQ_GEN", sequenceName = "DAT_CD_HISTORY_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_CD_HISTORY_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long = 0

    @Column(name = "CD_ID")
    @Basic
    var cdId: Long? = null

    @Column(name = "UCR_NUMBER")
    @Basic
    var ucrNumber: String? = null

    @Column(name = "USER_COMMENT", length = 4000)
    @Basic
    var comment: String? = null

    @Column(name = "COMMENTER")
    @Basic
    var name: String? = null

    @Column(name = "ACTION_CODE")
    @Basic
    var actionCode: String? = null

    @Column(name = "USER_DESCRIPTION", length = 4000)
    @Basic
    var description: String? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null

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