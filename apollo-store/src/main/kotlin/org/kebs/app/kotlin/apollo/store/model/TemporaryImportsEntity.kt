package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_CD_TEMPORARY_IMPORTS")
class TemporaryImportsEntity: Serializable {

    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_CD_TEMPORARY_IMPORTS_SEQ_GEN", allocationSize = 1, sequenceName = "DAT_KEBS_CD_TEMPORARY_IMPORTS_SEQ")
    @GeneratedValue(generator = "DAT_KEBS_CD_TEMPORARY_IMPORTS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long = 0
    @Column(name = "STATUS")
    @Basic
    var status: Int? = null

    @Column(name = "ASSIGNED_STATUS")
    @Basic
    var assignedStatus: Int? = null

    @Column(name = "APPROVAL_STATUS")
    @Basic
    var approvalStatus: String? = null

    @Column(name = "ENTRY_NUMBER")
    @Basic
    var entryNumber: String? = null
    @Column(name = "ENTRY_POINT")
    @Basic
    var entryPoint: String? = null
    @Column(name = "UCR_NUMBER")
    @Basic
    var ucrNumber: String? = null
    @Column(name = "BILL_OF_LANDING")
    @Basic
    var billOfLanding: String? = null
    @Column(name = "DESCRIPTION_GOODS")
    @Basic
    var goodsDescription: String? = null

    @Column(name = "IDF_NUMBER")
    @Basic
    var idfNumber: String? = null

    @Column(name = "KRA_BOND")
    @Basic
    var kraBond: String? = null

    @Column(name = "IMPORTATION_DOC")
    @Basic
    var importationDOC: String? = null


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

    @JoinColumn(name = "APPLICANT_ID", referencedColumnName = "ID")
    @ManyToOne
    var applicantId: CdApplicantDetailsEntity? = null

    @JoinColumn(name = "ASSIGN_IO_ID", referencedColumnName = "ID")
    @ManyToOne
    var assignIoId: UsersEntity? = null
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TemporaryImportsEntity

        if (id != other.id) return false
        if (status != other.status) return false
        if (assignedStatus != other.assignedStatus) return false
        if (approvalStatus != other.approvalStatus) return false
        if (entryNumber != other.entryNumber) return false
        if (entryPoint != other.entryPoint) return false
        if (ucrNumber != other.ucrNumber) return false
        if (billOfLanding != other.billOfLanding) return false
        if (goodsDescription != other.goodsDescription) return false
        if (idfNumber != other.idfNumber) return false
        if (kraBond != other.kraBond) return false
        if (importationDOC != other.importationDOC) return false
        if (varField1 != other.varField1) return false
        if (varField2 != other.varField2) return false
        if (varField3 != other.varField3) return false
        if (varField4 != other.varField4) return false
        if (varField5 != other.varField5) return false
        if (varField6 != other.varField6) return false
        if (varField7 != other.varField7) return false
        if (varField8 != other.varField8) return false
        if (varField9 != other.varField9) return false
        if (varField10 != other.varField10) return false
        if (createdBy != other.createdBy) return false
        if (createdOn != other.createdOn) return false
        if (modifiedBy != other.modifiedBy) return false
        if (modifiedOn != other.modifiedOn) return false
        if (deleteBy != other.deleteBy) return false
        if (deletedOn != other.deletedOn) return false
        if (applicantId != other.applicantId) return false
        if (assignIoId != other.assignIoId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (status ?: 0)
        result = 31 * result + (assignedStatus ?: 0)
        result = 31 * result + (approvalStatus?.hashCode() ?: 0)
        result = 31 * result + (entryNumber?.hashCode() ?: 0)
        result = 31 * result + (entryPoint?.hashCode() ?: 0)
        result = 31 * result + (ucrNumber?.hashCode() ?: 0)
        result = 31 * result + (billOfLanding?.hashCode() ?: 0)
        result = 31 * result + (goodsDescription?.hashCode() ?: 0)
        result = 31 * result + (idfNumber?.hashCode() ?: 0)
        result = 31 * result + (kraBond?.hashCode() ?: 0)
        result = 31 * result + (importationDOC?.hashCode() ?: 0)
        result = 31 * result + (varField1?.hashCode() ?: 0)
        result = 31 * result + (varField2?.hashCode() ?: 0)
        result = 31 * result + (varField3?.hashCode() ?: 0)
        result = 31 * result + (varField4?.hashCode() ?: 0)
        result = 31 * result + (varField5?.hashCode() ?: 0)
        result = 31 * result + (varField6?.hashCode() ?: 0)
        result = 31 * result + (varField7?.hashCode() ?: 0)
        result = 31 * result + (varField8?.hashCode() ?: 0)
        result = 31 * result + (varField9?.hashCode() ?: 0)
        result = 31 * result + (varField10?.hashCode() ?: 0)
        result = 31 * result + (createdBy?.hashCode() ?: 0)
        result = 31 * result + (createdOn?.hashCode() ?: 0)
        result = 31 * result + (modifiedBy?.hashCode() ?: 0)
        result = 31 * result + (modifiedOn?.hashCode() ?: 0)
        result = 31 * result + (deleteBy?.hashCode() ?: 0)
        result = 31 * result + (deletedOn?.hashCode() ?: 0)
        result = 31 * result + (applicantId?.hashCode() ?: 0)
        result = 31 * result + (assignIoId?.hashCode() ?: 0)
        return result
    }


}