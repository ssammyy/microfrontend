package org.kebs.app.kotlin.apollo.store.model.di

import java.io.Serializable
import java.math.BigDecimal
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_CD_DEMAND_NOTE_GROUPED_ITEMS")
class CdDemandNoteGroupedItemsEntity : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(
        name = "DAT_KEBS_CD_DEMAND_NOTE_GROUPED_ITEMS_SEQ_GEN",
        sequenceName = "DAT_KEBS_CD_DEMAND_NOTE_GROUPED_ITEMS_SEQ",
        allocationSize = 1
    )
    @GeneratedValue(generator = "DAT_KEBS_CD_DEMAND_NOTE_GROUPED_ITEMS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long = 0

    @Column(name = "ITEM_ID")
    @Basic
    var itemId: Long? = null

    @Column(name = "ITEM_GROUP_ID")
    @Basic
    var itemGroupId: Long? = null

    @Column(name = "PRODUCT")
    @Basic
    var product: String? = null

    @Column(name = "CF_AMOUNT", precision = 17, scale = 2)
    @Basic
    var cfValue: BigDecimal? = null

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

    @Column(name = "C_F_VALUE", precision = 17, scale = 2)
    @Basic
    var cfvalue: BigDecimal? = null


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as CdDemandNoteGroupedItemsEntity
        return id == that.id &&
                itemId == that.itemId &&
                product == that.product &&
                cfvalue == that.cfvalue &&
                description == that.description &&
                status == that.status &&
                varField1 == that.varField1 &&
                varField2 == that.varField2 &&
                varField3 == that.varField3
    }

    override fun hashCode(): Int {
        return Objects.hash(
            id,
            itemId,
            product,
            cfvalue,
            description,
            status,
            varField1,
            varField2,
            varField3
        )
    }
}