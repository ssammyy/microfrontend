package org.kebs.app.kotlin.apollo.store.model.std

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*

@Entity
@Table(name="SD_STANDARD_REVIEW_RECOMMENDATIONS")
class StandardReviewRecommendations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    var id:Long=0

    @Column(name="RECOMMENDATION")
    @Basic
    var recommendation: String? =null

    @Column(name="RECOMMENDATION_BY")
    @Basic
    var recommendationBy: String? =null

    @Column(name="DATE_OF_RECOMMENDATION")
    @Basic
    var dateOfRecommendation: String? =null

    @Transient
    @Column(name = "ACCENT_TO")
    @Basic
    val accentTo: Boolean = false
    @Column(name = "TASKID")
    @Basic
    var taskId: String? = null
}
