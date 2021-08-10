package org.kebs.app.kotlin.apollo.store.model.std

import javax.persistence.*
import kotlin.jvm.Transient

@Entity
@Table(name="SD_INDIVIDUAL_TARRIFF")
data class IndividualTarriff (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "ID")
        var id: Long=0,

        @Column(name="SUBSCIRPTION_FEE")
        @Basic
        var subscriptionFee:String?=null,

        @Column(name="LIBRARY_DEPOSIT_FEE")
        @Basic
        var libraryDepositFee:String?=null,

        @Column(name="TOTAL")
        @Basic
        var totalFee:String?=null

){
    @Transient
    var taskID : Long?= null
}