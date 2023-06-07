package org.kebs.app.kotlin.apollo.store.model.std

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*

@Entity
@Table(name="SD_STANDARD_REVIEW_COMMENTS")
class StandardReviewComments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    var id:Long=0

    @Column(name="COMMENTS")
    @Basic
    var comments: String? =null

    @Column(name="COMMENT_BY")
    @Basic
    var commentBy: String? =null

    @Column(name="DATE_OF_COMMENT")
    @Basic
    var dateOfComment: String? =null

    @Transient
    @Column(name = "TASKID")
    @Basic
    var taskId: String? = null

}
