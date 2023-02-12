package org.kebs.app.kotlin.apollo.store.model.qa

import java.io.Serializable
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_QA_INSPECTION_PRODUCT_LABELLING")
class QaInspectionProductLabelEntity : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(
        name = "DAT_KEBS_QA_INSPECTION_PRODUCT_LABELLING_SEQ_GEN",
        allocationSize = 1,
        sequenceName = "DAT_KEBS_QA_INSPECTION_PRODUCT_LABELLING_SEQ"
    )
    @GeneratedValue(generator = "DAT_KEBS_QA_INSPECTION_PRODUCT_LABELLING_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = null

    @Column(name = "PERMIT_ID")
    @Basic
    var permitId: Long? = null

    @Column(name = "INSPECTION_RECOMMENDATION_ID")
    @Basic
    var inspectionRecommendationId: Long? = null

    @Column(name = "PERMIT_REF_NUMBER")
    @Basic
    var permitRefNumber: String? = null

    @Column(name = "STANDARD_MARKING")
    @Basic
    var standardMarking: String? = null

    @Column(name = "FINDINGS")
    @Basic
    var findings: String? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null

    @Column(name = "TECHNICAL_INSPECTION_ID")
    @Basic
    var technicalInspectionId: String? = null

    @Column(name = "STATUS_OF_COMPLIANCE")
    @Basic
    var statusOfCompliance: String? = null

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
}
