package org.kebs.app.kotlin.apollo.store.model.std

import java.io.Serializable
import java.sql.Date
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name="STANDARD_LEVY_HISTORICAL_PAYMENTS")
class StdLevyHistoricalPayments : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id: Long=0

    @Column(name="ENTRY_NUMBER")
    @Basic
    var entryNumber:String?=null

    @Column(name="KRA_PIN")
    @Basic
    var kraPin: String?=null

    @Column(name="COMPANY_NAME")
    @Basic
    var companyName:String?=null

    @Column(name="TAX_HEAD")
    @Basic
    var taxHead:String?=null

    //  @Transient
    @Column(name = "PAYMENT_DATE")
    @Basic
    var paymentDate: Date? = null

    @Column(name = "PERIOD_FROM")
    @Basic
    var periodFrom: Date? = null

    @Column(name = "PERIOD_TO")
    @Basic
    var periodTo: Date? = null

    @Column(name="MODE_OF_PAYMENT")
    @Basic
    var modeOfPayment:String?=null

    @Column(name="AMOUNT")
    @Basic
    var amount:Float?=null

    @Column(name="LOCATION")
    @Basic
    var location:String?=null

    @Column(name="PRN")
    @Basic
    var prn:Long?=null


}
