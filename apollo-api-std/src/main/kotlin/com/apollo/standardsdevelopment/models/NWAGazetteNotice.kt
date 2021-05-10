package com.apollo.standardsdevelopment.models

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name="SD_NWA_GAZETTE_NOTICE")
class NWAGazetteNotice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    var id:Long=0

    @Column(name="KS_NUMBER")
    @Basic
    var ksNumber:String? =null

    @Column(name="DATE_UPLOADED")
    @Basic
    var dateUploaded:String? =null

    @Column(name="DESCRIPTION")
    @Basic
    var description:String? =null



    @Transient
    val approved : Boolean = true
    val taskId:String?=null
}
