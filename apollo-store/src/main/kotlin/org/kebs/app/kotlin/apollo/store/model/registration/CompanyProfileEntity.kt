package org.kebs.app.kotlin.apollo.store.model.registration

import java.io.Serializable
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp
import java.util.*
//import java.time.LocalDate
//import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_COMPANY_PROFILE")
class CompanyProfileEntity : Serializable {
    @Column(name = "ID")
    @Id
    @SequenceGenerator(
        name = "DAT_KEBS_COMPANY_PROFILE_SEQ_GEN",
        sequenceName = "DAT_KEBS_COMPANY_PROFILE_SEQ",
        allocationSize = 1
    )
    @GeneratedValue(generator = "DAT_KEBS_COMPANY_PROFILE_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long? = null

    @Column(name = "NAME")
    @Basic
    var name: String? = null

    @Column(name = "PHYSICAL_ADDRESS")
    @Basic
    var physicalAddress: String? = null

    @Column(name = "KRA_PIN")
    @Basic
    var kraPin: String? = null

    @Column(name = "UPDATE_DETAILS_STATUS")
    @Basic
    var updateDetailsStatus: Int? = null

    @Column(name = "UPDATE_DETAILS_COMMENT")
    @Basic
    var updateDetailsComment: String? = null

    @Column(name = "REQUESTER_COMMENT")
    @Basic
    var requesterComment: String? = null

    @Column(name = "UPDATE_FIRM_TYPE")
    @Basic
    var updateFirmType: Long? = null

    @Column(name = "REQUESTER_ID")
    @Basic
    var requesterId: Long? = null

    @Column(name = "MANUFACTURE_STATUS")
    @Basic
    var manufactureStatus: Int? = null

    @Column(name = "REGISTRATION_NUMBER")
    @Basic
    var registrationNumber: String? = null

    @Column(name = "POSTAL_ADDRESS")
    @Basic
    var postalAddress: String? = null

    @Column(name = "PLOT_NUMBER")
    @Basic
    var plotNumber: String? = null

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

    @Column(name = "FIRM_CATEGORY")
    @Basic
    var firmCategory: Long? = null


    @Column(name = "EDIT_STATUS")
    @Basic
    var editStatus: Long? = null

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
    var directorIdNumber: String? = null

    @Column(name = "ENTRY_NUMBER")
    @Basic
    var entryNumber: String? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null

    @Column(name = "SUSPENSION_STATUS")
    @Basic
    var suspensionStatus: Int? = null

    @Column(name = "CLOSURE_STATUS")
    @Basic
    var closureStatus: Int? = null

    @Column(name = "CLOSED_COMMODITY_MANUFACTURED")
    @Basic
    var closedCommodityManufactured: Int? = null

    @Column(name = "CLOSED_CONTRACTS_UNDERTAKEN")
    @Basic
    var closedContractsUndertaken: Int? = null

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

    @Column(name = "ACCENT_TO")
    @Basic
    var accentTo: Boolean? = null

    @Column(name = "TASK_ID")
    @Basic
    var taskId: String? = null

    @Column(name = "TASK_TYPE")
    @Basic
    var taskType: Long? = null

    @Column(name = "ASSIGN_STATUS")
    @Basic
    var assignStatus: Long? = null

    @Column(name = "ASSIGNED_TO")
    @Basic
    var assignedTo: Long? = null

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

    @Column(name = "FACTORY_VISIT_DATE ")
    @Basic
    var factoryVisitDate: java.sql.Date? = null

    @Column(name = "FACTORY_VISIT_STATUS ")
    @Basic
    var factoryVisitStatus: Int? = null

    @Column(name = "OWNERSHIP")
    @Basic
    var ownership: String? = null

    @Column(name = "SL_BPMN_PROCESS_INSTANCE")
    @Basic
    var slBpmnProcessInstance: String? = null

    @Column(name = "BRANCH_NAME")
    @Basic
    var branchName: String? = null

    @Column(name = "CLOSURE_OF_OPERATIONS")
    @Basic
    var closureOfOperations: Int? = null

    @Column(name = "TYPE_OF_MANUFACTURE")
    @Basic
    var typeOfManufacture: Int? = null

    @Column(name = "BUSINESS_LINE_NAME")
    @Basic
    var businessLineName: String? = null

    @Column(name = "BUSINESS_NATURE_NAME")
    @Basic
    var businessNatureName: String? = null

    @Column(name = "REGION_NAME")
    @Basic
    var regionName: String? = null

    @Column(name = "COUNTY_NAME")
    @Basic
    var countyName: String? = null

    @Column(name = "TOWN_NAME")
    @Basic
    var townName: String? = null

    @Column(name = "OTHER_BUSINESS_NATURE_TYPE")
    @Basic
    var otherBusinessNatureType: String? = null

    @Column(name = "SL_FORM_STATUS")
    @Basic
    var slFormStatus: Int? = 0

    @Column(name = "UPGRADE_TYPE")
    @Basic
    var upgradeType: Int? = 0

    @Column(name = "EXEMPTION_STATUS")
    @Basic
    var exemptionStatus: Long? = 0

}
