package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Date
import java.sql.Time
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

@Entity
@Table(name = "DAT_KEBS_MANUFACTURERS")
class ManufacturersEntity : Serializable {


    @Column(name = "ID")
    @Id
    @SequenceGenerator(name = "DAT_KEBS_MANUFACTURERS_SEQ_GEN", sequenceName = "DAT_KEBS_MANUFACTURERS_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_MANUFACTURERS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long = 0

    @Column(name = "NAME")
    @Basic
    var name: String? = null

    @Column(name = "CLOSURE_OF_OPERATIONS")
    @Basic
    var closureOfOperations: Int? = null

    @Column(name = "OWNERSHIP")
    @Basic
    var ownership: String? = null

    @Column(name = "DIRECTOR_ID_NUMBER")
    @Basic
    var directorIdNumber: String?= null

    @Column(name = "FACTORY_VISIT_DATE")
    @Basic
    var factoryVisitDate: String? = null

    @Column(name = "FACTORY_VISIT_STATUS")
    @Basic
    var factoryVisitStatus: Int? = null

    //    @NotNull(message = "KRA PIN is required")
    @Size(max = 20, min = 13, message = "Invalid KRA PIN")
    @NotEmpty(message = "KRA PIN is required")
    @Column(name = "KRA_PIN")
    @Basic
    var kraPin: String? = null

    @Size(max = 20, min = 13, message = "Invalid REGISTRATION NUMBER")
    @NotEmpty(message = "REGISTRATION_NUMBER is required")
    @Column(name = "REGISTRATION_NUMBER")
    @Basic
    var registrationNumber: String? = null

    @Column(name = "COMPANY_TELEPHONE")
    @Basic
    var companyTelephone: String? = null

    @Column(name = "COMPANY_EMAIL")
    @Basic
    var companyEmail: String? = null

    @Column(name = "ENTRY_NUMBER")
    @Basic
    var entryNumber: String? = null

    @Column(name = "POSTAL_ADDRESS")
    @Basic
    var postalAddress: String? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int = 0

    @Column(name = "REGISTRATION_DATE")
    @Basic
    var registrationDate: Date? = null

    @Column(name = "VAR_FIELD1")
    @Basic
    var varField1: String? = null

    @Column(name = "VAR_FIELD2")
    @Basic
    var varField2: String? = null

    @Column(name = "VAR_FIELD3")
    @Basic
    var varField3: String? = null

    @Column(name = "VAR_FIELD4")
    @Basic
    var varField4: String? = null

    @Column(name = "VAR_FIELD5")
    @Basic
    var varField5: String? = null

    @Column(name = "VAR_FIELD6")
    @Basic
    var varField6: String? = null

    @Column(name = "VAR_FIELD7")
    @Basic
    var varField7: String? = null

    @Column(name = "VAR_FIELD8")
    @Basic
    var varField8: String? = null

    @Column(name = "VAR_FIELD9")
    @Basic
    var varField9: String? = null

    @Column(name = "VAR_FIELD10")
    @Basic
    var varField10: String? = null

    @Column(name = "CREATED_BY")
    @Basic
    var createdBy: Long? = null

    @Column(name = "CREATED_ON")
    @Basic
    var createdOn: Date? = null

    @Column(name = "MODIFIED_BY")
    @Basic
    var modifiedBy: Long? = null

    @Column(name = "VERSION")
    @Basic
    var version: Int? = null

    @Column(name = "MODIFIED_DATE")
    @Basic
    var modifiedDate: Date? = null

//    @JoinColumn(name = "BUSINESS_NATURE_ID", referencedColumnName = "ID")
//    @ManyToOne
//    var businessNatureId: BusinessNatureEntity? = null
//
//    @Transient
//    var confirmBusinessNatureId: Long? = 0

//    @JoinColumn(name = "BUSINESS_LINE_ID", referencedColumnName = "ID")
//    @ManyToOne
//    var businessLineId: BusinessLinesEntity? = null
//
//    @Transient
//    var confirmLineBusinessId: Long? = 0

    @Column(name = "BUSINESS_LINE_ID")
    @Basic
    var businessLineId: Long? = null

    @Column(name = "BUSINESS_NATURE_ID")
    @Basic
    var businessNatureId: Long? = null

    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    @ManyToOne
    var userId: UsersEntity? = null

    @JoinColumn(name = "DEPARTMENT_CODE", referencedColumnName = "ID")
    @ManyToOne
//    @JsonBackReference(value="user-movement")
    var departmentCode: ManufacturerDepartmentsEntity? = null


//    @OneToMany(
//            mappedBy = "manufacturerId",
//            orphanRemoval = true,
//            cascade = [CascadeType.PERSIST, CascadeType.MERGE]
//    )
//    @LazyCollection(LazyCollectionOption.FALSE)
//    @JsonManagedReference
//    var manufacturerAddresses: MutableList<ManufacturerAddressesEntity> = mutableListOf()
//
//    @OneToMany(
//            mappedBy = "manufacturerId",
//            orphanRemoval = true,
//            cascade = [CascadeType.PERSIST, CascadeType.MERGE]
//    )
//    @LazyCollection(LazyCollectionOption.FALSE)
//    @JsonManagedReference
//    var manufacturerContacts: MutableList<ManufacturerContactsEntity> = mutableListOf()
//
//
//    @OneToMany(mappedBy = "manufacturerId")
//    @LazyCollection(LazyCollectionOption.FALSE)
//    @JsonManagedReference
//    var paymentsLogs: Collection<PaymentsLogEntity>? = null
//
//    @OneToMany(mappedBy = "manufacturerId")
//    @LazyCollection(LazyCollectionOption.FALSE)
//    @JsonManagedReference
//    var brsLookupManufacturerData: Collection<BrsLookupManufacturerDataEntity>? = null
//
//    @OneToMany(mappedBy = "manufacturerId")
//    @LazyCollection(LazyCollectionOption.FALSE)
//    @JsonManagedReference
//    var sdlSl1Forms: Collection<SdlSl1FormsEntity>? = null


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as ManufacturersEntity
        return id == that.id &&
                status == that.status &&
                name == that.name &&
                kraPin == that.kraPin &&
                registrationNumber == that.registrationNumber &&
                businessLineId == that.businessLineId &&
                businessNatureId == that.businessNatureId &&
                entryNumber == that.entryNumber &&
                postalAddress == that.postalAddress &&
                registrationDate == that.registrationDate &&
                varField1 == that.varField1 &&
                varField2 == that.varField2 &&
                varField3 == that.varField3 &&
                varField4 == that.varField4 &&
                varField5 == that.varField5 &&
                varField6 == that.varField6 &&
                varField7 == that.varField7 &&
                varField8 == that.varField8 &&
                ownership == that.ownership &&
                varField9 == that.varField9 &&
                varField10 == that.varField10 &&
                closureOfOperations == that.closureOfOperations &&
                createdBy == that.createdBy &&
                createdOn == that.createdOn &&
                modifiedBy == that.modifiedBy &&
                modifiedDate == that.modifiedDate &&
                factoryVisitDate == that.factoryVisitDate &&
                factoryVisitStatus == that.factoryVisitStatus
    }

    override fun hashCode(): Int {
        return Objects.hash(
                id,
                name,
                kraPin,
                businessLineId,
                businessNatureId,
                registrationNumber,
                entryNumber,
                postalAddress,
                status,
                registrationDate,
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
                ownership,
                createdOn,
                modifiedBy,
                closureOfOperations,
                modifiedDate,
                factoryVisitDate,
                factoryVisitStatus
        )
    }

}