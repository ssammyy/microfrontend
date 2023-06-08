package org.kebs.app.kotlin.apollo.store.model.ms

import java.io.Serializable
import java.sql.Date
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_MS_UPLOADS", schema = "APOLLO")
class MsUploadsEntity : Serializable {
    @Id
    @SequenceGenerator(name = "DAT_KEBS_MS_UPLOADS_SEQ_GEN", allocationSize = 1, sequenceName = "DAT_KEBS_MS_UPLOADS_SEQ")
    @GeneratedValue(generator = "DAT_KEBS_MS_UPLOADS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    var id: Long? = null

    @Basic
    @Column(name = "FILEPATH")
    var filepath: String? = null

    @Basic
    @Column(name = "DESCRIPTION")
    var description: String? = null

    @Basic
    @Column(name = "NAME")
    var name: String? = null

    @Basic
    @Column(name = "FILE_TYPE")
    var fileType: String? = null

    @Basic
    @Column(name = "DOCUMENT_TYPE")
    var documentType: String? = null

    @Basic
    @Column(name = "DOCUMENT")
    var document: ByteArray? = null

    @Basic
    @Column(name = "TRANSACTION_DATE")
    var transactionDate: Date? = null

    @Basic
    @Column(name = "MS_WORKPLAN_GENERATED_ID")
    var msWorkplanGeneratedId: Long? = null

    @Basic
    @Column(name = "MS_FUEL_INSPECTION_ID")
    var msFuelInspectionId: Long? = null

    @Basic
    @Column(name = "MS_COMPLAINT_ID")
    var msComplaintId: Long? = null

    @Basic
    @Column(name = "VERSION_NUMBER")
    var versionNumber: Long? = null

    @Basic
    @Column(name = "ORDINARY_STATUS")
    var ordinaryStatus: Int? = null

    @Basic
    @Column(name = "SSF_UPLOADS")
    var ssfUploads: Int? = null

    @Basic
    @Column(name = "COMPLAINT_UPLOADS")
    var complaintUploads: Int? = null

    @Basic
    @Column(name = "WORK_PLAN_UPLOADS")
    var workPlanUploads: Int? = null

    @Basic
    @Column(name = "FUEL_PLAN_UPLOADS")
    var fuelPlanUploads: Int? = null

    @Basic
    @Column(name = "IS_UPLOAD_FINAL_REPORT")
    var isUploadFinalReport: Int? = null

    @Basic
    @Column(name = "STATUS")
    var status: Int? = null

    @Basic
    @Column(name = "VAR_FIELD_1")
    var varField1: String? = null

    @Basic
    @Column(name = "VAR_FIELD_2")
    var varField2: String? = null

    @Basic
    @Column(name = "VAR_FIELD_3")
    var varField3: String? = null

    @Basic
    @Column(name = "VAR_FIELD_4")
    var varField4: String? = null

    @Basic
    @Column(name = "VAR_FIELD_5")
    var varField5: String? = null

    @Basic
    @Column(name = "VAR_FIELD_6")
    var varField6: String? = null

    @Basic
    @Column(name = "VAR_FIELD_7")
    var varField7: String? = null

    @Basic
    @Column(name = "VAR_FIELD_8")
    var varField8: String? = null

    @Basic
    @Column(name = "VAR_FIELD_9")
    var varField9: String? = null

    @Basic
    @Column(name = "VAR_FIELD_10")
    var varField10: String? = null

    @Basic
    @Column(name = "CREATED_BY")
    var createdBy: String? = null

    @Basic
    @Column(name = "CREATED_ON")
    var createdOn: Timestamp? = null

    @Basic
    @Column(name = "MODIFIED_BY")
    var modifiedBy: String? = null

    @Basic
    @Column(name = "MODIFIED_ON")
    var modifiedOn: Timestamp? = null

    @Basic
    @Column(name = "DELETE_BY")
    var deleteBy: String? = null

    @Basic
    @Column(name = "DELETED_ON")
    var deletedOn: Timestamp? = null
}
