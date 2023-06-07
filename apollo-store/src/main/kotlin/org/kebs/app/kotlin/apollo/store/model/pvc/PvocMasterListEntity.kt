package org.kebs.app.kotlin.apollo.store.model.pvc

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_PVOC_MASTER_LIST")
class PvocMasterListEntity : Serializable {
    @Column(name = "ID")
    @Id
    @SequenceGenerator(name = "DAT_KEBS_PVOC_MASTER_LIST_SEQ_GEN", sequenceName = "DAT_KEBS_PVOC_MASTER_LIST_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_PVOC_MASTER_LIST_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long = 0
    @Column(name = "STATUS")
    @Basic
    var status: Long? = null
    @Column(name = "SERIAL_NO")
    @Basic
    var serialNo: String? = null
    @Column(name = "APPLICANT")
    @Basic
    var applicant: String? = null
    @Column(name = "PRODUCT_DESCRIPTION")
    @Basic
    var productDescription: String? = null
    @Column(name = "UNIT")
    @Basic
    var unit: String? = null
    @Column(name = "QUANTITY")
    @Basic
    var quantity: String? = null
    @Column(name = "ORIGIN")
    @Basic
    var origin: String? = null
    @Column(name = "CURRENCY")
    @Basic
    var currency: String? = null
    @Column(name = "HS_CODE")
    @Basic
    var hsCode: String? = null
    @Column(name = "TOTAL_AMOUNT")
    @Basic
    var totalAmount: String? = null
    @Column(name = "DOCUMENTATION")
    @Basic
    var documentation: String? = null
    @Column(name = "WAIVERS_APPLICATION_ID")
    @Basic
    var waiversApplicationId: Long? = null
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

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as PvocMasterListEntity
        return id == that.id &&
                status == that.status &&
                serialNo == that.serialNo &&
                applicant == that.applicant &&
                productDescription == that.productDescription &&
                unit == that.unit &&
                quantity == that.quantity &&
                origin == that.origin &&
                hsCode == that.hsCode &&
                currency == that.currency &&
                totalAmount == that.totalAmount &&
                documentation == that.documentation &&
                waiversApplicationId == that.waiversApplicationId &&
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
        return Objects.hash(id, status, serialNo, applicant, productDescription,hsCode, unit, quantity, origin, currency, totalAmount, documentation, waiversApplicationId, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn)
    }
}