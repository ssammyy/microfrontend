package org.kebs.app.kotlin.apollo.store.model

import org.hibernate.annotations.DynamicUpdate
import java.io.Serializable
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "STG_STANDARDS_LEVY_MANUFACTURER_PENALTY")
@DynamicUpdate
class StagingStandardsLevyManufacturerPenalty : Serializable {
    @Column(name = "ID")
    @Id
    @SequenceGenerator(name = "STG_STANDARDS_LEVY_MANUFACTURER_PENALTY_SEQ_GEN", sequenceName = "STG_STANDARDS_LEVY_MANUFACTURER_PENALTY_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "STG_STANDARDS_LEVY_MANUFACTURER_PENALTY_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long? = null

    @Column(name = "MANUFACTURER_ID")
    @Basic
    var manufacturerId: Long? = null

    @Column(name = "KRA_PIN")
    @Basic
    var kraPin: String? = null

    @Column(name = "MANUFACTURER_NAME")
    @Basic
    var manufacturerName: String? = null

    @Column(name = "RECORD_STATUS")
    @Basic
    var recordStatus: String? = null

    @Column(name = "TRANSACTION_DATE")
    @Basic
    var transactionDate: Date? = null


    @Column(name = "TRANSMISSION_DATE")
    @Basic
    var transmissionDate: Timestamp? = null

    @Column(name = "PENALTY_PAYABLE")
    @Basic
    var penaltyPayable: BigDecimal? = null

    @Column(name = "PENALTY_GENERATION_DATE")
    @Basic
    var penaltyGenerationDate: Timestamp? = null

    @Column(name = "PERIOD_FROM")
    var periodFrom: java.util.Date? = null

    @Column(name = "PERIOD_TO")
    var periodTo: java.util.Date? = null


    @Column(name = "RETRIES")
    @Basic
    var retries: Int? = null


    @Column(name = "RETRIED")
    @Basic
    var retried: Int? = null


    @Column(name = "DESCRIPTIONS")
    @Basic
    var descriptions: String? = null

    @Column(name = "DESCRIPTION")
    @Basic
    var description: String? = null


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

    @Column(name = "LAST_MODIFIED_BY")
    @Basic
    var modifiedBy: String? = null

    @Column(name = "LAST_MODIFIED_ON")
    @Basic
    var modifiedOn: Timestamp? = null

    @Column(name = "DELETE_BY")
    @Basic
    var deleteBy: String? = null

    @Column(name = "DELETED_ON")
    @Basic
    var deletedOn: Timestamp? = null


    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (manufacturerId?.hashCode() ?: 0)
        result = 31 * result + (kraPin?.hashCode() ?: 0)
        result = 31 * result + (manufacturerName?.hashCode() ?: 0)
        result = 31 * result + (recordStatus?.hashCode() ?: 0)
        result = 31 * result + (transactionDate?.hashCode() ?: 0)
        result = 31 * result + (transmissionDate?.hashCode() ?: 0)
        result = 31 * result + (retries ?: 0)
        result = 31 * result + (retried ?: 0)
        result = 31 * result + (descriptions?.hashCode() ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + (status ?: 0)
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
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StagingStandardsLevyManufacturerPenalty

        if (id != other.id) return false
        if (manufacturerId != other.manufacturerId) return false
        if (kraPin != other.kraPin) return false
        if (manufacturerName != other.manufacturerName) return false
        if (recordStatus != other.recordStatus) return false
        if (transactionDate != other.transactionDate) return false
        if (transmissionDate != other.transmissionDate) return false
        if (retries != other.retries) return false
        if (retried != other.retried) return false
        if (descriptions != other.descriptions) return false
        if (description != other.description) return false
        if (status != other.status) return false
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

        return true
    }


}
