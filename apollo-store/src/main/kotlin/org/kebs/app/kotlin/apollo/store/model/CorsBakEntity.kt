package org.kebs.app.kotlin.apollo.store.model

import com.fasterxml.jackson.annotation.JsonFormat
import org.kebs.app.kotlin.apollo.store.model.di.ConsignmentDocumentDetailsEntity
import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_CORS_BAK")
class CorsBakEntity : Serializable {

    @Column(name = "ID", nullable = false, precision = 0)
    @Id
    @SequenceGenerator(name = "DAT_KEBS_CORS_BAK_SEQ_GEN", sequenceName = "DAT_KEBS_CORS_BAK_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_CORS_BAK_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long = 0

    @Column(name = "COR_NUMBER")
    @Basic
    var corNumber: String? = null

    @Column(name = "DOCUMENT_TYPE", length = 2)
    @Basic
    var documentsType: String? = "L"

    @Column(name = "COMPLIANT", length = 2)
    @Basic
    var compliant: String? = "Y"

    @Column(name = "ACCEPTABLE_DOC_DATE", nullable = true)
    @Basic
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    var acceptableDocDate: Timestamp? = null

    @Column(name = "FINAL_DOC_DATE", nullable = true)
    @Basic
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    var finalDocDate: Timestamp? = null

    @Column(name = "COR_ISSUE_DATE")
    @Basic
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    var corIssueDate: Timestamp? = null

    @Column(name = "COUNTRY_OF_SUPPLY")
    @Basic
    var countryOfSupply: String? = null

    @Column(name = "INSPECTION_CENTER")
    @Basic
    var inspectionCenter: String? = null

    @Column(name = "INSPECTION_ZONE", length = 400)
    @Basic
    var inspectionZone: String? = null

    @Column(name = "INSPECTION_PROVINCE", length = 400)
    @Basic
    var inspectionProvince: String? = null

    @Column(name = "EXPORTER_NAME")
    @Basic
    var exporterName: String? = null

    @Column(name = "EXPORTER_PIN")
    @Basic
    var exporterPin: String? = null

    @Column(name = "EXPORTER_ADDRESS_1")
    @Basic
    var exporterAddress1: String? = null

    @Column(name = "EXPORTER_ADDRESS_2")
    @Basic
    var exporterAddress2: String? = null

    @Column(name = "EXPORTER_EMAIL")
    @Basic
    var exporterEmail: String? = null

    @Column(name = "APPLICATION_BOOKING_DATE")
    @Basic
    var applicationBookingDate: Timestamp? = null

    @Column(name = "INSPECTION_DATE")
    @Basic
    var inspectionDate: Timestamp? = null

    @Column(name = "MAKE")
    @Basic
    var make: String? = null

    @Column(name = "MODEL")
    @Basic
    var model: String? = null

    @Column(name = "CHASIS_NUMBER")
    @Basic
    var chasisNumber: String? = null

    @Column(name = "ENGINE_NUMBER")
    @Basic
    var engineNumber: String? = null

    @Column(name = "ENGINE_CAPACITY")
    @Basic
    var engineCapacity: String? = null

    @Column(name = "YEAR_OF_MANUFACTURE")
    @Basic
    var yearOfManufacture: String? = null

    @Column(name = "YEAR_OF_FIRST_REGISTRATION")
    @Basic
    var yearOfFirstRegistration: String? = null

    @Column(name = "INSPECTION_MILEAGE")
    @Basic
    var inspectionMileage: String? = null

    @Column(name = "CUSTOMS_IE_NO")
    @Basic
    var customsIeNo: String? = null

    @Column(name = "UNITS_OF_MILEAGE")
    @Basic
    var unitsOfMileage: String? = null

    @Column(name = "INSPECTION_REMARKS", length = 4000)
    @Basic
    var inspectionRemarks: String? = null

    @Column(name = "INSPECTION_STATEMENT", length = 4000)
    @Basic
    var inspectionStatement: String? = null

    @Column(name = "PREVIOUS_REGISTRATION_NUMBER")
    @Basic
    var previousRegistrationNumber: String? = null

    @Column(name = "PREVIOUS_COUNTRY_OF_REGISTRATION")
    @Basic
    var previousCountryOfRegistration: String? = null

    @Column(name = "TARE_WEIGHT", precision = 17, scale = 2)
    @Basic
    var tareWeight: Double = 0.0

    @Column(name = "LOAD_CAPACITY", precision = 17, scale = 2)
    @Basic
    var loadCapacity: Double = 0.0

    @Column(name = "GROSS_WEIGHT", precision = 17, scale = 2)
    @Basic
    var grossWeight: Double = 0.0

    @Column(name = "NUMBER_OF_AXLES")
    @Basic
    var numberOfAxles: Long = 0

    @Column(name = "INSPECTION_OFFICER")
    @Basic
    var inspectionOfficer: String = ""

    @Column(name = "TYPE_OF_VEHICLE")
    @Basic
    var typeOfVehicle: String? = null

    @Column(name = "NUMBER_OF_PASSANGERS")
    @Basic
    var numberOfPassangers: Long = 0

    @Column(name = "TYPE_OF_BODY")
    @Basic
    var typeOfBody: String? = null

    @Column(name = "BODY_COLOR")
    @Basic
    var bodyColor: String? = null

    @Column(name = "FUEL_TYPE")
    @Basic
    var fuelType: String? = null

    @Column(name = "INSPECTION_FEE", precision = 17, scale = 2)
    @Basic
    var inspectionFee: Double? = 0.0

    @Column(name = "INSPECTION_FEE_RECEIPTS")
    @Basic
    var inspectionFeeReceipt: String? = null

    @Column(name = "APPROVAL_STATUS")
    @Basic
    var approvalStatus: String? = null

    @Column(name = "UCR_NUMBER")
    @Basic
    var ucrNumber: String? = null

    @Column(name = "transmission")
    @Basic
    var transmission: String? = null

    @Column(name = "INSPECTION_FEE_CURRENCY")
    @Basic
    var inspectionFeeCurrency: String? = null

    @Column(name = "PVOC_PARTNER")
    @Basic
    var partner: Long? = null

    @Column(name = "INSPECTION_FEE_EXCHANGE_RATE", precision = 10, scale = 2)
    @Basic
    var inspectionFeeExchangeRate: Double = 0.0

    @Column(name = "INSPECTION_FEE_PAYMENT_DATE")
    @Basic
    var inspectionFeePaymentDate: Timestamp? = null

    @Column(name = "ROUTE")
    @Basic
    var route: String? = null

    @Column(name = "REVIEW_STATUS")
    @Basic
    var reviewStatus: Int? = null

    @Column(name = "REVIEW_REMARKS")
    @Basic
    var reviewRemarks: String? = null

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

    @Column(name = "VERSION")
    @Basic
    var version: Long? = null

    @JoinColumn(name = "CONSIGNMENT_DOC_ID", referencedColumnName = "ID")
    @ManyToOne
    var consignmentDocId: ConsignmentDocumentDetailsEntity? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as CorsBakEntity
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
                ucrNumber == that.ucrNumber &&
                approvalStatus == that.approvalStatus &&
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
                deletedOn == that.deletedOn
    }

    override fun hashCode(): Int {
        return Objects.hash(
                id,
                corNumber,
                corIssueDate,
                countryOfSupply,
                inspectionCenter,
                exporterName,
                exporterAddress1,
                exporterAddress2,
                exporterEmail,
                applicationBookingDate,
                inspectionDate,
                make,
                model,
                chasisNumber,
                engineNumber,
                engineCapacity,
                yearOfManufacture,
                yearOfFirstRegistration,
                inspectionMileage,
                unitsOfMileage,
                inspectionRemarks,
                previousRegistrationNumber,
                previousCountryOfRegistration,
                tareWeight,
                loadCapacity,
                grossWeight,
                numberOfAxles,
                typeOfVehicle,
                numberOfPassangers,
                typeOfBody,
                bodyColor,
                fuelType,
                inspectionFee,
                inspectionFeeCurrency,
                inspectionFeeExchangeRate,
                inspectionFeePaymentDate,
                status,
                varField1,
                varField2,
                varField3,
                varField4,
                varField5,
                varField6,
                varField7,
                varField8,
                varField9,
                varField10,
                createdBy,
                createdOn,
                modifiedBy,
                modifiedOn,
                deleteBy,
                deletedOn
        )
    }
}
