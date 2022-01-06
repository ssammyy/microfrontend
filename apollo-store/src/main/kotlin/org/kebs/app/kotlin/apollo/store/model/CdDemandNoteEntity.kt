package org.kebs.app.kotlin.apollo.store.model

import org.kebs.app.kotlin.apollo.store.model.di.CdItemDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.di.DestinationInspectionFeeEntity
import org.kebs.app.kotlin.apollo.store.model.invoice.CurrencyMasterEntity
import java.io.Serializable
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_CD_DEMAND_NOTE")
class CdDemandNoteEntity : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_CD_DEMAND_NOTE_SEQ_GEN", allocationSize = 1, sequenceName = "DAT_KEBS_CD_DEMAND_NOTE_SEQ")
    @GeneratedValue(generator = "DAT_KEBS_CD_DEMAND_NOTE_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = 0 //id, nameImporter, address, telephone, product, cfvalue, destinationFeeValue

    @Column(name = "NAME_IMPORTER")
    @Basic
    var nameImporter: String? = null
    @Column(name = "SW_STATUS")
    @Basic
    var swStatus: Int? = 0

    @Column(name = "ADDRESS")
    @Basic
    var address: String? = null

    @Column(name = "TELEPHONE")
    @Basic
    var telephone: String? = null

    @Column(name = "PRODUCT")
    @Basic
    var product: String? = null

    @Column(name = "C_F_VALUE")
    @Basic
    var cfvalue: BigDecimal? = null

    @Column(name = "INVOICE_BATCH_NUMBER_ID")
    @Basic
    var invoiceBatchNumberId: Long? = null

    @Column(name = "DESTINATION_FEE_VALUE")
    @Basic
    var destinationFeeValue: Long? = 0

    @Column(name = "RATE")
    @Basic
    var rate: String? = null

    @Column(name = "TOTAL_AMOUNT")
    @Basic
    var totalAmount: BigDecimal? = null

    @Column(name = "AMOUNT_PAYABLE")
    @Basic
    var amountPayable: BigDecimal? = null

    @Column(name = "ENTRY_ABL_NUMBER")
    @Basic
    var entryAblNumber: String? = null

    @Column(name = "PRO_IDE_NUMBER")
    @Basic
    var proIdeNumber: String? = null

    @Column(name = "DATE_GENERATED")
    @Basic
    var dateGenerated: Date? = null

    @Column(name = "DESCRIPTION_GOODS")
    @Basic
    var descriptionGoods: String? = null

    @Column(name = "UCR_NUMBER")
    @Basic
    var ucrNumber: String? = null

    @Column(name = "CD_REF_NUMBER")
    @Basic
    var cdRefNo: String? = null

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

    @Column(name = "DEMAND_NOTE_NUMBER")
    @Basic
    var demandNoteNumber: String? = null

    @Column(name = "RECEIPT_NO")
    @Basic
    var receiptNo: String? = null

    @Column(name = "GENERATED_BY")
    @Basic
    var generatedBy: String? = null

    @Column(name = "PAYMENT_STATUS")
    @Basic
    var paymentStatus: Int? = 0

    @Column(name = "CD_ID")
    @Basic
    var cdId: Long? = 0


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as CdDemandNoteEntity
        return id == that.id &&
                cdId == that.cdId &&
                destinationFeeValue == that.destinationFeeValue &&
                invoiceBatchNumberId == that.invoiceBatchNumberId &&
                nameImporter == that.nameImporter &&
                address == that.address &&
                telephone == that.telephone &&
                product == that.product &&
                cfvalue == that.cfvalue &&
                rate == that.rate &&
                totalAmount == that.totalAmount &&
                amountPayable == that.amountPayable &&
                entryAblNumber == that.entryAblNumber &&
                proIdeNumber == that.proIdeNumber &&
                dateGenerated == that.dateGenerated &&
                descriptionGoods == that.descriptionGoods &&
                ucrNumber == that.ucrNumber &&
                cdRefNo == that.cdRefNo &&
                status == that.status &&
                demandNoteNumber == that.demandNoteNumber &&
                paymentStatus == that.paymentStatus &&
                generatedBy == that.generatedBy &&
                receiptNo == that.receiptNo &&
                varField1 == that.varField1 &&
                varField2 == that.varField2 &&
                varField3 == that.varField3 &&
                varField4 == that.varField4 &&
                varField5 == that.varField5 &&
                varField6 == that.varField6 &&
                varField7 == that.varField7 &&
                varField8 == that.varField8 &&
                varField9 == that.varField9 &&
                varField10 == that.varField10 &&
                createdBy == that.createdBy &&
                createdOn == that.createdOn &&
                modifiedBy == that.modifiedBy &&
                modifiedOn == that.modifiedOn &&
                deleteBy == that.deleteBy &&
                deletedOn == that.deletedOn
    }

    override fun hashCode(): Int {
        return Objects.hash(
            id,
            cdId,
            generatedBy,
            invoiceBatchNumberId,
            paymentStatus,
            receiptNo,
            destinationFeeValue,
            nameImporter,
            address,
            telephone,
            product,
                cfvalue,
                rate,
                totalAmount,
                amountPayable,
                entryAblNumber,
                proIdeNumber,
                dateGenerated,
                descriptionGoods,
                ucrNumber,
                cdRefNo,
                status,
                demandNoteNumber,
                varField1,
                varField2,
                varField3,
                varField4,
                varField5,
                varField6,
                varField7,
                varField8,
                varField9,
                varField10,
                createdBy,
                createdOn,
                modifiedBy,
                modifiedOn,
                deleteBy,
                deletedOn
        )
    }
}
