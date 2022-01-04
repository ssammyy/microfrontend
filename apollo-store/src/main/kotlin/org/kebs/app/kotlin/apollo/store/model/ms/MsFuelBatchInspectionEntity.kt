package org.kebs.app.kotlin.apollo.store.model.ms

import org.kebs.app.kotlin.apollo.store.model.WorkplanYearsCodesEntity
import java.io.Serializable
import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "STG_KEBS_MASTER_MS_BATCH_FUEL_INSPECTION")
class MsFuelBatchInspectionEntity : Serializable{
    @Column(name = "ID")
    @SequenceGenerator(name = "STG_KEBS_MASTER_MS_BATCH_FUEL_INSPECTION_SEQ_GEN", allocationSize = 1, sequenceName = "STG_KEBS_MASTER_MS_BATCH_FUEL_INSPECTION_SEQ")
    @GeneratedValue(generator = "STG_KEBS_MASTER_MS_BATCH_FUEL_INSPECTION_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long = 0

    @Column(name = "REGION_ID")
    @Basic
    var regionId: Long?= null

    @Column(name = "BATCH_CLOSED")
    @Basic
    var batchClosed: Int?= null

    @Column(name = "USER_TASK_ID")
    @Basic
    var userTaskId: Long? = null

    @Column(name = "COUNTY_ID")
    @Basic
    var countyId: Long?= null

    @Column(name = "TOWN_ID")
    @Basic
    var townId: Long?= null

    @Column(name = "REFERENCE_NUMBER")
    @Basic
    var referenceNumber: String?= null

//    @Column(name = "BATCH_FILE_YEAR")
//    @Basic
//    var batchFileYear: String?= null

    @Column(name = "YEAR_NAME_ID")
    @Basic
    var yearNameId: Long?= null

    @Column(name = "TRANSACTION_DATE")
    @Basic
    var transactionDate: Date? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null

    @Column(name = "REMARKS")
    @Basic
    var remarks: String? = null

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
}
