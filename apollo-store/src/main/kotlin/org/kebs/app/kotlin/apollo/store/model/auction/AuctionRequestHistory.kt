package org.kebs.app.kotlin.apollo.store.model.auction

import java.io.Serializable
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_AUCTION_REQUEST_HISTORY")
class AuctionRequestHistory : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_AUCTION_REQUEST_HISTORY_SEQ_GEN", sequenceName = "DAT_KEBS_AUCTION_REQUEST_HISTORY_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_AUCTION_REQUEST_HISTORY_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = null

    @Column(name = "AUCTION_ID")
    @Basic
    var auctionId: Long? = null

    @Column(name = "USERNAME", unique = true)
    @Basic
    var username: String? = null

    @Column(name = "ACTION_NAME", unique = true)
    @Basic
    var actionName: String? = null

    @Column(name = "ACTION_DESCRIPTION")
    @Basic
    var description: String? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null

    @Column(name = "CREATED_BY")
    @Basic
    var createdBy: String? = null

    @Column(name = "CREATED_ON")
    @Basic
    var createdOn: Timestamp? = null
}