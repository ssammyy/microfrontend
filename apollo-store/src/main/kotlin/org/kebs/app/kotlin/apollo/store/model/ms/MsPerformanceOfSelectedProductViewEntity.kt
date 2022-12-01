package org.kebs.app.kotlin.apollo.store.model.ms

import org.kebs.app.kotlin.apollo.store.model.ms.MsComplaintsInvestigationsViewEntity
import org.kebs.app.kotlin.apollo.store.model.ms.MsPerformanceOfSelectedProductViewEntity
import java.io.Serializable
import java.math.BigInteger
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
    var divisionId: BigInteger? = null

    @Basic
    @Column(name = "COMPLAINT_DEPARTMENT")
    var complaintDepartment: BigInteger? = null

    @Basic
    @Column(name = "REGION")
    var region: BigInteger? = null

    @Basic
    @Column(name = "COUNTY")
    var county: BigInteger? = null

    @Basic
    @Column(name = "TOWN_MARKET_CENTER")
    var townMarketCenter: BigInteger? = null

    @Basic
    @Column(name = "NAME_PRODUCT")
    var nameProduct: String? = null

    @Basic
    @Column(name = "BS_NUMBER")
    var bsNumber: String? = null

    @Basic
    @Column(name = "RESULTS_ANALYSIS")
    var resultsAnalysis: Byte? = null

    @Basic
    @Column(name = "ANALYSIS_DONE")
    var analysisDone: Byte? = null

    @Basic
    @Column(name = "COMPLIANCE_REMARKS")
    var complianceRemarks: String? = null

    @Basic
    @Column(name = "STATUS")
    var status: String? = null
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as MsPerformanceOfSelectedProductViewEntity
        return referenceNumber == that.referenceNumber && divisionId == that.divisionId && complaintDepartment == that.complaintDepartment && region == that.region && county == that.county && townMarketCenter == that.townMarketCenter && nameProduct == that.nameProduct && bsNumber == that.bsNumber && resultsAnalysis == that.resultsAnalysis && analysisDone == that.analysisDone && complianceRemarks == that.complianceRemarks && status == that.status
    }

    override fun hashCode(): Int {
        return Objects.hash(
            referenceNumber,
            divisionId,
            complaintDepartment,
            region,
            county,
            townMarketCenter,
            nameProduct,
            bsNumber,
            resultsAnalysis,
            analysisDone,
            complianceRemarks,
            status
        )
    }
}
