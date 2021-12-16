package org.kebs.app.kotlin.apollo.store.model.external

import java.io.Serializable
import java.sql.Timestamp
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "API_APPLICATION_CLIENT")
class SystemApiClient: Serializable {
    @Id
    @Column(name = "ID", nullable = false)
    var id: Long? = null

    // PVOC or KIMS
    @Column(name = "CLIENT_TYPE", nullable = false)
    var clientType: String? = null

    @Column(name = "CLIENT_ID", nullable = false)
    var clientId: String? = null

    @Column(name = "CLIENT_NAME", nullable = false)
    var clientName: String? = null

    // PVOC_READ, PVOC_WRITE, PVOC_AGENT, PVOC_ADMIN
    @Column(name = "CLIENT_ROLE", nullable = false)
    var clientRole: String? = null

    @Column(name = "CLIENT_SECRET", nullable = false)
    var clientSecret: String? = null

    @Column(name = "CALLBACK_URL", nullable = false)
    var callbackURL: String? = null

    @Column(name = "EVENTS_URL", nullable = false)
    var eventsURL: String? = null // System events

    @Column(name = "CLIENT_DESCRIPTIONS", nullable = false)
    var descriptions: String? = null

    @Column(name = "CLIENT_BLOCK", nullable = false)
    var clientBlocked: Int? = null

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

    @Column(name = "CREATED_BY", nullable = false)
    var createdBy: String? = null

    @Column(name = "CREATED_ON", nullable = false)
    var createdOn: Timestamp? = null

    @Column(name = "MODIFIED_BY")
    var modifiedBy: String? = null

    @Column(name = "MODIFIED_ON")
    var modifiedOn: Timestamp? = null

    @Column(name = "DELETE_BY")
    var deleteBy: String? = null

    @Column(name = "DELETED_ON")
    var deletedOn: Timestamp? = null

    @Column(name = "STATUS")
    var status: Int? = null
}