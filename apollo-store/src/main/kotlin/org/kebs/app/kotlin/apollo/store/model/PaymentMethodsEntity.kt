package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "CFG_PAYMENT_METHODS")
class PaymentMethodsEntity : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "CFG_PAYMENT_METHODS_SEQ_GEN", sequenceName = "CFG_PAYMENT_METHODS_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "CFG_PAYMENT_METHODS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long = 0

    @Column(name = "METHOD")
    @Basic
    var method: String? = null

    @Column(name = "PAY_BILL_NO")
    @Basic
    var payBillNo: String? = null

    @Column(name = "MPESA_ACC_NO")
    @Basic
    var mpesaAccNo: String? = null

    @Column(name = "VAT_NO")
    @Basic
    var vatNo: String? = null

    @Column(name = "PIN_NO")
    @Basic
    var pinNo: String? = null

    @Column(name = "BANK_ACCOUNT_KES_NUMBER")
    @Basic
    var bankAccountKesNumber: String? = null

    @Column(name = "BANK_ACCOUNT_USD_NUMBER")
    @Basic
    var bankAccountUsdNumber: String? = null

    @Column(name = "BANK_CODE")
    @Basic
    var bankCode: String? = null

    @Column(name = "BRANCH_CODE")
    @Basic
    var branchCode: String? = null

    @Column(name = "SWIFT_CODE")
    @Basic
    var swiftCode: String? = null

    @Column(name = "TILL_OR_ACCOUNT_NUMBER")
    @Basic
    var tillOrAccountNumber: String? = null

    @Column(name = "MPESA_MENU")
    @Basic
    var mpesaMenu: String? = null

    @Column(name = "LIPA_NA_MPESA")
    @Basic
    var lipaNaMpesa: String? = null

    @Column(name = "MPESA_BUY_GOODS")
    @Basic
    var mpesaBuyGoods: String? = null

    @Column(name = "MPESA_TILL_NUMBER")
    @Basic
    var mpesaTillNumber: String? = null

    @Column(name = "ENTER_AMOUNT")
    @Basic
    var enterAmount: String? = null

    @Column(name = "WAIT_FOR_MESSAGE")
    @Basic
    var waitForMessage: String? = null

    @Column(name = "CLICK_PAY_NOW")
    @Basic
    var clickPayNow: String? = null

    @Column(name = "BANK_NAME")
    @Basic
    var bankName: String? = null

    @Column(name = "BANK_ACCOUNT_NAME")
    @Basic
    var bankAccountName: String? = null

    @Column(name = "CURRENCY_CODE")
    @Basic
    var currencyCode: String? = null

    @Column(name = "BANK_ACCOUNT_NUMBER")
    @Basic
    var bankAccountNumber: String? = null

    @Column(name = "BANK_BRANCH")
    @Basic
    var bankBranch: String? = null

    @Column(name = "REFERENCE_NUMBER")
    @Basic
    var referenceNumber: String? = null

    @Column(name = "STATUS")
    @Basic
    var status: Long? = null

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as PaymentMethodsEntity
        return id == that.id &&
                payBillNo == that.payBillNo &&
                mpesaAccNo == that.mpesaAccNo &&
                vatNo == that.vatNo &&
                pinNo == that.pinNo &&
                bankAccountKesNumber == that.bankAccountKesNumber &&
                bankAccountUsdNumber == that.bankAccountUsdNumber &&
                bankCode == that.bankCode &&
                branchCode == that.branchCode &&
                swiftCode == that.swiftCode &&
                method == that.method &&
                tillOrAccountNumber == that.tillOrAccountNumber &&
                mpesaMenu == that.mpesaMenu &&
                lipaNaMpesa == that.lipaNaMpesa &&
                mpesaBuyGoods == that.mpesaBuyGoods &&
                mpesaTillNumber == that.mpesaTillNumber &&
                enterAmount == that.enterAmount &&
                waitForMessage == that.waitForMessage &&
                clickPayNow == that.clickPayNow &&
                bankName == that.bankName &&
                bankAccountName == that.bankAccountName &&
                bankAccountNumber == that.bankAccountNumber &&
                currencyCode == that.currencyCode &&
                bankBranch == that.bankBranch &&
                referenceNumber == that.referenceNumber &&
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
                lastModifiedBy == that.lastModifiedBy &&
                lastModifiedOn == that.lastModifiedOn &&
                updateBy == that.updateBy &&
                updatedOn == that.updatedOn &&
                deleteBy == that.deleteBy &&
                deletedOn == that.deletedOn &&
                version == that.version
    }

    override fun hashCode(): Int {
        return Objects.hash(id, method,
                payBillNo,
                mpesaAccNo,
                vatNo,
                pinNo,
                bankAccountKesNumber,
                bankAccountUsdNumber,
                bankCode,
                branchCode,
                swiftCode,
                currencyCode, tillOrAccountNumber, mpesaMenu, lipaNaMpesa, mpesaBuyGoods, mpesaTillNumber, enterAmount, waitForMessage, clickPayNow, bankName, bankAccountName, bankAccountNumber, bankBranch, referenceNumber, status, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, lastModifiedBy, lastModifiedOn, updateBy, updatedOn, deleteBy, deletedOn, version)
    }
}