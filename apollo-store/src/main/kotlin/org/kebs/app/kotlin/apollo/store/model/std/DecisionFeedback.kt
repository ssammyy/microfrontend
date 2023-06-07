package org.kebs.app.kotlin.apollo.store.model.std

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name="SD_DECISION_FEEDBACK")
class DecisionFeedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    var id:Long=0

    @Column(name="user_id")
    @Basic
    var user_id:Long=0

    @Column(name="item_id")
    @Basic
    var item_id:String?=null

    @Column(name="status")
    @Basic
    var status:Boolean?=null

    @Column(name="user_comment")
    @Basic
    var comment:String?=null

    @Transient
    @JsonProperty("taskId")
    var taskId:String?=null

    @Column(name="SUBMISSION_DATE")
    @Basic
    var submission_date: Timestamp?=null

}
