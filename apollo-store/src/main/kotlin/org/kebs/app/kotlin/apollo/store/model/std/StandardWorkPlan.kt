package org.kebs.app.kotlin.apollo.store.model.std

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name="SD_WORKPLAN")
class StandardWorkPlan {
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long=0

    @Column(name="TARGET_DATE")
    @Basic
    var targetDate:String?=null

    @Column(name="STAGE_DATE")
    @Basic
    var stageDate:String?=null

    @Column(name="STAGE_CODE")
    @Basic
    var value:String?=null

    @Column(name="SUB_STAGE")
    @Basic
    var subStage:String?=null

    @Column(name="STAGE_MONTH")
    @Basic
    var month:String?=null

    @Column(name="REQUEST_NO")
    @Basic
    var referenceNo:String?=null

    @Column(name="STATUS")
    @Basic
    var status:String?=null



    @Transient
    @JsonProperty("taskId")
    var taskId:String?=null

    @Column(name="CREATED_ON")
    @Basic
    var createdOn: Timestamp? =null

    @Column(name="MODIFIED_ON")
    @Basic
    var modifiedOn: Timestamp? =null

    @Column(name="DELETED_ON")
    @Basic
    var deletedOn: Timestamp? =null




}
