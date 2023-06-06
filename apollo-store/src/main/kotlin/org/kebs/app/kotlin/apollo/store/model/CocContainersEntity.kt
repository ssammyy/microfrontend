package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_COC_CONTAINERS")
class CocContainersEntity : Serializable {
    @Column(name = "ID", nullable = false, precision = 0)
    @Id
    @SequenceGenerator(
        name = "DAT_KEBS_COC_CONTAINERS_SEQ_GEN",
        sequenceName = "DAT_KEBS_COC_CONTAINERS_SEQ",
        allocationSize = 1
    )
    @GeneratedValue(generator = "DAT_KEBS_COC_CONTAINERS_SEQ_GEN", strategy = GenerationType.SEQUENCE)

    var id: Long = 0

    @Column(name = "COC_ID", nullable = false, length = 100)
    @Basic
    var cocId: Long = 0

    @Column(name = "SHIPMENT_SEAL_NUMBER")
    var shipmentSealNumbers: String? = null

    @Column(name = "SHIPMENT_CONTAINER_NUMBER")
    var shipmentContainerNumber: String? = null

    @Column(name = "SHIPMENT_GROSS_WEIGHT")
    var shipmentGrossWeight: Double? = null

    @Column(name = "SHIPMENT_PARTIAL_NUMBER", nullable = false, precision = 2)
    @Basic
    var shipmentPartialNumber: Long = 0

    @Column(name = "STATUS", nullable = true, precision = 0)
    @Basic
    var status: Int? = null

    @Column(name = "VAR_FIELD_1", nullable = true, length = 350)
    @Basic
    var varField1: String? = null

    @Column(name = "VAR_FIELD_2", nullable = true, length = 350)
    @Basic
    var varField2: String? = null

    @Column(name = "VAR_FIELD_3", nullable = true, length = 350)
    @Basic
    var varField3: String? = null

    @Column(name = "VAR_FIELD_4", nullable = true, length = 350)
    @Basic
    var varField4: String? = null

    @Column(name = "VAR_FIELD_5", nullable = true, length = 350)
    @Basic
    var varField5: String? = null

    @Column(name = "CREATED_BY", nullable = true, length = 100)
    @Basic
    var createdBy: String? = null

    @Column(name = "CREATED_ON", nullable = true)
    @Basic
    var createdOn: Timestamp? = null

    @Column(name = "MODIFIED_BY", nullable = true, length = 100)
    @Basic
    var modifiedBy: String? = null

    @Column(name = "MODIFIED_ON", nullable = true)
    @Basic
    var modifiedOn: Timestamp? = null

    @Column(name = "DELETE_BY", nullable = true, length = 100)
    @Basic
    var deleteBy: String? = null

    @Column(name = "DELETED_ON", nullable = true)
    @Basic
    var deletedOn: Timestamp? = null


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as CocContainersEntity
        return id == that.id &&
                cocId == that.cocId &&
                status == that.status &&
                varField1 == that.varField1 &&
                varField2 == that.varField2 &&
                varField3 == that.varField3 &&
                varField4 == that.varField4 &&
                varField5 == that.varField5 &&
                createdBy == that.createdBy &&
                createdOn == that.createdOn &&
                modifiedBy == that.modifiedBy &&
                modifiedOn == that.modifiedOn &&
                deleteBy == that.deleteBy &&
                deletedOn == that.deletedOn

    }

    override fun hashCode(): Int {
        return Objects.hash(
            id,
            cocId,
            status,
            varField1,
            varField2,
            varField3,
            varField4,
            varField5,
            createdBy,
            createdOn,
            modifiedBy,
            modifiedOn,
            deleteBy,
            deletedOn
        )
    }
}