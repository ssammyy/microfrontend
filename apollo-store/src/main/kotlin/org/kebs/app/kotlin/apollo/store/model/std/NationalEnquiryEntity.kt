package org.kebs.app.kotlin.apollo.store.model.std

import java.sql.Timestamp
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "SD_NEP_EQUIRY")
class NationalEnquiryEntity {
    @Column(name = "ID")
    @SequenceGenerator(
        name = "SD_NEP_EQUIRY_SEQ_GEN",
        allocationSize = 1,
        sequenceName = "SD_NEP_EQUIRY_SEQ"
    )
    @GeneratedValue(generator = "SD_NEP_EQUIRY_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = 0


    @Column(name = "ENQUIRING_ID")
    @Basic
    var requesterid : Long? = null

    @Column(name = "ENQUIRER_NAME")
    @Basic
    var requesterName : String? = null

    @Column(name = "EMAIL")
    @Basic
    var requesterEmail : String ? = null

    @Column(name = "PHONE")
    @Basic
    var requesterPhone : String ? = null

    @Column(name = "INSTITUTION")
    @Basic
    var requesterInstitution : String ? = null

    @Column(name = "COUNTRY")
    @Basic
    var requesterCountry : String ? = null

    @Column(name = "ENQUIRER_SUBJECT")
    @Basic
    var requesterSubject : String ? = null

    @Column(name = "ENQUIRER_COMMENT")
    @Basic
    var requesterComment : String? = null

    @Column(name = "FEED_BACK")
    @Basic
    var requesterFeedBack : String? = null

    @Column(name = "REQUEST_DATE")
    @Basic
    var requestDate: Timestamp?=null

    @Column(name = "STATUS")
    @Basic
    var status : Long? =0

    @Column(name = "DOCUMENT_UPLOAD_STATUS")
    @Basic
    var docUploadStatus : Long? =0


}
