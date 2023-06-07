package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_LOG_KRA_ENTRY_NUMBER_REQUEST")
class KraEntryNumberRequestLogEntity : Serializable {

    @Id
    @SequenceGenerator(name = "DAT_KEBS_LOG_KRA_ENTRY_NUMBER_REQUEST_SEQ_GEN", allocationSize = 1, sequenceName = "DAT_KEBS_LOG_KRA_ENTRY_NUMBER_REQUEST_SEQ")
    @GeneratedValue(generator = "DAT_KEBS_LOG_KRA_ENTRY_NUMBER_REQUEST_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID", nullable = false)
    var id: Long? = null


    @Column(name = "REQUEST_HASH")
    @Basic
    var requestHash: String? = null

    @Column(name = "REQUEST_TRANSMISSION_DATE")
    @Basic
    var requestTransmissionDate: String? = null

    @Column(name = "REQUEST_NO_OF_RECORDS")
    @Basic
    var requestNoOfRecords: String? = null

    @Column(name = "REQUEST_ENTRY_NUMBER")
    @Basic
    var requestEntryNumber: String? = null

    @Column(name = "REQUEST_KRA_PIN")
    @Basic
    var requestKraPin: String? = null

    @Column(name = "REQUEST_MANUFACTURER_NAME")
    @Basic
    var requestManufacturerName: String? = null

    @Column(name = "REQUEST_REGISTRATION_DATE")
    @Basic
    var requestRegistrationDate: String? = null

    @Column(name = "REQUEST_MANUFACTURE_STATUS")
    @Basic
    var requestManufactureStatus: String? = null

    @Column(name = "RESPONSE_STATUS")
    @Basic
    var responseStatus: String? = null

    @Column(name = "RESPONSE_RESPONSE_CODE")
    @Basic
    var responseResponseCode: String? = null

    @Column(name = "RESPONSE_MESSAGE")
    @Basic
    var responseMessage: String? = null

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
    var version: Int? = null
}