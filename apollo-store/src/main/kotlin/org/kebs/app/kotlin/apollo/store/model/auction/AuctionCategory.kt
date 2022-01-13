package org.kebs.app.kotlin.apollo.store.model.auction

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_AUCTION_CATEGORIES")
class AuctionCategory : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_AUCTION_CATEGORIES_SEQ_GEN", sequenceName = "DAT_KEBS_AUCTION_CATEGORIES_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_AUCTION_CATEGORIES_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = null

    @Column(name = "CATEGORY_CODE", unique = true)
    @Basic
    var categoryCode: String? = null

    @Column(name = "CATEGORY_NAME")
    @Basic
    var categoryName: String? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null
}