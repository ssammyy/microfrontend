package org.kebs.app.kotlin.apollo.store.model.invoice

import java.io.Serializable
import java.math.BigDecimal
import javax.persistence.*


@Entity
@Table(name = "DAT_KEBS_BILLS")
class BillPayments : Serializable{
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_INVOICE_TRANSACTIONS_SEQ_GEN", sequenceName = "DAT_KEBS_INVOICE_TRANSACTIONS_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_INVOICE_TRANSACTIONS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long = 0

    @Column(name = "CORPORATE_ID")
    var corporateId: Long = 0

    @Column(name = "BILL_NUMBER")
    @Basic
    var invoiceNumber: String? = null

    @Column(name = "TOTAL_AMOUNT")
    @Basic
    var totalAmount: BigDecimal? = null

    @Column(name = "PAYMENT_STATUS")
    @Basic
    var paymentStatus: Int? = null

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