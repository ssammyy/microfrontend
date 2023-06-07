package org.kebs.app.kotlin.apollo.store.model


import java.io.Serializable
import java.sql.Timestamp
import java.time.LocalDate
import java.util.*
import javax.persistence.*


@Entity
@Table(name = "LOG_KEBS_STANDARD_LEVY_PAYMENTS")
class LogKebsLevyPayments : Serializable {
    @Column(name = "ID", nullable = false, precision = 0)
    @Id
    var id: Long = 0

    @Column(name = "STATUS")
    var status: Long? = null

    @Column(name = "MANUFACTURER_ENTITY")
    var manufacturerEntity: Long? = null

    @Column(name = "PAYMENT_DATE")
    var paymentDate: Timestamp? = null

    @Column(name = "PAYMENT_AMOUNT")
    var paymentAmount: Float? = null

    @Column(name = "VISIT_STATUS")
    var visitStatus: Long? = null

    @Column(name = "OFFICER_ASSIGNED")
    var officerAssigned: Long? = null

    @Column(name = "LEVY_PAID")
    var levyPaid: Float? = null

    @Column(name = "LEVY_PAYABLE")
    var levyPayable: Float? = null

    @Column(name = "LEVY_PENALTIES_PAID")
    var levyPenaltiesPaid: Float? = null

    @Column(name = "LEVY_PENALTY_PAYMENT_DATE")
    var levyPenaltyPaymentDate: String? = null

    @Column(name = "VAR_FIELD_1")
    var varField1: String? = null

    @Column(name = "VAR_FIELD_2")
    var varField2: String? = null

    @Column(name = "VAR_FIELD_3")
    var varField3: String? = null

    @Column(name = "VAR_FIELD_4")
    var varField4: String? = null

    @Column(name = "VAR_FIELD_5")
    var varField5: String? = null

    @Column(name = "VAR_FIELD_6")
    var varField6: String? = null

    @Column(name = "VAR_FIELD_7")
    var varField7: String? = null

    @Column(name = "VAR_FIELD_8")
    var varField8: String? = null

    @Column(name = "VAR_FIELD_9")
    var varField9: String? = null

    @Column(name = "VAR_FIELD_10")
    var varField10: String? = null

    @Column(name = "CREATED_BY", nullable = false)
    var createdBy: String? = null

    @Column(name = "CREATED_ON", nullable = false)
    var createdOn: Timestamp? = null

    @Column(name = "MODIFIED_BY")
    var modifiedBy: String? = null

    @Column(name = "MODIFIED_ON")
    var modifiedOn: Timestamp? = null

    @Column(name = "DELETE_BY")
    var deleteBy: String? = null

    @Column(name = "DELETED_ON")
    var deletedOn: Date? = null

    @Column(name = "SL_REGISTRATION_STATUS")
    var slRegistrationStatus: Long? = null

    @Column(name = "SL_REGISTRATION_STARTED_ON")
    var slRegistrationStartedOn: Timestamp? = null

    @Column(name = "SL_REGISTRATION_COMPLETED_ON")
    var slRegistrationCompletedOn: Timestamp? = null

    @Column(name = "SL_REGISTRATION_PROCESS_INSTANCE_ID")
    var slRegistrationProcessInstanceId: String? = null

    @Column(name = "SL_SITE_VISIT_STATUS")
    var slSiteVisitStatus: Long? = null

    @Column(name = "SL_SITE_VISIT_STARTED_ON")
    var slSiteVisitStartedOn: Timestamp? = null

    @Column(name = "SL_SITE_VISIT_COMPLETED_ON")
    var slSiteVisitCompltedOn: Timestamp? = null

    @Column(name = "SL_SITE_VISIT_PROCESS_INSTANCE_ID")
    var slSiteVisitProcessInstanceId: String? = null

    @Column(name = "KRA_PIN")
    var kraPin: String? = null

    @Column(name = "NET_LEVY_AMOUNT")
    var netLevyAmount: Float? = null

    @Column(name = "LEVY_PENALTY_PAYABLE")
    var levyPenaltyPayable: Float? = null

    @Column(name = "PAID_STATUS")
    var paidStatus: Long? = null

    @Column(name = "YEARLY_CUMULATIVE_TOTAL")
    var yearlyCumulativeTotal: Float? = null

    @Column(name = "CURRENT_MONTH_LEVY")
    var currentMonthlyLevy: Float? = null

    @Column(name = "PAYMENT_ID")
    var paymentId: Long? = null

    @Column(name = "OVERALL_NET_AMT")
    var overallNetAmt: Float? = null

    @Column(name = "PERIOD_FROM")
    var periodFrom: Date? = null

    @Column(name = "PERIOD_TO")
    var periodTo: Date? = null

    @Column(name = "NET_PENALTY_AMT")
    var netPenaltyAmt: Float? = null

    @Column(name = "ENTRY_NUMBER")
    var entryNumber: String? = null

    @Column(name = "OVERDUE")
    var overDue: Long? = null

    @Column(name = "PENALTY_APPLIED")
    var penaltyApplied: Long? = null

    @Column(name = "PENALTY_DATE")
    var penaltyDate: Timestamp? = null

    @Column(name = "LEVY_DUE_DATE")
    var levyDueDate: Timestamp? = null

    @Column(name = "PAYMENT_STARTED")
    var paymentStarted: Long? = null

    @Column(name = "MONTHS_LATE")
    var monthsLate: Float? = null

    @Column(name = "PENALTY_SENT")
    var penaltySent: Long? = null

    @Column(name = "NAME_OF_FIRM")
    var nameOfFirm: String? = null

    @Column(name = "LOCATION")
    var location: String? = null

    @Column(name = "PRN")
    var prn: String? = null


}
