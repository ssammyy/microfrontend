package org.kebs.app.kotlin.apollo.store.model.pvc

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_PVOC_WAIVERS_REPORTS")
class PvocWaiversReportsEntity : Serializable {
    @get:Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_PVOC_WAIVERS_REPORTS_SEQ_GEN", sequenceName = "DAT_KEBS_PVOC_WAIVERS_REPORTS_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_PVOC_WAIVERS_REPORTS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @get:Id
    var id: Long = 0
    @get:Column(name = "STATUS")
    @get:Basic
    var status: Int? = null
    @get:Column(name = "APPLICANT")
    @get:Basic
    var applicant: String? = null
    @get:Column(name = "WETC_SERIAL")
    @get:Basic
    var wetcSerial: String? = null
    @get:Column(name = "DESCRIPTION")
    @get:Basic
    var description: String? = null
    @get:Column(name = "APPLICANT_JUSTFICATION")
    @get:Basic
    var applicantJustfication: String? = null
    @get:Column(name = "ANALYSIS_REMARKS")
    @get:Basic
    var analysisRemarks: String? = null
    @get:Column(name = "WETC_RECOMENDATION")
    @get:Basic
    var wetcRecomendation: String? = null
    @get:Column(name = "WAIVER_ID")
    @get:Basic
    var waiverId: Long? = null
    @get:Column(name = "VAR_FIELD_1")
    @get:Basic
    var varField1: String? = null
    @get:Column(name = "VAR_FIELD_2")
    @get:Basic
    var varField2: String? = null
    @get:Column(name = "VAR_FIELD_3")
    @get:Basic
    var varField3: String? = null
    @get:Column(name = "VAR_FIELD_4")
    @get:Basic
    var varField4: String? = null
    @get:Column(name = "VAR_FIELD_5")
    @get:Basic
    var varField5: String? = null
    @get:Column(name = "VAR_FIELD_6")
    @get:Basic
    var varField6: String? = null
    @get:Column(name = "VAR_FIELD_7")
    @get:Basic
    var varField7: String? = null
    @get:Column(name = "VAR_FIELD_8")
    @get:Basic
    var varField8: String? = null
    @get:Column(name = "VAR_FIELD_9")
    @get:Basic
    var varField9: String? = null
    @get:Column(name = "VAR_FIELD_10")
    @get:Basic
    var varField10: String? = null
    @get:Column(name = "CREATED_BY")
    @get:Basic
    var createdBy: String? = null
    @get:Column(name = "CREATED_ON")
    @get:Basic
    var createdOn: Timestamp? = null
    @get:Column(name = "MODIFIED_BY")
    @get:Basic
    var modifiedBy: String? = null
    @get:Column(name = "MODIFIED_ON")
    @get:Basic
    var modifiedOn: Timestamp? = null
    @get:Column(name = "DELETE_BY")
    @get:Basic
    var deleteBy: String? = null
    @get:Column(name = "DELETED_ON")
    @get:Basic
    var deletedOn: Timestamp? = null

    @get:Column(name = "REVIEW_STATUS")
    @get:Basic
    var reviewStatus: String? = null

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as PvocWaiversReportsEntity
        return id == that.id &&
                status == that.status &&
                applicant == that.applicant &&
                wetcSerial == that.wetcSerial &&
                description == that.description &&
                applicantJustfication == that.applicantJustfication &&
                analysisRemarks == that.analysisRemarks &&
                wetcRecomendation == that.wetcRecomendation &&
                waiverId == that.waiverId &&
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
                deletedOn == that.deletedOn &&
                reviewStatus == that.reviewStatus
    }

    override fun hashCode(): Int {
        return Objects.hash(id, status, applicant, reviewStatus, wetcSerial, description, applicantJustfication, analysisRemarks, wetcRecomendation, waiverId, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn)
    }
}