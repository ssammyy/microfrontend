package org.kebs.app.kotlin.apollo.store.model.std

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "SD_JUSTIFICATION")
class StandardJustification {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @Column(name = "SPC_MEETING_DATE")
    @Basic
    var spcMeetingDate: String? = null

    @Transient
    @JsonProperty("taskId")
    var taskId: String? = null

    @Column(name = "DEPARTMENT_ID")
    @Basic
    var departmentId: String? = null

    @Column(name = "TC_SECRETARY")
    @Basic
    var tcSecretary: String? = null

    @Column(name = "SL")
    @Basic
    var sl: String? = null

    @Column(name = "TITLE")
    @Basic
    var title: String? = null

    @Column(name = "EDITION")
    @Basic
    var edition: String? = null

    @Column(name = "REQUESTED_BY")
    @Basic
    var requestedBy: String? = null

    @Column(name = "ISSUES_ADDRESSED_BY")
    @Basic
    var issuesAddressedBy: String? = null

    @Column(name = "TC_ACCEPTANCE_DATE")
    @Basic
    var tcAcceptanceDate: String? = null

    @Column(name = "REQUEST_NO")
    @Basic
    var requestNo: String? = null

    @Column(name = "KS_ISO_NUMBER")
    @Basic
    var ksIsoNumber: String? = null

    @Column(name = "PURPOSE")
    @Basic
    var purpose: String? = null

    @Column(name = "SCOPE")
    @Basic
    var scope: String? = null

    @Column(name = "INTENDED_USERS")
    @Basic
    var intendedUsers: String? = null

    @Column(name = "TC_ID")
    @Basic
    var tcId: String? = null

    @Column(name = "CD_NUMBER")
    @Basic
    var cdNumber: String? = null

    @Column(name = "CREATED_ON")
    @Basic
    var createdOn: Timestamp? = null

    @Column(name = "MODIFIED_ON")
    @Basic
    var modifiedOn: Timestamp? = null

    @Column(name = "DELETED_ON")
    @Basic
    var deletedOn: Timestamp? = null

    @Column(name = "NWI_ID")
    @Basic
    var nwiId: String? = null


    @Column(name = "CREATED_BY")
    @Basic
    var createdBy: String? = null

    @Column(name = "STATUS")
    @Basic
    var status: String? = null


}
