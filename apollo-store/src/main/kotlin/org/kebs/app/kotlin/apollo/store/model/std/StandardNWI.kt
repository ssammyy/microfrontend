package org.kebs.app.kotlin.apollo.store.model.std

import com.fasterxml.jackson.annotation.JsonProperty

import java.io.File
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name="SD_NWI")
class StandardNWI {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    var id:Long=0

    @Transient
    @JsonProperty("taskId")
    var taskId:String?=null

    @Column(name="PROPOSAL_TITLE")
    @Basic
    var proposalTitle:String? =null

    @Column(name="SCOPE")
    @Basic
    var scope:String? =null

    @Column(name="PURPOSE")
    @Basic
    var purpose:String? =null

    @Column(name="TARGET_DATE")
    @Basic
    var targetDate:String? =null

    @Column(name="SIMILAR_STANDARDS")
    @Basic
    var similarStandards:String? =null

    @Column(name="LIAISON_ORGANIZATION")
    @Basic
    var liaisonOrganisation:String? =null


    @Column(name="DRAFT_ATTACHED")
    @Basic
    var draftAttached:String? =null

    @Column(name="OUTLINE_ATTACHED")
    @Basic
    var outlineAttached:String? =null

    @Column(name="DRAFT_OUTLINE_IMPOSSIBLE")
    @Basic
    var draftOutlineImpossible:String? =null

    @Column(name="OUTLINE_SENT_LATER")
    @Basic
    var outlineSentLater:String? =null

    @Column(name="NAME_OF_PROPOSER")
    @Basic
    var nameOfProposer:String? =null

    @Column(name="ORGANIZATION")
    @Basic
    var organization:String? =null

    @Column(name="CIRCULATION_DATE")
    @Basic
    var circulationDate:String? =null

    @Column(name="CLOSING_DATE")
    @Basic
    var closingDate:String? =null

    @Column(name="DATE_OF_PRESENTATION")
    @Basic
    var dateOfPresentation:String? =null

    @Column(name="NAME_OF_TC")
    @Basic
    var nameOfTC:String? =null

    @Column(name="REFERENCE_NUMBER")
    @Basic
    var referenceNumber:String? =null

    @Column(name="CREATED_ON")
    @Basic
    var createdOn:Timestamp? =null

    @Column(name="MODIFIED_ON")
    @Basic
    var modifiedOn:Timestamp? =null

    @Column(name="DELETED_ON")
    @Basic
    var deletedOn:Timestamp? =null

    @Column(name="STATUS")
    @Basic
    var status:String? =null


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StandardNWI

        if (id != other.id) return false
        if (taskId != other.taskId) return false
        if (proposalTitle != other.proposalTitle) return false
        if (scope != other.scope) return false
        if (purpose != other.purpose) return false
        if (targetDate != other.targetDate) return false
        if (similarStandards != other.similarStandards) return false
        if (liaisonOrganisation != other.liaisonOrganisation) return false
        if (draftAttached != other.draftAttached) return false
        if (outlineAttached != other.outlineAttached) return false
        if (draftOutlineImpossible != other.draftOutlineImpossible) return false
        if (outlineSentLater != other.outlineSentLater) return false
        if (nameOfProposer != other.nameOfProposer) return false
        if (organization != other.organization) return false
        if (circulationDate != other.circulationDate) return false
        if (closingDate != other.closingDate) return false
        if (dateOfPresentation != other.dateOfPresentation) return false
        if (nameOfTC != other.nameOfTC) return false
        if (referenceNumber != other.referenceNumber) return false
        if (createdOn != other.createdOn) return false
        if (modifiedOn != other.modifiedOn) return false
        if (deletedOn != other.deletedOn) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (taskId?.hashCode() ?: 0)
        result = 31 * result + (proposalTitle?.hashCode() ?: 0)
        result = 31 * result + (scope?.hashCode() ?: 0)
        result = 31 * result + (purpose?.hashCode() ?: 0)
        result = 31 * result + (targetDate?.hashCode() ?: 0)
        result = 31 * result + (similarStandards?.hashCode() ?: 0)
        result = 31 * result + (liaisonOrganisation?.hashCode() ?: 0)
        result = 31 * result + (draftAttached?.hashCode() ?: 0)
        result = 31 * result + (outlineAttached?.hashCode() ?: 0)
        result = 31 * result + (draftOutlineImpossible?.hashCode() ?: 0)
        result = 31 * result + (outlineSentLater?.hashCode() ?: 0)
        result = 31 * result + (nameOfProposer?.hashCode() ?: 0)
        result = 31 * result + (organization?.hashCode() ?: 0)
        result = 31 * result + (circulationDate?.hashCode() ?: 0)
        result = 31 * result + (closingDate?.hashCode() ?: 0)
        result = 31 * result + (dateOfPresentation?.hashCode() ?: 0)
        result = 31 * result + (nameOfTC?.hashCode() ?: 0)
        result = 31 * result + (referenceNumber?.hashCode() ?: 0)
        result = 31 * result + (createdOn?.hashCode() ?: 0)
        result = 31 * result + (modifiedOn?.hashCode() ?: 0)
        result = 31 * result + (deletedOn?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "StandardNWI(id=$id, taskId=$taskId, proposalTitle=$proposalTitle, scope=$scope, purpose=$purpose, targetDate=$targetDate, similarStandards=$similarStandards, liaisonOrganisation=$liaisonOrganisation, draftAttached=$draftAttached, outlineAttached=$outlineAttached, draftOutlineImpossible=$draftOutlineImpossible, outlineSentLater=$outlineSentLater, nameOfProposer=$nameOfProposer, organization=$organization, circulationDate=$circulationDate, closingDate=$closingDate, dateOfPresentation=$dateOfPresentation, nameOfTC=$nameOfTC, referenceNumber=$referenceNumber, createdOn=$createdOn, modifiedOn=$modifiedOn, deletedOn=$deletedOn)"
    }


}
