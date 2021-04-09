package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.math.BigDecimal
import java.util.*
import javax.persistence.*

/**
 * Logging Table for KRA SL2 payements, this one contains the detailed data for each payment request
 */
@Entity
@Table(name = "LOG_SL2_PAYMENTS_DETAILS")
class Sl2PaymentsDetailsEntity : Serializable {
    @Id
    @SequenceGenerator(name = "LOG_SL2_PAYMENTS_DETAILS_SEQ_GEN", sequenceName = "LOG_SL2_PAYMENTS_DETAILS_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "LOG_SL2_PAYMENTS_DETAILS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID", nullable = false)
    var id: Long? = null

    @Column(name = "HEADER_ID")
    var headerId: Long? = null

    @Column(name = "TRANSACTION_TYPE")
    var transactionType: String? = null

    @Column(name = "COMMODITY_TYPE")
    var commodityType: String? = null

    @Column(name = "PENALTY_ORDER_NO")
    var penaltyOrderNo: String? = null

    @Column(name = "PERIOD_FROM")
    var periodFrom: Date? = null

    @Column(name = "PERIOD_TO")
    var periodTo: Date? = null

    @Column(name = "QTY_MANF")
    var qtyManf: BigDecimal? = null

    @Column(name = "EX_FACT_VAL")
    var exFactVal: BigDecimal? = null

    @Column(name = "LEVY_PAID")
    var levyPaid: BigDecimal? = null

    @Column(name = "PENALTY_PAID")
    var penaltyPaid: BigDecimal? = null

    @Column(name = "TRANSACTION_DATE", nullable = false)
    var transactionDate: Date? = null

    @Column(name = "DESCRIPTION")
    var description: String? = null

    @Column(name = "STATUS")
    var status: Int? = null

    @Column(name = "DESCRIPTIONS")
    var descriptions: String? = null

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
    var createdOn: Date? = null

    @Column(name = "LAST_MODIFIED_BY")
    var lastModifiedBy: String? = null

    @Column(name = "LAST_MODIFIED_ON")
    var lastModifiedOn: Date? = null

    @Column(name = "UPDATE_BY")
    var updateBy: String? = null

    @Column(name = "UPDATED_ON")
    var updatedOn: Date? = null

    @Column(name = "DELETE_BY")
    var deleteBy: String? = null

    @Column(name = "DELETED_ON")
    var deletedOn: Date? = null

    @Column(name = "VERSION")
    var version: Int? = null

    companion object {
        private const val serialVersionUID = 1L
    }
}
