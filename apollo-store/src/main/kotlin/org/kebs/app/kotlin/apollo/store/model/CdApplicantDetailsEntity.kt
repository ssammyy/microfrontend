package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Time
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_CD_APPLICANT_DETAILS")
class CdApplicantDetailsEntity : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_CD_APPLICANT_DETAILS_SEQ_GEN", sequenceName = "DAT_KEBS_CD_APPLICANT_DETAILS_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_CD_APPLICANT_DETAILS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long = 0
    
    @Column(name = "NAME")
    @Basic
    var name: String? = null
    
    @Column(name = "PIN")
    @Basic
    var pin: String? = null
    
    @Column(name = "ADDRESS")
    @Basic
    var address: String? = null
    
    @Column(name = "CONTACT_PERSON")
    @Basic
    var contactPerson: String? = null
    
    @Column(name = "APPLICATION_CODE")
    @Basic
    var applicationCode: String? = null
    
    @Column(name = "COUNTRY")
    @Basic
    var country: String? = null
    
    @Column(name = "EMAIL")
    @Basic
    var email: String? = null
    
    @Column(name = "DATE_SUBMITTED")
    @Basic
    var dateSubmitted: Time? = null
    
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
    @Column(name = "LAST_MODIFIED_BY")
    @Basic
    var lastModifiedBy: String? = null

    @Column(name = "LAST_MODIFIED_ON")
    @Basic
    var lastModifiedOn: Timestamp? = null

    @Column(name = "UPDATE_BY")
    @Basic
    var updateBy: String? = null

    @Column(name = "UPDATED_ON")
    @Basic
    var updatedOn: Timestamp? = null

    @Column(name = "DELETE_BY")
    @Basic
    var deleteBy: String? = null

    @Column(name = "DELETED_ON")
    @Basic
    var deletedOn: Timestamp? = null

    @Column(name = "VERSION", precision = 0)
    @Basic
    var version: Long? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as CdApplicantDetailsEntity
        return id == that.id &&
                name == that.name &&
                pin == that.pin &&
                address == that.address &&
                contactPerson == that.contactPerson &&
                applicationCode == that.applicationCode &&
                country == that.country &&
                email == that.email &&
                dateSubmitted == that.dateSubmitted &&
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
                lastModifiedBy == that.lastModifiedBy &&
                lastModifiedOn == that.lastModifiedOn &&
                updateBy == that.updateBy &&
                updatedOn == that.updatedOn &&
                deleteBy == that.deleteBy &&
                deletedOn == that.deletedOn &&
                version == that.version
    }

    override fun hashCode(): Int {
        return Objects.hash(id, name, pin, address, contactPerson, applicationCode, country, email, dateSubmitted, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, lastModifiedBy, lastModifiedOn, updateBy, updatedOn, deleteBy, deletedOn, version)
    }
}