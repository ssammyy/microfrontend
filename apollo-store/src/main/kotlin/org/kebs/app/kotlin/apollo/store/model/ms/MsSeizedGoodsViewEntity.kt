package org.kebs.app.kotlin.apollo.store.model.ms

import org.kebs.app.kotlin.apollo.store.model.ms.MsSeizedGoodsViewEntity
import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "MS_SEIZED_GOODS_VIEW", schema = "APOLLO", catalog = "")
class MsSeizedGoodsViewEntity : Serializable {
    @Id
    @Column(name = "REFERENCE_NUMBER")
    var referenceNumber: String? = null

    @Basic
    @Column(name = "DIVISION_ID")
    var divisionId: Long? = null

    @Basic
    @Column(name = "COMPLAINT_DEPARTMENT")
    var complaintDepartment: Long? = null

    @Basic
    @Column(name = "REGION")
    var region: Long? = null

    @Basic
    @Column(name = "COUNTY")
    var county: Long? = null

    @Basic
    @Column(name = "TOWN_MARKET_CENTER")
    var townMarketCenter: Long? = null

    @Basic
    @Column(name = "DESCRIPTION_PRODUCTS_SEIZED")
    var descriptionProductsSeized: String? = null

    @Basic
    @Column(name = "QUANTITY")
    var quantity: String? = null

    @Basic
    @Column(name = "ESTIMATED_COST")
    var estimatedCost: String? = null

    @Basic
    @Column(name = "CURRENT_LOCATION")
    var currentLocation: String? = null

}
