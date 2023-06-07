package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_CD_LOCAL_COC")
class CdLocalCocEntity : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_CD_LOCAL_COC_SEQ_GEN", sequenceName = "DAT_KEBS_CD_LOCAL_COC_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_CD_LOCAL_COC_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = 0

    @Column(name = "COC_NO")
    @Basic
    var cocNo: String? = null

    @Column(name = "COC_TYPE")
    @Basic
    var cocType: String? = null

    @Column(name = "COC_STATUS")
    @Basic
    var cocStatus: String? = null

    @Column(name = "ISSUE_DATE")
    @Basic
    var issueDate: Date? = null

    @Column(name = "ENTRY_NO")
    @Basic
    var entryNo: String? = null

    @Column(name = "IDF_NO")
    @Basic
    var idfNo: String? = null

    @Column(name = "IMPORTER_NAME")
    @Basic
    var importerName: String? = null

    @Column(name = "IMPORTER_ADDRESS")
    @Basic
    var importerAddress: String? = null

    @Column(name = "IMPORTER_PIN")
    @Basic
    var importerPin: String? = null

    @Column(name = "CLEAR_AGENT")
    @Basic
    var clearAgent: String? = null

    @Column(name = "PORT_ENTRY")
    @Basic
    var portEntry: String? = null

    @Column(name = "REMARKS")
    @Basic
    var remarks: String? = null

    @Column(name = "UCR_NUMBER")
    @Basic
    var ucrNumber: String? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null

    @Column(name = "VAR_FIELD_1")
    @Basic
    var varField1: String? = null

    @Column(name = "VAR_FIELD_2")
    @Basic
    var varField2: String? = null

    @Column(name = "VAR_FIELD_3")
    @Basic
    var varField3: String? = null

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as CdLocalCocEntity
        return id == that.id &&
                cocNo == that.cocNo &&
                cocStatus == that.cocStatus &&
                cocType == that.cocType &&
                issueDate == that.issueDate &&
                entryNo == that.entryNo &&
                idfNo == that.idfNo &&
                importerName == that.importerName &&
                importerAddress == that.importerAddress &&
                importerPin == that.importerPin &&
                clearAgent == that.clearAgent &&
                portEntry == that.portEntry &&
                remarks == that.remarks &&
                ucrNumber == that.ucrNumber &&
                status == that.status &&
                varField1 == that.varField1 &&
                varField2 == that.varField2 &&
                varField3 == that.varField3 &&
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
                deletedOn == that.deletedOn
    }

    override fun hashCode(): Int {
        return Objects.hash(
                id,
                cocNo,
                cocType,
                cocStatus,
                issueDate,
                entryNo,
                idfNo,
                importerName,
                importerAddress,
                importerPin,
                clearAgent,
                portEntry,
                remarks,
                ucrNumber,
                status,
                varField1,
                varField2,
                varField3,
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
                deletedOn
        )
    }
}