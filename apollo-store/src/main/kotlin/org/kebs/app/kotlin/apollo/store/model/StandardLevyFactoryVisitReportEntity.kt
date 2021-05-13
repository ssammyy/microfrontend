package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Timestamp
import java.time.LocalDate
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_STANDARD_LEVY_FACTORY_VISIT_REPORT")
class StandardLevyFactoryVisitReportEntity : Serializable {
    @Id
    @SequenceGenerator(name = "DAT_STANDARD_LEVY_FACTORY_VISIT_REPORT_SEQ_GEN", sequenceName = "DAT_STANDARD_LEVY_FACTORY_VISIT_REPORT_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_STANDARD_LEVY_FACTORY_VISIT_REPORT_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    var id: Long? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null

    @Column(name = "ASSISTANT_MANAGER_APPROVAL")
    @Basic
    var assistantManagerApproval: Int? = null

    @Column(name = "MANAGERS_APPROVAL")
    @Basic
    var managersApproval: Int? = null

    @Column(name = "PURPOSE")
    @Basic
    var purpose: String? = null

    @Column(name = "PERSON_MET")
    @Basic
    var personMet: String? = null

    @Column(name = "ACTION_TAKEN")
    @Basic
    var actionTaken: String? = null

    @Column(name = "REMARKS")
    @Basic
    var remarks: String? = null

    @Column(name = "OFFICERS_FEEDBACK")
    @Basic
    var officersFeedback: String? = null

    @Column(name = "MANUFACTURER_ENTITY")
    @Basic
    var manufacturerEntity: Long? = null

    @Column(name = "SCHEDULED_VISIT_DATE")
    @Basic
    var scheduledVisitDate: Date? = null

    @Column(name = "REPORT_DATE")
    @Basic
    var reportDate: LocalDate? = null

    @Column(name = "SL_STATUS")
    @Basic
    var slStatus: Int? = null

    @Column(name = "SL_STARTED_ON")
    @Basic
    var slStartedOn: Timestamp? = null

    @Column(name = "SL_COMPLETED_ON")
    @Basic
    var slCompletedOn: Timestamp? = null

    @Column(name = "SL_PROCESS_INSTANCE_ID")
    @Basic
    var slProcessInstanceId: String? = null

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
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as StandardLevyFactoryVisitReportEntity
        return id == that.id && status == that.status && assistantManagerApproval == that.assistantManagerApproval && managersApproval == that.managersApproval && purpose == that.purpose && personMet == that.personMet && actionTaken == that.actionTaken && remarks == that.remarks && officersFeedback == that.officersFeedback && varField1 == that.varField1 && varField2 == that.varField2 && varField3 == that.varField3 && varField4 == that.varField4 && varField5 == that.varField5 && varField6 == that.varField6 && varField7 == that.varField7 && varField8 == that.varField8 && varField9 == that.varField9 && varField10 == that.varField10 && createdBy == that.createdBy && createdOn == that.createdOn && modifiedBy == that.modifiedBy && modifiedOn == that.modifiedOn && deleteBy == that.deleteBy && deletedOn == that.deletedOn
    }

    override fun hashCode(): Int {
        return Objects.hash(
            id,
            status,
            assistantManagerApproval,
            managersApproval,
            purpose,
            personMet,
            actionTaken,
            remarks,
            officersFeedback,
            varField1,
            varField2,
            varField3,
            varField4,
            varField5,
            varField6,
            varField7,
            varField8,
            varField9,
            varField10,
            createdBy,
            createdOn,
            modifiedBy,
            modifiedOn,
            deleteBy,
            deletedOn
        )
    }
}
