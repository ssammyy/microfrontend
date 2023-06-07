package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_STD_LEVY_NOTIFICATION_FORM_TBL")
class StdLevyNotificationForm : Serializable {
    @Column(name = "ID")
    @Id
    @SequenceGenerator(name = "DAT_KEBS_STD_LEVY_NOTIFICATION_FORM_TBL_SEQ_GEN", sequenceName = "DAT_KEBS_STD_LEVY_NOTIFICATION_FORM_TBL_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_STD_LEVY_NOTIFICATION_FORM_TBL_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long? = 0

    @Column(name = "NAME_BUSINESS_PROPRIETOR")
    @Basic
    var nameBusinessProprietor: String? = null

    @Column(name = "COMMODITIES_MANUFACTURED")
    @Basic
    var commoditiesManufactured: String? = null

    @Column(name = "CHIEF_EXECUTIVE_DIRECTORS")
    @Basic
    var chiefExecutiveDirectors: String? = null

    @Column(name = "CHIEF_EXECUTIVE_DIRECTORS_STATUS")
    @Basic
    var chiefExecutiveDirectorsStatus: Int? = 0

    @Column(name = "DATE_MANUFACTURE_COMMENCED")
    @Basic
    var dateManufactureCommenced: Date? = null

    @Column(name = "TOTAL_VALUE_OF_MANUFACTURE")
    @Basic
    var totalValueOfManufacture: BigDecimal? = null

    @Column(name = "DESCRIPTION")
    @Basic
    var description: String? = null

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

    @Column(name = "VAR_FIELD_6")
    @Basic
    var varField6: String? = null

    @Column(name = "VAR_FIELD_7")
    @Basic
    var varField7: String? = null

    @Column(name = "VAR_FIELD_8")
    @Basic
    var varField8: String? = null

    @Column(name = "ENTRY_NUMBER")
    @Basic
    var entryNumber: String? = null

    @Column(name = "MANUFACTURER_ID")
    @Basic
    var manufacturerId: Long? = null

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
        val that = other as StdLevyNotificationForm
        return id == that.id &&
                nameBusinessProprietor == that.nameBusinessProprietor &&
                commoditiesManufactured == that.commoditiesManufactured &&
                chiefExecutiveDirectors == that.chiefExecutiveDirectors &&
                chiefExecutiveDirectorsStatus == that.chiefExecutiveDirectorsStatus &&
                dateManufactureCommenced == that.dateManufactureCommenced &&
                totalValueOfManufacture == that.totalValueOfManufacture &&
                description == that.description &&
                status == that.status &&
                varField1 == that.varField1 &&
                varField2 == that.varField2 &&
                varField3 == that.varField3 &&
                varField4 == that.varField4 &&
                varField5 == that.varField5 &&
                varField6 == that.varField6 &&
                varField7 == that.varField7 &&
                varField8 == that.varField8 &&
                entryNumber == that.entryNumber &&
                manufacturerId == that.manufacturerId &&
                createdBy == that.createdBy &&
                createdOn == that.createdOn &&
                modifiedBy == that.modifiedBy &&
                modifiedOn == that.modifiedOn &&
                deleteBy == that.deleteBy &&
                deletedOn == that.deletedOn
    }

    override fun hashCode(): Int {
        return Objects.hash(id, nameBusinessProprietor, commoditiesManufactured, chiefExecutiveDirectors, chiefExecutiveDirectorsStatus, dateManufactureCommenced, totalValueOfManufacture, description, status, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, entryNumber, manufacturerId, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn)
    }
}