package org.kebs.app.kotlin.apollo.store.model

import org.kebs.app.kotlin.apollo.store.model.ms.MsFuelInspectionEntity
import java.io.Serializable
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_MS_FUEL_REMEDY_INVOICES")
class MsFuelRemedyInvoicesEntity : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_MS_FUEL_REMEDY_INVOICES_SEQ_GEN", allocationSize = 1, sequenceName = "DAT_KEBS_MS_FUEL_REMEDY_INVOICES_SEQ")
    @GeneratedValue(generator = "DAT_KEBS_MS_FUEL_REMEDY_INVOICES_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long = 0

    @Column(name = "INVOICE_NUMBER")
    @Basic
    var invoiceNumber: String? = null

    @Column(name = "SAGE_INVOICE_NUMBER")
    @Basic
    var sageInvoiceNumber: String? = null

    @Column(name = "RECEIPT_NO")
    @Basic
    var receiptNo: String? = null

    @Column(name = "INVOICE_BATCH_NUMBER_ID")
    @Basic
    var invoiceBatchNumberId: Long? = null

    @Column(name = "PAYMENT_STATUS")
    @Basic
    var paymentStatus: Int? = 0

    @Column(name = "AMOUNT")
    @Basic
    var amount: BigDecimal? = null

    @Column(name = "TOTAL_TAX_AMOUNT")
    @Basic
    var totalTaxAmount: BigDecimal? = null

    @Column(name = "INVOICE_DATE")
    @Basic
    var invoiceDate: Date? = null

    @Column(name = "PAYMENT_DATE")
    @Basic
    var paymentDate: Timestamp? = null

    @Column(name = "TRANSACTION_DATE")
    @Basic
    var transactionDate: Date? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null

    @Column(name = "REMARKS")
    @Basic
    var remarks: String? = null

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
    var lastModifiedBy: String? = null

    @Column(name = "LAST_MODIFIED_ON")
    @Basic
    var lastModifiedOn: Timestamp? = null

    @Column(name = "UPDATE_BY")
    @Basic
    var updateBy: String? = null

    @Column(name = "UPDATED_ON")
    @Basic
    var updatedOn: Timestamp? = null

    @Column(name = "DELETE_BY")
    @Basic
    var deleteBy: String? = null

    @Column(name = "DELETED_ON")
    @Basic
    var deletedOn: Timestamp? = null

    @Column(name = "VERSION")
    @Basic
    var version: Long? = null

    @Column(name = "REMUNERATION_RATE_LITER")
    @Basic
    var remunerationRateLiter: Long? = null

    @Column(name = "REMUNERATION_SUB_TOTAL")
    @Basic
    var remunerationSubTotal: Long? = null

    @Column(name = "REMUNERATION_VAT")
    @Basic
    var remunerationVat: BigDecimal? = null

    @Column(name = "REMUNERATION_TOTAL")
    @Basic
    var remunerationTotal: BigDecimal? = null

    @Column(name = "VOLUME_FUEL_REMEDIATED")
    @Basic
    var volumeFuelRemediated: Long? = null

    @Column(name = "SUBSISTENCE_TOTAL_NIGHTS")
    @Basic
    var subsistenceTotalNights: Long? = null

    @Column(name = "SUBSISTENCE_TOTAL_NIGHTS_RATE")
    @Basic
    var subsistenceTotalNightsRate: Long? = null

    @Column(name = "SUBSISTENCE_RATE")
    @Basic
    var subsistenceRate: Long? = null

    @Column(name = "SUBSISTENCE_RATE_NIGHT_TOTAL")
    @Basic
    var subsistenceRateNightTotal: Long? = null

    @Column(name = "SUBSISTENCE_VAT")
    @Basic
    var subsistenceVat: BigDecimal? = null

    @Column(name = "SUBSISTENCE_TOTAL")
    @Basic
    var subsistenceTotal: BigDecimal? = null

    @Column(name = "TRANSPORT_AIR_TICKET")
    @Basic
    var transportAirTicket: Long? = null

    @Column(name = "TRANSPORT_INKM")
    @Basic
    var transportInkm: Long? = null

    @Column(name = "TRANSPORT_RATE")
    @Basic
    var transportRate: Long? = null

    @Column(name = "TRANSPORT_TOTAL_KMRATE")
    @Basic
    var transportTotalKmrate: Long? = null

    @Column(name = "TRANSPORT_VAT")
    @Basic
    var transportVat: BigDecimal? = null

    @Column(name = "TRANSPORT_TOTAL")
    @Basic
    var transportTotal: BigDecimal? = null

    @Column(name = "TRANSPORT_GRAND_TOTAL")
    @Basic
    var transportGrandTotal: BigDecimal? = null

    @Column(name = "AMOUNT_TO_PAY")
    @Basic
    var amountToPay: BigDecimal? = null

    @Column(name = "INSPECTION_ID")
    @Basic
    var fuelInspectionId: Long? = null

    @Column(name = "FUEL_INSPECTION_REF_NUMBER")
    @Basic
    var fuelInspectionRefNumber: String? = null

}
