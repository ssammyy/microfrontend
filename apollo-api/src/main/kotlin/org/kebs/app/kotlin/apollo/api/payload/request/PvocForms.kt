package org.kebs.app.kotlin.apollo.api.payload.request

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.kebs.app.kotlin.apollo.api.service.AmountInWordsService
import org.kebs.app.kotlin.apollo.api.service.BillStatus
import org.kebs.app.kotlin.apollo.api.service.ValidateUrl
import org.kebs.app.kotlin.apollo.store.model.PaymentMethodsEntity
import org.kebs.app.kotlin.apollo.store.model.di.IDFDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.di.IDFItemDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.invoice.BillPayments
import org.kebs.app.kotlin.apollo.store.model.pvc.*
import java.math.BigDecimal
import java.math.RoundingMode
import java.sql.Date
import java.sql.Timestamp
import javax.validation.Valid
import javax.validation.constraints.*
class PvocContainerDetails {
    @Min(value = 0, message = "Weight should not be negative, minimum is 0")
    @JsonAlias("SHIPMENT_GROSS_WEIGHT")
    var shipmentGrossWeight: Double? = null

    @JsonAlias("SHIPMENT_CONTAINER_NUMBER")
    @Size(max = 200, message = "Description should be upto 200 characters")
    var shipmentContainerNumber: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Description should be upto 150 characters")
    @JsonAlias("SHIPMENT_SEAL_NUMBERS")
    var shipmentSealNumbers: String? = null

    @NotNull(message = "Required field")
    @Min(value = 0, message = "Should not be negative, minimum is 0")
    @JsonAlias("SHIPMENT_PARTIAL_NUMBER")
    var shipmentPartialNumber: Long = 0
}

class PvocPartnersForms {
    @NotNull(message = "Partner Ref Number is required")
    var partnerRefNo: String? = null

    @NotNull(message = "Group Code Number is required")
    var groupCode: String? = null

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
    var mouDays: Long = 0


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
    @Min(value = 0, message = "Quantity should not be negative, minimum is 0")
    @JsonAlias("SHIPMENT_LINE_NUMBER")
    var shipmentLineNumber: Long = 0

    @NotEmpty(message = "Required field")
    @Pattern(regexp = "^[A-Za-z0-9_/-]+$", message = "Alphanumeric values expected for this field")
    @Size(max = 100, message = "Should be upto 100 characters")
    @JsonAlias("SHIPMENT_LINE_HS_CODE")
    var shipmentLineHscode: String? = null

    @NotNull(message = "Required field")
    @Min(value = 0, message = "Quantity should not be negative, minimum is 0")
    @JsonAlias("SHIPMENT_LINE_QUANTITY")
    var shipmentLineQuantity: Double = 0.0

    @NotEmpty(message = "Required field")
    @Size(max = 20, message = "Shipment line unit can have upto 20 characters")
    @JsonAlias("Shipment Line Unit of Measure")
    var shipmentLineUnitofMeasure: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 250, message = "Description should be upto 250 characters")
    @JsonAlias("SHIPMENT_LINE_DESCRIPTION")
    var shipmentLineDescription: String? = null

    @Size(max = 100, message = "VIN should be upto 100 characters")
    @JsonAlias("SHIPMENT_LINE_VIN")
    var shipmentLineVin: String? = null

    @Size(max = 150, message = "Should be upto 150 characters")
    @JsonAlias("SHIPMENT_LINE_STICKER_NUMBER")
    var shipmentLineStickerNumber: String? = null

    @Size(max = 200, message = "ICS should be upto 200 characters")
    @JsonAlias("SHIPMENT_LINE_ICS")
    var shipmentLineIcs: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 200, message = "Standard reference should be upto 200 characters")
    @JsonAlias("SHIPMENT_LINE_STANDARDS_REFERENCE")
    var shipmentLineStandardsReference: String? = null

    @Size(max = 200, message = "Licence reference should be upto 200 characters")
    @JsonAlias("SHIPMENT_LINE_LICENCE_REFERENCE")
    var shipmentLineLicenceReference: String? = null

    @Size(max = 100, message = "Line registration should be upto 200 characters")
    @JsonAlias("SHIPMENT_LINE_REGISTRATION")
    var shipmentLineRegistration: String? = null

    @Size(max = 100, message = "Brand name should be upto 200 characters")
    @JsonAlias("SHIPMENT_BRAND_NAME")
    var shipmentBrandName: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 100, message = "Category should be upto 200 characters")
    @JsonAlias("PRODUCT_CATEGORY")
    var productCategory: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 200, message = "Description should be upto 200 characters")
    @JsonAlias("SHIPMENT_QUANTITY_DELIVERED")
    var shipmentQuantityDelivered: Double? = null

    @Min(value = 0, message = "Weight should not be negative, minimum is 0")
    @JsonAlias("SHIPMENT_GROSS_WEIGHT")
    var shipmentGrossWeight: Double? = null

    @JsonAlias("SHIPMENT_CONTAINER_NUMBER")
    @Size(max = 200, message = "Description should be upto 200 characters")
    var shipmentContainerNumber: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Description should be upto 150 characters")
    @JsonAlias("SHIPMENT_SEAL_NUMBERS")
    var shipmentSealNumbers: String? = null

    @NotNull(message = "Required field")
    @Min(value = 0, message = "Should not be negative, minimum is 0")
    @JsonAlias("SHIPMENT_PARTIAL_NUMBER")
    var shipmentPartialNumber: Long = 0
}

@JsonIgnoreProperties(ignoreUnknown = true)
class CocEntityForm {

    @NotEmpty(message = "Required field")
    @Pattern(regexp = "^[A-Za-z0-9_/-]+$", message = "Only Alphanumeric values expected for this field")
    @Size(max = 100, message = "Line registration should be upto 200 characters")
    @JsonAlias("COC_NUMBER")
    var cocNumber: String? = null

    @Pattern(regexp = "^[A-Za-z0-9_/-]+$", message = "Only Alphanumeric values expected for this field")
    @Size(max = 100, message = "ID number should be upto 200 characters")
    @JsonAlias("IDF NUMBER")
    var idfNumber: String? = null

    @NotEmpty(message = "Required field")
    @Pattern(regexp = "^[A-Za-z0-9_/-]+$", message = "Only Alphanumeric values expected for this field")
    @Size(max = 100, message = "RFI number should be upto 200 characters")
    @JsonAlias("RFI_NUMBER")
    var rfiNumber: String? = null

    @NotEmpty(message = "Required field")
    @Pattern(regexp = "^[A-Za-z0-9_/-]+$", message = "Only Alphanumeric values expected for this field")
    @Size(max = 100, message = "UCR number should be upto 200 characters")
    @JsonAlias("UCR_NUMBER")
    var ucrNumber: String? = null

    @JsonAlias("FINAL_DOC_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var finalDocDate: Timestamp? = null

    @JsonAlias("RFC_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var rfcDate: Timestamp? = null


    @JsonAlias("COC_ISSUED_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var cocIssueDate: Timestamp? = null


    @NotEmpty(message = "Required field")
    @Pattern(regexp = "^[yYNn]$", message = "Only Y/N expected for this field")
    @Size(max = 10, message = "should be upto 10 characters")
    @JsonAlias("clean")
    var compliant: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 400, message = "hould be upto 400 characters")
    @JsonAlias("COC_REMARKS")
    var cocRemarks: String? = null


    @NotEmpty(message = "Required field")
    @JsonAlias("ISSUING_OFFICE")
    var issuingOffice: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 200, message = "Should be upto 150 characters")
    @JsonAlias("IMPORTER_NAME")
    var importerName: String? = null

    @Pattern(regexp = "^[A-Za-z0-9_/-]*$", message = "Only Alphanumeric values expected for this field")
    @Size(max = 100, message = "Importer pin should be upto 50 characters")
    @JsonAlias("IMPORTER_PIN")
    var importerPin: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Adress 1 should be upto 150 characters")
    @JsonAlias("IMPORTER_ADDRESS_1")
    var importerAddress1: String? = null


    @Size(max = 150, message = "Address 2 should be upto 150 characters")
    @JsonAlias("IMPORTER_ADDRESS_2")
    var importerAddress2: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 100, message = "city should be upto 100 characters")
    @JsonAlias("IMPORTER_CITY")
    var importerCity: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 100, message = "Country should be upto 100 characters")
    @JsonAlias("IMPORTER_COUNTRY")
    var importerCountry: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 75, message = "ZIP should be upto 75 characters")
    @JsonAlias("IMPORTER_ZIP_CODE")
    var importerZipCode: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 20, message = "Phone should be upto 20 characters")
    @Pattern(regexp = "^[0-9+-]+$", message = "Only numeric, + and - values alowed values expected for this field")
    @JsonAlias("IMPORTER_TELEPHONE_NUMBER")
    var importerTelephoneNumber: String? = null


    @Size(max = 75, message = "Fax should be upto 30 characters")
    @JsonAlias("IMPORTER_FAX_NUMBER")
    var importerFaxNumber: String? = null

    @NotEmpty(message = "Required field")
    @Email(message = "importer email is required")
    @Size(max = 100, message = "Email should be upto 100 characters")
    @JsonAlias("IMPORTER_EMAIL")
    var importerEmail: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 200, message = "Name should be upto 200 characters")
    @JsonAlias("EXPORTER_NAME")
    var exporterName: String? = null


    @Pattern(regexp = "^[A-Za-z0-9_/-]+$", message = "Only Alphanumeric values expected for this field")
    @Size(max = 100, message = "PIN should be upto 100 characters")
    @JsonAlias("EXPORTER_PIN")
    var exporterPin: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Address 1 should be upto 150 characters")
    @JsonAlias("EXPORTER_ADDRESS_1")
    var exporterAddress1: String? = null


    @Size(max = 150, message = "Address 2 should be upto 150 characters")
    @JsonAlias("EXPORTER_ADDRESS_2")
    var exporterAddress2: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "should be upto 150 characters")
    @JsonAlias("EXPORTER_CITY")
    var exporterCity: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "should be upto 150 characters")
    @JsonAlias("EXPORTER_COUNTRY")
    var exporterCountry: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 75, message = "should be upto 75 characters")
    @JsonAlias("EXPORTER_ZIP_CODE")
    var exporterZipCode: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 20, message = "Phone should be upto 20 characters")
    @Pattern(regexp = "^[0-9+-]+$", message = "Only numeric, + and - values alowed values expected for this field")
    @JsonAlias("EXPORTER_TELEPHONE_NUMBER")
    var exporterTelephoneNumber: String? = null

    @Size(max = 75, message = "should be upto 75 characters")
    @JsonAlias("EXPORTER_FAX_NUMBER")
    var exporterFaxNumber: String? = null

    @NotEmpty(message = "Required field")
    @Email(message = "Should be a valid email")
    @Size(max = 100, message = "should be upto 100 characters")
    @JsonAlias("EXPORTER_EMAIL")
    var exporterEmail: String? = null

    @Size(max = 150, message = "should be upto 150 characters")
    @JsonAlias("PLACE_OF_INSPECTION")
    var placeOfInspection: String? = null // Optional

    @Size(max = 250, message = "should be upto 250 characters")
    var inspectionZone: String? = null

    @Size(max = 250, message = "should be upto 250 characters")
    var inspectionProvince: String? = null

    @JsonAlias("DATE_OF_INSPECTION")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var dateOfInspection: Timestamp? = null // Optional

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "should be upto 150 characters")
    @JsonAlias("PORT_OF_DESTINATION")
    var portOfDestination: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "should be upto 150 characters")
    @JsonAlias("SHIPMENT_MODE")
    var shipmentMode: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "should be upto 150 characters")
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
    @Size(max = 5, message = "should be upto 5 characters")
    @JsonAlias("FINAL_INVOICE_CURRENCY")
    var finalInvoiceCurrency: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "should be upto 150 characters")
    @JsonAlias("FINAL_INVOICE_NUMBER")
    var finalInvoiceNumber: String? = null

    @JsonAlias("FINAL_INVOICE_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var finalInvoiceDate: Timestamp? = null

    @JsonAlias("PAYMENT_INVOICE_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var invoicePaymentDate: Timestamp? = null

    @NotEmpty(message = "Required field")
    @Size(max = 10, message = "should be upto 10 characters")
    @JsonAlias("ROUTE")
    var route: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("PRODUCT")
    var cocItems: List<CocItem>? = null

    @Size(max = 80, message = "should be upto 80 containers")
    @JsonAlias("SHIPMENT_CONTAINERS")
    var shipmentContainers: List<PvocContainerDetails>? = null

    @NotNull(message = "Required field")
    @Min(value = 1, message = "Version should be greater than or equal to one")
    @JsonAlias("VERSION")
    var version: Long? = null
}

@JsonIgnoreProperties(ignoreUnknown = true)
class NcrEntityForm {

    @JsonAlias("NCR_NUMBER")
    @Size(max = 100, message = "Should be upto 100 characters")
    var ncrNumber: String? = null

    @JsonAlias("IDF_NUMBER")
    @Size(max = 150, message = "Should be upto 150 characters")
    var idfNumber: String? = null

    @JsonAlias("RFI_NUMBER")
    @Size(max = 150, message = "Should be upto 150 characters")
    var rfiNumber: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Should be upto 150 characters")
    @JsonAlias("UCR_NUMBER")
    var ucrNumber: String? = null

    @JsonAlias("ACCEPTABLE_DOC_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var acceptableDocDate: Timestamp? = null

    @JsonAlias("FINAL_DOC_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var finalDocDate: Timestamp? = null

    @JsonAlias("RFC_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var rfcDate: Timestamp? = null


    @JsonAlias("NCR_ISSUED_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var ncrIssueDate: Timestamp? = null


    @NotEmpty(message = "Required field")
    @Size(max = 5, message = "Should be upto 5 characters(Y/N)")
    @JsonAlias("clean")
    var compliant: String? = null

    @JsonAlias("NCR_REMARKS")
    @Size(max = 4000, message = "Should be upto 4000 characters")
    var ncrRemarks: String? = null


    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Should be upto 150 characters")
    @JsonAlias("ISSUING_OFFICE")
    var issuingOffice: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 400, message = "Should be upto 400 characters")
    @JsonAlias("IMPORTER_NAME")
    var importerName: String? = null

    @Size(max = 100, message = "Should be upto 100 characters")
    @JsonAlias("IMPORTER_PIN")
    var importerPin: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Should be upto 150 characters")
    @JsonAlias("IMPORTER_ADDRESS_1")
    var importerAddress1: String? = null

    @Size(max = 150, message = "Should be upto 150 characters")
    @JsonAlias("IMPORTER_ADDRESS_2")
    var importerAddress2: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Should be upto 150 characters")
    @JsonAlias("IMPORTER_CITY")
    var importerCity: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 50, message = "Should be upto 50 characters")
    @JsonAlias("IMPORTER_COUNTRY")
    var importerCountry: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 50, message = "Should be upto 50 characters")
    @JsonAlias("IMPORTER_ZIP_CODE")
    var importerZipCode: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 20, message = "Should be upto 20 characters")
    @JsonAlias("IMPORTER_TELEPHONE_NUMBER")
    var importerTelephoneNumber: String? = null


    @Size(max = 75, message = "Should be upto 75 characters")
    @JsonAlias("IMPORTER_FAX_NUMBER")
    var importerFaxNumber: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("IMPORTER_EMAIL")
    var importerEmail: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 400, message = "Should be upto 400 characters")
    @JsonAlias("EXPORTER_NAME")
    var exporterName: String? = null

    @Pattern(regexp = "^[A-Za-z0-9_/-]*$", message = "Only Alphanumeric values expected for this field")
    @Size(max = 100, message = "Should be upto 100 characters")
    @JsonAlias("EXPORTER_PIN")
    var exporterPin: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 250, message = "Should be upto 400 characters")
    @JsonAlias("EXPORTER_ADDRESS_1")
    var exporterAddress1: String? = null


    @Size(max = 250, message = "Should be upto 400 characters")
    @JsonAlias("EXPORTER_ADDRESS_2")
    var exporterAddress2: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 100, message = "Should be upto 100 characters")
    @JsonAlias("EXPORTER_CITY")
    var exporterCity: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 100, message = "Should be upto 100 characters")
    @JsonAlias("EXPORTER_COUNTRY")
    var exporterCountry: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 75, message = "Should be upto 75 characters")
    @JsonAlias("EXPORTER_ZIP_CODE")
    var exporterZipCode: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 20, message = "Should be upto 20 characters")
    @JsonAlias("EXPORTER_TELEPHONE_NUMBER")
    var exporterTelephoneNumber: String? = null

    @JsonAlias("EXPORTER_FAX_NUMBER")
    @Size(max = 75, message = "Should be upto 75 characters")
    var exporterFaxNumber: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Should be upto 150 characters")
    @JsonAlias("EXPORTER_EMAIL")
    var exporterEmail: String? = null

    @NotEmpty(message = "Required field")
    @JsonAlias("PLACE_OF_INSPECTION")
    var placeOfInspection: String? = null

    @Size(max = 250, message = "should be upto 250 characters")
    var inspectionZone: String? = null

    @Size(max = 250, message = "should be upto 250 characters")
    var inspectionProvince: String? = null

    @NotNull(message = "Required field")
    @JsonAlias("DATE_OF_INSPECTION")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var dateOfInspection: Timestamp? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Should be upto 150 characters")
    @JsonAlias("PORT_OF_DESTINATION")
    var portOfDestination: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 50, message = "Should be upto 50 characters")
    @JsonAlias("SHIPMENT_MODE")
    var shipmentMode: String? = null

    @Size(max = 150, message = "Should be upto 150 characters")
    @JsonAlias("SHIPMENT_SEAL_NUMBER")
    var shipmentSealNumbers: String? = null // optional

    @Size(max = 150, message = "Should be upto 150 characters")
    @JsonAlias("SHIPMENT_CONTAINER_NUMBER")
    var shipmentContainerNumber: String? = null // optional

    @NotNull(message = "Required field")
    @Min(value = 0, message = "Gross weight should not be negative")
    @JsonAlias("SHIPMENT_GROSS_WEIGHT")
    var shipmentGrossWeight: Double? = null

    @NotEmpty(message = "Required field")
    @Size(max = 100, message = "Should be upto 100 characters")
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
    @Size(max = 20, message = "Should be upto 20 characters")
    @JsonAlias("FINAL_INVOICE_CURRENCY")
    var finalInvoiceCurrency: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Should be upto 150 characters")
    @JsonAlias("FINAL_INVOICE_NUMBER")
    var finalInvoiceNumber: String? = null

    @JsonAlias("FINAL_INVOICE_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var finalInvoiceDate: Timestamp? = null

    @JsonAlias("FINAL_INVOICE_PAYMENT_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var paymentDate: Timestamp? = null

    @NotEmpty(message = "Required field")
    @Size(max = 10, message = "Should be upto 10 characters")
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
    @Size(max = 150, message = "Should be upto 150 characters")
    var declaredHsCode: String = ""

    @NotEmpty(message = "Required field")
    @Size(max = 400, message = "Should be upto 400 characters")
    var productDescription: String? = ""

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Should be upto 150 characters")
    var shipmentLineHsCode: String? = ""

    @NotNull(message = "Required field")
    @Min(value = 0, message = "Shipment Line number cannot be negative")
    var shipmentLineNumber: Long? = 0

    @NotNull(message = "Required field")
    @Min(value = 0, message = "Quantity cannot be negative")
    var shipmentLineQuantity: Long? = null

    @NotEmpty(message = "Required field")
    @Size(max = 50, message = "Should be upto 50 characters")
    var shipmentLineUnitofMeasure: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 400, message = "Should be upto 400 characters")
    var shipmentLineDescription: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 250, message = "Should be upto 250 characters")
    var shipmentLineVin: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Should be upto 150 characters")
    var shipmentLineStickerNumber: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Should be upto 150 characters")
    var shipmentLineIcs: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Should be upto 150 characters")
    var shipmentLineStandardsReference: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Should be upto 150 characters")
    var shipmentLineLicenceReference: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Should be upto 150 characters")
    var shipmentLineRegistration: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 250, message = "Should be upto 250 characters")
    var shipmentLineBrandName: String? = null
}

@JsonIgnoreProperties(ignoreUnknown = true)
class CoiEntityForm {

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "should be upto 150 characters")
    var coiNumber: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "should be upto 150 characters")
    var idfNumber: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "should be upto 150 characters")
    var rfiNumber: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "should be upto 150 characters")
    var ucrNumber: String? = null

    @JsonAlias("ACCEPTABLE_DOC_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var acceptableDocDate: Timestamp? = null

    @JsonAlias("FINAL_DOC_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var finalDocDate: Timestamp? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var rfcDate: Timestamp? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var coiIssueDate: Timestamp? = null


    @NotEmpty(message = "Required field")
    @Size(max = 5, message = "should be upto 5 characters")
    @JsonAlias("clean")
    var compliant: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 250, message = "should be upto 250 characters")
    var coiRemarks: String? = null


    @NotEmpty(message = "Required field")
    @Size(max = 100, message = "should be upto 100 characters")
    var issuingOffice: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "should be upto 150 characters")
    var importerName: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "should be upto 150 characters")
    var importerPin: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "should be upto 150 characters")
    var importerAddress1: String? = null

    @Size(max = 150, message = "should be upto 150 characters")
    var importerAddress2: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "should be upto 150 characters")
    var importerCity: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 50, message = "should be upto 50 characters")
    var importerCountry: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 75, message = "should be upto 75 characters")
    var importerZipCode: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 20, message = "should be upto 20 characters")
    var importerTelephoneNumber: String? = null


    @NotEmpty(message = "Required field")
    @Size(max = 75, message = "should be upto 75 characters")
    var importerFaxNumber: String? = null

    @NotEmpty(message = "Required field")
    @Email(message = "Invalid email address")
    @Size(max = 100, message = "should be upto 100 characters")
    var importerEmail: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "should be upto 150 characters")
    var exporterName: String? = null

    @Pattern(regexp = "^[A-Za-z0-9_/-]*$", message = "Only Alphanumeric values expected for this field")
    @Size(max = 100, message = "should be upto 100 characters")
    var exporterPin: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "should be upto 150 characters")
    var exporterAddress1: String? = null

    @Size(max = 250, message = "should be upto 250 characters")
    var inspectionZone: String? = null

    @Size(max = 250, message = "should be upto 250 characters")
    var inspectionProvince: String? = null

    @Size(max = 150, message = "should be upto 150 characters")
    var exporterAddress2: String? = null

    @NotEmpty(message = "Required field")
    var exporterCity: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 50, message = "should be upto 50 characters")
    var exporterCountry: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 75, message = "should be upto 75 characters")
    var exporterZipCode: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 20, message = "should be upto 20 characters")
    var exporterTelephoneNumber: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 75, message = "should be upto 75 characters")
    var exporterFaxNumber: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 100, message = "should be upto 1o0 characters")
    @Email(message = "Invalid email address")
    var exporterEmail: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "should be upto 150 characters")
    var placeOfInspection: String? = null

    @NotNull(message = "Required field")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var dateOfInspection: Timestamp? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "should be upto 150 characters")
    var portOfDestination: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "should be upto 150 characters")
    var shipmentMode: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 50, message = "should be upto 50 characters")
    var countryOfSupply: String? = null

    @NotNull(message = "Required field")
    @Min(value = 0, message = "should be greater than zero")
    var finalInvoiceFobValue: Double = 0.0

    @NotNull(message = "Required field")
    @Min(value = 0, message = "should be greater than zero")
    var finalInvoiceExchangeRate: Double = 0.0

    @NotEmpty(message = "Required field")
    @Size(max = 50, message = "should be upto 50 characters")
    var finalInvoiceCurrency: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "should be upto 150 characters")
    var finalInvoiceNumber: String? = null

    @NotNull(message = "Required field")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var finalInvoiceDate: Timestamp? = null

    @NotNull(message = "Required field")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var invoicePaymentDate: Timestamp? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "should be upto 150 characters")
    var shipmentSealNumbers: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "should be upto 150 characters")
    var shipmentContainerNumber: String? = null

    @NotNull(message = "Required field")
    @Min(value = 0, message = "Minimum value is 0")
    var shipmentGrossWeight: Double? = null

    @NotEmpty(message = "Required field")
    @Size(max = 10, message = "should be upto 10 characters")
    var route: String? = null

    @Valid
    @NotEmpty(message = "Required field")
    @NotNull(message = "Required field")
    var coiItems: List<CoiItem>? = null

    @NotNull(message = "Required field")
    @Min(value = 1, message = "Version should not be less than one")
    var version: Long? = null
}

class CorEntityForm {
    @NotEmpty(message = "COR number is required")
    @Size(max = 150, message = "should be upto 100 characters")
    var corNumber: String? = null

    @Size(max = 150, message = "should be upto 100 characters")
    var ucrNumber: String? = null

    @JsonAlias("ACCEPTABLE_DOC_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var acceptableDocDate: Timestamp? = null

    @JsonAlias("FINAL_DOC_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var finalDocDate: Timestamp? = null

    @NotNull(message = "COR issue date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var corIssueDate: Timestamp? = null

    @NotNull(message = "RFC date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var rfcDate: Timestamp? = null

    @NotEmpty(message = "Country of supply is required")
    @Size(max = 50, message = "should be upto 50 characters")
    var countryOfSupply: String? = null

    @NotEmpty(message = "Inspection center is required")
    @Size(max = 150, message = "should be upto 150 characters")
    var inspectionCenter: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "should be upto 150 characters")
    var exporterName: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "should be upto 150 characters")
    var exporterAddress1: String? = null

    @Size(max = 150, message = "should be upto 150 characters")
    var exporterAddress2: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 100, message = "should be upto 150 characters")
    @Email(message = "Invalid email address")
    var exporterEmail: String? = null

    @Pattern(regexp = "^[A-Za-z0-9_/-]*$", message = "Only Alphanumeric values expected for this field")
    @Size(max = 150, message = "should be upto 150 characters")
    var exporterPin: String? = null

    @NotNull(message = "Inspection date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var inspectionDate: Timestamp? = null

    @NotEmpty(message = "Make is required")
    @Size(max = 100, message = "should be upto 100 characters")
    var make: String? = null

    @NotEmpty(message = "Field is required")
    @Size(max = 100, message = "should be upto 100 characters")
    var model: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 80, message = "should be upto 80 characters")
    var chasisNumber: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 100, message = "should be upto 100 characters")
    var engineNumber: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 100, message = "should be upto 100 characters")
    var engineCapacity: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 10, message = "should be upto 10 characters")
    var yearOfManufacture: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 10, message = "should be upto 10 characters")
    var yearOfFirstRegistration: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 50, message = "should be upto 50 characters")
    var inspectionMileage: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 10, message = "should be upto 10 characters")
    var unitsOfMileage: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 250, message = "should be upto 250 characters")
    var inspectionRemarks: String? = null

    @Size(max = 250, message = "should be upto 250 characters")
    var inspectionZone: String? = null

    @Size(max = 250, message = "should be upto 250 characters")
    var inspectionProvince: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 50, message = "should be upto 50 characters")
    var previousRegistrationNumber: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 50, message = "should be upto 50 characters")
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
    @Size(max = 150, message = "should be upto 50 characters")
    var typeOfVehicle: String? = null

    @NotNull(message = "Required field")
    @Min(value = 0, message = "Number of passengers cannot be negative")
    var numberOfPassengers: Long? = null

    @Size(max = 50, message = "should be upto 50 characters")
    var typeOfBody: String? = null

    @Size(max = 50, message = "should be upto 50 characters")
    var bodyColor: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 50, message = "should be upto 50 characters")
    var fuelType: String? = null

    @NotNull(message = "Required field")
    @Min(value = 0, message = "Inspection fee cannot be negative")
    var inspectionFee: Double? = null

    @Size(max = 100, message = "should be upto 100 characters")
    var inspectionFeeReceipt: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 50, message = "should be upto 50 characters")
    var transmission: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 50, message = "should be upto 50 characters")
    var inspectionOfficer: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 20, message = "should be upto 20 characters")
    var inspectionFeeCurrency: String? = null

    @NotNull(message = "Required field")
    @Min(value = 0, message = "Exchange rate cannot be negative")
    var inspectionFeeExchangeRate: Double? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var inspectionFeePaymentDate: Timestamp? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var finalInvoiceDate: Timestamp? = null

    @Size(max = 10, message = "should be upto 10 characters")
    var route: String? = null

    @NotNull(message = "Required field")
    @Min(value = 1, message = "Varsion cannot be less than one")
    var version: Long? = null
}

class NcrCorEntityForm {
    @NotEmpty(message = "NCR number is required")
    @Size(max = 100, message = "should be upto 100 characters")
    var ncrNumber: String? = null

    @Size(max = 100, message = "should be upto 100 characters")
    var ucrNumber: String? = null

    @JsonAlias("ACCEPTABLE_DOC_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var acceptableDocDate: Timestamp? = null

    @JsonAlias("FINAL_DOC_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var finalDocDate: Timestamp? = null

    @NotNull(message = "NCR issue date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var ncrIssueDate: Timestamp? = null

    @NotNull(message = "RFC date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var rfcDate: Timestamp? = null

    @NotEmpty(message = "Country of supply is required")
    @Size(max = 50, message = "should be upto 50 characters")
    var countryOfSupply: String? = null

    @NotEmpty(message = "Inspection center is required")
    @Size(max = 150, message = "should be upto 150 characters")
    var inspectionCenter: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "should be upto 150 characters")
    var exporterName: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "should be upto 150 characters")
    var exporterAddress1: String? = null

    @Size(max = 150, message = "should be upto 150 characters")
    var exporterAddress2: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 100, message = "should be upto 150 characters")
    @Email(message = "Invalid email address")
    var exporterEmail: String? = null


    @NotNull(message = "Inspection date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var inspectionDate: Timestamp? = null

    @NotEmpty(message = "Make is required")
    @Size(max = 100, message = "should be upto 100 characters")
    var make: String? = null

    @NotEmpty(message = "Field is required")
    @Size(max = 100, message = "should be upto 100 characters")
    var model: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 80, message = "should be upto 80 characters")
    var chasisNumber: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 100, message = "should be upto 100 characters")
    var engineNumber: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 100, message = "should be upto 100 characters")
    var engineCapacity: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 10, message = "should be upto 10 characters")
    var yearOfManufacture: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 10, message = "should be upto 10 characters")
    var yearOfFirstRegistration: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 50, message = "should be upto 50 characters")
    var inspectionMileage: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 10, message = "should be upto 10 characters")
    var unitsOfMileage: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 4000, message = "should be upto 250 characters")
    var inspectionRemarks: String? = null

    @Size(max = 250, message = "should be upto 250 characters")
    var inspectionZone: String? = null

    @Size(max = 250, message = "should be upto 250 characters")
    var inspectionProvince: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 50, message = "should be upto 50 characters")
    var previousRegistrationNumber: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 50, message = "should be upto 50 characters")
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

    @Size(max = 100, message = "should be upto 50 characters")
    var typeOfBody: String? = null

    @Size(max = 50, message = "should be upto 50 characters")
    var bodyColor: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 50, message = "should be upto 50 characters")
    var fuelType: String? = null

    @NotNull(message = "Required field")
    @Min(value = 0, message = "Inspection fee cannot be negative")
    var inspectionFee: Double? = null

    @Size(max = 200, message = "should be upto 100 characters")
    var inspectionFeeReceipt: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 50, message = "should be upto 50 characters")
    var transmission: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 50, message = "should be upto 50 characters")
    var inspectionOfficer: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 20, message = "should be upto 20 characters")
    var inspectionFeeCurrency: String? = null

    @NotNull(message = "Required field")
    @Min(value = 0, message = "Exchange rate cannot be negative")
    var inspectionFeeExchangeRate: Double? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var inspectionFeePaymentDate: Timestamp? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var finalInvoiceDate: Timestamp? = null

    @Size(max = 10, message = "should be upto 10 characters")
    var route: String? = null

    @NotNull(message = "Required field")
    @Min(value = 1, message = "Version cannot be less than one")
    var version: Long? = null
}

class RfcItemForm {
    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Should be upto 150 characters")
    var declaredHsCode: String? = null

    @NotNull(message = "Required field")
    @Min(value = 0, message = "Quantity cannot be negative")
    var itemQuantity: Long? = null

    @NotEmpty(message = "Required field")
    @Size(max = 400, message = "Should be upto 400 characters")
    var productDescription: String? = null

    companion object {
        fun fromEntity(rfc: RfcItemEntity): RfcItemForm {
            return RfcItemForm().apply {
                declaredHsCode = rfc.declaredHsCode
                itemQuantity = rfc.itemQuantity?.toLong()
                productDescription = rfc.productDescription
            }
        }

        fun fromList(rfcs: List<RfcItemEntity>): List<RfcItemForm> {
            val rfcData = mutableListOf<RfcItemForm>()
            rfcs.forEach { rfcData.add(fromEntity(it)) }
            return rfcData
        }
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class RfcEntityForm {

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Should be upto 150 characters")
    var rfcNumber: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Should be upto 150 characters")
    var idfNumber: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Should be upto 150 characters")
    var ucrNumber: String? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var rfcDate: Date? = null

    @Size(max = 50, message = "Should be upto 50 characters")
    var countryOfDestination: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 50, message = "Should be upto 50 characters")
    var applicationType: String? = null

    @Size(max = 150, message = "Should be upto 150 characters")
    var sorReference: String? = null

    @Size(max = 150, message = "Should be upto 150 characters")
    var solReference: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 400, message = "Should be upto 150 characters")
    var importerName: String? = null

    @Pattern(regexp = "^[A-Za-z0-9_/-]*$", message = "Only Alphanumeric values expected for this field")
    @Size(max = 100, message = "Should be upto 100 characters")
    var importerPin: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Should be upto 150 characters")
    var importerAddress1: String? = null


    @Size(max = 150, message = "Should be upto 150 characters")
    var importerAddress2: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Should be upto 150 characters")
    var importerCity: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 50, message = "Should be upto 50 characters")
    var importerCountry: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 75, message = "Should be upto 75 characters")
    var importerZipCode: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 20, message = "Should be upto 20 characters")
    @Pattern(regexp = "^[0-9+-]*$", message = "Invalid phone number provided")
    var importerTelephoneNumber: String? = null


    @NotEmpty(message = "Required field")
    @Size(max = 75, message = "Should be upto 75 characters")
    var importerFaxNumber: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 100, message = "Should be upto 150 characters")
    @Email(message = "Invalid email address")
    var importerEmail: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 400, message = "Should be upto 150 characters")
    var exporterName: String? = null

    @Size(max = 100, message = "Should be upto 100 characters")
    var exporterPin: String? = null // optional for rfc

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Should be upto 150 characters")
    var exporterAddress1: String? = null


    @Size(max = 150, message = "Should be upto 150 characters")
    var exporterAddress2: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Should be upto 150 characters")
    var exporterCity: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 50, message = "Should be upto 50 characters")
    var exporterCountry: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 75, message = "Should be upto 75 characters")
    var exporterZipCode: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 20, message = "Should be upto 20 characters")
    @Pattern(regexp = "^[0-9+-]*$", message = "Invalid phone number provided")
    var exporterTelephoneNumber: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 75, message = "Should be upto 75 characters")
    var exporterFaxNumber: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 100, message = "Should be upto 100 characters")
    @Email(message = "Invalid email address")
    var exporterEmail: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Should be upto 150 characters")
    var placeOfInspection: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Should be upto 150 characters")
    var placeOfInspectionAddress: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 100, message = "Should be upto 100 characters")
    @Email(message = "Invalid email address")
    var placeOfInspectionEmail: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Should be upto 150 characters")
    var placeOfInspectionContacts: String? = null


    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Should be upto 150 characters")
    var portOfLoading: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Should be upto 150 characters")
    var portOfDischarge: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 50, message = "Should be upto 50 characters")
    var shipmentMethod: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 50, message = "Should be upto 50 characters")
    var countryOfSupply: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 10, message = "Should be upto 10 characters")
    var route: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 400, message = "Should be upto 400 characters")
    var goodsCondition: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Should be upto 150 characters")
    var assemblyState: String? = null

    @Size(min = 0, max = 25, message = "Should be upto 25 attachments")
    var linkToAttachedDocuments: List<String>? = null

    @Valid
    @NotEmpty(message = "Required field")
    var items: List<RfcItemForm>? = null

    @NotNull(message = "Required field")
    @Min(value = 0, message = "Version should be set to positive number only")
    @Max(value = 100, message = "Max Value is 100")
    var version: Long? = null
    fun fillDetails(rfcEntity: RfcEntity) {
        rfcEntity.rfcNumber = rfcNumber
        rfcEntity.idfNumber = idfNumber
        rfcEntity.ucrNumber = ucrNumber
        rfcEntity.rfcDate = rfcDate
        rfcEntity.countryOfDestination = countryOfDestination
        rfcEntity.applicationType = applicationType
        rfcEntity.solReference = solReference
        rfcEntity.sorReference = sorReference
        rfcEntity.importerName = importerName
        rfcEntity.importerCountry = importerCountry
        rfcEntity.importerAddress1 = importerAddress1
        rfcEntity.importerAddress2 = importerAddress2
        rfcEntity.importerCity = importerCity
        rfcEntity.importerFaxNumber = importerFaxNumber
        rfcEntity.importerPin = importerPin
        rfcEntity.importerZipcode = importerZipCode
        rfcEntity.importerTelephoneNumber = importerTelephoneNumber
        rfcEntity.importerEmail = importerEmail
        rfcEntity.exporterName = exporterName
        rfcEntity.exporterPin = exporterPin
        rfcEntity.exporterCity = exporterCity
        rfcEntity.exporterAddress1 = exporterAddress1
        rfcEntity.exporterAddress2 = exporterAddress2
        rfcEntity.exporterCountry = exporterCountry
        rfcEntity.exporterEmail = exporterEmail
        rfcEntity.exporterFaxNumber = exporterFaxNumber
        rfcEntity.exporterTelephoneNumber = exporterTelephoneNumber
        rfcEntity.exporterZipcode = exporterZipCode
        rfcEntity.placeOfInspection = placeOfInspection
        rfcEntity.placeOfInspectionAddress = placeOfInspectionAddress
        rfcEntity.placeOfInspectionContacts = placeOfInspectionContacts
        rfcEntity.placeOfInspectionEmail = placeOfInspectionEmail
        rfcEntity.portOfDischarge = portOfDischarge
        rfcEntity.portOfLoading = portOfLoading
        rfcEntity.shipmentMethod = shipmentMethod
        rfcEntity.countryOfSupply = countryOfSupply
        rfcEntity.route = route
        rfcEntity.version = version ?: 1
        rfcEntity.goodsCondition = goodsCondition
        rfcEntity.assemblyState = assemblyState
        rfcEntity.linkToAttachedDocuments = linkToAttachedDocuments?.joinToString(",")
    }

    companion object {
        fun fromEntity(rfcEntity: RfcEntity, rfcItemEntity: List<RfcItemEntity>): RfcEntityForm {
            return RfcEntityForm().apply {
                rfcNumber = rfcEntity.rfcNumber
                idfNumber = rfcEntity.idfNumber
                ucrNumber = rfcEntity.ucrNumber
                rfcDate = rfcEntity.rfcDate
                countryOfDestination = rfcEntity.countryOfDestination
                applicationType = rfcEntity.applicationType
                solReference = rfcEntity.solReference
                sorReference = rfcEntity.sorReference
                importerName = rfcEntity.importerName
                importerCountry = rfcEntity.importerCountry
                importerAddress1 = rfcEntity.importerAddress1
                importerAddress2 = rfcEntity.importerAddress2
                importerCity = rfcEntity.importerCity
                importerFaxNumber = rfcEntity.importerFaxNumber
                importerPin = rfcEntity.importerPin
                importerZipCode = rfcEntity.importerZipcode
                importerTelephoneNumber = rfcEntity.importerTelephoneNumber
                importerEmail = rfcEntity.importerEmail
                exporterName = rfcEntity.exporterName
                exporterPin = rfcEntity.exporterPin
                exporterCity = rfcEntity.exporterCity
                exporterAddress1 = rfcEntity.exporterAddress1
                exporterAddress2 = rfcEntity.exporterAddress2
                exporterCountry = rfcEntity.exporterCountry
                exporterEmail = rfcEntity.exporterEmail
                exporterFaxNumber = rfcEntity.exporterFaxNumber
                exporterTelephoneNumber = rfcEntity.exporterTelephoneNumber
                exporterZipCode = rfcEntity.exporterZipcode
                placeOfInspection = rfcEntity.placeOfInspection
                placeOfInspectionAddress = rfcEntity.placeOfInspectionAddress
                placeOfInspectionContacts = rfcEntity.placeOfInspectionContacts
                placeOfInspectionEmail = rfcEntity.placeOfInspectionEmail
                portOfDischarge = rfcEntity.portOfDischarge
                portOfLoading = rfcEntity.portOfLoading
                shipmentMethod = rfcEntity.shipmentMethod
                countryOfSupply = rfcEntity.countryOfSupply
                route = rfcEntity.route
                version = rfcEntity.version
                goodsCondition = rfcEntity.goodsCondition
                assemblyState = rfcEntity.assemblyState
                linkToAttachedDocuments = rfcEntity.linkToAttachedDocuments?.split(",")
                items = RfcItemForm.fromList(rfcItemEntity)
            }
        }
    }
}


@JsonIgnoreProperties(ignoreUnknown = true)
class RfcCorForm {

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Should be upto 150 characters")
    var rfcNumber: String? = null

    @Size(max = 150, message = "Should be upto 150 characters")
    var idfNumber: String? = null

    @Size(max = 150, message = "Should be upto 150 characters")
    var ucrNumber: String? = null

    @NotNull(message = "Rfc Date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var rfcDate: Date? = null

    @Size(max = 50, message = "Should be upto 50 characters")
    var countryOfDestination: String? = null

    @Size(max = 150, message = "Should be upto 100 characters")
    var importerName: String? = null

    @Size(max = 150, message = "Should be upto 150 characters")
    var importerPin: String? = null

    @Size(max = 150, message = "Should be upto 150 characters")
    var importerAddress1: String? = null

    @Size(max = 150, message = "Should be upto 150 characters")
    var importerAddress2: String? = null

    @Size(max = 150, message = "Should be upto 150 characters")
    var importerCity: String? = null

    @Size(max = 50, message = "Should be upto 50 characters")
    var importerCountry: String? = null

    @Size(max = 75, message = "Should be upto 75 characters")
    var importerZipCode: String? = null

    @Size(max = 20, message = "Should be upto 20 characters")
    @Pattern(regexp = "^[0-9+-]+$", message = "Phone number is invalid, remove non-numeric characters")
    var importerTelephoneNumber: String? = null

    @Size(max = 75, message = "Should be upto 75 characters")
    var importerFaxNumber: String? = null

    @Size(max = 150, message = "Should be upto 150 characters")
    @Email(message = "Should be a valid email address")
    var importerEmail: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Should be upto 150 characters")
    var exporterName: String? = null

    @Pattern(regexp = "^[A-Za-z0-9_/-]*$", message = "Only Alphanumeric values expected for this field")
    @Size(max = 150, message = "Should be upto 150 characters")
    var exporterPin: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Should be upto 150 characters")
    var exporterAddress1: String? = null

    @Size(max = 150, message = "Should be upto 150 characters")
    var exporterAddress2: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Should be upto 150 characters")
    var exporterCity: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 50, message = "Should be upto 50 characters")
    var exporterCountry: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 75, message = "Should be upto 75 characters")
    var exporterZipCode: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 20, message = "Should be upto 20 characters")
    @Pattern(regexp = "^[0-9+-]+$", message = "Phone number should only contain numeric characters")
    var exporterTelephoneNumber: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 75, message = "Should be upto 75 characters")
    var exporterFaxNumber: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Should be upto 150 characters")
    @Email(message = "Invalid email address")
    var exporterEmail: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Should be upto 150 characters")
    var placeOfInspection: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Should be upto 150 characters")
    var applicantName: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Should be upto 150 characters")
    var applicantPin: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Should be upto 150 characters")
    var applicantAddress1: String? = null

    @Size(max = 150, message = "Should be upto 150 characters")
    var applicantAddress2: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Should be upto 150 characters")
    var applicantCity: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Should be upto 150 characters")
    var applicantCountry: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 100, message = "Should be upto 100 characters")
    var applicantZipCode: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 20, message = "Should be upto 20 characters")
    @Pattern(regexp = "^[0-9+-]+$", message = "Phone number should contain numeric characters only")
    var applicantTelephoneNumber: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 75, message = "Should be upto 75 characters")
    var applicantFaxNumber: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 100, message = "Should be upto 100 characters")
    @Email(message = "Invalid email address")
    var applicantEmail: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Should be upto 150 characters")
    var placeOfInspectionAddress: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Should be upto 150 characters")
    @Email(message = "Invalid email address")
    var placeOfInspectionEmail: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Should be upto 150 characters")
    var placeOfInspectionContacts: String? = null


    @NotEmpty(message = "Required field")
    @Size(max = 100, message = "Should be upto 100 characters")
    var portOfLoading: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 100, message = "Should be upto 100 characters")
    var portOfDischarge: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Should be upto 150 characters")
    var shipmentMethod: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 50, message = "Should be upto 100 characters")
    var countryOfSupply: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 1024, message = "Should be upto 1024 characters")
    var goodsCondition: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 100, message = "Should be upto 100 characters")
    var assemblyState: String? = null

    @Size(min = 0, max = 25, message = "Should be upto 25 attachments")
    var linkToAttachedDocuments: List<String>? = null //Optional

    @NotNull(message = "Required field")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var preferredInspectionDate: Timestamp? = null

    @NotEmpty(message = "Required field")
    @Size(max = 100, message = "Should be upto 100 characters")
    var make: String? = null

    @Size(max = 100, message = "Should be upto 100 characters")
    var route: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 100, message = "Should be upto 100 characters")
    var model: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Should be upto 100 characters")
    var chassisNumber: String? = null

    @Size(max = 150, message = "Should be upto 100 characters")
    var engineNumber: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 30, message = "Should be upto 30 characters")
    var engineCapacity: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 10, message = "Should be upto 10 characters")
    var yearOfManufacture: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 10, message = "Should be upto 10 characters")
    var yearOfFirstRegistration: String? = null

    @NotNull(message = "Required field")
    @Min(value = 0, message = "Version should be set to positive number only")
    @Max(value = 100, message = "Version should be upto 100")
    var version: Long? = 1
    fun fillCorRfc(rfc: RfcCorEntity) {
        rfc.rfcDate = rfcDate
        rfc.countryOfDestination = countryOfDestination
        rfc.idfNumber = idfNumber
        rfc.ucrNumber = ucrNumber
        rfc.rfcNumber = rfcNumber
        // Importer
        rfc.importerName = importerName
        rfc.importerPin = importerPin
        rfc.importerAddress1 = importerAddress1
        rfc.importerAddress2 = importerAddress2
        rfc.importerCity = importerCity
        rfc.importerCountry = importerCountry
        rfc.importerZipcode = importerZipCode
        rfc.importerTelephoneNumber = importerTelephoneNumber
        rfc.importerFaxNumber = importerFaxNumber
        rfc.importerEmail = importerEmail
        // Exporter
        rfc.exporterName = exporterName
        rfc.exporterPin = exporterPin
        rfc.exporterAddress1 = exporterAddress1
        rfc.exporterAddress2 = exporterAddress2
        rfc.exporterCity = exporterCity
        rfc.exporterCountry = exporterCountry
        rfc.exporterZipcode = exporterZipCode
        rfc.exporterTelephoneNumber = exporterTelephoneNumber
        rfc.exporterEmail = exporterEmail
        // Applicant details
        rfc.applicantName = applicantName
        rfc.applicantPin = applicantPin
        rfc.applicantAddress1 = applicantAddress1
        rfc.applicantAddress2 = applicantAddress2
        rfc.applicantCity = applicantCity
        rfc.applicantCountry = applicantCountry
        rfc.applicantZipcode = applicantZipCode
        rfc.applicantTelephoneNumber = applicantTelephoneNumber
        rfc.applicantFaxNumber = applicantFaxNumber
        rfc.applicantEmail = applicantEmail
        // Inspection Details
        rfc.placeOfInspection = placeOfInspection
        rfc.placeOfInspectionAddress = placeOfInspectionAddress
        rfc.placeOfInspectionEmail = placeOfInspectionEmail
        rfc.placeOfInspectionContacts = placeOfInspectionContacts
        rfc.portOfLoading = portOfLoading
        rfc.portOfDischarge = portOfDischarge
        rfc.shipmentMethod = shipmentMethod
        rfc.countryOfSupply = countryOfSupply
        rfc.goodsCondition = goodsCondition
        rfc.assemblyState = assemblyState
        rfc.linkToAttachedDocuments = linkToAttachedDocuments?.joinToString(",")
        rfc.preferredInspectionDate = preferredInspectionDate
        // Vehicle
        rfc.make = make
        rfc.model = model
        rfc.chassisNumber = chassisNumber
        rfc.engineCapacity = engineCapacity
        rfc.engineNumber = engineNumber
        rfc.yearOfManufacture = yearOfManufacture
        rfc.yearOfFirstRegistration = yearOfFirstRegistration
        rfc.version = version ?: 1
        rfc.varField1 = version?.toString() ?: "1"

    }

    companion object {
        fun fromEntity(rfc: RfcCorEntity): RfcCorForm {
            return RfcCorForm().apply {
                rfcDate = rfc.rfcDate
                countryOfDestination = rfc.countryOfDestination
                idfNumber = rfc.idfNumber
                ucrNumber = rfc.ucrNumber
                rfcNumber = rfc.rfcNumber
                // Importer
                importerName = rfc.importerName
                importerPin = rfc.importerPin
                importerAddress1 = rfc.importerAddress1
                importerAddress2 = rfc.importerAddress2
                importerCity = rfc.importerCity
                importerCountry = rfc.importerCountry
                importerZipCode = rfc.importerZipcode
                importerTelephoneNumber = rfc.importerTelephoneNumber
                importerFaxNumber = rfc.importerFaxNumber
                importerEmail = rfc.importerEmail
                // Exporter
                exporterName = rfc.exporterName
                exporterPin = rfc.exporterPin
                exporterAddress1 = rfc.exporterAddress1
                exporterAddress2 = rfc.exporterAddress2
                exporterCity = rfc.exporterCity
                exporterCountry = rfc.exporterCountry
                exporterZipCode = rfc.exporterZipcode
                exporterTelephoneNumber = rfc.exporterTelephoneNumber
                exporterEmail = rfc.exporterEmail
                // Applicant details
                applicantName = rfc.applicantName
                applicantPin = rfc.applicantPin
                applicantAddress1 = rfc.applicantAddress1
                applicantAddress2 = rfc.applicantAddress2
                applicantCity = rfc.applicantCity
                applicantCountry = rfc.applicantCountry
                applicantZipCode = rfc.applicantZipcode
                applicantTelephoneNumber = rfc.applicantTelephoneNumber
                applicantFaxNumber = rfc.applicantFaxNumber
                applicantEmail = rfc.applicantEmail
                // Inspection Details
                placeOfInspection = rfc.placeOfInspection
                placeOfInspectionAddress = rfc.placeOfInspectionAddress
                placeOfInspectionEmail = rfc.placeOfInspectionEmail
                placeOfInspectionContacts = rfc.placeOfInspectionContacts
                portOfLoading = rfc.portOfLoading
                portOfDischarge = rfc.portOfDischarge
                shipmentMethod = rfc.shipmentMethod
                countryOfSupply = rfc.countryOfSupply
                goodsCondition = rfc.goodsCondition
                assemblyState = rfc.assemblyState
                linkToAttachedDocuments = rfc.linkToAttachedDocuments?.split(",")
                preferredInspectionDate = rfc.preferredInspectionDate
                // Vehicle
                make = rfc.make
                model = rfc.model
                chassisNumber = rfc.chassisNumber
                engineCapacity = rfc.engineCapacity
                engineNumber = rfc.engineNumber
                yearOfManufacture = rfc.yearOfManufacture
                yearOfFirstRegistration = rfc.yearOfFirstRegistration
                version = rfc.version
            }
        }
    }
}

class IdfItem {
    @NotEmpty(message = "Required field")
    @Size(max = 250, message = "should be upto 250 characters")
    var itemDescription: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "should be upto 150 characters")
    var hsCode: String? = null

    @NotNull(message = "Required field")
    @Min(value = 0, message = "Quantity should be greater than or equal to zero")
    var quantity: Long? = null

    @NotNull(message = "Required field")
    var used: Boolean? = null

    @NotEmpty(message = "Required field")
    @Size(max = 50, message = "should be upto 50 characters")
    var unitOfMeasure: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "should be upto 150 characters")
    var applicableStandard: String? = null

    @NotNull(message = "Required field")
    @Min(value = 0, message = "Item Cost should be greater than or equal to zero")
    var itemCost: Double? = null

    companion object {
        fun fromEntity(idfItemsEntity: IDFItemDetailsEntity): IdfItem {
            return IdfItem()
                .apply {
                    itemDescription = idfItemsEntity.tariffGoodsDesc ?: idfItemsEntity.description ?: "NA"
                    hsCode = idfItemsEntity.commodityCode
                    quantity = idfItemsEntity.unitNum?.toLong()
                    used = idfItemsEntity.usedStatus
                    unitOfMeasure = idfItemsEntity.unitCode
                    applicableStandard = idfItemsEntity.categoryTypePro
                    itemCost = idfItemsEntity.itemPrice?.setScale(0, RoundingMode.CEILING)?.toDouble()
                }
        }

        fun fromList(items: List<IDFItemDetailsEntity>): List<IdfItem> {
            val idfIems = mutableListOf<IdfItem>()
            items.forEach { idfIems.add(fromEntity(it)) }
            return idfIems
        }
    }

}

@JsonIgnoreProperties(ignoreUnknown = true)
open class IdfEntityForm {
    @NotEmpty(message = "Required field")
    @Size(max = 100, message = "should be upto 100 characters")
    var idfNumber: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 100, message = "should be upto 100 characters")
    var ucrNumber: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "should be upto 150 characters")
    var importerName: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "should be upto 150 characters")
    var importerAddress: String? = null

    @NotEmpty(message = "Required field")
    @Email(message = "Invalid email address")
    @Size(max = 100, message = "should be upto 100 characters")
    var importerEmail: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 20, message = "should be upto 20 characters")
    var importerTelephoneNumber: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "should be upto 150 characters")
    var importerContactName: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 75, message = "should be upto 75 characters")
    var importerFaxNumber: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "should be upto 150 characters")
    var exporterName: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "should be upto 150 characters")
    var exporterAddress: String? = null

    @NotEmpty(message = "Required field")
    @Email(message = "invalid email address")
    @Size(max = 100, message = "should be upto 100 characters")
    var exporterEmail: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 20, message = "should be upto 20 characters")
    var exporterTelephoneNumber: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "should be upto 150 characters")
    var exporterContactName: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 75, message = "should be upto 75 characters")
    var exporterFaxNumber: String? = null


    @NotEmpty(message = "Required field")
    @Size(max = 15, message = "should be upto 15 characters")
    var countryOfSupply: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "should be upto 150 characters")
    var portOfDischarge: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "should be upto 150 characters")
    var portOfCustomsClearance: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "should be upto 150 characters")
    var modeOfTransport: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 5, message = "should be upto 50 characters")
    var comesa: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "should be upto 150 characters")
    var invoiceNumber: String? = null

    @NotNull(message = "Required field")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var invoiceDate: Timestamp? = null

    @NotEmpty(message = "Required field")
    @Size(max = 5, message = "should be upto 5 characters")
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
    @Size(max = 250, message = "should be upto 250 characters")
    var observations: String? = null

    @NotNull(message = "Required field")
    var usedStatus: Boolean? = null

    @Valid
    @NotEmpty(message = "Required field")
    var items: List<IdfItem>? = null

    companion object {
        fun fromEntity(idf: IDFDetailsEntity, itemList: List<IDFItemDetailsEntity>): IdfEntityForm {
            return IdfEntityForm().apply {
                idfNumber = idf.baseDocRefNo
                ucrNumber = idf.ucrNo
                importerName = idf.consigneeBusinessName
                importerAddress = idf.consigneeBusinessAddress
                importerFaxNumber = idf.sellerFax
                importerTelephoneNumber = idf.importerTelephoneNumber
                importerEmail = idf.importerEmail
                importerContactName = idf.importerContactName
                exporterName = idf.sellerName
                exporterAddress = idf.sellerAddress
                exporterFaxNumber = idf.sellerFax
                exporterTelephoneNumber = idf.sellerTelephoneNumber
                exporterContactName = idf.sellerContactName
                exporterEmail = idf.sellerEmail
                countryOfSupply = idf.countryOfSupply
                portOfDischarge = idf.portOfDischarge
                portOfCustomsClearance = idf.portOfCustomsClearance
                modeOfTransport = idf.modeOfTransport
                countryOfSupply = idf.consignorCountryCode
                comesa = idf.comesa
                invoiceNumber = idf.invoiceNumber
                invoiceDate = idf.invoiceDate
                currency = idf.currencyCode
                exchangeRate = idf.exchangeRate ?: 0.0
                fobValue = idf.fobValue ?: 0.0
                freight = 0.0
                insurance = (idf.totalInsurance ?: BigDecimal.ZERO).toDouble()
                observations = idf.observations ?: "NA"
                otherCharges = (idf.totalOtherCharges ?: BigDecimal.ZERO).toDouble()
                total = (idf.totalAmount ?: BigDecimal.ZERO).toDouble()
                items = IdfItem.fromList(itemList)
            }
        }
    }
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
    lateinit var responseCode: String
    lateinit var responseMessage: String
    var data: Map<String, Any?>? = null
}

class PvocKebsQueryForm {
    @NotEmpty(message = "Document type is required")
    @Size(min = 1, max = 10, message = "Document type should be upto 10 characters")
    @Pattern(
        regexp = "coc|cor|coi|ncr|rfc",
        flags = [Pattern.Flag.CASE_INSENSITIVE],
        message = "Document type should be one of COC,COI,COR,NCR or RFC"
    )
    var documentType: String? = null

    @Size(min = 0, max = 100, message = "Certificate number should be upto 100 characters")
    var certNumber: String? = null

    @NotEmpty(message = "RFC number is required")
    @Size(min = 0, max = 100, message = "RFC Number should be upto 100 characters")
    var rfcNumber: String? = null

    @Size(min = 0, max = 100, message = "Invoice Number should be upto 100 characters")
    var invoiceNumber: String? = null

    @Size(min = 0, max = 100, message = "Invoice Number should be upto 100 characters")
    var idfNumber: String? = null

    @NotEmpty(message = "UCR number is required")
    @Size(min = 0, max = 100, message = "IDF Number should be upto 100 characters")
    var ucrNumber: String? = null

    @NotEmpty(message = "Query is required")
    @Size(min = 1, max = 4000, message = "Query should be upto 4000 characters")
    var partnerQuery: String? = null
}

class PvocQueryResponse {
    @NotEmpty(message = "Document type is required")
    @Size(min = 0, max = 10, message = "Invoice Number should be upto 10 characters")
    var documentType: String? = null

    @NotEmpty(message = "Serial number is required")
    @Size(min = 0, max = 100, message = "Invoice Number should be upto 100 characters")
    var serialNumber: String? = null

    @NotEmpty(message = "Query response is required")
    @Size(min = 1, max = 4000, message = "Query should be upto 4000 characters")
    var queryResponse: String? = null

    @NotEmpty(message = "Query analysis is required")
    @Size(min = 1, max = 4000, message = "Query should be upto 4000 characters")
    var queryAnalysis: String? = null

    @Size(min = 0, max = 25, message = "Should be upto 25 attachments")
    var linkToUploads: List<String>? = null
}

class PvocQueryConclusion {
    @NotEmpty(message = "Document type is required")
    var responseType: String? = null

    @NotEmpty(message = "Query analysis is required")
    @Size(min = 1, max = 4000, message = "Query should be upto 4000 characters")
    var queryAnalysis: String? = null

    @NotEmpty(message = "Serial number is required")
    var serialNumber: String? = null

    @NotEmpty(message = "Conclusion is required")
    @Size(min = 1, max = 4000, message = "Response should be upto 4000 characters")
    var responseData: String? = null

    var linkToUploads: String? = null
}

class KebsQueryResponseForm {
    @NotEmpty(message = "Query serial number is required")
    var serialNumber: String? = null

    @NotEmpty(message = "Query response is required")
    var queryResponse: String? = null

    @NotEmpty(message = "Query analysis is required")
    var queryAnalysis: String? = null

    @NotEmpty(message = "Query conclusion is required")
    var conclusion: String? = null
    var linkToUploads: String? = null

}

class KebsQueryResponse {
    var documentType: String? = null
    var certNumber: String? = null
    var serialNumber: String? = null
    var responseSerialNumber: String? = null
    var rfcNumber: String? = null
    var invoiceNumber: String? = null
    var ucrNumber: String? = null
    var queryResponse: String? = null
    var queryAnalysis: String? = null
    var conclusion: String? = null
    var linkToUploads: String? = null

}

class KebsPvocQueryForm {
    @NotNull(message = "Document partner is required")
    var partnerId: Long? = null
    var serialNumber: String? = null

    @NotEmpty(message = "document type is required")
    var documentType: String? = null

    @NotEmpty(message = "Certificate number is required")
    var certNumber: String? = null

    //@NotEmpty(message = "RFC Number is required")
    var rfcNumber: String? = null
    var invoiceNumber: String? = null

    @NotEmpty(message = "UCR number is required")
    var ucrNumber: String? = null

    @NotEmpty(message = "Query details cannot be empty")
    var kebsQuery: String? = null
}

class RiskProfileForm {
    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Description should be upto 150 characters")
    var hsCode: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 200, message = "Description should be upto 200 characters")
    var brandName: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 4000, message = "Description should be upto 400 characters")
    var productDescription: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 50, message = "Description should be upto 50 characters")
    var countryOfSupply: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 200, message = "Description should be upto 200 characters")
    var manufacturer: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Description should be upto 150 characters")
    var traderName: String? = null

    @Size(max = 150, message = "Description should be upto 150 characters")
    var importerName: String? = null

    @NotEmpty(message = "Required PIn")
    @Pattern(regexp = "^[A-Za-z0-9_/-]*$", message = "Only Alphanumeric values expected for this field")
    @Size(max = 100, message = "Should be upto 100 characters")
    var importerPin: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 200, message = "Description should be upto 200 characters")
    var exporterName: String? = null

    @Pattern(regexp = "^[A-Za-z0-9_/-]*$", message = "Only Alphanumeric values expected for this field")
    @Size(max = 100, message = "Description should be upto 100 characters")
    var exporterPin: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 20, message = "Description should be upto 20 characters")
    var riskLevel: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 4000, message = "Description should be upto 400 characters")
    var riskDescription: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 4000, message = "Description should be upto 400 characters")
    var remarks: String? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var categorizationDate: java.sql.Date? = null
}

@JsonIgnoreProperties(ignoreUnknown = true)
class PvocComplaintForm {
    @NotEmpty(message = "Please enter your name")
    var lastName: String? = null

    @NotEmpty(message = "Please enter your name")
    var firstName: String? = null

    @NotEmpty(message = "Please enter your phone number")
    var phoneNo: String? = null

    @NotEmpty(message = "Please enter you city")
    var address: String? = null

    var complaintTitle: String? = null

    var cocNumber: String? = null

    var rfcNumber: String? = null


    @NotEmpty(message = "Please enter email address")
    @Email(message = "Please enter valid email address")
    var emailAddress: String? = null

    @NotEmpty(message = "Please enter complaint")
    var complaintDescription: String? = null

    @NotNull(message = "Please select agent")
    var pvocAgent: Long? = null

    @NotNull(message = "Please select nature of complaint")
    var categoryId: Long? = null

    @NotNull(message = "Please select nature type of complaint")
    var subCategoryId: Long? = null

    fun fillDetails(complaint: PvocComplaintEntity) {
        complaint.address = this.address
        complaint.firstName = this.firstName
        complaint.lastName = this.lastName
        complaint.generalDescription = this.complaintDescription
        complaint.complaintTitle = this.complaintTitle
        complaint.phoneNo = this.phoneNo
        complaint.email = this.emailAddress
        complaint.rfcNumber = this.rfcNumber
        complaint.cocNumber = this.cocNumber
    }

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
    val action: Long? = null

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

class PaymentModes {
    var accountNumber: String? = null
    var usdAccountNumber: String? = null
    var accountName: String? = null
    var bankName: String? = null
    var swiftCode: String? = null
    var bankCode: String? = null

    companion object {
        fun fromEntity(pMethod: PaymentMethodsEntity): PaymentModes {
            return PaymentModes().apply {
                accountName = pMethod.bankAccountName
                accountNumber = pMethod.bankAccountName
                bankName = pMethod.bankName
                usdAccountNumber = pMethod.bankAccountUsdNumber
                swiftCode = pMethod.swiftCode
                bankCode = pMethod.bankCode
            }
        }

        fun fromList(toList: List<PaymentMethodsEntity>): List<PaymentModes> {
            val bills = mutableListOf<PaymentModes>()
            toList.forEach { bills.add(fromEntity(it)) }
            return bills
        }
    }
}

class PvocInvoiceData {
    var invoiceNumber: String? = null
    var soldTo: String? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var invoiceDate: Timestamp? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var orderDate: Timestamp? = null
    var orderNumber: String? = null
    var customerNumber: String? = null
    var customerCode: String? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var dueDate: Timestamp? = null
    var paymentStatus: String? = null
    var amount: BigDecimal? = null
    var taxAmount: BigDecimal? = null
    var vatAmount: BigDecimal? = null
    var totalTaxAmount: BigDecimal? = BigDecimal.ZERO
    var penaltyAmount: BigDecimal? = null
    var totalAmount: BigDecimal? = null
    var totalAmountAfterTax: BigDecimal? = null
    var totalBalance: BigDecimal? = null
    var discountAmount: BigDecimal? = null
    var amountInWords: String? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var discountDate: Timestamp? = null
    var currency: String? = null

    companion object {
        fun fromEntity(
            bill: BillPayments,
            clientId: String,
            corporateCode: String,
            amountInWordsService: AmountInWordsService
        ): PvocInvoiceData {
            val dt = PvocInvoiceData()
            dt.apply {
                invoiceNumber = bill.invoiceNumber.orEmpty()
                customerCode = corporateCode
                soldTo = clientId
                invoiceDate = bill.paymentRequestDate
                orderDate = bill.createOn
                orderNumber = bill.billRefNumber
                customerNumber = bill.customerCode
                dueDate = bill.billDate
                amount = bill.billAmount
                penaltyAmount = bill.penaltyAmount
                totalAmount = bill.totalAmount
                totalAmountAfterTax = bill.totalAmount
                totalBalance = bill.totalBalance
                vatAmount = BigDecimal.ZERO
                taxAmount = BigDecimal.ZERO
                discountAmount = BigDecimal.ZERO
                currency = bill.currencyCode
                amountInWords = amountInWordsService.amountToWords(
                    bill.totalAmount
                        ?: BigDecimal.ZERO, bill.currencyCode.orEmpty()
                )
            }
            dt.paymentStatus = when (bill.billStatus) {
                BillStatus.PAID.status -> "PAID"
                BillStatus.PENDING_PAYMENT.status -> "PENDING"
                BillStatus.CLOSED.status -> "CLOSED"
                else -> "OPEN"
            }
            return dt
        }

        fun fromList(
            toList: List<BillPayments>,
            clientId: String,
            corporateCode: String,
            amountInWordsService: AmountInWordsService
        ): List<PvocInvoiceData> {
            val bills = mutableListOf<PvocInvoiceData>()
            toList.forEach { bills.add(fromEntity(it, clientId, corporateCode, amountInWordsService)) }
            return bills
        }
    }
}

class CallbackUrlForm {
    @NotEmpty(message = "Required field")
    @Size(max = 150, message = "Description should be upto 150 characters")
    @ValidateUrl(message = "Invalid URL, please check its a valid http or https link")
    var url: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 200, message = "Description should be upto 200 characters")
    @Pattern(
        regexp = "BASIC|DIGEST",
        message = "Authentication should be either BASIC or DIGEST",
        flags = [Pattern.Flag.CASE_INSENSITIVE]
    )
    var authentication: String? = null

    @NotEmpty(message = "Required field")
    @Size(max = 200, message = "Description should be upto 200 characters")
    var username: String? = null


    @NotEmpty(message = "Required field")
    @Size(max = 4000, message = "Description should be upto 400 characters")
    var password: String? = null

}