package org.kebs.app.kotlin.apollo.store.model


import java.io.Serializable
import java.sql.Timestamp
import java.time.LocalDate
import java.util.*
import javax.persistence.*


@Entity
@Table(name = "LOG_KRA_PIN_VALIDATIONS")
class KraPinValidations : Serializable {
    @Id
    @SequenceGenerator(name = "LOG_KRA_PIN_VALIDATIONS_SEQ_GEN", sequenceName = "LOG_KRA_PIN_VALIDATIONS_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "LOG_KRA_PIN_VALIDATIONS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID", nullable = false)
    var id: Long? = null

    @Column(name = "RESPONSE_CODE")
    var responseCode: String? = null

    @Column(name = "KRA_PIN")
    var kraPin: String? = null

    @Column(name = "RESPONSE_STATUS")
    var responseStatus: String? = null

    @Column(name = "RESPONSE_MESSAGE")
    var responseMessage: String? = null

    @Column(name = "RESPONSE_PAYLOAD")
    var responsePayload: String? = null

    @Column(name = "TRANSACTION_DATE", nullable = false)
    var transactionDate: LocalDate? = null

    @Column(name = "TRANSMISSION_DATE")
    var transmissionDate: Timestamp? = null

    @Column(name = "DESCRIPTION")
    var description: String? = null

    @Column(name = "STATUS")
    var status: Int? = null

    @Column(name = "DESCRIPTIONS")
    var descriptions: String? = null

    @Column(name = "VAR_FIELD_1")
    var varField1: String? = null

    @Column(name = "VAR_FIELD_2")
    var varField2: String? = null

    @Column(name = "VAR_FIELD_3")
    var varField3: String? = null

    @Column(name = "VAR_FIELD_4")
    var varField4: String? = null

    @Column(name = "VAR_FIELD_5")
    var varField5: String? = null

    @Column(name = "VAR_FIELD_6")
    var varField6: String? = null

    @Column(name = "VAR_FIELD_7")
    var varField7: String? = null

    @Column(name = "VAR_FIELD_8")
    var varField8: String? = null

    @Column(name = "VAR_FIELD_9")
    var varField9: String? = null

    @Column(name = "VAR_FIELD_10")
    var varField10: String? = null

    @Column(name = "CREATED_BY", nullable = false)
    var createdBy: String? = null

    @Column(name = "CREATED_ON", nullable = false)
    var createdOn: Timestamp? = null

    @Column(name = "LAST_MODIFIED_BY")
    var lastModifiedBy: String? = null

    @Column(name = "LAST_MODIFIED_ON")
    var lastModifiedOn: Timestamp? = null

    @Column(name = "UPDATE_BY")
    var updateBy: String? = null

    @Column(name = "UPDATED_ON")
    var updatedOn: Timestamp? = null

    @Column(name = "DELETE_BY")
    var deleteBy: String? = null

    @Column(name = "DELETED_ON")
    var deletedOn: Date? = null

    @Column(name = "VERSION")
    var version: Int? = null

    @Column(name = "REQUEST_REFERENCE", nullable = true)
    var requestReference: String? = null


}
