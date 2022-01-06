package org.kebs.app.kotlin.apollo.api.payload.response

import org.kebs.app.kotlin.apollo.store.model.invoice.CorporateCustomerAccounts
import java.math.BigDecimal
import java.sql.Timestamp
import javax.persistence.Basic
import javax.persistence.Column
import javax.persistence.Id

class FinanceInvoiceDao {
}

class BillingLimitsDao {
    var limitId: Long = 0
    var corporateType: String? = null // COURIER
    var billType: String? = null
    var billReceiptPrefix: String? = null
    var billPaymentDay: Int? = null
    var penaltyAmount: BigDecimal? = null
    var penaltyType: String? = null
    var maxBillAmount: BigDecimal? = null

}

class CorporateCustomerAccountDao {
    var corporatId: Long = 0
    var corporateIdentifier: String? = null
    var corporateName: String? = null
    var corporateType: String? = null // COURIER
    var corporateEmail: String? = null
    var corporatePhone: String? =null
    var corporateBillNumber: String? = null
    var lastPayment: Timestamp? = null
    var contactName: String? = null
    var contactPhone: String? = null
    var contactEmail: String? = null
    var currentBalance: BigDecimal? = null
    var isCiakMember: String? = null
    var accountSuspended: String? = null
    var paymentDays: Int? = 0
    var accountStatus: String? = null
    var status: Int? = null

    companion object {
        fun fromEntity(account: CorporateCustomerAccounts): CorporateCustomerAccountDao {
            val dao = CorporateCustomerAccountDao()
            dao.apply {
                corporatId = account.id
                corporateIdentifier = account.corporateIdentifier
                corporateName = account.corporateName
                corporateType = account.corporateType
                corporateEmail = account.corporateEmail
                corporatePhone=account.corporatePhone
                corporateBillNumber = account.corporateBillNumber
                lastPayment = account.lastPayment
                contactName = account.contactName
                contactPhone = account.contactPhone
                contactEmail = account.contactEmail
                currentBalance = account.currentBalance
                paymentDays = account.paymentDays
            }
            dao.isCiakMember = when (account.isCiakMember) {
                true -> "YES"
                else -> "NO"
            }
            dao.accountStatus = when (account.accountBlocked) {
                0 -> "ACTIVE"
                1 -> "BLOCKED"
                else -> "UNKNOWN"
            }
            dao.accountSuspended = when (account.accountSuspendend) {
                0 -> "ACTIVE"
                1 -> "SUSPENDED"
                else -> "UNKNOWN"
            }
            return dao
        }

        fun fromList(accounts: List<CorporateCustomerAccounts>): List<CorporateCustomerAccountDao> {
            val daos = mutableListOf<CorporateCustomerAccountDao>()
            accounts.forEach { daos.add(fromEntity(it)) }
            return daos
        }
    }
}