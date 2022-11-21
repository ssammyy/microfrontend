package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std

import mu.KotlinLogging
import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.engine.repository.Deployment
import org.flowable.task.api.Task
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.StandardsLevyBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.dto.std.*
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.std.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.Timestamp
import java.util.*
import kotlin.collections.HashMap

@Service
class NationalWorkshopAgreementService(
    private val runtimeService: RuntimeService,
    private val taskService: TaskService,
    @Qualifier("processEngine") private val processEngine: ProcessEngine,
    private val repositoryService: RepositoryService,
    private val commonDaoServices: CommonDaoServices,
    private val nwaJustificationRepository: NwaJustificationRepository,
    private val nwaPreliminaryDraftRepository: NwaPreliminaryDraftRepository,
    private val technicalCommitteeRepository: TechnicalCommitteeRepository,
    private val technicalComListRepository: TechnicalComListRepository,
    private val departmentRepository: DepartmentRepository,
    private val departmentListRepository: DepartmentListRepository,
    private val nwaWorkshopDraftRepository: NwaWorkShopDraftRepository,
    private val standardRepository: StandardRepository,
    private val nwaGazettementRepository: NwaGazettementRepository,
    private val notifications: Notifications,
    private val bpmnService: StandardsLevyBpmn,
    private val userListRepository: UserListRepository,
    private val standardNwaRemarksRepository: StandardNwaRemarksRepository,
) {


    fun getKNWDepartments(): MutableList<Department>
    {
        return departmentRepository.findAll()
    }

    fun getKNWCommittee(): MutableList<TechnicalCommittee>
    {
        return technicalCommitteeRepository.findAll()
    }

    fun prepareJustification(nwaJustification: NWAJustification) : NWAJustification {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        nwaJustification.knw=nwaJustification.knw
        nwaJustification.dateOfMeeting=nwaJustification.dateOfMeeting
        nwaJustification.knwSecretary=nwaJustification.knwSecretary
        nwaJustification.sl=nwaJustification.sl
        nwaJustification.requestedBy=nwaJustification.requestedBy
        nwaJustification.issuesAddressed=nwaJustification.issuesAddressed
        nwaJustification.acceptanceDate=nwaJustification.acceptanceDate
        nwaJustification.referenceMaterial=nwaJustification.referenceMaterial
        nwaJustification.department=nwaJustification.department
        nwaJustification.justificationStatus=0
        nwaJustification.remarks=nwaJustification.remarks

        nwaJustification.submissionDate = Timestamp(System.currentTimeMillis())

        nwaJustification.requestNumber = nwaJustification.requestNumber
        nwaJustification.knwCommittee = technicalComListRepository.findNameById(nwaJustification.knw?.toLong())
        nwaJustification.departmentName = departmentListRepository.findNameById(nwaJustification.department?.toLong())
        //var justificationUploadId =sdNwaUploadsEntityRepository.getMaxUploadedID()
        return nwaJustificationRepository.save(nwaJustification)


    }


    fun getNwaJustification(): MutableList<KnwaJustification> {
        return nwaJustificationRepository.getNwaJustification()
    }


    // Decision on Justification
    fun decisionOnJustification(
        nwaJustification: NWAJustification,
        standardNwaRemarks: StandardNwaRemarks
    ) : String {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        nwaJustification.accentTo=nwaJustification.accentTo
        val decision=nwaJustification.accentTo

        val fName = loggedInUser.firstName
        val sName = loggedInUser.lastName
        val usersName = "$fName  $sName"
        standardNwaRemarks.justificationID=standardNwaRemarks.justificationID
        standardNwaRemarks.remarks=standardNwaRemarks.remarks
        standardNwaRemarks.status = 1.toString()
        standardNwaRemarks.role = "KNW SEC"
        standardNwaRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
        standardNwaRemarks.remarkBy = usersName

        if (decision === "Yes") {
            nwaJustificationRepository.findByIdOrNull(nwaJustification.id)?.let { nwaJustification ->
                val valueFound =getCDNumber()
                with(nwaJustification) {
                    justificationStatus = 1
                    cdNumber = valueFound.first
                    cdn = valueFound.second

                }
                nwaJustificationRepository.save(nwaJustification)
                standardNwaRemarksRepository.save(standardNwaRemarks)
            }?: throw Exception("JUSTIFICATION NOT FOUND")

        } else if (decision === "No") {
            nwaJustificationRepository.findByIdOrNull(nwaJustification.id)?.let { nwaJustification ->

                with(nwaJustification) {
                    justificationStatus = 2
                }
                nwaJustificationRepository.save(nwaJustification)
                standardNwaRemarksRepository.save(standardNwaRemarks)
            } ?: throw Exception("JUSTIFICATION NOT FOUND")

        }

        return "Actioned"
    }

    fun getCDNumber(): Pair<String, Long>
    {
        //val allRequests: Int
        var allRequests =nwaJustificationRepository.getMaxCDN()

        var startId="NWA"

        allRequests = allRequests.plus(1)

        val year = Calendar.getInstance()[Calendar.YEAR]

        return Pair("$startId/$allRequests:$year", allRequests)
    }

    fun getApprovedJustification(): MutableList<KnwaJustification> {
        return nwaJustificationRepository.getApprovedJustification()
    }

    // Prepare Preliminary Draft
    fun preparePreliminaryDraft(
        nwaPreliminaryDraft: NWAPreliminaryDraft
    ) : NWAPreliminaryDraft
    {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        nwaPreliminaryDraft.title=nwaPreliminaryDraft.title
        nwaPreliminaryDraft.scope=nwaPreliminaryDraft.scope
        nwaPreliminaryDraft.normativeReference=nwaPreliminaryDraft.normativeReference
        nwaPreliminaryDraft.symbolsAbbreviatedTerms=nwaPreliminaryDraft.symbolsAbbreviatedTerms
        nwaPreliminaryDraft.clause=nwaPreliminaryDraft.clause
        nwaPreliminaryDraft.special=nwaPreliminaryDraft.special
        nwaPreliminaryDraft.datePdPrepared = commonDaoServices.getTimestamp()
        nwaPreliminaryDraft.justificationNumber= nwaPreliminaryDraft.justificationNumber
        nwaPreliminaryDraft.workShopDate= nwaPreliminaryDraft.workShopDate
        nwaPreliminaryDraft.status=0
        val fName=loggedInUser.firstName
        val lName=loggedInUser.lastName
        val name= "$fName  $lName"
        nwaPreliminaryDraft.preparedBy=name
        nwaPreliminaryDraft.preparedById=loggedInUser.id

        nwaJustificationRepository.findByIdOrNull(nwaPreliminaryDraft.justificationNumber)?.let { nwaJustification ->
            with(nwaJustification) {
                justificationStatus = 3

            }
            nwaJustificationRepository.save(nwaJustification)
            nwaPreliminaryDraftRepository.save(nwaPreliminaryDraft)
        }?: throw Exception("JUSTIFICATION NOT FOUND")

        return nwaPreliminaryDraft

    }

    fun getPreliminaryDraft(): MutableList<NwaPDraft> {
        return nwaPreliminaryDraftRepository.getPreliminaryDraft()
    }

    // Decision on PD
    fun decisionOnPreliminaryDraft(
        nwaPreliminaryDraft: NWAPreliminaryDraft,
        standardNwaRemarks: StandardNwaRemarks
    ) : String {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        nwaPreliminaryDraft.accentTo=nwaPreliminaryDraft.accentTo
        nwaPreliminaryDraft.title=nwaPreliminaryDraft.title
        nwaPreliminaryDraft.scope=nwaPreliminaryDraft.scope
        nwaPreliminaryDraft.normativeReference=nwaPreliminaryDraft.normativeReference
        nwaPreliminaryDraft.symbolsAbbreviatedTerms=nwaPreliminaryDraft.symbolsAbbreviatedTerms
        nwaPreliminaryDraft.clause=nwaPreliminaryDraft.clause
        nwaPreliminaryDraft.special=nwaPreliminaryDraft.special
        val decision=nwaPreliminaryDraft.accentTo

        val fName = loggedInUser.firstName
        val sName = loggedInUser.lastName
        val usersName = "$fName  $sName"
        standardNwaRemarks.justificationID=standardNwaRemarks.justificationID
        standardNwaRemarks.remarks=standardNwaRemarks.remarks
        standardNwaRemarks.status = 1.toString()
        standardNwaRemarks.role = "KNW SEC"
        standardNwaRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
        standardNwaRemarks.remarkBy = usersName

        if (decision === "Yes") {
            nwaPreliminaryDraftRepository.findByIdOrNull(nwaPreliminaryDraft.id)?.let { nwaPreliminaryDraft ->

                with(nwaPreliminaryDraft) {
                    status = 1
                }
                nwaPreliminaryDraftRepository.save(nwaPreliminaryDraft)
                standardNwaRemarksRepository.save(standardNwaRemarks)
            }?: throw Exception("PRELIMINARY DRAFT NOT FOUND")

        } else if (decision === "No") {
            nwaPreliminaryDraftRepository.findByIdOrNull(nwaPreliminaryDraft.id)?.let { nwaPreliminaryDraft ->

                with(nwaPreliminaryDraft) {
                    status = 2
                }
                nwaPreliminaryDraftRepository.save(nwaPreliminaryDraft)
                standardNwaRemarksRepository.save(standardNwaRemarks)
            } ?: throw Exception("PRELIMINARY DRAFT NOT FOUND")

        }

        return "Actioned"
    }

    fun getRejectedPreliminaryDraft(): MutableList<NwaPDraft> {
        return nwaPreliminaryDraftRepository.getRejectedPreliminaryDraft()
    }



}