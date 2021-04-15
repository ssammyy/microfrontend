package org.kebs.app.kotlin.apollo.store.model.di

import org.kebs.app.kotlin.apollo.store.model.SectionsEntity
import org.kebs.app.kotlin.apollo.store.model.SubSectionsLevel2Entity
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import java.io.Serializable
import java.sql.Date
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_CONSIGNMENT_DOCUMENT_DETAILS")
class ConsignmentDocumentDetailsEntity : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(
        name = "DAT_KEBS_CONSIGNMENT_DOCUMENT_DETAILS_SEQ_GEN",
        sequenceName = "DAT_KEBS_CONSIGNMENT_DOCUMENT_DETAILS_SEQ",
        allocationSize = 1
    )
    @GeneratedValue(generator = "DAT_KEBS_CONSIGNMENT_DOCUMENT_DETAILS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = null

    @Transient
    var confirmAssignedUserId: Long? = null

    @Transient
    var confirmCdStatusTypeId: Long? = null

//    @Transient
//    var confirmPortId: Long? = null
//
//    @Transient
//    var confirmCfsId: Long? = null


    @Basic
    @Column(name = "UUID")
    var uuid: String? = null

    @Basic
    @Column(name = "ISSUED_DATE_TIME")
    var issuedDateTime: String? = null

    @Basic
    @Column(name = "SUMMARY_PAGE_URL")
    var summaryPageURL: String? = null

    @Basic
    @Column(name = "LOCAL_COI_REMARKS")
    var localCoiRemarks: String? = null

    @Basic
    @Column(name = "SEND_COI_REMARKS")
    var sendCoiRemarks: String? = null

    @Column(name = "UCR_NUMBER")
    @Basic
    var ucrNumber: String? = null

    @Column(name = "VERSION")
    @Basic
    var version: Long? = null

    @Column(name = "IDF_NUMBER")
    @Basic
    var idfNumber: String? = null

    @Column(name = "COC_NUMBER")
    @Basic
    var cocNumber: String? = null

    @Column(name = "SEND_DEMAND_NOTE_REMARKS")
    @Basic
    var sendDemandNoteRemarks: String? = null

    @Column(name = "SEND_DEMAND_NOTE")
    @Basic
    var sendDemandNote: Int? = null

    @Column(name = "LOCAL_COI")
    @Basic
    var localCoi: Int? = null

    @Column(name = "OLD_CD_STATUS")
    @Basic
    var oldCdStatus: Int? = null

    @Column(name = "SEND_COI_STATUS")
    @Basic
    var sendCoiStatus: Int? = null

    @Column(name = "LOCAL_COC_COR_STATUS")
    @Basic
    var localCocOrCorStatus: Int? = null

    @Column(name = "LOCAL_COC_COR_REMARKS")
    @Basic
    var localCocOrCorRemarks: String? = null

    @Column(name = "LOCAL_COC_COR_DATE")
    @Basic
    var localCocOrCorDate: Date? = null

    @Column(name = "COMPLIANT_STATUS")
    @Basic
    var compliantStatus: Int? = null

    @Column(name = "COMPLIANT_REMARKS")
    @Basic
    var compliantRemarks: String? = null

    @Column(name = "COMPLIANT_DATE")
    @Basic
    var compliantDate: Date? = null

    @Column(name = "BLACKLIST_APPROVED_STATUS")
    @Basic
    var blacklistApprovedStatus: Int? = null

    @Column(name = "BLACKLIST_APPROVED_REMARKS")
    @Basic
    var blacklistApprovedRemarks: String? = null

    @Column(name = "BLACKLIST_APPROVED_DATE")
    @Basic
    var blacklistApprovedDate: Date? = null

    @Column(name = "BLACKLIST_STATUS")
    @Basic
    var blacklistStatus: Int? = null

    @Column(name = "BLACKLIST_REMARKS")
    @Basic
    var blacklistRemarks: String? = null

    @Column(name = "BLACKLIST_DATE")
    @Basic
    var blacklistDate: Date? = null

    @Column(name = "BLACKLIST_ID")
    @Basic
    var blacklistId: Int? = null

    @Column(name = "ASSIGNED_STATUS")
    @Basic
    var assignedStatus: Int? = null

    @Column(name = "ASSIGNED_REMARKS")
    @Basic
    var assignedRemarks: String? = null

    @Column(name = "ASSIGN_PORT_REMARKS")
    @Basic
    var assignPortRemarks: String? = null

    @Column(name = "ASSIGNED_DATE")
    @Basic
    var assignedDate: Date? = null

    @Column(name = "REASSIGNED_STATUS")
    @Basic
    var reassignedStatus: Int? = null

    @Column(name = "REASSIGNED_REMARKS")
    @Basic
    var reassignedRemarks: String? = null

    @Column(name = "REASSIGNED_DATE")
    @Basic
    var reassignedDate: Date? = null

    @Column(name = "PROCESS_REJECTION_STATUS")
    @Basic
    var processRejectionStatus: Int? = null

    @Column(name = "PROCESS_REJECTION_DATE")
    @Basic
    var processRejectionDate: Date? = null

    @Column(name = "PROCESS_REJECTION_REMARKS")
    @Basic
    var processRejectionRemarks: String? = null

    @Column(name = "DOC_TYPE_ID")
    @Basic
    var docTypeId: Long? = null

    @Column(name = "CLUSTER_ID")
    @Basic
    var clusterId: Long? = null

//    @JoinColumn(name = "CD_TYPE", referencedColumnName = "ID")
//    @ManyToOne
//    var cdType: ConsignmentDocumentTypesEntity? = null

    @JoinColumn(name = "ASSIGNED_INSPECTION_OFFICER", referencedColumnName = "ID")
    @ManyToOne
    var assignedInspectionOfficer: UsersEntity? = null

//    @JoinColumn(name = "PORT_OF_ARRIVAL", referencedColumnName = "ID")
//    @ManyToOne
//    var portOfArrival: SectionsEntity? = null
//
//    @JoinColumn(name = "FREIGHT_STATION", referencedColumnName = "ID")
//    @ManyToOne
//    var freightStation: SubSectionsLevel2Entity? = null

    //    @JoinColumn(name = "CD_EXPORTER", referencedColumnName = "ID")
//    @ManyToOne
//    var cdExporter: CdExporterDetailsEntity? = null
//
//    @JoinColumn(name = "CD_IMPORTER", referencedColumnName = "ID")
//    @ManyToOne
//    var cdImporter: CdImporterDetailsEntity? = null
//
    @JoinColumn(name = "CD_STANDARD", referencedColumnName = "ID")
    @ManyToOne
    var cdStandard: CdStandardsEntity? = null

    //
//    @JoinColumn(name = "CD_TRANSPORT", referencedColumnName = "ID")
//    @ManyToOne
//    var cdTransport: CdTransportDetailsEntity? = null
//
//    @JoinColumn(name = "CD_CONSIGNEE", referencedColumnName = "ID")
//    @ManyToOne
//    var cdConsignee: CdConsigneeDetailsEntity? = null
//
//    @JoinColumn(name = "CD_CONSIGNOR", referencedColumnName = "ID")
//    @ManyToOne
//    var cdConsignor: CdConsignorDetailsEntity? = null
//
//    @JoinColumn(name = "CD_HEADER_ONE", referencedColumnName = "ID")
//    @ManyToOne
//    var cdHeaderOne: CdValuesHeaderLevelEntity? = null
//
////    @JoinColumn(name = "CD_STANDARDS_TWO", referencedColumnName = "ID")
////    @ManyToOne
////    var cdStandardsTwo: CdStandardsTwoEntity? = null
    @Column(name = "CD_COC_LOCAL_TYPE_ID")
    @Basic
    var cdCocLocalTypeId: Long? = null

    @Column(name = "CD_TYPE")
    @Basic
    var cdType: Long? = null

    @Column(name = "PORT_OF_ARRIVAL")
    @Basic
    var portOfArrival: Long? = null

    @Column(name = "FREIGHT_STATION")
    @Basic
    var freightStation: Long? = null

    @Column(name = "CD_IMPORTER")
    @Basic
    var cdImporter: Long? = null

    @Column(name = "CD_CONSIGNEE")
    @Basic
    var cdConsignee: Long? = null

    @Column(name = "CD_PGA_HEADER")
    @Basic
    var cdPgaHeader: Long? = null

    @Column(name = "CD_HEADER_TWO")
    @Basic
    var cdHeaderTwo: Long? = null

    @Column(name = "CD_EXPORTER")
    @Basic
    var cdExporter: Long? = null

    @Column(name = "CD_CONSIGNOR")
    @Basic
    var cdConsignor: Long? = null

    @Column(name = "CD_TRANSPORT")
    @Basic
    var cdTransport: Long? = null

    @Column(name = "CD_HEADER_ONE")
    @Basic
    var cdHeaderOne: Long? = null

    @Column(name = "CD_STANDARDS_TWO")
    @Basic
    var cdStandardsTwo: Long? = null

    @JoinColumn(name = "ASSIGNER", referencedColumnName = "ID")
    @ManyToOne
    var assigner: UsersEntity? = null

    @Column(name = "CS_APPROVAL_STATUS")
    @Basic
    var csApprovalStatus: Int? = null

    @Column(name = "DI_PROCESS_INSTANCE_ID")
    @Basic
    var diProcessInstanceId: String? = null

    @Column(name = "DI_PROCESS_STATUS")
    @Basic
    var diProcessStatus: Int? = null

    @Column(name = "DI_PROCESS_STARTED_ON")
    @Basic
    var diProcessStartedOn: Timestamp? = null

    @Column(name = "DI_PROCESS_COMPLETED_ON")
    @Basic
    var diProcessCompletedOn: Timestamp? = null

    @Column(name = "APPROVE_REJECT_CD_STATUS")
    @Basic
    var approveRejectCdStatus: Int? = null

    @Column(name = "APPROVE_REJECT_CD_DATE")
    @Basic
    var approveRejectCdDate: Date? = null

    @Column(name = "APPROVE_REJECT_CD_REMARKS")
    @Basic
    var approveRejectCdRemarks: String? = null

    @JoinColumn(name = "APPROVE_REJECT_CD_STATUS_TYPE", referencedColumnName = "ID")
    @ManyToOne
    var approveRejectCdStatusType: CdStatusTypesEntity? = null

    @Column(name = "CDREFNUMBER")
    @Basic
    var cdRefNumber: String? = null

    @Column(name = "DESCRIPTION")
    @Basic
    var description: String? = null

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
        val that = other as ConsignmentDocumentDetailsEntity
        return id == that.id &&
                uuid == that.uuid &&
                cdCocLocalTypeId == that.cdCocLocalTypeId &&
                cdType == that.cdType &&
                issuedDateTime == that.issuedDateTime &&
                summaryPageURL == that.summaryPageURL &&
                portOfArrival == that.portOfArrival &&
                clusterId == that.clusterId &&
                freightStation == that.freightStation &&
                cdImporter == that.cdImporter &&
                cdConsignee == that.cdConsignee &&
                cdExporter == that.cdExporter &&
                cdConsignor == that.cdConsignor &&
                cdTransport == that.cdTransport &&
                cdHeaderOne == that.cdHeaderOne &&
                cdStandardsTwo == that.cdStandardsTwo &&
                cdPgaHeader == that.cdPgaHeader &&
                cdHeaderTwo == that.cdHeaderTwo &&
                sendDemandNoteRemarks == that.sendDemandNoteRemarks &&
                sendDemandNote == that.sendDemandNote &&
                sendCoiStatus == that.sendCoiStatus &&
                localCoiRemarks == that.localCoiRemarks &&
                sendCoiRemarks == that.sendCoiRemarks &&
//                confirmPortId == that.confirmPortId &&
//                confirmCfsId == that.confirmCfsId &&
                version == that.version &&
                confirmAssignedUserId == that.confirmAssignedUserId &&
                localCocOrCorStatus == that.localCocOrCorStatus &&
                localCocOrCorDate == that.localCocOrCorDate &&
                localCocOrCorRemarks == that.localCocOrCorRemarks &&
                compliantStatus == that.compliantStatus &&
                compliantDate == that.compliantDate &&
                compliantRemarks == that.compliantRemarks &&
                blacklistStatus == that.blacklistStatus &&
                blacklistDate == that.blacklistDate &&
                blacklistRemarks == that.blacklistRemarks &&
                blacklistApprovedStatus == that.blacklistApprovedStatus &&
                blacklistApprovedDate == that.blacklistApprovedDate &&
                blacklistApprovedRemarks == that.blacklistApprovedRemarks &&
                blacklistId == that.blacklistId &&
                assignedStatus == that.assignedStatus &&
                assignedDate == that.assignedDate &&
                assignedRemarks == that.assignedRemarks &&
                assignPortRemarks == that.assignPortRemarks &&
                reassignedStatus == that.reassignedStatus &&
                reassignedDate == that.reassignedDate &&
                reassignedRemarks == that.reassignedRemarks &&
                processRejectionStatus == that.processRejectionStatus &&
                processRejectionDate == that.processRejectionDate &&
                processRejectionRemarks == that.processRejectionRemarks &&
                docTypeId == that.docTypeId &&
                ucrNumber == that.ucrNumber &&
                idfNumber == that.idfNumber &&
                cocNumber == that.cocNumber &&
//                assigner == that.assigner &&
                localCoi == that.localCoi &&
                csApprovalStatus == that.csApprovalStatus &&
                description == that.description &&
                diProcessInstanceId == that.diProcessInstanceId &&
                diProcessStatus == that.diProcessStatus &&
                diProcessStartedOn == that.diProcessStartedOn &&
                diProcessCompletedOn == that.diProcessCompletedOn &&
                approveRejectCdStatus == that.approveRejectCdStatus &&
                approveRejectCdDate == that.approveRejectCdDate &&
                approveRejectCdRemarks == that.approveRejectCdRemarks &&
                approveRejectCdStatusType == that.approveRejectCdStatusType &&
                cdRefNumber == that.cdRefNumber &&
                oldCdStatus == that.oldCdStatus &&
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
            uuid,
            localCoi,
            sendCoiStatus,
            localCoiRemarks,
            sendCoiRemarks,
            version,
            sendDemandNote,
            sendDemandNoteRemarks,
            cdPgaHeader,
            clusterId,
            cdHeaderTwo,
            cdCocLocalTypeId,
            cdStandardsTwo,
            portOfArrival,
            freightStation,
            cdImporter,
            cdConsignee,
            cdExporter,
            cdConsignor,
            cdTransport,
            cdHeaderOne,
            ucrNumber,
            docTypeId,
            idfNumber,
            cocNumber,
            oldCdStatus,
            compliantStatus,
            compliantDate,
            compliantRemarks,
            localCocOrCorDate,
            localCocOrCorRemarks,
            localCocOrCorStatus,
            blacklistRemarks,
            blacklistDate,
            blacklistStatus,
            blacklistApprovedRemarks,
            blacklistApprovedDate,
            blacklistApprovedStatus,
            processRejectionRemarks,
            processRejectionDate,
            processRejectionStatus,
            confirmAssignedUserId,
            assignedStatus,
            assignedDate,
            assignedRemarks,
            reassignedStatus,
            reassignedDate,
            reassignedRemarks,
            issuedDateTime,
            summaryPageURL,
//                assigner,confirmPortId, confirmCfsId,
            csApprovalStatus,
            diProcessInstanceId,
            blacklistId,
            diProcessStatus,
            diProcessStartedOn,
            diProcessCompletedOn,
            assignPortRemarks,
            approveRejectCdStatus,
            approveRejectCdDate,
            approveRejectCdRemarks,
            approveRejectCdStatusType,
            cdRefNumber,
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
