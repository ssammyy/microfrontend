package org.kebs.app.kotlin.apollo.store.model.qa

import java.io.Serializable
import java.sql.Timestamp
import javax.persistence.*


@Entity
@Table(name = "DAT_KEBS_PERMIT_TRANSACTION_MIGRATION_DMARK")
class PermitMigrationApplicationsEntityDmark : Serializable {
    @Column(name = "FIRM_ID")
    @SequenceGenerator(
        name = "DAT_KEBS_PERMIT_TRANSACTION_SEQ_GEN",
        allocationSize = 1,
        sequenceName = "DAT_KEBS_PERMIT_TRANSACTION_SEQ"
    )
    @GeneratedValue(generator = "DAT_KEBS_PERMIT_TRANSACTION_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = 0

    @Column(name = "POSITION")
    @Basic
    var position: String? = null

    @Column(name = "VAT_NO")
    @Basic
    var vatNo: String? = null

    @Column(name = "ANNUAL_TURNOVER")
    @Basic
    var annualTurnover: String? = null

    @Column(name = "SUSPENSION_STATUS")
    @Basic
    var suspensionStatus: String? = null


    @Column(name = "DATE_OF_ISSUE")
    @Basic
    var dateOfIssue: Timestamp? = null


    @Column(name = "EFFECTIVE_DATE")
    @Basic
    var effectiveDate: Timestamp? = null

    @Column(name = "DATE_OF_VISIT")
    @Basic
    var dateOfVisit: Timestamp? = null

    @Column(name = "INSPECTOR_NAME")
    @Basic
    var inspectorName: String? = null

    @Column(name = "INSPECTOR_REMARKS")
    @Basic
    var inspectorRemarks: String? = null

    @Column(name = "KS_NUMBER")
    @Basic
    var ksNumber: String? = null

    @Column(name = "PERMIT_TYPE")
    @Basic
    var permitType: String? = null

    @Column(name = "PRODUCT_NAME")
    @Basic
    var productName: String? = null

    @Column(name = "TITLE")
    @Basic
    var title: String? = null

    @Column(name = "TOTAL_COST")
    @Basic
    var totalCost: String? = null

    @Column(name = "TOTAL_PAYMENT")
    @Basic
    var totalPayment: String? = null

    @Column(name = "TRADE_MARK")
    @Basic
    var tradeMark: String? = null

    @Column(name = "USER_ID")
    @Basic
    var userId: String? = null

    @Column(name = "PERMIT_NUMBER")
    @Basic
    var permitNumber: String? = null

    @Column(name = "DATE_OF_EXPIRY")
    @Basic
    var dateOfExpiry: Timestamp? = null

    @Column(name = "COMMODITY_DESCRIPTION")
    @Basic
    var commodityDescription: String? = null

    @Column(name = "ENABLED")
    @Basic
    var enabled: String? = null


    @Column(name = "BROAD_PRODUCT_CATEGORY")
    @Basic
    var broadProductCategory: String? = null

    @Column(name = "PRODUCT_CATEGORY")
    @Basic
    var productCategory: String? = null

    @Column(name = "PRODUCT")
    @Basic
    var product: String? = null


    @Column(name = "PRODUCT_SUB_CATEGORY")
    @Basic
    var productSubCategory: String? = null

    @Column(name = "STANDARD_CATEGORY")
    @Basic
    var standardCategory: String? = null

    @Column(name = "PRODUCT_STANDARD")
    @Basic
    var productStandard: String? = null

    @Column(name = "PERMIT_FOREIGN_STATUS")
    @Basic
    var permitForeignStatus: String? = null

    @Column(name = "END_OF_PRODUCTION_STATUS")
    @Basic
    var endOfProductionStatus: String? = null

    @Column(name = "DIVISION_ID")
    @Basic
    var divisionId: String? = null

    @Column(name = "SECTION_ID")
    @Basic
    var sectionId: String? = null

    @Column(name = "PERMIT_EXPIRED_STATUS")
    @Basic
    var permitExpiredStatus: String? = null

    @Column(name = "RENEWAL_STATUS")
    @Basic
    var renewalStatus: String? = null

    @Column(name = "COMPANY_NAME")
    @Basic
    var companyName: String? = null

    @Column(name = "ADDRESS")
    @Basic
    var address: String? = null

    @Column(name = "POSTAL_CODE")
    @Basic
    var postalCode: String? = null


    @Column(name = "PHONE")
    @Basic
    var phone: String? = null


    @Column(name = "FAX")
    @Basic
    var fax: String? = null


    @Column(name = "EMAIL")
    @Basic
    var email: String? = null

    @Column(name = "PHYSICAL_ADDRESS")
    @Basic
    var physicalAddress: String? = null

    @Column(name = "PIN_NO")
    @Basic
    var pinNo: String? = null


    @Column(name = "VAT_NO2")
    @Basic
    var vatNo2: String? = null


    @Column(name = "REGION")
    @Basic
    var region: String? = null

    @Column(name = "TYPE")
    @Basic
    var type: String? = null

    @Column(name = "MIGRATED_STATUS")
    @Basic
    var migratedStatus: String? = null

    @Column(name = "MIGRATION_REMARKS")
    @Basic
    var migrationRemarks: String? = null

    @Column(name = "MIGRATION_BY")
    @Basic
    var migrationBy: String? = null

    @Column(name = "MIGRATION_ON")
    @Basic
    var migrationOn: String? = null


}
