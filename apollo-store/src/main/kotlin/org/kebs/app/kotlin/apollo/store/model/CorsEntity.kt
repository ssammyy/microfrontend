package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_CORS")
class CorsEntity : Serializable {
    @get:Column(name = "ID")
    @get:Id
    var id: Long = 0

    @get:Column(name = "COR_NUMBER")
    @get:Basic
    var corNumber: String? = null

    @get:Column(name = "COR_ISSUE_DATE")
    @get:Basic
    var corIssueDate: Timestamp? = null

    @get:Column(name = "COUNTRY_OF_SUPPLY")
    @get:Basic
    var countryOfSupply: String? = null

    @get:Column(name = "INSPECTION_CENTER")
    @get:Basic
    var inspectionCenter: String? = null

    @get:Column(name = "EXPORTER_NAME")
    @get:Basic
    var exporterName: String? = null

    @get:Column(name = "EXPORTER_ADDRESS_1")
    @get:Basic
    var exporterAddress1: String? = null

    @get:Column(name = "EXPORTER_ADDRESS_2")
    @get:Basic
    var exporterAddress2: String? = null

    @get:Column(name = "EXPORTER_EMAIL")
    @get:Basic
    var exporterEmail: String? = null

    @get:Column(name = "APPLICATION_BOOKING_DATE")
    @get:Basic
    var applicationBookingDate: Timestamp? = null

    @get:Column(name = "INSPECTION_DATE")
    @get:Basic
    var inspectionDate: Timestamp? = null

    @get:Column(name = "MAKE")
    @get:Basic
    var make: String? = null

    @get:Column(name = "MODEL")
    @get:Basic
    var model: String? = null

    @get:Column(name = "CHASIS_NUMBER")
    @get:Basic
    var chasisNumber: String? = null

    @get:Column(name = "ENGINE_NUMBER")
    @get:Basic
    var engineNumber: String? = null

    @get:Column(name = "ENGINE_CAPACITY")
    @get:Basic
    var engineCapacity: String? = null

    @get:Column(name = "YEAR_OF_MANUFACTURE")
    @get:Basic
    var yearOfManufacture: String? = null

    @get:Column(name = "YEAR_OF_FIRST_REGISTRATION")
    @get:Basic
    var yearOfFirstRegistration: String? = null

    @get:Column(name = "INSPECTION_MILEAGE")
    @get:Basic
    var inspectionMileage: String? = null

    @get:Column(name = "UNITS_OF_MILEAGE")
    @get:Basic
    var unitsOfMileage: String? = null

    @get:Column(name = "INSPECTION_REMARKS")
    @get:Basic
    var inspectionRemarks: String? = null

    @get:Column(name = "PREVIOUS_REGISTRATION_NUMBER")
    @get:Basic
    var previousRegistrationNumber: String? = null

    @get:Column(name = "PREVIOUS_COUNTRY_OF_REGISTRATION")
    @get:Basic
    var previousCountryOfRegistration: String? = null

    @get:Column(name = "TARE_WEIGHT")
    @get:Basic
    var tareWeight: Long = 0

    @get:Column(name = "LOAD_CAPACITY")
    @get:Basic
    var loadCapacity: Long = 0

    @get:Column(name = "GROSS_WEIGHT")
    @get:Basic
    var grossWeight: Long = 0

    @get:Column(name = "NUMBER_OF_AXLES")
    @get:Basic
    var numberOfAxles: Long = 0

    @get:Column(name = "TYPE_OF_VEHICLE")
    @get:Basic
    var typeOfVehicle: String? = null

    @get:Column(name = "NUMBER_OF_PASSANGERS")
    @get:Basic
    var numberOfPassangers: Long = 0

    @get:Column(name = "TYPE_OF_BODY")
    @get:Basic
    var typeOfBody: String? = null

    @get:Column(name = "BODY_COLOR")
    @get:Basic
    var bodyColor: String? = null

    @get:Column(name = "FUEL_TYPE")
    @get:Basic
    var fuelType: String? = null

    @get:Column(name = "INSPECTION_FEE")
    @get:Basic
    var inspectionFee: Long = 0

    @get:Column(name = "INSPECTION_FEE_CURRENCY")
    @get:Basic
    var inspectionFeeCurrency: String? = null

    @get:Column(name = "INSPECTION_FEE_EXCHANGE_RATE")
    @get:Basic
    var inspectionFeeExchangeRate: Long = 0

    @get:Column(name = "INSPECTION_FEE_PAYMENT_DATE")
    @get:Basic
    var inspectionFeePaymentDate: Timestamp? = null

    @get:Column(name = "STATUS")
    @get:Basic
    var status: Long? = null

    @get:Column(name = "VAR_FIELD_1")
    @get:Basic
    var varField1: String? = null

    @get:Column(name = "VAR_FIELD_2")
    @get:Basic
    var varField2: String? = null

    @get:Column(name = "VAR_FIELD_3")
    @get:Basic
    var varField3: String? = null

    @get:Column(name = "VAR_FIELD_4")
    @get:Basic
    var varField4: String? = null

    @get:Column(name = "VAR_FIELD_5")
    @get:Basic
    var varField5: String? = null

    @get:Column(name = "VAR_FIELD_6")
    @get:Basic
    var varField6: String? = null

    @get:Column(name = "VAR_FIELD_7")
    @get:Basic
    var varField7: String? = null

    @get:Column(name = "VAR_FIELD_8")
    @get:Basic
    var varField8: String? = null

    @get:Column(name = "VAR_FIELD_9")
    @get:Basic
    var varField9: String? = null

    @get:Column(name = "VAR_FIELD_10")
    @get:Basic
    var varField10: String? = null

    @get:Column(name = "CREATED_BY")
    @get:Basic
    var createdBy: String? = null

    @get:Column(name = "CREATED_ON")
    @get:Basic
    var createdOn: Timestamp? = null

    @get:Column(name = "MODIFIED_BY")
    @get:Basic
    var modifiedBy: String? = null

    @get:Column(name = "MODIFIED_ON")
    @get:Basic
    var modifiedOn: Timestamp? = null

    @get:Column(name = "DELETE_BY")
    @get:Basic
    var deleteBy: String? = null

    @get:Column(name = "DELETED_ON")
    @get:Basic
    var deletedOn: Timestamp? = null

    @get:Column(name = "APPROVAL_STATUS")
    @get:Basic
    var approvalStatus: String? = null

    @get:Column(name = "URNNUMBER")
    @get:Basic
    var urnnumber: String? = null

    @get:Column(name = "UCR_NUMBER")
    @get:Basic
    var ucrNumber: String? = null

    @get:Column(name = "CONSIGNMENT_DOC_ID")
    @get:Basic
    var consignmentDocId: Long? = null

    @get:Column(name = "PARTNER")
    @get:Basic
    var partner: String? = null

    @get:Column(name = "SERIAL_NUMBER")
    @get:Basic
    var serialNumber: String? = null

    @get:Column(name = "ODEMETRE_READING")
    @get:Basic
    var odemetreReading: String? = null

    @get:Column(name = "DRIVE")
    @get:Basic
    var drive: String? = null

    @get:Column(name = "TRANSMISSION")
    @get:Basic
    var transmission: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as CorsEntity
        return id == that.id && tareWeight == that.tareWeight && loadCapacity == that.loadCapacity && grossWeight == that.grossWeight && numberOfAxles == that.numberOfAxles && numberOfPassangers == that.numberOfPassangers && inspectionFee == that.inspectionFee && inspectionFeeExchangeRate == that.inspectionFeeExchangeRate &&
                corNumber == that.corNumber &&
                corIssueDate == that.corIssueDate &&
                countryOfSupply == that.countryOfSupply &&
                inspectionCenter == that.inspectionCenter &&
                exporterName == that.exporterName &&
                exporterAddress1 == that.exporterAddress1 &&
                exporterAddress2 == that.exporterAddress2 &&
                exporterEmail == that.exporterEmail &&
                applicationBookingDate == that.applicationBookingDate &&
                inspectionDate == that.inspectionDate &&
                make == that.make &&
                model == that.model &&
                chasisNumber == that.chasisNumber &&
                engineNumber == that.engineNumber &&
                engineCapacity == that.engineCapacity &&
                yearOfManufacture == that.yearOfManufacture &&
                yearOfFirstRegistration == that.yearOfFirstRegistration &&
                inspectionMileage == that.inspectionMileage &&
                unitsOfMileage == that.unitsOfMileage &&
                inspectionRemarks == that.inspectionRemarks &&
                previousRegistrationNumber == that.previousRegistrationNumber &&
                previousCountryOfRegistration == that.previousCountryOfRegistration &&
                typeOfVehicle == that.typeOfVehicle &&
                typeOfBody == that.typeOfBody &&
                bodyColor == that.bodyColor &&
                fuelType == that.fuelType &&
                inspectionFeeCurrency == that.inspectionFeeCurrency &&
                inspectionFeePaymentDate == that.inspectionFeePaymentDate &&
                status == that.status &&
                varField1 == that.varField1 &&
                varField2 == that.varField2 &&
                varField3 == that.varField3 &&
                varField4 == that.varField4 &&
                varField5 == that.varField5 &&
                varField6 == that.varField6 &&
                varField7 == that.varField7 &&
                varField8 == that.varField8 &&
                varField9 == that.varField9 &&
                varField10 == that.varField10 &&
                createdBy == that.createdBy &&
                createdOn == that.createdOn &&
                modifiedBy == that.modifiedBy &&
                modifiedOn == that.modifiedOn &&
                deleteBy == that.deleteBy &&
                deletedOn == that.deletedOn &&
                approvalStatus == that.approvalStatus &&
                urnnumber == that.urnnumber &&
                ucrNumber == that.ucrNumber &&
                consignmentDocId == that.consignmentDocId &&
                partner == that.partner &&
                serialNumber == that.serialNumber &&
                odemetreReading == that.odemetreReading &&
                drive == that.drive &&
                transmission == that.transmission
    }

    override fun hashCode(): Int {
        return Objects.hash(id, corNumber, corIssueDate, countryOfSupply, inspectionCenter, exporterName, exporterAddress1, exporterAddress2, exporterEmail, applicationBookingDate, inspectionDate, make, model, chasisNumber, engineNumber, engineCapacity, yearOfManufacture, yearOfFirstRegistration, inspectionMileage, unitsOfMileage, inspectionRemarks, previousRegistrationNumber, previousCountryOfRegistration, tareWeight, loadCapacity, grossWeight, numberOfAxles, typeOfVehicle, numberOfPassangers, typeOfBody, bodyColor, fuelType, inspectionFee, inspectionFeeCurrency, inspectionFeeExchangeRate, inspectionFeePaymentDate, status, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn, approvalStatus, urnnumber, ucrNumber, consignmentDocId, partner, serialNumber, odemetreReading, drive, transmission)
    }
}