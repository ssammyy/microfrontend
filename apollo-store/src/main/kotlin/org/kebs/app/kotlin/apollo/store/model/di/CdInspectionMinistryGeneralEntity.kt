package org.kebs.app.kotlin.apollo.store.model.di

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_CD_INSPECTION_MINISTRY_GENERAL")
class CdInspectionMinistryGeneralEntity : Serializable {

    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_CD_INSPECTION_MINISTRY_GENERAL_SEQ_GEN", sequenceName = "DAT_KEBS_CD_INSPECTION_MINISTRY_GENERAL_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_CD_INSPECTION_MINISTRY_GENERAL_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = null
    
    @Column(name = "STATUS")
    @Basic
    var status: Int? = null

    @Column(name = "OWNER_NAME")
    @Basic
    var ownerName: String? = null

    @Column(name = "MAKE_OF_VEHICLE")
    @Basic
    var makeOfVehicle: String? = null

    @Column(name = "ENGINE_CAPACITY")
    @Basic
    var engineCapacity: String? = null

    @Column(name = "MANUFACTURE_DATE")
    @Basic
    var manufactureDate: String? = null

    @Column(name = "ENGINE_NO")
    @Basic
    var engineNo: String? = null

    @Column(name = "TYPE_OF_BODY")
    @Basic
    var typeOfBody: String? = null

    @Column(name = "REG_NO")
    @Basic
    var regNo: String? = null

    @Column(name = "INSURANCE_NO")
    @Basic
    var insuranceNo: String? = null

    @Column(name = "ADDRESS")
    @Basic
    var address: String? = null

    @Column(name = "MODEL")
    @Basic
    var model: String? = null

    @Column(name = "ODOMETER_READING")
    @Basic
    var odometerReading: String? = null

    @Column(name = "FIRST_REGISTRATION_DATE")
    @Basic
    var firstRegistrationDate: String? = null

    @Column(name = "CHASSIS_NO")
    @Basic
    var chassisNo: String? = null

    @Column(name = "CARRYING_CAPACITY")
    @Basic
    var carryingCapacity: String? = null

    @Column(name = "EXPIRY_DATE")
    @Basic
    var expiryDate: String? = null

    @Column(name = "PLACE_OF_INSPECTION")
    @Basic
    var placeOfInspection: String? = null

    @Column(name = "RECEIPT_NO")
    @Basic
    var receiptNo: String? = null

    @Column(name = "INSPECTION_NO")
    @Basic
    var inspectionNo: String? = null

    @Column(name = "INSPECTION_START_TIME")
    @Basic
    var inspectionStartTime: String? = null

    @Column(name = "INSPECTION_END_TIME")
    @Basic
    var inspectionEndTime: String? = null

    @Column(name = "INSPECTION_PERIOD")
    @Basic
    var inspectionPeriod: String? = null

    @Column(name = "INSPECTED_BY")
    @Basic
    var inspectedBy: String? = null

    @Column(name = "CHECKED_BY")
    @Basic
    var checkedBy: String? = null

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

    @Column(name = "SECTION")
    @Basic
    var section: String? = null

    @JoinColumn(name = "CD_ITEM_DETAILS_ID", referencedColumnName = "ID")
    @ManyToOne
    var cdItemDetails: CdItemDetailsEntity? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as CdInspectionMinistryGeneralEntity
        return id == that.id &&
                status == that.status &&
                ownerName == that.ownerName &&
                makeOfVehicle == that.makeOfVehicle &&
                engineCapacity == that.engineCapacity &&
                manufactureDate == that.manufactureDate &&
                engineNo == that.engineNo &&
                typeOfBody == that.typeOfBody &&
                regNo == that.regNo &&
                insuranceNo == that.insuranceNo &&
                address == that.address &&
                model == that.model &&
                odometerReading == that.odometerReading &&
                firstRegistrationDate == that.firstRegistrationDate &&
                chassisNo == that.chassisNo &&
                carryingCapacity == that.carryingCapacity &&
                expiryDate == that.expiryDate &&
                placeOfInspection == that.placeOfInspection &&
                receiptNo == that.receiptNo &&
                inspectionNo == that.inspectionNo &&
                inspectionStartTime == that.inspectionStartTime &&
                inspectionEndTime == that.inspectionEndTime &&
                inspectionPeriod == that.inspectionPeriod &&
                inspectedBy == that.inspectedBy &&
                checkedBy == that.checkedBy &&
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
                section == that.section
    }

    override fun hashCode(): Int {
        return Objects.hash(id, status, ownerName, makeOfVehicle, engineCapacity, manufactureDate, engineNo, typeOfBody, regNo, insuranceNo, address, model, odometerReading, firstRegistrationDate, chassisNo, carryingCapacity, expiryDate, placeOfInspection, receiptNo, inspectionNo, inspectionStartTime, inspectionEndTime, inspectionPeriod, inspectedBy, checkedBy, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn, section)
    }
}