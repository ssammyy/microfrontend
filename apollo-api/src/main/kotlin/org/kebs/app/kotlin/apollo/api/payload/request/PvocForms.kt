package org.kebs.app.kotlin.apollo.api.payload.request

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocPartnersEntity
import java.sql.Date
import java.sql.Timestamp
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

    @NotNull(message = "Partner country is required")
    var partnerCountry: Long? = null
    var partnerZipcode: String? = null
    var partnerTelephoneNumber: String? = null
    var partnerFaxNumber: String? = null

    @NotNull(message = "Partner email is required")
    @Email(message = "Please enter a valid email address")
    var partnerEmail: String? = null

    // Billing information
    @NotNull(message = "Partner billing contact name is required")
    var billingContactName: String? = null

    @NotNull(message = "Partner billing contact phone is required")
    var billingContactPhone: String? = null

    @NotNull(message = "Partner billing contact email is required")
    var billingContactEmail: String? = null

    var billingLimitId: Long = 0


    fun addDetails(partner: PvocPartnersEntity, update: Boolean) {
        partner.partnerAddress1 = this.partnerAddress1
        partner.partnerName = this.partnerName
        partner.partnerRefNo = this.partnerRefNo
        partner.partnerEmail = this.partnerEmail
        partner.partnerAddress2 = this.partnerAddress2
        partner.partnerCity = this.partnerCity
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

    @JsonProperty("SHIPMENT_BRAND_NAME")
    var shipmentBrandName: String? = null

    @NotEmpty(message = "Required field")
    @JsonProperty("PRODUCT_CATEGORY")
    var productCategory: String? = null

}

class DocumentPaymentDetails {

    @NotNull(message = "Required field")
    @JsonProperty("INSPECTION_VALUE")
    var inspectionValue: Double = 0.0

    @NotNull(message = "Required field")
    @JsonProperty("INSPECTION_PENALTY")
    var penaltyValue: Double = 0.0

    @NotNull(message = "Required field")
    @JsonProperty("INSPECTION_TAX")
    var taxValue: Double = 0.0

    @NotNull(message = "Required field")
    @JsonProperty("INSPECTION_EXCHANGE_RATE")
    var innvoiceExchangeRate: Double = 0.0

    @NotEmpty(message = "Required field")
    @JsonProperty("INSPECTION_CURRENCY")
    var inspectionCurrency: String? = null

    @NotEmpty(message = "Required field")
    @JsonProperty("RECEIPT_NUMBER")
    var receiptNumber: String? = null

    @NotNull(message = "Required field")
    @JsonProperty("PAYMENT_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var paymentDate: Timestamp? = null
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
    @JsonProperty("SHIPMENT_SEAL_NUMBER")
    var shipmentSealNumbers: String? = null

    @NotEmpty(message = "Required field")
    @JsonProperty("SHIPMENT_CONTAINER_NUMBER")
    var shipmentContainerNumber: String? = null

    @NotEmpty(message = "Required field")
    @JsonProperty("SHIPMENT_GROSS_WEIGHT")
    var shipmentGrossWeight: String? = null

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
    var cocItems: List<CocItem>? = null

    @NotEmpty(message = "Required field")
    @JsonProperty("INSPECTION_FEE")
    var inspectionFee: DocumentPaymentDetails? = null

    @NotEmpty(message = "Required field")
    @JsonProperty("VERSION")
    var version: Long? = null
}


class CoiItem {
    @NotNull(message = "Required field")
    var coiNumber: String = ""

    @NotNull(message = "Required field")
    var declaredHsCode: String = ""

    @NotNull(message = "Required field")
    var itemQuantity: String? = ""

    @NotNull(message = "Required field")
    var productDescription: String? = ""

    @NotNull(message = "Required field")
    var ownerPin: String? = ""

    @NotNull(message = "Required field")
    var status: Int = 0

    @NotNull(message = "Required field")
    var shipmentLineHsCode: String? = ""

    @NotNull(message = "Required field")
    var shipmentLineNumber: Long? = 0

    @NotEmpty(message = "Required field")
    var shipmentLineQuantity: Long? = null

    @NotEmpty(message = "Required field")
    var shipmentLineUnitofMeasure: String? = null

    @NotEmpty(message = "Required field")
    var shipmentLineDescription: String? = null

    @NotEmpty(message = "Required field")
    var shipmentLineVin: String? = null

    @NotEmpty(message = "Required field")
    var shipmentLineStickerNumber: String? = null

    @NotEmpty(message = "Required field")
    var shipmentLineIcs: String? = null

    @NotEmpty(message = "Required field")
    var shipmentLineStandardsReference: String? = null

    @NotEmpty(message = "Required field")
    var shipmentLineLicenceReference: String? = null

    @NotEmpty(message = "Required field")
    var shipmentLineRegistration: String? = null

    @NotEmpty(message = "Required field")
    var shipmentLineBrandName: String? = null
}

@JsonIgnoreProperties(ignoreUnknown = true)
class CoiEntityForm {

    @NotEmpty(message = "Required field")
    var coiNumber: String? = null

    @NotEmpty(message = "Required field")
    var idfNumber: String? = null

    @NotEmpty(message = "Required field")
    var rfiNumber: String? = null

    @NotEmpty(message = "Required field")
    var ucrNumber: String? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var rfcDate: Timestamp? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var cocIssueDate: Timestamp? = null


    @NotEmpty(message = "Required field")
    var clean: String? = null

    @NotEmpty(message = "Required field")
    var cocRemarks: String? = null


    @NotEmpty(message = "Required field")
    var issuingOffice: String? = null

    @NotEmpty(message = "Required field")
    var importerName: String? = null

    @NotEmpty(message = "Required field")
    var importerPin: String? = null

    @NotEmpty(message = "Required field")
    var importerAddress1: String? = null


    @NotEmpty(message = "Required field")
    var importerAddress2: String? = null

    @NotEmpty(message = "Required field")
    var importerCity: String? = null

    @NotEmpty(message = "Required field")
    var importerCountry: String? = null

    @NotEmpty(message = "Required field")
    var importerZipCode: String? = null

    @NotEmpty(message = "Required field")
    var importerTelephoneNumber: String? = null


    @NotEmpty(message = "Required field")
    var importerFaxNumber: String? = null

    @NotEmpty(message = "Required field")
    var importerEmail: String? = null

    @NotEmpty(message = "Required field")
    var exporterName: String? = null

    @NotEmpty(message = "Required field")
    var exporterPin: String? = null

    @NotEmpty(message = "Required field")
    var exporterAddress1: String? = null


    @NotEmpty(message = "Required field")
    var exporterAddress2: String? = null

    @NotEmpty(message = "Required field")
    var exporterCity: String? = null

    @NotEmpty(message = "Required field")
    var exporterCountry: String? = null

    @NotEmpty(message = "Required field")
    var exporterZipCode: String? = null

    @NotEmpty(message = "Required field")
    var exporterTelephoneNumber: String? = null

    @NotEmpty(message = "Required field")
    var exporterFaxNumber: String? = null

    @NotEmpty(message = "Required field")
    var exporterEmail: String? = null

    @NotEmpty(message = "Required field")
    var placeOfInspection: String? = null

    @NotNull(message = "Required field")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var dateOfInspection: Timestamp? = null

    @NotEmpty(message = "Required field")
    var portOfDestination: String? = null

    @NotEmpty(message = "Required field")
    var shipmentMode: String? = null

    @NotEmpty(message = "Required field")
    var countryOfSupply: String? = null

    @NotNull(message = "Required field")
    var finalInvoiceFobValue: Double = 0.0

    @NotNull(message = "Required field")
    var finalInvoiceExchangeRate: Double = 0.0

    @NotEmpty(message = "Required field")
    var finalInvoiceCurrency: String? = null

    @NotEmpty(message = "Required field")
    var finalInvoiceNumber: String? = null

    @NotNull(message = "Required field")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var finalInvoiceDate: Timestamp? = null

    var shipmentSealNumbers: String? = null

    var shipmentContainerNumber: String? = null

    var shipmentGrossWeight: String? = null

    @NotEmpty(message = "Required field")
    var route: String? = null

    @NotEmpty(message = "Required field")
    var coiItems: List<CoiItem>? = null

    @NotEmpty(message = "Required field")
    var inspectionFee: DocumentPaymentDetails? = null

    @NotEmpty(message = "Required field")
    var version: Long? = null
}

class CorEntityForm {
    @NotEmpty(message = "Please enter your phone number")
    var corNumber: String? = null

    @NotEmpty(message = "Please enter your phone number")
    var ucrNumber: String? = null

    @NotEmpty(message = "Please enter your phone number")
    var corIssueDate: String? = null

    @NotEmpty(message = "Please enter your phone number")
    var countryOfSupply: String? = null

    @NotEmpty(message = "Please enter your phone number")
    var inspectionCenter: String? = null

    @NotEmpty(message = "Please enter your phone number")
    var exporterName: String? = null

    @NotEmpty(message = "Please enter your phone number")
    var exporterAddress1: String? = null

    @NotEmpty(message = "Please enter your phone number")
    var exporterAddress2: String? = null

    @NotEmpty(message = "Please enter your phone number")
    var exporterEmail: String? = null

    @NotEmpty(message = "Please enter your phone number")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var applicationBookingDate: Timestamp? = null

    @NotEmpty(message = "Please enter your phone number")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var inspectionDate: Timestamp? = null

    @NotEmpty(message = "Please enter your phone number")
    var make: String? = null

    @NotEmpty(message = "Please enter your phone number")
    var model: String? = null

    @NotEmpty(message = "Please enter your phone number")
    var chasisNumber: String? = null

    @NotEmpty(message = "Please enter your phone number")
    var engineNumber: String? = null

    @NotEmpty(message = "Please enter your phone number")
    var engineCapacity: String? = null

    @NotEmpty(message = "Please enter your phone number")
    var yearOfManufacture: String? = null

    @NotEmpty(message = "Please enter your phone number")
    var yearOfFirstRegistration: String? = null

    @NotEmpty(message = "Please enter your phone number")
    var inspectionMileage: String? = null

    @NotEmpty(message = "Please enter your phone number")
    var unitsOfMileage: String? = null

    @NotEmpty(message = "Please enter your phone number")
    var inspectionRemarks: String? = null

    @NotEmpty(message = "Please enter your phone number")
    var previousRegistrationNumber: String? = null

    @NotEmpty(message = "Please enter your phone number")
    var previousCountryOfRegistration: String? = null

    @NotEmpty(message = "Please enter your phone number")
    var tareWeight: Long? = null

    @NotEmpty(message = "Please enter your phone number")
    var loadCapacity: Long? = null

    @NotEmpty(message = "Please enter your phone number")
    var grossWeight: Long? = null

    @NotEmpty(message = "Please enter your phone number")
    var numberOfAxles: Long? = null

    @NotEmpty(message = "Please enter your phone number")
    var typeOfVehicle: String? = null

    @NotEmpty(message = "Please enter your phone number")
    var numberOfPassengers: Long? = null

    @NotEmpty(message = "Please enter your phone number")
    var typeOfBody: String? = null

    @NotEmpty(message = "Please enter your phone number")
    var bodyColor: String? = null

    @NotEmpty(message = "Please enter your phone number")
    var fuelType: String? = null

    @NotEmpty(message = "Please enter your phone number")
    var inspectionFee: Double? = null

    @NotEmpty(message = "Required field")
    var transmission: String? = null

    @NotEmpty(message = "Required field")
    var inspectionOfficer: String? = null

    @NotEmpty(message = "Please enter your phone number")
    var inspectionFeeCurrency: String? = null

    @NotEmpty(message = "Please enter your phone number")
    var inspectionFeeExchangeRate: Long? = null

    @NotEmpty(message = "Please enter your phone number")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var inspectionFeePaymentDate: Timestamp? = null

    @NotEmpty(message = "Required field")
    var version: Long? = null
}

class RfcCoiItem {
    @NotEmpty(message = "Required field")
    var declaredHsCode: String? = null

    @NotEmpty(message = "Required field")
    var itemQuantity: String? = null

    @NotEmpty(message = "Required field")
    var productDescription: String? = null

    @NotEmpty(message = "Required field")
    var ownerPin: String? = null

    @NotEmpty(message = "Required field")
    var ownerName: String? = null
}

@JsonIgnoreProperties(ignoreUnknown = true)
class RfcCoiEntityForm {

    @NotEmpty(message = "Required field")
    var rfcNumber: String? = null

    @NotEmpty(message = "Required field")
    var idfNumber: String? = null

    @NotEmpty(message = "Required field")
    var ucrNumber: String? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var rfcDate: Date? = null

    @NotEmpty(message = "Required field")
    var countryOfDestination: String? = null

    @NotEmpty(message = "Required field")
    var applicationType: String? = null

    @NotEmpty(message = "Required field")
    var sorReference: String? = null

    @NotEmpty(message = "Required field")
    var solReference: String? = null

    @NotEmpty(message = "Required field")
    var importerName: String? = null

    @NotEmpty(message = "Required field")
    var importerPin: String? = null

    @NotEmpty(message = "Required field")
    var importerAddress1: String? = null


    @NotEmpty(message = "Required field")
    var importerAddress2: String? = null

    @NotEmpty(message = "Required field")
    var importerCity: String? = null

    @NotEmpty(message = "Required field")
    var importerCountry: String? = null

    @NotEmpty(message = "Required field")
    var importerZipCode: String? = null

    @NotEmpty(message = "Required field")
    var importerTelephoneNumber: String? = null


    @NotEmpty(message = "Required field")
    var importerFaxNumber: String? = null

    @NotEmpty(message = "Required field")
    var importerEmail: String? = null

    @NotEmpty(message = "Required field")
    var exporterName: String? = null

    @NotEmpty(message = "Required field")
    var exporterPin: String? = null

    @NotEmpty(message = "Required field")
    var exporterAddress1: String? = null


    @NotEmpty(message = "Required field")
    var exporterAddress2: String? = null

    @NotEmpty(message = "Required field")
    var exporterCity: String? = null

    @NotEmpty(message = "Required field")
    var exporterCountry: String? = null

    @NotEmpty(message = "Required field")
    var exporterZipCode: String? = null

    @NotEmpty(message = "Required field")
    var exporterTelephoneNumber: String? = null

    @NotEmpty(message = "Required field")
    var exporterFaxNumber: String? = null

    @NotEmpty(message = "Required field")
    var exporterEmail: String? = null

    @NotEmpty(message = "Required field")
    var placeOfInspection: String? = null

    @NotNull(message = "Required field")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var dateOfInspection: Timestamp? = null

    @NotEmpty(message = "Required field")
    var placeOfInspectionAddress: String? = null

    @NotEmpty(message = "Required field")
    var placeOfInspectionEmail: String? = null

    @NotEmpty(message = "Required field")
    var placeOfInspectionContacts: String? = null


    @NotEmpty(message = "Required field")
    var portOfLoading: String? = null

    @NotEmpty(message = "Required field")
    var portOfDischarge: String? = null

    @NotEmpty(message = "Required field")
    var shipmentMode: String? = null

    @NotEmpty(message = "Required field")
    var countryOfSupply: String? = null

    @NotEmpty(message = "Required field")
    var route: String? = null

    @NotEmpty(message = "Required field")
    var goodsCondition: String? = null

    @NotEmpty(message = "Required field")
    var assemblyState: String? = null

    @NotEmpty(message = "Required field")
    var linkToAttachedDocuments: List<String>? = null

    @NotEmpty(message = "Required field")
    var items: List<RfcCoiItem>? = null

    var inspectionFee: DocumentPaymentDetails? = null
}

class IdfItem {
    @NotEmpty(message = "Required field")
    var itemDescription: String? = null

    @NotEmpty(message = "Required field")
    var hsCode: String? = null

    @NotEmpty(message = "Required field")
    var quantity: Long? = null

    @NotEmpty(message = "Required field")
    var newUsed: String? = null

    @NotEmpty(message = "Required field")
    var unitOfMeasure: String? = null

    @NotEmpty(message = "Required field")
    var applicableStandard: String? = null

    @NotEmpty(message = "Required field")
    var itemCost: Long? = null
}

@JsonIgnoreProperties(ignoreUnknown = true)
open class IdfEntityForm {
    @NotEmpty(message = "Required field")
    var idfNumber: String? = null

    @NotEmpty(message = "Required field")
    var ucrNumber: String? = null

    @NotEmpty(message = "Required field")
    var importerName: String? = null

    @NotEmpty(message = "Required field")
    var importerAddress: String? = null

    @NotEmpty(message = "Required field")
    var importerEmail: String? = null

    @NotEmpty(message = "Required field")
    var importerTelephoneNumber: String? = null

    @NotEmpty(message = "Required field")
    var importerContactName: String? = null

    @NotEmpty(message = "Required field")
    var importerFaxNumber: String? = null

    @NotEmpty(message = "Required field")
    var exporterName: String? = null

    @NotEmpty(message = "Required field")
    var exporterAddress: String? = null

    @NotEmpty(message = "Required field")
    var exporterEmail: String? = null

    @NotEmpty(message = "Required field")
    var exporterTelephoneNumber: String? = null

    @NotEmpty(message = "Required field")
    var exporterContactName: String? = null

    @NotEmpty(message = "Required field")
    var exporterFaxNumber: String? = null


    @NotEmpty(message = "Required field")
    var countryOfSupply: String? = null

    @NotEmpty(message = "Required field")
    var portOfDischarge: String? = null

    @NotEmpty(message = "Required field")
    var portOfCustomsClearance: String? = null

    @NotEmpty(message = "Required field")
    var modeOfTransport: String? = null

    @NotEmpty(message = "Required field")
    var comesa: String? = null

    @NotEmpty(message = "Required field")
    var invoiceNumber: String? = null

    @NotEmpty(message = "Required field")
    var invoiceDate: Timestamp? = null

    @NotEmpty(message = "Required field")
    var currency: String? = null

    @NotEmpty(message = "Required field")
    var exchangeRate: Double? = null

    @NotEmpty(message = "Required field")
    var fobValue: Double? = null

    @NotEmpty(message = "Required field")
    var freight: Double? = null

    @NotEmpty(message = "Required field")
    var insurance: Double? = null

    @NotEmpty(message = "Required field")
    var otherChargers: Double? = null

    @NotEmpty(message = "Required field")
    var total: Double? = null

    @NotEmpty(message = "Required field")
    var observations: String? = null

    @NotEmpty(message = "Required field")
    var usedStatus: String? = null

    @NotEmpty(message = "Required field")
    var items: List<IdfItem>? = null
}

class IdfCorForm: IdfEntityForm() {
    @NotEmpty(message = "Required field")
    var chassisNumber: String? = null

    @NotEmpty(message = "Required field")
    var engineNumber: String? = null

    @NotEmpty(message = "Required field")
    var yearOfManufacture: String? = null

    @NotEmpty(message = "Required field")
    var yearOfFirstRegistration: String? = null
}

class RiskProfileForm {
    @NotEmpty(message = "Required field")
    var hsCode: String? = null

    @NotEmpty(message = "Required field")
    var brandName: String? = null

    @NotEmpty(message = "Required field")
    var productDescription: String? = null

    @NotEmpty(message = "Required field")
    var countryOfSupply: String? = null

    @NotEmpty(message = "Required field")
    var manufacturer: String? = null

    @NotEmpty(message = "Required field")
    var traderName: String? = null

    @NotEmpty(message = "Required field")
    var importerName: String? = null

    @NotEmpty(message = "Required field")
    var importerPin: String? = null

    @NotEmpty(message = "Required field")
    var exporterName: String? = null

    @NotEmpty(message = "Required field")
    var exporterPin: String? = null

    @NotEmpty(message = "Required field")
    var riskLevel: String? = null

    @NotEmpty(message = "Required field")
    var riskDescription: String? = null

    @NotEmpty(message = "Required field")
    var remarks: String? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var categorizationDate: java.sql.Date? = null
}

class PvocComplaintForm {
    @NotEmpty(message = "Please enter your name")
    var complaintName: String? = null

    @NotEmpty(message = "Please enter your phone number")
    var phoneNo: String? = null

    @NotEmpty(message = "Please enter you city")
    var address: String? = null

    var cocNo: String? = null

    var rfcNo: String? = null

    @NotEmpty(message = "Please enter email address")
    @Email(message = "Please enter valid email address")
    var email: String? = null

    @NotEmpty(message = "Please enter complaint")
    var complaintDescription: String? = null

    @NotNull(message = "Please select agent")
    var pvocAgent: Long? = null

    @NotNull(message = "Please select nature of complaint")
    var categoryId: Long? = null

    @NotNull(message = "Please select nature type of complaint")
    var subCategoryId: Long? = null

}

class ExemptionStatusForm {
    @NotNull(message = "Task Identifier is required")
    val taskId: String? = null

    @NotNull(message = "Please select Approval Status")
    val status: String? = null

    @NotNull(message = "Please select certificate validity")
    val certificateValidity: String? = null

    @NotNull(message = "Rejection/Approval remarks are required")
    val remarks: String? = null
}

class ComplaintStatusForm {
    @NotNull(message = "Task Identifier is required")
    val taskId: String? = null

    @NotNull(message = "Please select Approval Status")
    val status: String? = null

    @NotNull(message = "Please select action")
    val action: String? = null

    @NotNull(message = "Rejection/Approval remarks are required")
    val remarks: String? = null
}

class WaiverStatusForm {
    @NotNull(message = "Task Identifier is required")
    val taskId: String? = null

    @NotNull(message = "Please select action")
    val action: String? = null

    @NotNull(message = "Rejection/Approval remarks are required")
    val remarks: String? = null
}