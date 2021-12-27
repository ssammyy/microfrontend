package org.kebs.app.kotlin.apollo.store.model

import org.kebs.app.kotlin.apollo.store.model.ms.MsFuelInspectionEntity
import java.io.Serializable
import java.sql.Date
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_MS_SAMPLE_SUBMISSION")
class MsSampleSubmissionEntity : Serializable{
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_MS_SAMPLE_SUBMISSION_SEQ_GEN", allocationSize = 1, sequenceName = "DAT_KEBS_MS_SAMPLE_SUBMISSION_SEQ")
    @GeneratedValue(generator = "DAT_KEBS_MS_SAMPLE_SUBMISSION_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = 0

    @Column(name = "NAME_PRODUCT")
    @Basic
    var nameProduct: String? = null

    @Column(name = "BS_NUMBER")
    @Basic
    var bsNumber: String? = null

    @Column(name = "LAB_REF")
    @Basic
    var labRef: String? = null

    @Column(name = "ADDITIONAL_INF_PROVIDED_CUSTOMER")
    @Basic
    var additionalInfProvidedCustomer: String? = null

    @Column(name = "DATE_ANALYSIS_STARTED")
    @Basic
    var dateAnalsyisStarted: Date? = null

    @Column(name = "DATE_OF_RECEIPT")
    @Basic
    var dateOfReceipt: Date? = null

    @Column(name = "SIZE_TEST_SAMPLE")
    @Basic
    var sizeTestSample: Long? = null

    @Column(name = "SIZE_REF_SAMPLE")
    @Basic
    var sizeRefSample: Long? = null

    @Column(name = "FILE_REF_NUMBER")
    @Basic
    var fileRefNumber: String? = null

    @Column(name = "PACKAGING")
    @Basic
    var packaging: String? = null

    @Column(name = "LABELLING_IDENTIFICATION")
    @Basic
    var labellingIdentification: String? = null

    @Column(name = "CONDITION")
    @Basic
    var condition: String? = null

    @Column(name = "REFERENCES_STANDARDS")
    @Basic
    var referencesStandards: String? = null

    @Column(name = "SENDERS_NAME")
    @Basic
    var sendersName: String? = null

    @Column(name = "DESIGNATION")
    @Basic
    var designation: String? = null

    @Column(name = "SIGNATURE")
    @Basic
    var signature: String? = null

    @Column(name = "ADDRESS")
    @Basic
    var address: String? = null

    @Column(name = "SENDERS_DATE")
    @Basic
    var sendersDate: String? = null

    @Column(name = "RECEIVERS_NAME")
    @Basic
    var receiversName: String? = null

    @Column(name = "SAMPLE_REFERENCES")
    @Basic
    var sampleReferences: String? = null

    @Column(name = "TEST_CHARGES_KSH")
    @Basic
    var testChargesKsh: Long? = null

    @Column(name = "RECEIPT_LPO_NUMBER")
    @Basic
    var receiptLpoNumber: String? = null

    @Column(name = "INVOICE_NUMBER")
    @Basic
    var invoiceNumber: String? = null

    @Column(name = "DISPOSAL")
    @Basic
    var disposal: String? = null

    @Column(name = "REMARKS")
    @Basic
    var remarks: String? = null

    @Column(name = "LABORATORY_NAME1")
    @Basic
    var laboratoryName1: String? = null

    @Column(name = "LABORATORY_NAME2")
    @Basic
    var laboratoryName2: String? = null

    @Column(name = "LABORATORY_NAME3")
    @Basic
    var laboratoryName3: String? = null

    @Column(name = "LABORATORY_NAME4")
    @Basic
    var laboratoryName4: String? = null

    @Column(name = "LABORATORY_NAME5")
    @Basic
    var laboratoryName5: String? = null

    @Column(name = "LABORATORY_NAME6")
    @Basic
    var laboratoryName6: String? = null

    @Column(name = "LABORATORY_NAME7")
    @Basic
    var laboratoryName7: String? = null

    @Column(name = "LABORATORY_NAME8")
    @Basic
    var laboratoryName8: String? = null

    @Column(name = "LABORATORY_NAME9")
    @Basic
    var laboratoryName9: String? = null

    @Column(name = "LABORATORY_NAME10")
    @Basic
    var laboratoryName10: String? = null


    @Column(name = "PARAMETER_1")
    @Basic
    var parameter1: String? = null

    @Column(name = "PARAMETER_2")
    @Basic
    var parameter2: String? = null

    @Column(name = "PARAMETER_3")
    @Basic
    var parameter3: String? = null

    @Column(name = "PARAMETER_4")
    @Basic
    var parameter4: String? = null

    @Column(name = "PARAMETER_5")
    @Basic
    var parameter5: String? = null

    @Column(name = "PARAMETER_6")
    @Basic
    var parameter6: String? = null

    @Column(name = "PARAMETER_7")
    @Basic
    var parameter7: String? = null

    @Column(name = "PARAMETER_8")
    @Basic
    var parameter8: String? = null

    @Column(name = "PARAMETER_9")
    @Basic
    var parameter9: String? = null

    @Column(name = "PARAMETER_10")
    @Basic
    var parameter10: String? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int? = 0

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

    @JoinColumn(name = "MS_WORKPLAN_GENERATED_ID", referencedColumnName = "ID")
    @ManyToOne
    var workPlanGeneratedID: MsWorkPlanGeneratedEntity? = null

    @JoinColumn(name = "MS_FUEL_INSPECTION_ID", referencedColumnName = "ID")
    @ManyToOne
    var msFuelInspectionId: MsFuelInspectionEntity? = null

//    @OneToMany(mappedBy = "datKebsMsSampleSubmissionBySampleSubmissionId")
//    var datKebsMsLaboratoryParametersById: Collection<MsLaboratoryParametersEntity>? = null

    @JoinColumn(name = "SAMPLE_COLLECTION_NUMBER", referencedColumnName = "ID")
    @ManyToOne
    var sampleCollectionNumber: MsSampleCollectionEntity? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as MsSampleSubmissionEntity
        return id == that.id &&

                parameter1 ==  that.parameter1 &&
                parameter2 ==  that.parameter2 &&
                parameter3 ==  that.parameter3 &&
                parameter4 ==  that.parameter4 &&
                parameter5 ==  that.parameter5 &&
                parameter6 ==  that.parameter6 &&
                parameter7 ==  that.parameter7 &&
                parameter8 ==  that.parameter8 &&
                parameter9 ==  that.parameter9 &&
                parameter10 ==  that.parameter10 &&

                laboratoryName1 ==  that.laboratoryName1 &&
                laboratoryName2 ==  that.laboratoryName2 &&
                laboratoryName3 ==  that.laboratoryName3 &&
                laboratoryName4 ==  that.laboratoryName4 &&
                laboratoryName5 ==  that.laboratoryName5 &&
                laboratoryName6 ==  that.laboratoryName6 &&
                laboratoryName7 ==  that.laboratoryName7 &&
                laboratoryName8 ==  that.laboratoryName8 &&
                laboratoryName9 ==  that.laboratoryName9 &&

                labRef == that.labRef &&
                dateOfReceipt == that.dateOfReceipt &&
                dateAnalsyisStarted == that.dateAnalsyisStarted &&
                additionalInfProvidedCustomer == that.additionalInfProvidedCustomer &&
                nameProduct == that.nameProduct &&
                bsNumber == that.bsNumber &&
                sizeTestSample == that.sizeTestSample &&
                sizeRefSample == that.sizeRefSample &&
                fileRefNumber == that.fileRefNumber &&
                packaging == that.packaging &&
                labellingIdentification == that.labellingIdentification &&
                condition == that.condition &&
                referencesStandards == that.referencesStandards &&
                sendersName == that.sendersName &&
                designation == that.designation &&
                signature == that.signature &&
                address == that.address &&
                sendersDate == that.sendersDate &&
                receiversName == that.receiversName &&
                sampleReferences == that.sampleReferences &&
                testChargesKsh == that.testChargesKsh &&
                receiptLpoNumber == that.receiptLpoNumber &&
                invoiceNumber == that.invoiceNumber &&
                disposal == that.disposal &&
                remarks == that.remarks &&
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
        return Objects.hash(id, nameProduct, bsNumber,
                labRef ,
                        dateOfReceipt ,
                        dateAnalsyisStarted ,
                        additionalInfProvidedCustomer ,
                parameter1,
                parameter2,
                parameter3,
                parameter4,
                parameter5,
                parameter6,
                parameter7,
                parameter8,
                parameter9,
                parameter10,
                laboratoryName1,
                laboratoryName2,
                laboratoryName3,
                laboratoryName4,
                laboratoryName5,
                laboratoryName6,
                laboratoryName7,
                laboratoryName8,
                laboratoryName9,
                laboratoryName10,sizeTestSample, sizeRefSample, fileRefNumber, packaging, labellingIdentification, condition, referencesStandards, sendersName, designation, signature, address, sendersDate, receiversName, sampleReferences, testChargesKsh, receiptLpoNumber, invoiceNumber, disposal, remarks, status, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn)
    }

}
