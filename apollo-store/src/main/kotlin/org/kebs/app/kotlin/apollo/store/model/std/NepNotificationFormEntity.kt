package org.kebs.app.kotlin.apollo.store.model.std

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name="SD_NEP_NOTIFICATION_FORM")
class NepNotificationFormEntity  : Serializable {

    @Column(name = "ID")
    @SequenceGenerator(
        name = "SD_NEP_NOTIFICATION_FORM_SEQ_GEN",
        allocationSize = 1,
        sequenceName = "SD_NEP_NOTIFICATION_FORM_SEQ"
    )
    @GeneratedValue(generator = "SD_NEP_NOTIFICATION_FORM_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = 0

    @Column(name="DATE_PD_PREPARED")
    @Basic
    var datePrepared:Timestamp?=null

    @Column(name="NOTIFYING_MEMBER")
    @Basic
    var notifyingMember: String?=null

    @Column(name="AGENCY_RESPONSIBLE")
    @Basic
    var agencyResponsible:String?=null

    @Column(name="ADDRESS_OF_AGENCY")
    @Basic
    var addressOfAgency:String?=null

    //  @Transient
    @Column(name = "TELEPHONE_OF_AGENCY")
    @Basic
    var telephoneOfAgency: String? = null

    @Column(name = "FAX_OF_AGENCY")
    @Basic
    var faxOfAgency: String? = null

    @Column(name = "EMAIL_OF_AGENCY")
    @Basic
    var emailOfAgency: String? = null

    @Column(name = "WEBSITE_OF_AGENCY")
    @Basic
    var websiteOfAgency: String? = null

    @Column(name = "NOTIFIED_UNDER_ARTICLE")
    @Basic
    var notifiedUnderArticle: String? = null

    @Column(name = "PRODUCTS_COVERED")
    @Basic
    var productsCovered: String? = null

    @Column(name = "DESCRIPTION_OF_NOTIFIED_DOCUMENT")
    @Basic
    var descriptionOfNotifiedDoc: String? = null

    @Column(name = "OBJECTIVE_AND_RATIONALE")
    @Basic
    var objectiveAndRationale: String? = null

    @Column(name = "DESCRIPTION_OF_CONTENT")
    @Basic
    var descriptionOfContent: String? = null

    @Column(name = "RELEVANT_DOCUMENTS")
    @Basic
    var relevantDocuments: String? = null

    @Column(name="PROPOSED_DATE_OF_ADOPTION")
    @Basic
    var proposedDateOfAdoption:Timestamp?=null

    @Column(name = "PROPOSED_DATE_OF_ENTRY_INTO_FORCE")
    @Basic
    var proposedDateOfEntryIntoForce: Timestamp? = null

    @Column(name = "FINAL_DATE_FOR_COMMENTS")
    @Basic
    var finalDateForComments: Timestamp? = null

    @Column(name = "TEXT_AVAILABLE_FROM")
    @Basic
    var textAvailableFrom: String? = null

    @Column(name = "PREPARED_BY")
    @Basic
    var preparedBy: String? = null

    @Column(name = "STATUS")
    @Basic
    var status: Long? = 0

    @Column(name = "DOCUMENT_ATTACHED")
    @Basic
    var documentAttached: Long? = 0

    @Column(name = "P_ID")
    @Basic
    var pid: Long? = 0

    @Column(name = "CD_ID")
    @Basic
    var cd_Id: Long? = 0

    @Column(name = "PRD_NAME")
    @Basic
    var prd_name: String? = null

    @Column(name = "KS_NUMBER")
    @Basic
    var ks_NUMBER: String? = null

    @Column(name = "ORGANIZATION")
    @Basic
    var organization: String? = null

    @Column(name = "PRD_BY")
    @Basic
    var prd_by: String? = null

    @Column(name = "PRD_STATUS")
    @Basic
    var prdStatus: String? = null

    @Column(name = "PRD_CREATED_ON")
    @Basic
    var created_on: String? = null

    @Column(name = "NO_OF_COMMENTS")
    @Basic
    var number_OF_COMMENTS: String? = null

    @Column(name = "VAR_FIELD_ONE")
    @Basic
    var var_FIELD_1: String? = null

}