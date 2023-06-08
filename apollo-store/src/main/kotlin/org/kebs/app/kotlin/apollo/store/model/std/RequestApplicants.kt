package org.kebs.app.kotlin.apollo.store.model.std

import javax.persistence.*

@Entity
@Table(name = "SD_REQUEST_APPLICANTS")
class RequestApplicants {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="REQUESTOR_ID")
    var id: Long =0

    @Column(name="NAME")
    @Basic
    var name: String?= null

    @Column(name="PHONENUMBER")
    @Basic
    var phoneNumber:String?=null

    @Column(name="EMAIL")
    @Basic
    var email:String?= null

    @OneToMany
    @JoinColumn(name="REQUESTOR_ID")
    var standardRequest:Set<StandardRequest>?=null
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RequestApplicants

        if (id != other.id) return false
        if (name != other.name) return false
        if (phoneNumber != other.phoneNumber) return false
        if (email != other.email) return false
        if (standardRequest != other.standardRequest) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (phoneNumber?.hashCode() ?: 0)
        result = 31 * result + (email?.hashCode() ?: 0)
        result = 31 * result + (standardRequest?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "RequestApplicants(id=$id, name=$name, phoneNumber=$phoneNumber, email=$email, standardRequest=$standardRequest)"
    }


}
