package org.kebs.app.kotlin.apollo.store.model.ms

import org.kebs.app.kotlin.apollo.store.model.ms.OutletVisitedAndSummaryOfFindingsViewEntity
import org.kebs.app.kotlin.apollo.store.model.ms.SummaryOfSamplesDrawnViewEntity
import java.io.Serializable
import java.util.*
import javax.persistence.Basic
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "SUMMARY_OF_SAMPLES_DRAWN_VIEW", schema = "APOLLO", catalog = "")
class SummaryOfSamplesDrawnViewEntity : Serializable {
    @Basic
    @Column(name = "FILE_REF_NUMBER")
    var fileRefNumber: String? = null

    @Basic
    @Column(name = "OUTLET_NAME")
    var nameOutlet: String? = null

    @Basic
    @Column(name = "DATA_REPORT_ID")
    var dataReportId: String? = null

    @Basic
    @Column(name = "PRODUCT_NAME")
    var productName: String? = null

    @Basic
    @Column(name = "PRODUCT_BRAND")
    var productBrand: String? = null

    @Basic
    @Column(name = "ADDRESS")
    var address: String? = null

    @Basic
    @Column(name = "COUNTRY_OF_ORIGIN")
    var countryOfOrigin: String? = null

    @Basic
    @Column(name = "EXPIRY_DATE")
    var expiryDate: String? = null

    @Basic
    @Column(name = "BATCH_NUMBER")
    var batchNumber: String? = null

    @Basic
    @Column(name = "MS_WORKPLAN_GENERATED_ID")
    var msWorkplanGeneratedId: String? = null

    @Basic
    @Column(name = "SENDERS_NAME")
    var sendersName: String? = null

    @Id
    @Column(name = "ID")
    var id: String? = null

    @Basic
    @Column(name = "SAMPLE_COLLECTION_DATE")
    var sampleCollectionDate: String? = null

    @Basic
    @Column(name = "DATE_SUBMITTED")
    var dateSubmitted: String? = null
}
