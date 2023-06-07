package org.kebs.app.kotlin.apollo.store.model.riskProfile

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_RISK_PROFILE_EXPORTER")
class RiskProfileExporterEntity : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_RISK_PROFILE_EXPORTER_SEQ_GEN", sequenceName = "DAT_KEBS_RISK_PROFILE_EXPORTER_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_RISK_PROFILE_EXPORTER_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long = 0

    @Column(name = "NAME")
    @Basic
    var name: String? = null

    @Column(name = "PIN")
    @Basic
    var pin: String? = null

    @Column(name = "POSTAL_ADDRESS")
    @Basic
    var postalAddress: String? = null

    @Column(name = "PHYSICAL_ADDRESS")
    @Basic
    var physicalAddress: String? = null

    @Column(name = "TELEPHONE")
    @Basic
    var telephone: String? = null

    @Column(name = "APPLICATION_CODE")
    @Basic
    var applicationCode: String? = null

    @Column(name = "POSTAL_COUNTRY")
    @Basic
    var postalCountry: String? = null

    @Column(name = "PHYSICAL_COUNTRY")
    @Basic
    var physicalCountry: String? = null

    @Column(name = "EMAIL")
    @Basic
    var email: String? = null

    @Column(name = "OGA_REF_NO")
    @Basic
    var ogaRefNo: String? = null

    @Column(name = "FAX")
    @Basic
    var fax: String? = null

    @Column(name = "SECTOR_OF_ACTIVITY")
    @Basic
    var sectorOfActivity: String? = null

    @Column(name = "WAREHOUSE_LOCATION")
    @Basic
    var warehouseLocation: String? = null

    @Column(name = "WAREHOUSE_CODE")
    @Basic
    var warehouseCode: String? = null

    @Column(name = "POSTAL_COUNTRY_NAME")
    @Basic
    var postalCountryName: String? = null

    @Column(name = "PHYSICAL_COUNTRY_NAME")
    @Basic
    var physicalCountryName: String? = null

    @Column(name = "MDA_REF_NO")
    @Basic
    var mdaRefNo: String? = null

    @Column(name = "REASON")
    @Basic
    var reason: String? = null

    @Column(name = "DESCRIPTION")
    @Basic
    var description: String? = null

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
        val that = other as RiskProfileExporterEntity
        return id == that.id &&
                name == that.name &&
                pin == that.pin &&
                postalAddress == that.postalAddress &&
                physicalAddress == that.physicalAddress &&
                telephone == that.telephone &&
                applicationCode == that.applicationCode &&
                postalCountry == that.postalCountry &&
                physicalCountry == that.physicalCountry &&
                email == that.email &&
                ogaRefNo == that.ogaRefNo &&
                fax == that.fax &&
                sectorOfActivity == that.sectorOfActivity &&
                warehouseLocation == that.warehouseLocation &&
                warehouseCode == that.warehouseCode &&
                postalCountryName == that.postalCountryName &&
                physicalCountryName == that.physicalCountryName &&
                mdaRefNo == that.mdaRefNo &&
                reason == that.reason &&
                description == that.description &&
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
        return Objects.hash(id, name, pin, postalAddress, physicalAddress, telephone, applicationCode, postalCountry, physicalCountry, email, ogaRefNo, fax, sectorOfActivity, warehouseLocation, warehouseCode, postalCountryName, physicalCountryName, mdaRefNo, reason,description, status, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn)
    }
}