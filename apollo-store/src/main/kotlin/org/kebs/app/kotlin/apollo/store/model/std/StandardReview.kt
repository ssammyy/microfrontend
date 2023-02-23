package org.kebs.app.kotlin.apollo.store.model.std

import java.sql.Timestamp
import javax.persistence.*


@Entity
@Table(name="SD_STANDARD_REVIEW")
class StandardReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    var id:Long=0

    @Column(name="STANDARD_NUMBER")
    @Basic
    var standardNumber:String? =null

    @Column(name="TITLE")
    @Basic
    var title:String? =null

    @Column(name="DOCUMENT_TYPE")
    @Basic
    var documentType:String? =null

    @Column(name="PREPARED_BY")
    @Basic
    var preparedBy:Long? =null

    @Column(name="DATE_PREPARED")
    @Basic
    var datePrepared:Timestamp? =null

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

    @Column(name="STANDARD_TYPE")
    @Basic
    var standardType:String? =null

    @Column(name="REQUEST_NUMBER")
    @Basic
    var requestNumber:String? =null

    @Column(name="ASSIGNED_TO")
    @Basic
    var assignedTo:Long? =null

    @Column(name="STANDARD_ID")
    @Basic
    var standardId:Long? =null

    @Column(name="CIRCULATION_DATE")
    @Basic
    var circulationDate:Timestamp? =null

    @Column(name="CLOSING_DATE")
    @Basic
    var closingDate:Timestamp? =null

    @Column(name="DATE_FORMED")
    @Basic
    var dateFormed:Timestamp? =null

    @Column(name="TC_SECRETARY")
    @Basic
    var tcSecretary:String? =null

    @Column(name="EDITION")
    @Basic
    var edition:String? =null

    @Column(name="OPERATION_OPTION")
    @Basic
    var operationOption:String? =null

    @Column(name="JUSTIFICATION")
    @Basic
    var justification:String? =null

    @Column(name="NAME_OF_RESPONDENT")
    @Basic
    var nameOfRespondent:String? =null

    @Column(name="NAME_OF_ORGANIZATION")
    @Basic
    var nameOfOrganization:String? =null

    @Column(name="POSITION_OF_RESPONDENT")
    @Basic
    var positionOfRespondent:String? =null

    @Column(name="RECOMENDATION")
    @Basic
    var recommendation:String? =null

    @Column(name="DATE_OF_RECOMENDATION")
    @Basic
    var dateOfRecommendation:Timestamp? =null

    @Column(name="TIMELINE")
    @Basic
    var timeline:Timestamp? =null

    @Column(name="STATUS")
    @Basic
    var status:Long? =null

    @Column(name="FEED_BACK")
    @Basic
    var feedback:Long? =null


}
