package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std

import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.StandardsLevyBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.dto.std.NWAJustificationDecision
import org.kebs.app.kotlin.apollo.common.dto.std.NwaJustificationDto
import org.kebs.app.kotlin.apollo.common.dto.std.ProcessInstanceResponseValue
import org.kebs.app.kotlin.apollo.common.dto.std.WorkShopAgreementTasks
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.std.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
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
     private val standardRequestRepository: StandardRequestRepository) {

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
        nwaJustification.meetingDate=nwaJustificationDto.meetingDate
        nwaJustification.knwSecretary=nwaJustificationDto.knwSecretary
        nwaJustification.sl=nwaJustificationDto.sl
        nwaJustification.requestedBy=nwaJustificationDto.requestedBy
        nwaJustification.issuesAddressed=nwaJustificationDto.issuesAddressed
        nwaJustification.knwAcceptanceDate=nwaJustificationDto.knwAcceptanceDate
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
    fun decisionOnJustification(
        nwaJustificationDecision: NWAJustificationDecision,
        standardNwaRemarks: StandardNwaRemarks
    ) : String {
        val variables: MutableMap<String, Any> = java.util.HashMap()
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val decision = nwaJustificationDecision.decision
        val st= StandardRequest()

        val fname = loggedInUser.firstName
        val sname = loggedInUser.lastName
        val usersName = "$fname  $sname"
        standardNwaRemarks.justificationID=nwaJustificationDecision.approvalID
        standardNwaRemarks.remarks = nwaJustificationDecision.comments
        standardNwaRemarks.status = 1.toString()
        standardNwaRemarks.dateOfRemark = Timestamp(System.currentTimeMillis())
        standardNwaRemarks.remarkBy = usersName
        if (decision == "Yes") {


            standardRequestRepository.findByIdOrNull(nwaJustificationDecision.approvalID)?.let { standard ->

                with(standard) {
                    nwaStdNumber= getKSNumber()
                    status="Prepare Preliminary Draft"

                }
                standardRequestRepository.save(standard)
                standardNwaRemarksRepository.save(standardNwaRemarks)
            }?: throw Exception("REQUEST NOT FOUND")
        }else if (decision == "No"){
            standardRequestRepository.findByIdOrNull(nwaJustificationDecision.approvalID)?.let { std ->
            with(std) {
                status="Assigned To TC Sec"

            }
            standardRequestRepository.save(std)
            standardNwaRemarksRepository.save(standardNwaRemarks)
        }?: throw Exception("REQUEST NOT FOUND")

        }

        return "Approved"
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

    fun getKSNumber(): String
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