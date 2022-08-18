package org.kebs.app.kotlin.apollo.store.model.std

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import javax.persistence.*
import kotlin.jvm.Transient

@Entity
@Table(name="SD_COM_STANDARD_REQUEST")
class CompanyStandardRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id: Long=0

    @Column(name="REQUEST_NUMBER")
    @Basic
    var requestNumber:String?=null


    @Column(name="SUBMISSION_DATE")
    @Basic
    var submissionDate: Timestamp?=null

    @Column(name="COMPANY_NAME")
    @Basic
    var companyName: String?=null

    @Column(name="DEPARTMENT")
    @Basic
    var departmentId: String?=null

    @Column(name="PRODUCT")
    @Basic
    var productId: String?=null

    @Column(name="TC_ID")
    @Basic
    var tcId: String?=null

    @Column(name="PRODUCT_SUB_CATEGORY")
    @Basic
    var productSubCategoryId: String?=null

    @Column(name="COMPANY_PHONE")
    @Basic
    var companyPhone: String?=null

    @Column(name="COMPANY_EMAIL")
    @Basic
    var companyEmail: String?=null

    @Transient
    @JsonProperty("DEPARTMENT_NAME")
    var departmentName: String?=null

    @Transient
    @JsonProperty("PRODUCT_NAME")
    var productName: String?=null

    @Transient
    @JsonProperty("PRODUCT_SUB_CATEGORY_NAME")
    var productSubCategoryName: String?=null

    @Transient
    @JsonProperty("TC_NAME")
    var tcName: String?=null

    //@Transient
    @Column(name = "TASKID")
    @Basic
    var taskId: String? = null

    @Column(name = "ASSIGNED_TO")
    @Basic
    var assignedTo: Long? = null


}
