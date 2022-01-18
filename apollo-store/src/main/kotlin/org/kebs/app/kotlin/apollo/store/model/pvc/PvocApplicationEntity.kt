package org.kebs.app.kotlin.apollo.store.model.pvc

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_PVOC_APPLICATION")
class PvocApplicationEntity : Serializable {
    @Column(name = "ID", nullable = false, precision = 0)
    @Id
    @SequenceGenerator(name = "DAT_KEBS_PVOC_APPLICATION_SEQ_GEN", sequenceName = "DAT_KEBS_PVOC_APPLICATION_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_PVOC_APPLICATION_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long? = 0
    @Column(name = "STATUS")
    @Basic
    var status: Int? = null
    @Column(name = "TERMS_CONDITIONS")
    @Basic
    var termsConditions: Int? = null
    @Column(name = "CONPANY_NAME")
    @Basic
    var conpanyName: String? = null
    @Column(name = "COMPANY_PIN_NO")
    @Basic
    var companyPinNo: String? = null
    @Column(name = "EMAIL")
    @Basic
    var email: String? = null

    @Column(name = "SN")
    @Basic
    var sn: String? = null

    @Column(name = "FINISHED")
    @Basic
    var finished: Int? = null

    @Column(name = "TELEPHONE_NO")
    @Basic
    var telephoneNo: String? = null
    @Column(name = "POSTAL_AADRESS")
    @Basic
    var postalAadress: String? = null
    @Column(name = "PHYSICAL_LOCATION")
    @Basic
    var physicalLocation: String? = null
    @Column(name = "CONTACT_PERSORN")
    @Basic
    var contactPersorn: String? = null
    @Column(name = "ADDRESS")
    @Basic
    var address: String? = null
    @Column(name = "EXCEPTION_CATEGORY")
    @Basic
    var exceptionCategory: String? = null
    @Column(name = "HS_CODE")
    @Basic
    var hsCode: String? = null
    @Column(name = "COUNTRY_OF_ORIGIN")
    @Basic
    var countryOfOrigin: String? = null
    @Column(name = "INDUSTRIAL_SPARES_DESCRIPTION")
    @Basic
    var industrialSparesDescription: String? = null
    @Column(name = "MACHINE_TO_BE_FITTED")
    @Basic
    var machineToBeFitted: String? = null
    @Column(name = "RAW_MATERIAL_DESCRIPTION")
    @Basic
    var rawMaterialDescription: String? = null
    @Column(name = "END_PRODUCT")
    @Basic
    var endProduct: String? = null
    @Column(name = "DUTY_RATE")
    @Basic
    var dutyRate: String? = null
    @Column(name = "MACHINE_DESCRIPTION")
    @Basic
    var machineDescription: String? = null
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
    @Column(name = "SECTION")
    @Basic
    var section: String? = null
    @Column(name = "APPLICATION_TYPE")
    @Basic
    var applicationType: Long? = null
    @Column(name = "MAKE_MODEL")
    @Basic
    var makeModel: String? = null
    @Column(name = "REVIEW_STATUS")
    @Basic
    var reviewStatus: String? = null
    @Column(name = "APPLICATION_DATE")
    @Basic
    var applicationDate: Date? = null
    @Column(name = "COMPANY_NAME_STATUS_REVIEW")
    @Basic
    var companyNameStatusReview: String? = null
    @Column(name = "COMPANY_PIN_STATUS_REVIEW")
    @Basic
    var companyPinStatusReview: String? = null
    @Column(name = "EMAIL_STATUS_REVIEW")
    @Basic
    var emailStatusReview: String? = null
    @Column(name = "TELEPHONE_STATUS_REVIEW")
    @Basic
    var telephoneStatusReview: String? = null
    @Column(name = "PHYSICAL_STATUS_REVIEW")
    @Basic
    var physicalStatusReview: String? = null
    @Column(name = "CONTACT_PERSON_STATUS_REVIEW")
    @Basic
    var contactPersonStatusReview: String? = null
    @Column(name = "ADDRESS_STATUS_REVIEW")
    @Basic
    var addressStatusReview: String? = null
    @Column(name = "EXCEPTION_CATEGORY_STATUS_REVIEW")
    @Basic
    var exceptionCategoryStatusReview: String? = null
    @Column(name = "HSCODE_STATUS_REVIEW")
    @Basic
    var hscodeStatusReview: String? = null
    @Column(name = "CONTRY_OF_ORIGIN_STATUS_REVIEW")
    @Basic
    var contryOfOriginStatusReview: String? = null
    @Column(name = "INDUSTRIAL_SPARES_STATUS_REVIEW")
    @Basic
    var industrialSparesStatusReview: String? = null

    @Column(name = "POSTAL_AADRESS_STATUS_REVIEW")
    @Basic
    var postalAadressStatusReview: String? = null
    @Column(name = "MACHINE_TO_BE_FITTED_STATUS_REVIEW")
    @Basic
    var machineToBeFittedStatusReview: String? = null
    @Column(name = "RAW_MATERIAL_DESCRIPTION_STATUS_REVIEW")
    @Basic
    var rawMaterialDescriptionStatusReview: String? = null
    @Column(name = "END_PRODUCT_STATUS_REVIEW")
    @Basic
    var endProductStatusReview: String? = null
    @Column(name = "DUTY_RATE_STATUS_REVIEW")
    @Basic
    var dutyRateStatusReview: String? = null
    @Column(name = "MACHINE_DESCRIPTION_STATUS_REVIEW")
    @Basic
    var machineDescriptionStatusReview: String? = null

    @Column(name = "APPLICATION_DATE_STATUS_REVIEW")
    @Basic
    var applicationDateStatusReview: String? = null
    @Column(name = "SECTION_ID_OFFICER")
    @Basic
    var sectionIdOfficer: Long? = null
    @Column(name = "PHYSICAL_LOCATION_STATUS_REVIEW")
    @Basic
    var physicalLocationStatusReview: String? = null

    @Column(name = "PVOC_EA_STATUS")
    @Basic
    var pvocEaStatus: Int? = null

    @Column(name = "PVOC_EA_STARTED_ON")
    @Basic
    var pvocEaStartedOn: Timestamp? = null

    @Column(name = "PVOC_EA_COMPLETED_ON")
    @Basic
    var pvocEaCompletedOn: Timestamp? = null

    @Column(name = "PVOC_EA_PROCESS_INSTANCE_ID")
    @Basic
    var pvocEaProcessInstanceId: String? = null

    @Column(name = "PVOC_WA_STATUS")
    @Basic
    var pvocWaStatus: Int? = null

    @Column(name = "PVOC_WA_STARTED_ON")
    @Basic
    var pvocWaStartedOn: Timestamp? = null

    @Column(name = "PVOC_WA_COMPLETED_ON")
    @Basic
    var pvocWaCompletedOn: Timestamp? = null

    @Column(name = "PVOC_WA_PROCESS_INSTANCE_ID")
    @Basic
    var pvocWaProcessInstanceId: String? = null

    @Column(name = "FINAL_APPROVAL")
    @Basic
    var finalApproval: String? = null

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as PvocApplicationEntity
        return id == that.id &&
                status == that.status &&
                conpanyName == that.conpanyName &&
                companyPinNo == that.companyPinNo &&
                email == that.email &&
                telephoneNo == that.telephoneNo &&
                sn == that.sn &&
                postalAadress == that.postalAadress &&
                physicalLocation == that.physicalLocation &&
                termsConditions == that.termsConditions &&
                contactPersorn == that.contactPersorn &&
                address == that.address &&
                exceptionCategory == that.exceptionCategory &&
                hsCode == that.hsCode &&
                countryOfOrigin == that.countryOfOrigin &&
                industrialSparesDescription == that.industrialSparesDescription &&
                machineToBeFitted == that.machineToBeFitted &&
                rawMaterialDescription == that.rawMaterialDescription &&
                endProduct == that.endProduct &&
                dutyRate == that.dutyRate &&
                finished == that.finished &&
                machineDescription == that.machineDescription &&
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
                deletedOn == that.deletedOn &&
                section == that.section &&
                applicationType == that.applicationType &&
                makeModel == that.makeModel &&
                reviewStatus == that.reviewStatus &&
                applicationDate == that.applicationDate &&
                companyNameStatusReview == that.companyNameStatusReview &&
                companyPinStatusReview == that.companyPinStatusReview &&
                emailStatusReview == that.emailStatusReview &&
                telephoneStatusReview == that.telephoneStatusReview &&
                physicalStatusReview == that.physicalStatusReview &&
                contactPersonStatusReview == that.contactPersonStatusReview &&
                addressStatusReview == that.addressStatusReview &&
                exceptionCategoryStatusReview == that.exceptionCategoryStatusReview &&
                hscodeStatusReview == that.hscodeStatusReview &&
                contryOfOriginStatusReview == that.contryOfOriginStatusReview &&
                industrialSparesStatusReview == that.industrialSparesStatusReview &&
                machineToBeFittedStatusReview == that.machineToBeFittedStatusReview &&
                rawMaterialDescriptionStatusReview == that.rawMaterialDescriptionStatusReview &&
                endProductStatusReview == that.endProductStatusReview &&
                dutyRateStatusReview == that.dutyRateStatusReview &&
                applicationDateStatusReview == that.applicationDateStatusReview &&
                machineDescriptionStatusReview == that.machineDescriptionStatusReview &&
                physicalLocationStatusReview == that.physicalLocationStatusReview &&
                sectionIdOfficer == that.sectionIdOfficer &&
                finalApproval == that.finalApproval &&
                postalAadressStatusReview == that.postalAadressStatusReview
    }

    override fun hashCode(): Int {
        return Objects.hash(id, status, conpanyName, companyPinNo, email, sn, finished, termsConditions, telephoneNo, postalAadress, physicalLocation, contactPersorn, address, exceptionCategory, hsCode, countryOfOrigin, industrialSparesDescription, machineToBeFitted, rawMaterialDescription, endProduct, dutyRate, machineDescription, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn, section, applicationType, makeModel, reviewStatus, applicationDate, companyNameStatusReview, companyPinStatusReview, emailStatusReview, telephoneStatusReview, physicalStatusReview, contactPersonStatusReview, addressStatusReview, exceptionCategoryStatusReview, hscodeStatusReview, contryOfOriginStatusReview, industrialSparesStatusReview, machineToBeFittedStatusReview, rawMaterialDescriptionStatusReview, endProductStatusReview, dutyRateStatusReview, machineDescriptionStatusReview, applicationDateStatusReview, physicalLocationStatusReview, sectionIdOfficer, finalApproval,postalAadressStatusReview)
    }
}