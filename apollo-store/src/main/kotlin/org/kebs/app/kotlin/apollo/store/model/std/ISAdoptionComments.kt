package org.kebs.app.kotlin.apollo.store.model.std

import javax.persistence.*
import kotlin.jvm.Transient

@Entity
@Table(name="SD_ADOPTION_PROPOSAL_COMMENTS")
class ISAdoptionComments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    val id:Long=0

    @Column(name="USER_ID")
    @Basic
    val user_id:String? =null

    @Column(name="ADOPTION_PROPOSAL_COMMENT")
    @Basic
    val adoption_proposal_comment:String? =null

    @Column(name="COMMENT_TIME")
    @Basic
    val comment_time:String? =null

    @Transient
    var taskId:String?=null


}
