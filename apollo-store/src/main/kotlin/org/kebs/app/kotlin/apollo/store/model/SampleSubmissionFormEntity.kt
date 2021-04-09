/*
 *
 *  *
 *  *
 *  *  *    Copyright (c) ${YEAR}.   BSK Global Technologies
 *  *  *
 *  *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *    you may not use this file except in compliance with the License.
 *  *  *    You may obtain a copy of the License at
 *  *  *
 *  *  *       http://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  *    Unless required by applicable law or agreed to in writing, software
 *  *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *   See the License for the specific language governing permissions and
 *  *  *   limitations under the License.
 *  *
 *
 */

package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Time
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_SAMPLE_SUBMISSION_FORM")
class SampleSubmissionFormEntity : Serializable {
    @Column(name = "ID")
    @Id
    var id: Long = 0

    @Column(name = "PRODUCT")
    @Basic
    var product: String? = null

    @Column(name = "PACKAGING")
    @Basic
    var packaging: String? = null

    @Column(name = "TRADEMARK")
    @Basic
    var trademark: String? = null

    @Column(name = "DATE_OF_MANUFACTURE")
    @Basic
    var dateOfManufacture: Time? = null

    @Column(name = "EXPIRELY_DATE")
    @Basic
    var expirelyDate: Time? = null

    @Column(name = "CONTENT_DECLARED")
    @Basic
    var contentDeclared: String? = null

    @Column(name = "BATCH_NO")
    @Basic
    var batchNo: String? = null

    @Column(name = "ANY_OTHER_MARKING")
    @Basic
    var anyOtherMarking: String? = null

    @Column(name = "SITE_OF_TEST_SAMPLE")
    @Basic
    var siteOfTestSample: String? = null

    @Column(name = "SITE_OF_REFERENCE_SAMPLE")
    @Basic
    var siteOfReferenceSample: String? = null

    @Column(name = "CONDITION_OF_SAMPLE")
    @Basic
    var conditionOfSample: String? = null

    @Column(name = "REFERENCE_STANDARD_SPECIFICATION")
    @Basic
    var referenceStandardSpecification: String? = null

    @Column(name = "TEST_REQUEST")
    @Basic
    var testRequest: String? = null

    @Column(name = "LABORATORY")
    @Basic
    var laboratory: String? = null

    @Column(name = "PURPOSE_OF_TEST")
    @Basic
    var purposeOfTest: String? = null

    @Column(name = "PARAMETER_TO_TESTED")
    @Basic
    var parameterToTested: String? = null

    @Column(name = "CUSTOMER_NAME")
    @Basic
    var customerName: String? = null

    @Column(name = "ORGANIZATION")
    @Basic
    var organization: String? = null

    @Column(name = "ORGANIZATION_ADDRESS")
    @Basic
    var organizationAddress: String? = null

    @Column(name = "ORGANIZATION_TEL")
    @Basic
    var organizationTel: String? = null

    @Column(name = "ORGANIZATION_EMAIL")
    @Basic
    var organizationEmail: String? = null

    @Column(name = "ORGANIZATION_SIGNTURE")
    @Basic
    var organizationSignture: String? = null

    @Column(name = "ORGANIZATION_SIGNTURE_DATE")
    @Basic
    var organizationSigntureDate: String? = null

    @Column(name = "FILE_REFERENCE_NO")
    @Basic
    var fileReferenceNo: String? = null

    @Column(name = "SCF_NO")
    @Basic
    var scfNo: String? = null

    @Column(name = "SAMPLE_REFERENCE_NO")
    @Basic
    var sampleReferenceNo: String? = null

    @Column(name = "TEST_CHARGES")
    @Basic
    var testCharges: Long? = null

    @Column(name = "RECEIPT_LPO_NO")
    @Basic
    var receiptLpoNo: String? = null

    @Column(name = "INVOICE_NO")
    @Basic
    var invoiceNo: String? = null

    @Column(name = "RECIEVERS_NAME")
    @Basic
    var recieversName: String? = null

    @Column(name = "SIGNTURE_DATE_RECIEVER")
    @Basic
    var signtureDateReciever: Time? = null

    @Column(name = "NOTES_ON_TRASMISSION_OF_RESULT")
    @Basic
    var notesOnTrasmissionOfResult: String? = null

    @Column(name = "DISPOSAL")
    @Basic
    var disposal: String? = null

    @Column(name = "COC_NO")
    @Basic
    var cocNo: String? = null

    @Column(name = "REMARKS")
    @Basic
    var remarks: String? = null

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

    @Column(name = "VERSION")
    @Basic
    var version: Long? = null

    @JoinColumn(name = "GENERAL_FORM_ID", referencedColumnName = "ID")
    @ManyToOne
    var generalFormId: InspectionGeneralFormEntity? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as SampleSubmissionFormEntity
        if (id != that.id) return false
        if (if (product != null) product != that.product else that.product != null) return false
        if (if (packaging != null) packaging != that.packaging else that.packaging != null) return false
        if (if (trademark != null) trademark != that.trademark else that.trademark != null) return false
        if (if (dateOfManufacture != null) dateOfManufacture != that.dateOfManufacture else that.dateOfManufacture != null) return false
        if (if (expirelyDate != null) expirelyDate != that.expirelyDate else that.expirelyDate != null) return false
        if (if (contentDeclared != null) contentDeclared != that.contentDeclared else that.contentDeclared != null) return false
        if (if (batchNo != null) batchNo != that.batchNo else that.batchNo != null) return false
        if (if (anyOtherMarking != null) anyOtherMarking != that.anyOtherMarking else that.anyOtherMarking != null) return false
        if (if (siteOfTestSample != null) siteOfTestSample != that.siteOfTestSample else that.siteOfTestSample != null) return false
        if (if (siteOfReferenceSample != null) siteOfReferenceSample != that.siteOfReferenceSample else that.siteOfReferenceSample != null) return false
        if (if (conditionOfSample != null) conditionOfSample != that.conditionOfSample else that.conditionOfSample != null) return false
        if (if (referenceStandardSpecification != null) referenceStandardSpecification != that.referenceStandardSpecification else that.referenceStandardSpecification != null) return false
        if (if (testRequest != null) testRequest != that.testRequest else that.testRequest != null) return false
        if (if (laboratory != null) laboratory != that.laboratory else that.laboratory != null) return false
        if (if (purposeOfTest != null) purposeOfTest != that.purposeOfTest else that.purposeOfTest != null) return false
        if (if (parameterToTested != null) parameterToTested != that.parameterToTested else that.parameterToTested != null) return false
        if (if (customerName != null) customerName != that.customerName else that.customerName != null) return false
        if (if (organization != null) organization != that.organization else that.organization != null) return false
        if (if (organizationAddress != null) organizationAddress != that.organizationAddress else that.organizationAddress != null) return false
        if (if (organizationTel != null) organizationTel != that.organizationTel else that.organizationTel != null) return false
        if (if (organizationEmail != null) organizationEmail != that.organizationEmail else that.organizationEmail != null) return false
        if (if (organizationSignture != null) organizationSignture != that.organizationSignture else that.organizationSignture != null) return false
        if (if (organizationSigntureDate != null) organizationSigntureDate != that.organizationSigntureDate else that.organizationSigntureDate != null) return false
        if (if (fileReferenceNo != null) fileReferenceNo != that.fileReferenceNo else that.fileReferenceNo != null) return false
        if (if (scfNo != null) scfNo != that.scfNo else that.scfNo != null) return false
        if (if (sampleReferenceNo != null) sampleReferenceNo != that.sampleReferenceNo else that.sampleReferenceNo != null) return false
        if (if (testCharges != null) testCharges != that.testCharges else that.testCharges != null) return false
        if (if (receiptLpoNo != null) receiptLpoNo != that.receiptLpoNo else that.receiptLpoNo != null) return false
        if (if (invoiceNo != null) invoiceNo != that.invoiceNo else that.invoiceNo != null) return false
        if (if (recieversName != null) recieversName != that.recieversName else that.recieversName != null) return false
        if (if (signtureDateReciever != null) signtureDateReciever != that.signtureDateReciever else that.signtureDateReciever != null) return false
        if (if (notesOnTrasmissionOfResult != null) notesOnTrasmissionOfResult != that.notesOnTrasmissionOfResult else that.notesOnTrasmissionOfResult != null) return false
        if (if (disposal != null) disposal != that.disposal else that.disposal != null) return false
        if (if (cocNo != null) cocNo != that.cocNo else that.cocNo != null) return false
        if (if (remarks != null) remarks != that.remarks else that.remarks != null) return false
        if (if (dateSubmitted != null) dateSubmitted != that.dateSubmitted else that.dateSubmitted != null) return false
        if (if (varField1 != null) varField1 != that.varField1 else that.varField1 != null) return false
        if (if (varField2 != null) varField2 != that.varField2 else that.varField2 != null) return false
        if (if (varField3 != null) varField3 != that.varField3 else that.varField3 != null) return false
        if (if (varField4 != null) varField4 != that.varField4 else that.varField4 != null) return false
        if (if (varField5 != null) varField5 != that.varField5 else that.varField5 != null) return false
        if (if (varField6 != null) varField6 != that.varField6 else that.varField6 != null) return false
        if (if (varField7 != null) varField7 != that.varField7 else that.varField7 != null) return false
        if (if (varField8 != null) varField8 != that.varField8 else that.varField8 != null) return false
        if (if (varField9 != null) varField9 != that.varField9 else that.varField9 != null) return false
        if (if (varField10 != null) varField10 != that.varField10 else that.varField10 != null) return false
        if (if (createdBy != null) createdBy != that.createdBy else that.createdBy != null) return false
        if (if (createdOn != null) !createdOn!!.equals(that.createdOn) else that.createdOn != null) return false
        if (if (lastModifiedBy != null) lastModifiedBy != that.lastModifiedBy else that.lastModifiedBy != null) return false
        if (if (lastModifiedOn != null) !lastModifiedOn!!.equals(that.lastModifiedOn) else that.lastModifiedOn != null) return false
        if (if (updateBy != null) updateBy != that.updateBy else that.updateBy != null) return false
        if (if (updatedOn != null) !updatedOn!!.equals(that.updatedOn) else that.updatedOn != null) return false
        if (if (deleteBy != null) deleteBy != that.deleteBy else that.deleteBy != null) return false
        if (if (deletedOn != null) !deletedOn!!.equals(that.deletedOn) else that.deletedOn != null) return false
        return !if (version != null) version != that.version else that.version != null
    }

    override fun hashCode(): Int {
        var result = (id xor (id ushr 32)).toInt()
        result = 31 * result + if (product != null) product.hashCode() else 0
        result = 31 * result + if (packaging != null) packaging.hashCode() else 0
        result = 31 * result + if (trademark != null) trademark.hashCode() else 0
        result = 31 * result + if (dateOfManufacture != null) dateOfManufacture.hashCode() else 0
        result = 31 * result + if (expirelyDate != null) expirelyDate.hashCode() else 0
        result = 31 * result + if (contentDeclared != null) contentDeclared.hashCode() else 0
        result = 31 * result + if (batchNo != null) batchNo.hashCode() else 0
        result = 31 * result + if (anyOtherMarking != null) anyOtherMarking.hashCode() else 0
        result = 31 * result + if (siteOfTestSample != null) siteOfTestSample.hashCode() else 0
        result = 31 * result + if (siteOfReferenceSample != null) siteOfReferenceSample.hashCode() else 0
        result = 31 * result + if (conditionOfSample != null) conditionOfSample.hashCode() else 0
        result = 31 * result + if (referenceStandardSpecification != null) referenceStandardSpecification.hashCode() else 0
        result = 31 * result + if (testRequest != null) testRequest.hashCode() else 0
        result = 31 * result + if (laboratory != null) laboratory.hashCode() else 0
        result = 31 * result + if (purposeOfTest != null) purposeOfTest.hashCode() else 0
        result = 31 * result + if (parameterToTested != null) parameterToTested.hashCode() else 0
        result = 31 * result + if (customerName != null) customerName.hashCode() else 0
        result = 31 * result + if (organization != null) organization.hashCode() else 0
        result = 31 * result + if (organizationAddress != null) organizationAddress.hashCode() else 0
        result = 31 * result + if (organizationTel != null) organizationTel.hashCode() else 0
        result = 31 * result + if (organizationEmail != null) organizationEmail.hashCode() else 0
        result = 31 * result + if (organizationSignture != null) organizationSignture.hashCode() else 0
        result = 31 * result + if (organizationSigntureDate != null) organizationSigntureDate.hashCode() else 0
        result = 31 * result + if (fileReferenceNo != null) fileReferenceNo.hashCode() else 0
        result = 31 * result + if (scfNo != null) scfNo.hashCode() else 0
        result = 31 * result + if (sampleReferenceNo != null) sampleReferenceNo.hashCode() else 0
        result = 31 * result + if (testCharges != null) testCharges.hashCode() else 0
        result = 31 * result + if (receiptLpoNo != null) receiptLpoNo.hashCode() else 0
        result = 31 * result + if (invoiceNo != null) invoiceNo.hashCode() else 0
        result = 31 * result + if (recieversName != null) recieversName.hashCode() else 0
        result = 31 * result + if (signtureDateReciever != null) signtureDateReciever.hashCode() else 0
        result = 31 * result + if (notesOnTrasmissionOfResult != null) notesOnTrasmissionOfResult.hashCode() else 0
        result = 31 * result + if (disposal != null) disposal.hashCode() else 0
        result = 31 * result + if (cocNo != null) cocNo.hashCode() else 0
        result = 31 * result + if (remarks != null) remarks.hashCode() else 0
        result = 31 * result + if (dateSubmitted != null) dateSubmitted.hashCode() else 0
        result = 31 * result + if (varField1 != null) varField1.hashCode() else 0
        result = 31 * result + if (varField2 != null) varField2.hashCode() else 0
        result = 31 * result + if (varField3 != null) varField3.hashCode() else 0
        result = 31 * result + if (varField4 != null) varField4.hashCode() else 0
        result = 31 * result + if (varField5 != null) varField5.hashCode() else 0
        result = 31 * result + if (varField6 != null) varField6.hashCode() else 0
        result = 31 * result + if (varField7 != null) varField7.hashCode() else 0
        result = 31 * result + if (varField8 != null) varField8.hashCode() else 0
        result = 31 * result + if (varField9 != null) varField9.hashCode() else 0
        result = 31 * result + if (varField10 != null) varField10.hashCode() else 0
        result = 31 * result + if (createdBy != null) createdBy.hashCode() else 0
        result = 31 * result + if (createdOn != null) createdOn.hashCode() else 0
        result = 31 * result + if (lastModifiedBy != null) lastModifiedBy.hashCode() else 0
        result = 31 * result + if (lastModifiedOn != null) lastModifiedOn.hashCode() else 0
        result = 31 * result + if (updateBy != null) updateBy.hashCode() else 0
        result = 31 * result + if (updatedOn != null) updatedOn.hashCode() else 0
        result = 31 * result + if (deleteBy != null) deleteBy.hashCode() else 0
        result = 31 * result + if (deletedOn != null) deletedOn.hashCode() else 0
        result = 31 * result + if (version != null) version.hashCode() else 0
        return result
    }

}

