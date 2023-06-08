package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp
import javax.persistence.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

/**
 * Logging Table for KRA SL2 payements, this one contains the header values
 */
@Entity
@Table(name = "LOG_SL2_PAYMENTS_HEADER")
class Sl2PaymentsHeaderEntity : Serializable {
    @Id
    @SequenceGenerator(name = "LOG_SL2_PAYMENTS_HEADER_SEQ_GEN", sequenceName = "LOG_SL2_PAYMENTS_HEADER_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "LOG_SL2_PAYMENTS_HEADER_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID", nullable = false)
    var id: Long? = null

    @Column(name = "REQUEST_HEADER_TRANSMISSION_DATE")
    @NotNull(message = "Required field")
    var requestHeaderTransmissionDate: Timestamp? = null

//    @Column(name = "HEADER_TRANSMISSION_DATE")
//    @NotNull(message = "Required field")
//    var headerTransmissionDate: Date? = null

    @Column(name = "REQUEST_HEADER_ENTRY_NO")
    @NotEmpty(message = "Required field")
    var requestHeaderEntryNo: String? = null

    @Column(name = "REQUEST_HEADER_KRA_PIN")
    @NotEmpty(message = "Required field")
    var requestHeaderKraPin: String? = null

    @Column(name = "REQUEST_HEADER_MANUFACTURER_NAME")
    @NotEmpty(message = "Required field")
    var requestHeaderManufacturerName: String? = null

    @Column(name = "REQUEST_HEADER_PAYMENT_SLIP_NO")
    @NotEmpty(message = "Required field")
    var requestHeaderPaymentSlipNo: String? = null

    @Column(name = "REQUEST_HEADER_PAYMENT_SLIP_DATE")
    @NotNull(message = "Required field")
    var requestHeaderPaymentSlipDate: Date? = null

    @Column(name = "REQUEST_HEADER_PAYMENT_TYPE")
    @NotEmpty(message = "Required field")
    var requestHeaderPaymentType: String? = null

    @Column(name = "REQUEST_HEADER_TOTAL_DECL_AMT")
    @NotNull(message = "Required field")
    var requestHeaderTotalDeclAmt: BigDecimal? = null

    @Column(name = "REQUEST_HEADER_TOTAL_PENALTY_AMT")
    @NotNull(message = "Required field")
    var requestHeaderTotalPenaltyAmt: BigDecimal? = null

    @Column(name = "REQUEST_HEADER_TOTAL_PAYMENT_AMT")
    @NotNull(message = "Required field")
    var requestHeaderTotalPaymentAmt: BigDecimal? = null

    @Column(name = "REQUEST_HEADER_BANK")
    var requestHeaderBank: String? = null

    @Column(name = "REQUEST_BANK_REF_NO")
    var requestBankRefNo: String? = null

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
    var createdOn: Timestamp? = null

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
