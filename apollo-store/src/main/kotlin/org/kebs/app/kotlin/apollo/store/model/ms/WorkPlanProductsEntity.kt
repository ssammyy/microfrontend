package org.kebs.app.kotlin.apollo.store.model.ms


import java.io.Serializable
import java.sql.Date
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_MS_WORKPLAN_PRODUCTS")
class WorkPlanProductsEntity : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_MS_WORKPLAN_PRODUCTS_SEQ_GEN", allocationSize = 1, sequenceName = "DAT_KEBS_MS_WORKPLAN_PRODUCTS_SEQ")
    @GeneratedValue(generator = "DAT_KEBS_MS_WORKPLAN_PRODUCTS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = null

    @Basic
    @Column(name = "PRODUCT_NAME")
    var productName: String? = null

    @Basic
    @Column(name = "REFERENCE_NO")
    var referenceNo: String? = null

    @Basic
    @Column(name = "RECOMMENDATION")
    var recommendation: String? = null

    @Basic
    @Column(name = "DESTRUCTION_RECOMMENDED")
    var destructionRecommended: Int? = null

    @Basic
    @Column(name = "HOD_RECOMMENDATION_STATUS")
    var hodRecommendationStatus: Int? = null

    @Basic
    @Column(name = "HOD_RECOMMENDATION_REMARKS")
    var hodRecommendationRemarks: String? = null

    @Basic
    @Column(name = "DIRECTOR_RECOMMENDATION_STATUS")
    var directorRecommendationStatus: Int? = null

    @Basic
    @Column(name = "DIRECTOR_RECOMMENDATION_REMARKS")
    var directorRecommendationRemarks: String? = null

    @Basic
    @Column(name = "CLIENT_APPEALED")
    var clientAppealed: Int? = null

    @Basic
    @Column(name = "DESTRUCTION_STATUS")
    var destructionStatus: Int? = null

    @Basic
    @Column(name = "APPEAL_STATUS")
    var appealStatus: Int? = null

    @Basic
    @Column(name = "DESTRUCTION_NOTIFICATION_STATUS")
    var destructionNotificationStatus: Int? = null

    @Basic
    @Column(name = "DESTRUCTION_NOTIFICATION_DOC_ID")
    var destructionNotificationDocId: Long? = null

    @Basic
    @Column(name = "WORK_PLAN_ID")
    var workPlanId: Long? = null

    @Basic
    @Column(name = "SSF_ID")
    var ssfId: Long? = null

    @Basic
    @Column(name = "DESTRUCTION_CLIENT_EMAIL")
    var destructionClientEmail: String? = null

    @Basic
    @Column(name = "DESTRUCTION_CLIENT_FULL_NAME")
    var destructionClientFullName: String? = null

    @Basic
    @Column(name = "DESTRUCTION_NOTIFICATION_DATE")
    var destructionNotificationDate: Date? = null

    @Basic
    @Column(name = "DESTRUCTION_DOC_ID")
    var destructionDocId: Long? = null

    @Basic
    @Column(name = "DESTRUCTED_STATUS")
    var destructedStatus: Int? = null

    @Basic
    @Column(name = "STATUS")
    var status: Int? = null

    @Basic
    @Column(name = "DESCRIPTION")
    var description: String? = null

    @Basic
    @Column(name = "VAR_FIELD_1")
    var varField1: String? = null

    @Basic
    @Column(name = "VAR_FIELD_2")
    var varField2: String? = null

    @Basic
    @Column(name = "VAR_FIELD_3")
    var varField3: String? = null

    @Basic
    @Column(name = "VAR_FIELD_4")
    var varField4: String? = null

    @Basic
    @Column(name = "VAR_FIELD_5")
    var varField5: String? = null

    @Basic
    @Column(name = "VAR_FIELD_6")
    var varField6: String? = null

    @Basic
    @Column(name = "VAR_FIELD_7")
    var varField7: String? = null

    @Basic
    @Column(name = "VAR_FIELD_8")
    var varField8: String? = null

    @Basic
    @Column(name = "VAR_FIELD_9")
    var varField9: String? = null

    @Basic
    @Column(name = "VAR_FIELD_10")
    var varField10: String? = null

    @Basic
    @Column(name = "CREATED_BY")
    var createdBy: String? = null

    @Basic
    @Column(name = "CREATED_ON")
    var createdOn: Timestamp? = null

    @Basic
    @Column(name = "MODIFIED_BY")
    var modifiedBy: String? = null

    @Basic
    @Column(name = "MODIFIED_ON")
    var modifiedOn: Timestamp? = null

    @Basic
    @Column(name = "DELETE_BY")
    var deleteBy: String? = null

    @Basic
    @Column(name = "DELETED_ON")
    var deletedOn: Timestamp? = null
    
}
