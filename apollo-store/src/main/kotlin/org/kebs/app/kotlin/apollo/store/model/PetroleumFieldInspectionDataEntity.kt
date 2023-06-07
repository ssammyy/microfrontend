package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_PETROLEUM_FIELD_INSPECTION_DATA")
class PetroleumFieldInspectionDataEntity: Serializable {
    @Column(name = "ID")
    @Id
    @SequenceGenerator(name = "DAT_PETROLEUM_FIELD_INSPECTION_DATA_SEQ_GEN", sequenceName = "DAT_PETROLEUM_FIELD_INSPECTION_DATA_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_PETROLEUM_FIELD_INSPECTION_DATA_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long = 0

    @Column(name = "INTRODUCTION")
    @Basic
    var introduction: String? = null

    @JoinColumn(name = "INSTALLATION_INSPECTION_DATA", referencedColumnName = "ID")
    @ManyToOne
    var installationInspectionData: PetroleumInstallationInspectionEntity? = null

    @Column(name = "CONSTRUCTION_DETAILS")
    @Basic
    var constructionDetails: String? = null

    @Column(name = "PRODUCT_STORAGE_DETAILS")
    @Basic
    var productStorageDetails: String? = null

    @Column(name = "PIPE_WORKS_AND_VENTS")
    @Basic
    var pipeWorksAndVents: String? = null

    @Column(name = "PUMPS_AND_DISPENSERS")
    @Basic
    var pumpsAndDispensers: String? = null

    @Column(name = "LOCATION_AND_CABLING")
    @Basic
    var locationAndCabling: String? = null

    @Column(name = "EMERGENCY_MEASURES")
    @Basic
    var emergencyMeasures: String? = null

    @Column(name = "TRAINING")
    @Basic
    var training: String? = null

    @Column(name = "RECOMMENDATION")
    @Basic
    var recommendation: String? = null

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

    @Column(name = "SECTION")
    @Basic
    var section: String? = null

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as PetroleumFieldInspectionDataEntity
        return id == that.id &&
                introduction == that.introduction &&
                constructionDetails == that.constructionDetails &&
                productStorageDetails == that.productStorageDetails &&
                pipeWorksAndVents == that.pipeWorksAndVents &&
                pumpsAndDispensers == that.pumpsAndDispensers &&
                locationAndCabling == that.locationAndCabling &&
                emergencyMeasures == that.emergencyMeasures &&
                training == that.training &&
                recommendation == that.recommendation &&
                status == that.status &&
                varField1 == that.varField1 &&
                varField2 == that.varField2 &&
                varField3 == that.varField3 &&
                varField4 == that.varField4 &&
                varField5 == that.varField5 &&
                varField6 == that.varField6 &&
                varField7 == that.varField7 &&
                installationInspectionData == that.installationInspectionData &&
                varField8 == that.varField8 &&
                varField9 == that.varField9 &&
                varField10 == that.varField10 &&
                createdBy == that.createdBy &&
                createdOn == that.createdOn &&
                modifiedBy == that.modifiedBy &&
                modifiedOn == that.modifiedOn &&
                deleteBy == that.deleteBy &&
                deletedOn == that.deletedOn &&
                section == that.section
    }

    override fun hashCode(): Int {
        return Objects.hash(id, introduction, installationInspectionData, constructionDetails, productStorageDetails, pipeWorksAndVents, pumpsAndDispensers, locationAndCabling, emergencyMeasures, training, recommendation, status, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn, section)
    }
}