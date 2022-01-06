package org.kebs.app.kotlin.apollo.store.model.std

import java.sql.Timestamp
import javax.persistence.*
import kotlin.jvm.Transient

@Entity
@Table(name="SD_ADOPTION_PROPOSAL_COMMENTS")
class ISAdoptionComments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    var id:Long=0

    @Column(name="USER_ID")
    @Basic
    var user_id:String? =null

    @Column(name="ADOPTION_PROPOSAL_COMMENT")
    @Basic
    var adoption_proposal_comment:String? =null

    @Column(name="COMMENT_TIME")
    @Basic
    var comment_time: Timestamp?=null

    @Transient
    var taskId:String?=null


}
