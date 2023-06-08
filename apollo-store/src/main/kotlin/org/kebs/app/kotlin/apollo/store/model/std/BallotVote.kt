package org.kebs.app.kotlin.apollo.store.model.std

import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "BALLOT_VOTE")
class BallotVote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id: Long = 0

    @Column(name = "BALLOT_ID")
    @Basic
    var ballotId: Long = 0

    @Column(name = "USER_ID")
    @Basic
    var userId: Long = 0

    @Column(name = "APPROVAL_STATUS")
    @Basic
    var approvalStatus: Long = 0


    @Column(name = "COMMENTS")
    @Basic
    var comment: String? = null

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
