package org.kebs.app.kotlin.apollo.store.customdto

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.store.model.CocItemsEntity
import org.kebs.app.kotlin.apollo.store.model.CocsEntity
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

class CustomCocXmlDto {

    @JacksonXmlProperty(localName = "COC_NUMBER")
    var cocNumber: String? = null

    @JacksonXmlProperty(localName = "IDF_NUMBER")
    var idfNumber: String? = null

    @JacksonXmlProperty(localName = "RFI_NUMBER")
    var rfiNumber: String? = null

    @JacksonXmlProperty(localName = "UCR_NUMBER")
    var ucrNumber: String? = null

    @JacksonXmlProperty(localName = "RFC_DATE")
    var rfcDate: String? = null

    @JacksonXmlProperty(localName = "COC_ISSUE_DATE")
    var cocIssueDate: String? = null

    @JacksonXmlProperty(localName = "IS_CLEAN")
    var isClean: String? = null

    @JacksonXmlProperty(localName = "COC_REMARKS")
    var cocRemarks: String? = null

    @JacksonXmlProperty(localName = "ISSUING_OFFICE")
    var issuingOffice: String? = null

    @JacksonXmlProperty(localName = "IMPORTER_NAME")
    var importerName: String? = null

    @JacksonXmlProperty(localName = "IMPORTER_PIN")
    var importerPin: String? = null

    @JacksonXmlProperty(localName = "IMPORTER_ADDRESS_1")
    var importerAddress1: String? = null

    @JacksonXmlProperty(localName = "IMPORTER_ADDRESS_2")
    var importerAddress2: String? = null

    @JacksonXmlProperty(localName = "IMPORTER_CITY")
    var importerCity: String? = null

    @JacksonXmlProperty(localName = "IMPORTER_COUNTRY")
    var importerCountry: String? = null

    @JacksonXmlProperty(localName = "IMPORTER_ZIPCODE")
    var importerZipCode: String? = null

    @JacksonXmlProperty(localName = "IMPORTER_TELEPHONE_NUMBER")
    var importerTelephoneNumber: String? = null

    @JacksonXmlProperty(localName = "IMPORTER_FAX_NUMBER")
    var importerFaxNumber: String? = null

    @JacksonXmlProperty(localName = "IMPORTER_EMAIL")
    var importerEmail: String? = null

    @JacksonXmlProperty(localName = "EXPORTER_NAME")
    var exporterName: String? = null

    @JacksonXmlProperty(localName = "EXPORTER_PIN")
    var exporterPin: String? = null

    @JacksonXmlProperty(localName = "EXPORTER_ADDRESS_1")
    var exporterAddress1: String? = null

    @JacksonXmlProperty(localName = "EXPORTER_ADDRESS_2")
    var exporterAddress2: String? = null

    @JacksonXmlProperty(localName = "EXPORTER_CITY")
    var exporterCity: String? = null

    @JacksonXmlProperty(localName = "EXPORTER_COUNTRY")
    var exporterCountry: String? = null

    @JacksonXmlProperty(localName = "EXPORTER_ZIPCODE")
    var exporterZipCode: String? = null

    @JacksonXmlProperty(localName = "EXPORTER_TELEPHONE_NUMBER")
    var exporterTelephoneNumber: String? = null

    @JacksonXmlProperty(localName = "EXPORTER_FAX_NUMBER")
    var exporterFaxNumber: String? = null

    @JacksonXmlProperty(localName = "EXPORTER_EMAIL")
    var exporterEmail: String? = null

    @JacksonXmlProperty(localName = "PLACE_OF_INSPECTION")
    var placeOfInspection: String? = null

    @JacksonXmlProperty(localName = "DATE_OF_INSPECTION")
    var dateOfInspection: String? = null

    @JacksonXmlProperty(localName = "PORT_OF_DESTINATION")
    var portOfDestination: String? = null

    @JacksonXmlProperty(localName = "SHIPMENT_MODE")
    var shipmentMode: String? = null

    @JacksonXmlProperty(localName = "COUNTRY_OF_SUPPLY")
    var countryOfSupply: String? = null

    @JacksonXmlProperty(localName = "FINAL_INVOICE_FOB_VALUE")
    var finalInvoiceFobValue: String? = null.toString()

    @JacksonXmlProperty(localName = "FINAL_INVOICE_EXCHANGE_RATE")
    var finalInvoiceExchangeRate: String? = null.toString()

    @JacksonXmlProperty(localName = "FINAL_INVOICE_CURRENCY")
    var finalInvoiceCurrency: String? = null

    @JacksonXmlProperty(localName = "FINAL_INVOICE_DATE")
    var finalInvoiceDate: String? = null

    @JacksonXmlProperty(localName = "SHIPMENT_PARTIAL_NUMBER")
    var shipmentPartialNumber: Long? = null

    @JacksonXmlProperty(localName = "SHIPMENT_SEAL_NUMBERS")
    var shipmentSealNumbers: String? = null

    @JacksonXmlProperty(localName = "SHIPMENT_CONTAINER_NUMBER")
    var shipmentContainerNumber: String? = null

    @JacksonXmlProperty(localName = "SHIPMENT_GROSS_WEIGHT")
    var shipmentGrossWeight: String? = null

    @JacksonXmlProperty(localName = "SHIPMENT_QUANTITY_DELIVERED")
    var shipmentQuantityDelivered: String? = null

    @JacksonXmlProperty(localName = "ROUTE")
    var route: String? = null

    @JacksonXmlProperty(localName = "PRODUCT_CATEGORY")
    var productCategory: String? = null

    @JacksonXmlProperty(localName = "PARTNER")
    var partner: String? = null

    @JacksonXmlProperty(localName = "COC_DETAILS")
    var cocDetals: CocDetails? = null

    fun convertTimestampToKeswsValidDate(timestamp: Timestamp): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val date = sdf.format(timestamp)
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

fun CocsEntity.toCocXmlRecordRefl() = with(CustomCocXmlDto::class.primaryConstructor!!) {
    val propertiesByName = CocsEntity::class.memberProperties.associateBy { it.name }
    callBy(args = parameters.associate { parameter ->
        parameter to when (parameter.name) {
            else -> propertiesByName[parameter.name]?.get(this@toCocXmlRecordRefl)
        }
    })
}

fun CocItemsEntity.toCocItemDetailsXmlRecordRefl(cocNumber: String) = CocDetails(
        this.shipmentLineNumber, this.shipmentLineHscode?:"NA", this.shipmentLineQuantity.toLong(),
        this.shipmentLineUnitofMeasure?:"NA",
        this.shipmentLineDescription?:"NA", this.shipmentLineVin?:"NA",
        this.shipmentLineStickerNumber?:"NA", this.shipmentLineIcs?:"NA",
        this.shipmentLineStandardsReference?:"NA", this.shipmentLineLicenceReference?:"NA",
        this.shipmentLineRegistration?:"NA",cocNumber ,this.shipmentLineBrandName?:"NA"
)
