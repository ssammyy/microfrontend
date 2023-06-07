package org.kebs.app.kotlin.apollo.store.model.std

import java.sql.Timestamp
import javax.persistence.*
import kotlin.jvm.Transient

@Entity
@Table(name = "SD_NEP_WTO_NOTIFICATION")
class NEPWtoNotification {
    @Column(name = "ID")
    @SequenceGenerator(
        name = "SD_NEP_WTO_NOTIFICATION_SEQ_GEN",
        allocationSize = 1,
        sequenceName = "SD_NEP_WTO_NOTIFICATION_SEQ"
    )
    @GeneratedValue(generator = "SD_NEP_WTO_NOTIFICATION_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = 0

    @Column(name = "NOTIFICATION")
    @Basic
    var notification: String? = null


    @Column(name = "DRAFT_ID")
    @Basic
    var draftId: Long? = null

    @Column(name = "PREPARED_BY")
    @Basic
    var preparedBy: Long? = null

    @Column(name = "DATE_UPLOADED")
    @Basic
    var dateUploaded: Timestamp? = null

    @Column(name = "STATUS")
    @Basic
    var status: Long? = null

}
