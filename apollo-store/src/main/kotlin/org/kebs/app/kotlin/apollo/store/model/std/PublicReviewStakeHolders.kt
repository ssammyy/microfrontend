package org.kebs.app.kotlin.apollo.store.model.std

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import javax.persistence.*
import kotlin.jvm.Transient

@Entity
@Table(name="SD_PUBLIC_REVIEW_STAKE_HOLDERS")
class PublicReviewStakeHolders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id: Long=0

    @Column(name="PR_ID")
    @Basic
    var prId:Long?=null


    @Column(name="NAME")
    @Basic
    var name: String?=null

    @Column(name="EMAIL")
    @Basic
    var email: String?=null

    @Column(name="TELEPHONE")
    @Basic
    var telephone: String?=null

    @Column(name="DATE_OF_CREATION")
    @Basic
    var dateOfCreation: Timestamp?=null

    @Column(name="USER_ID")
    @Basic
    var userId: Long?=null

    @Column(name="STATUS")
    @Basic
    var status: Long?=null

    @Column(name="COMMENT_ID")
    @Basic
    var commentId: Long?=null

    @Column(name="ENCRYPTED_ID")
    @Basic
    var encrypted: String?=null



}
