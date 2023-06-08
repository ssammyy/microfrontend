package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Date
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "MS_SAMPLE_COLLECTION")
class MsSampleCollectionView: Serializable {
    @Column(name = "ID")
//    @SequenceGenerator(name = "DAT_KEBS_MS_DATA_REPORT_SEQ_GEN", allocationSize = 1, sequenceName = "DAT_KEBS_MS_DATA_REPORT_SEQ")
//    @GeneratedValue(generator = "DAT_KEBS_MS_DATA_REPORT_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = 0

    @Column(name = "PRODUCT_BRAND_NAME")
    @Basic
    var productBrandName: String? = null

    @Column(name = "BATCH_SIZE")
    @Basic
    var batchSize: String? = null

    @Column(name = "BATCH_NO")
    @Basic
    var batchNo: String? = null

    @Column(name = "SAMPLE_SIZE")
    @Basic
    var sampleSize: String? = null

    @Column(name = "SAMPLE_COLLECTION_ID")
    @Basic
    var sampleCollectionId: Long? = null

    @Column(name = "NAME_MANUFACTURER_TRADER")
    @Basic
    var nameManufacturerTrader: String? = null

    @Column(name = "ADDRESS_MANUFACTURE_TRADER")
    @Basic
    var addressManufactureTrader: String? = null

    @Column(name = "ANY_REMARKS")
    @Basic
    var anyRemarks: String? = null

    @Column(name = "NAME_OFFICER_COLLECTING_SAMPLE")
    @Basic
    var nameOfficerCollectingSample: String? = null

    @Column(name = "DATE_OFFICER_COLLECTING_SAMPLE")
    @Basic
    var dateOfficerCollectingSample: Date? = null

    @Column(name = "DESIGNATION_OFFICER_COLLECTING_SAMPLE")
    @Basic
    var designationOfficerCollectingSample: String? = null

    @Column(name = "DATE_WITNESS")
    @Basic
    var dateWitness: Date? = null

    @Column(name = "DESIGNATION_WITNESS")
    @Basic
    var designationWitness: String? = null

    @Column(name = "NAME_WITNESS")
    @Basic
    var nameWitness: String? = null

    @Column(name = "REASONS_COLLECTING_SAMPLES")
    @Basic
    var reasonsCollectingSamples: String? = null

    @Column(name = "SAMPLING_METHOD")
    @Basic
    var samplingMethod: String? = null

    @Column(name = "STATUS")
    @Basic
    var status: Long? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as MsSampleCollectionView
        return productBrandName == that.productBrandName &&
                batchSize == that.batchSize &&
                batchNo == that.batchNo &&
                sampleSize == that.sampleSize &&
                sampleCollectionId == that.sampleCollectionId &&
                nameManufacturerTrader == that.nameManufacturerTrader &&
                addressManufactureTrader == that.addressManufactureTrader &&
                anyRemarks == that.anyRemarks &&
                nameOfficerCollectingSample == that.nameOfficerCollectingSample &&
                dateOfficerCollectingSample == that.dateOfficerCollectingSample &&
                designationOfficerCollectingSample == that.designationOfficerCollectingSample &&
                dateWitness == that.dateWitness &&
                designationWitness == that.designationWitness &&
                nameWitness == that.nameWitness &&
                reasonsCollectingSamples == that.reasonsCollectingSamples &&
                samplingMethod == that.samplingMethod &&
                status == that.status
    }

    override fun hashCode(): Int {
        return Objects.hash(productBrandName, batchSize, batchNo, sampleSize, sampleCollectionId, nameManufacturerTrader, addressManufactureTrader, anyRemarks, nameOfficerCollectingSample, dateOfficerCollectingSample, designationOfficerCollectingSample, dateWitness, designationWitness, nameWitness, reasonsCollectingSamples, samplingMethod, status)
    }
}
