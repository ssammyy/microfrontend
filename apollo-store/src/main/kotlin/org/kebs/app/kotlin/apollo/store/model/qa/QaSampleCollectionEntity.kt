package org.kebs.app.kotlin.apollo.store.model.qa

import java.io.Serializable
import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_QA_SAMPLE_COLLECTION")
class QaSampleCollectionEntity : Serializable {
    @Column(name = "ID")
    @Id
    @SequenceGenerator(name = "DAT_KEBS_MANUFACTURE_PLANT_DETAILS_SEQ_GEN", sequenceName = "DAT_KEBS_MANUFACTURE_PLANT_DETAILS_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_MANUFACTURE_PLANT_DETAILS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long = 0

    @Column(name = "SCF_NO")
    @Basic
    var scfNo: String? = null

    @Column(name = "PERMIT_ID")
    @Basic
    var permitId: String? = null

    @Column(name = "ITEM_ID")
    @Basic
    var itemId: Long? = null

    @Column(name = "NAME_OF_MANUFACTURE")
    @Basic
    var nameOfManufacture: String? = null

    @Column(name = "ADDRESS_OF_MANUFACTURE")
    @Basic
    var addressOfManufacture: String? = null

    @Column(name = "NAME_OF_PRODUCT")
    @Basic
    var nameOfProduct: String? = null

    @Column(name = "BRAND_NAME")
    @Basic
    var brandName: String? = null

    @Column(name = "BATCH_NO")
    @Basic
    var batchNo: String? = null

    @Column(name = "QUANTITY_DECLARED")
    @Basic
    var quantityDeclared: String? = null

    @Column(name = "REFERENCE_STANDARD")
    @Basic
    var referenceStandard: String? = null

    @Column(name = "BATCH_SIZE")
    @Basic
    var batchSize: String? = null

    @Column(name = "SAMPLE_SIZE")
    @Basic
    var sampleSize: String? = null

    @Column(name = "SAMPLING_METHOD")
    @Basic
    var samplingMethod: String? = null

    @Column(name = "REASON_FOR_COLLECTING_SAMPLE")
    @Basic
    var reasonForCollectingSample: String? = null

    @Column(name = "ANY_REMARKS")
    @Basic
    var anyRemarks: String? = null

    @Column(name = "NAME_OF_OFFICER")
    @Basic
    var nameOfOfficer: String? = null

    @Column(name = "OFFICER_DESIGNATION")
    @Basic
    var officerDesignation: String? = null

    @Column(name = "OFFICER_DATE")
    @Basic
    var officerDate: Date? = null

    @Column(name = "NAME_OF_WITNESS")
    @Basic
    var nameOfWitness: String? = null

    @Column(name = "WITNESS_DESIGNATION")
    @Basic
    var witnessDesignation: String? = null

    @Column(name = "WITNESS_DATE")
    @Basic
    var witnessDate: Date? = null

    @Column(name = "DESCRIPTION")
    @Basic
    var description: String? = null

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other  as QaSampleCollectionEntity
        return id == that.id &&
                permitId == that.permitId &&
                nameOfManufacture == that.nameOfManufacture &&
                addressOfManufacture == that.addressOfManufacture &&
                nameOfProduct == that.nameOfProduct &&
                brandName == that.brandName &&
                batchNo == that.batchNo &&
                batchSize == that.batchSize &&
                sampleSize == that.sampleSize &&
                samplingMethod == that.samplingMethod &&
                reasonForCollectingSample == that.reasonForCollectingSample &&
                anyRemarks == that.anyRemarks &&
                nameOfOfficer == that.nameOfOfficer &&
                officerDesignation == that.officerDesignation &&
                officerDate == that.officerDate &&
                nameOfWitness == that.nameOfWitness &&
                witnessDesignation == that.witnessDesignation &&
                witnessDate == that.witnessDate &&
                description == that.description &&
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
            permitId,
            nameOfManufacture,
            addressOfManufacture,
            nameOfProduct,
            brandName,
            batchNo,
            batchSize,
            sampleSize,
            samplingMethod,
            reasonForCollectingSample,
            anyRemarks,
            nameOfOfficer,
            officerDesignation,
            officerDate,
            nameOfWitness,
            witnessDesignation,
            witnessDate,
            description,
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