package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*


@Entity
@Table(name = "LOG_SFTP_TRANSMISSION")
class SftpTransmissionEntity : Serializable {


    @Id
    @SequenceGenerator(name = "LOG_SFTP_TRANSMISSION_SEQ_GEN", sequenceName = "LOG_SFTP_TRANSMISSION_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "LOG_SFTP_TRANSMISSION_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID", nullable = false)
    var id: Long? = null

    @Column(name = "FILENAME")
    var filename: String? = null

    @Column(name = "FLOW_DIRECTION")
    var flowDirection: String? = null

    @Column(name = "FILE_TYPE")
    var fileType: String? = null

    @Column(name = "REMOTE_REFERENCE")
    var remoteReference: String? = null

    @Column(name = "TRANSACTION_DATE", nullable = false)
    var transactionDate: Date? = null

    @Column(name = "TRANSACTION_STATUS")
    var transactionStatus: Int? = null

    @Column(name = "TRANSACTION_REFERENCE")
    var transactionReference: String? = null

    @Column(name = "RETRIES")
    var retries: Int? = null

    @Column(name = "RETRIED")
    var retried: Int? = null

    @Column(name = "LAST_UPDATED")
    var lastUpdated: Date? = null

    @Column(name = "CALLING_METHOD")
    var callingMethod: String? = null

    @Column(name = "RESPONSE_STATUS")
    var responseStatus: String? = null

    @Column(name = "RESPONSE_MESSAGE")
    var responseMessage: String? = null

    @Column(name = "MESSAGE_RETRY_COUNT")
    var retryCount: Long? = null

    @Column(name = "KESW_ERROR_CODE")
    var keswErrorCode: String? = null

    @Column(name = "KESW_ERROR_MESSAGE")
    var keswErrorMessage: String? = null

    @Column(name = "TRANSACTION_START_DATE")
    var transactionStartDate: Timestamp? = null

    @Column(name = "TRANSACTION_COMPLETED_DATE")
    var transactionCompletedDate: Timestamp? = null

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

    @Column(name = "CREATED_BY")
    var createdBy: String? = null

    @Column(name = "CREATED_ON")
    var createdOn: Timestamp? = null

    @Column(name = "MODIFIED_BY")
    var modifiedBy: String? = null

    @Column(name = "MODIFIED_ON")
    var modifiedOn: Timestamp? = null

    @Column(name = "DELETE_BY")
    var deleteBy: String? = null

    @Column(name = "DELETED_ON")
    var deletedOn: Timestamp? = null

    companion object {
        private const val serialVersionUID = 1L
    }
}