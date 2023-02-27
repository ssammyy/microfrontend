package org.kebs.app.kotlin.apollo.store.model.qa

import java.io.Serializable
import java.sql.Date
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_MANUFACTURE_PLANT_DETAILS")
class ManufacturePlantDetailsEntity : Serializable {
    @Column(name = "ID")
    @Id
    @SequenceGenerator(
        name = "DAT_KEBS_MANUFACTURE_PLANT_DETAILS_SEQ_GEN",
        sequenceName = "DAT_KEBS_MANUFACTURE_PLANT_DETAILS_SEQ",
        allocationSize = 1
    )
    @GeneratedValue(generator = "DAT_KEBS_MANUFACTURE_PLANT_DETAILS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long = 0

    @Column(name = "MANUFACTURE_ID")
    @Basic
    var companyProfileId: Long? = null

    @Column(name = "COUNTY")
    @Basic
    var county: Long? = null

    @Column(name = "TOWN")
    @Basic
    var town: Long? = null

    @Column(name = "INSPECTION_FEE_STATUS")
    @Basic
    var inspectionFeeStatus: Int? = null

    @Column(name = "INVOICE_INSPECTION_GENERATED")
    @Basic
    var invoiceInspectionGenerated: Int? = null

    @Column(name = "PAID_DATE")
    @Basic
    var paidDate: Date? = null

    @Column(name = "ENDING_DATE")
    @Basic
    var endingDate: Date? = null

    @Column(name = "USER_ID")
    @Basic
    var userId: Long? = null

    @Column(name = "INVOICE_SHARED_ID")
    @Basic
    var invoiceSharedId: Long? = null

    @Column(name = "LOCATION")
    @Basic
    var location: String? = null

    @Column(name = "BRANCH_NAME")
    @Basic
    var branchName: String? = null

    @Column(name = "TOKEN_GIVEN")
    @Basic
    var tokenGiven: String? = null

    @Column(name = "STREET")
    @Basic
    var street: String? = null

    @Column(name = "BUILDING_NAME")
    @Basic
    var buildingName: String? = null

    @Column(name = "NEAREST_LAND_MARK")
    @Basic
    var nearestLandMark: String? = null

    @Column(name = "POSTAL_ADDRESS")
    @Basic
    var postalAddress: String? = null

    @Column(name = "TELEPHONE")
    @Basic
    var telephone: String? = null

    @Column(name = "EMAIL_ADDRESS")
    @Basic
    var emailAddress: String? = null

    @Column(name = "PHYSICAL_ADDRESS")
    @Basic
    var physicalAddress: String? = null

    @Column(name = "FAX_NO")
    @Basic
    var faxNo: String? = null

    @Column(name = "PLOT_NO")
    @Basic
    var plotNo: String? = null

    @Column(name = "DESIGNATION")
    @Basic
    var designation: String? = null

    @Column(name = "CONTACT_PERSON")
    @Basic
    var contactPerson: String? = null

    @Column(name = "ATTACHED_SKETCH_MAP_DOCUMENT")
    @Lob
    var attachedSketchMapDocument: ByteArray? = null

    @Column(name = "ATTACHED_SKETCH_MAP_NAME")
    @Basic
    var attachedSketchMapName: String? = null

    @Column(name = "ATTACHED_SKETCH_MAP_FILE_TYPE")
    @Basic
    var attachedSketchMapFileType: String? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null

    @Column(name = "DESCRIPTIONS")
    @Basic
    var descriptions: String? = null

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

    @Column(name = "REGION")
    @Basic
    var region: Long? = null

}
