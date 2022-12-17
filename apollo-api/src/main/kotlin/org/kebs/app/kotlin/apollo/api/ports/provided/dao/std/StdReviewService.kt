package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std

import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.StandardsLevyBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.dto.std.ProcessInstanceRecommendations
import org.kebs.app.kotlin.apollo.store.model.std.ReviewStandards
import org.kebs.app.kotlin.apollo.store.model.std.StandardReview
import org.kebs.app.kotlin.apollo.store.repo.std.*
import org.springframework.beans.factory.annotation.Autowired

class StdReviewService(
    @Autowired
    private val standardRepository: StandardRepository,
    private val standardReviewRepository: StandardReviewRepository,
    private val standardReviewCommentsRepository: StandardReviewCommentsRepository,
    private val standardReviewRecommendationsRepository: StandardReviewRecommendationsRepository,
    private val companyStandardRepository: CompanyStandardRepository,
    private val notifications: Notifications,
    private val standardReviewProposalCommentsRepository: StandardReviewProposalCommentsRepository,
    private val standardReviewProposalRecommendationsRepo: StandardReviewProposalRecommendationsRepo,
    private val commonDaoServices: CommonDaoServices,
    private val bpmnService: StandardsLevyBpmn,
    private val reviewStandardRemarksRepository : ReviewStandardRemarksRepository,
    private val iSAdoptionJustificationRepository: ISAdoptionJustificationRepository,
    private val departmentListRepository: DepartmentListRepository,
) {
    fun getStandardsForReview(): MutableList<ReviewStandards> {
        return standardRepository.getStandardsForReview()
    }

    fun standardReviewForm(standardReview: StandardReview) : StandardReview
    {

        val loggedInUser = commonDaoServices.loggedInUserDetails()
        standardReview.title=standardReview.title
        standardReview.standardNumber=standardReview.standardNumber
        standardReview.documentType=standardReview.documentType
        standardReview.preparedBy=standardReview.preparedBy
        standardReview.preparedBy = loggedInUser.id
        standardReview.datePrepared=standardReview.datePrepared
        standardReview.scope=standardReview.scope
        standardReview.normativeReference=standardReview.normativeReference
        standardReview.symbolsAbbreviatedTerms=standardReview.symbolsAbbreviatedTerms
        standardReview.clause=standardReview.clause
        standardReview.special=standardReview.special
        standardReview.standardType=standardReview.standardType

        val ispDetails = standardReviewRepository.save(standardReview)

        var userList= companyStandardRepository.getTcSecEmailList()

        //email to stakeholders
        val targetUrl = "https://kimsint.kebs.org/";
        userList.forEach { item->
            //val recipient="stephenmuganda@gmail.com"
            val recipient= item.getUserEmail()
            val subject = "New Adoption Proposal Review"+  standardReview.standardNumber
            val messageBody= "Dear ${item.getFirstName()} ${item.getLastName()},An adoption document has been uploaded Kindly login to the system to comment on it.Click on the Link below to view. ${targetUrl} "
            if (recipient != null) {
                notifications.sendEmail(recipient, subject, messageBody)
            }
        }

        return ispDetails

    }
}