package org.kebs.app.kotlin.apollo.store.model.std

import com.fasterxml.jackson.annotation.JsonProperty
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
    var organization:String? = null

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
    var authorizingName:String? = null

    @Column(name="AUTHORISING_PERSON_POSITION")
    @Basic
    var authorisingPersonPosition:String? = null

    @Column(name="AUTHORISING_PERSON_EMAIL")
    @Basic
    var authorisingPersonEmail:String? = null

    @Column(name="QUALIFICATIONS")
    @Basic
    var qualifications:String? = null

    @Column(name="COMMITTMENT")
    @Basic
    var commitment:String? = null

    @Transient
    @JsonProperty("taskId")
    var taskId:String?=null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MembershipTCApplication

        if (id != other.id) return false
        if (technicalCommittee != other.technicalCommittee) return false
        if (organization != other.organization) return false
        if (nomineeName != other.nomineeName) return false
        if (position != other.position) return false
        if (postalAddress != other.postalAddress) return false
        if (mobileNumber != other.mobileNumber) return false
        if (email != other.email) return false
        if (authorizingName != other.authorizingName) return false
        if (authorisingPersonPosition != other.authorisingPersonPosition) return false
        if (authorisingPersonEmail != other.authorisingPersonEmail) return false
        if (qualifications != other.qualifications) return false
        if (commitment != other.commitment) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (technicalCommittee?.hashCode() ?: 0)
        result = 31 * result + (organization?.hashCode() ?: 0)
        result = 31 * result + (nomineeName?.hashCode() ?: 0)
        result = 31 * result + (position?.hashCode() ?: 0)
        result = 31 * result + (postalAddress?.hashCode() ?: 0)
        result = 31 * result + (mobileNumber?.hashCode() ?: 0)
        result = 31 * result + (email?.hashCode() ?: 0)
        result = 31 * result + (authorizingName?.hashCode() ?: 0)
        result = 31 * result + (authorisingPersonPosition?.hashCode() ?: 0)
        result = 31 * result + (authorisingPersonEmail?.hashCode() ?: 0)
        result = 31 * result + (qualifications?.hashCode() ?: 0)
        result = 31 * result + (commitment?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "MembershipTCApplication(id=$id, technicalCommittee=$technicalCommittee, organization=$organization, nomineeName=$nomineeName, position=$position, postalAddress=$postalAddress, mobileNumber=$mobileNumber, email=$email, authorizingName=$authorizingName, authorisingPersonPosition=$authorisingPersonPosition, authorisingPersonEmail=$authorisingPersonEmail, qualifications=$qualifications, commitment=$commitment)"
    }


}
