package org.kebs.app.kotlin.apollo.store.model.std

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "SD_NWA_DISDT_JUSTIFICATION")
class DISDTJustification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id: Long = 0

    @Transient
    @JsonProperty("taskId")
    var taskId: String? = null

    @Column(name = "COST")
    @Basic
    var cost: String? = null

    @Column(name = "NUMBER_OF_MEETINGS")
    @Basic
    var numberOfMeetings: String? = null

    @Column(name = "IDENTIFIED_NEED")
    @Basic
    var identifiedNeed: String? = null

    @Column(name = "DATE_OF_APPROVAL")
    @Basic
    var dateOfApproval: Timestamp? = null

    @Column(name="DATE_PREPARED")
    @Basic
    var datePrepared: Timestamp?=null

    @Column(name = "CD_APP_NUMBER")
    @Basic
    var cdAppNumber: String? = null

    @Transient
    val accentTo: Boolean = false

}
