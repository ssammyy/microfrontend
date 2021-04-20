package org.kebs.app.kotlin.apollo.store.model

import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_PVOC_COR_TIMELINES_DATA", schema = "APOLLO", catalog = "")
class DatKebsPvocCorTimelinesDataEntity {
    @Column(name = "ID")
    @GeneratedValue
    @Id
    var id: Long? = null
        private set

    @Column(name = "IDF_NUMBER")
    @Basic
    var idfNumber: String? = null

    @Column(name = "RFI_NUMBER")
    @Basic
    var rfiNumber: String? = null

    @Column(name = "CERTIFICATE_NUMBER")
    @Basic
    var certificateNumber: String? = null

    @Column(name = "CERTIFICATE_ISSUE_DATE")
    @Basic
    var certificateIssueDate: Timestamp? = null

    @Column(name = "COC_NCR_NUMBER")
    @Basic
    var cocNcrNumber: String? = null

    @Column(name = "REMARKS")
    @Basic
    var remarks: String? = null

    @Column(name = "ISSUING_OFFICE")
    @Basic
    var issuingOffice: String? = null

    @Column(name = "IMPORTER")
    @Basic
    var importer: String? = null

    @Column(name = "IMPORTER_ADDRESS_L1")
    @Basic
    var importerAddressL1: String? = null

    @Column(name = "IMPORTER_ADDRESS_L2")
    @Basic
    var importerAddressL2: String? = null

    @Column(name = "IMPORTER_CITY")
    @Basic
    var importerCity: String? = null

    @Column(name = "IMPORTER_COUNTRY")
    @Basic
    var importerCountry: String? = null

    @Column(name = "IMPORTER_ZIP_CODE")
    @Basic
    var importerZipCode: String? = null

    @Column(name = "IMPORTER_TELEPHONE")
    @Basic
    var importerTelephone: String? = null

    @Column(name = "IMPORTER_FAX_NO")
    @Basic
    var importerFaxNo: String? = null

    @Column(name = "IMPORTER_EMAIL")
    @Basic
    var importerEmail: String? = null

    @Column(name = "PORT_OF_INSPECTION")
    @Basic
    var portOfInspection: String? = null

    @Column(name = "DATE_OF_INSPECTION")
    @Basic
    var dateOfInspection: Timestamp? = null

    @Column(name = "SHIPMENT_MODE")
    @Basic
    var shipmentMode: String? = null

    @Column(name = "COUNTRY_OF_SUPPLY")
    @Basic
    var countryOfSupply: String? = null

    @Column(name = "FINAL_INVOICE_FOB_VALUE")
    @Basic
    var finalInvoiceFobValue: String? = null

    @Column(name = "FINAL_INVOICE_CURRENCY")
    @Basic
    var finalInvoiceCurrency: String? = null

    @Column(name = "FINAL_INVOICE_EXCHANGE_RATE")
    @Basic
    var finalInvoiceExchangeRate: String? = null

    @Column(name = "FINAL_INVOICE_NUMBER")
    @Basic
    var finalInvoiceNumber: String? = null

    @Column(name = "FINAL_INVOICE_DATE")
    @Basic
    var finalInvoiceDate: Timestamp? = null

    @Column(name = "SHIPMENT_PARTIAL_NUMBER")
    @Basic
    var shipmentPartialNumber: String? = null

    @Column(name = "SHIPMENT_SEAL_NUMBER")
    @Basic
    var shipmentSealNumber: String? = null

    @Column(name = "SHIPMENT_QUALITY_DELIVERED")
    @Basic
    var shipmentQualityDelivered: String? = null

    @Column(name = "SHIPMENT_LINE_NUMBER")
    @Basic
    var shipmentLineNumber: String? = null

    @Column(name = "SHIPMENT_LINE_HS_CODE")
    @Basic
    var shipmentLineHsCode: String? = null

    @Column(name = "SHIPMENT_LINE_UNIT_OF_MEASURE")
    @Basic
    var shipmentLineUnitOfMeasure: String? = null

    @Column(name = "SHIPMENT_LINE_DESCRIPTION")
    @Basic
    var shipmentLineDescription: String? = null

    @Column(name = "SHIPMENT_LINE_VIN")
    @Basic
    var shipmentLineVin: String? = null

    @Column(name = "SHIPMENT_LINE_LOCAL_ICS")
    @Basic
    var shipmentLineLocalIcs: String? = null

    @Column(name = "SHIPMENT_LINE_STANDARDS_REF")
    @Basic
    var shipmentLineStandardsRef: String? = null

    @Column(name = "ROUTE")
    @Basic
    var route: String? = null

    @Column(name = "UCR_NUMBER")
    @Basic
    var ucrNumber: String? = null

    @Column(name = "INSPECTION_SCHEDULING")
    @Basic
    var inspectionScheduling: String? = null

    @Column(name = "TAT_COC_INSURANCE")
    @Basic
    var tatCocInsurance: String? = null

    @Column(name = "STATUS")
    @Basic
    var status: Long? = null

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

    @Column(name = "HOD_STATUS")
    @Basic
    var hodStatus: String? = null

    @Column(name = "MPVOC_AGENT")
    @Basic
    var mpvocAgent: Long? = null

    @Column(name = "PVOC_MONIT_STATUS")
    @Basic
    var pvocMonitStatus: Long? = null

    @Column(name = "PVOC_MONIT_STARTED_ON")
    @Basic
    var pvocMonitStartedOn: Timestamp? = null

    @Column(name = "PVOC_MONIT_COMPLETED_ON")
    @Basic
    var pvocMonitCompletedOn: Timestamp? = null

    @Column(name = "PVOC_MONIT_PROCESS_INSTANCE_ID")
    @Basic
    var pvocMonitProcessInstanceId: String? = null
    fun setId(id: Long) {
        this.id = id
    }

    fun setId(id: Long?) {
        this.id = id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as DatKebsPvocCorTimelinesDataEntity
        return id == that.id && idfNumber == that.idfNumber && rfiNumber == that.rfiNumber && certificateNumber == that.certificateNumber && certificateIssueDate == that.certificateIssueDate && cocNcrNumber == that.cocNcrNumber && remarks == that.remarks && issuingOffice == that.issuingOffice && importer == that.importer && importerAddressL1 == that.importerAddressL1 && importerAddressL2 == that.importerAddressL2 && importerCity == that.importerCity && importerCountry == that.importerCountry && importerZipCode == that.importerZipCode && importerTelephone == that.importerTelephone && importerFaxNo == that.importerFaxNo && importerEmail == that.importerEmail && portOfInspection == that.portOfInspection && dateOfInspection == that.dateOfInspection && shipmentMode == that.shipmentMode && countryOfSupply == that.countryOfSupply && finalInvoiceFobValue == that.finalInvoiceFobValue && finalInvoiceCurrency == that.finalInvoiceCurrency && finalInvoiceExchangeRate == that.finalInvoiceExchangeRate && finalInvoiceNumber == that.finalInvoiceNumber && finalInvoiceDate == that.finalInvoiceDate && shipmentPartialNumber == that.shipmentPartialNumber && shipmentSealNumber == that.shipmentSealNumber && shipmentQualityDelivered == that.shipmentQualityDelivered && shipmentLineNumber == that.shipmentLineNumber && shipmentLineHsCode == that.shipmentLineHsCode && shipmentLineUnitOfMeasure == that.shipmentLineUnitOfMeasure && shipmentLineDescription == that.shipmentLineDescription && shipmentLineVin == that.shipmentLineVin && shipmentLineLocalIcs == that.shipmentLineLocalIcs && shipmentLineStandardsRef == that.shipmentLineStandardsRef && route == that.route && ucrNumber == that.ucrNumber && inspectionScheduling == that.inspectionScheduling && tatCocInsurance == that.tatCocInsurance && status == that.status && varField1 == that.varField1 && varField2 == that.varField2 && varField3 == that.varField3 && varField4 == that.varField4 && varField5 == that.varField5 && varField6 == that.varField6 && varField7 == that.varField7 && varField8 == that.varField8 && varField9 == that.varField9 && varField10 == that.varField10 && createdBy == that.createdBy && createdOn == that.createdOn && modifiedBy == that.modifiedBy && modifiedOn == that.modifiedOn && deleteBy == that.deleteBy && deletedOn == that.deletedOn && hodStatus == that.hodStatus && mpvocAgent == that.mpvocAgent && pvocMonitStatus == that.pvocMonitStatus && pvocMonitStartedOn == that.pvocMonitStartedOn && pvocMonitCompletedOn == that.pvocMonitCompletedOn && pvocMonitProcessInstanceId == that.pvocMonitProcessInstanceId
    }

    override fun hashCode(): Int {
        return Objects.hash(
            id,
            idfNumber,
            rfiNumber,
            certificateNumber,
            certificateIssueDate,
            cocNcrNumber,
            remarks,
            issuingOffice,
            importer,
            importerAddressL1,
            importerAddressL2,
            importerCity,
            importerCountry,
            importerZipCode,
            importerTelephone,
            importerFaxNo,
            importerEmail,
            portOfInspection,
            dateOfInspection,
            shipmentMode,
            countryOfSupply,
            finalInvoiceFobValue,
            finalInvoiceCurrency,
            finalInvoiceExchangeRate,
            finalInvoiceNumber,
            finalInvoiceDate,
            shipmentPartialNumber,
            shipmentSealNumber,
            shipmentQualityDelivered,
            shipmentLineNumber,
            shipmentLineHsCode,
            shipmentLineUnitOfMeasure,
            shipmentLineDescription,
            shipmentLineVin,
            shipmentLineLocalIcs,
            shipmentLineStandardsRef,
            route,
            ucrNumber,
            inspectionScheduling,
            tatCocInsurance,
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
            hodStatus,
            mpvocAgent,
            pvocMonitStatus,
            pvocMonitStartedOn,
            pvocMonitCompletedOn,
            pvocMonitProcessInstanceId
        )
    }
}