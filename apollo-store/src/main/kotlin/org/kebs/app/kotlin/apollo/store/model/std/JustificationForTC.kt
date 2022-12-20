package org.kebs.app.kotlin.apollo.store.model.std

import javax.persistence.*

@Entity
@Table(name="SD_JUSTIFICATION_FOR_TC")
class JustificationForTC {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    var id:Long =0

    @Column(name="SUBJECT")
    @Basic
    var subject:String? = null

    @Column(name="PROPOSER")
    @Basic
    var proposer:String? = null

    @Column(name="PURPOSE")
    @Basic
    var purpose:String? = null

    @Column(name="NAME_OF_TC")
    @Basic
    var nameOfTC:String? = null

    @Column(name="SCOPE")
    @Basic
    var scope:String? = null

    @Column(name="COM_REQUEST_ID")
    @Basic
    var comRequestId:Long? = null

    @Column(name="STATUS")
    @Basic
    var status:Long? = null

    @Column(name="PROPOSED_TC_REPRESENTATION")
    @Basic
    var proposedRepresentation:String? = null

    @Column(name="PROGRAMME_OF_WORK")
    @Basic
    var programmeOfWork:String? = null

    @Column(name="LIAISONS_ORGANIZATION")
    @Basic
    var liaisonOrganization:String? = null

    @Column(name="ORGANIZATION")
    @Basic
    var organization:String? = null

    @Column(name="REFERENCE_NUMBER")
    @Basic
    var referenceNumber:String? = null

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as JustificationForTC

        if (id != other.id) return false
        if (subject != other.subject) return false
        if (proposer != other.proposer) return false
        if (purpose != other.purpose) return false
        if (nameOfTC != other.nameOfTC) return false
        if (scope != other.scope) return false
        if (proposedRepresentation != other.proposedRepresentation) return false
        if (programmeOfWork != other.programmeOfWork) return false
        if (liaisonOrganization != other.liaisonOrganization) return false
        if (organization != other.organization) return false
        if (referenceNumber != other.referenceNumber) return false
        if (targetDate != other.targetDate) return false
        if (dateOfPresentation != other.dateOfPresentation) return false
        if (accentTo != other.accentTo) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (subject?.hashCode() ?: 0)
        result = 31 * result + (proposer?.hashCode() ?: 0)
        result = 31 * result + (purpose?.hashCode() ?: 0)
        result = 31 * result + (nameOfTC?.hashCode() ?: 0)
        result = 31 * result + (scope?.hashCode() ?: 0)
        result = 31 * result + (proposedRepresentation?.hashCode() ?: 0)
        result = 31 * result + (programmeOfWork?.hashCode() ?: 0)
        result = 31 * result + (liaisonOrganization?.hashCode() ?: 0)
        result = 31 * result + (organization?.hashCode() ?: 0)
        result = 31 * result + (referenceNumber?.hashCode() ?: 0)
        result = 31 * result + (targetDate?.hashCode() ?: 0)
        result = 31 * result + (dateOfPresentation?.hashCode() ?: 0)
        result = 31 * result + (accentTo?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "JustificationForTC(id=$id, subject=$subject, proposer=$proposer, purpose=$purpose, nameOfTC=$nameOfTC, scope=$scope, proposedRepresentation=$proposedRepresentation, programmeOfWork=$programmeOfWork, liaisonOrganization=$liaisonOrganization, organization=$organization, referenceNumber=$referenceNumber, targetDate=$targetDate, dateOfPresentation=$dateOfPresentation,accentTo=$accentTo)"
    }


}
