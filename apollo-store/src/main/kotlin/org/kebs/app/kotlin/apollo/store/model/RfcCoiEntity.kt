package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Date
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_RFC_COI")
class RfcCoiEntity : Serializable {
    @Column(name = "ID", nullable = false, precision = 0)
    @Id
    @SequenceGenerator(name = "DAT_KEBS_RFC_COI_SEQ_GEN", sequenceName = "DAT_KEBS_RFC_COI_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_RFC_COI_SEQ_GEN", strategy = GenerationType.SEQUENCE)

    var id: Long = 0

    @Column(name = "RFC_NUMBER", nullable = false, length = 50)
    @Basic
    var rfcNumber: String? = null

    @Column(name = "IDF_NUMBER", nullable = true, length = 50)
    @Basic
    var idfNumber: String? = null

    @Column(name = "UCR_NUMBER", nullable = false, length = 50)
    @Basic
    var ucrNumber: String? = null

    @Column(name = "RFC_DATE", nullable = true)
    @Basic
    var rfcDate: Date? = null

    @Column(name = "COUNTRY_OF_DESTINATION", nullable = false, length = 100)
    @Basic
    var countryOfDestination: String? = null

    @Column(name = "APPLICATION_TYPE", nullable = false, length = 50)
    @Basic
    var applicationType: String? = null

    @Column(name = "SOR_REFERENCE", nullable = true, length = 100)
    @Basic
    var sorReference: String? = null

    @Column(name = "SOL_REFERENCE", nullable = true, length = 100)
    @Basic
    var solReference: String? = null

    @Column(name = "IMPORTER_NAME", nullable = false, length = 1000)
    @Basic
    var importerName: String? = null

    @Column(name = "IMPORTER_PIN", nullable = false, length = 150)
    @Basic
    var importerPin: String? = null

    @Column(name = "IMPORTER_ADDRESS_1", nullable = false, length = 1000)
    @Basic
    var importerAddress1: String? = null

    @Column(name = "IMPORTER_ADDRESS_2", nullable = true, length = 1000)
    @Basic
    var importerAddress2: String? = null

    @Column(name = "IMPORTER_CITY", nullable = false, length = 100)
    @Basic
    var importerCity: String? = null

    @Column(name = "IMPORTER_COUNTRY", nullable = false, length = 100)
    @Basic
    var importerCountry: String? = null

    @Column(name = "IMPORTER_ZIPCODE", nullable = false, length = 100)
    @Basic
    var importerZipcode: String? = null

    @Column(name = "IMPORTER_TELEPHONE_NUMBER", nullable = false, length = 100)
    @Basic
    var importerTelephoneNumber: String? = null

    @Column(name = "IMPORTER_FAX_NUMBER", nullable = true, length = 100)
    @Basic
    var importerFaxNumber: String? = null

    @Column(name = "IMPORTER_EMAIL", nullable = false, length = 150)
    @Basic
    var importerEmail: String? = null

    @Column(name = "EXPORTER_NAME", nullable = false, length = 1000)
    @Basic
    var exporterName: String? = null

    @Column(name = "EXPORTER_PIN", nullable = false, length = 150)
    @Basic
    var exporterPin: String? = null

    @Column(name = "EXPORTER_ADDRESS_1", nullable = false, length = 1000)
    @Basic
    var exporterAddress1: String? = null

    @Column(name = "EXPORTER_ADDRESS_2", nullable = true, length = 1000)
    @Basic
    var exporterAddress2: String? = null

    @Column(name = "EXPORTER_CITY", nullable = false, length = 100)
    @Basic
    var exporterCity: String? = null

    @Column(name = "EXPORTER_COUNTRY", nullable = false, length = 100)
    @Basic
    var exporterCountry: String? = null

    @Column(name = "EXPORTER_ZIPCODE", nullable = false, length = 100)
    @Basic
    var exporterZipcode: String? = null

    @Column(name = "EXPORTER_TELEPHONE_NUMBER", nullable = false, length = 100)
    @Basic
    var exporterTelephoneNumber: String? = null

    @Column(name = "EXPORTER_FAX_NUMBER", nullable = true, length = 100)
    @Basic
    var exporterFaxNumber: String? = null

    @Column(name = "EXPORTER_EMAIL", nullable = false, length = 150)
    @Basic
    var exporterEmail: String? = null

    @Column(name = "PLACE_OF_INSPECTION", nullable = false, length = 4000)
    @Basic
    var placeOfInspection: String? = null

    @Column(name = "PLACE_OF_INSPECTION_ADDRESS", nullable = false, length = 500)
    @Basic
    var placeOfInspectionAddress: String? = null

    @Column(name = "PLACE_OF_INSPECTION_EMAIL", nullable = false, length = 500)
    @Basic
    var placeOfInspectionEmail: String? = null

    @Column(name = "PLACE_OF_INSPECTION_CONTACTS", nullable = false, length = 500)
    @Basic
    var placeOfInspectionContacts: String? = null

    @Column(name = "PORT_OF_LOADING", nullable = true, length = 200)
    @Basic
    var portOfLoading: String? = null

    @Column(name = "PORT_OF_DISCHARGE", nullable = false, length = 200)
    @Basic
    var portOfDischarge: String? = null

    @Column(name = "SHIPMENT_METHOD", nullable = false, length = 200)
    @Basic
    var shipmentMethod: String? = null

    @Column(name = "COUNTRY_OF_SUPPLY", nullable = false, length = 4000)
    @Basic
    var countryOfSupply: String? = null

    @Column(name = "ROUTE", nullable = false, length = 10)
    @Basic
    var route: String? = null

    @Column(name = "GOODS_CONDITION", nullable = false, length = 250)
    @Basic
    var goodsCondition: String? = null

    @Column(name = "ASSEMBLY_STATE", nullable = true, length = 250)
    @Basic
    var assemblyState: String? = null

    @Column(name = "LINK_TO_ATTACHED_DOCUMENTS", nullable = true, length = 4000)
    @Basic
    var linkToAttachedDocuments: String? = null

    @Column(name = "STATUS", nullable = true, precision = 0)
    @Basic
    var status: Long? = null

    @Column(name = "VAR_FIELD_1", nullable = true, length = 350)
    @Basic
    var varField1: String? = null

    @Column(name = "VAR_FIELD_2", nullable = true, length = 350)
    @Basic
    var varField2: String? = null

    @Column(name = "VAR_FIELD_3", nullable = true, length = 350)
    @Basic
    var varField3: String? = null

    @Column(name = "VAR_FIELD_4", nullable = true, length = 350)
    @Basic
    var varField4: String? = null

    @Column(name = "VAR_FIELD_5", nullable = true, length = 350)
    @Basic
    var varField5: String? = null

    @Column(name = "VAR_FIELD_6", nullable = true, length = 350)
    @Basic
    var varField6: String? = null

    @Column(name = "VAR_FIELD_7", nullable = true, length = 350)
    @Basic
    var varField7: String? = null

    @Column(name = "VAR_FIELD_8", nullable = true, length = 350)
    @Basic
    var varField8: String? = null

    @Column(name = "VAR_FIELD_9", nullable = true, length = 350)
    @Basic
    var varField9: String? = null

    @Column(name = "VAR_FIELD_10", nullable = true, length = 350)
    @Basic
    var varField10: String? = null

    @Column(name = "CREATED_BY", nullable = true, length = 100)
    @Basic
    var createdBy: String? = null

    @Column(name = "CREATED_ON", nullable = true)
    @Basic
    var createdOn: Timestamp? = null

    @Column(name = "MODIFIED_BY", nullable = true, length = 100)
    @Basic
    var modifiedBy: String? = null

    @Column(name = "MODIFIED_ON", nullable = true)
    @Basic
    var modifiedOn: Timestamp? = null

    @Column(name = "DELETE_BY", nullable = true, length = 100)
    @Basic
    var deleteBy: String? = null

    @Column(name = "DELETED_ON", nullable = true)
    @Basic
    var deletedOn: Timestamp? = null

    @Column(name = "PARTNER", nullable = true, precision = 0)
    @Basic
    var partner: Long? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as RfcCoiEntity
        return id == that.id &&
                rfcNumber == that.rfcNumber &&
                idfNumber == that.idfNumber &&
                ucrNumber == that.ucrNumber &&
                rfcDate == that.rfcDate &&
                countryOfDestination == that.countryOfDestination &&
                applicationType == that.applicationType &&
                sorReference == that.sorReference &&
                solReference == that.solReference &&
                importerName == that.importerName &&
                importerPin == that.importerPin &&
                importerAddress1 == that.importerAddress1 &&
                importerAddress2 == that.importerAddress2 &&
                importerCity == that.importerCity &&
                importerCountry == that.importerCountry &&
                importerZipcode == that.importerZipcode &&
                importerTelephoneNumber == that.importerTelephoneNumber &&
                importerFaxNumber == that.importerFaxNumber &&
                importerEmail == that.importerEmail &&
                exporterName == that.exporterName &&
                exporterPin == that.exporterPin &&
                exporterAddress1 == that.exporterAddress1 &&
                exporterAddress2 == that.exporterAddress2 &&
                exporterCity == that.exporterCity &&
                exporterCountry == that.exporterCountry &&
                exporterZipcode == that.exporterZipcode &&
                exporterTelephoneNumber == that.exporterTelephoneNumber &&
                exporterFaxNumber == that.exporterFaxNumber &&
                exporterEmail == that.exporterEmail &&
                placeOfInspection == that.placeOfInspection &&
                placeOfInspectionAddress == that.placeOfInspectionAddress &&
                placeOfInspectionEmail == that.placeOfInspectionEmail &&
                placeOfInspectionContacts == that.placeOfInspectionContacts &&
                portOfLoading == that.portOfLoading &&
                portOfDischarge == that.portOfDischarge &&
                shipmentMethod == that.shipmentMethod &&
                countryOfSupply == that.countryOfSupply &&
                route == that.route &&
                goodsCondition == that.goodsCondition &&
                assemblyState == that.assemblyState &&
                linkToAttachedDocuments == that.linkToAttachedDocuments &&
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
                partner == that.partner
    }

    override fun hashCode(): Int {
        return Objects.hash(
            id,
            rfcNumber,
            idfNumber,
            ucrNumber,
            rfcDate,
            countryOfDestination,
            applicationType,
            sorReference,
            solReference,
            importerName,
            importerPin,
            importerAddress1,
            importerAddress2,
            importerCity,
            importerCountry,
            importerZipcode,
            importerTelephoneNumber,
            importerFaxNumber,
            importerEmail,
            exporterName,
            exporterPin,
            exporterAddress1,
            exporterAddress2,
            exporterCity,
            exporterCountry,
            exporterZipcode,
            exporterTelephoneNumber,
            exporterFaxNumber,
            exporterEmail,
            placeOfInspection,
            placeOfInspectionAddress,
            placeOfInspectionEmail,
            placeOfInspectionContacts,
            portOfLoading,
            portOfDischarge,
            shipmentMethod,
            countryOfSupply,
            route,
            goodsCondition,
            assemblyState,
            linkToAttachedDocuments,
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
            deletedOn,
            partner
        )
    }
}