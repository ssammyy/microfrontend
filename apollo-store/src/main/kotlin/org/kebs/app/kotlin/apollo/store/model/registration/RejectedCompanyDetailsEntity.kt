package org.kebs.app.kotlin.apollo.store.model.registration

import java.io.Serializable
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp
import java.util.*
//import java.time.LocalDate
//import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_REJECTED_COMPANY_DETAILS")
class RejectedCompanyDetailsEntity : Serializable {
    @Column(name = "ID")
    @Id
    @SequenceGenerator(
        name = "DAT_KEBS_REJECTED_COMPANY_DETAILS_SEQ_GEN",
        sequenceName = "DAT_KEBS_REJECTED_COMPANY_DETAILS_SEQ",
        allocationSize = 1
    )
    @GeneratedValue(generator = "DAT_KEBS_REJECTED_COMPANY_DETAILS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long? = null

    @Column(name = "COMPANY_ID")
    @Basic
    var companyId: Long? = null

    @Column(name = "COMPANY_NAME")
    @Basic
    var companyName: String? = null

    @Column(name = "EDIT_ID")
    @Basic
    var editID: Long? = null

    @Column(name = "PHYSICAL_ADDRESS_EDIT")
    @Basic
    var physicalAddressEdit: String? = null

    @Column(name = "PHYSICAL_ADDRESS")
    @Basic
    var physicalAddress: String? = null

    @Column(name = "POSTAL_ADDRESS_EDIT")
    @Basic
    var postalAddressEdit: String? = null

    @Column(name = "POSTAL_ADDRESS")
    @Basic
    var postalAddress: String? = null

    @Column(name = "OWNERSHIP_EDIT")
    @Basic
    var ownershipEdit: String? = null

    @Column(name = "OWNERSHIP")
    @Basic
    var ownership: String? = null

    @Column(name = "COMPANY_EMAIL_EDIT")
    @Basic
    var companyEmailEdit: String? = null

    @Column(name = "COMPANY_EMAIL")
    @Basic
    var companyEmail: String? = null

    @Column(name = "COMPANY_TELEPHONE_EDIT")
    @Basic
    var companyTelephoneEdit: String? = null

    @Column(name = "COMPANY_TELEPHONE")
    @Basic
    var companyTelephone: String? = null

    @Column(name = "ASSIGNED_TO")
    @Basic
    var assignedTo: Long? = null

    @Column(name = "STATUS")
    @Basic
    var status: Long? = null


}