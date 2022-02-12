package org.kebs.app.kotlin.apollo.api.payload.request

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocPartnersEntity
import java.sql.Date
import java.sql.Timestamp
import javax.validation.Valid
import javax.validation.constraints.Email
import javax.validation.constraints.Min
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
    @JsonAlias("SHIPMENT_PARTIAL_NUMBER")
    var shipmentPartialNumber: Long = 0

    @NotEmpty(message = "Required field")
    @JsonAlias("SHIPMENT_SEAL_NUMBERS")
    var shipmentSealNumbers: String? = null

    @JsonAlias("SHIPMENT_CONTAINER_NUMBER")
    var shipmentContainerNumber: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("SHIPMENT_GROSS_WEIGHT")
    var shipmentGrossWeight: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("SHIPMENT_QUANTITY_DELIVERED")
    var shipmentQuantityDelivered: String? = null

    @NotNull(message = "Required field")
    @JsonAlias("SHIPMENT_LINE_NUMBER")
    var shipmentLineNumber: Long = 0

    @NotEmpty(message = "Required field")
    @JsonAlias("SHIPMENT_LINE_HS_CODE")
    var shipmentLineHscode: String? = null

    @NotNull(message = "Required field")
    @JsonAlias("SHIPMENT_LINE_QUANTITY")
    var shipmentLineQuantity: Double = 0.0

    @NotEmpty(message = "Required field")
    @JsonAlias("Shipment Line Unit of Measure")
    var shipmentLineUnitofMeasure: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("SHIPMENT_LINE_DESCRIPTION")
    var shipmentLineDescription: String? = null

    @JsonAlias("SHIPMENT_LINE_VIN")
    var shipmentLineVin: String? = null


    @JsonAlias("SHIPMENT_LINE_STICKER_NUMBER")
    var shipmentLineStickerNumber: String? = null

    @JsonAlias("SHIPMENT_LINE_ICS")
    var shipmentLineIcs: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("SHIPMENT_LINE_STANDARDS_REFERENCE")
    var shipmentLineStandardsReference: String? = null

    @JsonAlias("SHIPMENT_LINE_LICENCE_REFERENCE")
    var shipmentLineLicenceReference: String? = null

    @JsonAlias("SHIPMENT_LINE_REGISTRATION")
    var shipmentLineRegistration: String? = null

    @JsonAlias("SHIPMENT_BRAND_NAME")
    var shipmentBrandName: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("PRODUCT_CATEGORY")
    var productCategory: String? = null

}

class DocumentPaymentDetails {

    @NotNull(message = "Required field")
    @JsonAlias("INSPECTION_VALUE")
    var inspectionValue: Double = 0.0

    @NotNull(message = "Required field")
    @JsonAlias("INSPECTION_PENALTY")
    var penaltyValue: Double = 0.0

    @NotNull(message = "Required field")
    @JsonAlias("INSPECTION_TAX")
    var taxValue: Double = 0.0

    @NotNull(message = "Required field")
    @JsonAlias("INSPECTION_EXCHANGE_RATE")
    var innvoiceExchangeRate: Double = 0.0

    @NotEmpty(message = "Required field")
    @JsonAlias("INSPECTION_CURRENCY")
    var inspectionCurrency: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("RECEIPT_NUMBER")
    var receiptNumber: String? = null

    @NotNull(message = "Required field")
    @JsonAlias("PAYMENT_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var paymentDate: Timestamp? = null
}

@JsonIgnoreProperties(ignoreUnknown = true)
class CocEntityForm {

    @JsonAlias("COC_NUMBER")
    var cocNumber: String? = null

    @JsonAlias("IDF NUMBER")
    var idfNumber: String? = null

    @JsonAlias("RFI_NUMBER")
    var rfiNumber: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("UCR_NUMBER")
    var ucrNumber: String? = null

    @JsonAlias("RFC_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var rfcDate: Timestamp? = null


    @JsonAlias("COC_ISSUED_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var cocIssueDate: Timestamp? = null


    @NotEmpty(message = "Required field")
    @JsonAlias("CLEAN")
    var clean: String? = null

    @JsonAlias("COC_REMARKS")
    var cocRemarks: String? = null


    @NotEmpty(message = "Required field")
    @JsonAlias("ISSUING_OFFICE")
    var issuingOffice: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("IMPORTER_NAME")
    var importerName: String? = null

    @JsonAlias("IMPORTER_PIN")
    var importerPin: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("IMPORTER_ADDRESS_1")
    var importerAddress1: String? = null


    @JsonAlias("IMPORTER_ADDRESS_2")
    var importerAddress2: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("IMPORTER_CITY")
    var importerCity: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("IMPORTER_COUNTRY")
    var importerCountry: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("IMPORTER_ZIP_CODE")
    var importerZipCode: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("IMPORTER_TELEPHONE_NUMBER")
    var importerTelephoneNumber: String? = null


    @JsonAlias("IMPORTER_FAX_NUMBER")
    var importerFaxNumber: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("IMPORTER_EMAIL")
    var importerEmail: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("EXPORTER_NAME")
    var exporterName: String? = null

    @JsonAlias("EXPORTER_PIN")
    var exporterPin: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("EXPORTER_ADDRESS_1")
    var exporterAddress1: String? = null


    @JsonAlias("EXPORTER_ADDRESS_2")
    var exporterAddress2: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("EXPORTER_CITY")
    var exporterCity: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("EXPORTER_COUNTRY")
    var exporterCountry: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("EXPORTER_ZIP_CODE")
    var exporterZipCode: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("EXPORTER_TELEPHONE_NUMBER")
    var exporterTelephoneNumber: String? = null

    @JsonAlias("EXPORTER_FAX_NUMBER")
    var exporterFaxNumber: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("EXPORTER_EMAIL")
    var exporterEmail: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("PLACE_OF_INSPECTION")
    var placeOfInspection: String? = null

    @NotNull(message = "Required field")
    @JsonAlias("DATE_OF_INSPECTION")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var dateOfInspection: Timestamp? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("PORT_OF_DESTINATION")
    var portOfDestination: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("SHIPMENT_MODE")
    var shipmentMode: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("SHIPMENT_SEAL_NUMBER")
    var shipmentSealNumbers: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("SHIPMENT_CONTAINER_NUMBER")
    var shipmentContainerNumber: String? = null

    @NotNull(message = "Required field")
    @Min(value = 0, message = "Gross weight should not be negative")
    @JsonAlias("SHIPMENT_GROSS_WEIGHT")
    var shipmentGrossWeight: Double? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("COUNTRY_OF_SUPPLY")
    var countryOfSupply: String? = null

    @NotNull(message = "Required field")
    @Min(value = 0, message = "FOB invoice value should not be negative")
    @JsonAlias("FINAL_INVOICE_FOB_VALUE")
    var finalInvoiceFobValue: Double = 0.0

    @NotNull(message = "Required field")
    @Min(value = 0, message = "Tax rate should not be negative")
    @JsonAlias("FINAL_INVOICE_EXCHANGE_RATE")
    var finalInvoiceExchangeRate: Double = 0.0

    @NotEmpty(message = "Required field")
    @JsonAlias("FINAL_INVOICE_CURRENCY")
    var finalInvoiceCurrency: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("FINAL_INVOICE_NUMBER")
    var finalInvoiceNumber: String? = null

    @NotNull(message = "Required field")
    @JsonAlias("FINAL_INVOICE_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var finalInvoiceDate: Timestamp? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("ROUTE")
    var route: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("PRODUCT")
    var cocItems: List<CocItem>? = null

    @NotNull(message = "Required field")
    @JsonAlias("INSPECTION_FEE")
    var inspectionFee: DocumentPaymentDetails? = null

    @NotNull(message = "Required field")
    @Min(value = 1, message = "Version should be greater than or equal to one")
    @JsonAlias("VERSION")
    var version: Long? = null
}

@JsonIgnoreProperties(ignoreUnknown = true)
class NcrEntityForm {

    @JsonAlias("COC_NUMBER")
    var ncrNumber: String? = null

    @JsonAlias("IDF NUMBER")
    var idfNumber: String? = null

    @JsonAlias("RFI_NUMBER")
    var rfiNumber: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("UCR_NUMBER")
    var ucrNumber: String? = null

    @JsonAlias("RFC_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var rfcDate: Timestamp? = null


    @JsonAlias("COC_ISSUED_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var ncrIssueDate: Timestamp? = null


    @NotEmpty(message = "Required field")
    @JsonAlias("CLEAN")
    var clean: String? = null

    @JsonAlias("COC_REMARKS")
    var ncrRemarks: String? = null


    @NotEmpty(message = "Required field")
    @JsonAlias("ISSUING_OFFICE")
    var issuingOffice: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("IMPORTER_NAME")
    var importerName: String? = null

    @JsonAlias("IMPORTER_PIN")
    var importerPin: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("IMPORTER_ADDRESS_1")
    var importerAddress1: String? = null


    @JsonAlias("IMPORTER_ADDRESS_2")
    var importerAddress2: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("IMPORTER_CITY")
    var importerCity: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("IMPORTER_COUNTRY")
    var importerCountry: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("IMPORTER_ZIP_CODE")
    var importerZipCode: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("IMPORTER_TELEPHONE_NUMBER")
    var importerTelephoneNumber: String? = null


    @JsonAlias("IMPORTER_FAX_NUMBER")
    var importerFaxNumber: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("IMPORTER_EMAIL")
    var importerEmail: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("EXPORTER_NAME")
    var exporterName: String? = null

    @JsonAlias("EXPORTER_PIN")
    var exporterPin: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("EXPORTER_ADDRESS_1")
    var exporterAddress1: String? = null


    @JsonAlias("EXPORTER_ADDRESS_2")
    var exporterAddress2: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("EXPORTER_CITY")
    var exporterCity: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("EXPORTER_COUNTRY")
    var exporterCountry: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("EXPORTER_ZIP_CODE")
    var exporterZipCode: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("EXPORTER_TELEPHONE_NUMBER")
    var exporterTelephoneNumber: String? = null

    @JsonAlias("EXPORTER_FAX_NUMBER")
    var exporterFaxNumber: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("EXPORTER_EMAIL")
    var exporterEmail: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("PLACE_OF_INSPECTION")
    var placeOfInspection: String? = null

    @NotNull(message = "Required field")
    @JsonAlias("DATE_OF_INSPECTION")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var dateOfInspection: Timestamp? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("PORT_OF_DESTINATION")
    var portOfDestination: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("SHIPMENT_MODE")
    var shipmentMode: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("SHIPMENT_SEAL_NUMBER")
    var shipmentSealNumbers: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("SHIPMENT_CONTAINER_NUMBER")
    var shipmentContainerNumber: String? = null

    @NotNull(message = "Required field")
    @Min(value = 0, message = "Gross weight should not be negative")
    @JsonAlias("SHIPMENT_GROSS_WEIGHT")
    var shipmentGrossWeight: Double? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("COUNTRY_OF_SUPPLY")
    var countryOfSupply: String? = null

    @NotNull(message = "Required field")
    @Min(value = 0, message = "FOB invoice value should not be negative")
    @JsonAlias("FINAL_INVOICE_FOB_VALUE")
    var finalInvoiceFobValue: Double = 0.0

    @NotNull(message = "Required field")
    @Min(value = 0, message = "Tax rate should not be negative")
    @JsonAlias("FINAL_INVOICE_EXCHANGE_RATE")
    var finalInvoiceExchangeRate: Double = 0.0

    @NotEmpty(message = "Required field")
    @JsonAlias("FINAL_INVOICE_CURRENCY")
    var finalInvoiceCurrency: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("FINAL_INVOICE_NUMBER")
    var finalInvoiceNumber: String? = null

    @NotNull(message = "Required field")
    @JsonAlias("FINAL_INVOICE_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var finalInvoiceDate: Timestamp? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("ROUTE")
    var route: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("NCR_ITEMS")
    var ncrItems: List<CocItem>? = null

    @NotNull(message = "Required field")
    @Min(value = 1, message = "Version should be greater than or equal to one")
    @JsonAlias("VERSION")
    var version: Long? = null
}

class CoiItem {

    @NotEmpty(message = "Required field")
    var declaredHsCode: String = ""

    @NotEmpty(message = "Required field")
    var productDescription: String? = ""

    @NotEmpty(message = "Required field")
    var shipmentLineHsCode: String? = ""

    @NotNull(message = "Required field")
    @Min(value = 0, message = "Shipment Line number cannot be negative")
    var shipmentLineNumber: Long? = 0

    @NotNull(message = "Required field")
    @Min(value = 0, message = "Quantity cannot be negative")
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
    var coiRemarks: String? = null


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

    @NotEmpty(message = "Required field")
    var shipmentSealNumbers: String? = null

    @NotEmpty(message = "Required field")
    var shipmentContainerNumber: String? = null

    @NotNull(message = "Required field")
    @Min(value = 0, message = "Minimum value is 0")
    var shipmentGrossWeight: Double? = null

    @NotEmpty(message = "Required field")
    var route: String? = null

    @Valid
    @NotEmpty(message = "Required field")
    @NotNull(message = "Required field")
    var coiItems: List<CoiItem>? = null

    @Valid
    @NotNull(message = "Required field")
    var inspectionFee: DocumentPaymentDetails? = null

    @NotNull(message = "Required field")
    @Min(value = 1, message = "Version should not be less than one")
    var version: Long? = null
}

class CorEntityForm {
    @NotEmpty(message = "COR numnber is required")
    var corNumber: String? = null

    @NotNull(message = "UCR Number is required")
    var ucrNumber: String? = null

    @NotNull(message = "COR issue date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var corIssueDate: Timestamp? = null

    @NotEmpty(message = "Country of supply is required")
    var countryOfSupply: String? = null

    @NotEmpty(message = "Inspection center is required")
    var inspectionCenter: String? = null

    @NotEmpty(message = "Required field")
    var exporterName: String? = null

    @NotEmpty(message = "Required field")
    var exporterAddress1: String? = null

    @NotEmpty(message = "Required field")
    var exporterAddress2: String? = null

    @NotEmpty(message = "Required field")
    var exporterEmail: String? = null

    @NotNull(message = "Booking date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var applicationBookingDate: Timestamp? = null

    @NotNull(message = "Inspection date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var inspectionDate: Timestamp? = null

    @NotEmpty(message = "Make is required")
    var make: String? = null

    @NotEmpty(message = "Field is required")
    var model: String? = null

    @NotEmpty(message = "Required field")
    var chasisNumber: String? = null

    @NotEmpty(message = "Required field")
    var engineNumber: String? = null

    @NotEmpty(message = "Required field")
    var engineCapacity: String? = null

    @NotEmpty(message = "Required field")
    var yearOfManufacture: String? = null

    @NotEmpty(message = "Required field")
    var yearOfFirstRegistration: String? = null

    @NotEmpty(message = "Required field")
    var inspectionMileage: String? = null

    @NotEmpty(message = "Required field")
    var unitsOfMileage: String? = null

    @NotEmpty(message = "Required field")
    var inspectionRemarks: String? = null

    @NotEmpty(message = "Required field")
    var previousRegistrationNumber: String? = null

    @NotEmpty(message = "Required field")
    var previousCountryOfRegistration: String? = null

    @NotNull(message = "Required field")
    @Min(value = 0, message = "Tare weight cannot be negative")
    var tareWeight: Double? = null

    @NotNull(message = "Required field")
    @Min(value = 0, message = "Load capacity cannot be negative")
    var loadCapacity: Double? = null

    @NotNull(message = "Required field")
    @Min(value = 0, message = "Gross weight cannot be negative")
    var grossWeight: Double? = null

    @NotNull(message = "Required field")
    @Min(value = 0, message = "Axles cannot be negative")
    var numberOfAxles: Long? = null

    @NotEmpty(message = "Required field")
    var typeOfVehicle: String? = null

    @NotNull(message = "Required field")
    @Min(value = 0, message = "Number of passengers cannot be negative")
    var numberOfPassengers: Long? = null

    @NotEmpty(message = "Required field")
    var typeOfBody: String? = null

    @NotEmpty(message = "Required field")
    var bodyColor: String? = null

    @NotEmpty(message = "Required field")
    var fuelType: String? = null

    @NotNull(message = "Required field")
    @Min(value = 0, message = "Inspection fee cannot be negative")
    var inspectionFee: Double? = null

    @NotEmpty(message = "Required field")
    var transmission: String? = null

    @NotEmpty(message = "Required field")
    var inspectionOfficer: String? = null

    @NotEmpty(message = "Required field")
    var inspectionFeeCurrency: String? = null

    @NotNull(message = "Required field")
    @Min(value = 0, message = "Exchange rate cannot be negative")
    var inspectionFeeExchangeRate: Double? = null

    @NotNull(message = "Required field")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var inspectionFeePaymentDate: Timestamp? = null

    @NotNull(message = "Required field")
    @Min(value = 1, message = "Varsion cannot be less than one")
    var version: Long? = null
}

class RfcCoiItem {
    @NotEmpty(message = "Required field")
    var declaredHsCode: String? = null

    @NotNull(message = "Required field")
    @Min(value = 0, message = "Quantity cannot be negative")
    var itemQuantity: Long? = null

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
    var shipmentMethod: String? = null

    @NotEmpty(message = "Required field")
    var countryOfSupply: String? = null

    @NotEmpty(message = "Required field")
    var route: String? = null

    @NotEmpty(message = "Required field")
    var goodsCondition: String? = null

    @NotEmpty(message = "Required field")
    var assemblyState: String? = null

    @NotEmpty(message = "Required field")
    var linkToAttachedDocuments: String? = null

    @Valid
    @NotEmpty(message = "Required field")
    var items: List<RfcCoiItem>? = null
}

class IdfItem {
    @NotEmpty(message = "Required field")
    var itemDescription: String? = null

    @NotEmpty(message = "Required field")
    var hsCode: String? = null

    @NotNull(message = "Required field")
    @Min(value = 0, message = "Quantity should be greater than or equal to zero")
    var quantity: Long? = null

    @NotNull(message = "Required field")
    var used: Boolean? = null

    @NotEmpty(message = "Required field")
    var unitOfMeasure: String? = null

    @NotEmpty(message = "Required field")
    var applicableStandard: String? = null

    @NotNull(message = "Required field")
    @Min(value = 0, message = "Item Cost should be greater than or equal to zero")
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

    @NotNull(message = "Required field")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var invoiceDate: Timestamp? = null

    @NotEmpty(message = "Required field")
    var currency: String? = null

    @NotNull(message = "Required field")
    @Min(value = 0, message = "Exchange rate should be greater than or equal to zero")
    var exchangeRate: Double? = null

    @NotNull(message = "Required field")
    @Min(value = 0, message = "FOB value should be greater than or equal to zero")
    var fobValue: Double? = null

    @NotNull(message = "Required field")
    @Min(value = 0, message = "Freight rate should be greater than or equal to zero")
    var freight: Double? = null

    @NotNull(message = "Required field")
    @Min(value = 0, message = "Insurance amount should be greater than or equal to zero")
    var insurance: Double? = null

    @NotNull(message = "Required field")
    @Min(value = 0, message = "Charges should be greater than or equal to zero")
    var otherCharges: Double? = null

    @NotNull(message = "Required field")
    @Min(value = 0, message = "Total charges should be greater than or equal to zero")
    var total: Double? = null

    @NotEmpty(message = "Required field")
    var observations: String? = null

    @NotNull(message = "Required field")
    var usedStatus: Boolean? = null

    @Valid
    @NotEmpty(message = "Required field")
    var items: List<IdfItem>? = null
}

class IdfCorForm : IdfEntityForm() {
    @NotEmpty(message = "Required field")
    var chassisNumber: String? = null

    @NotEmpty(message = "Required field")
    var engineNumber: String? = null

    @NotEmpty(message = "Required field")
    var yearOfManufacture: String? = null

    @NotEmpty(message = "Required field")
    var yearOfFirstRegistration: String? = null
}

class PvocResponseModel {
    @NotEmpty(message = "Required field")
    val responseCode: String? = null

    @NotEmpty(message = "Required field")
    val responseMessage: String? = null
    val data: Map<String, Any?>? = null
}

class PvocKebsQueryForm {
    var queryReference: String? = null
    var documentType: String? = null
    var certNumber: String? = null
    var rfcNumber: String? = null
    var invoiceNumber: String? = null
    var ucrNumber: String? = null
    var partnerQuery: String? = null
}

class PvocQueryResponse {
    var documentType: String? = null
    var certNumber: String? = null
    var queryReference: String? = null
    var queryResponse: String? = null
    var queryAnalysis: String? = null
    var conclusion: String? = null
    var linkToUploads: String? = null
}

class KebsPvocQueryForm {
    var queryReference: String? = null
    var documentType: String? = null
    var certNumber: String? = null
    var rfcNumber: String? = null
    var invoiceNumber: String? = null
    var ucrNumber: String? = null
    var kebsQuery: String? = null
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