package org.kebs.app.kotlin.apollo.store.model.ms

import org.kebs.app.kotlin.apollo.store.model.ms.FieldInspectionSummaryReportViewEntity
import java.io.Serializable
import java.math.BigInteger
import java.sql.Date
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "FIELD_INSPECTION_SUMMARY_REPORT_VIEW", schema = "APOLLO", catalog = "")
class FieldInspectionSummaryReportViewEntity : Serializable {
    @Id
    @Column(name = "ID")
    var id: Long? = null

    @Basic
    @Column(name = "INSPECTION_DATE")
    var inspectionDate: Date? = null

    @Basic
    @Column(name = "MARKET_CENTER")
    var marketCenter: String? = null

    @Basic
    @Column(name = "OUTLET_DETAILS")
    var outletDetails: String? = null

    @Basic
    @Column(name = "COMPLAINT_DEPARTMENT")
    var complaintDepartment: Long? = null

    @Basic
    @Column(name = "OFFICER_ID")
    var officerId: Long? = null

    @Basic
    @Column(name = "TOTAL_COMPLIANCE_SCORE")
    var totalComplianceScore: String? = null

    @Basic
    @Column(name = "REPORT_DATE")
    var reportDate: Date? = null

    @Basic
    @Column(name = "DATEOF_VISIT")
    var dateofVisit: String? = null

    @Basic
    @Column(name = "DATEOF_SURVEILLANCE_REPORT")
    var dateofSurveillanceReport: String? = null

    @Basic
    @Column(name = "MARKET_CENTRE")
    var marketCentre: String? = null

    @Basic
    @Column(name = "NAME_OUTLET")
    var nameOutlet: String? = null

    @Basic
    @Column(name = "NO_SAMPLES_DRAWN_SUBMITTED")
    var noSamplesDrawnSubmitted: String? = null

    @Basic
    @Column(name = "COMPLIANCE_PHYSICAL_INSPECTION")
    var compliancePhysicalInspection: String? = null

    @Basic
    @Column(name = "MOST_RECURRING_NON_COMPLIANT")
    var mostRecurringNonCompliant: String? = null

    @Basic
    @Column(name = "PCXA")
    var pcxa: String? = null

    @Basic
    @Column(name = "SECTOR_NAME")
    var sectorName: String? = null

    @Basic
    @Column(name = "NO_OF_SAMPLES_PHYSICALLY_INSPECTED")
    var noOfSamplesPhysicallyInspected: Long? = null

    @Basic
    @Column(name = "VISIT_ASPERMS_SCHEDULE")
    var visitAspermsSchedule: String? = null

    @Basic
    @Column(name = "TIME_TAKEN_FILE_SURVEILLANCE_REPORT")
    var timeTakenFileSurveillanceReport: String? = null

    @Basic
    @Column(name = "FILING_WITHIN1_DAYAFTER_VISIT")
    var filingWithin1DayafterVisit: String? = null

}
