package org.kebs.app.kotlin.apollo.store.model.ms

import org.kebs.app.kotlin.apollo.store.model.MsWorkPlanGeneratedEntity
import java.io.Serializable
import java.sql.Date
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_MS_SAMPLE_COLLECTION")
class MsSampleCollectionEntity : Serializable{
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_MS_SAMPLE_COLLECTION_SEQ_GEN", allocationSize = 1, sequenceName = "DAT_KEBS_MS_SAMPLE_COLLECTION_SEQ")
    @GeneratedValue(generator = "DAT_KEBS_MS_SAMPLE_COLLECTION_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = 0

    @Column(name = "NAME_MANUFACTURER_TRADER")
    @Basic
    var nameManufacturerTrader: String? = null

    @Column(name = "ADDRESS_MANUFACTURE_TRADER")
    @Basic
    var addressManufacturerTrader: String? = null

    @Column(name = "SAMPLING_METHOD")
    @Basic
    var samplingMethod: String? = null

    @Column(name = "REASONS_COLLECTING_SAMPLES")
    @Basic
    var reasonsCollectingSamples: String? = null

    @Column(name = "ANY_REMARKS")
    @Basic
    var anyRemarks: String? = null

    @Column(name = "NAME_OFFICER_COLLECTING_SAMPLE")
    @Basic
    var nameOfficerCollectingSample: String? = null

    @Column(name = "DESIGNATION_OFFICER_COLLECTING_SAMPLE")
    @Basic
    var designationOfficerCollectingSample: String? = null

    @Column(name = "DATE_OFFICER_COLLECTING_SAMPLE")
    @Basic
    var dateOfficerCollectingSample: Date? = null

    @Column(name = "NAME_WITNESS")
    @Basic
    var nameWitness: String? = null

    @Column(name = "DESIGNATION_WITNESS")
    @Basic
    var designationWitness: String? = null

    @Column(name = "DATE_WITNESS")
    @Basic
    var dateWitness: Date? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int? = 0

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

    @Column(name = "MS_WORKPLAN_GENERATED_ID")
    @Basic
    var workPlanGeneratedID: Long? = null

    @Column(name = "MS_FUEL_INSPECTION_ID")
    @Basic
    var msFuelInspectionId: Long? = null
}
