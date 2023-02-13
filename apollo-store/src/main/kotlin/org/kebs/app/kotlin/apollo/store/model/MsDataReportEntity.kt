package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Date
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_MS_DATA_REPORT")
class MsDataReportEntity  : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_MS_DATA_REPORT_SEQ_GEN", allocationSize = 1, sequenceName = "DAT_KEBS_MS_DATA_REPORT_SEQ")
    @GeneratedValue(generator = "DAT_KEBS_MS_DATA_REPORT_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long = 0

    @Column(name = "REFERENCE_NUMBER")
    @Basic
    var referenceNumber: String? = null

    @Column(name = "DOC_LIST")
    @Basic
    var docList: String? = null

    @Column(name = "PHYSICAL_LOCATION")
    @Basic
    var physicalLocation: String? = null

    @Column(name = "OUTLET_NAME")
    @Basic
    var outletName: String? = null

    @Column(name = "PHONE_NUMBER")
    @Basic
    var phoneNumber: String? = null

    @Column(name = "EMAIL_ADDRESS")
    @Basic
    var emailAddress: String? = null

    @Column(name = "FINAL_ACTION_ON_SIZED")
    @Basic
    var finalActionOnSized: Int? = 0

    @Column(name = "INSPECTION_DATE")
    @Basic
    var inspectionDate: Date? = null

    @Column(name = "INSPECTOR_NAME")
    @Basic
    var inspectorName: String? = null

    @Column(name = "FUNCTION")
    @Basic
    var function: String? = null

    @Column(name = "DEPARTMENT")
    @Basic
    var department: String? = null

    @Column(name = "REGION")
    @Basic
    var regionName: String? = null

    @Column(name = "TOWN")
    @Basic
    var town: String? = null

    @Column(name = "MARKET_CENTER")
    @Basic
    var marketCenter: String? = null

    @Column(name = "OUTLET_DETAILS")
    @Basic
    var outletDetails: String? = null

    @Column(name = "MOST_RECURRING_NON_COMPLIANT")
    @Basic
    var mostRecurringNonCompliant: String? = null

    @Column(name = "PERSON_MET")
    @Basic
    var personMet: String? = null

    @Column(name = "SUMMARY_FINDINGS_ACTIONS_TAKEN")
    @Basic
    var summaryFindingsActionsTaken: String? = null

    @Column(name = "SAMPLES_DRAWN_AND_SUBMITTED")
    @Basic
    var samplesDrawnAndSubmitted: String? = null

    @Column(name = "SOURCE_OF_PRODUCT_AND_EVIDENCE")
    @Basic
    var sourceOfProductAndEvidence: String? = null

    @Column(name = "FINAL_ACTION_SEIZED_GOODS")
    @Basic
    var finalActionSeizedGoods: String? = null

    @Column(name = "TOTAL_COMPLIANCE_SCORE")
    @Basic
    var totalComplianceScore: String? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int? = 0

    @Column(name = "VAR_FIELD_1")
    @Basic
    var varField1: String? = null

    @Column(name = "VAR_FIELD_2")
    @Basic
    var varField2: String? = null

    @Column(name = "VAR_FIELD_3")
    @Basic
    var varField3: String? = null

    @Column(name = "VAR_FIELD_4")
    @Basic
    var varField4: String? = null

    @Column(name = "VAR_FIELD_5")
    @Basic
    var varField5: String? = null

    @Column(name = "VAR_FIELD_6")
    @Basic
    var varField6: String? = null

    @Column(name = "VAR_FIELD_7")
    @Basic
    var varField7: String? = null

    @Column(name = "VAR_FIELD_8")
    @Basic
    var varField8: String? = null

    @Column(name = "VAR_FIELD_9")
    @Basic
    var varField9: String? = null

    @Column(name = "VAR_FIELD_10")
    @Basic
    var varField10: String? = null

    @Column(name = "CREATED_BY")
    @Basic
    var createdBy: String? = null

    @Column(name = "CREATED_ON")
    @Basic
    var createdOn: Timestamp? = null

    @Column(name = "MODIFIED_BY")
    @Basic
    var modifiedBy: String? = null

    @Column(name = "MODIFIED_ON")
    @Basic
    var modifiedOn: Timestamp? = null

    @Column(name = "DELETE_BY")
    @Basic
    var deleteBy: String? = null

    @Column(name = "DELETED_ON")
    @Basic
    var deletedOn: Timestamp? = null

    @Column(name = "MS_WORKPLAN_GENERATED_ID")
    @Basic
    var workPlanGeneratedID: Long? = null


}
