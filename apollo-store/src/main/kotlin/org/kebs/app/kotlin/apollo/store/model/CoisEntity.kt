package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@Entity
@Table(name = "DAT_KEBS_COIS")
class CoisEntity : Serializable {
    @Column(name = "ID", nullable = false, precision = 0)
    @Id
    @SequenceGenerator(name = "DAT_KEBS_COIS_SEQ_GEN", sequenceName = "DAT_KEBS_COIS_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_COIS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long = 0

    @NotEmpty(message = "Required field")
    @Column(name = "COI_NUMBER", nullable = false, length = 50)
    @Basic
    var coiNumber: String? = null

    @NotEmpty(message = "Required field")
    @Column(name = "IDF_NUMBER", nullable = false, length = 50)
    @Basic
    var idfNumber: String? = null

    @Column(name = "RFI_NUMBER", nullable = true, length = 50)
    @Basic
    var rfiNumber: String? = null

    @NotEmpty(message = "Required field")
    @Column(name = "UCR_NUMBER", nullable = false, length = 50)
    @Basic
    var ucrNumber: String? = null

    @Column(name = "RFC_DATE", nullable = true)
    @Basic
    var rfcDate: Timestamp? = null

    @NotNull(message = "Required field")
    @Column(name = "COI_ISSUE_DATE", nullable = false)
    @Basic
    var coiIssueDate: Timestamp? = null

    @NotEmpty(message = "Required field")
    @Column(name = "CLEAN", nullable = false, length = 1)
    @Basic
    var clean: String? = null

    @NotEmpty(message = "Required field")
    @Column(name = "COI_REMARKS", nullable = false, length = 4000)
    @Basic
    var coiRemarks: String? = null

    @NotEmpty(message = "Required field")
    @Column(name = "ISSUING_OFFICE", nullable = false, length = 4000)
    @Basic
    var issuingOffice: String? = null

    @NotEmpty(message = "Required field")
    @Column(name = "IMPORTER_NAME", nullable = false, length = 1000)
    @Basic
    var importerName: String? = null

    @NotEmpty(message = "Required field")
    @Column(name = "IMPORTER_PIN", nullable = false, length = 150)
    @Basic
    var importerPin: String? = null

    @NotEmpty(message = "Required field")
    @Column(name = "IMPORTER_ADDRESS_1", nullable = false, length = 1000)
    @Basic
    var importerAddress1: String? = null

    @Column(name = "IMPORTER_ADDRESS_2", nullable = true, length = 1000)
    @Basic
    var importerAddress2: String? = null

    @NotEmpty(message = "Required field")
    @Column(name = "IMPORTER_CITY", nullable = false, length = 100)
    @Basic
    var importerCity: String? = null

    @NotEmpty(message = "Required field")
    @Column(name = "IMPORTER_COUNTRY", nullable = false, length = 100)
    @Basic
    var importerCountry: String? = null

    @NotEmpty(message = "Required field")
    @Column(name = "IMPORTER_ZIPCODE", nullable = false, length = 100)
    @Basic
    var importerZipcode: String? = null

    @NotEmpty(message = "Required field")
    @Column(name = "IMPORTER_TELEPHONE_NUMBER", nullable = false, length = 100)
    @Basic
    var importerTelephoneNumber: String? = null

    @Column(name = "IMPORTER_FAX_NUMBER", nullable = true, length = 100)
    @Basic
    var importerFaxNumber: String? = null

    @NotEmpty(message = "Required field")
    @Column(name = "IMPORTER_EMAIL", nullable = false, length = 150)
    @Basic
    var importerEmail: String? = null

    @NotEmpty(message = "Required field")
    @Column(name = "EXPORTER_NAME", nullable = false, length = 1000)
    @Basic
    var exporterName: String? = null

    @NotEmpty(message = "Required field")
    @Column(name = "EXPORTER_PIN", nullable = false, length = 150)
    @Basic
    var exporterPin: String? = null

    @NotEmpty(message = "Required field")
    @Column(name = "EXPORTER_ADDRESS_1", nullable = false, length = 1000)
    @Basic
    var exporterAddress1: String? = null

    @Column(name = "EXPORTER_ADDRESS_2", nullable = true, length = 1000)
    @Basic
    var exporterAddress2: String? = null

    @NotEmpty(message = "Required field")
    @Column(name = "EXPORTER_CITY", nullable = false, length = 100)
    @Basic
    var exporterCity: String? = null

    @NotEmpty(message = "Required field")
    @Column(name = "EXPORTER_COUNTRY", nullable = false, length = 100)
    @Basic
    var exporterCountry: String? = null

    @NotEmpty(message = "Required field")
    @Column(name = "EXPORTER_ZIPCODE", nullable = false, length = 100)
    @Basic
    var exporterZipcode: String? = null

    @NotEmpty(message = "Required field")
    @Column(name = "EXPORTER_TELEPHONE_NUMBER", nullable = false, length = 100)
    @Basic
    var exporterTelephoneNumber: String? = null

    @Column(name = "EXPORTER_FAX_NUMBER", nullable = true, length = 100)
    @Basic
    var exporterFaxNumber: String? = null

    @NotEmpty(message = "Required field")
    @Column(name = "EXPORTER_EMAIL", nullable = false, length = 150)
    @Basic
    var exporterEmail: String? = null

    @NotEmpty(message = "Required field")
    @Column(name = "PLACE_OF_INSPECTION", nullable = false, length = 4000)
    @Basic
    var placeOfInspection: String? = null

    @NotNull(message = "Required field")
    @Column(name = "DATE_OF_INSPECTION", nullable = false)
    @Basic
    var dateOfInspection: Timestamp? = null

    @NotEmpty(message = "Required field")
    @Column(name = "PORT_OF_DESTINATION", nullable = false, length = 3000)
    @Basic
    var portOfDestination: String? = null

    @NotEmpty(message = "Required field")
    @Column(name = "SHIPMENT_MODE", nullable = false, length = 200)
    @Basic
    var shipmentMode: String? = null

    @NotEmpty(message = "Required field")
    @Column(name = "COUNTRY_OF_SUPPLY", nullable = false, length = 4000)
    @Basic
    var countryOfSupply: String? = null

    @NotNull(message = "Required field")
    @Column(name = "FINAL_INVOICE_FOB_VALUE", nullable = false, precision = 2)
    @Basic
    var finalInvoiceFobValue: Long = 0

    @NotNull(message = "Required field")
    @Column(name = "FINAL_INVOICE_EXCHANGE_RATE", nullable = false, precision = 2)
    @Basic
    var finalInvoiceExchangeRate: Long = 0

    @NotEmpty(message = "Required field")
    @Column(name = "FINAL_INVOICE_CURRENCY", nullable = false, length = 10)
    @Basic
    var finalInvoiceCurrency: String? = null

    @NotEmpty(message = "Required field")
    @Column(name = "FINAL_INVOICE_NUMBER", nullable = false, length = 4000)
    @Basic
    var finalInvoiceNumber: String? = null

    @NotNull(message = "Required field")
    @Column(name = "FINAL_INVOICE_DATE", nullable = false)
    @Basic
    var finalInvoiceDate: Timestamp? = null

    @NotNull(message = "Required field")
    @Column(name = "SHIPMENT_PARTIAL_NUMBER", nullable = false, precision = 2)
    @Basic
    var shipmentPartialNumber: Long = 0

    @NotEmpty(message = "Required field")
    @Column(name = "SHIPMENT_SEAL_NUMBERS", nullable = false, length = 4000)
    @Basic
    var shipmentSealNumbers: String? = null

    @Column(name = "SHIPMENT_CONTAINER_NUMBER", nullable = true, length = 4000)
    @Basic
    var shipmentContainerNumber: String? = null

    @NotEmpty(message = "Required field")
    @Column(name = "SHIPMENT_GROSS_WEIGHT", nullable = false, length = 10)
    @Basic
    var shipmentGrossWeight: String? = null

    @Column(name = "SHIPMENT_LINE_OWNER_PIN", nullable = true, length = 500)
    @Basic
    var shipmentLineOwnerPin: String? = null

    @Column(name = "SHIPMENT_LINE_OWNER_NAME", nullable = true, length = 500)
    @Basic
    var shipmentLineOwnerName: String? = null

    @NotEmpty(message = "Required field")
    @Column(name = "PRODUCT_CATEGORY", nullable = false, length = 2500)
    @Basic
    var productCategory: String? = null

    @Column(name = "STATUS", nullable = true, precision = 0)
    @Basic
    var status: Int? = null

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

    @Column(name = "PARTNER", nullable = true, length = 50)
    @Basic
    var partner: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as CoisEntity
        return id == that.id && finalInvoiceFobValue == that.finalInvoiceFobValue && finalInvoiceExchangeRate == that.finalInvoiceExchangeRate && shipmentPartialNumber == that.shipmentPartialNumber &&
                coiNumber == that.coiNumber &&
                idfNumber == that.idfNumber &&
                rfiNumber == that.rfiNumber &&
                ucrNumber == that.ucrNumber &&
                rfcDate == that.rfcDate &&
                coiIssueDate == that.coiIssueDate &&
                clean == that.clean &&
                coiRemarks == that.coiRemarks &&
                issuingOffice == that.issuingOffice &&
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
                dateOfInspection == that.dateOfInspection &&
                portOfDestination == that.portOfDestination &&
                shipmentMode == that.shipmentMode &&
                countryOfSupply == that.countryOfSupply &&
                finalInvoiceCurrency == that.finalInvoiceCurrency &&
                finalInvoiceNumber == that.finalInvoiceNumber &&
                finalInvoiceDate == that.finalInvoiceDate &&
                shipmentSealNumbers == that.shipmentSealNumbers &&
                shipmentContainerNumber == that.shipmentContainerNumber &&
                shipmentGrossWeight == that.shipmentGrossWeight &&
                shipmentLineOwnerPin == that.shipmentLineOwnerPin &&
                shipmentLineOwnerName == that.shipmentLineOwnerName &&
                productCategory == that.productCategory &&
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
            coiNumber,
            idfNumber,
            rfiNumber,
            ucrNumber,
            rfcDate,
            coiIssueDate,
            clean,
            coiRemarks,
            issuingOffice,
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
            dateOfInspection,
            portOfDestination,
            shipmentMode,
            countryOfSupply,
            finalInvoiceFobValue,
            finalInvoiceExchangeRate,
            finalInvoiceCurrency,
            finalInvoiceNumber,
            finalInvoiceDate,
            shipmentPartialNumber,
            shipmentSealNumbers,
            shipmentContainerNumber,
            shipmentGrossWeight,
            shipmentLineOwnerPin,
            shipmentLineOwnerName,
            productCategory,
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