package org.kebs.app.kotlin.apollo.store.model.std

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "SD_NWI")
class StandardNWI {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id: Long = 0

    @Transient
    @JsonProperty("taskId")
    var taskId: String? = null

    @Column(name = "PROPOSAL_TITLE")
    @Basic
    var proposalTitle: String? = null

    @Column(name = "SCOPE")
    @Basic
    var scope: String? = null

    @Column(name = "PURPOSE")
    @Basic
    var purpose: String? = null

    @Column(name = "TARGET_DATE")
    @Basic
    var targetDate: String? = null

    @Column(name = "SIMILAR_STANDARDS")
    @Basic
    var similarStandards: String? = null

    @Column(name = "LIAISON_ORGANIZATION")
    @Basic
    var liaisonOrganisation: String? = null


    @Column(name = "DRAFT_ATTACHED")
    @Basic
    var draftAttached: String? = null

    @Column(name = "OUTLINE_ATTACHED")
    @Basic
    var outlineAttached: String? = null

    @Column(name = "DRAFT_OUTLINE_IMPOSSIBLE")
    @Basic
    var draftOutlineImpossible: String? = null

    @Column(name = "OUTLINE_SENT_LATER")
    @Basic
    var outlineSentLater: String? = null

    @Column(name = "NAME_OF_PROPOSER")
    @Basic
    var nameOfProposer: String? = null

    @Column(name = "ORGANIZATION")
    @Basic
    var organization: String? = null

    @Column(name = "CIRCULATION_DATE")
    @Basic
    var circulationDate: String? = null

    @Column(name = "CLOSING_DATE")
    @Basic
    var closingDate: String? = null

    @Column(name = "DATE_OF_PRESENTATION")
    @Basic
    var dateOfPresentation: String? = null

    @Column(name = "NAME_OF_TC")
    @Basic
    var nameOfTC: String? = null

    @Column(name = "NAME_OF_DEPARTMENT")
    @Basic
    var nameOfDepartment: String? = null

    @Column(name = "TC_ID")
    @Basic
    var tcId: String? = null


    @Column(name = "DEPARTMENT_ID")
    @Basic
    var departmentId: Long? = 0


    @Column(name = "REFERENCE_NUMBER")
    @Basic
    var referenceNumber: String? = null

    @Column(name = "CREATED_ON")
    @Basic
    var createdOn: Timestamp? = null

    @Column(name = "TC_SEC")
    @Basic
    var tcSec: String? = null

    @Column(name = "MODIFIED_ON")
    @Basic
    var modifiedOn: Timestamp? = null

    @Column(name = "DELETED_ON")
    @Basic
    var deletedOn: Timestamp? = null

    @Column(name = "STATUS")
    @Basic
    var status: String? = null

    @Column(name = "PROCESS_STATUS")
    @Basic
    var processStatus: String? = null

    @Column(name = "STANDARD_ID")
    @Basic
    var standardId: Long? = null

    @Column(name = "PD_STATUS")
    @Basic
    var pdStatus: String? = null

    @Column(name = "MINUTES_PD_STATUS")
    @Basic
    var minutesPdStatus: String? = null


    @Column(name = "DRAFT_DOCUMENTS_PD_STATUS")
    @Basic
    var draftDocsPdStatus: String? = null


    @Column(name = "PRELIMINARY_DOCUMENTS_PD_STATUS")
    @Basic
    var prPdStatus: String? = null

    @Column(name = "DEFERRED_DATE")
    @Basic
    var deferredDate: Timestamp? = null

    @Column(name = "APPROVAL_DATE")
    @Basic
    var approvalDate: Timestamp? = null


}
