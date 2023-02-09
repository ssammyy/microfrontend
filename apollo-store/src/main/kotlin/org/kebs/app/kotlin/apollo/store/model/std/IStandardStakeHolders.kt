package org.kebs.app.kotlin.apollo.store.model.std

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import javax.persistence.*
import kotlin.jvm.Transient

@Entity
@Table(name="SD_IS_STAKE_HOLDERS")
class IStandardStakeHolders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id: Long=0

    @Column(name="DRAFT_ID")
    @Basic
    var draftId:Long?=null


    @Column(name="NAME")
    @Basic
    var name: String?=null

    @Column(name="NAMES")
    @Basic
    var names: String?=null

    @Column(name="EMAIL")
    @Basic
    var email: String?=null

    @Column(name="TELEPHONE")
    @Basic
    var telephone: String?=null

    @Column(name="DATE_OF_CREATION")
    @Basic
    var dateOfCreation: Timestamp?=null




}
