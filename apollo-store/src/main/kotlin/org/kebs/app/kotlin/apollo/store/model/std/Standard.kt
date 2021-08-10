package org.kebs.app.kotlin.apollo.store.model.std

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*

@Entity
@Table(name="SD_STANDARD_TBL")
class Standard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    var id:Long=0


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

    @Column(name="STANDARD_NUMBER")
    @Basic
    var standardNumber:String? =null

    @Column(name="STATUS")
    @Basic
    var status:String? =null


}
