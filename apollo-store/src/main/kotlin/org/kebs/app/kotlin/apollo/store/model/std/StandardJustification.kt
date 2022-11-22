package org.kebs.app.kotlin.apollo.store.model.std

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name="SD_JUSTIFICATION")
class StandardJustification {
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    val id:Long=0

    @Column(name="SPC_MEETING_DATE")
    @Basic
    val spcMeetingDate:String?=null

    @Transient
    @JsonProperty("taskId")
    var taskId:String?=null

    @Column(name="DEPARTMENT_ID")
    @Basic
    val departmentId:String?=null

    @Column(name="TC_SECRETARY")
    @Basic
    val tcSecretary:String?=null

    @Column(name="SL")
    @Basic
    val sl:String?=null

    @Column(name="TITLE")
    @Basic
    val title:String?=null

    @Column(name="EDITION")
    @Basic
    val edition:String?=null

    @Column(name="REQUESTED_BY")
    @Basic
    val requestedBy:String?=null

    @Column(name="ISSUES_ADDRESSED_BY")
    @Basic
    val issuesAddressedBy:String?=null

    @Column(name="TC_ACCEPTANCE_DATE")
    @Basic
    val tcAcceptanceDate:String?=null

    @Column(name="REQUEST_NO")
    @Basic
    val requestNo:String?=null

    @Column(name="TC_ID")
    @Basic
    val tcId:String?=null

    @Column(name="CD_NUMBER")
    @Basic
    var cdNumber:String?=null

    @Column(name="CREATED_ON")
    @Basic
    var createdOn: Timestamp? =null

    @Column(name="MODIFIED_ON")
    @Basic
    var modifiedOn: Timestamp? =null

    @Column(name="DELETED_ON")
    @Basic
    var deletedOn: Timestamp? =null

    @Column(name = "NWI_ID")
    @Basic
    var nwiId: String?=null


    @Column(name = "CREATED_BY")
    @Basic
    var createdBy: String?=null

    @Column(name = "STATUS")
    @Basic
    var status: String?=null





}
