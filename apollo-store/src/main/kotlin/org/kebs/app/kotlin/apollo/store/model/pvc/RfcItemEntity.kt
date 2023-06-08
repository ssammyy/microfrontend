package org.kebs.app.kotlin.apollo.store.model.pvc

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_RFC_REQUEST_ITEMS")
class RfcItemEntity : Serializable {
    @Column(name = "ID", nullable = false, precision = 0)
    @Id
    @SequenceGenerator(name = "DAT_KEBS_RFC_REQUEST_ITEMS_SEQ_GEN", sequenceName = "DAT_KEBS_RFC_REQUEST_ITEMS_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_RFC_REQUEST_ITEMS_SEQ_GEN", strategy = GenerationType.SEQUENCE)

    var id: Long = 0

    @Column(name = "RFC_ID", nullable = false)
    @Basic
    var rfcId: Long = 0

    @Column(name = "DECLARED_HS_CODE", nullable = false, length = 100)
    @Basic
    var declaredHsCode: String? = null

    @Column(name = "ITEM_QUANTITY", nullable = true, length = 1000)
    @Basic
    var itemQuantity: String? = null

    @Column(name = "PRODUCT_DESCRIPTION", nullable = true, length = 1000)
    @Basic
    var productDescription: String? = null

    @Column(name = "OWNER_PIN", nullable = true, length = 100)
    @Basic
    var ownerPin: String? = null

    @Column(name = "OWNER_NAME", nullable = true, length = 1000)
    @Basic
    var ownerName: String? = null

    @Column(name = "STATUS", nullable = true, precision = 0)
    @Basic
    var status: Long? = null

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

    @Column(name = "CREATED_BY", nullable = true, length = 100)
    @Basic
    var createdBy: String? = null

    @Column(name = "CREATED_ON", nullable = true)
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


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as RfcItemEntity
        return id == that.id &&
                rfcId == that.rfcId &&
                declaredHsCode == that.declaredHsCode &&
                itemQuantity == that.itemQuantity &&
                productDescription == that.productDescription &&
                ownerPin == that.ownerPin &&
                ownerName == that.ownerName &&
                status == that.status &&
                varField1 == that.varField1 &&
                varField2 == that.varField2 &&
                varField3 == that.varField3 &&
                varField4 == that.varField4 &&
                varField5 == that.varField5 &&
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
            rfcId,
            declaredHsCode,
            itemQuantity,
            productDescription,
            ownerPin,
            ownerName,
            status,
            varField1,
            varField2,
            varField3,
            varField4,
            varField5,
            createdBy,
            createdOn,
            modifiedBy,
            modifiedOn,
            deleteBy,
            deletedOn
        )
    }

}