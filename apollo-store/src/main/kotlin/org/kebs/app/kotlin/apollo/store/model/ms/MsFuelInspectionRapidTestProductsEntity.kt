package org.kebs.app.kotlin.apollo.store.model.ms

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_MS_FUEL_INSPECTION_RAPID_TEST_PRODUCTS", schema = "APOLLO", catalog = "")
class MsFuelInspectionRapidTestProductsEntity : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_MS_FUEL_INSPECTION_RAPID_TEST_PRODUCTS_SEQ_GEN", allocationSize = 1, sequenceName = "DAT_KEBS_MS_FUEL_INSPECTION_RAPID_TEST_PRODUCTS_SEQ")
    @GeneratedValue(generator = "DAT_KEBS_MS_FUEL_INSPECTION_RAPID_TEST_PRODUCTS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long= 0

    @Basic
    @Column(name = "PRODUCT_NAME")
    var productName: String? = null

    @Basic
    @Column(name = "EXPORT_MARKER_TEST")
    var exportMarkerTest: String? = null

    @Basic
    @Column(name = "SAMPLE_SIZE")
    var sampleSize: String? = null

    @Basic
    @Column(name = "BATCH_SIZE")
    var batchSize: String? = null

    @Basic
    @Column(name = "BATCH_NUMBER")
    var batchNumber: String? = null

    @Basic
    @Column(name = "DOMESTIC_KEROSENE_MARKER_TEST")
    var domesticKeroseneMarkerTest: String? = null

    @Basic
    @Column(name = "SULPHUR_MARKER_TEST")
    var sulphurMarkerTest: String? = null


    @Basic
    @Column(name = "STATUS")
    var status: Int? = null

    @Basic
    @Column(name = "EXPORT_MARKER_TEST_STATUS")
    var exportMarkerTestStatus: Int? = null

    @Basic
    @Column(name = "DOMESTIC_KEROSENE_MARKER_TEST_STATUS")
    var domesticKeroseneMarkerTestStatus: Int? = null

    @Basic
    @Column(name = "SULPHUR_MARKER_TEST_STATUS")
    var sulphurMarkerTestStatus: Int? = null

    @Basic
    @Column(name = "OVERALL_COMPLIANCE_STATUS")
    var overallComplianceStatus: Int? = null

    @Basic
    @Column(name = "FUEL_INSPECTION_ID")
    var fuelInspectionId: Long? = null

    @Basic
    @Column(name = "DESCRIPTION")
    var description: String? = null

    @Basic
    @Column(name = "VAR_FIELD_1")
    var varField1: String? = null

    @Basic
    @Column(name = "VAR_FIELD_2")
    var varField2: String? = null

    @Basic
    @Column(name = "VAR_FIELD_3")
    var varField3: String? = null

    @Basic
    @Column(name = "VAR_FIELD_4")
    var varField4: String? = null

    @Basic
    @Column(name = "VAR_FIELD_5")
    var varField5: String? = null

    @Basic
    @Column(name = "VAR_FIELD_6")
    var varField6: String? = null

    @Basic
    @Column(name = "VAR_FIELD_7")
    var varField7: String? = null

    @Basic
    @Column(name = "VAR_FIELD_8")
    var varField8: String? = null

    @Basic
    @Column(name = "VAR_FIELD_9")
    var varField9: String? = null

    @Basic
    @Column(name = "VAR_FIELD_10")
    var varField10: String? = null

    @Basic
    @Column(name = "CREATED_BY")
    var createdBy: String? = null

    @Basic
    @Column(name = "CREATED_ON")
    var createdOn: Timestamp? = null

    @Basic
    @Column(name = "MODIFIED_BY")
    var modifiedBy: String? = null

    @Basic
    @Column(name = "MODIFIED_ON")
    var modifiedOn: Timestamp? = null

    @Basic
    @Column(name = "DELETE_BY")
    var deleteBy: String? = null

    @Basic
    @Column(name = "DELETED_ON")
    var deletedOn: Timestamp? = null
}
