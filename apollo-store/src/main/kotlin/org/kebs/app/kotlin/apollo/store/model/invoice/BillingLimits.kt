package org.kebs.app.kotlin.apollo.store.model.invoice

import java.io.Serializable
import java.math.BigDecimal
import java.sql.Timestamp
import javax.persistence.*


@Table(name = "DAT_KEBS_BILL_LIMITS")
@Entity
class BillingLimits : Serializable {
    @Column(name = "ID")
    @Id
    @SequenceGenerator(name = "DAT_KEBS_BILL_LIMITS_SEQ_GEN", sequenceName = "DAT_KEBS_BILL_LIMITS_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_BILL_LIMITS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long = 0

    @Column(name = "BILL_TYPE")
    @Basic
    var billType: String? = null

    @Column(name = "CORPORATE_TYPE")
    @Basic
    var corporateType: String? = null

    @Column(name = "BILL_RECEIPT_PREFIX")
    @Basic
    var billReceiptPrefix: String? = null

    @Column(name = "BILL_DATE_TYPE") // DATE_RANGE, LAST_MONTH
    @Basic
    var billDateType: String? = null

    @Column(name = "BILL_START_DAY")
    @Basic
    var billStartDate: Int? = null

    @Column(name = "BILL_END_DAY")
    @Basic
    var billEndDay: Int? = null

    @Column(name = "BILL_PAYMENT_DAY")
    @Basic
    var billPaymentDay: Int? = null

    @Column(name = "PENALTY_AMOUNT")
    @Basic
    var penaltyAmount: BigDecimal? = null

    @Column(name = "PENALTY_TYPE") //FIXED,PERCENTAGE
    @Basic
    var penaltyType: String? = null

    @Column(name = "BILL_AMOUNT")
    @Basic
    var maxBillAmount: BigDecimal? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null

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
}