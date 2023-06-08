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
@Table(name = "OUTLET_VISITED_AND_SUMMARY_OF_FINDINGS_VIEW", schema = "APOLLO", catalog = "")
class OutletVisitedAndSummaryOfFindingsViewEntity : Serializable {

    @Basic
    @Column(name = "INSPECTION_DATE")
    var inspectionDate: String? = null

    @Basic
    @Column(name = "OUTLET_NAME")
    var outletName: String? = null

    @Basic
    @Column(name = "OUTLET_DETAILS")
    var outletDetails: String? = null

    @Basic
    @Column(name = "PHONE_NUMBER")
    var phoneNumber: String? = null

    @Basic
    @Column(name = "EMAIL_ADDRESS")
    var emailAddress: String? = null

    @Basic
    @Column(name = "PERSON_MET")
    var personMet: String? = null

    @Basic
    @Column(name = "SUMMARY_FINDINGS_ACTIONS_TAKEN")
    var summaryFindingsActionsTaken: String? = null

    @Basic
    @Column(name = "REMARKS")
    var remarks: String? = null

    @Basic
    @Column(name = "MS_WORKPLAN_GENERATED_ID")
    var msWorkplanGeneratedId: String? = null

    @Id
    @Column(name = "ID")
    var id: String? = null

}
