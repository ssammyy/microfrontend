package org.kebs.app.kotlin.apollo.store.model.std

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import javax.persistence.*
import kotlin.jvm.Transient

@Entity
@Table(name = "SD_STANDARD_REQUEST")
class StandardRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id: Long = 0

    @Column(name = "REQUEST_NUMBER")
    @Basic
    var requestNumber: String? = null

    @Column(name = "RANK")
    @Basic
    var rank: String? = null

    @Column(name = "REQUESTOR_NAME")
    @Basic
    var name: String? = null

    @Column(name = "REQUESTOR_PHONE_NUMBER")
    @Basic
    var phone: String? = null

    @Column(name = "REQUESTOR_EMAIL")
    @Basic
    var email: String? = null


    @Column(name = "SUBMISSION_DATE")
    @Basic
    var submissionDate: Timestamp? = null


    @Column(name = "DEPARTMENT_ID")
    @Basic
    var departmentId: String? = null

    @Column(name = "PRODUCT_SUB_CATEGORY_ID")
    @Basic
    var productSubCategoryId: String? = null

    @Column(name = "TECHNICAL_COMMITTEE_ID")
    @Basic
    var tcId: String? = null

    @Column(name = "PRODUCT_ID")
    @Basic
    var productId: String? = null

    @Column(name = "ORGANISATION_NAME")
    @Basic
    var organisationName: String? = null

    @Column(name = "SUBJECT")
    @Basic
    var subject: String? = null

    @Column(name = "DESCRIPTION")
    @Basic
    var description: String? = null

    @Column(name = "ECONOMIC_EFFICIENCY")
    @Basic
    var economicEfficiency: String? = null

    @Column(name = "HEALTH_SAFETY")
    @Basic
    var healthSafety: String? = null

    @Column(name = "ENVIRONMENT")
    @Basic
    var environment: String? = null

    @Column(name = "INTEGRATION")
    @Basic
    var integration: String? = null

    @Column(name = "EXPORT_MARKETS")
    @Basic
    var exportMarkets: String? = null

    @Column(name = "LEVEL_OF_STANDARD")
    @Basic
    var levelOfStandard: String? = null

    @Column(name = "STATUS")
    @Basic
    var status: String? = null


    @Transient
    @JsonProperty("departmentName")
    var departmentName: String? = null

    @Column(name = "CREATED_BY")
    @Basic
    var createdBy: String? = null

    @Column(name = "CREATED_ON")
    @Basic
    var createdOn: Timestamp? = null

    @Column(name = "MODIFIED_ON")
    @Basic
    var modifiedOn: Timestamp? = null

    @Column(name = "DELETED_ON")
    @Basic
    var deletedOn: Timestamp? = null




    @Column(name = "MODIFIED_BY")
    @Basic
    var modifiedBy: String? = null

    @Column(name = "DELETE_BY")
    @Basic
    var deleteBy: String? = null

    @Transient
    @JsonProperty("taskId")
    var taskId: String? = null

    @Column(name = "PROCESS")
    @Basic
    var process: String? = null

    @Column(name = "TC_SEC_ASSIGNED")
    @Basic
    var tcSecAssigned: String? = null

    @Column(name = "NWI_STATUS")
    @Basic
    var nwiStatus: String? = null

    @Column(name = "NWA_STD_NUMBER")
    @Basic
    var nwaStdNumber: String? = null

    @Column(name = "NWA_CD_NUMBER")
    @Basic
    var nwaCdNumber: String? = null

}
