package org.kebs.app.kotlin.apollo.store.model

import org.kebs.app.kotlin.apollo.store.model.di.ConsignmentDocumentDetailsEntity
import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotEmpty

@Entity
@Table(name = "DAT_KEBS_COCS")
class CocsEntity : Serializable {
    @Id
    @SequenceGenerator(name = "DAT_KEBS_COCS_SEQ_GEN", sequenceName = "DAT_KEBS_COCS_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_COCS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID", nullable = false, precision = 0)
    var id: Long = 0

    @Column(name = "COC_NUMBER", nullable = false, length = 50)
    @Basic
    var cocNumber: String? = null

    @Column(name = "COI_NUMBER", nullable = false, length = 50)
    @Basic
    var coiNumber: String? = null

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

    @Column(name = "ACCEPTABLE_DOC_DATE", nullable = true)
    @Basic
    var acceptableDocDate: Timestamp? = null

    @Column(name = "FINAL_DOC_DATE", nullable = true)
    @Basic
    var finalDocDate: Timestamp? = null

    @Column(name = "RFC_DATE", nullable = true)
    @Basic
    var rfcDate: Timestamp? = null

    @Column(name = "COC_ISSUE_DATE", nullable = true)
    @Basic
    var cocIssueDate: Timestamp? = null

    @Column(name = "COI_ISSUE_DATE", nullable = true)
    @Basic
    var coiIssueDate: Timestamp? = null

    @NotEmpty(message = "Required field")
    @Column(name = "IS_CLEAN", nullable = false, length = 1)
    @Basic
    var clean: String? = null

    @Column(name = "COC_REMARKS", nullable = false, length = 4000)
    @Basic
    var cocRemarks: String? = null

    @Column(name = "COI_REMARKS", nullable = false, length = 4000)
    @Basic
    var coiRemarks: String? = null

    @Column(name = "ISSUING_OFFICE", nullable = false, length = 4000)
    @Basic
    var issuingOffice: String? = null

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
    var importerZipCode: String? = null

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
    var exporterZipCode: String? = null

    @Column(name = "EXPORTER_TELEPHONE_NUMBER", nullable = false, length = 100)
    @Basic
    var exporterTelephoneNumber: String? = null

    @Column(name = "EXPORTER_FAX_NUMBER", nullable = true, length = 100)
    @Basic
    var exporterFaxNumber: String? = null

    @Column(name = "EXPORTER_EMAIL", nullable = false, length = 150)
    @Basic
    var exporterEmail: String? = null

    @Column(name = "PLACE_OF_INSPECTION", length = 4000)
    @Basic
    var placeOfInspection: String? = null


    @Column(name = "INSPECTION_ZONE", length = 400)
    @Basic
    var inspectionZone: String? = null

    @Column(name = "INSPECTION_PROVINCE", length = 400)
    @Basic
    var inspectionProvince: String? = null

    @Column(name = "DATE_OF_INSPECTION")
    @Basic
    var dateOfInspection: Timestamp? = null

    @Column(name = "PORT_OF_DESTINATION", nullable = false, length = 3000)
    @Basic
    var portOfDestination: String? = null

    @Column(name = "SHIPMENT_MODE", nullable = false, length = 200)
    @Basic
    var shipmentMode: String? = null

    @Column(name = "COUNTRY_OF_SUPPLY", nullable = false, length = 4000)
    @Basic
    var countryOfSupply: String? = null

    @Column(name = "FINAL_INVOICE_FOB_VALUE", nullable = false, precision = 17, scale = 2)
    @Basic
    var finalInvoiceFobValue: Double = 0.0

    @Column(name = "FINAL_INVOICE_NUMBER", nullable = false)
    @Basic
    var finalInvoiceNumber: String? = null

    @Column(name = "FINAL_INVOICE_EXCHANGE_RATE", precision = 17, scale = 2)
    @Basic
    var finalInvoiceExchangeRate: Double = 0.0

    @Column(name = "FINAL_INVOICE_CURRENCY", length = 10)
    @Basic
    var finalInvoiceCurrency: String? = null

    @Column(name = "FINAL_INVOICE_DATE")
    @Basic
    var finalInvoiceDate: Timestamp? = null


    @Column(name = "INVOICE_PAYMENT_DATE")
    @Basic
    var invoicePaymentDate: Timestamp? = null

    @Column(name = "SHIPMENT_PARTIAL_NUMBER")
    @Basic
    var shipmentPartialNumber: Long = 0

    @Column(name = "SHIPMENT_SEAL_NUMBERS", nullable = true, length = 4000)
    @Basic
    var shipmentSealNumbers: String? = null

    @Column(name = "SHIPMENT_CONTAINER_NUMBER", nullable = true, length = 4000)
    @Basic
    var shipmentContainerNumber: String? = null

    @Column(name = "SHIPMENT_GROSS_WEIGHT", nullable = true, length = 10)
    @Basic
    var shipmentGrossWeight: String? = null

    @Column(name = "SHIPMENT_QUANTITY_DELIVERED")
    @Basic
    var shipmentQuantityDelivered: String? = null

    @Column(name = "ROUTE", nullable = false, length = 10)
    @Basic
    var route: String? = null

    @Column(name = "PRODUCT_CATEGORY", nullable = false, length = 2500)
    @Basic
    var productCategory: String? = null

    @Column(name = "COC_TYPE", length = 2)
    @Basic
    var cocType: String? = null

    @Column(name = "DOCUMENT_TYPE", length = 10)
    @Basic
    var documentsType: String? = "L"

    @Column(name = "CLEARING_AGENT")
    @Basic
    var clearingAgent: String? = null

    @Column(name = "CUSTOMS_ENTRY_NUMBER")
    @Basic
    var customsEntryNumber: String? = null

    @Column(name = "REVIEW_STATUS", nullable = true, precision = 0)
    @Basic
    var reviewStatus: Int? = null

    @Column(name = "REVIEW_REMARKS", nullable = true, precision = 0)
    @Basic
    var reviewRemarks: String? = null

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

    @Column(name = "VERSION", nullable = true)
    @Basic
    var version: Long? = null

    @Column(name = "PVOC_PARTNER", nullable = true)
    @Basic
    var partner: Long? = null

    @JoinColumn(name = "CD_ID", referencedColumnName = "ID")
    @ManyToOne
    var consignmentDocId: ConsignmentDocumentDetailsEntity? = null

    @Column(name = "REPORT_GENERATION_STATUS", nullable = true, length = 50)
    @Basic
    var reportGenerationStatus: Int? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as CocsEntity
        return id == that.id && finalInvoiceFobValue == that.finalInvoiceFobValue && finalInvoiceExchangeRate == that.finalInvoiceExchangeRate && shipmentPartialNumber == that.shipmentPartialNumber &&
                cocNumber == that.cocNumber &&
                coiNumber == that.coiNumber &&
                finalInvoiceNumber == that.finalInvoiceNumber &&
                idfNumber == that.idfNumber &&
                rfiNumber == that.rfiNumber &&
                ucrNumber == that.ucrNumber &&
                rfcDate == that.rfcDate &&
                shipmentQuantityDelivered == that.shipmentQuantityDelivered &&
                cocIssueDate == that.cocIssueDate &&
                coiIssueDate == that.coiIssueDate &&
                clean == that.clean &&
                cocRemarks == that.cocRemarks &&
                coiRemarks == that.coiRemarks &&
                issuingOffice == that.issuingOffice &&
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
                placeOfInspection == that.placeOfInspection &&
                dateOfInspection == that.dateOfInspection &&
                portOfDestination == that.portOfDestination &&
                shipmentMode == that.shipmentMode &&
                countryOfSupply == that.countryOfSupply &&
                finalInvoiceCurrency == that.finalInvoiceCurrency &&
                finalInvoiceDate == that.finalInvoiceDate &&
                shipmentSealNumbers == that.shipmentSealNumbers &&
                shipmentContainerNumber == that.shipmentContainerNumber &&
                shipmentGrossWeight == that.shipmentGrossWeight &&
                partner == that.partner &&
                route == that.route &&
                productCategory == that.productCategory &&
                cocType == that.cocType &&
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
                reportGenerationStatus == that.reportGenerationStatus &&
                partner == that.partner

    }

    override fun hashCode(): Int {
        var result = Objects.hash(
                id,
                cocNumber,
                coiNumber,
                finalInvoiceNumber,
                idfNumber,
                rfiNumber,
                ucrNumber,
                rfcDate,
                cocIssueDate,
                coiIssueDate,
                shipmentQuantityDelivered,
                clean,
                cocRemarks,
                coiRemarks,
                issuingOffice,
                importerName,
                importerPin,
                importerAddress1,
                importerAddress2,
                importerCity,
                importerCountry,
                importerZipCode,
                importerTelephoneNumber,
                importerFaxNumber,
                importerEmail,
                exporterName,
                exporterPin,
                exporterAddress1,
                exporterAddress2,
                exporterCity,
                exporterCountry,
                exporterZipCode,
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
                finalInvoiceDate,
                shipmentPartialNumber,
                shipmentSealNumbers,
                shipmentContainerNumber,
                shipmentGrossWeight,
//            shipmentGrossWeightUnit,
                route,
                cocType,
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
                reportGenerationStatus,
                partner
        )
        return result
    }
}
