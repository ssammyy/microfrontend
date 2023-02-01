package org.kebs.app.kotlin.apollo.store.model.std

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import java.util.*
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

    @Column(name="SDN")
    @Basic
    var sdn:Long? =null

    @Column(name="ISDN")
    @Basic
    var isdn:Long? =null


    @Column(name="STATUS")
    @Basic
    var status:Long? =null

    @Column(name="IS_GAZETTED")
    @Basic
    var isGazetted:Long? =null

    @Column(name="STANDARD_TYPE")
    @Basic
    var standardType:String? =null

    @Column(name="DESCRIPTION")
    @Basic
    var description:String? =null

    @Column(name="DATE_OF_GAZETTEMENT")
    @Basic
    var dateOfGazettement: Timestamp? =null


    @Column(name="DATE_FORMED")
    @Basic
    var dateFormed: Timestamp? =null

    @Column(name="CS_ID")
    @Basic
    var comStdId:Long? =null



}
