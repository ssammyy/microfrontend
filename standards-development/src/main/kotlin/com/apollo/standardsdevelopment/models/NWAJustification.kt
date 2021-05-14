package com.apollo.standardsdevelopment.models

import java.sql.Timestamp
import javax.persistence.*
import kotlin.jvm.Transient


@Entity
@Table(name="NWA_JUSTIFICATION")
class NWAJustification {

    @Id
    @Column(name="ID")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    val id:Long=0

    @Column(name="MEETING_DATE")
    @Basic
    val meetingDate : Timestamp?=null

    @Column(name="KNW")
    @Basic
    val knw : String?=null

    @Column(name="KNW_SECRETARY")
    @Basic
    val knwSecretary : String?=null

    @Column(name="SL")
    @Basic
    val sl : String?=null

    @Column(name="REQUEST_NUMBER")
    @Basic
    val requestNumber : String?=null

    @Column(name="REQUESTED_BY")
    @Basic
    val requestedBy : String?=null

    @Column(name="ISSUES_ADDRESSED")
    @Basic
    val issuesAddressed : String?=null

    @Column(name="KNW_ACCEPTANCE_DATE")
    @Basic
    val knwAcceptanceDate : Timestamp?=null

    @Column(name="REFERENCE_MATERIAL")
    @Basic
    val referenceMaterial : String?=null

    @Column(name="DEPARTMENT")
    @Basic
    val department : String?=null

    @Transient
    val taskId : String = ""
    val approved : String = ""

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NWAJustification

        if (id != other.id) return false
        if (meetingDate != other.meetingDate) return false
        if (knw != other.knw) return false
        if (knwSecretary != other.knwSecretary) return false
        if (sl != other.sl) return false
        if (requestNumber != other.requestNumber) return false
        if (requestedBy != other.requestedBy) return false
        if (issuesAddressed != other.issuesAddressed) return false
        if (knwAcceptanceDate != other.knwAcceptanceDate) return false
        if (referenceMaterial != other.referenceMaterial) return false
        if (department != other.department) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (meetingDate?.hashCode() ?: 0)
        result = 31 * result + (knw?.hashCode() ?: 0)
        result = 31 * result + (knwSecretary?.hashCode() ?: 0)
        result = 31 * result + (sl?.hashCode() ?: 0)
        result = 31 * result + (requestNumber?.hashCode() ?: 0)
        result = 31 * result + (requestedBy?.hashCode() ?: 0)
        result = 31 * result + (issuesAddressed?.hashCode() ?: 0)
        result = 31 * result + (knwAcceptanceDate?.hashCode() ?: 0)
        result = 31 * result + (referenceMaterial?.hashCode() ?: 0)
        result = 31 * result + (department?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "NWAService(id=$id, meetingDate=$meetingDate, knw=$knw, knwSecretary=$knwSecretary, sl=$sl," +
                " requestNumber=$requestNumber, requestedBy=$requestedBy,issuesAddressed=$issuesAddressed,knwAcceptanceDate=$knwAcceptanceDate" +
                ",referenceMaterial=$referenceMaterial,department=$department)"
    }
}
