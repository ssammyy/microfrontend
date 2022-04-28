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


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as CallForTCApplication
        return id == that.id &&
                dateOfPublishing == that.dateOfPublishing &&
                tc == that.tc &&
                tcId == that.tcId &&

                title == that.title &&
                expiryDate == that.expiryDate &&
                description == that.description &&
                varField4 == that.varField4 &&
                varField5 == that.varField5 &&
                varField6 == that.varField6 &&
                varField7 == that.varField7 &&
                varField8 == that.varField8 &&
                varField9 == that.varField9 &&
                varField10 == that.varField10 &&
                createdBy == that.createdBy &&
                createdOn == that.createdOn &&
                modifiedBy == that.modifiedBy &&
                modifiedOn == that.modifiedOn &&
                deleteBy == that.deleteBy &&
                status == that.status &&

                deletedOn == that.deletedOn
    }

    override fun hashCode(): Int {
        return Objects.hash(
            id,
            dateOfPublishing,
            tc,
            tcId,
            title,
            expiryDate,
            description,
            varField4,
            varField5,
            varField6,
            varField7,
            varField8,
            varField9,
            varField10,
            createdBy,
            createdOn,
            modifiedBy,
            modifiedOn,
            deleteBy,
            status,
            deletedOn
        )
    }

}
