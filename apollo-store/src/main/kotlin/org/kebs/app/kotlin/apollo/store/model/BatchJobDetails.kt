package org.kebs.app.kotlin.apollo.store.model

import org.hibernate.annotations.DynamicUpdate
import java.io.Serializable
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "CFG_BATCH_JOB_DETAILS")
@DynamicUpdate
class BatchJobDetails : Serializable {
    @Column(name = "ID")
    @Id
    @SequenceGenerator(name = "CFG_BATCH_JOB_DETAILS_SEQ_GEN", sequenceName = "CFG_BATCH_JOB_DETAILS_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "CFG_BATCH_JOB_DETAILS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long? = null

    @Column(name = "PAGE_SIZE")
    @Basic
    var pageSize: Int = 20

    @Column(name = "INITIAL_PAGE")
    @Basic
    var initialPage: Int = 1

    @Column(name = "JOB_URI")
    @Basic
    var jobUri: String? = null

    @Column(name = "JOB_TYPE_ID")
    @Basic
    var jobTypeId: Long? = null

    @Column(name = "INTEGRATION_ID")
    @Basic
    var integrationId: Long? = null

    @Column(name = "START_STATUS")
    @Basic
    var startStatus: Int? = null

    @Column(name = "END_SUCCESS_STATUS")
    @Basic
    var endSuccessStatus: Int? = null

    @Column(name = "END_PENDING_STATUS")
    @Basic
    var endPendingStatus: Int? = null


    @Column(name = "END_FAILURE_STATUS")
    @Basic
    var endFailureStatus: Int? = null

    @Column(name = "END_EXCEPTION_STATUS")
    @Basic
    var endExceptionStatus: Int? = null


    @Column(name = "RETRIES")
    @Basic
    var retries: Int? = null


    @Column(name = "RETRIED")
    @Basic
    var retried: Int? = null


    @Column(name = "PROCESSING_ACTOR_BEAN")
    @Basic
    var processingActorBean: String? = null

    @Column(name = "STANDARD_DATE_FORMAT")
    @Basic
    var standardDateFormat: String = "dd/MM/yyyy"

    @Column(name = "DESCRIPTIONS")
    @Basic
    var descriptions: String? = null

    @Column(name = "DESCRIPTION")
    @Basic
    var description: String? = null


    @Column(name = "STATUS")
    @Basic
    var status: Int? = null


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

    @Column(name = "LAST_MODIFIED_BY")
    @Basic
    var modifiedBy: String? = null

    @Column(name = "LAST_MODIFIED_ON")
    @Basic
    var modifiedOn: Timestamp? = null

    @Column(name = "DELETE_BY")
    @Basic
    var deleteBy: String? = null

    @Column(name = "DELETED_ON")
    @Basic
    var deletedOn: Timestamp? = null


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BatchJobDetails

        if (id != other.id) return false
        if (jobTypeId != other.jobTypeId) return false
        if (integrationId != other.integrationId) return false
        if (startStatus != other.startStatus) return false
        if (endSuccessStatus != other.endSuccessStatus) return false
        if (endPendingStatus != other.endPendingStatus) return false
        if (endFailureStatus != other.endFailureStatus) return false
        if (endExceptionStatus != other.endExceptionStatus) return false
        if (retries != other.retries) return false
        if (retried != other.retried) return false
        if (processingActorBean != other.processingActorBean) return false
        if (descriptions != other.descriptions) return false
        if (description != other.description) return false
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
        if (createdBy != other.createdBy) return false
        if (createdOn != other.createdOn) return false
        if (modifiedBy != other.modifiedBy) return false
        if (modifiedOn != other.modifiedOn) return false
        if (deleteBy != other.deleteBy) return false
        if (deletedOn != other.deletedOn) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (jobTypeId?.hashCode() ?: 0)
        result = 31 * result + (integrationId?.hashCode() ?: 0)
        result = 31 * result + (startStatus ?: 0)
        result = 31 * result + (endSuccessStatus ?: 0)
        result = 31 * result + (endPendingStatus ?: 0)
        result = 31 * result + (endFailureStatus ?: 0)
        result = 31 * result + (endExceptionStatus ?: 0)
        result = 31 * result + (retries ?: 0)
        result = 31 * result + (retried ?: 0)
        result = 31 * result + (processingActorBean?.hashCode() ?: 0)
        result = 31 * result + (descriptions?.hashCode() ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + (status ?: 0)
        result = 31 * result + (varField1?.hashCode() ?: 0)
        result = 31 * result + (varField2?.hashCode() ?: 0)
        result = 31 * result + (varField3?.hashCode() ?: 0)
        result = 31 * result + (varField4?.hashCode() ?: 0)
        result = 31 * result + (varField5?.hashCode() ?: 0)
        result = 31 * result + (varField6?.hashCode() ?: 0)
        result = 31 * result + (varField7?.hashCode() ?: 0)
        result = 31 * result + (varField8?.hashCode() ?: 0)
        result = 31 * result + (varField9?.hashCode() ?: 0)
        result = 31 * result + (varField10?.hashCode() ?: 0)
        result = 31 * result + (createdBy?.hashCode() ?: 0)
        result = 31 * result + (createdOn?.hashCode() ?: 0)
        result = 31 * result + (modifiedBy?.hashCode() ?: 0)
        result = 31 * result + (modifiedOn?.hashCode() ?: 0)
        result = 31 * result + (deleteBy?.hashCode() ?: 0)
        result = 31 * result + (deletedOn?.hashCode() ?: 0)
        return result
    }


}
