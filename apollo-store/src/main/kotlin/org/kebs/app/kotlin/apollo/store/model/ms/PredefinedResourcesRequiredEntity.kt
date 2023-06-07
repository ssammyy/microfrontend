package org.kebs.app.kotlin.apollo.store.model.ms

import java.io.Serializable
import java.math.BigInteger
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "CFG_MS_PREDEFINED_RESOURCES_REQUIRED", schema = "APOLLO", catalog = "")
class PredefinedResourcesRequiredEntity : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "CFG_MS_PREDEFINED_RESOURCES_REQUIRED_SEQ_GEN", allocationSize = 1, sequenceName = "CFG_MS_PREDEFINED_RESOURCES_REQUIRED_SEQ")
    @GeneratedValue(generator = "CFG_MS_PREDEFINED_RESOURCES_REQUIRED_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = null

    @Basic
    @Column(name = "RESOURCE_NAME")
    var resourceName: String? = null

    @Basic
    @Column(name = "STATUS")
    var status: Int? = null

    @Basic
    @Column(name = "DESCRIPTION")
    var description: String? = null

    @Basic
    @Column(name = "VAR_FIELD_1")
    var varField1: String? = null

    @Basic
    @Column(name = "VAR_FIELD_2")
    var varField2: String? = null

    @Basic
    @Column(name = "VAR_FIELD_3")
    var varField3: String? = null

    @Basic
    @Column(name = "VAR_FIELD_4")
    var varField4: String? = null

    @Basic
    @Column(name = "VAR_FIELD_5")
    var varField5: String? = null

    @Basic
    @Column(name = "VAR_FIELD_6")
    var varField6: String? = null

    @Basic
    @Column(name = "VAR_FIELD_7")
    var varField7: String? = null

    @Basic
    @Column(name = "VAR_FIELD_8")
    var varField8: String? = null

    @Basic
    @Column(name = "VAR_FIELD_9")
    var varField9: String? = null

    @Basic
    @Column(name = "VAR_FIELD_10")
    var varField10: String? = null

    @Basic
    @Column(name = "CREATED_BY")
    var createdBy: String? = null

    @Basic
    @Column(name = "CREATED_ON")
    var createdOn: Timestamp? = null

    @Basic
    @Column(name = "MODIFIED_BY")
    var modifiedBy: String? = null

    @Basic
    @Column(name = "MODIFIED_ON")
    var modifiedOn: Timestamp? = null

    @Basic
    @Column(name = "DELETE_BY")
    var deleteBy: String? = null

    @Basic
    @Column(name = "DELETED_ON")
    var deletedOn: Timestamp? = null

}
