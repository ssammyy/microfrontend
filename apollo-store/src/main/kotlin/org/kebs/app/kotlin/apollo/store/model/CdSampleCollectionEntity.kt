package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Timestamp
import java.sql.Date
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_CD_SAMPLE_COLLECTION")
class CdSampleCollectionEntity : Serializable{
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_CD_SAMPLE_COLLECTION_SEQ_GEN", allocationSize = 1, sequenceName = "DAT_KEBS_CD_SAMPLE_COLLECTION_SEQ")
    @GeneratedValue(generator = "DAT_KEBS_CD_SAMPLE_COLLECTION_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = 0

    @Column(name = "PRODUCT_BRAND_NAME")
    @Basic
    var productBrandName: String? = null

    /*
    @JoinColumn(name = "PERMIT_ID", referencedColumnName = "ID")
    @ManyToOne
    var permitId: PermitApplicationEntity? = null
    */

    @Column(name = "PERMIT_ID")
    @Basic
    var permitId: Long? = null

    @Column(name = "SAMPLE_COLLECTION_FILE_PATH")
    @Basic
    var sampleCollectionFilePath: String? = null

    @Column(name = "BATCH_NUMBER")
    @Basic
    var batchNumber: String? = null

    @Column(name = "SL_NO")
    @Basic
    var slNo: String? = null

    @Column(name = "SAVE_REASON")
    @Basic
    var saveReason: String? = null

    @Column(name = "BATCH_SIZE")
    @Basic
    var batchSize: Long? = null

    @Column(name = "SAMPLE_SIZE")
    @Basic
    var sampleSize: String? = null

    @Column(name = "SAMPLING_METHOD")
    @Basic
    var samplingMethod: String? = null

    @Column(name = "REASONS_COLLECT_SAMPLE")
    @Basic
    var reasonsCollectSample: String? = null

    @Column(name = "COLLECT_ANY_REMARKS")
    @Basic
    var collectAnyRemarks: String? = null

    @Column(name = "UCR_NUMBER")
    @Basic
    var ucrNumber: String? = null

    @Column(name = "MANUF_TRADER_NAME")
    @Basic
    var manufTraderName: String? = null

    @Column(name = "MANUF_TRADER_ADDRESS")
    @Basic
    var manufTraderAddress: String? = null

    @Column(name = "ITEM_HSCODE")
    @Basic
    var itemHscode: String? = null

    @Column(name = "ITEM_ID")
    @Basic
    var itemId: Long? = null

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

    @Column(name = "REF_NUMBER")
    @Basic
    var refNumber: String? = null

    @Column(name = "LABELING_DETAILS")
    @Basic
    var labelingDetails: String? = null

    @Column(name = "CONTACT_PERSON_NAME")
    @Basic
    var contactPersonName: String? = null

    @Column(name = "CONTACT_PERSON_TEL")
    @Basic
    var contactPersonTel: String? = null

    @Column(name = "CONTAINER_NOS")
    @Basic
    var containerNos: String? = null

    @Column(name = "CONDITION_OF_PRODUCT")
    @Basic
    var conditionOfProduct: String? = null

    @Column(name = "REFERENCE_STANDARDS")
    @Basic
    var referenceStandards: String? = null

    @Column(name = "MODE_RELEASE")
    @Basic
    var modeRelease: String? = null

    @Column(name = "NON_COMPLIANCE_REMARKS")
    @Basic
    var nonComplianceRemarks: String? = null

    @Column(name = "NON_COMPLIANCE_STATUS")
    @Basic
    var nonComplianceStatus: Long? = null

    @Column(name = "CUSTOMS_I_E_NO")
    @Basic
    var customsIENo: String? = null

    @Column(name = "KEBS_DESIGNATION")
    @Basic
    var kebsDesignation: String? = null

    @Column(name = "POINT_OF_INSPECTION")
    @Basic
    var pointOfInspection: String? = null

    @Column(name = "IMPORTER_NAME")
    @Basic
    var importerName: String? = null

    @Column(name = "IMPORTER_ADDRESS")
    @Basic
    var importerAddress: String? = null

    @Column(name = "IMPORTER_TELEPHONE")
    @Basic
    var importerTelephone: String? = null

    @Column(name = "IMPORTER_FAX")
    @Basic
    var importerFax: String? = null

    @Column(name = "IMPORTER_PHYSICAL_LOCATION")
    @Basic
    var importerPhysicalLocation: String? = null

    @Column(name = "SUPPLIER_NAME")
    @Basic
    var supplierName: String? = null

    @Column(name = "SUPPLIER_ADDRESS")
    @Basic
    var supplierAddress: String? = null

    @Column(name = "COUNTRY_OF_ORIGIN")
    @Basic
    var countryOfOrigin: String? = null

    @Column(name = "QUANTITY")
    @Basic
    var quantity: String? = null

    @Column(name = "COC_NUMBER")
    @Basic
    var cocNumber: String? = null

    @Column(name = "MANUFACTURING_DATE")
    @Basic
    var manufacturingDate: Date? = null

    @Column(name = "EXPIRY_DATE")
    @Basic
    var expiryDate: Date? = null

    @Column(name = "WITNESS_NAME")
    @Basic
    var witnessName: String? = null

    @Column(name = "WITNESS_DESIGNATION")
    @Basic
    var witnessDesignation: String? = null

    @Column(name = "WITNESS_DATE")
    @Basic
    var witnessDate: Date? = null

    @Column(name = "KEBS_INSPECTOR_DATE")
    @Basic
    var kebsInspectorDate: Date? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as CdSampleCollectionEntity
        return id == that.id &&
                slNo == that.slNo &&
                productBrandName == that.productBrandName &&
                batchNumber == that.batchNumber &&
                batchSize == that.batchSize &&
                sampleSize == that.sampleSize &&
                samplingMethod == that.samplingMethod &&
                reasonsCollectSample == that.reasonsCollectSample &&
                collectAnyRemarks == that.collectAnyRemarks &&
                ucrNumber == that.ucrNumber &&
                manufTraderName == that.manufTraderName &&
                manufTraderAddress == that.manufTraderAddress &&
                itemHscode == that.itemHscode &&
                itemId == that.itemId &&
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
                refNumber == that.refNumber &&
                labelingDetails == that.labelingDetails &&
                contactPersonName == that.contactPersonName &&
                contactPersonTel == that.contactPersonTel &&
                containerNos == that.containerNos &&
                conditionOfProduct == that.conditionOfProduct &&
                referenceStandards == that.referenceStandards &&
                modeRelease == that.modeRelease &&
                nonComplianceRemarks == that.nonComplianceRemarks &&
                nonComplianceStatus == that.nonComplianceStatus &&
                customsIENo == that.customsIENo &&
                kebsDesignation == that.kebsDesignation &&
                pointOfInspection == that.pointOfInspection &&
                importerName == that.importerName &&
                importerAddress == that.importerAddress &&
                importerTelephone == that.importerTelephone &&
                importerFax == that.importerFax &&
                importerPhysicalLocation == that.importerPhysicalLocation &&
                supplierName == that.supplierName &&
                supplierAddress == that.supplierAddress &&
                countryOfOrigin == that.countryOfOrigin &&
                quantity == that.quantity &&
                cocNumber == that.cocNumber &&
                manufacturingDate == that.manufacturingDate &&
                expiryDate == that.expiryDate &&
                witnessName == that.witnessName &&
                permitId == that.permitId &&
                witnessDesignation == that.witnessDesignation &&
                witnessDate == that.witnessDate &&
                sampleCollectionFilePath == that.sampleCollectionFilePath &&
                kebsInspectorDate == that.kebsInspectorDate
    }

    override fun hashCode(): Int {
        return Objects.hash(id,slNo, productBrandName, sampleCollectionFilePath, permitId, batchNumber, batchSize, sampleSize, samplingMethod, reasonsCollectSample, collectAnyRemarks, ucrNumber, manufTraderName, manufTraderAddress, itemHscode, itemId, status, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn, refNumber, labelingDetails, contactPersonName, contactPersonTel, containerNos, conditionOfProduct, referenceStandards, modeRelease, nonComplianceRemarks, nonComplianceStatus, customsIENo, kebsDesignation, pointOfInspection, importerName, importerAddress, importerTelephone, importerFax, importerPhysicalLocation, supplierName, supplierAddress, countryOfOrigin, quantity, cocNumber, manufacturingDate, expiryDate, witnessName, witnessDesignation, witnessDate, kebsInspectorDate)
    }
}