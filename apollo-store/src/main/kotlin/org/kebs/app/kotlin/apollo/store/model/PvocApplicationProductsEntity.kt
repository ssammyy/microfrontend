package org.kebs.app.kotlin.apollo.store.model

import com.fasterxml.jackson.annotation.JsonFormat
import org.hibernate.annotations.Parent
import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*
import javax.persistence.JoinColumn




@Entity
@Table(name = "DAT_KEBS_PVOC_APPLICATION_PRODUCTS")
class PvocApplicationProductsEntity : Serializable {
//    @Column(name = "ID", nullable = false, precision = 0)
//    @SequenceGenerator(name = "DAT_KEBS_PVOC_APPLICATION_PRODUCTS_SEQ_GEN", sequenceName = "DAT_KEBS_PVOC_APPLICATION_PRODUCTS_SEQ", allocationSize = 1)
//    @GeneratedValue(generator = "DAT_KEBS_PVOC_APPLICATION_PRODUCTS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
//    @Id
//    var id: Long? = 0

    @Column(name = "ID", nullable = false, precision = 0)
    @Id
    @SequenceGenerator(name = "DAT_KEBS_PVOC_APPLICATION_PRODUCTS_SEQ_GEN", sequenceName = "DAT_KEBS_PVOC_APPLICATION_PRODUCTS_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_PVOC_APPLICATION_PRODUCTS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long? = 0
    @Column(name = "STATUS", nullable = true, precision = 0)
    @Basic
    var status: Int? = null
    @Column(name = "PRODUCT_NAME", nullable = true, length = 200)
    @Basic
    var productName: String? = null
    @Column(name = "BRAND", nullable = true, length = 200)
    @Basic
    var brand: String? = null
    @Column(name = "KEBS_STANDARDIZATION_MARK_PERMIT", nullable = true, length = 200)
    @Basic
    var kebsStandardizationMarkPermit: String? = null

    @Column(name = "EXPIRELY_DATE", nullable = true, length = 200)
    @Basic
    var expirelyDate: String? = null
    @Column(name = "VAR_FIELD_1", nullable = true, length = 350)
    @Basic
    var varField1: String? = null
    @Column(name = "VAR_FIELD_2", nullable = true, length = 350)
    @Basic
    var varField2: String? = null
    @Column(name = "VAR_FIELD_3", nullable = true, length = 350)
    @Basic
    var varField3: String? = null
    @Column(name = "VAR_FIELD_4", nullable = true, length = 350)
    @Basic
    var varField4: String? = null
    @Column(name = "VAR_FIELD_5", nullable = true, length = 350)
    @Basic
    var varField5: String? = null
    @Column(name = "VAR_FIELD_6", nullable = true, length = 350)
    @Basic
    var varField6: String? = null
    @Column(name = "VAR_FIELD_7", nullable = true, length = 350)
    @Basic
    var varField7: String? = null
    @Column(name = "VAR_FIELD_8", nullable = true, length = 350)
    @Basic
    var varField8: String? = null
    @Column(name = "VAR_FIELD_9", nullable = true, length = 350)
    @Basic
    var varField9: String? = null
    @Column(name = "VAR_FIELD_10", nullable = true, length = 350)
    @Basic
    var varField10: String? = null
    @Column(name = "CREATED_BY", nullable = false, length = 100)
    @Basic
    var createdBy: String? = null
    @Column(name = "CREATED_ON", nullable = false)
    @Basic
    var createdOn: Timestamp? = null
    @Column(name = "MODIFIED_BY", nullable = true, length = 100)
    @Basic
    var modifiedBy: String? = null
    @Column(name = "MODIFIED_ON", nullable = true)
    @Basic
    var modifiedOn: Timestamp? = null
    @Column(name = "DELETE_BY", nullable = true, length = 100)
    @Basic
    var deleteBy: String? = null
    @Column(name = "DELETED_ON", nullable = true)
    @Basic
    var deletedOn: Timestamp? = null
    @Column(name = "SECTION", nullable = true, length = 400)
    @Basic
    var section: String? = null

    @Column(name = "CHECK_BOX_CHECKED", nullable = true, length = 400)
    @Basic
    var checkBoxChecked: Int? = null

    @JoinColumn(name = "PVOC_APPLICATION_ID", referencedColumnName = "ID")//cascade=CascadeType.ALL
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = PvocApplicationEntity::class)
    var pvocApplicationId: PvocApplicationEntity? = null

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as PvocApplicationProductsEntity
        return id == that.id &&
                status == that.status &&
                productName == that.productName &&
                brand == that.brand &&
                kebsStandardizationMarkPermit == that.kebsStandardizationMarkPermit &&
                expirelyDate == that.expirelyDate &&
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
                deletedOn == that.deletedOn &&
                section == that.section &&
                checkBoxChecked == that.checkBoxChecked
    }

    override fun hashCode(): Int {
        return Objects.hash(id, status, productName, brand, kebsStandardizationMarkPermit, expirelyDate, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn, section, checkBoxChecked)
    }

}