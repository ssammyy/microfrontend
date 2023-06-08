package org.kebs.app.kotlin.apollo.store.model.pvc

import java.io.Serializable
import java.math.BigDecimal
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

enum class PartnerCategory(val description: String) {
    SGS("SGS"), ITS("ITS"), BV("BV"), QISJ("QISJ"), CCIC("CCIC"), COTECNA("COTECNA"),OTHER("OTHER")
}

@Entity
@Table(name = "DAT_KEBS_PVOC_PARTNER_TYPE")
class PvocPartnerTypeEntity : Serializable {

    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_PVOC_PARTNER_TYPE_SEQ_GEN", sequenceName = "DAT_KEBS_PVOC_PARTNER_TYPE_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_PVOC_PARTNER_TYPE_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long = 0

    @Column(name = "PARTNER_TYPE")
    @Basic
    var partnerType: String? = null

    @Column(name = "PARTNER_CATEGORY") // SGS, ITS, BV, QISJ, CCIC and COTECNA
    @Basic
    var partnerCategory: String? = null

    @Column(name = "TYPE_DESCRIPTION")
    @Basic
    var typeDescription: String? = null

    @Column(name = "BILLING_START_DATE")
    @Basic
    var billingStartDate: Long? = null

    @Column(name = "BILLING_END_DATE")
    @Basic
    var billingEndDate: Long? = null

    @Column(name = "CHARGE_TYPE") // PERCENTAGE/FIXED
    @Basic
    var chargeType: String? = null

    @Column(name = "CHARGE_AMOUNT", precision = 19, scale = 2)
    var chargeAmount: BigDecimal? = null

    @Column(name = "STATUS")
    @Basic
    var status: Long? = null

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

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as PvocPartnerTypeEntity
        return id == that.id &&
                partnerType == that.partnerType &&
                partnerCategory == that.partnerCategory
    }

    override fun hashCode(): Int {
        return Objects.hash(id, status, varField1, varField2, varField3, varField4, varField5, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn)
    }
}