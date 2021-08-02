package org.kebs.app.kotlin.apollo.store.model.std

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import javax.persistence.*
import kotlin.jvm.Transient

@Entity
@Table(name="SD_STANDARD_REQUEST")
class StandardRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id: Long=0

    @Column(name="REQUEST_NUMBER")
    @Basic
    var requestNumber:String?=null

    @Column(name="RANK")
    @Basic
    var rank:String?=null

    @Column(name="REQUESTOR_NAME")
    @Basic
    var name:String?=null

    @Column(name="REQUESTOR_PHONE_NUMBER")
    @Basic
    var phone:String?=null

    @Column(name="REQUESTOR_EMAIL")
    @Basic
    var email:String?=null


    @Column(name="SUBMISSION_DATE")
    @Basic
    var submissionDate: Timestamp?=null


    @Column(name="DEPARTMENT_ID")
    @Basic
    var departmentId:String?=null

    @Column(name="PRODUCT_SUB_CATEGORY_ID")
    @Basic
    var productSubCategoryId:String?=null

    @Column(name="TECHNICAL_COMMITTEE_ID")
    @Basic
    var tcId:String?=null

    @Column(name="PRODUCT_ID")
    @Basic
    var productId: String?=null

    @Transient
    @JsonProperty("tcName")
    var tcName: String?=null

    @Transient
    @JsonProperty("departmentName")
    var departmentName: String?=null

    @Transient
    @JsonProperty("productName")
    var productName: String?=null

    @Transient
    @JsonProperty("productSubCategoryName")
    var productSubCategoryName: String?=null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StandardRequest

        if (id != other.id) return false
        if (requestNumber != other.requestNumber) return false
        if (name != other.name) return false
        if (phone != other.phone) return false
        if (email != other.email) return false
        if (submissionDate != other.submissionDate) return false
        if (departmentId != other.departmentId) return false
        if (productSubCategoryId != other.productSubCategoryId) return false
        if (tcId != other.tcId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (requestNumber?.hashCode() ?: 0)
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (phone?.hashCode() ?: 0)
        result = 31 * result + (email?.hashCode() ?: 0)
        result = 31 * result + (submissionDate?.hashCode() ?: 0)
        result = 31 * result + (departmentId?.hashCode() ?: 0)
        result = 31 * result + (productSubCategoryId?.hashCode() ?: 0)
        result = 31 * result + (tcId?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "StandardRequest(id=$id, requestNumber=$requestNumber, name=$name, phone=$phone, email=$email, submission_date=$submissionDate, department_id=$departmentId, product_category=$productSubCategoryId, tc_id=$tcId)"
    }


}
