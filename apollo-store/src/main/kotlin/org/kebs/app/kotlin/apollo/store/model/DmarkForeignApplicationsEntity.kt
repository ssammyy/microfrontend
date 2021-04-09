package org.kebs.app.kotlin.apollo.store.model

import org.kebs.app.kotlin.apollo.store.model.qa.PermitTypesEntity
import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_DMARK_FOREIGN_APPLICATIONS")
class DmarkForeignApplicationsEntity : Serializable {
    @Column(name = "ID")
    @Id
    @SequenceGenerator(name = "DAT_DMARK_FOREIGN_APPLICATIONS_SEQ_GEN", sequenceName = "DAT_KEBS_PERMIT_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_DMARK_FOREIGN_APPLICATIONS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long? = 0

    @Column(name = "FIRST_NAME")
    @Basic
    var firstName: String? = null

    @Column(name = "EXTRA_DOCUMENTS")
    @Basic
    var extraDocuments: String? = null

    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    @ManyToOne
    var userId: UsersEntity? = null

    @Column(name = "COMPANY_REPRESENTATIVE_EMAIL")
    @Basic
    var companyRepresentativeEmail: String? = null

    @Column(name = "LAST_NAME")
    @Basic
    var lastName: String? = null

    @Column(name = "SITE_APPLICABLE")
    @Basic
    var siteApplicable: String? = null

    @Column(name = "USER_NAME")
    @Basic
    var userName: String? = null

    @Column(name = "EMAIL")
    @Basic
    var email: String? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null

    @Column(name = "KS_NUMBER")
    @Basic
    var ksNumber: String? = null

    @Column(name = "APPROVED_DATE")
    @Basic
    var approvedDate: Timestamp? = null

    @Column(name = "EXPIRY_DATE")
    @Basic
    var expiryDate: Date? = null

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

//    @Column(name = "PRODUCT_CATEGORY")
//    @Basic
//    var productCategory: String? = null

    @Column(name = "DESCRIPTION")
    @Basic
    var description: String? = null

    @Column(name = "TRADE_MARK")
    @Basic
    var tradeMark: String? = null

    @Column(name = "SAVE_REASON")
    @Basic
    var saveReason: String? = null

//    @Column(name = "BROAD_PRODUCT_CATEGORY")
//    @Basic
//    var broadProductCategory: String? = null

    @Column(name = "STANDARD_TITLE")
    @Basic
    var standardTitle: String? = null

    @Column(name = "VALIDITY_PERIOD")
    @Basic
    var validityPeriod: String? = null

    @Column(name = "PAYMENT_STATUS")
    @Basic
    var paymentStatus: Int? = null

    @Column(name = "DATE_AWARDED")
    @Basic
    var dateAwarded: Date? = null

    @Column(name = "PERMIT_NUMBER")
    @Basic
    var permitNumber: String? = null

    @Column(name = "MANUFACTURER_COMPANY_NAME")
    @Basic
    var manufacturerCompanyName: String? = null

    @Column(name = "MANUFACTURER_ID")
    @Basic
    var manufacturerId: Long? = null


    @Column(name = "COMPANY_REPRESENTATIVE")
    @Basic
    var companyRepresentative: String? = null

    @Column(name = "PHYSICAL_ADDRESS")
    @Basic
    var physicalAddress: String? = null

    @Column(name = "STREET_ADDRESS")
    @Basic
    var streetAddress: String? = null

    @Column(name = "BUILDING")
    @Basic
    var building: String? = null

    @Column(name = "CITY")
    @Basic
    var city: String? = null

    @Column(name = "STATE")
    @Basic
    var state: String? = null

    @Column(name = "ZIP_CODE")
    @Basic
    var zipCode: String? = null

    @Column(name = "COUNTRY")
    @Basic
    var country: String? = null

    @Column(name = "FILE_NAME")
    @Basic
    var fileName: String? = null


//    @Column(name = "LAB_REPORT_BLOB")
//    @Basic
//    var labReportBlob: ByteArray? = null

    @JoinColumn(name = "PERMIT_TYPE", referencedColumnName = "ID")
    @ManyToOne
    var permitType: PermitTypesEntity? = null

    @Column(name = "MANUFACTURER")
    @Basic
    var manufacturer: Long? = null

    /**
     *
     */
//    @JoinColumn(name = "PRODUCT_SUB_CATEGORY", referencedColumnName = "ID")
//    @ManyToOne
//    var productSubCategory: ProductSubcategoryEntity? = null
    @Column(name = "PRODUCT_SUB_CATEGORY")
    @Basic
    var productSubCategory: Long? = null

    @Column(name = "NO_OF_SITES_PRODUCING_THE_PRODUCT")
    @Basic
    var noOfSitesProducingTheProduct: Long? = null

//    @JoinColumn(name = "PRODUCT_CATEGORY", referencedColumnName = "ID")
//    @ManyToOne
//    var productCategory: KebsProductCategoriesEntity? = null

    @Column(name = "PRODUCT_CATEGORY")
    @Basic
    var productCategory: Long? = null


//    @JoinColumn(name = "BROAD_PRODUCT_CATEGORY", referencedColumnName = "ID")
//    @ManyToOne
//    var broadProductCategory: BroadProductCategoryEntity? = null

    @Column(name = "BROAD_PRODUCT_CATEGORY")
    @Basic
    var broadProductCategory: Long? = null

//    @JoinColumn(name = "PRODUCT", referencedColumnName = "ID")
//    @ManyToOne
//    var product: ProductsEntity? = null
    @Column(name = "PRODUCT")
    @Basic
    var product: Long? = null

    @Column(name = "KEBS_PRODUCT_CATEGORY")
    @Basic
    var kebsProductCategory: String? = null

    @Column(name = "LAB_REPORTS_FILEPATH_1")
    @Basic
    var labReportsFilepath1: String? = null

    @Column(name = "LAB_REPORTS_FILEPATH_2")
    @Basic
    var labReportsFilepath2: String? = null

    @Column(name = "LAB_REPORTS_FILEPATH_3")
    @Basic
    var labReportsFilepath3: String? = null


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as DmarkForeignApplicationsEntity
        return id == that.id &&
                siteApplicable == that.siteApplicable &&
                userId == that.userId &&
                labReportsFilepath3 == that.labReportsFilepath3 &&
                labReportsFilepath2 == that.labReportsFilepath2 &&
                fileName == that.fileName &&
                kebsProductCategory == that.kebsProductCategory &&
                labReportsFilepath1 == that.labReportsFilepath1 &&
                product == that.product &&
                noOfSitesProducingTheProduct == that.noOfSitesProducingTheProduct &&
                productSubCategory == that.productSubCategory &&
                zipCode == that.zipCode &&
                state == that.state &&
                city == that.city &&
                building == that.building &&
                streetAddress == that.streetAddress &&
                companyRepresentativeEmail == that.companyRepresentativeEmail &&
                physicalAddress == that.physicalAddress &&
                companyRepresentative == that.companyRepresentative &&
                manufacturerCompanyName == that.manufacturerCompanyName &&
                manufacturer == that.manufacturer &&
                permitType == that.permitType &&
                firstName == that.firstName &&
                lastName == that.lastName &&
                userName == that.userName &&
                email == that.email &&
                status == that.status &&
                ksNumber == that.ksNumber &&
                approvedDate == that.approvedDate &&
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
                productCategory == that.productCategory &&
                description == that.description &&
                tradeMark == that.tradeMark &&
                saveReason == that.saveReason &&
                broadProductCategory == that.broadProductCategory &&
                standardTitle == that.standardTitle &&
                validityPeriod == that.validityPeriod &&
                paymentStatus == that.paymentStatus &&
                dateAwarded == that.dateAwarded &&
                permitNumber == that.permitNumber
    }

    override fun hashCode(): Int {
        return Objects.hash(id, product, userId, siteApplicable, labReportsFilepath2, labReportsFilepath3, fileName, kebsProductCategory, labReportsFilepath1, productSubCategory, firstName, streetAddress, building, city, state, zipCode, companyRepresentativeEmail, lastName, userName, email, status, ksNumber, approvedDate, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn, productCategory, description, tradeMark, saveReason, broadProductCategory, standardTitle, validityPeriod, paymentStatus, dateAwarded, permitNumber, permitType, manufacturer, manufacturerCompanyName, companyRepresentative, physicalAddress)
    }
}