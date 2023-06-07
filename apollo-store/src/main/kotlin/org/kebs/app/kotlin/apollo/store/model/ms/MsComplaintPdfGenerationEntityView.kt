package org.kebs.app.kotlin.apollo.store.model.ms

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "MS_COMPLAINT_PDF_GENERATION", schema = "APOLLO")
class MsComplaintPdfGenerationEntityView : Serializable {

    @Id
    @Column(name = "REFERENCE_NUMBER")
    var referenceNumber: String? = null

    @Basic
    @Column(name = "FIRST_NAME")
    var firstName: String? = null

    @Basic
    @Column(name = "PHYSICAL_ADDRESS_CUSTOMER")
    var physicalAddressCustomer: String? = null

    @Basic
    @Column(name = "LAST_NAME")
    var lastName: String? = null

    @Basic
    @Column(name = "MOBILE_PHONE_NUMBER")
    var mobilePhoneNumber: String? = null

    @Basic
    @Column(name = "ID_NUMBER")
    var idNumber: String? = null

    @Basic
    @Column(name = "POSTAL_ADDRESS")
    var postalAddress: String? = null

    @Basic
    @Column(name = "PHYSICAL_ADDRESS")
    var physicalAddress: String? = null

    @Basic
    @Column(name = "EMAIL_ADDRESS")
    var emailAddress: String? = null

    @Basic
    @Column(name = "BUILDING")
    var building: String? = null

    @Basic
    @Column(name = "BUSINESS_ADDRESS")
    var businessAddress: String? = null

    @Basic
    @Column(name = "TELEPHONE_NUMBER")
    var telephoneNumber: String? = null

    @Basic
    @Column(name = "PHONE_NUMBER")
    var phoneNumber: String? = null

    @Basic
    @Column(name = "EMAIL")
    var email: String? = null

    @Basic
    @Column(name = "NAME_CONTACT_PERSON")
    var nameContactPerson: String? = null

    @Basic
    @Column(name = "COMPLAINT_DETAILS")
    var complaintDetails: String? = null

    @Basic
    @Column(name = "COMPLAINT_SAMPLE_DETAILS")
    var complaintSampleDetails: String? = null

    @Basic
    @Column(name = "REMEDY_SOUGHT")
    var remedySought: String? = null

    @Basic
    @Column(name = "CREATED_ON")
    var createdOn: String? = null

    @Basic
    @Column(name = "CREATED_BY")
    var createdBy: String? = null
}
