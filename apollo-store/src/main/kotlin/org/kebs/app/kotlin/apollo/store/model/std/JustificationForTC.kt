package org.kebs.app.kotlin.apollo.store.model.std

import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "SD_JUSTIFICATION_FOR_TC")
class JustificationForTC {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id: Long = 0

    @Column(name = "SUBJECT")
    @Basic
    var subject: String? = null

    @Column(name = "PROPOSER")
    @Basic
    var proposer: String? = null

    @Column(name = "PURPOSE")
    @Basic
    var purpose: String? = null

    @Column(name = "NAME_OF_TC")
    @Basic
    var nameOfTC: String? = null

    @Column(name = "SCOPE")
    @Basic
    var scope: String? = null

    @Column(name = "COM_REQUEST_ID")
    @Basic
    var comRequestId: Long? = null

    @Column(name = "STATUS")
    @Basic
    var status: Long? = null

    @Column(name = "PROPOSED_TC_REPRESENTATION")
    @Basic
    var proposedRepresentation: String? = null

    @Column(name = "PROGRAMME_OF_WORK")
    @Basic
    var programmeOfWork: String? = null

    @Column(name = "LIAISONS_ORGANIZATION")
    @Basic
    var liaisonOrganization: String? = null

    @Column(name = "ORGANIZATION")
    @Basic
    var organization: String? = null

    @Column(name = "REFERENCE_NUMBER")
    @Basic
    var referenceNumber: String? = null

    @Column(name = "TARGET_DATE")
    @Basic
    var targetDate: String? = null

    @Column(name = "DATE_OF_PRESENTATION")
    @Basic
    var dateOfPresentation: String? = null

    @Column(name = "ACCENT_TO")
    @Basic
    var accentTo: String? = null

    @Column(name = "TC_NUMBER")
    @Basic
    var tcNumber: String? = null

    @Column(name = "CREATED_ON")
    @Basic
    var createdOn: Timestamp? = null

    @Column(name = "CREATED_BY")
    @Basic
    var createdBy: Long? = null

    @Column(name = "VERSION")
    @Basic
    var version: String? = null

    @Column(name = "COMMENTS")
    @Basic
    var comments: String? = null

    @Column(name = "HOF_ID")
    @Basic
    var hofId: Long? = null

    @Column(name = "SPC_ID")
    @Basic
    var spcId: Long? = null

    @Column(name = "SAC_ID")
    @Basic
    var sacId: Long? = null

    @Column(name = "COMMENTSSPC")
    @Basic
    var commentsSpc: String? = null


    @Column(name = "HOFREVIEWDATE")
    @Basic
    var hofReviewDate: Timestamp? = null

    @Column(name = "SPCREVIEWDATE")
    @Basic
    var spcReviewDate: Timestamp? = null

    @Column(name = "SACREVIEWDATE")
    @Basic
    var sacReviewDate: Timestamp? = null

    @Column(name = "DEPARTMENT_ID")
    @Basic
    var departmentId: Long? = null



}
