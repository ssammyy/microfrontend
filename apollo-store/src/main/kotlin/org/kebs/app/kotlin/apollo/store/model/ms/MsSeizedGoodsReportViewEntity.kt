package org.kebs.app.kotlin.apollo.store.model.ms

import org.kebs.app.kotlin.apollo.store.model.ms.MsSeizedGoodsReportViewEntity
import java.io.Serializable
import java.sql.Date
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "MS_SEIZED_GOODS_REPORT_VIEW", schema = "APOLLO", catalog = "")
class   MsSeizedGoodsReportViewEntity : Serializable {
    @Id
    @Column(name = "ID")
    var id: Long? = null

    @Basic
    @Column(name = "DATEOF_SEIZURE")
    var dateofSeizure: String? = null

    @Basic
    @Column(name = "MARKET_CENTRE")
    var marketCentre: String? = null

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
    @Column(name = "PRODUCT")
    var product: String? = null

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
    @Column(name = "PRODUCTS_DUE_FOR_RELEASE")
    var productsDueForRelease: String? = null

    @Basic
    @Column(name = "DATEOF_DESTRUCTED")
    var dateofDestructed: String? = null

    @Basic
    @Column(name = "DATEOF_RELEASE")
    var dateofRelease: String? = null

    @Basic
    @Column(name = "DATE_SEIZURE")
    var dateSeizure: Date? = null

    @Basic
    @Column(name = "DATE_DESTRUCTED")
    var dateDestructed: Date? = null

    @Basic
    @Column(name = "DATE_RELEASE")
    var dateRelease: Date? = null

}
