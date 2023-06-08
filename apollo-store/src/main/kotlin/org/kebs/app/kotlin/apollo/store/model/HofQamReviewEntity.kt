package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_HOF_QAM_REVIEW")
class HofQamReviewEntity : Serializable {
    @Column(name = "ID")
    @Id
    @SequenceGenerator(name = "DAT_KEBS_HOF_QAM_REVIEW_SEQ_GEN", sequenceName = "DAT_KEBS_HOF_QAM_REVIEW_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_HOF_QAM_REVIEW_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long = 0

    @Column(name = "MANUFACTURER_ID", precision = 0)
    @Basic
    var manufacturerId: Long? = null

    @Column(name = "PERMIT_ID", precision = 0)
    @Basic
    var permitId: Long? = null
    
    @Column(name = "COMPLETENESS_STATUS")
    @Basic
    var completenessStatus: Int = 0

    @Column(name = "RESUBMIT_STATUS")
    @Basic
    var resubmitStatus: Int = 0

    @Column(name = "VISITING_USER_ID")
    @Basic
    var visitingUserId: Long? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int = 0

    @Column(name = "NOTIFICATION_LINK")
    @Basic
    var notificationLink: String? = null

    @Column(name = "REJECTED_REASON")
    @Basic
    var rejectedReason: String? = null

    @Column(name = "REJECTED_STATUS")
    @Basic
    var rejectedStatus: Int = 0

    @Column(name = "REVIEWED_ON")
    @Basic
    var reviewedOn: Timestamp? = null

    @Column(name = "DATE_RECEIVED_ON")
    @Basic
    var dateReceivedOn: Timestamp? = null

    @Column(name = "DATE_VISIT_END_ON")
    @Basic
    var dateVisitEndOn: Timestamp? = null

    @Column(name = "DATE_VISITED")
    @Basic
    var dateVisited: Timestamp? = null

    @Column(name = "VISIT_STATUS")
    @Basic
    var visitStatus: Int = 0

    @Column(name = "FEEDBACK")
    @Basic
    var feedback: String? = null

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

    @Column(name = "CREATED_BY")
    @Basic
    var createdBy: String? = null

    @Column(name = "CREATED_ON")
    @Basic
    var createdOn: Timestamp? = null

    @Column(name = "MODIFIED_BY")
    @Basic
    var modifiedBy: String? = null

    @Column(name = "MODIFIED_ON")
    @Basic
    var modifiedOn: Timestamp? = null

    @Column(name = "DELETE_BY")
    @Basic
    var deleteBy: String? = null

    @Column(name = "DELETED_ON")
    @Basic
    var deletedOn: Timestamp? = null

//    @JoinColumn(name = "PERMIT_ID", referencedColumnName = "ID")
//    @ManyToOne
//    var permitId: PermitApplicationEntity? = null


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as HofQamReviewEntity
        return id == that.id && completenessStatus == that.completenessStatus && status == that.status && rejectedStatus == that.rejectedStatus && visitStatus == that.visitStatus &&
                manufacturerId == that.manufacturerId && resubmitStatus == that.resubmitStatus &&
                permitId == that.permitId &&
                visitingUserId == that.visitingUserId &&
                notificationLink == that.notificationLink &&
                rejectedReason == that.rejectedReason &&
                reviewedOn == that.reviewedOn &&
                dateReceivedOn == that.dateReceivedOn &&
                dateVisitEndOn == that.dateVisitEndOn &&
                dateVisited == that.dateVisited &&
                feedback == that.feedback &&
                varField1 == that.varField1 &&
                varField2 == that.varField2 &&
                varField3 == that.varField3 &&
                varField4 == that.varField4 &&
                varField5 == that.varField5 &&
                varField6 == that.varField6 &&
                varField7 == that.varField7 &&
                varField8 == that.varField8 &&
                varField9 == that.varField9 &&
                varField10 == that.varField10 &&
                createdBy == that.createdBy &&
                createdOn == that.createdOn &&
                modifiedBy == that.modifiedBy &&
                modifiedOn == that.modifiedOn &&
                deleteBy == that.deleteBy &&
                deletedOn == that.deletedOn
    }

    override fun hashCode(): Int {
        return Objects.hash(id, manufacturerId, resubmitStatus, visitingUserId, permitId, completenessStatus, status, notificationLink, rejectedReason, rejectedStatus, reviewedOn, dateReceivedOn, dateVisitEndOn, dateVisited, visitStatus, feedback, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn)
    }
}