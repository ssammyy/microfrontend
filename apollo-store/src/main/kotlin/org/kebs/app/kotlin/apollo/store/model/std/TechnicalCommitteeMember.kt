package org.kebs.app.kotlin.apollo.store.model.std

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "SD_TECHNICAL_COMMITTEE_MEMBER")
class TechnicalCommitteeMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id: Long = 0

    @Column(name = "USER_ID")
    @Basic
    var userId: Long = 0

    @Column(name = "TC_ID")
    @Basic
    var tcId: Long = 0

    @Column(name = "TC")
    @Basic
    var tc: String? = null

    @Column(name = "NAME")
    @Basic
    var name: String? = null

    @Column(name = "EMAIL")
    @Basic
    var email: String? = null

    @Column(name = "POSTAL_ADDRESS")
    @Basic
    var postalAddress: String? = null

    @Column(name = "MOBILE_NUMBER")
    @Basic
    var mobileNumber: String? = null

    @Column(name = "DATE_OF_CREATION")
    @Basic
    var dateOfCreation: Timestamp? = null

    @Column(name = "STATUS")
    @Basic
    var status: String? = null


    @Column(name = "VAR_FIELD_1")
    @Basic
    var varField1: String? = null


    @Column(name = "VAR_FIELD_2")
    @Basic
    var varField2: String? = null


    @Column(name = "VAR_FIELD_3")
    @Basic
    var varField3: String? = null

    @Column(name = "VAR_FIELD_4")
    @Basic
    var varField4: String? = null

    @Column(name = "VAR_FIELD_5")
    @Basic
    var varField5: String? = null

    @Column(name = "VAR_FIELD_6")
    @Basic
    var varField6: String? = null

    @Column(name = "VAR_FIELD_7")
    @Basic
    var varField7: String? = null

    @Column(name = "VAR_FIELD_8")
    @Basic
    var varField8: String? = null

    @Column(name = "VAR_FIELD_9")
    @Basic
    var varField9: String? = null

    @Column(name = "VAR_FIELD_10")
    @Basic
    var varField10: String? = null


    @Transient
    @JsonProperty("taskId")
    var taskId: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TechnicalCommitteeMember

        if (id != other.id) return false
        if (userId != other.userId) return false
        if (tcId != other.tcId) return false
        if (tc != other.tc) return false
        if (name != other.name) return false
        if (email != other.email) return false
        if (postalAddress != other.postalAddress) return false
        if (mobileNumber != other.mobileNumber) return false
        if (dateOfCreation != other.dateOfCreation) return false
        if (status != other.status) return false

        if (varField1 != other.varField1) return false
        if (varField2 != other.varField2) return false
        if (varField3 != other.varField3) return false
        if (varField4 != other.varField4) return false
        if (varField5 != other.varField5) return false
        if (varField6 != other.varField6) return false
        if (varField7 != other.varField7) return false
        if (varField8 != other.varField8) return false

        if (varField9 != other.varField9) return false
        if (varField10 != other.varField10) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (userId.hashCode() ?: 0)
        result = 31 * result + (tcId.hashCode() ?: 0)
        result = 31 * result + (tc?.hashCode() ?: 0)
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (postalAddress?.hashCode() ?: 0)
        result = 31 * result + (mobileNumber?.hashCode() ?: 0)
        result = 31 * result + (email?.hashCode() ?: 0)
        result = 31 * result + (dateOfCreation?.hashCode() ?: 0)
        result = 31 * result + (varField1?.hashCode() ?: 0)
        result = 31 * result + (varField2?.hashCode() ?: 0)
        result = 31 * result + (varField3?.hashCode() ?: 0)
        result = 31 * result + (varField4?.hashCode() ?: 0)
        result = 31 * result + (varField5?.hashCode() ?: 0)
        result = 31 * result + (varField6?.hashCode() ?: 0)
        result = 31 * result + (status?.hashCode() ?: 0)
        result = 31 * result + (varField7?.hashCode() ?: 0)
        result = 31 * result + (varField8?.hashCode() ?: 0)
        result = 31 * result + (varField9?.hashCode() ?: 0)
        result = 31 * result + (varField10?.hashCode() ?: 0)


        return result
    }

    override fun toString(): String {
        return "MembershipTCApplication(id=$id, userId=$userId, tcId=$tcId, tc=$tc, name=$name, postalAddress=$postalAddress, mobileNumber=$mobileNumber, email=$email, dateOfCreation=$dateOfCreation, varField1=$varField1, varField2=$varField2, varField3=$varField3, " +
                "varField4=$varField4,varField5=$varField5,varField6=$varField6,status=$status" +
                ",varField7=$varField7,varField8=$varField8," +
                "varField9=$varField9,varField10=$varField10)"
    }


}
