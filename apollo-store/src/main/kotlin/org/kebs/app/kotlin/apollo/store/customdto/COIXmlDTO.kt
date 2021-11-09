package org.kebs.app.kotlin.apollo.store.customdto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import org.kebs.app.kotlin.apollo.store.model.CocsEntity
import java.sql.Timestamp
import java.text.SimpleDateFormat
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

@JacksonXmlRootElement(localName = "APOLLO.DAT_KEBS_COISS")
class COIXmlDTO {

    @JacksonXmlProperty(isAttribute = true, localName = "xmlns")
    private val xmlns = "http://DAT_KEBS_COIS/xsd"

    @JacksonXmlProperty(isAttribute = true, localName = "xmlns:xdb")
    private val xmlnsXdb = "http://xmlns.oracle.com/xdb"

    @JacksonXmlProperty(isAttribute = true, localName = "xsi:schemaLocation")
    private val schemaLocation = "http://DAT_KEBS_COIS/xsd schema.xsd"

    @JacksonXmlProperty(isAttribute = true, localName = "xmlns:xsi")
    private val xmlnsXsi = "http://www.w3.org/2001/XMLSchema-instance"

    @JacksonXmlProperty(localName = "APOLLO.DAT_KEBS_COIS")
    @JacksonXmlElementWrapper(useWrapping = false)
    var coi: List<CustomCoiXmlDto>? = null
}

class CustomCoiXmlDto {

    @JacksonXmlProperty(localName = "ID")
    var id: Long? = null

    @JacksonXmlProperty(localName = "COI_NUMBER")
    var coiNumber: String? = null

    @JacksonXmlProperty(localName = "IDF_NUMBER")
    var idfNumber: String? = null

    @JacksonXmlProperty(localName = "RFI_NUMBER")
    var rfiNumber: String? = null

    @JacksonXmlProperty(localName = "UCR_NUMBER")
    var ucrNumber: String? = null

    @JacksonXmlProperty(localName = "RFC_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var rfcDate: String? = null

    @JacksonXmlProperty(localName = "COI_ISSUE_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var coiIssueDate: String? = null

    @JacksonXmlProperty(localName = "CLEAN")
    var clean: String? = null

    @JacksonXmlProperty(localName = "COI_REMARKS")
    var coiRemarks: String? = null

    @JacksonXmlProperty(localName = "ISSUING_OFFICE")
    var issuingOffice: String? = null

    @JacksonXmlProperty(localName = "IMPORTER_NAME")
    var importerName: String? = null

    @JacksonXmlProperty(localName = "IMPORTER_PIN")
    var importerPin: String? = null

    @JacksonXmlProperty(localName = "IMPORTER_ADDRESS_1")
    var importerAddress1: String? = null

    @JacksonXmlProperty(localName = "IMPORTER_CITY")
    var importerCity: String? = null

    @JacksonXmlProperty(localName = "IMPORTER_COUNTRY")
    var importerCountry: String? = null

    @JacksonXmlProperty(localName = "IMPORTER_ZIPCODE")
    var importerZipcode: String? = null

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

    @JacksonXmlProperty(localName = "EXPORTER_CITY")
    var exporterCity: String? = null

    @JacksonXmlProperty(localName = "EXPORTER_COUNTRY")
    var exporterCountry: String? = null

    @JacksonXmlProperty(localName = "EXPORTER_ZIPCODE")
    var exporterZipcode: String? = null

    @JacksonXmlProperty(localName = "EXPORTER_TELEPHONE_NUMBER")
    var exporterTelephoneNumber: String? = null

    @JacksonXmlProperty(localName = "EXPORTER_FAX_NUMBER")
    var exporterFaxNumber: String? = null

    @JacksonXmlProperty(localName = "EXPORTER_EMAIL")
    var exporterEmail: String? = null

    @JacksonXmlProperty(localName = "PLACE_OF_INSPECTION")
    var placeOfInspection: String? = null

    @JacksonXmlProperty(localName = "DATE_OF_INSPECTION")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var dateOfInspection: String? = null

    @JacksonXmlProperty(localName = "PORT_OF_DESTINATION")
    var portOfDestination: String? = null

    @JacksonXmlProperty(localName = "SHIPMENT_MODE")
    var shipmentMode: String? = null

    @JacksonXmlProperty(localName = "COUNTRY_OF_SUPPLY")
    var countryOfSupply: String? = null

    @JacksonXmlProperty(localName = "FINAL_INVOICE_FOB_VALUE")
    var finalInvoiceFobValue: Double? = null

    @JacksonXmlProperty(localName = "FINAL_INVOICE_EXCHANGE_RATE")
    var finalInvoiceExchangeRate: Double? = null

    @JacksonXmlProperty(localName = "FINAL_INVOICE_CURRENCY")
    var finalInvoiceCurrency: String? = null

    @JacksonXmlProperty(localName = "FINAL_INVOICE_NUMBER")
    var finalInvoiceNumber: String? = null

    @JacksonXmlProperty(localName = "FINAL_INVOICE_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var finalInvoiceDate: String? = null


    @JacksonXmlProperty(localName = "SHIPMENT_PARTIAL_NUMBER")
    var shipmentPartialNumber: Long? = null

    @JacksonXmlProperty(localName = "SHIPMENT_SEAL_NUMBERS")
    var shipmentSealNumbers: String? = null

    @JacksonXmlProperty(localName = "SHIPMENT_GROSS_WEIGHT")
    var shipmentGrossWeight: String? = null

    @JacksonXmlProperty(localName = "PRODUCT_CATEGORY")
    var productCategory: String? = null

    @JacksonXmlProperty(localName = "ROUTE")
    var route: String? = "D"


    fun convertTimestampToKeswsValidDate(timestamp: Timestamp): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val date =sdf.format(timestamp)
        return date
    }
}

fun CocsEntity.toCoiXmlRecordRefl() = with(CustomCoiXmlDto::class.primaryConstructor!!) {
    val propertiesByName = CocsEntity::class.memberProperties.associateBy { it.name }
    callBy(args = parameters.associate { parameter ->
        parameter to when (parameter.name) {
            else -> propertiesByName[parameter.name]?.get(this@toCoiXmlRecordRefl)
        }
    })
}
