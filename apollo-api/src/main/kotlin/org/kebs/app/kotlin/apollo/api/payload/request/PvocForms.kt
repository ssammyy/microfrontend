package org.kebs.app.kotlin.apollo.api.payload.request

import org.kebs.app.kotlin.apollo.store.model.pvc.PvocPartnersEntity
import javax.persistence.Basic
import javax.persistence.Column

class PvocPartnersForms {
    var partnerRefNo: String? = null
    var partnerName: String? = null
    var partnerPin: String? = null
    var partnerAddress1: String? = null
    var partnerAddress2: String? = null
    var partnerCity: String? = null
    var partnerCountry: String? = null
    var partnerZipcode: String? = null
    var partnerTelephoneNumber: String? = null
    var partnerFaxNumber: String? = null
    var partnerEmail: String? = null
    fun addDetails(partner: PvocPartnersEntity, update:Boolean) {
        partner.apply {
            partnerAddress1 = this.partnerAddress1
            partnerAddress2 = this.partnerAddress2
            partnerCity = this.partnerCity
            partnerCountry=this.partnerCountry
            partnerFaxNumber=partnerFaxNumber
            partnerPin=partnerPin
            partnerTelephoneNumber=partnerTelephoneNumber
            partnerZipcode=partnerZipcode
        }
        if(!update){
            partner.partnerRefNo=partnerRefNo
        }
    }
}