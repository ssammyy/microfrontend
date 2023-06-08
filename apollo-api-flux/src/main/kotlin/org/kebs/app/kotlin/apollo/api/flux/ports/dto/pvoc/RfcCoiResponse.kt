package org.kebs.app.kotlin.apollo.api.flux.ports.dto.pvoc

import org.kebs.app.kotlin.apollo.store.model.pvc.RfcItemEntity
import java.sql.Date


class RfcCoiResponse {
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
    var items: MutableList<RfcCoiItemsResponse> = mutableListOf()
}

class RfcCoiWithItems {
//    var rfcCoi: RfcCoiEntity? = null
    var rfcCoiItems: List<RfcItemEntity>? = null
}

