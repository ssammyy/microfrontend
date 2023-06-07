package org.kebs.app.kotlin.apollo.store.model.importer

import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import java.io.Serializable
import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_RFC_DOCUMENTS_DETAILS")
class RfcDocumentsDetailsEntity : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_RFC_DOCUMENTS_DETAILS_SEQ_GEN", sequenceName = "DAT_KEBS_RFC_DOCUMENTS_DETAILS_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_RFC_DOCUMENTS_DETAILS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long = 0

    @Column(name = "RFC_NUMBER")
    @Basic
    var rfcNumber: String? = null

    @Column(name = "RFC_DATE")
    @Basic
    var rfcDate: Date? = null

    @Column(name = "IDF_NUMBER")
    @Basic
    var idfNumber: String? = null

    @Column(name = "UCR_NUMBER")
    @Basic
    var ucrNumber: String? = null

    @Column(name = "COUNTRY_OF_DESTINATION")
    @Basic
    var countryOfDestination: String? = null

    @Column(name = "APPLICATION_TYPE")
    @Basic
    var applicationType: String? = null

    @Column(name = "SOR_REFERENCE")
    @Basic
    var sorReference: String? = null

    @Column(name = "SOL_REFERENCE")
    @Basic
    var solReference: String? = null

    @Column(name = "IMPORTER_NAME")
    @Basic
    var importerName: String? = null

    @Column(name = "IMPORTER_PIN")
    @Basic
    var importerPin: String? = null

    @Column(name = "IMPORTER_ADDRESS_1")
    @Basic
    var importerAddress1: String? = null

    @Column(name = "IMPORTER_ADDRESS_2")
    @Basic
    var importerAddress2: String? = null

    @Column(name = "IMPORTER_CITY")
    @Basic
    var importerCity: String? = null

    @Column(name = "IMPORTER_COUNTRY")
    @Basic
    var importerCountry: String? = null

    @Column(name = "IMPORTER_ZIP_CODE")
    @Basic
    var importerZipCode: String? = null

    @Column(name = "IMPORTER_TELEPHONE_NUMBER")
    @Basic
    var importerTelephoneNumber: String? = null

    @Column(name = "IMPORTER_FAX_NUMBER")
    @Basic
    var importerFaxNumber: String? = null

    @Column(name = "IMPORTER_EMAIL")
    @Basic
    var importerEmail: String? = null

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

    @Column(name = "EXPORTER_CITY")
    @Basic
    var exporterCity: String? = null

    @Column(name = "EXPORTER_COUNTRY")
    @Basic
    var exporterCountry: String? = null

    @Column(name = "EXPORTER_ZIP_CODE")
    @Basic
    var exporterZipCode: String? = null

    @Column(name = "EXPORTER_TELEPHONE_NUMBER")
    @Basic
    var exporterTelephoneNumber: String? = null

    @Column(name = "EXPORTER_FAX_NUMBER")
    @Basic
    var exporterFaxNumber: String? = null

    @Column(name = "EXPORTER_EMAIL")
    @Basic
    var exporterEmail: String? = null

    @Column(name = "THIRD_PARTY_NAME")
    @Basic
    var thirdPartyName: String? = null

    @Column(name = "THIRD_PARTY_PIN")
    @Basic
    var thirdPartyPin: String? = null

    @Column(name = "THIRD_PARTY_ADDRESS_1")
    @Basic
    var thirdPartyAddress1: String? = null

    @Column(name = "THIRD_PARTY_ADDRESS_2")
    @Basic
    var thirdPartyAddress2: String? = null

    @Column(name = "THIRD_PARTY_CITY")
    @Basic
    var thirdPartyCity: String? = null

    @Column(name = "THIRD_PARTY_COUNTRY")
    @Basic
    var thirdPartyCountry: String? = null

    @Column(name = "THIRD_PARTY_ZIP_CODE")
    @Basic
    var thirdPartyZipCode: String? = null

    @Column(name = "THIRD_PARTY_TELEPHONE_NUMBER")
    @Basic
    var thirdPartyTelephoneNumber: String? = null

    @Column(name = "THIRD_PARTY_FAX_NUMBER")
    @Basic
    var thirdPartyFaxNumber: String? = null

    @Column(name = "THIRD_PARTY_EMAIL")
    @Basic
    var thirdPartyEmail: String? = null

    @Column(name = "APPLICANT_NAME")
    @Basic
    var applicantName: String? = null

    @Column(name = "APPLICANT_PIN")
    @Basic
    var applicantPin: String? = null

    @Column(name = "APPLICANT_ADDRESS_1")
    @Basic
    var applicantAddress1: String? = null

    @Column(name = "APPLICANT_ADDRESS_2")
    @Basic
    var applicantAddress2: String? = null

    @Column(name = "APPLICANT_CITY")
    @Basic
    var applicantCity: String? = null

    @Column(name = "APPLICANT_COUNTRY")
    @Basic
    var applicantCountry: String? = null

    @Column(name = "APPLICANT_ZIP_CODE")
    @Basic
    var applicantZipCode: String? = null

    @Column(name = "APPLICANT_TELEPHONE_NUMBER")
    @Basic
    var applicantTelephoneNumber: String? = null

    @Column(name = "APPLICANT_FAX_NUMBER")
    @Basic
    var applicantFaxNumber: String? = null

    @Column(name = "APPLICANT_EMAIL")
    @Basic
    var applicantEmail: String? = null

    @Column(name = "PLACE_OF_INSPECTION")
    @Basic
    var placeOfInspection: String? = null

    @Column(name = "PLACE_OF_INSPECTION_ADDRESS")
    @Basic
    var placeOfInspectionAddress: String? = null

    @Column(name = "PLACE_OF_INSPECTION_EMAIL")
    @Basic
    var placeOfInspectionEmail: String? = null

    @Column(name = "PLACE_OF_INSPECTION_CONTACTS")
    @Basic
    var placeOfInspectionContacts: String? = null

    @Column(name = "PORT_OF_LOADING")
    @Basic
    var portOfLoading: String? = null

    @Column(name = "PORT_OF_DISCHARGE")
    @Basic
    var portOfDischarge: String? = null

    @Column(name = "SHIPPING_METHOD")
    @Basic
    var shippingMethod: String? = null

    @Column(name = "COUNTRY_OF_SUPPLY")
    @Basic
    var countryOfSupply: String? = null

    @Column(name = "ROUTE")
    @Basic
    var route: String? = null

    @Column(name = "GOODS_CONDITION")
    @Basic
    var goodsCondition: String? = null

    @Column(name = "ASSEMBLY_STATE")
    @Basic
    var assemblyState: String? = null

    @Column(name = "LIST_TO_ATTACHED_DOCUMENTS")
    @Basic
    var listToAttachedDocuments: String? = null

    @Column(name = "PREFERRED_DATE_OF_INSPECTION")
    @Basic
    var preferredDateOfInspection: Date? = null

    @Column(name = "MAKE")
    @Basic
    var make: String? = null

    @Column(name = "MODEL")
    @Basic
    var model: String? = null

    @Column(name = "CHASSIS_NUMBER")
    @Basic
    var chassisNumber: String? = null

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

    @Column(name = "PARTNER")
    @Basic
    var partner: String? = null

    @Column(name = "COMPLETENESS_STATUS")
    @Basic
    var completenessStatus: Int? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null

    @Column(name = "DESCRIPTIONS")
    @Basic
    var descriptions: String? = null

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

    @Column(name = "VERSION")
    @Basic
    var version: Long? = null

    @JoinColumn(name = "IMPORTER_ID", referencedColumnName = "ID")
    @ManyToOne
    var importerID: UsersEntity? = null

    @JoinColumn(name = "RFC_TYPE_ID", referencedColumnName = "ID")
    @ManyToOne
    var rfcTypeId: RfcTypesEntity? = null


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as RfcDocumentsDetailsEntity
        return id == that.id &&
                rfcNumber == that.rfcNumber &&
                rfcDate == that.rfcDate &&
                idfNumber == that.idfNumber &&
                ucrNumber == that.ucrNumber &&
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
                importerZipCode == that.importerZipCode &&
                importerTelephoneNumber == that.importerTelephoneNumber &&
                importerFaxNumber == that.importerFaxNumber &&
                importerEmail == that.importerEmail &&
                exporterName == that.exporterName &&
                exporterPin == that.exporterPin &&
                exporterAddress1 == that.exporterAddress1 &&
                exporterAddress2 == that.exporterAddress2 &&
                exporterCity == that.exporterCity &&
                exporterCountry == that.exporterCountry &&
                exporterZipCode == that.exporterZipCode &&
                exporterTelephoneNumber == that.exporterTelephoneNumber &&
                exporterFaxNumber == that.exporterFaxNumber &&
                exporterEmail == that.exporterEmail &&
                thirdPartyName == that.thirdPartyName &&
                thirdPartyPin == that.thirdPartyPin &&
                thirdPartyAddress1 == that.thirdPartyAddress1 &&
                thirdPartyAddress2 == that.thirdPartyAddress2 &&
                thirdPartyCity == that.thirdPartyCity &&
                thirdPartyCountry == that.thirdPartyCountry &&
                thirdPartyZipCode == that.thirdPartyZipCode &&
                thirdPartyTelephoneNumber == that.thirdPartyTelephoneNumber &&
                thirdPartyFaxNumber == that.thirdPartyFaxNumber &&
                thirdPartyEmail == that.thirdPartyEmail &&
                applicantName == that.applicantName &&
                applicantPin == that.applicantPin &&
                applicantAddress1 == that.applicantAddress1 &&
                applicantAddress2 == that.applicantAddress2 &&
                applicantCity == that.applicantCity &&
                applicantCountry == that.applicantCountry &&
                applicantZipCode == that.applicantZipCode &&
                applicantTelephoneNumber == that.applicantTelephoneNumber &&
                applicantFaxNumber == that.applicantFaxNumber &&
                applicantEmail == that.applicantEmail &&
                placeOfInspection == that.placeOfInspection &&
                placeOfInspectionAddress == that.placeOfInspectionAddress &&
                placeOfInspectionEmail == that.placeOfInspectionEmail &&
                placeOfInspectionContacts == that.placeOfInspectionContacts &&
                portOfLoading == that.portOfLoading &&
                portOfDischarge == that.portOfDischarge &&
                shippingMethod == that.shippingMethod &&
                countryOfSupply == that.countryOfSupply &&
                route == that.route &&
                goodsCondition == that.goodsCondition &&
                assemblyState == that.assemblyState &&
                listToAttachedDocuments == that.listToAttachedDocuments &&
                preferredDateOfInspection == that.preferredDateOfInspection &&
                make == that.make &&
                model == that.model &&
                chassisNumber == that.chassisNumber &&
                engineNumber == that.engineNumber &&
                engineCapacity == that.engineCapacity &&
                yearOfManufacture == that.yearOfManufacture &&
                yearOfFirstRegistration == that.yearOfFirstRegistration &&
                partner == that.partner &&
                completenessStatus == that.completenessStatus &&
                status == that.status &&
                descriptions == that.descriptions &&
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
        return Objects.hash(id, rfcNumber, completenessStatus, rfcDate, idfNumber, ucrNumber, countryOfDestination, applicationType, sorReference, solReference, importerName, importerPin, importerAddress1, importerAddress2, importerCity, importerCountry, importerZipCode, importerTelephoneNumber, importerFaxNumber, importerEmail, exporterName, exporterPin, exporterAddress1, exporterAddress2, exporterCity, exporterCountry, exporterZipCode, exporterTelephoneNumber, exporterFaxNumber, exporterEmail, thirdPartyName, thirdPartyPin, thirdPartyAddress1, thirdPartyAddress2, thirdPartyCity, thirdPartyCountry, thirdPartyZipCode, thirdPartyTelephoneNumber, thirdPartyFaxNumber, thirdPartyEmail, applicantName, applicantPin, applicantAddress1, applicantAddress2, applicantCity, applicantCountry, applicantZipCode, applicantTelephoneNumber, applicantFaxNumber, applicantEmail, placeOfInspection, placeOfInspectionAddress, placeOfInspectionEmail, placeOfInspectionContacts, portOfLoading, portOfDischarge, shippingMethod, countryOfSupply, route, goodsCondition, assemblyState, listToAttachedDocuments, preferredDateOfInspection, make, model, chassisNumber, engineNumber, engineCapacity, yearOfManufacture, yearOfFirstRegistration, partner, status, descriptions, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, lastModifiedBy, lastModifiedOn, updateBy, updatedOn, deleteBy, deletedOn, version)
    }
}