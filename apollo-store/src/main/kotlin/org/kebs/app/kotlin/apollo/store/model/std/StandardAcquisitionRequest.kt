package org.kebs.app.kotlin.apollo.store.model.std

import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "SD_STANDARD_ACQUISITION_REQUEST")
class StandardAcquisitionRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "REQUEST_ID")
    @Basic
    var requestId: Long = 0

    @Column(name = "REQUESTER_NAME")
    @Basic
    var name: String? = null

    @Column(name = "DESIGNATION_OCCUPATION")
    @Basic
    var designationOccupation: String? = null

    @Column(name = "ADDRESS")
    @Basic
    var address: String? = null

    @Column(name = "EMAIL")
    @Basic
    var email: String? = null

    @Column(name = "PHONE")
    @Basic
    var phone: String? = null

    @Column(name = "REQUEST_DATE")
    @Basic
    var date: Timestamp? = null

    @Column(name = "COMPANY_NAME")
    @Basic
    var accountType: String? = null


    @Column(name = "EMPLOYER_NAME")
    @Basic
    var employerName: String? = null

    @Column(name = "EMAIL_OF_EMPLOYER")
    @Basic
    var emailOfEmployer: String? = null


    @Column(name = "EMPLOYER_COMPANY_NAME")
    @Basic
    var employerCompanyName: String? = null


    @Column(name = "EMPLOYER_OCCUPATION")
    @Basic
    var employerOccupation: String? = null


    @Column(name = "EMPLOYER_ADDRESS")
    @Basic
    var employerAddress: String? = null

    @Column(name = "EMPLOYER_TELEPHONE_NO")
    @Basic
    var employerTelephoneNo: String? = null


    @Column(name = "EMPLOYER_APPROVAL_DATE")
    @Basic
    var employerApprovalDate: Timestamp? = null


    @Column(name = "SIC_ASSIGNED_ID")
    @Basic
    var sicAssignedId: String? = null

    @Column(name = "SIC_ASSIGNED_DATE_ASSIGNED")
    @Basic
    var sicAssignedDateAssigned: Timestamp? = null

    @Column(name = "APPROVED_BY_EMPLOYER")
    @Basic
    var approvedByEmployer: String? = null


    @Column(name = "STANDARD_NAME_REQUESTED")
    @Basic
    var standardNameRequested: String? = null

    @Column(name = "ENCRYPTED_ID")
    @Basic
    var encryptedId: String? = null

    @Column(name = "EMAIL_SENT_TO_EMPLOYER")
    @Basic
    var emailSentToEmployer: String? = null


    @Column(name = "STANDARD_EXISTS")
    @Basic
    var standardExists: String? = null

    @Column(name = "INVOICE_SENT")
    @Basic
    var invoiceSent: String? = null

    @Column(name = "PAYMENT_RECEIVED")
    @Basic
    var paymentReceived: String? = null

    @Column(name = "STANDARD_SENT_TO_CUSTOMER")
    @Basic
    var standardSentToCustomer: String? = null


    @Column(name = "STANDARD_SENT_TO_CUSTOMER_DATE")
    @Basic
    var standardSentToCustomerDate: Timestamp? = null

    @Column(name = "SIMILAR_STANDARDS")
    @Basic
    var similarStandards: String? = null

    @Column(name = "SIMILAR_STANDARDS_SENT_TO_CUSTOMER_DATE")
    @Basic
    var similarStandardsSentToCustomerDate: Timestamp? = null

    @Column(name = "PROFORMA_INVOICE_SENT")
    @Basic
    var proformaInvoiceSent: String? = null


    @Column(name = "PROFORMA_PAYMENT_RECEIVED")
    @Basic
    var proformaPaymentReceived: String? = null


    @Column(name = "REQUIRES_PUBLISHING")
    @Basic
    var requiresPublishing: String? = null


    @Column(name = "PAYMENT_TARIFF")
    @Basic
    var paymentTariff: String? = null

    @Column(name = "PAYMENT_AMOUNT")
    @Basic
    var paymentAmount: String? = null

    @Column(name = "SIMILAR_STANDARD_SENT_TO_CUSTOMER")
    @Basic
    var similarStandardSentToCustomer: String? = null

    @Column(name = "SIMILAR_STANDARD_SENT_TO_CUSTOMER_DATE")
    @Basic
    var similarStandardSentToCustomerDate: Timestamp? = null



}
