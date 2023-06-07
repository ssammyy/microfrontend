package org.kebs.app.kotlin.apollo.standardsdevelopment.models

import java.sql.Timestamp
import javax.persistence.*

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


    @Column(name="SUBMISSION_DATE")
    @Basic
    var submission_date: Timestamp?=null

    @ManyToOne
    var user: User?= null

    @ManyToOne
    var department: Department?= null

    @ManyToOne
    var productSubCategory: ProductSubCategory?= null

    @ManyToOne
    var technicalCommittee: TechnicalCommittee?= null
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StandardRequest

        if (id != other.id) return false
        if (requestNumber != other.requestNumber) return false
        if (submission_date != other.submission_date) return false
        if (user != other.user) return false
        if (department != other.department) return false
        if (productSubCategory != other.productSubCategory) return false
        if (technicalCommittee != other.technicalCommittee) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (requestNumber?.hashCode() ?: 0)
        result = 31 * result + (submission_date?.hashCode() ?: 0)
        result = 31 * result + (user?.hashCode() ?: 0)
        result = 31 * result + (department?.hashCode() ?: 0)
        result = 31 * result + (productSubCategory?.hashCode() ?: 0)
        result = 31 * result + (technicalCommittee?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "StandardRequestService(id=$id, requestNumber=$requestNumber, submission_date=$submission_date, user=$user, department=$department, productSubCategory=$productSubCategory, technicalCommittee=$technicalCommittee)"
    }


}
