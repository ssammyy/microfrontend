package org.kebs.app.kotlin.apollo.store.customdto

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.store.model.CocItemsEntity
import org.kebs.app.kotlin.apollo.store.model.CocsBakEntity
import org.kebs.app.kotlin.apollo.store.model.CorsBakEntity
import org.springframework.beans.factory.annotation.Autowired
import java.sql.Timestamp
import java.text.SimpleDateFormat
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

@JacksonXmlRootElement(localName = "APOLLO.DAT_KEBS_COCS_BAKS")
class COCXmlDTO {

    @JacksonXmlProperty(isAttribute = true, localName = "xmlns")
    private val xmlns = "http://DAT_KEBS_COCS_BAK/xsd"

    @JacksonXmlProperty(isAttribute = true, localName = "xmlns:xdb")
    private val xmlnsXdb = "http://xmlns.oracle.com/xdb"

    @JacksonXmlProperty(isAttribute = true, localName = "xsi:schemaLocation")
    private val schemaLocation = "http://DAT_KEBS_COCS_BAK/xsd schema.xsd"

    @JacksonXmlProperty(isAttribute = true, localName = "xmlns:xsi")
    private val xmlnsXsi = "http://www.w3.org/2001/XMLSchema-instance"

    @JacksonXmlProperty(localName = "APOLLO.DAT_KEBS_COCS_BAK")
    var coc: CustomCocXmlDto? = null
}

class CustomCocXmlDto(cocNumber: String, idfNumber: String, rfiNumber: String, ucrNumber: String, rfcDate: Timestamp, cocIssueDate: Timestamp,
                      clean: String, cocRemarks: String, issuingOffice: String, importerName: String, importerPin: String, importerAddress1: String,
                      importerAddress2: String, importerCity: String, importerCountry: String, importerZipCode: String, importerTelephoneNumber: String,
                      importerFaxNumber: String, importerEmail: String, exporterName: String, exporterPin: String, exporterAddress1: String,
                      exporterAddress2: String, exporterCity: String, exporterCountry: String, exporterZipCode: String, exporterTelephoneNumber: String,
                      exporterFaxNumber: String, exporterEmail: String, placeOfInspection: String, dateOfInspection: Timestamp, portOfDestination: String,
                      shipmentMode: String, countryOfSupply: String, finalInvoiceFobValue: Long, finalInvoiceExchangeRate: Long, finalInvoiceCurrency: String,
                      finalInvoiceDate: Timestamp, shipmentPartialNumber: Long, shipmentSealNumbers: String, shipmentContainerNumber: String,
                      shipmentGrossWeight: String, shipmentQuantityDelivered: String, route: String, productCategory: String, partner: String) {

    @JacksonXmlProperty(localName = "COC_NUMBER")
    var cocNumber: String? = cocNumber

    @JacksonXmlProperty(localName = "IDF_NUMBER")
    var idfNumber: String? = idfNumber

    @JacksonXmlProperty(localName = "RFI_NUMBER")
    var rfiNumber: String? = rfiNumber

    @JacksonXmlProperty(localName = "UCR_NUMBER")
    var ucrNumber: String? = ucrNumber

    @JacksonXmlProperty(localName = "RFC_DATE")
    var rfcDate: String? = this.convertTimestampToKeswsValidDate(rfcDate)

    @JacksonXmlProperty(localName = "COC_ISSUE_DATE")
    var cocIssueDate: String? = this.convertTimestampToKeswsValidDate(cocIssueDate)

    @JacksonXmlProperty(localName = "IS_CLEAN")
    var isClean: String? = clean

    @JacksonXmlProperty(localName = "COC_REMARKS")
    var cocRemarks: String? = cocRemarks

    @JacksonXmlProperty(localName = "ISSUING_OFFICE")
    var issuingOffice: String? = issuingOffice

    @JacksonXmlProperty(localName = "IMPORTER_NAME")
    var importerName: String? = importerName

    @JacksonXmlProperty(localName = "IMPORTER_PIN")
    var importerPin: String? = importerPin

    @JacksonXmlProperty(localName = "IMPORTER_ADDRESS_1")
    var importerAddress1: String? = importerAddress1

    @JacksonXmlProperty(localName = "IMPORTER_ADDRESS_2")
    var importerAddress2: String? = importerAddress2

    @JacksonXmlProperty(localName = "IMPORTER_CITY")
    var importerCity: String? = importerCity

    @JacksonXmlProperty(localName = "IMPORTER_COUNTRY")
    var importerCountry: String? = importerCountry

    @JacksonXmlProperty(localName = "IMPORTER_ZIPCODE")
    var importerZipCode: String? = importerZipCode

    @JacksonXmlProperty(localName = "IMPORTER_TELEPHONE_NUMBER")
    var importerTelephoneNumber: String? = importerTelephoneNumber

    @JacksonXmlProperty(localName = "IMPORTER_FAX_NUMBER")
    var importerFaxNumber: String? = importerFaxNumber

    @JacksonXmlProperty(localName = "IMPORTER_EMAIL")
    var importerEmail: String? = importerEmail

    @JacksonXmlProperty(localName = "EXPORTER_NAME")
    var exporterName: String? = exporterName

    @JacksonXmlProperty(localName = "EXPORTER_PIN")
    var exporterPin: String? = exporterPin

    @JacksonXmlProperty(localName = "EXPORTER_ADDRESS_1")
    var exporterAddress1: String? = exporterAddress1

    @JacksonXmlProperty(localName = "EXPORTER_ADDRESS_2")
    var exporterAddress2: String? = exporterAddress2

    @JacksonXmlProperty(localName = "EXPORTER_CITY")
    var exporterCity: String? = exporterCity

    @JacksonXmlProperty(localName = "EXPORTER_COUNTRY")
    var exporterCountry: String? = exporterCountry

    @JacksonXmlProperty(localName = "EXPORTER_ZIPCODE")
    var exporterZipCode: String? = exporterZipCode

    @JacksonXmlProperty(localName = "EXPORTER_TELEPHONE_NUMBER")
    var exporterTelephoneNumber: String? = exporterTelephoneNumber

    @JacksonXmlProperty(localName = "EXPORTER_FAX_NUMBER")
    var exporterFaxNumber: String? = exporterFaxNumber

    @JacksonXmlProperty(localName = "EXPORTER_EMAIL")
    var exporterEmail: String? = exporterEmail

    @JacksonXmlProperty(localName = "PLACE_OF_INSPECTION")
    var placeOfInspection: String? = placeOfInspection

    @JacksonXmlProperty(localName = "DATE_OF_INSPECTION")
    var dateOfInspection: String? = this.convertTimestampToKeswsValidDate(dateOfInspection)

    @JacksonXmlProperty(localName = "PORT_OF_DESTINATION")
    var portOfDestination: String? = portOfDestination

    @JacksonXmlProperty(localName = "SHIPMENT_MODE")
    var shipmentMode: String? = shipmentMode

    @JacksonXmlProperty(localName = "COUNTRY_OF_SUPPLY")
    var countryOfSupply: String? = countryOfSupply

    @JacksonXmlProperty(localName = "FINAL_INVOICE_FOB_VALUE")
    var finalInvoiceFobValue: String? = finalInvoiceFobValue.toString()

    @JacksonXmlProperty(localName = "FINAL_INVOICE_EXCHANGE_RATE")
    var finalInvoiceExchangeRate: String? = finalInvoiceExchangeRate.toString()

    @JacksonXmlProperty(localName = "FINAL_INVOICE_CURRENCY")
    var finalInvoiceCurrency: String? = finalInvoiceCurrency

    @JacksonXmlProperty(localName = "FINAL_INVOICE_DATE")
    var finalInvoiceDate: String? = this.convertTimestampToKeswsValidDate(finalInvoiceDate)

    @JacksonXmlProperty(localName = "SHIPMENT_PARTIAL_NUMBER")
    var shipmentPartialNumber: Long? = shipmentPartialNumber

    @JacksonXmlProperty(localName = "SHIPMENT_SEAL_NUMBERS")
    var shipmentSealNumbers: String? = shipmentSealNumbers

    @JacksonXmlProperty(localName = "SHIPMENT_CONTAINER_NUMBER")
    var shipmentContainerNumber: String? = shipmentContainerNumber

    @JacksonXmlProperty(localName = "SHIPMENT_GROSS_WEIGHT")
    var shipmentGrossWeight: String? = shipmentGrossWeight

    @JacksonXmlProperty(localName = "SHIPMENT_QUANTITY_DELIVERED")
    var shipmentQuantityDelivered: String? = shipmentQuantityDelivered

    @JacksonXmlProperty(localName = "ROUTE")
    var route: String? = route

    @JacksonXmlProperty(localName = "PRODUCT_CATEGORY")
    var productCategory: String? = productCategory

    @JacksonXmlProperty(localName = "PARTNER")
    var partner: String? = partner

    @JacksonXmlProperty(localName = "COC_DETAILS")
    var cocDetals: CocDetails? = null

    fun convertTimestampToKeswsValidDate(timestamp: Timestamp): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val date =sdf.format(timestamp)
        return date
    }
}

class CocDetails(shipmentLineNumber: Long, shipmentLineHscode: String, shipmentLineQuantity: Long, shipmentLineUnitofMeasure: String,
                 shipmentLineDescription: String, shipmentLineVin: String, shipmentLineStickerNumber: String, shipmentLineIcs: String,
                 shipmentLineStandardsReference: String, shipmentLineLicenceReference: String, shipmentLineRegistration: String,
                 cocNumber: String, shipmentLineBrandName: String) {

    @JacksonXmlProperty(localName = "SHIPMENT_LINE_NUMBER")
    var shipmentLineNumber: Long? = shipmentLineNumber

    @JacksonXmlProperty(localName = "SHIPMENT_LINE_HSCODE")
    var shipmentLineHscode: String? = shipmentLineHscode

    @JacksonXmlProperty(localName = "SHIPMENT_LINE_QUANTITY")
    var shipmentLineQuantity: Long? = shipmentLineQuantity

    @JacksonXmlProperty(localName = "SHIPMENT_LINE_UNITOF_MEASURE")
    var shipmentLineUnitofMeasure: String? = shipmentLineUnitofMeasure

    @JacksonXmlProperty(localName = "SHIPMENT_LINE_DESCRIPTION")
    var shipmentLineDescription: String? = shipmentLineDescription

    @JacksonXmlProperty(localName = "SHIPMENT_LINE_VIN")
    var shipmentLineVin: String? = shipmentLineVin

    @JacksonXmlProperty(localName = "SHIPMENT_LINE_STICKER_NUMBER")
    var shipmentLineStickerNumber: String? = shipmentLineStickerNumber

    @JacksonXmlProperty(localName = "SHIPMENT_LINE_ICS")
    var shipmentLineIcs: String? = shipmentLineIcs

    @JacksonXmlProperty(localName = "SHIPMENT_LINE_STANDARDS_REFERENCE")
    var shipmentLineStandardsReference: String? = shipmentLineStandardsReference

    @JacksonXmlProperty(localName = "SHIPMENT_LINE_LICENCE_REFERENCE")
    var shipmentLineLicenceReference: String? = shipmentLineLicenceReference

    @JacksonXmlProperty(localName = "SHIPMENT_LINE_REGISTRATION")
    var shipmentLineRegistration: String? = shipmentLineRegistration

    @JacksonXmlProperty(localName = "COC_NUMBER")
    var cocNumber: String? = cocNumber

    @JacksonXmlProperty(localName = "SHIPMENT_LINE_BRANDNAME")
    var shipmentLineBrandName: String? = shipmentLineBrandName
}

fun CocsBakEntity.toCocXmlRecordRefl() = with(CustomCocXmlDto::class.primaryConstructor!!) {
    val propertiesByName = CocsBakEntity::class.memberProperties.associateBy { it.name }
    callBy(args = parameters.associate { parameter ->
        parameter to when (parameter.name) {
            else -> propertiesByName[parameter.name]?.get(this@toCocXmlRecordRefl)
        }
    })
}

fun CocItemsEntity.toCocItemDetailsXmlRecordRefl() = with(CocDetails::class.primaryConstructor!!) {
    val propertiesByName = CocItemsEntity::class.memberProperties.associateBy { it.name }
    callBy(args = parameters.associate { parameter ->
        parameter to when (parameter.name) {
            else -> propertiesByName[parameter.name]?.get(this@toCocItemDetailsXmlRecordRefl)
        }
    })
}
