package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std

import com.google.gson.Gson
import mu.KotlinLogging
import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.StandardsLevyBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.dto.std.*
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.std.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.multipart.MultipartFile
import java.sql.Timestamp
import java.util.*

@Service
class WorkshopAgreementService
    (private val runtimeService: RuntimeService,
     private val taskService: TaskService,
     @Qualifier("processEngine") private val processEngine: ProcessEngine,
     private val repositoryService: RepositoryService,
     private val commonDaoServices: CommonDaoServices,
     private val nwaJustificationRepository: NwaJustificationRepository,
     private val nwaDisDtJustificationRepository: NWADISDTJustificationRepository,
     private val nwaPreliminaryDraftRepository: NwaPreliminaryDraftRepository,
     private val technicalCommitteeRepository: TechnicalCommitteeRepository,
     private val technicalComListRepository: TechnicalComListRepository,
     private val departmentRepository: DepartmentRepository,
     private val departmentListRepository: DepartmentListRepository,
     private val sdNwaUploadsEntityRepository: DatKebsSdNwaUploadsEntityRepository,
     private val nwaWorkshopDraftRepository: NwaWorkShopDraftRepository,
     private val nwaStandardRepository: NwaStandardRepository,
     private val nwaGazettementRepository: NwaGazettementRepository,
     private val nwaGazetteNoticeRepository: NwaGazetteNoticeRepository,
     private val sdDiJustificationUploadsRepository: SDDIJustificationUploadsRepository,
     private val nwaPreliminaryDraftUploadsRepository: NWAPreliminaryDraftUploadsRepository,
     private val nwaStandardUploadsRepository: NWAStandardUploadsRepository,
     private val nwaWorkShopDraftUploadsRepository: NWAWorkShopDraftUploadsRepository,
     private val notifications: Notifications,
     private val bpmnService: StandardsLevyBpmn,
     private val userListRepository: UserListRepository,
     private val standardNwaRemarksRepository: StandardNwaRemarksRepository,
     private val companyStandardRepository: CompanyStandardRepository,
     private val standardRepository: StandardRepository,
     private val comStdDraftRepository: ComStdDraftRepository,
     private val comStandardDraftUploadsRepository: ComStandardDraftUploadsRepository,
     private val companyStandardRemarksRepository: CompanyStandardRemarksRepository,
     private val standardRequestRepository: StandardRequestRepository,
     private val applicationMapProperties: ApplicationMapProperties,
     ) {

    val callUrl=applicationMapProperties.mapKebsLevyUrl

    fun getWorkshopStandards(): MutableList<StandardRequest>
    {
        return standardRequestRepository.getWorkshopStandards()
    }

    fun getKNWDepartments(): MutableList<Department>
    {
        return departmentRepository.findAll()
    }

    fun getKNWCommittee(): MutableList<TechnicalCommittee>
    {
        return technicalCommitteeRepository.findAll()
    }
    fun prepareJustification(nwaJustificationDto: NwaJustificationDto) : NWAJustification {
        val nwaJustification= NWAJustification()
        val dateOfSubmission= Timestamp(System.currentTimeMillis())
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        nwaJustification.knw=nwaJustificationDto.knw
        nwaJustification.dateOfMeeting=nwaJustificationDto.dateOfMeeting
        nwaJustification.sl=nwaJustificationDto.sl
        nwaJustification.requestedBy=nwaJustificationDto.requestedBy
        nwaJustification.issuesAddressed=nwaJustificationDto.issuesAddressed
        nwaJustification.acceptanceDate=nwaJustificationDto.acceptanceDate
        nwaJustification.referenceMaterial=nwaJustificationDto.referenceMaterial
        nwaJustification.department=nwaJustificationDto.department
        nwaJustification.status= 0.toString()
        nwaJustification.remarks=nwaJustificationDto.remarks
        nwaJustification.requestId=nwaJustificationDto.requestId
        val deadline: Timestamp = Timestamp.valueOf(dateOfSubmission.toLocalDateTime().plusDays(90))
        //nwaJustification.assignedTo= companyStandardRepository.getKnwSecId()
        nwaJustification.submissionDate = dateOfSubmission
        nwaJustification.deadLine=deadline

        nwaJustification.requestNumber = getRQNumber()

        nwaJustification.knwCommittee= technicalComListRepository.findNameById(nwaJustification.knw?.toLong())

        nwaJustification.departmentName= departmentListRepository.findNameById(nwaJustification.department?.toLong())

        standardRequestRepository.findByIdOrNull(nwaJustificationDto.requestId)?.let { standard ->

            with(standard) {
                status = "Workshop Justification Approval"

            }
            standardRequestRepository.save(standard)
        }?: throw Exception("REQUEST NOT FOUND")

        val justification =nwaJustificationRepository.save(nwaJustification)

        return justification

    }

    fun getJustification(requestId: Long): MutableList<NWAJustification>
    {
        return nwaJustificationRepository.getJustification(requestId)
    }

    fun getWorkshopJustification(): MutableList<StandardRequest>
    {
        return standardRequestRepository.getWorkshopJustification()
    }

    fun decisionOnJustification(
        nwaDecisionOnJustificationDto: NwaDecisionOnJustificationDto
    ) : StandardRequest {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val decision = nwaDecisionOnJustificationDto.accentTo
        val st= StandardRequest()
        val sr= StandardNwaRemarks()

        val fname = loggedInUser.firstName
        val sname = loggedInUser.lastName
        val usersName = "$fname  $sname"
        sr.justificationID=nwaDecisionOnJustificationDto.requestId
        sr.remarks = nwaDecisionOnJustificationDto.comments
        sr.status = 1.toString()
        sr.dateOfRemark = Timestamp(System.currentTimeMillis())
        sr.remarkBy = usersName
        if (decision == "Yes") {
          var cdNumber=getCDNumber()
//            val gson = Gson()
//            KotlinLogging.logger { }.info { "CD Number" + gson.toJson(cdNumber) }

            standardRequestRepository.findByIdOrNull(nwaDecisionOnJustificationDto.requestId)?.let { sts ->

                with(sts) {
                    nwaCdNumber= cdNumber
                    status="Prepare Preliminary Draft"

                }
                standardRequestRepository.save(sts)
                standardNwaRemarksRepository.save(sr)
            }?: throw Exception("REQUEST NOT FOUND")
        }else if (decision == "No"){
            standardRequestRepository.findByIdOrNull(nwaDecisionOnJustificationDto.requestId)?.let { std ->
            with(std) {
                status="Assigned To TC Sec"

            }
            standardRequestRepository.save(std)
            standardNwaRemarksRepository.save(sr)
        }?: throw Exception("REQUEST NOT FOUND")

        }

        return st
    }

    fun getWorkshopForPDraft(): MutableList<StandardRequest>
    {
        return standardRequestRepository.getWorkshopForPDraft()
    }


    // Prepare Preliminary Draft
    fun preparePreliminaryDraft(
        wpd: WorkshopPreliminaryDraft
    ) : ComStdDraft {
        val pd= ComStdDraft()
        val variable: MutableMap<String, Any> = HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        pd.requestId=wpd.requestId
        pd.title=wpd.title
        pd.scope=wpd.scope
        pd.normativeReference=wpd.normativeReference
        pd.symbolsAbbreviatedTerms=wpd.symbolsAbbreviatedTerms
        pd.clause=wpd.clause
        pd.special=wpd.special
        pd.workShopDate=wpd.workShopDate
        pd.standardType="Kenya Standard"
        pd.uploadDate = commonDaoServices.getTimestamp()
        pd.status=0
        val nwaDetails = comStdDraftRepository.save(pd)
        standardRequestRepository.findByIdOrNull(wpd.requestId)?.let { standard ->

            with(standard) {
                status="Preliminary Draft Prepared"

            }
            standardRequestRepository.save(standard)
        }?: throw Exception("REQUEST NOT FOUND")
        return nwaDetails
    }

    fun getWorkShopDraftForEditing(): MutableList<NwaRequest>
    {
        return standardRequestRepository.getWorkShopDraftForEditing()
    }

    fun getPreparedPD(): MutableList<StandardRequest>
    {
        return standardRequestRepository.getPreparedPD()
    }

//    fun getWorkShopDraftForEditing(requestId: Long): MutableList<ComStandard> {
//        return companyStandardRepository.getWorkShopDraftForEditing(requestId)
//    }

    fun editPreliminaryDraft(
        wpd: WorkshopPreliminaryDraft
    ) : ComStdDraft {
        val pd= ComStdDraft()
        val variable: MutableMap<String, Any> = HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        pd.requestId=wpd.requestId
        pd.title=wpd.title
        pd.scope=wpd.scope
        pd.normativeReference=wpd.normativeReference
        pd.symbolsAbbreviatedTerms=wpd.symbolsAbbreviatedTerms
        pd.clause=wpd.clause
        pd.special=wpd.special
        pd.workShopDate=wpd.workShopDate
        pd.standardType="Kenya Standard"
        pd.uploadDate = commonDaoServices.getTimestamp()
        pd.status=0
        pd.id=wpd.id
        val nwaDetails = comStdDraftRepository.save(pd)
        standardRequestRepository.findByIdOrNull(wpd.requestId)?.let { standard ->

            with(standard) {
                status="Preliminary Draft Prepared"

            }
            standardRequestRepository.save(standard)
        }?: throw Exception("REQUEST NOT FOUND")
        return nwaDetails
    }

    fun getWorkShopStdDraft(): MutableList<ComStdDraft> {
        return comStdDraftRepository.getWorkShopStdDraft()
    }



    fun decisionOnStdDraft(
        workshopAgreementDecisionDto: WorkshopAgreementDecisionDto
    ) : ResponseMsg {
        var response=""
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val companyStandardRemarks= CompanyStandardRemarks()
        val decision=workshopAgreementDecisionDto.accentTo
        val fName = loggedInUser.firstName
        val sName = loggedInUser.lastName
        val usersName = "$fName  $sName"
        companyStandardRemarks.requestId= workshopAgreementDecisionDto.requestId
        companyStandardRemarks.remarks= workshopAgreementDecisionDto.remarks
        companyStandardRemarks.status = 1.toString()
        companyStandardRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
        companyStandardRemarks.remarkBy = usersName
        companyStandardRemarks.role = "TC/KNW Secretary"
        companyStandardRemarks.standardType = "Kenya Standard"
        //val deadline: Timestamp = Timestamp.valueOf(companyStandardRemarks.dateOfRemark!!.toLocalDateTime().plusMonths(2))

        if (decision == "Yes") {
            comStdDraftRepository.findByIdOrNull(workshopAgreementDecisionDto.id)?.let { comStdDraft ->
                with(comStdDraft) {
                    status = 4
                }
                val cs=CompanyStandard()
                cs.departmentId=workshopAgreementDecisionDto.departmentId
                cs.subject=workshopAgreementDecisionDto.subject
                cs.description=workshopAgreementDecisionDto.description
                cs.requestNumber=workshopAgreementDecisionDto.requestNumber
                cs.status=0
                cs.uploadDate = Timestamp(System.currentTimeMillis())
                cs.draftId=workshopAgreementDecisionDto.id
                cs.requestId=workshopAgreementDecisionDto.requestId
                cs.title=workshopAgreementDecisionDto.title
                cs.standardType="Kenya Standard"

                val draftStandard= companyStandardRepository.save(cs)

                comStdDraftRepository.save(comStdDraft)
                companyStandardRemarksRepository.save(companyStandardRemarks)
                response="Draft Approved"
                var userList= companyStandardRepository.getHopEmailList()

                //email to Head of publishing
                val targetUrl="${callUrl}/hopTasks"
                userList.forEach { item->
                    //val recipient="stephenmuganda@gmail.com"
                    val recipient= item.getUserEmail()
                    val subject = "Kenya Standard"
                    val messageBody= "Dear ${item.getFirstName()} ${item.getLastName()}, A standard has been uploaded.Login to KIEMS $targetUrl to initiate piblishing"
                    if (recipient != null) {
                        notifications.sendEmail(recipient, subject, messageBody)
                    }
                }

            }?: throw Exception("DRAFT NOT FOUND")

        } else if (decision == "No") {
            comStdDraftRepository.findByIdOrNull(workshopAgreementDecisionDto.id)?.let { comStdDraft ->

                with(comStdDraft) {
                    status = 1
                }
                comStdDraftRepository.save(comStdDraft)
                companyStandardRemarksRepository.save(companyStandardRemarks)

                standardRequestRepository.findByIdOrNull(workshopAgreementDecisionDto.requestId)?.let { standard ->

                    with(standard) {
                        status="Make Changes to Preliminary Draft"
                    }
                    standardRequestRepository.save(standard)
                    response="Draft Not Approved"
                }?: throw Exception("REQUEST NOT FOUND")

            } ?: throw Exception("DRAFT NOT FOUND")
        }

        return ResponseMsg(response)
    }

    fun getWorkShopStdForEditing(): MutableList<ComStandard> {
        return companyStandardRepository.getWorkShopStdForEditing()
    }





    fun submitDraftForEditing(isDraftDto: CSDraftDto ) : CompanyStandard
    {
        val variable: MutableMap<String, Any> = HashMap()
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
        //val targetUrl = "https://kimsint.kebs.org/";
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

    fun getRQNumber(): String
    {
        val allRequests =nwaJustificationRepository.findAllByOrderByIdDesc()

        var lastId:String?="0"
        var finalValue =1
        var startId="RQ"


        for(item in allRequests){
            println(item)
            lastId = item.requestNumber
            break
        }

        if(lastId != "0")
        {
            val strs = lastId?.split(":")?.toTypedArray()

            val firstPortion = strs?.get(0)

            val lastPortArray = firstPortion?.split("/")?.toTypedArray()

            val intToIncrement =lastPortArray?.get(1)

            finalValue = (intToIncrement?.toInt()!!)
            finalValue += 1
        }


        val year = Calendar.getInstance()[Calendar.YEAR]

        return "$startId/$finalValue:$year"
    }

    fun getCDNumber(): String
    {
        val allRequests =nwaStandardRepository.findAllByOrderByIdDesc()

        var lastId:String?="0"
        var finalValue =1
        var startId="NWA"


        for(item in allRequests){
            println(item)
            lastId = item.ksNumber
            break
        }

        if(lastId != "0")
        {
            val strs = lastId?.split(":")?.toTypedArray()

            val firstPortion = strs?.get(0)

            val lastPortArray = firstPortion?.split("/")?.toTypedArray()

            val intToIncrement =lastPortArray?.get(1)

            finalValue = (intToIncrement?.toInt()!!)
            finalValue += 1
        }


        val year = Calendar.getInstance()[Calendar.YEAR]

        return "$startId/$finalValue:$year"
    }

}