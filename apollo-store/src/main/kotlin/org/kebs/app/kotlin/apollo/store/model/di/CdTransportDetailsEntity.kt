package org.kebs.app.kotlin.apollo.store.model.di

import java.io.Serializable
import java.sql.Time
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_CD_TRANSPORT_DETAILS")
class CdTransportDetailsEntity : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_CD_TRANSPORT_DETAILS_SEQ_GEN", sequenceName = "DAT_KEBS_CD_TRANSPORT_DETAILS_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_CD_TRANSPORT_DETAILS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long = 0

    @Column(name = "MODE_OF_TRANSPORT")
    @Basic
    var modeOfTransport: String? = null

    @Column(name = "VOYAGE_NO")
    @Basic
    var voyageNo: String? = null

    @Column(name = "VESSEL_NAME")
    @Basic
    var vesselName: String? = null

    @Column(name = "DATE_OF_ARRIVAL")
    @Basic
    var dateOfArrival: String? = null

    @Column(name = "BLAWB")
    @Basic
    var blawb: String? = null

    @Column(name = "IN_LAND_TRANS_PORT_CO")
    @Basic
    var inLandTransPortCo: String? = null

    @Column(name = "IN_LAND_TRANS_PORT_CO_REF_NO")
    @Basic
    var inLandTransPortCoRefNo: String? = null

    @Column(name = "KEPHIS_COLLECTION_OFFICE")
    @Basic
    var kephisCollectionOffice: String? = null

    @Column(name = "CERTIFICATION_CATEGORY")
    @Basic
    var certificationCategory: String? = null

    @Column(name = "PLACE_OF_APPLICATION")
    @Basic
    var placeOfApplication: String? = null

    @Column(name = "CUSTOM_OFFICE")
    @Basic
    var customOffice: String? = null

    @Column(name = "CUSTOM_OFFICE_DESC")
    @Basic
    var customOfficeDesc: String? = null

    @Column(name = "MODE_OF_TRANSPORT_DESC")
    @Basic
    var modeOfTransportDesc: String? = null

    @Column(name = "CARGO_TYPE_INDICATOR")
    @Basic
    var cargoTypeIndicator: String? = null

    @Column(name = "CARRIER")
    @Basic
    var carrier: String? = null

    @Column(name = "MARKS_AND_NUMBERS")
    @Basic
    var marksAndNumbers: String? = null

    @Column(name = "MANIFEST_NO")
    @Basic
    var manifestNo: String? = null

    @Column(name = "PORT_OF_DEPARTURE")
    @Basic
    var portOfDeparture: String? = null

    @Column(name = "PORT_OF_DEPARTURE_DESC")
    @Basic
    var portOfDepartureDesc: String? = null

    @Column(name = "SHIPMENT_DATE")
    @Basic
    var shipmentDate: String? = null

    @Column(name = "PORT_OF_ARRIVAL")
    @Basic
    var portOfArrival: String? = null

    @Column(name = "PORT_OF_ARRIVAL_DESC")
    @Basic
    var portOfArrivalDesc: String? = null

    @Column(name = "FREIGHT_STATION")
    @Basic
    var freightStation: String? = null

    @Column(name = "FREIGHT_STATION_DESC")
    @Basic
    var freightStationDesc: String? = null

    @Column(name = "DATE_SUBMITTED")
    @Basic
    var dateSubmitted: Time? = null

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

    @Column(name = "LAST_MODIFIED_BY")
    @Basic
    var lastModifiedBy: String? = null

    @Column(name = "LAST_MODIFIED_ON")
    @Basic
    var lastModifiedOn: Timestamp? = null

    @Column(name = "UPDATE_BY")
    @Basic
    var updateBy: String? = null

    @Column(name = "UPDATED_ON")
    @Basic
    var updatedOn: Timestamp? = null

    @Column(name = "DELETE_BY")
    @Basic
    var deleteBy: String? = null

    @Column(name = "DELETED_ON")
    @Basic
    var deletedOn: Timestamp? = null

    @Column(name = "VERSION", precision = 0)
    @Basic
    var version: Long? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as CdTransportDetailsEntity
        return id == that.id &&
                inLandTransPortCo == that.inLandTransPortCo &&
                inLandTransPortCoRefNo == that.inLandTransPortCoRefNo &&
                kephisCollectionOffice == that.kephisCollectionOffice &&
                certificationCategory == that.certificationCategory &&
                placeOfApplication == that.placeOfApplication &&
                modeOfTransport == that.modeOfTransport &&
                voyageNo == that.voyageNo &&
                vesselName == that.vesselName &&
                dateOfArrival == that.dateOfArrival &&
                blawb == that.blawb &&
                customOffice == that.customOffice &&
                shipmentDate == that.shipmentDate &&
                carrier == that.carrier &&
                marksAndNumbers == that.marksAndNumbers &&
                customOfficeDesc == that.customOfficeDesc &&
                modeOfTransportDesc == that.modeOfTransportDesc &&
                manifestNo == that.manifestNo &&
                portOfDeparture == that.portOfDeparture &&
                portOfDepartureDesc == that.portOfDepartureDesc &&
                cargoTypeIndicator == that.cargoTypeIndicator &&
                portOfArrival == that.portOfArrival &&
                portOfArrivalDesc == that.portOfArrivalDesc &&
                freightStation == that.freightStation &&
                freightStationDesc == that.freightStationDesc &&
                dateSubmitted == that.dateSubmitted &&
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
                lastModifiedBy == that.lastModifiedBy &&
                lastModifiedOn == that.lastModifiedOn &&
                updateBy == that.updateBy &&
                updatedOn == that.updatedOn &&
                deleteBy == that.deleteBy &&
                deletedOn == that.deletedOn &&
                version == that.version
    }

    override fun hashCode(): Int {
        return Objects.hash(id, cargoTypeIndicator, inLandTransPortCo,
                inLandTransPortCoRefNo,
                kephisCollectionOffice,
                certificationCategory,
                placeOfApplication, portOfDepartureDesc, carrier, portOfDeparture, marksAndNumbers, shipmentDate, dateOfArrival, vesselName, customOfficeDesc, portOfArrivalDesc, freightStationDesc, modeOfTransport, voyageNo, blawb, customOffice, modeOfTransportDesc, manifestNo, portOfArrival, freightStation, dateSubmitted, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, lastModifiedBy, lastModifiedOn, updateBy, updatedOn, deleteBy, deletedOn, version)
    }
}