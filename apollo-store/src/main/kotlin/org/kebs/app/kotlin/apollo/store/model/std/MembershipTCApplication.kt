package org.kebs.app.kotlin.apollo.store.model.std

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name="SD_MEMBERSHIP_TO_TC")
class MembershipTCApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    var id:Long =0

    @Column(name="TECHNICAL_COMMITTEE")
    @Basic
    var technicalCommittee:String? = null

    @Column(name="ORGANIZATION")
    @Basic
    var organisationName:String? = null

    @Column(name="NOMINEE_NAME")
    @Basic
    var nomineeName:String? = null

    @Column(name="POSITION")
    @Basic
    var position:String? = null

    @Column(name="POSTAL_ADDRESS")
    @Basic
    var postalAddress:String? = null

    @Column(name="MOBILE_NUMBER")
    @Basic
    var mobileNumber:String? = null

    @Column(name="EMAIL")
    @Basic
    var email:String? = null

    @Column(name="AUTHORISING_PERSON_NAME")
    @Basic
    var authorizingPerson:String? = null

    @Column(name="AUTHORISING_PERSON_POSITION")
    @Basic
    var authorizingPersonPosition:String? = null

    @Column(name = "AUTHORISING_PERSON_EMAIL")
    @Basic
    var authorizingPersonEmail: String? = null

    @Column(name = "QUALIFICATIONS")
    @Basic
    var qualification: String? = null

    @Column(name = "COMMITTMENT")
    @Basic
    var commitment: String? = null


    @Column(name = "TC_APPLICATION_ID")
    @Basic
    var tcId: Long? = null

    @Column(name = "DATE_OF_APPLICATION")
    @Basic
    var dateOfApplication: Timestamp? = null

    @Column(name = "STATUS")
    @Basic
    var status: String? = null

    @Column(name = "COMMENTS_BY_HOF")
    @Basic
    var comments_by_hof: String? = null

    @Column(name = "COMMENTS_BY_SPC")
    @Basic
    var commentsBySpc: String? = null

    @Column(name = "COMMENTS_BY_SAC")
    @Basic
    var commentsBySac: String? = null

    @Column(name = "HOF_ID")
    @Basic
    var hofId: String? = null

    @Column(name = "SPC_ID")
    @Basic
    var spcId: String? = null

    @Column(name = "SAC_ID")
    @Basic
    var sacId: String? = null

    @Column(name = "VAR_FIELD_9")
    @Basic
    var varField9: String? = null

    @Column(name = "VAR_FIELD_10")
    @Basic
    var varField10: String? = null

    @Column(name = "APPROVED_BY_ORGANISATION")
    @Basic
    var approvedByOrganization: String? = null


    @Transient
    @JsonProperty("taskId")
    var taskId: String? = null


}
