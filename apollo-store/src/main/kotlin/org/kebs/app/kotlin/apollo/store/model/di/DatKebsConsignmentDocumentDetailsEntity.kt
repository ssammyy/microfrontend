package org.kebs.app.kotlin.apollo.store.model.di

import java.sql.Time
import java.sql.Timestamp
import java.util.*
import javax.persistence.*
//
//@Entity
//@Table(name = "DAT_KEBS_CONSIGNMENT_DOCUMENT_DETAILS", schema = "APOLLO", catalog = "")
class DatKebsConsignmentDocumentDetailsEntity {
    @Column(name = "ID")
    @Id
    var id: Long = 0

    @Column(name = "UCR_NUMBER")
    @Basic
    var ucrNumber: String? = null

    @Column(name = "CD_IMPORTER")
    @Basic
    var cdImporter: Long? = null

    @Column(name = "CD_CONSIGNEE")
    @Basic
    var cdConsignee: Long? = null

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

    @Column(name = "DESCRIPTION")
    @Basic
    var description: String? = null

    @Column(name = "STATUS")
    @Basic
    var status: Long? = null

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

    @Column(name = "PORT_OF_ARRIVAL")
    @Basic
    var portOfArrival: Long? = null

    @Column(name = "FREIGHT_STATION")
    @Basic
    var freightStation: Long? = null

    @Column(name = "DOC_TYPE_ID")
    @Basic
    var docTypeId: Long? = null

    @Column(name = "ASSIGNED_STATUS")
    @Basic
    var assignedStatus: Long? = null

    @Column(name = "PROCESS_REJECTION_STATUS")
    @Basic
    var processRejectionStatus: Long? = null

    @Column(name = "PROCESS_REJECTION_REMARKS")
    @Basic
    var processRejectionRemarks: String? = null

    @Column(name = "PROCESS_REJECTION_DATE")
    @Basic
    var processRejectionDate: Time? = null

    @Column(name = "ASSIGNED_REMARKS")
    @Basic
    var assignedRemarks: String? = null

    @Column(name = "ASSIGNED_DATE")
    @Basic
    var assignedDate: Time? = null

    @Column(name = "REASSIGNED_REMARKS")
    @Basic
    var reassignedRemarks: String? = null

    @Column(name = "REASSIGNED_DATE")
    @Basic
    var reassignedDate: Time? = null

    @Column(name = "REASSIGNED_STATUS")
    @Basic
    var reassignedStatus: Long? = null

    @Column(name = "TARGET_APPROVED_REMARKS")
    @Basic
    var targetApprovedRemarks: String? = null

    @Column(name = "TARGET_APPROVED_DATE")
    @Basic
    var targetApprovedDate: Time? = null

    @Column(name = "TARGET_APPROVED_STATUS")
    @Basic
    var targetApprovedStatus: Long? = null

    @Column(name = "BLACKLIST_APPROVED_REMARKS")
    @Basic
    var blacklistApprovedRemarks: String? = null

    @Column(name = "BLACKLIST_APPROVED_DATE")
    @Basic
    var blacklistApprovedDate: Time? = null

    @Column(name = "BLACKLIST_APPROVED_STATUS")
    @Basic
    var blacklistApprovedStatus: Long? = null

    @Column(name = "BLACKLIST_REMARKS")
    @Basic
    var blacklistRemarks: String? = null

    @Column(name = "BLACKLIST_DATE")
    @Basic
    var blacklistDate: Time? = null

    @Column(name = "BLACKLIST_STATUS")
    @Basic
    var blacklistStatus: Long? = null

    @Column(name = "IDF_NUMBER")
    @Basic
    var idfNumber: String? = null

    @Column(name = "COC_NUMBER")
    @Basic
    var cocNumber: String? = null

    @Column(name = "LOCAL_COC_COR_REMARKS")
    @Basic
    var localCocCorRemarks: String? = null

    @Column(name = "LOCAL_COC_COR_DATE")
    @Basic
    var localCocCorDate: Time? = null

    @Column(name = "LOCAL_COC_COR_STATUS")
    @Basic
    var localCocCorStatus: Long? = null

    @Column(name = "COMPLIANT_REMARKS")
    @Basic
    var compliantRemarks: String? = null

    @Column(name = "COMPLIANT_DATE")
    @Basic
    var compliantDate: Time? = null

    @Column(name = "COMPLIANT_STATUS")
    @Basic
    var compliantStatus: Long? = null

    @Column(name = "UUID")
    @Basic
    var uuid: String? = null

    @Column(name = "CS_APPROVAL_STATUS")
    @Basic
    var csApprovalStatus: Long? = null

    @Column(name = "CD_STANDARDS_TWO")
    @Basic
    var cdStandardsTwo: Long? = null

    @Column(name = "CLUSTER_ID")
    @Basic
    var clusterId: Long? = null
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as DatKebsConsignmentDocumentDetailsEntity
        return id == that.id &&
                ucrNumber == that.ucrNumber &&
                cdImporter == that.cdImporter &&
                cdConsignee == that.cdConsignee &&
                cdExporter == that.cdExporter &&
                cdConsignor == that.cdConsignor &&
                cdTransport == that.cdTransport &&
                cdHeaderOne == that.cdHeaderOne &&
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
                deletedOn == that.deletedOn &&
                portOfArrival == that.portOfArrival &&
                freightStation == that.freightStation &&
                docTypeId == that.docTypeId &&
                assignedStatus == that.assignedStatus &&
                processRejectionStatus == that.processRejectionStatus &&
                processRejectionRemarks == that.processRejectionRemarks &&
                processRejectionDate == that.processRejectionDate &&
                assignedRemarks == that.assignedRemarks &&
                assignedDate == that.assignedDate &&
                reassignedRemarks == that.reassignedRemarks &&
                reassignedDate == that.reassignedDate &&
                reassignedStatus == that.reassignedStatus &&
                targetApprovedRemarks == that.targetApprovedRemarks &&
                targetApprovedDate == that.targetApprovedDate &&
                targetApprovedStatus == that.targetApprovedStatus &&
                blacklistApprovedRemarks == that.blacklistApprovedRemarks &&
                blacklistApprovedDate == that.blacklistApprovedDate &&
                blacklistApprovedStatus == that.blacklistApprovedStatus &&
                blacklistRemarks == that.blacklistRemarks &&
                blacklistDate == that.blacklistDate &&
                blacklistStatus == that.blacklistStatus &&
                idfNumber == that.idfNumber &&
                cocNumber == that.cocNumber &&
                localCocCorRemarks == that.localCocCorRemarks &&
                localCocCorDate == that.localCocCorDate &&
                localCocCorStatus == that.localCocCorStatus &&
                compliantRemarks == that.compliantRemarks &&
                compliantDate == that.compliantDate &&
                compliantStatus == that.compliantStatus &&
                uuid == that.uuid &&
                csApprovalStatus == that.csApprovalStatus &&
                cdStandardsTwo == that.cdStandardsTwo &&
                clusterId == that.clusterId
    }

    override fun hashCode(): Int {
        return Objects.hash(id, ucrNumber, cdImporter, cdConsignee, cdExporter, cdConsignor, cdTransport, cdHeaderOne, description, status, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn, portOfArrival, freightStation, docTypeId, assignedStatus, processRejectionStatus, processRejectionRemarks, processRejectionDate, assignedRemarks, assignedDate, reassignedRemarks, reassignedDate, reassignedStatus, targetApprovedRemarks, targetApprovedDate, targetApprovedStatus, blacklistApprovedRemarks, blacklistApprovedDate, blacklistApprovedStatus, blacklistRemarks, blacklistDate, blacklistStatus, idfNumber, cocNumber, localCocCorRemarks, localCocCorDate, localCocCorStatus, compliantRemarks, compliantDate, compliantStatus, uuid, csApprovalStatus, cdStandardsTwo, clusterId)
    }
}
