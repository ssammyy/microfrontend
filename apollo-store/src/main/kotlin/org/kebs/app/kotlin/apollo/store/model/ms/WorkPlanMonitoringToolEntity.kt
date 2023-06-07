package org.kebs.app.kotlin.apollo.store.model.ms

import org.kebs.app.kotlin.apollo.store.model.ms.WorkPlanMonitoringToolEntity
import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "WORK_PLAN_MONITORING_TOOL", schema = "APOLLO", catalog = "")
class WorkPlanMonitoringToolEntity : Serializable {
    @Basic
    @Column(name = "OFFICER_ID")
    var officerId: Long? = null

    @Basic
    @Column(name = "REGION_ID")
    var regionId: Long? = null

    @Basic
    @Column(name = "COMPLAINT_DEPARTMENT")
    var complaintDepartment: Long? = null

    @Basic
    @Column(name = "TIME_ACTIVITY_DATE")
    var timeActivityDate: Date? = null

    @Basic
    @Column(name = "TIME_ACTIVITY_END_DATE")
    var timeActivityEndDate: Date? = null


    @Id
    @Column(name = "ID")
    var id: Long? = null

    @Basic
    @Column(name = "REFERENCE_NUMBER")
    var referenceNumber: String? = null

    @Basic
    @Column(name = "TARGETED_MONTH")
    var targetedMonth: String? = null

    @Basic
    @Column(name = "PRODUCT_STRING")
    var productString: String? = null

    @Basic
    @Column(name = "OFFICERS")
    var officers: String? = null

    @Basic
    @Column(name = "REGION")
    var region: String? = null

    @Basic
    @Column(name = "COUNTY")
    var county: String? = null

    @Basic
    @Column(name = "TOWN")
    var town: String? = null

    @Basic
    @Column(name = "JULY")
    var july: String? = null

    @Basic
    @Column(name = "AUGUST")
    var august: String? = null

    @Basic
    @Column(name = "SEPTEMBER")
    var september: String? = null

    @Basic
    @Column(name = "OCTOBER")
    var october: String? = null

    @Basic
    @Column(name = "NOVEMBER")
    var november: String? = null

    @Basic
    @Column(name = "DECEMBER")
    var december: String? = null

    @Basic
    @Column(name = "JANUARY")
    var january: String? = null

    @Basic
    @Column(name = "FEBRUARY")
    var february: String? = null

    @Basic
    @Column(name = "MARCH")
    var march: String? = null

    @Basic
    @Column(name = "APRIL")
    var april: String? = null

    @Basic
    @Column(name = "MAY")
    var may: String? = null

    @Basic
    @Column(name = "JUNE")
    var june: String? = null

}
