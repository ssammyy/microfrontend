package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_SCHEMES_OF_SUPERVISION")
class SchemesOfSupervisionEntity: Serializable {
    @Column(name = "ID")
    @Id
    var id: Long? = 0

    @Column(name = "STATUS")
    @Basic
    var status: Long? = null

    @Column(name = "NAME_OF_FIRM")
    @Basic
    var nameOfFirm: String? = null

    @Column(name = "ADDRESS")
    @Basic
    var address: String? = null

    @Column(name = "TELEPHONE_NUMBER")
    @Basic
    var telephoneNumber: String? = null

    @Column(name = "PRODUCTS_COVERED_UNDER_SCHEME")
    @Basic
    var productsCoveredUnderScheme: String? = null

    @Column(name = "BRAND")
    @Basic
    var brand: String? = null

    @Column(name = "APPLICABLE_STANDARDS")
    @Basic
    var applicableStandards: String? = null

    @Column(name = "COMPANY_REPRESENTATIVE")
    @Basic
    var companyRepresentative: String? = null

    @Column(name = "DESIGNATION")
    @Basic
    var designation: String? = null

    @Column(name = "COMPANY_NAME")
    @Basic
    var companyName: String? = null

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
        val that = other as SchemesOfSupervisionEntity
        return id == that.id &&
                status == that.status &&
                nameOfFirm == that.nameOfFirm &&
                address == that.address &&
                telephoneNumber == that.telephoneNumber &&
                productsCoveredUnderScheme == that.productsCoveredUnderScheme &&
                brand == that.brand &&
                applicableStandards == that.applicableStandards &&
                companyRepresentative == that.companyRepresentative &&
                designation == that.designation &&
                companyName == that.companyName &&
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
                deletedOn == that.deletedOn
    }

    override fun hashCode(): Int {
        return Objects.hash(id, status, nameOfFirm, address, telephoneNumber, productsCoveredUnderScheme, brand, applicableStandards, companyRepresentative, designation, companyName, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn)
    }
}