package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Date
import java.sql.Timestamp
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@Entity
@Table(name = "DAT_KEBS_RISK_PROFILE_DATA")
class RiskProfileDataEntity : Serializable {
    @Column(name = "ID", nullable = false, precision = 0)
    @Id
    @SequenceGenerator(name = "DAT_KEBS_RISK_PROFILE_DATA_SEQ_GEN", sequenceName = "DAT_KEBS_RISK_PROFILE_DATA_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_RISK_PROFILE_DATA_SEQ_GEN", strategy = GenerationType.SEQUENCE)

    var id: Long = 0

    @Column(name = "HS_CODE", nullable = true, length = 20)
    @Basic
    var hsCode: String? = null

    @Column(name = "BRAND_NAME", nullable = true, length = 350)
    @Basic
    var brandName: String? = null

    @Column(name = "PRODUCT_DESCRIPTION", nullable = true, length = 1000)
    @Basic
    var productDescription: String? = null

    @Column(name = "COUNTRY_OF_SUPPLY", nullable = true, length = 250)
    @Basic
    var countryOfSupply: String? = null

    @Column(name = "MANUFACTURER", nullable = true, length = 250)
    @Basic
    var manufacturer: String? = null

    @Column(name = "TRADER_NAME", nullable = true, length = 250)
    @Basic
    var traderName: String? = null

    @Column(name = "IMPORTER_NAME", nullable = true, length = 250)
    @Basic
    var importerName: String? = null

    @Column(name = "EXPORTER_NAME", nullable = true, length = 250)
    @Basic
    var exporterName: String? = null

    @NotEmpty(message = "Required field")
    @Column(name = "RISK_LEVEL", nullable = false, length = 50)
    @Basic
    var riskLevel: String? = null

    @NotEmpty(message = "Required field")
    @Column(name = "RISK_DESCRIPTION", nullable = false, length = 4000)
    @Basic
    var riskDescription: String? = null

    @Column(name = "REMARKS", nullable = true, length = 4000)
    @Basic
    var remarks: String? = null

    @NotNull(message = "Required field")
    @Column(name = "CATEGORIZATION_DATE", nullable = false)
    @Basic
    var categorizationDate: Date? = null

    @Column(name = "STATUS", nullable = true, precision = 0)
    @Basic
    var status: Long? = null

    @Column(name = "VAR_FIELD_1", nullable = true, length = 350)
    @Basic
    var varField1: String? = null

    @Column(name = "VAR_FIELD_2", nullable = true, length = 350)
    @Basic
    var varField2: String? = null

    @Column(name = "VAR_FIELD_3", nullable = true, length = 350)
    @Basic
    var varField3: String? = null

    @Column(name = "VAR_FIELD_4", nullable = true, length = 350)
    @Basic
    var varField4: String? = null

    @Column(name = "VAR_FIELD_5", nullable = true, length = 350)
    @Basic
    var varField5: String? = null

    @Column(name = "VAR_FIELD_6", nullable = true, length = 350)
    @Basic
    var varField6: String? = null

    @Column(name = "VAR_FIELD_7", nullable = true, length = 350)
    @Basic
    var varField7: String? = null

    @Column(name = "VAR_FIELD_8", nullable = true, length = 350)
    @Basic
    var varField8: String? = null

    @Column(name = "VAR_FIELD_9", nullable = true, length = 350)
    @Basic
    var varField9: String? = null

    @Column(name = "VAR_FIELD_10", nullable = true, length = 350)
    @Basic
    var varField10: String? = null

    @Column(name = "CREATED_BY", nullable = true, length = 100)
    @Basic
    var createdBy: String? = null

    @Column(name = "CREATED_ON", nullable = true)
    @Basic
    var createdOn: Timestamp? = null

    @Column(name = "MODIFIED_BY", nullable = true, length = 100)
    @Basic
    var modifiedBy: String? = null

    @Column(name = "MODIFIED_ON", nullable = true)
    @Basic
    var modifiedOn: Timestamp? = null

    @Column(name = "DELETE_BY", nullable = true, length = 100)
    @Basic
    var deleteBy: String? = null

    @Column(name = "DELETED_ON", nullable = true)
    @Basic
    var deletedOn: Timestamp? = null

    @NotEmpty(message = "Required field")
    @Column(name = "PARTNER", nullable = true, length = 50)
    @Basic
    var partner: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as RiskProfileDataEntity
        return id == that.id &&
                hsCode == that.hsCode &&
                brandName == that.brandName &&
                productDescription == that.productDescription &&
                countryOfSupply == that.countryOfSupply &&
                manufacturer == that.manufacturer &&
                traderName == that.traderName &&
                importerName == that.importerName &&
                exporterName == that.exporterName &&
                riskLevel == that.riskLevel &&
                riskDescription == that.riskDescription &&
                remarks == that.remarks &&
                categorizationDate == that.categorizationDate &&
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
                deletedOn == that.deletedOn &&
                partner == that.partner

    }

    override fun hashCode(): Int {
        return Objects.hash(
            id,
            hsCode,
            brandName,
            productDescription,
            countryOfSupply,
            manufacturer,
            traderName,
            importerName,
            exporterName,
            riskLevel,
            riskDescription,
            remarks,
            categorizationDate,
            status,
            varField1,
            varField2,
            varField3,
            varField4,
            varField5,
            varField6,
            varField7,
            varField8,
            varField9,
            varField10,
            createdBy,
            createdOn,
            modifiedBy,
            modifiedOn,
            deleteBy,
            deletedOn,
            partner

        )
    }
}