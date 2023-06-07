package org.kebs.app.kotlin.apollo.store.model.pvc

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_PVOC_PRODUCT_CATEGORIZATION_ISSUES")
class PvocProductCategorizationIssuessEntity : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(
        name = "DAT_KEBS_PVOC_PRODUCT_CATEGORIZATION_ISSUES_SEQ_GEN",
        sequenceName = "DAT_KEBS_PVOC_PRODUCT_CATEGORIZATION_ISSUES_SEQ",
        allocationSize = 1
    )
    @GeneratedValue(
        generator = "DAT_KEBS_PVOC_PRODUCT_CATEGORIZATION_ISSUES_SEQ_GEN",
        strategy = GenerationType.SEQUENCE
    )
    @Id
    var id: Long? = null

    @Column(name = "ROUTE")
    @Basic
    var route: String? = null

    @Column(name = "PARTNER_CATEGORY")
    @Basic
    var penaltyCategory: String? = null

    @Column(name = "EXPECTED_CATEGORY")
    @Basic
    var expectedCategory: String? = null

    @Column(name = "PARTNER_STANDARD")
    @Basic
    var partnerStandardApplied: String? = null

    @Column(name = "EXPECTED_STANDARD")
    @Basic
    var expectedStandard: String? = null

    @Column(name = "MONITORING_ID", nullable = false)
    @Basic
    var monitoringId: Long? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null
}