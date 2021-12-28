package org.kebs.app.kotlin.apollo.api.payload.request

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocPartnersEntity
import java.sql.Timestamp
import javax.persistence.Basic
import javax.persistence.Column
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
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

class CocItem {
    @NotNull(message = "Required field")
    @JsonProperty("SHIPMENT_PARTIAL_NUMBER")
    var shipmentPartialNumber: Long = 0

    @NotEmpty(message = "Required field")
    @JsonProperty("SHIPMENT_SEAL_NUMBERS")
    var shipmentSealNumbers: String? = null

    @JsonProperty("SHIPMENT_CONTAINER_NUMBER")
    var shipmentContainerNumber: String? = null

    @NotEmpty(message = "Required field")
    @JsonProperty("SHIPMENT_GROSS_WEIGHT")
    var shipmentGrossWeight: String? = null

    @NotEmpty(message = "Required field")
    @JsonProperty("SHIPMENT_QUANTITY_DELIVERED")
    var shipmentQuantityDelivered: String? = null

    @NotNull(message = "Required field")
    @JsonProperty("SHIPMENT_LINE_NUMBER")
    var shipmentLineNumber: Long = 0

    @NotEmpty(message = "Required field")
    @JsonProperty("SHIPMENT_LINE_HS_CODE")
    var shipmentLineHscode: String? = null

    @NotNull(message = "Required field")
    @JsonProperty("SHIPMENT_LINE_QUANTITY")
    var shipmentLineQuantity: Double = 0.0

    @NotEmpty(message = "Required field")
    @JsonProperty("Shipment Line Unit of Measure")
    var shipmentLineUnitofMeasure: String? = null

    @NotEmpty(message = "Required field")
    @JsonProperty("SHIPMENT_LINE_DESCRIPTION")
    var shipmentLineDescription: String? = null

    @JsonProperty("SHIPMENT_LINE_VIN")
    var shipmentLineVin: String? = null


    @JsonProperty("SHIPMENT_LINE_STICKER_NUMBER")
    var shipmentLineStickerNumber: String? = null

    @JsonProperty("SHIPMENT_LINE_ICS")
    var shipmentLineIcs: String? = null

    @NotEmpty(message = "Required field")
    @JsonProperty("SHIPMENT_LINE_STANDARDS_REFERENCE")
    var shipmentLineStandardsReference: String? = null

    @JsonProperty("SHIPMENT_LINE_LICENCE_REFERENCE")
    var shipmentLineLicenceReference: String? = null

    @JsonProperty("SHIPMENT_LINE_REGISTRATION")
    var shipmentLineRegistration: String? = null

    @NotEmpty(message = "Required field")
    @JsonProperty("PRODUCT_CATEGORY")
    var productCategory: String? = null

}

@JsonIgnoreProperties(ignoreUnknown = true)
class CocEntityForm {

    @JsonProperty("COC_NUMBER")
    var cocNumber: String? = null

    @JsonProperty("IDF NUMBER")
    var idfNumber: String? = null

    @JsonProperty("RFI_NUMBER")
    var rfiNumber: String? = null

    @NotEmpty(message = "Required field")
    @JsonProperty("UCR_NUMBER")
    var ucrNumber: String? = null

    @JsonProperty("RFC DATE")
    @JsonAlias("RFC_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var rfcDate: Timestamp? = null


    @JsonProperty("COC_ISSUED_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var cocIssueDate: Timestamp? = null


    @NotEmpty(message = "Required field")
    @JsonProperty("CLEAN")
    var clean: String? = null

    @JsonProperty("COC_REMARKS")
    var cocRemarks: String? = null


    @NotEmpty(message = "Required field")
    @JsonProperty("ISSUING_OFFICE")
    var issuingOffice: String? = null

    @NotEmpty(message = "Required field")
    @JsonProperty("IMPORTER_NAME")
    var importerName: String? = null

    @JsonProperty("IMPORTER_PIN")
    var importerPin: String? = null

    @NotEmpty(message = "Required field")
    @JsonProperty("IMPORTER_ADDRESS_1")
    var importerAddress1: String? = null


    @JsonProperty("IMPORTER_ADDRESS_2")
    var importerAddress2: String? = null

    @NotEmpty(message = "Required field")
    @JsonProperty("IMPORTER_CITY")
    var importerCity: String? = null

    @NotEmpty(message = "Required field")
    @JsonProperty("IMPORTER_COUNTRY")
    @JsonAlias("")
    var importerCountry: String? = null

    @NotEmpty(message = "Required field")
    @JsonProperty("IMPORTER_ZIP_CODE")
    var importerZipCode: String? = null

    @NotEmpty(message = "Required field")
    @JsonProperty("IMPORTER_TELEPHONE_NUMBER")
    var importerTelephoneNumber: String? = null


    @JsonProperty("IMPORTER_FAX_NUMBER")
    var importerFaxNumber: String? = null

    @NotEmpty(message = "Required field")
    @JsonProperty("IMPORTER_EMAIL")
    var importerEmail: String? = null

    @NotEmpty(message = "Required field")
    @JsonProperty("EXPORTER_NAME")
    var exporterName: String? = null

    @JsonProperty("EXPORTER_PIN")
    var exporterPin: String? = null

    @NotEmpty(message = "Required field")
    @JsonProperty("EXPORTER_ADDRESS_1")
    var exporterAddress1: String? = null


    @JsonProperty("EXPORTER_ADDRESS_2")
    var exporterAddress2: String? = null

    @NotEmpty(message = "Required field")
    @JsonProperty("EXPORTER_CITY")
    var exporterCity: String? = null

    @NotEmpty(message = "Required field")
    @JsonProperty("EXPORTER_COUNTRY")
    var exporterCountry: String? = null

    @NotEmpty(message = "Required field")
    @JsonProperty("EXPORTER_ZIP_CODE")
    var exporterZipCode: String? = null

    @NotEmpty(message = "Required field")
    @JsonProperty("EXPORTER_TELEPHONE_NUMBER")
    var exporterTelephoneNumber: String? = null

    @JsonProperty("EXPORTER_FAX_NUMBER")
    var exporterFaxNumber: String? = null

    @NotEmpty(message = "Required field")
    @JsonProperty("EXPORTER_EMAIL")
    var exporterEmail: String? = null

    @NotEmpty(message = "Required field")
    @JsonProperty("PLACE_OF_INSPECTION")
    var placeOfInspection: String? = null

    @NotNull(message = "Required field")
    @JsonProperty("DATE_OF_INSPECTION")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var dateOfInspection: Timestamp? = null

    @NotEmpty(message = "Required field")
    @JsonProperty("PORT_OF_DESTINATION")
    var portOfDestination: String? = null

    @NotEmpty(message = "Required field")
    @JsonProperty("SHIPMENT_MODE")
    var shipmentMode: String? = null

    @NotEmpty(message = "Required field")
    @JsonProperty("COUNTRY_OF_SUPPLY")
    var countryOfSupply: String? = null

    @NotNull(message = "Required field")
    @JsonProperty("FINAL_INVOICE_FOB_VALUE")
    var finalInvoiceFobValue: Double = 0.0

    @NotNull(message = "Required field")
    @JsonProperty("FINAL_INVOICE_EXCHANGE_RATE")
    var finalInvoiceExchangeRate: Double = 0.0

    @NotEmpty(message = "Required field")
    @JsonProperty("FINAL_INVOICE_CURRENCY")
    var finalInvoiceCurrency: String? = null

    @NotEmpty(message = "Required field")
    @JsonProperty("FINAL_INVOICE_NUMBER")
    var finalInvoiceNumber: String? = null

    @NotNull(message = "Required field")
    @JsonProperty("FINAL_INVOICE_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var finalInvoiceDate: Timestamp? = null

    @NotEmpty(message = "Required field")
    @JsonProperty("ROUTE")
    var route: String? = null

    @NotEmpty(message = "Required field")
    @JsonProperty("PRODUCT")
    var products: List<CocItem>? = null
}