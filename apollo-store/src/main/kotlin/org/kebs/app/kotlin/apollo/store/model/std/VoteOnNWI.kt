package org.kebs.app.kotlin.apollo.store.model.std

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "SD_VOTE_ON_NWI")
class VoteOnNWI {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id: Long =0

    @Column(name = "USER_ID")
    @Basic
    var userId: Long= 1

    @Column(name = "VOTED_ON_BEHALF_ON_BY")
    @Basic
    var votedOnBehalfBy: Long? = 0


    @Column(name = "VOTED_ON_BEHALF_ON_STATUS")
    @Basic
    var votedOnBehalfStatus: Long? = 0


    @Column(name = "NWI_ID")
    @Basic
    var nwiId: Long= 1

    @Transient
    @JsonProperty("taskId")
    var taskId:String?=null

    @Column(name = "DECISION")
    @Basic
    var decision: String?= null

    @Column(name = "REASON")
    @Basic
    var reason: String?= null

    @Column(name = "POSITION")
    @Basic
    var position: String?= null

    @Column(name = "ORGANIZATION")
    @Basic
    var organization: String?= null

    @Column(name = "STATUS")
    @Basic
    var status: Long? = 0


    @Column(name = "CREATED_ON")
    @Basic
    var createdOn: Timestamp? = null

    @Column(name = "MODIFIED_ON")
    @Basic
    var modifiedOn: Timestamp? = null

    @Column(name = "DELETED_ON")
    @Basic
    var deletedOn: Timestamp? = null
}

