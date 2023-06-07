package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Timestamp
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "CFG_BRANCH_DETAILS")
class BranchDetailsEntity : Serializable {
    @Id
    @Column(name = "ID", nullable = false)
    var id: Long? = null

    @Column(name = "MANUFACTURER_ID", nullable = false)
    var manufacturerId: Long? = null

    @Column(name = "BRANCH_NAME", nullable = false)
    var branchName: String? = null

    @Column(name = "DESCRIPTIONS", nullable = false)
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

    companion object {
        private const val serialVersionUID = 1L
    }
}