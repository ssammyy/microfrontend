package org.kebs.app.kotlin.apollo.store.model.di

import java.io.Serializable
import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_CD_LOCAL_COR")
class CdLocalCorEntity : Serializable {

    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_CD_LOCAL_COR_SEQ_GEN", sequenceName = "DAT_KEBS_CD_LOCAL_COR_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_CD_LOCAL_COR_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = null

    @Column(name = "COR_SERIAL_NO")
    @Basic
    var corSerialNo: String? = null

    @Column(name = "COR_ISSUE_DATE")
    @Basic
    var corIssueDate: Date? = null

    @Column(name = "COR_EXPIRY_DATE")
    @Basic
    var corExpiryDate: Date? = null

    @Column(name = "IMPORTER_NAME")
    @Basic
    var importerName: String? = null

    @Column(name = "IMPORTER_ADDRESS")
    @Basic
    var importerAddress: String? = null

    @Column(name = "IMPORTER_PHONE")
    @Basic
    var importerPhone: String? = null

    @Column(name = "EXPORTER_NAME")
    @Basic
    var exporterName: String? = null

    @Column(name = "EXPORTER_ADDRESS")
    @Basic
    var exporterAddress: String? = null

    @Column(name = "EXPORTER_EMAIL")
    @Basic
    var exporterEmail: String? = null

    @Column(name = "CHASSIS_NO")
    @Basic
    var chassisNo: String? = null

    @Column(name = "ENGINE_NO_MODEL")
    @Basic
    var engineNoModel: String? = null

    @Column(name = "ENGINE_CAPACITY")
    @Basic
    var engineCapacity: String? = null

    @Column(name = "YEAR_OF_MANUFACTURE")
    @Basic
    var yearOfManufacture: String? = null

    @Column(name = "YEAR_OF_FIRST_REGISTRATION")
    @Basic
    var yearOfFirstRegistration: String? = null

    @Column(name = "CUSTOMS_IE_NO")
    @Basic
    var customsIeNo: String? = null

    @Column(name = "COUNTRY_OF_SUPPLY")
    @Basic
    var countryOfSupply: String? = null

    @Column(name = "BODY_TYPE")
    @Basic
    var bodyType: String? = null

    @Column(name = "FUEL")
    @Basic
    var fuel: String? = null

    @Column(name = "TARE_WEIGHT")
    @Basic
    var tareWeight: String? = null

    @Column(name = "TYPE_OF_VEHICLE")
    @Basic
    var typeOfVehicle: String? = null

    @Column(name = "IDF_NO")
    @Basic
    var idfNo: String? = null

    @Column(name = "UCR_NUMBER")
    @Basic
    var ucrNumber: String? = null

    @Column(name = "MAKE")
    @Basic
    var make: String? = null

    @Column(name = "MODEL")
    @Basic
    var model: String? = null

    @Column(name = "INSPECTION_MILEAGE")
    @Basic
    var inspectionMileage: String? = null

    @Column(name = "UNITS_OF_MILEAGE")
    @Basic
    var unitsOfMileage: String? = null

    @Column(name = "COLOR")
    @Basic
    var color: String? = null

    @Column(name = "AXLE_NO")
    @Basic
    var axleNo: String? = null

    @Column(name = "TRANSMISSION")
    @Basic
    var transmission: String? = null

    @Column(name = "NO_OF_PASSENGERS")
    @Basic
    var noOfPassengers: String? = null

    @Column(name = "PREV_REG_NO")
    @Basic
    var prevRegNo: String? = null

    @Column(name = "PREV_COUNTRY_OF_REG")
    @Basic
    var prevCountryOfReg: String? = null

    @Column(name = "INSPECTION_DETAILS")
    @Basic
    var inspectionDetails: String? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null

    @Column(name = "VAR_FIELD_1")
    @Basic
    var varField1: String? = null

    @Column(name = "VAR_FIELD_2")
    @Basic
    var varField2: String? = null

    @Column(name = "VAR_FIELD_3")
    @Basic
    var varField3: String? = null

    @Column(name = "VAR_FIELD_4")
    @Basic
    var varField4: String? = null

    @Column(name = "VAR_FIELD_5")
    @Basic
    var varField5: String? = null

    @Column(name = "VAR_FIELD_6")
    @Basic
    var varField6: String? = null

    @Column(name = "VAR_FIELD_7")
    @Basic
    var varField7: String? = null

    @Column(name = "VAR_FIELD_8")
    @Basic
    var varField8: String? = null

    @Column(name = "VAR_FIELD_9")
    @Basic
    var varField9: String? = null

    @Column(name = "VAR_FIELD_10")
    @Basic
    var varField10: String? = null

    @Column(name = "CREATED_BY")
    @Basic
    var createdBy: String? = null

    @Column(name = "CREATED_ON")
    @Basic
    var createdOn: Timestamp? = null

    @Column(name = "MODIFIED_BY")
    @Basic
    var modifiedBy: String? = null

    @Column(name = "MODIFIED_ON")
    @Basic
    var modifiedOn: Timestamp? = null

    @Column(name = "DELETE_BY")
    @Basic
    var deleteBy: String? = null

    @Column(name = "DELETED_ON")
    @Basic
    var deletedOn: Timestamp? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as CdLocalCorEntity
        return id == that.id &&
                corSerialNo == that.corSerialNo &&
                corIssueDate == that.corIssueDate &&
                corExpiryDate == that.corExpiryDate &&
                importerName == that.importerName &&
                importerAddress == that.importerAddress &&
                importerPhone == that.importerPhone &&
                exporterName == that.exporterName &&
                exporterAddress == that.exporterAddress &&
                exporterEmail == that.exporterEmail &&
                chassisNo == that.chassisNo &&
                engineNoModel == that.engineNoModel &&
                engineCapacity == that.engineCapacity &&
                yearOfManufacture == that.yearOfManufacture &&
                yearOfFirstRegistration == that.yearOfFirstRegistration &&
                customsIeNo == that.customsIeNo &&
                countryOfSupply == that.countryOfSupply &&
                bodyType == that.bodyType &&
                fuel == that.fuel &&
                tareWeight == that.tareWeight &&
                typeOfVehicle == that.typeOfVehicle &&
                idfNo == that.idfNo &&
                ucrNumber == that.ucrNumber &&
                make == that.make &&
                model == that.model &&
                inspectionMileage == that.inspectionMileage &&
                unitsOfMileage == that.unitsOfMileage &&
                color == that.color &&
                axleNo == that.axleNo &&
                transmission == that.transmission &&
                noOfPassengers == that.noOfPassengers &&
                prevRegNo == that.prevRegNo &&
                prevCountryOfReg == that.prevCountryOfReg &&
                inspectionDetails == that.inspectionDetails &&
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
                deletedOn == that.deletedOn
    }

    override fun hashCode(): Int {
        return Objects.hash(id, corSerialNo, corIssueDate, corExpiryDate, importerName, importerAddress, importerPhone, exporterName, exporterAddress, exporterEmail, chassisNo, engineNoModel, engineCapacity, yearOfManufacture, yearOfFirstRegistration, customsIeNo, countryOfSupply, bodyType, fuel, tareWeight, typeOfVehicle, idfNo, ucrNumber, make, model, inspectionMileage, unitsOfMileage, color, axleNo, transmission, noOfPassengers, prevRegNo, prevCountryOfReg, inspectionDetails, status, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn)
    }
}