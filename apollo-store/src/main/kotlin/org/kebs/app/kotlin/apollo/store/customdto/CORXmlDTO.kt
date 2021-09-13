package org.kebs.app.kotlin.apollo.store.customdto

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.store.model.CorsBakEntity
import java.sql.Timestamp
import java.text.SimpleDateFormat
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

@JacksonXmlRootElement(localName = "APOLLO.DAT_KEBS_CORS_BAKS")
class CORXmlDTO {
    @JacksonXmlProperty(isAttribute = true, localName = "xmlns")
    private val xmlns = "http://DAT_KEBS_CORS_BAK/xsd"

    @JacksonXmlProperty(isAttribute = true, localName = "xmlns:xdb")
    private val xmlnsXdb = "http://xmlns.oracle.com/xdb"

    @JacksonXmlProperty(isAttribute = true, localName = "xsi:schemaLocation")
    private val schemaLocation = "http://DAT_KEBS_CORS_BAK/xsd schema.xsd"

    @JacksonXmlProperty(isAttribute = true, localName = "xmlns:xsi")
    private val xmlnsXsi = "http://www.w3.org/2001/XMLSchema-instance"

    @JacksonXmlProperty(localName = "APOLLO.DAT_KEBS_CORS_BAK")
    var cor: CustomCorXmlDto? = null
}

class CustomCorXmlDto {

    @JacksonXmlProperty(localName = "ID")
    var id: String? = null

    @JacksonXmlProperty(localName = "COR_NUMBER")
    var corNumber: String? = null

    @JacksonXmlProperty(localName = "COR_ISSUE_DATE")
    var corIssueDate: String? = null

    @JacksonXmlProperty(localName = "COUNTRY_OF_SUPPLY")
    var countryOfSupply: String? = null

    @JacksonXmlProperty(localName = "INSPECTION_CENTER")
    var inspectionCenter: String? = null

    @JacksonXmlProperty(localName = "EXPORTER_NAME")
    var exporterName: String? = null

    @JacksonXmlProperty(localName = "EXPORTER_ADDRESS_1")
    var exporterAddress1: String? = null

    @JacksonXmlProperty(localName = "EXPORTER_EMAIL")
    var exporterEmail: String? = null

    @JacksonXmlProperty(localName = "APPLICATION_BOOKING_DATE")
    var applicationBookingDate: String? = null

    @JacksonXmlProperty(localName = "INSPECTION_DATE")
    var inspectionDate: String? = null

    @JacksonXmlProperty(localName = "MAKE")
    var make: String? = null

    @JacksonXmlProperty(localName = "MODEL")
    var model: String? = null

    @JacksonXmlProperty(localName = "CHASIS_NUMBER")
    var chasisNumber: String? = null

    @JacksonXmlProperty(localName = "ENGINE_NUMBER")
    var engineNumber: String? = null

    @JacksonXmlProperty(localName = "ENGINE_CAPACITY")
    var engineCapacity: String? = null

    @JacksonXmlProperty(localName = "YEAR_OF_MANUFACTURE")
    var yearOfManufacture: String? = null

    @JacksonXmlProperty(localName = "YEAR_OF_FIRST_REGISTRATION")
    var yearOfFirstRegistration: String? = null

    @JacksonXmlProperty(localName = "INSPECTION_MILEAGE")
    var inspectionMileage: String? = null

    @JacksonXmlProperty(localName = "UNITS_OF_MILEAGE")
    var unitsOfMileage: String? = null

    @JacksonXmlProperty(localName = "INSPECTION_REMARKS")
    var inspectionRemarks: String? = null

    @JacksonXmlProperty(localName = "PREVIOUS_REGISTRATION_NUMBER")
    var previousRegistrationNumber: String? = null

    @JacksonXmlProperty(localName = "PREVIOUS_COUNTRY_OF_REGISTRATION")
    var previousCountryOfRegistration: String? = null

    @JacksonXmlProperty(localName = "TARE_WEIGHT")
    var tareWeight: String? = null

    @JacksonXmlProperty(localName = "LOAD_CAPACITY")
    var loadCapacity: String? = null

    @JacksonXmlProperty(localName = "GROSS_WEIGHT")
    var grossWeight: String? = null

    @JacksonXmlProperty(localName = "NUMBER_OF_AXLES")
    var numberOfAxles: String? = null

    @JacksonXmlProperty(localName = "TYPE_OF_VEHICLE")
    var typeOfVehicle: String? = null

    @JacksonXmlProperty(localName = "NUMBER_OF_PASSANGERS")
    var numberOfPassangers: String? = null

    @JacksonXmlProperty(localName = "TYPE_OF_BODY")
    var typeOfBody: String? = null

    @JacksonXmlProperty(localName = "BODY_COLOR")
    var bodyColor: String? = null

    @JacksonXmlProperty(localName = "FUEL_TYPE")
    var fuelType: String? = null

    @JacksonXmlProperty(localName = "VERSION")
    var version: String? = null

    fun convertTimestampToKeswsValidDate(timestamp: Timestamp): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val date =sdf.format(timestamp)
        return date
    }
}

fun CorsBakEntity.toCorXmlRecordRefl() = with(CustomCorXmlDto::class.primaryConstructor!!) {
    val propertiesByName = CorsBakEntity::class.memberProperties.associateBy { it.name }
    callBy(args = parameters.associate { parameter ->
        parameter to when (parameter.name) {
            else -> propertiesByName[parameter.name]?.get(this@toCorXmlRecordRefl)
        }
    })
}
