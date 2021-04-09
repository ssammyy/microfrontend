package org.kebs.app.kotlin.apollo.store.model.PvocResponses

import org.springframework.stereotype.Component
import java.sql.Date
import java.sql.Timestamp
import javax.persistence.Basic
import javax.persistence.Column

@Component
class RfcGoodsResponse {
    var rfcNumber: String? = null
    var idfNumber: String? = null
    var ucrNumber: String? = null
    var rfcDate: Date? = null
    var countryOfDestination: String? = null
    var applicationType: String? = null
    var sorReference: String? = null
    var solReference: String? = null
    var importerName: String? = null
    var importerPin: String? = null
    var importerAddress1: String? = null
    var importerAddress2: String? = null
    var importerCity: String? = null
    var importerCountry: String? = null
    var importerZipcode: String? = null
    var importerTelephoneNumber: String? = null
    var importerFaxNumber: String? = null
    var importerEmail: String? = null
    var exporterName: String? = null
    var exporterPin: String? = null
    var exporterAddress1: String? = null
    var exporterAddress2: String? = null
    var exporterCity: String? = null
    var exporterCountry: String? = null
    var exporterZipcode: String? = null
    var exporterTelephoneNumber: String? = null
    var exporterFaxNumber: String? = null
    var exporterEmail: String? = null
    var thirdPartyName: String? = null
    var thirdPartyPin: String? = null
    var thirdPartyAddress1: String? = null
    var thirdPartyAddress2: String? = null
    var thirdPartyCity: String? = null
    var thirdPartyCountry: String? = null
    var thirdPartyZipcode: String? = null
    var thirdPartyTelephoneNumber: String? = null
    var thirdPartyFaxNumber: String? = null
    var thirdPartyEmail: String? = null
    var applicantName: String? = null
    var applicantPin: String? = null
    var applicantAddress1: String? = null
    var applicantAddress2: String? = null
    var applicantCity: String? = null
    var applicantCountry: String? = null
    var applicantZipcode: String? = null
    var applicantTelephoneNumber: String? = null
    var applicantFaxNumber: String? = null
    var applicantEmail: String? = null
    var placeOfInspection: String? = null
    var placeOfInspectionAddress: String? = null
    var placeOfInspectionEmail: String? = null
    var placeOfInspectionContacts: String? = null
    var portOfLoading: String? = null
    var portOfDischarge: String? = null
    var shipmentMethod: String? = null
    var countryOfSupply: String? = null
    var route: String? = null
    var goodsCondition: String? = null
    var assemblyState: String? = null
    var linkToAttachedDocuments: String? = null
    var partner: String? = null
}