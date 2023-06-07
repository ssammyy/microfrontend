package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "CFG_SIDEBAR_CHILDREN")
class SidebarChildrenEntity : Serializable {
    @Id
    @SequenceGenerator(
        name = "CFG_SIDEBAR_CHILDREN_SEQ_GEN",
        sequenceName = "CFG_SIDEBAR_CHILDREN_SEQ",
        allocationSize = 1
    )
    @GeneratedValue(generator = "CFG_SIDEBAR_CHILDREN_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID", nullable = false)
    var id: Long? = null

    @Column(name = "ROLE_ID")
    var roleId: Long? = null

    @Column(name = "MAIN_ID", nullable = false)
    var mainId: Long? = null

    @Column(name = "STATUS")
    var status: Int? = null

    @Column(name = "PATH")
    var path: String? = null

    @Column(name = "TITLE")
    var title: String? = null

    @Column(name = "AB")
    var aB: String? = null

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

    companion object {
        private const val serialVersionUID = 1L
    }
}