package org.kebs.app.kotlin.apollo.standardsdevelopment.models

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*

@Entity
@Table(name="SD_NWA_STANDARD_TB")
class NWAStandard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    var id:Long=0

    @Transient
    @JsonProperty("taskId")
    var taskId:String?=null

    @Column(name="TITLE")
    @Basic
    var title:String? =null

    @Column(name="SCOPE")
    @Basic
    var scope:String? =null

    @Column(name="NORMATIVE_REFERENCE")
    @Basic
    var normativeReference:String? =null

    @Column(name="SYMBOLS_ABBREVIATED_TERMS")
    @Basic
    var symbolsAbbreviatedTerms:String? =null

    @Column(name="CLAUSE")
    @Basic
    var clause:String? =null

    @Column(name="SPECIAL")
    @Basic
    var special:String? =null

    @Column(name="KNWA_NUMBER")
    @Basic
    var ksNumber:String? =null

    @Transient
    val approved : Boolean = true
}