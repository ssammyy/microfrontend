package org.kebs.app.kotlin.apollo.store.model.std

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name="SD_NEP_DRAFT")
class SdNepDraft {
    @Column(name = "ID")
    @SequenceGenerator(
        name = "SD_NEP_DRAFT_SEQ_GEN",
        allocationSize = 1,
        sequenceName = "SD_NEP_DRAFT_SEQ"
    )
    @GeneratedValue(generator = "SD_NEP_DRAFT_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = 0

    @Column(name="TITLE")
    @Basic
    var title:String? =null

    @Column(name="SCOPE")
    @Basic
    var scope:String? =null

    @Column(name="NORMATIVE_REFERENCE")
    @Basic
    var normativeReference:String? =null

    @Column(name="SYMBOLS_ABBREVIATED_TERMS")
    @Basic
    var symbolsAbbreviatedTerms:String? =null

    @Column(name="CLAUSE")
    @Basic
    var clause:String? =null

    @Column(name="SPECIAL")
    @Basic
    var special:String? =null

    @Column(name="PREPARED_BY")
    @Basic
    var preparedBy:Long? =null


    @Column(name = "STATUS")
    @Basic
    var status: Long? = null

    @Column(name = "DATE_PD_PREPARED")
    @Basic
    var datePrepared: Timestamp? = null

    @Column(name="NOTIFICATION")
    @Basic
    var notification:String? =null

    @Column(name="TYPE_NOTIFICATION")
    @Basic
    var typeOfNotification:String? =null

    @Column(name = "UPLOAD_DOCUMENT")
    @Basic
    var uploadDocument: Long? = null



}

