package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_IMPORTER_CONTACT_DETAILS")
class ImporterContactDetailsEntity : Serializable{
    @Column(name = "ID")
    @Id
    @SequenceGenerator(name = "DAT_KEBS_IMPORTER_CONTACT_DETAILS_SEQ_GEN", sequenceName = "DAT_KEBS_IMPORTER_CONTACT_DETAILS_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_IMPORTER_CONTACT_DETAILS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long = 0

    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    @ManyToOne
    var userId: UsersEntity? = null

    @Column(name = "FIRST_NAME")
    @Basic
    var firstName: String? = null

    @Column(name = "LAST_NAME")
    @Basic
    var lastName: String? = null

    @Column(name = "EMAIL_ADDRESS")
    @Basic
    var emailAddress: String? = null

    @Column(name = "CELL_PHONE")
    @Basic
    var cellPhone: String? = null

    @Column(name = "REGISTRATION_DATE")
    @Basic
    var registrationDate: Date? = null

    @Column(name = "PIN")
    @Basic
    var pin: String? = null

    @Column(name = "POSTAL_ADDRESS")
    @Basic
    var postalAddress: String? = null

    @Column(name = "PHYSICAL_ADDRESS")
    @Basic
    var physicalAddress: String? = null

    @Column(name = "APPLICATION_CODE")
    @Basic
    var applicationCode: String? = null

    @Column(name = "POSTAL_COUNTRY")
    @Basic
    var postalCountry: String? = null

    @Column(name = "PHYSICAL_COUNTRY")
    @Basic
    var physicalCountry: String? = null

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

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null

    @Column(name = "DESCRIPTIONS")
    @Basic
    var descriptions: String? = null

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

    @Column(name = "MODIFIED_BY")
    @Basic
    var modifiedBy: String? = null

    @Column(name = "DELETE_BY")
    @Basic
    var deleteBy: String? = null

    @Column(name = "CREATED_ON")
    @Basic
    var createdOn: Date? = null

    @Column(name = "MODIFIED_ON")
    @Basic
    var modifiedOn: Timestamp? = null

    @Column(name = "DELETED_ON")
    @Basic
    var deletedOn: Timestamp? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as ImporterContactDetailsEntity
        return id == that.id &&
                firstName == that.firstName &&
                lastName == that.lastName &&
                emailAddress == that.emailAddress &&
                cellPhone == that.cellPhone &&
                registrationDate == that.registrationDate &&
                pin == that.pin &&
                postalAddress == that.postalAddress &&
                physicalAddress == that.physicalAddress &&
                applicationCode == that.applicationCode &&
                postalCountry == that.postalCountry &&
                physicalCountry == that.physicalCountry &&
                ogaRefNo == that.ogaRefNo &&
                fax == that.fax &&
                sectorOfActivity == that.sectorOfActivity &&
                warehouseLocation == that.warehouseLocation &&
                status == that.status &&
                descriptions == that.descriptions &&
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
                modifiedBy == that.modifiedBy &&
                deleteBy == that.deleteBy &&
                createdOn == that.createdOn &&
                modifiedOn == that.modifiedOn &&
                deletedOn == that.deletedOn
    }

    override fun hashCode(): Int {
        return Objects.hash(id, firstName, lastName, emailAddress, cellPhone, registrationDate, pin, postalAddress, physicalAddress, applicationCode, postalCountry, physicalCountry, ogaRefNo, fax, sectorOfActivity, warehouseLocation, status, descriptions, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, modifiedBy, deleteBy, createdOn, modifiedOn, deletedOn)
    }
}