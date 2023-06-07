package org.kebs.app.kotlin.apollo.store.model.pvc

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_PVOC_EXCEPTION_CERTIFICATES")
class PvocExceptionCertificate : Serializable {
    @Column(name = "ID")
    @Id
    @SequenceGenerator(name = "DAT_KEBS_PVOC_EXCEPTION_CERTIFICATES_SEQ_GEN", sequenceName = "DAT_KEBS_PVOC_EXCEPTION_CERTIFICATES_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_PVOC_EXCEPTION_CERTIFICATES_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long = 0

    @Column(name = "APPLICATION_ID")
    @Basic
    var applicationId: Long? = null

    @Column(name = "CERTIFICATE_NUMBER")
    @Basic
    var certificateNumber: String? = null

    @Column(name = "DEPARTMENT_CODE")
    @Basic
    var departmentCode: String? = null

    @Column(name = "CERTIFICATE_PREFIX")
    @Basic
    var certPrefix: String? = null

    @Column(name = "ISSUED_ON")
    @Basic
    var issuedOn: Date? = null


    @Column(name = "EXPIRED_ON")
    @Basic
    var expiresOn: Date? = null

    @Column(name = "CERTIFICATE_RENEWED")
    @Basic
    var certificateRenewed: Boolean? = null

    @Column(name = "CERTIFICATE_RENEWED_ON")
    @Basic
    var certificateRenewedOn: Timestamp? = null

    @Column(name = "CERTIFICATE_REVOKED")
    @Basic
    var certificateRevoked: Boolean? = null

    @Column(name = "CERTIFICATE_REVOKED_BY")
    @Basic
    var certificateRevokedBy: String? = null

    @Column(name = "CERTIFICATE_VERSION")
    @Basic
    var certificateVersion: Long? = null

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

}