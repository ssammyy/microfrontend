package org.kebs.app.kotlin.apollo.store.model.pvc

import java.io.Serializable
import java.math.BigDecimal
import javax.persistence.*

@Entity
@Table(name = "CFG_KEBS_PVOC_TIMELINE_PENALTIES")
class PvocTimelinePenalties : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "CFG_KEBS_PVOC_TIMELINE_PENALTIES_SEQ_GEN", sequenceName = "CFG_KEBS_PVOC_TIMELINE_PENALTIES_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "CFG_KEBS_PVOC_TIMELINE_PENALTIES_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = null

    @JoinColumn(name = "PARTNER_TYPE", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    var partnerType: PvocPartnerTypeEntity? = null

    @Column(name = "ROUTE")
    @Basic
    var route: String? = null

    @Column(name = "PENALTY_TYPE")
    @Basic
    var penaltyType: String? = null

    @Column(name = "APPLICABLE_PENALTY", precision = 15, scale = 4)
    @Basic
    var applicablePenalty: BigDecimal? = null

    @Column(name = "FIRST_PENALTY", precision = 15, scale = 4)
    @Basic
    var firstPenalty: BigDecimal? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null
}