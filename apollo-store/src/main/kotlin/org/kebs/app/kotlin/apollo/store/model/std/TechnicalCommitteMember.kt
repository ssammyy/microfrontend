package org.kebs.app.kotlin.apollo.store.model.std

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name="SD_TECHNICAL_COMMITTEE")
class TechnicalCommitteMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    var id:Long=0

    @Column(name="USER_ID")
    @Basic
    var userId:Long=0

    @Column(name="TC")
    @Basic
    var tc:String?=null

    @Column(name="NAME")
    @Basic
    var name:String?=null

    @Column(name="EMAIL")
    @Basic
    var email:String?=null


    @Transient
    @JsonProperty("taskId")
    var taskId:String?=null


}
