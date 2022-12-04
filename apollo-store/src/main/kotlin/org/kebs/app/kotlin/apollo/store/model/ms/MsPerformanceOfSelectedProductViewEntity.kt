package org.kebs.app.kotlin.apollo.store.model.ms

import org.kebs.app.kotlin.apollo.store.model.ms.MsComplaintsInvestigationsViewEntity
import org.kebs.app.kotlin.apollo.store.model.ms.MsPerformanceOfSelectedProductViewEntity
import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "MS_PERFORMANCE_OF_SELECTED_PRODUCT_VIEW", schema = "APOLLO", catalog = "")
class MsPerformanceOfSelectedProductViewEntity : Serializable {
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
    @Column(name = "NAME_PRODUCT")
    var nameProduct: String? = null

    @Basic
    @Column(name = "BS_NUMBER")
    var bsNumber: String? = null

    @Basic
    @Column(name = "RESULTS_ANALYSIS")
    var resultsAnalysis: Int? = null

    @Basic
    @Column(name = "ANALYSIS_DONE")
    var analysisDone: Int? = null

    @Basic
    @Column(name = "COMPLIANCE_REMARKS")
    var complianceRemarks: String? = null

    @Basic
    @Column(name = "STATUS")
    var status: String? = null

}
