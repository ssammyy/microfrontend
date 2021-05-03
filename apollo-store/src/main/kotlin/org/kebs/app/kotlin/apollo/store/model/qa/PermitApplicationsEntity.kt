package org.kebs.app.kotlin.apollo.store.model.qa

import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import java.io.Serializable
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp
import javax.persistence.*


@Entity
@Table(name = "DAT_KEBS_PERMIT_TRANSACTION")
class PermitApplicationsEntity:Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_PERMIT_TRANSACTION_SEQ_GEN", allocationSize = 1, sequenceName = "DAT_KEBS_PERMIT_TRANSACTION_SEQ")
    @GeneratedValue(generator = "DAT_KEBS_PERMIT_TRANSACTION_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = 0

    @Column(name = "APPLICANT_NAME")
    @Basic
    var applicantName: String? = null

    @Column(name = "FIRM_NAME")
    @Basic
    var firmName: String? = null

    @Column(name = "POSTAL_ADDRESS")
    @Basic
    var postalAddress: String? = null

    @Column(name = "POSITION")
    @Basic
    var position: String? = null

    @Column(name = "PERMIT_NUMBER")
    @Basic
    var permitNumber: String? = null

    @Column(name = "COMMODITY_DESCRIPTION")
    @Basic
    var commodityDescription: String? = null

    @Column(name = "TELEPHONE_NO")
    @Basic
    var telephoneNo: String? = null

    @Column(name = "FAX_NO")
    @Basic
    var faxNo: String? = null

    @Column(name = "EMAIL")
    @Basic
    var email: String? = null

    @Column(name = "PHYSICAL_ADDRESS")
    @Basic
    var physicalAddress: String? = null

    @Column(name = "DESCRIPTION")
    @Basic
    var description: String? = null

    @Column(name = "PLOT_NO")
    @Basic
    var plotNo: String? = null


    @Column(name = "DESIGNATION")
    @Basic
    var designation: String? = null



    @Column(name = "VAT_NO")
    @Basic
    var vatNo: String? = null


    @Column(name = "region")
    @Basic
    var region: String? = null

    @Column(name = "INSPECTION_DATE")
    @Basic
    var inspectionDate: Date? = null

    @Column(name = "INSPECTION_SCHEDULED_STATUS")
    @Basic
    var inspectionScheduledStatus: Int? = null

    @Column(name = "GENERATE_SCHEME_STATUS")
    @Basic
    var generateSchemeStatus: Int? = null

    @Column(name = "COMPLIANT_STATUS")
    @Basic
    var compliantStatus: Int? = null


//    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
//    @ManyToOne
//    var userId: UsersEntity? = null

//    @JoinColumn(name = "PERMIT_TYPE", referencedColumnName = "ID")
//    @ManyToOne
//    var permitType: PermitTypesEntity? = null


    @Column(name = "USER_ID")
    @Basic
    var userId: Long? = null

    @Column(name = "PERMIT_TYPE")
    @Basic
    var permitType: Long? = null

    @Column(name = "DIVISION_ID")
    @Basic
    var divisionId: Long? = null

    @Column(name = "SECTION_ID")
    @Basic
    var sectionId: Long? = null


    @Column(name = "KS_NUMBER")
    @Basic
    var ksNumber: String? = null


    @Column(name = "DATE_OF_ISSUE")
    @Basic
    var dateOfIssue: Timestamp? = null

    @Column(name = "DATE_OF_EXPIRY")
    @Basic
    var dateOfExpiry: Timestamp? = null

    @Column(name = "SUSPENSION_STATUS")
    @Basic
    var applicationSuspensionStatus: Int? = null

    @Column(name = "PRODUCT_NAME")
    @Basic
    var productName: String? = null



    @Column(name = "TRADE_MARK")
    @Basic
    var tradeMark: String? = null


    @Column(name = "STA10_FILLED_STATUS")
    @Basic
    var sta10FilledStatus: Int? = null

    @Column(name = "STA10_FILLED_OFFICER_STATUS")
    @Basic
    var sta10FilledOfficerStatus: Int? = null

    @Column(name = "QAM_ID")
    @Basic
    var qamId: Long? = null

    @Column(name = "HOD_ID")
    @Basic
    var hodId: Long? = null

    @Column(name = "RM_ID")
    @Basic
    var rmId: Long? = null

    @Column(name = "HOF_ID")
    @Basic
    var hofId: Long? = null

    @Column(name = "QAO_ID")
    @Basic
    var qaoId: Long? = null

    @Column(name = "STA3_FILLED_STATUS")
    @Basic
    var sta3FilledStatus: Int? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null

    @Column(name = "END_OF_PRODUCTION_STATUS")
    @Basic
    var endOfProductionStatus: Int? = null

    @Column(name = "FMARK_GENERATED")
    @Basic
    var fmarkGenerated: Int? = null

    @Column(name = "PERMIT_FOREIGN_STATUS")
    @Basic
    var permitForeignStatus: Int? = null

    @Column(name = "ENABLED")
    @Basic
    var enabled: Int? = null

    @Column(name = "INVOICE_GENERATED")
    @Basic
    var invoiceGenerated: Int? = null


    @Column(name = "SME_FORM_FILLED_STATUS")
    @Basic
    var smeFormFilledStatus: Int? = null

    @Column(name = "SEND_APPLICATION")
    @Basic
    var sendApplication: Int? = null

    @Column(name = "PAID_STATUS")
    @Basic
    var paidStatus: Int? = null

    @Column(name = "ASSIGN_OFFICER_STATUS")
    @Basic
    var assignOfficerStatus: Int? = null


    @Column(name = "TITLE")
    @Basic
    var title: String? = null

    @Column(name = "HOF_QAM_COMPLETENESS_STATUS")
    @Basic
    var hofQamCompletenessStatus: Int? = null


    @Column(name = "HOF_QAM_COMPLETENESS_REMARKS")
    @Basic
    var hofQamCompletenessRemarks: String? = null

    //A Declaration Form

    @Column(name = "TOTAL_COST")
    @Basic
    var totalCost: BigDecimal? = null


    @Column(name = "TOTAL_PAYMENT")
    @Basic
    var totalPayment: BigDecimal? = null


    @Column(name = "ANNUAL_TURNOVER")
    @Basic
    var annualTurnOver: BigDecimal? = null


    @Column(name = "INSPECTOR_REMARKS")
    @Basic
    var inspectorsRemark: String? = null

    @Column(name = "ATTACHED_PLANT_REMARKS")
    @Basic
    var attachedPlantRemarks: String? = null

    @Column(name = "INSPECTOR_NAME")
    @Basic
    var inspectorsName: String? = null

    @Column(name = "DATE_OF_VISIT")
    @Basic
    var dateOfVisit :Timestamp? = null

    @Column(name = "PRODUCT_CATEGORY")
    @Basic
    var productCategory: Long? = null

    @Column(name = "BROAD_PRODUCT_CATEGORY")
    @Basic
    var broadProductCategory: Long? = null

    @Column(name = "PRODUCT")
    @Basic
    var product: Long? = null

    @Column(name = "VERSION_NUMBER")
    @Basic
    var versionNumber: Long? = null

    @Column(name = "PRODUCT_STANDARD")
    @Basic
    var productStandard: Long? = null

    @Column(name = "STANDARD_CATEGORY")
    @Basic
    var standardCategory: Long? = null

    @Column(name = "PRODUCT_SUB_CATEGORY")
    @Basic
    var productSubCategory: Long? = null

    @Column(name = "ATTACHED_PLANT_ID")
    @Basic
    var attachedPlantId: Long? = null


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
        if (javaClass != other?.javaClass) return false

        other as PermitApplicationsEntity

        if (id != other.id) return false
        if (applicantName != other.applicantName) return false
        if (firmName != other.firmName) return false
        if (permitNumber != other.permitNumber) return false
        if (description != other.description) return false
        if (postalAddress != other.postalAddress) return false
        if (position != other.position) return false
        if (telephoneNo != other.telephoneNo) return false
        if (faxNo != other.faxNo) return false
        if (email != other.email) return false
        if (physicalAddress != other.physicalAddress) return false
        if (plotNo != other.plotNo) return false
        if (designation != other.designation) return false
        if (vatNo != other.vatNo) return false
        if (productCategory != other.productCategory) return false
        if (broadProductCategory != other.broadProductCategory) return false
        if (product != other.product) return false
        if (versionNumber != other.versionNumber) return false
        if (divisionId != other.divisionId) return false
        if (sectionId != other.sectionId) return false
        if (standardCategory != other.standardCategory) return false
        if (productStandard != other.productStandard) return false
        if (productSubCategory != other.productSubCategory) return false
        if (attachedPlantId != other.attachedPlantId) return false
        if (attachedPlantId != other.attachedPlantId) return false
        if (userId != other.userId) return false
        if (qamId != other.qamId) return false
        if (hodId != other.hodId) return false
        if (rmId != other.rmId) return false
        if (hofId != other.hofId) return false
        if (qaoId != other.qaoId) return false
        if (permitType != other.permitType) return false
        if (ksNumber != other.ksNumber) return false
        if (commodityDescription != other.commodityDescription) return false
        if (dateOfIssue != other.dateOfIssue) return false
        if (dateOfExpiry != other.dateOfExpiry) return false
        if (applicationSuspensionStatus != other.applicationSuspensionStatus) return false
        if (attachedPlantRemarks != other.attachedPlantRemarks) return false
        if (productName != other.productName) return false
        if (tradeMark != other.tradeMark) return false
        if (hofQamCompletenessRemarks != other.hofQamCompletenessRemarks) return false
        if (hofQamCompletenessStatus != other.hofQamCompletenessStatus) return false
        if (compliantStatus != other.compliantStatus) return false
        if (paidStatus != other.paidStatus) return false
        if (assignOfficerStatus != other.assignOfficerStatus) return false
        if (permitForeignStatus != other.permitForeignStatus) return false
        if (status != other.status) return false
        if (sta10FilledOfficerStatus != other.sta10FilledOfficerStatus) return false
        if (generateSchemeStatus != other.generateSchemeStatus) return false
        if (sta10FilledStatus != other.sta10FilledStatus) return false
        if (inspectionDate != other.inspectionDate) return false
        if (inspectionScheduledStatus != other.inspectionScheduledStatus) return false
        if (sta3FilledStatus != other.sta3FilledStatus) return false
        if (sendApplication != other.sendApplication) return false
        if (invoiceGenerated != other.invoiceGenerated) return false
        if (fmarkGenerated != other.fmarkGenerated) return false
        if (endOfProductionStatus != other.endOfProductionStatus) return false
        if (enabled != other.enabled) return false
        if (smeFormFilledStatus != other.smeFormFilledStatus) return false
        if (title != other.title) return false
        if (totalCost != other.totalCost) return false
        if (totalPayment != other.totalPayment) return false
        if (annualTurnOver != other.annualTurnOver) return false
        if (inspectorsRemark != other.inspectorsRemark) return false
        if (inspectorsName != other.inspectorsName) return false
        if (dateOfVisit != other.dateOfVisit) return false
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

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (applicantName?.hashCode() ?: 0)
        result = 31 * result + (firmName?.hashCode() ?: 0)
        result = 31 * result + (permitNumber?.hashCode() ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + (postalAddress?.hashCode() ?: 0)
        result = 31 * result + (position?.hashCode() ?: 0)
        result = 31 * result + (telephoneNo?.hashCode() ?: 0)
        result = 31 * result + (faxNo?.hashCode() ?: 0)
        result = 31 * result + (email?.hashCode() ?: 0)
        result = 31 * result + (physicalAddress?.hashCode() ?: 0)
        result = 31 * result + (plotNo?.hashCode() ?: 0)
        result = 31 * result + (designation?.hashCode() ?: 0)
        result = 31 * result + (vatNo?.hashCode() ?: 0)
        result = 31 * result + (productCategory?.hashCode() ?: 0)
        result = 31 * result + (broadProductCategory?.hashCode() ?: 0)
        result = 31 * result + (product?.hashCode() ?: 0)
        result = 31 * result + (versionNumber?.hashCode() ?: 0)
        result = 31 * result + (divisionId?.hashCode() ?: 0)
        result = 31 * result + (sectionId?.hashCode() ?: 0)
        result = 31 * result + (attachedPlantId?.hashCode() ?: 0)
        result = 31 * result + (standardCategory?.hashCode() ?: 0)
        result = 31 * result + (productStandard?.hashCode() ?: 0)
        result = 31 * result + (productSubCategory?.hashCode() ?: 0)
        result = 31 * result + (region?.hashCode() ?: 0)
        result = 31 * result + (userId?.hashCode() ?: 0)
        result = 31 * result + (qamId?.hashCode() ?: 0)
        result = 31 * result + (hofId?.hashCode() ?: 0)
        result = 31 * result + (hodId?.hashCode() ?: 0)
        result = 31 * result + (rmId?.hashCode() ?: 0)
        result = 31 * result + (qaoId?.hashCode() ?: 0)
        result = 31 * result + (permitType?.hashCode() ?: 0)
        result = 31 * result + (ksNumber?.hashCode() ?: 0)
        result = 31 * result + (dateOfIssue?.hashCode() ?: 0)
        result = 31 * result + (dateOfExpiry?.hashCode() ?: 0)
        result = 31 * result + (applicationSuspensionStatus ?: 0)
        result = 31 * result + (attachedPlantRemarks?.hashCode() ?: 0)
        result = 31 * result + (productName?.hashCode() ?: 0)
        result = 31 * result + (commodityDescription?.hashCode() ?: 0)
        result = 31 * result + (tradeMark?.hashCode() ?: 0)
        result = 31 * result + (paidStatus?.hashCode() ?: 0)
        result = 31 * result + (hofQamCompletenessRemarks?.hashCode() ?: 0)
        result = 31 * result + (assignOfficerStatus ?: 0)
        result = 31 * result + (compliantStatus ?: 0)
        result = 31 * result + (smeFormFilledStatus ?: 0)
        result = 31 * result + (invoiceGenerated ?: 0)
        result = 31 * result + (enabled ?: 0)
        result = 31 * result + (sendApplication ?: 0)
        result = 31 * result + (permitForeignStatus ?: 0)
        result = 31 * result + (fmarkGenerated ?: 0)
        result = 31 * result + (endOfProductionStatus ?: 0)
        result = 31 * result + (status ?: 0)
        result = 31 * result + (generateSchemeStatus ?: 0)
        result = 31 * result + (hofQamCompletenessStatus ?: 0)
        result = 31 * result + (sta10FilledOfficerStatus ?: 0)
        result = 31 * result + (inspectionScheduledStatus?: 0)
        result = 31 * result + (sta10FilledStatus ?: 0)
        result = 31 * result + (sta3FilledStatus ?: 0)
        result = 31 * result + (inspectionDate?.hashCode() ?: 0)
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (totalCost?.hashCode() ?: 0)
        result = 31 * result + (totalPayment?.hashCode() ?: 0)
        result = 31 * result + (annualTurnOver?.hashCode() ?: 0)
        result = 31 * result + (inspectorsRemark?.hashCode() ?: 0)
        result = 31 * result + (inspectorsName?.hashCode() ?: 0)
        result = 31 * result + (dateOfVisit?.hashCode() ?: 0)
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


}