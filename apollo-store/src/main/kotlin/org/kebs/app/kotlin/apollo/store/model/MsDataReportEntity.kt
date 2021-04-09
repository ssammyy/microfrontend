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
    var id: Long? = 0

    @Column(name = "REFERENCE_NUMBER")
    @Basic
    var referenceNumber: String? = null

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

    @Column(name = "PERSON_MET")
    @Basic
    var personMet: String? = null

    @Column(name = "SUMMARY_FINDINGS_ACTIONS_TAKEN")
    @Basic
    var summaryFindingsActionsTaken: String? = null

    @Column(name = "FINAL_ACTION_SEIZED_GOODS")
    @Basic
    var finalActionSeizedGoods: String? = null

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

    @JoinColumn(name = "MS_WORKPLAN_GENERATED_ID", referencedColumnName = "ID")
    @ManyToOne
    var workPlanGeneratedID: MsWorkPlanGeneratedEntity? = null

//    @OneToMany(mappedBy = "datKebsMsDataReportByDataReportId")
//    var datKebsMsDataReportParametersById: Collection<MsDataReportParametersEntity>? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as MsDataReportEntity
        return id == that.id &&
                referenceNumber == that.referenceNumber &&
                inspectionDate == that.inspectionDate &&
                inspectorName == that.inspectorName &&
                function == that.function &&
                department == that.department &&
                regionName == that.regionName &&
                town == that.town &&
                marketCenter == that.marketCenter &&
                outletDetails == that.outletDetails &&
                personMet == that.personMet &&
                summaryFindingsActionsTaken == that.summaryFindingsActionsTaken &&
                finalActionSeizedGoods == that.finalActionSeizedGoods &&
                status == that.status &&
                varField1 == that.varField1 &&
                varField2 == that.varField2 &&
                varField3 == that.varField3 &&
                varField4 == that.varField4 &&
                varField5 == that.varField5 &&
                varField6 == that.varField6 &&
                varField7 == that.varField7 &&
                varField8 == that.varField8 &&
                varField9 == that.varField9 &&
                varField10 == that.varField10 &&
                createdBy == that.createdBy &&
                createdOn == that.createdOn &&
                modifiedBy == that.modifiedBy &&
                modifiedOn == that.modifiedOn &&
                deleteBy == that.deleteBy &&
                deletedOn == that.deletedOn
    }

    override fun hashCode(): Int {
        return Objects.hash(id, referenceNumber, inspectionDate, inspectorName, function, department, regionName, town, marketCenter, outletDetails, personMet, summaryFindingsActionsTaken, finalActionSeizedGoods, status, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn)
    }

}