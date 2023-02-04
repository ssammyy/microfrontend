package org.kebs.app.kotlin.apollo.store.model.ms

import org.kebs.app.kotlin.apollo.store.model.ms.WorkPlanMonitoringSameTaskDateEntity
import java.io.Serializable
import java.sql.Date
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "WORK_PLAN_MONITORING_SAME_TASK_DATE", schema = "APOLLO", catalog = "")
class WorkPlanMonitoringSameTaskDateEntity : Serializable {
    @Id
    @Column(name = "ID")
    var id: Long? = null

    @Basic
    @Column(name = "WORKPLAN_YEAR_ID")
    var workplanYearId: Long? = null

    @Basic
    @Column(name = "TIME_ACTIVITY_END_DATE")
    var timeActivityEndDate: Date? = null

    @Basic
    @Column(name = "TIME_ACTIVITY_DATE")
    var timeActivityDate: Date? = null

    @Basic
    @Column(name = "REGION_ID")
    var regionId: Long? = null

    @Basic
    @Column(name = "OFFICER_ID")
    var officerId: Long? = null

    @Basic
    @Column(name = "OFFICER_NAME")
    var officerName: String? = null

    @Basic
    @Column(name = "COUNTY_ID")
    var countyId: Long? = null

    @Basic
    @Column(name = "TOWNS_ID")
    var townsId: Long? = null
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as WorkPlanMonitoringSameTaskDateEntity
        return id == that.id && workplanYearId == that.workplanYearId && timeActivityEndDate == that.timeActivityEndDate && regionId == that.regionId && officerId == that.officerId && officerName == that.officerName && countyId == that.countyId && townsId == that.townsId
    }

    override fun hashCode(): Int {
        return Objects.hash(
            id,
            workplanYearId,
            timeActivityEndDate,
            regionId,
            officerId,
            officerName,
            countyId,
            townsId
        )
    }
}
