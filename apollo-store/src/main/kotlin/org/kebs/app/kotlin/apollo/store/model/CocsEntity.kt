package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_COCS")
class CocsEntity : Serializable  {
    @Column(name = "ID", nullable = false, precision = 0)
    @Id
    @SequenceGenerator(name = "DAT_KEBS_COCS_SEQ_GEN", sequenceName = "DAT_KEBS_COCS_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_COCS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long? = 0

     @Column(name = "COC_NUMBER", nullable = false, length = 50)
     @Basic
    var cocNumber: String? = null
     @Column(name = "IDF_NUMBER", nullable = false, length = 50)
     @Basic
    var idfNumber: String? = null
     @Column(name = "RFI_NUMBER", nullable = true, length = 50)
     @Basic
    var rfiNumber: String? = null
     @Column(name = "UCR_NUMBER", nullable = false, length = 50)
     @Basic
    var ucrNumber: String? = null
     @Column(name = "RFC_DATE", nullable = true)
     @Basic
    var rfcDate: Timestamp? = null
     @Column(name = "COC_ISSUE_DATE", nullable = false)
     @Basic
    var cocIssueDate: Timestamp? = null
     @Column(name = "CLEAN", nullable = false, length = 1)
     @Basic
    var clean: String? = null
     @Column(name = "COC_REMARKS", nullable = false, length = 4000)
     @Basic
    var cocRemarks: String? = null
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
     @Column(name = "PLACE_OF_INSPECTION", nullable = false, length = 4000)
     @Basic
    var placeOfInspection: String? = null
     @Column(name = "DATE_OF_INSPECTION", nullable = false)
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
     @Column(name = "FINAL_INVOICE_FOB_VALUE", nullable = false, precision = 2)
     @Basic
    var finalInvoiceFobValue: Long = 0
     @Column(name = "FINAL_INVOICE_EXCHANGE_RATE", nullable = false, precision = 2)
     @Basic
    var finalInvoiceExchangeRate: Long = 0
     @Column(name = "FINAL_INVOICE_CURRENCY", nullable = false, length = 10)
     @Basic
    var finalInvoiceCurrency: String? = null
     @Column(name = "FINAL_INVOICE_DATE", nullable = false)
     @Basic
    var finalInvoiceDate: Timestamp? = null
     @Column(name = "SHIPMENT_PARTIAL_NUMBER", nullable = false, precision = 2)
     @Basic
    var shipmentPartialNumber: Long = 0
     @Column(name = "SHIPMENT_SEAL_NUMBERS", nullable = false, length = 4000)
     @Basic
    var shipmentSealNumbers: String? = null
     @Column(name = "SHIPMENT_CONTAINER_NUMBER", nullable = true, length = 4000)
     @Basic
    var shipmentContainerNumber: String? = null
     @Column(name = "SHIPMENT_GROSS_WEIGHT", nullable = false, length = 10)
     @Basic
    var shipmentGrossWeight: String? = null
     @Column(name = "SHIPMENT_LINE_NUMBER", nullable = false, precision = 2)
     @Basic
    var shipmentLineNumber: Long = 0
     @Column(name = "SHIPMENT_LINE_QUANTITY", nullable = false, precision = 2)
     @Basic
    var shipmentLineQuantity: Long = 0
     @Column(name = "SHIPMENT_LINE_UNITOF_MEASURE", nullable = false, length = 400)
     @Basic
    var shipmentLineUnitofMeasure: String? = null
     @Column(name = "SHIPMENT_LINE_BRANDNAME", nullable = true, length = 1000)
     @Basic
    var shipmentLineBrandname: String? = null
     @Column(name = "SHIPMENT_LINE_DESCRIPTION", nullable = false, length = 4000)
     @Basic
    var shipmentLineDescription: String? = null
     @Column(name = "SHIPMENT_LINE_VIN", nullable = true, length = 30)
     @Basic
    var shipmentLineVin: String? = null
     @Column(name = "SHIPMENT_LINE_STICKER_NUMBER", nullable = true, length = 30)
     @Basic
    var shipmentLineStickerNumber: String? = null
     @Column(name = "SHIPMENT_LINE_ICS", nullable = true, length = 4000)
     @Basic
    var shipmentLineIcs: String? = null
     @Column(name = "SHIPMENT_LINE_STANDARDS_REFERENCE", nullable = false, length = 4000)
     @Basic
    var shipmentLineStandardsReference: String? = null
     @Column(name = "SHIPMENT_LINE_REGISTRATION", nullable = true, length = 100)
     @Basic
    var shipmentLineRegistration: String? = null
     @Column(name = "ROUTE", nullable = false, length = 10)
     @Basic
    var route: String? = null
     @Column(name = "PRODUCT_CATEGORY", nullable = false, length = 2500)
     @Basic
    var productCategory: String? = null
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

    @Column(name = "COC_TYPE", nullable = true, precision = 0)
    @Basic
    var cocType: Long? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as CocsEntity
        if (id != that.id) return false
        if (finalInvoiceFobValue != that.finalInvoiceFobValue) return false
        if (finalInvoiceExchangeRate != that.finalInvoiceExchangeRate) return false
        if (shipmentPartialNumber != that.shipmentPartialNumber) return false
        if (shipmentLineNumber != that.shipmentLineNumber) return false
        if (shipmentLineQuantity != that.shipmentLineQuantity) return false
        if (if (cocNumber != null) cocNumber != that.cocNumber else that.cocNumber != null) return false
        if (if (idfNumber != null) idfNumber != that.idfNumber else that.idfNumber != null) return false
        if (if (rfiNumber != null) rfiNumber != that.rfiNumber else that.rfiNumber != null) return false
        if (if (ucrNumber != null) ucrNumber != that.ucrNumber else that.ucrNumber != null) return false
        if (if (rfcDate != null) !rfcDate!!.equals(that.rfcDate) else that.rfcDate != null) return false
        if (if (cocIssueDate != null) !cocIssueDate!!.equals(that.cocIssueDate) else that.cocIssueDate != null) return false
        if (if (clean != null) clean != that.clean else that.clean != null) return false
        if (if (cocRemarks != null) cocRemarks != that.cocRemarks else that.cocRemarks != null) return false
        if (if (issuingOffice != null) issuingOffice != that.issuingOffice else that.issuingOffice != null) return false
        if (if (importerName != null) importerName != that.importerName else that.importerName != null) return false
        if (if (importerPin != null) importerPin != that.importerPin else that.importerPin != null) return false
        if (if (importerAddress1 != null) importerAddress1 != that.importerAddress1 else that.importerAddress1 != null) return false
        if (if (importerAddress2 != null) importerAddress2 != that.importerAddress2 else that.importerAddress2 != null) return false
        if (if (importerCity != null) importerCity != that.importerCity else that.importerCity != null) return false
        if (if (importerCountry != null) importerCountry != that.importerCountry else that.importerCountry != null) return false
        if (if (importerZipCode != null) importerZipCode != that.importerZipCode else that.importerZipCode != null) return false
        if (if (importerTelephoneNumber != null) importerTelephoneNumber != that.importerTelephoneNumber else that.importerTelephoneNumber != null) return false
        if (if (importerFaxNumber != null) importerFaxNumber != that.importerFaxNumber else that.importerFaxNumber != null) return false
        if (if (importerEmail != null) importerEmail != that.importerEmail else that.importerEmail != null) return false
        if (if (exporterName != null) exporterName != that.exporterName else that.exporterName != null) return false
        if (if (exporterPin != null) exporterPin != that.exporterPin else that.exporterPin != null) return false
        if (if (exporterAddress1 != null) exporterAddress1 != that.exporterAddress1 else that.exporterAddress1 != null) return false
        if (if (exporterAddress2 != null) exporterAddress2 != that.exporterAddress2 else that.exporterAddress2 != null) return false
        if (if (exporterCity != null) exporterCity != that.exporterCity else that.exporterCity != null) return false
        if (if (exporterCountry != null) exporterCountry != that.exporterCountry else that.exporterCountry != null) return false
        if (if (exporterZipCode != null) exporterZipCode != that.exporterZipCode else that.exporterZipCode != null) return false
        if (if (exporterTelephoneNumber != null) exporterTelephoneNumber != that.exporterTelephoneNumber else that.exporterTelephoneNumber != null) return false
        if (if (exporterFaxNumber != null) exporterFaxNumber != that.exporterFaxNumber else that.exporterFaxNumber != null) return false
        if (if (exporterEmail != null) exporterEmail != that.exporterEmail else that.exporterEmail != null) return false
        if (if (placeOfInspection != null) placeOfInspection != that.placeOfInspection else that.placeOfInspection != null) return false
        if (if (dateOfInspection != null) !dateOfInspection!!.equals(that.dateOfInspection) else that.dateOfInspection != null) return false
        if (if (portOfDestination != null) portOfDestination != that.portOfDestination else that.portOfDestination != null) return false
        if (if (shipmentMode != null) shipmentMode != that.shipmentMode else that.shipmentMode != null) return false
        if (if (countryOfSupply != null) countryOfSupply != that.countryOfSupply else that.countryOfSupply != null) return false
        if (if (finalInvoiceCurrency != null) finalInvoiceCurrency != that.finalInvoiceCurrency else that.finalInvoiceCurrency != null) return false
        if (if (finalInvoiceDate != null) !finalInvoiceDate!!.equals(that.finalInvoiceDate) else that.finalInvoiceDate != null) return false
        if (if (shipmentSealNumbers != null) shipmentSealNumbers != that.shipmentSealNumbers else that.shipmentSealNumbers != null) return false
        if (if (shipmentContainerNumber != null) shipmentContainerNumber != that.shipmentContainerNumber else that.shipmentContainerNumber != null) return false
        if (if (shipmentGrossWeight != null) shipmentGrossWeight != that.shipmentGrossWeight else that.shipmentGrossWeight != null) return false
        if (if (shipmentLineUnitofMeasure != null) shipmentLineUnitofMeasure != that.shipmentLineUnitofMeasure else that.shipmentLineUnitofMeasure != null) return false
        if (if (shipmentLineBrandname != null) shipmentLineBrandname != that.shipmentLineBrandname else that.shipmentLineBrandname != null) return false
        if (if (shipmentLineDescription != null) shipmentLineDescription != that.shipmentLineDescription else that.shipmentLineDescription != null) return false
        if (if (shipmentLineVin != null) shipmentLineVin != that.shipmentLineVin else that.shipmentLineVin != null) return false
        if (if (shipmentLineStickerNumber != null) shipmentLineStickerNumber != that.shipmentLineStickerNumber else that.shipmentLineStickerNumber != null) return false
        if (if (shipmentLineIcs != null) shipmentLineIcs != that.shipmentLineIcs else that.shipmentLineIcs != null) return false
        if (if (shipmentLineStandardsReference != null) shipmentLineStandardsReference != that.shipmentLineStandardsReference else that.shipmentLineStandardsReference != null) return false
        if (if (shipmentLineRegistration != null) shipmentLineRegistration != that.shipmentLineRegistration else that.shipmentLineRegistration != null) return false
        if (if (route != null) route != that.route else that.route != null) return false
        if (if (productCategory != null) productCategory != that.productCategory else that.productCategory != null) return false
        if (if (status != null) status != that.status else that.status != null) return false
        if (if (cocType != null) cocType != that.cocType else that.cocType != null) return false
        if (if (varField1 != null) varField1 != that.varField1 else that.varField1 != null) return false
        if (if (varField2 != null) varField2 != that.varField2 else that.varField2 != null) return false
        if (if (varField3 != null) varField3 != that.varField3 else that.varField3 != null) return false
        if (if (varField4 != null) varField4 != that.varField4 else that.varField4 != null) return false
        if (if (varField5 != null) varField5 != that.varField5 else that.varField5 != null) return false
        if (if (varField6 != null) varField6 != that.varField6 else that.varField6 != null) return false
        if (if (varField7 != null) varField7 != that.varField7 else that.varField7 != null) return false
        if (if (varField8 != null) varField8 != that.varField8 else that.varField8 != null) return false
        if (if (varField9 != null) varField9 != that.varField9 else that.varField9 != null) return false
        if (if (varField10 != null) varField10 != that.varField10 else that.varField10 != null) return false
        if (if (createdBy != null) createdBy != that.createdBy else that.createdBy != null) return false
        if (if (createdOn != null) !createdOn!!.equals(that.createdOn) else that.createdOn != null) return false
        if (if (modifiedBy != null) modifiedBy != that.modifiedBy else that.modifiedBy != null) return false
        if (if (modifiedOn != null) !modifiedOn!!.equals(that.modifiedOn) else that.modifiedOn != null) return false
        if (if (deleteBy != null) deleteBy != that.deleteBy else that.deleteBy != null) return false
        return if (if (deletedOn != null) !deletedOn!!.equals(that.deletedOn) else that.deletedOn != null) false else true
    }

//    override fun hashCode(): Int {
//        var result = (id?.xor((id!! ushr 32)))?.toInt()
//        result = 31 * result + if (cocNumber != null) cocNumber.hashCode() else 0
//        result = 31 * result + if (idfNumber != null) idfNumber.hashCode() else 0
//        result = 31 * result + if (rfiNumber != null) rfiNumber.hashCode() else 0
//        result = 31 * result + if (ucrNumber != null) ucrNumber.hashCode() else 0
//        result = 31 * result + if (rfcDate != null) rfcDate.hashCode() else 0
//        result = 31 * result + if (cocIssueDate != null) cocIssueDate.hashCode() else 0
//        result = 31 * result + if (clean != null) clean.hashCode() else 0
//        result = 31 * result + if (cocRemarks != null) cocRemarks.hashCode() else 0
//        result = 31 * result + if (issuingOffice != null) issuingOffice.hashCode() else 0
//        result = 31 * result + if (importerName != null) importerName.hashCode() else 0
//        result = 31 * result + if (importerPin != null) importerPin.hashCode() else 0
//        result = 31 * result + if (importerAddress1 != null) importerAddress1.hashCode() else 0
//        result = 31 * result + if (importerAddress2 != null) importerAddress2.hashCode() else 0
//        result = 31 * result + if (importerCity != null) importerCity.hashCode() else 0
//        result = 31 * result + if (importerCountry != null) importerCountry.hashCode() else 0
//        result = 31 * result + if (importerZipcode != null) importerZipcode.hashCode() else 0
//        result = 31 * result + if (importerTelephoneNumber != null) importerTelephoneNumber.hashCode() else 0
//        result = 31 * result + if (importerFaxNumber != null) importerFaxNumber.hashCode() else 0
//        result = 31 * result + if (importerEmail != null) importerEmail.hashCode() else 0
//        result = 31 * result + if (exporterName != null) exporterName.hashCode() else 0
//        result = 31 * result + if (exporterPin != null) exporterPin.hashCode() else 0
//        result = 31 * result + if (exporterAddress1 != null) exporterAddress1.hashCode() else 0
//        result = 31 * result + if (exporterAddress2 != null) exporterAddress2.hashCode() else 0
//        result = 31 * result + if (exporterCity != null) exporterCity.hashCode() else 0
//        result = 31 * result + if (exporterCountry != null) exporterCountry.hashCode() else 0
//        result = 31 * result + if (exporterZipcode != null) exporterZipcode.hashCode() else 0
//        result = 31 * result + if (exporterTelephoneNumber != null) exporterTelephoneNumber.hashCode() else 0
//        result = 31 * result + if (exporterFaxNumber != null) exporterFaxNumber.hashCode() else 0
//        result = 31 * result + if (exporterEmail != null) exporterEmail.hashCode() else 0
//        result = 31 * result + if (placeOfInspection != null) placeOfInspection.hashCode() else 0
//        result = 31 * result + if (dateOfInspection != null) dateOfInspection.hashCode() else 0
//        result = 31 * result + if (portOfDestination != null) portOfDestination.hashCode() else 0
//        result = 31 * result + if (shipmentMode != null) shipmentMode.hashCode() else 0
//        result = 31 * result + if (countryOfSupply != null) countryOfSupply.hashCode() else 0
//        result = 31 * result + (finalInvoiceFobValue xor (finalInvoiceFobValue ushr 32)).toInt()
//        result = 31 * result + (finalInvoiceExchangeRate xor (finalInvoiceExchangeRate ushr 32)).toInt()
//        result = 31 * result + if (finalInvoiceCurrency != null) finalInvoiceCurrency.hashCode() else 0
//        result = 31 * result + if (finalInvoiceDate != null) finalInvoiceDate.hashCode() else 0
//        result = 31 * result + (shipmentPartialNumber xor (shipmentPartialNumber ushr 32)).toInt()
//        result = 31 * result + if (shipmentSealNumbers != null) shipmentSealNumbers.hashCode() else 0
//        result = 31 * result + if (shipmentContainerNumber != null) shipmentContainerNumber.hashCode() else 0
//        result = 31 * result + if (shipmentGrossWeight != null) shipmentGrossWeight.hashCode() else 0
//        result = 31 * result + (shipmentLineNumber xor (shipmentLineNumber ushr 32)).toInt()
//        result = 31 * result + (shipmentLineQuantity xor (shipmentLineQuantity ushr 32)).toInt()
//        result = 31 * result + if (shipmentLineUnitofMeasure != null) shipmentLineUnitofMeasure.hashCode() else 0
//        result = 31 * result + if (shipmentLineBrandname != null) shipmentLineBrandname.hashCode() else 0
//        result = 31 * result + if (shipmentLineDescription != null) shipmentLineDescription.hashCode() else 0
//        result = 31 * result + if (shipmentLineVin != null) shipmentLineVin.hashCode() else 0
//        result = 31 * result + if (shipmentLineStickerNumber != null) shipmentLineStickerNumber.hashCode() else 0
//        result = 31 * result + if (shipmentLineIcs != null) shipmentLineIcs.hashCode() else 0
//        result =
//            31 * result + if (shipmentLineStandardsReference != null) shipmentLineStandardsReference.hashCode() else 0
//        result = 31 * result + if (shipmentLineRegistration != null) shipmentLineRegistration.hashCode() else 0
//        result = 31 * result + if (route != null) route.hashCode() else 0
//        result = 31 * result + if (productCategory != null) productCategory.hashCode() else 0
//        result = 31 * result + if (status != null) status.hashCode() else 0
//        result = 31 * result + if (varField1 != null) varField1.hashCode() else 0
//        result = 31 * result + if (varField2 != null) varField2.hashCode() else 0
//        result = 31 * result + if (varField3 != null) varField3.hashCode() else 0
//        result = 31 * result + if (varField4 != null) varField4.hashCode() else 0
//        result = 31 * result + if (varField5 != null) varField5.hashCode() else 0
//        result = 31 * result + if (varField6 != null) varField6.hashCode() else 0
//        result = 31 * result + if (varField7 != null) varField7.hashCode() else 0
//        result = 31 * result + if (varField8 != null) varField8.hashCode() else 0
//        result = 31 * result + if (varField9 != null) varField9.hashCode() else 0
//        result = 31 * result + if (varField10 != null) varField10.hashCode() else 0
//        result = 31 * result + if (createdBy != null) createdBy.hashCode() else 0
//        result = 31 * result + if (createdOn != null) createdOn.hashCode() else 0
//        result = 31 * result + if (modifiedBy != null) modifiedBy.hashCode() else 0
//        result = 31 * result + if (modifiedOn != null) modifiedOn.hashCode() else 0
//        result = 31 * result + if (deleteBy != null) deleteBy.hashCode() else 0
//        result = 31 * result + if (deletedOn != null) deletedOn.hashCode() else 0
//        return result
//    }
}