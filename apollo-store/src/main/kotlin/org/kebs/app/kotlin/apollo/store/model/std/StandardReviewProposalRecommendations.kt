package org.kebs.app.kotlin.apollo.store.model.std

import java.sql.Timestamp
import javax.persistence.*


@Entity
@Table(name="SD_REVIEW_PROPOSAL_RECOMMENDATIONS")
class StandardReviewProposalRecommendations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    var id:Long=0

    @Column(name="PROPOSAL_ID")
    @Basic
    var proposalId:String? =null

    @Column(name="RECOMMENDATION_TIME")
    @Basic
    var recommendationTime:Timestamp? =null

    @Column(name="SUMMARY_OF_RECOMMENDATIONS")
    @Basic
    var summaryOfRecommendations:String? =null

    @Column(name="USER_ID")
    @Basic
    var userID:Long? =null

    @Column(name="ASSIGNED_TO")
    @Basic
    var assignedTo:Long? =null

    @Column(name="FEEDBACK")
    @Basic
    var feedback:Long? =null

    @Column(name = "PROCESS_ID")
    @Basic
    var processId: String? = null

    @Column(name = "TASK_ID")
    @Basic
    var taskId: String? = null






}
