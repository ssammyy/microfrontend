package org.kebs.app.kotlin.apollo.store.model

import org.kebs.app.kotlin.apollo.store.model.ms.MsFuelInspectionEntity
import java.io.Serializable
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_MS_FUEL_REMEDIATION")
class MsFuelRemediationEntity : Serializable{
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_MS_FUEL_REMEDIATION_SEQ_GEN", allocationSize = 1, sequenceName = "DAT_KEBS_MS_FUEL_REMEDIATION_SEQ")
    @GeneratedValue(generator = "DAT_KEBS_MS_FUEL_REMEDIATION_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long = 0

    @Transient
    var processStageRemediation: String? = null

    @Column(name = "PRODUCT_TYPE")
    @Basic
    var productType: String? = null

    @Column(name = "APPLICABLE_KENYA_STANDARD")
    @Basic
    var applicableKenyaStandard: String? = null

    @Column(name = "REMEDIATION_PROCEDURE")
    @Basic
    var remediationProcedure: String? = null

    @Column(name = "VOLUME_OF_PRODUCT_CONTAMINATED")
    @Basic
    var volumeOfProductContaminated: String? = null

    @Column(name = "CONTAMINATED_FUEL_TYPE")
    @Basic
    var contaminatedFuelType: String? = null

    @Column(name = "QUANTITY_OF_FUEL")
    @Basic
    var quantityOfFuel: String? = null

    @Column(name = "VOLUME_ADDED")
    @Basic
    var volumeAdded: String? = null

    @Column(name = "TOTAL_VOLUME")
    @Basic
    var totalVolume: String? = null

    @Column(name = "PRO_FORMA_INVOICE_STATUS")
    @Basic
    var proFormaInvoiceStatus: Long? = null

    @Column(name = "PRO_FORMA_INVOICE_NO")
    @Basic
    var proFormaInvoiceNo: String? = null

    @Column(name = "INVOICE_AMOUNT")
    @Basic
    var invoiceAmount: BigDecimal? = null

    @Column(name = "FEE_PAID_RECEIPT_NO")
    @Basic
    var feePaidReceiptNo: String? = null

    @Column(name = "DATE_OF_REMEDIATION")
    @Basic
    var dateOfRemediation: Date? = null

    @Column(name = "DATE_OF_PAYMENT")
    @Basic
    var dateOfPayment: Date? = null

    @Column(name = "DESCRIPTION")
    @Basic
    var description: String? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null

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

    @Column(name = "FUEL_INSPECTION_ID")
    @Basic
    var fuelInspectionId: Long? = null
}
