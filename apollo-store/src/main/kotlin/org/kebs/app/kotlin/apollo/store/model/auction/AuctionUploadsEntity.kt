package org.kebs.app.kotlin.apollo.store.model.auction

import java.io.Serializable
import java.sql.Date
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_AUCTION_UPLOADS")
class AuctionUploadsEntity : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_AUCTION_UPLOADS_SEQ_GEN", sequenceName = "DAT_KEBS_AUCTION_UPLOADS_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_AUCTION_UPLOADS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long = 0

    @JoinColumn(name = "AUCTION_ID", referencedColumnName = "ID")
    @ManyToOne
    var auctionId: AuctionRequests? = null

    @Column(name = "FILEPATH")
    @Basic
    var filepath: String? = null

    @Column(name = "DESCRIPTION")
    @Basic
    var description: String? = null

    @Column(name = "NAME")
    @Basic
    var name: String? = null

    @Column(name = "FILE_TYPE")
    @Basic
    var fileType: String? = null

    @Column(name = "DOCUMENT_TYPE")
    @Basic
    var documentType: String? = null

    @Column(name = "DOCUMENT")
    @Lob
    var document: ByteArray? = null

    @Column(name = "TRANSACTION_DATE")
    @Basic
    var transactionDate: Date? = null

    @Column(name = "FILE_SIZE")
    @Basic
    var fileSize: Long? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null
}