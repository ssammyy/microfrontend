package org.kebs.app.kotlin.apollo.store.model.pvc

import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_PVOC_COMPLAINT")
class PvocComplaintEntity : Serializable {


    @Column(name = "ID", nullable = false, precision = 0)
    @Id
    @SequenceGenerator(name = "DAT_KEBS_PVOC_COMPLAINT_SEQ_GEN", sequenceName = "DAT_KEBS_PVOC_COMPLAINT_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_PVOC_COMPLAINT_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long? = 0

    @Column(name = "REF_PREFIX")
    @Basic
    var refPrefix: String? = null

    @Column(name = "REF_NO")
    @Basic
    var refNo: String? = null

    @Column(name = "COMPLAINT_NAME")
    @Basic
    var complaintName: String? = null

    @Column(name = "PHONE_NO")
    @Basic
    var phoneNo: String? = null

    @Column(name = "ADDRESS")
    @Basic
    var address: String? = null

    @JoinColumn(name = "PVOC_AGENT", referencedColumnName = "ID")
    @ManyToOne
    var pvocAgent: PvocPartnersEntity? = null

    @Column(name = "REVIEW_STATUS")
    @Basic
    var reviewStatus: String? = null


    @Column(name = "REVIEWED_ON")
    @Basic
    var reviewedOn: Timestamp? = null

    @Column(name = "AGENT_REVIEW_REMARKS")
    @Basic
    var agentReviewRemarks: String? = null

    @Column(name = "GENERAL_DESCRIPTION")
    @Basic
    var generalDescription: String? = null

    @Column(name = "COC_NO")
    @Basic
    var cocNo: String? = null

    @Column(name = "RFC_NO")
    @Basic
    var rfcNo: String? = null

    @Column(name = "RECOMENDATION")
    @Basic
    var recomendation: String? = null

    @Column(name = "MPVOC_RECOMENDATION_DATE")
    @Basic
    var mpvocRecomendationDate: Timestamp? = null

    @Column(name = "HOD_RECOMENDATION_DATE")
    @Basic
    var hodRecomendationDate: Timestamp? = null

    @Column(name = "EMAIL")
    @Basic
    var email: String? = null

    @JoinColumn(name = "COMPLIANT_NATURE", referencedColumnName = "ID")
    @ManyToOne
    var compliantNature: PvocComplaintCategoryEntity? = null

    @JoinColumn(name = "COMPLIANT_SUB_CATEGORY", referencedColumnName = "ID")
    @ManyToOne
    var compliantSubCategory: PvocComplaintCertificationSubCategoriesEntity? = null

    @JoinColumn(name = "MPVOC", referencedColumnName = "ID")
    @ManyToOne
    var mpvoc: UsersEntity? = null

    @JoinColumn(name = "PVOC_USER", referencedColumnName = "ID")
    @ManyToOne
    var pvocUser: UsersEntity? = null

    @JoinColumn(name = "HOD", referencedColumnName = "ID")
    @ManyToOne
    var hod: UsersEntity? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as PvocComplaintEntity
        return id == that.id &&
                status == that.status &&
                complaintName == that.complaintName &&
                phoneNo == that.phoneNo &&
                address == that.address &&
                pvocAgent == that.pvocAgent &&
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
                reviewStatus == that.reviewStatus &&
                generalDescription == that.generalDescription &&
                cocNo == that.cocNo &&
                recomendation == that.recomendation &&
                rfcNo == that.rfcNo &&
                email == that.email
    }

    override fun hashCode(): Int {
        return Objects.hash(id, status, complaintName, phoneNo, address, pvocAgent, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn, reviewStatus, generalDescription, recomendation, cocNo, rfcNo, email)
    }

}