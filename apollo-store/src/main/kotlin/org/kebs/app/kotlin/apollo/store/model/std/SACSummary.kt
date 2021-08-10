package org.kebs.app.kotlin.apollo.store.model.std;

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
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

}
