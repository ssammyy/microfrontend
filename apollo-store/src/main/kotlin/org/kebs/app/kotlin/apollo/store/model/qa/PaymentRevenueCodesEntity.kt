package org.kebs.app.kotlin.apollo.store.model.qa

import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "CFG_KEBS_QA_PAYMENT_REVENUE_CODES", schema = "APOLLO", catalog = "")
class PaymentRevenueCodesEntity : Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID")
    var id: Long? = null

    @Basic
    @Column(name = "REGION_ID")
    var regionId: Long? = null

    @Basic
    @Column(name = "REVENUE_CODE")
    var revenueCode: String? = null

    @Basic
    @Column(name = "REVENUE_DESCRIPTION")
    var revenueDescription: String? = null

    @Basic
    @Column(name = "PERMIT_TYPE_ID")
    var permitTypeId: Long? = null

    @Basic
    @Column(name = "VAR_FIELD_1")
    var varField1: String? = null

    @Basic
    @Column(name = "VAR_FIELD_2")
    var varField2: String? = null

    @Basic
    @Column(name = "VAR_FIELD_3")
    var varField3: String? = null

    @Basic
    @Column(name = "VAR_FIELD_4")
    var varField4: String? = null

    @Basic
    @Column(name = "VAR_FIELD_5")
    var varField5: String? = null

    @Basic
    @Column(name = "VAR_FIELD_6")
    var varField6: String? = null

    @Basic
    @Column(name = "VAR_FIELD_7")
    var varField7: String? = null

    @Basic
    @Column(name = "VAR_FIELD_8")
    var varField8: String? = null

    @Basic
    @Column(name = "VAR_FIELD_9")
    var varField9: String? = null

    @Basic
    @Column(name = "VAR_FIELD_10")
    var varField10: String? = null
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as PaymentRevenueCodesEntity
        return id == that.id && regionId == that.regionId && revenueCode == that.revenueCode && revenueDescription == that.revenueDescription && permitTypeId == that.permitTypeId && varField1 == that.varField1 && varField2 == that.varField2 && varField3 == that.varField3 && varField4 == that.varField4 && varField5 == that.varField5 && varField6 == that.varField6 && varField7 == that.varField7 && varField8 == that.varField8 && varField9 == that.varField9 && varField10 == that.varField10
    }

    override fun hashCode(): Int {
        return Objects.hash(
            id,
            regionId,
            revenueCode,
            revenueDescription,
            permitTypeId,
            varField1,
            varField2,
            varField3,
            varField4,
            varField5,
            varField6,
            varField7,
            varField8,
            varField9,
            varField10
        )
    }
}
