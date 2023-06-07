package org.kebs.app.kotlin.apollo.store.model.std

import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "SD_SCHEME_MEMBERSHIP_REQUEST")
class SchemeMembershipRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "REQUEST_ID")
    @Basic
    val requestId: Long = 0

    @Column(name = "REQUESTER_NAME")
    @Basic
    val name: String? = null

    @Column(name = "DESIGNATION_OCCUPATION")
    @Basic
    val designationOccupation: String? = null

    @Column(name = "ADDRESS")
    @Basic
    val address: String? = null

    @Column(name = "EMAIL")
    @Basic
    val email: String? = null

    @Column(name = "PHONE")
    @Basic
    val phone: String? = null

    @Column(name = "REQUEST_DATE")
    @Basic
    val date: String? = null

    @Column(name = "ACCOUNT_TYPE")
    @Basic
    val accountType: String? = null

    @Column(name = "SIC_ASSIGNED_ID")
    @Basic
    var sicAssignedId: String? = null

    @Column(name = "VAR_FIELD_1")
    @Basic
    var varField1: String? = null

    @Column(name = "WEB_STORE_ACCOUNT_CREATION_DATE")
    @Basic
    var webStoreAccountCreationDate: Timestamp? = null

    @Column(name = "VAR_FIELD_3")
    @Basic
    var varField3: String? = null

    @Column(name = "VAR_FIELD_4")
    @Basic
    var varField4: String? = null

    @Column(name = "CREATED_AT")
    @Basic
    var createdOn: Timestamp? = null

    @Column(name = "SIC_ASSIGNED_DATE_ASSIGNED")
    @Basic
    var sicAssignedDateAssigned: Timestamp? = null

    @Column(name = "INVOICE_STATUS")
    @Basic
    var invoiceStatus: String? = null

    @Column(name = "INVOICE_NUMBER")
    @Basic
    var invoiceNumber: String? = null

    @Column(name = "INVOICE_AMOUNT")
    @Basic
    var invoiceAmount: String? = null

    @Column(name = "INVOICE_GENERATED_DATE")
    @Basic
    var invoiceGeneratedDate: Timestamp? = null

    @Column(name = "INVOICE_PAYMENT_DATE")
    @Basic
    var invoicePaymentDate: Timestamp? = null


}
