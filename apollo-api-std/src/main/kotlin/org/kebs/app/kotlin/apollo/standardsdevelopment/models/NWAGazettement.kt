package org.kebs.app.kotlin.apollo.standardsdevelopment.models

import javax.persistence.*

@Entity
@Table(name="SD_NWA_GAZETTEMENT")
class NWAGazettement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    var id:Long=0

    @Column(name="KS_NUMBER")
    @Basic
    var ksNumber:String? =null

    @Column(name="DATE_OF_GAZETTEMENT")
    @Basic
    var dateOfGazettement:String? =null

    @Column(name="DESCRIPTION")
    @Basic
    var description:String? =null



    @Transient
    val approved : Boolean = true
    val taskId:String?=null
}
