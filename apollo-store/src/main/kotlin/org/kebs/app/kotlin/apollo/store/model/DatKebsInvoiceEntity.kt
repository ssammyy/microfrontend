//package org.kebs.app.kotlin.apollo.store.model
//
//import java.io.Serializable
//import java.sql.Timestamp
//import java.util.*
//import javax.persistence.*
//
//@Entity
//@Table(name = "DAT_KEBS_INVOICE")
//class DatKebsInvoiceEntity: Serializable {
//    @Column(name = "ID")
//    @Id
//    @SequenceGenerator(name = "DAT_KEBS_INVOICE_SEQ_GEN", sequenceName = "DAT_KEBS_INVOICE_SEQ", allocationSize = 1)
//    @GeneratedValue(generator = "DAT_KEBS_INVOICE_SEQ_GEN", strategy = GenerationType.SEQUENCE)
//    var id: Long? = 0
//
//    @Column(name = "STATUS")
//    @Basic
//    var status: Long? = null
//
//    @Column(name = "EXPIRY_DATE")
//    @Basic
//    var expiryDate: Timestamp? = null
//
//    @Column(name = "BUSINESS_NAME")
//    @Basic
//    var businessName: String? = null
//
//    @Column(name = "MANUFACTURER_ID")
//    @Basic
//    var manufacturerId: Long? = null
//
//    @Column(name = "GOODS")
//    @Basic
//    var goods: String? = null
//
//    @Column(name = "CONDITIONS")
//    @Basic
//    var conditions: String? = null
//
//    @Column(name = "SIGNATURE")
//    @Basic
//    var signature: String? = null
//
//    @Column(name = "VAR_FIELD_1")
//    @Basic
//    var varField1: String? = null
//
//    @Column(name = "VAR_FIELD_2")
//    @Basic
//    var varField2: String? = null
//
//    @Column(name = "VAR_FIELD_3")
//    @Basic
//    var varField3: String? = null
//
//    @Column(name = "VAR_FIELD_4")
//    @Basic
//    var varField4: String? = null
//
//    @Column(name = "VAR_FIELD_5")
//    @Basic
//    var varField5: String? = null
//
//    @Column(name = "VAR_FIELD_6")
//    @Basic
//    var varField6: String? = null
//
//    @Column(name = "VAR_FIELD_7")
//    @Basic
//    var varField7: String? = null
//
//    @Column(name = "VAR_FIELD_8")
//    @Basic
//    var varField8: String? = null
//
//    @Column(name = "VAR_FIELD_9")
//    @Basic
//    var varField9: String? = null
//
//    @Column(name = "VAR_FIELD_10")
//    @Basic
//    var varField10: String? = null
//
//    @Column(name = "CREATED_BY")
//    @Basic
//    var createdBy: String? = null
//
//    @Column(name = "CREATED_ON")
//    @Basic
//    var createdOn: Timestamp? = null
//
//    @Column(name = "MODIFIED_BY")
//    @Basic
//    var modifiedBy: String? = null
//
//    @Column(name = "MODIFIED_ON")
//    @Basic
//    var modifiedOn: Timestamp? = null
//
//    @Column(name = "DELETE_BY")
//    @Basic
//    var deleteBy: String? = null
//
//    @Column(name = "DELETED_ON")
//    @Basic
//    var deletedOn: Timestamp? = null
//
//    @Column(name = "VALIDITY")
//    @Basic
//    var validity: Long? = null
//
//    @Column(name = "AMOUNT")
//    @Basic
//    var amount: Long? = null
//
//    @Column(name = "MANUFACTURER")
//    @Basic
//    var manufacturer: Long? = null
//
//    @Column(name = "PERMIT_ID")
//    @Basic
//    var permitId: Long? = null
//
//    @Column(name = "STANDARD_COST")
//    @Basic
//    var standardCost: Long? = null
//
//    @Column(name = "APPLICATION_COST")
//    @Basic
//    var applicationCost: Long? = null
//
//    @Column(name = "INSPECTION_COST")
//    @Basic
//    var inspectionCost: Long? = null
//
//    @Column(name = "FMARK_STATUS")
//    @Basic
//    var fmarkStatus: Long? = null
//
//    @Column(name = "FMARK_COST")
//    @Basic
//    var fmarkCost: Long? = null
//
//    @Column(name = "TAX")
//    @Basic
//    var tax: Long? = null
//
//    @Column(name = "INSTALLATION_INSPECTION_ID")
//    @Basic
//    var installationInspectionId: Long? = null
//
//    @Column(name = "FUEL_INSPECTION_COST")
//    @Basic
//    var fuelInspectionCost: Long? = null
//
//    @Column(name = "PAID_ON")
//    @Basic
//    var paidOn: Timestamp? = null
//
//    @Column(name = "AMOUNT_PAID")
//    @Basic
//    var amountPaid: Long? = null
//
//    @Column(name = "PHONE")
//    @Basic
//    var phone: String? = null
//
//    override fun equals(o: Any?): Boolean {
//        if (this === o) return true
//        if (o == null || javaClass != o.javaClass) return false
//        val that = o as DatKebsInvoiceEntity
//        return id == that.id &&
//                status == that.status &&
//                expiryDate == that.expiryDate &&
//                businessName == that.businessName &&
//                manufacturerId == that.manufacturerId &&
//                goods == that.goods &&
//                conditions == that.conditions &&
//                signature == that.signature &&
//                varField1 == that.varField1 &&
//                varField2 == that.varField2 &&
//                varField3 == that.varField3 &&
//                varField4 == that.varField4 &&
//                varField5 == that.varField5 &&
//                varField6 == that.varField6 &&
//                varField7 == that.varField7 &&
//                varField8 == that.varField8 &&
//                varField9 == that.varField9 &&
//                varField10 == that.varField10 &&
//                createdBy == that.createdBy &&
//                createdOn == that.createdOn &&
//                modifiedBy == that.modifiedBy &&
//                modifiedOn == that.modifiedOn &&
//                deleteBy == that.deleteBy &&
//                deletedOn == that.deletedOn &&
//                validity == that.validity &&
//                amount == that.amount &&
//                manufacturer == that.manufacturer &&
//                permitId == that.permitId &&
//                standardCost == that.standardCost &&
//                applicationCost == that.applicationCost &&
//                inspectionCost == that.inspectionCost &&
//                fmarkStatus == that.fmarkStatus &&
//                fmarkCost == that.fmarkCost &&
//                tax == that.tax &&
//                installationInspectionId == that.installationInspectionId &&
//                fuelInspectionCost == that.fuelInspectionCost &&
//                paidOn == that.paidOn &&
//                amountPaid == that.amountPaid &&
//                phone == that.phone
//    }
//
//    override fun hashCode(): Int {
//        return Objects.hash(id, status, expiryDate, businessName, manufacturerId, goods, conditions, signature, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn, validity, amount, manufacturer, permitId, standardCost, applicationCost, inspectionCost, fmarkStatus, fmarkCost, tax, installationInspectionId, fuelInspectionCost, paidOn, amountPaid, phone)
//    }
//}