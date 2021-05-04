package com.apollo.standardsdevelopment.models

import javax.persistence.*

@Entity
@Table (name = "SD_ENQUIRY_TRACKER")
class EnqiryTracker {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ENQUIRY_TRACKER_ID")
    @Basic
    var enquiry_tracker_id : Long = 0

    @Column(name = "ENQUIRER_ID")
    @Basic
    val enquirer_id : Long? = null;
    @Column(name = "ENQUIRER_EMAIL")
    @Basic
    val enquirer_email : String? = null;
    @Column(name = "STATUS")
    @Basic
    val status : Long? = null;
    @Column(name = "ENQUIRY_TIMESTAMP")
    @Basic
    val enquiry_timestamp : String? = null;

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
}
