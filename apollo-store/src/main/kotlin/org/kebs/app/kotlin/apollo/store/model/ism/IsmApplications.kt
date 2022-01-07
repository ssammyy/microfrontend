package org.kebs.app.kotlin.apollo.store.model.ism

import java.io.Serializable
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "ISM_APPLICATION_REQUESTS")
class IsmApplications: Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "ISM_APPLICATION_REQUESTS_SEQ_GEN", sequenceName = "ISM_APPLICATION_REQUESTS_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "ISM_APPLICATION_REQUESTS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long = 0

    @Column(name = "COC_ID")
    var cocId: Long? = 0

    @Column(name = "CONSIGNMENT_ID")
    var consignmentId: Long? = 0

    @Column(name = "UCR_NUMBER")
    @Basic
    var ucrNumber: String? = null

    @Column(name = "COMPANY_NAME")
    @Basic
    var companyName: String?=null

    @Column(name = "FIRST_NAME")
    @Basic
    var firstName: String? = null

    @Column(name = "MIDDLE_NAME")
    @Basic
    var middleName: String? = null

    @Column(name = "LAST_NAME")
    @Basic
    var lastName: String? = null

    @Column(name = "EMAIL_ADDRESS")
    @Basic
    var emailAddress: String? = null

    @Column(name = "REMARKS")
    @Basic
    var remarks: String? = null

    @Column(name = "APPROVAL_REMARKS")
    @Basic
    var approvalRemarks: String? = null

    @Column(name = "APPROVED_BY")
    @Basic
    var approvedBy: String?=null

    @Column(name = "APPROVED_REJECTED_ON")
    @Basic
    var approvedRejectedOn: Timestamp?=null

    @Column(name = "PROCESS_ID")
    @Basic
    var processId: String? = null

    @Column(name = "REQUEST_APPROVED")
    @Basic
    var requestApproved: Int? = null

    @Column(name = "REQUEST_COMPLETED")
    @Basic
    var completed: Boolean? = false

    @Column(name = "COMPLETED_ON")
    @Basic
    var completedOn: Timestamp? = null

    @Column(name = "STATUS")
    @Basic
    var status: Long? = null

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

}