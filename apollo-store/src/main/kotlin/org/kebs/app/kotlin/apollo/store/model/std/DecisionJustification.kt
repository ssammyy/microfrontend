package org.kebs.app.kotlin.apollo.store.model.std

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*

@Entity
@Table(name = "SD_JUSTIFICATION_DECISION")
class DecisionJustification {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long =0

    @Column(name = "REASON")
    @Basic
    var reason: String? = null

    @Column(name = "DECISION")
    @Basic
    var decision: String? = null

    @Column(name = "REFERENCE_NO")
    @Basic
    var referenceNo: String? = null

    @Transient
    @JsonProperty("taskId")
    var taskId:String?=null
}
