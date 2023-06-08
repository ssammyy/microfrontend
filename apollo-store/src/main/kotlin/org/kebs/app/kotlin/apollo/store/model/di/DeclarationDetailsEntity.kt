package org.kebs.app.kotlin.apollo.store.model.di

import java.io.Serializable
import java.math.BigDecimal
import java.sql.Time
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_DECLARATION_DETAILS")
class DeclarationDetailsEntity : Serializable {

    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_DECLARATION_DETAILS_SEQ_GEN", sequenceName = "DAT_KEBS_DECLARATION_DETAILS_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_DECLARATION_DETAILS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
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

    @Column(name = "DECLARATION_REF_NO", nullable = true, length = 200)
    @Basic
    var declarationRefNo: String? = null

    @Column(name = "OFFICE_CODE", nullable = true, length = 200)
    @Basic
    var officeCode: String? = null

    @Column(name = "OFFICE_SUB_CODE", nullable = true, length = 200)
    @Basic
    var officeSubCode: String? = null

    @Column(name = "DECLARANT_REGIME", nullable = true, length = 200)
    @Basic
    var declarantRegime: String? = null

    @Column(name = "REF_NUM", nullable = true, length = 200)
    @Basic
    var refNum: String? = null

    @Column(name = "TOTAL_INSURANCE", nullable = true, precision = 4)
    @Basic
    var totalInsurance: BigDecimal? = null

    @Column(name = "TOTAL_OTHER_CHARGES", nullable = true, precision = 4)
    @Basic
    var totalOtherCharges: BigDecimal? = null

    @Column(name = "TOTAL_CUSTOMS_VALUE", nullable = true, precision = 4)
    @Basic
    var totalCustomsValue: BigDecimal? = null

    @Column(name = "VALUATION_METHOD", nullable = true, length = 10)
    @Basic
    var valuationMethod: String? = null

    @Column(name = "DECLARANT_PIN", nullable = true, length = 20)
    @Basic
    var declarantPin: String? = null

    @Column(name = "DECLARANT_AEO_FLAG", nullable = true, length = 20)
    @Basic
    var declarantAeoFlag: String? = null

    @Column(name = "COMPANY_PIN", nullable = true, length = 20)
    @Basic
    var companyPin: String? = null

    @Column(name = "LAST_CONSIGNMENT_COUNTRY", nullable = true, length = 10)
    @Basic
    var lastConsignmentCountry: String? = null

    @Column(name = "ENTRY_DATE", nullable = true)
    @Basic
    var entryDate: Timestamp? = null

    @Column(name = "REGION_OF_DESTINATION_CODE", nullable = true, length = 10)
    @Basic
    var regionOfDestinationCode: String? = null

    @Column(name = "IDEN_MEAN_TRANS_DEP_ARR", nullable = true, length = 50)
    @Basic
    var idenMeanTransDepArr: String? = null

    @Column(name = "NATIONALITY_TRANS_DEP_ARR", nullable = true, length = 10)
    @Basic
    var nationalityTransDepArr: String? = null

    @Column(name = "DELIV_TERMS_SUB1", nullable = true, length = 100)
    @Basic
    var delivTermsSub1: String? = null

    @Column(name = "DELIV_TERMS_SUB2", nullable = true, length = 100)
    @Basic
    var delivTermsSub2: String? = null

    @Column(name = "CURRENCY_CODE", nullable = true, length = 5)
    @Basic
    var currencyCode: String? = null

    @Column(name = "TOTAL_AMOUNT", nullable = true, precision = 4)
    @Basic
    var totalAmount: BigDecimal? = null

    @Column(name = "EXCHANGE_RATE", nullable = true, precision = 9)
    @Basic
    var exchangeRate: BigDecimal? = null

    @Column(name = "TRANS_TYPE", nullable = true, length = 5)
    @Basic
    var transType: String? = null

    @Column(name = "INLAND_TRANS", nullable = true, length = 10)
    @Basic
    var inlandTrans: String? = null

    @Column(name = "PLACE_OF_LOADING", nullable = true, length = 20)
    @Basic
    var placeOfLoading: String? = null

    @Column(name = "TERMS_PAYMENT", nullable = true, length = 5)
    @Basic
    var termsPayment: String? = null

    @Column(name = "BANK_ACCOUNT", nullable = true, length = 40)
    @Basic
    var bankAccount: String? = null

    @Column(name = "BANK_BRANCH_ID", nullable = true, length = 40)
    @Basic
    var bankBranchId: String? = null

    @Column(name = "BOND_NUM", nullable = true, length = 40)
    @Basic
    var bondNum: String? = null

    @Column(name = "BOND_AMOUNT", nullable = true, length = 25)
    @Basic
    var bondAmount: String? = null

    @Column(name = "SUB_OFFICE_2", nullable = true, length = 10)
    @Basic
    var subOffice2: String? = null

    @Column(name = "SUB_OFFICE_3", nullable = true, length = 10)
    @Basic
    var subOffice3: String? = null

    @Column(name = "IDE_WH_TYPE", nullable = true, length = 5)
    @Basic
    var ideWhType: String? = null

    @Column(name = "IDE_EH", nullable = true, length = 15)
    @Basic
    var ideEh: String? = null

    @Column(name = "COUNTRY_TRANSIT", nullable = true, length = 20)
    @Basic
    var countryTransit: String? = null

    @Column(name = "CD_REGISTRATION_DATE", nullable = true)
    @Basic
    var cdRegistrationDate: Timestamp? = null

    @Column(name = "SEALS_NUM", nullable = true, length = 100)
    @Basic
    var sealsNum: String? = null

    @Column(name = "EXEMPTION_YEAR", nullable = true, length = 5)
    @Basic
    var exemptionYear: String? = null

    @Column(name = "INVOICE_DATE", nullable = true)
    @Basic
    var invoiceDate: Timestamp? = null

    @Column(name = "CD_PAY_AMOUNT", nullable = true, precision = 4)
    @Basic
    var cdPayAmount: BigDecimal? = null

    @Column(name = "CD_GUARANTEED_AMOUNT", nullable = true, precision = 4)
    @Basic
    var cdGuaranteedAmount: BigDecimal? = null

    @Column(name = "CONSIGNOR_REGISTERED", nullable = true, length = 2)
    @Basic
    var consignorRegistered: String? = null

    @Column(name = "CONSIGNOR_PIN_NUMBER", nullable = true, length = 20)
    @Basic
    var consignorPinNumber: String? = null

    @Column(name = "CONSIGNOR_COUNTRY_CODE", nullable = true, length = 10)
    @Basic
    var consignorCountryCode: String? = null

    @Column(name = "CONSIGNOR_BUSINESS_NAME", nullable = true, length = 200)
    @Basic
    var consignorBusinessName: String? = null

    @Column(name = "CONSIGNOR_AEO_FLAG", nullable = true, length = 20)
    @Basic
    var consignorAeoFlag: String? = null

    @Column(name = "CONSIGNOR_BUSINESS_ADDRESS", nullable = true, length = 255)
    @Basic
    var consignorBusinessAddress: String? = null

    @Column(name = "CONSIGNOR_BUSINESS_NATION", nullable = true, length = 10)
    @Basic
    var consignorBusinessNation: String? = null

    @Column(name = "CONSIGNEE_REGISTERED", nullable = true, length = 2)
    @Basic
    var consigneeRegistered: String? = null

    @Column(name = "CONSIGNEE_PIN_NUMBER", nullable = true, length = 20)
    @Basic
    var consigneePinNumber: String? = null

    @Column(name = "CONSIGNEE_COUNTRY_CODE", nullable = true, length = 10)
    @Basic
    var consigneeCountryCode: String? = null

    @Column(name = "CONSIGNEE_BUSINESS_NAME", nullable = true, length = 200)
    @Basic
    var consigneeBusinessName: String? = null

    @Column(name = "CONSIGNEE_AEO_FLAG", nullable = true, length = 20)
    @Basic
    var consigneeAeoFlag: String? = null

    @Column(name = "CONSIGNEE_BUSINESS_ADDRESS", nullable = true, length = 255)
    @Basic
    var consigneeBusinessAddress: String? = null

    @Column(name = "CONSIGNEE_BUSINESS_NATION", nullable = true, length = 10)
    @Basic
    var consigneeBusinessNation: String? = null

    @Column(name = "MANIFEST_NUMBER", nullable = true, length = 200)
    @Basic
    var manifestNumber: String? = null

    @Column(name = "PREFIX", nullable = true, length = 200)
    @Basic
    var prefix: String? = null

    @Column(name = "BILL_CODE", nullable = true, length = 200)
    @Basic
    var billCode: String? = null

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
        val that = other as DeclarationDetailsEntity
        return id == that.id && userId == that.userId && messageDate == that.messageDate && module == that.module && action == that.action && information == that.information && messageVersion == that.messageVersion && direction == that.direction && messageNature == that.messageNature && declarationRefNo == that.declarationRefNo && officeCode == that.officeCode && officeSubCode == that.officeSubCode && declarantRegime == that.declarantRegime && refNum == that.refNum && totalInsurance == that.totalInsurance && totalOtherCharges == that.totalOtherCharges && totalCustomsValue == that.totalCustomsValue && valuationMethod == that.valuationMethod && declarantPin == that.declarantPin && declarantAeoFlag == that.declarantAeoFlag && companyPin == that.companyPin && lastConsignmentCountry == that.lastConsignmentCountry && entryDate == that.entryDate && regionOfDestinationCode == that.regionOfDestinationCode && idenMeanTransDepArr == that.idenMeanTransDepArr && nationalityTransDepArr == that.nationalityTransDepArr && delivTermsSub1 == that.delivTermsSub1 && delivTermsSub2 == that.delivTermsSub2 && currencyCode == that.currencyCode && totalAmount == that.totalAmount && exchangeRate == that.exchangeRate && transType == that.transType && inlandTrans == that.inlandTrans && placeOfLoading == that.placeOfLoading && termsPayment == that.termsPayment && bankAccount == that.bankAccount && bankBranchId == that.bankBranchId && bondNum == that.bondNum && bondAmount == that.bondAmount && subOffice2 == that.subOffice2 && subOffice3 == that.subOffice3 && ideWhType == that.ideWhType && ideEh == that.ideEh && countryTransit == that.countryTransit && cdRegistrationDate == that.cdRegistrationDate && sealsNum == that.sealsNum && exemptionYear == that.exemptionYear && invoiceDate == that.invoiceDate && cdPayAmount == that.cdPayAmount && cdGuaranteedAmount == that.cdGuaranteedAmount && consignorRegistered == that.consignorRegistered && consignorPinNumber == that.consignorPinNumber && consignorCountryCode == that.consignorCountryCode && consignorBusinessName == that.consignorBusinessName && consignorAeoFlag == that.consignorAeoFlag && consignorBusinessAddress == that.consignorBusinessAddress && consignorBusinessNation == that.consignorBusinessNation && consigneeRegistered == that.consigneeRegistered && consigneePinNumber == that.consigneePinNumber && consigneeCountryCode == that.consigneeCountryCode && consigneeBusinessName == that.consigneeBusinessName && consigneeAeoFlag == that.consigneeAeoFlag && consigneeBusinessAddress == that.consigneeBusinessAddress && consigneeBusinessNation == that.consigneeBusinessNation && manifestNumber == that.manifestNumber && prefix == that.prefix && billCode == that.billCode && description == that.description && status == that.status && varField1 == that.varField1 && varField2 == that.varField2 && varField3 == that.varField3 && varField4 == that.varField4 && varField5 == that.varField5 && varField6 == that.varField6 && varField7 == that.varField7 && varField8 == that.varField8 && varField9 == that.varField9 && varField10 == that.varField10 && createdBy == that.createdBy && createdOn == that.createdOn && modifiedBy == that.modifiedBy && modifiedOn == that.modifiedOn && deleteBy == that.deleteBy && deletedOn == that.deletedOn
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
            declarationRefNo,
            officeCode,
            officeSubCode,
            declarantRegime,
            refNum,
            totalInsurance,
            totalOtherCharges,
            totalCustomsValue,
            valuationMethod,
            declarantPin,
            declarantAeoFlag,
            companyPin,
            lastConsignmentCountry,
            entryDate,
            regionOfDestinationCode,
            idenMeanTransDepArr,
            nationalityTransDepArr,
            delivTermsSub1,
            delivTermsSub2,
            currencyCode,
            totalAmount,
            exchangeRate,
            transType,
            inlandTrans,
            placeOfLoading,
            termsPayment,
            bankAccount,
            bankBranchId,
            bondNum,
            bondAmount,
            subOffice2,
            subOffice3,
            ideWhType,
            ideEh,
            countryTransit,
            cdRegistrationDate,
            sealsNum,
            exemptionYear,
            invoiceDate,
            cdPayAmount,
            cdGuaranteedAmount,
            consignorRegistered,
            consignorPinNumber,
            consignorCountryCode,
            consignorBusinessName,
            consignorAeoFlag,
            consignorBusinessAddress,
            consignorBusinessNation,
            consigneeRegistered,
            consigneePinNumber,
            consigneeCountryCode,
            consigneeBusinessName,
            consigneeAeoFlag,
            consigneeBusinessAddress,
            consigneeBusinessNation,
            manifestNumber,
            prefix,
            billCode,
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
