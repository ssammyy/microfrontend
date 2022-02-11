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

class CustomCorXmlDto(id: Long?, corNumber: String?, corIssueDate: Timestamp?, countryOfSupply: String?, inspectionCenter: String?, exporterName: String?,
                      exporterAddress1: String?, exporterEmail: String?, applicationBookingDate: Timestamp?, inspectionDate: Timestamp?, make: String?, model: String?,
                      chasisNumber: String?, engineNumber: String?, engineCapacity: String?, yearOfManufacture: String?, yearOfFirstRegistration: String?, inspectionMileage: String?,
                      unitsOfMileage: String?, inspectionRemarks: String?, previousRegistrationNumber: String?, previousCountryOfRegistration: String?, tareWeight: Double?,
                      loadCapacity: Double?, grossWeight: Double?, numberOfAxles: Long?, typeOfVehicle: String?, numberOfPassangers: Long?, typeOfBody: String?,
                      bodyColor: String?, fuelType: String?, version: Long?
) {

    @JacksonXmlProperty(localName = "ID")
    var id: String? = id.toString()

    @JacksonXmlProperty(localName = "COR_NUMBER")
    var corNumber: String? = corNumber.orEmpty()

    @JacksonXmlProperty(localName = "COR_ISSUE_DATE")
    var corIssueDate: String? = this.convertTimestampToKeswsValidDate(corIssueDate)

    @JacksonXmlProperty(localName = "COUNTRY_OF_SUPPLY")
    var countryOfSupply: String? = countryOfSupply.orEmpty()

    @JacksonXmlProperty(localName = "INSPECTION_CENTER")
    var inspectionCenter: String? = inspectionCenter.orEmpty()

    @JacksonXmlProperty(localName = "EXPORTER_NAME")
    var exporterName: String? = exporterName.orEmpty()

    @JacksonXmlProperty(localName = "EXPORTER_ADDRESS_1")
    var exporterAddress1: String? = exporterAddress1.orEmpty()

    @JacksonXmlProperty(localName = "EXPORTER_EMAIL")
    var exporterEmail: String? = exporterEmail.orEmpty()

    @JacksonXmlProperty(localName = "APPLICATION_BOOKING_DATE")
    var applicationBookingDate: String? = this.convertTimestampToKeswsValidDate(applicationBookingDate)

    @JacksonXmlProperty(localName = "INSPECTION_DATE")
    var inspectionDate: String? = this.convertTimestampToKeswsValidDate(inspectionDate)

    @JacksonXmlProperty(localName = "MAKE")
    var make: String? = make

    @JacksonXmlProperty(localName = "MODEL")
    var model: String? = model

    @JacksonXmlProperty(localName = "CHASIS_NUMBER")
    var chasisNumber: String? = chasisNumber

    @JacksonXmlProperty(localName = "ENGINE_NUMBER")
    var engineNumber: String? = engineNumber

    @JacksonXmlProperty(localName = "ENGINE_CAPACITY")
    var engineCapacity: String? = engineCapacity

    @JacksonXmlProperty(localName = "YEAR_OF_MANUFACTURE")
    var yearOfManufacture: String? = yearOfManufacture

    @JacksonXmlProperty(localName = "YEAR_OF_FIRST_REGISTRATION")
    var yearOfFirstRegistration: String? = yearOfFirstRegistration

    @JacksonXmlProperty(localName = "INSPECTION_MILEAGE")
    var inspectionMileage: String? = inspectionMileage

    @JacksonXmlProperty(localName = "UNITS_OF_MILEAGE")
    var unitsOfMileage: String? = unitsOfMileage.orEmpty()

    @JacksonXmlProperty(localName = "INSPECTION_REMARKS")
    var inspectionRemarks: String? = inspectionRemarks.orEmpty()

    @JacksonXmlProperty(localName = "PREVIOUS_REGISTRATION_NUMBER")
    var previousRegistrationNumber: String? = previousRegistrationNumber.orEmpty()

    @JacksonXmlProperty(localName = "PREVIOUS_COUNTRY_OF_REGISTRATION")
    var previousCountryOfRegistration: String? = previousCountryOfRegistration.orEmpty()

    @JacksonXmlProperty(localName = "TARE_WEIGHT")
    var tareWeight: String? = tareWeight?.toString()

    @JacksonXmlProperty(localName = "LOAD_CAPACITY")
    var loadCapacity: String? = loadCapacity?.toString()

    @JacksonXmlProperty(localName = "GROSS_WEIGHT")
    var grossWeight: String? = grossWeight?.toString()

    @JacksonXmlProperty(localName = "NUMBER_OF_AXLES")
    var numberOfAxles: String? = numberOfAxles?.toString()

    @JacksonXmlProperty(localName = "TYPE_OF_VEHICLE")
    var typeOfVehicle: String? = typeOfVehicle.orEmpty()

    @JacksonXmlProperty(localName = "NUMBER_OF_PASSANGERS")
    var numberOfPassangers: String? = numberOfPassangers?.toString()

    @JacksonXmlProperty(localName = "TYPE_OF_BODY")
    var typeOfBody: String? = typeOfBody.orEmpty()

    @JacksonXmlProperty(localName = "BODY_COLOR")
    var bodyColor: String? = bodyColor.orEmpty()

    @JacksonXmlProperty(localName = "FUEL_TYPE")
    var fuelType: String? = fuelType

    @JacksonXmlProperty(localName = "VERSION")
    var version: String? = version?.toString() ?: "1"

    fun convertTimestampToKeswsValidDate(timestamp: Timestamp?): String? {
        if(timestamp==null){
            return null
        }
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val date =sdf.format(timestamp)
        return date
    }
}

fun CorsBakEntity.toCorXmlRecordRefl() = with(CustomCorXmlDto::class.primaryConstructor!!) {
    val propertiesByName = CorsBakEntity::class.memberProperties.associateBy { it.name }
    callBy(args = parameters.associate { parameter ->
        KotlinLogging.logger {  }.info("P: ${parameter.name}")
        parameter to when (parameter.name) {
            else -> propertiesByName[parameter.name]?.get(this@toCorXmlRecordRefl)
        }
    })
}
