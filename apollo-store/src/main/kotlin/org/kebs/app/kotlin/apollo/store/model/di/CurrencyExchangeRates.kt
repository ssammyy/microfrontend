package org.kebs.app.kotlin.apollo.store.model.di

import java.io.Serializable
import java.math.BigDecimal
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "CFG_CURRENCY_EXCHANGE_RATES")
class CurrencyExchangeRates : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "CFG_CURRENCY_EXCHANGE_RATES_SEQ_GEN", allocationSize = 1, sequenceName = "CFG_CURRENCY_EXCHANGE_RATES_SEQ")
    @GeneratedValue(generator = "CFG_CURRENCY_EXCHANGE_RATES_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = 0

    @Column(name = "CURRENCY_CODE")
    @Basic
    var currencyCode: String? = null

    @Column(name = "EXCHANGE_RATE")
    @Basic
    var exchangeRate: BigDecimal? = null

    @Column(name = "APPLICABLE_DATE")
    @Basic
    var applicableDate: Timestamp? = null


    @Column(name = "DESCRIPTION")
    @Basic
    var description: String? = null

    @Column(name = "CURRENT_RATE", nullable = false)
    @Basic
    var currentRate: Int? = null

    @Column(name = "CHANGE_DATE")
    @Basic
    var changeDate: Timestamp? = null

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
        val that = other as CurrencyExchangeRates
        return id == that.id &&
                currencyCode == that.currencyCode &&
                description == that.description &&
                status == that.status &&
                varField1 == that.varField1 &&
                varField2 == that.varField2 &&
                varField3 == that.varField3 &&
                varField4 == that.varField4 &&
                varField5 == that.varField5 &&
                createdBy == that.createdBy &&
                createdOn == that.createdOn &&
                modifiedBy == that.modifiedBy &&
                modifiedOn == that.modifiedOn &&
                deleteBy == that.deleteBy &&
                deletedOn == that.deletedOn
    }

    override fun hashCode(): Int {
        return Objects.hash(id, description, status, varField1, varField2, varField3, varField4, varField5, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn)
    }
}