package org.kebs.app.kotlin.apollo.store.model.di

import java.io.Serializable
import java.sql.Time
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_CD_VALUES_HEADER_LEVEL")
class CdValuesHeaderLevelEntity : Serializable{
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_CD_VALUES_HEADER_LEVEL_SEQ_GEN", sequenceName = "DAT_KEBS_CD_VALUES_HEADER_LEVEL_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_CD_VALUES_HEADER_LEVEL_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long = 0

    @Column(name = "FOREIGN_CURRENCY_CODE")
    @Basic
    var foreignCurrencyCode: String? = null

    @Column(name = "FREIGHT_FCY")
    @Basic
    var freightFcy: String? = null

    @Column(name = "TRANSACTION_TERMS")
    @Basic
    var transactionTerms: String? = null

    @Column(name = "FREIGHT_NCY")
    @Basic
    var freightNcy: String? = null

    @Column(name = "CIF_FCY")
    @Basic
    var cifFcy: String? = null

    @Column(name = "CIF_NCY")
    @Basic
    var cifNcy: String? = null

    @Column(name = "INSURANCE_NYC")
    @Basic
    var insuranceNyc: String? = null

    @Column(name = "FOREX_RATE")
    @Basic
    var forexRate: String? = null

    @Column(name = "INSURANCE_FCY")
    @Basic
    var insuranceFcy: String? = null

    @Column(name = "FOB_NCY")
    @Basic
    var fobNcy: String? = null

    @Column(name = "OTHER_CHARGES_NCY")
    @Basic
    var otherChargesNcy: String? = null

    @Column(name = "FOB_FCY")
    @Basic
    var fobFcy: String? = null

    @Column(name = "INCO_TERMS")
    @Basic
    var incoTerms: String? = null

    @Column(name = "COMESA")
    @Basic
    var comesa: String? = null

    @Column(name = "INVOICE_DATE")
    @Basic
    var invoiceDate: String? = null

    @Column(name = "INVOICE_NUMBER")
    @Basic
    var invoiceNumber: String? = null

    @Column(name = "COUNTRY_OF_SUPPLY")
    @Basic
    var countryOfSupply: String? = null

    @Column(name = "COUNTRY_OF_SUPPLY_NAME")
    @Basic
    var countryOfSupplyName: String? = null

    @Column(name = "OTHER_CHARGES_FCY")
    @Basic
    var otherChargesFcy: String? = null

    @Column(name = "DATE_SUBMITTED")
    @Basic
    var dateSubmitted: Time? = null

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
    var lastModifiedBy: String? = null

    @Column(name = "LAST_MODIFIED_ON")
    @Basic
    var lastModifiedOn: Timestamp? = null

    @Column(name = "UPDATE_BY")
    @Basic
    var updateBy: String? = null

    @Column(name = "UPDATED_ON")
    @Basic
    var updatedOn: Timestamp? = null

    @Column(name = "DELETE_BY")
    @Basic
    var deleteBy: String? = null

    @Column(name = "DELETED_ON")
    @Basic
    var deletedOn: Timestamp? = null

    @Column(name = "VERSION")
    @Basic
    var version: Long? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as CdValuesHeaderLevelEntity
        return id == that.id &&
                foreignCurrencyCode == that.foreignCurrencyCode &&
                comesa == that.comesa &&
                freightFcy == that.freightFcy &&
                transactionTerms == that.transactionTerms &&
                freightNcy == that.freightNcy &&
                cifFcy == that.cifFcy &&
                invoiceNumber == that.invoiceNumber &&
                countryOfSupply == that.countryOfSupply &&
                countryOfSupplyName == that.countryOfSupplyName &&
                incoTerms == that.incoTerms &&
                cifNcy == that.cifNcy &&
                insuranceNyc == that.insuranceNyc &&
                forexRate == that.forexRate &&
                insuranceFcy == that.insuranceFcy &&
                fobNcy == that.fobNcy &&
                otherChargesNcy == that.otherChargesNcy &&
                fobFcy == that.fobFcy &&
                otherChargesFcy == that.otherChargesFcy &&
                invoiceDate == that.invoiceDate &&
                dateSubmitted == that.dateSubmitted &&
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
                lastModifiedBy == that.lastModifiedBy &&
                lastModifiedOn == that.lastModifiedOn &&
                updateBy == that.updateBy &&
                updatedOn == that.updatedOn &&
                deleteBy == that.deleteBy &&
                deletedOn == that.deletedOn &&
                version == that.version
    }

    override fun hashCode(): Int {
        return Objects.hash(
            id,
            foreignCurrencyCode,
            freightFcy,
            freightNcy,
                transactionTerms,
                comesa,
                invoiceNumber,
                invoiceDate,
                incoTerms,
                countryOfSupply,
                countryOfSupplyName,
            cifFcy,
            cifNcy,
            insuranceNyc,
            forexRate,
            insuranceFcy,
            fobNcy,
            otherChargesNcy,
            fobFcy,
            otherChargesFcy,
            dateSubmitted,
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
            lastModifiedBy,
            lastModifiedOn,
            updateBy,
            updatedOn,
            deleteBy,
            deletedOn,
            version
        )
    }
}