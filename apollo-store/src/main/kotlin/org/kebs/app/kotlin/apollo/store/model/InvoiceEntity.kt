package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.math.BigDecimal
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_INVOICE")
class InvoiceEntity: Serializable {
    @Column(name = "ID")
    @Id
    @SequenceGenerator(name = "DAT_KEBS_INVOICE_SEQ_GEN", sequenceName = "DAT_KEBS_INVOICE_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_INVOICE_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long? = 0

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null

    @Column(name = "PAID_ON")
    @Basic
    var paidOn: Timestamp? = null

    @Column(name = "FUEL_INSPECTION_COST")
    @Basic
    var fuelInspectionCost: BigDecimal? = null

    @JoinColumn(name = "INSTALLATION_INSPECTION_ID", referencedColumnName = "ID")
    @ManyToOne
    var installationInspectionId: PetroleumInstallationInspectionEntity? = null

    @Column(name = "FMARK_STATUS")
    @Basic
    var fmarkStatus: BigDecimal? = null

    @Column(name = "FMARK_COST")
    @Basic
    var fmarkCost: BigDecimal? = null

    @Column(name = "TAX")
    @Basic
    var tax: BigDecimal? = null

    @Column(name = "PAYMENT_STATUS")
    @Basic
    var paymentStatus: Int? = 0

    @Column(name = "INVOICE_BATCH_NUMBER_ID")
    @Basic
    var invoiceBatchNumberId: Long? = null

    @Column(name = "STANDARD_COST")
    @Basic
    var standardCost: BigDecimal? = null

    @Column(name = "APPLICATION_COST")
    @Basic
    var applicationCost: BigDecimal? = null

    @Column(name = "INSPECTION_COST")
    @Basic
    var inspectionCost: BigDecimal? = null

    /*
    @JoinColumn(name = "MANUFACTURER", referencedColumnName = "ID")
    @ManyToOne
    var manufacturer: ManufacturersEntity? = null
     */

    @Column(name = "MANUFACTURER")
    @Basic
    var manufacturer: Long? = null

//    @JoinColumn(name = "PERMIT_ID", referencedColumnName = "ID")
//    @ManyToOne
//    var permitId: PermitApplicationEntity? = null

    @Column(name = "PERMIT_ID")
    @Basic
    var permitId: Long? = null

    @Column(name = "USER_ID")
    @Basic
    var userId: Long? = null

    /*
    @JoinColumn(name = "PERMIT_ID", referencedColumnName = "ID")
    @ManyToOne
    var permitId: PermitApplicationEntity? = null
     */

    @Column(name = "AMOUNT")
    @Basic
    var amount: BigDecimal? = null

    @Column(name = "EXPIRY_DATE")
    @Basic
    var expiryDate: Timestamp? = null

    @Column(name = "BUSINESS_NAME")
    @Basic
    var businessName: String? = null

    @Column(name = "GOODS")
    @Basic
    var goods: String? = null

    @Column(name = "RECEIPT_NO")
    @Basic
    var receiptNo: String? = null

    @Column(name = "INVOICE_NUMBER")
    @Basic
    var invoiceNumber: String? = null

    @Column(name = "CONDITIONS")
    @Basic
    var conditions: String? = null

    @Column(name = "SIGNATURE")
    @Basic
    var signature: String? = null

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

    @Column(name = "PHONE")
    @Basic
    var phone: String? = null

    @Column(name = "DELETED_ON")
    @Basic
    var deletedOn: Timestamp? = null

    @Column(name = "AMOUNT_PAID")
    @Basic
    var amountPaid: BigDecimal? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as InvoiceEntity
        return id == that.id &&
                inspectionCost == that.inspectionCost &&
                applicationCost == that.applicationCost &&
                paymentStatus == that.paymentStatus &&
                receiptNo == that.receiptNo &&
                invoiceNumber == that.invoiceNumber &&
                invoiceBatchNumberId == that.invoiceBatchNumberId &&
                standardCost == that.standardCost &&
                userId == that.userId &&
                permitId == that.permitId &&
                fmarkCost == that.fmarkCost &&
                amountPaid == that.amountPaid &&
                manufacturer == that.manufacturer &&
                amount == that.amount &&
                status == that.status &&
                expiryDate == that.expiryDate &&
                businessName == that.businessName &&
                goods == that.goods &&
                fmarkStatus == that.fmarkStatus &&
                conditions == that.conditions &&
                signature == that.signature &&
                varField1 == that.varField1 &&
                varField2 == that.varField2 &&
                varField3 == that.varField3 &&
                varField4 == that.varField4 &&
                varField5 == that.varField5 &&
                varField6 == that.varField6 &&
                varField7 == that.varField7 &&
                fuelInspectionCost == that.fuelInspectionCost &&
                varField8 == that.varField8 &&
                varField9 == that.varField9 &&
                varField10 == that.varField10 &&
                createdBy == that.createdBy &&
                installationInspectionId == that.installationInspectionId &&
                createdOn == that.createdOn &&
                tax == that.tax &&
                modifiedBy == that.modifiedBy &&
                modifiedOn == that.modifiedOn &&
                deleteBy == that.deleteBy &&
                phone == that.phone &&
                paidOn == that.paidOn &&
                deletedOn == that.deletedOn
    }

    override fun hashCode(): Int {
        return Objects.hash(id, status, receiptNo, invoiceNumber,paymentStatus, invoiceBatchNumberId, userId, paidOn, phone, fmarkCost, amountPaid, fuelInspectionCost, installationInspectionId, tax, standardCost, fmarkStatus, inspectionCost, applicationCost, expiryDate, manufacturer, businessName, goods, conditions, signature, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn, amount, permitId)
    }
}