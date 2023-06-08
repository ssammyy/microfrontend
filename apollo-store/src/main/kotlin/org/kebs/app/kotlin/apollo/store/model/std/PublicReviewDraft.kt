package org.kebs.app.kotlin.apollo.store.model.std

import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "SD_PUBLIC_REVIEW_DRAFTS")
class PublicReviewDraft {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id: Long = 0

    @Column(name = "PRD_BY")
    @Basic
    var prdBy: Long = 0

    @Column(name = "PRD_NAME")
    @Basic
    var prdName: String? = null


    @Column(name = "CREATED_ON")
    @Basic
    var createdOn: Timestamp? = null

    @Column(name = "MODIFIED_ON")
    @Basic
    var modifiedOn: Timestamp? = null

    @Column(name = "DELETED_ON")
    @Basic
    var deletedOn: Timestamp? = null


    @Column(name = "CD_ID")
    @Basic
    var cdID: Long = 0

    @Column(name = "STATUS")
    @Basic
    var status: String? = null


    @Column(name = "CREATED_BY")
    @Basic
    var createdBy: String? = null

    @Column(name = "MODIFIED_BY")
    @Basic
    var modifiedBy: String? = null

    @Column(name = "DELETE_BY")
    @Basic
    var deleteBy: String? = null

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
    var previousVersion: String? = null

    @Column(name = "VAR_FIELD_8")
    @Basic
    var versionNumber: String? = null

    @Column(name = "VAR_FIELD_9")
    @Basic
    var originalVersion: String? = null

    @Column(name = "KS_NUMBER")
    @Basic
    var ksNumber: String? = null

    @Column(name = "STD_DRAFT_ID")
    @Basic
    var stdDraftId: Long? = 0

    @Column(name = "STD_ID")
    @Basic
    var stdId: Long? = 0

    @Column(name = "DRAFT_REVIEW_STATUS")
    @Basic
    var draftReviewStatus: Long? = 0

    @Column(name="ENCRYPTED_ID")
    @Basic
    var encrypted: String?=null

    @Column(name = "CIRCULATION_DATE")
    @Basic
    var circulationDate: Timestamp? = null

    @Column(name = "CLOSING_DATE")
    @Basic
    var closingDate: Timestamp? = null





}
