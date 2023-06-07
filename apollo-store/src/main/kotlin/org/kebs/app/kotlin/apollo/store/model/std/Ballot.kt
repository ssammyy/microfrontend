package org.kebs.app.kotlin.apollo.store.model.std

import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "BALLOT")
class Ballot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id: Long = 0

    @Column(name = "PRD_ID")
    @Basic
    var prdID: Long = 0

    @Column(name = "BALLOT_NAME")
    @Basic
    var ballotName: String? = null

    @Column(name = "BALLOT_DRAFT_BY")
    @Basic
    var ballotDraftBy: String? = null

    @Column(name = "FDKS_NUMBER")
    @Basic
    var fdksNumber: String? = null


    @Column(name = "CREATED_ON")
    @Basic
    var createdOn: Timestamp? = null

    @Column(name = "MODIFIED_ON")
    @Basic
    var modifiedOn: Timestamp? = null

    @Column(name = "DELETED_ON")
    @Basic
    var deletedOn: Timestamp? = null

    @Column(name = "CREATED_BY")
    @Basic
    var createdBy: String? = null

    @Column(name = "MODIFIED_BY")
    @Basic
    var modifiedBy: String? = null

    @Column(name = "DELETE_BY")
    @Basic
    var deleteBy: String? = null


    @Column(name = "APPROVAL_STATUS")
    @Basic
    var approvalStatus: String? = null

    @Column(name = "VAR_FIELD_2")
    @Basic
    var varField2: String? = null

    @Column(name = "VAR_FIELD_1")
    @Basic
    var varField1: String? = null


    @Column(name = "STATUS")
    @Basic
    var status: String? = null

    @Column(name = "EDITED_STATUS")
    @Basic
    var editedStatus: String? = null
}
