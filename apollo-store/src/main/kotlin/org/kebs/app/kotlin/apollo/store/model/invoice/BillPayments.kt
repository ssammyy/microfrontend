package org.kebs.app.kotlin.apollo.store.model.invoice

import java.io.Serializable
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp
import javax.persistence.*


@Entity
@Table(name = "DAT_KEBS_BILLS")
class BillPayments : Serializable {
    @Column(name = "ID")
    @Id
    @SequenceGenerator(name = "DAT_KEBS_BILLS_SEQ_GEN", sequenceName = "DAT_KEBS_BILLS_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_BILLS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long = 0

    @Column(name = "CORPORATE_ID")
    var corporateId: Long = 0

    @Column(name = "BILL_NUMBER")
    @Basic
    var billNumber: String? = null

    @Column(name = "BILL_NUMBER_PREFIX")
    @Basic
    var billNumberPrefix: String? = null

    @Column(name = "BILL_NUMBER_SEQUENCE")
    @Basic
    var billNumberSequence: Long? = null

    @Column(name = "INVOICE_NUMBER")
    @Basic
    var invoiceNumber: String? = null

    @Column(name = "BILL_AMOUNT")
    @Basic
    var billAmount: BigDecimal? = null

    @Column(name = "PENALTY_AMOUNT")
    @Basic
    var penaltyAmount: BigDecimal? = null

    @Column(name = "TOTAL_AMOUNT")
    @Basic
    var totalAmount: BigDecimal? = null

    @Column(name = "PAYMENT_STATUS")
    @Basic
    var paymentStatus: Int? = null

    @Column(name = "PAYMENT_REQUEST_DATE")
    @Basic
    var paymentRequestDate: Timestamp? = null

    @Column(name = "PAYMENT_REQUEST_REFERENCE")
    @Basic
    var paymentRequestReference: String? = null

    @Column(name = "BILL_STATUS")
    @Basic
    var billStatus: Int? = null

    @Column(name = "BILL_STATUS_DESC")
    @Basic
    var billStatusDesc: String? = null

    @Column(name = "PAYMENT_DATE")
    @Basic
    var paymentDate: Date? = null

    @Column(name = "NOTICE_COUNT")
    @Basic
    var noticeCount:Long? = null

    @Column(name = "NEXT_NOTICE_DATE")
    @Basic
    var nextNoticeDate: Date? = null

    @Column(name = "PAYMENT_RECEIPT")
    @Basic
    var paymentReceipts: String? = null

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

    @Column(name = "CREATE_ON")
    @Basic
    var createOn: Timestamp? = null

}