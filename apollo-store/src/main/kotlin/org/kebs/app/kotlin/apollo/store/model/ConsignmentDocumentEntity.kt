package org.kebs.app.kotlin.apollo.store.model

import org.kebs.app.kotlin.apollo.store.model.di.*
import java.io.Serializable
import java.sql.Time
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_CONSIGNMENT_DOCUMENT")
class ConsignmentDocumentEntity : Serializable {
    @Column(name = "ID")
    @Id
    @SequenceGenerator(name = "DAT_KEBS_CONSIGNMENT_DOCUMENT_SEQ_GEN", sequenceName = "DAT_KEBS_CONSIGNMENT_DOCUMENT_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_CONSIGNMENT_DOCUMENT_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long? = 0

    @Column(name = "DOCUMENT_NAME")
    @Basic
    var documentName: String? = null

    @Column(name = "DOCUMENT_TYPE_CODE")
    @Basic
    var documentTypeCode: String? = null

    @Column(name = "DOCUMNET_TYPE_DESCRIPTION")
    @Basic
    var documnetTypeDescription: String? = null

    @Column(name = "MASTER_APPROVAL_VERSION_NO")
    @Basic
    var masterApprovalVersionNo: String? = null

    @Column(name = "DOCUMENT_NO")
    @Basic
    var documentNo: String? = null

    @Column(name = "CMS_DOCUMENT_TYPE_CODE")
    @Basic
    var cmsDocumentTypeCode: String? = null

    @Column(name = "PROCESS")
    @Basic
    var process: String? = null

    @Column(name = "APPLICATION_REF_NO")
    @Basic
    var applicationRefNo: String? = null

    @Column(name = "APPLICATION_TYPE_CODE")
    @Basic
    var applicationTypeCode: String? = null

    @Column(name = "TERMS_AND_CONDITIONS")
    @Basic
    var termsAndConditions: String? = null

    @Column(name = "APPLICATION_TYPE_DESCRIPTION")
    @Basic
    var applicationTypeDescription: String? = null

    @Column(name = "CONSIGNMENT_TYPE_CODE")
    @Basic
    var consignmentTypeCode: String? = null

    @Column(name = "CONSIGNMENT_TYPE_DESCRIPTION")
    @Basic
    var consignmentTypeDescription: String? = null

    @Column(name = "MDA_CODE")
    @Basic
    var mdaCode: String? = null

    @Column(name = "MDA_DESCRIPTION")
    @Basic
    var mdaDescription: String? = null

    @Column(name = "PROCESS_CODE")
    @Basic
    var processCode: String? = null

    @Column(name = "PROCESS_DESCRIPTION")
    @Basic
    var processDescription: String? = null

    @Column(name = "APPLICATION_DATE")
    @Basic
    var applicationDate: Date? = null

    @Column(name = "APPROVAL_DATE")
    @Basic
    var approvalDate: Date? = null

    @Column(name = "BLACKLIST_REQUEST_DATE")
    @Basic
    var blacklistRequestDate: Date? = null

    @Column(name = "APPROVAL_STATUS")
    @Basic
    var approvalStatus: String? = null



    @Column(name = "FINAL_APPROVAL_DATE")
    @Basic
    var finalApprovalDate: Time? = null

    @Column(name = "DECLARATION_NO")
    @Basic
    var declarationNo: String? = null

    @Column(name = "CONSIGNMENT_TYPE")
    @Basic
    var consignmentType: String? = null

    @Column(name = "CONSIGNMENT_CODE")
    @Basic
    var consignmentCode: String? = null

    @Column(name = "CONSIGNMENT_DESCRIPTION")
    @Basic
    var consignmentDescription: String? = null

    @Column(name = "MASTER_APPROVAL_NO")
    @Basic
    var masterApprovalNo: String? = null

    @Column(name = "UCR_NUMBER")
    @Basic
    var ucrNumber: String? = null

    @Column(name = "DATE_INSPECTION_SCHEDULED")
    @Basic
    var dateInspectionScheduled: Date? = null

    @Column(name = "VERSION_NO")
    @Basic
    var versionNo: String? = null

    @Column(name = "REMARKS")
    @Basic
    var remarks: String? = null

    @Column(name = "CONDITION_OF_APPROVAL")
    @Basic
    var conditionOfApproval: String? = null

    @Column(name = "PURPOSE_OF_IMPORT")
    @Basic
    var purposeOfImport: String? = null

    @Column(name = "PURPOSE_OF_EXPORT")
    @Basic
    var purposeOfExport: String? = null

    @Column(name = "DATE_SUBMITTED")
    @Basic
    var dateSubmitted: Time? = null

    @Column(name = "LINK_AGENT_PORTAL")
    @Basic
    var linkAgentPortal: String? = null

    @Column(name = "ASSIGNED_IO")
    @Basic
    var assignedIo: Long? = null

    @Column(name = "ASSIGNED_STATUS")
    @Basic
    var assignedStatus: Int? = null

    @Column(name = "BLACKLIST_REJECTED")
    @Basic
    var blacklistRejected: Int? = null

    @Column(name = "TARGETING_REJECTED")
    @Basic
    var targetingRejected: Int? = null

    @Column(name = "REJECTED_STATUS")
    @Basic
    var rejectedStatus: Int? = null

    @Column(name = "REJECTED_REASON")
    @Basic
    var rejectedReason: String? = null

    @Column(name = "APPROVE_REMARKS")
    @Basic
    var approveRemarks: String? = null

    @Column(name = "OFFICER_REMARKS_STATUS")
    @Basic
    var officerRemarksStatus: Int? = null

    @Column(name = "OFFICER_REMARKS")
    @Basic
    var officerRemarks: String? = null

    @Column(name = "SUPERVISOR_REMARKS_STATUS")
    @Basic
    var supervisorRemarksStatus: Int? = null

    @Column(name = "SUPERVISOR_REMARKS")
    @Basic
    var supervisorRemarks: String? = null

    @Column(name = "BLACKLIST_STATUS")
    @Basic
    var blacklistStatus: Int? = null

    @Column(name = "BLACKLIST_REMARKS")
    @Basic
    var blacklistRemarks: String? = null

    @Column(name = "TARGETED_STATUS")
    @Basic
    var targetedStatus: Int? = null

    @Column(name = "TARGETED_REMARKS")
    @Basic
    var targetedRemarks: String? = null

    @Column(name = "INSPECTION_SCHEDULED_STATUS")
    @Basic
    var inspectionScheduledStatus: Int? = 0

    @Column(name = "BLACKLIST_USER")
    @Basic
    var blacklistUser: Long? = null

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

    @Column(name = "RESUBMIT_STATUS")
    @Basic
    var resubmitStatus: Int? = null

    @Column(name = "RESUBMIT_DATE")
    @Basic
    var resubmitDate: Timestamp? = null

    @Column(name = "RISK_STATUS")
    @Basic
    var riskStatus: Int? = null

    @Column(name = "RISK_ITEM_NUMBER")
    @Basic
    var riskItemNumber: Long? = null

    @Column(name = "DEFER_STATUS")
    @Basic
    var deferStatus: Int? = null

    @Column(name = "DEFER_REASON")
    @Basic
    var deferReason: String? = null

    @Column(name = "SUPERVISOR_ID")
    @Basic
    var supervisorId: Long? = null

    @Column(name = "CD_STATUS")
    @Basic
    var cdStatus: String? = null

    @Column(name = "SAVE_REASON")
    @Basic
    var saveReason: String? = null

    @Column(name = "APPROVE_NAME")
    @Basic
    var approveName: String? = null

    @Column(name = "PROCESS_REJECTION_REMARKS")
    @Basic
    var processRejectionRemarks: String? = null

    @Column(name = "RISK_LEVEL")
    @Basic
    var riskLevel: String? = null

    @Column(name = "APPROVE_MARK")
    @Basic
    var approveMark: Long? = null

    @Column(name = "TARGET_STATUS")
    @Basic
    var targetStatus: Long? = null

    @Column(name = "PROCESS_REJECTION_STATUS")
    @Basic
    var processRejectionStatus: Int? = null

    @Column(name = "COMPLIANT_STATUS")
    @Basic
    var compliantStatus: Int? = null

    @Column(name = "PAYMENT_STATUS")
    @Basic
    var paymentStatus: Int? = null

    @Column(name = "TARGET_APPROVED")
    @Basic
    var targetApproved: Int? = null

    @Column(name = "BLACKLIST_APPROVED")
    @Basic
    var blacklistApproved: Int? = null

    @Column(name = "INSPECTION_CHECKLIST_STATUS")
    @Basic
    var inspectionChecklistStatus: Int? = null

    @Column(name = "REF_NUMBER")
    @Basic
    var refNumber: String? = null

    @Column(name = "MINISTRY_INSPECTION_STATUS")
    @Basic
    var ministryInspectionStatus: Int? = null

    @Column(name = "MINISTRY_INSPECTION_REMARKS")
    @Basic
    var ministryInspectionRemarks: String? = null

    @Column(name = "MINISTRY_INSPECTION_INSPECTED_STATUS")
    @Basic
    var ministryInspectionInspectedStatus: Int? = null

    @Column(name = "MINISTRY_INSPECTION_INSPECTED_REMARKS")
    @Basic
    var ministryInspectionInspectedRemarks: String? = null

    @Column(name = "MINISTRY_INSPECTION_ASSIGNED_DATE")
    @Basic
    var ministryInspectionAssignedDate: Date? = null

    @Column(name = "MINISTRY_INSPECTION_INSPECTED_DATE")
    @Basic
    var ministryInspectionInspectedDate: Date? = null

    @JoinColumn(name = "CHECKLIST_TYPE_AGROCHEM", referencedColumnName = "ID")
    @ManyToOne
    var checklistTypeAgrochem: CdChecklistTypesEntity? = null


    @JoinColumn(name = "CHECKLIST_TYPE_ENGINEERING", referencedColumnName = "ID")
    @ManyToOne
    var checklistTypeEngineering: CdChecklistTypesEntity? = null


    @JoinColumn(name = "CHECKLIST_TYPE_OTHERS", referencedColumnName = "ID")
    @ManyToOne
    var checklistTypeOthers: CdChecklistTypesEntity? = null


//    @JoinColumn(name = "ITEM_ID", referencedColumnName = "ID")
//    @ManyToOne
//    var itemId: CdItemDetailsEntity? = null VALUES_HEADER_LEVEL_ID

    @JoinColumn(name = "VALUES_HEADER_LEVEL_ID", referencedColumnName = "ID")
    @ManyToOne
    var valuesHeaderLevelId: CdValuesHeaderLevelEntity? = null

    @JoinColumn(name = "ARRIVAL_POINT", referencedColumnName = "ID")
    @ManyToOne
    var arrivalPoint: CfgArrivalPointEntity? = null

    @JoinColumn(name = "PORT_OF_ARRIVAL", referencedColumnName = "ID")
    @ManyToOne
    var portOfArrival: SectionsEntity? = null

    @JoinColumn(name = "FREIGHT_STATION", referencedColumnName = "ID")
    @ManyToOne
    var freightStation: SubSectionsLevel2Entity? = null

    @JoinColumn(name = "EXPORTER_ID", referencedColumnName = "ID")
    @ManyToOne
    var exporterId: CdExporterDetailsEntity? = null

    @JoinColumn(name = "IMPORTER_ID", referencedColumnName = "ID")
    @ManyToOne
    var importerId: CdImporterDetailsEntity? = null

    @JoinColumn(name = "APPLICANT_ID", referencedColumnName = "ID")
    @ManyToOne
    var applicantId: CdApplicantDetailsEntity? = null

    @JoinColumn(name = "TRANSPORT_ID", referencedColumnName = "ID")
    @ManyToOne
    var transportId: CdTransportDetailsEntity? = null

    @JoinColumn(name = "CONSIGNEE_ID", referencedColumnName = "ID")
    @ManyToOne
    var consigneeId: CdConsigneeDetailsEntity? = null

    @JoinColumn(name = "CONSIGNOR_ID", referencedColumnName = "ID")
    @ManyToOne
    var consignorId: CdConsignorDetailsEntity? = null

    @JoinColumn(name = "APPLICANT_STATUS", referencedColumnName = "ID")
    @ManyToOne
    var applicantStatus: CdApplicationStatusEntity? = null

    @JoinColumn(name = "ASSIGN_IO_ID", referencedColumnName = "ID")
    @ManyToOne
    var assignIoId: UsersEntity? = null

    @JoinColumn(name = "COC_ID", referencedColumnName = "ID")
    @ManyToOne
    var cocId: CocsEntity? = null

    @JoinColumn(name = "NCR_ID", referencedColumnName = "ID")
    @ManyToOne
    var ncrId: CocsEntity? = null

    @JoinColumn(name = "COR_ID", referencedColumnName = "ID")
    @ManyToOne
    var corId: CorsEntity? = null

    @JoinColumn(name = "IDF_ID", referencedColumnName = "ID")
    @ManyToOne
    var idfId: IdfsEntity? = null

    @JoinColumn(name = "CLEARING_AGENT_ID", referencedColumnName = "ID")
    @ManyToOne
    var clearingAgentId: CdClearingAgentDetailsEntity? = null

    @JoinColumn(name = "PROCESS_STATUS", referencedColumnName = "ID")
    @ManyToOne
    var processStatus: CdProcessTypesEntity? = null

    @Column(name = "DI_COC_PROCESS_STATUS")
    @Basic
    var diCocProcessStatus: Int? = null

    @Column(name = "DI_COC_PROCESS_STARTED_ON")
    @Basic
    var diCocProcessStartedOn: Timestamp? = null

    @Column(name = "DI_COC_PROCESS_COMPLETED_ON")
    @Basic
    var diCocProcessCompletedOn: Timestamp? = null

    @Column(name = "DI_COC_PROCESS_INSTANCE_ID")
    @Basic
    var diCocProcessInstanceId: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other ) return true
        if ( other == null || javaClass != other.javaClass) return false
        val that = other as ConsignmentDocumentEntity
        return id == that.id &&
                approveRemarks == that.approveRemarks &&
                blacklistRejected == that.blacklistRejected &&
                targetingRejected == that.targetingRejected &&
                processRejectionRemarks == that.processRejectionRemarks &&
                dateInspectionScheduled == that.dateInspectionScheduled &&
                officerRemarksStatus == that.officerRemarksStatus &&
                officerRemarks == that.officerRemarks &&
                supervisorRemarksStatus == that.supervisorRemarksStatus &&
                supervisorRemarks == that.supervisorRemarks &&
                targetApproved  == that.targetApproved &&
                blacklistApproved  == that.blacklistApproved &&
                blacklistRequestDate == that.blacklistRequestDate &&
                targetedStatus == that.targetedStatus &&
                targetedRemarks == that.targetedRemarks &&
                inspectionChecklistStatus == that.inspectionChecklistStatus &&
                blacklistUser == that.blacklistUser &&
                inspectionScheduledStatus  == that.inspectionScheduledStatus &&
                blacklistRemarks == that.blacklistRemarks &&
                blacklistStatus == that.blacklistStatus &&
                processRejectionStatus == that.processRejectionStatus &&
                approveName == that.approveName &&
                documentName == that.documentName &&
                documentTypeCode == that.documentTypeCode &&
                documnetTypeDescription == that.documnetTypeDescription &&
                documentNo == that.documentNo &&
                cmsDocumentTypeCode == that.cmsDocumentTypeCode &&
                process == that.process &&
                applicationRefNo == that.applicationRefNo &&
                applicationTypeCode == that.applicationTypeCode &&
                applicationTypeDescription == that.applicationTypeDescription &&
                consignmentTypeCode == that.consignmentTypeCode &&
                consignmentTypeDescription == that.consignmentTypeDescription &&
                mdaCode == that.mdaCode &&
                paymentStatus == that.paymentStatus &&
                mdaDescription == that.mdaDescription &&
                processCode == that.processCode &&
                processDescription == that.processDescription &&
                applicationDate == that.applicationDate &&
                approvalDate == that.approvalDate &&
                approvalStatus == that.approvalStatus &&
                finalApprovalDate == that.finalApprovalDate &&
                declarationNo == that.declarationNo &&
                consignmentType == that.consignmentType &&
                consignmentCode == that.consignmentCode &&
                consignmentDescription == that.consignmentDescription &&
                masterApprovalNo == that.masterApprovalNo &&
                ucrNumber == that.ucrNumber &&
                versionNo == that.versionNo &&
                remarks == that.remarks &&
                termsAndConditions == that.termsAndConditions &&
                conditionOfApproval == that.conditionOfApproval &&
                purposeOfImport == that.purposeOfImport &&
                purposeOfExport == that.purposeOfExport &&
                dateSubmitted == that.dateSubmitted &&
                masterApprovalVersionNo == that.masterApprovalVersionNo &&
                linkAgentPortal == that.linkAgentPortal &&
                assignedIo == that.assignedIo &&
                assignedStatus == that.assignedStatus &&
                rejectedStatus == that.rejectedStatus &&
                rejectedReason == that.rejectedReason &&
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
                version == that.version &&
                resubmitStatus == that.resubmitStatus &&
                resubmitDate == that.resubmitDate &&
                riskStatus == that.riskStatus &&
                riskItemNumber == that.riskItemNumber &&
                deferStatus == that.deferStatus &&
                deferReason == that.deferReason &&
                supervisorId == that.supervisorId &&
                cdStatus == that.cdStatus &&
                saveReason == that.saveReason &&
                riskLevel == that.riskLevel &&
                approveMark == that.approveMark &&
                targetStatus == that.targetStatus &&
                compliantStatus == that.compliantStatus &&
                refNumber == that.refNumber &&
                ncrId == that.ncrId &&
                corId == that.corId &&
                idfId == that.idfId &&
                ministryInspectionStatus == that.ministryInspectionStatus &&
                ministryInspectionRemarks == that.ministryInspectionRemarks &&
                ministryInspectionInspectedStatus == that.ministryInspectionInspectedStatus &&
                ministryInspectionInspectedRemarks == that.ministryInspectionInspectedRemarks &&
                ministryInspectionInspectedDate == that.ministryInspectionInspectedDate &&
                ministryInspectionAssignedDate == that.ministryInspectionAssignedDate
    }

    override fun hashCode(): Int {
        return Objects.hash(
                id,
                targetingRejected,
                blacklistRejected,
                targetedStatus,
                termsAndConditions,
                approveRemarks,
                processRejectionStatus,
                dateInspectionScheduled,
                processRejectionRemarks,
                officerRemarks,
                officerRemarksStatus,
                inspectionChecklistStatus,
                supervisorRemarksStatus,
                supervisorRemarks,
                inspectionScheduledStatus,
                blacklistUser,
                blacklistApproved,
                targetApproved,
                paymentStatus,
                blacklistRequestDate,
                approveName,
                targetedRemarks,
                documentName,
                blacklistRemarks,
                blacklistStatus,
                documentTypeCode,
                documnetTypeDescription,
                documentNo,
                cmsDocumentTypeCode,
                process,
                applicationRefNo,
                applicationTypeCode,
                applicationTypeDescription,
                consignmentTypeCode,
                consignmentTypeDescription,
                mdaCode,
                mdaDescription,
                processCode,
                processDescription,
                applicationDate,
                approvalDate,
                approvalStatus,
                masterApprovalVersionNo,
                finalApprovalDate,
                declarationNo,
                consignmentType,
                consignmentCode,
                consignmentDescription,
                masterApprovalNo,
                ucrNumber,
                versionNo,
                remarks,
                conditionOfApproval,
                purposeOfImport,
                purposeOfExport,
                dateSubmitted,
                linkAgentPortal,
                assignedIo,
                assignedStatus,
                rejectedStatus,
                rejectedReason,
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
                version,
                resubmitStatus,
                resubmitDate,
                riskStatus,
                riskItemNumber,
                deferStatus,
                deferReason,
                supervisorId,
                cdStatus,
                saveReason,
                riskLevel,
                approveMark,
                targetStatus,
                compliantStatus,
                refNumber,
                ncrId,
                corId,
                idfId,
                ministryInspectionStatus,
                ministryInspectionRemarks,
                ministryInspectionInspectedStatus,
                ministryInspectionInspectedRemarks,
                ministryInspectionInspectedDate, ministryInspectionAssignedDate
        )
    }

}