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


class CustomCoiXmlDto(id: Long?,
                      coiNumber: String?,
idfNumber: String? ,
rfiNumber: String? ,
ucrNumber: String? ,
rfcDate: Timestamp? ,
coiIssueDate: Timestamp? ,
clean: String? ,
coiRemarks: String? ,
issuingOffice: String? ,
importerName: String? ,
importerPin: String? ,
importerAddress1: String? ,
importerCity: String? ,
importerCountry: String? ,
importerZipcode: String? ,
importerTelephoneNumber: String?,
importerFaxNumber: String? ,
importerEmail: String? ,
exporterName: String? ,
exporterPin: String? ,
exporterAddress1: String? ,
exporterCity: String? ,
exporterCountry: String? ,
exporterZipcode: String? ,
exporterTelephoneNumber: String? ,
exporterFaxNumber: String? ,
exporterEmail: String? ,
placeOfInspection: String? ,
                      dateOfInspection: Timestamp? ,
portOfDestination: String? ,
shipmentMode: String? ,
                      countryOfSupply: String? ,
 finalInvoiceFobValue: Double?,
finalInvoiceExchangeRate: Double? ,
finalInvoiceCurrency: String?,
finalInvoiceNumber: String? ,
finalInvoiceDate: Timestamp?,
shipmentPartialNumber: Long? ,
shipmentSealNumbers: String?,
shipmentGrossWeight: String?,
productCategory: String?,
route: String? ) {
    @JacksonXmlProperty(localName = "COI_NUMBER")
    var coiNumber: String? = coiNumber

    @JacksonXmlProperty(localName = "IDF_NUMBER")
    var idfNumber: String? = idfNumber

    @JacksonXmlProperty(localName = "RFI_NUMBER")
    var rfiNumber: String? = rfiNumber

    @JacksonXmlProperty(localName = "UCR_NUMBER")
    var ucrNumber: String? = ucrNumber

    @JacksonXmlProperty(localName = "RFC_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var rfcDate: String? = this.convertTimestampToKeswsValidDate(rfcDate)

    @JacksonXmlProperty(localName = "COI_ISSUE_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var coiIssueDate: String? = convertTimestampToKeswsValidDate(coiIssueDate)

    @JacksonXmlProperty(localName = "CLEAN")
    var clean: String? = clean

    @JacksonXmlProperty(localName = "COI_REMARKS")
    var coiRemarks: String? = coiRemarks

    @JacksonXmlProperty(localName = "ISSUING_OFFICE")
    var issuingOffice: String? = issuingOffice

    @JacksonXmlProperty(localName = "IMPORTER_NAME")
    var importerName: String? = importerName

    @JacksonXmlProperty(localName = "IMPORTER_PIN")
    var importerPin: String? = importerPin

    @JacksonXmlProperty(localName = "IMPORTER_ADDRESS_1")
    var importerAddress1: String? = importerAddress1

    @JacksonXmlProperty(localName = "IMPORTER_CITY")
    var importerCity: String? = importerCity

    @JacksonXmlProperty(localName = "IMPORTER_COUNTRY")
    var importerCountry: String? = importerCountry

    @JacksonXmlProperty(localName = "IMPORTER_ZIPCODE")
    var importerZipcode: String? = importerZipcode

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

    @JacksonXmlProperty(localName = "EXPORTER_CITY")
    var exporterCity: String? = exporterCity

    @JacksonXmlProperty(localName = "EXPORTER_COUNTRY")
    var exporterCountry: String? = exporterCountry

    @JacksonXmlProperty(localName = "EXPORTER_ZIPCODE")
    var exporterZipcode: String? = exporterZipcode

    @JacksonXmlProperty(localName = "EXPORTER_TELEPHONE_NUMBER")
    var exporterTelephoneNumber: String? = exporterTelephoneNumber

    @JacksonXmlProperty(localName = "EXPORTER_FAX_NUMBER")
    var exporterFaxNumber: String? = exporterFaxNumber

    @JacksonXmlProperty(localName = "EXPORTER_EMAIL")
    var exporterEmail: String? = exporterEmail

    @JacksonXmlProperty(localName = "PLACE_OF_INSPECTION")
    var placeOfInspection: String? = placeOfInspection

    @JacksonXmlProperty(localName = "DATE_OF_INSPECTION")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var dateOfInspection: String? = convertTimestampToKeswsValidDate(dateOfInspection)

    @JacksonXmlProperty(localName = "PORT_OF_DESTINATION")
    var portOfDestination: String? = portOfDestination

    @JacksonXmlProperty(localName = "SHIPMENT_MODE")
    var shipmentMode: String? = shipmentMode

    @JacksonXmlProperty(localName = "COUNTRY_OF_SUPPLY")
    var countryOfSupply: String? = countryOfSupply

    @JacksonXmlProperty(localName = "FINAL_INVOICE_FOB_VALUE")
    var finalInvoiceFobValue: Double? = finalInvoiceFobValue

    @JacksonXmlProperty(localName = "FINAL_INVOICE_EXCHANGE_RATE")
    var finalInvoiceExchangeRate: Double? = finalInvoiceExchangeRate

    @JacksonXmlProperty(localName = "FINAL_INVOICE_CURRENCY")
    var finalInvoiceCurrency: String? = finalInvoiceCurrency

    @JacksonXmlProperty(localName = "FINAL_INVOICE_NUMBER")
    var finalInvoiceNumber: String? = finalInvoiceNumber

    @JacksonXmlProperty(localName = "FINAL_INVOICE_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var finalInvoiceDate: String? = convertTimestampToKeswsValidDate(finalInvoiceDate)


    @JacksonXmlProperty(localName = "SHIPMENT_PARTIAL_NUMBER")
    var shipmentPartialNumber: Long? = shipmentPartialNumber

    @JacksonXmlProperty(localName = "SHIPMENT_SEAL_NUMBERS")
    var shipmentSealNumbers: String? = shipmentSealNumbers

    @JacksonXmlProperty(localName = "SHIPMENT_GROSS_WEIGHT")
    var shipmentGrossWeight: String? = shipmentGrossWeight

    @JacksonXmlProperty(localName = "PRODUCT_CATEGORY")
    var productCategory: String? = productCategory

    @JacksonXmlProperty(localName = "ROUTE")
    var route: String? = route?:"D"

    @JacksonXmlProperty(localName = "VERSION")
    var version: String? = "1"

    fun convertTimestampToKeswsValidDate(timestamp: Timestamp?): String {
        return timestamp?.let {
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val date = sdf.format(it)
            return date
        }?:""
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
