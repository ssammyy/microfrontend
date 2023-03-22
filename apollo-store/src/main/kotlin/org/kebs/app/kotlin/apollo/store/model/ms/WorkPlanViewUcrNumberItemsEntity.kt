package org.kebs.app.kotlin.apollo.store.model.ms

import org.kebs.app.kotlin.apollo.store.model.ms.WorkPlanViewUcrNumberItemsEntity
import java.io.Serializable
import java.math.BigInteger
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "WORK_PLAN_VIEW_UCR_NUMBER_ITEMS", schema = "APOLLO", catalog = "")
class WorkPlanViewUcrNumberItemsEntity : Serializable {

    @Id
    @Column(name = "ITEM_ID")
    var itemId: Long? = null

    @Basic
    @Column(name = "UCR_NUMBER")
    var ucrNumber: String? = null

    @Basic
    @Column(name = "ITEM_DESCRIPTION")
    var itemDescription: String? = null

    @Basic
    @Column(name = "QUANTITY")
    var quantity: Long? = null

    @Basic
    @Column(name = "PACKAGE_QUANTITY")
    var packageQuantity: BigInteger? = null

    @Basic
    @Column(name = "ITEM_GROSS_WEIGHT")
    var itemGrossWeight: String? = null

    @Basic
    @Column(name = "HS_DESCRIPTION")
    var hsDescription: String? = null

    @Basic
    @Column(name = "ITEM_HS_CODE")
    var itemHsCode: String? = null

}
