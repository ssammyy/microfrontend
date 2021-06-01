package org.kebs.app.kotlin.apollo.standardsdevelopment.models

import javax.persistence.*
import kotlin.jvm.Transient

@Entity
@Table(name="SD_ADOPTION_PROPOSAL")
class ISAdoptionProposal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    val id:Long=0

    @Column(name="DOC_NAME")
    @Basic
    val proposal_doc_name:String? =null

    @Transient
    var taskId:String?=null

}
