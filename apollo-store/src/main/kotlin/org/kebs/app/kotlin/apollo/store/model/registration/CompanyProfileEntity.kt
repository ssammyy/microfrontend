package org.kebs.app.kotlin.apollo.store.model.registration

import java.io.Serializable
import java.math.BigDecimal
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_COMPANY_PROFILE")
class CompanyProfileEntity : Serializable {
    @Column(name = "ID")
    @Id
    @SequenceGenerator(name = "DAT_KEBS_COMPANY_PROFILE_SEQ_GEN", sequenceName = "DAT_KEBS_COMPANY_PROFILE_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_COMPANY_PROFILE_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long? = null

    @Column(name = "NAME")
    @Basic
    var name: String? = null

    @Column(name = "KRA_PIN")
    @Basic
    var kraPin: String? = null

    @Column(name = "REGISTRATION_NUMBER")
    @Basic
    var registrationNumber: String? = null

    @Column(name = "POSTAL_ADDRESS")
    @Basic
    var postalAddress: String? = null

    @Column(name = "COMPANY_EMAIL")
    @Basic
    var companyEmail: String? = null

    @Column(name = "COMPANY_TELEPHONE")
    @Basic
    var companyTelephone: String? = null

    @Column(name = "YEARLY_TURNOVER")
    @Basic
    var yearlyTurnover: BigDecimal? = null

    @Column(name = "BUSINESS_LINES")
    @Basic
    var businessLines: Long? = null

    @Column(name = "BUSINESS_NATURES")
    @Basic
    var businessNatures: Long? = null

    @Column(name = "BUILDING_NAME")
    @Basic
    var buildingName: String? = null

    @Column(name = "USER_CLASSIFICATION")
    @Basic
    var userClassification: Long? = null

    @Column(name = "STREET_NAME")
    @Basic
    var streetName: String? = null

    @Column(name = "REGION")
    @Basic
    var region: Long? = null

    @Column(name = "COUNTY")
    @Basic
    var county: Long? = null

    @Column(name = "TOWN")
    @Basic
    var town: Long? = null

    @Column(name = "USER_ID")
    @Basic
    var userId: Long? = null

    @Column(name = "DESCRIPTION")
    @Basic
    var description: String? = null

    @Column(name = "DIRECTOR_ID_NUMBER")
    @Basic
    var directorIdNumber: String?= null

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
        val that = other as CompanyProfileEntity
        return id == that.id &&
                name == that.name &&
                userId == that.userId &&
                kraPin == that.kraPin &&
                registrationNumber == that.registrationNumber &&
                postalAddress == that.postalAddress &&
                companyEmail == that.companyEmail &&
                companyTelephone == that.companyTelephone &&
                yearlyTurnover == that.yearlyTurnover &&
                businessLines == that.businessLines &&
                businessNatures == that.businessNatures &&
                buildingName == that.buildingName &&
                streetName == that.streetName &&
                userClassification == that.userClassification &&
                region == that.region &&
                county == that.county &&
                town == that.town &&
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
        return Objects.hash(
            id,
            name,
            userId,
            kraPin,
            registrationNumber,
            postalAddress,
            companyEmail,
            companyTelephone,
            yearlyTurnover,
            businessLines,
            businessNatures,
            buildingName,
            streetName,
            userClassification,
            region,
            county,
            town,
            description,
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
            deletedOn
        )
    }
}