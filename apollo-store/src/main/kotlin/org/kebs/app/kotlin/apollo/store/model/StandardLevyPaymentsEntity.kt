package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.math.BigDecimal
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_STG_STANDARD_LEVY_PAYMENTS")
class StandardLevyPaymentsEntity: Serializable {
    @Column(name = "ID")
    @Id
    @SequenceGenerator(name = "DAT_KEBS_STG_STANDARD_LEVY_PAYMENTS_SEQ_GEN", sequenceName = "DAT_KEBS_STG_STANDARD_LEVY_PAYMENTS_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_STG_STANDARD_LEVY_PAYMENTS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long = 0

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null

    @JoinColumn(name = "MANUFACTURER_ENTITY", referencedColumnName = "ID")
    @ManyToOne
    var manufacturerEntity: ManufacturersEntity? = null

    @Column(name = "PAYMENT_DATE")
    @Basic
    var paymentDate: String? = null

    @Column(name = "PAYMENT_AMOUNT")
    @Basic
    var paymentAmount: BigDecimal? = null

    @Column(name = "VISIT_STATUS")
    @Basic
    var visitStatus: Long? = null

    @JoinColumn(name = "OFFICER_ASSIGNED", referencedColumnName = "ID")
    @ManyToOne
    var officerAssigned: UsersEntity? = null

    @Column(name = "LEVY_PAID")
    @Basic
    var levyPaid: BigDecimal? = null

    @Column(name = "LEVY_PAYABLE")
    @Basic
    var levyPayable: BigDecimal? = null

    @Column(name = "LEVY_PENALTIES")
    @Basic
    var levyPenalties: BigDecimal? = null

    @Column(name = "LEVY_PENALTY_PAYMENT_DATE")
    @Basic
    var levyPenaltyPaymentDate: String? = null

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

    @Column(name = "SL_REGISTRATION_STATUS")
    @Basic
    var slRegistrationStatus: Int? = null

    @Column(name = "SL_REGISTRATION_STARTED_ON")
    @Basic
    var slRegistrationStartedOn: Timestamp? = null

    @Column(name = "SL_REGISTRATION_COMPLETED_ON")
    @Basic
    var slRegistrationCompletedOn: Timestamp? = null

    @Column(name = "SL_REGISTRATION_PROCESS_INSTANCE_ID")
    @Basic
    var slRegistrationProcessInstanceId: String? = null

    @Column(name = "SL_SITE_VISIT_STATUS")
    @Basic
    var slSiteVisitStatus: Int? = null

    @Column(name = "SL_SITE_VISIT_STARTED_ON")
    @Basic
    var slSiteVisitStartedOn: Timestamp? = null

    @Column(name = "SL_SITE_VISIT_COMPLETED_ON")
    @Basic
    var slSiteVisitCompletedOn: Timestamp? = null

    @Column(name = "SL_SITE_VISIT_PROCESS_INSTANCE_ID")
    @Basic
    var slSiteVisitProcessInstanceId: String? = null

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as StandardLevyPaymentsEntity
        return id == that.id &&
                status == that.status &&
                manufacturerEntity == that.manufacturerEntity &&
                paymentDate == that.paymentDate &&
                paymentAmount == that.paymentAmount &&
                visitStatus == that.visitStatus &&
                officerAssigned == that.officerAssigned &&
                levyPaid == that.levyPaid &&
                levyPayable == that.levyPayable &&
                levyPenalties == that.levyPenalties &&
                levyPenaltyPaymentDate == that.levyPenaltyPaymentDate &&
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
        return Objects.hash(id, status, manufacturerEntity, paymentDate, paymentAmount, visitStatus, officerAssigned, levyPaid, levyPayable, levyPenalties, levyPenaltyPaymentDate, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn)
    }
}