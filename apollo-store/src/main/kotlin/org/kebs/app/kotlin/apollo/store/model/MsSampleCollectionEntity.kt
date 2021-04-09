package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Date
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_MS_SAMPLE_COLLECTION")
class MsSampleCollectionEntity : Serializable{
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_MS_SAMPLE_COLLECTION_SEQ_GEN", allocationSize = 1, sequenceName = "DAT_KEBS_MS_SAMPLE_COLLECTION_SEQ")
    @GeneratedValue(generator = "DAT_KEBS_MS_SAMPLE_COLLECTION_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = 0

    @Column(name = "NAME_MANUFACTURER_TRADER")
    @Basic
    var nameManufacturerTrader: String? = null

    @Column(name = "ADDRESS_MANUFACTURE_TRADER")
    @Basic
    var addressManufacturerTrader: String? = null

    @Column(name = "SAMPLING_METHOD")
    @Basic
    var samplingMethod: String? = null

    @Column(name = "REASONS_COLLECTING_SAMPLES")
    @Basic
    var reasonsCollectingSamples: String? = null

    @Column(name = "ANY_REMARKS")
    @Basic
    var anyRemarks: String? = null

    @Column(name = "NAME_OFFICER_COLLECTING_SAMPLE")
    @Basic
    var nameOfficerCollectingSample: String? = null

    @Column(name = "DESIGNATION_OFFICER_COLLECTING_SAMPLE")
    @Basic
    var designationOfficerCollectingSample: String? = null

    @Column(name = "DATE_OFFICER_COLLECTING_SAMPLE")
    @Basic
    var dateOfficerCollectingSample: Date? = null

    @Column(name = "NAME_WITNESS")
    @Basic
    var nameWitness: String? = null

    @Column(name = "DESIGNATION_WITNESS")
    @Basic
    var designationWitness: String? = null

    @Column(name = "DATE_WITNESS")
    @Basic
    var dateWitness: Date? = null

    @Column(name = "PRODUCT_BRAND_NAME_1")
    @Basic
    var productBrandName1: String? = null

    @Column(name = "BATCH_NO_1")
    @Basic
    var batchNo1: String? = null

    @Column(name = "BATCH_SIZE_1")
    @Basic
    var batchSize1: String? = null

    @Column(name = "SAMPLE_SIZE_1")
    @Basic
    var sampleSize1: String? = null

    @Column(name = "PRODUCT_BRAND_NAME_2")
    @Basic
    var productBrandName2: String? = null

    @Column(name = "BATCH_NO_2")
    @Basic
    var batchNo2: String? = null

    @Column(name = "BATCH_SIZE_2")
    @Basic
    var batchSize2: String? = null

    @Column(name = "SAMPLE_SIZE_2")
    @Basic
    var sampleSize2: String? = null

    @Column(name = "PRODUCT_BRAND_NAME_3")
    @Basic
    var productBrandName3: String? = null

    @Column(name = "BATCH_NO_3")
    @Basic
    var batchNo3: String? = null

    @Column(name = "BATCH_SIZE_3")
    @Basic
    var batchSize3: String? = null

    @Column(name = "SAMPLE_SIZE_3")
    @Basic
    var sampleSize3: String? = null

    @Column(name = "PRODUCT_BRAND_NAME_4")
    @Basic
    var productBrandName4: String? = null

    @Column(name = "BATCH_NO_4")
    @Basic
    var batchNo4: String? = null

    @Column(name = "BATCH_SIZE_4")
    @Basic
    var batchSize4: String? = null

    @Column(name = "SAMPLE_SIZE_4")
    @Basic
    var sampleSize4: String? = null

    @Column(name = "PRODUCT_BRAND_NAME_5")
    @Basic
    var productBrandName5: String? = null

    @Column(name = "BATCH_NO_5")
    @Basic
    var batchNo5: String? = null

    @Column(name = "BATCH_SIZE_5")
    @Basic
    var batchSize5: String? = null

    @Column(name = "SAMPLE_SIZE_5")
    @Basic
    var sampleSize5: String? = null


    @Column(name = "STATUS")
    @Basic
    var status: Int? = 0

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

    @JoinColumn(name = "MS_WORKPLAN_GENERATED_ID", referencedColumnName = "ID")
    @ManyToOne
    var workPlanGeneratedID: MsWorkPlanGeneratedEntity? = null

    @JoinColumn(name = "MS_FUEL_INSPECTION_ID", referencedColumnName = "ID")
    @ManyToOne
    var msFuelInspectionId: MsFuelInspectionEntity? = null

//    @OneToMany(mappedBy = "datKebsMsSampleCollectionBySampleCollectionId")
//    var datKebsMsCollectionParametersById: Collection<MsCollectionParametersEntity>? = null

//    @OneToMany(mappedBy = "datKebsMsSampleCollectionBySampleCollectionNumber")
//    var sampleSubmissionsById: Collection<MsSampleSubmissionEntity>? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as MsSampleCollectionEntity
        return id == that.id &&
                nameManufacturerTrader == that.nameManufacturerTrader &&
                samplingMethod == that.samplingMethod &&
                reasonsCollectingSamples == that.reasonsCollectingSamples &&
                anyRemarks == that.anyRemarks &&
                nameOfficerCollectingSample == that.nameOfficerCollectingSample &&
                designationOfficerCollectingSample == that.designationOfficerCollectingSample &&
                dateOfficerCollectingSample == that.dateOfficerCollectingSample &&
                nameWitness == that.nameWitness &&
                designationWitness == that.designationWitness &&
                dateWitness == that.dateWitness &&
                addressManufacturerTrader == that.addressManufacturerTrader &&

                productBrandName1 == that.productBrandName1 &&
                productBrandName2 == that.productBrandName2 &&
                productBrandName3 == that.productBrandName3 &&
                productBrandName4 == that.productBrandName4 &&
                productBrandName5 == that.productBrandName5 &&

                batchNo1 == that.batchNo1 &&
                batchNo2 == that.batchNo2 &&
                batchNo3 == that.batchNo3 &&
                batchNo4 == that.batchNo4 &&
                batchNo5 == that.batchNo5 &&

                batchSize1 == that.batchSize1 &&
                batchSize2 == that.batchSize2 &&
                batchSize3 == that.batchSize3 &&
                batchSize4 == that.batchSize4 &&
                batchSize5 == that.batchSize5 &&

                sampleSize1 == that.sampleSize1 &&
                sampleSize2 == that.sampleSize2 &&
                sampleSize3 == that.sampleSize3 &&
                sampleSize4 == that.sampleSize4 &&
                sampleSize5 == that.sampleSize5 &&

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
        return Objects.hash(id,
                productBrandName1, productBrandName2, productBrandName3,productBrandName4,productBrandName5,
                batchNo1, batchNo2,batchNo3,batchNo4,batchNo5,
                batchSize1,batchSize2,batchSize3,batchSize4,batchSize5,
                sampleSize1,sampleSize2,sampleSize3,sampleSize4,sampleSize5,
                addressManufacturerTrader, nameManufacturerTrader, samplingMethod, reasonsCollectingSamples, anyRemarks, nameOfficerCollectingSample, designationOfficerCollectingSample, dateOfficerCollectingSample, nameWitness, designationWitness, dateWitness, status, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn)
    }

}