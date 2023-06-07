package org.kebs.app.kotlin.apollo.store.model.pvc

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "CFG_KEBS_PVOC_TIMELINE_CONFIGURATION")
class PvocTimelineConfigurations : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "CFG_KEBS_PVOC_TIMELINE_CONFIGURATION_SEQ_GEN", sequenceName = "CFG_KEBS_PVOC_TIMELINE_CONFIGURATION_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "CFG_KEBS_PVOC_TIMELINE_CONFIGURATION_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = null

    @Column(name = "DOCUMENT_TYPE", unique = true)
    @Basic
    var documentType: String? = null

    @Column(name = "CATEGORY_NAME")
    @Basic
    var categoryName: String? = null

    @Column(name = "RFC_TO_INSPECTION_MAX")
    @Basic
    var rfcToInspectionMax: Int = 0

    @Column(name = "RFC_TO_INSUANCE_MAX")
    @Basic
    var rfcToInsuanceMax: Int = 0

    @Column(name = "INSPECTION_TO_ISSUANCE_MAX")
    @Basic
    var inspectionToIssuanceMax: Int = 0

    @Column(name = "PAYMENT_TO_ISSUANCE_MAX")
    @Basic
    var paymentToIssuanceMax: Int = 0

    @Column(name = "ACCEPTABLE_DOC_TO_ISSUANCE_MAX")
    @Basic
    var acceptableDocToIssuanceMax: Int = 0

    @Column(name = "FINAL_DOC_TO_INSPECTION_MAX")
    @Basic
    var finalDocToInspectionMax: Int = 0

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null
}