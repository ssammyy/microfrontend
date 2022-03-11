package org.kebs.app.kotlin.apollo.store.model.std;

import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name="SD_SAC_SUMMARY")
 class SACSummary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    var id: Long =0

    @Column(name="SL")
    @Basic
    var sl:String?=null

    @Column(name="KS")
    @Basic
    var ks:String?=null

    @Column(name="REQUESTED_BY")
    @Basic
    var requestedBy:String?=null

    @Column(name="ISSUES_ADDRESSED")
    @Basic
    var issuesAddressed:String?=null

    @Column(name="BACKGROUND_INFORMATION")
    @Basic
    var backgroundInformation:String?=null

   @Column(name="APPROVAL_STATUS")
   @Basic
   var approvalStatus:String?=null

   @Column(name="FEEDBACK")
   @Basic
   var feedback:String?=null

   @Column(name = "DATE_OF_APPROVAL")
   @Basic
   var dateOfApproval:Timestamp? = null

   @Column(name="EAC_GAZETTE")
   @Basic
   var eacGazette:String?=null

   @Column(name="AUTHENTIC_TEXT")
   @Basic
   var authenticText:String?=null

   @Column(name = "VAR_FIELD_1")
   @Basic
   var varField1: String? = null

   @Column(name = "VAR_FIELD_2")
   @Basic
   var varField2: String? = null

   @Column(name = "DEPARTMENT")
   @Basic
   var department: String? = null

   @Column(name = "TECHNICAL_COMMITTEE")
   @Basic
   var technicalCommittee: String? = null
   @Column(name = "TITLE")
   @Basic
   var title: String? = null

   @Column(name = "REFERENCE_MATERIAL")
   @Basic
   var referenceMaterial: String? = null

   @Column(name = "EDITION")
   @Basic
   var edition: String? = null

   @Column(name = "VAR_FIELD_8")
   @Basic
   var varField8: String? = null

   @Column(name = "VAR_FIELD_9")
   @Basic
   var varField9: String? = null

   @Column(name = "VAR_FIELD_10")
   @Basic
   var varField10: String? = null

   override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other == null || javaClass != other.javaClass) return false
      val that = other as SACSummary
      return id == that.id &&
              sl == that.sl &&
              ks == that.ks &&
              requestedBy == that.requestedBy &&

              issuesAddressed == that.issuesAddressed &&
              backgroundInformation == that.backgroundInformation &&
              approvalStatus == that.approvalStatus &&
              feedback == that.feedback &&
              dateOfApproval == that.dateOfApproval &&
              eacGazette == that.eacGazette &&
              authenticText == that.authenticText &&
              technicalCommittee == that.technicalCommittee &&
              title == that.title &&
              referenceMaterial == that.referenceMaterial &&
              edition == that.edition &&
              varField8 == that.varField8 &&
              varField9 == that.varField9 &&
              varField10 == that.varField10 &&

              varField1 == that.varField1 &&
              varField2 == that.varField2 &&
              department == that.department


   }

   override fun hashCode(): Int {
      return Objects.hash(
         id,
         sl,
         ks,
         requestedBy,
         issuesAddressed,
         backgroundInformation,
         approvalStatus,
         technicalCommittee,
         title,
         referenceMaterial,
         edition,
         varField8,
         varField9,
         varField10,
         feedback,
         dateOfApproval,
         varField1,
         varField2,
         department,

      )
   }

}
