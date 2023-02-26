package org.kebs.app.kotlin.apollo.store.model.std

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name="SD_WORKSHOP_STD")
class SDWorkshopStd {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    var id:Long=0

    @Column(name="REQUEST_ID")
    @Basic
    var requestId:Long? =null

    @Column(name="NWA_STD_NUMBER")
    @Basic
    var nwaStdNumber:String? =null


}

