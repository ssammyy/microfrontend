package org.kebs.app.kotlin.apollo.api.payload.request

import org.kebs.app.kotlin.apollo.store.model.pvc.PvocPartnersEntity
import javax.persistence.Basic
import javax.persistence.Column
import javax.validation.constraints.Email
import javax.validation.constraints.NotNull

class PvocPartnersForms {
    @NotNull(message = "Partner Ref Number is required")
    var partnerRefNo: String? = null

    @NotNull(message = "Partner Name Number is required")
    var partnerName: String? = null

    @NotNull(message = "Partner KRA PIN Number is required")
    var partnerPin: String? = null
    var partnerAddress1: String? = null
    var partnerAddress2: String? = null
    var partnerCity: String? = null
    var partnerCountry: String? = null
    var partnerZipcode: String? = null
    var partnerTelephoneNumber: String? = null
    var partnerFaxNumber: String? = null

    @NotNull(message = "Partner email is required")
    @Email(message = "Please enter a valid email address")
    var partnerEmail: String? = null
    fun addDetails(partner: PvocPartnersEntity, update: Boolean) {
        partner.partnerAddress1 = this.partnerAddress1
        partner.partnerName = this.partnerName
        partner.partnerRefNo = this.partnerRefNo
        partner.partnerEmail = this.partnerEmail
        partner.partnerAddress2 = this.partnerAddress2
        partner.partnerCity = this.partnerCity
        partner.partnerCountry = this.partnerCountry
        partner.partnerFaxNumber = this.partnerFaxNumber
        partner.partnerPin = this.partnerPin
        partner.partnerTelephoneNumber = this.partnerTelephoneNumber
        partner.partnerZipcode = this.partnerZipcode

        if (!update) {
            partner.partnerRefNo = partnerRefNo
        }
    }
}