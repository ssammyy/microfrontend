package org.kebs.app.kotlin.apollo.store.model.std

import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "SD_NEP_NOTIFICATION")
class NationalEnquiryPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    @Basic
    var id : Long? = 0

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

    @Column(name = "REQUEST_DATE")
    @Basic
    var requestDate: LocalDate = LocalDate.now()

    @Column(name = "STATUS")
    @Basic
    var status : Long? =0
}
