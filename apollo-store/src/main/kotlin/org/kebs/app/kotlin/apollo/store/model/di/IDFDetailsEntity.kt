package org.kebs.app.kotlin.apollo.store.model.di

import java.io.Serializable
import java.math.BigDecimal
import java.sql.Time
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_IDF_DETAILS")
class IDFDetailsEntity : Serializable {

    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_IDF_DETAILS_SEQ_GEN", sequenceName = "DAT_KEBS_IDF_DETAILS_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_IDF_DETAILS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = null

    @Column(name = "USER_ID", nullable = true, length = 200)
    @Basic
    var userId: String? = null

    @Column(name = "MESSAGE_DATE", nullable = true)
    @Basic
    var messageDate: Timestamp? = null

    @Column(name = "MODULE", nullable = true, length = 200)
    @Basic
    var module: String? = null

    @Column(name = "ACTION", nullable = true, precision = 0)
    @Basic
    var action: Long? = null

    @Column(name = "INFORMATION", nullable = true, length = 200)
    @Basic
    var information: String? = null

    @Column(name = "MESSAGE_VERSION", nullable = true, precision = 0)
    @Basic
    var messageVersion: Long? = null

    @Column(name = "DIRECTION", nullable = true, length = 200)
    @Basic
    var direction: String? = null

    @Column(name = "MESSAGE_NATURE", nullable = true, precision = 0)
    @Basic
    var messageNature: Long? = null

    @Column(name = "BASE_DOC_REF_NO", nullable = true, length = 200)
    @Basic
    var baseDocRefNo: String? = null

    @Column(name = "PLACE_OF_LOADING", nullable = true, length = 200)
    @Basic
    var placeOfLoading: String? = null

    @Column(name = "DECLARANT_PIN", nullable = true, length = 200)
    @Basic
    var declarantPin: String? = null

    @Column(name = "DECLARANT_AEO_FLAG", nullable = true, length = 200)
    @Basic
    var declarantAeoFlag: String? = null

    @Column(name = "DECLARANT_REGIME", nullable = true, length = 200)
    @Basic
    var declarantRegime: String? = null

    @Column(name = "DELIV_TERMS_SUB2", nullable = true, length = 200)
    @Basic
    var delivTermsSub2: String? = null

    @Column(name = "TOTAL_CUSTOMS_VALUE", nullable = true, precision = 0)
    @Basic
    var totalCustomsValue: BigDecimal? = null

    @Column(name = "TOTAL_OTHER_CHARGES", nullable = true, precision = 0)
    @Basic
    var totalOtherCharges: BigDecimal? = null

    @Column(name = "ENTRY_DATE", nullable = true)
    @Basic
    var entryDate: Timestamp? = null

    @Column(name = "DELIV_TERMS_SUB1", nullable = true, length = 200)
    @Basic
    var delivTermsSub1: String? = null

    @Column(name = "COMPANY_PIN", nullable = true, length = 200)
    @Basic
    var companyPin: String? = null

    @Column(name = "TOTAL_AMOUNT", nullable = true, precision = 0)
    @Basic
    var totalAmount: BigDecimal? = null

    @Column(name = "REFERENCE_NO", nullable = true, length = 200)
    @Basic
    var refNo: String? = null

    @Column(name = "CURRENCY_CODE", nullable = true, length = 200)
    @Basic
    var currencyCode: String? = null

    @Column(name = "OFFICE_CODE", nullable = true, length = 200)
    @Basic
    var officeCode: String? = null

    @Column(name = "OFFICE_SUB_DIVISION_CODE", nullable = true, length = 200)
    @Basic
    var officeSubDivisionCode: String? = null

    @Column(name = "CD_REGISTRATION_DATE", nullable = true)
    @Basic
    var cdRegistrationDate: Timestamp? = null

    @Column(name = "TOTAL_INSURANCE", nullable = true, precision = 0)
    @Basic
    var totalInsurance: BigDecimal? = null

    @Column(name = "TRANSPORT_TYPE", nullable = true, precision = 0)
    @Basic
    var transportType: Long? = null

    @Column(name = "CONSIGNEE_PIN_NO", nullable = true, length = 200)
    @Basic
    var consigneePinNo: String? = null

    @Column(name = "CONSIGNEE_COUNTRY_CODE", nullable = true, length = 200)
    @Basic
    var consigneeCountryCode: String? = null

    @Column(name = "CONSIGNEE_BUSINESS_NAME", nullable = true, length = 200)
    @Basic
    var consigneeBusinessName: String? = null

    @Column(name = "CONSIGNEE_AEO_FLAG", nullable = true, length = 200)
    @Basic
    var consigneeAeoFlag: String? = null

    @Column(name = "CONSIGNEE_BUSINESS_ADDRESS", nullable = true, length = 200)
    @Basic
    var consigneeBusinessAddress: String? = null

    @Column(name = "CONSIGNEE_BUSINESS_NATION", nullable = true, length = 200)
    @Basic
    var consigneeBusinessNation: String? = null

    @Column(name = "CONSIGNOR_PIN_NO", nullable = true, length = 200)
    @Basic
    var consignorPinNo: String? = null

    @Column(name = "CONSIGNOR_COUNTRY_CODE", nullable = true, length = 200)
    @Basic
    var consignorCountryCode: String? = null

    @Column(name = "CONSIGNOR_BUSINESS_NAME", nullable = true, length = 200)
    @Basic
    var consignorBusinessName: String? = null

    @Column(name = "CONSIGNOR_AEO_FLAG", nullable = true, length = 200)
    @Basic
    var consignorAeoFlag: String? = null

    @Column(name = "CONSIGNOR_BUSINESS_ADDRESS", nullable = true, length = 200)
    @Basic
    var consignorBusinessAddress: String? = null

    @Column(name = "CONSIGNOR_BUSINESS_NATION", nullable = true, length = 200)
    @Basic
    var consignorBusinessNation: String? = null

    @Column(name = "UCR_NO", nullable = true, length = 200)
    @Basic
    var ucrNo: String? = null

    @Column(name = "DESCRIPTION", nullable = true, length = 200)
    @Basic
    var description: String? = null

    @Column(name = "STATUS", nullable = true, precision = 0)
    @Basic
    var status: Long? = null

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

    @Column(name = "VAR_FIELD_6", nullable = true, length = 350)
    @Basic
    var varField6: String? = null

    @Column(name = "VAR_FIELD_7", nullable = true, length = 350)
    @Basic
    var varField7: String? = null

    @Column(name = "VAR_FIELD_8", nullable = true, length = 350)
    @Basic
    var varField8: String? = null

    @Column(name = "VAR_FIELD_9", nullable = true, length = 350)
    @Basic
    var varField9: String? = null

    @Column(name = "VAR_FIELD_10", nullable = true, length = 350)
    @Basic
    var varField10: String? = null

    @Column(name = "CREATED_BY", nullable = false, length = 100)
    @Basic
    var createdBy: String? = null

    @Column(name = "CREATED_ON", nullable = false)
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
        val that = other as IDFDetailsEntity
        return id == that.id && userId == that.userId && messageDate == that.messageDate && module == that.module && action == that.action && information == that.information && messageVersion == that.messageVersion && direction == that.direction && messageNature == that.messageNature && baseDocRefNo == that.baseDocRefNo && placeOfLoading == that.placeOfLoading && declarantPin == that.declarantPin && declarantAeoFlag == that.declarantAeoFlag && declarantRegime == that.declarantRegime && delivTermsSub2 == that.delivTermsSub2 && totalCustomsValue == that.totalCustomsValue && totalOtherCharges == that.totalOtherCharges && entryDate == that.entryDate && delivTermsSub1 == that.delivTermsSub1 && companyPin == that.companyPin && totalAmount == that.totalAmount && officeSubDivisionCode == that.officeSubDivisionCode && refNo == that.refNo && currencyCode == that.currencyCode && officeCode == that.officeCode && cdRegistrationDate == that.cdRegistrationDate && totalInsurance == that.totalInsurance && transportType == that.transportType && consigneePinNo == that.consigneePinNo && consigneeCountryCode == that.consigneeCountryCode && consigneeBusinessName == that.consigneeBusinessName && consigneeAeoFlag == that.consigneeAeoFlag && consigneeBusinessAddress == that.consigneeBusinessAddress && consigneeBusinessNation == that.consigneeBusinessNation && consignorPinNo == that.consignorPinNo && consignorCountryCode == that.consignorCountryCode && consignorBusinessName == that.consignorBusinessName && consignorAeoFlag == that.consignorAeoFlag && consignorBusinessAddress == that.consignorBusinessAddress && consignorBusinessNation == that.consignorBusinessNation && description == that.description && status == that.status && varField1 == that.varField1 && varField2 == that.varField2 && varField3 == that.varField3 && varField4 == that.varField4 && varField5 == that.varField5 && varField6 == that.varField6 && varField7 == that.varField7 && varField8 == that.varField8 && varField9 == that.varField9 && varField10 == that.varField10 && createdBy == that.createdBy && createdOn == that.createdOn && modifiedBy == that.modifiedBy && modifiedOn == that.modifiedOn && deleteBy == that.deleteBy && deletedOn == that.deletedOn
    }

    override fun hashCode(): Int {
        return Objects.hash(
            id,
            userId,
            messageDate,
            module,
            action,
            information,
            messageVersion,
            direction,
            messageNature,
            baseDocRefNo,
            placeOfLoading,
            declarantPin,
            declarantAeoFlag,
            declarantRegime,
            delivTermsSub2,
            totalCustomsValue,
            totalOtherCharges,
            entryDate,
            delivTermsSub1,
            companyPin,
            totalAmount,
            officeSubDivisionCode,
            refNo,
            currencyCode,
            officeCode,
            cdRegistrationDate,
            totalInsurance,
            transportType,
            consigneePinNo,
            consigneeCountryCode,
            consigneeBusinessName,
            consigneeAeoFlag,
            consigneeBusinessAddress,
            consigneeBusinessNation,
            consignorPinNo,
            consignorCountryCode,
            consignorBusinessName,
            consignorAeoFlag,
            consignorBusinessAddress,
            consignorBusinessNation,
            description,
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
