package org.kebs.app.kotlin.apollo.store.model.invoice

import java.io.Serializable
import java.math.BigDecimal
import java.sql.Timestamp
import javax.persistence.*


@Entity
@Table(name = "DAT_INV_CORPORATE_CUSTOMERS")
class CorporateCustomerAccounts : Serializable {
    @Column(name = "ID")
    @Id
    var id: Long = 0

    @Column(name = "CORPORATE_IDENTIFIER", unique = true)
    var corporateIdentifier: String? = null

    @Column(name = "CORPORATE_NAME", nullable = false)
    var corporateName: String? = null

    @Column(name = "CORPORATE_TYPE", nullable = false)
    var corporateType: String? = null // COURIER

    @Column(name = "CORPORATE_EMAIL", nullable = false)
    var corporateEmail: String? = null

    @Column(name = "CORPORATE_BILL_NUMBER", nullable = false)
    var corporateBillNumber: String? = null

    @Column(name = "LAST_PAYMENT", nullable = false)
    var lastPayment: Timestamp? = null

    @Column(name = "CONTACT_NAME", nullable = false)
    var contactName: String? = null

    @Column(name = "CONTACT_PHONE", nullable = false)
    var contactPhone: String? = null

    @Column(name = "CONTACT_EMAIL", nullable = false)
    var contactEmail: String? = null

    @Column(name = "CURRENT_BALANCE", nullable = false)
    var currentBalance: BigDecimal? = null

    @Column(name = "ACCOUNT_SUSPENDEND", nullable = false)
    var accountSuspendend: Int? = null

    @Column(name = "ACCOUNT_BLOCKED", nullable = false)
    var accountBlocked: Int? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null

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
}