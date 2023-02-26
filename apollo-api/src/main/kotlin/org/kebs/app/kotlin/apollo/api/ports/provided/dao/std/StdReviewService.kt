package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std

import com.google.gson.Gson
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.StandardsLevyBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.dto.std.*
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.std.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.util.*
import kotlin.collections.HashMap

@Service
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
    private val sdReviewCommentsRepository: SDReviewCommentsRepository,
    private val standardRequestRepository: StandardRequestRepository,
    private val comStdDraftRepository: ComStdDraftRepository,
    private val companyStandardRemarksRepository: CompanyStandardRemarksRepository,
) {
    fun getStandardsForReview(): MutableList<ReviewStandards> {
        return standardRepository.getStandardsForReview()
    }

    fun standardReviewForm(standardReviewDto: StandardReviewDto) : StandardReview
    {
                val gson = Gson()
        KotlinLogging.logger { }.info { "WORKSHOP DRAFT DECISION" + gson.toJson(standardReviewDto) }

        val reviewBody= StandardReview();
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val dateOfCirculation=standardReviewDto.circulationDate
        var circulationDate: Timestamp? =null
       // var closingDate: Timestamp? =null
        if(dateOfCirculation==null){
            circulationDate=commonDaoServices.getTimestamp()

        }else{
            circulationDate=standardReviewDto.circulationDate
        }
        val closingDate: Timestamp = Timestamp.valueOf(circulationDate?.toLocalDateTime()?.plusDays(30) )
        reviewBody.standardId=standardReviewDto.id
        reviewBody.title=standardReviewDto.title
        reviewBody.standardNumber=standardReviewDto.standardNumber
        reviewBody.standardType=standardReviewDto.standardType
        reviewBody.documentType=standardReviewDto.documentType
        reviewBody.circulationDate=circulationDate
        reviewBody.closingDate=closingDate
        reviewBody.edition=standardReviewDto.edition
        reviewBody.preparedBy=loggedInUser.id
        reviewBody.tcSecretary=loggedInUser.firstName + loggedInUser.lastName

        val ispDetails = standardReviewRepository.save(reviewBody)

        var userList= companyStandardRepository.getICTList()
        val reviewId=ispDetails.id
        standardRepository.findByIdOrNull(standardReviewDto.id)?.let { std ->
            with(std) {
                status = 3

            }
            standardRepository.save(std)
        }?: throw Exception("STANDARD NOT FOUND")

        //email to stakeholders
        val targetUrl = "https://kimsint.kebs.org/systemicReview/$reviewId";
        userList.forEach { item->
            //val recipient="stephenmuganda@gmail.com"
            val recipient= item.getUserEmail()
            val subject = "Systemic Review for Kenya Standard of title"+  standardReviewDto.title
            val messageBody= "Dear ${item.getFirstName()} ${item.getLastName()},An adoption document for Systemic review has been uploaded Kindly Click on the Link below to view. ${targetUrl} "
            if (recipient != null) {
                notifications.sendEmail(recipient, subject, messageBody)
            }
        }

        return ispDetails

    }

    fun getProposal(): MutableList<StandardReview>{
        return standardReviewRepository.getReviewProposalForComment();
    }

    fun getProposals(reviewId: Long): MutableList<StandardReview> {
        return standardReviewRepository.getReviewProposalToComment(reviewId)
    }

    //Submit Adoption Proposal comments
    fun submitAPComments(st: StandardReviewCommentDto){
        val variables: MutableMap<String, Any> = HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val sc= SDReviewComments()

        sc.reviewId=st.id
        sc.standardId=st.standardId
        sc.title=st.title
        sc.standardNumber=st.standardNumber
        sc.documentType=st.documentType
        sc.dateFormed=st.dateFormed
        sc.circulationDate=st.circulationDate
        sc.closingDate=st.closingDate
        sc.nameOfTcSecretary=st.nameOfTcSecretary
        sc.justification=st.justification
        sc.edition=st.edition
        sc.nameOfRespondent=st.nameOfRespondent
        sc.positionOfRespondent=st.positionOfRespondent
        sc.nameOfOrganization=st.nameOfOrganization

        sc.commentTime = Timestamp(System.currentTimeMillis())

        sdReviewCommentsRepository.save(sc)

    }
    fun getProposalsComments(reviewId: Long): MutableList<SDReviewComments> {
        return sdReviewCommentsRepository.getProposalsComments(reviewId)
    }

    fun getStandardsForRecommendation(): MutableList<StandardReview> {
        return standardReviewRepository.getStandardsForRecommendation()
    }

    fun makeRecommendationsOnAdoptionProposal(st: StandardReviewRecommendationDto) : StandardReview {

        val reviewBody = StandardReview();
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val dateOfAnalysis = Timestamp(System.currentTimeMillis())
        val deadline: Timestamp = Timestamp.valueOf(dateOfAnalysis.toLocalDateTime().plusMonths(2))
        reviewBody.dateOfRecommendation= dateOfAnalysis
        reviewBody.recommendation= st.recommendation
        reviewBody.timeline=deadline
        val ispDetails = standardReviewRepository.save(reviewBody)
        standardReviewRepository.findByIdOrNull(st.reviewId)?.let { std ->
            with(std) {
                status = 1
                dateOfRecommendation=dateOfAnalysis
                timeline=deadline
                feedback=st.feedback

            }
            standardReviewRepository.save(std)
        }?: throw Exception("REVIEW NOT FOUND")

        return ispDetails

    }

    fun getStandardsForSpcAction(): MutableList<StandardReview> {
        return standardReviewRepository.getStandardsForSpcAction()
    }



    fun decisionOnStdDraft(
        sp: SpcStandardReviewCommentDto
    ) : ResponseMsg {
        val sr=StandardRequest()
        var requestNo=generateSRNumber("ENG")
        sr.submissionDate = Timestamp(System.currentTimeMillis())
        sr.createdOn = Timestamp(System.currentTimeMillis())
        sr.status = "NA"
        sr.levelOfStandard=sp.standardType

        sr.requestNumber = requestNo
        sr.rank = '3'.toString()
        val reqId= standardRequestRepository.save(sr)
        val pd= ComStdDraft()
        pd.requestId=reqId.id
        pd.title=sp.title
        pd.standardType=sp.standardType
        pd.uploadDate = commonDaoServices.getTimestamp()
        pd.status=4

        val drId=comStdDraftRepository.save(pd)

        standardReviewRepository.findByIdOrNull(sp.id)?.let { std ->
            with(std) {
                status = 2

            }
            standardReviewRepository.save(std)
        }?: throw Exception("REVIEW NOT FOUND")

        var feedback =sp.feedback
        var response=""
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val companyStandardRemarks= CompanyStandardRemarks()
        val decision=sp.accentTo
        val fName = loggedInUser.firstName
        val sName = loggedInUser.lastName
        val usersName = "$fName  $sName"
        companyStandardRemarks.requestId= sr.id
        companyStandardRemarks.remarks= sp.remarks
        companyStandardRemarks.status = 1.toString()
        companyStandardRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
        companyStandardRemarks.remarkBy = usersName
        companyStandardRemarks.role = "TC/KNW Secretary"
        companyStandardRemarks.standardType = sp.standardType
        //val deadline: Timestamp = Timestamp.valueOf(companyStandardRemarks.dateOfRemark!!.toLocalDateTime().plusMonths(2))

        if (decision == "Yes") {
            if (feedback != null) {
                if(feedback.equals(1)){
                    val cs=CompanyStandard()
                    cs.subject=sp.subject
                    cs.description=sp.description
                    cs.requestNumber=requestNo
                    cs.status=0
                    cs.uploadDate = Timestamp(System.currentTimeMillis())
                    cs.draftId=drId.id
                    cs.requestId=sr.id
                    cs.title=sp.title
                    cs.standardType=sp.standardType

                    val draftStandard= companyStandardRepository.save(cs)

                    companyStandardRemarksRepository.save(companyStandardRemarks)

                    var userList= companyStandardRepository.getHopEmailList()

                    //email to Head of publishing
                    val targetUrl = "https://kimsint.kebs.org/hopTasks";
                    userList.forEach { item->
                        //val recipient="stephenmuganda@gmail.com"
                        val recipient= item.getUserEmail()
                        val subject = "Kenya Standard"
                        val messageBody= "Dear ${item.getFirstName()} ${item.getLastName()}, A standard has been uploaded.Login to KIEMS $targetUrl to initiate piblishing"
                        if (recipient != null) {
                            notifications.sendEmail(recipient, subject, messageBody)
                        }
                    }
                }else if (feedback.equals(2)){
                    val cs=CompanyStandard()
                    cs.subject=sp.subject
                    cs.description=sp.description
                    cs.requestNumber=requestNo
                    cs.status=0
                    cs.uploadDate = Timestamp(System.currentTimeMillis())
                    cs.draftId=drId.id
                    cs.requestId=sr.id
                    cs.title=sp.title
                    cs.standardType=sp.standardType

                    val draftStandard= companyStandardRepository.save(cs)

                    companyStandardRemarksRepository.save(companyStandardRemarks)

                    var userList= companyStandardRepository.getHopEmailList()

                    //email to Head of publishing
                    val targetUrl = "https://kimsint.kebs.org/hopTasks";
                    userList.forEach { item->
                        //val recipient="stephenmuganda@gmail.com"
                        val recipient= item.getUserEmail()
                        val subject = "Kenya Standard"
                        val messageBody= "Dear ${item.getFirstName()} ${item.getLastName()}, A standard has been uploaded.Login to KIEMS $targetUrl to initiate piblishing"
                        if (recipient != null) {
                            notifications.sendEmail(recipient, subject, messageBody)
                        }
                    }
                }else if (feedback.equals(3)){
                    if(sp.standardType=="Kenya Standard"){
                        standardRequestRepository.findByIdOrNull(reqId.id)?.let { ks ->
                            with(ks) {
                                status = "Assigned to Tc Sec"

                            }
                            standardRequestRepository.save(ks)
                        }?: throw Exception("REVIEW NOT FOUND")
                    }else{
                        standardRequestRepository.findByIdOrNull(reqId.id)?.let { ks ->
                            with(ks) {
                                status = "To Be Defined"

                            }
                            standardRequestRepository.save(ks)
                        }?: throw Exception("REVIEW NOT FOUND")
                    }

                }else if(feedback.equals(4)){
                    val cs=CompanyStandard()
                    cs.subject=sp.subject
                    cs.description=sp.description
                    cs.requestNumber=requestNo
                    cs.status=0
                    cs.uploadDate = Timestamp(System.currentTimeMillis())
                    cs.draftId=drId.id
                    cs.requestId=sr.id
                    cs.title=sp.title
                    cs.standardType=sp.standardType

                    val draftStandard= companyStandardRepository.save(cs)

                    companyStandardRemarksRepository.save(companyStandardRemarks)

                    var userList= companyStandardRepository.getHopEmailList()

                    //email to Head of publishing
                    val targetUrl = "https://kimsint.kebs.org/hopTasks";
                    userList.forEach { item->
                        //val recipient="stephenmuganda@gmail.com"
                        val recipient= item.getUserEmail()
                        val subject = "Kenya Standard"
                        val messageBody= "Dear ${item.getFirstName()} ${item.getLastName()}, A standard has been uploaded.Login to KIEMS $targetUrl to initiate piblishing"
                        if (recipient != null) {
                            notifications.sendEmail(recipient, subject, messageBody)
                        }
                    }
                }
            }



        } else if (decision == "No") {
            standardRepository.findByIdOrNull(sp.id)?.let { sd ->

                with(sd) {
                    status = 3
                }
                standardRepository.save(sd)
                companyStandardRemarksRepository.save(companyStandardRemarks)


            } ?: throw Exception("DRAFT NOT FOUND")
        }

        return ResponseMsg(response)
    }


    fun getStdForEditing(): MutableList<ComStandard> {
        return companyStandardRepository.getStdForEditing()
    }

    fun submitDraftForEditing(isDraftDto: CSDraftDto ) : CompanyStandard
    {
        val variable: MutableMap<String, Any> = java.util.HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val com=CompanyStandard()

        companyStandardRepository.findByIdOrNull(isDraftDto.id)?.let { companyStandard ->
            with(companyStandard) {
                title=isDraftDto.title
                departmentId = isDraftDto.departmentId
                subject=isDraftDto.subject
                description=isDraftDto.description
                requestNumber=isDraftDto.requestNumber
                status=1
                documentType=isDraftDto.documentType
                draftId=isDraftDto.draftId
                requestId=isDraftDto.requestId
                scope=isDraftDto.scope
                normativeReference=isDraftDto.normativeReference
                symbolsAbbreviatedTerms=isDraftDto.symbolsAbbreviatedTerms
                clause=isDraftDto.clause
                documentType=isDraftDto.documentType
                special=isDraftDto.special

            }
            companyStandardRepository.save(companyStandard)

        }?: throw Exception("STANDARD NOT FOUND")

        var userList= companyStandardRepository.getHopEmailList()

        //email to Head of publishing
        val targetUrl = "https://kimsint.kebs.org/";
        userList.forEach { item->
            //val recipient="stephenmuganda@gmail.com"
            val recipient= item.getUserEmail()
            val subject = "Standard"
            val messageBody= "Dear ${item.getFirstName()} ${item.getLastName()}, A standard has been uploaded."
            if (recipient != null) {
                notifications.sendEmail(recipient, subject, messageBody)
            }
        }
        return com
    }

    fun generateSRNumber(departmentAbbrv: String?): String {
        val allRequests = standardRequestRepository.findAllByOrderByIdDesc()
        var lastId: String? = "0"
        var finalValue = 1

        for (item in allRequests) {
            lastId = item.requestNumber
            break
        }
        if (lastId != "0") {
            val strs = lastId?.split(":")?.toTypedArray()

            val firstPortion = strs?.get(0)

            val lastPortArray = firstPortion?.split("/")?.toTypedArray()

            val intToIncrement = lastPortArray?.get(1)

            finalValue = (intToIncrement?.toInt()!!)
            finalValue += 1
        }


        val year = Calendar.getInstance()[Calendar.YEAR]

        return "$departmentAbbrv/$finalValue:$year"
    }

}