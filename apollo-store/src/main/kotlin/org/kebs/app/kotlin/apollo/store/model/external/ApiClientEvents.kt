package org.kebs.app.kotlin.apollo.store.model.external

import java.io.Serializable
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "API_APPLICATION_CLIENT_EVENTS")
class ApiClientEvents: Serializable {
    @Column(name = "ID")
    @Id
    var id: Long = 0

    @Column(name = "CLIENT_ID")
    var agentId: Long = 0

    @Column(name = "EVENT_NAME", nullable = false)
    var eventName: String? = null

    @Column(name = "EVENT_TYPE", nullable = false)
    var eventType: String? = null

    @Column(name = "CONTACT_EMAIL", nullable = false)
    var contactEmail: String? = null

    @Column(name = "CONTENT", nullable = false)
    var eventContents: String? = null

    @Column(name = "STATUS")
    @Basic
    var status: Long? = null

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

}