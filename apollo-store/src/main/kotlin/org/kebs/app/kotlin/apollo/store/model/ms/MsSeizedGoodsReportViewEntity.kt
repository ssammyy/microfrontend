package org.kebs.app.kotlin.apollo.store.model.ms

import org.kebs.app.kotlin.apollo.store.model.ms.MsSeizedGoodsReportViewEntity
import java.io.Serializable
import java.sql.Date
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "MS_SEIZED_GOODS_REPORT_VIEW", schema = "APOLLO", catalog = "")
class MsSeizedGoodsReportViewEntity : Serializable {
    @Id
    @Column(name = "ID")
    var id: Long? = null

    @Basic
    @Column(name = "SEIZURE_DECLARATION_ID")
    var seizureDeclarationId: Long? = null

    @Basic
    @Column(name = "SEIZED_GOODS_ID")
    var seizedGoodsId: Long? = null

    @Basic
    @Column(name = "WORKPLAN_PRODUCT_ID")
    var workplanProductId: Long? = null

    @Basic
    @Column(name = "OFFICER")
    var officer: String? = null

    @Basic
    @Column(name = "DATE_OF_SEIZURE_AS_DATE")
    var dateofSeizureAsDate: Date? = null

    @Basic
    @Column(name = "DATE_OF_SEIZURE")
    var dateofSeizure: String? = null

    @Basic
    @Column(name = "MARKET_CENTRE")
    var marketCentre: String? = null

    @Basic
    @Column(name = "PRODUCT_FIELD")
    var product: String? = null

    @Basic
    @Column(name = "NAME_OUTLET")
    var nameOutlet: String? = null

    @Basic
    @Column(name = "DESCRIPTION_PRODUCTS_SEIZED")
    var descriptionProductsSeized: String? = null

    @Basic
    @Column(name = "BRAND")
    var brand: String? = null

    @Basic
    @Column(name = "SECTOR")
    var sector: String? = null

    @Basic
    @Column(name = "QUANTITY")
    var quantity: String? = null

    @Basic
    @Column(name = "UNIT")
    var unit: String? = null

    @Basic
    @Column(name = "ESTIMATED_COST")
    var estimatedCost: String? = null

    @Basic
    @Column(name = "CURRENT_LOCATION_SEIZED_PRODUCTS")
    var currentLocationSeizedProducts: String? = null

    @Basic
    @Column(name = "PRODUCTS_DUE_FOR_DESTRUCTION")
    var productsDueForDestruction: String? = null

    @Basic
    @Column(name = "DATE_OF_DESTRUCTION_NOTIFICATION")
    var dateOfDestructionNotification: String? = null

    @Basic
    @Column(name = "ARE_PRODUCTS_DESTROYED")
    var areProductsDestroyed: String? = null

    @Basic
    @Column(name = "MS_WORKPLAN_GENERATED_ID")
    var msWorkplanGeneratedId: Long? = null
}

