package org.kebs.app.kotlin.apollo.store.model.std

import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "SD_CALL_FOR_TC_APPLICATION")
class CallForTCApplication {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @Column(name = "DATE_OF_PUBLISHING")
    @Basic
    var dateOfPublishing: String? = null

    @Column(name = "TC")
    @Basic
    var tc: String? = null

    @Column(name = "TC_ID")
    @Basic
    var tcId: Long? = null

    @Column(name = "TITLE")
    @Basic
    var title: String? = null

    @Column(name = "EXPIRY_DATE")
    @Basic
    var expiryDate: Timestamp? = null

    @Column(name = "DESCRIPTION")
    @Basic
    var description: String? = null

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

    @Column(name = "STATUS")
    @Basic
    var status: String? = null




}
